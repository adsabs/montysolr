package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * Finds the {@link AqpANTLRNode} with tokenLabel
 * 
 * <pre>
 * DEFOP
 * </pre>
 * 
 * and sets their @{code tokenInput} to be the name of the default operator.
 * 
 * If there is only one child, the child is returned and we remove the operator.
 * This happens mainly for simple queries such as
 * 
 * <pre>
 * field:value
 * </pre>
 * 
 * But also for queries which are itself clauses, like:
 * 
 * <pre>
 * +(this that)
 * </pre>
 * 
 * which produces:
 * 
 * <pre>
 *            DEFOP
 *              |
 *           MODIFIER
 *            /   \
 *               TMODIFIER
 *                  |
 *                CLAUSE
 *                  | 
 *                DEFOP
 *                /   \
 *          MODIFIER MODIFIER   
 *             |        |
 * </pre>
 * 
 * 
 * @see DefaultOperatorAttribute
 * @see AqpQueryParser#setDefaultOperator(org.apache.lucene.queryparser.flexible.standard.config.DefaultOperatorAttribute.Operator)
 * 
 */
public class AqpDEFOPProcessor extends QueryNodeProcessorImpl implements
    QueryNodeProcessor {

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {

    if (node instanceof AqpANTLRNode
        && ((AqpANTLRNode) node).getTokenLabel().equals("DEFOP")) {

      // only one child, we'll simplify the tree
      if (node.getChildren().size() == 1) {
        QueryNode child = node.getChildren().get(0);
        while (!child.isLeaf() && child.getChildren().size() == 1 
        		&& child instanceof AqpANTLRNode
            && ((AqpANTLRNode) child).getTokenLabel().equals("DEFOP")) {
          child = child.getChildren().get(0);
        }
        return child;
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

  protected StandardQueryConfigHandler.Operator getDefaultOperator()
      throws QueryNodeException {
    QueryConfigHandler queryConfig = getQueryConfigHandler();

    if (queryConfig != null) {

      if (queryConfig
          .has(StandardQueryConfigHandler.ConfigurationKeys.DEFAULT_OPERATOR)) {
        return queryConfig
            .get(StandardQueryConfigHandler.ConfigurationKeys.DEFAULT_OPERATOR);
      }
    }
    throw new QueryNodeException(new MessageImpl(
        QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
        "Configuration error: "
            + StandardQueryConfigHandler.ConfigurationKeys.class.toString()
            + " is missing"));
  }

}
