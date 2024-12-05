package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenFilterFactory;

import java.util.Map;

/**
 * @author jluker
 */
public class AuthorNormalizeFilterFactory extends TokenFilterFactory {

    private boolean keepApostrophes;

    public AuthorNormalizeFilterFactory(Map<String, String> args) {
        super(args);
        if (args.containsKey("keepApostrophe")) {
            String inputType = args.remove("keepApostrophe");
            if (inputType.equals("true")) keepApostrophes = true;
        }
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameter(s): " + args);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
     */
    public AuthorNormalizeFilter create(TokenStream input) {
        return new AuthorNormalizeFilter(input, keepApostrophes);
    }
}
