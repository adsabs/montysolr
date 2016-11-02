package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;

/**
 * Sets the node into the BoostQueryNode, this processor requires that
 * {@link AqpTMODIFIERProcessor} ran before. Because we depend on the proper
 * tree shape.
 * 
 * <p>
 * 
 * If BOOST node contains only one child, we return that child and do nothing.
 * 
 * <p>
 * 
 * If BOOST node contains two children, we take the first and check its input,
 * eg.
 * 
 * <pre>
 * 					BOOST
 *                  /  \
 *               ^0.1  rest
 * </pre>
 * 
 * We create a new node BoostQueryNode(rest, 0.1) and return that node.
 * 
 * <p>
 * 
 * Presence of the BOOST node child means user specified at least "^" We'll use
 * the default from the configuration
 * 
 * @see AqpTMODIFIERProcessor
 * @see AqpFUZZYProcessor
 */
public class AqpBOOSTProcessor extends QueryNodeProcessorImpl implements
    QueryNodeProcessor {

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    if (node instanceof AqpANTLRNode
        && ((AqpANTLRNode) node).getTokenLabel().equals("BOOST")) {

      if (node.getChildren().size() == 1) {
        return node.getChildren().get(0);
      }

      Float boost = getBoostValue(node);
      if (boost == null) {
        return node.getChildren().get(node.getChildren().size() - 1);
      }
      return new BoostQueryNode(node.getChildren().get(
          node.getChildren().size() - 1), boost);

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

  private Float getBoostValue(QueryNode boostNode) throws QueryNodeException {
    if (boostNode.getChildren() != null) {

      AqpANTLRNode child = ((AqpANTLRNode) boostNode.getChildren().get(0));
      String input = child.getTokenInput();
      float boost;

      if (input.equals("^")) {
        QueryConfigHandler queryConfig = getQueryConfigHandler();
        if (queryConfig == null
            || !queryConfig
                .has(AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_BOOST)) {
          throw new QueryNodeException(new MessageImpl(
              QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
              "Configuration error: IMPLICIT_BOOST value is missing"));
        }
        boost = queryConfig
            .get(AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_BOOST);
      } else {
        boost = Float.valueOf(input.replace("^", ""));
      }

      return boost;

    }
    return null;
  }

}
