package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;

import java.util.List;
import java.util.Map;

/**
 * Looks at the QueryNode(s) and prepares them for analysis. This must happen
 * before AqpAdsabsAnalyzerProcessor
 *
 * <pre>
 * author:surname, m* =&gt; author:surname, m
 * </pre>
 *
 * @see QueryConfigHandler
 * @see AqpAdsabsAnalyzerProcessor
 * @see AqpAdsabsExpandAuthorSearchProcessor
 */
public class AqpAdsabsAuthorPreProcessor extends AqpQueryNodeProcessorImpl {

    private Map<String, int[]> fieldMap;

    public AqpAdsabsAuthorPreProcessor() {
        // empty constructor
    }

    @Override
    public QueryNode process(QueryNode queryTree) throws QueryNodeException {
        if (getQueryConfigHandler().has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS)) {
            fieldMap = getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS);
            return super.process(queryTree);
        }
        return queryTree;
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node)
            throws QueryNodeException {

        joinParenthesizedAuthorNames(node);

        if (node instanceof FieldQueryNode) {

            FieldQueryNode fqn = ((FieldQueryNode) node);
            if (fieldMap.containsKey(fqn.getFieldAsString())) {
                String field = fqn.getFieldAsString();
                String[] nameParts = fqn.getTextAsString().split(" ");
                if (node instanceof QuotedFieldQueryNode) {
                    String value = fqn.getTextAsString();
                    if (value.length() > 1
                            && value.indexOf(' ') < 0
                            && value.indexOf(',') < 0
                            && value.indexOf('?') < 0
                            && value.indexOf('^') < 0
                            && value.indexOf('*') == value.length() - 1) {
                        return new WildcardQueryNode(fqn.getField(), value,
                                fqn.getBegin(), fqn.getEnd());
                    }
                }
                if (node instanceof WildcardQueryNode) {
                    if (nameParts.length == 1
                            || nameParts[nameParts.length - 1].replace("*", "").length() > 1) {
                        return node;
                    }
                    nameParts[nameParts.length - 1] = nameParts[nameParts.length - 1].replace("*", "");
                    StringBuffer newName = new StringBuffer();
                    newName.append(nameParts[0]);
                    for (int i = 1; i < nameParts.length; i++) {
                        newName.append(" ");
                        newName.append(nameParts[i]);
                    }
                    if (newName.indexOf("*") > -1) return node; // it should still be treated as wildcard
                    node = new FieldQueryNode(fqn.getField(), newName.toString(), fqn.getBegin(), fqn.getEnd());
                }
                return node;
            }
        }
        return node;
    }

    /**
     * A parenthesized author list uses commas as name punctuation, rather than
     * as query separators.  The grammar deliberately keeps those commas as
     * delimiter nodes, so join adjacent plain author terms before analysis.
     * Quoted and wildcard terms retain their existing parser semantics.
     */
    private void joinParenthesizedAuthorNames(QueryNode node) {
        if (!(node instanceof GroupQueryNode)) {
            return;
        }
        for (QueryNode child : node.getChildren()) {
            if (child instanceof BooleanQueryNode) {
                joinAuthorBoolean((BooleanQueryNode) child);
            }
        }
    }

    private void joinAuthorBoolean(BooleanQueryNode node) {
        List<QueryNode> children = node.getChildren();
        if (children == null || children.size() < 3) {
            return;
        }
        boolean joined = false;
        for (int i = 0; i + 2 < children.size(); i++) {
            QueryNode leftNode = children.get(i);
            QueryNode delimiter = children.get(i + 1);
            QueryNode rightNode = children.get(i + 2);
            FieldQueryNode left = findPlainField(leftNode);
            FieldQueryNode right = findPlainField(rightNode);
            if (left == null || right == null
                    || !compatibleQueryWrappers(leftNode, rightNode)
                    || !isCommaDelimiter(delimiter)
                    || !isAuthorField(left)
                    || !isAuthorField(right)) {
                continue;
            }

            String joinedText = left.getTextAsString() + ", " + right.getTextAsString();
            QueryNode replacement = replaceLeaf(leftNode, left,
                    new QuotedFieldQueryNode(left.getField(), joinedText,
                            left.getBegin(), right.getEnd()));
            children.set(i, replacement);
            children.remove(i + 2);
            children.remove(i + 1);
            joined = true;
            i--;
        }
        if (joined) {
            node.set(children);
        }
    }

    private FieldQueryNode findPlainField(QueryNode node) {
        if (node.getClass() == FieldQueryNode.class) {
            return (FieldQueryNode) node;
        }
        List<QueryNode> children = node.getChildren();
        if (children != null && children.size() == 1) {
            return findPlainField(children.get(0));
        }
        return null;
    }

    private boolean compatibleQueryWrappers(QueryNode left, QueryNode right) {
        left = skipUnaryWrappers(left);
        right = skipUnaryWrappers(right);
        if (left instanceof ModifierQueryNode || right instanceof ModifierQueryNode) {
            if (!(left instanceof ModifierQueryNode) || !(right instanceof ModifierQueryNode)) {
                return false;
            }
            ModifierQueryNode leftModifier = (ModifierQueryNode) left;
            ModifierQueryNode rightModifier = (ModifierQueryNode) right;
            if (leftModifier.getModifier() == ModifierQueryNode.Modifier.MOD_NOT
                    || rightModifier.getModifier() == ModifierQueryNode.Modifier.MOD_NOT) {
                return false;
            }
            return leftModifier.getModifier() == rightModifier.getModifier()
                    && compatibleQueryWrappers(leftModifier.getChild(), rightModifier.getChild());
        }
        if (left instanceof BoostQueryNode || right instanceof BoostQueryNode) {
            if (!(left instanceof BoostQueryNode) || !(right instanceof BoostQueryNode)) {
                return false;
            }
            BoostQueryNode leftBoost = (BoostQueryNode) left;
            BoostQueryNode rightBoost = (BoostQueryNode) right;
            return Float.compare(leftBoost.getValue(), rightBoost.getValue()) == 0
                    && compatibleQueryWrappers(leftBoost.getChild(), rightBoost.getChild());
        }
        return true;
    }

    private QueryNode skipUnaryWrappers(QueryNode node) {
        while (!(node instanceof ModifierQueryNode) && !(node instanceof BoostQueryNode)) {
            List<QueryNode> children = node.getChildren();
            if (children == null || children.size() != 1) {
                break;
            }
            node = children.get(0);
        }
        return node;
    }

    private QueryNode replaceLeaf(QueryNode node, QueryNode oldLeaf, QueryNode replacement) {
        if (node == oldLeaf) {
            return replacement;
        }
        List<QueryNode> children = node.getChildren();
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) == oldLeaf) {
                    children.set(i, replacement);
                    node.set(children);
                    return node;
                }
            }
            for (QueryNode child : children) {
                QueryNode updated = replaceLeaf(child, oldLeaf, replacement);
                if (updated != child) {
                    return node;
                }
            }
        }
        return node;
    }

    private boolean isAuthorField(FieldQueryNode node) {
        return fieldMap.containsKey(node.getFieldAsString());
    }

    private boolean isCommaDelimiter(QueryNode node) {
        QueryNode terminal = node;
        while (!(terminal instanceof AqpANTLRNode)) {
            List<QueryNode> children = terminal.getChildren();
            if (children == null || children.size() != 1) {
                return false;
            }
            terminal = children.get(0);
        }
        if (!"QDELIMITER".equals(((AqpANTLRNode) terminal).getTokenName())) {
            return false;
        }
        QueryNode punctuation = AqpQProcessor.getTerminalNode(terminal);
        return punctuation instanceof AqpANTLRNode
                && ",".equals(((AqpANTLRNode) punctuation).getTokenInput());
    }

    @Override
    protected QueryNode postProcessNode(QueryNode node)
            throws QueryNodeException {
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }

}
