package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenFilterFactory;

import java.util.Map;

public class PythonicAuthorNormalizeFilterFactory extends TokenFilterFactory {


    public PythonicAuthorNormalizeFilterFactory(Map<String, String> args) {
        super(args);
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameter(s): " + args);
        }
    }

    public PythonicAuthorNormalizerFilter create(TokenStream input) {
        return new PythonicAuthorNormalizerFilter(input);
    }
}
