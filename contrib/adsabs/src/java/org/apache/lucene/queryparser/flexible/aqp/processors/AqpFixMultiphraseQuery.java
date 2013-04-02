package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;

public class AqpFixMultiphraseQuery extends QueryNodeProcessorImpl {

	@Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
	  return node;
  }

	@Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
		if (node instanceof MultiPhraseQueryNode) {
			
			
			List<QueryNode> children = node.getChildren();
			NodeOfQuery graph = new NodeOfQuery(((FieldQueryNode)children.get(0)).getBegin());
			
			for (QueryNode child : children) {
				System.out.println("addToken(): " + child);
				graph.addNode((FieldQueryNode)child);
			}
			
			System.out.println(graph.toString());
			
			List<List<List<QueryNode>>> queries;
			try {
	      queries = graph.traverseGraphFindAllQueries();
      } catch (CloneNotSupportedException e) {
	      throw new QueryNodeException(e);
      }
			
			// each list is a query - inside the query, every 
			// element is a list (if there are more elements, they 
			// share the same span)
			
			List<QueryNode> mainQueryClauses = new ArrayList<QueryNode>();
			for (List<List<QueryNode>> oneQuery: queries) {
				QueryNode qn;
				List<QueryNode> clauses = new ArrayList<QueryNode>();
				for (List<QueryNode> qElement: oneQuery) {
					if (qElement.size() > 1) { // synonymous tokens at the same position/offset
						qn = new AqpOrQueryNode((List<QueryNode>) qElement);
					}
					else {
						qn = qElement.get(0);
					}
					clauses.add(qn);
				}
				if (clauses.size() > 1) {
					mainQueryClauses.add(new AqpAndQueryNode(clauses));
				}
				else {
					mainQueryClauses.add(clauses.get(0));
				}
				
			}
			if (mainQueryClauses.size() > 1) {
				return new AqpOrQueryNode(mainQueryClauses);
			}
			else {
				return mainQueryClauses.get(0);
			}
				
		}
	  return node;
  }

	@Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
	  return children;
  }


	class NodeOfQuery {
		protected int startPos;
		private List<QueryNode> payload = new ArrayList<QueryNode>();
		private Map<Integer, NodeOfQuery> children;
		private int nodeRetrieved = 0;
		protected int endPos = -1;

		public NodeOfQuery(int startPosition) {
			startPos = startPosition;
			payload = new ArrayList<QueryNode>();
			children = new HashMap<Integer, NodeOfQuery>();
		}
		
		public NodeOfQuery(QueryNode node) {
			startPos = ((FieldQueryNode) node).getBegin();
			payload = new ArrayList<QueryNode>();
			children = new HashMap<Integer, NodeOfQuery>();
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
			for (Entry<Integer, NodeOfQuery> nq: children.entrySet()) {
				sb.append(ind + "<child key=\"" + nq.getKey() + "\">\n");
				sb.append(nq.getValue().prn(indent + 2));
				sb.append(ind +"</child>\n");
			}
			sb.append(ind + "</NodeOfQuery>\n");
			return sb.toString();
		}
		
		public void addNode(QueryNode qnode) {
			FieldQueryNode node = ((FieldQueryNode) qnode);
			assert node.getBegin() > -1;
			assert node.getEnd() > -1 && node.getEnd() > node.getBegin();
			
			if (node.getBegin() == startPos) {
				if (endPos > -1 && node.getEnd() == endPos) {
					if (!payload.contains(node))
						System.out.println("Adding payload: " + node);
						payload.add(node);
					  return;
				}
				if (children.containsKey(node.getEnd())) {
					System.out.println("Adding child: [" + node.getEnd() + "] " + node);
					children.get(node.getEnd()).addNode(node);
				}
				else {
					System.out.println("Creating child: [" + node.getEnd() + "] " + node);
					children.put(node.getEnd(), new NodeOfQuery(node));
				}
			}
			else {
				if (endPos > -1 && node.getEnd() == endPos && node.getBegin() == startPos) {
					if (!payload.contains(node))
						System.out.println("#2 Adding payload: " + node);
						payload.add(node);
					  return;
				}
				
				if (children.size() == 0 && node.getBegin() > endPos) {
					System.out.println("#2 Appending child: [" + node.getEnd() + "] " + node);
					children.put(node.getEnd(), new NodeOfQuery(node));
				}
				else {
					//if (children.containsKey(node.getEnd())) {
					//	System.out.println("#2 Adding child: [" + node.getEnd() + "] " + node);
					//	children.get(node.getEnd()).addNode(node);
					//	return;
					//}
					
					for (Entry<Integer, NodeOfQuery> child: children.entrySet()) {
						//if (node.getEnd() > child.getKey()) {
							System.out.println("Descending into: " + child.getKey());
							child.getValue().addNode(node);
						//}
					}
				}
			}
		}
		
		public void drillDown(QueryPath path) {
			if (children.size() == 0) { // terminal node
				path.terminus();
				return;
			}
			for (Entry<Integer, NodeOfQuery> child: children.entrySet()) {
				path.push(child.getValue().startPos);
				path.push(child.getValue().endPos);
				child.getValue().drillDown(path);
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
	    //assert path.get(pos) == startPos;
	    if (payload.size() > 0) {
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
	    nodeRetrieved++;
	    if (pos >= path.size())
				return;
			children.get(path.get(pos+1)).retrieveQueryElements(oneQuery, path, pos+2);
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
}
