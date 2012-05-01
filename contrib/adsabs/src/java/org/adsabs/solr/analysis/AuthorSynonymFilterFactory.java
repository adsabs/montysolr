/**
 * 
 */
package org.adsabs.solr.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;
import org.apache.solr.common.ResourceLoader;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.util.plugin.ResourceLoaderAware;
import org.apache.solr.core.SolrResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jluker
 *
 */
public class AuthorSynonymFilterFactory extends BaseTokenFilterFactory implements ResourceLoaderAware {

    public static final Logger log = LoggerFactory.getLogger(AuthorSynonymFilter.class);
    
    private AuthorSynonymMap synMap;
    
	public void inform(ResourceLoader loader) {
		String synonyms = args.get("synonyms");
		if (synonyms == null)
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Missing required argument 'synonyms'.");
	    List<String> rules=null;
	    try {
	    	File synonymFile = new File(((SolrResourceLoader) loader).getConfigDir() + synonyms);
	        if (synonymFile.exists()) {
	        	rules = loader.getLines(synonyms);
	      	} else {
	      		log.error("can't find " + synonyms);
	      	}
	    } catch (IOException e) {
	      	throw new RuntimeException(e);
	    }
	    
	    synMap = new AuthorSynonymMap();
	    parseRules(rules, synMap);
	}
	
	public void parseRules(List<String> rules, AuthorSynonymMap synMap) {
		
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

	@Override
    public void init(Map<String, String> args) {
	    super.init(args);
	}
	
	public AuthorSynonymMap getSynonymMap() {
		return synMap;
	}
	
	public void setSynonymMap(AuthorSynonymMap synMap) {
		this.synMap = synMap;
	}
	  
	public TokenStream create(TokenStream input) {
		return new AuthorSynonymFilter(input, synMap);
	}
}