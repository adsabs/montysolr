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
import org.apache.lucene.queryparser.flexible.core.nodes.MatchNoDocsQueryNode;
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
import org.apache.solr.analysis.author.AuthorCreateQueryVariationsFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                AuthorQueryParts parts = identifyLikelyAuthorQuery(subQuery, config);
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

            // Keep long unfielded phrases out of author fields to stay within the
            // synonym-expansion clause budget.
            String nonAuthorQf = unfieldedPhraseQfWithoutAuthors(node);
            if (nonAuthorQf != null) {
                if (nonAuthorQf.isEmpty()) {
                    return new MatchNoDocsQueryNode();
                }
                local.add("qf='" + escapeLocalParam(nonAuthorQf) + "'");
            }

            // let adismax know that we want exact search
            if (node.getTag("aqp.exact") != null ||
                    (node.getParent() instanceof AqpAdsabsSynonymQueryNode && !((AqpAdsabsSynonymQueryNode) node.getParent()).isActivated())) {
                local.add("aqp.exact.search=true");
            } else if (getConfigVal("aqp.maxPhraseLength", null) != null) {
                local.add("aqp.maxPhraseLength=" + getConfigVal("aqp.maxPhraseLength"));
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
        List<QueryNode> nameAlternatives = new ArrayList<QueryNode>();
        if (parts.indexedAuthor) {
            nameAlternatives.add(new QuotedFieldQueryNode("author", parts.authorName,
                    node.getBegin(), node.getEnd()));
        }
        nameAlternatives.add(new QuotedFieldQueryNode("abs", parts.fullName,
                node.getBegin(), node.getEnd()));

        List<QueryNode> clauses = new ArrayList<QueryNode>();
        clauses.add(new AqpOrQueryNode(nameAlternatives));
        for (String keyword : parts.nonAuthor.split("\\s+")) {
            if (!keyword.isEmpty()) {
                clauses.add(new FieldQueryNode("abs", keyword, node.getBegin(), node.getEnd()));
            }
        }
        return new AqpAndQueryNode(clauses);
    }

    private static AuthorQueryParts identifyLikelyAuthorQuery(String input, QueryConfigHandler config) {
        String value = input.trim();
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            value = value.substring(1, value.length() - 1).trim();
        }

        String[] parts = value.split("\\s+");
        if (parts.length < 3 || !isNameToken(parts[0]) || !isNameToken(parts[1])) {
            return null;
        }
        String authorName = parts[1] + ", " + parts[0];
        AuthorEvidence evidence = lookupAuthorEvidence(authorName, config);
        boolean indexedAuthor = evidence.indexed;
        if (!indexedAuthor && (!evidence.available
                || !isCapitalizedName(parts[0]) || !isCapitalizedName(parts[1])
                || !hasAbstractPhraseEvidence(parts[0] + " " + parts[1], config))) {
            return null;
        }

        StringBuilder nonAuthor = new StringBuilder();
        for (int i = 2; i < parts.length; i++) {
            if (nonAuthor.length() > 0) {
                nonAuthor.append(' ');
            }
            nonAuthor.append(parts[i]);
        }

        return new AuthorQueryParts(parts[0] + " " + parts[1], authorName,
                nonAuthor.toString(), indexedAuthor);
    }

    private static final class AuthorQueryParts {
        final String fullName;
        final String authorName;
        final String nonAuthor;
        final boolean indexedAuthor;

        AuthorQueryParts(String fullName, String authorName, String nonAuthor, boolean indexedAuthor) {
            this.fullName = fullName;
            this.authorName = authorName;
            this.nonAuthor = nonAuthor;
            this.indexedAuthor = indexedAuthor;
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

    private static AuthorEvidence lookupAuthorEvidence(String authorName, QueryConfigHandler config) {
        AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
        if (reqAttr == null || reqAttr.getRequest() == null) {
            return new AuthorEvidence(false, false);
        }

        SolrQueryRequest req = reqAttr.getRequest();
        Analyzer analyzer = req.getSchema().getFieldType("author").getIndexAnalyzer();
        String normalizedTerm = null;
        try (TokenStream tokens = analyzer.tokenStream("author", new StringReader(authorName))) {
            CharTermAttribute term = tokens.addAttribute(CharTermAttribute.class);
            tokens.reset();
            if (tokens.incrementToken()) {
                normalizedTerm = term.toString();
            }
            tokens.end();
        } catch (IOException e) {
            return new AuthorEvidence(false, false);
        }
        if (normalizedTerm == null) {
            return new AuthorEvidence(false, true);
        }

        try {
            Terms terms = MultiTerms.getTerms(req.getSearcher().getIndexReader(), "author");
            if (terms == null) {
                return new AuthorEvidence(false, true);
            }
            BytesRef prefix = new BytesRef(normalizedTerm);
            TermsEnum iterator = terms.iterator();
            iterator.seekCeil(prefix);
            BytesRef candidate = iterator.term();
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
    private String unfieldedPhraseQfWithoutAuthors(QueryNode node) {
        if (!(node instanceof QuotedFieldQueryNode)) {
            return null;
        }
        String phrase = ((FieldQueryNode) node).getTextAsString();
        if (countPhraseParts(phrase) <= AuthorCreateQueryVariationsFilter.MAX_NAME_PARTS) {
            return null;
        }

        String qf = getConfigVal("aqp.unfielded.queryFields", null);
        if (qf == null || qf.trim().isEmpty()) {
            return null;
        }

        Map<String, int[]> authorFields =
                getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS);
        StringBuilder filtered = new StringBuilder();
        for (String fieldBoost : qf.trim().split("\\s+")) {
            int boost = fieldBoost.indexOf('^');
            String field = boost < 0 ? fieldBoost : fieldBoost.substring(0, boost);
            if (isAuthorField(field, authorFields)) {
                continue;
            }
            if (filtered.length() > 0) {
                filtered.append(' ');
            }
            filtered.append(fieldBoost);
        }
        return filtered.toString();
    }

    private static int countPhraseParts(String phrase) {
        // Count nonempty words so whitespace formatting does not change the cutoff.
        String trimmed = phrase.trim();
        return trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
    }

    private static boolean isAuthorField(String field, Map<String, int[]> configuredAuthorFields) {
        if (configuredAuthorFields != null && configuredAuthorFields.containsKey(field)) {
            return true;
        }
        return field.equals("author") || field.startsWith("author_")
                || field.equals("first_author") || field.startsWith("first_author_")
                || field.equals("book_author") || field.startsWith("book_author_")
                || field.equals("editor") || field.startsWith("editor_");
    }

    private static String escapeLocalParam(String value) {
        return value.replace("\\", "\\\\").replace("'", "\\'");
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
