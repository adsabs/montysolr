// $ANTLR 3.4 /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g 2012-05-10 12:28:25

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class ADSLexer extends Lexer {
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
    public String getGrammarFileName() { return "/dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g"; }

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:11:7: ( '#' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:11:9: '#'
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
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:12:7: ( '/' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:12:9: '/'
            {
            match('/'); 

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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:13:7: ( '<=>' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:13:9: '<=>'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:14:7: ( '=' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:14:9: '='
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:346:9: ( '(' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:346:11: '('
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:348:9: ( ')' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:348:11: ')'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:350:9: ( '[' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:350:11: '['
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:352:9: ( ']' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:352:11: ']'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:354:9: ( ':' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:354:11: ':'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:356:7: ( '+' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:356:9: '+'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:358:7: ( ( '-' | '–' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
            {
            if ( input.LA(1)=='-'||input.LA(1)=='\u2013' ) {
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:360:7: ( '*' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:360:9: '*'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:362:8: ( ( '?' )+ )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:362:10: ( '?' )+
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:362:10: ( '?' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='?') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:362:10: '?'
            	    {
            	    match('?'); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:369:7: ( '^' ( NUMBER )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:369:9: '^' ( NUMBER )?
            {
            match('^'); 

            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:369:13: ( NUMBER )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:369:13: NUMBER
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:371:7: ( '~' ( NUMBER )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:371:9: '~' ( NUMBER )?
            {
            match('~'); 

            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:371:13: ( NUMBER )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:371:13: NUMBER
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:373:8: ( '\\\"' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:373:10: '\\\"'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:375:8: ( '\\'' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:375:10: '\\''
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

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:377:7: ( ',' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:377:9: ','
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:379:10: ( ';' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:379:12: ';'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:383:2: (~ ( '0' .. '9' | ' ' | COMMA | PLUS | MINUS | '$' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
            {
            if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\u001F')||(input.LA(1) >= '!' && input.LA(1) <= '#')||(input.LA(1) >= '%' && input.LA(1) <= '*')||(input.LA(1) >= '.' && input.LA(1) <= '/')||(input.LA(1) >= ':' && input.LA(1) <= '\u2012')||(input.LA(1) >= '\u2014' && input.LA(1) <= '\uFFFF') ) {
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:388:18: ( '\\\\' . )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:388:21: '\\\\' .
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:390:4: ( 'TO' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:390:6: 'TO'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:393:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:393:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:393:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:393:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:394:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:394:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:394:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:394:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:395:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:395:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:396:7: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:396:9: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:402:2: ( '^' ( AS_CHAR )+ ( ',' ( ' ' | AS_CHAR )+ )* ( '$' )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:403:2: '^' ( AS_CHAR )+ ( ',' ( ' ' | AS_CHAR )+ )* ( '$' )?
            {
            match('^'); 

            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:403:6: ( AS_CHAR )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '\u0000' && LA4_0 <= '\u001F')||(LA4_0 >= '!' && LA4_0 <= '#')||(LA4_0 >= '%' && LA4_0 <= '*')||(LA4_0 >= '.' && LA4_0 <= '/')||(LA4_0 >= ':' && LA4_0 <= '\u2012')||(LA4_0 >= '\u2014' && LA4_0 <= '\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\u001F')||(input.LA(1) >= '!' && input.LA(1) <= '#')||(input.LA(1) >= '%' && input.LA(1) <= '*')||(input.LA(1) >= '.' && input.LA(1) <= '/')||(input.LA(1) >= ':' && input.LA(1) <= '\u2012')||(input.LA(1) >= '\u2014' && input.LA(1) <= '\uFFFF') ) {
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
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:403:15: ( ',' ( ' ' | AS_CHAR )+ )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==',') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:403:16: ',' ( ' ' | AS_CHAR )+
            	    {
            	    match(','); 

            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:403:20: ( ' ' | AS_CHAR )+
            	    int cnt5=0;
            	    loop5:
            	    do {
            	        int alt5=2;
            	        int LA5_0 = input.LA(1);

            	        if ( ((LA5_0 >= '\u0000' && LA5_0 <= '#')||(LA5_0 >= '%' && LA5_0 <= '*')||(LA5_0 >= '.' && LA5_0 <= '/')||(LA5_0 >= ':' && LA5_0 <= '\u2012')||(LA5_0 >= '\u2014' && LA5_0 <= '\uFFFF')) ) {
            	            alt5=1;
            	        }


            	        switch (alt5) {
            	    	case 1 :
            	    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
            	    	    {
            	    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '#')||(input.LA(1) >= '%' && input.LA(1) <= '*')||(input.LA(1) >= '.' && input.LA(1) <= '/')||(input.LA(1) >= ':' && input.LA(1) <= '\u2012')||(input.LA(1) >= '\u2014' && input.LA(1) <= '\uFFFF') ) {
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
            	                EarlyExitException eee =
            	                    new EarlyExitException(5, input);
            	                throw eee;
            	        }
            	        cnt5++;
            	    } while (true);


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:403:39: ( '$' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='$') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:403:39: '$'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:437:2: ( '-' INT INT INT INT | INT INT INT INT '-' ( INT INT INT INT )? )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='-') ) {
                alt9=1;
            }
            else if ( ((LA9_0 >= '0' && LA9_0 <= '9')) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:438:2: '-' INT INT INT INT
                    {
                    match('-'); 

                    mINT(); 


                    mINT(); 


                    mINT(); 


                    mINT(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:439:4: INT INT INT INT '-' ( INT INT INT INT )?
                    {
                    mINT(); 


                    mINT(); 


                    mINT(); 


                    mINT(); 


                    match('-'); 

                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:439:24: ( INT INT INT INT )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:439:25: INT INT INT INT
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

    // $ANTLR start "IDENTIFIER"
    public final void mIDENTIFIER() throws RecognitionException {
        try {
            int _type = IDENTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:443:2: ( ( 'arXiv' | 'arxiv' ) ':' ( TERM_CHAR )+ | 'doi:' ( TERM_CHAR )+ )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='a') ) {
                alt13=1;
            }
            else if ( (LA13_0=='d') ) {
                alt13=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }
            switch (alt13) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:443:4: ( 'arXiv' | 'arxiv' ) ':' ( TERM_CHAR )+
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:443:4: ( 'arXiv' | 'arxiv' )
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0=='a') ) {
                        int LA10_1 = input.LA(2);

                        if ( (LA10_1=='r') ) {
                            int LA10_2 = input.LA(3);

                            if ( (LA10_2=='X') ) {
                                alt10=1;
                            }
                            else if ( (LA10_2=='x') ) {
                                alt10=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 10, 2, input);

                                throw nvae;

                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 10, 1, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 10, 0, input);

                        throw nvae;

                    }
                    switch (alt10) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:443:5: 'arXiv'
                            {
                            match("arXiv"); 



                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:443:13: 'arxiv'
                            {
                            match("arxiv"); 



                            }
                            break;

                    }


                    match(':'); 

                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:443:26: ( TERM_CHAR )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0 >= '\u0000' && LA11_0 <= '\b')||(LA11_0 >= '\u000B' && LA11_0 <= '\f')||(LA11_0 >= '\u000E' && LA11_0 <= '\u001F')||(LA11_0 >= '#' && LA11_0 <= '&')||LA11_0=='+'||(LA11_0 >= '-' && LA11_0 <= '9')||(LA11_0 >= '<' && LA11_0 <= '>')||(LA11_0 >= '@' && LA11_0 <= 'Z')||LA11_0=='\\'||(LA11_0 >= '_' && LA11_0 <= 'z')||LA11_0=='|'||(LA11_0 >= '\u007F' && LA11_0 <= '\u2FFF')||(LA11_0 >= '\u3001' && LA11_0 <= '\uFFFF')) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:443:26: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:444:3: 'doi:' ( TERM_CHAR )+
                    {
                    match("doi:"); 



                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:444:10: ( TERM_CHAR )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0 >= '\u0000' && LA12_0 <= '\b')||(LA12_0 >= '\u000B' && LA12_0 <= '\f')||(LA12_0 >= '\u000E' && LA12_0 <= '\u001F')||(LA12_0 >= '#' && LA12_0 <= '&')||LA12_0=='+'||(LA12_0 >= '-' && LA12_0 <= '9')||(LA12_0 >= '<' && LA12_0 <= '>')||(LA12_0 >= '@' && LA12_0 <= 'Z')||LA12_0=='\\'||(LA12_0 >= '_' && LA12_0 <= 'z')||LA12_0=='|'||(LA12_0 >= '\u007F' && LA12_0 <= '\u2FFF')||(LA12_0 >= '\u3001' && LA12_0 <= '\uFFFF')) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:444:10: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt12 >= 1 ) break loop12;
                                EarlyExitException eee =
                                    new EarlyExitException(12, input);
                                throw eee;
                        }
                        cnt12++;
                    } while (true);


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
    // $ANTLR end "IDENTIFIER"

    // $ANTLR start "FUNC_NAME"
    public final void mFUNC_NAME() throws RecognitionException {
        try {
            int _type = FUNC_NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:451:2: ( TERM_NORMAL '(' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:452:2: TERM_NORMAL '('
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:456:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:456:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:465:13: ( '0' .. '9' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:470:2: ( (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | '–' | ';' ) | ESC_CHAR ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:471:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | '–' | ';' ) | ESC_CHAR )
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:471:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | '–' | ';' ) | ESC_CHAR )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( ((LA14_0 >= '\u0000' && LA14_0 <= '\b')||(LA14_0 >= '\u000B' && LA14_0 <= '\f')||(LA14_0 >= '\u000E' && LA14_0 <= '\u001F')||(LA14_0 >= '$' && LA14_0 <= '&')||(LA14_0 >= '.' && LA14_0 <= '9')||LA14_0=='<'||LA14_0=='>'||(LA14_0 >= '@' && LA14_0 <= 'Z')||(LA14_0 >= '_' && LA14_0 <= 'z')||LA14_0=='|'||(LA14_0 >= '\u007F' && LA14_0 <= '\u2012')||(LA14_0 >= '\u2014' && LA14_0 <= '\u2FFF')||(LA14_0 >= '\u3001' && LA14_0 <= '\uFFFF')) ) {
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:471:3: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | '–' | ';' )
                    {
                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\u001F')||(input.LA(1) >= '$' && input.LA(1) <= '&')||(input.LA(1) >= '.' && input.LA(1) <= '9')||input.LA(1)=='<'||input.LA(1)=='>'||(input.LA(1) >= '@' && input.LA(1) <= 'Z')||(input.LA(1) >= '_' && input.LA(1) <= 'z')||input.LA(1)=='|'||(input.LA(1) >= '\u007F' && input.LA(1) <= '\u2012')||(input.LA(1) >= '\u2014' && input.LA(1) <= '\u2FFF')||(input.LA(1) >= '\u3001' && input.LA(1) <= '\uFFFF') ) {
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:478:5: ESC_CHAR
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:482:2: ( ( TERM_START_CHAR | '+' | '-' | '–' | '=' | '#' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:483:2: ( TERM_START_CHAR | '+' | '-' | '–' | '=' | '#' )
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:483:2: ( TERM_START_CHAR | '+' | '-' | '–' | '=' | '#' )
            int alt15=6;
            int LA15_0 = input.LA(1);

            if ( ((LA15_0 >= '\u0000' && LA15_0 <= '\b')||(LA15_0 >= '\u000B' && LA15_0 <= '\f')||(LA15_0 >= '\u000E' && LA15_0 <= '\u001F')||(LA15_0 >= '$' && LA15_0 <= '&')||(LA15_0 >= '.' && LA15_0 <= '9')||LA15_0=='<'||LA15_0=='>'||(LA15_0 >= '@' && LA15_0 <= 'Z')||LA15_0=='\\'||(LA15_0 >= '_' && LA15_0 <= 'z')||LA15_0=='|'||(LA15_0 >= '\u007F' && LA15_0 <= '\u2012')||(LA15_0 >= '\u2014' && LA15_0 <= '\u2FFF')||(LA15_0 >= '\u3001' && LA15_0 <= '\uFFFF')) ) {
                alt15=1;
            }
            else if ( (LA15_0=='+') ) {
                alt15=2;
            }
            else if ( (LA15_0=='-') ) {
                alt15=3;
            }
            else if ( (LA15_0=='\u2013') ) {
                alt15=4;
            }
            else if ( (LA15_0=='=') ) {
                alt15=5;
            }
            else if ( (LA15_0=='#') ) {
                alt15=6;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }
            switch (alt15) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:483:3: TERM_START_CHAR
                    {
                    mTERM_START_CHAR(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:483:22: '+'
                    {
                    match('+'); 

                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:483:28: '-'
                    {
                    match('-'); 

                    }
                    break;
                case 4 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:483:34: '–'
                    {
                    match('\u2013'); 

                    }
                    break;
                case 5 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:483:40: '='
                    {
                    match('='); 

                    }
                    break;
                case 6 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:483:46: '#'
                    {
                    match('#'); 

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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:489:2: ( INT ( INT )? ( '/' | MINUS | '.' ) INT ( INT )? ( '/' | MINUS | '.' ) INT INT ( INT INT )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:490:2: INT ( INT )? ( '/' | MINUS | '.' ) INT ( INT )? ( '/' | MINUS | '.' ) INT INT ( INT INT )?
            {
            mINT(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:490:6: ( INT )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( ((LA16_0 >= '0' && LA16_0 <= '9')) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
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


            if ( (input.LA(1) >= '-' && input.LA(1) <= '/')||input.LA(1)=='\u2013' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            mINT(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:490:31: ( INT )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( ((LA17_0 >= '0' && LA17_0 <= '9')) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
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


            if ( (input.LA(1) >= '-' && input.LA(1) <= '/')||input.LA(1)=='\u2013' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            mINT(); 


            mINT(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:490:60: ( INT INT )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( ((LA18_0 >= '0' && LA18_0 <= '9')) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:490:61: INT INT
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:494:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:495:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:495:2: ( INT )+
            int cnt19=0;
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0 >= '0' && LA19_0 <= '9')) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
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
            	    if ( cnt19 >= 1 ) break loop19;
                        EarlyExitException eee =
                            new EarlyExitException(19, input);
                        throw eee;
                }
                cnt19++;
            } while (true);


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:495:7: ( '.' ( INT )+ )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0=='.') ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:495:8: '.' ( INT )+
                    {
                    match('.'); 

                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:495:12: ( INT )+
                    int cnt20=0;
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( ((LA20_0 >= '0' && LA20_0 <= '9')) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:
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
                    	    if ( cnt20 >= 1 ) break loop20;
                                EarlyExitException eee =
                                    new EarlyExitException(20, input);
                                throw eee;
                        }
                        cnt20++;
                    } while (true);


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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:498:18: ( NUMBER 'm' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:499:2: NUMBER 'm'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:501:18: ( NUMBER 'h' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:502:2: NUMBER 'h'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:504:18: ( NUMBER 'd' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:505:2: NUMBER 'd'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:507:18: ( NUMBER 's' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:508:2: NUMBER 's'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:511:2: ( INT INT COLON INT INT COLON NUMBER ( PLUS | MINUS ) INT INT COLON INT INT COLON NUMBER )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:512:2: INT INT COLON INT INT COLON NUMBER ( PLUS | MINUS ) INT INT COLON INT INT COLON NUMBER
            {
            mINT(); 


            mINT(); 


            mCOLON(); 


            mINT(); 


            mINT(); 


            mCOLON(); 


            mNUMBER(); 


            if ( input.LA(1)=='+'||input.LA(1)=='-'||input.LA(1)=='\u2013' ) {
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:516:2: ( TERM_START_CHAR ( TERM_CHAR )* )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:517:2: TERM_START_CHAR ( TERM_CHAR )*
            {
            mTERM_START_CHAR(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:517:18: ( TERM_CHAR )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( ((LA22_0 >= '\u0000' && LA22_0 <= '\b')||(LA22_0 >= '\u000B' && LA22_0 <= '\f')||(LA22_0 >= '\u000E' && LA22_0 <= '\u001F')||(LA22_0 >= '#' && LA22_0 <= '&')||LA22_0=='+'||(LA22_0 >= '-' && LA22_0 <= '9')||(LA22_0 >= '<' && LA22_0 <= '>')||(LA22_0 >= '@' && LA22_0 <= 'Z')||LA22_0=='\\'||(LA22_0 >= '_' && LA22_0 <= 'z')||LA22_0=='|'||(LA22_0 >= '\u007F' && LA22_0 <= '\u2FFF')||(LA22_0 >= '\u3001' && LA22_0 <= '\uFFFF')) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:517:20: TERM_CHAR
            	    {
            	    mTERM_CHAR(); 


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:521:15: ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ )
            int alt34=3;
            alt34 = dfa34.predict(input);
            switch (alt34) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:2: ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )*
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:2: ( STAR | QMARK )
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
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:3: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:8: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+
                    int cnt26=0;
                    loop26:
                    do {
                        int alt26=2;
                        alt26 = dfa26.predict(input);
                        switch (alt26) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:16: ( TERM_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:16: ( TERM_CHAR )+
                    	    int cnt24=0;
                    	    loop24:
                    	    do {
                    	        int alt24=2;
                    	        int LA24_0 = input.LA(1);

                    	        if ( ((LA24_0 >= '\u0000' && LA24_0 <= '\b')||(LA24_0 >= '\u000B' && LA24_0 <= '\f')||(LA24_0 >= '\u000E' && LA24_0 <= '\u001F')||(LA24_0 >= '#' && LA24_0 <= '&')||LA24_0=='+'||(LA24_0 >= '-' && LA24_0 <= '9')||(LA24_0 >= '<' && LA24_0 <= '>')||(LA24_0 >= '@' && LA24_0 <= 'Z')||LA24_0=='\\'||(LA24_0 >= '_' && LA24_0 <= 'z')||LA24_0=='|'||(LA24_0 >= '\u007F' && LA24_0 <= '\u2FFF')||(LA24_0 >= '\u3001' && LA24_0 <= '\uFFFF')) ) {
                    	            alt24=1;
                    	        }


                    	        switch (alt24) {
                    	    	case 1 :
                    	    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:16: TERM_CHAR
                    	    	    {
                    	    	    mTERM_CHAR(); 


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    if ( cnt24 >= 1 ) break loop24;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(24, input);
                    	                throw eee;
                    	        }
                    	        cnt24++;
                    	    } while (true);


                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:27: ( QMARK | STAR )
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
                    	            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:28: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:34: STAR
                    	            {
                    	            mSTAR(); 


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt26 >= 1 ) break loop26;
                                EarlyExitException eee =
                                    new EarlyExitException(26, input);
                                throw eee;
                        }
                        cnt26++;
                    } while (true);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:42: ( TERM_CHAR )*
                    loop27:
                    do {
                        int alt27=2;
                        int LA27_0 = input.LA(1);

                        if ( ((LA27_0 >= '\u0000' && LA27_0 <= '\b')||(LA27_0 >= '\u000B' && LA27_0 <= '\f')||(LA27_0 >= '\u000E' && LA27_0 <= '\u001F')||(LA27_0 >= '#' && LA27_0 <= '&')||LA27_0=='+'||(LA27_0 >= '-' && LA27_0 <= '9')||(LA27_0 >= '<' && LA27_0 <= '>')||(LA27_0 >= '@' && LA27_0 <= 'Z')||LA27_0=='\\'||(LA27_0 >= '_' && LA27_0 <= 'z')||LA27_0=='|'||(LA27_0 >= '\u007F' && LA27_0 <= '\u2FFF')||(LA27_0 >= '\u3001' && LA27_0 <= '\uFFFF')) ) {
                            alt27=1;
                        }


                        switch (alt27) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:43: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop27;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:4: TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )*
                    {
                    mTERM_START_CHAR(); 


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+
                    int cnt30=0;
                    loop30:
                    do {
                        int alt30=2;
                        alt30 = dfa30.predict(input);
                        switch (alt30) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:21: ( TERM_CHAR )* ( QMARK | STAR )
                    	    {
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:21: ( TERM_CHAR )*
                    	    loop28:
                    	    do {
                    	        int alt28=2;
                    	        int LA28_0 = input.LA(1);

                    	        if ( ((LA28_0 >= '\u0000' && LA28_0 <= '\b')||(LA28_0 >= '\u000B' && LA28_0 <= '\f')||(LA28_0 >= '\u000E' && LA28_0 <= '\u001F')||(LA28_0 >= '#' && LA28_0 <= '&')||LA28_0=='+'||(LA28_0 >= '-' && LA28_0 <= '9')||(LA28_0 >= '<' && LA28_0 <= '>')||(LA28_0 >= '@' && LA28_0 <= 'Z')||LA28_0=='\\'||(LA28_0 >= '_' && LA28_0 <= 'z')||LA28_0=='|'||(LA28_0 >= '\u007F' && LA28_0 <= '\u2FFF')||(LA28_0 >= '\u3001' && LA28_0 <= '\uFFFF')) ) {
                    	            alt28=1;
                    	        }


                    	        switch (alt28) {
                    	    	case 1 :
                    	    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:21: TERM_CHAR
                    	    	    {
                    	    	    mTERM_CHAR(); 


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    break loop28;
                    	        }
                    	    } while (true);


                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:32: ( QMARK | STAR )
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
                    	            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:33: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:39: STAR
                    	            {
                    	            mSTAR(); 


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt30 >= 1 ) break loop30;
                                EarlyExitException eee =
                                    new EarlyExitException(30, input);
                                throw eee;
                        }
                        cnt30++;
                    } while (true);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:47: ( TERM_CHAR )*
                    loop31:
                    do {
                        int alt31=2;
                        int LA31_0 = input.LA(1);

                        if ( ((LA31_0 >= '\u0000' && LA31_0 <= '\b')||(LA31_0 >= '\u000B' && LA31_0 <= '\f')||(LA31_0 >= '\u000E' && LA31_0 <= '\u001F')||(LA31_0 >= '#' && LA31_0 <= '&')||LA31_0=='+'||(LA31_0 >= '-' && LA31_0 <= '9')||(LA31_0 >= '<' && LA31_0 <= '>')||(LA31_0 >= '@' && LA31_0 <= 'Z')||LA31_0=='\\'||(LA31_0 >= '_' && LA31_0 <= 'z')||LA31_0=='|'||(LA31_0 >= '\u007F' && LA31_0 <= '\u2FFF')||(LA31_0 >= '\u3001' && LA31_0 <= '\uFFFF')) ) {
                            alt31=1;
                        }


                        switch (alt31) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:48: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop31;
                        }
                    } while (true);


                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:524:4: ( STAR | QMARK ) ( TERM_CHAR )+
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:524:4: ( STAR | QMARK )
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
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:524:5: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:524:10: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:524:17: ( TERM_CHAR )+
                    int cnt33=0;
                    loop33:
                    do {
                        int alt33=2;
                        int LA33_0 = input.LA(1);

                        if ( ((LA33_0 >= '\u0000' && LA33_0 <= '\b')||(LA33_0 >= '\u000B' && LA33_0 <= '\f')||(LA33_0 >= '\u000E' && LA33_0 <= '\u001F')||(LA33_0 >= '#' && LA33_0 <= '&')||LA33_0=='+'||(LA33_0 >= '-' && LA33_0 <= '9')||(LA33_0 >= '<' && LA33_0 <= '>')||(LA33_0 >= '@' && LA33_0 <= 'Z')||LA33_0=='\\'||(LA33_0 >= '_' && LA33_0 <= 'z')||LA33_0=='|'||(LA33_0 >= '\u007F' && LA33_0 <= '\u2FFF')||(LA33_0 >= '\u3001' && LA33_0 <= '\uFFFF')) ) {
                            alt33=1;
                        }


                        switch (alt33) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:524:17: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt33 >= 1 ) break loop33;
                                EarlyExitException eee =
                                    new EarlyExitException(33, input);
                                throw eee;
                        }
                        cnt33++;
                    } while (true);


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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:529:2: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:530:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:530:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+
            int cnt35=0;
            loop35:
            do {
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
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:530:10: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:530:19: ~ ( '\\\"' | '\\\\' | '?' | '*' )
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
                        EarlyExitException eee =
                            new EarlyExitException(35, input);
                        throw eee;
                }
                cnt35++;
            } while (true);


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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:17: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+
            int cnt36=0;
            loop36:
            do {
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
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:10: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:19: ~ ( '\\\"' | '\\\\' )
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
                        EarlyExitException eee =
                            new EarlyExitException(36, input);
                        throw eee;
                }
                cnt36++;
            } while (true);


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

    public void mTokens() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:8: ( T__68 | T__69 | T__70 | T__71 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | CARAT | TILDE | DQUOTE | SQUOTE | COMMA | SEMICOLON | TO | AND | OR | NOT | NEAR | AUTHOR_SEARCH | DATE_RANGE | IDENTIFIER | FUNC_NAME | WS | DATE_TOKEN | NUMBER | HOUR | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING )
        int alt37=36;
        alt37 = dfa37.predict(input);
        switch (alt37) {
            case 1 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:10: T__68
                {
                mT__68(); 


                }
                break;
            case 2 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:16: T__69
                {
                mT__69(); 


                }
                break;
            case 3 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:22: T__70
                {
                mT__70(); 


                }
                break;
            case 4 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:28: T__71
                {
                mT__71(); 


                }
                break;
            case 5 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:34: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 6 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:41: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 7 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:48: LBRACK
                {
                mLBRACK(); 


                }
                break;
            case 8 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:55: RBRACK
                {
                mRBRACK(); 


                }
                break;
            case 9 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:62: COLON
                {
                mCOLON(); 


                }
                break;
            case 10 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:68: PLUS
                {
                mPLUS(); 


                }
                break;
            case 11 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:73: MINUS
                {
                mMINUS(); 


                }
                break;
            case 12 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:79: STAR
                {
                mSTAR(); 


                }
                break;
            case 13 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:84: QMARK
                {
                mQMARK(); 


                }
                break;
            case 14 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:90: CARAT
                {
                mCARAT(); 


                }
                break;
            case 15 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:96: TILDE
                {
                mTILDE(); 


                }
                break;
            case 16 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:102: DQUOTE
                {
                mDQUOTE(); 


                }
                break;
            case 17 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:109: SQUOTE
                {
                mSQUOTE(); 


                }
                break;
            case 18 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:116: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 19 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:122: SEMICOLON
                {
                mSEMICOLON(); 


                }
                break;
            case 20 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:132: TO
                {
                mTO(); 


                }
                break;
            case 21 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:135: AND
                {
                mAND(); 


                }
                break;
            case 22 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:139: OR
                {
                mOR(); 


                }
                break;
            case 23 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:142: NOT
                {
                mNOT(); 


                }
                break;
            case 24 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:146: NEAR
                {
                mNEAR(); 


                }
                break;
            case 25 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:151: AUTHOR_SEARCH
                {
                mAUTHOR_SEARCH(); 


                }
                break;
            case 26 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:165: DATE_RANGE
                {
                mDATE_RANGE(); 


                }
                break;
            case 27 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:176: IDENTIFIER
                {
                mIDENTIFIER(); 


                }
                break;
            case 28 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:187: FUNC_NAME
                {
                mFUNC_NAME(); 


                }
                break;
            case 29 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:197: WS
                {
                mWS(); 


                }
                break;
            case 30 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:200: DATE_TOKEN
                {
                mDATE_TOKEN(); 


                }
                break;
            case 31 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:211: NUMBER
                {
                mNUMBER(); 


                }
                break;
            case 32 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:218: HOUR
                {
                mHOUR(); 


                }
                break;
            case 33 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:223: TERM_NORMAL
                {
                mTERM_NORMAL(); 


                }
                break;
            case 34 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:235: TERM_TRUNCATED
                {
                mTERM_TRUNCATED(); 


                }
                break;
            case 35 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:250: PHRASE
                {
                mPHRASE(); 


                }
                break;
            case 36 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:257: PHRASE_ANYTHING
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
        "\4\uffff\1\13\1\uffff\5\13\2\uffff\1\13";
    static final String DFA34_eofS =
        "\16\uffff";
    static final String DFA34_minS =
        "\3\0\1\uffff\7\0\2\uffff\1\0";
    static final String DFA34_maxS =
        "\3\uffff\1\uffff\7\uffff\2\uffff\1\uffff";
    static final String DFA34_acceptS =
        "\3\uffff\1\2\7\uffff\1\3\1\1\1\uffff";
    static final String DFA34_specialS =
        "\1\2\1\7\1\0\1\uffff\1\3\1\5\1\1\1\6\1\4\1\12\1\11\2\uffff\1\10}>";
    static final String[] DFA34_transitionS = {
            "\11\3\2\uffff\2\3\1\uffff\22\3\4\uffff\3\3\3\uffff\1\1\3\uffff"+
            "\14\3\2\uffff\1\3\1\uffff\1\3\1\2\33\3\1\uffff\1\3\2\uffff\34"+
            "\3\1\uffff\1\3\2\uffff\u1f94\3\1\uffff\u0fec\3\1\uffff\ucfff"+
            "\3",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\12\3\4\4\uffff\1\6"+
            "\1\uffff\1\7\14\4\2\uffff\1\4\1\11\1\4\1\uffff\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff\u1f94\4\1\10\u0fec\4\1"+
            "\uffff\ucfff\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\12\3\4\4\uffff\1\6"+
            "\1\uffff\1\7\14\4\2\uffff\1\4\1\11\1\4\1\2\33\4\1\uffff\1\5"+
            "\2\uffff\34\4\1\uffff\1\4\2\uffff\u1f94\4\1\10\u0fec\4\1\uffff"+
            "\ucfff\4",
            "",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\12\3\4\3\uffff\1\14"+
            "\1\6\1\uffff\1\7\14\4\2\uffff\1\4\1\11\1\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff\u1f94\4\1\10\u0fec\4\1"+
            "\uffff\ucfff\4",
            "\0\15",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\12\3\4\3\uffff\1\14"+
            "\1\6\1\uffff\1\7\14\4\2\uffff\1\4\1\11\1\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff\u1f94\4\1\10\u0fec\4\1"+
            "\uffff\ucfff\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\12\3\4\3\uffff\1\14"+
            "\1\6\1\uffff\1\7\14\4\2\uffff\1\4\1\11\1\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff\u1f94\4\1\10\u0fec\4\1"+
            "\uffff\ucfff\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\12\3\4\3\uffff\1\14"+
            "\1\6\1\uffff\1\7\14\4\2\uffff\1\4\1\11\1\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff\u1f94\4\1\10\u0fec\4\1"+
            "\uffff\ucfff\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\12\3\4\3\uffff\1\14"+
            "\1\6\1\uffff\1\7\14\4\2\uffff\1\4\1\11\1\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff\u1f94\4\1\10\u0fec\4\1"+
            "\uffff\ucfff\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\12\3\4\3\uffff\1\14"+
            "\1\6\1\uffff\1\7\14\4\2\uffff\1\4\1\11\1\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff\u1f94\4\1\10\u0fec\4\1"+
            "\uffff\ucfff\4",
            "",
            "",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\12\3\4\3\uffff\1\14"+
            "\1\6\1\uffff\1\7\14\4\2\uffff\1\4\1\11\1\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\1\uffff\1\4\2\uffff\u1f94\4\1\10\u0fec\4\1"+
            "\uffff\ucfff\4"
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

    class DFA34 extends DFA {

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
        public String getDescription() {
            return "521:1: TERM_TRUNCATED : ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA34_2 = input.LA(1);

                        s = -1;
                        if ( ((LA34_2 >= '\u0000' && LA34_2 <= '\b')||(LA34_2 >= '\u000B' && LA34_2 <= '\f')||(LA34_2 >= '\u000E' && LA34_2 <= '\u001F')||(LA34_2 >= '$' && LA34_2 <= '&')||(LA34_2 >= '.' && LA34_2 <= '9')||LA34_2=='<'||LA34_2=='>'||(LA34_2 >= '@' && LA34_2 <= 'Z')||(LA34_2 >= '_' && LA34_2 <= 'z')||LA34_2=='|'||(LA34_2 >= '\u007F' && LA34_2 <= '\u2012')||(LA34_2 >= '\u2014' && LA34_2 <= '\u2FFF')||(LA34_2 >= '\u3001' && LA34_2 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA34_2=='\\') ) {s = 5;}

                        else if ( (LA34_2=='+') ) {s = 6;}

                        else if ( (LA34_2=='-') ) {s = 7;}

                        else if ( (LA34_2=='\u2013') ) {s = 8;}

                        else if ( (LA34_2=='=') ) {s = 9;}

                        else if ( (LA34_2=='#') ) {s = 10;}

                        else if ( (LA34_2=='?') ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA34_6 = input.LA(1);

                        s = -1;
                        if ( (LA34_6=='*'||LA34_6=='?') ) {s = 12;}

                        else if ( ((LA34_6 >= '\u0000' && LA34_6 <= '\b')||(LA34_6 >= '\u000B' && LA34_6 <= '\f')||(LA34_6 >= '\u000E' && LA34_6 <= '\u001F')||(LA34_6 >= '$' && LA34_6 <= '&')||(LA34_6 >= '.' && LA34_6 <= '9')||LA34_6=='<'||LA34_6=='>'||(LA34_6 >= '@' && LA34_6 <= 'Z')||(LA34_6 >= '_' && LA34_6 <= 'z')||LA34_6=='|'||(LA34_6 >= '\u007F' && LA34_6 <= '\u2012')||(LA34_6 >= '\u2014' && LA34_6 <= '\u2FFF')||(LA34_6 >= '\u3001' && LA34_6 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA34_6=='\\') ) {s = 5;}

                        else if ( (LA34_6=='+') ) {s = 6;}

                        else if ( (LA34_6=='-') ) {s = 7;}

                        else if ( (LA34_6=='\u2013') ) {s = 8;}

                        else if ( (LA34_6=='=') ) {s = 9;}

                        else if ( (LA34_6=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA34_0 = input.LA(1);

                        s = -1;
                        if ( (LA34_0=='*') ) {s = 1;}

                        else if ( (LA34_0=='?') ) {s = 2;}

                        else if ( ((LA34_0 >= '\u0000' && LA34_0 <= '\b')||(LA34_0 >= '\u000B' && LA34_0 <= '\f')||(LA34_0 >= '\u000E' && LA34_0 <= '\u001F')||(LA34_0 >= '$' && LA34_0 <= '&')||(LA34_0 >= '.' && LA34_0 <= '9')||LA34_0=='<'||LA34_0=='>'||(LA34_0 >= '@' && LA34_0 <= 'Z')||LA34_0=='\\'||(LA34_0 >= '_' && LA34_0 <= 'z')||LA34_0=='|'||(LA34_0 >= '\u007F' && LA34_0 <= '\u2012')||(LA34_0 >= '\u2014' && LA34_0 <= '\u2FFF')||(LA34_0 >= '\u3001' && LA34_0 <= '\uFFFF')) ) {s = 3;}

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA34_4 = input.LA(1);

                        s = -1;
                        if ( (LA34_4=='*'||LA34_4=='?') ) {s = 12;}

                        else if ( ((LA34_4 >= '\u0000' && LA34_4 <= '\b')||(LA34_4 >= '\u000B' && LA34_4 <= '\f')||(LA34_4 >= '\u000E' && LA34_4 <= '\u001F')||(LA34_4 >= '$' && LA34_4 <= '&')||(LA34_4 >= '.' && LA34_4 <= '9')||LA34_4=='<'||LA34_4=='>'||(LA34_4 >= '@' && LA34_4 <= 'Z')||(LA34_4 >= '_' && LA34_4 <= 'z')||LA34_4=='|'||(LA34_4 >= '\u007F' && LA34_4 <= '\u2012')||(LA34_4 >= '\u2014' && LA34_4 <= '\u2FFF')||(LA34_4 >= '\u3001' && LA34_4 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA34_4=='\\') ) {s = 5;}

                        else if ( (LA34_4=='+') ) {s = 6;}

                        else if ( (LA34_4=='-') ) {s = 7;}

                        else if ( (LA34_4=='\u2013') ) {s = 8;}

                        else if ( (LA34_4=='=') ) {s = 9;}

                        else if ( (LA34_4=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA34_8 = input.LA(1);

                        s = -1;
                        if ( (LA34_8=='*'||LA34_8=='?') ) {s = 12;}

                        else if ( ((LA34_8 >= '\u0000' && LA34_8 <= '\b')||(LA34_8 >= '\u000B' && LA34_8 <= '\f')||(LA34_8 >= '\u000E' && LA34_8 <= '\u001F')||(LA34_8 >= '$' && LA34_8 <= '&')||(LA34_8 >= '.' && LA34_8 <= '9')||LA34_8=='<'||LA34_8=='>'||(LA34_8 >= '@' && LA34_8 <= 'Z')||(LA34_8 >= '_' && LA34_8 <= 'z')||LA34_8=='|'||(LA34_8 >= '\u007F' && LA34_8 <= '\u2012')||(LA34_8 >= '\u2014' && LA34_8 <= '\u2FFF')||(LA34_8 >= '\u3001' && LA34_8 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA34_8=='\\') ) {s = 5;}

                        else if ( (LA34_8=='+') ) {s = 6;}

                        else if ( (LA34_8=='-') ) {s = 7;}

                        else if ( (LA34_8=='\u2013') ) {s = 8;}

                        else if ( (LA34_8=='=') ) {s = 9;}

                        else if ( (LA34_8=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA34_5 = input.LA(1);

                        s = -1;
                        if ( ((LA34_5 >= '\u0000' && LA34_5 <= '\uFFFF')) ) {s = 13;}

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA34_7 = input.LA(1);

                        s = -1;
                        if ( (LA34_7=='*'||LA34_7=='?') ) {s = 12;}

                        else if ( ((LA34_7 >= '\u0000' && LA34_7 <= '\b')||(LA34_7 >= '\u000B' && LA34_7 <= '\f')||(LA34_7 >= '\u000E' && LA34_7 <= '\u001F')||(LA34_7 >= '$' && LA34_7 <= '&')||(LA34_7 >= '.' && LA34_7 <= '9')||LA34_7=='<'||LA34_7=='>'||(LA34_7 >= '@' && LA34_7 <= 'Z')||(LA34_7 >= '_' && LA34_7 <= 'z')||LA34_7=='|'||(LA34_7 >= '\u007F' && LA34_7 <= '\u2012')||(LA34_7 >= '\u2014' && LA34_7 <= '\u2FFF')||(LA34_7 >= '\u3001' && LA34_7 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA34_7=='\\') ) {s = 5;}

                        else if ( (LA34_7=='+') ) {s = 6;}

                        else if ( (LA34_7=='-') ) {s = 7;}

                        else if ( (LA34_7=='\u2013') ) {s = 8;}

                        else if ( (LA34_7=='=') ) {s = 9;}

                        else if ( (LA34_7=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA34_1 = input.LA(1);

                        s = -1;
                        if ( ((LA34_1 >= '\u0000' && LA34_1 <= '\b')||(LA34_1 >= '\u000B' && LA34_1 <= '\f')||(LA34_1 >= '\u000E' && LA34_1 <= '\u001F')||(LA34_1 >= '$' && LA34_1 <= '&')||(LA34_1 >= '.' && LA34_1 <= '9')||LA34_1=='<'||LA34_1=='>'||(LA34_1 >= '@' && LA34_1 <= 'Z')||(LA34_1 >= '_' && LA34_1 <= 'z')||LA34_1=='|'||(LA34_1 >= '\u007F' && LA34_1 <= '\u2012')||(LA34_1 >= '\u2014' && LA34_1 <= '\u2FFF')||(LA34_1 >= '\u3001' && LA34_1 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA34_1=='\\') ) {s = 5;}

                        else if ( (LA34_1=='+') ) {s = 6;}

                        else if ( (LA34_1=='-') ) {s = 7;}

                        else if ( (LA34_1=='\u2013') ) {s = 8;}

                        else if ( (LA34_1=='=') ) {s = 9;}

                        else if ( (LA34_1=='#') ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA34_13 = input.LA(1);

                        s = -1;
                        if ( (LA34_13=='*'||LA34_13=='?') ) {s = 12;}

                        else if ( ((LA34_13 >= '\u0000' && LA34_13 <= '\b')||(LA34_13 >= '\u000B' && LA34_13 <= '\f')||(LA34_13 >= '\u000E' && LA34_13 <= '\u001F')||(LA34_13 >= '$' && LA34_13 <= '&')||(LA34_13 >= '.' && LA34_13 <= '9')||LA34_13=='<'||LA34_13=='>'||(LA34_13 >= '@' && LA34_13 <= 'Z')||(LA34_13 >= '_' && LA34_13 <= 'z')||LA34_13=='|'||(LA34_13 >= '\u007F' && LA34_13 <= '\u2012')||(LA34_13 >= '\u2014' && LA34_13 <= '\u2FFF')||(LA34_13 >= '\u3001' && LA34_13 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA34_13=='\\') ) {s = 5;}

                        else if ( (LA34_13=='+') ) {s = 6;}

                        else if ( (LA34_13=='-') ) {s = 7;}

                        else if ( (LA34_13=='\u2013') ) {s = 8;}

                        else if ( (LA34_13=='=') ) {s = 9;}

                        else if ( (LA34_13=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA34_10 = input.LA(1);

                        s = -1;
                        if ( (LA34_10=='*'||LA34_10=='?') ) {s = 12;}

                        else if ( ((LA34_10 >= '\u0000' && LA34_10 <= '\b')||(LA34_10 >= '\u000B' && LA34_10 <= '\f')||(LA34_10 >= '\u000E' && LA34_10 <= '\u001F')||(LA34_10 >= '$' && LA34_10 <= '&')||(LA34_10 >= '.' && LA34_10 <= '9')||LA34_10=='<'||LA34_10=='>'||(LA34_10 >= '@' && LA34_10 <= 'Z')||(LA34_10 >= '_' && LA34_10 <= 'z')||LA34_10=='|'||(LA34_10 >= '\u007F' && LA34_10 <= '\u2012')||(LA34_10 >= '\u2014' && LA34_10 <= '\u2FFF')||(LA34_10 >= '\u3001' && LA34_10 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA34_10=='\\') ) {s = 5;}

                        else if ( (LA34_10=='+') ) {s = 6;}

                        else if ( (LA34_10=='-') ) {s = 7;}

                        else if ( (LA34_10=='\u2013') ) {s = 8;}

                        else if ( (LA34_10=='=') ) {s = 9;}

                        else if ( (LA34_10=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA34_9 = input.LA(1);

                        s = -1;
                        if ( (LA34_9=='*'||LA34_9=='?') ) {s = 12;}

                        else if ( ((LA34_9 >= '\u0000' && LA34_9 <= '\b')||(LA34_9 >= '\u000B' && LA34_9 <= '\f')||(LA34_9 >= '\u000E' && LA34_9 <= '\u001F')||(LA34_9 >= '$' && LA34_9 <= '&')||(LA34_9 >= '.' && LA34_9 <= '9')||LA34_9=='<'||LA34_9=='>'||(LA34_9 >= '@' && LA34_9 <= 'Z')||(LA34_9 >= '_' && LA34_9 <= 'z')||LA34_9=='|'||(LA34_9 >= '\u007F' && LA34_9 <= '\u2012')||(LA34_9 >= '\u2014' && LA34_9 <= '\u2FFF')||(LA34_9 >= '\u3001' && LA34_9 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA34_9=='\\') ) {s = 5;}

                        else if ( (LA34_9=='+') ) {s = 6;}

                        else if ( (LA34_9=='-') ) {s = 7;}

                        else if ( (LA34_9=='\u2013') ) {s = 8;}

                        else if ( (LA34_9=='=') ) {s = 9;}

                        else if ( (LA34_9=='#') ) {s = 10;}

                        else s = 11;

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
        "\2\10\1\uffff\5\10\2\uffff\1\10";
    static final String DFA26_eofS =
        "\13\uffff";
    static final String DFA26_minS =
        "\10\0\2\uffff\1\0";
    static final String DFA26_maxS =
        "\10\uffff\2\uffff\1\uffff";
    static final String DFA26_acceptS =
        "\10\uffff\1\2\1\1\1\uffff";
    static final String DFA26_specialS =
        "\1\2\1\1\1\6\1\4\1\5\1\10\1\0\1\3\2\uffff\1\7}>";
    static final String[] DFA26_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\4\uffff\1\3\1"+
            "\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\uffff\33\1\1\uffff\1\2"+
            "\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1\uffff"+
            "\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\0\12",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1"
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

    class DFA26 extends DFA {

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
        public String getDescription() {
            return "()+ loopback of 522:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA26_6 = input.LA(1);

                        s = -1;
                        if ( ((LA26_6 >= '\u0000' && LA26_6 <= '\b')||(LA26_6 >= '\u000B' && LA26_6 <= '\f')||(LA26_6 >= '\u000E' && LA26_6 <= '\u001F')||(LA26_6 >= '$' && LA26_6 <= '&')||(LA26_6 >= '.' && LA26_6 <= '9')||LA26_6=='<'||LA26_6=='>'||(LA26_6 >= '@' && LA26_6 <= 'Z')||(LA26_6 >= '_' && LA26_6 <= 'z')||LA26_6=='|'||(LA26_6 >= '\u007F' && LA26_6 <= '\u2012')||(LA26_6 >= '\u2014' && LA26_6 <= '\u2FFF')||(LA26_6 >= '\u3001' && LA26_6 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_6=='\\') ) {s = 2;}

                        else if ( (LA26_6=='+') ) {s = 3;}

                        else if ( (LA26_6=='-') ) {s = 4;}

                        else if ( (LA26_6=='\u2013') ) {s = 5;}

                        else if ( (LA26_6=='=') ) {s = 6;}

                        else if ( (LA26_6=='#') ) {s = 7;}

                        else if ( (LA26_6=='*'||LA26_6=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA26_1 = input.LA(1);

                        s = -1;
                        if ( ((LA26_1 >= '\u0000' && LA26_1 <= '\b')||(LA26_1 >= '\u000B' && LA26_1 <= '\f')||(LA26_1 >= '\u000E' && LA26_1 <= '\u001F')||(LA26_1 >= '$' && LA26_1 <= '&')||(LA26_1 >= '.' && LA26_1 <= '9')||LA26_1=='<'||LA26_1=='>'||(LA26_1 >= '@' && LA26_1 <= 'Z')||(LA26_1 >= '_' && LA26_1 <= 'z')||LA26_1=='|'||(LA26_1 >= '\u007F' && LA26_1 <= '\u2012')||(LA26_1 >= '\u2014' && LA26_1 <= '\u2FFF')||(LA26_1 >= '\u3001' && LA26_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_1=='\\') ) {s = 2;}

                        else if ( (LA26_1=='+') ) {s = 3;}

                        else if ( (LA26_1=='-') ) {s = 4;}

                        else if ( (LA26_1=='\u2013') ) {s = 5;}

                        else if ( (LA26_1=='=') ) {s = 6;}

                        else if ( (LA26_1=='#') ) {s = 7;}

                        else if ( (LA26_1=='*'||LA26_1=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA26_0 = input.LA(1);

                        s = -1;
                        if ( ((LA26_0 >= '\u0000' && LA26_0 <= '\b')||(LA26_0 >= '\u000B' && LA26_0 <= '\f')||(LA26_0 >= '\u000E' && LA26_0 <= '\u001F')||(LA26_0 >= '$' && LA26_0 <= '&')||(LA26_0 >= '.' && LA26_0 <= '9')||LA26_0=='<'||LA26_0=='>'||(LA26_0 >= '@' && LA26_0 <= 'Z')||(LA26_0 >= '_' && LA26_0 <= 'z')||LA26_0=='|'||(LA26_0 >= '\u007F' && LA26_0 <= '\u2012')||(LA26_0 >= '\u2014' && LA26_0 <= '\u2FFF')||(LA26_0 >= '\u3001' && LA26_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_0=='\\') ) {s = 2;}

                        else if ( (LA26_0=='+') ) {s = 3;}

                        else if ( (LA26_0=='-') ) {s = 4;}

                        else if ( (LA26_0=='\u2013') ) {s = 5;}

                        else if ( (LA26_0=='=') ) {s = 6;}

                        else if ( (LA26_0=='#') ) {s = 7;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA26_7 = input.LA(1);

                        s = -1;
                        if ( ((LA26_7 >= '\u0000' && LA26_7 <= '\b')||(LA26_7 >= '\u000B' && LA26_7 <= '\f')||(LA26_7 >= '\u000E' && LA26_7 <= '\u001F')||(LA26_7 >= '$' && LA26_7 <= '&')||(LA26_7 >= '.' && LA26_7 <= '9')||LA26_7=='<'||LA26_7=='>'||(LA26_7 >= '@' && LA26_7 <= 'Z')||(LA26_7 >= '_' && LA26_7 <= 'z')||LA26_7=='|'||(LA26_7 >= '\u007F' && LA26_7 <= '\u2012')||(LA26_7 >= '\u2014' && LA26_7 <= '\u2FFF')||(LA26_7 >= '\u3001' && LA26_7 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_7=='\\') ) {s = 2;}

                        else if ( (LA26_7=='+') ) {s = 3;}

                        else if ( (LA26_7=='-') ) {s = 4;}

                        else if ( (LA26_7=='\u2013') ) {s = 5;}

                        else if ( (LA26_7=='=') ) {s = 6;}

                        else if ( (LA26_7=='#') ) {s = 7;}

                        else if ( (LA26_7=='*'||LA26_7=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA26_3 = input.LA(1);

                        s = -1;
                        if ( ((LA26_3 >= '\u0000' && LA26_3 <= '\b')||(LA26_3 >= '\u000B' && LA26_3 <= '\f')||(LA26_3 >= '\u000E' && LA26_3 <= '\u001F')||(LA26_3 >= '$' && LA26_3 <= '&')||(LA26_3 >= '.' && LA26_3 <= '9')||LA26_3=='<'||LA26_3=='>'||(LA26_3 >= '@' && LA26_3 <= 'Z')||(LA26_3 >= '_' && LA26_3 <= 'z')||LA26_3=='|'||(LA26_3 >= '\u007F' && LA26_3 <= '\u2012')||(LA26_3 >= '\u2014' && LA26_3 <= '\u2FFF')||(LA26_3 >= '\u3001' && LA26_3 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_3=='\\') ) {s = 2;}

                        else if ( (LA26_3=='+') ) {s = 3;}

                        else if ( (LA26_3=='-') ) {s = 4;}

                        else if ( (LA26_3=='\u2013') ) {s = 5;}

                        else if ( (LA26_3=='=') ) {s = 6;}

                        else if ( (LA26_3=='#') ) {s = 7;}

                        else if ( (LA26_3=='*'||LA26_3=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA26_4 = input.LA(1);

                        s = -1;
                        if ( ((LA26_4 >= '\u0000' && LA26_4 <= '\b')||(LA26_4 >= '\u000B' && LA26_4 <= '\f')||(LA26_4 >= '\u000E' && LA26_4 <= '\u001F')||(LA26_4 >= '$' && LA26_4 <= '&')||(LA26_4 >= '.' && LA26_4 <= '9')||LA26_4=='<'||LA26_4=='>'||(LA26_4 >= '@' && LA26_4 <= 'Z')||(LA26_4 >= '_' && LA26_4 <= 'z')||LA26_4=='|'||(LA26_4 >= '\u007F' && LA26_4 <= '\u2012')||(LA26_4 >= '\u2014' && LA26_4 <= '\u2FFF')||(LA26_4 >= '\u3001' && LA26_4 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_4=='\\') ) {s = 2;}

                        else if ( (LA26_4=='+') ) {s = 3;}

                        else if ( (LA26_4=='-') ) {s = 4;}

                        else if ( (LA26_4=='\u2013') ) {s = 5;}

                        else if ( (LA26_4=='=') ) {s = 6;}

                        else if ( (LA26_4=='#') ) {s = 7;}

                        else if ( (LA26_4=='*'||LA26_4=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA26_2 = input.LA(1);

                        s = -1;
                        if ( ((LA26_2 >= '\u0000' && LA26_2 <= '\uFFFF')) ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA26_10 = input.LA(1);

                        s = -1;
                        if ( ((LA26_10 >= '\u0000' && LA26_10 <= '\b')||(LA26_10 >= '\u000B' && LA26_10 <= '\f')||(LA26_10 >= '\u000E' && LA26_10 <= '\u001F')||(LA26_10 >= '$' && LA26_10 <= '&')||(LA26_10 >= '.' && LA26_10 <= '9')||LA26_10=='<'||LA26_10=='>'||(LA26_10 >= '@' && LA26_10 <= 'Z')||(LA26_10 >= '_' && LA26_10 <= 'z')||LA26_10=='|'||(LA26_10 >= '\u007F' && LA26_10 <= '\u2012')||(LA26_10 >= '\u2014' && LA26_10 <= '\u2FFF')||(LA26_10 >= '\u3001' && LA26_10 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_10=='\\') ) {s = 2;}

                        else if ( (LA26_10=='+') ) {s = 3;}

                        else if ( (LA26_10=='-') ) {s = 4;}

                        else if ( (LA26_10=='\u2013') ) {s = 5;}

                        else if ( (LA26_10=='=') ) {s = 6;}

                        else if ( (LA26_10=='#') ) {s = 7;}

                        else if ( (LA26_10=='*'||LA26_10=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA26_5 = input.LA(1);

                        s = -1;
                        if ( ((LA26_5 >= '\u0000' && LA26_5 <= '\b')||(LA26_5 >= '\u000B' && LA26_5 <= '\f')||(LA26_5 >= '\u000E' && LA26_5 <= '\u001F')||(LA26_5 >= '$' && LA26_5 <= '&')||(LA26_5 >= '.' && LA26_5 <= '9')||LA26_5=='<'||LA26_5=='>'||(LA26_5 >= '@' && LA26_5 <= 'Z')||(LA26_5 >= '_' && LA26_5 <= 'z')||LA26_5=='|'||(LA26_5 >= '\u007F' && LA26_5 <= '\u2012')||(LA26_5 >= '\u2014' && LA26_5 <= '\u2FFF')||(LA26_5 >= '\u3001' && LA26_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_5=='\\') ) {s = 2;}

                        else if ( (LA26_5=='+') ) {s = 3;}

                        else if ( (LA26_5=='-') ) {s = 4;}

                        else if ( (LA26_5=='\u2013') ) {s = 5;}

                        else if ( (LA26_5=='=') ) {s = 6;}

                        else if ( (LA26_5=='#') ) {s = 7;}

                        else if ( (LA26_5=='*'||LA26_5=='?') ) {s = 9;}

                        else s = 8;

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
        "\2\10\1\uffff\5\10\2\uffff\1\10";
    static final String DFA30_eofS =
        "\13\uffff";
    static final String DFA30_minS =
        "\10\0\2\uffff\1\0";
    static final String DFA30_maxS =
        "\10\uffff\2\uffff\1\uffff";
    static final String DFA30_acceptS =
        "\10\uffff\1\2\1\1\1\uffff";
    static final String DFA30_specialS =
        "\1\1\1\10\1\4\1\3\1\7\1\0\1\2\1\5\2\uffff\1\6}>";
    static final String[] DFA30_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\0\12",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\7\3\1\3\uffff\1\11"+
            "\1\3\1\uffff\1\4\14\1\2\uffff\1\1\1\6\1\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\1\uffff\1\1\2\uffff\u1f94\1\1\5\u0fec\1\1"+
            "\uffff\ucfff\1"
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

    class DFA30 extends DFA {

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
        public String getDescription() {
            return "()+ loopback of 523:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA30_5 = input.LA(1);

                        s = -1;
                        if ( ((LA30_5 >= '\u0000' && LA30_5 <= '\b')||(LA30_5 >= '\u000B' && LA30_5 <= '\f')||(LA30_5 >= '\u000E' && LA30_5 <= '\u001F')||(LA30_5 >= '$' && LA30_5 <= '&')||(LA30_5 >= '.' && LA30_5 <= '9')||LA30_5=='<'||LA30_5=='>'||(LA30_5 >= '@' && LA30_5 <= 'Z')||(LA30_5 >= '_' && LA30_5 <= 'z')||LA30_5=='|'||(LA30_5 >= '\u007F' && LA30_5 <= '\u2012')||(LA30_5 >= '\u2014' && LA30_5 <= '\u2FFF')||(LA30_5 >= '\u3001' && LA30_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_5=='\\') ) {s = 2;}

                        else if ( (LA30_5=='+') ) {s = 3;}

                        else if ( (LA30_5=='-') ) {s = 4;}

                        else if ( (LA30_5=='\u2013') ) {s = 5;}

                        else if ( (LA30_5=='=') ) {s = 6;}

                        else if ( (LA30_5=='#') ) {s = 7;}

                        else if ( (LA30_5=='*'||LA30_5=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA30_0 = input.LA(1);

                        s = -1;
                        if ( ((LA30_0 >= '\u0000' && LA30_0 <= '\b')||(LA30_0 >= '\u000B' && LA30_0 <= '\f')||(LA30_0 >= '\u000E' && LA30_0 <= '\u001F')||(LA30_0 >= '$' && LA30_0 <= '&')||(LA30_0 >= '.' && LA30_0 <= '9')||LA30_0=='<'||LA30_0=='>'||(LA30_0 >= '@' && LA30_0 <= 'Z')||(LA30_0 >= '_' && LA30_0 <= 'z')||LA30_0=='|'||(LA30_0 >= '\u007F' && LA30_0 <= '\u2012')||(LA30_0 >= '\u2014' && LA30_0 <= '\u2FFF')||(LA30_0 >= '\u3001' && LA30_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_0=='\\') ) {s = 2;}

                        else if ( (LA30_0=='+') ) {s = 3;}

                        else if ( (LA30_0=='-') ) {s = 4;}

                        else if ( (LA30_0=='\u2013') ) {s = 5;}

                        else if ( (LA30_0=='=') ) {s = 6;}

                        else if ( (LA30_0=='#') ) {s = 7;}

                        else if ( (LA30_0=='*'||LA30_0=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA30_6 = input.LA(1);

                        s = -1;
                        if ( ((LA30_6 >= '\u0000' && LA30_6 <= '\b')||(LA30_6 >= '\u000B' && LA30_6 <= '\f')||(LA30_6 >= '\u000E' && LA30_6 <= '\u001F')||(LA30_6 >= '$' && LA30_6 <= '&')||(LA30_6 >= '.' && LA30_6 <= '9')||LA30_6=='<'||LA30_6=='>'||(LA30_6 >= '@' && LA30_6 <= 'Z')||(LA30_6 >= '_' && LA30_6 <= 'z')||LA30_6=='|'||(LA30_6 >= '\u007F' && LA30_6 <= '\u2012')||(LA30_6 >= '\u2014' && LA30_6 <= '\u2FFF')||(LA30_6 >= '\u3001' && LA30_6 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_6=='\\') ) {s = 2;}

                        else if ( (LA30_6=='+') ) {s = 3;}

                        else if ( (LA30_6=='-') ) {s = 4;}

                        else if ( (LA30_6=='\u2013') ) {s = 5;}

                        else if ( (LA30_6=='=') ) {s = 6;}

                        else if ( (LA30_6=='#') ) {s = 7;}

                        else if ( (LA30_6=='*'||LA30_6=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA30_3 = input.LA(1);

                        s = -1;
                        if ( ((LA30_3 >= '\u0000' && LA30_3 <= '\b')||(LA30_3 >= '\u000B' && LA30_3 <= '\f')||(LA30_3 >= '\u000E' && LA30_3 <= '\u001F')||(LA30_3 >= '$' && LA30_3 <= '&')||(LA30_3 >= '.' && LA30_3 <= '9')||LA30_3=='<'||LA30_3=='>'||(LA30_3 >= '@' && LA30_3 <= 'Z')||(LA30_3 >= '_' && LA30_3 <= 'z')||LA30_3=='|'||(LA30_3 >= '\u007F' && LA30_3 <= '\u2012')||(LA30_3 >= '\u2014' && LA30_3 <= '\u2FFF')||(LA30_3 >= '\u3001' && LA30_3 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_3=='\\') ) {s = 2;}

                        else if ( (LA30_3=='+') ) {s = 3;}

                        else if ( (LA30_3=='-') ) {s = 4;}

                        else if ( (LA30_3=='\u2013') ) {s = 5;}

                        else if ( (LA30_3=='=') ) {s = 6;}

                        else if ( (LA30_3=='#') ) {s = 7;}

                        else if ( (LA30_3=='*'||LA30_3=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA30_2 = input.LA(1);

                        s = -1;
                        if ( ((LA30_2 >= '\u0000' && LA30_2 <= '\uFFFF')) ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA30_7 = input.LA(1);

                        s = -1;
                        if ( ((LA30_7 >= '\u0000' && LA30_7 <= '\b')||(LA30_7 >= '\u000B' && LA30_7 <= '\f')||(LA30_7 >= '\u000E' && LA30_7 <= '\u001F')||(LA30_7 >= '$' && LA30_7 <= '&')||(LA30_7 >= '.' && LA30_7 <= '9')||LA30_7=='<'||LA30_7=='>'||(LA30_7 >= '@' && LA30_7 <= 'Z')||(LA30_7 >= '_' && LA30_7 <= 'z')||LA30_7=='|'||(LA30_7 >= '\u007F' && LA30_7 <= '\u2012')||(LA30_7 >= '\u2014' && LA30_7 <= '\u2FFF')||(LA30_7 >= '\u3001' && LA30_7 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_7=='\\') ) {s = 2;}

                        else if ( (LA30_7=='+') ) {s = 3;}

                        else if ( (LA30_7=='-') ) {s = 4;}

                        else if ( (LA30_7=='\u2013') ) {s = 5;}

                        else if ( (LA30_7=='=') ) {s = 6;}

                        else if ( (LA30_7=='#') ) {s = 7;}

                        else if ( (LA30_7=='*'||LA30_7=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA30_10 = input.LA(1);

                        s = -1;
                        if ( ((LA30_10 >= '\u0000' && LA30_10 <= '\b')||(LA30_10 >= '\u000B' && LA30_10 <= '\f')||(LA30_10 >= '\u000E' && LA30_10 <= '\u001F')||(LA30_10 >= '$' && LA30_10 <= '&')||(LA30_10 >= '.' && LA30_10 <= '9')||LA30_10=='<'||LA30_10=='>'||(LA30_10 >= '@' && LA30_10 <= 'Z')||(LA30_10 >= '_' && LA30_10 <= 'z')||LA30_10=='|'||(LA30_10 >= '\u007F' && LA30_10 <= '\u2012')||(LA30_10 >= '\u2014' && LA30_10 <= '\u2FFF')||(LA30_10 >= '\u3001' && LA30_10 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_10=='\\') ) {s = 2;}

                        else if ( (LA30_10=='+') ) {s = 3;}

                        else if ( (LA30_10=='-') ) {s = 4;}

                        else if ( (LA30_10=='\u2013') ) {s = 5;}

                        else if ( (LA30_10=='=') ) {s = 6;}

                        else if ( (LA30_10=='#') ) {s = 7;}

                        else if ( (LA30_10=='*'||LA30_10=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA30_4 = input.LA(1);

                        s = -1;
                        if ( ((LA30_4 >= '\u0000' && LA30_4 <= '\b')||(LA30_4 >= '\u000B' && LA30_4 <= '\f')||(LA30_4 >= '\u000E' && LA30_4 <= '\u001F')||(LA30_4 >= '$' && LA30_4 <= '&')||(LA30_4 >= '.' && LA30_4 <= '9')||LA30_4=='<'||LA30_4=='>'||(LA30_4 >= '@' && LA30_4 <= 'Z')||(LA30_4 >= '_' && LA30_4 <= 'z')||LA30_4=='|'||(LA30_4 >= '\u007F' && LA30_4 <= '\u2012')||(LA30_4 >= '\u2014' && LA30_4 <= '\u2FFF')||(LA30_4 >= '\u3001' && LA30_4 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_4=='\\') ) {s = 2;}

                        else if ( (LA30_4=='+') ) {s = 3;}

                        else if ( (LA30_4=='-') ) {s = 4;}

                        else if ( (LA30_4=='\u2013') ) {s = 5;}

                        else if ( (LA30_4=='=') ) {s = 6;}

                        else if ( (LA30_4=='#') ) {s = 7;}

                        else if ( (LA30_4=='*'||LA30_4=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA30_1 = input.LA(1);

                        s = -1;
                        if ( ((LA30_1 >= '\u0000' && LA30_1 <= '\b')||(LA30_1 >= '\u000B' && LA30_1 <= '\f')||(LA30_1 >= '\u000E' && LA30_1 <= '\u001F')||(LA30_1 >= '$' && LA30_1 <= '&')||(LA30_1 >= '.' && LA30_1 <= '9')||LA30_1=='<'||LA30_1=='>'||(LA30_1 >= '@' && LA30_1 <= 'Z')||(LA30_1 >= '_' && LA30_1 <= 'z')||LA30_1=='|'||(LA30_1 >= '\u007F' && LA30_1 <= '\u2012')||(LA30_1 >= '\u2014' && LA30_1 <= '\u2FFF')||(LA30_1 >= '\u3001' && LA30_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_1=='\\') ) {s = 2;}

                        else if ( (LA30_1=='+') ) {s = 3;}

                        else if ( (LA30_1=='-') ) {s = 4;}

                        else if ( (LA30_1=='\u2013') ) {s = 5;}

                        else if ( (LA30_1=='=') ) {s = 6;}

                        else if ( (LA30_1=='#') ) {s = 7;}

                        else if ( (LA30_1=='*'||LA30_1=='?') ) {s = 9;}

                        else s = 8;

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
        "\2\uffff\1\37\1\52\7\uffff\1\30\1\54\1\55\1\56\1\uffff\1\60\3\uffff"+
        "\4\52\1\uffff\1\72\3\52\3\uffff\1\52\1\uffff\5\52\2\uffff\1\52\12"+
        "\uffff\1\106\2\52\1\112\2\52\1\uffff\1\72\7\52\1\122\3\uffff\1\124"+
        "\2\52\1\uffff\1\127\1\52\1\72\1\uffff\1\72\2\52\3\uffff\2\52\1\uffff"+
        "\1\143\1\72\1\52\1\72\4\52\1\uffff\2\52\1\uffff\1\53\2\72\2\52\1"+
        "\153\1\52\1\uffff\2\52\1\153\1\53";
    static final String DFA37_eofS =
        "\160\uffff";
    static final String DFA37_minS =
        "\1\0\1\uffff\2\0\7\uffff\1\60\3\0\1\uffff\1\0\3\uffff\4\0\1\uffff"+
        "\5\0\2\uffff\7\0\2\uffff\1\0\7\uffff\2\0\1\uffff\6\0\1\uffff\12"+
        "\0\2\uffff\3\0\1\uffff\3\0\1\uffff\3\0\3\uffff\2\0\1\uffff\10\0"+
        "\1\uffff\2\0\1\uffff\7\0\1\uffff\4\0";
    static final String DFA37_maxS =
        "\1\uffff\1\uffff\2\uffff\7\uffff\1\71\3\uffff\1\uffff\1\uffff\3"+
        "\uffff\4\uffff\1\uffff\5\uffff\2\uffff\7\uffff\2\uffff\1\uffff\7"+
        "\uffff\2\uffff\1\uffff\6\uffff\1\uffff\12\uffff\2\uffff\3\uffff"+
        "\1\uffff\3\uffff\1\uffff\3\uffff\3\uffff\2\uffff\1\uffff\10\uffff"+
        "\1\uffff\2\uffff\1\uffff\7\uffff\1\uffff\4\uffff";
    static final String DFA37_acceptS =
        "\1\uffff\1\1\2\uffff\1\4\1\5\1\6\1\7\1\10\1\11\1\12\4\uffff\1\17"+
        "\1\uffff\1\21\1\22\1\23\4\uffff\1\13\5\uffff\1\35\1\2\7\uffff\1"+
        "\34\1\42\1\uffff\1\41\1\32\1\14\1\15\1\16\1\31\1\20\2\uffff\1\44"+
        "\6\uffff\1\37\12\uffff\1\43\1\24\3\uffff\1\26\3\uffff\1\40\3\uffff"+
        "\1\3\1\43\1\25\2\uffff\1\27\10\uffff\1\33\2\uffff\1\30\7\uffff\1"+
        "\36\4\uffff";
    static final String DFA37_specialS =
        "\1\4\1\uffff\1\70\1\40\10\uffff\1\26\1\77\1\13\1\uffff\1\46\3\uffff"+
        "\1\75\1\52\1\71\1\101\1\uffff\1\17\1\100\1\33\1\107\1\36\2\uffff"+
        "\1\2\1\35\1\55\1\47\1\7\1\1\1\0\2\uffff\1\105\7\uffff\1\72\1\73"+
        "\1\uffff\1\27\1\61\1\44\1\14\1\60\1\56\1\uffff\1\34\1\37\1\42\1"+
        "\53\1\110\1\21\1\43\1\23\1\12\1\5\2\uffff\1\3\1\25\1\16\1\uffff"+
        "\1\102\1\57\1\104\1\uffff\1\103\1\64\1\11\3\uffff\1\30\1\15\1\uffff"+
        "\1\74\1\22\1\65\1\20\1\54\1\50\1\111\1\106\1\uffff\1\45\1\51\1\uffff"+
        "\1\41\1\32\1\76\1\31\1\63\1\62\1\67\1\uffff\1\24\1\66\1\6\1\10}>";
    static final String[] DFA37_transitionS = {
            "\11\34\2\36\2\34\1\36\22\34\1\36\1\uffff\1\20\1\1\3\34\1\21"+
            "\1\5\1\6\1\14\1\12\1\22\1\13\1\34\1\2\12\31\1\11\1\23\1\3\1"+
            "\4\1\34\1\15\1\34\1\32\14\34\1\27\1\26\4\34\1\24\6\34\1\7\1"+
            "\35\1\10\1\16\2\34\1\25\2\34\1\33\11\34\1\27\1\26\13\34\1\uffff"+
            "\1\34\1\uffff\1\17\u1f94\34\1\30\u0fec\34\1\36\ucfff\34",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\51"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\53",
            "\11\50\2\uffff\2\50\1\uffff\22\50\3\uffff\4\50\4\uffff\1\50"+
            "\1\uffff\15\50\2\uffff\3\50\1\uffff\33\50\1\uffff\1\50\2\uffff"+
            "\34\50\1\uffff\1\50\2\uffff\u2f81\50\1\uffff\ucfff\50",
            "\11\50\2\uffff\2\50\1\uffff\22\50\3\uffff\4\50\4\uffff\1\50"+
            "\1\uffff\15\50\2\uffff\3\50\1\15\33\50\1\uffff\1\50\2\uffff"+
            "\34\50\1\uffff\1\50\2\uffff\u2f81\50\1\uffff\ucfff\50",
            "\40\57\1\uffff\3\57\1\uffff\6\57\3\uffff\2\57\12\uffff\u1fd9"+
            "\57\1\uffff\udfec\57",
            "",
            "\42\62\1\uffff\7\62\1\63\24\62\1\63\34\62\1\61\uffa3\62",
            "",
            "",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\17\40\1\64\13\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\16\40\1\65\14\40\1\uffff\1\41\2\uffff\17\40\1\65"+
            "\3\40\1\66\10\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40"+
            "\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\22\40\1\67\10\40\1\uffff\1\41\2\uffff\23\40\1\67"+
            "\10\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\5\40\1\71\11\40\1\70\13\40\1\uffff\1\41\2\uffff\6"+
            "\40\1\71\11\40\1\70\13\40\1\uffff\1\40\2\uffff\u1f94\40\1\44"+
            "\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\75\1\74\1\77\12\73\2\uffff"+
            "\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\76\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\16\40\1\65\14\40\1\uffff\1\41\2\uffff\17\40\1\65"+
            "\14\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\20\40\1\100\13\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\0\101",
            "",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\0\102",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\103\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2"+
            "\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\0\104",
            "\42\62\1\105\7\62\1\63\24\62\1\63\34\62\1\61\uffa3\62",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\4\40\1\107\26\40\1\uffff\1\41\2\uffff\5\40\1\107"+
            "\26\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\30\40\1\110\2\40\1\uffff\1\41\2\uffff\31\40\1\111"+
            "\2\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\24\40\1\113\6\40\1\uffff\1\41\2\uffff\25\40\1\113"+
            "\6\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\1\40\1\114\31\40\1\uffff\1\41\2\uffff\2\40\1\114"+
            "\31\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\75\1\74\1\77\12\115\1\116"+
            "\1\uffff\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40"+
            "\1\uffff\1\40\2\uffff\u1f94\40\1\76\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\117\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\120\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\120\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\120\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\12\40\1\121\21\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\42\62\1\105\7\62\1\63\24\62\1\63\34\62\1\61\uffa3\62",
            "",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\12\40\1\125\21\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\12\40\1\126\21\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\22\40\1\130\10\40\1\uffff\1\41\2\uffff\23\40\1\130"+
            "\10\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\1\132\1\40\12\131\2\uffff"+
            "\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\134\2\136\12\133\2\uffff\1"+
            "\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\135\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\134\2\136\12\137\2\uffff\1"+
            "\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\135\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\1\140\1\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\27\40\1\141\4\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\27\40\1\142\4\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\144\1\132\1\40\12\145\2\uffff"+
            "\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\146\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\134\2\136\12\146\2\uffff\1"+
            "\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\135\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\147\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\147\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\147\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\134\2\136\12\40\2\uffff\1"+
            "\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\135\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\1\140\1\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\1\140\1\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\150\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\1\132\1\40\12\145\2\uffff"+
            "\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\146\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\151\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\152\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\154\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\155\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\156\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\157\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40"
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

    class DFA37 extends DFA {

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
        public String getDescription() {
            return "1:1: Tokens : ( T__68 | T__69 | T__70 | T__71 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | CARAT | TILDE | DQUOTE | SQUOTE | COMMA | SEMICOLON | TO | AND | OR | NOT | NEAR | AUTHOR_SEARCH | DATE_RANGE | IDENTIFIER | FUNC_NAME | WS | DATE_TOKEN | NUMBER | HOUR | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA37_38 = input.LA(1);

                        s = -1;
                        if ( (LA37_38=='(') ) {s = 39;}

                        else if ( ((LA37_38 >= '\u0000' && LA37_38 <= '\b')||(LA37_38 >= '\u000B' && LA37_38 <= '\f')||(LA37_38 >= '\u000E' && LA37_38 <= '\u001F')||(LA37_38 >= '$' && LA37_38 <= '&')||(LA37_38 >= '.' && LA37_38 <= '9')||LA37_38=='<'||LA37_38=='>'||(LA37_38 >= '@' && LA37_38 <= 'Z')||(LA37_38 >= '_' && LA37_38 <= 'z')||LA37_38=='|'||(LA37_38 >= '\u007F' && LA37_38 <= '\u2012')||(LA37_38 >= '\u2014' && LA37_38 <= '\u2FFF')||(LA37_38 >= '\u3001' && LA37_38 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_38=='\\') ) {s = 33;}

                        else if ( (LA37_38=='+') ) {s = 34;}

                        else if ( (LA37_38=='-') ) {s = 35;}

                        else if ( (LA37_38=='\u2013') ) {s = 36;}

                        else if ( (LA37_38=='=') ) {s = 37;}

                        else if ( (LA37_38=='#') ) {s = 38;}

                        else if ( (LA37_38=='*'||LA37_38=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA37_37 = input.LA(1);

                        s = -1;
                        if ( (LA37_37=='(') ) {s = 39;}

                        else if ( ((LA37_37 >= '\u0000' && LA37_37 <= '\b')||(LA37_37 >= '\u000B' && LA37_37 <= '\f')||(LA37_37 >= '\u000E' && LA37_37 <= '\u001F')||(LA37_37 >= '$' && LA37_37 <= '&')||(LA37_37 >= '.' && LA37_37 <= '9')||LA37_37=='<'||LA37_37=='>'||(LA37_37 >= '@' && LA37_37 <= 'Z')||(LA37_37 >= '_' && LA37_37 <= 'z')||LA37_37=='|'||(LA37_37 >= '\u007F' && LA37_37 <= '\u2012')||(LA37_37 >= '\u2014' && LA37_37 <= '\u2FFF')||(LA37_37 >= '\u3001' && LA37_37 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_37=='\\') ) {s = 33;}

                        else if ( (LA37_37=='+') ) {s = 34;}

                        else if ( (LA37_37=='-') ) {s = 35;}

                        else if ( (LA37_37=='\u2013') ) {s = 36;}

                        else if ( (LA37_37=='=') ) {s = 37;}

                        else if ( (LA37_37=='#') ) {s = 38;}

                        else if ( (LA37_37=='*'||LA37_37=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA37_32 = input.LA(1);

                        s = -1;
                        if ( (LA37_32=='(') ) {s = 39;}

                        else if ( ((LA37_32 >= '\u0000' && LA37_32 <= '\b')||(LA37_32 >= '\u000B' && LA37_32 <= '\f')||(LA37_32 >= '\u000E' && LA37_32 <= '\u001F')||(LA37_32 >= '$' && LA37_32 <= '&')||(LA37_32 >= '.' && LA37_32 <= '9')||LA37_32=='<'||LA37_32=='>'||(LA37_32 >= '@' && LA37_32 <= 'Z')||(LA37_32 >= '_' && LA37_32 <= 'z')||LA37_32=='|'||(LA37_32 >= '\u007F' && LA37_32 <= '\u2012')||(LA37_32 >= '\u2014' && LA37_32 <= '\u2FFF')||(LA37_32 >= '\u3001' && LA37_32 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_32=='\\') ) {s = 33;}

                        else if ( (LA37_32=='+') ) {s = 34;}

                        else if ( (LA37_32=='-') ) {s = 35;}

                        else if ( (LA37_32=='\u2013') ) {s = 36;}

                        else if ( (LA37_32=='=') ) {s = 37;}

                        else if ( (LA37_32=='#') ) {s = 38;}

                        else if ( (LA37_32=='*'||LA37_32=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA37_71 = input.LA(1);

                        s = -1;
                        if ( (LA37_71=='(') ) {s = 39;}

                        else if ( ((LA37_71 >= '\u0000' && LA37_71 <= '\b')||(LA37_71 >= '\u000B' && LA37_71 <= '\f')||(LA37_71 >= '\u000E' && LA37_71 <= '\u001F')||(LA37_71 >= '$' && LA37_71 <= '&')||(LA37_71 >= '.' && LA37_71 <= '9')||LA37_71=='<'||LA37_71=='>'||(LA37_71 >= '@' && LA37_71 <= 'Z')||(LA37_71 >= '_' && LA37_71 <= 'z')||LA37_71=='|'||(LA37_71 >= '\u007F' && LA37_71 <= '\u2012')||(LA37_71 >= '\u2014' && LA37_71 <= '\u2FFF')||(LA37_71 >= '\u3001' && LA37_71 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_71=='\\') ) {s = 33;}

                        else if ( (LA37_71=='+') ) {s = 34;}

                        else if ( (LA37_71=='-') ) {s = 35;}

                        else if ( (LA37_71=='\u2013') ) {s = 36;}

                        else if ( (LA37_71=='=') ) {s = 37;}

                        else if ( (LA37_71=='#') ) {s = 38;}

                        else if ( (LA37_71=='*'||LA37_71=='?') ) {s = 40;}

                        else s = 84;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA37_0 = input.LA(1);

                        s = -1;
                        if ( (LA37_0=='#') ) {s = 1;}

                        else if ( (LA37_0=='/') ) {s = 2;}

                        else if ( (LA37_0=='<') ) {s = 3;}

                        else if ( (LA37_0=='=') ) {s = 4;}

                        else if ( (LA37_0=='(') ) {s = 5;}

                        else if ( (LA37_0==')') ) {s = 6;}

                        else if ( (LA37_0=='[') ) {s = 7;}

                        else if ( (LA37_0==']') ) {s = 8;}

                        else if ( (LA37_0==':') ) {s = 9;}

                        else if ( (LA37_0=='+') ) {s = 10;}

                        else if ( (LA37_0=='-') ) {s = 11;}

                        else if ( (LA37_0=='*') ) {s = 12;}

                        else if ( (LA37_0=='?') ) {s = 13;}

                        else if ( (LA37_0=='^') ) {s = 14;}

                        else if ( (LA37_0=='~') ) {s = 15;}

                        else if ( (LA37_0=='\"') ) {s = 16;}

                        else if ( (LA37_0=='\'') ) {s = 17;}

                        else if ( (LA37_0==',') ) {s = 18;}

                        else if ( (LA37_0==';') ) {s = 19;}

                        else if ( (LA37_0=='T') ) {s = 20;}

                        else if ( (LA37_0=='a') ) {s = 21;}

                        else if ( (LA37_0=='O'||LA37_0=='o') ) {s = 22;}

                        else if ( (LA37_0=='N'||LA37_0=='n') ) {s = 23;}

                        else if ( (LA37_0=='\u2013') ) {s = 24;}

                        else if ( ((LA37_0 >= '0' && LA37_0 <= '9')) ) {s = 25;}

                        else if ( (LA37_0=='A') ) {s = 26;}

                        else if ( (LA37_0=='d') ) {s = 27;}

                        else if ( ((LA37_0 >= '\u0000' && LA37_0 <= '\b')||(LA37_0 >= '\u000B' && LA37_0 <= '\f')||(LA37_0 >= '\u000E' && LA37_0 <= '\u001F')||(LA37_0 >= '$' && LA37_0 <= '&')||LA37_0=='.'||LA37_0=='>'||LA37_0=='@'||(LA37_0 >= 'B' && LA37_0 <= 'M')||(LA37_0 >= 'P' && LA37_0 <= 'S')||(LA37_0 >= 'U' && LA37_0 <= 'Z')||(LA37_0 >= '_' && LA37_0 <= '`')||(LA37_0 >= 'b' && LA37_0 <= 'c')||(LA37_0 >= 'e' && LA37_0 <= 'm')||(LA37_0 >= 'p' && LA37_0 <= 'z')||LA37_0=='|'||(LA37_0 >= '\u007F' && LA37_0 <= '\u2012')||(LA37_0 >= '\u2014' && LA37_0 <= '\u2FFF')||(LA37_0 >= '\u3001' && LA37_0 <= '\uFFFF')) ) {s = 28;}

                        else if ( (LA37_0=='\\') ) {s = 29;}

                        else if ( ((LA37_0 >= '\t' && LA37_0 <= '\n')||LA37_0=='\r'||LA37_0==' '||LA37_0=='\u3000') ) {s = 30;}

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA37_68 = input.LA(1);

                        s = -1;
                        if ( (LA37_68=='\"') ) {s = 69;}

                        else if ( (LA37_68=='\\') ) {s = 49;}

                        else if ( ((LA37_68 >= '\u0000' && LA37_68 <= '!')||(LA37_68 >= '#' && LA37_68 <= ')')||(LA37_68 >= '+' && LA37_68 <= '>')||(LA37_68 >= '@' && LA37_68 <= '[')||(LA37_68 >= ']' && LA37_68 <= '\uFFFF')) ) {s = 50;}

                        else if ( (LA37_68=='*'||LA37_68=='?') ) {s = 51;}

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA37_110 = input.LA(1);

                        s = -1;
                        if ( (LA37_110=='(') ) {s = 39;}

                        else if ( ((LA37_110 >= '\u0000' && LA37_110 <= '\b')||(LA37_110 >= '\u000B' && LA37_110 <= '\f')||(LA37_110 >= '\u000E' && LA37_110 <= '\u001F')||(LA37_110 >= '$' && LA37_110 <= '&')||(LA37_110 >= '.' && LA37_110 <= '9')||LA37_110=='<'||LA37_110=='>'||(LA37_110 >= '@' && LA37_110 <= 'Z')||(LA37_110 >= '_' && LA37_110 <= 'z')||LA37_110=='|'||(LA37_110 >= '\u007F' && LA37_110 <= '\u2012')||(LA37_110 >= '\u2014' && LA37_110 <= '\u2FFF')||(LA37_110 >= '\u3001' && LA37_110 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_110=='\\') ) {s = 33;}

                        else if ( (LA37_110=='+') ) {s = 34;}

                        else if ( (LA37_110=='-') ) {s = 35;}

                        else if ( (LA37_110=='\u2013') ) {s = 36;}

                        else if ( (LA37_110=='=') ) {s = 37;}

                        else if ( (LA37_110=='#') ) {s = 38;}

                        else if ( (LA37_110=='*'||LA37_110=='?') ) {s = 40;}

                        else s = 107;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA37_36 = input.LA(1);

                        s = -1;
                        if ( (LA37_36=='(') ) {s = 39;}

                        else if ( ((LA37_36 >= '\u0000' && LA37_36 <= '\b')||(LA37_36 >= '\u000B' && LA37_36 <= '\f')||(LA37_36 >= '\u000E' && LA37_36 <= '\u001F')||(LA37_36 >= '$' && LA37_36 <= '&')||(LA37_36 >= '.' && LA37_36 <= '9')||LA37_36=='<'||LA37_36=='>'||(LA37_36 >= '@' && LA37_36 <= 'Z')||(LA37_36 >= '_' && LA37_36 <= 'z')||LA37_36=='|'||(LA37_36 >= '\u007F' && LA37_36 <= '\u2012')||(LA37_36 >= '\u2014' && LA37_36 <= '\u2FFF')||(LA37_36 >= '\u3001' && LA37_36 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_36=='\\') ) {s = 33;}

                        else if ( (LA37_36=='+') ) {s = 34;}

                        else if ( (LA37_36=='-') ) {s = 35;}

                        else if ( (LA37_36=='\u2013') ) {s = 36;}

                        else if ( (LA37_36=='=') ) {s = 37;}

                        else if ( (LA37_36=='#') ) {s = 38;}

                        else if ( (LA37_36=='*'||LA37_36=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA37_111 = input.LA(1);

                        s = -1;
                        if ( (LA37_111=='(') ) {s = 39;}

                        else if ( ((LA37_111 >= '\u0000' && LA37_111 <= '\b')||(LA37_111 >= '\u000B' && LA37_111 <= '\f')||(LA37_111 >= '\u000E' && LA37_111 <= '\u001F')||(LA37_111 >= '$' && LA37_111 <= '&')||(LA37_111 >= '.' && LA37_111 <= '9')||LA37_111=='<'||LA37_111=='>'||(LA37_111 >= '@' && LA37_111 <= 'Z')||(LA37_111 >= '_' && LA37_111 <= 'z')||LA37_111=='|'||(LA37_111 >= '\u007F' && LA37_111 <= '\u2012')||(LA37_111 >= '\u2014' && LA37_111 <= '\u2FFF')||(LA37_111 >= '\u3001' && LA37_111 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_111=='\\') ) {s = 33;}

                        else if ( (LA37_111=='+') ) {s = 34;}

                        else if ( (LA37_111=='-') ) {s = 35;}

                        else if ( (LA37_111=='\u2013') ) {s = 36;}

                        else if ( (LA37_111=='=') ) {s = 37;}

                        else if ( (LA37_111=='#') ) {s = 38;}

                        else if ( (LA37_111=='*'||LA37_111=='?') ) {s = 40;}

                        else s = 43;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA37_81 = input.LA(1);

                        s = -1;
                        if ( (LA37_81==':') ) {s = 96;}

                        else if ( (LA37_81=='(') ) {s = 39;}

                        else if ( ((LA37_81 >= '\u0000' && LA37_81 <= '\b')||(LA37_81 >= '\u000B' && LA37_81 <= '\f')||(LA37_81 >= '\u000E' && LA37_81 <= '\u001F')||(LA37_81 >= '$' && LA37_81 <= '&')||(LA37_81 >= '.' && LA37_81 <= '9')||LA37_81=='<'||LA37_81=='>'||(LA37_81 >= '@' && LA37_81 <= 'Z')||(LA37_81 >= '_' && LA37_81 <= 'z')||LA37_81=='|'||(LA37_81 >= '\u007F' && LA37_81 <= '\u2012')||(LA37_81 >= '\u2014' && LA37_81 <= '\u2FFF')||(LA37_81 >= '\u3001' && LA37_81 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_81=='\\') ) {s = 33;}

                        else if ( (LA37_81=='+') ) {s = 34;}

                        else if ( (LA37_81=='-') ) {s = 35;}

                        else if ( (LA37_81=='\u2013') ) {s = 36;}

                        else if ( (LA37_81=='=') ) {s = 37;}

                        else if ( (LA37_81=='#') ) {s = 38;}

                        else if ( (LA37_81=='*'||LA37_81=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA37_67 = input.LA(1);

                        s = -1;
                        if ( (LA37_67=='(') ) {s = 39;}

                        else if ( ((LA37_67 >= '\u0000' && LA37_67 <= '\b')||(LA37_67 >= '\u000B' && LA37_67 <= '\f')||(LA37_67 >= '\u000E' && LA37_67 <= '\u001F')||(LA37_67 >= '$' && LA37_67 <= '&')||(LA37_67 >= '.' && LA37_67 <= '9')||LA37_67=='<'||LA37_67=='>'||(LA37_67 >= '@' && LA37_67 <= 'Z')||(LA37_67 >= '_' && LA37_67 <= 'z')||LA37_67=='|'||(LA37_67 >= '\u007F' && LA37_67 <= '\u2012')||(LA37_67 >= '\u2014' && LA37_67 <= '\u2FFF')||(LA37_67 >= '\u3001' && LA37_67 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_67=='\\') ) {s = 33;}

                        else if ( (LA37_67=='+') ) {s = 34;}

                        else if ( (LA37_67=='-') ) {s = 35;}

                        else if ( (LA37_67=='\u2013') ) {s = 36;}

                        else if ( (LA37_67=='=') ) {s = 37;}

                        else if ( (LA37_67=='#') ) {s = 38;}

                        else if ( (LA37_67=='*'||LA37_67=='?') ) {s = 40;}

                        else s = 82;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA37_14 = input.LA(1);

                        s = -1;
                        if ( ((LA37_14 >= '\u0000' && LA37_14 <= '\u001F')||(LA37_14 >= '!' && LA37_14 <= '#')||(LA37_14 >= '%' && LA37_14 <= '*')||(LA37_14 >= '.' && LA37_14 <= '/')||(LA37_14 >= ':' && LA37_14 <= '\u2012')||(LA37_14 >= '\u2014' && LA37_14 <= '\uFFFF')) ) {s = 47;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA37_55 = input.LA(1);

                        s = -1;
                        if ( (LA37_55=='(') ) {s = 39;}

                        else if ( ((LA37_55 >= '\u0000' && LA37_55 <= '\b')||(LA37_55 >= '\u000B' && LA37_55 <= '\f')||(LA37_55 >= '\u000E' && LA37_55 <= '\u001F')||(LA37_55 >= '$' && LA37_55 <= '&')||(LA37_55 >= '.' && LA37_55 <= '9')||LA37_55=='<'||LA37_55=='>'||(LA37_55 >= '@' && LA37_55 <= 'Z')||(LA37_55 >= '_' && LA37_55 <= 'z')||LA37_55=='|'||(LA37_55 >= '\u007F' && LA37_55 <= '\u2012')||(LA37_55 >= '\u2014' && LA37_55 <= '\u2FFF')||(LA37_55 >= '\u3001' && LA37_55 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_55=='\\') ) {s = 33;}

                        else if ( (LA37_55=='+') ) {s = 34;}

                        else if ( (LA37_55=='-') ) {s = 35;}

                        else if ( (LA37_55=='\u2013') ) {s = 36;}

                        else if ( (LA37_55=='=') ) {s = 37;}

                        else if ( (LA37_55=='#') ) {s = 38;}

                        else if ( (LA37_55=='*'||LA37_55=='?') ) {s = 40;}

                        else s = 74;

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA37_86 = input.LA(1);

                        s = -1;
                        if ( (LA37_86=='v') ) {s = 98;}

                        else if ( (LA37_86=='(') ) {s = 39;}

                        else if ( ((LA37_86 >= '\u0000' && LA37_86 <= '\b')||(LA37_86 >= '\u000B' && LA37_86 <= '\f')||(LA37_86 >= '\u000E' && LA37_86 <= '\u001F')||(LA37_86 >= '$' && LA37_86 <= '&')||(LA37_86 >= '.' && LA37_86 <= '9')||LA37_86=='<'||LA37_86=='>'||(LA37_86 >= '@' && LA37_86 <= 'Z')||(LA37_86 >= '_' && LA37_86 <= 'u')||(LA37_86 >= 'w' && LA37_86 <= 'z')||LA37_86=='|'||(LA37_86 >= '\u007F' && LA37_86 <= '\u2012')||(LA37_86 >= '\u2014' && LA37_86 <= '\u2FFF')||(LA37_86 >= '\u3001' && LA37_86 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_86=='\\') ) {s = 33;}

                        else if ( (LA37_86=='+') ) {s = 34;}

                        else if ( (LA37_86=='-') ) {s = 35;}

                        else if ( (LA37_86=='\u2013') ) {s = 36;}

                        else if ( (LA37_86=='=') ) {s = 37;}

                        else if ( (LA37_86=='#') ) {s = 38;}

                        else if ( (LA37_86=='*'||LA37_86=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA37_73 = input.LA(1);

                        s = -1;
                        if ( (LA37_73=='i') ) {s = 86;}

                        else if ( (LA37_73=='(') ) {s = 39;}

                        else if ( ((LA37_73 >= '\u0000' && LA37_73 <= '\b')||(LA37_73 >= '\u000B' && LA37_73 <= '\f')||(LA37_73 >= '\u000E' && LA37_73 <= '\u001F')||(LA37_73 >= '$' && LA37_73 <= '&')||(LA37_73 >= '.' && LA37_73 <= '9')||LA37_73=='<'||LA37_73=='>'||(LA37_73 >= '@' && LA37_73 <= 'Z')||(LA37_73 >= '_' && LA37_73 <= 'h')||(LA37_73 >= 'j' && LA37_73 <= 'z')||LA37_73=='|'||(LA37_73 >= '\u007F' && LA37_73 <= '\u2012')||(LA37_73 >= '\u2014' && LA37_73 <= '\u2FFF')||(LA37_73 >= '\u3001' && LA37_73 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_73=='\\') ) {s = 33;}

                        else if ( (LA37_73=='+') ) {s = 34;}

                        else if ( (LA37_73=='-') ) {s = 35;}

                        else if ( (LA37_73=='\u2013') ) {s = 36;}

                        else if ( (LA37_73=='=') ) {s = 37;}

                        else if ( (LA37_73=='#') ) {s = 38;}

                        else if ( (LA37_73=='*'||LA37_73=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA37_25 = input.LA(1);

                        s = -1;
                        if ( ((LA37_25 >= '0' && LA37_25 <= '9')) ) {s = 59;}

                        else if ( (LA37_25=='.') ) {s = 60;}

                        else if ( (LA37_25=='\\') ) {s = 33;}

                        else if ( (LA37_25=='+') ) {s = 34;}

                        else if ( (LA37_25=='-') ) {s = 61;}

                        else if ( (LA37_25=='\u2013') ) {s = 62;}

                        else if ( (LA37_25=='=') ) {s = 37;}

                        else if ( (LA37_25=='#') ) {s = 38;}

                        else if ( (LA37_25=='(') ) {s = 39;}

                        else if ( ((LA37_25 >= '\u0000' && LA37_25 <= '\b')||(LA37_25 >= '\u000B' && LA37_25 <= '\f')||(LA37_25 >= '\u000E' && LA37_25 <= '\u001F')||(LA37_25 >= '$' && LA37_25 <= '&')||LA37_25=='<'||LA37_25=='>'||(LA37_25 >= '@' && LA37_25 <= 'Z')||(LA37_25 >= '_' && LA37_25 <= 'z')||LA37_25=='|'||(LA37_25 >= '\u007F' && LA37_25 <= '\u2012')||(LA37_25 >= '\u2014' && LA37_25 <= '\u2FFF')||(LA37_25 >= '\u3001' && LA37_25 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_25=='/') ) {s = 63;}

                        else if ( (LA37_25=='*'||LA37_25=='?') ) {s = 40;}

                        else s = 58;

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA37_91 = input.LA(1);

                        s = -1;
                        if ( (LA37_91=='(') ) {s = 39;}

                        else if ( ((LA37_91 >= '.' && LA37_91 <= '/')) ) {s = 94;}

                        else if ( (LA37_91=='\\') ) {s = 33;}

                        else if ( (LA37_91=='+') ) {s = 34;}

                        else if ( (LA37_91=='-') ) {s = 92;}

                        else if ( (LA37_91=='\u2013') ) {s = 93;}

                        else if ( (LA37_91=='=') ) {s = 37;}

                        else if ( (LA37_91=='#') ) {s = 38;}

                        else if ( ((LA37_91 >= '0' && LA37_91 <= '9')) ) {s = 102;}

                        else if ( ((LA37_91 >= '\u0000' && LA37_91 <= '\b')||(LA37_91 >= '\u000B' && LA37_91 <= '\f')||(LA37_91 >= '\u000E' && LA37_91 <= '\u001F')||(LA37_91 >= '$' && LA37_91 <= '&')||LA37_91=='<'||LA37_91=='>'||(LA37_91 >= '@' && LA37_91 <= 'Z')||(LA37_91 >= '_' && LA37_91 <= 'z')||LA37_91=='|'||(LA37_91 >= '\u007F' && LA37_91 <= '\u2012')||(LA37_91 >= '\u2014' && LA37_91 <= '\u2FFF')||(LA37_91 >= '\u3001' && LA37_91 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_91=='*'||LA37_91=='?') ) {s = 40;}

                        else s = 58;

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA37_64 = input.LA(1);

                        s = -1;
                        if ( (LA37_64=='i') ) {s = 81;}

                        else if ( (LA37_64=='(') ) {s = 39;}

                        else if ( ((LA37_64 >= '\u0000' && LA37_64 <= '\b')||(LA37_64 >= '\u000B' && LA37_64 <= '\f')||(LA37_64 >= '\u000E' && LA37_64 <= '\u001F')||(LA37_64 >= '$' && LA37_64 <= '&')||(LA37_64 >= '.' && LA37_64 <= '9')||LA37_64=='<'||LA37_64=='>'||(LA37_64 >= '@' && LA37_64 <= 'Z')||(LA37_64 >= '_' && LA37_64 <= 'h')||(LA37_64 >= 'j' && LA37_64 <= 'z')||LA37_64=='|'||(LA37_64 >= '\u007F' && LA37_64 <= '\u2012')||(LA37_64 >= '\u2014' && LA37_64 <= '\u2FFF')||(LA37_64 >= '\u3001' && LA37_64 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_64=='\\') ) {s = 33;}

                        else if ( (LA37_64=='+') ) {s = 34;}

                        else if ( (LA37_64=='-') ) {s = 35;}

                        else if ( (LA37_64=='\u2013') ) {s = 36;}

                        else if ( (LA37_64=='=') ) {s = 37;}

                        else if ( (LA37_64=='#') ) {s = 38;}

                        else if ( (LA37_64=='*'||LA37_64=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA37_89 = input.LA(1);

                        s = -1;
                        if ( (LA37_89=='-') ) {s = 100;}

                        else if ( (LA37_89=='(') ) {s = 39;}

                        else if ( (LA37_89=='.') ) {s = 90;}

                        else if ( (LA37_89=='\\') ) {s = 33;}

                        else if ( (LA37_89=='+') ) {s = 34;}

                        else if ( (LA37_89=='\u2013') ) {s = 36;}

                        else if ( (LA37_89=='=') ) {s = 37;}

                        else if ( (LA37_89=='#') ) {s = 38;}

                        else if ( ((LA37_89 >= '0' && LA37_89 <= '9')) ) {s = 101;}

                        else if ( ((LA37_89 >= '\u0000' && LA37_89 <= '\b')||(LA37_89 >= '\u000B' && LA37_89 <= '\f')||(LA37_89 >= '\u000E' && LA37_89 <= '\u001F')||(LA37_89 >= '$' && LA37_89 <= '&')||LA37_89=='/'||LA37_89=='<'||LA37_89=='>'||(LA37_89 >= '@' && LA37_89 <= 'Z')||(LA37_89 >= '_' && LA37_89 <= 'z')||LA37_89=='|'||(LA37_89 >= '\u007F' && LA37_89 <= '\u2012')||(LA37_89 >= '\u2014' && LA37_89 <= '\u2FFF')||(LA37_89 >= '\u3001' && LA37_89 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_89=='*'||LA37_89=='?') ) {s = 40;}

                        else s = 58;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA37_66 = input.LA(1);

                        s = -1;
                        if ( (LA37_66=='(') ) {s = 39;}

                        else if ( ((LA37_66 >= '\u0000' && LA37_66 <= '\b')||(LA37_66 >= '\u000B' && LA37_66 <= '\f')||(LA37_66 >= '\u000E' && LA37_66 <= '\u001F')||(LA37_66 >= '$' && LA37_66 <= '&')||(LA37_66 >= '.' && LA37_66 <= '9')||LA37_66=='<'||LA37_66=='>'||(LA37_66 >= '@' && LA37_66 <= 'Z')||(LA37_66 >= '_' && LA37_66 <= 'z')||LA37_66=='|'||(LA37_66 >= '\u007F' && LA37_66 <= '\u2012')||(LA37_66 >= '\u2014' && LA37_66 <= '\u2FFF')||(LA37_66 >= '\u3001' && LA37_66 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_66=='\\') ) {s = 33;}

                        else if ( (LA37_66=='+') ) {s = 34;}

                        else if ( (LA37_66=='-') ) {s = 35;}

                        else if ( (LA37_66=='\u2013') ) {s = 36;}

                        else if ( (LA37_66=='=') ) {s = 37;}

                        else if ( (LA37_66=='#') ) {s = 38;}

                        else if ( (LA37_66=='*'||LA37_66=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA37_108 = input.LA(1);

                        s = -1;
                        if ( (LA37_108=='(') ) {s = 39;}

                        else if ( ((LA37_108 >= '0' && LA37_108 <= '9')) ) {s = 110;}

                        else if ( (LA37_108=='\\') ) {s = 33;}

                        else if ( (LA37_108=='+') ) {s = 34;}

                        else if ( (LA37_108=='-') ) {s = 35;}

                        else if ( (LA37_108=='\u2013') ) {s = 36;}

                        else if ( (LA37_108=='=') ) {s = 37;}

                        else if ( (LA37_108=='#') ) {s = 38;}

                        else if ( ((LA37_108 >= '\u0000' && LA37_108 <= '\b')||(LA37_108 >= '\u000B' && LA37_108 <= '\f')||(LA37_108 >= '\u000E' && LA37_108 <= '\u001F')||(LA37_108 >= '$' && LA37_108 <= '&')||(LA37_108 >= '.' && LA37_108 <= '/')||LA37_108=='<'||LA37_108=='>'||(LA37_108 >= '@' && LA37_108 <= 'Z')||(LA37_108 >= '_' && LA37_108 <= 'z')||LA37_108=='|'||(LA37_108 >= '\u007F' && LA37_108 <= '\u2012')||(LA37_108 >= '\u2014' && LA37_108 <= '\u2FFF')||(LA37_108 >= '\u3001' && LA37_108 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_108=='*'||LA37_108=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA37_72 = input.LA(1);

                        s = -1;
                        if ( (LA37_72=='i') ) {s = 85;}

                        else if ( (LA37_72=='(') ) {s = 39;}

                        else if ( ((LA37_72 >= '\u0000' && LA37_72 <= '\b')||(LA37_72 >= '\u000B' && LA37_72 <= '\f')||(LA37_72 >= '\u000E' && LA37_72 <= '\u001F')||(LA37_72 >= '$' && LA37_72 <= '&')||(LA37_72 >= '.' && LA37_72 <= '9')||LA37_72=='<'||LA37_72=='>'||(LA37_72 >= '@' && LA37_72 <= 'Z')||(LA37_72 >= '_' && LA37_72 <= 'h')||(LA37_72 >= 'j' && LA37_72 <= 'z')||LA37_72=='|'||(LA37_72 >= '\u007F' && LA37_72 <= '\u2012')||(LA37_72 >= '\u2014' && LA37_72 <= '\u2FFF')||(LA37_72 >= '\u3001' && LA37_72 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_72=='\\') ) {s = 33;}

                        else if ( (LA37_72=='+') ) {s = 34;}

                        else if ( (LA37_72=='-') ) {s = 35;}

                        else if ( (LA37_72=='\u2013') ) {s = 36;}

                        else if ( (LA37_72=='=') ) {s = 37;}

                        else if ( (LA37_72=='#') ) {s = 38;}

                        else if ( (LA37_72=='*'||LA37_72=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA37_12 = input.LA(1);

                        s = -1;
                        if ( ((LA37_12 >= '\u0000' && LA37_12 <= '\b')||(LA37_12 >= '\u000B' && LA37_12 <= '\f')||(LA37_12 >= '\u000E' && LA37_12 <= '\u001F')||(LA37_12 >= '#' && LA37_12 <= '&')||LA37_12=='+'||(LA37_12 >= '-' && LA37_12 <= '9')||(LA37_12 >= '<' && LA37_12 <= '>')||(LA37_12 >= '@' && LA37_12 <= 'Z')||LA37_12=='\\'||(LA37_12 >= '_' && LA37_12 <= 'z')||LA37_12=='|'||(LA37_12 >= '\u007F' && LA37_12 <= '\u2FFF')||(LA37_12 >= '\u3001' && LA37_12 <= '\uFFFF')) ) {s = 40;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA37_52 = input.LA(1);

                        s = -1;
                        if ( (LA37_52=='(') ) {s = 39;}

                        else if ( ((LA37_52 >= '\u0000' && LA37_52 <= '\b')||(LA37_52 >= '\u000B' && LA37_52 <= '\f')||(LA37_52 >= '\u000E' && LA37_52 <= '\u001F')||(LA37_52 >= '$' && LA37_52 <= '&')||(LA37_52 >= '.' && LA37_52 <= '9')||LA37_52=='<'||LA37_52=='>'||(LA37_52 >= '@' && LA37_52 <= 'Z')||(LA37_52 >= '_' && LA37_52 <= 'z')||LA37_52=='|'||(LA37_52 >= '\u007F' && LA37_52 <= '\u2012')||(LA37_52 >= '\u2014' && LA37_52 <= '\u2FFF')||(LA37_52 >= '\u3001' && LA37_52 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_52=='\\') ) {s = 33;}

                        else if ( (LA37_52=='+') ) {s = 34;}

                        else if ( (LA37_52=='-') ) {s = 35;}

                        else if ( (LA37_52=='\u2013') ) {s = 36;}

                        else if ( (LA37_52=='=') ) {s = 37;}

                        else if ( (LA37_52=='#') ) {s = 38;}

                        else if ( (LA37_52=='*'||LA37_52=='?') ) {s = 40;}

                        else s = 70;

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA37_85 = input.LA(1);

                        s = -1;
                        if ( (LA37_85=='v') ) {s = 97;}

                        else if ( (LA37_85=='(') ) {s = 39;}

                        else if ( ((LA37_85 >= '\u0000' && LA37_85 <= '\b')||(LA37_85 >= '\u000B' && LA37_85 <= '\f')||(LA37_85 >= '\u000E' && LA37_85 <= '\u001F')||(LA37_85 >= '$' && LA37_85 <= '&')||(LA37_85 >= '.' && LA37_85 <= '9')||LA37_85=='<'||LA37_85=='>'||(LA37_85 >= '@' && LA37_85 <= 'Z')||(LA37_85 >= '_' && LA37_85 <= 'u')||(LA37_85 >= 'w' && LA37_85 <= 'z')||LA37_85=='|'||(LA37_85 >= '\u007F' && LA37_85 <= '\u2012')||(LA37_85 >= '\u2014' && LA37_85 <= '\u2FFF')||(LA37_85 >= '\u3001' && LA37_85 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_85=='\\') ) {s = 33;}

                        else if ( (LA37_85=='+') ) {s = 34;}

                        else if ( (LA37_85=='-') ) {s = 35;}

                        else if ( (LA37_85=='\u2013') ) {s = 36;}

                        else if ( (LA37_85=='=') ) {s = 37;}

                        else if ( (LA37_85=='#') ) {s = 38;}

                        else if ( (LA37_85=='*'||LA37_85=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA37_103 = input.LA(1);

                        s = -1;
                        if ( (LA37_103=='(') ) {s = 39;}

                        else if ( ((LA37_103 >= '0' && LA37_103 <= '9')) ) {s = 105;}

                        else if ( (LA37_103=='\\') ) {s = 33;}

                        else if ( (LA37_103=='+') ) {s = 34;}

                        else if ( (LA37_103=='-') ) {s = 35;}

                        else if ( (LA37_103=='\u2013') ) {s = 36;}

                        else if ( (LA37_103=='=') ) {s = 37;}

                        else if ( (LA37_103=='#') ) {s = 38;}

                        else if ( ((LA37_103 >= '\u0000' && LA37_103 <= '\b')||(LA37_103 >= '\u000B' && LA37_103 <= '\f')||(LA37_103 >= '\u000E' && LA37_103 <= '\u001F')||(LA37_103 >= '$' && LA37_103 <= '&')||(LA37_103 >= '.' && LA37_103 <= '/')||LA37_103=='<'||LA37_103=='>'||(LA37_103 >= '@' && LA37_103 <= 'Z')||(LA37_103 >= '_' && LA37_103 <= 'z')||LA37_103=='|'||(LA37_103 >= '\u007F' && LA37_103 <= '\u2012')||(LA37_103 >= '\u2014' && LA37_103 <= '\u2FFF')||(LA37_103 >= '\u3001' && LA37_103 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_103=='*'||LA37_103=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA37_101 = input.LA(1);

                        s = -1;
                        if ( (LA37_101=='(') ) {s = 39;}

                        else if ( (LA37_101=='.') ) {s = 90;}

                        else if ( (LA37_101=='\\') ) {s = 33;}

                        else if ( (LA37_101=='+') ) {s = 34;}

                        else if ( (LA37_101=='-') ) {s = 35;}

                        else if ( (LA37_101=='\u2013') ) {s = 36;}

                        else if ( (LA37_101=='=') ) {s = 37;}

                        else if ( (LA37_101=='#') ) {s = 38;}

                        else if ( ((LA37_101 >= '0' && LA37_101 <= '9')) ) {s = 101;}

                        else if ( ((LA37_101 >= '\u0000' && LA37_101 <= '\b')||(LA37_101 >= '\u000B' && LA37_101 <= '\f')||(LA37_101 >= '\u000E' && LA37_101 <= '\u001F')||(LA37_101 >= '$' && LA37_101 <= '&')||LA37_101=='/'||LA37_101=='<'||LA37_101=='>'||(LA37_101 >= '@' && LA37_101 <= 'Z')||(LA37_101 >= '_' && LA37_101 <= 'z')||LA37_101=='|'||(LA37_101 >= '\u007F' && LA37_101 <= '\u2012')||(LA37_101 >= '\u2014' && LA37_101 <= '\u2FFF')||(LA37_101 >= '\u3001' && LA37_101 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_101=='*'||LA37_101=='?') ) {s = 40;}

                        else s = 58;

                        if ( s>=0 ) return s;
                        break;

                    case 27 : 
                        int LA37_27 = input.LA(1);

                        s = -1;
                        if ( (LA37_27=='o') ) {s = 64;}

                        else if ( ((LA37_27 >= '\u0000' && LA37_27 <= '\b')||(LA37_27 >= '\u000B' && LA37_27 <= '\f')||(LA37_27 >= '\u000E' && LA37_27 <= '\u001F')||(LA37_27 >= '$' && LA37_27 <= '&')||(LA37_27 >= '.' && LA37_27 <= '9')||LA37_27=='<'||LA37_27=='>'||(LA37_27 >= '@' && LA37_27 <= 'Z')||(LA37_27 >= '_' && LA37_27 <= 'n')||(LA37_27 >= 'p' && LA37_27 <= 'z')||LA37_27=='|'||(LA37_27 >= '\u007F' && LA37_27 <= '\u2012')||(LA37_27 >= '\u2014' && LA37_27 <= '\u2FFF')||(LA37_27 >= '\u3001' && LA37_27 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_27=='\\') ) {s = 33;}

                        else if ( (LA37_27=='+') ) {s = 34;}

                        else if ( (LA37_27=='-') ) {s = 35;}

                        else if ( (LA37_27=='\u2013') ) {s = 36;}

                        else if ( (LA37_27=='=') ) {s = 37;}

                        else if ( (LA37_27=='#') ) {s = 38;}

                        else if ( (LA37_27=='(') ) {s = 39;}

                        else if ( (LA37_27=='*'||LA37_27=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 28 : 
                        int LA37_59 = input.LA(1);

                        s = -1;
                        if ( ((LA37_59 >= '0' && LA37_59 <= '9')) ) {s = 77;}

                        else if ( (LA37_59=='(') ) {s = 39;}

                        else if ( (LA37_59=='.') ) {s = 60;}

                        else if ( (LA37_59=='\\') ) {s = 33;}

                        else if ( (LA37_59=='+') ) {s = 34;}

                        else if ( (LA37_59=='-') ) {s = 61;}

                        else if ( (LA37_59=='\u2013') ) {s = 62;}

                        else if ( (LA37_59=='=') ) {s = 37;}

                        else if ( (LA37_59=='#') ) {s = 38;}

                        else if ( ((LA37_59 >= '\u0000' && LA37_59 <= '\b')||(LA37_59 >= '\u000B' && LA37_59 <= '\f')||(LA37_59 >= '\u000E' && LA37_59 <= '\u001F')||(LA37_59 >= '$' && LA37_59 <= '&')||LA37_59=='<'||LA37_59=='>'||(LA37_59 >= '@' && LA37_59 <= 'Z')||(LA37_59 >= '_' && LA37_59 <= 'z')||LA37_59=='|'||(LA37_59 >= '\u007F' && LA37_59 <= '\u2012')||(LA37_59 >= '\u2014' && LA37_59 <= '\u2FFF')||(LA37_59 >= '\u3001' && LA37_59 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_59=='/') ) {s = 63;}

                        else if ( (LA37_59==':') ) {s = 78;}

                        else if ( (LA37_59=='*'||LA37_59=='?') ) {s = 40;}

                        else s = 58;

                        if ( s>=0 ) return s;
                        break;

                    case 29 : 
                        int LA37_33 = input.LA(1);

                        s = -1;
                        if ( ((LA37_33 >= '\u0000' && LA37_33 <= '\uFFFF')) ) {s = 66;}

                        if ( s>=0 ) return s;
                        break;

                    case 30 : 
                        int LA37_29 = input.LA(1);

                        s = -1;
                        if ( ((LA37_29 >= '\u0000' && LA37_29 <= '\uFFFF')) ) {s = 65;}

                        if ( s>=0 ) return s;
                        break;

                    case 31 : 
                        int LA37_60 = input.LA(1);

                        s = -1;
                        if ( (LA37_60=='(') ) {s = 39;}

                        else if ( ((LA37_60 >= '0' && LA37_60 <= '9')) ) {s = 79;}

                        else if ( (LA37_60=='\\') ) {s = 33;}

                        else if ( (LA37_60=='+') ) {s = 34;}

                        else if ( (LA37_60=='-') ) {s = 35;}

                        else if ( (LA37_60=='\u2013') ) {s = 36;}

                        else if ( (LA37_60=='=') ) {s = 37;}

                        else if ( (LA37_60=='#') ) {s = 38;}

                        else if ( ((LA37_60 >= '\u0000' && LA37_60 <= '\b')||(LA37_60 >= '\u000B' && LA37_60 <= '\f')||(LA37_60 >= '\u000E' && LA37_60 <= '\u001F')||(LA37_60 >= '$' && LA37_60 <= '&')||(LA37_60 >= '.' && LA37_60 <= '/')||LA37_60=='<'||LA37_60=='>'||(LA37_60 >= '@' && LA37_60 <= 'Z')||(LA37_60 >= '_' && LA37_60 <= 'z')||LA37_60=='|'||(LA37_60 >= '\u007F' && LA37_60 <= '\u2012')||(LA37_60 >= '\u2014' && LA37_60 <= '\u2FFF')||(LA37_60 >= '\u3001' && LA37_60 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_60=='*'||LA37_60=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 32 : 
                        int LA37_3 = input.LA(1);

                        s = -1;
                        if ( (LA37_3=='=') ) {s = 41;}

                        else if ( ((LA37_3 >= '\u0000' && LA37_3 <= '\b')||(LA37_3 >= '\u000B' && LA37_3 <= '\f')||(LA37_3 >= '\u000E' && LA37_3 <= '\u001F')||(LA37_3 >= '$' && LA37_3 <= '&')||(LA37_3 >= '.' && LA37_3 <= '9')||LA37_3=='<'||LA37_3=='>'||(LA37_3 >= '@' && LA37_3 <= 'Z')||(LA37_3 >= '_' && LA37_3 <= 'z')||LA37_3=='|'||(LA37_3 >= '\u007F' && LA37_3 <= '\u2012')||(LA37_3 >= '\u2014' && LA37_3 <= '\u2FFF')||(LA37_3 >= '\u3001' && LA37_3 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_3=='\\') ) {s = 33;}

                        else if ( (LA37_3=='+') ) {s = 34;}

                        else if ( (LA37_3=='-') ) {s = 35;}

                        else if ( (LA37_3=='\u2013') ) {s = 36;}

                        else if ( (LA37_3=='#') ) {s = 38;}

                        else if ( (LA37_3=='(') ) {s = 39;}

                        else if ( (LA37_3=='*'||LA37_3=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 33 : 
                        int LA37_100 = input.LA(1);

                        s = -1;
                        if ( ((LA37_100 >= '0' && LA37_100 <= '9')) ) {s = 104;}

                        else if ( (LA37_100=='(') ) {s = 39;}

                        else if ( ((LA37_100 >= '\u0000' && LA37_100 <= '\b')||(LA37_100 >= '\u000B' && LA37_100 <= '\f')||(LA37_100 >= '\u000E' && LA37_100 <= '\u001F')||(LA37_100 >= '$' && LA37_100 <= '&')||(LA37_100 >= '.' && LA37_100 <= '/')||LA37_100=='<'||LA37_100=='>'||(LA37_100 >= '@' && LA37_100 <= 'Z')||(LA37_100 >= '_' && LA37_100 <= 'z')||LA37_100=='|'||(LA37_100 >= '\u007F' && LA37_100 <= '\u2012')||(LA37_100 >= '\u2014' && LA37_100 <= '\u2FFF')||(LA37_100 >= '\u3001' && LA37_100 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_100=='\\') ) {s = 33;}

                        else if ( (LA37_100=='+') ) {s = 34;}

                        else if ( (LA37_100=='-') ) {s = 35;}

                        else if ( (LA37_100=='\u2013') ) {s = 36;}

                        else if ( (LA37_100=='=') ) {s = 37;}

                        else if ( (LA37_100=='#') ) {s = 38;}

                        else if ( (LA37_100=='*'||LA37_100=='?') ) {s = 40;}

                        else s = 43;

                        if ( s>=0 ) return s;
                        break;

                    case 34 : 
                        int LA37_61 = input.LA(1);

                        s = -1;
                        if ( (LA37_61=='(') ) {s = 39;}

                        else if ( ((LA37_61 >= '0' && LA37_61 <= '9')) ) {s = 80;}

                        else if ( (LA37_61=='\\') ) {s = 33;}

                        else if ( (LA37_61=='+') ) {s = 34;}

                        else if ( (LA37_61=='-') ) {s = 35;}

                        else if ( (LA37_61=='\u2013') ) {s = 36;}

                        else if ( (LA37_61=='=') ) {s = 37;}

                        else if ( (LA37_61=='#') ) {s = 38;}

                        else if ( ((LA37_61 >= '\u0000' && LA37_61 <= '\b')||(LA37_61 >= '\u000B' && LA37_61 <= '\f')||(LA37_61 >= '\u000E' && LA37_61 <= '\u001F')||(LA37_61 >= '$' && LA37_61 <= '&')||(LA37_61 >= '.' && LA37_61 <= '/')||LA37_61=='<'||LA37_61=='>'||(LA37_61 >= '@' && LA37_61 <= 'Z')||(LA37_61 >= '_' && LA37_61 <= 'z')||LA37_61=='|'||(LA37_61 >= '\u007F' && LA37_61 <= '\u2012')||(LA37_61 >= '\u2014' && LA37_61 <= '\u2FFF')||(LA37_61 >= '\u3001' && LA37_61 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_61=='*'||LA37_61=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 35 : 
                        int LA37_65 = input.LA(1);

                        s = -1;
                        if ( ((LA37_65 >= '\u0000' && LA37_65 <= '\b')||(LA37_65 >= '\u000B' && LA37_65 <= '\f')||(LA37_65 >= '\u000E' && LA37_65 <= '\u001F')||(LA37_65 >= '$' && LA37_65 <= '&')||(LA37_65 >= '.' && LA37_65 <= '9')||LA37_65=='<'||LA37_65=='>'||(LA37_65 >= '@' && LA37_65 <= 'Z')||(LA37_65 >= '_' && LA37_65 <= 'z')||LA37_65=='|'||(LA37_65 >= '\u007F' && LA37_65 <= '\u2012')||(LA37_65 >= '\u2014' && LA37_65 <= '\u2FFF')||(LA37_65 >= '\u3001' && LA37_65 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_65=='\\') ) {s = 33;}

                        else if ( (LA37_65=='+') ) {s = 34;}

                        else if ( (LA37_65=='-') ) {s = 35;}

                        else if ( (LA37_65=='\u2013') ) {s = 36;}

                        else if ( (LA37_65=='=') ) {s = 37;}

                        else if ( (LA37_65=='#') ) {s = 38;}

                        else if ( (LA37_65=='(') ) {s = 39;}

                        else if ( (LA37_65=='*'||LA37_65=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 36 : 
                        int LA37_54 = input.LA(1);

                        s = -1;
                        if ( (LA37_54=='X') ) {s = 72;}

                        else if ( (LA37_54=='x') ) {s = 73;}

                        else if ( (LA37_54=='(') ) {s = 39;}

                        else if ( ((LA37_54 >= '\u0000' && LA37_54 <= '\b')||(LA37_54 >= '\u000B' && LA37_54 <= '\f')||(LA37_54 >= '\u000E' && LA37_54 <= '\u001F')||(LA37_54 >= '$' && LA37_54 <= '&')||(LA37_54 >= '.' && LA37_54 <= '9')||LA37_54=='<'||LA37_54=='>'||(LA37_54 >= '@' && LA37_54 <= 'W')||(LA37_54 >= 'Y' && LA37_54 <= 'Z')||(LA37_54 >= '_' && LA37_54 <= 'w')||(LA37_54 >= 'y' && LA37_54 <= 'z')||LA37_54=='|'||(LA37_54 >= '\u007F' && LA37_54 <= '\u2012')||(LA37_54 >= '\u2014' && LA37_54 <= '\u2FFF')||(LA37_54 >= '\u3001' && LA37_54 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_54=='\\') ) {s = 33;}

                        else if ( (LA37_54=='+') ) {s = 34;}

                        else if ( (LA37_54=='-') ) {s = 35;}

                        else if ( (LA37_54=='\u2013') ) {s = 36;}

                        else if ( (LA37_54=='=') ) {s = 37;}

                        else if ( (LA37_54=='#') ) {s = 38;}

                        else if ( (LA37_54=='*'||LA37_54=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 37 : 
                        int LA37_97 = input.LA(1);

                        s = -1;
                        if ( (LA37_97==':') ) {s = 96;}

                        else if ( (LA37_97=='(') ) {s = 39;}

                        else if ( ((LA37_97 >= '\u0000' && LA37_97 <= '\b')||(LA37_97 >= '\u000B' && LA37_97 <= '\f')||(LA37_97 >= '\u000E' && LA37_97 <= '\u001F')||(LA37_97 >= '$' && LA37_97 <= '&')||(LA37_97 >= '.' && LA37_97 <= '9')||LA37_97=='<'||LA37_97=='>'||(LA37_97 >= '@' && LA37_97 <= 'Z')||(LA37_97 >= '_' && LA37_97 <= 'z')||LA37_97=='|'||(LA37_97 >= '\u007F' && LA37_97 <= '\u2012')||(LA37_97 >= '\u2014' && LA37_97 <= '\u2FFF')||(LA37_97 >= '\u3001' && LA37_97 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_97=='\\') ) {s = 33;}

                        else if ( (LA37_97=='+') ) {s = 34;}

                        else if ( (LA37_97=='-') ) {s = 35;}

                        else if ( (LA37_97=='\u2013') ) {s = 36;}

                        else if ( (LA37_97=='=') ) {s = 37;}

                        else if ( (LA37_97=='#') ) {s = 38;}

                        else if ( (LA37_97=='*'||LA37_97=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 38 : 
                        int LA37_16 = input.LA(1);

                        s = -1;
                        if ( (LA37_16=='\\') ) {s = 49;}

                        else if ( ((LA37_16 >= '\u0000' && LA37_16 <= '!')||(LA37_16 >= '#' && LA37_16 <= ')')||(LA37_16 >= '+' && LA37_16 <= '>')||(LA37_16 >= '@' && LA37_16 <= '[')||(LA37_16 >= ']' && LA37_16 <= '\uFFFF')) ) {s = 50;}

                        else if ( (LA37_16=='*'||LA37_16=='?') ) {s = 51;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 39 : 
                        int LA37_35 = input.LA(1);

                        s = -1;
                        if ( (LA37_35=='(') ) {s = 39;}

                        else if ( ((LA37_35 >= '\u0000' && LA37_35 <= '\b')||(LA37_35 >= '\u000B' && LA37_35 <= '\f')||(LA37_35 >= '\u000E' && LA37_35 <= '\u001F')||(LA37_35 >= '$' && LA37_35 <= '&')||(LA37_35 >= '.' && LA37_35 <= '9')||LA37_35=='<'||LA37_35=='>'||(LA37_35 >= '@' && LA37_35 <= 'Z')||(LA37_35 >= '_' && LA37_35 <= 'z')||LA37_35=='|'||(LA37_35 >= '\u007F' && LA37_35 <= '\u2012')||(LA37_35 >= '\u2014' && LA37_35 <= '\u2FFF')||(LA37_35 >= '\u3001' && LA37_35 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_35=='\\') ) {s = 33;}

                        else if ( (LA37_35=='+') ) {s = 34;}

                        else if ( (LA37_35=='-') ) {s = 35;}

                        else if ( (LA37_35=='\u2013') ) {s = 36;}

                        else if ( (LA37_35=='=') ) {s = 37;}

                        else if ( (LA37_35=='#') ) {s = 38;}

                        else if ( (LA37_35=='*'||LA37_35=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 40 : 
                        int LA37_93 = input.LA(1);

                        s = -1;
                        if ( (LA37_93=='(') ) {s = 39;}

                        else if ( ((LA37_93 >= '0' && LA37_93 <= '9')) ) {s = 103;}

                        else if ( (LA37_93=='\\') ) {s = 33;}

                        else if ( (LA37_93=='+') ) {s = 34;}

                        else if ( (LA37_93=='-') ) {s = 35;}

                        else if ( (LA37_93=='\u2013') ) {s = 36;}

                        else if ( (LA37_93=='=') ) {s = 37;}

                        else if ( (LA37_93=='#') ) {s = 38;}

                        else if ( ((LA37_93 >= '\u0000' && LA37_93 <= '\b')||(LA37_93 >= '\u000B' && LA37_93 <= '\f')||(LA37_93 >= '\u000E' && LA37_93 <= '\u001F')||(LA37_93 >= '$' && LA37_93 <= '&')||(LA37_93 >= '.' && LA37_93 <= '/')||LA37_93=='<'||LA37_93=='>'||(LA37_93 >= '@' && LA37_93 <= 'Z')||(LA37_93 >= '_' && LA37_93 <= 'z')||LA37_93=='|'||(LA37_93 >= '\u007F' && LA37_93 <= '\u2012')||(LA37_93 >= '\u2014' && LA37_93 <= '\u2FFF')||(LA37_93 >= '\u3001' && LA37_93 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_93=='*'||LA37_93=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 41 : 
                        int LA37_98 = input.LA(1);

                        s = -1;
                        if ( (LA37_98==':') ) {s = 96;}

                        else if ( (LA37_98=='(') ) {s = 39;}

                        else if ( ((LA37_98 >= '\u0000' && LA37_98 <= '\b')||(LA37_98 >= '\u000B' && LA37_98 <= '\f')||(LA37_98 >= '\u000E' && LA37_98 <= '\u001F')||(LA37_98 >= '$' && LA37_98 <= '&')||(LA37_98 >= '.' && LA37_98 <= '9')||LA37_98=='<'||LA37_98=='>'||(LA37_98 >= '@' && LA37_98 <= 'Z')||(LA37_98 >= '_' && LA37_98 <= 'z')||LA37_98=='|'||(LA37_98 >= '\u007F' && LA37_98 <= '\u2012')||(LA37_98 >= '\u2014' && LA37_98 <= '\u2FFF')||(LA37_98 >= '\u3001' && LA37_98 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_98=='\\') ) {s = 33;}

                        else if ( (LA37_98=='+') ) {s = 34;}

                        else if ( (LA37_98=='-') ) {s = 35;}

                        else if ( (LA37_98=='\u2013') ) {s = 36;}

                        else if ( (LA37_98=='=') ) {s = 37;}

                        else if ( (LA37_98=='#') ) {s = 38;}

                        else if ( (LA37_98=='*'||LA37_98=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 42 : 
                        int LA37_21 = input.LA(1);

                        s = -1;
                        if ( (LA37_21=='N'||LA37_21=='n') ) {s = 53;}

                        else if ( (LA37_21=='r') ) {s = 54;}

                        else if ( ((LA37_21 >= '\u0000' && LA37_21 <= '\b')||(LA37_21 >= '\u000B' && LA37_21 <= '\f')||(LA37_21 >= '\u000E' && LA37_21 <= '\u001F')||(LA37_21 >= '$' && LA37_21 <= '&')||(LA37_21 >= '.' && LA37_21 <= '9')||LA37_21=='<'||LA37_21=='>'||(LA37_21 >= '@' && LA37_21 <= 'M')||(LA37_21 >= 'O' && LA37_21 <= 'Z')||(LA37_21 >= '_' && LA37_21 <= 'm')||(LA37_21 >= 'o' && LA37_21 <= 'q')||(LA37_21 >= 's' && LA37_21 <= 'z')||LA37_21=='|'||(LA37_21 >= '\u007F' && LA37_21 <= '\u2012')||(LA37_21 >= '\u2014' && LA37_21 <= '\u2FFF')||(LA37_21 >= '\u3001' && LA37_21 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_21=='\\') ) {s = 33;}

                        else if ( (LA37_21=='+') ) {s = 34;}

                        else if ( (LA37_21=='-') ) {s = 35;}

                        else if ( (LA37_21=='\u2013') ) {s = 36;}

                        else if ( (LA37_21=='=') ) {s = 37;}

                        else if ( (LA37_21=='#') ) {s = 38;}

                        else if ( (LA37_21=='(') ) {s = 39;}

                        else if ( (LA37_21=='*'||LA37_21=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 43 : 
                        int LA37_62 = input.LA(1);

                        s = -1;
                        if ( (LA37_62=='(') ) {s = 39;}

                        else if ( ((LA37_62 >= '0' && LA37_62 <= '9')) ) {s = 80;}

                        else if ( (LA37_62=='\\') ) {s = 33;}

                        else if ( (LA37_62=='+') ) {s = 34;}

                        else if ( (LA37_62=='-') ) {s = 35;}

                        else if ( (LA37_62=='\u2013') ) {s = 36;}

                        else if ( (LA37_62=='=') ) {s = 37;}

                        else if ( (LA37_62=='#') ) {s = 38;}

                        else if ( ((LA37_62 >= '\u0000' && LA37_62 <= '\b')||(LA37_62 >= '\u000B' && LA37_62 <= '\f')||(LA37_62 >= '\u000E' && LA37_62 <= '\u001F')||(LA37_62 >= '$' && LA37_62 <= '&')||(LA37_62 >= '.' && LA37_62 <= '/')||LA37_62=='<'||LA37_62=='>'||(LA37_62 >= '@' && LA37_62 <= 'Z')||(LA37_62 >= '_' && LA37_62 <= 'z')||LA37_62=='|'||(LA37_62 >= '\u007F' && LA37_62 <= '\u2012')||(LA37_62 >= '\u2014' && LA37_62 <= '\u2FFF')||(LA37_62 >= '\u3001' && LA37_62 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_62=='*'||LA37_62=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 44 : 
                        int LA37_92 = input.LA(1);

                        s = -1;
                        if ( (LA37_92=='(') ) {s = 39;}

                        else if ( ((LA37_92 >= '0' && LA37_92 <= '9')) ) {s = 103;}

                        else if ( (LA37_92=='\\') ) {s = 33;}

                        else if ( (LA37_92=='+') ) {s = 34;}

                        else if ( (LA37_92=='-') ) {s = 35;}

                        else if ( (LA37_92=='\u2013') ) {s = 36;}

                        else if ( (LA37_92=='=') ) {s = 37;}

                        else if ( (LA37_92=='#') ) {s = 38;}

                        else if ( ((LA37_92 >= '\u0000' && LA37_92 <= '\b')||(LA37_92 >= '\u000B' && LA37_92 <= '\f')||(LA37_92 >= '\u000E' && LA37_92 <= '\u001F')||(LA37_92 >= '$' && LA37_92 <= '&')||(LA37_92 >= '.' && LA37_92 <= '/')||LA37_92=='<'||LA37_92=='>'||(LA37_92 >= '@' && LA37_92 <= 'Z')||(LA37_92 >= '_' && LA37_92 <= 'z')||LA37_92=='|'||(LA37_92 >= '\u007F' && LA37_92 <= '\u2012')||(LA37_92 >= '\u2014' && LA37_92 <= '\u2FFF')||(LA37_92 >= '\u3001' && LA37_92 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_92=='*'||LA37_92=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 45 : 
                        int LA37_34 = input.LA(1);

                        s = -1;
                        if ( (LA37_34=='(') ) {s = 39;}

                        else if ( ((LA37_34 >= '\u0000' && LA37_34 <= '\b')||(LA37_34 >= '\u000B' && LA37_34 <= '\f')||(LA37_34 >= '\u000E' && LA37_34 <= '\u001F')||(LA37_34 >= '$' && LA37_34 <= '&')||(LA37_34 >= '.' && LA37_34 <= '9')||LA37_34=='<'||LA37_34=='>'||(LA37_34 >= '@' && LA37_34 <= 'Z')||(LA37_34 >= '_' && LA37_34 <= 'z')||LA37_34=='|'||(LA37_34 >= '\u007F' && LA37_34 <= '\u2012')||(LA37_34 >= '\u2014' && LA37_34 <= '\u2FFF')||(LA37_34 >= '\u3001' && LA37_34 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_34=='\\') ) {s = 33;}

                        else if ( (LA37_34=='+') ) {s = 34;}

                        else if ( (LA37_34=='-') ) {s = 35;}

                        else if ( (LA37_34=='\u2013') ) {s = 36;}

                        else if ( (LA37_34=='=') ) {s = 37;}

                        else if ( (LA37_34=='#') ) {s = 38;}

                        else if ( (LA37_34=='*'||LA37_34=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 46 : 
                        int LA37_57 = input.LA(1);

                        s = -1;
                        if ( (LA37_57=='A'||LA37_57=='a') ) {s = 76;}

                        else if ( (LA37_57=='(') ) {s = 39;}

                        else if ( ((LA37_57 >= '\u0000' && LA37_57 <= '\b')||(LA37_57 >= '\u000B' && LA37_57 <= '\f')||(LA37_57 >= '\u000E' && LA37_57 <= '\u001F')||(LA37_57 >= '$' && LA37_57 <= '&')||(LA37_57 >= '.' && LA37_57 <= '9')||LA37_57=='<'||LA37_57=='>'||LA37_57=='@'||(LA37_57 >= 'B' && LA37_57 <= 'Z')||(LA37_57 >= '_' && LA37_57 <= '`')||(LA37_57 >= 'b' && LA37_57 <= 'z')||LA37_57=='|'||(LA37_57 >= '\u007F' && LA37_57 <= '\u2012')||(LA37_57 >= '\u2014' && LA37_57 <= '\u2FFF')||(LA37_57 >= '\u3001' && LA37_57 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_57=='\\') ) {s = 33;}

                        else if ( (LA37_57=='+') ) {s = 34;}

                        else if ( (LA37_57=='-') ) {s = 35;}

                        else if ( (LA37_57=='\u2013') ) {s = 36;}

                        else if ( (LA37_57=='=') ) {s = 37;}

                        else if ( (LA37_57=='#') ) {s = 38;}

                        else if ( (LA37_57=='*'||LA37_57=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 47 : 
                        int LA37_76 = input.LA(1);

                        s = -1;
                        if ( (LA37_76=='R'||LA37_76=='r') ) {s = 88;}

                        else if ( (LA37_76=='(') ) {s = 39;}

                        else if ( ((LA37_76 >= '\u0000' && LA37_76 <= '\b')||(LA37_76 >= '\u000B' && LA37_76 <= '\f')||(LA37_76 >= '\u000E' && LA37_76 <= '\u001F')||(LA37_76 >= '$' && LA37_76 <= '&')||(LA37_76 >= '.' && LA37_76 <= '9')||LA37_76=='<'||LA37_76=='>'||(LA37_76 >= '@' && LA37_76 <= 'Q')||(LA37_76 >= 'S' && LA37_76 <= 'Z')||(LA37_76 >= '_' && LA37_76 <= 'q')||(LA37_76 >= 's' && LA37_76 <= 'z')||LA37_76=='|'||(LA37_76 >= '\u007F' && LA37_76 <= '\u2012')||(LA37_76 >= '\u2014' && LA37_76 <= '\u2FFF')||(LA37_76 >= '\u3001' && LA37_76 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_76=='\\') ) {s = 33;}

                        else if ( (LA37_76=='+') ) {s = 34;}

                        else if ( (LA37_76=='-') ) {s = 35;}

                        else if ( (LA37_76=='\u2013') ) {s = 36;}

                        else if ( (LA37_76=='=') ) {s = 37;}

                        else if ( (LA37_76=='#') ) {s = 38;}

                        else if ( (LA37_76=='*'||LA37_76=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 48 : 
                        int LA37_56 = input.LA(1);

                        s = -1;
                        if ( (LA37_56=='T'||LA37_56=='t') ) {s = 75;}

                        else if ( (LA37_56=='(') ) {s = 39;}

                        else if ( ((LA37_56 >= '\u0000' && LA37_56 <= '\b')||(LA37_56 >= '\u000B' && LA37_56 <= '\f')||(LA37_56 >= '\u000E' && LA37_56 <= '\u001F')||(LA37_56 >= '$' && LA37_56 <= '&')||(LA37_56 >= '.' && LA37_56 <= '9')||LA37_56=='<'||LA37_56=='>'||(LA37_56 >= '@' && LA37_56 <= 'S')||(LA37_56 >= 'U' && LA37_56 <= 'Z')||(LA37_56 >= '_' && LA37_56 <= 's')||(LA37_56 >= 'u' && LA37_56 <= 'z')||LA37_56=='|'||(LA37_56 >= '\u007F' && LA37_56 <= '\u2012')||(LA37_56 >= '\u2014' && LA37_56 <= '\u2FFF')||(LA37_56 >= '\u3001' && LA37_56 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_56=='\\') ) {s = 33;}

                        else if ( (LA37_56=='+') ) {s = 34;}

                        else if ( (LA37_56=='-') ) {s = 35;}

                        else if ( (LA37_56=='\u2013') ) {s = 36;}

                        else if ( (LA37_56=='=') ) {s = 37;}

                        else if ( (LA37_56=='#') ) {s = 38;}

                        else if ( (LA37_56=='*'||LA37_56=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 49 : 
                        int LA37_53 = input.LA(1);

                        s = -1;
                        if ( (LA37_53=='D'||LA37_53=='d') ) {s = 71;}

                        else if ( (LA37_53=='(') ) {s = 39;}

                        else if ( ((LA37_53 >= '\u0000' && LA37_53 <= '\b')||(LA37_53 >= '\u000B' && LA37_53 <= '\f')||(LA37_53 >= '\u000E' && LA37_53 <= '\u001F')||(LA37_53 >= '$' && LA37_53 <= '&')||(LA37_53 >= '.' && LA37_53 <= '9')||LA37_53=='<'||LA37_53=='>'||(LA37_53 >= '@' && LA37_53 <= 'C')||(LA37_53 >= 'E' && LA37_53 <= 'Z')||(LA37_53 >= '_' && LA37_53 <= 'c')||(LA37_53 >= 'e' && LA37_53 <= 'z')||LA37_53=='|'||(LA37_53 >= '\u007F' && LA37_53 <= '\u2012')||(LA37_53 >= '\u2014' && LA37_53 <= '\u2FFF')||(LA37_53 >= '\u3001' && LA37_53 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_53=='\\') ) {s = 33;}

                        else if ( (LA37_53=='+') ) {s = 34;}

                        else if ( (LA37_53=='-') ) {s = 35;}

                        else if ( (LA37_53=='\u2013') ) {s = 36;}

                        else if ( (LA37_53=='=') ) {s = 37;}

                        else if ( (LA37_53=='#') ) {s = 38;}

                        else if ( (LA37_53=='*'||LA37_53=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 50 : 
                        int LA37_105 = input.LA(1);

                        s = -1;
                        if ( (LA37_105=='(') ) {s = 39;}

                        else if ( ((LA37_105 >= '0' && LA37_105 <= '9')) ) {s = 108;}

                        else if ( (LA37_105=='\\') ) {s = 33;}

                        else if ( (LA37_105=='+') ) {s = 34;}

                        else if ( (LA37_105=='-') ) {s = 35;}

                        else if ( (LA37_105=='\u2013') ) {s = 36;}

                        else if ( (LA37_105=='=') ) {s = 37;}

                        else if ( (LA37_105=='#') ) {s = 38;}

                        else if ( ((LA37_105 >= '\u0000' && LA37_105 <= '\b')||(LA37_105 >= '\u000B' && LA37_105 <= '\f')||(LA37_105 >= '\u000E' && LA37_105 <= '\u001F')||(LA37_105 >= '$' && LA37_105 <= '&')||(LA37_105 >= '.' && LA37_105 <= '/')||LA37_105=='<'||LA37_105=='>'||(LA37_105 >= '@' && LA37_105 <= 'Z')||(LA37_105 >= '_' && LA37_105 <= 'z')||LA37_105=='|'||(LA37_105 >= '\u007F' && LA37_105 <= '\u2012')||(LA37_105 >= '\u2014' && LA37_105 <= '\u2FFF')||(LA37_105 >= '\u3001' && LA37_105 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_105=='*'||LA37_105=='?') ) {s = 40;}

                        else s = 107;

                        if ( s>=0 ) return s;
                        break;

                    case 51 : 
                        int LA37_104 = input.LA(1);

                        s = -1;
                        if ( ((LA37_104 >= '0' && LA37_104 <= '9')) ) {s = 106;}

                        else if ( (LA37_104=='(') ) {s = 39;}

                        else if ( ((LA37_104 >= '\u0000' && LA37_104 <= '\b')||(LA37_104 >= '\u000B' && LA37_104 <= '\f')||(LA37_104 >= '\u000E' && LA37_104 <= '\u001F')||(LA37_104 >= '$' && LA37_104 <= '&')||(LA37_104 >= '.' && LA37_104 <= '/')||LA37_104=='<'||LA37_104=='>'||(LA37_104 >= '@' && LA37_104 <= 'Z')||(LA37_104 >= '_' && LA37_104 <= 'z')||LA37_104=='|'||(LA37_104 >= '\u007F' && LA37_104 <= '\u2012')||(LA37_104 >= '\u2014' && LA37_104 <= '\u2FFF')||(LA37_104 >= '\u3001' && LA37_104 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_104=='\\') ) {s = 33;}

                        else if ( (LA37_104=='+') ) {s = 34;}

                        else if ( (LA37_104=='-') ) {s = 35;}

                        else if ( (LA37_104=='\u2013') ) {s = 36;}

                        else if ( (LA37_104=='=') ) {s = 37;}

                        else if ( (LA37_104=='#') ) {s = 38;}

                        else if ( (LA37_104=='*'||LA37_104=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 52 : 
                        int LA37_80 = input.LA(1);

                        s = -1;
                        if ( (LA37_80=='(') ) {s = 39;}

                        else if ( ((LA37_80 >= '0' && LA37_80 <= '9')) ) {s = 95;}

                        else if ( (LA37_80=='\\') ) {s = 33;}

                        else if ( (LA37_80=='+') ) {s = 34;}

                        else if ( (LA37_80=='-') ) {s = 92;}

                        else if ( (LA37_80=='\u2013') ) {s = 93;}

                        else if ( (LA37_80=='=') ) {s = 37;}

                        else if ( (LA37_80=='#') ) {s = 38;}

                        else if ( ((LA37_80 >= '.' && LA37_80 <= '/')) ) {s = 94;}

                        else if ( ((LA37_80 >= '\u0000' && LA37_80 <= '\b')||(LA37_80 >= '\u000B' && LA37_80 <= '\f')||(LA37_80 >= '\u000E' && LA37_80 <= '\u001F')||(LA37_80 >= '$' && LA37_80 <= '&')||LA37_80=='<'||LA37_80=='>'||(LA37_80 >= '@' && LA37_80 <= 'Z')||(LA37_80 >= '_' && LA37_80 <= 'z')||LA37_80=='|'||(LA37_80 >= '\u007F' && LA37_80 <= '\u2012')||(LA37_80 >= '\u2014' && LA37_80 <= '\u2FFF')||(LA37_80 >= '\u3001' && LA37_80 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_80=='*'||LA37_80=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 53 : 
                        int LA37_90 = input.LA(1);

                        s = -1;
                        if ( (LA37_90=='(') ) {s = 39;}

                        else if ( ((LA37_90 >= '0' && LA37_90 <= '9')) ) {s = 102;}

                        else if ( (LA37_90=='\\') ) {s = 33;}

                        else if ( (LA37_90=='+') ) {s = 34;}

                        else if ( (LA37_90=='-') ) {s = 35;}

                        else if ( (LA37_90=='\u2013') ) {s = 36;}

                        else if ( (LA37_90=='=') ) {s = 37;}

                        else if ( (LA37_90=='#') ) {s = 38;}

                        else if ( ((LA37_90 >= '\u0000' && LA37_90 <= '\b')||(LA37_90 >= '\u000B' && LA37_90 <= '\f')||(LA37_90 >= '\u000E' && LA37_90 <= '\u001F')||(LA37_90 >= '$' && LA37_90 <= '&')||(LA37_90 >= '.' && LA37_90 <= '/')||LA37_90=='<'||LA37_90=='>'||(LA37_90 >= '@' && LA37_90 <= 'Z')||(LA37_90 >= '_' && LA37_90 <= 'z')||LA37_90=='|'||(LA37_90 >= '\u007F' && LA37_90 <= '\u2012')||(LA37_90 >= '\u2014' && LA37_90 <= '\u2FFF')||(LA37_90 >= '\u3001' && LA37_90 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_90=='*'||LA37_90=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 54 : 
                        int LA37_109 = input.LA(1);

                        s = -1;
                        if ( ((LA37_109 >= '0' && LA37_109 <= '9')) ) {s = 111;}

                        else if ( (LA37_109=='(') ) {s = 39;}

                        else if ( ((LA37_109 >= '\u0000' && LA37_109 <= '\b')||(LA37_109 >= '\u000B' && LA37_109 <= '\f')||(LA37_109 >= '\u000E' && LA37_109 <= '\u001F')||(LA37_109 >= '$' && LA37_109 <= '&')||(LA37_109 >= '.' && LA37_109 <= '/')||LA37_109=='<'||LA37_109=='>'||(LA37_109 >= '@' && LA37_109 <= 'Z')||(LA37_109 >= '_' && LA37_109 <= 'z')||LA37_109=='|'||(LA37_109 >= '\u007F' && LA37_109 <= '\u2012')||(LA37_109 >= '\u2014' && LA37_109 <= '\u2FFF')||(LA37_109 >= '\u3001' && LA37_109 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_109=='\\') ) {s = 33;}

                        else if ( (LA37_109=='+') ) {s = 34;}

                        else if ( (LA37_109=='-') ) {s = 35;}

                        else if ( (LA37_109=='\u2013') ) {s = 36;}

                        else if ( (LA37_109=='=') ) {s = 37;}

                        else if ( (LA37_109=='#') ) {s = 38;}

                        else if ( (LA37_109=='*'||LA37_109=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 55 : 
                        int LA37_106 = input.LA(1);

                        s = -1;
                        if ( ((LA37_106 >= '0' && LA37_106 <= '9')) ) {s = 109;}

                        else if ( (LA37_106=='(') ) {s = 39;}

                        else if ( ((LA37_106 >= '\u0000' && LA37_106 <= '\b')||(LA37_106 >= '\u000B' && LA37_106 <= '\f')||(LA37_106 >= '\u000E' && LA37_106 <= '\u001F')||(LA37_106 >= '$' && LA37_106 <= '&')||(LA37_106 >= '.' && LA37_106 <= '/')||LA37_106=='<'||LA37_106=='>'||(LA37_106 >= '@' && LA37_106 <= 'Z')||(LA37_106 >= '_' && LA37_106 <= 'z')||LA37_106=='|'||(LA37_106 >= '\u007F' && LA37_106 <= '\u2012')||(LA37_106 >= '\u2014' && LA37_106 <= '\u2FFF')||(LA37_106 >= '\u3001' && LA37_106 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_106=='\\') ) {s = 33;}

                        else if ( (LA37_106=='+') ) {s = 34;}

                        else if ( (LA37_106=='-') ) {s = 35;}

                        else if ( (LA37_106=='\u2013') ) {s = 36;}

                        else if ( (LA37_106=='=') ) {s = 37;}

                        else if ( (LA37_106=='#') ) {s = 38;}

                        else if ( (LA37_106=='*'||LA37_106=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 56 : 
                        int LA37_2 = input.LA(1);

                        s = -1;
                        if ( ((LA37_2 >= '\u0000' && LA37_2 <= '\b')||(LA37_2 >= '\u000B' && LA37_2 <= '\f')||(LA37_2 >= '\u000E' && LA37_2 <= '\u001F')||(LA37_2 >= '$' && LA37_2 <= '&')||(LA37_2 >= '.' && LA37_2 <= '9')||LA37_2=='<'||LA37_2=='>'||(LA37_2 >= '@' && LA37_2 <= 'Z')||(LA37_2 >= '_' && LA37_2 <= 'z')||LA37_2=='|'||(LA37_2 >= '\u007F' && LA37_2 <= '\u2012')||(LA37_2 >= '\u2014' && LA37_2 <= '\u2FFF')||(LA37_2 >= '\u3001' && LA37_2 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_2=='\\') ) {s = 33;}

                        else if ( (LA37_2=='+') ) {s = 34;}

                        else if ( (LA37_2=='-') ) {s = 35;}

                        else if ( (LA37_2=='\u2013') ) {s = 36;}

                        else if ( (LA37_2=='=') ) {s = 37;}

                        else if ( (LA37_2=='#') ) {s = 38;}

                        else if ( (LA37_2=='(') ) {s = 39;}

                        else if ( (LA37_2=='*'||LA37_2=='?') ) {s = 40;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;

                    case 57 : 
                        int LA37_22 = input.LA(1);

                        s = -1;
                        if ( (LA37_22=='R'||LA37_22=='r') ) {s = 55;}

                        else if ( ((LA37_22 >= '\u0000' && LA37_22 <= '\b')||(LA37_22 >= '\u000B' && LA37_22 <= '\f')||(LA37_22 >= '\u000E' && LA37_22 <= '\u001F')||(LA37_22 >= '$' && LA37_22 <= '&')||(LA37_22 >= '.' && LA37_22 <= '9')||LA37_22=='<'||LA37_22=='>'||(LA37_22 >= '@' && LA37_22 <= 'Q')||(LA37_22 >= 'S' && LA37_22 <= 'Z')||(LA37_22 >= '_' && LA37_22 <= 'q')||(LA37_22 >= 's' && LA37_22 <= 'z')||LA37_22=='|'||(LA37_22 >= '\u007F' && LA37_22 <= '\u2012')||(LA37_22 >= '\u2014' && LA37_22 <= '\u2FFF')||(LA37_22 >= '\u3001' && LA37_22 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_22=='\\') ) {s = 33;}

                        else if ( (LA37_22=='+') ) {s = 34;}

                        else if ( (LA37_22=='-') ) {s = 35;}

                        else if ( (LA37_22=='\u2013') ) {s = 36;}

                        else if ( (LA37_22=='=') ) {s = 37;}

                        else if ( (LA37_22=='#') ) {s = 38;}

                        else if ( (LA37_22=='(') ) {s = 39;}

                        else if ( (LA37_22=='*'||LA37_22=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 58 : 
                        int LA37_49 = input.LA(1);

                        s = -1;
                        if ( ((LA37_49 >= '\u0000' && LA37_49 <= '\uFFFF')) ) {s = 68;}

                        if ( s>=0 ) return s;
                        break;

                    case 59 : 
                        int LA37_50 = input.LA(1);

                        s = -1;
                        if ( (LA37_50=='\"') ) {s = 69;}

                        else if ( (LA37_50=='\\') ) {s = 49;}

                        else if ( ((LA37_50 >= '\u0000' && LA37_50 <= '!')||(LA37_50 >= '#' && LA37_50 <= ')')||(LA37_50 >= '+' && LA37_50 <= '>')||(LA37_50 >= '@' && LA37_50 <= '[')||(LA37_50 >= ']' && LA37_50 <= '\uFFFF')) ) {s = 50;}

                        else if ( (LA37_50=='*'||LA37_50=='?') ) {s = 51;}

                        if ( s>=0 ) return s;
                        break;

                    case 60 : 
                        int LA37_88 = input.LA(1);

                        s = -1;
                        if ( (LA37_88=='(') ) {s = 39;}

                        else if ( ((LA37_88 >= '\u0000' && LA37_88 <= '\b')||(LA37_88 >= '\u000B' && LA37_88 <= '\f')||(LA37_88 >= '\u000E' && LA37_88 <= '\u001F')||(LA37_88 >= '$' && LA37_88 <= '&')||(LA37_88 >= '.' && LA37_88 <= '9')||LA37_88=='<'||LA37_88=='>'||(LA37_88 >= '@' && LA37_88 <= 'Z')||(LA37_88 >= '_' && LA37_88 <= 'z')||LA37_88=='|'||(LA37_88 >= '\u007F' && LA37_88 <= '\u2012')||(LA37_88 >= '\u2014' && LA37_88 <= '\u2FFF')||(LA37_88 >= '\u3001' && LA37_88 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_88=='\\') ) {s = 33;}

                        else if ( (LA37_88=='+') ) {s = 34;}

                        else if ( (LA37_88=='-') ) {s = 35;}

                        else if ( (LA37_88=='\u2013') ) {s = 36;}

                        else if ( (LA37_88=='=') ) {s = 37;}

                        else if ( (LA37_88=='#') ) {s = 38;}

                        else if ( (LA37_88=='*'||LA37_88=='?') ) {s = 40;}

                        else s = 99;

                        if ( s>=0 ) return s;
                        break;

                    case 61 : 
                        int LA37_20 = input.LA(1);

                        s = -1;
                        if ( (LA37_20=='O') ) {s = 52;}

                        else if ( ((LA37_20 >= '\u0000' && LA37_20 <= '\b')||(LA37_20 >= '\u000B' && LA37_20 <= '\f')||(LA37_20 >= '\u000E' && LA37_20 <= '\u001F')||(LA37_20 >= '$' && LA37_20 <= '&')||(LA37_20 >= '.' && LA37_20 <= '9')||LA37_20=='<'||LA37_20=='>'||(LA37_20 >= '@' && LA37_20 <= 'N')||(LA37_20 >= 'P' && LA37_20 <= 'Z')||(LA37_20 >= '_' && LA37_20 <= 'z')||LA37_20=='|'||(LA37_20 >= '\u007F' && LA37_20 <= '\u2012')||(LA37_20 >= '\u2014' && LA37_20 <= '\u2FFF')||(LA37_20 >= '\u3001' && LA37_20 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_20=='\\') ) {s = 33;}

                        else if ( (LA37_20=='+') ) {s = 34;}

                        else if ( (LA37_20=='-') ) {s = 35;}

                        else if ( (LA37_20=='\u2013') ) {s = 36;}

                        else if ( (LA37_20=='=') ) {s = 37;}

                        else if ( (LA37_20=='#') ) {s = 38;}

                        else if ( (LA37_20=='(') ) {s = 39;}

                        else if ( (LA37_20=='*'||LA37_20=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 62 : 
                        int LA37_102 = input.LA(1);

                        s = -1;
                        if ( (LA37_102=='(') ) {s = 39;}

                        else if ( ((LA37_102 >= '0' && LA37_102 <= '9')) ) {s = 102;}

                        else if ( (LA37_102=='\\') ) {s = 33;}

                        else if ( (LA37_102=='+') ) {s = 34;}

                        else if ( (LA37_102=='-') ) {s = 35;}

                        else if ( (LA37_102=='\u2013') ) {s = 36;}

                        else if ( (LA37_102=='=') ) {s = 37;}

                        else if ( (LA37_102=='#') ) {s = 38;}

                        else if ( ((LA37_102 >= '\u0000' && LA37_102 <= '\b')||(LA37_102 >= '\u000B' && LA37_102 <= '\f')||(LA37_102 >= '\u000E' && LA37_102 <= '\u001F')||(LA37_102 >= '$' && LA37_102 <= '&')||(LA37_102 >= '.' && LA37_102 <= '/')||LA37_102=='<'||LA37_102=='>'||(LA37_102 >= '@' && LA37_102 <= 'Z')||(LA37_102 >= '_' && LA37_102 <= 'z')||LA37_102=='|'||(LA37_102 >= '\u007F' && LA37_102 <= '\u2012')||(LA37_102 >= '\u2014' && LA37_102 <= '\u2FFF')||(LA37_102 >= '\u3001' && LA37_102 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_102=='*'||LA37_102=='?') ) {s = 40;}

                        else s = 58;

                        if ( s>=0 ) return s;
                        break;

                    case 63 : 
                        int LA37_13 = input.LA(1);

                        s = -1;
                        if ( (LA37_13=='?') ) {s = 13;}

                        else if ( ((LA37_13 >= '\u0000' && LA37_13 <= '\b')||(LA37_13 >= '\u000B' && LA37_13 <= '\f')||(LA37_13 >= '\u000E' && LA37_13 <= '\u001F')||(LA37_13 >= '#' && LA37_13 <= '&')||LA37_13=='+'||(LA37_13 >= '-' && LA37_13 <= '9')||(LA37_13 >= '<' && LA37_13 <= '>')||(LA37_13 >= '@' && LA37_13 <= 'Z')||LA37_13=='\\'||(LA37_13 >= '_' && LA37_13 <= 'z')||LA37_13=='|'||(LA37_13 >= '\u007F' && LA37_13 <= '\u2FFF')||(LA37_13 >= '\u3001' && LA37_13 <= '\uFFFF')) ) {s = 40;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;

                    case 64 : 
                        int LA37_26 = input.LA(1);

                        s = -1;
                        if ( (LA37_26=='N'||LA37_26=='n') ) {s = 53;}

                        else if ( ((LA37_26 >= '\u0000' && LA37_26 <= '\b')||(LA37_26 >= '\u000B' && LA37_26 <= '\f')||(LA37_26 >= '\u000E' && LA37_26 <= '\u001F')||(LA37_26 >= '$' && LA37_26 <= '&')||(LA37_26 >= '.' && LA37_26 <= '9')||LA37_26=='<'||LA37_26=='>'||(LA37_26 >= '@' && LA37_26 <= 'M')||(LA37_26 >= 'O' && LA37_26 <= 'Z')||(LA37_26 >= '_' && LA37_26 <= 'm')||(LA37_26 >= 'o' && LA37_26 <= 'z')||LA37_26=='|'||(LA37_26 >= '\u007F' && LA37_26 <= '\u2012')||(LA37_26 >= '\u2014' && LA37_26 <= '\u2FFF')||(LA37_26 >= '\u3001' && LA37_26 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_26=='\\') ) {s = 33;}

                        else if ( (LA37_26=='+') ) {s = 34;}

                        else if ( (LA37_26=='-') ) {s = 35;}

                        else if ( (LA37_26=='\u2013') ) {s = 36;}

                        else if ( (LA37_26=='=') ) {s = 37;}

                        else if ( (LA37_26=='#') ) {s = 38;}

                        else if ( (LA37_26=='(') ) {s = 39;}

                        else if ( (LA37_26=='*'||LA37_26=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 65 : 
                        int LA37_23 = input.LA(1);

                        s = -1;
                        if ( (LA37_23=='O'||LA37_23=='o') ) {s = 56;}

                        else if ( (LA37_23=='E'||LA37_23=='e') ) {s = 57;}

                        else if ( ((LA37_23 >= '\u0000' && LA37_23 <= '\b')||(LA37_23 >= '\u000B' && LA37_23 <= '\f')||(LA37_23 >= '\u000E' && LA37_23 <= '\u001F')||(LA37_23 >= '$' && LA37_23 <= '&')||(LA37_23 >= '.' && LA37_23 <= '9')||LA37_23=='<'||LA37_23=='>'||(LA37_23 >= '@' && LA37_23 <= 'D')||(LA37_23 >= 'F' && LA37_23 <= 'N')||(LA37_23 >= 'P' && LA37_23 <= 'Z')||(LA37_23 >= '_' && LA37_23 <= 'd')||(LA37_23 >= 'f' && LA37_23 <= 'n')||(LA37_23 >= 'p' && LA37_23 <= 'z')||LA37_23=='|'||(LA37_23 >= '\u007F' && LA37_23 <= '\u2012')||(LA37_23 >= '\u2014' && LA37_23 <= '\u2FFF')||(LA37_23 >= '\u3001' && LA37_23 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_23=='\\') ) {s = 33;}

                        else if ( (LA37_23=='+') ) {s = 34;}

                        else if ( (LA37_23=='-') ) {s = 35;}

                        else if ( (LA37_23=='\u2013') ) {s = 36;}

                        else if ( (LA37_23=='=') ) {s = 37;}

                        else if ( (LA37_23=='#') ) {s = 38;}

                        else if ( (LA37_23=='(') ) {s = 39;}

                        else if ( (LA37_23=='*'||LA37_23=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 66 : 
                        int LA37_75 = input.LA(1);

                        s = -1;
                        if ( (LA37_75=='(') ) {s = 39;}

                        else if ( ((LA37_75 >= '\u0000' && LA37_75 <= '\b')||(LA37_75 >= '\u000B' && LA37_75 <= '\f')||(LA37_75 >= '\u000E' && LA37_75 <= '\u001F')||(LA37_75 >= '$' && LA37_75 <= '&')||(LA37_75 >= '.' && LA37_75 <= '9')||LA37_75=='<'||LA37_75=='>'||(LA37_75 >= '@' && LA37_75 <= 'Z')||(LA37_75 >= '_' && LA37_75 <= 'z')||LA37_75=='|'||(LA37_75 >= '\u007F' && LA37_75 <= '\u2012')||(LA37_75 >= '\u2014' && LA37_75 <= '\u2FFF')||(LA37_75 >= '\u3001' && LA37_75 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_75=='\\') ) {s = 33;}

                        else if ( (LA37_75=='+') ) {s = 34;}

                        else if ( (LA37_75=='-') ) {s = 35;}

                        else if ( (LA37_75=='\u2013') ) {s = 36;}

                        else if ( (LA37_75=='=') ) {s = 37;}

                        else if ( (LA37_75=='#') ) {s = 38;}

                        else if ( (LA37_75=='*'||LA37_75=='?') ) {s = 40;}

                        else s = 87;

                        if ( s>=0 ) return s;
                        break;

                    case 67 : 
                        int LA37_79 = input.LA(1);

                        s = -1;
                        if ( (LA37_79=='(') ) {s = 39;}

                        else if ( ((LA37_79 >= '0' && LA37_79 <= '9')) ) {s = 91;}

                        else if ( (LA37_79=='\\') ) {s = 33;}

                        else if ( (LA37_79=='+') ) {s = 34;}

                        else if ( (LA37_79=='-') ) {s = 92;}

                        else if ( (LA37_79=='\u2013') ) {s = 93;}

                        else if ( (LA37_79=='=') ) {s = 37;}

                        else if ( (LA37_79=='#') ) {s = 38;}

                        else if ( ((LA37_79 >= '.' && LA37_79 <= '/')) ) {s = 94;}

                        else if ( ((LA37_79 >= '\u0000' && LA37_79 <= '\b')||(LA37_79 >= '\u000B' && LA37_79 <= '\f')||(LA37_79 >= '\u000E' && LA37_79 <= '\u001F')||(LA37_79 >= '$' && LA37_79 <= '&')||LA37_79=='<'||LA37_79=='>'||(LA37_79 >= '@' && LA37_79 <= 'Z')||(LA37_79 >= '_' && LA37_79 <= 'z')||LA37_79=='|'||(LA37_79 >= '\u007F' && LA37_79 <= '\u2012')||(LA37_79 >= '\u2014' && LA37_79 <= '\u2FFF')||(LA37_79 >= '\u3001' && LA37_79 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_79=='*'||LA37_79=='?') ) {s = 40;}

                        else s = 58;

                        if ( s>=0 ) return s;
                        break;

                    case 68 : 
                        int LA37_77 = input.LA(1);

                        s = -1;
                        if ( ((LA37_77 >= '0' && LA37_77 <= '9')) ) {s = 89;}

                        else if ( (LA37_77=='(') ) {s = 39;}

                        else if ( (LA37_77=='.') ) {s = 90;}

                        else if ( (LA37_77=='\\') ) {s = 33;}

                        else if ( (LA37_77=='+') ) {s = 34;}

                        else if ( (LA37_77=='-') ) {s = 35;}

                        else if ( (LA37_77=='\u2013') ) {s = 36;}

                        else if ( (LA37_77=='=') ) {s = 37;}

                        else if ( (LA37_77=='#') ) {s = 38;}

                        else if ( ((LA37_77 >= '\u0000' && LA37_77 <= '\b')||(LA37_77 >= '\u000B' && LA37_77 <= '\f')||(LA37_77 >= '\u000E' && LA37_77 <= '\u001F')||(LA37_77 >= '$' && LA37_77 <= '&')||LA37_77=='/'||LA37_77=='<'||LA37_77=='>'||(LA37_77 >= '@' && LA37_77 <= 'Z')||(LA37_77 >= '_' && LA37_77 <= 'z')||LA37_77=='|'||(LA37_77 >= '\u007F' && LA37_77 <= '\u2012')||(LA37_77 >= '\u2014' && LA37_77 <= '\u2FFF')||(LA37_77 >= '\u3001' && LA37_77 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_77=='*'||LA37_77=='?') ) {s = 40;}

                        else s = 58;

                        if ( s>=0 ) return s;
                        break;

                    case 69 : 
                        int LA37_41 = input.LA(1);

                        s = -1;
                        if ( (LA37_41=='>') ) {s = 67;}

                        else if ( (LA37_41=='(') ) {s = 39;}

                        else if ( ((LA37_41 >= '\u0000' && LA37_41 <= '\b')||(LA37_41 >= '\u000B' && LA37_41 <= '\f')||(LA37_41 >= '\u000E' && LA37_41 <= '\u001F')||(LA37_41 >= '$' && LA37_41 <= '&')||(LA37_41 >= '.' && LA37_41 <= '9')||LA37_41=='<'||(LA37_41 >= '@' && LA37_41 <= 'Z')||(LA37_41 >= '_' && LA37_41 <= 'z')||LA37_41=='|'||(LA37_41 >= '\u007F' && LA37_41 <= '\u2012')||(LA37_41 >= '\u2014' && LA37_41 <= '\u2FFF')||(LA37_41 >= '\u3001' && LA37_41 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_41=='\\') ) {s = 33;}

                        else if ( (LA37_41=='+') ) {s = 34;}

                        else if ( (LA37_41=='-') ) {s = 35;}

                        else if ( (LA37_41=='\u2013') ) {s = 36;}

                        else if ( (LA37_41=='=') ) {s = 37;}

                        else if ( (LA37_41=='#') ) {s = 38;}

                        else if ( (LA37_41=='*'||LA37_41=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 70 : 
                        int LA37_95 = input.LA(1);

                        s = -1;
                        if ( (LA37_95=='(') ) {s = 39;}

                        else if ( ((LA37_95 >= '.' && LA37_95 <= '/')) ) {s = 94;}

                        else if ( (LA37_95=='\\') ) {s = 33;}

                        else if ( (LA37_95=='+') ) {s = 34;}

                        else if ( (LA37_95=='-') ) {s = 92;}

                        else if ( (LA37_95=='\u2013') ) {s = 93;}

                        else if ( (LA37_95=='=') ) {s = 37;}

                        else if ( (LA37_95=='#') ) {s = 38;}

                        else if ( ((LA37_95 >= '\u0000' && LA37_95 <= '\b')||(LA37_95 >= '\u000B' && LA37_95 <= '\f')||(LA37_95 >= '\u000E' && LA37_95 <= '\u001F')||(LA37_95 >= '$' && LA37_95 <= '&')||(LA37_95 >= '0' && LA37_95 <= '9')||LA37_95=='<'||LA37_95=='>'||(LA37_95 >= '@' && LA37_95 <= 'Z')||(LA37_95 >= '_' && LA37_95 <= 'z')||LA37_95=='|'||(LA37_95 >= '\u007F' && LA37_95 <= '\u2012')||(LA37_95 >= '\u2014' && LA37_95 <= '\u2FFF')||(LA37_95 >= '\u3001' && LA37_95 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_95=='*'||LA37_95=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 71 : 
                        int LA37_28 = input.LA(1);

                        s = -1;
                        if ( ((LA37_28 >= '\u0000' && LA37_28 <= '\b')||(LA37_28 >= '\u000B' && LA37_28 <= '\f')||(LA37_28 >= '\u000E' && LA37_28 <= '\u001F')||(LA37_28 >= '$' && LA37_28 <= '&')||(LA37_28 >= '.' && LA37_28 <= '9')||LA37_28=='<'||LA37_28=='>'||(LA37_28 >= '@' && LA37_28 <= 'Z')||(LA37_28 >= '_' && LA37_28 <= 'z')||LA37_28=='|'||(LA37_28 >= '\u007F' && LA37_28 <= '\u2012')||(LA37_28 >= '\u2014' && LA37_28 <= '\u2FFF')||(LA37_28 >= '\u3001' && LA37_28 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_28=='\\') ) {s = 33;}

                        else if ( (LA37_28=='+') ) {s = 34;}

                        else if ( (LA37_28=='-') ) {s = 35;}

                        else if ( (LA37_28=='\u2013') ) {s = 36;}

                        else if ( (LA37_28=='=') ) {s = 37;}

                        else if ( (LA37_28=='#') ) {s = 38;}

                        else if ( (LA37_28=='(') ) {s = 39;}

                        else if ( (LA37_28=='*'||LA37_28=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 72 : 
                        int LA37_63 = input.LA(1);

                        s = -1;
                        if ( (LA37_63=='(') ) {s = 39;}

                        else if ( ((LA37_63 >= '0' && LA37_63 <= '9')) ) {s = 80;}

                        else if ( (LA37_63=='\\') ) {s = 33;}

                        else if ( (LA37_63=='+') ) {s = 34;}

                        else if ( (LA37_63=='-') ) {s = 35;}

                        else if ( (LA37_63=='\u2013') ) {s = 36;}

                        else if ( (LA37_63=='=') ) {s = 37;}

                        else if ( (LA37_63=='#') ) {s = 38;}

                        else if ( ((LA37_63 >= '\u0000' && LA37_63 <= '\b')||(LA37_63 >= '\u000B' && LA37_63 <= '\f')||(LA37_63 >= '\u000E' && LA37_63 <= '\u001F')||(LA37_63 >= '$' && LA37_63 <= '&')||(LA37_63 >= '.' && LA37_63 <= '/')||LA37_63=='<'||LA37_63=='>'||(LA37_63 >= '@' && LA37_63 <= 'Z')||(LA37_63 >= '_' && LA37_63 <= 'z')||LA37_63=='|'||(LA37_63 >= '\u007F' && LA37_63 <= '\u2012')||(LA37_63 >= '\u2014' && LA37_63 <= '\u2FFF')||(LA37_63 >= '\u3001' && LA37_63 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_63=='*'||LA37_63=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 73 : 
                        int LA37_94 = input.LA(1);

                        s = -1;
                        if ( (LA37_94=='(') ) {s = 39;}

                        else if ( ((LA37_94 >= '0' && LA37_94 <= '9')) ) {s = 103;}

                        else if ( (LA37_94=='\\') ) {s = 33;}

                        else if ( (LA37_94=='+') ) {s = 34;}

                        else if ( (LA37_94=='-') ) {s = 35;}

                        else if ( (LA37_94=='\u2013') ) {s = 36;}

                        else if ( (LA37_94=='=') ) {s = 37;}

                        else if ( (LA37_94=='#') ) {s = 38;}

                        else if ( ((LA37_94 >= '\u0000' && LA37_94 <= '\b')||(LA37_94 >= '\u000B' && LA37_94 <= '\f')||(LA37_94 >= '\u000E' && LA37_94 <= '\u001F')||(LA37_94 >= '$' && LA37_94 <= '&')||(LA37_94 >= '.' && LA37_94 <= '/')||LA37_94=='<'||LA37_94=='>'||(LA37_94 >= '@' && LA37_94 <= 'Z')||(LA37_94 >= '_' && LA37_94 <= 'z')||LA37_94=='|'||(LA37_94 >= '\u007F' && LA37_94 <= '\u2012')||(LA37_94 >= '\u2014' && LA37_94 <= '\u2FFF')||(LA37_94 >= '\u3001' && LA37_94 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA37_94=='*'||LA37_94=='?') ) {s = 40;}

                        else s = 42;

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