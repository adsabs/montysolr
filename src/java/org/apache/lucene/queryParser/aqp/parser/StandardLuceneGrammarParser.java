// $ANTLR 3.4 StandardLuceneGrammar.g 2011-11-04 21:01:02

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AMPER", "AND", "ATOM", "BOOST", "CARAT", "CLAUSE", "COLON", "DATE_TOKEN", "DQUOTE", "ESC_CHAR", "FIELD", "FUZZY", "INT", "LBRACK", "LCURLY", "LPAREN", "MINUS", "MODIFIER", "NEAR", "NOT", "NUMBER", "OPERATOR", "OR", "PHRASE", "PHRASE_ANYTHING", "PLUS", "QANYTHING", "QDATE", "QMARK", "QNORMAL", "QPHRASE", "QPHRASETRUNC", "QRANGEEX", "QRANGEIN", "QTRUNCATED", "RBRACK", "RCURLY", "RPAREN", "SQUOTE", "STAR", "TERM_CHAR", "TERM_NORMAL", "TERM_START_CHAR", "TERM_TRUNCATED", "TILDE", "TMODIFIER", "TO", "VBAR", "WS", "'/'"
    };

    public static final int EOF=-1;
    public static final int T__53=53;
    public static final int AMPER=4;
    public static final int AND=5;
    public static final int ATOM=6;
    public static final int BOOST=7;
    public static final int CARAT=8;
    public static final int CLAUSE=9;
    public static final int COLON=10;
    public static final int DATE_TOKEN=11;
    public static final int DQUOTE=12;
    public static final int ESC_CHAR=13;
    public static final int FIELD=14;
    public static final int FUZZY=15;
    public static final int INT=16;
    public static final int LBRACK=17;
    public static final int LCURLY=18;
    public static final int LPAREN=19;
    public static final int MINUS=20;
    public static final int MODIFIER=21;
    public static final int NEAR=22;
    public static final int NOT=23;
    public static final int NUMBER=24;
    public static final int OPERATOR=25;
    public static final int OR=26;
    public static final int PHRASE=27;
    public static final int PHRASE_ANYTHING=28;
    public static final int PLUS=29;
    public static final int QANYTHING=30;
    public static final int QDATE=31;
    public static final int QMARK=32;
    public static final int QNORMAL=33;
    public static final int QPHRASE=34;
    public static final int QPHRASETRUNC=35;
    public static final int QRANGEEX=36;
    public static final int QRANGEIN=37;
    public static final int QTRUNCATED=38;
    public static final int RBRACK=39;
    public static final int RCURLY=40;
    public static final int RPAREN=41;
    public static final int SQUOTE=42;
    public static final int STAR=43;
    public static final int TERM_CHAR=44;
    public static final int TERM_NORMAL=45;
    public static final int TERM_START_CHAR=46;
    public static final int TERM_TRUNCATED=47;
    public static final int TILDE=48;
    public static final int TMODIFIER=49;
    public static final int TO=50;
    public static final int VBAR=51;
    public static final int WS=52;

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
    // StandardLuceneGrammar.g:34:1: mainQ : ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
    public final StandardLuceneGrammarParser.mainQ_return mainQ() throws RecognitionException {
        StandardLuceneGrammarParser.mainQ_return retval = new StandardLuceneGrammarParser.mainQ_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseOr_return clauseOr1 =null;


        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // StandardLuceneGrammar.g:34:7: ( ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
            // StandardLuceneGrammar.g:35:2: ( clauseOr )+
            {
            // StandardLuceneGrammar.g:35:2: ( clauseOr )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= LBRACK && LA1_0 <= MINUS)||LA1_0==NUMBER||(LA1_0 >= PHRASE && LA1_0 <= PLUS)||LA1_0==QMARK||LA1_0==STAR||LA1_0==TERM_NORMAL||LA1_0==TERM_TRUNCATED) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // StandardLuceneGrammar.g:35:2: clauseOr
            	    {
            	    pushFollow(FOLLOW_clauseOr_in_mainQ133);
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
            // 35:12: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
            {
                // StandardLuceneGrammar.g:35:15: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
    // StandardLuceneGrammar.g:39:1: clauseOr : (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* ;
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
            // StandardLuceneGrammar.g:40:3: ( (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* )
            // StandardLuceneGrammar.g:40:5: (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
            {
            // StandardLuceneGrammar.g:40:5: (first= clauseAnd -> $first)
            // StandardLuceneGrammar.g:40:6: first= clauseAnd
            {
            pushFollow(FOLLOW_clauseAnd_in_clauseOr165);
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
            // 40:22: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:40:33: ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==OR) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // StandardLuceneGrammar.g:40:34: or others= clauseAnd
            	    {
            	    pushFollow(FOLLOW_or_in_clauseOr174);
            	    or2=or();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_or.add(or2.getTree());

            	    pushFollow(FOLLOW_clauseAnd_in_clauseOr178);
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
            	    // 40:54: -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
            	    {
            	        // StandardLuceneGrammar.g:40:57: ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
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
    // StandardLuceneGrammar.g:43:1: clauseAnd : (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* ;
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
            // StandardLuceneGrammar.g:44:3: ( (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* )
            // StandardLuceneGrammar.g:44:5: (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
            {
            // StandardLuceneGrammar.g:44:5: (first= clauseNot -> $first)
            // StandardLuceneGrammar.g:44:6: first= clauseNot
            {
            pushFollow(FOLLOW_clauseNot_in_clauseAnd207);
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
            // 44:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:44:34: ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==AND) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // StandardLuceneGrammar.g:44:35: and others= clauseNot
            	    {
            	    pushFollow(FOLLOW_and_in_clauseAnd217);
            	    and3=and();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_and.add(and3.getTree());

            	    pushFollow(FOLLOW_clauseNot_in_clauseAnd221);
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
            	    // 44:56: -> ^( OPERATOR[\"AND\"] ( clauseNot )+ )
            	    {
            	        // StandardLuceneGrammar.g:44:59: ^( OPERATOR[\"AND\"] ( clauseNot )+ )
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
    // StandardLuceneGrammar.g:47:1: clauseNot : (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* ;
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
            // StandardLuceneGrammar.g:48:3: ( (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* )
            // StandardLuceneGrammar.g:48:5: (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
            {
            // StandardLuceneGrammar.g:48:5: (first= clauseNear -> $first)
            // StandardLuceneGrammar.g:48:6: first= clauseNear
            {
            pushFollow(FOLLOW_clauseNear_in_clauseNot252);
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
            // 48:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:48:34: ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
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
            	    // StandardLuceneGrammar.g:48:35: not others= clauseNear
            	    {
            	    pushFollow(FOLLOW_not_in_clauseNot261);
            	    not4=not();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_not.add(not4.getTree());

            	    pushFollow(FOLLOW_clauseNear_in_clauseNot265);
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
            	    // 48:57: -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
            	    {
            	        // StandardLuceneGrammar.g:48:60: ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
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
    // StandardLuceneGrammar.g:51:1: clauseNear : (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* ;
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
            // StandardLuceneGrammar.g:52:3: ( (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* )
            // StandardLuceneGrammar.g:52:5: (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
            {
            // StandardLuceneGrammar.g:52:5: (first= clauseBasic -> $first)
            // StandardLuceneGrammar.g:52:6: first= clauseBasic
            {
            pushFollow(FOLLOW_clauseBasic_in_clauseNear296);
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
            // 52:24: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:52:35: ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==NEAR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // StandardLuceneGrammar.g:52:36: near others= clauseBasic
            	    {
            	    pushFollow(FOLLOW_near_in_clauseNear305);
            	    near5=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near5.getTree());

            	    pushFollow(FOLLOW_clauseBasic_in_clauseNear309);
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
            	    // 52:60: -> ^( near ( clauseBasic )+ )
            	    {
            	        // StandardLuceneGrammar.g:52:63: ^( near ( clauseBasic )+ )
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
    // StandardLuceneGrammar.g:55:1: clauseBasic : ( ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom );
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
            // StandardLuceneGrammar.g:56:2: ( ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom )
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
                    // StandardLuceneGrammar.g:57:2: ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )?
                    {
                    // StandardLuceneGrammar.g:57:40: ( modifier )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==MINUS||LA6_0==PLUS) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // StandardLuceneGrammar.g:57:40: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_clauseBasic350);
                            modifier6=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier6.getTree());

                            }
                            break;

                    }


                    LPAREN7=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic353); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN7);


                    // StandardLuceneGrammar.g:57:57: ( clauseOr )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0 >= LBRACK && LA7_0 <= MINUS)||LA7_0==NUMBER||(LA7_0 >= PHRASE && LA7_0 <= PLUS)||LA7_0==QMARK||LA7_0==STAR||LA7_0==TERM_NORMAL||LA7_0==TERM_TRUNCATED) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:57:57: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic355);
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


                    RPAREN9=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic358); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN9);


                    // StandardLuceneGrammar.g:57:74: ( term_modifier )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==CARAT||LA8_0==TILDE) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // StandardLuceneGrammar.g:57:74: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_clauseBasic360);
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
                    // 58:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                    {
                        // StandardLuceneGrammar.g:58:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // StandardLuceneGrammar.g:58:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:58:26: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // StandardLuceneGrammar.g:58:36: ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // StandardLuceneGrammar.g:58:48: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // StandardLuceneGrammar.g:58:63: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
                    // StandardLuceneGrammar.g:59:4: ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )?
                    {
                    // StandardLuceneGrammar.g:59:46: ( modifier )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==MINUS||LA9_0==PLUS) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // StandardLuceneGrammar.g:59:46: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_clauseBasic410);
                            modifier11=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier11.getTree());

                            }
                            break;

                    }


                    LPAREN12=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic413); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN12);


                    // StandardLuceneGrammar.g:59:63: ( clauseOr )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0 >= LBRACK && LA10_0 <= MINUS)||LA10_0==NUMBER||(LA10_0 >= PHRASE && LA10_0 <= PLUS)||LA10_0==QMARK||LA10_0==STAR||LA10_0==TERM_NORMAL||LA10_0==TERM_TRUNCATED) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:59:63: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic415);
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


                    RPAREN14=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic418); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN14);


                    // StandardLuceneGrammar.g:59:80: ( term_modifier )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==CARAT||LA11_0==TILDE) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // StandardLuceneGrammar.g:59:80: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_clauseBasic420);
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
                    // 60:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                    {
                        // StandardLuceneGrammar.g:60:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // StandardLuceneGrammar.g:60:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:60:26: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // StandardLuceneGrammar.g:60:36: ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // StandardLuceneGrammar.g:60:48: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // StandardLuceneGrammar.g:60:63: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
                    // StandardLuceneGrammar.g:61:4: ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN
                    {
                    LPAREN16=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic464); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN16);


                    // StandardLuceneGrammar.g:61:23: ( clauseOr )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0 >= LBRACK && LA12_0 <= MINUS)||LA12_0==NUMBER||(LA12_0 >= PHRASE && LA12_0 <= PLUS)||LA12_0==QMARK||LA12_0==STAR||LA12_0==TERM_NORMAL||LA12_0==TERM_TRUNCATED) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:61:23: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic466);
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


                    RPAREN18=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic469); if (state.failed) return retval; 
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
                    // 62:3: -> ( clauseOr )+
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
                    // StandardLuceneGrammar.g:63:4: atom
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_atom_in_clauseBasic481);
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
    // StandardLuceneGrammar.g:67:1: atom : ( ( modifier )? field multi_value ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) ) | ( modifier )? ( STAR COLON )? STAR -> ^( MODIFIER ( modifier )? ^( QANYTHING STAR[\"*\"] ) ) );
    public final StandardLuceneGrammarParser.atom_return atom() throws RecognitionException {
        StandardLuceneGrammarParser.atom_return retval = new StandardLuceneGrammarParser.atom_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR29=null;
        Token COLON30=null;
        Token STAR31=null;
        StandardLuceneGrammarParser.modifier_return modifier20 =null;

        StandardLuceneGrammarParser.field_return field21 =null;

        StandardLuceneGrammarParser.multi_value_return multi_value22 =null;

        StandardLuceneGrammarParser.term_modifier_return term_modifier23 =null;

        StandardLuceneGrammarParser.modifier_return modifier24 =null;

        StandardLuceneGrammarParser.field_return field25 =null;

        StandardLuceneGrammarParser.value_return value26 =null;

        StandardLuceneGrammarParser.term_modifier_return term_modifier27 =null;

        StandardLuceneGrammarParser.modifier_return modifier28 =null;


        Object STAR29_tree=null;
        Object COLON30_tree=null;
        Object STAR31_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_multi_value=new RewriteRuleSubtreeStream(adaptor,"rule multi_value");
        try {
            // StandardLuceneGrammar.g:68:2: ( ( modifier )? field multi_value ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) ) | ( modifier )? ( STAR COLON )? STAR -> ^( MODIFIER ( modifier )? ^( QANYTHING STAR[\"*\"] ) ) )
            int alt21=3;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                switch ( input.LA(2) ) {
                case TERM_NORMAL:
                    {
                    int LA21_3 = input.LA(3);

                    if ( (LA21_3==COLON) ) {
                        int LA21_6 = input.LA(4);

                        if ( (LA21_6==LPAREN) ) {
                            alt21=1;
                        }
                        else if ( ((LA21_6 >= LBRACK && LA21_6 <= LCURLY)||LA21_6==NUMBER||(LA21_6 >= PHRASE && LA21_6 <= PHRASE_ANYTHING)||LA21_6==QMARK||LA21_6==TERM_NORMAL||LA21_6==TERM_TRUNCATED) ) {
                            alt21=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 21, 6, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA21_3==EOF||LA21_3==AND||LA21_3==CARAT||(LA21_3 >= LBRACK && LA21_3 <= MINUS)||(LA21_3 >= NEAR && LA21_3 <= NUMBER)||(LA21_3 >= OR && LA21_3 <= PLUS)||LA21_3==QMARK||LA21_3==RPAREN||LA21_3==STAR||LA21_3==TERM_NORMAL||(LA21_3 >= TERM_TRUNCATED && LA21_3 <= TILDE)) ) {
                        alt21=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 3, input);

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
                    alt21=2;
                    }
                    break;
                case STAR:
                    {
                    alt21=3;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;

                }

                }
                break;
            case MINUS:
                {
                switch ( input.LA(2) ) {
                case TERM_NORMAL:
                    {
                    int LA21_3 = input.LA(3);

                    if ( (LA21_3==COLON) ) {
                        int LA21_6 = input.LA(4);

                        if ( (LA21_6==LPAREN) ) {
                            alt21=1;
                        }
                        else if ( ((LA21_6 >= LBRACK && LA21_6 <= LCURLY)||LA21_6==NUMBER||(LA21_6 >= PHRASE && LA21_6 <= PHRASE_ANYTHING)||LA21_6==QMARK||LA21_6==TERM_NORMAL||LA21_6==TERM_TRUNCATED) ) {
                            alt21=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 21, 6, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA21_3==EOF||LA21_3==AND||LA21_3==CARAT||(LA21_3 >= LBRACK && LA21_3 <= MINUS)||(LA21_3 >= NEAR && LA21_3 <= NUMBER)||(LA21_3 >= OR && LA21_3 <= PLUS)||LA21_3==QMARK||LA21_3==RPAREN||LA21_3==STAR||LA21_3==TERM_NORMAL||(LA21_3 >= TERM_TRUNCATED && LA21_3 <= TILDE)) ) {
                        alt21=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 3, input);

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
                    alt21=2;
                    }
                    break;
                case STAR:
                    {
                    alt21=3;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 2, input);

                    throw nvae;

                }

                }
                break;
            case TERM_NORMAL:
                {
                int LA21_3 = input.LA(2);

                if ( (LA21_3==COLON) ) {
                    int LA21_6 = input.LA(3);

                    if ( (LA21_6==LPAREN) ) {
                        alt21=1;
                    }
                    else if ( ((LA21_6 >= LBRACK && LA21_6 <= LCURLY)||LA21_6==NUMBER||(LA21_6 >= PHRASE && LA21_6 <= PHRASE_ANYTHING)||LA21_6==QMARK||LA21_6==TERM_NORMAL||LA21_6==TERM_TRUNCATED) ) {
                        alt21=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 6, input);

                        throw nvae;

                    }
                }
                else if ( (LA21_3==EOF||LA21_3==AND||LA21_3==CARAT||(LA21_3 >= LBRACK && LA21_3 <= MINUS)||(LA21_3 >= NEAR && LA21_3 <= NUMBER)||(LA21_3 >= OR && LA21_3 <= PLUS)||LA21_3==QMARK||LA21_3==RPAREN||LA21_3==STAR||LA21_3==TERM_NORMAL||(LA21_3 >= TERM_TRUNCATED && LA21_3 <= TILDE)) ) {
                    alt21=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 3, input);

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
                alt21=2;
                }
                break;
            case STAR:
                {
                alt21=3;
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
                    // StandardLuceneGrammar.g:69:2: ( modifier )? field multi_value ( term_modifier )?
                    {
                    // StandardLuceneGrammar.g:69:2: ( modifier )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==MINUS||LA14_0==PLUS) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // StandardLuceneGrammar.g:69:2: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom502);
                            modifier20=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier20.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_field_in_atom505);
                    field21=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field21.getTree());

                    pushFollow(FOLLOW_multi_value_in_atom507);
                    multi_value22=multi_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_multi_value.add(multi_value22.getTree());

                    // StandardLuceneGrammar.g:69:30: ( term_modifier )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==CARAT||LA15_0==TILDE) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // StandardLuceneGrammar.g:69:30: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_atom509);
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
                    // 70:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) )
                    {
                        // StandardLuceneGrammar.g:70:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // StandardLuceneGrammar.g:70:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:70:26: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // StandardLuceneGrammar.g:70:36: ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // StandardLuceneGrammar.g:70:48: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // StandardLuceneGrammar.g:70:63: ^( FIELD field multi_value )
                        {
                        Object root_4 = (Object)adaptor.nil();
                        root_4 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_4);

                        adaptor.addChild(root_4, stream_field.nextTree());

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
                    // StandardLuceneGrammar.g:71:4: ( modifier )? ( field )? value ( term_modifier )?
                    {
                    // StandardLuceneGrammar.g:71:4: ( modifier )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==MINUS||LA16_0==PLUS) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // StandardLuceneGrammar.g:71:4: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom545);
                            modifier24=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier24.getTree());

                            }
                            break;

                    }


                    // StandardLuceneGrammar.g:71:14: ( field )?
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
                            // StandardLuceneGrammar.g:71:14: field
                            {
                            pushFollow(FOLLOW_field_in_atom548);
                            field25=field();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_field.add(field25.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_value_in_atom551);
                    value26=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value26.getTree());

                    // StandardLuceneGrammar.g:71:27: ( term_modifier )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==CARAT||LA18_0==TILDE) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // StandardLuceneGrammar.g:71:27: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_atom553);
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
                    // 72:3: -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) )
                    {
                        // StandardLuceneGrammar.g:72:6: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:72:17: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // StandardLuceneGrammar.g:72:27: ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:72:39: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // StandardLuceneGrammar.g:72:54: ^( FIELD ( field )? value )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_3);

                        // StandardLuceneGrammar.g:72:62: ( field )?
                        if ( stream_field.hasNext() ) {
                            adaptor.addChild(root_3, stream_field.nextTree());

                        }
                        stream_field.reset();

                        adaptor.addChild(root_3, stream_value.nextTree());

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
                    // StandardLuceneGrammar.g:73:4: ( modifier )? ( STAR COLON )? STAR
                    {
                    // StandardLuceneGrammar.g:73:4: ( modifier )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==MINUS||LA19_0==PLUS) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // StandardLuceneGrammar.g:73:4: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom587);
                            modifier28=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier28.getTree());

                            }
                            break;

                    }


                    // StandardLuceneGrammar.g:73:14: ( STAR COLON )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==STAR) ) {
                        int LA20_1 = input.LA(2);

                        if ( (LA20_1==COLON) ) {
                            alt20=1;
                        }
                    }
                    switch (alt20) {
                        case 1 :
                            // StandardLuceneGrammar.g:73:15: STAR COLON
                            {
                            STAR29=(Token)match(input,STAR,FOLLOW_STAR_in_atom591); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(STAR29);


                            COLON30=(Token)match(input,COLON,FOLLOW_COLON_in_atom593); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COLON.add(COLON30);


                            }
                            break;

                    }


                    STAR31=(Token)match(input,STAR,FOLLOW_STAR_in_atom597); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR31);


                    // AST REWRITE
                    // elements: modifier, STAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 74:3: -> ^( MODIFIER ( modifier )? ^( QANYTHING STAR[\"*\"] ) )
                    {
                        // StandardLuceneGrammar.g:74:6: ^( MODIFIER ( modifier )? ^( QANYTHING STAR[\"*\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:74:17: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // StandardLuceneGrammar.g:74:27: ^( QANYTHING STAR[\"*\"] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QANYTHING, "QANYTHING")
                        , root_2);

                        adaptor.addChild(root_2, 
                        (Object)adaptor.create(STAR, "*")
                        );

                        adaptor.addChild(root_1, root_2);
                        }

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
    // StandardLuceneGrammar.g:78:1: field : TERM_NORMAL COLON -> TERM_NORMAL ;
    public final StandardLuceneGrammarParser.field_return field() throws RecognitionException {
        StandardLuceneGrammarParser.field_return retval = new StandardLuceneGrammarParser.field_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_NORMAL32=null;
        Token COLON33=null;

        Object TERM_NORMAL32_tree=null;
        Object COLON33_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_TERM_NORMAL=new RewriteRuleTokenStream(adaptor,"token TERM_NORMAL");

        try {
            // StandardLuceneGrammar.g:79:2: ( TERM_NORMAL COLON -> TERM_NORMAL )
            // StandardLuceneGrammar.g:80:2: TERM_NORMAL COLON
            {
            TERM_NORMAL32=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_field634); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL32);


            COLON33=(Token)match(input,COLON,FOLLOW_COLON_in_field636); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(COLON33);


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
            // 80:20: -> TERM_NORMAL
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
    // StandardLuceneGrammar.g:83:1: value : ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) );
    public final StandardLuceneGrammarParser.value_return value() throws RecognitionException {
        StandardLuceneGrammarParser.value_return retval = new StandardLuceneGrammarParser.value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token QMARK40=null;
        StandardLuceneGrammarParser.range_term_in_return range_term_in34 =null;

        StandardLuceneGrammarParser.range_term_ex_return range_term_ex35 =null;

        StandardLuceneGrammarParser.normal_return normal36 =null;

        StandardLuceneGrammarParser.truncated_return truncated37 =null;

        StandardLuceneGrammarParser.quoted_return quoted38 =null;

        StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated39 =null;


        Object QMARK40_tree=null;
        RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
        RewriteRuleSubtreeStream stream_range_term_ex=new RewriteRuleSubtreeStream(adaptor,"rule range_term_ex");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_range_term_in=new RewriteRuleSubtreeStream(adaptor,"rule range_term_in");
        try {
            // StandardLuceneGrammar.g:84:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) )
            int alt22=7;
            switch ( input.LA(1) ) {
            case LBRACK:
                {
                alt22=1;
                }
                break;
            case LCURLY:
                {
                alt22=2;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
                {
                alt22=3;
                }
                break;
            case TERM_TRUNCATED:
                {
                alt22=4;
                }
                break;
            case PHRASE:
                {
                alt22=5;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt22=6;
                }
                break;
            case QMARK:
                {
                alt22=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;

            }

            switch (alt22) {
                case 1 :
                    // StandardLuceneGrammar.g:85:2: range_term_in
                    {
                    pushFollow(FOLLOW_range_term_in_in_value655);
                    range_term_in34=range_term_in();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_in.add(range_term_in34.getTree());

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
                    // 85:16: -> ^( QRANGEIN range_term_in )
                    {
                        // StandardLuceneGrammar.g:85:19: ^( QRANGEIN range_term_in )
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
                    // StandardLuceneGrammar.g:86:4: range_term_ex
                    {
                    pushFollow(FOLLOW_range_term_ex_in_value668);
                    range_term_ex35=range_term_ex();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_ex.add(range_term_ex35.getTree());

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
                    // 86:18: -> ^( QRANGEEX range_term_ex )
                    {
                        // StandardLuceneGrammar.g:86:21: ^( QRANGEEX range_term_ex )
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
                    // StandardLuceneGrammar.g:87:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_value682);
                    normal36=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal36.getTree());

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
                    // 87:11: -> ^( QNORMAL normal )
                    {
                        // StandardLuceneGrammar.g:87:14: ^( QNORMAL normal )
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
                    // StandardLuceneGrammar.g:88:4: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_value696);
                    truncated37=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated37.getTree());

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
                    // 88:14: -> ^( QTRUNCATED truncated )
                    {
                        // StandardLuceneGrammar.g:88:17: ^( QTRUNCATED truncated )
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
                    // StandardLuceneGrammar.g:89:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_value710);
                    quoted38=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted38.getTree());

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
                    // 89:11: -> ^( QPHRASE quoted )
                    {
                        // StandardLuceneGrammar.g:89:14: ^( QPHRASE quoted )
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
                    // StandardLuceneGrammar.g:90:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_value723);
                    quoted_truncated39=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated39.getTree());

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
                    // 90:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // StandardLuceneGrammar.g:90:24: ^( QPHRASETRUNC quoted_truncated )
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
                    // StandardLuceneGrammar.g:91:4: QMARK
                    {
                    QMARK40=(Token)match(input,QMARK,FOLLOW_QMARK_in_value736); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QMARK.add(QMARK40);


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
                    // 91:10: -> ^( QTRUNCATED QMARK )
                    {
                        // StandardLuceneGrammar.g:91:13: ^( QTRUNCATED QMARK )
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
    // StandardLuceneGrammar.g:96:1: range_term_in : LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK ;
    public final StandardLuceneGrammarParser.range_term_in_return range_term_in() throws RecognitionException {
        StandardLuceneGrammarParser.range_term_in_return retval = new StandardLuceneGrammarParser.range_term_in_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LBRACK41=null;
        Token TO42=null;
        Token RBRACK43=null;
        StandardLuceneGrammarParser.range_value_return a =null;

        StandardLuceneGrammarParser.range_value_return b =null;


        Object LBRACK41_tree=null;
        Object TO42_tree=null;
        Object RBRACK43_tree=null;
        RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
        RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // StandardLuceneGrammar.g:97:2: ( LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK )
            // StandardLuceneGrammar.g:98:8: LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK
            {
            LBRACK41=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_range_term_in768); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK41);


            // StandardLuceneGrammar.g:99:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // StandardLuceneGrammar.g:99:9: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_in780);
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
            // 99:23: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // StandardLuceneGrammar.g:99:38: ^( QANYTHING QANYTHING[\"*\"] )
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


            // StandardLuceneGrammar.g:100:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==DATE_TOKEN||LA24_0==NUMBER||(LA24_0 >= PHRASE && LA24_0 <= PHRASE_ANYTHING)||LA24_0==STAR||LA24_0==TERM_NORMAL||LA24_0==TERM_TRUNCATED||LA24_0==TO) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // StandardLuceneGrammar.g:100:10: ( TO )? b= range_value
                    {
                    // StandardLuceneGrammar.g:100:10: ( TO )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==TO) ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // StandardLuceneGrammar.g:100:10: TO
                            {
                            TO42=(Token)match(input,TO,FOLLOW_TO_in_range_term_in803); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO42);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_in808);
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
                    // 100:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // StandardLuceneGrammar.g:100:35: ( $b)?
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


            RBRACK43=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_range_term_in829); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACK.add(RBRACK43);


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
    // StandardLuceneGrammar.g:105:1: range_term_ex : LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY ;
    public final StandardLuceneGrammarParser.range_term_ex_return range_term_ex() throws RecognitionException {
        StandardLuceneGrammarParser.range_term_ex_return retval = new StandardLuceneGrammarParser.range_term_ex_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LCURLY44=null;
        Token TO45=null;
        Token RCURLY46=null;
        StandardLuceneGrammarParser.range_value_return a =null;

        StandardLuceneGrammarParser.range_value_return b =null;


        Object LCURLY44_tree=null;
        Object TO45_tree=null;
        Object RCURLY46_tree=null;
        RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // StandardLuceneGrammar.g:106:2: ( LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY )
            // StandardLuceneGrammar.g:107:8: LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY
            {
            LCURLY44=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_range_term_ex849); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LCURLY.add(LCURLY44);


            // StandardLuceneGrammar.g:108:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // StandardLuceneGrammar.g:108:10: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_ex862);
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
            // 108:24: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // StandardLuceneGrammar.g:108:39: ^( QANYTHING QANYTHING[\"*\"] )
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


            // StandardLuceneGrammar.g:109:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==DATE_TOKEN||LA26_0==NUMBER||(LA26_0 >= PHRASE && LA26_0 <= PHRASE_ANYTHING)||LA26_0==STAR||LA26_0==TERM_NORMAL||LA26_0==TERM_TRUNCATED||LA26_0==TO) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // StandardLuceneGrammar.g:109:10: ( TO )? b= range_value
                    {
                    // StandardLuceneGrammar.g:109:10: ( TO )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==TO) ) {
                        alt25=1;
                    }
                    switch (alt25) {
                        case 1 :
                            // StandardLuceneGrammar.g:109:10: TO
                            {
                            TO45=(Token)match(input,TO,FOLLOW_TO_in_range_term_ex885); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO45);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_ex890);
                    b=range_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_value.add(b.getTree());

                    // AST REWRITE
                    // elements: a, b
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
                    // 109:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // StandardLuceneGrammar.g:109:35: ( $b)?
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


            RCURLY46=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_range_term_ex911); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RCURLY.add(RCURLY46);


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
    // StandardLuceneGrammar.g:113:1: range_value : ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) );
    public final StandardLuceneGrammarParser.range_value_return range_value() throws RecognitionException {
        StandardLuceneGrammarParser.range_value_return retval = new StandardLuceneGrammarParser.range_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR52=null;
        StandardLuceneGrammarParser.truncated_return truncated47 =null;

        StandardLuceneGrammarParser.quoted_return quoted48 =null;

        StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated49 =null;

        StandardLuceneGrammarParser.date_return date50 =null;

        StandardLuceneGrammarParser.normal_return normal51 =null;


        Object STAR52_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_date=new RewriteRuleSubtreeStream(adaptor,"rule date");
        try {
            // StandardLuceneGrammar.g:114:2: ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) )
            int alt27=6;
            switch ( input.LA(1) ) {
            case TERM_TRUNCATED:
                {
                alt27=1;
                }
                break;
            case PHRASE:
                {
                alt27=2;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt27=3;
                }
                break;
            case DATE_TOKEN:
                {
                alt27=4;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
                {
                alt27=5;
                }
                break;
            case STAR:
                {
                alt27=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;

            }

            switch (alt27) {
                case 1 :
                    // StandardLuceneGrammar.g:115:2: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_range_value925);
                    truncated47=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated47.getTree());

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
                    // 115:12: -> ^( QTRUNCATED truncated )
                    {
                        // StandardLuceneGrammar.g:115:15: ^( QTRUNCATED truncated )
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
                    // StandardLuceneGrammar.g:116:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_range_value938);
                    quoted48=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted48.getTree());

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
                    // 116:11: -> ^( QPHRASE quoted )
                    {
                        // StandardLuceneGrammar.g:116:14: ^( QPHRASE quoted )
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
                    // StandardLuceneGrammar.g:117:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_range_value951);
                    quoted_truncated49=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated49.getTree());

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
                    // 117:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // StandardLuceneGrammar.g:117:24: ^( QPHRASETRUNC quoted_truncated )
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
                    // StandardLuceneGrammar.g:118:4: date
                    {
                    pushFollow(FOLLOW_date_in_range_value964);
                    date50=date();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_date.add(date50.getTree());

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
                    // 118:9: -> ^( QNORMAL date )
                    {
                        // StandardLuceneGrammar.g:118:12: ^( QNORMAL date )
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
                    // StandardLuceneGrammar.g:119:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_range_value977);
                    normal51=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal51.getTree());

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
                    // 119:11: -> ^( QNORMAL normal )
                    {
                        // StandardLuceneGrammar.g:119:14: ^( QNORMAL normal )
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
                    // StandardLuceneGrammar.g:120:4: STAR
                    {
                    STAR52=(Token)match(input,STAR,FOLLOW_STAR_in_range_value991); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR52);


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
                    // 120:9: -> ^( QANYTHING STAR )
                    {
                        // StandardLuceneGrammar.g:120:12: ^( QANYTHING STAR )
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
    // StandardLuceneGrammar.g:123:1: multi_value : LPAREN multiClause RPAREN -> multiClause ;
    public final StandardLuceneGrammarParser.multi_value_return multi_value() throws RecognitionException {
        StandardLuceneGrammarParser.multi_value_return retval = new StandardLuceneGrammarParser.multi_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN53=null;
        Token RPAREN55=null;
        StandardLuceneGrammarParser.multiClause_return multiClause54 =null;


        Object LPAREN53_tree=null;
        Object RPAREN55_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_multiClause=new RewriteRuleSubtreeStream(adaptor,"rule multiClause");
        try {
            // StandardLuceneGrammar.g:124:2: ( LPAREN multiClause RPAREN -> multiClause )
            // StandardLuceneGrammar.g:125:2: LPAREN multiClause RPAREN
            {
            LPAREN53=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_multi_value1012); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN53);


            pushFollow(FOLLOW_multiClause_in_multi_value1014);
            multiClause54=multiClause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_multiClause.add(multiClause54.getTree());

            RPAREN55=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_multi_value1016); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN55);


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
            // 125:28: -> multiClause
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
    // StandardLuceneGrammar.g:130:1: multiClause : ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
    public final StandardLuceneGrammarParser.multiClause_return multiClause() throws RecognitionException {
        StandardLuceneGrammarParser.multiClause_return retval = new StandardLuceneGrammarParser.multiClause_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseOr_return clauseOr56 =null;


        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // StandardLuceneGrammar.g:131:2: ( ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
            // StandardLuceneGrammar.g:136:2: ( clauseOr )+
            {
            // StandardLuceneGrammar.g:136:2: ( clauseOr )+
            int cnt28=0;
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( ((LA28_0 >= LBRACK && LA28_0 <= MINUS)||LA28_0==NUMBER||(LA28_0 >= PHRASE && LA28_0 <= PLUS)||LA28_0==QMARK||LA28_0==STAR||LA28_0==TERM_NORMAL||LA28_0==TERM_TRUNCATED) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // StandardLuceneGrammar.g:136:2: clauseOr
            	    {
            	    pushFollow(FOLLOW_clauseOr_in_multiClause1043);
            	    clauseOr56=clauseOr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr56.getTree());

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
            // 136:12: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
            {
                // StandardLuceneGrammar.g:136:15: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
    // StandardLuceneGrammar.g:149:1: multiDefault : ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) ;
    public final StandardLuceneGrammarParser.multiDefault_return multiDefault() throws RecognitionException {
        StandardLuceneGrammarParser.multiDefault_return retval = new StandardLuceneGrammarParser.multiDefault_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiOr_return multiOr57 =null;


        RewriteRuleSubtreeStream stream_multiOr=new RewriteRuleSubtreeStream(adaptor,"rule multiOr");
        try {
            // StandardLuceneGrammar.g:150:2: ( ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) )
            // StandardLuceneGrammar.g:151:2: ( multiOr )+
            {
            // StandardLuceneGrammar.g:151:2: ( multiOr )+
            int cnt29=0;
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( ((LA29_0 >= LBRACK && LA29_0 <= LCURLY)||LA29_0==MINUS||LA29_0==NUMBER||(LA29_0 >= PHRASE && LA29_0 <= PLUS)||LA29_0==QMARK||LA29_0==TERM_NORMAL||LA29_0==TERM_TRUNCATED) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // StandardLuceneGrammar.g:151:2: multiOr
            	    {
            	    pushFollow(FOLLOW_multiOr_in_multiDefault1087);
            	    multiOr57=multiOr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_multiOr.add(multiOr57.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt29 >= 1 ) break loop29;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(29, input);
                        throw eee;
                }
                cnt29++;
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
            // 151:11: -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
            {
                // StandardLuceneGrammar.g:151:14: ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
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
    // StandardLuceneGrammar.g:154:1: multiOr : (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* ;
    public final StandardLuceneGrammarParser.multiOr_return multiOr() throws RecognitionException {
        StandardLuceneGrammarParser.multiOr_return retval = new StandardLuceneGrammarParser.multiOr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiAnd_return first =null;

        StandardLuceneGrammarParser.multiAnd_return others =null;

        StandardLuceneGrammarParser.or_return or58 =null;


        RewriteRuleSubtreeStream stream_multiAnd=new RewriteRuleSubtreeStream(adaptor,"rule multiAnd");
        RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
        try {
            // StandardLuceneGrammar.g:155:2: ( (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* )
            // StandardLuceneGrammar.g:156:2: (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
            {
            // StandardLuceneGrammar.g:156:2: (first= multiAnd -> $first)
            // StandardLuceneGrammar.g:156:3: first= multiAnd
            {
            pushFollow(FOLLOW_multiAnd_in_multiOr1115);
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
            // 156:19: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:156:30: ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==OR) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // StandardLuceneGrammar.g:156:31: or others= multiAnd
            	    {
            	    pushFollow(FOLLOW_or_in_multiOr1125);
            	    or58=or();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_or.add(or58.getTree());

            	    pushFollow(FOLLOW_multiAnd_in_multiOr1129);
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
            	    // 156:49: -> ^( OPERATOR[\"OR\"] ( multiAnd )+ )
            	    {
            	        // StandardLuceneGrammar.g:156:52: ^( OPERATOR[\"OR\"] ( multiAnd )+ )
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
    // $ANTLR end "multiOr"


    public static class multiAnd_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiAnd"
    // StandardLuceneGrammar.g:159:1: multiAnd : (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* ;
    public final StandardLuceneGrammarParser.multiAnd_return multiAnd() throws RecognitionException {
        StandardLuceneGrammarParser.multiAnd_return retval = new StandardLuceneGrammarParser.multiAnd_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiNot_return first =null;

        StandardLuceneGrammarParser.multiNot_return others =null;

        StandardLuceneGrammarParser.and_return and59 =null;


        RewriteRuleSubtreeStream stream_multiNot=new RewriteRuleSubtreeStream(adaptor,"rule multiNot");
        RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");
        try {
            // StandardLuceneGrammar.g:160:2: ( (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* )
            // StandardLuceneGrammar.g:161:2: (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
            {
            // StandardLuceneGrammar.g:161:2: (first= multiNot -> $first)
            // StandardLuceneGrammar.g:161:3: first= multiNot
            {
            pushFollow(FOLLOW_multiNot_in_multiAnd1160);
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
            // 161:19: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:161:30: ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==AND) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // StandardLuceneGrammar.g:161:31: and others= multiNot
            	    {
            	    pushFollow(FOLLOW_and_in_multiAnd1170);
            	    and59=and();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_and.add(and59.getTree());

            	    pushFollow(FOLLOW_multiNot_in_multiAnd1174);
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
            	    // 161:51: -> ^( OPERATOR[\"AND\"] ( multiNot )+ )
            	    {
            	        // StandardLuceneGrammar.g:161:54: ^( OPERATOR[\"AND\"] ( multiNot )+ )
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
    // $ANTLR end "multiAnd"


    public static class multiNot_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiNot"
    // StandardLuceneGrammar.g:164:1: multiNot : (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )* ;
    public final StandardLuceneGrammarParser.multiNot_return multiNot() throws RecognitionException {
        StandardLuceneGrammarParser.multiNot_return retval = new StandardLuceneGrammarParser.multiNot_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiNear_return first =null;

        StandardLuceneGrammarParser.multiNear_return others =null;

        StandardLuceneGrammarParser.not_return not60 =null;


        RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
        RewriteRuleSubtreeStream stream_multiNear=new RewriteRuleSubtreeStream(adaptor,"rule multiNear");
        try {
            // StandardLuceneGrammar.g:165:2: ( (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )* )
            // StandardLuceneGrammar.g:166:2: (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )*
            {
            // StandardLuceneGrammar.g:166:2: (first= multiNear -> $first)
            // StandardLuceneGrammar.g:166:3: first= multiNear
            {
            pushFollow(FOLLOW_multiNear_in_multiNot1205);
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
            // 166:20: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:166:31: ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )*
            loop32:
            do {
                int alt32=2;
                int LA32_0 = input.LA(1);

                if ( (LA32_0==AND) ) {
                    int LA32_1 = input.LA(2);

                    if ( (LA32_1==NOT) ) {
                        alt32=1;
                    }


                }
                else if ( (LA32_0==NOT) ) {
                    alt32=1;
                }


                switch (alt32) {
            	case 1 :
            	    // StandardLuceneGrammar.g:166:32: not others= multiNear
            	    {
            	    pushFollow(FOLLOW_not_in_multiNot1215);
            	    not60=not();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_not.add(not60.getTree());

            	    pushFollow(FOLLOW_multiNear_in_multiNot1219);
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
            	    // 166:52: -> ^( OPERATOR[\"NOT\"] ( multiNear )+ )
            	    {
            	        // StandardLuceneGrammar.g:166:55: ^( OPERATOR[\"NOT\"] ( multiNear )+ )
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
    // $ANTLR end "multiNot"


    public static class multiNear_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiNear"
    // StandardLuceneGrammar.g:169:1: multiNear : (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )* ;
    public final StandardLuceneGrammarParser.multiNear_return multiNear() throws RecognitionException {
        StandardLuceneGrammarParser.multiNear_return retval = new StandardLuceneGrammarParser.multiNear_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.multiBasic_return first =null;

        StandardLuceneGrammarParser.multiBasic_return others =null;

        StandardLuceneGrammarParser.near_return near61 =null;


        RewriteRuleSubtreeStream stream_near=new RewriteRuleSubtreeStream(adaptor,"rule near");
        RewriteRuleSubtreeStream stream_multiBasic=new RewriteRuleSubtreeStream(adaptor,"rule multiBasic");
        try {
            // StandardLuceneGrammar.g:170:2: ( (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )* )
            // StandardLuceneGrammar.g:171:2: (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )*
            {
            // StandardLuceneGrammar.g:171:2: (first= multiBasic -> $first)
            // StandardLuceneGrammar.g:171:3: first= multiBasic
            {
            pushFollow(FOLLOW_multiBasic_in_multiNear1249);
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
            // 171:21: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:171:32: ( near others= multiBasic -> ^( near ( multiBasic )+ ) )*
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==NEAR) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // StandardLuceneGrammar.g:171:33: near others= multiBasic
            	    {
            	    pushFollow(FOLLOW_near_in_multiNear1259);
            	    near61=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near61.getTree());

            	    pushFollow(FOLLOW_multiBasic_in_multiNear1263);
            	    others=multiBasic();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_multiBasic.add(others.getTree());

            	    // AST REWRITE
            	    // elements: multiBasic, near
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 171:55: -> ^( near ( multiBasic )+ )
            	    {
            	        // StandardLuceneGrammar.g:171:58: ^( near ( multiBasic )+ )
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
            	    break loop33;
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
    // StandardLuceneGrammar.g:175:1: multiBasic : mterm ;
    public final StandardLuceneGrammarParser.multiBasic_return multiBasic() throws RecognitionException {
        StandardLuceneGrammarParser.multiBasic_return retval = new StandardLuceneGrammarParser.multiBasic_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.mterm_return mterm62 =null;



        try {
            // StandardLuceneGrammar.g:176:2: ( mterm )
            // StandardLuceneGrammar.g:177:2: mterm
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_mterm_in_multiBasic1289);
            mterm62=mterm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mterm62.getTree());

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
    // StandardLuceneGrammar.g:180:1: mterm : ( modifier )? value -> ^( MODIFIER ( modifier )? value ) ;
    public final StandardLuceneGrammarParser.mterm_return mterm() throws RecognitionException {
        StandardLuceneGrammarParser.mterm_return retval = new StandardLuceneGrammarParser.mterm_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.modifier_return modifier63 =null;

        StandardLuceneGrammarParser.value_return value64 =null;


        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try {
            // StandardLuceneGrammar.g:181:2: ( ( modifier )? value -> ^( MODIFIER ( modifier )? value ) )
            // StandardLuceneGrammar.g:182:2: ( modifier )? value
            {
            // StandardLuceneGrammar.g:182:2: ( modifier )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==MINUS||LA34_0==PLUS) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // StandardLuceneGrammar.g:182:2: modifier
                    {
                    pushFollow(FOLLOW_modifier_in_mterm1305);
                    modifier63=modifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_modifier.add(modifier63.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_value_in_mterm1308);
            value64=value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_value.add(value64.getTree());

            // AST REWRITE
            // elements: modifier, value
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 182:18: -> ^( MODIFIER ( modifier )? value )
            {
                // StandardLuceneGrammar.g:182:21: ^( MODIFIER ( modifier )? value )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(MODIFIER, "MODIFIER")
                , root_1);

                // StandardLuceneGrammar.g:182:32: ( modifier )?
                if ( stream_modifier.hasNext() ) {
                    adaptor.addChild(root_1, stream_modifier.nextTree());

                }
                stream_modifier.reset();

                adaptor.addChild(root_1, stream_value.nextTree());

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
    // StandardLuceneGrammar.g:186:1: normal : ( TERM_NORMAL | NUMBER );
    public final StandardLuceneGrammarParser.normal_return normal() throws RecognitionException {
        StandardLuceneGrammarParser.normal_return retval = new StandardLuceneGrammarParser.normal_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set65=null;

        Object set65_tree=null;

        try {
            // StandardLuceneGrammar.g:187:2: ( TERM_NORMAL | NUMBER )
            // StandardLuceneGrammar.g:
            {
            root_0 = (Object)adaptor.nil();


            set65=(Token)input.LT(1);

            if ( input.LA(1)==NUMBER||input.LA(1)==TERM_NORMAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set65)
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
    // StandardLuceneGrammar.g:195:1: truncated : TERM_TRUNCATED ;
    public final StandardLuceneGrammarParser.truncated_return truncated() throws RecognitionException {
        StandardLuceneGrammarParser.truncated_return retval = new StandardLuceneGrammarParser.truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_TRUNCATED66=null;

        Object TERM_TRUNCATED66_tree=null;

        try {
            // StandardLuceneGrammar.g:196:2: ( TERM_TRUNCATED )
            // StandardLuceneGrammar.g:197:2: TERM_TRUNCATED
            {
            root_0 = (Object)adaptor.nil();


            TERM_TRUNCATED66=(Token)match(input,TERM_TRUNCATED,FOLLOW_TERM_TRUNCATED_in_truncated1361); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            TERM_TRUNCATED66_tree = 
            (Object)adaptor.create(TERM_TRUNCATED66)
            ;
            adaptor.addChild(root_0, TERM_TRUNCATED66_tree);
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
    // StandardLuceneGrammar.g:201:1: quoted_truncated : PHRASE_ANYTHING ;
    public final StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated() throws RecognitionException {
        StandardLuceneGrammarParser.quoted_truncated_return retval = new StandardLuceneGrammarParser.quoted_truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE_ANYTHING67=null;

        Object PHRASE_ANYTHING67_tree=null;

        try {
            // StandardLuceneGrammar.g:202:2: ( PHRASE_ANYTHING )
            // StandardLuceneGrammar.g:203:2: PHRASE_ANYTHING
            {
            root_0 = (Object)adaptor.nil();


            PHRASE_ANYTHING67=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1376); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE_ANYTHING67_tree = 
            (Object)adaptor.create(PHRASE_ANYTHING67)
            ;
            adaptor.addChild(root_0, PHRASE_ANYTHING67_tree);
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
    // StandardLuceneGrammar.g:206:1: quoted : PHRASE ;
    public final StandardLuceneGrammarParser.quoted_return quoted() throws RecognitionException {
        StandardLuceneGrammarParser.quoted_return retval = new StandardLuceneGrammarParser.quoted_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE68=null;

        Object PHRASE68_tree=null;

        try {
            // StandardLuceneGrammar.g:206:8: ( PHRASE )
            // StandardLuceneGrammar.g:207:2: PHRASE
            {
            root_0 = (Object)adaptor.nil();


            PHRASE68=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_quoted1388); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE68_tree = 
            (Object)adaptor.create(PHRASE68)
            ;
            adaptor.addChild(root_0, PHRASE68_tree);
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
    // StandardLuceneGrammar.g:213:1: operator : ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] ) ;
    public final StandardLuceneGrammarParser.operator_return operator() throws RecognitionException {
        StandardLuceneGrammarParser.operator_return retval = new StandardLuceneGrammarParser.operator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND69=null;
        Token OR70=null;
        Token NOT71=null;
        Token NEAR72=null;

        Object AND69_tree=null;
        Object OR70_tree=null;
        Object NOT71_tree=null;
        Object NEAR72_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");

        try {
            // StandardLuceneGrammar.g:213:9: ( ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] ) )
            // StandardLuceneGrammar.g:213:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] )
            {
            // StandardLuceneGrammar.g:213:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] )
            int alt35=4;
            switch ( input.LA(1) ) {
            case AND:
                {
                alt35=1;
                }
                break;
            case OR:
                {
                alt35=2;
                }
                break;
            case NOT:
                {
                alt35=3;
                }
                break;
            case NEAR:
                {
                alt35=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;

            }

            switch (alt35) {
                case 1 :
                    // StandardLuceneGrammar.g:214:2: AND
                    {
                    AND69=(Token)match(input,AND,FOLLOW_AND_in_operator1404); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AND.add(AND69);


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
                    // 214:6: -> OPERATOR[\"AND\"]
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
                    // StandardLuceneGrammar.g:215:4: OR
                    {
                    OR70=(Token)match(input,OR,FOLLOW_OR_in_operator1414); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_OR.add(OR70);


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
                    // 215:7: -> OPERATOR[\"OR\"]
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
                    // StandardLuceneGrammar.g:216:4: NOT
                    {
                    NOT71=(Token)match(input,NOT,FOLLOW_NOT_in_operator1424); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NOT.add(NOT71);


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
                    // 216:8: -> OPERATOR[\"NOT\"]
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
                    // StandardLuceneGrammar.g:217:4: NEAR
                    {
                    NEAR72=(Token)match(input,NEAR,FOLLOW_NEAR_in_operator1434); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEAR.add(NEAR72);


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
                    // 217:9: -> OPERATOR[\"NEAR\"]
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
    // StandardLuceneGrammar.g:220:1: modifier : ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] );
    public final StandardLuceneGrammarParser.modifier_return modifier() throws RecognitionException {
        StandardLuceneGrammarParser.modifier_return retval = new StandardLuceneGrammarParser.modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PLUS73=null;
        Token MINUS74=null;

        Object PLUS73_tree=null;
        Object MINUS74_tree=null;
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
        RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");

        try {
            // StandardLuceneGrammar.g:220:9: ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==PLUS) ) {
                alt36=1;
            }
            else if ( (LA36_0==MINUS) ) {
                alt36=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;

            }
            switch (alt36) {
                case 1 :
                    // StandardLuceneGrammar.g:221:2: PLUS
                    {
                    PLUS73=(Token)match(input,PLUS,FOLLOW_PLUS_in_modifier1451); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PLUS.add(PLUS73);


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
                    // 221:7: -> PLUS[\"+\"]
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
                    // StandardLuceneGrammar.g:222:4: MINUS
                    {
                    MINUS74=(Token)match(input,MINUS,FOLLOW_MINUS_in_modifier1461); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MINUS.add(MINUS74);


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
                    // 222:10: -> MINUS[\"-\"]
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
    // StandardLuceneGrammar.g:234:1: term_modifier : ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) );
    public final StandardLuceneGrammarParser.term_modifier_return term_modifier() throws RecognitionException {
        StandardLuceneGrammarParser.term_modifier_return retval = new StandardLuceneGrammarParser.term_modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE75=null;
        Token CARAT76=null;
        Token CARAT77=null;
        Token TILDE78=null;

        Object TILDE75_tree=null;
        Object CARAT76_tree=null;
        Object CARAT77_tree=null;
        Object TILDE78_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

        try {
            // StandardLuceneGrammar.g:234:15: ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==TILDE) ) {
                alt39=1;
            }
            else if ( (LA39_0==CARAT) ) {
                alt39=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;

            }
            switch (alt39) {
                case 1 :
                    // StandardLuceneGrammar.g:235:2: TILDE ( CARAT )?
                    {
                    TILDE75=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1479); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TILDE.add(TILDE75);


                    // StandardLuceneGrammar.g:235:8: ( CARAT )?
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==CARAT) ) {
                        alt37=1;
                    }
                    switch (alt37) {
                        case 1 :
                            // StandardLuceneGrammar.g:235:8: CARAT
                            {
                            CARAT76=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1481); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CARAT.add(CARAT76);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: TILDE, CARAT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 235:15: -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE )
                    {
                        // StandardLuceneGrammar.g:235:18: ^( BOOST ( CARAT )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_1);

                        // StandardLuceneGrammar.g:235:26: ( CARAT )?
                        if ( stream_CARAT.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_CARAT.nextNode()
                            );

                        }
                        stream_CARAT.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                        // StandardLuceneGrammar.g:235:34: ^( FUZZY TILDE )
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

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:236:4: CARAT ( TILDE )?
                    {
                    CARAT77=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1503); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CARAT.add(CARAT77);


                    // StandardLuceneGrammar.g:236:10: ( TILDE )?
                    int alt38=2;
                    int LA38_0 = input.LA(1);

                    if ( (LA38_0==TILDE) ) {
                        alt38=1;
                    }
                    switch (alt38) {
                        case 1 :
                            // StandardLuceneGrammar.g:236:10: TILDE
                            {
                            TILDE78=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1505); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TILDE.add(TILDE78);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: TILDE, CARAT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 236:17: -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? )
                    {
                        // StandardLuceneGrammar.g:236:20: ^( BOOST CARAT )
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

                        // StandardLuceneGrammar.g:236:35: ^( FUZZY ( TILDE )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FUZZY, "FUZZY")
                        , root_1);

                        // StandardLuceneGrammar.g:236:43: ( TILDE )?
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
    // StandardLuceneGrammar.g:256:1: boost : ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? ;
    public final StandardLuceneGrammarParser.boost_return boost() throws RecognitionException {
        StandardLuceneGrammarParser.boost_return retval = new StandardLuceneGrammarParser.boost_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token CARAT79=null;
        Token NUMBER80=null;

        Object CARAT79_tree=null;
        Object NUMBER80_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // StandardLuceneGrammar.g:256:7: ( ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? )
            // StandardLuceneGrammar.g:257:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )?
            {
            // StandardLuceneGrammar.g:257:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) )
            // StandardLuceneGrammar.g:257:3: CARAT
            {
            CARAT79=(Token)match(input,CARAT,FOLLOW_CARAT_in_boost1537); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CARAT.add(CARAT79);


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
            // 257:9: -> ^( BOOST NUMBER[\"DEF\"] )
            {
                // StandardLuceneGrammar.g:257:12: ^( BOOST NUMBER[\"DEF\"] )
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


            // StandardLuceneGrammar.g:258:2: ( NUMBER -> ^( BOOST NUMBER ) )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==NUMBER) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // StandardLuceneGrammar.g:258:3: NUMBER
                    {
                    NUMBER80=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_boost1552); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER80);


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
                    // 258:10: -> ^( BOOST NUMBER )
                    {
                        // StandardLuceneGrammar.g:258:13: ^( BOOST NUMBER )
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
    // StandardLuceneGrammar.g:261:1: fuzzy : ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? ;
    public final StandardLuceneGrammarParser.fuzzy_return fuzzy() throws RecognitionException {
        StandardLuceneGrammarParser.fuzzy_return retval = new StandardLuceneGrammarParser.fuzzy_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE81=null;
        Token NUMBER82=null;

        Object TILDE81_tree=null;
        Object NUMBER82_tree=null;
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // StandardLuceneGrammar.g:261:7: ( ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? )
            // StandardLuceneGrammar.g:262:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )?
            {
            // StandardLuceneGrammar.g:262:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) )
            // StandardLuceneGrammar.g:262:3: TILDE
            {
            TILDE81=(Token)match(input,TILDE,FOLLOW_TILDE_in_fuzzy1575); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TILDE.add(TILDE81);


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
            // 262:9: -> ^( FUZZY NUMBER[\"DEF\"] )
            {
                // StandardLuceneGrammar.g:262:12: ^( FUZZY NUMBER[\"DEF\"] )
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


            // StandardLuceneGrammar.g:263:2: ( NUMBER -> ^( FUZZY NUMBER ) )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==NUMBER) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // StandardLuceneGrammar.g:263:3: NUMBER
                    {
                    NUMBER82=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_fuzzy1590); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER82);


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
                    // 263:10: -> ^( FUZZY NUMBER )
                    {
                        // StandardLuceneGrammar.g:263:13: ^( FUZZY NUMBER )
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
    // StandardLuceneGrammar.g:266:1: not : ( ( AND NOT )=> AND NOT | NOT );
    public final StandardLuceneGrammarParser.not_return not() throws RecognitionException {
        StandardLuceneGrammarParser.not_return retval = new StandardLuceneGrammarParser.not_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND83=null;
        Token NOT84=null;
        Token NOT85=null;

        Object AND83_tree=null;
        Object NOT84_tree=null;
        Object NOT85_tree=null;

        try {
            // StandardLuceneGrammar.g:266:5: ( ( AND NOT )=> AND NOT | NOT )
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==AND) && (synpred4_StandardLuceneGrammar())) {
                alt42=1;
            }
            else if ( (LA42_0==NOT) ) {
                alt42=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;

            }
            switch (alt42) {
                case 1 :
                    // StandardLuceneGrammar.g:267:2: ( AND NOT )=> AND NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    AND83=(Token)match(input,AND,FOLLOW_AND_in_not1620); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    AND83_tree = 
                    (Object)adaptor.create(AND83)
                    ;
                    adaptor.addChild(root_0, AND83_tree);
                    }

                    NOT84=(Token)match(input,NOT,FOLLOW_NOT_in_not1622); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT84_tree = 
                    (Object)adaptor.create(NOT84)
                    ;
                    adaptor.addChild(root_0, NOT84_tree);
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:268:4: NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    NOT85=(Token)match(input,NOT,FOLLOW_NOT_in_not1627); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT85_tree = 
                    (Object)adaptor.create(NOT85)
                    ;
                    adaptor.addChild(root_0, NOT85_tree);
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
    // StandardLuceneGrammar.g:271:1: and : AND ;
    public final StandardLuceneGrammarParser.and_return and() throws RecognitionException {
        StandardLuceneGrammarParser.and_return retval = new StandardLuceneGrammarParser.and_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND86=null;

        Object AND86_tree=null;

        try {
            // StandardLuceneGrammar.g:271:6: ( AND )
            // StandardLuceneGrammar.g:272:2: AND
            {
            root_0 = (Object)adaptor.nil();


            AND86=(Token)match(input,AND,FOLLOW_AND_in_and1641); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            AND86_tree = 
            (Object)adaptor.create(AND86)
            ;
            adaptor.addChild(root_0, AND86_tree);
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
    // StandardLuceneGrammar.g:275:1: or : OR ;
    public final StandardLuceneGrammarParser.or_return or() throws RecognitionException {
        StandardLuceneGrammarParser.or_return retval = new StandardLuceneGrammarParser.or_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OR87=null;

        Object OR87_tree=null;

        try {
            // StandardLuceneGrammar.g:275:5: ( OR )
            // StandardLuceneGrammar.g:276:2: OR
            {
            root_0 = (Object)adaptor.nil();


            OR87=(Token)match(input,OR,FOLLOW_OR_in_or1655); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            OR87_tree = 
            (Object)adaptor.create(OR87)
            ;
            adaptor.addChild(root_0, OR87_tree);
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
    // StandardLuceneGrammar.g:279:1: near : ( NEAR -> ^( OPERATOR[\"NEAR\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )? ;
    public final StandardLuceneGrammarParser.near_return near() throws RecognitionException {
        StandardLuceneGrammarParser.near_return retval = new StandardLuceneGrammarParser.near_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token b=null;
        Token NEAR88=null;
        Token char_literal89=null;

        Object b_tree=null;
        Object NEAR88_tree=null;
        Object char_literal89_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // StandardLuceneGrammar.g:279:6: ( ( NEAR -> ^( OPERATOR[\"NEAR\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )? )
            // StandardLuceneGrammar.g:280:2: ( NEAR -> ^( OPERATOR[\"NEAR\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )?
            {
            // StandardLuceneGrammar.g:280:2: ( NEAR -> ^( OPERATOR[\"NEAR\"] ) )
            // StandardLuceneGrammar.g:280:3: NEAR
            {
            NEAR88=(Token)match(input,NEAR,FOLLOW_NEAR_in_near1670); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NEAR.add(NEAR88);


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
            // 280:8: -> ^( OPERATOR[\"NEAR\"] )
            {
                // StandardLuceneGrammar.g:280:11: ^( OPERATOR[\"NEAR\"] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OPERATOR, "NEAR")
                , root_1);

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:281:2: ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==53) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // StandardLuceneGrammar.g:281:3: '/' b= NUMBER
                    {
                    char_literal89=(Token)match(input,53,FOLLOW_53_in_near1683); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_53.add(char_literal89);


                    b=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_near1687); if (state.failed) return retval; 
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
                    // 281:16: -> ^( OPERATOR[\"NEAR:\" + $b.getText()] )
                    {
                        // StandardLuceneGrammar.g:281:19: ^( OPERATOR[\"NEAR:\" + $b.getText()] )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(OPERATOR, "NEAR:" + b.getText())
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
    // StandardLuceneGrammar.g:284:1: date : DATE_TOKEN ;
    public final StandardLuceneGrammarParser.date_return date() throws RecognitionException {
        StandardLuceneGrammarParser.date_return retval = new StandardLuceneGrammarParser.date_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token DATE_TOKEN90=null;

        Object DATE_TOKEN90_tree=null;

        try {
            // StandardLuceneGrammar.g:284:6: ( DATE_TOKEN )
            // StandardLuceneGrammar.g:286:2: DATE_TOKEN
            {
            root_0 = (Object)adaptor.nil();


            DATE_TOKEN90=(Token)match(input,DATE_TOKEN,FOLLOW_DATE_TOKEN_in_date1711); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            DATE_TOKEN90_tree = 
            (Object)adaptor.create(DATE_TOKEN90)
            ;
            adaptor.addChild(root_0, DATE_TOKEN90_tree);
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
        // StandardLuceneGrammar.g:57:2: ( modifier LPAREN ( clauseOr )+ RPAREN )
        // StandardLuceneGrammar.g:57:3: modifier LPAREN ( clauseOr )+ RPAREN
        {
        pushFollow(FOLLOW_modifier_in_synpred1_StandardLuceneGrammar338);
        modifier();

        state._fsp--;
        if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar340); if (state.failed) return ;

        // StandardLuceneGrammar.g:57:19: ( clauseOr )+
        int cnt44=0;
        loop44:
        do {
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( ((LA44_0 >= LBRACK && LA44_0 <= MINUS)||LA44_0==NUMBER||(LA44_0 >= PHRASE && LA44_0 <= PLUS)||LA44_0==QMARK||LA44_0==STAR||LA44_0==TERM_NORMAL||LA44_0==TERM_TRUNCATED) ) {
                alt44=1;
            }


            switch (alt44) {
        	case 1 :
        	    // StandardLuceneGrammar.g:57:19: clauseOr
        	    {
        	    pushFollow(FOLLOW_clauseOr_in_synpred1_StandardLuceneGrammar342);
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


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar345); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred1_StandardLuceneGrammar

    // $ANTLR start synpred2_StandardLuceneGrammar
    public final void synpred2_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:59:4: ( LPAREN ( clauseOr )+ RPAREN term_modifier )
        // StandardLuceneGrammar.g:59:5: LPAREN ( clauseOr )+ RPAREN term_modifier
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar399); if (state.failed) return ;

        // StandardLuceneGrammar.g:59:12: ( clauseOr )+
        int cnt45=0;
        loop45:
        do {
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( ((LA45_0 >= LBRACK && LA45_0 <= MINUS)||LA45_0==NUMBER||(LA45_0 >= PHRASE && LA45_0 <= PLUS)||LA45_0==QMARK||LA45_0==STAR||LA45_0==TERM_NORMAL||LA45_0==TERM_TRUNCATED) ) {
                alt45=1;
            }


            switch (alt45) {
        	case 1 :
        	    // StandardLuceneGrammar.g:59:12: clauseOr
        	    {
        	    pushFollow(FOLLOW_clauseOr_in_synpred2_StandardLuceneGrammar401);
        	    clauseOr();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt45 >= 1 ) break loop45;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(45, input);
                    throw eee;
            }
            cnt45++;
        } while (true);


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar404); if (state.failed) return ;

        pushFollow(FOLLOW_term_modifier_in_synpred2_StandardLuceneGrammar406);
        term_modifier();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_StandardLuceneGrammar

    // $ANTLR start synpred3_StandardLuceneGrammar
    public final void synpred3_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:61:4: ( LPAREN )
        // StandardLuceneGrammar.g:61:5: LPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar459); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred3_StandardLuceneGrammar

    // $ANTLR start synpred4_StandardLuceneGrammar
    public final void synpred4_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:267:2: ( AND NOT )
        // StandardLuceneGrammar.g:267:3: AND NOT
        {
        match(input,AND,FOLLOW_AND_in_synpred4_StandardLuceneGrammar1614); if (state.failed) return ;

        match(input,NOT,FOLLOW_NOT_in_synpred4_StandardLuceneGrammar1616); if (state.failed) return ;

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


 

    public static final BitSet FOLLOW_clauseOr_in_mainQ133 = new BitSet(new long[]{0x0000A801391E0002L});
    public static final BitSet FOLLOW_clauseAnd_in_clauseOr165 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_or_in_clauseOr174 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseAnd_in_clauseOr178 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_clauseNot_in_clauseAnd207 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_and_in_clauseAnd217 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseNot_in_clauseAnd221 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_clauseNear_in_clauseNot252 = new BitSet(new long[]{0x0000000000800022L});
    public static final BitSet FOLLOW_not_in_clauseNot261 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseNear_in_clauseNot265 = new BitSet(new long[]{0x0000000000800022L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseNear296 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_near_in_clauseNear305 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseNear309 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_modifier_in_clauseBasic350 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic353 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic355 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic358 = new BitSet(new long[]{0x0001000000000102L});
    public static final BitSet FOLLOW_term_modifier_in_clauseBasic360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_clauseBasic410 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic413 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic415 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic418 = new BitSet(new long[]{0x0001000000000102L});
    public static final BitSet FOLLOW_term_modifier_in_clauseBasic420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic464 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic466 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic469 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_clauseBasic481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom502 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_field_in_atom505 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_multi_value_in_atom507 = new BitSet(new long[]{0x0001000000000102L});
    public static final BitSet FOLLOW_term_modifier_in_atom509 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom545 = new BitSet(new long[]{0x0000A00119060000L});
    public static final BitSet FOLLOW_field_in_atom548 = new BitSet(new long[]{0x0000A00119060000L});
    public static final BitSet FOLLOW_value_in_atom551 = new BitSet(new long[]{0x0001000000000102L});
    public static final BitSet FOLLOW_term_modifier_in_atom553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom587 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_STAR_in_atom591 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_COLON_in_atom593 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_STAR_in_atom597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_NORMAL_in_field634 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_COLON_in_field636 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_in_in_value655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_ex_in_value668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_value682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_value696 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_value710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_value723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QMARK_in_value736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACK_in_range_term_in768 = new BitSet(new long[]{0x0000A80019000800L});
    public static final BitSet FOLLOW_range_value_in_range_term_in780 = new BitSet(new long[]{0x0004A88019000800L});
    public static final BitSet FOLLOW_TO_in_range_term_in803 = new BitSet(new long[]{0x0000A80019000800L});
    public static final BitSet FOLLOW_range_value_in_range_term_in808 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_RBRACK_in_range_term_in829 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LCURLY_in_range_term_ex849 = new BitSet(new long[]{0x0000A80019000800L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex862 = new BitSet(new long[]{0x0004A90019000800L});
    public static final BitSet FOLLOW_TO_in_range_term_ex885 = new BitSet(new long[]{0x0000A80019000800L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex890 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_RCURLY_in_range_term_ex911 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_range_value925 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_range_value938 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_range_value951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_in_range_value964 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_range_value977 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_range_value991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_multi_value1012 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_multiClause_in_multi_value1014 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RPAREN_in_multi_value1016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clauseOr_in_multiClause1043 = new BitSet(new long[]{0x0000A801391E0002L});
    public static final BitSet FOLLOW_multiOr_in_multiDefault1087 = new BitSet(new long[]{0x0000A00139160002L});
    public static final BitSet FOLLOW_multiAnd_in_multiOr1115 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_or_in_multiOr1125 = new BitSet(new long[]{0x0000A00139160000L});
    public static final BitSet FOLLOW_multiAnd_in_multiOr1129 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_multiNot_in_multiAnd1160 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_and_in_multiAnd1170 = new BitSet(new long[]{0x0000A00139160000L});
    public static final BitSet FOLLOW_multiNot_in_multiAnd1174 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_multiNear_in_multiNot1205 = new BitSet(new long[]{0x0000000000800022L});
    public static final BitSet FOLLOW_not_in_multiNot1215 = new BitSet(new long[]{0x0000A00139160000L});
    public static final BitSet FOLLOW_multiNear_in_multiNot1219 = new BitSet(new long[]{0x0000000000800022L});
    public static final BitSet FOLLOW_multiBasic_in_multiNear1249 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_near_in_multiNear1259 = new BitSet(new long[]{0x0000A00139160000L});
    public static final BitSet FOLLOW_multiBasic_in_multiNear1263 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_mterm_in_multiBasic1289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_mterm1305 = new BitSet(new long[]{0x0000A00119060000L});
    public static final BitSet FOLLOW_value_in_mterm1308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_TRUNCATED_in_truncated1361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_in_quoted1388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_operator1404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_operator1414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_operator1424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_operator1434 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_modifier1451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_modifier1461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1479 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1503 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_boost1537 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_NUMBER_in_boost1552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_fuzzy1575 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_NUMBER_in_fuzzy1590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_not1620 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_NOT_in_not1622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_not1627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_and1641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_or1655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_near1670 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_53_in_near1683 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_NUMBER_in_near1687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DATE_TOKEN_in_date1711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_synpred1_StandardLuceneGrammar338 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar340 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_synpred1_StandardLuceneGrammar342 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar345 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar399 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_synpred2_StandardLuceneGrammar401 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar404 = new BitSet(new long[]{0x0001000000000100L});
    public static final BitSet FOLLOW_term_modifier_in_synpred2_StandardLuceneGrammar406 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_synpred4_StandardLuceneGrammar1614 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_NOT_in_synpred4_StandardLuceneGrammar1616 = new BitSet(new long[]{0x0000000000000002L});

}