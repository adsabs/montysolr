package org.apache.lucene.queryParser.aqp.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryParser.aqp.builders.AqpFunctionQueryBuilderProvider;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.util.AttributeImpl;

public class AqpFunctionQueryBuilderConfigImpl extends AttributeImpl implements
		AqpFunctionQueryBuilderConfig {
	
	private static final long serialVersionUID = 1919178907275699596L;
	
	List<AqpFunctionQueryBuilderProvider> providers = new ArrayList<AqpFunctionQueryBuilderProvider>();
	Map<String, AqpFunctionQueryBuilder> builders = new HashMap<String, AqpFunctionQueryBuilder>();
	
	public void addProvider(AqpFunctionQueryBuilderProvider provider) {
		if (!providers.contains(provider)) {
			providers.add(provider);
		}

	}

	public void setBuilder(String funcName, AqpFunctionQueryBuilder builder) {
		builders.put(funcName, builder);
	}

	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node) {
		if (builders.containsKey(funcName)) {
			return builders.get(funcName);
		}
		for (AqpFunctionQueryBuilderProvider provider: providers) {
			AqpFunctionQueryBuilder builder = provider.getBuilder(funcName, node);
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
