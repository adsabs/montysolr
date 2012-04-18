/**
 * 
 */
package org.adsabs.solr.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

/**
 * @author jluker
 *
 */
public class AuthorNormalizeFilterFactory extends BaseTokenFilterFactory {

	/* (non-Javadoc)
	 * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	@Override
	public AuthorNormalizeFilter create(TokenStream input) {
		return new AuthorNormalizeFilter(input);
	}
}
