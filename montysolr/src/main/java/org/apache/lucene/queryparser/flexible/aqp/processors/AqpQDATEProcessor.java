package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;

import java.util.List;

public class AqpQDATEProcessor extends AqpQProcessorPost {

    public boolean nodeIsWanted(AqpANTLRNode node) {
        return node.getTokenLabel().equals("QDATE");
    }

    public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {

        QueryConfigHandler queryConfig = getQueryConfigHandler();

        if (!queryConfig.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DEFAULT_DATE_RANGE_FIELD)) {
            throw new QueryNodeException(new MessageImpl(
                    "Configuration error",
                    "DefaultDateRangeField is missing from configuration"));
        }

        String dateField = queryConfig.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DEFAULT_DATE_RANGE_FIELD);
        if (dateField == null) {
            throw new QueryNodeException(new MessageImpl(
                    "Configuration error",
                    "DefaultDateRangeField is not set"));
        }


        AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
        String input = subChild.getTokenInput();
        int start_point = subChild.getTokenStart();

        String lower = null;
        String upper = null;
        int lower_start = 0;
        int lower_end = 0;
        int upper_start = 0;
        int upper_end = 0;
        boolean upperExclusive = false;

        if (input.startsWith("-") || input.endsWith("-")) {
            AqpFeedback feedback = getFeedbackAttr();
            if (input.startsWith("-")) {
                lower = "*";
                upper = input.substring(1);

                lower_start = start_point + input.indexOf(upper) - 2;
                lower_end = lower_start + 1;

                upper_start = start_point + input.indexOf(upper);
                upper_end = upper_start + upper.length();

            } else {
                lower = input.substring(0, input.length() - 1);
                upper = "*";

                lower_start = start_point + input.indexOf(lower);
                lower_end = lower_start + lower.length();

                upper_start = lower_end + 1;
                upper_end = upper_start + 1;
            }
            feedback.createEvent(AqpFeedback.TYPE.DEPRECATED, this.getClass(), subChild,
                    "The query syntax \"" + input + "\" is deprecated. Please use:" +
                            "{{{" + dateField + ":[" + lower + " TO " + upper + "]}}}");

        } else {
            String[] parts = input.split("-");
            lower = parts[0];
            lower_start = start_point + input.indexOf(lower);
            lower_end = lower_start + lower.length();

            upper = parts[1];
            upper_start = start_point + input.indexOf(upper);
            upper_end = upper_start + upper.length();

        }
        boolean expandYears = Boolean.TRUE.equals(queryConfig.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_READY))
                && isDateRangeField(node, dateField);
        if (expandYears) {
            String expandedLower = expandLowerYear(lower);
            lower = expandedLower;
            String expandedUpper = expandUpperYear(upper);
            upperExclusive = !expandedUpper.equals(upper);
            upper = expandedUpper;
        }

        FieldQueryNode lowerBound = new FieldQueryNode(dateField,
                EscapeQuerySyntaxImpl.discardEscapeChar(lower),
                lower_start, lower_end);
        FieldQueryNode upperBound = new FieldQueryNode(dateField,
                EscapeQuerySyntaxImpl.discardEscapeChar(upper),
                upper_start, upper_end);

        return new TermRangeQueryNode(lowerBound, upperBound, true, !upperExclusive);
    }

    private String expandLowerYear(String value) {
        if (value.length() == 4 && isDigits(value, 0, 4)) {
            return value + "-01-01T00:00:00Z";
        }
        return value;
    }

    private String expandUpperYear(String value) {
        if (value.length() == 4 && isDigits(value, 0, 4)) {
            return value + "-01-01T00:00:00Z+1YEAR";
        }
        return value;
    }

    private boolean isDigits(String value, int start, int end) {
        for (int i = start; i < end; i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isDateRangeField(AqpANTLRNode node, String dateField) {
        String explicitField = null;
        QueryNode ancestor = node.getParent();
        while (ancestor != null) {
            if (ancestor instanceof AqpANTLRNode && ((AqpANTLRNode) ancestor).getTokenLabel().equals("FIELD")) {
                AqpANTLRNode fieldNode = (AqpANTLRNode) ancestor;
                List<QueryNode> children = fieldNode.getChildren();
                if (children != null && children.size() > 1 && children.get(0) instanceof AqpANTLRNode fieldName) {
                    explicitField = fieldName.getTokenInput();
                }
            }
            ancestor = ancestor.getParent();
        }
        return explicitField == null || dateField.equals(explicitField);
    }
}
