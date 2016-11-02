package org.apache.lucene.queryparser.flexible.aqp.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilderProvider;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeReflector;

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
	
	public void addProvider(int index, AqpFunctionQueryBuilderProvider provider) {
    if (providers.contains(provider)) {
      providers.remove(provider);
    }
    providers.add(index, provider);
  }

	public void setBuilder(String funcName, AqpFunctionQueryBuilder builder) {
		builders.put(funcName, builder);
	}

	/**
	 * NOTE: passing the config as an argument is not good, normally it should be
	 * accessible to us. However, the builders have no access to it, so we must
	 * do this
	 * @throws QueryNodeException
	 *     when no builder is found
	 */
	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
		throws QueryNodeException {
		
		if (builders.containsKey(funcName)) {
			return builders.get(funcName);
		}
		for (AqpFunctionQueryBuilderProvider provider: providers) {
		  AqpFunctionQueryBuilder builder = provider.getBuilder(funcName, node, config);
			if (builder != null) {
				return builder;
			}
		}
		return null;
	}

	@Override
	public void clear() {
		providers.clear();
		builders.clear();
	}

	@Override
	public void copyTo(AttributeImpl target) {
		AqpFunctionQueryBuilderConfig t = (AqpFunctionQueryBuilderConfig) target;
		for (AqpFunctionQueryBuilderProvider provider: providers) {
      t.addProvider(provider);
    }
    for ( Entry<String, AqpFunctionQueryBuilder> builder: builders.entrySet()) {
      t.setBuilder(builder.getKey(), builder.getValue());
    }
	}

	
  @Override
  public void reflectWith(AttributeReflector reflector) {
    for (AqpFunctionQueryBuilderProvider provider: providers) {
      reflector.reflect(AqpFunctionQueryBuilderConfig.class, "provider", provider);
    }
    for ( Entry<String, AqpFunctionQueryBuilder> builder: builders.entrySet()) {
      reflector.reflect(AqpFunctionQueryBuilderConfig.class, "builder:" + builder.getKey(), builder.getValue());
    }
  }

}
