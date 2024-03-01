package org.apache.lucene.search.spans;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.MultiFields;

/**
 * Matches spans near the beginning of a field.
 *
 * <p>This class is a simple extension of {@link SpanPositionRangeQuery} in that it assumes the
 * start to be zero and only checks the end boundary.
 */
public class SpanExactPositionQuery extends SpanPositionAndDocumentQuery {

    protected String fieldName;
    protected int position;

    /**
     * Construct a SpanFirstQuery matching spans in <code>match</code> whose end position is less than
     * or equal to <code>end</code>.
     */
    public SpanExactPositionQuery(SpanQuery match, String fieldName, int position) {
        super(match);

        this.fieldName = fieldName;
        this.position = position;
    }

    @Override
    protected FilterSpans.AcceptStatus acceptPosition(Spans spans, Document currentDocument) {
        assert spans.startPosition() != spans.endPosition()
                : "start equals end: " + spans.startPosition();

        int docPosition = position;
        if (position < 0) {
            int count = currentDocument.getFields(fieldName).length;
            docPosition = count + position;
        }

        if (spans.startPosition() != docPosition)
            return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;

        return FilterSpans.AcceptStatus.YES;
    }

    @Override
    public String toString(String field) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("spanExactPosition(");
        buffer.append(match.toString(field));
        buffer.append(", ");
        buffer.append(position);
        buffer.append(")");
        return buffer.toString();
    }
}
