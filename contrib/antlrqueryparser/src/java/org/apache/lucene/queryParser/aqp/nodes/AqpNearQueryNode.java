package org.apache.lucene.queryParser.aqp.nodes;

import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class AqpNearQueryNode implements QueryNode {
	
	public AqpNearQueryNode(List<QueryNode> children, int proximity) {
		//
	}

	
	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<QueryNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean containsTag(String tagName) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean containsTag(CharSequence tagName) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public Object getTag(String tagName) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object getTag(CharSequence tagName) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public QueryNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public QueryNode cloneTree() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void add(QueryNode child) {
		// TODO Auto-generated method stub

	}

	public void add(List<QueryNode> children) {
		// TODO Auto-generated method stub

	}

	public void set(List<QueryNode> children) {
		// TODO Auto-generated method stub

	}

	public void setTag(String tagName, Object value) {
		// TODO Auto-generated method stub

	}

	public void setTag(CharSequence tagName, Object value) {
		// TODO Auto-generated method stub

	}

	
	public void unsetTag(String tagName) {
		// TODO Auto-generated method stub

	}

	
	public void unsetTag(CharSequence tagName) {
		// TODO Auto-generated method stub

	}

	
	public Map<CharSequence, Object> getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Map<String, Object> getTagMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
