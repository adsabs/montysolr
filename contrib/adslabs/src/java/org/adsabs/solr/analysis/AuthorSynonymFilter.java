package org.adsabs.solr.analysis;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorSynonymFilter extends TokenFilter {

    public static final Logger log = LoggerFactory.getLogger(AuthorSynonymFilter.class);
    public static final String TOKEN_TYPE_AUTHOR_CURATED_SYN = "AUTHOR_CURATED_SYN";
    private final AuthorSynonymMap synMap;  
    
	protected AuthorSynonymFilter(TokenStream input, AuthorSynonymMap synMap) {
		super(input);
		if (synMap == null)
			throw new IllegalArgumentException("map is required");
		this.synMap = synMap;
	    // just ensuring these attributes exist...
		this.termAtt = addAttribute(CharTermAttribute.class);
		this.posIncrAtt = addAttribute(PositionIncrementAttribute.class);
		this.synonymStack = new Stack<String>();
		this.typeAtt = addAttribute(TypeAttribute.class);
	}

	private Stack<String> synonymStack;
	private AttributeSource.State current;
	
	private final CharTermAttribute termAtt;
	private final PositionIncrementAttribute posIncrAtt;
    private final TypeAttribute typeAtt;
    
	@Override
	public boolean incrementToken() throws IOException {
		if (this.synonymStack.size() > 0) {
			String syn = this.synonymStack.pop();
			log.debug("indexing synonym: " + syn);
			this.restoreState(this.current);
			this.termAtt.setEmpty();
			this.termAtt.append(syn);
			this.posIncrAtt.setPositionIncrement(0);
			this.typeAtt.setType(TOKEN_TYPE_AUTHOR_CURATED_SYN);
			return true;
		}
		
	    if (!input.incrementToken()) return false;
	    
	    if (this.getSynonyms()) {
	    	this.current = this.captureState();
	    }
	    
    	return true;
	}
	
	private boolean getSynonyms() {
	    String authorTok = termAtt.toString();
	    log.debug("looking for synonyms for " + authorTok);
	    Pattern p = Pattern.compile(authorTok);
	    List<String> synonyms = this.synMap.get(p);
	    if (synonyms != null && synonyms.size() > 0) {
		    log.debug("synonyms: " + synonyms);
		    for (String s : synonyms) {
		    	synonymStack.push(s);
		    }
	        return true;
	    }
	    return false;
	}
}
