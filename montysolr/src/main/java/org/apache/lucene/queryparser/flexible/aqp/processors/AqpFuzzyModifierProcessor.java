package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpDisjunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFuzzyModifierNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpWhiteSpacedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.*;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Interprets {@link AqpFuzzyModifierNode} as phrase slop for quoted or wildcard nodes and as
 * {@link FuzzyQueryNode} for other field nodes. Fields configured by
 * {@code aqp.force.fuzzy.phrases} use fuzzy matching even when quoted.
 * <p>
 * For whitespace disjunctions, only quoted branches receive slop; unquoted branches remain
 * unchanged. Direct phrase and wildcard slop nodes carry {@link #EXPLICIT_SLOP}.
 */
public class AqpFuzzyModifierProcessor extends AqpQueryNodeProcessorImpl implements
        QueryNodeProcessor {
    public static final String EXPLICIT_SLOP = "aqp.explicit.slop";

    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
        if (node instanceof AqpFuzzyModifierNode) {
            QueryNode child = ((AqpFuzzyModifierNode) node).getChild();
            Float fuzzy = ((AqpFuzzyModifierNode) node).getFuzzyValue();

            if (child instanceof AqpDisjunctionQueryNode) {
                AqpDisjunctionQueryNode disjunction = (AqpDisjunctionQueryNode) child;
                List<QueryNode> sloppedChildren = new ArrayList<>(disjunction.getChildren().size());
                for (QueryNode disjunct : disjunction.getChildren()) {
                    if (!(disjunct instanceof AqpWhiteSpacedQueryNode)) {
                        throw new QueryNodeException(new MessageImpl(
                                QueryParserMessages.INVALID_SYNTAX, node
                                        + "\nUse of ~ is not allowed here"));
                    }
                    if (isQuotedWhitespaceNode((AqpWhiteSpacedQueryNode) disjunct)) {
                        sloppedChildren.add(new SlopQueryNode(disjunct, fuzzy.intValue()));
                    } else {
                        sloppedChildren.add(disjunct);
                    }
                }
                return new AqpDisjunctionQueryNode(sloppedChildren, disjunction.getTieBreaker());
            }

            QueryConfigHandler config = getQueryConfigHandler();

            boolean quotedWhitespaceNode = false;
            if (child instanceof AqpWhiteSpacedQueryNode) {
                quotedWhitespaceNode = isQuotedWhitespaceNode((AqpWhiteSpacedQueryNode) child);
            }

            if (child instanceof QuotedFieldQueryNode
                    || child instanceof WildcardQueryNode
                    || quotedWhitespaceNode) {

                if (child instanceof QuotedFieldQueryNode && hasConfigMap()) {
                    Map<String, String> configMap = getConfigMap();

                    if (configMap.containsKey("aqp.force.fuzzy.phrases")) {
                        FieldQueryNode fieldNode = (FieldQueryNode) child;

                        // if this is a phrase, but we want to treat it as a fuzzy field
                        for (String field : configMap.get("aqp.force.fuzzy.phrases").split(",")) {
                            if (field.equals(fieldNode.getFieldAsString())) {
                                return makeFuzzyNode(fieldNode, fuzzy);
                            }
                        }
                    }
                }

                if (fuzzy.intValue() < fuzzy) {

                    if (config
                            .has(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK)) {
                        AqpFeedback feedback = config
                                .get(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK);
                        feedback.sendEvent(feedback.createEvent(AqpFeedback.TYPE.WARN,
                                this.getClass(), node,
                                "For phrases and wildcard queries the float attribute " + fuzzy
                                        + " is automatically converted to: " + fuzzy.intValue()));
                    }
                }

                SlopQueryNode slopNode = new SlopQueryNode(child, fuzzy.intValue());
                slopNode.setTag(EXPLICIT_SLOP, true);
                return slopNode;
            } else if (child instanceof FieldQueryNode) {

                FieldQueryNode fieldNode = (FieldQueryNode) child;
                return makeFuzzyNode(fieldNode, fuzzy);
            } else {
                throw new QueryNodeException(new MessageImpl(
                        QueryParserMessages.INVALID_SYNTAX, node
                        + "\nUse of ~ is not allowed here"));
            }

        }
        return node;
    }

    @Override
    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }

    private boolean isQuotedWhitespaceNode(AqpWhiteSpacedQueryNode node) {
        String value = node.getTextAsString();
        return value.startsWith("\"") && value.endsWith("\"");
    }

    private QueryNode makeFuzzyNode(FieldQueryNode fieldNode, Float fuzzy) {
        return new FuzzyQueryNode(fieldNode.getFieldAsString(), fieldNode.getTextAsString(),
                fuzzy, fieldNode.getBegin(), fieldNode.getEnd());
    }

}
