/**
 * 
 */
package org.apache.solr.analysis.author;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * @author jluker
 *
 */
public final class AuthorNormalizeFilter extends TokenFilter {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	
	/**
	 * @param input
	 */
	public AuthorNormalizeFilter(TokenStream input) {
		super(input);
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public boolean incrementToken() throws IOException {
	    if (!input.incrementToken()) return false;
	    String normalized = AuthorUtils.normalizeAuthor(termAtt.toString());
	    termAtt.setEmpty().append(normalized);
	    typeAtt.setType(AuthorUtils.TOKEN_TYPE_AUTHOR);
        return true;
	}
}
