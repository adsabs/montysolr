package newseman;

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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import newseman.SemanticTaggerTokenFilter.TokenState;

import org.apache.lucene.util.ArrayUtil;
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
 * The translation is done by a Pythonic library called SEMAN. Please see 
 * documentation of SEMAN for further details.
 * 
 * 
 * 
 */
public class SemanticTaggerTokenFilter extends TokenFilter {

	/** The Token.type used to indicate semes to higher level filters. */
	public static final String SEM_TOKEN_TYPE = "SEM";
	public static final String POS_TOKEN_TYPE = "POS";

	private String[] stack = null;
	private int index = 0;
	private AttributeSource.State current = null;
	private int todo = 0;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	private final SemanticTagAttribute semAtt = addAttribute(SemanticTagAttribute.class);

	protected SemanticTagger seman = null; // Seman wrapper
	protected int maxBuffer = 4096; // # of tokens we send to Seman in one go
	private List<TokenState> enhancedTokens = null;
	private Iterator<TokenState> etIterator = null;

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
	
	public SemanticTaggerTokenFilter(TokenStream input) {
		super(input);
		if (input == null)
			throw new IllegalArgumentException("input must not be null");
	}

	/** Returns the next token in the stream, or null at EOS. */
	@Override
	public final boolean incrementToken() throws IOException {
		
		while (todo > 0) { // pop semes from stack
			if (createToken(stack[index++], current)) {
				todo--;
				return true;
			}
		}
		

		if (etIterator==null || !etIterator.hasNext()) {
			if (!input.incrementToken()) {
				return false; // EOS; iterator exhausted
			}
			enhancedTokens = advanceInputTokenStream(); // advance the input token stream
			if (enhancedTokens==null) {
				return false;
			}
			etIterator = enhancedTokens.iterator();
		}
		
		
		if (etIterator.hasNext()) {
			TokenState ct = etIterator.next();
			restoreState(ct.getState());
			String[] semes = ct.getSemes();
			if (semes!=null) {
				stack = semes;
				index = 0;
				current = captureState();
				todo = stack.length;
			}
			else {
				todo = 0;
				index = 0;
			}
		}
		
		
		return true;
	}
	
	
	public String[][] getTranslations(String[][] tokens) throws IOException {
		return seman.translateTokenCollection(tokens);
	}

	private List<TokenState> advanceInputTokenStream() throws IOException {
		Map<String, Integer> mapping= new HashMap<String, Integer>(maxBuffer);
		List<TokenState> tokens = new ArrayList<TokenState>(maxBuffer);
		String[][] passedTokens = new String[maxBuffer][];
		//ArrayList<String[]>passedTokens = new ArrayList<String[]>(maxBuffer);

		
		// first collect the tokens
		passedTokens[0] = new String[]{"token", "id"}; // header
		int i = 0;
	    do {
	    	TokenState s = new TokenState(input.captureState());
	    	String id = Integer.toString(input.hashCode());
	    	mapping.put(id, i);
	    	tokens.add(s);
	    	String[] token = {termAtt.toString(), id};
	    	passedTokens[i] = token;
	    	//passedTokens.add(token);
	    	i++;
	    } while (input.incrementToken() && i < maxBuffer);
	    
	    String[][] results = getTranslations(passedTokens);
	    
	    String[] header = results[0];
		Integer sem_idx = null;
		Integer extra_sem_idx = null;
		Integer extra_surface_idx =  null;
	    for (int j=2;j<header.length;j++) {
	    	String h = header[j];
	    	if (h.equals("sem")) {
	    		sem_idx = j;
	    	}
	    	else if(h.equals("extrasem")) {
	    		extra_sem_idx = j;
	    	}
	    	else if(h.equals("extrasurface")) {
	    		extra_surface_idx = j;
	    	}
	    }
	    
	    for (String[] r: results) {
	    	if (r==null) break;
	    	
	    	String id = r[1];
	    	if (r.length > extra_sem_idx) { // multi-token seme
	    		String[] semes = r[extra_sem_idx].split(" ");
	    		// TODO: create a new state
	    		TokenState token = tokens.get(mapping.get(id));
	    		token.setOtherName(r[extra_surface_idx]);
	    		tokens.add(token);
	    	}
	    	else if (r.length > sem_idx) {
	    		String[] semes = r[sem_idx].split(" ");
	    		if (mapping.containsKey(id)) {
	    			TokenState token = tokens.get(mapping.get(id));
	    			token.setSemes(semes);
	    			tokens.add(token);
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
	protected boolean createToken(String sem, AttributeSource.State current) {
		restoreState(current);
		termAtt.setEmpty().append(sem);
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
		enhancedTokens = null;
		etIterator = null;
	}
	
	class TokenState {
		State state = null;
		String[] semes = null;
		String otherName = null;
		
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
		public String getOtherName() {
			return this.otherName;
		}
		public void setOtherName(String name) {
			this.otherName = name;
		}
	}
	
}
