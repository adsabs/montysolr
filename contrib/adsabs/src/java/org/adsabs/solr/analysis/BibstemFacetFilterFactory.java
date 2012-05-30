package org.adsabs.solr.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

public class BibstemFacetFilterFactory extends BaseTokenFilterFactory {

	public TokenStream create(TokenStream input) {
		return new BibstemFacetFilter(input);
	}

}
