// $ANTLR 3.4 StandardLuceneGrammar.g 2011-11-01 22:10:30

   package org.apache.lucene.queryParser.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class StandardLuceneGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ADDED", "AMPER", "AND", "ATOM", "BOOST", "CARAT", "CLAUSE", "COLON", "DATE_TOKEN", "DQUOTE", "ESC_CHAR", "FIELD", "FUZZY", "INT", "LBRACK", "LCURLY", "LPAREN", "MINUS", "MODIFIER", "MULTIATOM", "MULTITERM", "NEAR", "NORMAL_CHAR", "NOT", "NUCLEUS", "NUMBER", "OPERATOR", "OR", "PHRASE", "PHRASE_ANYTHING", "PLUS", "QANYTHING", "QDATE", "QMARK", "QNORMAL", "QPHRASE", "QPHRASETRUNC", "QRANGEEX", "QRANGEIN", "QTRUNCATED", "RBRACK", "RCURLY", "RPAREN", "SQUOTE", "STAR", "TERM_NORMAL", "TERM_TRUNCATED", "TILDE", "TMODIFIER", "TO", "VALUE", "VBAR", "WS", "'/'"
    };

    public static final int EOF=-1;
    public static final int T__57=57;
    public static final int ADDED=4;
    public static final int AMPER=5;
    public static final int AND=6;
    public static final int ATOM=7;
    public static final int BOOST=8;
    public static final int CARAT=9;
    public static final int CLAUSE=10;
    public static final int COLON=11;
    public static final int DATE_TOKEN=12;
    public static final int DQUOTE=13;
    public static final int ESC_CHAR=14;
    public static final int FIELD=15;
    public static final int FUZZY=16;
    public static final int INT=17;
    public static final int LBRACK=18;
    public static final int LCURLY=19;
    public static final int LPAREN=20;
    public static final int MINUS=21;
    public static final int MODIFIER=22;
    public static final int MULTIATOM=23;
    public static final int MULTITERM=24;
    public static final int NEAR=25;
    public static final int NORMAL_CHAR=26;
    public static final int NOT=27;
    public static final int NUCLEUS=28;
    public static final int NUMBER=29;
    public static final int OPERATOR=30;
    public static final int OR=31;
    public static final int PHRASE=32;
    public static final int PHRASE_ANYTHING=33;
    public static final int PLUS=34;
    public static final int QANYTHING=35;
    public static final int QDATE=36;
    public static final int QMARK=37;
    public static final int QNORMAL=38;
    public static final int QPHRASE=39;
    public static final int QPHRASETRUNC=40;
    public static final int QRANGEEX=41;
    public static final int QRANGEIN=42;
    public static final int QTRUNCATED=43;
    public static final int RBRACK=44;
    public static final int RCURLY=45;
    public static final int RPAREN=46;
    public static final int SQUOTE=47;
    public static final int STAR=48;
    public static final int TERM_NORMAL=49;
    public static final int TERM_TRUNCATED=50;
    public static final int TILDE=51;
    public static final int TMODIFIER=52;
    public static final int TO=53;
    public static final int VALUE=54;
    public static final int VBAR=55;
    public static final int WS=56;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public StandardLuceneGrammarParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public StandardLuceneGrammarParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return StandardLuceneGrammarParser.tokenNames; }
    public String getGrammarFileName() { return "StandardLuceneGrammar.g"; }


    public static class mainQ_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mainQ"
    // StandardLuceneGrammar.g:39:1: mainQ : ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
    public final StandardLuceneGrammarParser.mainQ_return mainQ() throws RecognitionException {
        StandardLuceneGrammarParser.mainQ_return retval = new StandardLuceneGrammarParser.mainQ_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseOr_return clauseOr1 =null;


        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // StandardLuceneGrammar.g:39:7: ( ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
            // StandardLuceneGrammar.g:40:2: ( clauseOr )+
            {
            // StandardLuceneGrammar.g:40:2: ( clauseOr )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= LBRACK && LA1_0 <= MINUS)||LA1_0==NUMBER||(LA1_0 >= PHRASE && LA1_0 <= PLUS)||LA1_0==QMARK||(LA1_0 >= STAR && LA1_0 <= TERM_TRUNCATED)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // StandardLuceneGrammar.g:40:2: clauseOr
            	    {
            	    pushFollow(FOLLOW_clauseOr_in_mainQ158);
            	    clauseOr1=clauseOr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr1.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            // AST REWRITE
            // elements: clauseOr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 40:12: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
            {
                // StandardLuceneGrammar.g:40:15: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OPERATOR, "DEFOP")
                , root_1);

                if ( !(stream_clauseOr.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_clauseOr.hasNext() ) {
                    adaptor.addChild(root_1, stream_clauseOr.nextTree());

                }
                stream_clauseOr.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "mainQ"


    public static class clauseOr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseOr"
    // StandardLuceneGrammar.g:44:1: clauseOr : (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* ;
    public final StandardLuceneGrammarParser.clauseOr_return clauseOr() throws RecognitionException {
        StandardLuceneGrammarParser.clauseOr_return retval = new StandardLuceneGrammarParser.clauseOr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseAnd_return first =null;

        StandardLuceneGrammarParser.clauseAnd_return others =null;

        StandardLuceneGrammarParser.or_return or2 =null;


        RewriteRuleSubtreeStream stream_clauseAnd=new RewriteRuleSubtreeStream(adaptor,"rule clauseAnd");
        RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
        try {
            // StandardLuceneGrammar.g:45:3: ( (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* )
            // StandardLuceneGrammar.g:45:5: (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
            {
            // StandardLuceneGrammar.g:45:5: (first= clauseAnd -> $first)
            // StandardLuceneGrammar.g:45:6: first= clauseAnd
            {
            pushFollow(FOLLOW_clauseAnd_in_clauseOr191);
            first=clauseAnd();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseAnd.add(first.getTree());

            // AST REWRITE
            // elements: first
            // token labels: 
            // rule labels: retval, first
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.tree:null);

            root_0 = (Object)adaptor.nil();
            // 45:22: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:45:33: ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==OR) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // StandardLuceneGrammar.g:45:34: or others= clauseAnd
            	    {
            	    pushFollow(FOLLOW_or_in_clauseOr200);
            	    or2=or();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_or.add(or2.getTree());

            	    pushFollow(FOLLOW_clauseAnd_in_clauseOr204);
            	    others=clauseAnd();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseAnd.add(others.getTree());

            	    // AST REWRITE
            	    // elements: clauseAnd
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 45:54: -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
            	    {
            	        // StandardLuceneGrammar.g:45:57: ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "OR")
            	        , root_1);

            	        if ( !(stream_clauseAnd.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_clauseAnd.hasNext() ) {
            	            adaptor.addChild(root_1, stream_clauseAnd.nextTree());

            	        }
            	        stream_clauseAnd.reset();

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;
            	    }

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "clauseOr"


    public static class clauseAnd_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseAnd"
    // StandardLuceneGrammar.g:48:1: clauseAnd : (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* ;
    public final StandardLuceneGrammarParser.clauseAnd_return clauseAnd() throws RecognitionException {
        StandardLuceneGrammarParser.clauseAnd_return retval = new StandardLuceneGrammarParser.clauseAnd_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseNot_return first =null;

        StandardLuceneGrammarParser.clauseNot_return others =null;

        StandardLuceneGrammarParser.and_return and3 =null;


        RewriteRuleSubtreeStream stream_clauseNot=new RewriteRuleSubtreeStream(adaptor,"rule clauseNot");
        RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");
        try {
            // StandardLuceneGrammar.g:49:3: ( (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* )
            // StandardLuceneGrammar.g:49:5: (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
            {
            // StandardLuceneGrammar.g:49:5: (first= clauseNot -> $first)
            // StandardLuceneGrammar.g:49:6: first= clauseNot
            {
            pushFollow(FOLLOW_clauseNot_in_clauseAnd233);
            first=clauseNot();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseNot.add(first.getTree());

            // AST REWRITE
            // elements: first
            // token labels: 
            // rule labels: retval, first
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.tree:null);

            root_0 = (Object)adaptor.nil();
            // 49:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:49:34: ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==AND) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // StandardLuceneGrammar.g:49:35: and others= clauseNot
            	    {
            	    pushFollow(FOLLOW_and_in_clauseAnd243);
            	    and3=and();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_and.add(and3.getTree());

            	    pushFollow(FOLLOW_clauseNot_in_clauseAnd247);
            	    others=clauseNot();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseNot.add(others.getTree());

            	    // AST REWRITE
            	    // elements: clauseNot
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 49:56: -> ^( OPERATOR[\"AND\"] ( clauseNot )+ )
            	    {
            	        // StandardLuceneGrammar.g:49:59: ^( OPERATOR[\"AND\"] ( clauseNot )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "AND")
            	        , root_1);

            	        if ( !(stream_clauseNot.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_clauseNot.hasNext() ) {
            	            adaptor.addChild(root_1, stream_clauseNot.nextTree());

            	        }
            	        stream_clauseNot.reset();

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;
            	    }

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "clauseAnd"


    public static class clauseNot_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseNot"
    // StandardLuceneGrammar.g:52:1: clauseNot : (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* ;
    public final StandardLuceneGrammarParser.clauseNot_return clauseNot() throws RecognitionException {
        StandardLuceneGrammarParser.clauseNot_return retval = new StandardLuceneGrammarParser.clauseNot_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseNear_return first =null;

        StandardLuceneGrammarParser.clauseNear_return others =null;

        StandardLuceneGrammarParser.not_return not4 =null;


        RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
        RewriteRuleSubtreeStream stream_clauseNear=new RewriteRuleSubtreeStream(adaptor,"rule clauseNear");
        try {
            // StandardLuceneGrammar.g:53:3: ( (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* )
            // StandardLuceneGrammar.g:53:5: (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
            {
            // StandardLuceneGrammar.g:53:5: (first= clauseNear -> $first)
            // StandardLuceneGrammar.g:53:6: first= clauseNear
            {
            pushFollow(FOLLOW_clauseNear_in_clauseNot278);
            first=clauseNear();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseNear.add(first.getTree());

            // AST REWRITE
            // elements: first
            // token labels: 
            // rule labels: retval, first
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.tree:null);

            root_0 = (Object)adaptor.nil();
            // 53:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:53:34: ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==AND) ) {
                    int LA4_1 = input.LA(2);

                    if ( (LA4_1==NOT) ) {
                        alt4=1;
                    }


                }
                else if ( (LA4_0==NOT) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // StandardLuceneGrammar.g:53:35: not others= clauseNear
            	    {
            	    pushFollow(FOLLOW_not_in_clauseNot287);
            	    not4=not();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_not.add(not4.getTree());

            	    pushFollow(FOLLOW_clauseNear_in_clauseNot291);
            	    others=clauseNear();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseNear.add(others.getTree());

            	    // AST REWRITE
            	    // elements: clauseNear
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 53:57: -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
            	    {
            	        // StandardLuceneGrammar.g:53:60: ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "NOT")
            	        , root_1);

            	        if ( !(stream_clauseNear.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_clauseNear.hasNext() ) {
            	            adaptor.addChild(root_1, stream_clauseNear.nextTree());

            	        }
            	        stream_clauseNear.reset();

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;
            	    }

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "clauseNot"


    public static class clauseNear_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseNear"
    // StandardLuceneGrammar.g:56:1: clauseNear : (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* ;
    public final StandardLuceneGrammarParser.clauseNear_return clauseNear() throws RecognitionException {
        StandardLuceneGrammarParser.clauseNear_return retval = new StandardLuceneGrammarParser.clauseNear_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseBasic_return first =null;

        StandardLuceneGrammarParser.clauseBasic_return others =null;

        StandardLuceneGrammarParser.near_return near5 =null;


        RewriteRuleSubtreeStream stream_clauseBasic=new RewriteRuleSubtreeStream(adaptor,"rule clauseBasic");
        RewriteRuleSubtreeStream stream_near=new RewriteRuleSubtreeStream(adaptor,"rule near");
        try {
            // StandardLuceneGrammar.g:57:3: ( (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* )
            // StandardLuceneGrammar.g:57:5: (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
            {
            // StandardLuceneGrammar.g:57:5: (first= clauseBasic -> $first)
            // StandardLuceneGrammar.g:57:6: first= clauseBasic
            {
            pushFollow(FOLLOW_clauseBasic_in_clauseNear322);
            first=clauseBasic();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseBasic.add(first.getTree());

            // AST REWRITE
            // elements: first
            // token labels: 
            // rule labels: retval, first
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.tree:null);

            root_0 = (Object)adaptor.nil();
            // 57:24: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:57:35: ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==NEAR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // StandardLuceneGrammar.g:57:36: near others= clauseBasic
            	    {
            	    pushFollow(FOLLOW_near_in_clauseNear331);
            	    near5=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near5.getTree());

            	    pushFollow(FOLLOW_clauseBasic_in_clauseNear335);
            	    others=clauseBasic();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseBasic.add(others.getTree());

            	    // AST REWRITE
            	    // elements: clauseBasic, near
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 57:60: -> ^( near ( clauseBasic )+ )
            	    {
            	        // StandardLuceneGrammar.g:57:63: ^( near ( clauseBasic )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(stream_near.nextNode(), root_1);

            	        if ( !(stream_clauseBasic.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_clauseBasic.hasNext() ) {
            	            adaptor.addChild(root_1, stream_clauseBasic.nextTree());

            	        }
            	        stream_clauseBasic.reset();

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;
            	    }

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "clauseNear"


    public static class clauseBasic_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseBasic"
    // StandardLuceneGrammar.g:60:1: clauseBasic : ( ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom );
    public final StandardLuceneGrammarParser.clauseBasic_return clauseBasic() throws RecognitionException {
        StandardLuceneGrammarParser.clauseBasic_return retval = new StandardLuceneGrammarParser.clauseBasic_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN7=null;
        Token RPAREN9=null;
        Token LPAREN12=null;
        Token RPAREN14=null;
        Token LPAREN16=null;
        Token RPAREN18=null;
        StandardLuceneGrammarParser.modifier_return modifier6 =null;

        StandardLuceneGrammarParser.clauseOr_return clauseOr8 =null;

        StandardLuceneGrammarParser.term_modifier_return term_modifier10 =null;

        StandardLuceneGrammarParser.modifier_return modifier11 =null;

        StandardLuceneGrammarParser.clauseOr_return clauseOr13 =null;

        StandardLuceneGrammarParser.term_modifier_return term_modifier15 =null;

        StandardLuceneGrammarParser.clauseOr_return clauseOr17 =null;

        StandardLuceneGrammarParser.atom_return atom19 =null;


        Object LPAREN7_tree=null;
        Object RPAREN9_tree=null;
        Object LPAREN12_tree=null;
        Object RPAREN14_tree=null;
        Object LPAREN16_tree=null;
        Object RPAREN18_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // StandardLuceneGrammar.g:61:2: ( ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom )
            int alt13=4;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                int LA13_1 = input.LA(2);

                if ( (synpred1_StandardLuceneGrammar()) ) {
                    alt13=1;
                }
                else if ( (synpred2_StandardLuceneGrammar()) ) {
                    alt13=2;
                }
                else if ( (true) ) {
                    alt13=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;

                }
                }
                break;
            case MINUS:
                {
                int LA13_2 = input.LA(2);

                if ( (synpred1_StandardLuceneGrammar()) ) {
                    alt13=1;
                }
                else if ( (synpred2_StandardLuceneGrammar()) ) {
                    alt13=2;
                }
                else if ( (true) ) {
                    alt13=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 2, input);

                    throw nvae;

                }
                }
                break;
            case LPAREN:
                {
                int LA13_3 = input.LA(2);

                if ( (synpred1_StandardLuceneGrammar()) ) {
                    alt13=1;
                }
                else if ( (synpred2_StandardLuceneGrammar()) ) {
                    alt13=2;
                }
                else if ( (synpred3_StandardLuceneGrammar()) ) {
                    alt13=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 3, input);

                    throw nvae;

                }
                }
                break;
            case LBRACK:
            case LCURLY:
            case NUMBER:
            case PHRASE:
            case PHRASE_ANYTHING:
            case QMARK:
            case STAR:
            case TERM_NORMAL:
            case TERM_TRUNCATED:
                {
                alt13=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }

            switch (alt13) {
                case 1 :
                    // StandardLuceneGrammar.g:62:2: ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )?
                    {
                    // StandardLuceneGrammar.g:62:40: ( modifier )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==MINUS||LA6_0==PLUS) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // StandardLuceneGrammar.g:62:40: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_clauseBasic377);
                            modifier6=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier6.getTree());

                            }
                            break;

                    }


                    LPAREN7=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic380); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN7);


                    // StandardLuceneGrammar.g:62:57: ( clauseOr )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0 >= LBRACK && LA7_0 <= MINUS)||LA7_0==NUMBER||(LA7_0 >= PHRASE && LA7_0 <= PLUS)||LA7_0==QMARK||(LA7_0 >= STAR && LA7_0 <= TERM_TRUNCATED)) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:62:57: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic382);
                    	    clauseOr8=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr8.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    RPAREN9=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic385); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN9);


                    // StandardLuceneGrammar.g:62:74: ( term_modifier )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==CARAT||LA8_0==TILDE) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // StandardLuceneGrammar.g:62:74: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_clauseBasic387);
                            term_modifier10=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier10.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: clauseOr, term_modifier, modifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 63:3: -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                    {
                        // StandardLuceneGrammar.g:63:6: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:63:17: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // StandardLuceneGrammar.g:63:27: ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:63:39: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // StandardLuceneGrammar.g:63:54: ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_3);

                        // StandardLuceneGrammar.g:63:63: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
                        {
                        Object root_4 = (Object)adaptor.nil();
                        root_4 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(OPERATOR, "DEFOP")
                        , root_4);

                        if ( !(stream_clauseOr.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_clauseOr.hasNext() ) {
                            adaptor.addChild(root_4, stream_clauseOr.nextTree());

                        }
                        stream_clauseOr.reset();

                        adaptor.addChild(root_3, root_4);
                        }

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:64:4: ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )?
                    {
                    // StandardLuceneGrammar.g:64:46: ( modifier )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==MINUS||LA9_0==PLUS) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // StandardLuceneGrammar.g:64:46: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_clauseBasic437);
                            modifier11=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier11.getTree());

                            }
                            break;

                    }


                    LPAREN12=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic440); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN12);


                    // StandardLuceneGrammar.g:64:63: ( clauseOr )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0 >= LBRACK && LA10_0 <= MINUS)||LA10_0==NUMBER||(LA10_0 >= PHRASE && LA10_0 <= PLUS)||LA10_0==QMARK||(LA10_0 >= STAR && LA10_0 <= TERM_TRUNCATED)) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:64:63: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic442);
                    	    clauseOr13=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr13.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt10 >= 1 ) break loop10;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);


                    RPAREN14=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic445); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN14);


                    // StandardLuceneGrammar.g:64:80: ( term_modifier )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==CARAT||LA11_0==TILDE) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // StandardLuceneGrammar.g:64:80: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_clauseBasic447);
                            term_modifier15=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier15.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: clauseOr, term_modifier, modifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 65:3: -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                    {
                        // StandardLuceneGrammar.g:65:6: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:65:17: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // StandardLuceneGrammar.g:65:27: ^( TMODIFIER ( term_modifier )? ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:65:39: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // StandardLuceneGrammar.g:65:54: ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_3);

                        // StandardLuceneGrammar.g:65:63: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
                        {
                        Object root_4 = (Object)adaptor.nil();
                        root_4 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(OPERATOR, "DEFOP")
                        , root_4);

                        if ( !(stream_clauseOr.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_clauseOr.hasNext() ) {
                            adaptor.addChild(root_4, stream_clauseOr.nextTree());

                        }
                        stream_clauseOr.reset();

                        adaptor.addChild(root_3, root_4);
                        }

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // StandardLuceneGrammar.g:66:4: ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN
                    {
                    LPAREN16=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic491); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN16);


                    // StandardLuceneGrammar.g:66:23: ( clauseOr )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0 >= LBRACK && LA12_0 <= MINUS)||LA12_0==NUMBER||(LA12_0 >= PHRASE && LA12_0 <= PLUS)||LA12_0==QMARK||(LA12_0 >= STAR && LA12_0 <= TERM_TRUNCATED)) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:66:23: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic493);
                    	    clauseOr17=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr17.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt12 >= 1 ) break loop12;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(12, input);
                                throw eee;
                        }
                        cnt12++;
                    } while (true);


                    RPAREN18=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic496); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN18);


                    // AST REWRITE
                    // elements: clauseOr
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 67:3: -> ( clauseOr )+
                    {
                        if ( !(stream_clauseOr.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_clauseOr.hasNext() ) {
                            adaptor.addChild(root_0, stream_clauseOr.nextTree());

                        }
                        stream_clauseOr.reset();

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // StandardLuceneGrammar.g:68:4: atom
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_atom_in_clauseBasic508);
                    atom19=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom19.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "clauseBasic"


    public static class atom_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "atom"
    // StandardLuceneGrammar.g:72:1: atom : ( ( modifier )? field multi_value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field ^( VALUE multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? ^( VALUE value ) ) ) ) | ( STAR COLON )? STAR -> ^( QANYTHING STAR[\"*\"] ) );
    public final StandardLuceneGrammarParser.atom_return atom() throws RecognitionException {
        StandardLuceneGrammarParser.atom_return retval = new StandardLuceneGrammarParser.atom_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR28=null;
        Token COLON29=null;
        Token STAR30=null;
        StandardLuceneGrammarParser.modifier_return modifier20 =null;

        StandardLuceneGrammarParser.field_return field21 =null;

        StandardLuceneGrammarParser.multi_value_return multi_value22 =null;

        StandardLuceneGrammarParser.term_modifier_return term_modifier23 =null;

        StandardLuceneGrammarParser.modifier_return modifier24 =null;

        StandardLuceneGrammarParser.field_return field25 =null;

        StandardLuceneGrammarParser.value_return value26 =null;

        StandardLuceneGrammarParser.term_modifier_return term_modifier27 =null;


        Object STAR28_tree=null;
        Object COLON29_tree=null;
        Object STAR30_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_multi_value=new RewriteRuleSubtreeStream(adaptor,"rule multi_value");
        try {
            // StandardLuceneGrammar.g:73:2: ( ( modifier )? field multi_value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field ^( VALUE multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? ^( VALUE value ) ) ) ) | ( STAR COLON )? STAR -> ^( QANYTHING STAR[\"*\"] ) )
            int alt20=3;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                int LA20_1 = input.LA(2);

                if ( (LA20_1==TERM_NORMAL) ) {
                    int LA20_3 = input.LA(3);

                    if ( (LA20_3==COLON) ) {
                        int LA20_6 = input.LA(4);

                        if ( (LA20_6==LPAREN) ) {
                            alt20=1;
                        }
                        else if ( ((LA20_6 >= LBRACK && LA20_6 <= LCURLY)||LA20_6==NUMBER||(LA20_6 >= PHRASE && LA20_6 <= PHRASE_ANYTHING)||LA20_6==QMARK||(LA20_6 >= TERM_NORMAL && LA20_6 <= TERM_TRUNCATED)) ) {
                            alt20=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 20, 6, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA20_3==EOF||LA20_3==AND||LA20_3==CARAT||(LA20_3 >= LBRACK && LA20_3 <= MINUS)||LA20_3==NEAR||LA20_3==NOT||LA20_3==NUMBER||(LA20_3 >= OR && LA20_3 <= PLUS)||LA20_3==QMARK||LA20_3==RPAREN||(LA20_3 >= STAR && LA20_3 <= TILDE)) ) {
                        alt20=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 20, 3, input);

                        throw nvae;

                    }
                }
                else if ( ((LA20_1 >= LBRACK && LA20_1 <= LCURLY)||LA20_1==NUMBER||(LA20_1 >= PHRASE && LA20_1 <= PHRASE_ANYTHING)||LA20_1==QMARK||LA20_1==TERM_TRUNCATED) ) {
                    alt20=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 1, input);

                    throw nvae;

                }
                }
                break;
            case MINUS:
                {
                int LA20_2 = input.LA(2);

                if ( (LA20_2==TERM_NORMAL) ) {
                    int LA20_3 = input.LA(3);

                    if ( (LA20_3==COLON) ) {
                        int LA20_6 = input.LA(4);

                        if ( (LA20_6==LPAREN) ) {
                            alt20=1;
                        }
                        else if ( ((LA20_6 >= LBRACK && LA20_6 <= LCURLY)||LA20_6==NUMBER||(LA20_6 >= PHRASE && LA20_6 <= PHRASE_ANYTHING)||LA20_6==QMARK||(LA20_6 >= TERM_NORMAL && LA20_6 <= TERM_TRUNCATED)) ) {
                            alt20=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 20, 6, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA20_3==EOF||LA20_3==AND||LA20_3==CARAT||(LA20_3 >= LBRACK && LA20_3 <= MINUS)||LA20_3==NEAR||LA20_3==NOT||LA20_3==NUMBER||(LA20_3 >= OR && LA20_3 <= PLUS)||LA20_3==QMARK||LA20_3==RPAREN||(LA20_3 >= STAR && LA20_3 <= TILDE)) ) {
                        alt20=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 20, 3, input);

                        throw nvae;

                    }
                }
                else if ( ((LA20_2 >= LBRACK && LA20_2 <= LCURLY)||LA20_2==NUMBER||(LA20_2 >= PHRASE && LA20_2 <= PHRASE_ANYTHING)||LA20_2==QMARK||LA20_2==TERM_TRUNCATED) ) {
                    alt20=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 2, input);

                    throw nvae;

                }
                }
                break;
            case TERM_NORMAL:
                {
                int LA20_3 = input.LA(2);

                if ( (LA20_3==COLON) ) {
                    int LA20_6 = input.LA(3);

                    if ( (LA20_6==LPAREN) ) {
                        alt20=1;
                    }
                    else if ( ((LA20_6 >= LBRACK && LA20_6 <= LCURLY)||LA20_6==NUMBER||(LA20_6 >= PHRASE && LA20_6 <= PHRASE_ANYTHING)||LA20_6==QMARK||(LA20_6 >= TERM_NORMAL && LA20_6 <= TERM_TRUNCATED)) ) {
                        alt20=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 20, 6, input);

                        throw nvae;

                    }
                }
                else if ( (LA20_3==EOF||LA20_3==AND||LA20_3==CARAT||(LA20_3 >= LBRACK && LA20_3 <= MINUS)||LA20_3==NEAR||LA20_3==NOT||LA20_3==NUMBER||(LA20_3 >= OR && LA20_3 <= PLUS)||LA20_3==QMARK||LA20_3==RPAREN||(LA20_3 >= STAR && LA20_3 <= TILDE)) ) {
                    alt20=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 3, input);

                    throw nvae;

                }
                }
                break;
            case LBRACK:
            case LCURLY:
            case NUMBER:
            case PHRASE:
            case PHRASE_ANYTHING:
            case QMARK:
            case TERM_TRUNCATED:
                {
                alt20=2;
                }
                break;
            case STAR:
                {
                alt20=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;

            }

            switch (alt20) {
                case 1 :
                    // StandardLuceneGrammar.g:74:2: ( modifier )? field multi_value ( term_modifier )?
                    {
                    // StandardLuceneGrammar.g:74:2: ( modifier )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==MINUS||LA14_0==PLUS) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // StandardLuceneGrammar.g:74:2: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom529);
                            modifier20=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier20.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_field_in_atom532);
                    field21=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field21.getTree());

                    pushFollow(FOLLOW_multi_value_in_atom534);
                    multi_value22=multi_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_multi_value.add(multi_value22.getTree());

                    // StandardLuceneGrammar.g:74:30: ( term_modifier )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==CARAT||LA15_0==TILDE) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // StandardLuceneGrammar.g:74:30: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_atom536);
                            term_modifier23=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier23.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: term_modifier, modifier, multi_value, field
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 74:45: -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field ^( VALUE multi_value ) ) ) )
                    {
                        // StandardLuceneGrammar.g:74:48: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field ^( VALUE multi_value ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:74:59: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // StandardLuceneGrammar.g:74:69: ^( TMODIFIER ( term_modifier )? ^( FIELD field ^( VALUE multi_value ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:74:81: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // StandardLuceneGrammar.g:74:96: ^( FIELD field ^( VALUE multi_value ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_3);

                        adaptor.addChild(root_3, stream_field.nextTree());

                        // StandardLuceneGrammar.g:74:110: ^( VALUE multi_value )
                        {
                        Object root_4 = (Object)adaptor.nil();
                        root_4 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(VALUE, "VALUE")
                        , root_4);

                        adaptor.addChild(root_4, stream_multi_value.nextTree());

                        adaptor.addChild(root_3, root_4);
                        }

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:76:2: ( modifier )? ( field )? value ( term_modifier )?
                    {
                    // StandardLuceneGrammar.g:76:2: ( modifier )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==MINUS||LA16_0==PLUS) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // StandardLuceneGrammar.g:76:2: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom572);
                            modifier24=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier24.getTree());

                            }
                            break;

                    }


                    // StandardLuceneGrammar.g:76:12: ( field )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==TERM_NORMAL) ) {
                        int LA17_1 = input.LA(2);

                        if ( (LA17_1==COLON) ) {
                            alt17=1;
                        }
                    }
                    switch (alt17) {
                        case 1 :
                            // StandardLuceneGrammar.g:76:12: field
                            {
                            pushFollow(FOLLOW_field_in_atom575);
                            field25=field();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_field.add(field25.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_value_in_atom578);
                    value26=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value26.getTree());

                    // StandardLuceneGrammar.g:76:25: ( term_modifier )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==CARAT||LA18_0==TILDE) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // StandardLuceneGrammar.g:76:25: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_atom580);
                            term_modifier27=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier27.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: term_modifier, modifier, value, field
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 76:40: -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? ^( VALUE value ) ) ) )
                    {
                        // StandardLuceneGrammar.g:76:43: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? ^( VALUE value ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:76:54: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // StandardLuceneGrammar.g:76:64: ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? ^( VALUE value ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:76:76: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // StandardLuceneGrammar.g:76:91: ^( FIELD ( field )? ^( VALUE value ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_3);

                        // StandardLuceneGrammar.g:76:99: ( field )?
                        if ( stream_field.hasNext() ) {
                            adaptor.addChild(root_3, stream_field.nextTree());

                        }
                        stream_field.reset();

                        // StandardLuceneGrammar.g:76:106: ^( VALUE value )
                        {
                        Object root_4 = (Object)adaptor.nil();
                        root_4 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(VALUE, "VALUE")
                        , root_4);

                        adaptor.addChild(root_4, stream_value.nextTree());

                        adaptor.addChild(root_3, root_4);
                        }

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // StandardLuceneGrammar.g:77:4: ( STAR COLON )? STAR
                    {
                    // StandardLuceneGrammar.g:77:4: ( STAR COLON )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==STAR) ) {
                        int LA19_1 = input.LA(2);

                        if ( (LA19_1==COLON) ) {
                            alt19=1;
                        }
                    }
                    switch (alt19) {
                        case 1 :
                            // StandardLuceneGrammar.g:77:5: STAR COLON
                            {
                            STAR28=(Token)match(input,STAR,FOLLOW_STAR_in_atom616); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(STAR28);


                            COLON29=(Token)match(input,COLON,FOLLOW_COLON_in_atom618); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COLON.add(COLON29);


                            }
                            break;

                    }


                    STAR30=(Token)match(input,STAR,FOLLOW_STAR_in_atom622); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR30);


                    // AST REWRITE
                    // elements: STAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 77:23: -> ^( QANYTHING STAR[\"*\"] )
                    {
                        // StandardLuceneGrammar.g:77:26: ^( QANYTHING STAR[\"*\"] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QANYTHING, "QANYTHING")
                        , root_1);

                        adaptor.addChild(root_1, 
                        (Object)adaptor.create(STAR, "*")
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "atom"


    public static class field_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "field"
    // StandardLuceneGrammar.g:81:1: field : TERM_NORMAL COLON -> TERM_NORMAL ;
    public final StandardLuceneGrammarParser.field_return field() throws RecognitionException {
        StandardLuceneGrammarParser.field_return retval = new StandardLuceneGrammarParser.field_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_NORMAL31=null;
        Token COLON32=null;

        Object TERM_NORMAL31_tree=null;
        Object COLON32_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_TERM_NORMAL=new RewriteRuleTokenStream(adaptor,"token TERM_NORMAL");

        try {
            // StandardLuceneGrammar.g:82:2: ( TERM_NORMAL COLON -> TERM_NORMAL )
            // StandardLuceneGrammar.g:83:2: TERM_NORMAL COLON
            {
            TERM_NORMAL31=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_field649); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL31);


            COLON32=(Token)match(input,COLON,FOLLOW_COLON_in_field651); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(COLON32);


            // AST REWRITE
            // elements: TERM_NORMAL
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 83:20: -> TERM_NORMAL
            {
                adaptor.addChild(root_0, 
                stream_TERM_NORMAL.nextNode()
                );

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "field"


    public static class value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "value"
    // StandardLuceneGrammar.g:86:1: value : ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) );
    public final StandardLuceneGrammarParser.value_return value() throws RecognitionException {
        StandardLuceneGrammarParser.value_return retval = new StandardLuceneGrammarParser.value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token QMARK39=null;
        StandardLuceneGrammarParser.range_term_in_return range_term_in33 =null;

        StandardLuceneGrammarParser.range_term_ex_return range_term_ex34 =null;

        StandardLuceneGrammarParser.normal_return normal35 =null;

        StandardLuceneGrammarParser.truncated_return truncated36 =null;

        StandardLuceneGrammarParser.quoted_return quoted37 =null;

        StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated38 =null;


        Object QMARK39_tree=null;
        RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
        RewriteRuleSubtreeStream stream_range_term_ex=new RewriteRuleSubtreeStream(adaptor,"rule range_term_ex");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_range_term_in=new RewriteRuleSubtreeStream(adaptor,"rule range_term_in");
        try {
            // StandardLuceneGrammar.g:87:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) )
            int alt21=7;
            switch ( input.LA(1) ) {
            case LBRACK:
                {
                alt21=1;
                }
                break;
            case LCURLY:
                {
                alt21=2;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
                {
                alt21=3;
                }
                break;
            case TERM_TRUNCATED:
                {
                alt21=4;
                }
                break;
            case PHRASE:
                {
                alt21=5;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt21=6;
                }
                break;
            case QMARK:
                {
                alt21=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }

            switch (alt21) {
                case 1 :
                    // StandardLuceneGrammar.g:88:2: range_term_in
                    {
                    pushFollow(FOLLOW_range_term_in_in_value670);
                    range_term_in33=range_term_in();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_in.add(range_term_in33.getTree());

                    // AST REWRITE
                    // elements: range_term_in
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 88:16: -> ^( QRANGEIN range_term_in )
                    {
                        // StandardLuceneGrammar.g:88:19: ^( QRANGEIN range_term_in )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QRANGEIN, "QRANGEIN")
                        , root_1);

                        adaptor.addChild(root_1, stream_range_term_in.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:89:4: range_term_ex
                    {
                    pushFollow(FOLLOW_range_term_ex_in_value683);
                    range_term_ex34=range_term_ex();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_ex.add(range_term_ex34.getTree());

                    // AST REWRITE
                    // elements: range_term_ex
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 89:18: -> ^( QRANGEEX range_term_ex )
                    {
                        // StandardLuceneGrammar.g:89:21: ^( QRANGEEX range_term_ex )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QRANGEEX, "QRANGEEX")
                        , root_1);

                        adaptor.addChild(root_1, stream_range_term_ex.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // StandardLuceneGrammar.g:90:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_value697);
                    normal35=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal35.getTree());

                    // AST REWRITE
                    // elements: normal
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 90:11: -> ^( QNORMAL normal )
                    {
                        // StandardLuceneGrammar.g:90:14: ^( QNORMAL normal )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QNORMAL, "QNORMAL")
                        , root_1);

                        adaptor.addChild(root_1, stream_normal.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // StandardLuceneGrammar.g:91:4: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_value711);
                    truncated36=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated36.getTree());

                    // AST REWRITE
                    // elements: truncated
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 91:14: -> ^( QTRUNCATED truncated )
                    {
                        // StandardLuceneGrammar.g:91:17: ^( QTRUNCATED truncated )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QTRUNCATED, "QTRUNCATED")
                        , root_1);

                        adaptor.addChild(root_1, stream_truncated.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 5 :
                    // StandardLuceneGrammar.g:92:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_value725);
                    quoted37=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted37.getTree());

                    // AST REWRITE
                    // elements: quoted
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 92:11: -> ^( QPHRASE quoted )
                    {
                        // StandardLuceneGrammar.g:92:14: ^( QPHRASE quoted )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QPHRASE, "QPHRASE")
                        , root_1);

                        adaptor.addChild(root_1, stream_quoted.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 6 :
                    // StandardLuceneGrammar.g:93:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_value738);
                    quoted_truncated38=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated38.getTree());

                    // AST REWRITE
                    // elements: quoted_truncated
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 93:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // StandardLuceneGrammar.g:93:24: ^( QPHRASETRUNC quoted_truncated )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QPHRASETRUNC, "QPHRASETRUNC")
                        , root_1);

                        adaptor.addChild(root_1, stream_quoted_truncated.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 7 :
                    // StandardLuceneGrammar.g:94:4: QMARK
                    {
                    QMARK39=(Token)match(input,QMARK,FOLLOW_QMARK_in_value751); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QMARK.add(QMARK39);


                    // AST REWRITE
                    // elements: QMARK
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 94:10: -> ^( QTRUNCATED QMARK )
                    {
                        // StandardLuceneGrammar.g:94:13: ^( QTRUNCATED QMARK )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QTRUNCATED, "QTRUNCATED")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_QMARK.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "value"


    public static class range_term_in_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "range_term_in"
    // StandardLuceneGrammar.g:99:1: range_term_in : LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK ;
    public final StandardLuceneGrammarParser.range_term_in_return range_term_in() throws RecognitionException {
        StandardLuceneGrammarParser.range_term_in_return retval = new StandardLuceneGrammarParser.range_term_in_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LBRACK40=null;
        Token TO41=null;
        Token RBRACK42=null;
        StandardLuceneGrammarParser.range_value_return a =null;

        StandardLuceneGrammarParser.range_value_return b =null;


        Object LBRACK40_tree=null;
        Object TO41_tree=null;
        Object RBRACK42_tree=null;
        RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
        RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // StandardLuceneGrammar.g:100:2: ( LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK )
            // StandardLuceneGrammar.g:101:8: LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK
            {
            LBRACK40=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_range_term_in783); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK40);


            // StandardLuceneGrammar.g:102:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // StandardLuceneGrammar.g:102:9: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_in795);
            a=range_value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_range_value.add(a.getTree());

            // AST REWRITE
            // elements: range_value
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 102:23: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // StandardLuceneGrammar.g:102:38: ^( QANYTHING QANYTHING[\"*\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(QANYTHING, "QANYTHING")
                , root_1);

                adaptor.addChild(root_1, 
                (Object)adaptor.create(QANYTHING, "*")
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:103:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==DATE_TOKEN||LA23_0==NUMBER||(LA23_0 >= PHRASE && LA23_0 <= PHRASE_ANYTHING)||(LA23_0 >= STAR && LA23_0 <= TERM_TRUNCATED)||LA23_0==TO) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // StandardLuceneGrammar.g:103:10: ( TO )? b= range_value
                    {
                    // StandardLuceneGrammar.g:103:10: ( TO )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==TO) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // StandardLuceneGrammar.g:103:10: TO
                            {
                            TO41=(Token)match(input,TO,FOLLOW_TO_in_range_term_in818); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO41);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_in823);
                    b=range_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_value.add(b.getTree());

                    // AST REWRITE
                    // elements: b, a
                    // token labels: 
                    // rule labels: retval, b, a
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_b=new RewriteRuleSubtreeStream(adaptor,"rule b",b!=null?b.tree:null);
                    RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"rule a",a!=null?a.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 103:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // StandardLuceneGrammar.g:103:35: ( $b)?
                        if ( stream_b.hasNext() ) {
                            adaptor.addChild(root_0, stream_b.nextTree());

                        }
                        stream_b.reset();

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }


            RBRACK42=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_range_term_in844); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACK.add(RBRACK42);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "range_term_in"


    public static class range_term_ex_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "range_term_ex"
    // StandardLuceneGrammar.g:108:1: range_term_ex : LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY ;
    public final StandardLuceneGrammarParser.range_term_ex_return range_term_ex() throws RecognitionException {
        StandardLuceneGrammarParser.range_term_ex_return retval = new StandardLuceneGrammarParser.range_term_ex_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LCURLY43=null;
        Token TO44=null;
        Token RCURLY45=null;
        StandardLuceneGrammarParser.range_value_return a =null;

        StandardLuceneGrammarParser.range_value_return b =null;


        Object LCURLY43_tree=null;
        Object TO44_tree=null;
        Object RCURLY45_tree=null;
        RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // StandardLuceneGrammar.g:109:2: ( LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY )
            // StandardLuceneGrammar.g:110:8: LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY
            {
            LCURLY43=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_range_term_ex864); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LCURLY.add(LCURLY43);


            // StandardLuceneGrammar.g:111:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // StandardLuceneGrammar.g:111:10: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_ex877);
            a=range_value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_range_value.add(a.getTree());

            // AST REWRITE
            // elements: range_value
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 111:24: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // StandardLuceneGrammar.g:111:39: ^( QANYTHING QANYTHING[\"*\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(QANYTHING, "QANYTHING")
                , root_1);

                adaptor.addChild(root_1, 
                (Object)adaptor.create(QANYTHING, "*")
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:112:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==DATE_TOKEN||LA25_0==NUMBER||(LA25_0 >= PHRASE && LA25_0 <= PHRASE_ANYTHING)||(LA25_0 >= STAR && LA25_0 <= TERM_TRUNCATED)||LA25_0==TO) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // StandardLuceneGrammar.g:112:10: ( TO )? b= range_value
                    {
                    // StandardLuceneGrammar.g:112:10: ( TO )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==TO) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // StandardLuceneGrammar.g:112:10: TO
                            {
                            TO44=(Token)match(input,TO,FOLLOW_TO_in_range_term_ex900); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO44);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_ex905);
                    b=range_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_value.add(b.getTree());

                    // AST REWRITE
                    // elements: b, a
                    // token labels: 
                    // rule labels: retval, b, a
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_b=new RewriteRuleSubtreeStream(adaptor,"rule b",b!=null?b.tree:null);
                    RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"rule a",a!=null?a.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 112:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // StandardLuceneGrammar.g:112:35: ( $b)?
                        if ( stream_b.hasNext() ) {
                            adaptor.addChild(root_0, stream_b.nextTree());

                        }
                        stream_b.reset();

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }


            RCURLY45=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_range_term_ex926); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RCURLY.add(RCURLY45);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "range_term_ex"


    public static class range_value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "range_value"
    // StandardLuceneGrammar.g:116:1: range_value : ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) );
    public final StandardLuceneGrammarParser.range_value_return range_value() throws RecognitionException {
        StandardLuceneGrammarParser.range_value_return retval = new StandardLuceneGrammarParser.range_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR51=null;
        StandardLuceneGrammarParser.truncated_return truncated46 =null;

        StandardLuceneGrammarParser.quoted_return quoted47 =null;

        StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated48 =null;

        StandardLuceneGrammarParser.date_return date49 =null;

        StandardLuceneGrammarParser.normal_return normal50 =null;


        Object STAR51_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_date=new RewriteRuleSubtreeStream(adaptor,"rule date");
        try {
            // StandardLuceneGrammar.g:117:2: ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) )
            int alt26=6;
            switch ( input.LA(1) ) {
            case TERM_TRUNCATED:
                {
                alt26=1;
                }
                break;
            case PHRASE:
                {
                alt26=2;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt26=3;
                }
                break;
            case DATE_TOKEN:
                {
                alt26=4;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
                {
                alt26=5;
                }
                break;
            case STAR:
                {
                alt26=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;

            }

            switch (alt26) {
                case 1 :
                    // StandardLuceneGrammar.g:118:2: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_range_value940);
                    truncated46=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated46.getTree());

                    // AST REWRITE
                    // elements: truncated
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 118:12: -> ^( QTRUNCATED truncated )
                    {
                        // StandardLuceneGrammar.g:118:15: ^( QTRUNCATED truncated )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QTRUNCATED, "QTRUNCATED")
                        , root_1);

                        adaptor.addChild(root_1, stream_truncated.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:119:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_range_value953);
                    quoted47=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted47.getTree());

                    // AST REWRITE
                    // elements: quoted
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 119:11: -> ^( QPHRASE quoted )
                    {
                        // StandardLuceneGrammar.g:119:14: ^( QPHRASE quoted )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QPHRASE, "QPHRASE")
                        , root_1);

                        adaptor.addChild(root_1, stream_quoted.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // StandardLuceneGrammar.g:120:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_range_value966);
                    quoted_truncated48=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated48.getTree());

                    // AST REWRITE
                    // elements: quoted_truncated
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 120:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // StandardLuceneGrammar.g:120:24: ^( QPHRASETRUNC quoted_truncated )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QPHRASETRUNC, "QPHRASETRUNC")
                        , root_1);

                        adaptor.addChild(root_1, stream_quoted_truncated.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // StandardLuceneGrammar.g:121:4: date
                    {
                    pushFollow(FOLLOW_date_in_range_value979);
                    date49=date();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_date.add(date49.getTree());

                    // AST REWRITE
                    // elements: date
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 121:9: -> ^( QNORMAL date )
                    {
                        // StandardLuceneGrammar.g:121:12: ^( QNORMAL date )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QNORMAL, "QNORMAL")
                        , root_1);

                        adaptor.addChild(root_1, stream_date.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 5 :
                    // StandardLuceneGrammar.g:122:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_range_value992);
                    normal50=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal50.getTree());

                    // AST REWRITE
                    // elements: normal
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 122:11: -> ^( QNORMAL normal )
                    {
                        // StandardLuceneGrammar.g:122:14: ^( QNORMAL normal )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QNORMAL, "QNORMAL")
                        , root_1);

                        adaptor.addChild(root_1, stream_normal.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 6 :
                    // StandardLuceneGrammar.g:123:4: STAR
                    {
                    STAR51=(Token)match(input,STAR,FOLLOW_STAR_in_range_value1006); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR51);


                    // AST REWRITE
                    // elements: STAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 123:9: -> ^( QANYTHING STAR )
                    {
                        // StandardLuceneGrammar.g:123:12: ^( QANYTHING STAR )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QANYTHING, "QANYTHING")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_STAR.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "range_value"


    public static class multi_value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multi_value"
    // StandardLuceneGrammar.g:126:1: multi_value : LPAREN multiClause RPAREN -> multiClause ;
    public final StandardLuceneGrammarParser.multi_value_return multi_value() throws RecognitionException {
        StandardLuceneGrammarParser.multi_value_return retval = new StandardLuceneGrammarParser.multi_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN52=null;
        Token RPAREN54=null;
        StandardLuceneGrammarParser.multiClause_return multiClause53 =null;


        Object LPAREN52_tree=null;
        Object RPAREN54_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_multiClause=new RewriteRuleSubtreeStream(adaptor,"rule multiClause");
        try {
            // StandardLuceneGrammar.g:127:2: ( LPAREN multiClause RPAREN -> multiClause )
            // StandardLuceneGrammar.g:128:2: LPAREN multiClause RPAREN
            {
            LPAREN52=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_multi_value1027); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN52);


            pushFollow(FOLLOW_multiClause_in_multi_value1029);
            multiClause53=multiClause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_multiClause.add(multiClause53.getTree());

            RPAREN54=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_multi_value1031); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN54);


            // AST REWRITE
            // elements: multiClause
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 128:28: -> multiClause
            {
                adaptor.addChild(root_0, stream_multiClause.nextTree());

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "multi_value"


    public static class multiClause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiClause"
    // StandardLuceneGrammar.g:133:1: multiClause : ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
    public final StandardLuceneGrammarParser.multiClause_return multiClause() throws RecognitionException {
        StandardLuceneGrammarParser.multiClause_return retval = new StandardLuceneGrammarParser.multiClause_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseOr_return clauseOr55 =null;


        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // StandardLuceneGrammar.g:134:2: ( ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
            // StandardLuceneGrammar.g:135:2: ( clauseOr )+
            {
            // StandardLuceneGrammar.g:135:2: ( clauseOr )+
            int cnt27=0;
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( ((LA27_0 >= LBRACK && LA27_0 <= MINUS)||LA27_0==NUMBER||(LA27_0 >= PHRASE && LA27_0 <= PLUS)||LA27_0==QMARK||(LA27_0 >= STAR && LA27_0 <= TERM_TRUNCATED)) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // StandardLuceneGrammar.g:135:2: clauseOr
            	    {
            	    pushFollow(FOLLOW_clauseOr_in_multiClause1050);
            	    clauseOr55=clauseOr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr55.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt27 >= 1 ) break loop27;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(27, input);
                        throw eee;
                }
                cnt27++;
            } while (true);


            // AST REWRITE
            // elements: clauseOr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 135:12: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
            {
                // StandardLuceneGrammar.g:135:15: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OPERATOR, "DEFOP")
                , root_1);

                if ( !(stream_clauseOr.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_clauseOr.hasNext() ) {
                    adaptor.addChild(root_1, stream_clauseOr.nextTree());

                }
                stream_clauseOr.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "multiClause"


    public static class multiDefault_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiDefault"
    // StandardLuceneGrammar.g:145:1: multiDefault : ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) ;
    public final StandardLuceneGrammarParser.multiDefault_return multiDefault() throws RecognitionException {
        StandardLuceneGrammarParser.multiDefault_return retval = new StandardLuceneGrammarParser.multiDefault_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiOr_return multiOr56 =null;


        RewriteRuleSubtreeStream stream_multiOr=new RewriteRuleSubtreeStream(adaptor,"rule multiOr");
        try {
            // StandardLuceneGrammar.g:146:2: ( ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) )
            // StandardLuceneGrammar.g:147:2: ( multiOr )+
            {
            // StandardLuceneGrammar.g:147:2: ( multiOr )+
            int cnt28=0;
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( ((LA28_0 >= LBRACK && LA28_0 <= LCURLY)||LA28_0==MINUS||LA28_0==NUMBER||(LA28_0 >= PHRASE && LA28_0 <= PLUS)||LA28_0==QMARK||(LA28_0 >= TERM_NORMAL && LA28_0 <= TERM_TRUNCATED)) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // StandardLuceneGrammar.g:147:2: multiOr
            	    {
            	    pushFollow(FOLLOW_multiOr_in_multiDefault1088);
            	    multiOr56=multiOr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_multiOr.add(multiOr56.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt28 >= 1 ) break loop28;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(28, input);
                        throw eee;
                }
                cnt28++;
            } while (true);


            // AST REWRITE
            // elements: multiOr
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 147:11: -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
            {
                // StandardLuceneGrammar.g:147:14: ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OPERATOR, "DEFOP")
                , root_1);

                if ( !(stream_multiOr.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_multiOr.hasNext() ) {
                    adaptor.addChild(root_1, stream_multiOr.nextTree());

                }
                stream_multiOr.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "multiDefault"


    public static class multiOr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiOr"
    // StandardLuceneGrammar.g:150:1: multiOr : (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* ;
    public final StandardLuceneGrammarParser.multiOr_return multiOr() throws RecognitionException {
        StandardLuceneGrammarParser.multiOr_return retval = new StandardLuceneGrammarParser.multiOr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiAnd_return first =null;

        StandardLuceneGrammarParser.multiAnd_return others =null;

        StandardLuceneGrammarParser.or_return or57 =null;


        RewriteRuleSubtreeStream stream_multiAnd=new RewriteRuleSubtreeStream(adaptor,"rule multiAnd");
        RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
        try {
            // StandardLuceneGrammar.g:151:2: ( (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* )
            // StandardLuceneGrammar.g:152:2: (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
            {
            // StandardLuceneGrammar.g:152:2: (first= multiAnd -> $first)
            // StandardLuceneGrammar.g:152:3: first= multiAnd
            {
            pushFollow(FOLLOW_multiAnd_in_multiOr1116);
            first=multiAnd();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_multiAnd.add(first.getTree());

            // AST REWRITE
            // elements: first
            // token labels: 
            // rule labels: retval, first
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.tree:null);

            root_0 = (Object)adaptor.nil();
            // 152:19: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:152:30: ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( (LA29_0==OR) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // StandardLuceneGrammar.g:152:31: or others= multiAnd
            	    {
            	    pushFollow(FOLLOW_or_in_multiOr1126);
            	    or57=or();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_or.add(or57.getTree());

            	    pushFollow(FOLLOW_multiAnd_in_multiOr1130);
            	    others=multiAnd();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_multiAnd.add(others.getTree());

            	    // AST REWRITE
            	    // elements: multiAnd
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 152:49: -> ^( OPERATOR[\"OR\"] ( multiAnd )+ )
            	    {
            	        // StandardLuceneGrammar.g:152:52: ^( OPERATOR[\"OR\"] ( multiAnd )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "OR")
            	        , root_1);

            	        if ( !(stream_multiAnd.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_multiAnd.hasNext() ) {
            	            adaptor.addChild(root_1, stream_multiAnd.nextTree());

            	        }
            	        stream_multiAnd.reset();

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;
            	    }

            	    }
            	    break;

            	default :
            	    break loop29;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "multiOr"


    public static class multiAnd_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiAnd"
    // StandardLuceneGrammar.g:155:1: multiAnd : (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* ;
    public final StandardLuceneGrammarParser.multiAnd_return multiAnd() throws RecognitionException {
        StandardLuceneGrammarParser.multiAnd_return retval = new StandardLuceneGrammarParser.multiAnd_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiNot_return first =null;

        StandardLuceneGrammarParser.multiNot_return others =null;

        StandardLuceneGrammarParser.and_return and58 =null;


        RewriteRuleSubtreeStream stream_multiNot=new RewriteRuleSubtreeStream(adaptor,"rule multiNot");
        RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");
        try {
            // StandardLuceneGrammar.g:156:2: ( (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* )
            // StandardLuceneGrammar.g:157:2: (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
            {
            // StandardLuceneGrammar.g:157:2: (first= multiNot -> $first)
            // StandardLuceneGrammar.g:157:3: first= multiNot
            {
            pushFollow(FOLLOW_multiNot_in_multiAnd1161);
            first=multiNot();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_multiNot.add(first.getTree());

            // AST REWRITE
            // elements: first
            // token labels: 
            // rule labels: retval, first
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.tree:null);

            root_0 = (Object)adaptor.nil();
            // 157:19: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:157:30: ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==AND) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // StandardLuceneGrammar.g:157:31: and others= multiNot
            	    {
            	    pushFollow(FOLLOW_and_in_multiAnd1171);
            	    and58=and();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_and.add(and58.getTree());

            	    pushFollow(FOLLOW_multiNot_in_multiAnd1175);
            	    others=multiNot();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_multiNot.add(others.getTree());

            	    // AST REWRITE
            	    // elements: multiNot
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 157:51: -> ^( OPERATOR[\"AND\"] ( multiNot )+ )
            	    {
            	        // StandardLuceneGrammar.g:157:54: ^( OPERATOR[\"AND\"] ( multiNot )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "AND")
            	        , root_1);

            	        if ( !(stream_multiNot.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_multiNot.hasNext() ) {
            	            adaptor.addChild(root_1, stream_multiNot.nextTree());

            	        }
            	        stream_multiNot.reset();

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;
            	    }

            	    }
            	    break;

            	default :
            	    break loop30;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "multiAnd"


    public static class multiNot_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiNot"
    // StandardLuceneGrammar.g:160:1: multiNot : (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )* ;
    public final StandardLuceneGrammarParser.multiNot_return multiNot() throws RecognitionException {
        StandardLuceneGrammarParser.multiNot_return retval = new StandardLuceneGrammarParser.multiNot_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiNear_return first =null;

        StandardLuceneGrammarParser.multiNear_return others =null;

        StandardLuceneGrammarParser.not_return not59 =null;


        RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
        RewriteRuleSubtreeStream stream_multiNear=new RewriteRuleSubtreeStream(adaptor,"rule multiNear");
        try {
            // StandardLuceneGrammar.g:161:2: ( (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )* )
            // StandardLuceneGrammar.g:162:2: (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )*
            {
            // StandardLuceneGrammar.g:162:2: (first= multiNear -> $first)
            // StandardLuceneGrammar.g:162:3: first= multiNear
            {
            pushFollow(FOLLOW_multiNear_in_multiNot1206);
            first=multiNear();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_multiNear.add(first.getTree());

            // AST REWRITE
            // elements: first
            // token labels: 
            // rule labels: retval, first
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.tree:null);

            root_0 = (Object)adaptor.nil();
            // 162:20: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:162:31: ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==AND) ) {
                    int LA31_1 = input.LA(2);

                    if ( (LA31_1==NOT) ) {
                        alt31=1;
                    }


                }
                else if ( (LA31_0==NOT) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // StandardLuceneGrammar.g:162:32: not others= multiNear
            	    {
            	    pushFollow(FOLLOW_not_in_multiNot1216);
            	    not59=not();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_not.add(not59.getTree());

            	    pushFollow(FOLLOW_multiNear_in_multiNot1220);
            	    others=multiNear();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_multiNear.add(others.getTree());

            	    // AST REWRITE
            	    // elements: multiNear
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 162:52: -> ^( OPERATOR[\"NOT\"] ( multiNear )+ )
            	    {
            	        // StandardLuceneGrammar.g:162:55: ^( OPERATOR[\"NOT\"] ( multiNear )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "NOT")
            	        , root_1);

            	        if ( !(stream_multiNear.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_multiNear.hasNext() ) {
            	            adaptor.addChild(root_1, stream_multiNear.nextTree());

            	        }
            	        stream_multiNear.reset();

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;
            	    }

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "multiNot"


    public static class multiNear_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiNear"
    // StandardLuceneGrammar.g:165:1: multiNear : (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )* ;
    public final StandardLuceneGrammarParser.multiNear_return multiNear() throws RecognitionException {
        StandardLuceneGrammarParser.multiNear_return retval = new StandardLuceneGrammarParser.multiNear_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiBasic_return first =null;

        StandardLuceneGrammarParser.multiBasic_return others =null;

        StandardLuceneGrammarParser.near_return near60 =null;


        RewriteRuleSubtreeStream stream_near=new RewriteRuleSubtreeStream(adaptor,"rule near");
        RewriteRuleSubtreeStream stream_multiBasic=new RewriteRuleSubtreeStream(adaptor,"rule multiBasic");
        try {
            // StandardLuceneGrammar.g:166:2: ( (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )* )
            // StandardLuceneGrammar.g:167:2: (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )*
            {
            // StandardLuceneGrammar.g:167:2: (first= multiBasic -> $first)
            // StandardLuceneGrammar.g:167:3: first= multiBasic
            {
            pushFollow(FOLLOW_multiBasic_in_multiNear1250);
            first=multiBasic();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_multiBasic.add(first.getTree());

            // AST REWRITE
            // elements: first
            // token labels: 
            // rule labels: retval, first
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.tree:null);

            root_0 = (Object)adaptor.nil();
            // 167:21: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:167:32: ( near others= multiBasic -> ^( near ( multiBasic )+ ) )*
            loop32:
            do {
                int alt32=2;
                int LA32_0 = input.LA(1);

                if ( (LA32_0==NEAR) ) {
                    alt32=1;
                }


                switch (alt32) {
            	case 1 :
            	    // StandardLuceneGrammar.g:167:33: near others= multiBasic
            	    {
            	    pushFollow(FOLLOW_near_in_multiNear1260);
            	    near60=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near60.getTree());

            	    pushFollow(FOLLOW_multiBasic_in_multiNear1264);
            	    others=multiBasic();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_multiBasic.add(others.getTree());

            	    // AST REWRITE
            	    // elements: near, multiBasic
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 167:55: -> ^( near ( multiBasic )+ )
            	    {
            	        // StandardLuceneGrammar.g:167:58: ^( near ( multiBasic )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(stream_near.nextNode(), root_1);

            	        if ( !(stream_multiBasic.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_multiBasic.hasNext() ) {
            	            adaptor.addChild(root_1, stream_multiBasic.nextTree());

            	        }
            	        stream_multiBasic.reset();

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;
            	    }

            	    }
            	    break;

            	default :
            	    break loop32;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "multiNear"


    public static class multiBasic_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiBasic"
    // StandardLuceneGrammar.g:171:1: multiBasic : mterm ;
    public final StandardLuceneGrammarParser.multiBasic_return multiBasic() throws RecognitionException {
        StandardLuceneGrammarParser.multiBasic_return retval = new StandardLuceneGrammarParser.multiBasic_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.mterm_return mterm61 =null;



        try {
            // StandardLuceneGrammar.g:172:2: ( mterm )
            // StandardLuceneGrammar.g:173:2: mterm
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_mterm_in_multiBasic1290);
            mterm61=mterm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mterm61.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "multiBasic"


    public static class mterm_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mterm"
    // StandardLuceneGrammar.g:176:1: mterm : ( modifier )? value -> ^( MODIFIER ( modifier )? ^( VALUE value ) ) ;
    public final StandardLuceneGrammarParser.mterm_return mterm() throws RecognitionException {
        StandardLuceneGrammarParser.mterm_return retval = new StandardLuceneGrammarParser.mterm_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.modifier_return modifier62 =null;

        StandardLuceneGrammarParser.value_return value63 =null;


        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try {
            // StandardLuceneGrammar.g:177:2: ( ( modifier )? value -> ^( MODIFIER ( modifier )? ^( VALUE value ) ) )
            // StandardLuceneGrammar.g:178:2: ( modifier )? value
            {
            // StandardLuceneGrammar.g:178:2: ( modifier )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==MINUS||LA33_0==PLUS) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // StandardLuceneGrammar.g:178:2: modifier
                    {
                    pushFollow(FOLLOW_modifier_in_mterm1306);
                    modifier62=modifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_modifier.add(modifier62.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_value_in_mterm1309);
            value63=value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_value.add(value63.getTree());

            // AST REWRITE
            // elements: value, modifier
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 178:18: -> ^( MODIFIER ( modifier )? ^( VALUE value ) )
            {
                // StandardLuceneGrammar.g:178:21: ^( MODIFIER ( modifier )? ^( VALUE value ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(MODIFIER, "MODIFIER")
                , root_1);

                // StandardLuceneGrammar.g:178:32: ( modifier )?
                if ( stream_modifier.hasNext() ) {
                    adaptor.addChild(root_1, stream_modifier.nextTree());

                }
                stream_modifier.reset();

                // StandardLuceneGrammar.g:178:42: ^( VALUE value )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VALUE, "VALUE")
                , root_2);

                adaptor.addChild(root_2, stream_value.nextTree());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "mterm"


    public static class normal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "normal"
    // StandardLuceneGrammar.g:182:1: normal : ( TERM_NORMAL | NUMBER );
    public final StandardLuceneGrammarParser.normal_return normal() throws RecognitionException {
        StandardLuceneGrammarParser.normal_return retval = new StandardLuceneGrammarParser.normal_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set64=null;

        Object set64_tree=null;

        try {
            // StandardLuceneGrammar.g:183:2: ( TERM_NORMAL | NUMBER )
            // StandardLuceneGrammar.g:
            {
            root_0 = (Object)adaptor.nil();


            set64=(Token)input.LT(1);

            if ( input.LA(1)==NUMBER||input.LA(1)==TERM_NORMAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set64)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "normal"


    public static class truncated_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "truncated"
    // StandardLuceneGrammar.g:191:1: truncated : TERM_TRUNCATED ;
    public final StandardLuceneGrammarParser.truncated_return truncated() throws RecognitionException {
        StandardLuceneGrammarParser.truncated_return retval = new StandardLuceneGrammarParser.truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_TRUNCATED65=null;

        Object TERM_TRUNCATED65_tree=null;

        try {
            // StandardLuceneGrammar.g:192:2: ( TERM_TRUNCATED )
            // StandardLuceneGrammar.g:193:2: TERM_TRUNCATED
            {
            root_0 = (Object)adaptor.nil();


            TERM_TRUNCATED65=(Token)match(input,TERM_TRUNCATED,FOLLOW_TERM_TRUNCATED_in_truncated1366); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            TERM_TRUNCATED65_tree = 
            (Object)adaptor.create(TERM_TRUNCATED65)
            ;
            adaptor.addChild(root_0, TERM_TRUNCATED65_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "truncated"


    public static class quoted_truncated_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "quoted_truncated"
    // StandardLuceneGrammar.g:197:1: quoted_truncated : PHRASE_ANYTHING ;
    public final StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated() throws RecognitionException {
        StandardLuceneGrammarParser.quoted_truncated_return retval = new StandardLuceneGrammarParser.quoted_truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE_ANYTHING66=null;

        Object PHRASE_ANYTHING66_tree=null;

        try {
            // StandardLuceneGrammar.g:198:2: ( PHRASE_ANYTHING )
            // StandardLuceneGrammar.g:199:2: PHRASE_ANYTHING
            {
            root_0 = (Object)adaptor.nil();


            PHRASE_ANYTHING66=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1381); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE_ANYTHING66_tree = 
            (Object)adaptor.create(PHRASE_ANYTHING66)
            ;
            adaptor.addChild(root_0, PHRASE_ANYTHING66_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "quoted_truncated"


    public static class quoted_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "quoted"
    // StandardLuceneGrammar.g:202:1: quoted : PHRASE ;
    public final StandardLuceneGrammarParser.quoted_return quoted() throws RecognitionException {
        StandardLuceneGrammarParser.quoted_return retval = new StandardLuceneGrammarParser.quoted_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE67=null;

        Object PHRASE67_tree=null;

        try {
            // StandardLuceneGrammar.g:202:8: ( PHRASE )
            // StandardLuceneGrammar.g:203:2: PHRASE
            {
            root_0 = (Object)adaptor.nil();


            PHRASE67=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_quoted1393); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE67_tree = 
            (Object)adaptor.create(PHRASE67)
            ;
            adaptor.addChild(root_0, PHRASE67_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "quoted"


    public static class operator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "operator"
    // StandardLuceneGrammar.g:209:1: operator : ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] ) ;
    public final StandardLuceneGrammarParser.operator_return operator() throws RecognitionException {
        StandardLuceneGrammarParser.operator_return retval = new StandardLuceneGrammarParser.operator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND68=null;
        Token OR69=null;
        Token NOT70=null;
        Token NEAR71=null;

        Object AND68_tree=null;
        Object OR69_tree=null;
        Object NOT70_tree=null;
        Object NEAR71_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");

        try {
            // StandardLuceneGrammar.g:209:9: ( ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] ) )
            // StandardLuceneGrammar.g:209:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] )
            {
            // StandardLuceneGrammar.g:209:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] )
            int alt34=4;
            switch ( input.LA(1) ) {
            case AND:
                {
                alt34=1;
                }
                break;
            case OR:
                {
                alt34=2;
                }
                break;
            case NOT:
                {
                alt34=3;
                }
                break;
            case NEAR:
                {
                alt34=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 34, 0, input);

                throw nvae;

            }

            switch (alt34) {
                case 1 :
                    // StandardLuceneGrammar.g:210:2: AND
                    {
                    AND68=(Token)match(input,AND,FOLLOW_AND_in_operator1409); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AND.add(AND68);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 210:6: -> OPERATOR[\"AND\"]
                    {
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(OPERATOR, "AND")
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:211:4: OR
                    {
                    OR69=(Token)match(input,OR,FOLLOW_OR_in_operator1419); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_OR.add(OR69);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 211:7: -> OPERATOR[\"OR\"]
                    {
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(OPERATOR, "OR")
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // StandardLuceneGrammar.g:212:4: NOT
                    {
                    NOT70=(Token)match(input,NOT,FOLLOW_NOT_in_operator1429); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NOT.add(NOT70);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 212:8: -> OPERATOR[\"NOT\"]
                    {
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(OPERATOR, "NOT")
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // StandardLuceneGrammar.g:213:4: NEAR
                    {
                    NEAR71=(Token)match(input,NEAR,FOLLOW_NEAR_in_operator1439); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEAR.add(NEAR71);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 213:9: -> OPERATOR[\"NEAR\"]
                    {
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(OPERATOR, "NEAR")
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "operator"


    public static class modifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "modifier"
    // StandardLuceneGrammar.g:216:1: modifier : ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] );
    public final StandardLuceneGrammarParser.modifier_return modifier() throws RecognitionException {
        StandardLuceneGrammarParser.modifier_return retval = new StandardLuceneGrammarParser.modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PLUS72=null;
        Token MINUS73=null;

        Object PLUS72_tree=null;
        Object MINUS73_tree=null;
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
        RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");

        try {
            // StandardLuceneGrammar.g:216:9: ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] )
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==PLUS) ) {
                alt35=1;
            }
            else if ( (LA35_0==MINUS) ) {
                alt35=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;

            }
            switch (alt35) {
                case 1 :
                    // StandardLuceneGrammar.g:217:2: PLUS
                    {
                    PLUS72=(Token)match(input,PLUS,FOLLOW_PLUS_in_modifier1456); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PLUS.add(PLUS72);


                    // AST REWRITE
                    // elements: PLUS
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 217:7: -> PLUS[\"+\"]
                    {
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(PLUS, "+")
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:218:4: MINUS
                    {
                    MINUS73=(Token)match(input,MINUS,FOLLOW_MINUS_in_modifier1466); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MINUS.add(MINUS73);


                    // AST REWRITE
                    // elements: MINUS
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 218:10: -> MINUS[\"-\"]
                    {
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(MINUS, "-")
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "modifier"


    public static class term_modifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "term_modifier"
    // StandardLuceneGrammar.g:230:1: term_modifier : ( TILDE ( CARAT )? -> ^( FUZZY TILDE ) ^( BOOST ( CARAT )? ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) );
    public final StandardLuceneGrammarParser.term_modifier_return term_modifier() throws RecognitionException {
        StandardLuceneGrammarParser.term_modifier_return retval = new StandardLuceneGrammarParser.term_modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE74=null;
        Token CARAT75=null;
        Token CARAT76=null;
        Token TILDE77=null;

        Object TILDE74_tree=null;
        Object CARAT75_tree=null;
        Object CARAT76_tree=null;
        Object TILDE77_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

        try {
            // StandardLuceneGrammar.g:230:15: ( TILDE ( CARAT )? -> ^( FUZZY TILDE ) ^( BOOST ( CARAT )? ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==TILDE) ) {
                alt38=1;
            }
            else if ( (LA38_0==CARAT) ) {
                alt38=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;

            }
            switch (alt38) {
                case 1 :
                    // StandardLuceneGrammar.g:231:2: TILDE ( CARAT )?
                    {
                    TILDE74=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1484); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TILDE.add(TILDE74);


                    // StandardLuceneGrammar.g:231:8: ( CARAT )?
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==CARAT) ) {
                        alt36=1;
                    }
                    switch (alt36) {
                        case 1 :
                            // StandardLuceneGrammar.g:231:8: CARAT
                            {
                            CARAT75=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1486); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CARAT.add(CARAT75);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: CARAT, TILDE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 231:15: -> ^( FUZZY TILDE ) ^( BOOST ( CARAT )? )
                    {
                        // StandardLuceneGrammar.g:231:18: ^( FUZZY TILDE )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FUZZY, "FUZZY")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_TILDE.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                        // StandardLuceneGrammar.g:231:33: ^( BOOST ( CARAT )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_1);

                        // StandardLuceneGrammar.g:231:41: ( CARAT )?
                        if ( stream_CARAT.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_CARAT.nextNode()
                            );

                        }
                        stream_CARAT.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:232:4: CARAT ( TILDE )?
                    {
                    CARAT76=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1507); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CARAT.add(CARAT76);


                    // StandardLuceneGrammar.g:232:10: ( TILDE )?
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==TILDE) ) {
                        alt37=1;
                    }
                    switch (alt37) {
                        case 1 :
                            // StandardLuceneGrammar.g:232:10: TILDE
                            {
                            TILDE77=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1509); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TILDE.add(TILDE77);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: CARAT, TILDE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 232:17: -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? )
                    {
                        // StandardLuceneGrammar.g:232:20: ^( BOOST CARAT )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_CARAT.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                        // StandardLuceneGrammar.g:232:35: ^( FUZZY ( TILDE )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FUZZY, "FUZZY")
                        , root_1);

                        // StandardLuceneGrammar.g:232:43: ( TILDE )?
                        if ( stream_TILDE.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_TILDE.nextNode()
                            );

                        }
                        stream_TILDE.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "term_modifier"


    public static class boost_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "boost"
    // StandardLuceneGrammar.g:252:1: boost : ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? ;
    public final StandardLuceneGrammarParser.boost_return boost() throws RecognitionException {
        StandardLuceneGrammarParser.boost_return retval = new StandardLuceneGrammarParser.boost_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token CARAT78=null;
        Token NUMBER79=null;

        Object CARAT78_tree=null;
        Object NUMBER79_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // StandardLuceneGrammar.g:252:7: ( ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? )
            // StandardLuceneGrammar.g:253:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )?
            {
            // StandardLuceneGrammar.g:253:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) )
            // StandardLuceneGrammar.g:253:3: CARAT
            {
            CARAT78=(Token)match(input,CARAT,FOLLOW_CARAT_in_boost1541); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CARAT.add(CARAT78);


            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 253:9: -> ^( BOOST NUMBER[\"DEF\"] )
            {
                // StandardLuceneGrammar.g:253:12: ^( BOOST NUMBER[\"DEF\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(BOOST, "BOOST")
                , root_1);

                adaptor.addChild(root_1, 
                (Object)adaptor.create(NUMBER, "DEF")
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:254:2: ( NUMBER -> ^( BOOST NUMBER ) )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==NUMBER) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // StandardLuceneGrammar.g:254:3: NUMBER
                    {
                    NUMBER79=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_boost1556); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER79);


                    // AST REWRITE
                    // elements: NUMBER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 254:10: -> ^( BOOST NUMBER )
                    {
                        // StandardLuceneGrammar.g:254:13: ^( BOOST NUMBER )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_NUMBER.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "boost"


    public static class fuzzy_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "fuzzy"
    // StandardLuceneGrammar.g:257:1: fuzzy : ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? ;
    public final StandardLuceneGrammarParser.fuzzy_return fuzzy() throws RecognitionException {
        StandardLuceneGrammarParser.fuzzy_return retval = new StandardLuceneGrammarParser.fuzzy_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE80=null;
        Token NUMBER81=null;

        Object TILDE80_tree=null;
        Object NUMBER81_tree=null;
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // StandardLuceneGrammar.g:257:7: ( ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? )
            // StandardLuceneGrammar.g:258:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )?
            {
            // StandardLuceneGrammar.g:258:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) )
            // StandardLuceneGrammar.g:258:3: TILDE
            {
            TILDE80=(Token)match(input,TILDE,FOLLOW_TILDE_in_fuzzy1579); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TILDE.add(TILDE80);


            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 258:9: -> ^( FUZZY NUMBER[\"DEF\"] )
            {
                // StandardLuceneGrammar.g:258:12: ^( FUZZY NUMBER[\"DEF\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(FUZZY, "FUZZY")
                , root_1);

                adaptor.addChild(root_1, 
                (Object)adaptor.create(NUMBER, "DEF")
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:259:2: ( NUMBER -> ^( FUZZY NUMBER ) )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==NUMBER) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // StandardLuceneGrammar.g:259:3: NUMBER
                    {
                    NUMBER81=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_fuzzy1594); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER81);


                    // AST REWRITE
                    // elements: NUMBER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 259:10: -> ^( FUZZY NUMBER )
                    {
                        // StandardLuceneGrammar.g:259:13: ^( FUZZY NUMBER )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FUZZY, "FUZZY")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_NUMBER.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "fuzzy"


    public static class not_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "not"
    // StandardLuceneGrammar.g:262:1: not : ( ( AND NOT )=> AND NOT | NOT );
    public final StandardLuceneGrammarParser.not_return not() throws RecognitionException {
        StandardLuceneGrammarParser.not_return retval = new StandardLuceneGrammarParser.not_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND82=null;
        Token NOT83=null;
        Token NOT84=null;

        Object AND82_tree=null;
        Object NOT83_tree=null;
        Object NOT84_tree=null;

        try {
            // StandardLuceneGrammar.g:262:5: ( ( AND NOT )=> AND NOT | NOT )
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==AND) && (synpred4_StandardLuceneGrammar())) {
                alt41=1;
            }
            else if ( (LA41_0==NOT) ) {
                alt41=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;

            }
            switch (alt41) {
                case 1 :
                    // StandardLuceneGrammar.g:263:2: ( AND NOT )=> AND NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    AND82=(Token)match(input,AND,FOLLOW_AND_in_not1624); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    AND82_tree = 
                    (Object)adaptor.create(AND82)
                    ;
                    adaptor.addChild(root_0, AND82_tree);
                    }

                    NOT83=(Token)match(input,NOT,FOLLOW_NOT_in_not1626); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT83_tree = 
                    (Object)adaptor.create(NOT83)
                    ;
                    adaptor.addChild(root_0, NOT83_tree);
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:264:4: NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    NOT84=(Token)match(input,NOT,FOLLOW_NOT_in_not1631); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT84_tree = 
                    (Object)adaptor.create(NOT84)
                    ;
                    adaptor.addChild(root_0, NOT84_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "not"


    public static class and_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "and"
    // StandardLuceneGrammar.g:267:1: and : AND ;
    public final StandardLuceneGrammarParser.and_return and() throws RecognitionException {
        StandardLuceneGrammarParser.and_return retval = new StandardLuceneGrammarParser.and_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND85=null;

        Object AND85_tree=null;

        try {
            // StandardLuceneGrammar.g:267:6: ( AND )
            // StandardLuceneGrammar.g:268:2: AND
            {
            root_0 = (Object)adaptor.nil();


            AND85=(Token)match(input,AND,FOLLOW_AND_in_and1645); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            AND85_tree = 
            (Object)adaptor.create(AND85)
            ;
            adaptor.addChild(root_0, AND85_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "and"


    public static class or_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "or"
    // StandardLuceneGrammar.g:271:1: or : OR ;
    public final StandardLuceneGrammarParser.or_return or() throws RecognitionException {
        StandardLuceneGrammarParser.or_return retval = new StandardLuceneGrammarParser.or_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OR86=null;

        Object OR86_tree=null;

        try {
            // StandardLuceneGrammar.g:271:5: ( OR )
            // StandardLuceneGrammar.g:272:2: OR
            {
            root_0 = (Object)adaptor.nil();


            OR86=(Token)match(input,OR,FOLLOW_OR_in_or1659); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            OR86_tree = 
            (Object)adaptor.create(OR86)
            ;
            adaptor.addChild(root_0, OR86_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "or"


    public static class near_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "near"
    // StandardLuceneGrammar.g:275:1: near : ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )? ;
    public final StandardLuceneGrammarParser.near_return near() throws RecognitionException {
        StandardLuceneGrammarParser.near_return retval = new StandardLuceneGrammarParser.near_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token b=null;
        Token NEAR87=null;
        Token char_literal88=null;

        Object b_tree=null;
        Object NEAR87_tree=null;
        Object char_literal88_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");
        RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // StandardLuceneGrammar.g:275:6: ( ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )? )
            // StandardLuceneGrammar.g:276:2: ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )?
            {
            // StandardLuceneGrammar.g:276:2: ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) )
            // StandardLuceneGrammar.g:276:3: NEAR
            {
            NEAR87=(Token)match(input,NEAR,FOLLOW_NEAR_in_near1674); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NEAR.add(NEAR87);


            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 276:8: -> ^( OPERATOR[\"NEAR 5\"] )
            {
                // StandardLuceneGrammar.g:276:11: ^( OPERATOR[\"NEAR 5\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OPERATOR, "NEAR 5")
                , root_1);

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:277:2: ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==57) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // StandardLuceneGrammar.g:277:3: '/' b= NUMBER
                    {
                    char_literal88=(Token)match(input,57,FOLLOW_57_in_near1687); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_57.add(char_literal88);


                    b=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_near1691); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(b);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 277:16: -> ^( OPERATOR[\"NEAR \" + $b.getText()] )
                    {
                        // StandardLuceneGrammar.g:277:19: ^( OPERATOR[\"NEAR \" + $b.getText()] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(OPERATOR, "NEAR " + b.getText())
                        , root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "near"


    public static class date_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "date"
    // StandardLuceneGrammar.g:280:1: date : DATE_TOKEN ;
    public final StandardLuceneGrammarParser.date_return date() throws RecognitionException {
        StandardLuceneGrammarParser.date_return retval = new StandardLuceneGrammarParser.date_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token DATE_TOKEN89=null;

        Object DATE_TOKEN89_tree=null;

        try {
            // StandardLuceneGrammar.g:280:6: ( DATE_TOKEN )
            // StandardLuceneGrammar.g:282:2: DATE_TOKEN
            {
            root_0 = (Object)adaptor.nil();


            DATE_TOKEN89=(Token)match(input,DATE_TOKEN,FOLLOW_DATE_TOKEN_in_date1715); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            DATE_TOKEN89_tree = 
            (Object)adaptor.create(DATE_TOKEN89)
            ;
            adaptor.addChild(root_0, DATE_TOKEN89_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "date"

    // $ANTLR start synpred1_StandardLuceneGrammar
    public final void synpred1_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:62:2: ( modifier LPAREN ( clauseOr )+ RPAREN )
        // StandardLuceneGrammar.g:62:3: modifier LPAREN ( clauseOr )+ RPAREN
        {
        pushFollow(FOLLOW_modifier_in_synpred1_StandardLuceneGrammar365);
        modifier();

        state._fsp--;
        if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar367); if (state.failed) return ;

        // StandardLuceneGrammar.g:62:19: ( clauseOr )+
        int cnt43=0;
        loop43:
        do {
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( ((LA43_0 >= LBRACK && LA43_0 <= MINUS)||LA43_0==NUMBER||(LA43_0 >= PHRASE && LA43_0 <= PLUS)||LA43_0==QMARK||(LA43_0 >= STAR && LA43_0 <= TERM_TRUNCATED)) ) {
                alt43=1;
            }


            switch (alt43) {
        	case 1 :
        	    // StandardLuceneGrammar.g:62:19: clauseOr
        	    {
        	    pushFollow(FOLLOW_clauseOr_in_synpred1_StandardLuceneGrammar369);
        	    clauseOr();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt43 >= 1 ) break loop43;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(43, input);
                    throw eee;
            }
            cnt43++;
        } while (true);


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar372); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred1_StandardLuceneGrammar

    // $ANTLR start synpred2_StandardLuceneGrammar
    public final void synpred2_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:64:4: ( LPAREN ( clauseOr )+ RPAREN term_modifier )
        // StandardLuceneGrammar.g:64:5: LPAREN ( clauseOr )+ RPAREN term_modifier
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar426); if (state.failed) return ;

        // StandardLuceneGrammar.g:64:12: ( clauseOr )+
        int cnt44=0;
        loop44:
        do {
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( ((LA44_0 >= LBRACK && LA44_0 <= MINUS)||LA44_0==NUMBER||(LA44_0 >= PHRASE && LA44_0 <= PLUS)||LA44_0==QMARK||(LA44_0 >= STAR && LA44_0 <= TERM_TRUNCATED)) ) {
                alt44=1;
            }


            switch (alt44) {
        	case 1 :
        	    // StandardLuceneGrammar.g:64:12: clauseOr
        	    {
        	    pushFollow(FOLLOW_clauseOr_in_synpred2_StandardLuceneGrammar428);
        	    clauseOr();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt44 >= 1 ) break loop44;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(44, input);
                    throw eee;
            }
            cnt44++;
        } while (true);


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar431); if (state.failed) return ;

        pushFollow(FOLLOW_term_modifier_in_synpred2_StandardLuceneGrammar433);
        term_modifier();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_StandardLuceneGrammar

    // $ANTLR start synpred3_StandardLuceneGrammar
    public final void synpred3_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:66:4: ( LPAREN )
        // StandardLuceneGrammar.g:66:5: LPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar486); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred3_StandardLuceneGrammar

    // $ANTLR start synpred4_StandardLuceneGrammar
    public final void synpred4_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:263:2: ( AND NOT )
        // StandardLuceneGrammar.g:263:3: AND NOT
        {
        match(input,AND,FOLLOW_AND_in_synpred4_StandardLuceneGrammar1618); if (state.failed) return ;

        match(input,NOT,FOLLOW_NOT_in_synpred4_StandardLuceneGrammar1620); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred4_StandardLuceneGrammar

    // Delegated rules

    public final boolean synpred1_StandardLuceneGrammar() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_StandardLuceneGrammar_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_StandardLuceneGrammar() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_StandardLuceneGrammar_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_StandardLuceneGrammar() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_StandardLuceneGrammar_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_StandardLuceneGrammar() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_StandardLuceneGrammar_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_clauseOr_in_mainQ158 = new BitSet(new long[]{0x00070027203C0002L});
    public static final BitSet FOLLOW_clauseAnd_in_clauseOr191 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_or_in_clauseOr200 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_clauseAnd_in_clauseOr204 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_clauseNot_in_clauseAnd233 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_and_in_clauseAnd243 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_clauseNot_in_clauseAnd247 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_clauseNear_in_clauseNot278 = new BitSet(new long[]{0x0000000008000042L});
    public static final BitSet FOLLOW_not_in_clauseNot287 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_clauseNear_in_clauseNot291 = new BitSet(new long[]{0x0000000008000042L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseNear322 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_near_in_clauseNear331 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseNear335 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_modifier_in_clauseBasic377 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic380 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic382 = new BitSet(new long[]{0x00074027203C0000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic385 = new BitSet(new long[]{0x0008000000000202L});
    public static final BitSet FOLLOW_term_modifier_in_clauseBasic387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_clauseBasic437 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic440 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic442 = new BitSet(new long[]{0x00074027203C0000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic445 = new BitSet(new long[]{0x0008000000000202L});
    public static final BitSet FOLLOW_term_modifier_in_clauseBasic447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic491 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic493 = new BitSet(new long[]{0x00074027203C0000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_clauseBasic508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom529 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_field_in_atom532 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_multi_value_in_atom534 = new BitSet(new long[]{0x0008000000000202L});
    public static final BitSet FOLLOW_term_modifier_in_atom536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom572 = new BitSet(new long[]{0x00060023200C0000L});
    public static final BitSet FOLLOW_field_in_atom575 = new BitSet(new long[]{0x00060023200C0000L});
    public static final BitSet FOLLOW_value_in_atom578 = new BitSet(new long[]{0x0008000000000202L});
    public static final BitSet FOLLOW_term_modifier_in_atom580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_atom616 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_COLON_in_atom618 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_STAR_in_atom622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_NORMAL_in_field649 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_COLON_in_field651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_in_in_value670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_ex_in_value683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_value697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_value711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_value725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_value738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QMARK_in_value751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACK_in_range_term_in783 = new BitSet(new long[]{0x0007000320001000L});
    public static final BitSet FOLLOW_range_value_in_range_term_in795 = new BitSet(new long[]{0x0027100320001000L});
    public static final BitSet FOLLOW_TO_in_range_term_in818 = new BitSet(new long[]{0x0007000320001000L});
    public static final BitSet FOLLOW_range_value_in_range_term_in823 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_RBRACK_in_range_term_in844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LCURLY_in_range_term_ex864 = new BitSet(new long[]{0x0007000320001000L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex877 = new BitSet(new long[]{0x0027200320001000L});
    public static final BitSet FOLLOW_TO_in_range_term_ex900 = new BitSet(new long[]{0x0007000320001000L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex905 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_RCURLY_in_range_term_ex926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_range_value940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_range_value953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_range_value966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_in_range_value979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_range_value992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_range_value1006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_multi_value1027 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_multiClause_in_multi_value1029 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_RPAREN_in_multi_value1031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clauseOr_in_multiClause1050 = new BitSet(new long[]{0x00070027203C0002L});
    public static final BitSet FOLLOW_multiOr_in_multiDefault1088 = new BitSet(new long[]{0x00060027202C0002L});
    public static final BitSet FOLLOW_multiAnd_in_multiOr1116 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_or_in_multiOr1126 = new BitSet(new long[]{0x00060027202C0000L});
    public static final BitSet FOLLOW_multiAnd_in_multiOr1130 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_multiNot_in_multiAnd1161 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_and_in_multiAnd1171 = new BitSet(new long[]{0x00060027202C0000L});
    public static final BitSet FOLLOW_multiNot_in_multiAnd1175 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_multiNear_in_multiNot1206 = new BitSet(new long[]{0x0000000008000042L});
    public static final BitSet FOLLOW_not_in_multiNot1216 = new BitSet(new long[]{0x00060027202C0000L});
    public static final BitSet FOLLOW_multiNear_in_multiNot1220 = new BitSet(new long[]{0x0000000008000042L});
    public static final BitSet FOLLOW_multiBasic_in_multiNear1250 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_near_in_multiNear1260 = new BitSet(new long[]{0x00060027202C0000L});
    public static final BitSet FOLLOW_multiBasic_in_multiNear1264 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_mterm_in_multiBasic1290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_mterm1306 = new BitSet(new long[]{0x00060023200C0000L});
    public static final BitSet FOLLOW_value_in_mterm1309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_TRUNCATED_in_truncated1366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_in_quoted1393 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_operator1409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_operator1419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_operator1429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_operator1439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_modifier1456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_modifier1466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1484 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1486 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1507 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1509 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_boost1541 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_NUMBER_in_boost1556 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_fuzzy1579 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_NUMBER_in_fuzzy1594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_not1624 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_NOT_in_not1626 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_not1631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_and1645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_or1659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_near1674 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_57_in_near1687 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_NUMBER_in_near1691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DATE_TOKEN_in_date1715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_synpred1_StandardLuceneGrammar365 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar367 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_clauseOr_in_synpred1_StandardLuceneGrammar369 = new BitSet(new long[]{0x00074027203C0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar426 = new BitSet(new long[]{0x00070027203C0000L});
    public static final BitSet FOLLOW_clauseOr_in_synpred2_StandardLuceneGrammar428 = new BitSet(new long[]{0x00074027203C0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar431 = new BitSet(new long[]{0x0008000000000200L});
    public static final BitSet FOLLOW_term_modifier_in_synpred2_StandardLuceneGrammar433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar486 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_synpred4_StandardLuceneGrammar1618 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_NOT_in_synpred4_StandardLuceneGrammar1620 = new BitSet(new long[]{0x0000000000000002L});

}