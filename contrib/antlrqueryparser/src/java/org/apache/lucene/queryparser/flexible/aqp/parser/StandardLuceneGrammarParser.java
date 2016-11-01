// $ANTLR 3.5.2 /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g 2016-11-01 17:08:13

  package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class StandardLuceneGrammarParser extends UnforgivingParser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AMPER", "AND", "ATOM", "BOOST", 
		"CARAT", "CLAUSE", "COLON", "DATE_TOKEN", "DQUOTE", "ESC_CHAR", "FIELD", 
		"FUZZY", "INT", "LBRACK", "LCURLY", "LPAREN", "MINUS", "MODIFIER", "NOT", 
		"NUMBER", "OPERATOR", "OR", "PHRASE", "PHRASE_ANYTHING", "PLUS", "QANYTHING", 
		"QDATE", "QMARK", "QNORMAL", "QPHRASE", "QPHRASETRUNC", "QRANGEEX", "QRANGEIN", 
		"QTRUNCATED", "RBRACK", "RCURLY", "RPAREN", "SQUOTE", "STAR", "TERM_CHAR", 
		"TERM_NORMAL", "TERM_START_CHAR", "TERM_TRUNCATED", "TILDE", "TMODIFIER", 
		"TO", "VBAR", "WS"
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
	public static final int NOT=22;
	public static final int NUMBER=23;
	public static final int OPERATOR=24;
	public static final int OR=25;
	public static final int PHRASE=26;
	public static final int PHRASE_ANYTHING=27;
	public static final int PLUS=28;
	public static final int QANYTHING=29;
	public static final int QDATE=30;
	public static final int QMARK=31;
	public static final int QNORMAL=32;
	public static final int QPHRASE=33;
	public static final int QPHRASETRUNC=34;
	public static final int QRANGEEX=35;
	public static final int QRANGEIN=36;
	public static final int QTRUNCATED=37;
	public static final int RBRACK=38;
	public static final int RCURLY=39;
	public static final int RPAREN=40;
	public static final int SQUOTE=41;
	public static final int STAR=42;
	public static final int TERM_CHAR=43;
	public static final int TERM_NORMAL=44;
	public static final int TERM_START_CHAR=45;
	public static final int TERM_TRUNCATED=46;
	public static final int TILDE=47;
	public static final int TMODIFIER=48;
	public static final int TO=49;
	public static final int VBAR=50;
	public static final int WS=51;

	// delegates
	public UnforgivingParser[] getDelegates() {
		return new UnforgivingParser[] {};
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
	@Override public String[] getTokenNames() { return StandardLuceneGrammarParser.tokenNames; }
	@Override public String getGrammarFileName() { return "/dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g"; }


	public static class mainQ_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "mainQ"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:103:1: mainQ : ( clauseOr )+ EOF -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
	public final StandardLuceneGrammarParser.mainQ_return mainQ() throws RecognitionException {
		StandardLuceneGrammarParser.mainQ_return retval = new StandardLuceneGrammarParser.mainQ_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EOF2=null;
		ParserRuleReturnScope clauseOr1 =null;

		Object EOF2_tree=null;
		RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
		RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:103:7: ( ( clauseOr )+ EOF -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:104:2: ( clauseOr )+ EOF
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:104:2: ( clauseOr )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= LBRACK && LA1_0 <= MINUS)||LA1_0==NUMBER||(LA1_0 >= PHRASE && LA1_0 <= PLUS)||LA1_0==QMARK||LA1_0==STAR||LA1_0==TERM_NORMAL||LA1_0==TERM_TRUNCATED) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:104:2: clauseOr
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
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 104:16: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
			{
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:104:19: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:108:1: clauseOr : (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* ;
	public final StandardLuceneGrammarParser.clauseOr_return clauseOr() throws RecognitionException {
		StandardLuceneGrammarParser.clauseOr_return retval = new StandardLuceneGrammarParser.clauseOr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope or3 =null;

		RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
		RewriteRuleSubtreeStream stream_clauseAnd=new RewriteRuleSubtreeStream(adaptor,"rule clauseAnd");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:109:3: ( (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )* )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:109:5: (first= clauseAnd -> $first) ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:109:5: (first= clauseAnd -> $first)
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:109:6: first= clauseAnd
			{
			pushFollow(FOLLOW_clauseAnd_in_clauseOr246);
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
			// 109:22: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:109:33: ( or others= clauseAnd -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ ) )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==OR) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:109:34: or others= clauseAnd
					{
					pushFollow(FOLLOW_or_in_clauseOr255);
					or3=or();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_or.add(or3.getTree());
					pushFollow(FOLLOW_clauseAnd_in_clauseOr259);
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
					// 109:54: -> ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:109:57: ^( OPERATOR[\"OR\"] ( clauseAnd )+ )
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:112:1: clauseAnd : (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* ;
	public final StandardLuceneGrammarParser.clauseAnd_return clauseAnd() throws RecognitionException {
		StandardLuceneGrammarParser.clauseAnd_return retval = new StandardLuceneGrammarParser.clauseAnd_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope and4 =null;

		RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");
		RewriteRuleSubtreeStream stream_clauseNot=new RewriteRuleSubtreeStream(adaptor,"rule clauseNot");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:113:3: ( (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )* )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:113:5: (first= clauseNot -> $first) ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:113:5: (first= clauseNot -> $first)
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:113:6: first= clauseNot
			{
			pushFollow(FOLLOW_clauseNot_in_clauseAnd288);
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
			// 113:23: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:113:34: ( and others= clauseNot -> ^( OPERATOR[\"AND\"] ( clauseNot )+ ) )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==AND) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:113:35: and others= clauseNot
					{
					pushFollow(FOLLOW_and_in_clauseAnd298);
					and4=and();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_and.add(and4.getTree());
					pushFollow(FOLLOW_clauseNot_in_clauseAnd302);
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
					// 113:56: -> ^( OPERATOR[\"AND\"] ( clauseNot )+ )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:113:59: ^( OPERATOR[\"AND\"] ( clauseNot )+ )
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:116:1: clauseNot : (first= clauseBasic -> $first) ( not others= clauseBasic -> ^( OPERATOR[\"NOT\"] ( clauseBasic )+ ) )* ;
	public final StandardLuceneGrammarParser.clauseNot_return clauseNot() throws RecognitionException {
		StandardLuceneGrammarParser.clauseNot_return retval = new StandardLuceneGrammarParser.clauseNot_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope not5 =null;

		RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
		RewriteRuleSubtreeStream stream_clauseBasic=new RewriteRuleSubtreeStream(adaptor,"rule clauseBasic");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:117:3: ( (first= clauseBasic -> $first) ( not others= clauseBasic -> ^( OPERATOR[\"NOT\"] ( clauseBasic )+ ) )* )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:117:5: (first= clauseBasic -> $first) ( not others= clauseBasic -> ^( OPERATOR[\"NOT\"] ( clauseBasic )+ ) )*
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:117:5: (first= clauseBasic -> $first)
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:117:6: first= clauseBasic
			{
			pushFollow(FOLLOW_clauseBasic_in_clauseNot333);
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
			// 117:24: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:117:35: ( not others= clauseBasic -> ^( OPERATOR[\"NOT\"] ( clauseBasic )+ ) )*
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:117:36: not others= clauseBasic
					{
					pushFollow(FOLLOW_not_in_clauseNot342);
					not5=not();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_not.add(not5.getTree());
					pushFollow(FOLLOW_clauseBasic_in_clauseNot346);
					others=clauseBasic();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_clauseBasic.add(others.getTree());
					// AST REWRITE
					// elements: clauseBasic
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 117:59: -> ^( OPERATOR[\"NOT\"] ( clauseBasic )+ )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:117:62: ^( OPERATOR[\"NOT\"] ( clauseBasic )+ )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "NOT"), root_1);
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


	public static class clauseBasic_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "clauseBasic"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:121:1: clauseBasic : ( ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom );
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
		ParserRuleReturnScope modifier6 =null;
		ParserRuleReturnScope clauseOr8 =null;
		ParserRuleReturnScope term_modifier10 =null;
		ParserRuleReturnScope modifier11 =null;
		ParserRuleReturnScope clauseOr13 =null;
		ParserRuleReturnScope term_modifier15 =null;
		ParserRuleReturnScope clauseOr17 =null;
		ParserRuleReturnScope atom19 =null;

		Object LPAREN7_tree=null;
		Object RPAREN9_tree=null;
		Object LPAREN12_tree=null;
		Object RPAREN14_tree=null;
		Object LPAREN16_tree=null;
		Object RPAREN18_tree=null;
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");
		RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
		RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:122:2: ( ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) ) | ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN -> ( clauseOr )+ | atom )
			int alt12=4;
			switch ( input.LA(1) ) {
			case PLUS:
				{
				int LA12_1 = input.LA(2);
				if ( (synpred1_StandardLuceneGrammar()) ) {
					alt12=1;
				}
				else if ( (synpred2_StandardLuceneGrammar()) ) {
					alt12=2;
				}
				else if ( (true) ) {
					alt12=4;
				}

				}
				break;
			case MINUS:
				{
				int LA12_2 = input.LA(2);
				if ( (synpred1_StandardLuceneGrammar()) ) {
					alt12=1;
				}
				else if ( (synpred2_StandardLuceneGrammar()) ) {
					alt12=2;
				}
				else if ( (true) ) {
					alt12=4;
				}

				}
				break;
			case LPAREN:
				{
				int LA12_3 = input.LA(2);
				if ( (synpred1_StandardLuceneGrammar()) ) {
					alt12=1;
				}
				else if ( (synpred2_StandardLuceneGrammar()) ) {
					alt12=2;
				}
				else if ( (synpred3_StandardLuceneGrammar()) ) {
					alt12=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 12, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
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
				alt12=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 12, 0, input);
				throw nvae;
			}
			switch (alt12) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:2: ( modifier LPAREN ( clauseOr )+ RPAREN )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )?
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:40: ( modifier )?
					int alt5=2;
					int LA5_0 = input.LA(1);
					if ( (LA5_0==MINUS||LA5_0==PLUS) ) {
						alt5=1;
					}
					switch (alt5) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:40: modifier
							{
							pushFollow(FOLLOW_modifier_in_clauseBasic391);
							modifier6=modifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_modifier.add(modifier6.getTree());
							}
							break;

					}

					LPAREN7=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic394); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN7);

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:57: ( clauseOr )+
					int cnt6=0;
					loop6:
					while (true) {
						int alt6=2;
						int LA6_0 = input.LA(1);
						if ( ((LA6_0 >= LBRACK && LA6_0 <= MINUS)||LA6_0==NUMBER||(LA6_0 >= PHRASE && LA6_0 <= PLUS)||LA6_0==QMARK||LA6_0==STAR||LA6_0==TERM_NORMAL||LA6_0==TERM_TRUNCATED) ) {
							alt6=1;
						}

						switch (alt6) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:57: clauseOr
							{
							pushFollow(FOLLOW_clauseOr_in_clauseBasic396);
							clauseOr8=clauseOr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr8.getTree());
							}
							break;

						default :
							if ( cnt6 >= 1 ) break loop6;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(6, input);
							throw eee;
						}
						cnt6++;
					}

					RPAREN9=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic399); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN9);

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:74: ( term_modifier )?
					int alt7=2;
					int LA7_0 = input.LA(1);
					if ( (LA7_0==CARAT||LA7_0==TILDE) ) {
						alt7=1;
					}
					switch (alt7) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:74: term_modifier
							{
							pushFollow(FOLLOW_term_modifier_in_clauseBasic401);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 124:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:124:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLAUSE, "CLAUSE"), root_1);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:124:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_2);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:124:26: ( modifier )?
						if ( stream_modifier.hasNext() ) {
							adaptor.addChild(root_2, stream_modifier.nextTree());
						}
						stream_modifier.reset();

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:124:36: ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
						{
						Object root_3 = (Object)adaptor.nil();
						root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(TMODIFIER, "TMODIFIER"), root_3);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:124:48: ( term_modifier )?
						if ( stream_term_modifier.hasNext() ) {
							adaptor.addChild(root_3, stream_term_modifier.nextTree());
						}
						stream_term_modifier.reset();

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:124:63: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:4: ( LPAREN ( clauseOr )+ RPAREN term_modifier )=> ( modifier )? LPAREN ( clauseOr )+ RPAREN ( term_modifier )?
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:46: ( modifier )?
					int alt8=2;
					int LA8_0 = input.LA(1);
					if ( (LA8_0==MINUS||LA8_0==PLUS) ) {
						alt8=1;
					}
					switch (alt8) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:46: modifier
							{
							pushFollow(FOLLOW_modifier_in_clauseBasic451);
							modifier11=modifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_modifier.add(modifier11.getTree());
							}
							break;

					}

					LPAREN12=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic454); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN12);

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:63: ( clauseOr )+
					int cnt9=0;
					loop9:
					while (true) {
						int alt9=2;
						int LA9_0 = input.LA(1);
						if ( ((LA9_0 >= LBRACK && LA9_0 <= MINUS)||LA9_0==NUMBER||(LA9_0 >= PHRASE && LA9_0 <= PLUS)||LA9_0==QMARK||LA9_0==STAR||LA9_0==TERM_NORMAL||LA9_0==TERM_TRUNCATED) ) {
							alt9=1;
						}

						switch (alt9) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:63: clauseOr
							{
							pushFollow(FOLLOW_clauseOr_in_clauseBasic456);
							clauseOr13=clauseOr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr13.getTree());
							}
							break;

						default :
							if ( cnt9 >= 1 ) break loop9;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(9, input);
							throw eee;
						}
						cnt9++;
					}

					RPAREN14=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic459); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN14);

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:80: ( term_modifier )?
					int alt10=2;
					int LA10_0 = input.LA(1);
					if ( (LA10_0==CARAT||LA10_0==TILDE) ) {
						alt10=1;
					}
					switch (alt10) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:80: term_modifier
							{
							pushFollow(FOLLOW_term_modifier_in_clauseBasic461);
							term_modifier15=term_modifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier15.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: clauseOr, modifier, term_modifier
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 126:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:126:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLAUSE, "CLAUSE"), root_1);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:126:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ) )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_2);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:126:26: ( modifier )?
						if ( stream_modifier.hasNext() ) {
							adaptor.addChild(root_2, stream_modifier.nextTree());
						}
						stream_modifier.reset();

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:126:36: ^( TMODIFIER ( term_modifier )? ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
						{
						Object root_3 = (Object)adaptor.nil();
						root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(TMODIFIER, "TMODIFIER"), root_3);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:126:48: ( term_modifier )?
						if ( stream_term_modifier.hasNext() ) {
							adaptor.addChild(root_3, stream_term_modifier.nextTree());
						}
						stream_term_modifier.reset();

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:126:63: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:127:4: ( LPAREN )=> LPAREN ( clauseOr )+ RPAREN
					{
					LPAREN16=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_clauseBasic506); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN16);

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:127:24: ( clauseOr )+
					int cnt11=0;
					loop11:
					while (true) {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( ((LA11_0 >= LBRACK && LA11_0 <= MINUS)||LA11_0==NUMBER||(LA11_0 >= PHRASE && LA11_0 <= PLUS)||LA11_0==QMARK||LA11_0==STAR||LA11_0==TERM_NORMAL||LA11_0==TERM_TRUNCATED) ) {
							alt11=1;
						}

						switch (alt11) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:127:24: clauseOr
							{
							pushFollow(FOLLOW_clauseOr_in_clauseBasic508);
							clauseOr17=clauseOr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr17.getTree());
							}
							break;

						default :
							if ( cnt11 >= 1 ) break loop11;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(11, input);
							throw eee;
						}
						cnt11++;
					}

					RPAREN18=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_clauseBasic511); if (state.failed) return retval; 
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 128:3: -> ( clauseOr )+
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:129:4: atom
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_atom_in_clauseBasic523);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "atom"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:133:1: atom : ( ( modifier )? field multi_value ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) ) );
	public final StandardLuceneGrammarParser.atom_return atom() throws RecognitionException {
		StandardLuceneGrammarParser.atom_return retval = new StandardLuceneGrammarParser.atom_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope modifier20 =null;
		ParserRuleReturnScope field21 =null;
		ParserRuleReturnScope multi_value22 =null;
		ParserRuleReturnScope term_modifier23 =null;
		ParserRuleReturnScope modifier24 =null;
		ParserRuleReturnScope field25 =null;
		ParserRuleReturnScope value26 =null;
		ParserRuleReturnScope term_modifier27 =null;

		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_multi_value=new RewriteRuleSubtreeStream(adaptor,"rule multi_value");
		RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
		RewriteRuleSubtreeStream stream_term_modifier=new RewriteRuleSubtreeStream(adaptor,"rule term_modifier");
		RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:134:2: ( ( modifier )? field multi_value ( term_modifier )? -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) ) | ( modifier )? ( field )? value ( term_modifier )? -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) ) )
			int alt18=2;
			switch ( input.LA(1) ) {
			case PLUS:
				{
				int LA18_1 = input.LA(2);
				if ( (LA18_1==TERM_NORMAL) ) {
					int LA18_3 = input.LA(3);
					if ( (LA18_3==COLON) ) {
						int LA18_5 = input.LA(4);
						if ( (LA18_5==LPAREN) ) {
							alt18=1;
						}
						else if ( ((LA18_5 >= LBRACK && LA18_5 <= LCURLY)||LA18_5==NUMBER||(LA18_5 >= PHRASE && LA18_5 <= PHRASE_ANYTHING)||LA18_5==QMARK||LA18_5==STAR||LA18_5==TERM_NORMAL||LA18_5==TERM_TRUNCATED) ) {
							alt18=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 18, 5, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( (LA18_3==EOF||LA18_3==AND||LA18_3==CARAT||(LA18_3 >= LBRACK && LA18_3 <= MINUS)||(LA18_3 >= NOT && LA18_3 <= NUMBER)||(LA18_3 >= OR && LA18_3 <= PLUS)||LA18_3==QMARK||LA18_3==RPAREN||LA18_3==STAR||LA18_3==TERM_NORMAL||(LA18_3 >= TERM_TRUNCATED && LA18_3 <= TILDE)) ) {
						alt18=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 18, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( ((LA18_1 >= LBRACK && LA18_1 <= LCURLY)||LA18_1==NUMBER||(LA18_1 >= PHRASE && LA18_1 <= PHRASE_ANYTHING)||LA18_1==QMARK||LA18_1==STAR||LA18_1==TERM_TRUNCATED) ) {
					alt18=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 18, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case MINUS:
				{
				int LA18_2 = input.LA(2);
				if ( (LA18_2==TERM_NORMAL) ) {
					int LA18_3 = input.LA(3);
					if ( (LA18_3==COLON) ) {
						int LA18_5 = input.LA(4);
						if ( (LA18_5==LPAREN) ) {
							alt18=1;
						}
						else if ( ((LA18_5 >= LBRACK && LA18_5 <= LCURLY)||LA18_5==NUMBER||(LA18_5 >= PHRASE && LA18_5 <= PHRASE_ANYTHING)||LA18_5==QMARK||LA18_5==STAR||LA18_5==TERM_NORMAL||LA18_5==TERM_TRUNCATED) ) {
							alt18=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 18, 5, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( (LA18_3==EOF||LA18_3==AND||LA18_3==CARAT||(LA18_3 >= LBRACK && LA18_3 <= MINUS)||(LA18_3 >= NOT && LA18_3 <= NUMBER)||(LA18_3 >= OR && LA18_3 <= PLUS)||LA18_3==QMARK||LA18_3==RPAREN||LA18_3==STAR||LA18_3==TERM_NORMAL||(LA18_3 >= TERM_TRUNCATED && LA18_3 <= TILDE)) ) {
						alt18=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 18, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( ((LA18_2 >= LBRACK && LA18_2 <= LCURLY)||LA18_2==NUMBER||(LA18_2 >= PHRASE && LA18_2 <= PHRASE_ANYTHING)||LA18_2==QMARK||LA18_2==STAR||LA18_2==TERM_TRUNCATED) ) {
					alt18=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 18, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case TERM_NORMAL:
				{
				int LA18_3 = input.LA(2);
				if ( (LA18_3==COLON) ) {
					int LA18_5 = input.LA(3);
					if ( (LA18_5==LPAREN) ) {
						alt18=1;
					}
					else if ( ((LA18_5 >= LBRACK && LA18_5 <= LCURLY)||LA18_5==NUMBER||(LA18_5 >= PHRASE && LA18_5 <= PHRASE_ANYTHING)||LA18_5==QMARK||LA18_5==STAR||LA18_5==TERM_NORMAL||LA18_5==TERM_TRUNCATED) ) {
						alt18=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 18, 5, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA18_3==EOF||LA18_3==AND||LA18_3==CARAT||(LA18_3 >= LBRACK && LA18_3 <= MINUS)||(LA18_3 >= NOT && LA18_3 <= NUMBER)||(LA18_3 >= OR && LA18_3 <= PLUS)||LA18_3==QMARK||LA18_3==RPAREN||LA18_3==STAR||LA18_3==TERM_NORMAL||(LA18_3 >= TERM_TRUNCATED && LA18_3 <= TILDE)) ) {
					alt18=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 18, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:135:2: ( modifier )? field multi_value ( term_modifier )?
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:135:2: ( modifier )?
					int alt13=2;
					int LA13_0 = input.LA(1);
					if ( (LA13_0==MINUS||LA13_0==PLUS) ) {
						alt13=1;
					}
					switch (alt13) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:135:2: modifier
							{
							pushFollow(FOLLOW_modifier_in_atom544);
							modifier20=modifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_modifier.add(modifier20.getTree());
							}
							break;

					}

					pushFollow(FOLLOW_field_in_atom547);
					field21=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field21.getTree());
					pushFollow(FOLLOW_multi_value_in_atom549);
					multi_value22=multi_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_multi_value.add(multi_value22.getTree());
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:135:30: ( term_modifier )?
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0==CARAT||LA14_0==TILDE) ) {
						alt14=1;
					}
					switch (alt14) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:135:30: term_modifier
							{
							pushFollow(FOLLOW_term_modifier_in_atom551);
							term_modifier23=term_modifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier23.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: modifier, term_modifier, multi_value, field
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 136:3: -> ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:136:6: ^( CLAUSE ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CLAUSE, "CLAUSE"), root_1);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:136:15: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) ) )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_2);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:136:26: ( modifier )?
						if ( stream_modifier.hasNext() ) {
							adaptor.addChild(root_2, stream_modifier.nextTree());
						}
						stream_modifier.reset();

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:136:36: ^( TMODIFIER ( term_modifier )? ^( FIELD field multi_value ) )
						{
						Object root_3 = (Object)adaptor.nil();
						root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(TMODIFIER, "TMODIFIER"), root_3);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:136:48: ( term_modifier )?
						if ( stream_term_modifier.hasNext() ) {
							adaptor.addChild(root_3, stream_term_modifier.nextTree());
						}
						stream_term_modifier.reset();

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:136:63: ^( FIELD field multi_value )
						{
						Object root_4 = (Object)adaptor.nil();
						root_4 = (Object)adaptor.becomeRoot((Object)adaptor.create(FIELD, "FIELD"), root_4);
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:137:4: ( modifier )? ( field )? value ( term_modifier )?
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:137:4: ( modifier )?
					int alt15=2;
					int LA15_0 = input.LA(1);
					if ( (LA15_0==MINUS||LA15_0==PLUS) ) {
						alt15=1;
					}
					switch (alt15) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:137:4: modifier
							{
							pushFollow(FOLLOW_modifier_in_atom587);
							modifier24=modifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_modifier.add(modifier24.getTree());
							}
							break;

					}

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:137:14: ( field )?
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
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:137:14: field
							{
							pushFollow(FOLLOW_field_in_atom590);
							field25=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field25.getTree());
							}
							break;

					}

					pushFollow(FOLLOW_value_in_atom593);
					value26=value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_value.add(value26.getTree());
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:137:27: ( term_modifier )?
					int alt17=2;
					int LA17_0 = input.LA(1);
					if ( (LA17_0==CARAT||LA17_0==TILDE) ) {
						alt17=1;
					}
					switch (alt17) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:137:27: term_modifier
							{
							pushFollow(FOLLOW_term_modifier_in_atom595);
							term_modifier27=term_modifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_term_modifier.add(term_modifier27.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: term_modifier, value, field, modifier
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 138:3: -> ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:138:6: ^( MODIFIER ( modifier )? ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) ) )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_1);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:138:17: ( modifier )?
						if ( stream_modifier.hasNext() ) {
							adaptor.addChild(root_1, stream_modifier.nextTree());
						}
						stream_modifier.reset();

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:138:27: ^( TMODIFIER ( term_modifier )? ^( FIELD ( field )? value ) )
						{
						Object root_2 = (Object)adaptor.nil();
						root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(TMODIFIER, "TMODIFIER"), root_2);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:138:39: ( term_modifier )?
						if ( stream_term_modifier.hasNext() ) {
							adaptor.addChild(root_2, stream_term_modifier.nextTree());
						}
						stream_term_modifier.reset();

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:138:54: ^( FIELD ( field )? value )
						{
						Object root_3 = (Object)adaptor.nil();
						root_3 = (Object)adaptor.becomeRoot((Object)adaptor.create(FIELD, "FIELD"), root_3);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:138:62: ( field )?
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:142:1: field : TERM_NORMAL COLON -> TERM_NORMAL ;
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
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:143:2: ( TERM_NORMAL COLON -> TERM_NORMAL )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:144:2: TERM_NORMAL COLON
			{
			TERM_NORMAL28=(Token)match(input,TERM_NORMAL,FOLLOW_TERM_NORMAL_in_field642); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_TERM_NORMAL.add(TERM_NORMAL28);

			COLON29=(Token)match(input,COLON,FOLLOW_COLON_in_field644); if (state.failed) return retval; 
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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 144:20: -> TERM_NORMAL
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


	public static class value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "value"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:147:1: value : ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) | STAR COLON b= STAR -> ^( QANYTHING $b) | STAR -> ^( QTRUNCATED STAR ) );
	public final StandardLuceneGrammarParser.value_return value() throws RecognitionException {
		StandardLuceneGrammarParser.value_return retval = new StandardLuceneGrammarParser.value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token b=null;
		Token QMARK36=null;
		Token STAR37=null;
		Token COLON38=null;
		Token STAR39=null;
		ParserRuleReturnScope range_term_in30 =null;
		ParserRuleReturnScope range_term_ex31 =null;
		ParserRuleReturnScope normal32 =null;
		ParserRuleReturnScope truncated33 =null;
		ParserRuleReturnScope quoted34 =null;
		ParserRuleReturnScope quoted_truncated35 =null;

		Object b_tree=null;
		Object QMARK36_tree=null;
		Object STAR37_tree=null;
		Object COLON38_tree=null;
		Object STAR39_tree=null;
		RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
		RewriteRuleTokenStream stream_QMARK=new RewriteRuleTokenStream(adaptor,"token QMARK");
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
		RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
		RewriteRuleSubtreeStream stream_range_term_in=new RewriteRuleSubtreeStream(adaptor,"rule range_term_in");
		RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
		RewriteRuleSubtreeStream stream_range_term_ex=new RewriteRuleSubtreeStream(adaptor,"rule range_term_ex");
		RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:148:2: ( range_term_in -> ^( QRANGEIN range_term_in ) | range_term_ex -> ^( QRANGEEX range_term_ex ) | normal -> ^( QNORMAL normal ) | truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | QMARK -> ^( QTRUNCATED QMARK ) | STAR COLON b= STAR -> ^( QANYTHING $b) | STAR -> ^( QTRUNCATED STAR ) )
			int alt19=9;
			switch ( input.LA(1) ) {
			case LBRACK:
				{
				alt19=1;
				}
				break;
			case LCURLY:
				{
				alt19=2;
				}
				break;
			case NUMBER:
			case TERM_NORMAL:
				{
				alt19=3;
				}
				break;
			case TERM_TRUNCATED:
				{
				alt19=4;
				}
				break;
			case PHRASE:
				{
				alt19=5;
				}
				break;
			case PHRASE_ANYTHING:
				{
				alt19=6;
				}
				break;
			case QMARK:
				{
				alt19=7;
				}
				break;
			case STAR:
				{
				int LA19_8 = input.LA(2);
				if ( (LA19_8==COLON) ) {
					alt19=8;
				}
				else if ( (LA19_8==EOF||LA19_8==AND||LA19_8==CARAT||(LA19_8 >= LBRACK && LA19_8 <= MINUS)||(LA19_8 >= NOT && LA19_8 <= NUMBER)||(LA19_8 >= OR && LA19_8 <= PLUS)||LA19_8==QMARK||LA19_8==RPAREN||LA19_8==STAR||LA19_8==TERM_NORMAL||(LA19_8 >= TERM_TRUNCATED && LA19_8 <= TILDE)) ) {
					alt19=9;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 19, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:149:2: range_term_in
					{
					pushFollow(FOLLOW_range_term_in_in_value663);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 149:16: -> ^( QRANGEIN range_term_in )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:149:19: ^( QRANGEIN range_term_in )
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
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:150:4: range_term_ex
					{
					pushFollow(FOLLOW_range_term_ex_in_value676);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 150:18: -> ^( QRANGEEX range_term_ex )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:150:21: ^( QRANGEEX range_term_ex )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QRANGEEX, "QRANGEEX"), root_1);
						adaptor.addChild(root_1, stream_range_term_ex.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:151:4: normal
					{
					pushFollow(FOLLOW_normal_in_value690);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 151:11: -> ^( QNORMAL normal )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:151:14: ^( QNORMAL normal )
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
				case 4 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:152:4: truncated
					{
					pushFollow(FOLLOW_truncated_in_value704);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 152:14: -> ^( QTRUNCATED truncated )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:152:17: ^( QTRUNCATED truncated )
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
				case 5 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:153:4: quoted
					{
					pushFollow(FOLLOW_quoted_in_value718);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 153:11: -> ^( QPHRASE quoted )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:153:14: ^( QPHRASE quoted )
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
				case 6 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:154:4: quoted_truncated
					{
					pushFollow(FOLLOW_quoted_truncated_in_value731);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 154:21: -> ^( QPHRASETRUNC quoted_truncated )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:154:24: ^( QPHRASETRUNC quoted_truncated )
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
				case 7 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:155:4: QMARK
					{
					QMARK36=(Token)match(input,QMARK,FOLLOW_QMARK_in_value744); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_QMARK.add(QMARK36);

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
					// 155:10: -> ^( QTRUNCATED QMARK )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:155:13: ^( QTRUNCATED QMARK )
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
				case 8 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:156:4: STAR COLON b= STAR
					{
					STAR37=(Token)match(input,STAR,FOLLOW_STAR_in_value757); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_STAR.add(STAR37);

					COLON38=(Token)match(input,COLON,FOLLOW_COLON_in_value759); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COLON.add(COLON38);

					b=(Token)match(input,STAR,FOLLOW_STAR_in_value763); if (state.failed) return retval; 
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 156:22: -> ^( QANYTHING $b)
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:156:25: ^( QANYTHING $b)
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(QANYTHING, "QANYTHING"), root_1);
						adaptor.addChild(root_1, stream_b.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 9 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:157:5: STAR
					{
					STAR39=(Token)match(input,STAR,FOLLOW_STAR_in_value778); if (state.failed) return retval; 
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 157:10: -> ^( QTRUNCATED STAR )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:157:13: ^( QTRUNCATED STAR )
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "range_term_in"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:162:1: range_term_in : LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK ;
	public final StandardLuceneGrammarParser.range_term_in_return range_term_in() throws RecognitionException {
		StandardLuceneGrammarParser.range_term_in_return retval = new StandardLuceneGrammarParser.range_term_in_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LBRACK40=null;
		Token TO41=null;
		Token RBRACK42=null;
		ParserRuleReturnScope a =null;
		ParserRuleReturnScope b =null;

		Object LBRACK40_tree=null;
		Object TO41_tree=null;
		Object RBRACK42_tree=null;
		RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
		RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
		RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
		RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:163:2: ( LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:164:8: LBRACK (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RBRACK
			{
			LBRACK40=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_range_term_in809); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK40);

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:165:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:165:9: a= range_value
			{
			pushFollow(FOLLOW_range_value_in_range_term_in821);
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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 165:23: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
			{
				adaptor.addChild(root_0, stream_range_value.nextTree());
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:165:38: ^( QANYTHING QANYTHING[\"*\"] )
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

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:166:8: ( ( TO )? b= range_value -> $a ( $b)? )?
			int alt21=2;
			int LA21_0 = input.LA(1);
			if ( (LA21_0==DATE_TOKEN||LA21_0==NUMBER||(LA21_0 >= PHRASE && LA21_0 <= PHRASE_ANYTHING)||LA21_0==STAR||LA21_0==TERM_NORMAL||LA21_0==TERM_TRUNCATED||LA21_0==TO) ) {
				alt21=1;
			}
			switch (alt21) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:166:10: ( TO )? b= range_value
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:166:10: ( TO )?
					int alt20=2;
					int LA20_0 = input.LA(1);
					if ( (LA20_0==TO) ) {
						alt20=1;
					}
					switch (alt20) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:166:10: TO
							{
							TO41=(Token)match(input,TO,FOLLOW_TO_in_range_term_in844); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_TO.add(TO41);

							}
							break;

					}

					pushFollow(FOLLOW_range_value_in_range_term_in849);
					b=range_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range_value.add(b.getTree());
					// AST REWRITE
					// elements: b, a
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
					// 166:28: -> $a ( $b)?
					{
						adaptor.addChild(root_0, stream_a.nextTree());
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:166:35: ( $b)?
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

			RBRACK42=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_range_term_in870); if (state.failed) return retval; 
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "range_term_ex"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:171:1: range_term_ex : LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY ;
	public final StandardLuceneGrammarParser.range_term_ex_return range_term_ex() throws RecognitionException {
		StandardLuceneGrammarParser.range_term_ex_return retval = new StandardLuceneGrammarParser.range_term_ex_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LCURLY43=null;
		Token TO44=null;
		Token RCURLY45=null;
		ParserRuleReturnScope a =null;
		ParserRuleReturnScope b =null;

		Object LCURLY43_tree=null;
		Object TO44_tree=null;
		Object RCURLY45_tree=null;
		RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
		RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
		RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
		RewriteRuleSubtreeStream stream_range_value=new RewriteRuleSubtreeStream(adaptor,"rule range_value");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:172:2: ( LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:173:8: LCURLY (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) ) ( ( TO )? b= range_value -> $a ( $b)? )? RCURLY
			{
			LCURLY43=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_range_term_ex890); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LCURLY.add(LCURLY43);

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:174:8: (a= range_value -> range_value ^( QANYTHING QANYTHING[\"*\"] ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:174:10: a= range_value
			{
			pushFollow(FOLLOW_range_value_in_range_term_ex903);
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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 174:24: -> range_value ^( QANYTHING QANYTHING[\"*\"] )
			{
				adaptor.addChild(root_0, stream_range_value.nextTree());
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:174:39: ^( QANYTHING QANYTHING[\"*\"] )
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

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:175:8: ( ( TO )? b= range_value -> $a ( $b)? )?
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==DATE_TOKEN||LA23_0==NUMBER||(LA23_0 >= PHRASE && LA23_0 <= PHRASE_ANYTHING)||LA23_0==STAR||LA23_0==TERM_NORMAL||LA23_0==TERM_TRUNCATED||LA23_0==TO) ) {
				alt23=1;
			}
			switch (alt23) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:175:10: ( TO )? b= range_value
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:175:10: ( TO )?
					int alt22=2;
					int LA22_0 = input.LA(1);
					if ( (LA22_0==TO) ) {
						alt22=1;
					}
					switch (alt22) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:175:10: TO
							{
							TO44=(Token)match(input,TO,FOLLOW_TO_in_range_term_ex926); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_TO.add(TO44);

							}
							break;

					}

					pushFollow(FOLLOW_range_value_in_range_term_ex931);
					b=range_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_range_value.add(b.getTree());
					// AST REWRITE
					// elements: b, a
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
					// 175:28: -> $a ( $b)?
					{
						adaptor.addChild(root_0, stream_a.nextTree());
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:175:35: ( $b)?
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

			RCURLY45=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_range_term_ex952); if (state.failed) return retval; 
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "range_value"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:179:1: range_value : ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) );
	public final StandardLuceneGrammarParser.range_value_return range_value() throws RecognitionException {
		StandardLuceneGrammarParser.range_value_return retval = new StandardLuceneGrammarParser.range_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STAR51=null;
		ParserRuleReturnScope truncated46 =null;
		ParserRuleReturnScope quoted47 =null;
		ParserRuleReturnScope quoted_truncated48 =null;
		ParserRuleReturnScope date49 =null;
		ParserRuleReturnScope normal50 =null;

		Object STAR51_tree=null;
		RewriteRuleTokenStream stream_STAR=new RewriteRuleTokenStream(adaptor,"token STAR");
		RewriteRuleSubtreeStream stream_quoted=new RewriteRuleSubtreeStream(adaptor,"rule quoted");
		RewriteRuleSubtreeStream stream_date=new RewriteRuleSubtreeStream(adaptor,"rule date");
		RewriteRuleSubtreeStream stream_normal=new RewriteRuleSubtreeStream(adaptor,"rule normal");
		RewriteRuleSubtreeStream stream_truncated=new RewriteRuleSubtreeStream(adaptor,"rule truncated");
		RewriteRuleSubtreeStream stream_quoted_truncated=new RewriteRuleSubtreeStream(adaptor,"rule quoted_truncated");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:180:2: ( truncated -> ^( QTRUNCATED truncated ) | quoted -> ^( QPHRASE quoted ) | quoted_truncated -> ^( QPHRASETRUNC quoted_truncated ) | date -> ^( QNORMAL date ) | normal -> ^( QNORMAL normal ) | STAR -> ^( QANYTHING STAR ) )
			int alt24=6;
			switch ( input.LA(1) ) {
			case TERM_TRUNCATED:
				{
				alt24=1;
				}
				break;
			case PHRASE:
				{
				alt24=2;
				}
				break;
			case PHRASE_ANYTHING:
				{
				alt24=3;
				}
				break;
			case DATE_TOKEN:
				{
				alt24=4;
				}
				break;
			case NUMBER:
			case TERM_NORMAL:
				{
				alt24=5;
				}
				break;
			case STAR:
				{
				alt24=6;
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:181:2: truncated
					{
					pushFollow(FOLLOW_truncated_in_range_value966);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 181:12: -> ^( QTRUNCATED truncated )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:181:15: ^( QTRUNCATED truncated )
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:182:4: quoted
					{
					pushFollow(FOLLOW_quoted_in_range_value979);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 182:11: -> ^( QPHRASE quoted )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:182:14: ^( QPHRASE quoted )
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:183:4: quoted_truncated
					{
					pushFollow(FOLLOW_quoted_truncated_in_range_value992);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 183:21: -> ^( QPHRASETRUNC quoted_truncated )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:183:24: ^( QPHRASETRUNC quoted_truncated )
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:184:4: date
					{
					pushFollow(FOLLOW_date_in_range_value1005);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 184:9: -> ^( QNORMAL date )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:184:12: ^( QNORMAL date )
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:185:4: normal
					{
					pushFollow(FOLLOW_normal_in_range_value1018);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 185:11: -> ^( QNORMAL normal )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:185:14: ^( QNORMAL normal )
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:186:4: STAR
					{
					STAR51=(Token)match(input,STAR,FOLLOW_STAR_in_range_value1032); if (state.failed) return retval; 
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
					// 186:9: -> ^( QANYTHING STAR )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:186:12: ^( QANYTHING STAR )
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


	public static class multi_value_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multi_value"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:189:1: multi_value : LPAREN multiClause RPAREN -> multiClause ;
	public final StandardLuceneGrammarParser.multi_value_return multi_value() throws RecognitionException {
		StandardLuceneGrammarParser.multi_value_return retval = new StandardLuceneGrammarParser.multi_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token LPAREN52=null;
		Token RPAREN54=null;
		ParserRuleReturnScope multiClause53 =null;

		Object LPAREN52_tree=null;
		Object RPAREN54_tree=null;
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleSubtreeStream stream_multiClause=new RewriteRuleSubtreeStream(adaptor,"rule multiClause");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:190:2: ( LPAREN multiClause RPAREN -> multiClause )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:191:2: LPAREN multiClause RPAREN
			{
			LPAREN52=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_multi_value1053); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN52);

			pushFollow(FOLLOW_multiClause_in_multi_value1055);
			multiClause53=multiClause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_multiClause.add(multiClause53.getTree());
			RPAREN54=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_multi_value1057); if (state.failed) return retval; 
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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 191:28: -> multiClause
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multiClause"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:196:1: multiClause : ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) ;
	public final StandardLuceneGrammarParser.multiClause_return multiClause() throws RecognitionException {
		StandardLuceneGrammarParser.multiClause_return retval = new StandardLuceneGrammarParser.multiClause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope clauseOr55 =null;

		RewriteRuleSubtreeStream stream_clauseOr=new RewriteRuleSubtreeStream(adaptor,"rule clauseOr");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:197:2: ( ( clauseOr )+ -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:202:2: ( clauseOr )+
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:202:2: ( clauseOr )+
			int cnt25=0;
			loop25:
			while (true) {
				int alt25=2;
				int LA25_0 = input.LA(1);
				if ( ((LA25_0 >= LBRACK && LA25_0 <= MINUS)||LA25_0==NUMBER||(LA25_0 >= PHRASE && LA25_0 <= PLUS)||LA25_0==QMARK||LA25_0==STAR||LA25_0==TERM_NORMAL||LA25_0==TERM_TRUNCATED) ) {
					alt25=1;
				}

				switch (alt25) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:202:2: clauseOr
					{
					pushFollow(FOLLOW_clauseOr_in_multiClause1084);
					clauseOr55=clauseOr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_clauseOr.add(clauseOr55.getTree());
					}
					break;

				default :
					if ( cnt25 >= 1 ) break loop25;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(25, input);
					throw eee;
				}
				cnt25++;
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
			// 202:12: -> ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
			{
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:202:15: ^( OPERATOR[\"DEFOP\"] ( clauseOr )+ )
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


	public static class multiDefault_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multiDefault"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:215:1: multiDefault : ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) ;
	public final StandardLuceneGrammarParser.multiDefault_return multiDefault() throws RecognitionException {
		StandardLuceneGrammarParser.multiDefault_return retval = new StandardLuceneGrammarParser.multiDefault_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope multiOr56 =null;

		RewriteRuleSubtreeStream stream_multiOr=new RewriteRuleSubtreeStream(adaptor,"rule multiOr");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:216:2: ( ( multiOr )+ -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:217:2: ( multiOr )+
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:217:2: ( multiOr )+
			int cnt26=0;
			loop26:
			while (true) {
				int alt26=2;
				int LA26_0 = input.LA(1);
				if ( ((LA26_0 >= LBRACK && LA26_0 <= LCURLY)||LA26_0==MINUS||LA26_0==NUMBER||(LA26_0 >= PHRASE && LA26_0 <= PLUS)||LA26_0==QMARK||LA26_0==STAR||LA26_0==TERM_NORMAL||LA26_0==TERM_TRUNCATED) ) {
					alt26=1;
				}

				switch (alt26) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:217:2: multiOr
					{
					pushFollow(FOLLOW_multiOr_in_multiDefault1128);
					multiOr56=multiOr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_multiOr.add(multiOr56.getTree());
					}
					break;

				default :
					if ( cnt26 >= 1 ) break loop26;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(26, input);
					throw eee;
				}
				cnt26++;
			}

			// AST REWRITE
			// elements: multiOr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 217:11: -> ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
			{
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:217:14: ^( OPERATOR[\"DEFOP\"] ( multiOr )+ )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "DEFOP"), root_1);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multiOr"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:220:1: multiOr : (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* ;
	public final StandardLuceneGrammarParser.multiOr_return multiOr() throws RecognitionException {
		StandardLuceneGrammarParser.multiOr_return retval = new StandardLuceneGrammarParser.multiOr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope or57 =null;

		RewriteRuleSubtreeStream stream_or=new RewriteRuleSubtreeStream(adaptor,"rule or");
		RewriteRuleSubtreeStream stream_multiAnd=new RewriteRuleSubtreeStream(adaptor,"rule multiAnd");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:221:2: ( (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )* )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:222:2: (first= multiAnd -> $first) ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:222:2: (first= multiAnd -> $first)
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:222:3: first= multiAnd
			{
			pushFollow(FOLLOW_multiAnd_in_multiOr1156);
			first=multiAnd();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_multiAnd.add(first.getTree());
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
			// 222:19: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:222:30: ( or others= multiAnd -> ^( OPERATOR[\"OR\"] ( multiAnd )+ ) )*
			loop27:
			while (true) {
				int alt27=2;
				int LA27_0 = input.LA(1);
				if ( (LA27_0==OR) ) {
					alt27=1;
				}

				switch (alt27) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:222:31: or others= multiAnd
					{
					pushFollow(FOLLOW_or_in_multiOr1166);
					or57=or();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_or.add(or57.getTree());
					pushFollow(FOLLOW_multiAnd_in_multiOr1170);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 222:49: -> ^( OPERATOR[\"OR\"] ( multiAnd )+ )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:222:52: ^( OPERATOR[\"OR\"] ( multiAnd )+ )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "OR"), root_1);
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
					break loop27;
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
	// $ANTLR end "multiOr"


	public static class multiAnd_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multiAnd"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:225:1: multiAnd : (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* ;
	public final StandardLuceneGrammarParser.multiAnd_return multiAnd() throws RecognitionException {
		StandardLuceneGrammarParser.multiAnd_return retval = new StandardLuceneGrammarParser.multiAnd_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope and58 =null;

		RewriteRuleSubtreeStream stream_multiNot=new RewriteRuleSubtreeStream(adaptor,"rule multiNot");
		RewriteRuleSubtreeStream stream_and=new RewriteRuleSubtreeStream(adaptor,"rule and");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:226:2: ( (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )* )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:227:2: (first= multiNot -> $first) ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:227:2: (first= multiNot -> $first)
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:227:3: first= multiNot
			{
			pushFollow(FOLLOW_multiNot_in_multiAnd1201);
			first=multiNot();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_multiNot.add(first.getTree());
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
			// 227:19: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:227:30: ( and others= multiNot -> ^( OPERATOR[\"AND\"] ( multiNot )+ ) )*
			loop28:
			while (true) {
				int alt28=2;
				int LA28_0 = input.LA(1);
				if ( (LA28_0==AND) ) {
					alt28=1;
				}

				switch (alt28) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:227:31: and others= multiNot
					{
					pushFollow(FOLLOW_and_in_multiAnd1211);
					and58=and();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_and.add(and58.getTree());
					pushFollow(FOLLOW_multiNot_in_multiAnd1215);
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 227:51: -> ^( OPERATOR[\"AND\"] ( multiNot )+ )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:227:54: ^( OPERATOR[\"AND\"] ( multiNot )+ )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OPERATOR, "AND"), root_1);
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
					break loop28;
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
	// $ANTLR end "multiAnd"


	public static class multiNot_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multiNot"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:230:1: multiNot : (first= multiBasic -> $first) ( not others= multiBasic -> ^( not ( multiBasic )+ ) )* ;
	public final StandardLuceneGrammarParser.multiNot_return multiNot() throws RecognitionException {
		StandardLuceneGrammarParser.multiNot_return retval = new StandardLuceneGrammarParser.multiNot_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope first =null;
		ParserRuleReturnScope others =null;
		ParserRuleReturnScope not59 =null;

		RewriteRuleSubtreeStream stream_not=new RewriteRuleSubtreeStream(adaptor,"rule not");
		RewriteRuleSubtreeStream stream_multiBasic=new RewriteRuleSubtreeStream(adaptor,"rule multiBasic");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:231:2: ( (first= multiBasic -> $first) ( not others= multiBasic -> ^( not ( multiBasic )+ ) )* )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:232:2: (first= multiBasic -> $first) ( not others= multiBasic -> ^( not ( multiBasic )+ ) )*
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:232:2: (first= multiBasic -> $first)
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:232:3: first= multiBasic
			{
			pushFollow(FOLLOW_multiBasic_in_multiNot1246);
			first=multiBasic();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_multiBasic.add(first.getTree());
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
			// 232:21: -> $first
			{
				adaptor.addChild(root_0, stream_first.nextTree());
			}


			retval.tree = root_0;
			}

			}

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:232:32: ( not others= multiBasic -> ^( not ( multiBasic )+ ) )*
			loop29:
			while (true) {
				int alt29=2;
				int LA29_0 = input.LA(1);
				if ( (LA29_0==AND) ) {
					int LA29_1 = input.LA(2);
					if ( (LA29_1==NOT) ) {
						alt29=1;
					}

				}
				else if ( (LA29_0==NOT) ) {
					alt29=1;
				}

				switch (alt29) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:232:33: not others= multiBasic
					{
					pushFollow(FOLLOW_not_in_multiNot1256);
					not59=not();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_not.add(not59.getTree());
					pushFollow(FOLLOW_multiBasic_in_multiNot1260);
					others=multiBasic();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_multiBasic.add(others.getTree());
					// AST REWRITE
					// elements: multiBasic, not
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 232:54: -> ^( not ( multiBasic )+ )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:232:57: ^( not ( multiBasic )+ )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(stream_not.nextNode(), root_1);
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
					break loop29;
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
	// $ANTLR end "multiNot"


	public static class multiBasic_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "multiBasic"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:237:1: multiBasic : mterm ;
	public final StandardLuceneGrammarParser.multiBasic_return multiBasic() throws RecognitionException {
		StandardLuceneGrammarParser.multiBasic_return retval = new StandardLuceneGrammarParser.multiBasic_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope mterm60 =null;


		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:238:2: ( mterm )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:239:2: mterm
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_mterm_in_multiBasic1287);
			mterm60=mterm();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, mterm60.getTree());

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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "mterm"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:242:1: mterm : ( modifier )? value -> ^( MODIFIER ( modifier )? value ) ;
	public final StandardLuceneGrammarParser.mterm_return mterm() throws RecognitionException {
		StandardLuceneGrammarParser.mterm_return retval = new StandardLuceneGrammarParser.mterm_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope modifier61 =null;
		ParserRuleReturnScope value62 =null;

		RewriteRuleSubtreeStream stream_modifier=new RewriteRuleSubtreeStream(adaptor,"rule modifier");
		RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:243:2: ( ( modifier )? value -> ^( MODIFIER ( modifier )? value ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:244:2: ( modifier )? value
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:244:2: ( modifier )?
			int alt30=2;
			int LA30_0 = input.LA(1);
			if ( (LA30_0==MINUS||LA30_0==PLUS) ) {
				alt30=1;
			}
			switch (alt30) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:244:2: modifier
					{
					pushFollow(FOLLOW_modifier_in_mterm1303);
					modifier61=modifier();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_modifier.add(modifier61.getTree());
					}
					break;

			}

			pushFollow(FOLLOW_value_in_mterm1306);
			value62=value();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_value.add(value62.getTree());
			// AST REWRITE
			// elements: modifier, value
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 244:18: -> ^( MODIFIER ( modifier )? value )
			{
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:244:21: ^( MODIFIER ( modifier )? value )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(MODIFIER, "MODIFIER"), root_1);
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:244:32: ( modifier )?
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "normal"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:248:1: normal : ( TERM_NORMAL | NUMBER );
	public final StandardLuceneGrammarParser.normal_return normal() throws RecognitionException {
		StandardLuceneGrammarParser.normal_return retval = new StandardLuceneGrammarParser.normal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set63=null;

		Object set63_tree=null;

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:249:2: ( TERM_NORMAL | NUMBER )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
			{
			root_0 = (Object)adaptor.nil();


			set63=input.LT(1);
			if ( input.LA(1)==NUMBER||input.LA(1)==TERM_NORMAL ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set63));
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:257:1: truncated : TERM_TRUNCATED ;
	public final StandardLuceneGrammarParser.truncated_return truncated() throws RecognitionException {
		StandardLuceneGrammarParser.truncated_return retval = new StandardLuceneGrammarParser.truncated_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TERM_TRUNCATED64=null;

		Object TERM_TRUNCATED64_tree=null;

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:258:2: ( TERM_TRUNCATED )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:259:2: TERM_TRUNCATED
			{
			root_0 = (Object)adaptor.nil();


			TERM_TRUNCATED64=(Token)match(input,TERM_TRUNCATED,FOLLOW_TERM_TRUNCATED_in_truncated1359); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TERM_TRUNCATED64_tree = (Object)adaptor.create(TERM_TRUNCATED64);
			adaptor.addChild(root_0, TERM_TRUNCATED64_tree);
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:263:1: quoted_truncated : PHRASE_ANYTHING ;
	public final StandardLuceneGrammarParser.quoted_truncated_return quoted_truncated() throws RecognitionException {
		StandardLuceneGrammarParser.quoted_truncated_return retval = new StandardLuceneGrammarParser.quoted_truncated_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PHRASE_ANYTHING65=null;

		Object PHRASE_ANYTHING65_tree=null;

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:264:2: ( PHRASE_ANYTHING )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:265:2: PHRASE_ANYTHING
			{
			root_0 = (Object)adaptor.nil();


			PHRASE_ANYTHING65=(Token)match(input,PHRASE_ANYTHING,FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1374); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			PHRASE_ANYTHING65_tree = (Object)adaptor.create(PHRASE_ANYTHING65);
			adaptor.addChild(root_0, PHRASE_ANYTHING65_tree);
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:268:1: quoted : PHRASE ;
	public final StandardLuceneGrammarParser.quoted_return quoted() throws RecognitionException {
		StandardLuceneGrammarParser.quoted_return retval = new StandardLuceneGrammarParser.quoted_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PHRASE66=null;

		Object PHRASE66_tree=null;

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:268:8: ( PHRASE )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:269:2: PHRASE
			{
			root_0 = (Object)adaptor.nil();


			PHRASE66=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_quoted1386); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			PHRASE66_tree = (Object)adaptor.create(PHRASE66);
			adaptor.addChild(root_0, PHRASE66_tree);
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "operator"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:275:1: operator : ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] ) ;
	public final StandardLuceneGrammarParser.operator_return operator() throws RecognitionException {
		StandardLuceneGrammarParser.operator_return retval = new StandardLuceneGrammarParser.operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AND67=null;
		Token OR68=null;
		Token NOT69=null;

		Object AND67_tree=null;
		Object OR68_tree=null;
		Object NOT69_tree=null;
		RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
		RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
		RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:275:9: ( ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:275:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] )
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:275:11: ( AND -> OPERATOR[\"AND\"] | OR -> OPERATOR[\"OR\"] | NOT -> OPERATOR[\"NOT\"] )
			int alt31=3;
			switch ( input.LA(1) ) {
			case AND:
				{
				alt31=1;
				}
				break;
			case OR:
				{
				alt31=2;
				}
				break;
			case NOT:
				{
				alt31=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 31, 0, input);
				throw nvae;
			}
			switch (alt31) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:276:2: AND
					{
					AND67=(Token)match(input,AND,FOLLOW_AND_in_operator1402); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_AND.add(AND67);

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
					// 276:6: -> OPERATOR[\"AND\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(OPERATOR, "AND"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:277:4: OR
					{
					OR68=(Token)match(input,OR,FOLLOW_OR_in_operator1412); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_OR.add(OR68);

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
					// 277:7: -> OPERATOR[\"OR\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(OPERATOR, "OR"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:278:4: NOT
					{
					NOT69=(Token)match(input,NOT,FOLLOW_NOT_in_operator1422); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NOT.add(NOT69);

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
					// 278:8: -> OPERATOR[\"NOT\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(OPERATOR, "NOT"));
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "modifier"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:281:1: modifier : ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] );
	public final StandardLuceneGrammarParser.modifier_return modifier() throws RecognitionException {
		StandardLuceneGrammarParser.modifier_return retval = new StandardLuceneGrammarParser.modifier_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PLUS70=null;
		Token MINUS71=null;

		Object PLUS70_tree=null;
		Object MINUS71_tree=null;
		RewriteRuleTokenStream stream_PLUS=new RewriteRuleTokenStream(adaptor,"token PLUS");
		RewriteRuleTokenStream stream_MINUS=new RewriteRuleTokenStream(adaptor,"token MINUS");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:281:9: ( PLUS -> PLUS[\"+\"] | MINUS -> MINUS[\"-\"] )
			int alt32=2;
			int LA32_0 = input.LA(1);
			if ( (LA32_0==PLUS) ) {
				alt32=1;
			}
			else if ( (LA32_0==MINUS) ) {
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:282:2: PLUS
					{
					PLUS70=(Token)match(input,PLUS,FOLLOW_PLUS_in_modifier1439); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_PLUS.add(PLUS70);

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
					// 282:7: -> PLUS[\"+\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(PLUS, "+"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:283:4: MINUS
					{
					MINUS71=(Token)match(input,MINUS,FOLLOW_MINUS_in_modifier1449); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_MINUS.add(MINUS71);

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
					// 283:10: -> MINUS[\"-\"]
					{
						adaptor.addChild(root_0, (Object)adaptor.create(MINUS, "-"));
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
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "term_modifier"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:295:1: term_modifier : ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) );
	public final StandardLuceneGrammarParser.term_modifier_return term_modifier() throws RecognitionException {
		StandardLuceneGrammarParser.term_modifier_return retval = new StandardLuceneGrammarParser.term_modifier_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TILDE72=null;
		Token CARAT73=null;
		Token CARAT74=null;
		Token TILDE75=null;

		Object TILDE72_tree=null;
		Object CARAT73_tree=null;
		Object CARAT74_tree=null;
		Object TILDE75_tree=null;
		RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");
		RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:295:15: ( TILDE ( CARAT )? -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE ) | CARAT ( TILDE )? -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? ) )
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:296:2: TILDE ( CARAT )?
					{
					TILDE72=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1467); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_TILDE.add(TILDE72);

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:296:8: ( CARAT )?
					int alt33=2;
					int LA33_0 = input.LA(1);
					if ( (LA33_0==CARAT) ) {
						alt33=1;
					}
					switch (alt33) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:296:8: CARAT
							{
							CARAT73=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1469); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_CARAT.add(CARAT73);

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
					// 296:15: -> ^( BOOST ( CARAT )? ) ^( FUZZY TILDE )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:296:18: ^( BOOST ( CARAT )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BOOST, "BOOST"), root_1);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:296:26: ( CARAT )?
						if ( stream_CARAT.hasNext() ) {
							adaptor.addChild(root_1, stream_CARAT.nextNode());
						}
						stream_CARAT.reset();

						adaptor.addChild(root_0, root_1);
						}

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:296:34: ^( FUZZY TILDE )
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:297:4: CARAT ( TILDE )?
					{
					CARAT74=(Token)match(input,CARAT,FOLLOW_CARAT_in_term_modifier1491); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_CARAT.add(CARAT74);

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:297:10: ( TILDE )?
					int alt34=2;
					int LA34_0 = input.LA(1);
					if ( (LA34_0==TILDE) ) {
						alt34=1;
					}
					switch (alt34) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:297:10: TILDE
							{
							TILDE75=(Token)match(input,TILDE,FOLLOW_TILDE_in_term_modifier1493); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_TILDE.add(TILDE75);

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
					// 297:17: -> ^( BOOST CARAT ) ^( FUZZY ( TILDE )? )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:297:20: ^( BOOST CARAT )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BOOST, "BOOST"), root_1);
						adaptor.addChild(root_1, stream_CARAT.nextNode());
						adaptor.addChild(root_0, root_1);
						}

						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:297:35: ^( FUZZY ( TILDE )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FUZZY, "FUZZY"), root_1);
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:297:43: ( TILDE )?
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
	// $ANTLR end "term_modifier"


	public static class boost_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "boost"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:317:1: boost : ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? ;
	public final StandardLuceneGrammarParser.boost_return boost() throws RecognitionException {
		StandardLuceneGrammarParser.boost_return retval = new StandardLuceneGrammarParser.boost_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token CARAT76=null;
		Token NUMBER77=null;

		Object CARAT76_tree=null;
		Object NUMBER77_tree=null;
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_CARAT=new RewriteRuleTokenStream(adaptor,"token CARAT");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:317:7: ( ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )? )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:318:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( BOOST NUMBER ) )?
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:318:2: ( CARAT -> ^( BOOST NUMBER[\"DEF\"] ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:318:3: CARAT
			{
			CARAT76=(Token)match(input,CARAT,FOLLOW_CARAT_in_boost1525); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_CARAT.add(CARAT76);

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
			// 318:9: -> ^( BOOST NUMBER[\"DEF\"] )
			{
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:318:12: ^( BOOST NUMBER[\"DEF\"] )
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

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:319:2: ( NUMBER -> ^( BOOST NUMBER ) )?
			int alt36=2;
			int LA36_0 = input.LA(1);
			if ( (LA36_0==NUMBER) ) {
				alt36=1;
			}
			switch (alt36) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:319:3: NUMBER
					{
					NUMBER77=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_boost1540); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER77);

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
					// 319:10: -> ^( BOOST NUMBER )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:319:13: ^( BOOST NUMBER )
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:1: fuzzy : ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? ;
	public final StandardLuceneGrammarParser.fuzzy_return fuzzy() throws RecognitionException {
		StandardLuceneGrammarParser.fuzzy_return retval = new StandardLuceneGrammarParser.fuzzy_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TILDE78=null;
		Token NUMBER79=null;

		Object TILDE78_tree=null;
		Object NUMBER79_tree=null;
		RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
		RewriteRuleTokenStream stream_TILDE=new RewriteRuleTokenStream(adaptor,"token TILDE");

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:7: ( ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )? )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:323:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) ) ( NUMBER -> ^( FUZZY NUMBER ) )?
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:323:2: ( TILDE -> ^( FUZZY NUMBER[\"DEF\"] ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:323:3: TILDE
			{
			TILDE78=(Token)match(input,TILDE,FOLLOW_TILDE_in_fuzzy1563); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_TILDE.add(TILDE78);

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
			// 323:9: -> ^( FUZZY NUMBER[\"DEF\"] )
			{
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:323:12: ^( FUZZY NUMBER[\"DEF\"] )
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

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:2: ( NUMBER -> ^( FUZZY NUMBER ) )?
			int alt37=2;
			int LA37_0 = input.LA(1);
			if ( (LA37_0==NUMBER) ) {
				alt37=1;
			}
			switch (alt37) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:3: NUMBER
					{
					NUMBER79=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_fuzzy1578); if (state.failed) return retval; 
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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 324:10: -> ^( FUZZY NUMBER )
					{
						// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:13: ^( FUZZY NUMBER )
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:327:1: not : ( ( AND NOT )=> AND NOT | NOT );
	public final StandardLuceneGrammarParser.not_return not() throws RecognitionException {
		StandardLuceneGrammarParser.not_return retval = new StandardLuceneGrammarParser.not_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AND80=null;
		Token NOT81=null;
		Token NOT82=null;

		Object AND80_tree=null;
		Object NOT81_tree=null;
		Object NOT82_tree=null;

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:327:5: ( ( AND NOT )=> AND NOT | NOT )
			int alt38=2;
			int LA38_0 = input.LA(1);
			if ( (LA38_0==AND) && (synpred4_StandardLuceneGrammar())) {
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
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:328:2: ( AND NOT )=> AND NOT
					{
					root_0 = (Object)adaptor.nil();


					AND80=(Token)match(input,AND,FOLLOW_AND_in_not1608); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AND80_tree = (Object)adaptor.create(AND80);
					adaptor.addChild(root_0, AND80_tree);
					}

					NOT81=(Token)match(input,NOT,FOLLOW_NOT_in_not1610); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT81_tree = (Object)adaptor.create(NOT81);
					adaptor.addChild(root_0, NOT81_tree);
					}

					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:329:4: NOT
					{
					root_0 = (Object)adaptor.nil();


					NOT82=(Token)match(input,NOT,FOLLOW_NOT_in_not1615); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT82_tree = (Object)adaptor.create(NOT82);
					adaptor.addChild(root_0, NOT82_tree);
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:332:1: and : AND ;
	public final StandardLuceneGrammarParser.and_return and() throws RecognitionException {
		StandardLuceneGrammarParser.and_return retval = new StandardLuceneGrammarParser.and_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token AND83=null;

		Object AND83_tree=null;

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:332:6: ( AND )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:333:2: AND
			{
			root_0 = (Object)adaptor.nil();


			AND83=(Token)match(input,AND,FOLLOW_AND_in_and1629); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			AND83_tree = (Object)adaptor.create(AND83);
			adaptor.addChild(root_0, AND83_tree);
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
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:336:1: or : OR ;
	public final StandardLuceneGrammarParser.or_return or() throws RecognitionException {
		StandardLuceneGrammarParser.or_return retval = new StandardLuceneGrammarParser.or_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token OR84=null;

		Object OR84_tree=null;

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:336:5: ( OR )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:337:2: OR
			{
			root_0 = (Object)adaptor.nil();


			OR84=(Token)match(input,OR,FOLLOW_OR_in_or1643); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			OR84_tree = (Object)adaptor.create(OR84);
			adaptor.addChild(root_0, OR84_tree);
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


	public static class date_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "date"
	// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:341:1: date : DATE_TOKEN ;
	public final StandardLuceneGrammarParser.date_return date() throws RecognitionException {
		StandardLuceneGrammarParser.date_return retval = new StandardLuceneGrammarParser.date_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token DATE_TOKEN85=null;

		Object DATE_TOKEN85_tree=null;

		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:341:6: ( DATE_TOKEN )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:343:2: DATE_TOKEN
			{
			root_0 = (Object)adaptor.nil();


			DATE_TOKEN85=(Token)match(input,DATE_TOKEN,FOLLOW_DATE_TOKEN_in_date1660); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			DATE_TOKEN85_tree = (Object)adaptor.create(DATE_TOKEN85);
			adaptor.addChild(root_0, DATE_TOKEN85_tree);
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
		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:2: ( modifier LPAREN ( clauseOr )+ RPAREN )
		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:3: modifier LPAREN ( clauseOr )+ RPAREN
		{
		pushFollow(FOLLOW_modifier_in_synpred1_StandardLuceneGrammar379);
		modifier();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar381); if (state.failed) return;

		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:19: ( clauseOr )+
		int cnt39=0;
		loop39:
		while (true) {
			int alt39=2;
			int LA39_0 = input.LA(1);
			if ( ((LA39_0 >= LBRACK && LA39_0 <= MINUS)||LA39_0==NUMBER||(LA39_0 >= PHRASE && LA39_0 <= PLUS)||LA39_0==QMARK||LA39_0==STAR||LA39_0==TERM_NORMAL||LA39_0==TERM_TRUNCATED) ) {
				alt39=1;
			}

			switch (alt39) {
			case 1 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:123:19: clauseOr
				{
				pushFollow(FOLLOW_clauseOr_in_synpred1_StandardLuceneGrammar383);
				clauseOr();
				state._fsp--;
				if (state.failed) return;

				}
				break;

			default :
				if ( cnt39 >= 1 ) break loop39;
				if (state.backtracking>0) {state.failed=true; return;}
				EarlyExitException eee = new EarlyExitException(39, input);
				throw eee;
			}
			cnt39++;
		}

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar386); if (state.failed) return;

		}

	}
	// $ANTLR end synpred1_StandardLuceneGrammar

	// $ANTLR start synpred2_StandardLuceneGrammar
	public final void synpred2_StandardLuceneGrammar_fragment() throws RecognitionException {
		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:4: ( LPAREN ( clauseOr )+ RPAREN term_modifier )
		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:5: LPAREN ( clauseOr )+ RPAREN term_modifier
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar440); if (state.failed) return;

		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:12: ( clauseOr )+
		int cnt40=0;
		loop40:
		while (true) {
			int alt40=2;
			int LA40_0 = input.LA(1);
			if ( ((LA40_0 >= LBRACK && LA40_0 <= MINUS)||LA40_0==NUMBER||(LA40_0 >= PHRASE && LA40_0 <= PLUS)||LA40_0==QMARK||LA40_0==STAR||LA40_0==TERM_NORMAL||LA40_0==TERM_TRUNCATED) ) {
				alt40=1;
			}

			switch (alt40) {
			case 1 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:125:12: clauseOr
				{
				pushFollow(FOLLOW_clauseOr_in_synpred2_StandardLuceneGrammar442);
				clauseOr();
				state._fsp--;
				if (state.failed) return;

				}
				break;

			default :
				if ( cnt40 >= 1 ) break loop40;
				if (state.backtracking>0) {state.failed=true; return;}
				EarlyExitException eee = new EarlyExitException(40, input);
				throw eee;
			}
			cnt40++;
		}

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar445); if (state.failed) return;

		pushFollow(FOLLOW_term_modifier_in_synpred2_StandardLuceneGrammar447);
		term_modifier();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred2_StandardLuceneGrammar

	// $ANTLR start synpred3_StandardLuceneGrammar
	public final void synpred3_StandardLuceneGrammar_fragment() throws RecognitionException {
		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:127:4: ( LPAREN )
		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:127:5: LPAREN
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar500); if (state.failed) return;

		}

	}
	// $ANTLR end synpred3_StandardLuceneGrammar

	// $ANTLR start synpred4_StandardLuceneGrammar
	public final void synpred4_StandardLuceneGrammar_fragment() throws RecognitionException {
		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:328:2: ( AND NOT )
		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:328:3: AND NOT
		{
		match(input,AND,FOLLOW_AND_in_synpred4_StandardLuceneGrammar1602); if (state.failed) return;

		match(input,NOT,FOLLOW_NOT_in_synpred4_StandardLuceneGrammar1604); if (state.failed) return;

		}

	}
	// $ANTLR end synpred4_StandardLuceneGrammar

	// Delegated rules

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



	public static final BitSet FOLLOW_clauseOr_in_mainQ212 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_EOF_in_mainQ215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_clauseAnd_in_clauseOr246 = new BitSet(new long[]{0x0000000002000002L});
	public static final BitSet FOLLOW_or_in_clauseOr255 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_clauseAnd_in_clauseOr259 = new BitSet(new long[]{0x0000000002000002L});
	public static final BitSet FOLLOW_clauseNot_in_clauseAnd288 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_and_in_clauseAnd298 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_clauseNot_in_clauseAnd302 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_clauseBasic_in_clauseNot333 = new BitSet(new long[]{0x0000000000400022L});
	public static final BitSet FOLLOW_not_in_clauseNot342 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_clauseBasic_in_clauseNot346 = new BitSet(new long[]{0x0000000000400022L});
	public static final BitSet FOLLOW_modifier_in_clauseBasic391 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_LPAREN_in_clauseBasic394 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_clauseOr_in_clauseBasic396 = new BitSet(new long[]{0x000055009C9E0000L});
	public static final BitSet FOLLOW_RPAREN_in_clauseBasic399 = new BitSet(new long[]{0x0000800000000102L});
	public static final BitSet FOLLOW_term_modifier_in_clauseBasic401 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_modifier_in_clauseBasic451 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_LPAREN_in_clauseBasic454 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_clauseOr_in_clauseBasic456 = new BitSet(new long[]{0x000055009C9E0000L});
	public static final BitSet FOLLOW_RPAREN_in_clauseBasic459 = new BitSet(new long[]{0x0000800000000102L});
	public static final BitSet FOLLOW_term_modifier_in_clauseBasic461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_clauseBasic506 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_clauseOr_in_clauseBasic508 = new BitSet(new long[]{0x000055009C9E0000L});
	public static final BitSet FOLLOW_RPAREN_in_clauseBasic511 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_atom_in_clauseBasic523 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_modifier_in_atom544 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_field_in_atom547 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_multi_value_in_atom549 = new BitSet(new long[]{0x0000800000000102L});
	public static final BitSet FOLLOW_term_modifier_in_atom551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_modifier_in_atom587 = new BitSet(new long[]{0x000054008C860000L});
	public static final BitSet FOLLOW_field_in_atom590 = new BitSet(new long[]{0x000054008C860000L});
	public static final BitSet FOLLOW_value_in_atom593 = new BitSet(new long[]{0x0000800000000102L});
	public static final BitSet FOLLOW_term_modifier_in_atom595 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TERM_NORMAL_in_field642 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_COLON_in_field644 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_term_in_in_value663 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_term_ex_in_value676 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_normal_in_value690 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncated_in_value704 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_quoted_in_value718 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_quoted_truncated_in_value731 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QMARK_in_value744 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STAR_in_value757 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_COLON_in_value759 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_STAR_in_value763 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STAR_in_value778 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LBRACK_in_range_term_in809 = new BitSet(new long[]{0x000054000C800800L});
	public static final BitSet FOLLOW_range_value_in_range_term_in821 = new BitSet(new long[]{0x000254400C800800L});
	public static final BitSet FOLLOW_TO_in_range_term_in844 = new BitSet(new long[]{0x000054000C800800L});
	public static final BitSet FOLLOW_range_value_in_range_term_in849 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_RBRACK_in_range_term_in870 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LCURLY_in_range_term_ex890 = new BitSet(new long[]{0x000054000C800800L});
	public static final BitSet FOLLOW_range_value_in_range_term_ex903 = new BitSet(new long[]{0x000254800C800800L});
	public static final BitSet FOLLOW_TO_in_range_term_ex926 = new BitSet(new long[]{0x000054000C800800L});
	public static final BitSet FOLLOW_range_value_in_range_term_ex931 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_RCURLY_in_range_term_ex952 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncated_in_range_value966 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_quoted_in_range_value979 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_quoted_truncated_in_range_value992 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_in_range_value1005 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_normal_in_range_value1018 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STAR_in_range_value1032 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_multi_value1053 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_multiClause_in_multi_value1055 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_RPAREN_in_multi_value1057 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_clauseOr_in_multiClause1084 = new BitSet(new long[]{0x000054009C9E0002L});
	public static final BitSet FOLLOW_multiOr_in_multiDefault1128 = new BitSet(new long[]{0x000054009C960002L});
	public static final BitSet FOLLOW_multiAnd_in_multiOr1156 = new BitSet(new long[]{0x0000000002000002L});
	public static final BitSet FOLLOW_or_in_multiOr1166 = new BitSet(new long[]{0x000054009C960000L});
	public static final BitSet FOLLOW_multiAnd_in_multiOr1170 = new BitSet(new long[]{0x0000000002000002L});
	public static final BitSet FOLLOW_multiNot_in_multiAnd1201 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_and_in_multiAnd1211 = new BitSet(new long[]{0x000054009C960000L});
	public static final BitSet FOLLOW_multiNot_in_multiAnd1215 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_multiBasic_in_multiNot1246 = new BitSet(new long[]{0x0000000000400022L});
	public static final BitSet FOLLOW_not_in_multiNot1256 = new BitSet(new long[]{0x000054009C960000L});
	public static final BitSet FOLLOW_multiBasic_in_multiNot1260 = new BitSet(new long[]{0x0000000000400022L});
	public static final BitSet FOLLOW_mterm_in_multiBasic1287 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_modifier_in_mterm1303 = new BitSet(new long[]{0x000054008C860000L});
	public static final BitSet FOLLOW_value_in_mterm1306 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TERM_TRUNCATED_in_truncated1359 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PHRASE_ANYTHING_in_quoted_truncated1374 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PHRASE_in_quoted1386 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AND_in_operator1402 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OR_in_operator1412 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_operator1422 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PLUS_in_modifier1439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_modifier1449 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TILDE_in_term_modifier1467 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_CARAT_in_term_modifier1469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CARAT_in_term_modifier1491 = new BitSet(new long[]{0x0000800000000002L});
	public static final BitSet FOLLOW_TILDE_in_term_modifier1493 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CARAT_in_boost1525 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_NUMBER_in_boost1540 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TILDE_in_fuzzy1563 = new BitSet(new long[]{0x0000000000800002L});
	public static final BitSet FOLLOW_NUMBER_in_fuzzy1578 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AND_in_not1608 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_NOT_in_not1610 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_not1615 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AND_in_and1629 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OR_in_or1643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DATE_TOKEN_in_date1660 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_modifier_in_synpred1_StandardLuceneGrammar379 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred1_StandardLuceneGrammar381 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_clauseOr_in_synpred1_StandardLuceneGrammar383 = new BitSet(new long[]{0x000055009C9E0000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred1_StandardLuceneGrammar386 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred2_StandardLuceneGrammar440 = new BitSet(new long[]{0x000054009C9E0000L});
	public static final BitSet FOLLOW_clauseOr_in_synpred2_StandardLuceneGrammar442 = new BitSet(new long[]{0x000055009C9E0000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred2_StandardLuceneGrammar445 = new BitSet(new long[]{0x0000800000000100L});
	public static final BitSet FOLLOW_term_modifier_in_synpred2_StandardLuceneGrammar447 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred3_StandardLuceneGrammar500 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AND_in_synpred4_StandardLuceneGrammar1602 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_NOT_in_synpred4_StandardLuceneGrammar1604 = new BitSet(new long[]{0x0000000000000002L});
}
