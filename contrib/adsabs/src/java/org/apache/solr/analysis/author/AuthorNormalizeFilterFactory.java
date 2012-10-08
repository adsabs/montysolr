/**
 * 
 */
package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

/**
 * @author jluker
 *
 */
public class AuthorNormalizeFilterFactory extends TokenFilterFactory {

	/* (non-Javadoc)
	 * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	public AuthorNormalizeFilter create(TokenStream input) {
		return new AuthorNormalizeFilter(input);
	}
}
