package org.apache.lucene.queryParser.aqp;

import org.antlr.runtime.TokenStream;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

public abstract class AqpSyntaxParserAbstract implements AqpSyntaxParser {
	
	/**
	 * This parse method uses reflection so that it can load any grammar
	 * without knowing in advance its name
	 */
    @Override public QueryNode parse(CharSequence query, CharSequence field)
	throws QueryNodeParseException {
    	TokenStream tokens = getTokenStream(query);
    	return parseTokenStream(tokens, query, field);
    }
}
