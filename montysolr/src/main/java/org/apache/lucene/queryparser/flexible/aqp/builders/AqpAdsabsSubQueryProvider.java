package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.flexible.aqp.NestedParseException;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParser;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParserFull;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpChangeRewriteMethodProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.search.*;
import org.apache.lucene.search.SecondOrderCollector.FinalValueType;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.spans.SpanNegativeIndexRangeQuery;
import org.apache.lucene.queries.spans.SpanPositionRangeQuery;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.*;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.solr.servlet.SolrRequestParsers;
import org.apache.solr.uninverting.UninvertingReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;


/**
 * I know this is confusing. This is called in the building phase,
 * by that time all the parsing was already done. All the parsers
 * here return a QUERY
 *
 * @see AqpFunctionQueryBuilderProvider
 */
public class AqpAdsabsSubQueryProvider implements
        AqpFunctionQueryBuilderProvider {


    private static final String TOPN_SCORE_APPLIED = AqpChangeRewriteMethodProcessor.TOPN_SCORE_APPLIED;
    private static final String TOPN_SCORE_MODIFIER = AqpChangeRewriteMethodProcessor.TOPN_SCORE_MODIFIER;
    private static final String TOPN_OWNED_SCORING = "aqp.topn.owned.scoring";
    public static Map<String, AqpSubqueryParser> parsers = new HashMap<String, AqpSubqueryParser>();

    //TODO: make configurable
    static String[] citationSearchIdField = new String[]{"bibcode", "alternate_bibcode"};
    static String citationSearchRefField = "reference";

    private static LuceneCacheWrapper<NumericDocValues> getLuceneCache(FunctionQParser fp, String fieldname) throws SyntaxError {
        LuceneCacheWrapper<NumericDocValues> cacheWrapper;
        SchemaField field = fp.getReq().getSchema().getField(fieldname);
        try {
            cacheWrapper = LuceneCacheWrapper.getFloatCache(
                    field.getName(), UninvertingReader.Type.SORTED_SET_FLOAT,
                    fp.getReq().getSearcher().getSlowAtomicReader());
        } catch (IOException e) {
            throw new SyntaxError("Naughty, naughty server error", e);
        }
        return cacheWrapper;
    }
    private static boolean containsOwnedScoring(Query query, Set<Query> ownedScoring) {
        if (ownedScoring.contains(query)) {
            return true;
        }
        if (query instanceof FunctionScoreQuery) {
            return containsOwnedScoring(((FunctionScoreQuery) query).getWrappedQuery(), ownedScoring);
        }
        if (query instanceof BoostQuery) {
            return containsOwnedScoring(((BoostQuery) query).getQuery(), ownedScoring);
        }
        if (query instanceof DisjunctionMaxQuery) {
            for (Query disjunct : ((DisjunctionMaxQuery) query).getDisjuncts()) {
                if (containsOwnedScoring(disjunct, ownedScoring)) {
                    return true;
                }
            }
            return false;
        }
        if (query instanceof BooleanQuery) {
            for (BooleanClause clause : ((BooleanQuery) query).clauses()) {
                if (containsOwnedScoring(clause.getQuery(), ownedScoring)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean fullyOwnedScoring(Query query, Set<Query> ownedScoring) {
        if (ownedScoring.contains(query)) {
            return true;
        }
        if (query instanceof FunctionScoreQuery || query instanceof BoostQuery) {
            Query child = query instanceof FunctionScoreQuery
                    ? ((FunctionScoreQuery) query).getWrappedQuery()
                    : ((BoostQuery) query).getQuery();
            return fullyOwnedScoring(child, ownedScoring);
        }
        if (query instanceof DisjunctionMaxQuery) {
            Collection<Query> disjuncts = ((DisjunctionMaxQuery) query).getDisjuncts();
            return !disjuncts.isEmpty() && disjuncts.stream()
                    .allMatch(disjunct -> fullyOwnedScoring(disjunct, ownedScoring));
        }
        if (query instanceof BooleanQuery) {
            List<BooleanClause> clauses = ((BooleanQuery) query).clauses();
            return !clauses.isEmpty() && clauses.stream()
                    .allMatch(clause -> fullyOwnedScoring(clause.getQuery(), ownedScoring));
        }
        return false;
    }
    private static Query ensureCustomScoring(Query query, float modifier, Set<Query> ownedScoring) {
        if (fullyOwnedScoring(query, ownedScoring)) {
            return query;
        }
        if (query instanceof FunctionScoreQuery) {
            FunctionScoreQuery functionScore = (FunctionScoreQuery) query;
            if (!containsOwnedScoring(query, ownedScoring)) {
                Query scored = AqpScoringQueryNodeBuilder.wrapQuery(query, "cite_read_boost", modifier);
                ownedScoring.add(scored);
                return scored;
            }
            Query scoredChild = ensureCustomScoring(functionScore.getWrappedQuery(), modifier, ownedScoring);
            return new FunctionScoreQuery(scoredChild, functionScore.getSource());
        }
        if (query instanceof BoostQuery) {
            BoostQuery boostQuery = (BoostQuery) query;
            Query scoredChild = ensureCustomScoring(boostQuery.getQuery(), modifier, ownedScoring);
            return new BoostQuery(scoredChild, boostQuery.getBoost());
        }
        if (query instanceof DisjunctionMaxQuery) {
            DisjunctionMaxQuery disjunction = (DisjunctionMaxQuery) query;
            if (!containsOwnedScoring(query, ownedScoring)) {
                Query scored = AqpScoringQueryNodeBuilder.wrapQuery(query, "cite_read_boost", modifier);
                ownedScoring.add(scored);
                return scored;
            }
            List<Query> rebuiltDisjuncts = new ArrayList<>();
            for (Query disjunct : disjunction.getDisjuncts()) {
                rebuiltDisjuncts.add(ensureCustomScoring(disjunct, modifier, ownedScoring));
            }
            return new DisjunctionMaxQuery(rebuiltDisjuncts, disjunction.getTieBreakerMultiplier());
        }
        if (query instanceof BooleanQuery) {
            BooleanQuery booleanQuery = (BooleanQuery) query;
            if (!containsOwnedScoring(query, ownedScoring)) {
                Query scored = AqpScoringQueryNodeBuilder.wrapQuery(query, "cite_read_boost", modifier);
                ownedScoring.add(scored);
                return scored;
            }
            BooleanQuery.Builder rebuilt = new BooleanQuery.Builder();
            for (BooleanClause clause : booleanQuery.clauses()) {
                rebuilt.add(ensureCustomScoring(clause.getQuery(), modifier, ownedScoring), clause.getOccur());
            }
            rebuilt.setMinimumNumberShouldMatch(booleanQuery.getMinimumNumberShouldMatch());
            return rebuilt.build();
        }
        Query scored = AqpScoringQueryNodeBuilder.wrapQuery(query, "cite_read_boost", modifier);
        ownedScoring.add(scored);
        return scored;
    }
    @SuppressWarnings("unchecked")
    private static Set<Query> getOwnedScoring(Map<Object, Object> requestContext) {
        Object existing = requestContext.get(TOPN_OWNED_SCORING);
        if (existing instanceof Set<?>) {
            return (Set<Query>) existing;
        }
        Set<Query> created = Collections.newSetFromMap(new IdentityHashMap<>());
        requestContext.put(TOPN_OWNED_SCORING, created);
        return created;
    }

    /**
     * MoreLikeThisQuery normally puts all input in one field and caps analyzed
     * input at 5,000 tokens. Similarity preserves source-field associations,
     * while trending reads the full collected reader list.
     */
    private static class AdsMoreLikeThisQuery extends MoreLikeThisQuery {
        private final String fieldName;
        private final Map<String, Collection<Object>> fieldValues;

        AdsMoreLikeThisQuery(String likeText, String[] moreLikeFields,
                             Analyzer analyzer, String fieldName) {
            super(likeText, moreLikeFields, analyzer, fieldName);
            this.fieldName = fieldName;
            this.fieldValues = null;
        }

        AdsMoreLikeThisQuery(String likeText, String[] moreLikeFields, Analyzer analyzer,
                             String fieldName,
                             Map<String, Collection<Object>> fieldValues) {
            super(likeText, moreLikeFields, analyzer, fieldName);
            this.fieldName = fieldName;
            Map<String, Collection<Object>> copied = new HashMap<>();
            for (Map.Entry<String, Collection<Object>> entry : fieldValues.entrySet()) {
                copied.put(entry.getKey(), List.copyOf(entry.getValue()));
            }
            this.fieldValues = Map.copyOf(copied);
        }

        @Override
        public Query rewrite(IndexSearcher searcher) throws IOException {
            MoreLikeThis mlt = new MoreLikeThis(searcher.getIndexReader());
            mlt.setFieldNames(getMoreLikeFields());
            mlt.setAnalyzer(getAnalyzer());
            mlt.setMinTermFreq(getMinTermFrequency());
            if (getMinDocFreq() >= 0) {
                mlt.setMinDocFreq(getMinDocFreq());
            }
            mlt.setMaxQueryTerms(getMaxQueryTerms());
            mlt.setStopWords(getStopWords());
            BooleanQuery terms;
            if (fieldValues == null) {
                mlt.setMaxNumTokensParsed(Integer.MAX_VALUE);
                terms = (BooleanQuery) mlt.like(fieldName, new StringReader(getLikeText()));
            } else {
                terms = (BooleanQuery) mlt.like(fieldValues);
            }
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            for (BooleanClause clause : terms) {
                query.add(clause);
            }
            query.setMinimumNumberShouldMatch(
                    (int) (terms.clauses().size() * getPercentTermsToMatch()));
            return query.build();
        }
        @Override
        public boolean equals(Object other) {
            return other instanceof AdsMoreLikeThisQuery
                    && super.equals(other)
                    && fieldName.equals(((AdsMoreLikeThisQuery) other).fieldName)
                    && Objects.equals(fieldValues, ((AdsMoreLikeThisQuery) other).fieldValues);
        }

        @Override
        public int hashCode() {
            return 31 * super.hashCode() + Objects.hash(fieldName, fieldValues);
        }
    }

    static {

        /* @api.doc
         *
         * def lucene(query):
         * 		"""
         *    Default Lucene query parser
         * 		"""
         */
        parsers.put(LuceneQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), LuceneQParserPlugin.NAME);
                return q.getQuery();
            }
        });
        /**
         * comment XXX
         */
        parsers.put(FunctionQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), FunctionQParserPlugin.NAME);
                return q.getQuery();
            }
        });
        parsers.put(PrefixQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), PrefixQParserPlugin.NAME);
                return q.getQuery();
            }
        });
        parsers.put(BoostQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), BoostQParserPlugin.NAME);
                return q.getQuery();
            }
        });
        parsers.put(DisMaxQParserPlugin.NAME, new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), DisMaxQParserPlugin.NAME);
                return simplify(q.getQuery());
            }
        });
        parsers.put(ExtendedDismaxQParserPlugin.NAME, new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), ExtendedDismaxQParserPlugin.NAME);
                return simplify(q.getQuery());
            }
        });
        parsers.put(FieldQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), FieldQParserPlugin.NAME);
                return q.getQuery();
            }
        });
        parsers.put(RawQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                String qstr = fp.getString();
                if (!qstr.startsWith("{!")) {
                    throw new SyntaxError(
                            "Raw query parser requires you to specify local params, eg: raw({!f=field}" + fp.getString() + ")");
                }
                QParser q = fp.subQuery(qstr, RawQParserPlugin.NAME);
                return q.getQuery();
            }
        });
        parsers.put(NestedQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), NestedQParserPlugin.NAME);
                return q.getQuery();
            }
        });
        parsers.put(FunctionRangeQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), FunctionRangeQParserPlugin.NAME);
                return q.getQuery();
            }
        });
        parsers.put(SpatialFilterQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), SpatialFilterQParserPlugin.NAME);
                return q.getQuery();
            }
        });
        parsers.put(SpatialBoxQParserPlugin.NAME, new AqpSubqueryParser() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), SpatialBoxQParserPlugin.NAME);
                return q.getQuery();
            }
        });

        /* @api.doc
         *
         * def trending(query):
         * 		"""
         *    Finds the 200 most interesting papers first, then uses
         *    this initial set to collect *all* readers of these papers
         *    and then finds other docs these readers read.
         *
         *    Technical note: we are using modified MoreLikeThis
         *    functionality, with the following parameters:
         *
         *     - setMinTermFrequency(0)
         *		 - setMinDocFreq(2)
         *     - setMaxQueryTerms(200)
         *     - setBoost(2.0f)
         *     - setPercentTermsToMatch(0.0f)
         *
         *    @since 40.2.0.0
         *
         * 		"""
         *    return "trending(%s)" % query
         */
        parsers.put("trending", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                return parseReaderOverlap(fp, 200);
            }
        });

        /* @api.doc
         *
         * def coreads(query):
         * 		"""
         *    Finds papers read by the same readers as the query results.
         *    Unlike trending(), all eligible reader terms are retained so
         *    the score reflects the complete reader overlap.
         *
         * 		"""
         *    return "coreads(%s)" % query
         */
        parsers.put("coreads", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                return parseCoreads(fp);
            }
        });


        /* @api.doc
         *
         * def pos(query, start, end=None):
         * 		"""
         *    Positional search; returns only documents that
         *    are in the given position (range).
         *
         *    Example:
         *
         *    	```pos(author:accomazzi, 1)``` finds the papers
         *    			where 'accomazzi' is the first author
         *
         *      ```pos(author:accomazzi, 1, 1)``` finds the papers
         *          where 'accomazzi' is the only author
         *
         *      ```pos(author:accomazzi, 1, 5)``` finds the papers
         *          where 'accomazzi' is listed as 1st-5th author
         *
         *    Technical note:
         *
         *    This query will work only for indexes that contain
         *    positional information, such as: title, author. It
         *    will not work for other indexes, such as bibcode,
         *    keyword. Though we'll still allow you to query
         *    them (even if it is useless).
         *
         *
         *    Syntax note:
         *
         *    The old ADS Classic syntax was: ```^accomazzi$```
         *    where ```^``` means *first* and ```$``` means *last*.
         *    ADS Classic cannot search for position ranges, but
         *    the new system cannot search for the last (yet). It
         *    is low priority now.
         *
         *    @since 40.2.0.0
         *
         * 		"""
         *    return "pos(%s, %s, %s)" % (query, start, end or start)
         */
        parsers.put("pos", new AqpSubqueryParserFull() {
            @Override
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query query = fp.parseNestedQuery();
                int start = fp.parseInt();
                int end = start;

                if (fp.hasMoreArguments()) {
                    end = fp.parseInt();
                }

                if (fp.hasMoreArguments()) {
                    throw new NestedParseException("Wrong number of arguments");
                }

                assert end != 0;

                SpanConverter converter = new SpanConverter();
                converter.setWrapNonConvertible(true);

                // a field can have a different positionIncrementGap
                int positionIncrementGap = 1;
                String queryField = getField(query);
                if (fp.getReq() != null) {
                    IndexSchema schema = fp.getReq().getSchema();
                    SchemaField field = schema.getFieldOrNull(queryField);
                    if (field != null) {
                        FieldType fType = field.getType();
                        //if (!fType.isMultiValued()) {
                        //	throw new SyntaxError("The positional search doesn't make sense for: " + query);
                        //}
                        positionIncrementGap = fType.getIndexAnalyzer().getPositionIncrementGap(field.getName());
                        if (positionIncrementGap == 0)
                            positionIncrementGap = 1;
                    }
                }

                boolean wrapConstant = false;
                float boostFactor = 1.0f;
                if (query instanceof BoostQuery) {
                    boostFactor = ((BoostQuery) query).getBoost();
                    query = ((BoostQuery) query).getQuery();
                }
                if (query instanceof ConstantScoreQuery) {
                    query = ((ConstantScoreQuery) query).getQuery();
                    wrapConstant = true;
                }


                SpanQuery spanQuery;
                try {
                    spanQuery = converter.getSpanQuery(new SpanConverterContainer(query, 1, true));
                } catch (QueryNodeException e) {
                    SyntaxError ex = new SyntaxError(e.getMessage(), e);
                    ex.setStackTrace(e.getStackTrace());
                    throw ex;
                }

                if (start < 0 || end < 0) {
                    query = new SpanNegativeIndexRangeQuery(spanQuery, queryField, start, end, positionIncrementGap);
                } else {
                    query = new SpanPositionRangeQuery(spanQuery, (start - 1) * positionIncrementGap, end * positionIncrementGap); //lucene counts from zeroes
                }

                if (wrapConstant)
                    query = new ConstantScoreQuery(query);
                if (boostFactor != 1.0f)
                    query = new BoostQuery(query, boostFactor);
                return query;
            }

            private String getField(Query query) throws SyntaxError {

                if (query instanceof TermQuery) {
                    return ((TermQuery) query).getTerm().field();
                } else if (query instanceof SynonymQuery) {
                    for (Term t : ((SynonymQuery) query).getTerms()) {
                        return t.field();
                    }
                } else if (query instanceof BooleanQuery) {
                    HashSet<String> s = new HashSet<String>();
                    for (BooleanClause c : ((BooleanQuery) query).clauses()) {
                        s.add(getField(c.getQuery()));
                    }

                    if (s.size() > 1) {
                        throw new SyntaxError("`pos` queries cannot handle boolean queries that span multiple fields, " +
                                "including virtual field queries. Try using a non-virtual field instead.");
                    }

                    return (String) s.toArray()[0];
                } else if (query instanceof BoostQuery) {
                    return getField(((BoostQuery) query).getQuery());
                } else if (query instanceof ConstantScoreQuery) {
                    return getField(((ConstantScoreQuery) query).getQuery());
                } else if (query instanceof MultiTermQuery) {
                    return ((MultiTermQuery) query).getField();
                } else if (query instanceof DisjunctionMaxQuery) {
                    String field = null;

                    for (Query q : ((DisjunctionMaxQuery) query).getDisjuncts()) {
                        String f = getField(q);

                        if (field == null) {
                            field = f;
                        } else if (!field.equals(f)) {
                            throw new SyntaxError("`pos` queries cannot handle disjunction queries that span multiple fields, " +
                                    "including virtual field queries. Try using a non-virtual field instead.");
                        }
                    }

                    return field;
                } else {
                    // last resort
                    return query.toString().split(":")[0];
                }

                return null;
            }
        });

        /* @api.doc
         *
         * def classic_relevance(query, ratio=0.5):
         * 		"""
         *    Toy-implementation of the ADS Classic relevance score
         *    algorithm. You can wrap any query and obtain the
         *    hits sorted in the ADS Classic ways (sort of)
         *
         *    Technical note:
         *
         *    This is inefficient and not to be used in production.
         *    We apply the **boost factor** that was computed beforehand
         *    by ADS Classic to each document that matches. (We are not
         *    scoring docs that are not selected by Lucene).
         *    The boost factor is inside ```cite_read_boost``` field -
         *    we'll use cache to retrieve these values fast,
         *    but it is still inefficient
         *
         *
         *    ADS Classic score is implemented as:
         *
         *    ```new_score = (0.5 * norm(lucene_score)) + (0.5 * cite_read_boost)```
         *
         *    where:
         *
         *       norm(LS) = normalized score (in this case it will be a Lucene
         *                  score, normalized to be in the range 1-0, where
         *                  1 = the first, best hit; LS/MaximumLuceneScore
         *
         *       cite_read_boost = the document boosts are combination of
         *                  normalized reads and cites:
         *                  ```cite_read_boost = log(1 + cites + norm_reads)```
         *
         *                  where:
         *
         *                  	```norm_reads``` are normalized values for
         *                    reads over the past two years
         *
         *
         *
         *    @experimental
         *    @synonym cr()
         *    @since 40.2.2.0
         *    @since 40.3.0.1 - added parameter to configure ratio
         *
         * 		"""
         *    return "classic_relevance(%s, %0.2f)" % (query,ratio)
         */
        parsers.put("classic_relevance", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {

                Query innerQuery = fp.parseNestedQuery();
                float ratio = 0.5f;
                if (fp.hasMoreArguments()) {
                    ratio = fp.parseFloat();
                }

                if (ratio < 0 || ratio > 1.0f) {
                    throw new SyntaxError("The ratio must be in the range 0.0-1.0");
                }

                @SuppressWarnings("unchecked")
                SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
                        (CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));

                LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "cite_read_boost");

                return new SecondOrderQuery(innerQuery,
                        new SecondOrderCollectorAdsClassicScoringFormula(citationsWrapper, boostWrapper, ratio));
            }
        });
        parsers.put("cr", parsers.get("classic_relevance"));


        /* @api.doc
         *
         * def topn(max, query, spec=None):
         * 		"""
         *    Limit results to the best top N (by their ranking or sort order)
         *
         *    @param max
         *    	- integer, how many results should be considered
         *    @param query
         *    	- query object
         *    @param spec
         *    	- str, can be either 'relevance' or
         *        sort specification in the SOLR format
         *
         *    Example:
         *
         *    	```topn(200, title:hubble)``` returns only the
         *         first 200 papers based on the relevancy score
         *
         *      ```topn(200, citations(title:hubble), citation_count desc)```
         *         returns only the
         *         first 200 papers, but because the results are
         *         sorted by number of citations, you will get the first
         *         200 most cited papers
         *
         *
         *    Technical note:
         *
         *    We do not impose limit of hits that you can return with
         *    this operator. But you must be aware that the query is
         *    going to be slower than normal queries.
         *
         *    @since 40.2.2.0
         *    """
         *    return "topn(%s, %s, '%s')" % (int(max), query, spec or 'score')
         *
         */
        parsers.put("topn", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                int topN = -1;
                try {
                    topN = fp.parseInt();
                } catch (NumberFormatException e) {
                    throw new SyntaxError("The function signature is topn(int, query, [sort order]). Error: " + e.getMessage());
                }

                if (topN < 1) {  //|| topN > 50000 - previously, i was limiting the fields
                    throw new SyntaxError("Hmmm, the first argument of your operator must be a positive number.");
                }

                SolrQueryRequest req = fp.getReq();
                SolrParams requestParams = req.getParams();
                String modifierValue = requestParams.get("aqp.classic_scoring.modifier");
                if (modifierValue == null) {
                    Object contextModifier = req.getContext().get(TOPN_SCORE_MODIFIER);
                    modifierValue = contextModifier == null ? null : contextModifier.toString();
                }
                float scoreModifier = modifierValue == null ? 0.5f : Float.parseFloat(modifierValue);
                Map<Object, Object> requestContext = req.getContext();
                Set<Query> ownedScoring = getOwnedScoring(requestContext);
                Object previousScoreState = requestContext.put(TOPN_SCORE_APPLIED, Boolean.TRUE);
                QParser eqp;
                Query innerQuery;
                try {
                    eqp = fp.subQuery(fp.parseId(), "aqp");
                    innerQuery = eqp.getQuery();
                } finally {
                    if (previousScoreState == null) {
                        requestContext.remove(TOPN_SCORE_APPLIED);
                    } else {
                        requestContext.put(TOPN_SCORE_APPLIED, previousScoreState);
                    }
                }

                if (innerQuery == null) {
                    throw new SyntaxError("This query is empty: " + eqp.getString());
                }

                String sortOrRank = "score desc";
                if (fp.hasMoreArguments()) {
                    sortOrRank = fp.parseId();
                }

                sortOrRank = sortOrRank.toLowerCase();

                if (sortOrRank.contains("\"") || sortOrRank.contains("'")) {
                    sortOrRank = sortOrRank.substring(1, sortOrRank.length() - 1);
                }

                SortSpec sortSpec = SortSpecParsing.parseSortSpec(sortOrRank, fp.getReq());

                /*
                 * A top-level AQP query can be rescored with cite_read_boost.  The
                 * topn collector must rank on that same score whenever the requested
                 * sort includes score; otherwise it truncates by the inner Lucene score
                 * and only applies cite_read_boost after the wrong documents have
                 * been selected.
                 */
                boolean scoreSort = sortSpec.getSort() == null || sortSpec.includesScore();
                boolean innerScoreApplied = fullyOwnedScoring(innerQuery, ownedScoring);
                if (scoreSort && !innerScoreApplied) {
                    innerQuery = ensureCustomScoring(innerQuery, scoreModifier, ownedScoring);
                    innerScoreApplied = true;
                }
                Query q;
                if (sortSpec.getSort() == null) {
                    q = new SecondOrderQuery(innerQuery,
                            new SecondOrderCollectorTopN(topN));
                } else {

                    SolrIndexSearcher searcher = fp.getReq().getSearcher();

                    Sort sortOrder;
                    try {
                        sortOrder = searcher.weightSort(sortSpec.getSort());
                    } catch (IOException e) {
                        throw new SyntaxError("I am sorry, you can't use " + sortOrRank + " for topn() sorting. Reason: " + e.getMessage());
                    }

                    q = new SecondOrderQuery(innerQuery,
                            new SecondOrderCollectorTopN(sortOrRank, topN, sortOrder));
                }

                // Field-only sorts select documents without custom scores; apply the
                // configured score to the selected results afterward.
                boolean outputScoreApplied = innerScoreApplied
                        && (sortSpec.getSort() == null || sortSpec.includesScore());
                if (!outputScoreApplied) {
                    Query scored = AqpScoringQueryNodeBuilder.wrapQuery(q, "cite_read_boost", scoreModifier);
                    ownedScoring.add(scored);
                    return scored;
                }
                ownedScoring.add(q);
                return q;
            }
        });

        /* @api.doc
         *
         * def citations(query):
         * 		"""
         *    Finds set of papers that have **P** in their reference list
         *
         *    'P' is the set of papers that will be selected by the query
         *
         *    Example:
         *
         *    	```citations(title:hubble)``` returns papers (potentionally
         *         hundreds of thousands!) that are citing papers P
         *
         *
         *      ```citations(citations(author:huchra))``` returns papers
         *         (potentionally millions!) that are citing papers that
         *         are citing papers written by 'huchra'
         *
         *
         *    Technical note:
         *
         *    We have optimized this query so that it works well with
         *    millions of hits. But don't expect miracles. 0.5M hits
         *    takes few hundred milliseconds; 2M hits will take seconds
         *    (but less than 10s, since that is the speed the old desktop
         *    did it)
         *
         *
         *    @since 40.1.0.0
         *    """
         *    return "citations(%s)" % (query,)
         *
         */
        parsers.put("citations", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();

                @SuppressWarnings("unchecked")
                SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
                        (CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));

                return new SecondOrderQuery(innerQuery,
                        new SecondOrderCollectorCitedBy(citationsWrapper), false);
            }
        });


        /* @api.doc
         *
         * def references(query):
         * 		"""
         *    Finds set of papers that **are** in the references list of **P**
         *
         *    'P' is the set of papers that will be selected by the query
         *
         *    Example:
         *
         *    	```references(title:hubble)``` returns papers (potentionally
         *         few hundred) that are **cited by** papers that have 'hubble'
         *         in their title
         *
         *
         *      ```references(author:huchra)``` returns papers
         *         that your favorite author cites
         *
         *
         *    Technical note:
         *
         *    The same caveats as citations()
         *
         *
         *    @since 40.1.0.0
         *    """
         *    return "references(%s)" % (query,)
         *
         */
        parsers.put("references", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();

                @SuppressWarnings("unchecked")
                SolrCacheWrapper<CitationCache<Object, Integer>> referencesWrapper = new SolrCacheWrapper.ReferencesCache(
                        (CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));


                return new SecondOrderQuery(innerQuery,
                        new SecondOrderCollectorCitesRAM(referencesWrapper), false);
            }
        });

        /* @api.doc
         *
         * def joincitations(query):
         * 		"""
         *    Equivalent of citations() but implemented using lucene block-join
         *
         *
         *    @experimental
         *    @access devel
         *    @since 40.1.0.0
         *    """
         *    return "joincitations(%s)" % (query,)
         */
        parsers.put("joincitations", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();
                SolrQueryRequest req = fp.getReq();
                try {
                    // XXX: not sure if i can use several fields: citationSearchIdField
                    return JoinUtil.createJoinQuery("bibcode", false, "reference", innerQuery,
                            req.getSearcher(), ScoreMode.Avg);
                } catch (IOException e) {
                    throw new SyntaxError(e.getMessage());
                }
            }
        });

        /* @api.doc
         *
         * def joinreferences(query):
         * 		"""
         *    Equivalent of references() but implemented using lucene block-join
         *
         *
         *    @experimental
         *    @access devel
         *    @since 40.1.0.0
         *    """
         *    return "joinreferences(%s)" % (query,)
         *
         */
        parsers.put("joinreferences", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();
                SolrQueryRequest req = fp.getReq();
                try {
                    return JoinUtil.createJoinQuery("bibcode", false, "citation", innerQuery,
                            req.getSearcher(), ScoreMode.Avg);
                } catch (IOException e) {
                    throw new SyntaxError(e.getMessage());
                }
            }
        });


        /* @api.doc
         *
         * def useful(query):
         * 		"""
         *    What experts are citing; this mimics the ADS Classic implementation
         *    ```references(topn(200, classic_relevance(Q)))```
         *
         *    In other words, this will first find papers using the inner query,
         *    it will re-score them using the ADS classic ranking formula,
         *    then selects 200 top papers. And then get **references from** these
         *    200 papers.
         *
         *    @experimental
         *    @since 40.2.0.0
         *    """
         *    return "useful(%s)" % (query,)
         *
         */
        parsers.put("useful", new AqpSubqueryParserFull() { // this function values can be analyzed
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();

                @SuppressWarnings("unchecked")
                SolrCacheWrapper<CitationCache<Object, Integer>> referencesWrapper = new SolrCacheWrapper.ReferencesCache(
                        (CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));

                LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "cite_read_boost");

                SecondOrderQuery outerQuery =
                        new SecondOrderQuery( // references
                                new SecondOrderQuery( // topn
                                        //new SecondOrderQuery(innerQuery, // classic_relevance
                                        //		new SecondOrderCollectorAdsClassicScoringFormula(referencesWrapper, boostWrapper)),
                                        innerQuery,
                                        new SecondOrderCollectorTopN(200)),
                                new SecondOrderCollectorCitesRAM(referencesWrapper));

                outerQuery.getcollector().setFinalValueType(FinalValueType.ABS_COUNT);
                return outerQuery;
            }

        });

        /* @api.doc
         *
         * def useful2(query):
         * 		"""
         *    What experts are citing; original implementation of useful()
         *    -- using special collector
         *
         *    Technical details:
         *
         *    This function will add the cite_read_boost factor (from the
         *    1st order set) to the score (of the 2nd order result set).
         *    If no boost factor is available, doc will be penalized by
         *    having its score lowered by 20%
         *
         *    @access devel
         *    @experimental
         *    @since 40.1.2.0
         *    """
         *    return "useful2(%s)" % (query,)
         *
         */
        parsers.put("useful2", new AqpSubqueryParserFull() { // this function values can be analyzed
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();

                @SuppressWarnings("unchecked")
                SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
                        (CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));

                //TODO: make configurable the name of the field
                LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "cite_read_boost");

                return new SecondOrderQuery(new SecondOrderQuery(innerQuery, new SecondOrderCollectorTopN(200)),
                        new SecondOrderCollectorOperatorExpertsCiting(citationsWrapper, boostWrapper));
            }
        });


        /* @api.doc
         *
         * def reviews(query):
         * 		"""
         *    What is cited by experts; this mimics the ADS Classic implementation
         *    is: ```citations(topn(200, Q, "citations desc"))```
         *
         *    In other words, this will first find papers using the query,
         *    it will re-score them using the ADS classic ranking formula,
         *    then selects 200 top papers. And then get **citations for** these
         *    200 papers.
         *
         *    @experimental
         *    @since 40.2.0.0
         *
         *    @change 63.1.1.20
         *    Added second argument to adjust how much weight is given to the text
         *    features and how much citations are going to be the predominant factor.
         *    For example ratio of '1.0' means that final score will be calculated
         *    based on the match of query against the text. Ratio of '0.0' means that
         *    citations is what governs the order of the inner topn(200) papers.
         *    Default is 0.0 and the range must be between 0.0 and 1.0f
         *
         *    Changed the implementation from ```citations(topn(200, classic_relevance(Q)))```
         *    to ```citations(topn(200, Q, "citations desc"))```
         *
         *    """
         *    return "reviews(%s)" % (query,)
         *
         */
        parsers.put("reviews", new AqpSubqueryParserFull() { // this function values can be analyzed
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();

                @SuppressWarnings("unchecked")
                SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
                        (CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));

                LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "citation_count");

                float textWeightRatio = 0.0f; // 0.0f == all what matters are citations
                if (fp.hasMoreArguments())
                    textWeightRatio = fp.parseFloat();

                SecondOrderQuery outerQuery =
                        new SecondOrderQuery( // citations
                                new SecondOrderQuery( // topn
                                        //innerQuery,
                                        new SecondOrderQuery(innerQuery, // sort by citations
                                                new SecondOrderCollectorAdsClassicScoringFormula(citationsWrapper, boostWrapper, textWeightRatio)),
                                        new SecondOrderCollectorTopN(200)),
                                new SecondOrderCollectorCitedBy(citationsWrapper));

                outerQuery.getcollector().setFinalValueType(FinalValueType.ABS_COUNT);
                return outerQuery;
            }

        });


        /* @api.doc
         *
         * def instructive(query):
         * 		"""
         *    The synonym of @see reviews
         *    """
         *    return reviews(query)
         */
        parsers.put("instructive", parsers.get("reviews"));

        // original impl of reviews() = find papers that cite the most cited papers
        parsers.put("reviews2", new AqpSubqueryParserFull() { // this function values can be analyzed
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();

                @SuppressWarnings("unchecked")
                SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
                        (CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));

                LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "cite_read_boost");

                return new SecondOrderQuery(new SecondOrderQuery(innerQuery, new SecondOrderCollectorTopN(200)),
                        new SecondOrderCollectorCitingTheMostCited(citationsWrapper, boostWrapper));
            }
        });
        parsers.put("citis", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();

                @SuppressWarnings("unchecked")
                SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
                        (CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));

                return new SecondOrderQuery(innerQuery,
                        new SecondOrderCollectorCites(citationsWrapper, new String[]{citationSearchRefField}), false);

            }
        });
        parsers.put("aqp", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), "aqp");
                return q.getQuery();
            }
        });

        parsers.put("adismax", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                QParser q = fp.subQuery(fp.getString(), "adismax");
                return simplify(q.getQuery());
            }
        });

        parsers.put("edismax_nonanalyzed", new AqpSubqueryParserFull() { // used for nodes that were already analyzed
            public Query parse(FunctionQParser fp) throws SyntaxError {
                final String original = fp.getString();
                QParser ep = fp.subQuery("xxx", "adismax");
                Query q = ep.getQuery();
                QParser fakeParser = new QParser(original, null, null, null) {
                    @Override
                    public Query parse() throws SyntaxError {
                        String[] parts = getString().split(":");
                        return new TermQuery(new Term(parts[0], original));
                    }
                };
                return simplify(reParse(q, fakeParser, TermQuery.class));
            }
        });
        parsers.put("edismax_combined_aqp", new AqpSubqueryParserFull() { // will decide whether new aqp() parse is needed
            public Query parse(FunctionQParser fp) throws SyntaxError {
                final String original = fp.getString();
                //System.out.println("edismax fed: " + original);
                QParser eqp = fp.subQuery(original, "adismax");
                Query q = eqp.getQuery();
                //System.out.println("edismax produced: " + q);
                return simplify(q);
            }

            protected Query swimDeep(DisjunctionMaxQuery query) throws SyntaxError {
                List<Query> parts = new ArrayList<>(query.getDisjuncts());
                for (int i = 0; i < parts.size(); i++) {
                    Query oldQ = parts.get(i);
                    String field = null;
                    if (oldQ instanceof TermQuery) {
                        field = toBeAnalyzedAgain(((TermQuery) oldQ));
                    } else if (oldQ instanceof BooleanQuery) {
                        List<BooleanClause> clauses = ((BooleanQuery) oldQ).clauses();
                        if (clauses.size() > 0) {
                            Query firstQuery = clauses.get(0).getQuery();
                            if (firstQuery instanceof TermQuery) {
                                field = toBeAnalyzedAgain(((TermQuery) firstQuery));
                            }
                        }
                    }
                    if (field != null) {
                        parts.set(i, reAnalyze(field, getParser().getString(),
                                oldQ instanceof BoostQuery ? ((BoostQuery) oldQ).getBoost() : null));
                    } else {
                        parts.set(i, swimDeep(oldQ));
                    }
                }
                return new DisjunctionMaxQuery(parts, query.getTieBreakerMultiplier());
            }

            private String toBeAnalyzedAgain(TermQuery q) {
                //String f = q.getTerm().field();
                //if (f.equals("author")) {
                //  return "author";
                //}
                return null;
                //return f; // always re-analyze
            }

            private Query reAnalyze(String field, String value, Float boost) throws SyntaxError {
                QParser fParser = getParser();
                //System.out.println(field+ ":"+fParser.getString() + "|value=" + value);
                QParser aqp = fParser.subQuery(field + ":" + getOriginalInput(), "aqp");
                Query q = aqp.getQuery();
                if (boost != null && boost != 1.0f) {
                    q = new BoostQuery(q, boost);
                }
                return q;
            }
        });
        parsers.put("edismax_always_aqp", new AqpSubqueryParserFull() { // will use edismax to create top query, but the rest is done by aqp
            public Query parse(FunctionQParser fp) throws SyntaxError {
                final String original = fp.getString();
                final boolean exactSearch = original.startsWith("{!adismax")
                        && original.contains("aqp.exact.search=true");
                final int localParamsEnd = original.indexOf('}');
                final String queryText = localParamsEnd >= 0
                        ? original.substring(localParamsEnd + 1)
                        : original;
                QParser eqp = fp.subQuery("xxx", "adismax");
                Query q = eqp.getQuery();
                fp.setString(queryText);
                return simplify(reParse(q, fp, Boolean.valueOf(exactSearch), TermQuery.class));
            }

            protected Query swimDeep(DisjunctionMaxQuery query) throws SyntaxError {
                List<Query> parts = new ArrayList<>(query.getDisjuncts().size());
                for (Query oldQ : query.getDisjuncts()) {
                    Query candidate = oldQ;
                    Float boost = null;
                    if (candidate instanceof BoostQuery) {
                        boost = ((BoostQuery) candidate).getBoost();
                        candidate = ((BoostQuery) candidate).getQuery();
                    }
                    String field = null;
                    if (candidate instanceof TermQuery) {
                        field = ((TermQuery) candidate).getTerm().field();
                    } else if (candidate instanceof BooleanQuery) {
                        List<BooleanClause> clauses = ((BooleanQuery) candidate).clauses();
                        if (clauses.size() > 0) {
                            Query firstQuery = clauses.get(0).getQuery();
                            if (firstQuery instanceof BoostQuery) {
                                firstQuery = ((BoostQuery) firstQuery).getQuery();
                            }
                            if (firstQuery instanceof TermQuery) {
                                field = ((TermQuery) firstQuery).getTerm().field();
                            }
                        }
                    }
                    if (field != null) {
                        parts.add(reAnalyze(field, getParser().getString(), boost));
                    } else {
                        parts.add(swimDeep(oldQ));
                    }
                }
                return new DisjunctionMaxQuery(parts, query.getTieBreakerMultiplier());
            }

            private Query reAnalyze(String field, String value, Float boost) throws SyntaxError {
                QParser fParser = getParser();
                boolean exactSearch = Boolean.TRUE.equals(getReParseContext());
                String exactPrefix = exactSearch ? "=" : "";
                QParser aqp = fParser.subQuery(exactPrefix + field + ":" + getOriginalInput(), "aqp");
                Query q = aqp.getQuery();
                if (boost != null && boost != 1.0f) {
                    q = new BoostQuery(q, boost);
                }
                return q;
            }
        });

        parsers.put("tweak", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {

                String configuration = fp.parseId();
                Query q = fp.parseNestedQuery();

                MultiMapSolrParams params = SolrRequestParsers.parseQueryString(configuration);

                if (params.get("collector_final_value", null) != null) {
                    String cfv = params.get("collector_final_value", "avg");
                    if (q instanceof SecondOrderQuery) {
                        SecondOrderCollector collector = ((SecondOrderQuery) q).getcollector();
                        try {
                            collector.setFinalValueType(SecondOrderCollector.FinalValueType.valueOf(cfv));
                        } catch (IllegalArgumentException e) {
                            throw new SyntaxError("Wrong parameter: " + e.getMessage(), e);
                        }
                    }
                }
                return q;
            }
        });

        // helper method; SOLR is not warming up caches when index is opened first time
        // so we have to do it ourselves
        parsers.put("warm_cache", new AqpSubqueryParserFull() {
            @SuppressWarnings("unchecked")
            public Query parse(FunctionQParser fp) throws SyntaxError {

                final SolrQueryRequest req = fp.getReq();
                @SuppressWarnings("rawtypes") final CitationCache cache = (CitationCache) req.getSearcher().getCache("citations-cache");
                if (!cache.isWarmingOrWarmed()) {
                    if (cache.size() > 0) {
                        return new MatchNoDocsQuery(); // we only allow it once (solr warms caches after first searcher was opened)
                    }
                    cache.warm(req.getSearcher(), cache);
                }
                return new MatchNoDocsQuery();
            }
        });

        /* @api.doc
         *
         * def constant(query):
         * 		"""
         *    Applies constant score (that can be set by boost factor)
         *
         *    Example:
         *
         *    	```constant(title:hubble^2)```
         *
         *
         *    @since 63.1.0.24
         *    """
         *    return "constant(%s)" % (query,)
         *
         */
        parsers.put("constant", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                Query innerQuery = fp.parseNestedQuery();

                return new ConstantScoreQuery(innerQuery);
            }
        });

        parsers.put("docs", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {

                SolrQueryRequest req = fp.getReq();
                String input = fp.getString();
                String filterName = input.substring(1, input.length() - 1).trim();


                // pick the content stream (actually a filter)
                List<ContentStream> streams = new ArrayList<ContentStream>(1);
                if (req.getContentStreams() != null) {
                    ContentStream lcs = null;
                    for (ContentStream cs : req.getContentStreams()) {
                        if (filterName.equals(cs.getName())) {
                            streams.add(cs);
                        }
                    }
                }


                // create a new local request with just this one content stream
                ModifiableSolrParams params = new ModifiableSolrParams();
                params.set("defType", "bitset");

                SolrQueryRequestBase locReq = new LocalSolrQueryRequest(req.getCore(), params);
                try {
                    locReq.setContentStreams(streams);

                    // stream can also be in local params...
                    String filter = req.getParams().get(filterName);
                    String qString;
                    if (filter != null) {
                        qString = filter.trim();
                    } else {
                        qString = "*:*";
                    }
                    params.set("q", qString);

                    Query q;

                    if (streams.size() == 0 && qString.equals("*:*"))
                        throw new SolrException(ErrorCode.BAD_REQUEST, "Invalid query, missing stream for bigquery(" + input + ")");

                    try {
                        q = QParser.getParser(qString, "bitset", true, locReq).getQuery();
                    } catch (SyntaxError e) {
                        throw new SolrException(ErrorCode.BAD_REQUEST, "Invalid query bigquery(" + input + ")", e);
                    }

                    return q;
                } finally {
                    locReq.close();
                }
            }
        });

        /* @api.doc
         *
         * def similar(queryOrText, fields, maxQueryTerms, docToSearch, minTermFreq, minDocFreq, percentToMatch):
         *    """
         *    Finds similar documents:
         *
         *      @param queryOrText: string, this can be a query or input
         *      @param fields: list of fields separated by spaces, or special token 'input'
         *        which means "use the query as is, as input"
         *      @param maxQueryTerms: modifies similarity search, only this many terms will
         *        be considered during the search (those terms are NOT the first X collected,
         *        but they will be the first X terms weighted by TFIDF)
         *      @param docToSearch: how many documents to collect in the first phase, is ignored
         *        when fields='input'
         *      @param minTermFreq: term is selected only if its frequency is this or higher
         *      @param minDocFreq: selected term must be present in at least that many documents
         *      @param percentToMatch: ratio of terms that have to be present in the selected
         *        documents, default is 0.0f. For example, if 100 terms was used to discover
         *        similar docs, and if the ratio was 0.3f - then 30 terms must be present in the
         *        docs that are returned.
         *
         *
         *    Example:
         *
         *      ```similar(title:hubble^2, abstract, 100)```
         *
         *      Will find all documents that have 'hubble' in title, will collect first and use their terms
         *      to discover similar documents in abstract. It will only use 100 search tokens.
         *
         *
         *    @since 63.1.0.24
         *    @since 63.1.0.57  - exposed parameters to modify similar() behaviour
         *    """
         *    return "similar(%s)" % (query,)
         *
         */
        parsers.put("similar", new AqpSubqueryParserFull() {
            public Query parse(FunctionQParser fp) throws SyntaxError {
                String input = fp.parseId();

                String toLoad = "abstract title";
                if (fp.hasMoreArguments())
                    toLoad = fp.parseId();

                int maxQueryTerms = 100;
                if (fp.hasMoreArguments())
                    maxQueryTerms = Math.min(fp.parseInt(), maxQueryTerms);

                int docToSearch = 200;
                if (fp.hasMoreArguments())
                    docToSearch = Math.min(fp.parseInt(), docToSearch);

                int minTermFrequency = 2;
                if (fp.hasMoreArguments())
                    minTermFrequency = Math.max(fp.parseInt(), 1);

                int minDocFrequency = 2;
                if (fp.hasMoreArguments())
                    minDocFrequency = Math.max(fp.parseInt(), 1);

                float percentToMatch = 0.0f;
                if (fp.hasMoreArguments())
                    percentToMatch = fp.parseFloat();

                SolrQueryRequest req = fp.getReq();
                FixedBitSet toIgnore = null;
                Map<String, Collection<Object>> fieldValues = new HashMap<>();
                String[] fieldsToLoad = toLoad.split(" ");

                if (toLoad.indexOf("input") > -1) {
                    if (toLoad.length() > 5) {
                        fieldsToLoad = toLoad.substring(toLoad.indexOf("input") + 6).split(" ");
                    } else {
                        fieldsToLoad = new String[]{"abstract"};
                    }
                    for (String field : fieldsToLoad) {
                        fieldValues.put(field, Collections.singletonList(input));
                    }
                } else {

                    fieldsToLoad = toLoad.split(" ");
                    QParser aqp = fp.subQuery(input, "aqp");
                    Query innerQuery = aqp.parse();

                    HashSet<String> docFields = new HashSet<String>();
                    Collections.addAll(docFields, fieldsToLoad);

                    SolrIndexSearcher searcher = req.getSearcher();


                    toIgnore = new FixedBitSet(searcher.maxDoc());

                    TopDocs topDocs;

                    try {
                        topDocs = searcher.search(innerQuery, docToSearch);
                        if (topDocs.totalHits.value == 0)
                            return new MatchNoDocsQuery();

                        for (ScoreDoc d : topDocs.scoreDocs) {
                            toIgnore.set(d.doc);
                            Document vals = searcher.doc(d.doc, docFields);
                            for (String f : docFields) {
                                Collection<Object> values = fieldValues.computeIfAbsent(
                                        f, ignored -> new ArrayList<>());
                                for (String x : vals.getValues(f)) {
                                    values.add(x);
                                }
                            }
                        }

                        // set some better defaults when it is a rare doc
                        if (topDocs.totalHits.value < 2) {
                            minTermFrequency = 0;
                            minDocFrequency = 1;
                        }

                    } catch (IOException e) {
                        throw new SyntaxError(e.getMessage(), e);
                    }

                }


                Analyzer analyzer = req.getSchema().getIndexAnalyzer();
                AdsMoreLikeThisQuery mlt = new AdsMoreLikeThisQuery(input, fieldsToLoad,
                        analyzer, fieldsToLoad[0], fieldValues);

                mlt.setMinTermFrequency(minTermFrequency);
                mlt.setMinDocFreq(minDocFrequency);

                mlt.setPercentTermsToMatch(percentToMatch);
                mlt.setMaxQueryTerms(maxQueryTerms);

                if (toIgnore != null) {
                    BooleanQuery.Builder query = new BooleanQuery.Builder();
                    query.add(mlt, BooleanClause.Occur.MUST);
                    query.add(new BitSetQuery(toIgnore), BooleanClause.Occur.MUST_NOT);
                    return query.build();
                } else {
                    return mlt;
                }

            }
        });

    }

    private static Query parseNestedFunctionQuery(FunctionQParser fp) throws SyntaxError {
        String nestedQuery = "{!aqp aqp.caret_in_function=true}" + fp.getString();
        QParser aqp = fp.subQuery(nestedQuery, "aqp");
        return aqp.parse();
    }

    private static Query parseCoreads(FunctionQParser fp) throws SyntaxError {
        Query innerQuery = parseNestedFunctionQuery(fp);
        SolrQueryRequest req = fp.getReq();
        SolrIndexSearcher searcher = req.getSearcher();
        final Set<String> readers = new HashSet<String>();
        final String fieldName = "reader";
        final HashSet<String> fieldsToLoad = new HashSet<String>();
        fieldsToLoad.add(fieldName);

        try {
            searcher.search(innerQuery, new SimpleCollector() {
                private LeafReader reader;

                @Override
                public org.apache.lucene.search.ScoreMode scoreMode() {
                    return org.apache.lucene.search.ScoreMode.COMPLETE_NO_SCORES;
                }

                @Override
                public void collect(int doc) throws IOException {
                    Document document = reader.document(doc, fieldsToLoad);
                    Collections.addAll(readers, document.getValues(fieldName));
                }

                @Override
                public void doSetNextReader(LeafReaderContext context) throws IOException {
                    reader = context.reader();
                }
            });
        } catch (IOException e) {
            throw new SyntaxError(e.getMessage(), e);
        }

        return new BoostQuery(new CoreadsQuery(fieldName, readers), 2.0f);
    }

    private static Query parseReaderOverlap(FunctionQParser fp, int maxQueryTerms) throws SyntaxError {
        Query innerQuery = parseNestedFunctionQuery(fp);

        SolrQueryRequest req = fp.getReq();
        SolrIndexSearcher searcher = req.getSearcher();
        final String fieldName = "reader";

        Query seedQuery = new BooleanQuery.Builder()
                .add(innerQuery, BooleanClause.Occur.MUST)
                .add(new WildcardQuery(new Term(fieldName, "*")), BooleanClause.Occur.FILTER)
                .build();

        SecondOrderQuery discoverMostReadQ = new SecondOrderQuery(seedQuery,
                new SecondOrderCollectorTopN(200));
        discoverMostReadQ.getcollector().setFinalValueType(FinalValueType.ABS_COUNT);

        final StringBuilder readers = new StringBuilder();
        final HashSet<String> fieldsToLoad = new HashSet<String>();
        fieldsToLoad.add(fieldName);

        try {
            searcher.search(discoverMostReadQ, new SimpleCollector() {
                @Override
                public org.apache.lucene.search.ScoreMode scoreMode() {
                    return org.apache.lucene.search.ScoreMode.COMPLETE_NO_SCORES;
                }

                private Document d;
                private LeafReader reader;
                private boolean firstPassed = false;

                @Override
                public void collect(int doc) throws IOException {
                    d = reader.document(doc, fieldsToLoad);
                    for (String val : d.getValues(fieldName)) {
                        if (firstPassed)
                            readers.append(" ");
                        readers.append(val);
                        firstPassed = true;
                    }
                }

                @Override
                public void doSetNextReader(LeafReaderContext context)
                        throws IOException {
                    this.reader = context.reader();
                }
            });
        } catch (IOException e) {
            throw new SyntaxError(e.getMessage(), e);
        }

        AdsMoreLikeThisQuery mlt = new AdsMoreLikeThisQuery(readers.toString(), new String[]{fieldName},
                new WhitespaceAnalyzer(), fieldName);

        mlt.setMinTermFrequency(0);
        mlt.setMinDocFreq(2);
        mlt.setMaxQueryTerms(maxQueryTerms);
        mlt.setPercentTermsToMatch(0.0f);

        return new BoostQuery(mlt, 2.0f);
    }

    /**
     * comment ZZZZZ
     */
    public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config)
            throws QueryNodeException {


        AqpSubqueryParser provider = parsers.get(funcName);
        if (provider == null)
            return null;

        AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);

        SolrQueryRequest req = reqAttr.getRequest();
        if (req == null)
            return null;


        SolrParams localParams = reqAttr.getLocalParams();
        if (localParams == null) {
            localParams = new ModifiableSolrParams();
        } else {
            localParams = new ModifiableSolrParams(localParams);
        }

        if (localParams.get(QueryParsing.DEFTYPE, null) == null) {
            ((ModifiableSolrParams) localParams).set(QueryParsing.DEFTYPE, "aqp");
        }
        AqpFunctionQParser parser = new AqpFunctionQParser("", localParams,
                reqAttr.getParams(), req);

        // TODO: builder is reusing parser object; that may be bad if two threads
        // are accessing it. Not happening now, but ...
        return new AqpSubQueryTreeBuilder(provider, parser);

    }

	/*
	private void getSpan(QueryNode node, Integer[] span) {
		List<QueryNode> children = node.getChildren();
		swimDeep(children.get(0), span);
		swimDeep(children.get(children.size()-1), span);
	}
	

	private void swimDeep(QueryNode node, Integer[] span) {

		if (node instanceof AqpANTLRNode) {
			int i = ((AqpANTLRNode) node).getTokenStart();
			int j = ((AqpANTLRNode) node).getTokenEnd();

			if(j>i) {
				if (i != -1 && i < span[0]) {
					span[0] = i;
				}
				if (j != -1 && j > span[1]) {
					span[1] = j;
				}
			}
		}
		if (!node.isLeaf()) {
			for (QueryNode child: node.getChildren()) {
				swimDeep(child, span);
			}
		}

	}
	*/

}
