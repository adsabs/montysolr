package org.apache.solr.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.core.PubdateSortFilter;

public class PubdateSortFilterFactory extends TokenFilterFactory {

	public TokenStream create(TokenStream input) {
		return new PubdateSortFilter(input);
	}

}
