/**
 * 
 */
package org.apache.solr.analysis.author;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.WriteableTokenFilterFactory;

/**
 * @author jluker
 *
 */
public class AuthorTransliterationsCollectorFactory extends WriteableTokenFilterFactory {

    public void init(Map<String, String> args) {
	    super.init(args);
	}
	  
	/* (non-Javadoc)
	 * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	public AuthorTransliterationsCollectorFilter create(TokenStream input) {
		return new AuthorTransliterationsCollectorFilter(input, getSynonymMap());
	}

}
