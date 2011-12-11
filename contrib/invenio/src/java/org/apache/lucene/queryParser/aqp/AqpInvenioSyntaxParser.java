package org.apache.lucene.queryParser.aqp;



import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.apache.lucene.messages.Message;
import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.AqpCommonTree;
import org.apache.lucene.queryParser.aqp.AqpCommonTreeAdaptor;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

import org.apache.lucene.queryParser.aqp.AqpSyntaxParser;
import org.apache.lucene.queryParser.aqp.AqpSyntaxParserAbstract;
import org.apache.lucene.queryParser.aqp.parser.FixInvenioLexer;
import org.apache.lucene.queryParser.aqp.parser.FixInvenioParser;
import org.apache.lucene.queryParser.aqp.parser.InvenioLexer;
import org.apache.lucene.queryParser.aqp.parser.InvenioParser;

/**
 * A modified version of the default parser, it uses two grammars.
 * {@link FixInvenioSyntaxParser} first to fix the syntactic ambiguities
 * that are allowed with Invenio. Then we use the {@link InvenioParser}
 * to process the cleaned up query.
 * 
 * @author rchyla
 *
 */

public class AqpInvenioSyntaxParser extends AqpSyntaxParserAbstract {
    
    
    @Override
    public AqpSyntaxParser initializeGrammar(String grammarName)
            throws QueryNodeParseException {
        return this;
    }

	@Override
	public TokenStream getTokenStream(CharSequence input) throws QueryNodeParseException {
		ANTLRStringStream in = new ANTLRStringStream(input.toString());
		FixInvenioLexer fLexer = new FixInvenioLexer(in);
		FixInvenioParser fParser = new FixInvenioParser(new CommonTokenStream(fLexer));
		
		try {
			fParser.mainQ();
		} catch (RecognitionException e) {
			throw new QueryNodeParseException(new MessageImpl(input + " " + 
					fParser.getErrorMessage(e, fParser.getTokenNames())));
		}
		
		in = new ANTLRStringStream(fParser.corrected.toString());
        InvenioLexer lexer = new InvenioLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        return tokens;
	}

	@Override
	public QueryNode parseTokenStream(TokenStream tokens, CharSequence query,
			CharSequence field) throws QueryNodeParseException {
		InvenioParser parser = new InvenioParser(tokens);
        InvenioParser.mainQ_return returnValue;
        
        AqpCommonTreeAdaptor adaptor = new AqpCommonTreeAdaptor(parser.getTokenNames());
        parser.setTreeAdaptor(adaptor);
        
        AqpCommonTree astTree;
        
        try {
            returnValue = parser.mainQ();
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

            