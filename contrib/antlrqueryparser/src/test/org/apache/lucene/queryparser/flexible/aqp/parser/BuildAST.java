package org.apache.lucene.queryparser.flexible.aqp.parser;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.antlr.stringtemplate.*;
import java.lang.reflect.*;

import org.apache.lucene.queryparser.flexible.aqp.parser.*;

/**
 * A utility class for generating the dot/graph representations of the query
 * 
 * 
 */

// import
// org.apache.lucene.queryparser.flexible.aqp.parser.StandardLuceneGrammarLexer;
// import
// org.apache.lucene.queryparser.flexible.aqp.parser.StandardLuceneGrammarParser;

/*
 * Arguments: - grammar - query - rule-name (optional, default "mainQ") - action
 * (optional, default: dot)
 */
public class BuildAST {
  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws Exception {
    String grammar = args[0];
    String ruleName = "mainQ";
    String action = "dot";

    if (args.length > 2) {
      ruleName = args[2];
    }

    if (args.length > 3) {
      if (args[3].toLowerCase().equals("dot")) {
        action = "dot";
      } else if (args[3].toLowerCase().equals("tree")) {
        action = "tree";
      } else {
        throw new Exception("Unknown argument " + args[3]
            + ". Allowed actions: dot,tree");
      }
    }

    System.err.println("Grammar: " + grammar + " rule:" + ruleName
        + "\nquery: " + (args.length > 1 ? args[1] : "--"));
    String input = args.length > 1 ? args[1] : "No input given";

    ANTLRStringStream in = new ANTLRStringStream(input);
    // Lexer lexer = new StandardLuceneGrammarLexer(in);
    // CommonTokenStream ts = new CommonTokenStream(lexer);

    // System.err.println(ts.toString());

    // CommonTokenStream tokens = new CommonTokenStream(lexer);

    // StandardLuceneGrammarParser parser = new
    // StandardLuceneGrammarParser(tokens);
    // StandardLuceneGrammarParser.mainQ_return returnValue = parser.mainQ();
    // CommonTree tree = (CommonTree)returnValue.getTree();

    // get the Classes
    Class clsLexer = Class
        .forName("org.apache.lucene.queryparser.flexible.aqp.parser." + grammar
            + "Lexer");
    Class clsParser = Class
        .forName("org.apache.lucene.queryparser.flexible.aqp.parser." + grammar
            + "Parser");

    // insantiate lexer with one parameter
    Class partypes[] = new Class[1];
    partypes[0] = CharStream.class;
    Constructor ctLexer = clsLexer.getConstructor(partypes);

    Object arglist[] = new Object[1];
    arglist[0] = in;
    Object iLexer = ctLexer.newInstance(arglist);

    // get tokens
    CommonTokenStream tokens = new CommonTokenStream(
        (TokenSource) clsLexer.cast(iLexer));

    // instantiate parser using parameters
    Class partypes2[] = new Class[1];
    partypes2[0] = TokenStream.class;
    Constructor ct = clsParser.getConstructor(partypes2);

    Object arglist2[] = new Object[1];
    arglist2[0] = tokens;
    Object iParser = ct.newInstance(arglist2);

    // call the mainQ parser rule
    Method iParserMainQ = clsParser.getDeclaredMethod(ruleName);
    Object retVal = iParserMainQ.invoke(iParser);
    Method getMethod = iParserMainQ.getReturnType().getMethod("getTree");
    CommonTree tree = (CommonTree) (getMethod.invoke(retVal));

    // print the output
    if (action.equals("dot")) {
      DOTTreeGenerator gen = new DOTTreeGenerator();
      StringTemplate st = gen.toDOT(tree);
      System.out.println(st);
    } else if (action.equals("tree")) {
      System.out.println(tree.toStringTree());
    }
  }
}
