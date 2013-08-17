package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.ValueSourceParser;


/**
 * Returns a special FunctionQParser provider for the functions implemented
 * by Solr functions: http://wiki.apache.org/solr/FunctionQuery
 * 
 */
public class AqpSolrFunctionProvider implements AqpFunctionQueryBuilderProvider {

	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
		throws QueryNodeException {
		
		AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
		
		SolrQueryRequest req = reqAttr.getRequest();
		if (req == null)
			return null;
		
		ValueSourceParser provider = req.getCore().getValueSourceParser(funcName);
		
		if (provider == null)
			return null;
		
			
		AqpFunctionQParser fParser = new AqpFunctionQParser(null, reqAttr.getLocalParams(), 
				reqAttr.getParams(), req);
		
		//AqpFunctionQueryTreeBuilder.flattenChildren(node);
		//AqpFunctionQueryTreeBuilder.simplifyValueNode(node);
		
		return new AqpFunctionQueryTreeBuilder(provider, fParser);
		
	}

}
