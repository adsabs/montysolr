package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.queryparser.flexible.aqp.ADSEscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
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

            String funcName = "edismax_combined_aqp"; //"edismax_always_aqp"; //"edismax_combined_aqp";
            String subQuery = ((FieldQueryNode) node).getTextAsString();

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
