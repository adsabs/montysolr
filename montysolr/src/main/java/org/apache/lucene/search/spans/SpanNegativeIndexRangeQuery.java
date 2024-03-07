package org.apache.lucene.search.spans;

import org.apache.lucene.document.Document;

/**
 * Matches spans near the beginning of a field.
 *
 * <p>This class is a simple extension of {@link SpanPositionRangeQuery} in that it assumes the
 * start to be zero and only checks the end boundary.
 */
public class SpanNegativeIndexRangeQuery extends SpanPositionAndDocumentQuery {

    protected String fieldName;
    protected int startPosition;
    protected int endPosition;

    /**
     * <p>Constructs a query to perform range matches, where the range position may be negative.
     * <p>In the case of negative positions, the negative position is added to the total field count
     * for a given document.
     * For example: if the field count is 5, and the position is -1, the effective position is 4.
     * <p>If the effective position is negative, the entire document is skipped.
     *
     * @param match Potential span match iterator
     * @param fieldName Field to search
     * @param startPosition The start position of the range. Can be positive or negative, but never 0
     * @param endPosition The end position of the range. Can be positive or negative, but never 0
     */
    public SpanNegativeIndexRangeQuery(SpanQuery match, String fieldName, int startPosition, int endPosition) {
        super(match);

        this.fieldName = fieldName;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * Constructs a query to perform exact position matches.
     *
     * @param match Potential span match iterator
     * @param fieldName Field to search
     * @param startPosition The exact position the term can be in. Can be positive or negative, but never 0
     */
    public SpanNegativeIndexRangeQuery(SpanQuery match, String fieldName, int startPosition) {
        this(match, fieldName, startPosition, 0);
    }

    @Override
    protected FilterSpans.AcceptStatus acceptPosition(Spans spans, Document currentDocument) {
        assert spans.startPosition() != spans.endPosition()
                : "start equals end: " + spans.startPosition();

        int docStartPosition = startPosition;
        if (startPosition < 0) {
            int count = currentDocument.getFields(fieldName).length;
            docStartPosition = count + startPosition;

            if (docStartPosition < 0) {
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;
            }
        }
        int docEndPosition = endPosition;
        if (endPosition < 0) {
            int count = currentDocument.getFields(fieldName).length;
            docEndPosition = count + endPosition;

            if (docEndPosition < 0) {
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;
            }
        }

        if (endPosition != startPosition) {
            // Too late; beyond the end position
            if (spans.startPosition() > docEndPosition)
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;

            if (spans.endPosition() > docEndPosition)
                return FilterSpans.AcceptStatus.NO;

            // Too early; before the start position
            if (spans.endPosition() < docStartPosition)
                return FilterSpans.AcceptStatus.NO;

            if (spans.startPosition() < docStartPosition)
                return FilterSpans.AcceptStatus.NO;
        } else {
            // We are doing an exact position search
            if (spans.startPosition() != docStartPosition)
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;
        }

        return FilterSpans.AcceptStatus.YES;
    }

    // Override required otherwise the queries may be cached incorrectly
    @Override
    public boolean equals(Object other) {
        if (!sameClassAs(other))
            return false;

        SpanNegativeIndexRangeQuery otherQuery = (SpanNegativeIndexRangeQuery) other;
        return super.equals(other)
                && startPosition == otherQuery.startPosition
                && endPosition == otherQuery.endPosition
                && fieldName.equals(otherQuery.fieldName);
    }

    @Override
    public String toString(String field) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("spanNegativeIndexRange(");
        buffer.append(match.toString(field));
        buffer.append(", ");
        buffer.append(startPosition);
        buffer.append(", ");
        buffer.append(endPosition);
        buffer.append(")");
        return buffer.toString();
    }
}
