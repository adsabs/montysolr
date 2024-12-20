package org.apache.lucene.search.spans;

import org.apache.lucene.document.Document;
import org.apache.lucene.queries.spans.FilterSpans;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.queries.spans.Spans;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.QueryVisitor;

/**
 * Matches spans that are within a range of positions in the document. This query type supports negative indices.
 */
public class SpanNegativeIndexRangeQuery extends SpanPositionAndDocumentQuery {

    // Field name is required to check the total number of entries the document has for that field
    // This information is not included in the Spans object; it needs to come from the Document
    protected String fieldName;
    protected int startPosition;
    protected int endPosition;
    private final int positionIncrementGap;

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
    public SpanNegativeIndexRangeQuery(SpanQuery match, String fieldName, int startPosition, int endPosition,
                                       int positionIncrementGap) {
        super(match);

        this.fieldName = fieldName;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.positionIncrementGap = positionIncrementGap;
    }

    @Override
    protected FilterSpans.AcceptStatus acceptPosition(Spans spans, Document currentDocument) {
        int count = currentDocument.getFields(fieldName).length;
        int docStartPosition = startPosition;
        if (startPosition < 0) {
            docStartPosition = count + startPosition;

            // If the start index is still negative, clip it to the beginning of the field
            // This is similar to Python, where if a = [1,2,3], then a[-10:] == [1,2,3] but a[:-10] == []
            if (docStartPosition < 0) {
                docStartPosition = 0;
            }
        }
        int docEndPosition = endPosition;
        if (endPosition < 0) {
            docEndPosition = count + endPosition + 1;

            // If the end position is still negative, there can be no matches in the document
            // Consider what the user is asking for: "Give me all matches that are _not_ in the last N positions"
            // If the user is asking for everything except the last 10 positions, and the field only has 5 positions,
            // then there can be no matches.
            if (docEndPosition < 0) {
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;
            }
        }

        int spanStart = spans.startPosition();
        int spanEnd = spans.endPosition();
        if (positionIncrementGap > 1) {
            // positionIncrementGap produces sequences of positions like f(n) = n*(positionIncrementGap + 2)
            // e.g. 0, 102, 204, 306
            // The end of each position is its start position +1
            // It is NOT the next start position
            // e.g. (start, end): (0, 1), (102, 103), (204, 205), (306, 307)
            spanStart = spans.startPosition() / (positionIncrementGap + 2);
            spanEnd = (spans.endPosition() - 1) / (positionIncrementGap + 2);
            if (spanStart == spanEnd && spans.startPosition() != spans.endPosition())
                spanEnd += 1;
        }

        if (endPosition != startPosition) {
            // Too late; beyond the end position
            if (spanStart > docEndPosition)
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;

            if (spanEnd > docEndPosition)
                return FilterSpans.AcceptStatus.NO;

            // Too early; before the start position
            if (spanEnd < docStartPosition)
                return FilterSpans.AcceptStatus.NO;

            if (spanStart < docStartPosition)
                return FilterSpans.AcceptStatus.NO;
        } else {
            // We are doing an exact position search
            if (spanStart != docStartPosition)
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

    @Override
    public void visit(QueryVisitor visitor) {
        if (visitor.acceptField(this.fieldName)) {
            this.match.visit(visitor.getSubVisitor(BooleanClause.Occur.MUST, this));
        }
    }
}
