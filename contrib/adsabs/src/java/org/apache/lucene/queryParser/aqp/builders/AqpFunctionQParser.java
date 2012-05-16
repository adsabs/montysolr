package org.apache.lucene.queryParser.aqp.builders;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.function.FunctionQuery;
import org.apache.solr.search.function.QueryValueSource;
import org.apache.solr.search.function.ValueSource;


public class AqpFunctionQParser extends FunctionQParser {

	public AqpFunctionQParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);
	}
	
	private int currChild = -1;
	
	private String consume() {
		currChild++;
		return inputValues.get(currChild);
	}
	
	
	
	@Override
	protected ValueSource parseValueSource(boolean doConsumeDelimiter)
			throws ParseException {
		
		consume();
		
		// check if there is a query already built inside our node
		QueryNode node = getQueryNode();
		if (node.containsTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID)) {
			Query q = (Query) node.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
			if (q instanceof FunctionQuery) {
				return ((FunctionQuery) q).getValueSource();
			}
			else {
				return new QueryValueSource(q, 0.0f);
			}
		}
		throw new ParseException("Unexpected function query (not yet ready to handle this one!)");
	}
	
	private QueryNode qNode = null;
	
	public void setQueryNode(QueryNode node) {
		this.qNode = node;
	}
	
	public QueryNode getQueryNode() {
		return qNode;
	}
	
	private List<String> inputValues;
	public void setInputValues(List<String> input) {
		this.inputValues = input;
	}
	
	
	public String parseId() throws ParseException {
		String input = consume();
		return input;
	}
	
	public String getString() {
		String input = consume(); //TODO: check for nested queries
		return input;
	}
}
