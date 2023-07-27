package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

/*
 * A cleanup filter that catches cases that should not
 * be considered author search
 */
public class AuthorDetectAndIgnoreFilterFactory extends TokenFilterFactory {

    private Integer maxlen;

    public AuthorDetectAndIgnoreFilterFactory(Map<String, String> args) {
        super(args);
        maxlen = -1;
        if (args.containsKey("maxlen")) {
            maxlen = Integer.parseInt(args.remove("maxlen"));
        }
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameter(s): " + args);
        }
    }

    public AuthorDetectAndIgnoreFilter create(TokenStream input) {
        return new AuthorDetectAndIgnoreFilter(input, maxlen);
    }
}
