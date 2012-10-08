package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.HashSet;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuthorQueryVariationsFilter extends TokenFilter {
	
    public static final Logger log = LoggerFactory.getLogger(AuthorQueryVariationsFilter.class);
    
    public AuthorQueryVariationsFilter(TokenStream input) {
		super(input);
		this.termAtt = addAttribute(CharTermAttribute.class);
		this.posIncrAtt = addAttribute(PositionIncrementAttribute.class);
		this.variationStack = new Stack<String>();
		this.typeAtt = addAttribute(TypeAttribute.class);
	}
    
	private Stack<String> variationStack;
	private AttributeSource.State current;
	
	private final CharTermAttribute termAtt;
	private final PositionIncrementAttribute posIncrAtt;
    private final TypeAttribute typeAtt;
	
	@Override
	public boolean incrementToken() throws IOException {
		if (this.variationStack.size() > 0) {
			String syn = this.variationStack.pop();
			this.restoreState(this.current);
			this.termAtt.setEmpty();
			this.termAtt.append(syn);
			this.posIncrAtt.setPositionIncrement(0);
			this.typeAtt.setType(AuthorUtils.TOKEN_TYPE_QUERY_VARIANT);
			return true;
		}
		
	    if (!input.incrementToken()) return false;
	    
	    if (this.genVariations()) {
	    	this.current = this.captureState();
	    }
	    
    	return true;
	}
	
	private boolean genVariations() {
	    String authorName = termAtt.toString();
	    //log.debug("generating variations for " + authorName);
	    HashSet<String> variations = AuthorQueryVariations.getQueryVariationsInclRegex(authorName);
	    if (variations.size() > 0) {
		    //log.debug("variations: " + variations);
		    for (String s : variations) {
		    	variationStack.push(s);
		    }
	        return true;
	    }
	    return false;
	}
}
