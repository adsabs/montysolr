package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * rchyla: I have decided to make all the author search use wildcards instead
 * of regexes (WHENEVER it is possible), therefore this filter will
 * translate the regex into wildcards (where possible).
 * 
 */
public final class AuthorQueryVariationsFilter extends TokenFilter {
	
    public static final Logger log = LoggerFactory.getLogger(AuthorQueryVariationsFilter.class);
    
    public AuthorQueryVariationsFilter(TokenStream input) {
		super(input);
		this.termAtt = addAttribute(CharTermAttribute.class);
		this.posIncrAtt = addAttribute(PositionIncrementAttribute.class);
		this.variationStack = new ArrayDeque<String>();
		this.typeAtt = addAttribute(TypeAttribute.class);
	}
    
	private Deque<String> variationStack;
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
			this.typeAtt.setType(AuthorUtils.AUTHOR_QUERY_VARIANT);
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
		      if (s.endsWith(".*") && !s.substring(0,s.length()-2).contains("\\b")) {
		        s = s.replace(".*", "*");
		      }
		      variationStack.add(s);
		    }
	        return true;
	    }
	    return false;
	}
	
	@Override
	  public void reset() throws IOException {
		super.reset();
		variationStack.clear();
		current = null;
	}
}
