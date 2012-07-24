// $ANTLR 3.4 /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g 2012-07-23 12:41:10

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class FixInvenioLexer extends Lexer {
    public static final int EOF=-1;
    public static final int AMBIGUITY=4;
    public static final int DQUOTE=5;
    public static final int ESC_CHAR=6;
    public static final int INT=7;
    public static final int LPAREN=8;
    public static final int PHRASE=9;
    public static final int QPHRASE=10;
    public static final int QREGEX=11;
    public static final int REGEX=12;
    public static final int RPAREN=13;
    public static final int SAFE_TOKEN=14;
    public static final int SLASH=15;
    public static final int SPACE=16;
    public static final int SQUOTE=17;
    public static final int SUB_SUS=18;
    public static final int SUSPICIOUS_TOKEN=19;
    public static final int TOKEN=20;
    public static final int WS=21;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public FixInvenioLexer() {} 
    public FixInvenioLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public FixInvenioLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g"; }

    // $ANTLR start "SLASH"
    public final void mSLASH() throws RecognitionException {
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:112:18: ( '/' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:112:20: '/'
            {
            match('/'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SLASH"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:114:9: ( '(' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:114:11: '('
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:116:9: ( ')' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:116:11: ')'
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

    // $ANTLR start "DQUOTE"
    public final void mDQUOTE() throws RecognitionException {
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:121:2: ( '\\\"' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:121:4: '\\\"'
            {
            match('\"'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DQUOTE"

    // $ANTLR start "SQUOTE"
    public final void mSQUOTE() throws RecognitionException {
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:124:2: ( '\\'' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:124:4: '\\''
            {
            match('\''); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SQUOTE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:127:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )+ )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:127:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )+
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:127:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '\t' && LA1_0 <= '\n')||LA1_0=='\r'||LA1_0==' '||LA1_0=='\u3000') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:
            	    {
            	    if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' '||input.LA(1)=='\u3000' ) {
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
    // $ANTLR end "WS"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:135:13: ( '0' .. '9' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:138:18: ( '\\\\' . )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:138:21: '\\\\' .
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

    // $ANTLR start "PHRASE"
    public final void mPHRASE() throws RecognitionException {
        try {
            int _type = PHRASE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:142:8: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE | SQUOTE ( ESC_CHAR |~ ( '\\'' | '\\\\' | '\\\\\\'' ) )+ SQUOTE )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\"') ) {
                alt4=1;
            }
            else if ( (LA4_0=='\'') ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:143:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+ DQUOTE
                    {
                    mDQUOTE(); 


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:143:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' ) )+
                    int cnt2=0;
                    loop2:
                    do {
                        int alt2=3;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0=='\\') ) {
                            alt2=1;
                        }
                        else if ( ((LA2_0 >= '\u0000' && LA2_0 <= '!')||(LA2_0 >= '#' && LA2_0 <= '[')||(LA2_0 >= ']' && LA2_0 <= '\uFFFF')) ) {
                            alt2=2;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:143:10: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:143:19: ~ ( '\\\"' | '\\\\' )
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
                    	    if ( cnt2 >= 1 ) break loop2;
                                EarlyExitException eee =
                                    new EarlyExitException(2, input);
                                throw eee;
                        }
                        cnt2++;
                    } while (true);


                    mDQUOTE(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:144:4: SQUOTE ( ESC_CHAR |~ ( '\\'' | '\\\\' | '\\\\\\'' ) )+ SQUOTE
                    {
                    mSQUOTE(); 


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:144:11: ( ESC_CHAR |~ ( '\\'' | '\\\\' | '\\\\\\'' ) )+
                    int cnt3=0;
                    loop3:
                    do {
                        int alt3=3;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0=='\\') ) {
                            alt3=1;
                        }
                        else if ( ((LA3_0 >= '\u0000' && LA3_0 <= '&')||(LA3_0 >= '(' && LA3_0 <= '[')||(LA3_0 >= ']' && LA3_0 <= '\uFFFF')) ) {
                            alt3=2;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:144:12: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:144:21: ~ ( '\\'' | '\\\\' | '\\\\\\'' )
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
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


                    mSQUOTE(); 


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
    // $ANTLR end "PHRASE"

    // $ANTLR start "REGEX"
    public final void mREGEX() throws RecognitionException {
        try {
            int _type = REGEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:148:2: ( SLASH ( ESC_CHAR |~ ( '\\\\' | '\\\\/' ) )+ SLASH )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:149:2: SLASH ( ESC_CHAR |~ ( '\\\\' | '\\\\/' ) )+ SLASH
            {
            mSLASH(); 


            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:149:8: ( ESC_CHAR |~ ( '\\\\' | '\\\\/' ) )+
            int cnt5=0;
            loop5:
            do {
                int alt5=3;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='/') ) {
                    int LA5_1 = input.LA(2);

                    if ( ((LA5_1 >= '\u0000' && LA5_1 <= '\uFFFF')) ) {
                        alt5=2;
                    }


                }
                else if ( (LA5_0=='\\') ) {
                    alt5=1;
                }
                else if ( ((LA5_0 >= '\u0000' && LA5_0 <= '.')||(LA5_0 >= '0' && LA5_0 <= '[')||(LA5_0 >= ']' && LA5_0 <= '\uFFFF')) ) {
                    alt5=2;
                }


                switch (alt5) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:149:9: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:149:18: ~ ( '\\\\' | '\\\\/' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
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


            mSLASH(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "REGEX"

    // $ANTLR start "SAFE_TOKEN"
    public final void mSAFE_TOKEN() throws RecognitionException {
        try {
            int _type = SAFE_TOKEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:153:2: ( (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '\\\\' | '/' | ')' | '(' ) | ESC_CHAR )+ )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:153:3: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '\\\\' | '/' | ')' | '(' ) | ESC_CHAR )+
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:153:3: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '\\\\' | '/' | ')' | '(' ) | ESC_CHAR )+
            int cnt6=0;
            loop6:
            do {
                int alt6=3;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0 >= '\u0000' && LA6_0 <= '\b')||(LA6_0 >= '\u000B' && LA6_0 <= '\f')||(LA6_0 >= '\u000E' && LA6_0 <= '\u001F')||LA6_0=='!'||(LA6_0 >= '#' && LA6_0 <= '&')||(LA6_0 >= '*' && LA6_0 <= '.')||(LA6_0 >= '0' && LA6_0 <= '[')||(LA6_0 >= ']' && LA6_0 <= '\u2FFF')||(LA6_0 >= '\u3001' && LA6_0 <= '\uFFFF')) ) {
                    alt6=1;
                }
                else if ( (LA6_0=='\\') ) {
                    alt6=2;
                }


                switch (alt6) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:153:4: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '\\\\' | '/' | ')' | '(' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\u001F')||input.LA(1)=='!'||(input.LA(1) >= '#' && input.LA(1) <= '&')||(input.LA(1) >= '*' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\u2FFF')||(input.LA(1) >= '\u3001' && input.LA(1) <= '\uFFFF') ) {
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
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:156:5: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SAFE_TOKEN"

    // $ANTLR start "SUSPICIOUS_TOKEN"
    public final void mSUSPICIOUS_TOKEN() throws RecognitionException {
        try {
            int _type = SUSPICIOUS_TOKEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:159:2: ( ( SAFE_TOKEN LPAREN ( SUB_SUS )? RPAREN )+ ( SUB_SUS )? )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:160:2: ( SAFE_TOKEN LPAREN ( SUB_SUS )? RPAREN )+ ( SUB_SUS )?
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:160:2: ( SAFE_TOKEN LPAREN ( SUB_SUS )? RPAREN )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                alt8 = dfa8.predict(input);
                switch (alt8) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:160:3: SAFE_TOKEN LPAREN ( SUB_SUS )? RPAREN
            	    {
            	    mSAFE_TOKEN(); 


            	    mLPAREN(); 


            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:160:21: ( SUB_SUS )?
            	    int alt7=2;
            	    int LA7_0 = input.LA(1);

            	    if ( ((LA7_0 >= '\u0000' && LA7_0 <= '\b')||(LA7_0 >= '\u000B' && LA7_0 <= '\f')||(LA7_0 >= '\u000E' && LA7_0 <= '\u001F')||LA7_0=='!'||(LA7_0 >= '#' && LA7_0 <= '&')||LA7_0=='('||(LA7_0 >= '*' && LA7_0 <= '.')||(LA7_0 >= '0' && LA7_0 <= '\u2FFF')||(LA7_0 >= '\u3001' && LA7_0 <= '\uFFFF')) ) {
            	        alt7=1;
            	    }
            	    switch (alt7) {
            	        case 1 :
            	            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:160:21: SUB_SUS
            	            {
            	            mSUB_SUS(); 


            	            }
            	            break;

            	    }


            	    mRPAREN(); 


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:160:39: ( SUB_SUS )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( ((LA9_0 >= '\u0000' && LA9_0 <= '\b')||(LA9_0 >= '\u000B' && LA9_0 <= '\f')||(LA9_0 >= '\u000E' && LA9_0 <= '\u001F')||LA9_0=='!'||(LA9_0 >= '#' && LA9_0 <= '&')||LA9_0=='('||(LA9_0 >= '*' && LA9_0 <= '.')||(LA9_0 >= '0' && LA9_0 <= '\u2FFF')||(LA9_0 >= '\u3001' && LA9_0 <= '\uFFFF')) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:160:39: SUB_SUS
                    {
                    mSUB_SUS(); 


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
    // $ANTLR end "SUSPICIOUS_TOKEN"

    // $ANTLR start "SUB_SUS"
    public final void mSUB_SUS() throws RecognitionException {
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:166:2: ( LPAREN SUB_SUS RPAREN | SAFE_TOKEN )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='(') ) {
                alt10=1;
            }
            else if ( ((LA10_0 >= '\u0000' && LA10_0 <= '\b')||(LA10_0 >= '\u000B' && LA10_0 <= '\f')||(LA10_0 >= '\u000E' && LA10_0 <= '\u001F')||LA10_0=='!'||(LA10_0 >= '#' && LA10_0 <= '&')||(LA10_0 >= '*' && LA10_0 <= '.')||(LA10_0 >= '0' && LA10_0 <= '\u2FFF')||(LA10_0 >= '\u3001' && LA10_0 <= '\uFFFF')) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:167:2: LPAREN SUB_SUS RPAREN
                    {
                    mLPAREN(); 


                    mSUB_SUS(); 


                    mRPAREN(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:168:4: SAFE_TOKEN
                    {
                    mSAFE_TOKEN(); 


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SUB_SUS"

    public void mTokens() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:1:8: ( LPAREN | RPAREN | WS | PHRASE | REGEX | SAFE_TOKEN | SUSPICIOUS_TOKEN )
        int alt11=7;
        alt11 = dfa11.predict(input);
        switch (alt11) {
            case 1 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:1:10: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 2 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:1:17: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 3 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:1:24: WS
                {
                mWS(); 


                }
                break;
            case 4 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:1:27: PHRASE
                {
                mPHRASE(); 


                }
                break;
            case 5 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:1:34: REGEX
                {
                mREGEX(); 


                }
                break;
            case 6 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:1:40: SAFE_TOKEN
                {
                mSAFE_TOKEN(); 


                }
                break;
            case 7 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:1:51: SUSPICIOUS_TOKEN
                {
                mSUSPICIOUS_TOKEN(); 


                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    protected DFA11 dfa11 = new DFA11(this);
    static final String DFA8_eotS =
        "\1\1\1\uffff\1\1\2\uffff\1\1";
    static final String DFA8_eofS =
        "\6\uffff";
    static final String DFA8_minS =
        "\1\0\1\uffff\2\0\1\uffff\1\0";
    static final String DFA8_maxS =
        "\1\uffff\1\uffff\2\uffff\1\uffff\1\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\2\2\uffff\1\1\1\uffff";
    static final String DFA8_specialS =
        "\1\2\1\uffff\1\0\1\1\1\uffff\1\3}>";
    static final String[] DFA8_transitionS = {
            "\11\2\2\uffff\2\2\1\uffff\22\2\1\uffff\1\2\1\uffff\4\2\3\uffff"+
            "\5\2\1\uffff\54\2\1\3\u2fa3\2\1\uffff\ucfff\2",
            "",
            "\11\2\2\uffff\2\2\1\uffff\22\2\1\uffff\1\2\1\uffff\4\2\1\uffff"+
            "\1\4\1\uffff\5\2\1\uffff\54\2\1\3\u2fa3\2\1\uffff\ucfff\2",
            "\0\5",
            "",
            "\11\2\2\uffff\2\2\1\uffff\22\2\1\uffff\1\2\1\uffff\4\2\1\uffff"+
            "\1\4\1\uffff\5\2\1\uffff\54\2\1\3\u2fa3\2\1\uffff\ucfff\2"
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "()+ loopback of 160:2: ( SAFE_TOKEN LPAREN ( SUB_SUS )? RPAREN )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA8_2 = input.LA(1);

                        s = -1;
                        if ( ((LA8_2 >= '\u0000' && LA8_2 <= '\b')||(LA8_2 >= '\u000B' && LA8_2 <= '\f')||(LA8_2 >= '\u000E' && LA8_2 <= '\u001F')||LA8_2=='!'||(LA8_2 >= '#' && LA8_2 <= '&')||(LA8_2 >= '*' && LA8_2 <= '.')||(LA8_2 >= '0' && LA8_2 <= '[')||(LA8_2 >= ']' && LA8_2 <= '\u2FFF')||(LA8_2 >= '\u3001' && LA8_2 <= '\uFFFF')) ) {s = 2;}

                        else if ( (LA8_2=='\\') ) {s = 3;}

                        else if ( (LA8_2=='(') ) {s = 4;}

                        else s = 1;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA8_3 = input.LA(1);

                        s = -1;
                        if ( ((LA8_3 >= '\u0000' && LA8_3 <= '\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA8_0 = input.LA(1);

                        s = -1;
                        if ( ((LA8_0 >= '\u0000' && LA8_0 <= '\b')||(LA8_0 >= '\u000B' && LA8_0 <= '\f')||(LA8_0 >= '\u000E' && LA8_0 <= '\u001F')||LA8_0=='!'||(LA8_0 >= '#' && LA8_0 <= '&')||(LA8_0 >= '*' && LA8_0 <= '.')||(LA8_0 >= '0' && LA8_0 <= '[')||(LA8_0 >= ']' && LA8_0 <= '\u2FFF')||(LA8_0 >= '\u3001' && LA8_0 <= '\uFFFF')) ) {s = 2;}

                        else if ( (LA8_0=='\\') ) {s = 3;}

                        else s = 1;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA8_5 = input.LA(1);

                        s = -1;
                        if ( ((LA8_5 >= '\u0000' && LA8_5 <= '\b')||(LA8_5 >= '\u000B' && LA8_5 <= '\f')||(LA8_5 >= '\u000E' && LA8_5 <= '\u001F')||LA8_5=='!'||(LA8_5 >= '#' && LA8_5 <= '&')||(LA8_5 >= '*' && LA8_5 <= '.')||(LA8_5 >= '0' && LA8_5 <= '[')||(LA8_5 >= ']' && LA8_5 <= '\u2FFF')||(LA8_5 >= '\u3001' && LA8_5 <= '\uFFFF')) ) {s = 2;}

                        else if ( (LA8_5=='\\') ) {s = 3;}

                        else if ( (LA8_5=='(') ) {s = 4;}

                        else s = 1;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 8, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA11_eotS =
        "\6\uffff\1\10\3\uffff\1\10";
    static final String DFA11_eofS =
        "\13\uffff";
    static final String DFA11_minS =
        "\1\0\5\uffff\2\0\2\uffff\1\0";
    static final String DFA11_maxS =
        "\1\uffff\5\uffff\2\uffff\2\uffff\1\uffff";
    static final String DFA11_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\2\uffff\1\6\1\7\1\uffff";
    static final String DFA11_specialS =
        "\1\1\5\uffff\1\3\1\0\2\uffff\1\2}>";
    static final String[] DFA11_transitionS = {
            "\11\6\2\3\2\6\1\3\22\6\1\3\1\6\1\4\4\6\1\4\1\1\1\2\5\6\1\5\54"+
            "\6\1\7\u2fa3\6\1\3\ucfff\6",
            "",
            "",
            "",
            "",
            "",
            "\11\6\2\uffff\2\6\1\uffff\22\6\1\uffff\1\6\1\uffff\4\6\1\uffff"+
            "\1\11\1\uffff\5\6\1\uffff\54\6\1\7\u2fa3\6\1\uffff\ucfff\6",
            "\0\12",
            "",
            "",
            "\11\6\2\uffff\2\6\1\uffff\22\6\1\uffff\1\6\1\uffff\4\6\1\uffff"+
            "\1\11\1\uffff\5\6\1\uffff\54\6\1\7\u2fa3\6\1\uffff\ucfff\6"
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( LPAREN | RPAREN | WS | PHRASE | REGEX | SAFE_TOKEN | SUSPICIOUS_TOKEN );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA11_7 = input.LA(1);

                        s = -1;
                        if ( ((LA11_7 >= '\u0000' && LA11_7 <= '\uFFFF')) ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA11_0 = input.LA(1);

                        s = -1;
                        if ( (LA11_0=='(') ) {s = 1;}

                        else if ( (LA11_0==')') ) {s = 2;}

                        else if ( ((LA11_0 >= '\t' && LA11_0 <= '\n')||LA11_0=='\r'||LA11_0==' '||LA11_0=='\u3000') ) {s = 3;}

                        else if ( (LA11_0=='\"'||LA11_0=='\'') ) {s = 4;}

                        else if ( (LA11_0=='/') ) {s = 5;}

                        else if ( ((LA11_0 >= '\u0000' && LA11_0 <= '\b')||(LA11_0 >= '\u000B' && LA11_0 <= '\f')||(LA11_0 >= '\u000E' && LA11_0 <= '\u001F')||LA11_0=='!'||(LA11_0 >= '#' && LA11_0 <= '&')||(LA11_0 >= '*' && LA11_0 <= '.')||(LA11_0 >= '0' && LA11_0 <= '[')||(LA11_0 >= ']' && LA11_0 <= '\u2FFF')||(LA11_0 >= '\u3001' && LA11_0 <= '\uFFFF')) ) {s = 6;}

                        else if ( (LA11_0=='\\') ) {s = 7;}

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA11_10 = input.LA(1);

                        s = -1;
                        if ( ((LA11_10 >= '\u0000' && LA11_10 <= '\b')||(LA11_10 >= '\u000B' && LA11_10 <= '\f')||(LA11_10 >= '\u000E' && LA11_10 <= '\u001F')||LA11_10=='!'||(LA11_10 >= '#' && LA11_10 <= '&')||(LA11_10 >= '*' && LA11_10 <= '.')||(LA11_10 >= '0' && LA11_10 <= '[')||(LA11_10 >= ']' && LA11_10 <= '\u2FFF')||(LA11_10 >= '\u3001' && LA11_10 <= '\uFFFF')) ) {s = 6;}

                        else if ( (LA11_10=='\\') ) {s = 7;}

                        else if ( (LA11_10=='(') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA11_6 = input.LA(1);

                        s = -1;
                        if ( ((LA11_6 >= '\u0000' && LA11_6 <= '\b')||(LA11_6 >= '\u000B' && LA11_6 <= '\f')||(LA11_6 >= '\u000E' && LA11_6 <= '\u001F')||LA11_6=='!'||(LA11_6 >= '#' && LA11_6 <= '&')||(LA11_6 >= '*' && LA11_6 <= '.')||(LA11_6 >= '0' && LA11_6 <= '[')||(LA11_6 >= ']' && LA11_6 <= '\u2FFF')||(LA11_6 >= '\u3001' && LA11_6 <= '\uFFFF')) ) {s = 6;}

                        else if ( (LA11_6=='\\') ) {s = 7;}

                        else if ( (LA11_6=='(') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 11, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}