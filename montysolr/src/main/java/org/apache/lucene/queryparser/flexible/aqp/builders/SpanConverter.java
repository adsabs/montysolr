package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermStates;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.queries.spans.*;
import org.apache.lucene.queries.payloads.*;

import java.io.IOException;
import java.util.*;

public class SpanConverter {
    private static final int MAX_POSITIONED_SLOP_FOR_EXACT_ALTERNATIVES = 64;
    private static final int MAX_POSITIONED_SLOP_ALTERNATIVES = 64;
    private static final int MAX_POSITIONED_SLOP_SEARCH_STEPS = 4096;


    boolean wrapNonConvertible = false;

    public SpanQuery getSpanQuery(SpanConverterContainer container)
            throws QueryNodeException {
        Query q = container.query;
        float boost = container.boost;

        if (q instanceof SpanQuery) {
            return wrapBoost((SpanQuery) q, boost);
        } else if (q instanceof TermQuery) {
            return wrapBoost(new SpanTermQuery(((TermQuery) q).getTerm()), boost);
        } else if (q instanceof ConstantScoreQuery) {
            return getSpanQuery(new SpanConverterContainer(((ConstantScoreQuery) q).getQuery(), 1, true, 0.0f));
        } else if (q instanceof WildcardQuery wildcardQuery) {
            return wrapBoost(wrapMultiTermQuery(wildcardQuery), boost);
        } else if (q instanceof PrefixQuery prefixQuery) {
            return wrapBoost(wrapMultiTermQuery(prefixQuery), boost);
        } else if (q instanceof MultiPhraseQuery) {
            return wrapBoost(convertMultiPhraseToSpan(container), boost);
        } else if (q instanceof PhraseQuery) {
            return wrapBoost(convertPhraseToSpan(container), boost);
        } else if (q instanceof BooleanQuery) {
            return wrapBoost(convertBooleanToSpan(container), boost);
        } else if (q instanceof RegexpQuery regexpQuery) {
            return wrapBoost(wrapMultiTermQuery(regexpQuery), boost);
        } else if (q instanceof DisjunctionMaxQuery) {
            return wrapBoost(convertDisjunctionQuery(container), boost);
        } else if (q instanceof BoostQuery) {
            return wrapBoost(getSpanQuery(new SpanConverterContainer(((BoostQuery) q).getQuery(), 1, true)),
                    ((BoostQuery) q).getBoost());
        } else if (q instanceof MatchNoDocsQuery) {
            return new EmptySpanQuery(container.query);
        } else if (q instanceof SynonymQuery) {
            return wrapBoost(convertSynonymToSpan(container), boost);
        } else if (q instanceof FunctionScoreQuery functionScoreQuery) {
            container.query = functionScoreQuery.getWrappedQuery();
            return getSpanQuery(container);
        } else if (q instanceof BoostQuery boostQuery) {
            container.query = boostQuery.getQuery();
            return getSpanQuery(container);
        } else if (q instanceof ConstantScoreQuery constantScoreQuery) {
            container.query = constantScoreQuery.getQuery();
            return getSpanQuery(container);
        }

        SpanQuery wrapped = wrapNonConvertible(container);
        if (wrapped != null)
            return wrapped;

        throw new QueryNodeException(new MessageImpl(
                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
                "(yet) Unsupported clause inside span query: "
                        + q.getClass().getName()));
    }

    private SpanQuery convertDisjunctionQuery(SpanConverterContainer container) throws QueryNodeException {
        DisjunctionMaxQuery q = (DisjunctionMaxQuery) container.query;
        Collection<Query> clauses = q.getDisjuncts();
        if (clauses.isEmpty()) {
            container.query = new MatchNoDocsQuery();
            return getSpanQuery(container);
        } else if (clauses.size() == 1) {
            container.query = clauses.stream().findFirst().get();
            return getSpanQuery(container);
        } else {
            // we assume it is OR query case
            Collection<Query> disjuncts = q.getDisjuncts();
            BooleanQuery.Builder bQuery = new BooleanQuery.Builder();
            for (Query qa : disjuncts) {
                bQuery.add(qa, Occur.SHOULD);
            }
            container.query = bQuery.build();
            return getSpanQuery(container);
        }
    }

    private SpanQuery wrapBoost(SpanQuery q, float boost) {
        if (Float.compare(boost, 1f) == 0) {
            return q;
        }

        // Score boosting was rewritten in Lucene 8, so we have to use PayloadScoreQueries to recreate
        // the old, constant score boosting behavior
        // With `includeSpanScore = true`, the score of the SpanQuery is multiplied by the payload score
        return new PayloadScoreQuery(q, new ConstantPayloadFunction(boost),
                PayloadDecoder.FLOAT_DECODER, true);
    }

    public SpanQuery wrapNonConvertible(SpanConverterContainer container) {
        if (wrapNonConvertible) {
            return doWrapping(container);
        }
        return null;
    }

    private SpanQuery doWrapping(SpanConverterContainer container) {
        return new EmptySpanQuery(container.query);
    }

    public void setWrapNonConvertible(boolean v) {
        wrapNonConvertible = v;
    }

    private SpanQuery convertMultiPhraseToSpan(SpanConverterContainer container) {
        MultiPhraseQuery q = (MultiPhraseQuery) container.query;
        Term[][] termArrays = q.getTermArrays();
        if (termArrays.length == 0) {
            return new EmptySpanQuery(q);
        }
        SpanQuery[] clauses = convertTermArrays(termArrays);
        if (clauses == null) {
            return new EmptySpanQuery(q);
        }
        return convertPositionedClauses(q, clauses, q.getPositions(), q.getSlop());
    }

    private SpanQuery convertPhraseToSpan(SpanConverterContainer container) {
        PhraseQuery q = (PhraseQuery) container.query;
        Term[] terms = q.getTerms();
        if (terms.length == 0) {
            return new EmptySpanQuery(q);
        }
        return convertPositionedClauses(q, convertTerms(terms),
                q.getPositions(), q.getSlop());
    }

    private SpanQuery[] convertTerms(Term[] terms) {
        SpanQuery[] clauses = new SpanQuery[terms.length];
        for (int i = 0; i < terms.length; i++) {
            clauses[i] = new SpanTermQuery(terms[i]);
        }
        return clauses;
    }

    private SpanQuery[] convertTermArrays(Term[][] termArrays) {
        SpanQuery[] clauses = new SpanQuery[termArrays.length];
        for (int i = 0; i < termArrays.length; i++) {
            Term[] terms = termArrays[i];
            if (terms.length == 0) {
                return null;
            }
            if (terms.length == 1) {
                clauses[i] = new SpanTermQuery(terms[0]);
            } else {
                clauses[i] = new SpanOrQuery(convertTerms(terms));
            }
        }
        return clauses;
    }

    private SpanQuery convertPositionedClauses(Query source, SpanQuery[] clauses,
                                                int[] positions, int querySlop) {
        if (clauses.length == 1) {
            return clauses[0];
        }
        if (clauses.length == 0) {
            return new EmptySpanQuery(source);
        }

        boolean hasPositionHoles = false;
        for (int i = 1; i < positions.length; i++) {
            hasPositionHoles |= positions[i] - positions[i - 1] > 1;
        }
        // SpanNear slop counts unmatched positions, not the phrase-wide move cost across holes.
        if (querySlop > 0 && hasPositionHoles) {
            if (querySlop > MAX_POSITIONED_SLOP_FOR_EXACT_ALTERNATIVES) {
                return convertPositionedClausesLinearly(clauses, positions, querySlop);
            }
            List<SpanQuery> alternatives = new ArrayList<>();
            int[] candidatePositions = new int[positions.length];
            long[] positionOffsets = new long[positions.length];
            int[] remainingSearchSteps = {MAX_POSITIONED_SLOP_SEARCH_STEPS};
            candidatePositions[0] = 0;
            for (long center = -((long) querySlop); center <= querySlop; center++) {
                int remainingSlop = querySlop - (int) Math.abs(center);
                if (!addPositionedSlopAlternatives(clauses, positions, candidatePositions,
                        positionOffsets, 1, center, remainingSlop, alternatives,
                        remainingSearchSteps)) {
                    return convertPositionedClausesLinearly(clauses, positions, querySlop);
                }
            }
            if (alternatives.isEmpty()) {
                return new EmptySpanQuery(source);
            }
            if (alternatives.size() == 1) {
                return alternatives.get(0);
            }
            return new SpanOrQuery(alternatives.toArray(new SpanQuery[0]));
        }
        return convertPositionedClausesLinearly(clauses, positions, querySlop);
    }

    private SpanQuery convertPositionedClausesLinearly(SpanQuery[] clauses, int[] positions,
                                                         int querySlop) {
        SpanNearQuery.Builder builder = new SpanNearQuery.Builder(
                clauses[0].getField(), true);
        builder.addClause(clauses[0]);
        for (int i = 1; i < clauses.length; i++) {
            int gap = positions[i] - positions[i - 1] - 1;
            if (gap > 0) {
                builder.addGap(gap);
            }
            builder.addClause(clauses[i]);
        }
        builder.setSlop(querySlop);
        return builder.build();
    }

    private boolean addPositionedSlopAlternatives(SpanQuery[] clauses, int[] positions,
                                                   int[] candidatePositions, long[] positionOffsets,
                                                   int clauseIndex, long center, int remainingSlop,
                                                   List<SpanQuery> alternatives,
                                                   int[] remainingSearchSteps) {
        if (--remainingSearchSteps[0] < 0) {
            return false;
        }
        if (clauseIndex == clauses.length) {
            if (!isLowerMedian(positionOffsets, center)) {
                return true;
            }
            if (alternatives.size() >= MAX_POSITIONED_SLOP_ALTERNATIVES) {
                return false;
            }
            SpanNearQuery.Builder builder = new SpanNearQuery.Builder(
                    clauses[0].getField(), true);
            builder.addClause(clauses[0]);
            for (int i = 1; i < clauses.length; i++) {
                int gap = candidatePositions[i] - candidatePositions[i - 1] - 1;
                if (gap > 0) {
                    builder.addGap(gap);
                }
                builder.addClause(clauses[i]);
            }
            alternatives.add(builder.setSlop(0).build());
            return true;
        }

        long queryPosition = (long) positions[clauseIndex] - positions[0];
        for (long offset = center - remainingSlop;
             offset <= center + remainingSlop; offset++) {
            long candidatePosition = queryPosition + offset;
            if (candidatePosition <= candidatePositions[clauseIndex - 1]
                    || candidatePosition > Integer.MAX_VALUE) {
                continue;
            }
            int editCost = (int) Math.abs(offset - center);
            if (editCost > remainingSlop) {
                continue;
            }
            candidatePositions[clauseIndex] = (int) candidatePosition;
            positionOffsets[clauseIndex] = offset;
            if (!addPositionedSlopAlternatives(clauses, positions, candidatePositions,
                    positionOffsets, clauseIndex + 1, center, remainingSlop - editCost,
                    alternatives, remainingSearchSteps)) {
                return false;
            }
        }
        return true;
    }


    private boolean isLowerMedian(long[] positionOffsets, long center) {
        int rank = (positionOffsets.length - 1) / 2;
        int below = 0;
        int atOrBelow = 0;
        for (long offset : positionOffsets) {
            if (offset < center) {
                below++;
            }
            if (offset <= center) {
                atOrBelow++;
            }
        }
        return below <= rank && atOrBelow > rank;
    }

    /*
     * Silly convertor for now it can handle only boolean queries of the same type
     * (ie not mixed cases). To do that, I have to build a graph (tree) and maybe
     * of only pairs (?)
     */
    protected SpanQuery convertBooleanToSpan(SpanConverterContainer container)
            throws QueryNodeException {
        BooleanQuery q = (BooleanQuery) container.query;

        List<BooleanClause> clauses = q.clauses();
        SpanQuery[] spanClauses = new SpanQuery[clauses.size()];
        String field = null;
        Occur o = null;
        int i = 0;
        for (BooleanClause c : clauses) {
            if (o != null && !o.equals(c.getOccur())) {
                throw new QueryNodeException(new MessageImpl(
                        QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
                        "(yet) Unsupported clause inside span query: "
                                + q.getClass().getName()));
            }
            o = c.getOccur();

            Query sq = c.getQuery();
            SpanQuery result = getSpanQuery(new SpanConverterContainer(sq, 1, true));
            spanClauses[i] = result;

            i++;
        }
        if (spanClauses.length == 1) {
            if (o.equals(Occur.MUST) || o.equals(Occur.SHOULD)) {
                return spanClauses[0];
            }
            throw new QueryNodeException(new MessageImpl(
                    QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
                    "A prohibited singleton cannot form a proximity span"));
        }
        try {
            if (o.equals(Occur.MUST)) {
                return new SpanNearQuery(spanClauses, container.slop,
                        container.inOrder);
            } else if (o.equals(Occur.SHOULD)) {
                return new SpanOrQuery(spanClauses);
            } else if (o.equals(Occur.MUST_NOT)) {
                SpanQuery[] exclude = new SpanQuery[spanClauses.length - 1];
                System.arraycopy(spanClauses, 1, exclude, 0, spanClauses.length - 1);
                return new SpanNotQuery(spanClauses[0], new SpanOrQuery(exclude));
            }
        } catch (IllegalArgumentException exc) {
            throw new QueryNodeException(new MessageImpl(
                    QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
                    "Proximity searches must be executed against the same field; please specify the field explicitly"));
        }


        throw new QueryNodeException(new MessageImpl(
                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
                "Congratulations! You have hit (yet) unsupported case: "
                        + q.getClass().getName()));
    }

    /**
     * Convert Synonym query, essentially will treat it the same way as
     * boolean OR query
     */
    protected SpanQuery convertSynonymToSpan(SpanConverterContainer container)
            throws QueryNodeException {
        SynonymQuery q = (SynonymQuery) container.query;
        SpanQuery[] spanClauses = new SpanQuery[q.getTerms().size()];

        int i = 0;
        for (Term t : q.getTerms()) {
            TermQuery sq = new TermQuery(t);
            SpanQuery result = getSpanQuery(new SpanConverterContainer(sq, 1, false));
            spanClauses[i] = result;
            i++;
        }

        return new SpanOrQuery(spanClauses);
    }

    /**
     * Count a positional multi-term pattern as the single user clause it represents.
     *
     * <p>IndexSearcher applies its nested-clause limit to QueryVisitor leaves after rewriting. The
     * normal SpanMultiTermQueryWrapper rewrite exposes each matching dictionary term as a separate
     * leaf, even though all those terms are alternatives in one positional clause. Keep the
     * pattern intact for that check, then use the same scoring span rewrite at weight creation
     * (or the configured top-terms span rewrite). The query is not expanded during clause
     * visiting, so weight creation does not repeat a prior term/state collection. The retained
     * working-set shape is the same as existing legal SpanOr expansions (one per-term
     * weight/postings stream), not a second term-set copy. The rewrite preserves every selected
     * positional, payload, and scoring behavior; memory remains proportional to matching terms
     * because each term needs its own span/postings stream.
     */
    private SpanQuery wrapMultiTermQuery(MultiTermQuery query) {
        return new DeferredSpanMultiTermQuery(query);
    }

    private static final class DeferredSpanMultiTermQuery extends SpanQuery {
        private final MultiTermQuery query;
        private final SpanMultiTermQueryWrapper.SpanRewriteMethod spanRewrite;

        private DeferredSpanMultiTermQuery(MultiTermQuery query) {
            this.query = query;
            MultiTermQuery.RewriteMethod rewrite = query.getRewriteMethod();
            this.spanRewrite =
                    rewrite instanceof TopTermsRewrite<?> topTermsRewrite
                            ? new SpanMultiTermQueryWrapper.TopTermsSpanBooleanQueryRewrite(
                                    topTermsRewrite.getSize())
                            : SpanMultiTermQueryWrapper.SCORING_SPAN_QUERY_REWRITE;
        }

        @Override
        public String getField() {
            return query.getField();
        }

        @Override
        public SpanWeight createWeight(IndexSearcher searcher, ScoreMode scoreMode,
                                       float boost) throws IOException {
            SpanQuery expanded = spanRewrite.rewrite(searcher.getIndexReader(), query);
            return expanded.createWeight(searcher, scoreMode, boost);
        }

        @Override
        public void visit(QueryVisitor visitor) {
            if (visitor.acceptField(getField())) {
                query.visit(visitor.getSubVisitor(Occur.MUST, this));
            }
        }

        @Override
        public String toString(String field) {
            return "SpanMultiTermQueryWrapper(" + query.toString(field) + ")";
        }

        @Override
        public boolean equals(Object other) {
            return sameClassAs(other)
                    && query.equals(((DeferredSpanMultiTermQuery) other).query);
        }

        @Override
        public int hashCode() {
            return classHash() ^ query.hashCode();
        }
    }

    class Leaf {
        public List<BooleanClause> members = new ArrayList<BooleanClause>();
        public BooleanClause left;
        public Leaf right;

        public Leaf(BooleanClause left, Leaf right) {
            this.left = left;
            this.right = right;
        }
    }

    /*
     * Creates a tree of the clauses, according to operator precedence:
     *
     * Thus: D +C -A -B becomes:
     *
     * - / \ A - / \ B + / \ C D
     */
    private Leaf constructTree(BooleanClause[] clauses) {
        List<BooleanClause> toProcess = Arrays.asList(clauses);
        Leaf leaf = new Leaf(null, null);
        leaf.members = toProcess;

        // from highest priority
        // findNots(leaf);
        // findAnds(leaf);
        // findOrs(leaf);
        return leaf;
    }

    private void findNots(Leaf leaf) {

        for (BooleanClause m : leaf.members) {
            if (m.getOccur().equals(Occur.MUST_NOT)) {
                leaf.members.remove(m);
                leaf.left = m;
            }
        }

    }


    public static class ConstantPayloadFunction extends PayloadFunction {

        private float boost = 1.0f;

        public ConstantPayloadFunction(float boost) {
            this.boost = boost;
        }

        @Override
        public float currentScore(int i, String s, int i1, int i2, int i3, float v, float v1) {
            return boost;
        }

        @Override
        public float docScore(int i, String s, int i1, float v) {
            return boost;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ConstantPayloadFunction) {
                return ((ConstantPayloadFunction) o).boost == boost;
            }

            return false;
        }

        @Override
        public String toString() {
            return "ConstantPayloadFunction(" + boost + ")";
        }
    }

    public static class EmptySpanQuery extends SpanQuery {

        private final Query wrappedQ;
        private final Spans emptySpan;

        public EmptySpanQuery(Query wrappedQ) {
            this.wrappedQ = wrappedQ;

            emptySpan = new Spans() {
                @Override
                public int nextStartPosition() throws IOException {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public int startPosition() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public int endPosition() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public int width() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public void collect(SpanCollector collector) throws IOException {
                    // TODO Auto-generated method stub

                }

                @Override
                public float positionsCost() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public int docID() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public int nextDoc() throws IOException {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public int advance(int target) throws IOException {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public long cost() {
                    // TODO Auto-generated method stub
                    return 0;
                }
            };
        }


        @Override
        public String getField() {
            if (wrappedQ instanceof RegexpQuery) {
                return ((RegexpQuery) wrappedQ).getField();
            }
            return null;
        }

        @Override
        public String toString(String field) {
            return "EmptySpanQuery(" + wrappedQ.toString() + ")";
        }

        @Override
        public SpanWeight createWeight(IndexSearcher searcher, ScoreMode needsScores, float boost) throws IOException {
            return new EmptySpanWeight(this, searcher, null, boost);
        }

        @Override
        public void visit(QueryVisitor visitor) {

        }


        @Override
        public boolean equals(Object obj) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int hashCode() {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    public static class EmptySpanWeight extends SpanWeight {


        public EmptySpanWeight(SpanQuery query, IndexSearcher searcher, Map<Term, TermStates> TermStatess, float boost)
                throws IOException {
            super(query, searcher, TermStatess, boost);
        }


        @Override
        public Explanation explain(LeafReaderContext context, int doc) throws IOException {
            return Explanation.noMatch("Ignored: " + parentQuery.toString(), new ArrayList<Explanation>());
        }

        @Override
        public SpanScorer scorer(LeafReaderContext context) throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void extractTermStates(Map<Term, TermStates> contexts) {
            // TODO Auto-generated method stub

        }

        @Override
        public Spans getSpans(LeafReaderContext ctx, Postings requiredPostings) throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isCacheable(LeafReaderContext ctx) {
            return false;
        }

    }
}
