// $ANTLR 3.5.2 /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g 2016-11-02 17:01:07

  package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class StandardLuceneGrammarLexer extends Lexer {
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

		public void recover(RecognitionException re) {
			// throw unchecked exception
		  throw new RuntimeException(re);
		}


	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public StandardLuceneGrammarLexer() {} 
	public StandardLuceneGrammarLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public StandardLuceneGrammarLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "/dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g"; }

	// $ANTLR start "LPAREN"
	public final void mLPAREN() throws RecognitionException {
		try {
			int _type = LPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:353:9: ( '(' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:353:11: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LPAREN"

	// $ANTLR start "RPAREN"
	public final void mRPAREN() throws RecognitionException {
		try {
			int _type = RPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:355:9: ( ')' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:355:11: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RPAREN"

	// $ANTLR start "LBRACK"
	public final void mLBRACK() throws RecognitionException {
		try {
			int _type = LBRACK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:357:9: ( '[' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:357:11: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LBRACK"

	// $ANTLR start "RBRACK"
	public final void mRBRACK() throws RecognitionException {
		try {
			int _type = RBRACK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:359:9: ( ']' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:359:11: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RBRACK"

	// $ANTLR start "COLON"
	public final void mCOLON() throws RecognitionException {
		try {
			int _type = COLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:361:9: ( ':' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:361:11: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COLON"

	// $ANTLR start "PLUS"
	public final void mPLUS() throws RecognitionException {
		try {
			int _type = PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:363:7: ( '+' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:363:9: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUS"

	// $ANTLR start "MINUS"
	public final void mMINUS() throws RecognitionException {
		try {
			int _type = MINUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:365:7: ( ( '-' | '\\!' ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
			{
			if ( input.LA(1)=='!'||input.LA(1)=='-' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MINUS"

	// $ANTLR start "STAR"
	public final void mSTAR() throws RecognitionException {
		try {
			int _type = STAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:367:7: ( '*' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:367:9: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STAR"

	// $ANTLR start "QMARK"
	public final void mQMARK() throws RecognitionException {
		try {
			int _type = QMARK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:369:8: ( ( '?' )+ )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:369:10: ( '?' )+
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:369:10: ( '?' )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0=='?') ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:369:10: '?'
					{
					match('?'); 
					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "QMARK"

	// $ANTLR start "VBAR"
	public final void mVBAR() throws RecognitionException {
		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:371:16: ( '|' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:371:18: '|'
			{
			match('|'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "VBAR"

	// $ANTLR start "AMPER"
	public final void mAMPER() throws RecognitionException {
		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:373:16: ( '&' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:373:18: '&'
			{
			match('&'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AMPER"

	// $ANTLR start "LCURLY"
	public final void mLCURLY() throws RecognitionException {
		try {
			int _type = LCURLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:375:9: ( '{' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:375:11: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LCURLY"

	// $ANTLR start "RCURLY"
	public final void mRCURLY() throws RecognitionException {
		try {
			int _type = RCURLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:377:9: ( '}' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:377:11: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RCURLY"

	// $ANTLR start "CARAT"
	public final void mCARAT() throws RecognitionException {
		try {
			int _type = CARAT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:379:7: ( '^' ( ( INT )+ ( '.' ( INT )+ )? )? )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:379:9: '^' ( ( INT )+ ( '.' ( INT )+ )? )?
			{
			match('^'); 
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:379:13: ( ( INT )+ ( '.' ( INT )+ )? )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( ((LA5_0 >= '0' && LA5_0 <= '9')) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:379:14: ( INT )+ ( '.' ( INT )+ )?
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:379:14: ( INT )+
					int cnt2=0;
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt2 >= 1 ) break loop2;
							EarlyExitException eee = new EarlyExitException(2, input);
							throw eee;
						}
						cnt2++;
					}

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:379:19: ( '.' ( INT )+ )?
					int alt4=2;
					int LA4_0 = input.LA(1);
					if ( (LA4_0=='.') ) {
						alt4=1;
					}
					switch (alt4) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:379:20: '.' ( INT )+
							{
							match('.'); 
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:379:24: ( INT )+
							int cnt3=0;
							loop3:
							while (true) {
								int alt3=2;
								int LA3_0 = input.LA(1);
								if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
									alt3=1;
								}

								switch (alt3) {
								case 1 :
									// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
									{
									if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
										input.consume();
									}
									else {
										MismatchedSetException mse = new MismatchedSetException(null,input);
										recover(mse);
										throw mse;
									}
									}
									break;

								default :
									if ( cnt3 >= 1 ) break loop3;
									EarlyExitException eee = new EarlyExitException(3, input);
									throw eee;
								}
								cnt3++;
							}

							}
							break;

					}

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CARAT"

	// $ANTLR start "TILDE"
	public final void mTILDE() throws RecognitionException {
		try {
			int _type = TILDE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:381:7: ( '~' ( ( INT )+ ( '.' ( INT )+ )? )? )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:381:9: '~' ( ( INT )+ ( '.' ( INT )+ )? )?
			{
			match('~'); 
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:381:13: ( ( INT )+ ( '.' ( INT )+ )? )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( ((LA9_0 >= '0' && LA9_0 <= '9')) ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:381:14: ( INT )+ ( '.' ( INT )+ )?
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:381:14: ( INT )+
					int cnt6=0;
					loop6:
					while (true) {
						int alt6=2;
						int LA6_0 = input.LA(1);
						if ( ((LA6_0 >= '0' && LA6_0 <= '9')) ) {
							alt6=1;
						}

						switch (alt6) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt6 >= 1 ) break loop6;
							EarlyExitException eee = new EarlyExitException(6, input);
							throw eee;
						}
						cnt6++;
					}

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:381:19: ( '.' ( INT )+ )?
					int alt8=2;
					int LA8_0 = input.LA(1);
					if ( (LA8_0=='.') ) {
						alt8=1;
					}
					switch (alt8) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:381:20: '.' ( INT )+
							{
							match('.'); 
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:381:24: ( INT )+
							int cnt7=0;
							loop7:
							while (true) {
								int alt7=2;
								int LA7_0 = input.LA(1);
								if ( ((LA7_0 >= '0' && LA7_0 <= '9')) ) {
									alt7=1;
								}

								switch (alt7) {
								case 1 :
									// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
									{
									if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
										input.consume();
									}
									else {
										MismatchedSetException mse = new MismatchedSetException(null,input);
										recover(mse);
										throw mse;
									}
									}
									break;

								default :
									if ( cnt7 >= 1 ) break loop7;
									EarlyExitException eee = new EarlyExitException(7, input);
									throw eee;
								}
								cnt7++;
							}

							}
							break;

					}

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TILDE"

	// $ANTLR start "DQUOTE"
	public final void mDQUOTE() throws RecognitionException {
		try {
			int _type = DQUOTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:384:2: ( '\\\"' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:384:4: '\\\"'
			{
			match('\"'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DQUOTE"

	// $ANTLR start "SQUOTE"
	public final void mSQUOTE() throws RecognitionException {
		try {
			int _type = SQUOTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:387:2: ( '\\'' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:387:4: '\\''
			{
			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SQUOTE"

	// $ANTLR start "TO"
	public final void mTO() throws RecognitionException {
		try {
			int _type = TO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:392:4: ( 'TO' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:392:6: 'TO'
			{
			match("TO"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TO"

	// $ANTLR start "AND"
	public final void mAND() throws RecognitionException {
		try {
			int _type = AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0=='A'||LA11_0=='a') ) {
				alt11=1;
			}
			else if ( (LA11_0=='&') ) {
				alt11=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 11, 0, input);
				throw nvae;
			}

			switch (alt11) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
					{
					if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:48: ( AMPER ( AMPER )? )
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:48: ( AMPER ( AMPER )? )
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:49: AMPER ( AMPER )?
					{
					mAMPER(); 

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:55: ( AMPER )?
					int alt10=2;
					int LA10_0 = input.LA(1);
					if ( (LA10_0=='&') ) {
						alt10=1;
					}
					switch (alt10) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
							{
							if ( input.LA(1)=='&' ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

					}

					}

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AND"

	// $ANTLR start "OR"
	public final void mOR() throws RecognitionException {
		try {
			int _type = OR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:396:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:396:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:396:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0=='O'||LA13_0=='o') ) {
				alt13=1;
			}
			else if ( (LA13_0=='|') ) {
				alt13=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}

			switch (alt13) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:396:8: ( 'o' | 'O' ) ( 'r' | 'R' )
					{
					if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:396:34: ( VBAR ( VBAR )? )
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:396:34: ( VBAR ( VBAR )? )
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:396:35: VBAR ( VBAR )?
					{
					mVBAR(); 

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:396:40: ( VBAR )?
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0=='|') ) {
						alt12=1;
					}
					switch (alt12) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
							{
							if ( input.LA(1)=='|' ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

					}

					}

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OR"

	// $ANTLR start "NOT"
	public final void mNOT() throws RecognitionException {
		try {
			int _type = NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:397:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:397:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
			{
			if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:400:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:400:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )
			{
			if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' '||input.LA(1)=='\u3000' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			_channel=HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:421:13: ( '0' .. '9' )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "ESC_CHAR"
	public final void mESC_CHAR() throws RecognitionException {
		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:424:18: ( '\\\\' . )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:424:21: '\\\\' .
			{
			match('\\'); 
			matchAny(); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ESC_CHAR"

	// $ANTLR start "TERM_START_CHAR"
	public final void mTERM_START_CHAR() throws RecognitionException {
		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:428:2: ( (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' ) | ESC_CHAR ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:429:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' ) | ESC_CHAR )
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:429:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' ) | ESC_CHAR )
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( ((LA14_0 >= '\u0000' && LA14_0 <= '\b')||(LA14_0 >= '\u000B' && LA14_0 <= '\f')||(LA14_0 >= '\u000E' && LA14_0 <= '\u001F')||(LA14_0 >= '#' && LA14_0 <= '&')||LA14_0==','||(LA14_0 >= '.' && LA14_0 <= '9')||(LA14_0 >= ';' && LA14_0 <= '>')||(LA14_0 >= '@' && LA14_0 <= 'Z')||(LA14_0 >= '_' && LA14_0 <= 'z')||LA14_0=='|'||(LA14_0 >= '\u007F' && LA14_0 <= '\u2FFF')||(LA14_0 >= '\u3001' && LA14_0 <= '\uFFFF')) ) {
				alt14=1;
			}
			else if ( (LA14_0=='\\') ) {
				alt14=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 14, 0, input);
				throw nvae;
			}

			switch (alt14) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:429:3: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\u001F')||(input.LA(1) >= '#' && input.LA(1) <= '&')||input.LA(1)==','||(input.LA(1) >= '.' && input.LA(1) <= '9')||(input.LA(1) >= ';' && input.LA(1) <= '>')||(input.LA(1) >= '@' && input.LA(1) <= 'Z')||(input.LA(1) >= '_' && input.LA(1) <= 'z')||input.LA(1)=='|'||(input.LA(1) >= '\u007F' && input.LA(1) <= '\u2FFF')||(input.LA(1) >= '\u3001' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:435:5: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TERM_START_CHAR"

	// $ANTLR start "TERM_CHAR"
	public final void mTERM_CHAR() throws RecognitionException {
		try {
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:439:2: ( ( TERM_START_CHAR | '-' | '+' ) )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:440:2: ( TERM_START_CHAR | '-' | '+' )
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:440:2: ( TERM_START_CHAR | '-' | '+' )
			int alt15=3;
			int LA15_0 = input.LA(1);
			if ( ((LA15_0 >= '\u0000' && LA15_0 <= '\b')||(LA15_0 >= '\u000B' && LA15_0 <= '\f')||(LA15_0 >= '\u000E' && LA15_0 <= '\u001F')||(LA15_0 >= '#' && LA15_0 <= '&')||LA15_0==','||(LA15_0 >= '.' && LA15_0 <= '9')||(LA15_0 >= ';' && LA15_0 <= '>')||(LA15_0 >= '@' && LA15_0 <= 'Z')||LA15_0=='\\'||(LA15_0 >= '_' && LA15_0 <= 'z')||LA15_0=='|'||(LA15_0 >= '\u007F' && LA15_0 <= '\u2FFF')||(LA15_0 >= '\u3001' && LA15_0 <= '\uFFFF')) ) {
				alt15=1;
			}
			else if ( (LA15_0=='-') ) {
				alt15=2;
			}
			else if ( (LA15_0=='+') ) {
				alt15=3;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}

			switch (alt15) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:440:3: TERM_START_CHAR
					{
					mTERM_START_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:440:21: '-'
					{
					match('-'); 
					}
					break;
				case 3 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:440:27: '+'
					{
					match('+'); 
					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TERM_CHAR"

	// $ANTLR start "NUMBER"
	public final void mNUMBER() throws RecognitionException {
		try {
			int _type = NUMBER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:445:2: ( ( INT )+ ( '.' ( INT )+ )? )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:446:2: ( INT )+ ( '.' ( INT )+ )?
			{
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:446:2: ( INT )+
			int cnt16=0;
			loop16:
			while (true) {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( ((LA16_0 >= '0' && LA16_0 <= '9')) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt16 >= 1 ) break loop16;
					EarlyExitException eee = new EarlyExitException(16, input);
					throw eee;
				}
				cnt16++;
			}

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:446:7: ( '.' ( INT )+ )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0=='.') ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:446:8: '.' ( INT )+
					{
					match('.'); 
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:446:12: ( INT )+
					int cnt17=0;
					loop17:
					while (true) {
						int alt17=2;
						int LA17_0 = input.LA(1);
						if ( ((LA17_0 >= '0' && LA17_0 <= '9')) ) {
							alt17=1;
						}

						switch (alt17) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt17 >= 1 ) break loop17;
							EarlyExitException eee = new EarlyExitException(17, input);
							throw eee;
						}
						cnt17++;
					}

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NUMBER"

	// $ANTLR start "DATE_TOKEN"
	public final void mDATE_TOKEN() throws RecognitionException {
		try {
			int _type = DATE_TOKEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:450:2: ( INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )? )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:451:2: INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )?
			{
			mINT(); 

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:451:6: ( INT )?
			int alt19=2;
			int LA19_0 = input.LA(1);
			if ( ((LA19_0 >= '0' && LA19_0 <= '9')) ) {
				alt19=1;
			}
			switch (alt19) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			if ( (input.LA(1) >= '-' && input.LA(1) <= '/') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			mINT(); 

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:451:29: ( INT )?
			int alt20=2;
			int LA20_0 = input.LA(1);
			if ( ((LA20_0 >= '0' && LA20_0 <= '9')) ) {
				alt20=1;
			}
			switch (alt20) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			if ( (input.LA(1) >= '-' && input.LA(1) <= '/') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			mINT(); 

			mINT(); 

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:451:56: ( INT INT )?
			int alt21=2;
			int LA21_0 = input.LA(1);
			if ( ((LA21_0 >= '0' && LA21_0 <= '9')) ) {
				alt21=1;
			}
			switch (alt21) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:451:57: INT INT
					{
					mINT(); 

					mINT(); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DATE_TOKEN"

	// $ANTLR start "TERM_NORMAL"
	public final void mTERM_NORMAL() throws RecognitionException {
		try {
			int _type = TERM_NORMAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:455:2: ( TERM_START_CHAR ( TERM_CHAR )* )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:456:2: TERM_START_CHAR ( TERM_CHAR )*
			{
			mTERM_START_CHAR(); 

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:456:18: ( TERM_CHAR )*
			loop22:
			while (true) {
				int alt22=2;
				int LA22_0 = input.LA(1);
				if ( ((LA22_0 >= '\u0000' && LA22_0 <= '\b')||(LA22_0 >= '\u000B' && LA22_0 <= '\f')||(LA22_0 >= '\u000E' && LA22_0 <= '\u001F')||(LA22_0 >= '#' && LA22_0 <= '&')||(LA22_0 >= '+' && LA22_0 <= '9')||(LA22_0 >= ';' && LA22_0 <= '>')||(LA22_0 >= '@' && LA22_0 <= 'Z')||LA22_0=='\\'||(LA22_0 >= '_' && LA22_0 <= 'z')||LA22_0=='|'||(LA22_0 >= '\u007F' && LA22_0 <= '\u2FFF')||(LA22_0 >= '\u3001' && LA22_0 <= '\uFFFF')) ) {
					alt22=1;
				}

				switch (alt22) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:456:20: TERM_CHAR
					{
					mTERM_CHAR(); 

					}
					break;

				default :
					break loop22;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TERM_NORMAL"

	// $ANTLR start "TERM_TRUNCATED"
	public final void mTERM_TRUNCATED() throws RecognitionException {
		try {
			int _type = TERM_TRUNCATED;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:460:15: ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ )
			int alt34=3;
			alt34 = dfa34.predict(input);
			switch (alt34) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:2: ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )*
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:2: ( STAR | QMARK )
					int alt23=2;
					int LA23_0 = input.LA(1);
					if ( (LA23_0=='*') ) {
						alt23=1;
					}
					else if ( (LA23_0=='?') ) {
						alt23=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 23, 0, input);
						throw nvae;
					}

					switch (alt23) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:3: STAR
							{
							mSTAR(); 

							}
							break;
						case 2 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:8: QMARK
							{
							mQMARK(); 

							}
							break;

					}

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+
					int cnt26=0;
					loop26:
					while (true) {
						int alt26=2;
						alt26 = dfa26.predict(input);
						switch (alt26) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:16: ( TERM_CHAR )+ ( QMARK | STAR )
							{
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:16: ( TERM_CHAR )+
							int cnt24=0;
							loop24:
							while (true) {
								int alt24=2;
								int LA24_0 = input.LA(1);
								if ( ((LA24_0 >= '\u0000' && LA24_0 <= '\b')||(LA24_0 >= '\u000B' && LA24_0 <= '\f')||(LA24_0 >= '\u000E' && LA24_0 <= '\u001F')||(LA24_0 >= '#' && LA24_0 <= '&')||(LA24_0 >= '+' && LA24_0 <= '9')||(LA24_0 >= ';' && LA24_0 <= '>')||(LA24_0 >= '@' && LA24_0 <= 'Z')||LA24_0=='\\'||(LA24_0 >= '_' && LA24_0 <= 'z')||LA24_0=='|'||(LA24_0 >= '\u007F' && LA24_0 <= '\u2FFF')||(LA24_0 >= '\u3001' && LA24_0 <= '\uFFFF')) ) {
									alt24=1;
								}

								switch (alt24) {
								case 1 :
									// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:16: TERM_CHAR
									{
									mTERM_CHAR(); 

									}
									break;

								default :
									if ( cnt24 >= 1 ) break loop24;
									EarlyExitException eee = new EarlyExitException(24, input);
									throw eee;
								}
								cnt24++;
							}

							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:27: ( QMARK | STAR )
							int alt25=2;
							int LA25_0 = input.LA(1);
							if ( (LA25_0=='?') ) {
								alt25=1;
							}
							else if ( (LA25_0=='*') ) {
								alt25=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 25, 0, input);
								throw nvae;
							}

							switch (alt25) {
								case 1 :
									// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:28: QMARK
									{
									mQMARK(); 

									}
									break;
								case 2 :
									// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:34: STAR
									{
									mSTAR(); 

									}
									break;

							}

							}
							break;

						default :
							if ( cnt26 >= 1 ) break loop26;
							EarlyExitException eee = new EarlyExitException(26, input);
							throw eee;
						}
						cnt26++;
					}

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:42: ( TERM_CHAR )*
					loop27:
					while (true) {
						int alt27=2;
						int LA27_0 = input.LA(1);
						if ( ((LA27_0 >= '\u0000' && LA27_0 <= '\b')||(LA27_0 >= '\u000B' && LA27_0 <= '\f')||(LA27_0 >= '\u000E' && LA27_0 <= '\u001F')||(LA27_0 >= '#' && LA27_0 <= '&')||(LA27_0 >= '+' && LA27_0 <= '9')||(LA27_0 >= ';' && LA27_0 <= '>')||(LA27_0 >= '@' && LA27_0 <= 'Z')||LA27_0=='\\'||(LA27_0 >= '_' && LA27_0 <= 'z')||LA27_0=='|'||(LA27_0 >= '\u007F' && LA27_0 <= '\u2FFF')||(LA27_0 >= '\u3001' && LA27_0 <= '\uFFFF')) ) {
							alt27=1;
						}

						switch (alt27) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:461:43: TERM_CHAR
							{
							mTERM_CHAR(); 

							}
							break;

						default :
							break loop27;
						}
					}

					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:4: TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )*
					{
					mTERM_START_CHAR(); 

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+
					int cnt30=0;
					loop30:
					while (true) {
						int alt30=2;
						alt30 = dfa30.predict(input);
						switch (alt30) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:21: ( TERM_CHAR )* ( QMARK | STAR )
							{
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:21: ( TERM_CHAR )*
							loop28:
							while (true) {
								int alt28=2;
								int LA28_0 = input.LA(1);
								if ( ((LA28_0 >= '\u0000' && LA28_0 <= '\b')||(LA28_0 >= '\u000B' && LA28_0 <= '\f')||(LA28_0 >= '\u000E' && LA28_0 <= '\u001F')||(LA28_0 >= '#' && LA28_0 <= '&')||(LA28_0 >= '+' && LA28_0 <= '9')||(LA28_0 >= ';' && LA28_0 <= '>')||(LA28_0 >= '@' && LA28_0 <= 'Z')||LA28_0=='\\'||(LA28_0 >= '_' && LA28_0 <= 'z')||LA28_0=='|'||(LA28_0 >= '\u007F' && LA28_0 <= '\u2FFF')||(LA28_0 >= '\u3001' && LA28_0 <= '\uFFFF')) ) {
									alt28=1;
								}

								switch (alt28) {
								case 1 :
									// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:21: TERM_CHAR
									{
									mTERM_CHAR(); 

									}
									break;

								default :
									break loop28;
								}
							}

							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:32: ( QMARK | STAR )
							int alt29=2;
							int LA29_0 = input.LA(1);
							if ( (LA29_0=='?') ) {
								alt29=1;
							}
							else if ( (LA29_0=='*') ) {
								alt29=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 29, 0, input);
								throw nvae;
							}

							switch (alt29) {
								case 1 :
									// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:33: QMARK
									{
									mQMARK(); 

									}
									break;
								case 2 :
									// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:39: STAR
									{
									mSTAR(); 

									}
									break;

							}

							}
							break;

						default :
							if ( cnt30 >= 1 ) break loop30;
							EarlyExitException eee = new EarlyExitException(30, input);
							throw eee;
						}
						cnt30++;
					}

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:47: ( TERM_CHAR )*
					loop31:
					while (true) {
						int alt31=2;
						int LA31_0 = input.LA(1);
						if ( ((LA31_0 >= '\u0000' && LA31_0 <= '\b')||(LA31_0 >= '\u000B' && LA31_0 <= '\f')||(LA31_0 >= '\u000E' && LA31_0 <= '\u001F')||(LA31_0 >= '#' && LA31_0 <= '&')||(LA31_0 >= '+' && LA31_0 <= '9')||(LA31_0 >= ';' && LA31_0 <= '>')||(LA31_0 >= '@' && LA31_0 <= 'Z')||LA31_0=='\\'||(LA31_0 >= '_' && LA31_0 <= 'z')||LA31_0=='|'||(LA31_0 >= '\u007F' && LA31_0 <= '\u2FFF')||(LA31_0 >= '\u3001' && LA31_0 <= '\uFFFF')) ) {
							alt31=1;
						}

						switch (alt31) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:462:48: TERM_CHAR
							{
							mTERM_CHAR(); 

							}
							break;

						default :
							break loop31;
						}
					}

					}
					break;
				case 3 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:463:4: ( STAR | QMARK ) ( TERM_CHAR )+
					{
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:463:4: ( STAR | QMARK )
					int alt32=2;
					int LA32_0 = input.LA(1);
					if ( (LA32_0=='*') ) {
						alt32=1;
					}
					else if ( (LA32_0=='?') ) {
						alt32=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 32, 0, input);
						throw nvae;
					}

					switch (alt32) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:463:5: STAR
							{
							mSTAR(); 

							}
							break;
						case 2 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:463:10: QMARK
							{
							mQMARK(); 

							}
							break;

					}

					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:463:17: ( TERM_CHAR )+
					int cnt33=0;
					loop33:
					while (true) {
						int alt33=2;
						int LA33_0 = input.LA(1);
						if ( ((LA33_0 >= '\u0000' && LA33_0 <= '\b')||(LA33_0 >= '\u000B' && LA33_0 <= '\f')||(LA33_0 >= '\u000E' && LA33_0 <= '\u001F')||(LA33_0 >= '#' && LA33_0 <= '&')||(LA33_0 >= '+' && LA33_0 <= '9')||(LA33_0 >= ';' && LA33_0 <= '>')||(LA33_0 >= '@' && LA33_0 <= 'Z')||LA33_0=='\\'||(LA33_0 >= '_' && LA33_0 <= 'z')||LA33_0=='|'||(LA33_0 >= '\u007F' && LA33_0 <= '\u2FFF')||(LA33_0 >= '\u3001' && LA33_0 <= '\uFFFF')) ) {
							alt33=1;
						}

						switch (alt33) {
						case 1 :
							// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:463:17: TERM_CHAR
							{
							mTERM_CHAR(); 

							}
							break;

						default :
							if ( cnt33 >= 1 ) break loop33;
							EarlyExitException eee = new EarlyExitException(33, input);
							throw eee;
						}
						cnt33++;
					}

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TERM_TRUNCATED"

	// $ANTLR start "PHRASE"
	public final void mPHRASE() throws RecognitionException {
		try {
			int _type = PHRASE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:468:2: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:469:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE
			{
			mDQUOTE(); 

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:469:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+
			int cnt35=0;
			loop35:
			while (true) {
				int alt35=3;
				int LA35_0 = input.LA(1);
				if ( (LA35_0=='\\') ) {
					alt35=1;
				}
				else if ( ((LA35_0 >= '\u0000' && LA35_0 <= '!')||(LA35_0 >= '#' && LA35_0 <= ')')||(LA35_0 >= '+' && LA35_0 <= '>')||(LA35_0 >= '@' && LA35_0 <= '[')||(LA35_0 >= ']' && LA35_0 <= '\uFFFF')) ) {
					alt35=2;
				}

				switch (alt35) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:469:10: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:469:19: ~ ( '\\\"' | '\\\\' | '?' | '*' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= ')')||(input.LA(1) >= '+' && input.LA(1) <= '>')||(input.LA(1) >= '@' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt35 >= 1 ) break loop35;
					EarlyExitException eee = new EarlyExitException(35, input);
					throw eee;
				}
				cnt35++;
			}

			mDQUOTE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PHRASE"

	// $ANTLR start "PHRASE_ANYTHING"
	public final void mPHRASE_ANYTHING() throws RecognitionException {
		try {
			int _type = PHRASE_ANYTHING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:472:17: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE )
			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:473:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE
			{
			mDQUOTE(); 

			// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:473:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+
			int cnt36=0;
			loop36:
			while (true) {
				int alt36=3;
				int LA36_0 = input.LA(1);
				if ( (LA36_0=='\\') ) {
					alt36=1;
				}
				else if ( ((LA36_0 >= '\u0000' && LA36_0 <= '!')||(LA36_0 >= '#' && LA36_0 <= '[')||(LA36_0 >= ']' && LA36_0 <= '\uFFFF')) ) {
					alt36=2;
				}

				switch (alt36) {
				case 1 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:473:10: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:473:19: ~ ( '\\\"' | '\\\\' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt36 >= 1 ) break loop36;
					EarlyExitException eee = new EarlyExitException(36, input);
					throw eee;
				}
				cnt36++;
			}

			mDQUOTE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PHRASE_ANYTHING"

	@Override
	public void mTokens() throws RecognitionException {
		// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:8: ( LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | WS | NUMBER | DATE_TOKEN | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING )
		int alt37=26;
		alt37 = dfa37.predict(input);
		switch (alt37) {
			case 1 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:10: LPAREN
				{
				mLPAREN(); 

				}
				break;
			case 2 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:17: RPAREN
				{
				mRPAREN(); 

				}
				break;
			case 3 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:24: LBRACK
				{
				mLBRACK(); 

				}
				break;
			case 4 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:31: RBRACK
				{
				mRBRACK(); 

				}
				break;
			case 5 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:38: COLON
				{
				mCOLON(); 

				}
				break;
			case 6 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:44: PLUS
				{
				mPLUS(); 

				}
				break;
			case 7 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:49: MINUS
				{
				mMINUS(); 

				}
				break;
			case 8 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:55: STAR
				{
				mSTAR(); 

				}
				break;
			case 9 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:60: QMARK
				{
				mQMARK(); 

				}
				break;
			case 10 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:66: LCURLY
				{
				mLCURLY(); 

				}
				break;
			case 11 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:73: RCURLY
				{
				mRCURLY(); 

				}
				break;
			case 12 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:80: CARAT
				{
				mCARAT(); 

				}
				break;
			case 13 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:86: TILDE
				{
				mTILDE(); 

				}
				break;
			case 14 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:92: DQUOTE
				{
				mDQUOTE(); 

				}
				break;
			case 15 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:99: SQUOTE
				{
				mSQUOTE(); 

				}
				break;
			case 16 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:106: TO
				{
				mTO(); 

				}
				break;
			case 17 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:109: AND
				{
				mAND(); 

				}
				break;
			case 18 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:113: OR
				{
				mOR(); 

				}
				break;
			case 19 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:116: NOT
				{
				mNOT(); 

				}
				break;
			case 20 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:120: WS
				{
				mWS(); 

				}
				break;
			case 21 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:123: NUMBER
				{
				mNUMBER(); 

				}
				break;
			case 22 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:130: DATE_TOKEN
				{
				mDATE_TOKEN(); 

				}
				break;
			case 23 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:141: TERM_NORMAL
				{
				mTERM_NORMAL(); 

				}
				break;
			case 24 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:153: TERM_TRUNCATED
				{
				mTERM_TRUNCATED(); 

				}
				break;
			case 25 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:168: PHRASE
				{
				mPHRASE(); 

				}
				break;
			case 26 :
				// /dvt/workspace2/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:175: PHRASE_ANYTHING
				{
				mPHRASE_ANYTHING(); 

				}
				break;

		}
	}


	protected DFA34 dfa34 = new DFA34(this);
	protected DFA26 dfa26 = new DFA26(this);
	protected DFA30 dfa30 = new DFA30(this);
	protected DFA37 dfa37 = new DFA37(this);
	static final String DFA34_eotS =
		"\4\uffff\1\10\1\uffff\2\10\2\uffff\1\10";
	static final String DFA34_eofS =
		"\13\uffff";
	static final String DFA34_minS =
		"\3\0\1\uffff\4\0\2\uffff\1\0";
	static final String DFA34_maxS =
		"\3\uffff\1\uffff\4\uffff\2\uffff\1\uffff";
	static final String DFA34_acceptS =
		"\3\uffff\1\2\4\uffff\1\3\1\1\1\uffff";
	static final String DFA34_specialS =
		"\1\1\1\4\1\3\1\uffff\1\2\1\5\1\6\1\0\2\uffff\1\7}>";
	static final String[] DFA34_transitionS = {
			"\11\3\2\uffff\2\3\1\uffff\22\3\3\uffff\4\3\3\uffff\1\1\1\uffff\1\3\1"+
			"\uffff\14\3\1\uffff\4\3\1\2\33\3\1\uffff\1\3\2\uffff\34\3\1\uffff\1\3"+
			"\2\uffff\u2f81\3\1\uffff\ucfff\3",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\4\uffff\1\7\1\4\1\6\14\4"+
			"\1\uffff\4\4\1\uffff\33\4\1\uffff\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff"+
			"\u2f81\4\1\uffff\ucfff\4",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\4\uffff\1\7\1\4\1\6\14\4"+
			"\1\uffff\4\4\1\2\33\4\1\uffff\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff\u2f81"+
			"\4\1\uffff\ucfff\4",
			"",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\3\uffff\1\11\1\7\1\4\1\6"+
			"\14\4\1\uffff\4\4\1\11\33\4\1\uffff\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff"+
			"\u2f81\4\1\uffff\ucfff\4",
			"\0\12",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\3\uffff\1\11\1\7\1\4\1\6"+
			"\14\4\1\uffff\4\4\1\11\33\4\1\uffff\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff"+
			"\u2f81\4\1\uffff\ucfff\4",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\3\uffff\1\11\1\7\1\4\1\6"+
			"\14\4\1\uffff\4\4\1\11\33\4\1\uffff\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff"+
			"\u2f81\4\1\uffff\ucfff\4",
			"",
			"",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\3\uffff\1\11\1\7\1\4\1\6"+
			"\14\4\1\uffff\4\4\1\11\33\4\1\uffff\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff"+
			"\u2f81\4\1\uffff\ucfff\4"
	};

	static final short[] DFA34_eot = DFA.unpackEncodedString(DFA34_eotS);
	static final short[] DFA34_eof = DFA.unpackEncodedString(DFA34_eofS);
	static final char[] DFA34_min = DFA.unpackEncodedStringToUnsignedChars(DFA34_minS);
	static final char[] DFA34_max = DFA.unpackEncodedStringToUnsignedChars(DFA34_maxS);
	static final short[] DFA34_accept = DFA.unpackEncodedString(DFA34_acceptS);
	static final short[] DFA34_special = DFA.unpackEncodedString(DFA34_specialS);
	static final short[][] DFA34_transition;

	static {
		int numStates = DFA34_transitionS.length;
		DFA34_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA34_transition[i] = DFA.unpackEncodedString(DFA34_transitionS[i]);
		}
	}

	protected class DFA34 extends DFA {

		public DFA34(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 34;
			this.eot = DFA34_eot;
			this.eof = DFA34_eof;
			this.min = DFA34_min;
			this.max = DFA34_max;
			this.accept = DFA34_accept;
			this.special = DFA34_special;
			this.transition = DFA34_transition;
		}
		@Override
		public String getDescription() {
			return "460:1: TERM_TRUNCATED : ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA34_7 = input.LA(1);
						s = -1;
						if ( (LA34_7=='*'||LA34_7=='?') ) {s = 9;}
						else if ( ((LA34_7 >= '\u0000' && LA34_7 <= '\b')||(LA34_7 >= '\u000B' && LA34_7 <= '\f')||(LA34_7 >= '\u000E' && LA34_7 <= '\u001F')||(LA34_7 >= '#' && LA34_7 <= '&')||LA34_7==','||(LA34_7 >= '.' && LA34_7 <= '9')||(LA34_7 >= ';' && LA34_7 <= '>')||(LA34_7 >= '@' && LA34_7 <= 'Z')||(LA34_7 >= '_' && LA34_7 <= 'z')||LA34_7=='|'||(LA34_7 >= '\u007F' && LA34_7 <= '\u2FFF')||(LA34_7 >= '\u3001' && LA34_7 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA34_7=='\\') ) {s = 5;}
						else if ( (LA34_7=='-') ) {s = 6;}
						else if ( (LA34_7=='+') ) {s = 7;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA34_0 = input.LA(1);
						s = -1;
						if ( (LA34_0=='*') ) {s = 1;}
						else if ( (LA34_0=='?') ) {s = 2;}
						else if ( ((LA34_0 >= '\u0000' && LA34_0 <= '\b')||(LA34_0 >= '\u000B' && LA34_0 <= '\f')||(LA34_0 >= '\u000E' && LA34_0 <= '\u001F')||(LA34_0 >= '#' && LA34_0 <= '&')||LA34_0==','||(LA34_0 >= '.' && LA34_0 <= '9')||(LA34_0 >= ';' && LA34_0 <= '>')||(LA34_0 >= '@' && LA34_0 <= 'Z')||LA34_0=='\\'||(LA34_0 >= '_' && LA34_0 <= 'z')||LA34_0=='|'||(LA34_0 >= '\u007F' && LA34_0 <= '\u2FFF')||(LA34_0 >= '\u3001' && LA34_0 <= '\uFFFF')) ) {s = 3;}
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA34_4 = input.LA(1);
						s = -1;
						if ( (LA34_4=='*'||LA34_4=='?') ) {s = 9;}
						else if ( ((LA34_4 >= '\u0000' && LA34_4 <= '\b')||(LA34_4 >= '\u000B' && LA34_4 <= '\f')||(LA34_4 >= '\u000E' && LA34_4 <= '\u001F')||(LA34_4 >= '#' && LA34_4 <= '&')||LA34_4==','||(LA34_4 >= '.' && LA34_4 <= '9')||(LA34_4 >= ';' && LA34_4 <= '>')||(LA34_4 >= '@' && LA34_4 <= 'Z')||(LA34_4 >= '_' && LA34_4 <= 'z')||LA34_4=='|'||(LA34_4 >= '\u007F' && LA34_4 <= '\u2FFF')||(LA34_4 >= '\u3001' && LA34_4 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA34_4=='\\') ) {s = 5;}
						else if ( (LA34_4=='-') ) {s = 6;}
						else if ( (LA34_4=='+') ) {s = 7;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA34_2 = input.LA(1);
						s = -1;
						if ( ((LA34_2 >= '\u0000' && LA34_2 <= '\b')||(LA34_2 >= '\u000B' && LA34_2 <= '\f')||(LA34_2 >= '\u000E' && LA34_2 <= '\u001F')||(LA34_2 >= '#' && LA34_2 <= '&')||LA34_2==','||(LA34_2 >= '.' && LA34_2 <= '9')||(LA34_2 >= ';' && LA34_2 <= '>')||(LA34_2 >= '@' && LA34_2 <= 'Z')||(LA34_2 >= '_' && LA34_2 <= 'z')||LA34_2=='|'||(LA34_2 >= '\u007F' && LA34_2 <= '\u2FFF')||(LA34_2 >= '\u3001' && LA34_2 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA34_2=='\\') ) {s = 5;}
						else if ( (LA34_2=='-') ) {s = 6;}
						else if ( (LA34_2=='+') ) {s = 7;}
						else if ( (LA34_2=='?') ) {s = 2;}
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA34_1 = input.LA(1);
						s = -1;
						if ( ((LA34_1 >= '\u0000' && LA34_1 <= '\b')||(LA34_1 >= '\u000B' && LA34_1 <= '\f')||(LA34_1 >= '\u000E' && LA34_1 <= '\u001F')||(LA34_1 >= '#' && LA34_1 <= '&')||LA34_1==','||(LA34_1 >= '.' && LA34_1 <= '9')||(LA34_1 >= ';' && LA34_1 <= '>')||(LA34_1 >= '@' && LA34_1 <= 'Z')||(LA34_1 >= '_' && LA34_1 <= 'z')||LA34_1=='|'||(LA34_1 >= '\u007F' && LA34_1 <= '\u2FFF')||(LA34_1 >= '\u3001' && LA34_1 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA34_1=='\\') ) {s = 5;}
						else if ( (LA34_1=='-') ) {s = 6;}
						else if ( (LA34_1=='+') ) {s = 7;}
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA34_5 = input.LA(1);
						s = -1;
						if ( ((LA34_5 >= '\u0000' && LA34_5 <= '\uFFFF')) ) {s = 10;}
						if ( s>=0 ) return s;
						break;

					case 6 : 
						int LA34_6 = input.LA(1);
						s = -1;
						if ( (LA34_6=='*'||LA34_6=='?') ) {s = 9;}
						else if ( ((LA34_6 >= '\u0000' && LA34_6 <= '\b')||(LA34_6 >= '\u000B' && LA34_6 <= '\f')||(LA34_6 >= '\u000E' && LA34_6 <= '\u001F')||(LA34_6 >= '#' && LA34_6 <= '&')||LA34_6==','||(LA34_6 >= '.' && LA34_6 <= '9')||(LA34_6 >= ';' && LA34_6 <= '>')||(LA34_6 >= '@' && LA34_6 <= 'Z')||(LA34_6 >= '_' && LA34_6 <= 'z')||LA34_6=='|'||(LA34_6 >= '\u007F' && LA34_6 <= '\u2FFF')||(LA34_6 >= '\u3001' && LA34_6 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA34_6=='\\') ) {s = 5;}
						else if ( (LA34_6=='-') ) {s = 6;}
						else if ( (LA34_6=='+') ) {s = 7;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 7 : 
						int LA34_10 = input.LA(1);
						s = -1;
						if ( (LA34_10=='*'||LA34_10=='?') ) {s = 9;}
						else if ( ((LA34_10 >= '\u0000' && LA34_10 <= '\b')||(LA34_10 >= '\u000B' && LA34_10 <= '\f')||(LA34_10 >= '\u000E' && LA34_10 <= '\u001F')||(LA34_10 >= '#' && LA34_10 <= '&')||LA34_10==','||(LA34_10 >= '.' && LA34_10 <= '9')||(LA34_10 >= ';' && LA34_10 <= '>')||(LA34_10 >= '@' && LA34_10 <= 'Z')||(LA34_10 >= '_' && LA34_10 <= 'z')||LA34_10=='|'||(LA34_10 >= '\u007F' && LA34_10 <= '\u2FFF')||(LA34_10 >= '\u3001' && LA34_10 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA34_10=='\\') ) {s = 5;}
						else if ( (LA34_10=='-') ) {s = 6;}
						else if ( (LA34_10=='+') ) {s = 7;}
						else s = 8;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 34, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA26_eotS =
		"\2\5\1\uffff\2\5\2\uffff\1\5";
	static final String DFA26_eofS =
		"\10\uffff";
	static final String DFA26_minS =
		"\5\0\2\uffff\1\0";
	static final String DFA26_maxS =
		"\5\uffff\2\uffff\1\uffff";
	static final String DFA26_acceptS =
		"\5\uffff\1\2\1\1\1\uffff";
	static final String DFA26_specialS =
		"\1\0\1\2\1\3\1\5\1\1\2\uffff\1\4}>";
	static final String[] DFA26_transitionS = {
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\4\uffff\1\4\1\1\1\3\14\1\1"+
			"\uffff\4\1\1\uffff\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1\1\1\3\14"+
			"\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1",
			"\0\7",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1\1\1\3\14"+
			"\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1\1\1\3\14"+
			"\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1",
			"",
			"",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1\1\1\3\14"+
			"\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1"
	};

	static final short[] DFA26_eot = DFA.unpackEncodedString(DFA26_eotS);
	static final short[] DFA26_eof = DFA.unpackEncodedString(DFA26_eofS);
	static final char[] DFA26_min = DFA.unpackEncodedStringToUnsignedChars(DFA26_minS);
	static final char[] DFA26_max = DFA.unpackEncodedStringToUnsignedChars(DFA26_maxS);
	static final short[] DFA26_accept = DFA.unpackEncodedString(DFA26_acceptS);
	static final short[] DFA26_special = DFA.unpackEncodedString(DFA26_specialS);
	static final short[][] DFA26_transition;

	static {
		int numStates = DFA26_transitionS.length;
		DFA26_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA26_transition[i] = DFA.unpackEncodedString(DFA26_transitionS[i]);
		}
	}

	protected class DFA26 extends DFA {

		public DFA26(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 26;
			this.eot = DFA26_eot;
			this.eof = DFA26_eof;
			this.min = DFA26_min;
			this.max = DFA26_max;
			this.accept = DFA26_accept;
			this.special = DFA26_special;
			this.transition = DFA26_transition;
		}
		@Override
		public String getDescription() {
			return "()+ loopback of 461:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA26_0 = input.LA(1);
						s = -1;
						if ( ((LA26_0 >= '\u0000' && LA26_0 <= '\b')||(LA26_0 >= '\u000B' && LA26_0 <= '\f')||(LA26_0 >= '\u000E' && LA26_0 <= '\u001F')||(LA26_0 >= '#' && LA26_0 <= '&')||LA26_0==','||(LA26_0 >= '.' && LA26_0 <= '9')||(LA26_0 >= ';' && LA26_0 <= '>')||(LA26_0 >= '@' && LA26_0 <= 'Z')||(LA26_0 >= '_' && LA26_0 <= 'z')||LA26_0=='|'||(LA26_0 >= '\u007F' && LA26_0 <= '\u2FFF')||(LA26_0 >= '\u3001' && LA26_0 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA26_0=='\\') ) {s = 2;}
						else if ( (LA26_0=='-') ) {s = 3;}
						else if ( (LA26_0=='+') ) {s = 4;}
						else s = 5;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA26_4 = input.LA(1);
						s = -1;
						if ( ((LA26_4 >= '\u0000' && LA26_4 <= '\b')||(LA26_4 >= '\u000B' && LA26_4 <= '\f')||(LA26_4 >= '\u000E' && LA26_4 <= '\u001F')||(LA26_4 >= '#' && LA26_4 <= '&')||LA26_4==','||(LA26_4 >= '.' && LA26_4 <= '9')||(LA26_4 >= ';' && LA26_4 <= '>')||(LA26_4 >= '@' && LA26_4 <= 'Z')||(LA26_4 >= '_' && LA26_4 <= 'z')||LA26_4=='|'||(LA26_4 >= '\u007F' && LA26_4 <= '\u2FFF')||(LA26_4 >= '\u3001' && LA26_4 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA26_4=='\\') ) {s = 2;}
						else if ( (LA26_4=='-') ) {s = 3;}
						else if ( (LA26_4=='+') ) {s = 4;}
						else if ( (LA26_4=='*'||LA26_4=='?') ) {s = 6;}
						else s = 5;
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA26_1 = input.LA(1);
						s = -1;
						if ( ((LA26_1 >= '\u0000' && LA26_1 <= '\b')||(LA26_1 >= '\u000B' && LA26_1 <= '\f')||(LA26_1 >= '\u000E' && LA26_1 <= '\u001F')||(LA26_1 >= '#' && LA26_1 <= '&')||LA26_1==','||(LA26_1 >= '.' && LA26_1 <= '9')||(LA26_1 >= ';' && LA26_1 <= '>')||(LA26_1 >= '@' && LA26_1 <= 'Z')||(LA26_1 >= '_' && LA26_1 <= 'z')||LA26_1=='|'||(LA26_1 >= '\u007F' && LA26_1 <= '\u2FFF')||(LA26_1 >= '\u3001' && LA26_1 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA26_1=='\\') ) {s = 2;}
						else if ( (LA26_1=='-') ) {s = 3;}
						else if ( (LA26_1=='+') ) {s = 4;}
						else if ( (LA26_1=='*'||LA26_1=='?') ) {s = 6;}
						else s = 5;
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA26_2 = input.LA(1);
						s = -1;
						if ( ((LA26_2 >= '\u0000' && LA26_2 <= '\uFFFF')) ) {s = 7;}
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA26_7 = input.LA(1);
						s = -1;
						if ( ((LA26_7 >= '\u0000' && LA26_7 <= '\b')||(LA26_7 >= '\u000B' && LA26_7 <= '\f')||(LA26_7 >= '\u000E' && LA26_7 <= '\u001F')||(LA26_7 >= '#' && LA26_7 <= '&')||LA26_7==','||(LA26_7 >= '.' && LA26_7 <= '9')||(LA26_7 >= ';' && LA26_7 <= '>')||(LA26_7 >= '@' && LA26_7 <= 'Z')||(LA26_7 >= '_' && LA26_7 <= 'z')||LA26_7=='|'||(LA26_7 >= '\u007F' && LA26_7 <= '\u2FFF')||(LA26_7 >= '\u3001' && LA26_7 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA26_7=='\\') ) {s = 2;}
						else if ( (LA26_7=='-') ) {s = 3;}
						else if ( (LA26_7=='+') ) {s = 4;}
						else if ( (LA26_7=='*'||LA26_7=='?') ) {s = 6;}
						else s = 5;
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA26_3 = input.LA(1);
						s = -1;
						if ( ((LA26_3 >= '\u0000' && LA26_3 <= '\b')||(LA26_3 >= '\u000B' && LA26_3 <= '\f')||(LA26_3 >= '\u000E' && LA26_3 <= '\u001F')||(LA26_3 >= '#' && LA26_3 <= '&')||LA26_3==','||(LA26_3 >= '.' && LA26_3 <= '9')||(LA26_3 >= ';' && LA26_3 <= '>')||(LA26_3 >= '@' && LA26_3 <= 'Z')||(LA26_3 >= '_' && LA26_3 <= 'z')||LA26_3=='|'||(LA26_3 >= '\u007F' && LA26_3 <= '\u2FFF')||(LA26_3 >= '\u3001' && LA26_3 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA26_3=='\\') ) {s = 2;}
						else if ( (LA26_3=='-') ) {s = 3;}
						else if ( (LA26_3=='+') ) {s = 4;}
						else if ( (LA26_3=='*'||LA26_3=='?') ) {s = 6;}
						else s = 5;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 26, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA30_eotS =
		"\2\5\1\uffff\2\5\2\uffff\1\5";
	static final String DFA30_eofS =
		"\10\uffff";
	static final String DFA30_minS =
		"\5\0\2\uffff\1\0";
	static final String DFA30_maxS =
		"\5\uffff\2\uffff\1\uffff";
	static final String DFA30_acceptS =
		"\5\uffff\1\2\1\1\1\uffff";
	static final String DFA30_specialS =
		"\1\2\1\1\1\4\1\5\1\0\2\uffff\1\3}>";
	static final String[] DFA30_transitionS = {
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1\1\1\3\14"+
			"\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1\1\1\3\14"+
			"\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1",
			"\0\7",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1\1\1\3\14"+
			"\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1\1\1\3\14"+
			"\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1",
			"",
			"",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1\1\1\3\14"+
			"\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff"+
			"\u2f81\1\1\uffff\ucfff\1"
	};

	static final short[] DFA30_eot = DFA.unpackEncodedString(DFA30_eotS);
	static final short[] DFA30_eof = DFA.unpackEncodedString(DFA30_eofS);
	static final char[] DFA30_min = DFA.unpackEncodedStringToUnsignedChars(DFA30_minS);
	static final char[] DFA30_max = DFA.unpackEncodedStringToUnsignedChars(DFA30_maxS);
	static final short[] DFA30_accept = DFA.unpackEncodedString(DFA30_acceptS);
	static final short[] DFA30_special = DFA.unpackEncodedString(DFA30_specialS);
	static final short[][] DFA30_transition;

	static {
		int numStates = DFA30_transitionS.length;
		DFA30_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA30_transition[i] = DFA.unpackEncodedString(DFA30_transitionS[i]);
		}
	}

	protected class DFA30 extends DFA {

		public DFA30(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 30;
			this.eot = DFA30_eot;
			this.eof = DFA30_eof;
			this.min = DFA30_min;
			this.max = DFA30_max;
			this.accept = DFA30_accept;
			this.special = DFA30_special;
			this.transition = DFA30_transition;
		}
		@Override
		public String getDescription() {
			return "()+ loopback of 462:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA30_4 = input.LA(1);
						s = -1;
						if ( ((LA30_4 >= '\u0000' && LA30_4 <= '\b')||(LA30_4 >= '\u000B' && LA30_4 <= '\f')||(LA30_4 >= '\u000E' && LA30_4 <= '\u001F')||(LA30_4 >= '#' && LA30_4 <= '&')||LA30_4==','||(LA30_4 >= '.' && LA30_4 <= '9')||(LA30_4 >= ';' && LA30_4 <= '>')||(LA30_4 >= '@' && LA30_4 <= 'Z')||(LA30_4 >= '_' && LA30_4 <= 'z')||LA30_4=='|'||(LA30_4 >= '\u007F' && LA30_4 <= '\u2FFF')||(LA30_4 >= '\u3001' && LA30_4 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA30_4=='\\') ) {s = 2;}
						else if ( (LA30_4=='-') ) {s = 3;}
						else if ( (LA30_4=='+') ) {s = 4;}
						else if ( (LA30_4=='*'||LA30_4=='?') ) {s = 6;}
						else s = 5;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA30_1 = input.LA(1);
						s = -1;
						if ( ((LA30_1 >= '\u0000' && LA30_1 <= '\b')||(LA30_1 >= '\u000B' && LA30_1 <= '\f')||(LA30_1 >= '\u000E' && LA30_1 <= '\u001F')||(LA30_1 >= '#' && LA30_1 <= '&')||LA30_1==','||(LA30_1 >= '.' && LA30_1 <= '9')||(LA30_1 >= ';' && LA30_1 <= '>')||(LA30_1 >= '@' && LA30_1 <= 'Z')||(LA30_1 >= '_' && LA30_1 <= 'z')||LA30_1=='|'||(LA30_1 >= '\u007F' && LA30_1 <= '\u2FFF')||(LA30_1 >= '\u3001' && LA30_1 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA30_1=='\\') ) {s = 2;}
						else if ( (LA30_1=='-') ) {s = 3;}
						else if ( (LA30_1=='+') ) {s = 4;}
						else if ( (LA30_1=='*'||LA30_1=='?') ) {s = 6;}
						else s = 5;
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA30_0 = input.LA(1);
						s = -1;
						if ( ((LA30_0 >= '\u0000' && LA30_0 <= '\b')||(LA30_0 >= '\u000B' && LA30_0 <= '\f')||(LA30_0 >= '\u000E' && LA30_0 <= '\u001F')||(LA30_0 >= '#' && LA30_0 <= '&')||LA30_0==','||(LA30_0 >= '.' && LA30_0 <= '9')||(LA30_0 >= ';' && LA30_0 <= '>')||(LA30_0 >= '@' && LA30_0 <= 'Z')||(LA30_0 >= '_' && LA30_0 <= 'z')||LA30_0=='|'||(LA30_0 >= '\u007F' && LA30_0 <= '\u2FFF')||(LA30_0 >= '\u3001' && LA30_0 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA30_0=='\\') ) {s = 2;}
						else if ( (LA30_0=='-') ) {s = 3;}
						else if ( (LA30_0=='+') ) {s = 4;}
						else if ( (LA30_0=='*'||LA30_0=='?') ) {s = 6;}
						else s = 5;
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA30_7 = input.LA(1);
						s = -1;
						if ( ((LA30_7 >= '\u0000' && LA30_7 <= '\b')||(LA30_7 >= '\u000B' && LA30_7 <= '\f')||(LA30_7 >= '\u000E' && LA30_7 <= '\u001F')||(LA30_7 >= '#' && LA30_7 <= '&')||LA30_7==','||(LA30_7 >= '.' && LA30_7 <= '9')||(LA30_7 >= ';' && LA30_7 <= '>')||(LA30_7 >= '@' && LA30_7 <= 'Z')||(LA30_7 >= '_' && LA30_7 <= 'z')||LA30_7=='|'||(LA30_7 >= '\u007F' && LA30_7 <= '\u2FFF')||(LA30_7 >= '\u3001' && LA30_7 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA30_7=='\\') ) {s = 2;}
						else if ( (LA30_7=='-') ) {s = 3;}
						else if ( (LA30_7=='+') ) {s = 4;}
						else if ( (LA30_7=='*'||LA30_7=='?') ) {s = 6;}
						else s = 5;
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA30_2 = input.LA(1);
						s = -1;
						if ( ((LA30_2 >= '\u0000' && LA30_2 <= '\uFFFF')) ) {s = 7;}
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA30_3 = input.LA(1);
						s = -1;
						if ( ((LA30_3 >= '\u0000' && LA30_3 <= '\b')||(LA30_3 >= '\u000B' && LA30_3 <= '\f')||(LA30_3 >= '\u000E' && LA30_3 <= '\u001F')||(LA30_3 >= '#' && LA30_3 <= '&')||LA30_3==','||(LA30_3 >= '.' && LA30_3 <= '9')||(LA30_3 >= ';' && LA30_3 <= '>')||(LA30_3 >= '@' && LA30_3 <= 'Z')||(LA30_3 >= '_' && LA30_3 <= 'z')||LA30_3=='|'||(LA30_3 >= '\u007F' && LA30_3 <= '\u2FFF')||(LA30_3 >= '\u3001' && LA30_3 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA30_3=='\\') ) {s = 2;}
						else if ( (LA30_3=='-') ) {s = 3;}
						else if ( (LA30_3=='+') ) {s = 4;}
						else if ( (LA30_3=='*'||LA30_3=='?') ) {s = 6;}
						else s = 5;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 30, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA37_eotS =
		"\10\uffff\1\32\1\34\4\uffff\1\35\1\uffff\2\42\1\50\1\42\1\53\1\42\1\uffff"+
		"\1\57\1\42\10\uffff\1\66\1\uffff\1\42\1\uffff\3\42\1\uffff\1\50\1\53\1"+
		"\uffff\1\53\2\42\1\uffff\1\57\3\42\3\uffff\1\42\1\50\1\76\2\57\1\42\2"+
		"\uffff\1\57\4\42\1\57\1\42\1\107\1\uffff\1\42\1\107";
	static final String DFA37_eofS =
		"\112\uffff";
	static final String DFA37_minS =
		"\1\0\7\uffff\2\0\4\uffff\1\0\1\uffff\6\0\1\uffff\3\0\4\uffff\2\0\1\uffff"+
		"\1\0\1\uffff\5\0\1\uffff\2\0\1\uffff\3\0\1\uffff\5\0\2\uffff\6\0\2\uffff"+
		"\10\0\1\uffff\2\0";
	static final String DFA37_maxS =
		"\1\uffff\7\uffff\2\uffff\4\uffff\1\uffff\1\uffff\6\uffff\1\uffff\3\uffff"+
		"\4\uffff\2\uffff\1\uffff\1\uffff\1\uffff\5\uffff\1\uffff\2\uffff\1\uffff"+
		"\3\uffff\1\uffff\5\uffff\2\uffff\6\uffff\2\uffff\10\uffff\1\uffff\2\uffff";
	static final String DFA37_acceptS =
		"\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\2\uffff\1\12\1\13\1\14\1\15\1\uffff"+
		"\1\17\6\uffff\1\24\3\uffff\1\10\1\30\1\11\1\16\2\uffff\1\32\1\uffff\1"+
		"\27\5\uffff\1\21\2\uffff\1\22\3\uffff\1\25\5\uffff\1\31\1\20\6\uffff\1"+
		"\31\1\23\10\uffff\1\26\2\uffff";
	static final String DFA37_specialS =
		"\1\26\7\uffff\1\25\1\37\4\uffff\1\35\1\uffff\1\14\1\21\1\6\1\31\1\3\1"+
		"\43\1\uffff\1\4\1\0\1\40\4\uffff\1\44\1\53\1\uffff\1\20\1\uffff\1\56\1"+
		"\41\1\7\1\10\1\17\1\uffff\1\32\1\13\1\uffff\1\1\1\42\1\27\1\uffff\1\11"+
		"\1\12\1\33\1\51\1\45\2\uffff\1\50\1\2\1\55\1\22\1\54\1\30\2\uffff\1\15"+
		"\1\16\1\36\1\5\1\23\1\52\1\46\1\34\1\uffff\1\47\1\24}>";
	static final String[] DFA37_transitionS = {
			"\11\30\2\26\2\30\1\26\22\30\1\26\1\7\1\16\3\30\1\22\1\17\1\1\1\2\1\10"+
			"\1\6\1\30\1\7\2\30\12\27\1\5\4\30\1\11\1\30\1\21\14\30\1\25\1\23\4\30"+
			"\1\20\6\30\1\3\1\31\1\4\1\14\2\30\1\21\14\30\1\25\1\23\13\30\1\12\1\24"+
			"\1\13\1\15\u2f81\30\1\26\ucfff\30",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\11\33\2\uffff\2\33\1\uffff\22\33\3\uffff\4\33\4\uffff\17\33\1\uffff"+
			"\4\33\1\uffff\33\33\1\uffff\1\33\2\uffff\34\33\1\uffff\1\33\2\uffff\u2f81"+
			"\33\1\uffff\ucfff\33",
			"\11\33\2\uffff\2\33\1\uffff\22\33\3\uffff\4\33\4\uffff\17\33\1\uffff"+
			"\4\33\1\11\33\33\1\uffff\1\33\2\uffff\34\33\1\uffff\1\33\2\uffff\u2f81"+
			"\33\1\uffff\ucfff\33",
			"",
			"",
			"",
			"",
			"\42\37\1\uffff\7\37\1\40\24\37\1\40\34\37\1\36\uffa3\37",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\17\43\1\41\13\43\1\uffff\1\44\2\uffff\34"+
			"\43\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\16\43\1\47\14\43\1\uffff\1\44\2\uffff\17"+
			"\43\1\47\14\43\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\3\43\1\51\3\uffff\1\33\1\46"+
			"\1\43\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1"+
			"\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\22\43\1\52\10\43\1\uffff\1\44\2\uffff\23"+
			"\43\1\52\10\43\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\54\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\17\43\1\55\13\43\1\uffff\1\44\2\uffff\20"+
			"\43\1\55\13\43\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\62\1\56\1\61\12\60\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34"+
			"\43\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\0\63",
			"",
			"",
			"",
			"",
			"\0\64",
			"\42\37\1\65\7\37\1\40\24\37\1\40\34\37\1\36\uffa3\37",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\0\67",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\4\43\1\70\26\43\1\uffff\1\44\2\uffff\5"+
			"\43\1\70\26\43\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\24\43\1\71\6\43\1\uffff\1\44\2\uffff\25"+
			"\43\1\71\6\43\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\72\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1"+
			"\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\62\1\56\1\61\12\73\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34"+
			"\43\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\74\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1"+
			"\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\74\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1"+
			"\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\42\37\1\65\7\37\1\40\24\37\1\40\34\37\1\36\uffa3\37",
			"",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\101\2\100\12\77\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\1\102\1\43\12\73\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34"+
			"\43\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\101\2\100\12\103\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\101\2\100\12\104\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\105\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\105\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\104\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\101\2\100\12\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\104\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\106\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\110\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\2\43\12\111\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43"+
			"\1\uffff\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43",
			"\11\43\2\uffff\2\43\1\uffff\22\43\3\uffff\4\43\3\uffff\1\33\1\46\1\43"+
			"\1\45\14\43\1\uffff\4\43\1\33\33\43\1\uffff\1\44\2\uffff\34\43\1\uffff"+
			"\1\43\2\uffff\u2f81\43\1\uffff\ucfff\43"
	};

	static final short[] DFA37_eot = DFA.unpackEncodedString(DFA37_eotS);
	static final short[] DFA37_eof = DFA.unpackEncodedString(DFA37_eofS);
	static final char[] DFA37_min = DFA.unpackEncodedStringToUnsignedChars(DFA37_minS);
	static final char[] DFA37_max = DFA.unpackEncodedStringToUnsignedChars(DFA37_maxS);
	static final short[] DFA37_accept = DFA.unpackEncodedString(DFA37_acceptS);
	static final short[] DFA37_special = DFA.unpackEncodedString(DFA37_specialS);
	static final short[][] DFA37_transition;

	static {
		int numStates = DFA37_transitionS.length;
		DFA37_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA37_transition[i] = DFA.unpackEncodedString(DFA37_transitionS[i]);
		}
	}

	protected class DFA37 extends DFA {

		public DFA37(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 37;
			this.eot = DFA37_eot;
			this.eof = DFA37_eof;
			this.min = DFA37_min;
			this.max = DFA37_max;
			this.accept = DFA37_accept;
			this.special = DFA37_special;
			this.transition = DFA37_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | WS | NUMBER | DATE_TOKEN | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA37_24 = input.LA(1);
						s = -1;
						if ( ((LA37_24 >= '\u0000' && LA37_24 <= '\b')||(LA37_24 >= '\u000B' && LA37_24 <= '\f')||(LA37_24 >= '\u000E' && LA37_24 <= '\u001F')||(LA37_24 >= '#' && LA37_24 <= '&')||LA37_24==','||(LA37_24 >= '.' && LA37_24 <= '9')||(LA37_24 >= ';' && LA37_24 <= '>')||(LA37_24 >= '@' && LA37_24 <= 'Z')||(LA37_24 >= '_' && LA37_24 <= 'z')||LA37_24=='|'||(LA37_24 >= '\u007F' && LA37_24 <= '\u2FFF')||(LA37_24 >= '\u3001' && LA37_24 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_24=='\\') ) {s = 36;}
						else if ( (LA37_24=='-') ) {s = 37;}
						else if ( (LA37_24=='+') ) {s = 38;}
						else if ( (LA37_24=='*'||LA37_24=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA37_44 = input.LA(1);
						s = -1;
						if ( ((LA37_44 >= '\u0000' && LA37_44 <= '\b')||(LA37_44 >= '\u000B' && LA37_44 <= '\f')||(LA37_44 >= '\u000E' && LA37_44 <= '\u001F')||(LA37_44 >= '#' && LA37_44 <= '&')||LA37_44==','||(LA37_44 >= '.' && LA37_44 <= '9')||(LA37_44 >= ';' && LA37_44 <= '>')||(LA37_44 >= '@' && LA37_44 <= 'Z')||(LA37_44 >= '_' && LA37_44 <= 'z')||LA37_44=='|'||(LA37_44 >= '\u007F' && LA37_44 <= '\u2FFF')||(LA37_44 >= '\u3001' && LA37_44 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_44=='\\') ) {s = 36;}
						else if ( (LA37_44=='-') ) {s = 37;}
						else if ( (LA37_44=='+') ) {s = 38;}
						else if ( (LA37_44=='*'||LA37_44=='?') ) {s = 27;}
						else s = 43;
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA37_56 = input.LA(1);
						s = -1;
						if ( ((LA37_56 >= '\u0000' && LA37_56 <= '\b')||(LA37_56 >= '\u000B' && LA37_56 <= '\f')||(LA37_56 >= '\u000E' && LA37_56 <= '\u001F')||(LA37_56 >= '#' && LA37_56 <= '&')||LA37_56==','||(LA37_56 >= '.' && LA37_56 <= '9')||(LA37_56 >= ';' && LA37_56 <= '>')||(LA37_56 >= '@' && LA37_56 <= 'Z')||(LA37_56 >= '_' && LA37_56 <= 'z')||LA37_56=='|'||(LA37_56 >= '\u007F' && LA37_56 <= '\u2FFF')||(LA37_56 >= '\u3001' && LA37_56 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_56=='\\') ) {s = 36;}
						else if ( (LA37_56=='-') ) {s = 37;}
						else if ( (LA37_56=='+') ) {s = 38;}
						else if ( (LA37_56=='*'||LA37_56=='?') ) {s = 27;}
						else s = 40;
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA37_20 = input.LA(1);
						s = -1;
						if ( (LA37_20=='|') ) {s = 44;}
						else if ( ((LA37_20 >= '\u0000' && LA37_20 <= '\b')||(LA37_20 >= '\u000B' && LA37_20 <= '\f')||(LA37_20 >= '\u000E' && LA37_20 <= '\u001F')||(LA37_20 >= '#' && LA37_20 <= '&')||LA37_20==','||(LA37_20 >= '.' && LA37_20 <= '9')||(LA37_20 >= ';' && LA37_20 <= '>')||(LA37_20 >= '@' && LA37_20 <= 'Z')||(LA37_20 >= '_' && LA37_20 <= 'z')||(LA37_20 >= '\u007F' && LA37_20 <= '\u2FFF')||(LA37_20 >= '\u3001' && LA37_20 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_20=='\\') ) {s = 36;}
						else if ( (LA37_20=='-') ) {s = 37;}
						else if ( (LA37_20=='+') ) {s = 38;}
						else if ( (LA37_20=='*'||LA37_20=='?') ) {s = 27;}
						else s = 43;
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA37_23 = input.LA(1);
						s = -1;
						if ( (LA37_23=='.') ) {s = 46;}
						else if ( ((LA37_23 >= '0' && LA37_23 <= '9')) ) {s = 48;}
						else if ( (LA37_23=='/') ) {s = 49;}
						else if ( (LA37_23=='-') ) {s = 50;}
						else if ( ((LA37_23 >= '\u0000' && LA37_23 <= '\b')||(LA37_23 >= '\u000B' && LA37_23 <= '\f')||(LA37_23 >= '\u000E' && LA37_23 <= '\u001F')||(LA37_23 >= '#' && LA37_23 <= '&')||LA37_23==','||(LA37_23 >= ';' && LA37_23 <= '>')||(LA37_23 >= '@' && LA37_23 <= 'Z')||(LA37_23 >= '_' && LA37_23 <= 'z')||LA37_23=='|'||(LA37_23 >= '\u007F' && LA37_23 <= '\u2FFF')||(LA37_23 >= '\u3001' && LA37_23 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_23=='\\') ) {s = 36;}
						else if ( (LA37_23=='+') ) {s = 38;}
						else if ( (LA37_23=='*'||LA37_23=='?') ) {s = 27;}
						else s = 47;
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA37_66 = input.LA(1);
						s = -1;
						if ( ((LA37_66 >= '0' && LA37_66 <= '9')) ) {s = 68;}
						else if ( ((LA37_66 >= '\u0000' && LA37_66 <= '\b')||(LA37_66 >= '\u000B' && LA37_66 <= '\f')||(LA37_66 >= '\u000E' && LA37_66 <= '\u001F')||(LA37_66 >= '#' && LA37_66 <= '&')||LA37_66==','||(LA37_66 >= '.' && LA37_66 <= '/')||(LA37_66 >= ';' && LA37_66 <= '>')||(LA37_66 >= '@' && LA37_66 <= 'Z')||(LA37_66 >= '_' && LA37_66 <= 'z')||LA37_66=='|'||(LA37_66 >= '\u007F' && LA37_66 <= '\u2FFF')||(LA37_66 >= '\u3001' && LA37_66 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_66=='\\') ) {s = 36;}
						else if ( (LA37_66=='-') ) {s = 37;}
						else if ( (LA37_66=='+') ) {s = 38;}
						else if ( (LA37_66=='*'||LA37_66=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 6 : 
						int LA37_18 = input.LA(1);
						s = -1;
						if ( (LA37_18=='&') ) {s = 41;}
						else if ( ((LA37_18 >= '\u0000' && LA37_18 <= '\b')||(LA37_18 >= '\u000B' && LA37_18 <= '\f')||(LA37_18 >= '\u000E' && LA37_18 <= '\u001F')||(LA37_18 >= '#' && LA37_18 <= '%')||LA37_18==','||(LA37_18 >= '.' && LA37_18 <= '9')||(LA37_18 >= ';' && LA37_18 <= '>')||(LA37_18 >= '@' && LA37_18 <= 'Z')||(LA37_18 >= '_' && LA37_18 <= 'z')||LA37_18=='|'||(LA37_18 >= '\u007F' && LA37_18 <= '\u2FFF')||(LA37_18 >= '\u3001' && LA37_18 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_18=='\\') ) {s = 36;}
						else if ( (LA37_18=='-') ) {s = 37;}
						else if ( (LA37_18=='+') ) {s = 38;}
						else if ( (LA37_18=='*'||LA37_18=='?') ) {s = 27;}
						else s = 40;
						if ( s>=0 ) return s;
						break;

					case 7 : 
						int LA37_37 = input.LA(1);
						s = -1;
						if ( ((LA37_37 >= '\u0000' && LA37_37 <= '\b')||(LA37_37 >= '\u000B' && LA37_37 <= '\f')||(LA37_37 >= '\u000E' && LA37_37 <= '\u001F')||(LA37_37 >= '#' && LA37_37 <= '&')||LA37_37==','||(LA37_37 >= '.' && LA37_37 <= '9')||(LA37_37 >= ';' && LA37_37 <= '>')||(LA37_37 >= '@' && LA37_37 <= 'Z')||(LA37_37 >= '_' && LA37_37 <= 'z')||LA37_37=='|'||(LA37_37 >= '\u007F' && LA37_37 <= '\u2FFF')||(LA37_37 >= '\u3001' && LA37_37 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_37=='\\') ) {s = 36;}
						else if ( (LA37_37=='-') ) {s = 37;}
						else if ( (LA37_37=='+') ) {s = 38;}
						else if ( (LA37_37=='*'||LA37_37=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 8 : 
						int LA37_38 = input.LA(1);
						s = -1;
						if ( ((LA37_38 >= '\u0000' && LA37_38 <= '\b')||(LA37_38 >= '\u000B' && LA37_38 <= '\f')||(LA37_38 >= '\u000E' && LA37_38 <= '\u001F')||(LA37_38 >= '#' && LA37_38 <= '&')||LA37_38==','||(LA37_38 >= '.' && LA37_38 <= '9')||(LA37_38 >= ';' && LA37_38 <= '>')||(LA37_38 >= '@' && LA37_38 <= 'Z')||(LA37_38 >= '_' && LA37_38 <= 'z')||LA37_38=='|'||(LA37_38 >= '\u007F' && LA37_38 <= '\u2FFF')||(LA37_38 >= '\u3001' && LA37_38 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_38=='\\') ) {s = 36;}
						else if ( (LA37_38=='-') ) {s = 37;}
						else if ( (LA37_38=='+') ) {s = 38;}
						else if ( (LA37_38=='*'||LA37_38=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 9 : 
						int LA37_48 = input.LA(1);
						s = -1;
						if ( (LA37_48=='.') ) {s = 46;}
						else if ( ((LA37_48 >= '0' && LA37_48 <= '9')) ) {s = 59;}
						else if ( (LA37_48=='/') ) {s = 49;}
						else if ( (LA37_48=='-') ) {s = 50;}
						else if ( ((LA37_48 >= '\u0000' && LA37_48 <= '\b')||(LA37_48 >= '\u000B' && LA37_48 <= '\f')||(LA37_48 >= '\u000E' && LA37_48 <= '\u001F')||(LA37_48 >= '#' && LA37_48 <= '&')||LA37_48==','||(LA37_48 >= ';' && LA37_48 <= '>')||(LA37_48 >= '@' && LA37_48 <= 'Z')||(LA37_48 >= '_' && LA37_48 <= 'z')||LA37_48=='|'||(LA37_48 >= '\u007F' && LA37_48 <= '\u2FFF')||(LA37_48 >= '\u3001' && LA37_48 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_48=='\\') ) {s = 36;}
						else if ( (LA37_48=='+') ) {s = 38;}
						else if ( (LA37_48=='*'||LA37_48=='?') ) {s = 27;}
						else s = 47;
						if ( s>=0 ) return s;
						break;

					case 10 : 
						int LA37_49 = input.LA(1);
						s = -1;
						if ( ((LA37_49 >= '0' && LA37_49 <= '9')) ) {s = 60;}
						else if ( ((LA37_49 >= '\u0000' && LA37_49 <= '\b')||(LA37_49 >= '\u000B' && LA37_49 <= '\f')||(LA37_49 >= '\u000E' && LA37_49 <= '\u001F')||(LA37_49 >= '#' && LA37_49 <= '&')||LA37_49==','||(LA37_49 >= '.' && LA37_49 <= '/')||(LA37_49 >= ';' && LA37_49 <= '>')||(LA37_49 >= '@' && LA37_49 <= 'Z')||(LA37_49 >= '_' && LA37_49 <= 'z')||LA37_49=='|'||(LA37_49 >= '\u007F' && LA37_49 <= '\u2FFF')||(LA37_49 >= '\u3001' && LA37_49 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_49=='\\') ) {s = 36;}
						else if ( (LA37_49=='-') ) {s = 37;}
						else if ( (LA37_49=='+') ) {s = 38;}
						else if ( (LA37_49=='*'||LA37_49=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 11 : 
						int LA37_42 = input.LA(1);
						s = -1;
						if ( ((LA37_42 >= '\u0000' && LA37_42 <= '\b')||(LA37_42 >= '\u000B' && LA37_42 <= '\f')||(LA37_42 >= '\u000E' && LA37_42 <= '\u001F')||(LA37_42 >= '#' && LA37_42 <= '&')||LA37_42==','||(LA37_42 >= '.' && LA37_42 <= '9')||(LA37_42 >= ';' && LA37_42 <= '>')||(LA37_42 >= '@' && LA37_42 <= 'Z')||(LA37_42 >= '_' && LA37_42 <= 'z')||LA37_42=='|'||(LA37_42 >= '\u007F' && LA37_42 <= '\u2FFF')||(LA37_42 >= '\u3001' && LA37_42 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_42=='\\') ) {s = 36;}
						else if ( (LA37_42=='-') ) {s = 37;}
						else if ( (LA37_42=='+') ) {s = 38;}
						else if ( (LA37_42=='*'||LA37_42=='?') ) {s = 27;}
						else s = 43;
						if ( s>=0 ) return s;
						break;

					case 12 : 
						int LA37_16 = input.LA(1);
						s = -1;
						if ( (LA37_16=='O') ) {s = 33;}
						else if ( ((LA37_16 >= '\u0000' && LA37_16 <= '\b')||(LA37_16 >= '\u000B' && LA37_16 <= '\f')||(LA37_16 >= '\u000E' && LA37_16 <= '\u001F')||(LA37_16 >= '#' && LA37_16 <= '&')||LA37_16==','||(LA37_16 >= '.' && LA37_16 <= '9')||(LA37_16 >= ';' && LA37_16 <= '>')||(LA37_16 >= '@' && LA37_16 <= 'N')||(LA37_16 >= 'P' && LA37_16 <= 'Z')||(LA37_16 >= '_' && LA37_16 <= 'z')||LA37_16=='|'||(LA37_16 >= '\u007F' && LA37_16 <= '\u2FFF')||(LA37_16 >= '\u3001' && LA37_16 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_16=='\\') ) {s = 36;}
						else if ( (LA37_16=='-') ) {s = 37;}
						else if ( (LA37_16=='+') ) {s = 38;}
						else if ( (LA37_16=='*'||LA37_16=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 13 : 
						int LA37_63 = input.LA(1);
						s = -1;
						if ( ((LA37_63 >= '0' && LA37_63 <= '9')) ) {s = 68;}
						else if ( ((LA37_63 >= '.' && LA37_63 <= '/')) ) {s = 64;}
						else if ( (LA37_63=='-') ) {s = 65;}
						else if ( ((LA37_63 >= '\u0000' && LA37_63 <= '\b')||(LA37_63 >= '\u000B' && LA37_63 <= '\f')||(LA37_63 >= '\u000E' && LA37_63 <= '\u001F')||(LA37_63 >= '#' && LA37_63 <= '&')||LA37_63==','||(LA37_63 >= ';' && LA37_63 <= '>')||(LA37_63 >= '@' && LA37_63 <= 'Z')||(LA37_63 >= '_' && LA37_63 <= 'z')||LA37_63=='|'||(LA37_63 >= '\u007F' && LA37_63 <= '\u2FFF')||(LA37_63 >= '\u3001' && LA37_63 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_63=='\\') ) {s = 36;}
						else if ( (LA37_63=='+') ) {s = 38;}
						else if ( (LA37_63=='*'||LA37_63=='?') ) {s = 27;}
						else s = 47;
						if ( s>=0 ) return s;
						break;

					case 14 : 
						int LA37_64 = input.LA(1);
						s = -1;
						if ( ((LA37_64 >= '0' && LA37_64 <= '9')) ) {s = 69;}
						else if ( ((LA37_64 >= '\u0000' && LA37_64 <= '\b')||(LA37_64 >= '\u000B' && LA37_64 <= '\f')||(LA37_64 >= '\u000E' && LA37_64 <= '\u001F')||(LA37_64 >= '#' && LA37_64 <= '&')||LA37_64==','||(LA37_64 >= '.' && LA37_64 <= '/')||(LA37_64 >= ';' && LA37_64 <= '>')||(LA37_64 >= '@' && LA37_64 <= 'Z')||(LA37_64 >= '_' && LA37_64 <= 'z')||LA37_64=='|'||(LA37_64 >= '\u007F' && LA37_64 <= '\u2FFF')||(LA37_64 >= '\u3001' && LA37_64 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_64=='\\') ) {s = 36;}
						else if ( (LA37_64=='-') ) {s = 37;}
						else if ( (LA37_64=='+') ) {s = 38;}
						else if ( (LA37_64=='*'||LA37_64=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 15 : 
						int LA37_39 = input.LA(1);
						s = -1;
						if ( (LA37_39=='D'||LA37_39=='d') ) {s = 56;}
						else if ( ((LA37_39 >= '\u0000' && LA37_39 <= '\b')||(LA37_39 >= '\u000B' && LA37_39 <= '\f')||(LA37_39 >= '\u000E' && LA37_39 <= '\u001F')||(LA37_39 >= '#' && LA37_39 <= '&')||LA37_39==','||(LA37_39 >= '.' && LA37_39 <= '9')||(LA37_39 >= ';' && LA37_39 <= '>')||(LA37_39 >= '@' && LA37_39 <= 'C')||(LA37_39 >= 'E' && LA37_39 <= 'Z')||(LA37_39 >= '_' && LA37_39 <= 'c')||(LA37_39 >= 'e' && LA37_39 <= 'z')||LA37_39=='|'||(LA37_39 >= '\u007F' && LA37_39 <= '\u2FFF')||(LA37_39 >= '\u3001' && LA37_39 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_39=='\\') ) {s = 36;}
						else if ( (LA37_39=='-') ) {s = 37;}
						else if ( (LA37_39=='+') ) {s = 38;}
						else if ( (LA37_39=='*'||LA37_39=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 16 : 
						int LA37_33 = input.LA(1);
						s = -1;
						if ( ((LA37_33 >= '\u0000' && LA37_33 <= '\b')||(LA37_33 >= '\u000B' && LA37_33 <= '\f')||(LA37_33 >= '\u000E' && LA37_33 <= '\u001F')||(LA37_33 >= '#' && LA37_33 <= '&')||LA37_33==','||(LA37_33 >= '.' && LA37_33 <= '9')||(LA37_33 >= ';' && LA37_33 <= '>')||(LA37_33 >= '@' && LA37_33 <= 'Z')||(LA37_33 >= '_' && LA37_33 <= 'z')||LA37_33=='|'||(LA37_33 >= '\u007F' && LA37_33 <= '\u2FFF')||(LA37_33 >= '\u3001' && LA37_33 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_33=='\\') ) {s = 36;}
						else if ( (LA37_33=='-') ) {s = 37;}
						else if ( (LA37_33=='+') ) {s = 38;}
						else if ( (LA37_33=='*'||LA37_33=='?') ) {s = 27;}
						else s = 54;
						if ( s>=0 ) return s;
						break;

					case 17 : 
						int LA37_17 = input.LA(1);
						s = -1;
						if ( (LA37_17=='N'||LA37_17=='n') ) {s = 39;}
						else if ( ((LA37_17 >= '\u0000' && LA37_17 <= '\b')||(LA37_17 >= '\u000B' && LA37_17 <= '\f')||(LA37_17 >= '\u000E' && LA37_17 <= '\u001F')||(LA37_17 >= '#' && LA37_17 <= '&')||LA37_17==','||(LA37_17 >= '.' && LA37_17 <= '9')||(LA37_17 >= ';' && LA37_17 <= '>')||(LA37_17 >= '@' && LA37_17 <= 'M')||(LA37_17 >= 'O' && LA37_17 <= 'Z')||(LA37_17 >= '_' && LA37_17 <= 'm')||(LA37_17 >= 'o' && LA37_17 <= 'z')||LA37_17=='|'||(LA37_17 >= '\u007F' && LA37_17 <= '\u2FFF')||(LA37_17 >= '\u3001' && LA37_17 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_17=='\\') ) {s = 36;}
						else if ( (LA37_17=='-') ) {s = 37;}
						else if ( (LA37_17=='+') ) {s = 38;}
						else if ( (LA37_17=='*'||LA37_17=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 18 : 
						int LA37_58 = input.LA(1);
						s = -1;
						if ( ((LA37_58 >= '0' && LA37_58 <= '9')) ) {s = 63;}
						else if ( ((LA37_58 >= '.' && LA37_58 <= '/')) ) {s = 64;}
						else if ( (LA37_58=='-') ) {s = 65;}
						else if ( ((LA37_58 >= '\u0000' && LA37_58 <= '\b')||(LA37_58 >= '\u000B' && LA37_58 <= '\f')||(LA37_58 >= '\u000E' && LA37_58 <= '\u001F')||(LA37_58 >= '#' && LA37_58 <= '&')||LA37_58==','||(LA37_58 >= ';' && LA37_58 <= '>')||(LA37_58 >= '@' && LA37_58 <= 'Z')||(LA37_58 >= '_' && LA37_58 <= 'z')||LA37_58=='|'||(LA37_58 >= '\u007F' && LA37_58 <= '\u2FFF')||(LA37_58 >= '\u3001' && LA37_58 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_58=='\\') ) {s = 36;}
						else if ( (LA37_58=='+') ) {s = 38;}
						else if ( (LA37_58=='*'||LA37_58=='?') ) {s = 27;}
						else s = 47;
						if ( s>=0 ) return s;
						break;

					case 19 : 
						int LA37_67 = input.LA(1);
						s = -1;
						if ( ((LA37_67 >= '.' && LA37_67 <= '/')) ) {s = 64;}
						else if ( (LA37_67=='-') ) {s = 65;}
						else if ( ((LA37_67 >= '\u0000' && LA37_67 <= '\b')||(LA37_67 >= '\u000B' && LA37_67 <= '\f')||(LA37_67 >= '\u000E' && LA37_67 <= '\u001F')||(LA37_67 >= '#' && LA37_67 <= '&')||LA37_67==','||(LA37_67 >= '0' && LA37_67 <= '9')||(LA37_67 >= ';' && LA37_67 <= '>')||(LA37_67 >= '@' && LA37_67 <= 'Z')||(LA37_67 >= '_' && LA37_67 <= 'z')||LA37_67=='|'||(LA37_67 >= '\u007F' && LA37_67 <= '\u2FFF')||(LA37_67 >= '\u3001' && LA37_67 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_67=='\\') ) {s = 36;}
						else if ( (LA37_67=='+') ) {s = 38;}
						else if ( (LA37_67=='*'||LA37_67=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 20 : 
						int LA37_73 = input.LA(1);
						s = -1;
						if ( ((LA37_73 >= '\u0000' && LA37_73 <= '\b')||(LA37_73 >= '\u000B' && LA37_73 <= '\f')||(LA37_73 >= '\u000E' && LA37_73 <= '\u001F')||(LA37_73 >= '#' && LA37_73 <= '&')||LA37_73==','||(LA37_73 >= '.' && LA37_73 <= '9')||(LA37_73 >= ';' && LA37_73 <= '>')||(LA37_73 >= '@' && LA37_73 <= 'Z')||(LA37_73 >= '_' && LA37_73 <= 'z')||LA37_73=='|'||(LA37_73 >= '\u007F' && LA37_73 <= '\u2FFF')||(LA37_73 >= '\u3001' && LA37_73 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_73=='\\') ) {s = 36;}
						else if ( (LA37_73=='-') ) {s = 37;}
						else if ( (LA37_73=='+') ) {s = 38;}
						else if ( (LA37_73=='*'||LA37_73=='?') ) {s = 27;}
						else s = 71;
						if ( s>=0 ) return s;
						break;

					case 21 : 
						int LA37_8 = input.LA(1);
						s = -1;
						if ( ((LA37_8 >= '\u0000' && LA37_8 <= '\b')||(LA37_8 >= '\u000B' && LA37_8 <= '\f')||(LA37_8 >= '\u000E' && LA37_8 <= '\u001F')||(LA37_8 >= '#' && LA37_8 <= '&')||(LA37_8 >= '+' && LA37_8 <= '9')||(LA37_8 >= ';' && LA37_8 <= '>')||(LA37_8 >= '@' && LA37_8 <= 'Z')||LA37_8=='\\'||(LA37_8 >= '_' && LA37_8 <= 'z')||LA37_8=='|'||(LA37_8 >= '\u007F' && LA37_8 <= '\u2FFF')||(LA37_8 >= '\u3001' && LA37_8 <= '\uFFFF')) ) {s = 27;}
						else s = 26;
						if ( s>=0 ) return s;
						break;

					case 22 : 
						int LA37_0 = input.LA(1);
						s = -1;
						if ( (LA37_0=='(') ) {s = 1;}
						else if ( (LA37_0==')') ) {s = 2;}
						else if ( (LA37_0=='[') ) {s = 3;}
						else if ( (LA37_0==']') ) {s = 4;}
						else if ( (LA37_0==':') ) {s = 5;}
						else if ( (LA37_0=='+') ) {s = 6;}
						else if ( (LA37_0=='!'||LA37_0=='-') ) {s = 7;}
						else if ( (LA37_0=='*') ) {s = 8;}
						else if ( (LA37_0=='?') ) {s = 9;}
						else if ( (LA37_0=='{') ) {s = 10;}
						else if ( (LA37_0=='}') ) {s = 11;}
						else if ( (LA37_0=='^') ) {s = 12;}
						else if ( (LA37_0=='~') ) {s = 13;}
						else if ( (LA37_0=='\"') ) {s = 14;}
						else if ( (LA37_0=='\'') ) {s = 15;}
						else if ( (LA37_0=='T') ) {s = 16;}
						else if ( (LA37_0=='A'||LA37_0=='a') ) {s = 17;}
						else if ( (LA37_0=='&') ) {s = 18;}
						else if ( (LA37_0=='O'||LA37_0=='o') ) {s = 19;}
						else if ( (LA37_0=='|') ) {s = 20;}
						else if ( (LA37_0=='N'||LA37_0=='n') ) {s = 21;}
						else if ( ((LA37_0 >= '\t' && LA37_0 <= '\n')||LA37_0=='\r'||LA37_0==' '||LA37_0=='\u3000') ) {s = 22;}
						else if ( ((LA37_0 >= '0' && LA37_0 <= '9')) ) {s = 23;}
						else if ( ((LA37_0 >= '\u0000' && LA37_0 <= '\b')||(LA37_0 >= '\u000B' && LA37_0 <= '\f')||(LA37_0 >= '\u000E' && LA37_0 <= '\u001F')||(LA37_0 >= '#' && LA37_0 <= '%')||LA37_0==','||(LA37_0 >= '.' && LA37_0 <= '/')||(LA37_0 >= ';' && LA37_0 <= '>')||LA37_0=='@'||(LA37_0 >= 'B' && LA37_0 <= 'M')||(LA37_0 >= 'P' && LA37_0 <= 'S')||(LA37_0 >= 'U' && LA37_0 <= 'Z')||(LA37_0 >= '_' && LA37_0 <= '`')||(LA37_0 >= 'b' && LA37_0 <= 'm')||(LA37_0 >= 'p' && LA37_0 <= 'z')||(LA37_0 >= '\u007F' && LA37_0 <= '\u2FFF')||(LA37_0 >= '\u3001' && LA37_0 <= '\uFFFF')) ) {s = 24;}
						else if ( (LA37_0=='\\') ) {s = 25;}
						if ( s>=0 ) return s;
						break;

					case 23 : 
						int LA37_46 = input.LA(1);
						s = -1;
						if ( ((LA37_46 >= '0' && LA37_46 <= '9')) ) {s = 58;}
						else if ( ((LA37_46 >= '\u0000' && LA37_46 <= '\b')||(LA37_46 >= '\u000B' && LA37_46 <= '\f')||(LA37_46 >= '\u000E' && LA37_46 <= '\u001F')||(LA37_46 >= '#' && LA37_46 <= '&')||LA37_46==','||(LA37_46 >= '.' && LA37_46 <= '/')||(LA37_46 >= ';' && LA37_46 <= '>')||(LA37_46 >= '@' && LA37_46 <= 'Z')||(LA37_46 >= '_' && LA37_46 <= 'z')||LA37_46=='|'||(LA37_46 >= '\u007F' && LA37_46 <= '\u2FFF')||(LA37_46 >= '\u3001' && LA37_46 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_46=='\\') ) {s = 36;}
						else if ( (LA37_46=='-') ) {s = 37;}
						else if ( (LA37_46=='+') ) {s = 38;}
						else if ( (LA37_46=='*'||LA37_46=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 24 : 
						int LA37_60 = input.LA(1);
						s = -1;
						if ( ((LA37_60 >= '0' && LA37_60 <= '9')) ) {s = 67;}
						else if ( ((LA37_60 >= '.' && LA37_60 <= '/')) ) {s = 64;}
						else if ( (LA37_60=='-') ) {s = 65;}
						else if ( ((LA37_60 >= '\u0000' && LA37_60 <= '\b')||(LA37_60 >= '\u000B' && LA37_60 <= '\f')||(LA37_60 >= '\u000E' && LA37_60 <= '\u001F')||(LA37_60 >= '#' && LA37_60 <= '&')||LA37_60==','||(LA37_60 >= ';' && LA37_60 <= '>')||(LA37_60 >= '@' && LA37_60 <= 'Z')||(LA37_60 >= '_' && LA37_60 <= 'z')||LA37_60=='|'||(LA37_60 >= '\u007F' && LA37_60 <= '\u2FFF')||(LA37_60 >= '\u3001' && LA37_60 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_60=='\\') ) {s = 36;}
						else if ( (LA37_60=='+') ) {s = 38;}
						else if ( (LA37_60=='*'||LA37_60=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 25 : 
						int LA37_19 = input.LA(1);
						s = -1;
						if ( (LA37_19=='R'||LA37_19=='r') ) {s = 42;}
						else if ( ((LA37_19 >= '\u0000' && LA37_19 <= '\b')||(LA37_19 >= '\u000B' && LA37_19 <= '\f')||(LA37_19 >= '\u000E' && LA37_19 <= '\u001F')||(LA37_19 >= '#' && LA37_19 <= '&')||LA37_19==','||(LA37_19 >= '.' && LA37_19 <= '9')||(LA37_19 >= ';' && LA37_19 <= '>')||(LA37_19 >= '@' && LA37_19 <= 'Q')||(LA37_19 >= 'S' && LA37_19 <= 'Z')||(LA37_19 >= '_' && LA37_19 <= 'q')||(LA37_19 >= 's' && LA37_19 <= 'z')||LA37_19=='|'||(LA37_19 >= '\u007F' && LA37_19 <= '\u2FFF')||(LA37_19 >= '\u3001' && LA37_19 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_19=='\\') ) {s = 36;}
						else if ( (LA37_19=='-') ) {s = 37;}
						else if ( (LA37_19=='+') ) {s = 38;}
						else if ( (LA37_19=='*'||LA37_19=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 26 : 
						int LA37_41 = input.LA(1);
						s = -1;
						if ( ((LA37_41 >= '\u0000' && LA37_41 <= '\b')||(LA37_41 >= '\u000B' && LA37_41 <= '\f')||(LA37_41 >= '\u000E' && LA37_41 <= '\u001F')||(LA37_41 >= '#' && LA37_41 <= '&')||LA37_41==','||(LA37_41 >= '.' && LA37_41 <= '9')||(LA37_41 >= ';' && LA37_41 <= '>')||(LA37_41 >= '@' && LA37_41 <= 'Z')||(LA37_41 >= '_' && LA37_41 <= 'z')||LA37_41=='|'||(LA37_41 >= '\u007F' && LA37_41 <= '\u2FFF')||(LA37_41 >= '\u3001' && LA37_41 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_41=='\\') ) {s = 36;}
						else if ( (LA37_41=='-') ) {s = 37;}
						else if ( (LA37_41=='+') ) {s = 38;}
						else if ( (LA37_41=='*'||LA37_41=='?') ) {s = 27;}
						else s = 40;
						if ( s>=0 ) return s;
						break;

					case 27 : 
						int LA37_50 = input.LA(1);
						s = -1;
						if ( ((LA37_50 >= '0' && LA37_50 <= '9')) ) {s = 60;}
						else if ( ((LA37_50 >= '\u0000' && LA37_50 <= '\b')||(LA37_50 >= '\u000B' && LA37_50 <= '\f')||(LA37_50 >= '\u000E' && LA37_50 <= '\u001F')||(LA37_50 >= '#' && LA37_50 <= '&')||LA37_50==','||(LA37_50 >= '.' && LA37_50 <= '/')||(LA37_50 >= ';' && LA37_50 <= '>')||(LA37_50 >= '@' && LA37_50 <= 'Z')||(LA37_50 >= '_' && LA37_50 <= 'z')||LA37_50=='|'||(LA37_50 >= '\u007F' && LA37_50 <= '\u2FFF')||(LA37_50 >= '\u3001' && LA37_50 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_50=='\\') ) {s = 36;}
						else if ( (LA37_50=='-') ) {s = 37;}
						else if ( (LA37_50=='+') ) {s = 38;}
						else if ( (LA37_50=='*'||LA37_50=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 28 : 
						int LA37_70 = input.LA(1);
						s = -1;
						if ( ((LA37_70 >= '0' && LA37_70 <= '9')) ) {s = 72;}
						else if ( ((LA37_70 >= '\u0000' && LA37_70 <= '\b')||(LA37_70 >= '\u000B' && LA37_70 <= '\f')||(LA37_70 >= '\u000E' && LA37_70 <= '\u001F')||(LA37_70 >= '#' && LA37_70 <= '&')||LA37_70==','||(LA37_70 >= '.' && LA37_70 <= '/')||(LA37_70 >= ';' && LA37_70 <= '>')||(LA37_70 >= '@' && LA37_70 <= 'Z')||(LA37_70 >= '_' && LA37_70 <= 'z')||LA37_70=='|'||(LA37_70 >= '\u007F' && LA37_70 <= '\u2FFF')||(LA37_70 >= '\u3001' && LA37_70 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_70=='\\') ) {s = 36;}
						else if ( (LA37_70=='-') ) {s = 37;}
						else if ( (LA37_70=='+') ) {s = 38;}
						else if ( (LA37_70=='*'||LA37_70=='?') ) {s = 27;}
						else s = 71;
						if ( s>=0 ) return s;
						break;

					case 29 : 
						int LA37_14 = input.LA(1);
						s = -1;
						if ( (LA37_14=='\\') ) {s = 30;}
						else if ( ((LA37_14 >= '\u0000' && LA37_14 <= '!')||(LA37_14 >= '#' && LA37_14 <= ')')||(LA37_14 >= '+' && LA37_14 <= '>')||(LA37_14 >= '@' && LA37_14 <= '[')||(LA37_14 >= ']' && LA37_14 <= '\uFFFF')) ) {s = 31;}
						else if ( (LA37_14=='*'||LA37_14=='?') ) {s = 32;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 30 : 
						int LA37_65 = input.LA(1);
						s = -1;
						if ( ((LA37_65 >= '0' && LA37_65 <= '9')) ) {s = 69;}
						else if ( ((LA37_65 >= '\u0000' && LA37_65 <= '\b')||(LA37_65 >= '\u000B' && LA37_65 <= '\f')||(LA37_65 >= '\u000E' && LA37_65 <= '\u001F')||(LA37_65 >= '#' && LA37_65 <= '&')||LA37_65==','||(LA37_65 >= '.' && LA37_65 <= '/')||(LA37_65 >= ';' && LA37_65 <= '>')||(LA37_65 >= '@' && LA37_65 <= 'Z')||(LA37_65 >= '_' && LA37_65 <= 'z')||LA37_65=='|'||(LA37_65 >= '\u007F' && LA37_65 <= '\u2FFF')||(LA37_65 >= '\u3001' && LA37_65 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_65=='\\') ) {s = 36;}
						else if ( (LA37_65=='-') ) {s = 37;}
						else if ( (LA37_65=='+') ) {s = 38;}
						else if ( (LA37_65=='*'||LA37_65=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 31 : 
						int LA37_9 = input.LA(1);
						s = -1;
						if ( (LA37_9=='?') ) {s = 9;}
						else if ( ((LA37_9 >= '\u0000' && LA37_9 <= '\b')||(LA37_9 >= '\u000B' && LA37_9 <= '\f')||(LA37_9 >= '\u000E' && LA37_9 <= '\u001F')||(LA37_9 >= '#' && LA37_9 <= '&')||(LA37_9 >= '+' && LA37_9 <= '9')||(LA37_9 >= ';' && LA37_9 <= '>')||(LA37_9 >= '@' && LA37_9 <= 'Z')||LA37_9=='\\'||(LA37_9 >= '_' && LA37_9 <= 'z')||LA37_9=='|'||(LA37_9 >= '\u007F' && LA37_9 <= '\u2FFF')||(LA37_9 >= '\u3001' && LA37_9 <= '\uFFFF')) ) {s = 27;}
						else s = 28;
						if ( s>=0 ) return s;
						break;

					case 32 : 
						int LA37_25 = input.LA(1);
						s = -1;
						if ( ((LA37_25 >= '\u0000' && LA37_25 <= '\uFFFF')) ) {s = 51;}
						if ( s>=0 ) return s;
						break;

					case 33 : 
						int LA37_36 = input.LA(1);
						s = -1;
						if ( ((LA37_36 >= '\u0000' && LA37_36 <= '\uFFFF')) ) {s = 55;}
						if ( s>=0 ) return s;
						break;

					case 34 : 
						int LA37_45 = input.LA(1);
						s = -1;
						if ( (LA37_45=='T'||LA37_45=='t') ) {s = 57;}
						else if ( ((LA37_45 >= '\u0000' && LA37_45 <= '\b')||(LA37_45 >= '\u000B' && LA37_45 <= '\f')||(LA37_45 >= '\u000E' && LA37_45 <= '\u001F')||(LA37_45 >= '#' && LA37_45 <= '&')||LA37_45==','||(LA37_45 >= '.' && LA37_45 <= '9')||(LA37_45 >= ';' && LA37_45 <= '>')||(LA37_45 >= '@' && LA37_45 <= 'S')||(LA37_45 >= 'U' && LA37_45 <= 'Z')||(LA37_45 >= '_' && LA37_45 <= 's')||(LA37_45 >= 'u' && LA37_45 <= 'z')||LA37_45=='|'||(LA37_45 >= '\u007F' && LA37_45 <= '\u2FFF')||(LA37_45 >= '\u3001' && LA37_45 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_45=='\\') ) {s = 36;}
						else if ( (LA37_45=='-') ) {s = 37;}
						else if ( (LA37_45=='+') ) {s = 38;}
						else if ( (LA37_45=='*'||LA37_45=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 35 : 
						int LA37_21 = input.LA(1);
						s = -1;
						if ( (LA37_21=='O'||LA37_21=='o') ) {s = 45;}
						else if ( ((LA37_21 >= '\u0000' && LA37_21 <= '\b')||(LA37_21 >= '\u000B' && LA37_21 <= '\f')||(LA37_21 >= '\u000E' && LA37_21 <= '\u001F')||(LA37_21 >= '#' && LA37_21 <= '&')||LA37_21==','||(LA37_21 >= '.' && LA37_21 <= '9')||(LA37_21 >= ';' && LA37_21 <= '>')||(LA37_21 >= '@' && LA37_21 <= 'N')||(LA37_21 >= 'P' && LA37_21 <= 'Z')||(LA37_21 >= '_' && LA37_21 <= 'n')||(LA37_21 >= 'p' && LA37_21 <= 'z')||LA37_21=='|'||(LA37_21 >= '\u007F' && LA37_21 <= '\u2FFF')||(LA37_21 >= '\u3001' && LA37_21 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_21=='\\') ) {s = 36;}
						else if ( (LA37_21=='-') ) {s = 37;}
						else if ( (LA37_21=='+') ) {s = 38;}
						else if ( (LA37_21=='*'||LA37_21=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 36 : 
						int LA37_30 = input.LA(1);
						s = -1;
						if ( ((LA37_30 >= '\u0000' && LA37_30 <= '\uFFFF')) ) {s = 52;}
						if ( s>=0 ) return s;
						break;

					case 37 : 
						int LA37_52 = input.LA(1);
						s = -1;
						if ( (LA37_52=='\"') ) {s = 53;}
						else if ( (LA37_52=='\\') ) {s = 30;}
						else if ( ((LA37_52 >= '\u0000' && LA37_52 <= '!')||(LA37_52 >= '#' && LA37_52 <= ')')||(LA37_52 >= '+' && LA37_52 <= '>')||(LA37_52 >= '@' && LA37_52 <= '[')||(LA37_52 >= ']' && LA37_52 <= '\uFFFF')) ) {s = 31;}
						else if ( (LA37_52=='*'||LA37_52=='?') ) {s = 32;}
						if ( s>=0 ) return s;
						break;

					case 38 : 
						int LA37_69 = input.LA(1);
						s = -1;
						if ( ((LA37_69 >= '0' && LA37_69 <= '9')) ) {s = 70;}
						else if ( ((LA37_69 >= '\u0000' && LA37_69 <= '\b')||(LA37_69 >= '\u000B' && LA37_69 <= '\f')||(LA37_69 >= '\u000E' && LA37_69 <= '\u001F')||(LA37_69 >= '#' && LA37_69 <= '&')||LA37_69==','||(LA37_69 >= '.' && LA37_69 <= '/')||(LA37_69 >= ';' && LA37_69 <= '>')||(LA37_69 >= '@' && LA37_69 <= 'Z')||(LA37_69 >= '_' && LA37_69 <= 'z')||LA37_69=='|'||(LA37_69 >= '\u007F' && LA37_69 <= '\u2FFF')||(LA37_69 >= '\u3001' && LA37_69 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_69=='\\') ) {s = 36;}
						else if ( (LA37_69=='-') ) {s = 37;}
						else if ( (LA37_69=='+') ) {s = 38;}
						else if ( (LA37_69=='*'||LA37_69=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 39 : 
						int LA37_72 = input.LA(1);
						s = -1;
						if ( ((LA37_72 >= '0' && LA37_72 <= '9')) ) {s = 73;}
						else if ( ((LA37_72 >= '\u0000' && LA37_72 <= '\b')||(LA37_72 >= '\u000B' && LA37_72 <= '\f')||(LA37_72 >= '\u000E' && LA37_72 <= '\u001F')||(LA37_72 >= '#' && LA37_72 <= '&')||LA37_72==','||(LA37_72 >= '.' && LA37_72 <= '/')||(LA37_72 >= ';' && LA37_72 <= '>')||(LA37_72 >= '@' && LA37_72 <= 'Z')||(LA37_72 >= '_' && LA37_72 <= 'z')||LA37_72=='|'||(LA37_72 >= '\u007F' && LA37_72 <= '\u2FFF')||(LA37_72 >= '\u3001' && LA37_72 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_72=='\\') ) {s = 36;}
						else if ( (LA37_72=='-') ) {s = 37;}
						else if ( (LA37_72=='+') ) {s = 38;}
						else if ( (LA37_72=='*'||LA37_72=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 40 : 
						int LA37_55 = input.LA(1);
						s = -1;
						if ( ((LA37_55 >= '\u0000' && LA37_55 <= '\b')||(LA37_55 >= '\u000B' && LA37_55 <= '\f')||(LA37_55 >= '\u000E' && LA37_55 <= '\u001F')||(LA37_55 >= '#' && LA37_55 <= '&')||LA37_55==','||(LA37_55 >= '.' && LA37_55 <= '9')||(LA37_55 >= ';' && LA37_55 <= '>')||(LA37_55 >= '@' && LA37_55 <= 'Z')||(LA37_55 >= '_' && LA37_55 <= 'z')||LA37_55=='|'||(LA37_55 >= '\u007F' && LA37_55 <= '\u2FFF')||(LA37_55 >= '\u3001' && LA37_55 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_55=='\\') ) {s = 36;}
						else if ( (LA37_55=='-') ) {s = 37;}
						else if ( (LA37_55=='+') ) {s = 38;}
						else if ( (LA37_55=='*'||LA37_55=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 41 : 
						int LA37_51 = input.LA(1);
						s = -1;
						if ( ((LA37_51 >= '\u0000' && LA37_51 <= '\b')||(LA37_51 >= '\u000B' && LA37_51 <= '\f')||(LA37_51 >= '\u000E' && LA37_51 <= '\u001F')||(LA37_51 >= '#' && LA37_51 <= '&')||LA37_51==','||(LA37_51 >= '.' && LA37_51 <= '9')||(LA37_51 >= ';' && LA37_51 <= '>')||(LA37_51 >= '@' && LA37_51 <= 'Z')||(LA37_51 >= '_' && LA37_51 <= 'z')||LA37_51=='|'||(LA37_51 >= '\u007F' && LA37_51 <= '\u2FFF')||(LA37_51 >= '\u3001' && LA37_51 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_51=='\\') ) {s = 36;}
						else if ( (LA37_51=='-') ) {s = 37;}
						else if ( (LA37_51=='+') ) {s = 38;}
						else if ( (LA37_51=='*'||LA37_51=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;

					case 42 : 
						int LA37_68 = input.LA(1);
						s = -1;
						if ( ((LA37_68 >= '0' && LA37_68 <= '9')) ) {s = 68;}
						else if ( ((LA37_68 >= '\u0000' && LA37_68 <= '\b')||(LA37_68 >= '\u000B' && LA37_68 <= '\f')||(LA37_68 >= '\u000E' && LA37_68 <= '\u001F')||(LA37_68 >= '#' && LA37_68 <= '&')||LA37_68==','||(LA37_68 >= '.' && LA37_68 <= '/')||(LA37_68 >= ';' && LA37_68 <= '>')||(LA37_68 >= '@' && LA37_68 <= 'Z')||(LA37_68 >= '_' && LA37_68 <= 'z')||LA37_68=='|'||(LA37_68 >= '\u007F' && LA37_68 <= '\u2FFF')||(LA37_68 >= '\u3001' && LA37_68 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_68=='\\') ) {s = 36;}
						else if ( (LA37_68=='-') ) {s = 37;}
						else if ( (LA37_68=='+') ) {s = 38;}
						else if ( (LA37_68=='*'||LA37_68=='?') ) {s = 27;}
						else s = 47;
						if ( s>=0 ) return s;
						break;

					case 43 : 
						int LA37_31 = input.LA(1);
						s = -1;
						if ( (LA37_31=='\"') ) {s = 53;}
						else if ( (LA37_31=='\\') ) {s = 30;}
						else if ( ((LA37_31 >= '\u0000' && LA37_31 <= '!')||(LA37_31 >= '#' && LA37_31 <= ')')||(LA37_31 >= '+' && LA37_31 <= '>')||(LA37_31 >= '@' && LA37_31 <= '[')||(LA37_31 >= ']' && LA37_31 <= '\uFFFF')) ) {s = 31;}
						else if ( (LA37_31=='*'||LA37_31=='?') ) {s = 32;}
						if ( s>=0 ) return s;
						break;

					case 44 : 
						int LA37_59 = input.LA(1);
						s = -1;
						if ( (LA37_59=='.') ) {s = 66;}
						else if ( ((LA37_59 >= '0' && LA37_59 <= '9')) ) {s = 59;}
						else if ( ((LA37_59 >= '\u0000' && LA37_59 <= '\b')||(LA37_59 >= '\u000B' && LA37_59 <= '\f')||(LA37_59 >= '\u000E' && LA37_59 <= '\u001F')||(LA37_59 >= '#' && LA37_59 <= '&')||LA37_59==','||LA37_59=='/'||(LA37_59 >= ';' && LA37_59 <= '>')||(LA37_59 >= '@' && LA37_59 <= 'Z')||(LA37_59 >= '_' && LA37_59 <= 'z')||LA37_59=='|'||(LA37_59 >= '\u007F' && LA37_59 <= '\u2FFF')||(LA37_59 >= '\u3001' && LA37_59 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_59=='\\') ) {s = 36;}
						else if ( (LA37_59=='-') ) {s = 37;}
						else if ( (LA37_59=='+') ) {s = 38;}
						else if ( (LA37_59=='*'||LA37_59=='?') ) {s = 27;}
						else s = 47;
						if ( s>=0 ) return s;
						break;

					case 45 : 
						int LA37_57 = input.LA(1);
						s = -1;
						if ( ((LA37_57 >= '\u0000' && LA37_57 <= '\b')||(LA37_57 >= '\u000B' && LA37_57 <= '\f')||(LA37_57 >= '\u000E' && LA37_57 <= '\u001F')||(LA37_57 >= '#' && LA37_57 <= '&')||LA37_57==','||(LA37_57 >= '.' && LA37_57 <= '9')||(LA37_57 >= ';' && LA37_57 <= '>')||(LA37_57 >= '@' && LA37_57 <= 'Z')||(LA37_57 >= '_' && LA37_57 <= 'z')||LA37_57=='|'||(LA37_57 >= '\u007F' && LA37_57 <= '\u2FFF')||(LA37_57 >= '\u3001' && LA37_57 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_57=='\\') ) {s = 36;}
						else if ( (LA37_57=='-') ) {s = 37;}
						else if ( (LA37_57=='+') ) {s = 38;}
						else if ( (LA37_57=='*'||LA37_57=='?') ) {s = 27;}
						else s = 62;
						if ( s>=0 ) return s;
						break;

					case 46 : 
						int LA37_35 = input.LA(1);
						s = -1;
						if ( ((LA37_35 >= '\u0000' && LA37_35 <= '\b')||(LA37_35 >= '\u000B' && LA37_35 <= '\f')||(LA37_35 >= '\u000E' && LA37_35 <= '\u001F')||(LA37_35 >= '#' && LA37_35 <= '&')||LA37_35==','||(LA37_35 >= '.' && LA37_35 <= '9')||(LA37_35 >= ';' && LA37_35 <= '>')||(LA37_35 >= '@' && LA37_35 <= 'Z')||(LA37_35 >= '_' && LA37_35 <= 'z')||LA37_35=='|'||(LA37_35 >= '\u007F' && LA37_35 <= '\u2FFF')||(LA37_35 >= '\u3001' && LA37_35 <= '\uFFFF')) ) {s = 35;}
						else if ( (LA37_35=='\\') ) {s = 36;}
						else if ( (LA37_35=='-') ) {s = 37;}
						else if ( (LA37_35=='+') ) {s = 38;}
						else if ( (LA37_35=='*'||LA37_35=='?') ) {s = 27;}
						else s = 34;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 37, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
