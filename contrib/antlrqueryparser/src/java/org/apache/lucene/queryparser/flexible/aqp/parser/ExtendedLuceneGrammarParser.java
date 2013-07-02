// $ANTLR 3.4 /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g 2013-06-27 18:46:02

  package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class ExtendedLuceneGrammarParser extends UnforgivingParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AMPER", "AND", "ATOM", "BOOST", "CARAT", "CLAUSE", "COLON", "DATE_TOKEN", "DQUOTE", "ESC_CHAR", "FIELD", "FUZZY", "INT", "LBRACK", "LCURLY", "LPAREN", "MINUS", "MODIFIER", "NEAR", "NOT", "NUMBER", "OPERATOR", "OR", "PHRASE", "PHRASE_ANYTHING", "PLUS", "QANYTHING", "QDATE", "QMARK", "QNORMAL", "QPHRASE", "QPHRASETRUNC", "QRANGEEX", "QRANGEIN", "QTRUNCATED", "RBRACK", "RCURLY", "RPAREN", "SQUOTE", "STAR", "TERM_CHAR", "TERM_NORMAL", "TERM_START_CHAR", "TERM_TRUNCATED", "TILDE", "TMODIFIER", "TO", "VBAR", "WS"
    };

    public static final int EOF=-1;
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
    public UnforgivingParser[] getDelegates() {
        return new UnforgivingParser[] {};
    }

    // delegators


    public ExtendedLuceneGrammarParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public ExtendedLuceneGrammarParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return ExtendedLuceneGrammarParser.tokenNames; }
    public String getGrammarFileName() { return "/dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g"; }


    public static class mainQ_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mainQ"
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:103:1: mainQ : ( clauseOr )+ EOF -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
    public final ExtendedLuceneGrammarParser.mainQ_return mainQ() throws RecognitionException {
        ExtendedLuceneGrammarParser.mainQ_return retval = new ExtendedLuceneGrammarParser.mainQ_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token EOF2=null;
        ExtendedLuceneGrammarParser.clauseOr_return clauseOr1 =null;


        Object EOF2_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:103:7: ( ( clauseOr )+ EOF -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:104:2: ( clauseOr )+ EOF
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:104:2: ( clauseOr )+
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
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:104:2: clauseOr
            	    {
            	    pushFollow(FOLLOW_clauseOr_in_mainQ212);
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


            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_mainQ215); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EOF.add(EOF2);


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
            // 104:16: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
            {
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:104:19: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:107:1: clauseOr : (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* ;
    public final ExtendedLuceneGrammarParser.clauseOr_return clauseOr() throws RecognitionException {
        ExtendedLuceneGrammarParser.clauseOr_return retval = new ExtendedLuceneGrammarParser.clauseOr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.clauseAnd_return first =null;

        ExtendedLuceneGrammarParser.clauseAnd_return others =null;

        ExtendedLuceneGrammarParser.or_return or3 =null;


        RewriteRuleSubtreeStream stream_clauseAnd=new RewriteRuleSubtreeStream(adaptor,"rule clauseAnd");
        RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:108:3: ( (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:108:5: (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:108:5: (first= clauseAnd -> $first)
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:108:6: first= clauseAnd
            {
            pushFollow(FOLLOW_clauseAnd_in_clauseOr242);
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
            // 108:22: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:108:33: ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==OR) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:108:34: or others= clauseAnd
            	    {
            	    pushFollow(FOLLOW_or_in_clauseOr251);
            	    or3=or();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_or.add(or3.getTree());

            	    pushFollow(FOLLOW_clauseAnd_in_clauseOr255);
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
            	    // 108:54: -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:108:57: ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:111:1: clauseAnd : (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* ;
    public final ExtendedLuceneGrammarParser.clauseAnd_return clauseAnd() throws RecognitionException {
        ExtendedLuceneGrammarParser.clauseAnd_return retval = new ExtendedLuceneGrammarParser.clauseAnd_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.clauseNot_return first =null;

        ExtendedLuceneGrammarParser.clauseNot_return others =null;

        ExtendedLuceneGrammarParser.and_return and4 =null;


        RewriteRuleSubtreeStream stream_clauseNot=new RewriteRuleSubtreeStream(adaptor,"rule clauseNot");
        RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:112:3: ( (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:112:5: (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:112:5: (first= clauseNot -> $first)
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:112:6: first= clauseNot
            {
            pushFollow(FOLLOW_clauseNot_in_clauseAnd284);
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
            // 112:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:112:34: ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==AND) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:112:35: and others= clauseNot
            	    {
            	    pushFollow(FOLLOW_and_in_clauseAnd294);
            	    and4=and();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_and.add(and4.getTree());

            	    pushFollow(FOLLOW_clauseNot_in_clauseAnd298);
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
            	    // 112:56: -> ^( OPERATOR[\"AND\"] ( clauseNot )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:112:59: ^( OPERATOR[\"AND\"] ( clauseNot )+ )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:115:1: clauseNot : (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* ;
    public final ExtendedLuceneGrammarParser.clauseNot_return clauseNot() throws RecognitionException {
        ExtendedLuceneGrammarParser.clauseNot_return retval = new ExtendedLuceneGrammarParser.clauseNot_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.clauseNear_return first =null;

        ExtendedLuceneGrammarParser.clauseNear_return others =null;

        ExtendedLuceneGrammarParser.not_return not5 =null;


        RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
        RewriteRuleSubtreeStream stream_clauseNear=new RewriteRuleSubtreeStream(adaptor,"rule clauseNear");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:116:3: ( (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:116:5: (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:116:5: (first= clauseNear -> $first)
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:116:6: first= clauseNear
            {
            pushFollow(FOLLOW_clauseNear_in_clauseNot329);
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
            // 116:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:116:34: ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
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
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:116:35: not others= clauseNear
            	    {
            	    pushFollow(FOLLOW_not_in_clauseNot338);
            	    not5=not();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_not.add(not5.getTree());

            	    pushFollow(FOLLOW_clauseNear_in_clauseNot342);
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
            	    // 116:57: -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:116:60: ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:119:1: clauseNear : (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* ;
    public final ExtendedLuceneGrammarParser.clauseNear_return clauseNear() throws RecognitionException {
        ExtendedLuceneGrammarParser.clauseNear_return retval = new ExtendedLuceneGrammarParser.clauseNear_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.clauseBasic_return first =null;

        ExtendedLuceneGrammarParser.clauseBasic_return others =null;

        ExtendedLuceneGrammarParser.near_return near6 =null;


        RewriteRuleSubtreeStream stream_clauseBasic=new RewriteRuleSubtreeStream(adaptor,"rule clauseBasic");
        RewriteRuleSubtreeStream stream_near=new RewriteRuleSubtreeStream(adaptor,"rule near");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:120:3: ( (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:120:5: (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:120:5: (first= clauseBasic -> $first)
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:120:6: first= clauseBasic
            {
            pushFollow(FOLLOW_clauseBasic_in_clauseNear373);
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
            // 120:24: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:120:35: ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==NEAR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:120:36: near others= clauseBasic
            	    {
            	    pushFollow(FOLLOW_near_in_clauseNear382);
            	    near6=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near6.getTree());

            	    pushFollow(FOLLOW_clauseBasic_in_clauseNear386);
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
            	    // 120:60: -> ^( near ( clauseBasic )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:120:63: ^( near ( clauseBasic )+ )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:123:1: clauseBasic : ( ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom );
    public final ExtendedLuceneGrammarParser.clauseBasic_return clauseBasic() throws RecognitionException {
        ExtendedLuceneGrammarParser.clauseBasic_return retval = new ExtendedLuceneGrammarParser.clauseBasic_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN8=null;
        Token RPAREN10=null;
        Token LPAREN13=null;
        Token RPAREN15=null;
        Token LPAREN17=null;
        Token RPAREN19=null;
        ExtendedLuceneGrammarParser.modifier_return modifier7 =null;

        ExtendedLuceneGrammarParser.clauseOr_return clauseOr9 =null;

        ExtendedLuceneGrammarParser.term_modifier_return term_modifier11 =null;

        ExtendedLuceneGrammarParser.modifier_return modifier12 =null;

        ExtendedLuceneGrammarParser.clauseOr_return clauseOr14 =null;

        ExtendedLuceneGrammarParser.term_modifier_return term_modifier16 =null;

        ExtendedLuceneGrammarParser.clauseOr_return clauseOr18 =null;

        ExtendedLuceneGrammarParser.atom_return atom20 =null;


        Object LPAREN8_tree=null;
        Object RPAREN10_tree=null;
        Object LPAREN13_tree=null;
        Object RPAREN15_tree=null;
        Object LPAREN17_tree=null;
        Object RPAREN19_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:124:2: ( ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom )
            int alt13=4;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                int LA13_1 = input.LA(2);

                if ( (synpred1_ExtendedLuceneGrammar()) ) {
                    alt13=1;
                }
                else if ( (synpred2_ExtendedLuceneGrammar()) ) {
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

                if ( (synpred1_ExtendedLuceneGrammar()) ) {
                    alt13=1;
                }
                else if ( (synpred2_ExtendedLuceneGrammar()) ) {
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

                if ( (synpred1_ExtendedLuceneGrammar()) ) {
                    alt13=1;
                }
                else if ( (synpred2_ExtendedLuceneGrammar()) ) {
                    alt13=2;
                }
                else if ( (synpred3_ExtendedLuceneGrammar()) ) {
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:2: ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:40: ( modifier )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==MINUS||LA6_0==PLUS) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:40: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_clauseBasic427);
                            modifier7=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier7.getTree());

                            }
                            break;

                    }


                    LPAREN8=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic430); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN8);


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:57: ( clauseOr )+
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
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:57: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic432);
                    	    clauseOr9=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr9.getTree());

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


                    RPAREN10=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic435); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN10);


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:74: ( term_modifier )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==CARAT||LA8_0==TILDE) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:74: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_clauseBasic437);
                            term_modifier11=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier11.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: modifier, clauseOr, term_modifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 126:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:126:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:126:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:126:26: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:126:36: ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:126:48: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:126:63: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:4: ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:46: ( modifier )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==MINUS||LA9_0==PLUS) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:46: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_clauseBasic487);
                            modifier12=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier12.getTree());

                            }
                            break;

                    }


                    LPAREN13=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic490); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN13);


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:63: ( clauseOr )+
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
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:63: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic492);
                    	    clauseOr14=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr14.getTree());

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


                    RPAREN15=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic495); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN15);


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:80: ( term_modifier )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==CARAT||LA11_0==TILDE) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:80: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_clauseBasic497);
                            term_modifier16=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier16.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: term_modifier, modifier, clauseOr
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 128:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:128:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:128:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:128:26: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:128:36: ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:128:48: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:128:63: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:129:4: ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN
                    {
                    LPAREN17=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic542); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN17);


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:129:24: ( clauseOr )+
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
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:129:24: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic544);
                    	    clauseOr18=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr18.getTree());

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


                    RPAREN19=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic547); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN19);


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
                    // 130:3: -> ( clauseOr )+
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:131:4: atom
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_atom_in_clauseBasic559);
                    atom20=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom20.getTree());

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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:135:1: atom : ( ( modifier )? field multi_value ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) ) );
    public final ExtendedLuceneGrammarParser.atom_return atom() throws RecognitionException {
        ExtendedLuceneGrammarParser.atom_return retval = new ExtendedLuceneGrammarParser.atom_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.modifier_return modifier21 =null;

        ExtendedLuceneGrammarParser.field_return field22 =null;

        ExtendedLuceneGrammarParser.multi_value_return multi_value23 =null;

        ExtendedLuceneGrammarParser.term_modifier_return term_modifier24 =null;

        ExtendedLuceneGrammarParser.modifier_return modifier25 =null;

        ExtendedLuceneGrammarParser.field_return field26 =null;

        ExtendedLuceneGrammarParser.value_return value27 =null;

        ExtendedLuceneGrammarParser.term_modifier_return term_modifier28 =null;


        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_multi_value=new RewriteRuleSubtreeStream(adaptor,"rule multi_value");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:136:2: ( ( modifier )? field multi_value ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) ) )
            int alt19=2;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==TERM_NORMAL) ) {
                    int LA19_3 = input.LA(3);

                    if ( (LA19_3==COLON) ) {
                        int LA19_5 = input.LA(4);

                        if ( (LA19_5==LPAREN) ) {
                            alt19=1;
                        }
                        else if ( ((LA19_5 >= LBRACK && LA19_5 <= LCURLY)||LA19_5==NUMBER||(LA19_5 >= PHRASE && LA19_5 <= PHRASE_ANYTHING)||LA19_5==QMARK||LA19_5==STAR||LA19_5==TERM_NORMAL||LA19_5==TERM_TRUNCATED) ) {
                            alt19=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 19, 5, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA19_3==EOF||LA19_3==AND||LA19_3==CARAT||(LA19_3 >= LBRACK && LA19_3 <= MINUS)||(LA19_3 >= NEAR && LA19_3 <= NUMBER)||(LA19_3 >= OR && LA19_3 <= PLUS)||LA19_3==QMARK||LA19_3==RPAREN||LA19_3==STAR||LA19_3==TERM_NORMAL||(LA19_3 >= TERM_TRUNCATED && LA19_3 <= TILDE)) ) {
                        alt19=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 3, input);

                        throw nvae;

                    }
                }
                else if ( ((LA19_1 >= LBRACK && LA19_1 <= LCURLY)||LA19_1==NUMBER||(LA19_1 >= PHRASE && LA19_1 <= PHRASE_ANYTHING)||LA19_1==QMARK||LA19_1==STAR||LA19_1==TERM_TRUNCATED) ) {
                    alt19=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;

                }
                }
                break;
            case MINUS:
                {
                int LA19_2 = input.LA(2);

                if ( (LA19_2==TERM_NORMAL) ) {
                    int LA19_3 = input.LA(3);

                    if ( (LA19_3==COLON) ) {
                        int LA19_5 = input.LA(4);

                        if ( (LA19_5==LPAREN) ) {
                            alt19=1;
                        }
                        else if ( ((LA19_5 >= LBRACK && LA19_5 <= LCURLY)||LA19_5==NUMBER||(LA19_5 >= PHRASE && LA19_5 <= PHRASE_ANYTHING)||LA19_5==QMARK||LA19_5==STAR||LA19_5==TERM_NORMAL||LA19_5==TERM_TRUNCATED) ) {
                            alt19=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 19, 5, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA19_3==EOF||LA19_3==AND||LA19_3==CARAT||(LA19_3 >= LBRACK && LA19_3 <= MINUS)||(LA19_3 >= NEAR && LA19_3 <= NUMBER)||(LA19_3 >= OR && LA19_3 <= PLUS)||LA19_3==QMARK||LA19_3==RPAREN||LA19_3==STAR||LA19_3==TERM_NORMAL||(LA19_3 >= TERM_TRUNCATED && LA19_3 <= TILDE)) ) {
                        alt19=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 3, input);

                        throw nvae;

                    }
                }
                else if ( ((LA19_2 >= LBRACK && LA19_2 <= LCURLY)||LA19_2==NUMBER||(LA19_2 >= PHRASE && LA19_2 <= PHRASE_ANYTHING)||LA19_2==QMARK||LA19_2==STAR||LA19_2==TERM_TRUNCATED) ) {
                    alt19=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 2, input);

                    throw nvae;

                }
                }
                break;
            case TERM_NORMAL:
                {
                int LA19_3 = input.LA(2);

                if ( (LA19_3==COLON) ) {
                    int LA19_5 = input.LA(3);

                    if ( (LA19_5==LPAREN) ) {
                        alt19=1;
                    }
                    else if ( ((LA19_5 >= LBRACK && LA19_5 <= LCURLY)||LA19_5==NUMBER||(LA19_5 >= PHRASE && LA19_5 <= PHRASE_ANYTHING)||LA19_5==QMARK||LA19_5==STAR||LA19_5==TERM_NORMAL||LA19_5==TERM_TRUNCATED) ) {
                        alt19=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 5, input);

                        throw nvae;

                    }
                }
                else if ( (LA19_3==EOF||LA19_3==AND||LA19_3==CARAT||(LA19_3 >= LBRACK && LA19_3 <= MINUS)||(LA19_3 >= NEAR && LA19_3 <= NUMBER)||(LA19_3 >= OR && LA19_3 <= PLUS)||LA19_3==QMARK||LA19_3==RPAREN||LA19_3==STAR||LA19_3==TERM_NORMAL||(LA19_3 >= TERM_TRUNCATED && LA19_3 <= TILDE)) ) {
                    alt19=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 3, input);

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
            case TERM_TRUNCATED:
                {
                alt19=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }

            switch (alt19) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:137:2: ( modifier )? field multi_value ( term_modifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:137:2: ( modifier )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==MINUS||LA14_0==PLUS) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:137:2: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom580);
                            modifier21=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier21.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_field_in_atom583);
                    field22=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field22.getTree());

                    pushFollow(FOLLOW_multi_value_in_atom585);
                    multi_value23=multi_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_multi_value.add(multi_value23.getTree());

                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:137:30: ( term_modifier )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==CARAT||LA15_0==TILDE) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:137:30: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_atom587);
                            term_modifier24=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier24.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: field, multi_value, modifier, term_modifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 138:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:138:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:138:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:138:26: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:138:36: ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:138:48: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:138:63: ^( FIELD field multi_value )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:139:4: ( modifier )? ( field )? value ( term_modifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:139:4: ( modifier )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==MINUS||LA16_0==PLUS) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:139:4: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom623);
                            modifier25=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier25.getTree());

                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:139:14: ( field )?
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
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:139:14: field
                            {
                            pushFollow(FOLLOW_field_in_atom626);
                            field26=field();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_field.add(field26.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_value_in_atom629);
                    value27=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value27.getTree());

                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:139:27: ( term_modifier )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==CARAT||LA18_0==TILDE) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:139:27: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_atom631);
                            term_modifier28=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier28.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: value, term_modifier, modifier, field
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 140:3: -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:140:6: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:140:17: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:140:27: ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:140:39: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:140:54: ^( FIELD ( field )? value )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:140:62: ( field )?
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:144:1: field : TERM_NORMAL COLON -> TERM_NORMAL ;
    public final ExtendedLuceneGrammarParser.field_return field() throws RecognitionException {
        ExtendedLuceneGrammarParser.field_return retval = new ExtendedLuceneGrammarParser.field_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_NORMAL29=null;
        Token COLON30=null;

        Object TERM_NORMAL29_tree=null;
        Object COLON30_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_TERM_NORMAL=new RewriteRuleTokenStream(adaptor,"token TERM_NORMAL");

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:145:2: ( TERM_NORMAL COLON -> TERM_NORMAL )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:146:2: TERM_NORMAL COLON
            {
            TERM_NORMAL29=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_field678); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL29);


            COLON30=(Token)match(input,COLON,FOLLOW_COLON_in_field680); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(COLON30);


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
            // 146:20: -> TERM_NORMAL
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:149:1: value : ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) | STAR COLON b= STAR -> ^( QANYTHING $b) | STAR -> ^( QTRUNCATED STAR ) );
    public final ExtendedLuceneGrammarParser.value_return value() throws RecognitionException {
        ExtendedLuceneGrammarParser.value_return retval = new ExtendedLuceneGrammarParser.value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token b=null;
        Token QMARK37=null;
        Token STAR38=null;
        Token COLON39=null;
        Token STAR40=null;
        ExtendedLuceneGrammarParser.range_term_in_return range_term_in31 =null;

        ExtendedLuceneGrammarParser.range_term_ex_return range_term_ex32 =null;

        ExtendedLuceneGrammarParser.normal_return normal33 =null;

        ExtendedLuceneGrammarParser.truncated_return truncated34 =null;

        ExtendedLuceneGrammarParser.quoted_return quoted35 =null;

        ExtendedLuceneGrammarParser.quoted_truncated_return quoted_truncated36 =null;


        Object b_tree=null;
        Object QMARK37_tree=null;
        Object STAR38_tree=null;
        Object COLON39_tree=null;
        Object STAR40_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
        RewriteRuleSubtreeStream stream_range_term_ex=new RewriteRuleSubtreeStream(adaptor,"rule range_term_ex");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_range_term_in=new RewriteRuleSubtreeStream(adaptor,"rule range_term_in");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:150:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) | STAR COLON b= STAR -> ^( QANYTHING $b) | STAR -> ^( QTRUNCATED STAR ) )
            int alt20=9;
            switch ( input.LA(1) ) {
            case LBRACK:
                {
                alt20=1;
                }
                break;
            case LCURLY:
                {
                alt20=2;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
                {
                alt20=3;
                }
                break;
            case TERM_TRUNCATED:
                {
                alt20=4;
                }
                break;
            case PHRASE:
                {
                alt20=5;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt20=6;
                }
                break;
            case QMARK:
                {
                alt20=7;
                }
                break;
            case STAR:
                {
                int LA20_8 = input.LA(2);

                if ( (LA20_8==COLON) ) {
                    alt20=8;
                }
                else if ( (LA20_8==EOF||LA20_8==AND||LA20_8==CARAT||(LA20_8 >= LBRACK && LA20_8 <= MINUS)||(LA20_8 >= NEAR && LA20_8 <= NUMBER)||(LA20_8 >= OR && LA20_8 <= PLUS)||LA20_8==QMARK||LA20_8==RPAREN||LA20_8==STAR||LA20_8==TERM_NORMAL||(LA20_8 >= TERM_TRUNCATED && LA20_8 <= TILDE)) ) {
                    alt20=9;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 8, input);

                    throw nvae;

                }
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:151:2: range_term_in
                    {
                    pushFollow(FOLLOW_range_term_in_in_value699);
                    range_term_in31=range_term_in();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_in.add(range_term_in31.getTree());

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
                    // 151:16: -> ^( QRANGEIN range_term_in )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:151:19: ^( QRANGEIN range_term_in )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:152:4: range_term_ex
                    {
                    pushFollow(FOLLOW_range_term_ex_in_value712);
                    range_term_ex32=range_term_ex();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_ex.add(range_term_ex32.getTree());

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
                    // 152:18: -> ^( QRANGEEX range_term_ex )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:152:21: ^( QRANGEEX range_term_ex )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:153:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_value726);
                    normal33=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal33.getTree());

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
                    // 153:11: -> ^( QNORMAL normal )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:153:14: ^( QNORMAL normal )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:154:4: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_value740);
                    truncated34=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated34.getTree());

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
                    // 154:14: -> ^( QTRUNCATED truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:154:17: ^( QTRUNCATED truncated )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:155:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_value754);
                    quoted35=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted35.getTree());

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
                    // 155:11: -> ^( QPHRASE quoted )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:155:14: ^( QPHRASE quoted )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:156:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_value767);
                    quoted_truncated36=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated36.getTree());

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
                    // 156:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:156:24: ^( QPHRASETRUNC quoted_truncated )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:157:4: QMARK
                    {
                    QMARK37=(Token)match(input,QMARK,FOLLOW_QMARK_in_value780); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QMARK.add(QMARK37);


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
                    // 157:10: -> ^( QTRUNCATED QMARK )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:157:13: ^( QTRUNCATED QMARK )
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
                case 8 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:158:4: STAR COLON b= STAR
                    {
                    STAR38=(Token)match(input,STAR,FOLLOW_STAR_in_value793); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR38);


                    COLON39=(Token)match(input,COLON,FOLLOW_COLON_in_value795); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(COLON39);


                    b=(Token)match(input,STAR,FOLLOW_STAR_in_value799); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(b);


                    // AST REWRITE
                    // elements: b
                    // token labels: b
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_b=new RewriteRuleTokenStream(adaptor,"token b",b);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 158:22: -> ^( QANYTHING $b)
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:158:25: ^( QANYTHING $b)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QANYTHING, "QANYTHING")
                        , root_1);

                        adaptor.addChild(root_1, stream_b.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 9 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:159:5: STAR
                    {
                    STAR40=(Token)match(input,STAR,FOLLOW_STAR_in_value814); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR40);


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
                    // 159:10: -> ^( QTRUNCATED STAR )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:159:13: ^( QTRUNCATED STAR )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QTRUNCATED, "QTRUNCATED")
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
    // $ANTLR end "value"


    public static class range_term_in_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "range_term_in"
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:164:1: range_term_in : LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK ;
    public final ExtendedLuceneGrammarParser.range_term_in_return range_term_in() throws RecognitionException {
        ExtendedLuceneGrammarParser.range_term_in_return retval = new ExtendedLuceneGrammarParser.range_term_in_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LBRACK41=null;
        Token TO42=null;
        Token RBRACK43=null;
        ExtendedLuceneGrammarParser.range_value_return a =null;

        ExtendedLuceneGrammarParser.range_value_return b =null;


        Object LBRACK41_tree=null;
        Object TO42_tree=null;
        Object RBRACK43_tree=null;
        RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
        RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:165:2: ( LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:166:8: LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK
            {
            LBRACK41=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_range_term_in845); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK41);


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:167:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:167:9: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_in857);
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
            // 167:23: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:167:38: ^( QANYTHING QANYTHING[\"*\"] )
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


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:168:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==DATE_TOKEN||LA22_0==NUMBER||(LA22_0 >= PHRASE && LA22_0 <= PHRASE_ANYTHING)||LA22_0==STAR||LA22_0==TERM_NORMAL||LA22_0==TERM_TRUNCATED||LA22_0==TO) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:168:10: ( TO )? b= range_value
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:168:10: ( TO )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==TO) ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:168:10: TO
                            {
                            TO42=(Token)match(input,TO,FOLLOW_TO_in_range_term_in880); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO42);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_in885);
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
                    // 168:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:168:35: ( $b)?
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


            RBRACK43=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_range_term_in906); if (state.failed) return retval; 
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:173:1: range_term_ex : LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY ;
    public final ExtendedLuceneGrammarParser.range_term_ex_return range_term_ex() throws RecognitionException {
        ExtendedLuceneGrammarParser.range_term_ex_return retval = new ExtendedLuceneGrammarParser.range_term_ex_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LCURLY44=null;
        Token TO45=null;
        Token RCURLY46=null;
        ExtendedLuceneGrammarParser.range_value_return a =null;

        ExtendedLuceneGrammarParser.range_value_return b =null;


        Object LCURLY44_tree=null;
        Object TO45_tree=null;
        Object RCURLY46_tree=null;
        RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:174:2: ( LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:175:8: LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY
            {
            LCURLY44=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_range_term_ex926); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LCURLY.add(LCURLY44);


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:176:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:176:10: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_ex939);
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
            // 176:24: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:176:39: ^( QANYTHING QANYTHING[\"*\"] )
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


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:177:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==DATE_TOKEN||LA24_0==NUMBER||(LA24_0 >= PHRASE && LA24_0 <= PHRASE_ANYTHING)||LA24_0==STAR||LA24_0==TERM_NORMAL||LA24_0==TERM_TRUNCATED||LA24_0==TO) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:177:10: ( TO )? b= range_value
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:177:10: ( TO )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==TO) ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:177:10: TO
                            {
                            TO45=(Token)match(input,TO,FOLLOW_TO_in_range_term_ex962); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO45);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_ex967);
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
                    // 177:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:177:35: ( $b)?
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


            RCURLY46=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_range_term_ex988); if (state.failed) return retval; 
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:181:1: range_value : ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) );
    public final ExtendedLuceneGrammarParser.range_value_return range_value() throws RecognitionException {
        ExtendedLuceneGrammarParser.range_value_return retval = new ExtendedLuceneGrammarParser.range_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR52=null;
        ExtendedLuceneGrammarParser.truncated_return truncated47 =null;

        ExtendedLuceneGrammarParser.quoted_return quoted48 =null;

        ExtendedLuceneGrammarParser.quoted_truncated_return quoted_truncated49 =null;

        ExtendedLuceneGrammarParser.date_return date50 =null;

        ExtendedLuceneGrammarParser.normal_return normal51 =null;


        Object STAR52_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_date=new RewriteRuleSubtreeStream(adaptor,"rule date");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:182:2: ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) )
            int alt25=6;
            switch ( input.LA(1) ) {
            case TERM_TRUNCATED:
                {
                alt25=1;
                }
                break;
            case PHRASE:
                {
                alt25=2;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt25=3;
                }
                break;
            case DATE_TOKEN:
                {
                alt25=4;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
                {
                alt25=5;
                }
                break;
            case STAR:
                {
                alt25=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;

            }

            switch (alt25) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:183:2: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_range_value1002);
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
                    // 183:12: -> ^( QTRUNCATED truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:183:15: ^( QTRUNCATED truncated )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:184:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_range_value1015);
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
                    // 184:11: -> ^( QPHRASE quoted )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:184:14: ^( QPHRASE quoted )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:185:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_range_value1028);
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
                    // 185:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:185:24: ^( QPHRASETRUNC quoted_truncated )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:186:4: date
                    {
                    pushFollow(FOLLOW_date_in_range_value1041);
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
                    // 186:9: -> ^( QNORMAL date )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:186:12: ^( QNORMAL date )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:187:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_range_value1054);
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
                    // 187:11: -> ^( QNORMAL normal )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:187:14: ^( QNORMAL normal )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:188:4: STAR
                    {
                    STAR52=(Token)match(input,STAR,FOLLOW_STAR_in_range_value1068); if (state.failed) return retval; 
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
                    // 188:9: -> ^( QANYTHING STAR )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:188:12: ^( QANYTHING STAR )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:191:1: multi_value : LPAREN multiClause RPAREN -> multiClause ;
    public final ExtendedLuceneGrammarParser.multi_value_return multi_value() throws RecognitionException {
        ExtendedLuceneGrammarParser.multi_value_return retval = new ExtendedLuceneGrammarParser.multi_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN53=null;
        Token RPAREN55=null;
        ExtendedLuceneGrammarParser.multiClause_return multiClause54 =null;


        Object LPAREN53_tree=null;
        Object RPAREN55_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_multiClause=new RewriteRuleSubtreeStream(adaptor,"rule multiClause");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:192:2: ( LPAREN multiClause RPAREN -> multiClause )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:193:2: LPAREN multiClause RPAREN
            {
            LPAREN53=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_multi_value1089); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN53);


            pushFollow(FOLLOW_multiClause_in_multi_value1091);
            multiClause54=multiClause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_multiClause.add(multiClause54.getTree());

            RPAREN55=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_multi_value1093); if (state.failed) return retval; 
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
            // 193:28: -> multiClause
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:198:1: multiClause : ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
    public final ExtendedLuceneGrammarParser.multiClause_return multiClause() throws RecognitionException {
        ExtendedLuceneGrammarParser.multiClause_return retval = new ExtendedLuceneGrammarParser.multiClause_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.clauseOr_return clauseOr56 =null;


        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:199:2: ( ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:204:2: ( clauseOr )+
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:204:2: ( clauseOr )+
            int cnt26=0;
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( ((LA26_0 >= LBRACK && LA26_0 <= MINUS)||LA26_0==NUMBER||(LA26_0 >= PHRASE && LA26_0 <= PLUS)||LA26_0==QMARK||LA26_0==STAR||LA26_0==TERM_NORMAL||LA26_0==TERM_TRUNCATED) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:204:2: clauseOr
            	    {
            	    pushFollow(FOLLOW_clauseOr_in_multiClause1120);
            	    clauseOr56=clauseOr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr56.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt26 >= 1 ) break loop26;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(26, input);
                        throw eee;
                }
                cnt26++;
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
            // 204:12: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
            {
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:204:15: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:217:1: multiDefault : ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) ;
    public final ExtendedLuceneGrammarParser.multiDefault_return multiDefault() throws RecognitionException {
        ExtendedLuceneGrammarParser.multiDefault_return retval = new ExtendedLuceneGrammarParser.multiDefault_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.multiOr_return multiOr57 =null;


        RewriteRuleSubtreeStream stream_multiOr=new RewriteRuleSubtreeStream(adaptor,"rule multiOr");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:218:2: ( ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:219:2: ( multiOr )+
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:219:2: ( multiOr )+
            int cnt27=0;
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( ((LA27_0 >= LBRACK && LA27_0 <= LCURLY)||LA27_0==MINUS||LA27_0==NUMBER||(LA27_0 >= PHRASE && LA27_0 <= PLUS)||LA27_0==QMARK||LA27_0==STAR||LA27_0==TERM_NORMAL||LA27_0==TERM_TRUNCATED) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:219:2: multiOr
            	    {
            	    pushFollow(FOLLOW_multiOr_in_multiDefault1164);
            	    multiOr57=multiOr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_multiOr.add(multiOr57.getTree());

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
            // 219:11: -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
            {
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:219:14: ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:222:1: multiOr : (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* ;
    public final ExtendedLuceneGrammarParser.multiOr_return multiOr() throws RecognitionException {
        ExtendedLuceneGrammarParser.multiOr_return retval = new ExtendedLuceneGrammarParser.multiOr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.multiAnd_return first =null;

        ExtendedLuceneGrammarParser.multiAnd_return others =null;

        ExtendedLuceneGrammarParser.or_return or58 =null;


        RewriteRuleSubtreeStream stream_multiAnd=new RewriteRuleSubtreeStream(adaptor,"rule multiAnd");
        RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:223:2: ( (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:224:2: (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:224:2: (first= multiAnd -> $first)
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:224:3: first= multiAnd
            {
            pushFollow(FOLLOW_multiAnd_in_multiOr1192);
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
            // 224:19: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:224:30: ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==OR) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:224:31: or others= multiAnd
            	    {
            	    pushFollow(FOLLOW_or_in_multiOr1202);
            	    or58=or();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_or.add(or58.getTree());

            	    pushFollow(FOLLOW_multiAnd_in_multiOr1206);
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
            	    // 224:49: -> ^( OPERATOR[\"OR\"] ( multiAnd )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:224:52: ^( OPERATOR[\"OR\"] ( multiAnd )+ )
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
            	    break loop28;
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:227:1: multiAnd : (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* ;
    public final ExtendedLuceneGrammarParser.multiAnd_return multiAnd() throws RecognitionException {
        ExtendedLuceneGrammarParser.multiAnd_return retval = new ExtendedLuceneGrammarParser.multiAnd_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.multiNot_return first =null;

        ExtendedLuceneGrammarParser.multiNot_return others =null;

        ExtendedLuceneGrammarParser.and_return and59 =null;


        RewriteRuleSubtreeStream stream_multiNot=new RewriteRuleSubtreeStream(adaptor,"rule multiNot");
        RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:228:2: ( (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:229:2: (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:229:2: (first= multiNot -> $first)
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:229:3: first= multiNot
            {
            pushFollow(FOLLOW_multiNot_in_multiAnd1237);
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
            // 229:19: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:229:30: ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
            loop29:
            do {
                int alt29=2;
                int LA29_0 = input.LA(1);

                if ( (LA29_0==AND) ) {
                    alt29=1;
                }


                switch (alt29) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:229:31: and others= multiNot
            	    {
            	    pushFollow(FOLLOW_and_in_multiAnd1247);
            	    and59=and();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_and.add(and59.getTree());

            	    pushFollow(FOLLOW_multiNot_in_multiAnd1251);
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
            	    // 229:51: -> ^( OPERATOR[\"AND\"] ( multiNot )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:229:54: ^( OPERATOR[\"AND\"] ( multiNot )+ )
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
    // $ANTLR end "multiAnd"


    public static class multiNot_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiNot"
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:232:1: multiNot : (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )* ;
    public final ExtendedLuceneGrammarParser.multiNot_return multiNot() throws RecognitionException {
        ExtendedLuceneGrammarParser.multiNot_return retval = new ExtendedLuceneGrammarParser.multiNot_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.multiNear_return first =null;

        ExtendedLuceneGrammarParser.multiNear_return others =null;

        ExtendedLuceneGrammarParser.not_return not60 =null;


        RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
        RewriteRuleSubtreeStream stream_multiNear=new RewriteRuleSubtreeStream(adaptor,"rule multiNear");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:233:2: ( (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )* )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:234:2: (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:234:2: (first= multiNear -> $first)
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:234:3: first= multiNear
            {
            pushFollow(FOLLOW_multiNear_in_multiNot1282);
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
            // 234:20: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:234:31: ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==AND) ) {
                    int LA30_1 = input.LA(2);

                    if ( (LA30_1==NOT) ) {
                        alt30=1;
                    }


                }
                else if ( (LA30_0==NOT) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:234:32: not others= multiNear
            	    {
            	    pushFollow(FOLLOW_not_in_multiNot1292);
            	    not60=not();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_not.add(not60.getTree());

            	    pushFollow(FOLLOW_multiNear_in_multiNot1296);
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
            	    // 234:52: -> ^( OPERATOR[\"NOT\"] ( multiNear )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:234:55: ^( OPERATOR[\"NOT\"] ( multiNear )+ )
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
    // $ANTLR end "multiNot"


    public static class multiNear_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiNear"
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:237:1: multiNear : (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )* ;
    public final ExtendedLuceneGrammarParser.multiNear_return multiNear() throws RecognitionException {
        ExtendedLuceneGrammarParser.multiNear_return retval = new ExtendedLuceneGrammarParser.multiNear_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.multiBasic_return first =null;

        ExtendedLuceneGrammarParser.multiBasic_return others =null;

        ExtendedLuceneGrammarParser.near_return near61 =null;


        RewriteRuleSubtreeStream stream_near=new RewriteRuleSubtreeStream(adaptor,"rule near");
        RewriteRuleSubtreeStream stream_multiBasic=new RewriteRuleSubtreeStream(adaptor,"rule multiBasic");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:238:2: ( (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )* )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:239:2: (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:239:2: (first= multiBasic -> $first)
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:239:3: first= multiBasic
            {
            pushFollow(FOLLOW_multiBasic_in_multiNear1326);
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
            // 239:21: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:239:32: ( near others= multiBasic -> ^( near ( multiBasic )+ ) )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==NEAR) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:239:33: near others= multiBasic
            	    {
            	    pushFollow(FOLLOW_near_in_multiNear1336);
            	    near61=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near61.getTree());

            	    pushFollow(FOLLOW_multiBasic_in_multiNear1340);
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
            	    // 239:55: -> ^( near ( multiBasic )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:239:58: ^( near ( multiBasic )+ )
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
    // $ANTLR end "multiNear"


    public static class multiBasic_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiBasic"
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:243:1: multiBasic : mterm ;
    public final ExtendedLuceneGrammarParser.multiBasic_return multiBasic() throws RecognitionException {
        ExtendedLuceneGrammarParser.multiBasic_return retval = new ExtendedLuceneGrammarParser.multiBasic_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.mterm_return mterm62 =null;



        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:244:2: ( mterm )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:245:2: mterm
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_mterm_in_multiBasic1366);
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:248:1: mterm : ( modifier )? value -> ^( MODIFIER ( modifier )? value ) ;
    public final ExtendedLuceneGrammarParser.mterm_return mterm() throws RecognitionException {
        ExtendedLuceneGrammarParser.mterm_return retval = new ExtendedLuceneGrammarParser.mterm_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ExtendedLuceneGrammarParser.modifier_return modifier63 =null;

        ExtendedLuceneGrammarParser.value_return value64 =null;


        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:249:2: ( ( modifier )? value -> ^( MODIFIER ( modifier )? value ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:250:2: ( modifier )? value
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:250:2: ( modifier )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==MINUS||LA32_0==PLUS) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:250:2: modifier
                    {
                    pushFollow(FOLLOW_modifier_in_mterm1382);
                    modifier63=modifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_modifier.add(modifier63.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_value_in_mterm1385);
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
            // 250:18: -> ^( MODIFIER ( modifier )? value )
            {
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:250:21: ^( MODIFIER ( modifier )? value )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(MODIFIER, "MODIFIER")
                , root_1);

                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:250:32: ( modifier )?
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:254:1: normal : ( TERM_NORMAL | NUMBER );
    public final ExtendedLuceneGrammarParser.normal_return normal() throws RecognitionException {
        ExtendedLuceneGrammarParser.normal_return retval = new ExtendedLuceneGrammarParser.normal_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set65=null;

        Object set65_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:255:2: ( TERM_NORMAL | NUMBER )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:263:1: truncated : TERM_TRUNCATED ;
    public final ExtendedLuceneGrammarParser.truncated_return truncated() throws RecognitionException {
        ExtendedLuceneGrammarParser.truncated_return retval = new ExtendedLuceneGrammarParser.truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_TRUNCATED66=null;

        Object TERM_TRUNCATED66_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:264:2: ( TERM_TRUNCATED )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:265:2: TERM_TRUNCATED
            {
            root_0 = (Object)adaptor.nil();


            TERM_TRUNCATED66=(Token)match(input,TERM_TRUNCATED,FOLLOW_TERM_TRUNCATED_in_truncated1438); if (state.failed) return retval;
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:269:1: quoted_truncated : PHRASE_ANYTHING ;
    public final ExtendedLuceneGrammarParser.quoted_truncated_return quoted_truncated() throws RecognitionException {
        ExtendedLuceneGrammarParser.quoted_truncated_return retval = new ExtendedLuceneGrammarParser.quoted_truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE_ANYTHING67=null;

        Object PHRASE_ANYTHING67_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:270:2: ( PHRASE_ANYTHING )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:271:2: PHRASE_ANYTHING
            {
            root_0 = (Object)adaptor.nil();


            PHRASE_ANYTHING67=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1453); if (state.failed) return retval;
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:274:1: quoted : PHRASE ;
    public final ExtendedLuceneGrammarParser.quoted_return quoted() throws RecognitionException {
        ExtendedLuceneGrammarParser.quoted_return retval = new ExtendedLuceneGrammarParser.quoted_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE68=null;

        Object PHRASE68_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:274:8: ( PHRASE )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:275:2: PHRASE
            {
            root_0 = (Object)adaptor.nil();


            PHRASE68=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_quoted1465); if (state.failed) return retval;
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:281:1: operator : ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] ) ;
    public final ExtendedLuceneGrammarParser.operator_return operator() throws RecognitionException {
        ExtendedLuceneGrammarParser.operator_return retval = new ExtendedLuceneGrammarParser.operator_return();
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:281:9: ( ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:281:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] )
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:281:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] )
            int alt33=4;
            switch ( input.LA(1) ) {
            case AND:
                {
                alt33=1;
                }
                break;
            case OR:
                {
                alt33=2;
                }
                break;
            case NOT:
                {
                alt33=3;
                }
                break;
            case NEAR:
                {
                alt33=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;

            }

            switch (alt33) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:282:2: AND
                    {
                    AND69=(Token)match(input,AND,FOLLOW_AND_in_operator1481); if (state.failed) return retval; 
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
                    // 282:6: -> OPERATOR[\"AND\"]
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:283:4: OR
                    {
                    OR70=(Token)match(input,OR,FOLLOW_OR_in_operator1491); if (state.failed) return retval; 
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
                    // 283:7: -> OPERATOR[\"OR\"]
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:284:4: NOT
                    {
                    NOT71=(Token)match(input,NOT,FOLLOW_NOT_in_operator1501); if (state.failed) return retval; 
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
                    // 284:8: -> OPERATOR[\"NOT\"]
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:285:4: NEAR
                    {
                    NEAR72=(Token)match(input,NEAR,FOLLOW_NEAR_in_operator1511); if (state.failed) return retval; 
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
                    // 285:9: -> OPERATOR[\"NEAR\"]
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:288:1: modifier : ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] );
    public final ExtendedLuceneGrammarParser.modifier_return modifier() throws RecognitionException {
        ExtendedLuceneGrammarParser.modifier_return retval = new ExtendedLuceneGrammarParser.modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PLUS73=null;
        Token MINUS74=null;

        Object PLUS73_tree=null;
        Object MINUS74_tree=null;
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
        RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:288:9: ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] )
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==PLUS) ) {
                alt34=1;
            }
            else if ( (LA34_0==MINUS) ) {
                alt34=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 34, 0, input);

                throw nvae;

            }
            switch (alt34) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:289:2: PLUS
                    {
                    PLUS73=(Token)match(input,PLUS,FOLLOW_PLUS_in_modifier1528); if (state.failed) return retval; 
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
                    // 289:7: -> PLUS[\"+\"]
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:290:4: MINUS
                    {
                    MINUS74=(Token)match(input,MINUS,FOLLOW_MINUS_in_modifier1538); if (state.failed) return retval; 
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
                    // 290:10: -> MINUS[\"-\"]
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:302:1: term_modifier : ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) );
    public final ExtendedLuceneGrammarParser.term_modifier_return term_modifier() throws RecognitionException {
        ExtendedLuceneGrammarParser.term_modifier_return retval = new ExtendedLuceneGrammarParser.term_modifier_return();
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:302:15: ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) )
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==TILDE) ) {
                alt37=1;
            }
            else if ( (LA37_0==CARAT) ) {
                alt37=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;

            }
            switch (alt37) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:303:2: TILDE ( CARAT )?
                    {
                    TILDE75=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1556); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TILDE.add(TILDE75);


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:303:8: ( CARAT )?
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==CARAT) ) {
                        alt35=1;
                    }
                    switch (alt35) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:303:8: CARAT
                            {
                            CARAT76=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1558); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CARAT.add(CARAT76);


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
                    // 303:15: -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:303:18: ^( BOOST ( CARAT )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:303:26: ( CARAT )?
                        if ( stream_CARAT.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_CARAT.nextNode()
                            );

                        }
                        stream_CARAT.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:303:34: ^( FUZZY TILDE )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:304:4: CARAT ( TILDE )?
                    {
                    CARAT77=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1580); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CARAT.add(CARAT77);


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:304:10: ( TILDE )?
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==TILDE) ) {
                        alt36=1;
                    }
                    switch (alt36) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:304:10: TILDE
                            {
                            TILDE78=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1582); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TILDE.add(TILDE78);


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
                    // 304:17: -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:304:20: ^( BOOST CARAT )
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

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:304:35: ^( FUZZY ( TILDE )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FUZZY, "FUZZY")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:304:43: ( TILDE )?
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:324:1: boost : ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? ;
    public final ExtendedLuceneGrammarParser.boost_return boost() throws RecognitionException {
        ExtendedLuceneGrammarParser.boost_return retval = new ExtendedLuceneGrammarParser.boost_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token CARAT79=null;
        Token NUMBER80=null;

        Object CARAT79_tree=null;
        Object NUMBER80_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:324:7: ( ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:325:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )?
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:325:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:325:3: CARAT
            {
            CARAT79=(Token)match(input,CARAT,FOLLOW_CARAT_in_boost1614); if (state.failed) return retval; 
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
            // 325:9: -> ^( BOOST NUMBER[\"DEF\"] )
            {
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:325:12: ^( BOOST NUMBER[\"DEF\"] )
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


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:326:2: ( NUMBER -> ^( BOOST NUMBER ) )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==NUMBER) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:326:3: NUMBER
                    {
                    NUMBER80=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_boost1629); if (state.failed) return retval; 
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
                    // 326:10: -> ^( BOOST NUMBER )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:326:13: ^( BOOST NUMBER )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:329:1: fuzzy : ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? ;
    public final ExtendedLuceneGrammarParser.fuzzy_return fuzzy() throws RecognitionException {
        ExtendedLuceneGrammarParser.fuzzy_return retval = new ExtendedLuceneGrammarParser.fuzzy_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE81=null;
        Token NUMBER82=null;

        Object TILDE81_tree=null;
        Object NUMBER82_tree=null;
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:329:7: ( ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:330:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )?
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:330:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:330:3: TILDE
            {
            TILDE81=(Token)match(input,TILDE,FOLLOW_TILDE_in_fuzzy1652); if (state.failed) return retval; 
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
            // 330:9: -> ^( FUZZY NUMBER[\"DEF\"] )
            {
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:330:12: ^( FUZZY NUMBER[\"DEF\"] )
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


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:331:2: ( NUMBER -> ^( FUZZY NUMBER ) )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==NUMBER) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:331:3: NUMBER
                    {
                    NUMBER82=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_fuzzy1667); if (state.failed) return retval; 
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
                    // 331:10: -> ^( FUZZY NUMBER )
                    {
                        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:331:13: ^( FUZZY NUMBER )
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:334:1: not : ( ( AND NOT )=> AND NOT | NOT );
    public final ExtendedLuceneGrammarParser.not_return not() throws RecognitionException {
        ExtendedLuceneGrammarParser.not_return retval = new ExtendedLuceneGrammarParser.not_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND83=null;
        Token NOT84=null;
        Token NOT85=null;

        Object AND83_tree=null;
        Object NOT84_tree=null;
        Object NOT85_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:334:5: ( ( AND NOT )=> AND NOT | NOT )
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==AND) && (synpred4_ExtendedLuceneGrammar())) {
                alt40=1;
            }
            else if ( (LA40_0==NOT) ) {
                alt40=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;

            }
            switch (alt40) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:335:2: ( AND NOT )=> AND NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    AND83=(Token)match(input,AND,FOLLOW_AND_in_not1697); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    AND83_tree = 
                    (Object)adaptor.create(AND83)
                    ;
                    adaptor.addChild(root_0, AND83_tree);
                    }

                    NOT84=(Token)match(input,NOT,FOLLOW_NOT_in_not1699); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT84_tree = 
                    (Object)adaptor.create(NOT84)
                    ;
                    adaptor.addChild(root_0, NOT84_tree);
                    }

                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:336:4: NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    NOT85=(Token)match(input,NOT,FOLLOW_NOT_in_not1704); if (state.failed) return retval;
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:339:1: and : AND ;
    public final ExtendedLuceneGrammarParser.and_return and() throws RecognitionException {
        ExtendedLuceneGrammarParser.and_return retval = new ExtendedLuceneGrammarParser.and_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND86=null;

        Object AND86_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:339:6: ( AND )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:340:2: AND
            {
            root_0 = (Object)adaptor.nil();


            AND86=(Token)match(input,AND,FOLLOW_AND_in_and1718); if (state.failed) return retval;
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:343:1: or : OR ;
    public final ExtendedLuceneGrammarParser.or_return or() throws RecognitionException {
        ExtendedLuceneGrammarParser.or_return retval = new ExtendedLuceneGrammarParser.or_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OR87=null;

        Object OR87_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:343:5: ( OR )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:344:2: OR
            {
            root_0 = (Object)adaptor.nil();


            OR87=(Token)match(input,OR,FOLLOW_OR_in_or1732); if (state.failed) return retval;
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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:347:1: near : ( NEAR -> ^( OPERATOR[$NEAR] ) ) ;
    public final ExtendedLuceneGrammarParser.near_return near() throws RecognitionException {
        ExtendedLuceneGrammarParser.near_return retval = new ExtendedLuceneGrammarParser.near_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token NEAR88=null;

        Object NEAR88_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:347:7: ( ( NEAR -> ^( OPERATOR[$NEAR] ) ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:348:3: ( NEAR -> ^( OPERATOR[$NEAR] ) )
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:348:3: ( NEAR -> ^( OPERATOR[$NEAR] ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:348:4: NEAR
            {
            NEAR88=(Token)match(input,NEAR,FOLLOW_NEAR_in_near1749); if (state.failed) return retval; 
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
            // 348:9: -> ^( OPERATOR[$NEAR] )
            {
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:348:12: ^( OPERATOR[$NEAR] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OPERATOR, NEAR88)
                , root_1);

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

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
    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:351:1: date : DATE_TOKEN ;
    public final ExtendedLuceneGrammarParser.date_return date() throws RecognitionException {
        ExtendedLuceneGrammarParser.date_return retval = new ExtendedLuceneGrammarParser.date_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token DATE_TOKEN89=null;

        Object DATE_TOKEN89_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:351:6: ( DATE_TOKEN )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:353:2: DATE_TOKEN
            {
            root_0 = (Object)adaptor.nil();


            DATE_TOKEN89=(Token)match(input,DATE_TOKEN,FOLLOW_DATE_TOKEN_in_date1773); if (state.failed) return retval;
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

    // $ANTLR start synpred1_ExtendedLuceneGrammar
    public final void synpred1_ExtendedLuceneGrammar_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:2: ( modifier LPAREN ( clauseOr )+ RPAREN )
        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:3: modifier LPAREN ( clauseOr )+ RPAREN
        {
        pushFollow(FOLLOW_modifier_in_synpred1_ExtendedLuceneGrammar415);
        modifier();

        state._fsp--;
        if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1_ExtendedLuceneGrammar417); if (state.failed) return ;

        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:19: ( clauseOr )+
        int cnt41=0;
        loop41:
        do {
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( ((LA41_0 >= LBRACK && LA41_0 <= MINUS)||LA41_0==NUMBER||(LA41_0 >= PHRASE && LA41_0 <= PLUS)||LA41_0==QMARK||LA41_0==STAR||LA41_0==TERM_NORMAL||LA41_0==TERM_TRUNCATED) ) {
                alt41=1;
            }


            switch (alt41) {
        	case 1 :
        	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:125:19: clauseOr
        	    {
        	    pushFollow(FOLLOW_clauseOr_in_synpred1_ExtendedLuceneGrammar419);
        	    clauseOr();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt41 >= 1 ) break loop41;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(41, input);
                    throw eee;
            }
            cnt41++;
        } while (true);


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1_ExtendedLuceneGrammar422); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred1_ExtendedLuceneGrammar

    // $ANTLR start synpred2_ExtendedLuceneGrammar
    public final void synpred2_ExtendedLuceneGrammar_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:4: ( LPAREN ( clauseOr )+ RPAREN term_modifier )
        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:5: LPAREN ( clauseOr )+ RPAREN term_modifier
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred2_ExtendedLuceneGrammar476); if (state.failed) return ;

        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:12: ( clauseOr )+
        int cnt42=0;
        loop42:
        do {
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( ((LA42_0 >= LBRACK && LA42_0 <= MINUS)||LA42_0==NUMBER||(LA42_0 >= PHRASE && LA42_0 <= PLUS)||LA42_0==QMARK||LA42_0==STAR||LA42_0==TERM_NORMAL||LA42_0==TERM_TRUNCATED) ) {
                alt42=1;
            }


            switch (alt42) {
        	case 1 :
        	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:127:12: clauseOr
        	    {
        	    pushFollow(FOLLOW_clauseOr_in_synpred2_ExtendedLuceneGrammar478);
        	    clauseOr();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt42 >= 1 ) break loop42;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(42, input);
                    throw eee;
            }
            cnt42++;
        } while (true);


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred2_ExtendedLuceneGrammar481); if (state.failed) return ;

        pushFollow(FOLLOW_term_modifier_in_synpred2_ExtendedLuceneGrammar483);
        term_modifier();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_ExtendedLuceneGrammar

    // $ANTLR start synpred3_ExtendedLuceneGrammar
    public final void synpred3_ExtendedLuceneGrammar_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:129:4: ( LPAREN )
        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:129:5: LPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred3_ExtendedLuceneGrammar536); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred3_ExtendedLuceneGrammar

    // $ANTLR start synpred4_ExtendedLuceneGrammar
    public final void synpred4_ExtendedLuceneGrammar_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:335:2: ( AND NOT )
        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/ExtendedLuceneGrammar.g:335:3: AND NOT
        {
        match(input,AND,FOLLOW_AND_in_synpred4_ExtendedLuceneGrammar1691); if (state.failed) return ;

        match(input,NOT,FOLLOW_NOT_in_synpred4_ExtendedLuceneGrammar1693); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred4_ExtendedLuceneGrammar

    // Delegated rules

    public final boolean synpred3_ExtendedLuceneGrammar() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_ExtendedLuceneGrammar_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_ExtendedLuceneGrammar() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_ExtendedLuceneGrammar_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_ExtendedLuceneGrammar() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_ExtendedLuceneGrammar_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_ExtendedLuceneGrammar() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_ExtendedLuceneGrammar_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_clauseOr_in_mainQ212 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_EOF_in_mainQ215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clauseAnd_in_clauseOr242 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_or_in_clauseOr251 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseAnd_in_clauseOr255 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_clauseNot_in_clauseAnd284 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_and_in_clauseAnd294 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseNot_in_clauseAnd298 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_clauseNear_in_clauseNot329 = new BitSet(new long[]{0x0000000000800022L});
    public static final BitSet FOLLOW_not_in_clauseNot338 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseNear_in_clauseNot342 = new BitSet(new long[]{0x0000000000800022L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseNear373 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_near_in_clauseNear382 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseNear386 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_modifier_in_clauseBasic427 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic430 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic432 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic435 = new BitSet(new long[]{0x0001000000000102L});
    public static final BitSet FOLLOW_term_modifier_in_clauseBasic437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_clauseBasic487 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic490 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic492 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic495 = new BitSet(new long[]{0x0001000000000102L});
    public static final BitSet FOLLOW_term_modifier_in_clauseBasic497 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic542 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic544 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_clauseBasic559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom580 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_field_in_atom583 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_multi_value_in_atom585 = new BitSet(new long[]{0x0001000000000102L});
    public static final BitSet FOLLOW_term_modifier_in_atom587 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom623 = new BitSet(new long[]{0x0000A80119060000L});
    public static final BitSet FOLLOW_field_in_atom626 = new BitSet(new long[]{0x0000A80119060000L});
    public static final BitSet FOLLOW_value_in_atom629 = new BitSet(new long[]{0x0001000000000102L});
    public static final BitSet FOLLOW_term_modifier_in_atom631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_NORMAL_in_field678 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_COLON_in_field680 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_in_in_value699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_ex_in_value712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_value726 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_value740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_value754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_value767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QMARK_in_value780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_value793 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_COLON_in_value795 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_STAR_in_value799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_value814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACK_in_range_term_in845 = new BitSet(new long[]{0x0000A80019000800L});
    public static final BitSet FOLLOW_range_value_in_range_term_in857 = new BitSet(new long[]{0x0004A88019000800L});
    public static final BitSet FOLLOW_TO_in_range_term_in880 = new BitSet(new long[]{0x0000A80019000800L});
    public static final BitSet FOLLOW_range_value_in_range_term_in885 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_RBRACK_in_range_term_in906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LCURLY_in_range_term_ex926 = new BitSet(new long[]{0x0000A80019000800L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex939 = new BitSet(new long[]{0x0004A90019000800L});
    public static final BitSet FOLLOW_TO_in_range_term_ex962 = new BitSet(new long[]{0x0000A80019000800L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex967 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_RCURLY_in_range_term_ex988 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_range_value1002 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_range_value1015 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_range_value1028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_in_range_value1041 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_range_value1054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_range_value1068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_multi_value1089 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_multiClause_in_multi_value1091 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RPAREN_in_multi_value1093 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clauseOr_in_multiClause1120 = new BitSet(new long[]{0x0000A801391E0002L});
    public static final BitSet FOLLOW_multiOr_in_multiDefault1164 = new BitSet(new long[]{0x0000A80139160002L});
    public static final BitSet FOLLOW_multiAnd_in_multiOr1192 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_or_in_multiOr1202 = new BitSet(new long[]{0x0000A80139160000L});
    public static final BitSet FOLLOW_multiAnd_in_multiOr1206 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_multiNot_in_multiAnd1237 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_and_in_multiAnd1247 = new BitSet(new long[]{0x0000A80139160000L});
    public static final BitSet FOLLOW_multiNot_in_multiAnd1251 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_multiNear_in_multiNot1282 = new BitSet(new long[]{0x0000000000800022L});
    public static final BitSet FOLLOW_not_in_multiNot1292 = new BitSet(new long[]{0x0000A80139160000L});
    public static final BitSet FOLLOW_multiNear_in_multiNot1296 = new BitSet(new long[]{0x0000000000800022L});
    public static final BitSet FOLLOW_multiBasic_in_multiNear1326 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_near_in_multiNear1336 = new BitSet(new long[]{0x0000A80139160000L});
    public static final BitSet FOLLOW_multiBasic_in_multiNear1340 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_mterm_in_multiBasic1366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_mterm1382 = new BitSet(new long[]{0x0000A80119060000L});
    public static final BitSet FOLLOW_value_in_mterm1385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_TRUNCATED_in_truncated1438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_in_quoted1465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_operator1481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_operator1491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_operator1501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_operator1511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_modifier1528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_modifier1538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1556 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1580 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_boost1614 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_NUMBER_in_boost1629 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_fuzzy1652 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_NUMBER_in_fuzzy1667 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_not1697 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_NOT_in_not1699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_not1704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_and1718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_or1732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_near1749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DATE_TOKEN_in_date1773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_synpred1_ExtendedLuceneGrammar415 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1_ExtendedLuceneGrammar417 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_synpred1_ExtendedLuceneGrammar419 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1_ExtendedLuceneGrammar422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred2_ExtendedLuceneGrammar476 = new BitSet(new long[]{0x0000A801391E0000L});
    public static final BitSet FOLLOW_clauseOr_in_synpred2_ExtendedLuceneGrammar478 = new BitSet(new long[]{0x0000AA01391E0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred2_ExtendedLuceneGrammar481 = new BitSet(new long[]{0x0001000000000100L});
    public static final BitSet FOLLOW_term_modifier_in_synpred2_ExtendedLuceneGrammar483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred3_ExtendedLuceneGrammar536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_synpred4_ExtendedLuceneGrammar1691 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_NOT_in_synpred4_ExtendedLuceneGrammar1693 = new BitSet(new long[]{0x0000000000000002L});

}