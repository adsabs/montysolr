package org.apache.solr.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.aqp.NestedParseException;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryParser.core.nodes.OpaqueQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.function.ConstValueSource;
import org.apache.solr.search.function.DoubleConstValueSource;
import org.apache.solr.search.function.FunctionQuery;
import org.apache.solr.search.function.LiteralValueSource;
import org.apache.solr.search.function.QueryValueSource;
import org.apache.solr.search.function.ValueSource;


public class AqpFunctionQParser extends FunctionQParser {

	public AqpFunctionQParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);
	}
	
	private int currChild = -1;
	private QueryNode qNode = null;
	
	protected boolean canConsume() {
		return currChild+1 <= qNode.getChildren().size()-1;
	}
	protected QueryNode consume() {
		currChild++;
		try {
			return qNode.getChildren().get(currChild);
		}
		catch (Exception e) {
			throw new NestedParseException("Function tried to get a new argument, but none is available" + qNode.toString());
		}
			
	}
	
	protected String consumeAsString() {
		QueryNode qn = consume();
		return (String) ((OpaqueQueryNode) qn).getValue();
	}
	
	
	@Override
	protected ValueSource parseValueSource(boolean doConsumeDelimiter)
			throws ParseException {
		
		// check if there is a query already built inside our node
		QueryNode node = consume();
		if (node.containsTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID)) {
			Query q = (Query) node.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
			if (q instanceof FunctionQuery) {
				return ((FunctionQuery) q).getValueSource();
			}
			else {
				return new QueryValueSource(q, 0.0f);
			}
		}
		String input = (String) ((OpaqueQueryNode) node).getValue();
		
		
		if (input.substring(0, 1).equals("\"") || input.substring(0, 1).equals("\'")) {
			return new LiteralValueSource(input);
		}
		else if (input.substring(0,1).equals("$")) {
		      String val = getParam(input);
		      if (val == null) {
		        throw new ParseException("Missing param " + input + " while parsing function '" + val + "'");
		      }

		      QParser subParser = subQuery(val, "func");
		      if (subParser instanceof FunctionQParser) {
		        ((FunctionQParser)subParser).setParseMultipleSources(true);
		      }
		      Query subQuery = subParser.getQuery();
		      if (subQuery instanceof FunctionQuery) {
		        return ((FunctionQuery) subQuery).getValueSource();
		      } else {
		        return new QueryValueSource(subQuery, 0.0f);
		      }
		}
		else if (req != null && req.getSchema().getField(input) != null) {
			SchemaField f = req.getSchema().getField(input);
	        return f.getType().getValueSource(f, this);
		}
		
		QueryParsing.StrParser p = new QueryParsing.StrParser(input);
		Number num = p.getNumber();
		
		if (num instanceof Long) {
	        return new LongConstValueSource(num.longValue());
	      } else if (num instanceof Double) {
	        return new DoubleConstValueSource(num.doubleValue());
	      } else {
	        // shouldn't happen
	        return new ConstValueSource(num.floatValue());
	      }
		
		
	}
	
	
	
	public void setQueryNode(QueryNode node) {
		this.qNode = node;
	}
	
	public QueryNode getQueryNode() {
		return qNode;
	}
	
	
	public String parseId() throws ParseException {
		return consumeAsString();
		
	}
	
	public String getString() {
		return consumeAsString();
	}
	
	public int parseInt() {
		return Integer.valueOf(consumeAsString());
	}
	
	public Float parseFloat() throws ParseException {
	    String str = consumeAsString();
	    if (argWasQuoted()) throw new ParseException("Expected float instead of quoted string:" + str);
	    float value = Float.parseFloat(str);
	    return value;
	  }
	
	public double parseDouble() throws ParseException {
	    String str = consumeAsString();
	    if (argWasQuoted()) throw new ParseException("Expected double instead of quoted string:" + str);
	    double value = Double.parseDouble(str);
	    return value;
	  }
	
	public List<ValueSource> parseValueSourceList() throws ParseException {
	    List<ValueSource> sources = new ArrayList<ValueSource>(3);
	    while (canConsume()) {
	      sources.add(parseValueSource(true));
	    }
	    return sources;
	  }
	
	// here we differ from the standard lucene/solr way
	// I'd say we differ because we can handle it in an elegant
	// way and have the nested queries directly inside, however
	// this means we are not compatible
	public Query parseNestedQuery() throws ParseException {
	    // check if there is a query already built inside our node
		QueryNode node = consume();
		if (node.containsTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID)) {
			return (Query) node.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
		}
		
		throw new ParseException("Nested query was expected, instead we have: " + node.toString());
	  }
	
	public boolean hasMoreArguments() {
		return canConsume();
	}
}
