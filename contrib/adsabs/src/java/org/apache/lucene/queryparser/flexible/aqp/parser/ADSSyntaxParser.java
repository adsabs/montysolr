
                
package org.apache.lucene.queryparser.flexible.aqp.parser;


/**
 * Automatically generated SyntaxParser wrapper built by ant
 * from the grammar source: /home/jluker/workspace/montysolr_trunk/contrib/adsabs/src/java/org/apache/lucene/queryparser/flexible/aqp/parser/ADS.g
 * 
 * YOUR CHANGES WILL BE OVERWRITTEN AUTOMATICALLY!
 */

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.apache.lucene.queryparser.flexible.messages.Message;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.aqp.AqpCommonTree;
import org.apache.lucene.queryparser.flexible.aqp.AqpCommonTreeAdaptor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParser;            	
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParserAbstract;
import org.apache.lucene.queryparser.flexible.aqp.parser.ADSLexer;
import org.apache.lucene.queryparser.flexible.aqp.parser.ADSParser;


public class ADSSyntaxParser extends AqpSyntaxParserAbstract {
    
    
    public AqpSyntaxParser initializeGrammar(String grammarName)
            throws QueryNodeParseException {
        return this;
    }

    public TokenStream getTokenStream(CharSequence input) {
        ANTLRStringStream in = new ANTLRStringStream(input.toString());
        ADSLexer lexer = new ADSLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return tokens;
    }

    public QueryNode parseTokenStream(TokenStream tokens, CharSequence query,
            CharSequence field) throws QueryNodeParseException {
        ADSParser parser = new ADSParser(tokens);
        ADSParser.mainQ_return returnValue;
        
        AqpCommonTreeAdaptor adaptor = new AqpCommonTreeAdaptor(parser.getTokenNames());
        parser.setTreeAdaptor(adaptor);
        
        AqpCommonTree astTree;
        
        try {
            returnValue = parser.mainQ();
            // this prevents parser from recovering, however it can also interfere
            // with custom error handling (if present inside the grammar)
        	if (parser.getNumberOfSyntaxErrors() > 0) {
                throw new Exception("The parser reported a syntax error, antlrqueryparser hates errors!");
            }
            astTree = (AqpCommonTree) returnValue.getTree();
            return astTree.toQueryNodeTree();
        } catch (RecognitionException e) {
            throw new QueryNodeParseException(new MessageImpl(query + " " + parser.getErrorMessage(e, parser.getTokenNames())));
        } catch (Exception e) {
            Message message = new MessageImpl(
                    QueryParserMessages.INVALID_SYNTAX_CANNOT_PARSE, query,
                    e.getMessage());
            QueryNodeParseException ee = new QueryNodeParseException(e);
            ee.setQuery(query);
            ee.setNonLocalizedMessage(message);
            throw ee;
        } catch (Error e) {
            Message message = new MessageImpl(
                    QueryParserMessages.INVALID_SYNTAX_CANNOT_PARSE, query,
                    e.getMessage());
            QueryNodeParseException ee = new QueryNodeParseException(e);
            ee.setQuery(query);
            ee.setNonLocalizedMessage(message);
            throw ee;
        }
    }

}

            

            