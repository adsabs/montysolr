// $ANTLR 3.4 StandardLuceneGrammar.g 2011-10-25 21:31:46

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ADDED", "AMPER", "AND", "ATOM", "BOOST", "CARAT", "CLAUSE", "COLON", "DQUOTE", "ESC_CHAR", "FIELD", "FUZZY", "INT", "LBRACK", "LCURLY", "LPAREN", "MINUS", "MODIFIER", "MULTIATOM", "NEAR", "NORMAL_CHAR", "NOT", "NUMBER", "OPERATOR", "OR", "PHRASE", "PHRASE_ANYTHING", "PLUS", "QANYTHING", "QMARK", "QNORMAL", "QPHRASE", "QPHRASETRUNC", "QRANGEEX", "QRANGEIN", "QTRUNCATED", "RBRACK", "RCURLY", "RPAREN", "SQUOTE", "STAR", "TERM_NORMAL", "TERM_TRUNCATED", "TILDE", "TMODIFIER", "TO", "VALUE", "VBAR", "WS", "'/'"
    };

    public static final int EOF=-1;
    public static final int T__53=53;
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
    public static final int NEAR=23;
    public static final int NORMAL_CHAR=24;
    public static final int NOT=25;
    public static final int NUMBER=26;
    public static final int OPERATOR=27;
    public static final int OR=28;
    public static final int PHRASE=29;
    public static final int PHRASE_ANYTHING=30;
    public static final int PLUS=31;
    public static final int QANYTHING=32;
    public static final int QMARK=33;
    public static final int QNORMAL=34;
    public static final int QPHRASE=35;
    public static final int QPHRASETRUNC=36;
    public static final int QRANGEEX=37;
    public static final int QRANGEIN=38;
    public static final int QTRUNCATED=39;
    public static final int RBRACK=40;
    public static final int RCURLY=41;
    public static final int RPAREN=42;
    public static final int SQUOTE=43;
    public static final int STAR=44;
    public static final int TERM_NORMAL=45;
    public static final int TERM_TRUNCATED=46;
    public static final int TILDE=47;
    public static final int TMODIFIER=48;
    public static final int TO=49;
    public static final int VALUE=50;
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
    // StandardLuceneGrammar.g:36:1: mainQ : ( clauseDefault )+ -> ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) ;
    public final StandardLuceneGrammarParser.mainQ_return mainQ() throws RecognitionException {
        StandardLuceneGrammarParser.mainQ_return retval = new StandardLuceneGrammarParser.mainQ_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.clauseDefault_return clauseDefault1 =null;


        RewriteRuleSubtreeStream stream_clauseDefault=new RewriteRuleSubtreeStream(adaptor,"rule clauseDefault");
        try {
            // StandardLuceneGrammar.g:36:7: ( ( clauseDefault )+ -> ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) )
            // StandardLuceneGrammar.g:37:2: ( clauseDefault )+
            {
            // StandardLuceneGrammar.g:37:2: ( clauseDefault )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= LBRACK && LA1_0 <= MINUS)||LA1_0==NUMBER||(LA1_0 >= PHRASE && LA1_0 <= PLUS)||(LA1_0 >= TERM_NORMAL && LA1_0 <= TERM_TRUNCATED)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // StandardLuceneGrammar.g:37:2: clauseDefault
            	    {
            	    pushFollow(FOLLOW_clauseDefault_in_mainQ143);
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
            // 37:17: -> ^( OPERATOR[\"AND\"] ( clauseDefault )+ )
            {
                // StandardLuceneGrammar.g:37:20: ^( OPERATOR[\"AND\"] ( clauseDefault )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OPERATOR, "AND")
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
    // StandardLuceneGrammar.g:41:1: clauseDefault : (first= clauseStrongest -> $first) ( OR others= clauseStrongest -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ ) )* ;
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
            // StandardLuceneGrammar.g:42:3: ( (first= clauseStrongest -> $first) ( OR others= clauseStrongest -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ ) )* )
            // StandardLuceneGrammar.g:42:5: (first= clauseStrongest -> $first) ( OR others= clauseStrongest -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ ) )*
            {
            // StandardLuceneGrammar.g:42:5: (first= clauseStrongest -> $first)
            // StandardLuceneGrammar.g:42:6: first= clauseStrongest
            {
            pushFollow(FOLLOW_clauseStrongest_in_clauseDefault176);
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
            // 42:28: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:42:39: ( OR others= clauseStrongest -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==OR) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // StandardLuceneGrammar.g:42:40: OR others= clauseStrongest
            	    {
            	    OR2=(Token)match(input,OR,FOLLOW_OR_in_clauseDefault185); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_OR.add(OR2);


            	    pushFollow(FOLLOW_clauseStrongest_in_clauseDefault189);
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
            	    // 42:66: -> ^( OPERATOR[\"OR\"] ( clauseStrongest )+ )
            	    {
            	        // StandardLuceneGrammar.g:42:69: ^( OPERATOR[\"OR\"] ( clauseStrongest )+ )
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
    // StandardLuceneGrammar.g:45:1: clauseStrongest : (first= clauseStrong -> $first) ( AND others= clauseStrong -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ ) )* ;
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
            // StandardLuceneGrammar.g:46:3: ( (first= clauseStrong -> $first) ( AND others= clauseStrong -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ ) )* )
            // StandardLuceneGrammar.g:46:5: (first= clauseStrong -> $first) ( AND others= clauseStrong -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ ) )*
            {
            // StandardLuceneGrammar.g:46:5: (first= clauseStrong -> $first)
            // StandardLuceneGrammar.g:46:6: first= clauseStrong
            {
            pushFollow(FOLLOW_clauseStrong_in_clauseStrongest218);
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
            // 46:26: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:46:37: ( AND others= clauseStrong -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==AND) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // StandardLuceneGrammar.g:46:38: AND others= clauseStrong
            	    {
            	    AND3=(Token)match(input,AND,FOLLOW_AND_in_clauseStrongest228); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_AND.add(AND3);


            	    pushFollow(FOLLOW_clauseStrong_in_clauseStrongest232);
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
            	    // 46:62: -> ^( OPERATOR[\"AND\"] ( clauseStrong )+ )
            	    {
            	        // StandardLuceneGrammar.g:46:65: ^( OPERATOR[\"AND\"] ( clauseStrong )+ )
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
    // StandardLuceneGrammar.g:49:1: clauseStrong : (first= clauseWeak -> $first) ( NOT others= clauseWeak -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ ) )* ;
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
            // StandardLuceneGrammar.g:50:3: ( (first= clauseWeak -> $first) ( NOT others= clauseWeak -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ ) )* )
            // StandardLuceneGrammar.g:50:5: (first= clauseWeak -> $first) ( NOT others= clauseWeak -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ ) )*
            {
            // StandardLuceneGrammar.g:50:5: (first= clauseWeak -> $first)
            // StandardLuceneGrammar.g:50:6: first= clauseWeak
            {
            pushFollow(FOLLOW_clauseWeak_in_clauseStrong263);
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
            // 50:23: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:50:34: ( NOT others= clauseWeak -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==NOT) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // StandardLuceneGrammar.g:50:35: NOT others= clauseWeak
            	    {
            	    NOT4=(Token)match(input,NOT,FOLLOW_NOT_in_clauseStrong272); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_NOT.add(NOT4);


            	    pushFollow(FOLLOW_clauseWeak_in_clauseStrong276);
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
            	    // 50:57: -> ^( OPERATOR[\"NOT\"] ( clauseWeak )+ )
            	    {
            	        // StandardLuceneGrammar.g:50:60: ^( OPERATOR[\"NOT\"] ( clauseWeak )+ )
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
    // StandardLuceneGrammar.g:53:1: clauseWeak : (first= primaryClause -> $first) ( near others= primaryClause -> ^( near ( primaryClause )+ ) )* ;
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
            // StandardLuceneGrammar.g:54:3: ( (first= primaryClause -> $first) ( near others= primaryClause -> ^( near ( primaryClause )+ ) )* )
            // StandardLuceneGrammar.g:54:5: (first= primaryClause -> $first) ( near others= primaryClause -> ^( near ( primaryClause )+ ) )*
            {
            // StandardLuceneGrammar.g:54:5: (first= primaryClause -> $first)
            // StandardLuceneGrammar.g:54:6: first= primaryClause
            {
            pushFollow(FOLLOW_primaryClause_in_clauseWeak307);
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
            // 54:26: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // StandardLuceneGrammar.g:54:37: ( near others= primaryClause -> ^( near ( primaryClause )+ ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==NEAR) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // StandardLuceneGrammar.g:54:38: near others= primaryClause
            	    {
            	    pushFollow(FOLLOW_near_in_clauseWeak316);
            	    near5=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near5.getTree());

            	    pushFollow(FOLLOW_primaryClause_in_clauseWeak320);
            	    others=primaryClause();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_primaryClause.add(others.getTree());

            	    // AST REWRITE
            	    // elements: primaryClause, near
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 54:64: -> ^( near ( primaryClause )+ )
            	    {
            	        // StandardLuceneGrammar.g:54:67: ^( near ( primaryClause )+ )
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
    // StandardLuceneGrammar.g:57:1: primaryClause : ( ( modifier LPAREN ( clauseDefault )+ RPAREN )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) ) | ( LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) ) | ( LPAREN )=> LPAREN ( clauseDefault )+ RPAREN -> ( clauseDefault )+ | atom );
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
            // StandardLuceneGrammar.g:58:2: ( ( modifier LPAREN ( clauseDefault )+ RPAREN )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) ) | ( LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) ) | ( LPAREN )=> LPAREN ( clauseDefault )+ RPAREN -> ( clauseDefault )+ | atom )
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
                    // StandardLuceneGrammar.g:60:2: ( modifier LPAREN ( clauseDefault )+ RPAREN )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )?
                    {
                    // StandardLuceneGrammar.g:60:45: ( modifier )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==MINUS||LA6_0==PLUS) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // StandardLuceneGrammar.g:60:45: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_primaryClause364);
                            modifier6=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier6.getTree());

                            }
                            break;

                    }


                    LPAREN7=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_primaryClause367); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN7);


                    // StandardLuceneGrammar.g:60:62: ( clauseDefault )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0 >= LBRACK && LA7_0 <= MINUS)||LA7_0==NUMBER||(LA7_0 >= PHRASE && LA7_0 <= PLUS)||(LA7_0 >= TERM_NORMAL && LA7_0 <= TERM_TRUNCATED)) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:60:62: clauseDefault
                    	    {
                    	    pushFollow(FOLLOW_clauseDefault_in_primaryClause369);
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


                    RPAREN9=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_primaryClause372); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN9);


                    // StandardLuceneGrammar.g:60:84: ( CARAT NUMBER )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==CARAT) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // StandardLuceneGrammar.g:60:85: CARAT NUMBER
                            {
                            CARAT10=(Token)match(input,CARAT,FOLLOW_CARAT_in_primaryClause375); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CARAT.add(CARAT10);


                            NUMBER11=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_primaryClause377); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER11);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: NUMBER, modifier, clauseDefault
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 60:100: -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) )
                    {
                        // StandardLuceneGrammar.g:60:103: ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // StandardLuceneGrammar.g:60:112: ^( MODIFIER ( modifier )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:60:123: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:60:134: ^( BOOST ( NUMBER )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_2);

                        // StandardLuceneGrammar.g:60:142: ( NUMBER )?
                        if ( stream_NUMBER.hasNext() ) {
                            adaptor.addChild(root_2, 
                            stream_NUMBER.nextNode()
                            );

                        }
                        stream_NUMBER.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:60:151: ^( OPERATOR[\"AND\"] ( clauseDefault )+ )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(OPERATOR, "AND")
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
                    // StandardLuceneGrammar.g:61:4: ( LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER )=> ( modifier )? LPAREN ( clauseDefault )+ RPAREN ( CARAT NUMBER )?
                    {
                    // StandardLuceneGrammar.g:61:50: ( modifier )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==MINUS||LA9_0==PLUS) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // StandardLuceneGrammar.g:61:50: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_primaryClause428);
                            modifier12=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier12.getTree());

                            }
                            break;

                    }


                    LPAREN13=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_primaryClause431); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN13);


                    // StandardLuceneGrammar.g:61:67: ( clauseDefault )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0 >= LBRACK && LA10_0 <= MINUS)||LA10_0==NUMBER||(LA10_0 >= PHRASE && LA10_0 <= PLUS)||(LA10_0 >= TERM_NORMAL && LA10_0 <= TERM_TRUNCATED)) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:61:67: clauseDefault
                    	    {
                    	    pushFollow(FOLLOW_clauseDefault_in_primaryClause433);
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


                    RPAREN15=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_primaryClause436); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN15);


                    // StandardLuceneGrammar.g:61:89: ( CARAT NUMBER )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==CARAT) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // StandardLuceneGrammar.g:61:90: CARAT NUMBER
                            {
                            CARAT16=(Token)match(input,CARAT,FOLLOW_CARAT_in_primaryClause439); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CARAT.add(CARAT16);


                            NUMBER17=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_primaryClause441); if (state.failed) return retval; 
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
                    // 61:105: -> ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) )
                    {
                        // StandardLuceneGrammar.g:61:108: ^( CLAUSE ^( MODIFIER ( modifier )? ) ^( BOOST ( NUMBER )? ) ^( OPERATOR[\"AND\"] ( clauseDefault )+ ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // StandardLuceneGrammar.g:61:117: ^( MODIFIER ( modifier )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:61:128: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:61:139: ^( BOOST ( NUMBER )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_2);

                        // StandardLuceneGrammar.g:61:147: ( NUMBER )?
                        if ( stream_NUMBER.hasNext() ) {
                            adaptor.addChild(root_2, 
                            stream_NUMBER.nextNode()
                            );

                        }
                        stream_NUMBER.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:61:156: ^( OPERATOR[\"AND\"] ( clauseDefault )+ )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(OPERATOR, "AND")
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
                    // StandardLuceneGrammar.g:62:4: ( LPAREN )=> LPAREN ( clauseDefault )+ RPAREN
                    {
                    LPAREN18=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_primaryClause483); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN18);


                    // StandardLuceneGrammar.g:62:22: ( clauseDefault )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0 >= LBRACK && LA12_0 <= MINUS)||LA12_0==NUMBER||(LA12_0 >= PHRASE && LA12_0 <= PLUS)||(LA12_0 >= TERM_NORMAL && LA12_0 <= TERM_TRUNCATED)) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:62:22: clauseDefault
                    	    {
                    	    pushFollow(FOLLOW_clauseDefault_in_primaryClause485);
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


                    RPAREN20=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_primaryClause488); if (state.failed) return retval; 
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
                    // 62:44: -> ( clauseDefault )+
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
                    // StandardLuceneGrammar.g:63:4: atom
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_atom_in_primaryClause498);
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
    // StandardLuceneGrammar.g:67:1: atom : ( ( modifier )? field multi_value -> ^( MULTIATOM ^( MODIFIER ( modifier )? ) ^( FIELD field ) ^( VALUE multi_value ) ) | ( modifier )? ( field )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( FIELD ( field )? ) ^( VALUE value ) ) );
    public final StandardLuceneGrammarParser.atom_return atom() throws RecognitionException {
        StandardLuceneGrammarParser.atom_return retval = new StandardLuceneGrammarParser.atom_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.modifier_return modifier22 =null;

        StandardLuceneGrammarParser.field_return field23 =null;

        StandardLuceneGrammarParser.multi_value_return multi_value24 =null;

        StandardLuceneGrammarParser.modifier_return modifier25 =null;

        StandardLuceneGrammarParser.field_return field26 =null;

        StandardLuceneGrammarParser.value_return value27 =null;


        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_multi_value=new RewriteRuleSubtreeStream(adaptor,"rule multi_value");
        try {
            // StandardLuceneGrammar.g:68:2: ( ( modifier )? field multi_value -> ^( MULTIATOM ^( MODIFIER ( modifier )? ) ^( FIELD field ) ^( VALUE multi_value ) ) | ( modifier )? ( field )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( FIELD ( field )? ) ^( VALUE value ) ) )
            int alt17=2;
            switch ( input.LA(1) ) {
            case MINUS:
            case PLUS:
                {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==TERM_NORMAL) ) {
                    int LA17_2 = input.LA(3);

                    if ( (LA17_2==COLON) ) {
                        int LA17_4 = input.LA(4);

                        if ( (LA17_4==LPAREN) ) {
                            alt17=1;
                        }
                        else if ( ((LA17_4 >= LBRACK && LA17_4 <= LCURLY)||LA17_4==NUMBER||(LA17_4 >= PHRASE && LA17_4 <= PHRASE_ANYTHING)||(LA17_4 >= TERM_NORMAL && LA17_4 <= TERM_TRUNCATED)) ) {
                            alt17=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 17, 4, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA17_2==EOF||LA17_2==AND||LA17_2==CARAT||(LA17_2 >= LBRACK && LA17_2 <= MINUS)||LA17_2==NEAR||(LA17_2 >= NOT && LA17_2 <= NUMBER)||(LA17_2 >= OR && LA17_2 <= PLUS)||LA17_2==RPAREN||(LA17_2 >= TERM_NORMAL && LA17_2 <= TILDE)) ) {
                        alt17=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 2, input);

                        throw nvae;

                    }
                }
                else if ( ((LA17_1 >= LBRACK && LA17_1 <= LCURLY)||LA17_1==NUMBER||(LA17_1 >= PHRASE && LA17_1 <= PHRASE_ANYTHING)||LA17_1==TERM_TRUNCATED) ) {
                    alt17=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;

                }
                }
                break;
            case TERM_NORMAL:
                {
                int LA17_2 = input.LA(2);

                if ( (LA17_2==COLON) ) {
                    int LA17_4 = input.LA(3);

                    if ( (LA17_4==LPAREN) ) {
                        alt17=1;
                    }
                    else if ( ((LA17_4 >= LBRACK && LA17_4 <= LCURLY)||LA17_4==NUMBER||(LA17_4 >= PHRASE && LA17_4 <= PHRASE_ANYTHING)||(LA17_4 >= TERM_NORMAL && LA17_4 <= TERM_TRUNCATED)) ) {
                        alt17=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 4, input);

                        throw nvae;

                    }
                }
                else if ( (LA17_2==EOF||LA17_2==AND||LA17_2==CARAT||(LA17_2 >= LBRACK && LA17_2 <= MINUS)||LA17_2==NEAR||(LA17_2 >= NOT && LA17_2 <= NUMBER)||(LA17_2 >= OR && LA17_2 <= PLUS)||LA17_2==RPAREN||(LA17_2 >= TERM_NORMAL && LA17_2 <= TILDE)) ) {
                    alt17=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 2, input);

                    throw nvae;

                }
                }
                break;
            case LBRACK:
            case LCURLY:
            case NUMBER:
            case PHRASE:
            case PHRASE_ANYTHING:
            case TERM_TRUNCATED:
                {
                alt17=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }

            switch (alt17) {
                case 1 :
                    // StandardLuceneGrammar.g:69:2: ( modifier )? field multi_value
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
                            pushFollow(FOLLOW_modifier_in_atom520);
                            modifier22=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier22.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_field_in_atom523);
                    field23=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field23.getTree());

                    pushFollow(FOLLOW_multi_value_in_atom525);
                    multi_value24=multi_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_multi_value.add(multi_value24.getTree());

                    // AST REWRITE
                    // elements: field, multi_value, modifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 69:30: -> ^( MULTIATOM ^( MODIFIER ( modifier )? ) ^( FIELD field ) ^( VALUE multi_value ) )
                    {
                        // StandardLuceneGrammar.g:69:33: ^( MULTIATOM ^( MODIFIER ( modifier )? ) ^( FIELD field ) ^( VALUE multi_value ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MULTIATOM, "MULTIATOM")
                        , root_1);

                        // StandardLuceneGrammar.g:69:45: ^( MODIFIER ( modifier )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:69:56: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:69:67: ^( FIELD field )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_2);

                        adaptor.addChild(root_2, stream_field.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:69:82: ^( VALUE multi_value )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(VALUE, "VALUE")
                        , root_2);

                        adaptor.addChild(root_2, stream_multi_value.nextTree());

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
                    // StandardLuceneGrammar.g:70:4: ( modifier )? ( field )? value
                    {
                    // StandardLuceneGrammar.g:70:4: ( modifier )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==MINUS||LA15_0==PLUS) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // StandardLuceneGrammar.g:70:4: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom555);
                            modifier25=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier25.getTree());

                            }
                            break;

                    }


                    // StandardLuceneGrammar.g:70:14: ( field )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==TERM_NORMAL) ) {
                        int LA16_1 = input.LA(2);

                        if ( (LA16_1==COLON) ) {
                            alt16=1;
                        }
                    }
                    switch (alt16) {
                        case 1 :
                            // StandardLuceneGrammar.g:70:14: field
                            {
                            pushFollow(FOLLOW_field_in_atom558);
                            field26=field();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_field.add(field26.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_value_in_atom561);
                    value27=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value27.getTree());

                    // AST REWRITE
                    // elements: modifier, value, field
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 70:27: -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( FIELD ( field )? ) ^( VALUE value ) )
                    {
                        // StandardLuceneGrammar.g:70:30: ^( ATOM ^( MODIFIER ( modifier )? ) ^( FIELD ( field )? ) ^( VALUE value ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(ATOM, "ATOM")
                        , root_1);

                        // StandardLuceneGrammar.g:70:37: ^( MODIFIER ( modifier )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // StandardLuceneGrammar.g:70:48: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:70:59: ^( FIELD ( field )? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_2);

                        // StandardLuceneGrammar.g:70:67: ( field )?
                        if ( stream_field.hasNext() ) {
                            adaptor.addChild(root_2, stream_field.nextTree());

                        }
                        stream_field.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:70:75: ^( VALUE value )
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
    // StandardLuceneGrammar.g:74:1: field : TERM_NORMAL COLON -> TERM_NORMAL ;
    public final StandardLuceneGrammarParser.field_return field() throws RecognitionException {
        StandardLuceneGrammarParser.field_return retval = new StandardLuceneGrammarParser.field_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_NORMAL28=null;
        Token COLON29=null;

        Object TERM_NORMAL28_tree=null;
        Object COLON29_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_TERM_NORMAL=new RewriteRuleTokenStream(adaptor,"token TERM_NORMAL");

        try {
            // StandardLuceneGrammar.g:75:2: ( TERM_NORMAL COLON -> TERM_NORMAL )
            // StandardLuceneGrammar.g:76:2: TERM_NORMAL COLON
            {
            TERM_NORMAL28=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_field609); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL28);


            COLON29=(Token)match(input,COLON,FOLLOW_COLON_in_field611); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(COLON29);


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
            // 76:20: -> TERM_NORMAL
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
    // StandardLuceneGrammar.g:79:1: value : ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) ) ( term_modifier )? -> ( term_modifier )? $value;
    public final StandardLuceneGrammarParser.value_return value() throws RecognitionException {
        StandardLuceneGrammarParser.value_return retval = new StandardLuceneGrammarParser.value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.range_term_in_return range_term_in30 =null;

        StandardLuceneGrammarParser.range_term_ex_return range_term_ex31 =null;

        StandardLuceneGrammarParser.normal_return normal32 =null;

        StandardLuceneGrammarParser.truncated_return truncated33 =null;

        StandardLuceneGrammarParser.quoted_return quoted34 =null;

        StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated35 =null;

        StandardLuceneGrammarParser.term_modifier_return term_modifier36 =null;


        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_range_term_ex=new RewriteRuleSubtreeStream(adaptor,"rule range_term_ex");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_range_term_in=new RewriteRuleSubtreeStream(adaptor,"rule range_term_in");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        try {
            // StandardLuceneGrammar.g:80:2: ( ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) ) ( term_modifier )? -> ( term_modifier )? $value)
            // StandardLuceneGrammar.g:81:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) ) ( term_modifier )?
            {
            // StandardLuceneGrammar.g:81:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) )
            int alt18=6;
            switch ( input.LA(1) ) {
            case LBRACK:
                {
                alt18=1;
                }
                break;
            case LCURLY:
                {
                alt18=2;
                }
                break;
            case NUMBER:
            case TERM_NORMAL:
                {
                alt18=3;
                }
                break;
            case TERM_TRUNCATED:
                {
                alt18=4;
                }
                break;
            case PHRASE:
                {
                alt18=5;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt18=6;
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
                    // StandardLuceneGrammar.g:82:2: range_term_in
                    {
                    pushFollow(FOLLOW_range_term_in_in_value634);
                    range_term_in30=range_term_in();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_in.add(range_term_in30.getTree());

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
                    // 82:16: -> ^( QRANGEIN range_term_in )
                    {
                        // StandardLuceneGrammar.g:82:19: ^( QRANGEIN range_term_in )
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
                    // StandardLuceneGrammar.g:83:4: range_term_ex
                    {
                    pushFollow(FOLLOW_range_term_ex_in_value647);
                    range_term_ex31=range_term_ex();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_ex.add(range_term_ex31.getTree());

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
                    // 83:18: -> ^( QRANGEEX range_term_ex )
                    {
                        // StandardLuceneGrammar.g:83:21: ^( QRANGEEX range_term_ex )
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
                    // StandardLuceneGrammar.g:84:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_value661);
                    normal32=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal32.getTree());

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
                    // 84:11: -> ^( QNORMAL normal )
                    {
                        // StandardLuceneGrammar.g:84:14: ^( QNORMAL normal )
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
                    // StandardLuceneGrammar.g:85:4: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_value674);
                    truncated33=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated33.getTree());

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
                    // 85:14: -> ^( QTRUNCATED truncated )
                    {
                        // StandardLuceneGrammar.g:85:17: ^( QTRUNCATED truncated )
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
                    // StandardLuceneGrammar.g:86:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_value687);
                    quoted34=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted34.getTree());

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
                    // 86:11: -> ^( QPHRASE quoted )
                    {
                        // StandardLuceneGrammar.g:86:14: ^( QPHRASE quoted )
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
                    // StandardLuceneGrammar.g:87:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_value700);
                    quoted_truncated35=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated35.getTree());

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
                    // 87:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // StandardLuceneGrammar.g:87:24: ^( QPHRASETRUNC quoted_truncated )
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

            }


            // StandardLuceneGrammar.g:89:2: ( term_modifier )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==CARAT||LA19_0==TILDE) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // StandardLuceneGrammar.g:89:2: term_modifier
                    {
                    pushFollow(FOLLOW_term_modifier_in_value714);
                    term_modifier36=term_modifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier36.getTree());

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
            // 89:17: -> ( term_modifier )? $value
            {
                // StandardLuceneGrammar.g:89:20: ( term_modifier )?
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
    // StandardLuceneGrammar.g:94:1: range_term_in : LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK ;
    public final StandardLuceneGrammarParser.range_term_in_return range_term_in() throws RecognitionException {
        StandardLuceneGrammarParser.range_term_in_return retval = new StandardLuceneGrammarParser.range_term_in_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LBRACK37=null;
        Token TO38=null;
        Token RBRACK39=null;
        StandardLuceneGrammarParser.range_value_return a =null;

        StandardLuceneGrammarParser.range_value_return b =null;


        Object LBRACK37_tree=null;
        Object TO38_tree=null;
        Object RBRACK39_tree=null;
        RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
        RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // StandardLuceneGrammar.g:95:2: ( LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK )
            // StandardLuceneGrammar.g:96:8: LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK
            {
            LBRACK37=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_range_term_in747); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK37);


            // StandardLuceneGrammar.g:97:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // StandardLuceneGrammar.g:97:9: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_in759);
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
            // 97:23: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // StandardLuceneGrammar.g:97:38: ^( QANYTHING QANYTHING[\"*\"] )
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


            // StandardLuceneGrammar.g:98:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==NUMBER||(LA21_0 >= PHRASE && LA21_0 <= PHRASE_ANYTHING)||(LA21_0 >= STAR && LA21_0 <= TERM_TRUNCATED)||LA21_0==TO) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // StandardLuceneGrammar.g:98:10: ( TO )? b= range_value
                    {
                    // StandardLuceneGrammar.g:98:10: ( TO )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==TO) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // StandardLuceneGrammar.g:98:10: TO
                            {
                            TO38=(Token)match(input,TO,FOLLOW_TO_in_range_term_in782); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO38);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_in787);
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
                    // 98:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // StandardLuceneGrammar.g:98:35: ( $b)?
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


            RBRACK39=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_range_term_in808); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACK.add(RBRACK39);


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
    // StandardLuceneGrammar.g:103:1: range_term_ex : LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY ;
    public final StandardLuceneGrammarParser.range_term_ex_return range_term_ex() throws RecognitionException {
        StandardLuceneGrammarParser.range_term_ex_return retval = new StandardLuceneGrammarParser.range_term_ex_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LCURLY40=null;
        Token TO41=null;
        Token RCURLY42=null;
        StandardLuceneGrammarParser.range_value_return a =null;

        StandardLuceneGrammarParser.range_value_return b =null;


        Object LCURLY40_tree=null;
        Object TO41_tree=null;
        Object RCURLY42_tree=null;
        RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // StandardLuceneGrammar.g:104:2: ( LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY )
            // StandardLuceneGrammar.g:105:8: LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY
            {
            LCURLY40=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_range_term_ex828); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LCURLY.add(LCURLY40);


            // StandardLuceneGrammar.g:106:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // StandardLuceneGrammar.g:106:10: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_ex841);
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
            // 106:24: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // StandardLuceneGrammar.g:106:39: ^( QANYTHING QANYTHING[\"*\"] )
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


            // StandardLuceneGrammar.g:107:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==NUMBER||(LA23_0 >= PHRASE && LA23_0 <= PHRASE_ANYTHING)||(LA23_0 >= STAR && LA23_0 <= TERM_TRUNCATED)||LA23_0==TO) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // StandardLuceneGrammar.g:107:10: ( TO )? b= range_value
                    {
                    // StandardLuceneGrammar.g:107:10: ( TO )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==TO) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // StandardLuceneGrammar.g:107:10: TO
                            {
                            TO41=(Token)match(input,TO,FOLLOW_TO_in_range_term_ex864); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO41);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_ex869);
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
                    // 107:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // StandardLuceneGrammar.g:107:35: ( $b)?
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


            RCURLY42=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_range_term_ex890); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RCURLY.add(RCURLY42);


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
    // StandardLuceneGrammar.g:111:1: range_value : ( normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | STAR -> ^( QANYTHING STAR ) );
    public final StandardLuceneGrammarParser.range_value_return range_value() throws RecognitionException {
        StandardLuceneGrammarParser.range_value_return retval = new StandardLuceneGrammarParser.range_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR47=null;
        StandardLuceneGrammarParser.normal_return normal43 =null;

        StandardLuceneGrammarParser.truncated_return truncated44 =null;

        StandardLuceneGrammarParser.quoted_return quoted45 =null;

        StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated46 =null;


        Object STAR47_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        try {
            // StandardLuceneGrammar.g:112:2: ( normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | STAR -> ^( QANYTHING STAR ) )
            int alt24=5;
            switch ( input.LA(1) ) {
            case NUMBER:
            case TERM_NORMAL:
                {
                alt24=1;
                }
                break;
            case TERM_TRUNCATED:
                {
                alt24=2;
                }
                break;
            case PHRASE:
                {
                alt24=3;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt24=4;
                }
                break;
            case STAR:
                {
                alt24=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;

            }

            switch (alt24) {
                case 1 :
                    // StandardLuceneGrammar.g:113:2: normal
                    {
                    pushFollow(FOLLOW_normal_in_range_value904);
                    normal43=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal43.getTree());

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
                    // 113:9: -> ^( QNORMAL normal )
                    {
                        // StandardLuceneGrammar.g:113:12: ^( QNORMAL normal )
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
                    // StandardLuceneGrammar.g:114:4: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_range_value917);
                    truncated44=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated44.getTree());

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
                    // 114:14: -> ^( QTRUNCATED truncated )
                    {
                        // StandardLuceneGrammar.g:114:17: ^( QTRUNCATED truncated )
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
                    // StandardLuceneGrammar.g:115:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_range_value930);
                    quoted45=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted45.getTree());

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
                    // 115:11: -> ^( QPHRASE quoted )
                    {
                        // StandardLuceneGrammar.g:115:14: ^( QPHRASE quoted )
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
                    // StandardLuceneGrammar.g:116:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_range_value943);
                    quoted_truncated46=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated46.getTree());

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
                    // 116:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // StandardLuceneGrammar.g:116:24: ^( QPHRASETRUNC quoted_truncated )
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
                    // StandardLuceneGrammar.g:117:4: STAR
                    {
                    STAR47=(Token)match(input,STAR,FOLLOW_STAR_in_range_value956); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR47);


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
                    // 117:9: -> ^( QANYTHING STAR )
                    {
                        // StandardLuceneGrammar.g:117:12: ^( QANYTHING STAR )
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
    // StandardLuceneGrammar.g:120:1: multi_value : LPAREN ( mterm )+ RPAREN -> ( mterm )+ ;
    public final StandardLuceneGrammarParser.multi_value_return multi_value() throws RecognitionException {
        StandardLuceneGrammarParser.multi_value_return retval = new StandardLuceneGrammarParser.multi_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN48=null;
        Token RPAREN50=null;
        StandardLuceneGrammarParser.mterm_return mterm49 =null;


        Object LPAREN48_tree=null;
        Object RPAREN50_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_mterm=new RewriteRuleSubtreeStream(adaptor,"rule mterm");
        try {
            // StandardLuceneGrammar.g:121:2: ( LPAREN ( mterm )+ RPAREN -> ( mterm )+ )
            // StandardLuceneGrammar.g:122:2: LPAREN ( mterm )+ RPAREN
            {
            LPAREN48=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_multi_value977); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN48);


            // StandardLuceneGrammar.g:122:9: ( mterm )+
            int cnt25=0;
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( ((LA25_0 >= LBRACK && LA25_0 <= LCURLY)||LA25_0==MINUS||LA25_0==NUMBER||(LA25_0 >= PHRASE && LA25_0 <= PLUS)||(LA25_0 >= TERM_NORMAL && LA25_0 <= TERM_TRUNCATED)) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // StandardLuceneGrammar.g:122:9: mterm
            	    {
            	    pushFollow(FOLLOW_mterm_in_multi_value979);
            	    mterm49=mterm();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_mterm.add(mterm49.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt25 >= 1 ) break loop25;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(25, input);
                        throw eee;
                }
                cnt25++;
            } while (true);


            RPAREN50=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_multi_value982); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN50);


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
            // 122:23: -> ( mterm )+
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
    // StandardLuceneGrammar.g:126:1: mterm : ( ( modifier )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( VALUE value ) ) ) ;
    public final StandardLuceneGrammarParser.mterm_return mterm() throws RecognitionException {
        StandardLuceneGrammarParser.mterm_return retval = new StandardLuceneGrammarParser.mterm_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        StandardLuceneGrammarParser.modifier_return modifier51 =null;

        StandardLuceneGrammarParser.value_return value52 =null;


        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try {
            // StandardLuceneGrammar.g:127:2: ( ( ( modifier )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( VALUE value ) ) ) )
            // StandardLuceneGrammar.g:128:2: ( ( modifier )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( VALUE value ) ) )
            {
            // StandardLuceneGrammar.g:128:2: ( ( modifier )? value -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( VALUE value ) ) )
            // StandardLuceneGrammar.g:128:3: ( modifier )? value
            {
            // StandardLuceneGrammar.g:128:3: ( modifier )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==MINUS||LA26_0==PLUS) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // StandardLuceneGrammar.g:128:3: modifier
                    {
                    pushFollow(FOLLOW_modifier_in_mterm1005);
                    modifier51=modifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_modifier.add(modifier51.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_value_in_mterm1008);
            value52=value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_value.add(value52.getTree());

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
            // 128:19: -> ^( ATOM ^( MODIFIER ( modifier )? ) ^( VALUE value ) )
            {
                // StandardLuceneGrammar.g:128:22: ^( ATOM ^( MODIFIER ( modifier )? ) ^( VALUE value ) )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(ATOM, "ATOM")
                , root_1);

                // StandardLuceneGrammar.g:128:29: ^( MODIFIER ( modifier )? )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(MODIFIER, "MODIFIER")
                , root_2);

                // StandardLuceneGrammar.g:128:40: ( modifier )?
                if ( stream_modifier.hasNext() ) {
                    adaptor.addChild(root_2, stream_modifier.nextTree());

                }
                stream_modifier.reset();

                adaptor.addChild(root_1, root_2);
                }

                // StandardLuceneGrammar.g:128:51: ^( VALUE value )
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
    // StandardLuceneGrammar.g:132:1: normal : ( TERM_NORMAL | NUMBER );
    public final StandardLuceneGrammarParser.normal_return normal() throws RecognitionException {
        StandardLuceneGrammarParser.normal_return retval = new StandardLuceneGrammarParser.normal_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set53=null;

        Object set53_tree=null;

        try {
            // StandardLuceneGrammar.g:133:2: ( TERM_NORMAL | NUMBER )
            // StandardLuceneGrammar.g:
            {
            root_0 = (Object)adaptor.nil();


            set53=(Token)input.LT(1);

            if ( input.LA(1)==NUMBER||input.LA(1)==TERM_NORMAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set53)
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
    // StandardLuceneGrammar.g:141:1: truncated : TERM_TRUNCATED ;
    public final StandardLuceneGrammarParser.truncated_return truncated() throws RecognitionException {
        StandardLuceneGrammarParser.truncated_return retval = new StandardLuceneGrammarParser.truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_TRUNCATED54=null;

        Object TERM_TRUNCATED54_tree=null;

        try {
            // StandardLuceneGrammar.g:142:2: ( TERM_TRUNCATED )
            // StandardLuceneGrammar.g:143:2: TERM_TRUNCATED
            {
            root_0 = (Object)adaptor.nil();


            TERM_TRUNCATED54=(Token)match(input,TERM_TRUNCATED,FOLLOW_TERM_TRUNCATED_in_truncated1070); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            TERM_TRUNCATED54_tree = 
            (Object)adaptor.create(TERM_TRUNCATED54)
            ;
            adaptor.addChild(root_0, TERM_TRUNCATED54_tree);
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
    // StandardLuceneGrammar.g:147:1: quoted_truncated : PHRASE_ANYTHING ;
    public final StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated() throws RecognitionException {
        StandardLuceneGrammarParser.quoted_truncated_return retval = new StandardLuceneGrammarParser.quoted_truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE_ANYTHING55=null;

        Object PHRASE_ANYTHING55_tree=null;

        try {
            // StandardLuceneGrammar.g:148:2: ( PHRASE_ANYTHING )
            // StandardLuceneGrammar.g:149:2: PHRASE_ANYTHING
            {
            root_0 = (Object)adaptor.nil();


            PHRASE_ANYTHING55=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1085); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE_ANYTHING55_tree = 
            (Object)adaptor.create(PHRASE_ANYTHING55)
            ;
            adaptor.addChild(root_0, PHRASE_ANYTHING55_tree);
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
    // StandardLuceneGrammar.g:152:1: quoted : PHRASE ;
    public final StandardLuceneGrammarParser.quoted_return quoted() throws RecognitionException {
        StandardLuceneGrammarParser.quoted_return retval = new StandardLuceneGrammarParser.quoted_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE56=null;

        Object PHRASE56_tree=null;

        try {
            // StandardLuceneGrammar.g:152:8: ( PHRASE )
            // StandardLuceneGrammar.g:153:2: PHRASE
            {
            root_0 = (Object)adaptor.nil();


            PHRASE56=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_quoted1097); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE56_tree = 
            (Object)adaptor.create(PHRASE56)
            ;
            adaptor.addChild(root_0, PHRASE56_tree);
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
    // StandardLuceneGrammar.g:159:1: operator : ( AND | OR | NOT | NEAR ) ;
    public final StandardLuceneGrammarParser.operator_return operator() throws RecognitionException {
        StandardLuceneGrammarParser.operator_return retval = new StandardLuceneGrammarParser.operator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set57=null;

        Object set57_tree=null;

        try {
            // StandardLuceneGrammar.g:159:9: ( ( AND | OR | NOT | NEAR ) )
            // StandardLuceneGrammar.g:
            {
            root_0 = (Object)adaptor.nil();


            set57=(Token)input.LT(1);

            if ( input.LA(1)==AND||input.LA(1)==NEAR||input.LA(1)==NOT||input.LA(1)==OR ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set57)
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
    // StandardLuceneGrammar.g:161:1: modifier : ( PLUS | MINUS ) ;
    public final StandardLuceneGrammarParser.modifier_return modifier() throws RecognitionException {
        StandardLuceneGrammarParser.modifier_return retval = new StandardLuceneGrammarParser.modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set58=null;

        Object set58_tree=null;

        try {
            // StandardLuceneGrammar.g:161:9: ( ( PLUS | MINUS ) )
            // StandardLuceneGrammar.g:
            {
            root_0 = (Object)adaptor.nil();


            set58=(Token)input.LT(1);

            if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
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
    // $ANTLR end "modifier"


    public static class term_modifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "term_modifier"
    // StandardLuceneGrammar.g:173:1: term_modifier : ( ( ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )* ) | ( TILDE -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) ) ) ( (~ ( WS | TILDE | CARAT ) )=>f= NUMBER -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) ) )* );
    public final StandardLuceneGrammarParser.term_modifier_return term_modifier() throws RecognitionException {
        StandardLuceneGrammarParser.term_modifier_return retval = new StandardLuceneGrammarParser.term_modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token b=null;
        Token f=null;
        Token CARAT59=null;
        Token TILDE60=null;
        Token TILDE61=null;
        Token TILDE62=null;

        Object b_tree=null;
        Object f_tree=null;
        Object CARAT59_tree=null;
        Object TILDE60_tree=null;
        Object TILDE61_tree=null;
        Object TILDE62_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

        try {
            // StandardLuceneGrammar.g:173:15: ( ( ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )* ) | ( TILDE -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) ) ) ( (~ ( WS | TILDE | CARAT ) )=>f= NUMBER -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) ) )* )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==CARAT) ) {
                alt29=1;
            }
            else if ( (LA29_0==TILDE) ) {
                alt29=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;

            }
            switch (alt29) {
                case 1 :
                    // StandardLuceneGrammar.g:175:2: ( ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )* )
                    {
                    // StandardLuceneGrammar.g:175:2: ( ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )* )
                    // StandardLuceneGrammar.g:176:4: ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) ) ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )*
                    {
                    // StandardLuceneGrammar.g:176:4: ( CARAT b= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) ) )
                    // StandardLuceneGrammar.g:176:5: CARAT b= NUMBER
                    {
                    CARAT59=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1156); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CARAT.add(CARAT59);


                    b=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_term_modifier1160); if (state.failed) return retval; 
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
                    // 176:20: -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) )
                    {
                        // StandardLuceneGrammar.g:176:23: ^( TMODIFIER ^( BOOST $b) ^( FUZZY ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:176:35: ^( BOOST $b)
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_2);

                        adaptor.addChild(root_2, stream_b.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:176:47: ^( FUZZY )
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


                    // StandardLuceneGrammar.g:178:2: ( ( TILDE NUMBER )=> TILDE f= NUMBER -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) ) | TILDE -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) ) )*
                    loop27:
                    do {
                        int alt27=3;
                        int LA27_0 = input.LA(1);

                        if ( (LA27_0==TILDE) ) {
                            int LA27_2 = input.LA(2);

                            if ( (LA27_2==NUMBER) ) {
                                int LA27_3 = input.LA(3);

                                if ( (synpred4_StandardLuceneGrammar()) ) {
                                    alt27=1;
                                }
                                else if ( (true) ) {
                                    alt27=2;
                                }


                            }
                            else if ( (LA27_2==EOF||LA27_2==AND||(LA27_2 >= LBRACK && LA27_2 <= MINUS)||LA27_2==NEAR||LA27_2==NOT||(LA27_2 >= OR && LA27_2 <= PLUS)||LA27_2==RPAREN||(LA27_2 >= TERM_NORMAL && LA27_2 <= TILDE)) ) {
                                alt27=2;
                            }


                        }


                        switch (alt27) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:179:3: ( TILDE NUMBER )=> TILDE f= NUMBER
                    	    {
                    	    TILDE60=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1198); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_TILDE.add(TILDE60);


                    	    f=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_term_modifier1202); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_NUMBER.add(f);


                    	    // AST REWRITE
                    	    // elements: f, b
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
                    	    // 179:35: -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) )
                    	    {
                    	        // StandardLuceneGrammar.g:179:38: ^( TMODIFIER ^( BOOST $b) ^( FUZZY $f) )
                    	        {
                    	        Object root_1 = (Object)adaptor.nil();
                    	        root_1 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                    	        , root_1);

                    	        // StandardLuceneGrammar.g:179:50: ^( BOOST $b)
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(BOOST, "BOOST")
                    	        , root_2);

                    	        adaptor.addChild(root_2, stream_b.nextNode());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        // StandardLuceneGrammar.g:179:62: ^( FUZZY $f)
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
                    	    // StandardLuceneGrammar.g:180:5: TILDE
                    	    {
                    	    TILDE61=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1229); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_TILDE.add(TILDE61);


                    	    // AST REWRITE
                    	    // elements: b, NUMBER
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
                    	    // 180:11: -> ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) )
                    	    {
                    	        // StandardLuceneGrammar.g:180:14: ^( TMODIFIER ^( BOOST $b) ^( FUZZY NUMBER[\"0.5\"] ) )
                    	        {
                    	        Object root_1 = (Object)adaptor.nil();
                    	        root_1 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                    	        , root_1);

                    	        // StandardLuceneGrammar.g:180:26: ^( BOOST $b)
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(BOOST, "BOOST")
                    	        , root_2);

                    	        adaptor.addChild(root_2, stream_b.nextNode());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        // StandardLuceneGrammar.g:180:38: ^( FUZZY NUMBER[\"0.5\"] )
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
                    	    break loop27;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:186:4: ( TILDE -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) ) ) ( (~ ( WS | TILDE | CARAT ) )=>f= NUMBER -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) ) )*
                    {
                    // StandardLuceneGrammar.g:186:4: ( TILDE -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) ) )
                    // StandardLuceneGrammar.g:186:5: TILDE
                    {
                    TILDE62=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1272); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TILDE.add(TILDE62);


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
                    // 186:11: -> ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) )
                    {
                        // StandardLuceneGrammar.g:186:14: ^( TMODIFIER ^( BOOST ) ^( FUZZY NUMBER[\"0.5\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_1);

                        // StandardLuceneGrammar.g:186:26: ^( BOOST )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        // StandardLuceneGrammar.g:186:35: ^( FUZZY NUMBER[\"0.5\"] )
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


                    // StandardLuceneGrammar.g:187:4: ( (~ ( WS | TILDE | CARAT ) )=>f= NUMBER -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) ) )*
                    loop28:
                    do {
                        int alt28=2;
                        int LA28_0 = input.LA(1);

                        if ( (LA28_0==NUMBER) ) {
                            int LA28_2 = input.LA(2);

                            if ( (synpred5_StandardLuceneGrammar()) ) {
                                alt28=1;
                            }


                        }


                        switch (alt28) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:187:5: (~ ( WS | TILDE | CARAT ) )=>f= NUMBER
                    	    {
                    	    f=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_term_modifier1311); if (state.failed) return retval; 
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
                    	    // 187:35: -> ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) )
                    	    {
                    	        // StandardLuceneGrammar.g:187:38: ^( TMODIFIER ^( BOOST ) ^( FUZZY ( $f)? ) )
                    	        {
                    	        Object root_1 = (Object)adaptor.nil();
                    	        root_1 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                    	        , root_1);

                    	        // StandardLuceneGrammar.g:187:50: ^( BOOST )
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(BOOST, "BOOST")
                    	        , root_2);

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        // StandardLuceneGrammar.g:187:60: ^( FUZZY ( $f)? )
                    	        {
                    	        Object root_2 = (Object)adaptor.nil();
                    	        root_2 = (Object)adaptor.becomeRoot(
                    	        (Object)adaptor.create(FUZZY, "FUZZY")
                    	        , root_2);

                    	        // StandardLuceneGrammar.g:187:69: ( $f)?
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
                    	    break loop28;
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
    // StandardLuceneGrammar.g:191:1: boost : CARAT NUMBER ;
    public final StandardLuceneGrammarParser.boost_return boost() throws RecognitionException {
        StandardLuceneGrammarParser.boost_return retval = new StandardLuceneGrammarParser.boost_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token CARAT63=null;
        Token NUMBER64=null;

        Object CARAT63_tree=null;
        Object NUMBER64_tree=null;

        try {
            // StandardLuceneGrammar.g:191:7: ( CARAT NUMBER )
            // StandardLuceneGrammar.g:192:2: CARAT NUMBER
            {
            root_0 = (Object)adaptor.nil();


            CARAT63=(Token)match(input,CARAT,FOLLOW_CARAT_in_boost1348); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            CARAT63_tree = 
            (Object)adaptor.create(CARAT63)
            ;
            adaptor.addChild(root_0, CARAT63_tree);
            }

            NUMBER64=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_boost1350); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NUMBER64_tree = 
            (Object)adaptor.create(NUMBER64)
            ;
            adaptor.addChild(root_0, NUMBER64_tree);
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
    // StandardLuceneGrammar.g:195:1: fuzzy : TILDE NUMBER ;
    public final StandardLuceneGrammarParser.fuzzy_return fuzzy() throws RecognitionException {
        StandardLuceneGrammarParser.fuzzy_return retval = new StandardLuceneGrammarParser.fuzzy_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE65=null;
        Token NUMBER66=null;

        Object TILDE65_tree=null;
        Object NUMBER66_tree=null;

        try {
            // StandardLuceneGrammar.g:195:7: ( TILDE NUMBER )
            // StandardLuceneGrammar.g:196:2: TILDE NUMBER
            {
            root_0 = (Object)adaptor.nil();


            TILDE65=(Token)match(input,TILDE,FOLLOW_TILDE_in_fuzzy1362); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            TILDE65_tree = 
            (Object)adaptor.create(TILDE65)
            ;
            adaptor.addChild(root_0, TILDE65_tree);
            }

            NUMBER66=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_fuzzy1364); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NUMBER66_tree = 
            (Object)adaptor.create(NUMBER66)
            ;
            adaptor.addChild(root_0, NUMBER66_tree);
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
    // StandardLuceneGrammar.g:200:1: near : ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )? ;
    public final StandardLuceneGrammarParser.near_return near() throws RecognitionException {
        StandardLuceneGrammarParser.near_return retval = new StandardLuceneGrammarParser.near_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token b=null;
        Token NEAR67=null;
        Token char_literal68=null;

        Object b_tree=null;
        Object NEAR67_tree=null;
        Object char_literal68_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // StandardLuceneGrammar.g:200:6: ( ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )? )
            // StandardLuceneGrammar.g:201:2: ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )?
            {
            // StandardLuceneGrammar.g:201:2: ( NEAR -> ^( OPERATOR[\"NEAR 5\"] ) )
            // StandardLuceneGrammar.g:201:3: NEAR
            {
            NEAR67=(Token)match(input,NEAR,FOLLOW_NEAR_in_near1378); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NEAR.add(NEAR67);


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
            // 201:8: -> ^( OPERATOR[\"NEAR 5\"] )
            {
                // StandardLuceneGrammar.g:201:11: ^( OPERATOR[\"NEAR 5\"] )
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


            // StandardLuceneGrammar.g:202:2: ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR \" + $b.getText()] ) )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==53) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // StandardLuceneGrammar.g:202:3: '/' b= NUMBER
                    {
                    char_literal68=(Token)match(input,53,FOLLOW_53_in_near1391); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_53.add(char_literal68);


                    b=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_near1395); if (state.failed) return retval; 
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
                    // 202:16: -> ^( OPERATOR[\"NEAR \" + $b.getText()] )
                    {
                        // StandardLuceneGrammar.g:202:19: ^( OPERATOR[\"NEAR \" + $b.getText()] )
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
        // StandardLuceneGrammar.g:60:2: ( modifier LPAREN ( clauseDefault )+ RPAREN )
        // StandardLuceneGrammar.g:60:3: modifier LPAREN ( clauseDefault )+ RPAREN
        {
        pushFollow(FOLLOW_modifier_in_synpred1_StandardLuceneGrammar352);
        modifier();

        state._fsp--;
        if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar354); if (state.failed) return ;

        // StandardLuceneGrammar.g:60:19: ( clauseDefault )+
        int cnt31=0;
        loop31:
        do {
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( ((LA31_0 >= LBRACK && LA31_0 <= MINUS)||LA31_0==NUMBER||(LA31_0 >= PHRASE && LA31_0 <= PLUS)||(LA31_0 >= TERM_NORMAL && LA31_0 <= TERM_TRUNCATED)) ) {
                alt31=1;
            }


            switch (alt31) {
        	case 1 :
        	    // StandardLuceneGrammar.g:60:19: clauseDefault
        	    {
        	    pushFollow(FOLLOW_clauseDefault_in_synpred1_StandardLuceneGrammar356);
        	    clauseDefault();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt31 >= 1 ) break loop31;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(31, input);
                    throw eee;
            }
            cnt31++;
        } while (true);


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar359); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred1_StandardLuceneGrammar

    // $ANTLR start synpred2_StandardLuceneGrammar
    public final void synpred2_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:61:4: ( LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER )
        // StandardLuceneGrammar.g:61:5: LPAREN ( clauseDefault )+ RPAREN CARAT NUMBER
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar415); if (state.failed) return ;

        // StandardLuceneGrammar.g:61:12: ( clauseDefault )+
        int cnt32=0;
        loop32:
        do {
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( ((LA32_0 >= LBRACK && LA32_0 <= MINUS)||LA32_0==NUMBER||(LA32_0 >= PHRASE && LA32_0 <= PLUS)||(LA32_0 >= TERM_NORMAL && LA32_0 <= TERM_TRUNCATED)) ) {
                alt32=1;
            }


            switch (alt32) {
        	case 1 :
        	    // StandardLuceneGrammar.g:61:12: clauseDefault
        	    {
        	    pushFollow(FOLLOW_clauseDefault_in_synpred2_StandardLuceneGrammar417);
        	    clauseDefault();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt32 >= 1 ) break loop32;
        	    if (state.backtracking>0) {state.failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(32, input);
                    throw eee;
            }
            cnt32++;
        } while (true);


        match(input,RPAREN,FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar420); if (state.failed) return ;

        match(input,CARAT,FOLLOW_CARAT_in_synpred2_StandardLuceneGrammar422); if (state.failed) return ;

        match(input,NUMBER,FOLLOW_NUMBER_in_synpred2_StandardLuceneGrammar424); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_StandardLuceneGrammar

    // $ANTLR start synpred3_StandardLuceneGrammar
    public final void synpred3_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:62:4: ( LPAREN )
        // StandardLuceneGrammar.g:62:5: LPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar479); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred3_StandardLuceneGrammar

    // $ANTLR start synpred4_StandardLuceneGrammar
    public final void synpred4_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:179:3: ( TILDE NUMBER )
        // StandardLuceneGrammar.g:179:4: TILDE NUMBER
        {
        match(input,TILDE,FOLLOW_TILDE_in_synpred4_StandardLuceneGrammar1192); if (state.failed) return ;

        match(input,NUMBER,FOLLOW_NUMBER_in_synpred4_StandardLuceneGrammar1194); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred4_StandardLuceneGrammar

    // $ANTLR start synpred5_StandardLuceneGrammar
    public final void synpred5_StandardLuceneGrammar_fragment() throws RecognitionException {
        // StandardLuceneGrammar.g:187:5: (~ ( WS | TILDE | CARAT ) )
        // StandardLuceneGrammar.g:
        {
        if ( (input.LA(1) >= ADDED && input.LA(1) <= BOOST)||(input.LA(1) >= CLAUSE && input.LA(1) <= TERM_TRUNCATED)||(input.LA(1) >= TMODIFIER && input.LA(1) <= VBAR)||input.LA(1)==53 ) {
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


 

    public static final BitSet FOLLOW_clauseDefault_in_mainQ143 = new BitSet(new long[]{0x00006000E41E0002L});
    public static final BitSet FOLLOW_clauseStrongest_in_clauseDefault176 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_OR_in_clauseDefault185 = new BitSet(new long[]{0x00006000E41E0000L});
    public static final BitSet FOLLOW_clauseStrongest_in_clauseDefault189 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_clauseStrong_in_clauseStrongest218 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_AND_in_clauseStrongest228 = new BitSet(new long[]{0x00006000E41E0000L});
    public static final BitSet FOLLOW_clauseStrong_in_clauseStrongest232 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_clauseWeak_in_clauseStrong263 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_NOT_in_clauseStrong272 = new BitSet(new long[]{0x00006000E41E0000L});
    public static final BitSet FOLLOW_clauseWeak_in_clauseStrong276 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_primaryClause_in_clauseWeak307 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_near_in_clauseWeak316 = new BitSet(new long[]{0x00006000E41E0000L});
    public static final BitSet FOLLOW_primaryClause_in_clauseWeak320 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_modifier_in_primaryClause364 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryClause367 = new BitSet(new long[]{0x00006000E41E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_primaryClause369 = new BitSet(new long[]{0x00006400E41E0000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryClause372 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_CARAT_in_primaryClause375 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NUMBER_in_primaryClause377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_primaryClause428 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_primaryClause431 = new BitSet(new long[]{0x00006000E41E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_primaryClause433 = new BitSet(new long[]{0x00006400E41E0000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryClause436 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_CARAT_in_primaryClause439 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NUMBER_in_primaryClause441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryClause483 = new BitSet(new long[]{0x00006000E41E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_primaryClause485 = new BitSet(new long[]{0x00006400E41E0000L});
    public static final BitSet FOLLOW_RPAREN_in_primaryClause488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_primaryClause498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom520 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_field_in_atom523 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_multi_value_in_atom525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom555 = new BitSet(new long[]{0x0000600064060000L});
    public static final BitSet FOLLOW_field_in_atom558 = new BitSet(new long[]{0x0000600064060000L});
    public static final BitSet FOLLOW_value_in_atom561 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_NORMAL_in_field609 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_COLON_in_field611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_in_in_value634 = new BitSet(new long[]{0x0000800000000202L});
    public static final BitSet FOLLOW_range_term_ex_in_value647 = new BitSet(new long[]{0x0000800000000202L});
    public static final BitSet FOLLOW_normal_in_value661 = new BitSet(new long[]{0x0000800000000202L});
    public static final BitSet FOLLOW_truncated_in_value674 = new BitSet(new long[]{0x0000800000000202L});
    public static final BitSet FOLLOW_quoted_in_value687 = new BitSet(new long[]{0x0000800000000202L});
    public static final BitSet FOLLOW_quoted_truncated_in_value700 = new BitSet(new long[]{0x0000800000000202L});
    public static final BitSet FOLLOW_term_modifier_in_value714 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACK_in_range_term_in747 = new BitSet(new long[]{0x0000700064000000L});
    public static final BitSet FOLLOW_range_value_in_range_term_in759 = new BitSet(new long[]{0x0002710064000000L});
    public static final BitSet FOLLOW_TO_in_range_term_in782 = new BitSet(new long[]{0x0000700064000000L});
    public static final BitSet FOLLOW_range_value_in_range_term_in787 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_RBRACK_in_range_term_in808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LCURLY_in_range_term_ex828 = new BitSet(new long[]{0x0000700064000000L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex841 = new BitSet(new long[]{0x0002720064000000L});
    public static final BitSet FOLLOW_TO_in_range_term_ex864 = new BitSet(new long[]{0x0000700064000000L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex869 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RCURLY_in_range_term_ex890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_range_value904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_range_value917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_range_value930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_range_value943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_range_value956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_multi_value977 = new BitSet(new long[]{0x00006000E4160000L});
    public static final BitSet FOLLOW_mterm_in_multi_value979 = new BitSet(new long[]{0x00006400E4160000L});
    public static final BitSet FOLLOW_RPAREN_in_multi_value982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_mterm1005 = new BitSet(new long[]{0x0000600064060000L});
    public static final BitSet FOLLOW_value_in_mterm1008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_TRUNCATED_in_truncated1070 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_in_quoted1097 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1156 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NUMBER_in_term_modifier1160 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1198 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NUMBER_in_term_modifier1202 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1229 = new BitSet(new long[]{0x0000800000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1272 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_NUMBER_in_term_modifier1311 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_CARAT_in_boost1348 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NUMBER_in_boost1350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_fuzzy1362 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NUMBER_in_fuzzy1364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_near1378 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_53_in_near1391 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NUMBER_in_near1395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_synpred1_StandardLuceneGrammar352 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar354 = new BitSet(new long[]{0x00006000E41E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_synpred1_StandardLuceneGrammar356 = new BitSet(new long[]{0x00006400E41E0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar415 = new BitSet(new long[]{0x00006000E41E0000L});
    public static final BitSet FOLLOW_clauseDefault_in_synpred2_StandardLuceneGrammar417 = new BitSet(new long[]{0x00006400E41E0000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar420 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_CARAT_in_synpred2_StandardLuceneGrammar422 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NUMBER_in_synpred2_StandardLuceneGrammar424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar479 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_synpred4_StandardLuceneGrammar1192 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NUMBER_in_synpred4_StandardLuceneGrammar1194 = new BitSet(new long[]{0x0000000000000002L});

}