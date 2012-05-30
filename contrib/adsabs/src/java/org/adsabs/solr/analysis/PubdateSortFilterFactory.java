package org.adsabs.solr.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

public class PubdateSortFilterFactory extends BaseTokenFilterFactory {

	public TokenStream create(TokenStream input) {
		return new PubdateSortFilter(input);
	}

}
