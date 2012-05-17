/**
 * 
 */
package org.adsabs.solr.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.WriteableTokenFilterFactory;
import org.apache.solr.common.ResourceLoader;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.util.plugin.ResourceLoaderAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jluker
 *
 */
public class AuthorSynonymFilterFactory extends WriteableTokenFilterFactory implements ResourceLoaderAware {

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
	
	/* obsolete
	public void parseRules(List<String> rules, WriteableSynonymMap synMap) {
		
		for (String rule : rules) {
			List<String> mapping = StrUtils.splitSmart(rule, "=>", false);
		    if (mapping.size() != 2) 
		    	throw new RuntimeException("Invalid Synonym Rule:" + rule);
		    String key = mapping.get(0).trim();
		    List<String> values = getSynList(mapping.get(1));
		    synMap.put(key, values);
		}
	}
	
	public List<String> getSynList(String synonyms) {
		List<String> list = new ArrayList<String>();
		for (String s : StrUtils.splitSmart(synonyms, ";", false)) {
			list.add(s.trim());
		}
		return list;
	}
	*/
	
	
}