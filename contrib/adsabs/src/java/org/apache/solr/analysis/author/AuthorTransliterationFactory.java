/**
 * 
 */
package org.apache.solr.analysis.author;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.solr.analysis.PersistingMapTokenFilterFactory;

/**
 * @author jluker
 *
 */
public class AuthorTransliterationFactory extends TokenFilterFactory {

    public void init(Map<String, String> args) {
	    super.init(args);
	}
	  
	/* (non-Javadoc)
	 * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	public AuthorTransliterationFilter create(TokenStream input) {
		return new AuthorTransliterationFilter(input);
	}

}
