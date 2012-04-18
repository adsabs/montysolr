/**
 * 
 */
package org.adsabs.solr.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.adsabs.solr.AuthorUtils;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jluker
 *
 */
public class AuthorAutoSynonymFilter extends TokenFilter {

    public static final Logger log = LoggerFactory.getLogger(AuthorAutoSynonymFilter.class);
    public static final String TOKEN_TYPE_AUTHOR_AUTO_SYN = "AUTHOR_AUTO_SYN";
    
	public AuthorAutoSynonymFilter(TokenStream input) {
		super(input);
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
	
	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public boolean incrementToken() throws IOException {
		
		if (this.synonymStack.size() > 0) {
			String syn = this.synonymStack.pop();
			log.debug("indexing synonym: " + syn);
			this.restoreState(this.current);
			this.termAtt.setEmpty();
			this.termAtt.append(syn);
			this.posIncrAtt.setPositionIncrement(0);
			this.typeAtt.setType(TOKEN_TYPE_AUTHOR_AUTO_SYN);
			return true;
		}
		
	    if (!input.incrementToken()) return false;
	    
	    if (this.genSynonyms()) {
	    	this.current = this.captureState();
	    }
	    
    	return true;
	}
	
	private boolean genSynonyms() {
	    String authorName = termAtt.toString();
	    log.debug("generating synonyms for " + authorName);
	    ArrayList<String> synonyms = AuthorUtils.genSynonyms(authorName);
	    if (synonyms.size() > 0) {
		    log.debug("synonyms: " + synonyms);
		    for (String s : synonyms) {
		    	synonymStack.push(s);
		    }
	        return true;
	    }
	    return false;
	}

}
