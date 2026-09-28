package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.queryparser.flexible.aqp.ADSEscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpWhiteSpacedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.DeletedQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.*;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import java.io.IOException;
import org.apache.lucene.queryparser.flexible.standard.processors.AnalyzerQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiFieldQueryNodeProcessor;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.MultiTerms;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.QueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.solr.request.SolrQueryRequest;

import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;

/**
 * This processor wraps fields with the 'null' value into edismax
 * search. This is the solution for the unfielded search
 *
 * @see FieldableNode
 * @see MultiFieldQueryNodeProcessor
 * @see AnalyzerQueryNodeProcessor
 */
public class AqpUnfieldedSearchProcessor extends AqpQueryNodeProcessorImpl implements
        QueryNodeProcessor {

    ADSEscapeQuerySyntaxImpl escaper = new ADSEscapeQuerySyntaxImpl();
    private static final String DROP_STOPWORD_FRAGMENT = "aqp.drop.unfielded.stopword";
    private static final String PRESERVE_STOPWORD_FRAGMENT = "aqp.preserve.unfielded.stopword";
    private static final String AUTHOR_MAX_WORD_INDEX = "aqp.unfielded.author.maxWordIndex";
    private static final int DEFAULT_AUTHOR_MAX_WORD_INDEX = 5;

    @Override
    public QueryNode process(QueryNode queryTree) throws QueryNodeException {
        QueryConfigHandler config = getQueryConfigHandler();
        String unfieldedName = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.UNFIELDED_SEARCH_FIELD);
        Analyzer analyzer = config.get(StandardQueryConfigHandler.ConfigurationKeys.ANALYZER);
        if (analyzer != null) {
            List<FieldQueryNode> unfieldedNodes = new ArrayList<>();
            collectUnfieldedNodes(queryTree, unfieldedName, unfieldedNodes);
            boolean hasMeaningfulToken = false;
            for (FieldQueryNode node : unfieldedNodes) {
                if (!hasExactAncestor(node)
                        && emitsToken(analyzer, unfieldedName, node.getTextAsString())) {
                    hasMeaningfulToken = true;
                    break;
                }
            }
            for (FieldQueryNode node : unfieldedNodes) {
                if (!hasExactAncestor(node) && isPlainAnalyzedLiteral(node)
                        && !emitsToken(analyzer, unfieldedName, node.getTextAsString())) {
                    if (hasModifierAncestor(node)) {
                        node.setTag(PRESERVE_STOPWORD_FRAGMENT, true);
                    } else if (hasMeaningfulToken) {
                        node.setTag(DROP_STOPWORD_FRAGMENT, true);
                    }
                }
            }
        }
        return super.process(queryTree);
    }


    @Override
    protected QueryNode postProcessNode(QueryNode node)
            throws QueryNodeException {

        if (node instanceof FieldQueryNode && !(node instanceof AqpAdsabsRegexQueryNode)) {

            QueryConfigHandler config = getQueryConfigHandler();

            String unfieldedName = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.UNFIELDED_SEARCH_FIELD);
            if (!((FieldQueryNode) node).getField().equals(unfieldedName)) {
                return node;
            }

            if (Boolean.TRUE.equals(node.getTag(DROP_STOPWORD_FRAGMENT))) {
                return new DeletedQueryNode();
            }
            if (!config.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)) {
                throw new QueryNodeException(new MessageImpl(
                        "Invalid configuration",
                        "Missing FunctionQueryBuilder provider"));
            }
            List<String> local = new ArrayList<String>();
            if (Boolean.TRUE.equals(node.getTag(PRESERVE_STOPWORD_FRAGMENT))) {
                local.add("aqp.preserve.unfielded.stopwords=true");
            }

            String funcName = "edismax_combined_aqp";
            String subQuery = ((FieldQueryNode) node).getTextAsString();
            if (node instanceof AqpWhiteSpacedQueryNode
                    && !(node.getParent() instanceof SlopQueryNode)) {
                AuthorQueryParts parts = identifyLikelyAuthorQuery(subQuery, config, getMaxAuthorWordIndex());
                if (parts != null) {
                    return buildAuthorQuery((AqpWhiteSpacedQueryNode) node, parts);
                }
            }

            if (node instanceof FuzzyQueryNode) {
                subQuery += "~" + ((FuzzyQueryNode) node).getSimilarity();
                // The edismax-only pass treats a floating fuzzy similarity as
                // an ordinary unfielded term. Re-run fuzzy nodes through AQP
                // after edismax selects the target fields, preserving the
                // fuzzy modifier and the analyzer's edit-distance semantics.
                funcName = "edismax_always_aqp";
            }
            if (node instanceof AqpNonAnalyzedQueryNode) {
                funcName = "edismax_nonanalyzed";
            } else {
                //subQuery = (String) escaper.escape(subQuery, Locale.getDefault(), EscapeQuerySyntax.Type.NORMAL);

                if (node instanceof QuotedFieldQueryNode) {
                    subQuery = "\"" + subQuery + "\"";
                    //local.add("sow=false");
                }
                if (node.getParent() instanceof SlopQueryNode) {
                    subQuery = subQuery + "~" + ((SlopQueryNode) node.getParent()).getValue();
                    //if (node.getParent().getParent() instanceof BoostQueryNode) {
                    //	subQuery = subQuery + "^" + ((BoostQueryNode) node.getParent().getParent()).getValue();
                    //}
                }
	      /*else if (node.getParent() instanceof BoostQueryNode) {
	      	//subQuery = subQuery + "^" + ((BoostQueryNode) node.getParent()).getValue();
	      	if (node.getParent().getParent() != null) {
	      	  QueryNode root = node.getParent().getParent();
	      	  List<QueryNode> children = root.getChildren();
	      	  children.clear();
	      	  children.add(node);
	      	  root.set(children);
	      	}
	      	
	      }*/
            }
            node.setTag("subQuery", subQuery);

            AqpFunctionQueryBuilder builder = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)
                    .getBuilder(funcName, node, config);

            if (builder == null) {
                throw new QueryNodeException(new MessageImpl(QueryParserMessages.INVALID_SYNTAX,
                        "Unknown function \"" + funcName + "\""));
            }

            // let adismax know that we want exact search
            if (node.getTag("aqp.exact") != null ||
                    (node.getParent() instanceof AqpAdsabsSynonymQueryNode && !((AqpAdsabsSynonymQueryNode) node.getParent()).isActivated())) {
                local.add("aqp.exact.search=true ");
            } else if (getConfigVal("aqp.maxPhraseLength", null) != null) {
                subQuery = "{!adismax aqp.maxPhraseLength=" + getConfigVal("aqp.maxPhraseLength") + "}" + subQuery;
            }

            List<OriginalInput> fValues = new ArrayList<OriginalInput>();
            if (local.size() > 0) {
                subQuery = "{!adismax " + String.join(" ", local).trim() + "}" + subQuery;
            }
            fValues.add(new OriginalInput(subQuery, -1, -1));
            return new AqpFunctionQueryNode(funcName, builder, fValues);
        }
        return node;
    }

    private QueryNode buildAuthorQuery(AqpWhiteSpacedQueryNode node,
                                       AuthorQueryParts parts) {
        List<QueryNode> clauses = new ArrayList<QueryNode>();
        for (AuthorName name : parts.names) {
            List<QueryNode> nameAlternatives = new ArrayList<QueryNode>();
            if (name.authorName != null) {
                nameAlternatives.add(new QuotedFieldQueryNode("author", name.authorName,
                        node.getBegin(), node.getEnd()));
            }
            nameAlternatives.add(new QuotedFieldQueryNode("abs", name.fullName,
                    node.getBegin(), node.getEnd()));
            if (!name.typedPhrase.equals(name.fullName)) {
                nameAlternatives.add(new QuotedFieldQueryNode("abs", name.typedPhrase,
                        node.getBegin(), node.getEnd()));
            }
            clauses.add(new AqpOrQueryNode(nameAlternatives));
        }
        for (String keyword : parts.keywords) {
            clauses.add(new FieldQueryNode("abs", keyword, node.getBegin(), node.getEnd()));
        }
        return new AqpAndQueryNode(clauses);
    }

    private int getMaxAuthorWordIndex() {
        String value = getConfigVal(AUTHOR_MAX_WORD_INDEX, null);
        if (value == null) {
            return DEFAULT_AUTHOR_MAX_WORD_INDEX;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return DEFAULT_AUTHOR_MAX_WORD_INDEX;
        }
    }

    private static AuthorQueryParts identifyLikelyAuthorQuery(String input, QueryConfigHandler config,
                                                              int maxWordIndex) {
        String value = input.trim();
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            value = value.substring(1, value.length() - 1).trim();
        }

        String[] parts = value.split("\\s+");
        if (parts.length < 3) {
            return null;
        }

        List<AuthorName> names = new ArrayList<AuthorName>();
        boolean[] consumed = new boolean[parts.length];
        AuthorIndex index = new AuthorIndex();
        for (int i = 0; i + 1 < parts.length && i + 1 <= maxWordIndex; i++) {
            AuthorName name = identifyName(parts, i, index, config, i == 0);
            if (name != null) {
                names.add(name);
                consumed[i] = true;
                consumed[i + 1] = true;
                i++;
            }
        }
        if (names.isEmpty()) {
            return null;
        }

        List<String> keywords = new ArrayList<String>();
        for (int i = 0; i < parts.length; i++) {
            if (!consumed[i]) {
                keywords.add(parts[i]);
            }
        }
        if (keywords.isEmpty()) {
            return null;
        }
        return new AuthorQueryParts(names, keywords);
    }

    private static AuthorName identifyName(String[] parts, int i, AuthorIndex index,
                                           QueryConfigHandler config, boolean allowAbstractFallback) {
        String first = parts[i];
        String last = parts[i + 1];
        if (!isNameToken(first) || !isNameToken(last)) {
            return null;
        }

        AuthorEvidence firstLast = index.evidence(config, last + ", " + first);
        if (firstLast.indexed) {
            String phrase = first + " " + last;
            return new AuthorName(last + ", " + first, phrase, phrase);
        }
        AuthorEvidence lastFirst = index.evidence(config, first + ", " + last);
        if (lastFirst.indexed) {
            return new AuthorName(first + ", " + last, last + " " + first, first + " " + last);
        }
        if (allowAbstractFallback && firstLast.available
                && isCapitalizedName(first) && isCapitalizedName(last)
                && hasAbstractPhraseEvidence(first + " " + last, config)) {
            String phrase = first + " " + last;
            return new AuthorName(null, phrase, phrase);
        }
        return null;
    }

    private static final class AuthorQueryParts {
        final List<AuthorName> names;
        final List<String> keywords;

        AuthorQueryParts(List<AuthorName> names, List<String> keywords) {
            this.names = names;
            this.keywords = keywords;
        }
    }

    private static final class AuthorName {
        final String authorName;
        final String fullName;
        final String typedPhrase;

        AuthorName(String authorName, String fullName, String typedPhrase) {
            this.authorName = authorName;
            this.fullName = fullName;
            this.typedPhrase = typedPhrase;
        }
    }

    private static final class AuthorEvidence {
        final boolean indexed;
        final boolean available;

        AuthorEvidence(boolean indexed, boolean available) {
            this.indexed = indexed;
            this.available = available;
        }
    }

    private static final class AuthorIndex {
        private Analyzer analyzer;
        private TermsEnum iterator;
        private boolean available;
        private boolean resolved;

        private void resolve(QueryConfigHandler config) {
            if (resolved) {
                return;
            }
            resolved = true;
            AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
            if (reqAttr == null || reqAttr.getRequest() == null) {
                return;
            }
            SolrQueryRequest req = reqAttr.getRequest();
            analyzer = req.getSchema().getFieldType("author").getIndexAnalyzer();
            try {
                Terms terms = MultiTerms.getTerms(req.getSearcher().getIndexReader(), "author");
                iterator = terms == null ? null : terms.iterator();
            } catch (IOException e) {
                return;
            }
            available = true;
        }

        AuthorEvidence evidence(QueryConfigHandler config, String authorName) {
            resolve(config);
            if (!available) {
                return new AuthorEvidence(false, false);
            }

            String normalizedTerm;
            try {
                normalizedTerm = normalize(authorName);
            } catch (IOException e) {
                return new AuthorEvidence(false, false);
            }
            if (normalizedTerm == null || iterator == null) {
                return new AuthorEvidence(false, true);
            }

            try {
                BytesRef prefix = new BytesRef(normalizedTerm);
                BytesRef candidate = iterator.seekCeil(prefix) == TermsEnum.SeekStatus.END ? null : iterator.term();
                boolean indexed = candidate != null && candidate.length >= prefix.length;
                for (int i = 0; indexed && i < prefix.length; i++) {
                    indexed = candidate.bytes[candidate.offset + i] == prefix.bytes[prefix.offset + i];
                }
                if (indexed && candidate.length > prefix.length) {
                    indexed = candidate.bytes[candidate.offset + prefix.length] == ' ';
                }
                return new AuthorEvidence(indexed, true);
            } catch (IOException e) {
                return new AuthorEvidence(false, false);
            }
        }

        private String normalize(String authorName) throws IOException {
            try (TokenStream tokens = analyzer.tokenStream("author", new StringReader(authorName))) {
                CharTermAttribute term = tokens.addAttribute(CharTermAttribute.class);
                tokens.reset();
                String normalized = null;
                if (tokens.incrementToken()) {
                    normalized = term.toString();
                }
                tokens.end();
                return normalized;
            }
        }
    }

    private static boolean hasAbstractPhraseEvidence(String fullName, QueryConfigHandler config) {
        AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
        if (reqAttr == null || reqAttr.getRequest() == null) {
            return false;
        }

        SolrQueryRequest req = reqAttr.getRequest();
        Analyzer analyzer = req.getSchema().getFieldType("abstract").getIndexAnalyzer();
        Query phrase = new QueryBuilder(analyzer).createPhraseQuery("abstract", fullName);
        if (phrase == null) {
            return false;
        }
        try {
            return req.getSearcher().search(phrase, 1).totalHits.value > 0;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean isNameToken(String value) {
        if (value.length() < 2) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isLetter(c) && c != '-' && c != '\'') {
                return false;
            }
        }
        return true;
    }

    private static boolean isCapitalizedName(String value) {
        if (!Character.isUpperCase(value.charAt(0))) {
            return false;
        }
        for (int i = 1; i < value.length(); i++) {
            if (Character.isUpperCase(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean hasExactAncestor(QueryNode node) {
        QueryNode parent = node.getParent();
        while (parent != null) {
            if (parent instanceof AqpAdsabsSynonymQueryNode
                    && !((AqpAdsabsSynonymQueryNode) parent).isActivated()) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    private boolean hasModifierAncestor(QueryNode node) {
        QueryNode parent = node.getParent();
        while (parent != null) {
            if (parent instanceof ModifierQueryNode
                    && Boolean.TRUE.equals(parent.getTag(AqpMODIFIERProcessor.EXPLICIT_MODIFIER_TAG))) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    private boolean isPlainAnalyzedLiteral(FieldQueryNode node) {
        return !(node instanceof AqpNonAnalyzedQueryNode
                || node instanceof QuotedFieldQueryNode
                || node instanceof FuzzyQueryNode
                || node instanceof WildcardQueryNode
                || node.getParent() instanceof RangeQueryNode);
    }

    private void collectUnfieldedNodes(QueryNode node, String unfieldedName,
                                       List<FieldQueryNode> result) {
        if (node instanceof FieldQueryNode
                && unfieldedName.equals(((FieldQueryNode) node).getFieldAsString())
                && isPlainAnalyzedLiteral((FieldQueryNode) node)) {
            result.add((FieldQueryNode) node);
        }
        if (node.getChildren() != null) {
            for (QueryNode child : node.getChildren()) {
                collectUnfieldedNodes(child, unfieldedName, result);
            }
        }
    }

    private boolean emitsToken(Analyzer analyzer, String field, String text) {
        try (TokenStream stream = analyzer.tokenStream(field, text)) {
            stream.reset();
            boolean emits = stream.incrementToken();
            stream.end();
            return emits;
        } catch (IOException e) {
            throw new RuntimeException("Unable to analyze unfielded query fragment", e);
        }
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node)
            throws QueryNodeException {
        if (node instanceof AqpAdsabsSynonymQueryNode) {
            applyTagToAllChildren(((AqpAdsabsSynonymQueryNode) node).getChild());
        }
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }

    private void applyTagToAllChildren(QueryNode node) {

        if (node instanceof FieldQueryNode) {
            node.setTag("aqp.exact", true);
        }
        if (node.getChildren() != null) {
            for (QueryNode child : node.getChildren()) {
                applyTagToAllChildren(child);
            }
        }
    }

}
