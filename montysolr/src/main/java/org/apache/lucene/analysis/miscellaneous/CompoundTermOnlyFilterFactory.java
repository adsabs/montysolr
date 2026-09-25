package org.apache.lucene.analysis.miscellaneous;

import org.apache.lucene.analysis.TokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;

import java.util.Map;

/** Factory for {@link CompoundTermOnlyFilter}. */
public class CompoundTermOnlyFilterFactory extends TokenFilterFactory {
    public CompoundTermOnlyFilterFactory(Map<String, String> args) {
        super(args);
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameters: " + args);
        }
    }

    @Override
    public TokenStream create(TokenStream input) {
        return new CompoundTermOnlyFilter(input);
    }
}
