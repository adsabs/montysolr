// $ANTLR 3.4 StandardLuceneGrammar.g 2011-11-02 17:18:25

   package org.apache.lucene.queryParser.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class StandardLuceneGrammarLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__57=57;
    public static final int ADDED=4;
    public static final int AMPER=5;
    public static final int AND=6;
    public static final int ATOM=7;
    public static final int BOOST=8;
    public static final int CARAT=9;
    public static final int CLAUSE=10;
    public static final int COLON=11;
    public static final int DATE_TOKEN=12;
    public static final int DQUOTE=13;
    public static final int ESC_CHAR=14;
    public static final int FIELD=15;
    public static final int FUZZY=16;
    public static final int INT=17;
    public static final int LBRACK=18;
    public static final int LCURLY=19;
    public static final int LPAREN=20;
    public static final int MINUS=21;
    public static final int MODIFIER=22;
    public static final int MULTIATOM=23;
    public static final int MULTITERM=24;
    public static final int NEAR=25;
    public static final int NORMAL_CHAR=26;
    public static final int NOT=27;
    public static final int NUCLEUS=28;
    public static final int NUMBER=29;
    public static final int OPERATOR=30;
    public static final int OR=31;
    public static final int PHRASE=32;
    public static final int PHRASE_ANYTHING=33;
    public static final int PLUS=34;
    public static final int QANYTHING=35;
    public static final int QDATE=36;
    public static final int QMARK=37;
    public static final int QNORMAL=38;
    public static final int QPHRASE=39;
    public static final int QPHRASETRUNC=40;
    public static final int QRANGEEX=41;
    public static final int QRANGEIN=42;
    public static final int QTRUNCATED=43;
    public static final int RBRACK=44;
    public static final int RCURLY=45;
    public static final int RPAREN=46;
    public static final int SQUOTE=47;
    public static final int STAR=48;
    public static final int TERM_NORMAL=49;
    public static final int TERM_TRUNCATED=50;
    public static final int TILDE=51;
    public static final int TMODIFIER=52;
    public static final int TO=53;
    public static final int VALUE=54;
    public static final int VBAR=55;
    public static final int WS=56;

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
    public String getGrammarFileName() { return "StandardLuceneGrammar.g"; }

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // StandardLuceneGrammar.g:11:7: ( '/' )
            // StandardLuceneGrammar.g:11:9: '/'
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
    // $ANTLR end "T__57"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // StandardLuceneGrammar.g:292:9: ( '(' )
            // StandardLuceneGrammar.g:292:11: '('
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
            // StandardLuceneGrammar.g:294:9: ( ')' )
            // StandardLuceneGrammar.g:294:11: ')'
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
            // StandardLuceneGrammar.g:296:9: ( '[' )
            // StandardLuceneGrammar.g:296:11: '['
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
            // StandardLuceneGrammar.g:298:9: ( ']' )
            // StandardLuceneGrammar.g:298:11: ']'
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
            // StandardLuceneGrammar.g:300:9: ( ':' )
            // StandardLuceneGrammar.g:300:11: ':'
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
            // StandardLuceneGrammar.g:302:7: ( '+' )
            // StandardLuceneGrammar.g:302:9: '+'
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
            // StandardLuceneGrammar.g:304:7: ( ( '-' | '\\!' ) )
            // StandardLuceneGrammar.g:
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
            // StandardLuceneGrammar.g:306:7: ( '*' )
            // StandardLuceneGrammar.g:306:9: '*'
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
            // StandardLuceneGrammar.g:308:8: ( ( '?' )+ )
            // StandardLuceneGrammar.g:308:10: ( '?' )+
            {
            // StandardLuceneGrammar.g:308:10: ( '?' )+
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
            	    // StandardLuceneGrammar.g:308:10: '?'
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

    // $ANTLR start "VBAR"
    public final void mVBAR() throws RecognitionException {
        try {
            // StandardLuceneGrammar.g:310:16: ( '|' )
            // StandardLuceneGrammar.g:310:18: '|'
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
            // StandardLuceneGrammar.g:312:16: ( '&' )
            // StandardLuceneGrammar.g:312:18: '&'
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
            // StandardLuceneGrammar.g:314:9: ( '{' )
            // StandardLuceneGrammar.g:314:11: '{'
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
            // StandardLuceneGrammar.g:316:9: ( '}' )
            // StandardLuceneGrammar.g:316:11: '}'
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
            // StandardLuceneGrammar.g:318:7: ( '^' ( ( INT )+ ( '.' ( INT )+ )? )? )
            // StandardLuceneGrammar.g:318:9: '^' ( ( INT )+ ( '.' ( INT )+ )? )?
            {
            match('^'); 

            // StandardLuceneGrammar.g:318:13: ( ( INT )+ ( '.' ( INT )+ )? )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( ((LA5_0 >= '0' && LA5_0 <= '9')) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // StandardLuceneGrammar.g:318:14: ( INT )+ ( '.' ( INT )+ )?
                    {
                    // StandardLuceneGrammar.g:318:14: ( INT )+
                    int cnt2=0;
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:
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
                                EarlyExitException eee =
                                    new EarlyExitException(2, input);
                                throw eee;
                        }
                        cnt2++;
                    } while (true);


                    // StandardLuceneGrammar.g:318:19: ( '.' ( INT )+ )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='.') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // StandardLuceneGrammar.g:318:20: '.' ( INT )+
                            {
                            match('.'); 

                            // StandardLuceneGrammar.g:318:24: ( INT )+
                            int cnt3=0;
                            loop3:
                            do {
                                int alt3=2;
                                int LA3_0 = input.LA(1);

                                if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
                                    alt3=1;
                                }


                                switch (alt3) {
                            	case 1 :
                            	    // StandardLuceneGrammar.g:
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
                                        EarlyExitException eee =
                                            new EarlyExitException(3, input);
                                        throw eee;
                                }
                                cnt3++;
                            } while (true);


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
            // StandardLuceneGrammar.g:320:7: ( '~' ( ( INT )+ ( '.' ( INT )+ )? )? )
            // StandardLuceneGrammar.g:320:9: '~' ( ( INT )+ ( '.' ( INT )+ )? )?
            {
            match('~'); 

            // StandardLuceneGrammar.g:320:13: ( ( INT )+ ( '.' ( INT )+ )? )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( ((LA9_0 >= '0' && LA9_0 <= '9')) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // StandardLuceneGrammar.g:320:14: ( INT )+ ( '.' ( INT )+ )?
                    {
                    // StandardLuceneGrammar.g:320:14: ( INT )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0 >= '0' && LA6_0 <= '9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:
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
                                EarlyExitException eee =
                                    new EarlyExitException(6, input);
                                throw eee;
                        }
                        cnt6++;
                    } while (true);


                    // StandardLuceneGrammar.g:320:19: ( '.' ( INT )+ )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='.') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // StandardLuceneGrammar.g:320:20: '.' ( INT )+
                            {
                            match('.'); 

                            // StandardLuceneGrammar.g:320:24: ( INT )+
                            int cnt7=0;
                            loop7:
                            do {
                                int alt7=2;
                                int LA7_0 = input.LA(1);

                                if ( ((LA7_0 >= '0' && LA7_0 <= '9')) ) {
                                    alt7=1;
                                }


                                switch (alt7) {
                            	case 1 :
                            	    // StandardLuceneGrammar.g:
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
                                        EarlyExitException eee =
                                            new EarlyExitException(7, input);
                                        throw eee;
                                }
                                cnt7++;
                            } while (true);


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
            // StandardLuceneGrammar.g:323:2: ( '\\\"' )
            // StandardLuceneGrammar.g:323:4: '\\\"'
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
            // StandardLuceneGrammar.g:326:2: ( '\\'' )
            // StandardLuceneGrammar.g:326:4: '\\''
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

    // $ANTLR start "ESC_CHAR"
    public final void mESC_CHAR() throws RecognitionException {
        try {
            // StandardLuceneGrammar.g:329:18: ( '\\\\' . )
            // StandardLuceneGrammar.g:329:21: '\\\\' .
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
            // StandardLuceneGrammar.g:331:4: ( 'TO' )
            // StandardLuceneGrammar.g:331:6: 'TO'
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
            // StandardLuceneGrammar.g:334:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) ) )
            // StandardLuceneGrammar.g:334:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
            {
            // StandardLuceneGrammar.g:334:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
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
                    // StandardLuceneGrammar.g:334:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
                    // StandardLuceneGrammar.g:334:48: ( AMPER ( AMPER )? )
                    {
                    // StandardLuceneGrammar.g:334:48: ( AMPER ( AMPER )? )
                    // StandardLuceneGrammar.g:334:49: AMPER ( AMPER )?
                    {
                    mAMPER(); 


                    // StandardLuceneGrammar.g:334:55: ( AMPER )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0=='&') ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // StandardLuceneGrammar.g:
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
            // StandardLuceneGrammar.g:335:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) ) )
            // StandardLuceneGrammar.g:335:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
            {
            // StandardLuceneGrammar.g:335:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
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
                    // StandardLuceneGrammar.g:335:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
                    // StandardLuceneGrammar.g:335:34: ( VBAR ( VBAR )? )
                    {
                    // StandardLuceneGrammar.g:335:34: ( VBAR ( VBAR )? )
                    // StandardLuceneGrammar.g:335:35: VBAR ( VBAR )?
                    {
                    mVBAR(); 


                    // StandardLuceneGrammar.g:335:40: ( VBAR )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0=='|') ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // StandardLuceneGrammar.g:
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
            // StandardLuceneGrammar.g:336:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
            // StandardLuceneGrammar.g:336:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
            // StandardLuceneGrammar.g:337:7: ( ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' ) )
            // StandardLuceneGrammar.g:337:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            {
            // StandardLuceneGrammar.g:337:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0=='n') ) {
                int LA14_1 = input.LA(2);

                if ( (LA14_1=='E'||LA14_1=='e') ) {
                    alt14=1;
                }
                else {
                    alt14=2;
                }
            }
            else if ( (LA14_0=='N') ) {
                alt14=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }
            switch (alt14) {
                case 1 :
                    // StandardLuceneGrammar.g:337:10: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:337:60: 'n'
                    {
                    match('n'); 

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
    // $ANTLR end "NEAR"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // StandardLuceneGrammar.g:340:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' ) )
            // StandardLuceneGrammar.g:340:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )
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
            // StandardLuceneGrammar.g:350:13: ( '0' .. '9' )
            // StandardLuceneGrammar.g:
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

    // $ANTLR start "NORMAL_CHAR"
    public final void mNORMAL_CHAR() throws RecognitionException {
        try {
            // StandardLuceneGrammar.g:352:23: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' ) )
            // StandardLuceneGrammar.g:353:6: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' )
            {
            if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\u001F')||(input.LA(1) >= '#' && input.LA(1) <= '%')||input.LA(1)==','||input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= ';' && input.LA(1) <= '>')||(input.LA(1) >= '@' && input.LA(1) <= 'Z')||(input.LA(1) >= '_' && input.LA(1) <= 'z')||(input.LA(1) >= '\u007F' && input.LA(1) <= '\u2FFF')||(input.LA(1) >= '\u3001' && input.LA(1) <= '\uFFFF') ) {
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
    // $ANTLR end "NORMAL_CHAR"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // StandardLuceneGrammar.g:362:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // StandardLuceneGrammar.g:363:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // StandardLuceneGrammar.g:363:2: ( INT )+
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
            	    // StandardLuceneGrammar.g:
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


            // StandardLuceneGrammar.g:363:7: ( '.' ( INT )+ )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0=='.') ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // StandardLuceneGrammar.g:363:8: '.' ( INT )+
                    {
                    match('.'); 

                    // StandardLuceneGrammar.g:363:12: ( INT )+
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
                    	    // StandardLuceneGrammar.g:
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

    // $ANTLR start "DATE_TOKEN"
    public final void mDATE_TOKEN() throws RecognitionException {
        try {
            int _type = DATE_TOKEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // StandardLuceneGrammar.g:367:2: ( INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )? )
            // StandardLuceneGrammar.g:367:4: INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )?
            {
            mINT(); 


            // StandardLuceneGrammar.g:367:8: ( INT )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( ((LA18_0 >= '0' && LA18_0 <= '9')) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // StandardLuceneGrammar.g:
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


            // StandardLuceneGrammar.g:367:31: ( INT )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( ((LA19_0 >= '0' && LA19_0 <= '9')) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // StandardLuceneGrammar.g:
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


            // StandardLuceneGrammar.g:367:58: ( INT INT )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( ((LA20_0 >= '0' && LA20_0 <= '9')) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // StandardLuceneGrammar.g:367:59: INT INT
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
            // StandardLuceneGrammar.g:371:2: ( ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )* )
            // StandardLuceneGrammar.g:372:2: ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )*
            {
            // StandardLuceneGrammar.g:372:2: ( NORMAL_CHAR | ESC_CHAR )
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( ((LA21_0 >= '\u0000' && LA21_0 <= '\b')||(LA21_0 >= '\u000B' && LA21_0 <= '\f')||(LA21_0 >= '\u000E' && LA21_0 <= '\u001F')||(LA21_0 >= '#' && LA21_0 <= '%')||LA21_0==','||LA21_0=='.'||(LA21_0 >= '0' && LA21_0 <= '9')||(LA21_0 >= ';' && LA21_0 <= '>')||(LA21_0 >= '@' && LA21_0 <= 'Z')||(LA21_0 >= '_' && LA21_0 <= 'z')||(LA21_0 >= '\u007F' && LA21_0 <= '\u2FFF')||(LA21_0 >= '\u3001' && LA21_0 <= '\uFFFF')) ) {
                alt21=1;
            }
            else if ( (LA21_0=='\\') ) {
                alt21=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }
            switch (alt21) {
                case 1 :
                    // StandardLuceneGrammar.g:372:4: NORMAL_CHAR
                    {
                    mNORMAL_CHAR(); 


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:372:18: ESC_CHAR
                    {
                    mESC_CHAR(); 


                    }
                    break;

            }


            // StandardLuceneGrammar.g:372:28: ( NORMAL_CHAR | ESC_CHAR )*
            loop22:
            do {
                int alt22=3;
                int LA22_0 = input.LA(1);

                if ( ((LA22_0 >= '\u0000' && LA22_0 <= '\b')||(LA22_0 >= '\u000B' && LA22_0 <= '\f')||(LA22_0 >= '\u000E' && LA22_0 <= '\u001F')||(LA22_0 >= '#' && LA22_0 <= '%')||LA22_0==','||LA22_0=='.'||(LA22_0 >= '0' && LA22_0 <= '9')||(LA22_0 >= ';' && LA22_0 <= '>')||(LA22_0 >= '@' && LA22_0 <= 'Z')||(LA22_0 >= '_' && LA22_0 <= 'z')||(LA22_0 >= '\u007F' && LA22_0 <= '\u2FFF')||(LA22_0 >= '\u3001' && LA22_0 <= '\uFFFF')) ) {
                    alt22=1;
                }
                else if ( (LA22_0=='\\') ) {
                    alt22=2;
                }


                switch (alt22) {
            	case 1 :
            	    // StandardLuceneGrammar.g:372:30: NORMAL_CHAR
            	    {
            	    mNORMAL_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // StandardLuceneGrammar.g:372:44: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


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
            // StandardLuceneGrammar.g:376:15: ( ( STAR | QMARK ) ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )* ( NORMAL_CHAR | ESC_CHAR )* | ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+ ( NORMAL_CHAR | ESC_CHAR )* )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0=='*'||LA32_0=='?') ) {
                alt32=1;
            }
            else if ( ((LA32_0 >= '\u0000' && LA32_0 <= '\b')||(LA32_0 >= '\u000B' && LA32_0 <= '\f')||(LA32_0 >= '\u000E' && LA32_0 <= '\u001F')||(LA32_0 >= '#' && LA32_0 <= '%')||LA32_0==','||LA32_0=='.'||(LA32_0 >= '0' && LA32_0 <= '9')||(LA32_0 >= ';' && LA32_0 <= '>')||(LA32_0 >= '@' && LA32_0 <= 'Z')||LA32_0=='\\'||(LA32_0 >= '_' && LA32_0 <= 'z')||(LA32_0 >= '\u007F' && LA32_0 <= '\u2FFF')||(LA32_0 >= '\u3001' && LA32_0 <= '\uFFFF')) ) {
                alt32=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;

            }
            switch (alt32) {
                case 1 :
                    // StandardLuceneGrammar.g:377:2: ( STAR | QMARK ) ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )* ( NORMAL_CHAR | ESC_CHAR )*
                    {
                    // StandardLuceneGrammar.g:377:2: ( STAR | QMARK )
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
                            // StandardLuceneGrammar.g:377:3: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // StandardLuceneGrammar.g:377:8: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // StandardLuceneGrammar.g:377:15: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )*
                    loop26:
                    do {
                        int alt26=2;
                        alt26 = dfa26.predict(input);
                        switch (alt26) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:377:16: ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // StandardLuceneGrammar.g:377:16: ( NORMAL_CHAR | ESC_CHAR )+
                    	    int cnt24=0;
                    	    loop24:
                    	    do {
                    	        int alt24=3;
                    	        int LA24_0 = input.LA(1);

                    	        if ( ((LA24_0 >= '\u0000' && LA24_0 <= '\b')||(LA24_0 >= '\u000B' && LA24_0 <= '\f')||(LA24_0 >= '\u000E' && LA24_0 <= '\u001F')||(LA24_0 >= '#' && LA24_0 <= '%')||LA24_0==','||LA24_0=='.'||(LA24_0 >= '0' && LA24_0 <= '9')||(LA24_0 >= ';' && LA24_0 <= '>')||(LA24_0 >= '@' && LA24_0 <= 'Z')||(LA24_0 >= '_' && LA24_0 <= 'z')||(LA24_0 >= '\u007F' && LA24_0 <= '\u2FFF')||(LA24_0 >= '\u3001' && LA24_0 <= '\uFFFF')) ) {
                    	            alt24=1;
                    	        }
                    	        else if ( (LA24_0=='\\') ) {
                    	            alt24=2;
                    	        }


                    	        switch (alt24) {
                    	    	case 1 :
                    	    	    // StandardLuceneGrammar.g:377:17: NORMAL_CHAR
                    	    	    {
                    	    	    mNORMAL_CHAR(); 


                    	    	    }
                    	    	    break;
                    	    	case 2 :
                    	    	    // StandardLuceneGrammar.g:377:29: ESC_CHAR
                    	    	    {
                    	    	    mESC_CHAR(); 


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


                    	    // StandardLuceneGrammar.g:377:40: ( QMARK | STAR )
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
                    	            // StandardLuceneGrammar.g:377:41: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // StandardLuceneGrammar.g:377:47: STAR
                    	            {
                    	            mSTAR(); 


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop26;
                        }
                    } while (true);


                    // StandardLuceneGrammar.g:377:55: ( NORMAL_CHAR | ESC_CHAR )*
                    loop27:
                    do {
                        int alt27=3;
                        int LA27_0 = input.LA(1);

                        if ( ((LA27_0 >= '\u0000' && LA27_0 <= '\b')||(LA27_0 >= '\u000B' && LA27_0 <= '\f')||(LA27_0 >= '\u000E' && LA27_0 <= '\u001F')||(LA27_0 >= '#' && LA27_0 <= '%')||LA27_0==','||LA27_0=='.'||(LA27_0 >= '0' && LA27_0 <= '9')||(LA27_0 >= ';' && LA27_0 <= '>')||(LA27_0 >= '@' && LA27_0 <= 'Z')||(LA27_0 >= '_' && LA27_0 <= 'z')||(LA27_0 >= '\u007F' && LA27_0 <= '\u2FFF')||(LA27_0 >= '\u3001' && LA27_0 <= '\uFFFF')) ) {
                            alt27=1;
                        }
                        else if ( (LA27_0=='\\') ) {
                            alt27=2;
                        }


                        switch (alt27) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:377:56: NORMAL_CHAR
                    	    {
                    	    mNORMAL_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // StandardLuceneGrammar.g:377:68: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop27;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:378:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+ ( NORMAL_CHAR | ESC_CHAR )*
                    {
                    // StandardLuceneGrammar.g:378:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+
                    int cnt30=0;
                    loop30:
                    do {
                        int alt30=2;
                        alt30 = dfa30.predict(input);
                        switch (alt30) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:378:5: ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // StandardLuceneGrammar.g:378:5: ( NORMAL_CHAR | ESC_CHAR )+
                    	    int cnt28=0;
                    	    loop28:
                    	    do {
                    	        int alt28=3;
                    	        int LA28_0 = input.LA(1);

                    	        if ( ((LA28_0 >= '\u0000' && LA28_0 <= '\b')||(LA28_0 >= '\u000B' && LA28_0 <= '\f')||(LA28_0 >= '\u000E' && LA28_0 <= '\u001F')||(LA28_0 >= '#' && LA28_0 <= '%')||LA28_0==','||LA28_0=='.'||(LA28_0 >= '0' && LA28_0 <= '9')||(LA28_0 >= ';' && LA28_0 <= '>')||(LA28_0 >= '@' && LA28_0 <= 'Z')||(LA28_0 >= '_' && LA28_0 <= 'z')||(LA28_0 >= '\u007F' && LA28_0 <= '\u2FFF')||(LA28_0 >= '\u3001' && LA28_0 <= '\uFFFF')) ) {
                    	            alt28=1;
                    	        }
                    	        else if ( (LA28_0=='\\') ) {
                    	            alt28=2;
                    	        }


                    	        switch (alt28) {
                    	    	case 1 :
                    	    	    // StandardLuceneGrammar.g:378:6: NORMAL_CHAR
                    	    	    {
                    	    	    mNORMAL_CHAR(); 


                    	    	    }
                    	    	    break;
                    	    	case 2 :
                    	    	    // StandardLuceneGrammar.g:378:18: ESC_CHAR
                    	    	    {
                    	    	    mESC_CHAR(); 


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    if ( cnt28 >= 1 ) break loop28;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(28, input);
                    	                throw eee;
                    	        }
                    	        cnt28++;
                    	    } while (true);


                    	    // StandardLuceneGrammar.g:378:29: ( QMARK | STAR )
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
                    	            // StandardLuceneGrammar.g:378:30: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // StandardLuceneGrammar.g:378:36: STAR
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


                    // StandardLuceneGrammar.g:378:44: ( NORMAL_CHAR | ESC_CHAR )*
                    loop31:
                    do {
                        int alt31=3;
                        int LA31_0 = input.LA(1);

                        if ( ((LA31_0 >= '\u0000' && LA31_0 <= '\b')||(LA31_0 >= '\u000B' && LA31_0 <= '\f')||(LA31_0 >= '\u000E' && LA31_0 <= '\u001F')||(LA31_0 >= '#' && LA31_0 <= '%')||LA31_0==','||LA31_0=='.'||(LA31_0 >= '0' && LA31_0 <= '9')||(LA31_0 >= ';' && LA31_0 <= '>')||(LA31_0 >= '@' && LA31_0 <= 'Z')||(LA31_0 >= '_' && LA31_0 <= 'z')||(LA31_0 >= '\u007F' && LA31_0 <= '\u2FFF')||(LA31_0 >= '\u3001' && LA31_0 <= '\uFFFF')) ) {
                            alt31=1;
                        }
                        else if ( (LA31_0=='\\') ) {
                            alt31=2;
                        }


                        switch (alt31) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:378:45: NORMAL_CHAR
                    	    {
                    	    mNORMAL_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // StandardLuceneGrammar.g:378:57: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop31;
                        }
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
            // StandardLuceneGrammar.g:383:2: ( DQUOTE (~ ( '\\\"' | '\\\\\"' | '?' | '*' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:384:2: DQUOTE (~ ( '\\\"' | '\\\\\"' | '?' | '*' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:384:9: (~ ( '\\\"' | '\\\\\"' | '?' | '*' ) )+
            int cnt33=0;
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( ((LA33_0 >= '\u0000' && LA33_0 <= '!')||(LA33_0 >= '#' && LA33_0 <= ')')||(LA33_0 >= '+' && LA33_0 <= '>')||(LA33_0 >= '@' && LA33_0 <= '\uFFFF')) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // StandardLuceneGrammar.g:384:9: ~ ( '\\\"' | '\\\\\"' | '?' | '*' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= ')')||(input.LA(1) >= '+' && input.LA(1) <= '>')||(input.LA(1) >= '@' && input.LA(1) <= '\uFFFF') ) {
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
                        EarlyExitException eee =
                            new EarlyExitException(33, input);
                        throw eee;
                }
                cnt33++;
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
            // StandardLuceneGrammar.g:387:17: ( DQUOTE (~ ( '\\\"' | '\\\\\"' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:388:2: DQUOTE (~ ( '\\\"' | '\\\\\"' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:388:9: (~ ( '\\\"' | '\\\\\"' ) )+
            int cnt34=0;
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( ((LA34_0 >= '\u0000' && LA34_0 <= '!')||(LA34_0 >= '#' && LA34_0 <= '\uFFFF')) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // StandardLuceneGrammar.g:388:9: ~ ( '\\\"' | '\\\\\"' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '\uFFFF') ) {
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
                        EarlyExitException eee =
                            new EarlyExitException(34, input);
                        throw eee;
                }
                cnt34++;
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
        // StandardLuceneGrammar.g:1:8: ( T__57 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | NEAR | WS | NUMBER | DATE_TOKEN | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING )
        int alt35=28;
        alt35 = dfa35.predict(input);
        switch (alt35) {
            case 1 :
                // StandardLuceneGrammar.g:1:10: T__57
                {
                mT__57(); 


                }
                break;
            case 2 :
                // StandardLuceneGrammar.g:1:16: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 3 :
                // StandardLuceneGrammar.g:1:23: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 4 :
                // StandardLuceneGrammar.g:1:30: LBRACK
                {
                mLBRACK(); 


                }
                break;
            case 5 :
                // StandardLuceneGrammar.g:1:37: RBRACK
                {
                mRBRACK(); 


                }
                break;
            case 6 :
                // StandardLuceneGrammar.g:1:44: COLON
                {
                mCOLON(); 


                }
                break;
            case 7 :
                // StandardLuceneGrammar.g:1:50: PLUS
                {
                mPLUS(); 


                }
                break;
            case 8 :
                // StandardLuceneGrammar.g:1:55: MINUS
                {
                mMINUS(); 


                }
                break;
            case 9 :
                // StandardLuceneGrammar.g:1:61: STAR
                {
                mSTAR(); 


                }
                break;
            case 10 :
                // StandardLuceneGrammar.g:1:66: QMARK
                {
                mQMARK(); 


                }
                break;
            case 11 :
                // StandardLuceneGrammar.g:1:72: LCURLY
                {
                mLCURLY(); 


                }
                break;
            case 12 :
                // StandardLuceneGrammar.g:1:79: RCURLY
                {
                mRCURLY(); 


                }
                break;
            case 13 :
                // StandardLuceneGrammar.g:1:86: CARAT
                {
                mCARAT(); 


                }
                break;
            case 14 :
                // StandardLuceneGrammar.g:1:92: TILDE
                {
                mTILDE(); 


                }
                break;
            case 15 :
                // StandardLuceneGrammar.g:1:98: DQUOTE
                {
                mDQUOTE(); 


                }
                break;
            case 16 :
                // StandardLuceneGrammar.g:1:105: SQUOTE
                {
                mSQUOTE(); 


                }
                break;
            case 17 :
                // StandardLuceneGrammar.g:1:112: TO
                {
                mTO(); 


                }
                break;
            case 18 :
                // StandardLuceneGrammar.g:1:115: AND
                {
                mAND(); 


                }
                break;
            case 19 :
                // StandardLuceneGrammar.g:1:119: OR
                {
                mOR(); 


                }
                break;
            case 20 :
                // StandardLuceneGrammar.g:1:122: NOT
                {
                mNOT(); 


                }
                break;
            case 21 :
                // StandardLuceneGrammar.g:1:126: NEAR
                {
                mNEAR(); 


                }
                break;
            case 22 :
                // StandardLuceneGrammar.g:1:131: WS
                {
                mWS(); 


                }
                break;
            case 23 :
                // StandardLuceneGrammar.g:1:134: NUMBER
                {
                mNUMBER(); 


                }
                break;
            case 24 :
                // StandardLuceneGrammar.g:1:141: DATE_TOKEN
                {
                mDATE_TOKEN(); 


                }
                break;
            case 25 :
                // StandardLuceneGrammar.g:1:152: TERM_NORMAL
                {
                mTERM_NORMAL(); 


                }
                break;
            case 26 :
                // StandardLuceneGrammar.g:1:164: TERM_TRUNCATED
                {
                mTERM_TRUNCATED(); 


                }
                break;
            case 27 :
                // StandardLuceneGrammar.g:1:179: PHRASE
                {
                mPHRASE(); 


                }
                break;
            case 28 :
                // StandardLuceneGrammar.g:1:186: PHRASE_ANYTHING
                {
                mPHRASE_ANYTHING(); 


                }
                break;

        }

    }


    protected DFA26 dfa26 = new DFA26(this);
    protected DFA30 dfa30 = new DFA30(this);
    protected DFA35 dfa35 = new DFA35(this);
    static final String DFA26_eotS =
        "\2\3\3\uffff\1\3";
    static final String DFA26_eofS =
        "\6\uffff";
    static final String DFA26_minS =
        "\3\0\2\uffff\1\0";
    static final String DFA26_maxS =
        "\3\uffff\2\uffff\1\uffff";
    static final String DFA26_acceptS =
        "\3\uffff\1\2\1\1\1\uffff";
    static final String DFA26_specialS =
        "\1\3\1\0\1\2\2\uffff\1\1}>";
    static final String[] DFA26_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\6\uffff\1\1\1\uffff"+
            "\1\1\1\uffff\12\1\1\uffff\4\1\1\uffff\33\1\1\uffff\1\2\2\uffff"+
            "\34\1\4\uffff\u2f81\1\1\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\4\uffff\1\4\1\uffff"+
            "\1\1\1\uffff\1\1\1\uffff\12\1\1\uffff\4\1\1\4\33\1\1\uffff\1"+
            "\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\ucfff\1",
            "\0\5",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\4\uffff\1\4\1\uffff"+
            "\1\1\1\uffff\1\1\1\uffff\12\1\1\uffff\4\1\1\4\33\1\1\uffff\1"+
            "\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\ucfff\1"
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
            return "()* loopback of 377:15: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA26_1 = input.LA(1);

                        s = -1;
                        if ( ((LA26_1 >= '\u0000' && LA26_1 <= '\b')||(LA26_1 >= '\u000B' && LA26_1 <= '\f')||(LA26_1 >= '\u000E' && LA26_1 <= '\u001F')||(LA26_1 >= '#' && LA26_1 <= '%')||LA26_1==','||LA26_1=='.'||(LA26_1 >= '0' && LA26_1 <= '9')||(LA26_1 >= ';' && LA26_1 <= '>')||(LA26_1 >= '@' && LA26_1 <= 'Z')||(LA26_1 >= '_' && LA26_1 <= 'z')||(LA26_1 >= '\u007F' && LA26_1 <= '\u2FFF')||(LA26_1 >= '\u3001' && LA26_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_1=='\\') ) {s = 2;}

                        else if ( (LA26_1=='*'||LA26_1=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA26_5 = input.LA(1);

                        s = -1;
                        if ( ((LA26_5 >= '\u0000' && LA26_5 <= '\b')||(LA26_5 >= '\u000B' && LA26_5 <= '\f')||(LA26_5 >= '\u000E' && LA26_5 <= '\u001F')||(LA26_5 >= '#' && LA26_5 <= '%')||LA26_5==','||LA26_5=='.'||(LA26_5 >= '0' && LA26_5 <= '9')||(LA26_5 >= ';' && LA26_5 <= '>')||(LA26_5 >= '@' && LA26_5 <= 'Z')||(LA26_5 >= '_' && LA26_5 <= 'z')||(LA26_5 >= '\u007F' && LA26_5 <= '\u2FFF')||(LA26_5 >= '\u3001' && LA26_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_5=='\\') ) {s = 2;}

                        else if ( (LA26_5=='*'||LA26_5=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA26_2 = input.LA(1);

                        s = -1;
                        if ( ((LA26_2 >= '\u0000' && LA26_2 <= '\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA26_0 = input.LA(1);

                        s = -1;
                        if ( ((LA26_0 >= '\u0000' && LA26_0 <= '\b')||(LA26_0 >= '\u000B' && LA26_0 <= '\f')||(LA26_0 >= '\u000E' && LA26_0 <= '\u001F')||(LA26_0 >= '#' && LA26_0 <= '%')||LA26_0==','||LA26_0=='.'||(LA26_0 >= '0' && LA26_0 <= '9')||(LA26_0 >= ';' && LA26_0 <= '>')||(LA26_0 >= '@' && LA26_0 <= 'Z')||(LA26_0 >= '_' && LA26_0 <= 'z')||(LA26_0 >= '\u007F' && LA26_0 <= '\u2FFF')||(LA26_0 >= '\u3001' && LA26_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA26_0=='\\') ) {s = 2;}

                        else s = 3;

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
        "\2\3\3\uffff\1\3";
    static final String DFA30_eofS =
        "\6\uffff";
    static final String DFA30_minS =
        "\3\0\2\uffff\1\0";
    static final String DFA30_maxS =
        "\3\uffff\2\uffff\1\uffff";
    static final String DFA30_acceptS =
        "\3\uffff\1\2\1\1\1\uffff";
    static final String DFA30_specialS =
        "\1\2\1\1\1\3\2\uffff\1\0}>";
    static final String[] DFA30_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\6\uffff\1\1\1\uffff"+
            "\1\1\1\uffff\12\1\1\uffff\4\1\1\uffff\33\1\1\uffff\1\2\2\uffff"+
            "\34\1\4\uffff\u2f81\1\1\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\4\uffff\1\4\1\uffff"+
            "\1\1\1\uffff\1\1\1\uffff\12\1\1\uffff\4\1\1\4\33\1\1\uffff\1"+
            "\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\ucfff\1",
            "\0\5",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\4\uffff\1\4\1\uffff"+
            "\1\1\1\uffff\1\1\1\uffff\12\1\1\uffff\4\1\1\4\33\1\1\uffff\1"+
            "\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\ucfff\1"
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
            return "()+ loopback of 378:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA30_5 = input.LA(1);

                        s = -1;
                        if ( ((LA30_5 >= '\u0000' && LA30_5 <= '\b')||(LA30_5 >= '\u000B' && LA30_5 <= '\f')||(LA30_5 >= '\u000E' && LA30_5 <= '\u001F')||(LA30_5 >= '#' && LA30_5 <= '%')||LA30_5==','||LA30_5=='.'||(LA30_5 >= '0' && LA30_5 <= '9')||(LA30_5 >= ';' && LA30_5 <= '>')||(LA30_5 >= '@' && LA30_5 <= 'Z')||(LA30_5 >= '_' && LA30_5 <= 'z')||(LA30_5 >= '\u007F' && LA30_5 <= '\u2FFF')||(LA30_5 >= '\u3001' && LA30_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_5=='\\') ) {s = 2;}

                        else if ( (LA30_5=='*'||LA30_5=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA30_1 = input.LA(1);

                        s = -1;
                        if ( ((LA30_1 >= '\u0000' && LA30_1 <= '\b')||(LA30_1 >= '\u000B' && LA30_1 <= '\f')||(LA30_1 >= '\u000E' && LA30_1 <= '\u001F')||(LA30_1 >= '#' && LA30_1 <= '%')||LA30_1==','||LA30_1=='.'||(LA30_1 >= '0' && LA30_1 <= '9')||(LA30_1 >= ';' && LA30_1 <= '>')||(LA30_1 >= '@' && LA30_1 <= 'Z')||(LA30_1 >= '_' && LA30_1 <= 'z')||(LA30_1 >= '\u007F' && LA30_1 <= '\u2FFF')||(LA30_1 >= '\u3001' && LA30_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_1=='\\') ) {s = 2;}

                        else if ( (LA30_1=='*'||LA30_1=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA30_0 = input.LA(1);

                        s = -1;
                        if ( ((LA30_0 >= '\u0000' && LA30_0 <= '\b')||(LA30_0 >= '\u000B' && LA30_0 <= '\f')||(LA30_0 >= '\u000E' && LA30_0 <= '\u001F')||(LA30_0 >= '#' && LA30_0 <= '%')||LA30_0==','||LA30_0=='.'||(LA30_0 >= '0' && LA30_0 <= '9')||(LA30_0 >= ';' && LA30_0 <= '>')||(LA30_0 >= '@' && LA30_0 <= 'Z')||(LA30_0 >= '_' && LA30_0 <= 'z')||(LA30_0 >= '\u007F' && LA30_0 <= '\u2FFF')||(LA30_0 >= '\u3001' && LA30_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA30_0=='\\') ) {s = 2;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA30_2 = input.LA(1);

                        s = -1;
                        if ( ((LA30_2 >= '\u0000' && LA30_2 <= '\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 30, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA35_eotS =
        "\11\uffff\1\34\1\36\4\uffff\1\37\1\uffff\2\43\1\uffff\1\43\1\uffff"+
        "\1\52\1\43\1\uffff\1\54\1\43\7\uffff\1\61\1\uffff\1\43\1\uffff\1"+
        "\43\1\25\2\43\1\uffff\1\43\1\uffff\1\54\1\uffff\1\43\2\uffff\1\43"+
        "\1\23\1\71\1\43\2\54\2\uffff\1\52\1\54\2\43\1\54\1\43\1\56\1\43"+
        "\1\56";
    static final String DFA35_eofS =
        "\103\uffff";
    static final String DFA35_minS =
        "\1\0\10\uffff\2\0\4\uffff\1\0\1\uffff\2\0\1\uffff\1\0\1\uffff\2"+
        "\0\1\uffff\3\0\4\uffff\1\0\1\uffff\1\0\1\uffff\6\0\1\uffff\1\0\1"+
        "\uffff\1\0\1\uffff\1\0\2\uffff\6\0\2\uffff\11\0";
    static final String DFA35_maxS =
        "\1\uffff\10\uffff\2\uffff\4\uffff\1\uffff\1\uffff\2\uffff\1\uffff"+
        "\1\uffff\1\uffff\2\uffff\1\uffff\3\uffff\4\uffff\1\uffff\1\uffff"+
        "\1\uffff\1\uffff\6\uffff\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff"+
        "\1\uffff\2\uffff\6\uffff\2\uffff\11\uffff";
    static final String DFA35_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff\1\13\1\14\1\15"+
        "\1\16\1\uffff\1\20\2\uffff\1\22\1\uffff\1\23\2\uffff\1\26\3\uffff"+
        "\1\11\1\32\1\12\1\17\1\uffff\1\34\1\uffff\1\31\6\uffff\1\25\1\uffff"+
        "\1\27\1\uffff\1\30\1\uffff\1\33\1\21\6\uffff\1\33\1\24\11\uffff";
    static final String DFA35_specialS =
        "\1\4\10\uffff\1\11\1\16\4\uffff\1\34\1\uffff\1\15\1\10\1\uffff\1"+
        "\5\1\uffff\1\42\1\2\1\uffff\1\44\1\27\1\37\4\uffff\1\6\1\uffff\1"+
        "\30\1\uffff\1\22\1\40\1\0\1\25\1\14\1\33\1\uffff\1\3\1\uffff\1\1"+
        "\1\uffff\1\12\2\uffff\1\26\1\35\1\7\1\32\1\23\1\36\2\uffff\1\41"+
        "\1\31\1\43\1\17\1\20\1\45\1\13\1\24\1\21}>";
    static final String[] DFA35_transitionS = {
            "\11\32\2\30\2\32\1\30\22\32\1\30\1\10\1\17\3\32\1\23\1\20\1"+
            "\2\1\3\1\11\1\7\1\32\1\10\1\32\1\1\12\31\1\6\4\32\1\12\1\32"+
            "\1\22\14\32\1\27\1\24\4\32\1\21\6\32\1\4\1\33\1\5\1\15\2\32"+
            "\1\22\14\32\1\26\1\24\13\32\1\13\1\25\1\14\1\16\u2f81\32\1\30"+
            "\ucfff\32",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\3\35\6\uffff\1\35"+
            "\1\uffff\1\35\1\uffff\12\35\1\uffff\4\35\1\uffff\33\35\1\uffff"+
            "\1\35\2\uffff\34\35\4\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\3\35\6\uffff\1\35"+
            "\1\uffff\1\35\1\uffff\12\35\1\uffff\4\35\1\12\33\35\1\uffff"+
            "\1\35\2\uffff\34\35\4\uffff\u2f81\35\1\uffff\ucfff\35",
            "",
            "",
            "",
            "",
            "\42\40\1\uffff\7\40\1\41\24\40\1\41\uffc0\40",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\17"+
            "\44\1\42\13\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1"+
            "\uffff\ucfff\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\16"+
            "\44\1\46\14\44\1\uffff\1\45\2\uffff\17\44\1\46\14\44\4\uffff"+
            "\u2f81\44\1\uffff\ucfff\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\22"+
            "\44\1\47\10\44\1\uffff\1\45\2\uffff\23\44\1\47\10\44\4\uffff"+
            "\u2f81\44\1\uffff\ucfff\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\5"+
            "\44\1\51\11\44\1\50\13\44\1\uffff\1\45\2\uffff\6\44\1\51\11"+
            "\44\1\50\13\44\4\uffff\u2f81\44\1\uffff\ucfff\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\5"+
            "\44\1\51\11\44\1\50\13\44\1\uffff\1\45\2\uffff\6\44\1\51\11"+
            "\44\1\50\13\44\4\uffff\u2f81\44\1\uffff\ucfff\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\56\1\53\1\56\12\55\1\uffff\4\44\1\35\33\44\1"+
            "\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\0\57",
            "",
            "",
            "",
            "",
            "\42\40\1\60\7\40\1\41\24\40\1\41\uffc0\40",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\0\62",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\4"+
            "\44\1\63\26\44\1\uffff\1\45\2\uffff\5\44\1\63\26\44\4\uffff"+
            "\u2f81\44\1\uffff\ucfff\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\24"+
            "\44\1\64\6\44\1\uffff\1\45\2\uffff\25\44\1\64\6\44\4\uffff\u2f81"+
            "\44\1\uffff\ucfff\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\1"+
            "\44\1\65\31\44\1\uffff\1\45\2\uffff\2\44\1\65\31\44\4\uffff"+
            "\u2f81\44\1\uffff\ucfff\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\66\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\56\1\53\1\56\12\67\1\uffff\4\44\1\35\33\44\1"+
            "\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\22"+
            "\44\1\72\10\44\1\uffff\1\45\2\uffff\23\44\1\72\10\44\4\uffff"+
            "\u2f81\44\1\uffff\ucfff\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\56\1\74\1\56\12\73\1\uffff\4\44\1\35\33\44\1"+
            "\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\75\1\uffff\12\67\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\56\1\74\1\56\12\76\1\uffff\4\44\1\35\33\44\1"+
            "\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\77\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\76\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\76\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\100\1\uffff\4\44\1\35"+
            "\33\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\101\1\uffff\4\44\1\35"+
            "\33\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\102\1\uffff\4\44\1\35"+
            "\33\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\u2f81\44\1\uffff\ucfff"+
            "\44"
    };

    static final short[] DFA35_eot = DFA.unpackEncodedString(DFA35_eotS);
    static final short[] DFA35_eof = DFA.unpackEncodedString(DFA35_eofS);
    static final char[] DFA35_min = DFA.unpackEncodedStringToUnsignedChars(DFA35_minS);
    static final char[] DFA35_max = DFA.unpackEncodedStringToUnsignedChars(DFA35_maxS);
    static final short[] DFA35_accept = DFA.unpackEncodedString(DFA35_acceptS);
    static final short[] DFA35_special = DFA.unpackEncodedString(DFA35_specialS);
    static final short[][] DFA35_transition;

    static {
        int numStates = DFA35_transitionS.length;
        DFA35_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA35_transition[i] = DFA.unpackEncodedString(DFA35_transitionS[i]);
        }
    }

    class DFA35 extends DFA {

        public DFA35(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 35;
            this.eot = DFA35_eot;
            this.eof = DFA35_eof;
            this.min = DFA35_min;
            this.max = DFA35_max;
            this.accept = DFA35_accept;
            this.special = DFA35_special;
            this.transition = DFA35_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__57 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | NEAR | WS | NUMBER | DATE_TOKEN | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA35_38 = input.LA(1);

                        s = -1;
                        if ( (LA35_38=='D'||LA35_38=='d') ) {s = 51;}

                        else if ( ((LA35_38 >= '\u0000' && LA35_38 <= '\b')||(LA35_38 >= '\u000B' && LA35_38 <= '\f')||(LA35_38 >= '\u000E' && LA35_38 <= '\u001F')||(LA35_38 >= '#' && LA35_38 <= '%')||LA35_38==','||LA35_38=='.'||(LA35_38 >= '0' && LA35_38 <= '9')||(LA35_38 >= ';' && LA35_38 <= '>')||(LA35_38 >= '@' && LA35_38 <= 'C')||(LA35_38 >= 'E' && LA35_38 <= 'Z')||(LA35_38 >= '_' && LA35_38 <= 'c')||(LA35_38 >= 'e' && LA35_38 <= 'z')||(LA35_38 >= '\u007F' && LA35_38 <= '\u2FFF')||(LA35_38 >= '\u3001' && LA35_38 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_38=='\\') ) {s = 37;}

                        else if ( (LA35_38=='*'||LA35_38=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA35_45 = input.LA(1);

                        s = -1;
                        if ( (LA35_45=='.') ) {s = 43;}

                        else if ( ((LA35_45 >= '0' && LA35_45 <= '9')) ) {s = 55;}

                        else if ( (LA35_45=='-'||LA35_45=='/') ) {s = 46;}

                        else if ( ((LA35_45 >= '\u0000' && LA35_45 <= '\b')||(LA35_45 >= '\u000B' && LA35_45 <= '\f')||(LA35_45 >= '\u000E' && LA35_45 <= '\u001F')||(LA35_45 >= '#' && LA35_45 <= '%')||LA35_45==','||(LA35_45 >= ';' && LA35_45 <= '>')||(LA35_45 >= '@' && LA35_45 <= 'Z')||(LA35_45 >= '_' && LA35_45 <= 'z')||(LA35_45 >= '\u007F' && LA35_45 <= '\u2FFF')||(LA35_45 >= '\u3001' && LA35_45 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_45=='\\') ) {s = 37;}

                        else if ( (LA35_45=='*'||LA35_45=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA35_23 = input.LA(1);

                        s = -1;
                        if ( (LA35_23=='O'||LA35_23=='o') ) {s = 40;}

                        else if ( (LA35_23=='E'||LA35_23=='e') ) {s = 41;}

                        else if ( ((LA35_23 >= '\u0000' && LA35_23 <= '\b')||(LA35_23 >= '\u000B' && LA35_23 <= '\f')||(LA35_23 >= '\u000E' && LA35_23 <= '\u001F')||(LA35_23 >= '#' && LA35_23 <= '%')||LA35_23==','||LA35_23=='.'||(LA35_23 >= '0' && LA35_23 <= '9')||(LA35_23 >= ';' && LA35_23 <= '>')||(LA35_23 >= '@' && LA35_23 <= 'D')||(LA35_23 >= 'F' && LA35_23 <= 'N')||(LA35_23 >= 'P' && LA35_23 <= 'Z')||(LA35_23 >= '_' && LA35_23 <= 'd')||(LA35_23 >= 'f' && LA35_23 <= 'n')||(LA35_23 >= 'p' && LA35_23 <= 'z')||(LA35_23 >= '\u007F' && LA35_23 <= '\u2FFF')||(LA35_23 >= '\u3001' && LA35_23 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_23=='\\') ) {s = 37;}

                        else if ( (LA35_23=='*'||LA35_23=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA35_43 = input.LA(1);

                        s = -1;
                        if ( ((LA35_43 >= '0' && LA35_43 <= '9')) ) {s = 54;}

                        else if ( ((LA35_43 >= '\u0000' && LA35_43 <= '\b')||(LA35_43 >= '\u000B' && LA35_43 <= '\f')||(LA35_43 >= '\u000E' && LA35_43 <= '\u001F')||(LA35_43 >= '#' && LA35_43 <= '%')||LA35_43==','||LA35_43=='.'||(LA35_43 >= ';' && LA35_43 <= '>')||(LA35_43 >= '@' && LA35_43 <= 'Z')||(LA35_43 >= '_' && LA35_43 <= 'z')||(LA35_43 >= '\u007F' && LA35_43 <= '\u2FFF')||(LA35_43 >= '\u3001' && LA35_43 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_43=='\\') ) {s = 37;}

                        else if ( (LA35_43=='*'||LA35_43=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA35_0 = input.LA(1);

                        s = -1;
                        if ( (LA35_0=='/') ) {s = 1;}

                        else if ( (LA35_0=='(') ) {s = 2;}

                        else if ( (LA35_0==')') ) {s = 3;}

                        else if ( (LA35_0=='[') ) {s = 4;}

                        else if ( (LA35_0==']') ) {s = 5;}

                        else if ( (LA35_0==':') ) {s = 6;}

                        else if ( (LA35_0=='+') ) {s = 7;}

                        else if ( (LA35_0=='!'||LA35_0=='-') ) {s = 8;}

                        else if ( (LA35_0=='*') ) {s = 9;}

                        else if ( (LA35_0=='?') ) {s = 10;}

                        else if ( (LA35_0=='{') ) {s = 11;}

                        else if ( (LA35_0=='}') ) {s = 12;}

                        else if ( (LA35_0=='^') ) {s = 13;}

                        else if ( (LA35_0=='~') ) {s = 14;}

                        else if ( (LA35_0=='\"') ) {s = 15;}

                        else if ( (LA35_0=='\'') ) {s = 16;}

                        else if ( (LA35_0=='T') ) {s = 17;}

                        else if ( (LA35_0=='A'||LA35_0=='a') ) {s = 18;}

                        else if ( (LA35_0=='&') ) {s = 19;}

                        else if ( (LA35_0=='O'||LA35_0=='o') ) {s = 20;}

                        else if ( (LA35_0=='|') ) {s = 21;}

                        else if ( (LA35_0=='n') ) {s = 22;}

                        else if ( (LA35_0=='N') ) {s = 23;}

                        else if ( ((LA35_0 >= '\t' && LA35_0 <= '\n')||LA35_0=='\r'||LA35_0==' '||LA35_0=='\u3000') ) {s = 24;}

                        else if ( ((LA35_0 >= '0' && LA35_0 <= '9')) ) {s = 25;}

                        else if ( ((LA35_0 >= '\u0000' && LA35_0 <= '\b')||(LA35_0 >= '\u000B' && LA35_0 <= '\f')||(LA35_0 >= '\u000E' && LA35_0 <= '\u001F')||(LA35_0 >= '#' && LA35_0 <= '%')||LA35_0==','||LA35_0=='.'||(LA35_0 >= ';' && LA35_0 <= '>')||LA35_0=='@'||(LA35_0 >= 'B' && LA35_0 <= 'M')||(LA35_0 >= 'P' && LA35_0 <= 'S')||(LA35_0 >= 'U' && LA35_0 <= 'Z')||(LA35_0 >= '_' && LA35_0 <= '`')||(LA35_0 >= 'b' && LA35_0 <= 'm')||(LA35_0 >= 'p' && LA35_0 <= 'z')||(LA35_0 >= '\u007F' && LA35_0 <= '\u2FFF')||(LA35_0 >= '\u3001' && LA35_0 <= '\uFFFF')) ) {s = 26;}

                        else if ( (LA35_0=='\\') ) {s = 27;}

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA35_20 = input.LA(1);

                        s = -1;
                        if ( (LA35_20=='R'||LA35_20=='r') ) {s = 39;}

                        else if ( ((LA35_20 >= '\u0000' && LA35_20 <= '\b')||(LA35_20 >= '\u000B' && LA35_20 <= '\f')||(LA35_20 >= '\u000E' && LA35_20 <= '\u001F')||(LA35_20 >= '#' && LA35_20 <= '%')||LA35_20==','||LA35_20=='.'||(LA35_20 >= '0' && LA35_20 <= '9')||(LA35_20 >= ';' && LA35_20 <= '>')||(LA35_20 >= '@' && LA35_20 <= 'Q')||(LA35_20 >= 'S' && LA35_20 <= 'Z')||(LA35_20 >= '_' && LA35_20 <= 'q')||(LA35_20 >= 's' && LA35_20 <= 'z')||(LA35_20 >= '\u007F' && LA35_20 <= '\u2FFF')||(LA35_20 >= '\u3001' && LA35_20 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_20=='\\') ) {s = 37;}

                        else if ( (LA35_20=='*'||LA35_20=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA35_32 = input.LA(1);

                        s = -1;
                        if ( (LA35_32=='\"') ) {s = 48;}

                        else if ( ((LA35_32 >= '\u0000' && LA35_32 <= '!')||(LA35_32 >= '#' && LA35_32 <= ')')||(LA35_32 >= '+' && LA35_32 <= '>')||(LA35_32 >= '@' && LA35_32 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA35_32=='*'||LA35_32=='?') ) {s = 33;}

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA35_52 = input.LA(1);

                        s = -1;
                        if ( ((LA35_52 >= '\u0000' && LA35_52 <= '\b')||(LA35_52 >= '\u000B' && LA35_52 <= '\f')||(LA35_52 >= '\u000E' && LA35_52 <= '\u001F')||(LA35_52 >= '#' && LA35_52 <= '%')||LA35_52==','||LA35_52=='.'||(LA35_52 >= '0' && LA35_52 <= '9')||(LA35_52 >= ';' && LA35_52 <= '>')||(LA35_52 >= '@' && LA35_52 <= 'Z')||(LA35_52 >= '_' && LA35_52 <= 'z')||(LA35_52 >= '\u007F' && LA35_52 <= '\u2FFF')||(LA35_52 >= '\u3001' && LA35_52 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_52=='\\') ) {s = 37;}

                        else if ( (LA35_52=='*'||LA35_52=='?') ) {s = 29;}

                        else s = 57;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA35_18 = input.LA(1);

                        s = -1;
                        if ( (LA35_18=='N'||LA35_18=='n') ) {s = 38;}

                        else if ( ((LA35_18 >= '\u0000' && LA35_18 <= '\b')||(LA35_18 >= '\u000B' && LA35_18 <= '\f')||(LA35_18 >= '\u000E' && LA35_18 <= '\u001F')||(LA35_18 >= '#' && LA35_18 <= '%')||LA35_18==','||LA35_18=='.'||(LA35_18 >= '0' && LA35_18 <= '9')||(LA35_18 >= ';' && LA35_18 <= '>')||(LA35_18 >= '@' && LA35_18 <= 'M')||(LA35_18 >= 'O' && LA35_18 <= 'Z')||(LA35_18 >= '_' && LA35_18 <= 'm')||(LA35_18 >= 'o' && LA35_18 <= 'z')||(LA35_18 >= '\u007F' && LA35_18 <= '\u2FFF')||(LA35_18 >= '\u3001' && LA35_18 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_18=='\\') ) {s = 37;}

                        else if ( (LA35_18=='*'||LA35_18=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA35_9 = input.LA(1);

                        s = -1;
                        if ( ((LA35_9 >= '\u0000' && LA35_9 <= '\b')||(LA35_9 >= '\u000B' && LA35_9 <= '\f')||(LA35_9 >= '\u000E' && LA35_9 <= '\u001F')||(LA35_9 >= '#' && LA35_9 <= '%')||LA35_9==','||LA35_9=='.'||(LA35_9 >= '0' && LA35_9 <= '9')||(LA35_9 >= ';' && LA35_9 <= '>')||(LA35_9 >= '@' && LA35_9 <= 'Z')||LA35_9=='\\'||(LA35_9 >= '_' && LA35_9 <= 'z')||(LA35_9 >= '\u007F' && LA35_9 <= '\u2FFF')||(LA35_9 >= '\u3001' && LA35_9 <= '\uFFFF')) ) {s = 29;}

                        else s = 28;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA35_47 = input.LA(1);

                        s = -1;
                        if ( ((LA35_47 >= '\u0000' && LA35_47 <= '\b')||(LA35_47 >= '\u000B' && LA35_47 <= '\f')||(LA35_47 >= '\u000E' && LA35_47 <= '\u001F')||(LA35_47 >= '#' && LA35_47 <= '%')||LA35_47==','||LA35_47=='.'||(LA35_47 >= '0' && LA35_47 <= '9')||(LA35_47 >= ';' && LA35_47 <= '>')||(LA35_47 >= '@' && LA35_47 <= 'Z')||(LA35_47 >= '_' && LA35_47 <= 'z')||(LA35_47 >= '\u007F' && LA35_47 <= '\u2FFF')||(LA35_47 >= '\u3001' && LA35_47 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_47=='\\') ) {s = 37;}

                        else if ( (LA35_47=='*'||LA35_47=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA35_64 = input.LA(1);

                        s = -1;
                        if ( ((LA35_64 >= '0' && LA35_64 <= '9')) ) {s = 65;}

                        else if ( ((LA35_64 >= '\u0000' && LA35_64 <= '\b')||(LA35_64 >= '\u000B' && LA35_64 <= '\f')||(LA35_64 >= '\u000E' && LA35_64 <= '\u001F')||(LA35_64 >= '#' && LA35_64 <= '%')||LA35_64==','||LA35_64=='.'||(LA35_64 >= ';' && LA35_64 <= '>')||(LA35_64 >= '@' && LA35_64 <= 'Z')||(LA35_64 >= '_' && LA35_64 <= 'z')||(LA35_64 >= '\u007F' && LA35_64 <= '\u2FFF')||(LA35_64 >= '\u3001' && LA35_64 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_64=='\\') ) {s = 37;}

                        else if ( (LA35_64=='*'||LA35_64=='?') ) {s = 29;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA35_40 = input.LA(1);

                        s = -1;
                        if ( (LA35_40=='T'||LA35_40=='t') ) {s = 52;}

                        else if ( ((LA35_40 >= '\u0000' && LA35_40 <= '\b')||(LA35_40 >= '\u000B' && LA35_40 <= '\f')||(LA35_40 >= '\u000E' && LA35_40 <= '\u001F')||(LA35_40 >= '#' && LA35_40 <= '%')||LA35_40==','||LA35_40=='.'||(LA35_40 >= '0' && LA35_40 <= '9')||(LA35_40 >= ';' && LA35_40 <= '>')||(LA35_40 >= '@' && LA35_40 <= 'S')||(LA35_40 >= 'U' && LA35_40 <= 'Z')||(LA35_40 >= '_' && LA35_40 <= 's')||(LA35_40 >= 'u' && LA35_40 <= 'z')||(LA35_40 >= '\u007F' && LA35_40 <= '\u2FFF')||(LA35_40 >= '\u3001' && LA35_40 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_40=='\\') ) {s = 37;}

                        else if ( (LA35_40=='*'||LA35_40=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA35_17 = input.LA(1);

                        s = -1;
                        if ( (LA35_17=='O') ) {s = 34;}

                        else if ( ((LA35_17 >= '\u0000' && LA35_17 <= '\b')||(LA35_17 >= '\u000B' && LA35_17 <= '\f')||(LA35_17 >= '\u000E' && LA35_17 <= '\u001F')||(LA35_17 >= '#' && LA35_17 <= '%')||LA35_17==','||LA35_17=='.'||(LA35_17 >= '0' && LA35_17 <= '9')||(LA35_17 >= ';' && LA35_17 <= '>')||(LA35_17 >= '@' && LA35_17 <= 'N')||(LA35_17 >= 'P' && LA35_17 <= 'Z')||(LA35_17 >= '_' && LA35_17 <= 'z')||(LA35_17 >= '\u007F' && LA35_17 <= '\u2FFF')||(LA35_17 >= '\u3001' && LA35_17 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_17=='\\') ) {s = 37;}

                        else if ( (LA35_17=='*'||LA35_17=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA35_10 = input.LA(1);

                        s = -1;
                        if ( (LA35_10=='?') ) {s = 10;}

                        else if ( ((LA35_10 >= '\u0000' && LA35_10 <= '\b')||(LA35_10 >= '\u000B' && LA35_10 <= '\f')||(LA35_10 >= '\u000E' && LA35_10 <= '\u001F')||(LA35_10 >= '#' && LA35_10 <= '%')||LA35_10==','||LA35_10=='.'||(LA35_10 >= '0' && LA35_10 <= '9')||(LA35_10 >= ';' && LA35_10 <= '>')||(LA35_10 >= '@' && LA35_10 <= 'Z')||LA35_10=='\\'||(LA35_10 >= '_' && LA35_10 <= 'z')||(LA35_10 >= '\u007F' && LA35_10 <= '\u2FFF')||(LA35_10 >= '\u3001' && LA35_10 <= '\uFFFF')) ) {s = 29;}

                        else s = 30;

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA35_61 = input.LA(1);

                        s = -1;
                        if ( ((LA35_61 >= '0' && LA35_61 <= '9')) ) {s = 62;}

                        else if ( ((LA35_61 >= '\u0000' && LA35_61 <= '\b')||(LA35_61 >= '\u000B' && LA35_61 <= '\f')||(LA35_61 >= '\u000E' && LA35_61 <= '\u001F')||(LA35_61 >= '#' && LA35_61 <= '%')||LA35_61==','||LA35_61=='.'||(LA35_61 >= ';' && LA35_61 <= '>')||(LA35_61 >= '@' && LA35_61 <= 'Z')||(LA35_61 >= '_' && LA35_61 <= 'z')||(LA35_61 >= '\u007F' && LA35_61 <= '\u2FFF')||(LA35_61 >= '\u3001' && LA35_61 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_61=='\\') ) {s = 37;}

                        else if ( (LA35_61=='*'||LA35_61=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA35_62 = input.LA(1);

                        s = -1;
                        if ( ((LA35_62 >= '0' && LA35_62 <= '9')) ) {s = 62;}

                        else if ( ((LA35_62 >= '\u0000' && LA35_62 <= '\b')||(LA35_62 >= '\u000B' && LA35_62 <= '\f')||(LA35_62 >= '\u000E' && LA35_62 <= '\u001F')||(LA35_62 >= '#' && LA35_62 <= '%')||LA35_62==','||LA35_62=='.'||(LA35_62 >= ';' && LA35_62 <= '>')||(LA35_62 >= '@' && LA35_62 <= 'Z')||(LA35_62 >= '_' && LA35_62 <= 'z')||(LA35_62 >= '\u007F' && LA35_62 <= '\u2FFF')||(LA35_62 >= '\u3001' && LA35_62 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_62=='\\') ) {s = 37;}

                        else if ( (LA35_62=='*'||LA35_62=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA35_66 = input.LA(1);

                        s = -1;
                        if ( ((LA35_66 >= '\u0000' && LA35_66 <= '\b')||(LA35_66 >= '\u000B' && LA35_66 <= '\f')||(LA35_66 >= '\u000E' && LA35_66 <= '\u001F')||(LA35_66 >= '#' && LA35_66 <= '%')||LA35_66==','||LA35_66=='.'||(LA35_66 >= '0' && LA35_66 <= '9')||(LA35_66 >= ';' && LA35_66 <= '>')||(LA35_66 >= '@' && LA35_66 <= 'Z')||(LA35_66 >= '_' && LA35_66 <= 'z')||(LA35_66 >= '\u007F' && LA35_66 <= '\u2FFF')||(LA35_66 >= '\u3001' && LA35_66 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_66=='\\') ) {s = 37;}

                        else if ( (LA35_66=='*'||LA35_66=='?') ) {s = 29;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA35_36 = input.LA(1);

                        s = -1;
                        if ( ((LA35_36 >= '\u0000' && LA35_36 <= '\b')||(LA35_36 >= '\u000B' && LA35_36 <= '\f')||(LA35_36 >= '\u000E' && LA35_36 <= '\u001F')||(LA35_36 >= '#' && LA35_36 <= '%')||LA35_36==','||LA35_36=='.'||(LA35_36 >= '0' && LA35_36 <= '9')||(LA35_36 >= ';' && LA35_36 <= '>')||(LA35_36 >= '@' && LA35_36 <= 'Z')||(LA35_36 >= '_' && LA35_36 <= 'z')||(LA35_36 >= '\u007F' && LA35_36 <= '\u2FFF')||(LA35_36 >= '\u3001' && LA35_36 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_36=='\\') ) {s = 37;}

                        else if ( (LA35_36=='*'||LA35_36=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA35_54 = input.LA(1);

                        s = -1;
                        if ( ((LA35_54 >= '0' && LA35_54 <= '9')) ) {s = 59;}

                        else if ( (LA35_54=='.') ) {s = 60;}

                        else if ( (LA35_54=='-'||LA35_54=='/') ) {s = 46;}

                        else if ( ((LA35_54 >= '\u0000' && LA35_54 <= '\b')||(LA35_54 >= '\u000B' && LA35_54 <= '\f')||(LA35_54 >= '\u000E' && LA35_54 <= '\u001F')||(LA35_54 >= '#' && LA35_54 <= '%')||LA35_54==','||(LA35_54 >= ';' && LA35_54 <= '>')||(LA35_54 >= '@' && LA35_54 <= 'Z')||(LA35_54 >= '_' && LA35_54 <= 'z')||(LA35_54 >= '\u007F' && LA35_54 <= '\u2FFF')||(LA35_54 >= '\u3001' && LA35_54 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_54=='\\') ) {s = 37;}

                        else if ( (LA35_54=='*'||LA35_54=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA35_65 = input.LA(1);

                        s = -1;
                        if ( ((LA35_65 >= '0' && LA35_65 <= '9')) ) {s = 66;}

                        else if ( ((LA35_65 >= '\u0000' && LA35_65 <= '\b')||(LA35_65 >= '\u000B' && LA35_65 <= '\f')||(LA35_65 >= '\u000E' && LA35_65 <= '\u001F')||(LA35_65 >= '#' && LA35_65 <= '%')||LA35_65==','||LA35_65=='.'||(LA35_65 >= ';' && LA35_65 <= '>')||(LA35_65 >= '@' && LA35_65 <= 'Z')||(LA35_65 >= '_' && LA35_65 <= 'z')||(LA35_65 >= '\u007F' && LA35_65 <= '\u2FFF')||(LA35_65 >= '\u3001' && LA35_65 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_65=='\\') ) {s = 37;}

                        else if ( (LA35_65=='*'||LA35_65=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA35_39 = input.LA(1);

                        s = -1;
                        if ( ((LA35_39 >= '\u0000' && LA35_39 <= '\b')||(LA35_39 >= '\u000B' && LA35_39 <= '\f')||(LA35_39 >= '\u000E' && LA35_39 <= '\u001F')||(LA35_39 >= '#' && LA35_39 <= '%')||LA35_39==','||LA35_39=='.'||(LA35_39 >= '0' && LA35_39 <= '9')||(LA35_39 >= ';' && LA35_39 <= '>')||(LA35_39 >= '@' && LA35_39 <= 'Z')||(LA35_39 >= '_' && LA35_39 <= 'z')||(LA35_39 >= '\u007F' && LA35_39 <= '\u2FFF')||(LA35_39 >= '\u3001' && LA35_39 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_39=='\\') ) {s = 37;}

                        else if ( (LA35_39=='*'||LA35_39=='?') ) {s = 29;}

                        else s = 21;

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA35_50 = input.LA(1);

                        s = -1;
                        if ( ((LA35_50 >= '\u0000' && LA35_50 <= '\b')||(LA35_50 >= '\u000B' && LA35_50 <= '\f')||(LA35_50 >= '\u000E' && LA35_50 <= '\u001F')||(LA35_50 >= '#' && LA35_50 <= '%')||LA35_50==','||LA35_50=='.'||(LA35_50 >= '0' && LA35_50 <= '9')||(LA35_50 >= ';' && LA35_50 <= '>')||(LA35_50 >= '@' && LA35_50 <= 'Z')||(LA35_50 >= '_' && LA35_50 <= 'z')||(LA35_50 >= '\u007F' && LA35_50 <= '\u2FFF')||(LA35_50 >= '\u3001' && LA35_50 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_50=='\\') ) {s = 37;}

                        else if ( (LA35_50=='*'||LA35_50=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA35_26 = input.LA(1);

                        s = -1;
                        if ( ((LA35_26 >= '\u0000' && LA35_26 <= '\b')||(LA35_26 >= '\u000B' && LA35_26 <= '\f')||(LA35_26 >= '\u000E' && LA35_26 <= '\u001F')||(LA35_26 >= '#' && LA35_26 <= '%')||LA35_26==','||LA35_26=='.'||(LA35_26 >= '0' && LA35_26 <= '9')||(LA35_26 >= ';' && LA35_26 <= '>')||(LA35_26 >= '@' && LA35_26 <= 'Z')||(LA35_26 >= '_' && LA35_26 <= 'z')||(LA35_26 >= '\u007F' && LA35_26 <= '\u2FFF')||(LA35_26 >= '\u3001' && LA35_26 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_26=='\\') ) {s = 37;}

                        else if ( (LA35_26=='*'||LA35_26=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA35_34 = input.LA(1);

                        s = -1;
                        if ( ((LA35_34 >= '\u0000' && LA35_34 <= '\b')||(LA35_34 >= '\u000B' && LA35_34 <= '\f')||(LA35_34 >= '\u000E' && LA35_34 <= '\u001F')||(LA35_34 >= '#' && LA35_34 <= '%')||LA35_34==','||LA35_34=='.'||(LA35_34 >= '0' && LA35_34 <= '9')||(LA35_34 >= ';' && LA35_34 <= '>')||(LA35_34 >= '@' && LA35_34 <= 'Z')||(LA35_34 >= '_' && LA35_34 <= 'z')||(LA35_34 >= '\u007F' && LA35_34 <= '\u2FFF')||(LA35_34 >= '\u3001' && LA35_34 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_34=='\\') ) {s = 37;}

                        else if ( (LA35_34=='*'||LA35_34=='?') ) {s = 29;}

                        else s = 49;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA35_59 = input.LA(1);

                        s = -1;
                        if ( ((LA35_59 >= '0' && LA35_59 <= '9')) ) {s = 62;}

                        else if ( (LA35_59=='.') ) {s = 60;}

                        else if ( (LA35_59=='-'||LA35_59=='/') ) {s = 46;}

                        else if ( ((LA35_59 >= '\u0000' && LA35_59 <= '\b')||(LA35_59 >= '\u000B' && LA35_59 <= '\f')||(LA35_59 >= '\u000E' && LA35_59 <= '\u001F')||(LA35_59 >= '#' && LA35_59 <= '%')||LA35_59==','||(LA35_59 >= ';' && LA35_59 <= '>')||(LA35_59 >= '@' && LA35_59 <= 'Z')||(LA35_59 >= '_' && LA35_59 <= 'z')||(LA35_59 >= '\u007F' && LA35_59 <= '\u2FFF')||(LA35_59 >= '\u3001' && LA35_59 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_59=='\\') ) {s = 37;}

                        else if ( (LA35_59=='*'||LA35_59=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA35_53 = input.LA(1);

                        s = -1;
                        if ( (LA35_53=='R'||LA35_53=='r') ) {s = 58;}

                        else if ( ((LA35_53 >= '\u0000' && LA35_53 <= '\b')||(LA35_53 >= '\u000B' && LA35_53 <= '\f')||(LA35_53 >= '\u000E' && LA35_53 <= '\u001F')||(LA35_53 >= '#' && LA35_53 <= '%')||LA35_53==','||LA35_53=='.'||(LA35_53 >= '0' && LA35_53 <= '9')||(LA35_53 >= ';' && LA35_53 <= '>')||(LA35_53 >= '@' && LA35_53 <= 'Q')||(LA35_53 >= 'S' && LA35_53 <= 'Z')||(LA35_53 >= '_' && LA35_53 <= 'q')||(LA35_53 >= 's' && LA35_53 <= 'z')||(LA35_53 >= '\u007F' && LA35_53 <= '\u2FFF')||(LA35_53 >= '\u3001' && LA35_53 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_53=='\\') ) {s = 37;}

                        else if ( (LA35_53=='*'||LA35_53=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 27 : 
                        int LA35_41 = input.LA(1);

                        s = -1;
                        if ( (LA35_41=='A'||LA35_41=='a') ) {s = 53;}

                        else if ( ((LA35_41 >= '\u0000' && LA35_41 <= '\b')||(LA35_41 >= '\u000B' && LA35_41 <= '\f')||(LA35_41 >= '\u000E' && LA35_41 <= '\u001F')||(LA35_41 >= '#' && LA35_41 <= '%')||LA35_41==','||LA35_41=='.'||(LA35_41 >= '0' && LA35_41 <= '9')||(LA35_41 >= ';' && LA35_41 <= '>')||LA35_41=='@'||(LA35_41 >= 'B' && LA35_41 <= 'Z')||(LA35_41 >= '_' && LA35_41 <= '`')||(LA35_41 >= 'b' && LA35_41 <= 'z')||(LA35_41 >= '\u007F' && LA35_41 <= '\u2FFF')||(LA35_41 >= '\u3001' && LA35_41 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_41=='\\') ) {s = 37;}

                        else if ( (LA35_41=='*'||LA35_41=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 28 : 
                        int LA35_15 = input.LA(1);

                        s = -1;
                        if ( ((LA35_15 >= '\u0000' && LA35_15 <= '!')||(LA35_15 >= '#' && LA35_15 <= ')')||(LA35_15 >= '+' && LA35_15 <= '>')||(LA35_15 >= '@' && LA35_15 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA35_15=='*'||LA35_15=='?') ) {s = 33;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;

                    case 29 : 
                        int LA35_51 = input.LA(1);

                        s = -1;
                        if ( ((LA35_51 >= '\u0000' && LA35_51 <= '\b')||(LA35_51 >= '\u000B' && LA35_51 <= '\f')||(LA35_51 >= '\u000E' && LA35_51 <= '\u001F')||(LA35_51 >= '#' && LA35_51 <= '%')||LA35_51==','||LA35_51=='.'||(LA35_51 >= '0' && LA35_51 <= '9')||(LA35_51 >= ';' && LA35_51 <= '>')||(LA35_51 >= '@' && LA35_51 <= 'Z')||(LA35_51 >= '_' && LA35_51 <= 'z')||(LA35_51 >= '\u007F' && LA35_51 <= '\u2FFF')||(LA35_51 >= '\u3001' && LA35_51 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_51=='\\') ) {s = 37;}

                        else if ( (LA35_51=='*'||LA35_51=='?') ) {s = 29;}

                        else s = 19;

                        if ( s>=0 ) return s;
                        break;

                    case 30 : 
                        int LA35_55 = input.LA(1);

                        s = -1;
                        if ( (LA35_55=='.') ) {s = 61;}

                        else if ( ((LA35_55 >= '0' && LA35_55 <= '9')) ) {s = 55;}

                        else if ( ((LA35_55 >= '\u0000' && LA35_55 <= '\b')||(LA35_55 >= '\u000B' && LA35_55 <= '\f')||(LA35_55 >= '\u000E' && LA35_55 <= '\u001F')||(LA35_55 >= '#' && LA35_55 <= '%')||LA35_55==','||(LA35_55 >= ';' && LA35_55 <= '>')||(LA35_55 >= '@' && LA35_55 <= 'Z')||(LA35_55 >= '_' && LA35_55 <= 'z')||(LA35_55 >= '\u007F' && LA35_55 <= '\u2FFF')||(LA35_55 >= '\u3001' && LA35_55 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_55=='\\') ) {s = 37;}

                        else if ( (LA35_55=='*'||LA35_55=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 31 : 
                        int LA35_27 = input.LA(1);

                        s = -1;
                        if ( ((LA35_27 >= '\u0000' && LA35_27 <= '\uFFFF')) ) {s = 47;}

                        if ( s>=0 ) return s;
                        break;

                    case 32 : 
                        int LA35_37 = input.LA(1);

                        s = -1;
                        if ( ((LA35_37 >= '\u0000' && LA35_37 <= '\uFFFF')) ) {s = 50;}

                        if ( s>=0 ) return s;
                        break;

                    case 33 : 
                        int LA35_58 = input.LA(1);

                        s = -1;
                        if ( ((LA35_58 >= '\u0000' && LA35_58 <= '\b')||(LA35_58 >= '\u000B' && LA35_58 <= '\f')||(LA35_58 >= '\u000E' && LA35_58 <= '\u001F')||(LA35_58 >= '#' && LA35_58 <= '%')||LA35_58==','||LA35_58=='.'||(LA35_58 >= '0' && LA35_58 <= '9')||(LA35_58 >= ';' && LA35_58 <= '>')||(LA35_58 >= '@' && LA35_58 <= 'Z')||(LA35_58 >= '_' && LA35_58 <= 'z')||(LA35_58 >= '\u007F' && LA35_58 <= '\u2FFF')||(LA35_58 >= '\u3001' && LA35_58 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_58=='\\') ) {s = 37;}

                        else if ( (LA35_58=='*'||LA35_58=='?') ) {s = 29;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 34 : 
                        int LA35_22 = input.LA(1);

                        s = -1;
                        if ( (LA35_22=='O'||LA35_22=='o') ) {s = 40;}

                        else if ( (LA35_22=='E'||LA35_22=='e') ) {s = 41;}

                        else if ( ((LA35_22 >= '\u0000' && LA35_22 <= '\b')||(LA35_22 >= '\u000B' && LA35_22 <= '\f')||(LA35_22 >= '\u000E' && LA35_22 <= '\u001F')||(LA35_22 >= '#' && LA35_22 <= '%')||LA35_22==','||LA35_22=='.'||(LA35_22 >= '0' && LA35_22 <= '9')||(LA35_22 >= ';' && LA35_22 <= '>')||(LA35_22 >= '@' && LA35_22 <= 'D')||(LA35_22 >= 'F' && LA35_22 <= 'N')||(LA35_22 >= 'P' && LA35_22 <= 'Z')||(LA35_22 >= '_' && LA35_22 <= 'd')||(LA35_22 >= 'f' && LA35_22 <= 'n')||(LA35_22 >= 'p' && LA35_22 <= 'z')||(LA35_22 >= '\u007F' && LA35_22 <= '\u2FFF')||(LA35_22 >= '\u3001' && LA35_22 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_22=='\\') ) {s = 37;}

                        else if ( (LA35_22=='*'||LA35_22=='?') ) {s = 29;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 35 : 
                        int LA35_60 = input.LA(1);

                        s = -1;
                        if ( ((LA35_60 >= '0' && LA35_60 <= '9')) ) {s = 63;}

                        else if ( ((LA35_60 >= '\u0000' && LA35_60 <= '\b')||(LA35_60 >= '\u000B' && LA35_60 <= '\f')||(LA35_60 >= '\u000E' && LA35_60 <= '\u001F')||(LA35_60 >= '#' && LA35_60 <= '%')||LA35_60==','||LA35_60=='.'||(LA35_60 >= ';' && LA35_60 <= '>')||(LA35_60 >= '@' && LA35_60 <= 'Z')||(LA35_60 >= '_' && LA35_60 <= 'z')||(LA35_60 >= '\u007F' && LA35_60 <= '\u2FFF')||(LA35_60 >= '\u3001' && LA35_60 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_60=='\\') ) {s = 37;}

                        else if ( (LA35_60=='*'||LA35_60=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 36 : 
                        int LA35_25 = input.LA(1);

                        s = -1;
                        if ( (LA35_25=='.') ) {s = 43;}

                        else if ( ((LA35_25 >= '0' && LA35_25 <= '9')) ) {s = 45;}

                        else if ( (LA35_25=='-'||LA35_25=='/') ) {s = 46;}

                        else if ( ((LA35_25 >= '\u0000' && LA35_25 <= '\b')||(LA35_25 >= '\u000B' && LA35_25 <= '\f')||(LA35_25 >= '\u000E' && LA35_25 <= '\u001F')||(LA35_25 >= '#' && LA35_25 <= '%')||LA35_25==','||(LA35_25 >= ';' && LA35_25 <= '>')||(LA35_25 >= '@' && LA35_25 <= 'Z')||(LA35_25 >= '_' && LA35_25 <= 'z')||(LA35_25 >= '\u007F' && LA35_25 <= '\u2FFF')||(LA35_25 >= '\u3001' && LA35_25 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_25=='\\') ) {s = 37;}

                        else if ( (LA35_25=='*'||LA35_25=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 37 : 
                        int LA35_63 = input.LA(1);

                        s = -1;
                        if ( ((LA35_63 >= '0' && LA35_63 <= '9')) ) {s = 64;}

                        else if ( ((LA35_63 >= '\u0000' && LA35_63 <= '\b')||(LA35_63 >= '\u000B' && LA35_63 <= '\f')||(LA35_63 >= '\u000E' && LA35_63 <= '\u001F')||(LA35_63 >= '#' && LA35_63 <= '%')||LA35_63==','||LA35_63=='.'||(LA35_63 >= ';' && LA35_63 <= '>')||(LA35_63 >= '@' && LA35_63 <= 'Z')||(LA35_63 >= '_' && LA35_63 <= 'z')||(LA35_63 >= '\u007F' && LA35_63 <= '\u2FFF')||(LA35_63 >= '\u3001' && LA35_63 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA35_63=='\\') ) {s = 37;}

                        else if ( (LA35_63=='*'||LA35_63=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 35, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}