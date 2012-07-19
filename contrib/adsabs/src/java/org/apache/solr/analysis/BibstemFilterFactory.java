package org.apache.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.core.BibstemFilter;

public class BibstemFilterFactory extends TokenFilterFactory {
	
	private boolean facetMode = false;
	
	public void init(Map<String,String> args) {
		String f = args.get("facetMode");
	    if (f != null && f.toLowerCase().contains("true")) {
	      facetMode = true;
	    }
	}

	public TokenStream create(TokenStream input) {
		return new BibstemFilter(input, facetMode);
	}

}
