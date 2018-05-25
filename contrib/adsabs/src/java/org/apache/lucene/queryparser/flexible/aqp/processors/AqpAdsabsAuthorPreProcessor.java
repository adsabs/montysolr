package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpConstantQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

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
 * 
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
    
    if (node instanceof FieldQueryNode ) {
      
      FieldQueryNode fqn = ((FieldQueryNode) node);
      if (fieldMap.containsKey(fqn.getFieldAsString())) {
        String field = fqn.getFieldAsString();
        String[] nameParts = fqn.getTextAsString().split(" ");
        if (node instanceof WildcardQueryNode) { 
          if (nameParts[nameParts.length-1].replace("*","").length() > 1) return node;
          // make "kurtz, m*" a simple case
          node = new FieldQueryNode(fqn.getField(), fqn.getTextAsString().replace("*", "").trim(), fqn.getBegin(), fqn.getEnd());
        }
        return node;
      }
    }
    return node;
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
