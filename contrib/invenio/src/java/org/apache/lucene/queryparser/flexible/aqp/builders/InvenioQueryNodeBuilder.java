package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queryparser.flexible.aqp.config.InvenioQueryAttribute.Channel;
import org.apache.lucene.queryparser.flexible.aqp.nodes.InvenioQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.InvenioQuery;
import org.apache.lucene.search.InvenioQueryBitSet;
import org.apache.lucene.search.Query;


/**
 * This builder wraps the query inside {@link InvenioQuery} or 
 * {@link InvenioQueryBitSet} depending on whether {@link Channel}#INTBITSET
 * is set or not.
 * 
 * The query is built by the {@link QueryBuilder} - which is a reference
 * to the builder which called {@link InvenioQueryNodeBuilder}
 * 
 * @see AqpInvenioQueryParserSolr
 * 
 * @author rca
 *
 */
public class InvenioQueryNodeBuilder implements StandardQueryBuilder {
	
	private QueryBuilder master;
	
	public InvenioQueryNodeBuilder(QueryBuilder parentBuilder) {
		master = parentBuilder;
	}
	
	public Query build(QueryNode queryNode) throws QueryNodeException {
		
		InvenioQueryNode iq = (InvenioQueryNode) queryNode;
		QueryNode q = iq.getChild();
		
		if (iq.getChannel() == Channel.INTBITSET ) {
			return new InvenioQueryBitSet((Query) master.build(q), iq.getIdField(), iq.getSearchField());
		}
		else {
			return new InvenioQuery((Query) master.build(q), iq.getIdField(), iq.getSearchField());
		}
			
	  }
	
	
}
