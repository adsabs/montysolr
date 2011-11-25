package org.apache.lucene.queryParser.aqp.parser;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeAdaptor;
import org.apache.lucene.messages.Message;
import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.parser.SyntaxParser;

import org.apache.lucene.queryParser.aqp.AqpCommonTree;
import org.apache.lucene.queryParser.aqp.AqpCommonTreeAdaptor;
import org.apache.lucene.queryParser.aqp.parser.StandardLuceneGrammarLexer;
import org.apache.lucene.queryParser.aqp.parser.StandardLuceneGrammarParser;

public class AqpSyntaxParser implements SyntaxParser {
	
	private Class clsLexer;
	private Class clsParser;
	
	private Object iLexer;
	private Object iParser;
	
	private Method invokeMainQ;
    private Method getTreeMethod;
    
    private Lexer lexer;
    private Parser parser;
    
    private String[] tokenNames;
	
    public AqpSyntaxParser() {
    	// empty constructor
    }
    
	public AqpSyntaxParser initializeGrammar(String grammarName) throws Exception {
		
		// get the Classes
        clsLexer = Class.forName("org.apache.lucene.queryParser.aqp.parser." + grammarName + "Lexer");
        clsParser = Class.forName("org.apache.lucene.queryParser.aqp.parser." + grammarName + "Parser");
        
        // instantiate lexer with no parameter
        Class partypes[] = new Class[0];
        //partypes[0] = CharStream.class;
        Constructor ctLexer = clsLexer.getConstructor(partypes);    
        Object arglist[] = new Object[0];
        iLexer = ctLexer.newInstance(arglist);
        
        // instantiate parser using no parameters
        ANTLRStringStream fakeInput = new ANTLRStringStream("none");
        CommonTokenStream fakeTokens = new CommonTokenStream((TokenSource) clsLexer.cast(iLexer));
        Class partypes2[] = new Class[1];
        partypes2[0] = TokenStream.class;
        Constructor ct = clsParser.getConstructor(partypes2);         
        iParser = ct.newInstance(fakeTokens);
        
        parser = (Parser) iParser;
        lexer = (Lexer) iLexer;
        
        // get tokenNames
        Method getTokenNames = clsParser.getDeclaredMethod("getTokenNames");
        tokenNames = (String[]) getTokenNames.invoke(iParser);
        
        // create adaptor
        AqpCommonTreeAdaptor adaptor = new AqpCommonTreeAdaptor(tokenNames);
        
        // set adaptor
        Method setTreeAdaptor = clsParser.getDeclaredMethod("setTreeAdaptor", TreeAdaptor.class);
        setTreeAdaptor.invoke(iParser, adaptor);
        
        
        // get the mainQ parser rule & return value 
        invokeMainQ= clsParser.getDeclaredMethod("mainQ");
        getTreeMethod = invokeMainQ.getReturnType().getMethod("getTree");
        
        //AqpCommonTree ast = parseTest("hey:joe");
        
        return this;
        
	}
	
	/**
	 * This parse method uses reflection so that it can load any grammar
	 * without knowing in advance its name
	 */
	@Override
	public QueryNode parse(CharSequence query, CharSequence field)
			throws QueryNodeParseException {
		
		ANTLRStringStream input = new ANTLRStringStream(query.toString());		
		lexer.setCharStream(input);
		
		// get tokens
        CommonTokenStream tokens = new CommonTokenStream((TokenSource) clsLexer.cast(iLexer));
        // set tokens
        parser.setTokenStream(tokens);
        // get tree back
		Object retVal;
		AqpCommonTree astTree;
		
		try {
			retVal = invokeMainQ.invoke(iParser);
			astTree = (AqpCommonTree) (getTreeMethod.invoke(retVal));
		} catch (Error e) {
			Message message = new MessageImpl(
					QueryParserMessages.INVALID_SYNTAX_CANNOT_PARSE, query,
					e.getMessage());
			QueryNodeParseException ee = new QueryNodeParseException(e);
			ee.setQuery(query);
			ee.setNonLocalizedMessage(message);
			throw ee;
		} catch (Exception e) { //TODO: these exceptions are from the code, should not be printed
			e.printStackTrace();
			QueryNodeParseException ee = new QueryNodeParseException(e);
			throw ee;
		}
		
		try {
			return astTree.toQueryNodeTree();
		}
		catch (RecognitionException e) {
			throw new QueryNodeParseException(new MessageImpl(query + " >> " + parser.getErrorMessage(e, parser.getTokenNames())));
		}
		
	}

	/**
	 * Parses a query and returns QueryNode tree. It does not use reflection, it is here for a
	 * testing purposes only. It will be moved to a standard class.
	 * 
	 * @param query
	 * @param field
	 * @return
	 * @throws QueryNodeParseException
	 */
	public QueryNode parse_normal(CharSequence query, CharSequence field)
			throws QueryNodeParseException {

		ANTLRStringStream in = new ANTLRStringStream(query.toString());
		StandardLuceneGrammarLexer lexer = new StandardLuceneGrammarLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		StandardLuceneGrammarParser parser = new StandardLuceneGrammarParser(
				tokens);
		StandardLuceneGrammarParser.mainQ_return returnValue;
		
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
