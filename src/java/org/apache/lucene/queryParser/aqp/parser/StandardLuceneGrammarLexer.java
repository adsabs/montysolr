// $ANTLR 3.4 StandardLuceneGrammar.g 2011-10-31 01:29:16

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
            // StandardLuceneGrammar.g:217:9: ( '(' )
            // StandardLuceneGrammar.g:217:11: '('
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
            // StandardLuceneGrammar.g:219:9: ( ')' )
            // StandardLuceneGrammar.g:219:11: ')'
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
            // StandardLuceneGrammar.g:221:9: ( '[' )
            // StandardLuceneGrammar.g:221:11: '['
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
            // StandardLuceneGrammar.g:223:9: ( ']' )
            // StandardLuceneGrammar.g:223:11: ']'
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
            // StandardLuceneGrammar.g:225:9: ( ':' )
            // StandardLuceneGrammar.g:225:11: ':'
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
            // StandardLuceneGrammar.g:227:7: ( '+' )
            // StandardLuceneGrammar.g:227:9: '+'
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
            // StandardLuceneGrammar.g:229:7: ( '-' )
            // StandardLuceneGrammar.g:229:9: '-'
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
            // StandardLuceneGrammar.g:231:7: ( '*' )
            // StandardLuceneGrammar.g:231:9: '*'
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
            // StandardLuceneGrammar.g:233:8: ( '?' )
            // StandardLuceneGrammar.g:233:10: '?'
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
            // StandardLuceneGrammar.g:235:16: ( '|' )
            // StandardLuceneGrammar.g:235:18: '|'
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
            // StandardLuceneGrammar.g:237:16: ( '&' )
            // StandardLuceneGrammar.g:237:18: '&'
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
            // StandardLuceneGrammar.g:239:9: ( '{' )
            // StandardLuceneGrammar.g:239:11: '{'
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
            // StandardLuceneGrammar.g:241:9: ( '}' )
            // StandardLuceneGrammar.g:241:11: '}'
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
            // StandardLuceneGrammar.g:243:7: ( '^' )
            // StandardLuceneGrammar.g:243:9: '^'
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
            // StandardLuceneGrammar.g:245:7: ( '~' )
            // StandardLuceneGrammar.g:245:9: '~'
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
            // StandardLuceneGrammar.g:248:2: ( '\\\"' )
            // StandardLuceneGrammar.g:248:4: '\\\"'
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
            // StandardLuceneGrammar.g:251:2: ( '\\'' )
            // StandardLuceneGrammar.g:251:4: '\\''
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
            // StandardLuceneGrammar.g:254:18: ( '\\\\' . )
            // StandardLuceneGrammar.g:254:21: '\\\\' .
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
            // StandardLuceneGrammar.g:256:4: ( 'TO' )
            // StandardLuceneGrammar.g:256:6: 'TO'
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
            // StandardLuceneGrammar.g:259:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) ) )
            // StandardLuceneGrammar.g:259:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
            {
            // StandardLuceneGrammar.g:259:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
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
                    // StandardLuceneGrammar.g:259:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
                    // StandardLuceneGrammar.g:259:48: ( AMPER ( AMPER )? )
                    {
                    // StandardLuceneGrammar.g:259:48: ( AMPER ( AMPER )? )
                    // StandardLuceneGrammar.g:259:49: AMPER ( AMPER )?
                    {
                    mAMPER(); 


                    // StandardLuceneGrammar.g:259:55: ( AMPER )?
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
            // StandardLuceneGrammar.g:260:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) ) )
            // StandardLuceneGrammar.g:260:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
            {
            // StandardLuceneGrammar.g:260:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
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
                    // StandardLuceneGrammar.g:260:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
                    // StandardLuceneGrammar.g:260:34: ( VBAR ( VBAR )? )
                    {
                    // StandardLuceneGrammar.g:260:34: ( VBAR ( VBAR )? )
                    // StandardLuceneGrammar.g:260:35: VBAR ( VBAR )?
                    {
                    mVBAR(); 


                    // StandardLuceneGrammar.g:260:40: ( VBAR )?
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
            // StandardLuceneGrammar.g:261:7: ( ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) | '!' ) )
            // StandardLuceneGrammar.g:261:9: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) | '!' )
            {
            // StandardLuceneGrammar.g:261:9: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) | '!' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='N'||LA5_0=='n') ) {
                alt5=1;
            }
            else if ( (LA5_0=='!') ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // StandardLuceneGrammar.g:261:10: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
                    // StandardLuceneGrammar.g:261:48: '!'
                    {
                    match('!'); 

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
            // StandardLuceneGrammar.g:262:7: ( ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' ) )
            // StandardLuceneGrammar.g:262:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            {
            // StandardLuceneGrammar.g:262:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
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
                    // StandardLuceneGrammar.g:262:10: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
                    // StandardLuceneGrammar.g:262:60: 'n'
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
            // StandardLuceneGrammar.g:265:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // StandardLuceneGrammar.g:265:9: ( ' ' | '\\t' | '\\r' | '\\n' )
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
            // StandardLuceneGrammar.g:274:13: ( '0' .. '9' )
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
            // StandardLuceneGrammar.g:276:23: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' ) )
            // StandardLuceneGrammar.g:276:25: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' )
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
            // StandardLuceneGrammar.g:285:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // StandardLuceneGrammar.g:286:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // StandardLuceneGrammar.g:286:2: ( INT )+
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


            // StandardLuceneGrammar.g:286:7: ( '.' ( INT )+ )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='.') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // StandardLuceneGrammar.g:286:8: '.' ( INT )+
                    {
                    match('.'); 

                    // StandardLuceneGrammar.g:286:12: ( INT )+
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

    // $ANTLR start "TERM_NORMAL"
    public final void mTERM_NORMAL() throws RecognitionException {
        try {
            int _type = TERM_NORMAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // StandardLuceneGrammar.g:291:2: ( ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )* )
            // StandardLuceneGrammar.g:292:2: ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )*
            {
            // StandardLuceneGrammar.g:292:2: ( NORMAL_CHAR | ESC_CHAR )
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
                    // StandardLuceneGrammar.g:292:4: NORMAL_CHAR
                    {
                    mNORMAL_CHAR(); 


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:292:18: ESC_CHAR
                    {
                    mESC_CHAR(); 


                    }
                    break;

            }


            // StandardLuceneGrammar.g:292:28: ( NORMAL_CHAR | ESC_CHAR )*
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
            	    // StandardLuceneGrammar.g:292:30: NORMAL_CHAR
            	    {
            	    mNORMAL_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // StandardLuceneGrammar.g:292:44: ESC_CHAR
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
            // StandardLuceneGrammar.g:296:15: ( ( STAR | QMARK ) ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )* ( NORMAL_CHAR | ESC_CHAR )* | ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+ ( NORMAL_CHAR | ESC_CHAR )* )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0=='*'||LA18_0=='?') ) {
                alt18=1;
            }
            else if ( ((LA18_0 >= '\u0000' && LA18_0 <= '\b')||(LA18_0 >= '\u000B' && LA18_0 <= '\f')||(LA18_0 >= '\u000E' && LA18_0 <= '\u001F')||(LA18_0 >= '#' && LA18_0 <= '%')||LA18_0==','||LA18_0=='.'||(LA18_0 >= '0' && LA18_0 <= '9')||(LA18_0 >= ';' && LA18_0 <= '>')||(LA18_0 >= '@' && LA18_0 <= 'Z')||LA18_0=='\\'||(LA18_0 >= '_' && LA18_0 <= 'z')||(LA18_0 >= '\u007F' && LA18_0 <= '\uFFFF')) ) {
                alt18=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }
            switch (alt18) {
                case 1 :
                    // StandardLuceneGrammar.g:297:2: ( STAR | QMARK ) ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )* ( NORMAL_CHAR | ESC_CHAR )*
                    {
                    if ( input.LA(1)=='*'||input.LA(1)=='?' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    // StandardLuceneGrammar.g:297:15: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )*
                    loop13:
                    do {
                        int alt13=2;
                        alt13 = dfa13.predict(input);
                        switch (alt13) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:297:16: ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // StandardLuceneGrammar.g:297:16: ( NORMAL_CHAR | ESC_CHAR )+
                    	    int cnt12=0;
                    	    loop12:
                    	    do {
                    	        int alt12=3;
                    	        int LA12_0 = input.LA(1);

                    	        if ( ((LA12_0 >= '\u0000' && LA12_0 <= '\b')||(LA12_0 >= '\u000B' && LA12_0 <= '\f')||(LA12_0 >= '\u000E' && LA12_0 <= '\u001F')||(LA12_0 >= '#' && LA12_0 <= '%')||LA12_0==','||LA12_0=='.'||(LA12_0 >= '0' && LA12_0 <= '9')||(LA12_0 >= ';' && LA12_0 <= '>')||(LA12_0 >= '@' && LA12_0 <= 'Z')||(LA12_0 >= '_' && LA12_0 <= 'z')||(LA12_0 >= '\u007F' && LA12_0 <= '\uFFFF')) ) {
                    	            alt12=1;
                    	        }
                    	        else if ( (LA12_0=='\\') ) {
                    	            alt12=2;
                    	        }


                    	        switch (alt12) {
                    	    	case 1 :
                    	    	    // StandardLuceneGrammar.g:297:17: NORMAL_CHAR
                    	    	    {
                    	    	    mNORMAL_CHAR(); 


                    	    	    }
                    	    	    break;
                    	    	case 2 :
                    	    	    // StandardLuceneGrammar.g:297:29: ESC_CHAR
                    	    	    {
                    	    	    mESC_CHAR(); 


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
                    	    break loop13;
                        }
                    } while (true);


                    // StandardLuceneGrammar.g:297:55: ( NORMAL_CHAR | ESC_CHAR )*
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
                    	    // StandardLuceneGrammar.g:297:56: NORMAL_CHAR
                    	    {
                    	    mNORMAL_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // StandardLuceneGrammar.g:297:68: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop14;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:298:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+ ( NORMAL_CHAR | ESC_CHAR )*
                    {
                    // StandardLuceneGrammar.g:298:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+
                    int cnt16=0;
                    loop16:
                    do {
                        int alt16=2;
                        alt16 = dfa16.predict(input);
                        switch (alt16) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:298:5: ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // StandardLuceneGrammar.g:298:5: ( NORMAL_CHAR | ESC_CHAR )+
                    	    int cnt15=0;
                    	    loop15:
                    	    do {
                    	        int alt15=3;
                    	        int LA15_0 = input.LA(1);

                    	        if ( ((LA15_0 >= '\u0000' && LA15_0 <= '\b')||(LA15_0 >= '\u000B' && LA15_0 <= '\f')||(LA15_0 >= '\u000E' && LA15_0 <= '\u001F')||(LA15_0 >= '#' && LA15_0 <= '%')||LA15_0==','||LA15_0=='.'||(LA15_0 >= '0' && LA15_0 <= '9')||(LA15_0 >= ';' && LA15_0 <= '>')||(LA15_0 >= '@' && LA15_0 <= 'Z')||(LA15_0 >= '_' && LA15_0 <= 'z')||(LA15_0 >= '\u007F' && LA15_0 <= '\uFFFF')) ) {
                    	            alt15=1;
                    	        }
                    	        else if ( (LA15_0=='\\') ) {
                    	            alt15=2;
                    	        }


                    	        switch (alt15) {
                    	    	case 1 :
                    	    	    // StandardLuceneGrammar.g:298:6: NORMAL_CHAR
                    	    	    {
                    	    	    mNORMAL_CHAR(); 


                    	    	    }
                    	    	    break;
                    	    	case 2 :
                    	    	    // StandardLuceneGrammar.g:298:18: ESC_CHAR
                    	    	    {
                    	    	    mESC_CHAR(); 


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
                    	    if ( cnt16 >= 1 ) break loop16;
                                EarlyExitException eee =
                                    new EarlyExitException(16, input);
                                throw eee;
                        }
                        cnt16++;
                    } while (true);


                    // StandardLuceneGrammar.g:298:44: ( NORMAL_CHAR | ESC_CHAR )*
                    loop17:
                    do {
                        int alt17=3;
                        int LA17_0 = input.LA(1);

                        if ( ((LA17_0 >= '\u0000' && LA17_0 <= '\b')||(LA17_0 >= '\u000B' && LA17_0 <= '\f')||(LA17_0 >= '\u000E' && LA17_0 <= '\u001F')||(LA17_0 >= '#' && LA17_0 <= '%')||LA17_0==','||LA17_0=='.'||(LA17_0 >= '0' && LA17_0 <= '9')||(LA17_0 >= ';' && LA17_0 <= '>')||(LA17_0 >= '@' && LA17_0 <= 'Z')||(LA17_0 >= '_' && LA17_0 <= 'z')||(LA17_0 >= '\u007F' && LA17_0 <= '\uFFFF')) ) {
                            alt17=1;
                        }
                        else if ( (LA17_0=='\\') ) {
                            alt17=2;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // StandardLuceneGrammar.g:298:45: NORMAL_CHAR
                    	    {
                    	    mNORMAL_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // StandardLuceneGrammar.g:298:57: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop17;
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
            // StandardLuceneGrammar.g:303:2: ( DQUOTE (~ ( '\\\"' | '?' | '*' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:304:2: DQUOTE (~ ( '\\\"' | '?' | '*' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:304:9: (~ ( '\\\"' | '?' | '*' ) )+
            int cnt19=0;
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0 >= '\u0000' && LA19_0 <= '!')||(LA19_0 >= '#' && LA19_0 <= ')')||(LA19_0 >= '+' && LA19_0 <= '>')||(LA19_0 >= '@' && LA19_0 <= '\uFFFF')) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // StandardLuceneGrammar.g:
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
    // $ANTLR end "PHRASE"

    // $ANTLR start "PHRASE_ANYTHING"
    public final void mPHRASE_ANYTHING() throws RecognitionException {
        try {
            int _type = PHRASE_ANYTHING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // StandardLuceneGrammar.g:307:17: ( DQUOTE (~ ( '\\\"' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:308:2: DQUOTE (~ ( '\\\"' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:308:9: (~ ( '\\\"' ) )+
            int cnt20=0;
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( ((LA20_0 >= '\u0000' && LA20_0 <= '!')||(LA20_0 >= '#' && LA20_0 <= '\uFFFF')) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // StandardLuceneGrammar.g:
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
            	    if ( cnt20 >= 1 ) break loop20;
                        EarlyExitException eee =
                            new EarlyExitException(20, input);
                        throw eee;
                }
                cnt20++;
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
        int alt21=27;
        alt21 = dfa21.predict(input);
        switch (alt21) {
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


    protected DFA13 dfa13 = new DFA13(this);
    protected DFA16 dfa16 = new DFA16(this);
    protected DFA21 dfa21 = new DFA21(this);
    static final String DFA13_eotS =
        "\2\3\3\uffff\1\3";
    static final String DFA13_eofS =
        "\6\uffff";
    static final String DFA13_minS =
        "\3\0\2\uffff\1\0";
    static final String DFA13_maxS =
        "\3\uffff\2\uffff\1\uffff";
    static final String DFA13_acceptS =
        "\3\uffff\1\2\1\1\1\uffff";
    static final String DFA13_specialS =
        "\1\1\1\3\1\2\2\uffff\1\0}>";
    static final String[] DFA13_transitionS = {
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

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "()* loopback of 297:15: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA13_5 = input.LA(1);

                        s = -1;
                        if ( ((LA13_5 >= '\u0000' && LA13_5 <= '\b')||(LA13_5 >= '\u000B' && LA13_5 <= '\f')||(LA13_5 >= '\u000E' && LA13_5 <= '\u001F')||(LA13_5 >= '#' && LA13_5 <= '%')||LA13_5==','||LA13_5=='.'||(LA13_5 >= '0' && LA13_5 <= '9')||(LA13_5 >= ';' && LA13_5 <= '>')||(LA13_5 >= '@' && LA13_5 <= 'Z')||(LA13_5 >= '_' && LA13_5 <= 'z')||(LA13_5 >= '\u007F' && LA13_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA13_5=='\\') ) {s = 2;}

                        else if ( (LA13_5=='*'||LA13_5=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA13_0 = input.LA(1);

                        s = -1;
                        if ( ((LA13_0 >= '\u0000' && LA13_0 <= '\b')||(LA13_0 >= '\u000B' && LA13_0 <= '\f')||(LA13_0 >= '\u000E' && LA13_0 <= '\u001F')||(LA13_0 >= '#' && LA13_0 <= '%')||LA13_0==','||LA13_0=='.'||(LA13_0 >= '0' && LA13_0 <= '9')||(LA13_0 >= ';' && LA13_0 <= '>')||(LA13_0 >= '@' && LA13_0 <= 'Z')||(LA13_0 >= '_' && LA13_0 <= 'z')||(LA13_0 >= '\u007F' && LA13_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA13_0=='\\') ) {s = 2;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA13_2 = input.LA(1);

                        s = -1;
                        if ( ((LA13_2 >= '\u0000' && LA13_2 <= '\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA13_1 = input.LA(1);

                        s = -1;
                        if ( ((LA13_1 >= '\u0000' && LA13_1 <= '\b')||(LA13_1 >= '\u000B' && LA13_1 <= '\f')||(LA13_1 >= '\u000E' && LA13_1 <= '\u001F')||(LA13_1 >= '#' && LA13_1 <= '%')||LA13_1==','||LA13_1=='.'||(LA13_1 >= '0' && LA13_1 <= '9')||(LA13_1 >= ';' && LA13_1 <= '>')||(LA13_1 >= '@' && LA13_1 <= 'Z')||(LA13_1 >= '_' && LA13_1 <= 'z')||(LA13_1 >= '\u007F' && LA13_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA13_1=='\\') ) {s = 2;}

                        else if ( (LA13_1=='*'||LA13_1=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 13, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA16_eotS =
        "\2\3\3\uffff\1\3";
    static final String DFA16_eofS =
        "\6\uffff";
    static final String DFA16_minS =
        "\3\0\2\uffff\1\0";
    static final String DFA16_maxS =
        "\3\uffff\2\uffff\1\uffff";
    static final String DFA16_acceptS =
        "\3\uffff\1\2\1\1\1\uffff";
    static final String DFA16_specialS =
        "\1\3\1\0\1\2\2\uffff\1\1}>";
    static final String[] DFA16_transitionS = {
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

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "()+ loopback of 298:4: ( ( NORMAL_CHAR | ESC_CHAR )+ ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA16_1 = input.LA(1);

                        s = -1;
                        if ( ((LA16_1 >= '\u0000' && LA16_1 <= '\b')||(LA16_1 >= '\u000B' && LA16_1 <= '\f')||(LA16_1 >= '\u000E' && LA16_1 <= '\u001F')||(LA16_1 >= '#' && LA16_1 <= '%')||LA16_1==','||LA16_1=='.'||(LA16_1 >= '0' && LA16_1 <= '9')||(LA16_1 >= ';' && LA16_1 <= '>')||(LA16_1 >= '@' && LA16_1 <= 'Z')||(LA16_1 >= '_' && LA16_1 <= 'z')||(LA16_1 >= '\u007F' && LA16_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA16_1=='\\') ) {s = 2;}

                        else if ( (LA16_1=='*'||LA16_1=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA16_5 = input.LA(1);

                        s = -1;
                        if ( ((LA16_5 >= '\u0000' && LA16_5 <= '\b')||(LA16_5 >= '\u000B' && LA16_5 <= '\f')||(LA16_5 >= '\u000E' && LA16_5 <= '\u001F')||(LA16_5 >= '#' && LA16_5 <= '%')||LA16_5==','||LA16_5=='.'||(LA16_5 >= '0' && LA16_5 <= '9')||(LA16_5 >= ';' && LA16_5 <= '>')||(LA16_5 >= '@' && LA16_5 <= 'Z')||(LA16_5 >= '_' && LA16_5 <= 'z')||(LA16_5 >= '\u007F' && LA16_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA16_5=='\\') ) {s = 2;}

                        else if ( (LA16_5=='*'||LA16_5=='?') ) {s = 4;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA16_2 = input.LA(1);

                        s = -1;
                        if ( ((LA16_2 >= '\u0000' && LA16_2 <= '\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA16_0 = input.LA(1);

                        s = -1;
                        if ( ((LA16_0 >= '\u0000' && LA16_0 <= '\b')||(LA16_0 >= '\u000B' && LA16_0 <= '\f')||(LA16_0 >= '\u000E' && LA16_0 <= '\u001F')||(LA16_0 >= '#' && LA16_0 <= '%')||LA16_0==','||LA16_0=='.'||(LA16_0 >= '0' && LA16_0 <= '9')||(LA16_0 >= ';' && LA16_0 <= '>')||(LA16_0 >= '@' && LA16_0 <= 'Z')||(LA16_0 >= '_' && LA16_0 <= 'z')||(LA16_0 >= '\u007F' && LA16_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA16_0=='\\') ) {s = 2;}

                        else s = 3;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 16, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA21_eotS =
        "\11\uffff\1\35\1\37\4\uffff\1\40\1\uffff\2\44\1\uffff\1\44\1\uffff"+
        "\1\53\1\uffff\1\44\1\uffff\1\55\1\44\7\uffff\1\61\1\uffff\1\44\1"+
        "\uffff\1\44\1\25\2\44\1\uffff\1\44\1\uffff\1\55\1\44\2\uffff\1\44"+
        "\1\23\1\27\1\44\1\55\1\uffff\1\53";
    static final String DFA21_eofS =
        "\71\uffff";
    static final String DFA21_minS =
        "\1\0\10\uffff\2\0\4\uffff\1\0\1\uffff\2\0\1\uffff\1\0\1\uffff\1"+
        "\0\1\uffff\1\0\1\uffff\3\0\4\uffff\1\0\1\uffff\1\0\1\uffff\6\0\1"+
        "\uffff\1\0\1\uffff\2\0\2\uffff\5\0\1\uffff\1\0";
    static final String DFA21_maxS =
        "\1\uffff\10\uffff\2\uffff\4\uffff\1\uffff\1\uffff\2\uffff\1\uffff"+
        "\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff\3\uffff\4\uffff"+
        "\1\uffff\1\uffff\1\uffff\1\uffff\6\uffff\1\uffff\1\uffff\1\uffff"+
        "\2\uffff\2\uffff\5\uffff\1\uffff\1\uffff";
    static final String DFA21_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff\1\13\1\14\1\15"+
        "\1\16\1\uffff\1\20\2\uffff\1\22\1\uffff\1\23\1\uffff\1\24\1\uffff"+
        "\1\26\3\uffff\1\11\1\31\1\12\1\17\1\uffff\1\33\1\uffff\1\30\6\uffff"+
        "\1\25\1\uffff\1\27\2\uffff\1\32\1\21\5\uffff\1\32\1\uffff";
    static final String DFA21_specialS =
        "\1\20\10\uffff\1\30\1\11\4\uffff\1\22\1\uffff\1\2\1\0\1\uffff\1"+
        "\13\1\uffff\1\34\1\uffff\1\1\1\uffff\1\31\1\12\1\14\4\uffff\1\16"+
        "\1\uffff\1\7\1\uffff\1\23\1\15\1\5\1\32\1\33\1\26\1\uffff\1\24\1"+
        "\uffff\1\3\1\25\2\uffff\1\10\1\17\1\4\1\27\1\6\1\uffff\1\21}>";
    static final String[] DFA21_transitionS = {
            "\11\33\2\31\2\33\1\31\22\33\1\31\1\27\1\17\3\33\1\23\1\20\1"+
            "\2\1\3\1\11\1\7\1\33\1\10\1\33\1\1\12\32\1\6\4\33\1\12\1\33"+
            "\1\22\14\33\1\30\1\24\4\33\1\21\6\33\1\4\1\34\1\5\1\15\2\33"+
            "\1\22\14\33\1\26\1\24\13\33\1\13\1\25\1\14\1\16\uff81\33",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\3\36\6\uffff\1\36"+
            "\1\uffff\1\36\1\uffff\12\36\1\uffff\4\36\1\uffff\33\36\1\uffff"+
            "\1\36\2\uffff\34\36\4\uffff\uff81\36",
            "\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\3\36\6\uffff\1\36"+
            "\1\uffff\1\36\1\uffff\12\36\1\uffff\4\36\1\uffff\33\36\1\uffff"+
            "\1\36\2\uffff\34\36\4\uffff\uff81\36",
            "",
            "",
            "",
            "",
            "\42\41\1\uffff\7\41\1\42\24\41\1\42\uffc0\41",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\17"+
            "\45\1\43\13\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\16"+
            "\45\1\47\14\45\1\uffff\1\46\2\uffff\17\45\1\47\14\45\4\uffff"+
            "\uff81\45",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\22"+
            "\45\1\50\10\45\1\uffff\1\46\2\uffff\23\45\1\50\10\45\4\uffff"+
            "\uff81\45",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\5"+
            "\45\1\52\11\45\1\51\13\45\1\uffff\1\46\2\uffff\6\45\1\52\11"+
            "\45\1\51\13\45\4\uffff\uff81\45",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\5"+
            "\45\1\52\11\45\1\51\13\45\1\uffff\1\46\2\uffff\6\45\1\52\11"+
            "\45\1\51\13\45\4\uffff\uff81\45",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\54\1\uffff\12\56\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "\0\57",
            "",
            "",
            "",
            "",
            "\42\41\1\60\7\41\1\42\24\41\1\42\uffc0\41",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "\0\62",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\4"+
            "\45\1\63\26\45\1\uffff\1\46\2\uffff\5\45\1\63\26\45\4\uffff"+
            "\uff81\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\24"+
            "\45\1\64\6\45\1\uffff\1\46\2\uffff\25\45\1\64\6\45\4\uffff\uff81"+
            "\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\1"+
            "\45\1\65\31\45\1\uffff\1\46\2\uffff\2\45\1\65\31\45\4\uffff"+
            "\uff81\45",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\66\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\54\1\uffff\12\56\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\22"+
            "\45\1\70\10\45\1\uffff\1\46\2\uffff\23\45\1\70\10\45\4\uffff"+
            "\uff81\45",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\66\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\4\uffff\1\36"+
            "\1\uffff\1\45\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\36\33"+
            "\45\1\uffff\1\46\2\uffff\34\45\4\uffff\uff81\45"
    };

    static final short[] DFA21_eot = DFA.unpackEncodedString(DFA21_eotS);
    static final short[] DFA21_eof = DFA.unpackEncodedString(DFA21_eofS);
    static final char[] DFA21_min = DFA.unpackEncodedStringToUnsignedChars(DFA21_minS);
    static final char[] DFA21_max = DFA.unpackEncodedStringToUnsignedChars(DFA21_maxS);
    static final short[] DFA21_accept = DFA.unpackEncodedString(DFA21_acceptS);
    static final short[] DFA21_special = DFA.unpackEncodedString(DFA21_specialS);
    static final short[][] DFA21_transition;

    static {
        int numStates = DFA21_transitionS.length;
        DFA21_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA21_transition[i] = DFA.unpackEncodedString(DFA21_transitionS[i]);
        }
    }

    class DFA21 extends DFA {

        public DFA21(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 21;
            this.eot = DFA21_eot;
            this.eof = DFA21_eof;
            this.min = DFA21_min;
            this.max = DFA21_max;
            this.accept = DFA21_accept;
            this.special = DFA21_special;
            this.transition = DFA21_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__55 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | TO | AND | OR | NOT | NEAR | WS | NUMBER | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA21_18 = input.LA(1);

                        s = -1;
                        if ( (LA21_18=='N'||LA21_18=='n') ) {s = 39;}

                        else if ( ((LA21_18 >= '\u0000' && LA21_18 <= '\b')||(LA21_18 >= '\u000B' && LA21_18 <= '\f')||(LA21_18 >= '\u000E' && LA21_18 <= '\u001F')||(LA21_18 >= '#' && LA21_18 <= '%')||LA21_18==','||LA21_18=='.'||(LA21_18 >= '0' && LA21_18 <= '9')||(LA21_18 >= ';' && LA21_18 <= '>')||(LA21_18 >= '@' && LA21_18 <= 'M')||(LA21_18 >= 'O' && LA21_18 <= 'Z')||(LA21_18 >= '_' && LA21_18 <= 'm')||(LA21_18 >= 'o' && LA21_18 <= 'z')||(LA21_18 >= '\u007F' && LA21_18 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_18=='\\') ) {s = 38;}

                        else if ( (LA21_18=='*'||LA21_18=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA21_24 = input.LA(1);

                        s = -1;
                        if ( (LA21_24=='O'||LA21_24=='o') ) {s = 41;}

                        else if ( (LA21_24=='E'||LA21_24=='e') ) {s = 42;}

                        else if ( ((LA21_24 >= '\u0000' && LA21_24 <= '\b')||(LA21_24 >= '\u000B' && LA21_24 <= '\f')||(LA21_24 >= '\u000E' && LA21_24 <= '\u001F')||(LA21_24 >= '#' && LA21_24 <= '%')||LA21_24==','||LA21_24=='.'||(LA21_24 >= '0' && LA21_24 <= '9')||(LA21_24 >= ';' && LA21_24 <= '>')||(LA21_24 >= '@' && LA21_24 <= 'D')||(LA21_24 >= 'F' && LA21_24 <= 'N')||(LA21_24 >= 'P' && LA21_24 <= 'Z')||(LA21_24 >= '_' && LA21_24 <= 'd')||(LA21_24 >= 'f' && LA21_24 <= 'n')||(LA21_24 >= 'p' && LA21_24 <= 'z')||(LA21_24 >= '\u007F' && LA21_24 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_24=='\\') ) {s = 38;}

                        else if ( (LA21_24=='*'||LA21_24=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA21_17 = input.LA(1);

                        s = -1;
                        if ( (LA21_17=='O') ) {s = 35;}

                        else if ( ((LA21_17 >= '\u0000' && LA21_17 <= '\b')||(LA21_17 >= '\u000B' && LA21_17 <= '\f')||(LA21_17 >= '\u000E' && LA21_17 <= '\u001F')||(LA21_17 >= '#' && LA21_17 <= '%')||LA21_17==','||LA21_17=='.'||(LA21_17 >= '0' && LA21_17 <= '9')||(LA21_17 >= ';' && LA21_17 <= '>')||(LA21_17 >= '@' && LA21_17 <= 'N')||(LA21_17 >= 'P' && LA21_17 <= 'Z')||(LA21_17 >= '_' && LA21_17 <= 'z')||(LA21_17 >= '\u007F' && LA21_17 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_17=='\\') ) {s = 38;}

                        else if ( (LA21_17=='*'||LA21_17=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA21_46 = input.LA(1);

                        s = -1;
                        if ( (LA21_46=='.') ) {s = 44;}

                        else if ( ((LA21_46 >= '0' && LA21_46 <= '9')) ) {s = 46;}

                        else if ( ((LA21_46 >= '\u0000' && LA21_46 <= '\b')||(LA21_46 >= '\u000B' && LA21_46 <= '\f')||(LA21_46 >= '\u000E' && LA21_46 <= '\u001F')||(LA21_46 >= '#' && LA21_46 <= '%')||LA21_46==','||(LA21_46 >= ';' && LA21_46 <= '>')||(LA21_46 >= '@' && LA21_46 <= 'Z')||(LA21_46 >= '_' && LA21_46 <= 'z')||(LA21_46 >= '\u007F' && LA21_46 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_46=='\\') ) {s = 38;}

                        else if ( (LA21_46=='*'||LA21_46=='?') ) {s = 30;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA21_52 = input.LA(1);

                        s = -1;
                        if ( ((LA21_52 >= '\u0000' && LA21_52 <= '\b')||(LA21_52 >= '\u000B' && LA21_52 <= '\f')||(LA21_52 >= '\u000E' && LA21_52 <= '\u001F')||(LA21_52 >= '#' && LA21_52 <= '%')||LA21_52==','||LA21_52=='.'||(LA21_52 >= '0' && LA21_52 <= '9')||(LA21_52 >= ';' && LA21_52 <= '>')||(LA21_52 >= '@' && LA21_52 <= 'Z')||(LA21_52 >= '_' && LA21_52 <= 'z')||(LA21_52 >= '\u007F' && LA21_52 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_52=='\\') ) {s = 38;}

                        else if ( (LA21_52=='*'||LA21_52=='?') ) {s = 30;}

                        else s = 23;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA21_39 = input.LA(1);

                        s = -1;
                        if ( (LA21_39=='D'||LA21_39=='d') ) {s = 51;}

                        else if ( ((LA21_39 >= '\u0000' && LA21_39 <= '\b')||(LA21_39 >= '\u000B' && LA21_39 <= '\f')||(LA21_39 >= '\u000E' && LA21_39 <= '\u001F')||(LA21_39 >= '#' && LA21_39 <= '%')||LA21_39==','||LA21_39=='.'||(LA21_39 >= '0' && LA21_39 <= '9')||(LA21_39 >= ';' && LA21_39 <= '>')||(LA21_39 >= '@' && LA21_39 <= 'C')||(LA21_39 >= 'E' && LA21_39 <= 'Z')||(LA21_39 >= '_' && LA21_39 <= 'c')||(LA21_39 >= 'e' && LA21_39 <= 'z')||(LA21_39 >= '\u007F' && LA21_39 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_39=='\\') ) {s = 38;}

                        else if ( (LA21_39=='*'||LA21_39=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA21_54 = input.LA(1);

                        s = -1;
                        if ( ((LA21_54 >= '0' && LA21_54 <= '9')) ) {s = 54;}

                        else if ( ((LA21_54 >= '\u0000' && LA21_54 <= '\b')||(LA21_54 >= '\u000B' && LA21_54 <= '\f')||(LA21_54 >= '\u000E' && LA21_54 <= '\u001F')||(LA21_54 >= '#' && LA21_54 <= '%')||LA21_54==','||LA21_54=='.'||(LA21_54 >= ';' && LA21_54 <= '>')||(LA21_54 >= '@' && LA21_54 <= 'Z')||(LA21_54 >= '_' && LA21_54 <= 'z')||(LA21_54 >= '\u007F' && LA21_54 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_54=='\\') ) {s = 38;}

                        else if ( (LA21_54=='*'||LA21_54=='?') ) {s = 30;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA21_35 = input.LA(1);

                        s = -1;
                        if ( ((LA21_35 >= '\u0000' && LA21_35 <= '\b')||(LA21_35 >= '\u000B' && LA21_35 <= '\f')||(LA21_35 >= '\u000E' && LA21_35 <= '\u001F')||(LA21_35 >= '#' && LA21_35 <= '%')||LA21_35==','||LA21_35=='.'||(LA21_35 >= '0' && LA21_35 <= '9')||(LA21_35 >= ';' && LA21_35 <= '>')||(LA21_35 >= '@' && LA21_35 <= 'Z')||(LA21_35 >= '_' && LA21_35 <= 'z')||(LA21_35 >= '\u007F' && LA21_35 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_35=='\\') ) {s = 38;}

                        else if ( (LA21_35=='*'||LA21_35=='?') ) {s = 30;}

                        else s = 49;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA21_50 = input.LA(1);

                        s = -1;
                        if ( ((LA21_50 >= '\u0000' && LA21_50 <= '\b')||(LA21_50 >= '\u000B' && LA21_50 <= '\f')||(LA21_50 >= '\u000E' && LA21_50 <= '\u001F')||(LA21_50 >= '#' && LA21_50 <= '%')||LA21_50==','||LA21_50=='.'||(LA21_50 >= '0' && LA21_50 <= '9')||(LA21_50 >= ';' && LA21_50 <= '>')||(LA21_50 >= '@' && LA21_50 <= 'Z')||(LA21_50 >= '_' && LA21_50 <= 'z')||(LA21_50 >= '\u007F' && LA21_50 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_50=='\\') ) {s = 38;}

                        else if ( (LA21_50=='*'||LA21_50=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA21_10 = input.LA(1);

                        s = -1;
                        if ( ((LA21_10 >= '\u0000' && LA21_10 <= '\b')||(LA21_10 >= '\u000B' && LA21_10 <= '\f')||(LA21_10 >= '\u000E' && LA21_10 <= '\u001F')||(LA21_10 >= '#' && LA21_10 <= '%')||LA21_10==','||LA21_10=='.'||(LA21_10 >= '0' && LA21_10 <= '9')||(LA21_10 >= ';' && LA21_10 <= '>')||(LA21_10 >= '@' && LA21_10 <= 'Z')||LA21_10=='\\'||(LA21_10 >= '_' && LA21_10 <= 'z')||(LA21_10 >= '\u007F' && LA21_10 <= '\uFFFF')) ) {s = 30;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA21_27 = input.LA(1);

                        s = -1;
                        if ( ((LA21_27 >= '\u0000' && LA21_27 <= '\b')||(LA21_27 >= '\u000B' && LA21_27 <= '\f')||(LA21_27 >= '\u000E' && LA21_27 <= '\u001F')||(LA21_27 >= '#' && LA21_27 <= '%')||LA21_27==','||LA21_27=='.'||(LA21_27 >= '0' && LA21_27 <= '9')||(LA21_27 >= ';' && LA21_27 <= '>')||(LA21_27 >= '@' && LA21_27 <= 'Z')||(LA21_27 >= '_' && LA21_27 <= 'z')||(LA21_27 >= '\u007F' && LA21_27 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_27=='\\') ) {s = 38;}

                        else if ( (LA21_27=='*'||LA21_27=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA21_20 = input.LA(1);

                        s = -1;
                        if ( (LA21_20=='R'||LA21_20=='r') ) {s = 40;}

                        else if ( ((LA21_20 >= '\u0000' && LA21_20 <= '\b')||(LA21_20 >= '\u000B' && LA21_20 <= '\f')||(LA21_20 >= '\u000E' && LA21_20 <= '\u001F')||(LA21_20 >= '#' && LA21_20 <= '%')||LA21_20==','||LA21_20=='.'||(LA21_20 >= '0' && LA21_20 <= '9')||(LA21_20 >= ';' && LA21_20 <= '>')||(LA21_20 >= '@' && LA21_20 <= 'Q')||(LA21_20 >= 'S' && LA21_20 <= 'Z')||(LA21_20 >= '_' && LA21_20 <= 'q')||(LA21_20 >= 's' && LA21_20 <= 'z')||(LA21_20 >= '\u007F' && LA21_20 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_20=='\\') ) {s = 38;}

                        else if ( (LA21_20=='*'||LA21_20=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA21_28 = input.LA(1);

                        s = -1;
                        if ( ((LA21_28 >= '\u0000' && LA21_28 <= '\uFFFF')) ) {s = 47;}

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA21_38 = input.LA(1);

                        s = -1;
                        if ( ((LA21_38 >= '\u0000' && LA21_38 <= '\uFFFF')) ) {s = 50;}

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA21_33 = input.LA(1);

                        s = -1;
                        if ( (LA21_33=='\"') ) {s = 48;}

                        else if ( ((LA21_33 >= '\u0000' && LA21_33 <= '!')||(LA21_33 >= '#' && LA21_33 <= ')')||(LA21_33 >= '+' && LA21_33 <= '>')||(LA21_33 >= '@' && LA21_33 <= '\uFFFF')) ) {s = 33;}

                        else if ( (LA21_33=='*'||LA21_33=='?') ) {s = 34;}

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA21_51 = input.LA(1);

                        s = -1;
                        if ( ((LA21_51 >= '\u0000' && LA21_51 <= '\b')||(LA21_51 >= '\u000B' && LA21_51 <= '\f')||(LA21_51 >= '\u000E' && LA21_51 <= '\u001F')||(LA21_51 >= '#' && LA21_51 <= '%')||LA21_51==','||LA21_51=='.'||(LA21_51 >= '0' && LA21_51 <= '9')||(LA21_51 >= ';' && LA21_51 <= '>')||(LA21_51 >= '@' && LA21_51 <= 'Z')||(LA21_51 >= '_' && LA21_51 <= 'z')||(LA21_51 >= '\u007F' && LA21_51 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_51=='\\') ) {s = 38;}

                        else if ( (LA21_51=='*'||LA21_51=='?') ) {s = 30;}

                        else s = 19;

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA21_0 = input.LA(1);

                        s = -1;
                        if ( (LA21_0=='/') ) {s = 1;}

                        else if ( (LA21_0=='(') ) {s = 2;}

                        else if ( (LA21_0==')') ) {s = 3;}

                        else if ( (LA21_0=='[') ) {s = 4;}

                        else if ( (LA21_0==']') ) {s = 5;}

                        else if ( (LA21_0==':') ) {s = 6;}

                        else if ( (LA21_0=='+') ) {s = 7;}

                        else if ( (LA21_0=='-') ) {s = 8;}

                        else if ( (LA21_0=='*') ) {s = 9;}

                        else if ( (LA21_0=='?') ) {s = 10;}

                        else if ( (LA21_0=='{') ) {s = 11;}

                        else if ( (LA21_0=='}') ) {s = 12;}

                        else if ( (LA21_0=='^') ) {s = 13;}

                        else if ( (LA21_0=='~') ) {s = 14;}

                        else if ( (LA21_0=='\"') ) {s = 15;}

                        else if ( (LA21_0=='\'') ) {s = 16;}

                        else if ( (LA21_0=='T') ) {s = 17;}

                        else if ( (LA21_0=='A'||LA21_0=='a') ) {s = 18;}

                        else if ( (LA21_0=='&') ) {s = 19;}

                        else if ( (LA21_0=='O'||LA21_0=='o') ) {s = 20;}

                        else if ( (LA21_0=='|') ) {s = 21;}

                        else if ( (LA21_0=='n') ) {s = 22;}

                        else if ( (LA21_0=='!') ) {s = 23;}

                        else if ( (LA21_0=='N') ) {s = 24;}

                        else if ( ((LA21_0 >= '\t' && LA21_0 <= '\n')||LA21_0=='\r'||LA21_0==' ') ) {s = 25;}

                        else if ( ((LA21_0 >= '0' && LA21_0 <= '9')) ) {s = 26;}

                        else if ( ((LA21_0 >= '\u0000' && LA21_0 <= '\b')||(LA21_0 >= '\u000B' && LA21_0 <= '\f')||(LA21_0 >= '\u000E' && LA21_0 <= '\u001F')||(LA21_0 >= '#' && LA21_0 <= '%')||LA21_0==','||LA21_0=='.'||(LA21_0 >= ';' && LA21_0 <= '>')||LA21_0=='@'||(LA21_0 >= 'B' && LA21_0 <= 'M')||(LA21_0 >= 'P' && LA21_0 <= 'S')||(LA21_0 >= 'U' && LA21_0 <= 'Z')||(LA21_0 >= '_' && LA21_0 <= '`')||(LA21_0 >= 'b' && LA21_0 <= 'm')||(LA21_0 >= 'p' && LA21_0 <= 'z')||(LA21_0 >= '\u007F' && LA21_0 <= '\uFFFF')) ) {s = 27;}

                        else if ( (LA21_0=='\\') ) {s = 28;}

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA21_56 = input.LA(1);

                        s = -1;
                        if ( ((LA21_56 >= '\u0000' && LA21_56 <= '\b')||(LA21_56 >= '\u000B' && LA21_56 <= '\f')||(LA21_56 >= '\u000E' && LA21_56 <= '\u001F')||(LA21_56 >= '#' && LA21_56 <= '%')||LA21_56==','||LA21_56=='.'||(LA21_56 >= '0' && LA21_56 <= '9')||(LA21_56 >= ';' && LA21_56 <= '>')||(LA21_56 >= '@' && LA21_56 <= 'Z')||(LA21_56 >= '_' && LA21_56 <= 'z')||(LA21_56 >= '\u007F' && LA21_56 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_56=='\\') ) {s = 38;}

                        else if ( (LA21_56=='*'||LA21_56=='?') ) {s = 30;}

                        else s = 43;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA21_15 = input.LA(1);

                        s = -1;
                        if ( ((LA21_15 >= '\u0000' && LA21_15 <= '!')||(LA21_15 >= '#' && LA21_15 <= ')')||(LA21_15 >= '+' && LA21_15 <= '>')||(LA21_15 >= '@' && LA21_15 <= '\uFFFF')) ) {s = 33;}

                        else if ( (LA21_15=='*'||LA21_15=='?') ) {s = 34;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA21_37 = input.LA(1);

                        s = -1;
                        if ( ((LA21_37 >= '\u0000' && LA21_37 <= '\b')||(LA21_37 >= '\u000B' && LA21_37 <= '\f')||(LA21_37 >= '\u000E' && LA21_37 <= '\u001F')||(LA21_37 >= '#' && LA21_37 <= '%')||LA21_37==','||LA21_37=='.'||(LA21_37 >= '0' && LA21_37 <= '9')||(LA21_37 >= ';' && LA21_37 <= '>')||(LA21_37 >= '@' && LA21_37 <= 'Z')||(LA21_37 >= '_' && LA21_37 <= 'z')||(LA21_37 >= '\u007F' && LA21_37 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_37=='\\') ) {s = 38;}

                        else if ( (LA21_37=='*'||LA21_37=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA21_44 = input.LA(1);

                        s = -1;
                        if ( ((LA21_44 >= '0' && LA21_44 <= '9')) ) {s = 54;}

                        else if ( ((LA21_44 >= '\u0000' && LA21_44 <= '\b')||(LA21_44 >= '\u000B' && LA21_44 <= '\f')||(LA21_44 >= '\u000E' && LA21_44 <= '\u001F')||(LA21_44 >= '#' && LA21_44 <= '%')||LA21_44==','||LA21_44=='.'||(LA21_44 >= ';' && LA21_44 <= '>')||(LA21_44 >= '@' && LA21_44 <= 'Z')||(LA21_44 >= '_' && LA21_44 <= 'z')||(LA21_44 >= '\u007F' && LA21_44 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_44=='\\') ) {s = 38;}

                        else if ( (LA21_44=='*'||LA21_44=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA21_47 = input.LA(1);

                        s = -1;
                        if ( ((LA21_47 >= '\u0000' && LA21_47 <= '\b')||(LA21_47 >= '\u000B' && LA21_47 <= '\f')||(LA21_47 >= '\u000E' && LA21_47 <= '\u001F')||(LA21_47 >= '#' && LA21_47 <= '%')||LA21_47==','||LA21_47=='.'||(LA21_47 >= '0' && LA21_47 <= '9')||(LA21_47 >= ';' && LA21_47 <= '>')||(LA21_47 >= '@' && LA21_47 <= 'Z')||(LA21_47 >= '_' && LA21_47 <= 'z')||(LA21_47 >= '\u007F' && LA21_47 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_47=='\\') ) {s = 38;}

                        else if ( (LA21_47=='*'||LA21_47=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA21_42 = input.LA(1);

                        s = -1;
                        if ( (LA21_42=='A'||LA21_42=='a') ) {s = 53;}

                        else if ( ((LA21_42 >= '\u0000' && LA21_42 <= '\b')||(LA21_42 >= '\u000B' && LA21_42 <= '\f')||(LA21_42 >= '\u000E' && LA21_42 <= '\u001F')||(LA21_42 >= '#' && LA21_42 <= '%')||LA21_42==','||LA21_42=='.'||(LA21_42 >= '0' && LA21_42 <= '9')||(LA21_42 >= ';' && LA21_42 <= '>')||LA21_42=='@'||(LA21_42 >= 'B' && LA21_42 <= 'Z')||(LA21_42 >= '_' && LA21_42 <= '`')||(LA21_42 >= 'b' && LA21_42 <= 'z')||(LA21_42 >= '\u007F' && LA21_42 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_42=='\\') ) {s = 38;}

                        else if ( (LA21_42=='*'||LA21_42=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA21_53 = input.LA(1);

                        s = -1;
                        if ( (LA21_53=='R'||LA21_53=='r') ) {s = 56;}

                        else if ( ((LA21_53 >= '\u0000' && LA21_53 <= '\b')||(LA21_53 >= '\u000B' && LA21_53 <= '\f')||(LA21_53 >= '\u000E' && LA21_53 <= '\u001F')||(LA21_53 >= '#' && LA21_53 <= '%')||LA21_53==','||LA21_53=='.'||(LA21_53 >= '0' && LA21_53 <= '9')||(LA21_53 >= ';' && LA21_53 <= '>')||(LA21_53 >= '@' && LA21_53 <= 'Q')||(LA21_53 >= 'S' && LA21_53 <= 'Z')||(LA21_53 >= '_' && LA21_53 <= 'q')||(LA21_53 >= 's' && LA21_53 <= 'z')||(LA21_53 >= '\u007F' && LA21_53 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_53=='\\') ) {s = 38;}

                        else if ( (LA21_53=='*'||LA21_53=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA21_9 = input.LA(1);

                        s = -1;
                        if ( ((LA21_9 >= '\u0000' && LA21_9 <= '\b')||(LA21_9 >= '\u000B' && LA21_9 <= '\f')||(LA21_9 >= '\u000E' && LA21_9 <= '\u001F')||(LA21_9 >= '#' && LA21_9 <= '%')||LA21_9==','||LA21_9=='.'||(LA21_9 >= '0' && LA21_9 <= '9')||(LA21_9 >= ';' && LA21_9 <= '>')||(LA21_9 >= '@' && LA21_9 <= 'Z')||LA21_9=='\\'||(LA21_9 >= '_' && LA21_9 <= 'z')||(LA21_9 >= '\u007F' && LA21_9 <= '\uFFFF')) ) {s = 30;}

                        else s = 29;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA21_26 = input.LA(1);

                        s = -1;
                        if ( (LA21_26=='.') ) {s = 44;}

                        else if ( ((LA21_26 >= '0' && LA21_26 <= '9')) ) {s = 46;}

                        else if ( ((LA21_26 >= '\u0000' && LA21_26 <= '\b')||(LA21_26 >= '\u000B' && LA21_26 <= '\f')||(LA21_26 >= '\u000E' && LA21_26 <= '\u001F')||(LA21_26 >= '#' && LA21_26 <= '%')||LA21_26==','||(LA21_26 >= ';' && LA21_26 <= '>')||(LA21_26 >= '@' && LA21_26 <= 'Z')||(LA21_26 >= '_' && LA21_26 <= 'z')||(LA21_26 >= '\u007F' && LA21_26 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_26=='\\') ) {s = 38;}

                        else if ( (LA21_26=='*'||LA21_26=='?') ) {s = 30;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA21_40 = input.LA(1);

                        s = -1;
                        if ( ((LA21_40 >= '\u0000' && LA21_40 <= '\b')||(LA21_40 >= '\u000B' && LA21_40 <= '\f')||(LA21_40 >= '\u000E' && LA21_40 <= '\u001F')||(LA21_40 >= '#' && LA21_40 <= '%')||LA21_40==','||LA21_40=='.'||(LA21_40 >= '0' && LA21_40 <= '9')||(LA21_40 >= ';' && LA21_40 <= '>')||(LA21_40 >= '@' && LA21_40 <= 'Z')||(LA21_40 >= '_' && LA21_40 <= 'z')||(LA21_40 >= '\u007F' && LA21_40 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_40=='\\') ) {s = 38;}

                        else if ( (LA21_40=='*'||LA21_40=='?') ) {s = 30;}

                        else s = 21;

                        if ( s>=0 ) return s;
                        break;

                    case 27 : 
                        int LA21_41 = input.LA(1);

                        s = -1;
                        if ( (LA21_41=='T'||LA21_41=='t') ) {s = 52;}

                        else if ( ((LA21_41 >= '\u0000' && LA21_41 <= '\b')||(LA21_41 >= '\u000B' && LA21_41 <= '\f')||(LA21_41 >= '\u000E' && LA21_41 <= '\u001F')||(LA21_41 >= '#' && LA21_41 <= '%')||LA21_41==','||LA21_41=='.'||(LA21_41 >= '0' && LA21_41 <= '9')||(LA21_41 >= ';' && LA21_41 <= '>')||(LA21_41 >= '@' && LA21_41 <= 'S')||(LA21_41 >= 'U' && LA21_41 <= 'Z')||(LA21_41 >= '_' && LA21_41 <= 's')||(LA21_41 >= 'u' && LA21_41 <= 'z')||(LA21_41 >= '\u007F' && LA21_41 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_41=='\\') ) {s = 38;}

                        else if ( (LA21_41=='*'||LA21_41=='?') ) {s = 30;}

                        else s = 36;

                        if ( s>=0 ) return s;
                        break;

                    case 28 : 
                        int LA21_22 = input.LA(1);

                        s = -1;
                        if ( (LA21_22=='O'||LA21_22=='o') ) {s = 41;}

                        else if ( (LA21_22=='E'||LA21_22=='e') ) {s = 42;}

                        else if ( ((LA21_22 >= '\u0000' && LA21_22 <= '\b')||(LA21_22 >= '\u000B' && LA21_22 <= '\f')||(LA21_22 >= '\u000E' && LA21_22 <= '\u001F')||(LA21_22 >= '#' && LA21_22 <= '%')||LA21_22==','||LA21_22=='.'||(LA21_22 >= '0' && LA21_22 <= '9')||(LA21_22 >= ';' && LA21_22 <= '>')||(LA21_22 >= '@' && LA21_22 <= 'D')||(LA21_22 >= 'F' && LA21_22 <= 'N')||(LA21_22 >= 'P' && LA21_22 <= 'Z')||(LA21_22 >= '_' && LA21_22 <= 'd')||(LA21_22 >= 'f' && LA21_22 <= 'n')||(LA21_22 >= 'p' && LA21_22 <= 'z')||(LA21_22 >= '\u007F' && LA21_22 <= '\uFFFF')) ) {s = 37;}

                        else if ( (LA21_22=='\\') ) {s = 38;}

                        else if ( (LA21_22=='*'||LA21_22=='?') ) {s = 30;}

                        else s = 43;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 21, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}