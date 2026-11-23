package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;

import java.util.List;

/** Recognizes mixed-width year ranges before grouping or analysis erases the original syntax. */
public class AqpShortYearRangeProcessor extends QueryNodeProcessorImpl {
    @Override
    public QueryNode process(QueryNode tree) throws QueryNodeException {
        if (Boolean.TRUE.equals(getQueryConfigHandler().get(ConfigurationKeys.GENERATED_FIELD_QUERY))) {
            return tree;
        }
        return super.process(tree);
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
        if (!(node instanceof AqpANTLRNode syntax) || syntax.isLeaf()) {
            return node;
        }
        boolean quoted = "QPHRASE".equals(syntax.getTokenLabel());
        if (!quoted && !"QNORMAL".equals(syntax.getTokenLabel())) {
            return node;
        }
        AqpANTLRNode token = (AqpANTLRNode) syntax.getChildren().get(0);
        String text = token.getTokenInput();
        if (text == null) {
            return node;
        }
        int start = quoted ? 1 : 0;
        int end = text.length() - start;
        int separator = text.indexOf('-');
        int lowerLength = separator - start;
        int upperLength = end - separator - 1;
        if (!((lowerLength >= 1 && lowerLength <= 3 && upperLength == 4)
                || (lowerLength == 4 && upperLength >= 1 && upperLength <= 3))) {
            return node;
        }
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);
            if (i != separator && (c < '0' || c > '9')) {
                return node;
            }
        }
        if (!isExplicitYear(node)) {
            return node;
        }

        String lower = text.substring(start, separator);
        String upper = text.substring(separator + 1, end);
        if (lowerLength < 4) {
            lower = "0000".substring(lowerLength) + lower;
        } else {
            // An abbreviated end supplies the trailing digits of the starting year.
            upper = lower.substring(0, 4 - upperLength) + upper;
        }
        if (upper.compareTo(lower) < 0) {
            throw new QueryNodeException(new MessageImpl("Year range end precedes start in year:"
                    + text + "; specify both years with four digits."));
        }
        int tokenStart = token.getTokenStart();
        FieldQueryNode lowerBound = new FieldQueryNode("year", lower, tokenStart + start, tokenStart + separator);
        FieldQueryNode upperBound = new FieldQueryNode("year", upper, tokenStart + separator + 1, tokenStart + end);
        return new TermRangeQueryNode(lowerBound, upperBound, true, true);
    }

    private boolean isExplicitYear(QueryNode node) {
        String field = null;
        for (QueryNode parent = node.getParent(); parent instanceof AqpANTLRNode syntax; parent = parent.getParent()) {
            switch (syntax.getTokenLabel()) {
                case "QRANGEIN", "QRANGEEX", "QIDENTIFIER" -> {
                    return false;
                }
                case "TMODIFIER" -> {
                    for (QueryNode child : syntax.getChildren()) {
                        if (child instanceof AqpANTLRNode modifier && "FUZZY".equals(modifier.getTokenLabel())
                                && !modifier.isLeaf()) {
                            return false;
                        }
                    }
                }
                case "FIELD" -> {
                    List<QueryNode> children = syntax.getChildren();
                    if (children.size() == 2) {
                        // Like AqpFIELDProcessor, let an enclosing field override nested fields.
                        field = ((AqpANTLRNode) children.get(0)).getTokenInput();
                    }
                }
            }
        }
        return "year".equals(field);
    }

    @Override
    protected QueryNode postProcessNode(QueryNode node) {
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children) {
        return children;
    }
}
