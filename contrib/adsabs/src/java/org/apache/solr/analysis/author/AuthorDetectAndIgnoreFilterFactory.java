/**
 * 
 */
package org.apache.solr.analysis.author;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.StrUtils;

/*
 * A cleanup filter that catches cases that should not 
 * be considered author search
 */
public class AuthorDetectAndIgnoreFilterFactory extends TokenFilterFactory {
	
	private Integer maxlen;

	public void init(Map<String, String> args) {
    super.init(args);
    maxlen = -1;
    if (args.containsKey("maxlen")) {
    	maxlen = Integer.parseInt(args.get("maxlen"));
    }
    
  }
	
	public AuthorDetectAndIgnoreFilter create(TokenStream input) {
		return new AuthorDetectAndIgnoreFilter(input, maxlen);
	}
}
