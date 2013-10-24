package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.processors.AnalyzerQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MatchAllDocsQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiFieldQueryNodeProcessor;

/**
 * This processor changes field value 'null' into ''.
 * 
 * The processor solves the following problem (probably affecting SOLR only):
 * 
 * {@link MultiFieldQueryNodeProcessor} looks at {@link FieldableNode} and
 * eventually creates several instances of them (eg. "query" becomes field:query
 * fieldb:query). But this works only when the initial field==null. If it was
 * '', nothing happens. If the current configuration contains
 * {@link MultiFieldQueryNodeProcessor} but it is empty, also the field will be
 * null (because {@link DefaultFieldAttribute} is by default null)
 * 
 * {@link AnalyzerQueryNodeProcessor}, on the other hand, expects that field is
 * a string value - to be precise, it asks the current analyzer to check the
 * field. And if the field was null, then with SOLR this throws
 * {@link NullPointerException}
 * 
 * Please put {@link AqpNullDefaultFieldProcessor} also before
 * {@link MatchAllDocsQueryNodeProcessor} otherwise you will get
 * {@link NullPointerException} if the default field is null
 * 
 * @see FieldableNode
 * @see MultiFieldQueryNodeProcessor
 * @see AnalyzerQueryNodeProcessor
 * @see AqpInvenioQueryParser
 * @see DefaultFieldAttribute
 * 
 */
public class AqpNullDefaultFieldProcessor extends QueryNodeProcessorImpl
    implements QueryNodeProcessor {

	
  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    if (node instanceof FieldQueryNode
        && ((FieldQueryNode) node).getField() == null) {
    	String field = getDefaultFieldName();
    	if (field != null) {
    		((FieldQueryNode) node).setField(field);
    	}
    	else {
    		((FieldQueryNode) node).setField("");
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
  
  private String getDefaultFieldName() throws QueryNodeException {
    QueryConfigHandler queryConfig = getQueryConfigHandler();
    if (queryConfig != null) {
      if (queryConfig
          .has(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD)) {
        return queryConfig
            .get(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD);
      }
    }
    return null;
  }

}
