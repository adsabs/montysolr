package org.adsabs.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

public class BibstemFilterFactory extends BaseTokenFilterFactory {
	
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
