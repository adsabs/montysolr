package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParser;
import org.apache.lucene.search.Query;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.QParser;

public class AqpAdsabsCustomQueryProvider implements
		AqpFunctionQueryBuilderProvider {
	
	public static Map<String, AqpSubqueryParser> parsers = new HashMap<String, AqpSubqueryParser>();
	
	static { // a workaround for pos(author,...) -- =before i fix the PositionSearchQuery
		parsers.put("pos", new AqpSubqueryParser() {
	      @Override
	      public Query parse(FunctionQParser fp) throws ParseException {
	      	String field = fp.parseId();
	      	int start = fp.parseInt();
	      	int end = fp.parseInt();
	      	String value = fp.parseId();
	      	
	        
	        if (!value.contains("\"")) {
	          value = '"' + value + '"';
	        }
	        QParser ep = fp.subQuery(String.format("first_author:%s", value), "aqp");
	        return ep.getQuery();
	      }
	    });
	};

	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
		throws QueryNodeException {
		
		
		AqpSubqueryParser provider = parsers.get(funcName);
		if (provider == null)
			return null;
		
		AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
    
		AqpFunctionQParser queryParser;
    SolrQueryRequest req = reqAttr.getRequest();
    
    if (req == null) {
      // the params are all null, the functions MUST NOT use req()
      // we live dangerously
      queryParser = new AqpFunctionQParser(null, null, null, null);
    }
    else {
      queryParser = new AqpFunctionQParser(null, reqAttr.getLocalParams(), reqAttr.getParams(), req);
    }
    
		
		//AqpFunctionQueryTreeBuilder.flattenChildren(node); // convert into opaque nodes
		//AqpFunctionQueryTreeBuilder.simplifyValueNode(node); // remove func name, leave only values
		
		return new AqpSubQueryTreeBuilder(provider, queryParser);
				
	}

}
