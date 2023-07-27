package org.apache.lucene.queryparser.flexible.aqp;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.TreeAdaptor;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardLuceneParser;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpCommonTree;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpCommonTreeAdaptor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.Message;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * This implementation can load any AST grammar from the repository of grammars
 * without a need to provide a Java implementation. It uses reflection, so it
 * might be slower than a dedicated parsing class.
 * <p>
 * Every grammar must have a top-level rule called <b>mainQ</b>
 * <p>
 * And every grammar must return AST.
 * <p>
 * If you know that you are going to instantiate specific parser, then
 * you should not use this generic class.
 *
 * @see AqpSyntaxParserAbstract
 * @see AqpStandardLuceneParser#init()
 */
public class AqpSyntaxParserLoadableImpl extends AqpSyntaxParserAbstract {

    @SuppressWarnings("rawtypes")
    private Class clsLexer;
    @SuppressWarnings("rawtypes")
    private Class clsParser;

    private Object iLexer;
    private Object iParser;

    private Method invokeMainQ;
    private Method getTreeMethod;
    private Method getNumberOfSyntaxErrorsMethod;

    private Lexer lexer;
    private Parser parser;

    private String[] tokenNames;

    public AqpSyntaxParserLoadableImpl() {
        // empty constructor
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AqpSyntaxParser initializeGrammar(String grammarName)
            throws QueryNodeParseException {

        try {
            // get the Classes
            clsLexer = Class
                    .forName("org.apache.lucene.queryparser.flexible.aqp.parser."
                            + grammarName + "Lexer");
            clsParser = Class
                    .forName("org.apache.lucene.queryparser.flexible.aqp.parser."
                            + grammarName + "Parser");

            // instantiate lexer with no parameter
            Class[] partypes = new Class[0];
            // partypes[0] = CharStream.class;
            Constructor ctLexer = clsLexer.getConstructor(partypes);
            Object[] arglist = new Object[0];
            iLexer = ctLexer.newInstance(arglist);

            // instantiate parser using no parameters
            // ANTLRStringStream fakeInput = new ANTLRStringStream("none");
            CommonTokenStream fakeTokens = new CommonTokenStream(
                    (TokenSource) clsLexer.cast(iLexer));
            Class[] partypes2 = new Class[1];
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
            Method setTreeAdaptor = clsParser.getDeclaredMethod("setTreeAdaptor",
                    TreeAdaptor.class);
            setTreeAdaptor.invoke(iParser, adaptor);

            // get the mainQ parser rule & return value
            invokeMainQ = clsParser.getDeclaredMethod("mainQ");
            getTreeMethod = invokeMainQ.getReturnType().getMethod("getTree");
            getNumberOfSyntaxErrorsMethod = clsParser
                    .getMethod("getNumberOfSyntaxErrors");

            // AqpCommonTree ast = parseTest("hey:joe");

            return this;

        } catch (Exception e) {
            e.printStackTrace();
            throw new QueryNodeParseException(e);
        }

    }

    public TokenStream getTokenStream(CharSequence query) {
        ANTLRStringStream input = new ANTLRStringStream(query.toString());
        lexer.setCharStream(input);

        // get tokens
        CommonTokenStream tokens = new CommonTokenStream(
                (TokenSource) clsLexer.cast(iLexer));
        return tokens;
    }

    public QueryNode parseTokenStream(TokenStream tokens, CharSequence query,
                                      CharSequence field) throws QueryNodeParseException {

        // set tokens
        parser.setTokenStream(tokens);

        // get tree back
        Object retVal;
        AqpCommonTree astTree;

        try {
            retVal = invokeMainQ.invoke(iParser);
            astTree = (AqpCommonTree) (getTreeMethod.invoke(retVal));

            // this prevents parser from recovering, however it can also interfere
            // with custom error handling (if present inside the grammar)
            Object errNo = getNumberOfSyntaxErrorsMethod.invoke(iParser);

            if (errNo instanceof Integer && ((Integer) errNo > 0)) {
                throw new Error(
                        "The parser reported a syntax error, antlrqueryparser hates errors!");
            }
        } catch (Error e) {
            Message message = new MessageImpl(
                    QueryParserMessages.INVALID_SYNTAX_CANNOT_PARSE, query,
                    e.getMessage());
            QueryNodeParseException ee = new QueryNodeParseException(e);
            ee.setQuery(query);
            ee.setNonLocalizedMessage(message);
            throw ee;
        } catch (Exception e) { // TODO: these exceptions are from the code, should
            // not be printed
            // e.printStackTrace();
            QueryNodeParseException ee = new QueryNodeParseException(e);
            throw ee;
        }

        try {
            return astTree.toQueryNodeTree();
        } catch (RecognitionException e) {
            throw new QueryNodeParseException(new MessageImpl(query + " >> "
                    + parser.getErrorMessage(e, parser.getTokenNames())));
        }

    }

}
