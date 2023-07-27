package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;

public class AqpAdsabsScoringQueryNode extends QueryNodeImpl implements QueryNode {

    private String classicSource = null;
    private float modifier = 1.0f;

    public AqpAdsabsScoringQueryNode(QueryNode nested, String classicSource, float modifier) {
        setLeaf(false);
        allocate();
        add(nested);
        this.classicSource = classicSource;
        this.modifier = modifier;
    }

    public float getModifier() {
        return this.modifier;
    }

    public String getSource() {
        return this.classicSource;
    }

    @Override
    public String toString() {
        StringBuffer bo = new StringBuffer();
        bo.append("<scoring source=" + classicSource + "\" modifier=\"" + modifier + ">\n");
        for (QueryNode child : this.getChildren()) {
            bo.append("<child>" + child.toString() + "</child>\n");
        }
        bo.append("</scoring>");
        return bo.toString();
    }

    @Override
    public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
        return "score(" + this.getChildren().get(0).toQueryString(escapeSyntaxParser)
                + ", " + classicSource + ", " + modifier + ")";
    }

}