package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;

import com.mongodb.QueryOperators;

public class AqpFixMultiphraseQuery extends QueryNodeProcessorImpl {

	@Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
	  return node;
  }

	@Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
		if (node instanceof MultiPhraseQueryNode) {
			NodeOfQuery graph = null;
			
			List<QueryNode> children = node.getChildren();
			for (QueryNode child : children) {
				System.out.println(((FieldQueryNode) child).getPositionIncrement() + child.toString());
				if (graph == null) {
					graph = new NodeOfQuery(((FieldQueryNode)child).getBegin());
				}
				graph.addNode((FieldQueryNode)child);
			}
			try {
	      List<List<List<FieldQueryNode>>> queries = graph.traverseGraphFindAllQueries();
      } catch (CloneNotSupportedException e) {
	      throw new QueryNodeException(e);
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
		private List<FieldQueryNode> payload = new ArrayList<FieldQueryNode>();
		private Map<Integer, NodeOfQuery> children;
		private int nodeRetrieved = 0;
		protected int endPos = -1;

		public NodeOfQuery(int startPosition) {
			startPos = startPosition;
			payload = new ArrayList<FieldQueryNode>();
			children = new HashMap<Integer, NodeOfQuery>();
		}
		
		public NodeOfQuery(FieldQueryNode node) {
			startPos = node.getBegin();
			payload = new ArrayList<FieldQueryNode>();
			children = new HashMap<Integer, NodeOfQuery>();
			endPos  = node.getEnd();
			payload.add(node);
		}
		
		
		public void addNode(FieldQueryNode node) {
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
					System.out.println("Adding child: " + node.getEnd());
					children.get(node.getEnd()).addNode(node);
				}
				else {
					System.out.println("Creating child: " + node.getEnd());
					children.put(node.getEnd(), new NodeOfQuery(node));
				}
			}
			else {
				if (children.size() == 0) {
					System.out.println("Appending child: " + node.getBegin());
					children.put(node.getEnd(), new NodeOfQuery(node));
				}
				else {
					for (Entry<Integer, NodeOfQuery> child: children.entrySet()) {
						if (node.getBegin() > child.getKey()) {
							System.out.println("Descending into: " + child.getKey());
							child.getValue().addNode(node);
						}
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
		
		public List<List<List<FieldQueryNode>>> traverseGraphFindAllQueries() 
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
			
			List<List<List<FieldQueryNode>>> queries = new ArrayList<List<List<FieldQueryNode>>>();
			
			// retrieve only the queries made of query elements that cover the longest distance
			for (int i=0;i<measured.length;i++) {
				if (measured[i] != max) 
					continue;
				List<List<FieldQueryNode>> oneQuery = new ArrayList<List<FieldQueryNode>>();
				retrieveQueryElements(oneQuery, paths.get(i), 0);
				assert oneQuery.size() == paths.get(i).size() / 2;
				queries.add(oneQuery);
			}
			
			assert queries.size() > 0;
			return queries;
		}
		
		private void retrieveQueryElements(List<List<FieldQueryNode>> oneQuery, List<Integer> path, int pos) 
				throws CloneNotSupportedException {
	    //assert path.get(pos) == startPos;
	    if (payload.size() > 0) {
		    if (nodeRetrieved > 0) {
		    	ArrayList<FieldQueryNode> copyOfNodes = new ArrayList<FieldQueryNode>(payload.size());
		    	for (FieldQueryNode n: payload) {
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
