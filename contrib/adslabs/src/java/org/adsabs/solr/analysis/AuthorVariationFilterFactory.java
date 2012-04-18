package org.adsabs.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

public class AuthorVariationFilterFactory extends BaseTokenFilterFactory {

    @Override
    public void init(Map<String,String> args) {
        super.init(args);
    }
    
	@Override
	public TokenStream create(TokenStream input) {
		return new AuthorVariationFilter(input);
	}

}
