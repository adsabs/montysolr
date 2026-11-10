package org.apache.lucene.search.spans;

import java.io.IOException;
import java.util.Objects;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
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
    protected final String fieldName;
    protected final int startPosition;
    protected final int endPosition;
    private final Analyzer indexAnalyzer;

    private static final class ValuePositionState extends PositionState {
        int documentId = -1;
        int valueCount;
        int indexedValueCount;
        int[] starts;
        int[] ends;
        int[] ordinals;
    }

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
     * @param indexAnalyzer Index analyzer for reconstructing field-value position boundaries
     */
    public SpanNegativeIndexRangeQuery(SpanQuery match, String fieldName, int startPosition, int endPosition,
                                       Analyzer indexAnalyzer) {
        super(match);

        this.fieldName = Objects.requireNonNull(fieldName);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.indexAnalyzer = Objects.requireNonNull(indexAnalyzer);
    }

    @Override
    protected PositionState createPositionState() {
        return new ValuePositionState();
    }

    @Override
    protected FilterSpans.AcceptStatus acceptPosition(Spans spans, Document currentDocument,
                                                      PositionState positionState) throws IOException {
        ValuePositionState state = (ValuePositionState) positionState;
        if (state.documentId != spans.docID()) {
            analyzeDocumentValues(currentDocument, spans.docID(), state);
        }

        int count = state.valueCount;
        int docStartPosition = startPosition;
        if (startPosition < 0) {
            docStartPosition = count + startPosition;

            // Clip an over-negative start to the beginning of the field.
            if (docStartPosition < 0) {
                docStartPosition = 0;
            }
        }
        int docEndPosition = endPosition;
        if (endPosition < 0) {
            docEndPosition = count + endPosition + 1;

            // An over-negative end leaves no positions in the requested range.
            if (docEndPosition < 0) {
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;
            }
        }

        int spanStart = valueOrdinalAt(spans.startPosition(), state);
        if (spanStart < 0) {
            if (state.indexedValueCount == 0
                    || spans.startPosition() >= state.ends[state.indexedValueCount - 1]) {
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;
            }
            return FilterSpans.AcceptStatus.NO;
        }
        if (endPosition != startPosition) {
            int spanEnd = valueOrdinalAt(spans.endPosition() - 1, state);
            if (spanEnd >= 0) {
                // The containing value ordinal is inclusive; range ends are exclusive.
                spanEnd++;
            }

            // Too late; beyond the end position.
            if (spanStart > docEndPosition)
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;

            if (spanEnd < 0 || spanEnd > docEndPosition)
                return FilterSpans.AcceptStatus.NO;

            // Too early; before the start position.
            if (spanEnd < docStartPosition)
                return FilterSpans.AcceptStatus.NO;

            if (spanStart < docStartPosition)
                return FilterSpans.AcceptStatus.NO;
        } else {
            // Span positions are ordered; skip earlier positions and stop beyond the target.
            if (spanStart > docStartPosition)
                return FilterSpans.AcceptStatus.NO_MORE_IN_CURRENT_DOC;
            if (spanStart < docStartPosition)
                return FilterSpans.AcceptStatus.NO;
        }

        return FilterSpans.AcceptStatus.YES;
    }

    private void analyzeDocumentValues(Document document, int documentId, ValuePositionState state)
            throws IOException {
        IndexableField[] fields = document.getFields(fieldName);
        state.documentId = documentId;
        state.valueCount = fields.length;
        state.indexedValueCount = 0;
        if (fields.length > 0 && (state.starts == null || state.starts.length < fields.length)) {
            state.starts = new int[fields.length];
            state.ends = new int[fields.length];
            state.ordinals = new int[fields.length];
        }

        int position = -1;
        for (int ordinal = 0; ordinal < fields.length; ordinal++) {
            if (ordinal > 0) {
                position += indexAnalyzer.getPositionIncrementGap(fieldName);
            }

            String value = fields[ordinal].stringValue();
            if (value == null) {
                continue;
            }

            try (TokenStream tokenStream = indexAnalyzer.tokenStream(fieldName, value)) {
                PositionIncrementAttribute increment = tokenStream.addAttribute(PositionIncrementAttribute.class);
                tokenStream.reset();

                boolean hasToken = false;
                int valueStart = 0;
                int valueEnd = 0;
                while (tokenStream.incrementToken()) {
                    position += increment.getPositionIncrement();
                    int tokenEnd = position + 1;
                    if (!hasToken) {
                        valueStart = position;
                        valueEnd = tokenEnd;
                        hasToken = true;
                    } else {
                        valueStart = Math.min(valueStart, position);
                        valueEnd = Math.max(valueEnd, tokenEnd);
                    }
                }

                tokenStream.end();
                position += increment.getPositionIncrement();

                if (hasToken) {
                    int index = state.indexedValueCount++;
                    state.starts[index] = valueStart;
                    state.ends[index] = valueEnd;
                    state.ordinals[index] = ordinal;
                }
            }
        }
    }

    private static int valueOrdinalAt(int position, ValuePositionState state) {
        int low = 0;
        int high = state.indexedValueCount - 1;
        while (low <= high) {
            int middle = (low + high) >>> 1;
            if (position < state.starts[middle]) {
                high = middle - 1;
            } else if (position >= state.ends[middle]) {
                low = middle + 1;
            } else {
                return state.ordinals[middle];
            }
        }
        return -1;
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
                && fieldName.equals(otherQuery.fieldName)
                && indexAnalyzer.equals(otherQuery.indexAnalyzer);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 31 * hash + fieldName.hashCode();
        hash = 31 * hash + startPosition;
        hash = 31 * hash + endPosition;
        hash = 31 * hash + indexAnalyzer.hashCode();
        return hash;
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
