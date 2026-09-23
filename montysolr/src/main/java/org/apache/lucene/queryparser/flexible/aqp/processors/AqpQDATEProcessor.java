package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
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
        String explicitField = getExplicitField(node);
        boolean yearRange = isYearRange(input);
        boolean unfieldedYearRange = yearRange && explicitField == null;
        boolean yearFieldRange = yearRange && (unfieldedYearRange || "year".equals(explicitField));
        String rangeField = yearFieldRange ? "year" : (explicitField == null ? dateField : explicitField);
        int start_point = subChild.getTokenStart();

        String lower = null;
        String upper = null;
        int lower_start = 0;
        int lower_end = 0;
        int upper_start = 0;
        int upper_end = 0;

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
        if (unfieldedYearRange && expandYears) {
            // Unfielded year ranges can match records indexed with either date or year values.
            String dateLower = expandLowerYear(lower);
            String dateUpper = expandUpperYear(upper);
            TermRangeQueryNode yearRangeNode = createRangeNode(
                    "year", lower, upper, lower_start, lower_end, upper_start, upper_end, true);
            TermRangeQueryNode dateRangeNode = createRangeNode(
                    dateField, dateLower, dateUpper, lower_start, lower_end, upper_start, upper_end,
                    dateUpper.equals(upper));
            return new AqpOrQueryNode(List.of(yearRangeNode, dateRangeNode));
        }

        boolean upperExclusive = false;
        if (!yearFieldRange && expandYears) {
            lower = expandLowerYear(lower);
            String expandedUpper = expandUpperYear(upper);
            upperExclusive = !expandedUpper.equals(upper);
            upper = expandedUpper;
        }

        return createRangeNode(
                rangeField, lower, upper, lower_start, lower_end, upper_start, upper_end, !upperExclusive);
    }

    private TermRangeQueryNode createRangeNode(String field, String lower, String upper,
                                               int lowerStart, int lowerEnd, int upperStart, int upperEnd,
                                               boolean upperInclusive) throws QueryNodeException {
        try {
            FieldQueryNode lowerBound = new FieldQueryNode(field,
                    EscapeQuerySyntaxImpl.discardEscapeChar(lower), lowerStart, lowerEnd);
            FieldQueryNode upperBound = new FieldQueryNode(field,
                    EscapeQuerySyntaxImpl.discardEscapeChar(upper), upperStart, upperEnd);
            return new TermRangeQueryNode(lowerBound, upperBound, true, upperInclusive);
        } catch (org.apache.lucene.queryparser.flexible.standard.parser.ParseException e) {
            throw new QueryNodeException(new MessageImpl(e.getMessage()));
        }
    }

    private boolean isYearRange(String input) {
        if (input == null || input.length() != 9 || input.charAt(4) != '-') {
            return false;
        }
        for (int i = 0; i < input.length(); i++) {
            if (i == 4) {
                continue;
            }
            char c = input.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private String getExplicitField(AqpANTLRNode node) {
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
        return explicitField;
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
        String explicitField = getExplicitField(node);
        return explicitField == null || dateField.equals(explicitField);
    }
}
