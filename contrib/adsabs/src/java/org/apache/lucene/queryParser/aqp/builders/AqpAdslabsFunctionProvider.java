package org.apache.lucene.queryParser.aqp.builders;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.OpaqueQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.ValueSourceParser;
import org.apache.solr.search.function.FunctionQuery;
import org.apache.solr.search.function.PositionSearchFunction;

public class AqpAdslabsFunctionProvider implements
		AqpFunctionQueryBuilderProvider {

	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node) {
			
		if (funcName.equals("pos")) {
			return new AqpFunctionQueryBuilderAbstract(){
				
				public Query build(QueryNode node) {
					List<QueryNode> children = node.getChildren();
					PositionSearchFunction ps = new PositionSearchFunction(
							(String) ((OpaqueQueryNode) children.get(0)).getValue(), 
							(String) ((OpaqueQueryNode) children.get(1)).getValue(), 
							Integer.valueOf((String) ((OpaqueQueryNode) children.get(2)).getValue()), 
							Integer.valueOf((String) ((OpaqueQueryNode) children.get(3)).getValue())
							);
					return new FunctionQuery(ps);
				}

				public AqpFunctionQueryNode createQNode(QueryNode node) throws QueryNodeException {
					List<String> input = harvestInput(node);
					if (input.contains("QFUNC")) {
						throw new QueryNodeException(new MessageImpl("pos() function loves not nested calls!"));
					}
					
					List<QueryNode> children = node.getChildren();
					
					if (input.size() != children.size()) {
						throw new QueryNodeException(new MessageImpl("Ayayaj I failed getting data, master!"));
					}
					
					if (input.size() == 3) {
						input.add(input.get(input.size()-1));
						children.add(null);
					}
					
					if (input.size() != 4) {
						throw new QueryNodeException(new MessageImpl("More input please, thank you"));
					}
					
					for (int i=0; i<input.size();i++) {
						children.set(i, new OpaqueQueryNode("child#" + i, input.get(i)));
					}
					
					return new AqpFunctionQueryNode(this, children);
				}
			};
		}
		else if (ValueSourceParser.standardValueSourceParsers.containsKey(funcName)) {
			
			// XXX: such an ugly mess...
			final ValueSourceParser argParser = ValueSourceParser.standardValueSourceParsers.get(funcName);
			final AqpFunctionQParser fParser = new AqpFunctionQParser(null, null, null, null);
			final List<String> inputVals = AqpFunctionQueryBuilderAbstract.harvestInput(node);
			inputVals.remove(0);
			
			return new AqpFunctionQueryBuilderAbstract(){
				public Query build(QueryNode node) {
					try {
						fParser.setQueryNode(node);
						fParser.setInputValues(inputVals);
						return new FunctionQuery(argParser.parse(fParser));
					} catch (ParseException e1) {
						throw new RuntimeException("My function is screwed!");
					}
				}
			};
			
		}
		return null;
	}

}
