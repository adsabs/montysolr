// $ANTLR 3.5.2 /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g 2020-01-21 16:57:09

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class ADSLexer extends Lexer {
	public static final int EOF=-1;
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
	public static final int QMARK=45;
	public static final int QNORMAL=46;
	public static final int QPHRASE=47;
	public static final int QPHRASETRUNC=48;
	public static final int QPOSITION=49;
	public static final int QRANGEEX=50;
	public static final int QRANGEIN=51;
	public static final int QREGEX=52;
	public static final int QTRUNCATED=53;
	public static final int RBRACK=54;
	public static final int REGEX=55;
	public static final int RPAREN=56;
	public static final int SEMICOLON=57;
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
	public static final int XMETA=68;

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public ADSLexer() {} 
	public ADSLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public ADSLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "/dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g"; }

	// $ANTLR start "T__69"
	public final void mT__69() throws RecognitionException {
		try {
			int _type = T__69;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:11:7: ( '#' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:11:9: '#'
			{
			match('#'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__69"

	// $ANTLR start "T__70"
	public final void mT__70() throws RecognitionException {
		try {
			int _type = T__70;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:12:7: ( '<=>' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:12:9: '<=>'
			{
			match("<=>"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__70"

	// $ANTLR start "T__71"
	public final void mT__71() throws RecognitionException {
		try {
			int _type = T__71;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:13:7: ( '=' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:13:9: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__71"

	// $ANTLR start "LPAREN"
	public final void mLPAREN() throws RecognitionException {
		try {
			int _type = LPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:343:9: ( '(' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:343:11: '('
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:345:9: ( ')' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:345:11: ')'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:347:9: ( '[' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:347:11: '['
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:349:9: ( ']' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:349:11: ']'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:351:9: ( ':' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:351:11: ':'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:353:7: ( '+' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:353:9: '+'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:355:7: ( '-' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:355:9: '-'
			{
			match('-'); 
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:357:7: ( '*' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:357:9: '*'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:359:8: ( ( '?' )+ )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:359:10: ( '?' )+
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:359:10: ( '?' )+
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:359:10: '?'
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

	// $ANTLR start "CARAT"
	public final void mCARAT() throws RecognitionException {
		try {
			int _type = CARAT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:366:7: ( '^' ( NUMBER )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:366:9: '^' ( NUMBER )?
			{
			match('^'); 
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:366:13: ( NUMBER )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:366:13: NUMBER
					{
					mNUMBER(); 

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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:368:7: ( '~' ( NUMBER )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:368:9: '~' ( NUMBER )?
			{
			match('~'); 
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:368:13: ( NUMBER )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:368:13: NUMBER
					{
					mNUMBER(); 

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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:370:9: ( '\\\"' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:370:11: '\\\"'
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

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:374:7: ( ',' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:374:9: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "SEMICOLON"
	public final void mSEMICOLON() throws RecognitionException {
		try {
			int _type = SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:376:10: ( ';' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:376:13: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SEMICOLON"

	// $ANTLR start "AS_CHAR"
	public final void mAS_CHAR() throws RecognitionException {
		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:380:3: (~ ( '0' .. '9' | ' ' | COMMA | PLUS | MINUS | '$' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
			{
			if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\u001F')||(input.LA(1) >= '!' && input.LA(1) <= '#')||(input.LA(1) >= '%' && input.LA(1) <= '*')||(input.LA(1) >= '.' && input.LA(1) <= '/')||(input.LA(1) >= ':' && input.LA(1) <= '\uFFFF') ) {
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
	// $ANTLR end "AS_CHAR"

	// $ANTLR start "ESC_CHAR"
	public final void mESC_CHAR() throws RecognitionException {
		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:385:18: ( '\\\\' . )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:385:21: '\\\\' .
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

	// $ANTLR start "TO"
	public final void mTO() throws RecognitionException {
		try {
			int _type = TO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:387:5: ( 'TO' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:387:7: 'TO'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:390:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:390:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:390:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:390:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:391:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:391:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:391:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:391:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:392:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:392:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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

	// $ANTLR start "NEAR"
	public final void mNEAR() throws RecognitionException {
		try {
			int _type = NEAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:393:7: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) ( '0' .. '9' )* )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:393:9: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) ( '0' .. '9' )*
			{
			if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:393:57: ( '0' .. '9' )*
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
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
					break loop4;
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
	// $ANTLR end "NEAR"

	// $ANTLR start "AUTHOR_SEARCH"
	public final void mAUTHOR_SEARCH() throws RecognitionException {
		try {
			int _type = AUTHOR_SEARCH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:399:3: ( '^' ( AS_CHAR )+ ( ',' ( ' ' | AS_CHAR )+ )* ( '$' )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:400:3: '^' ( AS_CHAR )+ ( ',' ( ' ' | AS_CHAR )+ )* ( '$' )?
			{
			match('^'); 
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:400:7: ( AS_CHAR )+
			int cnt5=0;
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( ((LA5_0 >= '\u0000' && LA5_0 <= '\u001F')||(LA5_0 >= '!' && LA5_0 <= '#')||(LA5_0 >= '%' && LA5_0 <= '*')||(LA5_0 >= '.' && LA5_0 <= '/')||(LA5_0 >= ':' && LA5_0 <= '\uFFFF')) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\u001F')||(input.LA(1) >= '!' && input.LA(1) <= '#')||(input.LA(1) >= '%' && input.LA(1) <= '*')||(input.LA(1) >= '.' && input.LA(1) <= '/')||(input.LA(1) >= ':' && input.LA(1) <= '\uFFFF') ) {
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
					if ( cnt5 >= 1 ) break loop5;
					EarlyExitException eee = new EarlyExitException(5, input);
					throw eee;
				}
				cnt5++;
			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:400:16: ( ',' ( ' ' | AS_CHAR )+ )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==',') ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:400:17: ',' ( ' ' | AS_CHAR )+
					{
					match(','); 
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:400:21: ( ' ' | AS_CHAR )+
					int cnt6=0;
					loop6:
					while (true) {
						int alt6=2;
						int LA6_0 = input.LA(1);
						if ( ((LA6_0 >= '\u0000' && LA6_0 <= '#')||(LA6_0 >= '%' && LA6_0 <= '*')||(LA6_0 >= '.' && LA6_0 <= '/')||(LA6_0 >= ':' && LA6_0 <= '\uFFFF')) ) {
							alt6=1;
						}

						switch (alt6) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '#')||(input.LA(1) >= '%' && input.LA(1) <= '*')||(input.LA(1) >= '.' && input.LA(1) <= '/')||(input.LA(1) >= ':' && input.LA(1) <= '\uFFFF') ) {
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

					}
					break;

				default :
					break loop7;
				}
			}

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:400:40: ( '$' )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0=='$') ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:400:40: '$'
					{
					match('$'); 
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
	// $ANTLR end "AUTHOR_SEARCH"

	// $ANTLR start "DATE_RANGE"
	public final void mDATE_RANGE() throws RecognitionException {
		try {
			int _type = DATE_RANGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:434:3: ( '-' INT INT INT INT | INT INT INT INT '-' ( INT INT INT INT )? )
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0=='-') ) {
				alt10=1;
			}
			else if ( ((LA10_0 >= '0' && LA10_0 <= '9')) ) {
				alt10=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}

			switch (alt10) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:435:3: '-' INT INT INT INT
					{
					match('-'); 
					mINT(); 

					mINT(); 

					mINT(); 

					mINT(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:436:5: INT INT INT INT '-' ( INT INT INT INT )?
					{
					mINT(); 

					mINT(); 

					mINT(); 

					mINT(); 

					match('-'); 
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:436:25: ( INT INT INT INT )?
					int alt9=2;
					int LA9_0 = input.LA(1);
					if ( ((LA9_0 >= '0' && LA9_0 <= '9')) ) {
						alt9=1;
					}
					switch (alt9) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:436:26: INT INT INT INT
							{
							mINT(); 

							mINT(); 

							mINT(); 

							mINT(); 

							}
							break;

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
	// $ANTLR end "DATE_RANGE"

	// $ANTLR start "FUNC_NAME"
	public final void mFUNC_NAME() throws RecognitionException {
		try {
			int _type = FUNC_NAME;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:448:3: ( TERM_NORMAL '(' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:449:3: TERM_NORMAL '('
			{
			mTERM_NORMAL(); 

			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FUNC_NAME"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:453:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:453:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:462:13: ( '0' .. '9' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
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

	// $ANTLR start "TERM_START_CHAR"
	public final void mTERM_START_CHAR() throws RecognitionException {
		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:467:3: ( (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | ';' | '/' ) | ESC_CHAR ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:468:3: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | ';' | '/' ) | ESC_CHAR )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:468:3: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | ';' | '/' ) | ESC_CHAR )
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( ((LA11_0 >= '\u0000' && LA11_0 <= '\b')||(LA11_0 >= '\u000B' && LA11_0 <= '\f')||(LA11_0 >= '\u000E' && LA11_0 <= '\u001F')||(LA11_0 >= '$' && LA11_0 <= '\'')||LA11_0=='.'||(LA11_0 >= '0' && LA11_0 <= '9')||LA11_0=='<'||LA11_0=='>'||(LA11_0 >= '@' && LA11_0 <= 'Z')||(LA11_0 >= '_' && LA11_0 <= 'z')||LA11_0=='|'||(LA11_0 >= '\u007F' && LA11_0 <= '\u2FFF')||(LA11_0 >= '\u3001' && LA11_0 <= '\uFFFF')) ) {
				alt11=1;
			}
			else if ( (LA11_0=='\\') ) {
				alt11=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 11, 0, input);
				throw nvae;
			}

			switch (alt11) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:468:4: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | ';' | '/' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\u001F')||(input.LA(1) >= '$' && input.LA(1) <= '\'')||input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||input.LA(1)=='<'||input.LA(1)=='>'||(input.LA(1) >= '@' && input.LA(1) <= 'Z')||(input.LA(1) >= '_' && input.LA(1) <= 'z')||input.LA(1)=='|'||(input.LA(1) >= '\u007F' && input.LA(1) <= '\u2FFF')||(input.LA(1) >= '\u3001' && input.LA(1) <= '\uFFFF') ) {
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:475:6: ESC_CHAR
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:479:3: ( ( TERM_START_CHAR | '+' | '-' | '=' | '#' | '/' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:480:3: ( TERM_START_CHAR | '+' | '-' | '=' | '#' | '/' )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:480:3: ( TERM_START_CHAR | '+' | '-' | '=' | '#' | '/' )
			int alt12=6;
			int LA12_0 = input.LA(1);
			if ( ((LA12_0 >= '\u0000' && LA12_0 <= '\b')||(LA12_0 >= '\u000B' && LA12_0 <= '\f')||(LA12_0 >= '\u000E' && LA12_0 <= '\u001F')||(LA12_0 >= '$' && LA12_0 <= '\'')||LA12_0=='.'||(LA12_0 >= '0' && LA12_0 <= '9')||LA12_0=='<'||LA12_0=='>'||(LA12_0 >= '@' && LA12_0 <= 'Z')||LA12_0=='\\'||(LA12_0 >= '_' && LA12_0 <= 'z')||LA12_0=='|'||(LA12_0 >= '\u007F' && LA12_0 <= '\u2FFF')||(LA12_0 >= '\u3001' && LA12_0 <= '\uFFFF')) ) {
				alt12=1;
			}
			else if ( (LA12_0=='+') ) {
				alt12=2;
			}
			else if ( (LA12_0=='-') ) {
				alt12=3;
			}
			else if ( (LA12_0=='=') ) {
				alt12=4;
			}
			else if ( (LA12_0=='#') ) {
				alt12=5;
			}
			else if ( (LA12_0=='/') ) {
				alt12=6;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 12, 0, input);
				throw nvae;
			}

			switch (alt12) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:480:4: TERM_START_CHAR
					{
					mTERM_START_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:480:23: '+'
					{
					match('+'); 
					}
					break;
				case 3 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:480:29: '-'
					{
					match('-'); 
					}
					break;
				case 4 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:480:35: '='
					{
					match('='); 
					}
					break;
				case 5 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:480:41: '#'
					{
					match('#'); 
					}
					break;
				case 6 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:480:47: '/'
					{
					match('/'); 
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

	// $ANTLR start "DATE_TOKEN"
	public final void mDATE_TOKEN() throws RecognitionException {
		try {
			int _type = DATE_TOKEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:486:3: ( INT ( INT )? ( '/' | MINUS | '.' ) INT ( INT )? ( '/' | MINUS | '.' ) INT INT ( INT INT )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:487:3: INT ( INT )? ( '/' | MINUS | '.' ) INT ( INT )? ( '/' | MINUS | '.' ) INT INT ( INT INT )?
			{
			mINT(); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:487:7: ( INT )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( ((LA13_0 >= '0' && LA13_0 <= '9')) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
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

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:487:32: ( INT )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( ((LA14_0 >= '0' && LA14_0 <= '9')) ) {
				alt14=1;
			}
			switch (alt14) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
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

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:487:61: ( INT INT )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( ((LA15_0 >= '0' && LA15_0 <= '9')) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:487:62: INT INT
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

	// $ANTLR start "NUMBER"
	public final void mNUMBER() throws RecognitionException {
		try {
			int _type = NUMBER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:491:3: ( ( INT )+ ( '.' ( INT )+ )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:492:3: ( INT )+ ( '.' ( INT )+ )?
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:492:3: ( INT )+
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
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

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:492:8: ( '.' ( INT )+ )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0=='.') ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:492:9: '.' ( INT )+
					{
					match('.'); 
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:492:13: ( INT )+
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
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:
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

	// $ANTLR start "M_NUMBER"
	public final void mM_NUMBER() throws RecognitionException {
		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:495:18: ( NUMBER 'm' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:496:3: NUMBER 'm'
			{
			mNUMBER(); 

			match('m'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "M_NUMBER"

	// $ANTLR start "H_NUMBER"
	public final void mH_NUMBER() throws RecognitionException {
		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:498:18: ( NUMBER 'h' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:499:3: NUMBER 'h'
			{
			mNUMBER(); 

			match('h'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "H_NUMBER"

	// $ANTLR start "D_NUMBER"
	public final void mD_NUMBER() throws RecognitionException {
		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:501:18: ( NUMBER 'd' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:502:3: NUMBER 'd'
			{
			mNUMBER(); 

			match('d'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "D_NUMBER"

	// $ANTLR start "S_NUMBER"
	public final void mS_NUMBER() throws RecognitionException {
		try {
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:504:18: ( NUMBER 's' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:505:3: NUMBER 's'
			{
			mNUMBER(); 

			match('s'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "S_NUMBER"

	// $ANTLR start "HOUR"
	public final void mHOUR() throws RecognitionException {
		try {
			int _type = HOUR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:508:3: ( INT INT COLON INT INT COLON NUMBER ( PLUS | MINUS ) INT INT COLON INT INT COLON NUMBER )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:509:3: INT INT COLON INT INT COLON NUMBER ( PLUS | MINUS ) INT INT COLON INT INT COLON NUMBER
			{
			mINT(); 

			mINT(); 

			mCOLON(); 

			mINT(); 

			mINT(); 

			mCOLON(); 

			mNUMBER(); 

			if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			mINT(); 

			mINT(); 

			mCOLON(); 

			mINT(); 

			mINT(); 

			mCOLON(); 

			mNUMBER(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "HOUR"

	// $ANTLR start "TERM_NORMAL"
	public final void mTERM_NORMAL() throws RecognitionException {
		try {
			int _type = TERM_NORMAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:513:3: ( TERM_START_CHAR ( TERM_CHAR )* )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:514:3: TERM_START_CHAR ( TERM_CHAR )*
			{
			mTERM_START_CHAR(); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:514:19: ( TERM_CHAR )*
			loop19:
			while (true) {
				int alt19=2;
				int LA19_0 = input.LA(1);
				if ( ((LA19_0 >= '\u0000' && LA19_0 <= '\b')||(LA19_0 >= '\u000B' && LA19_0 <= '\f')||(LA19_0 >= '\u000E' && LA19_0 <= '\u001F')||(LA19_0 >= '#' && LA19_0 <= '\'')||LA19_0=='+'||(LA19_0 >= '-' && LA19_0 <= '9')||(LA19_0 >= '<' && LA19_0 <= '>')||(LA19_0 >= '@' && LA19_0 <= 'Z')||LA19_0=='\\'||(LA19_0 >= '_' && LA19_0 <= 'z')||LA19_0=='|'||(LA19_0 >= '\u007F' && LA19_0 <= '\u2FFF')||(LA19_0 >= '\u3001' && LA19_0 <= '\uFFFF')) ) {
					alt19=1;
				}

				switch (alt19) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:514:21: TERM_CHAR
					{
					mTERM_CHAR(); 

					}
					break;

				default :
					break loop19;
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:518:15: ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ )
			int alt31=3;
			alt31 = dfa31.predict(input);
			switch (alt31) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:3: ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )*
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:3: ( STAR | QMARK )
					int alt20=2;
					int LA20_0 = input.LA(1);
					if ( (LA20_0=='*') ) {
						alt20=1;
					}
					else if ( (LA20_0=='?') ) {
						alt20=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 20, 0, input);
						throw nvae;
					}

					switch (alt20) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:4: STAR
							{
							mSTAR(); 

							}
							break;
						case 2 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:9: QMARK
							{
							mQMARK(); 

							}
							break;

					}

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:16: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+
					int cnt23=0;
					loop23:
					while (true) {
						int alt23=2;
						alt23 = dfa23.predict(input);
						switch (alt23) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:17: ( TERM_CHAR )+ ( QMARK | STAR )
							{
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:17: ( TERM_CHAR )+
							int cnt21=0;
							loop21:
							while (true) {
								int alt21=2;
								int LA21_0 = input.LA(1);
								if ( ((LA21_0 >= '\u0000' && LA21_0 <= '\b')||(LA21_0 >= '\u000B' && LA21_0 <= '\f')||(LA21_0 >= '\u000E' && LA21_0 <= '\u001F')||(LA21_0 >= '#' && LA21_0 <= '\'')||LA21_0=='+'||(LA21_0 >= '-' && LA21_0 <= '9')||(LA21_0 >= '<' && LA21_0 <= '>')||(LA21_0 >= '@' && LA21_0 <= 'Z')||LA21_0=='\\'||(LA21_0 >= '_' && LA21_0 <= 'z')||LA21_0=='|'||(LA21_0 >= '\u007F' && LA21_0 <= '\u2FFF')||(LA21_0 >= '\u3001' && LA21_0 <= '\uFFFF')) ) {
									alt21=1;
								}

								switch (alt21) {
								case 1 :
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:17: TERM_CHAR
									{
									mTERM_CHAR(); 

									}
									break;

								default :
									if ( cnt21 >= 1 ) break loop21;
									EarlyExitException eee = new EarlyExitException(21, input);
									throw eee;
								}
								cnt21++;
							}

							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:28: ( QMARK | STAR )
							int alt22=2;
							int LA22_0 = input.LA(1);
							if ( (LA22_0=='?') ) {
								alt22=1;
							}
							else if ( (LA22_0=='*') ) {
								alt22=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 22, 0, input);
								throw nvae;
							}

							switch (alt22) {
								case 1 :
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:29: QMARK
									{
									mQMARK(); 

									}
									break;
								case 2 :
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:35: STAR
									{
									mSTAR(); 

									}
									break;

							}

							}
							break;

						default :
							if ( cnt23 >= 1 ) break loop23;
							EarlyExitException eee = new EarlyExitException(23, input);
							throw eee;
						}
						cnt23++;
					}

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:43: ( TERM_CHAR )*
					loop24:
					while (true) {
						int alt24=2;
						int LA24_0 = input.LA(1);
						if ( ((LA24_0 >= '\u0000' && LA24_0 <= '\b')||(LA24_0 >= '\u000B' && LA24_0 <= '\f')||(LA24_0 >= '\u000E' && LA24_0 <= '\u001F')||(LA24_0 >= '#' && LA24_0 <= '\'')||LA24_0=='+'||(LA24_0 >= '-' && LA24_0 <= '9')||(LA24_0 >= '<' && LA24_0 <= '>')||(LA24_0 >= '@' && LA24_0 <= 'Z')||LA24_0=='\\'||(LA24_0 >= '_' && LA24_0 <= 'z')||LA24_0=='|'||(LA24_0 >= '\u007F' && LA24_0 <= '\u2FFF')||(LA24_0 >= '\u3001' && LA24_0 <= '\uFFFF')) ) {
							alt24=1;
						}

						switch (alt24) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:519:44: TERM_CHAR
							{
							mTERM_CHAR(); 

							}
							break;

						default :
							break loop24;
						}
					}

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:5: TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )*
					{
					mTERM_START_CHAR(); 

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:21: ( ( TERM_CHAR )* ( QMARK | STAR ) )+
					int cnt27=0;
					loop27:
					while (true) {
						int alt27=2;
						alt27 = dfa27.predict(input);
						switch (alt27) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:22: ( TERM_CHAR )* ( QMARK | STAR )
							{
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:22: ( TERM_CHAR )*
							loop25:
							while (true) {
								int alt25=2;
								int LA25_0 = input.LA(1);
								if ( ((LA25_0 >= '\u0000' && LA25_0 <= '\b')||(LA25_0 >= '\u000B' && LA25_0 <= '\f')||(LA25_0 >= '\u000E' && LA25_0 <= '\u001F')||(LA25_0 >= '#' && LA25_0 <= '\'')||LA25_0=='+'||(LA25_0 >= '-' && LA25_0 <= '9')||(LA25_0 >= '<' && LA25_0 <= '>')||(LA25_0 >= '@' && LA25_0 <= 'Z')||LA25_0=='\\'||(LA25_0 >= '_' && LA25_0 <= 'z')||LA25_0=='|'||(LA25_0 >= '\u007F' && LA25_0 <= '\u2FFF')||(LA25_0 >= '\u3001' && LA25_0 <= '\uFFFF')) ) {
									alt25=1;
								}

								switch (alt25) {
								case 1 :
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:22: TERM_CHAR
									{
									mTERM_CHAR(); 

									}
									break;

								default :
									break loop25;
								}
							}

							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:33: ( QMARK | STAR )
							int alt26=2;
							int LA26_0 = input.LA(1);
							if ( (LA26_0=='?') ) {
								alt26=1;
							}
							else if ( (LA26_0=='*') ) {
								alt26=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 26, 0, input);
								throw nvae;
							}

							switch (alt26) {
								case 1 :
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:34: QMARK
									{
									mQMARK(); 

									}
									break;
								case 2 :
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:40: STAR
									{
									mSTAR(); 

									}
									break;

							}

							}
							break;

						default :
							if ( cnt27 >= 1 ) break loop27;
							EarlyExitException eee = new EarlyExitException(27, input);
							throw eee;
						}
						cnt27++;
					}

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:48: ( TERM_CHAR )*
					loop28:
					while (true) {
						int alt28=2;
						int LA28_0 = input.LA(1);
						if ( ((LA28_0 >= '\u0000' && LA28_0 <= '\b')||(LA28_0 >= '\u000B' && LA28_0 <= '\f')||(LA28_0 >= '\u000E' && LA28_0 <= '\u001F')||(LA28_0 >= '#' && LA28_0 <= '\'')||LA28_0=='+'||(LA28_0 >= '-' && LA28_0 <= '9')||(LA28_0 >= '<' && LA28_0 <= '>')||(LA28_0 >= '@' && LA28_0 <= 'Z')||LA28_0=='\\'||(LA28_0 >= '_' && LA28_0 <= 'z')||LA28_0=='|'||(LA28_0 >= '\u007F' && LA28_0 <= '\u2FFF')||(LA28_0 >= '\u3001' && LA28_0 <= '\uFFFF')) ) {
							alt28=1;
						}

						switch (alt28) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:49: TERM_CHAR
							{
							mTERM_CHAR(); 

							}
							break;

						default :
							break loop28;
						}
					}

					}
					break;
				case 3 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:521:5: ( STAR | QMARK ) ( TERM_CHAR )+
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:521:5: ( STAR | QMARK )
					int alt29=2;
					int LA29_0 = input.LA(1);
					if ( (LA29_0=='*') ) {
						alt29=1;
					}
					else if ( (LA29_0=='?') ) {
						alt29=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 29, 0, input);
						throw nvae;
					}

					switch (alt29) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:521:6: STAR
							{
							mSTAR(); 

							}
							break;
						case 2 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:521:11: QMARK
							{
							mQMARK(); 

							}
							break;

					}

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:521:18: ( TERM_CHAR )+
					int cnt30=0;
					loop30:
					while (true) {
						int alt30=2;
						int LA30_0 = input.LA(1);
						if ( ((LA30_0 >= '\u0000' && LA30_0 <= '\b')||(LA30_0 >= '\u000B' && LA30_0 <= '\f')||(LA30_0 >= '\u000E' && LA30_0 <= '\u001F')||(LA30_0 >= '#' && LA30_0 <= '\'')||LA30_0=='+'||(LA30_0 >= '-' && LA30_0 <= '9')||(LA30_0 >= '<' && LA30_0 <= '>')||(LA30_0 >= '@' && LA30_0 <= 'Z')||LA30_0=='\\'||(LA30_0 >= '_' && LA30_0 <= 'z')||LA30_0=='|'||(LA30_0 >= '\u007F' && LA30_0 <= '\u2FFF')||(LA30_0 >= '\u3001' && LA30_0 <= '\uFFFF')) ) {
							alt30=1;
						}

						switch (alt30) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:521:18: TERM_CHAR
							{
							mTERM_CHAR(); 

							}
							break;

						default :
							if ( cnt30 >= 1 ) break loop30;
							EarlyExitException eee = new EarlyExitException(30, input);
							throw eee;
						}
						cnt30++;
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:526:3: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:527:3: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE
			{
			mDQUOTE(); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:527:10: ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+
			int cnt32=0;
			loop32:
			while (true) {
				int alt32=3;
				int LA32_0 = input.LA(1);
				if ( (LA32_0=='\\') ) {
					alt32=1;
				}
				else if ( ((LA32_0 >= '\u0000' && LA32_0 <= '!')||(LA32_0 >= '#' && LA32_0 <= ')')||(LA32_0 >= '+' && LA32_0 <= '>')||(LA32_0 >= '@' && LA32_0 <= '[')||(LA32_0 >= ']' && LA32_0 <= '\uFFFF')) ) {
					alt32=2;
				}

				switch (alt32) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:527:11: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:527:20: ~ ( '\\\"' | '\\\\' | '?' | '*' )
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
					if ( cnt32 >= 1 ) break loop32;
					EarlyExitException eee = new EarlyExitException(32, input);
					throw eee;
				}
				cnt32++;
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:530:17: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:531:3: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE
			{
			mDQUOTE(); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:531:10: ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+
			int cnt33=0;
			loop33:
			while (true) {
				int alt33=3;
				int LA33_0 = input.LA(1);
				if ( (LA33_0=='\\') ) {
					alt33=1;
				}
				else if ( ((LA33_0 >= '\u0000' && LA33_0 <= '!')||(LA33_0 >= '#' && LA33_0 <= '[')||(LA33_0 >= ']' && LA33_0 <= '\uFFFF')) ) {
					alt33=2;
				}

				switch (alt33) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:531:11: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:531:20: ~ ( '\\\"' | '\\\\' )
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
					if ( cnt33 >= 1 ) break loop33;
					EarlyExitException eee = new EarlyExitException(33, input);
					throw eee;
				}
				cnt33++;
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

	// $ANTLR start "LOCAL_PARAMS"
	public final void mLOCAL_PARAMS() throws RecognitionException {
		try {
			int _type = LOCAL_PARAMS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:534:15: ( '{!' ( ESC_CHAR |~ ( '}' | '\\\\' ) )+ '}' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:3: '{!' ( ESC_CHAR |~ ( '}' | '\\\\' ) )+ '}'
			{
			match("{!"); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:8: ( ESC_CHAR |~ ( '}' | '\\\\' ) )+
			int cnt34=0;
			loop34:
			while (true) {
				int alt34=3;
				int LA34_0 = input.LA(1);
				if ( (LA34_0=='\\') ) {
					alt34=1;
				}
				else if ( ((LA34_0 >= '\u0000' && LA34_0 <= '[')||(LA34_0 >= ']' && LA34_0 <= '|')||(LA34_0 >= '~' && LA34_0 <= '\uFFFF')) ) {
					alt34=2;
				}

				switch (alt34) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:9: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:18: ~ ( '}' | '\\\\' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '|')||(input.LA(1) >= '~' && input.LA(1) <= '\uFFFF') ) {
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
					if ( cnt34 >= 1 ) break loop34;
					EarlyExitException eee = new EarlyExitException(34, input);
					throw eee;
				}
				cnt34++;
			}

			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LOCAL_PARAMS"

	// $ANTLR start "REGEX"
	public final void mREGEX() throws RecognitionException {
		try {
			int _type = REGEX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:537:7: ( '/' ( ESC_CHAR |~ ( '/' | '\\\\' ) )+ '/' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:538:3: '/' ( ESC_CHAR |~ ( '/' | '\\\\' ) )+ '/'
			{
			match('/'); 
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:538:7: ( ESC_CHAR |~ ( '/' | '\\\\' ) )+
			int cnt35=0;
			loop35:
			while (true) {
				int alt35=3;
				int LA35_0 = input.LA(1);
				if ( (LA35_0=='\\') ) {
					alt35=1;
				}
				else if ( ((LA35_0 >= '\u0000' && LA35_0 <= '.')||(LA35_0 >= '0' && LA35_0 <= '[')||(LA35_0 >= ']' && LA35_0 <= '\uFFFF')) ) {
					alt35=2;
				}

				switch (alt35) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:538:8: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:538:17: ~ ( '/' | '\\\\' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
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

			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "REGEX"

	@Override
	public void mTokens() throws RecognitionException {
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:8: ( T__69 | T__70 | T__71 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | CARAT | TILDE | DQUOTE | COMMA | SEMICOLON | TO | AND | OR | NOT | NEAR | AUTHOR_SEARCH | DATE_RANGE | FUNC_NAME | WS | DATE_TOKEN | NUMBER | HOUR | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING | LOCAL_PARAMS | REGEX )
		int alt36=35;
		alt36 = dfa36.predict(input);
		switch (alt36) {
			case 1 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:10: T__69
				{
				mT__69(); 

				}
				break;
			case 2 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:16: T__70
				{
				mT__70(); 

				}
				break;
			case 3 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:22: T__71
				{
				mT__71(); 

				}
				break;
			case 4 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:28: LPAREN
				{
				mLPAREN(); 

				}
				break;
			case 5 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:35: RPAREN
				{
				mRPAREN(); 

				}
				break;
			case 6 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:42: LBRACK
				{
				mLBRACK(); 

				}
				break;
			case 7 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:49: RBRACK
				{
				mRBRACK(); 

				}
				break;
			case 8 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:56: COLON
				{
				mCOLON(); 

				}
				break;
			case 9 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:62: PLUS
				{
				mPLUS(); 

				}
				break;
			case 10 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:67: MINUS
				{
				mMINUS(); 

				}
				break;
			case 11 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:73: STAR
				{
				mSTAR(); 

				}
				break;
			case 12 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:78: QMARK
				{
				mQMARK(); 

				}
				break;
			case 13 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:84: CARAT
				{
				mCARAT(); 

				}
				break;
			case 14 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:90: TILDE
				{
				mTILDE(); 

				}
				break;
			case 15 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:96: DQUOTE
				{
				mDQUOTE(); 

				}
				break;
			case 16 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:103: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 17 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:109: SEMICOLON
				{
				mSEMICOLON(); 

				}
				break;
			case 18 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:119: TO
				{
				mTO(); 

				}
				break;
			case 19 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:122: AND
				{
				mAND(); 

				}
				break;
			case 20 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:126: OR
				{
				mOR(); 

				}
				break;
			case 21 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:129: NOT
				{
				mNOT(); 

				}
				break;
			case 22 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:133: NEAR
				{
				mNEAR(); 

				}
				break;
			case 23 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:138: AUTHOR_SEARCH
				{
				mAUTHOR_SEARCH(); 

				}
				break;
			case 24 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:152: DATE_RANGE
				{
				mDATE_RANGE(); 

				}
				break;
			case 25 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:163: FUNC_NAME
				{
				mFUNC_NAME(); 

				}
				break;
			case 26 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:173: WS
				{
				mWS(); 

				}
				break;
			case 27 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:176: DATE_TOKEN
				{
				mDATE_TOKEN(); 

				}
				break;
			case 28 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:187: NUMBER
				{
				mNUMBER(); 

				}
				break;
			case 29 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:194: HOUR
				{
				mHOUR(); 

				}
				break;
			case 30 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:199: TERM_NORMAL
				{
				mTERM_NORMAL(); 

				}
				break;
			case 31 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:211: TERM_TRUNCATED
				{
				mTERM_TRUNCATED(); 

				}
				break;
			case 32 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:226: PHRASE
				{
				mPHRASE(); 

				}
				break;
			case 33 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:233: PHRASE_ANYTHING
				{
				mPHRASE_ANYTHING(); 

				}
				break;
			case 34 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:249: LOCAL_PARAMS
				{
				mLOCAL_PARAMS(); 

				}
				break;
			case 35 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:262: REGEX
				{
				mREGEX(); 

				}
				break;

		}
	}


	protected DFA31 dfa31 = new DFA31(this);
	protected DFA23 dfa23 = new DFA23(this);
	protected DFA27 dfa27 = new DFA27(this);
	protected DFA36 dfa36 = new DFA36(this);
	static final String DFA31_eotS =
		"\4\uffff\1\13\1\uffff\5\13\2\uffff\1\13";
	static final String DFA31_eofS =
		"\16\uffff";
	static final String DFA31_minS =
		"\3\0\1\uffff\7\0\2\uffff\1\0";
	static final String DFA31_maxS =
		"\3\uffff\1\uffff\7\uffff\2\uffff\1\uffff";
	static final String DFA31_acceptS =
		"\3\uffff\1\2\7\uffff\1\3\1\1\1\uffff";
	static final String DFA31_specialS =
		"\1\11\1\1\1\10\1\uffff\1\3\1\4\1\5\1\6\1\7\1\12\1\0\2\uffff\1\2}>";
	static final String[] DFA31_transitionS = {
			"\11\3\2\uffff\2\3\1\uffff\22\3\4\uffff\4\3\2\uffff\1\1\3\uffff\1\3\1"+
			"\uffff\12\3\2\uffff\1\3\1\uffff\1\3\1\2\33\3\1\uffff\1\3\2\uffff\34\3"+
			"\1\uffff\1\3\2\uffff\u2f81\3\1\uffff\ucfff\3",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\11\4\4\3\uffff\1\6\1\uffff"+
			"\1\7\1\4\1\12\12\4\2\uffff\1\4\1\10\1\4\1\uffff\33\4\1\uffff\1\5\2\uffff"+
			"\34\4\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\11\4\4\3\uffff\1\6\1\uffff"+
			"\1\7\1\4\1\12\12\4\2\uffff\1\4\1\10\1\4\1\2\33\4\1\uffff\1\5\2\uffff"+
			"\34\4\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
			"",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\11\4\4\2\uffff\1\14\1\6\1\uffff"+
			"\1\7\1\4\1\12\12\4\2\uffff\1\4\1\10\1\4\1\14\33\4\1\uffff\1\5\2\uffff"+
			"\34\4\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
			"\0\15",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\11\4\4\2\uffff\1\14\1\6\1\uffff"+
			"\1\7\1\4\1\12\12\4\2\uffff\1\4\1\10\1\4\1\14\33\4\1\uffff\1\5\2\uffff"+
			"\34\4\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\11\4\4\2\uffff\1\14\1\6\1\uffff"+
			"\1\7\1\4\1\12\12\4\2\uffff\1\4\1\10\1\4\1\14\33\4\1\uffff\1\5\2\uffff"+
			"\34\4\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\11\4\4\2\uffff\1\14\1\6\1\uffff"+
			"\1\7\1\4\1\12\12\4\2\uffff\1\4\1\10\1\4\1\14\33\4\1\uffff\1\5\2\uffff"+
			"\34\4\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\11\4\4\2\uffff\1\14\1\6\1\uffff"+
			"\1\7\1\4\1\12\12\4\2\uffff\1\4\1\10\1\4\1\14\33\4\1\uffff\1\5\2\uffff"+
			"\34\4\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\11\4\4\2\uffff\1\14\1\6\1\uffff"+
			"\1\7\1\4\1\12\12\4\2\uffff\1\4\1\10\1\4\1\14\33\4\1\uffff\1\5\2\uffff"+
			"\34\4\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
			"",
			"",
			"\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\11\4\4\2\uffff\1\14\1\6\1\uffff"+
			"\1\7\1\4\1\12\12\4\2\uffff\1\4\1\10\1\4\1\14\33\4\1\uffff\1\5\2\uffff"+
			"\34\4\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4"
	};

	static final short[] DFA31_eot = DFA.unpackEncodedString(DFA31_eotS);
	static final short[] DFA31_eof = DFA.unpackEncodedString(DFA31_eofS);
	static final char[] DFA31_min = DFA.unpackEncodedStringToUnsignedChars(DFA31_minS);
	static final char[] DFA31_max = DFA.unpackEncodedStringToUnsignedChars(DFA31_maxS);
	static final short[] DFA31_accept = DFA.unpackEncodedString(DFA31_acceptS);
	static final short[] DFA31_special = DFA.unpackEncodedString(DFA31_specialS);
	static final short[][] DFA31_transition;

	static {
		int numStates = DFA31_transitionS.length;
		DFA31_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA31_transition[i] = DFA.unpackEncodedString(DFA31_transitionS[i]);
		}
	}

	protected class DFA31 extends DFA {

		public DFA31(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 31;
			this.eot = DFA31_eot;
			this.eof = DFA31_eof;
			this.min = DFA31_min;
			this.max = DFA31_max;
			this.accept = DFA31_accept;
			this.special = DFA31_special;
			this.transition = DFA31_transition;
		}
		@Override
		public String getDescription() {
			return "518:1: TERM_TRUNCATED : ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA31_10 = input.LA(1);
						s = -1;
						if ( (LA31_10=='*'||LA31_10=='?') ) {s = 12;}
						else if ( ((LA31_10 >= '\u0000' && LA31_10 <= '\b')||(LA31_10 >= '\u000B' && LA31_10 <= '\f')||(LA31_10 >= '\u000E' && LA31_10 <= '\u001F')||(LA31_10 >= '$' && LA31_10 <= '\'')||LA31_10=='.'||(LA31_10 >= '0' && LA31_10 <= '9')||LA31_10=='<'||LA31_10=='>'||(LA31_10 >= '@' && LA31_10 <= 'Z')||(LA31_10 >= '_' && LA31_10 <= 'z')||LA31_10=='|'||(LA31_10 >= '\u007F' && LA31_10 <= '\u2FFF')||(LA31_10 >= '\u3001' && LA31_10 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA31_10=='\\') ) {s = 5;}
						else if ( (LA31_10=='+') ) {s = 6;}
						else if ( (LA31_10=='-') ) {s = 7;}
						else if ( (LA31_10=='=') ) {s = 8;}
						else if ( (LA31_10=='#') ) {s = 9;}
						else if ( (LA31_10=='/') ) {s = 10;}
						else s = 11;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA31_1 = input.LA(1);
						s = -1;
						if ( ((LA31_1 >= '\u0000' && LA31_1 <= '\b')||(LA31_1 >= '\u000B' && LA31_1 <= '\f')||(LA31_1 >= '\u000E' && LA31_1 <= '\u001F')||(LA31_1 >= '$' && LA31_1 <= '\'')||LA31_1=='.'||(LA31_1 >= '0' && LA31_1 <= '9')||LA31_1=='<'||LA31_1=='>'||(LA31_1 >= '@' && LA31_1 <= 'Z')||(LA31_1 >= '_' && LA31_1 <= 'z')||LA31_1=='|'||(LA31_1 >= '\u007F' && LA31_1 <= '\u2FFF')||(LA31_1 >= '\u3001' && LA31_1 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA31_1=='\\') ) {s = 5;}
						else if ( (LA31_1=='+') ) {s = 6;}
						else if ( (LA31_1=='-') ) {s = 7;}
						else if ( (LA31_1=='=') ) {s = 8;}
						else if ( (LA31_1=='#') ) {s = 9;}
						else if ( (LA31_1=='/') ) {s = 10;}
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA31_13 = input.LA(1);
						s = -1;
						if ( (LA31_13=='*'||LA31_13=='?') ) {s = 12;}
						else if ( ((LA31_13 >= '\u0000' && LA31_13 <= '\b')||(LA31_13 >= '\u000B' && LA31_13 <= '\f')||(LA31_13 >= '\u000E' && LA31_13 <= '\u001F')||(LA31_13 >= '$' && LA31_13 <= '\'')||LA31_13=='.'||(LA31_13 >= '0' && LA31_13 <= '9')||LA31_13=='<'||LA31_13=='>'||(LA31_13 >= '@' && LA31_13 <= 'Z')||(LA31_13 >= '_' && LA31_13 <= 'z')||LA31_13=='|'||(LA31_13 >= '\u007F' && LA31_13 <= '\u2FFF')||(LA31_13 >= '\u3001' && LA31_13 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA31_13=='\\') ) {s = 5;}
						else if ( (LA31_13=='+') ) {s = 6;}
						else if ( (LA31_13=='-') ) {s = 7;}
						else if ( (LA31_13=='=') ) {s = 8;}
						else if ( (LA31_13=='#') ) {s = 9;}
						else if ( (LA31_13=='/') ) {s = 10;}
						else s = 11;
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA31_4 = input.LA(1);
						s = -1;
						if ( (LA31_4=='*'||LA31_4=='?') ) {s = 12;}
						else if ( ((LA31_4 >= '\u0000' && LA31_4 <= '\b')||(LA31_4 >= '\u000B' && LA31_4 <= '\f')||(LA31_4 >= '\u000E' && LA31_4 <= '\u001F')||(LA31_4 >= '$' && LA31_4 <= '\'')||LA31_4=='.'||(LA31_4 >= '0' && LA31_4 <= '9')||LA31_4=='<'||LA31_4=='>'||(LA31_4 >= '@' && LA31_4 <= 'Z')||(LA31_4 >= '_' && LA31_4 <= 'z')||LA31_4=='|'||(LA31_4 >= '\u007F' && LA31_4 <= '\u2FFF')||(LA31_4 >= '\u3001' && LA31_4 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA31_4=='\\') ) {s = 5;}
						else if ( (LA31_4=='+') ) {s = 6;}
						else if ( (LA31_4=='-') ) {s = 7;}
						else if ( (LA31_4=='=') ) {s = 8;}
						else if ( (LA31_4=='#') ) {s = 9;}
						else if ( (LA31_4=='/') ) {s = 10;}
						else s = 11;
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA31_5 = input.LA(1);
						s = -1;
						if ( ((LA31_5 >= '\u0000' && LA31_5 <= '\uFFFF')) ) {s = 13;}
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA31_6 = input.LA(1);
						s = -1;
						if ( (LA31_6=='*'||LA31_6=='?') ) {s = 12;}
						else if ( ((LA31_6 >= '\u0000' && LA31_6 <= '\b')||(LA31_6 >= '\u000B' && LA31_6 <= '\f')||(LA31_6 >= '\u000E' && LA31_6 <= '\u001F')||(LA31_6 >= '$' && LA31_6 <= '\'')||LA31_6=='.'||(LA31_6 >= '0' && LA31_6 <= '9')||LA31_6=='<'||LA31_6=='>'||(LA31_6 >= '@' && LA31_6 <= 'Z')||(LA31_6 >= '_' && LA31_6 <= 'z')||LA31_6=='|'||(LA31_6 >= '\u007F' && LA31_6 <= '\u2FFF')||(LA31_6 >= '\u3001' && LA31_6 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA31_6=='\\') ) {s = 5;}
						else if ( (LA31_6=='+') ) {s = 6;}
						else if ( (LA31_6=='-') ) {s = 7;}
						else if ( (LA31_6=='=') ) {s = 8;}
						else if ( (LA31_6=='#') ) {s = 9;}
						else if ( (LA31_6=='/') ) {s = 10;}
						else s = 11;
						if ( s>=0 ) return s;
						break;

					case 6 : 
						int LA31_7 = input.LA(1);
						s = -1;
						if ( (LA31_7=='*'||LA31_7=='?') ) {s = 12;}
						else if ( ((LA31_7 >= '\u0000' && LA31_7 <= '\b')||(LA31_7 >= '\u000B' && LA31_7 <= '\f')||(LA31_7 >= '\u000E' && LA31_7 <= '\u001F')||(LA31_7 >= '$' && LA31_7 <= '\'')||LA31_7=='.'||(LA31_7 >= '0' && LA31_7 <= '9')||LA31_7=='<'||LA31_7=='>'||(LA31_7 >= '@' && LA31_7 <= 'Z')||(LA31_7 >= '_' && LA31_7 <= 'z')||LA31_7=='|'||(LA31_7 >= '\u007F' && LA31_7 <= '\u2FFF')||(LA31_7 >= '\u3001' && LA31_7 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA31_7=='\\') ) {s = 5;}
						else if ( (LA31_7=='+') ) {s = 6;}
						else if ( (LA31_7=='-') ) {s = 7;}
						else if ( (LA31_7=='=') ) {s = 8;}
						else if ( (LA31_7=='#') ) {s = 9;}
						else if ( (LA31_7=='/') ) {s = 10;}
						else s = 11;
						if ( s>=0 ) return s;
						break;

					case 7 : 
						int LA31_8 = input.LA(1);
						s = -1;
						if ( (LA31_8=='*'||LA31_8=='?') ) {s = 12;}
						else if ( ((LA31_8 >= '\u0000' && LA31_8 <= '\b')||(LA31_8 >= '\u000B' && LA31_8 <= '\f')||(LA31_8 >= '\u000E' && LA31_8 <= '\u001F')||(LA31_8 >= '$' && LA31_8 <= '\'')||LA31_8=='.'||(LA31_8 >= '0' && LA31_8 <= '9')||LA31_8=='<'||LA31_8=='>'||(LA31_8 >= '@' && LA31_8 <= 'Z')||(LA31_8 >= '_' && LA31_8 <= 'z')||LA31_8=='|'||(LA31_8 >= '\u007F' && LA31_8 <= '\u2FFF')||(LA31_8 >= '\u3001' && LA31_8 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA31_8=='\\') ) {s = 5;}
						else if ( (LA31_8=='+') ) {s = 6;}
						else if ( (LA31_8=='-') ) {s = 7;}
						else if ( (LA31_8=='=') ) {s = 8;}
						else if ( (LA31_8=='#') ) {s = 9;}
						else if ( (LA31_8=='/') ) {s = 10;}
						else s = 11;
						if ( s>=0 ) return s;
						break;

					case 8 : 
						int LA31_2 = input.LA(1);
						s = -1;
						if ( ((LA31_2 >= '\u0000' && LA31_2 <= '\b')||(LA31_2 >= '\u000B' && LA31_2 <= '\f')||(LA31_2 >= '\u000E' && LA31_2 <= '\u001F')||(LA31_2 >= '$' && LA31_2 <= '\'')||LA31_2=='.'||(LA31_2 >= '0' && LA31_2 <= '9')||LA31_2=='<'||LA31_2=='>'||(LA31_2 >= '@' && LA31_2 <= 'Z')||(LA31_2 >= '_' && LA31_2 <= 'z')||LA31_2=='|'||(LA31_2 >= '\u007F' && LA31_2 <= '\u2FFF')||(LA31_2 >= '\u3001' && LA31_2 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA31_2=='\\') ) {s = 5;}
						else if ( (LA31_2=='+') ) {s = 6;}
						else if ( (LA31_2=='-') ) {s = 7;}
						else if ( (LA31_2=='=') ) {s = 8;}
						else if ( (LA31_2=='#') ) {s = 9;}
						else if ( (LA31_2=='/') ) {s = 10;}
						else if ( (LA31_2=='?') ) {s = 2;}
						if ( s>=0 ) return s;
						break;

					case 9 : 
						int LA31_0 = input.LA(1);
						s = -1;
						if ( (LA31_0=='*') ) {s = 1;}
						else if ( (LA31_0=='?') ) {s = 2;}
						else if ( ((LA31_0 >= '\u0000' && LA31_0 <= '\b')||(LA31_0 >= '\u000B' && LA31_0 <= '\f')||(LA31_0 >= '\u000E' && LA31_0 <= '\u001F')||(LA31_0 >= '$' && LA31_0 <= '\'')||LA31_0=='.'||(LA31_0 >= '0' && LA31_0 <= '9')||LA31_0=='<'||LA31_0=='>'||(LA31_0 >= '@' && LA31_0 <= 'Z')||LA31_0=='\\'||(LA31_0 >= '_' && LA31_0 <= 'z')||LA31_0=='|'||(LA31_0 >= '\u007F' && LA31_0 <= '\u2FFF')||(LA31_0 >= '\u3001' && LA31_0 <= '\uFFFF')) ) {s = 3;}
						if ( s>=0 ) return s;
						break;

					case 10 : 
						int LA31_9 = input.LA(1);
						s = -1;
						if ( (LA31_9=='*'||LA31_9=='?') ) {s = 12;}
						else if ( ((LA31_9 >= '\u0000' && LA31_9 <= '\b')||(LA31_9 >= '\u000B' && LA31_9 <= '\f')||(LA31_9 >= '\u000E' && LA31_9 <= '\u001F')||(LA31_9 >= '$' && LA31_9 <= '\'')||LA31_9=='.'||(LA31_9 >= '0' && LA31_9 <= '9')||LA31_9=='<'||LA31_9=='>'||(LA31_9 >= '@' && LA31_9 <= 'Z')||(LA31_9 >= '_' && LA31_9 <= 'z')||LA31_9=='|'||(LA31_9 >= '\u007F' && LA31_9 <= '\u2FFF')||(LA31_9 >= '\u3001' && LA31_9 <= '\uFFFF')) ) {s = 4;}
						else if ( (LA31_9=='\\') ) {s = 5;}
						else if ( (LA31_9=='+') ) {s = 6;}
						else if ( (LA31_9=='-') ) {s = 7;}
						else if ( (LA31_9=='=') ) {s = 8;}
						else if ( (LA31_9=='#') ) {s = 9;}
						else if ( (LA31_9=='/') ) {s = 10;}
						else s = 11;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 31, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA23_eotS =
		"\2\10\1\uffff\5\10\2\uffff\1\10";
	static final String DFA23_eofS =
		"\13\uffff";
	static final String DFA23_minS =
		"\10\0\2\uffff\1\0";
	static final String DFA23_maxS =
		"\10\uffff\2\uffff\1\uffff";
	static final String DFA23_acceptS =
		"\10\uffff\1\2\1\1\1\uffff";
	static final String DFA23_specialS =
		"\1\5\1\1\1\3\1\6\1\0\1\2\1\4\1\7\2\uffff\1\10}>";
	static final String[] DFA23_transitionS = {
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\3\uffff\1\3\1\uffff\1"+
			"\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\uffff\33\1\1\uffff\1\2\2\uffff"+
			"\34\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\0\12",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"",
			"",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1"
	};

	static final short[] DFA23_eot = DFA.unpackEncodedString(DFA23_eotS);
	static final short[] DFA23_eof = DFA.unpackEncodedString(DFA23_eofS);
	static final char[] DFA23_min = DFA.unpackEncodedStringToUnsignedChars(DFA23_minS);
	static final char[] DFA23_max = DFA.unpackEncodedStringToUnsignedChars(DFA23_maxS);
	static final short[] DFA23_accept = DFA.unpackEncodedString(DFA23_acceptS);
	static final short[] DFA23_special = DFA.unpackEncodedString(DFA23_specialS);
	static final short[][] DFA23_transition;

	static {
		int numStates = DFA23_transitionS.length;
		DFA23_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA23_transition[i] = DFA.unpackEncodedString(DFA23_transitionS[i]);
		}
	}

	protected class DFA23 extends DFA {

		public DFA23(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 23;
			this.eot = DFA23_eot;
			this.eof = DFA23_eof;
			this.min = DFA23_min;
			this.max = DFA23_max;
			this.accept = DFA23_accept;
			this.special = DFA23_special;
			this.transition = DFA23_transition;
		}
		@Override
		public String getDescription() {
			return "()+ loopback of 519:16: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA23_4 = input.LA(1);
						s = -1;
						if ( ((LA23_4 >= '\u0000' && LA23_4 <= '\b')||(LA23_4 >= '\u000B' && LA23_4 <= '\f')||(LA23_4 >= '\u000E' && LA23_4 <= '\u001F')||(LA23_4 >= '$' && LA23_4 <= '\'')||LA23_4=='.'||(LA23_4 >= '0' && LA23_4 <= '9')||LA23_4=='<'||LA23_4=='>'||(LA23_4 >= '@' && LA23_4 <= 'Z')||(LA23_4 >= '_' && LA23_4 <= 'z')||LA23_4=='|'||(LA23_4 >= '\u007F' && LA23_4 <= '\u2FFF')||(LA23_4 >= '\u3001' && LA23_4 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA23_4=='\\') ) {s = 2;}
						else if ( (LA23_4=='+') ) {s = 3;}
						else if ( (LA23_4=='-') ) {s = 4;}
						else if ( (LA23_4=='=') ) {s = 5;}
						else if ( (LA23_4=='#') ) {s = 6;}
						else if ( (LA23_4=='/') ) {s = 7;}
						else if ( (LA23_4=='*'||LA23_4=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA23_1 = input.LA(1);
						s = -1;
						if ( ((LA23_1 >= '\u0000' && LA23_1 <= '\b')||(LA23_1 >= '\u000B' && LA23_1 <= '\f')||(LA23_1 >= '\u000E' && LA23_1 <= '\u001F')||(LA23_1 >= '$' && LA23_1 <= '\'')||LA23_1=='.'||(LA23_1 >= '0' && LA23_1 <= '9')||LA23_1=='<'||LA23_1=='>'||(LA23_1 >= '@' && LA23_1 <= 'Z')||(LA23_1 >= '_' && LA23_1 <= 'z')||LA23_1=='|'||(LA23_1 >= '\u007F' && LA23_1 <= '\u2FFF')||(LA23_1 >= '\u3001' && LA23_1 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA23_1=='\\') ) {s = 2;}
						else if ( (LA23_1=='+') ) {s = 3;}
						else if ( (LA23_1=='-') ) {s = 4;}
						else if ( (LA23_1=='=') ) {s = 5;}
						else if ( (LA23_1=='#') ) {s = 6;}
						else if ( (LA23_1=='/') ) {s = 7;}
						else if ( (LA23_1=='*'||LA23_1=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA23_5 = input.LA(1);
						s = -1;
						if ( ((LA23_5 >= '\u0000' && LA23_5 <= '\b')||(LA23_5 >= '\u000B' && LA23_5 <= '\f')||(LA23_5 >= '\u000E' && LA23_5 <= '\u001F')||(LA23_5 >= '$' && LA23_5 <= '\'')||LA23_5=='.'||(LA23_5 >= '0' && LA23_5 <= '9')||LA23_5=='<'||LA23_5=='>'||(LA23_5 >= '@' && LA23_5 <= 'Z')||(LA23_5 >= '_' && LA23_5 <= 'z')||LA23_5=='|'||(LA23_5 >= '\u007F' && LA23_5 <= '\u2FFF')||(LA23_5 >= '\u3001' && LA23_5 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA23_5=='\\') ) {s = 2;}
						else if ( (LA23_5=='+') ) {s = 3;}
						else if ( (LA23_5=='-') ) {s = 4;}
						else if ( (LA23_5=='=') ) {s = 5;}
						else if ( (LA23_5=='#') ) {s = 6;}
						else if ( (LA23_5=='/') ) {s = 7;}
						else if ( (LA23_5=='*'||LA23_5=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA23_2 = input.LA(1);
						s = -1;
						if ( ((LA23_2 >= '\u0000' && LA23_2 <= '\uFFFF')) ) {s = 10;}
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA23_6 = input.LA(1);
						s = -1;
						if ( ((LA23_6 >= '\u0000' && LA23_6 <= '\b')||(LA23_6 >= '\u000B' && LA23_6 <= '\f')||(LA23_6 >= '\u000E' && LA23_6 <= '\u001F')||(LA23_6 >= '$' && LA23_6 <= '\'')||LA23_6=='.'||(LA23_6 >= '0' && LA23_6 <= '9')||LA23_6=='<'||LA23_6=='>'||(LA23_6 >= '@' && LA23_6 <= 'Z')||(LA23_6 >= '_' && LA23_6 <= 'z')||LA23_6=='|'||(LA23_6 >= '\u007F' && LA23_6 <= '\u2FFF')||(LA23_6 >= '\u3001' && LA23_6 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA23_6=='\\') ) {s = 2;}
						else if ( (LA23_6=='+') ) {s = 3;}
						else if ( (LA23_6=='-') ) {s = 4;}
						else if ( (LA23_6=='=') ) {s = 5;}
						else if ( (LA23_6=='#') ) {s = 6;}
						else if ( (LA23_6=='/') ) {s = 7;}
						else if ( (LA23_6=='*'||LA23_6=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA23_0 = input.LA(1);
						s = -1;
						if ( ((LA23_0 >= '\u0000' && LA23_0 <= '\b')||(LA23_0 >= '\u000B' && LA23_0 <= '\f')||(LA23_0 >= '\u000E' && LA23_0 <= '\u001F')||(LA23_0 >= '$' && LA23_0 <= '\'')||LA23_0=='.'||(LA23_0 >= '0' && LA23_0 <= '9')||LA23_0=='<'||LA23_0=='>'||(LA23_0 >= '@' && LA23_0 <= 'Z')||(LA23_0 >= '_' && LA23_0 <= 'z')||LA23_0=='|'||(LA23_0 >= '\u007F' && LA23_0 <= '\u2FFF')||(LA23_0 >= '\u3001' && LA23_0 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA23_0=='\\') ) {s = 2;}
						else if ( (LA23_0=='+') ) {s = 3;}
						else if ( (LA23_0=='-') ) {s = 4;}
						else if ( (LA23_0=='=') ) {s = 5;}
						else if ( (LA23_0=='#') ) {s = 6;}
						else if ( (LA23_0=='/') ) {s = 7;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 6 : 
						int LA23_3 = input.LA(1);
						s = -1;
						if ( ((LA23_3 >= '\u0000' && LA23_3 <= '\b')||(LA23_3 >= '\u000B' && LA23_3 <= '\f')||(LA23_3 >= '\u000E' && LA23_3 <= '\u001F')||(LA23_3 >= '$' && LA23_3 <= '\'')||LA23_3=='.'||(LA23_3 >= '0' && LA23_3 <= '9')||LA23_3=='<'||LA23_3=='>'||(LA23_3 >= '@' && LA23_3 <= 'Z')||(LA23_3 >= '_' && LA23_3 <= 'z')||LA23_3=='|'||(LA23_3 >= '\u007F' && LA23_3 <= '\u2FFF')||(LA23_3 >= '\u3001' && LA23_3 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA23_3=='\\') ) {s = 2;}
						else if ( (LA23_3=='+') ) {s = 3;}
						else if ( (LA23_3=='-') ) {s = 4;}
						else if ( (LA23_3=='=') ) {s = 5;}
						else if ( (LA23_3=='#') ) {s = 6;}
						else if ( (LA23_3=='/') ) {s = 7;}
						else if ( (LA23_3=='*'||LA23_3=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 7 : 
						int LA23_7 = input.LA(1);
						s = -1;
						if ( ((LA23_7 >= '\u0000' && LA23_7 <= '\b')||(LA23_7 >= '\u000B' && LA23_7 <= '\f')||(LA23_7 >= '\u000E' && LA23_7 <= '\u001F')||(LA23_7 >= '$' && LA23_7 <= '\'')||LA23_7=='.'||(LA23_7 >= '0' && LA23_7 <= '9')||LA23_7=='<'||LA23_7=='>'||(LA23_7 >= '@' && LA23_7 <= 'Z')||(LA23_7 >= '_' && LA23_7 <= 'z')||LA23_7=='|'||(LA23_7 >= '\u007F' && LA23_7 <= '\u2FFF')||(LA23_7 >= '\u3001' && LA23_7 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA23_7=='\\') ) {s = 2;}
						else if ( (LA23_7=='+') ) {s = 3;}
						else if ( (LA23_7=='-') ) {s = 4;}
						else if ( (LA23_7=='=') ) {s = 5;}
						else if ( (LA23_7=='#') ) {s = 6;}
						else if ( (LA23_7=='/') ) {s = 7;}
						else if ( (LA23_7=='*'||LA23_7=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 8 : 
						int LA23_10 = input.LA(1);
						s = -1;
						if ( ((LA23_10 >= '\u0000' && LA23_10 <= '\b')||(LA23_10 >= '\u000B' && LA23_10 <= '\f')||(LA23_10 >= '\u000E' && LA23_10 <= '\u001F')||(LA23_10 >= '$' && LA23_10 <= '\'')||LA23_10=='.'||(LA23_10 >= '0' && LA23_10 <= '9')||LA23_10=='<'||LA23_10=='>'||(LA23_10 >= '@' && LA23_10 <= 'Z')||(LA23_10 >= '_' && LA23_10 <= 'z')||LA23_10=='|'||(LA23_10 >= '\u007F' && LA23_10 <= '\u2FFF')||(LA23_10 >= '\u3001' && LA23_10 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA23_10=='\\') ) {s = 2;}
						else if ( (LA23_10=='+') ) {s = 3;}
						else if ( (LA23_10=='-') ) {s = 4;}
						else if ( (LA23_10=='=') ) {s = 5;}
						else if ( (LA23_10=='#') ) {s = 6;}
						else if ( (LA23_10=='/') ) {s = 7;}
						else if ( (LA23_10=='*'||LA23_10=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 23, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA27_eotS =
		"\2\10\1\uffff\5\10\2\uffff\1\10";
	static final String DFA27_eofS =
		"\13\uffff";
	static final String DFA27_minS =
		"\10\0\2\uffff\1\0";
	static final String DFA27_maxS =
		"\10\uffff\2\uffff\1\uffff";
	static final String DFA27_acceptS =
		"\10\uffff\1\2\1\1\1\uffff";
	static final String DFA27_specialS =
		"\1\3\1\10\1\2\1\4\1\7\1\0\1\1\1\5\2\uffff\1\6}>";
	static final String[] DFA27_transitionS = {
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\0\12",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
			"",
			"",
			"\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\6\4\1\2\uffff\1\11\1\3\1\uffff"+
			"\1\4\1\1\1\7\12\1\2\uffff\1\1\1\5\1\1\1\11\33\1\1\uffff\1\2\2\uffff\34"+
			"\1\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1"
	};

	static final short[] DFA27_eot = DFA.unpackEncodedString(DFA27_eotS);
	static final short[] DFA27_eof = DFA.unpackEncodedString(DFA27_eofS);
	static final char[] DFA27_min = DFA.unpackEncodedStringToUnsignedChars(DFA27_minS);
	static final char[] DFA27_max = DFA.unpackEncodedStringToUnsignedChars(DFA27_maxS);
	static final short[] DFA27_accept = DFA.unpackEncodedString(DFA27_acceptS);
	static final short[] DFA27_special = DFA.unpackEncodedString(DFA27_specialS);
	static final short[][] DFA27_transition;

	static {
		int numStates = DFA27_transitionS.length;
		DFA27_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA27_transition[i] = DFA.unpackEncodedString(DFA27_transitionS[i]);
		}
	}

	protected class DFA27 extends DFA {

		public DFA27(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 27;
			this.eot = DFA27_eot;
			this.eof = DFA27_eof;
			this.min = DFA27_min;
			this.max = DFA27_max;
			this.accept = DFA27_accept;
			this.special = DFA27_special;
			this.transition = DFA27_transition;
		}
		@Override
		public String getDescription() {
			return "()+ loopback of 520:21: ( ( TERM_CHAR )* ( QMARK | STAR ) )+";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA27_5 = input.LA(1);
						s = -1;
						if ( ((LA27_5 >= '\u0000' && LA27_5 <= '\b')||(LA27_5 >= '\u000B' && LA27_5 <= '\f')||(LA27_5 >= '\u000E' && LA27_5 <= '\u001F')||(LA27_5 >= '$' && LA27_5 <= '\'')||LA27_5=='.'||(LA27_5 >= '0' && LA27_5 <= '9')||LA27_5=='<'||LA27_5=='>'||(LA27_5 >= '@' && LA27_5 <= 'Z')||(LA27_5 >= '_' && LA27_5 <= 'z')||LA27_5=='|'||(LA27_5 >= '\u007F' && LA27_5 <= '\u2FFF')||(LA27_5 >= '\u3001' && LA27_5 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA27_5=='\\') ) {s = 2;}
						else if ( (LA27_5=='+') ) {s = 3;}
						else if ( (LA27_5=='-') ) {s = 4;}
						else if ( (LA27_5=='=') ) {s = 5;}
						else if ( (LA27_5=='#') ) {s = 6;}
						else if ( (LA27_5=='/') ) {s = 7;}
						else if ( (LA27_5=='*'||LA27_5=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA27_6 = input.LA(1);
						s = -1;
						if ( ((LA27_6 >= '\u0000' && LA27_6 <= '\b')||(LA27_6 >= '\u000B' && LA27_6 <= '\f')||(LA27_6 >= '\u000E' && LA27_6 <= '\u001F')||(LA27_6 >= '$' && LA27_6 <= '\'')||LA27_6=='.'||(LA27_6 >= '0' && LA27_6 <= '9')||LA27_6=='<'||LA27_6=='>'||(LA27_6 >= '@' && LA27_6 <= 'Z')||(LA27_6 >= '_' && LA27_6 <= 'z')||LA27_6=='|'||(LA27_6 >= '\u007F' && LA27_6 <= '\u2FFF')||(LA27_6 >= '\u3001' && LA27_6 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA27_6=='\\') ) {s = 2;}
						else if ( (LA27_6=='+') ) {s = 3;}
						else if ( (LA27_6=='-') ) {s = 4;}
						else if ( (LA27_6=='=') ) {s = 5;}
						else if ( (LA27_6=='#') ) {s = 6;}
						else if ( (LA27_6=='/') ) {s = 7;}
						else if ( (LA27_6=='*'||LA27_6=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA27_2 = input.LA(1);
						s = -1;
						if ( ((LA27_2 >= '\u0000' && LA27_2 <= '\uFFFF')) ) {s = 10;}
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA27_0 = input.LA(1);
						s = -1;
						if ( ((LA27_0 >= '\u0000' && LA27_0 <= '\b')||(LA27_0 >= '\u000B' && LA27_0 <= '\f')||(LA27_0 >= '\u000E' && LA27_0 <= '\u001F')||(LA27_0 >= '$' && LA27_0 <= '\'')||LA27_0=='.'||(LA27_0 >= '0' && LA27_0 <= '9')||LA27_0=='<'||LA27_0=='>'||(LA27_0 >= '@' && LA27_0 <= 'Z')||(LA27_0 >= '_' && LA27_0 <= 'z')||LA27_0=='|'||(LA27_0 >= '\u007F' && LA27_0 <= '\u2FFF')||(LA27_0 >= '\u3001' && LA27_0 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA27_0=='\\') ) {s = 2;}
						else if ( (LA27_0=='+') ) {s = 3;}
						else if ( (LA27_0=='-') ) {s = 4;}
						else if ( (LA27_0=='=') ) {s = 5;}
						else if ( (LA27_0=='#') ) {s = 6;}
						else if ( (LA27_0=='/') ) {s = 7;}
						else if ( (LA27_0=='*'||LA27_0=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA27_3 = input.LA(1);
						s = -1;
						if ( ((LA27_3 >= '\u0000' && LA27_3 <= '\b')||(LA27_3 >= '\u000B' && LA27_3 <= '\f')||(LA27_3 >= '\u000E' && LA27_3 <= '\u001F')||(LA27_3 >= '$' && LA27_3 <= '\'')||LA27_3=='.'||(LA27_3 >= '0' && LA27_3 <= '9')||LA27_3=='<'||LA27_3=='>'||(LA27_3 >= '@' && LA27_3 <= 'Z')||(LA27_3 >= '_' && LA27_3 <= 'z')||LA27_3=='|'||(LA27_3 >= '\u007F' && LA27_3 <= '\u2FFF')||(LA27_3 >= '\u3001' && LA27_3 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA27_3=='\\') ) {s = 2;}
						else if ( (LA27_3=='+') ) {s = 3;}
						else if ( (LA27_3=='-') ) {s = 4;}
						else if ( (LA27_3=='=') ) {s = 5;}
						else if ( (LA27_3=='#') ) {s = 6;}
						else if ( (LA27_3=='/') ) {s = 7;}
						else if ( (LA27_3=='*'||LA27_3=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA27_7 = input.LA(1);
						s = -1;
						if ( ((LA27_7 >= '\u0000' && LA27_7 <= '\b')||(LA27_7 >= '\u000B' && LA27_7 <= '\f')||(LA27_7 >= '\u000E' && LA27_7 <= '\u001F')||(LA27_7 >= '$' && LA27_7 <= '\'')||LA27_7=='.'||(LA27_7 >= '0' && LA27_7 <= '9')||LA27_7=='<'||LA27_7=='>'||(LA27_7 >= '@' && LA27_7 <= 'Z')||(LA27_7 >= '_' && LA27_7 <= 'z')||LA27_7=='|'||(LA27_7 >= '\u007F' && LA27_7 <= '\u2FFF')||(LA27_7 >= '\u3001' && LA27_7 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA27_7=='\\') ) {s = 2;}
						else if ( (LA27_7=='+') ) {s = 3;}
						else if ( (LA27_7=='-') ) {s = 4;}
						else if ( (LA27_7=='=') ) {s = 5;}
						else if ( (LA27_7=='#') ) {s = 6;}
						else if ( (LA27_7=='/') ) {s = 7;}
						else if ( (LA27_7=='*'||LA27_7=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 6 : 
						int LA27_10 = input.LA(1);
						s = -1;
						if ( ((LA27_10 >= '\u0000' && LA27_10 <= '\b')||(LA27_10 >= '\u000B' && LA27_10 <= '\f')||(LA27_10 >= '\u000E' && LA27_10 <= '\u001F')||(LA27_10 >= '$' && LA27_10 <= '\'')||LA27_10=='.'||(LA27_10 >= '0' && LA27_10 <= '9')||LA27_10=='<'||LA27_10=='>'||(LA27_10 >= '@' && LA27_10 <= 'Z')||(LA27_10 >= '_' && LA27_10 <= 'z')||LA27_10=='|'||(LA27_10 >= '\u007F' && LA27_10 <= '\u2FFF')||(LA27_10 >= '\u3001' && LA27_10 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA27_10=='\\') ) {s = 2;}
						else if ( (LA27_10=='+') ) {s = 3;}
						else if ( (LA27_10=='-') ) {s = 4;}
						else if ( (LA27_10=='=') ) {s = 5;}
						else if ( (LA27_10=='#') ) {s = 6;}
						else if ( (LA27_10=='/') ) {s = 7;}
						else if ( (LA27_10=='*'||LA27_10=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 7 : 
						int LA27_4 = input.LA(1);
						s = -1;
						if ( ((LA27_4 >= '\u0000' && LA27_4 <= '\b')||(LA27_4 >= '\u000B' && LA27_4 <= '\f')||(LA27_4 >= '\u000E' && LA27_4 <= '\u001F')||(LA27_4 >= '$' && LA27_4 <= '\'')||LA27_4=='.'||(LA27_4 >= '0' && LA27_4 <= '9')||LA27_4=='<'||LA27_4=='>'||(LA27_4 >= '@' && LA27_4 <= 'Z')||(LA27_4 >= '_' && LA27_4 <= 'z')||LA27_4=='|'||(LA27_4 >= '\u007F' && LA27_4 <= '\u2FFF')||(LA27_4 >= '\u3001' && LA27_4 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA27_4=='\\') ) {s = 2;}
						else if ( (LA27_4=='+') ) {s = 3;}
						else if ( (LA27_4=='-') ) {s = 4;}
						else if ( (LA27_4=='=') ) {s = 5;}
						else if ( (LA27_4=='#') ) {s = 6;}
						else if ( (LA27_4=='/') ) {s = 7;}
						else if ( (LA27_4=='*'||LA27_4=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;

					case 8 : 
						int LA27_1 = input.LA(1);
						s = -1;
						if ( ((LA27_1 >= '\u0000' && LA27_1 <= '\b')||(LA27_1 >= '\u000B' && LA27_1 <= '\f')||(LA27_1 >= '\u000E' && LA27_1 <= '\u001F')||(LA27_1 >= '$' && LA27_1 <= '\'')||LA27_1=='.'||(LA27_1 >= '0' && LA27_1 <= '9')||LA27_1=='<'||LA27_1=='>'||(LA27_1 >= '@' && LA27_1 <= 'Z')||(LA27_1 >= '_' && LA27_1 <= 'z')||LA27_1=='|'||(LA27_1 >= '\u007F' && LA27_1 <= '\u2FFF')||(LA27_1 >= '\u3001' && LA27_1 <= '\uFFFF')) ) {s = 1;}
						else if ( (LA27_1=='\\') ) {s = 2;}
						else if ( (LA27_1=='+') ) {s = 3;}
						else if ( (LA27_1=='-') ) {s = 4;}
						else if ( (LA27_1=='=') ) {s = 5;}
						else if ( (LA27_1=='#') ) {s = 6;}
						else if ( (LA27_1=='/') ) {s = 7;}
						else if ( (LA27_1=='*'||LA27_1=='?') ) {s = 9;}
						else s = 8;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 27, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA36_eotS =
		"\2\uffff\1\35\7\uffff\1\46\1\50\1\51\1\52\1\uffff\1\54\2\uffff\4\35\1"+
		"\66\1\35\4\uffff\1\35\1\uffff\1\35\1\uffff\4\35\14\uffff\1\100\2\35\1"+
		"\102\2\35\1\uffff\1\66\4\35\1\111\1\35\3\uffff\1\113\1\uffff\1\114\1\35"+
		"\1\66\1\uffff\1\66\1\35\4\uffff\1\126\1\66\1\35\1\66\4\35\1\126\1\uffff"+
		"\1\47\2\66\2\35\1\136\1\35\1\uffff\2\35\1\136\1\47";
	static final String DFA36_eofS =
		"\143\uffff";
	static final String DFA36_minS =
		"\1\0\1\uffff\1\0\7\uffff\1\60\3\0\1\uffff\1\0\2\uffff\7\0\3\uffff\1\0"+
		"\1\uffff\6\0\11\uffff\2\0\1\uffff\6\0\1\uffff\10\0\2\uffff\1\0\1\uffff"+
		"\3\0\1\uffff\2\0\4\uffff\11\0\1\uffff\7\0\1\uffff\4\0";
	static final String DFA36_maxS =
		"\1\uffff\1\uffff\1\uffff\7\uffff\1\71\3\uffff\1\uffff\1\uffff\2\uffff"+
		"\7\uffff\3\uffff\1\uffff\1\uffff\6\uffff\11\uffff\2\uffff\1\uffff\6\uffff"+
		"\1\uffff\10\uffff\2\uffff\1\uffff\1\uffff\3\uffff\1\uffff\2\uffff\4\uffff"+
		"\11\uffff\1\uffff\7\uffff\1\uffff\4\uffff";
	static final String DFA36_acceptS =
		"\1\uffff\1\1\1\uffff\1\3\1\4\1\5\1\6\1\7\1\10\1\11\4\uffff\1\16\1\uffff"+
		"\1\20\1\21\7\uffff\1\32\1\42\1\43\1\uffff\1\36\6\uffff\1\31\1\37\1\12"+
		"\1\30\1\13\1\14\1\15\1\27\1\17\2\uffff\1\41\6\uffff\1\34\10\uffff\1\40"+
		"\1\22\1\uffff\1\24\3\uffff\1\35\2\uffff\1\2\1\40\1\23\1\25\11\uffff\1"+
		"\26\7\uffff\1\33\4\uffff";
	static final String DFA36_specialS =
		"\1\1\1\uffff\1\13\10\uffff\1\26\1\61\1\71\1\uffff\1\23\2\uffff\1\36\1"+
		"\40\1\42\1\32\1\60\1\67\1\15\3\uffff\1\0\1\uffff\1\74\1\16\1\41\1\43\1"+
		"\50\1\52\11\uffff\1\34\1\5\1\uffff\1\12\1\46\1\55\1\24\1\62\1\64\1\uffff"+
		"\1\73\1\33\1\53\1\57\1\72\1\25\1\2\1\7\2\uffff\1\17\1\uffff\1\30\1\65"+
		"\1\70\1\uffff\1\44\1\27\4\uffff\1\66\1\10\1\31\1\75\1\54\1\63\1\4\1\56"+
		"\1\3\1\uffff\1\51\1\37\1\14\1\45\1\20\1\6\1\21\1\uffff\1\47\1\22\1\11"+
		"\1\35}>";
	static final String[] DFA36_transitionS = {
			"\11\27\2\31\2\27\1\31\22\27\1\31\1\uffff\1\17\1\1\4\27\1\4\1\5\1\13\1"+
			"\11\1\20\1\12\1\27\1\33\12\26\1\10\1\21\1\2\1\3\1\27\1\14\1\27\1\23\14"+
			"\27\1\25\1\24\4\27\1\22\6\27\1\6\1\30\1\7\1\15\2\27\1\23\14\27\1\25\1"+
			"\24\13\27\1\32\1\27\1\uffff\1\16\u2f81\27\1\31\ucfff\27",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\34\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\12\47",
			"\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\5\45\3\uffff\1\45\1\uffff"+
			"\15\45\2\uffff\3\45\1\uffff\33\45\1\uffff\1\45\2\uffff\34\45\1\uffff"+
			"\1\45\2\uffff\u2f81\45\1\uffff\ucfff\45",
			"\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\5\45\3\uffff\1\45\1\uffff"+
			"\15\45\2\uffff\3\45\1\14\33\45\1\uffff\1\45\2\uffff\34\45\1\uffff\1\45"+
			"\2\uffff\u2f81\45\1\uffff\ucfff\45",
			"\40\53\1\uffff\3\53\1\uffff\6\53\3\uffff\2\53\12\uffff\uffc6\53",
			"",
			"\42\56\1\uffff\7\56\1\57\24\56\1\57\34\56\1\55\uffa3\56",
			"",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\17\36"+
			"\1\60\13\36\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36"+
			"\1\uffff\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\16\36"+
			"\1\62\14\36\1\uffff\1\37\2\uffff\17\36\1\62\14\36\1\uffff\1\36\2\uffff"+
			"\u2f81\36\1\uffff\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\22\36"+
			"\1\63\10\36\1\uffff\1\37\2\uffff\23\36\1\63\10\36\1\uffff\1\36\2\uffff"+
			"\u2f81\36\1\uffff\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\5\36\1"+
			"\65\11\36\1\64\13\36\1\uffff\1\37\2\uffff\6\36\1\65\11\36\1\64\13\36"+
			"\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\71\1\70\1\72\12\67\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\0\73",
			"",
			"",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\74\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\0\75",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\0\76",
			"\42\56\1\77\7\56\1\57\24\56\1\57\34\56\1\55\uffa3\56",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\4\36\1"+
			"\101\26\36\1\uffff\1\37\2\uffff\5\36\1\101\26\36\1\uffff\1\36\2\uffff"+
			"\u2f81\36\1\uffff\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\24\36"+
			"\1\103\6\36\1\uffff\1\37\2\uffff\25\36\1\103\6\36\1\uffff\1\36\2\uffff"+
			"\u2f81\36\1\uffff\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\1\36\1"+
			"\104\31\36\1\uffff\1\37\2\uffff\2\36\1\104\31\36\1\uffff\1\36\2\uffff"+
			"\u2f81\36\1\uffff\ucfff\36",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\71\1\70\1\72\12\105\1\106\1\uffff\1\36\1\61\1\36\1\45"+
			"\33\36\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff"+
			"\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\107\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\110\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\110\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\42\56\1\77\7\56\1\57\24\56\1\57\34\56\1\55\uffa3\56",
			"",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\22\36"+
			"\1\115\10\36\1\uffff\1\37\2\uffff\23\36\1\115\10\36\1\uffff\1\36\2\uffff"+
			"\u2f81\36\1\uffff\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\117\1\43\12\116\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\121\1\123\1\122\12\120\2\uffff\1\36\1\61\1\36\1\45\33"+
			"\36\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff"+
			"\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\121\1\123\1\122\12\124\2\uffff\1\36\1\61\1\36\1\45\33"+
			"\36\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff"+
			"\ucfff\36",
			"",
			"",
			"",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\125\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\127\1\117\1\43\12\130\2\uffff\1\36\1\61\1\36\1\45\33"+
			"\36\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff"+
			"\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\131\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\121\1\123\1\122\12\131\2\uffff\1\36\1\61\1\36\1\45\33"+
			"\36\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff"+
			"\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\132\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\132\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\132\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\121\1\123\1\122\12\36\2\uffff\1\36\1\61\1\36\1\45\33"+
			"\36\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff"+
			"\ucfff\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\125\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\133\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\117\1\43\12\130\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\131\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\134\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\135\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\137\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\140\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\141\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\142\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36",
			"\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\1\42\4\36\1\44\1\uffff\1\45"+
			"\1\40\1\uffff\1\41\1\36\1\43\12\36\2\uffff\1\36\1\61\1\36\1\45\33\36"+
			"\1\uffff\1\37\2\uffff\34\36\1\uffff\1\36\2\uffff\u2f81\36\1\uffff\ucfff"+
			"\36"
	};

	static final short[] DFA36_eot = DFA.unpackEncodedString(DFA36_eotS);
	static final short[] DFA36_eof = DFA.unpackEncodedString(DFA36_eofS);
	static final char[] DFA36_min = DFA.unpackEncodedStringToUnsignedChars(DFA36_minS);
	static final char[] DFA36_max = DFA.unpackEncodedStringToUnsignedChars(DFA36_maxS);
	static final short[] DFA36_accept = DFA.unpackEncodedString(DFA36_acceptS);
	static final short[] DFA36_special = DFA.unpackEncodedString(DFA36_specialS);
	static final short[][] DFA36_transition;

	static {
		int numStates = DFA36_transitionS.length;
		DFA36_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA36_transition[i] = DFA.unpackEncodedString(DFA36_transitionS[i]);
		}
	}

	protected class DFA36 extends DFA {

		public DFA36(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 36;
			this.eot = DFA36_eot;
			this.eof = DFA36_eof;
			this.min = DFA36_min;
			this.max = DFA36_max;
			this.accept = DFA36_accept;
			this.special = DFA36_special;
			this.transition = DFA36_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__69 | T__70 | T__71 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | CARAT | TILDE | DQUOTE | COMMA | SEMICOLON | TO | AND | OR | NOT | NEAR | AUTHOR_SEARCH | DATE_RANGE | FUNC_NAME | WS | DATE_TOKEN | NUMBER | HOUR | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING | LOCAL_PARAMS | REGEX );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA36_28 = input.LA(1);
						s = -1;
						if ( (LA36_28=='>') ) {s = 60;}
						else if ( (LA36_28=='(') ) {s = 36;}
						else if ( ((LA36_28 >= '\u0000' && LA36_28 <= '\b')||(LA36_28 >= '\u000B' && LA36_28 <= '\f')||(LA36_28 >= '\u000E' && LA36_28 <= '\u001F')||(LA36_28 >= '$' && LA36_28 <= '\'')||LA36_28=='.'||(LA36_28 >= '0' && LA36_28 <= '9')||LA36_28=='<'||(LA36_28 >= '@' && LA36_28 <= 'Z')||(LA36_28 >= '_' && LA36_28 <= 'z')||LA36_28=='|'||(LA36_28 >= '\u007F' && LA36_28 <= '\u2FFF')||(LA36_28 >= '\u3001' && LA36_28 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_28=='\\') ) {s = 31;}
						else if ( (LA36_28=='+') ) {s = 32;}
						else if ( (LA36_28=='-') ) {s = 33;}
						else if ( (LA36_28=='=') ) {s = 49;}
						else if ( (LA36_28=='#') ) {s = 34;}
						else if ( (LA36_28=='/') ) {s = 35;}
						else if ( (LA36_28=='*'||LA36_28=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA36_0 = input.LA(1);
						s = -1;
						if ( (LA36_0=='#') ) {s = 1;}
						else if ( (LA36_0=='<') ) {s = 2;}
						else if ( (LA36_0=='=') ) {s = 3;}
						else if ( (LA36_0=='(') ) {s = 4;}
						else if ( (LA36_0==')') ) {s = 5;}
						else if ( (LA36_0=='[') ) {s = 6;}
						else if ( (LA36_0==']') ) {s = 7;}
						else if ( (LA36_0==':') ) {s = 8;}
						else if ( (LA36_0=='+') ) {s = 9;}
						else if ( (LA36_0=='-') ) {s = 10;}
						else if ( (LA36_0=='*') ) {s = 11;}
						else if ( (LA36_0=='?') ) {s = 12;}
						else if ( (LA36_0=='^') ) {s = 13;}
						else if ( (LA36_0=='~') ) {s = 14;}
						else if ( (LA36_0=='\"') ) {s = 15;}
						else if ( (LA36_0==',') ) {s = 16;}
						else if ( (LA36_0==';') ) {s = 17;}
						else if ( (LA36_0=='T') ) {s = 18;}
						else if ( (LA36_0=='A'||LA36_0=='a') ) {s = 19;}
						else if ( (LA36_0=='O'||LA36_0=='o') ) {s = 20;}
						else if ( (LA36_0=='N'||LA36_0=='n') ) {s = 21;}
						else if ( ((LA36_0 >= '0' && LA36_0 <= '9')) ) {s = 22;}
						else if ( ((LA36_0 >= '\u0000' && LA36_0 <= '\b')||(LA36_0 >= '\u000B' && LA36_0 <= '\f')||(LA36_0 >= '\u000E' && LA36_0 <= '\u001F')||(LA36_0 >= '$' && LA36_0 <= '\'')||LA36_0=='.'||LA36_0=='>'||LA36_0=='@'||(LA36_0 >= 'B' && LA36_0 <= 'M')||(LA36_0 >= 'P' && LA36_0 <= 'S')||(LA36_0 >= 'U' && LA36_0 <= 'Z')||(LA36_0 >= '_' && LA36_0 <= '`')||(LA36_0 >= 'b' && LA36_0 <= 'm')||(LA36_0 >= 'p' && LA36_0 <= 'z')||LA36_0=='|'||(LA36_0 >= '\u007F' && LA36_0 <= '\u2FFF')||(LA36_0 >= '\u3001' && LA36_0 <= '\uFFFF')) ) {s = 23;}
						else if ( (LA36_0=='\\') ) {s = 24;}
						else if ( ((LA36_0 >= '\t' && LA36_0 <= '\n')||LA36_0=='\r'||LA36_0==' '||LA36_0=='\u3000') ) {s = 25;}
						else if ( (LA36_0=='{') ) {s = 26;}
						else if ( (LA36_0=='/') ) {s = 27;}
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA36_61 = input.LA(1);
						s = -1;
						if ( (LA36_61=='(') ) {s = 36;}
						else if ( ((LA36_61 >= '\u0000' && LA36_61 <= '\b')||(LA36_61 >= '\u000B' && LA36_61 <= '\f')||(LA36_61 >= '\u000E' && LA36_61 <= '\u001F')||(LA36_61 >= '$' && LA36_61 <= '\'')||LA36_61=='.'||(LA36_61 >= '0' && LA36_61 <= '9')||LA36_61=='<'||LA36_61=='>'||(LA36_61 >= '@' && LA36_61 <= 'Z')||(LA36_61 >= '_' && LA36_61 <= 'z')||LA36_61=='|'||(LA36_61 >= '\u007F' && LA36_61 <= '\u2FFF')||(LA36_61 >= '\u3001' && LA36_61 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_61=='\\') ) {s = 31;}
						else if ( (LA36_61=='+') ) {s = 32;}
						else if ( (LA36_61=='-') ) {s = 33;}
						else if ( (LA36_61=='=') ) {s = 49;}
						else if ( (LA36_61=='#') ) {s = 34;}
						else if ( (LA36_61=='/') ) {s = 35;}
						else if ( (LA36_61=='*'||LA36_61=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA36_85 = input.LA(1);
						s = -1;
						if ( ((LA36_85 >= '0' && LA36_85 <= '9')) ) {s = 85;}
						else if ( (LA36_85=='(') ) {s = 36;}
						else if ( ((LA36_85 >= '\u0000' && LA36_85 <= '\b')||(LA36_85 >= '\u000B' && LA36_85 <= '\f')||(LA36_85 >= '\u000E' && LA36_85 <= '\u001F')||(LA36_85 >= '$' && LA36_85 <= '\'')||LA36_85=='.'||LA36_85=='<'||LA36_85=='>'||(LA36_85 >= '@' && LA36_85 <= 'Z')||(LA36_85 >= '_' && LA36_85 <= 'z')||LA36_85=='|'||(LA36_85 >= '\u007F' && LA36_85 <= '\u2FFF')||(LA36_85 >= '\u3001' && LA36_85 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_85=='\\') ) {s = 31;}
						else if ( (LA36_85=='+') ) {s = 32;}
						else if ( (LA36_85=='-') ) {s = 33;}
						else if ( (LA36_85=='=') ) {s = 49;}
						else if ( (LA36_85=='#') ) {s = 34;}
						else if ( (LA36_85=='/') ) {s = 35;}
						else if ( (LA36_85=='*'||LA36_85=='?') ) {s = 37;}
						else s = 86;
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA36_83 = input.LA(1);
						s = -1;
						if ( (LA36_83=='(') ) {s = 36;}
						else if ( ((LA36_83 >= '0' && LA36_83 <= '9')) ) {s = 90;}
						else if ( (LA36_83=='\\') ) {s = 31;}
						else if ( (LA36_83=='+') ) {s = 32;}
						else if ( (LA36_83=='-') ) {s = 33;}
						else if ( (LA36_83=='=') ) {s = 49;}
						else if ( (LA36_83=='#') ) {s = 34;}
						else if ( (LA36_83=='/') ) {s = 35;}
						else if ( ((LA36_83 >= '\u0000' && LA36_83 <= '\b')||(LA36_83 >= '\u000B' && LA36_83 <= '\f')||(LA36_83 >= '\u000E' && LA36_83 <= '\u001F')||(LA36_83 >= '$' && LA36_83 <= '\'')||LA36_83=='.'||LA36_83=='<'||LA36_83=='>'||(LA36_83 >= '@' && LA36_83 <= 'Z')||(LA36_83 >= '_' && LA36_83 <= 'z')||LA36_83=='|'||(LA36_83 >= '\u007F' && LA36_83 <= '\u2FFF')||(LA36_83 >= '\u3001' && LA36_83 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_83=='*'||LA36_83=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA36_46 = input.LA(1);
						s = -1;
						if ( (LA36_46=='\"') ) {s = 63;}
						else if ( (LA36_46=='\\') ) {s = 45;}
						else if ( ((LA36_46 >= '\u0000' && LA36_46 <= '!')||(LA36_46 >= '#' && LA36_46 <= ')')||(LA36_46 >= '+' && LA36_46 <= '>')||(LA36_46 >= '@' && LA36_46 <= '[')||(LA36_46 >= ']' && LA36_46 <= '\uFFFF')) ) {s = 46;}
						else if ( (LA36_46=='*'||LA36_46=='?') ) {s = 47;}
						if ( s>=0 ) return s;
						break;

					case 6 : 
						int LA36_92 = input.LA(1);
						s = -1;
						if ( (LA36_92=='(') ) {s = 36;}
						else if ( ((LA36_92 >= '0' && LA36_92 <= '9')) ) {s = 95;}
						else if ( (LA36_92=='\\') ) {s = 31;}
						else if ( (LA36_92=='+') ) {s = 32;}
						else if ( (LA36_92=='-') ) {s = 33;}
						else if ( (LA36_92=='=') ) {s = 49;}
						else if ( (LA36_92=='#') ) {s = 34;}
						else if ( (LA36_92=='/') ) {s = 35;}
						else if ( ((LA36_92 >= '\u0000' && LA36_92 <= '\b')||(LA36_92 >= '\u000B' && LA36_92 <= '\f')||(LA36_92 >= '\u000E' && LA36_92 <= '\u001F')||(LA36_92 >= '$' && LA36_92 <= '\'')||LA36_92=='.'||LA36_92=='<'||LA36_92=='>'||(LA36_92 >= '@' && LA36_92 <= 'Z')||(LA36_92 >= '_' && LA36_92 <= 'z')||LA36_92=='|'||(LA36_92 >= '\u007F' && LA36_92 <= '\u2FFF')||(LA36_92 >= '\u3001' && LA36_92 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_92=='*'||LA36_92=='?') ) {s = 37;}
						else s = 94;
						if ( s>=0 ) return s;
						break;

					case 7 : 
						int LA36_62 = input.LA(1);
						s = -1;
						if ( (LA36_62=='\"') ) {s = 63;}
						else if ( (LA36_62=='\\') ) {s = 45;}
						else if ( ((LA36_62 >= '\u0000' && LA36_62 <= '!')||(LA36_62 >= '#' && LA36_62 <= ')')||(LA36_62 >= '+' && LA36_62 <= '>')||(LA36_62 >= '@' && LA36_62 <= '[')||(LA36_62 >= ']' && LA36_62 <= '\uFFFF')) ) {s = 46;}
						else if ( (LA36_62=='*'||LA36_62=='?') ) {s = 47;}
						if ( s>=0 ) return s;
						break;

					case 8 : 
						int LA36_78 = input.LA(1);
						s = -1;
						if ( (LA36_78=='-') ) {s = 87;}
						else if ( (LA36_78=='(') ) {s = 36;}
						else if ( (LA36_78=='.') ) {s = 79;}
						else if ( (LA36_78=='\\') ) {s = 31;}
						else if ( (LA36_78=='+') ) {s = 32;}
						else if ( (LA36_78=='=') ) {s = 49;}
						else if ( (LA36_78=='#') ) {s = 34;}
						else if ( (LA36_78=='/') ) {s = 35;}
						else if ( ((LA36_78 >= '0' && LA36_78 <= '9')) ) {s = 88;}
						else if ( ((LA36_78 >= '\u0000' && LA36_78 <= '\b')||(LA36_78 >= '\u000B' && LA36_78 <= '\f')||(LA36_78 >= '\u000E' && LA36_78 <= '\u001F')||(LA36_78 >= '$' && LA36_78 <= '\'')||LA36_78=='<'||LA36_78=='>'||(LA36_78 >= '@' && LA36_78 <= 'Z')||(LA36_78 >= '_' && LA36_78 <= 'z')||LA36_78=='|'||(LA36_78 >= '\u007F' && LA36_78 <= '\u2FFF')||(LA36_78 >= '\u3001' && LA36_78 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_78=='*'||LA36_78=='?') ) {s = 37;}
						else s = 54;
						if ( s>=0 ) return s;
						break;

					case 9 : 
						int LA36_97 = input.LA(1);
						s = -1;
						if ( (LA36_97=='(') ) {s = 36;}
						else if ( ((LA36_97 >= '\u0000' && LA36_97 <= '\b')||(LA36_97 >= '\u000B' && LA36_97 <= '\f')||(LA36_97 >= '\u000E' && LA36_97 <= '\u001F')||(LA36_97 >= '$' && LA36_97 <= '\'')||LA36_97=='.'||(LA36_97 >= '0' && LA36_97 <= '9')||LA36_97=='<'||LA36_97=='>'||(LA36_97 >= '@' && LA36_97 <= 'Z')||(LA36_97 >= '_' && LA36_97 <= 'z')||LA36_97=='|'||(LA36_97 >= '\u007F' && LA36_97 <= '\u2FFF')||(LA36_97 >= '\u3001' && LA36_97 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_97=='\\') ) {s = 31;}
						else if ( (LA36_97=='+') ) {s = 32;}
						else if ( (LA36_97=='-') ) {s = 33;}
						else if ( (LA36_97=='=') ) {s = 49;}
						else if ( (LA36_97=='#') ) {s = 34;}
						else if ( (LA36_97=='/') ) {s = 35;}
						else if ( (LA36_97=='*'||LA36_97=='?') ) {s = 37;}
						else s = 94;
						if ( s>=0 ) return s;
						break;

					case 10 : 
						int LA36_48 = input.LA(1);
						s = -1;
						if ( (LA36_48=='(') ) {s = 36;}
						else if ( ((LA36_48 >= '\u0000' && LA36_48 <= '\b')||(LA36_48 >= '\u000B' && LA36_48 <= '\f')||(LA36_48 >= '\u000E' && LA36_48 <= '\u001F')||(LA36_48 >= '$' && LA36_48 <= '\'')||LA36_48=='.'||(LA36_48 >= '0' && LA36_48 <= '9')||LA36_48=='<'||LA36_48=='>'||(LA36_48 >= '@' && LA36_48 <= 'Z')||(LA36_48 >= '_' && LA36_48 <= 'z')||LA36_48=='|'||(LA36_48 >= '\u007F' && LA36_48 <= '\u2FFF')||(LA36_48 >= '\u3001' && LA36_48 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_48=='\\') ) {s = 31;}
						else if ( (LA36_48=='+') ) {s = 32;}
						else if ( (LA36_48=='-') ) {s = 33;}
						else if ( (LA36_48=='=') ) {s = 49;}
						else if ( (LA36_48=='#') ) {s = 34;}
						else if ( (LA36_48=='/') ) {s = 35;}
						else if ( (LA36_48=='*'||LA36_48=='?') ) {s = 37;}
						else s = 64;
						if ( s>=0 ) return s;
						break;

					case 11 : 
						int LA36_2 = input.LA(1);
						s = -1;
						if ( (LA36_2=='=') ) {s = 28;}
						else if ( ((LA36_2 >= '\u0000' && LA36_2 <= '\b')||(LA36_2 >= '\u000B' && LA36_2 <= '\f')||(LA36_2 >= '\u000E' && LA36_2 <= '\u001F')||(LA36_2 >= '$' && LA36_2 <= '\'')||LA36_2=='.'||(LA36_2 >= '0' && LA36_2 <= '9')||LA36_2=='<'||LA36_2=='>'||(LA36_2 >= '@' && LA36_2 <= 'Z')||(LA36_2 >= '_' && LA36_2 <= 'z')||LA36_2=='|'||(LA36_2 >= '\u007F' && LA36_2 <= '\u2FFF')||(LA36_2 >= '\u3001' && LA36_2 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_2=='\\') ) {s = 31;}
						else if ( (LA36_2=='+') ) {s = 32;}
						else if ( (LA36_2=='-') ) {s = 33;}
						else if ( (LA36_2=='#') ) {s = 34;}
						else if ( (LA36_2=='/') ) {s = 35;}
						else if ( (LA36_2=='(') ) {s = 36;}
						else if ( (LA36_2=='*'||LA36_2=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 12 : 
						int LA36_89 = input.LA(1);
						s = -1;
						if ( (LA36_89=='(') ) {s = 36;}
						else if ( ((LA36_89 >= '0' && LA36_89 <= '9')) ) {s = 89;}
						else if ( (LA36_89=='\\') ) {s = 31;}
						else if ( (LA36_89=='+') ) {s = 32;}
						else if ( (LA36_89=='-') ) {s = 33;}
						else if ( (LA36_89=='=') ) {s = 49;}
						else if ( (LA36_89=='#') ) {s = 34;}
						else if ( (LA36_89=='/') ) {s = 35;}
						else if ( ((LA36_89 >= '\u0000' && LA36_89 <= '\b')||(LA36_89 >= '\u000B' && LA36_89 <= '\f')||(LA36_89 >= '\u000E' && LA36_89 <= '\u001F')||(LA36_89 >= '$' && LA36_89 <= '\'')||LA36_89=='.'||LA36_89=='<'||LA36_89=='>'||(LA36_89 >= '@' && LA36_89 <= 'Z')||(LA36_89 >= '_' && LA36_89 <= 'z')||LA36_89=='|'||(LA36_89 >= '\u007F' && LA36_89 <= '\u2FFF')||(LA36_89 >= '\u3001' && LA36_89 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_89=='*'||LA36_89=='?') ) {s = 37;}
						else s = 54;
						if ( s>=0 ) return s;
						break;

					case 13 : 
						int LA36_24 = input.LA(1);
						s = -1;
						if ( ((LA36_24 >= '\u0000' && LA36_24 <= '\uFFFF')) ) {s = 59;}
						if ( s>=0 ) return s;
						break;

					case 14 : 
						int LA36_31 = input.LA(1);
						s = -1;
						if ( ((LA36_31 >= '\u0000' && LA36_31 <= '\uFFFF')) ) {s = 61;}
						if ( s>=0 ) return s;
						break;

					case 15 : 
						int LA36_65 = input.LA(1);
						s = -1;
						if ( (LA36_65=='(') ) {s = 36;}
						else if ( ((LA36_65 >= '\u0000' && LA36_65 <= '\b')||(LA36_65 >= '\u000B' && LA36_65 <= '\f')||(LA36_65 >= '\u000E' && LA36_65 <= '\u001F')||(LA36_65 >= '$' && LA36_65 <= '\'')||LA36_65=='.'||(LA36_65 >= '0' && LA36_65 <= '9')||LA36_65=='<'||LA36_65=='>'||(LA36_65 >= '@' && LA36_65 <= 'Z')||(LA36_65 >= '_' && LA36_65 <= 'z')||LA36_65=='|'||(LA36_65 >= '\u007F' && LA36_65 <= '\u2FFF')||(LA36_65 >= '\u3001' && LA36_65 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_65=='\\') ) {s = 31;}
						else if ( (LA36_65=='+') ) {s = 32;}
						else if ( (LA36_65=='-') ) {s = 33;}
						else if ( (LA36_65=='=') ) {s = 49;}
						else if ( (LA36_65=='#') ) {s = 34;}
						else if ( (LA36_65=='/') ) {s = 35;}
						else if ( (LA36_65=='*'||LA36_65=='?') ) {s = 37;}
						else s = 75;
						if ( s>=0 ) return s;
						break;

					case 16 : 
						int LA36_91 = input.LA(1);
						s = -1;
						if ( ((LA36_91 >= '0' && LA36_91 <= '9')) ) {s = 93;}
						else if ( (LA36_91=='(') ) {s = 36;}
						else if ( ((LA36_91 >= '\u0000' && LA36_91 <= '\b')||(LA36_91 >= '\u000B' && LA36_91 <= '\f')||(LA36_91 >= '\u000E' && LA36_91 <= '\u001F')||(LA36_91 >= '$' && LA36_91 <= '\'')||LA36_91=='.'||LA36_91=='<'||LA36_91=='>'||(LA36_91 >= '@' && LA36_91 <= 'Z')||(LA36_91 >= '_' && LA36_91 <= 'z')||LA36_91=='|'||(LA36_91 >= '\u007F' && LA36_91 <= '\u2FFF')||(LA36_91 >= '\u3001' && LA36_91 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_91=='\\') ) {s = 31;}
						else if ( (LA36_91=='+') ) {s = 32;}
						else if ( (LA36_91=='-') ) {s = 33;}
						else if ( (LA36_91=='=') ) {s = 49;}
						else if ( (LA36_91=='#') ) {s = 34;}
						else if ( (LA36_91=='/') ) {s = 35;}
						else if ( (LA36_91=='*'||LA36_91=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 17 : 
						int LA36_93 = input.LA(1);
						s = -1;
						if ( ((LA36_93 >= '0' && LA36_93 <= '9')) ) {s = 96;}
						else if ( (LA36_93=='(') ) {s = 36;}
						else if ( ((LA36_93 >= '\u0000' && LA36_93 <= '\b')||(LA36_93 >= '\u000B' && LA36_93 <= '\f')||(LA36_93 >= '\u000E' && LA36_93 <= '\u001F')||(LA36_93 >= '$' && LA36_93 <= '\'')||LA36_93=='.'||LA36_93=='<'||LA36_93=='>'||(LA36_93 >= '@' && LA36_93 <= 'Z')||(LA36_93 >= '_' && LA36_93 <= 'z')||LA36_93=='|'||(LA36_93 >= '\u007F' && LA36_93 <= '\u2FFF')||(LA36_93 >= '\u3001' && LA36_93 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_93=='\\') ) {s = 31;}
						else if ( (LA36_93=='+') ) {s = 32;}
						else if ( (LA36_93=='-') ) {s = 33;}
						else if ( (LA36_93=='=') ) {s = 49;}
						else if ( (LA36_93=='#') ) {s = 34;}
						else if ( (LA36_93=='/') ) {s = 35;}
						else if ( (LA36_93=='*'||LA36_93=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 18 : 
						int LA36_96 = input.LA(1);
						s = -1;
						if ( ((LA36_96 >= '0' && LA36_96 <= '9')) ) {s = 98;}
						else if ( (LA36_96=='(') ) {s = 36;}
						else if ( ((LA36_96 >= '\u0000' && LA36_96 <= '\b')||(LA36_96 >= '\u000B' && LA36_96 <= '\f')||(LA36_96 >= '\u000E' && LA36_96 <= '\u001F')||(LA36_96 >= '$' && LA36_96 <= '\'')||LA36_96=='.'||LA36_96=='<'||LA36_96=='>'||(LA36_96 >= '@' && LA36_96 <= 'Z')||(LA36_96 >= '_' && LA36_96 <= 'z')||LA36_96=='|'||(LA36_96 >= '\u007F' && LA36_96 <= '\u2FFF')||(LA36_96 >= '\u3001' && LA36_96 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_96=='\\') ) {s = 31;}
						else if ( (LA36_96=='+') ) {s = 32;}
						else if ( (LA36_96=='-') ) {s = 33;}
						else if ( (LA36_96=='=') ) {s = 49;}
						else if ( (LA36_96=='#') ) {s = 34;}
						else if ( (LA36_96=='/') ) {s = 35;}
						else if ( (LA36_96=='*'||LA36_96=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 19 : 
						int LA36_15 = input.LA(1);
						s = -1;
						if ( (LA36_15=='\\') ) {s = 45;}
						else if ( ((LA36_15 >= '\u0000' && LA36_15 <= '!')||(LA36_15 >= '#' && LA36_15 <= ')')||(LA36_15 >= '+' && LA36_15 <= '>')||(LA36_15 >= '@' && LA36_15 <= '[')||(LA36_15 >= ']' && LA36_15 <= '\uFFFF')) ) {s = 46;}
						else if ( (LA36_15=='*'||LA36_15=='?') ) {s = 47;}
						else s = 44;
						if ( s>=0 ) return s;
						break;

					case 20 : 
						int LA36_51 = input.LA(1);
						s = -1;
						if ( (LA36_51=='(') ) {s = 36;}
						else if ( ((LA36_51 >= '\u0000' && LA36_51 <= '\b')||(LA36_51 >= '\u000B' && LA36_51 <= '\f')||(LA36_51 >= '\u000E' && LA36_51 <= '\u001F')||(LA36_51 >= '$' && LA36_51 <= '\'')||LA36_51=='.'||(LA36_51 >= '0' && LA36_51 <= '9')||LA36_51=='<'||LA36_51=='>'||(LA36_51 >= '@' && LA36_51 <= 'Z')||(LA36_51 >= '_' && LA36_51 <= 'z')||LA36_51=='|'||(LA36_51 >= '\u007F' && LA36_51 <= '\u2FFF')||(LA36_51 >= '\u3001' && LA36_51 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_51=='\\') ) {s = 31;}
						else if ( (LA36_51=='+') ) {s = 32;}
						else if ( (LA36_51=='-') ) {s = 33;}
						else if ( (LA36_51=='=') ) {s = 49;}
						else if ( (LA36_51=='#') ) {s = 34;}
						else if ( (LA36_51=='/') ) {s = 35;}
						else if ( (LA36_51=='*'||LA36_51=='?') ) {s = 37;}
						else s = 66;
						if ( s>=0 ) return s;
						break;

					case 21 : 
						int LA36_60 = input.LA(1);
						s = -1;
						if ( (LA36_60=='(') ) {s = 36;}
						else if ( ((LA36_60 >= '\u0000' && LA36_60 <= '\b')||(LA36_60 >= '\u000B' && LA36_60 <= '\f')||(LA36_60 >= '\u000E' && LA36_60 <= '\u001F')||(LA36_60 >= '$' && LA36_60 <= '\'')||LA36_60=='.'||(LA36_60 >= '0' && LA36_60 <= '9')||LA36_60=='<'||LA36_60=='>'||(LA36_60 >= '@' && LA36_60 <= 'Z')||(LA36_60 >= '_' && LA36_60 <= 'z')||LA36_60=='|'||(LA36_60 >= '\u007F' && LA36_60 <= '\u2FFF')||(LA36_60 >= '\u3001' && LA36_60 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_60=='\\') ) {s = 31;}
						else if ( (LA36_60=='+') ) {s = 32;}
						else if ( (LA36_60=='-') ) {s = 33;}
						else if ( (LA36_60=='=') ) {s = 49;}
						else if ( (LA36_60=='#') ) {s = 34;}
						else if ( (LA36_60=='/') ) {s = 35;}
						else if ( (LA36_60=='*'||LA36_60=='?') ) {s = 37;}
						else s = 73;
						if ( s>=0 ) return s;
						break;

					case 22 : 
						int LA36_11 = input.LA(1);
						s = -1;
						if ( ((LA36_11 >= '\u0000' && LA36_11 <= '\b')||(LA36_11 >= '\u000B' && LA36_11 <= '\f')||(LA36_11 >= '\u000E' && LA36_11 <= '\u001F')||(LA36_11 >= '#' && LA36_11 <= '\'')||LA36_11=='+'||(LA36_11 >= '-' && LA36_11 <= '9')||(LA36_11 >= '<' && LA36_11 <= '>')||(LA36_11 >= '@' && LA36_11 <= 'Z')||LA36_11=='\\'||(LA36_11 >= '_' && LA36_11 <= 'z')||LA36_11=='|'||(LA36_11 >= '\u007F' && LA36_11 <= '\u2FFF')||(LA36_11 >= '\u3001' && LA36_11 <= '\uFFFF')) ) {s = 37;}
						else s = 40;
						if ( s>=0 ) return s;
						break;

					case 23 : 
						int LA36_72 = input.LA(1);
						s = -1;
						if ( (LA36_72=='(') ) {s = 36;}
						else if ( ((LA36_72 >= '0' && LA36_72 <= '9')) ) {s = 84;}
						else if ( (LA36_72=='\\') ) {s = 31;}
						else if ( (LA36_72=='+') ) {s = 32;}
						else if ( (LA36_72=='-') ) {s = 81;}
						else if ( (LA36_72=='=') ) {s = 49;}
						else if ( (LA36_72=='#') ) {s = 34;}
						else if ( (LA36_72=='/') ) {s = 82;}
						else if ( (LA36_72=='.') ) {s = 83;}
						else if ( ((LA36_72 >= '\u0000' && LA36_72 <= '\b')||(LA36_72 >= '\u000B' && LA36_72 <= '\f')||(LA36_72 >= '\u000E' && LA36_72 <= '\u001F')||(LA36_72 >= '$' && LA36_72 <= '\'')||LA36_72=='<'||LA36_72=='>'||(LA36_72 >= '@' && LA36_72 <= 'Z')||(LA36_72 >= '_' && LA36_72 <= 'z')||LA36_72=='|'||(LA36_72 >= '\u007F' && LA36_72 <= '\u2FFF')||(LA36_72 >= '\u3001' && LA36_72 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_72=='*'||LA36_72=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 24 : 
						int LA36_67 = input.LA(1);
						s = -1;
						if ( (LA36_67=='(') ) {s = 36;}
						else if ( ((LA36_67 >= '\u0000' && LA36_67 <= '\b')||(LA36_67 >= '\u000B' && LA36_67 <= '\f')||(LA36_67 >= '\u000E' && LA36_67 <= '\u001F')||(LA36_67 >= '$' && LA36_67 <= '\'')||LA36_67=='.'||(LA36_67 >= '0' && LA36_67 <= '9')||LA36_67=='<'||LA36_67=='>'||(LA36_67 >= '@' && LA36_67 <= 'Z')||(LA36_67 >= '_' && LA36_67 <= 'z')||LA36_67=='|'||(LA36_67 >= '\u007F' && LA36_67 <= '\u2FFF')||(LA36_67 >= '\u3001' && LA36_67 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_67=='\\') ) {s = 31;}
						else if ( (LA36_67=='+') ) {s = 32;}
						else if ( (LA36_67=='-') ) {s = 33;}
						else if ( (LA36_67=='=') ) {s = 49;}
						else if ( (LA36_67=='#') ) {s = 34;}
						else if ( (LA36_67=='/') ) {s = 35;}
						else if ( (LA36_67=='*'||LA36_67=='?') ) {s = 37;}
						else s = 76;
						if ( s>=0 ) return s;
						break;

					case 25 : 
						int LA36_79 = input.LA(1);
						s = -1;
						if ( (LA36_79=='(') ) {s = 36;}
						else if ( ((LA36_79 >= '0' && LA36_79 <= '9')) ) {s = 89;}
						else if ( (LA36_79=='\\') ) {s = 31;}
						else if ( (LA36_79=='+') ) {s = 32;}
						else if ( (LA36_79=='-') ) {s = 33;}
						else if ( (LA36_79=='=') ) {s = 49;}
						else if ( (LA36_79=='#') ) {s = 34;}
						else if ( (LA36_79=='/') ) {s = 35;}
						else if ( ((LA36_79 >= '\u0000' && LA36_79 <= '\b')||(LA36_79 >= '\u000B' && LA36_79 <= '\f')||(LA36_79 >= '\u000E' && LA36_79 <= '\u001F')||(LA36_79 >= '$' && LA36_79 <= '\'')||LA36_79=='.'||LA36_79=='<'||LA36_79=='>'||(LA36_79 >= '@' && LA36_79 <= 'Z')||(LA36_79 >= '_' && LA36_79 <= 'z')||LA36_79=='|'||(LA36_79 >= '\u007F' && LA36_79 <= '\u2FFF')||(LA36_79 >= '\u3001' && LA36_79 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_79=='*'||LA36_79=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 26 : 
						int LA36_21 = input.LA(1);
						s = -1;
						if ( (LA36_21=='O'||LA36_21=='o') ) {s = 52;}
						else if ( (LA36_21=='E'||LA36_21=='e') ) {s = 53;}
						else if ( ((LA36_21 >= '\u0000' && LA36_21 <= '\b')||(LA36_21 >= '\u000B' && LA36_21 <= '\f')||(LA36_21 >= '\u000E' && LA36_21 <= '\u001F')||(LA36_21 >= '$' && LA36_21 <= '\'')||LA36_21=='.'||(LA36_21 >= '0' && LA36_21 <= '9')||LA36_21=='<'||LA36_21=='>'||(LA36_21 >= '@' && LA36_21 <= 'D')||(LA36_21 >= 'F' && LA36_21 <= 'N')||(LA36_21 >= 'P' && LA36_21 <= 'Z')||(LA36_21 >= '_' && LA36_21 <= 'd')||(LA36_21 >= 'f' && LA36_21 <= 'n')||(LA36_21 >= 'p' && LA36_21 <= 'z')||LA36_21=='|'||(LA36_21 >= '\u007F' && LA36_21 <= '\u2FFF')||(LA36_21 >= '\u3001' && LA36_21 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_21=='\\') ) {s = 31;}
						else if ( (LA36_21=='+') ) {s = 32;}
						else if ( (LA36_21=='-') ) {s = 33;}
						else if ( (LA36_21=='=') ) {s = 49;}
						else if ( (LA36_21=='#') ) {s = 34;}
						else if ( (LA36_21=='/') ) {s = 35;}
						else if ( (LA36_21=='(') ) {s = 36;}
						else if ( (LA36_21=='*'||LA36_21=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 27 : 
						int LA36_56 = input.LA(1);
						s = -1;
						if ( (LA36_56=='(') ) {s = 36;}
						else if ( ((LA36_56 >= '0' && LA36_56 <= '9')) ) {s = 71;}
						else if ( (LA36_56=='\\') ) {s = 31;}
						else if ( (LA36_56=='+') ) {s = 32;}
						else if ( (LA36_56=='-') ) {s = 33;}
						else if ( (LA36_56=='=') ) {s = 49;}
						else if ( (LA36_56=='#') ) {s = 34;}
						else if ( (LA36_56=='/') ) {s = 35;}
						else if ( ((LA36_56 >= '\u0000' && LA36_56 <= '\b')||(LA36_56 >= '\u000B' && LA36_56 <= '\f')||(LA36_56 >= '\u000E' && LA36_56 <= '\u001F')||(LA36_56 >= '$' && LA36_56 <= '\'')||LA36_56=='.'||LA36_56=='<'||LA36_56=='>'||(LA36_56 >= '@' && LA36_56 <= 'Z')||(LA36_56 >= '_' && LA36_56 <= 'z')||LA36_56=='|'||(LA36_56 >= '\u007F' && LA36_56 <= '\u2FFF')||(LA36_56 >= '\u3001' && LA36_56 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_56=='*'||LA36_56=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 28 : 
						int LA36_45 = input.LA(1);
						s = -1;
						if ( ((LA36_45 >= '\u0000' && LA36_45 <= '\uFFFF')) ) {s = 62;}
						if ( s>=0 ) return s;
						break;

					case 29 : 
						int LA36_98 = input.LA(1);
						s = -1;
						if ( (LA36_98=='(') ) {s = 36;}
						else if ( ((LA36_98 >= '\u0000' && LA36_98 <= '\b')||(LA36_98 >= '\u000B' && LA36_98 <= '\f')||(LA36_98 >= '\u000E' && LA36_98 <= '\u001F')||(LA36_98 >= '$' && LA36_98 <= '\'')||LA36_98=='.'||(LA36_98 >= '0' && LA36_98 <= '9')||LA36_98=='<'||LA36_98=='>'||(LA36_98 >= '@' && LA36_98 <= 'Z')||(LA36_98 >= '_' && LA36_98 <= 'z')||LA36_98=='|'||(LA36_98 >= '\u007F' && LA36_98 <= '\u2FFF')||(LA36_98 >= '\u3001' && LA36_98 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_98=='\\') ) {s = 31;}
						else if ( (LA36_98=='+') ) {s = 32;}
						else if ( (LA36_98=='-') ) {s = 33;}
						else if ( (LA36_98=='=') ) {s = 49;}
						else if ( (LA36_98=='#') ) {s = 34;}
						else if ( (LA36_98=='/') ) {s = 35;}
						else if ( (LA36_98=='*'||LA36_98=='?') ) {s = 37;}
						else s = 39;
						if ( s>=0 ) return s;
						break;

					case 30 : 
						int LA36_18 = input.LA(1);
						s = -1;
						if ( (LA36_18=='O') ) {s = 48;}
						else if ( ((LA36_18 >= '\u0000' && LA36_18 <= '\b')||(LA36_18 >= '\u000B' && LA36_18 <= '\f')||(LA36_18 >= '\u000E' && LA36_18 <= '\u001F')||(LA36_18 >= '$' && LA36_18 <= '\'')||LA36_18=='.'||(LA36_18 >= '0' && LA36_18 <= '9')||LA36_18=='<'||LA36_18=='>'||(LA36_18 >= '@' && LA36_18 <= 'N')||(LA36_18 >= 'P' && LA36_18 <= 'Z')||(LA36_18 >= '_' && LA36_18 <= 'z')||LA36_18=='|'||(LA36_18 >= '\u007F' && LA36_18 <= '\u2FFF')||(LA36_18 >= '\u3001' && LA36_18 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_18=='\\') ) {s = 31;}
						else if ( (LA36_18=='+') ) {s = 32;}
						else if ( (LA36_18=='-') ) {s = 33;}
						else if ( (LA36_18=='=') ) {s = 49;}
						else if ( (LA36_18=='#') ) {s = 34;}
						else if ( (LA36_18=='/') ) {s = 35;}
						else if ( (LA36_18=='(') ) {s = 36;}
						else if ( (LA36_18=='*'||LA36_18=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 31 : 
						int LA36_88 = input.LA(1);
						s = -1;
						if ( (LA36_88=='(') ) {s = 36;}
						else if ( (LA36_88=='.') ) {s = 79;}
						else if ( (LA36_88=='\\') ) {s = 31;}
						else if ( (LA36_88=='+') ) {s = 32;}
						else if ( (LA36_88=='-') ) {s = 33;}
						else if ( (LA36_88=='=') ) {s = 49;}
						else if ( (LA36_88=='#') ) {s = 34;}
						else if ( (LA36_88=='/') ) {s = 35;}
						else if ( ((LA36_88 >= '0' && LA36_88 <= '9')) ) {s = 88;}
						else if ( ((LA36_88 >= '\u0000' && LA36_88 <= '\b')||(LA36_88 >= '\u000B' && LA36_88 <= '\f')||(LA36_88 >= '\u000E' && LA36_88 <= '\u001F')||(LA36_88 >= '$' && LA36_88 <= '\'')||LA36_88=='<'||LA36_88=='>'||(LA36_88 >= '@' && LA36_88 <= 'Z')||(LA36_88 >= '_' && LA36_88 <= 'z')||LA36_88=='|'||(LA36_88 >= '\u007F' && LA36_88 <= '\u2FFF')||(LA36_88 >= '\u3001' && LA36_88 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_88=='*'||LA36_88=='?') ) {s = 37;}
						else s = 54;
						if ( s>=0 ) return s;
						break;

					case 32 : 
						int LA36_19 = input.LA(1);
						s = -1;
						if ( (LA36_19=='N'||LA36_19=='n') ) {s = 50;}
						else if ( ((LA36_19 >= '\u0000' && LA36_19 <= '\b')||(LA36_19 >= '\u000B' && LA36_19 <= '\f')||(LA36_19 >= '\u000E' && LA36_19 <= '\u001F')||(LA36_19 >= '$' && LA36_19 <= '\'')||LA36_19=='.'||(LA36_19 >= '0' && LA36_19 <= '9')||LA36_19=='<'||LA36_19=='>'||(LA36_19 >= '@' && LA36_19 <= 'M')||(LA36_19 >= 'O' && LA36_19 <= 'Z')||(LA36_19 >= '_' && LA36_19 <= 'm')||(LA36_19 >= 'o' && LA36_19 <= 'z')||LA36_19=='|'||(LA36_19 >= '\u007F' && LA36_19 <= '\u2FFF')||(LA36_19 >= '\u3001' && LA36_19 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_19=='\\') ) {s = 31;}
						else if ( (LA36_19=='+') ) {s = 32;}
						else if ( (LA36_19=='-') ) {s = 33;}
						else if ( (LA36_19=='=') ) {s = 49;}
						else if ( (LA36_19=='#') ) {s = 34;}
						else if ( (LA36_19=='/') ) {s = 35;}
						else if ( (LA36_19=='(') ) {s = 36;}
						else if ( (LA36_19=='*'||LA36_19=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 33 : 
						int LA36_32 = input.LA(1);
						s = -1;
						if ( (LA36_32=='(') ) {s = 36;}
						else if ( ((LA36_32 >= '\u0000' && LA36_32 <= '\b')||(LA36_32 >= '\u000B' && LA36_32 <= '\f')||(LA36_32 >= '\u000E' && LA36_32 <= '\u001F')||(LA36_32 >= '$' && LA36_32 <= '\'')||LA36_32=='.'||(LA36_32 >= '0' && LA36_32 <= '9')||LA36_32=='<'||LA36_32=='>'||(LA36_32 >= '@' && LA36_32 <= 'Z')||(LA36_32 >= '_' && LA36_32 <= 'z')||LA36_32=='|'||(LA36_32 >= '\u007F' && LA36_32 <= '\u2FFF')||(LA36_32 >= '\u3001' && LA36_32 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_32=='\\') ) {s = 31;}
						else if ( (LA36_32=='+') ) {s = 32;}
						else if ( (LA36_32=='-') ) {s = 33;}
						else if ( (LA36_32=='=') ) {s = 49;}
						else if ( (LA36_32=='#') ) {s = 34;}
						else if ( (LA36_32=='/') ) {s = 35;}
						else if ( (LA36_32=='*'||LA36_32=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 34 : 
						int LA36_20 = input.LA(1);
						s = -1;
						if ( (LA36_20=='R'||LA36_20=='r') ) {s = 51;}
						else if ( ((LA36_20 >= '\u0000' && LA36_20 <= '\b')||(LA36_20 >= '\u000B' && LA36_20 <= '\f')||(LA36_20 >= '\u000E' && LA36_20 <= '\u001F')||(LA36_20 >= '$' && LA36_20 <= '\'')||LA36_20=='.'||(LA36_20 >= '0' && LA36_20 <= '9')||LA36_20=='<'||LA36_20=='>'||(LA36_20 >= '@' && LA36_20 <= 'Q')||(LA36_20 >= 'S' && LA36_20 <= 'Z')||(LA36_20 >= '_' && LA36_20 <= 'q')||(LA36_20 >= 's' && LA36_20 <= 'z')||LA36_20=='|'||(LA36_20 >= '\u007F' && LA36_20 <= '\u2FFF')||(LA36_20 >= '\u3001' && LA36_20 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_20=='\\') ) {s = 31;}
						else if ( (LA36_20=='+') ) {s = 32;}
						else if ( (LA36_20=='-') ) {s = 33;}
						else if ( (LA36_20=='=') ) {s = 49;}
						else if ( (LA36_20=='#') ) {s = 34;}
						else if ( (LA36_20=='/') ) {s = 35;}
						else if ( (LA36_20=='(') ) {s = 36;}
						else if ( (LA36_20=='*'||LA36_20=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 35 : 
						int LA36_33 = input.LA(1);
						s = -1;
						if ( (LA36_33=='(') ) {s = 36;}
						else if ( ((LA36_33 >= '\u0000' && LA36_33 <= '\b')||(LA36_33 >= '\u000B' && LA36_33 <= '\f')||(LA36_33 >= '\u000E' && LA36_33 <= '\u001F')||(LA36_33 >= '$' && LA36_33 <= '\'')||LA36_33=='.'||(LA36_33 >= '0' && LA36_33 <= '9')||LA36_33=='<'||LA36_33=='>'||(LA36_33 >= '@' && LA36_33 <= 'Z')||(LA36_33 >= '_' && LA36_33 <= 'z')||LA36_33=='|'||(LA36_33 >= '\u007F' && LA36_33 <= '\u2FFF')||(LA36_33 >= '\u3001' && LA36_33 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_33=='\\') ) {s = 31;}
						else if ( (LA36_33=='+') ) {s = 32;}
						else if ( (LA36_33=='-') ) {s = 33;}
						else if ( (LA36_33=='=') ) {s = 49;}
						else if ( (LA36_33=='#') ) {s = 34;}
						else if ( (LA36_33=='/') ) {s = 35;}
						else if ( (LA36_33=='*'||LA36_33=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 36 : 
						int LA36_71 = input.LA(1);
						s = -1;
						if ( (LA36_71=='(') ) {s = 36;}
						else if ( ((LA36_71 >= '0' && LA36_71 <= '9')) ) {s = 80;}
						else if ( (LA36_71=='\\') ) {s = 31;}
						else if ( (LA36_71=='+') ) {s = 32;}
						else if ( (LA36_71=='-') ) {s = 81;}
						else if ( (LA36_71=='=') ) {s = 49;}
						else if ( (LA36_71=='#') ) {s = 34;}
						else if ( (LA36_71=='/') ) {s = 82;}
						else if ( (LA36_71=='.') ) {s = 83;}
						else if ( ((LA36_71 >= '\u0000' && LA36_71 <= '\b')||(LA36_71 >= '\u000B' && LA36_71 <= '\f')||(LA36_71 >= '\u000E' && LA36_71 <= '\u001F')||(LA36_71 >= '$' && LA36_71 <= '\'')||LA36_71=='<'||LA36_71=='>'||(LA36_71 >= '@' && LA36_71 <= 'Z')||(LA36_71 >= '_' && LA36_71 <= 'z')||LA36_71=='|'||(LA36_71 >= '\u007F' && LA36_71 <= '\u2FFF')||(LA36_71 >= '\u3001' && LA36_71 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_71=='*'||LA36_71=='?') ) {s = 37;}
						else s = 54;
						if ( s>=0 ) return s;
						break;

					case 37 : 
						int LA36_90 = input.LA(1);
						s = -1;
						if ( (LA36_90=='(') ) {s = 36;}
						else if ( ((LA36_90 >= '0' && LA36_90 <= '9')) ) {s = 92;}
						else if ( (LA36_90=='\\') ) {s = 31;}
						else if ( (LA36_90=='+') ) {s = 32;}
						else if ( (LA36_90=='-') ) {s = 33;}
						else if ( (LA36_90=='=') ) {s = 49;}
						else if ( (LA36_90=='#') ) {s = 34;}
						else if ( (LA36_90=='/') ) {s = 35;}
						else if ( ((LA36_90 >= '\u0000' && LA36_90 <= '\b')||(LA36_90 >= '\u000B' && LA36_90 <= '\f')||(LA36_90 >= '\u000E' && LA36_90 <= '\u001F')||(LA36_90 >= '$' && LA36_90 <= '\'')||LA36_90=='.'||LA36_90=='<'||LA36_90=='>'||(LA36_90 >= '@' && LA36_90 <= 'Z')||(LA36_90 >= '_' && LA36_90 <= 'z')||LA36_90=='|'||(LA36_90 >= '\u007F' && LA36_90 <= '\u2FFF')||(LA36_90 >= '\u3001' && LA36_90 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_90=='*'||LA36_90=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 38 : 
						int LA36_49 = input.LA(1);
						s = -1;
						if ( (LA36_49=='(') ) {s = 36;}
						else if ( ((LA36_49 >= '\u0000' && LA36_49 <= '\b')||(LA36_49 >= '\u000B' && LA36_49 <= '\f')||(LA36_49 >= '\u000E' && LA36_49 <= '\u001F')||(LA36_49 >= '$' && LA36_49 <= '\'')||LA36_49=='.'||(LA36_49 >= '0' && LA36_49 <= '9')||LA36_49=='<'||LA36_49=='>'||(LA36_49 >= '@' && LA36_49 <= 'Z')||(LA36_49 >= '_' && LA36_49 <= 'z')||LA36_49=='|'||(LA36_49 >= '\u007F' && LA36_49 <= '\u2FFF')||(LA36_49 >= '\u3001' && LA36_49 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_49=='\\') ) {s = 31;}
						else if ( (LA36_49=='+') ) {s = 32;}
						else if ( (LA36_49=='-') ) {s = 33;}
						else if ( (LA36_49=='=') ) {s = 49;}
						else if ( (LA36_49=='#') ) {s = 34;}
						else if ( (LA36_49=='/') ) {s = 35;}
						else if ( (LA36_49=='*'||LA36_49=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 39 : 
						int LA36_95 = input.LA(1);
						s = -1;
						if ( (LA36_95=='(') ) {s = 36;}
						else if ( ((LA36_95 >= '0' && LA36_95 <= '9')) ) {s = 97;}
						else if ( (LA36_95=='\\') ) {s = 31;}
						else if ( (LA36_95=='+') ) {s = 32;}
						else if ( (LA36_95=='-') ) {s = 33;}
						else if ( (LA36_95=='=') ) {s = 49;}
						else if ( (LA36_95=='#') ) {s = 34;}
						else if ( (LA36_95=='/') ) {s = 35;}
						else if ( ((LA36_95 >= '\u0000' && LA36_95 <= '\b')||(LA36_95 >= '\u000B' && LA36_95 <= '\f')||(LA36_95 >= '\u000E' && LA36_95 <= '\u001F')||(LA36_95 >= '$' && LA36_95 <= '\'')||LA36_95=='.'||LA36_95=='<'||LA36_95=='>'||(LA36_95 >= '@' && LA36_95 <= 'Z')||(LA36_95 >= '_' && LA36_95 <= 'z')||LA36_95=='|'||(LA36_95 >= '\u007F' && LA36_95 <= '\u2FFF')||(LA36_95 >= '\u3001' && LA36_95 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_95=='*'||LA36_95=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 40 : 
						int LA36_34 = input.LA(1);
						s = -1;
						if ( (LA36_34=='(') ) {s = 36;}
						else if ( ((LA36_34 >= '\u0000' && LA36_34 <= '\b')||(LA36_34 >= '\u000B' && LA36_34 <= '\f')||(LA36_34 >= '\u000E' && LA36_34 <= '\u001F')||(LA36_34 >= '$' && LA36_34 <= '\'')||LA36_34=='.'||(LA36_34 >= '0' && LA36_34 <= '9')||LA36_34=='<'||LA36_34=='>'||(LA36_34 >= '@' && LA36_34 <= 'Z')||(LA36_34 >= '_' && LA36_34 <= 'z')||LA36_34=='|'||(LA36_34 >= '\u007F' && LA36_34 <= '\u2FFF')||(LA36_34 >= '\u3001' && LA36_34 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_34=='\\') ) {s = 31;}
						else if ( (LA36_34=='+') ) {s = 32;}
						else if ( (LA36_34=='-') ) {s = 33;}
						else if ( (LA36_34=='=') ) {s = 49;}
						else if ( (LA36_34=='#') ) {s = 34;}
						else if ( (LA36_34=='/') ) {s = 35;}
						else if ( (LA36_34=='*'||LA36_34=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 41 : 
						int LA36_87 = input.LA(1);
						s = -1;
						if ( ((LA36_87 >= '0' && LA36_87 <= '9')) ) {s = 91;}
						else if ( (LA36_87=='(') ) {s = 36;}
						else if ( ((LA36_87 >= '\u0000' && LA36_87 <= '\b')||(LA36_87 >= '\u000B' && LA36_87 <= '\f')||(LA36_87 >= '\u000E' && LA36_87 <= '\u001F')||(LA36_87 >= '$' && LA36_87 <= '\'')||LA36_87=='.'||LA36_87=='<'||LA36_87=='>'||(LA36_87 >= '@' && LA36_87 <= 'Z')||(LA36_87 >= '_' && LA36_87 <= 'z')||LA36_87=='|'||(LA36_87 >= '\u007F' && LA36_87 <= '\u2FFF')||(LA36_87 >= '\u3001' && LA36_87 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_87=='\\') ) {s = 31;}
						else if ( (LA36_87=='+') ) {s = 32;}
						else if ( (LA36_87=='-') ) {s = 33;}
						else if ( (LA36_87=='=') ) {s = 49;}
						else if ( (LA36_87=='#') ) {s = 34;}
						else if ( (LA36_87=='/') ) {s = 35;}
						else if ( (LA36_87=='*'||LA36_87=='?') ) {s = 37;}
						else s = 39;
						if ( s>=0 ) return s;
						break;

					case 42 : 
						int LA36_35 = input.LA(1);
						s = -1;
						if ( (LA36_35=='(') ) {s = 36;}
						else if ( ((LA36_35 >= '\u0000' && LA36_35 <= '\b')||(LA36_35 >= '\u000B' && LA36_35 <= '\f')||(LA36_35 >= '\u000E' && LA36_35 <= '\u001F')||(LA36_35 >= '$' && LA36_35 <= '\'')||LA36_35=='.'||(LA36_35 >= '0' && LA36_35 <= '9')||LA36_35=='<'||LA36_35=='>'||(LA36_35 >= '@' && LA36_35 <= 'Z')||(LA36_35 >= '_' && LA36_35 <= 'z')||LA36_35=='|'||(LA36_35 >= '\u007F' && LA36_35 <= '\u2FFF')||(LA36_35 >= '\u3001' && LA36_35 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_35=='\\') ) {s = 31;}
						else if ( (LA36_35=='+') ) {s = 32;}
						else if ( (LA36_35=='-') ) {s = 33;}
						else if ( (LA36_35=='=') ) {s = 49;}
						else if ( (LA36_35=='#') ) {s = 34;}
						else if ( (LA36_35=='/') ) {s = 35;}
						else if ( (LA36_35=='*'||LA36_35=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 43 : 
						int LA36_57 = input.LA(1);
						s = -1;
						if ( (LA36_57=='(') ) {s = 36;}
						else if ( ((LA36_57 >= '0' && LA36_57 <= '9')) ) {s = 72;}
						else if ( (LA36_57=='\\') ) {s = 31;}
						else if ( (LA36_57=='+') ) {s = 32;}
						else if ( (LA36_57=='-') ) {s = 33;}
						else if ( (LA36_57=='=') ) {s = 49;}
						else if ( (LA36_57=='#') ) {s = 34;}
						else if ( (LA36_57=='/') ) {s = 35;}
						else if ( ((LA36_57 >= '\u0000' && LA36_57 <= '\b')||(LA36_57 >= '\u000B' && LA36_57 <= '\f')||(LA36_57 >= '\u000E' && LA36_57 <= '\u001F')||(LA36_57 >= '$' && LA36_57 <= '\'')||LA36_57=='.'||LA36_57=='<'||LA36_57=='>'||(LA36_57 >= '@' && LA36_57 <= 'Z')||(LA36_57 >= '_' && LA36_57 <= 'z')||LA36_57=='|'||(LA36_57 >= '\u007F' && LA36_57 <= '\u2FFF')||(LA36_57 >= '\u3001' && LA36_57 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_57=='*'||LA36_57=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 44 : 
						int LA36_81 = input.LA(1);
						s = -1;
						if ( (LA36_81=='(') ) {s = 36;}
						else if ( ((LA36_81 >= '0' && LA36_81 <= '9')) ) {s = 90;}
						else if ( (LA36_81=='\\') ) {s = 31;}
						else if ( (LA36_81=='+') ) {s = 32;}
						else if ( (LA36_81=='-') ) {s = 33;}
						else if ( (LA36_81=='=') ) {s = 49;}
						else if ( (LA36_81=='#') ) {s = 34;}
						else if ( (LA36_81=='/') ) {s = 35;}
						else if ( ((LA36_81 >= '\u0000' && LA36_81 <= '\b')||(LA36_81 >= '\u000B' && LA36_81 <= '\f')||(LA36_81 >= '\u000E' && LA36_81 <= '\u001F')||(LA36_81 >= '$' && LA36_81 <= '\'')||LA36_81=='.'||LA36_81=='<'||LA36_81=='>'||(LA36_81 >= '@' && LA36_81 <= 'Z')||(LA36_81 >= '_' && LA36_81 <= 'z')||LA36_81=='|'||(LA36_81 >= '\u007F' && LA36_81 <= '\u2FFF')||(LA36_81 >= '\u3001' && LA36_81 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_81=='*'||LA36_81=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 45 : 
						int LA36_50 = input.LA(1);
						s = -1;
						if ( (LA36_50=='D'||LA36_50=='d') ) {s = 65;}
						else if ( (LA36_50=='(') ) {s = 36;}
						else if ( ((LA36_50 >= '\u0000' && LA36_50 <= '\b')||(LA36_50 >= '\u000B' && LA36_50 <= '\f')||(LA36_50 >= '\u000E' && LA36_50 <= '\u001F')||(LA36_50 >= '$' && LA36_50 <= '\'')||LA36_50=='.'||(LA36_50 >= '0' && LA36_50 <= '9')||LA36_50=='<'||LA36_50=='>'||(LA36_50 >= '@' && LA36_50 <= 'C')||(LA36_50 >= 'E' && LA36_50 <= 'Z')||(LA36_50 >= '_' && LA36_50 <= 'c')||(LA36_50 >= 'e' && LA36_50 <= 'z')||LA36_50=='|'||(LA36_50 >= '\u007F' && LA36_50 <= '\u2FFF')||(LA36_50 >= '\u3001' && LA36_50 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_50=='\\') ) {s = 31;}
						else if ( (LA36_50=='+') ) {s = 32;}
						else if ( (LA36_50=='-') ) {s = 33;}
						else if ( (LA36_50=='=') ) {s = 49;}
						else if ( (LA36_50=='#') ) {s = 34;}
						else if ( (LA36_50=='/') ) {s = 35;}
						else if ( (LA36_50=='*'||LA36_50=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 46 : 
						int LA36_84 = input.LA(1);
						s = -1;
						if ( (LA36_84=='(') ) {s = 36;}
						else if ( (LA36_84=='.') ) {s = 83;}
						else if ( (LA36_84=='\\') ) {s = 31;}
						else if ( (LA36_84=='+') ) {s = 32;}
						else if ( (LA36_84=='-') ) {s = 81;}
						else if ( (LA36_84=='=') ) {s = 49;}
						else if ( (LA36_84=='#') ) {s = 34;}
						else if ( (LA36_84=='/') ) {s = 82;}
						else if ( ((LA36_84 >= '\u0000' && LA36_84 <= '\b')||(LA36_84 >= '\u000B' && LA36_84 <= '\f')||(LA36_84 >= '\u000E' && LA36_84 <= '\u001F')||(LA36_84 >= '$' && LA36_84 <= '\'')||(LA36_84 >= '0' && LA36_84 <= '9')||LA36_84=='<'||LA36_84=='>'||(LA36_84 >= '@' && LA36_84 <= 'Z')||(LA36_84 >= '_' && LA36_84 <= 'z')||LA36_84=='|'||(LA36_84 >= '\u007F' && LA36_84 <= '\u2FFF')||(LA36_84 >= '\u3001' && LA36_84 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_84=='*'||LA36_84=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 47 : 
						int LA36_58 = input.LA(1);
						s = -1;
						if ( (LA36_58=='(') ) {s = 36;}
						else if ( ((LA36_58 >= '0' && LA36_58 <= '9')) ) {s = 72;}
						else if ( (LA36_58=='\\') ) {s = 31;}
						else if ( (LA36_58=='+') ) {s = 32;}
						else if ( (LA36_58=='-') ) {s = 33;}
						else if ( (LA36_58=='=') ) {s = 49;}
						else if ( (LA36_58=='#') ) {s = 34;}
						else if ( (LA36_58=='/') ) {s = 35;}
						else if ( ((LA36_58 >= '\u0000' && LA36_58 <= '\b')||(LA36_58 >= '\u000B' && LA36_58 <= '\f')||(LA36_58 >= '\u000E' && LA36_58 <= '\u001F')||(LA36_58 >= '$' && LA36_58 <= '\'')||LA36_58=='.'||LA36_58=='<'||LA36_58=='>'||(LA36_58 >= '@' && LA36_58 <= 'Z')||(LA36_58 >= '_' && LA36_58 <= 'z')||LA36_58=='|'||(LA36_58 >= '\u007F' && LA36_58 <= '\u2FFF')||(LA36_58 >= '\u3001' && LA36_58 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_58=='*'||LA36_58=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 48 : 
						int LA36_22 = input.LA(1);
						s = -1;
						if ( ((LA36_22 >= '0' && LA36_22 <= '9')) ) {s = 55;}
						else if ( (LA36_22=='.') ) {s = 56;}
						else if ( (LA36_22=='\\') ) {s = 31;}
						else if ( (LA36_22=='+') ) {s = 32;}
						else if ( (LA36_22=='-') ) {s = 57;}
						else if ( (LA36_22=='=') ) {s = 49;}
						else if ( (LA36_22=='#') ) {s = 34;}
						else if ( (LA36_22=='/') ) {s = 58;}
						else if ( (LA36_22=='(') ) {s = 36;}
						else if ( ((LA36_22 >= '\u0000' && LA36_22 <= '\b')||(LA36_22 >= '\u000B' && LA36_22 <= '\f')||(LA36_22 >= '\u000E' && LA36_22 <= '\u001F')||(LA36_22 >= '$' && LA36_22 <= '\'')||LA36_22=='<'||LA36_22=='>'||(LA36_22 >= '@' && LA36_22 <= 'Z')||(LA36_22 >= '_' && LA36_22 <= 'z')||LA36_22=='|'||(LA36_22 >= '\u007F' && LA36_22 <= '\u2FFF')||(LA36_22 >= '\u3001' && LA36_22 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_22=='*'||LA36_22=='?') ) {s = 37;}
						else s = 54;
						if ( s>=0 ) return s;
						break;

					case 49 : 
						int LA36_12 = input.LA(1);
						s = -1;
						if ( (LA36_12=='?') ) {s = 12;}
						else if ( ((LA36_12 >= '\u0000' && LA36_12 <= '\b')||(LA36_12 >= '\u000B' && LA36_12 <= '\f')||(LA36_12 >= '\u000E' && LA36_12 <= '\u001F')||(LA36_12 >= '#' && LA36_12 <= '\'')||LA36_12=='+'||(LA36_12 >= '-' && LA36_12 <= '9')||(LA36_12 >= '<' && LA36_12 <= '>')||(LA36_12 >= '@' && LA36_12 <= 'Z')||LA36_12=='\\'||(LA36_12 >= '_' && LA36_12 <= 'z')||LA36_12=='|'||(LA36_12 >= '\u007F' && LA36_12 <= '\u2FFF')||(LA36_12 >= '\u3001' && LA36_12 <= '\uFFFF')) ) {s = 37;}
						else s = 41;
						if ( s>=0 ) return s;
						break;

					case 50 : 
						int LA36_52 = input.LA(1);
						s = -1;
						if ( (LA36_52=='T'||LA36_52=='t') ) {s = 67;}
						else if ( (LA36_52=='(') ) {s = 36;}
						else if ( ((LA36_52 >= '\u0000' && LA36_52 <= '\b')||(LA36_52 >= '\u000B' && LA36_52 <= '\f')||(LA36_52 >= '\u000E' && LA36_52 <= '\u001F')||(LA36_52 >= '$' && LA36_52 <= '\'')||LA36_52=='.'||(LA36_52 >= '0' && LA36_52 <= '9')||LA36_52=='<'||LA36_52=='>'||(LA36_52 >= '@' && LA36_52 <= 'S')||(LA36_52 >= 'U' && LA36_52 <= 'Z')||(LA36_52 >= '_' && LA36_52 <= 's')||(LA36_52 >= 'u' && LA36_52 <= 'z')||LA36_52=='|'||(LA36_52 >= '\u007F' && LA36_52 <= '\u2FFF')||(LA36_52 >= '\u3001' && LA36_52 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_52=='\\') ) {s = 31;}
						else if ( (LA36_52=='+') ) {s = 32;}
						else if ( (LA36_52=='-') ) {s = 33;}
						else if ( (LA36_52=='=') ) {s = 49;}
						else if ( (LA36_52=='#') ) {s = 34;}
						else if ( (LA36_52=='/') ) {s = 35;}
						else if ( (LA36_52=='*'||LA36_52=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 51 : 
						int LA36_82 = input.LA(1);
						s = -1;
						if ( (LA36_82=='(') ) {s = 36;}
						else if ( ((LA36_82 >= '0' && LA36_82 <= '9')) ) {s = 90;}
						else if ( (LA36_82=='\\') ) {s = 31;}
						else if ( (LA36_82=='+') ) {s = 32;}
						else if ( (LA36_82=='-') ) {s = 33;}
						else if ( (LA36_82=='=') ) {s = 49;}
						else if ( (LA36_82=='#') ) {s = 34;}
						else if ( (LA36_82=='/') ) {s = 35;}
						else if ( ((LA36_82 >= '\u0000' && LA36_82 <= '\b')||(LA36_82 >= '\u000B' && LA36_82 <= '\f')||(LA36_82 >= '\u000E' && LA36_82 <= '\u001F')||(LA36_82 >= '$' && LA36_82 <= '\'')||LA36_82=='.'||LA36_82=='<'||LA36_82=='>'||(LA36_82 >= '@' && LA36_82 <= 'Z')||(LA36_82 >= '_' && LA36_82 <= 'z')||LA36_82=='|'||(LA36_82 >= '\u007F' && LA36_82 <= '\u2FFF')||(LA36_82 >= '\u3001' && LA36_82 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_82=='*'||LA36_82=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 52 : 
						int LA36_53 = input.LA(1);
						s = -1;
						if ( (LA36_53=='A'||LA36_53=='a') ) {s = 68;}
						else if ( (LA36_53=='(') ) {s = 36;}
						else if ( ((LA36_53 >= '\u0000' && LA36_53 <= '\b')||(LA36_53 >= '\u000B' && LA36_53 <= '\f')||(LA36_53 >= '\u000E' && LA36_53 <= '\u001F')||(LA36_53 >= '$' && LA36_53 <= '\'')||LA36_53=='.'||(LA36_53 >= '0' && LA36_53 <= '9')||LA36_53=='<'||LA36_53=='>'||LA36_53=='@'||(LA36_53 >= 'B' && LA36_53 <= 'Z')||(LA36_53 >= '_' && LA36_53 <= '`')||(LA36_53 >= 'b' && LA36_53 <= 'z')||LA36_53=='|'||(LA36_53 >= '\u007F' && LA36_53 <= '\u2FFF')||(LA36_53 >= '\u3001' && LA36_53 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_53=='\\') ) {s = 31;}
						else if ( (LA36_53=='+') ) {s = 32;}
						else if ( (LA36_53=='-') ) {s = 33;}
						else if ( (LA36_53=='=') ) {s = 49;}
						else if ( (LA36_53=='#') ) {s = 34;}
						else if ( (LA36_53=='/') ) {s = 35;}
						else if ( (LA36_53=='*'||LA36_53=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 53 : 
						int LA36_68 = input.LA(1);
						s = -1;
						if ( (LA36_68=='R'||LA36_68=='r') ) {s = 77;}
						else if ( (LA36_68=='(') ) {s = 36;}
						else if ( ((LA36_68 >= '\u0000' && LA36_68 <= '\b')||(LA36_68 >= '\u000B' && LA36_68 <= '\f')||(LA36_68 >= '\u000E' && LA36_68 <= '\u001F')||(LA36_68 >= '$' && LA36_68 <= '\'')||LA36_68=='.'||(LA36_68 >= '0' && LA36_68 <= '9')||LA36_68=='<'||LA36_68=='>'||(LA36_68 >= '@' && LA36_68 <= 'Q')||(LA36_68 >= 'S' && LA36_68 <= 'Z')||(LA36_68 >= '_' && LA36_68 <= 'q')||(LA36_68 >= 's' && LA36_68 <= 'z')||LA36_68=='|'||(LA36_68 >= '\u007F' && LA36_68 <= '\u2FFF')||(LA36_68 >= '\u3001' && LA36_68 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_68=='\\') ) {s = 31;}
						else if ( (LA36_68=='+') ) {s = 32;}
						else if ( (LA36_68=='-') ) {s = 33;}
						else if ( (LA36_68=='=') ) {s = 49;}
						else if ( (LA36_68=='#') ) {s = 34;}
						else if ( (LA36_68=='/') ) {s = 35;}
						else if ( (LA36_68=='*'||LA36_68=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 54 : 
						int LA36_77 = input.LA(1);
						s = -1;
						if ( ((LA36_77 >= '0' && LA36_77 <= '9')) ) {s = 85;}
						else if ( (LA36_77=='(') ) {s = 36;}
						else if ( ((LA36_77 >= '\u0000' && LA36_77 <= '\b')||(LA36_77 >= '\u000B' && LA36_77 <= '\f')||(LA36_77 >= '\u000E' && LA36_77 <= '\u001F')||(LA36_77 >= '$' && LA36_77 <= '\'')||LA36_77=='.'||LA36_77=='<'||LA36_77=='>'||(LA36_77 >= '@' && LA36_77 <= 'Z')||(LA36_77 >= '_' && LA36_77 <= 'z')||LA36_77=='|'||(LA36_77 >= '\u007F' && LA36_77 <= '\u2FFF')||(LA36_77 >= '\u3001' && LA36_77 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_77=='\\') ) {s = 31;}
						else if ( (LA36_77=='+') ) {s = 32;}
						else if ( (LA36_77=='-') ) {s = 33;}
						else if ( (LA36_77=='=') ) {s = 49;}
						else if ( (LA36_77=='#') ) {s = 34;}
						else if ( (LA36_77=='/') ) {s = 35;}
						else if ( (LA36_77=='*'||LA36_77=='?') ) {s = 37;}
						else s = 86;
						if ( s>=0 ) return s;
						break;

					case 55 : 
						int LA36_23 = input.LA(1);
						s = -1;
						if ( ((LA36_23 >= '\u0000' && LA36_23 <= '\b')||(LA36_23 >= '\u000B' && LA36_23 <= '\f')||(LA36_23 >= '\u000E' && LA36_23 <= '\u001F')||(LA36_23 >= '$' && LA36_23 <= '\'')||LA36_23=='.'||(LA36_23 >= '0' && LA36_23 <= '9')||LA36_23=='<'||LA36_23=='>'||(LA36_23 >= '@' && LA36_23 <= 'Z')||(LA36_23 >= '_' && LA36_23 <= 'z')||LA36_23=='|'||(LA36_23 >= '\u007F' && LA36_23 <= '\u2FFF')||(LA36_23 >= '\u3001' && LA36_23 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_23=='\\') ) {s = 31;}
						else if ( (LA36_23=='+') ) {s = 32;}
						else if ( (LA36_23=='-') ) {s = 33;}
						else if ( (LA36_23=='=') ) {s = 49;}
						else if ( (LA36_23=='#') ) {s = 34;}
						else if ( (LA36_23=='/') ) {s = 35;}
						else if ( (LA36_23=='(') ) {s = 36;}
						else if ( (LA36_23=='*'||LA36_23=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 56 : 
						int LA36_69 = input.LA(1);
						s = -1;
						if ( ((LA36_69 >= '0' && LA36_69 <= '9')) ) {s = 78;}
						else if ( (LA36_69=='(') ) {s = 36;}
						else if ( (LA36_69=='.') ) {s = 79;}
						else if ( (LA36_69=='\\') ) {s = 31;}
						else if ( (LA36_69=='+') ) {s = 32;}
						else if ( (LA36_69=='-') ) {s = 33;}
						else if ( (LA36_69=='=') ) {s = 49;}
						else if ( (LA36_69=='#') ) {s = 34;}
						else if ( (LA36_69=='/') ) {s = 35;}
						else if ( ((LA36_69 >= '\u0000' && LA36_69 <= '\b')||(LA36_69 >= '\u000B' && LA36_69 <= '\f')||(LA36_69 >= '\u000E' && LA36_69 <= '\u001F')||(LA36_69 >= '$' && LA36_69 <= '\'')||LA36_69=='<'||LA36_69=='>'||(LA36_69 >= '@' && LA36_69 <= 'Z')||(LA36_69 >= '_' && LA36_69 <= 'z')||LA36_69=='|'||(LA36_69 >= '\u007F' && LA36_69 <= '\u2FFF')||(LA36_69 >= '\u3001' && LA36_69 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_69=='*'||LA36_69=='?') ) {s = 37;}
						else s = 54;
						if ( s>=0 ) return s;
						break;

					case 57 : 
						int LA36_13 = input.LA(1);
						s = -1;
						if ( ((LA36_13 >= '\u0000' && LA36_13 <= '\u001F')||(LA36_13 >= '!' && LA36_13 <= '#')||(LA36_13 >= '%' && LA36_13 <= '*')||(LA36_13 >= '.' && LA36_13 <= '/')||(LA36_13 >= ':' && LA36_13 <= '\uFFFF')) ) {s = 43;}
						else s = 42;
						if ( s>=0 ) return s;
						break;

					case 58 : 
						int LA36_59 = input.LA(1);
						s = -1;
						if ( ((LA36_59 >= '\u0000' && LA36_59 <= '\b')||(LA36_59 >= '\u000B' && LA36_59 <= '\f')||(LA36_59 >= '\u000E' && LA36_59 <= '\u001F')||(LA36_59 >= '$' && LA36_59 <= '\'')||LA36_59=='.'||(LA36_59 >= '0' && LA36_59 <= '9')||LA36_59=='<'||LA36_59=='>'||(LA36_59 >= '@' && LA36_59 <= 'Z')||(LA36_59 >= '_' && LA36_59 <= 'z')||LA36_59=='|'||(LA36_59 >= '\u007F' && LA36_59 <= '\u2FFF')||(LA36_59 >= '\u3001' && LA36_59 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_59=='\\') ) {s = 31;}
						else if ( (LA36_59=='+') ) {s = 32;}
						else if ( (LA36_59=='-') ) {s = 33;}
						else if ( (LA36_59=='=') ) {s = 49;}
						else if ( (LA36_59=='#') ) {s = 34;}
						else if ( (LA36_59=='/') ) {s = 35;}
						else if ( (LA36_59=='(') ) {s = 36;}
						else if ( (LA36_59=='*'||LA36_59=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 59 : 
						int LA36_55 = input.LA(1);
						s = -1;
						if ( ((LA36_55 >= '0' && LA36_55 <= '9')) ) {s = 69;}
						else if ( (LA36_55=='(') ) {s = 36;}
						else if ( (LA36_55=='.') ) {s = 56;}
						else if ( (LA36_55=='\\') ) {s = 31;}
						else if ( (LA36_55=='+') ) {s = 32;}
						else if ( (LA36_55=='-') ) {s = 57;}
						else if ( (LA36_55=='=') ) {s = 49;}
						else if ( (LA36_55=='#') ) {s = 34;}
						else if ( (LA36_55=='/') ) {s = 58;}
						else if ( ((LA36_55 >= '\u0000' && LA36_55 <= '\b')||(LA36_55 >= '\u000B' && LA36_55 <= '\f')||(LA36_55 >= '\u000E' && LA36_55 <= '\u001F')||(LA36_55 >= '$' && LA36_55 <= '\'')||LA36_55=='<'||LA36_55=='>'||(LA36_55 >= '@' && LA36_55 <= 'Z')||(LA36_55 >= '_' && LA36_55 <= 'z')||LA36_55=='|'||(LA36_55 >= '\u007F' && LA36_55 <= '\u2FFF')||(LA36_55 >= '\u3001' && LA36_55 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_55==':') ) {s = 70;}
						else if ( (LA36_55=='*'||LA36_55=='?') ) {s = 37;}
						else s = 54;
						if ( s>=0 ) return s;
						break;

					case 60 : 
						int LA36_30 = input.LA(1);
						s = -1;
						if ( (LA36_30=='(') ) {s = 36;}
						else if ( ((LA36_30 >= '\u0000' && LA36_30 <= '\b')||(LA36_30 >= '\u000B' && LA36_30 <= '\f')||(LA36_30 >= '\u000E' && LA36_30 <= '\u001F')||(LA36_30 >= '$' && LA36_30 <= '\'')||LA36_30=='.'||(LA36_30 >= '0' && LA36_30 <= '9')||LA36_30=='<'||LA36_30=='>'||(LA36_30 >= '@' && LA36_30 <= 'Z')||(LA36_30 >= '_' && LA36_30 <= 'z')||LA36_30=='|'||(LA36_30 >= '\u007F' && LA36_30 <= '\u2FFF')||(LA36_30 >= '\u3001' && LA36_30 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_30=='\\') ) {s = 31;}
						else if ( (LA36_30=='+') ) {s = 32;}
						else if ( (LA36_30=='-') ) {s = 33;}
						else if ( (LA36_30=='=') ) {s = 49;}
						else if ( (LA36_30=='#') ) {s = 34;}
						else if ( (LA36_30=='/') ) {s = 35;}
						else if ( (LA36_30=='*'||LA36_30=='?') ) {s = 37;}
						else s = 29;
						if ( s>=0 ) return s;
						break;

					case 61 : 
						int LA36_80 = input.LA(1);
						s = -1;
						if ( (LA36_80=='(') ) {s = 36;}
						else if ( (LA36_80=='.') ) {s = 83;}
						else if ( (LA36_80=='\\') ) {s = 31;}
						else if ( (LA36_80=='+') ) {s = 32;}
						else if ( (LA36_80=='-') ) {s = 81;}
						else if ( (LA36_80=='=') ) {s = 49;}
						else if ( (LA36_80=='#') ) {s = 34;}
						else if ( (LA36_80=='/') ) {s = 82;}
						else if ( ((LA36_80 >= '0' && LA36_80 <= '9')) ) {s = 89;}
						else if ( ((LA36_80 >= '\u0000' && LA36_80 <= '\b')||(LA36_80 >= '\u000B' && LA36_80 <= '\f')||(LA36_80 >= '\u000E' && LA36_80 <= '\u001F')||(LA36_80 >= '$' && LA36_80 <= '\'')||LA36_80=='<'||LA36_80=='>'||(LA36_80 >= '@' && LA36_80 <= 'Z')||(LA36_80 >= '_' && LA36_80 <= 'z')||LA36_80=='|'||(LA36_80 >= '\u007F' && LA36_80 <= '\u2FFF')||(LA36_80 >= '\u3001' && LA36_80 <= '\uFFFF')) ) {s = 30;}
						else if ( (LA36_80=='*'||LA36_80=='?') ) {s = 37;}
						else s = 54;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 36, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
