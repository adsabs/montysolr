package org.apache.solr.analysis;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.DuplicateTermAttribute;

import java.io.IOException;

/*
 * Purpose of this filter is just to make a copy of the currently processed token
 * (so that we can comapre the copy against the later version of the transformed
 * token)
 */
public final class DuplicatingFilter extends TokenFilter {


    private final DuplicateTermAttribute dupAttr = addAttribute(DuplicateTermAttribute.class);
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    public DuplicatingFilter(TokenStream input) {
        super(input);
    }

    @Override
    public boolean incrementToken() throws IOException {

        if (input.incrementToken()) {
            dupAttr.setValue(termAtt.toString());
            return true;
        } else {
            return false;
        }
    }

}
