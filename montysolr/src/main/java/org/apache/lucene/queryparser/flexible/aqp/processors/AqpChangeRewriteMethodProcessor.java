/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsScoringQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TokenizedPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiTermRewriteMethodProcessor;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.RefCounted;

import java.io.IOException;
import java.util.*;

public class AqpChangeRewriteMethodProcessor extends
        AqpQueryNodeProcessorImpl {
    boolean first = true;
    public static final String TOPN_SCORE_APPLIED = "aqp.topn.score.applied";
    public static final String TOPN_SCORE_MODIFIER = "aqp.topn.score.modifier";
    private Set<String> types = null;
    private Set<String> fields = null;
    private Set<String> ignoredFields = null;
    private boolean isTopNOnly(QueryNode node) {
        if (node instanceof AqpFunctionQueryNode) {
            AqpFunctionQueryNode function = (AqpFunctionQueryNode) node;
            return "topn".equalsIgnoreCase(function.getName());
        }
        List<QueryNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return false;
        }
        for (QueryNode child : children) {
            if (!isTopNOnly(child)) {
                return false;
            }
        }
        return true;
    }
    private boolean containsTopN(QueryNode node) {
        if (node instanceof AqpFunctionQueryNode) {
            AqpFunctionQueryNode function = (AqpFunctionQueryNode) node;
            if ("topn".equalsIgnoreCase(function.getName())) {
                return true;
            }
        }
        List<QueryNode> children = node.getChildren();
        if (children != null) {
            for (QueryNode child : children) {
                if (containsTopN(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    private QueryNode scoreNonTopN(QueryNode node, float modifier) {
        if (isTopNOnly(node)) {
            return node;
        }
        if (!containsTopN(node)) {
            return new AqpAdsabsScoringQueryNode(node, "cite_read_boost", modifier);
        }
        List<QueryNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return node;
        }
        List<QueryNode> rewritten = new ArrayList<>(children.size());
        for (QueryNode child : children) {
            rewritten.add(scoreNonTopN(child, modifier));
        }
        node.set(rewritten);
        return node;
    }


    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {

        if (first && getConfigVal("aqp.classic_scoring.modifier", "").strip() != "") {
            SolrQueryRequest req = this.getQueryConfigHandler()
                    .get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
                    .getRequest();
            if (!Boolean.TRUE.equals(req.getContext().get(TOPN_SCORE_APPLIED))) {
                float modifier = Float.parseFloat(getConfigVal("aqp.classic_scoring.modifier"));
                // Preserve the configured modifier in request context for
                // lazy nested parsers, including opaque function arguments
                // whose topn() nodes are not visible in this AST.
                req.getContext().put(TOPN_SCORE_MODIFIER, modifier);
                if (containsTopN(node)) {
                    ModifiableSolrParams params = new ModifiableSolrParams(req.getParams());
                    params.remove("aqp.classic_scoring.modifier");
                    req.setParams(params);

                    node = scoreNonTopN(node, modifier);
                    first = false;
                    return node;
                }

                // TODO: i don't want to make the source field be changed with URL params
                // but i'd like it to be configurable

                ModifiableSolrParams params = new ModifiableSolrParams(req.getParams());
                params.remove("aqp.classic_scoring.modifier");
                req.setParams(params);


                node = new AqpAdsabsScoringQueryNode(node, "cite_read_boost", modifier);
            }
        }
        first = false;
        return node;

    }

    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
        String key;
        String method;
        if (node instanceof PrefixWildcardQueryNode) {
            key = "aqp.qprefix.scoring." + ((FieldQueryNode) node).getFieldAsString();
            if ((method = getConfigVal(key, "")) != "") {
                setRewriteMethod(node, method);
            }
        } else if (node instanceof WildcardQueryNode) {
            key = "aqp.qwildcard.scoring." + ((FieldQueryNode) node).getFieldAsString();
            if ((method = getConfigVal(key, "")) != "") {
                setRewriteMethod(node, method);
            }
        } else if (node instanceof RegexpQueryNode) {
            key = "aqp.qregex.scoring." + ((FieldQueryNode) node).getFieldAsString();
            if ((method = getConfigVal(key, "")) != "") {
                setRewriteMethod(node, method);
            }
        } else if (node instanceof SlopQueryNode
                && node.getChildren() != null
                && node.getChildren().size() == 1
                && node.getChildren().get(0) instanceof AqpOrQueryNode) {
            AqpOrQueryNode alternatives = (AqpOrQueryNode) node.getChildren().get(0);
            List<QueryNode> branches = alternatives.getChildren();
            List<QueryNode> sloppedBranches = new LinkedList<>();
            int slop = ((SlopQueryNode) node).getValue();
            boolean explicitSlop = Boolean.TRUE.equals(
                    node.getTag(AqpFuzzyModifierProcessor.EXPLICIT_SLOP));
            for (QueryNode branch : branches) {
                SlopQueryNode branchSlop = new SlopQueryNode(branch, slop);
                if (explicitSlop) {
                    branchSlop.setTag(AqpFuzzyModifierProcessor.EXPLICIT_SLOP, true);
                }
                sloppedBranches.add(branchSlop);
            }
            AqpOrQueryNode sloppedAlternatives = new AqpOrQueryNode(sloppedBranches);
            Object synonymsTag = alternatives.getTag(AqpQueryTreeBuilder.SYNONYMS);
            if (synonymsTag != null) {
                sloppedAlternatives.setTag(AqpQueryTreeBuilder.SYNONYMS, synonymsTag);
            }
            return sloppedAlternatives;
        } else if (node instanceof MultiPhraseQueryNode) {
            if (getConfigVal("aqp.multiphrase.keep_one", null) != null) {

                if (types == null) {
                    types = new HashSet<String>();
                    Collections.addAll(types, getConfigVal("aqp.multiphrase.keep_one").split(","));
                }

                if (getConfigVal("aqp.multiphrase.keep_one.ignore.fields", null) != null) {

                    if (ignoredFields == null) {
                        ignoredFields = new HashSet<String>();
                        Collections.addAll(ignoredFields, getConfigVal("aqp.multiphrase.keep_one.ignore.fields").split(","));
                    }

                    if (ignoredFields.contains((String) ((MultiPhraseQueryNode) node).getField())) {

                        // for ignored fields, we don't want to do proximity search
                        for (QueryNode child : node.getChildren()) {
                            child.setTag(AqpAnalyzerQueryNodeProcessor.MAX_MULTI_TOKEN_SIZE, 0);
                        }

                        return node;
                    }
                }


                AqpRequestParams reqAttr = this.getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
                if (reqAttr != null) {
                    IndexSchema schema = reqAttr.getRequest().getSchema();
                    FieldType fType = schema.getFieldType((String) ((MultiPhraseQueryNode) node).getField());
                    if (fType != null) {
                        node.setTag("field.is.tokenized", fType.isTokenized());
                    }
                }


                try {
                    node = simplifyMultiphrase(node, types);
                } catch (IOException e) {
                    throw new QueryNodeException(e);
                }
            }
        } else if (node instanceof AqpOrQueryNode &&
                node.getTag(AqpQueryTreeBuilder.SYNONYMS) != null &&
                (Boolean) node.getTag(AqpQueryTreeBuilder.SYNONYMS)
        ) {
            List<QueryNode> children = node.getChildren();
            FieldQueryNode firstTerm = null;
            boolean hasSpanAlternatives = false;
            for (QueryNode child : children) {
                if (child instanceof FieldQueryNode) {
                    if (firstTerm == null) {
                        firstTerm = (FieldQueryNode) child;
                    }
                } else {
                    hasSpanAlternatives = true;
                }
            }
            if (firstTerm != null && getFields().contains(firstTerm.getFieldAsString())) {
                List<QueryNode> terms = children;
                if (hasSpanAlternatives) {
                    terms = new ArrayList<>(children.size());
                    for (QueryNode child : children) {
                        if (child instanceof FieldQueryNode) {
                            terms.add(child);
                        }
                    }
                }
                List<QueryNode> selected = new ArrayList<>(children.size());
                try {
                    pickSynonyms(terms, selected, getTypes());
                    // Exact raw phrase/span alternatives are not same-position terms.
                    if (hasSpanAlternatives) {
                        for (QueryNode child : children) {
                            if (!(child instanceof FieldQueryNode)) {
                                selected.add(child);
                            }
                        }
                    }
                    node.set(selected);
                } catch (IOException e) {
                    throw new QueryNodeException(e);
                }
            }
        }

        return isSynonymDisjunction(node) ? compactPhraseAlternatives(node) : node;
    }

    private boolean isSynonymDisjunction(QueryNode node) {
        return node instanceof AqpOrQueryNode
                && Boolean.TRUE.equals(node.getTag(AqpQueryTreeBuilder.SYNONYMS));
    }

    private void collectExactPhrases(QueryNode node, List<QueryNode> phrases) {
        if (isSynonymDisjunction(node)) {
            for (QueryNode child : node.getChildren()) {
                collectExactPhrases(child, phrases);
            }
        } else if (node instanceof SlopQueryNode && ((SlopQueryNode) node).getValue() == 0
                && Boolean.TRUE.equals(node.getTag(AqpPostAnalysisProcessor.RAW_POSITIONAL_PATH))) {
            QueryNode phrase = ((SlopQueryNode) node).getChild();
            if (phrase instanceof MultiPhraseQueryNode || phrase instanceof TokenizedPhraseQueryNode) {
                phrases.add(node);
            }
        }
    }

    private QueryNode compactPhraseAlternatives(QueryNode node) {
        List<QueryNode> phrases = new ArrayList<>();
        collectExactPhrases(node, phrases);
        if (phrases.size() < 2) {
            return node;
        }
        Set<QueryNode> covered = Collections.newSetFromMap(new IdentityHashMap<>());
        for (QueryNode candidate : phrases) {
            for (QueryNode covering : phrases) {
                if (candidate != covering && !covered.contains(covering)
                        && coversExactPhrase(covering, candidate)) {
                    covered.add(candidate);
                    break;
                }
            }
        }
        return covered.isEmpty() ? node : removeCoveredPhrases(node, covered);
    }

    private boolean coversExactPhrase(QueryNode covering, QueryNode candidate) {
        List<QueryNode> left = ((SlopQueryNode) covering).getChild().getChildren();
        List<QueryNode> right = ((SlopQueryNode) candidate).getChild().getChildren();
        int l = 0;
        int r = 0;
        while (l < left.size() && r < right.size()) {
            int position = ((FieldQueryNode) left.get(l)).getPositionIncrement();
            if (((FieldQueryNode) right.get(r)).getPositionIncrement() != position) {
                return false;
            }
            int end = l + 1;
            while (end < left.size()
                    && ((FieldQueryNode) left.get(end)).getPositionIncrement() == position) {
                end++;
            }
            do {
                FieldQueryNode term = (FieldQueryNode) right.get(r++);
                boolean found = false;
                for (int i = l; i < end; i++) {
                    FieldQueryNode option = (FieldQueryNode) left.get(i);
                    if (option.getFieldAsString().equals(term.getFieldAsString())
                            && option.getTextAsString().equals(term.getTextAsString())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            } while (r < right.size()
                    && ((FieldQueryNode) right.get(r)).getPositionIncrement() == position);
            l = end;
        }
        return l == left.size() && r == right.size();
    }

    private QueryNode removeCoveredPhrases(QueryNode node, Set<QueryNode> covered) {
        if (covered.contains(node)) {
            return null;
        }
        if (isSynonymDisjunction(node)) {
            List<QueryNode> children = new ArrayList<>();
            for (QueryNode child : node.getChildren()) {
                QueryNode kept = removeCoveredPhrases(child, covered);
                if (kept != null) {
                    children.add(kept);
                }
            }
            if (children.isEmpty()) {
                return null;
            }
            node.set(children);
        }
        return node;
    }

    private Set<String> getTypes() {
        if (types == null) {
            types = new HashSet<String>();
            Collections.addAll(types, getConfigVal("aqp.multiphrase.keep_one", "").split(","));
        }
        return types;
    }

    private Set<String> getFields() {
        if (fields == null) {
            fields = new HashSet<String>();
            Collections.addAll(fields, getConfigVal("aqp.multiphrase.fields", "").split(","));
        }
        return fields;
    }


    private QueryNode simplifyMultiphrase(QueryNode node, Set<String> typesToKeep) throws IOException {
        List<QueryNode> children = node.getChildren();
        if (children == null) {
            return node;
        }

        TreeMap<Integer, List<QueryNode>> positionTermMap = new TreeMap<>();
        for (QueryNode child : children) {
            FieldQueryNode termNode = (FieldQueryNode) child;
            List<QueryNode> termList = positionTermMap.get(termNode.getPositionIncrement());
            if (termList == null) {
                termList = new LinkedList<>();
                positionTermMap.put(termNode.getPositionIncrement(), termList);
            }
            termList.add(termNode);
        }

        List<QueryNode> literalPath = findLiteralPath(positionTermMap, typesToKeep);
        if (literalPath == null || hasMultiTokenSynonym(children)) {
            List<QueryNode> selected = new LinkedList<>();
            for (List<QueryNode> termList : positionTermMap.values()) {
                if (termList.size() > 1) {
                    pickSynonyms(termList, selected, typesToKeep);
                } else {
                    selected.add(termList.get(0));
                }
            }
            node.set(selected);
            return node;
        }

        List<QueryNode> synonymPath = new LinkedList<>();
        for (List<QueryNode> termList : positionTermMap.values()) {
            if (termList.size() > 1) {
                List<QueryNode> clonedTerms = new LinkedList<>();
                for (QueryNode term : termList) {
                    clonedTerms.add(cloneWithTags(term));
                }
                pickSynonyms(clonedTerms, synonymPath, typesToKeep);
            } else {
                synonymPath.add(cloneWithTags(termList.get(0)));
            }
        }

        MultiPhraseQueryNode literalPhrase = new MultiPhraseQueryNode();
        for (QueryNode term : literalPath) {
            literalPhrase.add((FieldQueryNode) term);
        }
        MultiPhraseQueryNode synonymPhrase = new MultiPhraseQueryNode();
        synonymPhrase.set(synonymPath);
        AqpOrQueryNode alternatives = new AqpOrQueryNode(Arrays.asList(literalPhrase, synonymPhrase));
        alternatives.setTag(AqpQueryTreeBuilder.SYNONYMS, true);
        return alternatives;
    }

    /**
     * Returns the user's original token at every position, or null when some stacked position holds only
     * synonyms, because a phrase that skips a position would search for different text.
     */
    private List<QueryNode> findLiteralPath(TreeMap<Integer, List<QueryNode>> positionTermMap,
                                            Set<String> typesToKeep) {
        List<QueryNode> literalPath = new LinkedList<>();
        for (List<QueryNode> termList : positionTermMap.values()) {
            QueryNode literal = termList.size() == 1 ? termList.get(0) : null;
            for (int i = 0; literal == null && i < termList.size(); i++) {
                String type = (String) termList.get(i).getTag(AqpAnalyzerQueryNodeProcessor.TYPE_ATTRIBUTE);
                if (!typesToKeep.contains(type)) {
                    literal = termList.get(i);
                }
            }
            if (literal == null) {
                return null;
            }
            literalPath.add(literal);
        }
        return literalPath;
    }

    private QueryNode cloneWithTags(QueryNode node) throws IOException {
        try {
            QueryNode clone = node.cloneTree();
            for (Map.Entry<String, Object> entry : node.getTagMap().entrySet()) {
                clone.setTag(entry.getKey(), entry.getValue());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new IOException(e);
        }
    }

    private void pickSynonyms(List<QueryNode> termList, List<QueryNode> newList, Set<String> typesToKeep) throws IOException {

        SolrQueryRequest req = this.getQueryConfigHandler()
                .get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
                .getRequest();
        SolrIndexSearcher searcher;
        RefCounted<SolrIndexSearcher> s = req.getCore().getRegisteredSearcher();

        try {
            searcher = s.get();

            // there exists two situations:
            //   1. user input was short and resulted in multi-token synonym
            //   2. user input was series of tokens that were identified as multi-token synonym
            // here we have to decide what scenario it is and for
            //   1. pick the most-frequent synonym
            //   2. pick the least-frequent synonym
            // we'll use the information about the original input token begin and end
            // positions, to guess what situation we are in

            int equalLength = 0;
            int userInputLen = 0;
            int numTokens = 0;
            int tokenLongerThanInput = 0;
            int tokenShorterThanInput = 0;
            int begin = 0;
            int end = 0;
            int len = 0;
            String text;
            FieldQueryNode maxFreqTerm = null;
            FieldQueryNode minFreqTerm = null;
            FieldQueryNode closestLenTerm = null;
            int termFreq;
            int minFreq = Integer.MAX_VALUE;
            int maxFreq = Integer.MIN_VALUE;
            Integer closestLen = null;
            int oldSize = newList.size();

            // first decide one scenarios 1. xor 2.
            for (QueryNode n : termList) {

                String t = (String) n.getTag(AqpAnalyzerQueryNodeProcessor.TYPE_ATTRIBUTE);
                if (t != null && !typesToKeep.contains(t)) {
                    continue;
                }

                FieldQueryNode termNode = (FieldQueryNode) n;
                begin = termNode.getBegin();
                end = termNode.getEnd();
                text = termNode.getTextAsString();
                len = text.length() - (text.indexOf("::") + 2);
                userInputLen += len;
                numTokens++;

                // how many times the current token fits into the user input
                // anything below 1.0 means the current token is longer than
                // what user typed

                float ratio = (float) (end - begin) / (float) len;

                if (ratio == 1.0f) {
                    equalLength++;
                } else if (ratio < 1.2f) { // we give it bit of slack
                    tokenLongerThanInput++;
                } else {
                    tokenShorterThanInput++;
                }

                if (closestLen == null || Math.abs((end - begin) - len) < closestLen) {
                    closestLen = Math.abs((end - begin) - len);
                    closestLenTerm = termNode;
                }


                // careful, 0 means the term does not exist
                termFreq = searcher.docFreq(new Term(termNode.getFieldAsString(), text));

                // we'll ignore unknown terms
                if (termFreq > 0) {
                    if (termFreq < minFreq) {
                        minFreqTerm = termNode;
                        minFreq = termFreq;
                    } else if (termFreq == minFreq && text.length() > minFreqTerm.getValue().length()) {
                        minFreqTerm = termNode; // if same docfreq, pick longer ones
                    }

                    if (termFreq > maxFreq) {
                        maxFreqTerm = termNode;
                        maxFreq = termFreq;
                    } else if (termFreq == maxFreq && text.length() < minFreqTerm.getValue().length()) {
                        maxFreqTerm = termNode; // if same frequency, pick shorter one
                    }
                }

            }

            String strategy = null;
            if (tokenLongerThanInput > tokenShorterThanInput) {
                strategy = "mostFrequent"; // most tokens are longer than input (i.e. user typed acronym)
                // pick the shortest - i.e. more frequent term
            } else if (tokenShorterThanInput > tokenLongerThanInput) {
                strategy = "leastFrequent"; // most tokens were equal or shorter than the user's input
                // pick the longest - i.e. more specific term
            } else { // they were equal lengths
                strategy = "cantDecide";
                if (minFreqTerm != null && maxFreqTerm != null) {
                    float diffMax = Math.abs((float) userInputLen / numTokens - maxFreqTerm.getTextAsString().length());
                    float diffMin = Math.abs((float) userInputLen / numTokens - minFreqTerm.getTextAsString().length());

                    if (diffMax < diffMin) { // longer term is closer to input
                        strategy = "leastFrequent";
                    } else if (diffMin < diffMax) { // shorter term is closer to the user input length
                        strategy = "mostFrequent";
                    }
                }
            }


            if (strategy.equals("mostFrequent") && maxFreqTerm != null) {
                newList.add(maxFreqTerm);
            } else if (strategy.equals("leastFrequent") && minFreqTerm != null) {
                newList.add(minFreqTerm);
            } else if (strategy.equals("cantDecide") && closestLenTerm != null) {
                newList.add(closestLenTerm);
            }
            if (newList.size() == oldSize) { // we didn't find any type that would satisfy the condition
                QueryNode picked = termList.get(0);
                // pick the longest if you can
                int x = 0;
                for (QueryNode t : termList) {
                    int l = ((FieldQueryNode) t).getTextAsString().length();
                    if (l > x) {
                        x = l;
                        picked = t;
                    }
                }
                newList.add(picked);
            }

            FieldQueryNode selected = (FieldQueryNode) newList.get(newList.size() - 1);
            int selectedBegin = selected.getBegin();
            int selectedEnd = selected.getEnd();
            int equivalentWidth = 0;
            if (selectedBegin >= 0 && selectedEnd >= selectedBegin) {
                for (QueryNode candidate : termList) {
                    FieldQueryNode fieldCandidate = (FieldQueryNode) candidate;
                    if (fieldCandidate.getBegin() == selectedBegin
                            && fieldCandidate.getEnd() == selectedEnd) {
                        Integer width = (Integer) fieldCandidate.getTag(
                                AqpAnalyzerQueryNodeProcessor.MAX_MULTI_TOKEN_SIZE);
                        if (width != null && width > equivalentWidth) {
                            equivalentWidth = width;
                        }
                    }
                }
            }
            if (equivalentWidth > 1) {
                selected.setTag(AqpAnalyzerQueryNodeProcessor.MAX_MULTI_TOKEN_SIZE,
                        equivalentWidth);
            }

        } finally {
            s.decref();
        }
    }

    private boolean hasMultiTokenSynonym(List<QueryNode> children) {
        for (QueryNode child : children) {
            String type = (String) child.getTag(AqpAnalyzerQueryNodeProcessor.TYPE_ATTRIBUTE);
            if ("SYNONYM".equals(type)
                    && ((FieldQueryNode) child).getTextAsString().indexOf(' ') >= 0) {
                return true;
            }
        }
        return false;
    }

    private void setRewriteMethod(QueryNode node, String method) throws QueryNodeException {
        if ("constant".equals(method)) {
            node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, MultiTermQuery.CONSTANT_SCORE_BLENDED_REWRITE);
        } else if ("constant_boolean".equals(method)) {
            node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, MultiTermQuery.CONSTANT_SCORE_BOOLEAN_REWRITE);
        } else if ("boolean".equals(method)) {
            node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, MultiTermQuery.SCORING_BOOLEAN_REWRITE);
        } else if ("topterms_blended".equals(method)) {
            node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, new MultiTermQuery.TopTermsBlendedFreqScoringRewrite(1024));
        } else if ("topterms".equals(method)) {
            node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, new MultiTermQuery.TopTermsScoringBooleanQueryRewrite(1024));
        } else if ("topterms_boosted".equals(method)) {
            node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, new MultiTermQuery.TopTermsBoostOnlyBooleanQueryRewrite(1024));
        } else {
            throw new QueryNodeException(new MessageImpl(
                    QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED, "Unknown rewrite method: \"" + method + "\""));
        }
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children) throws QueryNodeException {
        return children;
    }

}
