// $ANTLR 3.4 /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g 2012-07-26 21:32:24

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class InvenioParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AMBIGUITY", "AMPER", "AND", "ATOM", "BAR", "BOOST", "CARAT", "CLAUSE", "COLON", "DATE_TOKEN", "DQUOTE", "ESC_CHAR", "FIELD", "FUZZY", "IDENTIFIER", "INT", "LBRACK", "LCURLY", "LPAREN", "MINUS", "MODIFIER", "NEAR", "NOT", "NUMBER", "OPERATOR", "OR", "PHRASE", "PHRASE_ANYTHING", "PLUS", "QANYTHING", "QDATE", "QFUNC", "QMARK", "QNORMAL", "QPHRASE", "QPHRASETRUNC", "QRANGEEX", "QRANGEIN", "QREGEX", "QTRUNCATED", "RBRACK", "RCURLY", "REGEX", "RPAREN", "SECOND_ORDER_OP", "SLASH", "SQUOTE", "STAR", "TERM_CHAR", "TERM_NORMAL", "TERM_START_CHAR", "TERM_TRUNCATED", "TILDE", "TMODIFIER", "TO", "VBAR", "WS"
    };

    public static final int EOF=-1;
    public static final int AMBIGUITY=4;
    public static final int AMPER=5;
    public static final int AND=6;
    public static final int ATOM=7;
    public static final int BAR=8;
    public static final int BOOST=9;
    public static final int CARAT=10;
    public static final int CLAUSE=11;
    public static final int COLON=12;
    public static final int DATE_TOKEN=13;
    public static final int DQUOTE=14;
    public static final int ESC_CHAR=15;
    public static final int FIELD=16;
    public static final int FUZZY=17;
    public static final int IDENTIFIER=18;
    public static final int INT=19;
    public static final int LBRACK=20;
    public static final int LCURLY=21;
    public static final int LPAREN=22;
    public static final int MINUS=23;
    public static final int MODIFIER=24;
    public static final int NEAR=25;
    public static final int NOT=26;
    public static final int NUMBER=27;
    public static final int OPERATOR=28;
    public static final int OR=29;
    public static final int PHRASE=30;
    public static final int PHRASE_ANYTHING=31;
    public static final int PLUS=32;
    public static final int QANYTHING=33;
    public static final int QDATE=34;
    public static final int QFUNC=35;
    public static final int QMARK=36;
    public static final int QNORMAL=37;
    public static final int QPHRASE=38;
    public static final int QPHRASETRUNC=39;
    public static final int QRANGEEX=40;
    public static final int QRANGEIN=41;
    public static final int QREGEX=42;
    public static final int QTRUNCATED=43;
    public static final int RBRACK=44;
    public static final int RCURLY=45;
    public static final int REGEX=46;
    public static final int RPAREN=47;
    public static final int SECOND_ORDER_OP=48;
    public static final int SLASH=49;
    public static final int SQUOTE=50;
    public static final int STAR=51;
    public static final int TERM_CHAR=52;
    public static final int TERM_NORMAL=53;
    public static final int TERM_START_CHAR=54;
    public static final int TERM_TRUNCATED=55;
    public static final int TILDE=56;
    public static final int TMODIFIER=57;
    public static final int TO=58;
    public static final int VBAR=59;
    public static final int WS=60;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public InvenioParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public InvenioParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return InvenioParser.tokenNames; }
    public String getGrammarFileName() { return "/dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g"; }


    public static class mainQ_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mainQ"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:37:1: mainQ : ( operator clauseTop -> ^( AMBIGUITY[\"leftmost-operation\"] operator clauseTop ) | clauseTop );
    public final InvenioParser.mainQ_return mainQ() throws RecognitionException {
        InvenioParser.mainQ_return retval = new InvenioParser.mainQ_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.operator_return operator1 =null;

        InvenioParser.clauseTop_return clauseTop2 =null;

        InvenioParser.clauseTop_return clauseTop3 =null;


        RewriteRuleSubtreeStream stream_clauseTop=new RewriteRuleSubtreeStream(adaptor,"rule clauseTop");
        RewriteRuleSubtreeStream stream_operator=new RewriteRuleSubtreeStream(adaptor,"rule operator");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:37:7: ( operator clauseTop -> ^( AMBIGUITY[\"leftmost-operation\"] operator clauseTop ) | clauseTop )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==AND||(LA1_0 >= NEAR && LA1_0 <= NOT)||LA1_0==OR) ) {
                alt1=1;
            }
            else if ( (LA1_0==BAR||LA1_0==IDENTIFIER||(LA1_0 >= LBRACK && LA1_0 <= LPAREN)||LA1_0==NUMBER||(LA1_0 >= PHRASE && LA1_0 <= PHRASE_ANYTHING)||LA1_0==QMARK||LA1_0==REGEX||LA1_0==SECOND_ORDER_OP||LA1_0==STAR||LA1_0==TERM_NORMAL||LA1_0==TERM_TRUNCATED) ) {
                alt1=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }
            switch (alt1) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:38:2: operator clauseTop
                    {
                    pushFollow(FOLLOW_operator_in_mainQ150);
                    operator1=operator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_operator.add(operator1.getTree());

                    pushFollow(FOLLOW_clauseTop_in_mainQ152);
                    clauseTop2=clauseTop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_clauseTop.add(clauseTop2.getTree());

                    // AST REWRITE
                    // elements: clauseTop, operator
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 38:21: -> ^( AMBIGUITY[\"leftmost-operation\"] operator clauseTop )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:38:24: ^( AMBIGUITY[\"leftmost-operation\"] operator clauseTop )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(AMBIGUITY, "leftmost-operation")
                        , root_1);

                        adaptor.addChild(root_1, stream_operator.nextTree());

                        adaptor.addChild(root_1, stream_clauseTop.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:39:4: clauseTop
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_clauseTop_in_mainQ168);
                    clauseTop3=clauseTop();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, clauseTop3.getTree());

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
    // $ANTLR end "mainQ"


    public static class clauseTop_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseTop"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:42:1: clauseTop : clauseOr -> clauseOr ;
    public final InvenioParser.clauseTop_return clauseTop() throws RecognitionException {
        InvenioParser.clauseTop_return retval = new InvenioParser.clauseTop_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.clauseOr_return clauseOr4 =null;


        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:43:3: ( clauseOr -> clauseOr )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:46:3: clauseOr
            {
            pushFollow(FOLLOW_clauseOr_in_clauseTop188);
            clauseOr4=clauseOr();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr4.getTree());

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
            // 46:12: -> clauseOr
            {
                adaptor.addChild(root_0, stream_clauseOr.nextTree());

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
    // $ANTLR end "clauseTop"


    public static class clauseOr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseOr"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:49:1: clauseOr : ( clauseBasic -> clauseBasic ) ( operator a= clauseBasic -> ^( operator $clauseOr $a) |a= clauseBasic -> ^( OPERATOR[\"DEFOP\"] $clauseOr $a) )* ;
    public final InvenioParser.clauseOr_return clauseOr() throws RecognitionException {
        InvenioParser.clauseOr_return retval = new InvenioParser.clauseOr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.clauseBasic_return a =null;

        InvenioParser.clauseBasic_return clauseBasic5 =null;

        InvenioParser.operator_return operator6 =null;


        RewriteRuleSubtreeStream stream_clauseBasic=new RewriteRuleSubtreeStream(adaptor,"rule clauseBasic");
        RewriteRuleSubtreeStream stream_operator=new RewriteRuleSubtreeStream(adaptor,"rule operator");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:50:3: ( ( clauseBasic -> clauseBasic ) ( operator a= clauseBasic -> ^( operator $clauseOr $a) |a= clauseBasic -> ^( OPERATOR[\"DEFOP\"] $clauseOr $a) )* )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:51:3: ( clauseBasic -> clauseBasic ) ( operator a= clauseBasic -> ^( operator $clauseOr $a) |a= clauseBasic -> ^( OPERATOR[\"DEFOP\"] $clauseOr $a) )*
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:51:3: ( clauseBasic -> clauseBasic )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:51:4: clauseBasic
            {
            pushFollow(FOLLOW_clauseBasic_in_clauseOr209);
            clauseBasic5=clauseBasic();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseBasic.add(clauseBasic5.getTree());

            // AST REWRITE
            // elements: clauseBasic
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 51:16: -> clauseBasic
            {
                adaptor.addChild(root_0, stream_clauseBasic.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:52:3: ( operator a= clauseBasic -> ^( operator $clauseOr $a) |a= clauseBasic -> ^( OPERATOR[\"DEFOP\"] $clauseOr $a) )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==AND||(LA2_0 >= NEAR && LA2_0 <= NOT)||LA2_0==OR) ) {
                    alt2=1;
                }
                else if ( (LA2_0==BAR||LA2_0==IDENTIFIER||(LA2_0 >= LBRACK && LA2_0 <= LPAREN)||LA2_0==NUMBER||(LA2_0 >= PHRASE && LA2_0 <= PHRASE_ANYTHING)||LA2_0==QMARK||LA2_0==REGEX||LA2_0==SECOND_ORDER_OP||LA2_0==STAR||LA2_0==TERM_NORMAL||LA2_0==TERM_TRUNCATED) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:52:4: operator a= clauseBasic
            	    {
            	    pushFollow(FOLLOW_operator_in_clauseOr219);
            	    operator6=operator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_operator.add(operator6.getTree());

            	    pushFollow(FOLLOW_clauseBasic_in_clauseOr223);
            	    a=clauseBasic();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseBasic.add(a.getTree());

            	    // AST REWRITE
            	    // elements: operator, a, clauseOr
            	    // token labels: 
            	    // rule labels: retval, a
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            	    RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"rule a",a!=null?a.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 52:27: -> ^( operator $clauseOr $a)
            	    {
            	        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:52:30: ^( operator $clauseOr $a)
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(stream_operator.nextNode(), root_1);

            	        adaptor.addChild(root_1, stream_retval.nextTree());

            	        adaptor.addChild(root_1, stream_a.nextTree());

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;
            	    }

            	    }
            	    break;
            	case 2 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:53:5: a= clauseBasic
            	    {
            	    pushFollow(FOLLOW_clauseBasic_in_clauseOr243);
            	    a=clauseBasic();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseBasic.add(a.getTree());

            	    // AST REWRITE
            	    // elements: clauseOr, a
            	    // token labels: 
            	    // rule labels: retval, a
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            	    RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"rule a",a!=null?a.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 53:19: -> ^( OPERATOR[\"DEFOP\"] $clauseOr $a)
            	    {
            	        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:53:22: ^( OPERATOR[\"DEFOP\"] $clauseOr $a)
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "DEFOP")
            	        , root_1);

            	        adaptor.addChild(root_1, stream_retval.nextTree());

            	        adaptor.addChild(root_1, stream_a.nextTree());

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


    public static class clauseBare_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseBare"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:56:1: clauseBare : ( clauseBasic -> clauseBasic ) (a= clauseBasic -> ^( OPERATOR[\"DEFOP\"] $clauseBare $a) )* ;
    public final InvenioParser.clauseBare_return clauseBare() throws RecognitionException {
        InvenioParser.clauseBare_return retval = new InvenioParser.clauseBare_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.clauseBasic_return a =null;

        InvenioParser.clauseBasic_return clauseBasic7 =null;


        RewriteRuleSubtreeStream stream_clauseBasic=new RewriteRuleSubtreeStream(adaptor,"rule clauseBasic");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:57:3: ( ( clauseBasic -> clauseBasic ) (a= clauseBasic -> ^( OPERATOR[\"DEFOP\"] $clauseBare $a) )* )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:58:3: ( clauseBasic -> clauseBasic ) (a= clauseBasic -> ^( OPERATOR[\"DEFOP\"] $clauseBare $a) )*
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:58:3: ( clauseBasic -> clauseBasic )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:58:4: clauseBasic
            {
            pushFollow(FOLLOW_clauseBasic_in_clauseBare274);
            clauseBasic7=clauseBasic();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_clauseBasic.add(clauseBasic7.getTree());

            // AST REWRITE
            // elements: clauseBasic
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 58:16: -> clauseBasic
            {
                adaptor.addChild(root_0, stream_clauseBasic.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:59:3: (a= clauseBasic -> ^( OPERATOR[\"DEFOP\"] $clauseBare $a) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==BAR||LA3_0==IDENTIFIER||(LA3_0 >= LBRACK && LA3_0 <= LPAREN)||LA3_0==NUMBER||(LA3_0 >= PHRASE && LA3_0 <= PHRASE_ANYTHING)||LA3_0==QMARK||LA3_0==REGEX||LA3_0==SECOND_ORDER_OP||LA3_0==STAR||LA3_0==TERM_NORMAL||LA3_0==TERM_TRUNCATED) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:59:4: a= clauseBasic
            	    {
            	    pushFollow(FOLLOW_clauseBasic_in_clauseBare286);
            	    a=clauseBasic();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_clauseBasic.add(a.getTree());

            	    // AST REWRITE
            	    // elements: clauseBare, a
            	    // token labels: 
            	    // rule labels: retval, a
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {

            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            	    RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"rule a",a!=null?a.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 59:18: -> ^( OPERATOR[\"DEFOP\"] $clauseBare $a)
            	    {
            	        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:59:21: ^( OPERATOR[\"DEFOP\"] $clauseBare $a)
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        root_1 = (Object)adaptor.becomeRoot(
            	        (Object)adaptor.create(OPERATOR, "DEFOP")
            	        , root_1);

            	        adaptor.addChild(root_1, stream_retval.nextTree());

            	        adaptor.addChild(root_1, stream_a.nextTree());

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
    // $ANTLR end "clauseBare"


    public static class clauseBasic_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clauseBasic"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:81:1: clauseBasic : ( ( modifier LPAREN clauseOr RPAREN )=> ( modifier )? LPAREN clauseOr RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) ) ) | ( LPAREN clauseOr RPAREN term_modifier )=> ( modifier )? LPAREN clauseOr RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) ) ) | ( LPAREN )=> LPAREN clauseOr RPAREN -> clauseOr | second_order_op clauseBasic -> ^( second_order_op clauseBasic ) | atom );
    public final InvenioParser.clauseBasic_return clauseBasic() throws RecognitionException {
        InvenioParser.clauseBasic_return retval = new InvenioParser.clauseBasic_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN9=null;
        Token RPAREN11=null;
        Token LPAREN14=null;
        Token RPAREN16=null;
        Token LPAREN18=null;
        Token RPAREN20=null;
        InvenioParser.modifier_return modifier8 =null;

        InvenioParser.clauseOr_return clauseOr10 =null;

        InvenioParser.term_modifier_return term_modifier12 =null;

        InvenioParser.modifier_return modifier13 =null;

        InvenioParser.clauseOr_return clauseOr15 =null;

        InvenioParser.term_modifier_return term_modifier17 =null;

        InvenioParser.clauseOr_return clauseOr19 =null;

        InvenioParser.second_order_op_return second_order_op21 =null;

        InvenioParser.clauseBasic_return clauseBasic22 =null;

        InvenioParser.atom_return atom23 =null;


        Object LPAREN9_tree=null;
        Object RPAREN11_tree=null;
        Object LPAREN14_tree=null;
        Object RPAREN16_tree=null;
        Object LPAREN18_tree=null;
        Object RPAREN20_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_clauseBasic=new RewriteRuleSubtreeStream(adaptor,"rule clauseBasic");
        RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
        RewriteRuleSubtreeStream stream_second_order_op=new RewriteRuleSubtreeStream(adaptor,"rule second_order_op");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:82:2: ( ( modifier LPAREN clauseOr RPAREN )=> ( modifier )? LPAREN clauseOr RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) ) ) | ( LPAREN clauseOr RPAREN term_modifier )=> ( modifier )? LPAREN clauseOr RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) ) ) | ( LPAREN )=> LPAREN clauseOr RPAREN -> clauseOr | second_order_op clauseBasic -> ^( second_order_op clauseBasic ) | atom )
            int alt8=5;
            switch ( input.LA(1) ) {
            case BAR:
                {
                int LA8_1 = input.LA(2);

                if ( (synpred1_Invenio()) ) {
                    alt8=1;
                }
                else if ( (synpred2_Invenio()) ) {
                    alt8=2;
                }
                else if ( (true) ) {
                    alt8=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;

                }
                }
                break;
            case LPAREN:
                {
                int LA8_2 = input.LA(2);

                if ( (synpred1_Invenio()) ) {
                    alt8=1;
                }
                else if ( (synpred2_Invenio()) ) {
                    alt8=2;
                }
                else if ( (synpred3_Invenio()) ) {
                    alt8=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 2, input);

                    throw nvae;

                }
                }
                break;
            case SECOND_ORDER_OP:
                {
                alt8=4;
                }
                break;
            case IDENTIFIER:
            case LBRACK:
            case LCURLY:
            case NUMBER:
            case PHRASE:
            case PHRASE_ANYTHING:
            case QMARK:
            case REGEX:
            case STAR:
            case TERM_NORMAL:
            case TERM_TRUNCATED:
                {
                alt8=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }

            switch (alt8) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:83:2: ( modifier LPAREN clauseOr RPAREN )=> ( modifier )? LPAREN clauseOr RPAREN ( term_modifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:83:39: ( modifier )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==BAR) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:83:39: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_clauseBasic339);
                            modifier8=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier8.getTree());

                            }
                            break;

                    }


                    LPAREN9=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic342); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN9);


                    pushFollow(FOLLOW_clauseOr_in_clauseBasic344);
                    clauseOr10=clauseOr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr10.getTree());

                    RPAREN11=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic346); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN11);


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:83:72: ( term_modifier )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==CARAT||LA5_0==TILDE) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:83:72: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_clauseBasic348);
                            term_modifier12=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier12.getTree());

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
                    // 84:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:84:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:84:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:84:26: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:84:36: ^( TMODIFIER ( term_modifier )? clauseOr )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:84:48: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        adaptor.addChild(root_3, stream_clauseOr.nextTree());

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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:85:4: ( LPAREN clauseOr RPAREN term_modifier )=> ( modifier )? LPAREN clauseOr RPAREN ( term_modifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:85:45: ( modifier )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==BAR) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:85:45: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_clauseBasic390);
                            modifier13=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier13.getTree());

                            }
                            break;

                    }


                    LPAREN14=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic393); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN14);


                    pushFollow(FOLLOW_clauseOr_in_clauseBasic395);
                    clauseOr15=clauseOr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr15.getTree());

                    RPAREN16=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic397); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN16);


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:85:78: ( term_modifier )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==CARAT||LA7_0==TILDE) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:85:78: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_clauseBasic399);
                            term_modifier17=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier17.getTree());

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
                    // 86:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:86:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:86:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? clauseOr ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:86:26: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:86:36: ^( TMODIFIER ( term_modifier )? clauseOr )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:86:48: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        adaptor.addChild(root_3, stream_clauseOr.nextTree());

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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:87:4: ( LPAREN )=> LPAREN clauseOr RPAREN
                    {
                    LPAREN18=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic436); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN18);


                    pushFollow(FOLLOW_clauseOr_in_clauseBasic438);
                    clauseOr19=clauseOr();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr19.getTree());

                    RPAREN20=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic440); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN20);


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
                    // 88:3: -> clauseOr
                    {
                        adaptor.addChild(root_0, stream_clauseOr.nextTree());

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:89:4: second_order_op clauseBasic
                    {
                    pushFollow(FOLLOW_second_order_op_in_clauseBasic451);
                    second_order_op21=second_order_op();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_second_order_op.add(second_order_op21.getTree());

                    pushFollow(FOLLOW_clauseBasic_in_clauseBasic453);
                    clauseBasic22=clauseBasic();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_clauseBasic.add(clauseBasic22.getTree());

                    // AST REWRITE
                    // elements: clauseBasic, second_order_op
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 89:32: -> ^( second_order_op clauseBasic )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:89:35: ^( second_order_op clauseBasic )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_second_order_op.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_clauseBasic.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 5 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:90:4: atom
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_atom_in_clauseBasic466);
                    atom23=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom23.getTree());

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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:94:1: atom : ( ( modifier )? field multi_value ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) ) | ( modifier )? ( STAR COLON )? STAR -> ^( MODIFIER ( modifier )? ^( QANYTHING STAR[\"*\"] ) ) );
    public final InvenioParser.atom_return atom() throws RecognitionException {
        InvenioParser.atom_return retval = new InvenioParser.atom_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR33=null;
        Token COLON34=null;
        Token STAR35=null;
        InvenioParser.modifier_return modifier24 =null;

        InvenioParser.field_return field25 =null;

        InvenioParser.multi_value_return multi_value26 =null;

        InvenioParser.term_modifier_return term_modifier27 =null;

        InvenioParser.modifier_return modifier28 =null;

        InvenioParser.field_return field29 =null;

        InvenioParser.value_return value30 =null;

        InvenioParser.term_modifier_return term_modifier31 =null;

        InvenioParser.modifier_return modifier32 =null;


        Object STAR33_tree=null;
        Object COLON34_tree=null;
        Object STAR35_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        RewriteRuleSubtreeStream stream_multi_value=new RewriteRuleSubtreeStream(adaptor,"rule multi_value");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:95:2: ( ( modifier )? field multi_value ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) ) | ( modifier )? ( STAR COLON )? STAR -> ^( MODIFIER ( modifier )? ^( QANYTHING STAR[\"*\"] ) ) )
            int alt16=3;
            switch ( input.LA(1) ) {
            case BAR:
                {
                switch ( input.LA(2) ) {
                case TERM_NORMAL:
                    {
                    int LA16_2 = input.LA(3);

                    if ( (LA16_2==COLON) ) {
                        int LA16_5 = input.LA(4);

                        if ( (LA16_5==LPAREN) ) {
                            alt16=1;
                        }
                        else if ( (LA16_5==IDENTIFIER||(LA16_5 >= LBRACK && LA16_5 <= LCURLY)||LA16_5==NUMBER||(LA16_5 >= PHRASE && LA16_5 <= PHRASE_ANYTHING)||LA16_5==QMARK||LA16_5==REGEX||LA16_5==TERM_NORMAL||LA16_5==TERM_TRUNCATED) ) {
                            alt16=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 5, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA16_2==EOF||LA16_2==AND||LA16_2==BAR||LA16_2==CARAT||LA16_2==IDENTIFIER||(LA16_2 >= LBRACK && LA16_2 <= LPAREN)||(LA16_2 >= NEAR && LA16_2 <= NUMBER)||(LA16_2 >= OR && LA16_2 <= PHRASE_ANYTHING)||LA16_2==QMARK||(LA16_2 >= REGEX && LA16_2 <= SECOND_ORDER_OP)||LA16_2==STAR||LA16_2==TERM_NORMAL||(LA16_2 >= TERM_TRUNCATED && LA16_2 <= TILDE)) ) {
                        alt16=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 2, input);

                        throw nvae;

                    }
                    }
                    break;
                case IDENTIFIER:
                case LBRACK:
                case LCURLY:
                case NUMBER:
                case PHRASE:
                case PHRASE_ANYTHING:
                case QMARK:
                case REGEX:
                case TERM_TRUNCATED:
                    {
                    alt16=2;
                    }
                    break;
                case STAR:
                    {
                    alt16=3;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;

                }

                }
                break;
            case TERM_NORMAL:
                {
                int LA16_2 = input.LA(2);

                if ( (LA16_2==COLON) ) {
                    int LA16_5 = input.LA(3);

                    if ( (LA16_5==LPAREN) ) {
                        alt16=1;
                    }
                    else if ( (LA16_5==IDENTIFIER||(LA16_5 >= LBRACK && LA16_5 <= LCURLY)||LA16_5==NUMBER||(LA16_5 >= PHRASE && LA16_5 <= PHRASE_ANYTHING)||LA16_5==QMARK||LA16_5==REGEX||LA16_5==TERM_NORMAL||LA16_5==TERM_TRUNCATED) ) {
                        alt16=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 5, input);

                        throw nvae;

                    }
                }
                else if ( (LA16_2==EOF||LA16_2==AND||LA16_2==BAR||LA16_2==CARAT||LA16_2==IDENTIFIER||(LA16_2 >= LBRACK && LA16_2 <= LPAREN)||(LA16_2 >= NEAR && LA16_2 <= NUMBER)||(LA16_2 >= OR && LA16_2 <= PHRASE_ANYTHING)||LA16_2==QMARK||(LA16_2 >= REGEX && LA16_2 <= SECOND_ORDER_OP)||LA16_2==STAR||LA16_2==TERM_NORMAL||(LA16_2 >= TERM_TRUNCATED && LA16_2 <= TILDE)) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 2, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
            case LBRACK:
            case LCURLY:
            case NUMBER:
            case PHRASE:
            case PHRASE_ANYTHING:
            case QMARK:
            case REGEX:
            case TERM_TRUNCATED:
                {
                alt16=2;
                }
                break;
            case STAR:
                {
                alt16=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }

            switch (alt16) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:96:2: ( modifier )? field multi_value ( term_modifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:96:2: ( modifier )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==BAR) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:96:2: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom486);
                            modifier24=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier24.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_field_in_atom489);
                    field25=field();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_field.add(field25.getTree());

                    pushFollow(FOLLOW_multi_value_in_atom491);
                    multi_value26=multi_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_multi_value.add(multi_value26.getTree());

                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:96:30: ( term_modifier )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==CARAT||LA10_0==TILDE) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:96:30: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_atom493);
                            term_modifier27=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier27.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: field, term_modifier, modifier, multi_value
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 97:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:97:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(CLAUSE, "CLAUSE")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:97:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:97:26: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:97:36: ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:97:48: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_3, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:97:63: ^( FIELD field multi_value )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:98:4: ( modifier )? ( field )? value ( term_modifier )?
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:98:4: ( modifier )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==BAR) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:98:4: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom529);
                            modifier28=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier28.getTree());

                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:98:14: ( field )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==TERM_NORMAL) ) {
                        int LA12_1 = input.LA(2);

                        if ( (LA12_1==COLON) ) {
                            alt12=1;
                        }
                    }
                    switch (alt12) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:98:14: field
                            {
                            pushFollow(FOLLOW_field_in_atom532);
                            field29=field();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_field.add(field29.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_value_in_atom535);
                    value30=value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value.add(value30.getTree());

                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:98:27: ( term_modifier )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==CARAT||LA13_0==TILDE) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:98:27: term_modifier
                            {
                            pushFollow(FOLLOW_term_modifier_in_atom537);
                            term_modifier31=term_modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier31.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: field, value, modifier, term_modifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 99:3: -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:99:6: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:99:17: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:99:27: ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        root_2 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TMODIFIER, "TMODIFIER")
                        , root_2);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:99:39: ( term_modifier )?
                        if ( stream_term_modifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_term_modifier.nextTree());

                        }
                        stream_term_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:99:54: ^( FIELD ( field )? value )
                        {
                        Object root_3 = (Object)adaptor.nil();
                        root_3 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FIELD, "FIELD")
                        , root_3);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:99:62: ( field )?
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:100:4: ( modifier )? ( STAR COLON )? STAR
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:100:4: ( modifier )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==BAR) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:100:4: modifier
                            {
                            pushFollow(FOLLOW_modifier_in_atom571);
                            modifier32=modifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_modifier.add(modifier32.getTree());

                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:100:14: ( STAR COLON )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==STAR) ) {
                        int LA15_1 = input.LA(2);

                        if ( (LA15_1==COLON) ) {
                            alt15=1;
                        }
                    }
                    switch (alt15) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:100:15: STAR COLON
                            {
                            STAR33=(Token)match(input,STAR,FOLLOW_STAR_in_atom575); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_STAR.add(STAR33);


                            COLON34=(Token)match(input,COLON,FOLLOW_COLON_in_atom577); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COLON.add(COLON34);


                            }
                            break;

                    }


                    STAR35=(Token)match(input,STAR,FOLLOW_STAR_in_atom581); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR35);


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
                    // 101:3: -> ^( MODIFIER ( modifier )? ^( QANYTHING STAR[\"*\"] ) )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:101:6: ^( MODIFIER ( modifier )? ^( QANYTHING STAR[\"*\"] ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(MODIFIER, "MODIFIER")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:101:17: ( modifier )?
                        if ( stream_modifier.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifier.nextTree());

                        }
                        stream_modifier.reset();

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:101:27: ^( QANYTHING STAR[\"*\"] )
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:105:1: field : TERM_NORMAL COLON -> TERM_NORMAL ;
    public final InvenioParser.field_return field() throws RecognitionException {
        InvenioParser.field_return retval = new InvenioParser.field_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_NORMAL36=null;
        Token COLON37=null;

        Object TERM_NORMAL36_tree=null;
        Object COLON37_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_TERM_NORMAL=new RewriteRuleTokenStream(adaptor,"token TERM_NORMAL");

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:106:2: ( TERM_NORMAL COLON -> TERM_NORMAL )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:107:2: TERM_NORMAL COLON
            {
            TERM_NORMAL36=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_field618); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL36);


            COLON37=(Token)match(input,COLON,FOLLOW_COLON_in_field620); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(COLON37);


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
            // 107:20: -> TERM_NORMAL
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:110:1: value : ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) | REGEX -> ^( QREGEX REGEX ) );
    public final InvenioParser.value_return value() throws RecognitionException {
        InvenioParser.value_return retval = new InvenioParser.value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token QMARK44=null;
        Token REGEX45=null;
        InvenioParser.range_term_in_return range_term_in38 =null;

        InvenioParser.range_term_ex_return range_term_ex39 =null;

        InvenioParser.normal_return normal40 =null;

        InvenioParser.truncated_return truncated41 =null;

        InvenioParser.quoted_return quoted42 =null;

        InvenioParser.quoted_truncated_return quoted_truncated43 =null;


        Object QMARK44_tree=null;
        Object REGEX45_tree=null;
        RewriteRuleTokenStream stream_REGEX=new RewriteRuleTokenStream(adaptor,"token REGEX");
        RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
        RewriteRuleSubtreeStream stream_range_term_ex=new RewriteRuleSubtreeStream(adaptor,"rule range_term_ex");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_range_term_in=new RewriteRuleSubtreeStream(adaptor,"rule range_term_in");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:111:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) | REGEX -> ^( QREGEX REGEX ) )
            int alt17=8;
            switch ( input.LA(1) ) {
            case LBRACK:
                {
                alt17=1;
                }
                break;
            case LCURLY:
                {
                alt17=2;
                }
                break;
            case IDENTIFIER:
            case NUMBER:
            case TERM_NORMAL:
                {
                alt17=3;
                }
                break;
            case TERM_TRUNCATED:
                {
                alt17=4;
                }
                break;
            case PHRASE:
                {
                alt17=5;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt17=6;
                }
                break;
            case QMARK:
                {
                alt17=7;
                }
                break;
            case REGEX:
                {
                alt17=8;
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:112:2: range_term_in
                    {
                    pushFollow(FOLLOW_range_term_in_in_value639);
                    range_term_in38=range_term_in();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_in.add(range_term_in38.getTree());

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
                    // 112:16: -> ^( QRANGEIN range_term_in )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:112:19: ^( QRANGEIN range_term_in )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:113:4: range_term_ex
                    {
                    pushFollow(FOLLOW_range_term_ex_in_value652);
                    range_term_ex39=range_term_ex();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_range_term_ex.add(range_term_ex39.getTree());

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
                    // 113:18: -> ^( QRANGEEX range_term_ex )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:113:21: ^( QRANGEEX range_term_ex )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:114:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_value666);
                    normal40=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal40.getTree());

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
                    // 114:11: -> ^( QNORMAL normal )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:114:14: ^( QNORMAL normal )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:115:4: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_value680);
                    truncated41=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated41.getTree());

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
                    // 115:14: -> ^( QTRUNCATED truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:115:17: ^( QTRUNCATED truncated )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:116:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_value694);
                    quoted42=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted42.getTree());

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
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:116:14: ^( QPHRASE quoted )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:117:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_value707);
                    quoted_truncated43=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated43.getTree());

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
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:117:24: ^( QPHRASETRUNC quoted_truncated )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:118:4: QMARK
                    {
                    QMARK44=(Token)match(input,QMARK,FOLLOW_QMARK_in_value720); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QMARK.add(QMARK44);


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
                    // 118:10: -> ^( QTRUNCATED QMARK )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:118:13: ^( QTRUNCATED QMARK )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:119:4: REGEX
                    {
                    REGEX45=(Token)match(input,REGEX,FOLLOW_REGEX_in_value733); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_REGEX.add(REGEX45);


                    // AST REWRITE
                    // elements: REGEX
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 119:10: -> ^( QREGEX REGEX )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:119:13: ^( QREGEX REGEX )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QREGEX, "QREGEX")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_REGEX.nextNode()
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:124:1: range_term_in : LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK ;
    public final InvenioParser.range_term_in_return range_term_in() throws RecognitionException {
        InvenioParser.range_term_in_return retval = new InvenioParser.range_term_in_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LBRACK46=null;
        Token TO47=null;
        Token RBRACK48=null;
        InvenioParser.range_value_return a =null;

        InvenioParser.range_value_return b =null;


        Object LBRACK46_tree=null;
        Object TO47_tree=null;
        Object RBRACK48_tree=null;
        RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
        RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:125:2: ( LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:126:8: LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK
            {
            LBRACK46=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_range_term_in765); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK46);


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:127:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:127:9: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_in777);
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
            // 127:23: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:127:38: ^( QANYTHING QANYTHING[\"*\"] )
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


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:128:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==DATE_TOKEN||LA19_0==IDENTIFIER||LA19_0==NUMBER||(LA19_0 >= PHRASE && LA19_0 <= PHRASE_ANYTHING)||LA19_0==STAR||LA19_0==TERM_NORMAL||LA19_0==TERM_TRUNCATED||LA19_0==TO) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:128:10: ( TO )? b= range_value
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:128:10: ( TO )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==TO) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:128:10: TO
                            {
                            TO47=(Token)match(input,TO,FOLLOW_TO_in_range_term_in800); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO47);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_in805);
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
                    // 128:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:128:35: ( $b)?
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


            RBRACK48=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_range_term_in826); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACK.add(RBRACK48);


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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:133:1: range_term_ex : LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY ;
    public final InvenioParser.range_term_ex_return range_term_ex() throws RecognitionException {
        InvenioParser.range_term_ex_return retval = new InvenioParser.range_term_ex_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LCURLY49=null;
        Token TO50=null;
        Token RCURLY51=null;
        InvenioParser.range_value_return a =null;

        InvenioParser.range_value_return b =null;


        Object LCURLY49_tree=null;
        Object TO50_tree=null;
        Object RCURLY51_tree=null;
        RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
        RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:134:2: ( LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:135:8: LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY
            {
            LCURLY49=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_range_term_ex846); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LCURLY.add(LCURLY49);


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:136:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:136:10: a= range_value
            {
            pushFollow(FOLLOW_range_value_in_range_term_ex859);
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
            // 136:24: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
            {
                adaptor.addChild(root_0, stream_range_value.nextTree());

                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:136:39: ^( QANYTHING QANYTHING[\"*\"] )
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


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:137:8: ( ( TO )? b= range_value -> $a ( $b)? )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==DATE_TOKEN||LA21_0==IDENTIFIER||LA21_0==NUMBER||(LA21_0 >= PHRASE && LA21_0 <= PHRASE_ANYTHING)||LA21_0==STAR||LA21_0==TERM_NORMAL||LA21_0==TERM_TRUNCATED||LA21_0==TO) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:137:10: ( TO )? b= range_value
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:137:10: ( TO )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==TO) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:137:10: TO
                            {
                            TO50=(Token)match(input,TO,FOLLOW_TO_in_range_term_ex882); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TO.add(TO50);


                            }
                            break;

                    }


                    pushFollow(FOLLOW_range_value_in_range_term_ex887);
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
                    // 137:28: -> $a ( $b)?
                    {
                        adaptor.addChild(root_0, stream_a.nextTree());

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:137:35: ( $b)?
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


            RCURLY51=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_range_term_ex908); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RCURLY.add(RCURLY51);


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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:141:1: range_value : ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) );
    public final InvenioParser.range_value_return range_value() throws RecognitionException {
        InvenioParser.range_value_return retval = new InvenioParser.range_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token STAR57=null;
        InvenioParser.truncated_return truncated52 =null;

        InvenioParser.quoted_return quoted53 =null;

        InvenioParser.quoted_truncated_return quoted_truncated54 =null;

        InvenioParser.date_return date55 =null;

        InvenioParser.normal_return normal56 =null;


        Object STAR57_tree=null;
        RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
        RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
        RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
        RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");
        RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
        RewriteRuleSubtreeStream stream_date=new RewriteRuleSubtreeStream(adaptor,"rule date");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:142:2: ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) )
            int alt22=6;
            switch ( input.LA(1) ) {
            case TERM_TRUNCATED:
                {
                alt22=1;
                }
                break;
            case PHRASE:
                {
                alt22=2;
                }
                break;
            case PHRASE_ANYTHING:
                {
                alt22=3;
                }
                break;
            case DATE_TOKEN:
                {
                alt22=4;
                }
                break;
            case IDENTIFIER:
            case NUMBER:
            case TERM_NORMAL:
                {
                alt22=5;
                }
                break;
            case STAR:
                {
                alt22=6;
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:143:2: truncated
                    {
                    pushFollow(FOLLOW_truncated_in_range_value922);
                    truncated52=truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_truncated.add(truncated52.getTree());

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
                    // 143:12: -> ^( QTRUNCATED truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:143:15: ^( QTRUNCATED truncated )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:144:4: quoted
                    {
                    pushFollow(FOLLOW_quoted_in_range_value935);
                    quoted53=quoted();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted.add(quoted53.getTree());

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
                    // 144:11: -> ^( QPHRASE quoted )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:144:14: ^( QPHRASE quoted )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:145:4: quoted_truncated
                    {
                    pushFollow(FOLLOW_quoted_truncated_in_range_value948);
                    quoted_truncated54=quoted_truncated();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated54.getTree());

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
                    // 145:21: -> ^( QPHRASETRUNC quoted_truncated )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:145:24: ^( QPHRASETRUNC quoted_truncated )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:146:4: date
                    {
                    pushFollow(FOLLOW_date_in_range_value961);
                    date55=date();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_date.add(date55.getTree());

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
                    // 146:9: -> ^( QNORMAL date )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:146:12: ^( QNORMAL date )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:147:4: normal
                    {
                    pushFollow(FOLLOW_normal_in_range_value974);
                    normal56=normal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_normal.add(normal56.getTree());

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
                    // 147:11: -> ^( QNORMAL normal )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:147:14: ^( QNORMAL normal )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:148:4: STAR
                    {
                    STAR57=(Token)match(input,STAR,FOLLOW_STAR_in_range_value988); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STAR.add(STAR57);


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
                    // 148:9: -> ^( QANYTHING STAR )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:148:12: ^( QANYTHING STAR )
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:151:1: multi_value : LPAREN multiClause RPAREN -> multiClause ;
    public final InvenioParser.multi_value_return multi_value() throws RecognitionException {
        InvenioParser.multi_value_return retval = new InvenioParser.multi_value_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN58=null;
        Token RPAREN60=null;
        InvenioParser.multiClause_return multiClause59 =null;


        Object LPAREN58_tree=null;
        Object RPAREN60_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_multiClause=new RewriteRuleSubtreeStream(adaptor,"rule multiClause");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:152:2: ( LPAREN multiClause RPAREN -> multiClause )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:153:2: LPAREN multiClause RPAREN
            {
            LPAREN58=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_multi_value1009); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN58);


            pushFollow(FOLLOW_multiClause_in_multi_value1011);
            multiClause59=multiClause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_multiClause.add(multiClause59.getTree());

            RPAREN60=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_multi_value1013); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN60);


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
            // 153:28: -> multiClause
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:158:1: multiClause : clauseTop ;
    public final InvenioParser.multiClause_return multiClause() throws RecognitionException {
        InvenioParser.multiClause_return retval = new InvenioParser.multiClause_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.clauseTop_return clauseTop61 =null;



        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:159:2: ( clauseTop )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:165:2: clauseTop
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_clauseTop_in_multiClause1042);
            clauseTop61=clauseTop();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, clauseTop61.getTree());

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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:178:1: multiDefault : ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) ;
    public final InvenioParser.multiDefault_return multiDefault() throws RecognitionException {
        InvenioParser.multiDefault_return retval = new InvenioParser.multiDefault_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.multiOr_return multiOr62 =null;


        RewriteRuleSubtreeStream stream_multiOr=new RewriteRuleSubtreeStream(adaptor,"rule multiOr");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:179:2: ( ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:180:2: ( multiOr )+
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:180:2: ( multiOr )+
            int cnt23=0;
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==BAR||LA23_0==IDENTIFIER||(LA23_0 >= LBRACK && LA23_0 <= LCURLY)||LA23_0==NUMBER||(LA23_0 >= PHRASE && LA23_0 <= PHRASE_ANYTHING)||LA23_0==QMARK||LA23_0==REGEX||LA23_0==TERM_NORMAL||LA23_0==TERM_TRUNCATED) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:180:2: multiOr
            	    {
            	    pushFollow(FOLLOW_multiOr_in_multiDefault1075);
            	    multiOr62=multiOr();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_multiOr.add(multiOr62.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt23 >= 1 ) break loop23;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(23, input);
                        throw eee;
                }
                cnt23++;
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
            // 180:11: -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
            {
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:180:14: ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:183:1: multiOr : (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* ;
    public final InvenioParser.multiOr_return multiOr() throws RecognitionException {
        InvenioParser.multiOr_return retval = new InvenioParser.multiOr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.multiAnd_return first =null;

        InvenioParser.multiAnd_return others =null;

        InvenioParser.or_return or63 =null;


        RewriteRuleSubtreeStream stream_multiAnd=new RewriteRuleSubtreeStream(adaptor,"rule multiAnd");
        RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:184:2: ( (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:185:2: (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:185:2: (first= multiAnd -> $first)
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:185:3: first= multiAnd
            {
            pushFollow(FOLLOW_multiAnd_in_multiOr1103);
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
            // 185:19: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:185:30: ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==OR) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:185:31: or others= multiAnd
            	    {
            	    pushFollow(FOLLOW_or_in_multiOr1113);
            	    or63=or();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_or.add(or63.getTree());

            	    pushFollow(FOLLOW_multiAnd_in_multiOr1117);
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
            	    // 185:49: -> ^( OPERATOR[\"OR\"] ( multiAnd )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:185:52: ^( OPERATOR[\"OR\"] ( multiAnd )+ )
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
            	    break loop24;
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:188:1: multiAnd : (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* ;
    public final InvenioParser.multiAnd_return multiAnd() throws RecognitionException {
        InvenioParser.multiAnd_return retval = new InvenioParser.multiAnd_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.multiNot_return first =null;

        InvenioParser.multiNot_return others =null;

        InvenioParser.and_return and64 =null;


        RewriteRuleSubtreeStream stream_multiNot=new RewriteRuleSubtreeStream(adaptor,"rule multiNot");
        RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:189:2: ( (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:190:2: (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:190:2: (first= multiNot -> $first)
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:190:3: first= multiNot
            {
            pushFollow(FOLLOW_multiNot_in_multiAnd1148);
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
            // 190:19: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:190:30: ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==AND) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:190:31: and others= multiNot
            	    {
            	    pushFollow(FOLLOW_and_in_multiAnd1158);
            	    and64=and();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_and.add(and64.getTree());

            	    pushFollow(FOLLOW_multiNot_in_multiAnd1162);
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
            	    // 190:51: -> ^( OPERATOR[\"AND\"] ( multiNot )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:190:54: ^( OPERATOR[\"AND\"] ( multiNot )+ )
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
            	    break loop25;
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:193:1: multiNot : (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )* ;
    public final InvenioParser.multiNot_return multiNot() throws RecognitionException {
        InvenioParser.multiNot_return retval = new InvenioParser.multiNot_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.multiNear_return first =null;

        InvenioParser.multiNear_return others =null;

        InvenioParser.not_return not65 =null;


        RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
        RewriteRuleSubtreeStream stream_multiNear=new RewriteRuleSubtreeStream(adaptor,"rule multiNear");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:194:2: ( (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )* )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:195:2: (first= multiNear -> $first) ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:195:2: (first= multiNear -> $first)
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:195:3: first= multiNear
            {
            pushFollow(FOLLOW_multiNear_in_multiNot1193);
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
            // 195:20: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:195:31: ( not others= multiNear -> ^( OPERATOR[\"NOT\"] ( multiNear )+ ) )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==AND) ) {
                    int LA26_1 = input.LA(2);

                    if ( (LA26_1==NOT) ) {
                        alt26=1;
                    }


                }
                else if ( (LA26_0==NOT) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:195:32: not others= multiNear
            	    {
            	    pushFollow(FOLLOW_not_in_multiNot1203);
            	    not65=not();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_not.add(not65.getTree());

            	    pushFollow(FOLLOW_multiNear_in_multiNot1207);
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
            	    // 195:52: -> ^( OPERATOR[\"NOT\"] ( multiNear )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:195:55: ^( OPERATOR[\"NOT\"] ( multiNear )+ )
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
            	    break loop26;
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:198:1: multiNear : (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )* ;
    public final InvenioParser.multiNear_return multiNear() throws RecognitionException {
        InvenioParser.multiNear_return retval = new InvenioParser.multiNear_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.multiBasic_return first =null;

        InvenioParser.multiBasic_return others =null;

        InvenioParser.near_return near66 =null;


        RewriteRuleSubtreeStream stream_near=new RewriteRuleSubtreeStream(adaptor,"rule near");
        RewriteRuleSubtreeStream stream_multiBasic=new RewriteRuleSubtreeStream(adaptor,"rule multiBasic");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:199:2: ( (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )* )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:200:2: (first= multiBasic -> $first) ( near others= multiBasic -> ^( near ( multiBasic )+ ) )*
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:200:2: (first= multiBasic -> $first)
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:200:3: first= multiBasic
            {
            pushFollow(FOLLOW_multiBasic_in_multiNear1237);
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
            // 200:21: -> $first
            {
                adaptor.addChild(root_0, stream_first.nextTree());

            }


            retval.tree = root_0;
            }

            }


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:200:32: ( near others= multiBasic -> ^( near ( multiBasic )+ ) )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==NEAR) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:200:33: near others= multiBasic
            	    {
            	    pushFollow(FOLLOW_near_in_multiNear1247);
            	    near66=near();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_near.add(near66.getTree());

            	    pushFollow(FOLLOW_multiBasic_in_multiNear1251);
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
            	    // 200:55: -> ^( near ( multiBasic )+ )
            	    {
            	        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:200:58: ^( near ( multiBasic )+ )
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
            	    break loop27;
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:204:1: multiBasic : mterm ;
    public final InvenioParser.multiBasic_return multiBasic() throws RecognitionException {
        InvenioParser.multiBasic_return retval = new InvenioParser.multiBasic_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.mterm_return mterm67 =null;



        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:205:2: ( mterm )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:206:2: mterm
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_mterm_in_multiBasic1277);
            mterm67=mterm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, mterm67.getTree());

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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:209:1: mterm : ( modifier )? value -> ^( MODIFIER ( modifier )? value ) ;
    public final InvenioParser.mterm_return mterm() throws RecognitionException {
        InvenioParser.mterm_return retval = new InvenioParser.mterm_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        InvenioParser.modifier_return modifier68 =null;

        InvenioParser.value_return value69 =null;


        RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:210:2: ( ( modifier )? value -> ^( MODIFIER ( modifier )? value ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:211:2: ( modifier )? value
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:211:2: ( modifier )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==BAR) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:211:2: modifier
                    {
                    pushFollow(FOLLOW_modifier_in_mterm1293);
                    modifier68=modifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_modifier.add(modifier68.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_value_in_mterm1296);
            value69=value();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_value.add(value69.getTree());

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
            // 211:18: -> ^( MODIFIER ( modifier )? value )
            {
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:211:21: ^( MODIFIER ( modifier )? value )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(MODIFIER, "MODIFIER")
                , root_1);

                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:211:32: ( modifier )?
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:215:1: normal : ( TERM_NORMAL | NUMBER | IDENTIFIER );
    public final InvenioParser.normal_return normal() throws RecognitionException {
        InvenioParser.normal_return retval = new InvenioParser.normal_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set70=null;

        Object set70_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:216:2: ( TERM_NORMAL | NUMBER | IDENTIFIER )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
            {
            root_0 = (Object)adaptor.nil();


            set70=(Token)input.LT(1);

            if ( input.LA(1)==IDENTIFIER||input.LA(1)==NUMBER||input.LA(1)==TERM_NORMAL ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set70)
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:225:1: truncated : TERM_TRUNCATED ;
    public final InvenioParser.truncated_return truncated() throws RecognitionException {
        InvenioParser.truncated_return retval = new InvenioParser.truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TERM_TRUNCATED71=null;

        Object TERM_TRUNCATED71_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:226:2: ( TERM_TRUNCATED )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:227:2: TERM_TRUNCATED
            {
            root_0 = (Object)adaptor.nil();


            TERM_TRUNCATED71=(Token)match(input,TERM_TRUNCATED,FOLLOW_TERM_TRUNCATED_in_truncated1354); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            TERM_TRUNCATED71_tree = 
            (Object)adaptor.create(TERM_TRUNCATED71)
            ;
            adaptor.addChild(root_0, TERM_TRUNCATED71_tree);
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:231:1: quoted_truncated : PHRASE_ANYTHING ;
    public final InvenioParser.quoted_truncated_return quoted_truncated() throws RecognitionException {
        InvenioParser.quoted_truncated_return retval = new InvenioParser.quoted_truncated_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE_ANYTHING72=null;

        Object PHRASE_ANYTHING72_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:232:2: ( PHRASE_ANYTHING )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:233:2: PHRASE_ANYTHING
            {
            root_0 = (Object)adaptor.nil();


            PHRASE_ANYTHING72=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1369); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE_ANYTHING72_tree = 
            (Object)adaptor.create(PHRASE_ANYTHING72)
            ;
            adaptor.addChild(root_0, PHRASE_ANYTHING72_tree);
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:236:1: quoted : PHRASE ;
    public final InvenioParser.quoted_return quoted() throws RecognitionException {
        InvenioParser.quoted_return retval = new InvenioParser.quoted_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE73=null;

        Object PHRASE73_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:236:8: ( PHRASE )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:237:2: PHRASE
            {
            root_0 = (Object)adaptor.nil();


            PHRASE73=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_quoted1381); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PHRASE73_tree = 
            (Object)adaptor.create(PHRASE73)
            ;
            adaptor.addChild(root_0, PHRASE73_tree);
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:243:1: operator : ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] ) ;
    public final InvenioParser.operator_return operator() throws RecognitionException {
        InvenioParser.operator_return retval = new InvenioParser.operator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND74=null;
        Token OR75=null;
        Token NOT76=null;
        Token NEAR77=null;

        Object AND74_tree=null;
        Object OR75_tree=null;
        Object NOT76_tree=null;
        Object NEAR77_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:243:9: ( ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:243:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] )
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:243:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] | NEAR -> OPERATOR[\"NEAR\"] )
            int alt29=4;
            switch ( input.LA(1) ) {
            case AND:
                {
                alt29=1;
                }
                break;
            case OR:
                {
                alt29=2;
                }
                break;
            case NOT:
                {
                alt29=3;
                }
                break;
            case NEAR:
                {
                alt29=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;

            }

            switch (alt29) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:244:2: AND
                    {
                    AND74=(Token)match(input,AND,FOLLOW_AND_in_operator1397); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AND.add(AND74);


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
                    // 244:6: -> OPERATOR[\"AND\"]
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:245:4: OR
                    {
                    OR75=(Token)match(input,OR,FOLLOW_OR_in_operator1407); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_OR.add(OR75);


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
                    // 245:7: -> OPERATOR[\"OR\"]
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:246:4: NOT
                    {
                    NOT76=(Token)match(input,NOT,FOLLOW_NOT_in_operator1417); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NOT.add(NOT76);


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
                    // 246:8: -> OPERATOR[\"NOT\"]
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:247:4: NEAR
                    {
                    NEAR77=(Token)match(input,NEAR,FOLLOW_NEAR_in_operator1427); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEAR.add(NEAR77);


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
                    // 247:9: -> OPERATOR[\"NEAR\"]
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:250:1: modifier : BAR ;
    public final InvenioParser.modifier_return modifier() throws RecognitionException {
        InvenioParser.modifier_return retval = new InvenioParser.modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token BAR78=null;

        Object BAR78_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:250:9: ( BAR )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:253:2: BAR
            {
            root_0 = (Object)adaptor.nil();


            BAR78=(Token)match(input,BAR,FOLLOW_BAR_in_modifier1447); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BAR78_tree = 
            (Object)adaptor.create(BAR78)
            ;
            adaptor.addChild(root_0, BAR78_tree);
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:266:1: term_modifier : ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) );
    public final InvenioParser.term_modifier_return term_modifier() throws RecognitionException {
        InvenioParser.term_modifier_return retval = new InvenioParser.term_modifier_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE79=null;
        Token CARAT80=null;
        Token CARAT81=null;
        Token TILDE82=null;

        Object TILDE79_tree=null;
        Object CARAT80_tree=null;
        Object CARAT81_tree=null;
        Object TILDE82_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:266:15: ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==TILDE) ) {
                alt32=1;
            }
            else if ( (LA32_0==CARAT) ) {
                alt32=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;

            }
            switch (alt32) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:267:2: TILDE ( CARAT )?
                    {
                    TILDE79=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1462); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_TILDE.add(TILDE79);


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:267:8: ( CARAT )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==CARAT) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:267:8: CARAT
                            {
                            CARAT80=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1464); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_CARAT.add(CARAT80);


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
                    // 267:15: -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:267:18: ^( BOOST ( CARAT )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(BOOST, "BOOST")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:267:26: ( CARAT )?
                        if ( stream_CARAT.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_CARAT.nextNode()
                            );

                        }
                        stream_CARAT.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:267:34: ^( FUZZY TILDE )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:268:4: CARAT ( TILDE )?
                    {
                    CARAT81=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1486); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CARAT.add(CARAT81);


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:268:10: ( TILDE )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==TILDE) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:268:10: TILDE
                            {
                            TILDE82=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1488); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_TILDE.add(TILDE82);


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
                    // 268:17: -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:268:20: ^( BOOST CARAT )
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

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:268:35: ^( FUZZY ( TILDE )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(FUZZY, "FUZZY")
                        , root_1);

                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:268:43: ( TILDE )?
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:288:1: boost : ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? ;
    public final InvenioParser.boost_return boost() throws RecognitionException {
        InvenioParser.boost_return retval = new InvenioParser.boost_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token CARAT83=null;
        Token NUMBER84=null;

        Object CARAT83_tree=null;
        Object NUMBER84_tree=null;
        RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:288:7: ( ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:289:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )?
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:289:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:289:3: CARAT
            {
            CARAT83=(Token)match(input,CARAT,FOLLOW_CARAT_in_boost1520); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CARAT.add(CARAT83);


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
            // 289:9: -> ^( BOOST NUMBER[\"DEF\"] )
            {
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:289:12: ^( BOOST NUMBER[\"DEF\"] )
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


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:290:2: ( NUMBER -> ^( BOOST NUMBER ) )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==NUMBER) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:290:3: NUMBER
                    {
                    NUMBER84=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_boost1535); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER84);


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
                    // 290:10: -> ^( BOOST NUMBER )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:290:13: ^( BOOST NUMBER )
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:293:1: fuzzy : ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? ;
    public final InvenioParser.fuzzy_return fuzzy() throws RecognitionException {
        InvenioParser.fuzzy_return retval = new InvenioParser.fuzzy_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token TILDE85=null;
        Token NUMBER86=null;

        Object TILDE85_tree=null;
        Object NUMBER86_tree=null;
        RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:293:7: ( ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:294:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )?
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:294:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:294:3: TILDE
            {
            TILDE85=(Token)match(input,TILDE,FOLLOW_TILDE_in_fuzzy1558); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TILDE.add(TILDE85);


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
            // 294:9: -> ^( FUZZY NUMBER[\"DEF\"] )
            {
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:294:12: ^( FUZZY NUMBER[\"DEF\"] )
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


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:295:2: ( NUMBER -> ^( FUZZY NUMBER ) )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==NUMBER) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:295:3: NUMBER
                    {
                    NUMBER86=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_fuzzy1573); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER86);


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
                    // 295:10: -> ^( FUZZY NUMBER )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:295:13: ^( FUZZY NUMBER )
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:298:1: not : ( ( AND NOT )=> AND NOT | NOT );
    public final InvenioParser.not_return not() throws RecognitionException {
        InvenioParser.not_return retval = new InvenioParser.not_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND87=null;
        Token NOT88=null;
        Token NOT89=null;

        Object AND87_tree=null;
        Object NOT88_tree=null;
        Object NOT89_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:298:5: ( ( AND NOT )=> AND NOT | NOT )
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==AND) && (synpred4_Invenio())) {
                alt35=1;
            }
            else if ( (LA35_0==NOT) ) {
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:299:2: ( AND NOT )=> AND NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    AND87=(Token)match(input,AND,FOLLOW_AND_in_not1603); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    AND87_tree = 
                    (Object)adaptor.create(AND87)
                    ;
                    adaptor.addChild(root_0, AND87_tree);
                    }

                    NOT88=(Token)match(input,NOT,FOLLOW_NOT_in_not1605); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT88_tree = 
                    (Object)adaptor.create(NOT88)
                    ;
                    adaptor.addChild(root_0, NOT88_tree);
                    }

                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:300:4: NOT
                    {
                    root_0 = (Object)adaptor.nil();


                    NOT89=(Token)match(input,NOT,FOLLOW_NOT_in_not1610); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NOT89_tree = 
                    (Object)adaptor.create(NOT89)
                    ;
                    adaptor.addChild(root_0, NOT89_tree);
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:303:1: and : AND ;
    public final InvenioParser.and_return and() throws RecognitionException {
        InvenioParser.and_return retval = new InvenioParser.and_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token AND90=null;

        Object AND90_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:303:6: ( AND )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:304:2: AND
            {
            root_0 = (Object)adaptor.nil();


            AND90=(Token)match(input,AND,FOLLOW_AND_in_and1624); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            AND90_tree = 
            (Object)adaptor.create(AND90)
            ;
            adaptor.addChild(root_0, AND90_tree);
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:307:1: or : OR ;
    public final InvenioParser.or_return or() throws RecognitionException {
        InvenioParser.or_return retval = new InvenioParser.or_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OR91=null;

        Object OR91_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:307:5: ( OR )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:308:2: OR
            {
            root_0 = (Object)adaptor.nil();


            OR91=(Token)match(input,OR,FOLLOW_OR_in_or1638); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            OR91_tree = 
            (Object)adaptor.create(OR91)
            ;
            adaptor.addChild(root_0, OR91_tree);
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:311:1: near : ( NEAR -> ^( OPERATOR[\"NEAR\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )? ;
    public final InvenioParser.near_return near() throws RecognitionException {
        InvenioParser.near_return retval = new InvenioParser.near_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token b=null;
        Token NEAR92=null;
        Token char_literal93=null;

        Object b_tree=null;
        Object NEAR92_tree=null;
        Object char_literal93_tree=null;
        RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");
        RewriteRuleTokenStream stream_SLASH=new RewriteRuleTokenStream(adaptor,"token SLASH");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:311:6: ( ( NEAR -> ^( OPERATOR[\"NEAR\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )? )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:312:2: ( NEAR -> ^( OPERATOR[\"NEAR\"] ) ) ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )?
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:312:2: ( NEAR -> ^( OPERATOR[\"NEAR\"] ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:312:3: NEAR
            {
            NEAR92=(Token)match(input,NEAR,FOLLOW_NEAR_in_near1653); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NEAR.add(NEAR92);


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
            // 312:8: -> ^( OPERATOR[\"NEAR\"] )
            {
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:312:11: ^( OPERATOR[\"NEAR\"] )
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


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:313:2: ( '/' b= NUMBER -> ^( OPERATOR[\"NEAR:\" + $b.getText()] ) )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==SLASH) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:313:3: '/' b= NUMBER
                    {
                    char_literal93=(Token)match(input,SLASH,FOLLOW_SLASH_in_near1666); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SLASH.add(char_literal93);


                    b=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_near1670); if (state.failed) return retval; 
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
                    // 313:16: -> ^( OPERATOR[\"NEAR:\" + $b.getText()] )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:313:19: ^( OPERATOR[\"NEAR:\" + $b.getText()] )
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
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:316:1: date : DATE_TOKEN ;
    public final InvenioParser.date_return date() throws RecognitionException {
        InvenioParser.date_return retval = new InvenioParser.date_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token DATE_TOKEN94=null;

        Object DATE_TOKEN94_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:316:6: ( DATE_TOKEN )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:318:2: DATE_TOKEN
            {
            root_0 = (Object)adaptor.nil();


            DATE_TOKEN94=(Token)match(input,DATE_TOKEN,FOLLOW_DATE_TOKEN_in_date1694); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            DATE_TOKEN94_tree = 
            (Object)adaptor.create(DATE_TOKEN94)
            ;
            adaptor.addChild(root_0, DATE_TOKEN94_tree);
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


    public static class second_order_op_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "second_order_op"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:321:1: second_order_op : o= SECOND_ORDER_OP COLON -> ^( QFUNC[$o.getText()] ) ;
    public final InvenioParser.second_order_op_return second_order_op() throws RecognitionException {
        InvenioParser.second_order_op_return retval = new InvenioParser.second_order_op_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token o=null;
        Token COLON95=null;

        Object o_tree=null;
        Object COLON95_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_SECOND_ORDER_OP=new RewriteRuleTokenStream(adaptor,"token SECOND_ORDER_OP");

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:322:2: (o= SECOND_ORDER_OP COLON -> ^( QFUNC[$o.getText()] ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:323:2: o= SECOND_ORDER_OP COLON
            {
            o=(Token)match(input,SECOND_ORDER_OP,FOLLOW_SECOND_ORDER_OP_in_second_order_op1709); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SECOND_ORDER_OP.add(o);


            COLON95=(Token)match(input,COLON,FOLLOW_COLON_in_second_order_op1711); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_COLON.add(COLON95);


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
            // 323:26: -> ^( QFUNC[$o.getText()] )
            {
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:323:29: ^( QFUNC[$o.getText()] )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(QFUNC, o.getText())
                , root_1);

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
    // $ANTLR end "second_order_op"

    // $ANTLR start synpred1_Invenio
    public final void synpred1_Invenio_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:83:2: ( modifier LPAREN clauseOr RPAREN )
        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:83:3: modifier LPAREN clauseOr RPAREN
        {
        pushFollow(FOLLOW_modifier_in_synpred1_Invenio328);
        modifier();

        state._fsp--;
        if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred1_Invenio330); if (state.failed) return ;

        pushFollow(FOLLOW_clauseOr_in_synpred1_Invenio332);
        clauseOr();

        state._fsp--;
        if (state.failed) return ;

        match(input,RPAREN,FOLLOW_RPAREN_in_synpred1_Invenio334); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred1_Invenio

    // $ANTLR start synpred2_Invenio
    public final void synpred2_Invenio_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:85:4: ( LPAREN clauseOr RPAREN term_modifier )
        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:85:5: LPAREN clauseOr RPAREN term_modifier
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred2_Invenio380); if (state.failed) return ;

        pushFollow(FOLLOW_clauseOr_in_synpred2_Invenio382);
        clauseOr();

        state._fsp--;
        if (state.failed) return ;

        match(input,RPAREN,FOLLOW_RPAREN_in_synpred2_Invenio384); if (state.failed) return ;

        pushFollow(FOLLOW_term_modifier_in_synpred2_Invenio386);
        term_modifier();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_Invenio

    // $ANTLR start synpred3_Invenio
    public final void synpred3_Invenio_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:87:4: ( LPAREN )
        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:87:5: LPAREN
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred3_Invenio431); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred3_Invenio

    // $ANTLR start synpred4_Invenio
    public final void synpred4_Invenio_fragment() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:299:2: ( AND NOT )
        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:299:3: AND NOT
        {
        match(input,AND,FOLLOW_AND_in_synpred4_Invenio1597); if (state.failed) return ;

        match(input,NOT,FOLLOW_NOT_in_synpred4_Invenio1599); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred4_Invenio

    // Delegated rules

    public final boolean synpred1_Invenio() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_Invenio_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_Invenio() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_Invenio_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_Invenio() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_Invenio_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_Invenio() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_Invenio_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_operator_in_mainQ150 = new BitSet(new long[]{0x00A94010C8740100L});
    public static final BitSet FOLLOW_clauseTop_in_mainQ152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clauseTop_in_mainQ168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clauseOr_in_clauseTop188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseOr209 = new BitSet(new long[]{0x00A94010EE740142L});
    public static final BitSet FOLLOW_operator_in_clauseOr219 = new BitSet(new long[]{0x00A94010C8740100L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseOr223 = new BitSet(new long[]{0x00A94010EE740142L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseOr243 = new BitSet(new long[]{0x00A94010EE740142L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseBare274 = new BitSet(new long[]{0x00A94010C8740102L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseBare286 = new BitSet(new long[]{0x00A94010C8740102L});
    public static final BitSet FOLLOW_modifier_in_clauseBasic339 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic342 = new BitSet(new long[]{0x00A94010C8740100L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic344 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic346 = new BitSet(new long[]{0x0100000000000402L});
    public static final BitSet FOLLOW_term_modifier_in_clauseBasic348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_clauseBasic390 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic393 = new BitSet(new long[]{0x00A94010C8740100L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic395 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic397 = new BitSet(new long[]{0x0100000000000402L});
    public static final BitSet FOLLOW_term_modifier_in_clauseBasic399 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_clauseBasic436 = new BitSet(new long[]{0x00A94010C8740100L});
    public static final BitSet FOLLOW_clauseOr_in_clauseBasic438 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_RPAREN_in_clauseBasic440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_second_order_op_in_clauseBasic451 = new BitSet(new long[]{0x00A94010C8740100L});
    public static final BitSet FOLLOW_clauseBasic_in_clauseBasic453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_clauseBasic466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom486 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_field_in_atom489 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_multi_value_in_atom491 = new BitSet(new long[]{0x0100000000000402L});
    public static final BitSet FOLLOW_term_modifier_in_atom493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom529 = new BitSet(new long[]{0x00A04010C8340000L});
    public static final BitSet FOLLOW_field_in_atom532 = new BitSet(new long[]{0x00A04010C8340000L});
    public static final BitSet FOLLOW_value_in_atom535 = new BitSet(new long[]{0x0100000000000402L});
    public static final BitSet FOLLOW_term_modifier_in_atom537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_atom571 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_STAR_in_atom575 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_COLON_in_atom577 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_STAR_in_atom581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_NORMAL_in_field618 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_COLON_in_field620 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_in_in_value639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_term_ex_in_value652 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_value666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_value680 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_value694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_value707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QMARK_in_value720 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REGEX_in_value733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACK_in_range_term_in765 = new BitSet(new long[]{0x00A80000C8042000L});
    public static final BitSet FOLLOW_range_value_in_range_term_in777 = new BitSet(new long[]{0x04A81000C8042000L});
    public static final BitSet FOLLOW_TO_in_range_term_in800 = new BitSet(new long[]{0x00A80000C8042000L});
    public static final BitSet FOLLOW_range_value_in_range_term_in805 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_RBRACK_in_range_term_in826 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LCURLY_in_range_term_ex846 = new BitSet(new long[]{0x00A80000C8042000L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex859 = new BitSet(new long[]{0x04A82000C8042000L});
    public static final BitSet FOLLOW_TO_in_range_term_ex882 = new BitSet(new long[]{0x00A80000C8042000L});
    public static final BitSet FOLLOW_range_value_in_range_term_ex887 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_RCURLY_in_range_term_ex908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_truncated_in_range_value922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_in_range_value935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_truncated_in_range_value948 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_in_range_value961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normal_in_range_value974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAR_in_range_value988 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_multi_value1009 = new BitSet(new long[]{0x00A94010C8740100L});
    public static final BitSet FOLLOW_multiClause_in_multi_value1011 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_RPAREN_in_multi_value1013 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clauseTop_in_multiClause1042 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiOr_in_multiDefault1075 = new BitSet(new long[]{0x00A04010C8340102L});
    public static final BitSet FOLLOW_multiAnd_in_multiOr1103 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_or_in_multiOr1113 = new BitSet(new long[]{0x00A04010C8340100L});
    public static final BitSet FOLLOW_multiAnd_in_multiOr1117 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_multiNot_in_multiAnd1148 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_and_in_multiAnd1158 = new BitSet(new long[]{0x00A04010C8340100L});
    public static final BitSet FOLLOW_multiNot_in_multiAnd1162 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_multiNear_in_multiNot1193 = new BitSet(new long[]{0x0000000004000042L});
    public static final BitSet FOLLOW_not_in_multiNot1203 = new BitSet(new long[]{0x00A04010C8340100L});
    public static final BitSet FOLLOW_multiNear_in_multiNot1207 = new BitSet(new long[]{0x0000000004000042L});
    public static final BitSet FOLLOW_multiBasic_in_multiNear1237 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_near_in_multiNear1247 = new BitSet(new long[]{0x00A04010C8340100L});
    public static final BitSet FOLLOW_multiBasic_in_multiNear1251 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_mterm_in_multiBasic1277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_mterm1293 = new BitSet(new long[]{0x00A04010C8340000L});
    public static final BitSet FOLLOW_value_in_mterm1296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TERM_TRUNCATED_in_truncated1354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_in_quoted1381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_operator1397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_operator1407 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_operator1417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_operator1427 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BAR_in_modifier1447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1462 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_term_modifier1486 = new BitSet(new long[]{0x0100000000000002L});
    public static final BitSet FOLLOW_TILDE_in_term_modifier1488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARAT_in_boost1520 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_NUMBER_in_boost1535 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_fuzzy1558 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_NUMBER_in_fuzzy1573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_not1603 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NOT_in_not1605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_not1610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_and1624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_or1638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEAR_in_near1653 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_SLASH_in_near1666 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_NUMBER_in_near1670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DATE_TOKEN_in_date1694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SECOND_ORDER_OP_in_second_order_op1709 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_COLON_in_second_order_op1711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_synpred1_Invenio328 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred1_Invenio330 = new BitSet(new long[]{0x00A94010C8740100L});
    public static final BitSet FOLLOW_clauseOr_in_synpred1_Invenio332 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred1_Invenio334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred2_Invenio380 = new BitSet(new long[]{0x00A94010C8740100L});
    public static final BitSet FOLLOW_clauseOr_in_synpred2_Invenio382 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred2_Invenio384 = new BitSet(new long[]{0x0100000000000400L});
    public static final BitSet FOLLOW_term_modifier_in_synpred2_Invenio386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred3_Invenio431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_synpred4_Invenio1597 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_NOT_in_synpred4_Invenio1599 = new BitSet(new long[]{0x0000000000000002L});

}