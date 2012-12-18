package org.apache.lucene.queryparser.flexible.aqp.processors;


import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

/**
 * Looks at the QueryNode(s) and translates the field name if we have a mapping
 * for it. It is used to change the field names on-the-fly. It does the same
 * thing as AqpFieldMapperProcessor
 * 
 * @see AqpFieldMapper
 * @see QueryConfigHandler
 * @author rchyla
 * 
 */
public class AqpAdsabsFieldMapperProcessorPostAnalysis extends AqpFieldMapperProcessor {


	public AqpAdsabsFieldMapperProcessorPostAnalysis() {
	  super();
	  // empty constructor
	}
	
	@Override
  public QueryNode process(QueryNode queryTree) throws QueryNodeException {
	  mapKey = AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER_POST_ANALYSIS;
    return super.process(queryTree);
  }
	
}
