package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpBooleanQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNotQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TokenizedPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.solr.common.params.SolrParams;

/**
 * This processor must follow the {@link AqpAnalyzerQueryNodeProcessor}
 * It will build a graph of the query node and will handle the cases
 * where a synonym expansion spans over several tokens. Basically,
 * we build every possible path that queries can be constructed.
 * 
 * You can supply your own query builder(s) which can do different things
 * based on the type of the resulting query graph. Ie. you may want to
 * add a boost to queries that were original input, or wrap queries with
 * many terms into a spanquery instead of a phrase. The options are many!
 * 
 * @author rchyla
 *
 */
public class AqpPostAnalysisProcessor extends QueryNodeProcessorImpl {

  
	@Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
	  return node;
  }

	@Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
		if (node.getTag(AqpAdsabsAnalyzerProcessor.ANALYZED) != null) {
			List<List<List<QueryNode>>> queryStructure;
			
			AqpRequestParams req = getRequest();
			SolrParams params = req.getParams();
			final String unfieldedDefaultOperator = "and";
			if (params != null) {
				params.get(AqpAdsabsQueryParser.AQP_UNFIELDED_OPERATOR_PARAM, "or").toLowerCase();
			}
			
			if (node instanceof TokenizedPhraseQueryNode) { // no need to do anything
				return node;
			}
			else if (node instanceof GroupQueryNode ) { // may have multi-token expansion (when it wasn't surrounded by "")
				
				if (node.getChildren().size() > 0 && node.getChildren().get(0) instanceof BooleanQueryNode 
						&& ((BooleanQueryNode) node.getChildren().get(0)).getChildren().size() > 1) {
					queryStructure = extractQueries(node.getChildren().get(0));
					final int proximity = getDefaultProximityValue();
					
					return buildNewQueryNode(queryStructure,
							new QueryBuilder() {
								@Override
								public QueryNode buildQuery(List<QueryNode> clauses) {
									if (unfieldedDefaultOperator.equals("span")) {
										return new AqpNearQueryNode(clauses, proximity);
									}
									else if (unfieldedDefaultOperator.equals("and")) {
										return new AqpAndQueryNode(clauses);
									}
									else if (unfieldedDefaultOperator.equals("not")) {
										return new AqpNotQueryNode(clauses);
									}
									else {
										return new AqpOrQueryNode(clauses);
									}
								}
							}
					);
				}
				
			}
			else if (node instanceof MultiPhraseQueryNode ) {
				queryStructure = extractQueries(node);
				
				if (node.getParent() instanceof FuzzyQueryNode) { // "some span query"~3
					
					final FuzzyQueryNode parent = (FuzzyQueryNode) node.getParent();
					
					return buildNewQueryNode(queryStructure,
							new QueryBuilder() {
								@Override
								public QueryNode buildQuery(List<QueryNode> clauses) {
									return new AqpNearQueryNode(clauses, parent.getPositionIncrement()); 
								}
							}
					);
				}
				else {
					
					return buildNewQueryNode(queryStructure,      // default: create boolean ((+a +b) OR (+a +(b|c)))
							new QueryBuilder() {
								@Override
								public QueryNode buildQuery(List<QueryNode> clauses) {
									if (this.isMultiDimensional) {
										MultiPhraseQueryNode pq = new MultiPhraseQueryNode();
										for (QueryNode c: clauses) {
											if (c.isLeaf()) {
												pq.add(c);
											}
											else {
												for (QueryNode child: c.getChildren()) {
													pq.add(child);
												}
											}
										}
										return pq;
									} 
									else {
										TokenizedPhraseQueryNode pq = new TokenizedPhraseQueryNode();
										//MultiPhraseQueryNode pq = new MultiPhraseQueryNode();
										pq.add(clauses);
										return pq;
									}
								}
							}
					);
				}
			}
			
			// do nothing, we don't know how to process this type
			return node; 
			
		}
		
	  return node;
  }
	
	private AqpRequestParams getRequest() throws QueryNodeException {
		QueryConfigHandler config = getQueryConfigHandler();
		AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
		if (config == null || config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST) == null) {
			throw new QueryNodeException(new MessageImpl(
          QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
          "Configuration error: "
          + "SOLR_REQUEST is missing"));
		}
		return config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
	}
	
	
	private Integer getDefaultProximityValue() throws QueryNodeException {
    QueryConfigHandler queryConfig = getQueryConfigHandler();
    if (queryConfig == null
        || !queryConfig.has(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_PROXIMITY)) {
      throw new QueryNodeException(new MessageImpl(
          QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
          "Configuration error: "
          + "DefaultProximity value is missing"));
    }
    return queryConfig.get(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_PROXIMITY);
  }

	/*
	 * Build a simple Query node from
	 * 	  queries
	 *       - list of queries, all the possible combinations of consecutive
	 *         QueryNodes ordered to cover the query input
	 */
	protected QueryNode buildNewQueryNode (List<List<List<QueryNode>>> queries,
			QueryBuilder queryBuilder) {
		List<QueryNode> mainQueryClauses = new ArrayList<QueryNode>();
		for (List<List<QueryNode>> oneQuery: queries) {
			List<QueryNode> clauses = new ArrayList<QueryNode>();
			for (List<QueryNode> qElement: oneQuery) {
				clauses.add(queryBuilder.buildQueryElement(qElement));
			}
			if (clauses.size() > 1) {
				mainQueryClauses.add(queryBuilder.buildQuery(clauses));
				
			}
			else {
				mainQueryClauses.add(clauses.get(0));
			}
		}
		return queryBuilder.buildTopQuery(mainQueryClauses);
	}
	
	
	/*
	 * this method knows to handle FieldQueryNodes, it is especially useful
	 * for 	MultiPhraseQueryNode
	 * 
	 * If there are non-fieldable nodes, it will fail. We cannot process
	 * such queries (and we shouldn't!)
	 */
	protected List<List<List<QueryNode>>> extractQueries(QueryNode node) throws QueryNodeException {
			
			List<QueryNode> children = node.getChildren();
			NodeOfQuery graph = new NodeOfQuery(-1, -1);
			
			for (QueryNode child : children) {
				//System.out.println("addToken(): " + child);
				graph.consume(child);
			}
			
			//System.out.println(graph.toString());
			
			List<List<List<QueryNode>>> queries;
			try {
	      queries = graph.traverseGraphFindAllQueries();
      } catch (CloneNotSupportedException e) {
	      throw new QueryNodeException(e);
      }
			
			// each list is a query - inside the query, every 
			// element is a list (if there are more elements, they 
			// share the same span)
			return queries;
	}
	
	@Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
	  return children;
  }


	class NodeOfQuery {
		protected int startPos;
		private List<QueryNode> payload = new ArrayList<QueryNode>();
		private List<NodeOfQuery> children;
		private int nodeRetrieved = 0;
		protected int endPos = -1;

		public NodeOfQuery(int startPosition, int endPosition) {
			startPos = startPosition;
			endPos = endPosition;
			payload = new ArrayList<QueryNode>();
			children = new ArrayList<NodeOfQuery>();
		}
		
		public NodeOfQuery(QueryNode node) {
			startPos = ((FieldQueryNode) node).getBegin();
			payload = new ArrayList<QueryNode>();
			children = new ArrayList<NodeOfQuery>();
			endPos  = ((FieldQueryNode) node).getEnd();
			payload.add(node);
		}
		
		@Override
		public String toString() {
			return prn(0);
		}
		
		public String prn(int indent) {
			StringBuilder sb = new StringBuilder();
			for (int i=0;i<indent;i++) {
				sb.append(" ");
			}
			String ind = sb.toString();
			sb = new StringBuilder();
			
			sb.append(ind + "<NodeOfQuery startPos=\"" + this.startPos
					      + "\" endPos=\"" + this.endPos + "\"/>\n");
			for (QueryNode child: payload) {
				sb.append(ind + "<payload>" + child + "</payload>\n");
			}
			for (NodeOfQuery child: children) {
				sb.append(ind + "<child>\n");
				sb.append(child.prn(indent + 2));
				sb.append(ind +"</child>\n");
			}
			sb.append(ind + "</NodeOfQuery>\n");
			return sb.toString();
		}
		
		public void consume(QueryNode qnode) {
			FieldQueryNode node = ((FieldQueryNode) qnode);
			boolean descended = false;
			for (NodeOfQuery child: children) {
				if (child.startPos == node.getBegin() && child.endPos == node.getEnd()) {
					child.addPayload(node);
					return;
				}
				if (child.startPos < node.getBegin() && child.endPos < node.getEnd()) {
					child.consume(qnode);
					descended = true;
				}
			}
			if (descended == false && node.getBegin() > this.startPos) {
				children.add(new NodeOfQuery(node));
			}
		}
		
		public void addPayload(QueryNode node) {
			if (!payload.contains(node))
				//System.out.println("Adding payload: " + node);
				payload.add(node);
		}
		

		
		public void drillDown(QueryPath path) {
			if (children.size() == 0) { // terminal node
				path.terminus();
				return;
			}
			for (NodeOfQuery child: children) {
				path.push(child.startPos);
				path.push(child.endPos);
				child.drillDown(path);
				path.pop();
				path.pop();
			}
		}
		
		public List<List<List<QueryNode>>> traverseGraphFindAllQueries() 
				throws CloneNotSupportedException {
			QueryPath path = new QueryPath(); // find all queries
			drillDown(path);
			
			// measure how long a string the query covers
			List<List<Integer>> paths = path.getAllPaths();
			int[] measured = measurePaths(paths);
			
			// we'll consider only the queries that cover the max distance
			int max = 0;
			for (int m: measured) {
				if (m > max) 
					max = m;
			}
			
			List<List<List<QueryNode>>> queries = new ArrayList<List<List<QueryNode>>>();
			
			// retrieve only the queries made of query elements that cover the longest distance
			for (int i=0;i<measured.length;i++) {
				if (measured[i] != max) 
					continue;
				List<List<QueryNode>> oneQuery = new ArrayList<List<QueryNode>>();
				retrieveQueryElements(oneQuery, paths.get(i), 0);
				assert oneQuery.size() == paths.get(i).size() / 2;
				queries.add(oneQuery);
			}
			
			assert queries.size() > 0;
			return queries;
		}
		
		private void retrieveQueryElements(List<List<QueryNode>> oneQuery, List<Integer> path, int pos) 
				throws CloneNotSupportedException {
			if (pos >= path.size())
				return;
			
			Integer keyStart = path.get(pos);
			Integer keyEnd = path.get(pos+1);
			

			for (NodeOfQuery child: children) {
				if (child.startPos == keyStart && child.endPos == keyEnd) {
					child.insertItself(oneQuery);
					child.retrieveQueryElements(oneQuery, path, pos+2);
					return;
				}
			}
			throw new IllegalStateException("Trying to get query element that doesn't exist: " + keyStart + ":" + keyEnd);
    }
		
		private void insertItself(List<List<QueryNode>> oneQuery) throws CloneNotSupportedException {
			if (nodeRetrieved > 0) {
	    	ArrayList<QueryNode> copyOfNodes = new ArrayList<QueryNode>(payload.size());
	    	for (QueryNode n: payload) {
	    		copyOfNodes.add(n.cloneTree());
	    	}
	    	oneQuery.add(copyOfNodes);
	    }
	    else {
	    	oneQuery.add(payload);
	    }
    }

		
		private int[] measurePaths(List<List<Integer>> paths) {
			int[] measuredPaths = new int[paths.size()];
			int j = 0;
			for (List<Integer> path: paths) {
				assert path.size() % 2 == 0;
				int length = 0;
				for (int i=0;i<path.size(); i=i+2) {
					length += path.get(i+1) - path.get(i);
				}
				length += (path.size() / 2)-1; // number of edges (assuming it equals 1 space, hm...)
				measuredPaths[j++] = length;
			}
			return measuredPaths;
		}
	}
	
	
	class QueryPath {
		private ArrayList<Integer> data;
		private ArrayList<List<Integer>> paths;
		public QueryPath() {
			data = new ArrayList<Integer>();
			paths = new ArrayList<List<Integer>>();
		}
		public void push(Integer position) {
			data.add(position);
		}
		public Integer pop() {
			return data.remove(data.size()-1);
		}
		public void terminus() {
			ArrayList<Integer> newData = new ArrayList<Integer>(data);
			paths.add(newData);
		}
		public List<List<Integer>> getAllPaths() {
			return paths;
		}
	}
	
	class QueryBuilder {
		public boolean isMultiDimensional = false;
		public void reset() {
			isMultiDimensional = false;
		}
		public QueryNode buildQueryElement(List<QueryNode> samePositionElements) {
			if (samePositionElements.size() > 1) { // synonymous tokens at the same position/offset
				isMultiDimensional = true;
				return new AqpOrQueryNode(samePositionElements);
			}
			else {
				return samePositionElements.get(0);
			}
		}
		public QueryNode buildQuery(List<QueryNode> queryElements) {
			return new AqpAndQueryNode(queryElements);
		}
		public QueryNode buildTopQuery(List<QueryNode> mainQueryClauses) {
	    if (mainQueryClauses.size() == 1) {
	    	return mainQueryClauses.get(0);
	    }
	    else {
	    	return new AqpOrQueryNode(mainQueryClauses);
	    }
    }
	}
}
