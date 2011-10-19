// $ANTLR 3.4 Invenio.g 2011-10-18 14:28:56

   package org.apache.lucene.queryParser.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class InvenioLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__51=51;
    public static final int T__52=52;
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
    public static final int NEAR=22;
    public static final int NORMAL_CHAR=23;
    public static final int NOT=24;
    public static final int NUMBER=25;
    public static final int OPERATOR=26;
    public static final int OR=27;
    public static final int PHRASE=28;
    public static final int PHRASE_ANYTHING=29;
    public static final int PLUS=30;
    public static final int QANYTHING=31;
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
    public static final int TERM_NORMAL=44;
    public static final int TERM_TRUNCATED=45;
    public static final int TILDE=46;
    public static final int TO=47;
    public static final int VALUE=48;
    public static final int VBAR=49;
    public static final int WS=50;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public InvenioLexer() {} 
    public InvenioLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InvenioLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "Invenio.g"; }

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Invenio.g:11:7: ( '#' )
            // Invenio.g:11:9: '#'
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
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Invenio.g:12:7: ( '/' )
            // Invenio.g:12:9: '/'
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
    // $ANTLR end "T__52"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Invenio.g:210:9: ( '(' )
            // Invenio.g:210:11: '('
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
            // Invenio.g:212:9: ( ')' )
            // Invenio.g:212:11: ')'
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
            // Invenio.g:214:9: ( '[' )
            // Invenio.g:214:11: '['
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
            // Invenio.g:216:9: ( ']' )
            // Invenio.g:216:11: ']'
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
            // Invenio.g:218:9: ( ':' )
            // Invenio.g:218:11: ':'
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
            // Invenio.g:220:7: ( '+' )
            // Invenio.g:220:9: '+'
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
            // Invenio.g:222:7: ( '-' )
            // Invenio.g:222:9: '-'
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
            // Invenio.g:224:7: ( '*' )
            // Invenio.g:224:9: '*'
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
            // Invenio.g:226:8: ( '?' )
            // Invenio.g:226:10: '?'
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
            // Invenio.g:228:16: ( '|' )
            // Invenio.g:228:18: '|'
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
            // Invenio.g:230:16: ( '&' )
            // Invenio.g:230:18: '&'
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
            // Invenio.g:232:9: ( '{' )
            // Invenio.g:232:11: '{'
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
            // Invenio.g:234:9: ( '}' )
            // Invenio.g:234:11: '}'
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
            // Invenio.g:236:7: ( '^' )
            // Invenio.g:236:9: '^'
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
            // Invenio.g:238:7: ( '~' )
            // Invenio.g:238:9: '~'
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
            // Invenio.g:241:2: ( '\\\"' )
            // Invenio.g:241:4: '\\\"'
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
            // Invenio.g:244:2: ( '\\'' )
            // Invenio.g:244:4: '\\''
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
            int _type = ESC_CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Invenio.g:247:9: ( '\\\\' . )
            // Invenio.g:247:12: '\\\\' .
            {
            match('\\'); 

            matchAny(); 

            }

            state.type = _type;
            state.channel = _channel;
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
            // Invenio.g:249:4: ( 'TO' )
            // Invenio.g:249:6: 'TO'
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
            // Invenio.g:252:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) | PLUS ) )
            // Invenio.g:252:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) | PLUS )
            {
            // Invenio.g:252:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) | PLUS )
            int alt2=3;
            switch ( input.LA(1) ) {
            case 'A':
            case 'a':
                {
                alt2=1;
                }
                break;
            case '&':
                {
                alt2=2;
                }
                break;
            case '+':
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }

            switch (alt2) {
                case 1 :
                    // Invenio.g:252:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
                    // Invenio.g:252:48: ( AMPER ( AMPER )? )
                    {
                    // Invenio.g:252:48: ( AMPER ( AMPER )? )
                    // Invenio.g:252:49: AMPER ( AMPER )?
                    {
                    mAMPER(); 


                    // Invenio.g:252:55: ( AMPER )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0=='&') ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // Invenio.g:
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
                case 3 :
                    // Invenio.g:252:65: PLUS
                    {
                    mPLUS(); 


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
            // Invenio.g:253:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) ) )
            // Invenio.g:253:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
            {
            // Invenio.g:253:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
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
                    // Invenio.g:253:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
                    // Invenio.g:253:34: ( VBAR ( VBAR )? )
                    {
                    // Invenio.g:253:34: ( VBAR ( VBAR )? )
                    // Invenio.g:253:35: VBAR ( VBAR )?
                    {
                    mVBAR(); 


                    // Invenio.g:253:40: ( VBAR )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0=='|') ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // Invenio.g:
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
            // Invenio.g:254:7: ( ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) | '!' | MINUS ) )
            // Invenio.g:254:9: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) | '!' | MINUS )
            {
            // Invenio.g:254:9: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) | '!' | MINUS )
            int alt5=3;
            switch ( input.LA(1) ) {
            case 'N':
            case 'n':
                {
                alt5=1;
                }
                break;
            case '!':
                {
                alt5=2;
                }
                break;
            case '-':
                {
                alt5=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }

            switch (alt5) {
                case 1 :
                    // Invenio.g:254:10: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
                    break;
                case 2 :
                    // Invenio.g:254:48: '!'
                    {
                    match('!'); 

                    }
                    break;
                case 3 :
                    // Invenio.g:254:54: MINUS
                    {
                    mMINUS(); 


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
    // $ANTLR end "NOT"

    // $ANTLR start "NEAR"
    public final void mNEAR() throws RecognitionException {
        try {
            int _type = NEAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Invenio.g:255:7: ( ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' ) )
            // Invenio.g:255:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            {
            // Invenio.g:255:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
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
                    // Invenio.g:255:10: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
                    // Invenio.g:255:60: 'n'
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
            // Invenio.g:258:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // Invenio.g:258:9: ( ' ' | '\\t' | '\\r' | '\\n' )
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
            // Invenio.g:267:13: ( '0' .. '9' )
            // Invenio.g:
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
            // Invenio.g:269:23: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' ) )
            // Invenio.g:269:25: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' )
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
            // Invenio.g:278:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // Invenio.g:279:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // Invenio.g:279:2: ( INT )+
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
            	    // Invenio.g:
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


            // Invenio.g:279:7: ( '.' ( INT )+ )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='.') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // Invenio.g:279:8: '.' ( INT )+
                    {
                    match('.'); 

                    // Invenio.g:279:12: ( INT )+
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
                    	    // Invenio.g:
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

    // $ANTLR start "TERM_NORMAL"
    public final void mTERM_NORMAL() throws RecognitionException {
        try {
            int _type = TERM_NORMAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Invenio.g:284:2: ( ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )* )
            // Invenio.g:285:2: ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )*
            {
            // Invenio.g:285:2: ( NORMAL_CHAR | ESC_CHAR )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( ((LA10_0 >= '\u0000' && LA10_0 <= '\b')||(LA10_0 >= '\u000B' && LA10_0 <= '\f')||(LA10_0 >= '\u000E' && LA10_0 <= '\u001F')||(LA10_0 >= '#' && LA10_0 <= '%')||LA10_0==','||LA10_0=='.'||(LA10_0 >= '0' && LA10_0 <= '9')||(LA10_0 >= ';' && LA10_0 <= '>')||(LA10_0 >= '@' && LA10_0 <= 'Z')||(LA10_0 >= '_' && LA10_0 <= 'z')||(LA10_0 >= '\u007F' && LA10_0 <= '\uFFFF')) ) {
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
                    // Invenio.g:285:4: NORMAL_CHAR
                    {
                    mNORMAL_CHAR(); 


                    }
                    break;
                case 2 :
                    // Invenio.g:285:18: ESC_CHAR
                    {
                    mESC_CHAR(); 


                    }
                    break;

            }


            // Invenio.g:285:28: ( NORMAL_CHAR | ESC_CHAR )*
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
            	    // Invenio.g:285:30: NORMAL_CHAR
            	    {
            	    mNORMAL_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // Invenio.g:285:44: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;

            	default :
            	    break loop11;
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
            // Invenio.g:289:15: ( ( NORMAL_CHAR | ( STAR | QMARK ) )+ )
            // Invenio.g:290:2: ( NORMAL_CHAR | ( STAR | QMARK ) )+
            {
            // Invenio.g:290:2: ( NORMAL_CHAR | ( STAR | QMARK ) )+
            int cnt12=0;
            loop12:
            do {
                int alt12=3;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0 >= '\u0000' && LA12_0 <= '\b')||(LA12_0 >= '\u000B' && LA12_0 <= '\f')||(LA12_0 >= '\u000E' && LA12_0 <= '\u001F')||(LA12_0 >= '#' && LA12_0 <= '%')||LA12_0==','||LA12_0=='.'||(LA12_0 >= '0' && LA12_0 <= '9')||(LA12_0 >= ';' && LA12_0 <= '>')||(LA12_0 >= '@' && LA12_0 <= 'Z')||(LA12_0 >= '_' && LA12_0 <= 'z')||(LA12_0 >= '\u007F' && LA12_0 <= '\uFFFF')) ) {
                    alt12=1;
                }
                else if ( (LA12_0=='*'||LA12_0=='?') ) {
                    alt12=2;
                }


                switch (alt12) {
            	case 1 :
            	    // Invenio.g:290:3: NORMAL_CHAR
            	    {
            	    mNORMAL_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // Invenio.g:290:17: ( STAR | QMARK )
            	    {
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
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);


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
            // Invenio.g:296:2: ( DQUOTE (~ ( '\\\"' | '?' | '*' ) )+ DQUOTE )
            // Invenio.g:297:2: DQUOTE (~ ( '\\\"' | '?' | '*' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // Invenio.g:297:9: (~ ( '\\\"' | '?' | '*' ) )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0 >= '\u0000' && LA13_0 <= '!')||(LA13_0 >= '#' && LA13_0 <= ')')||(LA13_0 >= '+' && LA13_0 <= '>')||(LA13_0 >= '@' && LA13_0 <= '\uFFFF')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // Invenio.g:
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
            	    if ( cnt13 >= 1 ) break loop13;
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
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
            // Invenio.g:300:17: ( DQUOTE (~ ( '\\\"' ) )+ DQUOTE )
            // Invenio.g:301:2: DQUOTE (~ ( '\\\"' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // Invenio.g:301:9: (~ ( '\\\"' ) )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0 >= '\u0000' && LA14_0 <= '!')||(LA14_0 >= '#' && LA14_0 <= '\uFFFF')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // Invenio.g:
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
            	    if ( cnt14 >= 1 ) break loop14;
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
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
        // Invenio.g:1:8: ( T__51 | T__52 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | ESC_CHAR | TO | AND | OR | NOT | NEAR | WS | NUMBER | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING )
        int alt15=29;
        alt15 = dfa15.predict(input);
        switch (alt15) {
            case 1 :
                // Invenio.g:1:10: T__51
                {
                mT__51(); 


                }
                break;
            case 2 :
                // Invenio.g:1:16: T__52
                {
                mT__52(); 


                }
                break;
            case 3 :
                // Invenio.g:1:22: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 4 :
                // Invenio.g:1:29: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 5 :
                // Invenio.g:1:36: LBRACK
                {
                mLBRACK(); 


                }
                break;
            case 6 :
                // Invenio.g:1:43: RBRACK
                {
                mRBRACK(); 


                }
                break;
            case 7 :
                // Invenio.g:1:50: COLON
                {
                mCOLON(); 


                }
                break;
            case 8 :
                // Invenio.g:1:56: PLUS
                {
                mPLUS(); 


                }
                break;
            case 9 :
                // Invenio.g:1:61: MINUS
                {
                mMINUS(); 


                }
                break;
            case 10 :
                // Invenio.g:1:67: STAR
                {
                mSTAR(); 


                }
                break;
            case 11 :
                // Invenio.g:1:72: QMARK
                {
                mQMARK(); 


                }
                break;
            case 12 :
                // Invenio.g:1:78: LCURLY
                {
                mLCURLY(); 


                }
                break;
            case 13 :
                // Invenio.g:1:85: RCURLY
                {
                mRCURLY(); 


                }
                break;
            case 14 :
                // Invenio.g:1:92: CARAT
                {
                mCARAT(); 


                }
                break;
            case 15 :
                // Invenio.g:1:98: TILDE
                {
                mTILDE(); 


                }
                break;
            case 16 :
                // Invenio.g:1:104: DQUOTE
                {
                mDQUOTE(); 


                }
                break;
            case 17 :
                // Invenio.g:1:111: SQUOTE
                {
                mSQUOTE(); 


                }
                break;
            case 18 :
                // Invenio.g:1:118: ESC_CHAR
                {
                mESC_CHAR(); 


                }
                break;
            case 19 :
                // Invenio.g:1:127: TO
                {
                mTO(); 


                }
                break;
            case 20 :
                // Invenio.g:1:130: AND
                {
                mAND(); 


                }
                break;
            case 21 :
                // Invenio.g:1:134: OR
                {
                mOR(); 


                }
                break;
            case 22 :
                // Invenio.g:1:137: NOT
                {
                mNOT(); 


                }
                break;
            case 23 :
                // Invenio.g:1:141: NEAR
                {
                mNEAR(); 


                }
                break;
            case 24 :
                // Invenio.g:1:146: WS
                {
                mWS(); 


                }
                break;
            case 25 :
                // Invenio.g:1:149: NUMBER
                {
                mNUMBER(); 


                }
                break;
            case 26 :
                // Invenio.g:1:156: TERM_NORMAL
                {
                mTERM_NORMAL(); 


                }
                break;
            case 27 :
                // Invenio.g:1:168: TERM_TRUNCATED
                {
                mTERM_TRUNCATED(); 


                }
                break;
            case 28 :
                // Invenio.g:1:183: PHRASE
                {
                mPHRASE(); 


                }
                break;
            case 29 :
                // Invenio.g:1:190: PHRASE_ANYTHING
                {
                mPHRASE_ANYTHING(); 


                }
                break;

        }

    }


    protected DFA15 dfa15 = new DFA15(this);
    static final String DFA15_eotS =
        "\1\uffff\1\36\10\uffff\1\44\1\45\4\uffff\1\46\2\uffff\2\40\1\uffff"+
        "\1\40\1\uffff\1\57\1\uffff\1\40\1\uffff\1\61\1\40\1\uffff\1\40\11"+
        "\uffff\1\64\1\65\1\40\1\27\2\40\1\uffff\1\40\1\uffff\1\61\3\uffff"+
        "\1\25\1\31\1\40\1\61\1\uffff\1\57";
    static final String DFA15_eofS =
        "\74\uffff";
    static final String DFA15_minS =
        "\2\0\10\uffff\2\0\4\uffff\1\0\1\uffff\3\0\1\uffff\1\0\1\uffff\1"+
        "\0\1\uffff\1\0\1\uffff\2\0\1\uffff\1\0\7\uffff\1\0\1\uffff\6\0\1"+
        "\uffff\1\0\1\uffff\1\0\3\uffff\4\0\1\uffff\1\0";
    static final String DFA15_maxS =
        "\2\uffff\10\uffff\2\uffff\4\uffff\1\uffff\1\uffff\3\uffff\1\uffff"+
        "\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff\2\uffff\1\uffff"+
        "\1\uffff\7\uffff\1\uffff\1\uffff\6\uffff\1\uffff\1\uffff\1\uffff"+
        "\1\uffff\3\uffff\4\uffff\1\uffff\1\uffff";
    static final String DFA15_acceptS =
        "\2\uffff\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\2\uffff\1\14\1\15\1\16"+
        "\1\17\1\uffff\1\21\3\uffff\1\24\1\uffff\1\25\1\uffff\1\26\1\uffff"+
        "\1\30\2\uffff\1\1\1\uffff\1\32\1\33\1\10\1\11\1\12\1\13\1\20\1\uffff"+
        "\1\35\6\uffff\1\27\1\uffff\1\31\1\uffff\1\34\1\22\1\23\4\uffff\1"+
        "\34\1\uffff";
    static final String DFA15_specialS =
        "\1\32\1\24\10\uffff\1\6\1\31\4\uffff\1\5\1\uffff\1\16\1\11\1\4\1"+
        "\uffff\1\15\1\uffff\1\25\1\uffff\1\3\1\uffff\1\0\1\2\1\uffff\1\30"+
        "\7\uffff\1\20\1\uffff\1\26\1\33\1\1\1\10\1\13\1\22\1\uffff\1\23"+
        "\1\uffff\1\7\3\uffff\1\17\1\27\1\21\1\14\1\uffff\1\12}>";
    static final String[] DFA15_transitionS = {
            "\11\35\2\33\2\35\1\33\22\35\1\33\1\31\1\20\1\1\2\35\1\25\1\21"+
            "\1\3\1\4\1\12\1\10\1\35\1\11\1\35\1\2\12\34\1\7\4\35\1\13\1"+
            "\35\1\24\14\35\1\32\1\26\4\35\1\23\6\35\1\5\1\22\1\6\1\16\2"+
            "\35\1\24\14\35\1\30\1\26\13\35\1\14\1\27\1\15\1\17\uff81\35",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\33"+
            "\37\1\uffff\1\40\2\uffff\34\37\4\uffff\uff81\37",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\11\41\2\uffff\2\41\1\uffff\22\41\3\uffff\3\41\4\uffff\1\41"+
            "\1\uffff\1\41\1\uffff\1\41\1\uffff\12\41\1\uffff\40\41\4\uffff"+
            "\34\41\4\uffff\uff81\41",
            "\11\41\2\uffff\2\41\1\uffff\22\41\3\uffff\3\41\4\uffff\1\41"+
            "\1\uffff\1\41\1\uffff\1\41\1\uffff\12\41\1\uffff\40\41\4\uffff"+
            "\34\41\4\uffff\uff81\41",
            "",
            "",
            "",
            "",
            "\42\47\1\uffff\7\47\1\50\24\47\1\50\uffc0\47",
            "",
            "\0\51",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\17"+
            "\37\1\52\13\37\4\uffff\34\37\4\uffff\uff81\37",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\16"+
            "\37\1\53\14\37\4\uffff\17\37\1\53\14\37\4\uffff\uff81\37",
            "",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\22"+
            "\37\1\54\10\37\4\uffff\23\37\1\54\10\37\4\uffff\uff81\37",
            "",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\5"+
            "\37\1\56\11\37\1\55\13\37\1\uffff\1\40\2\uffff\6\37\1\56\11"+
            "\37\1\55\13\37\4\uffff\uff81\37",
            "",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\5"+
            "\37\1\56\11\37\1\55\13\37\4\uffff\6\37\1\56\11\37\1\55\13\37"+
            "\4\uffff\uff81\37",
            "",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\60\1\uffff\12\62\1\uffff\4\37\1\41\33"+
            "\37\1\uffff\1\40\2\uffff\34\37\4\uffff\uff81\37",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\33"+
            "\37\4\uffff\34\37\4\uffff\uff81\37",
            "",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\33"+
            "\37\4\uffff\34\37\4\uffff\uff81\37",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\42\47\1\63\7\47\1\50\24\47\1\50\uffc0\47",
            "",
            "\11\40\2\uffff\2\40\1\uffff\22\40\3\uffff\3\40\6\uffff\1\40"+
            "\1\uffff\1\40\1\uffff\12\40\1\uffff\4\40\1\uffff\33\40\1\uffff"+
            "\1\40\2\uffff\34\40\4\uffff\uff81\40",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\33"+
            "\37\1\uffff\1\40\2\uffff\34\37\4\uffff\uff81\37",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\4"+
            "\37\1\66\26\37\4\uffff\5\37\1\66\26\37\4\uffff\uff81\37",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\33"+
            "\37\1\uffff\1\40\2\uffff\34\37\4\uffff\uff81\37",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\24"+
            "\37\1\67\6\37\4\uffff\25\37\1\67\6\37\4\uffff\uff81\37",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\1"+
            "\37\1\70\31\37\4\uffff\2\37\1\70\31\37\4\uffff\uff81\37",
            "",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\71\1\uffff\4\37\1\41\33"+
            "\37\4\uffff\34\37\4\uffff\uff81\37",
            "",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\60\1\uffff\12\62\1\uffff\4\37\1\41\33"+
            "\37\1\uffff\1\40\2\uffff\34\37\4\uffff\uff81\37",
            "",
            "",
            "",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\33"+
            "\37\1\uffff\1\40\2\uffff\34\37\4\uffff\uff81\37",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\33"+
            "\37\1\uffff\1\40\2\uffff\34\37\4\uffff\uff81\37",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\22"+
            "\37\1\73\10\37\4\uffff\23\37\1\73\10\37\4\uffff\uff81\37",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\71\1\uffff\4\37\1\41\33"+
            "\37\1\uffff\1\40\2\uffff\34\37\4\uffff\uff81\37",
            "",
            "\11\37\2\uffff\2\37\1\uffff\22\37\3\uffff\3\37\4\uffff\1\41"+
            "\1\uffff\1\37\1\uffff\1\37\1\uffff\12\37\1\uffff\4\37\1\41\33"+
            "\37\1\uffff\1\40\2\uffff\34\37\4\uffff\uff81\37"
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
            return "1:1: Tokens : ( T__51 | T__52 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | ESC_CHAR | TO | AND | OR | NOT | NEAR | WS | NUMBER | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA15_28 = input.LA(1);

                        s = -1;
                        if ( (LA15_28=='.') ) {s = 48;}

                        else if ( ((LA15_28 >= '0' && LA15_28 <= '9')) ) {s = 50;}

                        else if ( ((LA15_28 >= '\u0000' && LA15_28 <= '\b')||(LA15_28 >= '\u000B' && LA15_28 <= '\f')||(LA15_28 >= '\u000E' && LA15_28 <= '\u001F')||(LA15_28 >= '#' && LA15_28 <= '%')||LA15_28==','||(LA15_28 >= ';' && LA15_28 <= '>')||(LA15_28 >= '@' && LA15_28 <= 'Z')||(LA15_28 >= '_' && LA15_28 <= 'z')||(LA15_28 >= '\u007F' && LA15_28 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_28=='\\') ) {s = 32;}

                        else if ( (LA15_28=='*'||LA15_28=='?') ) {s = 33;}

                        else s = 49;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA15_43 = input.LA(1);

                        s = -1;
                        if ( (LA15_43=='D'||LA15_43=='d') ) {s = 54;}

                        else if ( ((LA15_43 >= '\u0000' && LA15_43 <= '\b')||(LA15_43 >= '\u000B' && LA15_43 <= '\f')||(LA15_43 >= '\u000E' && LA15_43 <= '\u001F')||(LA15_43 >= '#' && LA15_43 <= '%')||LA15_43==','||LA15_43=='.'||(LA15_43 >= '0' && LA15_43 <= '9')||(LA15_43 >= ';' && LA15_43 <= '>')||(LA15_43 >= '@' && LA15_43 <= 'C')||(LA15_43 >= 'E' && LA15_43 <= 'Z')||(LA15_43 >= '_' && LA15_43 <= 'c')||(LA15_43 >= 'e' && LA15_43 <= 'z')||(LA15_43 >= '\u007F' && LA15_43 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_43=='*'||LA15_43=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA15_29 = input.LA(1);

                        s = -1;
                        if ( ((LA15_29 >= '\u0000' && LA15_29 <= '\b')||(LA15_29 >= '\u000B' && LA15_29 <= '\f')||(LA15_29 >= '\u000E' && LA15_29 <= '\u001F')||(LA15_29 >= '#' && LA15_29 <= '%')||LA15_29==','||LA15_29=='.'||(LA15_29 >= '0' && LA15_29 <= '9')||(LA15_29 >= ';' && LA15_29 <= '>')||(LA15_29 >= '@' && LA15_29 <= 'Z')||(LA15_29 >= '_' && LA15_29 <= 'z')||(LA15_29 >= '\u007F' && LA15_29 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_29=='*'||LA15_29=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA15_26 = input.LA(1);

                        s = -1;
                        if ( (LA15_26=='O'||LA15_26=='o') ) {s = 45;}

                        else if ( (LA15_26=='E'||LA15_26=='e') ) {s = 46;}

                        else if ( ((LA15_26 >= '\u0000' && LA15_26 <= '\b')||(LA15_26 >= '\u000B' && LA15_26 <= '\f')||(LA15_26 >= '\u000E' && LA15_26 <= '\u001F')||(LA15_26 >= '#' && LA15_26 <= '%')||LA15_26==','||LA15_26=='.'||(LA15_26 >= '0' && LA15_26 <= '9')||(LA15_26 >= ';' && LA15_26 <= '>')||(LA15_26 >= '@' && LA15_26 <= 'D')||(LA15_26 >= 'F' && LA15_26 <= 'N')||(LA15_26 >= 'P' && LA15_26 <= 'Z')||(LA15_26 >= '_' && LA15_26 <= 'd')||(LA15_26 >= 'f' && LA15_26 <= 'n')||(LA15_26 >= 'p' && LA15_26 <= 'z')||(LA15_26 >= '\u007F' && LA15_26 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_26=='*'||LA15_26=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA15_20 = input.LA(1);

                        s = -1;
                        if ( (LA15_20=='N'||LA15_20=='n') ) {s = 43;}

                        else if ( ((LA15_20 >= '\u0000' && LA15_20 <= '\b')||(LA15_20 >= '\u000B' && LA15_20 <= '\f')||(LA15_20 >= '\u000E' && LA15_20 <= '\u001F')||(LA15_20 >= '#' && LA15_20 <= '%')||LA15_20==','||LA15_20=='.'||(LA15_20 >= '0' && LA15_20 <= '9')||(LA15_20 >= ';' && LA15_20 <= '>')||(LA15_20 >= '@' && LA15_20 <= 'M')||(LA15_20 >= 'O' && LA15_20 <= 'Z')||(LA15_20 >= '_' && LA15_20 <= 'm')||(LA15_20 >= 'o' && LA15_20 <= 'z')||(LA15_20 >= '\u007F' && LA15_20 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_20=='*'||LA15_20=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA15_16 = input.LA(1);

                        s = -1;
                        if ( ((LA15_16 >= '\u0000' && LA15_16 <= '!')||(LA15_16 >= '#' && LA15_16 <= ')')||(LA15_16 >= '+' && LA15_16 <= '>')||(LA15_16 >= '@' && LA15_16 <= '\uFFFF')) ) {s = 39;}

                        else if ( (LA15_16=='*'||LA15_16=='?') ) {s = 40;}

                        else s = 38;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA15_10 = input.LA(1);

                        s = -1;
                        if ( ((LA15_10 >= '\u0000' && LA15_10 <= '\b')||(LA15_10 >= '\u000B' && LA15_10 <= '\f')||(LA15_10 >= '\u000E' && LA15_10 <= '\u001F')||(LA15_10 >= '#' && LA15_10 <= '%')||LA15_10=='*'||LA15_10==','||LA15_10=='.'||(LA15_10 >= '0' && LA15_10 <= '9')||(LA15_10 >= ';' && LA15_10 <= 'Z')||(LA15_10 >= '_' && LA15_10 <= 'z')||(LA15_10 >= '\u007F' && LA15_10 <= '\uFFFF')) ) {s = 33;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA15_50 = input.LA(1);

                        s = -1;
                        if ( (LA15_50=='.') ) {s = 48;}

                        else if ( ((LA15_50 >= '0' && LA15_50 <= '9')) ) {s = 50;}

                        else if ( ((LA15_50 >= '\u0000' && LA15_50 <= '\b')||(LA15_50 >= '\u000B' && LA15_50 <= '\f')||(LA15_50 >= '\u000E' && LA15_50 <= '\u001F')||(LA15_50 >= '#' && LA15_50 <= '%')||LA15_50==','||(LA15_50 >= ';' && LA15_50 <= '>')||(LA15_50 >= '@' && LA15_50 <= 'Z')||(LA15_50 >= '_' && LA15_50 <= 'z')||(LA15_50 >= '\u007F' && LA15_50 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_50=='\\') ) {s = 32;}

                        else if ( (LA15_50=='*'||LA15_50=='?') ) {s = 33;}

                        else s = 49;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA15_44 = input.LA(1);

                        s = -1;
                        if ( ((LA15_44 >= '\u0000' && LA15_44 <= '\b')||(LA15_44 >= '\u000B' && LA15_44 <= '\f')||(LA15_44 >= '\u000E' && LA15_44 <= '\u001F')||(LA15_44 >= '#' && LA15_44 <= '%')||LA15_44==','||LA15_44=='.'||(LA15_44 >= '0' && LA15_44 <= '9')||(LA15_44 >= ';' && LA15_44 <= '>')||(LA15_44 >= '@' && LA15_44 <= 'Z')||(LA15_44 >= '_' && LA15_44 <= 'z')||(LA15_44 >= '\u007F' && LA15_44 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_44=='\\') ) {s = 32;}

                        else if ( (LA15_44=='*'||LA15_44=='?') ) {s = 33;}

                        else s = 23;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA15_19 = input.LA(1);

                        s = -1;
                        if ( (LA15_19=='O') ) {s = 42;}

                        else if ( ((LA15_19 >= '\u0000' && LA15_19 <= '\b')||(LA15_19 >= '\u000B' && LA15_19 <= '\f')||(LA15_19 >= '\u000E' && LA15_19 <= '\u001F')||(LA15_19 >= '#' && LA15_19 <= '%')||LA15_19==','||LA15_19=='.'||(LA15_19 >= '0' && LA15_19 <= '9')||(LA15_19 >= ';' && LA15_19 <= '>')||(LA15_19 >= '@' && LA15_19 <= 'N')||(LA15_19 >= 'P' && LA15_19 <= 'Z')||(LA15_19 >= '_' && LA15_19 <= 'z')||(LA15_19 >= '\u007F' && LA15_19 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_19=='*'||LA15_19=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA15_59 = input.LA(1);

                        s = -1;
                        if ( ((LA15_59 >= '\u0000' && LA15_59 <= '\b')||(LA15_59 >= '\u000B' && LA15_59 <= '\f')||(LA15_59 >= '\u000E' && LA15_59 <= '\u001F')||(LA15_59 >= '#' && LA15_59 <= '%')||LA15_59==','||LA15_59=='.'||(LA15_59 >= '0' && LA15_59 <= '9')||(LA15_59 >= ';' && LA15_59 <= '>')||(LA15_59 >= '@' && LA15_59 <= 'Z')||(LA15_59 >= '_' && LA15_59 <= 'z')||(LA15_59 >= '\u007F' && LA15_59 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_59=='\\') ) {s = 32;}

                        else if ( (LA15_59=='*'||LA15_59=='?') ) {s = 33;}

                        else s = 47;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA15_45 = input.LA(1);

                        s = -1;
                        if ( (LA15_45=='T'||LA15_45=='t') ) {s = 55;}

                        else if ( ((LA15_45 >= '\u0000' && LA15_45 <= '\b')||(LA15_45 >= '\u000B' && LA15_45 <= '\f')||(LA15_45 >= '\u000E' && LA15_45 <= '\u001F')||(LA15_45 >= '#' && LA15_45 <= '%')||LA15_45==','||LA15_45=='.'||(LA15_45 >= '0' && LA15_45 <= '9')||(LA15_45 >= ';' && LA15_45 <= '>')||(LA15_45 >= '@' && LA15_45 <= 'S')||(LA15_45 >= 'U' && LA15_45 <= 'Z')||(LA15_45 >= '_' && LA15_45 <= 's')||(LA15_45 >= 'u' && LA15_45 <= 'z')||(LA15_45 >= '\u007F' && LA15_45 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_45=='*'||LA15_45=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA15_57 = input.LA(1);

                        s = -1;
                        if ( ((LA15_57 >= '0' && LA15_57 <= '9')) ) {s = 57;}

                        else if ( ((LA15_57 >= '\u0000' && LA15_57 <= '\b')||(LA15_57 >= '\u000B' && LA15_57 <= '\f')||(LA15_57 >= '\u000E' && LA15_57 <= '\u001F')||(LA15_57 >= '#' && LA15_57 <= '%')||LA15_57==','||LA15_57=='.'||(LA15_57 >= ';' && LA15_57 <= '>')||(LA15_57 >= '@' && LA15_57 <= 'Z')||(LA15_57 >= '_' && LA15_57 <= 'z')||(LA15_57 >= '\u007F' && LA15_57 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_57=='\\') ) {s = 32;}

                        else if ( (LA15_57=='*'||LA15_57=='?') ) {s = 33;}

                        else s = 49;

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA15_22 = input.LA(1);

                        s = -1;
                        if ( (LA15_22=='R'||LA15_22=='r') ) {s = 44;}

                        else if ( ((LA15_22 >= '\u0000' && LA15_22 <= '\b')||(LA15_22 >= '\u000B' && LA15_22 <= '\f')||(LA15_22 >= '\u000E' && LA15_22 <= '\u001F')||(LA15_22 >= '#' && LA15_22 <= '%')||LA15_22==','||LA15_22=='.'||(LA15_22 >= '0' && LA15_22 <= '9')||(LA15_22 >= ';' && LA15_22 <= '>')||(LA15_22 >= '@' && LA15_22 <= 'Q')||(LA15_22 >= 'S' && LA15_22 <= 'Z')||(LA15_22 >= '_' && LA15_22 <= 'q')||(LA15_22 >= 's' && LA15_22 <= 'z')||(LA15_22 >= '\u007F' && LA15_22 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_22=='*'||LA15_22=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA15_18 = input.LA(1);

                        s = -1;
                        if ( ((LA15_18 >= '\u0000' && LA15_18 <= '\uFFFF')) ) {s = 41;}

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA15_54 = input.LA(1);

                        s = -1;
                        if ( ((LA15_54 >= '\u0000' && LA15_54 <= '\b')||(LA15_54 >= '\u000B' && LA15_54 <= '\f')||(LA15_54 >= '\u000E' && LA15_54 <= '\u001F')||(LA15_54 >= '#' && LA15_54 <= '%')||LA15_54==','||LA15_54=='.'||(LA15_54 >= '0' && LA15_54 <= '9')||(LA15_54 >= ';' && LA15_54 <= '>')||(LA15_54 >= '@' && LA15_54 <= 'Z')||(LA15_54 >= '_' && LA15_54 <= 'z')||(LA15_54 >= '\u007F' && LA15_54 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_54=='\\') ) {s = 32;}

                        else if ( (LA15_54=='*'||LA15_54=='?') ) {s = 33;}

                        else s = 21;

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA15_39 = input.LA(1);

                        s = -1;
                        if ( (LA15_39=='\"') ) {s = 51;}

                        else if ( ((LA15_39 >= '\u0000' && LA15_39 <= '!')||(LA15_39 >= '#' && LA15_39 <= ')')||(LA15_39 >= '+' && LA15_39 <= '>')||(LA15_39 >= '@' && LA15_39 <= '\uFFFF')) ) {s = 39;}

                        else if ( (LA15_39=='*'||LA15_39=='?') ) {s = 40;}

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA15_56 = input.LA(1);

                        s = -1;
                        if ( (LA15_56=='R'||LA15_56=='r') ) {s = 59;}

                        else if ( ((LA15_56 >= '\u0000' && LA15_56 <= '\b')||(LA15_56 >= '\u000B' && LA15_56 <= '\f')||(LA15_56 >= '\u000E' && LA15_56 <= '\u001F')||(LA15_56 >= '#' && LA15_56 <= '%')||LA15_56==','||LA15_56=='.'||(LA15_56 >= '0' && LA15_56 <= '9')||(LA15_56 >= ';' && LA15_56 <= '>')||(LA15_56 >= '@' && LA15_56 <= 'Q')||(LA15_56 >= 'S' && LA15_56 <= 'Z')||(LA15_56 >= '_' && LA15_56 <= 'q')||(LA15_56 >= 's' && LA15_56 <= 'z')||(LA15_56 >= '\u007F' && LA15_56 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_56=='*'||LA15_56=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA15_46 = input.LA(1);

                        s = -1;
                        if ( (LA15_46=='A'||LA15_46=='a') ) {s = 56;}

                        else if ( ((LA15_46 >= '\u0000' && LA15_46 <= '\b')||(LA15_46 >= '\u000B' && LA15_46 <= '\f')||(LA15_46 >= '\u000E' && LA15_46 <= '\u001F')||(LA15_46 >= '#' && LA15_46 <= '%')||LA15_46==','||LA15_46=='.'||(LA15_46 >= '0' && LA15_46 <= '9')||(LA15_46 >= ';' && LA15_46 <= '>')||LA15_46=='@'||(LA15_46 >= 'B' && LA15_46 <= 'Z')||(LA15_46 >= '_' && LA15_46 <= '`')||(LA15_46 >= 'b' && LA15_46 <= 'z')||(LA15_46 >= '\u007F' && LA15_46 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_46=='*'||LA15_46=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA15_48 = input.LA(1);

                        s = -1;
                        if ( ((LA15_48 >= '0' && LA15_48 <= '9')) ) {s = 57;}

                        else if ( ((LA15_48 >= '\u0000' && LA15_48 <= '\b')||(LA15_48 >= '\u000B' && LA15_48 <= '\f')||(LA15_48 >= '\u000E' && LA15_48 <= '\u001F')||(LA15_48 >= '#' && LA15_48 <= '%')||LA15_48==','||LA15_48=='.'||(LA15_48 >= ';' && LA15_48 <= '>')||(LA15_48 >= '@' && LA15_48 <= 'Z')||(LA15_48 >= '_' && LA15_48 <= 'z')||(LA15_48 >= '\u007F' && LA15_48 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_48=='*'||LA15_48=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA15_1 = input.LA(1);

                        s = -1;
                        if ( ((LA15_1 >= '\u0000' && LA15_1 <= '\b')||(LA15_1 >= '\u000B' && LA15_1 <= '\f')||(LA15_1 >= '\u000E' && LA15_1 <= '\u001F')||(LA15_1 >= '#' && LA15_1 <= '%')||LA15_1==','||LA15_1=='.'||(LA15_1 >= '0' && LA15_1 <= '9')||(LA15_1 >= ';' && LA15_1 <= '>')||(LA15_1 >= '@' && LA15_1 <= 'Z')||(LA15_1 >= '_' && LA15_1 <= 'z')||(LA15_1 >= '\u007F' && LA15_1 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_1=='\\') ) {s = 32;}

                        else if ( (LA15_1=='*'||LA15_1=='?') ) {s = 33;}

                        else s = 30;

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA15_24 = input.LA(1);

                        s = -1;
                        if ( (LA15_24=='O'||LA15_24=='o') ) {s = 45;}

                        else if ( (LA15_24=='E'||LA15_24=='e') ) {s = 46;}

                        else if ( ((LA15_24 >= '\u0000' && LA15_24 <= '\b')||(LA15_24 >= '\u000B' && LA15_24 <= '\f')||(LA15_24 >= '\u000E' && LA15_24 <= '\u001F')||(LA15_24 >= '#' && LA15_24 <= '%')||LA15_24==','||LA15_24=='.'||(LA15_24 >= '0' && LA15_24 <= '9')||(LA15_24 >= ';' && LA15_24 <= '>')||(LA15_24 >= '@' && LA15_24 <= 'D')||(LA15_24 >= 'F' && LA15_24 <= 'N')||(LA15_24 >= 'P' && LA15_24 <= 'Z')||(LA15_24 >= '_' && LA15_24 <= 'd')||(LA15_24 >= 'f' && LA15_24 <= 'n')||(LA15_24 >= 'p' && LA15_24 <= 'z')||(LA15_24 >= '\u007F' && LA15_24 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_24=='\\') ) {s = 32;}

                        else if ( (LA15_24=='*'||LA15_24=='?') ) {s = 33;}

                        else s = 47;

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA15_41 = input.LA(1);

                        s = -1;
                        if ( ((LA15_41 >= '\u0000' && LA15_41 <= '\b')||(LA15_41 >= '\u000B' && LA15_41 <= '\f')||(LA15_41 >= '\u000E' && LA15_41 <= '\u001F')||(LA15_41 >= '#' && LA15_41 <= '%')||LA15_41==','||LA15_41=='.'||(LA15_41 >= '0' && LA15_41 <= '9')||(LA15_41 >= ';' && LA15_41 <= '>')||(LA15_41 >= '@' && LA15_41 <= 'Z')||LA15_41=='\\'||(LA15_41 >= '_' && LA15_41 <= 'z')||(LA15_41 >= '\u007F' && LA15_41 <= '\uFFFF')) ) {s = 32;}

                        else s = 52;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA15_55 = input.LA(1);

                        s = -1;
                        if ( ((LA15_55 >= '\u0000' && LA15_55 <= '\b')||(LA15_55 >= '\u000B' && LA15_55 <= '\f')||(LA15_55 >= '\u000E' && LA15_55 <= '\u001F')||(LA15_55 >= '#' && LA15_55 <= '%')||LA15_55==','||LA15_55=='.'||(LA15_55 >= '0' && LA15_55 <= '9')||(LA15_55 >= ';' && LA15_55 <= '>')||(LA15_55 >= '@' && LA15_55 <= 'Z')||(LA15_55 >= '_' && LA15_55 <= 'z')||(LA15_55 >= '\u007F' && LA15_55 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_55=='\\') ) {s = 32;}

                        else if ( (LA15_55=='*'||LA15_55=='?') ) {s = 33;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA15_31 = input.LA(1);

                        s = -1;
                        if ( ((LA15_31 >= '\u0000' && LA15_31 <= '\b')||(LA15_31 >= '\u000B' && LA15_31 <= '\f')||(LA15_31 >= '\u000E' && LA15_31 <= '\u001F')||(LA15_31 >= '#' && LA15_31 <= '%')||LA15_31==','||LA15_31=='.'||(LA15_31 >= '0' && LA15_31 <= '9')||(LA15_31 >= ';' && LA15_31 <= '>')||(LA15_31 >= '@' && LA15_31 <= 'Z')||(LA15_31 >= '_' && LA15_31 <= 'z')||(LA15_31 >= '\u007F' && LA15_31 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_31=='*'||LA15_31=='?') ) {s = 33;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA15_11 = input.LA(1);

                        s = -1;
                        if ( ((LA15_11 >= '\u0000' && LA15_11 <= '\b')||(LA15_11 >= '\u000B' && LA15_11 <= '\f')||(LA15_11 >= '\u000E' && LA15_11 <= '\u001F')||(LA15_11 >= '#' && LA15_11 <= '%')||LA15_11=='*'||LA15_11==','||LA15_11=='.'||(LA15_11 >= '0' && LA15_11 <= '9')||(LA15_11 >= ';' && LA15_11 <= 'Z')||(LA15_11 >= '_' && LA15_11 <= 'z')||(LA15_11 >= '\u007F' && LA15_11 <= '\uFFFF')) ) {s = 33;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA15_0 = input.LA(1);

                        s = -1;
                        if ( (LA15_0=='#') ) {s = 1;}

                        else if ( (LA15_0=='/') ) {s = 2;}

                        else if ( (LA15_0=='(') ) {s = 3;}

                        else if ( (LA15_0==')') ) {s = 4;}

                        else if ( (LA15_0=='[') ) {s = 5;}

                        else if ( (LA15_0==']') ) {s = 6;}

                        else if ( (LA15_0==':') ) {s = 7;}

                        else if ( (LA15_0=='+') ) {s = 8;}

                        else if ( (LA15_0=='-') ) {s = 9;}

                        else if ( (LA15_0=='*') ) {s = 10;}

                        else if ( (LA15_0=='?') ) {s = 11;}

                        else if ( (LA15_0=='{') ) {s = 12;}

                        else if ( (LA15_0=='}') ) {s = 13;}

                        else if ( (LA15_0=='^') ) {s = 14;}

                        else if ( (LA15_0=='~') ) {s = 15;}

                        else if ( (LA15_0=='\"') ) {s = 16;}

                        else if ( (LA15_0=='\'') ) {s = 17;}

                        else if ( (LA15_0=='\\') ) {s = 18;}

                        else if ( (LA15_0=='T') ) {s = 19;}

                        else if ( (LA15_0=='A'||LA15_0=='a') ) {s = 20;}

                        else if ( (LA15_0=='&') ) {s = 21;}

                        else if ( (LA15_0=='O'||LA15_0=='o') ) {s = 22;}

                        else if ( (LA15_0=='|') ) {s = 23;}

                        else if ( (LA15_0=='n') ) {s = 24;}

                        else if ( (LA15_0=='!') ) {s = 25;}

                        else if ( (LA15_0=='N') ) {s = 26;}

                        else if ( ((LA15_0 >= '\t' && LA15_0 <= '\n')||LA15_0=='\r'||LA15_0==' ') ) {s = 27;}

                        else if ( ((LA15_0 >= '0' && LA15_0 <= '9')) ) {s = 28;}

                        else if ( ((LA15_0 >= '\u0000' && LA15_0 <= '\b')||(LA15_0 >= '\u000B' && LA15_0 <= '\f')||(LA15_0 >= '\u000E' && LA15_0 <= '\u001F')||(LA15_0 >= '$' && LA15_0 <= '%')||LA15_0==','||LA15_0=='.'||(LA15_0 >= ';' && LA15_0 <= '>')||LA15_0=='@'||(LA15_0 >= 'B' && LA15_0 <= 'M')||(LA15_0 >= 'P' && LA15_0 <= 'S')||(LA15_0 >= 'U' && LA15_0 <= 'Z')||(LA15_0 >= '_' && LA15_0 <= '`')||(LA15_0 >= 'b' && LA15_0 <= 'm')||(LA15_0 >= 'p' && LA15_0 <= 'z')||(LA15_0 >= '\u007F' && LA15_0 <= '\uFFFF')) ) {s = 29;}

                        if ( s>=0 ) return s;
                        break;

                    case 27 : 
                        int LA15_42 = input.LA(1);

                        s = -1;
                        if ( ((LA15_42 >= '\u0000' && LA15_42 <= '\b')||(LA15_42 >= '\u000B' && LA15_42 <= '\f')||(LA15_42 >= '\u000E' && LA15_42 <= '\u001F')||(LA15_42 >= '#' && LA15_42 <= '%')||LA15_42==','||LA15_42=='.'||(LA15_42 >= '0' && LA15_42 <= '9')||(LA15_42 >= ';' && LA15_42 <= '>')||(LA15_42 >= '@' && LA15_42 <= 'Z')||(LA15_42 >= '_' && LA15_42 <= 'z')||(LA15_42 >= '\u007F' && LA15_42 <= '\uFFFF')) ) {s = 31;}

                        else if ( (LA15_42=='\\') ) {s = 32;}

                        else if ( (LA15_42=='*'||LA15_42=='?') ) {s = 33;}

                        else s = 53;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 15, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}