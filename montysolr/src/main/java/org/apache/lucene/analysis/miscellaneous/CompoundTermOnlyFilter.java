package org.apache.lucene.analysis.miscellaneous;

import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;

import java.io.IOException;

/**
 * Removes the single-position parts that a word delimiter graph emits under a
 * multi-position compound, so {@code dust-dust} keeps only {@code dustdust}.
 * The removed positions are added to the next kept token, so a following word
 * keeps the position it has in an index that stores both the parts and the
 * compound. This lets an exact phrase query distinguish a hyphenated compound
 * from the same words separated by whitespace.
 */
public final class CompoundTermOnlyFilter extends FilteringTokenFilter {
    private final PositionIncrementAttribute posIncAtt = addAttribute(PositionIncrementAttribute.class);
    private final PositionLengthAttribute posLenAtt = addAttribute(PositionLengthAttribute.class);
    private int position = -1;
    private int coveredUntil = -1;

    public CompoundTermOnlyFilter(TokenStream in) {
        super(in);
    }

    @Override
    protected boolean accept() {
        position += posIncAtt.getPositionIncrement();
        int length = posLenAtt.getPositionLength();
        if (length > 1) {
            coveredUntil = Math.max(coveredUntil, position + length);
            return true;
        }
        return position >= coveredUntil;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        position = -1;
        coveredUntil = -1;
    }
}
