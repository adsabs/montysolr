/**
 * 
 */
package org.adsabs.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

/**
 * @author jluker
 *
 */
public class AuthorAutoSynonymFilterFactory extends BaseTokenFilterFactory {

	@Override
    public void init(Map<String, String> args) {
	    super.init(args);
	}
	  
	/* (non-Javadoc)
	 * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	@Override
	public AuthorAutoSynonymFilter create(TokenStream input) {
		return new AuthorAutoSynonymFilter(input);
	}

}
