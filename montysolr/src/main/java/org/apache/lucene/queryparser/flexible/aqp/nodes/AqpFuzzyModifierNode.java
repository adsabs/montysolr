package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFuzzyModifierProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeError;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;

/**
 * {@link AqpFuzzyModifierNode} is consumed by the {@link AqpFuzzyModifierProcessor}
 * which will decide what is the appropriate operation for '~' operator. E.g.
 *
 * <pre>
 *
 *    "foo bar"~5  == slop query node
 *    "foo"~5      == fuzzy search for 'foo'
 *  </pre>
 */
public class AqpFuzzyModifierNode extends QueryNodeImpl implements QueryNode {

    private static final long serialVersionUID = -3059874057254791689L;
    private final Float fuzzy;

    public AqpFuzzyModifierNode(QueryNode query, Float fuzzy) {
        if (query == null) {
            throw new QueryNodeError(new MessageImpl(
                    QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED, "query", "null"));
        }

        allocate();
        setLeaf(false);
        add(query);
        this.fuzzy = fuzzy;
    }

    public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
        if (getChild() == null)
            return "";

        String leftParenthensis = "";
        String rightParenthensis = "";

        if (getChild() != null && getChild() instanceof ModifierQueryNode) {
            leftParenthensis = "(";
            rightParenthensis = ")";
        }

        return leftParenthensis + getChild().toQueryString(escapeSyntaxParser)
                + rightParenthensis + "~" + this.fuzzy.toString();

    }

    public String toString() {
        return "<fuzzy value='" + this.fuzzy.toString() + "'>" + "\n"
                + getChild().toString() + "\n</fuzzy>";
    }

    public QueryNode getChild() {
        return getChildren().get(0);
    }

    public Float getFuzzyValue() {
        return fuzzy;
    }

}
