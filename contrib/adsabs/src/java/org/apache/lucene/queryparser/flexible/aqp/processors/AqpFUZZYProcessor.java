package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFuzzyModifierNode;
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
 *                  FUZZY
 *                  /  \
 *               ~0.1  rest
 * </pre>
 * 
 * We create a new node {@link AqpFuzzyModifierNode} (rest, 0.1) and
 * return that node.
 * 
 * <p>
 * 
 * Presence of the BOOST node child means user specified at least "^" We'll use
 * the default from the configuration.
 * 
 * @see AqpTMODIFIERProcessor
 * @see AqpFUZZYProcessor
 */
public class AqpFUZZYProcessor extends QueryNodeProcessorImpl implements
    QueryNodeProcessor {

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    if (node instanceof AqpANTLRNode
        && ((AqpANTLRNode) node).getTokenLabel().equals("FUZZY")) {

      if (node.getChildren().size() == 1) {
        return node.getChildren().get(0);
      }

      Float fuzzy = getFuzzyValue(node);

      if (fuzzy == null) {
        return node.getChildren().get(node.getChildren().size() - 1);
      }

      return new AqpFuzzyModifierNode(node.getChildren().get(
          node.getChildren().size() - 1), fuzzy);

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

  private Float getFuzzyValue(QueryNode fuzzyNode) throws QueryNodeException {
    if (fuzzyNode.getChildren() != null) {

      AqpANTLRNode child = ((AqpANTLRNode) fuzzyNode.getChildren().get(0));
      String input = child.getTokenInput();
      float fuzzy;

      if (input.equals("~")) {
        QueryConfigHandler queryConfig = getQueryConfigHandler();
        if (queryConfig == null
            || !queryConfig
                .has(AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_FUZZY)) {
          throw new QueryNodeException(
              new MessageImpl(
                  QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                  "Configuration error: "
                      + AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_FUZZY
                          .toString() + " is missing"));
        }
        fuzzy = queryConfig
            .get(AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_FUZZY);
      } else {
        fuzzy = Float.valueOf(input.replace("~", ""));
      }

      return fuzzy;

    }
    return null;
  }

}
