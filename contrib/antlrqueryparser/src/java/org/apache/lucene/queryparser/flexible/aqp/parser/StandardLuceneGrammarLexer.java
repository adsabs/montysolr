// $ANTLR 3.4 /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g 2012-12-18 21:53:07

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class StandardLuceneGrammarLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__53=53;
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
    public static final int NEAR=22;
    public static final int NOT=23;
    public static final int NUMBER=24;
    public static final int OPERATOR=25;
    public static final int OR=26;
    public static final int PHRASE=27;
    public static final int PHRASE_ANYTHING=28;
    public static final int PLUS=29;
    public static final int QANYTHING=30;
    public static final int QDATE=31;
    public static final int QMARK=32;
    public static final int QNORMAL=33;
    public static final int QPHRASE=34;
    public static final int QPHRASETRUNC=35;
    public static final int QRANGEEX=36;
    public static final int QRANGEIN=37;
    public static final int QTRUNCATED=38;
    public static final int RBRACK=39;
    public static final int RCURLY=40;
    public static final int RPAREN=41;
    public static final int SQUOTE=42;
    public static final int STAR=43;
    public static final int TERM_CHAR=44;
    public static final int TERM_NORMAL=45;
    public static final int TERM_START_CHAR=46;
    public static final int TERM_TRUNCATED=47;
    public static final int TILDE=48;
    public static final int TMODIFIER=49;
    public static final int TO=50;
    public static final int VBAR=51;
    public static final int WS=52;

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
    public String getGrammarFileName() { return "/dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g"; }

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:11:7: ( '/' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:11:9: '/'
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
    // $ANTLR end "T__53"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:296:9: ( '(' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:296:11: '('
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:298:9: ( ')' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:298:11: ')'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:300:9: ( '[' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:300:11: '['
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:302:9: ( ']' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:302:11: ']'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:304:9: ( ':' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:304:11: ':'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:306:7: ( '+' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:306:9: '+'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:308:7: ( ( '-' | '\\!' ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:310:7: ( '*' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:310:9: '*'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:312:8: ( ( '?' )+ )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:312:10: ( '?' )+
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:312:10: ( '?' )+
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
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:312:10: '?'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:314:16: ( '|' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:314:18: '|'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:316:16: ( '&' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:316:18: '&'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:318:9: ( '{' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:318:11: '{'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:320:9: ( '}' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:320:11: '}'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:7: ( '^' ( ( INT )+ ( '.' ( INT )+ )? )? )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:9: '^' ( ( INT )+ ( '.' ( INT )+ )? )?
            {
            match('^'); 

            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:13: ( ( INT )+ ( '.' ( INT )+ )? )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( ((LA5_0 >= '0' && LA5_0 <= '9')) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:14: ( INT )+ ( '.' ( INT )+ )?
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:14: ( INT )+
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
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:19: ( '.' ( INT )+ )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='.') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:20: '.' ( INT )+
                            {
                            match('.'); 

                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:322:24: ( INT )+
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
                            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:7: ( '~' ( ( INT )+ ( '.' ( INT )+ )? )? )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:9: '~' ( ( INT )+ ( '.' ( INT )+ )? )?
            {
            match('~'); 

            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:13: ( ( INT )+ ( '.' ( INT )+ )? )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( ((LA9_0 >= '0' && LA9_0 <= '9')) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:14: ( INT )+ ( '.' ( INT )+ )?
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:14: ( INT )+
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
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:19: ( '.' ( INT )+ )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='.') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:20: '.' ( INT )+
                            {
                            match('.'); 

                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:324:24: ( INT )+
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
                            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:327:2: ( '\\\"' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:327:4: '\\\"'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:330:2: ( '\\'' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:330:4: '\\''
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:335:4: ( 'TO' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:335:6: 'TO'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:338:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:338:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:338:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:338:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:338:48: ( AMPER ( AMPER )? )
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:338:48: ( AMPER ( AMPER )? )
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:338:49: AMPER ( AMPER )?
                    {
                    mAMPER(); 


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:338:55: ( AMPER )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0=='&') ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:339:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:339:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:339:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:339:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:339:34: ( VBAR ( VBAR )? )
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:339:34: ( VBAR ( VBAR )? )
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:339:35: VBAR ( VBAR )?
                    {
                    mVBAR(); 


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:339:40: ( VBAR )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0=='|') ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:340:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:340:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:341:7: ( ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:341:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:341:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:341:10: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:341:60: 'n'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:344:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:344:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:365:13: ( '0' .. '9' )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:368:18: ( '\\\\' . )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:368:21: '\\\\' .
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:372:2: ( (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' ) | ESC_CHAR ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:373:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' ) | ESC_CHAR )
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:373:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' ) | ESC_CHAR )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( ((LA15_0 >= '\u0000' && LA15_0 <= '\b')||(LA15_0 >= '\u000B' && LA15_0 <= '\f')||(LA15_0 >= '\u000E' && LA15_0 <= '\u001F')||(LA15_0 >= '#' && LA15_0 <= '&')||LA15_0==','||(LA15_0 >= '.' && LA15_0 <= '9')||(LA15_0 >= ';' && LA15_0 <= '>')||(LA15_0 >= '@' && LA15_0 <= 'Z')||(LA15_0 >= '_' && LA15_0 <= 'z')||LA15_0=='|'||(LA15_0 >= '\u007F' && LA15_0 <= '\u2FFF')||(LA15_0 >= '\u3001' && LA15_0 <= '\uFFFF')) ) {
                alt15=1;
            }
            else if ( (LA15_0=='\\') ) {
                alt15=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }
            switch (alt15) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:373:3: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' )
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
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:379:5: ESC_CHAR
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:383:2: ( ( TERM_START_CHAR | '-' | '+' ) )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:384:2: ( TERM_START_CHAR | '-' | '+' )
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:384:2: ( TERM_START_CHAR | '-' | '+' )
            int alt16=3;
            int LA16_0 = input.LA(1);

            if ( ((LA16_0 >= '\u0000' && LA16_0 <= '\b')||(LA16_0 >= '\u000B' && LA16_0 <= '\f')||(LA16_0 >= '\u000E' && LA16_0 <= '\u001F')||(LA16_0 >= '#' && LA16_0 <= '&')||LA16_0==','||(LA16_0 >= '.' && LA16_0 <= '9')||(LA16_0 >= ';' && LA16_0 <= '>')||(LA16_0 >= '@' && LA16_0 <= 'Z')||LA16_0=='\\'||(LA16_0 >= '_' && LA16_0 <= 'z')||LA16_0=='|'||(LA16_0 >= '\u007F' && LA16_0 <= '\u2FFF')||(LA16_0 >= '\u3001' && LA16_0 <= '\uFFFF')) ) {
                alt16=1;
            }
            else if ( (LA16_0=='-') ) {
                alt16=2;
            }
            else if ( (LA16_0=='+') ) {
                alt16=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }
            switch (alt16) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:384:3: TERM_START_CHAR
                    {
                    mTERM_START_CHAR(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:384:21: '-'
                    {
                    match('-'); 

                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:384:27: '+'
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:389:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:390:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:390:2: ( INT )+
            int cnt17=0;
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0 >= '0' && LA17_0 <= '9')) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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
                        EarlyExitException eee =
                            new EarlyExitException(17, input);
                        throw eee;
                }
                cnt17++;
            } while (true);


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:390:7: ( '.' ( INT )+ )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0=='.') ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:390:8: '.' ( INT )+
                    {
                    match('.'); 

                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:390:12: ( INT )+
                    int cnt18=0;
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( ((LA18_0 >= '0' && LA18_0 <= '9')) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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
                    	    if ( cnt18 >= 1 ) break loop18;
                                EarlyExitException eee =
                                    new EarlyExitException(18, input);
                                throw eee;
                        }
                        cnt18++;
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:394:2: ( INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )? )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:2: INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )?
            {
            mINT(); 


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:6: ( INT )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( ((LA20_0 >= '0' && LA20_0 <= '9')) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:29: ( INT )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( ((LA21_0 >= '0' && LA21_0 <= '9')) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:
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


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:56: ( INT INT )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( ((LA22_0 >= '0' && LA22_0 <= '9')) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:395:57: INT INT
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:399:2: ( TERM_START_CHAR ( TERM_CHAR )* )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:400:2: TERM_START_CHAR ( TERM_CHAR )*
            {
            mTERM_START_CHAR(); 


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:400:18: ( TERM_CHAR )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( ((LA23_0 >= '\u0000' && LA23_0 <= '\b')||(LA23_0 >= '\u000B' && LA23_0 <= '\f')||(LA23_0 >= '\u000E' && LA23_0 <= '\u001F')||(LA23_0 >= '#' && LA23_0 <= '&')||(LA23_0 >= '+' && LA23_0 <= '9')||(LA23_0 >= ';' && LA23_0 <= '>')||(LA23_0 >= '@' && LA23_0 <= 'Z')||LA23_0=='\\'||(LA23_0 >= '_' && LA23_0 <= 'z')||LA23_0=='|'||(LA23_0 >= '\u007F' && LA23_0 <= '\u2FFF')||(LA23_0 >= '\u3001' && LA23_0 <= '\uFFFF')) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:400:20: TERM_CHAR
            	    {
            	    mTERM_CHAR(); 


            	    }
            	    break;

            	default :
            	    break loop23;
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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:404:15: ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ )
            int alt35=3;
            alt35 = dfa35.predict(input);
            switch (alt35) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:2: ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )*
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:2: ( STAR | QMARK )
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0=='*') ) {
                        alt24=1;
                    }
                    else if ( (LA24_0=='?') ) {
                        alt24=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 24, 0, input);

                        throw nvae;

                    }
                    switch (alt24) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:3: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:8: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+
                    int cnt27=0;
                    loop27:
                    do {
                        int alt27=2;
                        alt27 = dfa27.predict(input);
                        switch (alt27) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:16: ( TERM_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:16: ( TERM_CHAR )+
                    	    int cnt25=0;
                    	    loop25:
                    	    do {
                    	        int alt25=2;
                    	        int LA25_0 = input.LA(1);

                    	        if ( ((LA25_0 >= '\u0000' && LA25_0 <= '\b')||(LA25_0 >= '\u000B' && LA25_0 <= '\f')||(LA25_0 >= '\u000E' && LA25_0 <= '\u001F')||(LA25_0 >= '#' && LA25_0 <= '&')||(LA25_0 >= '+' && LA25_0 <= '9')||(LA25_0 >= ';' && LA25_0 <= '>')||(LA25_0 >= '@' && LA25_0 <= 'Z')||LA25_0=='\\'||(LA25_0 >= '_' && LA25_0 <= 'z')||LA25_0=='|'||(LA25_0 >= '\u007F' && LA25_0 <= '\u2FFF')||(LA25_0 >= '\u3001' && LA25_0 <= '\uFFFF')) ) {
                    	            alt25=1;
                    	        }


                    	        switch (alt25) {
                    	    	case 1 :
                    	    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:16: TERM_CHAR
                    	    	    {
                    	    	    mTERM_CHAR(); 


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    if ( cnt25 >= 1 ) break loop25;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(25, input);
                    	                throw eee;
                    	        }
                    	        cnt25++;
                    	    } while (true);


                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:27: ( QMARK | STAR )
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
                    	            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:28: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:34: STAR
                    	            {
                    	            mSTAR(); 


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt27 >= 1 ) break loop27;
                                EarlyExitException eee =
                                    new EarlyExitException(27, input);
                                throw eee;
                        }
                        cnt27++;
                    } while (true);


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:42: ( TERM_CHAR )*
                    loop28:
                    do {
                        int alt28=2;
                        int LA28_0 = input.LA(1);

                        if ( ((LA28_0 >= '\u0000' && LA28_0 <= '\b')||(LA28_0 >= '\u000B' && LA28_0 <= '\f')||(LA28_0 >= '\u000E' && LA28_0 <= '\u001F')||(LA28_0 >= '#' && LA28_0 <= '&')||(LA28_0 >= '+' && LA28_0 <= '9')||(LA28_0 >= ';' && LA28_0 <= '>')||(LA28_0 >= '@' && LA28_0 <= 'Z')||LA28_0=='\\'||(LA28_0 >= '_' && LA28_0 <= 'z')||LA28_0=='|'||(LA28_0 >= '\u007F' && LA28_0 <= '\u2FFF')||(LA28_0 >= '\u3001' && LA28_0 <= '\uFFFF')) ) {
                            alt28=1;
                        }


                        switch (alt28) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:405:43: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop28;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:4: TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )*
                    {
                    mTERM_START_CHAR(); 


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+
                    int cnt31=0;
                    loop31:
                    do {
                        int alt31=2;
                        alt31 = dfa31.predict(input);
                        switch (alt31) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:21: ( TERM_CHAR )* ( QMARK | STAR )
                    	    {
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:21: ( TERM_CHAR )*
                    	    loop29:
                    	    do {
                    	        int alt29=2;
                    	        int LA29_0 = input.LA(1);

                    	        if ( ((LA29_0 >= '\u0000' && LA29_0 <= '\b')||(LA29_0 >= '\u000B' && LA29_0 <= '\f')||(LA29_0 >= '\u000E' && LA29_0 <= '\u001F')||(LA29_0 >= '#' && LA29_0 <= '&')||(LA29_0 >= '+' && LA29_0 <= '9')||(LA29_0 >= ';' && LA29_0 <= '>')||(LA29_0 >= '@' && LA29_0 <= 'Z')||LA29_0=='\\'||(LA29_0 >= '_' && LA29_0 <= 'z')||LA29_0=='|'||(LA29_0 >= '\u007F' && LA29_0 <= '\u2FFF')||(LA29_0 >= '\u3001' && LA29_0 <= '\uFFFF')) ) {
                    	            alt29=1;
                    	        }


                    	        switch (alt29) {
                    	    	case 1 :
                    	    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:21: TERM_CHAR
                    	    	    {
                    	    	    mTERM_CHAR(); 


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    break loop29;
                    	        }
                    	    } while (true);


                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:32: ( QMARK | STAR )
                    	    int alt30=2;
                    	    int LA30_0 = input.LA(1);

                    	    if ( (LA30_0=='?') ) {
                    	        alt30=1;
                    	    }
                    	    else if ( (LA30_0=='*') ) {
                    	        alt30=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 30, 0, input);

                    	        throw nvae;

                    	    }
                    	    switch (alt30) {
                    	        case 1 :
                    	            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:33: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:39: STAR
                    	            {
                    	            mSTAR(); 


                    	            }
                    	            break;

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


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:47: ( TERM_CHAR )*
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( ((LA32_0 >= '\u0000' && LA32_0 <= '\b')||(LA32_0 >= '\u000B' && LA32_0 <= '\f')||(LA32_0 >= '\u000E' && LA32_0 <= '\u001F')||(LA32_0 >= '#' && LA32_0 <= '&')||(LA32_0 >= '+' && LA32_0 <= '9')||(LA32_0 >= ';' && LA32_0 <= '>')||(LA32_0 >= '@' && LA32_0 <= 'Z')||LA32_0=='\\'||(LA32_0 >= '_' && LA32_0 <= 'z')||LA32_0=='|'||(LA32_0 >= '\u007F' && LA32_0 <= '\u2FFF')||(LA32_0 >= '\u3001' && LA32_0 <= '\uFFFF')) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:406:48: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop32;
                        }
                    } while (true);


                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:407:4: ( STAR | QMARK ) ( TERM_CHAR )+
                    {
                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:407:4: ( STAR | QMARK )
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0=='*') ) {
                        alt33=1;
                    }
                    else if ( (LA33_0=='?') ) {
                        alt33=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 33, 0, input);

                        throw nvae;

                    }
                    switch (alt33) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:407:5: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:407:10: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:407:17: ( TERM_CHAR )+
                    int cnt34=0;
                    loop34:
                    do {
                        int alt34=2;
                        int LA34_0 = input.LA(1);

                        if ( ((LA34_0 >= '\u0000' && LA34_0 <= '\b')||(LA34_0 >= '\u000B' && LA34_0 <= '\f')||(LA34_0 >= '\u000E' && LA34_0 <= '\u001F')||(LA34_0 >= '#' && LA34_0 <= '&')||(LA34_0 >= '+' && LA34_0 <= '9')||(LA34_0 >= ';' && LA34_0 <= '>')||(LA34_0 >= '@' && LA34_0 <= 'Z')||LA34_0=='\\'||(LA34_0 >= '_' && LA34_0 <= 'z')||LA34_0=='|'||(LA34_0 >= '\u007F' && LA34_0 <= '\u2FFF')||(LA34_0 >= '\u3001' && LA34_0 <= '\uFFFF')) ) {
                            alt34=1;
                        }


                        switch (alt34) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:407:17: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


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
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:412:2: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:413:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:413:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' ) )+
            int cnt36=0;
            loop36:
            do {
                int alt36=3;
                int LA36_0 = input.LA(1);

                if ( (LA36_0=='\\') ) {
                    alt36=1;
                }
                else if ( ((LA36_0 >= '\u0000' && LA36_0 <= '!')||(LA36_0 >= '#' && LA36_0 <= ')')||(LA36_0 >= '+' && LA36_0 <= '>')||(LA36_0 >= '@' && LA36_0 <= '[')||(LA36_0 >= ']' && LA36_0 <= '\uFFFF')) ) {
                    alt36=2;
                }


                switch (alt36) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:413:10: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:413:19: ~ ( '\\\"' | '\\\\' | '?' | '*' )
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
    // $ANTLR end "PHRASE"

    // $ANTLR start "PHRASE_ANYTHING"
    public final void mPHRASE_ANYTHING() throws RecognitionException {
        try {
            int _type = PHRASE_ANYTHING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:416:17: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE )
            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:417:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:417:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+
            int cnt37=0;
            loop37:
            do {
                int alt37=3;
                int LA37_0 = input.LA(1);

                if ( (LA37_0=='\\') ) {
                    alt37=1;
                }
                else if ( ((LA37_0 >= '\u0000' && LA37_0 <= '!')||(LA37_0 >= '#' && LA37_0 <= '[')||(LA37_0 >= ']' && LA37_0 <= '\uFFFF')) ) {
                    alt37=2;
                }


                switch (alt37) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:417:10: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:417:19: ~ ( '\\\"' | '\\\\' )
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
            	    if ( cnt37 >= 1 ) break loop37;
                        EarlyExitException eee =
                            new EarlyExitException(37, input);
                        throw eee;
                }
                cnt37++;
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
        // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:8: ( T__53 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | NEAR | WS | NUMBER | DATE_TOKEN | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING )
        int alt38=28;
        alt38 = dfa38.predict(input);
        switch (alt38) {
            case 1 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:10: T__53
                {
                mT__53(); 


                }
                break;
            case 2 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:16: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 3 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:23: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 4 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:30: LBRACK
                {
                mLBRACK(); 


                }
                break;
            case 5 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:37: RBRACK
                {
                mRBRACK(); 


                }
                break;
            case 6 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:44: COLON
                {
                mCOLON(); 


                }
                break;
            case 7 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:50: PLUS
                {
                mPLUS(); 


                }
                break;
            case 8 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:55: MINUS
                {
                mMINUS(); 


                }
                break;
            case 9 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:61: STAR
                {
                mSTAR(); 


                }
                break;
            case 10 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:66: QMARK
                {
                mQMARK(); 


                }
                break;
            case 11 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:72: LCURLY
                {
                mLCURLY(); 


                }
                break;
            case 12 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:79: RCURLY
                {
                mRCURLY(); 


                }
                break;
            case 13 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:86: CARAT
                {
                mCARAT(); 


                }
                break;
            case 14 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:92: TILDE
                {
                mTILDE(); 


                }
                break;
            case 15 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:98: DQUOTE
                {
                mDQUOTE(); 


                }
                break;
            case 16 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:105: SQUOTE
                {
                mSQUOTE(); 


                }
                break;
            case 17 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:112: TO
                {
                mTO(); 


                }
                break;
            case 18 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:115: AND
                {
                mAND(); 


                }
                break;
            case 19 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:119: OR
                {
                mOR(); 


                }
                break;
            case 20 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:122: NOT
                {
                mNOT(); 


                }
                break;
            case 21 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:126: NEAR
                {
                mNEAR(); 


                }
                break;
            case 22 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:131: WS
                {
                mWS(); 


                }
                break;
            case 23 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:134: NUMBER
                {
                mNUMBER(); 


                }
                break;
            case 24 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:141: DATE_TOKEN
                {
                mDATE_TOKEN(); 


                }
                break;
            case 25 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:152: TERM_NORMAL
                {
                mTERM_NORMAL(); 


                }
                break;
            case 26 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:164: TERM_TRUNCATED
                {
                mTERM_TRUNCATED(); 


                }
                break;
            case 27 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:179: PHRASE
                {
                mPHRASE(); 


                }
                break;
            case 28 :
                // /dvt/workspace/montysolr/contrib/antlrqueryparser/grammars/StandardLuceneGrammar.g:1:186: PHRASE_ANYTHING
                {
                mPHRASE_ANYTHING(); 


                }
                break;

        }

    }


    protected DFA35 dfa35 = new DFA35(this);
    protected DFA27 dfa27 = new DFA27(this);
    protected DFA31 dfa31 = new DFA31(this);
    protected DFA38 dfa38 = new DFA38(this);
    static final String DFA35_eotS =
        "\4\uffff\1\10\1\uffff\2\10\2\uffff\1\10";
    static final String DFA35_eofS =
        "\13\uffff";
    static final String DFA35_minS =
        "\3\0\1\uffff\4\0\2\uffff\1\0";
    static final String DFA35_maxS =
        "\3\uffff\1\uffff\4\uffff\2\uffff\1\uffff";
    static final String DFA35_acceptS =
        "\3\uffff\1\2\4\uffff\1\3\1\1\1\uffff";
    static final String DFA35_specialS =
        "\1\7\1\5\1\1\1\uffff\1\0\1\4\1\3\1\6\2\uffff\1\2}>";
    static final String[] DFA35_transitionS = {
            "\11\3\2\uffff\2\3\1\uffff\22\3\3\uffff\4\3\3\uffff\1\1\1\uffff"+
            "\1\3\1\uffff\14\3\1\uffff\4\3\1\2\33\3\1\uffff\1\3\2\uffff\34"+
            "\3\1\uffff\1\3\2\uffff\u2f81\3\1\uffff\ucfff\3",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\4\uffff\1\7\1\4"+
            "\1\6\14\4\1\uffff\4\4\1\uffff\33\4\1\uffff\1\5\2\uffff\34\4"+
            "\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\4\uffff\1\7\1\4"+
            "\1\6\14\4\1\uffff\4\4\1\2\33\4\1\uffff\1\5\2\uffff\34\4\1\uffff"+
            "\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
            "",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\3\uffff\1\11\1\7"+
            "\1\4\1\6\14\4\1\uffff\4\4\1\11\33\4\1\uffff\1\5\2\uffff\34\4"+
            "\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
            "\0\12",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\3\uffff\1\11\1\7"+
            "\1\4\1\6\14\4\1\uffff\4\4\1\11\33\4\1\uffff\1\5\2\uffff\34\4"+
            "\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\3\uffff\1\11\1\7"+
            "\1\4\1\6\14\4\1\uffff\4\4\1\11\33\4\1\uffff\1\5\2\uffff\34\4"+
            "\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4",
            "",
            "",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\4\4\3\uffff\1\11\1\7"+
            "\1\4\1\6\14\4\1\uffff\4\4\1\11\33\4\1\uffff\1\5\2\uffff\34\4"+
            "\1\uffff\1\4\2\uffff\u2f81\4\1\uffff\ucfff\4"
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
            return "404:1: TERM_TRUNCATED : ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA35_4 = input.LA(1);

                        s = -1;
                        if ( (LA35_4=='*'||LA35_4=='?') ) {s = 9;}

                        else if ( ((LA35_4 >= '\u0000' && LA35_4 <= '\b')||(LA35_4 >= '\u000B' && LA35_4 <= '\f')||(LA35_4 >= '\u000E' && LA35_4 <= '\u001F')||(LA35_4 >= '#' && LA35_4 <= '&')||LA35_4==','||(LA35_4 >= '.' && LA35_4 <= '9')||(LA35_4 >= ';' && LA35_4 <= '>')||(LA35_4 >= '@' && LA35_4 <= 'Z')||(LA35_4 >= '_' && LA35_4 <= 'z')||LA35_4=='|'||(LA35_4 >= '\u007F' && LA35_4 <= '\u2FFF')||(LA35_4 >= '\u3001' && LA35_4 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA35_4=='\\') ) {s = 5;}

                        else if ( (LA35_4=='-') ) {s = 6;}

                        else if ( (LA35_4=='+') ) {s = 7;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA35_2 = input.LA(1);

                        s = -1;
                        if ( ((LA35_2 >= '\u0000' && LA35_2 <= '\b')||(LA35_2 >= '\u000B' && LA35_2 <= '\f')||(LA35_2 >= '\u000E' && LA35_2 <= '\u001F')||(LA35_2 >= '#' && LA35_2 <= '&')||LA35_2==','||(LA35_2 >= '.' && LA35_2 <= '9')||(LA35_2 >= ';' && LA35_2 <= '>')||(LA35_2 >= '@' && LA35_2 <= 'Z')||(LA35_2 >= '_' && LA35_2 <= 'z')||LA35_2=='|'||(LA35_2 >= '\u007F' && LA35_2 <= '\u2FFF')||(LA35_2 >= '\u3001' && LA35_2 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA35_2=='\\') ) {s = 5;}

                        else if ( (LA35_2=='-') ) {s = 6;}

                        else if ( (LA35_2=='+') ) {s = 7;}

                        else if ( (LA35_2=='?') ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA35_10 = input.LA(1);

                        s = -1;
                        if ( (LA35_10=='*'||LA35_10=='?') ) {s = 9;}

                        else if ( ((LA35_10 >= '\u0000' && LA35_10 <= '\b')||(LA35_10 >= '\u000B' && LA35_10 <= '\f')||(LA35_10 >= '\u000E' && LA35_10 <= '\u001F')||(LA35_10 >= '#' && LA35_10 <= '&')||LA35_10==','||(LA35_10 >= '.' && LA35_10 <= '9')||(LA35_10 >= ';' && LA35_10 <= '>')||(LA35_10 >= '@' && LA35_10 <= 'Z')||(LA35_10 >= '_' && LA35_10 <= 'z')||LA35_10=='|'||(LA35_10 >= '\u007F' && LA35_10 <= '\u2FFF')||(LA35_10 >= '\u3001' && LA35_10 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA35_10=='\\') ) {s = 5;}

                        else if ( (LA35_10=='-') ) {s = 6;}

                        else if ( (LA35_10=='+') ) {s = 7;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA35_6 = input.LA(1);

                        s = -1;
                        if ( (LA35_6=='*'||LA35_6=='?') ) {s = 9;}

                        else if ( ((LA35_6 >= '\u0000' && LA35_6 <= '\b')||(LA35_6 >= '\u000B' && LA35_6 <= '\f')||(LA35_6 >= '\u000E' && LA35_6 <= '\u001F')||(LA35_6 >= '#' && LA35_6 <= '&')||LA35_6==','||(LA35_6 >= '.' && LA35_6 <= '9')||(LA35_6 >= ';' && LA35_6 <= '>')||(LA35_6 >= '@' && LA35_6 <= 'Z')||(LA35_6 >= '_' && LA35_6 <= 'z')||LA35_6=='|'||(LA35_6 >= '\u007F' && LA35_6 <= '\u2FFF')||(LA35_6 >= '\u3001' && LA35_6 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA35_6=='\\') ) {s = 5;}

                        else if ( (LA35_6=='-') ) {s = 6;}

                        else if ( (LA35_6=='+') ) {s = 7;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA35_5 = input.LA(1);

                        s = -1;
                        if ( ((LA35_5 >= '\u0000' && LA35_5 <= '\uFFFF')) ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA35_1 = input.LA(1);

                        s = -1;
                        if ( ((LA35_1 >= '\u0000' && LA35_1 <= '\b')||(LA35_1 >= '\u000B' && LA35_1 <= '\f')||(LA35_1 >= '\u000E' && LA35_1 <= '\u001F')||(LA35_1 >= '#' && LA35_1 <= '&')||LA35_1==','||(LA35_1 >= '.' && LA35_1 <= '9')||(LA35_1 >= ';' && LA35_1 <= '>')||(LA35_1 >= '@' && LA35_1 <= 'Z')||(LA35_1 >= '_' && LA35_1 <= 'z')||LA35_1=='|'||(LA35_1 >= '\u007F' && LA35_1 <= '\u2FFF')||(LA35_1 >= '\u3001' && LA35_1 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA35_1=='\\') ) {s = 5;}

                        else if ( (LA35_1=='-') ) {s = 6;}

                        else if ( (LA35_1=='+') ) {s = 7;}

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA35_7 = input.LA(1);

                        s = -1;
                        if ( (LA35_7=='*'||LA35_7=='?') ) {s = 9;}

                        else if ( ((LA35_7 >= '\u0000' && LA35_7 <= '\b')||(LA35_7 >= '\u000B' && LA35_7 <= '\f')||(LA35_7 >= '\u000E' && LA35_7 <= '\u001F')||(LA35_7 >= '#' && LA35_7 <= '&')||LA35_7==','||(LA35_7 >= '.' && LA35_7 <= '9')||(LA35_7 >= ';' && LA35_7 <= '>')||(LA35_7 >= '@' && LA35_7 <= 'Z')||(LA35_7 >= '_' && LA35_7 <= 'z')||LA35_7=='|'||(LA35_7 >= '\u007F' && LA35_7 <= '\u2FFF')||(LA35_7 >= '\u3001' && LA35_7 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA35_7=='\\') ) {s = 5;}

                        else if ( (LA35_7=='-') ) {s = 6;}

                        else if ( (LA35_7=='+') ) {s = 7;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA35_0 = input.LA(1);

                        s = -1;
                        if ( (LA35_0=='*') ) {s = 1;}

                        else if ( (LA35_0=='?') ) {s = 2;}

                        else if ( ((LA35_0 >= '\u0000' && LA35_0 <= '\b')||(LA35_0 >= '\u000B' && LA35_0 <= '\f')||(LA35_0 >= '\u000E' && LA35_0 <= '\u001F')||(LA35_0 >= '#' && LA35_0 <= '&')||LA35_0==','||(LA35_0 >= '.' && LA35_0 <= '9')||(LA35_0 >= ';' && LA35_0 <= '>')||(LA35_0 >= '@' && LA35_0 <= 'Z')||LA35_0=='\\'||(LA35_0 >= '_' && LA35_0 <= 'z')||LA35_0=='|'||(LA35_0 >= '\u007F' && LA35_0 <= '\u2FFF')||(LA35_0 >= '\u3001' && LA35_0 <= '\uFFFF')) ) {s = 3;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 35, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA27_eotS =
        "\2\5\1\uffff\2\5\2\uffff\1\5";
    static final String DFA27_eofS =
        "\10\uffff";
    static final String DFA27_minS =
        "\5\0\2\uffff\1\0";
    static final String DFA27_maxS =
        "\5\uffff\2\uffff\1\uffff";
    static final String DFA27_acceptS =
        "\5\uffff\1\2\1\1\1\uffff";
    static final String DFA27_specialS =
        "\1\3\1\5\1\4\1\0\1\2\2\uffff\1\1}>";
    static final String[] DFA27_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\4\uffff\1\4\1\1\1"+
            "\3\14\1\1\uffff\4\1\1\uffff\33\1\1\uffff\1\2\2\uffff\34\1\1"+
            "\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4"+
            "\1\1\1\3\14\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1"+
            "\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
            "\0\7",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4"+
            "\1\1\1\3\14\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1"+
            "\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4"+
            "\1\1\1\3\14\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1"+
            "\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4"+
            "\1\1\1\3\14\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1"+
            "\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1"
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

    class DFA27 extends DFA {

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
        public String getDescription() {
            return "()+ loopback of 405:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA27_3 = input.LA(1);

                        s = -1;
                        if ( ((LA27_3 >= '\u0000' && LA27_3 <= '\b')||(LA27_3 >= '\u000B' && LA27_3 <= '\f')||(LA27_3 >= '\u000E' && LA27_3 <= '\u001F')||(LA27_3 >= '#' && LA27_3 <= '&')||LA27_3==','||(LA27_3 >= '.' && LA27_3 <= '9')||(LA27_3 >= ';' && LA27_3 <= '>')||(LA27_3 >= '@' && LA27_3 <= 'Z')||(LA27_3 >= '_' && LA27_3 <= 'z')||LA27_3=='|'||(LA27_3 >= '\u007F' && LA27_3 <= '\u2FFF')||(LA27_3 >= '\u3001' && LA27_3 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA27_3=='\\') ) {s = 2;}

                        else if ( (LA27_3=='-') ) {s = 3;}

                        else if ( (LA27_3=='+') ) {s = 4;}

                        else if ( (LA27_3=='*'||LA27_3=='?') ) {s = 6;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA27_7 = input.LA(1);

                        s = -1;
                        if ( ((LA27_7 >= '\u0000' && LA27_7 <= '\b')||(LA27_7 >= '\u000B' && LA27_7 <= '\f')||(LA27_7 >= '\u000E' && LA27_7 <= '\u001F')||(LA27_7 >= '#' && LA27_7 <= '&')||LA27_7==','||(LA27_7 >= '.' && LA27_7 <= '9')||(LA27_7 >= ';' && LA27_7 <= '>')||(LA27_7 >= '@' && LA27_7 <= 'Z')||(LA27_7 >= '_' && LA27_7 <= 'z')||LA27_7=='|'||(LA27_7 >= '\u007F' && LA27_7 <= '\u2FFF')||(LA27_7 >= '\u3001' && LA27_7 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA27_7=='\\') ) {s = 2;}

                        else if ( (LA27_7=='-') ) {s = 3;}

                        else if ( (LA27_7=='+') ) {s = 4;}

                        else if ( (LA27_7=='*'||LA27_7=='?') ) {s = 6;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA27_4 = input.LA(1);

                        s = -1;
                        if ( ((LA27_4 >= '\u0000' && LA27_4 <= '\b')||(LA27_4 >= '\u000B' && LA27_4 <= '\f')||(LA27_4 >= '\u000E' && LA27_4 <= '\u001F')||(LA27_4 >= '#' && LA27_4 <= '&')||LA27_4==','||(LA27_4 >= '.' && LA27_4 <= '9')||(LA27_4 >= ';' && LA27_4 <= '>')||(LA27_4 >= '@' && LA27_4 <= 'Z')||(LA27_4 >= '_' && LA27_4 <= 'z')||LA27_4=='|'||(LA27_4 >= '\u007F' && LA27_4 <= '\u2FFF')||(LA27_4 >= '\u3001' && LA27_4 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA27_4=='\\') ) {s = 2;}

                        else if ( (LA27_4=='-') ) {s = 3;}

                        else if ( (LA27_4=='+') ) {s = 4;}

                        else if ( (LA27_4=='*'||LA27_4=='?') ) {s = 6;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA27_0 = input.LA(1);

                        s = -1;
                        if ( ((LA27_0 >= '\u0000' && LA27_0 <= '\b')||(LA27_0 >= '\u000B' && LA27_0 <= '\f')||(LA27_0 >= '\u000E' && LA27_0 <= '\u001F')||(LA27_0 >= '#' && LA27_0 <= '&')||LA27_0==','||(LA27_0 >= '.' && LA27_0 <= '9')||(LA27_0 >= ';' && LA27_0 <= '>')||(LA27_0 >= '@' && LA27_0 <= 'Z')||(LA27_0 >= '_' && LA27_0 <= 'z')||LA27_0=='|'||(LA27_0 >= '\u007F' && LA27_0 <= '\u2FFF')||(LA27_0 >= '\u3001' && LA27_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA27_0=='\\') ) {s = 2;}

                        else if ( (LA27_0=='-') ) {s = 3;}

                        else if ( (LA27_0=='+') ) {s = 4;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA27_2 = input.LA(1);

                        s = -1;
                        if ( ((LA27_2 >= '\u0000' && LA27_2 <= '\uFFFF')) ) {s = 7;}

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA27_1 = input.LA(1);

                        s = -1;
                        if ( ((LA27_1 >= '\u0000' && LA27_1 <= '\b')||(LA27_1 >= '\u000B' && LA27_1 <= '\f')||(LA27_1 >= '\u000E' && LA27_1 <= '\u001F')||(LA27_1 >= '#' && LA27_1 <= '&')||LA27_1==','||(LA27_1 >= '.' && LA27_1 <= '9')||(LA27_1 >= ';' && LA27_1 <= '>')||(LA27_1 >= '@' && LA27_1 <= 'Z')||(LA27_1 >= '_' && LA27_1 <= 'z')||LA27_1=='|'||(LA27_1 >= '\u007F' && LA27_1 <= '\u2FFF')||(LA27_1 >= '\u3001' && LA27_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA27_1=='\\') ) {s = 2;}

                        else if ( (LA27_1=='-') ) {s = 3;}

                        else if ( (LA27_1=='+') ) {s = 4;}

                        else if ( (LA27_1=='*'||LA27_1=='?') ) {s = 6;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 27, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA31_eotS =
        "\2\5\1\uffff\2\5\2\uffff\1\5";
    static final String DFA31_eofS =
        "\10\uffff";
    static final String DFA31_minS =
        "\5\0\2\uffff\1\0";
    static final String DFA31_maxS =
        "\5\uffff\2\uffff\1\uffff";
    static final String DFA31_acceptS =
        "\5\uffff\1\2\1\1\1\uffff";
    static final String DFA31_specialS =
        "\1\1\1\3\1\4\1\5\1\2\2\uffff\1\0}>";
    static final String[] DFA31_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4\1"+
            "\1\1\3\14\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1\1"+
            "\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4"+
            "\1\1\1\3\14\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1"+
            "\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
            "\0\7",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4"+
            "\1\1\1\3\14\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1"+
            "\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4"+
            "\1\1\1\3\14\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1"+
            "\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\4\1\3\uffff\1\6\1\4"+
            "\1\1\1\3\14\1\1\uffff\4\1\1\6\33\1\1\uffff\1\2\2\uffff\34\1"+
            "\1\uffff\1\1\2\uffff\u2f81\1\1\uffff\ucfff\1"
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

    class DFA31 extends DFA {

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
        public String getDescription() {
            return "()+ loopback of 406:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA31_7 = input.LA(1);

                        s = -1;
                        if ( ((LA31_7 >= '\u0000' && LA31_7 <= '\b')||(LA31_7 >= '\u000B' && LA31_7 <= '\f')||(LA31_7 >= '\u000E' && LA31_7 <= '\u001F')||(LA31_7 >= '#' && LA31_7 <= '&')||LA31_7==','||(LA31_7 >= '.' && LA31_7 <= '9')||(LA31_7 >= ';' && LA31_7 <= '>')||(LA31_7 >= '@' && LA31_7 <= 'Z')||(LA31_7 >= '_' && LA31_7 <= 'z')||LA31_7=='|'||(LA31_7 >= '\u007F' && LA31_7 <= '\u2FFF')||(LA31_7 >= '\u3001' && LA31_7 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA31_7=='\\') ) {s = 2;}

                        else if ( (LA31_7=='-') ) {s = 3;}

                        else if ( (LA31_7=='+') ) {s = 4;}

                        else if ( (LA31_7=='*'||LA31_7=='?') ) {s = 6;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA31_0 = input.LA(1);

                        s = -1;
                        if ( ((LA31_0 >= '\u0000' && LA31_0 <= '\b')||(LA31_0 >= '\u000B' && LA31_0 <= '\f')||(LA31_0 >= '\u000E' && LA31_0 <= '\u001F')||(LA31_0 >= '#' && LA31_0 <= '&')||LA31_0==','||(LA31_0 >= '.' && LA31_0 <= '9')||(LA31_0 >= ';' && LA31_0 <= '>')||(LA31_0 >= '@' && LA31_0 <= 'Z')||(LA31_0 >= '_' && LA31_0 <= 'z')||LA31_0=='|'||(LA31_0 >= '\u007F' && LA31_0 <= '\u2FFF')||(LA31_0 >= '\u3001' && LA31_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA31_0=='\\') ) {s = 2;}

                        else if ( (LA31_0=='-') ) {s = 3;}

                        else if ( (LA31_0=='+') ) {s = 4;}

                        else if ( (LA31_0=='*'||LA31_0=='?') ) {s = 6;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA31_4 = input.LA(1);

                        s = -1;
                        if ( ((LA31_4 >= '\u0000' && LA31_4 <= '\b')||(LA31_4 >= '\u000B' && LA31_4 <= '\f')||(LA31_4 >= '\u000E' && LA31_4 <= '\u001F')||(LA31_4 >= '#' && LA31_4 <= '&')||LA31_4==','||(LA31_4 >= '.' && LA31_4 <= '9')||(LA31_4 >= ';' && LA31_4 <= '>')||(LA31_4 >= '@' && LA31_4 <= 'Z')||(LA31_4 >= '_' && LA31_4 <= 'z')||LA31_4=='|'||(LA31_4 >= '\u007F' && LA31_4 <= '\u2FFF')||(LA31_4 >= '\u3001' && LA31_4 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA31_4=='\\') ) {s = 2;}

                        else if ( (LA31_4=='-') ) {s = 3;}

                        else if ( (LA31_4=='+') ) {s = 4;}

                        else if ( (LA31_4=='*'||LA31_4=='?') ) {s = 6;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA31_1 = input.LA(1);

                        s = -1;
                        if ( ((LA31_1 >= '\u0000' && LA31_1 <= '\b')||(LA31_1 >= '\u000B' && LA31_1 <= '\f')||(LA31_1 >= '\u000E' && LA31_1 <= '\u001F')||(LA31_1 >= '#' && LA31_1 <= '&')||LA31_1==','||(LA31_1 >= '.' && LA31_1 <= '9')||(LA31_1 >= ';' && LA31_1 <= '>')||(LA31_1 >= '@' && LA31_1 <= 'Z')||(LA31_1 >= '_' && LA31_1 <= 'z')||LA31_1=='|'||(LA31_1 >= '\u007F' && LA31_1 <= '\u2FFF')||(LA31_1 >= '\u3001' && LA31_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA31_1=='\\') ) {s = 2;}

                        else if ( (LA31_1=='-') ) {s = 3;}

                        else if ( (LA31_1=='+') ) {s = 4;}

                        else if ( (LA31_1=='*'||LA31_1=='?') ) {s = 6;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA31_2 = input.LA(1);

                        s = -1;
                        if ( ((LA31_2 >= '\u0000' && LA31_2 <= '\uFFFF')) ) {s = 7;}

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA31_3 = input.LA(1);

                        s = -1;
                        if ( ((LA31_3 >= '\u0000' && LA31_3 <= '\b')||(LA31_3 >= '\u000B' && LA31_3 <= '\f')||(LA31_3 >= '\u000E' && LA31_3 <= '\u001F')||(LA31_3 >= '#' && LA31_3 <= '&')||LA31_3==','||(LA31_3 >= '.' && LA31_3 <= '9')||(LA31_3 >= ';' && LA31_3 <= '>')||(LA31_3 >= '@' && LA31_3 <= 'Z')||(LA31_3 >= '_' && LA31_3 <= 'z')||LA31_3=='|'||(LA31_3 >= '\u007F' && LA31_3 <= '\u2FFF')||(LA31_3 >= '\u3001' && LA31_3 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA31_3=='\\') ) {s = 2;}

                        else if ( (LA31_3=='-') ) {s = 3;}

                        else if ( (LA31_3=='+') ) {s = 4;}

                        else if ( (LA31_3=='*'||LA31_3=='?') ) {s = 6;}

                        else s = 5;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 31, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA38_eotS =
        "\1\uffff\1\34\7\uffff\1\42\1\43\4\uffff\1\44\1\uffff\2\51\1\53\1"+
        "\51\1\56\1\62\1\51\1\uffff\1\64\1\51\2\uffff\1\51\1\uffff\2\51\7"+
        "\uffff\1\74\1\uffff\1\51\1\uffff\1\53\1\56\1\uffff\1\56\2\51\1\uffff"+
        "\1\51\1\uffff\1\64\4\51\3\uffff\1\53\1\104\1\51\2\64\1\51\2\uffff"+
        "\1\62\1\64\4\51\1\64\1\51\1\116\1\uffff\1\51\1\116";
    static final String DFA38_eofS =
        "\121\uffff";
    static final String DFA38_minS =
        "\2\0\7\uffff\2\0\4\uffff\1\0\1\uffff\7\0\1\uffff\3\0\1\uffff\4\0"+
        "\4\uffff\2\0\1\uffff\1\0\1\uffff\1\0\1\uffff\2\0\1\uffff\3\0\1\uffff"+
        "\1\0\1\uffff\6\0\2\uffff\6\0\2\uffff\11\0\1\uffff\2\0";
    static final String DFA38_maxS =
        "\2\uffff\7\uffff\2\uffff\4\uffff\1\uffff\1\uffff\7\uffff\1\uffff"+
        "\3\uffff\1\uffff\4\uffff\4\uffff\2\uffff\1\uffff\1\uffff\1\uffff"+
        "\1\uffff\1\uffff\2\uffff\1\uffff\3\uffff\1\uffff\1\uffff\1\uffff"+
        "\6\uffff\2\uffff\6\uffff\2\uffff\11\uffff\1\uffff\2\uffff";
    static final String DFA38_acceptS =
        "\2\uffff\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff\1\13\1\14\1\15\1\16"+
        "\1\uffff\1\20\7\uffff\1\26\3\uffff\1\1\4\uffff\1\32\1\11\1\12\1"+
        "\17\2\uffff\1\34\1\uffff\1\31\1\uffff\1\22\2\uffff\1\23\3\uffff"+
        "\1\25\1\uffff\1\27\6\uffff\1\33\1\21\6\uffff\1\33\1\24\11\uffff"+
        "\1\30\2\uffff";
    static final String DFA38_specialS =
        "\1\10\1\1\7\uffff\1\55\1\63\4\uffff\1\56\1\uffff\1\3\1\7\1\50\1"+
        "\13\1\51\1\62\1\21\1\uffff\1\2\1\34\1\11\1\uffff\1\15\1\12\1\46"+
        "\1\44\4\uffff\1\17\1\0\1\uffff\1\57\1\uffff\1\47\1\uffff\1\30\1"+
        "\5\1\uffff\1\60\1\32\1\40\1\uffff\1\61\1\uffff\1\43\1\14\1\25\1"+
        "\37\1\45\1\24\2\uffff\1\16\1\27\1\36\1\6\1\4\1\42\2\uffff\1\35\1"+
        "\54\1\41\1\33\1\22\1\23\1\53\1\26\1\31\1\uffff\1\20\1\52}>";
    static final String[] DFA38_transitionS = {
            "\11\32\2\30\2\32\1\30\22\32\1\30\1\10\1\17\3\32\1\23\1\20\1"+
            "\2\1\3\1\11\1\7\1\32\1\10\1\32\1\1\12\31\1\6\4\32\1\12\1\32"+
            "\1\22\14\32\1\27\1\24\4\32\1\21\6\32\1\4\1\33\1\5\1\15\2\32"+
            "\1\22\14\32\1\26\1\24\13\32\1\13\1\25\1\14\1\16\u2f81\32\1\30"+
            "\ucfff\32",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\11\41\2\uffff\2\41\1\uffff\22\41\3\uffff\4\41\4\uffff\17\41"+
            "\1\uffff\4\41\1\uffff\33\41\1\uffff\1\41\2\uffff\34\41\1\uffff"+
            "\1\41\2\uffff\u2f81\41\1\uffff\ucfff\41",
            "\11\41\2\uffff\2\41\1\uffff\22\41\3\uffff\4\41\4\uffff\17\41"+
            "\1\uffff\4\41\1\12\33\41\1\uffff\1\41\2\uffff\34\41\1\uffff"+
            "\1\41\2\uffff\u2f81\41\1\uffff\ucfff\41",
            "",
            "",
            "",
            "",
            "\42\46\1\uffff\7\46\1\47\24\46\1\47\34\46\1\45\uffa3\46",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\17\35\1\50\13\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\16\35\1\52\14\35\1\uffff"+
            "\1\36\2\uffff\17\35\1\52\14\35\1\uffff\1\35\2\uffff\u2f81\35"+
            "\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\3\35\1\54\3\uffff"+
            "\1\41\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1"+
            "\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\22\35\1\55\10\35\1\uffff"+
            "\1\36\2\uffff\23\35\1\55\10\35\1\uffff\1\35\2\uffff\u2f81\35"+
            "\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\57\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\5\35\1\61\11\35\1\60"+
            "\13\35\1\uffff\1\36\2\uffff\6\35\1\61\11\35\1\60\13\35\1\uffff"+
            "\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\5\35\1\61\11\35\1\60"+
            "\13\35\1\uffff\1\36\2\uffff\6\35\1\61\11\35\1\60\13\35\1\uffff"+
            "\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\67\1\63\1\66\12\65\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\0\70",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\0\71",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "",
            "",
            "",
            "",
            "\0\72",
            "\42\46\1\73\7\46\1\47\24\46\1\47\34\46\1\45\uffa3\46",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\4\35\1\75\26\35\1\uffff"+
            "\1\36\2\uffff\5\35\1\75\26\35\1\uffff\1\35\2\uffff\u2f81\35"+
            "\1\uffff\ucfff\35",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\24\35\1\76\6\35\1\uffff"+
            "\1\36\2\uffff\25\35\1\76\6\35\1\uffff\1\35\2\uffff\u2f81\35"+
            "\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\1\35\1\77\31\35\1\uffff"+
            "\1\36\2\uffff\2\35\1\77\31\35\1\uffff\1\35\2\uffff\u2f81\35"+
            "\1\uffff\ucfff\35",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\100\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\67\1\63\1\66\12\101\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\102\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\102\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\42\46\1\73\7\46\1\47\24\46\1\47\34\46\1\45\uffa3\46",
            "",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\22\35\1\105\10\35\1"+
            "\uffff\1\36\2\uffff\23\35\1\105\10\35\1\uffff\1\35\2\uffff\u2f81"+
            "\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\110\2\107\12\106\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\1\111\1\35\12\101\1\uffff\4\35\1\41\33\35\1"+
            "\uffff\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff"+
            "\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\110\2\107\12\112\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\110\2\107\12\113\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\114\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\114\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\113\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\110\2\107\12\35\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\113\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\115\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\117\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\2\35\12\120\1\uffff\4\35\1\41\33\35\1\uffff"+
            "\1\36\2\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff"+
            "\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\4\35\3\uffff\1\41"+
            "\1\40\1\35\1\37\14\35\1\uffff\4\35\1\41\33\35\1\uffff\1\36\2"+
            "\uffff\34\35\1\uffff\1\35\2\uffff\u2f81\35\1\uffff\ucfff\35"
    };

    static final short[] DFA38_eot = DFA.unpackEncodedString(DFA38_eotS);
    static final short[] DFA38_eof = DFA.unpackEncodedString(DFA38_eofS);
    static final char[] DFA38_min = DFA.unpackEncodedStringToUnsignedChars(DFA38_minS);
    static final char[] DFA38_max = DFA.unpackEncodedStringToUnsignedChars(DFA38_maxS);
    static final short[] DFA38_accept = DFA.unpackEncodedString(DFA38_acceptS);
    static final short[] DFA38_special = DFA.unpackEncodedString(DFA38_specialS);
    static final short[][] DFA38_transition;

    static {
        int numStates = DFA38_transitionS.length;
        DFA38_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA38_transition[i] = DFA.unpackEncodedString(DFA38_transitionS[i]);
        }
    }

    class DFA38 extends DFA {

        public DFA38(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 38;
            this.eot = DFA38_eot;
            this.eof = DFA38_eof;
            this.min = DFA38_min;
            this.max = DFA38_max;
            this.accept = DFA38_accept;
            this.special = DFA38_special;
            this.transition = DFA38_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__53 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | NEAR | WS | NUMBER | DATE_TOKEN | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA38_38 = input.LA(1);

                        s = -1;
                        if ( (LA38_38=='\"') ) {s = 59;}

                        else if ( (LA38_38=='\\') ) {s = 37;}

                        else if ( ((LA38_38 >= '\u0000' && LA38_38 <= '!')||(LA38_38 >= '#' && LA38_38 <= ')')||(LA38_38 >= '+' && LA38_38 <= '>')||(LA38_38 >= '@' && LA38_38 <= '[')||(LA38_38 >= ']' && LA38_38 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA38_38=='*'||LA38_38=='?') ) {s = 39;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA38_1 = input.LA(1);

                        s = -1;
                        if ( ((LA38_1 >= '\u0000' && LA38_1 <= '\b')||(LA38_1 >= '\u000B' && LA38_1 <= '\f')||(LA38_1 >= '\u000E' && LA38_1 <= '\u001F')||(LA38_1 >= '#' && LA38_1 <= '&')||LA38_1==','||(LA38_1 >= '.' && LA38_1 <= '9')||(LA38_1 >= ';' && LA38_1 <= '>')||(LA38_1 >= '@' && LA38_1 <= 'Z')||(LA38_1 >= '_' && LA38_1 <= 'z')||LA38_1=='|'||(LA38_1 >= '\u007F' && LA38_1 <= '\u2FFF')||(LA38_1 >= '\u3001' && LA38_1 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_1=='\\') ) {s = 30;}

                        else if ( (LA38_1=='-') ) {s = 31;}

                        else if ( (LA38_1=='+') ) {s = 32;}

                        else if ( (LA38_1=='*'||LA38_1=='?') ) {s = 33;}

                        else s = 28;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA38_25 = input.LA(1);

                        s = -1;
                        if ( (LA38_25=='.') ) {s = 51;}

                        else if ( ((LA38_25 >= '0' && LA38_25 <= '9')) ) {s = 53;}

                        else if ( (LA38_25=='/') ) {s = 54;}

                        else if ( (LA38_25=='-') ) {s = 55;}

                        else if ( ((LA38_25 >= '\u0000' && LA38_25 <= '\b')||(LA38_25 >= '\u000B' && LA38_25 <= '\f')||(LA38_25 >= '\u000E' && LA38_25 <= '\u001F')||(LA38_25 >= '#' && LA38_25 <= '&')||LA38_25==','||(LA38_25 >= ';' && LA38_25 <= '>')||(LA38_25 >= '@' && LA38_25 <= 'Z')||(LA38_25 >= '_' && LA38_25 <= 'z')||LA38_25=='|'||(LA38_25 >= '\u007F' && LA38_25 <= '\u2FFF')||(LA38_25 >= '\u3001' && LA38_25 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_25=='\\') ) {s = 30;}

                        else if ( (LA38_25=='+') ) {s = 32;}

                        else if ( (LA38_25=='*'||LA38_25=='?') ) {s = 33;}

                        else s = 52;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA38_17 = input.LA(1);

                        s = -1;
                        if ( (LA38_17=='O') ) {s = 40;}

                        else if ( ((LA38_17 >= '\u0000' && LA38_17 <= '\b')||(LA38_17 >= '\u000B' && LA38_17 <= '\f')||(LA38_17 >= '\u000E' && LA38_17 <= '\u001F')||(LA38_17 >= '#' && LA38_17 <= '&')||LA38_17==','||(LA38_17 >= '.' && LA38_17 <= '9')||(LA38_17 >= ';' && LA38_17 <= '>')||(LA38_17 >= '@' && LA38_17 <= 'N')||(LA38_17 >= 'P' && LA38_17 <= 'Z')||(LA38_17 >= '_' && LA38_17 <= 'z')||LA38_17=='|'||(LA38_17 >= '\u007F' && LA38_17 <= '\u2FFF')||(LA38_17 >= '\u3001' && LA38_17 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_17=='\\') ) {s = 30;}

                        else if ( (LA38_17=='-') ) {s = 31;}

                        else if ( (LA38_17=='+') ) {s = 32;}

                        else if ( (LA38_17=='*'||LA38_17=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA38_65 = input.LA(1);

                        s = -1;
                        if ( (LA38_65=='.') ) {s = 73;}

                        else if ( ((LA38_65 >= '0' && LA38_65 <= '9')) ) {s = 65;}

                        else if ( ((LA38_65 >= '\u0000' && LA38_65 <= '\b')||(LA38_65 >= '\u000B' && LA38_65 <= '\f')||(LA38_65 >= '\u000E' && LA38_65 <= '\u001F')||(LA38_65 >= '#' && LA38_65 <= '&')||LA38_65==','||LA38_65=='/'||(LA38_65 >= ';' && LA38_65 <= '>')||(LA38_65 >= '@' && LA38_65 <= 'Z')||(LA38_65 >= '_' && LA38_65 <= 'z')||LA38_65=='|'||(LA38_65 >= '\u007F' && LA38_65 <= '\u2FFF')||(LA38_65 >= '\u3001' && LA38_65 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_65=='\\') ) {s = 30;}

                        else if ( (LA38_65=='-') ) {s = 31;}

                        else if ( (LA38_65=='+') ) {s = 32;}

                        else if ( (LA38_65=='*'||LA38_65=='?') ) {s = 33;}

                        else s = 52;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA38_45 = input.LA(1);

                        s = -1;
                        if ( ((LA38_45 >= '\u0000' && LA38_45 <= '\b')||(LA38_45 >= '\u000B' && LA38_45 <= '\f')||(LA38_45 >= '\u000E' && LA38_45 <= '\u001F')||(LA38_45 >= '#' && LA38_45 <= '&')||LA38_45==','||(LA38_45 >= '.' && LA38_45 <= '9')||(LA38_45 >= ';' && LA38_45 <= '>')||(LA38_45 >= '@' && LA38_45 <= 'Z')||(LA38_45 >= '_' && LA38_45 <= 'z')||LA38_45=='|'||(LA38_45 >= '\u007F' && LA38_45 <= '\u2FFF')||(LA38_45 >= '\u3001' && LA38_45 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_45=='\\') ) {s = 30;}

                        else if ( (LA38_45=='-') ) {s = 31;}

                        else if ( (LA38_45=='+') ) {s = 32;}

                        else if ( (LA38_45=='*'||LA38_45=='?') ) {s = 33;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA38_64 = input.LA(1);

                        s = -1;
                        if ( ((LA38_64 >= '0' && LA38_64 <= '9')) ) {s = 70;}

                        else if ( ((LA38_64 >= '.' && LA38_64 <= '/')) ) {s = 71;}

                        else if ( (LA38_64=='-') ) {s = 72;}

                        else if ( ((LA38_64 >= '\u0000' && LA38_64 <= '\b')||(LA38_64 >= '\u000B' && LA38_64 <= '\f')||(LA38_64 >= '\u000E' && LA38_64 <= '\u001F')||(LA38_64 >= '#' && LA38_64 <= '&')||LA38_64==','||(LA38_64 >= ';' && LA38_64 <= '>')||(LA38_64 >= '@' && LA38_64 <= 'Z')||(LA38_64 >= '_' && LA38_64 <= 'z')||LA38_64=='|'||(LA38_64 >= '\u007F' && LA38_64 <= '\u2FFF')||(LA38_64 >= '\u3001' && LA38_64 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_64=='\\') ) {s = 30;}

                        else if ( (LA38_64=='+') ) {s = 32;}

                        else if ( (LA38_64=='*'||LA38_64=='?') ) {s = 33;}

                        else s = 52;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA38_18 = input.LA(1);

                        s = -1;
                        if ( (LA38_18=='N'||LA38_18=='n') ) {s = 42;}

                        else if ( ((LA38_18 >= '\u0000' && LA38_18 <= '\b')||(LA38_18 >= '\u000B' && LA38_18 <= '\f')||(LA38_18 >= '\u000E' && LA38_18 <= '\u001F')||(LA38_18 >= '#' && LA38_18 <= '&')||LA38_18==','||(LA38_18 >= '.' && LA38_18 <= '9')||(LA38_18 >= ';' && LA38_18 <= '>')||(LA38_18 >= '@' && LA38_18 <= 'M')||(LA38_18 >= 'O' && LA38_18 <= 'Z')||(LA38_18 >= '_' && LA38_18 <= 'm')||(LA38_18 >= 'o' && LA38_18 <= 'z')||LA38_18=='|'||(LA38_18 >= '\u007F' && LA38_18 <= '\u2FFF')||(LA38_18 >= '\u3001' && LA38_18 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_18=='\\') ) {s = 30;}

                        else if ( (LA38_18=='-') ) {s = 31;}

                        else if ( (LA38_18=='+') ) {s = 32;}

                        else if ( (LA38_18=='*'||LA38_18=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA38_0 = input.LA(1);

                        s = -1;
                        if ( (LA38_0=='/') ) {s = 1;}

                        else if ( (LA38_0=='(') ) {s = 2;}

                        else if ( (LA38_0==')') ) {s = 3;}

                        else if ( (LA38_0=='[') ) {s = 4;}

                        else if ( (LA38_0==']') ) {s = 5;}

                        else if ( (LA38_0==':') ) {s = 6;}

                        else if ( (LA38_0=='+') ) {s = 7;}

                        else if ( (LA38_0=='!'||LA38_0=='-') ) {s = 8;}

                        else if ( (LA38_0=='*') ) {s = 9;}

                        else if ( (LA38_0=='?') ) {s = 10;}

                        else if ( (LA38_0=='{') ) {s = 11;}

                        else if ( (LA38_0=='}') ) {s = 12;}

                        else if ( (LA38_0=='^') ) {s = 13;}

                        else if ( (LA38_0=='~') ) {s = 14;}

                        else if ( (LA38_0=='\"') ) {s = 15;}

                        else if ( (LA38_0=='\'') ) {s = 16;}

                        else if ( (LA38_0=='T') ) {s = 17;}

                        else if ( (LA38_0=='A'||LA38_0=='a') ) {s = 18;}

                        else if ( (LA38_0=='&') ) {s = 19;}

                        else if ( (LA38_0=='O'||LA38_0=='o') ) {s = 20;}

                        else if ( (LA38_0=='|') ) {s = 21;}

                        else if ( (LA38_0=='n') ) {s = 22;}

                        else if ( (LA38_0=='N') ) {s = 23;}

                        else if ( ((LA38_0 >= '\t' && LA38_0 <= '\n')||LA38_0=='\r'||LA38_0==' '||LA38_0=='\u3000') ) {s = 24;}

                        else if ( ((LA38_0 >= '0' && LA38_0 <= '9')) ) {s = 25;}

                        else if ( ((LA38_0 >= '\u0000' && LA38_0 <= '\b')||(LA38_0 >= '\u000B' && LA38_0 <= '\f')||(LA38_0 >= '\u000E' && LA38_0 <= '\u001F')||(LA38_0 >= '#' && LA38_0 <= '%')||LA38_0==','||LA38_0=='.'||(LA38_0 >= ';' && LA38_0 <= '>')||LA38_0=='@'||(LA38_0 >= 'B' && LA38_0 <= 'M')||(LA38_0 >= 'P' && LA38_0 <= 'S')||(LA38_0 >= 'U' && LA38_0 <= 'Z')||(LA38_0 >= '_' && LA38_0 <= '`')||(LA38_0 >= 'b' && LA38_0 <= 'm')||(LA38_0 >= 'p' && LA38_0 <= 'z')||(LA38_0 >= '\u007F' && LA38_0 <= '\u2FFF')||(LA38_0 >= '\u3001' && LA38_0 <= '\uFFFF')) ) {s = 26;}

                        else if ( (LA38_0=='\\') ) {s = 27;}

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA38_27 = input.LA(1);

                        s = -1;
                        if ( ((LA38_27 >= '\u0000' && LA38_27 <= '\uFFFF')) ) {s = 56;}

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA38_30 = input.LA(1);

                        s = -1;
                        if ( ((LA38_30 >= '\u0000' && LA38_30 <= '\uFFFF')) ) {s = 57;}

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA38_20 = input.LA(1);

                        s = -1;
                        if ( (LA38_20=='R'||LA38_20=='r') ) {s = 45;}

                        else if ( ((LA38_20 >= '\u0000' && LA38_20 <= '\b')||(LA38_20 >= '\u000B' && LA38_20 <= '\f')||(LA38_20 >= '\u000E' && LA38_20 <= '\u001F')||(LA38_20 >= '#' && LA38_20 <= '&')||LA38_20==','||(LA38_20 >= '.' && LA38_20 <= '9')||(LA38_20 >= ';' && LA38_20 <= '>')||(LA38_20 >= '@' && LA38_20 <= 'Q')||(LA38_20 >= 'S' && LA38_20 <= 'Z')||(LA38_20 >= '_' && LA38_20 <= 'q')||(LA38_20 >= 's' && LA38_20 <= 'z')||LA38_20=='|'||(LA38_20 >= '\u007F' && LA38_20 <= '\u2FFF')||(LA38_20 >= '\u3001' && LA38_20 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_20=='\\') ) {s = 30;}

                        else if ( (LA38_20=='-') ) {s = 31;}

                        else if ( (LA38_20=='+') ) {s = 32;}

                        else if ( (LA38_20=='*'||LA38_20=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA38_54 = input.LA(1);

                        s = -1;
                        if ( ((LA38_54 >= '0' && LA38_54 <= '9')) ) {s = 66;}

                        else if ( ((LA38_54 >= '\u0000' && LA38_54 <= '\b')||(LA38_54 >= '\u000B' && LA38_54 <= '\f')||(LA38_54 >= '\u000E' && LA38_54 <= '\u001F')||(LA38_54 >= '#' && LA38_54 <= '&')||LA38_54==','||(LA38_54 >= '.' && LA38_54 <= '/')||(LA38_54 >= ';' && LA38_54 <= '>')||(LA38_54 >= '@' && LA38_54 <= 'Z')||(LA38_54 >= '_' && LA38_54 <= 'z')||LA38_54=='|'||(LA38_54 >= '\u007F' && LA38_54 <= '\u2FFF')||(LA38_54 >= '\u3001' && LA38_54 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_54=='\\') ) {s = 30;}

                        else if ( (LA38_54=='-') ) {s = 31;}

                        else if ( (LA38_54=='+') ) {s = 32;}

                        else if ( (LA38_54=='*'||LA38_54=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA38_29 = input.LA(1);

                        s = -1;
                        if ( ((LA38_29 >= '\u0000' && LA38_29 <= '\b')||(LA38_29 >= '\u000B' && LA38_29 <= '\f')||(LA38_29 >= '\u000E' && LA38_29 <= '\u001F')||(LA38_29 >= '#' && LA38_29 <= '&')||LA38_29==','||(LA38_29 >= '.' && LA38_29 <= '9')||(LA38_29 >= ';' && LA38_29 <= '>')||(LA38_29 >= '@' && LA38_29 <= 'Z')||(LA38_29 >= '_' && LA38_29 <= 'z')||LA38_29=='|'||(LA38_29 >= '\u007F' && LA38_29 <= '\u2FFF')||(LA38_29 >= '\u3001' && LA38_29 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_29=='\\') ) {s = 30;}

                        else if ( (LA38_29=='-') ) {s = 31;}

                        else if ( (LA38_29=='+') ) {s = 32;}

                        else if ( (LA38_29=='*'||LA38_29=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA38_61 = input.LA(1);

                        s = -1;
                        if ( ((LA38_61 >= '\u0000' && LA38_61 <= '\b')||(LA38_61 >= '\u000B' && LA38_61 <= '\f')||(LA38_61 >= '\u000E' && LA38_61 <= '\u001F')||(LA38_61 >= '#' && LA38_61 <= '&')||LA38_61==','||(LA38_61 >= '.' && LA38_61 <= '9')||(LA38_61 >= ';' && LA38_61 <= '>')||(LA38_61 >= '@' && LA38_61 <= 'Z')||(LA38_61 >= '_' && LA38_61 <= 'z')||LA38_61=='|'||(LA38_61 >= '\u007F' && LA38_61 <= '\u2FFF')||(LA38_61 >= '\u3001' && LA38_61 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_61=='\\') ) {s = 30;}

                        else if ( (LA38_61=='-') ) {s = 31;}

                        else if ( (LA38_61=='+') ) {s = 32;}

                        else if ( (LA38_61=='*'||LA38_61=='?') ) {s = 33;}

                        else s = 43;

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA38_37 = input.LA(1);

                        s = -1;
                        if ( ((LA38_37 >= '\u0000' && LA38_37 <= '\uFFFF')) ) {s = 58;}

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA38_79 = input.LA(1);

                        s = -1;
                        if ( ((LA38_79 >= '0' && LA38_79 <= '9')) ) {s = 80;}

                        else if ( ((LA38_79 >= '\u0000' && LA38_79 <= '\b')||(LA38_79 >= '\u000B' && LA38_79 <= '\f')||(LA38_79 >= '\u000E' && LA38_79 <= '\u001F')||(LA38_79 >= '#' && LA38_79 <= '&')||LA38_79==','||(LA38_79 >= '.' && LA38_79 <= '/')||(LA38_79 >= ';' && LA38_79 <= '>')||(LA38_79 >= '@' && LA38_79 <= 'Z')||(LA38_79 >= '_' && LA38_79 <= 'z')||LA38_79=='|'||(LA38_79 >= '\u007F' && LA38_79 <= '\u2FFF')||(LA38_79 >= '\u3001' && LA38_79 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_79=='\\') ) {s = 30;}

                        else if ( (LA38_79=='-') ) {s = 31;}

                        else if ( (LA38_79=='+') ) {s = 32;}

                        else if ( (LA38_79=='*'||LA38_79=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA38_23 = input.LA(1);

                        s = -1;
                        if ( (LA38_23=='O'||LA38_23=='o') ) {s = 48;}

                        else if ( (LA38_23=='E'||LA38_23=='e') ) {s = 49;}

                        else if ( ((LA38_23 >= '\u0000' && LA38_23 <= '\b')||(LA38_23 >= '\u000B' && LA38_23 <= '\f')||(LA38_23 >= '\u000E' && LA38_23 <= '\u001F')||(LA38_23 >= '#' && LA38_23 <= '&')||LA38_23==','||(LA38_23 >= '.' && LA38_23 <= '9')||(LA38_23 >= ';' && LA38_23 <= '>')||(LA38_23 >= '@' && LA38_23 <= 'D')||(LA38_23 >= 'F' && LA38_23 <= 'N')||(LA38_23 >= 'P' && LA38_23 <= 'Z')||(LA38_23 >= '_' && LA38_23 <= 'd')||(LA38_23 >= 'f' && LA38_23 <= 'n')||(LA38_23 >= 'p' && LA38_23 <= 'z')||LA38_23=='|'||(LA38_23 >= '\u007F' && LA38_23 <= '\u2FFF')||(LA38_23 >= '\u3001' && LA38_23 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_23=='\\') ) {s = 30;}

                        else if ( (LA38_23=='-') ) {s = 31;}

                        else if ( (LA38_23=='+') ) {s = 32;}

                        else if ( (LA38_23=='*'||LA38_23=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA38_73 = input.LA(1);

                        s = -1;
                        if ( ((LA38_73 >= '0' && LA38_73 <= '9')) ) {s = 75;}

                        else if ( ((LA38_73 >= '\u0000' && LA38_73 <= '\b')||(LA38_73 >= '\u000B' && LA38_73 <= '\f')||(LA38_73 >= '\u000E' && LA38_73 <= '\u001F')||(LA38_73 >= '#' && LA38_73 <= '&')||LA38_73==','||(LA38_73 >= '.' && LA38_73 <= '/')||(LA38_73 >= ';' && LA38_73 <= '>')||(LA38_73 >= '@' && LA38_73 <= 'Z')||(LA38_73 >= '_' && LA38_73 <= 'z')||LA38_73=='|'||(LA38_73 >= '\u007F' && LA38_73 <= '\u2FFF')||(LA38_73 >= '\u3001' && LA38_73 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_73=='\\') ) {s = 30;}

                        else if ( (LA38_73=='-') ) {s = 31;}

                        else if ( (LA38_73=='+') ) {s = 32;}

                        else if ( (LA38_73=='*'||LA38_73=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA38_74 = input.LA(1);

                        s = -1;
                        if ( ((LA38_74 >= '.' && LA38_74 <= '/')) ) {s = 71;}

                        else if ( (LA38_74=='-') ) {s = 72;}

                        else if ( ((LA38_74 >= '\u0000' && LA38_74 <= '\b')||(LA38_74 >= '\u000B' && LA38_74 <= '\f')||(LA38_74 >= '\u000E' && LA38_74 <= '\u001F')||(LA38_74 >= '#' && LA38_74 <= '&')||LA38_74==','||(LA38_74 >= '0' && LA38_74 <= '9')||(LA38_74 >= ';' && LA38_74 <= '>')||(LA38_74 >= '@' && LA38_74 <= 'Z')||(LA38_74 >= '_' && LA38_74 <= 'z')||LA38_74=='|'||(LA38_74 >= '\u007F' && LA38_74 <= '\u2FFF')||(LA38_74 >= '\u3001' && LA38_74 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_74=='\\') ) {s = 30;}

                        else if ( (LA38_74=='+') ) {s = 32;}

                        else if ( (LA38_74=='*'||LA38_74=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA38_58 = input.LA(1);

                        s = -1;
                        if ( (LA38_58=='\"') ) {s = 59;}

                        else if ( (LA38_58=='\\') ) {s = 37;}

                        else if ( ((LA38_58 >= '\u0000' && LA38_58 <= '!')||(LA38_58 >= '#' && LA38_58 <= ')')||(LA38_58 >= '+' && LA38_58 <= '>')||(LA38_58 >= '@' && LA38_58 <= '[')||(LA38_58 >= ']' && LA38_58 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA38_58=='*'||LA38_58=='?') ) {s = 39;}

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA38_55 = input.LA(1);

                        s = -1;
                        if ( ((LA38_55 >= '0' && LA38_55 <= '9')) ) {s = 66;}

                        else if ( ((LA38_55 >= '\u0000' && LA38_55 <= '\b')||(LA38_55 >= '\u000B' && LA38_55 <= '\f')||(LA38_55 >= '\u000E' && LA38_55 <= '\u001F')||(LA38_55 >= '#' && LA38_55 <= '&')||LA38_55==','||(LA38_55 >= '.' && LA38_55 <= '/')||(LA38_55 >= ';' && LA38_55 <= '>')||(LA38_55 >= '@' && LA38_55 <= 'Z')||(LA38_55 >= '_' && LA38_55 <= 'z')||LA38_55=='|'||(LA38_55 >= '\u007F' && LA38_55 <= '\u2FFF')||(LA38_55 >= '\u3001' && LA38_55 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_55=='\\') ) {s = 30;}

                        else if ( (LA38_55=='-') ) {s = 31;}

                        else if ( (LA38_55=='+') ) {s = 32;}

                        else if ( (LA38_55=='*'||LA38_55=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA38_76 = input.LA(1);

                        s = -1;
                        if ( ((LA38_76 >= '0' && LA38_76 <= '9')) ) {s = 77;}

                        else if ( ((LA38_76 >= '\u0000' && LA38_76 <= '\b')||(LA38_76 >= '\u000B' && LA38_76 <= '\f')||(LA38_76 >= '\u000E' && LA38_76 <= '\u001F')||(LA38_76 >= '#' && LA38_76 <= '&')||LA38_76==','||(LA38_76 >= '.' && LA38_76 <= '/')||(LA38_76 >= ';' && LA38_76 <= '>')||(LA38_76 >= '@' && LA38_76 <= 'Z')||(LA38_76 >= '_' && LA38_76 <= 'z')||LA38_76=='|'||(LA38_76 >= '\u007F' && LA38_76 <= '\u2FFF')||(LA38_76 >= '\u3001' && LA38_76 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_76=='\\') ) {s = 30;}

                        else if ( (LA38_76=='-') ) {s = 31;}

                        else if ( (LA38_76=='+') ) {s = 32;}

                        else if ( (LA38_76=='*'||LA38_76=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA38_62 = input.LA(1);

                        s = -1;
                        if ( ((LA38_62 >= '\u0000' && LA38_62 <= '\b')||(LA38_62 >= '\u000B' && LA38_62 <= '\f')||(LA38_62 >= '\u000E' && LA38_62 <= '\u001F')||(LA38_62 >= '#' && LA38_62 <= '&')||LA38_62==','||(LA38_62 >= '.' && LA38_62 <= '9')||(LA38_62 >= ';' && LA38_62 <= '>')||(LA38_62 >= '@' && LA38_62 <= 'Z')||(LA38_62 >= '_' && LA38_62 <= 'z')||LA38_62=='|'||(LA38_62 >= '\u007F' && LA38_62 <= '\u2FFF')||(LA38_62 >= '\u3001' && LA38_62 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_62=='\\') ) {s = 30;}

                        else if ( (LA38_62=='-') ) {s = 31;}

                        else if ( (LA38_62=='+') ) {s = 32;}

                        else if ( (LA38_62=='*'||LA38_62=='?') ) {s = 33;}

                        else s = 68;

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA38_44 = input.LA(1);

                        s = -1;
                        if ( ((LA38_44 >= '\u0000' && LA38_44 <= '\b')||(LA38_44 >= '\u000B' && LA38_44 <= '\f')||(LA38_44 >= '\u000E' && LA38_44 <= '\u001F')||(LA38_44 >= '#' && LA38_44 <= '&')||LA38_44==','||(LA38_44 >= '.' && LA38_44 <= '9')||(LA38_44 >= ';' && LA38_44 <= '>')||(LA38_44 >= '@' && LA38_44 <= 'Z')||(LA38_44 >= '_' && LA38_44 <= 'z')||LA38_44=='|'||(LA38_44 >= '\u007F' && LA38_44 <= '\u2FFF')||(LA38_44 >= '\u3001' && LA38_44 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_44=='\\') ) {s = 30;}

                        else if ( (LA38_44=='-') ) {s = 31;}

                        else if ( (LA38_44=='+') ) {s = 32;}

                        else if ( (LA38_44=='*'||LA38_44=='?') ) {s = 33;}

                        else s = 43;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA38_77 = input.LA(1);

                        s = -1;
                        if ( ((LA38_77 >= '0' && LA38_77 <= '9')) ) {s = 79;}

                        else if ( ((LA38_77 >= '\u0000' && LA38_77 <= '\b')||(LA38_77 >= '\u000B' && LA38_77 <= '\f')||(LA38_77 >= '\u000E' && LA38_77 <= '\u001F')||(LA38_77 >= '#' && LA38_77 <= '&')||LA38_77==','||(LA38_77 >= '.' && LA38_77 <= '/')||(LA38_77 >= ';' && LA38_77 <= '>')||(LA38_77 >= '@' && LA38_77 <= 'Z')||(LA38_77 >= '_' && LA38_77 <= 'z')||LA38_77=='|'||(LA38_77 >= '\u007F' && LA38_77 <= '\u2FFF')||(LA38_77 >= '\u3001' && LA38_77 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_77=='\\') ) {s = 30;}

                        else if ( (LA38_77=='-') ) {s = 31;}

                        else if ( (LA38_77=='+') ) {s = 32;}

                        else if ( (LA38_77=='*'||LA38_77=='?') ) {s = 33;}

                        else s = 78;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA38_48 = input.LA(1);

                        s = -1;
                        if ( (LA38_48=='T'||LA38_48=='t') ) {s = 62;}

                        else if ( ((LA38_48 >= '\u0000' && LA38_48 <= '\b')||(LA38_48 >= '\u000B' && LA38_48 <= '\f')||(LA38_48 >= '\u000E' && LA38_48 <= '\u001F')||(LA38_48 >= '#' && LA38_48 <= '&')||LA38_48==','||(LA38_48 >= '.' && LA38_48 <= '9')||(LA38_48 >= ';' && LA38_48 <= '>')||(LA38_48 >= '@' && LA38_48 <= 'S')||(LA38_48 >= 'U' && LA38_48 <= 'Z')||(LA38_48 >= '_' && LA38_48 <= 's')||(LA38_48 >= 'u' && LA38_48 <= 'z')||LA38_48=='|'||(LA38_48 >= '\u007F' && LA38_48 <= '\u2FFF')||(LA38_48 >= '\u3001' && LA38_48 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_48=='\\') ) {s = 30;}

                        else if ( (LA38_48=='-') ) {s = 31;}

                        else if ( (LA38_48=='+') ) {s = 32;}

                        else if ( (LA38_48=='*'||LA38_48=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 27 : 
                        int LA38_72 = input.LA(1);

                        s = -1;
                        if ( ((LA38_72 >= '0' && LA38_72 <= '9')) ) {s = 76;}

                        else if ( ((LA38_72 >= '\u0000' && LA38_72 <= '\b')||(LA38_72 >= '\u000B' && LA38_72 <= '\f')||(LA38_72 >= '\u000E' && LA38_72 <= '\u001F')||(LA38_72 >= '#' && LA38_72 <= '&')||LA38_72==','||(LA38_72 >= '.' && LA38_72 <= '/')||(LA38_72 >= ';' && LA38_72 <= '>')||(LA38_72 >= '@' && LA38_72 <= 'Z')||(LA38_72 >= '_' && LA38_72 <= 'z')||LA38_72=='|'||(LA38_72 >= '\u007F' && LA38_72 <= '\u2FFF')||(LA38_72 >= '\u3001' && LA38_72 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_72=='\\') ) {s = 30;}

                        else if ( (LA38_72=='-') ) {s = 31;}

                        else if ( (LA38_72=='+') ) {s = 32;}

                        else if ( (LA38_72=='*'||LA38_72=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 28 : 
                        int LA38_26 = input.LA(1);

                        s = -1;
                        if ( ((LA38_26 >= '\u0000' && LA38_26 <= '\b')||(LA38_26 >= '\u000B' && LA38_26 <= '\f')||(LA38_26 >= '\u000E' && LA38_26 <= '\u001F')||(LA38_26 >= '#' && LA38_26 <= '&')||LA38_26==','||(LA38_26 >= '.' && LA38_26 <= '9')||(LA38_26 >= ';' && LA38_26 <= '>')||(LA38_26 >= '@' && LA38_26 <= 'Z')||(LA38_26 >= '_' && LA38_26 <= 'z')||LA38_26=='|'||(LA38_26 >= '\u007F' && LA38_26 <= '\u2FFF')||(LA38_26 >= '\u3001' && LA38_26 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_26=='\\') ) {s = 30;}

                        else if ( (LA38_26=='-') ) {s = 31;}

                        else if ( (LA38_26=='+') ) {s = 32;}

                        else if ( (LA38_26=='*'||LA38_26=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 29 : 
                        int LA38_69 = input.LA(1);

                        s = -1;
                        if ( ((LA38_69 >= '\u0000' && LA38_69 <= '\b')||(LA38_69 >= '\u000B' && LA38_69 <= '\f')||(LA38_69 >= '\u000E' && LA38_69 <= '\u001F')||(LA38_69 >= '#' && LA38_69 <= '&')||LA38_69==','||(LA38_69 >= '.' && LA38_69 <= '9')||(LA38_69 >= ';' && LA38_69 <= '>')||(LA38_69 >= '@' && LA38_69 <= 'Z')||(LA38_69 >= '_' && LA38_69 <= 'z')||LA38_69=='|'||(LA38_69 >= '\u007F' && LA38_69 <= '\u2FFF')||(LA38_69 >= '\u3001' && LA38_69 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_69=='\\') ) {s = 30;}

                        else if ( (LA38_69=='-') ) {s = 31;}

                        else if ( (LA38_69=='+') ) {s = 32;}

                        else if ( (LA38_69=='*'||LA38_69=='?') ) {s = 33;}

                        else s = 50;

                        if ( s>=0 ) return s;
                        break;

                    case 30 : 
                        int LA38_63 = input.LA(1);

                        s = -1;
                        if ( (LA38_63=='R'||LA38_63=='r') ) {s = 69;}

                        else if ( ((LA38_63 >= '\u0000' && LA38_63 <= '\b')||(LA38_63 >= '\u000B' && LA38_63 <= '\f')||(LA38_63 >= '\u000E' && LA38_63 <= '\u001F')||(LA38_63 >= '#' && LA38_63 <= '&')||LA38_63==','||(LA38_63 >= '.' && LA38_63 <= '9')||(LA38_63 >= ';' && LA38_63 <= '>')||(LA38_63 >= '@' && LA38_63 <= 'Q')||(LA38_63 >= 'S' && LA38_63 <= 'Z')||(LA38_63 >= '_' && LA38_63 <= 'q')||(LA38_63 >= 's' && LA38_63 <= 'z')||LA38_63=='|'||(LA38_63 >= '\u007F' && LA38_63 <= '\u2FFF')||(LA38_63 >= '\u3001' && LA38_63 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_63=='\\') ) {s = 30;}

                        else if ( (LA38_63=='-') ) {s = 31;}

                        else if ( (LA38_63=='+') ) {s = 32;}

                        else if ( (LA38_63=='*'||LA38_63=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 31 : 
                        int LA38_56 = input.LA(1);

                        s = -1;
                        if ( ((LA38_56 >= '\u0000' && LA38_56 <= '\b')||(LA38_56 >= '\u000B' && LA38_56 <= '\f')||(LA38_56 >= '\u000E' && LA38_56 <= '\u001F')||(LA38_56 >= '#' && LA38_56 <= '&')||LA38_56==','||(LA38_56 >= '.' && LA38_56 <= '9')||(LA38_56 >= ';' && LA38_56 <= '>')||(LA38_56 >= '@' && LA38_56 <= 'Z')||(LA38_56 >= '_' && LA38_56 <= 'z')||LA38_56=='|'||(LA38_56 >= '\u007F' && LA38_56 <= '\u2FFF')||(LA38_56 >= '\u3001' && LA38_56 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_56=='\\') ) {s = 30;}

                        else if ( (LA38_56=='-') ) {s = 31;}

                        else if ( (LA38_56=='+') ) {s = 32;}

                        else if ( (LA38_56=='*'||LA38_56=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 32 : 
                        int LA38_49 = input.LA(1);

                        s = -1;
                        if ( (LA38_49=='A'||LA38_49=='a') ) {s = 63;}

                        else if ( ((LA38_49 >= '\u0000' && LA38_49 <= '\b')||(LA38_49 >= '\u000B' && LA38_49 <= '\f')||(LA38_49 >= '\u000E' && LA38_49 <= '\u001F')||(LA38_49 >= '#' && LA38_49 <= '&')||LA38_49==','||(LA38_49 >= '.' && LA38_49 <= '9')||(LA38_49 >= ';' && LA38_49 <= '>')||LA38_49=='@'||(LA38_49 >= 'B' && LA38_49 <= 'Z')||(LA38_49 >= '_' && LA38_49 <= '`')||(LA38_49 >= 'b' && LA38_49 <= 'z')||LA38_49=='|'||(LA38_49 >= '\u007F' && LA38_49 <= '\u2FFF')||(LA38_49 >= '\u3001' && LA38_49 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_49=='\\') ) {s = 30;}

                        else if ( (LA38_49=='-') ) {s = 31;}

                        else if ( (LA38_49=='+') ) {s = 32;}

                        else if ( (LA38_49=='*'||LA38_49=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 33 : 
                        int LA38_71 = input.LA(1);

                        s = -1;
                        if ( ((LA38_71 >= '0' && LA38_71 <= '9')) ) {s = 76;}

                        else if ( ((LA38_71 >= '\u0000' && LA38_71 <= '\b')||(LA38_71 >= '\u000B' && LA38_71 <= '\f')||(LA38_71 >= '\u000E' && LA38_71 <= '\u001F')||(LA38_71 >= '#' && LA38_71 <= '&')||LA38_71==','||(LA38_71 >= '.' && LA38_71 <= '/')||(LA38_71 >= ';' && LA38_71 <= '>')||(LA38_71 >= '@' && LA38_71 <= 'Z')||(LA38_71 >= '_' && LA38_71 <= 'z')||LA38_71=='|'||(LA38_71 >= '\u007F' && LA38_71 <= '\u2FFF')||(LA38_71 >= '\u3001' && LA38_71 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_71=='\\') ) {s = 30;}

                        else if ( (LA38_71=='-') ) {s = 31;}

                        else if ( (LA38_71=='+') ) {s = 32;}

                        else if ( (LA38_71=='*'||LA38_71=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 34 : 
                        int LA38_66 = input.LA(1);

                        s = -1;
                        if ( ((LA38_66 >= '0' && LA38_66 <= '9')) ) {s = 74;}

                        else if ( ((LA38_66 >= '.' && LA38_66 <= '/')) ) {s = 71;}

                        else if ( (LA38_66=='-') ) {s = 72;}

                        else if ( ((LA38_66 >= '\u0000' && LA38_66 <= '\b')||(LA38_66 >= '\u000B' && LA38_66 <= '\f')||(LA38_66 >= '\u000E' && LA38_66 <= '\u001F')||(LA38_66 >= '#' && LA38_66 <= '&')||LA38_66==','||(LA38_66 >= ';' && LA38_66 <= '>')||(LA38_66 >= '@' && LA38_66 <= 'Z')||(LA38_66 >= '_' && LA38_66 <= 'z')||LA38_66=='|'||(LA38_66 >= '\u007F' && LA38_66 <= '\u2FFF')||(LA38_66 >= '\u3001' && LA38_66 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_66=='\\') ) {s = 30;}

                        else if ( (LA38_66=='+') ) {s = 32;}

                        else if ( (LA38_66=='*'||LA38_66=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 35 : 
                        int LA38_53 = input.LA(1);

                        s = -1;
                        if ( (LA38_53=='.') ) {s = 51;}

                        else if ( ((LA38_53 >= '0' && LA38_53 <= '9')) ) {s = 65;}

                        else if ( (LA38_53=='/') ) {s = 54;}

                        else if ( (LA38_53=='-') ) {s = 55;}

                        else if ( ((LA38_53 >= '\u0000' && LA38_53 <= '\b')||(LA38_53 >= '\u000B' && LA38_53 <= '\f')||(LA38_53 >= '\u000E' && LA38_53 <= '\u001F')||(LA38_53 >= '#' && LA38_53 <= '&')||LA38_53==','||(LA38_53 >= ';' && LA38_53 <= '>')||(LA38_53 >= '@' && LA38_53 <= 'Z')||(LA38_53 >= '_' && LA38_53 <= 'z')||LA38_53=='|'||(LA38_53 >= '\u007F' && LA38_53 <= '\u2FFF')||(LA38_53 >= '\u3001' && LA38_53 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_53=='\\') ) {s = 30;}

                        else if ( (LA38_53=='+') ) {s = 32;}

                        else if ( (LA38_53=='*'||LA38_53=='?') ) {s = 33;}

                        else s = 52;

                        if ( s>=0 ) return s;
                        break;

                    case 36 : 
                        int LA38_32 = input.LA(1);

                        s = -1;
                        if ( ((LA38_32 >= '\u0000' && LA38_32 <= '\b')||(LA38_32 >= '\u000B' && LA38_32 <= '\f')||(LA38_32 >= '\u000E' && LA38_32 <= '\u001F')||(LA38_32 >= '#' && LA38_32 <= '&')||LA38_32==','||(LA38_32 >= '.' && LA38_32 <= '9')||(LA38_32 >= ';' && LA38_32 <= '>')||(LA38_32 >= '@' && LA38_32 <= 'Z')||(LA38_32 >= '_' && LA38_32 <= 'z')||LA38_32=='|'||(LA38_32 >= '\u007F' && LA38_32 <= '\u2FFF')||(LA38_32 >= '\u3001' && LA38_32 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_32=='\\') ) {s = 30;}

                        else if ( (LA38_32=='-') ) {s = 31;}

                        else if ( (LA38_32=='+') ) {s = 32;}

                        else if ( (LA38_32=='*'||LA38_32=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 37 : 
                        int LA38_57 = input.LA(1);

                        s = -1;
                        if ( ((LA38_57 >= '\u0000' && LA38_57 <= '\b')||(LA38_57 >= '\u000B' && LA38_57 <= '\f')||(LA38_57 >= '\u000E' && LA38_57 <= '\u001F')||(LA38_57 >= '#' && LA38_57 <= '&')||LA38_57==','||(LA38_57 >= '.' && LA38_57 <= '9')||(LA38_57 >= ';' && LA38_57 <= '>')||(LA38_57 >= '@' && LA38_57 <= 'Z')||(LA38_57 >= '_' && LA38_57 <= 'z')||LA38_57=='|'||(LA38_57 >= '\u007F' && LA38_57 <= '\u2FFF')||(LA38_57 >= '\u3001' && LA38_57 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_57=='\\') ) {s = 30;}

                        else if ( (LA38_57=='-') ) {s = 31;}

                        else if ( (LA38_57=='+') ) {s = 32;}

                        else if ( (LA38_57=='*'||LA38_57=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 38 : 
                        int LA38_31 = input.LA(1);

                        s = -1;
                        if ( ((LA38_31 >= '\u0000' && LA38_31 <= '\b')||(LA38_31 >= '\u000B' && LA38_31 <= '\f')||(LA38_31 >= '\u000E' && LA38_31 <= '\u001F')||(LA38_31 >= '#' && LA38_31 <= '&')||LA38_31==','||(LA38_31 >= '.' && LA38_31 <= '9')||(LA38_31 >= ';' && LA38_31 <= '>')||(LA38_31 >= '@' && LA38_31 <= 'Z')||(LA38_31 >= '_' && LA38_31 <= 'z')||LA38_31=='|'||(LA38_31 >= '\u007F' && LA38_31 <= '\u2FFF')||(LA38_31 >= '\u3001' && LA38_31 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_31=='\\') ) {s = 30;}

                        else if ( (LA38_31=='-') ) {s = 31;}

                        else if ( (LA38_31=='+') ) {s = 32;}

                        else if ( (LA38_31=='*'||LA38_31=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 39 : 
                        int LA38_42 = input.LA(1);

                        s = -1;
                        if ( (LA38_42=='D'||LA38_42=='d') ) {s = 61;}

                        else if ( ((LA38_42 >= '\u0000' && LA38_42 <= '\b')||(LA38_42 >= '\u000B' && LA38_42 <= '\f')||(LA38_42 >= '\u000E' && LA38_42 <= '\u001F')||(LA38_42 >= '#' && LA38_42 <= '&')||LA38_42==','||(LA38_42 >= '.' && LA38_42 <= '9')||(LA38_42 >= ';' && LA38_42 <= '>')||(LA38_42 >= '@' && LA38_42 <= 'C')||(LA38_42 >= 'E' && LA38_42 <= 'Z')||(LA38_42 >= '_' && LA38_42 <= 'c')||(LA38_42 >= 'e' && LA38_42 <= 'z')||LA38_42=='|'||(LA38_42 >= '\u007F' && LA38_42 <= '\u2FFF')||(LA38_42 >= '\u3001' && LA38_42 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_42=='\\') ) {s = 30;}

                        else if ( (LA38_42=='-') ) {s = 31;}

                        else if ( (LA38_42=='+') ) {s = 32;}

                        else if ( (LA38_42=='*'||LA38_42=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 40 : 
                        int LA38_19 = input.LA(1);

                        s = -1;
                        if ( (LA38_19=='&') ) {s = 44;}

                        else if ( ((LA38_19 >= '\u0000' && LA38_19 <= '\b')||(LA38_19 >= '\u000B' && LA38_19 <= '\f')||(LA38_19 >= '\u000E' && LA38_19 <= '\u001F')||(LA38_19 >= '#' && LA38_19 <= '%')||LA38_19==','||(LA38_19 >= '.' && LA38_19 <= '9')||(LA38_19 >= ';' && LA38_19 <= '>')||(LA38_19 >= '@' && LA38_19 <= 'Z')||(LA38_19 >= '_' && LA38_19 <= 'z')||LA38_19=='|'||(LA38_19 >= '\u007F' && LA38_19 <= '\u2FFF')||(LA38_19 >= '\u3001' && LA38_19 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_19=='\\') ) {s = 30;}

                        else if ( (LA38_19=='-') ) {s = 31;}

                        else if ( (LA38_19=='+') ) {s = 32;}

                        else if ( (LA38_19=='*'||LA38_19=='?') ) {s = 33;}

                        else s = 43;

                        if ( s>=0 ) return s;
                        break;

                    case 41 : 
                        int LA38_21 = input.LA(1);

                        s = -1;
                        if ( (LA38_21=='|') ) {s = 47;}

                        else if ( ((LA38_21 >= '\u0000' && LA38_21 <= '\b')||(LA38_21 >= '\u000B' && LA38_21 <= '\f')||(LA38_21 >= '\u000E' && LA38_21 <= '\u001F')||(LA38_21 >= '#' && LA38_21 <= '&')||LA38_21==','||(LA38_21 >= '.' && LA38_21 <= '9')||(LA38_21 >= ';' && LA38_21 <= '>')||(LA38_21 >= '@' && LA38_21 <= 'Z')||(LA38_21 >= '_' && LA38_21 <= 'z')||(LA38_21 >= '\u007F' && LA38_21 <= '\u2FFF')||(LA38_21 >= '\u3001' && LA38_21 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_21=='\\') ) {s = 30;}

                        else if ( (LA38_21=='-') ) {s = 31;}

                        else if ( (LA38_21=='+') ) {s = 32;}

                        else if ( (LA38_21=='*'||LA38_21=='?') ) {s = 33;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 42 : 
                        int LA38_80 = input.LA(1);

                        s = -1;
                        if ( ((LA38_80 >= '\u0000' && LA38_80 <= '\b')||(LA38_80 >= '\u000B' && LA38_80 <= '\f')||(LA38_80 >= '\u000E' && LA38_80 <= '\u001F')||(LA38_80 >= '#' && LA38_80 <= '&')||LA38_80==','||(LA38_80 >= '.' && LA38_80 <= '9')||(LA38_80 >= ';' && LA38_80 <= '>')||(LA38_80 >= '@' && LA38_80 <= 'Z')||(LA38_80 >= '_' && LA38_80 <= 'z')||LA38_80=='|'||(LA38_80 >= '\u007F' && LA38_80 <= '\u2FFF')||(LA38_80 >= '\u3001' && LA38_80 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_80=='\\') ) {s = 30;}

                        else if ( (LA38_80=='-') ) {s = 31;}

                        else if ( (LA38_80=='+') ) {s = 32;}

                        else if ( (LA38_80=='*'||LA38_80=='?') ) {s = 33;}

                        else s = 78;

                        if ( s>=0 ) return s;
                        break;

                    case 43 : 
                        int LA38_75 = input.LA(1);

                        s = -1;
                        if ( ((LA38_75 >= '0' && LA38_75 <= '9')) ) {s = 75;}

                        else if ( ((LA38_75 >= '\u0000' && LA38_75 <= '\b')||(LA38_75 >= '\u000B' && LA38_75 <= '\f')||(LA38_75 >= '\u000E' && LA38_75 <= '\u001F')||(LA38_75 >= '#' && LA38_75 <= '&')||LA38_75==','||(LA38_75 >= '.' && LA38_75 <= '/')||(LA38_75 >= ';' && LA38_75 <= '>')||(LA38_75 >= '@' && LA38_75 <= 'Z')||(LA38_75 >= '_' && LA38_75 <= 'z')||LA38_75=='|'||(LA38_75 >= '\u007F' && LA38_75 <= '\u2FFF')||(LA38_75 >= '\u3001' && LA38_75 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_75=='\\') ) {s = 30;}

                        else if ( (LA38_75=='-') ) {s = 31;}

                        else if ( (LA38_75=='+') ) {s = 32;}

                        else if ( (LA38_75=='*'||LA38_75=='?') ) {s = 33;}

                        else s = 52;

                        if ( s>=0 ) return s;
                        break;

                    case 44 : 
                        int LA38_70 = input.LA(1);

                        s = -1;
                        if ( ((LA38_70 >= '0' && LA38_70 <= '9')) ) {s = 75;}

                        else if ( ((LA38_70 >= '.' && LA38_70 <= '/')) ) {s = 71;}

                        else if ( (LA38_70=='-') ) {s = 72;}

                        else if ( ((LA38_70 >= '\u0000' && LA38_70 <= '\b')||(LA38_70 >= '\u000B' && LA38_70 <= '\f')||(LA38_70 >= '\u000E' && LA38_70 <= '\u001F')||(LA38_70 >= '#' && LA38_70 <= '&')||LA38_70==','||(LA38_70 >= ';' && LA38_70 <= '>')||(LA38_70 >= '@' && LA38_70 <= 'Z')||(LA38_70 >= '_' && LA38_70 <= 'z')||LA38_70=='|'||(LA38_70 >= '\u007F' && LA38_70 <= '\u2FFF')||(LA38_70 >= '\u3001' && LA38_70 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_70=='\\') ) {s = 30;}

                        else if ( (LA38_70=='+') ) {s = 32;}

                        else if ( (LA38_70=='*'||LA38_70=='?') ) {s = 33;}

                        else s = 52;

                        if ( s>=0 ) return s;
                        break;

                    case 45 : 
                        int LA38_9 = input.LA(1);

                        s = -1;
                        if ( ((LA38_9 >= '\u0000' && LA38_9 <= '\b')||(LA38_9 >= '\u000B' && LA38_9 <= '\f')||(LA38_9 >= '\u000E' && LA38_9 <= '\u001F')||(LA38_9 >= '#' && LA38_9 <= '&')||(LA38_9 >= '+' && LA38_9 <= '9')||(LA38_9 >= ';' && LA38_9 <= '>')||(LA38_9 >= '@' && LA38_9 <= 'Z')||LA38_9=='\\'||(LA38_9 >= '_' && LA38_9 <= 'z')||LA38_9=='|'||(LA38_9 >= '\u007F' && LA38_9 <= '\u2FFF')||(LA38_9 >= '\u3001' && LA38_9 <= '\uFFFF')) ) {s = 33;}

                        else s = 34;

                        if ( s>=0 ) return s;
                        break;

                    case 46 : 
                        int LA38_15 = input.LA(1);

                        s = -1;
                        if ( (LA38_15=='\\') ) {s = 37;}

                        else if ( ((LA38_15 >= '\u0000' && LA38_15 <= '!')||(LA38_15 >= '#' && LA38_15 <= ')')||(LA38_15 >= '+' && LA38_15 <= '>')||(LA38_15 >= '@' && LA38_15 <= '[')||(LA38_15 >= ']' && LA38_15 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA38_15=='*'||LA38_15=='?') ) {s = 39;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 47 : 
                        int LA38_40 = input.LA(1);

                        s = -1;
                        if ( ((LA38_40 >= '\u0000' && LA38_40 <= '\b')||(LA38_40 >= '\u000B' && LA38_40 <= '\f')||(LA38_40 >= '\u000E' && LA38_40 <= '\u001F')||(LA38_40 >= '#' && LA38_40 <= '&')||LA38_40==','||(LA38_40 >= '.' && LA38_40 <= '9')||(LA38_40 >= ';' && LA38_40 <= '>')||(LA38_40 >= '@' && LA38_40 <= 'Z')||(LA38_40 >= '_' && LA38_40 <= 'z')||LA38_40=='|'||(LA38_40 >= '\u007F' && LA38_40 <= '\u2FFF')||(LA38_40 >= '\u3001' && LA38_40 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_40=='\\') ) {s = 30;}

                        else if ( (LA38_40=='-') ) {s = 31;}

                        else if ( (LA38_40=='+') ) {s = 32;}

                        else if ( (LA38_40=='*'||LA38_40=='?') ) {s = 33;}

                        else s = 60;

                        if ( s>=0 ) return s;
                        break;

                    case 48 : 
                        int LA38_47 = input.LA(1);

                        s = -1;
                        if ( ((LA38_47 >= '\u0000' && LA38_47 <= '\b')||(LA38_47 >= '\u000B' && LA38_47 <= '\f')||(LA38_47 >= '\u000E' && LA38_47 <= '\u001F')||(LA38_47 >= '#' && LA38_47 <= '&')||LA38_47==','||(LA38_47 >= '.' && LA38_47 <= '9')||(LA38_47 >= ';' && LA38_47 <= '>')||(LA38_47 >= '@' && LA38_47 <= 'Z')||(LA38_47 >= '_' && LA38_47 <= 'z')||LA38_47=='|'||(LA38_47 >= '\u007F' && LA38_47 <= '\u2FFF')||(LA38_47 >= '\u3001' && LA38_47 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_47=='\\') ) {s = 30;}

                        else if ( (LA38_47=='-') ) {s = 31;}

                        else if ( (LA38_47=='+') ) {s = 32;}

                        else if ( (LA38_47=='*'||LA38_47=='?') ) {s = 33;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 49 : 
                        int LA38_51 = input.LA(1);

                        s = -1;
                        if ( ((LA38_51 >= '0' && LA38_51 <= '9')) ) {s = 64;}

                        else if ( ((LA38_51 >= '\u0000' && LA38_51 <= '\b')||(LA38_51 >= '\u000B' && LA38_51 <= '\f')||(LA38_51 >= '\u000E' && LA38_51 <= '\u001F')||(LA38_51 >= '#' && LA38_51 <= '&')||LA38_51==','||(LA38_51 >= '.' && LA38_51 <= '/')||(LA38_51 >= ';' && LA38_51 <= '>')||(LA38_51 >= '@' && LA38_51 <= 'Z')||(LA38_51 >= '_' && LA38_51 <= 'z')||LA38_51=='|'||(LA38_51 >= '\u007F' && LA38_51 <= '\u2FFF')||(LA38_51 >= '\u3001' && LA38_51 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_51=='\\') ) {s = 30;}

                        else if ( (LA38_51=='-') ) {s = 31;}

                        else if ( (LA38_51=='+') ) {s = 32;}

                        else if ( (LA38_51=='*'||LA38_51=='?') ) {s = 33;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;

                    case 50 : 
                        int LA38_22 = input.LA(1);

                        s = -1;
                        if ( (LA38_22=='O'||LA38_22=='o') ) {s = 48;}

                        else if ( (LA38_22=='E'||LA38_22=='e') ) {s = 49;}

                        else if ( ((LA38_22 >= '\u0000' && LA38_22 <= '\b')||(LA38_22 >= '\u000B' && LA38_22 <= '\f')||(LA38_22 >= '\u000E' && LA38_22 <= '\u001F')||(LA38_22 >= '#' && LA38_22 <= '&')||LA38_22==','||(LA38_22 >= '.' && LA38_22 <= '9')||(LA38_22 >= ';' && LA38_22 <= '>')||(LA38_22 >= '@' && LA38_22 <= 'D')||(LA38_22 >= 'F' && LA38_22 <= 'N')||(LA38_22 >= 'P' && LA38_22 <= 'Z')||(LA38_22 >= '_' && LA38_22 <= 'd')||(LA38_22 >= 'f' && LA38_22 <= 'n')||(LA38_22 >= 'p' && LA38_22 <= 'z')||LA38_22=='|'||(LA38_22 >= '\u007F' && LA38_22 <= '\u2FFF')||(LA38_22 >= '\u3001' && LA38_22 <= '\uFFFF')) ) {s = 29;}

                        else if ( (LA38_22=='\\') ) {s = 30;}

                        else if ( (LA38_22=='-') ) {s = 31;}

                        else if ( (LA38_22=='+') ) {s = 32;}

                        else if ( (LA38_22=='*'||LA38_22=='?') ) {s = 33;}

                        else s = 50;

                        if ( s>=0 ) return s;
                        break;

                    case 51 : 
                        int LA38_10 = input.LA(1);

                        s = -1;
                        if ( (LA38_10=='?') ) {s = 10;}

                        else if ( ((LA38_10 >= '\u0000' && LA38_10 <= '\b')||(LA38_10 >= '\u000B' && LA38_10 <= '\f')||(LA38_10 >= '\u000E' && LA38_10 <= '\u001F')||(LA38_10 >= '#' && LA38_10 <= '&')||(LA38_10 >= '+' && LA38_10 <= '9')||(LA38_10 >= ';' && LA38_10 <= '>')||(LA38_10 >= '@' && LA38_10 <= 'Z')||LA38_10=='\\'||(LA38_10 >= '_' && LA38_10 <= 'z')||LA38_10=='|'||(LA38_10 >= '\u007F' && LA38_10 <= '\u2FFF')||(LA38_10 >= '\u3001' && LA38_10 <= '\uFFFF')) ) {s = 33;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 38, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}