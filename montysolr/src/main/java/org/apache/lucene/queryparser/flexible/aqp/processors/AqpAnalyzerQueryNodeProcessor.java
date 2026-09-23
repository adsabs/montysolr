package org.apache.lucene.queryparser.flexible.aqp.processors;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.*;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.AnalyzerQueryNodeProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * This is an improved version of the {@link AnalyzerQueryNodeProcessor} it is
 * better because it keeps track of the position offset which is absolutely
 * indispensable for proper parsing of expanded queries. And also we save the
 * type attribute name with the node
 * <p>
 * TODO: send a patch and make them accept it
 * <p>
 * This processor verifies if {@link ConfigurationKeys#ANALYZER} is defined in
 * the {@link QueryConfigHandler}. If it is and the analyzer is not
 * <code>null</code>, it looks for every {@link FieldQueryNode} that is not
 * {@link WildcardQueryNode}, {@link FuzzyQueryNode} or {@link RangeQueryNode}
 * contained in the query node tree, then it applies the analyzer to that
 * {@link FieldQueryNode} object.
 * <p>
 * If the analyzer return only one term, the returned term is set to the
 * {@link FieldQueryNode} and it's returned.
 * <p>
 * If the analyzer return more than one term, a {@link TokenizedPhraseQueryNode}
 * or {@link MultiPhraseQueryNode} is created, whether there is one or more
 * terms at the same position, and it's returned.
 * <p>
 * If no term is returned by the analyzer a {@link NoTokenFoundQueryNode} object
 * is returned.
 *
 * @see ConfigurationKeys#ANALYZER
 * @see Analyzer
 * @see TokenStream
 */

public class AqpAnalyzerQueryNodeProcessor extends QueryNodeProcessorImpl {

    public static String TYPE_ATTRIBUTE = "token_type_attribute";
    public static String SOURCE_TEXT = "source_text";
    public static String SOURCE_QUERY_TEXT = "source_query_text";
    public static String SOURCE_QUERY_START = "source_query_start";
    public static String MAX_MULTI_TOKEN_SIZE = "max_multi_token_size";
    public static String RAW_MULTI_TOKEN_ALIAS = "raw_multi_token_alias";
    public static String OMITTED_SOURCE_TOKEN = "omitted_source_token";
    public static final String OMITTED_SOURCE_RANGES = "omitted_source_ranges";
    public static final String SOURCE_INDEX_START = "source_index_start";
    public static final String SOURCE_INDEX_END = "source_index_end";
    public static final String RAW_INDEX_POSITION_DELTA = "raw_index_position_delta";
    private Analyzer analyzer;
    private final Map<String, List<IndexTokenPosition>> indexPositionCache =
            new HashMap<>();

    private boolean positionIncrementsEnabled;

    private static final class IndexTokenPosition {
        private final int start;
        private final int end;
        private final int position;
        private final int positionEnd;

        private IndexTokenPosition(int start, int end, int position, int positionEnd) {
            this.start = start;
            this.end = end;
            this.position = position;
            this.positionEnd = positionEnd;
        }
    }


    @Override
    public QueryNode process(QueryNode queryTree) throws QueryNodeException {
        indexPositionCache.clear();
        Analyzer configuredAnalyzer = getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);

        if (configuredAnalyzer != null) {
            this.analyzer = configuredAnalyzer;
            this.positionIncrementsEnabled = false;
            Boolean positionIncrementsEnabled = getQueryConfigHandler().get(
                    ConfigurationKeys.ENABLE_POSITION_INCREMENTS);

            if (positionIncrementsEnabled != null) {
                this.positionIncrementsEnabled = positionIncrementsEnabled;
            }

            if (this.analyzer != null) {
                return super.process(queryTree);
            }
        }

        return queryTree;
    }

    @Override
    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {

        if (node instanceof TextableQueryNode
                && !(node instanceof WildcardQueryNode)
                && !(node instanceof FuzzyQueryNode)
                && !(node instanceof RegexpQueryNode)
                && !(node.getParent() instanceof RangeQueryNode)) {

            FieldQueryNode fieldNode = ((FieldQueryNode) node);
            int queryStart = Math.max(fieldNode.getBegin(), 0); // could be -1
            String text = fieldNode.getTextAsString();
            String field = fieldNode.getFieldAsString();
            boolean preservePositionIncrements =
                    this.positionIncrementsEnabled || (field != null && field.endsWith("_nosyn"));

            TokenStream source = null;
            CachingTokenFilter buffer = null;

            try {
                source = this.analyzer.tokenStream(field, new StringReader(text));
                source.reset();
                buffer = new CachingTokenFilter(source); //TODO: use reusable strategy?

                PositionIncrementAttribute posIncrAtt = null;
                int numTokens = 0;
                int positionCount = 0;
                boolean severalTokensAtSamePosition = false;

                if (buffer.hasAttribute(PositionIncrementAttribute.class)) {
                    posIncrAtt = buffer.getAttribute(PositionIncrementAttribute.class);
                }

                PositionLengthAttribute posLengthAtt = null;
                if (buffer.hasAttribute(PositionLengthAttribute.class)) {
                    posLengthAtt = buffer.getAttribute(PositionLengthAttribute.class);
                }

                TypeAttribute typeAtt = null;
                if (buffer.hasAttribute(TypeAttribute.class)) {
                    typeAtt = buffer.getAttribute(TypeAttribute.class);
                }
                CharTermAttribute termAtt = buffer.getAttribute(CharTermAttribute.class);

                while (buffer.incrementToken()) {
                    numTokens++;
                    int positionIncrement = (posIncrAtt != null) ? posIncrAtt
                            .getPositionIncrement() : 1;
                    if (positionIncrement != 0) {
                        positionCount += positionIncrement;
                    } else {
                        severalTokensAtSamePosition = true;
                    }
                }


                // rewind the buffer stream
                buffer.reset();

                // close original stream - all tokens buffered
                source.close();
                source = null;
                // Index and query analyzers may share reusable components.
                // Capture source positions only after closing the query stream.
                List<IndexTokenPosition> indexPositions =
                        node instanceof QuotedFieldQueryNode && numTokens > 1
                                ? getIndexTokenPositions(field, text)
                                : Collections.emptyList();

                if (!buffer.hasAttribute(CharTermAttribute.class)) {
                    return new NoTokenFoundQueryNode();
                }


                int offsetStart = -1;
                int offsetEnd = -1;
                OffsetAttribute offsetAtt;
                if (buffer.hasAttribute(OffsetAttribute.class)) {
                    offsetAtt = buffer.getAttribute(OffsetAttribute.class);
                } else {
                    offsetAtt = null;
                }

                boolean punctuationIdentifier = isPunctuationIdentifier(text);
                boolean forcePhrase = node instanceof QuotedFieldQueryNode
                        || punctuationIdentifier;
                if (numTokens == 0) {
                    return new NoTokenFoundQueryNode();

                } else if (numTokens == 1) {
                    String term = null;
                    try {
                        boolean hasNext;
                        hasNext = buffer.incrementToken();
                        assert hasNext;
                        term = termAtt.toString();

                    } catch (IOException e) {
                        // safe to ignore, because we know the number of tokens
                    }

                    fieldNode.setText(term);
                    if (offsetAtt != null) {
                        fieldNode.setBegin(queryStart + offsetAtt.startOffset());
                        fieldNode.setEnd(queryStart + offsetAtt.endOffset());
                    }
                    setSourceIndexBounds(fieldNode, indexPositions, fieldNode.getBegin() - queryStart, fieldNode.getEnd() - queryStart);
                    if (typeAtt != null)
                        fieldNode.setTag(TYPE_ATTRIBUTE, typeAtt.type());
                    fieldNode.setTag(SOURCE_QUERY_TEXT, text);
                    fieldNode.setTag(SOURCE_QUERY_START, queryStart);
                    return fieldNode;

                } else if (severalTokensAtSamePosition || !forcePhrase) {
                    if (positionCount == 1 || !forcePhrase) {
                        // no phrase query:
                        LinkedList<QueryNode> children = new LinkedList<QueryNode>();

                        for (int i = 0; i < numTokens; i++) {
                            String term = null;
                            int positionLength = 1;
                            offsetStart = offsetEnd = -1;
                            try {
                                boolean hasNext = buffer.incrementToken();
                                assert hasNext;
                                term = termAtt.toString();
                                if (offsetAtt != null) {
                                    offsetStart = queryStart + offsetAtt.startOffset();
                                    offsetEnd = queryStart + offsetAtt.endOffset();
                                }
                                if (posLengthAtt != null) {
                                    positionLength = posLengthAtt.getPositionLength();
                                }

                            } catch (IOException e) {
                                // safe to ignore, because we know the number of tokens
                            }
                            FieldQueryNode fq = new FieldQueryNode(field, term, offsetStart,
                                    offsetEnd);
                            setSourceIndexBounds(fq, indexPositions, offsetStart - queryStart, offsetEnd - queryStart);
                            int multiTokenSize = getMultiTokenSize(term, positionLength,
                                    offsetStart, offsetEnd, queryStart, text);
                            if (typeAtt != null)
                                fq.setTag(TYPE_ATTRIBUTE, typeAtt.type());
                            if (multiTokenSize > 1)
                                fq.setTag(MAX_MULTI_TOKEN_SIZE, multiTokenSize);
                            if (isRawMultiTokenAlias(term, positionLength, offsetStart,
                                    offsetEnd, queryStart, text,
                                    typeAtt == null ? null : typeAtt.type()))
                                fq.setTag(RAW_MULTI_TOKEN_ALIAS, true);
                            String sourceValue = getSourceText(offsetStart, offsetEnd,
                                    queryStart, text);
                            if (sourceValue != null)
                                fq.setTag(SOURCE_TEXT, sourceValue);
                            fq.setTag(SOURCE_QUERY_TEXT, text);
                            fq.setTag(SOURCE_QUERY_START, queryStart);
                            children.add(fq);
                        }
                        return new GroupQueryNode(new BooleanQueryNode(children));
                    } else {
                        // phrase query:
                        MultiPhraseQueryNode mpq = new MultiPhraseQueryNode();

                        List<FieldQueryNode> multiTerms = new ArrayList<FieldQueryNode>();
                        int position = -1;
                        int i = 0;
                        int termGroupCount = 0;

                        for (; i < numTokens; i++) {
                            String term = null;
                            offsetStart = offsetEnd = -1;
                            int positionIncrement = 1;
                            int positionLength = 1;
                            String tokenType = null;
                            try {
                                boolean hasNext = buffer.incrementToken();
                                assert hasNext;
                                term = termAtt.toString();
                                if (posIncrAtt != null) {
                                    positionIncrement = posIncrAtt.getPositionIncrement();
                                }
                                if (offsetAtt != null) {
                                    offsetStart = queryStart + offsetAtt.startOffset();
                                    offsetEnd = queryStart + offsetAtt.endOffset();
                                }
                                if (posLengthAtt != null) {
                                    positionLength = posLengthAtt.getPositionLength();
                                }
                                if (typeAtt != null) {
                                    tokenType = typeAtt.type();
                                }
                            } catch (IOException e) {
                                // safe to ignore, because we know the number of tokens
                            }

                            if (positionIncrement > 0 && multiTerms.size() > 0) {
                                for (FieldQueryNode termNode : multiTerms) {
                                    if (preservePositionIncrements) {
                                        termNode.setPositionIncrement(position);
                                    } else {
                                        termNode.setPositionIncrement(termGroupCount);
                                    }
                                    mpq.add(termNode);
                                }
                                termGroupCount++;
                                multiTerms.clear();
                            }
                            position += positionIncrement;
                            FieldQueryNode fq = new FieldQueryNode(field, term, offsetStart,
                                    offsetEnd);
                            setSourceIndexBounds(fq, indexPositions, offsetStart - queryStart, offsetEnd - queryStart);
                            int multiTokenSize = getMultiTokenSize(term, positionLength,
                                    offsetStart, offsetEnd, queryStart, text);
                            fq.setTag(TYPE_ATTRIBUTE, tokenType);
                            if (multiTokenSize > 1)
                                fq.setTag(MAX_MULTI_TOKEN_SIZE, multiTokenSize);
                            if (isRawMultiTokenAlias(term, positionLength, offsetStart,
                                    offsetEnd, queryStart, text, tokenType))
                                fq.setTag(RAW_MULTI_TOKEN_ALIAS, true);
                            String sourceValue = getSourceText(offsetStart, offsetEnd,
                                    queryStart, text);
                            if (sourceValue != null)
                                fq.setTag(SOURCE_TEXT, sourceValue);
                            fq.setTag(SOURCE_QUERY_TEXT, text);
                            fq.setTag(SOURCE_QUERY_START, queryStart);
                            multiTerms.add(fq);

                        }
                        for (FieldQueryNode termNode : multiTerms) {

                            if (preservePositionIncrements) {
                                termNode.setPositionIncrement(position);

                            } else {
                                termNode.setPositionIncrement(termGroupCount);
                            }

                            mpq.add(termNode);

                        }

                        appendOmittedSourceNodes(mpq, node, field, text, queryStart);
                        if (preservePositionIncrements && !this.positionIncrementsEnabled)
                            mpq.setTag(AqpPostAnalysisProcessor.EXACT_GRAPH_PATH, true);
                        return mpq;

                    }

                } else {

                    TokenizedPhraseQueryNode pq = new TokenizedPhraseQueryNode();

                    int position = -1;

                    for (int i = 0; i < numTokens; i++) {
                        String term = null;
                        int positionIncrement = 1;
                        int positionLength = 1;
                        offsetStart = offsetEnd = -1;

                        try {
                            boolean hasNext = buffer.incrementToken();
                            assert hasNext;
                            term = termAtt.toString();

                            if (posIncrAtt != null) {
                                positionIncrement = posIncrAtt.getPositionIncrement();
                            }

                            if (posLengthAtt != null) {
                                positionLength = posLengthAtt.getPositionLength();
                            }

                            if (offsetAtt != null) {
                                offsetStart = queryStart + offsetAtt.startOffset();
                                offsetEnd = queryStart + offsetAtt.endOffset();
                            }

                        } catch (IOException e) {
                            // safe to ignore, because we know the number of tokens
                        }
                        FieldQueryNode newFieldNode = new FieldQueryNode(field, term,
                                offsetStart, offsetEnd);
                        setSourceIndexBounds(newFieldNode, indexPositions, offsetStart - queryStart, offsetEnd - queryStart);
                        int multiTokenSize = getMultiTokenSize(term, positionLength,
                                offsetStart, offsetEnd, queryStart, text);
                        if (typeAtt != null)
                            newFieldNode.setTag(TYPE_ATTRIBUTE, typeAtt.type());
                        if (multiTokenSize > 1)
                            newFieldNode.setTag(MAX_MULTI_TOKEN_SIZE, multiTokenSize);
                        if (isRawMultiTokenAlias(term, positionLength, offsetStart,
                                offsetEnd, queryStart, text,
                                typeAtt == null ? null : typeAtt.type()))
                            newFieldNode.setTag(RAW_MULTI_TOKEN_ALIAS, true);
                        String sourceValue = getSourceText(offsetStart, offsetEnd,
                                queryStart, text);
                        if (sourceValue != null)
                            newFieldNode.setTag(SOURCE_TEXT, sourceValue);
                        newFieldNode.setTag(SOURCE_QUERY_TEXT, text);
                        newFieldNode.setTag(SOURCE_QUERY_START, queryStart);

                        if (preservePositionIncrements) {
                            position += positionIncrement;
                            newFieldNode.setPositionIncrement(position);

                        } else {
                            newFieldNode.setPositionIncrement(i);
                        }

                        pq.add(newFieldNode);

                    }
                    appendOmittedSourceNodes(pq, node, field, text, queryStart);
                    if (preservePositionIncrements && !this.positionIncrementsEnabled)
                        pq.setTag(AqpPostAnalysisProcessor.EXACT_GRAPH_PATH, true);

                    return pq;

                }
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            } finally {
                if (source != null) {
                    try {
                        source.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (buffer != null) {
                    try {
                        buffer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return node;

    }
    private List<IndexTokenPosition> getIndexTokenPositions(String field,
            String text) throws IOException {
        String key = field + "\u0000" + text;
        List<IndexTokenPosition> cached = indexPositionCache.get(key);
        if (cached != null) {
            return cached;
        }

        List<IndexTokenPosition> positions = new ArrayList<>();
        Analyzer indexAnalyzer = getSourceAnalyzer();
        if (indexAnalyzer == null) {
            indexPositionCache.put(key, positions);
            return positions;
        }
        try (TokenStream stream = indexAnalyzer.tokenStream(field,
                new StringReader(text))) {
            TypeAttribute type = stream.addAttribute(TypeAttribute.class);
            PositionIncrementAttribute increment =
                    stream.addAttribute(PositionIncrementAttribute.class);
            PositionLengthAttribute length =
                    stream.addAttribute(PositionLengthAttribute.class);
            OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
            int position = -1;
            stream.reset();
            while (stream.incrementToken()) {
                position += increment.getPositionIncrement();
                if (!"SYNONYM".equals(type.type()) && !"ACRONYM".equals(type.type())) {
                    positions.add(new IndexTokenPosition(
                            offset.startOffset(), offset.endOffset(), position,
                            position + length.getPositionLength()));
                }
            }
            stream.end();
        }
        indexPositionCache.put(key, positions);
        return positions;
    }

    private void setSourceIndexBounds(FieldQueryNode node,
            List<IndexTokenPosition> positions, int start, int end) {
        int first = Integer.MAX_VALUE;
        int last = Integer.MIN_VALUE;
        for (IndexTokenPosition candidate : positions) {
            if (candidate.start < end && candidate.end > start) {
                first = Math.min(first, candidate.position);
                last = Math.max(last, candidate.positionEnd);
            }
        }
        if (first != Integer.MAX_VALUE) {
            node.setTag(SOURCE_INDEX_START, first);
            node.setTag(SOURCE_INDEX_END, last);
        }
    }


    private int getMultiTokenSize(String term, int positionLength, int offsetStart,
            int offsetEnd, int queryStart, String sourceText) {
        if (term == null) {
            return 0;
        }
        int separator = term.indexOf("::");
        String output = separator >= 0 ? term.substring(separator + 2) : term;
        int outputWordRuns = countWordRuns(output);
        if (outputWordRuns <= 1) {
            return positionLength;
        }
        int sourceWordRuns = 0;
        if (offsetStart >= queryStart && offsetEnd >= offsetStart) {
            int sourceStart = Math.min(offsetStart - queryStart, sourceText.length());
            int sourceEnd = Math.min(offsetEnd - queryStart, sourceText.length());
            if (sourceEnd >= sourceStart) {
                sourceWordRuns = countWordRuns(sourceText.substring(sourceStart, sourceEnd));
            }
        }
        if (sourceWordRuns >= outputWordRuns) {
            return positionLength;
        }
        return Math.max(positionLength, outputWordRuns);
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
    private boolean isRawMultiTokenAlias(String term, int positionLength,
            int offsetStart, int offsetEnd, int queryStart, String sourceText,
            String tokenType) {
        if (term == null || offsetStart < queryStart || offsetEnd < offsetStart) {
            return false;
        }
        // Acronym tokens retain their case-sensitive namespace. Their sibling
        // synonym tokens supply any genuine expanded phrase alternatives.
        if ("ACRONYM".equals(tokenType) || term.startsWith("acr::")) {
            return false;
        }
        int separator = term.indexOf("::");
        String output = separator >= 0 ? term.substring(separator + 2) : term;
        int outputWordRuns = countWordRuns(output);
        int sourceStart = Math.min(offsetStart - queryStart, sourceText.length());
        int sourceEnd = Math.min(offsetEnd - queryStart, sourceText.length());
        if (sourceEnd < sourceStart) {
            return false;
        }
        String sourceValue = sourceText.substring(sourceStart, sourceEnd);
        int sourceWordRuns = countWordRuns(sourceValue);
        if (outputWordRuns == 1) {
            return sourceWordRuns > 1
                    || (positionLength > 1 && !output.equalsIgnoreCase(sourceValue));
        }
        // A synthetic multi-word term has span width one even when its
        // source and normalized spelling occupy the same number of words.
        // Keep an index-normalized raw path for that equal-width case too.
        return sourceWordRuns > 0 && sourceWordRuns <= outputWordRuns;
    }
    private String getSourceText(int offsetStart, int offsetEnd, int queryStart,
            String sourceText) {
        if (offsetStart < queryStart || offsetEnd < offsetStart) {
            return null;
        }
        int sourceStart = Math.min(offsetStart - queryStart, sourceText.length());
        int sourceEnd = Math.min(offsetEnd - queryStart, sourceText.length());
        return sourceEnd >= sourceStart ? sourceText.substring(sourceStart, sourceEnd) : null;
    }

    private void appendOmittedSourceNodes(QueryNode phrase, QueryNode sourceNode,
            String field, String text, int queryStart) throws QueryNodeException {
        if (!(sourceNode instanceof QuotedFieldQueryNode)) {
            return;
        }

        List<QueryNode> existing = phrase.getChildren();
        List<int[]> omittedRanges = new ArrayList<>();
        int wordPosition = 0;
        int index = 0;
        while (index < text.length()) {
            while (index < text.length() && !isSourceWordChar(text.charAt(index))) {
                index++;
            }
            if (index >= text.length()) {
                break;
            }
            int start = index;
            while (index < text.length() && isSourceWordChar(text.charAt(index))) {
                index++;
            }
            int end = index;
            int absoluteStart = queryStart + start;
            int absoluteEnd = queryStart + end;
            boolean covered = false;
            for (QueryNode child : existing) {
                if (!(child instanceof FieldQueryNode)) {
                    continue;
                }
                FieldQueryNode fieldNode = (FieldQueryNode) child;
                if (fieldNode.getBegin() <= absoluteStart
                        && fieldNode.getEnd() >= absoluteEnd) {
                    covered = true;
                    break;
                }
            }
            if (!covered) {
                omittedRanges.add(new int[]{absoluteStart, absoluteEnd});
                String sourceWord = text.substring(start, end);
                for (String term : sourceTerms(field, sourceWord)) {
                    FieldQueryNode omitted = new FieldQueryNode(field, term,
                            absoluteStart, absoluteEnd);
                    omitted.setPositionIncrement(wordPosition);
                    omitted.setTag(OMITTED_SOURCE_TOKEN, true);
                    omitted.setTag(AqpPostAnalysisProcessor.RAW_POSITIONAL_PATH, true);
                    omitted.setTag(SOURCE_TEXT, sourceWord);
                    omitted.setTag(SOURCE_QUERY_TEXT, text);
                    omitted.setTag(SOURCE_QUERY_START, queryStart);
                    if (phrase instanceof MultiPhraseQueryNode) {
                        ((MultiPhraseQueryNode) phrase).add(omitted);
                    } else {
                        ((TokenizedPhraseQueryNode) phrase).add(omitted);
                    }
                }
            }
            wordPosition++;
        }
        if (!omittedRanges.isEmpty()) {
            for (QueryNode child : phrase.getChildren()) {
                child.setTag(OMITTED_SOURCE_RANGES, omittedRanges);
            }
        }
    }

    private List<String> sourceTerms(String field, String sourceWord) throws QueryNodeException {
        List<String> terms = analyzeSourceWord(field, sourceWord);
        if (terms.isEmpty()) {
            terms = analyzeSourceWord(field, sourceWord.toUpperCase(Locale.ROOT));
        }
        return terms;
    }

    private List<String> analyzeSourceWord(String field, String sourceWord) throws QueryNodeException {
        List<String> terms = new ArrayList<>();
        Analyzer sourceAnalyzer = getSourceAnalyzer();
        try (TokenStream stream = sourceAnalyzer.tokenStream(
                field, new StringReader(sourceWord))) {
            CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while (stream.incrementToken()) {
                terms.add(term.toString());
            }
            stream.end();
        } catch (IOException e) {
            throw new QueryNodeException(e);
        }
        return terms;
    }

    private Analyzer getSourceAnalyzer() {
        QueryConfigHandler config = getQueryConfigHandler();
        if (config != null) {
            AqpRequestParams request = config.get(
                    AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
            if (request != null && request.getRequest() != null
                    && request.getRequest().getSchema() != null
                    && request.getRequest().getSchema().getIndexAnalyzer() != null) {
                return request.getRequest().getSchema().getIndexAnalyzer();
            }
        }
        return analyzer;
    }

    private boolean isSourceWordChar(char ch) {
        return Character.isLetterOrDigit(ch);
    }


    /**
     * Keep identifier-like values containing punctuation together.  The
     * full-text analyzer emits both the catenated identifier and its
     * punctuation-delimited parts.  Treating an unquoted value such as
     * {@code 10.17909} as a normal Boolean query would make the parts match
     * independently, even though the input contains no whitespace.
     */
    private static boolean isPunctuationIdentifier(String text) {
        boolean hasDigit = false;
        boolean hasPunctuation = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c)) {
                return false;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetter(c)) {
                hasPunctuation = true;
            }
        }

        return hasDigit && hasPunctuation;
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {

        return children;

    }

}
