package org.apache.lucene.queryParser.aqp.parser;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.antlr.stringtemplate.*;
import java.lang.reflect.*;

import org.apache.lucene.queryParser.aqp.parser.*;

//import org.apache.lucene.queryParser.aqp.parser.StandardLuceneGrammarLexer;
//import org.apache.lucene.queryParser.aqp.parser.StandardLuceneGrammarParser;

public class BuildAST {
    //@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
    	String grammar = args[0];
    	
        System.err.println("Grammar: "  + args[0] + "\nquery: " + (args.length > 1 ? args[1] : "--"));
        String input = args.length > 1 ? args[1] : "No input given";
        	
        ANTLRStringStream in = new ANTLRStringStream(input);
        //Lexer lexer = new StandardLuceneGrammarLexer(in);
        //CommonTokenStream ts = new CommonTokenStream(lexer);
        
        
        //System.err.println(ts.toString());
        
        //CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        //StandardLuceneGrammarParser parser = new StandardLuceneGrammarParser(tokens);
        //StandardLuceneGrammarParser.mainQ_return returnValue = parser.mainQ();
        //CommonTree tree = (CommonTree)returnValue.getTree();
        
        
        // get the Classes
        Class clsLexer = Class.forName("org.apache.lucene.queryParser.aqp.parser." + grammar + "Lexer");
        Class clsParser = Class.forName("org.apache.lucene.queryParser.aqp.parser." + grammar + "Parser");
        
        
        System.err.println("xxx" + clsParser);
        
        // insantiate lexer 
        Class partypes[] = new Class[1];
        partypes[0] = CharStream.class;
        Constructor ctLexer 
          = clsLexer.getConstructor(partypes);
        
        Object arglist[] = new Object[1];
        arglist[0] = in;
        Object iLexer = ctLexer.newInstance(arglist);
        
        //Object iLexer = clsLexer.newInstance();
        
        System.err.println(clsLexer.cast(iLexer));
        ((Lexer) clsLexer.cast(iLexer)).setText(input);
        
        //Method iLexerMethod = clsLexer.getDeclaredMethod("setText");
        //iLexerMethod.invoke(iLexer, input);
        
        CommonTokenStream tokens = new CommonTokenStream((TokenSource) clsLexer.cast(iLexer));
        
        //System.err.println(tokens.toString());
        
        // parse and get the AST tree
        
        Class partypes2[] = new Class[1];
         partypes2[0] = TokenStream.class;
         Constructor ct 
           = clsParser.getConstructor(partypes2);
         
         Object arglist2[] = new Object[1];
         arglist2[0] = tokens;
         Object iParser = ct.newInstance(arglist2);
         
         
        //Object iParser = clsParser.newInstance();
        //Method iParserSetTokens= clsLexer.getDeclaredMethod("setTokenStream");
        ((Parser)iParser).setTokenStream(tokens);
         
        Method iParserMainQ= clsParser.getDeclaredMethod("mainQ");
        System.err.println(iParserMainQ);
        
        Object retVal = iParserMainQ.invoke(iParser);
        System.err.println(iParserMainQ.getReturnType().cast(retVal));
        
        Method getMethod = iParserMainQ.getReturnType().getMethod("getTree");
        
        CommonTree tree = (CommonTree) (getMethod.invoke(retVal));
        
        
        DOTTreeGenerator gen = new DOTTreeGenerator();
        StringTemplate st = gen.toDOT(tree);
        System.out.println(st);
    }
}

