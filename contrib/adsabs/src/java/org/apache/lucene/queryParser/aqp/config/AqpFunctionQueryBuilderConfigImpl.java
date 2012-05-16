package org.apache.lucene.queryParser.aqp.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryParser.aqp.builders.AqpFunctionQueryBuilderProvider;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.util.AttributeImpl;

public class AqpFunctionQueryBuilderConfigImpl extends AttributeImpl implements
		AqpFunctionQueryBuilderConfig {
	
	private static final long serialVersionUID = 1919178907275699596L;
	
	List<AqpFunctionQueryBuilderProvider> providers = new ArrayList<AqpFunctionQueryBuilderProvider>();
	Map<String, QueryBuilder> builders = new HashMap<String, QueryBuilder>();
	
	public void addProvider(AqpFunctionQueryBuilderProvider provider) {
		if (!providers.contains(provider)) {
			providers.add(provider);
		}

	}

	public void setBuilder(String funcName, QueryBuilder builder) {
		builders.put(funcName, builder);
	}

	/**
	 * NOTE: passing the config as an argument is not good, normally it should be
	 * accessible to us. However, the builders have no access to it, so we must
	 * do this
	 * @throws QueryNodeException 
	 */
	public QueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
		throws QueryNodeException {
		
		if (builders.containsKey(funcName)) {
			return builders.get(funcName);
		}
		for (AqpFunctionQueryBuilderProvider provider: providers) {
			QueryBuilder builder = provider.getBuilder(funcName, node, config);
			if (builder != null) {
				return builder;
			}
		}
		return null;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void copyTo(AttributeImpl target) {
		throw new UnsupportedOperationException();
		
	}

}
