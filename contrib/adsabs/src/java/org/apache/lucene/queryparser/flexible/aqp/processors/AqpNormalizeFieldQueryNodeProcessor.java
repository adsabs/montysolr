package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.RangeQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;


public class AqpNormalizeFieldQueryNodeProcessor extends
QueryNodeProcessorImpl {

	
	@Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
	  if (node instanceof AqpNonAnalyzedQueryNode) {
	    return node;
	  }
	  if (node instanceof WildcardQueryNode
        || node instanceof FuzzyQueryNode
        || (node instanceof FieldQueryNode && node.getParent() instanceof RangeQueryNode)
        || node instanceof RegexpQueryNode) {
	    
	    
	    FieldQueryNode txtNode = (FieldQueryNode) node;
      CharSequence text = txtNode.getText();

      Analyzer analyzer = getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);
      if (analyzer != null) {
        text = analyzer.normalize(txtNode.getFieldAsString(), text.toString()).utf8ToString();
        txtNode.setText(text);
      }      
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
