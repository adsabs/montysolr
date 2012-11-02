/**
 * 
 */
package org.apache.solr.analysis.author;

import java.io.IOException;
import java.io.InputStream;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.PersistingMapTokenFilterFactory;
import org.apache.solr.common.SolrException;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jluker
 *
 */
public class AuthorSynonymFilterFactory extends PersistingMapTokenFilterFactory implements ResourceLoaderAware {

    public static final Logger log = LoggerFactory.getLogger(AuthorSynonymFilter.class);
    
    
	public void inform(ResourceLoader loader) {
		super.inform(loader);
		
		String synonyms = args.get("synonyms");
		if (synonyms == null)
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Missing required argument 'synonyms'.");
		
	    InputStream rules;
		try {
	    	rules = loader.openResource(synonyms);
	        if (rules == null) {
	      		throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "can't find " + synonyms);
	      	}
	        rules.close();
	    } catch (IOException e) {
	      	throw new RuntimeException(e);
	    }
	    
	}
	
	public TokenStream create(TokenStream input) {
		return new AuthorSynonymFilter(input, getSynonymMap());
	}
	
}