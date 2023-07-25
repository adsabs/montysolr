package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFuzzyModifierNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.SlowFuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;

/**
 * Rewrites the query node which is below the {@link AqpFuzzyModifierNode}
 * 
 * The actions are: <p> 
 * 
 * 
 * {@link FieldQueryNode} <p> 
 *    - query is turned into {@link FuzzyQueryNode} or {@link SlowFuzzyQueryNode} <p>
 *      depending the configuration of {@link AqpStandardQueryConfigHandler}.ConfigurationKeys.ALLOW_SLOW_FUZZY
 *    - invalid syntax is raised if not 0.0 &gt; fuzzy &lt; 1.0  <p>
 *  
 * {@link WildcardQueryNode}, {@link QuotedFieldQueryNode} <p>
 *   - becomes {@link SlopQueryNode} <p>
 * 
 * 
 * {@link QuotedFieldQueryNode} <p>
 *   - wrapped with {@link SlopQueryNode}
 * 
 * 
 */
public class AqpFuzzyModifierProcessor extends AqpQueryNodeProcessorImpl implements
    QueryNodeProcessor {

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    if (node instanceof AqpFuzzyModifierNode) {
      QueryNode child = ((AqpFuzzyModifierNode) node).getChild();
      Float fuzzy = ((AqpFuzzyModifierNode) node).getFuzzyValue();

      QueryConfigHandler config = getQueryConfigHandler();

      if (child instanceof QuotedFieldQueryNode
          || child instanceof WildcardQueryNode) {
        
        if (child instanceof QuotedFieldQueryNode && hasConfigMap()) {
          Map<String, String> c = getConfigMap();
          
          if (c.containsKey("aqp.force.fuzzy.phrases")) {
            FieldQueryNode fn = (FieldQueryNode) child;
           
            // if this is a phrase, but we want to treat it as a fuzzy field
            for (String f: c.get("aqp.force.fuzzy.phrases").split(",")) {
              if (f.equals(fn.getFieldAsString())) {
                return makeFuzzyNode(config, fn, fuzzy);
              }
            }
          }
        }
        
        if (fuzzy.intValue() < fuzzy) {

          if (config
              .has(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK)) {
            AqpFeedback feedback = config
                .get(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK);
            feedback.sendEvent(feedback.createEvent(AqpFeedback.TYPE.WARN,
                this.getClass(), node,
                "For phrases and wildcard queries the float attribute " + fuzzy
                    + " is automatically converted to: " + fuzzy.intValue()));
          }
        }
        return new SlopQueryNode(child, fuzzy.intValue());
      } else if (child instanceof FieldQueryNode) {

        FieldQueryNode fn = (FieldQueryNode) child;
        return makeFuzzyNode(config, fn, fuzzy);
        

      } else {
        throw new QueryNodeException(new MessageImpl(
            QueryParserMessages.INVALID_SYNTAX, node.toString()
                + "\nUse of ~ is not allowed here"));
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
  
  private QueryNode makeFuzzyNode(QueryConfigHandler config, FieldQueryNode fn, Float fuzzy) {
    return new FuzzyQueryNode(fn.getFieldAsString(), fn.getTextAsString(),
        fuzzy, fn.getBegin(), fn.getEnd());
  }

}
