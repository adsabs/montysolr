// $ANTLR 3.4 StandardLuceneGrammar.g 2011-10-31 18:01:17

   package org.apache.lucene.queryParser.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class StandardLuceneGrammarLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__55=55;
    public static final int ADDED=4;
    public static final int AMPER=5;
    public static final int AND=6;
    public static final int ATOM=7;
    public static final int BOOST=8;
    public static final int CARAT=9;
    public static final int CLAUSE=10;
    public static final int COLON=11;
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
    public static final int MULTIATOM=22;
    public static final int MULTITERM=23;
    public static final int NEAR=24;
    public static final int NORMAL_CHAR=25;
    public static final int NOT=26;
    public static final int NUCLEUS=27;
    public static final int NUMBER=28;
    public static final int OPERATOR=29;
    public static final int OR=30;
    public static final int PHRASE=31;
    public static final int PHRASE_ANYTHING=32;
    public static final int PLUS=33;
    public static final int QANYTHING=34;
    public static final int QMARK=35;
    public static final int QNORMAL=36;
    public static final int QPHRASE=37;
    public static final int QPHRASETRUNC=38;
    public static final int QRANGEEX=39;
    public static final int QRANGEIN=40;
    public static final int QTRUNCATED=41;
    public static final int RBRACK=42;
    public static final int RCURLY=43;
    public static final int RPAREN=44;
    public static final int SQUOTE=45;
    public static final int STAR=46;
    public static final int TERM_NORMAL=47;
    public static final int TERM_TRUNCATED=48;
    public static final int TILDE=49;
    public static final int TMODIFIER=50;
    public static final int TO=51;
    public static final int VALUE=52;
    public static final int VBAR=53;
    public static final int WS=54;

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

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
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
    // $ANTLR end "T__55"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // StandardLuceneGrammar.g:231:9: ( '(' )
            // StandardLuceneGrammar.g:231:11: '('
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
            // StandardLuceneGrammar.g:233:9: ( ')' )
            // StandardLuceneGrammar.g:233:11: ')'
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
            // StandardLuceneGrammar.g:235:9: ( '[' )
            // StandardLuceneGrammar.g:235:11: '['
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
            // StandardLuceneGrammar.g:237:9: ( ']' )
            // StandardLuceneGrammar.g:237:11: ']'
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
            // StandardLuceneGrammar.g:239:9: ( ':' )
            // StandardLuceneGrammar.g:239:11: ':'
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
            // StandardLuceneGrammar.g:241:7: ( '+' )
            // StandardLuceneGrammar.g:241:9: '+'
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
            // StandardLuceneGrammar.g:243:7: ( ( '-' | '\\!' ) )
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
            // StandardLuceneGrammar.g:245:7: ( '*' )
            // StandardLuceneGrammar.g:245:9: '*'
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
            // StandardLuceneGrammar.g:247:8: ( '?' )
            // StandardLuceneGrammar.g:247:10: '?'
            {
            match('?'); 

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
            // StandardLuceneGrammar.g:249:16: ( '|' )
            // StandardLuceneGrammar.g:249:18: '|'
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
            // StandardLuceneGrammar.g:251:16: ( '&' )
            // StandardLuceneGrammar.g:251:18: '&'
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
            // StandardLuceneGrammar.g:253:9: ( '{' )
            // StandardLuceneGrammar.g:253:11: '{'
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
            // StandardLuceneGrammar.g:255:9: ( '}' )
            // StandardLuceneGrammar.g:255:11: '}'
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
            // StandardLuceneGrammar.g:257:7: ( '^' )
            // StandardLuceneGrammar.g:257:9: '^'
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
            // StandardLuceneGrammar.g:259:7: ( '~' )
            // StandardLuceneGrammar.g:259:9: '~'
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
            // StandardLuceneGrammar.g:262:2: ( '\\\"' )
            // StandardLuceneGrammar.g:262:4: '\\\"'
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
            // StandardLuceneGrammar.g:265:2: ( '\\'' )
            // StandardLuceneGrammar.g:265:4: '\\''
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
            // StandardLuceneGrammar.g:268:18: ( '\\\\' . )
            // StandardLuceneGrammar.g:268:21: '\\\\' .
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
            // StandardLuceneGrammar.g:270:4: ( 'TO' )
            // StandardLuceneGrammar.g:270:6: 'TO'
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
            // StandardLuceneGrammar.g:273:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) ) )
            // StandardLuceneGrammar.g:273:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
            {
            // StandardLuceneGrammar.g:273:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='A'||LA2_0=='a') ) {
                alt2=1;
            }
            else if ( (LA2_0=='&') ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }
            switch (alt2) {
                case 1 :
                    // StandardLuceneGrammar.g:273:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
                    // StandardLuceneGrammar.g:273:48: ( AMPER ( AMPER )? )
                    {
                    // StandardLuceneGrammar.g:273:48: ( AMPER ( AMPER )? )
                    // StandardLuceneGrammar.g:273:49: AMPER ( AMPER )?
                    {
                    mAMPER(); 


                    // StandardLuceneGrammar.g:273:55: ( AMPER )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0=='&') ) {
                        alt1=1;
                    }
                    switch (alt1) {
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
            // StandardLuceneGrammar.g:274:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) ) )
            // StandardLuceneGrammar.g:274:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
            {
            // StandardLuceneGrammar.g:274:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='O'||LA4_0=='o') ) {
                alt4=1;
            }
            else if ( (LA4_0=='|') ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // StandardLuceneGrammar.g:274:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
                    // StandardLuceneGrammar.g:274:34: ( VBAR ( VBAR )? )
                    {
                    // StandardLuceneGrammar.g:274:34: ( VBAR ( VBAR )? )
                    // StandardLuceneGrammar.g:274:35: VBAR ( VBAR )?
                    {
                    mVBAR(); 


                    // StandardLuceneGrammar.g:274:40: ( VBAR )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0=='|') ) {
                        alt3=1;
                    }
                    switch (alt3) {
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
            // StandardLuceneGrammar.g:275:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
            // StandardLuceneGrammar.g:275:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
            // StandardLuceneGrammar.g:276:7: ( ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' ) )
            // StandardLuceneGrammar.g:276:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            {
            // StandardLuceneGrammar.g:276:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='n') ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1=='E'||LA5_1=='e') ) {
                    alt5=1;
                }
                else {
                    alt5=2;
                }
            }
            else if ( (LA5_0=='N') ) {
                alt5=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // StandardLuceneGrammar.g:276:10: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
                    // StandardLuceneGrammar.g:276:60: 'n'
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
            // StandardLuceneGrammar.g:279:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // StandardLuceneGrammar.g:279:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
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
            // StandardLuceneGrammar.g:288:13: ( '0' .. '9' )
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
            // StandardLuceneGrammar.g:290:23: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' ) )
            // StandardLuceneGrammar.g:290:25: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' )
            {
            if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\u001F')||(input.LA(1) >= '#' && input.LA(1) <= '%')||input.LA(1)==','||input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= ';' && input.LA(1) <= '>')||(input.LA(1) >= '@' && input.LA(1) <= 'Z')||(input.LA(1) >= '_' && input.LA(1) <= 'z')||(input.LA(1) >= '\u007F' && input.LA(1) <= '\uFFFF') ) {
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
            // StandardLuceneGrammar.g:299:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // StandardLuceneGrammar.g:300:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // StandardLuceneGrammar.g:300:2: ( INT )+
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


            // StandardLuceneGrammar.g:300:7: ( '.' ( INT )+ )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='.') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // StandardLuceneGrammar.g:300:8: '.' ( INT )+
                    {
                    match('.'); 

                    // StandardLuceneGrammar.g:300:12: ( INT )+
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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "TERM_NORMAL"
    public final void mTERM_NORMAL() throws RecognitionException {
        try {
            int _type = TERM_NORMAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // StandardLuceneGrammar.g:305:2: ( ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )* )
            // StandardLuceneGrammar.g:306:2: ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )*
            {
            // StandardLuceneGrammar.g:306:2: ( NORMAL_CHAR | ESC_CHAR )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( ((LA9_0 >= '\u0000' && LA9_0 <= '\b')||(LA9_0 >= '\u000B' && LA9_0 <= '\f')||(LA9_0 >= '\u000E' && LA9_0 <= '\u001F')||(LA9_0 >= '#' && LA9_0 <= '%')||LA9_0==','||LA9_0=='.'||(LA9_0 >= '0' && LA9_0 <= '9')||(LA9_0 >= ';' && LA9_0 <= '>')||(LA9_0 >= '@' && LA9_0 <= 'Z')||(LA9_0 >= '_' && LA9_0 <= 'z')||(LA9_0 >= '\u007F' && LA9_0 <= '\uFFFF')) ) {
                alt9=1;
            }
            else if ( (LA9_0=='\\') ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // StandardLuceneGrammar.g:306:4: NORMAL_CHAR
                    {
                    mNORMAL_CHAR(); 


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:306:18: ESC_CHAR
                    {
                    mESC_CHAR(); 


                    }
                    break;

            }


            // StandardLuceneGrammar.g:306:28: ( NORMAL_CHAR | ESC_CHAR )*
            loop10:
            do {
                int alt10=3;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0 >= '\u0000' && LA10_0 <= '\b')||(LA10_0 >= '\u000B' && LA10_0 <= '\f')||(LA10_0 >= '\u000E' && LA10_0 <= '\u001F')||(LA10_0 >= '#' && LA10_0 <= '%')||LA10_0==','||LA10_0=='.'||(LA10_0 >= '0' && LA10_0 <= '9')||(LA10_0 >= ';' && LA10_0 <= '>')||(LA10_0 >= '@' && LA10_0 <= 'Z')||(LA10_0 >= '_' && LA10_0 <= 'z')||(LA10_0 >= '\u007F' && LA10_0 <= '\uFFFF')) ) {
                    alt10=1;
                }
                else if ( (LA10_0=='\\') ) {
                    alt10=2;
                }


                switch (alt10) {
            	case 1 :
            	    // StandardLuceneGrammar.g:306:30: NORMAL_CHAR
            	    {
            	    mNORMAL_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // StandardLuceneGrammar.g:306:44: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;

            	default :
            	    break loop10;
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
            // StandardLuceneGrammar.g:310:15: ( ( STAR | QMARK ) ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )* ( NORMAL_CHAR | ESC_CHAR )* | ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+ ( NORMAL_CHAR | ESC_CHAR )* )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0=='*'||LA17_0=='?') ) {
                alt17=1;
            }
            else if ( ((LA17_0 >= '\u0000' && LA17_0 <= '\b')||(LA17_0 >= '\u000B' && LA17_0 <= '\f')||(LA17_0 >= '\u000E' && LA17_0 <= '\u001F')||(LA17_0 >= '#' && LA17_0 <= '%')||LA17_0==','||LA17_0=='.'||(LA17_0 >= '0' && LA17_0 <= '9')||(LA17_0 >= ';' && LA17_0 <= '>')||(LA17_0 >= '@' && LA17_0 <= 'Z')||LA17_0=='\\'||(LA17_0 >= '_' && LA17_0 <= 'z')||(LA17_0 >= '\u007F' && LA17_0 <= '\uFFFF')) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }
            switch (alt17) {
                case 1 :
                    // StandardLuceneGrammar.g:311:2: ( STAR | QMARK ) ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )* ( NORMAL_CHAR | ESC_CHAR )*
                    {
                    if ( input.LA(1)=='*'||input.LA(1)=='?' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    // StandardLuceneGrammar.g:311:15: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )*
                    loop12:
                    do {
                        int alt12=2;
                        alt12 = dfa12.predict(input);
                        switch (alt12) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:311:16: ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // StandardLuceneGrammar.g:311:16: ( NORMAL_CHAR | ESC_CHAR )+
                    	    int cnt11=0;
                    	    loop11:
                    	    do {
                    	        int alt11=3;
                    	        int LA11_0 = input.LA(1);

                    	        if ( ((LA11_0 >= '\u0000' && LA11_0 <= '\b')||(LA11_0 >= '\u000B' && LA11_0 <= '\f')||(LA11_0 >= '\u000E' && LA11_0 <= '\u001F')||(LA11_0 >= '#' && LA11_0 <= '%')||LA11_0==','||LA11_0=='.'||(LA11_0 >= '0' && LA11_0 <= '9')||(LA11_0 >= ';' && LA11_0 <= '>')||(LA11_0 >= '@' && LA11_0 <= 'Z')||(LA11_0 >= '_' && LA11_0 <= 'z')||(LA11_0 >= '\u007F' && LA11_0 <= '\uFFFF')) ) {
                    	            alt11=1;
                    	        }
                    	        else if ( (LA11_0=='\\') ) {
                    	            alt11=2;
                    	        }


                    	        switch (alt11) {
                    	    	case 1 :
                    	    	    // StandardLuceneGrammar.g:311:17: NORMAL_CHAR
                    	    	    {
                    	    	    mNORMAL_CHAR(); 


                    	    	    }
                    	    	    break;
                    	    	case 2 :
                    	    	    // StandardLuceneGrammar.g:311:29: ESC_CHAR
                    	    	    {
                    	    	    mESC_CHAR(); 


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


                    	    if ( input.LA(1)=='*'||input.LA(1)=='?' ) {
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
                    	    break loop12;
                        }
                    } while (true);


                    // StandardLuceneGrammar.g:311:55: ( NORMAL_CHAR | ESC_CHAR )*
                    loop13:
                    do {
                        int alt13=3;
                        int LA13_0 = input.LA(1);

                        if ( ((LA13_0 >= '\u0000' && LA13_0 <= '\b')||(LA13_0 >= '\u000B' && LA13_0 <= '\f')||(LA13_0 >= '\u000E' && LA13_0 <= '\u001F')||(LA13_0 >= '#' && LA13_0 <= '%')||LA13_0==','||LA13_0=='.'||(LA13_0 >= '0' && LA13_0 <= '9')||(LA13_0 >= ';' && LA13_0 <= '>')||(LA13_0 >= '@' && LA13_0 <= 'Z')||(LA13_0 >= '_' && LA13_0 <= 'z')||(LA13_0 >= '\u007F' && LA13_0 <= '\uFFFF')) ) {
                            alt13=1;
                        }
                        else if ( (LA13_0=='\\') ) {
                            alt13=2;
                        }


                        switch (alt13) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:311:56: NORMAL_CHAR
                    	    {
                    	    mNORMAL_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // StandardLuceneGrammar.g:311:68: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop13;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:312:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+ ( NORMAL_CHAR | ESC_CHAR )*
                    {
                    // StandardLuceneGrammar.g:312:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+
                    int cnt15=0;
                    loop15:
                    do {
                        int alt15=2;
                        alt15 = dfa15.predict(input);
                        switch (alt15) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:312:5: ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // StandardLuceneGrammar.g:312:5: ( NORMAL_CHAR | ESC_CHAR )+
                    	    int cnt14=0;
                    	    loop14:
                    	    do {
                    	        int alt14=3;
                    	        int LA14_0 = input.LA(1);

                    	        if ( ((LA14_0 >= '\u0000' && LA14_0 <= '\b')||(LA14_0 >= '\u000B' && LA14_0 <= '\f')||(LA14_0 >= '\u000E' && LA14_0 <= '\u001F')||(LA14_0 >= '#' && LA14_0 <= '%')||LA14_0==','||LA14_0=='.'||(LA14_0 >= '0' && LA14_0 <= '9')||(LA14_0 >= ';' && LA14_0 <= '>')||(LA14_0 >= '@' && LA14_0 <= 'Z')||(LA14_0 >= '_' && LA14_0 <= 'z')||(LA14_0 >= '\u007F' && LA14_0 <= '\uFFFF')) ) {
                    	            alt14=1;
                    	        }
                    	        else if ( (LA14_0=='\\') ) {
                    	            alt14=2;
                    	        }


                    	        switch (alt14) {
                    	    	case 1 :
                    	    	    // StandardLuceneGrammar.g:312:6: NORMAL_CHAR
                    	    	    {
                    	    	    mNORMAL_CHAR(); 


                    	    	    }
                    	    	    break;
                    	    	case 2 :
                    	    	    // StandardLuceneGrammar.g:312:18: ESC_CHAR
                    	    	    {
                    	    	    mESC_CHAR(); 


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    if ( cnt14 >= 1 ) break loop14;
                    	                EarlyExitException eee =
                    	                    new EarlyExitException(14, input);
                    	                throw eee;
                    	        }
                    	        cnt14++;
                    	    } while (true);


                    	    if ( input.LA(1)=='*'||input.LA(1)=='?' ) {
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


                    // StandardLuceneGrammar.g:312:44: ( NORMAL_CHAR | ESC_CHAR )*
                    loop16:
                    do {
                        int alt16=3;
                        int LA16_0 = input.LA(1);

                        if ( ((LA16_0 >= '\u0000' && LA16_0 <= '\b')||(LA16_0 >= '\u000B' && LA16_0 <= '\f')||(LA16_0 >= '\u000E' && LA16_0 <= '\u001F')||(LA16_0 >= '#' && LA16_0 <= '%')||LA16_0==','||LA16_0=='.'||(LA16_0 >= '0' && LA16_0 <= '9')||(LA16_0 >= ';' && LA16_0 <= '>')||(LA16_0 >= '@' && LA16_0 <= 'Z')||(LA16_0 >= '_' && LA16_0 <= 'z')||(LA16_0 >= '\u007F' && LA16_0 <= '\uFFFF')) ) {
                            alt16=1;
                        }
                        else if ( (LA16_0=='\\') ) {
                            alt16=2;
                        }


                        switch (alt16) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:312:45: NORMAL_CHAR
                    	    {
                    	    mNORMAL_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // StandardLuceneGrammar.g:312:57: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop16;
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
            // StandardLuceneGrammar.g:317:2: ( DQUOTE (~ ( '\\\"' | '\\\\\"' | '?' | '*' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:318:2: DQUOTE (~ ( '\\\"' | '\\\\\"' | '?' | '*' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:318:9: (~ ( '\\\"' | '\\\\\"' | '?' | '*' ) )+
            int cnt18=0;
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0 >= '\u0000' && LA18_0 <= '!')||(LA18_0 >= '#' && LA18_0 <= ')')||(LA18_0 >= '+' && LA18_0 <= '>')||(LA18_0 >= '@' && LA18_0 <= '\uFFFF')) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // StandardLuceneGrammar.g:318:9: ~ ( '\\\"' | '\\\\\"' | '?' | '*' )
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
            	    if ( cnt18 >= 1 ) break loop18;
                        EarlyExitException eee =
                            new EarlyExitException(18, input);
                        throw eee;
                }
                cnt18++;
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
            // StandardLuceneGrammar.g:321:17: ( DQUOTE (~ ( '\\\"' | '\\\\\"' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:322:2: DQUOTE (~ ( '\\\"' | '\\\\\"' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:322:9: (~ ( '\\\"' | '\\\\\"' ) )+
            int cnt19=0;
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0 >= '\u0000' && LA19_0 <= '!')||(LA19_0 >= '#' && LA19_0 <= '\uFFFF')) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // StandardLuceneGrammar.g:322:9: ~ ( '\\\"' | '\\\\\"' )
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
            	    if ( cnt19 >= 1 ) break loop19;
                        EarlyExitException eee =
                            new EarlyExitException(19, input);
                        throw eee;
                }
                cnt19++;
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
        // StandardLuceneGrammar.g:1:8: ( T__55 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | NEAR | WS | NUMBER | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING )
        int alt20=27;
        alt20 = dfa20.predict(input);
        switch (alt20) {
            case 1 :
                // StandardLuceneGrammar.g:1:10: T__55
                {
                mT__55(); 


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
                // StandardLuceneGrammar.g:1:141: TERM_NORMAL
                {
                mTERM_NORMAL(); 


                }
                break;
            case 25 :
                // StandardLuceneGrammar.g:1:153: TERM_TRUNCATED
                {
                mTERM_TRUNCATED(); 


                }
                break;
            case 26 :
                // StandardLuceneGrammar.g:1:168: PHRASE
                {
                mPHRASE(); 


                }
                break;
            case 27 :
                // StandardLuceneGrammar.g:1:175: PHRASE_ANYTHING
                {
                mPHRASE_ANYTHING(); 


                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    protected DFA15 dfa15 = new DFA15(this);
    protected DFA20 dfa20 = new DFA20(this);
    static final String DFA12_eotS =
        "\2\3\3\uffff\1\3";
    static final String DFA12_eofS =
        "\6\uffff";
    static final String DFA12_minS =
        "\3\0\2\uffff\1\0";
    static final String DFA12_maxS =
        "\3\uffff\2\uffff\1\uffff";
    static final String DFA12_acceptS =
        "\3\uffff\1\2\1\1\1\uffff";
    static final String DFA12_specialS =
        "\1\3\1\2\1\0\2\uffff\1\1}>";
    static final String[] DFA12_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\6\uffff\1\1\1\uffff"+
            "\1\1\1\uffff\12\1\1\uffff\4\1\1\uffff\33\1\1\uffff\1\2\2\uffff"+
            "\34\1\4\uffff\uff81\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\4\uffff\1\4\1\uffff"+
            "\1\1\1\uffff\1\1\1\uffff\12\1\1\uffff\4\1\1\4\33\1\1\uffff\1"+
            "\2\2\uffff\34\1\4\uffff\uff81\1",
            "\0\5",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\4\uffff\1\4\1\uffff"+
            "\1\1\1\uffff\1\1\1\uffff\12\1\1\uffff\4\1\1\4\33\1\1\uffff\1"+
            "\2\2\uffff\34\1\4\uffff\uff81\1"
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "()* loopback of 311:15: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_2 = input.LA(1);

                        s = -1;
                        if ( ((LA12_2 >= '\u0000' && LA12_2 <= '\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA12_5 = input.LA(1);

                        s = -1;
                        if ( ((LA12_5 >= '\u0000' && LA12_5 <= '\b')||(LA12_5 >= '\u000B' && LA12_5 <= '\f')||(LA12_5 >= '\u000E' && LA12_5 <= '\u001F')||(LA12_5 >= '#' && LA12_5 <= '%')||LA12_5==','||LA12_5=='.'||(LA12_5 >= '0' && LA12_5 <= '9')||(LA12_5 >= ';' && LA12_5 <= '>')||(LA12_5 >= '@' && LA12_5 <= 'Z')||(LA12_5 >= '_' && LA12_5 <= 'z')||(LA12_5 >= '\u007F' && LA12_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA12_5=='\\') ) {s = 2;}

                        else if ( (LA12_5=='*'||LA12_5=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA12_1 = input.LA(1);

                        s = -1;
                        if ( ((LA12_1 >= '\u0000' && LA12_1 <= '\b')||(LA12_1 >= '\u000B' && LA12_1 <= '\f')||(LA12_1 >= '\u000E' && LA12_1 <= '\u001F')||(LA12_1 >= '#' && LA12_1 <= '%')||LA12_1==','||LA12_1=='.'||(LA12_1 >= '0' && LA12_1 <= '9')||(LA12_1 >= ';' && LA12_1 <= '>')||(LA12_1 >= '@' && LA12_1 <= 'Z')||(LA12_1 >= '_' && LA12_1 <= 'z')||(LA12_1 >= '\u007F' && LA12_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA12_1=='\\') ) {s = 2;}

                        else if ( (LA12_1=='*'||LA12_1=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA12_0 = input.LA(1);

                        s = -1;
                        if ( ((LA12_0 >= '\u0000' && LA12_0 <= '\b')||(LA12_0 >= '\u000B' && LA12_0 <= '\f')||(LA12_0 >= '\u000E' && LA12_0 <= '\u001F')||(LA12_0 >= '#' && LA12_0 <= '%')||LA12_0==','||LA12_0=='.'||(LA12_0 >= '0' && LA12_0 <= '9')||(LA12_0 >= ';' && LA12_0 <= '>')||(LA12_0 >= '@' && LA12_0 <= 'Z')||(LA12_0 >= '_' && LA12_0 <= 'z')||(LA12_0 >= '\u007F' && LA12_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA12_0=='\\') ) {s = 2;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 12, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA15_eotS =
        "\2\3\3\uffff\1\3";
    static final String DFA15_eofS =
        "\6\uffff";
    static final String DFA15_minS =
        "\3\0\2\uffff\1\0";
    static final String DFA15_maxS =
        "\3\uffff\2\uffff\1\uffff";
    static final String DFA15_acceptS =
        "\3\uffff\1\2\1\1\1\uffff";
    static final String DFA15_specialS =
        "\1\1\1\3\1\0\2\uffff\1\2}>";
    static final String[] DFA15_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\6\uffff\1\1\1\uffff"+
            "\1\1\1\uffff\12\1\1\uffff\4\1\1\uffff\33\1\1\uffff\1\2\2\uffff"+
            "\34\1\4\uffff\uff81\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\4\uffff\1\4\1\uffff"+
            "\1\1\1\uffff\1\1\1\uffff\12\1\1\uffff\4\1\1\4\33\1\1\uffff\1"+
            "\2\2\uffff\34\1\4\uffff\uff81\1",
            "\0\5",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\3\1\4\uffff\1\4\1\uffff"+
            "\1\1\1\uffff\1\1\1\uffff\12\1\1\uffff\4\1\1\4\33\1\1\uffff\1"+
            "\2\2\uffff\34\1\4\uffff\uff81\1"
    };

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "()+ loopback of 312:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA15_2 = input.LA(1);

                        s = -1;
                        if ( ((LA15_2 >= '\u0000' && LA15_2 <= '\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA15_0 = input.LA(1);

                        s = -1;
                        if ( ((LA15_0 >= '\u0000' && LA15_0 <= '\b')||(LA15_0 >= '\u000B' && LA15_0 <= '\f')||(LA15_0 >= '\u000E' && LA15_0 <= '\u001F')||(LA15_0 >= '#' && LA15_0 <= '%')||LA15_0==','||LA15_0=='.'||(LA15_0 >= '0' && LA15_0 <= '9')||(LA15_0 >= ';' && LA15_0 <= '>')||(LA15_0 >= '@' && LA15_0 <= 'Z')||(LA15_0 >= '_' && LA15_0 <= 'z')||(LA15_0 >= '\u007F' && LA15_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA15_0=='\\') ) {s = 2;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA15_5 = input.LA(1);

                        s = -1;
                        if ( ((LA15_5 >= '\u0000' && LA15_5 <= '\b')||(LA15_5 >= '\u000B' && LA15_5 <= '\f')||(LA15_5 >= '\u000E' && LA15_5 <= '\u001F')||(LA15_5 >= '#' && LA15_5 <= '%')||LA15_5==','||LA15_5=='.'||(LA15_5 >= '0' && LA15_5 <= '9')||(LA15_5 >= ';' && LA15_5 <= '>')||(LA15_5 >= '@' && LA15_5 <= 'Z')||(LA15_5 >= '_' && LA15_5 <= 'z')||(LA15_5 >= '\u007F' && LA15_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA15_5=='\\') ) {s = 2;}

                        else if ( (LA15_5=='*'||LA15_5=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA15_1 = input.LA(1);

                        s = -1;
                        if ( ((LA15_1 >= '\u0000' && LA15_1 <= '\b')||(LA15_1 >= '\u000B' && LA15_1 <= '\f')||(LA15_1 >= '\u000E' && LA15_1 <= '\u001F')||(LA15_1 >= '#' && LA15_1 <= '%')||LA15_1==','||LA15_1=='.'||(LA15_1 >= '0' && LA15_1 <= '9')||(LA15_1 >= ';' && LA15_1 <= '>')||(LA15_1 >= '@' && LA15_1 <= 'Z')||(LA15_1 >= '_' && LA15_1 <= 'z')||(LA15_1 >= '\u007F' && LA15_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA15_1=='\\') ) {s = 2;}

                        else if ( (LA15_1=='*'||LA15_1=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 15, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA20_eotS =
        "\11\uffff\1\34\1\36\4\uffff\1\37\1\uffff\2\43\1\uffff\1\43\1\uffff"+
        "\1\52\1\43\1\uffff\1\54\1\43\7\uffff\1\60\1\uffff\1\43\1\uffff\1"+
        "\43\1\25\2\43\1\uffff\1\43\1\uffff\1\54\1\43\2\uffff\1\43\1\23\1"+
        "\67\1\43\1\54\2\uffff\1\52";
    static final String DFA20_eofS =
        "\71\uffff";
    static final String DFA20_minS =
        "\1\0\10\uffff\2\0\4\uffff\1\0\1\uffff\2\0\1\uffff\1\0\1\uffff\2"+
        "\0\1\uffff\3\0\4\uffff\1\0\1\uffff\1\0\1\uffff\6\0\1\uffff\1\0\1"+
        "\uffff\2\0\2\uffff\5\0\2\uffff\1\0";
    static final String DFA20_maxS =
        "\1\uffff\10\uffff\2\uffff\4\uffff\1\uffff\1\uffff\2\uffff\1\uffff"+
        "\1\uffff\1\uffff\2\uffff\1\uffff\3\uffff\4\uffff\1\uffff\1\uffff"+
        "\1\uffff\1\uffff\6\uffff\1\uffff\1\uffff\1\uffff\2\uffff\2\uffff"+
        "\5\uffff\2\uffff\1\uffff";
    static final String DFA20_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff\1\13\1\14\1\15"+
        "\1\16\1\uffff\1\20\2\uffff\1\22\1\uffff\1\23\2\uffff\1\26\3\uffff"+
        "\1\11\1\31\1\12\1\17\1\uffff\1\33\1\uffff\1\30\6\uffff\1\25\1\uffff"+
        "\1\27\2\uffff\1\32\1\21\5\uffff\1\32\1\24\1\uffff";
    static final String DFA20_specialS =
        "\1\34\10\uffff\1\7\1\11\4\uffff\1\22\1\uffff\1\23\1\21\1\uffff\1"+
        "\31\1\uffff\1\13\1\24\1\uffff\1\3\1\27\1\16\4\uffff\1\0\1\uffff"+
        "\1\26\1\uffff\1\2\1\17\1\25\1\15\1\14\1\10\1\uffff\1\5\1\uffff\1"+
        "\33\1\12\2\uffff\1\30\1\1\1\4\1\6\1\20\2\uffff\1\32}>";
    static final String[] DFA20_transitionS = {
            "\11\32\2\30\2\32\1\30\22\32\1\30\1\10\1\17\3\32\1\23\1\20\1"+
            "\2\1\3\1\11\1\7\1\32\1\10\1\32\1\1\12\31\1\6\4\32\1\12\1\32"+
            "\1\22\14\32\1\27\1\24\4\32\1\21\6\32\1\4\1\33\1\5\1\15\2\32"+
            "\1\22\14\32\1\26\1\24\13\32\1\13\1\25\1\14\1\16\uff81\32",
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
            "\1\35\2\uffff\34\35\4\uffff\uff81\35",
            "\11\35\2\uffff\2\35\1\uffff\22\35\3\uffff\3\35\6\uffff\1\35"+
            "\1\uffff\1\35\1\uffff\12\35\1\uffff\4\35\1\uffff\33\35\1\uffff"+
            "\1\35\2\uffff\34\35\4\uffff\uff81\35",
            "",
            "",
            "",
            "",
            "\42\40\1\uffff\7\40\1\41\24\40\1\41\uffc0\40",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\17"+
            "\44\1\42\13\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\16"+
            "\44\1\46\14\44\1\uffff\1\45\2\uffff\17\44\1\46\14\44\4\uffff"+
            "\uff81\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\22"+
            "\44\1\47\10\44\1\uffff\1\45\2\uffff\23\44\1\47\10\44\4\uffff"+
            "\uff81\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\5"+
            "\44\1\51\11\44\1\50\13\44\1\uffff\1\45\2\uffff\6\44\1\51\11"+
            "\44\1\50\13\44\4\uffff\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\5"+
            "\44\1\51\11\44\1\50\13\44\1\uffff\1\45\2\uffff\6\44\1\51\11"+
            "\44\1\50\13\44\4\uffff\uff81\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\53\1\uffff\12\55\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "\0\56",
            "",
            "",
            "",
            "",
            "\42\40\1\57\7\40\1\41\24\40\1\41\uffc0\40",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "\0\61",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\4"+
            "\44\1\62\26\44\1\uffff\1\45\2\uffff\5\44\1\62\26\44\4\uffff"+
            "\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\24"+
            "\44\1\63\6\44\1\uffff\1\45\2\uffff\25\44\1\63\6\44\4\uffff\uff81"+
            "\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\1"+
            "\44\1\64\31\44\1\uffff\1\45\2\uffff\2\44\1\64\31\44\4\uffff"+
            "\uff81\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\65\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\53\1\uffff\12\55\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\22"+
            "\44\1\70\10\44\1\uffff\1\45\2\uffff\23\44\1\70\10\44\4\uffff"+
            "\uff81\44",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\65\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44",
            "",
            "",
            "\11\44\2\uffff\2\44\1\uffff\22\44\3\uffff\3\44\4\uffff\1\35"+
            "\1\uffff\1\44\1\uffff\1\44\1\uffff\12\44\1\uffff\4\44\1\35\33"+
            "\44\1\uffff\1\45\2\uffff\34\44\4\uffff\uff81\44"
    };

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__55 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | NEAR | WS | NUMBER | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA20_32 = input.LA(1);

                        s = -1;
                        if ( (LA20_32=='\"') ) {s = 47;}

                        else if ( ((LA20_32 >= '\u0000' && LA20_32 <= '!')||(LA20_32 >= '#' && LA20_32 <= ')')||(LA20_32 >= '+' && LA20_32 <= '>')||(LA20_32 >= '@' && LA20_32 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA20_32=='*'||LA20_32=='?') ) {s = 33;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA20_50 = input.LA(1);

                        s = -1;
                        if ( ((LA20_50 >= '\u0000' && LA20_50 <= '\b')||(LA20_50 >= '\u000B' && LA20_50 <= '\f')||(LA20_50 >= '\u000E' && LA20_50 <= '\u001F')||(LA20_50 >= '#' && LA20_50 <= '%')||LA20_50==','||LA20_50=='.'||(LA20_50 >= '0' && LA20_50 <= '9')||(LA20_50 >= ';' && LA20_50 <= '>')||(LA20_50 >= '@' && LA20_50 <= 'Z')||(LA20_50 >= '_' && LA20_50 <= 'z')||(LA20_50 >= '\u007F' && LA20_50 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_50=='\\') ) {s = 37;}

                        else if ( (LA20_50=='*'||LA20_50=='?') ) {s = 29;}

                        else s = 19;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA20_36 = input.LA(1);

                        s = -1;
                        if ( ((LA20_36 >= '\u0000' && LA20_36 <= '\b')||(LA20_36 >= '\u000B' && LA20_36 <= '\f')||(LA20_36 >= '\u000E' && LA20_36 <= '\u001F')||(LA20_36 >= '#' && LA20_36 <= '%')||LA20_36==','||LA20_36=='.'||(LA20_36 >= '0' && LA20_36 <= '9')||(LA20_36 >= ';' && LA20_36 <= '>')||(LA20_36 >= '@' && LA20_36 <= 'Z')||(LA20_36 >= '_' && LA20_36 <= 'z')||(LA20_36 >= '\u007F' && LA20_36 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_36=='\\') ) {s = 37;}

                        else if ( (LA20_36=='*'||LA20_36=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA20_25 = input.LA(1);

                        s = -1;
                        if ( (LA20_25=='.') ) {s = 43;}

                        else if ( ((LA20_25 >= '0' && LA20_25 <= '9')) ) {s = 45;}

                        else if ( ((LA20_25 >= '\u0000' && LA20_25 <= '\b')||(LA20_25 >= '\u000B' && LA20_25 <= '\f')||(LA20_25 >= '\u000E' && LA20_25 <= '\u001F')||(LA20_25 >= '#' && LA20_25 <= '%')||LA20_25==','||(LA20_25 >= ';' && LA20_25 <= '>')||(LA20_25 >= '@' && LA20_25 <= 'Z')||(LA20_25 >= '_' && LA20_25 <= 'z')||(LA20_25 >= '\u007F' && LA20_25 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_25=='\\') ) {s = 37;}

                        else if ( (LA20_25=='*'||LA20_25=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA20_51 = input.LA(1);

                        s = -1;
                        if ( ((LA20_51 >= '\u0000' && LA20_51 <= '\b')||(LA20_51 >= '\u000B' && LA20_51 <= '\f')||(LA20_51 >= '\u000E' && LA20_51 <= '\u001F')||(LA20_51 >= '#' && LA20_51 <= '%')||LA20_51==','||LA20_51=='.'||(LA20_51 >= '0' && LA20_51 <= '9')||(LA20_51 >= ';' && LA20_51 <= '>')||(LA20_51 >= '@' && LA20_51 <= 'Z')||(LA20_51 >= '_' && LA20_51 <= 'z')||(LA20_51 >= '\u007F' && LA20_51 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_51=='\\') ) {s = 37;}

                        else if ( (LA20_51=='*'||LA20_51=='?') ) {s = 29;}

                        else s = 55;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA20_43 = input.LA(1);

                        s = -1;
                        if ( ((LA20_43 >= '0' && LA20_43 <= '9')) ) {s = 53;}

                        else if ( ((LA20_43 >= '\u0000' && LA20_43 <= '\b')||(LA20_43 >= '\u000B' && LA20_43 <= '\f')||(LA20_43 >= '\u000E' && LA20_43 <= '\u001F')||(LA20_43 >= '#' && LA20_43 <= '%')||LA20_43==','||LA20_43=='.'||(LA20_43 >= ';' && LA20_43 <= '>')||(LA20_43 >= '@' && LA20_43 <= 'Z')||(LA20_43 >= '_' && LA20_43 <= 'z')||(LA20_43 >= '\u007F' && LA20_43 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_43=='\\') ) {s = 37;}

                        else if ( (LA20_43=='*'||LA20_43=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA20_52 = input.LA(1);

                        s = -1;
                        if ( (LA20_52=='R'||LA20_52=='r') ) {s = 56;}

                        else if ( ((LA20_52 >= '\u0000' && LA20_52 <= '\b')||(LA20_52 >= '\u000B' && LA20_52 <= '\f')||(LA20_52 >= '\u000E' && LA20_52 <= '\u001F')||(LA20_52 >= '#' && LA20_52 <= '%')||LA20_52==','||LA20_52=='.'||(LA20_52 >= '0' && LA20_52 <= '9')||(LA20_52 >= ';' && LA20_52 <= '>')||(LA20_52 >= '@' && LA20_52 <= 'Q')||(LA20_52 >= 'S' && LA20_52 <= 'Z')||(LA20_52 >= '_' && LA20_52 <= 'q')||(LA20_52 >= 's' && LA20_52 <= 'z')||(LA20_52 >= '\u007F' && LA20_52 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_52=='\\') ) {s = 37;}

                        else if ( (LA20_52=='*'||LA20_52=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA20_9 = input.LA(1);

                        s = -1;
                        if ( ((LA20_9 >= '\u0000' && LA20_9 <= '\b')||(LA20_9 >= '\u000B' && LA20_9 <= '\f')||(LA20_9 >= '\u000E' && LA20_9 <= '\u001F')||(LA20_9 >= '#' && LA20_9 <= '%')||LA20_9==','||LA20_9=='.'||(LA20_9 >= '0' && LA20_9 <= '9')||(LA20_9 >= ';' && LA20_9 <= '>')||(LA20_9 >= '@' && LA20_9 <= 'Z')||LA20_9=='\\'||(LA20_9 >= '_' && LA20_9 <= 'z')||(LA20_9 >= '\u007F' && LA20_9 <= '\uFFFF')) ) {s = 29;}

                        else s = 28;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA20_41 = input.LA(1);

                        s = -1;
                        if ( (LA20_41=='A'||LA20_41=='a') ) {s = 52;}

                        else if ( ((LA20_41 >= '\u0000' && LA20_41 <= '\b')||(LA20_41 >= '\u000B' && LA20_41 <= '\f')||(LA20_41 >= '\u000E' && LA20_41 <= '\u001F')||(LA20_41 >= '#' && LA20_41 <= '%')||LA20_41==','||LA20_41=='.'||(LA20_41 >= '0' && LA20_41 <= '9')||(LA20_41 >= ';' && LA20_41 <= '>')||LA20_41=='@'||(LA20_41 >= 'B' && LA20_41 <= 'Z')||(LA20_41 >= '_' && LA20_41 <= '`')||(LA20_41 >= 'b' && LA20_41 <= 'z')||(LA20_41 >= '\u007F' && LA20_41 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_41=='\\') ) {s = 37;}

                        else if ( (LA20_41=='*'||LA20_41=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA20_10 = input.LA(1);

                        s = -1;
                        if ( ((LA20_10 >= '\u0000' && LA20_10 <= '\b')||(LA20_10 >= '\u000B' && LA20_10 <= '\f')||(LA20_10 >= '\u000E' && LA20_10 <= '\u001F')||(LA20_10 >= '#' && LA20_10 <= '%')||LA20_10==','||LA20_10=='.'||(LA20_10 >= '0' && LA20_10 <= '9')||(LA20_10 >= ';' && LA20_10 <= '>')||(LA20_10 >= '@' && LA20_10 <= 'Z')||LA20_10=='\\'||(LA20_10 >= '_' && LA20_10 <= 'z')||(LA20_10 >= '\u007F' && LA20_10 <= '\uFFFF')) ) {s = 29;}

                        else s = 30;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA20_46 = input.LA(1);

                        s = -1;
                        if ( ((LA20_46 >= '\u0000' && LA20_46 <= '\b')||(LA20_46 >= '\u000B' && LA20_46 <= '\f')||(LA20_46 >= '\u000E' && LA20_46 <= '\u001F')||(LA20_46 >= '#' && LA20_46 <= '%')||LA20_46==','||LA20_46=='.'||(LA20_46 >= '0' && LA20_46 <= '9')||(LA20_46 >= ';' && LA20_46 <= '>')||(LA20_46 >= '@' && LA20_46 <= 'Z')||(LA20_46 >= '_' && LA20_46 <= 'z')||(LA20_46 >= '\u007F' && LA20_46 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_46=='\\') ) {s = 37;}

                        else if ( (LA20_46=='*'||LA20_46=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA20_22 = input.LA(1);

                        s = -1;
                        if ( (LA20_22=='O'||LA20_22=='o') ) {s = 40;}

                        else if ( (LA20_22=='E'||LA20_22=='e') ) {s = 41;}

                        else if ( ((LA20_22 >= '\u0000' && LA20_22 <= '\b')||(LA20_22 >= '\u000B' && LA20_22 <= '\f')||(LA20_22 >= '\u000E' && LA20_22 <= '\u001F')||(LA20_22 >= '#' && LA20_22 <= '%')||LA20_22==','||LA20_22=='.'||(LA20_22 >= '0' && LA20_22 <= '9')||(LA20_22 >= ';' && LA20_22 <= '>')||(LA20_22 >= '@' && LA20_22 <= 'D')||(LA20_22 >= 'F' && LA20_22 <= 'N')||(LA20_22 >= 'P' && LA20_22 <= 'Z')||(LA20_22 >= '_' && LA20_22 <= 'd')||(LA20_22 >= 'f' && LA20_22 <= 'n')||(LA20_22 >= 'p' && LA20_22 <= 'z')||(LA20_22 >= '\u007F' && LA20_22 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_22=='\\') ) {s = 37;}

                        else if ( (LA20_22=='*'||LA20_22=='?') ) {s = 29;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA20_40 = input.LA(1);

                        s = -1;
                        if ( (LA20_40=='T'||LA20_40=='t') ) {s = 51;}

                        else if ( ((LA20_40 >= '\u0000' && LA20_40 <= '\b')||(LA20_40 >= '\u000B' && LA20_40 <= '\f')||(LA20_40 >= '\u000E' && LA20_40 <= '\u001F')||(LA20_40 >= '#' && LA20_40 <= '%')||LA20_40==','||LA20_40=='.'||(LA20_40 >= '0' && LA20_40 <= '9')||(LA20_40 >= ';' && LA20_40 <= '>')||(LA20_40 >= '@' && LA20_40 <= 'S')||(LA20_40 >= 'U' && LA20_40 <= 'Z')||(LA20_40 >= '_' && LA20_40 <= 's')||(LA20_40 >= 'u' && LA20_40 <= 'z')||(LA20_40 >= '\u007F' && LA20_40 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_40=='\\') ) {s = 37;}

                        else if ( (LA20_40=='*'||LA20_40=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA20_39 = input.LA(1);

                        s = -1;
                        if ( ((LA20_39 >= '\u0000' && LA20_39 <= '\b')||(LA20_39 >= '\u000B' && LA20_39 <= '\f')||(LA20_39 >= '\u000E' && LA20_39 <= '\u001F')||(LA20_39 >= '#' && LA20_39 <= '%')||LA20_39==','||LA20_39=='.'||(LA20_39 >= '0' && LA20_39 <= '9')||(LA20_39 >= ';' && LA20_39 <= '>')||(LA20_39 >= '@' && LA20_39 <= 'Z')||(LA20_39 >= '_' && LA20_39 <= 'z')||(LA20_39 >= '\u007F' && LA20_39 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_39=='\\') ) {s = 37;}

                        else if ( (LA20_39=='*'||LA20_39=='?') ) {s = 29;}

                        else s = 21;

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA20_27 = input.LA(1);

                        s = -1;
                        if ( ((LA20_27 >= '\u0000' && LA20_27 <= '\uFFFF')) ) {s = 46;}

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA20_37 = input.LA(1);

                        s = -1;
                        if ( ((LA20_37 >= '\u0000' && LA20_37 <= '\uFFFF')) ) {s = 49;}

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA20_53 = input.LA(1);

                        s = -1;
                        if ( ((LA20_53 >= '0' && LA20_53 <= '9')) ) {s = 53;}

                        else if ( ((LA20_53 >= '\u0000' && LA20_53 <= '\b')||(LA20_53 >= '\u000B' && LA20_53 <= '\f')||(LA20_53 >= '\u000E' && LA20_53 <= '\u001F')||(LA20_53 >= '#' && LA20_53 <= '%')||LA20_53==','||LA20_53=='.'||(LA20_53 >= ';' && LA20_53 <= '>')||(LA20_53 >= '@' && LA20_53 <= 'Z')||(LA20_53 >= '_' && LA20_53 <= 'z')||(LA20_53 >= '\u007F' && LA20_53 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_53=='\\') ) {s = 37;}

                        else if ( (LA20_53=='*'||LA20_53=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA20_18 = input.LA(1);

                        s = -1;
                        if ( (LA20_18=='N'||LA20_18=='n') ) {s = 38;}

                        else if ( ((LA20_18 >= '\u0000' && LA20_18 <= '\b')||(LA20_18 >= '\u000B' && LA20_18 <= '\f')||(LA20_18 >= '\u000E' && LA20_18 <= '\u001F')||(LA20_18 >= '#' && LA20_18 <= '%')||LA20_18==','||LA20_18=='.'||(LA20_18 >= '0' && LA20_18 <= '9')||(LA20_18 >= ';' && LA20_18 <= '>')||(LA20_18 >= '@' && LA20_18 <= 'M')||(LA20_18 >= 'O' && LA20_18 <= 'Z')||(LA20_18 >= '_' && LA20_18 <= 'm')||(LA20_18 >= 'o' && LA20_18 <= 'z')||(LA20_18 >= '\u007F' && LA20_18 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_18=='\\') ) {s = 37;}

                        else if ( (LA20_18=='*'||LA20_18=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA20_15 = input.LA(1);

                        s = -1;
                        if ( ((LA20_15 >= '\u0000' && LA20_15 <= '!')||(LA20_15 >= '#' && LA20_15 <= ')')||(LA20_15 >= '+' && LA20_15 <= '>')||(LA20_15 >= '@' && LA20_15 <= '\uFFFF')) ) {s = 32;}

                        else if ( (LA20_15=='*'||LA20_15=='?') ) {s = 33;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA20_17 = input.LA(1);

                        s = -1;
                        if ( (LA20_17=='O') ) {s = 34;}

                        else if ( ((LA20_17 >= '\u0000' && LA20_17 <= '\b')||(LA20_17 >= '\u000B' && LA20_17 <= '\f')||(LA20_17 >= '\u000E' && LA20_17 <= '\u001F')||(LA20_17 >= '#' && LA20_17 <= '%')||LA20_17==','||LA20_17=='.'||(LA20_17 >= '0' && LA20_17 <= '9')||(LA20_17 >= ';' && LA20_17 <= '>')||(LA20_17 >= '@' && LA20_17 <= 'N')||(LA20_17 >= 'P' && LA20_17 <= 'Z')||(LA20_17 >= '_' && LA20_17 <= 'z')||(LA20_17 >= '\u007F' && LA20_17 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_17=='\\') ) {s = 37;}

                        else if ( (LA20_17=='*'||LA20_17=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA20_23 = input.LA(1);

                        s = -1;
                        if ( (LA20_23=='O'||LA20_23=='o') ) {s = 40;}

                        else if ( (LA20_23=='E'||LA20_23=='e') ) {s = 41;}

                        else if ( ((LA20_23 >= '\u0000' && LA20_23 <= '\b')||(LA20_23 >= '\u000B' && LA20_23 <= '\f')||(LA20_23 >= '\u000E' && LA20_23 <= '\u001F')||(LA20_23 >= '#' && LA20_23 <= '%')||LA20_23==','||LA20_23=='.'||(LA20_23 >= '0' && LA20_23 <= '9')||(LA20_23 >= ';' && LA20_23 <= '>')||(LA20_23 >= '@' && LA20_23 <= 'D')||(LA20_23 >= 'F' && LA20_23 <= 'N')||(LA20_23 >= 'P' && LA20_23 <= 'Z')||(LA20_23 >= '_' && LA20_23 <= 'd')||(LA20_23 >= 'f' && LA20_23 <= 'n')||(LA20_23 >= 'p' && LA20_23 <= 'z')||(LA20_23 >= '\u007F' && LA20_23 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_23=='\\') ) {s = 37;}

                        else if ( (LA20_23=='*'||LA20_23=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA20_38 = input.LA(1);

                        s = -1;
                        if ( (LA20_38=='D'||LA20_38=='d') ) {s = 50;}

                        else if ( ((LA20_38 >= '\u0000' && LA20_38 <= '\b')||(LA20_38 >= '\u000B' && LA20_38 <= '\f')||(LA20_38 >= '\u000E' && LA20_38 <= '\u001F')||(LA20_38 >= '#' && LA20_38 <= '%')||LA20_38==','||LA20_38=='.'||(LA20_38 >= '0' && LA20_38 <= '9')||(LA20_38 >= ';' && LA20_38 <= '>')||(LA20_38 >= '@' && LA20_38 <= 'C')||(LA20_38 >= 'E' && LA20_38 <= 'Z')||(LA20_38 >= '_' && LA20_38 <= 'c')||(LA20_38 >= 'e' && LA20_38 <= 'z')||(LA20_38 >= '\u007F' && LA20_38 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_38=='\\') ) {s = 37;}

                        else if ( (LA20_38=='*'||LA20_38=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA20_34 = input.LA(1);

                        s = -1;
                        if ( ((LA20_34 >= '\u0000' && LA20_34 <= '\b')||(LA20_34 >= '\u000B' && LA20_34 <= '\f')||(LA20_34 >= '\u000E' && LA20_34 <= '\u001F')||(LA20_34 >= '#' && LA20_34 <= '%')||LA20_34==','||LA20_34=='.'||(LA20_34 >= '0' && LA20_34 <= '9')||(LA20_34 >= ';' && LA20_34 <= '>')||(LA20_34 >= '@' && LA20_34 <= 'Z')||(LA20_34 >= '_' && LA20_34 <= 'z')||(LA20_34 >= '\u007F' && LA20_34 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_34=='\\') ) {s = 37;}

                        else if ( (LA20_34=='*'||LA20_34=='?') ) {s = 29;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA20_26 = input.LA(1);

                        s = -1;
                        if ( ((LA20_26 >= '\u0000' && LA20_26 <= '\b')||(LA20_26 >= '\u000B' && LA20_26 <= '\f')||(LA20_26 >= '\u000E' && LA20_26 <= '\u001F')||(LA20_26 >= '#' && LA20_26 <= '%')||LA20_26==','||LA20_26=='.'||(LA20_26 >= '0' && LA20_26 <= '9')||(LA20_26 >= ';' && LA20_26 <= '>')||(LA20_26 >= '@' && LA20_26 <= 'Z')||(LA20_26 >= '_' && LA20_26 <= 'z')||(LA20_26 >= '\u007F' && LA20_26 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_26=='\\') ) {s = 37;}

                        else if ( (LA20_26=='*'||LA20_26=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA20_49 = input.LA(1);

                        s = -1;
                        if ( ((LA20_49 >= '\u0000' && LA20_49 <= '\b')||(LA20_49 >= '\u000B' && LA20_49 <= '\f')||(LA20_49 >= '\u000E' && LA20_49 <= '\u001F')||(LA20_49 >= '#' && LA20_49 <= '%')||LA20_49==','||LA20_49=='.'||(LA20_49 >= '0' && LA20_49 <= '9')||(LA20_49 >= ';' && LA20_49 <= '>')||(LA20_49 >= '@' && LA20_49 <= 'Z')||(LA20_49 >= '_' && LA20_49 <= 'z')||(LA20_49 >= '\u007F' && LA20_49 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_49=='\\') ) {s = 37;}

                        else if ( (LA20_49=='*'||LA20_49=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA20_20 = input.LA(1);

                        s = -1;
                        if ( (LA20_20=='R'||LA20_20=='r') ) {s = 39;}

                        else if ( ((LA20_20 >= '\u0000' && LA20_20 <= '\b')||(LA20_20 >= '\u000B' && LA20_20 <= '\f')||(LA20_20 >= '\u000E' && LA20_20 <= '\u001F')||(LA20_20 >= '#' && LA20_20 <= '%')||LA20_20==','||LA20_20=='.'||(LA20_20 >= '0' && LA20_20 <= '9')||(LA20_20 >= ';' && LA20_20 <= '>')||(LA20_20 >= '@' && LA20_20 <= 'Q')||(LA20_20 >= 'S' && LA20_20 <= 'Z')||(LA20_20 >= '_' && LA20_20 <= 'q')||(LA20_20 >= 's' && LA20_20 <= 'z')||(LA20_20 >= '\u007F' && LA20_20 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_20=='\\') ) {s = 37;}

                        else if ( (LA20_20=='*'||LA20_20=='?') ) {s = 29;}

                        else s = 35;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA20_56 = input.LA(1);

                        s = -1;
                        if ( ((LA20_56 >= '\u0000' && LA20_56 <= '\b')||(LA20_56 >= '\u000B' && LA20_56 <= '\f')||(LA20_56 >= '\u000E' && LA20_56 <= '\u001F')||(LA20_56 >= '#' && LA20_56 <= '%')||LA20_56==','||LA20_56=='.'||(LA20_56 >= '0' && LA20_56 <= '9')||(LA20_56 >= ';' && LA20_56 <= '>')||(LA20_56 >= '@' && LA20_56 <= 'Z')||(LA20_56 >= '_' && LA20_56 <= 'z')||(LA20_56 >= '\u007F' && LA20_56 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_56=='\\') ) {s = 37;}

                        else if ( (LA20_56=='*'||LA20_56=='?') ) {s = 29;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;

                    case 27 : 
                        int LA20_45 = input.LA(1);

                        s = -1;
                        if ( (LA20_45=='.') ) {s = 43;}

                        else if ( ((LA20_45 >= '0' && LA20_45 <= '9')) ) {s = 45;}

                        else if ( ((LA20_45 >= '\u0000' && LA20_45 <= '\b')||(LA20_45 >= '\u000B' && LA20_45 <= '\f')||(LA20_45 >= '\u000E' && LA20_45 <= '\u001F')||(LA20_45 >= '#' && LA20_45 <= '%')||LA20_45==','||(LA20_45 >= ';' && LA20_45 <= '>')||(LA20_45 >= '@' && LA20_45 <= 'Z')||(LA20_45 >= '_' && LA20_45 <= 'z')||(LA20_45 >= '\u007F' && LA20_45 <= '\uFFFF')) ) {s = 36;}

                        else if ( (LA20_45=='\\') ) {s = 37;}

                        else if ( (LA20_45=='*'||LA20_45=='?') ) {s = 29;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;

                    case 28 : 
                        int LA20_0 = input.LA(1);

                        s = -1;
                        if ( (LA20_0=='/') ) {s = 1;}

                        else if ( (LA20_0=='(') ) {s = 2;}

                        else if ( (LA20_0==')') ) {s = 3;}

                        else if ( (LA20_0=='[') ) {s = 4;}

                        else if ( (LA20_0==']') ) {s = 5;}

                        else if ( (LA20_0==':') ) {s = 6;}

                        else if ( (LA20_0=='+') ) {s = 7;}

                        else if ( (LA20_0=='!'||LA20_0=='-') ) {s = 8;}

                        else if ( (LA20_0=='*') ) {s = 9;}

                        else if ( (LA20_0=='?') ) {s = 10;}

                        else if ( (LA20_0=='{') ) {s = 11;}

                        else if ( (LA20_0=='}') ) {s = 12;}

                        else if ( (LA20_0=='^') ) {s = 13;}

                        else if ( (LA20_0=='~') ) {s = 14;}

                        else if ( (LA20_0=='\"') ) {s = 15;}

                        else if ( (LA20_0=='\'') ) {s = 16;}

                        else if ( (LA20_0=='T') ) {s = 17;}

                        else if ( (LA20_0=='A'||LA20_0=='a') ) {s = 18;}

                        else if ( (LA20_0=='&') ) {s = 19;}

                        else if ( (LA20_0=='O'||LA20_0=='o') ) {s = 20;}

                        else if ( (LA20_0=='|') ) {s = 21;}

                        else if ( (LA20_0=='n') ) {s = 22;}

                        else if ( (LA20_0=='N') ) {s = 23;}

                        else if ( ((LA20_0 >= '\t' && LA20_0 <= '\n')||LA20_0=='\r'||LA20_0==' ') ) {s = 24;}

                        else if ( ((LA20_0 >= '0' && LA20_0 <= '9')) ) {s = 25;}

                        else if ( ((LA20_0 >= '\u0000' && LA20_0 <= '\b')||(LA20_0 >= '\u000B' && LA20_0 <= '\f')||(LA20_0 >= '\u000E' && LA20_0 <= '\u001F')||(LA20_0 >= '#' && LA20_0 <= '%')||LA20_0==','||LA20_0=='.'||(LA20_0 >= ';' && LA20_0 <= '>')||LA20_0=='@'||(LA20_0 >= 'B' && LA20_0 <= 'M')||(LA20_0 >= 'P' && LA20_0 <= 'S')||(LA20_0 >= 'U' && LA20_0 <= 'Z')||(LA20_0 >= '_' && LA20_0 <= '`')||(LA20_0 >= 'b' && LA20_0 <= 'm')||(LA20_0 >= 'p' && LA20_0 <= 'z')||(LA20_0 >= '\u007F' && LA20_0 <= '\uFFFF')) ) {s = 26;}

                        else if ( (LA20_0=='\\') ) {s = 27;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 20, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}