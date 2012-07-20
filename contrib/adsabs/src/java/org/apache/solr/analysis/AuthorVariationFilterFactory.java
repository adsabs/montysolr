package org.apache.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.core.AuthorVariationFilter;

public class AuthorVariationFilterFactory extends TokenFilterFactory {

    @Override
    public void init(Map<String,String> args) {
        super.init(args);
    }
    
	public TokenStream create(TokenStream input) {
		return new AuthorVariationFilter(input);
	}

}
