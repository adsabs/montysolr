// $ANTLR 3.5.2 /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g 2020-02-10 19:29:04

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class ADSParser extends UnforgivingParser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "AS_CHAR", "ATOM", "AUTHOR_SEARCH", 
		"BOOST", "CARAT", "CLAUSE", "COLON", "COMMA", "DATE_RANGE", "DATE_TOKEN", 
		"DQUOTE", "D_NUMBER", "EQUAL", "ESC_CHAR", "FIELD", "FUNC_NAME", "FUZZY", 
		"HASH", "HOUR", "H_NUMBER", "INT", "LBRACK", "LOCAL_PARAMS", "LPAREN", 
		"MINUS", "MODIFIER", "M_NUMBER", "NEAR", "NOT", "NUMBER", "OPERATOR", 
		"OR", "PHRASE", "PHRASE_ANYTHING", "PLUS", "QANYTHING", "QCOORDINATE", 
		"QDATE", "QDELIMITER", "QFUNC", "QIDENTIFIER", "QMARK", "QNORMAL", "QPHRASE", 
		"QPHRASETRUNC", "QPOSITION", "QRANGEEX", "QRANGEIN", "QREGEX", "QTRUNCATED", 
		"RBRACK", "REGEX", "RPAREN", "SEMICOLON", "STAR", "S_NUMBER", "TERM_CHAR", 
		"TERM_NORMAL", "TERM_START_CHAR", "TERM_TRUNCATED", "TILDE", "TMODIFIER", 
		"TO", "WS", "XMETA", "'#'", "'<=>'", "'='", "'arXiv:'", "'arxiv:'", "'doi:'"
	};
	public static final int EOF=-1;
	public static final int T__70=70;
	public static final int T__71=71;
	public static final int T__72=72;
	public static final int T__73=73;
	public static final int T__74=74;
	public static final int T__75=75;
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
	public static final int INT=25;
	public static final int LBRACK=26;
	public static final int LOCAL_PARAMS=27;
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
	public static final int QCOORDINATE=41;
	public static final int QDATE=42;
	public static final int QDELIMITER=43;
	public static final int QFUNC=44;
	public static final int QIDENTIFIER=45;
	public static final int QMARK=46;
	public static final int QNORMAL=47;
	public static final int QPHRASE=48;
	public static final int QPHRASETRUNC=49;
	public static final int QPOSITION=50;
	public static final int QRANGEEX=51;
	public static final int QRANGEIN=52;
	public static final int QREGEX=53;
	public static final int QTRUNCATED=54;
	public static final int RBRACK=55;
	public static final int REGEX=56;
	public static final int RPAREN=57;
	public static final int SEMICOLON=58;
	public static final int STAR=59;
	public static final int S_NUMBER=60;
	public static final int TERM_CHAR=61;
	public static final int TERM_NORMAL=62;
	public static final int TERM_START_CHAR=63;
	public static final int TERM_TRUNCATED=64;
	public static final int TILDE=65;
	public static final int TMODIFIER=66;
	public static final int TO=67;
	public static final int WS=68;
	public static final int XMETA=69;

	// delegates
	public UnforgivingParser[] getDelegates() {
		return new UnforgivingParser[] {};
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
	@Override public String[] getTokenNames() { return ADSParser.tokenNames; }
	@Override public String getGrammarFileName() { return "/dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g"; }


	public static class mainQ_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "mainQ"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:44:1: mainQ : ( clauseOr )+ EOF -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
	public final ADSParser.mainQ_return mainQ() throws RecognitionException {
		ADSParser.mainQ_return retval = new ADSParser.mainQ_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EOF2=null;
		ParserRuleReturnScope clauseOr1 =null;

		Object EOF2_tree=null;
		RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
		RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:44:7: ( ( clauseOr )+ EOF -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:45:3: ( clauseOr )+ EOF
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:45:3: ( clauseOr )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==AUTHOR_SEARCH||(LA1_0 >= COMMA && LA1_0 <= DATE_RANGE)||LA1_0==D_NUMBER||LA1_0==FUNC_NAME||(LA1_0 >= HOUR && LA1_0 <= H_NUMBER)||(LA1_0 >= LBRACK && LA1_0 <= MINUS)||LA1_0==NUMBER||(LA1_0 >= PHRASE && LA1_0 <= PLUS)||LA1_0==QMARK||LA1_0==REGEX||(LA1_0 >= SEMICOLON && LA1_0 <= STAR)||LA1_0==TERM_NORMAL||LA1_0==TERM_TRUNCATED||LA1_0==TO||(LA1_0 >= 70 && LA1_0 <= 75)) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:45:3: clauseOr
					{
					pushFollow(FOLLOW_clauseOr_in_mainQ188);
					clauseOr1=clauseOr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr1.getTree());
					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_mainQ191); if (state.failed) return retval; 
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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 45:17: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
			{
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:45:20: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "DEFOP"), root_1);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "clauseOr"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:49:1: clauseOr : (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* ;
	public final ADSParser.clauseOr_return clauseOr() throws RecognitionException {
		ADSParser.clauseOr_return retval = new ADSParser.clauseOr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope or3 =null;

		RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
		RewriteRuleSubtreeStream stream_clauseAnd=new RewriteRuleSubtreeStream(adaptor,"rule clauseAnd");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:50:3: ( (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:50:5: (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:50:5: (first= clauseAnd -> $first)
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:50:6: first= clauseAnd
			{
			pushFollow(FOLLOW_clauseAnd_in_clauseOr224);
			first=clauseAnd();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_clauseAnd.add(first.getTree());
			// AST REWRITE
			// elements: first
			// token labels: 
			// rule labels: first, retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.getTree():null);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 50:22: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:50:33: ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==OR) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:50:34: or others= clauseAnd
					{
					pushFollow(FOLLOW_or_in_clauseOr233);
					or3=or();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_or.add(or3.getTree());
					pushFollow(FOLLOW_clauseAnd_in_clauseOr237);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 50:54: -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:50:57: ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "OR"), root_1);
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
	// $ANTLR end "clauseOr"


	public static class clauseAnd_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "clauseAnd"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:53:1: clauseAnd : (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* ;
	public final ADSParser.clauseAnd_return clauseAnd() throws RecognitionException {
		ADSParser.clauseAnd_return retval = new ADSParser.clauseAnd_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope and4 =null;

		RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");
		RewriteRuleSubtreeStream stream_clauseNot=new RewriteRuleSubtreeStream(adaptor,"rule clauseNot");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:54:3: ( (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:54:5: (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:54:5: (first= clauseNot -> $first)
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:54:6: first= clauseNot
			{
			pushFollow(FOLLOW_clauseNot_in_clauseAnd266);
			first=clauseNot();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_clauseNot.add(first.getTree());
			// AST REWRITE
			// elements: first
			// token labels: 
			// rule labels: first, retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.getTree():null);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 54:23: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:54:34: ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==AND) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:54:35: and others= clauseNot
					{
					pushFollow(FOLLOW_and_in_clauseAnd276);
					and4=and();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_and.add(and4.getTree());
					pushFollow(FOLLOW_clauseNot_in_clauseAnd280);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 54:56: -> ^( OPERATOR[\"AND\"] ( clauseNot )+ )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:54:59: ^( OPERATOR[\"AND\"] ( clauseNot )+ )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "AND"), root_1);
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
	// $ANTLR end "clauseAnd"


	public static class clauseNot_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "clauseNot"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:57:1: clauseNot : (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* ;
	public final ADSParser.clauseNot_return clauseNot() throws RecognitionException {
		ADSParser.clauseNot_return retval = new ADSParser.clauseNot_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope not5 =null;

		RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
		RewriteRuleSubtreeStream stream_clauseNear=new RewriteRuleSubtreeStream(adaptor,"rule clauseNear");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:58:3: ( (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )* )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:58:5: (first= clauseNear -> $first) ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:58:5: (first= clauseNear -> $first)
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:58:6: first= clauseNear
			{
			pushFollow(FOLLOW_clauseNear_in_clauseNot309);
			first=clauseNear();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_clauseNear.add(first.getTree());
			// AST REWRITE
			// elements: first
			// token labels: 
			// rule labels: first, retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.getTree():null);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 58:23: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:58:34: ( not others= clauseNear -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ ) )*
			loop4:
			while (true) {
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:58:35: not others= clauseNear
					{
					pushFollow(FOLLOW_not_in_clauseNot318);
					not5=not();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_not.add(not5.getTree());
					pushFollow(FOLLOW_clauseNear_in_clauseNot322);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 58:57: -> ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:58:60: ^( OPERATOR[\"NOT\"] ( clauseNear )+ )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "NOT"), root_1);
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
	// $ANTLR end "clauseNot"


	public static class clauseNear_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "clauseNear"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:61:1: clauseNear : (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* ;
	public final ADSParser.clauseNear_return clauseNear() throws RecognitionException {
		ADSParser.clauseNear_return retval = new ADSParser.clauseNear_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope near6 =null;

		RewriteRuleSubtreeStream stream_clauseBasic=new RewriteRuleSubtreeStream(adaptor,"rule clauseBasic");
		RewriteRuleSubtreeStream stream_near=new RewriteRuleSubtreeStream(adaptor,"rule near");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:62:3: ( (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )* )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:62:5: (first= clauseBasic -> $first) ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:62:5: (first= clauseBasic -> $first)
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:62:6: first= clauseBasic
			{
			pushFollow(FOLLOW_clauseBasic_in_clauseNear353);
			first=clauseBasic();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_clauseBasic.add(first.getTree());
			// AST REWRITE
			// elements: first
			// token labels: 
			// rule labels: first, retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.getTree():null);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 62:24: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:62:35: ( near others= clauseBasic -> ^( near ( clauseBasic )+ ) )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==NEAR) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:62:36: near others= clauseBasic
					{
					pushFollow(FOLLOW_near_in_clauseNear362);
					near6=near();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_near.add(near6.getTree());
					pushFollow(FOLLOW_clauseBasic_in_clauseNear366);
					others=clauseBasic();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_clauseBasic.add(others.getTree());
					// AST REWRITE
					// elements: near, clauseBasic
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 62:60: -> ^( near ( clauseBasic )+ )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:62:63: ^( near ( clauseBasic )+ )
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
	// $ANTLR end "clauseNear"


	public static class clauseBasic_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "clauseBasic"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:65:1: clauseBasic : ( ( ( lmodifier )? func_name )=> ( lmodifier )? func_name ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) RPAREN ) ) ) ) | ( lmodifier LPAREN ( clauseOr )+ RPAREN )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN rmodifier )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) | atom );
	public final ADSParser.clauseBasic_return clauseBasic() throws RecognitionException {
		ADSParser.clauseBasic_return retval = new ADSParser.clauseBasic_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token RPAREN10=null;
		Token LPAREN13=null;
		Token RPAREN15=null;
		Token LPAREN18=null;
		Token RPAREN20=null;
		Token LPAREN22=null;
		Token RPAREN24=null;
		ParserRuleReturnScope lmodifier7 =null;
		ParserRuleReturnScope func_name8 =null;
		ParserRuleReturnScope clauseOr9 =null;
		ParserRuleReturnScope rmodifier11 =null;
		ParserRuleReturnScope lmodifier12 =null;
		ParserRuleReturnScope clauseOr14 =null;
		ParserRuleReturnScope rmodifier16 =null;
		ParserRuleReturnScope lmodifier17 =null;
		ParserRuleReturnScope clauseOr19 =null;
		ParserRuleReturnScope rmodifier21 =null;
		ParserRuleReturnScope clauseOr23 =null;
		ParserRuleReturnScope atom25 =null;

		Object RPAREN10_tree=null;
		Object LPAREN13_tree=null;
		Object RPAREN15_tree=null;
		Object LPAREN18_tree=null;
		Object RPAREN20_tree=null;
		Object LPAREN22_tree=null;
		Object RPAREN24_tree=null;
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_rmodifier=new RewriteRuleSubtreeStream(adaptor,"rule rmodifier");
		RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
		RewriteRuleSubtreeStream stream_lmodifier=new RewriteRuleSubtreeStream(adaptor,"rule lmodifier");
		RewriteRuleSubtreeStream stream_func_name=new RewriteRuleSubtreeStream(adaptor,"rule func_name");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:66:3: ( ( ( lmodifier )? func_name )=> ( lmodifier )? func_name ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) RPAREN ) ) ) ) | ( lmodifier LPAREN ( clauseOr )+ RPAREN )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN rmodifier )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )? -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) | atom )
			int alt16=5;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==PLUS) ) {
				int LA16_1 = input.LA(2);
				if ( (synpred1_ADS()) ) {
					alt16=1;
				}
				else if ( (synpred2_ADS()) ) {
					alt16=2;
				}
				else if ( (synpred3_ADS()) ) {
					alt16=3;
				}
				else if ( (true) ) {
					alt16=5;
				}

			}
			else if ( (LA16_0==MINUS) ) {
				int LA16_2 = input.LA(2);
				if ( (synpred1_ADS()) ) {
					alt16=1;
				}
				else if ( (synpred2_ADS()) ) {
					alt16=2;
				}
				else if ( (synpred3_ADS()) ) {
					alt16=3;
				}
				else if ( (true) ) {
					alt16=5;
				}

			}
			else if ( (LA16_0==72) ) {
				int LA16_3 = input.LA(2);
				if ( (synpred1_ADS()) ) {
					alt16=1;
				}
				else if ( (synpred2_ADS()) ) {
					alt16=2;
				}
				else if ( (synpred3_ADS()) ) {
					alt16=3;
				}
				else if ( (true) ) {
					alt16=5;
				}

			}
			else if ( (LA16_0==70) ) {
				int LA16_4 = input.LA(2);
				if ( (synpred1_ADS()) ) {
					alt16=1;
				}
				else if ( (synpred2_ADS()) ) {
					alt16=2;
				}
				else if ( (synpred3_ADS()) ) {
					alt16=3;
				}
				else if ( (true) ) {
					alt16=5;
				}

			}
			else if ( (LA16_0==FUNC_NAME) && (synpred1_ADS())) {
				alt16=1;
			}
			else if ( (LA16_0==LPAREN) ) {
				int LA16_6 = input.LA(2);
				if ( (synpred2_ADS()) ) {
					alt16=2;
				}
				else if ( (synpred3_ADS()) ) {
					alt16=3;
				}
				else if ( (synpred4_ADS()) ) {
					alt16=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 16, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA16_0==AUTHOR_SEARCH||(LA16_0 >= COMMA && LA16_0 <= DATE_RANGE)||LA16_0==D_NUMBER||(LA16_0 >= HOUR && LA16_0 <= H_NUMBER)||(LA16_0 >= LBRACK && LA16_0 <= LOCAL_PARAMS)||LA16_0==NUMBER||(LA16_0 >= PHRASE && LA16_0 <= PHRASE_ANYTHING)||LA16_0==QMARK||LA16_0==REGEX||(LA16_0 >= SEMICOLON && LA16_0 <= STAR)||LA16_0==TERM_NORMAL||LA16_0==TERM_TRUNCATED||LA16_0==TO||LA16_0==71||(LA16_0 >= 73 && LA16_0 <= 75)) ) {
				alt16=5;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 16, 0, input);
				throw nvae;
			}

			switch (alt16) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:4: ( ( lmodifier )? func_name )=> ( lmodifier )? func_name ( clauseOr )+ RPAREN ( rmodifier )?
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:30: ( lmodifier )?
					int alt6=2;
					int LA6_0 = input.LA(1);
					if ( (LA6_0==MINUS||LA6_0==PLUS||LA6_0==70||LA6_0==72) ) {
						alt6=1;
					}
					switch (alt6) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:30: lmodifier
							{
							pushFollow(FOLLOW_lmodifier_in_clauseBasic407);
							lmodifier7=lmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier7.getTree());
							}
							break;

					}

					pushFollow(FOLLOW_func_name_in_clauseBasic410);
					func_name8=func_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_func_name.add(func_name8.getTree());
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:51: ( clauseOr )+
					int cnt7=0;
					loop7:
					while (true) {
						int alt7=2;
						int LA7_0 = input.LA(1);
						if ( (LA7_0==AUTHOR_SEARCH||(LA7_0 >= COMMA && LA7_0 <= DATE_RANGE)||LA7_0==D_NUMBER||LA7_0==FUNC_NAME||(LA7_0 >= HOUR && LA7_0 <= H_NUMBER)||(LA7_0 >= LBRACK && LA7_0 <= MINUS)||LA7_0==NUMBER||(LA7_0 >= PHRASE && LA7_0 <= PLUS)||LA7_0==QMARK||LA7_0==REGEX||(LA7_0 >= SEMICOLON && LA7_0 <= STAR)||LA7_0==TERM_NORMAL||LA7_0==TERM_TRUNCATED||LA7_0==TO||(LA7_0 >= 70 && LA7_0 <= 75)) ) {
							alt7=1;
						}

						switch (alt7) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:51: clauseOr
							{
							pushFollow(FOLLOW_clauseOr_in_clauseBasic412);
							clauseOr9=clauseOr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr9.getTree());
							}
							break;

						default :
							if ( cnt7 >= 1 ) break loop7;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(7, input);
							throw eee;
						}
						cnt7++;
					}

					RPAREN10=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic416); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN10);

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:69: ( rmodifier )?
					int alt8=2;
					int LA8_0 = input.LA(1);
					if ( (LA8_0==CARAT||LA8_0==TILDE) ) {
						alt8=1;
					}
					switch (alt8) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:69: rmodifier
							{
							pushFollow(FOLLOW_rmodifier_in_clauseBasic418);
							rmodifier11=rmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier11.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: lmodifier, RPAREN, clauseOr, func_name, rmodifier
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 68:4: -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) RPAREN ) ) ) )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:68:7: ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) RPAREN ) ) ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLAUSE, "CLAUSE"), root_1);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:68:16: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) RPAREN ) ) )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_2);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:68:27: ( lmodifier )?
						if ( stream_lmodifier.hasNext() ) {
							adaptor.addChild(root_2, stream_lmodifier.nextTree());
						}
						stream_lmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:68:38: ^( TMODIFIER ( rmodifier )? ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) RPAREN ) )
						{
						Object root_3 = (Object)adaptor.nil();
						root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(TMODIFIER, "TMODIFIER"), root_3);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:68:50: ( rmodifier )?
						if ( stream_rmodifier.hasNext() ) {
							adaptor.addChild(root_3, stream_rmodifier.nextTree());
						}
						stream_rmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:68:61: ^( QFUNC func_name ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) RPAREN )
						{
						Object root_4 = (Object)adaptor.nil();
						root_4 = (Object)adaptor.becomeRoot((Object)adaptor.create(QFUNC, "QFUNC"), root_4);
						adaptor.addChild(root_4, stream_func_name.nextTree());
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:68:79: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
						{
						Object root_5 = (Object)adaptor.nil();
						root_5 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "DEFOP"), root_5);
						if ( !(stream_clauseOr.hasNext()) ) {
							throw new RewriteEarlyExitException();
						}
						while ( stream_clauseOr.hasNext() ) {
							adaptor.addChild(root_5, stream_clauseOr.nextTree());
						}
						stream_clauseOr.reset();

						adaptor.addChild(root_4, root_5);
						}

						adaptor.addChild(root_4, stream_RPAREN.nextNode());
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:5: ( lmodifier LPAREN ( clauseOr )+ RPAREN )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )?
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:44: ( lmodifier )?
					int alt9=2;
					int LA9_0 = input.LA(1);
					if ( (LA9_0==MINUS||LA9_0==PLUS||LA9_0==70||LA9_0==72) ) {
						alt9=1;
					}
					switch (alt9) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:44: lmodifier
							{
							pushFollow(FOLLOW_lmodifier_in_clauseBasic477);
							lmodifier12=lmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier12.getTree());
							}
							break;

					}

					LPAREN13=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic480); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN13);

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:62: ( clauseOr )+
					int cnt10=0;
					loop10:
					while (true) {
						int alt10=2;
						int LA10_0 = input.LA(1);
						if ( (LA10_0==AUTHOR_SEARCH||(LA10_0 >= COMMA && LA10_0 <= DATE_RANGE)||LA10_0==D_NUMBER||LA10_0==FUNC_NAME||(LA10_0 >= HOUR && LA10_0 <= H_NUMBER)||(LA10_0 >= LBRACK && LA10_0 <= MINUS)||LA10_0==NUMBER||(LA10_0 >= PHRASE && LA10_0 <= PLUS)||LA10_0==QMARK||LA10_0==REGEX||(LA10_0 >= SEMICOLON && LA10_0 <= STAR)||LA10_0==TERM_NORMAL||LA10_0==TERM_TRUNCATED||LA10_0==TO||(LA10_0 >= 70 && LA10_0 <= 75)) ) {
							alt10=1;
						}

						switch (alt10) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:62: clauseOr
							{
							pushFollow(FOLLOW_clauseOr_in_clauseBasic482);
							clauseOr14=clauseOr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr14.getTree());
							}
							break;

						default :
							if ( cnt10 >= 1 ) break loop10;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(10, input);
							throw eee;
						}
						cnt10++;
					}

					RPAREN15=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic485); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN15);

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:79: ( rmodifier )?
					int alt11=2;
					int LA11_0 = input.LA(1);
					if ( (LA11_0==CARAT||LA11_0==TILDE) ) {
						alt11=1;
					}
					switch (alt11) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:79: rmodifier
							{
							pushFollow(FOLLOW_rmodifier_in_clauseBasic487);
							rmodifier16=rmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier16.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: lmodifier, rmodifier, clauseOr
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 70:4: -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:70:7: ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLAUSE, "CLAUSE"), root_1);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:70:16: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_2);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:70:27: ( lmodifier )?
						if ( stream_lmodifier.hasNext() ) {
							adaptor.addChild(root_2, stream_lmodifier.nextTree());
						}
						stream_lmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:70:38: ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
						{
						Object root_3 = (Object)adaptor.nil();
						root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(TMODIFIER, "TMODIFIER"), root_3);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:70:50: ( rmodifier )?
						if ( stream_rmodifier.hasNext() ) {
							adaptor.addChild(root_3, stream_rmodifier.nextTree());
						}
						stream_rmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:70:61: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
						{
						Object root_4 = (Object)adaptor.nil();
						root_4 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "DEFOP"), root_4);
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:5: ( LPAREN ( clauseOr )+ RPAREN rmodifier )=> ( lmodifier )? LPAREN ( clauseOr )+ RPAREN ( rmodifier )?
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:43: ( lmodifier )?
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0==MINUS||LA12_0==PLUS||LA12_0==70||LA12_0==72) ) {
						alt12=1;
					}
					switch (alt12) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:43: lmodifier
							{
							pushFollow(FOLLOW_lmodifier_in_clauseBasic539);
							lmodifier17=lmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier17.getTree());
							}
							break;

					}

					LPAREN18=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic542); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN18);

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:61: ( clauseOr )+
					int cnt13=0;
					loop13:
					while (true) {
						int alt13=2;
						int LA13_0 = input.LA(1);
						if ( (LA13_0==AUTHOR_SEARCH||(LA13_0 >= COMMA && LA13_0 <= DATE_RANGE)||LA13_0==D_NUMBER||LA13_0==FUNC_NAME||(LA13_0 >= HOUR && LA13_0 <= H_NUMBER)||(LA13_0 >= LBRACK && LA13_0 <= MINUS)||LA13_0==NUMBER||(LA13_0 >= PHRASE && LA13_0 <= PLUS)||LA13_0==QMARK||LA13_0==REGEX||(LA13_0 >= SEMICOLON && LA13_0 <= STAR)||LA13_0==TERM_NORMAL||LA13_0==TERM_TRUNCATED||LA13_0==TO||(LA13_0 >= 70 && LA13_0 <= 75)) ) {
							alt13=1;
						}

						switch (alt13) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:61: clauseOr
							{
							pushFollow(FOLLOW_clauseOr_in_clauseBasic544);
							clauseOr19=clauseOr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr19.getTree());
							}
							break;

						default :
							if ( cnt13 >= 1 ) break loop13;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(13, input);
							throw eee;
						}
						cnt13++;
					}

					RPAREN20=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic547); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN20);

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:78: ( rmodifier )?
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0==CARAT||LA14_0==TILDE) ) {
						alt14=1;
					}
					switch (alt14) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:78: rmodifier
							{
							pushFollow(FOLLOW_rmodifier_in_clauseBasic549);
							rmodifier21=rmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier21.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: lmodifier, rmodifier, clauseOr
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 72:4: -> ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:72:7: ^( CLAUSE ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLAUSE, "CLAUSE"), root_1);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:72:16: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_2);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:72:27: ( lmodifier )?
						if ( stream_lmodifier.hasNext() ) {
							adaptor.addChild(root_2, stream_lmodifier.nextTree());
						}
						stream_lmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:72:38: ^( TMODIFIER ( rmodifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
						{
						Object root_3 = (Object)adaptor.nil();
						root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(TMODIFIER, "TMODIFIER"), root_3);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:72:50: ( rmodifier )?
						if ( stream_rmodifier.hasNext() ) {
							adaptor.addChild(root_3, stream_rmodifier.nextTree());
						}
						stream_rmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:72:61: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
						{
						Object root_4 = (Object)adaptor.nil();
						root_4 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "DEFOP"), root_4);
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:73:5: ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN
					{
					LPAREN22=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic595); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN22);

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:73:24: ( clauseOr )+
					int cnt15=0;
					loop15:
					while (true) {
						int alt15=2;
						int LA15_0 = input.LA(1);
						if ( (LA15_0==AUTHOR_SEARCH||(LA15_0 >= COMMA && LA15_0 <= DATE_RANGE)||LA15_0==D_NUMBER||LA15_0==FUNC_NAME||(LA15_0 >= HOUR && LA15_0 <= H_NUMBER)||(LA15_0 >= LBRACK && LA15_0 <= MINUS)||LA15_0==NUMBER||(LA15_0 >= PHRASE && LA15_0 <= PLUS)||LA15_0==QMARK||LA15_0==REGEX||(LA15_0 >= SEMICOLON && LA15_0 <= STAR)||LA15_0==TERM_NORMAL||LA15_0==TERM_TRUNCATED||LA15_0==TO||(LA15_0 >= 70 && LA15_0 <= 75)) ) {
							alt15=1;
						}

						switch (alt15) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:73:24: clauseOr
							{
							pushFollow(FOLLOW_clauseOr_in_clauseBasic597);
							clauseOr23=clauseOr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr23.getTree());
							}
							break;

						default :
							if ( cnt15 >= 1 ) break loop15;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(15, input);
							throw eee;
						}
						cnt15++;
					}

					RPAREN24=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic600); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN24);

					// AST REWRITE
					// elements: clauseOr
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 74:5: -> ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:74:8: ^( CLAUSE ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLAUSE, "CLAUSE"), root_1);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:74:17: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "DEFOP"), root_2);
						if ( !(stream_clauseOr.hasNext()) ) {
							throw new RewriteEarlyExitException();
						}
						while ( stream_clauseOr.hasNext() ) {
							adaptor.addChild(root_2, stream_clauseOr.nextTree());
						}
						stream_clauseOr.reset();

						adaptor.addChild(root_1, root_2);
						}

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:75:5: atom
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_atom_in_clauseBasic624);
					atom25=atom();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, atom25.getTree());

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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "atom"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:79:1: atom : ( ( lmodifier )? field multi_value ( rmodifier )? -> ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) ) ) | ( lmodifier )? ( field )? value ( rmodifier )? -> ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) ) ) );
	public final ADSParser.atom_return atom() throws RecognitionException {
		ADSParser.atom_return retval = new ADSParser.atom_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope lmodifier26 =null;
		ParserRuleReturnScope field27 =null;
		ParserRuleReturnScope multi_value28 =null;
		ParserRuleReturnScope rmodifier29 =null;
		ParserRuleReturnScope lmodifier30 =null;
		ParserRuleReturnScope field31 =null;
		ParserRuleReturnScope value32 =null;
		ParserRuleReturnScope rmodifier33 =null;

		RewriteRuleSubtreeStream stream_rmodifier=new RewriteRuleSubtreeStream(adaptor,"rule rmodifier");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_multi_value=new RewriteRuleSubtreeStream(adaptor,"rule multi_value");
		RewriteRuleSubtreeStream stream_lmodifier=new RewriteRuleSubtreeStream(adaptor,"rule lmodifier");
		RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:80:3: ( ( lmodifier )? field multi_value ( rmodifier )? -> ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) ) ) | ( lmodifier )? ( field )? value ( rmodifier )? -> ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) ) ) )
			int alt22=2;
			switch ( input.LA(1) ) {
			case PLUS:
				{
				int LA22_1 = input.LA(2);
				if ( (LA22_1==TERM_NORMAL) ) {
					int LA22_5 = input.LA(3);
					if ( (LA22_5==COLON) ) {
						int LA22_7 = input.LA(4);
						if ( (LA22_7==LPAREN) ) {
							alt22=1;
						}
						else if ( (LA22_7==AUTHOR_SEARCH||(LA22_7 >= COMMA && LA22_7 <= DATE_RANGE)||LA22_7==D_NUMBER||(LA22_7 >= HOUR && LA22_7 <= H_NUMBER)||(LA22_7 >= LBRACK && LA22_7 <= LOCAL_PARAMS)||LA22_7==NUMBER||(LA22_7 >= PHRASE && LA22_7 <= PHRASE_ANYTHING)||LA22_7==QMARK||LA22_7==REGEX||(LA22_7 >= SEMICOLON && LA22_7 <= STAR)||LA22_7==TERM_NORMAL||LA22_7==TERM_TRUNCATED||LA22_7==TO||LA22_7==71||(LA22_7 >= 73 && LA22_7 <= 75)) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( (LA22_5==EOF||LA22_5==AND||LA22_5==AUTHOR_SEARCH||LA22_5==CARAT||(LA22_5 >= COMMA && LA22_5 <= DATE_RANGE)||LA22_5==D_NUMBER||LA22_5==FUNC_NAME||(LA22_5 >= HOUR && LA22_5 <= H_NUMBER)||(LA22_5 >= LBRACK && LA22_5 <= MINUS)||(LA22_5 >= NEAR && LA22_5 <= NUMBER)||(LA22_5 >= OR && LA22_5 <= PLUS)||LA22_5==QMARK||(LA22_5 >= REGEX && LA22_5 <= STAR)||LA22_5==TERM_NORMAL||(LA22_5 >= TERM_TRUNCATED && LA22_5 <= TILDE)||LA22_5==TO||(LA22_5 >= 70 && LA22_5 <= 75)) ) {
						alt22=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 22, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA22_1==AUTHOR_SEARCH||(LA22_1 >= COMMA && LA22_1 <= DATE_RANGE)||LA22_1==D_NUMBER||(LA22_1 >= HOUR && LA22_1 <= H_NUMBER)||(LA22_1 >= LBRACK && LA22_1 <= LOCAL_PARAMS)||LA22_1==NUMBER||(LA22_1 >= PHRASE && LA22_1 <= PHRASE_ANYTHING)||LA22_1==QMARK||LA22_1==REGEX||(LA22_1 >= SEMICOLON && LA22_1 <= STAR)||LA22_1==TERM_TRUNCATED||LA22_1==TO||LA22_1==71||(LA22_1 >= 73 && LA22_1 <= 75)) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case MINUS:
				{
				int LA22_2 = input.LA(2);
				if ( (LA22_2==TERM_NORMAL) ) {
					int LA22_5 = input.LA(3);
					if ( (LA22_5==COLON) ) {
						int LA22_7 = input.LA(4);
						if ( (LA22_7==LPAREN) ) {
							alt22=1;
						}
						else if ( (LA22_7==AUTHOR_SEARCH||(LA22_7 >= COMMA && LA22_7 <= DATE_RANGE)||LA22_7==D_NUMBER||(LA22_7 >= HOUR && LA22_7 <= H_NUMBER)||(LA22_7 >= LBRACK && LA22_7 <= LOCAL_PARAMS)||LA22_7==NUMBER||(LA22_7 >= PHRASE && LA22_7 <= PHRASE_ANYTHING)||LA22_7==QMARK||LA22_7==REGEX||(LA22_7 >= SEMICOLON && LA22_7 <= STAR)||LA22_7==TERM_NORMAL||LA22_7==TERM_TRUNCATED||LA22_7==TO||LA22_7==71||(LA22_7 >= 73 && LA22_7 <= 75)) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( (LA22_5==EOF||LA22_5==AND||LA22_5==AUTHOR_SEARCH||LA22_5==CARAT||(LA22_5 >= COMMA && LA22_5 <= DATE_RANGE)||LA22_5==D_NUMBER||LA22_5==FUNC_NAME||(LA22_5 >= HOUR && LA22_5 <= H_NUMBER)||(LA22_5 >= LBRACK && LA22_5 <= MINUS)||(LA22_5 >= NEAR && LA22_5 <= NUMBER)||(LA22_5 >= OR && LA22_5 <= PLUS)||LA22_5==QMARK||(LA22_5 >= REGEX && LA22_5 <= STAR)||LA22_5==TERM_NORMAL||(LA22_5 >= TERM_TRUNCATED && LA22_5 <= TILDE)||LA22_5==TO||(LA22_5 >= 70 && LA22_5 <= 75)) ) {
						alt22=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 22, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA22_2==AUTHOR_SEARCH||(LA22_2 >= COMMA && LA22_2 <= DATE_RANGE)||LA22_2==D_NUMBER||(LA22_2 >= HOUR && LA22_2 <= H_NUMBER)||(LA22_2 >= LBRACK && LA22_2 <= LOCAL_PARAMS)||LA22_2==NUMBER||(LA22_2 >= PHRASE && LA22_2 <= PHRASE_ANYTHING)||LA22_2==QMARK||LA22_2==REGEX||(LA22_2 >= SEMICOLON && LA22_2 <= STAR)||LA22_2==TERM_TRUNCATED||LA22_2==TO||LA22_2==71||(LA22_2 >= 73 && LA22_2 <= 75)) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 72:
				{
				int LA22_3 = input.LA(2);
				if ( (LA22_3==TERM_NORMAL) ) {
					int LA22_5 = input.LA(3);
					if ( (LA22_5==COLON) ) {
						int LA22_7 = input.LA(4);
						if ( (LA22_7==LPAREN) ) {
							alt22=1;
						}
						else if ( (LA22_7==AUTHOR_SEARCH||(LA22_7 >= COMMA && LA22_7 <= DATE_RANGE)||LA22_7==D_NUMBER||(LA22_7 >= HOUR && LA22_7 <= H_NUMBER)||(LA22_7 >= LBRACK && LA22_7 <= LOCAL_PARAMS)||LA22_7==NUMBER||(LA22_7 >= PHRASE && LA22_7 <= PHRASE_ANYTHING)||LA22_7==QMARK||LA22_7==REGEX||(LA22_7 >= SEMICOLON && LA22_7 <= STAR)||LA22_7==TERM_NORMAL||LA22_7==TERM_TRUNCATED||LA22_7==TO||LA22_7==71||(LA22_7 >= 73 && LA22_7 <= 75)) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( (LA22_5==EOF||LA22_5==AND||LA22_5==AUTHOR_SEARCH||LA22_5==CARAT||(LA22_5 >= COMMA && LA22_5 <= DATE_RANGE)||LA22_5==D_NUMBER||LA22_5==FUNC_NAME||(LA22_5 >= HOUR && LA22_5 <= H_NUMBER)||(LA22_5 >= LBRACK && LA22_5 <= MINUS)||(LA22_5 >= NEAR && LA22_5 <= NUMBER)||(LA22_5 >= OR && LA22_5 <= PLUS)||LA22_5==QMARK||(LA22_5 >= REGEX && LA22_5 <= STAR)||LA22_5==TERM_NORMAL||(LA22_5 >= TERM_TRUNCATED && LA22_5 <= TILDE)||LA22_5==TO||(LA22_5 >= 70 && LA22_5 <= 75)) ) {
						alt22=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 22, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA22_3==AUTHOR_SEARCH||(LA22_3 >= COMMA && LA22_3 <= DATE_RANGE)||LA22_3==D_NUMBER||(LA22_3 >= HOUR && LA22_3 <= H_NUMBER)||(LA22_3 >= LBRACK && LA22_3 <= LOCAL_PARAMS)||LA22_3==NUMBER||(LA22_3 >= PHRASE && LA22_3 <= PHRASE_ANYTHING)||LA22_3==QMARK||LA22_3==REGEX||(LA22_3 >= SEMICOLON && LA22_3 <= STAR)||LA22_3==TERM_TRUNCATED||LA22_3==TO||LA22_3==71||(LA22_3 >= 73 && LA22_3 <= 75)) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 70:
				{
				int LA22_4 = input.LA(2);
				if ( (LA22_4==TERM_NORMAL) ) {
					int LA22_5 = input.LA(3);
					if ( (LA22_5==COLON) ) {
						int LA22_7 = input.LA(4);
						if ( (LA22_7==LPAREN) ) {
							alt22=1;
						}
						else if ( (LA22_7==AUTHOR_SEARCH||(LA22_7 >= COMMA && LA22_7 <= DATE_RANGE)||LA22_7==D_NUMBER||(LA22_7 >= HOUR && LA22_7 <= H_NUMBER)||(LA22_7 >= LBRACK && LA22_7 <= LOCAL_PARAMS)||LA22_7==NUMBER||(LA22_7 >= PHRASE && LA22_7 <= PHRASE_ANYTHING)||LA22_7==QMARK||LA22_7==REGEX||(LA22_7 >= SEMICOLON && LA22_7 <= STAR)||LA22_7==TERM_NORMAL||LA22_7==TERM_TRUNCATED||LA22_7==TO||LA22_7==71||(LA22_7 >= 73 && LA22_7 <= 75)) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( (LA22_5==EOF||LA22_5==AND||LA22_5==AUTHOR_SEARCH||LA22_5==CARAT||(LA22_5 >= COMMA && LA22_5 <= DATE_RANGE)||LA22_5==D_NUMBER||LA22_5==FUNC_NAME||(LA22_5 >= HOUR && LA22_5 <= H_NUMBER)||(LA22_5 >= LBRACK && LA22_5 <= MINUS)||(LA22_5 >= NEAR && LA22_5 <= NUMBER)||(LA22_5 >= OR && LA22_5 <= PLUS)||LA22_5==QMARK||(LA22_5 >= REGEX && LA22_5 <= STAR)||LA22_5==TERM_NORMAL||(LA22_5 >= TERM_TRUNCATED && LA22_5 <= TILDE)||LA22_5==TO||(LA22_5 >= 70 && LA22_5 <= 75)) ) {
						alt22=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 22, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA22_4==AUTHOR_SEARCH||(LA22_4 >= COMMA && LA22_4 <= DATE_RANGE)||LA22_4==D_NUMBER||(LA22_4 >= HOUR && LA22_4 <= H_NUMBER)||(LA22_4 >= LBRACK && LA22_4 <= LOCAL_PARAMS)||LA22_4==NUMBER||(LA22_4 >= PHRASE && LA22_4 <= PHRASE_ANYTHING)||LA22_4==QMARK||LA22_4==REGEX||(LA22_4 >= SEMICOLON && LA22_4 <= STAR)||LA22_4==TERM_TRUNCATED||LA22_4==TO||LA22_4==71||(LA22_4 >= 73 && LA22_4 <= 75)) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case TERM_NORMAL:
				{
				int LA22_5 = input.LA(2);
				if ( (LA22_5==COLON) ) {
					int LA22_7 = input.LA(3);
					if ( (LA22_7==LPAREN) ) {
						alt22=1;
					}
					else if ( (LA22_7==AUTHOR_SEARCH||(LA22_7 >= COMMA && LA22_7 <= DATE_RANGE)||LA22_7==D_NUMBER||(LA22_7 >= HOUR && LA22_7 <= H_NUMBER)||(LA22_7 >= LBRACK && LA22_7 <= LOCAL_PARAMS)||LA22_7==NUMBER||(LA22_7 >= PHRASE && LA22_7 <= PHRASE_ANYTHING)||LA22_7==QMARK||LA22_7==REGEX||(LA22_7 >= SEMICOLON && LA22_7 <= STAR)||LA22_7==TERM_NORMAL||LA22_7==TERM_TRUNCATED||LA22_7==TO||LA22_7==71||(LA22_7 >= 73 && LA22_7 <= 75)) ) {
						alt22=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 22, 7, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA22_5==EOF||LA22_5==AND||LA22_5==AUTHOR_SEARCH||LA22_5==CARAT||(LA22_5 >= COMMA && LA22_5 <= DATE_RANGE)||LA22_5==D_NUMBER||LA22_5==FUNC_NAME||(LA22_5 >= HOUR && LA22_5 <= H_NUMBER)||(LA22_5 >= LBRACK && LA22_5 <= MINUS)||(LA22_5 >= NEAR && LA22_5 <= NUMBER)||(LA22_5 >= OR && LA22_5 <= PLUS)||LA22_5==QMARK||(LA22_5 >= REGEX && LA22_5 <= STAR)||LA22_5==TERM_NORMAL||(LA22_5 >= TERM_TRUNCATED && LA22_5 <= TILDE)||LA22_5==TO||(LA22_5 >= 70 && LA22_5 <= 75)) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case AUTHOR_SEARCH:
			case COMMA:
			case DATE_RANGE:
			case D_NUMBER:
			case HOUR:
			case H_NUMBER:
			case LBRACK:
			case LOCAL_PARAMS:
			case NUMBER:
			case PHRASE:
			case PHRASE_ANYTHING:
			case QMARK:
			case REGEX:
			case SEMICOLON:
			case STAR:
			case TERM_TRUNCATED:
			case TO:
			case 71:
			case 73:
			case 74:
			case 75:
				{
				alt22=2;
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:81:3: ( lmodifier )? field multi_value ( rmodifier )?
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:81:3: ( lmodifier )?
					int alt17=2;
					int LA17_0 = input.LA(1);
					if ( (LA17_0==MINUS||LA17_0==PLUS||LA17_0==70||LA17_0==72) ) {
						alt17=1;
					}
					switch (alt17) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:81:3: lmodifier
							{
							pushFollow(FOLLOW_lmodifier_in_atom648);
							lmodifier26=lmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier26.getTree());
							}
							break;

					}

					pushFollow(FOLLOW_field_in_atom651);
					field27=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field27.getTree());
					pushFollow(FOLLOW_multi_value_in_atom653);
					multi_value28=multi_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_multi_value.add(multi_value28.getTree());
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:81:32: ( rmodifier )?
					int alt18=2;
					int LA18_0 = input.LA(1);
					if ( (LA18_0==CARAT||LA18_0==TILDE) ) {
						alt18=1;
					}
					switch (alt18) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:81:32: rmodifier
							{
							pushFollow(FOLLOW_rmodifier_in_atom655);
							rmodifier29=rmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier29.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: lmodifier, rmodifier, field, multi_value
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 82:4: -> ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) ) )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:82:7: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_1);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:82:18: ( lmodifier )?
						if ( stream_lmodifier.hasNext() ) {
							adaptor.addChild(root_1, stream_lmodifier.nextTree());
						}
						stream_lmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:82:29: ^( TMODIFIER ( rmodifier )? ^( FIELD field multi_value ) )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(TMODIFIER, "TMODIFIER"), root_2);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:82:41: ( rmodifier )?
						if ( stream_rmodifier.hasNext() ) {
							adaptor.addChild(root_2, stream_rmodifier.nextTree());
						}
						stream_rmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:82:52: ^( FIELD field multi_value )
						{
						Object root_3 = (Object)adaptor.nil();
						root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(FIELD, "FIELD"), root_3);
						adaptor.addChild(root_3, stream_field.nextTree());
						adaptor.addChild(root_3, stream_multi_value.nextTree());
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:83:5: ( lmodifier )? ( field )? value ( rmodifier )?
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:83:5: ( lmodifier )?
					int alt19=2;
					int LA19_0 = input.LA(1);
					if ( (LA19_0==MINUS||LA19_0==PLUS||LA19_0==70||LA19_0==72) ) {
						alt19=1;
					}
					switch (alt19) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:83:5: lmodifier
							{
							pushFollow(FOLLOW_lmodifier_in_atom689);
							lmodifier30=lmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_lmodifier.add(lmodifier30.getTree());
							}
							break;

					}

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:83:16: ( field )?
					int alt20=2;
					int LA20_0 = input.LA(1);
					if ( (LA20_0==TERM_NORMAL) ) {
						int LA20_1 = input.LA(2);
						if ( (LA20_1==COLON) ) {
							alt20=1;
						}
					}
					switch (alt20) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:83:16: field
							{
							pushFollow(FOLLOW_field_in_atom692);
							field31=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field31.getTree());
							}
							break;

					}

					pushFollow(FOLLOW_value_in_atom695);
					value32=value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_value.add(value32.getTree());
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:83:29: ( rmodifier )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( (LA21_0==CARAT||LA21_0==TILDE) ) {
						alt21=1;
					}
					switch (alt21) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:83:29: rmodifier
							{
							pushFollow(FOLLOW_rmodifier_in_atom697);
							rmodifier33=rmodifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_rmodifier.add(rmodifier33.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: lmodifier, rmodifier, field, value
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 84:3: -> ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) ) )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:84:6: ^( MODIFIER ( lmodifier )? ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_1);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:84:17: ( lmodifier )?
						if ( stream_lmodifier.hasNext() ) {
							adaptor.addChild(root_1, stream_lmodifier.nextTree());
						}
						stream_lmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:84:28: ^( TMODIFIER ( rmodifier )? ^( FIELD ( field )? value ) )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(TMODIFIER, "TMODIFIER"), root_2);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:84:40: ( rmodifier )?
						if ( stream_rmodifier.hasNext() ) {
							adaptor.addChild(root_2, stream_rmodifier.nextTree());
						}
						stream_rmodifier.reset();

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:84:51: ^( FIELD ( field )? value )
						{
						Object root_3 = (Object)adaptor.nil();
						root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(FIELD, "FIELD"), root_3);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:84:59: ( field )?
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "field"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:91:1: field : TERM_NORMAL COLON -> TERM_NORMAL ;
	public final ADSParser.field_return field() throws RecognitionException {
		ADSParser.field_return retval = new ADSParser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TERM_NORMAL34=null;
		Token COLON35=null;

		Object TERM_NORMAL34_tree=null;
		Object COLON35_tree=null;
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_TERM_NORMAL=new RewriteRuleTokenStream(adaptor,"token TERM_NORMAL");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:92:3: ( TERM_NORMAL COLON -> TERM_NORMAL )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:93:3: TERM_NORMAL COLON
			{
			TERM_NORMAL34=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_field756); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL34);

			COLON35=(Token)match(input,COLON,FOLLOW_COLON_in_field758); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_COLON.add(COLON35);

			// AST REWRITE
			// elements: TERM_NORMAL
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 93:21: -> TERM_NORMAL
			{
				adaptor.addChild(root_0, stream_TERM_NORMAL.nextNode());
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


	public static class range_term_in_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "range_term_in"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:98:1: range_term_in options {greedy=true; } : LBRACK (a= range_value -> $a ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK ;
	public final ADSParser.range_term_in_return range_term_in() throws RecognitionException {
		ADSParser.range_term_in_return retval = new ADSParser.range_term_in_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACK36=null;
		Token TO37=null;
		Token RBRACK38=null;
		ParserRuleReturnScope a =null;
		ParserRuleReturnScope b =null;

		Object LBRACK36_tree=null;
		Object TO37_tree=null;
		Object RBRACK38_tree=null;
		RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
		RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
		RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
		RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:100:3: ( LBRACK (a= range_value -> $a ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:101:8: LBRACK (a= range_value -> $a ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK
			{
			LBRACK36=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_range_term_in802); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK36);

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:102:8: (a= range_value -> $a ^( QANYTHING QANYTHING[\"*\"] ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:102:9: a= range_value
			{
			pushFollow(FOLLOW_range_value_in_range_term_in814);
			a=range_value();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range_value.add(a.getTree());
			// AST REWRITE
			// elements: a
			// token labels: 
			// rule labels: a, retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"rule a",a!=null?a.getTree():null);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 102:23: -> $a ^( QANYTHING QANYTHING[\"*\"] )
			{
				adaptor.addChild(root_0, stream_a.nextTree());
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:102:29: ^( QANYTHING QANYTHING[\"*\"] )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QANYTHING, "QANYTHING"), root_1);
				adaptor.addChild(root_1, (Object)adaptor.create(QANYTHING, "*"));
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:103:8: ( ( TO )? b= range_value -> $a ( $b)? )?
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==DATE_TOKEN||LA24_0==NUMBER||(LA24_0 >= PHRASE && LA24_0 <= PHRASE_ANYTHING)||LA24_0==STAR||LA24_0==TERM_NORMAL||LA24_0==TERM_TRUNCATED||LA24_0==TO) ) {
				alt24=1;
			}
			switch (alt24) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:103:9: ( TO )? b= range_value
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:103:9: ( TO )?
					int alt23=2;
					int LA23_0 = input.LA(1);
					if ( (LA23_0==TO) ) {
						int LA23_1 = input.LA(2);
						if ( (LA23_1==DATE_TOKEN||LA23_1==NUMBER||(LA23_1 >= PHRASE && LA23_1 <= PHRASE_ANYTHING)||LA23_1==STAR||LA23_1==TERM_NORMAL||LA23_1==TERM_TRUNCATED||LA23_1==TO) ) {
							alt23=1;
						}
					}
					switch (alt23) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:103:9: TO
							{
							TO37=(Token)match(input,TO,FOLLOW_TO_in_range_term_in837); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_TO.add(TO37);

							}
							break;

					}

					pushFollow(FOLLOW_range_value_in_range_term_in843);
					b=range_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range_value.add(b.getTree());
					// AST REWRITE
					// elements: a, b
					// token labels: 
					// rule labels: a, b, retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"rule a",a!=null?a.getTree():null);
					RewriteRuleSubtreeStream stream_b=new RewriteRuleSubtreeStream(adaptor,"rule b",b!=null?b.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 103:28: -> $a ( $b)?
					{
						adaptor.addChild(root_0, stream_a.nextTree());
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:103:35: ( $b)?
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

			RBRACK38=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_range_term_in864); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RBRACK.add(RBRACK38);

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


	public static class value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "value"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:118:1: value : ( REGEX -> ^( QREGEX REGEX ) | range_term_in -> ^( QRANGEIN range_term_in ) | identifier -> ^( QIDENTIFIER identifier ) | coordinate -> ^( QCOORDINATE coordinate ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | DATE_RANGE -> ^( QDATE DATE_RANGE ) | AUTHOR_SEARCH -> ^( QPOSITION AUTHOR_SEARCH ) | QMARK -> ^( QTRUNCATED QMARK ) | match_all -> ^( QANYTHING match_all ) | STAR -> ^( QTRUNCATED STAR ) | LOCAL_PARAMS -> ^( XMETA LOCAL_PARAMS ) | COMMA -> ^( QDELIMITER COMMA ) | SEMICOLON -> ^( QDELIMITER SEMICOLON ) );
	public final ADSParser.value_return value() throws RecognitionException {
		ADSParser.value_return retval = new ADSParser.value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token REGEX39=null;
		Token DATE_RANGE47=null;
		Token AUTHOR_SEARCH48=null;
		Token QMARK49=null;
		Token STAR51=null;
		Token LOCAL_PARAMS52=null;
		Token COMMA53=null;
		Token SEMICOLON54=null;
		ParserRuleReturnScope range_term_in40 =null;
		ParserRuleReturnScope identifier41 =null;
		ParserRuleReturnScope coordinate42 =null;
		ParserRuleReturnScope normal43 =null;
		ParserRuleReturnScope truncated44 =null;
		ParserRuleReturnScope quoted45 =null;
		ParserRuleReturnScope quoted_truncated46 =null;
		ParserRuleReturnScope match_all50 =null;

		Object REGEX39_tree=null;
		Object DATE_RANGE47_tree=null;
		Object AUTHOR_SEARCH48_tree=null;
		Object QMARK49_tree=null;
		Object STAR51_tree=null;
		Object LOCAL_PARAMS52_tree=null;
		Object COMMA53_tree=null;
		Object SEMICOLON54_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_AUTHOR_SEARCH=new RewriteRuleTokenStream(adaptor,"token AUTHOR_SEARCH");
		RewriteRuleTokenStream stream_REGEX=new RewriteRuleTokenStream(adaptor,"token REGEX");
		RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
		RewriteRuleTokenStream stream_SEMICOLON=new RewriteRuleTokenStream(adaptor,"token SEMICOLON");
		RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
		RewriteRuleTokenStream stream_LOCAL_PARAMS=new RewriteRuleTokenStream(adaptor,"token LOCAL_PARAMS");
		RewriteRuleTokenStream stream_DATE_RANGE=new RewriteRuleTokenStream(adaptor,"token DATE_RANGE");
		RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
		RewriteRuleSubtreeStream stream_identifier=new RewriteRuleSubtreeStream(adaptor,"rule identifier");
		RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
		RewriteRuleSubtreeStream stream_coordinate=new RewriteRuleSubtreeStream(adaptor,"rule coordinate");
		RewriteRuleSubtreeStream stream_range_term_in=new RewriteRuleSubtreeStream(adaptor,"rule range_term_in");
		RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
		RewriteRuleSubtreeStream stream_match_all=new RewriteRuleSubtreeStream(adaptor,"rule match_all");
		RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:119:3: ( REGEX -> ^( QREGEX REGEX ) | range_term_in -> ^( QRANGEIN range_term_in ) | identifier -> ^( QIDENTIFIER identifier ) | coordinate -> ^( QCOORDINATE coordinate ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | DATE_RANGE -> ^( QDATE DATE_RANGE ) | AUTHOR_SEARCH -> ^( QPOSITION AUTHOR_SEARCH ) | QMARK -> ^( QTRUNCATED QMARK ) | match_all -> ^( QANYTHING match_all ) | STAR -> ^( QTRUNCATED STAR ) | LOCAL_PARAMS -> ^( XMETA LOCAL_PARAMS ) | COMMA -> ^( QDELIMITER COMMA ) | SEMICOLON -> ^( QDELIMITER SEMICOLON ) )
			int alt25=16;
			switch ( input.LA(1) ) {
			case REGEX:
				{
				alt25=1;
				}
				break;
			case LBRACK:
				{
				alt25=2;
				}
				break;
			case 73:
			case 74:
			case 75:
				{
				alt25=3;
				}
				break;
			case D_NUMBER:
			case HOUR:
			case H_NUMBER:
			case 71:
				{
				alt25=4;
				}
				break;
			case NUMBER:
			case TERM_NORMAL:
			case TO:
				{
				alt25=5;
				}
				break;
			case TERM_TRUNCATED:
				{
				alt25=6;
				}
				break;
			case PHRASE:
				{
				alt25=7;
				}
				break;
			case PHRASE_ANYTHING:
				{
				alt25=8;
				}
				break;
			case DATE_RANGE:
				{
				alt25=9;
				}
				break;
			case AUTHOR_SEARCH:
				{
				alt25=10;
				}
				break;
			case QMARK:
				{
				alt25=11;
				}
				break;
			case STAR:
				{
				int LA25_12 = input.LA(2);
				if ( (LA25_12==COLON) ) {
					alt25=12;
				}
				else if ( (LA25_12==EOF||LA25_12==AND||LA25_12==AUTHOR_SEARCH||LA25_12==CARAT||(LA25_12 >= COMMA && LA25_12 <= DATE_RANGE)||LA25_12==D_NUMBER||LA25_12==FUNC_NAME||(LA25_12 >= HOUR && LA25_12 <= H_NUMBER)||(LA25_12 >= LBRACK && LA25_12 <= MINUS)||(LA25_12 >= NEAR && LA25_12 <= NUMBER)||(LA25_12 >= OR && LA25_12 <= PLUS)||LA25_12==QMARK||(LA25_12 >= REGEX && LA25_12 <= STAR)||LA25_12==TERM_NORMAL||(LA25_12 >= TERM_TRUNCATED && LA25_12 <= TILDE)||LA25_12==TO||(LA25_12 >= 70 && LA25_12 <= 75)) ) {
					alt25=13;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 25, 12, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case LOCAL_PARAMS:
				{
				alt25=14;
				}
				break;
			case COMMA:
				{
				alt25=15;
				}
				break;
			case SEMICOLON:
				{
				alt25=16;
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:120:3: REGEX
					{
					REGEX39=(Token)match(input,REGEX,FOLLOW_REGEX_in_value883); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_REGEX.add(REGEX39);

					// AST REWRITE
					// elements: REGEX
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 120:9: -> ^( QREGEX REGEX )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:120:12: ^( QREGEX REGEX )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QREGEX, "QREGEX"), root_1);
						adaptor.addChild(root_1, stream_REGEX.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:121:4: range_term_in
					{
					pushFollow(FOLLOW_range_term_in_in_value897);
					range_term_in40=range_term_in();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range_term_in.add(range_term_in40.getTree());
					// AST REWRITE
					// elements: range_term_in
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 121:18: -> ^( QRANGEIN range_term_in )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:121:21: ^( QRANGEIN range_term_in )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QRANGEIN, "QRANGEIN"), root_1);
						adaptor.addChild(root_1, stream_range_term_in.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:123:5: identifier
					{
					pushFollow(FOLLOW_identifier_in_value912);
					identifier41=identifier();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identifier.add(identifier41.getTree());
					// AST REWRITE
					// elements: identifier
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 123:16: -> ^( QIDENTIFIER identifier )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:123:19: ^( QIDENTIFIER identifier )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QIDENTIFIER, "QIDENTIFIER"), root_1);
						adaptor.addChild(root_1, stream_identifier.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:124:5: coordinate
					{
					pushFollow(FOLLOW_coordinate_in_value926);
					coordinate42=coordinate();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_coordinate.add(coordinate42.getTree());
					// AST REWRITE
					// elements: coordinate
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 124:16: -> ^( QCOORDINATE coordinate )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:124:19: ^( QCOORDINATE coordinate )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QCOORDINATE, "QCOORDINATE"), root_1);
						adaptor.addChild(root_1, stream_coordinate.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:125:5: normal
					{
					pushFollow(FOLLOW_normal_in_value940);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 125:12: -> ^( QNORMAL normal )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:125:15: ^( QNORMAL normal )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QNORMAL, "QNORMAL"), root_1);
						adaptor.addChild(root_1, stream_normal.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 6 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:126:5: truncated
					{
					pushFollow(FOLLOW_truncated_in_value955);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 126:15: -> ^( QTRUNCATED truncated )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:126:18: ^( QTRUNCATED truncated )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QTRUNCATED, "QTRUNCATED"), root_1);
						adaptor.addChild(root_1, stream_truncated.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 7 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:127:5: quoted
					{
					pushFollow(FOLLOW_quoted_in_value971);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 127:12: -> ^( QPHRASE quoted )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:127:15: ^( QPHRASE quoted )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QPHRASE, "QPHRASE"), root_1);
						adaptor.addChild(root_1, stream_quoted.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 8 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:128:5: quoted_truncated
					{
					pushFollow(FOLLOW_quoted_truncated_in_value985);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 128:22: -> ^( QPHRASETRUNC quoted_truncated )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:128:25: ^( QPHRASETRUNC quoted_truncated )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QPHRASETRUNC, "QPHRASETRUNC"), root_1);
						adaptor.addChild(root_1, stream_quoted_truncated.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 9 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:129:5: DATE_RANGE
					{
					DATE_RANGE47=(Token)match(input,DATE_RANGE,FOLLOW_DATE_RANGE_in_value999); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DATE_RANGE.add(DATE_RANGE47);

					// AST REWRITE
					// elements: DATE_RANGE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 129:16: -> ^( QDATE DATE_RANGE )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:129:19: ^( QDATE DATE_RANGE )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QDATE, "QDATE"), root_1);
						adaptor.addChild(root_1, stream_DATE_RANGE.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 10 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:130:5: AUTHOR_SEARCH
					{
					AUTHOR_SEARCH48=(Token)match(input,AUTHOR_SEARCH,FOLLOW_AUTHOR_SEARCH_in_value1013); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AUTHOR_SEARCH.add(AUTHOR_SEARCH48);

					// AST REWRITE
					// elements: AUTHOR_SEARCH
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 130:19: -> ^( QPOSITION AUTHOR_SEARCH )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:130:22: ^( QPOSITION AUTHOR_SEARCH )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QPOSITION, "QPOSITION"), root_1);
						adaptor.addChild(root_1, stream_AUTHOR_SEARCH.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 11 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:131:5: QMARK
					{
					QMARK49=(Token)match(input,QMARK,FOLLOW_QMARK_in_value1027); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_QMARK.add(QMARK49);

					// AST REWRITE
					// elements: QMARK
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 131:11: -> ^( QTRUNCATED QMARK )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:131:14: ^( QTRUNCATED QMARK )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QTRUNCATED, "QTRUNCATED"), root_1);
						adaptor.addChild(root_1, stream_QMARK.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 12 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:132:5: match_all
					{
					pushFollow(FOLLOW_match_all_in_value1041);
					match_all50=match_all();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_match_all.add(match_all50.getTree());
					// AST REWRITE
					// elements: match_all
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 132:15: -> ^( QANYTHING match_all )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:132:18: ^( QANYTHING match_all )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QANYTHING, "QANYTHING"), root_1);
						adaptor.addChild(root_1, stream_match_all.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 13 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:133:5: STAR
					{
					STAR51=(Token)match(input,STAR,FOLLOW_STAR_in_value1055); if (state.failed) return retval; 
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 133:10: -> ^( QTRUNCATED STAR )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:133:13: ^( QTRUNCATED STAR )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QTRUNCATED, "QTRUNCATED"), root_1);
						adaptor.addChild(root_1, stream_STAR.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 14 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:134:5: LOCAL_PARAMS
					{
					LOCAL_PARAMS52=(Token)match(input,LOCAL_PARAMS,FOLLOW_LOCAL_PARAMS_in_value1069); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LOCAL_PARAMS.add(LOCAL_PARAMS52);

					// AST REWRITE
					// elements: LOCAL_PARAMS
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 134:18: -> ^( XMETA LOCAL_PARAMS )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:134:21: ^( XMETA LOCAL_PARAMS )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(XMETA, "XMETA"), root_1);
						adaptor.addChild(root_1, stream_LOCAL_PARAMS.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 15 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:135:5: COMMA
					{
					COMMA53=(Token)match(input,COMMA,FOLLOW_COMMA_in_value1084); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COMMA.add(COMMA53);

					// AST REWRITE
					// elements: COMMA
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 135:11: -> ^( QDELIMITER COMMA )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:135:14: ^( QDELIMITER COMMA )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QDELIMITER, "QDELIMITER"), root_1);
						adaptor.addChild(root_1, stream_COMMA.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 16 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:136:5: SEMICOLON
					{
					SEMICOLON54=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_value1098); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_SEMICOLON.add(SEMICOLON54);

					// AST REWRITE
					// elements: SEMICOLON
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 136:15: -> ^( QDELIMITER SEMICOLON )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:136:18: ^( QDELIMITER SEMICOLON )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QDELIMITER, "QDELIMITER"), root_1);
						adaptor.addChild(root_1, stream_SEMICOLON.nextNode());
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


	public static class range_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "range_value"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:142:1: range_value : ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) );
	public final ADSParser.range_value_return range_value() throws RecognitionException {
		ADSParser.range_value_return retval = new ADSParser.range_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STAR60=null;
		ParserRuleReturnScope truncated55 =null;
		ParserRuleReturnScope quoted56 =null;
		ParserRuleReturnScope quoted_truncated57 =null;
		ParserRuleReturnScope date58 =null;
		ParserRuleReturnScope normal59 =null;

		Object STAR60_tree=null;
		RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
		RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
		RewriteRuleSubtreeStream stream_date=new RewriteRuleSubtreeStream(adaptor,"rule date");
		RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
		RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
		RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:143:3: ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) )
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
			case TO:
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:144:3: truncated
					{
					pushFollow(FOLLOW_truncated_in_range_value1129);
					truncated55=truncated();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_truncated.add(truncated55.getTree());
					// AST REWRITE
					// elements: truncated
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 144:13: -> ^( QTRUNCATED truncated )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:144:16: ^( QTRUNCATED truncated )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QTRUNCATED, "QTRUNCATED"), root_1);
						adaptor.addChild(root_1, stream_truncated.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:145:5: quoted
					{
					pushFollow(FOLLOW_quoted_in_range_value1143);
					quoted56=quoted();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_quoted.add(quoted56.getTree());
					// AST REWRITE
					// elements: quoted
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 145:12: -> ^( QPHRASE quoted )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:145:15: ^( QPHRASE quoted )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QPHRASE, "QPHRASE"), root_1);
						adaptor.addChild(root_1, stream_quoted.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:146:5: quoted_truncated
					{
					pushFollow(FOLLOW_quoted_truncated_in_range_value1157);
					quoted_truncated57=quoted_truncated();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_quoted_truncated.add(quoted_truncated57.getTree());
					// AST REWRITE
					// elements: quoted_truncated
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 146:22: -> ^( QPHRASETRUNC quoted_truncated )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:146:25: ^( QPHRASETRUNC quoted_truncated )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QPHRASETRUNC, "QPHRASETRUNC"), root_1);
						adaptor.addChild(root_1, stream_quoted_truncated.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:147:5: date
					{
					pushFollow(FOLLOW_date_in_range_value1171);
					date58=date();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_date.add(date58.getTree());
					// AST REWRITE
					// elements: date
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 147:10: -> ^( QNORMAL date )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:147:13: ^( QNORMAL date )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QNORMAL, "QNORMAL"), root_1);
						adaptor.addChild(root_1, stream_date.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:148:5: normal
					{
					pushFollow(FOLLOW_normal_in_range_value1185);
					normal59=normal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_normal.add(normal59.getTree());
					// AST REWRITE
					// elements: normal
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 148:12: -> ^( QNORMAL normal )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:148:15: ^( QNORMAL normal )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QNORMAL, "QNORMAL"), root_1);
						adaptor.addChild(root_1, stream_normal.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 6 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:149:5: STAR
					{
					STAR60=(Token)match(input,STAR,FOLLOW_STAR_in_range_value1200); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_STAR.add(STAR60);

					// AST REWRITE
					// elements: STAR
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 149:10: -> ^( QANYTHING STAR )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:149:13: ^( QANYTHING STAR )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QANYTHING, "QANYTHING"), root_1);
						adaptor.addChild(root_1, stream_STAR.nextNode());
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "func_name"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:152:1: func_name : FUNC_NAME ;
	public final ADSParser.func_name_return func_name() throws RecognitionException {
		ADSParser.func_name_return retval = new ADSParser.func_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token FUNC_NAME61=null;

		Object FUNC_NAME61_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:153:3: ( FUNC_NAME )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:154:3: FUNC_NAME
			{
			root_0 = (Object)adaptor.nil();


			FUNC_NAME61=(Token)match(input,FUNC_NAME,FOLLOW_FUNC_NAME_in_func_name1224); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			FUNC_NAME61_tree = (Object)adaptor.create(FUNC_NAME61);
			adaptor.addChild(root_0, FUNC_NAME61_tree);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multi_value"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:159:1: multi_value : LPAREN multiClause RPAREN -> ^( CLAUSE multiClause ) ;
	public final ADSParser.multi_value_return multi_value() throws RecognitionException {
		ADSParser.multi_value_return retval = new ADSParser.multi_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LPAREN62=null;
		Token RPAREN64=null;
		ParserRuleReturnScope multiClause63 =null;

		Object LPAREN62_tree=null;
		Object RPAREN64_tree=null;
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_multiClause=new RewriteRuleSubtreeStream(adaptor,"rule multiClause");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:160:3: ( LPAREN multiClause RPAREN -> ^( CLAUSE multiClause ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:161:3: LPAREN multiClause RPAREN
			{
			LPAREN62=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_multi_value1242); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN62);

			pushFollow(FOLLOW_multiClause_in_multi_value1244);
			multiClause63=multiClause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_multiClause.add(multiClause63.getTree());
			RPAREN64=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_multi_value1246); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN64);

			// AST REWRITE
			// elements: multiClause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 161:29: -> ^( CLAUSE multiClause )
			{
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:161:32: ^( CLAUSE multiClause )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLAUSE, "CLAUSE"), root_1);
				adaptor.addChild(root_1, stream_multiClause.nextTree());
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
	// $ANTLR end "multi_value"


	public static class multiClause_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multiClause"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:166:1: multiClause : ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
	public final ADSParser.multiClause_return multiClause() throws RecognitionException {
		ADSParser.multiClause_return retval = new ADSParser.multiClause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope clauseOr65 =null;

		RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:167:3: ( ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:172:3: ( clauseOr )+
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:172:3: ( clauseOr )+
			int cnt27=0;
			loop27:
			while (true) {
				int alt27=2;
				int LA27_0 = input.LA(1);
				if ( (LA27_0==AUTHOR_SEARCH||(LA27_0 >= COMMA && LA27_0 <= DATE_RANGE)||LA27_0==D_NUMBER||LA27_0==FUNC_NAME||(LA27_0 >= HOUR && LA27_0 <= H_NUMBER)||(LA27_0 >= LBRACK && LA27_0 <= MINUS)||LA27_0==NUMBER||(LA27_0 >= PHRASE && LA27_0 <= PLUS)||LA27_0==QMARK||LA27_0==REGEX||(LA27_0 >= SEMICOLON && LA27_0 <= STAR)||LA27_0==TERM_NORMAL||LA27_0==TERM_TRUNCATED||LA27_0==TO||(LA27_0 >= 70 && LA27_0 <= 75)) ) {
					alt27=1;
				}

				switch (alt27) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:172:3: clauseOr
					{
					pushFollow(FOLLOW_clauseOr_in_multiClause1284);
					clauseOr65=clauseOr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr65.getTree());
					}
					break;

				default :
					if ( cnt27 >= 1 ) break loop27;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(27, input);
					throw eee;
				}
				cnt27++;
			}

			// AST REWRITE
			// elements: clauseOr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 172:13: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
			{
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:172:16: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "DEFOP"), root_1);
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


	public static class match_all_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "match_all"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:233:1: match_all : STAR COLON STAR ;
	public final ADSParser.match_all_return match_all() throws RecognitionException {
		ADSParser.match_all_return retval = new ADSParser.match_all_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STAR66=null;
		Token COLON67=null;
		Token STAR68=null;

		Object STAR66_tree=null;
		Object COLON67_tree=null;
		Object STAR68_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:234:3: ( STAR COLON STAR )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:235:3: STAR COLON STAR
			{
			root_0 = (Object)adaptor.nil();


			STAR66=(Token)match(input,STAR,FOLLOW_STAR_in_match_all1346); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STAR66_tree = (Object)adaptor.create(STAR66);
			adaptor.addChild(root_0, STAR66_tree);
			}

			COLON67=(Token)match(input,COLON,FOLLOW_COLON_in_match_all1348); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON67_tree = (Object)adaptor.create(COLON67);
			adaptor.addChild(root_0, COLON67_tree);
			}

			STAR68=(Token)match(input,STAR,FOLLOW_STAR_in_match_all1350); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			STAR68_tree = (Object)adaptor.create(STAR68);
			adaptor.addChild(root_0, STAR68_tree);
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
	// $ANTLR end "match_all"


	public static class normal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "normal"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:237:1: normal : ( TERM_NORMAL | NUMBER | TO );
	public final ADSParser.normal_return normal() throws RecognitionException {
		ADSParser.normal_return retval = new ADSParser.normal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set69=null;

		Object set69_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:238:3: ( TERM_NORMAL | NUMBER | TO )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
			{
			root_0 = (Object)adaptor.nil();


			set69=input.LT(1);
			if ( input.LA(1)==NUMBER||input.LA(1)==TERM_NORMAL||input.LA(1)==TO ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set69));
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "truncated"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:247:1: truncated : TERM_TRUNCATED ;
	public final ADSParser.truncated_return truncated() throws RecognitionException {
		ADSParser.truncated_return retval = new ADSParser.truncated_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TERM_TRUNCATED70=null;

		Object TERM_TRUNCATED70_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:248:3: ( TERM_TRUNCATED )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:249:3: TERM_TRUNCATED
			{
			root_0 = (Object)adaptor.nil();


			TERM_TRUNCATED70=(Token)match(input,TERM_TRUNCATED,FOLLOW_TERM_TRUNCATED_in_truncated1406); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TERM_TRUNCATED70_tree = (Object)adaptor.create(TERM_TRUNCATED70);
			adaptor.addChild(root_0, TERM_TRUNCATED70_tree);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "quoted_truncated"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:253:1: quoted_truncated : PHRASE_ANYTHING ;
	public final ADSParser.quoted_truncated_return quoted_truncated() throws RecognitionException {
		ADSParser.quoted_truncated_return retval = new ADSParser.quoted_truncated_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PHRASE_ANYTHING71=null;

		Object PHRASE_ANYTHING71_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:254:3: ( PHRASE_ANYTHING )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:255:3: PHRASE_ANYTHING
			{
			root_0 = (Object)adaptor.nil();


			PHRASE_ANYTHING71=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1424); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			PHRASE_ANYTHING71_tree = (Object)adaptor.create(PHRASE_ANYTHING71);
			adaptor.addChild(root_0, PHRASE_ANYTHING71_tree);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "quoted"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:258:1: quoted : PHRASE ;
	public final ADSParser.quoted_return quoted() throws RecognitionException {
		ADSParser.quoted_return retval = new ADSParser.quoted_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PHRASE72=null;

		Object PHRASE72_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:258:9: ( PHRASE )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:259:3: PHRASE
			{
			root_0 = (Object)adaptor.nil();


			PHRASE72=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_quoted1439); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			PHRASE72_tree = (Object)adaptor.create(PHRASE72);
			adaptor.addChild(root_0, PHRASE72_tree);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "lmodifier"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:263:1: lmodifier : ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] | '=' -> EQUAL[\"=\"] | '#' -> HASH[\"#\"] );
	public final ADSParser.lmodifier_return lmodifier() throws RecognitionException {
		ADSParser.lmodifier_return retval = new ADSParser.lmodifier_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PLUS73=null;
		Token MINUS74=null;
		Token char_literal75=null;
		Token char_literal76=null;

		Object PLUS73_tree=null;
		Object MINUS74_tree=null;
		Object char_literal75_tree=null;
		Object char_literal76_tree=null;
		RewriteRuleTokenStream stream_70=new RewriteRuleTokenStream(adaptor,"token 70");
		RewriteRuleTokenStream stream_72=new RewriteRuleTokenStream(adaptor,"token 72");
		RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
		RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:263:10: ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] | '=' -> EQUAL[\"=\"] | '#' -> HASH[\"#\"] )
			int alt28=4;
			switch ( input.LA(1) ) {
			case PLUS:
				{
				alt28=1;
				}
				break;
			case MINUS:
				{
				alt28=2;
				}
				break;
			case 72:
				{
				alt28=3;
				}
				break;
			case 70:
				{
				alt28=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 28, 0, input);
				throw nvae;
			}
			switch (alt28) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:264:3: PLUS
					{
					PLUS73=(Token)match(input,PLUS,FOLLOW_PLUS_in_lmodifier1453); if (state.failed) return retval; 
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 264:8: -> PLUS[\"+\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(PLUS, "+"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:265:5: MINUS
					{
					MINUS74=(Token)match(input,MINUS,FOLLOW_MINUS_in_lmodifier1464); if (state.failed) return retval; 
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 265:11: -> MINUS[\"-\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(MINUS, "-"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:266:5: '='
					{
					char_literal75=(Token)match(input,72,FOLLOW_72_in_lmodifier1475); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_72.add(char_literal75);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 266:9: -> EQUAL[\"=\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(EQUAL, "="));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:267:5: '#'
					{
					char_literal76=(Token)match(input,70,FOLLOW_70_in_lmodifier1486); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_70.add(char_literal76);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 267:9: -> HASH[\"#\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(HASH, "#"));
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "rmodifier"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:272:1: rmodifier : ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) );
	public final ADSParser.rmodifier_return rmodifier() throws RecognitionException {
		ADSParser.rmodifier_return retval = new ADSParser.rmodifier_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TILDE77=null;
		Token CARAT78=null;
		Token CARAT79=null;
		Token TILDE80=null;

		Object TILDE77_tree=null;
		Object CARAT78_tree=null;
		Object CARAT79_tree=null;
		Object TILDE80_tree=null;
		RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
		RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:272:11: ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) )
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==TILDE) ) {
				alt31=1;
			}
			else if ( (LA31_0==CARAT) ) {
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:273:3: TILDE ( CARAT )?
					{
					TILDE77=(Token)match(input,TILDE,FOLLOW_TILDE_in_rmodifier1507); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_TILDE.add(TILDE77);

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:273:9: ( CARAT )?
					int alt29=2;
					int LA29_0 = input.LA(1);
					if ( (LA29_0==CARAT) ) {
						alt29=1;
					}
					switch (alt29) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:273:9: CARAT
							{
							CARAT78=(Token)match(input,CARAT,FOLLOW_CARAT_in_rmodifier1509); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_CARAT.add(CARAT78);

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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 273:16: -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:273:19: ^( BOOST ( CARAT )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BOOST, "BOOST"), root_1);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:273:27: ( CARAT )?
						if ( stream_CARAT.hasNext() ) {
							adaptor.addChild(root_1, stream_CARAT.nextNode());
						}
						stream_CARAT.reset();

						adaptor.addChild(root_0, root_1);
						}

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:273:35: ^( FUZZY TILDE )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FUZZY, "FUZZY"), root_1);
						adaptor.addChild(root_1, stream_TILDE.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:274:5: CARAT ( TILDE )?
					{
					CARAT79=(Token)match(input,CARAT,FOLLOW_CARAT_in_rmodifier1532); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_CARAT.add(CARAT79);

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:274:11: ( TILDE )?
					int alt30=2;
					int LA30_0 = input.LA(1);
					if ( (LA30_0==TILDE) ) {
						alt30=1;
					}
					switch (alt30) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:274:11: TILDE
							{
							TILDE80=(Token)match(input,TILDE,FOLLOW_TILDE_in_rmodifier1534); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_TILDE.add(TILDE80);

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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 274:18: -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:274:21: ^( BOOST CARAT )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BOOST, "BOOST"), root_1);
						adaptor.addChild(root_1, stream_CARAT.nextNode());
						adaptor.addChild(root_0, root_1);
						}

						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:274:36: ^( FUZZY ( TILDE )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FUZZY, "FUZZY"), root_1);
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:274:44: ( TILDE )?
						if ( stream_TILDE.hasNext() ) {
							adaptor.addChild(root_1, stream_TILDE.nextNode());
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "boost"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:278:1: boost : ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? ;
	public final ADSParser.boost_return boost() throws RecognitionException {
		ADSParser.boost_return retval = new ADSParser.boost_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token CARAT81=null;
		Token NUMBER82=null;

		Object CARAT81_tree=null;
		Object NUMBER82_tree=null;
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:278:7: ( ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:279:3: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )?
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:279:3: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:279:4: CARAT
			{
			CARAT81=(Token)match(input,CARAT,FOLLOW_CARAT_in_boost1565); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_CARAT.add(CARAT81);

			// AST REWRITE
			// elements: 
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 279:10: -> ^( BOOST NUMBER[\"DEF\"] )
			{
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:279:13: ^( BOOST NUMBER[\"DEF\"] )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BOOST, "BOOST"), root_1);
				adaptor.addChild(root_1, (Object)adaptor.create(NUMBER, "DEF"));
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:280:3: ( NUMBER -> ^( BOOST NUMBER ) )?
			int alt32=2;
			int LA32_0 = input.LA(1);
			if ( (LA32_0==NUMBER) ) {
				alt32=1;
			}
			switch (alt32) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:280:4: NUMBER
					{
					NUMBER82=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_boost1581); if (state.failed) return retval; 
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 280:11: -> ^( BOOST NUMBER )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:280:14: ^( BOOST NUMBER )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BOOST, "BOOST"), root_1);
						adaptor.addChild(root_1, stream_NUMBER.nextNode());
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "fuzzy"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:283:1: fuzzy : ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? ;
	public final ADSParser.fuzzy_return fuzzy() throws RecognitionException {
		ADSParser.fuzzy_return retval = new ADSParser.fuzzy_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TILDE83=null;
		Token NUMBER84=null;

		Object TILDE83_tree=null;
		Object NUMBER84_tree=null;
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:283:7: ( ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:284:3: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )?
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:284:3: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:284:4: TILDE
			{
			TILDE83=(Token)match(input,TILDE,FOLLOW_TILDE_in_fuzzy1606); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_TILDE.add(TILDE83);

			// AST REWRITE
			// elements: 
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 284:10: -> ^( FUZZY NUMBER[\"DEF\"] )
			{
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:284:13: ^( FUZZY NUMBER[\"DEF\"] )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FUZZY, "FUZZY"), root_1);
				adaptor.addChild(root_1, (Object)adaptor.create(NUMBER, "DEF"));
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:285:3: ( NUMBER -> ^( FUZZY NUMBER ) )?
			int alt33=2;
			int LA33_0 = input.LA(1);
			if ( (LA33_0==NUMBER) ) {
				alt33=1;
			}
			switch (alt33) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:285:4: NUMBER
					{
					NUMBER84=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_fuzzy1622); if (state.failed) return retval; 
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 285:11: -> ^( FUZZY NUMBER )
					{
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:285:14: ^( FUZZY NUMBER )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FUZZY, "FUZZY"), root_1);
						adaptor.addChild(root_1, stream_NUMBER.nextNode());
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "not"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:288:1: not : ( ( AND NOT )=> AND NOT | NOT );
	public final ADSParser.not_return not() throws RecognitionException {
		ADSParser.not_return retval = new ADSParser.not_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AND85=null;
		Token NOT86=null;
		Token NOT87=null;

		Object AND85_tree=null;
		Object NOT86_tree=null;
		Object NOT87_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:288:5: ( ( AND NOT )=> AND NOT | NOT )
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==AND) && (synpred5_ADS())) {
				alt34=1;
			}
			else if ( (LA34_0==NOT) ) {
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:289:3: ( AND NOT )=> AND NOT
					{
					root_0 = (Object)adaptor.nil();


					AND85=(Token)match(input,AND,FOLLOW_AND_in_not1654); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AND85_tree = (Object)adaptor.create(AND85);
					adaptor.addChild(root_0, AND85_tree);
					}

					NOT86=(Token)match(input,NOT,FOLLOW_NOT_in_not1656); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT86_tree = (Object)adaptor.create(NOT86);
					adaptor.addChild(root_0, NOT86_tree);
					}

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:290:5: NOT
					{
					root_0 = (Object)adaptor.nil();


					NOT87=(Token)match(input,NOT,FOLLOW_NOT_in_not1662); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT87_tree = (Object)adaptor.create(NOT87);
					adaptor.addChild(root_0, NOT87_tree);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "and"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:293:1: and : AND ;
	public final ADSParser.and_return and() throws RecognitionException {
		ADSParser.and_return retval = new ADSParser.and_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AND88=null;

		Object AND88_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:293:7: ( AND )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:294:3: AND
			{
			root_0 = (Object)adaptor.nil();


			AND88=(Token)match(input,AND,FOLLOW_AND_in_and1680); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			AND88_tree = (Object)adaptor.create(AND88);
			adaptor.addChild(root_0, AND88_tree);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "or"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:297:1: or : OR ;
	public final ADSParser.or_return or() throws RecognitionException {
		ADSParser.or_return retval = new ADSParser.or_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token OR89=null;

		Object OR89_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:297:5: ( OR )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:298:3: OR
			{
			root_0 = (Object)adaptor.nil();


			OR89=(Token)match(input,OR,FOLLOW_OR_in_or1697); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			OR89_tree = (Object)adaptor.create(OR89);
			adaptor.addChild(root_0, OR89_tree);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "near"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:301:1: near : ( NEAR -> ^( OPERATOR[$NEAR] ) ) ;
	public final ADSParser.near_return near() throws RecognitionException {
		ADSParser.near_return retval = new ADSParser.near_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token NEAR90=null;

		Object NEAR90_tree=null;
		RewriteRuleTokenStream stream_NEAR=new RewriteRuleTokenStream(adaptor,"token NEAR");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:301:7: ( ( NEAR -> ^( OPERATOR[$NEAR] ) ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:302:3: ( NEAR -> ^( OPERATOR[$NEAR] ) )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:302:3: ( NEAR -> ^( OPERATOR[$NEAR] ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:302:4: NEAR
			{
			NEAR90=(Token)match(input,NEAR,FOLLOW_NEAR_in_near1716); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_NEAR.add(NEAR90);

			// AST REWRITE
			// elements: 
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 302:9: -> ^( OPERATOR[$NEAR] )
			{
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:302:12: ^( OPERATOR[$NEAR] )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, NEAR90), root_1);
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


	public static class comma_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "comma"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:305:1: comma : ( COMMA )+ ;
	public final ADSParser.comma_return comma() throws RecognitionException {
		ADSParser.comma_return retval = new ADSParser.comma_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token COMMA91=null;

		Object COMMA91_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:305:7: ( ( COMMA )+ )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:306:3: ( COMMA )+
			{
			root_0 = (Object)adaptor.nil();


			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:306:3: ( COMMA )+
			int cnt35=0;
			loop35:
			while (true) {
				int alt35=2;
				int LA35_0 = input.LA(1);
				if ( (LA35_0==COMMA) ) {
					alt35=1;
				}

				switch (alt35) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:306:3: COMMA
					{
					COMMA91=(Token)match(input,COMMA,FOLLOW_COMMA_in_comma1739); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA91_tree = (Object)adaptor.create(COMMA91);
					adaptor.addChild(root_0, COMMA91_tree);
					}

					}
					break;

				default :
					if ( cnt35 >= 1 ) break loop35;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(35, input);
					throw eee;
				}
				cnt35++;
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
	// $ANTLR end "comma"


	public static class semicolon_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "semicolon"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:309:1: semicolon : ( SEMICOLON )+ ;
	public final ADSParser.semicolon_return semicolon() throws RecognitionException {
		ADSParser.semicolon_return retval = new ADSParser.semicolon_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token SEMICOLON92=null;

		Object SEMICOLON92_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:310:3: ( ( SEMICOLON )+ )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:311:3: ( SEMICOLON )+
			{
			root_0 = (Object)adaptor.nil();


			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:311:3: ( SEMICOLON )+
			int cnt36=0;
			loop36:
			while (true) {
				int alt36=2;
				int LA36_0 = input.LA(1);
				if ( (LA36_0==SEMICOLON) ) {
					alt36=1;
				}

				switch (alt36) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:311:3: SEMICOLON
					{
					SEMICOLON92=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_semicolon1756); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SEMICOLON92_tree = (Object)adaptor.create(SEMICOLON92);
					adaptor.addChild(root_0, SEMICOLON92_tree);
					}

					}
					break;

				default :
					if ( cnt36 >= 1 ) break loop36;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(36, input);
					throw eee;
				}
				cnt36++;
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
	// $ANTLR end "semicolon"


	public static class date_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:314:1: date : DATE_TOKEN ;
	public final ADSParser.date_return date() throws RecognitionException {
		ADSParser.date_return retval = new ADSParser.date_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token DATE_TOKEN93=null;

		Object DATE_TOKEN93_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:314:7: ( DATE_TOKEN )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:316:3: DATE_TOKEN
			{
			root_0 = (Object)adaptor.nil();


			DATE_TOKEN93=(Token)match(input,DATE_TOKEN,FOLLOW_DATE_TOKEN_in_date1775); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			DATE_TOKEN93_tree = (Object)adaptor.create(DATE_TOKEN93);
			adaptor.addChild(root_0, DATE_TOKEN93_tree);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "identifier"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:319:1: identifier : ( 'doi:' -> QNORMAL[\"doi\"] | 'arxiv:' -> QNORMAL[\"arxiv\"] | 'arXiv:' -> QNORMAL[\"arxiv\"] ) ( TERM_NORMAL -> $identifier TERM_NORMAL | PHRASE_ANYTHING -> $identifier ^( QPHRASETRUNC PHRASE_ANYTHING ) | PHRASE -> $identifier ^( QPHRASE PHRASE ) | NUMBER -> $identifier NUMBER | STAR -> $identifier ^( QANYTHING STAR ) ) ;
	public final ADSParser.identifier_return identifier() throws RecognitionException {
		ADSParser.identifier_return retval = new ADSParser.identifier_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal94=null;
		Token string_literal95=null;
		Token string_literal96=null;
		Token TERM_NORMAL97=null;
		Token PHRASE_ANYTHING98=null;
		Token PHRASE99=null;
		Token NUMBER100=null;
		Token STAR101=null;

		Object string_literal94_tree=null;
		Object string_literal95_tree=null;
		Object string_literal96_tree=null;
		Object TERM_NORMAL97_tree=null;
		Object PHRASE_ANYTHING98_tree=null;
		Object PHRASE99_tree=null;
		Object NUMBER100_tree=null;
		Object STAR101_tree=null;
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
		RewriteRuleTokenStream stream_PHRASE_ANYTHING=new RewriteRuleTokenStream(adaptor,"token PHRASE_ANYTHING");
		RewriteRuleTokenStream stream_PHRASE=new RewriteRuleTokenStream(adaptor,"token PHRASE");
		RewriteRuleTokenStream stream_73=new RewriteRuleTokenStream(adaptor,"token 73");
		RewriteRuleTokenStream stream_74=new RewriteRuleTokenStream(adaptor,"token 74");
		RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
		RewriteRuleTokenStream stream_TERM_NORMAL=new RewriteRuleTokenStream(adaptor,"token TERM_NORMAL");

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:320:3: ( ( 'doi:' -> QNORMAL[\"doi\"] | 'arxiv:' -> QNORMAL[\"arxiv\"] | 'arXiv:' -> QNORMAL[\"arxiv\"] ) ( TERM_NORMAL -> $identifier TERM_NORMAL | PHRASE_ANYTHING -> $identifier ^( QPHRASETRUNC PHRASE_ANYTHING ) | PHRASE -> $identifier ^( QPHRASE PHRASE ) | NUMBER -> $identifier NUMBER | STAR -> $identifier ^( QANYTHING STAR ) ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:322:3: ( 'doi:' -> QNORMAL[\"doi\"] | 'arxiv:' -> QNORMAL[\"arxiv\"] | 'arXiv:' -> QNORMAL[\"arxiv\"] ) ( TERM_NORMAL -> $identifier TERM_NORMAL | PHRASE_ANYTHING -> $identifier ^( QPHRASETRUNC PHRASE_ANYTHING ) | PHRASE -> $identifier ^( QPHRASE PHRASE ) | NUMBER -> $identifier NUMBER | STAR -> $identifier ^( QANYTHING STAR ) )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:322:3: ( 'doi:' -> QNORMAL[\"doi\"] | 'arxiv:' -> QNORMAL[\"arxiv\"] | 'arXiv:' -> QNORMAL[\"arxiv\"] )
			int alt37=3;
			switch ( input.LA(1) ) {
			case 75:
				{
				alt37=1;
				}
				break;
			case 74:
				{
				alt37=2;
				}
				break;
			case 73:
				{
				alt37=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 37, 0, input);
				throw nvae;
			}
			switch (alt37) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:322:4: 'doi:'
					{
					string_literal94=(Token)match(input,75,FOLLOW_75_in_identifier1797); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_75.add(string_literal94);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 322:11: -> QNORMAL[\"doi\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(QNORMAL, "doi"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:323:4: 'arxiv:'
					{
					string_literal95=(Token)match(input,74,FOLLOW_74_in_identifier1807); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_74.add(string_literal95);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 323:13: -> QNORMAL[\"arxiv\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(QNORMAL, "arxiv"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:324:4: 'arXiv:'
					{
					string_literal96=(Token)match(input,73,FOLLOW_73_in_identifier1817); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_73.add(string_literal96);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 324:14: -> QNORMAL[\"arxiv\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(QNORMAL, "arxiv"));
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:325:3: ( TERM_NORMAL -> $identifier TERM_NORMAL | PHRASE_ANYTHING -> $identifier ^( QPHRASETRUNC PHRASE_ANYTHING ) | PHRASE -> $identifier ^( QPHRASE PHRASE ) | NUMBER -> $identifier NUMBER | STAR -> $identifier ^( QANYTHING STAR ) )
			int alt38=5;
			switch ( input.LA(1) ) {
			case TERM_NORMAL:
				{
				alt38=1;
				}
				break;
			case PHRASE_ANYTHING:
				{
				alt38=2;
				}
				break;
			case PHRASE:
				{
				alt38=3;
				}
				break;
			case NUMBER:
				{
				alt38=4;
				}
				break;
			case STAR:
				{
				alt38=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 38, 0, input);
				throw nvae;
			}
			switch (alt38) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:325:4: TERM_NORMAL
					{
					TERM_NORMAL97=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_identifier1829); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL97);

					// AST REWRITE
					// elements: TERM_NORMAL, identifier
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 325:16: -> $identifier TERM_NORMAL
					{
						adaptor.addChild(root_0, stream_retval.nextTree());
						adaptor.addChild(root_0, stream_TERM_NORMAL.nextNode());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:326:5: PHRASE_ANYTHING
					{
					PHRASE_ANYTHING98=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_identifier1842); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_PHRASE_ANYTHING.add(PHRASE_ANYTHING98);

					// AST REWRITE
					// elements: identifier, PHRASE_ANYTHING
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 326:22: -> $identifier ^( QPHRASETRUNC PHRASE_ANYTHING )
					{
						adaptor.addChild(root_0, stream_retval.nextTree());
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:326:37: ^( QPHRASETRUNC PHRASE_ANYTHING )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QPHRASETRUNC, "QPHRASETRUNC"), root_1);
						adaptor.addChild(root_1, stream_PHRASE_ANYTHING.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:327:5: PHRASE
					{
					PHRASE99=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_identifier1860); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_PHRASE.add(PHRASE99);

					// AST REWRITE
					// elements: PHRASE, identifier
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 327:12: -> $identifier ^( QPHRASE PHRASE )
					{
						adaptor.addChild(root_0, stream_retval.nextTree());
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:327:27: ^( QPHRASE PHRASE )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QPHRASE, "QPHRASE"), root_1);
						adaptor.addChild(root_1, stream_PHRASE.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:328:5: NUMBER
					{
					NUMBER100=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_identifier1877); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER100);

					// AST REWRITE
					// elements: NUMBER, identifier
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 328:13: -> $identifier NUMBER
					{
						adaptor.addChild(root_0, stream_retval.nextTree());
						adaptor.addChild(root_0, stream_NUMBER.nextNode());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:329:5: STAR
					{
					STAR101=(Token)match(input,STAR,FOLLOW_STAR_in_identifier1891); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_STAR.add(STAR101);

					// AST REWRITE
					// elements: STAR, identifier
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 329:10: -> $identifier ^( QANYTHING STAR )
					{
						adaptor.addChild(root_0, stream_retval.nextTree());
						// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:329:25: ^( QANYTHING STAR )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QANYTHING, "QANYTHING"), root_1);
						adaptor.addChild(root_1, stream_STAR.nextNode());
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
	// $ANTLR end "identifier"


	public static class coordinate_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "coordinate"
	// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:334:1: coordinate : ( HOUR | H_NUMBER M_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER | H_NUMBER NUMBER ( PLUS | MINUS ) D_NUMBER NUMBER | D_NUMBER M_NUMBER S_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER S_NUMBER | H_NUMBER ( PLUS | MINUS ) D_NUMBER | '<=>' );
	public final ADSParser.coordinate_return coordinate() throws RecognitionException {
		ADSParser.coordinate_return retval = new ADSParser.coordinate_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token HOUR102=null;
		Token H_NUMBER103=null;
		Token M_NUMBER104=null;
		Token set105=null;
		Token D_NUMBER106=null;
		Token M_NUMBER107=null;
		Token H_NUMBER108=null;
		Token NUMBER109=null;
		Token set110=null;
		Token D_NUMBER111=null;
		Token NUMBER112=null;
		Token D_NUMBER113=null;
		Token M_NUMBER114=null;
		Token S_NUMBER115=null;
		Token set116=null;
		Token D_NUMBER117=null;
		Token M_NUMBER118=null;
		Token S_NUMBER119=null;
		Token H_NUMBER120=null;
		Token set121=null;
		Token D_NUMBER122=null;
		Token string_literal123=null;

		Object HOUR102_tree=null;
		Object H_NUMBER103_tree=null;
		Object M_NUMBER104_tree=null;
		Object set105_tree=null;
		Object D_NUMBER106_tree=null;
		Object M_NUMBER107_tree=null;
		Object H_NUMBER108_tree=null;
		Object NUMBER109_tree=null;
		Object set110_tree=null;
		Object D_NUMBER111_tree=null;
		Object NUMBER112_tree=null;
		Object D_NUMBER113_tree=null;
		Object M_NUMBER114_tree=null;
		Object S_NUMBER115_tree=null;
		Object set116_tree=null;
		Object D_NUMBER117_tree=null;
		Object M_NUMBER118_tree=null;
		Object S_NUMBER119_tree=null;
		Object H_NUMBER120_tree=null;
		Object set121_tree=null;
		Object D_NUMBER122_tree=null;
		Object string_literal123_tree=null;

		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:335:3: ( HOUR | H_NUMBER M_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER | H_NUMBER NUMBER ( PLUS | MINUS ) D_NUMBER NUMBER | D_NUMBER M_NUMBER S_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER S_NUMBER | H_NUMBER ( PLUS | MINUS ) D_NUMBER | '<=>' )
			int alt39=6;
			switch ( input.LA(1) ) {
			case HOUR:
				{
				alt39=1;
				}
				break;
			case H_NUMBER:
				{
				switch ( input.LA(2) ) {
				case M_NUMBER:
					{
					alt39=2;
					}
					break;
				case NUMBER:
					{
					alt39=3;
					}
					break;
				case MINUS:
				case PLUS:
					{
					alt39=5;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 39, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case D_NUMBER:
				{
				alt39=4;
				}
				break;
			case 71:
				{
				alt39=6;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 39, 0, input);
				throw nvae;
			}
			switch (alt39) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:339:3: HOUR
					{
					root_0 = (Object)adaptor.nil();


					HOUR102=(Token)match(input,HOUR,FOLLOW_HOUR_in_coordinate1933); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					HOUR102_tree = (Object)adaptor.create(HOUR102);
					adaptor.addChild(root_0, HOUR102_tree);
					}

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:341:3: H_NUMBER M_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER
					{
					root_0 = (Object)adaptor.nil();


					H_NUMBER103=(Token)match(input,H_NUMBER,FOLLOW_H_NUMBER_in_coordinate1942); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					H_NUMBER103_tree = (Object)adaptor.create(H_NUMBER103);
					adaptor.addChild(root_0, H_NUMBER103_tree);
					}

					M_NUMBER104=(Token)match(input,M_NUMBER,FOLLOW_M_NUMBER_in_coordinate1944); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					M_NUMBER104_tree = (Object)adaptor.create(M_NUMBER104);
					adaptor.addChild(root_0, M_NUMBER104_tree);
					}

					set105=input.LT(1);
					if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set105));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					D_NUMBER106=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate1952); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					D_NUMBER106_tree = (Object)adaptor.create(D_NUMBER106);
					adaptor.addChild(root_0, D_NUMBER106_tree);
					}

					M_NUMBER107=(Token)match(input,M_NUMBER,FOLLOW_M_NUMBER_in_coordinate1954); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					M_NUMBER107_tree = (Object)adaptor.create(M_NUMBER107);
					adaptor.addChild(root_0, M_NUMBER107_tree);
					}

					}
					break;
				case 3 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:343:3: H_NUMBER NUMBER ( PLUS | MINUS ) D_NUMBER NUMBER
					{
					root_0 = (Object)adaptor.nil();


					H_NUMBER108=(Token)match(input,H_NUMBER,FOLLOW_H_NUMBER_in_coordinate1963); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					H_NUMBER108_tree = (Object)adaptor.create(H_NUMBER108);
					adaptor.addChild(root_0, H_NUMBER108_tree);
					}

					NUMBER109=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_coordinate1965); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NUMBER109_tree = (Object)adaptor.create(NUMBER109);
					adaptor.addChild(root_0, NUMBER109_tree);
					}

					set110=input.LT(1);
					if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set110));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					D_NUMBER111=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate1973); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					D_NUMBER111_tree = (Object)adaptor.create(D_NUMBER111);
					adaptor.addChild(root_0, D_NUMBER111_tree);
					}

					NUMBER112=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_coordinate1975); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NUMBER112_tree = (Object)adaptor.create(NUMBER112);
					adaptor.addChild(root_0, NUMBER112_tree);
					}

					}
					break;
				case 4 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:345:3: D_NUMBER M_NUMBER S_NUMBER ( PLUS | MINUS ) D_NUMBER M_NUMBER S_NUMBER
					{
					root_0 = (Object)adaptor.nil();


					D_NUMBER113=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate1984); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					D_NUMBER113_tree = (Object)adaptor.create(D_NUMBER113);
					adaptor.addChild(root_0, D_NUMBER113_tree);
					}

					M_NUMBER114=(Token)match(input,M_NUMBER,FOLLOW_M_NUMBER_in_coordinate1986); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					M_NUMBER114_tree = (Object)adaptor.create(M_NUMBER114);
					adaptor.addChild(root_0, M_NUMBER114_tree);
					}

					S_NUMBER115=(Token)match(input,S_NUMBER,FOLLOW_S_NUMBER_in_coordinate1988); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					S_NUMBER115_tree = (Object)adaptor.create(S_NUMBER115);
					adaptor.addChild(root_0, S_NUMBER115_tree);
					}

					set116=input.LT(1);
					if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set116));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					D_NUMBER117=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate1996); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					D_NUMBER117_tree = (Object)adaptor.create(D_NUMBER117);
					adaptor.addChild(root_0, D_NUMBER117_tree);
					}

					M_NUMBER118=(Token)match(input,M_NUMBER,FOLLOW_M_NUMBER_in_coordinate1998); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					M_NUMBER118_tree = (Object)adaptor.create(M_NUMBER118);
					adaptor.addChild(root_0, M_NUMBER118_tree);
					}

					S_NUMBER119=(Token)match(input,S_NUMBER,FOLLOW_S_NUMBER_in_coordinate2000); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					S_NUMBER119_tree = (Object)adaptor.create(S_NUMBER119);
					adaptor.addChild(root_0, S_NUMBER119_tree);
					}

					}
					break;
				case 5 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:347:3: H_NUMBER ( PLUS | MINUS ) D_NUMBER
					{
					root_0 = (Object)adaptor.nil();


					H_NUMBER120=(Token)match(input,H_NUMBER,FOLLOW_H_NUMBER_in_coordinate2009); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					H_NUMBER120_tree = (Object)adaptor.create(H_NUMBER120);
					adaptor.addChild(root_0, H_NUMBER120_tree);
					}

					set121=input.LT(1);
					if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set121));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					D_NUMBER122=(Token)match(input,D_NUMBER,FOLLOW_D_NUMBER_in_coordinate2017); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					D_NUMBER122_tree = (Object)adaptor.create(D_NUMBER122);
					adaptor.addChild(root_0, D_NUMBER122_tree);
					}

					}
					break;
				case 6 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:349:3: '<=>'
					{
					root_0 = (Object)adaptor.nil();


					string_literal123=(Token)match(input,71,FOLLOW_71_in_coordinate2026); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal123_tree = (Object)adaptor.create(string_literal123);
					adaptor.addChild(root_0, string_literal123_tree);
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
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:4: ( ( lmodifier )? func_name )
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:5: ( lmodifier )? func_name
		{
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:5: ( lmodifier )?
		int alt40=2;
		int LA40_0 = input.LA(1);
		if ( (LA40_0==MINUS||LA40_0==PLUS||LA40_0==70||LA40_0==72) ) {
			alt40=1;
		}
		switch (alt40) {
			case 1 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:67:5: lmodifier
				{
				pushFollow(FOLLOW_lmodifier_in_synpred1_ADS399);
				lmodifier();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_func_name_in_synpred1_ADS402);
		func_name();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred1_ADS

	// $ANTLR start synpred2_ADS
	public final void synpred2_ADS_fragment() throws RecognitionException {
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:5: ( lmodifier LPAREN ( clauseOr )+ RPAREN )
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:6: lmodifier LPAREN ( clauseOr )+ RPAREN
		{
		pushFollow(FOLLOW_lmodifier_in_synpred2_ADS465);
		lmodifier();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred2_ADS467); if (state.failed) return;

		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:23: ( clauseOr )+
		int cnt41=0;
		loop41:
		while (true) {
			int alt41=2;
			int LA41_0 = input.LA(1);
			if ( (LA41_0==AUTHOR_SEARCH||(LA41_0 >= COMMA && LA41_0 <= DATE_RANGE)||LA41_0==D_NUMBER||LA41_0==FUNC_NAME||(LA41_0 >= HOUR && LA41_0 <= H_NUMBER)||(LA41_0 >= LBRACK && LA41_0 <= MINUS)||LA41_0==NUMBER||(LA41_0 >= PHRASE && LA41_0 <= PLUS)||LA41_0==QMARK||LA41_0==REGEX||(LA41_0 >= SEMICOLON && LA41_0 <= STAR)||LA41_0==TERM_NORMAL||LA41_0==TERM_TRUNCATED||LA41_0==TO||(LA41_0 >= 70 && LA41_0 <= 75)) ) {
				alt41=1;
			}

			switch (alt41) {
			case 1 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:69:23: clauseOr
				{
				pushFollow(FOLLOW_clauseOr_in_synpred2_ADS469);
				clauseOr();
				state._fsp--;
				if (state.failed) return;

				}
				break;

			default :
				if ( cnt41 >= 1 ) break loop41;
				if (state.backtracking>0) {state.failed=true; return;}
				EarlyExitException eee = new EarlyExitException(41, input);
				throw eee;
			}
			cnt41++;
		}

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred2_ADS472); if (state.failed) return;

		}

	}
	// $ANTLR end synpred2_ADS

	// $ANTLR start synpred3_ADS
	public final void synpred3_ADS_fragment() throws RecognitionException {
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:5: ( LPAREN ( clauseOr )+ RPAREN rmodifier )
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:6: LPAREN ( clauseOr )+ RPAREN rmodifier
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred3_ADS528); if (state.failed) return;

		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:13: ( clauseOr )+
		int cnt42=0;
		loop42:
		while (true) {
			int alt42=2;
			int LA42_0 = input.LA(1);
			if ( (LA42_0==AUTHOR_SEARCH||(LA42_0 >= COMMA && LA42_0 <= DATE_RANGE)||LA42_0==D_NUMBER||LA42_0==FUNC_NAME||(LA42_0 >= HOUR && LA42_0 <= H_NUMBER)||(LA42_0 >= LBRACK && LA42_0 <= MINUS)||LA42_0==NUMBER||(LA42_0 >= PHRASE && LA42_0 <= PLUS)||LA42_0==QMARK||LA42_0==REGEX||(LA42_0 >= SEMICOLON && LA42_0 <= STAR)||LA42_0==TERM_NORMAL||LA42_0==TERM_TRUNCATED||LA42_0==TO||(LA42_0 >= 70 && LA42_0 <= 75)) ) {
				alt42=1;
			}

			switch (alt42) {
			case 1 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:71:13: clauseOr
				{
				pushFollow(FOLLOW_clauseOr_in_synpred3_ADS530);
				clauseOr();
				state._fsp--;
				if (state.failed) return;

				}
				break;

			default :
				if ( cnt42 >= 1 ) break loop42;
				if (state.backtracking>0) {state.failed=true; return;}
				EarlyExitException eee = new EarlyExitException(42, input);
				throw eee;
			}
			cnt42++;
		}

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred3_ADS533); if (state.failed) return;

		pushFollow(FOLLOW_rmodifier_in_synpred3_ADS535);
		rmodifier();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred3_ADS

	// $ANTLR start synpred4_ADS
	public final void synpred4_ADS_fragment() throws RecognitionException {
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:73:5: ( LPAREN )
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:73:6: LPAREN
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred4_ADS590); if (state.failed) return;

		}

	}
	// $ANTLR end synpred4_ADS

	// $ANTLR start synpred5_ADS
	public final void synpred5_ADS_fragment() throws RecognitionException {
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:289:3: ( AND NOT )
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:289:4: AND NOT
		{
		match(input,AND,FOLLOW_AND_in_synpred5_ADS1648); if (state.failed) return;

		match(input,NOT,FOLLOW_NOT_in_synpred5_ADS1650); if (state.failed) return;

		}

	}
	// $ANTLR end synpred5_ADS

	// Delegated rules

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



	public static final BitSet FOLLOW_clauseOr_in_mainQ188 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_EOF_in_mainQ191 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_clauseAnd_in_clauseOr224 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_or_in_clauseOr233 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseAnd_in_clauseOr237 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_clauseNot_in_clauseAnd266 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_and_in_clauseAnd276 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseNot_in_clauseAnd280 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_clauseNear_in_clauseNot309 = new BitSet(new long[]{0x0000000200000012L});
	public static final BitSet FOLLOW_not_in_clauseNot318 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseNear_in_clauseNot322 = new BitSet(new long[]{0x0000000200000012L});
	public static final BitSet FOLLOW_clauseBasic_in_clauseNear353 = new BitSet(new long[]{0x0000000100000002L});
	public static final BitSet FOLLOW_near_in_clauseNear362 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseBasic_in_clauseNear366 = new BitSet(new long[]{0x0000000100000002L});
	public static final BitSet FOLLOW_lmodifier_in_clauseBasic407 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_func_name_in_clauseBasic410 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseOr_in_clauseBasic412 = new BitSet(new long[]{0x4F0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_RPAREN_in_clauseBasic416 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000002L});
	public static final BitSet FOLLOW_rmodifier_in_clauseBasic418 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_lmodifier_in_clauseBasic477 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_LPAREN_in_clauseBasic480 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseOr_in_clauseBasic482 = new BitSet(new long[]{0x4F0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_RPAREN_in_clauseBasic485 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000002L});
	public static final BitSet FOLLOW_rmodifier_in_clauseBasic487 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_lmodifier_in_clauseBasic539 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_LPAREN_in_clauseBasic542 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseOr_in_clauseBasic544 = new BitSet(new long[]{0x4F0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_RPAREN_in_clauseBasic547 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000002L});
	public static final BitSet FOLLOW_rmodifier_in_clauseBasic549 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_clauseBasic595 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseOr_in_clauseBasic597 = new BitSet(new long[]{0x4F0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_RPAREN_in_clauseBasic600 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_atom_in_clauseBasic624 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_lmodifier_in_atom648 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_field_in_atom651 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_multi_value_in_atom653 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000002L});
	public static final BitSet FOLLOW_rmodifier_in_atom655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_lmodifier_in_atom689 = new BitSet(new long[]{0x4D0040640D813080L,0x0000000000000E89L});
	public static final BitSet FOLLOW_field_in_atom692 = new BitSet(new long[]{0x4D0040640D813080L,0x0000000000000E89L});
	public static final BitSet FOLLOW_value_in_atom695 = new BitSet(new long[]{0x0000000000000202L,0x0000000000000002L});
	public static final BitSet FOLLOW_rmodifier_in_atom697 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TERM_NORMAL_in_field756 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_COLON_in_field758 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LBRACK_in_range_term_in802 = new BitSet(new long[]{0x4800006400004000L,0x0000000000000009L});
	public static final BitSet FOLLOW_range_value_in_range_term_in814 = new BitSet(new long[]{0x4880006400004000L,0x0000000000000009L});
	public static final BitSet FOLLOW_TO_in_range_term_in837 = new BitSet(new long[]{0x4800006400004000L,0x0000000000000009L});
	public static final BitSet FOLLOW_range_value_in_range_term_in843 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_RBRACK_in_range_term_in864 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_REGEX_in_value883 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_term_in_in_value897 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identifier_in_value912 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coordinate_in_value926 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_normal_in_value940 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncated_in_value955 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_quoted_in_value971 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_quoted_truncated_in_value985 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DATE_RANGE_in_value999 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AUTHOR_SEARCH_in_value1013 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QMARK_in_value1027 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_match_all_in_value1041 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STAR_in_value1055 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LOCAL_PARAMS_in_value1069 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COMMA_in_value1084 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SEMICOLON_in_value1098 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncated_in_range_value1129 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_quoted_in_range_value1143 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_quoted_truncated_in_range_value1157 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_in_range_value1171 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_normal_in_range_value1185 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STAR_in_range_value1200 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FUNC_NAME_in_func_name1224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_multi_value1242 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_multiClause_in_multi_value1244 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_RPAREN_in_multi_value1246 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_clauseOr_in_multiClause1284 = new BitSet(new long[]{0x4D0040E43D913082L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_STAR_in_match_all1346 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_COLON_in_match_all1348 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_STAR_in_match_all1350 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TERM_TRUNCATED_in_truncated1406 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1424 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PHRASE_in_quoted1439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PLUS_in_lmodifier1453 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_lmodifier1464 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_lmodifier1475 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_lmodifier1486 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TILDE_in_rmodifier1507 = new BitSet(new long[]{0x0000000000000202L});
	public static final BitSet FOLLOW_CARAT_in_rmodifier1509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CARAT_in_rmodifier1532 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
	public static final BitSet FOLLOW_TILDE_in_rmodifier1534 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CARAT_in_boost1565 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_NUMBER_in_boost1581 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TILDE_in_fuzzy1606 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_NUMBER_in_fuzzy1622 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AND_in_not1654 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_NOT_in_not1656 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_not1662 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AND_in_and1680 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OR_in_or1697 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NEAR_in_near1716 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COMMA_in_comma1739 = new BitSet(new long[]{0x0000000000001002L});
	public static final BitSet FOLLOW_SEMICOLON_in_semicolon1756 = new BitSet(new long[]{0x0400000000000002L});
	public static final BitSet FOLLOW_DATE_TOKEN_in_date1775 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_identifier1797 = new BitSet(new long[]{0x4800006400000000L});
	public static final BitSet FOLLOW_74_in_identifier1807 = new BitSet(new long[]{0x4800006400000000L});
	public static final BitSet FOLLOW_73_in_identifier1817 = new BitSet(new long[]{0x4800006400000000L});
	public static final BitSet FOLLOW_TERM_NORMAL_in_identifier1829 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PHRASE_ANYTHING_in_identifier1842 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PHRASE_in_identifier1860 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NUMBER_in_identifier1877 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STAR_in_identifier1891 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HOUR_in_coordinate1933 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_H_NUMBER_in_coordinate1942 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_M_NUMBER_in_coordinate1944 = new BitSet(new long[]{0x0000008020000000L});
	public static final BitSet FOLLOW_set_in_coordinate1946 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_D_NUMBER_in_coordinate1952 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_M_NUMBER_in_coordinate1954 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_H_NUMBER_in_coordinate1963 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_NUMBER_in_coordinate1965 = new BitSet(new long[]{0x0000008020000000L});
	public static final BitSet FOLLOW_set_in_coordinate1967 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_D_NUMBER_in_coordinate1973 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_NUMBER_in_coordinate1975 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_D_NUMBER_in_coordinate1984 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_M_NUMBER_in_coordinate1986 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_S_NUMBER_in_coordinate1988 = new BitSet(new long[]{0x0000008020000000L});
	public static final BitSet FOLLOW_set_in_coordinate1990 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_D_NUMBER_in_coordinate1996 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_M_NUMBER_in_coordinate1998 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_S_NUMBER_in_coordinate2000 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_H_NUMBER_in_coordinate2009 = new BitSet(new long[]{0x0000008020000000L});
	public static final BitSet FOLLOW_set_in_coordinate2011 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_D_NUMBER_in_coordinate2017 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_coordinate2026 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_lmodifier_in_synpred1_ADS399 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_func_name_in_synpred1_ADS402 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_lmodifier_in_synpred2_ADS465 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred2_ADS467 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseOr_in_synpred2_ADS469 = new BitSet(new long[]{0x4F0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_RPAREN_in_synpred2_ADS472 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred3_ADS528 = new BitSet(new long[]{0x4D0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_clauseOr_in_synpred3_ADS530 = new BitSet(new long[]{0x4F0040E43D913080L,0x0000000000000FC9L});
	public static final BitSet FOLLOW_RPAREN_in_synpred3_ADS533 = new BitSet(new long[]{0x0000000000000200L,0x0000000000000002L});
	public static final BitSet FOLLOW_rmodifier_in_synpred3_ADS535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred4_ADS590 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AND_in_synpred5_ADS1648 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_NOT_in_synpred5_ADS1650 = new BitSet(new long[]{0x0000000000000002L});
}
