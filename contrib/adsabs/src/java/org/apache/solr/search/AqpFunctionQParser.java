package org.apache.solr.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.ConstValueSource;
import org.apache.lucene.queries.function.valuesource.DoubleConstValueSource;
import org.apache.lucene.queries.function.valuesource.LiteralValueSource;
import org.apache.lucene.queries.function.valuesource.QueryValueSource;
import org.apache.lucene.queryparser.flexible.aqp.NestedParseException;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;


public class AqpFunctionQParser extends FunctionQParser {

	private static final String TAGID = QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID.toLowerCase();

	public AqpFunctionQParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);
	}

	private int currChild = -1;
	private AqpFunctionQueryNode qNode = null;

	protected boolean canConsume() {
		return currChild+1 <= qNode.getFuncValues().size()-1;
	}
	protected OriginalInput consume() {
		currChild++;
		try {
			return qNode.getFuncValues().get(currChild);
		}
		catch (Exception e) {
			throw new NestedParseException("Function tried to get a new argument, but none is available" + qNode.toString());
		}

	}

	protected String consumeAsString() {
		OriginalInput qn = consume();
		return qn.value;

	}


	@Override
	protected ValueSource parseValueSource(boolean doConsumeDelimiter)
	throws SyntaxError {

		// check if there is a query already built inside our node
		OriginalInput node = consume();
		String input = node.value;


		if (input.substring(0, 1).equals("\"") || input.substring(0, 1).equals("\'")) {
			return new LiteralValueSource(input);
		}
		else if (input.substring(0,1).equals("$")) {
			String val = getParam(input);
			if (val == null) {
				throw new SyntaxError("Missing param " + input + " while parsing function '" + val + "'");
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

		StrParser p = new StrParser(input);

		try {
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
		catch (NumberFormatException e) {
			return new LiteralValueSource(input);
		}


	}



	public void setQueryNode(AqpFunctionQueryNode node) {
		this.qNode = node;
		this.currChild = -1;
		if (node.getOriginalInput() != null) {
			sp = new StrParser(node.getOriginalInput().value);
		}
	}

	public QueryNode getQueryNode() {
		return qNode;
	}


	public String parseId() throws SyntaxError {
		return consumeAsString();
	}


	public int parseInt() {
		return Integer.valueOf(consumeAsString());
	}

	public Float parseFloat() throws SyntaxError {
		String str = consumeAsString();
		if (argWasQuoted()) throw new SyntaxError("Expected float instead of quoted string:" + str);
		float value = Float.parseFloat(str);
		return value;
	}

	public double parseDouble() throws SyntaxError {
		String str = consumeAsString();
		if (argWasQuoted()) throw new SyntaxError("Expected double instead of quoted string:" + str);
		double value = Double.parseDouble(str);
		return value;
	}

	public List<ValueSource> parseValueSourceList() throws SyntaxError {
		List<ValueSource> sources = new ArrayList<ValueSource>(3);
		while (canConsume()) {
			sources.add(parseValueSource(true));
		}
		return sources;
	}

	public Query parseNestedQuery() throws SyntaxError {
 		OriginalInput node = consume();
		QParser parser = subQuery(node.value, null); // use the default parser
		return parser.getQuery();
	}

	public boolean hasMoreArguments() {
		return canConsume();
	}
	
}
