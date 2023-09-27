package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

public class AuthorCreateQueryVariationsFilterFactory extends TokenFilterFactory {

    private boolean plainSurname = false;
    private int acronymVariations = 0;
    private String tokenType = null;
    private boolean addWildcards = false;
    private boolean shortenMultiname = false;
    private boolean lookAtPayloadForOrigAuthor = false;

    public AuthorCreateQueryVariationsFilterFactory(Map<String, String> args) {
        super(args);

        if (args.containsKey("acronymVariations")) {
            acronymVariations = Integer.parseInt(args.remove("acronymVariations"));
        }

        if (args.containsKey("plainSurname")) {
            plainSurname = args.remove("plainSurname").equals("true");
        }

        if (args.containsKey("addWildcards")) {
            addWildcards = args.remove("addWildcards").equals("true");
        }

        if (args.containsKey("addShortenedMultiName")) {
            shortenMultiname = args.remove("addShortenedMultiName").equals("true");
        }

        if (args.containsKey("lookAtPayloadForOrigAuthor")) {
            lookAtPayloadForOrigAuthor = args.remove("lookAtPayloadForOrigAuthor").equals("true");
        }

        if (args.containsKey("tokenType")) {
            tokenType = args.remove("tokenType");
            if (tokenType.equals("null")) tokenType = null;
        }

        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameter(s): " + args);
        }
    }

    public TokenStream create(TokenStream input) {
        return new AuthorCreateQueryVariationsFilter(input, tokenType,
                plainSurname, acronymVariations, addWildcards,
                shortenMultiname, lookAtPayloadForOrigAuthor);
    }

}
