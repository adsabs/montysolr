package org.apache.solr.analysis.author;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class AuthorQueryVariationsFilterFactory extends TokenFilterFactory {

    @Override
    public void init(Map<String,String> args) {
        super.init(args);
    }
    
	public TokenStream create(TokenStream input) {
		return new AuthorQueryVariationsFilter(input);
	}

}
