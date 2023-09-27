package org.apache.lucene.queryparser.flexible.aqp;

import org.antlr.runtime.TokenStream;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.parser.SyntaxParser;

public interface AqpSyntaxParser extends SyntaxParser {
    /**
     * @param grammarName - the name of the query
     * @return AqpSyntaxParser
     * @throws QueryNodeParseException - there are different implementations (should we want different
     *                                 interfaces?) some may be loading grammars on the fly, others will
     *                                 load grammars directly
     */
    AqpSyntaxParser initializeGrammar(String grammarName)
            throws QueryNodeParseException;

    /**
     * This method should return the stream of tokens, it can be used to modify
     * the original query before it gets executed
     *
     * @param input - original query
     * @return TokenStream
     * - (un)modified stream of tokens
     * @throws QueryNodeParseException - there are different implementations (should we want different
     *                                 interfaces?) some may be loading grammars on the fly, others will
     *                                 load grammars directly
     */
    TokenStream getTokenStream(CharSequence input)
            throws QueryNodeParseException;

    QueryNode parseTokenStream(TokenStream tokens, CharSequence query,
                                      CharSequence field) throws QueryNodeParseException;

    QueryNode parse(CharSequence query, CharSequence field)
            throws QueryNodeParseException;
}
