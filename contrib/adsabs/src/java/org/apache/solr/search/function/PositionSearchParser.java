package org.apache.solr.search.function;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queryparser.classic.ParseException;

public class PositionSearchParser extends ValueSourceParser {

	@Override
	public ValueSource parse(FunctionQParser fp) throws SyntaxError {
		String field = fp.parseArg();
		String author = fp.parseArg();
		int start = fp.parseInt();
		int end = fp.parseInt();

		return new PositionSearchFunction(field, author, start, end);
	}

	@Override
	public void init(NamedList args) {
		/* initialize the value to consider as null */
	}

}

