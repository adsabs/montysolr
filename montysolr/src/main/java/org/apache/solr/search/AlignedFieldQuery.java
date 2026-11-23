package org.apache.solr.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queries.payloads.SpanPayloadCheckQuery;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.SpanNegativeIndexRangeQuery;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Matches two independently evaluated stored values within a distance in array ordinals. */
public final class AlignedFieldQuery extends Query {
    private static final long MAX_REUSED_INDEX_BYTES = 64 * 1024L;

    private final Query leftQuery;
    private final Query rightQuery;
    private final SchemaField leftField;
    private final SchemaField rightField;
    private final int slop;

    public AlignedFieldQuery(Query leftQuery, Query rightQuery,
                             SchemaField leftField, SchemaField rightField, int slop) {
        this.leftQuery = leftQuery;
        this.rightQuery = rightQuery;
        this.leftField = leftField;
        this.rightField = rightField;
        this.slop = slop;
    }

    @Override
    public Weight createWeight(IndexSearcher searcher, ScoreMode scoreMode, float boost) throws IOException {
        FixedBitSet matches = new FixedBitSet(searcher.getIndexReader().maxDoc());
        Query candidates = new BooleanQuery.Builder()
                .add(candidateQuery(leftQuery), BooleanClause.Occur.FILTER)
                .add(candidateQuery(rightQuery), BooleanClause.Occur.FILTER)
                .build();
        String leftName = leftField.getName();
        String rightName = rightField.getName();
        boolean sameField = leftName.equals(rightName);
        Set<String> fields = sameField ? Set.of(leftName) : Set.of(leftName, rightName);
        SlotMatcher leftMatcher = new SlotMatcher(leftQuery, leftField);
        SlotMatcher rightMatcher = new SlotMatcher(rightQuery, rightField);
        searcher.search(candidates, new SimpleCollector() {
            private int docBase;
            private StoredFields storedFields;

            @Override
            public ScoreMode scoreMode() {
                return ScoreMode.COMPLETE_NO_SCORES;
            }

            @Override
            protected void doSetNextReader(LeafReaderContext context) throws IOException {
                docBase = context.docBase;
                storedFields = context.reader().storedFields();
            }

            @Override
            public void collect(int doc) throws IOException {
                Document document = storedFields.document(doc, fields);
                IndexableField[] leftValues = document.getFields(leftName);
                IndexableField[] rightValues = sameField ? leftValues : document.getFields(rightName);
                if (matchesPair(leftValues, rightValues, leftMatcher, rightMatcher)) {
                    matches.set(docBase + doc);
                }
            }
        });
        return new BitSetQuery(matches).createWeight(searcher, scoreMode, boost);
    }

    private boolean matchesPair(IndexableField[] leftValues, IndexableField[] rightValues,
                                SlotMatcher leftMatcher, SlotMatcher rightMatcher) throws IOException {
        int right = 0;
        for (int left = 0; left < leftValues.length; left++) {
            right = Math.max(right, left - slop);
            if (right >= rightValues.length) {
                break;
            }
            if (!leftMatcher.matches(leftValues[left])) {
                continue;
            }
            int end = (int) Math.min((long) left + slop, rightValues.length - 1);
            // The window moves forward; never check a failed right slot twice.
            for (; right <= end; right++) {
                if (rightMatcher.matches(rightValues[right])) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Query candidateQuery(Query query) {
        if (query instanceof FuzzyQuery fuzzy && fuzzy.getRewriteMethod() instanceof TopTermsRewrite<?>) {
            return new FuzzyQuery(fuzzy.getTerm(), fuzzy.getMaxEdits(), fuzzy.getPrefixLength(),
                    FuzzyQuery.defaultMaxExpansions, fuzzy.getTranspositions(), MultiTermQuery.CONSTANT_SCORE_REWRITE);
        }
        if (query instanceof SpanQuery || (query instanceof MultiTermQuery multiTerm
                && multiTerm.getRewriteMethod() instanceof TopTermsRewrite<?>)) {
            // Document-wide positions and bounded expansions can discard valid slot matches.
            return new MatchAllDocsQuery();
        }
        if (query instanceof BooleanQuery bool) {
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            for (BooleanClause clause : bool) {
                if (clause.getOccur() != BooleanClause.Occur.MUST_NOT) {
                    builder.add(candidateQuery(clause.getQuery()), BooleanClause.Occur.SHOULD);
                }
            }
            BooleanQuery candidates = builder.build();
            return candidates.clauses().isEmpty() ? new MatchAllDocsQuery() : candidates;
        }
        if (query instanceof DisjunctionMaxQuery disjunction) {
            List<Query> candidates = new ArrayList<>(disjunction.getDisjuncts().size());
            for (Query disjunct : disjunction) {
                candidates.add(candidateQuery(disjunct));
            }
            return new DisjunctionMaxQuery(candidates, 0.0f);
        }
        if (query instanceof BoostQuery boost) {
            return candidateQuery(boost.getQuery());
        }
        if (query instanceof ConstantScoreQuery constantScore) {
            return candidateQuery(constantScore.getQuery());
        }
        return query;
    }

    private static final class SlotMatcher {
        private final Query query;
        private final SchemaField field;
        private final FieldType type;
        private final Analyzer analyzer;
        private final boolean analyzeText;
        private final MemoryIndex index;

        private SlotMatcher(Query query, SchemaField field) {
            this.query = query;
            this.field = field;
            type = field.getType();
            analyzer = type.getIndexAnalyzer();
            SlotRequirements requirements = new SlotRequirements();
            query.visit(requirements);
            analyzeText = type.getClass() == TextField.class && !field.hasDocValues() && !field.omitNorms()
                    && !requirements.needsStoredValue;
            index = MemoryIndex.fromDocument(List.of(), analyzer, false, requirements.needsPayloads,
                    MAX_REUSED_INDEX_BYTES);
        }

        private boolean matches(IndexableField storedValue) throws IOException {
            index.reset();
            if (analyzeText) {
                index.addField(field.getName(), storedValue.stringValue(), analyzer);
            } else {
                for (IndexableField indexedValue : field.createFields(type.toObject(storedValue))) {
                    index.addField(indexedValue, analyzer);
                }
            }
            return index.createSearcher().count(query) > 0;
        }
    }

    private static final class SlotRequirements extends QueryVisitor {
        private boolean needsStoredValue;
        private boolean needsPayloads;

        @Override
        public QueryVisitor getSubVisitor(BooleanClause.Occur occur, Query parent) {
            visitLeaf(parent);
            return this;
        }

        @Override
        public void visitLeaf(Query query) {
            needsStoredValue |= query instanceof SpanNegativeIndexRangeQuery;
            needsPayloads |= query instanceof SpanPayloadCheckQuery;
        }
    }

    @Override
    public String toString(String field) {
        return "same(" + leftQuery + ", " + rightQuery + (slop == 0 ? "" : ", " + slop) + ")";
    }

    @Override
    public void visit(QueryVisitor visitor) {
        visitor.visitLeaf(this);
    }

    @Override
    public boolean equals(Object other) {
        if (!sameClassAs(other)) {
            return false;
        }
        AlignedFieldQuery that = (AlignedFieldQuery) other;
        return slop == that.slop
                && leftField.equals(that.leftField) && rightField.equals(that.rightField)
                && leftQuery.equals(that.leftQuery) && rightQuery.equals(that.rightQuery);
    }

    @Override
    public int hashCode() {
        int result = 31 * classHash() + leftQuery.hashCode();
        result = 31 * result + rightQuery.hashCode();
        result = 31 * result + leftField.hashCode();
        result = 31 * result + rightField.hashCode();
        return 31 * result + slop;
    }
}
