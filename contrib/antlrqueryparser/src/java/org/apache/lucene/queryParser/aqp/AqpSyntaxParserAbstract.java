package org.apache.lucene.queryParser.aqp;

import org.antlr.runtime.TokenStream;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

public abstract class AqpSyntaxParserAbstract implements AqpSyntaxParser {

	
    public QueryNode parse(CharSequence query, CharSequence field)
	throws QueryNodeParseException {
    	TokenStream tokens = getTokenStream(query);
    	return parseTokenStream(tokens, query, field);
    }
}
