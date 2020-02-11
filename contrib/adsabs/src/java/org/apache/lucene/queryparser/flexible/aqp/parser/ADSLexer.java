// $ANTLR 3.5.2 /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g 2020-02-10 19:29:05

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class ADSLexer extends Lexer {
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

	// $ANTLR start "T__70"
	public final void mT__70() throws RecognitionException {
		try {
			int _type = T__70;
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
	// $ANTLR end "T__70"

	// $ANTLR start "T__71"
	public final void mT__71() throws RecognitionException {
		try {
			int _type = T__71;
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
	// $ANTLR end "T__71"

	// $ANTLR start "T__72"
	public final void mT__72() throws RecognitionException {
		try {
			int _type = T__72;
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
	// $ANTLR end "T__72"

	// $ANTLR start "T__73"
	public final void mT__73() throws RecognitionException {
		try {
			int _type = T__73;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:14:7: ( 'arXiv:' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:14:9: 'arXiv:'
			{
			match("arXiv:"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__73"

	// $ANTLR start "T__74"
	public final void mT__74() throws RecognitionException {
		try {
			int _type = T__74;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:15:7: ( 'arxiv:' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:15:9: 'arxiv:'
			{
			match("arxiv:"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__74"

	// $ANTLR start "T__75"
	public final void mT__75() throws RecognitionException {
		try {
			int _type = T__75;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:16:7: ( 'doi:' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:16:9: 'doi:'
			{
			match("doi:"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__75"

	// $ANTLR start "LPAREN"
	public final void mLPAREN() throws RecognitionException {
		try {
			int _type = LPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:359:9: ( '(' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:359:11: '('
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:361:9: ( ')' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:361:11: ')'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:363:9: ( '[' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:363:11: '['
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:365:9: ( ']' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:365:11: ']'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:367:9: ( ':' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:367:11: ':'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:369:7: ( '+' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:369:9: '+'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:371:7: ( '-' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:371:9: '-'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:373:7: ( '*' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:373:9: '*'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:375:8: ( ( '?' )+ )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:375:10: ( '?' )+
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:375:10: ( '?' )+
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:375:10: '?'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:382:7: ( '^' ( NUMBER )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:382:9: '^' ( NUMBER )?
			{
			match('^'); 
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:382:13: ( NUMBER )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:382:13: NUMBER
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:384:7: ( '~' ( NUMBER )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:384:9: '~' ( NUMBER )?
			{
			match('~'); 
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:384:13: ( NUMBER )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:384:13: NUMBER
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:386:9: ( '\\\"' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:386:11: '\\\"'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:390:7: ( ',' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:390:9: ','
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:392:10: ( ';' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:392:13: ';'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:396:3: (~ ( '0' .. '9' | ' ' | COMMA | PLUS | MINUS | '$' ) )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:401:18: ( '\\\\' . )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:401:21: '\\\\' .
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:403:5: ( 'TO' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:403:7: 'TO'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:406:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:406:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:406:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:406:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:407:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:407:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:407:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:407:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:408:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:408:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:409:7: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) ( '0' .. '9' )* )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:409:9: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) ( '0' .. '9' )*
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:409:57: ( '0' .. '9' )*
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:415:3: ( '^' ( AS_CHAR )+ ( ',' ( ' ' | AS_CHAR )+ )* ( '$' )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:416:3: '^' ( AS_CHAR )+ ( ',' ( ' ' | AS_CHAR )+ )* ( '$' )?
			{
			match('^'); 
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:416:7: ( AS_CHAR )+
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

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:416:16: ( ',' ( ' ' | AS_CHAR )+ )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==',') ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:416:17: ',' ( ' ' | AS_CHAR )+
					{
					match(','); 
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:416:21: ( ' ' | AS_CHAR )+
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

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:416:40: ( '$' )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0=='$') ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:416:40: '$'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:450:3: ( '-' INT INT INT INT | INT INT INT INT '-' ( INT INT INT INT )? )
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:451:3: '-' INT INT INT INT
					{
					match('-'); 
					mINT(); 

					mINT(); 

					mINT(); 

					mINT(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:452:5: INT INT INT INT '-' ( INT INT INT INT )?
					{
					mINT(); 

					mINT(); 

					mINT(); 

					mINT(); 

					match('-'); 
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:452:25: ( INT INT INT INT )?
					int alt9=2;
					int LA9_0 = input.LA(1);
					if ( ((LA9_0 >= '0' && LA9_0 <= '9')) ) {
						alt9=1;
					}
					switch (alt9) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:452:26: INT INT INT INT
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:464:3: ( TERM_NORMAL '(' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:465:3: TERM_NORMAL '('
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:469:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:469:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:478:13: ( '0' .. '9' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:483:3: ( (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | ';' | '/' ) | ESC_CHAR ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:484:3: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | ';' | '/' ) | ESC_CHAR )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:484:3: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | ';' | '/' ) | ESC_CHAR )
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:484:4: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | ';' | '/' )
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:491:6: ESC_CHAR
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:495:3: ( ( TERM_START_CHAR | '+' | '-' | '=' | '#' | '/' ) )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:496:3: ( TERM_START_CHAR | '+' | '-' | '=' | '#' | '/' )
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:496:3: ( TERM_START_CHAR | '+' | '-' | '=' | '#' | '/' )
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:496:4: TERM_START_CHAR
					{
					mTERM_START_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:496:23: '+'
					{
					match('+'); 
					}
					break;
				case 3 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:496:29: '-'
					{
					match('-'); 
					}
					break;
				case 4 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:496:35: '='
					{
					match('='); 
					}
					break;
				case 5 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:496:41: '#'
					{
					match('#'); 
					}
					break;
				case 6 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:496:47: '/'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:502:3: ( INT ( INT )? ( '/' | MINUS | '.' ) INT ( INT )? ( '/' | MINUS | '.' ) INT INT ( INT INT )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:503:3: INT ( INT )? ( '/' | MINUS | '.' ) INT ( INT )? ( '/' | MINUS | '.' ) INT INT ( INT INT )?
			{
			mINT(); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:503:7: ( INT )?
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

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:503:32: ( INT )?
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

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:503:61: ( INT INT )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( ((LA15_0 >= '0' && LA15_0 <= '9')) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:503:62: INT INT
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:507:3: ( ( INT )+ ( '.' ( INT )+ )? )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:508:3: ( INT )+ ( '.' ( INT )+ )?
			{
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:508:3: ( INT )+
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

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:508:8: ( '.' ( INT )+ )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0=='.') ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:508:9: '.' ( INT )+
					{
					match('.'); 
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:508:13: ( INT )+
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:511:18: ( NUMBER 'm' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:512:3: NUMBER 'm'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:514:18: ( NUMBER 'h' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:515:3: NUMBER 'h'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:517:18: ( NUMBER 'd' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:518:3: NUMBER 'd'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:520:18: ( NUMBER 's' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:521:3: NUMBER 's'
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:524:3: ( INT INT COLON INT INT COLON NUMBER ( PLUS | MINUS ) INT INT COLON INT INT COLON NUMBER )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:525:3: INT INT COLON INT INT COLON NUMBER ( PLUS | MINUS ) INT INT COLON INT INT COLON NUMBER
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:529:3: ( TERM_START_CHAR ( TERM_CHAR )* )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:530:3: TERM_START_CHAR ( TERM_CHAR )*
			{
			mTERM_START_CHAR(); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:530:19: ( TERM_CHAR )*
			loop19:
			while (true) {
				int alt19=2;
				int LA19_0 = input.LA(1);
				if ( ((LA19_0 >= '\u0000' && LA19_0 <= '\b')||(LA19_0 >= '\u000B' && LA19_0 <= '\f')||(LA19_0 >= '\u000E' && LA19_0 <= '\u001F')||(LA19_0 >= '#' && LA19_0 <= '\'')||LA19_0=='+'||(LA19_0 >= '-' && LA19_0 <= '9')||(LA19_0 >= '<' && LA19_0 <= '>')||(LA19_0 >= '@' && LA19_0 <= 'Z')||LA19_0=='\\'||(LA19_0 >= '_' && LA19_0 <= 'z')||LA19_0=='|'||(LA19_0 >= '\u007F' && LA19_0 <= '\u2FFF')||(LA19_0 >= '\u3001' && LA19_0 <= '\uFFFF')) ) {
					alt19=1;
				}

				switch (alt19) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:530:21: TERM_CHAR
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:534:15: ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ )
			int alt31=3;
			alt31 = dfa31.predict(input);
			switch (alt31) {
				case 1 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:3: ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )*
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:3: ( STAR | QMARK )
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
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:4: STAR
							{
							mSTAR(); 

							}
							break;
						case 2 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:9: QMARK
							{
							mQMARK(); 

							}
							break;

					}

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:16: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+
					int cnt23=0;
					loop23:
					while (true) {
						int alt23=2;
						alt23 = dfa23.predict(input);
						switch (alt23) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:17: ( TERM_CHAR )+ ( QMARK | STAR )
							{
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:17: ( TERM_CHAR )+
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
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:17: TERM_CHAR
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

							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:28: ( QMARK | STAR )
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
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:29: QMARK
									{
									mQMARK(); 

									}
									break;
								case 2 :
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:35: STAR
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

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:43: ( TERM_CHAR )*
					loop24:
					while (true) {
						int alt24=2;
						int LA24_0 = input.LA(1);
						if ( ((LA24_0 >= '\u0000' && LA24_0 <= '\b')||(LA24_0 >= '\u000B' && LA24_0 <= '\f')||(LA24_0 >= '\u000E' && LA24_0 <= '\u001F')||(LA24_0 >= '#' && LA24_0 <= '\'')||LA24_0=='+'||(LA24_0 >= '-' && LA24_0 <= '9')||(LA24_0 >= '<' && LA24_0 <= '>')||(LA24_0 >= '@' && LA24_0 <= 'Z')||LA24_0=='\\'||(LA24_0 >= '_' && LA24_0 <= 'z')||LA24_0=='|'||(LA24_0 >= '\u007F' && LA24_0 <= '\u2FFF')||(LA24_0 >= '\u3001' && LA24_0 <= '\uFFFF')) ) {
							alt24=1;
						}

						switch (alt24) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:535:44: TERM_CHAR
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:5: TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )*
					{
					mTERM_START_CHAR(); 

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:21: ( ( TERM_CHAR )* ( QMARK | STAR ) )+
					int cnt27=0;
					loop27:
					while (true) {
						int alt27=2;
						alt27 = dfa27.predict(input);
						switch (alt27) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:22: ( TERM_CHAR )* ( QMARK | STAR )
							{
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:22: ( TERM_CHAR )*
							loop25:
							while (true) {
								int alt25=2;
								int LA25_0 = input.LA(1);
								if ( ((LA25_0 >= '\u0000' && LA25_0 <= '\b')||(LA25_0 >= '\u000B' && LA25_0 <= '\f')||(LA25_0 >= '\u000E' && LA25_0 <= '\u001F')||(LA25_0 >= '#' && LA25_0 <= '\'')||LA25_0=='+'||(LA25_0 >= '-' && LA25_0 <= '9')||(LA25_0 >= '<' && LA25_0 <= '>')||(LA25_0 >= '@' && LA25_0 <= 'Z')||LA25_0=='\\'||(LA25_0 >= '_' && LA25_0 <= 'z')||LA25_0=='|'||(LA25_0 >= '\u007F' && LA25_0 <= '\u2FFF')||(LA25_0 >= '\u3001' && LA25_0 <= '\uFFFF')) ) {
									alt25=1;
								}

								switch (alt25) {
								case 1 :
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:22: TERM_CHAR
									{
									mTERM_CHAR(); 

									}
									break;

								default :
									break loop25;
								}
							}

							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:33: ( QMARK | STAR )
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
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:34: QMARK
									{
									mQMARK(); 

									}
									break;
								case 2 :
									// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:40: STAR
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

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:48: ( TERM_CHAR )*
					loop28:
					while (true) {
						int alt28=2;
						int LA28_0 = input.LA(1);
						if ( ((LA28_0 >= '\u0000' && LA28_0 <= '\b')||(LA28_0 >= '\u000B' && LA28_0 <= '\f')||(LA28_0 >= '\u000E' && LA28_0 <= '\u001F')||(LA28_0 >= '#' && LA28_0 <= '\'')||LA28_0=='+'||(LA28_0 >= '-' && LA28_0 <= '9')||(LA28_0 >= '<' && LA28_0 <= '>')||(LA28_0 >= '@' && LA28_0 <= 'Z')||LA28_0=='\\'||(LA28_0 >= '_' && LA28_0 <= 'z')||LA28_0=='|'||(LA28_0 >= '\u007F' && LA28_0 <= '\u2FFF')||(LA28_0 >= '\u3001' && LA28_0 <= '\uFFFF')) ) {
							alt28=1;
						}

						switch (alt28) {
						case 1 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:536:49: TERM_CHAR
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:537:5: ( STAR | QMARK ) ( TERM_CHAR )+
					{
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:537:5: ( STAR | QMARK )
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
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:537:6: STAR
							{
							mSTAR(); 

							}
							break;
						case 2 :
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:537:11: QMARK
							{
							mQMARK(); 

							}
							break;

					}

					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:537:18: ( TERM_CHAR )+
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
							// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:537:18: TERM_CHAR
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:542:3: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:543:3: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE
			{
			mDQUOTE(); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:543:10: ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:543:11: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:543:20: ~ ( '\\\"' | '\\\\' | '?' | '*' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:546:17: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:547:3: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE
			{
			mDQUOTE(); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:547:10: ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:547:11: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:547:20: ~ ( '\\\"' | '\\\\' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:550:15: ( '{!' ( ESC_CHAR |~ ( '}' | '\\\\' ) )+ '}' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:551:3: '{!' ( ESC_CHAR |~ ( '}' | '\\\\' ) )+ '}'
			{
			match("{!"); 

			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:551:8: ( ESC_CHAR |~ ( '}' | '\\\\' ) )+
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:551:9: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:551:18: ~ ( '}' | '\\\\' )
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
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:553:7: ( '/' ( ESC_CHAR |~ ( '/' | '\\\\' ) )+ '/' )
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:554:3: '/' ( ESC_CHAR |~ ( '/' | '\\\\' ) )+ '/'
			{
			match('/'); 
			// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:554:7: ( ESC_CHAR |~ ( '/' | '\\\\' ) )+
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
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:554:8: ESC_CHAR
					{
					mESC_CHAR(); 

					}
					break;
				case 2 :
					// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:554:17: ~ ( '/' | '\\\\' )
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
		// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:8: ( T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | CARAT | TILDE | DQUOTE | COMMA | SEMICOLON | TO | AND | OR | NOT | NEAR | AUTHOR_SEARCH | DATE_RANGE | FUNC_NAME | WS | DATE_TOKEN | NUMBER | HOUR | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING | LOCAL_PARAMS | REGEX )
		int alt36=38;
		alt36 = dfa36.predict(input);
		switch (alt36) {
			case 1 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:10: T__70
				{
				mT__70(); 

				}
				break;
			case 2 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:16: T__71
				{
				mT__71(); 

				}
				break;
			case 3 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:22: T__72
				{
				mT__72(); 

				}
				break;
			case 4 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:28: T__73
				{
				mT__73(); 

				}
				break;
			case 5 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:34: T__74
				{
				mT__74(); 

				}
				break;
			case 6 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:40: T__75
				{
				mT__75(); 

				}
				break;
			case 7 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:46: LPAREN
				{
				mLPAREN(); 

				}
				break;
			case 8 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:53: RPAREN
				{
				mRPAREN(); 

				}
				break;
			case 9 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:60: LBRACK
				{
				mLBRACK(); 

				}
				break;
			case 10 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:67: RBRACK
				{
				mRBRACK(); 

				}
				break;
			case 11 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:74: COLON
				{
				mCOLON(); 

				}
				break;
			case 12 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:80: PLUS
				{
				mPLUS(); 

				}
				break;
			case 13 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:85: MINUS
				{
				mMINUS(); 

				}
				break;
			case 14 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:91: STAR
				{
				mSTAR(); 

				}
				break;
			case 15 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:96: QMARK
				{
				mQMARK(); 

				}
				break;
			case 16 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:102: CARAT
				{
				mCARAT(); 

				}
				break;
			case 17 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:108: TILDE
				{
				mTILDE(); 

				}
				break;
			case 18 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:114: DQUOTE
				{
				mDQUOTE(); 

				}
				break;
			case 19 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:121: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 20 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:127: SEMICOLON
				{
				mSEMICOLON(); 

				}
				break;
			case 21 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:137: TO
				{
				mTO(); 

				}
				break;
			case 22 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:140: AND
				{
				mAND(); 

				}
				break;
			case 23 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:144: OR
				{
				mOR(); 

				}
				break;
			case 24 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:147: NOT
				{
				mNOT(); 

				}
				break;
			case 25 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:151: NEAR
				{
				mNEAR(); 

				}
				break;
			case 26 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:156: AUTHOR_SEARCH
				{
				mAUTHOR_SEARCH(); 

				}
				break;
			case 27 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:170: DATE_RANGE
				{
				mDATE_RANGE(); 

				}
				break;
			case 28 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:181: FUNC_NAME
				{
				mFUNC_NAME(); 

				}
				break;
			case 29 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:191: WS
				{
				mWS(); 

				}
				break;
			case 30 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:194: DATE_TOKEN
				{
				mDATE_TOKEN(); 

				}
				break;
			case 31 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:205: NUMBER
				{
				mNUMBER(); 

				}
				break;
			case 32 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:212: HOUR
				{
				mHOUR(); 

				}
				break;
			case 33 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:217: TERM_NORMAL
				{
				mTERM_NORMAL(); 

				}
				break;
			case 34 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:229: TERM_TRUNCATED
				{
				mTERM_TRUNCATED(); 

				}
				break;
			case 35 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:244: PHRASE
				{
				mPHRASE(); 

				}
				break;
			case 36 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:251: PHRASE_ANYTHING
				{
				mPHRASE_ANYTHING(); 

				}
				break;
			case 37 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:267: LOCAL_PARAMS
				{
				mLOCAL_PARAMS(); 

				}
				break;
			case 38 :
				// /dvt/workspace/montysolr6/contrib/adsabs/grammars/ADS.g:1:280: REGEX
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
		"\1\7\1\3\1\1\1\uffff\1\5\1\0\1\10\1\11\1\12\1\2\1\4\2\uffff\1\6}>";
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
			return "534:1: TERM_TRUNCATED : ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA31_5 = input.LA(1);
						s = -1;
						if ( ((LA31_5 >= '\u0000' && LA31_5 <= '\uFFFF')) ) {s = 13;}
						if ( s>=0 ) return s;
						break;

					case 1 : 
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

					case 2 : 
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

					case 3 : 
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

					case 4 : 
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

					case 5 : 
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

					case 6 : 
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

					case 7 : 
						int LA31_0 = input.LA(1);
						s = -1;
						if ( (LA31_0=='*') ) {s = 1;}
						else if ( (LA31_0=='?') ) {s = 2;}
						else if ( ((LA31_0 >= '\u0000' && LA31_0 <= '\b')||(LA31_0 >= '\u000B' && LA31_0 <= '\f')||(LA31_0 >= '\u000E' && LA31_0 <= '\u001F')||(LA31_0 >= '$' && LA31_0 <= '\'')||LA31_0=='.'||(LA31_0 >= '0' && LA31_0 <= '9')||LA31_0=='<'||LA31_0=='>'||(LA31_0 >= '@' && LA31_0 <= 'Z')||LA31_0=='\\'||(LA31_0 >= '_' && LA31_0 <= 'z')||LA31_0=='|'||(LA31_0 >= '\u007F' && LA31_0 <= '\u2FFF')||(LA31_0 >= '\u3001' && LA31_0 <= '\uFFFF')) ) {s = 3;}
						if ( s>=0 ) return s;
						break;

					case 8 : 
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

					case 9 : 
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

					case 10 : 
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
		"\1\6\1\2\1\7\1\0\1\4\1\5\1\10\1\1\2\uffff\1\3}>";
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
			return "()+ loopback of 535:16: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
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

					case 1 : 
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

					case 2 : 
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

					case 3 : 
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

					case 4 : 
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

					case 5 : 
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

					case 6 : 
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

					case 7 : 
						int LA23_2 = input.LA(1);
						s = -1;
						if ( ((LA23_2 >= '\u0000' && LA23_2 <= '\uFFFF')) ) {s = 10;}
						if ( s>=0 ) return s;
						break;

					case 8 : 
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
		"\1\5\1\2\1\10\1\0\1\4\1\6\1\7\1\1\2\uffff\1\3}>";
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
			return "()+ loopback of 536:21: ( ( TERM_CHAR )* ( QMARK | STAR ) )+";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
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

					case 1 : 
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

					case 2 : 
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

					case 3 : 
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

					case 4 : 
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

					case 5 : 
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

					case 6 : 
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

					case 7 : 
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

					case 8 : 
						int LA27_2 = input.LA(1);
						s = -1;
						if ( ((LA27_2 >= '\u0000' && LA27_2 <= '\uFFFF')) ) {s = 10;}
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
		"\2\uffff\1\37\1\uffff\2\37\6\uffff\1\54\1\56\1\57\1\60\1\uffff\1\62\2"+
		"\uffff\4\37\1\72\1\37\4\uffff\1\37\1\uffff\1\37\1\uffff\4\37\2\uffff\4"+
		"\37\12\uffff\1\110\1\111\2\37\1\uffff\1\72\4\37\1\120\3\37\1\123\1\37"+
		"\4\uffff\1\126\1\37\1\72\1\uffff\1\72\1\37\1\uffff\2\37\4\uffff\1\142"+
		"\1\72\1\37\1\72\6\37\1\142\1\uffff\1\55\2\72\1\37\2\uffff\1\37\1\154\1"+
		"\37\1\uffff\2\37\1\154\1\55";
	static final String DFA36_eofS =
		"\161\uffff";
	static final String DFA36_minS =
		"\1\0\1\uffff\1\0\1\uffff\2\0\6\uffff\1\60\3\0\1\uffff\1\0\2\uffff\7\0"+
		"\3\uffff\1\0\1\uffff\6\0\2\uffff\4\0\7\uffff\2\0\1\uffff\4\0\1\uffff\14"+
		"\0\3\uffff\3\0\1\uffff\2\0\1\uffff\2\0\4\uffff\13\0\1\uffff\4\0\2\uffff"+
		"\3\0\1\uffff\4\0";
	static final String DFA36_maxS =
		"\1\uffff\1\uffff\1\uffff\1\uffff\2\uffff\6\uffff\1\71\3\uffff\1\uffff"+
		"\1\uffff\2\uffff\7\uffff\3\uffff\1\uffff\1\uffff\6\uffff\2\uffff\4\uffff"+
		"\7\uffff\2\uffff\1\uffff\4\uffff\1\uffff\14\uffff\3\uffff\3\uffff\1\uffff"+
		"\2\uffff\1\uffff\2\uffff\4\uffff\13\uffff\1\uffff\4\uffff\2\uffff\3\uffff"+
		"\1\uffff\4\uffff";
	static final String DFA36_acceptS =
		"\1\uffff\1\1\1\uffff\1\3\2\uffff\1\7\1\10\1\11\1\12\1\13\1\14\4\uffff"+
		"\1\21\1\uffff\1\23\1\24\7\uffff\1\35\1\45\1\46\1\uffff\1\41\6\uffff\1"+
		"\34\1\42\4\uffff\1\15\1\33\1\16\1\17\1\20\1\32\1\22\2\uffff\1\44\4\uffff"+
		"\1\37\14\uffff\1\43\1\25\1\27\3\uffff\1\40\2\uffff\1\2\2\uffff\1\26\1"+
		"\6\1\43\1\30\13\uffff\1\31\4\uffff\1\4\1\5\3\uffff\1\36\4\uffff";
	static final String DFA36_specialS =
		"\1\76\1\uffff\1\61\1\uffff\1\2\1\70\7\uffff\1\36\1\101\1\57\1\uffff\1"+
		"\66\2\uffff\1\5\1\6\1\12\1\10\1\45\1\26\1\46\3\uffff\1\56\1\uffff\1\44"+
		"\1\47\1\13\1\17\1\23\1\27\2\uffff\1\32\1\30\1\20\1\105\7\uffff\1\63\1"+
		"\73\1\uffff\1\4\1\21\1\34\1\41\1\uffff\1\1\1\60\1\62\1\65\1\72\1\3\1\110"+
		"\1\74\1\102\1\11\1\107\1\15\3\uffff\1\31\1\42\1\7\1\uffff\1\67\1\50\1"+
		"\uffff\1\77\1\103\4\uffff\1\35\1\14\1\16\1\52\1\64\1\71\1\106\1\43\1\100"+
		"\1\104\1\40\1\uffff\1\54\1\33\1\53\1\51\2\uffff\1\22\1\0\1\24\1\uffff"+
		"\1\55\1\25\1\37\1\75}>";
	static final String[] DFA36_transitionS = {
			"\11\31\2\33\2\31\1\33\22\31\1\33\1\uffff\1\21\1\1\4\31\1\6\1\7\1\15\1"+
			"\13\1\22\1\14\1\31\1\35\12\30\1\12\1\23\1\2\1\3\1\31\1\16\1\31\1\25\14"+
			"\31\1\27\1\26\4\31\1\24\6\31\1\10\1\32\1\11\1\17\2\31\1\4\2\31\1\5\11"+
			"\31\1\27\1\26\13\31\1\34\1\31\1\uffff\1\20\u2f81\31\1\33\ucfff\31",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\36\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\16\40"+
			"\1\51\14\40\1\uffff\1\41\2\uffff\17\40\1\51\3\40\1\50\10\40\1\uffff\1"+
			"\40\2\uffff\u2f81\40\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\20\40\1\53\13\40\1\uffff\1\40\2\uffff\u2f81\40"+
			"\1\uffff\ucfff\40",
			"",
			"",
			"",
			"",
			"",
			"",
			"\12\55",
			"\11\47\2\uffff\2\47\1\uffff\22\47\3\uffff\5\47\3\uffff\1\47\1\uffff"+
			"\15\47\2\uffff\3\47\1\uffff\33\47\1\uffff\1\47\2\uffff\34\47\1\uffff"+
			"\1\47\2\uffff\u2f81\47\1\uffff\ucfff\47",
			"\11\47\2\uffff\2\47\1\uffff\22\47\3\uffff\5\47\3\uffff\1\47\1\uffff"+
			"\15\47\2\uffff\3\47\1\16\33\47\1\uffff\1\47\2\uffff\34\47\1\uffff\1\47"+
			"\2\uffff\u2f81\47\1\uffff\ucfff\47",
			"\40\61\1\uffff\3\61\1\uffff\6\61\3\uffff\2\61\12\uffff\uffc6\61",
			"",
			"\42\64\1\uffff\7\64\1\65\24\64\1\65\34\64\1\63\uffa3\64",
			"",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\17\40"+
			"\1\66\13\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40"+
			"\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\16\40"+
			"\1\51\14\40\1\uffff\1\41\2\uffff\17\40\1\51\14\40\1\uffff\1\40\2\uffff"+
			"\u2f81\40\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\22\40"+
			"\1\67\10\40\1\uffff\1\41\2\uffff\23\40\1\67\10\40\1\uffff\1\40\2\uffff"+
			"\u2f81\40\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\5\40\1"+
			"\71\11\40\1\70\13\40\1\uffff\1\41\2\uffff\6\40\1\71\11\40\1\70\13\40"+
			"\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\75\1\74\1\76\12\73\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\0\77",
			"",
			"",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\100\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\0\101",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\30\40"+
			"\1\102\2\40\1\uffff\1\41\2\uffff\31\40\1\103\2\40\1\uffff\1\40\2\uffff"+
			"\u2f81\40\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\4\40\1"+
			"\104\26\40\1\uffff\1\41\2\uffff\5\40\1\104\26\40\1\uffff\1\40\2\uffff"+
			"\u2f81\40\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\12\40\1\105\21\40\1\uffff\1\40\2\uffff\u2f81\40"+
			"\1\uffff\ucfff\40",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\0\106",
			"\42\64\1\107\7\64\1\65\24\64\1\65\34\64\1\63\uffa3\64",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\24\40"+
			"\1\112\6\40\1\uffff\1\41\2\uffff\25\40\1\112\6\40\1\uffff\1\40\2\uffff"+
			"\u2f81\40\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\1\40\1"+
			"\113\31\40\1\uffff\1\41\2\uffff\2\40\1\113\31\40\1\uffff\1\40\2\uffff"+
			"\u2f81\40\1\uffff\ucfff\40",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\75\1\74\1\76\12\114\1\115\1\uffff\1\40\1\52\1\40\1\47"+
			"\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff"+
			"\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\116\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\117\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\117\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\12\40\1\121\21\40\1\uffff\1\40\2\uffff\u2f81\40"+
			"\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\12\40\1\122\21\40\1\uffff\1\40\2\uffff\u2f81\40"+
			"\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\1\124\1\uffff\1\40\1\52\1\40\1\47"+
			"\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff"+
			"\ucfff\40",
			"\42\64\1\107\7\64\1\65\24\64\1\65\34\64\1\63\uffa3\64",
			"",
			"",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\22\40"+
			"\1\127\10\40\1\uffff\1\41\2\uffff\23\40\1\127\10\40\1\uffff\1\40\2\uffff"+
			"\u2f81\40\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\131\1\45\12\130\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\133\1\135\1\134\12\132\2\uffff\1\40\1\52\1\40\1\47\33"+
			"\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff"+
			"\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\133\1\135\1\134\12\136\2\uffff\1\40\1\52\1\40\1\47\33"+
			"\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff"+
			"\ucfff\40",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\27\40\1\137\4\40\1\uffff\1\40\2\uffff\u2f81\40"+
			"\1\uffff\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\27\40\1\140\4\40\1\uffff\1\40\2\uffff\u2f81\40"+
			"\1\uffff\ucfff\40",
			"",
			"",
			"",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\141\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\143\1\131\1\45\12\144\2\uffff\1\40\1\52\1\40\1\47\33"+
			"\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff"+
			"\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\145\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\133\1\135\1\134\12\145\2\uffff\1\40\1\52\1\40\1\47\33"+
			"\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff"+
			"\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\146\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\146\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\146\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\133\1\135\1\134\12\40\2\uffff\1\40\1\52\1\40\1\47\33"+
			"\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff"+
			"\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\1\147\1\uffff\1\40\1\52\1\40\1\47"+
			"\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff"+
			"\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\1\150\1\uffff\1\40\1\52\1\40\1\47"+
			"\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff"+
			"\ucfff\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\141\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\151\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\131\1\45\12\144\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\145\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\152\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\153\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\155\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\156\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\157\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\160\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40",
			"\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\44\4\40\1\46\1\uffff\1\47"+
			"\1\42\1\uffff\1\43\1\40\1\45\12\40\2\uffff\1\40\1\52\1\40\1\47\33\40"+
			"\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff\u2f81\40\1\uffff\ucfff"+
			"\40"
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
			return "1:1: Tokens : ( T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | CARAT | TILDE | DQUOTE | COMMA | SEMICOLON | TO | AND | OR | NOT | NEAR | AUTHOR_SEARCH | DATE_RANGE | FUNC_NAME | WS | DATE_TOKEN | NUMBER | HOUR | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING | LOCAL_PARAMS | REGEX );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA36_106 = input.LA(1);
						s = -1;
						if ( (LA36_106=='(') ) {s = 38;}
						else if ( ((LA36_106 >= '0' && LA36_106 <= '9')) ) {s = 109;}
						else if ( (LA36_106=='\\') ) {s = 33;}
						else if ( (LA36_106=='+') ) {s = 34;}
						else if ( (LA36_106=='-') ) {s = 35;}
						else if ( (LA36_106=='=') ) {s = 42;}
						else if ( (LA36_106=='#') ) {s = 36;}
						else if ( (LA36_106=='/') ) {s = 37;}
						else if ( ((LA36_106 >= '\u0000' && LA36_106 <= '\b')||(LA36_106 >= '\u000B' && LA36_106 <= '\f')||(LA36_106 >= '\u000E' && LA36_106 <= '\u001F')||(LA36_106 >= '$' && LA36_106 <= '\'')||LA36_106=='.'||LA36_106=='<'||LA36_106=='>'||(LA36_106 >= '@' && LA36_106 <= 'Z')||(LA36_106 >= '_' && LA36_106 <= 'z')||LA36_106=='|'||(LA36_106 >= '\u007F' && LA36_106 <= '\u2FFF')||(LA36_106 >= '\u3001' && LA36_106 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_106=='*'||LA36_106=='?') ) {s = 39;}
						else s = 108;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA36_59 = input.LA(1);
						s = -1;
						if ( ((LA36_59 >= '0' && LA36_59 <= '9')) ) {s = 76;}
						else if ( (LA36_59=='(') ) {s = 38;}
						else if ( (LA36_59=='.') ) {s = 60;}
						else if ( (LA36_59=='\\') ) {s = 33;}
						else if ( (LA36_59=='+') ) {s = 34;}
						else if ( (LA36_59=='-') ) {s = 61;}
						else if ( (LA36_59=='=') ) {s = 42;}
						else if ( (LA36_59=='#') ) {s = 36;}
						else if ( (LA36_59=='/') ) {s = 62;}
						else if ( ((LA36_59 >= '\u0000' && LA36_59 <= '\b')||(LA36_59 >= '\u000B' && LA36_59 <= '\f')||(LA36_59 >= '\u000E' && LA36_59 <= '\u001F')||(LA36_59 >= '$' && LA36_59 <= '\'')||LA36_59=='<'||LA36_59=='>'||(LA36_59 >= '@' && LA36_59 <= 'Z')||(LA36_59 >= '_' && LA36_59 <= 'z')||LA36_59=='|'||(LA36_59 >= '\u007F' && LA36_59 <= '\u2FFF')||(LA36_59 >= '\u3001' && LA36_59 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_59==':') ) {s = 77;}
						else if ( (LA36_59=='*'||LA36_59=='?') ) {s = 39;}
						else s = 58;
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA36_4 = input.LA(1);
						s = -1;
						if ( (LA36_4=='r') ) {s = 40;}
						else if ( (LA36_4=='N'||LA36_4=='n') ) {s = 41;}
						else if ( ((LA36_4 >= '\u0000' && LA36_4 <= '\b')||(LA36_4 >= '\u000B' && LA36_4 <= '\f')||(LA36_4 >= '\u000E' && LA36_4 <= '\u001F')||(LA36_4 >= '$' && LA36_4 <= '\'')||LA36_4=='.'||(LA36_4 >= '0' && LA36_4 <= '9')||LA36_4=='<'||LA36_4=='>'||(LA36_4 >= '@' && LA36_4 <= 'M')||(LA36_4 >= 'O' && LA36_4 <= 'Z')||(LA36_4 >= '_' && LA36_4 <= 'm')||(LA36_4 >= 'o' && LA36_4 <= 'q')||(LA36_4 >= 's' && LA36_4 <= 'z')||LA36_4=='|'||(LA36_4 >= '\u007F' && LA36_4 <= '\u2FFF')||(LA36_4 >= '\u3001' && LA36_4 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_4=='\\') ) {s = 33;}
						else if ( (LA36_4=='+') ) {s = 34;}
						else if ( (LA36_4=='-') ) {s = 35;}
						else if ( (LA36_4=='=') ) {s = 42;}
						else if ( (LA36_4=='#') ) {s = 36;}
						else if ( (LA36_4=='/') ) {s = 37;}
						else if ( (LA36_4=='(') ) {s = 38;}
						else if ( (LA36_4=='*'||LA36_4=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA36_64 = input.LA(1);
						s = -1;
						if ( (LA36_64=='(') ) {s = 38;}
						else if ( ((LA36_64 >= '\u0000' && LA36_64 <= '\b')||(LA36_64 >= '\u000B' && LA36_64 <= '\f')||(LA36_64 >= '\u000E' && LA36_64 <= '\u001F')||(LA36_64 >= '$' && LA36_64 <= '\'')||LA36_64=='.'||(LA36_64 >= '0' && LA36_64 <= '9')||LA36_64=='<'||LA36_64=='>'||(LA36_64 >= '@' && LA36_64 <= 'Z')||(LA36_64 >= '_' && LA36_64 <= 'z')||LA36_64=='|'||(LA36_64 >= '\u007F' && LA36_64 <= '\u2FFF')||(LA36_64 >= '\u3001' && LA36_64 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_64=='\\') ) {s = 33;}
						else if ( (LA36_64=='+') ) {s = 34;}
						else if ( (LA36_64=='-') ) {s = 35;}
						else if ( (LA36_64=='=') ) {s = 42;}
						else if ( (LA36_64=='#') ) {s = 36;}
						else if ( (LA36_64=='/') ) {s = 37;}
						else if ( (LA36_64=='*'||LA36_64=='?') ) {s = 39;}
						else s = 80;
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA36_54 = input.LA(1);
						s = -1;
						if ( (LA36_54=='(') ) {s = 38;}
						else if ( ((LA36_54 >= '\u0000' && LA36_54 <= '\b')||(LA36_54 >= '\u000B' && LA36_54 <= '\f')||(LA36_54 >= '\u000E' && LA36_54 <= '\u001F')||(LA36_54 >= '$' && LA36_54 <= '\'')||LA36_54=='.'||(LA36_54 >= '0' && LA36_54 <= '9')||LA36_54=='<'||LA36_54=='>'||(LA36_54 >= '@' && LA36_54 <= 'Z')||(LA36_54 >= '_' && LA36_54 <= 'z')||LA36_54=='|'||(LA36_54 >= '\u007F' && LA36_54 <= '\u2FFF')||(LA36_54 >= '\u3001' && LA36_54 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_54=='\\') ) {s = 33;}
						else if ( (LA36_54=='+') ) {s = 34;}
						else if ( (LA36_54=='-') ) {s = 35;}
						else if ( (LA36_54=='=') ) {s = 42;}
						else if ( (LA36_54=='#') ) {s = 36;}
						else if ( (LA36_54=='/') ) {s = 37;}
						else if ( (LA36_54=='*'||LA36_54=='?') ) {s = 39;}
						else s = 72;
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA36_20 = input.LA(1);
						s = -1;
						if ( (LA36_20=='O') ) {s = 54;}
						else if ( ((LA36_20 >= '\u0000' && LA36_20 <= '\b')||(LA36_20 >= '\u000B' && LA36_20 <= '\f')||(LA36_20 >= '\u000E' && LA36_20 <= '\u001F')||(LA36_20 >= '$' && LA36_20 <= '\'')||LA36_20=='.'||(LA36_20 >= '0' && LA36_20 <= '9')||LA36_20=='<'||LA36_20=='>'||(LA36_20 >= '@' && LA36_20 <= 'N')||(LA36_20 >= 'P' && LA36_20 <= 'Z')||(LA36_20 >= '_' && LA36_20 <= 'z')||LA36_20=='|'||(LA36_20 >= '\u007F' && LA36_20 <= '\u2FFF')||(LA36_20 >= '\u3001' && LA36_20 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_20=='\\') ) {s = 33;}
						else if ( (LA36_20=='+') ) {s = 34;}
						else if ( (LA36_20=='-') ) {s = 35;}
						else if ( (LA36_20=='=') ) {s = 42;}
						else if ( (LA36_20=='#') ) {s = 36;}
						else if ( (LA36_20=='/') ) {s = 37;}
						else if ( (LA36_20=='(') ) {s = 38;}
						else if ( (LA36_20=='*'||LA36_20=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 6 : 
						int LA36_21 = input.LA(1);
						s = -1;
						if ( (LA36_21=='N'||LA36_21=='n') ) {s = 41;}
						else if ( ((LA36_21 >= '\u0000' && LA36_21 <= '\b')||(LA36_21 >= '\u000B' && LA36_21 <= '\f')||(LA36_21 >= '\u000E' && LA36_21 <= '\u001F')||(LA36_21 >= '$' && LA36_21 <= '\'')||LA36_21=='.'||(LA36_21 >= '0' && LA36_21 <= '9')||LA36_21=='<'||LA36_21=='>'||(LA36_21 >= '@' && LA36_21 <= 'M')||(LA36_21 >= 'O' && LA36_21 <= 'Z')||(LA36_21 >= '_' && LA36_21 <= 'm')||(LA36_21 >= 'o' && LA36_21 <= 'z')||LA36_21=='|'||(LA36_21 >= '\u007F' && LA36_21 <= '\u2FFF')||(LA36_21 >= '\u3001' && LA36_21 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_21=='\\') ) {s = 33;}
						else if ( (LA36_21=='+') ) {s = 34;}
						else if ( (LA36_21=='-') ) {s = 35;}
						else if ( (LA36_21=='=') ) {s = 42;}
						else if ( (LA36_21=='#') ) {s = 36;}
						else if ( (LA36_21=='/') ) {s = 37;}
						else if ( (LA36_21=='(') ) {s = 38;}
						else if ( (LA36_21=='*'||LA36_21=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 7 : 
						int LA36_76 = input.LA(1);
						s = -1;
						if ( ((LA36_76 >= '0' && LA36_76 <= '9')) ) {s = 88;}
						else if ( (LA36_76=='(') ) {s = 38;}
						else if ( (LA36_76=='.') ) {s = 89;}
						else if ( (LA36_76=='\\') ) {s = 33;}
						else if ( (LA36_76=='+') ) {s = 34;}
						else if ( (LA36_76=='-') ) {s = 35;}
						else if ( (LA36_76=='=') ) {s = 42;}
						else if ( (LA36_76=='#') ) {s = 36;}
						else if ( (LA36_76=='/') ) {s = 37;}
						else if ( ((LA36_76 >= '\u0000' && LA36_76 <= '\b')||(LA36_76 >= '\u000B' && LA36_76 <= '\f')||(LA36_76 >= '\u000E' && LA36_76 <= '\u001F')||(LA36_76 >= '$' && LA36_76 <= '\'')||LA36_76=='<'||LA36_76=='>'||(LA36_76 >= '@' && LA36_76 <= 'Z')||(LA36_76 >= '_' && LA36_76 <= 'z')||LA36_76=='|'||(LA36_76 >= '\u007F' && LA36_76 <= '\u2FFF')||(LA36_76 >= '\u3001' && LA36_76 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_76=='*'||LA36_76=='?') ) {s = 39;}
						else s = 58;
						if ( s>=0 ) return s;
						break;

					case 8 : 
						int LA36_23 = input.LA(1);
						s = -1;
						if ( (LA36_23=='O'||LA36_23=='o') ) {s = 56;}
						else if ( (LA36_23=='E'||LA36_23=='e') ) {s = 57;}
						else if ( ((LA36_23 >= '\u0000' && LA36_23 <= '\b')||(LA36_23 >= '\u000B' && LA36_23 <= '\f')||(LA36_23 >= '\u000E' && LA36_23 <= '\u001F')||(LA36_23 >= '$' && LA36_23 <= '\'')||LA36_23=='.'||(LA36_23 >= '0' && LA36_23 <= '9')||LA36_23=='<'||LA36_23=='>'||(LA36_23 >= '@' && LA36_23 <= 'D')||(LA36_23 >= 'F' && LA36_23 <= 'N')||(LA36_23 >= 'P' && LA36_23 <= 'Z')||(LA36_23 >= '_' && LA36_23 <= 'd')||(LA36_23 >= 'f' && LA36_23 <= 'n')||(LA36_23 >= 'p' && LA36_23 <= 'z')||LA36_23=='|'||(LA36_23 >= '\u007F' && LA36_23 <= '\u2FFF')||(LA36_23 >= '\u3001' && LA36_23 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_23=='\\') ) {s = 33;}
						else if ( (LA36_23=='+') ) {s = 34;}
						else if ( (LA36_23=='-') ) {s = 35;}
						else if ( (LA36_23=='=') ) {s = 42;}
						else if ( (LA36_23=='#') ) {s = 36;}
						else if ( (LA36_23=='/') ) {s = 37;}
						else if ( (LA36_23=='(') ) {s = 38;}
						else if ( (LA36_23=='*'||LA36_23=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 9 : 
						int LA36_68 = input.LA(1);
						s = -1;
						if ( (LA36_68=='(') ) {s = 38;}
						else if ( ((LA36_68 >= '\u0000' && LA36_68 <= '\b')||(LA36_68 >= '\u000B' && LA36_68 <= '\f')||(LA36_68 >= '\u000E' && LA36_68 <= '\u001F')||(LA36_68 >= '$' && LA36_68 <= '\'')||LA36_68=='.'||(LA36_68 >= '0' && LA36_68 <= '9')||LA36_68=='<'||LA36_68=='>'||(LA36_68 >= '@' && LA36_68 <= 'Z')||(LA36_68 >= '_' && LA36_68 <= 'z')||LA36_68=='|'||(LA36_68 >= '\u007F' && LA36_68 <= '\u2FFF')||(LA36_68 >= '\u3001' && LA36_68 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_68=='\\') ) {s = 33;}
						else if ( (LA36_68=='+') ) {s = 34;}
						else if ( (LA36_68=='-') ) {s = 35;}
						else if ( (LA36_68=='=') ) {s = 42;}
						else if ( (LA36_68=='#') ) {s = 36;}
						else if ( (LA36_68=='/') ) {s = 37;}
						else if ( (LA36_68=='*'||LA36_68=='?') ) {s = 39;}
						else s = 83;
						if ( s>=0 ) return s;
						break;

					case 10 : 
						int LA36_22 = input.LA(1);
						s = -1;
						if ( (LA36_22=='R'||LA36_22=='r') ) {s = 55;}
						else if ( ((LA36_22 >= '\u0000' && LA36_22 <= '\b')||(LA36_22 >= '\u000B' && LA36_22 <= '\f')||(LA36_22 >= '\u000E' && LA36_22 <= '\u001F')||(LA36_22 >= '$' && LA36_22 <= '\'')||LA36_22=='.'||(LA36_22 >= '0' && LA36_22 <= '9')||LA36_22=='<'||LA36_22=='>'||(LA36_22 >= '@' && LA36_22 <= 'Q')||(LA36_22 >= 'S' && LA36_22 <= 'Z')||(LA36_22 >= '_' && LA36_22 <= 'q')||(LA36_22 >= 's' && LA36_22 <= 'z')||LA36_22=='|'||(LA36_22 >= '\u007F' && LA36_22 <= '\u2FFF')||(LA36_22 >= '\u3001' && LA36_22 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_22=='\\') ) {s = 33;}
						else if ( (LA36_22=='+') ) {s = 34;}
						else if ( (LA36_22=='-') ) {s = 35;}
						else if ( (LA36_22=='=') ) {s = 42;}
						else if ( (LA36_22=='#') ) {s = 36;}
						else if ( (LA36_22=='/') ) {s = 37;}
						else if ( (LA36_22=='(') ) {s = 38;}
						else if ( (LA36_22=='*'||LA36_22=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 11 : 
						int LA36_34 = input.LA(1);
						s = -1;
						if ( (LA36_34=='(') ) {s = 38;}
						else if ( ((LA36_34 >= '\u0000' && LA36_34 <= '\b')||(LA36_34 >= '\u000B' && LA36_34 <= '\f')||(LA36_34 >= '\u000E' && LA36_34 <= '\u001F')||(LA36_34 >= '$' && LA36_34 <= '\'')||LA36_34=='.'||(LA36_34 >= '0' && LA36_34 <= '9')||LA36_34=='<'||LA36_34=='>'||(LA36_34 >= '@' && LA36_34 <= 'Z')||(LA36_34 >= '_' && LA36_34 <= 'z')||LA36_34=='|'||(LA36_34 >= '\u007F' && LA36_34 <= '\u2FFF')||(LA36_34 >= '\u3001' && LA36_34 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_34=='\\') ) {s = 33;}
						else if ( (LA36_34=='+') ) {s = 34;}
						else if ( (LA36_34=='-') ) {s = 35;}
						else if ( (LA36_34=='=') ) {s = 42;}
						else if ( (LA36_34=='#') ) {s = 36;}
						else if ( (LA36_34=='/') ) {s = 37;}
						else if ( (LA36_34=='*'||LA36_34=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 12 : 
						int LA36_88 = input.LA(1);
						s = -1;
						if ( (LA36_88=='-') ) {s = 99;}
						else if ( (LA36_88=='(') ) {s = 38;}
						else if ( (LA36_88=='.') ) {s = 89;}
						else if ( (LA36_88=='\\') ) {s = 33;}
						else if ( (LA36_88=='+') ) {s = 34;}
						else if ( (LA36_88=='=') ) {s = 42;}
						else if ( (LA36_88=='#') ) {s = 36;}
						else if ( (LA36_88=='/') ) {s = 37;}
						else if ( ((LA36_88 >= '0' && LA36_88 <= '9')) ) {s = 100;}
						else if ( ((LA36_88 >= '\u0000' && LA36_88 <= '\b')||(LA36_88 >= '\u000B' && LA36_88 <= '\f')||(LA36_88 >= '\u000E' && LA36_88 <= '\u001F')||(LA36_88 >= '$' && LA36_88 <= '\'')||LA36_88=='<'||LA36_88=='>'||(LA36_88 >= '@' && LA36_88 <= 'Z')||(LA36_88 >= '_' && LA36_88 <= 'z')||LA36_88=='|'||(LA36_88 >= '\u007F' && LA36_88 <= '\u2FFF')||(LA36_88 >= '\u3001' && LA36_88 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_88=='*'||LA36_88=='?') ) {s = 39;}
						else s = 58;
						if ( s>=0 ) return s;
						break;

					case 13 : 
						int LA36_70 = input.LA(1);
						s = -1;
						if ( (LA36_70=='\"') ) {s = 71;}
						else if ( (LA36_70=='\\') ) {s = 51;}
						else if ( ((LA36_70 >= '\u0000' && LA36_70 <= '!')||(LA36_70 >= '#' && LA36_70 <= ')')||(LA36_70 >= '+' && LA36_70 <= '>')||(LA36_70 >= '@' && LA36_70 <= '[')||(LA36_70 >= ']' && LA36_70 <= '\uFFFF')) ) {s = 52;}
						else if ( (LA36_70=='*'||LA36_70=='?') ) {s = 53;}
						if ( s>=0 ) return s;
						break;

					case 14 : 
						int LA36_89 = input.LA(1);
						s = -1;
						if ( (LA36_89=='(') ) {s = 38;}
						else if ( ((LA36_89 >= '0' && LA36_89 <= '9')) ) {s = 101;}
						else if ( (LA36_89=='\\') ) {s = 33;}
						else if ( (LA36_89=='+') ) {s = 34;}
						else if ( (LA36_89=='-') ) {s = 35;}
						else if ( (LA36_89=='=') ) {s = 42;}
						else if ( (LA36_89=='#') ) {s = 36;}
						else if ( (LA36_89=='/') ) {s = 37;}
						else if ( ((LA36_89 >= '\u0000' && LA36_89 <= '\b')||(LA36_89 >= '\u000B' && LA36_89 <= '\f')||(LA36_89 >= '\u000E' && LA36_89 <= '\u001F')||(LA36_89 >= '$' && LA36_89 <= '\'')||LA36_89=='.'||LA36_89=='<'||LA36_89=='>'||(LA36_89 >= '@' && LA36_89 <= 'Z')||(LA36_89 >= '_' && LA36_89 <= 'z')||LA36_89=='|'||(LA36_89 >= '\u007F' && LA36_89 <= '\u2FFF')||(LA36_89 >= '\u3001' && LA36_89 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_89=='*'||LA36_89=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 15 : 
						int LA36_35 = input.LA(1);
						s = -1;
						if ( (LA36_35=='(') ) {s = 38;}
						else if ( ((LA36_35 >= '\u0000' && LA36_35 <= '\b')||(LA36_35 >= '\u000B' && LA36_35 <= '\f')||(LA36_35 >= '\u000E' && LA36_35 <= '\u001F')||(LA36_35 >= '$' && LA36_35 <= '\'')||LA36_35=='.'||(LA36_35 >= '0' && LA36_35 <= '9')||LA36_35=='<'||LA36_35=='>'||(LA36_35 >= '@' && LA36_35 <= 'Z')||(LA36_35 >= '_' && LA36_35 <= 'z')||LA36_35=='|'||(LA36_35 >= '\u007F' && LA36_35 <= '\u2FFF')||(LA36_35 >= '\u3001' && LA36_35 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_35=='\\') ) {s = 33;}
						else if ( (LA36_35=='+') ) {s = 34;}
						else if ( (LA36_35=='-') ) {s = 35;}
						else if ( (LA36_35=='=') ) {s = 42;}
						else if ( (LA36_35=='#') ) {s = 36;}
						else if ( (LA36_35=='/') ) {s = 37;}
						else if ( (LA36_35=='*'||LA36_35=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 16 : 
						int LA36_42 = input.LA(1);
						s = -1;
						if ( (LA36_42=='(') ) {s = 38;}
						else if ( ((LA36_42 >= '\u0000' && LA36_42 <= '\b')||(LA36_42 >= '\u000B' && LA36_42 <= '\f')||(LA36_42 >= '\u000E' && LA36_42 <= '\u001F')||(LA36_42 >= '$' && LA36_42 <= '\'')||LA36_42=='.'||(LA36_42 >= '0' && LA36_42 <= '9')||LA36_42=='<'||LA36_42=='>'||(LA36_42 >= '@' && LA36_42 <= 'Z')||(LA36_42 >= '_' && LA36_42 <= 'z')||LA36_42=='|'||(LA36_42 >= '\u007F' && LA36_42 <= '\u2FFF')||(LA36_42 >= '\u3001' && LA36_42 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_42=='\\') ) {s = 33;}
						else if ( (LA36_42=='+') ) {s = 34;}
						else if ( (LA36_42=='-') ) {s = 35;}
						else if ( (LA36_42=='=') ) {s = 42;}
						else if ( (LA36_42=='#') ) {s = 36;}
						else if ( (LA36_42=='/') ) {s = 37;}
						else if ( (LA36_42=='*'||LA36_42=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 17 : 
						int LA36_55 = input.LA(1);
						s = -1;
						if ( (LA36_55=='(') ) {s = 38;}
						else if ( ((LA36_55 >= '\u0000' && LA36_55 <= '\b')||(LA36_55 >= '\u000B' && LA36_55 <= '\f')||(LA36_55 >= '\u000E' && LA36_55 <= '\u001F')||(LA36_55 >= '$' && LA36_55 <= '\'')||LA36_55=='.'||(LA36_55 >= '0' && LA36_55 <= '9')||LA36_55=='<'||LA36_55=='>'||(LA36_55 >= '@' && LA36_55 <= 'Z')||(LA36_55 >= '_' && LA36_55 <= 'z')||LA36_55=='|'||(LA36_55 >= '\u007F' && LA36_55 <= '\u2FFF')||(LA36_55 >= '\u3001' && LA36_55 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_55=='\\') ) {s = 33;}
						else if ( (LA36_55=='+') ) {s = 34;}
						else if ( (LA36_55=='-') ) {s = 35;}
						else if ( (LA36_55=='=') ) {s = 42;}
						else if ( (LA36_55=='#') ) {s = 36;}
						else if ( (LA36_55=='/') ) {s = 37;}
						else if ( (LA36_55=='*'||LA36_55=='?') ) {s = 39;}
						else s = 73;
						if ( s>=0 ) return s;
						break;

					case 18 : 
						int LA36_105 = input.LA(1);
						s = -1;
						if ( ((LA36_105 >= '0' && LA36_105 <= '9')) ) {s = 107;}
						else if ( (LA36_105=='(') ) {s = 38;}
						else if ( ((LA36_105 >= '\u0000' && LA36_105 <= '\b')||(LA36_105 >= '\u000B' && LA36_105 <= '\f')||(LA36_105 >= '\u000E' && LA36_105 <= '\u001F')||(LA36_105 >= '$' && LA36_105 <= '\'')||LA36_105=='.'||LA36_105=='<'||LA36_105=='>'||(LA36_105 >= '@' && LA36_105 <= 'Z')||(LA36_105 >= '_' && LA36_105 <= 'z')||LA36_105=='|'||(LA36_105 >= '\u007F' && LA36_105 <= '\u2FFF')||(LA36_105 >= '\u3001' && LA36_105 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_105=='\\') ) {s = 33;}
						else if ( (LA36_105=='+') ) {s = 34;}
						else if ( (LA36_105=='-') ) {s = 35;}
						else if ( (LA36_105=='=') ) {s = 42;}
						else if ( (LA36_105=='#') ) {s = 36;}
						else if ( (LA36_105=='/') ) {s = 37;}
						else if ( (LA36_105=='*'||LA36_105=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 19 : 
						int LA36_36 = input.LA(1);
						s = -1;
						if ( (LA36_36=='(') ) {s = 38;}
						else if ( ((LA36_36 >= '\u0000' && LA36_36 <= '\b')||(LA36_36 >= '\u000B' && LA36_36 <= '\f')||(LA36_36 >= '\u000E' && LA36_36 <= '\u001F')||(LA36_36 >= '$' && LA36_36 <= '\'')||LA36_36=='.'||(LA36_36 >= '0' && LA36_36 <= '9')||LA36_36=='<'||LA36_36=='>'||(LA36_36 >= '@' && LA36_36 <= 'Z')||(LA36_36 >= '_' && LA36_36 <= 'z')||LA36_36=='|'||(LA36_36 >= '\u007F' && LA36_36 <= '\u2FFF')||(LA36_36 >= '\u3001' && LA36_36 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_36=='\\') ) {s = 33;}
						else if ( (LA36_36=='+') ) {s = 34;}
						else if ( (LA36_36=='-') ) {s = 35;}
						else if ( (LA36_36=='=') ) {s = 42;}
						else if ( (LA36_36=='#') ) {s = 36;}
						else if ( (LA36_36=='/') ) {s = 37;}
						else if ( (LA36_36=='*'||LA36_36=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 20 : 
						int LA36_107 = input.LA(1);
						s = -1;
						if ( ((LA36_107 >= '0' && LA36_107 <= '9')) ) {s = 110;}
						else if ( (LA36_107=='(') ) {s = 38;}
						else if ( ((LA36_107 >= '\u0000' && LA36_107 <= '\b')||(LA36_107 >= '\u000B' && LA36_107 <= '\f')||(LA36_107 >= '\u000E' && LA36_107 <= '\u001F')||(LA36_107 >= '$' && LA36_107 <= '\'')||LA36_107=='.'||LA36_107=='<'||LA36_107=='>'||(LA36_107 >= '@' && LA36_107 <= 'Z')||(LA36_107 >= '_' && LA36_107 <= 'z')||LA36_107=='|'||(LA36_107 >= '\u007F' && LA36_107 <= '\u2FFF')||(LA36_107 >= '\u3001' && LA36_107 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_107=='\\') ) {s = 33;}
						else if ( (LA36_107=='+') ) {s = 34;}
						else if ( (LA36_107=='-') ) {s = 35;}
						else if ( (LA36_107=='=') ) {s = 42;}
						else if ( (LA36_107=='#') ) {s = 36;}
						else if ( (LA36_107=='/') ) {s = 37;}
						else if ( (LA36_107=='*'||LA36_107=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 21 : 
						int LA36_110 = input.LA(1);
						s = -1;
						if ( ((LA36_110 >= '0' && LA36_110 <= '9')) ) {s = 112;}
						else if ( (LA36_110=='(') ) {s = 38;}
						else if ( ((LA36_110 >= '\u0000' && LA36_110 <= '\b')||(LA36_110 >= '\u000B' && LA36_110 <= '\f')||(LA36_110 >= '\u000E' && LA36_110 <= '\u001F')||(LA36_110 >= '$' && LA36_110 <= '\'')||LA36_110=='.'||LA36_110=='<'||LA36_110=='>'||(LA36_110 >= '@' && LA36_110 <= 'Z')||(LA36_110 >= '_' && LA36_110 <= 'z')||LA36_110=='|'||(LA36_110 >= '\u007F' && LA36_110 <= '\u2FFF')||(LA36_110 >= '\u3001' && LA36_110 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_110=='\\') ) {s = 33;}
						else if ( (LA36_110=='+') ) {s = 34;}
						else if ( (LA36_110=='-') ) {s = 35;}
						else if ( (LA36_110=='=') ) {s = 42;}
						else if ( (LA36_110=='#') ) {s = 36;}
						else if ( (LA36_110=='/') ) {s = 37;}
						else if ( (LA36_110=='*'||LA36_110=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 22 : 
						int LA36_25 = input.LA(1);
						s = -1;
						if ( ((LA36_25 >= '\u0000' && LA36_25 <= '\b')||(LA36_25 >= '\u000B' && LA36_25 <= '\f')||(LA36_25 >= '\u000E' && LA36_25 <= '\u001F')||(LA36_25 >= '$' && LA36_25 <= '\'')||LA36_25=='.'||(LA36_25 >= '0' && LA36_25 <= '9')||LA36_25=='<'||LA36_25=='>'||(LA36_25 >= '@' && LA36_25 <= 'Z')||(LA36_25 >= '_' && LA36_25 <= 'z')||LA36_25=='|'||(LA36_25 >= '\u007F' && LA36_25 <= '\u2FFF')||(LA36_25 >= '\u3001' && LA36_25 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_25=='\\') ) {s = 33;}
						else if ( (LA36_25=='+') ) {s = 34;}
						else if ( (LA36_25=='-') ) {s = 35;}
						else if ( (LA36_25=='=') ) {s = 42;}
						else if ( (LA36_25=='#') ) {s = 36;}
						else if ( (LA36_25=='/') ) {s = 37;}
						else if ( (LA36_25=='(') ) {s = 38;}
						else if ( (LA36_25=='*'||LA36_25=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 23 : 
						int LA36_37 = input.LA(1);
						s = -1;
						if ( (LA36_37=='(') ) {s = 38;}
						else if ( ((LA36_37 >= '\u0000' && LA36_37 <= '\b')||(LA36_37 >= '\u000B' && LA36_37 <= '\f')||(LA36_37 >= '\u000E' && LA36_37 <= '\u001F')||(LA36_37 >= '$' && LA36_37 <= '\'')||LA36_37=='.'||(LA36_37 >= '0' && LA36_37 <= '9')||LA36_37=='<'||LA36_37=='>'||(LA36_37 >= '@' && LA36_37 <= 'Z')||(LA36_37 >= '_' && LA36_37 <= 'z')||LA36_37=='|'||(LA36_37 >= '\u007F' && LA36_37 <= '\u2FFF')||(LA36_37 >= '\u3001' && LA36_37 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_37=='\\') ) {s = 33;}
						else if ( (LA36_37=='+') ) {s = 34;}
						else if ( (LA36_37=='-') ) {s = 35;}
						else if ( (LA36_37=='=') ) {s = 42;}
						else if ( (LA36_37=='#') ) {s = 36;}
						else if ( (LA36_37=='/') ) {s = 37;}
						else if ( (LA36_37=='*'||LA36_37=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 24 : 
						int LA36_41 = input.LA(1);
						s = -1;
						if ( (LA36_41=='D'||LA36_41=='d') ) {s = 68;}
						else if ( (LA36_41=='(') ) {s = 38;}
						else if ( ((LA36_41 >= '\u0000' && LA36_41 <= '\b')||(LA36_41 >= '\u000B' && LA36_41 <= '\f')||(LA36_41 >= '\u000E' && LA36_41 <= '\u001F')||(LA36_41 >= '$' && LA36_41 <= '\'')||LA36_41=='.'||(LA36_41 >= '0' && LA36_41 <= '9')||LA36_41=='<'||LA36_41=='>'||(LA36_41 >= '@' && LA36_41 <= 'C')||(LA36_41 >= 'E' && LA36_41 <= 'Z')||(LA36_41 >= '_' && LA36_41 <= 'c')||(LA36_41 >= 'e' && LA36_41 <= 'z')||LA36_41=='|'||(LA36_41 >= '\u007F' && LA36_41 <= '\u2FFF')||(LA36_41 >= '\u3001' && LA36_41 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_41=='\\') ) {s = 33;}
						else if ( (LA36_41=='+') ) {s = 34;}
						else if ( (LA36_41=='-') ) {s = 35;}
						else if ( (LA36_41=='=') ) {s = 42;}
						else if ( (LA36_41=='#') ) {s = 36;}
						else if ( (LA36_41=='/') ) {s = 37;}
						else if ( (LA36_41=='*'||LA36_41=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 25 : 
						int LA36_74 = input.LA(1);
						s = -1;
						if ( (LA36_74=='(') ) {s = 38;}
						else if ( ((LA36_74 >= '\u0000' && LA36_74 <= '\b')||(LA36_74 >= '\u000B' && LA36_74 <= '\f')||(LA36_74 >= '\u000E' && LA36_74 <= '\u001F')||(LA36_74 >= '$' && LA36_74 <= '\'')||LA36_74=='.'||(LA36_74 >= '0' && LA36_74 <= '9')||LA36_74=='<'||LA36_74=='>'||(LA36_74 >= '@' && LA36_74 <= 'Z')||(LA36_74 >= '_' && LA36_74 <= 'z')||LA36_74=='|'||(LA36_74 >= '\u007F' && LA36_74 <= '\u2FFF')||(LA36_74 >= '\u3001' && LA36_74 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_74=='\\') ) {s = 33;}
						else if ( (LA36_74=='+') ) {s = 34;}
						else if ( (LA36_74=='-') ) {s = 35;}
						else if ( (LA36_74=='=') ) {s = 42;}
						else if ( (LA36_74=='#') ) {s = 36;}
						else if ( (LA36_74=='/') ) {s = 37;}
						else if ( (LA36_74=='*'||LA36_74=='?') ) {s = 39;}
						else s = 86;
						if ( s>=0 ) return s;
						break;

					case 26 : 
						int LA36_40 = input.LA(1);
						s = -1;
						if ( (LA36_40=='X') ) {s = 66;}
						else if ( (LA36_40=='x') ) {s = 67;}
						else if ( (LA36_40=='(') ) {s = 38;}
						else if ( ((LA36_40 >= '\u0000' && LA36_40 <= '\b')||(LA36_40 >= '\u000B' && LA36_40 <= '\f')||(LA36_40 >= '\u000E' && LA36_40 <= '\u001F')||(LA36_40 >= '$' && LA36_40 <= '\'')||LA36_40=='.'||(LA36_40 >= '0' && LA36_40 <= '9')||LA36_40=='<'||LA36_40=='>'||(LA36_40 >= '@' && LA36_40 <= 'W')||(LA36_40 >= 'Y' && LA36_40 <= 'Z')||(LA36_40 >= '_' && LA36_40 <= 'w')||(LA36_40 >= 'y' && LA36_40 <= 'z')||LA36_40=='|'||(LA36_40 >= '\u007F' && LA36_40 <= '\u2FFF')||(LA36_40 >= '\u3001' && LA36_40 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_40=='\\') ) {s = 33;}
						else if ( (LA36_40=='+') ) {s = 34;}
						else if ( (LA36_40=='-') ) {s = 35;}
						else if ( (LA36_40=='=') ) {s = 42;}
						else if ( (LA36_40=='#') ) {s = 36;}
						else if ( (LA36_40=='/') ) {s = 37;}
						else if ( (LA36_40=='*'||LA36_40=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 27 : 
						int LA36_100 = input.LA(1);
						s = -1;
						if ( (LA36_100=='(') ) {s = 38;}
						else if ( (LA36_100=='.') ) {s = 89;}
						else if ( (LA36_100=='\\') ) {s = 33;}
						else if ( (LA36_100=='+') ) {s = 34;}
						else if ( (LA36_100=='-') ) {s = 35;}
						else if ( (LA36_100=='=') ) {s = 42;}
						else if ( (LA36_100=='#') ) {s = 36;}
						else if ( (LA36_100=='/') ) {s = 37;}
						else if ( ((LA36_100 >= '0' && LA36_100 <= '9')) ) {s = 100;}
						else if ( ((LA36_100 >= '\u0000' && LA36_100 <= '\b')||(LA36_100 >= '\u000B' && LA36_100 <= '\f')||(LA36_100 >= '\u000E' && LA36_100 <= '\u001F')||(LA36_100 >= '$' && LA36_100 <= '\'')||LA36_100=='<'||LA36_100=='>'||(LA36_100 >= '@' && LA36_100 <= 'Z')||(LA36_100 >= '_' && LA36_100 <= 'z')||LA36_100=='|'||(LA36_100 >= '\u007F' && LA36_100 <= '\u2FFF')||(LA36_100 >= '\u3001' && LA36_100 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_100=='*'||LA36_100=='?') ) {s = 39;}
						else s = 58;
						if ( s>=0 ) return s;
						break;

					case 28 : 
						int LA36_56 = input.LA(1);
						s = -1;
						if ( (LA36_56=='T'||LA36_56=='t') ) {s = 74;}
						else if ( (LA36_56=='(') ) {s = 38;}
						else if ( ((LA36_56 >= '\u0000' && LA36_56 <= '\b')||(LA36_56 >= '\u000B' && LA36_56 <= '\f')||(LA36_56 >= '\u000E' && LA36_56 <= '\u001F')||(LA36_56 >= '$' && LA36_56 <= '\'')||LA36_56=='.'||(LA36_56 >= '0' && LA36_56 <= '9')||LA36_56=='<'||LA36_56=='>'||(LA36_56 >= '@' && LA36_56 <= 'S')||(LA36_56 >= 'U' && LA36_56 <= 'Z')||(LA36_56 >= '_' && LA36_56 <= 's')||(LA36_56 >= 'u' && LA36_56 <= 'z')||LA36_56=='|'||(LA36_56 >= '\u007F' && LA36_56 <= '\u2FFF')||(LA36_56 >= '\u3001' && LA36_56 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_56=='\\') ) {s = 33;}
						else if ( (LA36_56=='+') ) {s = 34;}
						else if ( (LA36_56=='-') ) {s = 35;}
						else if ( (LA36_56=='=') ) {s = 42;}
						else if ( (LA36_56=='#') ) {s = 36;}
						else if ( (LA36_56=='/') ) {s = 37;}
						else if ( (LA36_56=='*'||LA36_56=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 29 : 
						int LA36_87 = input.LA(1);
						s = -1;
						if ( ((LA36_87 >= '0' && LA36_87 <= '9')) ) {s = 97;}
						else if ( (LA36_87=='(') ) {s = 38;}
						else if ( ((LA36_87 >= '\u0000' && LA36_87 <= '\b')||(LA36_87 >= '\u000B' && LA36_87 <= '\f')||(LA36_87 >= '\u000E' && LA36_87 <= '\u001F')||(LA36_87 >= '$' && LA36_87 <= '\'')||LA36_87=='.'||LA36_87=='<'||LA36_87=='>'||(LA36_87 >= '@' && LA36_87 <= 'Z')||(LA36_87 >= '_' && LA36_87 <= 'z')||LA36_87=='|'||(LA36_87 >= '\u007F' && LA36_87 <= '\u2FFF')||(LA36_87 >= '\u3001' && LA36_87 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_87=='\\') ) {s = 33;}
						else if ( (LA36_87=='+') ) {s = 34;}
						else if ( (LA36_87=='-') ) {s = 35;}
						else if ( (LA36_87=='=') ) {s = 42;}
						else if ( (LA36_87=='#') ) {s = 36;}
						else if ( (LA36_87=='/') ) {s = 37;}
						else if ( (LA36_87=='*'||LA36_87=='?') ) {s = 39;}
						else s = 98;
						if ( s>=0 ) return s;
						break;

					case 30 : 
						int LA36_13 = input.LA(1);
						s = -1;
						if ( ((LA36_13 >= '\u0000' && LA36_13 <= '\b')||(LA36_13 >= '\u000B' && LA36_13 <= '\f')||(LA36_13 >= '\u000E' && LA36_13 <= '\u001F')||(LA36_13 >= '#' && LA36_13 <= '\'')||LA36_13=='+'||(LA36_13 >= '-' && LA36_13 <= '9')||(LA36_13 >= '<' && LA36_13 <= '>')||(LA36_13 >= '@' && LA36_13 <= 'Z')||LA36_13=='\\'||(LA36_13 >= '_' && LA36_13 <= 'z')||LA36_13=='|'||(LA36_13 >= '\u007F' && LA36_13 <= '\u2FFF')||(LA36_13 >= '\u3001' && LA36_13 <= '\uFFFF')) ) {s = 39;}
						else s = 46;
						if ( s>=0 ) return s;
						break;

					case 31 : 
						int LA36_111 = input.LA(1);
						s = -1;
						if ( (LA36_111=='(') ) {s = 38;}
						else if ( ((LA36_111 >= '\u0000' && LA36_111 <= '\b')||(LA36_111 >= '\u000B' && LA36_111 <= '\f')||(LA36_111 >= '\u000E' && LA36_111 <= '\u001F')||(LA36_111 >= '$' && LA36_111 <= '\'')||LA36_111=='.'||(LA36_111 >= '0' && LA36_111 <= '9')||LA36_111=='<'||LA36_111=='>'||(LA36_111 >= '@' && LA36_111 <= 'Z')||(LA36_111 >= '_' && LA36_111 <= 'z')||LA36_111=='|'||(LA36_111 >= '\u007F' && LA36_111 <= '\u2FFF')||(LA36_111 >= '\u3001' && LA36_111 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_111=='\\') ) {s = 33;}
						else if ( (LA36_111=='+') ) {s = 34;}
						else if ( (LA36_111=='-') ) {s = 35;}
						else if ( (LA36_111=='=') ) {s = 42;}
						else if ( (LA36_111=='#') ) {s = 36;}
						else if ( (LA36_111=='/') ) {s = 37;}
						else if ( (LA36_111=='*'||LA36_111=='?') ) {s = 39;}
						else s = 108;
						if ( s>=0 ) return s;
						break;

					case 32 : 
						int LA36_97 = input.LA(1);
						s = -1;
						if ( ((LA36_97 >= '0' && LA36_97 <= '9')) ) {s = 97;}
						else if ( (LA36_97=='(') ) {s = 38;}
						else if ( ((LA36_97 >= '\u0000' && LA36_97 <= '\b')||(LA36_97 >= '\u000B' && LA36_97 <= '\f')||(LA36_97 >= '\u000E' && LA36_97 <= '\u001F')||(LA36_97 >= '$' && LA36_97 <= '\'')||LA36_97=='.'||LA36_97=='<'||LA36_97=='>'||(LA36_97 >= '@' && LA36_97 <= 'Z')||(LA36_97 >= '_' && LA36_97 <= 'z')||LA36_97=='|'||(LA36_97 >= '\u007F' && LA36_97 <= '\u2FFF')||(LA36_97 >= '\u3001' && LA36_97 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_97=='\\') ) {s = 33;}
						else if ( (LA36_97=='+') ) {s = 34;}
						else if ( (LA36_97=='-') ) {s = 35;}
						else if ( (LA36_97=='=') ) {s = 42;}
						else if ( (LA36_97=='#') ) {s = 36;}
						else if ( (LA36_97=='/') ) {s = 37;}
						else if ( (LA36_97=='*'||LA36_97=='?') ) {s = 39;}
						else s = 98;
						if ( s>=0 ) return s;
						break;

					case 33 : 
						int LA36_57 = input.LA(1);
						s = -1;
						if ( (LA36_57=='A'||LA36_57=='a') ) {s = 75;}
						else if ( (LA36_57=='(') ) {s = 38;}
						else if ( ((LA36_57 >= '\u0000' && LA36_57 <= '\b')||(LA36_57 >= '\u000B' && LA36_57 <= '\f')||(LA36_57 >= '\u000E' && LA36_57 <= '\u001F')||(LA36_57 >= '$' && LA36_57 <= '\'')||LA36_57=='.'||(LA36_57 >= '0' && LA36_57 <= '9')||LA36_57=='<'||LA36_57=='>'||LA36_57=='@'||(LA36_57 >= 'B' && LA36_57 <= 'Z')||(LA36_57 >= '_' && LA36_57 <= '`')||(LA36_57 >= 'b' && LA36_57 <= 'z')||LA36_57=='|'||(LA36_57 >= '\u007F' && LA36_57 <= '\u2FFF')||(LA36_57 >= '\u3001' && LA36_57 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_57=='\\') ) {s = 33;}
						else if ( (LA36_57=='+') ) {s = 34;}
						else if ( (LA36_57=='-') ) {s = 35;}
						else if ( (LA36_57=='=') ) {s = 42;}
						else if ( (LA36_57=='#') ) {s = 36;}
						else if ( (LA36_57=='/') ) {s = 37;}
						else if ( (LA36_57=='*'||LA36_57=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 34 : 
						int LA36_75 = input.LA(1);
						s = -1;
						if ( (LA36_75=='R'||LA36_75=='r') ) {s = 87;}
						else if ( (LA36_75=='(') ) {s = 38;}
						else if ( ((LA36_75 >= '\u0000' && LA36_75 <= '\b')||(LA36_75 >= '\u000B' && LA36_75 <= '\f')||(LA36_75 >= '\u000E' && LA36_75 <= '\u001F')||(LA36_75 >= '$' && LA36_75 <= '\'')||LA36_75=='.'||(LA36_75 >= '0' && LA36_75 <= '9')||LA36_75=='<'||LA36_75=='>'||(LA36_75 >= '@' && LA36_75 <= 'Q')||(LA36_75 >= 'S' && LA36_75 <= 'Z')||(LA36_75 >= '_' && LA36_75 <= 'q')||(LA36_75 >= 's' && LA36_75 <= 'z')||LA36_75=='|'||(LA36_75 >= '\u007F' && LA36_75 <= '\u2FFF')||(LA36_75 >= '\u3001' && LA36_75 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_75=='\\') ) {s = 33;}
						else if ( (LA36_75=='+') ) {s = 34;}
						else if ( (LA36_75=='-') ) {s = 35;}
						else if ( (LA36_75=='=') ) {s = 42;}
						else if ( (LA36_75=='#') ) {s = 36;}
						else if ( (LA36_75=='/') ) {s = 37;}
						else if ( (LA36_75=='*'||LA36_75=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 35 : 
						int LA36_94 = input.LA(1);
						s = -1;
						if ( (LA36_94=='(') ) {s = 38;}
						else if ( (LA36_94=='.') ) {s = 93;}
						else if ( (LA36_94=='\\') ) {s = 33;}
						else if ( (LA36_94=='+') ) {s = 34;}
						else if ( (LA36_94=='-') ) {s = 91;}
						else if ( (LA36_94=='=') ) {s = 42;}
						else if ( (LA36_94=='#') ) {s = 36;}
						else if ( (LA36_94=='/') ) {s = 92;}
						else if ( ((LA36_94 >= '\u0000' && LA36_94 <= '\b')||(LA36_94 >= '\u000B' && LA36_94 <= '\f')||(LA36_94 >= '\u000E' && LA36_94 <= '\u001F')||(LA36_94 >= '$' && LA36_94 <= '\'')||(LA36_94 >= '0' && LA36_94 <= '9')||LA36_94=='<'||LA36_94=='>'||(LA36_94 >= '@' && LA36_94 <= 'Z')||(LA36_94 >= '_' && LA36_94 <= 'z')||LA36_94=='|'||(LA36_94 >= '\u007F' && LA36_94 <= '\u2FFF')||(LA36_94 >= '\u3001' && LA36_94 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_94=='*'||LA36_94=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 36 : 
						int LA36_32 = input.LA(1);
						s = -1;
						if ( (LA36_32=='(') ) {s = 38;}
						else if ( ((LA36_32 >= '\u0000' && LA36_32 <= '\b')||(LA36_32 >= '\u000B' && LA36_32 <= '\f')||(LA36_32 >= '\u000E' && LA36_32 <= '\u001F')||(LA36_32 >= '$' && LA36_32 <= '\'')||LA36_32=='.'||(LA36_32 >= '0' && LA36_32 <= '9')||LA36_32=='<'||LA36_32=='>'||(LA36_32 >= '@' && LA36_32 <= 'Z')||(LA36_32 >= '_' && LA36_32 <= 'z')||LA36_32=='|'||(LA36_32 >= '\u007F' && LA36_32 <= '\u2FFF')||(LA36_32 >= '\u3001' && LA36_32 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_32=='\\') ) {s = 33;}
						else if ( (LA36_32=='+') ) {s = 34;}
						else if ( (LA36_32=='-') ) {s = 35;}
						else if ( (LA36_32=='=') ) {s = 42;}
						else if ( (LA36_32=='#') ) {s = 36;}
						else if ( (LA36_32=='/') ) {s = 37;}
						else if ( (LA36_32=='*'||LA36_32=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 37 : 
						int LA36_24 = input.LA(1);
						s = -1;
						if ( ((LA36_24 >= '0' && LA36_24 <= '9')) ) {s = 59;}
						else if ( (LA36_24=='.') ) {s = 60;}
						else if ( (LA36_24=='\\') ) {s = 33;}
						else if ( (LA36_24=='+') ) {s = 34;}
						else if ( (LA36_24=='-') ) {s = 61;}
						else if ( (LA36_24=='=') ) {s = 42;}
						else if ( (LA36_24=='#') ) {s = 36;}
						else if ( (LA36_24=='/') ) {s = 62;}
						else if ( (LA36_24=='(') ) {s = 38;}
						else if ( ((LA36_24 >= '\u0000' && LA36_24 <= '\b')||(LA36_24 >= '\u000B' && LA36_24 <= '\f')||(LA36_24 >= '\u000E' && LA36_24 <= '\u001F')||(LA36_24 >= '$' && LA36_24 <= '\'')||LA36_24=='<'||LA36_24=='>'||(LA36_24 >= '@' && LA36_24 <= 'Z')||(LA36_24 >= '_' && LA36_24 <= 'z')||LA36_24=='|'||(LA36_24 >= '\u007F' && LA36_24 <= '\u2FFF')||(LA36_24 >= '\u3001' && LA36_24 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_24=='*'||LA36_24=='?') ) {s = 39;}
						else s = 58;
						if ( s>=0 ) return s;
						break;

					case 38 : 
						int LA36_26 = input.LA(1);
						s = -1;
						if ( ((LA36_26 >= '\u0000' && LA36_26 <= '\uFFFF')) ) {s = 63;}
						if ( s>=0 ) return s;
						break;

					case 39 : 
						int LA36_33 = input.LA(1);
						s = -1;
						if ( ((LA36_33 >= '\u0000' && LA36_33 <= '\uFFFF')) ) {s = 65;}
						if ( s>=0 ) return s;
						break;

					case 40 : 
						int LA36_79 = input.LA(1);
						s = -1;
						if ( (LA36_79=='(') ) {s = 38;}
						else if ( ((LA36_79 >= '0' && LA36_79 <= '9')) ) {s = 94;}
						else if ( (LA36_79=='\\') ) {s = 33;}
						else if ( (LA36_79=='+') ) {s = 34;}
						else if ( (LA36_79=='-') ) {s = 91;}
						else if ( (LA36_79=='=') ) {s = 42;}
						else if ( (LA36_79=='#') ) {s = 36;}
						else if ( (LA36_79=='/') ) {s = 92;}
						else if ( (LA36_79=='.') ) {s = 93;}
						else if ( ((LA36_79 >= '\u0000' && LA36_79 <= '\b')||(LA36_79 >= '\u000B' && LA36_79 <= '\f')||(LA36_79 >= '\u000E' && LA36_79 <= '\u001F')||(LA36_79 >= '$' && LA36_79 <= '\'')||LA36_79=='<'||LA36_79=='>'||(LA36_79 >= '@' && LA36_79 <= 'Z')||(LA36_79 >= '_' && LA36_79 <= 'z')||LA36_79=='|'||(LA36_79 >= '\u007F' && LA36_79 <= '\u2FFF')||(LA36_79 >= '\u3001' && LA36_79 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_79=='*'||LA36_79=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 41 : 
						int LA36_102 = input.LA(1);
						s = -1;
						if ( (LA36_102=='(') ) {s = 38;}
						else if ( ((LA36_102 >= '0' && LA36_102 <= '9')) ) {s = 106;}
						else if ( (LA36_102=='\\') ) {s = 33;}
						else if ( (LA36_102=='+') ) {s = 34;}
						else if ( (LA36_102=='-') ) {s = 35;}
						else if ( (LA36_102=='=') ) {s = 42;}
						else if ( (LA36_102=='#') ) {s = 36;}
						else if ( (LA36_102=='/') ) {s = 37;}
						else if ( ((LA36_102 >= '\u0000' && LA36_102 <= '\b')||(LA36_102 >= '\u000B' && LA36_102 <= '\f')||(LA36_102 >= '\u000E' && LA36_102 <= '\u001F')||(LA36_102 >= '$' && LA36_102 <= '\'')||LA36_102=='.'||LA36_102=='<'||LA36_102=='>'||(LA36_102 >= '@' && LA36_102 <= 'Z')||(LA36_102 >= '_' && LA36_102 <= 'z')||LA36_102=='|'||(LA36_102 >= '\u007F' && LA36_102 <= '\u2FFF')||(LA36_102 >= '\u3001' && LA36_102 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_102=='*'||LA36_102=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 42 : 
						int LA36_90 = input.LA(1);
						s = -1;
						if ( (LA36_90=='(') ) {s = 38;}
						else if ( (LA36_90=='.') ) {s = 93;}
						else if ( (LA36_90=='\\') ) {s = 33;}
						else if ( (LA36_90=='+') ) {s = 34;}
						else if ( (LA36_90=='-') ) {s = 91;}
						else if ( (LA36_90=='=') ) {s = 42;}
						else if ( (LA36_90=='#') ) {s = 36;}
						else if ( (LA36_90=='/') ) {s = 92;}
						else if ( ((LA36_90 >= '0' && LA36_90 <= '9')) ) {s = 101;}
						else if ( ((LA36_90 >= '\u0000' && LA36_90 <= '\b')||(LA36_90 >= '\u000B' && LA36_90 <= '\f')||(LA36_90 >= '\u000E' && LA36_90 <= '\u001F')||(LA36_90 >= '$' && LA36_90 <= '\'')||LA36_90=='<'||LA36_90=='>'||(LA36_90 >= '@' && LA36_90 <= 'Z')||(LA36_90 >= '_' && LA36_90 <= 'z')||LA36_90=='|'||(LA36_90 >= '\u007F' && LA36_90 <= '\u2FFF')||(LA36_90 >= '\u3001' && LA36_90 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_90=='*'||LA36_90=='?') ) {s = 39;}
						else s = 58;
						if ( s>=0 ) return s;
						break;

					case 43 : 
						int LA36_101 = input.LA(1);
						s = -1;
						if ( (LA36_101=='(') ) {s = 38;}
						else if ( ((LA36_101 >= '0' && LA36_101 <= '9')) ) {s = 101;}
						else if ( (LA36_101=='\\') ) {s = 33;}
						else if ( (LA36_101=='+') ) {s = 34;}
						else if ( (LA36_101=='-') ) {s = 35;}
						else if ( (LA36_101=='=') ) {s = 42;}
						else if ( (LA36_101=='#') ) {s = 36;}
						else if ( (LA36_101=='/') ) {s = 37;}
						else if ( ((LA36_101 >= '\u0000' && LA36_101 <= '\b')||(LA36_101 >= '\u000B' && LA36_101 <= '\f')||(LA36_101 >= '\u000E' && LA36_101 <= '\u001F')||(LA36_101 >= '$' && LA36_101 <= '\'')||LA36_101=='.'||LA36_101=='<'||LA36_101=='>'||(LA36_101 >= '@' && LA36_101 <= 'Z')||(LA36_101 >= '_' && LA36_101 <= 'z')||LA36_101=='|'||(LA36_101 >= '\u007F' && LA36_101 <= '\u2FFF')||(LA36_101 >= '\u3001' && LA36_101 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_101=='*'||LA36_101=='?') ) {s = 39;}
						else s = 58;
						if ( s>=0 ) return s;
						break;

					case 44 : 
						int LA36_99 = input.LA(1);
						s = -1;
						if ( ((LA36_99 >= '0' && LA36_99 <= '9')) ) {s = 105;}
						else if ( (LA36_99=='(') ) {s = 38;}
						else if ( ((LA36_99 >= '\u0000' && LA36_99 <= '\b')||(LA36_99 >= '\u000B' && LA36_99 <= '\f')||(LA36_99 >= '\u000E' && LA36_99 <= '\u001F')||(LA36_99 >= '$' && LA36_99 <= '\'')||LA36_99=='.'||LA36_99=='<'||LA36_99=='>'||(LA36_99 >= '@' && LA36_99 <= 'Z')||(LA36_99 >= '_' && LA36_99 <= 'z')||LA36_99=='|'||(LA36_99 >= '\u007F' && LA36_99 <= '\u2FFF')||(LA36_99 >= '\u3001' && LA36_99 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_99=='\\') ) {s = 33;}
						else if ( (LA36_99=='+') ) {s = 34;}
						else if ( (LA36_99=='-') ) {s = 35;}
						else if ( (LA36_99=='=') ) {s = 42;}
						else if ( (LA36_99=='#') ) {s = 36;}
						else if ( (LA36_99=='/') ) {s = 37;}
						else if ( (LA36_99=='*'||LA36_99=='?') ) {s = 39;}
						else s = 45;
						if ( s>=0 ) return s;
						break;

					case 45 : 
						int LA36_109 = input.LA(1);
						s = -1;
						if ( (LA36_109=='(') ) {s = 38;}
						else if ( ((LA36_109 >= '0' && LA36_109 <= '9')) ) {s = 111;}
						else if ( (LA36_109=='\\') ) {s = 33;}
						else if ( (LA36_109=='+') ) {s = 34;}
						else if ( (LA36_109=='-') ) {s = 35;}
						else if ( (LA36_109=='=') ) {s = 42;}
						else if ( (LA36_109=='#') ) {s = 36;}
						else if ( (LA36_109=='/') ) {s = 37;}
						else if ( ((LA36_109 >= '\u0000' && LA36_109 <= '\b')||(LA36_109 >= '\u000B' && LA36_109 <= '\f')||(LA36_109 >= '\u000E' && LA36_109 <= '\u001F')||(LA36_109 >= '$' && LA36_109 <= '\'')||LA36_109=='.'||LA36_109=='<'||LA36_109=='>'||(LA36_109 >= '@' && LA36_109 <= 'Z')||(LA36_109 >= '_' && LA36_109 <= 'z')||LA36_109=='|'||(LA36_109 >= '\u007F' && LA36_109 <= '\u2FFF')||(LA36_109 >= '\u3001' && LA36_109 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_109=='*'||LA36_109=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 46 : 
						int LA36_30 = input.LA(1);
						s = -1;
						if ( (LA36_30=='>') ) {s = 64;}
						else if ( (LA36_30=='(') ) {s = 38;}
						else if ( ((LA36_30 >= '\u0000' && LA36_30 <= '\b')||(LA36_30 >= '\u000B' && LA36_30 <= '\f')||(LA36_30 >= '\u000E' && LA36_30 <= '\u001F')||(LA36_30 >= '$' && LA36_30 <= '\'')||LA36_30=='.'||(LA36_30 >= '0' && LA36_30 <= '9')||LA36_30=='<'||(LA36_30 >= '@' && LA36_30 <= 'Z')||(LA36_30 >= '_' && LA36_30 <= 'z')||LA36_30=='|'||(LA36_30 >= '\u007F' && LA36_30 <= '\u2FFF')||(LA36_30 >= '\u3001' && LA36_30 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_30=='\\') ) {s = 33;}
						else if ( (LA36_30=='+') ) {s = 34;}
						else if ( (LA36_30=='-') ) {s = 35;}
						else if ( (LA36_30=='=') ) {s = 42;}
						else if ( (LA36_30=='#') ) {s = 36;}
						else if ( (LA36_30=='/') ) {s = 37;}
						else if ( (LA36_30=='*'||LA36_30=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 47 : 
						int LA36_15 = input.LA(1);
						s = -1;
						if ( ((LA36_15 >= '\u0000' && LA36_15 <= '\u001F')||(LA36_15 >= '!' && LA36_15 <= '#')||(LA36_15 >= '%' && LA36_15 <= '*')||(LA36_15 >= '.' && LA36_15 <= '/')||(LA36_15 >= ':' && LA36_15 <= '\uFFFF')) ) {s = 49;}
						else s = 48;
						if ( s>=0 ) return s;
						break;

					case 48 : 
						int LA36_60 = input.LA(1);
						s = -1;
						if ( (LA36_60=='(') ) {s = 38;}
						else if ( ((LA36_60 >= '0' && LA36_60 <= '9')) ) {s = 78;}
						else if ( (LA36_60=='\\') ) {s = 33;}
						else if ( (LA36_60=='+') ) {s = 34;}
						else if ( (LA36_60=='-') ) {s = 35;}
						else if ( (LA36_60=='=') ) {s = 42;}
						else if ( (LA36_60=='#') ) {s = 36;}
						else if ( (LA36_60=='/') ) {s = 37;}
						else if ( ((LA36_60 >= '\u0000' && LA36_60 <= '\b')||(LA36_60 >= '\u000B' && LA36_60 <= '\f')||(LA36_60 >= '\u000E' && LA36_60 <= '\u001F')||(LA36_60 >= '$' && LA36_60 <= '\'')||LA36_60=='.'||LA36_60=='<'||LA36_60=='>'||(LA36_60 >= '@' && LA36_60 <= 'Z')||(LA36_60 >= '_' && LA36_60 <= 'z')||LA36_60=='|'||(LA36_60 >= '\u007F' && LA36_60 <= '\u2FFF')||(LA36_60 >= '\u3001' && LA36_60 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_60=='*'||LA36_60=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 49 : 
						int LA36_2 = input.LA(1);
						s = -1;
						if ( (LA36_2=='=') ) {s = 30;}
						else if ( ((LA36_2 >= '\u0000' && LA36_2 <= '\b')||(LA36_2 >= '\u000B' && LA36_2 <= '\f')||(LA36_2 >= '\u000E' && LA36_2 <= '\u001F')||(LA36_2 >= '$' && LA36_2 <= '\'')||LA36_2=='.'||(LA36_2 >= '0' && LA36_2 <= '9')||LA36_2=='<'||LA36_2=='>'||(LA36_2 >= '@' && LA36_2 <= 'Z')||(LA36_2 >= '_' && LA36_2 <= 'z')||LA36_2=='|'||(LA36_2 >= '\u007F' && LA36_2 <= '\u2FFF')||(LA36_2 >= '\u3001' && LA36_2 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_2=='\\') ) {s = 33;}
						else if ( (LA36_2=='+') ) {s = 34;}
						else if ( (LA36_2=='-') ) {s = 35;}
						else if ( (LA36_2=='#') ) {s = 36;}
						else if ( (LA36_2=='/') ) {s = 37;}
						else if ( (LA36_2=='(') ) {s = 38;}
						else if ( (LA36_2=='*'||LA36_2=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 50 : 
						int LA36_61 = input.LA(1);
						s = -1;
						if ( (LA36_61=='(') ) {s = 38;}
						else if ( ((LA36_61 >= '0' && LA36_61 <= '9')) ) {s = 79;}
						else if ( (LA36_61=='\\') ) {s = 33;}
						else if ( (LA36_61=='+') ) {s = 34;}
						else if ( (LA36_61=='-') ) {s = 35;}
						else if ( (LA36_61=='=') ) {s = 42;}
						else if ( (LA36_61=='#') ) {s = 36;}
						else if ( (LA36_61=='/') ) {s = 37;}
						else if ( ((LA36_61 >= '\u0000' && LA36_61 <= '\b')||(LA36_61 >= '\u000B' && LA36_61 <= '\f')||(LA36_61 >= '\u000E' && LA36_61 <= '\u001F')||(LA36_61 >= '$' && LA36_61 <= '\'')||LA36_61=='.'||LA36_61=='<'||LA36_61=='>'||(LA36_61 >= '@' && LA36_61 <= 'Z')||(LA36_61 >= '_' && LA36_61 <= 'z')||LA36_61=='|'||(LA36_61 >= '\u007F' && LA36_61 <= '\u2FFF')||(LA36_61 >= '\u3001' && LA36_61 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_61=='*'||LA36_61=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 51 : 
						int LA36_51 = input.LA(1);
						s = -1;
						if ( ((LA36_51 >= '\u0000' && LA36_51 <= '\uFFFF')) ) {s = 70;}
						if ( s>=0 ) return s;
						break;

					case 52 : 
						int LA36_91 = input.LA(1);
						s = -1;
						if ( (LA36_91=='(') ) {s = 38;}
						else if ( ((LA36_91 >= '0' && LA36_91 <= '9')) ) {s = 102;}
						else if ( (LA36_91=='\\') ) {s = 33;}
						else if ( (LA36_91=='+') ) {s = 34;}
						else if ( (LA36_91=='-') ) {s = 35;}
						else if ( (LA36_91=='=') ) {s = 42;}
						else if ( (LA36_91=='#') ) {s = 36;}
						else if ( (LA36_91=='/') ) {s = 37;}
						else if ( ((LA36_91 >= '\u0000' && LA36_91 <= '\b')||(LA36_91 >= '\u000B' && LA36_91 <= '\f')||(LA36_91 >= '\u000E' && LA36_91 <= '\u001F')||(LA36_91 >= '$' && LA36_91 <= '\'')||LA36_91=='.'||LA36_91=='<'||LA36_91=='>'||(LA36_91 >= '@' && LA36_91 <= 'Z')||(LA36_91 >= '_' && LA36_91 <= 'z')||LA36_91=='|'||(LA36_91 >= '\u007F' && LA36_91 <= '\u2FFF')||(LA36_91 >= '\u3001' && LA36_91 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_91=='*'||LA36_91=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 53 : 
						int LA36_62 = input.LA(1);
						s = -1;
						if ( (LA36_62=='(') ) {s = 38;}
						else if ( ((LA36_62 >= '0' && LA36_62 <= '9')) ) {s = 79;}
						else if ( (LA36_62=='\\') ) {s = 33;}
						else if ( (LA36_62=='+') ) {s = 34;}
						else if ( (LA36_62=='-') ) {s = 35;}
						else if ( (LA36_62=='=') ) {s = 42;}
						else if ( (LA36_62=='#') ) {s = 36;}
						else if ( (LA36_62=='/') ) {s = 37;}
						else if ( ((LA36_62 >= '\u0000' && LA36_62 <= '\b')||(LA36_62 >= '\u000B' && LA36_62 <= '\f')||(LA36_62 >= '\u000E' && LA36_62 <= '\u001F')||(LA36_62 >= '$' && LA36_62 <= '\'')||LA36_62=='.'||LA36_62=='<'||LA36_62=='>'||(LA36_62 >= '@' && LA36_62 <= 'Z')||(LA36_62 >= '_' && LA36_62 <= 'z')||LA36_62=='|'||(LA36_62 >= '\u007F' && LA36_62 <= '\u2FFF')||(LA36_62 >= '\u3001' && LA36_62 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_62=='*'||LA36_62=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 54 : 
						int LA36_17 = input.LA(1);
						s = -1;
						if ( (LA36_17=='\\') ) {s = 51;}
						else if ( ((LA36_17 >= '\u0000' && LA36_17 <= '!')||(LA36_17 >= '#' && LA36_17 <= ')')||(LA36_17 >= '+' && LA36_17 <= '>')||(LA36_17 >= '@' && LA36_17 <= '[')||(LA36_17 >= ']' && LA36_17 <= '\uFFFF')) ) {s = 52;}
						else if ( (LA36_17=='*'||LA36_17=='?') ) {s = 53;}
						else s = 50;
						if ( s>=0 ) return s;
						break;

					case 55 : 
						int LA36_78 = input.LA(1);
						s = -1;
						if ( (LA36_78=='(') ) {s = 38;}
						else if ( ((LA36_78 >= '0' && LA36_78 <= '9')) ) {s = 90;}
						else if ( (LA36_78=='\\') ) {s = 33;}
						else if ( (LA36_78=='+') ) {s = 34;}
						else if ( (LA36_78=='-') ) {s = 91;}
						else if ( (LA36_78=='=') ) {s = 42;}
						else if ( (LA36_78=='#') ) {s = 36;}
						else if ( (LA36_78=='/') ) {s = 92;}
						else if ( (LA36_78=='.') ) {s = 93;}
						else if ( ((LA36_78 >= '\u0000' && LA36_78 <= '\b')||(LA36_78 >= '\u000B' && LA36_78 <= '\f')||(LA36_78 >= '\u000E' && LA36_78 <= '\u001F')||(LA36_78 >= '$' && LA36_78 <= '\'')||LA36_78=='<'||LA36_78=='>'||(LA36_78 >= '@' && LA36_78 <= 'Z')||(LA36_78 >= '_' && LA36_78 <= 'z')||LA36_78=='|'||(LA36_78 >= '\u007F' && LA36_78 <= '\u2FFF')||(LA36_78 >= '\u3001' && LA36_78 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_78=='*'||LA36_78=='?') ) {s = 39;}
						else s = 58;
						if ( s>=0 ) return s;
						break;

					case 56 : 
						int LA36_5 = input.LA(1);
						s = -1;
						if ( (LA36_5=='o') ) {s = 43;}
						else if ( ((LA36_5 >= '\u0000' && LA36_5 <= '\b')||(LA36_5 >= '\u000B' && LA36_5 <= '\f')||(LA36_5 >= '\u000E' && LA36_5 <= '\u001F')||(LA36_5 >= '$' && LA36_5 <= '\'')||LA36_5=='.'||(LA36_5 >= '0' && LA36_5 <= '9')||LA36_5=='<'||LA36_5=='>'||(LA36_5 >= '@' && LA36_5 <= 'Z')||(LA36_5 >= '_' && LA36_5 <= 'n')||(LA36_5 >= 'p' && LA36_5 <= 'z')||LA36_5=='|'||(LA36_5 >= '\u007F' && LA36_5 <= '\u2FFF')||(LA36_5 >= '\u3001' && LA36_5 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_5=='\\') ) {s = 33;}
						else if ( (LA36_5=='+') ) {s = 34;}
						else if ( (LA36_5=='-') ) {s = 35;}
						else if ( (LA36_5=='=') ) {s = 42;}
						else if ( (LA36_5=='#') ) {s = 36;}
						else if ( (LA36_5=='/') ) {s = 37;}
						else if ( (LA36_5=='(') ) {s = 38;}
						else if ( (LA36_5=='*'||LA36_5=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 57 : 
						int LA36_92 = input.LA(1);
						s = -1;
						if ( (LA36_92=='(') ) {s = 38;}
						else if ( ((LA36_92 >= '0' && LA36_92 <= '9')) ) {s = 102;}
						else if ( (LA36_92=='\\') ) {s = 33;}
						else if ( (LA36_92=='+') ) {s = 34;}
						else if ( (LA36_92=='-') ) {s = 35;}
						else if ( (LA36_92=='=') ) {s = 42;}
						else if ( (LA36_92=='#') ) {s = 36;}
						else if ( (LA36_92=='/') ) {s = 37;}
						else if ( ((LA36_92 >= '\u0000' && LA36_92 <= '\b')||(LA36_92 >= '\u000B' && LA36_92 <= '\f')||(LA36_92 >= '\u000E' && LA36_92 <= '\u001F')||(LA36_92 >= '$' && LA36_92 <= '\'')||LA36_92=='.'||LA36_92=='<'||LA36_92=='>'||(LA36_92 >= '@' && LA36_92 <= 'Z')||(LA36_92 >= '_' && LA36_92 <= 'z')||LA36_92=='|'||(LA36_92 >= '\u007F' && LA36_92 <= '\u2FFF')||(LA36_92 >= '\u3001' && LA36_92 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_92=='*'||LA36_92=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 58 : 
						int LA36_63 = input.LA(1);
						s = -1;
						if ( ((LA36_63 >= '\u0000' && LA36_63 <= '\b')||(LA36_63 >= '\u000B' && LA36_63 <= '\f')||(LA36_63 >= '\u000E' && LA36_63 <= '\u001F')||(LA36_63 >= '$' && LA36_63 <= '\'')||LA36_63=='.'||(LA36_63 >= '0' && LA36_63 <= '9')||LA36_63=='<'||LA36_63=='>'||(LA36_63 >= '@' && LA36_63 <= 'Z')||(LA36_63 >= '_' && LA36_63 <= 'z')||LA36_63=='|'||(LA36_63 >= '\u007F' && LA36_63 <= '\u2FFF')||(LA36_63 >= '\u3001' && LA36_63 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_63=='\\') ) {s = 33;}
						else if ( (LA36_63=='+') ) {s = 34;}
						else if ( (LA36_63=='-') ) {s = 35;}
						else if ( (LA36_63=='=') ) {s = 42;}
						else if ( (LA36_63=='#') ) {s = 36;}
						else if ( (LA36_63=='/') ) {s = 37;}
						else if ( (LA36_63=='(') ) {s = 38;}
						else if ( (LA36_63=='*'||LA36_63=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 59 : 
						int LA36_52 = input.LA(1);
						s = -1;
						if ( (LA36_52=='\"') ) {s = 71;}
						else if ( (LA36_52=='\\') ) {s = 51;}
						else if ( ((LA36_52 >= '\u0000' && LA36_52 <= '!')||(LA36_52 >= '#' && LA36_52 <= ')')||(LA36_52 >= '+' && LA36_52 <= '>')||(LA36_52 >= '@' && LA36_52 <= '[')||(LA36_52 >= ']' && LA36_52 <= '\uFFFF')) ) {s = 52;}
						else if ( (LA36_52=='*'||LA36_52=='?') ) {s = 53;}
						if ( s>=0 ) return s;
						break;

					case 60 : 
						int LA36_66 = input.LA(1);
						s = -1;
						if ( (LA36_66=='i') ) {s = 81;}
						else if ( (LA36_66=='(') ) {s = 38;}
						else if ( ((LA36_66 >= '\u0000' && LA36_66 <= '\b')||(LA36_66 >= '\u000B' && LA36_66 <= '\f')||(LA36_66 >= '\u000E' && LA36_66 <= '\u001F')||(LA36_66 >= '$' && LA36_66 <= '\'')||LA36_66=='.'||(LA36_66 >= '0' && LA36_66 <= '9')||LA36_66=='<'||LA36_66=='>'||(LA36_66 >= '@' && LA36_66 <= 'Z')||(LA36_66 >= '_' && LA36_66 <= 'h')||(LA36_66 >= 'j' && LA36_66 <= 'z')||LA36_66=='|'||(LA36_66 >= '\u007F' && LA36_66 <= '\u2FFF')||(LA36_66 >= '\u3001' && LA36_66 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_66=='\\') ) {s = 33;}
						else if ( (LA36_66=='+') ) {s = 34;}
						else if ( (LA36_66=='-') ) {s = 35;}
						else if ( (LA36_66=='=') ) {s = 42;}
						else if ( (LA36_66=='#') ) {s = 36;}
						else if ( (LA36_66=='/') ) {s = 37;}
						else if ( (LA36_66=='*'||LA36_66=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 61 : 
						int LA36_112 = input.LA(1);
						s = -1;
						if ( (LA36_112=='(') ) {s = 38;}
						else if ( ((LA36_112 >= '\u0000' && LA36_112 <= '\b')||(LA36_112 >= '\u000B' && LA36_112 <= '\f')||(LA36_112 >= '\u000E' && LA36_112 <= '\u001F')||(LA36_112 >= '$' && LA36_112 <= '\'')||LA36_112=='.'||(LA36_112 >= '0' && LA36_112 <= '9')||LA36_112=='<'||LA36_112=='>'||(LA36_112 >= '@' && LA36_112 <= 'Z')||(LA36_112 >= '_' && LA36_112 <= 'z')||LA36_112=='|'||(LA36_112 >= '\u007F' && LA36_112 <= '\u2FFF')||(LA36_112 >= '\u3001' && LA36_112 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_112=='\\') ) {s = 33;}
						else if ( (LA36_112=='+') ) {s = 34;}
						else if ( (LA36_112=='-') ) {s = 35;}
						else if ( (LA36_112=='=') ) {s = 42;}
						else if ( (LA36_112=='#') ) {s = 36;}
						else if ( (LA36_112=='/') ) {s = 37;}
						else if ( (LA36_112=='*'||LA36_112=='?') ) {s = 39;}
						else s = 45;
						if ( s>=0 ) return s;
						break;

					case 62 : 
						int LA36_0 = input.LA(1);
						s = -1;
						if ( (LA36_0=='#') ) {s = 1;}
						else if ( (LA36_0=='<') ) {s = 2;}
						else if ( (LA36_0=='=') ) {s = 3;}
						else if ( (LA36_0=='a') ) {s = 4;}
						else if ( (LA36_0=='d') ) {s = 5;}
						else if ( (LA36_0=='(') ) {s = 6;}
						else if ( (LA36_0==')') ) {s = 7;}
						else if ( (LA36_0=='[') ) {s = 8;}
						else if ( (LA36_0==']') ) {s = 9;}
						else if ( (LA36_0==':') ) {s = 10;}
						else if ( (LA36_0=='+') ) {s = 11;}
						else if ( (LA36_0=='-') ) {s = 12;}
						else if ( (LA36_0=='*') ) {s = 13;}
						else if ( (LA36_0=='?') ) {s = 14;}
						else if ( (LA36_0=='^') ) {s = 15;}
						else if ( (LA36_0=='~') ) {s = 16;}
						else if ( (LA36_0=='\"') ) {s = 17;}
						else if ( (LA36_0==',') ) {s = 18;}
						else if ( (LA36_0==';') ) {s = 19;}
						else if ( (LA36_0=='T') ) {s = 20;}
						else if ( (LA36_0=='A') ) {s = 21;}
						else if ( (LA36_0=='O'||LA36_0=='o') ) {s = 22;}
						else if ( (LA36_0=='N'||LA36_0=='n') ) {s = 23;}
						else if ( ((LA36_0 >= '0' && LA36_0 <= '9')) ) {s = 24;}
						else if ( ((LA36_0 >= '\u0000' && LA36_0 <= '\b')||(LA36_0 >= '\u000B' && LA36_0 <= '\f')||(LA36_0 >= '\u000E' && LA36_0 <= '\u001F')||(LA36_0 >= '$' && LA36_0 <= '\'')||LA36_0=='.'||LA36_0=='>'||LA36_0=='@'||(LA36_0 >= 'B' && LA36_0 <= 'M')||(LA36_0 >= 'P' && LA36_0 <= 'S')||(LA36_0 >= 'U' && LA36_0 <= 'Z')||(LA36_0 >= '_' && LA36_0 <= '`')||(LA36_0 >= 'b' && LA36_0 <= 'c')||(LA36_0 >= 'e' && LA36_0 <= 'm')||(LA36_0 >= 'p' && LA36_0 <= 'z')||LA36_0=='|'||(LA36_0 >= '\u007F' && LA36_0 <= '\u2FFF')||(LA36_0 >= '\u3001' && LA36_0 <= '\uFFFF')) ) {s = 25;}
						else if ( (LA36_0=='\\') ) {s = 26;}
						else if ( ((LA36_0 >= '\t' && LA36_0 <= '\n')||LA36_0=='\r'||LA36_0==' '||LA36_0=='\u3000') ) {s = 27;}
						else if ( (LA36_0=='{') ) {s = 28;}
						else if ( (LA36_0=='/') ) {s = 29;}
						if ( s>=0 ) return s;
						break;

					case 63 : 
						int LA36_81 = input.LA(1);
						s = -1;
						if ( (LA36_81=='v') ) {s = 95;}
						else if ( (LA36_81=='(') ) {s = 38;}
						else if ( ((LA36_81 >= '\u0000' && LA36_81 <= '\b')||(LA36_81 >= '\u000B' && LA36_81 <= '\f')||(LA36_81 >= '\u000E' && LA36_81 <= '\u001F')||(LA36_81 >= '$' && LA36_81 <= '\'')||LA36_81=='.'||(LA36_81 >= '0' && LA36_81 <= '9')||LA36_81=='<'||LA36_81=='>'||(LA36_81 >= '@' && LA36_81 <= 'Z')||(LA36_81 >= '_' && LA36_81 <= 'u')||(LA36_81 >= 'w' && LA36_81 <= 'z')||LA36_81=='|'||(LA36_81 >= '\u007F' && LA36_81 <= '\u2FFF')||(LA36_81 >= '\u3001' && LA36_81 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_81=='\\') ) {s = 33;}
						else if ( (LA36_81=='+') ) {s = 34;}
						else if ( (LA36_81=='-') ) {s = 35;}
						else if ( (LA36_81=='=') ) {s = 42;}
						else if ( (LA36_81=='#') ) {s = 36;}
						else if ( (LA36_81=='/') ) {s = 37;}
						else if ( (LA36_81=='*'||LA36_81=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 64 : 
						int LA36_95 = input.LA(1);
						s = -1;
						if ( (LA36_95==':') ) {s = 103;}
						else if ( (LA36_95=='(') ) {s = 38;}
						else if ( ((LA36_95 >= '\u0000' && LA36_95 <= '\b')||(LA36_95 >= '\u000B' && LA36_95 <= '\f')||(LA36_95 >= '\u000E' && LA36_95 <= '\u001F')||(LA36_95 >= '$' && LA36_95 <= '\'')||LA36_95=='.'||(LA36_95 >= '0' && LA36_95 <= '9')||LA36_95=='<'||LA36_95=='>'||(LA36_95 >= '@' && LA36_95 <= 'Z')||(LA36_95 >= '_' && LA36_95 <= 'z')||LA36_95=='|'||(LA36_95 >= '\u007F' && LA36_95 <= '\u2FFF')||(LA36_95 >= '\u3001' && LA36_95 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_95=='\\') ) {s = 33;}
						else if ( (LA36_95=='+') ) {s = 34;}
						else if ( (LA36_95=='-') ) {s = 35;}
						else if ( (LA36_95=='=') ) {s = 42;}
						else if ( (LA36_95=='#') ) {s = 36;}
						else if ( (LA36_95=='/') ) {s = 37;}
						else if ( (LA36_95=='*'||LA36_95=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 65 : 
						int LA36_14 = input.LA(1);
						s = -1;
						if ( (LA36_14=='?') ) {s = 14;}
						else if ( ((LA36_14 >= '\u0000' && LA36_14 <= '\b')||(LA36_14 >= '\u000B' && LA36_14 <= '\f')||(LA36_14 >= '\u000E' && LA36_14 <= '\u001F')||(LA36_14 >= '#' && LA36_14 <= '\'')||LA36_14=='+'||(LA36_14 >= '-' && LA36_14 <= '9')||(LA36_14 >= '<' && LA36_14 <= '>')||(LA36_14 >= '@' && LA36_14 <= 'Z')||LA36_14=='\\'||(LA36_14 >= '_' && LA36_14 <= 'z')||LA36_14=='|'||(LA36_14 >= '\u007F' && LA36_14 <= '\u2FFF')||(LA36_14 >= '\u3001' && LA36_14 <= '\uFFFF')) ) {s = 39;}
						else s = 47;
						if ( s>=0 ) return s;
						break;

					case 66 : 
						int LA36_67 = input.LA(1);
						s = -1;
						if ( (LA36_67=='i') ) {s = 82;}
						else if ( (LA36_67=='(') ) {s = 38;}
						else if ( ((LA36_67 >= '\u0000' && LA36_67 <= '\b')||(LA36_67 >= '\u000B' && LA36_67 <= '\f')||(LA36_67 >= '\u000E' && LA36_67 <= '\u001F')||(LA36_67 >= '$' && LA36_67 <= '\'')||LA36_67=='.'||(LA36_67 >= '0' && LA36_67 <= '9')||LA36_67=='<'||LA36_67=='>'||(LA36_67 >= '@' && LA36_67 <= 'Z')||(LA36_67 >= '_' && LA36_67 <= 'h')||(LA36_67 >= 'j' && LA36_67 <= 'z')||LA36_67=='|'||(LA36_67 >= '\u007F' && LA36_67 <= '\u2FFF')||(LA36_67 >= '\u3001' && LA36_67 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_67=='\\') ) {s = 33;}
						else if ( (LA36_67=='+') ) {s = 34;}
						else if ( (LA36_67=='-') ) {s = 35;}
						else if ( (LA36_67=='=') ) {s = 42;}
						else if ( (LA36_67=='#') ) {s = 36;}
						else if ( (LA36_67=='/') ) {s = 37;}
						else if ( (LA36_67=='*'||LA36_67=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 67 : 
						int LA36_82 = input.LA(1);
						s = -1;
						if ( (LA36_82=='v') ) {s = 96;}
						else if ( (LA36_82=='(') ) {s = 38;}
						else if ( ((LA36_82 >= '\u0000' && LA36_82 <= '\b')||(LA36_82 >= '\u000B' && LA36_82 <= '\f')||(LA36_82 >= '\u000E' && LA36_82 <= '\u001F')||(LA36_82 >= '$' && LA36_82 <= '\'')||LA36_82=='.'||(LA36_82 >= '0' && LA36_82 <= '9')||LA36_82=='<'||LA36_82=='>'||(LA36_82 >= '@' && LA36_82 <= 'Z')||(LA36_82 >= '_' && LA36_82 <= 'u')||(LA36_82 >= 'w' && LA36_82 <= 'z')||LA36_82=='|'||(LA36_82 >= '\u007F' && LA36_82 <= '\u2FFF')||(LA36_82 >= '\u3001' && LA36_82 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_82=='\\') ) {s = 33;}
						else if ( (LA36_82=='+') ) {s = 34;}
						else if ( (LA36_82=='-') ) {s = 35;}
						else if ( (LA36_82=='=') ) {s = 42;}
						else if ( (LA36_82=='#') ) {s = 36;}
						else if ( (LA36_82=='/') ) {s = 37;}
						else if ( (LA36_82=='*'||LA36_82=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 68 : 
						int LA36_96 = input.LA(1);
						s = -1;
						if ( (LA36_96==':') ) {s = 104;}
						else if ( (LA36_96=='(') ) {s = 38;}
						else if ( ((LA36_96 >= '\u0000' && LA36_96 <= '\b')||(LA36_96 >= '\u000B' && LA36_96 <= '\f')||(LA36_96 >= '\u000E' && LA36_96 <= '\u001F')||(LA36_96 >= '$' && LA36_96 <= '\'')||LA36_96=='.'||(LA36_96 >= '0' && LA36_96 <= '9')||LA36_96=='<'||LA36_96=='>'||(LA36_96 >= '@' && LA36_96 <= 'Z')||(LA36_96 >= '_' && LA36_96 <= 'z')||LA36_96=='|'||(LA36_96 >= '\u007F' && LA36_96 <= '\u2FFF')||(LA36_96 >= '\u3001' && LA36_96 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_96=='\\') ) {s = 33;}
						else if ( (LA36_96=='+') ) {s = 34;}
						else if ( (LA36_96=='-') ) {s = 35;}
						else if ( (LA36_96=='=') ) {s = 42;}
						else if ( (LA36_96=='#') ) {s = 36;}
						else if ( (LA36_96=='/') ) {s = 37;}
						else if ( (LA36_96=='*'||LA36_96=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 69 : 
						int LA36_43 = input.LA(1);
						s = -1;
						if ( (LA36_43=='i') ) {s = 69;}
						else if ( (LA36_43=='(') ) {s = 38;}
						else if ( ((LA36_43 >= '\u0000' && LA36_43 <= '\b')||(LA36_43 >= '\u000B' && LA36_43 <= '\f')||(LA36_43 >= '\u000E' && LA36_43 <= '\u001F')||(LA36_43 >= '$' && LA36_43 <= '\'')||LA36_43=='.'||(LA36_43 >= '0' && LA36_43 <= '9')||LA36_43=='<'||LA36_43=='>'||(LA36_43 >= '@' && LA36_43 <= 'Z')||(LA36_43 >= '_' && LA36_43 <= 'h')||(LA36_43 >= 'j' && LA36_43 <= 'z')||LA36_43=='|'||(LA36_43 >= '\u007F' && LA36_43 <= '\u2FFF')||(LA36_43 >= '\u3001' && LA36_43 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_43=='\\') ) {s = 33;}
						else if ( (LA36_43=='+') ) {s = 34;}
						else if ( (LA36_43=='-') ) {s = 35;}
						else if ( (LA36_43=='=') ) {s = 42;}
						else if ( (LA36_43=='#') ) {s = 36;}
						else if ( (LA36_43=='/') ) {s = 37;}
						else if ( (LA36_43=='*'||LA36_43=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 70 : 
						int LA36_93 = input.LA(1);
						s = -1;
						if ( (LA36_93=='(') ) {s = 38;}
						else if ( ((LA36_93 >= '0' && LA36_93 <= '9')) ) {s = 102;}
						else if ( (LA36_93=='\\') ) {s = 33;}
						else if ( (LA36_93=='+') ) {s = 34;}
						else if ( (LA36_93=='-') ) {s = 35;}
						else if ( (LA36_93=='=') ) {s = 42;}
						else if ( (LA36_93=='#') ) {s = 36;}
						else if ( (LA36_93=='/') ) {s = 37;}
						else if ( ((LA36_93 >= '\u0000' && LA36_93 <= '\b')||(LA36_93 >= '\u000B' && LA36_93 <= '\f')||(LA36_93 >= '\u000E' && LA36_93 <= '\u001F')||(LA36_93 >= '$' && LA36_93 <= '\'')||LA36_93=='.'||LA36_93=='<'||LA36_93=='>'||(LA36_93 >= '@' && LA36_93 <= 'Z')||(LA36_93 >= '_' && LA36_93 <= 'z')||LA36_93=='|'||(LA36_93 >= '\u007F' && LA36_93 <= '\u2FFF')||(LA36_93 >= '\u3001' && LA36_93 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_93=='*'||LA36_93=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 71 : 
						int LA36_69 = input.LA(1);
						s = -1;
						if ( (LA36_69==':') ) {s = 84;}
						else if ( (LA36_69=='(') ) {s = 38;}
						else if ( ((LA36_69 >= '\u0000' && LA36_69 <= '\b')||(LA36_69 >= '\u000B' && LA36_69 <= '\f')||(LA36_69 >= '\u000E' && LA36_69 <= '\u001F')||(LA36_69 >= '$' && LA36_69 <= '\'')||LA36_69=='.'||(LA36_69 >= '0' && LA36_69 <= '9')||LA36_69=='<'||LA36_69=='>'||(LA36_69 >= '@' && LA36_69 <= 'Z')||(LA36_69 >= '_' && LA36_69 <= 'z')||LA36_69=='|'||(LA36_69 >= '\u007F' && LA36_69 <= '\u2FFF')||(LA36_69 >= '\u3001' && LA36_69 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_69=='\\') ) {s = 33;}
						else if ( (LA36_69=='+') ) {s = 34;}
						else if ( (LA36_69=='-') ) {s = 35;}
						else if ( (LA36_69=='=') ) {s = 42;}
						else if ( (LA36_69=='#') ) {s = 36;}
						else if ( (LA36_69=='/') ) {s = 37;}
						else if ( (LA36_69=='*'||LA36_69=='?') ) {s = 39;}
						else s = 31;
						if ( s>=0 ) return s;
						break;

					case 72 : 
						int LA36_65 = input.LA(1);
						s = -1;
						if ( (LA36_65=='(') ) {s = 38;}
						else if ( ((LA36_65 >= '\u0000' && LA36_65 <= '\b')||(LA36_65 >= '\u000B' && LA36_65 <= '\f')||(LA36_65 >= '\u000E' && LA36_65 <= '\u001F')||(LA36_65 >= '$' && LA36_65 <= '\'')||LA36_65=='.'||(LA36_65 >= '0' && LA36_65 <= '9')||LA36_65=='<'||LA36_65=='>'||(LA36_65 >= '@' && LA36_65 <= 'Z')||(LA36_65 >= '_' && LA36_65 <= 'z')||LA36_65=='|'||(LA36_65 >= '\u007F' && LA36_65 <= '\u2FFF')||(LA36_65 >= '\u3001' && LA36_65 <= '\uFFFF')) ) {s = 32;}
						else if ( (LA36_65=='\\') ) {s = 33;}
						else if ( (LA36_65=='+') ) {s = 34;}
						else if ( (LA36_65=='-') ) {s = 35;}
						else if ( (LA36_65=='=') ) {s = 42;}
						else if ( (LA36_65=='#') ) {s = 36;}
						else if ( (LA36_65=='/') ) {s = 37;}
						else if ( (LA36_65=='*'||LA36_65=='?') ) {s = 39;}
						else s = 31;
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
