package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.StrUtils;

import java.util.List;
import java.util.Map;

public class AuthorCollectorFactory extends TokenFilterFactory {

    private final List<String> tokenTypes;
    private boolean emitTokens;

    public AuthorCollectorFactory(Map<String, String> args) {
        super(args);
        if (args.containsKey("tokenTypes")) {
            tokenTypes = StrUtils.splitSmart(args.remove("tokenTypes"), ",", false);
        } else {
            throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "The tokenType parameter missing");
        }
        emitTokens = false;
        if (args.containsKey("emitTokens")) {
            if (args.remove("emitTokens").equals("true")) {
                emitTokens = true;
            }
        }
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameter(s): " + args);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
     */
    public AuthorCollectorFilter create(TokenStream input) {

        AuthorCollectorFilter collector = new AuthorCollectorFilter(input);
        collector.setTokenTypes(tokenTypes);
        collector.setEmitTokens(emitTokens);
        return collector;
    }

}
