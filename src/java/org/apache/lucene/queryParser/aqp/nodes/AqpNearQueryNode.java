package org.apache.lucene.queryParser.aqp.nodes;

import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class AqpNearQueryNode implements QueryNode {
	
	public AqpNearQueryNode(List<QueryNode> children, int proximity) {
		//
	}

	@Override
	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QueryNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsTag(String tagName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsTag(CharSequence tagName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getTag(String tagName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getTag(CharSequence tagName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryNode cloneTree() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(QueryNode child) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(List<QueryNode> children) {
		// TODO Auto-generated method stub

	}

	@Override
	public void set(List<QueryNode> children) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTag(String tagName, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTag(CharSequence tagName, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsetTag(String tagName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsetTag(CharSequence tagName) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<CharSequence, Object> getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getTagMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
