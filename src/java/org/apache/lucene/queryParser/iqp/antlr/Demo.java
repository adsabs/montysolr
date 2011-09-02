import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.antlr.stringtemplate.*;

public class Demo {
    public static void main(String[] args) throws Exception {
        System.err.println("Java received:" + (args.length > 0 ? args[0] : ""));
        ANTLRStringStream in = new ANTLRStringStream(args.length > 0 ? args[0] : "No input given");
        StandardLuceneGrammarLexer lexer = new StandardLuceneGrammarLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        StandardLuceneGrammarParser parser = new StandardLuceneGrammarParser(tokens);
        StandardLuceneGrammarParser.mainQ_return returnValue = parser.mainQ();
        CommonTree tree = (CommonTree)returnValue.getTree();
        DOTTreeGenerator gen = new DOTTreeGenerator();
        StringTemplate st = gen.toDOT(tree);
        System.out.println(st);
    }
}

