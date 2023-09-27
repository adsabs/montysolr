package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;

import java.util.List;

/*
 * Processor which massages the AST tree before other processors work
 * with it.
 * 	- replaces chain of OPERATORs with the lowest ie. (AND (AND (AND..)))
 *    becomes (AND ...); this happens only if the OPERATOR has one
 *    single child of type: OPERATOR, ATOM, CLAUSE
 *
 *    Useful mostly for the DEFOP operator as our ANTLR grammars
 *    usually group same clauses under one operator
 *
 *  -
 */
public class AqpTreeRewriteProcessor extends QueryNodeProcessorImpl {

    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {

        if (node instanceof AqpANTLRNode && node.getChildren() != null) {
            List<QueryNode> children = node.getChildren();
            AqpANTLRNode n = (AqpANTLRNode) node;
            AqpANTLRNode child;

            // turn (AND (AND (CLAUSE...))) into (AND (CLAUSE...))
            // also (AND (ATOM ....)) into (ATOM...)
            if (n.getTokenName().equals("OPERATOR") && children.size() == 1) {
                child = (AqpANTLRNode) children.get(0);
                if (child.getTokenName().equals("OPERATOR")
                        || child.getTokenName().equals("ATOM")
                        || child.getTokenName().equals("CLAUSE")) {
                    return child;
                }
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

}
