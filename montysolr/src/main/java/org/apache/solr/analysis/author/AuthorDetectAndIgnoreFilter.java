package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

public final class AuthorDetectAndIgnoreFilter extends TokenFilter {

    private final Integer maxlen;
    private final CharTermAttribute termAtt;

    protected AuthorDetectAndIgnoreFilter(TokenStream input, Integer maxlen) {
        super(input);
        this.maxlen = maxlen;
        termAtt = addAttribute(CharTermAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!input.incrementToken()) {
            return false;
        }

        if (maxlen != null && maxlen != -1) {
            String[] parts = termAtt.toString().split(" ");
            return parts.length <= maxlen;
        }
        return true;
    }

}
