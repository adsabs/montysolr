package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNotQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.solr.common.params.SolrParams;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This processor must follow the {@link AqpAnalyzerQueryNodeProcessor}
 * It will build a graph of the query node and will handle the cases
 * where a synonym expansion spans over several tokens. Basically,
 * we build every possible path that queries can be constructed.
 * <p>
 * You can supply your own query builder(s) which can do different things
 * based on the type of the resulting query graph. Ie. you may want to
 * add a boost to queries that were original input, or wrap queries with
 * many terms into a spanquery instead of a phrase. The options are many!
 * <p>
 * This processor will extract all synonyms from the "multi token stream"
 * it will join the synonyms with OR's and keep other tokens in place
 * ie. it will create a new tree:
 *
 * <pre>
 *                      hubble space telescope goes home
 *                                         |
 *                                   ----------------
 *                                  /          \     \
 *       (hubble space telescope | HST)       goes   home
 *
 *  </pre>
 */
public class AqpPostAnalysisProcessor extends AqpQueryNodeProcessorImpl {

    public static String QUERY_BRANCH_SIZE = "max_branch_size";
    public static final String RAW_POSITIONAL_PATH = "raw_positional_path";
    public static final String RAW_POSITIONAL_GAPS = "raw_positional_gaps";
    private static final String RAW_LEADING_GAP = "raw_leading_gap";

    @Override
    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
        if (node instanceof SlopQueryNode) {
            SlopQueryNode slop = (SlopQueryNode) node;
            QueryNode child = slop.getChild();
            if (child instanceof AqpNearQueryNode
                    && Boolean.TRUE.equals(child.getTag(RAW_POSITIONAL_PATH))) {
                addSlop((AqpNearQueryNode) child, slop.getValue());
                return child;
            }
            if (child instanceof AqpOrQueryNode) {
                List<QueryNode> branches = child.getChildren();
                boolean normalized = false;
                for (QueryNode branch : branches) {
                    if (Boolean.TRUE.equals(branch.getTag(RAW_POSITIONAL_PATH))) {
                        normalized = true;
                        break;
                    }
                }
                if (normalized) {
                    // Standard phrase-slop cleanup discards wrappers around disjunctions.
                    // Factored positional branches carry the explicit slop on their
                    // outer Near node; nested Near nodes retain only exact adjacency.
                    for (int i = 0; i < branches.size(); i++) {
                        QueryNode branch = branches.get(i);
                        if (branch instanceof AqpNearQueryNode
                                && Boolean.TRUE.equals(branch.getTag(RAW_POSITIONAL_PATH))) {
                            addSlop((AqpNearQueryNode) branch, slop.getValue());
                        } else if (branch instanceof TokenizedPhraseQueryNode
                                || branch instanceof MultiPhraseQueryNode) {
                            branch.setTag(RAW_POSITIONAL_PATH, true);
                            branches.set(i, new SlopQueryNode(branch, slop.getValue()));
                        }
                    }
                    child.set(branches);
                    return child;
                }
            }
        }
        return node;
    }

    private void addSlop(AqpNearQueryNode node, int value) {
        Integer current = node.getSlop();
        if (current == null || current == 0) {
            node.setSlop(value);
        }
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
        if (node.getTag(AqpAdsabsAnalyzerProcessor.ANALYZED) != null) {
            List<List<List<QueryNode>>> queryStructure;

            AqpRequestParams req = getRequest();
            SolrParams params = req.getParams();
            final String unfieldedDefaultOperator = "and";
            if (params != null) {
                params.get(AqpAdsabsQueryParser.AQP_UNFIELDED_OPERATOR_PARAM, "or").toLowerCase();
            }

            if (node instanceof TokenizedPhraseQueryNode) {
                if (!hasRawPositionalPath(node)) {
                    return node;
                }
                queryStructure = extractQueries(node);
                return buildNewQueryNode(queryStructure,
                        new QueryBuilder() {
                            @Override
                            public QueryNode buildQuery(List<QueryNode> clauses) {
                                AqpNearQueryNode near = new AqpNearQueryNode(clauses, 0);
                                near.setInOrder(true);
                                return near;
                            }
                        },
                        getPhraseSlop(node), true);

            } else if (node instanceof GroupQueryNode) {
                if (node.getChildren().size() > 0 && node.getChildren().get(0) instanceof BooleanQueryNode
                        && node.getChildren().get(0).getChildren().size() > 1) {
                    queryStructure = extractQueries(node.getChildren().get(0));
                    final int proximity = getDefaultProximityValue();

                    return buildNewQueryNode(queryStructure,
                            new QueryBuilder() {
                                @Override
                                public QueryNode buildQuery(List<QueryNode> clauses) {
                                    if (unfieldedDefaultOperator.equals("span")) {
                                        return new AqpNearQueryNode(clauses, proximity);
                                    } else if (unfieldedDefaultOperator.equals("and")) {
                                        return new AqpAndQueryNode(clauses);
                                    } else if (unfieldedDefaultOperator.equals("not")) {
                                        return new AqpNotQueryNode(clauses);
                                    } else {
                                        return new AqpOrQueryNode(clauses);
                                    }
                                }
                            }
                    );
                }

            } else if (node instanceof MultiPhraseQueryNode) {
                queryStructure = extractQueries(node);

                if (node.getParent() instanceof FuzzyQueryNode) { // "some span query"~3

                    final FuzzyQueryNode parent = (FuzzyQueryNode) node.getParent();

                    return buildNewQueryNode(queryStructure,
                            new QueryBuilder() {
                                @Override
                                public QueryNode buildQuery(List<QueryNode> clauses) {
                                    return new AqpNearQueryNode(clauses, parent.getPositionIncrement());
                                }
                            }
                    );
                } else {

                    return buildNewQueryNode(queryStructure,      // default: create boolean ((+a +b) OR (+a +(b|c)))
                            new QueryBuilder() {
                                @Override
                                public QueryNode buildQuery(List<QueryNode> clauses) {
                                    if (this.isMultiDimensional) {
                                        MultiPhraseQueryNode pq = new MultiPhraseQueryNode();
                                        for (QueryNode c : clauses) {
                                            if (c.isLeaf()) {
                                                pq.add(c);
                                            } else {
                                                for (QueryNode child : c.getChildren()) {
                                                    pq.add(child);
                                                }
                                            }
                                        }
                                        return pq;
                                    } else {
                                        TokenizedPhraseQueryNode pq = new TokenizedPhraseQueryNode();
                                        //MultiPhraseQueryNode pq = new MultiPhraseQueryNode();
                                        pq.add(clauses);
                                        return pq;
                                    }
                                }
                            },
                            getPhraseSlop(node), true
                    );
                }
            }

            // do nothing, we don't know how to process this type
            return node;

        }

        return node;
    }

    private AqpRequestParams getRequest() throws QueryNodeException {
        QueryConfigHandler config = getQueryConfigHandler();
        AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
        if (config == null || config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST) == null) {
            throw new QueryNodeException(new MessageImpl(
                    QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                    "Configuration error: "
                            + "SOLR_REQUEST is missing"));
        }
        return config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
    }


    private Integer getDefaultProximityValue() throws QueryNodeException {
        QueryConfigHandler queryConfig = getQueryConfigHandler();
        if (queryConfig == null
                || !queryConfig.has(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_PROXIMITY)) {
            throw new QueryNodeException(new MessageImpl(
                    QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                    "Configuration error: "
                            + "DefaultProximity value is missing"));
        }
        return queryConfig.get(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_PROXIMITY);
    }

    private Integer getPhraseSlop(QueryNode node) {
        QueryNode parent = node.getParent();
        if (parent instanceof SlopQueryNode) {
            return ((SlopQueryNode) parent).getValue();
        }
        Integer configured = getQueryConfigHandler().get(
                StandardQueryConfigHandler.ConfigurationKeys.PHRASE_SLOP);
        return configured == null ? 0 : configured;
    }


    /*
     * Build a simple Query node from
     * 	  queries
     *       - list of queries, all the possible combinations of consecutive
     *         QueryNodes ordered to cover the query input
     */
    protected QueryNode buildNewQueryNode(List<List<List<QueryNode>>> queries,
                                          QueryBuilder queryBuilder) {
        return buildNewQueryNode(queries, queryBuilder, 0, false);
    }

    private QueryNode buildNewQueryNode(List<List<List<QueryNode>>> queries,
                                        QueryBuilder queryBuilder,
                                        Integer explicitPhraseSlop,
                                        boolean factorPositionalPaths) {
        if (factorPositionalPaths && containsRawPositionalPath(queries)) {
            if (explicitPhraseSlop != null && explicitPhraseSlop > 0) {
                return buildExplicitSlopQuery(queries);
            }
            return buildFactoredQuery(queries, queryBuilder);
        }

        List<QueryNode> mainQueryClauses = new ArrayList<QueryNode>();

        // last bit of info we add
        // is the number of tokens each branch contains
        // that can be used later for deciding whether they
        // need a slope

        int maxSize = 0;
        // find the length of the longest query (in tokens)
        for (List<List<QueryNode>> oneQuery : queries) {
            if (oneQuery.size() > maxSize)
                maxSize = oneQuery.size();
        }

        for (List<List<QueryNode>> oneQuery : queries) {
            queryBuilder.reset();
            List<QueryNode> clauses = new ArrayList<QueryNode>();
            boolean branchHasRawPositions = false;
            for (List<QueryNode> qElement : oneQuery) {
                for (QueryNode qn : qElement)
                    qn.setTag(AqpPostAnalysisProcessor.QUERY_BRANCH_SIZE, maxSize);
                QueryNode clause = compactMixedAlternatives(queryBuilder.buildQueryElement(qElement));
                boolean elementHasRawPositions = false;
                for (QueryNode qn : qElement) {
                    if (hasRawPositionalPath(qn)) {
                        elementHasRawPositions = true;
                        break;
                    }
                }
                if (elementHasRawPositions) {
                    clause.setTag(RAW_POSITIONAL_PATH, true);
                    branchHasRawPositions = true;
                }
                clauses.add(clause);
            }
            if (clauses.size() > 1) {
                QueryNode branch = queryBuilder.buildQuery(clauses);
                if (branchHasRawPositions) {
                    branch.setTag(RAW_POSITIONAL_PATH, true);
                }
                mainQueryClauses.add(branch);

            } else {
                mainQueryClauses.add(clauses.get(0));
            }
        }
        return queryBuilder.buildTopQuery(mainQueryClauses);
    }

    private boolean containsRawPositionalPath(
            List<List<List<QueryNode>>> queries) {
        for (List<List<QueryNode>> query : queries) {
            for (List<QueryNode> element : query) {
                for (QueryNode node : element) {
                    if (hasRawPositionalPath(node)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private QueryNode buildExplicitSlopQuery(
            List<List<List<QueryNode>>> paths) {
        List<QueryNode> branches = new ArrayList<>(paths.size());
        for (List<List<QueryNode>> path : paths) {
            if (path.isEmpty()) {
                continue;
            }
            branches.add(buildPositionedPhrasePath(path));
        }
        if (branches.size() == 1) {
            return branches.get(0);
        }
        AqpOrQueryNode disjunction = new AqpOrQueryNode(branches);
        disjunction.setTag(RAW_POSITIONAL_PATH, true);
        disjunction.setTag(AqpQueryTreeBuilder.SYNONYMS, true);
        return disjunction;
    }

    private QueryNode buildPositionedPhrasePath(List<List<QueryNode>> path) {
        boolean multiDimensional = false;
        for (List<QueryNode> element : path) {
            multiDimensional |= element.size() > 1;
        }
        QueryNode phrase = multiDimensional
                ? new MultiPhraseQueryNode() : new TokenizedPhraseQueryNode();
        int position = -1;
        for (List<QueryNode> element : path) {
            position += rawPositionDelta(element);
            for (QueryNode node : element) {
                ((FieldQueryNode) node).setPositionIncrement(position);
                phrase.add(node);
            }
        }
        phrase.setTag(RAW_POSITIONAL_PATH, true);
        return phrase;
    }

    private QueryNode buildFactoredQuery(
            List<List<List<QueryNode>>> queries,
            QueryBuilder queryBuilder) {
        List<QueryNode> alternatives = new ArrayList<>();
        List<List<List<QueryNode>>> variableWidthPaths = new ArrayList<>();
        for (List<List<QueryNode>> path : queries) {
            boolean fixedWidth = path.size() > 1;
            for (List<QueryNode> element : path) {
                for (QueryNode node : element) {
                    fixedWidth &= node instanceof FieldQueryNode;
                }
            }
            if (fixedWidth) {
                QueryNode phrase = new SlopQueryNode(buildPositionedPhrasePath(path), 0);
                phrase.setTag(RAW_POSITIONAL_PATH, true);
                alternatives.add(phrase);
            } else {
                variableWidthPaths.add(path);
            }
        }
        if (!variableWidthPaths.isEmpty()) {
            alternatives.add(buildFactoredPath(variableWidthPaths, queryBuilder));
        }
        return positionalAlternatives(alternatives);
    }

    private QueryNode buildFactoredPath(
            List<List<List<QueryNode>>> paths, QueryBuilder queryBuilder) {
        Map<String, List<List<List<QueryNode>>>> grouped = new java.util.LinkedHashMap<>();
        for (List<List<QueryNode>> path : paths) {
            if (!path.isEmpty()) {
                List<QueryNode> element = path.get(0);
                String key = singlePositionSignature(element) + "@" + rawPositionDelta(element);
                grouped.computeIfAbsent(key, ignored -> new ArrayList<>()).add(path);
            }
        }
        if (grouped.isEmpty()) {
            return null;
        }
        if (grouped.size() > 1) {
            List<QueryNode> branches = new ArrayList<>(grouped.size());
            for (List<List<List<QueryNode>>> group : grouped.values()) {
                branches.add(buildFactoredPath(group, queryBuilder));
            }
            return positionalAlternatives(branches);
        }

        List<List<List<QueryNode>>> group = grouped.values().iterator().next();
        List<QueryNode> first = group.get(0).get(0);
        boolean terminal = false;
        List<List<List<QueryNode>>> suffixes = new ArrayList<>(group.size());
        for (List<List<QueryNode>> path : group) {
            if (path.size() == 1) {
                terminal = true;
            } else {
                suffixes.add(path.subList(1, path.size()));
            }
        }
        QueryNode head = compactMixedAlternatives(queryBuilder.buildQueryElement(
                cloneElementUnchecked(first)));
        head.setTag(RAW_POSITIONAL_PATH, true);
        head.setTag(RAW_LEADING_GAP, rawPositionDelta(first) - 1);
        QueryNode suffix = buildFactoredPath(suffixes, queryBuilder);
        if (suffix == null) {
            return head;
        }
        QueryNode continuation = joinFactoredPath(
                terminal ? cloneElementUnchecked(Collections.singletonList(head)).get(0) : head,
                suffix);
        return terminal ? positionalAlternatives(java.util.Arrays.asList(head, continuation))
                : continuation;
    }

    private QueryNode positionalAlternatives(List<QueryNode> branches) {
        if (branches.size() == 1) {
            return branches.get(0);
        }
        AqpOrQueryNode alternatives = new AqpOrQueryNode(branches);
        alternatives.setTag(RAW_POSITIONAL_PATH, true);
        alternatives.setTag(AqpQueryTreeBuilder.SYNONYMS, true);
        Object gap = branches.get(0).getTag(RAW_LEADING_GAP);
        if (gap != null) {
            for (QueryNode branch : branches) {
                if (!gap.equals(branch.getTag(RAW_LEADING_GAP))) {
                    return alternatives;
                }
            }
            alternatives.setTag(RAW_LEADING_GAP, gap);
        }
        return alternatives;
    }

    private QueryNode joinFactoredPath(QueryNode head, QueryNode suffix) {
        Integer gap = (Integer) suffix.getTag(RAW_LEADING_GAP);
        if (gap == null) {
            // Different source holes are different transitions, not a reason
            // to drop a gap or widen the entire phrase.
            Map<Integer, List<QueryNode>> byGap = new java.util.LinkedHashMap<>();
            for (QueryNode branch : suffix.getChildren()) {
                Integer branchGap = (Integer) branch.getTag(RAW_LEADING_GAP);
                byGap.computeIfAbsent(branchGap, ignored -> new ArrayList<>()).add(branch);
            }
            List<QueryNode> continuations = new ArrayList<>(byGap.size());
            for (List<QueryNode> branches : byGap.values()) {
                continuations.add(joinFactoredPath(
                        cloneElementUnchecked(Collections.singletonList(head)).get(0),
                        positionalAlternatives(branches)));
            }
            return positionalAlternatives(continuations);
        }
        AqpNearQueryNode near = new AqpNearQueryNode(java.util.Arrays.asList(head, suffix), 0);
        near.setInOrder(true);
        near.setTag(RAW_POSITIONAL_PATH, true);
        near.setTag(RAW_LEADING_GAP, head.getTag(RAW_LEADING_GAP));
        if (gap > 0) {
            near.setTag(RAW_POSITIONAL_GAPS, new int[]{0, gap});
        }
        return near;
    }

    private List<QueryNode> cloneElementUnchecked(List<QueryNode> element) {
        try {
            return cloneElement(element);
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Unable to clone factored query element", e);
        }
    }

    private QueryNode compactMixedAlternatives(QueryNode node) {
        if (!(node instanceof AqpOrQueryNode)) {
            return node;
        }
        List<QueryNode> fields = new ArrayList<>();
        List<QueryNode> alternatives = new ArrayList<>();
        for (QueryNode child : node.getChildren()) {
            if (child instanceof FieldQueryNode) {
                fields.add(child);
            } else {
                alternatives.add(child);
            }
        }
        if (fields.size() <= 1 || alternatives.isEmpty()) {
            return node;
        }
        AqpOrQueryNode compact = new AqpOrQueryNode(fields);
        compact.setTag(AqpQueryTreeBuilder.SYNONYMS, true);
        alternatives.add(0, compact);
        node.set(alternatives);
        return node;
    }

    private int sourceGap(List<QueryNode> previous, List<QueryNode> next) {
        if (previous == null) {
            return 0;
        }
        Integer end = null;
        Integer start = null;
        for (QueryNode node : previous) {
            Integer value = (Integer) node.getTag(AqpAnalyzerQueryNodeProcessor.SOURCE_INDEX_END);
            if (value != null) {
                end = end == null ? value : Math.max(end, value);
            }
        }
        for (QueryNode node : next) {
            Integer value = (Integer) node.getTag(AqpAnalyzerQueryNodeProcessor.SOURCE_INDEX_START);
            if (value != null) {
                start = start == null ? value : Math.min(start, value);
            }
        }
        return start == null || end == null ? 0 : Math.max(0, start - end);
    }

    private void setIncomingGap(List<QueryNode> element, int gap) {
        for (QueryNode node : element) {
            node.setTag(AqpAnalyzerQueryNodeProcessor.RAW_INDEX_POSITION_DELTA, gap + 1);
        }
    }



    /*
     * this method knows to handle FieldQueryNodes, it is especially useful
     * for 	MultiPhraseQueryNode
     *
     * If there are non-fieldable nodes, it will fail. We cannot process
     * such queries (and we shouldn't!)
     */
    protected List<List<List<QueryNode>>> extractQueries(QueryNode node) throws QueryNodeException {

        List<List<List<QueryNode>>> queries;
        List<QueryNode> children = node.getChildren();

        NodeOfQuery graph = new NodeOfQuery(-1, -1);
        int maxDepth = 0;
        Integer maxAllowedDepth = Integer.valueOf(getConfigVal("aqp.maxPathLength", "100"));

        for (QueryNode child : children) {
            //System.out.println("addToken(): " + child);
            maxDepth = graph.consume(child);
            if (maxDepth > maxAllowedDepth)
                throw new QueryNodeException(new MessageImpl("Query exceed maxAllowedDepth of " + maxAllowedDepth + " tokens for query redistribution"));
        }

        //System.out.println(graph.toString());

        try {
            queries = graph.traverseGraphFindAllQueries();
            Integer phraseSlop = getPhraseSlop(node);
            boolean localRawAliases = node instanceof BooleanQueryNode
                    || phraseSlop == null || phraseSlop <= 0;
            queries = expandRawAliasPaths(queries, localRawAliases);
        } catch (CloneNotSupportedException | IOException e) {
            throw new QueryNodeException(e);
        }

        // each list is a query - inside the query, every
        // element is a list (if there are more elements, they
        // share the same span)
        return queries;
    }

    private List<List<List<QueryNode>>> expandRawAliasPaths(
            List<List<List<QueryNode>>> queries, boolean localRawAliases)
            throws CloneNotSupportedException, IOException {
        List<List<List<QueryNode>>> expandedQueries = new ArrayList<>();
        for (List<List<QueryNode>> query : queries) {
            if (localRawAliases) {
                boolean queryHasRaw = false;
                List<List<QueryNode>> factoredPath = new ArrayList<>(query.size());
                List<QueryNode> previous = null;
                for (List<QueryNode> element : query) {
                    List<QueryNode> choices = cloneElement(deduplicateElement(element));
                    List<List<List<QueryNode>>> rawSequences =
                            distinctRawSequences(element, choices);
                    for (List<List<QueryNode>> rawSequence : rawSequences) {
                        choices.add(buildRawAliasNode(rawSequence));
                    }
                    queryHasRaw |= !rawSequences.isEmpty();
                    for (QueryNode candidate : element) {
                        queryHasRaw |= hasRawPositionalPath(candidate);
                    }
                    setIncomingGap(choices, sourceGap(previous, element));
                    previous = element;
                    factoredPath.add(choices);
                }
                if (queryHasRaw) {
                    expandedQueries.add(clonePathWithPositions(factoredPath));
                } else {
                    expandedQueries.add(factoredPath);
                }
                continue;
            }

            List<List<List<QueryNode>>> variants = new ArrayList<>();
            variants.add(new ArrayList<>());
            boolean queryHasRaw = false;
            List<QueryNode> previous = null;
            for (List<QueryNode> element : query) {
                List<QueryNode> retained = cloneElement(deduplicateElement(element));
                List<List<List<QueryNode>>> rawSequences =
                        distinctRawSequences(element, retained);
                queryHasRaw |= !rawSequences.isEmpty();
                int gap = sourceGap(previous, element);
                setIncomingGap(retained, gap);
                for (List<List<QueryNode>> rawSequence : rawSequences) {
                    setIncomingGap(rawSequence.get(0), gap);
                }
                previous = element;

                List<List<List<QueryNode>>> next = new ArrayList<>();
                for (List<List<QueryNode>> variant : variants) {
                    if (!retained.isEmpty()) {
                        List<List<QueryNode>> retainedVariant = new ArrayList<>(variant);
                        retainedVariant.add(retained);
                        next.add(retainedVariant);
                    }
                    for (List<List<QueryNode>> rawSequence : rawSequences) {
                        List<List<QueryNode>> rawVariant = new ArrayList<>(variant);
                        rawVariant.addAll(rawSequence);
                        next.add(rawVariant);
                    }
                }
                variants = next;
            }
            if (queryHasRaw) {
                Set<String> seenVariants = new LinkedHashSet<>();
                for (List<List<QueryNode>> variant : variants) {
                    List<List<QueryNode>> positioned = clonePathWithPositions(variant);
                    if (seenVariants.add(pathSignature(positioned))) {
                        expandedQueries.add(positioned);
                    }
                }
            } else {
                expandedQueries.add(variants.get(0));
            }
        }
        return expandedQueries;
    }

    private List<List<List<QueryNode>>> distinctRawSequences(
            List<QueryNode> element, List<QueryNode> retained) throws IOException {
        List<List<List<QueryNode>>> rawSequences = new ArrayList<>();
        for (QueryNode candidate : element) {
            if (Boolean.TRUE.equals(candidate.getTag(
                    AqpAnalyzerQueryNodeProcessor.RAW_MULTI_TOKEN_ALIAS))) {
                FieldQueryNode alias = (FieldQueryNode) candidate;
                String value = alias.getTextAsString();
                int separator = value.indexOf("::");
                String output = separator >= 0 ? value.substring(separator + 2) : value;
                String source = (String) alias.getTag(
                        AqpAnalyzerQueryNodeProcessor.SOURCE_TEXT);
                if (countWordRuns(output) == 1 && hasAcronymAlternative(element, output)) {
                    continue;
                }
                rawSequences.addAll(rawAliasWordPaths(alias));
                if (source != null && !source.equals(output)
                        && countWordRuns(source) == countWordRuns(output)) {
                    rawSequences.addAll(rawAliasWordPaths(alias, source));
                }
            }
        }
        rawSequences.addAll(reanalyzedRawAliases(element));

        Set<String> retainedTerms = termSet(retained);
        Set<String> seenRawSequences = new HashSet<>();
        List<List<List<QueryNode>>> distinctRawSequences = new ArrayList<>();
        for (List<List<QueryNode>> rawSequence : rawSequences) {
            if (rawSequence.isEmpty()) {
                continue;
            }
            if (!seenRawSequences.add(sequenceSignature(rawSequence))) {
                continue;
            }
            if (rawSequence.size() == 1
                    && rawPositionDelta(rawSequence.get(0)) == 1
                    && retainedTerms.containsAll(termSet(rawSequence.get(0)))) {
                continue;
            }
            markRawPath(rawSequence);
            distinctRawSequences.add(rawSequence);
        }
        return distinctRawSequences;
    }

    private boolean hasAcronymAlternative(List<QueryNode> element, String text) {
        String acronym = "acr::" + text;
        for (QueryNode node : element) {
            if (node instanceof FieldQueryNode
                    && "ACRONYM".equals(node.getTag(AqpAnalyzerQueryNodeProcessor.TYPE_ATTRIBUTE))
                    && acronym.equals(((FieldQueryNode) node).getTextAsString())) {
                return true;
            }
        }
        return false;
    }

    private List<QueryNode> deduplicateElement(List<QueryNode> element) {
        List<QueryNode> retained = new ArrayList<>(element.size());
        Set<String> seen = new HashSet<>();
        for (QueryNode node : element) {
            if (seen.add(nodeSignature(node))) {
                retained.add(node);
            }
        }
        return retained;
    }

    private Set<String> termSet(List<QueryNode> element) {
        Set<String> terms = new HashSet<>();
        for (QueryNode node : element) {
            terms.add(nodeSignature(node));
        }
        return terms;
    }

    private String nodeSignature(QueryNode node) {
        if (node instanceof FieldQueryNode) {
            FieldQueryNode fieldNode = (FieldQueryNode) node;
            String field = fieldNode.getFieldAsString();
            String text = fieldNode.getTextAsString();
            return field.length() + ":" + field + text.length() + ":" + text;
        }
        return node.getClass().getName() + ":" + node.toString();
    }

    private String singlePositionSignature(List<QueryNode> element) {
        List<String> terms = new ArrayList<>(element.size());
        for (QueryNode node : element) {
            terms.add(nodeSignature(node));
        }
        Collections.sort(terms);
        return String.join("\u001f", terms);
    }

    private int rawPositionDelta(List<QueryNode> position) {
        int delta = 1;
        for (QueryNode node : position) {
            Integer rawDelta = (Integer) node.getTag(
                    AqpAnalyzerQueryNodeProcessor.RAW_INDEX_POSITION_DELTA);
            if (rawDelta != null) {
                delta = Math.max(delta, rawDelta);
            }
        }
        return delta;
    }

    private String sequenceSignature(List<List<QueryNode>> sequence) {
        List<String> positions = new ArrayList<>(sequence.size());
        for (List<QueryNode> position : sequence) {
            positions.add(singlePositionSignature(position)
                    + "@" + rawPositionDelta(position));
        }
        return String.join("\u001e", positions);
    }

    private String pathSignature(List<List<QueryNode>> path) {
        List<String> elements = new ArrayList<>(path.size());
        for (List<QueryNode> element : path) {
            List<String> signatures = new ArrayList<>(element.size());
            for (QueryNode node : element) {
                signatures.add(nodePositionSignature(node));
            }
            Collections.sort(signatures);
            elements.add(String.join("\u001f", signatures));
        }
        return String.join("\u001d", elements);
    }

    private String nodePositionSignature(QueryNode node) {
        if (node instanceof FieldQueryNode) {
            FieldQueryNode field = (FieldQueryNode) node;
            return nodeSignature(node) + "@" + field.getPositionIncrement();
        }
        List<QueryNode> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            List<String> childSignatures = new ArrayList<>(children.size());
            for (QueryNode child : children) {
                childSignatures.add(nodePositionSignature(child));
            }
            return node.getClass().getName() + "("
                    + String.join("\u001f", childSignatures) + ")";
        }
        return nodeSignature(node);
    }

    private boolean hasRawPositionalPath(QueryNode node) {
        if (Boolean.TRUE.equals(node.getTag(RAW_POSITIONAL_PATH))) {
            return true;
        }
        List<QueryNode> children = node.getChildren();
        if (children != null) {
            for (QueryNode child : children) {
                if (hasRawPositionalPath(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void markRawPath(List<List<QueryNode>> path) {
        for (List<QueryNode> position : path) {
            for (QueryNode node : position) {
                node.setTag(RAW_POSITIONAL_PATH, true);
            }
        }
    }

    private QueryNode buildRawAliasNode(List<List<QueryNode>> rawSequence) {
        if (rawSequence.size() == 1 && rawSequence.get(0).size() == 1) {
            return rawSequence.get(0).get(0);
        }
        QueryNode phrase = buildPositionedPhrasePath(rawSequence);
        // Intrinsic alias order is exact even if the parser's default slop
        // is positive. Explicit positive user slop uses the flat path instead.
        SlopQueryNode exact = new SlopQueryNode(phrase, 0);
        exact.setTag(RAW_POSITIONAL_PATH, true);
        return exact;
    }
    private List<QueryNode> cloneElement(List<QueryNode> element)
            throws CloneNotSupportedException {
        List<QueryNode> copy = new ArrayList<>(element.size());
        for (QueryNode node : element) {
            QueryNode clone = node.cloneTree();
            copyTags(node, clone);
            copy.add(clone);
        }
        return copy;
    }

    private void copyTags(QueryNode source, QueryNode target) {
        for (Entry<String, Object> tag : source.getTagMap().entrySet()) {
            target.setTag(tag.getKey(), tag.getValue());
        }
        if (source.getChildren() != null) {
            for (int i = 0; i < source.getChildren().size(); i++) {
                copyTags(source.getChildren().get(i), target.getChildren().get(i));
            }
        }
    }

    private List<List<QueryNode>> clonePathWithPositions(List<List<QueryNode>> path)
            throws CloneNotSupportedException {
        List<List<QueryNode>> copy = new ArrayList<>(path.size());
        int position = -1;
        for (List<QueryNode> element : path) {
            position += rawPositionDelta(element);
            List<QueryNode> cloned = cloneElement(element);
            for (QueryNode node : cloned) {
                node.setTag(RAW_POSITIONAL_PATH, true);
                if (node instanceof FieldQueryNode) {
                    ((FieldQueryNode) node).setPositionIncrement(position);
                }
            }
            copy.add(cloned);
        }
        return copy;
    }
    private List<List<List<QueryNode>>> reanalyzedRawAliases(List<QueryNode> element)
            throws IOException {
        List<List<List<QueryNode>>> sequences = new ArrayList<>();
        Analyzer analyzer = getQueryConfigHandler().get(
                StandardQueryConfigHandler.ConfigurationKeys.ANALYZER);
        if (analyzer == null) {
            return sequences;
        }
        for (QueryNode candidate : element) {
            if ("SYNONYM".equals(candidate.getTag(AqpAnalyzerQueryNodeProcessor.TYPE_ATTRIBUTE))
                    || !(candidate instanceof FieldQueryNode)) {
                continue;
            }
            String source = (String) candidate.getTag(AqpAnalyzerQueryNodeProcessor.SOURCE_TEXT);
            if (source == null || source.isBlank()) {
                continue;
            }
            try (TokenStream stream = analyzer.tokenStream(
                    ((FieldQueryNode) candidate).getFieldAsString(),
                    new StringReader(source))) {
                CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
                TypeAttribute type = stream.addAttribute(TypeAttribute.class);
                stream.reset();
                while (stream.incrementToken()) {
                    if ("SYNONYM".equals(type.type())
                            && countWordRuns(term) > countWordRuns(source)) {
                        FieldQueryNode recovered = new FieldQueryNode(
                                ((FieldQueryNode) candidate).getFieldAsString(),
                                term.toString(), ((FieldQueryNode) candidate).getBegin(),
                                ((FieldQueryNode) candidate).getEnd());
                        for (Entry<String, Object> tag
                                : candidate.getTagMap().entrySet()) {
                            recovered.setTag(tag.getKey(), tag.getValue());
                        }
                        sequences.addAll(rawAliasWordPaths(recovered));
                    }
                }
                stream.end();
            }
        }
        return sequences;
    }

    private int countWordRuns(CharSequence text) {
        int runs = 0;
        boolean inWord = false;
        for (int i = 0; i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i))) {
                inWord = false;
            } else if (!inWord) {
                runs++;
                inWord = true;
            }
        }
        return runs;
    }

    private static final class RawAliasToken {
        private final String term;
        private final int start;
        private final int end;
        private final int position;
        private final int positionLength;

        private RawAliasToken(String term, int start, int end,
                              int position, int positionLength) {
            this.term = term;
            this.start = start;
            this.end = end;
            this.position = position;
            this.positionLength = positionLength;
        }
    }

    private List<List<List<QueryNode>>> rawAliasWordPaths(FieldQueryNode alias)
            throws IOException {
        String value = alias.getTextAsString();
        int separator = value.indexOf("::");
        String raw = separator >= 0 ? value.substring(separator + 2) : value;
        return rawAliasWordPaths(alias, raw);
    }

    private List<List<List<QueryNode>>> rawAliasWordPaths(FieldQueryNode alias,
            String raw) throws IOException {
        List<RawAliasToken> tokens = new ArrayList<>();
        AqpRequestParams request = getQueryConfigHandler().get(
                AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
        if (request == null || request.getRequest() == null
                || request.getRequest().getSchema() == null) {
            throw new IOException("Solr schema is required for raw alias normalization");
        }
        Analyzer analyzer = request.getRequest().getSchema().getIndexAnalyzer();
        if (analyzer == null) {
            throw new IOException("Index analyzer is required for raw alias normalization");
        }
        try (TokenStream stream = analyzer.tokenStream(alias.getFieldAsString(),
                new StringReader(raw))) {
            CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
            TypeAttribute type = stream.addAttribute(TypeAttribute.class);
            PositionIncrementAttribute posInc =
                    stream.addAttribute(PositionIncrementAttribute.class);
            PositionLengthAttribute posLength =
                    stream.addAttribute(PositionLengthAttribute.class);
            OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
            int position = -1;
            stream.reset();
            while (stream.incrementToken()) {
                position += posInc.getPositionIncrement();
                if (!"SYNONYM".equals(type.type()) && !"ACRONYM".equals(type.type())) {
                    tokens.add(new RawAliasToken(term.toString(),
                            offset.startOffset(), offset.endOffset(), position,
                            Math.max(1, posLength.getPositionLength())));
                }
            }
            stream.end();
        }
        if (tokens.isEmpty()) {
            return new ArrayList<>();
        }
        int firstPosition = Integer.MAX_VALUE;
        int finalPosition = Integer.MIN_VALUE;
        for (RawAliasToken token : tokens) {
            firstPosition = Math.min(firstPosition, token.position);
            finalPosition = Math.max(finalPosition,
                    token.position + token.positionLength);
        }
        List<List<List<QueryNode>>> paths = new ArrayList<>();
        collectRawAliasPaths(alias, tokens, firstPosition, finalPosition,
                firstPosition, new ArrayList<>(), paths);
        return paths;
    }
    private void collectRawAliasPaths(FieldQueryNode alias,
            List<RawAliasToken> tokens, int position, int finalPosition,
            int previousEnd, List<List<QueryNode>> prefix,
            List<List<List<QueryNode>>> paths) {
        if (position == finalPosition) {
            if (!prefix.isEmpty()) {
                paths.add(prefix);
            }
            return;
        }
        Map<Integer, List<RawAliasToken>> byEnd = new java.util.LinkedHashMap<>();
        int nextPosition = Integer.MAX_VALUE;
        for (RawAliasToken token : tokens) {
            if (token.position == position) {
                byEnd.computeIfAbsent(token.position + token.positionLength,
                        ignored -> new ArrayList<>()).add(token);
            } else if (token.position > position) {
                nextPosition = Math.min(nextPosition, token.position);
            }
        }
        if (byEnd.isEmpty() && nextPosition != Integer.MAX_VALUE) {
            collectRawAliasPaths(alias, tokens, nextPosition, finalPosition,
                    previousEnd, prefix, paths);
            return;
        }
        for (Entry<Integer, List<RawAliasToken>> edge : byEnd.entrySet()) {
            List<QueryNode> element = new ArrayList<>(edge.getValue().size());
            for (RawAliasToken token : edge.getValue()) {
                FieldQueryNode node = new FieldQueryNode(alias.getFieldAsString(),
                        token.term, alias.getBegin() + token.start,
                        alias.getBegin() + token.end);
                node.setTag(AqpAnalyzerQueryNodeProcessor.RAW_INDEX_POSITION_DELTA,
                        prefix.isEmpty() ? 1 : 1 + token.position - previousEnd);
                element.add(node);
            }
            List<List<QueryNode>> next = new ArrayList<>(prefix);
            next.add(element);
            collectRawAliasPaths(alias, tokens, edge.getKey(), finalPosition,
                    edge.getKey(), next, paths);
        }
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }


    class NodeOfQuery {
        protected int startPos;
        private List<QueryNode> payload = new ArrayList<QueryNode>();
        private final List<NodeOfQuery> children;
        private final int nodeRetrieved = 0;
        protected int endPos = -1;
        private boolean terminalAlternative;

        public NodeOfQuery(int startPosition, int endPosition) {
            startPos = startPosition;
            endPos = endPosition;
            payload = new ArrayList<QueryNode>();
            children = new ArrayList<NodeOfQuery>();
        }

        public NodeOfQuery(QueryNode node) {
            startPos = ((FieldQueryNode) node).getBegin();
            payload = new ArrayList<QueryNode>();
            children = new ArrayList<NodeOfQuery>();
            endPos = ((FieldQueryNode) node).getEnd();
            payload.add(node);
        }

        @Override
        public String toString() {
            return prn(0);
        }

        public String prn(int indent) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < indent; i++) {
                sb.append(" ");
            }
            String ind = sb.toString();
            sb = new StringBuilder();

            sb.append(ind + "<NodeOfQuery startPos=\"" + this.startPos
                    + "\" endPos=\"" + this.endPos + "\"/>\n");
            for (QueryNode child : payload) {
                sb.append(ind + "<payload>" + child + "</payload>\n");
                System.out.println(ind + "<payload>" + child + "</payload>\n");
            }
            for (NodeOfQuery child : children) {
                sb.append(ind + "<child>\n");
                sb.append(child.prn(indent + 2));
                sb.append(ind + "</child>\n");
            }
            sb.append(ind + "</NodeOfQuery>\n");
            return sb.toString();
        }

        public int consume(QueryNode qnode) {
            if (Boolean.TRUE.equals(qnode.getTag(
                    AqpAnalyzerQueryNodeProcessor.OMITTED_SOURCE_TOKEN))) {
                return insertOptional((FieldQueryNode) qnode, 0, new HashSet<>());
            }
            return consume(qnode, 0);
        }

        private int insertOptional(FieldQueryNode node, int depth, Set<NodeOfQuery> visited) {
            if (!visited.add(this)) {
                return depth;
            }
            List<NodeOfQuery> existing = new ArrayList<>(children);
            List<NodeOfQuery> following = new ArrayList<>();
            boolean present = false;
            int deepest = depth;
            for (NodeOfQuery child : existing) {
                if (child.startPos == node.getBegin() && child.endPos == node.getEnd()) {
                    child.addPayload(node);
                    present = true;
                } else if (child.startPos >= node.getEnd()) {
                    following.add(child);
                }
                if (child.endPos <= node.getBegin()) {
                    deepest = Math.max(deepest, child.insertOptional(node, depth + 1, visited));
                }
            }
            if (!present && endPos <= node.getBegin()
                    && (!following.isEmpty() || existing.isEmpty() || terminalAlternative)) {
                NodeOfQuery inserted = new NodeOfQuery(node);
                inserted.children.addAll(following);
                if (existing.isEmpty() || terminalAlternative) {
                    terminalAlternative = true;
                }
                children.add(inserted);
                deepest = Math.max(deepest, depth + 1);
            }
            return deepest;
        }

        private int consume(QueryNode qnode, int depth) {
            FieldQueryNode node = ((FieldQueryNode) qnode);
            boolean descended = false;
            for (NodeOfQuery child : children) {
                if (child.startPos == node.getBegin() && child.endPos == node.getEnd()) {
                    child.addPayload(node);
                    return depth;
                }
                if (child.startPos < node.getBegin() && child.endPos < node.getEnd()) {
                    depth = child.consume(qnode, depth + 1);
                    descended = true;
                }
            }
            if (!descended && node.getBegin() > this.startPos) {
                NodeOfQuery inserted = new NodeOfQuery(node);
                // A source-recovered token can arrive after the analyzer has
                // already supplied its following native tokens. Keep the
                // existing direct (omit) edge and also connect the recovered
                // token to every following node (include) edge.
                for (NodeOfQuery later : children) {
                    if (later.startPos >= inserted.endPos) {
                        inserted.children.add(later);
                    }
                }
                children.add(inserted);
            }
            return depth;
        }

        public void addPayload(QueryNode node) {
            if (!payload.contains(node))
                //System.out.println("Adding payload: " + node);
                payload.add(node);
        }


        public void drillDown(QueryPath path) {
            if (children.isEmpty() || terminalAlternative) {
                path.terminus();
            }
            for (NodeOfQuery child : children) {
                path.push(child.startPos);
                path.push(child.endPos);
                child.drillDown(path);
                path.pop();
                path.pop();
            }
        }

        public List<List<List<QueryNode>>> traverseGraphFindAllQueries()
                throws CloneNotSupportedException {
            QueryPath path = new QueryPath(); // find all queries
            drillDown(path);

            // measure how long a string the query covers
            List<List<Integer>> paths = path.getAllPaths();
            int[] measured = measurePathsInclGaps(paths);

            // we'll consider only the queries that cover the max distance
            int max = 0;
            for (int m : measured) {
                if (m > max)
                    max = m;
            }

            List<List<List<QueryNode>>> queries = new ArrayList<List<List<QueryNode>>>();

            // retrieve only the queries made of query elements that cover the longest distance
            for (int i = 0; i < measured.length; i++) {
                if (measured[i] != max) {
                    //System.out.println("ignoring:" + measured[i] + " " + paths.get(i).toString());
                    continue;
                }

                List<List<QueryNode>> oneQuery = new ArrayList<List<QueryNode>>();
                retrieveQueryElements(oneQuery, paths.get(i), 0);
                assert oneQuery.size() == paths.get(i).size() / 2;
                queries.add(oneQuery);
            }

            assert queries.size() > 0;
            return queries;
        }

        private void retrieveQueryElements(List<List<QueryNode>> oneQuery, List<Integer> path, int pos)
                throws CloneNotSupportedException {
            if (pos >= path.size())
                return;

            Integer keyStart = path.get(pos);
            Integer keyEnd = path.get(pos + 1);


            for (NodeOfQuery child : children) {
                if (child.startPos == keyStart && child.endPos == keyEnd) {
                    child.insertItself(oneQuery);
                    child.retrieveQueryElements(oneQuery, path, pos + 2);
                    return;
                }
            }
            throw new IllegalStateException("Trying to get query element that doesn't exist: " + keyStart + ":" + keyEnd);
        }

        private void insertItself(List<List<QueryNode>> oneQuery) throws CloneNotSupportedException {
            //if (nodeRetrieved > 0) {
            ArrayList<QueryNode> copyOfNodes = new ArrayList<QueryNode>(payload.size());
            for (QueryNode n : payload) {
                QueryNode nClone = n.cloneTree();
                for (Entry<String, Object> e : n.getTagMap().entrySet()) {
                    nClone.setTag(e.getKey(), e.getValue());
                }
                copyOfNodes.add(nClone);
            }
            oneQuery.add(copyOfNodes);
            //}
            //else {
            //	oneQuery.add(payload);
            //}
        }

        private int[] measurePaths(List<List<Integer>> paths) {
            int[] measuredPaths = new int[paths.size()];
            int j = 0;
            for (List<Integer> path : paths) {
                assert path.size() % 2 == 0;
                int length = 0;
                for (int i = 0; i < path.size(); i = i + 2) {
                    length += path.get(i + 1) - path.get(i);
                }
                length += (path.size() / 2) - 1; // number of edges (assuming it equals 1 space, hm...)
                measuredPaths[j++] = length;
            }
            return measuredPaths;
        }

        private boolean isWhitespaceGap(List<Integer> path, int gapIndex) {
            QueryNode sourceNode = findSourceNode();
            if (sourceNode == null) {
                return false;
            }
            String source = (String) sourceNode.getTag(
                    AqpAnalyzerQueryNodeProcessor.SOURCE_QUERY_TEXT);
            Integer queryStart = (Integer) sourceNode.getTag(
                    AqpAnalyzerQueryNodeProcessor.SOURCE_QUERY_START);
            if (source == null || queryStart == null) {
                return false;
            }
            int start = path.get(gapIndex) - queryStart;
            int end = path.get(gapIndex + 1) - queryStart;
            if (start < 0 || end < start || end > source.length()) {
                return false;
            }
            for (int i = start; i < end; i++) {
                if (!Character.isWhitespace(source.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        private boolean isOmittedSourceGap(List<Integer> path, int gapIndex) {
            QueryNode sourceNode = findSourceNode();
            if (sourceNode == null) {
                return false;
            }
            String source = (String) sourceNode.getTag(
                    AqpAnalyzerQueryNodeProcessor.SOURCE_QUERY_TEXT);
            Integer queryStart = (Integer) sourceNode.getTag(
                    AqpAnalyzerQueryNodeProcessor.SOURCE_QUERY_START);
            @SuppressWarnings("unchecked")
            List<int[]> omittedRanges = (List<int[]>) sourceNode.getTag(
                    AqpAnalyzerQueryNodeProcessor.OMITTED_SOURCE_RANGES);
            if (source == null || queryStart == null || omittedRanges == null) {
                return false;
            }
            int start = path.get(gapIndex) - queryStart;
            int end = path.get(gapIndex + 1) - queryStart;
            if (start < 0 || end < start || end > source.length()) {
                return false;
            }
            boolean omitted = false;
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    continue;
                }
                boolean covered = false;
                for (int[] range : omittedRanges) {
                    if (range[0] <= queryStart + i && range[1] > queryStart + i) {
                        covered = true;
                        break;
                    }
                }
                if (!covered) {
                    return false;
                }
                omitted = true;
            }
            return omitted;
        }

        private QueryNode findSourceNode() {
            for (QueryNode node : payload) {
                if (node.getTag(AqpAnalyzerQueryNodeProcessor.SOURCE_QUERY_TEXT) != null) {
                    return node;
                }
            }
            for (NodeOfQuery child : children) {
                QueryNode node = child.findSourceNode();
                if (node != null) {
                    return node;
                }
            }
            return null;
        }

        private int[] measurePathsInclGaps(List<List<Integer>> paths) {
            int[] measuredPaths = new int[paths.size()];
            int pathLength = 0;
            QueryNode sourceNode = findSourceNode();
            String source = sourceNode == null ? null : (String) sourceNode.getTag(
                    AqpAnalyzerQueryNodeProcessor.SOURCE_QUERY_TEXT);
            Integer origin = sourceNode == null ? null : (Integer) sourceNode.getTag(
                    AqpAnalyzerQueryNodeProcessor.SOURCE_QUERY_START);
            for (int j = 0; j < measuredPaths.length; j++) {
                List<Integer> path = paths.get(j);
                assert path.size() % 2 == 0;
                pathLength = path.get(path.size() - 1) - path.get(0);
                if (source != null && origin != null) {
                    List<Integer> leading = java.util.Arrays.asList(origin, path.get(0));
                    if (isWhitespaceGap(leading, 0) || isOmittedSourceGap(leading, 0)) {
                        pathLength += path.get(0) - origin;
                    }
                    int last = path.get(path.size() - 1);
                    List<Integer> trailing = java.util.Arrays.asList(last, origin + source.length());
                    if (isWhitespaceGap(trailing, 0) || isOmittedSourceGap(trailing, 0)) {
                        pathLength += origin + source.length() - last;
                    }
                }
                int gaps = 0;
                for (int i = 1; i < path.size() - 1; i += 2) {
                    int g = path.get(i + 1) - path.get(i);
                    gaps += g;
                    if (g <= 2 || isWhitespaceGap(path, i)
                            || isOmittedSourceGap(path, i)) {
                        gaps -= g;
                    }
                }
                measuredPaths[j] = pathLength - gaps;
            }
            return measuredPaths;
        }
    }


    class QueryPath {
        private final ArrayList<Integer> data;
        private final ArrayList<List<Integer>> paths;

        public QueryPath() {
            data = new ArrayList<Integer>();
            paths = new ArrayList<List<Integer>>();
        }

        public void push(Integer position) {
            data.add(position);
        }

        public Integer pop() {
            return data.remove(data.size() - 1);
        }

        public void terminus() {
            ArrayList<Integer> newData = new ArrayList<Integer>(data);
            paths.add(newData);
        }

        public List<List<Integer>> getAllPaths() {
            return paths;
        }
    }

    class QueryBuilder {
        public boolean isMultiDimensional = false;

        public void reset() {
            isMultiDimensional = false;
        }

        public QueryNode buildQueryElement(List<QueryNode> samePositionElements) {
            if (samePositionElements.size() > 1) { // synonymous tokens at the same position/offset
                isMultiDimensional = true;
                AqpOrQueryNode q = new AqpOrQueryNode(samePositionElements);
                q.setTag(AqpQueryTreeBuilder.SYNONYMS, true);
                return q;
            } else {
                return samePositionElements.get(0);
            }
        }

        public QueryNode buildQuery(List<QueryNode> queryElements) {
            return new AqpAndQueryNode(queryElements);
        }

        public QueryNode buildTopQuery(List<QueryNode> mainQueryClauses) {
            if (mainQueryClauses.size() == 1) {
                return mainQueryClauses.get(0);
            } else {
                AqpOrQueryNode mainQ = new AqpOrQueryNode(mainQueryClauses);
                mainQ.setTag(AqpQueryTreeBuilder.SYNONYMS, true);
                return mainQ;
            }
        }
    }
}
