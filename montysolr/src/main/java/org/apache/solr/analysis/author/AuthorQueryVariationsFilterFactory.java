package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenFilterFactory;

import java.util.Map;

public class AuthorQueryVariationsFilterFactory extends TokenFilterFactory {

    protected AuthorQueryVariationsFilterFactory(Map<String, String> args) {
        super(args);
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameter(s): " + args);
        }
    }

    public TokenStream create(TokenStream input) {
        return new AuthorQueryVariationsFilter(input);
    }

}
