package org.apache.lucene.queryParser.aqp.builders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.aqp.AqpSubqueryParser;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.search.Query;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.function.ValueSource;

public class AqpAdslabsSubSueryProvider implements
		AqpFunctionQueryBuilderProvider {
	
	public static Map<String, AqpSubqueryParser> parsers = new HashMap<String, AqpSubqueryParser>();
	
	static {
		parsers.put("edismax", new AqpSubqueryParser() {
	      public Query parse(FunctionQParser fp) throws ParseException {
    		  List<ValueSource> lst = fp.parseValueSourceList();
    		  StringBuffer out = new StringBuffer();
    		  for (ValueSource vs: lst) {
    			  out.append(vs.toString());
    			  out.append(", ");
    		  }
    		  System.out.println("edismax(" + out.toString() + ")");
    		  return null;
	      }
	    });
	};

	public QueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
		throws QueryNodeException {
		
		
		AqpSubqueryParser provider = parsers.get(funcName);
		if (provider == null)
			return null;
			
		// the params are all null because we know we are not using solr request handler params
		AqpFunctionQParser parser = new AqpFunctionQParser(null, null, null, null);
		
		//AqpFunctionQueryTreeBuilder.flattenChildren(node);
		
		return new AqpSubQueryTreeBuilder(provider, parser);
				
	}

}
