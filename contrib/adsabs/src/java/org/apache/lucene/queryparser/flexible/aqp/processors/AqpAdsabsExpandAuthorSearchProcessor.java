package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TextableQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;

/**
 * Looks at the QueryNode(s) and translates the field name if we have a mapping
 * for it. It is used to change the field names on-the-fly
 * 
 * @see AqpFieldMapper
 * @see QueryConfigHandler
 * @author rchyla
 * 
 */
public class AqpAdsabsExpandAuthorSearchProcessor extends QueryNodeProcessorImpl {

  private Map<String, int[]> fields;

  public AqpAdsabsExpandAuthorSearchProcessor() {
    // empty constructor
  }

  @Override
  public QueryNode process(QueryNode queryTree) throws QueryNodeException {
    if (getQueryConfigHandler().has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS)) {
      fields = getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS);
      return super.process(queryTree);
    }
    return queryTree;
  }

  @Override
  protected QueryNode preProcessNode(QueryNode node)
    throws QueryNodeException {
    return node;
  }

  @Override
  protected QueryNode postProcessNode(QueryNode node)
    throws QueryNodeException {
    
    if (node.getTag(AqpAdsabsAnalyzerProcessor.ORIGINAL_VALUE) != null) {
      return expandNodes(node, (String) node.getTag(AqpAdsabsAnalyzerProcessor.ORIGINAL_VALUE));
    }
    return node;
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
  throws QueryNodeException {
    return children;
  }
  
  private QueryNode expandNodes(QueryNode node, String origValue) {
    if (!node.isLeaf()) {
      ArrayList<QueryNode> pl = new ArrayList<QueryNode>();
      List<QueryNode> children = node.getChildren();
      for (QueryNode child: children) {
        doExpansion(origValue, child, pl);
      }
      children.addAll(pl);
    }
    else {
      // now expand the parent
      ArrayList<QueryNode> pl = new ArrayList<QueryNode>();
      doExpansion(origValue, node, pl);
      if (pl.size()>0) {
        pl.add(0, node);
      }
      return new GroupQueryNode(new BooleanQueryNode(pl));
    }
    return node;
  }
  
  private void doExpansion(String origValue, QueryNode node, List<QueryNode> parentChildren) {
    if (node instanceof TextableQueryNode) {
      FieldQueryNode fqn = ((FieldQueryNode) node);
      if (fields.containsKey(fqn.getFieldAsString())) {
        String v = fqn.getTextAsString();
        if (v.endsWith(",")) {
          parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), fqn.getValue() + " *", fqn.getBegin(), fqn.getEnd()));
          return;
        }
        String[] origParts = origValue.split(" ");
        boolean lastPartWasAcronym = origParts[origParts.length-1].length() == 1;
        if (lastPartWasAcronym) {
          parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), fqn.getValue() + "*", fqn.getBegin(), fqn.getEnd()));
        }
        else {
          parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), fqn.getValue() + " *", fqn.getBegin(), fqn.getEnd()));
        }
        return;
      }
    }
    expandNodes(node, origValue);
  }

}
