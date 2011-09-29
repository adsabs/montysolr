package org.apache.lucene.queryParser.aqp.parser;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.antlr.stringtemplate.*;
import java.lang.reflect.*;

import org.apache.lucene.queryParser.aqp.parser.*;

//import org.apache.lucene.queryParser.aqp.parser.StandardLuceneGrammarLexer;
//import org.apache.lucene.queryParser.aqp.parser.StandardLuceneGrammarParser;

public class BuildAST {
    @SuppressWarnings("unchecked")
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
        
        
        
        // insantiate lexer with one parameter
        Class partypes[] = new Class[1];
        partypes[0] = CharStream.class;
        Constructor ctLexer 
          = clsLexer.getConstructor(partypes);
        
        Object arglist[] = new Object[1];
        arglist[0] = in;
        Object iLexer = ctLexer.newInstance(arglist);
        
        
        
        // get tokens
        CommonTokenStream tokens = new CommonTokenStream((TokenSource) clsLexer.cast(iLexer));
        
        
        // instantiate parser using parameters
        Class partypes2[] = new Class[1];
         partypes2[0] = TokenStream.class;
         Constructor ct 
           = clsParser.getConstructor(partypes2);
         
         Object arglist2[] = new Object[1];
         arglist2[0] = tokens;
         Object iParser = ct.newInstance(arglist2);
         
         
        // call the mainQ parser rule 
        Method iParserMainQ= clsParser.getDeclaredMethod("mainQ");
        Object retVal = iParserMainQ.invoke(iParser);
        Method getMethod = iParserMainQ.getReturnType().getMethod("getTree");
        CommonTree tree = (CommonTree) (getMethod.invoke(retVal));
        
        
        // print the AST tree
        DOTTreeGenerator gen = new DOTTreeGenerator();
        StringTemplate st = gen.toDOT(tree);
        System.out.println(st);
    }
}

