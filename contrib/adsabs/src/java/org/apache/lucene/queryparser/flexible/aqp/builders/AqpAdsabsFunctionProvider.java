package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.ValueSourceParser;

/**
 * 
 * This here is a place for our own custom functions implemented
 * as ValueSource (ie. the same as SOLR functions). This is
 * different from the {@link AqpAdsabsSubQueryProvider}
 *
 */
public class AqpAdsabsFunctionProvider implements
AqpFunctionQueryBuilderProvider {

	public static Map<String, ValueSourceParser> parsers = new HashMap<String, ValueSourceParser>();

	static {
		// currently empty	
	};

	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
	throws QueryNodeException {


		ValueSourceParser vsProvider = parsers.get(funcName);
		if (vsProvider == null)
			return null;

		// the params are all null because we know we are not using solr request handler params
		AqpFunctionQParser queryParser = new AqpFunctionQParser(null, null, null, null);


		return new AqpFunctionQueryTreeBuilder(vsProvider, queryParser);

	}

}
