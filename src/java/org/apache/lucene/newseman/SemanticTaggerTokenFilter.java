package org.apache.lucene.newseman;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.newseman.SemanticTaggerTokenFilter.TokenState;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.AttributeReflector;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.AttributeSource.State;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;

/**
 * The SemanticTagger receives previously tokenized {@link TokenStream} and
 * enriches tokens with semantic categories (so called semes). This enrichment
 * is controlled/governed by the dictionary/taxonomy/ontology.
 * 
 * Since we call an external application SEMAN written in Python, this
 * {@link TokenFilter} cannot be considered fast. It also depends on the options
 * that you activate for SEMAN. For example, you can run part-of-speech tagging
 * (inside SEMAN) to improve the results of the translation.
 * 
 * 
 * 
 */
public class SemanticTaggerTokenFilter extends TokenFilter {

	/** The Token.type used to indicate semes to higher level filters. */
	public static final String SEM_TOKEN_TYPE = "SEM";
	public static final String POS_TOKEN_TYPE = "POS";

	private List<String> stack = null;
	private int index = 0;
	private AttributeSource.State current = null;
	private int todo = 0;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	private final SemanticTagAttribute semAtt = addAttribute(SemanticTagAttribute.class);

	private SemanticTagger seman;
	private boolean translationDone;
	private TokenStream translatedStream;
	private int maxBuffer = 4096;
	private int curTokenCounter = 0;
	private int exhaustedTokenCounter = 0;
	private List<TokenState> enhancedTokens = null;

	/**
	 * Creates an instance for the given underlying stream and synonym table.
	 * 
	 * @param input
	 *            the underlying child token stream
	 * @param seman
	 *            instance of {@link SemanticTagger} which will be called to
	 *            translate the input {@link TokenStream}
	 */
	public SemanticTaggerTokenFilter(TokenStream input, SemanticTagger seman) {

		super(input);

		if (input == null)
			throw new IllegalArgumentException("input must not be null");
		if (seman == null)
			throw new IllegalArgumentException("seman must not be null");

		this.seman = seman;
	}

	/** Returns the next token in the stream, or null at EOS. */
	@Override
	public final boolean incrementToken() throws IOException {
		
		while (todo > 0 && index<todo) { // pop from stack
			if (createToken(stack.get(index++), current)) {
				todo--;
				return true;
			}
		}

		if (!translatedStream.incrementToken()) {
			return false; // EOS; iterator exhausted
		}
		
		if (curTokenCounter >= exhaustedTokenCounter) {
			enhancedTokens = advanceInputTokenStream(); // advance the input token stream
			if (enhancedTokens==null) {
				return false;
			}
		}
		
		curTokenCounter++;
		
		TokenState ct = enhancedTokens.get(curTokenCounter);
		
		restoreState(ct.getState());
		
		if (semAtt.hasSemanticTags()) {
			stack = semAtt.getSemanticTags(); // push onto stack
			index = 0;
			current = captureState();
			todo = stack.size();
		}
		else {
			todo = 0;
			index = 0;
		}
		return true;
	}
	
	
	public String[][] getTranslations(String[][] tokens) throws IOException {
		return seman.translateTokenCollection(tokens);
	}

	private List<TokenState> advanceInputTokenStream() throws IOException {
		Map<Integer, Integer> mapping= new HashMap<Integer, Integer>(maxBuffer);
		List<TokenState> tokens = new ArrayList<TokenState>(maxBuffer);
		String[][] passedTokens = new String[maxBuffer][];
		
		
		// first collect the tokens
		int i = 0;
	    do {
	    	TokenState s = new TokenState(input.captureState());
	    	mapping.put(s.hashCode(), i);
	    	tokens.set(i, s);
	    	String[] token = {
	    			"id", String.valueOf(input.hashCode()),
	    			"token", termAtt.toString(),
	    	};
	    	passedTokens[i] = token;
	    	i++;
	    } while (input.incrementToken() && i < maxBuffer);
	    
	    exhaustedTokenCounter += i;
	    
	    if (i < maxBuffer) {
	    	translationDone = true;
	    }
	    
	    String[][] results = getTranslations(passedTokens);
	    
	    for (String[] r: results) {
	    	Integer id = Integer.valueOf(r[1]);
	    	if (r.length > 4) {
	    		String[] semes = r[5].split(" ");
	    		if (mapping.containsKey(id)) {
	    			TokenState token = tokens.get(mapping.get(id));
	    			token.setSemes(semes);
	    		}
	    		else {
	    			throw new IOException("SEMAN created a new token which is not in TokenStream");
	    		}
	    	}
	    }
	    
	    return tokens;
	}

	/**
	 * Creates and returns a token for the given synonym of the current input
	 * token; Override for custom (stateless or stateful) behavior, if desired.
	 * 
	 * @param synonym
	 *            a synonym for the current token's term
	 * @param current
	 *            the current token from the underlying child stream
	 * @return a new token, or null to indicate that the given synonym should be
	 *         ignored
	 */
	protected boolean createToken(String synonym, AttributeSource.State current) {
		restoreState(current);
		termAtt.setEmpty().append(synonym);
		typeAtt.setType(SEM_TOKEN_TYPE);
		posIncrAtt.setPositionIncrement(0);
		return true;
	}


	@Override
	public void reset() throws IOException {
		super.reset();
		stack = null;
		index = 0;
		current = null;
		todo = 0;
	}
	
	class TokenState {
		State state = null;
		String[] semes = null;
		TokenState(State state) {
			this.state = state;
		}
		public State getState() {
			return this.state;
		}
		public void setState(State state) {
			this.state = state;
		}
		public String[] getSemes() {
			return this.semes;
		}
		public void setSemes(String[] semes) {
			this.semes = semes;
		}
	}
	
}
