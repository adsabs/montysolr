// $ANTLR 3.4 StandardLuceneGrammar.g 2011-10-31 22:43:11

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
            // StandardLuceneGrammar.g:238:9: ( '(' )
            // StandardLuceneGrammar.g:238:11: '('
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
            // StandardLuceneGrammar.g:240:9: ( ')' )
            // StandardLuceneGrammar.g:240:11: ')'
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
            // StandardLuceneGrammar.g:242:9: ( '[' )
            // StandardLuceneGrammar.g:242:11: '['
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
            // StandardLuceneGrammar.g:244:9: ( ']' )
            // StandardLuceneGrammar.g:244:11: ']'
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
            // StandardLuceneGrammar.g:246:9: ( ':' )
            // StandardLuceneGrammar.g:246:11: ':'
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
            // StandardLuceneGrammar.g:248:7: ( '+' )
            // StandardLuceneGrammar.g:248:9: '+'
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
            // StandardLuceneGrammar.g:250:7: ( ( '-' | '\\!' ) )
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
            // StandardLuceneGrammar.g:252:7: ( '*' )
            // StandardLuceneGrammar.g:252:9: '*'
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
            // StandardLuceneGrammar.g:254:8: ( ( '?' )+ )
            // StandardLuceneGrammar.g:254:10: ( '?' )+
            {
            // StandardLuceneGrammar.g:254:10: ( '?' )+
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
            	    // StandardLuceneGrammar.g:254:10: '?'
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
            // StandardLuceneGrammar.g:256:16: ( '|' )
            // StandardLuceneGrammar.g:256:18: '|'
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
            // StandardLuceneGrammar.g:258:16: ( '&' )
            // StandardLuceneGrammar.g:258:18: '&'
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
            // StandardLuceneGrammar.g:260:9: ( '{' )
            // StandardLuceneGrammar.g:260:11: '{'
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
            // StandardLuceneGrammar.g:262:9: ( '}' )
            // StandardLuceneGrammar.g:262:11: '}'
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
            // StandardLuceneGrammar.g:264:7: ( '^' )
            // StandardLuceneGrammar.g:264:9: '^'
            {
            match('^'); 

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
            // StandardLuceneGrammar.g:266:7: ( '~' )
            // StandardLuceneGrammar.g:266:9: '~'
            {
            match('~'); 

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
            // StandardLuceneGrammar.g:269:2: ( '\\\"' )
            // StandardLuceneGrammar.g:269:4: '\\\"'
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
            // StandardLuceneGrammar.g:272:2: ( '\\'' )
            // StandardLuceneGrammar.g:272:4: '\\''
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
            // StandardLuceneGrammar.g:275:18: ( '\\\\' . )
            // StandardLuceneGrammar.g:275:21: '\\\\' .
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
            // StandardLuceneGrammar.g:277:4: ( 'TO' )
            // StandardLuceneGrammar.g:277:6: 'TO'
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
            // StandardLuceneGrammar.g:280:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) ) )
            // StandardLuceneGrammar.g:280:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
            {
            // StandardLuceneGrammar.g:280:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='A'||LA3_0=='a') ) {
                alt3=1;
            }
            else if ( (LA3_0=='&') ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // StandardLuceneGrammar.g:280:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
                    // StandardLuceneGrammar.g:280:48: ( AMPER ( AMPER )? )
                    {
                    // StandardLuceneGrammar.g:280:48: ( AMPER ( AMPER )? )
                    // StandardLuceneGrammar.g:280:49: AMPER ( AMPER )?
                    {
                    mAMPER(); 


                    // StandardLuceneGrammar.g:280:55: ( AMPER )?
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0=='&') ) {
                        alt2=1;
                    }
                    switch (alt2) {
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
            // StandardLuceneGrammar.g:281:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) ) )
            // StandardLuceneGrammar.g:281:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
            {
            // StandardLuceneGrammar.g:281:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='O'||LA5_0=='o') ) {
                alt5=1;
            }
            else if ( (LA5_0=='|') ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // StandardLuceneGrammar.g:281:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
                    // StandardLuceneGrammar.g:281:34: ( VBAR ( VBAR )? )
                    {
                    // StandardLuceneGrammar.g:281:34: ( VBAR ( VBAR )? )
                    // StandardLuceneGrammar.g:281:35: VBAR ( VBAR )?
                    {
                    mVBAR(); 


                    // StandardLuceneGrammar.g:281:40: ( VBAR )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='|') ) {
                        alt4=1;
                    }
                    switch (alt4) {
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
            // StandardLuceneGrammar.g:282:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
            // StandardLuceneGrammar.g:282:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
            // StandardLuceneGrammar.g:283:7: ( ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' ) )
            // StandardLuceneGrammar.g:283:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            {
            // StandardLuceneGrammar.g:283:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='n') ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1=='E'||LA6_1=='e') ) {
                    alt6=1;
                }
                else {
                    alt6=2;
                }
            }
            else if ( (LA6_0=='N') ) {
                alt6=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // StandardLuceneGrammar.g:283:10: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
                    // StandardLuceneGrammar.g:283:60: 'n'
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
            // StandardLuceneGrammar.g:286:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' ) )
            // StandardLuceneGrammar.g:286:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )
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
            // StandardLuceneGrammar.g:296:13: ( '0' .. '9' )
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
            // StandardLuceneGrammar.g:298:23: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' ) )
            // StandardLuceneGrammar.g:299:6: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' )
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
            // StandardLuceneGrammar.g:308:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // StandardLuceneGrammar.g:309:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // StandardLuceneGrammar.g:309:2: ( INT )+
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


            // StandardLuceneGrammar.g:309:7: ( '.' ( INT )+ )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='.') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // StandardLuceneGrammar.g:309:8: '.' ( INT )+
                    {
                    match('.'); 

                    // StandardLuceneGrammar.g:309:12: ( INT )+
                    int cnt8=0;
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
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
                    	    if ( cnt8 >= 1 ) break loop8;
                                EarlyExitException eee =
                                    new EarlyExitException(8, input);
                                throw eee;
                        }
                        cnt8++;
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
            // StandardLuceneGrammar.g:313:2: ( INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )? )
            // StandardLuceneGrammar.g:313:4: INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )?
            {
            mINT(); 


            // StandardLuceneGrammar.g:313:8: ( INT )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( ((LA10_0 >= '0' && LA10_0 <= '9')) ) {
                alt10=1;
            }
            switch (alt10) {
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


            // StandardLuceneGrammar.g:313:31: ( INT )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0 >= '0' && LA11_0 <= '9')) ) {
                alt11=1;
            }
            switch (alt11) {
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


            // StandardLuceneGrammar.g:313:58: ( INT INT )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( ((LA12_0 >= '0' && LA12_0 <= '9')) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // StandardLuceneGrammar.g:313:59: INT INT
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
            // StandardLuceneGrammar.g:317:2: ( ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )* )
            // StandardLuceneGrammar.g:318:2: ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )*
            {
            // StandardLuceneGrammar.g:318:2: ( NORMAL_CHAR | ESC_CHAR )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( ((LA13_0 >= '\u0000' && LA13_0 <= '\b')||(LA13_0 >= '\u000B' && LA13_0 <= '\f')||(LA13_0 >= '\u000E' && LA13_0 <= '\u001F')||(LA13_0 >= '#' && LA13_0 <= '%')||LA13_0==','||LA13_0=='.'||(LA13_0 >= '0' && LA13_0 <= '9')||(LA13_0 >= ';' && LA13_0 <= '>')||(LA13_0 >= '@' && LA13_0 <= 'Z')||(LA13_0 >= '_' && LA13_0 <= 'z')||(LA13_0 >= '\u007F' && LA13_0 <= '\u2FFF')||(LA13_0 >= '\u3001' && LA13_0 <= '\uFFFF')) ) {
                alt13=1;
            }
            else if ( (LA13_0=='\\') ) {
                alt13=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }
            switch (alt13) {
                case 1 :
                    // StandardLuceneGrammar.g:318:4: NORMAL_CHAR
                    {
                    mNORMAL_CHAR(); 


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:318:18: ESC_CHAR
                    {
                    mESC_CHAR(); 


                    }
                    break;

            }


            // StandardLuceneGrammar.g:318:28: ( NORMAL_CHAR | ESC_CHAR )*
            loop14:
            do {
                int alt14=3;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0 >= '\u0000' && LA14_0 <= '\b')||(LA14_0 >= '\u000B' && LA14_0 <= '\f')||(LA14_0 >= '\u000E' && LA14_0 <= '\u001F')||(LA14_0 >= '#' && LA14_0 <= '%')||LA14_0==','||LA14_0=='.'||(LA14_0 >= '0' && LA14_0 <= '9')||(LA14_0 >= ';' && LA14_0 <= '>')||(LA14_0 >= '@' && LA14_0 <= 'Z')||(LA14_0 >= '_' && LA14_0 <= 'z')||(LA14_0 >= '\u007F' && LA14_0 <= '\u2FFF')||(LA14_0 >= '\u3001' && LA14_0 <= '\uFFFF')) ) {
                    alt14=1;
                }
                else if ( (LA14_0=='\\') ) {
                    alt14=2;
                }


                switch (alt14) {
            	case 1 :
            	    // StandardLuceneGrammar.g:318:30: NORMAL_CHAR
            	    {
            	    mNORMAL_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // StandardLuceneGrammar.g:318:44: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;

            	default :
            	    break loop14;
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
            // StandardLuceneGrammar.g:322:15: ( ( STAR | QMARK ) ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )* ( NORMAL_CHAR | ESC_CHAR )* | ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+ ( NORMAL_CHAR | ESC_CHAR )* )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0=='*'||LA24_0=='?') ) {
                alt24=1;
            }
            else if ( ((LA24_0 >= '\u0000' && LA24_0 <= '\b')||(LA24_0 >= '\u000B' && LA24_0 <= '\f')||(LA24_0 >= '\u000E' && LA24_0 <= '\u001F')||(LA24_0 >= '#' && LA24_0 <= '%')||LA24_0==','||LA24_0=='.'||(LA24_0 >= '0' && LA24_0 <= '9')||(LA24_0 >= ';' && LA24_0 <= '>')||(LA24_0 >= '@' && LA24_0 <= 'Z')||LA24_0=='\\'||(LA24_0 >= '_' && LA24_0 <= 'z')||(LA24_0 >= '\u007F' && LA24_0 <= '\u2FFF')||(LA24_0 >= '\u3001' && LA24_0 <= '\uFFFF')) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;

            }
            switch (alt24) {
                case 1 :
                    // StandardLuceneGrammar.g:323:2: ( STAR | QMARK ) ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )* ( NORMAL_CHAR | ESC_CHAR )*
                    {
                    // StandardLuceneGrammar.g:323:2: ( STAR | QMARK )
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0=='*') ) {
                        alt15=1;
                    }
                    else if ( (LA15_0=='?') ) {
                        alt15=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 0, input);

                        throw nvae;

                    }
                    switch (alt15) {
                        case 1 :
                            // StandardLuceneGrammar.g:323:3: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // StandardLuceneGrammar.g:323:8: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // StandardLuceneGrammar.g:323:15: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )*
                    loop18:
                    do {
                        int alt18=2;
                        alt18 = dfa18.predict(input);
                        switch (alt18) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:323:16: ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // StandardLuceneGrammar.g:323:16: ( NORMAL_CHAR | ESC_CHAR )+
                    	    int cnt16=0;
                    	    loop16:
                    	    do {
                    	        int alt16=3;
                    	        int LA16_0 = input.LA(1);

                    	        if ( ((LA16_0 >= '\u0000' && LA16_0 <= '\b')||(LA16_0 >= '\u000B' && LA16_0 <= '\f')||(LA16_0 >= '\u000E' && LA16_0 <= '\u001F')||(LA16_0 >= '#' && LA16_0 <= '%')||LA16_0==','||LA16_0=='.'||(LA16_0 >= '0' && LA16_0 <= '9')||(LA16_0 >= ';' && LA16_0 <= '>')||(LA16_0 >= '@' && LA16_0 <= 'Z')||(LA16_0 >= '_' && LA16_0 <= 'z')||(LA16_0 >= '\u007F' && LA16_0 <= '\u2FFF')||(LA16_0 >= '\u3001' && LA16_0 <= '\uFFFF')) ) {
                    	            alt16=1;
                    	        }
                    	        else if ( (LA16_0=='\\') ) {
                    	            alt16=2;
                    	        }


                    	        switch (alt16) {
                    	    	case 1 :
                    	    	    // StandardLuceneGrammar.g:323:17: NORMAL_CHAR
                    	    	    {
                    	    	    mNORMAL_CHAR(); 


                    	    	    }
                    	    	    break;
                    	    	case 2 :
                    	    	    // StandardLuceneGrammar.g:323:29: ESC_CHAR
                    	    	    {
                    	    	    mESC_CHAR(); 


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


                    	    // StandardLuceneGrammar.g:323:40: ( QMARK | STAR )
                    	    int alt17=2;
                    	    int LA17_0 = input.LA(1);

                    	    if ( (LA17_0=='?') ) {
                    	        alt17=1;
                    	    }
                    	    else if ( (LA17_0=='*') ) {
                    	        alt17=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 17, 0, input);

                    	        throw nvae;

                    	    }
                    	    switch (alt17) {
                    	        case 1 :
                    	            // StandardLuceneGrammar.g:323:41: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // StandardLuceneGrammar.g:323:47: STAR
                    	            {
                    	            mSTAR(); 


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop18;
                        }
                    } while (true);


                    // StandardLuceneGrammar.g:323:55: ( NORMAL_CHAR | ESC_CHAR )*
                    loop19:
                    do {
                        int alt19=3;
                        int LA19_0 = input.LA(1);

                        if ( ((LA19_0 >= '\u0000' && LA19_0 <= '\b')||(LA19_0 >= '\u000B' && LA19_0 <= '\f')||(LA19_0 >= '\u000E' && LA19_0 <= '\u001F')||(LA19_0 >= '#' && LA19_0 <= '%')||LA19_0==','||LA19_0=='.'||(LA19_0 >= '0' && LA19_0 <= '9')||(LA19_0 >= ';' && LA19_0 <= '>')||(LA19_0 >= '@' && LA19_0 <= 'Z')||(LA19_0 >= '_' && LA19_0 <= 'z')||(LA19_0 >= '\u007F' && LA19_0 <= '\u2FFF')||(LA19_0 >= '\u3001' && LA19_0 <= '\uFFFF')) ) {
                            alt19=1;
                        }
                        else if ( (LA19_0=='\\') ) {
                            alt19=2;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:323:56: NORMAL_CHAR
                    	    {
                    	    mNORMAL_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // StandardLuceneGrammar.g:323:68: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop19;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:324:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+ ( NORMAL_CHAR | ESC_CHAR )*
                    {
                    // StandardLuceneGrammar.g:324:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+
                    int cnt22=0;
                    loop22:
                    do {
                        int alt22=2;
                        alt22 = dfa22.predict(input);
                        switch (alt22) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:324:5: ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // StandardLuceneGrammar.g:324:5: ( NORMAL_CHAR | ESC_CHAR )+
                    	    int cnt20=0;
                    	    loop20:
                    	    do {
                    	        int alt20=3;
                    	        int LA20_0 = input.LA(1);

                    	        if ( ((LA20_0 >= '\u0000' && LA20_0 <= '\b')||(LA20_0 >= '\u000B' && LA20_0 <= '\f')||(LA20_0 >= '\u000E' && LA20_0 <= '\u001F')||(LA20_0 >= '#' && LA20_0 <= '%')||LA20_0==','||LA20_0=='.'||(LA20_0 >= '0' && LA20_0 <= '9')||(LA20_0 >= ';' && LA20_0 <= '>')||(LA20_0 >= '@' && LA20_0 <= 'Z')||(LA20_0 >= '_' && LA20_0 <= 'z')||(LA20_0 >= '\u007F' && LA20_0 <= '\u2FFF')||(LA20_0 >= '\u3001' && LA20_0 <= '\uFFFF')) ) {
                    	            alt20=1;
                    	        }
                    	        else if ( (LA20_0=='\\') ) {
                    	            alt20=2;
                    	        }


                    	        switch (alt20) {
                    	    	case 1 :
                    	    	    // StandardLuceneGrammar.g:324:6: NORMAL_CHAR
                    	    	    {
                    	    	    mNORMAL_CHAR(); 


                    	    	    }
                    	    	    break;
                    	    	case 2 :
                    	    	    // StandardLuceneGrammar.g:324:18: ESC_CHAR
                    	    	    {
                    	    	    mESC_CHAR(); 


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


                    	    // StandardLuceneGrammar.g:324:29: ( QMARK | STAR )
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
                    	            // StandardLuceneGrammar.g:324:30: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // StandardLuceneGrammar.g:324:36: STAR
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


                    // StandardLuceneGrammar.g:324:44: ( NORMAL_CHAR | ESC_CHAR )*
                    loop23:
                    do {
                        int alt23=3;
                        int LA23_0 = input.LA(1);

                        if ( ((LA23_0 >= '\u0000' && LA23_0 <= '\b')||(LA23_0 >= '\u000B' && LA23_0 <= '\f')||(LA23_0 >= '\u000E' && LA23_0 <= '\u001F')||(LA23_0 >= '#' && LA23_0 <= '%')||LA23_0==','||LA23_0=='.'||(LA23_0 >= '0' && LA23_0 <= '9')||(LA23_0 >= ';' && LA23_0 <= '>')||(LA23_0 >= '@' && LA23_0 <= 'Z')||(LA23_0 >= '_' && LA23_0 <= 'z')||(LA23_0 >= '\u007F' && LA23_0 <= '\u2FFF')||(LA23_0 >= '\u3001' && LA23_0 <= '\uFFFF')) ) {
                            alt23=1;
                        }
                        else if ( (LA23_0=='\\') ) {
                            alt23=2;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:324:45: NORMAL_CHAR
                    	    {
                    	    mNORMAL_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // StandardLuceneGrammar.g:324:57: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop23;
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
            // StandardLuceneGrammar.g:329:2: ( DQUOTE (~ ( '\\\"' | '\\\\\"' | '?' | '*' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:330:2: DQUOTE (~ ( '\\\"' | '\\\\\"' | '?' | '*' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:330:9: (~ ( '\\\"' | '\\\\\"' | '?' | '*' ) )+
            int cnt25=0;
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( ((LA25_0 >= '\u0000' && LA25_0 <= '!')||(LA25_0 >= '#' && LA25_0 <= ')')||(LA25_0 >= '+' && LA25_0 <= '>')||(LA25_0 >= '@' && LA25_0 <= '\uFFFF')) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // StandardLuceneGrammar.g:330:9: ~ ( '\\\"' | '\\\\\"' | '?' | '*' )
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
            	    if ( cnt25 >= 1 ) break loop25;
                        EarlyExitException eee =
                            new EarlyExitException(25, input);
                        throw eee;
                }
                cnt25++;
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
            // StandardLuceneGrammar.g:333:17: ( DQUOTE (~ ( '\\\"' | '\\\\\"' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:334:2: DQUOTE (~ ( '\\\"' | '\\\\\"' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:334:9: (~ ( '\\\"' | '\\\\\"' ) )+
            int cnt26=0;
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( ((LA26_0 >= '\u0000' && LA26_0 <= '!')||(LA26_0 >= '#' && LA26_0 <= '\uFFFF')) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // StandardLuceneGrammar.g:334:9: ~ ( '\\\"' | '\\\\\"' )
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
            	    if ( cnt26 >= 1 ) break loop26;
                        EarlyExitException eee =
                            new EarlyExitException(26, input);
                        throw eee;
                }
                cnt26++;
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
        int alt27=28;
        alt27 = dfa27.predict(input);
        switch (alt27) {
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


    protected DFA18 dfa18 = new DFA18(this);
    protected DFA22 dfa22 = new DFA22(this);
    protected DFA27 dfa27 = new DFA27(this);
    static final String DFA18_eotS =
        "\2\3\3\uffff\1\3";
    static final String DFA18_eofS =
        "\6\uffff";
    static final String DFA18_minS =
        "\3\0\2\uffff\1\0";
    static final String DFA18_maxS =
        "\3\uffff\2\uffff\1\uffff";
    static final String DFA18_acceptS =
        "\3\uffff\1\2\1\1\1\uffff";
    static final String DFA18_specialS =
        "\1\2\1\3\1\0\2\uffff\1\1}>";
    static final String[] DFA18_transitionS = {
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

    static final short[] DFA18_eot = DFA.unpackEncodedString(DFA18_eotS);
    static final short[] DFA18_eof = DFA.unpackEncodedString(DFA18_eofS);
    static final char[] DFA18_min = DFA.unpackEncodedStringToUnsignedChars(DFA18_minS);
    static final char[] DFA18_max = DFA.unpackEncodedStringToUnsignedChars(DFA18_maxS);
    static final short[] DFA18_accept = DFA.unpackEncodedString(DFA18_acceptS);
    static final short[] DFA18_special = DFA.unpackEncodedString(DFA18_specialS);
    static final short[][] DFA18_transition;

    static {
        int numStates = DFA18_transitionS.length;
        DFA18_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA18_transition[i] = DFA.unpackEncodedString(DFA18_transitionS[i]);
        }
    }

    class DFA18 extends DFA {

        public DFA18(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 18;
            this.eot = DFA18_eot;
            this.eof = DFA18_eof;
            this.min = DFA18_min;
            this.max = DFA18_max;
            this.accept = DFA18_accept;
            this.special = DFA18_special;
            this.transition = DFA18_transition;
        }
        public String getDescription() {
            return "()* loopback of 323:15: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA18_2 = input.LA(1);

                        s = -1;
                        if ( ((LA18_2 >= '\u0000' && LA18_2 <= '\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA18_5 = input.LA(1);

                        s = -1;
                        if ( ((LA18_5 >= '\u0000' && LA18_5 <= '\b')||(LA18_5 >= '\u000B' && LA18_5 <= '\f')||(LA18_5 >= '\u000E' && LA18_5 <= '\u001F')||(LA18_5 >= '#' && LA18_5 <= '%')||LA18_5==','||LA18_5=='.'||(LA18_5 >= '0' && LA18_5 <= '9')||(LA18_5 >= ';' && LA18_5 <= '>')||(LA18_5 >= '@' && LA18_5 <= 'Z')||(LA18_5 >= '_' && LA18_5 <= 'z')||(LA18_5 >= '\u007F' && LA18_5 <= '\u2FFF')||(LA18_5 >= '\u3001' && LA18_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA18_5=='\\') ) {s = 2;}

                        else if ( (LA18_5=='*'||LA18_5=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA18_0 = input.LA(1);

                        s = -1;
                        if ( ((LA18_0 >= '\u0000' && LA18_0 <= '\b')||(LA18_0 >= '\u000B' && LA18_0 <= '\f')||(LA18_0 >= '\u000E' && LA18_0 <= '\u001F')||(LA18_0 >= '#' && LA18_0 <= '%')||LA18_0==','||LA18_0=='.'||(LA18_0 >= '0' && LA18_0 <= '9')||(LA18_0 >= ';' && LA18_0 <= '>')||(LA18_0 >= '@' && LA18_0 <= 'Z')||(LA18_0 >= '_' && LA18_0 <= 'z')||(LA18_0 >= '\u007F' && LA18_0 <= '\u2FFF')||(LA18_0 >= '\u3001' && LA18_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA18_0=='\\') ) {s = 2;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA18_1 = input.LA(1);

                        s = -1;
                        if ( ((LA18_1 >= '\u0000' && LA18_1 <= '\b')||(LA18_1 >= '\u000B' && LA18_1 <= '\f')||(LA18_1 >= '\u000E' && LA18_1 <= '\u001F')||(LA18_1 >= '#' && LA18_1 <= '%')||LA18_1==','||LA18_1=='.'||(LA18_1 >= '0' && LA18_1 <= '9')||(LA18_1 >= ';' && LA18_1 <= '>')||(LA18_1 >= '@' && LA18_1 <= 'Z')||(LA18_1 >= '_' && LA18_1 <= 'z')||(LA18_1 >= '\u007F' && LA18_1 <= '\u2FFF')||(LA18_1 >= '\u3001' && LA18_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA18_1=='\\') ) {s = 2;}

                        else if ( (LA18_1=='*'||LA18_1=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 18, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA22_eotS =
        "\2\3\3\uffff\1\3";
    static final String DFA22_eofS =
        "\6\uffff";
    static final String DFA22_minS =
        "\3\0\2\uffff\1\0";
    static final String DFA22_maxS =
        "\3\uffff\2\uffff\1\uffff";
    static final String DFA22_acceptS =
        "\3\uffff\1\2\1\1\1\uffff";
    static final String DFA22_specialS =
        "\1\2\1\3\1\0\2\uffff\1\1}>";
    static final String[] DFA22_transitionS = {
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
            return "()+ loopback of 324:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA22_2 = input.LA(1);

                        s = -1;
                        if ( ((LA22_2 >= '\u0000' && LA22_2 <= '\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA22_5 = input.LA(1);

                        s = -1;
                        if ( ((LA22_5 >= '\u0000' && LA22_5 <= '\b')||(LA22_5 >= '\u000B' && LA22_5 <= '\f')||(LA22_5 >= '\u000E' && LA22_5 <= '\u001F')||(LA22_5 >= '#' && LA22_5 <= '%')||LA22_5==','||LA22_5=='.'||(LA22_5 >= '0' && LA22_5 <= '9')||(LA22_5 >= ';' && LA22_5 <= '>')||(LA22_5 >= '@' && LA22_5 <= 'Z')||(LA22_5 >= '_' && LA22_5 <= 'z')||(LA22_5 >= '\u007F' && LA22_5 <= '\u2FFF')||(LA22_5 >= '\u3001' && LA22_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_5=='\\') ) {s = 2;}

                        else if ( (LA22_5=='*'||LA22_5=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA22_0 = input.LA(1);

                        s = -1;
                        if ( ((LA22_0 >= '\u0000' && LA22_0 <= '\b')||(LA22_0 >= '\u000B' && LA22_0 <= '\f')||(LA22_0 >= '\u000E' && LA22_0 <= '\u001F')||(LA22_0 >= '#' && LA22_0 <= '%')||LA22_0==','||LA22_0=='.'||(LA22_0 >= '0' && LA22_0 <= '9')||(LA22_0 >= ';' && LA22_0 <= '>')||(LA22_0 >= '@' && LA22_0 <= 'Z')||(LA22_0 >= '_' && LA22_0 <= 'z')||(LA22_0 >= '\u007F' && LA22_0 <= '\u2FFF')||(LA22_0 >= '\u3001' && LA22_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_0=='\\') ) {s = 2;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA22_1 = input.LA(1);

                        s = -1;
                        if ( ((LA22_1 >= '\u0000' && LA22_1 <= '\b')||(LA22_1 >= '\u000B' && LA22_1 <= '\f')||(LA22_1 >= '\u000E' && LA22_1 <= '\u001F')||(LA22_1 >= '#' && LA22_1 <= '%')||LA22_1==','||LA22_1=='.'||(LA22_1 >= '0' && LA22_1 <= '9')||(LA22_1 >= ';' && LA22_1 <= '>')||(LA22_1 >= '@' && LA22_1 <= 'Z')||(LA22_1 >= '_' && LA22_1 <= 'z')||(LA22_1 >= '\u007F' && LA22_1 <= '\u2FFF')||(LA22_1 >= '\u3001' && LA22_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA22_1=='\\') ) {s = 2;}

                        else if ( (LA22_1=='*'||LA22_1=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 22, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA27_eotS =
        "\11\uffff\1\34\1\36\4\uffff\1\37\1\uffff\2\43\1\uffff\1\43\1\uffff"+
        "\1\52\1\43\1\uffff\1\54\1\43\7\uffff\1\61\1\uffff\1\43\1\uffff\1"+
        "\43\1\25\2\43\1\uffff\1\43\1\uffff\1\54\1\uffff\1\43\2\uffff\1\43"+
        "\1\23\1\71\1\43\2\54\2\uffff\1\52\1\54\2\43\1\54\1\43\1\56\1\43"+
        "\1\56";
    static final String DFA27_eofS =
        "\103\uffff";
    static final String DFA27_minS =
        "\1\0\10\uffff\2\0\4\uffff\1\0\1\uffff\2\0\1\uffff\1\0\1\uffff\2"+
        "\0\1\uffff\3\0\4\uffff\1\0\1\uffff\1\0\1\uffff\6\0\1\uffff\1\0\1"+
        "\uffff\1\0\1\uffff\1\0\2\uffff\6\0\2\uffff\11\0";
    static final String DFA27_maxS =
        "\1\uffff\10\uffff\2\uffff\4\uffff\1\uffff\1\uffff\2\uffff\1\uffff"+
        "\1\uffff\1\uffff\2\uffff\1\uffff\3\uffff\4\uffff\1\uffff\1\uffff"+
        "\1\uffff\1\uffff\6\uffff\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff"+
        "\1\uffff\2\uffff\6\uffff\2\uffff\11\uffff";
    static final String DFA27_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff\1\13\1\14\1\15"+
        "\1\16\1\uffff\1\20\2\uffff\1\22\1\uffff\1\23\2\uffff\1\26\3\uffff"+
        "\1\11\1\32\1\12\1\17\1\uffff\1\34\1\uffff\1\31\6\uffff\1\25\1\uffff"+
        "\1\27\1\uffff\1\30\1\uffff\1\33\1\21\6\uffff\1\33\1\24\11\uffff";
    static final String DFA27_specialS =
        "\1\27\10\uffff\1\30\1\22\4\uffff\1\24\1\uffff\1\4\1\16\1\uffff\1"+
        "\41\1\uffff\1\1\1\7\1\uffff\1\37\1\2\1\5\4\uffff\1\43\1\uffff\1"+
        "\45\1\uffff\1\3\1\6\1\0\1\35\1\25\1\31\1\uffff\1\42\1\uffff\1\21"+
        "\1\uffff\1\40\2\uffff\1\34\1\26\1\17\1\32\1\23\1\15\2\uffff\1\10"+
        "\1\12\1\33\1\11\1\36\1\13\1\44\1\14\1\20}>";
    static final String[] DFA27_transitionS = {
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
            return "1:1: Tokens : ( T__57 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | NEAR | WS | NUMBER | DATE_TOKEN | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA27_38 = input.LA(1);

                        s = -1;
                        if ( (LA27_38=='D'||LA27_38=='d') ) {s = 51;}

                        else if ( ((LA27_38 >= '\u0000' && LA27_38 <= '\b')||(LA27_38 >= '\u000B' && LA27_38 <= '\f')||(LA27_38 >= '\u000E' && LA27_38 <= '\u001F')||(LA27_38 >= '#' && LA27_38 <= '%')||LA27_38==','||LA27_38=='.'||(LA27_38 >= '0' && LA27_38 <= '9')||(LA27_38 >= ';' && LA27_38 <= '>')||(LA27_38 >= '@' && LA27_38 <= 'C')||(LA27_38 >= 'E' && LA27_38 <= 'Z')||(LA27_38 >= '_' && LA27_38 <= 'c')||(LA27_38 >= 'e' && LA27_38 <= 'z')||(LA27_38 >= '\u007F' && LA27_38 <= '\u2FFF')||(LA27_38 >= '\u3001' && LA27_38 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_38=='\\') ) {s = 37;}

                        else if ( (LA27_38=='*'||LA27_38=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA27_22 = input.LA(1);

                        s = -1;
                        if ( (LA27_22=='O'||LA27_22=='o') ) {s = 40;}

                        else if ( (LA27_22=='E'||LA27_22=='e') ) {s = 41;}

                        else if ( ((LA27_22 >= '\u0000' && LA27_22 <= '\b')||(LA27_22 >= '\u000B' && LA27_22 <= '\f')||(LA27_22 >= '\u000E' && LA27_22 <= '\u001F')||(LA27_22 >= '#' && LA27_22 <= '%')||LA27_22==','||LA27_22=='.'||(LA27_22 >= '0' && LA27_22 <= '9')||(LA27_22 >= ';' && LA27_22 <= '>')||(LA27_22 >= '@' && LA27_22 <= 'D')||(LA27_22 >= 'F' && LA27_22 <= 'N')||(LA27_22 >= 'P' && LA27_22 <= 'Z')||(LA27_22 >= '_' && LA27_22 <= 'd')||(LA27_22 >= 'f' && LA27_22 <= 'n')||(LA27_22 >= 'p' && LA27_22 <= 'z')||(LA27_22 >= '\u007F' && LA27_22 <= '\u2FFF')||(LA27_22 >= '\u3001' && LA27_22 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_22=='\\') ) {s = 37;}

                        else if ( (LA27_22=='*'||LA27_22=='?') ) {s = 29;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA27_26 = input.LA(1);

                        s = -1;
                        if ( ((LA27_26 >= '\u0000' && LA27_26 <= '\b')||(LA27_26 >= '\u000B' && LA27_26 <= '\f')||(LA27_26 >= '\u000E' && LA27_26 <= '\u001F')||(LA27_26 >= '#' && LA27_26 <= '%')||LA27_26==','||LA27_26=='.'||(LA27_26 >= '0' && LA27_26 <= '9')||(LA27_26 >= ';' && LA27_26 <= '>')||(LA27_26 >= '@' && LA27_26 <= 'Z')||(LA27_26 >= '_' && LA27_26 <= 'z')||(LA27_26 >= '\u007F' && LA27_26 <= '\u2FFF')||(LA27_26 >= '\u3001' && LA27_26 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_26=='\\') ) {s = 37;}

                        else if ( (LA27_26=='*'||LA27_26=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA27_36 = input.LA(1);

                        s = -1;
                        if ( ((LA27_36 >= '\u0000' && LA27_36 <= '\b')||(LA27_36 >= '\u000B' && LA27_36 <= '\f')||(LA27_36 >= '\u000E' && LA27_36 <= '\u001F')||(LA27_36 >= '#' && LA27_36 <= '%')||LA27_36==','||LA27_36=='.'||(LA27_36 >= '0' && LA27_36 <= '9')||(LA27_36 >= ';' && LA27_36 <= '>')||(LA27_36 >= '@' && LA27_36 <= 'Z')||(LA27_36 >= '_' && LA27_36 <= 'z')||(LA27_36 >= '\u007F' && LA27_36 <= '\u2FFF')||(LA27_36 >= '\u3001' && LA27_36 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_36=='\\') ) {s = 37;}

                        else if ( (LA27_36=='*'||LA27_36=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA27_17 = input.LA(1);

                        s = -1;
                        if ( (LA27_17=='O') ) {s = 34;}

                        else if ( ((LA27_17 >= '\u0000' && LA27_17 <= '\b')||(LA27_17 >= '\u000B' && LA27_17 <= '\f')||(LA27_17 >= '\u000E' && LA27_17 <= '\u001F')||(LA27_17 >= '#' && LA27_17 <= '%')||LA27_17==','||LA27_17=='.'||(LA27_17 >= '0' && LA27_17 <= '9')||(LA27_17 >= ';' && LA27_17 <= '>')||(LA27_17 >= '@' && LA27_17 <= 'N')||(LA27_17 >= 'P' && LA27_17 <= 'Z')||(LA27_17 >= '_' && LA27_17 <= 'z')||(LA27_17 >= '\u007F' && LA27_17 <= '\u2FFF')||(LA27_17 >= '\u3001' && LA27_17 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_17=='\\') ) {s = 37;}

                        else if ( (LA27_17=='*'||LA27_17=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA27_27 = input.LA(1);

                        s = -1;
                        if ( ((LA27_27 >= '\u0000' && LA27_27 <= '\uFFFF')) ) {s = 47;}

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA27_37 = input.LA(1);

                        s = -1;
                        if ( ((LA27_37 >= '\u0000' && LA27_37 <= '\uFFFF')) ) {s = 50;}

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA27_23 = input.LA(1);

                        s = -1;
                        if ( (LA27_23=='O'||LA27_23=='o') ) {s = 40;}

                        else if ( (LA27_23=='E'||LA27_23=='e') ) {s = 41;}

                        else if ( ((LA27_23 >= '\u0000' && LA27_23 <= '\b')||(LA27_23 >= '\u000B' && LA27_23 <= '\f')||(LA27_23 >= '\u000E' && LA27_23 <= '\u001F')||(LA27_23 >= '#' && LA27_23 <= '%')||LA27_23==','||LA27_23=='.'||(LA27_23 >= '0' && LA27_23 <= '9')||(LA27_23 >= ';' && LA27_23 <= '>')||(LA27_23 >= '@' && LA27_23 <= 'D')||(LA27_23 >= 'F' && LA27_23 <= 'N')||(LA27_23 >= 'P' && LA27_23 <= 'Z')||(LA27_23 >= '_' && LA27_23 <= 'd')||(LA27_23 >= 'f' && LA27_23 <= 'n')||(LA27_23 >= 'p' && LA27_23 <= 'z')||(LA27_23 >= '\u007F' && LA27_23 <= '\u2FFF')||(LA27_23 >= '\u3001' && LA27_23 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_23=='\\') ) {s = 37;}

                        else if ( (LA27_23=='*'||LA27_23=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA27_58 = input.LA(1);

                        s = -1;
                        if ( ((LA27_58 >= '\u0000' && LA27_58 <= '\b')||(LA27_58 >= '\u000B' && LA27_58 <= '\f')||(LA27_58 >= '\u000E' && LA27_58 <= '\u001F')||(LA27_58 >= '#' && LA27_58 <= '%')||LA27_58==','||LA27_58=='.'||(LA27_58 >= '0' && LA27_58 <= '9')||(LA27_58 >= ';' && LA27_58 <= '>')||(LA27_58 >= '@' && LA27_58 <= 'Z')||(LA27_58 >= '_' && LA27_58 <= 'z')||(LA27_58 >= '\u007F' && LA27_58 <= '\u2FFF')||(LA27_58 >= '\u3001' && LA27_58 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_58=='\\') ) {s = 37;}

                        else if ( (LA27_58=='*'||LA27_58=='?') ) {s = 29;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA27_61 = input.LA(1);

                        s = -1;
                        if ( ((LA27_61 >= '0' && LA27_61 <= '9')) ) {s = 62;}

                        else if ( ((LA27_61 >= '\u0000' && LA27_61 <= '\b')||(LA27_61 >= '\u000B' && LA27_61 <= '\f')||(LA27_61 >= '\u000E' && LA27_61 <= '\u001F')||(LA27_61 >= '#' && LA27_61 <= '%')||LA27_61==','||LA27_61=='.'||(LA27_61 >= ';' && LA27_61 <= '>')||(LA27_61 >= '@' && LA27_61 <= 'Z')||(LA27_61 >= '_' && LA27_61 <= 'z')||(LA27_61 >= '\u007F' && LA27_61 <= '\u2FFF')||(LA27_61 >= '\u3001' && LA27_61 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_61=='\\') ) {s = 37;}

                        else if ( (LA27_61=='*'||LA27_61=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA27_59 = input.LA(1);

                        s = -1;
                        if ( ((LA27_59 >= '0' && LA27_59 <= '9')) ) {s = 62;}

                        else if ( (LA27_59=='.') ) {s = 60;}

                        else if ( (LA27_59=='-'||LA27_59=='/') ) {s = 46;}

                        else if ( ((LA27_59 >= '\u0000' && LA27_59 <= '\b')||(LA27_59 >= '\u000B' && LA27_59 <= '\f')||(LA27_59 >= '\u000E' && LA27_59 <= '\u001F')||(LA27_59 >= '#' && LA27_59 <= '%')||LA27_59==','||(LA27_59 >= ';' && LA27_59 <= '>')||(LA27_59 >= '@' && LA27_59 <= 'Z')||(LA27_59 >= '_' && LA27_59 <= 'z')||(LA27_59 >= '\u007F' && LA27_59 <= '\u2FFF')||(LA27_59 >= '\u3001' && LA27_59 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_59=='\\') ) {s = 37;}

                        else if ( (LA27_59=='*'||LA27_59=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA27_63 = input.LA(1);

                        s = -1;
                        if ( ((LA27_63 >= '0' && LA27_63 <= '9')) ) {s = 64;}

                        else if ( ((LA27_63 >= '\u0000' && LA27_63 <= '\b')||(LA27_63 >= '\u000B' && LA27_63 <= '\f')||(LA27_63 >= '\u000E' && LA27_63 <= '\u001F')||(LA27_63 >= '#' && LA27_63 <= '%')||LA27_63==','||LA27_63=='.'||(LA27_63 >= ';' && LA27_63 <= '>')||(LA27_63 >= '@' && LA27_63 <= 'Z')||(LA27_63 >= '_' && LA27_63 <= 'z')||(LA27_63 >= '\u007F' && LA27_63 <= '\u2FFF')||(LA27_63 >= '\u3001' && LA27_63 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_63=='\\') ) {s = 37;}

                        else if ( (LA27_63=='*'||LA27_63=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA27_65 = input.LA(1);

                        s = -1;
                        if ( ((LA27_65 >= '0' && LA27_65 <= '9')) ) {s = 66;}

                        else if ( ((LA27_65 >= '\u0000' && LA27_65 <= '\b')||(LA27_65 >= '\u000B' && LA27_65 <= '\f')||(LA27_65 >= '\u000E' && LA27_65 <= '\u001F')||(LA27_65 >= '#' && LA27_65 <= '%')||LA27_65==','||LA27_65=='.'||(LA27_65 >= ';' && LA27_65 <= '>')||(LA27_65 >= '@' && LA27_65 <= 'Z')||(LA27_65 >= '_' && LA27_65 <= 'z')||(LA27_65 >= '\u007F' && LA27_65 <= '\u2FFF')||(LA27_65 >= '\u3001' && LA27_65 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_65=='\\') ) {s = 37;}

                        else if ( (LA27_65=='*'||LA27_65=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA27_55 = input.LA(1);

                        s = -1;
                        if ( (LA27_55=='.') ) {s = 61;}

                        else if ( ((LA27_55 >= '0' && LA27_55 <= '9')) ) {s = 55;}

                        else if ( ((LA27_55 >= '\u0000' && LA27_55 <= '\b')||(LA27_55 >= '\u000B' && LA27_55 <= '\f')||(LA27_55 >= '\u000E' && LA27_55 <= '\u001F')||(LA27_55 >= '#' && LA27_55 <= '%')||LA27_55==','||(LA27_55 >= ';' && LA27_55 <= '>')||(LA27_55 >= '@' && LA27_55 <= 'Z')||(LA27_55 >= '_' && LA27_55 <= 'z')||(LA27_55 >= '\u007F' && LA27_55 <= '\u2FFF')||(LA27_55 >= '\u3001' && LA27_55 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_55=='\\') ) {s = 37;}

                        else if ( (LA27_55=='*'||LA27_55=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA27_18 = input.LA(1);

                        s = -1;
                        if ( (LA27_18=='N'||LA27_18=='n') ) {s = 38;}

                        else if ( ((LA27_18 >= '\u0000' && LA27_18 <= '\b')||(LA27_18 >= '\u000B' && LA27_18 <= '\f')||(LA27_18 >= '\u000E' && LA27_18 <= '\u001F')||(LA27_18 >= '#' && LA27_18 <= '%')||LA27_18==','||LA27_18=='.'||(LA27_18 >= '0' && LA27_18 <= '9')||(LA27_18 >= ';' && LA27_18 <= '>')||(LA27_18 >= '@' && LA27_18 <= 'M')||(LA27_18 >= 'O' && LA27_18 <= 'Z')||(LA27_18 >= '_' && LA27_18 <= 'm')||(LA27_18 >= 'o' && LA27_18 <= 'z')||(LA27_18 >= '\u007F' && LA27_18 <= '\u2FFF')||(LA27_18 >= '\u3001' && LA27_18 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_18=='\\') ) {s = 37;}

                        else if ( (LA27_18=='*'||LA27_18=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA27_52 = input.LA(1);

                        s = -1;
                        if ( ((LA27_52 >= '\u0000' && LA27_52 <= '\b')||(LA27_52 >= '\u000B' && LA27_52 <= '\f')||(LA27_52 >= '\u000E' && LA27_52 <= '\u001F')||(LA27_52 >= '#' && LA27_52 <= '%')||LA27_52==','||LA27_52=='.'||(LA27_52 >= '0' && LA27_52 <= '9')||(LA27_52 >= ';' && LA27_52 <= '>')||(LA27_52 >= '@' && LA27_52 <= 'Z')||(LA27_52 >= '_' && LA27_52 <= 'z')||(LA27_52 >= '\u007F' && LA27_52 <= '\u2FFF')||(LA27_52 >= '\u3001' && LA27_52 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_52=='\\') ) {s = 37;}

                        else if ( (LA27_52=='*'||LA27_52=='?') ) {s = 29;}

                        else s = 57;

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA27_66 = input.LA(1);

                        s = -1;
                        if ( ((LA27_66 >= '\u0000' && LA27_66 <= '\b')||(LA27_66 >= '\u000B' && LA27_66 <= '\f')||(LA27_66 >= '\u000E' && LA27_66 <= '\u001F')||(LA27_66 >= '#' && LA27_66 <= '%')||LA27_66==','||LA27_66=='.'||(LA27_66 >= '0' && LA27_66 <= '9')||(LA27_66 >= ';' && LA27_66 <= '>')||(LA27_66 >= '@' && LA27_66 <= 'Z')||(LA27_66 >= '_' && LA27_66 <= 'z')||(LA27_66 >= '\u007F' && LA27_66 <= '\u2FFF')||(LA27_66 >= '\u3001' && LA27_66 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_66=='\\') ) {s = 37;}

                        else if ( (LA27_66=='*'||LA27_66=='?') ) {s = 29;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA27_45 = input.LA(1);

                        s = -1;
                        if ( (LA27_45=='.') ) {s = 43;}

                        else if ( ((LA27_45 >= '0' && LA27_45 <= '9')) ) {s = 55;}

                        else if ( (LA27_45=='-'||LA27_45=='/') ) {s = 46;}

                        else if ( ((LA27_45 >= '\u0000' && LA27_45 <= '\b')||(LA27_45 >= '\u000B' && LA27_45 <= '\f')||(LA27_45 >= '\u000E' && LA27_45 <= '\u001F')||(LA27_45 >= '#' && LA27_45 <= '%')||LA27_45==','||(LA27_45 >= ';' && LA27_45 <= '>')||(LA27_45 >= '@' && LA27_45 <= 'Z')||(LA27_45 >= '_' && LA27_45 <= 'z')||(LA27_45 >= '\u007F' && LA27_45 <= '\u2FFF')||(LA27_45 >= '\u3001' && LA27_45 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_45=='\\') ) {s = 37;}

                        else if ( (LA27_45=='*'||LA27_45=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA27_10 = input.LA(1);

                        s = -1;
                        if ( (LA27_10=='?') ) {s = 10;}

                        else if ( ((LA27_10 >= '\u0000' && LA27_10 <= '\b')||(LA27_10 >= '\u000B' && LA27_10 <= '\f')||(LA27_10 >= '\u000E' && LA27_10 <= '\u001F')||(LA27_10 >= '#' && LA27_10 <= '%')||LA27_10==','||LA27_10=='.'||(LA27_10 >= '0' && LA27_10 <= '9')||(LA27_10 >= ';' && LA27_10 <= '>')||(LA27_10 >= '@' && LA27_10 <= 'Z')||LA27_10=='\\'||(LA27_10 >= '_' && LA27_10 <= 'z')||(LA27_10 >= '\u007F' && LA27_10 <= '\u2FFF')||(LA27_10 >= '\u3001' && LA27_10 <= '\uFFFF')) ) {s = 29;}

                        else s = 30;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA27_54 = input.LA(1);

                        s = -1;
                        if ( ((LA27_54 >= '0' && LA27_54 <= '9')) ) {s = 59;}

                        else if ( (LA27_54=='.') ) {s = 60;}

                        else if ( (LA27_54=='-'||LA27_54=='/') ) {s = 46;}

                        else if ( ((LA27_54 >= '\u0000' && LA27_54 <= '\b')||(LA27_54 >= '\u000B' && LA27_54 <= '\f')||(LA27_54 >= '\u000E' && LA27_54 <= '\u001F')||(LA27_54 >= '#' && LA27_54 <= '%')||LA27_54==','||(LA27_54 >= ';' && LA27_54 <= '>')||(LA27_54 >= '@' && LA27_54 <= 'Z')||(LA27_54 >= '_' && LA27_54 <= 'z')||(LA27_54 >= '\u007F' && LA27_54 <= '\u2FFF')||(LA27_54 >= '\u3001' && LA27_54 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_54=='\\') ) {s = 37;}

                        else if ( (LA27_54=='*'||LA27_54=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA27_15 = input.LA(1);

                        s = -1;
                        if ( ((LA27_15 >= '\u0000' && LA27_15 <= '!')||(LA27_15 >= '#' && LA27_15 <= ')')||(LA27_15 >= '+' && LA27_15 <= '>')||(LA27_15 >= '@' && LA27_15 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA27_15=='*'||LA27_15=='?') ) {s = 33;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA27_40 = input.LA(1);

                        s = -1;
                        if ( (LA27_40=='T'||LA27_40=='t') ) {s = 52;}

                        else if ( ((LA27_40 >= '\u0000' && LA27_40 <= '\b')||(LA27_40 >= '\u000B' && LA27_40 <= '\f')||(LA27_40 >= '\u000E' && LA27_40 <= '\u001F')||(LA27_40 >= '#' && LA27_40 <= '%')||LA27_40==','||LA27_40=='.'||(LA27_40 >= '0' && LA27_40 <= '9')||(LA27_40 >= ';' && LA27_40 <= '>')||(LA27_40 >= '@' && LA27_40 <= 'S')||(LA27_40 >= 'U' && LA27_40 <= 'Z')||(LA27_40 >= '_' && LA27_40 <= 's')||(LA27_40 >= 'u' && LA27_40 <= 'z')||(LA27_40 >= '\u007F' && LA27_40 <= '\u2FFF')||(LA27_40 >= '\u3001' && LA27_40 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_40=='\\') ) {s = 37;}

                        else if ( (LA27_40=='*'||LA27_40=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA27_51 = input.LA(1);

                        s = -1;
                        if ( ((LA27_51 >= '\u0000' && LA27_51 <= '\b')||(LA27_51 >= '\u000B' && LA27_51 <= '\f')||(LA27_51 >= '\u000E' && LA27_51 <= '\u001F')||(LA27_51 >= '#' && LA27_51 <= '%')||LA27_51==','||LA27_51=='.'||(LA27_51 >= '0' && LA27_51 <= '9')||(LA27_51 >= ';' && LA27_51 <= '>')||(LA27_51 >= '@' && LA27_51 <= 'Z')||(LA27_51 >= '_' && LA27_51 <= 'z')||(LA27_51 >= '\u007F' && LA27_51 <= '\u2FFF')||(LA27_51 >= '\u3001' && LA27_51 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_51=='\\') ) {s = 37;}

                        else if ( (LA27_51=='*'||LA27_51=='?') ) {s = 29;}

                        else s = 19;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA27_0 = input.LA(1);

                        s = -1;
                        if ( (LA27_0=='/') ) {s = 1;}

                        else if ( (LA27_0=='(') ) {s = 2;}

                        else if ( (LA27_0==')') ) {s = 3;}

                        else if ( (LA27_0=='[') ) {s = 4;}

                        else if ( (LA27_0==']') ) {s = 5;}

                        else if ( (LA27_0==':') ) {s = 6;}

                        else if ( (LA27_0=='+') ) {s = 7;}

                        else if ( (LA27_0=='!'||LA27_0=='-') ) {s = 8;}

                        else if ( (LA27_0=='*') ) {s = 9;}

                        else if ( (LA27_0=='?') ) {s = 10;}

                        else if ( (LA27_0=='{') ) {s = 11;}

                        else if ( (LA27_0=='}') ) {s = 12;}

                        else if ( (LA27_0=='^') ) {s = 13;}

                        else if ( (LA27_0=='~') ) {s = 14;}

                        else if ( (LA27_0=='\"') ) {s = 15;}

                        else if ( (LA27_0=='\'') ) {s = 16;}

                        else if ( (LA27_0=='T') ) {s = 17;}

                        else if ( (LA27_0=='A'||LA27_0=='a') ) {s = 18;}

                        else if ( (LA27_0=='&') ) {s = 19;}

                        else if ( (LA27_0=='O'||LA27_0=='o') ) {s = 20;}

                        else if ( (LA27_0=='|') ) {s = 21;}

                        else if ( (LA27_0=='n') ) {s = 22;}

                        else if ( (LA27_0=='N') ) {s = 23;}

                        else if ( ((LA27_0 >= '\t' && LA27_0 <= '\n')||LA27_0=='\r'||LA27_0==' '||LA27_0=='\u3000') ) {s = 24;}

                        else if ( ((LA27_0 >= '0' && LA27_0 <= '9')) ) {s = 25;}

                        else if ( ((LA27_0 >= '\u0000' && LA27_0 <= '\b')||(LA27_0 >= '\u000B' && LA27_0 <= '\f')||(LA27_0 >= '\u000E' && LA27_0 <= '\u001F')||(LA27_0 >= '#' && LA27_0 <= '%')||LA27_0==','||LA27_0=='.'||(LA27_0 >= ';' && LA27_0 <= '>')||LA27_0=='@'||(LA27_0 >= 'B' && LA27_0 <= 'M')||(LA27_0 >= 'P' && LA27_0 <= 'S')||(LA27_0 >= 'U' && LA27_0 <= 'Z')||(LA27_0 >= '_' && LA27_0 <= '`')||(LA27_0 >= 'b' && LA27_0 <= 'm')||(LA27_0 >= 'p' && LA27_0 <= 'z')||(LA27_0 >= '\u007F' && LA27_0 <= '\u2FFF')||(LA27_0 >= '\u3001' && LA27_0 <= '\uFFFF')) ) {s = 26;}

                        else if ( (LA27_0=='\\') ) {s = 27;}

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA27_9 = input.LA(1);

                        s = -1;
                        if ( ((LA27_9 >= '\u0000' && LA27_9 <= '\b')||(LA27_9 >= '\u000B' && LA27_9 <= '\f')||(LA27_9 >= '\u000E' && LA27_9 <= '\u001F')||(LA27_9 >= '#' && LA27_9 <= '%')||LA27_9==','||LA27_9=='.'||(LA27_9 >= '0' && LA27_9 <= '9')||(LA27_9 >= ';' && LA27_9 <= '>')||(LA27_9 >= '@' && LA27_9 <= 'Z')||LA27_9=='\\'||(LA27_9 >= '_' && LA27_9 <= 'z')||(LA27_9 >= '\u007F' && LA27_9 <= '\u2FFF')||(LA27_9 >= '\u3001' && LA27_9 <= '\uFFFF')) ) {s = 29;}

                        else s = 28;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA27_41 = input.LA(1);

                        s = -1;
                        if ( (LA27_41=='A'||LA27_41=='a') ) {s = 53;}

                        else if ( ((LA27_41 >= '\u0000' && LA27_41 <= '\b')||(LA27_41 >= '\u000B' && LA27_41 <= '\f')||(LA27_41 >= '\u000E' && LA27_41 <= '\u001F')||(LA27_41 >= '#' && LA27_41 <= '%')||LA27_41==','||LA27_41=='.'||(LA27_41 >= '0' && LA27_41 <= '9')||(LA27_41 >= ';' && LA27_41 <= '>')||LA27_41=='@'||(LA27_41 >= 'B' && LA27_41 <= 'Z')||(LA27_41 >= '_' && LA27_41 <= '`')||(LA27_41 >= 'b' && LA27_41 <= 'z')||(LA27_41 >= '\u007F' && LA27_41 <= '\u2FFF')||(LA27_41 >= '\u3001' && LA27_41 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_41=='\\') ) {s = 37;}

                        else if ( (LA27_41=='*'||LA27_41=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA27_53 = input.LA(1);

                        s = -1;
                        if ( (LA27_53=='R'||LA27_53=='r') ) {s = 58;}

                        else if ( ((LA27_53 >= '\u0000' && LA27_53 <= '\b')||(LA27_53 >= '\u000B' && LA27_53 <= '\f')||(LA27_53 >= '\u000E' && LA27_53 <= '\u001F')||(LA27_53 >= '#' && LA27_53 <= '%')||LA27_53==','||LA27_53=='.'||(LA27_53 >= '0' && LA27_53 <= '9')||(LA27_53 >= ';' && LA27_53 <= '>')||(LA27_53 >= '@' && LA27_53 <= 'Q')||(LA27_53 >= 'S' && LA27_53 <= 'Z')||(LA27_53 >= '_' && LA27_53 <= 'q')||(LA27_53 >= 's' && LA27_53 <= 'z')||(LA27_53 >= '\u007F' && LA27_53 <= '\u2FFF')||(LA27_53 >= '\u3001' && LA27_53 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_53=='\\') ) {s = 37;}

                        else if ( (LA27_53=='*'||LA27_53=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 27 : 
                        int LA27_60 = input.LA(1);

                        s = -1;
                        if ( ((LA27_60 >= '0' && LA27_60 <= '9')) ) {s = 63;}

                        else if ( ((LA27_60 >= '\u0000' && LA27_60 <= '\b')||(LA27_60 >= '\u000B' && LA27_60 <= '\f')||(LA27_60 >= '\u000E' && LA27_60 <= '\u001F')||(LA27_60 >= '#' && LA27_60 <= '%')||LA27_60==','||LA27_60=='.'||(LA27_60 >= ';' && LA27_60 <= '>')||(LA27_60 >= '@' && LA27_60 <= 'Z')||(LA27_60 >= '_' && LA27_60 <= 'z')||(LA27_60 >= '\u007F' && LA27_60 <= '\u2FFF')||(LA27_60 >= '\u3001' && LA27_60 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_60=='\\') ) {s = 37;}

                        else if ( (LA27_60=='*'||LA27_60=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 28 : 
                        int LA27_50 = input.LA(1);

                        s = -1;
                        if ( ((LA27_50 >= '\u0000' && LA27_50 <= '\b')||(LA27_50 >= '\u000B' && LA27_50 <= '\f')||(LA27_50 >= '\u000E' && LA27_50 <= '\u001F')||(LA27_50 >= '#' && LA27_50 <= '%')||LA27_50==','||LA27_50=='.'||(LA27_50 >= '0' && LA27_50 <= '9')||(LA27_50 >= ';' && LA27_50 <= '>')||(LA27_50 >= '@' && LA27_50 <= 'Z')||(LA27_50 >= '_' && LA27_50 <= 'z')||(LA27_50 >= '\u007F' && LA27_50 <= '\u2FFF')||(LA27_50 >= '\u3001' && LA27_50 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_50=='\\') ) {s = 37;}

                        else if ( (LA27_50=='*'||LA27_50=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 29 : 
                        int LA27_39 = input.LA(1);

                        s = -1;
                        if ( ((LA27_39 >= '\u0000' && LA27_39 <= '\b')||(LA27_39 >= '\u000B' && LA27_39 <= '\f')||(LA27_39 >= '\u000E' && LA27_39 <= '\u001F')||(LA27_39 >= '#' && LA27_39 <= '%')||LA27_39==','||LA27_39=='.'||(LA27_39 >= '0' && LA27_39 <= '9')||(LA27_39 >= ';' && LA27_39 <= '>')||(LA27_39 >= '@' && LA27_39 <= 'Z')||(LA27_39 >= '_' && LA27_39 <= 'z')||(LA27_39 >= '\u007F' && LA27_39 <= '\u2FFF')||(LA27_39 >= '\u3001' && LA27_39 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_39=='\\') ) {s = 37;}

                        else if ( (LA27_39=='*'||LA27_39=='?') ) {s = 29;}

                        else s = 21;

                        if ( s>=0 ) return s;
                        break;

                    case 30 : 
                        int LA27_62 = input.LA(1);

                        s = -1;
                        if ( ((LA27_62 >= '0' && LA27_62 <= '9')) ) {s = 62;}

                        else if ( ((LA27_62 >= '\u0000' && LA27_62 <= '\b')||(LA27_62 >= '\u000B' && LA27_62 <= '\f')||(LA27_62 >= '\u000E' && LA27_62 <= '\u001F')||(LA27_62 >= '#' && LA27_62 <= '%')||LA27_62==','||LA27_62=='.'||(LA27_62 >= ';' && LA27_62 <= '>')||(LA27_62 >= '@' && LA27_62 <= 'Z')||(LA27_62 >= '_' && LA27_62 <= 'z')||(LA27_62 >= '\u007F' && LA27_62 <= '\u2FFF')||(LA27_62 >= '\u3001' && LA27_62 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_62=='\\') ) {s = 37;}

                        else if ( (LA27_62=='*'||LA27_62=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 31 : 
                        int LA27_25 = input.LA(1);

                        s = -1;
                        if ( (LA27_25=='.') ) {s = 43;}

                        else if ( ((LA27_25 >= '0' && LA27_25 <= '9')) ) {s = 45;}

                        else if ( (LA27_25=='-'||LA27_25=='/') ) {s = 46;}

                        else if ( ((LA27_25 >= '\u0000' && LA27_25 <= '\b')||(LA27_25 >= '\u000B' && LA27_25 <= '\f')||(LA27_25 >= '\u000E' && LA27_25 <= '\u001F')||(LA27_25 >= '#' && LA27_25 <= '%')||LA27_25==','||(LA27_25 >= ';' && LA27_25 <= '>')||(LA27_25 >= '@' && LA27_25 <= 'Z')||(LA27_25 >= '_' && LA27_25 <= 'z')||(LA27_25 >= '\u007F' && LA27_25 <= '\u2FFF')||(LA27_25 >= '\u3001' && LA27_25 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_25=='\\') ) {s = 37;}

                        else if ( (LA27_25=='*'||LA27_25=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 32 : 
                        int LA27_47 = input.LA(1);

                        s = -1;
                        if ( ((LA27_47 >= '\u0000' && LA27_47 <= '\b')||(LA27_47 >= '\u000B' && LA27_47 <= '\f')||(LA27_47 >= '\u000E' && LA27_47 <= '\u001F')||(LA27_47 >= '#' && LA27_47 <= '%')||LA27_47==','||LA27_47=='.'||(LA27_47 >= '0' && LA27_47 <= '9')||(LA27_47 >= ';' && LA27_47 <= '>')||(LA27_47 >= '@' && LA27_47 <= 'Z')||(LA27_47 >= '_' && LA27_47 <= 'z')||(LA27_47 >= '\u007F' && LA27_47 <= '\u2FFF')||(LA27_47 >= '\u3001' && LA27_47 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_47=='\\') ) {s = 37;}

                        else if ( (LA27_47=='*'||LA27_47=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 33 : 
                        int LA27_20 = input.LA(1);

                        s = -1;
                        if ( (LA27_20=='R'||LA27_20=='r') ) {s = 39;}

                        else if ( ((LA27_20 >= '\u0000' && LA27_20 <= '\b')||(LA27_20 >= '\u000B' && LA27_20 <= '\f')||(LA27_20 >= '\u000E' && LA27_20 <= '\u001F')||(LA27_20 >= '#' && LA27_20 <= '%')||LA27_20==','||LA27_20=='.'||(LA27_20 >= '0' && LA27_20 <= '9')||(LA27_20 >= ';' && LA27_20 <= '>')||(LA27_20 >= '@' && LA27_20 <= 'Q')||(LA27_20 >= 'S' && LA27_20 <= 'Z')||(LA27_20 >= '_' && LA27_20 <= 'q')||(LA27_20 >= 's' && LA27_20 <= 'z')||(LA27_20 >= '\u007F' && LA27_20 <= '\u2FFF')||(LA27_20 >= '\u3001' && LA27_20 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_20=='\\') ) {s = 37;}

                        else if ( (LA27_20=='*'||LA27_20=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 34 : 
                        int LA27_43 = input.LA(1);

                        s = -1;
                        if ( ((LA27_43 >= '0' && LA27_43 <= '9')) ) {s = 54;}

                        else if ( ((LA27_43 >= '\u0000' && LA27_43 <= '\b')||(LA27_43 >= '\u000B' && LA27_43 <= '\f')||(LA27_43 >= '\u000E' && LA27_43 <= '\u001F')||(LA27_43 >= '#' && LA27_43 <= '%')||LA27_43==','||LA27_43=='.'||(LA27_43 >= ';' && LA27_43 <= '>')||(LA27_43 >= '@' && LA27_43 <= 'Z')||(LA27_43 >= '_' && LA27_43 <= 'z')||(LA27_43 >= '\u007F' && LA27_43 <= '\u2FFF')||(LA27_43 >= '\u3001' && LA27_43 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_43=='\\') ) {s = 37;}

                        else if ( (LA27_43=='*'||LA27_43=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 35 : 
                        int LA27_32 = input.LA(1);

                        s = -1;
                        if ( (LA27_32=='\"') ) {s = 48;}

                        else if ( ((LA27_32 >= '\u0000' && LA27_32 <= '!')||(LA27_32 >= '#' && LA27_32 <= ')')||(LA27_32 >= '+' && LA27_32 <= '>')||(LA27_32 >= '@' && LA27_32 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA27_32=='*'||LA27_32=='?') ) {s = 33;}

                        if ( s>=0 ) return s;
                        break;

                    case 36 : 
                        int LA27_64 = input.LA(1);

                        s = -1;
                        if ( ((LA27_64 >= '0' && LA27_64 <= '9')) ) {s = 65;}

                        else if ( ((LA27_64 >= '\u0000' && LA27_64 <= '\b')||(LA27_64 >= '\u000B' && LA27_64 <= '\f')||(LA27_64 >= '\u000E' && LA27_64 <= '\u001F')||(LA27_64 >= '#' && LA27_64 <= '%')||LA27_64==','||LA27_64=='.'||(LA27_64 >= ';' && LA27_64 <= '>')||(LA27_64 >= '@' && LA27_64 <= 'Z')||(LA27_64 >= '_' && LA27_64 <= 'z')||(LA27_64 >= '\u007F' && LA27_64 <= '\u2FFF')||(LA27_64 >= '\u3001' && LA27_64 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_64=='\\') ) {s = 37;}

                        else if ( (LA27_64=='*'||LA27_64=='?') ) {s = 29;}

                        else s = 46;

                        if ( s>=0 ) return s;
                        break;

                    case 37 : 
                        int LA27_34 = input.LA(1);

                        s = -1;
                        if ( ((LA27_34 >= '\u0000' && LA27_34 <= '\b')||(LA27_34 >= '\u000B' && LA27_34 <= '\f')||(LA27_34 >= '\u000E' && LA27_34 <= '\u001F')||(LA27_34 >= '#' && LA27_34 <= '%')||LA27_34==','||LA27_34=='.'||(LA27_34 >= '0' && LA27_34 <= '9')||(LA27_34 >= ';' && LA27_34 <= '>')||(LA27_34 >= '@' && LA27_34 <= 'Z')||(LA27_34 >= '_' && LA27_34 <= 'z')||(LA27_34 >= '\u007F' && LA27_34 <= '\u2FFF')||(LA27_34 >= '\u3001' && LA27_34 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA27_34=='\\') ) {s = 37;}

                        else if ( (LA27_34=='*'||LA27_34=='?') ) {s = 29;}

                        else s = 49;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 27, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}