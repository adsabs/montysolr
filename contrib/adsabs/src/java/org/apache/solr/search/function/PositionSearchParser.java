package org.apache.solr.search.function;

import org.apache.lucene.queryParser.ParseException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.ValueSourceParser;

public class PositionSearchParser extends ValueSourceParser {

	@Override
	public ValueSource parse(FunctionQParser fp) throws ParseException {
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

