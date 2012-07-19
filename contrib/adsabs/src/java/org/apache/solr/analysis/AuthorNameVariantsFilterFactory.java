/**
 * 
 */
package org.apache.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.AuthorNameVariantsFilter;
import org.apache.lucene.analysis.synonym.WriteableTokenFilterFactory;

/**
 * @author jluker
 *
 */
public class AuthorNameVariantsFilterFactory extends WriteableTokenFilterFactory {

    public void init(Map<String, String> args) {
	    super.init(args);
	}
	  
	/* (non-Javadoc)
	 * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	public AuthorNameVariantsFilter create(TokenStream input) {
		return new AuthorNameVariantsFilter(input, getSynonymMap());
	}

}
