package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsIdentifierNode;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpUtils;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;

/**
 * Converts identifier syntax into identifier or wildcard nodes, reusing existing
 * field nodes when available. Grouped identifier values receive the identifier
 * field recursively.
 * <p>
 * Uses the configured default identifier field unless the syntax supplies one.
 * {@link AqpQPHRASEProcessor} runs first to convert quoted identifier values.
 *
 * @see QueryConfigHandler
 */
public class AqpQIDENTIFIERProcessor extends AqpQProcessor {

    public boolean nodeIsWanted(AqpANTLRNode node) {
        return node.getTokenLabel().equals("QIDENTIFIER");
    }

    public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
        String field = getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DEFAULT_IDENTIFIER_FIELD);
        String input = null;
        int start = 0;
        int end = 0;

        QueryNode valueNode = null;
        if (node.getChildren().size() == 2
                && node.getChildren().get(1) instanceof GroupQueryNode) {
            String identifierField = ((AqpANTLRNode) node.getChildren().get(0)).getTokenLabel();
            QueryNode groupedValue = node.getChildren().get(1);
            AqpUtils.applyFieldToAllChildren(identifierField, groupedValue);
            return groupedValue;
        }

        if (node.getChildren().size() == 1) {
            valueNode = node.getChildren().get(0);
        } else {
            field = ((AqpANTLRNode) node.getChildren().get(0)).getTokenLabel();
            valueNode = node.getChildren().get(1);
        }

        if (valueNode instanceof AqpANTLRNode) {
            input = EscapeQuerySyntaxImpl.discardEscapeChar(((AqpANTLRNode) valueNode).getTokenInput()).toString();
            start = ((AqpANTLRNode) valueNode).getTokenStart();
            end = ((AqpANTLRNode) valueNode).getTokenEnd();
        } else if (valueNode instanceof MatchAllDocsQueryNode) {
            // Token positions are unavailable after match-all conversion.
            return new PrefixWildcardQueryNode(field, "*", -1, -1);
        } else {
            input = ((FieldQueryNode) valueNode).getTextAsString();
            start = ((FieldQueryNode) valueNode).getBegin();
            end = ((FieldQueryNode) valueNode).getEnd();
        }

        if (valueNode instanceof FieldQueryNode) {
            ((FieldQueryNode) valueNode).setField(field);
            ((FieldQueryNode) valueNode).setText(input);
            ((FieldQueryNode) valueNode).setBegin(start);
            ((FieldQueryNode) valueNode).setEnd(end);
            return valueNode;
        } else {
            if (input.equals("*")) {
                return new PrefixWildcardQueryNode(field, input, start, end);
            }
            return new AqpAdsabsIdentifierNode(field, input, start, end);
        }

    }

}
