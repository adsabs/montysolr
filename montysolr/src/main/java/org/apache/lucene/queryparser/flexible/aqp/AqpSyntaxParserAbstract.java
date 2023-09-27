package org.apache.lucene.queryparser.flexible.aqp;

import org.antlr.runtime.TokenStream;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

/**
 * All ANTLR parsers should subclass {@link AqpSyntaxParser} and
 * provide two methods:
 * <p>
 * {@link AqpSyntaxParser}{@link #getTokenStream(CharSequence)}
 * {@link AqpSyntaxParser}{@link #parseTokenStream(TokenStream, CharSequence, CharSequence)}
 * <p>
 * Optionally, the new class can also override
 * <p>
 * {@link AqpSyntaxParser}{@link #initializeGrammar(String)}
 * <p>
 * The default implementation is using reflection and is able
 * to instantiate any grammar provided that the top parse rule
 * is called <b>mainQ</b> and that the grammar is producing
 * AST tree.
 */
public abstract class AqpSyntaxParserAbstract implements AqpSyntaxParser {

    /**
     * Parse the query and return the {@link QueryNode}. ANTLR will
     * do the parsing and we return AST.
     */
    public QueryNode parse(CharSequence query, CharSequence field)
            throws QueryNodeParseException {
        TokenStream tokens = getTokenStream(query);
        return parseTokenStream(tokens, query, field);
    }
}
