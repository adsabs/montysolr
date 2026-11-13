package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpBooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simplifies compatible boolean clauses and redundant root modifiers.
 * A lone prohibited root clause receives a match-all anchor.
 */
public class AqpOptimizationProcessor extends QueryNodeProcessorImpl implements
        QueryNodeProcessor {

    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
        if (node.getParent() == null && node.getChildren() != null
                && node.getChildren().size() == 1) {
            if (node instanceof BooleanQueryNode) {
                QueryNode onlyChild = node.getChildren().get(0);
                if (onlyChild instanceof ModifierQueryNode
                        && ((ModifierQueryNode) onlyChild).getModifier() == Modifier.MOD_NOT) {
                    return addMatchAllAnchor(onlyChild);
                }
                if (onlyChild instanceof ModifierQueryNode) {
                    return ((ModifierQueryNode) onlyChild).getChild();
                }
            } else if (node instanceof ModifierQueryNode
                    && ((ModifierQueryNode) node).getModifier() == Modifier.MOD_NOT) {
                return addMatchAllAnchor(node);
            }
        } else if (node instanceof AqpBooleanQueryNode &&
                (node.getTag(AqpQueryTreeBuilder.SYNONYMS) == null ||
                        !((boolean) node.getTag(AqpQueryTreeBuilder.SYNONYMS)))) {

            List<QueryNode> children = node.getChildren();
            String operator = ((AqpBooleanQueryNode) node).getOperator();
            boolean rewriteSafe = true;

            QueryNode subClause;
            QueryNode child;
            int flattenedClauseCount = 0;

            for (int i = 0; i < children.size(); i++) {
                child = children.get(i);
                if (child.isLeaf()) {
                    rewriteSafe = false;
                    break;
                }

                subClause = unwrapCompatibleModifiers(child);
                if (subClause instanceof FieldableNode) {
                    flattenedClauseCount++;
                } else if (subClause instanceof AqpBooleanQueryNode && ((AqpBooleanQueryNode) subClause)
                        .getOperator().equals(operator)) {
                    flattenedClauseCount = flattenedClauseCount + subClause.getChildren().size() + 1;
                } else {
                    rewriteSafe = false;
                    break;
                }
            }

            if (rewriteSafe && flattenedClauseCount > children.size()) {
                List<QueryNode> flattenedChildren = new ArrayList<QueryNode>();

                for (QueryNode originalChild : children) {
                    subClause = unwrapCompatibleModifiers(originalChild);
                    if (subClause instanceof AqpBooleanQueryNode) {
                        for (QueryNode nestedChild : subClause.getChildren()) {
                            flattenedChildren.add(nestedChild);
                        }
                    } else {
                        flattenedChildren.add(originalChild);
                    }
                }

                children.clear();
                node.set(flattenedChildren);
            }
        }

        return node;
    }

    private QueryNode addMatchAllAnchor(QueryNode prohibitedClause) {
        List<QueryNode> clauses = new ArrayList<QueryNode>(2);
        clauses.add(new MatchAllDocsQueryNode());
        clauses.add(prohibitedClause);
        return new AqpAndQueryNode(clauses);
    }

    private QueryNode unwrapCompatibleModifiers(QueryNode node) {
        Modifier outerModifier = null;
        if (node instanceof ModifierQueryNode) {
            outerModifier = ((ModifierQueryNode) node).getModifier();
        } else {
            return node;
        }

        QueryNode unwrappedNode = node;
        while (unwrappedNode instanceof ModifierQueryNode) {
            if (unwrappedNode.getChildren().size() != 1)
                break;
            QueryNode wrappedChild = unwrappedNode.getChildren().get(0);
            if (wrappedChild.getChildren() == null)
                break;

            boolean safe = true;
            for (QueryNode child : wrappedChild.getChildren()) {
                if (child instanceof ModifierQueryNode
                        && ((ModifierQueryNode) child).getModifier().equals(outerModifier)) {
                    // pass
                } else {
                    safe = false;
                    break;
                }
            }
            if (safe) {
                unwrappedNode = wrappedChild;
            } else {
                break;
            }
        }
        return unwrappedNode;
    }

    @Override
    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
        if (node instanceof AqpBooleanQueryNode) {
            Set<String> seen = new HashSet<String>();
            List<QueryNode> children = node.getChildren();
            int i = 0;
            boolean changed = false;
            while (i < children.size()) {
                if (seen.contains(children.get(i).toString())) {
                    children.remove(i);
                    changed = true;
                } else {
                    seen.add(children.get(i).toString());
                    i += 1;
                }
            }
            if (changed)
                node.set(children);
        }
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }

}
