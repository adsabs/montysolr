// $ANTLR 3.4 /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g 2012-05-10 12:28:24

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class ADSParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "AS_CHAR", "ATOM", "AUTHOR_SEARCH", "BOOST", "CARAT", "CLAUSE", "COLON", "COMMA", "DATE_RANGE", "DATE_TOKEN", "DQUOTE", "D_NUMBER", "EQUAL", "ESC_CHAR", "FIELD", "FUNC_NAME", "FUZZY", "HASH", "HOUR", "H_NUMBER", "IDENTIFIER", "INT", "LBRACK", "LPAREN", "MINUS", "MODIFIER", "M_NUMBER", "NEAR", "NOT", "NUMBER", "OPERATOR", "OR", "PHRASE", "PHRASE_ANYTHING", "PLUS", "QANYTHING", "QCOMMA", "QCOORDINATE", "QDATE", "QFUNC", "QIDENTIFIER", "QMARK", "QNORMAL", "QPHRASE", "QPHRASETRUNC", "QPOSITION", "QRANGEEX", "QRANGEIN", "QTRUNCATED", "RBRACK", "RPAREN", "SEMICOLON", "SQUOTE", "STAR", "S_NUMBER", "TERM_CHAR", "TERM_NORMAL", "TERM_START_CHAR", "TERM_TRUNCATED", "TILDE", "TMODIFIER", "TO", "WS", "'#'", "'/'", "'<=>'", "'='"
    };

    public static final int EOF=-1;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int AND=4;
    public static final int AS_CHAR=5;
    public static final int ATOM=6;
    public static final int AUTHOR_SEARCH=7;
    public static final int BOOST=8;
    public static final int CARAT=9;
    public static final int CLAUSE=10;
    public static final int COLON=11;
    public static final int COMMA=12;
    public static final int DATE_RANGE=13;
    public static final int DATE_TOKEN=14;
    public static final int DQUOTE=15;
    public static final int D_NUMBER=16;
    public static final int EQUAL=17;
    public static final int ESC_CHAR=18;
    public static final int FIELD=19;
    public static final int FUNC_NAME=20;
    public static final int FUZZY=21;
    public static final int HASH=22;
    public static final int HOUR=23;
    public static final int H_NUMBER=24;
    public static final int IDENTIFIER=25;
    public static final int INT=26;
    public static final int LBRACK=27;
    public static final int LPAREN=28;
    public static final int MINUS=29;
    public static final int MODIFIER=30;
    public static final int M_NUMBER=31;
    public static final int NEAR=32;
    public static final int NOT=33;
    public static final int NUMBER=34;
    public static final int OPERATOR=35;
    public static final int OR=36;
    public static final int PHRASE=37;
    public static final int PHRASE_ANYTHING=38;
    public static final int PLUS=39;
    public static final int QANYTHING=40;
    public static final int QCOMMA=41;
    public static final int QCOORDINATE=42;
    public static final int QDATE=43;
    public static final int QFUNC=44;
    public static final int QIDENTIFIER=45;
    public static final int QMARK=46;
    public static final int QNORMAL=47;
    public static final int QPHRASE=48;
    public static final int QPHRASETRUNC=49;
    public static final int QPOSITION=50;
    public static final int QRANGEEX=51;
    public static final int QRANGEIN=52;
    public static final int QTRUNCATED=53;
    public static final int RBRACK=54;
    public static final int RPAREN=55;
    public static final int SEMICOLON=56;
    public static final int SQUOTE=57;
    public static final int STAR=58;
    public static final int S_NUMBER=59;
    public static final int TERM_CHAR=60;
    public static final int TERM_NORMAL=61;
    public static final int TERM_START_CHAR=62;
    public static final int TERM_TRUNCATED=63;
    public static final int TILDE=64;
    public static final int TMODIFIER=65;
    public static final int TO=66;
    public static final int WS=67;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public ADSParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public ADSParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return ADSParser.tokenNames; }
    public String getGrammarFileName() { return "/dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g"; }


    public static class mainQ_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mainQ"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:41:1: mainQ : ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
    public final ADSParser.mainQ_return mainQ() throws RecognitionException {
        ADSParser.mainQ_return retval = new ADSParser.mainQ_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ADSParser.clauseOr_return clauseOr1 =null;


        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:41:7: ( ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:42:2: ( clauseOr )+
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:42:2: ( clauseOr )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==AUTHOR_SEARCH||LA1_0==DATE_RANGE||LA1_0==D_NUMBER||LA1_0==FUNC_NAME||(LA1_0 >= HOUR && LA1_0 <= IDENTIFIER)||(LA1_0 >= LBRACK && LA1_0 <= MINUS)||LA1_0==NUMBER||(LA1_0 >= PHRASE && LA1_0 <= PLUS)||LA1_0==QMARK||LA1_0==STAR||LA1_0==TERM_NORMAL||LA1_0==TERM_TRUNCATED||LA1_0==TO||LA1_0==68||(LA1_0 >= 70 && LA1_0 <= 71)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:42:2: clauseOr
            	    {
            	    pushFollow(FOLLOW_clauseOr_in_mainQ168);
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
            // 42:12: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
            {
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:42:15: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:46:1: clauseOr : (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* ;
    public final ADSParser.clauseOr_return clauseOr() throws RecognitionException {
        ADSParser.clauseOr_return retval = new ADSParser.clauseOr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ADSParser.clauseAnd_return first =null;

        ADSParser.clauseAnd_return others =null;

        ADSParser.or_return or2 =null;


        RewriteRuleSubtreeStream stream_clauseAnd=new RewriteRuleSubtreeStream(adaptor,"rule clauseAnd");
        RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:47:3: ( (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:47:5: (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:47:5: (first= clauseAnd -> $first)
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:47:6: first= clauseAnd
            {
            pushFollow(FOLLOW_clauseAnd_in_clauseOr201);
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
            // 47:22: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:47:33: ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==OR) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:47:34: or others= clauseAnd
            	    {
            	    pushFollow(FOLLOW_or_in_clauseOr210);
            	    or2=or();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_or.add(or2.getTree());

            	    pushFollow(FOLLOW_clauseAnd_in_clauseOr214);
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
            	    // 47:54: -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:47:57: ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:51:1: clauseAnd : (first= clauseSemicolon -> $first) ( and others= clauseSemicolon -> ^( OPERATOR[\"AND\"] ( clauseSemicolon )+ ) )* ;
    public final ADSParser.clauseAnd_return clauseAnd() throws RecognitionException {
        ADSParser.clauseAnd_return retval = new ADSParser.clauseAnd_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ADSParser.clauseSemicolon_return first =null;

        ADSParser.clauseSemicolon_return others =null;

        ADSParser.and_return and3 =null;


        RewriteRuleSubtreeStream stream_clauseSemicolon=new RewriteRuleSubtreeStream(adaptor,"rule clauseSemicolon");
        RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:52:3: ( (first= clauseSemicolon -> $first) ( and others= clauseSemicolon -> ^( OPERATOR[\"AND\"] ( clauseSemicolon )+ ) )* )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:52:5: (first= clauseSemicolon -> $first) ( and others= clauseSemicolon -> ^( OPERATOR[\"AND\"] ( clauseSemicolon )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:52:5: (first= clauseSemicolon -> $first)
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:52:6: first= clauseSemicolon
            {
            pushFollow(FOLLOW_clauseSemicolon_in_clauseAnd244);
            first=clauseSemicolon();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseSemicolon.add(first.getTree());

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
            // 52:29: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:52:40: ( and others= clauseSemicolon -> ^( OPERATOR[\"AND\"] ( clauseSemicolon )+ ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==AND) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:52:41: and others= clauseSemicolon
            	    {
            	    pushFollow(FOLLOW_and_in_clauseAnd254);
            	    and3=and();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_and.add(and3.getTree());

            	    pushFollow(FOLLOW_clauseSemicolon_in_clauseAnd258);
            	    others=clauseSemicolon();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseSemicolon.add(others.getTree());

            	    // AST REWRITE
            	    // elements: clauseSemicolon
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 52:68: -> ^( OPERATOR[\"AND\"] ( clauseSemicolon )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:52:71: ^( OPERATOR[\"AND\"] ( clauseSemicolon )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "AND")
            	        , root_1);

            	        if ( !(stream_clauseSemicolon.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_clauseSemicolon.hasNext() ) {
            	            adaptor.addChild(root_1, stream_clauseSemicolon.nextTree());

            	        }
            	        stream_clauseSemicolon.reset();

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


    public static class clauseSemicolon_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseSemicolon"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:55:1: clauseSemicolon : (first= clauseComma -> $first) ( semicolon others= clauseComma -> ^( OPERATOR[\"SEMICOLON\"] ( clauseComma )+ ) )* ;
    public final ADSParser.clauseSemicolon_return clauseSemicolon() throws RecognitionException {
        ADSParser.clauseSemicolon_return retval = new ADSParser.clauseSemicolon_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ADSParser.clauseComma_return first =null;

        ADSParser.clauseComma_return others =null;

        ADSParser.semicolon_return semicolon4 =null;


        RewriteRuleSubtreeStream stream_clauseComma=new RewriteRuleSubtreeStream(adaptor,"rule clauseComma");
        RewriteRuleSubtreeStream stream_semicolon=new RewriteRuleSubtreeStream(adaptor,"rule semicolon");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:56:3: ( (first= clauseComma -> $first) ( semicolon others= clauseComma -> ^( OPERATOR[\"SEMICOLON\"] ( clauseComma )+ ) )* )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:56:5: (first= clauseComma -> $first) ( semicolon others= clauseComma -> ^( OPERATOR[\"SEMICOLON\"] ( clauseComma )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:56:5: (first= clauseComma -> $first)
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:56:6: first= clauseComma
            {
            pushFollow(FOLLOW_clauseComma_in_clauseSemicolon287);
            first=clauseComma();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseComma.add(first.getTree());

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
            // 56:25: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:56:36: ( semicolon others= clauseComma -> ^( OPERATOR[\"SEMICOLON\"] ( clauseComma )+ ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==SEMICOLON) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:56:37: semicolon others= clauseComma
            	    {
            	    pushFollow(FOLLOW_semicolon_in_clauseSemicolon297);
            	    semicolon4=semicolon();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_semicolon.add(semicolon4.getTree());

            	    pushFollow(FOLLOW_clauseComma_in_clauseSemicolon301);
            	    others=clauseComma();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseComma.add(others.getTree());

            	    // AST REWRITE
            	    // elements: clauseComma
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 56:66: -> ^( OPERATOR[\"SEMICOLON\"] ( clauseComma )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:56:69: ^( OPERATOR[\"SEMICOLON\"] ( clauseComma )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "SEMICOLON")
            	        , root_1);

            	        if ( !(stream_clauseComma.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_clauseComma.hasNext() ) {
            	            adaptor.addChild(root_1, stream_clauseComma.nextTree());

            	        }
            	        stream_clauseComma.reset();

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
    // $ANTLR end "clauseSemicolon"


    public static class clauseComma_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseComma"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:59:1: clauseComma : (first= clauseNot -> $first) ( comma others= clauseNot -> ^( OPERATOR[\"COMMA\"] ( clauseNot )+ ) )* ;
    public final ADSParser.clauseComma_return clauseComma() throws RecognitionException {
        ADSParser.clauseComma_return retval = new ADSParser.clauseComma_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ADSParser.clauseNot_return first =null;

        ADSParser.clauseNot_return others =null;

        ADSParser.comma_return comma5 =null;


        RewriteRuleSubtreeStream stream_comma=new RewriteRuleSubtreeStream(adaptor,"rule comma");
        RewriteRuleSubtreeStream stream_clauseNot=new RewriteRuleSubtreeStream(adaptor,"rule clauseNot");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:60:3: ( (first= clauseNot -> $first) ( comma others= clauseNot -> ^( OPERATOR[\"COMMA\"] ( clauseNot )+ ) )* )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:60:5: (first= clauseNot -> $first) ( comma others= clauseNot -> ^( OPERATOR[\"COMMA\"] ( clauseNot )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:60:5: (first= clauseNot -> $first)
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:60:6: first= clauseNot
            {
            pushFollow(FOLLOW_clauseNot_in_clauseComma330);
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
            // 60:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:60:34: ( comma others= clauseNot -> ^( OPERATOR[\"COMMA\"] ( clauseNot )+ ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==COMMA) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:60:35: comma others= clauseNot
            	    {
            	    pushFollow(FOLLOW_comma_in_clauseComma340);
            	    comma5=comma();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_comma.add(comma5.getTree());

            	    pushFollow(FOLLOW_clauseNot_in_clauseComma344);
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
            	    // 60:58: -> ^( OPERATOR[\"COMMA\"] ( clauseNot )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:60:61: ^( OPERATOR[\"COMMA\"] ( clauseNot )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "COMMA")
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
    // $ANTLR end "clauseComma"


    public static class clauseNot_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseNot"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:63:1: clauseNot : (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* ;
    public final ADSParser.clauseNot_return clauseNot() throws RecognitionException {
        ADSParser.clauseNot_return retval = new ADSParser.clauseNot_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ADSParser.clauseNear_return first =null;

        ADSParser.clauseNear_return others =null;

        ADSParser.not_return not6 =null;


        RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
        RewriteRuleSubtreeStream stream_clauseNear=new RewriteRuleSubtreeStream(adaptor,"rule clauseNear");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:64:3: ( (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:64:5: (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:64:5: (first= clauseNear -> $first)
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:64:6: first= clauseNear
            {
            pushFollow(FOLLOW_clauseNear_in_clauseNot377);
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
            // 64:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:64:34: ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==AND) ) {
                    int LA6_2 = input.LA(2);

                    if ( (LA6_2==NOT) ) {
                        alt6=1;
                    }


                }
                else if ( (LA6_0==NOT) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:64:35: not others= clauseNear
            	    {
            	    pushFollow(FOLLOW_not_in_clauseNot386);
            	    not6=not();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_not.add(not6.getTree());

            	    pushFollow(FOLLOW_clauseNear_in_clauseNot390);
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
            	    // 64:57: -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:64:60: ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
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
            	    break loop6;
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:67:1: clauseNear : (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* ;
    public final ADSParser.clauseNear_return clauseNear() throws RecognitionException {
        ADSParser.clauseNear_return retval = new ADSParser.clauseNear_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ADSParser.clauseBasic_return first =null;

        ADSParser.clauseBasic_return others =null;

        ADSParser.near_return near7 =null;


        RewriteRuleSubtreeStream stream_clauseBasic=new RewriteRuleSubtreeStream(adaptor,"rule clauseBasic");
        RewriteRuleSubtreeStream stream_near=new RewriteRuleSubtreeStream(adaptor,"rule near");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:68:3: ( (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:68:5: (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:68:5: (first= clauseBasic -> $first)
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:68:6: first= clauseBasic
            {
            pushFollow(FOLLOW_clauseBasic_in_clauseNear421);
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
            // 68:24: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:68:35: ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==NEAR) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:68:36: near others= clauseBasic
            	    {
            	    pushFollow(FOLLOW_near_in_clauseNear430);
            	    near7=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near7.getTree());

            	    pushFollow(FOLLOW_clauseBasic_in_clauseNear434);
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
            	    // 68:60: -> ^( near ( clauseBasic )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:68:63: ^( near ( clauseBasic )+ )
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
            	    break loop7;
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:71:1: clauseBasic : ( ( ( lmodifier )? func_name )=> ( lmodifier )? func_name ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) ) | ( lmodifier LPAREN ( clauseOr )+ RPAREN )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN rmodifier )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom );
    public final ADSParser.clauseBasic_return clauseBasic() throws RecognitionException {
        ADSParser.clauseBasic_return retval = new ADSParser.clauseBasic_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token RPAREN11=null;
        Token LPAREN14=null;
        Token RPAREN16=null;
        Token LPAREN19=null;
        Token RPAREN21=null;
        Token LPAREN23=null;
        Token RPAREN25=null;
        ADSParser.lmodifier_return lmodifier8 =null;

        ADSParser.func_name_return func_name9 =null;

        ADSParser.clauseOr_return clauseOr10 =null;

        ADSParser.rmodifier_return rmodifier12 =null;

        ADSParser.lmodifier_return lmodifier13 =null;

        ADSParser.clauseOr_return clauseOr15 =null;

        ADSParser.rmodifier_return rmodifier17 =null;

        ADSParser.lmodifier_return lmodifier18 =null;

        ADSParser.clauseOr_return clauseOr20 =null;

        ADSParser.rmodifier_return rmodifier22 =null;

        ADSParser.clauseOr_return clauseOr24 =null;

        ADSParser.atom_return atom26 =null;


        Object RPAREN11_tree=null;
        Object LPAREN14_tree=null;
        Object RPAREN16_tree=null;
        Object LPAREN19_tree=null;
        Object RPAREN21_tree=null;
        Object LPAREN23_tree=null;
        Object RPAREN25_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_lmodifier=new RewriteRuleSubtreeStream(adaptor,"rule lmodifier");
        RewriteRuleSubtreeStream stream_func_name=new RewriteRuleSubtreeStream(adaptor,"rule func_name");
        RewriteRuleSubtreeStream stream_rmodifier=new RewriteRuleSubtreeStream(adaptor,"rule rmodifier");
        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:72:2: ( ( ( lmodifier )? func_name )=> ( lmodifier )? func_name ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) ) | ( lmodifier LPAREN ( clauseOr )+ RPAREN )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN rmodifier )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom )
            int alt18=5;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==PLUS) ) {
                int LA18_1 = input.LA(2);

                if ( (synpred1_ADS()) ) {
                    alt18=1;
                }
                else if ( (synpred2_ADS()) ) {
                    alt18=2;
                }
                else if ( (synpred3_ADS()) ) {
                    alt18=3;
                }
                else if ( (true) ) {
                    alt18=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA18_0==MINUS) ) {
                int LA18_2 = input.LA(2);

                if ( (synpred1_ADS()) ) {
                    alt18=1;
                }
                else if ( (synpred2_ADS()) ) {
                    alt18=2;
                }
                else if ( (synpred3_ADS()) ) {
                    alt18=3;
                }
                else if ( (true) ) {
                    alt18=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 2, input);

                    throw nvae;

                }
            }
            else if ( (LA18_0==71) ) {
                int LA18_3 = input.LA(2);

                if ( (synpred1_ADS()) ) {
                    alt18=1;
                }
                else if ( (synpred2_ADS()) ) {
                    alt18=2;
                }
                else if ( (synpred3_ADS()) ) {
                    alt18=3;
                }
                else if ( (true) ) {
                    alt18=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 3, input);

                    throw nvae;

                }
            }
            else if ( (LA18_0==68) ) {
                int LA18_4 = input.LA(2);

                if ( (synpred1_ADS()) ) {
                    alt18=1;
                }
                else if ( (synpred2_ADS()) ) {
                    alt18=2;
                }
                else if ( (synpred3_ADS()) ) {
                    alt18=3;
                }
                else if ( (true) ) {
                    alt18=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 4, input);

                    throw nvae;

                }
            }
            else if ( (LA18_0==FUNC_NAME) && (synpred1_ADS())) {
                alt18=1;
            }
            else if ( (LA18_0==LPAREN) ) {
                int LA18_6 = input.LA(2);

                if ( (synpred2_ADS()) ) {
                    alt18=2;
                }
                else if ( (synpred3_ADS()) ) {
                    alt18=3;
                }
                else if ( (synpred4_ADS()) ) {
                    alt18=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 6, input);

                    throw nvae;

                }
            }
            else if ( (LA18_0==AUTHOR_SEARCH||LA18_0==DATE_RANGE||LA18_0==D_NUMBER||(LA18_0 >= HOUR && LA18_0 <= IDENTIFIER)||LA18_0==LBRACK||LA18_0==NUMBER||(LA18_0 >= PHRASE && LA18_0 <= PHRASE_ANYTHING)||LA18_0==QMARK||LA18_0==STAR||LA18_0==TERM_NORMAL||LA18_0==TERM_TRUNCATED||LA18_0==TO||LA18_0==70) ) {
                alt18=5;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }
            switch (alt18) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:3: ( ( lmodifier )? func_name )=> ( lmodifier )? func_name ( clauseOr )+ RPAREN ( rmodifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:29: ( lmodifier )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==MINUS||LA8_0==PLUS||LA8_0==68||LA8_0==71) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:29: lmodifier
                            {
                            pushFollow(FOLLOW_lmodifier_in_clauseBasic473);
                            lmodifier8=lmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier8.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_func_name_in_clauseBasic476);
                    func_name9=func_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_func_name.add(func_name9.getTree());

                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:50: ( clauseOr )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==AUTHOR_SEARCH||LA9_0==DATE_RANGE||LA9_0==D_NUMBER||LA9_0==FUNC_NAME||(LA9_0 >= HOUR && LA9_0 <= IDENTIFIER)||(LA9_0 >= LBRACK && LA9_0 <= MINUS)||LA9_0==NUMBER||(LA9_0 >= PHRASE && LA9_0 <= PLUS)||LA9_0==QMARK||LA9_0==STAR||LA9_0==TERM_NORMAL||LA9_0==TERM_TRUNCATED||LA9_0==TO||LA9_0==68||(LA9_0 >= 70 && LA9_0 <= 71)) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:50: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic478);
                    	    clauseOr10=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr10.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt9 >= 1 ) break loop9;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);


                    RPAREN11=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic482); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN11);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:68: ( rmodifier )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==CARAT||LA10_0==TILDE) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:68: rmodifier
                            {
                            pushFollow(FOLLOW_rmodifier_in_clauseBasic484);
                            rmodifier12=rmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier12.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: lmodifier, func_name, clauseOr, rmodifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 74:3: -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:74:6: ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:74:15: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:74:26: ( lmodifier )?
                        if ( stream_lmodifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_lmodifier.nextTree());

                        }
                        stream_lmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:74:37: ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:74:49: ( rmodifier )?
                        if ( stream_rmodifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_rmodifier.nextTree());

                        }
                        stream_rmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:74:60: ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
                        {
                        Object root_4 = (Object)adaptor.nil();
                        root_4 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QFUNC, "QFUNC")
                        , root_4);

                        adaptor.addChild(root_4, stream_func_name.nextTree());

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:74:78: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
                        {
                        Object root_5 = (Object)adaptor.nil();
                        root_5 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(OPERATOR, "DEFOP")
                        , root_5);

                        if ( !(stream_clauseOr.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_clauseOr.hasNext() ) {
                            adaptor.addChild(root_5, stream_clauseOr.nextTree());

                        }
                        stream_clauseOr.reset();

                        adaptor.addChild(root_4, root_5);
                        }

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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:4: ( lmodifier LPAREN ( clauseOr )+ RPAREN )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:43: ( lmodifier )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==MINUS||LA11_0==PLUS||LA11_0==68||LA11_0==71) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:43: lmodifier
                            {
                            pushFollow(FOLLOW_lmodifier_in_clauseBasic539);
                            lmodifier13=lmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier13.getTree());

                            }
                            break;

                    }


                    LPAREN14=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic542); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN14);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:61: ( clauseOr )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==AUTHOR_SEARCH||LA12_0==DATE_RANGE||LA12_0==D_NUMBER||LA12_0==FUNC_NAME||(LA12_0 >= HOUR && LA12_0 <= IDENTIFIER)||(LA12_0 >= LBRACK && LA12_0 <= MINUS)||LA12_0==NUMBER||(LA12_0 >= PHRASE && LA12_0 <= PLUS)||LA12_0==QMARK||LA12_0==STAR||LA12_0==TERM_NORMAL||LA12_0==TERM_TRUNCATED||LA12_0==TO||LA12_0==68||(LA12_0 >= 70 && LA12_0 <= 71)) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:61: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic544);
                    	    clauseOr15=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr15.getTree());

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


                    RPAREN16=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic547); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN16);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:78: ( rmodifier )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==CARAT||LA13_0==TILDE) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:78: rmodifier
                            {
                            pushFollow(FOLLOW_rmodifier_in_clauseBasic549);
                            rmodifier17=rmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier17.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: clauseOr, lmodifier, rmodifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 76:3: -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:76:6: ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:76:15: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:76:26: ( lmodifier )?
                        if ( stream_lmodifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_lmodifier.nextTree());

                        }
                        stream_lmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:76:37: ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:76:49: ( rmodifier )?
                        if ( stream_rmodifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_rmodifier.nextTree());

                        }
                        stream_rmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:76:60: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:4: ( LPAREN ( clauseOr )+ RPAREN rmodifier )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:42: ( lmodifier )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==MINUS||LA14_0==PLUS||LA14_0==68||LA14_0==71) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:42: lmodifier
                            {
                            pushFollow(FOLLOW_lmodifier_in_clauseBasic599);
                            lmodifier18=lmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier18.getTree());

                            }
                            break;

                    }


                    LPAREN19=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic602); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN19);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:60: ( clauseOr )+
                    int cnt15=0;
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==AUTHOR_SEARCH||LA15_0==DATE_RANGE||LA15_0==D_NUMBER||LA15_0==FUNC_NAME||(LA15_0 >= HOUR && LA15_0 <= IDENTIFIER)||(LA15_0 >= LBRACK && LA15_0 <= MINUS)||LA15_0==NUMBER||(LA15_0 >= PHRASE && LA15_0 <= PLUS)||LA15_0==QMARK||LA15_0==STAR||LA15_0==TERM_NORMAL||LA15_0==TERM_TRUNCATED||LA15_0==TO||LA15_0==68||(LA15_0 >= 70 && LA15_0 <= 71)) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:60: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic604);
                    	    clauseOr20=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr20.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt15 >= 1 ) break loop15;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(15, input);
                                throw eee;
                        }
                        cnt15++;
                    } while (true);


                    RPAREN21=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic607); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN21);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:77: ( rmodifier )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==CARAT||LA16_0==TILDE) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:77: rmodifier
                            {
                            pushFollow(FOLLOW_rmodifier_in_clauseBasic609);
                            rmodifier22=rmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier22.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: rmodifier, clauseOr, lmodifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 78:3: -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:78:6: ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:78:15: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:78:26: ( lmodifier )?
                        if ( stream_lmodifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_lmodifier.nextTree());

                        }
                        stream_lmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:78:37: ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:78:49: ( rmodifier )?
                        if ( stream_rmodifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_rmodifier.nextTree());

                        }
                        stream_rmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:78:60: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
                case 4 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:79:4: ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN
                    {
                    LPAREN23=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic653); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN23);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:79:23: ( clauseOr )+
                    int cnt17=0;
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0==AUTHOR_SEARCH||LA17_0==DATE_RANGE||LA17_0==D_NUMBER||LA17_0==FUNC_NAME||(LA17_0 >= HOUR && LA17_0 <= IDENTIFIER)||(LA17_0 >= LBRACK && LA17_0 <= MINUS)||LA17_0==NUMBER||(LA17_0 >= PHRASE && LA17_0 <= PLUS)||LA17_0==QMARK||LA17_0==STAR||LA17_0==TERM_NORMAL||LA17_0==TERM_TRUNCATED||LA17_0==TO||LA17_0==68||(LA17_0 >= 70 && LA17_0 <= 71)) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:79:23: clauseOr
                    	    {
                    	    pushFollow(FOLLOW_clauseOr_in_clauseBasic655);
                    	    clauseOr24=clauseOr();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr24.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt17 >= 1 ) break loop17;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(17, input);
                                throw eee;
                        }
                        cnt17++;
                    } while (true);


                    RPAREN25=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic658); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN25);


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
                    // 80:3: -> ( clauseOr )+
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
                case 5 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:81:4: atom
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_atom_in_clauseBasic670);
                    atom26=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom26.getTree());

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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:85:1: atom : ( ( lmodifier )? field multi_value ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) ) ) ) | ( lmodifier )? ( field )? value ( rmodifier )? -> ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) ) ) | ( lmodifier )? ( STAR COLON )? STAR -> ^( MODIFIER ( lmodifier )? ^( QANYTHING STAR[\"*\"] ) ) );
    public final ADSParser.atom_return atom() throws RecognitionException {
        ADSParser.atom_return retval = new ADSParser.atom_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR36=null;
        Token COLON37=null;
        Token STAR38=null;
        ADSParser.lmodifier_return lmodifier27 =null;

        ADSParser.field_return field28 =null;

        ADSParser.multi_value_return multi_value29 =null;

        ADSParser.rmodifier_return rmodifier30 =null;

        ADSParser.lmodifier_return lmodifier31 =null;

        ADSParser.field_return field32 =null;

        ADSParser.value_return value33 =null;

        ADSParser.rmodifier_return rmodifier34 =null;

        ADSParser.lmodifier_return lmodifier35 =null;


        Object STAR36_tree=null;
        Object COLON37_tree=null;
        Object STAR38_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_lmodifier=new RewriteRuleSubtreeStream(adaptor,"rule lmodifier");
        RewriteRuleSubtreeStream stream_multi_value=new RewriteRuleSubtreeStream(adaptor,"rule multi_value");
        RewriteRuleSubtreeStream stream_rmodifier=new RewriteRuleSubtreeStream(adaptor,"rule rmodifier");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:86:2: ( ( lmodifier )? field multi_value ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) ) ) ) | ( lmodifier )? ( field )? value ( rmodifier )? -> ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) ) ) | ( lmodifier )? ( STAR COLON )? STAR -> ^( MODIFIER ( lmodifier )? ^( QANYTHING STAR[\"*\"] ) ) )
            int alt26=3;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                switch ( input.LA(2) ) {
                case TERM_NORMAL:
                    {
                    int LA26_5 = input.LA(3);

                    if ( (LA26_5==COLON) ) {
                        int LA26_8 = input.LA(4);

                        if ( (LA26_8==LPAREN) ) {
                            alt26=1;
                        }
                        else if ( (LA26_8==AUTHOR_SEARCH||LA26_8==DATE_RANGE||LA26_8==D_NUMBER||(LA26_8 >= HOUR && LA26_8 <= IDENTIFIER)||LA26_8==LBRACK||LA26_8==NUMBER||(LA26_8 >= PHRASE && LA26_8 <= PHRASE_ANYTHING)||LA26_8==QMARK||LA26_8==TERM_NORMAL||LA26_8==TERM_TRUNCATED||LA26_8==TO||LA26_8==70) ) {
                            alt26=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 26, 8, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA26_5==EOF||LA26_5==AND||LA26_5==AUTHOR_SEARCH||LA26_5==CARAT||(LA26_5 >= COMMA && LA26_5 <= DATE_RANGE)||LA26_5==D_NUMBER||LA26_5==FUNC_NAME||(LA26_5 >= HOUR && LA26_5 <= IDENTIFIER)||(LA26_5 >= LBRACK && LA26_5 <= MINUS)||(LA26_5 >= NEAR && LA26_5 <= NUMBER)||(LA26_5 >= OR && LA26_5 <= PLUS)||LA26_5==QMARK||(LA26_5 >= RPAREN && LA26_5 <= SEMICOLON)||LA26_5==STAR||LA26_5==TERM_NORMAL||(LA26_5 >= TERM_TRUNCATED && LA26_5 <= TILDE)||LA26_5==TO||LA26_5==68||(LA26_5 >= 70 && LA26_5 <= 71)) ) {
                        alt26=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 26, 5, input);

                        throw nvae;

                    }
                    }
                    break;
                case AUTHOR_SEARCH:
                case DATE_RANGE:
                case D_NUMBER:
                case HOUR:
                case H_NUMBER:
                case IDENTIFIER:
                case LBRACK:
                case NUMBER:
                case PHRASE:
                case PHRASE_ANYTHING:
                case QMARK:
                case TERM_TRUNCATED:
                case TO:
                case 70:
                    {
                    alt26=2;
                    }
                    break;
                case STAR:
                    {
                    alt26=3;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 26, 1, input);

                    throw nvae;

                }

                }
                break;
            case MINUS:
                {
                switch ( input.LA(2) ) {
                case TERM_NORMAL:
                    {
                    int LA26_5 = input.LA(3);

                    if ( (LA26_5==COLON) ) {
                        int LA26_8 = input.LA(4);

                        if ( (LA26_8==LPAREN) ) {
                            alt26=1;
                        }
                        else if ( (LA26_8==AUTHOR_SEARCH||LA26_8==DATE_RANGE||LA26_8==D_NUMBER||(LA26_8 >= HOUR && LA26_8 <= IDENTIFIER)||LA26_8==LBRACK||LA26_8==NUMBER||(LA26_8 >= PHRASE && LA26_8 <= PHRASE_ANYTHING)||LA26_8==QMARK||LA26_8==TERM_NORMAL||LA26_8==TERM_TRUNCATED||LA26_8==TO||LA26_8==70) ) {
                            alt26=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 26, 8, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA26_5==EOF||LA26_5==AND||LA26_5==AUTHOR_SEARCH||LA26_5==CARAT||(LA26_5 >= COMMA && LA26_5 <= DATE_RANGE)||LA26_5==D_NUMBER||LA26_5==FUNC_NAME||(LA26_5 >= HOUR && LA26_5 <= IDENTIFIER)||(LA26_5 >= LBRACK && LA26_5 <= MINUS)||(LA26_5 >= NEAR && LA26_5 <= NUMBER)||(LA26_5 >= OR && LA26_5 <= PLUS)||LA26_5==QMARK||(LA26_5 >= RPAREN && LA26_5 <= SEMICOLON)||LA26_5==STAR||LA26_5==TERM_NORMAL||(LA26_5 >= TERM_TRUNCATED && LA26_5 <= TILDE)||LA26_5==TO||LA26_5==68||(LA26_5 >= 70 && LA26_5 <= 71)) ) {
                        alt26=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 26, 5, input);

                        throw nvae;

                    }
                    }
                    break;
                case AUTHOR_SEARCH:
                case DATE_RANGE:
                case D_NUMBER:
                case HOUR:
                case H_NUMBER:
                case IDENTIFIER:
                case LBRACK:
                case NUMBER:
                case PHRASE:
                case PHRASE_ANYTHING:
                case QMARK:
                case TERM_TRUNCATED:
                case TO:
                case 70:
                    {
                    alt26=2;
                    }
                    break;
                case STAR:
                    {
                    alt26=3;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 26, 2, input);

                    throw nvae;

                }

                }
                break;
            case 71:
                {
                switch ( input.LA(2) ) {
                case TERM_NORMAL:
                    {
                    int LA26_5 = input.LA(3);

                    if ( (LA26_5==COLON) ) {
                        int LA26_8 = input.LA(4);

                        if ( (LA26_8==LPAREN) ) {
                            alt26=1;
                        }
                        else if ( (LA26_8==AUTHOR_SEARCH||LA26_8==DATE_RANGE||LA26_8==D_NUMBER||(LA26_8 >= HOUR && LA26_8 <= IDENTIFIER)||LA26_8==LBRACK||LA26_8==NUMBER||(LA26_8 >= PHRASE && LA26_8 <= PHRASE_ANYTHING)||LA26_8==QMARK||LA26_8==TERM_NORMAL||LA26_8==TERM_TRUNCATED||LA26_8==TO||LA26_8==70) ) {
                            alt26=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 26, 8, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA26_5==EOF||LA26_5==AND||LA26_5==AUTHOR_SEARCH||LA26_5==CARAT||(LA26_5 >= COMMA && LA26_5 <= DATE_RANGE)||LA26_5==D_NUMBER||LA26_5==FUNC_NAME||(LA26_5 >= HOUR && LA26_5 <= IDENTIFIER)||(LA26_5 >= LBRACK && LA26_5 <= MINUS)||(LA26_5 >= NEAR && LA26_5 <= NUMBER)||(LA26_5 >= OR && LA26_5 <= PLUS)||LA26_5==QMARK||(LA26_5 >= RPAREN && LA26_5 <= SEMICOLON)||LA26_5==STAR||LA26_5==TERM_NORMAL||(LA26_5 >= TERM_TRUNCATED && LA26_5 <= TILDE)||LA26_5==TO||LA26_5==68||(LA26_5 >= 70 && LA26_5 <= 71)) ) {
                        alt26=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 26, 5, input);

                        throw nvae;

                    }
                    }
                    break;
                case AUTHOR_SEARCH:
                case DATE_RANGE:
                case D_NUMBER:
                case HOUR:
                case H_NUMBER:
                case IDENTIFIER:
                case LBRACK:
                case NUMBER:
                case PHRASE:
                case PHRASE_ANYTHING:
                case QMARK:
                case TERM_TRUNCATED:
                case TO:
                case 70:
                    {
                    alt26=2;
                    }
                    break;
                case STAR:
                    {
                    alt26=3;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 26, 3, input);

                    throw nvae;

                }

                }
                break;
            case 68:
                {
                switch ( input.LA(2) ) {
                case TERM_NORMAL:
                    {
                    int LA26_5 = input.LA(3);

                    if ( (LA26_5==COLON) ) {
                        int LA26_8 = input.LA(4);

                        if ( (LA26_8==LPAREN) ) {
                            alt26=1;
                        }
                        else if ( (LA26_8==AUTHOR_SEARCH||LA26_8==DATE_RANGE||LA26_8==D_NUMBER||(LA26_8 >= HOUR && LA26_8 <= IDENTIFIER)||LA26_8==LBRACK||LA26_8==NUMBER||(LA26_8 >= PHRASE && LA26_8 <= PHRASE_ANYTHING)||LA26_8==QMARK||LA26_8==TERM_NORMAL||LA26_8==TERM_TRUNCATED||LA26_8==TO||LA26_8==70) ) {
                            alt26=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 26, 8, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA26_5==EOF||LA26_5==AND||LA26_5==AUTHOR_SEARCH||LA26_5==CARAT||(LA26_5 >= COMMA && LA26_5 <= DATE_RANGE)||LA26_5==D_NUMBER||LA26_5==FUNC_NAME||(LA26_5 >= HOUR && LA26_5 <= IDENTIFIER)||(LA26_5 >= LBRACK && LA26_5 <= MINUS)||(LA26_5 >= NEAR && LA26_5 <= NUMBER)||(LA26_5 >= OR && LA26_5 <= PLUS)||LA26_5==QMARK||(LA26_5 >= RPAREN && LA26_5 <= SEMICOLON)||LA26_5==STAR||LA26_5==TERM_NORMAL||(LA26_5 >= TERM_TRUNCATED && LA26_5 <= TILDE)||LA26_5==TO||LA26_5==68||(LA26_5 >= 70 && LA26_5 <= 71)) ) {
                        alt26=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 26, 5, input);

                        throw nvae;

                    }
                    }
                    break;
                case AUTHOR_SEARCH:
                case DATE_RANGE:
                case D_NUMBER:
                case HOUR:
                case H_NUMBER:
                case IDENTIFIER:
                case LBRACK:
                case NUMBER:
                case PHRASE:
                case PHRASE_ANYTHING:
                case QMARK:
                case TERM_TRUNCATED:
                case TO:
                case 70:
                    {
                    alt26=2;
                    }
                    break;
                case STAR:
                    {
                    alt26=3;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 26, 4, input);

                    throw nvae;

                }

                }
                break;
            case TERM_NORMAL:
                {
                int LA26_5 = input.LA(2);

                if ( (LA26_5==COLON) ) {
                    int LA26_8 = input.LA(3);

                    if ( (LA26_8==LPAREN) ) {
                        alt26=1;
                    }
                    else if ( (LA26_8==AUTHOR_SEARCH||LA26_8==DATE_RANGE||LA26_8==D_NUMBER||(LA26_8 >= HOUR && LA26_8 <= IDENTIFIER)||LA26_8==LBRACK||LA26_8==NUMBER||(LA26_8 >= PHRASE && LA26_8 <= PHRASE_ANYTHING)||LA26_8==QMARK||LA26_8==TERM_NORMAL||LA26_8==TERM_TRUNCATED||LA26_8==TO||LA26_8==70) ) {
                        alt26=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 26, 8, input);

                        throw nvae;

                    }
                }
                else if ( (LA26_5==EOF||LA26_5==AND||LA26_5==AUTHOR_SEARCH||LA26_5==CARAT||(LA26_5 >= COMMA && LA26_5 <= DATE_RANGE)||LA26_5==D_NUMBER||LA26_5==FUNC_NAME||(LA26_5 >= HOUR && LA26_5 <= IDENTIFIER)||(LA26_5 >= LBRACK && LA26_5 <= MINUS)||(LA26_5 >= NEAR && LA26_5 <= NUMBER)||(LA26_5 >= OR && LA26_5 <= PLUS)||LA26_5==QMARK||(LA26_5 >= RPAREN && LA26_5 <= SEMICOLON)||LA26_5==STAR||LA26_5==TERM_NORMAL||(LA26_5 >= TERM_TRUNCATED && LA26_5 <= TILDE)||LA26_5==TO||LA26_5==68||(LA26_5 >= 70 && LA26_5 <= 71)) ) {
                    alt26=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 26, 5, input);

                    throw nvae;

                }
                }
                break;
            case AUTHOR_SEARCH:
            case DATE_RANGE:
            case D_NUMBER:
            case HOUR:
            case H_NUMBER:
            case IDENTIFIER:
            case LBRACK:
            case NUMBER:
            case PHRASE:
            case PHRASE_ANYTHING:
            case QMARK:
            case TERM_TRUNCATED:
            case TO:
            case 70:
                {
                alt26=2;
                }
                break;
            case STAR:
                {
                alt26=3;
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:87:2: ( lmodifier )? field multi_value ( rmodifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:87:2: ( lmodifier )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==MINUS||LA19_0==PLUS||LA19_0==68||LA19_0==71) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:87:2: lmodifier
                            {
                            pushFollow(FOLLOW_lmodifier_in_atom691);
                            lmodifier27=lmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier27.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_field_in_atom694);
                    field28=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field28.getTree());

                    pushFollow(FOLLOW_multi_value_in_atom696);
                    multi_value29=multi_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_multi_value.add(multi_value29.getTree());

                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:87:31: ( rmodifier )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==CARAT||LA20_0==TILDE) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:87:31: rmodifier
                            {
                            pushFollow(FOLLOW_rmodifier_in_atom698);
                            rmodifier30=rmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier30.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: lmodifier, rmodifier, multi_value, field
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 88:3: -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:88:6: ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:88:15: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:88:26: ( lmodifier )?
                        if ( stream_lmodifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_lmodifier.nextTree());

                        }
                        stream_lmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:88:37: ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:88:49: ( rmodifier )?
                        if ( stream_rmodifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_rmodifier.nextTree());

                        }
                        stream_rmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:88:60: ^( FIELD field multi_value )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:90:2: ( lmodifier )? ( field )? value ( rmodifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:90:2: ( lmodifier )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==MINUS||LA21_0==PLUS||LA21_0==68||LA21_0==71) ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:90:2: lmodifier
                            {
                            pushFollow(FOLLOW_lmodifier_in_atom736);
                            lmodifier31=lmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier31.getTree());

                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:90:13: ( field )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==TERM_NORMAL) ) {
                        int LA22_1 = input.LA(2);

                        if ( (LA22_1==COLON) ) {
                            alt22=1;
                        }
                    }
                    switch (alt22) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:90:13: field
                            {
                            pushFollow(FOLLOW_field_in_atom739);
                            field32=field();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_field.add(field32.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_value_in_atom742);
                    value33=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value33.getTree());

                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:90:26: ( rmodifier )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==CARAT||LA23_0==TILDE) ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:90:26: rmodifier
                            {
                            pushFollow(FOLLOW_rmodifier_in_atom744);
                            rmodifier34=rmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier34.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: field, lmodifier, value, rmodifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 91:2: -> ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:91:5: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:91:16: ( lmodifier )?
                        if ( stream_lmodifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_lmodifier.nextTree());

                        }
                        stream_lmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:91:27: ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:91:39: ( rmodifier )?
                        if ( stream_rmodifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_rmodifier.nextTree());

                        }
                        stream_rmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:91:50: ^( FIELD ( field )? value )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:91:58: ( field )?
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:92:4: ( lmodifier )? ( STAR COLON )? STAR
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:92:4: ( lmodifier )?
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==MINUS||LA24_0==PLUS||LA24_0==68||LA24_0==71) ) {
                        alt24=1;
                    }
                    switch (alt24) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:92:4: lmodifier
                            {
                            pushFollow(FOLLOW_lmodifier_in_atom777);
                            lmodifier35=lmodifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier35.getTree());

                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:92:15: ( STAR COLON )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==STAR) ) {
                        int LA25_1 = input.LA(2);

                        if ( (LA25_1==COLON) ) {
                            alt25=1;
                        }
                    }
                    switch (alt25) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:92:16: STAR COLON
                            {
                            STAR36=(Token)match(input,STAR,FOLLOW_STAR_in_atom781); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(STAR36);


                            COLON37=(Token)match(input,COLON,FOLLOW_COLON_in_atom783); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COLON.add(COLON37);


                            }
                            break;

                    }


                    STAR38=(Token)match(input,STAR,FOLLOW_STAR_in_atom787); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR38);


                    // AST REWRITE
                    // elements: lmodifier, STAR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 93:2: -> ^( MODIFIER ( lmodifier )? ^( QANYTHING STAR[\"*\"] ) )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:93:5: ^( MODIFIER ( lmodifier )? ^( QANYTHING STAR[\"*\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:93:16: ( lmodifier )?
                        if ( stream_lmodifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_lmodifier.nextTree());

                        }
                        stream_lmodifier.reset();

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:93:27: ^( QANYTHING STAR[\"*\"] )
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:98:1: field : TERM_NORMAL COLON -> TERM_NORMAL ;
    public final ADSParser.field_return field() throws RecognitionException {
        ADSParser.field_return retval = new ADSParser.field_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_NORMAL39=null;
        Token COLON40=null;

        Object TERM_NORMAL39_tree=null;
        Object COLON40_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_TERM_NORMAL=new RewriteRuleTokenStream(adaptor,"token TERM_NORMAL");

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:99:2: ( TERM_NORMAL COLON -> TERM_NORMAL )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:100:2: TERM_NORMAL COLON
            {
            TERM_NORMAL39=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_field825); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL39);


            COLON40=(Token)match(input,COLON,FOLLOW_COLON_in_field827); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(COLON40);


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
            // 100:20: -> TERM_NORMAL
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:103:1: value : ( range_term_in -> ^( QRANGEIN range_term_in ) | identifier -> ^( QIDENTIFIER identifier ) | coordinate -> ^( QCOORDINATE coordinate ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | DATE_RANGE -> ^( QDATE DATE_RANGE ) | AUTHOR_SEARCH -> ^( QPOSITION AUTHOR_SEARCH ) | QMARK -> ^( QTRUNCATED QMARK ) );
    public final ADSParser.value_return value() throws RecognitionException {
        ADSParser.value_return retval = new ADSParser.value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token DATE_RANGE48=null;
        Token AUTHOR_SEARCH49=null;
        Token QMARK50=null;
        ADSParser.range_term_in_return range_term_in41 =null;

        ADSParser.identifier_return identifier42 =null;

        ADSParser.coordinate_return coordinate43 =null;

        ADSParser.normal_return normal44 =null;

        ADSParser.truncated_return truncated45 =null;

        ADSParser.quoted_return quoted46 =null;

        ADSParser.quoted_truncated_return quoted_truncated47 =null;


        Object DATE_RANGE48_tree=null;
        Object AUTHOR_SEARCH49_tree=null;
        Object QMARK50_tree=null;
        RewriteRuleTokenStream stream_AUTHOR_SEARCH=new RewriteRuleTokenStream(adaptor,"token AUTHOR_SEARCH");
        RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
        RewriteRuleTokenStream stream_DATE_RANGE=new RewriteRuleTokenStream(adaptor,"token DATE_RANGE");
        RewriteRuleSubtreeStream stream_coordinate=new RewriteRuleSubtreeStream(adaptor,"rule coordinate");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_range_term_in=new RewriteRuleSubtreeStream(adaptor,"rule range_term_in");
        RewriteRuleSubtreeStream stream_identifier=new RewriteRuleSubtreeStream(adaptor,"rule identifier");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:104:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | identifier -> ^( QIDENTIFIER identifier ) | coordinate -> ^( QCOORDINATE coordinate ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | DATE_RANGE -> ^( QDATE DATE_RANGE ) | AUTHOR_SEARCH -> ^( QPOSITION AUTHOR_SEARCH ) | QMARK -> ^( QTRUNCATED QMARK ) )
            int alt27=10;
            switch ( input.LA(1) ) {
            case LBRACK:
                {
                alt27=1;
                }
                break;
            case IDENTIFIER:
                {
                alt27=2;
                }
                break;
            case D_NUMBER:
            case HOUR:
            case H_NUMBER:
            case 70:
                {
                alt27=3;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
            case TO:
                {
                alt27=4;
                }
                break;
            case TERM_TRUNCATED:
                {
                alt27=5;
                }
                break;
            case PHRASE:
                {
                alt27=6;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt27=7;
                }
                break;
            case DATE_RANGE:
                {
                alt27=8;
                }
                break;
            case AUTHOR_SEARCH:
                {
                alt27=9;
                }
                break;
            case QMARK:
                {
                alt27=10;
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:105:2: range_term_in
                    {
                    pushFollow(FOLLOW_range_term_in_in_value846);
                    range_term_in41=range_term_in();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_in.add(range_term_in41.getTree());

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
                    // 105:16: -> ^( QRANGEIN range_term_in )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:105:19: ^( QRANGEIN range_term_in )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:107:4: identifier
                    {
                    pushFollow(FOLLOW_identifier_in_value860);
                    identifier42=identifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_identifier.add(identifier42.getTree());

                    // AST REWRITE
                    // elements: identifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 107:15: -> ^( QIDENTIFIER identifier )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:107:18: ^( QIDENTIFIER identifier )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QIDENTIFIER, "QIDENTIFIER")
                        , root_1);

                        adaptor.addChild(root_1, stream_identifier.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:108:4: coordinate
                    {
                    pushFollow(FOLLOW_coordinate_in_value873);
                    coordinate43=coordinate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_coordinate.add(coordinate43.getTree());

                    // AST REWRITE
                    // elements: coordinate
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 108:15: -> ^( QCOORDINATE coordinate )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:108:18: ^( QCOORDINATE coordinate )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QCOORDINATE, "QCOORDINATE")
                        , root_1);

                        adaptor.addChild(root_1, stream_coordinate.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:109:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_value886);
                    normal44=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal44.getTree());

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
                    // 109:11: -> ^( QNORMAL normal )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:109:14: ^( QNORMAL normal )
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
                case 5 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:110:4: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_value900);
                    truncated45=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated45.getTree());

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
                    // 110:14: -> ^( QTRUNCATED truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:110:17: ^( QTRUNCATED truncated )
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
                case 6 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:111:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_value914);
                    quoted46=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted46.getTree());

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
                    // 111:11: -> ^( QPHRASE quoted )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:111:14: ^( QPHRASE quoted )
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
                case 7 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:112:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_value927);
                    quoted_truncated47=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated47.getTree());

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
                    // 112:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:112:24: ^( QPHRASETRUNC quoted_truncated )
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
                case 8 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:113:4: DATE_RANGE
                    {
                    DATE_RANGE48=(Token)match(input,DATE_RANGE,FOLLOW_DATE_RANGE_in_value940); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DATE_RANGE.add(DATE_RANGE48);


                    // AST REWRITE
                    // elements: DATE_RANGE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 113:15: -> ^( QDATE DATE_RANGE )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:113:18: ^( QDATE DATE_RANGE )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QDATE, "QDATE")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_DATE_RANGE.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 9 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:114:4: AUTHOR_SEARCH
                    {
                    AUTHOR_SEARCH49=(Token)match(input,AUTHOR_SEARCH,FOLLOW_AUTHOR_SEARCH_in_value953); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AUTHOR_SEARCH.add(AUTHOR_SEARCH49);


                    // AST REWRITE
                    // elements: AUTHOR_SEARCH
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 114:18: -> ^( QPOSITION AUTHOR_SEARCH )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:114:21: ^( QPOSITION AUTHOR_SEARCH )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QPOSITION, "QPOSITION")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_AUTHOR_SEARCH.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 10 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:115:4: QMARK
                    {
                    QMARK50=(Token)match(input,QMARK,FOLLOW_QMARK_in_value966); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QMARK.add(QMARK50);


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
                    // 115:10: -> ^( QTRUNCATED QMARK )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:115:13: ^( QTRUNCATED QMARK )
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:121:1: range_term_in options {greedy=true; } : LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK ;
    public final ADSParser.range_term_in_return range_term_in() throws RecognitionException {
        ADSParser.range_term_in_return retval = new ADSParser.range_term_in_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LBRACK51=null;
        Token TO52=null;
        Token RBRACK53=null;
        ADSParser.range_value_return a =null;

        ADSParser.range_value_return b =null;


        Object LBRACK51_tree=null;
        Object TO52_tree=null;
        Object RBRACK53_tree=null;
        RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
        RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:123:2: ( LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:124:8: LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK
            {
            LBRACK51=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_range_term_in1015); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK51);


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:125:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:125:9: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_in1027);
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
            // 125:23: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:125:38: ^( QANYTHING QANYTHING[\"*\"] )
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


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:126:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==DATE_TOKEN||LA29_0==NUMBER||(LA29_0 >= PHRASE && LA29_0 <= PHRASE_ANYTHING)||LA29_0==STAR||LA29_0==TERM_NORMAL||LA29_0==TERM_TRUNCATED||LA29_0==TO) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:126:9: ( TO )? b= range_value
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:126:9: ( TO )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==TO) ) {
                        int LA28_1 = input.LA(2);

                        if ( (LA28_1==DATE_TOKEN||LA28_1==NUMBER||(LA28_1 >= PHRASE && LA28_1 <= PHRASE_ANYTHING)||LA28_1==STAR||LA28_1==TERM_NORMAL||LA28_1==TERM_TRUNCATED||LA28_1==TO) ) {
                            alt28=1;
                        }
                    }
                    switch (alt28) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:126:9: TO
                            {
                            TO52=(Token)match(input,TO,FOLLOW_TO_in_range_term_in1049); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO52);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_in1055);
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
                    // 126:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:126:35: ( $b)?
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


            RBRACK53=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_range_term_in1076); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACK.add(RBRACK53);


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


    public static class range_value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "range_value"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:142:1: range_value : ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) );
    public final ADSParser.range_value_return range_value() throws RecognitionException {
        ADSParser.range_value_return retval = new ADSParser.range_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR59=null;
        ADSParser.truncated_return truncated54 =null;

        ADSParser.quoted_return quoted55 =null;

        ADSParser.quoted_truncated_return quoted_truncated56 =null;

        ADSParser.date_return date57 =null;

        ADSParser.normal_return normal58 =null;


        Object STAR59_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_date=new RewriteRuleSubtreeStream(adaptor,"rule date");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:143:2: ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) )
            int alt30=6;
            switch ( input.LA(1) ) {
            case TERM_TRUNCATED:
                {
                alt30=1;
                }
                break;
            case PHRASE:
                {
                alt30=2;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt30=3;
                }
                break;
            case DATE_TOKEN:
                {
                alt30=4;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
            case TO:
                {
                alt30=5;
                }
                break;
            case STAR:
                {
                alt30=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;

            }

            switch (alt30) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:144:2: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_range_value1092);
                    truncated54=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated54.getTree());

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
                    // 144:12: -> ^( QTRUNCATED truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:144:15: ^( QTRUNCATED truncated )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:145:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_range_value1105);
                    quoted55=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted55.getTree());

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
                    // 145:11: -> ^( QPHRASE quoted )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:145:14: ^( QPHRASE quoted )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:146:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_range_value1118);
                    quoted_truncated56=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated56.getTree());

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
                    // 146:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:146:24: ^( QPHRASETRUNC quoted_truncated )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:147:4: date
                    {
                    pushFollow(FOLLOW_date_in_range_value1131);
                    date57=date();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_date.add(date57.getTree());

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
                    // 147:9: -> ^( QNORMAL date )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:147:12: ^( QNORMAL date )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:148:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_range_value1144);
                    normal58=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal58.getTree());

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
                    // 148:11: -> ^( QNORMAL normal )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:148:14: ^( QNORMAL normal )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:149:4: STAR
                    {
                    STAR59=(Token)match(input,STAR,FOLLOW_STAR_in_range_value1158); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR59);


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
                    // 149:9: -> ^( QANYTHING STAR )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:149:12: ^( QANYTHING STAR )
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


    public static class func_name_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "func_name"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:152:1: func_name : FUNC_NAME ;
    public final ADSParser.func_name_return func_name() throws RecognitionException {
        ADSParser.func_name_return retval = new ADSParser.func_name_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token FUNC_NAME60=null;

        Object FUNC_NAME60_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:153:2: ( FUNC_NAME )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:154:2: FUNC_NAME
            {
            root_0 = (Object)adaptor.nil();


            FUNC_NAME60=(Token)match(input,FUNC_NAME,FOLLOW_FUNC_NAME_in_func_name1179); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            FUNC_NAME60_tree = 
            (Object)adaptor.create(FUNC_NAME60)
            ;
            adaptor.addChild(root_0, FUNC_NAME60_tree);
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
    // $ANTLR end "func_name"


    public static class multi_value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multi_value"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:159:1: multi_value : LPAREN multiClause RPAREN -> multiClause ;
    public final ADSParser.multi_value_return multi_value() throws RecognitionException {
        ADSParser.multi_value_return retval = new ADSParser.multi_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN61=null;
        Token RPAREN63=null;
        ADSParser.multiClause_return multiClause62 =null;


        Object LPAREN61_tree=null;
        Object RPAREN63_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_multiClause=new RewriteRuleSubtreeStream(adaptor,"rule multiClause");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:160:2: ( LPAREN multiClause RPAREN -> multiClause )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:161:2: LPAREN multiClause RPAREN
            {
            LPAREN61=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_multi_value1194); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN61);


            pushFollow(FOLLOW_multiClause_in_multi_value1196);
            multiClause62=multiClause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_multiClause.add(multiClause62.getTree());

            RPAREN63=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_multi_value1198); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN63);


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
            // 161:28: -> multiClause
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:166:1: multiClause : ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
    public final ADSParser.multiClause_return multiClause() throws RecognitionException {
        ADSParser.multiClause_return retval = new ADSParser.multiClause_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ADSParser.clauseOr_return clauseOr64 =null;


        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:167:2: ( ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:172:2: ( clauseOr )+
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:172:2: ( clauseOr )+
            int cnt31=0;
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==AUTHOR_SEARCH||LA31_0==DATE_RANGE||LA31_0==D_NUMBER||LA31_0==FUNC_NAME||(LA31_0 >= HOUR && LA31_0 <= IDENTIFIER)||(LA31_0 >= LBRACK && LA31_0 <= MINUS)||LA31_0==NUMBER||(LA31_0 >= PHRASE && LA31_0 <= PLUS)||LA31_0==QMARK||LA31_0==STAR||LA31_0==TERM_NORMAL||LA31_0==TERM_TRUNCATED||LA31_0==TO||LA31_0==68||(LA31_0 >= 70 && LA31_0 <= 71)) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:172:2: clauseOr
            	    {
            	    pushFollow(FOLLOW_clauseOr_in_multiClause1225);
            	    clauseOr64=clauseOr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr64.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt31 >= 1 ) break loop31;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(31, input);
                        throw eee;
                }
                cnt31++;
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
            // 172:12: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
            {
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:172:15: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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


    public static class normal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "normal"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:233:1: normal : ( TERM_NORMAL | NUMBER | TO );
    public final ADSParser.normal_return normal() throws RecognitionException {
        ADSParser.normal_return retval = new ADSParser.normal_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set65=null;

        Object set65_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:234:2: ( TERM_NORMAL | NUMBER | TO )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
            {
            root_0 = (Object)adaptor.nil();


            set65=(Token)input.LT(1);

            if ( input.LA(1)==NUMBER||input.LA(1)==TERM_NORMAL||input.LA(1)==TO ) {
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:243:1: truncated : TERM_TRUNCATED ;
    public final ADSParser.truncated_return truncated() throws RecognitionException {
        ADSParser.truncated_return retval = new ADSParser.truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_TRUNCATED66=null;

        Object TERM_TRUNCATED66_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:244:2: ( TERM_TRUNCATED )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:245:2: TERM_TRUNCATED
            {
            root_0 = (Object)adaptor.nil();


            TERM_TRUNCATED66=(Token)match(input,TERM_TRUNCATED,FOLLOW_TERM_TRUNCATED_in_truncated1304); if (state.failed) return retval;
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:249:1: quoted_truncated : PHRASE_ANYTHING ;
    public final ADSParser.quoted_truncated_return quoted_truncated() throws RecognitionException {
        ADSParser.quoted_truncated_return retval = new ADSParser.quoted_truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE_ANYTHING67=null;

        Object PHRASE_ANYTHING67_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:250:2: ( PHRASE_ANYTHING )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:251:2: PHRASE_ANYTHING
            {
            root_0 = (Object)adaptor.nil();


            PHRASE_ANYTHING67=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1319); if (state.failed) return retval;
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:254:1: quoted : PHRASE ;
    public final ADSParser.quoted_return quoted() throws RecognitionException {
        ADSParser.quoted_return retval = new ADSParser.quoted_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE68=null;

        Object PHRASE68_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:254:8: ( PHRASE )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:255:2: PHRASE
            {
            root_0 = (Object)adaptor.nil();


            PHRASE68=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_quoted1331); if (state.failed) return retval;
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


    public static class lmodifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "lmodifier"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:259:1: lmodifier : ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] | '=' -> EQUAL[\"=\"] | '#' -> HASH[\"#\"] );
    public final ADSParser.lmodifier_return lmodifier() throws RecognitionException {
        ADSParser.lmodifier_return retval = new ADSParser.lmodifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PLUS69=null;
        Token MINUS70=null;
        Token char_literal71=null;
        Token char_literal72=null;

        Object PLUS69_tree=null;
        Object MINUS70_tree=null;
        Object char_literal71_tree=null;
        Object char_literal72_tree=null;
        RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
        RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
        RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");
        RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:259:10: ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] | '=' -> EQUAL[\"=\"] | '#' -> HASH[\"#\"] )
            int alt32=4;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                alt32=1;
                }
                break;
            case MINUS:
                {
                alt32=2;
                }
                break;
            case 71:
                {
                alt32=3;
                }
                break;
            case 68:
                {
                alt32=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;

            }

            switch (alt32) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:260:2: PLUS
                    {
                    PLUS69=(Token)match(input,PLUS,FOLLOW_PLUS_in_lmodifier1343); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_PLUS.add(PLUS69);


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
                    // 260:7: -> PLUS[\"+\"]
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:261:4: MINUS
                    {
                    MINUS70=(Token)match(input,MINUS,FOLLOW_MINUS_in_lmodifier1353); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_MINUS.add(MINUS70);


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
                    // 261:10: -> MINUS[\"-\"]
                    {
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(MINUS, "-")
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:262:4: '='
                    {
                    char_literal71=(Token)match(input,71,FOLLOW_71_in_lmodifier1363); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_71.add(char_literal71);


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
                    // 262:8: -> EQUAL[\"=\"]
                    {
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(EQUAL, "=")
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:263:4: '#'
                    {
                    char_literal72=(Token)match(input,68,FOLLOW_68_in_lmodifier1373); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_68.add(char_literal72);


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
                    // 263:8: -> HASH[\"#\"]
                    {
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(HASH, "#")
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
    // $ANTLR end "lmodifier"


    public static class rmodifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "rmodifier"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:268:1: rmodifier : ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) );
    public final ADSParser.rmodifier_return rmodifier() throws RecognitionException {
        ADSParser.rmodifier_return retval = new ADSParser.rmodifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE73=null;
        Token CARAT74=null;
        Token CARAT75=null;
        Token TILDE76=null;

        Object TILDE73_tree=null;
        Object CARAT74_tree=null;
        Object CARAT75_tree=null;
        Object TILDE76_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:268:11: ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) )
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==TILDE) ) {
                alt35=1;
            }
            else if ( (LA35_0==CARAT) ) {
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:269:2: TILDE ( CARAT )?
                    {
                    TILDE73=(Token)match(input,TILDE,FOLLOW_TILDE_in_rmodifier1392); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TILDE.add(TILDE73);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:269:8: ( CARAT )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==CARAT) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:269:8: CARAT
                            {
                            CARAT74=(Token)match(input,CARAT,FOLLOW_CARAT_in_rmodifier1394); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CARAT.add(CARAT74);


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
                    // 269:15: -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:269:18: ^( BOOST ( CARAT )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:269:26: ( CARAT )?
                        if ( stream_CARAT.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_CARAT.nextNode()
                            );

                        }
                        stream_CARAT.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:269:34: ^( FUZZY TILDE )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:270:4: CARAT ( TILDE )?
                    {
                    CARAT75=(Token)match(input,CARAT,FOLLOW_CARAT_in_rmodifier1416); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CARAT.add(CARAT75);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:270:10: ( TILDE )?
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==TILDE) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:270:10: TILDE
                            {
                            TILDE76=(Token)match(input,TILDE,FOLLOW_TILDE_in_rmodifier1418); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TILDE.add(TILDE76);


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
                    // 270:17: -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:270:20: ^( BOOST CARAT )
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

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:270:35: ^( FUZZY ( TILDE )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FUZZY, "FUZZY")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:270:43: ( TILDE )?
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
    // $ANTLR end "rmodifier"


    public static class boost_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "boost"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:274:1: boost : ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? ;
    public final ADSParser.boost_return boost() throws RecognitionException {
        ADSParser.boost_return retval = new ADSParser.boost_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token CARAT77=null;
        Token NUMBER78=null;

        Object CARAT77_tree=null;
        Object NUMBER78_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:274:7: ( ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:275:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )?
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:275:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:275:3: CARAT
            {
            CARAT77=(Token)match(input,CARAT,FOLLOW_CARAT_in_boost1447); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CARAT.add(CARAT77);


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
            // 275:9: -> ^( BOOST NUMBER[\"DEF\"] )
            {
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:275:12: ^( BOOST NUMBER[\"DEF\"] )
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


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:276:2: ( NUMBER -> ^( BOOST NUMBER ) )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==NUMBER) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:276:3: NUMBER
                    {
                    NUMBER78=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_boost1462); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER78);


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
                    // 276:10: -> ^( BOOST NUMBER )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:276:13: ^( BOOST NUMBER )
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:279:1: fuzzy : ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? ;
    public final ADSParser.fuzzy_return fuzzy() throws RecognitionException {
        ADSParser.fuzzy_return retval = new ADSParser.fuzzy_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE79=null;
        Token NUMBER80=null;

        Object TILDE79_tree=null;
        Object NUMBER80_tree=null;
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:279:7: ( ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:280:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )?
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:280:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:280:3: TILDE
            {
            TILDE79=(Token)match(input,TILDE,FOLLOW_TILDE_in_fuzzy1485); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TILDE.add(TILDE79);


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
            // 280:9: -> ^( FUZZY NUMBER[\"DEF\"] )
            {
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:280:12: ^( FUZZY NUMBER[\"DEF\"] )
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


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:281:2: ( NUMBER -> ^( FUZZY NUMBER ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==NUMBER) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:281:3: NUMBER
                    {
                    NUMBER80=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_fuzzy1500); if (state.failed) return retval; 
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
                    // 281:10: -> ^( FUZZY NUMBER )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:281:13: ^( FUZZY NUMBER )
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:284:1: not : ( ( AND NOT )=> AND NOT | NOT );
    public final ADSParser.not_return not() throws RecognitionException {
        ADSParser.not_return retval = new ADSParser.not_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND81=null;
        Token NOT82=null;
        Token NOT83=null;

        Object AND81_tree=null;
        Object NOT82_tree=null;
        Object NOT83_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:284:5: ( ( AND NOT )=> AND NOT | NOT )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==AND) && (synpred5_ADS())) {
                alt38=1;
            }
            else if ( (LA38_0==NOT) ) {
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:285:2: ( AND NOT )=> AND NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    AND81=(Token)match(input,AND,FOLLOW_AND_in_not1530); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    AND81_tree = 
                    (Object)adaptor.create(AND81)
                    ;
                    adaptor.addChild(root_0, AND81_tree);
                    }

                    NOT82=(Token)match(input,NOT,FOLLOW_NOT_in_not1532); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT82_tree = 
                    (Object)adaptor.create(NOT82)
                    ;
                    adaptor.addChild(root_0, NOT82_tree);
                    }

                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:286:4: NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    NOT83=(Token)match(input,NOT,FOLLOW_NOT_in_not1537); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT83_tree = 
                    (Object)adaptor.create(NOT83)
                    ;
                    adaptor.addChild(root_0, NOT83_tree);
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:289:1: and : AND ;
    public final ADSParser.and_return and() throws RecognitionException {
        ADSParser.and_return retval = new ADSParser.and_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND84=null;

        Object AND84_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:289:6: ( AND )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:290:2: AND
            {
            root_0 = (Object)adaptor.nil();


            AND84=(Token)match(input,AND,FOLLOW_AND_in_and1551); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            AND84_tree = 
            (Object)adaptor.create(AND84)
            ;
            adaptor.addChild(root_0, AND84_tree);
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:293:1: or : OR ;
    public final ADSParser.or_return or() throws RecognitionException {
        ADSParser.or_return retval = new ADSParser.or_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OR85=null;

        Object OR85_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:293:5: ( OR )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:294:2: OR
            {
            root_0 = (Object)adaptor.nil();


            OR85=(Token)match(input,OR,FOLLOW_OR_in_or1565); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            OR85_tree = 
            (Object)adaptor.create(OR85)
            ;
            adaptor.addChild(root_0, OR85_tree);
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
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:297:1: near : ( NEAR -> ^( OPERATOR[\"NEAR\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )? ;
    public final ADSParser.near_return near() throws RecognitionException {
        ADSParser.near_return retval = new ADSParser.near_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token b=null;
        Token NEAR86=null;
        Token char_literal87=null;

        Object b_tree=null;
        Object NEAR86_tree=null;
        Object char_literal87_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");
        RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:297:6: ( ( NEAR -> ^( OPERATOR[\"NEAR\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:298:2: ( NEAR -> ^( OPERATOR[\"NEAR\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )?
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:298:2: ( NEAR -> ^( OPERATOR[\"NEAR\"] ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:298:3: NEAR
            {
            NEAR86=(Token)match(input,NEAR,FOLLOW_NEAR_in_near1580); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NEAR.add(NEAR86);


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
            // 298:8: -> ^( OPERATOR[\"NEAR\"] )
            {
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:298:11: ^( OPERATOR[\"NEAR\"] )
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


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:299:2: ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==69) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:299:3: '/' b= NUMBER
                    {
                    char_literal87=(Token)match(input,69,FOLLOW_69_in_near1593); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_69.add(char_literal87);


                    b=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_near1597); if (state.failed) return retval; 
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
                    // 299:16: -> ^( OPERATOR[\"NEAR:\" + $b.getText()] )
                    {
                        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:299:19: ^( OPERATOR[\"NEAR:\" + $b.getText()] )
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


    public static class comma_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "comma"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:302:1: comma : ( COMMA )+ ;
    public final ADSParser.comma_return comma() throws RecognitionException {
        ADSParser.comma_return retval = new ADSParser.comma_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token COMMA88=null;

        Object COMMA88_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:302:7: ( ( COMMA )+ )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:303:2: ( COMMA )+
            {
            root_0 = (Object)adaptor.nil();


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:303:2: ( COMMA )+
            int cnt40=0;
            loop40:
            do {
                int alt40=2;
                int LA40_0 = input.LA(1);

                if ( (LA40_0==COMMA) ) {
                    alt40=1;
                }


                switch (alt40) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:303:2: COMMA
            	    {
            	    COMMA88=(Token)match(input,COMMA,FOLLOW_COMMA_in_comma1619); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    COMMA88_tree = 
            	    (Object)adaptor.create(COMMA88)
            	    ;
            	    adaptor.addChild(root_0, COMMA88_tree);
            	    }

            	    }
            	    break;

            	default :
            	    if ( cnt40 >= 1 ) break loop40;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(40, input);
                        throw eee;
                }
                cnt40++;
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
    // $ANTLR end "comma"


    public static class semicolon_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "semicolon"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:306:1: semicolon : ( SEMICOLON )+ ;
    public final ADSParser.semicolon_return semicolon() throws RecognitionException {
        ADSParser.semicolon_return retval = new ADSParser.semicolon_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token SEMICOLON89=null;

        Object SEMICOLON89_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:307:2: ( ( SEMICOLON )+ )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:308:2: ( SEMICOLON )+
            {
            root_0 = (Object)adaptor.nil();


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:308:2: ( SEMICOLON )+
            int cnt41=0;
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==SEMICOLON) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:308:2: SEMICOLON
            	    {
            	    SEMICOLON89=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_semicolon1633); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SEMICOLON89_tree = 
            	    (Object)adaptor.create(SEMICOLON89)
            	    ;
            	    adaptor.addChild(root_0, SEMICOLON89_tree);
            	    }

            	    }
            	    break;

            	default :
            	    if ( cnt41 >= 1 ) break loop41;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(41, input);
                        throw eee;
                }
                cnt41++;
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
    // $ANTLR end "semicolon"


    public static class date_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "date"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:311:1: date : DATE_TOKEN ;
    public final ADSParser.date_return date() throws RecognitionException {
        ADSParser.date_return retval = new ADSParser.date_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token DATE_TOKEN90=null;

        Object DATE_TOKEN90_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:311:6: ( DATE_TOKEN )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:313:2: DATE_TOKEN
            {
            root_0 = (Object)adaptor.nil();


            DATE_TOKEN90=(Token)match(input,DATE_TOKEN,FOLLOW_DATE_TOKEN_in_date1648); if (state.failed) return retval;
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


    public static class identifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "identifier"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:316:1: identifier : IDENTIFIER ;
    public final ADSParser.identifier_return identifier() throws RecognitionException {
        ADSParser.identifier_return retval = new ADSParser.identifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token IDENTIFIER91=null;

        Object IDENTIFIER91_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:317:2: ( IDENTIFIER )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:318:2: IDENTIFIER
            {
            root_0 = (Object)adaptor.nil();


            IDENTIFIER91=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_identifier1662); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER91_tree = 
            (Object)adaptor.create(IDENTIFIER91)
            ;
            adaptor.addChild(root_0, IDENTIFIER91_tree);
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
    // $ANTLR end "identifier"


    public static class coordinate_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "coordinate"
    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:321:1: coordinate : ( HOUR | H_NUMBER M_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER | H_NUMBER NUMBER ( PLUS | MINUS ) D_NUMBER NUMBER | D_NUMBER M_NUMBER S_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER S_NUMBER | H_NUMBER ( PLUS | MINUS ) D_NUMBER | '<=>' );
    public final ADSParser.coordinate_return coordinate() throws RecognitionException {
        ADSParser.coordinate_return retval = new ADSParser.coordinate_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token HOUR92=null;
        Token H_NUMBER93=null;
        Token M_NUMBER94=null;
        Token set95=null;
        Token D_NUMBER96=null;
        Token M_NUMBER97=null;
        Token H_NUMBER98=null;
        Token NUMBER99=null;
        Token set100=null;
        Token D_NUMBER101=null;
        Token NUMBER102=null;
        Token D_NUMBER103=null;
        Token M_NUMBER104=null;
        Token S_NUMBER105=null;
        Token set106=null;
        Token D_NUMBER107=null;
        Token M_NUMBER108=null;
        Token S_NUMBER109=null;
        Token H_NUMBER110=null;
        Token set111=null;
        Token D_NUMBER112=null;
        Token string_literal113=null;

        Object HOUR92_tree=null;
        Object H_NUMBER93_tree=null;
        Object M_NUMBER94_tree=null;
        Object set95_tree=null;
        Object D_NUMBER96_tree=null;
        Object M_NUMBER97_tree=null;
        Object H_NUMBER98_tree=null;
        Object NUMBER99_tree=null;
        Object set100_tree=null;
        Object D_NUMBER101_tree=null;
        Object NUMBER102_tree=null;
        Object D_NUMBER103_tree=null;
        Object M_NUMBER104_tree=null;
        Object S_NUMBER105_tree=null;
        Object set106_tree=null;
        Object D_NUMBER107_tree=null;
        Object M_NUMBER108_tree=null;
        Object S_NUMBER109_tree=null;
        Object H_NUMBER110_tree=null;
        Object set111_tree=null;
        Object D_NUMBER112_tree=null;
        Object string_literal113_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:322:2: ( HOUR | H_NUMBER M_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER | H_NUMBER NUMBER ( PLUS | MINUS ) D_NUMBER NUMBER | D_NUMBER M_NUMBER S_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER S_NUMBER | H_NUMBER ( PLUS | MINUS ) D_NUMBER | '<=>' )
            int alt42=6;
            switch ( input.LA(1) ) {
            case HOUR:
                {
                alt42=1;
                }
                break;
            case H_NUMBER:
                {
                switch ( input.LA(2) ) {
                case M_NUMBER:
                    {
                    alt42=2;
                    }
                    break;
                case NUMBER:
                    {
                    alt42=3;
                    }
                    break;
                case MINUS:
                case PLUS:
                    {
                    alt42=5;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 42, 2, input);

                    throw nvae;

                }

                }
                break;
            case D_NUMBER:
                {
                alt42=4;
                }
                break;
            case 70:
                {
                alt42=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;

            }

            switch (alt42) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:326:2: HOUR
                    {
                    root_0 = (Object)adaptor.nil();


                    HOUR92=(Token)match(input,HOUR,FOLLOW_HOUR_in_coordinate1681); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    HOUR92_tree = 
                    (Object)adaptor.create(HOUR92)
                    ;
                    adaptor.addChild(root_0, HOUR92_tree);
                    }

                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:328:2: H_NUMBER M_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER
                    {
                    root_0 = (Object)adaptor.nil();


                    H_NUMBER93=(Token)match(input,H_NUMBER,FOLLOW_H_NUMBER_in_coordinate1688); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    H_NUMBER93_tree = 
                    (Object)adaptor.create(H_NUMBER93)
                    ;
                    adaptor.addChild(root_0, H_NUMBER93_tree);
                    }

                    M_NUMBER94=(Token)match(input,M_NUMBER,FOLLOW_M_NUMBER_in_coordinate1690); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    M_NUMBER94_tree = 
                    (Object)adaptor.create(M_NUMBER94)
                    ;
                    adaptor.addChild(root_0, M_NUMBER94_tree);
                    }

                    set95=(Token)input.LT(1);

                    if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                        (Object)adaptor.create(set95)
                        );
                        state.errorRecovery=false;
                        state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    D_NUMBER96=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate1698); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    D_NUMBER96_tree = 
                    (Object)adaptor.create(D_NUMBER96)
                    ;
                    adaptor.addChild(root_0, D_NUMBER96_tree);
                    }

                    M_NUMBER97=(Token)match(input,M_NUMBER,FOLLOW_M_NUMBER_in_coordinate1700); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    M_NUMBER97_tree = 
                    (Object)adaptor.create(M_NUMBER97)
                    ;
                    adaptor.addChild(root_0, M_NUMBER97_tree);
                    }

                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:330:2: H_NUMBER NUMBER ( PLUS | MINUS ) D_NUMBER NUMBER
                    {
                    root_0 = (Object)adaptor.nil();


                    H_NUMBER98=(Token)match(input,H_NUMBER,FOLLOW_H_NUMBER_in_coordinate1707); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    H_NUMBER98_tree = 
                    (Object)adaptor.create(H_NUMBER98)
                    ;
                    adaptor.addChild(root_0, H_NUMBER98_tree);
                    }

                    NUMBER99=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_coordinate1709); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMBER99_tree = 
                    (Object)adaptor.create(NUMBER99)
                    ;
                    adaptor.addChild(root_0, NUMBER99_tree);
                    }

                    set100=(Token)input.LT(1);

                    if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                        (Object)adaptor.create(set100)
                        );
                        state.errorRecovery=false;
                        state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    D_NUMBER101=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate1717); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    D_NUMBER101_tree = 
                    (Object)adaptor.create(D_NUMBER101)
                    ;
                    adaptor.addChild(root_0, D_NUMBER101_tree);
                    }

                    NUMBER102=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_coordinate1719); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMBER102_tree = 
                    (Object)adaptor.create(NUMBER102)
                    ;
                    adaptor.addChild(root_0, NUMBER102_tree);
                    }

                    }
                    break;
                case 4 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:332:2: D_NUMBER M_NUMBER S_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER S_NUMBER
                    {
                    root_0 = (Object)adaptor.nil();


                    D_NUMBER103=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate1726); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    D_NUMBER103_tree = 
                    (Object)adaptor.create(D_NUMBER103)
                    ;
                    adaptor.addChild(root_0, D_NUMBER103_tree);
                    }

                    M_NUMBER104=(Token)match(input,M_NUMBER,FOLLOW_M_NUMBER_in_coordinate1728); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    M_NUMBER104_tree = 
                    (Object)adaptor.create(M_NUMBER104)
                    ;
                    adaptor.addChild(root_0, M_NUMBER104_tree);
                    }

                    S_NUMBER105=(Token)match(input,S_NUMBER,FOLLOW_S_NUMBER_in_coordinate1730); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    S_NUMBER105_tree = 
                    (Object)adaptor.create(S_NUMBER105)
                    ;
                    adaptor.addChild(root_0, S_NUMBER105_tree);
                    }

                    set106=(Token)input.LT(1);

                    if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                        (Object)adaptor.create(set106)
                        );
                        state.errorRecovery=false;
                        state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    D_NUMBER107=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate1738); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    D_NUMBER107_tree = 
                    (Object)adaptor.create(D_NUMBER107)
                    ;
                    adaptor.addChild(root_0, D_NUMBER107_tree);
                    }

                    M_NUMBER108=(Token)match(input,M_NUMBER,FOLLOW_M_NUMBER_in_coordinate1740); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    M_NUMBER108_tree = 
                    (Object)adaptor.create(M_NUMBER108)
                    ;
                    adaptor.addChild(root_0, M_NUMBER108_tree);
                    }

                    S_NUMBER109=(Token)match(input,S_NUMBER,FOLLOW_S_NUMBER_in_coordinate1742); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    S_NUMBER109_tree = 
                    (Object)adaptor.create(S_NUMBER109)
                    ;
                    adaptor.addChild(root_0, S_NUMBER109_tree);
                    }

                    }
                    break;
                case 5 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:334:2: H_NUMBER ( PLUS | MINUS ) D_NUMBER
                    {
                    root_0 = (Object)adaptor.nil();


                    H_NUMBER110=(Token)match(input,H_NUMBER,FOLLOW_H_NUMBER_in_coordinate1749); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    H_NUMBER110_tree = 
                    (Object)adaptor.create(H_NUMBER110)
                    ;
                    adaptor.addChild(root_0, H_NUMBER110_tree);
                    }

                    set111=(Token)input.LT(1);

                    if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                        (Object)adaptor.create(set111)
                        );
                        state.errorRecovery=false;
                        state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    D_NUMBER112=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate1757); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    D_NUMBER112_tree = 
                    (Object)adaptor.create(D_NUMBER112)
                    ;
                    adaptor.addChild(root_0, D_NUMBER112_tree);
                    }

                    }
                    break;
                case 6 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:336:2: '<=>'
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal113=(Token)match(input,70,FOLLOW_70_in_coordinate1764); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal113_tree = 
                    (Object)adaptor.create(string_literal113)
                    ;
                    adaptor.addChild(root_0, string_literal113_tree);
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
    // $ANTLR end "coordinate"

    // $ANTLR start synpred1_ADS
    public final void synpred1_ADS_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:3: ( ( lmodifier )? func_name )
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:4: ( lmodifier )? func_name
        {
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:4: ( lmodifier )?
        int alt43=2;
        int LA43_0 = input.LA(1);

        if ( (LA43_0==MINUS||LA43_0==PLUS||LA43_0==68||LA43_0==71) ) {
            alt43=1;
        }
        switch (alt43) {
            case 1 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:73:4: lmodifier
                {
                pushFollow(FOLLOW_lmodifier_in_synpred1_ADS465);
                lmodifier();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        pushFollow(FOLLOW_func_name_in_synpred1_ADS468);
        func_name();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred1_ADS

    // $ANTLR start synpred2_ADS
    public final void synpred2_ADS_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:4: ( lmodifier LPAREN ( clauseOr )+ RPAREN )
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:5: lmodifier LPAREN ( clauseOr )+ RPAREN
        {
        pushFollow(FOLLOW_lmodifier_in_synpred2_ADS527);
        lmodifier();

        state._fsp--;
        if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred2_ADS529); if (state.failed) return ;

        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:22: ( clauseOr )+
        int cnt44=0;
        loop44:
        do {
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==AUTHOR_SEARCH||LA44_0==DATE_RANGE||LA44_0==D_NUMBER||LA44_0==FUNC_NAME||(LA44_0 >= HOUR && LA44_0 <= IDENTIFIER)||(LA44_0 >= LBRACK && LA44_0 <= MINUS)||LA44_0==NUMBER||(LA44_0 >= PHRASE && LA44_0 <= PLUS)||LA44_0==QMARK||LA44_0==STAR||LA44_0==TERM_NORMAL||LA44_0==TERM_TRUNCATED||LA44_0==TO||LA44_0==68||(LA44_0 >= 70 && LA44_0 <= 71)) ) {
                alt44=1;
            }


            switch (alt44) {
        	case 1 :
        	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:75:22: clauseOr
        	    {
        	    pushFollow(FOLLOW_clauseOr_in_synpred2_ADS531);
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


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred2_ADS534); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_ADS

    // $ANTLR start synpred3_ADS
    public final void synpred3_ADS_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:4: ( LPAREN ( clauseOr )+ RPAREN rmodifier )
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:5: LPAREN ( clauseOr )+ RPAREN rmodifier
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred3_ADS588); if (state.failed) return ;

        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:12: ( clauseOr )+
        int cnt45=0;
        loop45:
        do {
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==AUTHOR_SEARCH||LA45_0==DATE_RANGE||LA45_0==D_NUMBER||LA45_0==FUNC_NAME||(LA45_0 >= HOUR && LA45_0 <= IDENTIFIER)||(LA45_0 >= LBRACK && LA45_0 <= MINUS)||LA45_0==NUMBER||(LA45_0 >= PHRASE && LA45_0 <= PLUS)||LA45_0==QMARK||LA45_0==STAR||LA45_0==TERM_NORMAL||LA45_0==TERM_TRUNCATED||LA45_0==TO||LA45_0==68||(LA45_0 >= 70 && LA45_0 <= 71)) ) {
                alt45=1;
            }


            switch (alt45) {
        	case 1 :
        	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:77:12: clauseOr
        	    {
        	    pushFollow(FOLLOW_clauseOr_in_synpred3_ADS590);
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


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred3_ADS593); if (state.failed) return ;

        pushFollow(FOLLOW_rmodifier_in_synpred3_ADS595);
        rmodifier();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred3_ADS

    // $ANTLR start synpred4_ADS
    public final void synpred4_ADS_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:79:4: ( LPAREN )
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:79:5: LPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred4_ADS648); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred4_ADS

    // $ANTLR start synpred5_ADS
    public final void synpred5_ADS_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:285:2: ( AND NOT )
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:285:3: AND NOT
        {
        match(input,AND,FOLLOW_AND_in_synpred5_ADS1524); if (state.failed) return ;

        match(input,NOT,FOLLOW_NOT_in_synpred5_ADS1526); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred5_ADS

    // Delegated rules

    public final boolean synpred2_ADS() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_ADS_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_ADS() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_ADS_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_ADS() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_ADS_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_ADS() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_ADS_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_ADS() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_ADS_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_clauseOr_in_mainQ168 = new BitSet(new long[]{0xA40040E43B912082L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseAnd_in_clauseOr201 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_or_in_clauseOr210 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseAnd_in_clauseOr214 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_clauseSemicolon_in_clauseAnd244 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_and_in_clauseAnd254 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseSemicolon_in_clauseAnd258 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_clauseComma_in_clauseSemicolon287 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_semicolon_in_clauseSemicolon297 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseComma_in_clauseSemicolon301 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_clauseNot_in_clauseComma330 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_comma_in_clauseComma340 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseNot_in_clauseComma344 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_clauseNear_in_clauseNot377 = new BitSet(new long[]{0x0000000200000012L});
    public static final BitSet FOLLOW_not_in_clauseNot386 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseNear_in_clauseNot390 = new BitSet(new long[]{0x0000000200000012L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseNear421 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_near_in_clauseNear430 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseNear434 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_lmodifier_in_clauseBasic473 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_func_name_in_clauseBasic476 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic478 = new BitSet(new long[]{0xA48040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic482 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000001L});
    public static final BitSet FOLLOW_rmodifier_in_clauseBasic484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lmodifier_in_clauseBasic539 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic542 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic544 = new BitSet(new long[]{0xA48040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic547 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000001L});
    public static final BitSet FOLLOW_rmodifier_in_clauseBasic549 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lmodifier_in_clauseBasic599 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic602 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic604 = new BitSet(new long[]{0xA48040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic607 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000001L});
    public static final BitSet FOLLOW_rmodifier_in_clauseBasic609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic653 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic655 = new BitSet(new long[]{0xA48040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_clauseBasic670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lmodifier_in_atom691 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_field_in_atom694 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_multi_value_in_atom696 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000001L});
    public static final BitSet FOLLOW_rmodifier_in_atom698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lmodifier_in_atom736 = new BitSet(new long[]{0xA00040640B812080L,0x0000000000000044L});
    public static final BitSet FOLLOW_field_in_atom739 = new BitSet(new long[]{0xA00040640B812080L,0x0000000000000044L});
    public static final BitSet FOLLOW_value_in_atom742 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000001L});
    public static final BitSet FOLLOW_rmodifier_in_atom744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lmodifier_in_atom777 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_STAR_in_atom781 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_COLON_in_atom783 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_STAR_in_atom787 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_NORMAL_in_field825 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_COLON_in_field827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_in_in_value846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_value860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_coordinate_in_value873 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_value886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_value900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_value914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_value927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DATE_RANGE_in_value940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AUTHOR_SEARCH_in_value953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QMARK_in_value966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACK_in_range_term_in1015 = new BitSet(new long[]{0xA400006400004000L,0x0000000000000004L});
    public static final BitSet FOLLOW_range_value_in_range_term_in1027 = new BitSet(new long[]{0xA440006400004000L,0x0000000000000004L});
    public static final BitSet FOLLOW_TO_in_range_term_in1049 = new BitSet(new long[]{0xA400006400004000L,0x0000000000000004L});
    public static final BitSet FOLLOW_range_value_in_range_term_in1055 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_RBRACK_in_range_term_in1076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_range_value1092 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_range_value1105 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_range_value1118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_in_range_value1131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_range_value1144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_range_value1158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNC_NAME_in_func_name1179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_multi_value1194 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_multiClause_in_multi_value1196 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_RPAREN_in_multi_value1198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clauseOr_in_multiClause1225 = new BitSet(new long[]{0xA40040E43B912082L,0x00000000000000D4L});
    public static final BitSet FOLLOW_TERM_TRUNCATED_in_truncated1304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_in_quoted1331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUS_in_lmodifier1343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_lmodifier1353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_lmodifier1363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_lmodifier1373 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_rmodifier1392 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_CARAT_in_rmodifier1394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_rmodifier1416 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_TILDE_in_rmodifier1418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_boost1447 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_NUMBER_in_boost1462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_fuzzy1485 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_NUMBER_in_fuzzy1500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_not1530 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NOT_in_not1532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_not1537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_and1551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_or1565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_near1580 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_near1593 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_NUMBER_in_near1597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_comma1619 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_SEMICOLON_in_semicolon1633 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_DATE_TOKEN_in_date1648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_identifier1662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HOUR_in_coordinate1681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_H_NUMBER_in_coordinate1688 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_M_NUMBER_in_coordinate1690 = new BitSet(new long[]{0x0000008020000000L});
    public static final BitSet FOLLOW_set_in_coordinate1692 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_D_NUMBER_in_coordinate1698 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_M_NUMBER_in_coordinate1700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_H_NUMBER_in_coordinate1707 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_NUMBER_in_coordinate1709 = new BitSet(new long[]{0x0000008020000000L});
    public static final BitSet FOLLOW_set_in_coordinate1711 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_D_NUMBER_in_coordinate1717 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_NUMBER_in_coordinate1719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_D_NUMBER_in_coordinate1726 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_M_NUMBER_in_coordinate1728 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_S_NUMBER_in_coordinate1730 = new BitSet(new long[]{0x0000008020000000L});
    public static final BitSet FOLLOW_set_in_coordinate1732 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_D_NUMBER_in_coordinate1738 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_M_NUMBER_in_coordinate1740 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_S_NUMBER_in_coordinate1742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_H_NUMBER_in_coordinate1749 = new BitSet(new long[]{0x0000008020000000L});
    public static final BitSet FOLLOW_set_in_coordinate1751 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_D_NUMBER_in_coordinate1757 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_coordinate1764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lmodifier_in_synpred1_ADS465 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_func_name_in_synpred1_ADS468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lmodifier_in_synpred2_ADS527 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred2_ADS529 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseOr_in_synpred2_ADS531 = new BitSet(new long[]{0xA48040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_RPAREN_in_synpred2_ADS534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred3_ADS588 = new BitSet(new long[]{0xA40040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_clauseOr_in_synpred3_ADS590 = new BitSet(new long[]{0xA48040E43B912080L,0x00000000000000D4L});
    public static final BitSet FOLLOW_RPAREN_in_synpred3_ADS593 = new BitSet(new long[]{0x0000000000000200L,0x0000000000000001L});
    public static final BitSet FOLLOW_rmodifier_in_synpred3_ADS595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred4_ADS648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_synpred5_ADS1524 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_NOT_in_synpred5_ADS1526 = new BitSet(new long[]{0x0000000000000002L});

}