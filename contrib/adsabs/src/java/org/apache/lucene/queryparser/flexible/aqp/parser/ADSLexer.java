// $ANTLR 3.4 /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g 2012-07-10 18:37:03

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class ADSLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__73=73;
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
    public static final int LPAREN=27;
    public static final int MINUS=28;
    public static final int MODIFIER=29;
    public static final int M_NUMBER=30;
    public static final int NEAR=31;
    public static final int NOT=32;
    public static final int NUMBER=33;
    public static final int OPERATOR=34;
    public static final int OR=35;
    public static final int PHRASE=36;
    public static final int PHRASE_ANYTHING=37;
    public static final int PLUS=38;
    public static final int QANYTHING=39;
    public static final int QCOMMA=40;
    public static final int QCOORDINATE=41;
    public static final int QDATE=42;
    public static final int QFUNC=43;
    public static final int QIDENTIFIER=44;
    public static final int QMARK=45;
    public static final int QNORMAL=46;
    public static final int QPHRASE=47;
    public static final int QPHRASETRUNC=48;
    public static final int QPOSITION=49;
    public static final int QRANGEEX=50;
    public static final int QRANGEIN=51;
    public static final int QTRUNCATED=52;
    public static final int RBRACK=53;
    public static final int RPAREN=54;
    public static final int SEMICOLON=55;
    public static final int SQUOTE=56;
    public static final int STAR=57;
    public static final int S_NUMBER=58;
    public static final int TERM_CHAR=59;
    public static final int TERM_NORMAL=60;
    public static final int TERM_START_CHAR=61;
    public static final int TERM_TRUNCATED=62;
    public static final int TILDE=63;
    public static final int TMODIFIER=64;
    public static final int TO=65;
    public static final int WS=66;

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

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
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
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
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
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
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
    // $ANTLR end "T__69"

    // $ANTLR start "T__70"
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
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
    // $ANTLR end "T__70"

    // $ANTLR start "T__71"
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:15:7: ( 'arXiv:' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:15:9: 'arXiv:'
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
    // $ANTLR end "T__71"

    // $ANTLR start "T__72"
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:16:7: ( 'arxiv:' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:16:9: 'arxiv:'
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
    // $ANTLR end "T__72"

    // $ANTLR start "T__73"
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:17:7: ( 'doi:' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:17:9: 'doi:'
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
    // $ANTLR end "T__73"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:357:9: ( '(' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:357:11: '('
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:359:9: ( ')' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:359:11: ')'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:361:9: ( '[' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:361:11: '['
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:363:9: ( ']' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:363:11: ']'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:365:9: ( ':' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:365:11: ':'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:367:7: ( '+' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:367:9: '+'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:369:7: ( ( '-' | '–' ) )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:371:7: ( '*' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:371:9: '*'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:373:8: ( ( '?' )+ )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:373:10: ( '?' )+
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:373:10: ( '?' )+
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
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:373:10: '?'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:380:7: ( '^' ( NUMBER )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:380:9: '^' ( NUMBER )?
            {
            match('^'); 

            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:380:13: ( NUMBER )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:380:13: NUMBER
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:382:7: ( '~' ( NUMBER )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:382:9: '~' ( NUMBER )?
            {
            match('~'); 

            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:382:13: ( NUMBER )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:382:13: NUMBER
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:384:8: ( '\\\"' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:384:10: '\\\"'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:386:8: ( '\\'' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:386:10: '\\''
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:388:7: ( ',' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:388:9: ','
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:390:10: ( ';' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:390:12: ';'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:394:2: (~ ( '0' .. '9' | ' ' | COMMA | PLUS | MINUS | '$' ) )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:399:18: ( '\\\\' . )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:399:21: '\\\\' .
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:401:4: ( 'TO' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:401:6: 'TO'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:404:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:404:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:404:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:404:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:405:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:405:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:405:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:405:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:406:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:406:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:407:7: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:407:9: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:413:2: ( '^' ( AS_CHAR )+ ( ',' ( ' ' | AS_CHAR )+ )* ( '$' )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:414:2: '^' ( AS_CHAR )+ ( ',' ( ' ' | AS_CHAR )+ )* ( '$' )?
            {
            match('^'); 

            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:414:6: ( AS_CHAR )+
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


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:414:15: ( ',' ( ' ' | AS_CHAR )+ )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==',') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:414:16: ',' ( ' ' | AS_CHAR )+
            	    {
            	    match(','); 

            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:414:20: ( ' ' | AS_CHAR )+
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


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:414:39: ( '$' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='$') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:414:39: '$'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:448:2: ( '-' INT INT INT INT | INT INT INT INT '-' ( INT INT INT INT )? )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:449:2: '-' INT INT INT INT
                    {
                    match('-'); 

                    mINT(); 


                    mINT(); 


                    mINT(); 


                    mINT(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:450:4: INT INT INT INT '-' ( INT INT INT INT )?
                    {
                    mINT(); 


                    mINT(); 


                    mINT(); 


                    mINT(); 


                    match('-'); 

                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:450:24: ( INT INT INT INT )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:450:25: INT INT INT INT
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:462:2: ( TERM_NORMAL '(' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:463:2: TERM_NORMAL '('
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:467:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:467:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:476:13: ( '0' .. '9' )
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:481:2: ( (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | '–' | ';' ) | ESC_CHAR ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:482:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | '–' | ';' ) | ESC_CHAR )
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:482:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | '–' | ';' ) | ESC_CHAR )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( ((LA10_0 >= '\u0000' && LA10_0 <= '\b')||(LA10_0 >= '\u000B' && LA10_0 <= '\f')||(LA10_0 >= '\u000E' && LA10_0 <= '\u001F')||(LA10_0 >= '$' && LA10_0 <= '&')||(LA10_0 >= '.' && LA10_0 <= '9')||LA10_0=='<'||LA10_0=='>'||(LA10_0 >= '@' && LA10_0 <= 'Z')||(LA10_0 >= '_' && LA10_0 <= 'z')||LA10_0=='|'||(LA10_0 >= '\u007F' && LA10_0 <= '\u2012')||(LA10_0 >= '\u2014' && LA10_0 <= '\u2FFF')||(LA10_0 >= '\u3001' && LA10_0 <= '\uFFFF')) ) {
                alt10=1;
            }
            else if ( (LA10_0=='\\') ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:482:3: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | ',' | '=' | '#' | '–' | ';' )
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
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:489:5: ESC_CHAR
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:493:2: ( ( TERM_START_CHAR | '+' | '-' | '–' | '=' | '#' ) )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:494:2: ( TERM_START_CHAR | '+' | '-' | '–' | '=' | '#' )
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:494:2: ( TERM_START_CHAR | '+' | '-' | '–' | '=' | '#' )
            int alt11=6;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0 >= '\u0000' && LA11_0 <= '\b')||(LA11_0 >= '\u000B' && LA11_0 <= '\f')||(LA11_0 >= '\u000E' && LA11_0 <= '\u001F')||(LA11_0 >= '$' && LA11_0 <= '&')||(LA11_0 >= '.' && LA11_0 <= '9')||LA11_0=='<'||LA11_0=='>'||(LA11_0 >= '@' && LA11_0 <= 'Z')||LA11_0=='\\'||(LA11_0 >= '_' && LA11_0 <= 'z')||LA11_0=='|'||(LA11_0 >= '\u007F' && LA11_0 <= '\u2012')||(LA11_0 >= '\u2014' && LA11_0 <= '\u2FFF')||(LA11_0 >= '\u3001' && LA11_0 <= '\uFFFF')) ) {
                alt11=1;
            }
            else if ( (LA11_0=='+') ) {
                alt11=2;
            }
            else if ( (LA11_0=='-') ) {
                alt11=3;
            }
            else if ( (LA11_0=='\u2013') ) {
                alt11=4;
            }
            else if ( (LA11_0=='=') ) {
                alt11=5;
            }
            else if ( (LA11_0=='#') ) {
                alt11=6;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:494:3: TERM_START_CHAR
                    {
                    mTERM_START_CHAR(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:494:22: '+'
                    {
                    match('+'); 

                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:494:28: '-'
                    {
                    match('-'); 

                    }
                    break;
                case 4 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:494:34: '–'
                    {
                    match('\u2013'); 

                    }
                    break;
                case 5 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:494:40: '='
                    {
                    match('='); 

                    }
                    break;
                case 6 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:494:46: '#'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:500:2: ( INT ( INT )? ( '/' | MINUS | '.' ) INT ( INT )? ( '/' | MINUS | '.' ) INT INT ( INT INT )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:501:2: INT ( INT )? ( '/' | MINUS | '.' ) INT ( INT )? ( '/' | MINUS | '.' ) INT INT ( INT INT )?
            {
            mINT(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:501:6: ( INT )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( ((LA12_0 >= '0' && LA12_0 <= '9')) ) {
                alt12=1;
            }
            switch (alt12) {
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


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:501:31: ( INT )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( ((LA13_0 >= '0' && LA13_0 <= '9')) ) {
                alt13=1;
            }
            switch (alt13) {
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


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:501:60: ( INT INT )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( ((LA14_0 >= '0' && LA14_0 <= '9')) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:501:61: INT INT
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:505:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:506:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:506:2: ( INT )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0 >= '0' && LA15_0 <= '9')) ) {
                    alt15=1;
                }


                switch (alt15) {
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
            	    if ( cnt15 >= 1 ) break loop15;
                        EarlyExitException eee =
                            new EarlyExitException(15, input);
                        throw eee;
                }
                cnt15++;
            } while (true);


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:506:7: ( '.' ( INT )+ )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0=='.') ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:506:8: '.' ( INT )+
                    {
                    match('.'); 

                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:506:12: ( INT )+
                    int cnt16=0;
                    loop16:
                    do {
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

                    	default :
                    	    if ( cnt16 >= 1 ) break loop16;
                                EarlyExitException eee =
                                    new EarlyExitException(16, input);
                                throw eee;
                        }
                        cnt16++;
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:509:18: ( NUMBER 'm' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:510:2: NUMBER 'm'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:512:18: ( NUMBER 'h' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:513:2: NUMBER 'h'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:515:18: ( NUMBER 'd' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:516:2: NUMBER 'd'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:518:18: ( NUMBER 's' )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:519:2: NUMBER 's'
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:522:2: ( INT INT COLON INT INT COLON NUMBER ( PLUS | MINUS ) INT INT COLON INT INT COLON NUMBER )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:523:2: INT INT COLON INT INT COLON NUMBER ( PLUS | MINUS ) INT INT COLON INT INT COLON NUMBER
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:527:2: ( TERM_START_CHAR ( TERM_CHAR )* )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:528:2: TERM_START_CHAR ( TERM_CHAR )*
            {
            mTERM_START_CHAR(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:528:18: ( TERM_CHAR )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0 >= '\u0000' && LA18_0 <= '\b')||(LA18_0 >= '\u000B' && LA18_0 <= '\f')||(LA18_0 >= '\u000E' && LA18_0 <= '\u001F')||(LA18_0 >= '#' && LA18_0 <= '&')||LA18_0=='+'||(LA18_0 >= '-' && LA18_0 <= '9')||(LA18_0 >= '<' && LA18_0 <= '>')||(LA18_0 >= '@' && LA18_0 <= 'Z')||LA18_0=='\\'||(LA18_0 >= '_' && LA18_0 <= 'z')||LA18_0=='|'||(LA18_0 >= '\u007F' && LA18_0 <= '\u2FFF')||(LA18_0 >= '\u3001' && LA18_0 <= '\uFFFF')) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:528:20: TERM_CHAR
            	    {
            	    mTERM_CHAR(); 


            	    }
            	    break;

            	default :
            	    break loop18;
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:532:15: ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ )
            int alt30=3;
            alt30 = dfa30.predict(input);
            switch (alt30) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:2: ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )*
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:2: ( STAR | QMARK )
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0=='*') ) {
                        alt19=1;
                    }
                    else if ( (LA19_0=='?') ) {
                        alt19=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 0, input);

                        throw nvae;

                    }
                    switch (alt19) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:3: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:8: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+
                    int cnt22=0;
                    loop22:
                    do {
                        int alt22=2;
                        alt22 = dfa22.predict(input);
                        switch (alt22) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:16: ( TERM_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:16: ( TERM_CHAR )+
                    	    int cnt20=0;
                    	    loop20:
                    	    do {
                    	        int alt20=2;
                    	        int LA20_0 = input.LA(1);

                    	        if ( ((LA20_0 >= '\u0000' && LA20_0 <= '\b')||(LA20_0 >= '\u000B' && LA20_0 <= '\f')||(LA20_0 >= '\u000E' && LA20_0 <= '\u001F')||(LA20_0 >= '#' && LA20_0 <= '&')||LA20_0=='+'||(LA20_0 >= '-' && LA20_0 <= '9')||(LA20_0 >= '<' && LA20_0 <= '>')||(LA20_0 >= '@' && LA20_0 <= 'Z')||LA20_0=='\\'||(LA20_0 >= '_' && LA20_0 <= 'z')||LA20_0=='|'||(LA20_0 >= '\u007F' && LA20_0 <= '\u2FFF')||(LA20_0 >= '\u3001' && LA20_0 <= '\uFFFF')) ) {
                    	            alt20=1;
                    	        }


                    	        switch (alt20) {
                    	    	case 1 :
                    	    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:16: TERM_CHAR
                    	    	    {
                    	    	    mTERM_CHAR(); 


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


                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:27: ( QMARK | STAR )
                    	    int alt21=2;
                    	    int LA21_0 = input.LA(1);

                    	    if ( (LA21_0=='?') ) {
                    	        alt21=1;
                    	    }
                    	    else if ( (LA21_0=='*') ) {
                    	        alt21=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 21, 0, input);

                    	        throw nvae;

                    	    }
                    	    switch (alt21) {
                    	        case 1 :
                    	            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:28: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:34: STAR
                    	            {
                    	            mSTAR(); 


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt22 >= 1 ) break loop22;
                                EarlyExitException eee =
                                    new EarlyExitException(22, input);
                                throw eee;
                        }
                        cnt22++;
                    } while (true);


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:42: ( TERM_CHAR )*
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( ((LA23_0 >= '\u0000' && LA23_0 <= '\b')||(LA23_0 >= '\u000B' && LA23_0 <= '\f')||(LA23_0 >= '\u000E' && LA23_0 <= '\u001F')||(LA23_0 >= '#' && LA23_0 <= '&')||LA23_0=='+'||(LA23_0 >= '-' && LA23_0 <= '9')||(LA23_0 >= '<' && LA23_0 <= '>')||(LA23_0 >= '@' && LA23_0 <= 'Z')||LA23_0=='\\'||(LA23_0 >= '_' && LA23_0 <= 'z')||LA23_0=='|'||(LA23_0 >= '\u007F' && LA23_0 <= '\u2FFF')||(LA23_0 >= '\u3001' && LA23_0 <= '\uFFFF')) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:533:43: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop23;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:4: TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )*
                    {
                    mTERM_START_CHAR(); 


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+
                    int cnt26=0;
                    loop26:
                    do {
                        int alt26=2;
                        alt26 = dfa26.predict(input);
                        switch (alt26) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:21: ( TERM_CHAR )* ( QMARK | STAR )
                    	    {
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:21: ( TERM_CHAR )*
                    	    loop24:
                    	    do {
                    	        int alt24=2;
                    	        int LA24_0 = input.LA(1);

                    	        if ( ((LA24_0 >= '\u0000' && LA24_0 <= '\b')||(LA24_0 >= '\u000B' && LA24_0 <= '\f')||(LA24_0 >= '\u000E' && LA24_0 <= '\u001F')||(LA24_0 >= '#' && LA24_0 <= '&')||LA24_0=='+'||(LA24_0 >= '-' && LA24_0 <= '9')||(LA24_0 >= '<' && LA24_0 <= '>')||(LA24_0 >= '@' && LA24_0 <= 'Z')||LA24_0=='\\'||(LA24_0 >= '_' && LA24_0 <= 'z')||LA24_0=='|'||(LA24_0 >= '\u007F' && LA24_0 <= '\u2FFF')||(LA24_0 >= '\u3001' && LA24_0 <= '\uFFFF')) ) {
                    	            alt24=1;
                    	        }


                    	        switch (alt24) {
                    	    	case 1 :
                    	    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:21: TERM_CHAR
                    	    	    {
                    	    	    mTERM_CHAR(); 


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    break loop24;
                    	        }
                    	    } while (true);


                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:32: ( QMARK | STAR )
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
                    	            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:33: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:39: STAR
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


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:47: ( TERM_CHAR )*
                    loop27:
                    do {
                        int alt27=2;
                        int LA27_0 = input.LA(1);

                        if ( ((LA27_0 >= '\u0000' && LA27_0 <= '\b')||(LA27_0 >= '\u000B' && LA27_0 <= '\f')||(LA27_0 >= '\u000E' && LA27_0 <= '\u001F')||(LA27_0 >= '#' && LA27_0 <= '&')||LA27_0=='+'||(LA27_0 >= '-' && LA27_0 <= '9')||(LA27_0 >= '<' && LA27_0 <= '>')||(LA27_0 >= '@' && LA27_0 <= 'Z')||LA27_0=='\\'||(LA27_0 >= '_' && LA27_0 <= 'z')||LA27_0=='|'||(LA27_0 >= '\u007F' && LA27_0 <= '\u2FFF')||(LA27_0 >= '\u3001' && LA27_0 <= '\uFFFF')) ) {
                            alt27=1;
                        }


                        switch (alt27) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:534:48: TERM_CHAR
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
                case 3 :
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:535:4: ( STAR | QMARK ) ( TERM_CHAR )+
                    {
                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:535:4: ( STAR | QMARK )
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0=='*') ) {
                        alt28=1;
                    }
                    else if ( (LA28_0=='?') ) {
                        alt28=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 28, 0, input);

                        throw nvae;

                    }
                    switch (alt28) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:535:5: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:535:10: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:535:17: ( TERM_CHAR )+
                    int cnt29=0;
                    loop29:
                    do {
                        int alt29=2;
                        int LA29_0 = input.LA(1);

                        if ( ((LA29_0 >= '\u0000' && LA29_0 <= '\b')||(LA29_0 >= '\u000B' && LA29_0 <= '\f')||(LA29_0 >= '\u000E' && LA29_0 <= '\u001F')||(LA29_0 >= '#' && LA29_0 <= '&')||LA29_0=='+'||(LA29_0 >= '-' && LA29_0 <= '9')||(LA29_0 >= '<' && LA29_0 <= '>')||(LA29_0 >= '@' && LA29_0 <= 'Z')||LA29_0=='\\'||(LA29_0 >= '_' && LA29_0 <= 'z')||LA29_0=='|'||(LA29_0 >= '\u007F' && LA29_0 <= '\u2FFF')||(LA29_0 >= '\u3001' && LA29_0 <= '\uFFFF')) ) {
                            alt29=1;
                        }


                        switch (alt29) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:535:17: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt29 >= 1 ) break loop29;
                                EarlyExitException eee =
                                    new EarlyExitException(29, input);
                                throw eee;
                        }
                        cnt29++;
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:540:2: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:541:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:541:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+
            int cnt31=0;
            loop31:
            do {
                int alt31=3;
                int LA31_0 = input.LA(1);

                if ( (LA31_0=='\\') ) {
                    alt31=1;
                }
                else if ( ((LA31_0 >= '\u0000' && LA31_0 <= '!')||(LA31_0 >= '#' && LA31_0 <= ')')||(LA31_0 >= '+' && LA31_0 <= '>')||(LA31_0 >= '@' && LA31_0 <= '[')||(LA31_0 >= ']' && LA31_0 <= '\uFFFF')) ) {
                    alt31=2;
                }


                switch (alt31) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:541:10: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:541:19: ~ ( '\\\"' | '\\\\' | '?' | '*' )
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
            	    if ( cnt31 >= 1 ) break loop31;
                        EarlyExitException eee =
                            new EarlyExitException(31, input);
                        throw eee;
                }
                cnt31++;
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
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:544:17: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE )
            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:545:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:545:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+
            int cnt32=0;
            loop32:
            do {
                int alt32=3;
                int LA32_0 = input.LA(1);

                if ( (LA32_0=='\\') ) {
                    alt32=1;
                }
                else if ( ((LA32_0 >= '\u0000' && LA32_0 <= '!')||(LA32_0 >= '#' && LA32_0 <= '[')||(LA32_0 >= ']' && LA32_0 <= '\uFFFF')) ) {
                    alt32=2;
                }


                switch (alt32) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:545:10: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:545:19: ~ ( '\\\"' | '\\\\' )
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
            	    if ( cnt32 >= 1 ) break loop32;
                        EarlyExitException eee =
                            new EarlyExitException(32, input);
                        throw eee;
                }
                cnt32++;
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
        // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:8: ( T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | CARAT | TILDE | DQUOTE | SQUOTE | COMMA | SEMICOLON | TO | AND | OR | NOT | NEAR | AUTHOR_SEARCH | DATE_RANGE | FUNC_NAME | WS | DATE_TOKEN | NUMBER | HOUR | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING )
        int alt33=38;
        alt33 = dfa33.predict(input);
        switch (alt33) {
            case 1 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:10: T__67
                {
                mT__67(); 


                }
                break;
            case 2 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:16: T__68
                {
                mT__68(); 


                }
                break;
            case 3 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:22: T__69
                {
                mT__69(); 


                }
                break;
            case 4 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:28: T__70
                {
                mT__70(); 


                }
                break;
            case 5 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:34: T__71
                {
                mT__71(); 


                }
                break;
            case 6 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:40: T__72
                {
                mT__72(); 


                }
                break;
            case 7 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:46: T__73
                {
                mT__73(); 


                }
                break;
            case 8 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:52: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 9 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:59: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 10 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:66: LBRACK
                {
                mLBRACK(); 


                }
                break;
            case 11 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:73: RBRACK
                {
                mRBRACK(); 


                }
                break;
            case 12 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:80: COLON
                {
                mCOLON(); 


                }
                break;
            case 13 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:86: PLUS
                {
                mPLUS(); 


                }
                break;
            case 14 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:91: MINUS
                {
                mMINUS(); 


                }
                break;
            case 15 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:97: STAR
                {
                mSTAR(); 


                }
                break;
            case 16 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:102: QMARK
                {
                mQMARK(); 


                }
                break;
            case 17 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:108: CARAT
                {
                mCARAT(); 


                }
                break;
            case 18 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:114: TILDE
                {
                mTILDE(); 


                }
                break;
            case 19 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:120: DQUOTE
                {
                mDQUOTE(); 


                }
                break;
            case 20 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:127: SQUOTE
                {
                mSQUOTE(); 


                }
                break;
            case 21 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:134: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 22 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:140: SEMICOLON
                {
                mSEMICOLON(); 


                }
                break;
            case 23 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:150: TO
                {
                mTO(); 


                }
                break;
            case 24 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:153: AND
                {
                mAND(); 


                }
                break;
            case 25 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:157: OR
                {
                mOR(); 


                }
                break;
            case 26 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:160: NOT
                {
                mNOT(); 


                }
                break;
            case 27 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:164: NEAR
                {
                mNEAR(); 


                }
                break;
            case 28 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:169: AUTHOR_SEARCH
                {
                mAUTHOR_SEARCH(); 


                }
                break;
            case 29 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:183: DATE_RANGE
                {
                mDATE_RANGE(); 


                }
                break;
            case 30 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:194: FUNC_NAME
                {
                mFUNC_NAME(); 


                }
                break;
            case 31 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:204: WS
                {
                mWS(); 


                }
                break;
            case 32 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:207: DATE_TOKEN
                {
                mDATE_TOKEN(); 


                }
                break;
            case 33 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:218: NUMBER
                {
                mNUMBER(); 


                }
                break;
            case 34 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:225: HOUR
                {
                mHOUR(); 


                }
                break;
            case 35 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:230: TERM_NORMAL
                {
                mTERM_NORMAL(); 


                }
                break;
            case 36 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:242: TERM_TRUNCATED
                {
                mTERM_TRUNCATED(); 


                }
                break;
            case 37 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:257: PHRASE
                {
                mPHRASE(); 


                }
                break;
            case 38 :
                // /dvt/workspace/montysolr/contrib/adsabs/grammars/ADS.g:1:264: PHRASE_ANYTHING
                {
                mPHRASE_ANYTHING(); 


                }
                break;

        }

    }


    protected DFA30 dfa30 = new DFA30(this);
    protected DFA22 dfa22 = new DFA22(this);
    protected DFA26 dfa26 = new DFA26(this);
    protected DFA33 dfa33 = new DFA33(this);
    static final String DFA30_eotS =
        "\4\uffff\1\13\1\uffff\5\13\2\uffff\1\13";
    static final String DFA30_eofS =
        "\16\uffff";
    static final String DFA30_minS =
        "\3\0\1\uffff\7\0\2\uffff\1\0";
    static final String DFA30_maxS =
        "\3\uffff\1\uffff\7\uffff\2\uffff\1\uffff";
    static final String DFA30_acceptS =
        "\3\uffff\1\2\7\uffff\1\3\1\1\1\uffff";
    static final String DFA30_specialS =
        "\1\6\1\3\1\1\1\uffff\1\5\1\7\1\2\1\4\1\12\1\11\1\10\2\uffff\1\0}>";
    static final String[] DFA30_transitionS = {
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
            return "532:1: TERM_TRUNCATED : ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA30_13 = input.LA(1);

                        s = -1;
                        if ( (LA30_13=='*'||LA30_13=='?') ) {s = 12;}

                        else if ( ((LA30_13 >= '\u0000' && LA30_13 <= '\b')||(LA30_13 >= '\u000B' && LA30_13 <= '\f')||(LA30_13 >= '\u000E' && LA30_13 <= '\u001F')||(LA30_13 >= '$' && LA30_13 <= '&')||(LA30_13 >= '.' && LA30_13 <= '9')||LA30_13=='<'||LA30_13=='>'||(LA30_13 >= '@' && LA30_13 <= 'Z')||(LA30_13 >= '_' && LA30_13 <= 'z')||LA30_13=='|'||(LA30_13 >= '\u007F' && LA30_13 <= '\u2012')||(LA30_13 >= '\u2014' && LA30_13 <= '\u2FFF')||(LA30_13 >= '\u3001' && LA30_13 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA30_13=='\\') ) {s = 5;}

                        else if ( (LA30_13=='+') ) {s = 6;}

                        else if ( (LA30_13=='-') ) {s = 7;}

                        else if ( (LA30_13=='\u2013') ) {s = 8;}

                        else if ( (LA30_13=='=') ) {s = 9;}

                        else if ( (LA30_13=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA30_2 = input.LA(1);

                        s = -1;
                        if ( ((LA30_2 >= '\u0000' && LA30_2 <= '\b')||(LA30_2 >= '\u000B' && LA30_2 <= '\f')||(LA30_2 >= '\u000E' && LA30_2 <= '\u001F')||(LA30_2 >= '$' && LA30_2 <= '&')||(LA30_2 >= '.' && LA30_2 <= '9')||LA30_2=='<'||LA30_2=='>'||(LA30_2 >= '@' && LA30_2 <= 'Z')||(LA30_2 >= '_' && LA30_2 <= 'z')||LA30_2=='|'||(LA30_2 >= '\u007F' && LA30_2 <= '\u2012')||(LA30_2 >= '\u2014' && LA30_2 <= '\u2FFF')||(LA30_2 >= '\u3001' && LA30_2 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA30_2=='\\') ) {s = 5;}

                        else if ( (LA30_2=='+') ) {s = 6;}

                        else if ( (LA30_2=='-') ) {s = 7;}

                        else if ( (LA30_2=='\u2013') ) {s = 8;}

                        else if ( (LA30_2=='=') ) {s = 9;}

                        else if ( (LA30_2=='#') ) {s = 10;}

                        else if ( (LA30_2=='?') ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA30_6 = input.LA(1);

                        s = -1;
                        if ( (LA30_6=='*'||LA30_6=='?') ) {s = 12;}

                        else if ( ((LA30_6 >= '\u0000' && LA30_6 <= '\b')||(LA30_6 >= '\u000B' && LA30_6 <= '\f')||(LA30_6 >= '\u000E' && LA30_6 <= '\u001F')||(LA30_6 >= '$' && LA30_6 <= '&')||(LA30_6 >= '.' && LA30_6 <= '9')||LA30_6=='<'||LA30_6=='>'||(LA30_6 >= '@' && LA30_6 <= 'Z')||(LA30_6 >= '_' && LA30_6 <= 'z')||LA30_6=='|'||(LA30_6 >= '\u007F' && LA30_6 <= '\u2012')||(LA30_6 >= '\u2014' && LA30_6 <= '\u2FFF')||(LA30_6 >= '\u3001' && LA30_6 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA30_6=='\\') ) {s = 5;}

                        else if ( (LA30_6=='+') ) {s = 6;}

                        else if ( (LA30_6=='-') ) {s = 7;}

                        else if ( (LA30_6=='\u2013') ) {s = 8;}

                        else if ( (LA30_6=='=') ) {s = 9;}

                        else if ( (LA30_6=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA30_1 = input.LA(1);

                        s = -1;
                        if ( ((LA30_1 >= '\u0000' && LA30_1 <= '\b')||(LA30_1 >= '\u000B' && LA30_1 <= '\f')||(LA30_1 >= '\u000E' && LA30_1 <= '\u001F')||(LA30_1 >= '$' && LA30_1 <= '&')||(LA30_1 >= '.' && LA30_1 <= '9')||LA30_1=='<'||LA30_1=='>'||(LA30_1 >= '@' && LA30_1 <= 'Z')||(LA30_1 >= '_' && LA30_1 <= 'z')||LA30_1=='|'||(LA30_1 >= '\u007F' && LA30_1 <= '\u2012')||(LA30_1 >= '\u2014' && LA30_1 <= '\u2FFF')||(LA30_1 >= '\u3001' && LA30_1 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA30_1=='\\') ) {s = 5;}

                        else if ( (LA30_1=='+') ) {s = 6;}

                        else if ( (LA30_1=='-') ) {s = 7;}

                        else if ( (LA30_1=='\u2013') ) {s = 8;}

                        else if ( (LA30_1=='=') ) {s = 9;}

                        else if ( (LA30_1=='#') ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA30_7 = input.LA(1);

                        s = -1;
                        if ( (LA30_7=='*'||LA30_7=='?') ) {s = 12;}

                        else if ( ((LA30_7 >= '\u0000' && LA30_7 <= '\b')||(LA30_7 >= '\u000B' && LA30_7 <= '\f')||(LA30_7 >= '\u000E' && LA30_7 <= '\u001F')||(LA30_7 >= '$' && LA30_7 <= '&')||(LA30_7 >= '.' && LA30_7 <= '9')||LA30_7=='<'||LA30_7=='>'||(LA30_7 >= '@' && LA30_7 <= 'Z')||(LA30_7 >= '_' && LA30_7 <= 'z')||LA30_7=='|'||(LA30_7 >= '\u007F' && LA30_7 <= '\u2012')||(LA30_7 >= '\u2014' && LA30_7 <= '\u2FFF')||(LA30_7 >= '\u3001' && LA30_7 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA30_7=='\\') ) {s = 5;}

                        else if ( (LA30_7=='+') ) {s = 6;}

                        else if ( (LA30_7=='-') ) {s = 7;}

                        else if ( (LA30_7=='\u2013') ) {s = 8;}

                        else if ( (LA30_7=='=') ) {s = 9;}

                        else if ( (LA30_7=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA30_4 = input.LA(1);

                        s = -1;
                        if ( (LA30_4=='*'||LA30_4=='?') ) {s = 12;}

                        else if ( ((LA30_4 >= '\u0000' && LA30_4 <= '\b')||(LA30_4 >= '\u000B' && LA30_4 <= '\f')||(LA30_4 >= '\u000E' && LA30_4 <= '\u001F')||(LA30_4 >= '$' && LA30_4 <= '&')||(LA30_4 >= '.' && LA30_4 <= '9')||LA30_4=='<'||LA30_4=='>'||(LA30_4 >= '@' && LA30_4 <= 'Z')||(LA30_4 >= '_' && LA30_4 <= 'z')||LA30_4=='|'||(LA30_4 >= '\u007F' && LA30_4 <= '\u2012')||(LA30_4 >= '\u2014' && LA30_4 <= '\u2FFF')||(LA30_4 >= '\u3001' && LA30_4 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA30_4=='\\') ) {s = 5;}

                        else if ( (LA30_4=='+') ) {s = 6;}

                        else if ( (LA30_4=='-') ) {s = 7;}

                        else if ( (LA30_4=='\u2013') ) {s = 8;}

                        else if ( (LA30_4=='=') ) {s = 9;}

                        else if ( (LA30_4=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA30_0 = input.LA(1);

                        s = -1;
                        if ( (LA30_0=='*') ) {s = 1;}

                        else if ( (LA30_0=='?') ) {s = 2;}

                        else if ( ((LA30_0 >= '\u0000' && LA30_0 <= '\b')||(LA30_0 >= '\u000B' && LA30_0 <= '\f')||(LA30_0 >= '\u000E' && LA30_0 <= '\u001F')||(LA30_0 >= '$' && LA30_0 <= '&')||(LA30_0 >= '.' && LA30_0 <= '9')||LA30_0=='<'||LA30_0=='>'||(LA30_0 >= '@' && LA30_0 <= 'Z')||LA30_0=='\\'||(LA30_0 >= '_' && LA30_0 <= 'z')||LA30_0=='|'||(LA30_0 >= '\u007F' && LA30_0 <= '\u2012')||(LA30_0 >= '\u2014' && LA30_0 <= '\u2FFF')||(LA30_0 >= '\u3001' && LA30_0 <= '\uFFFF')) ) {s = 3;}

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA30_5 = input.LA(1);

                        s = -1;
                        if ( ((LA30_5 >= '\u0000' && LA30_5 <= '\uFFFF')) ) {s = 13;}

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA30_10 = input.LA(1);

                        s = -1;
                        if ( (LA30_10=='*'||LA30_10=='?') ) {s = 12;}

                        else if ( ((LA30_10 >= '\u0000' && LA30_10 <= '\b')||(LA30_10 >= '\u000B' && LA30_10 <= '\f')||(LA30_10 >= '\u000E' && LA30_10 <= '\u001F')||(LA30_10 >= '$' && LA30_10 <= '&')||(LA30_10 >= '.' && LA30_10 <= '9')||LA30_10=='<'||LA30_10=='>'||(LA30_10 >= '@' && LA30_10 <= 'Z')||(LA30_10 >= '_' && LA30_10 <= 'z')||LA30_10=='|'||(LA30_10 >= '\u007F' && LA30_10 <= '\u2012')||(LA30_10 >= '\u2014' && LA30_10 <= '\u2FFF')||(LA30_10 >= '\u3001' && LA30_10 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA30_10=='\\') ) {s = 5;}

                        else if ( (LA30_10=='+') ) {s = 6;}

                        else if ( (LA30_10=='-') ) {s = 7;}

                        else if ( (LA30_10=='\u2013') ) {s = 8;}

                        else if ( (LA30_10=='=') ) {s = 9;}

                        else if ( (LA30_10=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA30_9 = input.LA(1);

                        s = -1;
                        if ( (LA30_9=='*'||LA30_9=='?') ) {s = 12;}

                        else if ( ((LA30_9 >= '\u0000' && LA30_9 <= '\b')||(LA30_9 >= '\u000B' && LA30_9 <= '\f')||(LA30_9 >= '\u000E' && LA30_9 <= '\u001F')||(LA30_9 >= '$' && LA30_9 <= '&')||(LA30_9 >= '.' && LA30_9 <= '9')||LA30_9=='<'||LA30_9=='>'||(LA30_9 >= '@' && LA30_9 <= 'Z')||(LA30_9 >= '_' && LA30_9 <= 'z')||LA30_9=='|'||(LA30_9 >= '\u007F' && LA30_9 <= '\u2012')||(LA30_9 >= '\u2014' && LA30_9 <= '\u2FFF')||(LA30_9 >= '\u3001' && LA30_9 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA30_9=='\\') ) {s = 5;}

                        else if ( (LA30_9=='+') ) {s = 6;}

                        else if ( (LA30_9=='-') ) {s = 7;}

                        else if ( (LA30_9=='\u2013') ) {s = 8;}

                        else if ( (LA30_9=='=') ) {s = 9;}

                        else if ( (LA30_9=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA30_8 = input.LA(1);

                        s = -1;
                        if ( (LA30_8=='*'||LA30_8=='?') ) {s = 12;}

                        else if ( ((LA30_8 >= '\u0000' && LA30_8 <= '\b')||(LA30_8 >= '\u000B' && LA30_8 <= '\f')||(LA30_8 >= '\u000E' && LA30_8 <= '\u001F')||(LA30_8 >= '$' && LA30_8 <= '&')||(LA30_8 >= '.' && LA30_8 <= '9')||LA30_8=='<'||LA30_8=='>'||(LA30_8 >= '@' && LA30_8 <= 'Z')||(LA30_8 >= '_' && LA30_8 <= 'z')||LA30_8=='|'||(LA30_8 >= '\u007F' && LA30_8 <= '\u2012')||(LA30_8 >= '\u2014' && LA30_8 <= '\u2FFF')||(LA30_8 >= '\u3001' && LA30_8 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA30_8=='\\') ) {s = 5;}

                        else if ( (LA30_8=='+') ) {s = 6;}

                        else if ( (LA30_8=='-') ) {s = 7;}

                        else if ( (LA30_8=='\u2013') ) {s = 8;}

                        else if ( (LA30_8=='=') ) {s = 9;}

                        else if ( (LA30_8=='#') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 30, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA22_eotS =
        "\2\10\1\uffff\5\10\2\uffff\1\10";
    static final String DFA22_eofS =
        "\13\uffff";
    static final String DFA22_minS =
        "\10\0\2\uffff\1\0";
    static final String DFA22_maxS =
        "\10\uffff\2\uffff\1\uffff";
    static final String DFA22_acceptS =
        "\10\uffff\1\2\1\1\1\uffff";
    static final String DFA22_specialS =
        "\1\2\1\10\1\5\1\4\1\6\1\7\1\0\1\3\2\uffff\1\1}>";
    static final String[] DFA22_transitionS = {
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

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "()+ loopback of 533:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA22_6 = input.LA(1);

                        s = -1;
                        if ( ((LA22_6 >= '\u0000' && LA22_6 <= '\b')||(LA22_6 >= '\u000B' && LA22_6 <= '\f')||(LA22_6 >= '\u000E' && LA22_6 <= '\u001F')||(LA22_6 >= '$' && LA22_6 <= '&')||(LA22_6 >= '.' && LA22_6 <= '9')||LA22_6=='<'||LA22_6=='>'||(LA22_6 >= '@' && LA22_6 <= 'Z')||(LA22_6 >= '_' && LA22_6 <= 'z')||LA22_6=='|'||(LA22_6 >= '\u007F' && LA22_6 <= '\u2012')||(LA22_6 >= '\u2014' && LA22_6 <= '\u2FFF')||(LA22_6 >= '\u3001' && LA22_6 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_6=='\\') ) {s = 2;}

                        else if ( (LA22_6=='+') ) {s = 3;}

                        else if ( (LA22_6=='-') ) {s = 4;}

                        else if ( (LA22_6=='\u2013') ) {s = 5;}

                        else if ( (LA22_6=='=') ) {s = 6;}

                        else if ( (LA22_6=='#') ) {s = 7;}

                        else if ( (LA22_6=='*'||LA22_6=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA22_10 = input.LA(1);

                        s = -1;
                        if ( ((LA22_10 >= '\u0000' && LA22_10 <= '\b')||(LA22_10 >= '\u000B' && LA22_10 <= '\f')||(LA22_10 >= '\u000E' && LA22_10 <= '\u001F')||(LA22_10 >= '$' && LA22_10 <= '&')||(LA22_10 >= '.' && LA22_10 <= '9')||LA22_10=='<'||LA22_10=='>'||(LA22_10 >= '@' && LA22_10 <= 'Z')||(LA22_10 >= '_' && LA22_10 <= 'z')||LA22_10=='|'||(LA22_10 >= '\u007F' && LA22_10 <= '\u2012')||(LA22_10 >= '\u2014' && LA22_10 <= '\u2FFF')||(LA22_10 >= '\u3001' && LA22_10 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_10=='\\') ) {s = 2;}

                        else if ( (LA22_10=='+') ) {s = 3;}

                        else if ( (LA22_10=='-') ) {s = 4;}

                        else if ( (LA22_10=='\u2013') ) {s = 5;}

                        else if ( (LA22_10=='=') ) {s = 6;}

                        else if ( (LA22_10=='#') ) {s = 7;}

                        else if ( (LA22_10=='*'||LA22_10=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA22_0 = input.LA(1);

                        s = -1;
                        if ( ((LA22_0 >= '\u0000' && LA22_0 <= '\b')||(LA22_0 >= '\u000B' && LA22_0 <= '\f')||(LA22_0 >= '\u000E' && LA22_0 <= '\u001F')||(LA22_0 >= '$' && LA22_0 <= '&')||(LA22_0 >= '.' && LA22_0 <= '9')||LA22_0=='<'||LA22_0=='>'||(LA22_0 >= '@' && LA22_0 <= 'Z')||(LA22_0 >= '_' && LA22_0 <= 'z')||LA22_0=='|'||(LA22_0 >= '\u007F' && LA22_0 <= '\u2012')||(LA22_0 >= '\u2014' && LA22_0 <= '\u2FFF')||(LA22_0 >= '\u3001' && LA22_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_0=='\\') ) {s = 2;}

                        else if ( (LA22_0=='+') ) {s = 3;}

                        else if ( (LA22_0=='-') ) {s = 4;}

                        else if ( (LA22_0=='\u2013') ) {s = 5;}

                        else if ( (LA22_0=='=') ) {s = 6;}

                        else if ( (LA22_0=='#') ) {s = 7;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA22_7 = input.LA(1);

                        s = -1;
                        if ( ((LA22_7 >= '\u0000' && LA22_7 <= '\b')||(LA22_7 >= '\u000B' && LA22_7 <= '\f')||(LA22_7 >= '\u000E' && LA22_7 <= '\u001F')||(LA22_7 >= '$' && LA22_7 <= '&')||(LA22_7 >= '.' && LA22_7 <= '9')||LA22_7=='<'||LA22_7=='>'||(LA22_7 >= '@' && LA22_7 <= 'Z')||(LA22_7 >= '_' && LA22_7 <= 'z')||LA22_7=='|'||(LA22_7 >= '\u007F' && LA22_7 <= '\u2012')||(LA22_7 >= '\u2014' && LA22_7 <= '\u2FFF')||(LA22_7 >= '\u3001' && LA22_7 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_7=='\\') ) {s = 2;}

                        else if ( (LA22_7=='+') ) {s = 3;}

                        else if ( (LA22_7=='-') ) {s = 4;}

                        else if ( (LA22_7=='\u2013') ) {s = 5;}

                        else if ( (LA22_7=='=') ) {s = 6;}

                        else if ( (LA22_7=='#') ) {s = 7;}

                        else if ( (LA22_7=='*'||LA22_7=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA22_3 = input.LA(1);

                        s = -1;
                        if ( ((LA22_3 >= '\u0000' && LA22_3 <= '\b')||(LA22_3 >= '\u000B' && LA22_3 <= '\f')||(LA22_3 >= '\u000E' && LA22_3 <= '\u001F')||(LA22_3 >= '$' && LA22_3 <= '&')||(LA22_3 >= '.' && LA22_3 <= '9')||LA22_3=='<'||LA22_3=='>'||(LA22_3 >= '@' && LA22_3 <= 'Z')||(LA22_3 >= '_' && LA22_3 <= 'z')||LA22_3=='|'||(LA22_3 >= '\u007F' && LA22_3 <= '\u2012')||(LA22_3 >= '\u2014' && LA22_3 <= '\u2FFF')||(LA22_3 >= '\u3001' && LA22_3 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_3=='\\') ) {s = 2;}

                        else if ( (LA22_3=='+') ) {s = 3;}

                        else if ( (LA22_3=='-') ) {s = 4;}

                        else if ( (LA22_3=='\u2013') ) {s = 5;}

                        else if ( (LA22_3=='=') ) {s = 6;}

                        else if ( (LA22_3=='#') ) {s = 7;}

                        else if ( (LA22_3=='*'||LA22_3=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA22_2 = input.LA(1);

                        s = -1;
                        if ( ((LA22_2 >= '\u0000' && LA22_2 <= '\uFFFF')) ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA22_4 = input.LA(1);

                        s = -1;
                        if ( ((LA22_4 >= '\u0000' && LA22_4 <= '\b')||(LA22_4 >= '\u000B' && LA22_4 <= '\f')||(LA22_4 >= '\u000E' && LA22_4 <= '\u001F')||(LA22_4 >= '$' && LA22_4 <= '&')||(LA22_4 >= '.' && LA22_4 <= '9')||LA22_4=='<'||LA22_4=='>'||(LA22_4 >= '@' && LA22_4 <= 'Z')||(LA22_4 >= '_' && LA22_4 <= 'z')||LA22_4=='|'||(LA22_4 >= '\u007F' && LA22_4 <= '\u2012')||(LA22_4 >= '\u2014' && LA22_4 <= '\u2FFF')||(LA22_4 >= '\u3001' && LA22_4 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_4=='\\') ) {s = 2;}

                        else if ( (LA22_4=='+') ) {s = 3;}

                        else if ( (LA22_4=='-') ) {s = 4;}

                        else if ( (LA22_4=='\u2013') ) {s = 5;}

                        else if ( (LA22_4=='=') ) {s = 6;}

                        else if ( (LA22_4=='#') ) {s = 7;}

                        else if ( (LA22_4=='*'||LA22_4=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA22_5 = input.LA(1);

                        s = -1;
                        if ( ((LA22_5 >= '\u0000' && LA22_5 <= '\b')||(LA22_5 >= '\u000B' && LA22_5 <= '\f')||(LA22_5 >= '\u000E' && LA22_5 <= '\u001F')||(LA22_5 >= '$' && LA22_5 <= '&')||(LA22_5 >= '.' && LA22_5 <= '9')||LA22_5=='<'||LA22_5=='>'||(LA22_5 >= '@' && LA22_5 <= 'Z')||(LA22_5 >= '_' && LA22_5 <= 'z')||LA22_5=='|'||(LA22_5 >= '\u007F' && LA22_5 <= '\u2012')||(LA22_5 >= '\u2014' && LA22_5 <= '\u2FFF')||(LA22_5 >= '\u3001' && LA22_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_5=='\\') ) {s = 2;}

                        else if ( (LA22_5=='+') ) {s = 3;}

                        else if ( (LA22_5=='-') ) {s = 4;}

                        else if ( (LA22_5=='\u2013') ) {s = 5;}

                        else if ( (LA22_5=='=') ) {s = 6;}

                        else if ( (LA22_5=='#') ) {s = 7;}

                        else if ( (LA22_5=='*'||LA22_5=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA22_1 = input.LA(1);

                        s = -1;
                        if ( ((LA22_1 >= '\u0000' && LA22_1 <= '\b')||(LA22_1 >= '\u000B' && LA22_1 <= '\f')||(LA22_1 >= '\u000E' && LA22_1 <= '\u001F')||(LA22_1 >= '$' && LA22_1 <= '&')||(LA22_1 >= '.' && LA22_1 <= '9')||LA22_1=='<'||LA22_1=='>'||(LA22_1 >= '@' && LA22_1 <= 'Z')||(LA22_1 >= '_' && LA22_1 <= 'z')||LA22_1=='|'||(LA22_1 >= '\u007F' && LA22_1 <= '\u2012')||(LA22_1 >= '\u2014' && LA22_1 <= '\u2FFF')||(LA22_1 >= '\u3001' && LA22_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_1=='\\') ) {s = 2;}

                        else if ( (LA22_1=='+') ) {s = 3;}

                        else if ( (LA22_1=='-') ) {s = 4;}

                        else if ( (LA22_1=='\u2013') ) {s = 5;}

                        else if ( (LA22_1=='=') ) {s = 6;}

                        else if ( (LA22_1=='#') ) {s = 7;}

                        else if ( (LA22_1=='*'||LA22_1=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 22, _s, input);
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
        "\1\10\1\2\1\6\1\3\1\5\1\7\1\1\1\4\2\uffff\1\0}>";
    static final String[] DFA26_transitionS = {
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
            return "()+ loopback of 534:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
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

                    case 1 : 
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

                    case 2 : 
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

                    case 3 : 
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

                    case 4 : 
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

                    case 8 : 
                        int LA26_0 = input.LA(1);

                        s = -1;
                        if ( ((LA26_0 >= '\u0000' && LA26_0 <= '\b')||(LA26_0 >= '\u000B' && LA26_0 <= '\f')||(LA26_0 >= '\u000E' && LA26_0 <= '\u001F')||(LA26_0 >= '$' && LA26_0 <= '&')||(LA26_0 >= '.' && LA26_0 <= '9')||LA26_0=='<'||LA26_0=='>'||(LA26_0 >= '@' && LA26_0 <= 'Z')||(LA26_0 >= '_' && LA26_0 <= 'z')||LA26_0=='|'||(LA26_0 >= '\u007F' && LA26_0 <= '\u2012')||(LA26_0 >= '\u2014' && LA26_0 <= '\u2FFF')||(LA26_0 >= '\u3001' && LA26_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_0=='\\') ) {s = 2;}

                        else if ( (LA26_0=='+') ) {s = 3;}

                        else if ( (LA26_0=='-') ) {s = 4;}

                        else if ( (LA26_0=='\u2013') ) {s = 5;}

                        else if ( (LA26_0=='=') ) {s = 6;}

                        else if ( (LA26_0=='#') ) {s = 7;}

                        else if ( (LA26_0=='*'||LA26_0=='?') ) {s = 9;}

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
    static final String DFA33_eotS =
        "\2\uffff\1\37\1\52\1\uffff\2\52\6\uffff\1\32\1\57\1\60\1\61\1\uffff"+
        "\1\63\3\uffff\4\52\1\uffff\1\73\1\52\3\uffff\1\52\1\uffff\5\52\2"+
        "\uffff\1\52\1\uffff\3\52\11\uffff\1\112\1\113\2\52\1\uffff\1\73"+
        "\6\52\1\122\2\52\1\125\1\52\4\uffff\1\130\1\52\1\73\1\uffff\1\73"+
        "\1\52\1\uffff\2\52\4\uffff\1\143\1\73\1\52\1\73\6\52\1\uffff\1\56"+
        "\2\73\1\52\2\uffff\1\52\1\155\1\52\1\uffff\2\52\1\155\1\56";
    static final String DFA33_eofS =
        "\162\uffff";
    static final String DFA33_minS =
        "\1\0\1\uffff\2\0\1\uffff\2\0\6\uffff\1\60\3\0\1\uffff\1\0\3\uffff"+
        "\4\0\1\uffff\3\0\2\uffff\7\0\2\uffff\1\0\1\uffff\3\0\6\uffff\2\0"+
        "\1\uffff\4\0\1\uffff\15\0\3\uffff\3\0\1\uffff\2\0\1\uffff\2\0\4"+
        "\uffff\12\0\1\uffff\4\0\2\uffff\3\0\1\uffff\4\0";
    static final String DFA33_maxS =
        "\1\uffff\1\uffff\2\uffff\1\uffff\2\uffff\6\uffff\1\71\3\uffff\1"+
        "\uffff\1\uffff\3\uffff\4\uffff\1\uffff\3\uffff\2\uffff\7\uffff\2"+
        "\uffff\1\uffff\1\uffff\3\uffff\6\uffff\2\uffff\1\uffff\4\uffff\1"+
        "\uffff\15\uffff\3\uffff\3\uffff\1\uffff\2\uffff\1\uffff\2\uffff"+
        "\4\uffff\12\uffff\1\uffff\4\uffff\2\uffff\3\uffff\1\uffff\4\uffff";
    static final String DFA33_acceptS =
        "\1\uffff\1\1\2\uffff\1\4\2\uffff\1\10\1\11\1\12\1\13\1\14\1\15\4"+
        "\uffff\1\22\1\uffff\1\24\1\25\1\26\4\uffff\1\16\3\uffff\1\37\1\2"+
        "\7\uffff\1\36\1\44\1\uffff\1\43\3\uffff\1\35\1\17\1\20\1\21\1\34"+
        "\1\23\2\uffff\1\46\4\uffff\1\41\15\uffff\1\45\1\27\1\31\3\uffff"+
        "\1\42\2\uffff\1\3\2\uffff\1\30\1\7\1\45\1\32\12\uffff\1\33\4\uffff"+
        "\1\5\1\6\3\uffff\1\40\4\uffff";
    static final String DFA33_specialS =
        "\1\24\1\uffff\1\17\1\100\1\uffff\1\40\1\107\7\uffff\1\2\1\106\1"+
        "\20\1\uffff\1\102\3\uffff\1\34\1\35\1\41\1\31\1\uffff\1\101\1\37"+
        "\1\60\2\uffff\1\62\1\57\1\56\1\64\1\70\1\67\1\71\2\uffff\1\25\1"+
        "\uffff\1\61\1\65\1\32\6\uffff\1\77\1\111\1\uffff\1\51\1\42\1\55"+
        "\1\53\1\uffff\1\74\1\27\1\103\1\13\1\110\1\36\1\6\1\66\1\0\1\10"+
        "\1\52\1\33\1\47\3\uffff\1\50\1\76\1\44\1\uffff\1\11\1\26\1\uffff"+
        "\1\3\1\15\4\uffff\1\30\1\63\1\43\1\54\1\12\1\4\1\5\1\45\1\7\1\16"+
        "\1\uffff\1\104\1\105\1\46\1\73\2\uffff\1\23\1\1\1\22\1\uffff\1\72"+
        "\1\21\1\75\1\14}>";
    static final String[] DFA33_transitionS = {
            "\11\34\2\36\2\34\1\36\22\34\1\36\1\uffff\1\22\1\1\3\34\1\23"+
            "\1\7\1\10\1\16\1\14\1\24\1\15\1\34\1\2\12\33\1\13\1\25\1\3\1"+
            "\4\1\34\1\17\1\34\1\27\14\34\1\31\1\30\4\34\1\26\6\34\1\11\1"+
            "\35\1\12\1\20\2\34\1\5\2\34\1\6\11\34\1\31\1\30\13\34\1\uffff"+
            "\1\34\1\uffff\1\21\u1f94\34\1\32\u0fec\34\1\36\ucfff\34",
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
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\16\40\1\54\14\40\1\uffff\1\41\2\uffff\17\40\1\54"+
            "\3\40\1\53\10\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40"+
            "\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\20\40\1\55\13\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\56",
            "\11\50\2\uffff\2\50\1\uffff\22\50\3\uffff\4\50\4\uffff\1\50"+
            "\1\uffff\15\50\2\uffff\3\50\1\uffff\33\50\1\uffff\1\50\2\uffff"+
            "\34\50\1\uffff\1\50\2\uffff\u2f81\50\1\uffff\ucfff\50",
            "\11\50\2\uffff\2\50\1\uffff\22\50\3\uffff\4\50\4\uffff\1\50"+
            "\1\uffff\15\50\2\uffff\3\50\1\17\33\50\1\uffff\1\50\2\uffff"+
            "\34\50\1\uffff\1\50\2\uffff\u2f81\50\1\uffff\ucfff\50",
            "\40\62\1\uffff\3\62\1\uffff\6\62\3\uffff\2\62\12\uffff\u1fd9"+
            "\62\1\uffff\udfec\62",
            "",
            "\42\65\1\uffff\7\65\1\66\24\65\1\66\34\65\1\64\uffa3\65",
            "",
            "",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\17\40\1\67\13\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\16\40\1\54\14\40\1\uffff\1\41\2\uffff\17\40\1\54"+
            "\14\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\22\40\1\70\10\40\1\uffff\1\41\2\uffff\23\40\1\70"+
            "\10\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\5\40\1\72\11\40\1\71\13\40\1\uffff\1\41\2\uffff\6"+
            "\40\1\72\11\40\1\71\13\40\1\uffff\1\40\2\uffff\u1f94\40\1\44"+
            "\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\76\1\75\1\100\12\74\2\uffff"+
            "\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\77\u0fec\40\1\uffff\ucfff\40",
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
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\30\40\1\104\2\40\1\uffff\1\41\2\uffff\31\40\1\105"+
            "\2\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\4\40\1\106\26\40\1\uffff\1\41\2\uffff\5\40\1\106"+
            "\26\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\12\40\1\107\21\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "",
            "",
            "",
            "",
            "",
            "\0\110",
            "\42\65\1\111\7\65\1\66\24\65\1\66\34\65\1\64\uffa3\65",
            "",
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
            "\1\40\1\50\24\40\1\114\6\40\1\uffff\1\41\2\uffff\25\40\1\114"+
            "\6\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\1\40\1\115\31\40\1\uffff\1\41\2\uffff\2\40\1\115"+
            "\31\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\76\1\75\1\100\12\116\1\117"+
            "\1\uffff\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40"+
            "\1\uffff\1\40\2\uffff\u1f94\40\1\77\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\120\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\121\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\121\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\121\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
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
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\12\40\1\123\21\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\12\40\1\124\21\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\1\126\1\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\42\65\1\111\7\65\1\66\24\65\1\66\34\65\1\64\uffa3\65",
            "",
            "",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\22\40\1\131\10\40\1\uffff\1\41\2\uffff\23\40\1\131"+
            "\10\40\1\uffff\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff"+
            "\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\1\133\1\40\12\132\2\uffff"+
            "\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\135\2\137\12\134\2\uffff\1"+
            "\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\136\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\135\2\137\12\140\2\uffff\1"+
            "\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\136\u0fec\40\1\uffff\ucfff\40",
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
            "",
            "",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\2\uffff\1\40\1\45"+
            "\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40\2\uffff"+
            "\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\144\1\133\1\40\12\145\2\uffff"+
            "\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\146\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\135\2\137\12\146\2\uffff\1"+
            "\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\136\u0fec\40\1\uffff\ucfff\40",
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
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\135\2\137\12\40\2\uffff\1"+
            "\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\136\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\1\150\1\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\14\40\1\151\1\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\152\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\1\133\1\40\12\145\2\uffff"+
            "\1\40\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff"+
            "\1\40\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\146\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\153\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\154\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\156\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\157\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\160\2\uffff\1\40"+
            "\1\45\1\40\1\50\33\40\1\uffff\1\41\2\uffff\34\40\1\uffff\1\40"+
            "\2\uffff\u1f94\40\1\44\u0fec\40\1\uffff\ucfff\40",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\1\46\3\40\1\uffff"+
            "\1\47\1\uffff\1\50\1\42\1\uffff\1\43\2\40\12\161\2\uffff\1\40"+
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

    static final short[] DFA33_eot = DFA.unpackEncodedString(DFA33_eotS);
    static final short[] DFA33_eof = DFA.unpackEncodedString(DFA33_eofS);
    static final char[] DFA33_min = DFA.unpackEncodedStringToUnsignedChars(DFA33_minS);
    static final char[] DFA33_max = DFA.unpackEncodedStringToUnsignedChars(DFA33_maxS);
    static final short[] DFA33_accept = DFA.unpackEncodedString(DFA33_acceptS);
    static final short[] DFA33_special = DFA.unpackEncodedString(DFA33_specialS);
    static final short[][] DFA33_transition;

    static {
        int numStates = DFA33_transitionS.length;
        DFA33_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA33_transition[i] = DFA.unpackEncodedString(DFA33_transitionS[i]);
        }
    }

    class DFA33 extends DFA {

        public DFA33(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 33;
            this.eot = DFA33_eot;
            this.eof = DFA33_eof;
            this.min = DFA33_min;
            this.max = DFA33_max;
            this.accept = DFA33_accept;
            this.special = DFA33_special;
            this.transition = DFA33_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | CARAT | TILDE | DQUOTE | SQUOTE | COMMA | SEMICOLON | TO | AND | OR | NOT | NEAR | AUTHOR_SEARCH | DATE_RANGE | FUNC_NAME | WS | DATE_TOKEN | NUMBER | HOUR | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA33_68 = input.LA(1);

                        s = -1;
                        if ( (LA33_68=='i') ) {s = 83;}

                        else if ( (LA33_68=='(') ) {s = 39;}

                        else if ( ((LA33_68 >= '\u0000' && LA33_68 <= '\b')||(LA33_68 >= '\u000B' && LA33_68 <= '\f')||(LA33_68 >= '\u000E' && LA33_68 <= '\u001F')||(LA33_68 >= '$' && LA33_68 <= '&')||(LA33_68 >= '.' && LA33_68 <= '9')||LA33_68=='<'||LA33_68=='>'||(LA33_68 >= '@' && LA33_68 <= 'Z')||(LA33_68 >= '_' && LA33_68 <= 'h')||(LA33_68 >= 'j' && LA33_68 <= 'z')||LA33_68=='|'||(LA33_68 >= '\u007F' && LA33_68 <= '\u2012')||(LA33_68 >= '\u2014' && LA33_68 <= '\u2FFF')||(LA33_68 >= '\u3001' && LA33_68 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_68=='\\') ) {s = 33;}

                        else if ( (LA33_68=='+') ) {s = 34;}

                        else if ( (LA33_68=='-') ) {s = 35;}

                        else if ( (LA33_68=='\u2013') ) {s = 36;}

                        else if ( (LA33_68=='=') ) {s = 37;}

                        else if ( (LA33_68=='#') ) {s = 38;}

                        else if ( (LA33_68=='*'||LA33_68=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA33_107 = input.LA(1);

                        s = -1;
                        if ( (LA33_107=='(') ) {s = 39;}

                        else if ( ((LA33_107 >= '0' && LA33_107 <= '9')) ) {s = 110;}

                        else if ( (LA33_107=='\\') ) {s = 33;}

                        else if ( (LA33_107=='+') ) {s = 34;}

                        else if ( (LA33_107=='-') ) {s = 35;}

                        else if ( (LA33_107=='\u2013') ) {s = 36;}

                        else if ( (LA33_107=='=') ) {s = 37;}

                        else if ( (LA33_107=='#') ) {s = 38;}

                        else if ( ((LA33_107 >= '\u0000' && LA33_107 <= '\b')||(LA33_107 >= '\u000B' && LA33_107 <= '\f')||(LA33_107 >= '\u000E' && LA33_107 <= '\u001F')||(LA33_107 >= '$' && LA33_107 <= '&')||(LA33_107 >= '.' && LA33_107 <= '/')||LA33_107=='<'||LA33_107=='>'||(LA33_107 >= '@' && LA33_107 <= 'Z')||(LA33_107 >= '_' && LA33_107 <= 'z')||LA33_107=='|'||(LA33_107 >= '\u007F' && LA33_107 <= '\u2012')||(LA33_107 >= '\u2014' && LA33_107 <= '\u2FFF')||(LA33_107 >= '\u3001' && LA33_107 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_107=='*'||LA33_107=='?') ) {s = 40;}

                        else s = 109;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA33_14 = input.LA(1);

                        s = -1;
                        if ( ((LA33_14 >= '\u0000' && LA33_14 <= '\b')||(LA33_14 >= '\u000B' && LA33_14 <= '\f')||(LA33_14 >= '\u000E' && LA33_14 <= '\u001F')||(LA33_14 >= '#' && LA33_14 <= '&')||LA33_14=='+'||(LA33_14 >= '-' && LA33_14 <= '9')||(LA33_14 >= '<' && LA33_14 <= '>')||(LA33_14 >= '@' && LA33_14 <= 'Z')||LA33_14=='\\'||(LA33_14 >= '_' && LA33_14 <= 'z')||LA33_14=='|'||(LA33_14 >= '\u007F' && LA33_14 <= '\u2FFF')||(LA33_14 >= '\u3001' && LA33_14 <= '\uFFFF')) ) {s = 40;}

                        else s = 47;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA33_83 = input.LA(1);

                        s = -1;
                        if ( (LA33_83=='v') ) {s = 97;}

                        else if ( (LA33_83=='(') ) {s = 39;}

                        else if ( ((LA33_83 >= '\u0000' && LA33_83 <= '\b')||(LA33_83 >= '\u000B' && LA33_83 <= '\f')||(LA33_83 >= '\u000E' && LA33_83 <= '\u001F')||(LA33_83 >= '$' && LA33_83 <= '&')||(LA33_83 >= '.' && LA33_83 <= '9')||LA33_83=='<'||LA33_83=='>'||(LA33_83 >= '@' && LA33_83 <= 'Z')||(LA33_83 >= '_' && LA33_83 <= 'u')||(LA33_83 >= 'w' && LA33_83 <= 'z')||LA33_83=='|'||(LA33_83 >= '\u007F' && LA33_83 <= '\u2012')||(LA33_83 >= '\u2014' && LA33_83 <= '\u2FFF')||(LA33_83 >= '\u3001' && LA33_83 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_83=='\\') ) {s = 33;}

                        else if ( (LA33_83=='+') ) {s = 34;}

                        else if ( (LA33_83=='-') ) {s = 35;}

                        else if ( (LA33_83=='\u2013') ) {s = 36;}

                        else if ( (LA33_83=='=') ) {s = 37;}

                        else if ( (LA33_83=='#') ) {s = 38;}

                        else if ( (LA33_83=='*'||LA33_83=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA33_94 = input.LA(1);

                        s = -1;
                        if ( (LA33_94=='(') ) {s = 39;}

                        else if ( ((LA33_94 >= '0' && LA33_94 <= '9')) ) {s = 103;}

                        else if ( (LA33_94=='\\') ) {s = 33;}

                        else if ( (LA33_94=='+') ) {s = 34;}

                        else if ( (LA33_94=='-') ) {s = 35;}

                        else if ( (LA33_94=='\u2013') ) {s = 36;}

                        else if ( (LA33_94=='=') ) {s = 37;}

                        else if ( (LA33_94=='#') ) {s = 38;}

                        else if ( ((LA33_94 >= '\u0000' && LA33_94 <= '\b')||(LA33_94 >= '\u000B' && LA33_94 <= '\f')||(LA33_94 >= '\u000E' && LA33_94 <= '\u001F')||(LA33_94 >= '$' && LA33_94 <= '&')||(LA33_94 >= '.' && LA33_94 <= '/')||LA33_94=='<'||LA33_94=='>'||(LA33_94 >= '@' && LA33_94 <= 'Z')||(LA33_94 >= '_' && LA33_94 <= 'z')||LA33_94=='|'||(LA33_94 >= '\u007F' && LA33_94 <= '\u2012')||(LA33_94 >= '\u2014' && LA33_94 <= '\u2FFF')||(LA33_94 >= '\u3001' && LA33_94 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_94=='*'||LA33_94=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA33_95 = input.LA(1);

                        s = -1;
                        if ( (LA33_95=='(') ) {s = 39;}

                        else if ( ((LA33_95 >= '0' && LA33_95 <= '9')) ) {s = 103;}

                        else if ( (LA33_95=='\\') ) {s = 33;}

                        else if ( (LA33_95=='+') ) {s = 34;}

                        else if ( (LA33_95=='-') ) {s = 35;}

                        else if ( (LA33_95=='\u2013') ) {s = 36;}

                        else if ( (LA33_95=='=') ) {s = 37;}

                        else if ( (LA33_95=='#') ) {s = 38;}

                        else if ( ((LA33_95 >= '\u0000' && LA33_95 <= '\b')||(LA33_95 >= '\u000B' && LA33_95 <= '\f')||(LA33_95 >= '\u000E' && LA33_95 <= '\u001F')||(LA33_95 >= '$' && LA33_95 <= '&')||(LA33_95 >= '.' && LA33_95 <= '/')||LA33_95=='<'||LA33_95=='>'||(LA33_95 >= '@' && LA33_95 <= 'Z')||(LA33_95 >= '_' && LA33_95 <= 'z')||LA33_95=='|'||(LA33_95 >= '\u007F' && LA33_95 <= '\u2012')||(LA33_95 >= '\u2014' && LA33_95 <= '\u2FFF')||(LA33_95 >= '\u3001' && LA33_95 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_95=='*'||LA33_95=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA33_66 = input.LA(1);

                        s = -1;
                        if ( (LA33_66=='(') ) {s = 39;}

                        else if ( ((LA33_66 >= '\u0000' && LA33_66 <= '\b')||(LA33_66 >= '\u000B' && LA33_66 <= '\f')||(LA33_66 >= '\u000E' && LA33_66 <= '\u001F')||(LA33_66 >= '$' && LA33_66 <= '&')||(LA33_66 >= '.' && LA33_66 <= '9')||LA33_66=='<'||LA33_66=='>'||(LA33_66 >= '@' && LA33_66 <= 'Z')||(LA33_66 >= '_' && LA33_66 <= 'z')||LA33_66=='|'||(LA33_66 >= '\u007F' && LA33_66 <= '\u2012')||(LA33_66 >= '\u2014' && LA33_66 <= '\u2FFF')||(LA33_66 >= '\u3001' && LA33_66 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_66=='\\') ) {s = 33;}

                        else if ( (LA33_66=='+') ) {s = 34;}

                        else if ( (LA33_66=='-') ) {s = 35;}

                        else if ( (LA33_66=='\u2013') ) {s = 36;}

                        else if ( (LA33_66=='=') ) {s = 37;}

                        else if ( (LA33_66=='#') ) {s = 38;}

                        else if ( (LA33_66=='*'||LA33_66=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA33_97 = input.LA(1);

                        s = -1;
                        if ( (LA33_97==':') ) {s = 104;}

                        else if ( (LA33_97=='(') ) {s = 39;}

                        else if ( ((LA33_97 >= '\u0000' && LA33_97 <= '\b')||(LA33_97 >= '\u000B' && LA33_97 <= '\f')||(LA33_97 >= '\u000E' && LA33_97 <= '\u001F')||(LA33_97 >= '$' && LA33_97 <= '&')||(LA33_97 >= '.' && LA33_97 <= '9')||LA33_97=='<'||LA33_97=='>'||(LA33_97 >= '@' && LA33_97 <= 'Z')||(LA33_97 >= '_' && LA33_97 <= 'z')||LA33_97=='|'||(LA33_97 >= '\u007F' && LA33_97 <= '\u2012')||(LA33_97 >= '\u2014' && LA33_97 <= '\u2FFF')||(LA33_97 >= '\u3001' && LA33_97 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_97=='\\') ) {s = 33;}

                        else if ( (LA33_97=='+') ) {s = 34;}

                        else if ( (LA33_97=='-') ) {s = 35;}

                        else if ( (LA33_97=='\u2013') ) {s = 36;}

                        else if ( (LA33_97=='=') ) {s = 37;}

                        else if ( (LA33_97=='#') ) {s = 38;}

                        else if ( (LA33_97=='*'||LA33_97=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA33_69 = input.LA(1);

                        s = -1;
                        if ( (LA33_69=='i') ) {s = 84;}

                        else if ( (LA33_69=='(') ) {s = 39;}

                        else if ( ((LA33_69 >= '\u0000' && LA33_69 <= '\b')||(LA33_69 >= '\u000B' && LA33_69 <= '\f')||(LA33_69 >= '\u000E' && LA33_69 <= '\u001F')||(LA33_69 >= '$' && LA33_69 <= '&')||(LA33_69 >= '.' && LA33_69 <= '9')||LA33_69=='<'||LA33_69=='>'||(LA33_69 >= '@' && LA33_69 <= 'Z')||(LA33_69 >= '_' && LA33_69 <= 'h')||(LA33_69 >= 'j' && LA33_69 <= 'z')||LA33_69=='|'||(LA33_69 >= '\u007F' && LA33_69 <= '\u2012')||(LA33_69 >= '\u2014' && LA33_69 <= '\u2FFF')||(LA33_69 >= '\u3001' && LA33_69 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_69=='\\') ) {s = 33;}

                        else if ( (LA33_69=='+') ) {s = 34;}

                        else if ( (LA33_69=='-') ) {s = 35;}

                        else if ( (LA33_69=='\u2013') ) {s = 36;}

                        else if ( (LA33_69=='=') ) {s = 37;}

                        else if ( (LA33_69=='#') ) {s = 38;}

                        else if ( (LA33_69=='*'||LA33_69=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA33_80 = input.LA(1);

                        s = -1;
                        if ( (LA33_80=='(') ) {s = 39;}

                        else if ( ((LA33_80 >= '0' && LA33_80 <= '9')) ) {s = 92;}

                        else if ( (LA33_80=='\\') ) {s = 33;}

                        else if ( (LA33_80=='+') ) {s = 34;}

                        else if ( (LA33_80=='-') ) {s = 93;}

                        else if ( (LA33_80=='\u2013') ) {s = 94;}

                        else if ( (LA33_80=='=') ) {s = 37;}

                        else if ( (LA33_80=='#') ) {s = 38;}

                        else if ( ((LA33_80 >= '.' && LA33_80 <= '/')) ) {s = 95;}

                        else if ( ((LA33_80 >= '\u0000' && LA33_80 <= '\b')||(LA33_80 >= '\u000B' && LA33_80 <= '\f')||(LA33_80 >= '\u000E' && LA33_80 <= '\u001F')||(LA33_80 >= '$' && LA33_80 <= '&')||LA33_80=='<'||LA33_80=='>'||(LA33_80 >= '@' && LA33_80 <= 'Z')||(LA33_80 >= '_' && LA33_80 <= 'z')||LA33_80=='|'||(LA33_80 >= '\u007F' && LA33_80 <= '\u2012')||(LA33_80 >= '\u2014' && LA33_80 <= '\u2FFF')||(LA33_80 >= '\u3001' && LA33_80 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_80=='*'||LA33_80=='?') ) {s = 40;}

                        else s = 59;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA33_93 = input.LA(1);

                        s = -1;
                        if ( (LA33_93=='(') ) {s = 39;}

                        else if ( ((LA33_93 >= '0' && LA33_93 <= '9')) ) {s = 103;}

                        else if ( (LA33_93=='\\') ) {s = 33;}

                        else if ( (LA33_93=='+') ) {s = 34;}

                        else if ( (LA33_93=='-') ) {s = 35;}

                        else if ( (LA33_93=='\u2013') ) {s = 36;}

                        else if ( (LA33_93=='=') ) {s = 37;}

                        else if ( (LA33_93=='#') ) {s = 38;}

                        else if ( ((LA33_93 >= '\u0000' && LA33_93 <= '\b')||(LA33_93 >= '\u000B' && LA33_93 <= '\f')||(LA33_93 >= '\u000E' && LA33_93 <= '\u001F')||(LA33_93 >= '$' && LA33_93 <= '&')||(LA33_93 >= '.' && LA33_93 <= '/')||LA33_93=='<'||LA33_93=='>'||(LA33_93 >= '@' && LA33_93 <= 'Z')||(LA33_93 >= '_' && LA33_93 <= 'z')||LA33_93=='|'||(LA33_93 >= '\u007F' && LA33_93 <= '\u2012')||(LA33_93 >= '\u2014' && LA33_93 <= '\u2FFF')||(LA33_93 >= '\u3001' && LA33_93 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_93=='*'||LA33_93=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA33_63 = input.LA(1);

                        s = -1;
                        if ( (LA33_63=='(') ) {s = 39;}

                        else if ( ((LA33_63 >= '0' && LA33_63 <= '9')) ) {s = 81;}

                        else if ( (LA33_63=='\\') ) {s = 33;}

                        else if ( (LA33_63=='+') ) {s = 34;}

                        else if ( (LA33_63=='-') ) {s = 35;}

                        else if ( (LA33_63=='\u2013') ) {s = 36;}

                        else if ( (LA33_63=='=') ) {s = 37;}

                        else if ( (LA33_63=='#') ) {s = 38;}

                        else if ( ((LA33_63 >= '\u0000' && LA33_63 <= '\b')||(LA33_63 >= '\u000B' && LA33_63 <= '\f')||(LA33_63 >= '\u000E' && LA33_63 <= '\u001F')||(LA33_63 >= '$' && LA33_63 <= '&')||(LA33_63 >= '.' && LA33_63 <= '/')||LA33_63=='<'||LA33_63=='>'||(LA33_63 >= '@' && LA33_63 <= 'Z')||(LA33_63 >= '_' && LA33_63 <= 'z')||LA33_63=='|'||(LA33_63 >= '\u007F' && LA33_63 <= '\u2012')||(LA33_63 >= '\u2014' && LA33_63 <= '\u2FFF')||(LA33_63 >= '\u3001' && LA33_63 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_63=='*'||LA33_63=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA33_113 = input.LA(1);

                        s = -1;
                        if ( (LA33_113=='(') ) {s = 39;}

                        else if ( ((LA33_113 >= '\u0000' && LA33_113 <= '\b')||(LA33_113 >= '\u000B' && LA33_113 <= '\f')||(LA33_113 >= '\u000E' && LA33_113 <= '\u001F')||(LA33_113 >= '$' && LA33_113 <= '&')||(LA33_113 >= '.' && LA33_113 <= '9')||LA33_113=='<'||LA33_113=='>'||(LA33_113 >= '@' && LA33_113 <= 'Z')||(LA33_113 >= '_' && LA33_113 <= 'z')||LA33_113=='|'||(LA33_113 >= '\u007F' && LA33_113 <= '\u2012')||(LA33_113 >= '\u2014' && LA33_113 <= '\u2FFF')||(LA33_113 >= '\u3001' && LA33_113 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_113=='\\') ) {s = 33;}

                        else if ( (LA33_113=='+') ) {s = 34;}

                        else if ( (LA33_113=='-') ) {s = 35;}

                        else if ( (LA33_113=='\u2013') ) {s = 36;}

                        else if ( (LA33_113=='=') ) {s = 37;}

                        else if ( (LA33_113=='#') ) {s = 38;}

                        else if ( (LA33_113=='*'||LA33_113=='?') ) {s = 40;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA33_84 = input.LA(1);

                        s = -1;
                        if ( (LA33_84=='v') ) {s = 98;}

                        else if ( (LA33_84=='(') ) {s = 39;}

                        else if ( ((LA33_84 >= '\u0000' && LA33_84 <= '\b')||(LA33_84 >= '\u000B' && LA33_84 <= '\f')||(LA33_84 >= '\u000E' && LA33_84 <= '\u001F')||(LA33_84 >= '$' && LA33_84 <= '&')||(LA33_84 >= '.' && LA33_84 <= '9')||LA33_84=='<'||LA33_84=='>'||(LA33_84 >= '@' && LA33_84 <= 'Z')||(LA33_84 >= '_' && LA33_84 <= 'u')||(LA33_84 >= 'w' && LA33_84 <= 'z')||LA33_84=='|'||(LA33_84 >= '\u007F' && LA33_84 <= '\u2012')||(LA33_84 >= '\u2014' && LA33_84 <= '\u2FFF')||(LA33_84 >= '\u3001' && LA33_84 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_84=='\\') ) {s = 33;}

                        else if ( (LA33_84=='+') ) {s = 34;}

                        else if ( (LA33_84=='-') ) {s = 35;}

                        else if ( (LA33_84=='\u2013') ) {s = 36;}

                        else if ( (LA33_84=='=') ) {s = 37;}

                        else if ( (LA33_84=='#') ) {s = 38;}

                        else if ( (LA33_84=='*'||LA33_84=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA33_98 = input.LA(1);

                        s = -1;
                        if ( (LA33_98==':') ) {s = 105;}

                        else if ( (LA33_98=='(') ) {s = 39;}

                        else if ( ((LA33_98 >= '\u0000' && LA33_98 <= '\b')||(LA33_98 >= '\u000B' && LA33_98 <= '\f')||(LA33_98 >= '\u000E' && LA33_98 <= '\u001F')||(LA33_98 >= '$' && LA33_98 <= '&')||(LA33_98 >= '.' && LA33_98 <= '9')||LA33_98=='<'||LA33_98=='>'||(LA33_98 >= '@' && LA33_98 <= 'Z')||(LA33_98 >= '_' && LA33_98 <= 'z')||LA33_98=='|'||(LA33_98 >= '\u007F' && LA33_98 <= '\u2012')||(LA33_98 >= '\u2014' && LA33_98 <= '\u2FFF')||(LA33_98 >= '\u3001' && LA33_98 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_98=='\\') ) {s = 33;}

                        else if ( (LA33_98=='+') ) {s = 34;}

                        else if ( (LA33_98=='-') ) {s = 35;}

                        else if ( (LA33_98=='\u2013') ) {s = 36;}

                        else if ( (LA33_98=='=') ) {s = 37;}

                        else if ( (LA33_98=='#') ) {s = 38;}

                        else if ( (LA33_98=='*'||LA33_98=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA33_2 = input.LA(1);

                        s = -1;
                        if ( ((LA33_2 >= '\u0000' && LA33_2 <= '\b')||(LA33_2 >= '\u000B' && LA33_2 <= '\f')||(LA33_2 >= '\u000E' && LA33_2 <= '\u001F')||(LA33_2 >= '$' && LA33_2 <= '&')||(LA33_2 >= '.' && LA33_2 <= '9')||LA33_2=='<'||LA33_2=='>'||(LA33_2 >= '@' && LA33_2 <= 'Z')||(LA33_2 >= '_' && LA33_2 <= 'z')||LA33_2=='|'||(LA33_2 >= '\u007F' && LA33_2 <= '\u2012')||(LA33_2 >= '\u2014' && LA33_2 <= '\u2FFF')||(LA33_2 >= '\u3001' && LA33_2 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_2=='\\') ) {s = 33;}

                        else if ( (LA33_2=='+') ) {s = 34;}

                        else if ( (LA33_2=='-') ) {s = 35;}

                        else if ( (LA33_2=='\u2013') ) {s = 36;}

                        else if ( (LA33_2=='=') ) {s = 37;}

                        else if ( (LA33_2=='#') ) {s = 38;}

                        else if ( (LA33_2=='(') ) {s = 39;}

                        else if ( (LA33_2=='*'||LA33_2=='?') ) {s = 40;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA33_16 = input.LA(1);

                        s = -1;
                        if ( ((LA33_16 >= '\u0000' && LA33_16 <= '\u001F')||(LA33_16 >= '!' && LA33_16 <= '#')||(LA33_16 >= '%' && LA33_16 <= '*')||(LA33_16 >= '.' && LA33_16 <= '/')||(LA33_16 >= ':' && LA33_16 <= '\u2012')||(LA33_16 >= '\u2014' && LA33_16 <= '\uFFFF')) ) {s = 50;}

                        else s = 49;

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA33_111 = input.LA(1);

                        s = -1;
                        if ( ((LA33_111 >= '0' && LA33_111 <= '9')) ) {s = 113;}

                        else if ( (LA33_111=='(') ) {s = 39;}

                        else if ( ((LA33_111 >= '\u0000' && LA33_111 <= '\b')||(LA33_111 >= '\u000B' && LA33_111 <= '\f')||(LA33_111 >= '\u000E' && LA33_111 <= '\u001F')||(LA33_111 >= '$' && LA33_111 <= '&')||(LA33_111 >= '.' && LA33_111 <= '/')||LA33_111=='<'||LA33_111=='>'||(LA33_111 >= '@' && LA33_111 <= 'Z')||(LA33_111 >= '_' && LA33_111 <= 'z')||LA33_111=='|'||(LA33_111 >= '\u007F' && LA33_111 <= '\u2012')||(LA33_111 >= '\u2014' && LA33_111 <= '\u2FFF')||(LA33_111 >= '\u3001' && LA33_111 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_111=='\\') ) {s = 33;}

                        else if ( (LA33_111=='+') ) {s = 34;}

                        else if ( (LA33_111=='-') ) {s = 35;}

                        else if ( (LA33_111=='\u2013') ) {s = 36;}

                        else if ( (LA33_111=='=') ) {s = 37;}

                        else if ( (LA33_111=='#') ) {s = 38;}

                        else if ( (LA33_111=='*'||LA33_111=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA33_108 = input.LA(1);

                        s = -1;
                        if ( ((LA33_108 >= '0' && LA33_108 <= '9')) ) {s = 111;}

                        else if ( (LA33_108=='(') ) {s = 39;}

                        else if ( ((LA33_108 >= '\u0000' && LA33_108 <= '\b')||(LA33_108 >= '\u000B' && LA33_108 <= '\f')||(LA33_108 >= '\u000E' && LA33_108 <= '\u001F')||(LA33_108 >= '$' && LA33_108 <= '&')||(LA33_108 >= '.' && LA33_108 <= '/')||LA33_108=='<'||LA33_108=='>'||(LA33_108 >= '@' && LA33_108 <= 'Z')||(LA33_108 >= '_' && LA33_108 <= 'z')||LA33_108=='|'||(LA33_108 >= '\u007F' && LA33_108 <= '\u2012')||(LA33_108 >= '\u2014' && LA33_108 <= '\u2FFF')||(LA33_108 >= '\u3001' && LA33_108 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_108=='\\') ) {s = 33;}

                        else if ( (LA33_108=='+') ) {s = 34;}

                        else if ( (LA33_108=='-') ) {s = 35;}

                        else if ( (LA33_108=='\u2013') ) {s = 36;}

                        else if ( (LA33_108=='=') ) {s = 37;}

                        else if ( (LA33_108=='#') ) {s = 38;}

                        else if ( (LA33_108=='*'||LA33_108=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA33_106 = input.LA(1);

                        s = -1;
                        if ( ((LA33_106 >= '0' && LA33_106 <= '9')) ) {s = 108;}

                        else if ( (LA33_106=='(') ) {s = 39;}

                        else if ( ((LA33_106 >= '\u0000' && LA33_106 <= '\b')||(LA33_106 >= '\u000B' && LA33_106 <= '\f')||(LA33_106 >= '\u000E' && LA33_106 <= '\u001F')||(LA33_106 >= '$' && LA33_106 <= '&')||(LA33_106 >= '.' && LA33_106 <= '/')||LA33_106=='<'||LA33_106=='>'||(LA33_106 >= '@' && LA33_106 <= 'Z')||(LA33_106 >= '_' && LA33_106 <= 'z')||LA33_106=='|'||(LA33_106 >= '\u007F' && LA33_106 <= '\u2012')||(LA33_106 >= '\u2014' && LA33_106 <= '\u2FFF')||(LA33_106 >= '\u3001' && LA33_106 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_106=='\\') ) {s = 33;}

                        else if ( (LA33_106=='+') ) {s = 34;}

                        else if ( (LA33_106=='-') ) {s = 35;}

                        else if ( (LA33_106=='\u2013') ) {s = 36;}

                        else if ( (LA33_106=='=') ) {s = 37;}

                        else if ( (LA33_106=='#') ) {s = 38;}

                        else if ( (LA33_106=='*'||LA33_106=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA33_0 = input.LA(1);

                        s = -1;
                        if ( (LA33_0=='#') ) {s = 1;}

                        else if ( (LA33_0=='/') ) {s = 2;}

                        else if ( (LA33_0=='<') ) {s = 3;}

                        else if ( (LA33_0=='=') ) {s = 4;}

                        else if ( (LA33_0=='a') ) {s = 5;}

                        else if ( (LA33_0=='d') ) {s = 6;}

                        else if ( (LA33_0=='(') ) {s = 7;}

                        else if ( (LA33_0==')') ) {s = 8;}

                        else if ( (LA33_0=='[') ) {s = 9;}

                        else if ( (LA33_0==']') ) {s = 10;}

                        else if ( (LA33_0==':') ) {s = 11;}

                        else if ( (LA33_0=='+') ) {s = 12;}

                        else if ( (LA33_0=='-') ) {s = 13;}

                        else if ( (LA33_0=='*') ) {s = 14;}

                        else if ( (LA33_0=='?') ) {s = 15;}

                        else if ( (LA33_0=='^') ) {s = 16;}

                        else if ( (LA33_0=='~') ) {s = 17;}

                        else if ( (LA33_0=='\"') ) {s = 18;}

                        else if ( (LA33_0=='\'') ) {s = 19;}

                        else if ( (LA33_0==',') ) {s = 20;}

                        else if ( (LA33_0==';') ) {s = 21;}

                        else if ( (LA33_0=='T') ) {s = 22;}

                        else if ( (LA33_0=='A') ) {s = 23;}

                        else if ( (LA33_0=='O'||LA33_0=='o') ) {s = 24;}

                        else if ( (LA33_0=='N'||LA33_0=='n') ) {s = 25;}

                        else if ( (LA33_0=='\u2013') ) {s = 26;}

                        else if ( ((LA33_0 >= '0' && LA33_0 <= '9')) ) {s = 27;}

                        else if ( ((LA33_0 >= '\u0000' && LA33_0 <= '\b')||(LA33_0 >= '\u000B' && LA33_0 <= '\f')||(LA33_0 >= '\u000E' && LA33_0 <= '\u001F')||(LA33_0 >= '$' && LA33_0 <= '&')||LA33_0=='.'||LA33_0=='>'||LA33_0=='@'||(LA33_0 >= 'B' && LA33_0 <= 'M')||(LA33_0 >= 'P' && LA33_0 <= 'S')||(LA33_0 >= 'U' && LA33_0 <= 'Z')||(LA33_0 >= '_' && LA33_0 <= '`')||(LA33_0 >= 'b' && LA33_0 <= 'c')||(LA33_0 >= 'e' && LA33_0 <= 'm')||(LA33_0 >= 'p' && LA33_0 <= 'z')||LA33_0=='|'||(LA33_0 >= '\u007F' && LA33_0 <= '\u2012')||(LA33_0 >= '\u2014' && LA33_0 <= '\u2FFF')||(LA33_0 >= '\u3001' && LA33_0 <= '\uFFFF')) ) {s = 28;}

                        else if ( (LA33_0=='\\') ) {s = 29;}

                        else if ( ((LA33_0 >= '\t' && LA33_0 <= '\n')||LA33_0=='\r'||LA33_0==' '||LA33_0=='\u3000') ) {s = 30;}

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA33_41 = input.LA(1);

                        s = -1;
                        if ( (LA33_41=='>') ) {s = 67;}

                        else if ( (LA33_41=='(') ) {s = 39;}

                        else if ( ((LA33_41 >= '\u0000' && LA33_41 <= '\b')||(LA33_41 >= '\u000B' && LA33_41 <= '\f')||(LA33_41 >= '\u000E' && LA33_41 <= '\u001F')||(LA33_41 >= '$' && LA33_41 <= '&')||(LA33_41 >= '.' && LA33_41 <= '9')||LA33_41=='<'||(LA33_41 >= '@' && LA33_41 <= 'Z')||(LA33_41 >= '_' && LA33_41 <= 'z')||LA33_41=='|'||(LA33_41 >= '\u007F' && LA33_41 <= '\u2012')||(LA33_41 >= '\u2014' && LA33_41 <= '\u2FFF')||(LA33_41 >= '\u3001' && LA33_41 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_41=='\\') ) {s = 33;}

                        else if ( (LA33_41=='+') ) {s = 34;}

                        else if ( (LA33_41=='-') ) {s = 35;}

                        else if ( (LA33_41=='\u2013') ) {s = 36;}

                        else if ( (LA33_41=='=') ) {s = 37;}

                        else if ( (LA33_41=='#') ) {s = 38;}

                        else if ( (LA33_41=='*'||LA33_41=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA33_81 = input.LA(1);

                        s = -1;
                        if ( (LA33_81=='(') ) {s = 39;}

                        else if ( ((LA33_81 >= '0' && LA33_81 <= '9')) ) {s = 96;}

                        else if ( (LA33_81=='\\') ) {s = 33;}

                        else if ( (LA33_81=='+') ) {s = 34;}

                        else if ( (LA33_81=='-') ) {s = 93;}

                        else if ( (LA33_81=='\u2013') ) {s = 94;}

                        else if ( (LA33_81=='=') ) {s = 37;}

                        else if ( (LA33_81=='#') ) {s = 38;}

                        else if ( ((LA33_81 >= '.' && LA33_81 <= '/')) ) {s = 95;}

                        else if ( ((LA33_81 >= '\u0000' && LA33_81 <= '\b')||(LA33_81 >= '\u000B' && LA33_81 <= '\f')||(LA33_81 >= '\u000E' && LA33_81 <= '\u001F')||(LA33_81 >= '$' && LA33_81 <= '&')||LA33_81=='<'||LA33_81=='>'||(LA33_81 >= '@' && LA33_81 <= 'Z')||(LA33_81 >= '_' && LA33_81 <= 'z')||LA33_81=='|'||(LA33_81 >= '\u007F' && LA33_81 <= '\u2012')||(LA33_81 >= '\u2014' && LA33_81 <= '\u2FFF')||(LA33_81 >= '\u3001' && LA33_81 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_81=='*'||LA33_81=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA33_61 = input.LA(1);

                        s = -1;
                        if ( (LA33_61=='(') ) {s = 39;}

                        else if ( ((LA33_61 >= '0' && LA33_61 <= '9')) ) {s = 80;}

                        else if ( (LA33_61=='\\') ) {s = 33;}

                        else if ( (LA33_61=='+') ) {s = 34;}

                        else if ( (LA33_61=='-') ) {s = 35;}

                        else if ( (LA33_61=='\u2013') ) {s = 36;}

                        else if ( (LA33_61=='=') ) {s = 37;}

                        else if ( (LA33_61=='#') ) {s = 38;}

                        else if ( ((LA33_61 >= '\u0000' && LA33_61 <= '\b')||(LA33_61 >= '\u000B' && LA33_61 <= '\f')||(LA33_61 >= '\u000E' && LA33_61 <= '\u001F')||(LA33_61 >= '$' && LA33_61 <= '&')||(LA33_61 >= '.' && LA33_61 <= '/')||LA33_61=='<'||LA33_61=='>'||(LA33_61 >= '@' && LA33_61 <= 'Z')||(LA33_61 >= '_' && LA33_61 <= 'z')||LA33_61=='|'||(LA33_61 >= '\u007F' && LA33_61 <= '\u2012')||(LA33_61 >= '\u2014' && LA33_61 <= '\u2FFF')||(LA33_61 >= '\u3001' && LA33_61 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_61=='*'||LA33_61=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA33_89 = input.LA(1);

                        s = -1;
                        if ( (LA33_89=='(') ) {s = 39;}

                        else if ( ((LA33_89 >= '\u0000' && LA33_89 <= '\b')||(LA33_89 >= '\u000B' && LA33_89 <= '\f')||(LA33_89 >= '\u000E' && LA33_89 <= '\u001F')||(LA33_89 >= '$' && LA33_89 <= '&')||(LA33_89 >= '.' && LA33_89 <= '9')||LA33_89=='<'||LA33_89=='>'||(LA33_89 >= '@' && LA33_89 <= 'Z')||(LA33_89 >= '_' && LA33_89 <= 'z')||LA33_89=='|'||(LA33_89 >= '\u007F' && LA33_89 <= '\u2012')||(LA33_89 >= '\u2014' && LA33_89 <= '\u2FFF')||(LA33_89 >= '\u3001' && LA33_89 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_89=='\\') ) {s = 33;}

                        else if ( (LA33_89=='+') ) {s = 34;}

                        else if ( (LA33_89=='-') ) {s = 35;}

                        else if ( (LA33_89=='\u2013') ) {s = 36;}

                        else if ( (LA33_89=='=') ) {s = 37;}

                        else if ( (LA33_89=='#') ) {s = 38;}

                        else if ( (LA33_89=='*'||LA33_89=='?') ) {s = 40;}

                        else s = 99;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA33_25 = input.LA(1);

                        s = -1;
                        if ( (LA33_25=='O'||LA33_25=='o') ) {s = 57;}

                        else if ( (LA33_25=='E'||LA33_25=='e') ) {s = 58;}

                        else if ( ((LA33_25 >= '\u0000' && LA33_25 <= '\b')||(LA33_25 >= '\u000B' && LA33_25 <= '\f')||(LA33_25 >= '\u000E' && LA33_25 <= '\u001F')||(LA33_25 >= '$' && LA33_25 <= '&')||(LA33_25 >= '.' && LA33_25 <= '9')||LA33_25=='<'||LA33_25=='>'||(LA33_25 >= '@' && LA33_25 <= 'D')||(LA33_25 >= 'F' && LA33_25 <= 'N')||(LA33_25 >= 'P' && LA33_25 <= 'Z')||(LA33_25 >= '_' && LA33_25 <= 'd')||(LA33_25 >= 'f' && LA33_25 <= 'n')||(LA33_25 >= 'p' && LA33_25 <= 'z')||LA33_25=='|'||(LA33_25 >= '\u007F' && LA33_25 <= '\u2012')||(LA33_25 >= '\u2014' && LA33_25 <= '\u2FFF')||(LA33_25 >= '\u3001' && LA33_25 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_25=='\\') ) {s = 33;}

                        else if ( (LA33_25=='+') ) {s = 34;}

                        else if ( (LA33_25=='-') ) {s = 35;}

                        else if ( (LA33_25=='\u2013') ) {s = 36;}

                        else if ( (LA33_25=='=') ) {s = 37;}

                        else if ( (LA33_25=='#') ) {s = 38;}

                        else if ( (LA33_25=='(') ) {s = 39;}

                        else if ( (LA33_25=='*'||LA33_25=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA33_45 = input.LA(1);

                        s = -1;
                        if ( (LA33_45=='i') ) {s = 71;}

                        else if ( (LA33_45=='(') ) {s = 39;}

                        else if ( ((LA33_45 >= '\u0000' && LA33_45 <= '\b')||(LA33_45 >= '\u000B' && LA33_45 <= '\f')||(LA33_45 >= '\u000E' && LA33_45 <= '\u001F')||(LA33_45 >= '$' && LA33_45 <= '&')||(LA33_45 >= '.' && LA33_45 <= '9')||LA33_45=='<'||LA33_45=='>'||(LA33_45 >= '@' && LA33_45 <= 'Z')||(LA33_45 >= '_' && LA33_45 <= 'h')||(LA33_45 >= 'j' && LA33_45 <= 'z')||LA33_45=='|'||(LA33_45 >= '\u007F' && LA33_45 <= '\u2012')||(LA33_45 >= '\u2014' && LA33_45 <= '\u2FFF')||(LA33_45 >= '\u3001' && LA33_45 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_45=='\\') ) {s = 33;}

                        else if ( (LA33_45=='+') ) {s = 34;}

                        else if ( (LA33_45=='-') ) {s = 35;}

                        else if ( (LA33_45=='\u2013') ) {s = 36;}

                        else if ( (LA33_45=='=') ) {s = 37;}

                        else if ( (LA33_45=='#') ) {s = 38;}

                        else if ( (LA33_45=='*'||LA33_45=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 27 : 
                        int LA33_71 = input.LA(1);

                        s = -1;
                        if ( (LA33_71==':') ) {s = 86;}

                        else if ( (LA33_71=='(') ) {s = 39;}

                        else if ( ((LA33_71 >= '\u0000' && LA33_71 <= '\b')||(LA33_71 >= '\u000B' && LA33_71 <= '\f')||(LA33_71 >= '\u000E' && LA33_71 <= '\u001F')||(LA33_71 >= '$' && LA33_71 <= '&')||(LA33_71 >= '.' && LA33_71 <= '9')||LA33_71=='<'||LA33_71=='>'||(LA33_71 >= '@' && LA33_71 <= 'Z')||(LA33_71 >= '_' && LA33_71 <= 'z')||LA33_71=='|'||(LA33_71 >= '\u007F' && LA33_71 <= '\u2012')||(LA33_71 >= '\u2014' && LA33_71 <= '\u2FFF')||(LA33_71 >= '\u3001' && LA33_71 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_71=='\\') ) {s = 33;}

                        else if ( (LA33_71=='+') ) {s = 34;}

                        else if ( (LA33_71=='-') ) {s = 35;}

                        else if ( (LA33_71=='\u2013') ) {s = 36;}

                        else if ( (LA33_71=='=') ) {s = 37;}

                        else if ( (LA33_71=='#') ) {s = 38;}

                        else if ( (LA33_71=='*'||LA33_71=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 28 : 
                        int LA33_22 = input.LA(1);

                        s = -1;
                        if ( (LA33_22=='O') ) {s = 55;}

                        else if ( ((LA33_22 >= '\u0000' && LA33_22 <= '\b')||(LA33_22 >= '\u000B' && LA33_22 <= '\f')||(LA33_22 >= '\u000E' && LA33_22 <= '\u001F')||(LA33_22 >= '$' && LA33_22 <= '&')||(LA33_22 >= '.' && LA33_22 <= '9')||LA33_22=='<'||LA33_22=='>'||(LA33_22 >= '@' && LA33_22 <= 'N')||(LA33_22 >= 'P' && LA33_22 <= 'Z')||(LA33_22 >= '_' && LA33_22 <= 'z')||LA33_22=='|'||(LA33_22 >= '\u007F' && LA33_22 <= '\u2012')||(LA33_22 >= '\u2014' && LA33_22 <= '\u2FFF')||(LA33_22 >= '\u3001' && LA33_22 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_22=='\\') ) {s = 33;}

                        else if ( (LA33_22=='+') ) {s = 34;}

                        else if ( (LA33_22=='-') ) {s = 35;}

                        else if ( (LA33_22=='\u2013') ) {s = 36;}

                        else if ( (LA33_22=='=') ) {s = 37;}

                        else if ( (LA33_22=='#') ) {s = 38;}

                        else if ( (LA33_22=='(') ) {s = 39;}

                        else if ( (LA33_22=='*'||LA33_22=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 29 : 
                        int LA33_23 = input.LA(1);

                        s = -1;
                        if ( (LA33_23=='N'||LA33_23=='n') ) {s = 44;}

                        else if ( ((LA33_23 >= '\u0000' && LA33_23 <= '\b')||(LA33_23 >= '\u000B' && LA33_23 <= '\f')||(LA33_23 >= '\u000E' && LA33_23 <= '\u001F')||(LA33_23 >= '$' && LA33_23 <= '&')||(LA33_23 >= '.' && LA33_23 <= '9')||LA33_23=='<'||LA33_23=='>'||(LA33_23 >= '@' && LA33_23 <= 'M')||(LA33_23 >= 'O' && LA33_23 <= 'Z')||(LA33_23 >= '_' && LA33_23 <= 'm')||(LA33_23 >= 'o' && LA33_23 <= 'z')||LA33_23=='|'||(LA33_23 >= '\u007F' && LA33_23 <= '\u2012')||(LA33_23 >= '\u2014' && LA33_23 <= '\u2FFF')||(LA33_23 >= '\u3001' && LA33_23 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_23=='\\') ) {s = 33;}

                        else if ( (LA33_23=='+') ) {s = 34;}

                        else if ( (LA33_23=='-') ) {s = 35;}

                        else if ( (LA33_23=='\u2013') ) {s = 36;}

                        else if ( (LA33_23=='=') ) {s = 37;}

                        else if ( (LA33_23=='#') ) {s = 38;}

                        else if ( (LA33_23=='(') ) {s = 39;}

                        else if ( (LA33_23=='*'||LA33_23=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 30 : 
                        int LA33_65 = input.LA(1);

                        s = -1;
                        if ( ((LA33_65 >= '\u0000' && LA33_65 <= '\b')||(LA33_65 >= '\u000B' && LA33_65 <= '\f')||(LA33_65 >= '\u000E' && LA33_65 <= '\u001F')||(LA33_65 >= '$' && LA33_65 <= '&')||(LA33_65 >= '.' && LA33_65 <= '9')||LA33_65=='<'||LA33_65=='>'||(LA33_65 >= '@' && LA33_65 <= 'Z')||(LA33_65 >= '_' && LA33_65 <= 'z')||LA33_65=='|'||(LA33_65 >= '\u007F' && LA33_65 <= '\u2012')||(LA33_65 >= '\u2014' && LA33_65 <= '\u2FFF')||(LA33_65 >= '\u3001' && LA33_65 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_65=='\\') ) {s = 33;}

                        else if ( (LA33_65=='+') ) {s = 34;}

                        else if ( (LA33_65=='-') ) {s = 35;}

                        else if ( (LA33_65=='\u2013') ) {s = 36;}

                        else if ( (LA33_65=='=') ) {s = 37;}

                        else if ( (LA33_65=='#') ) {s = 38;}

                        else if ( (LA33_65=='(') ) {s = 39;}

                        else if ( (LA33_65=='*'||LA33_65=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 31 : 
                        int LA33_28 = input.LA(1);

                        s = -1;
                        if ( ((LA33_28 >= '\u0000' && LA33_28 <= '\b')||(LA33_28 >= '\u000B' && LA33_28 <= '\f')||(LA33_28 >= '\u000E' && LA33_28 <= '\u001F')||(LA33_28 >= '$' && LA33_28 <= '&')||(LA33_28 >= '.' && LA33_28 <= '9')||LA33_28=='<'||LA33_28=='>'||(LA33_28 >= '@' && LA33_28 <= 'Z')||(LA33_28 >= '_' && LA33_28 <= 'z')||LA33_28=='|'||(LA33_28 >= '\u007F' && LA33_28 <= '\u2012')||(LA33_28 >= '\u2014' && LA33_28 <= '\u2FFF')||(LA33_28 >= '\u3001' && LA33_28 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_28=='\\') ) {s = 33;}

                        else if ( (LA33_28=='+') ) {s = 34;}

                        else if ( (LA33_28=='-') ) {s = 35;}

                        else if ( (LA33_28=='\u2013') ) {s = 36;}

                        else if ( (LA33_28=='=') ) {s = 37;}

                        else if ( (LA33_28=='#') ) {s = 38;}

                        else if ( (LA33_28=='(') ) {s = 39;}

                        else if ( (LA33_28=='*'||LA33_28=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 32 : 
                        int LA33_5 = input.LA(1);

                        s = -1;
                        if ( (LA33_5=='r') ) {s = 43;}

                        else if ( (LA33_5=='N'||LA33_5=='n') ) {s = 44;}

                        else if ( ((LA33_5 >= '\u0000' && LA33_5 <= '\b')||(LA33_5 >= '\u000B' && LA33_5 <= '\f')||(LA33_5 >= '\u000E' && LA33_5 <= '\u001F')||(LA33_5 >= '$' && LA33_5 <= '&')||(LA33_5 >= '.' && LA33_5 <= '9')||LA33_5=='<'||LA33_5=='>'||(LA33_5 >= '@' && LA33_5 <= 'M')||(LA33_5 >= 'O' && LA33_5 <= 'Z')||(LA33_5 >= '_' && LA33_5 <= 'm')||(LA33_5 >= 'o' && LA33_5 <= 'q')||(LA33_5 >= 's' && LA33_5 <= 'z')||LA33_5=='|'||(LA33_5 >= '\u007F' && LA33_5 <= '\u2012')||(LA33_5 >= '\u2014' && LA33_5 <= '\u2FFF')||(LA33_5 >= '\u3001' && LA33_5 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_5=='\\') ) {s = 33;}

                        else if ( (LA33_5=='+') ) {s = 34;}

                        else if ( (LA33_5=='-') ) {s = 35;}

                        else if ( (LA33_5=='\u2013') ) {s = 36;}

                        else if ( (LA33_5=='=') ) {s = 37;}

                        else if ( (LA33_5=='#') ) {s = 38;}

                        else if ( (LA33_5=='(') ) {s = 39;}

                        else if ( (LA33_5=='*'||LA33_5=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 33 : 
                        int LA33_24 = input.LA(1);

                        s = -1;
                        if ( (LA33_24=='R'||LA33_24=='r') ) {s = 56;}

                        else if ( ((LA33_24 >= '\u0000' && LA33_24 <= '\b')||(LA33_24 >= '\u000B' && LA33_24 <= '\f')||(LA33_24 >= '\u000E' && LA33_24 <= '\u001F')||(LA33_24 >= '$' && LA33_24 <= '&')||(LA33_24 >= '.' && LA33_24 <= '9')||LA33_24=='<'||LA33_24=='>'||(LA33_24 >= '@' && LA33_24 <= 'Q')||(LA33_24 >= 'S' && LA33_24 <= 'Z')||(LA33_24 >= '_' && LA33_24 <= 'q')||(LA33_24 >= 's' && LA33_24 <= 'z')||LA33_24=='|'||(LA33_24 >= '\u007F' && LA33_24 <= '\u2012')||(LA33_24 >= '\u2014' && LA33_24 <= '\u2FFF')||(LA33_24 >= '\u3001' && LA33_24 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_24=='\\') ) {s = 33;}

                        else if ( (LA33_24=='+') ) {s = 34;}

                        else if ( (LA33_24=='-') ) {s = 35;}

                        else if ( (LA33_24=='\u2013') ) {s = 36;}

                        else if ( (LA33_24=='=') ) {s = 37;}

                        else if ( (LA33_24=='#') ) {s = 38;}

                        else if ( (LA33_24=='(') ) {s = 39;}

                        else if ( (LA33_24=='*'||LA33_24=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 34 : 
                        int LA33_56 = input.LA(1);

                        s = -1;
                        if ( (LA33_56=='(') ) {s = 39;}

                        else if ( ((LA33_56 >= '\u0000' && LA33_56 <= '\b')||(LA33_56 >= '\u000B' && LA33_56 <= '\f')||(LA33_56 >= '\u000E' && LA33_56 <= '\u001F')||(LA33_56 >= '$' && LA33_56 <= '&')||(LA33_56 >= '.' && LA33_56 <= '9')||LA33_56=='<'||LA33_56=='>'||(LA33_56 >= '@' && LA33_56 <= 'Z')||(LA33_56 >= '_' && LA33_56 <= 'z')||LA33_56=='|'||(LA33_56 >= '\u007F' && LA33_56 <= '\u2012')||(LA33_56 >= '\u2014' && LA33_56 <= '\u2FFF')||(LA33_56 >= '\u3001' && LA33_56 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_56=='\\') ) {s = 33;}

                        else if ( (LA33_56=='+') ) {s = 34;}

                        else if ( (LA33_56=='-') ) {s = 35;}

                        else if ( (LA33_56=='\u2013') ) {s = 36;}

                        else if ( (LA33_56=='=') ) {s = 37;}

                        else if ( (LA33_56=='#') ) {s = 38;}

                        else if ( (LA33_56=='*'||LA33_56=='?') ) {s = 40;}

                        else s = 75;

                        if ( s>=0 ) return s;
                        break;

                    case 35 : 
                        int LA33_91 = input.LA(1);

                        s = -1;
                        if ( (LA33_91=='(') ) {s = 39;}

                        else if ( ((LA33_91 >= '0' && LA33_91 <= '9')) ) {s = 102;}

                        else if ( (LA33_91=='\\') ) {s = 33;}

                        else if ( (LA33_91=='+') ) {s = 34;}

                        else if ( (LA33_91=='-') ) {s = 35;}

                        else if ( (LA33_91=='\u2013') ) {s = 36;}

                        else if ( (LA33_91=='=') ) {s = 37;}

                        else if ( (LA33_91=='#') ) {s = 38;}

                        else if ( ((LA33_91 >= '\u0000' && LA33_91 <= '\b')||(LA33_91 >= '\u000B' && LA33_91 <= '\f')||(LA33_91 >= '\u000E' && LA33_91 <= '\u001F')||(LA33_91 >= '$' && LA33_91 <= '&')||(LA33_91 >= '.' && LA33_91 <= '/')||LA33_91=='<'||LA33_91=='>'||(LA33_91 >= '@' && LA33_91 <= 'Z')||(LA33_91 >= '_' && LA33_91 <= 'z')||LA33_91=='|'||(LA33_91 >= '\u007F' && LA33_91 <= '\u2012')||(LA33_91 >= '\u2014' && LA33_91 <= '\u2FFF')||(LA33_91 >= '\u3001' && LA33_91 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_91=='*'||LA33_91=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 36 : 
                        int LA33_78 = input.LA(1);

                        s = -1;
                        if ( ((LA33_78 >= '0' && LA33_78 <= '9')) ) {s = 90;}

                        else if ( (LA33_78=='(') ) {s = 39;}

                        else if ( (LA33_78=='.') ) {s = 91;}

                        else if ( (LA33_78=='\\') ) {s = 33;}

                        else if ( (LA33_78=='+') ) {s = 34;}

                        else if ( (LA33_78=='-') ) {s = 35;}

                        else if ( (LA33_78=='\u2013') ) {s = 36;}

                        else if ( (LA33_78=='=') ) {s = 37;}

                        else if ( (LA33_78=='#') ) {s = 38;}

                        else if ( ((LA33_78 >= '\u0000' && LA33_78 <= '\b')||(LA33_78 >= '\u000B' && LA33_78 <= '\f')||(LA33_78 >= '\u000E' && LA33_78 <= '\u001F')||(LA33_78 >= '$' && LA33_78 <= '&')||LA33_78=='/'||LA33_78=='<'||LA33_78=='>'||(LA33_78 >= '@' && LA33_78 <= 'Z')||(LA33_78 >= '_' && LA33_78 <= 'z')||LA33_78=='|'||(LA33_78 >= '\u007F' && LA33_78 <= '\u2012')||(LA33_78 >= '\u2014' && LA33_78 <= '\u2FFF')||(LA33_78 >= '\u3001' && LA33_78 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_78=='*'||LA33_78=='?') ) {s = 40;}

                        else s = 59;

                        if ( s>=0 ) return s;
                        break;

                    case 37 : 
                        int LA33_96 = input.LA(1);

                        s = -1;
                        if ( (LA33_96=='(') ) {s = 39;}

                        else if ( ((LA33_96 >= '.' && LA33_96 <= '/')) ) {s = 95;}

                        else if ( (LA33_96=='\\') ) {s = 33;}

                        else if ( (LA33_96=='+') ) {s = 34;}

                        else if ( (LA33_96=='-') ) {s = 93;}

                        else if ( (LA33_96=='\u2013') ) {s = 94;}

                        else if ( (LA33_96=='=') ) {s = 37;}

                        else if ( (LA33_96=='#') ) {s = 38;}

                        else if ( ((LA33_96 >= '\u0000' && LA33_96 <= '\b')||(LA33_96 >= '\u000B' && LA33_96 <= '\f')||(LA33_96 >= '\u000E' && LA33_96 <= '\u001F')||(LA33_96 >= '$' && LA33_96 <= '&')||(LA33_96 >= '0' && LA33_96 <= '9')||LA33_96=='<'||LA33_96=='>'||(LA33_96 >= '@' && LA33_96 <= 'Z')||(LA33_96 >= '_' && LA33_96 <= 'z')||LA33_96=='|'||(LA33_96 >= '\u007F' && LA33_96 <= '\u2012')||(LA33_96 >= '\u2014' && LA33_96 <= '\u2FFF')||(LA33_96 >= '\u3001' && LA33_96 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_96=='*'||LA33_96=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 38 : 
                        int LA33_102 = input.LA(1);

                        s = -1;
                        if ( (LA33_102=='(') ) {s = 39;}

                        else if ( ((LA33_102 >= '0' && LA33_102 <= '9')) ) {s = 102;}

                        else if ( (LA33_102=='\\') ) {s = 33;}

                        else if ( (LA33_102=='+') ) {s = 34;}

                        else if ( (LA33_102=='-') ) {s = 35;}

                        else if ( (LA33_102=='\u2013') ) {s = 36;}

                        else if ( (LA33_102=='=') ) {s = 37;}

                        else if ( (LA33_102=='#') ) {s = 38;}

                        else if ( ((LA33_102 >= '\u0000' && LA33_102 <= '\b')||(LA33_102 >= '\u000B' && LA33_102 <= '\f')||(LA33_102 >= '\u000E' && LA33_102 <= '\u001F')||(LA33_102 >= '$' && LA33_102 <= '&')||(LA33_102 >= '.' && LA33_102 <= '/')||LA33_102=='<'||LA33_102=='>'||(LA33_102 >= '@' && LA33_102 <= 'Z')||(LA33_102 >= '_' && LA33_102 <= 'z')||LA33_102=='|'||(LA33_102 >= '\u007F' && LA33_102 <= '\u2012')||(LA33_102 >= '\u2014' && LA33_102 <= '\u2FFF')||(LA33_102 >= '\u3001' && LA33_102 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_102=='*'||LA33_102=='?') ) {s = 40;}

                        else s = 59;

                        if ( s>=0 ) return s;
                        break;

                    case 39 : 
                        int LA33_72 = input.LA(1);

                        s = -1;
                        if ( (LA33_72=='\"') ) {s = 73;}

                        else if ( (LA33_72=='\\') ) {s = 52;}

                        else if ( ((LA33_72 >= '\u0000' && LA33_72 <= '!')||(LA33_72 >= '#' && LA33_72 <= ')')||(LA33_72 >= '+' && LA33_72 <= '>')||(LA33_72 >= '@' && LA33_72 <= '[')||(LA33_72 >= ']' && LA33_72 <= '\uFFFF')) ) {s = 53;}

                        else if ( (LA33_72=='*'||LA33_72=='?') ) {s = 54;}

                        if ( s>=0 ) return s;
                        break;

                    case 40 : 
                        int LA33_76 = input.LA(1);

                        s = -1;
                        if ( (LA33_76=='(') ) {s = 39;}

                        else if ( ((LA33_76 >= '\u0000' && LA33_76 <= '\b')||(LA33_76 >= '\u000B' && LA33_76 <= '\f')||(LA33_76 >= '\u000E' && LA33_76 <= '\u001F')||(LA33_76 >= '$' && LA33_76 <= '&')||(LA33_76 >= '.' && LA33_76 <= '9')||LA33_76=='<'||LA33_76=='>'||(LA33_76 >= '@' && LA33_76 <= 'Z')||(LA33_76 >= '_' && LA33_76 <= 'z')||LA33_76=='|'||(LA33_76 >= '\u007F' && LA33_76 <= '\u2012')||(LA33_76 >= '\u2014' && LA33_76 <= '\u2FFF')||(LA33_76 >= '\u3001' && LA33_76 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_76=='\\') ) {s = 33;}

                        else if ( (LA33_76=='+') ) {s = 34;}

                        else if ( (LA33_76=='-') ) {s = 35;}

                        else if ( (LA33_76=='\u2013') ) {s = 36;}

                        else if ( (LA33_76=='=') ) {s = 37;}

                        else if ( (LA33_76=='#') ) {s = 38;}

                        else if ( (LA33_76=='*'||LA33_76=='?') ) {s = 40;}

                        else s = 88;

                        if ( s>=0 ) return s;
                        break;

                    case 41 : 
                        int LA33_55 = input.LA(1);

                        s = -1;
                        if ( (LA33_55=='(') ) {s = 39;}

                        else if ( ((LA33_55 >= '\u0000' && LA33_55 <= '\b')||(LA33_55 >= '\u000B' && LA33_55 <= '\f')||(LA33_55 >= '\u000E' && LA33_55 <= '\u001F')||(LA33_55 >= '$' && LA33_55 <= '&')||(LA33_55 >= '.' && LA33_55 <= '9')||LA33_55=='<'||LA33_55=='>'||(LA33_55 >= '@' && LA33_55 <= 'Z')||(LA33_55 >= '_' && LA33_55 <= 'z')||LA33_55=='|'||(LA33_55 >= '\u007F' && LA33_55 <= '\u2012')||(LA33_55 >= '\u2014' && LA33_55 <= '\u2FFF')||(LA33_55 >= '\u3001' && LA33_55 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_55=='\\') ) {s = 33;}

                        else if ( (LA33_55=='+') ) {s = 34;}

                        else if ( (LA33_55=='-') ) {s = 35;}

                        else if ( (LA33_55=='\u2013') ) {s = 36;}

                        else if ( (LA33_55=='=') ) {s = 37;}

                        else if ( (LA33_55=='#') ) {s = 38;}

                        else if ( (LA33_55=='*'||LA33_55=='?') ) {s = 40;}

                        else s = 74;

                        if ( s>=0 ) return s;
                        break;

                    case 42 : 
                        int LA33_70 = input.LA(1);

                        s = -1;
                        if ( (LA33_70=='(') ) {s = 39;}

                        else if ( ((LA33_70 >= '\u0000' && LA33_70 <= '\b')||(LA33_70 >= '\u000B' && LA33_70 <= '\f')||(LA33_70 >= '\u000E' && LA33_70 <= '\u001F')||(LA33_70 >= '$' && LA33_70 <= '&')||(LA33_70 >= '.' && LA33_70 <= '9')||LA33_70=='<'||LA33_70=='>'||(LA33_70 >= '@' && LA33_70 <= 'Z')||(LA33_70 >= '_' && LA33_70 <= 'z')||LA33_70=='|'||(LA33_70 >= '\u007F' && LA33_70 <= '\u2012')||(LA33_70 >= '\u2014' && LA33_70 <= '\u2FFF')||(LA33_70 >= '\u3001' && LA33_70 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_70=='\\') ) {s = 33;}

                        else if ( (LA33_70=='+') ) {s = 34;}

                        else if ( (LA33_70=='-') ) {s = 35;}

                        else if ( (LA33_70=='\u2013') ) {s = 36;}

                        else if ( (LA33_70=='=') ) {s = 37;}

                        else if ( (LA33_70=='#') ) {s = 38;}

                        else if ( (LA33_70=='*'||LA33_70=='?') ) {s = 40;}

                        else s = 85;

                        if ( s>=0 ) return s;
                        break;

                    case 43 : 
                        int LA33_58 = input.LA(1);

                        s = -1;
                        if ( (LA33_58=='A'||LA33_58=='a') ) {s = 77;}

                        else if ( (LA33_58=='(') ) {s = 39;}

                        else if ( ((LA33_58 >= '\u0000' && LA33_58 <= '\b')||(LA33_58 >= '\u000B' && LA33_58 <= '\f')||(LA33_58 >= '\u000E' && LA33_58 <= '\u001F')||(LA33_58 >= '$' && LA33_58 <= '&')||(LA33_58 >= '.' && LA33_58 <= '9')||LA33_58=='<'||LA33_58=='>'||LA33_58=='@'||(LA33_58 >= 'B' && LA33_58 <= 'Z')||(LA33_58 >= '_' && LA33_58 <= '`')||(LA33_58 >= 'b' && LA33_58 <= 'z')||LA33_58=='|'||(LA33_58 >= '\u007F' && LA33_58 <= '\u2012')||(LA33_58 >= '\u2014' && LA33_58 <= '\u2FFF')||(LA33_58 >= '\u3001' && LA33_58 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_58=='\\') ) {s = 33;}

                        else if ( (LA33_58=='+') ) {s = 34;}

                        else if ( (LA33_58=='-') ) {s = 35;}

                        else if ( (LA33_58=='\u2013') ) {s = 36;}

                        else if ( (LA33_58=='=') ) {s = 37;}

                        else if ( (LA33_58=='#') ) {s = 38;}

                        else if ( (LA33_58=='*'||LA33_58=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 44 : 
                        int LA33_92 = input.LA(1);

                        s = -1;
                        if ( (LA33_92=='(') ) {s = 39;}

                        else if ( ((LA33_92 >= '.' && LA33_92 <= '/')) ) {s = 95;}

                        else if ( (LA33_92=='\\') ) {s = 33;}

                        else if ( (LA33_92=='+') ) {s = 34;}

                        else if ( (LA33_92=='-') ) {s = 93;}

                        else if ( (LA33_92=='\u2013') ) {s = 94;}

                        else if ( (LA33_92=='=') ) {s = 37;}

                        else if ( (LA33_92=='#') ) {s = 38;}

                        else if ( ((LA33_92 >= '0' && LA33_92 <= '9')) ) {s = 102;}

                        else if ( ((LA33_92 >= '\u0000' && LA33_92 <= '\b')||(LA33_92 >= '\u000B' && LA33_92 <= '\f')||(LA33_92 >= '\u000E' && LA33_92 <= '\u001F')||(LA33_92 >= '$' && LA33_92 <= '&')||LA33_92=='<'||LA33_92=='>'||(LA33_92 >= '@' && LA33_92 <= 'Z')||(LA33_92 >= '_' && LA33_92 <= 'z')||LA33_92=='|'||(LA33_92 >= '\u007F' && LA33_92 <= '\u2012')||(LA33_92 >= '\u2014' && LA33_92 <= '\u2FFF')||(LA33_92 >= '\u3001' && LA33_92 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_92=='*'||LA33_92=='?') ) {s = 40;}

                        else s = 59;

                        if ( s>=0 ) return s;
                        break;

                    case 45 : 
                        int LA33_57 = input.LA(1);

                        s = -1;
                        if ( (LA33_57=='T'||LA33_57=='t') ) {s = 76;}

                        else if ( (LA33_57=='(') ) {s = 39;}

                        else if ( ((LA33_57 >= '\u0000' && LA33_57 <= '\b')||(LA33_57 >= '\u000B' && LA33_57 <= '\f')||(LA33_57 >= '\u000E' && LA33_57 <= '\u001F')||(LA33_57 >= '$' && LA33_57 <= '&')||(LA33_57 >= '.' && LA33_57 <= '9')||LA33_57=='<'||LA33_57=='>'||(LA33_57 >= '@' && LA33_57 <= 'S')||(LA33_57 >= 'U' && LA33_57 <= 'Z')||(LA33_57 >= '_' && LA33_57 <= 's')||(LA33_57 >= 'u' && LA33_57 <= 'z')||LA33_57=='|'||(LA33_57 >= '\u007F' && LA33_57 <= '\u2012')||(LA33_57 >= '\u2014' && LA33_57 <= '\u2FFF')||(LA33_57 >= '\u3001' && LA33_57 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_57=='\\') ) {s = 33;}

                        else if ( (LA33_57=='+') ) {s = 34;}

                        else if ( (LA33_57=='-') ) {s = 35;}

                        else if ( (LA33_57=='\u2013') ) {s = 36;}

                        else if ( (LA33_57=='=') ) {s = 37;}

                        else if ( (LA33_57=='#') ) {s = 38;}

                        else if ( (LA33_57=='*'||LA33_57=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 46 : 
                        int LA33_34 = input.LA(1);

                        s = -1;
                        if ( (LA33_34=='(') ) {s = 39;}

                        else if ( ((LA33_34 >= '\u0000' && LA33_34 <= '\b')||(LA33_34 >= '\u000B' && LA33_34 <= '\f')||(LA33_34 >= '\u000E' && LA33_34 <= '\u001F')||(LA33_34 >= '$' && LA33_34 <= '&')||(LA33_34 >= '.' && LA33_34 <= '9')||LA33_34=='<'||LA33_34=='>'||(LA33_34 >= '@' && LA33_34 <= 'Z')||(LA33_34 >= '_' && LA33_34 <= 'z')||LA33_34=='|'||(LA33_34 >= '\u007F' && LA33_34 <= '\u2012')||(LA33_34 >= '\u2014' && LA33_34 <= '\u2FFF')||(LA33_34 >= '\u3001' && LA33_34 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_34=='\\') ) {s = 33;}

                        else if ( (LA33_34=='+') ) {s = 34;}

                        else if ( (LA33_34=='-') ) {s = 35;}

                        else if ( (LA33_34=='\u2013') ) {s = 36;}

                        else if ( (LA33_34=='=') ) {s = 37;}

                        else if ( (LA33_34=='#') ) {s = 38;}

                        else if ( (LA33_34=='*'||LA33_34=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 47 : 
                        int LA33_33 = input.LA(1);

                        s = -1;
                        if ( ((LA33_33 >= '\u0000' && LA33_33 <= '\uFFFF')) ) {s = 66;}

                        if ( s>=0 ) return s;
                        break;

                    case 48 : 
                        int LA33_29 = input.LA(1);

                        s = -1;
                        if ( ((LA33_29 >= '\u0000' && LA33_29 <= '\uFFFF')) ) {s = 65;}

                        if ( s>=0 ) return s;
                        break;

                    case 49 : 
                        int LA33_43 = input.LA(1);

                        s = -1;
                        if ( (LA33_43=='X') ) {s = 68;}

                        else if ( (LA33_43=='x') ) {s = 69;}

                        else if ( (LA33_43=='(') ) {s = 39;}

                        else if ( ((LA33_43 >= '\u0000' && LA33_43 <= '\b')||(LA33_43 >= '\u000B' && LA33_43 <= '\f')||(LA33_43 >= '\u000E' && LA33_43 <= '\u001F')||(LA33_43 >= '$' && LA33_43 <= '&')||(LA33_43 >= '.' && LA33_43 <= '9')||LA33_43=='<'||LA33_43=='>'||(LA33_43 >= '@' && LA33_43 <= 'W')||(LA33_43 >= 'Y' && LA33_43 <= 'Z')||(LA33_43 >= '_' && LA33_43 <= 'w')||(LA33_43 >= 'y' && LA33_43 <= 'z')||LA33_43=='|'||(LA33_43 >= '\u007F' && LA33_43 <= '\u2012')||(LA33_43 >= '\u2014' && LA33_43 <= '\u2FFF')||(LA33_43 >= '\u3001' && LA33_43 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_43=='\\') ) {s = 33;}

                        else if ( (LA33_43=='+') ) {s = 34;}

                        else if ( (LA33_43=='-') ) {s = 35;}

                        else if ( (LA33_43=='\u2013') ) {s = 36;}

                        else if ( (LA33_43=='=') ) {s = 37;}

                        else if ( (LA33_43=='#') ) {s = 38;}

                        else if ( (LA33_43=='*'||LA33_43=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 50 : 
                        int LA33_32 = input.LA(1);

                        s = -1;
                        if ( (LA33_32=='(') ) {s = 39;}

                        else if ( ((LA33_32 >= '\u0000' && LA33_32 <= '\b')||(LA33_32 >= '\u000B' && LA33_32 <= '\f')||(LA33_32 >= '\u000E' && LA33_32 <= '\u001F')||(LA33_32 >= '$' && LA33_32 <= '&')||(LA33_32 >= '.' && LA33_32 <= '9')||LA33_32=='<'||LA33_32=='>'||(LA33_32 >= '@' && LA33_32 <= 'Z')||(LA33_32 >= '_' && LA33_32 <= 'z')||LA33_32=='|'||(LA33_32 >= '\u007F' && LA33_32 <= '\u2012')||(LA33_32 >= '\u2014' && LA33_32 <= '\u2FFF')||(LA33_32 >= '\u3001' && LA33_32 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_32=='\\') ) {s = 33;}

                        else if ( (LA33_32=='+') ) {s = 34;}

                        else if ( (LA33_32=='-') ) {s = 35;}

                        else if ( (LA33_32=='\u2013') ) {s = 36;}

                        else if ( (LA33_32=='=') ) {s = 37;}

                        else if ( (LA33_32=='#') ) {s = 38;}

                        else if ( (LA33_32=='*'||LA33_32=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 51 : 
                        int LA33_90 = input.LA(1);

                        s = -1;
                        if ( (LA33_90=='-') ) {s = 100;}

                        else if ( (LA33_90=='(') ) {s = 39;}

                        else if ( (LA33_90=='.') ) {s = 91;}

                        else if ( (LA33_90=='\\') ) {s = 33;}

                        else if ( (LA33_90=='+') ) {s = 34;}

                        else if ( (LA33_90=='\u2013') ) {s = 36;}

                        else if ( (LA33_90=='=') ) {s = 37;}

                        else if ( (LA33_90=='#') ) {s = 38;}

                        else if ( ((LA33_90 >= '0' && LA33_90 <= '9')) ) {s = 101;}

                        else if ( ((LA33_90 >= '\u0000' && LA33_90 <= '\b')||(LA33_90 >= '\u000B' && LA33_90 <= '\f')||(LA33_90 >= '\u000E' && LA33_90 <= '\u001F')||(LA33_90 >= '$' && LA33_90 <= '&')||LA33_90=='/'||LA33_90=='<'||LA33_90=='>'||(LA33_90 >= '@' && LA33_90 <= 'Z')||(LA33_90 >= '_' && LA33_90 <= 'z')||LA33_90=='|'||(LA33_90 >= '\u007F' && LA33_90 <= '\u2012')||(LA33_90 >= '\u2014' && LA33_90 <= '\u2FFF')||(LA33_90 >= '\u3001' && LA33_90 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_90=='*'||LA33_90=='?') ) {s = 40;}

                        else s = 59;

                        if ( s>=0 ) return s;
                        break;

                    case 52 : 
                        int LA33_35 = input.LA(1);

                        s = -1;
                        if ( (LA33_35=='(') ) {s = 39;}

                        else if ( ((LA33_35 >= '\u0000' && LA33_35 <= '\b')||(LA33_35 >= '\u000B' && LA33_35 <= '\f')||(LA33_35 >= '\u000E' && LA33_35 <= '\u001F')||(LA33_35 >= '$' && LA33_35 <= '&')||(LA33_35 >= '.' && LA33_35 <= '9')||LA33_35=='<'||LA33_35=='>'||(LA33_35 >= '@' && LA33_35 <= 'Z')||(LA33_35 >= '_' && LA33_35 <= 'z')||LA33_35=='|'||(LA33_35 >= '\u007F' && LA33_35 <= '\u2012')||(LA33_35 >= '\u2014' && LA33_35 <= '\u2FFF')||(LA33_35 >= '\u3001' && LA33_35 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_35=='\\') ) {s = 33;}

                        else if ( (LA33_35=='+') ) {s = 34;}

                        else if ( (LA33_35=='-') ) {s = 35;}

                        else if ( (LA33_35=='\u2013') ) {s = 36;}

                        else if ( (LA33_35=='=') ) {s = 37;}

                        else if ( (LA33_35=='#') ) {s = 38;}

                        else if ( (LA33_35=='*'||LA33_35=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 53 : 
                        int LA33_44 = input.LA(1);

                        s = -1;
                        if ( (LA33_44=='D'||LA33_44=='d') ) {s = 70;}

                        else if ( (LA33_44=='(') ) {s = 39;}

                        else if ( ((LA33_44 >= '\u0000' && LA33_44 <= '\b')||(LA33_44 >= '\u000B' && LA33_44 <= '\f')||(LA33_44 >= '\u000E' && LA33_44 <= '\u001F')||(LA33_44 >= '$' && LA33_44 <= '&')||(LA33_44 >= '.' && LA33_44 <= '9')||LA33_44=='<'||LA33_44=='>'||(LA33_44 >= '@' && LA33_44 <= 'C')||(LA33_44 >= 'E' && LA33_44 <= 'Z')||(LA33_44 >= '_' && LA33_44 <= 'c')||(LA33_44 >= 'e' && LA33_44 <= 'z')||LA33_44=='|'||(LA33_44 >= '\u007F' && LA33_44 <= '\u2012')||(LA33_44 >= '\u2014' && LA33_44 <= '\u2FFF')||(LA33_44 >= '\u3001' && LA33_44 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_44=='\\') ) {s = 33;}

                        else if ( (LA33_44=='+') ) {s = 34;}

                        else if ( (LA33_44=='-') ) {s = 35;}

                        else if ( (LA33_44=='\u2013') ) {s = 36;}

                        else if ( (LA33_44=='=') ) {s = 37;}

                        else if ( (LA33_44=='#') ) {s = 38;}

                        else if ( (LA33_44=='*'||LA33_44=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 54 : 
                        int LA33_67 = input.LA(1);

                        s = -1;
                        if ( (LA33_67=='(') ) {s = 39;}

                        else if ( ((LA33_67 >= '\u0000' && LA33_67 <= '\b')||(LA33_67 >= '\u000B' && LA33_67 <= '\f')||(LA33_67 >= '\u000E' && LA33_67 <= '\u001F')||(LA33_67 >= '$' && LA33_67 <= '&')||(LA33_67 >= '.' && LA33_67 <= '9')||LA33_67=='<'||LA33_67=='>'||(LA33_67 >= '@' && LA33_67 <= 'Z')||(LA33_67 >= '_' && LA33_67 <= 'z')||LA33_67=='|'||(LA33_67 >= '\u007F' && LA33_67 <= '\u2012')||(LA33_67 >= '\u2014' && LA33_67 <= '\u2FFF')||(LA33_67 >= '\u3001' && LA33_67 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_67=='\\') ) {s = 33;}

                        else if ( (LA33_67=='+') ) {s = 34;}

                        else if ( (LA33_67=='-') ) {s = 35;}

                        else if ( (LA33_67=='\u2013') ) {s = 36;}

                        else if ( (LA33_67=='=') ) {s = 37;}

                        else if ( (LA33_67=='#') ) {s = 38;}

                        else if ( (LA33_67=='*'||LA33_67=='?') ) {s = 40;}

                        else s = 82;

                        if ( s>=0 ) return s;
                        break;

                    case 55 : 
                        int LA33_37 = input.LA(1);

                        s = -1;
                        if ( (LA33_37=='(') ) {s = 39;}

                        else if ( ((LA33_37 >= '\u0000' && LA33_37 <= '\b')||(LA33_37 >= '\u000B' && LA33_37 <= '\f')||(LA33_37 >= '\u000E' && LA33_37 <= '\u001F')||(LA33_37 >= '$' && LA33_37 <= '&')||(LA33_37 >= '.' && LA33_37 <= '9')||LA33_37=='<'||LA33_37=='>'||(LA33_37 >= '@' && LA33_37 <= 'Z')||(LA33_37 >= '_' && LA33_37 <= 'z')||LA33_37=='|'||(LA33_37 >= '\u007F' && LA33_37 <= '\u2012')||(LA33_37 >= '\u2014' && LA33_37 <= '\u2FFF')||(LA33_37 >= '\u3001' && LA33_37 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_37=='\\') ) {s = 33;}

                        else if ( (LA33_37=='+') ) {s = 34;}

                        else if ( (LA33_37=='-') ) {s = 35;}

                        else if ( (LA33_37=='\u2013') ) {s = 36;}

                        else if ( (LA33_37=='=') ) {s = 37;}

                        else if ( (LA33_37=='#') ) {s = 38;}

                        else if ( (LA33_37=='*'||LA33_37=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 56 : 
                        int LA33_36 = input.LA(1);

                        s = -1;
                        if ( (LA33_36=='(') ) {s = 39;}

                        else if ( ((LA33_36 >= '\u0000' && LA33_36 <= '\b')||(LA33_36 >= '\u000B' && LA33_36 <= '\f')||(LA33_36 >= '\u000E' && LA33_36 <= '\u001F')||(LA33_36 >= '$' && LA33_36 <= '&')||(LA33_36 >= '.' && LA33_36 <= '9')||LA33_36=='<'||LA33_36=='>'||(LA33_36 >= '@' && LA33_36 <= 'Z')||(LA33_36 >= '_' && LA33_36 <= 'z')||LA33_36=='|'||(LA33_36 >= '\u007F' && LA33_36 <= '\u2012')||(LA33_36 >= '\u2014' && LA33_36 <= '\u2FFF')||(LA33_36 >= '\u3001' && LA33_36 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_36=='\\') ) {s = 33;}

                        else if ( (LA33_36=='+') ) {s = 34;}

                        else if ( (LA33_36=='-') ) {s = 35;}

                        else if ( (LA33_36=='\u2013') ) {s = 36;}

                        else if ( (LA33_36=='=') ) {s = 37;}

                        else if ( (LA33_36=='#') ) {s = 38;}

                        else if ( (LA33_36=='*'||LA33_36=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 57 : 
                        int LA33_38 = input.LA(1);

                        s = -1;
                        if ( (LA33_38=='(') ) {s = 39;}

                        else if ( ((LA33_38 >= '\u0000' && LA33_38 <= '\b')||(LA33_38 >= '\u000B' && LA33_38 <= '\f')||(LA33_38 >= '\u000E' && LA33_38 <= '\u001F')||(LA33_38 >= '$' && LA33_38 <= '&')||(LA33_38 >= '.' && LA33_38 <= '9')||LA33_38=='<'||LA33_38=='>'||(LA33_38 >= '@' && LA33_38 <= 'Z')||(LA33_38 >= '_' && LA33_38 <= 'z')||LA33_38=='|'||(LA33_38 >= '\u007F' && LA33_38 <= '\u2012')||(LA33_38 >= '\u2014' && LA33_38 <= '\u2FFF')||(LA33_38 >= '\u3001' && LA33_38 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_38=='\\') ) {s = 33;}

                        else if ( (LA33_38=='+') ) {s = 34;}

                        else if ( (LA33_38=='-') ) {s = 35;}

                        else if ( (LA33_38=='\u2013') ) {s = 36;}

                        else if ( (LA33_38=='=') ) {s = 37;}

                        else if ( (LA33_38=='#') ) {s = 38;}

                        else if ( (LA33_38=='*'||LA33_38=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 58 : 
                        int LA33_110 = input.LA(1);

                        s = -1;
                        if ( (LA33_110=='(') ) {s = 39;}

                        else if ( ((LA33_110 >= '0' && LA33_110 <= '9')) ) {s = 112;}

                        else if ( (LA33_110=='\\') ) {s = 33;}

                        else if ( (LA33_110=='+') ) {s = 34;}

                        else if ( (LA33_110=='-') ) {s = 35;}

                        else if ( (LA33_110=='\u2013') ) {s = 36;}

                        else if ( (LA33_110=='=') ) {s = 37;}

                        else if ( (LA33_110=='#') ) {s = 38;}

                        else if ( ((LA33_110 >= '\u0000' && LA33_110 <= '\b')||(LA33_110 >= '\u000B' && LA33_110 <= '\f')||(LA33_110 >= '\u000E' && LA33_110 <= '\u001F')||(LA33_110 >= '$' && LA33_110 <= '&')||(LA33_110 >= '.' && LA33_110 <= '/')||LA33_110=='<'||LA33_110=='>'||(LA33_110 >= '@' && LA33_110 <= 'Z')||(LA33_110 >= '_' && LA33_110 <= 'z')||LA33_110=='|'||(LA33_110 >= '\u007F' && LA33_110 <= '\u2012')||(LA33_110 >= '\u2014' && LA33_110 <= '\u2FFF')||(LA33_110 >= '\u3001' && LA33_110 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_110=='*'||LA33_110=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 59 : 
                        int LA33_103 = input.LA(1);

                        s = -1;
                        if ( (LA33_103=='(') ) {s = 39;}

                        else if ( ((LA33_103 >= '0' && LA33_103 <= '9')) ) {s = 107;}

                        else if ( (LA33_103=='\\') ) {s = 33;}

                        else if ( (LA33_103=='+') ) {s = 34;}

                        else if ( (LA33_103=='-') ) {s = 35;}

                        else if ( (LA33_103=='\u2013') ) {s = 36;}

                        else if ( (LA33_103=='=') ) {s = 37;}

                        else if ( (LA33_103=='#') ) {s = 38;}

                        else if ( ((LA33_103 >= '\u0000' && LA33_103 <= '\b')||(LA33_103 >= '\u000B' && LA33_103 <= '\f')||(LA33_103 >= '\u000E' && LA33_103 <= '\u001F')||(LA33_103 >= '$' && LA33_103 <= '&')||(LA33_103 >= '.' && LA33_103 <= '/')||LA33_103=='<'||LA33_103=='>'||(LA33_103 >= '@' && LA33_103 <= 'Z')||(LA33_103 >= '_' && LA33_103 <= 'z')||LA33_103=='|'||(LA33_103 >= '\u007F' && LA33_103 <= '\u2012')||(LA33_103 >= '\u2014' && LA33_103 <= '\u2FFF')||(LA33_103 >= '\u3001' && LA33_103 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_103=='*'||LA33_103=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 60 : 
                        int LA33_60 = input.LA(1);

                        s = -1;
                        if ( ((LA33_60 >= '0' && LA33_60 <= '9')) ) {s = 78;}

                        else if ( (LA33_60=='(') ) {s = 39;}

                        else if ( (LA33_60=='.') ) {s = 61;}

                        else if ( (LA33_60=='\\') ) {s = 33;}

                        else if ( (LA33_60=='+') ) {s = 34;}

                        else if ( (LA33_60=='-') ) {s = 62;}

                        else if ( (LA33_60=='\u2013') ) {s = 63;}

                        else if ( (LA33_60=='=') ) {s = 37;}

                        else if ( (LA33_60=='#') ) {s = 38;}

                        else if ( ((LA33_60 >= '\u0000' && LA33_60 <= '\b')||(LA33_60 >= '\u000B' && LA33_60 <= '\f')||(LA33_60 >= '\u000E' && LA33_60 <= '\u001F')||(LA33_60 >= '$' && LA33_60 <= '&')||LA33_60=='<'||LA33_60=='>'||(LA33_60 >= '@' && LA33_60 <= 'Z')||(LA33_60 >= '_' && LA33_60 <= 'z')||LA33_60=='|'||(LA33_60 >= '\u007F' && LA33_60 <= '\u2012')||(LA33_60 >= '\u2014' && LA33_60 <= '\u2FFF')||(LA33_60 >= '\u3001' && LA33_60 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_60=='/') ) {s = 64;}

                        else if ( (LA33_60==':') ) {s = 79;}

                        else if ( (LA33_60=='*'||LA33_60=='?') ) {s = 40;}

                        else s = 59;

                        if ( s>=0 ) return s;
                        break;

                    case 61 : 
                        int LA33_112 = input.LA(1);

                        s = -1;
                        if ( (LA33_112=='(') ) {s = 39;}

                        else if ( ((LA33_112 >= '\u0000' && LA33_112 <= '\b')||(LA33_112 >= '\u000B' && LA33_112 <= '\f')||(LA33_112 >= '\u000E' && LA33_112 <= '\u001F')||(LA33_112 >= '$' && LA33_112 <= '&')||(LA33_112 >= '.' && LA33_112 <= '9')||LA33_112=='<'||LA33_112=='>'||(LA33_112 >= '@' && LA33_112 <= 'Z')||(LA33_112 >= '_' && LA33_112 <= 'z')||LA33_112=='|'||(LA33_112 >= '\u007F' && LA33_112 <= '\u2012')||(LA33_112 >= '\u2014' && LA33_112 <= '\u2FFF')||(LA33_112 >= '\u3001' && LA33_112 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_112=='\\') ) {s = 33;}

                        else if ( (LA33_112=='+') ) {s = 34;}

                        else if ( (LA33_112=='-') ) {s = 35;}

                        else if ( (LA33_112=='\u2013') ) {s = 36;}

                        else if ( (LA33_112=='=') ) {s = 37;}

                        else if ( (LA33_112=='#') ) {s = 38;}

                        else if ( (LA33_112=='*'||LA33_112=='?') ) {s = 40;}

                        else s = 109;

                        if ( s>=0 ) return s;
                        break;

                    case 62 : 
                        int LA33_77 = input.LA(1);

                        s = -1;
                        if ( (LA33_77=='R'||LA33_77=='r') ) {s = 89;}

                        else if ( (LA33_77=='(') ) {s = 39;}

                        else if ( ((LA33_77 >= '\u0000' && LA33_77 <= '\b')||(LA33_77 >= '\u000B' && LA33_77 <= '\f')||(LA33_77 >= '\u000E' && LA33_77 <= '\u001F')||(LA33_77 >= '$' && LA33_77 <= '&')||(LA33_77 >= '.' && LA33_77 <= '9')||LA33_77=='<'||LA33_77=='>'||(LA33_77 >= '@' && LA33_77 <= 'Q')||(LA33_77 >= 'S' && LA33_77 <= 'Z')||(LA33_77 >= '_' && LA33_77 <= 'q')||(LA33_77 >= 's' && LA33_77 <= 'z')||LA33_77=='|'||(LA33_77 >= '\u007F' && LA33_77 <= '\u2012')||(LA33_77 >= '\u2014' && LA33_77 <= '\u2FFF')||(LA33_77 >= '\u3001' && LA33_77 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_77=='\\') ) {s = 33;}

                        else if ( (LA33_77=='+') ) {s = 34;}

                        else if ( (LA33_77=='-') ) {s = 35;}

                        else if ( (LA33_77=='\u2013') ) {s = 36;}

                        else if ( (LA33_77=='=') ) {s = 37;}

                        else if ( (LA33_77=='#') ) {s = 38;}

                        else if ( (LA33_77=='*'||LA33_77=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 63 : 
                        int LA33_52 = input.LA(1);

                        s = -1;
                        if ( ((LA33_52 >= '\u0000' && LA33_52 <= '\uFFFF')) ) {s = 72;}

                        if ( s>=0 ) return s;
                        break;

                    case 64 : 
                        int LA33_3 = input.LA(1);

                        s = -1;
                        if ( (LA33_3=='=') ) {s = 41;}

                        else if ( ((LA33_3 >= '\u0000' && LA33_3 <= '\b')||(LA33_3 >= '\u000B' && LA33_3 <= '\f')||(LA33_3 >= '\u000E' && LA33_3 <= '\u001F')||(LA33_3 >= '$' && LA33_3 <= '&')||(LA33_3 >= '.' && LA33_3 <= '9')||LA33_3=='<'||LA33_3=='>'||(LA33_3 >= '@' && LA33_3 <= 'Z')||(LA33_3 >= '_' && LA33_3 <= 'z')||LA33_3=='|'||(LA33_3 >= '\u007F' && LA33_3 <= '\u2012')||(LA33_3 >= '\u2014' && LA33_3 <= '\u2FFF')||(LA33_3 >= '\u3001' && LA33_3 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_3=='\\') ) {s = 33;}

                        else if ( (LA33_3=='+') ) {s = 34;}

                        else if ( (LA33_3=='-') ) {s = 35;}

                        else if ( (LA33_3=='\u2013') ) {s = 36;}

                        else if ( (LA33_3=='#') ) {s = 38;}

                        else if ( (LA33_3=='(') ) {s = 39;}

                        else if ( (LA33_3=='*'||LA33_3=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 65 : 
                        int LA33_27 = input.LA(1);

                        s = -1;
                        if ( ((LA33_27 >= '0' && LA33_27 <= '9')) ) {s = 60;}

                        else if ( (LA33_27=='.') ) {s = 61;}

                        else if ( (LA33_27=='\\') ) {s = 33;}

                        else if ( (LA33_27=='+') ) {s = 34;}

                        else if ( (LA33_27=='-') ) {s = 62;}

                        else if ( (LA33_27=='\u2013') ) {s = 63;}

                        else if ( (LA33_27=='=') ) {s = 37;}

                        else if ( (LA33_27=='#') ) {s = 38;}

                        else if ( (LA33_27=='(') ) {s = 39;}

                        else if ( ((LA33_27 >= '\u0000' && LA33_27 <= '\b')||(LA33_27 >= '\u000B' && LA33_27 <= '\f')||(LA33_27 >= '\u000E' && LA33_27 <= '\u001F')||(LA33_27 >= '$' && LA33_27 <= '&')||LA33_27=='<'||LA33_27=='>'||(LA33_27 >= '@' && LA33_27 <= 'Z')||(LA33_27 >= '_' && LA33_27 <= 'z')||LA33_27=='|'||(LA33_27 >= '\u007F' && LA33_27 <= '\u2012')||(LA33_27 >= '\u2014' && LA33_27 <= '\u2FFF')||(LA33_27 >= '\u3001' && LA33_27 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_27=='/') ) {s = 64;}

                        else if ( (LA33_27=='*'||LA33_27=='?') ) {s = 40;}

                        else s = 59;

                        if ( s>=0 ) return s;
                        break;

                    case 66 : 
                        int LA33_18 = input.LA(1);

                        s = -1;
                        if ( (LA33_18=='\\') ) {s = 52;}

                        else if ( ((LA33_18 >= '\u0000' && LA33_18 <= '!')||(LA33_18 >= '#' && LA33_18 <= ')')||(LA33_18 >= '+' && LA33_18 <= '>')||(LA33_18 >= '@' && LA33_18 <= '[')||(LA33_18 >= ']' && LA33_18 <= '\uFFFF')) ) {s = 53;}

                        else if ( (LA33_18=='*'||LA33_18=='?') ) {s = 54;}

                        else s = 51;

                        if ( s>=0 ) return s;
                        break;

                    case 67 : 
                        int LA33_62 = input.LA(1);

                        s = -1;
                        if ( (LA33_62=='(') ) {s = 39;}

                        else if ( ((LA33_62 >= '0' && LA33_62 <= '9')) ) {s = 81;}

                        else if ( (LA33_62=='\\') ) {s = 33;}

                        else if ( (LA33_62=='+') ) {s = 34;}

                        else if ( (LA33_62=='-') ) {s = 35;}

                        else if ( (LA33_62=='\u2013') ) {s = 36;}

                        else if ( (LA33_62=='=') ) {s = 37;}

                        else if ( (LA33_62=='#') ) {s = 38;}

                        else if ( ((LA33_62 >= '\u0000' && LA33_62 <= '\b')||(LA33_62 >= '\u000B' && LA33_62 <= '\f')||(LA33_62 >= '\u000E' && LA33_62 <= '\u001F')||(LA33_62 >= '$' && LA33_62 <= '&')||(LA33_62 >= '.' && LA33_62 <= '/')||LA33_62=='<'||LA33_62=='>'||(LA33_62 >= '@' && LA33_62 <= 'Z')||(LA33_62 >= '_' && LA33_62 <= 'z')||LA33_62=='|'||(LA33_62 >= '\u007F' && LA33_62 <= '\u2012')||(LA33_62 >= '\u2014' && LA33_62 <= '\u2FFF')||(LA33_62 >= '\u3001' && LA33_62 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_62=='*'||LA33_62=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 68 : 
                        int LA33_100 = input.LA(1);

                        s = -1;
                        if ( ((LA33_100 >= '0' && LA33_100 <= '9')) ) {s = 106;}

                        else if ( (LA33_100=='(') ) {s = 39;}

                        else if ( ((LA33_100 >= '\u0000' && LA33_100 <= '\b')||(LA33_100 >= '\u000B' && LA33_100 <= '\f')||(LA33_100 >= '\u000E' && LA33_100 <= '\u001F')||(LA33_100 >= '$' && LA33_100 <= '&')||(LA33_100 >= '.' && LA33_100 <= '/')||LA33_100=='<'||LA33_100=='>'||(LA33_100 >= '@' && LA33_100 <= 'Z')||(LA33_100 >= '_' && LA33_100 <= 'z')||LA33_100=='|'||(LA33_100 >= '\u007F' && LA33_100 <= '\u2012')||(LA33_100 >= '\u2014' && LA33_100 <= '\u2FFF')||(LA33_100 >= '\u3001' && LA33_100 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_100=='\\') ) {s = 33;}

                        else if ( (LA33_100=='+') ) {s = 34;}

                        else if ( (LA33_100=='-') ) {s = 35;}

                        else if ( (LA33_100=='\u2013') ) {s = 36;}

                        else if ( (LA33_100=='=') ) {s = 37;}

                        else if ( (LA33_100=='#') ) {s = 38;}

                        else if ( (LA33_100=='*'||LA33_100=='?') ) {s = 40;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 69 : 
                        int LA33_101 = input.LA(1);

                        s = -1;
                        if ( (LA33_101=='(') ) {s = 39;}

                        else if ( (LA33_101=='.') ) {s = 91;}

                        else if ( (LA33_101=='\\') ) {s = 33;}

                        else if ( (LA33_101=='+') ) {s = 34;}

                        else if ( (LA33_101=='-') ) {s = 35;}

                        else if ( (LA33_101=='\u2013') ) {s = 36;}

                        else if ( (LA33_101=='=') ) {s = 37;}

                        else if ( (LA33_101=='#') ) {s = 38;}

                        else if ( ((LA33_101 >= '0' && LA33_101 <= '9')) ) {s = 101;}

                        else if ( ((LA33_101 >= '\u0000' && LA33_101 <= '\b')||(LA33_101 >= '\u000B' && LA33_101 <= '\f')||(LA33_101 >= '\u000E' && LA33_101 <= '\u001F')||(LA33_101 >= '$' && LA33_101 <= '&')||LA33_101=='/'||LA33_101=='<'||LA33_101=='>'||(LA33_101 >= '@' && LA33_101 <= 'Z')||(LA33_101 >= '_' && LA33_101 <= 'z')||LA33_101=='|'||(LA33_101 >= '\u007F' && LA33_101 <= '\u2012')||(LA33_101 >= '\u2014' && LA33_101 <= '\u2FFF')||(LA33_101 >= '\u3001' && LA33_101 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_101=='*'||LA33_101=='?') ) {s = 40;}

                        else s = 59;

                        if ( s>=0 ) return s;
                        break;

                    case 70 : 
                        int LA33_15 = input.LA(1);

                        s = -1;
                        if ( (LA33_15=='?') ) {s = 15;}

                        else if ( ((LA33_15 >= '\u0000' && LA33_15 <= '\b')||(LA33_15 >= '\u000B' && LA33_15 <= '\f')||(LA33_15 >= '\u000E' && LA33_15 <= '\u001F')||(LA33_15 >= '#' && LA33_15 <= '&')||LA33_15=='+'||(LA33_15 >= '-' && LA33_15 <= '9')||(LA33_15 >= '<' && LA33_15 <= '>')||(LA33_15 >= '@' && LA33_15 <= 'Z')||LA33_15=='\\'||(LA33_15 >= '_' && LA33_15 <= 'z')||LA33_15=='|'||(LA33_15 >= '\u007F' && LA33_15 <= '\u2FFF')||(LA33_15 >= '\u3001' && LA33_15 <= '\uFFFF')) ) {s = 40;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 71 : 
                        int LA33_6 = input.LA(1);

                        s = -1;
                        if ( (LA33_6=='o') ) {s = 45;}

                        else if ( ((LA33_6 >= '\u0000' && LA33_6 <= '\b')||(LA33_6 >= '\u000B' && LA33_6 <= '\f')||(LA33_6 >= '\u000E' && LA33_6 <= '\u001F')||(LA33_6 >= '$' && LA33_6 <= '&')||(LA33_6 >= '.' && LA33_6 <= '9')||LA33_6=='<'||LA33_6=='>'||(LA33_6 >= '@' && LA33_6 <= 'Z')||(LA33_6 >= '_' && LA33_6 <= 'n')||(LA33_6 >= 'p' && LA33_6 <= 'z')||LA33_6=='|'||(LA33_6 >= '\u007F' && LA33_6 <= '\u2012')||(LA33_6 >= '\u2014' && LA33_6 <= '\u2FFF')||(LA33_6 >= '\u3001' && LA33_6 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_6=='\\') ) {s = 33;}

                        else if ( (LA33_6=='+') ) {s = 34;}

                        else if ( (LA33_6=='-') ) {s = 35;}

                        else if ( (LA33_6=='\u2013') ) {s = 36;}

                        else if ( (LA33_6=='=') ) {s = 37;}

                        else if ( (LA33_6=='#') ) {s = 38;}

                        else if ( (LA33_6=='(') ) {s = 39;}

                        else if ( (LA33_6=='*'||LA33_6=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 72 : 
                        int LA33_64 = input.LA(1);

                        s = -1;
                        if ( (LA33_64=='(') ) {s = 39;}

                        else if ( ((LA33_64 >= '0' && LA33_64 <= '9')) ) {s = 81;}

                        else if ( (LA33_64=='\\') ) {s = 33;}

                        else if ( (LA33_64=='+') ) {s = 34;}

                        else if ( (LA33_64=='-') ) {s = 35;}

                        else if ( (LA33_64=='\u2013') ) {s = 36;}

                        else if ( (LA33_64=='=') ) {s = 37;}

                        else if ( (LA33_64=='#') ) {s = 38;}

                        else if ( ((LA33_64 >= '\u0000' && LA33_64 <= '\b')||(LA33_64 >= '\u000B' && LA33_64 <= '\f')||(LA33_64 >= '\u000E' && LA33_64 <= '\u001F')||(LA33_64 >= '$' && LA33_64 <= '&')||(LA33_64 >= '.' && LA33_64 <= '/')||LA33_64=='<'||LA33_64=='>'||(LA33_64 >= '@' && LA33_64 <= 'Z')||(LA33_64 >= '_' && LA33_64 <= 'z')||LA33_64=='|'||(LA33_64 >= '\u007F' && LA33_64 <= '\u2012')||(LA33_64 >= '\u2014' && LA33_64 <= '\u2FFF')||(LA33_64 >= '\u3001' && LA33_64 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA33_64=='*'||LA33_64=='?') ) {s = 40;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 73 : 
                        int LA33_53 = input.LA(1);

                        s = -1;
                        if ( (LA33_53=='\"') ) {s = 73;}

                        else if ( (LA33_53=='\\') ) {s = 52;}

                        else if ( ((LA33_53 >= '\u0000' && LA33_53 <= '!')||(LA33_53 >= '#' && LA33_53 <= ')')||(LA33_53 >= '+' && LA33_53 <= '>')||(LA33_53 >= '@' && LA33_53 <= '[')||(LA33_53 >= ']' && LA33_53 <= '\uFFFF')) ) {s = 53;}

                        else if ( (LA33_53=='*'||LA33_53=='?') ) {s = 54;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 33, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}