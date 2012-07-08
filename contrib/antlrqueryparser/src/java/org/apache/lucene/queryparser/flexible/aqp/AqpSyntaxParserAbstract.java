package org.apache.lucene.queryparser.flexible.aqp;

import org.antlr.runtime.TokenStream;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

public abstract class AqpSyntaxParserAbstract implements AqpSyntaxParser {

	
    public QueryNode parse(CharSequence query, CharSequence field)
	throws QueryNodeParseException {
    	TokenStream tokens = getTokenStream(query);
    	return parseTokenStream(tokens, query, field);
    }
}
