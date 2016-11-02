package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * TMODIFIER node contains FUZZY, BOOST, FIELD nodes. This processor changes the
 * tree from this shape:
 * 
 * <pre>
 *               TMODIFIER
 *               /   |   \
 *           BOOST FUZZY FIELD
 *             /           \
 *            ^1           ...
 * </pre>
 * 
 * To this shape:
 * 
 * <pre>
 *               BOOST
 *                / \
 *              ^1  FUZZY
 *                    |
 *                    FIELD
 *                      \
 *                     ...
 * </pre>
 * 
 * After the processor ran, the TMODIFIER node is removed and we return the
 * FUZZY node 
 * 
 * <p>
 * 
 * If TMODIFIER contains only single child, we return that child (thus remove
 * the TMODIFIER node from the tree).
 * 
 * @see AqpFUZZYProcessor
 * @see AqpBOOSTProcessor
 * @see AqpFIELDProcessor
 */
public class AqpTMODIFIERProcessor extends QueryNodeProcessorImpl implements
    QueryNodeProcessor {

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {

    if (node instanceof AqpANTLRNode
        && ((AqpANTLRNode) node).getTokenLabel().equals("TMODIFIER")) {

      List<QueryNode> children = node.getChildren();

      if (children.size() == 1) {
        return children.get(0);
      }

      QueryNode masterChild = null;
      QueryNode currentChild;
      List<QueryNode> currentChildren;

      for (int i = 0; i < children.size(); i++) {
        currentChild = children.get(i);
        if (currentChild.isLeaf()) {
          continue;
        }
        if (masterChild == null) {
          masterChild = currentChild;
          node = masterChild;
          continue;
        }
        currentChildren = masterChild.getChildren();
        currentChildren.add(currentChild);
        masterChild.set(currentChildren);
        masterChild = children.get(i);
      }

      return node;

    }
    return node;
  }

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    return node;
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
    return children;
  }

}
