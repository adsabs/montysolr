// $ANTLR 3.4 StandardLuceneGrammar.g 2011-10-31 01:29:15

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ADDED", "AMPER", "AND", "ATOM", "BOOST", "CARAT", "CLAUSE", "COLON", "DQUOTE", "ESC_CHAR", "FIELD", "FUZZY", "INT", "LBRACK", "LCURLY", "LPAREN", "MINUS", "MODIFIER", "MULTIATOM", "MULTITERM", "NEAR", "NORMAL_CHAR", "NOT", "NUCLEUS", "NUMBER", "OPERATOR", "OR", "PHRASE", "PHRASE_ANYTHING", "PLUS", "QANYTHING", "QMARK", "QNORMAL", "QPHRASE", "QPHRASETRUNC", "QRANGEEX", "QRANGEIN", "QTRUNCATED", "RBRACK", "RCURLY", "RPAREN", "SQUOTE", "STAR", "TERM_NORMAL", "TERM_TRUNCATED", "TILDE", "TMODIFIER", "TO", "VALUE", "VBAR", "WS", "'/'"
    };

    public static final int EOF=-1;
    public static final int T__55=55;
    public static final int ADDED=4;
    public static final int AMPER=5;
    public static final int AND=6;
    public static final int ATOM=7;
    public static final int BOOST=8;
    public static final int CARAT=9;
    public static final int CLAUSE=10;
    public static final int COLON=11;
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
    public static final int MULTIATOM=22;
    public static final int MULTITERM=23;
    public static final int NEAR=24;
    public static final int NORMAL_CHAR=25;
    public static final int NOT=26;
    public static final int NUCLEUS=27;
    public static final int NUMBER=28;
    public static final int OPERATOR=29;
    public static final int OR=30;
    public static final int PHRASE=31;
    public static final int PHRASE_ANYTHING=32;
    public static final int PLUS=33;
    public static final int QANYTHING=34;
    public static final int QMARK=35;
    public static final int QNORMAL=36;
    public static final int QPHRASE=37;
    public static final int QPHRASETRUNC=38;
    public static final int QRANGEEX=39;
    public static final int QRANGEIN=40;
    public static final int QTRUNCATED=41;
    public static final int RBRACK=42;
    public static final int RCURLY=43;
    public static final int RPAREN=44;
    public static final int SQUOTE=45;
    public static final int STAR=46;
    public static final int TERM_NORMAL=47;
    public static final int TERM_TRUNCATED=48;
    public static final int TILDE=49;
    public static final int TMODIFIER=50;
    public static final int TO=51;
    public static final int VALUE=52;
    public static final int VBAR=53;
    public static final int WS=54;

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
    // StandardLuceneGrammar.g:38:1: mainQ : ( clauseDefault )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) ;
    public final StandardLuceneGrammarParser.mainQ_return mainQ() throws RecognitionException {
        StandardLuceneGrammarParser.mainQ_return retval = new StandardLuceneGrammarParser.mainQ_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseDefault_return clauseDefault1 =null;


        RewriteRuleSubtreeStream stream_clauseDefault=new RewriteRuleSubtreeStream(adaptor,"rule clauseDefault");
        try {
            // StandardLuceneGrammar.g:38:7: ( ( clauseDefault )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) )
            // StandardLuceneGrammar.g:39:2: ( clauseDefault )+
            {
            // StandardLuceneGrammar.g:39:2: ( clauseDefault )+
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
            	    // StandardLuceneGrammar.g:39:2: clauseDefault
            	    {
            	    pushFollow(FOLLOW_clauseDefault_in_mainQ153);
            	    clauseDefault1=clauseDefault();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseDefault.add(clauseDefault1.getTree());

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
            // elements: clauseDefault
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 39:17: -> ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ )
            {
                // StandardLuceneGrammar.g:39:20: ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OPERATOR, "DEFOP")
                , root_1);

                if ( !(stream_clauseDefault.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_clauseDefault.hasNext() ) {
                    adaptor.addChild(root_1, stream_clauseDefault.nextTree());

                }
                stream_clauseDefault.reset();

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


    public static class clauseDefault_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseDefault"
    // StandardLuceneGrammar.g:43:1: clauseDefault : (first= clauseStrongest -> $first) ( OR others= clauseStrongest -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ ) )* ;
    public final StandardLuceneGrammarParser.clauseDefault_return clauseDefault() throws RecognitionException {
        StandardLuceneGrammarParser.clauseDefault_return retval = new StandardLuceneGrammarParser.clauseDefault_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OR2=null;
        StandardLuceneGrammarParser.clauseStrongest_return first =null;

        StandardLuceneGrammarParser.clauseStrongest_return others =null;


        Object OR2_tree=null;
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
        RewriteRuleSubtreeStream stream_clauseStrongest=new RewriteRuleSubtreeStream(adaptor,"rule clauseStrongest");
        try {
            // StandardLuceneGrammar.g:44:3: ( (first= clauseStrongest -> $first) ( OR others= clauseStrongest -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ ) )* )
            // StandardLuceneGrammar.g:44:5: (first= clauseStrongest -> $first) ( OR others= clauseStrongest -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ ) )*
            {
            // StandardLuceneGrammar.g:44:5: (first= clauseStrongest -> $first)
            // StandardLuceneGrammar.g:44:6: first= clauseStrongest
            {
            pushFollow(FOLLOW_clauseStrongest_in_clauseDefault186);
            first=clauseStrongest();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseStrongest.add(first.getTree());

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
            // 44:28: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:44:39: ( OR others= clauseStrongest -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==OR) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // StandardLuceneGrammar.g:44:40: OR others= clauseStrongest
            	    {
            	    OR2=(Token)match(input,OR,FOLLOW_OR_in_clauseDefault195); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_OR.add(OR2);


            	    pushFollow(FOLLOW_clauseStrongest_in_clauseDefault199);
            	    others=clauseStrongest();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseStrongest.add(others.getTree());

            	    // AST REWRITE
            	    // elements: clauseStrongest
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 44:66: -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ )
            	    {
            	        // StandardLuceneGrammar.g:44:69: ^( OPERATOR[\"OR\"] ( clauseStrongest )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "OR")
            	        , root_1);

            	        if ( !(stream_clauseStrongest.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_clauseStrongest.hasNext() ) {
            	            adaptor.addChild(root_1, stream_clauseStrongest.nextTree());

            	        }
            	        stream_clauseStrongest.reset();

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
    // $ANTLR end "clauseDefault"


    public static class clauseStrongest_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseStrongest"
    // StandardLuceneGrammar.g:47:1: clauseStrongest : (first= clauseStrong -> $first) ( AND others= clauseStrong -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ ) )* ;
    public final StandardLuceneGrammarParser.clauseStrongest_return clauseStrongest() throws RecognitionException {
        StandardLuceneGrammarParser.clauseStrongest_return retval = new StandardLuceneGrammarParser.clauseStrongest_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND3=null;
        StandardLuceneGrammarParser.clauseStrong_return first =null;

        StandardLuceneGrammarParser.clauseStrong_return others =null;


        Object AND3_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_clauseStrong=new RewriteRuleSubtreeStream(adaptor,"rule clauseStrong");
        try {
            // StandardLuceneGrammar.g:48:3: ( (first= clauseStrong -> $first) ( AND others= clauseStrong -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ ) )* )
            // StandardLuceneGrammar.g:48:5: (first= clauseStrong -> $first) ( AND others= clauseStrong -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ ) )*
            {
            // StandardLuceneGrammar.g:48:5: (first= clauseStrong -> $first)
            // StandardLuceneGrammar.g:48:6: first= clauseStrong
            {
            pushFollow(FOLLOW_clauseStrong_in_clauseStrongest228);
            first=clauseStrong();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseStrong.add(first.getTree());

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
            // 48:26: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:48:37: ( AND others= clauseStrong -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==AND) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // StandardLuceneGrammar.g:48:38: AND others= clauseStrong
            	    {
            	    AND3=(Token)match(input,AND,FOLLOW_AND_in_clauseStrongest238); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_AND.add(AND3);


            	    pushFollow(FOLLOW_clauseStrong_in_clauseStrongest242);
            	    others=clauseStrong();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseStrong.add(others.getTree());

            	    // AST REWRITE
            	    // elements: clauseStrong
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 48:62: -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ )
            	    {
            	        // StandardLuceneGrammar.g:48:65: ^( OPERATOR[\"AND\"] ( clauseStrong )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "AND")
            	        , root_1);

            	        if ( !(stream_clauseStrong.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_clauseStrong.hasNext() ) {
            	            adaptor.addChild(root_1, stream_clauseStrong.nextTree());

            	        }
            	        stream_clauseStrong.reset();

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
    // $ANTLR end "clauseStrongest"


    public static class clauseStrong_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseStrong"
    // StandardLuceneGrammar.g:51:1: clauseStrong : (first= clauseWeak -> $first) ( NOT others= clauseWeak -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ ) )* ;
    public final StandardLuceneGrammarParser.clauseStrong_return clauseStrong() throws RecognitionException {
        StandardLuceneGrammarParser.clauseStrong_return retval = new StandardLuceneGrammarParser.clauseStrong_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token NOT4=null;
        StandardLuceneGrammarParser.clauseWeak_return first =null;

        StandardLuceneGrammarParser.clauseWeak_return others =null;


        Object NOT4_tree=null;
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleSubtreeStream stream_clauseWeak=new RewriteRuleSubtreeStream(adaptor,"rule clauseWeak");
        try {
            // StandardLuceneGrammar.g:52:3: ( (first= clauseWeak -> $first) ( NOT others= clauseWeak -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ ) )* )
            // StandardLuceneGrammar.g:52:5: (first= clauseWeak -> $first) ( NOT others= clauseWeak -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ ) )*
            {
            // StandardLuceneGrammar.g:52:5: (first= clauseWeak -> $first)
            // StandardLuceneGrammar.g:52:6: first= clauseWeak
            {
            pushFollow(FOLLOW_clauseWeak_in_clauseStrong273);
            first=clauseWeak();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseWeak.add(first.getTree());

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
            // 52:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:52:34: ( NOT others= clauseWeak -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==NOT) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // StandardLuceneGrammar.g:52:35: NOT others= clauseWeak
            	    {
            	    NOT4=(Token)match(input,NOT,FOLLOW_NOT_in_clauseStrong282); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_NOT.add(NOT4);


            	    pushFollow(FOLLOW_clauseWeak_in_clauseStrong286);
            	    others=clauseWeak();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseWeak.add(others.getTree());

            	    // AST REWRITE
            	    // elements: clauseWeak
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 52:57: -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ )
            	    {
            	        // StandardLuceneGrammar.g:52:60: ^( OPERATOR[\"NOT\"] ( clauseWeak )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "NOT")
            	        , root_1);

            	        if ( !(stream_clauseWeak.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_clauseWeak.hasNext() ) {
            	            adaptor.addChild(root_1, stream_clauseWeak.nextTree());

            	        }
            	        stream_clauseWeak.reset();

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
    // $ANTLR end "clauseStrong"


    public static class clauseWeak_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseWeak"
    // StandardLuceneGrammar.g:55:1: clauseWeak : (first= primaryClause -> $first) ( near others= primaryClause -> ^( near ( primaryClause )+ ) )* ;
    public final StandardLuceneGrammarParser.clauseWeak_return clauseWeak() throws RecognitionException {
        StandardLuceneGrammarParser.clauseWeak_return retval = new StandardLuceneGrammarParser.clauseWeak_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.primaryClause_return first =null;

        StandardLuceneGrammarParser.primaryClause_return others =null;

        StandardLuceneGrammarParser.near_return near5 =null;


        RewriteRuleSubtreeStream stream_near=new RewriteRuleSubtreeStream(adaptor,"rule near");
        RewriteRuleSubtreeStream stream_primaryClause=new RewriteRuleSubtreeStream(adaptor,"rule primaryClause");
        try {
            // StandardLuceneGrammar.g:56:3: ( (first= primaryClause -> $first) ( near others= primaryClause -> ^( near ( primaryClause )+ ) )* )
            // StandardLuceneGrammar.g:56:5: (first= primaryClause -> $first) ( near others= primaryClause -> ^( near ( primaryClause )+ ) )*
            {
            // StandardLuceneGrammar.g:56:5: (first= primaryClause -> $first)
            // StandardLuceneGrammar.g:56:6: first= primaryClause
            {
            pushFollow(FOLLOW_primaryClause_in_clauseWeak317);
            first=primaryClause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_primaryClause.add(first.getTree());

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
            // 56:26: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:56:37: ( near others= primaryClause -> ^( near ( primaryClause )+ ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==NEAR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // StandardLuceneGrammar.g:56:38: near others= primaryClause
            	    {
            	    pushFollow(FOLLOW_near_in_clauseWeak326);
            	    near5=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near5.getTree());

            	    pushFollow(FOLLOW_primaryClause_in_clauseWeak330);
            	    others=primaryClause();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_primaryClause.add(others.getTree());

            	    // AST REWRITE
            	    // elements: near, primaryClause
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 56:64: -> ^( near ( primaryClause )+ )
            	    {
            	        // StandardLuceneGrammar.g:56:67: ^( near ( primaryClause )+ )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(stream_near.nextNode(), root_1);

            	        if ( !(stream_primaryClause.hasNext()) ) {
            	            throw new RewriteEarlyExitException();
            	        }
            	        while ( stream_primaryClause.hasNext() ) {
            	            adaptor.addChild(root_1, stream_primaryClause.nextTree());

            	        }
            	        stream_primaryClause.reset();

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
    // $ANTLR end "clauseWeak"


    public static class primaryClause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "primaryClause"
    // StandardLuceneGrammar.g:59:1: primaryClause : ( ( modifier LPAREN ( clauseDefault )+ RPAREN )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) ) | ( LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) ) | ( LPAREN )=> LPAREN ( clauseDefault )+ RPAREN -> ( clauseDefault )+ | atom );
    public final StandardLuceneGrammarParser.primaryClause_return primaryClause() throws RecognitionException {
        StandardLuceneGrammarParser.primaryClause_return retval = new StandardLuceneGrammarParser.primaryClause_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN7=null;
        Token RPAREN9=null;
        Token CARAT10=null;
        Token NUMBER11=null;
        Token LPAREN13=null;
        Token RPAREN15=null;
        Token CARAT16=null;
        Token NUMBER17=null;
        Token LPAREN18=null;
        Token RPAREN20=null;
        StandardLuceneGrammarParser.modifier_return modifier6 =null;

        StandardLuceneGrammarParser.clauseDefault_return clauseDefault8 =null;

        StandardLuceneGrammarParser.modifier_return modifier12 =null;

        StandardLuceneGrammarParser.clauseDefault_return clauseDefault14 =null;

        StandardLuceneGrammarParser.clauseDefault_return clauseDefault19 =null;

        StandardLuceneGrammarParser.atom_return atom21 =null;


        Object LPAREN7_tree=null;
        Object RPAREN9_tree=null;
        Object CARAT10_tree=null;
        Object NUMBER11_tree=null;
        Object LPAREN13_tree=null;
        Object RPAREN15_tree=null;
        Object CARAT16_tree=null;
        Object NUMBER17_tree=null;
        Object LPAREN18_tree=null;
        Object RPAREN20_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_clauseDefault=new RewriteRuleSubtreeStream(adaptor,"rule clauseDefault");
        try {
            // StandardLuceneGrammar.g:60:2: ( ( modifier LPAREN ( clauseDefault )+ RPAREN )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) ) | ( LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) ) | ( LPAREN )=> LPAREN ( clauseDefault )+ RPAREN -> ( clauseDefault )+ | atom )
            int alt13=4;
            switch ( input.LA(1) ) {
            case MINUS:
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
            case LPAREN:
                {
                int LA13_2 = input.LA(2);

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
                        new NoViableAltException("", 13, 2, input);

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
                    // StandardLuceneGrammar.g:62:2: ( modifier LPAREN ( clauseDefault )+ RPAREN )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )?
                    {
                    // StandardLuceneGrammar.g:62:45: ( modifier )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==MINUS||LA6_0==PLUS) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // StandardLuceneGrammar.g:62:45: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_primaryClause374);
                            modifier6=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier6.getTree());

                            }
                            break;

                    }


                    LPAREN7=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_primaryClause377); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN7);


                    // StandardLuceneGrammar.g:62:62: ( clauseDefault )+
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
                    	    // StandardLuceneGrammar.g:62:62: clauseDefault
                    	    {
                    	    pushFollow(FOLLOW_clauseDefault_in_primaryClause379);
                    	    clauseDefault8=clauseDefault();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseDefault.add(clauseDefault8.getTree());

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


                    RPAREN9=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_primaryClause382); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN9);


                    // StandardLuceneGrammar.g:62:84: ( CARAT NUMBER )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==CARAT) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // StandardLuceneGrammar.g:62:85: CARAT NUMBER
                            {
                            CARAT10=(Token)match(input,CARAT,FOLLOW_CARAT_in_primaryClause385); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CARAT.add(CARAT10);


                            NUMBER11=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_primaryClause387); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER11);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: clauseDefault, NUMBER, modifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 62:100: -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) )
                    {
                        // StandardLuceneGrammar.g:62:103: ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // StandardLuceneGrammar.g:62:112: ^( MODIFIER ( modifier )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:62:123: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:62:134: ^( BOOST ( NUMBER )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_2);

                        // StandardLuceneGrammar.g:62:142: ( NUMBER )?
                        if ( stream_NUMBER.hasNext() ) {
                            adaptor.addChild(root_2, 
                            stream_NUMBER.nextNode()
                            );

                        }
                        stream_NUMBER.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:62:151: ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(OPERATOR, "DEFOP")
                        , root_2);

                        if ( !(stream_clauseDefault.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_clauseDefault.hasNext() ) {
                            adaptor.addChild(root_2, stream_clauseDefault.nextTree());

                        }
                        stream_clauseDefault.reset();

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
                    // StandardLuceneGrammar.g:63:4: ( LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )?
                    {
                    // StandardLuceneGrammar.g:63:50: ( modifier )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==MINUS||LA9_0==PLUS) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // StandardLuceneGrammar.g:63:50: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_primaryClause438);
                            modifier12=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier12.getTree());

                            }
                            break;

                    }


                    LPAREN13=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_primaryClause441); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN13);


                    // StandardLuceneGrammar.g:63:67: ( clauseDefault )+
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
                    	    // StandardLuceneGrammar.g:63:67: clauseDefault
                    	    {
                    	    pushFollow(FOLLOW_clauseDefault_in_primaryClause443);
                    	    clauseDefault14=clauseDefault();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseDefault.add(clauseDefault14.getTree());

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


                    RPAREN15=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_primaryClause446); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN15);


                    // StandardLuceneGrammar.g:63:89: ( CARAT NUMBER )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==CARAT) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // StandardLuceneGrammar.g:63:90: CARAT NUMBER
                            {
                            CARAT16=(Token)match(input,CARAT,FOLLOW_CARAT_in_primaryClause449); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CARAT.add(CARAT16);


                            NUMBER17=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_primaryClause451); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER17);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: clauseDefault, NUMBER, modifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 63:105: -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) )
                    {
                        // StandardLuceneGrammar.g:63:108: ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // StandardLuceneGrammar.g:63:117: ^( MODIFIER ( modifier )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:63:128: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:63:139: ^( BOOST ( NUMBER )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_2);

                        // StandardLuceneGrammar.g:63:147: ( NUMBER )?
                        if ( stream_NUMBER.hasNext() ) {
                            adaptor.addChild(root_2, 
                            stream_NUMBER.nextNode()
                            );

                        }
                        stream_NUMBER.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:63:156: ^( OPERATOR[\"DEFOP\"] ( clauseDefault )+ )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(OPERATOR, "DEFOP")
                        , root_2);

                        if ( !(stream_clauseDefault.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_clauseDefault.hasNext() ) {
                            adaptor.addChild(root_2, stream_clauseDefault.nextTree());

                        }
                        stream_clauseDefault.reset();

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
                    // StandardLuceneGrammar.g:64:4: ( LPAREN )=> LPAREN ( clauseDefault )+ RPAREN
                    {
                    LPAREN18=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_primaryClause493); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN18);


                    // StandardLuceneGrammar.g:64:22: ( clauseDefault )+
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
                    	    // StandardLuceneGrammar.g:64:22: clauseDefault
                    	    {
                    	    pushFollow(FOLLOW_clauseDefault_in_primaryClause495);
                    	    clauseDefault19=clauseDefault();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_clauseDefault.add(clauseDefault19.getTree());

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


                    RPAREN20=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_primaryClause498); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN20);


                    // AST REWRITE
                    // elements: clauseDefault
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 64:44: -> ( clauseDefault )+
                    {
                        if ( !(stream_clauseDefault.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_clauseDefault.hasNext() ) {
                            adaptor.addChild(root_0, stream_clauseDefault.nextTree());

                        }
                        stream_clauseDefault.reset();

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // StandardLuceneGrammar.g:65:4: atom
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_atom_in_primaryClause508);
                    atom21=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom21.getTree());

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
    // $ANTLR end "primaryClause"


    public static class atom_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "atom"
    // StandardLuceneGrammar.g:69:1: atom : ( ( modifier )? field multi_value ( term_modifier )? -> ^( MULTITERM ^( MODIFIER ( modifier )? ) ^( FIELD field ) ^( MULTIATOM multi_value ) ( term_modifier )? ) | ( modifier )? ( field )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS ^( FIELD ( field )? ) ^( VALUE value ) ) ) );
    public final StandardLuceneGrammarParser.atom_return atom() throws RecognitionException {
        StandardLuceneGrammarParser.atom_return retval = new StandardLuceneGrammarParser.atom_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.modifier_return modifier22 =null;

        StandardLuceneGrammarParser.field_return field23 =null;

        StandardLuceneGrammarParser.multi_value_return multi_value24 =null;

        StandardLuceneGrammarParser.term_modifier_return term_modifier25 =null;

        StandardLuceneGrammarParser.modifier_return modifier26 =null;

        StandardLuceneGrammarParser.field_return field27 =null;

        StandardLuceneGrammarParser.value_return value28 =null;


        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_multi_value=new RewriteRuleSubtreeStream(adaptor,"rule multi_value");
        try {
            // StandardLuceneGrammar.g:70:2: ( ( modifier )? field multi_value ( term_modifier )? -> ^( MULTITERM ^( MODIFIER ( modifier )? ) ^( FIELD field ) ^( MULTIATOM multi_value ) ( term_modifier )? ) | ( modifier )? ( field )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS ^( FIELD ( field )? ) ^( VALUE value ) ) ) )
            int alt18=2;
            switch ( input.LA(1) ) {
            case MINUS:
            case PLUS:
                {
                switch ( input.LA(2) ) {
                case TERM_NORMAL:
                    {
                    int LA18_2 = input.LA(3);

                    if ( (LA18_2==COLON) ) {
                        int LA18_5 = input.LA(4);

                        if ( (LA18_5==LPAREN) ) {
                            alt18=1;
                        }
                        else if ( ((LA18_5 >= LBRACK && LA18_5 <= LCURLY)||LA18_5==NUMBER||(LA18_5 >= PHRASE && LA18_5 <= PHRASE_ANYTHING)||LA18_5==QMARK||(LA18_5 >= STAR && LA18_5 <= TERM_TRUNCATED)) ) {
                            alt18=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 18, 5, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA18_2==EOF||LA18_2==AND||LA18_2==CARAT||(LA18_2 >= LBRACK && LA18_2 <= MINUS)||LA18_2==NEAR||LA18_2==NOT||LA18_2==NUMBER||(LA18_2 >= OR && LA18_2 <= PLUS)||LA18_2==QMARK||LA18_2==RPAREN||(LA18_2 >= STAR && LA18_2 <= TILDE)) ) {
                        alt18=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 2, input);

                        throw nvae;

                    }
                    }
                    break;
                case STAR:
                    {
                    int LA18_3 = input.LA(3);

                    if ( (LA18_3==COLON) ) {
                        int LA18_6 = input.LA(4);

                        if ( (LA18_6==LPAREN) ) {
                            alt18=1;
                        }
                        else if ( ((LA18_6 >= LBRACK && LA18_6 <= LCURLY)||LA18_6==NUMBER||(LA18_6 >= PHRASE && LA18_6 <= PHRASE_ANYTHING)||LA18_6==QMARK||(LA18_6 >= STAR && LA18_6 <= TERM_TRUNCATED)) ) {
                            alt18=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 18, 6, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA18_3==EOF||LA18_3==AND||LA18_3==CARAT||(LA18_3 >= LBRACK && LA18_3 <= MINUS)||LA18_3==NEAR||LA18_3==NOT||LA18_3==NUMBER||(LA18_3 >= OR && LA18_3 <= PLUS)||LA18_3==QMARK||LA18_3==RPAREN||(LA18_3 >= STAR && LA18_3 <= TILDE)) ) {
                        alt18=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 3, input);

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
                    alt18=2;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    throw nvae;

                }

                }
                break;
            case TERM_NORMAL:
                {
                int LA18_2 = input.LA(2);

                if ( (LA18_2==COLON) ) {
                    int LA18_5 = input.LA(3);

                    if ( (LA18_5==LPAREN) ) {
                        alt18=1;
                    }
                    else if ( ((LA18_5 >= LBRACK && LA18_5 <= LCURLY)||LA18_5==NUMBER||(LA18_5 >= PHRASE && LA18_5 <= PHRASE_ANYTHING)||LA18_5==QMARK||(LA18_5 >= STAR && LA18_5 <= TERM_TRUNCATED)) ) {
                        alt18=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 5, input);

                        throw nvae;

                    }
                }
                else if ( (LA18_2==EOF||LA18_2==AND||LA18_2==CARAT||(LA18_2 >= LBRACK && LA18_2 <= MINUS)||LA18_2==NEAR||LA18_2==NOT||LA18_2==NUMBER||(LA18_2 >= OR && LA18_2 <= PLUS)||LA18_2==QMARK||LA18_2==RPAREN||(LA18_2 >= STAR && LA18_2 <= TILDE)) ) {
                    alt18=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 2, input);

                    throw nvae;

                }
                }
                break;
            case STAR:
                {
                int LA18_3 = input.LA(2);

                if ( (LA18_3==COLON) ) {
                    int LA18_6 = input.LA(3);

                    if ( (LA18_6==LPAREN) ) {
                        alt18=1;
                    }
                    else if ( ((LA18_6 >= LBRACK && LA18_6 <= LCURLY)||LA18_6==NUMBER||(LA18_6 >= PHRASE && LA18_6 <= PHRASE_ANYTHING)||LA18_6==QMARK||(LA18_6 >= STAR && LA18_6 <= TERM_TRUNCATED)) ) {
                        alt18=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 6, input);

                        throw nvae;

                    }
                }
                else if ( (LA18_3==EOF||LA18_3==AND||LA18_3==CARAT||(LA18_3 >= LBRACK && LA18_3 <= MINUS)||LA18_3==NEAR||LA18_3==NOT||LA18_3==NUMBER||(LA18_3 >= OR && LA18_3 <= PLUS)||LA18_3==QMARK||LA18_3==RPAREN||(LA18_3 >= STAR && LA18_3 <= TILDE)) ) {
                    alt18=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 3, input);

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
                alt18=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }

            switch (alt18) {
                case 1 :
                    // StandardLuceneGrammar.g:71:2: ( modifier )? field multi_value ( term_modifier )?
                    {
                    // StandardLuceneGrammar.g:71:2: ( modifier )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==MINUS||LA14_0==PLUS) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // StandardLuceneGrammar.g:71:2: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom530);
                            modifier22=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier22.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_field_in_atom533);
                    field23=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field23.getTree());

                    pushFollow(FOLLOW_multi_value_in_atom535);
                    multi_value24=multi_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_multi_value.add(multi_value24.getTree());

                    // StandardLuceneGrammar.g:71:30: ( term_modifier )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==CARAT||LA15_0==TILDE) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // StandardLuceneGrammar.g:71:30: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_atom537);
                            term_modifier25=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier25.getTree());

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
                    // 71:45: -> ^( MULTITERM ^( MODIFIER ( modifier )? ) ^( FIELD field ) ^( MULTIATOM multi_value ) ( term_modifier )? )
                    {
                        // StandardLuceneGrammar.g:71:48: ^( MULTITERM ^( MODIFIER ( modifier )? ) ^( FIELD field ) ^( MULTIATOM multi_value ) ( term_modifier )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MULTITERM, "MULTITERM")
                        , root_1);

                        // StandardLuceneGrammar.g:71:60: ^( MODIFIER ( modifier )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:71:71: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:71:82: ^( FIELD field )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_2);

                        adaptor.addChild(root_2, stream_field.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:71:97: ^( MULTIATOM multi_value )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MULTIATOM, "MULTIATOM")
                        , root_2);

                        adaptor.addChild(root_2, stream_multi_value.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:71:122: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:72:4: ( modifier )? ( field )? value
                    {
                    // StandardLuceneGrammar.g:72:4: ( modifier )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==MINUS||LA16_0==PLUS) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // StandardLuceneGrammar.g:72:4: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom571);
                            modifier26=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier26.getTree());

                            }
                            break;

                    }


                    // StandardLuceneGrammar.g:72:14: ( field )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==TERM_NORMAL) ) {
                        int LA17_1 = input.LA(2);

                        if ( (LA17_1==COLON) ) {
                            alt17=1;
                        }
                    }
                    else if ( (LA17_0==STAR) ) {
                        int LA17_2 = input.LA(2);

                        if ( (LA17_2==COLON) ) {
                            alt17=1;
                        }
                    }
                    switch (alt17) {
                        case 1 :
                            // StandardLuceneGrammar.g:72:14: field
                            {
                            pushFollow(FOLLOW_field_in_atom574);
                            field27=field();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_field.add(field27.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_value_in_atom577);
                    value28=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value28.getTree());

                    // AST REWRITE
                    // elements: field, modifier, value
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 72:27: -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS ^( FIELD ( field )? ) ^( VALUE value ) ) )
                    {
                        // StandardLuceneGrammar.g:72:30: ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS ^( FIELD ( field )? ) ^( VALUE value ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(ATOM, "ATOM")
                        , root_1);

                        // StandardLuceneGrammar.g:72:37: ^( MODIFIER ( modifier )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:72:48: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:72:59: ^( NUCLEUS ^( FIELD ( field )? ) ^( VALUE value ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(NUCLEUS, "NUCLEUS")
                        , root_2);

                        // StandardLuceneGrammar.g:72:69: ^( FIELD ( field )? )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_3);

                        // StandardLuceneGrammar.g:72:77: ( field )?
                        if ( stream_field.hasNext() ) {
                            adaptor.addChild(root_3, stream_field.nextTree());

                        }
                        stream_field.reset();

                        adaptor.addChild(root_2, root_3);
                        }

                        // StandardLuceneGrammar.g:72:85: ^( VALUE value )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(VALUE, "VALUE")
                        , root_3);

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
    // StandardLuceneGrammar.g:76:1: field : ( TERM_NORMAL COLON -> TERM_NORMAL | STAR COLON -> ^( QANYTHING STAR ) );
    public final StandardLuceneGrammarParser.field_return field() throws RecognitionException {
        StandardLuceneGrammarParser.field_return retval = new StandardLuceneGrammarParser.field_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_NORMAL29=null;
        Token COLON30=null;
        Token STAR31=null;
        Token COLON32=null;

        Object TERM_NORMAL29_tree=null;
        Object COLON30_tree=null;
        Object STAR31_tree=null;
        Object COLON32_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_TERM_NORMAL=new RewriteRuleTokenStream(adaptor,"token TERM_NORMAL");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");

        try {
            // StandardLuceneGrammar.g:77:2: ( TERM_NORMAL COLON -> TERM_NORMAL | STAR COLON -> ^( QANYTHING STAR ) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==TERM_NORMAL) ) {
                alt19=1;
            }
            else if ( (LA19_0==STAR) ) {
                alt19=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }
            switch (alt19) {
                case 1 :
                    // StandardLuceneGrammar.g:78:2: TERM_NORMAL COLON
                    {
                    TERM_NORMAL29=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_field629); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL29);


                    COLON30=(Token)match(input,COLON,FOLLOW_COLON_in_field631); if (state.failed) return retval; 
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
                    // 78:20: -> TERM_NORMAL
                    {
                        adaptor.addChild(root_0, 
                        stream_TERM_NORMAL.nextNode()
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:79:11: STAR COLON
                    {
                    STAR31=(Token)match(input,STAR,FOLLOW_STAR_in_field647); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR31);


                    COLON32=(Token)match(input,COLON,FOLLOW_COLON_in_field649); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(COLON32);


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
                    // 79:22: -> ^( QANYTHING STAR )
                    {
                        // StandardLuceneGrammar.g:79:25: ^( QANYTHING STAR )
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
    // $ANTLR end "field"


    public static class value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "value"
    // StandardLuceneGrammar.g:82:1: value : ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | truncated -> ^( QTRUNCATED truncated ) | normal -> ^( QNORMAL normal ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | STAR -> ^( QANYTHING STAR ) | QMARK -> ^( QANYTHING QMARK ) ) ( term_modifier )? -> ( term_modifier )? $value;
    public final StandardLuceneGrammarParser.value_return value() throws RecognitionException {
        StandardLuceneGrammarParser.value_return retval = new StandardLuceneGrammarParser.value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR39=null;
        Token QMARK40=null;
        StandardLuceneGrammarParser.range_term_in_return range_term_in33 =null;

        StandardLuceneGrammarParser.range_term_ex_return range_term_ex34 =null;

        StandardLuceneGrammarParser.truncated_return truncated35 =null;

        StandardLuceneGrammarParser.normal_return normal36 =null;

        StandardLuceneGrammarParser.quoted_return quoted37 =null;

        StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated38 =null;

        StandardLuceneGrammarParser.term_modifier_return term_modifier41 =null;


        Object STAR39_tree=null;
        Object QMARK40_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_range_term_ex=new RewriteRuleSubtreeStream(adaptor,"rule range_term_ex");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_range_term_in=new RewriteRuleSubtreeStream(adaptor,"rule range_term_in");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        try {
            // StandardLuceneGrammar.g:83:2: ( ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | truncated -> ^( QTRUNCATED truncated ) | normal -> ^( QNORMAL normal ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | STAR -> ^( QANYTHING STAR ) | QMARK -> ^( QANYTHING QMARK ) ) ( term_modifier )? -> ( term_modifier )? $value)
            // StandardLuceneGrammar.g:84:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | truncated -> ^( QTRUNCATED truncated ) | normal -> ^( QNORMAL normal ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | STAR -> ^( QANYTHING STAR ) | QMARK -> ^( QANYTHING QMARK ) ) ( term_modifier )?
            {
            // StandardLuceneGrammar.g:84:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | truncated -> ^( QTRUNCATED truncated ) | normal -> ^( QNORMAL normal ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | STAR -> ^( QANYTHING STAR ) | QMARK -> ^( QANYTHING QMARK ) )
            int alt20=8;
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
            case TERM_TRUNCATED:
                {
                alt20=3;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
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
            case STAR:
                {
                alt20=7;
                }
                break;
            case QMARK:
                {
                alt20=8;
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
                    // StandardLuceneGrammar.g:85:2: range_term_in
                    {
                    pushFollow(FOLLOW_range_term_in_in_value676);
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
                    pushFollow(FOLLOW_range_term_ex_in_value689);
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
                    // StandardLuceneGrammar.g:87:4: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_value703);
                    truncated35=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated35.getTree());

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
                    // 87:14: -> ^( QTRUNCATED truncated )
                    {
                        // StandardLuceneGrammar.g:87:17: ^( QTRUNCATED truncated )
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
                case 4 :
                    // StandardLuceneGrammar.g:88:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_value717);
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
                    // 88:11: -> ^( QNORMAL normal )
                    {
                        // StandardLuceneGrammar.g:88:14: ^( QNORMAL normal )
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
                    // StandardLuceneGrammar.g:89:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_value730);
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
                    pushFollow(FOLLOW_quoted_truncated_in_value743);
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
                    // StandardLuceneGrammar.g:91:4: STAR
                    {
                    STAR39=(Token)match(input,STAR,FOLLOW_STAR_in_value756); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR39);


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
                    // 91:9: -> ^( QANYTHING STAR )
                    {
                        // StandardLuceneGrammar.g:91:12: ^( QANYTHING STAR )
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
                case 8 :
                    // StandardLuceneGrammar.g:92:4: QMARK
                    {
                    QMARK40=(Token)match(input,QMARK,FOLLOW_QMARK_in_value769); if (state.failed) return retval; 
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
                    // 92:10: -> ^( QANYTHING QMARK )
                    {
                        // StandardLuceneGrammar.g:92:13: ^( QANYTHING QMARK )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QANYTHING, "QANYTHING")
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


            // StandardLuceneGrammar.g:94:2: ( term_modifier )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==CARAT||LA21_0==TILDE) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // StandardLuceneGrammar.g:94:2: term_modifier
                    {
                    pushFollow(FOLLOW_term_modifier_in_value783);
                    term_modifier41=term_modifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier41.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: value, term_modifier
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 94:17: -> ( term_modifier )? $value
            {
                // StandardLuceneGrammar.g:94:20: ( term_modifier )?
                if ( stream_term_modifier.hasNext() ) {
                    adaptor.addChild(root_0, stream_term_modifier.nextTree());

                }
                stream_term_modifier.reset();

                adaptor.addChild(root_0, stream_retval.nextTree());

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

        Token LBRACK42=null;
        Token TO43=null;
        Token RBRACK44=null;
        StandardLuceneGrammarParser.range_value_return a =null;

        StandardLuceneGrammarParser.range_value_return b =null;


        Object LBRACK42_tree=null;
        Object TO43_tree=null;
        Object RBRACK44_tree=null;
        RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
        RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // StandardLuceneGrammar.g:100:2: ( LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK )
            // StandardLuceneGrammar.g:101:8: LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK
            {
            LBRACK42=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_range_term_in816); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK42);


            // StandardLuceneGrammar.g:102:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // StandardLuceneGrammar.g:102:9: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_in828);
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

            if ( (LA23_0==NUMBER||(LA23_0 >= PHRASE && LA23_0 <= PHRASE_ANYTHING)||(LA23_0 >= STAR && LA23_0 <= TERM_TRUNCATED)||LA23_0==TO) ) {
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
                            TO43=(Token)match(input,TO,FOLLOW_TO_in_range_term_in851); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO43);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_in856);
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


            RBRACK44=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_range_term_in877); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACK.add(RBRACK44);


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

        Token LCURLY45=null;
        Token TO46=null;
        Token RCURLY47=null;
        StandardLuceneGrammarParser.range_value_return a =null;

        StandardLuceneGrammarParser.range_value_return b =null;


        Object LCURLY45_tree=null;
        Object TO46_tree=null;
        Object RCURLY47_tree=null;
        RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // StandardLuceneGrammar.g:109:2: ( LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY )
            // StandardLuceneGrammar.g:110:8: LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY
            {
            LCURLY45=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_range_term_ex897); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LCURLY.add(LCURLY45);


            // StandardLuceneGrammar.g:111:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // StandardLuceneGrammar.g:111:10: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_ex910);
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

            if ( (LA25_0==NUMBER||(LA25_0 >= PHRASE && LA25_0 <= PHRASE_ANYTHING)||(LA25_0 >= STAR && LA25_0 <= TERM_TRUNCATED)||LA25_0==TO) ) {
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
                            TO46=(Token)match(input,TO,FOLLOW_TO_in_range_term_ex933); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO46);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_ex938);
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


            RCURLY47=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_range_term_ex959); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RCURLY.add(RCURLY47);


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
    // StandardLuceneGrammar.g:116:1: range_value : ( normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | STAR -> ^( QANYTHING STAR ) );
    public final StandardLuceneGrammarParser.range_value_return range_value() throws RecognitionException {
        StandardLuceneGrammarParser.range_value_return retval = new StandardLuceneGrammarParser.range_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR52=null;
        StandardLuceneGrammarParser.normal_return normal48 =null;

        StandardLuceneGrammarParser.truncated_return truncated49 =null;

        StandardLuceneGrammarParser.quoted_return quoted50 =null;

        StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated51 =null;


        Object STAR52_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        try {
            // StandardLuceneGrammar.g:117:2: ( normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | STAR -> ^( QANYTHING STAR ) )
            int alt26=5;
            switch ( input.LA(1) ) {
            case NUMBER:
            case TERM_NORMAL:
                {
                alt26=1;
                }
                break;
            case TERM_TRUNCATED:
                {
                alt26=2;
                }
                break;
            case PHRASE:
                {
                alt26=3;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt26=4;
                }
                break;
            case STAR:
                {
                alt26=5;
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
                    // StandardLuceneGrammar.g:118:2: normal
                    {
                    pushFollow(FOLLOW_normal_in_range_value973);
                    normal48=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal48.getTree());

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
                    // 118:9: -> ^( QNORMAL normal )
                    {
                        // StandardLuceneGrammar.g:118:12: ^( QNORMAL normal )
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
                case 2 :
                    // StandardLuceneGrammar.g:119:4: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_range_value986);
                    truncated49=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated49.getTree());

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
                    // 119:14: -> ^( QTRUNCATED truncated )
                    {
                        // StandardLuceneGrammar.g:119:17: ^( QTRUNCATED truncated )
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
                case 3 :
                    // StandardLuceneGrammar.g:120:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_range_value999);
                    quoted50=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted50.getTree());

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
                    // 120:11: -> ^( QPHRASE quoted )
                    {
                        // StandardLuceneGrammar.g:120:14: ^( QPHRASE quoted )
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
                case 4 :
                    // StandardLuceneGrammar.g:121:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_range_value1012);
                    quoted_truncated51=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated51.getTree());

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
                    // 121:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // StandardLuceneGrammar.g:121:24: ^( QPHRASETRUNC quoted_truncated )
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
                case 5 :
                    // StandardLuceneGrammar.g:122:4: STAR
                    {
                    STAR52=(Token)match(input,STAR,FOLLOW_STAR_in_range_value1025); if (state.failed) return retval; 
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
                    // 122:9: -> ^( QANYTHING STAR )
                    {
                        // StandardLuceneGrammar.g:122:12: ^( QANYTHING STAR )
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
    // StandardLuceneGrammar.g:125:1: multi_value : LPAREN ( mterm )+ RPAREN -> ( mterm )+ ;
    public final StandardLuceneGrammarParser.multi_value_return multi_value() throws RecognitionException {
        StandardLuceneGrammarParser.multi_value_return retval = new StandardLuceneGrammarParser.multi_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN53=null;
        Token RPAREN55=null;
        StandardLuceneGrammarParser.mterm_return mterm54 =null;


        Object LPAREN53_tree=null;
        Object RPAREN55_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_mterm=new RewriteRuleSubtreeStream(adaptor,"rule mterm");
        try {
            // StandardLuceneGrammar.g:126:2: ( LPAREN ( mterm )+ RPAREN -> ( mterm )+ )
            // StandardLuceneGrammar.g:127:2: LPAREN ( mterm )+ RPAREN
            {
            LPAREN53=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_multi_value1046); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN53);


            // StandardLuceneGrammar.g:127:9: ( mterm )+
            int cnt27=0;
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( ((LA27_0 >= LBRACK && LA27_0 <= LCURLY)||LA27_0==MINUS||LA27_0==NUMBER||(LA27_0 >= PHRASE && LA27_0 <= PLUS)||LA27_0==QMARK||(LA27_0 >= STAR && LA27_0 <= TERM_TRUNCATED)) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // StandardLuceneGrammar.g:127:9: mterm
            	    {
            	    pushFollow(FOLLOW_mterm_in_multi_value1048);
            	    mterm54=mterm();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_mterm.add(mterm54.getTree());

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


            RPAREN55=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_multi_value1051); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN55);


            // AST REWRITE
            // elements: mterm
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 127:23: -> ( mterm )+
            {
                if ( !(stream_mterm.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_mterm.hasNext() ) {
                    adaptor.addChild(root_0, stream_mterm.nextTree());

                }
                stream_mterm.reset();

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


    public static class mterm_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mterm"
    // StandardLuceneGrammar.g:131:1: mterm : ( ( modifier )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS FIELD ^( VALUE value ) ) ) ) ;
    public final StandardLuceneGrammarParser.mterm_return mterm() throws RecognitionException {
        StandardLuceneGrammarParser.mterm_return retval = new StandardLuceneGrammarParser.mterm_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.modifier_return modifier56 =null;

        StandardLuceneGrammarParser.value_return value57 =null;


        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try {
            // StandardLuceneGrammar.g:132:2: ( ( ( modifier )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS FIELD ^( VALUE value ) ) ) ) )
            // StandardLuceneGrammar.g:133:2: ( ( modifier )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS FIELD ^( VALUE value ) ) ) )
            {
            // StandardLuceneGrammar.g:133:2: ( ( modifier )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS FIELD ^( VALUE value ) ) ) )
            // StandardLuceneGrammar.g:133:3: ( modifier )? value
            {
            // StandardLuceneGrammar.g:133:3: ( modifier )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==MINUS||LA28_0==PLUS) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // StandardLuceneGrammar.g:133:3: modifier
                    {
                    pushFollow(FOLLOW_modifier_in_mterm1074);
                    modifier56=modifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_modifier.add(modifier56.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_value_in_mterm1077);
            value57=value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_value.add(value57.getTree());

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
            // 133:19: -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS FIELD ^( VALUE value ) ) )
            {
                // StandardLuceneGrammar.g:133:22: ^( ATOM ^( MODIFIER ( modifier )? ) ^( NUCLEUS FIELD ^( VALUE value ) ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(ATOM, "ATOM")
                , root_1);

                // StandardLuceneGrammar.g:133:29: ^( MODIFIER ( modifier )? )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(MODIFIER, "MODIFIER")
                , root_2);

                // StandardLuceneGrammar.g:133:40: ( modifier )?
                if ( stream_modifier.hasNext() ) {
                    adaptor.addChild(root_2, stream_modifier.nextTree());

                }
                stream_modifier.reset();

                adaptor.addChild(root_1, root_2);
                }

                // StandardLuceneGrammar.g:133:51: ^( NUCLEUS FIELD ^( VALUE value ) )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(NUCLEUS, "NUCLEUS")
                , root_2);

                adaptor.addChild(root_2, 
                (Object)adaptor.create(FIELD, "FIELD")
                );

                // StandardLuceneGrammar.g:133:67: ^( VALUE value )
                {
                Object root_3 = (Object)adaptor.nil();
                root_3 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VALUE, "VALUE")
                , root_3);

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
    // StandardLuceneGrammar.g:137:1: normal : ( TERM_NORMAL | NUMBER );
    public final StandardLuceneGrammarParser.normal_return normal() throws RecognitionException {
        StandardLuceneGrammarParser.normal_return retval = new StandardLuceneGrammarParser.normal_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set58=null;

        Object set58_tree=null;

        try {
            // StandardLuceneGrammar.g:138:2: ( TERM_NORMAL | NUMBER )
            // StandardLuceneGrammar.g:
            {
            root_0 = (Object)adaptor.nil();


            set58=(Token)input.LT(1);

            if ( input.LA(1)==NUMBER||input.LA(1)==TERM_NORMAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set58)
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
    // StandardLuceneGrammar.g:146:1: truncated : TERM_TRUNCATED ;
    public final StandardLuceneGrammarParser.truncated_return truncated() throws RecognitionException {
        StandardLuceneGrammarParser.truncated_return retval = new StandardLuceneGrammarParser.truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_TRUNCATED59=null;

        Object TERM_TRUNCATED59_tree=null;

        try {
            // StandardLuceneGrammar.g:147:2: ( TERM_TRUNCATED )
            // StandardLuceneGrammar.g:148:2: TERM_TRUNCATED
            {
            root_0 = (Object)adaptor.nil();


            TERM_TRUNCATED59=(Token)match(input,TERM_TRUNCATED,FOLLOW_TERM_TRUNCATED_in_truncated1145); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            TERM_TRUNCATED59_tree = 
            (Object)adaptor.create(TERM_TRUNCATED59)
            ;
            adaptor.addChild(root_0, TERM_TRUNCATED59_tree);
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
    // StandardLuceneGrammar.g:152:1: quoted_truncated : PHRASE_ANYTHING ;
    public final StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated() throws RecognitionException {
        StandardLuceneGrammarParser.quoted_truncated_return retval = new StandardLuceneGrammarParser.quoted_truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE_ANYTHING60=null;

        Object PHRASE_ANYTHING60_tree=null;

        try {
            // StandardLuceneGrammar.g:153:2: ( PHRASE_ANYTHING )
            // StandardLuceneGrammar.g:154:2: PHRASE_ANYTHING
            {
            root_0 = (Object)adaptor.nil();


            PHRASE_ANYTHING60=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1160); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE_ANYTHING60_tree = 
            (Object)adaptor.create(PHRASE_ANYTHING60)
            ;
            adaptor.addChild(root_0, PHRASE_ANYTHING60_tree);
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
    // StandardLuceneGrammar.g:157:1: quoted : PHRASE ;
    public final StandardLuceneGrammarParser.quoted_return quoted() throws RecognitionException {
        StandardLuceneGrammarParser.quoted_return retval = new StandardLuceneGrammarParser.quoted_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE61=null;

        Object PHRASE61_tree=null;

        try {
            // StandardLuceneGrammar.g:157:8: ( PHRASE )
            // StandardLuceneGrammar.g:158:2: PHRASE
            {
            root_0 = (Object)adaptor.nil();


            PHRASE61=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_quoted1172); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE61_tree = 
            (Object)adaptor.create(PHRASE61)
            ;
            adaptor.addChild(root_0, PHRASE61_tree);
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
    // StandardLuceneGrammar.g:164:1: operator : ( AND | OR | NOT | NEAR ) ;
    public final StandardLuceneGrammarParser.operator_return operator() throws RecognitionException {
        StandardLuceneGrammarParser.operator_return retval = new StandardLuceneGrammarParser.operator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set62=null;

        Object set62_tree=null;

        try {
            // StandardLuceneGrammar.g:164:9: ( ( AND | OR | NOT | NEAR ) )
            // StandardLuceneGrammar.g:
            {
            root_0 = (Object)adaptor.nil();


            set62=(Token)input.LT(1);

            if ( input.LA(1)==AND||input.LA(1)==NEAR||input.LA(1)==NOT||input.LA(1)==OR ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set62)
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
    // $ANTLR end "operator"


    public static class modifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "modifier"
    // StandardLuceneGrammar.g:166:1: modifier : ( PLUS | MINUS ) ;
    public final StandardLuceneGrammarParser.modifier_return modifier() throws RecognitionException {
        StandardLuceneGrammarParser.modifier_return retval = new StandardLuceneGrammarParser.modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set63=null;

        Object set63_tree=null;

        try {
            // StandardLuceneGrammar.g:166:9: ( ( PLUS | MINUS ) )
            // StandardLuceneGrammar.g:
            {
            root_0 = (Object)adaptor.nil();


            set63=(Token)input.LT(1);

            if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set63)
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
    // $ANTLR end "modifier"


    public static class term_modifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "term_modifier"
    // StandardLuceneGrammar.g:178:1: term_modifier : ( ( ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )* ) | ( TILDE -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) ) ) ( (~ ( WS | TILDE | CARAT ) )=>f= NUMBER -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) ) )* );
    public final StandardLuceneGrammarParser.term_modifier_return term_modifier() throws RecognitionException {
        StandardLuceneGrammarParser.term_modifier_return retval = new StandardLuceneGrammarParser.term_modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token b=null;
        Token f=null;
        Token CARAT64=null;
        Token TILDE65=null;
        Token TILDE66=null;
        Token TILDE67=null;

        Object b_tree=null;
        Object f_tree=null;
        Object CARAT64_tree=null;
        Object TILDE65_tree=null;
        Object TILDE66_tree=null;
        Object TILDE67_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

        try {
            // StandardLuceneGrammar.g:178:15: ( ( ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )* ) | ( TILDE -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) ) ) ( (~ ( WS | TILDE | CARAT ) )=>f= NUMBER -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) ) )* )
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==CARAT) ) {
                alt31=1;
            }
            else if ( (LA31_0==TILDE) ) {
                alt31=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;

            }
            switch (alt31) {
                case 1 :
                    // StandardLuceneGrammar.g:180:2: ( ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )* )
                    {
                    // StandardLuceneGrammar.g:180:2: ( ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )* )
                    // StandardLuceneGrammar.g:181:4: ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )*
                    {
                    // StandardLuceneGrammar.g:181:4: ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) )
                    // StandardLuceneGrammar.g:181:5: CARAT b= NUMBER
                    {
                    CARAT64=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1231); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CARAT.add(CARAT64);


                    b=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_term_modifier1235); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(b);


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
                    // 181:20: -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) )
                    {
                        // StandardLuceneGrammar.g:181:23: ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:181:35: ^( BOOST $b)
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_2);

                        adaptor.addChild(root_2, stream_b.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:181:47: ^( FUZZY )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FUZZY, "FUZZY")
                        , root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }


                    // StandardLuceneGrammar.g:183:2: ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )*
                    loop29:
                    do {
                        int alt29=3;
                        int LA29_0 = input.LA(1);

                        if ( (LA29_0==TILDE) ) {
                            int LA29_2 = input.LA(2);

                            if ( (LA29_2==NUMBER) ) {
                                int LA29_3 = input.LA(3);

                                if ( (synpred4_StandardLuceneGrammar()) ) {
                                    alt29=1;
                                }
                                else if ( (true) ) {
                                    alt29=2;
                                }


                            }
                            else if ( (LA29_2==EOF||LA29_2==AND||(LA29_2 >= LBRACK && LA29_2 <= MINUS)||LA29_2==NEAR||LA29_2==NOT||(LA29_2 >= OR && LA29_2 <= PLUS)||LA29_2==QMARK||LA29_2==RPAREN||(LA29_2 >= STAR && LA29_2 <= TILDE)) ) {
                                alt29=2;
                            }


                        }


                        switch (alt29) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:184:3: ( TILDE NUMBER )=> TILDE f= NUMBER
                    	    {
                    	    TILDE65=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1273); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_TILDE.add(TILDE65);


                    	    f=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_term_modifier1277); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_NUMBER.add(f);


                    	    // AST REWRITE
                    	    // elements: b, f
                    	    // token labels: f, b
                    	    // rule labels: retval
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleTokenStream stream_f=new RewriteRuleTokenStream(adaptor,"token f",f);
                    	    RewriteRuleTokenStream stream_b=new RewriteRuleTokenStream(adaptor,"token b",b);
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    	    root_0 = (Object)adaptor.nil();
                    	    // 184:35: -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) )
                    	    {
                    	        // StandardLuceneGrammar.g:184:38: ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) )
                    	        {
                    	        Object root_1 = (Object)adaptor.nil();
                    	        root_1 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                    	        , root_1);

                    	        // StandardLuceneGrammar.g:184:50: ^( BOOST $b)
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(BOOST, "BOOST")
                    	        , root_2);

                    	        adaptor.addChild(root_2, stream_b.nextNode());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        // StandardLuceneGrammar.g:184:62: ^( FUZZY $f)
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(FUZZY, "FUZZY")
                    	        , root_2);

                    	        adaptor.addChild(root_2, stream_f.nextNode());

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
                    	    // StandardLuceneGrammar.g:185:5: TILDE
                    	    {
                    	    TILDE66=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1304); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_TILDE.add(TILDE66);


                    	    // AST REWRITE
                    	    // elements: NUMBER, b
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
                    	    // 185:11: -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) )
                    	    {
                    	        // StandardLuceneGrammar.g:185:14: ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) )
                    	        {
                    	        Object root_1 = (Object)adaptor.nil();
                    	        root_1 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                    	        , root_1);

                    	        // StandardLuceneGrammar.g:185:26: ^( BOOST $b)
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(BOOST, "BOOST")
                    	        , root_2);

                    	        adaptor.addChild(root_2, stream_b.nextNode());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        // StandardLuceneGrammar.g:185:38: ^( FUZZY NUMBER[\"0.5\"] )
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(FUZZY, "FUZZY")
                    	        , root_2);

                    	        adaptor.addChild(root_2, 
                    	        (Object)adaptor.create(NUMBER, "0.5")
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

                    	default :
                    	    break loop29;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:191:4: ( TILDE -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) ) ) ( (~ ( WS | TILDE | CARAT ) )=>f= NUMBER -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) ) )*
                    {
                    // StandardLuceneGrammar.g:191:4: ( TILDE -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) ) )
                    // StandardLuceneGrammar.g:191:5: TILDE
                    {
                    TILDE67=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1347); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TILDE.add(TILDE67);


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
                    // 191:11: -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) )
                    {
                        // StandardLuceneGrammar.g:191:14: ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:191:26: ^( BOOST )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:191:35: ^( FUZZY NUMBER[\"0.5\"] )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FUZZY, "FUZZY")
                        , root_2);

                        adaptor.addChild(root_2, 
                        (Object)adaptor.create(NUMBER, "0.5")
                        );

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }


                    // StandardLuceneGrammar.g:192:4: ( (~ ( WS | TILDE | CARAT ) )=>f= NUMBER -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) ) )*
                    loop30:
                    do {
                        int alt30=2;
                        int LA30_0 = input.LA(1);

                        if ( (LA30_0==NUMBER) ) {
                            int LA30_2 = input.LA(2);

                            if ( (synpred5_StandardLuceneGrammar()) ) {
                                alt30=1;
                            }


                        }


                        switch (alt30) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:192:5: (~ ( WS | TILDE | CARAT ) )=>f= NUMBER
                    	    {
                    	    f=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_term_modifier1386); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_NUMBER.add(f);


                    	    // AST REWRITE
                    	    // elements: f
                    	    // token labels: f
                    	    // rule labels: retval
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {

                    	    retval.tree = root_0;
                    	    RewriteRuleTokenStream stream_f=new RewriteRuleTokenStream(adaptor,"token f",f);
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    	    root_0 = (Object)adaptor.nil();
                    	    // 192:35: -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) )
                    	    {
                    	        // StandardLuceneGrammar.g:192:38: ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) )
                    	        {
                    	        Object root_1 = (Object)adaptor.nil();
                    	        root_1 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                    	        , root_1);

                    	        // StandardLuceneGrammar.g:192:50: ^( BOOST )
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(BOOST, "BOOST")
                    	        , root_2);

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        // StandardLuceneGrammar.g:192:60: ^( FUZZY ( $f)? )
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(FUZZY, "FUZZY")
                    	        , root_2);

                    	        // StandardLuceneGrammar.g:192:69: ( $f)?
                    	        if ( stream_f.hasNext() ) {
                    	            adaptor.addChild(root_2, stream_f.nextNode());

                    	        }
                    	        stream_f.reset();

                    	        adaptor.addChild(root_1, root_2);
                    	        }

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
    // StandardLuceneGrammar.g:196:1: boost : CARAT NUMBER ;
    public final StandardLuceneGrammarParser.boost_return boost() throws RecognitionException {
        StandardLuceneGrammarParser.boost_return retval = new StandardLuceneGrammarParser.boost_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token CARAT68=null;
        Token NUMBER69=null;

        Object CARAT68_tree=null;
        Object NUMBER69_tree=null;

        try {
            // StandardLuceneGrammar.g:196:7: ( CARAT NUMBER )
            // StandardLuceneGrammar.g:197:2: CARAT NUMBER
            {
            root_0 = (Object)adaptor.nil();


            CARAT68=(Token)match(input,CARAT,FOLLOW_CARAT_in_boost1423); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            CARAT68_tree = 
            (Object)adaptor.create(CARAT68)
            ;
            adaptor.addChild(root_0, CARAT68_tree);
            }

            NUMBER69=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_boost1425); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NUMBER69_tree = 
            (Object)adaptor.create(NUMBER69)
            ;
            adaptor.addChild(root_0, NUMBER69_tree);
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
    // StandardLuceneGrammar.g:200:1: fuzzy : TILDE NUMBER ;
    public final StandardLuceneGrammarParser.fuzzy_return fuzzy() throws RecognitionException {
        StandardLuceneGrammarParser.fuzzy_return retval = new StandardLuceneGrammarParser.fuzzy_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE70=null;
        Token NUMBER71=null;

        Object TILDE70_tree=null;
        Object NUMBER71_tree=null;

        try {
            // StandardLuceneGrammar.g:200:7: ( TILDE NUMBER )
            // StandardLuceneGrammar.g:201:2: TILDE NUMBER
            {
            root_0 = (Object)adaptor.nil();


            TILDE70=(Token)match(input,TILDE,FOLLOW_TILDE_in_fuzzy1437); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            TILDE70_tree = 
            (Object)adaptor.create(TILDE70)
            ;
            adaptor.addChild(root_0, TILDE70_tree);
            }

            NUMBER71=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_fuzzy1439); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NUMBER71_tree = 
            (Object)adaptor.create(NUMBER71)
            ;
            adaptor.addChild(root_0, NUMBER71_tree);
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


    public static class near_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "near"
    // StandardLuceneGrammar.g:205:1: near : ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )? ;
    public final StandardLuceneGrammarParser.near_return near() throws RecognitionException {
        StandardLuceneGrammarParser.near_return retval = new StandardLuceneGrammarParser.near_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token b=null;
        Token NEAR72=null;
        Token char_literal73=null;

        Object b_tree=null;
        Object NEAR72_tree=null;
        Object char_literal73_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // StandardLuceneGrammar.g:205:6: ( ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )? )
            // StandardLuceneGrammar.g:206:2: ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )?
            {
            // StandardLuceneGrammar.g:206:2: ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) )
            // StandardLuceneGrammar.g:206:3: NEAR
            {
            NEAR72=(Token)match(input,NEAR,FOLLOW_NEAR_in_near1453); if (state.failed) return retval; 
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
            // 206:8: -> ^( OPERATOR[\"NEAR 5\"] )
            {
                // StandardLuceneGrammar.g:206:11: ^( OPERATOR[\"NEAR 5\"] )
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


            // StandardLuceneGrammar.g:207:2: ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==55) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // StandardLuceneGrammar.g:207:3: '/' b= NUMBER
                    {
                    char_literal73=(Token)match(input,55,FOLLOW_55_in_near1466); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_55.add(char_literal73);


                    b=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_near1470); if (state.failed) return retval; 
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
                    // 207:16: -> ^( OPERATOR[\"NEAR \" + $b.getText()] )
                    {
                        // StandardLuceneGrammar.g:207:19: ^( OPERATOR[\"NEAR \" + $b.getText()] )
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

    // $ANTLR start synpred1_StandardLuceneGrammar
    public final void synpred1_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:62:2: ( modifier LPAREN ( clauseDefault )+ RPAREN )
        // StandardLuceneGrammar.g:62:3: modifier LPAREN ( clauseDefault )+ RPAREN
        {
        pushFollow(FOLLOW_modifier_in_synpred1_StandardLuceneGrammar362);
        modifier();

        state._fsp--;
        if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar364); if (state.failed) return ;

        // StandardLuceneGrammar.g:62:19: ( clauseDefault )+
        int cnt33=0;
        loop33:
        do {
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( ((LA33_0 >= LBRACK && LA33_0 <= MINUS)||LA33_0==NUMBER||(LA33_0 >= PHRASE && LA33_0 <= PLUS)||LA33_0==QMARK||(LA33_0 >= STAR && LA33_0 <= TERM_TRUNCATED)) ) {
                alt33=1;
            }


            switch (alt33) {
        	case 1 :
        	    // StandardLuceneGrammar.g:62:19: clauseDefault
        	    {
        	    pushFollow(FOLLOW_clauseDefault_in_synpred1_StandardLuceneGrammar366);
        	    clauseDefault();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt33 >= 1 ) break loop33;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(33, input);
                    throw eee;
            }
            cnt33++;
        } while (true);


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar369); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred1_StandardLuceneGrammar

    // $ANTLR start synpred2_StandardLuceneGrammar
    public final void synpred2_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:63:4: ( LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER )
        // StandardLuceneGrammar.g:63:5: LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar425); if (state.failed) return ;

        // StandardLuceneGrammar.g:63:12: ( clauseDefault )+
        int cnt34=0;
        loop34:
        do {
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( ((LA34_0 >= LBRACK && LA34_0 <= MINUS)||LA34_0==NUMBER||(LA34_0 >= PHRASE && LA34_0 <= PLUS)||LA34_0==QMARK||(LA34_0 >= STAR && LA34_0 <= TERM_TRUNCATED)) ) {
                alt34=1;
            }


            switch (alt34) {
        	case 1 :
        	    // StandardLuceneGrammar.g:63:12: clauseDefault
        	    {
        	    pushFollow(FOLLOW_clauseDefault_in_synpred2_StandardLuceneGrammar427);
        	    clauseDefault();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt34 >= 1 ) break loop34;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(34, input);
                    throw eee;
            }
            cnt34++;
        } while (true);


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar430); if (state.failed) return ;

        match(input,CARAT,FOLLOW_CARAT_in_synpred2_StandardLuceneGrammar432); if (state.failed) return ;

        match(input,NUMBER,FOLLOW_NUMBER_in_synpred2_StandardLuceneGrammar434); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_StandardLuceneGrammar

    // $ANTLR start synpred3_StandardLuceneGrammar
    public final void synpred3_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:64:4: ( LPAREN )
        // StandardLuceneGrammar.g:64:5: LPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar489); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred3_StandardLuceneGrammar

    // $ANTLR start synpred4_StandardLuceneGrammar
    public final void synpred4_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:184:3: ( TILDE NUMBER )
        // StandardLuceneGrammar.g:184:4: TILDE NUMBER
        {
        match(input,TILDE,FOLLOW_TILDE_in_synpred4_StandardLuceneGrammar1267); if (state.failed) return ;

        match(input,NUMBER,FOLLOW_NUMBER_in_synpred4_StandardLuceneGrammar1269); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred4_StandardLuceneGrammar

    // $ANTLR start synpred5_StandardLuceneGrammar
    public final void synpred5_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:192:5: (~ ( WS | TILDE | CARAT ) )
        // StandardLuceneGrammar.g:
        {
        if ( (input.LA(1) >= ADDED && input.LA(1) <= BOOST)||(input.LA(1) >= CLAUSE && input.LA(1) <= TERM_TRUNCATED)||(input.LA(1) >= TMODIFIER && input.LA(1) <= VBAR)||input.LA(1)==55 ) {
            input.consume();
            state.errorRecovery=false;
            state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }

    }
    // $ANTLR end synpred5_StandardLuceneGrammar

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
    public final boolean synpred5_StandardLuceneGrammar() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_StandardLuceneGrammar_fragment(); // can never throw exception
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


 

    public static final BitSet FOLLOW_clauseDefault_in_mainQ153 = new BitSet(new long[]{0x0001C00B901E0002L});
    public static final BitSet FOLLOW_clauseStrongest_in_clauseDefault186 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_OR_in_clauseDefault195 = new BitSet(new long[]{0x0001C00B901E0000L});
    public static final BitSet FOLLOW_clauseStrongest_in_clauseDefault199 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_clauseStrong_in_clauseStrongest228 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_AND_in_clauseStrongest238 = new BitSet(new long[]{0x0001C00B901E0000L});
    public static final BitSet FOLLOW_clauseStrong_in_clauseStrongest242 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_clauseWeak_in_clauseStrong273 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_NOT_in_clauseStrong282 = new BitSet(new long[]{0x0001C00B901E0000L});
    public static final BitSet FOLLOW_clauseWeak_in_clauseStrong286 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_primaryClause_in_clauseWeak317 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_near_in_clauseWeak326 = new BitSet(new long[]{0x0001C00B901E0000L});
    public static final BitSet FOLLOW_primaryClause_in_clauseWeak330 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_modifier_in_primaryClause374 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryClause377 = new BitSet(new long[]{0x0001C00B901E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_primaryClause379 = new BitSet(new long[]{0x0001D00B901E0000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryClause382 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_CARAT_in_primaryClause385 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NUMBER_in_primaryClause387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_primaryClause438 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryClause441 = new BitSet(new long[]{0x0001C00B901E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_primaryClause443 = new BitSet(new long[]{0x0001D00B901E0000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryClause446 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_CARAT_in_primaryClause449 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NUMBER_in_primaryClause451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryClause493 = new BitSet(new long[]{0x0001C00B901E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_primaryClause495 = new BitSet(new long[]{0x0001D00B901E0000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryClause498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_primaryClause508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom530 = new BitSet(new long[]{0x0000C00000000000L});
    public static final BitSet FOLLOW_field_in_atom533 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_multi_value_in_atom535 = new BitSet(new long[]{0x0002000000000202L});
    public static final BitSet FOLLOW_term_modifier_in_atom537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom571 = new BitSet(new long[]{0x0001C00990060000L});
    public static final BitSet FOLLOW_field_in_atom574 = new BitSet(new long[]{0x0001C00990060000L});
    public static final BitSet FOLLOW_value_in_atom577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_NORMAL_in_field629 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_COLON_in_field631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_field647 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_COLON_in_field649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_in_in_value676 = new BitSet(new long[]{0x0002000000000202L});
    public static final BitSet FOLLOW_range_term_ex_in_value689 = new BitSet(new long[]{0x0002000000000202L});
    public static final BitSet FOLLOW_truncated_in_value703 = new BitSet(new long[]{0x0002000000000202L});
    public static final BitSet FOLLOW_normal_in_value717 = new BitSet(new long[]{0x0002000000000202L});
    public static final BitSet FOLLOW_quoted_in_value730 = new BitSet(new long[]{0x0002000000000202L});
    public static final BitSet FOLLOW_quoted_truncated_in_value743 = new BitSet(new long[]{0x0002000000000202L});
    public static final BitSet FOLLOW_STAR_in_value756 = new BitSet(new long[]{0x0002000000000202L});
    public static final BitSet FOLLOW_QMARK_in_value769 = new BitSet(new long[]{0x0002000000000202L});
    public static final BitSet FOLLOW_term_modifier_in_value783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACK_in_range_term_in816 = new BitSet(new long[]{0x0001C00190000000L});
    public static final BitSet FOLLOW_range_value_in_range_term_in828 = new BitSet(new long[]{0x0009C40190000000L});
    public static final BitSet FOLLOW_TO_in_range_term_in851 = new BitSet(new long[]{0x0001C00190000000L});
    public static final BitSet FOLLOW_range_value_in_range_term_in856 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_RBRACK_in_range_term_in877 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LCURLY_in_range_term_ex897 = new BitSet(new long[]{0x0001C00190000000L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex910 = new BitSet(new long[]{0x0009C80190000000L});
    public static final BitSet FOLLOW_TO_in_range_term_ex933 = new BitSet(new long[]{0x0001C00190000000L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex938 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_RCURLY_in_range_term_ex959 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_range_value973 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_range_value986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_range_value999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_range_value1012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_range_value1025 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_multi_value1046 = new BitSet(new long[]{0x0001C00B90160000L});
    public static final BitSet FOLLOW_mterm_in_multi_value1048 = new BitSet(new long[]{0x0001D00B90160000L});
    public static final BitSet FOLLOW_RPAREN_in_multi_value1051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_mterm1074 = new BitSet(new long[]{0x0001C00990060000L});
    public static final BitSet FOLLOW_value_in_mterm1077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_TRUNCATED_in_truncated1145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_in_quoted1172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1231 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NUMBER_in_term_modifier1235 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1273 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NUMBER_in_term_modifier1277 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1304 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1347 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_NUMBER_in_term_modifier1386 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_CARAT_in_boost1423 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NUMBER_in_boost1425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_fuzzy1437 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NUMBER_in_fuzzy1439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_near1453 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_near1466 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NUMBER_in_near1470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_synpred1_StandardLuceneGrammar362 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar364 = new BitSet(new long[]{0x0001C00B901E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_synpred1_StandardLuceneGrammar366 = new BitSet(new long[]{0x0001D00B901E0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar425 = new BitSet(new long[]{0x0001C00B901E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_synpred2_StandardLuceneGrammar427 = new BitSet(new long[]{0x0001D00B901E0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar430 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_CARAT_in_synpred2_StandardLuceneGrammar432 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NUMBER_in_synpred2_StandardLuceneGrammar434 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_synpred4_StandardLuceneGrammar1267 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NUMBER_in_synpred4_StandardLuceneGrammar1269 = new BitSet(new long[]{0x0000000000000002L});

}