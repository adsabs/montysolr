// $ANTLR 3.4 StandardLuceneGrammar.g 2011-10-26 13:44:38

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
    public static final int MULTIVALUE=23;
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
            // StandardLuceneGrammar.g:216:9: ( '(' )
            // StandardLuceneGrammar.g:216:11: '('
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
            // StandardLuceneGrammar.g:218:9: ( ')' )
            // StandardLuceneGrammar.g:218:11: ')'
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
            // StandardLuceneGrammar.g:220:9: ( '[' )
            // StandardLuceneGrammar.g:220:11: '['
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
            // StandardLuceneGrammar.g:222:9: ( ']' )
            // StandardLuceneGrammar.g:222:11: ']'
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
            // StandardLuceneGrammar.g:224:9: ( ':' )
            // StandardLuceneGrammar.g:224:11: ':'
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
            // StandardLuceneGrammar.g:226:7: ( '+' )
            // StandardLuceneGrammar.g:226:9: '+'
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
            // StandardLuceneGrammar.g:228:7: ( '-' )
            // StandardLuceneGrammar.g:228:9: '-'
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
            // StandardLuceneGrammar.g:230:7: ( '*' )
            // StandardLuceneGrammar.g:230:9: '*'
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
            // StandardLuceneGrammar.g:232:8: ( '?' )
            // StandardLuceneGrammar.g:232:10: '?'
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
            // StandardLuceneGrammar.g:234:16: ( '|' )
            // StandardLuceneGrammar.g:234:18: '|'
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
            // StandardLuceneGrammar.g:236:16: ( '&' )
            // StandardLuceneGrammar.g:236:18: '&'
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
            // StandardLuceneGrammar.g:238:9: ( '{' )
            // StandardLuceneGrammar.g:238:11: '{'
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
            // StandardLuceneGrammar.g:240:9: ( '}' )
            // StandardLuceneGrammar.g:240:11: '}'
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
            // StandardLuceneGrammar.g:242:7: ( '^' )
            // StandardLuceneGrammar.g:242:9: '^'
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
            // StandardLuceneGrammar.g:244:7: ( '~' )
            // StandardLuceneGrammar.g:244:9: '~'
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
            // StandardLuceneGrammar.g:247:2: ( '\\\"' )
            // StandardLuceneGrammar.g:247:4: '\\\"'
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
            // StandardLuceneGrammar.g:250:2: ( '\\'' )
            // StandardLuceneGrammar.g:250:4: '\\''
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
            // StandardLuceneGrammar.g:253:9: ( '\\\\' . )
            // StandardLuceneGrammar.g:253:12: '\\\\' .
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
            // StandardLuceneGrammar.g:255:4: ( 'TO' )
            // StandardLuceneGrammar.g:255:6: 'TO'
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
            // StandardLuceneGrammar.g:258:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) ) )
            // StandardLuceneGrammar.g:258:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
            {
            // StandardLuceneGrammar.g:258:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) | ( AMPER ( AMPER )? ) )
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
                    // StandardLuceneGrammar.g:258:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
                    // StandardLuceneGrammar.g:258:48: ( AMPER ( AMPER )? )
                    {
                    // StandardLuceneGrammar.g:258:48: ( AMPER ( AMPER )? )
                    // StandardLuceneGrammar.g:258:49: AMPER ( AMPER )?
                    {
                    mAMPER(); 


                    // StandardLuceneGrammar.g:258:55: ( AMPER )?
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
            // StandardLuceneGrammar.g:259:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) ) )
            // StandardLuceneGrammar.g:259:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
            {
            // StandardLuceneGrammar.g:259:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) | ( VBAR ( VBAR )? ) )
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
                    // StandardLuceneGrammar.g:259:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
                    // StandardLuceneGrammar.g:259:34: ( VBAR ( VBAR )? )
                    {
                    // StandardLuceneGrammar.g:259:34: ( VBAR ( VBAR )? )
                    // StandardLuceneGrammar.g:259:35: VBAR ( VBAR )?
                    {
                    mVBAR(); 


                    // StandardLuceneGrammar.g:259:40: ( VBAR )?
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
            // StandardLuceneGrammar.g:260:7: ( ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) | '!' ) )
            // StandardLuceneGrammar.g:260:9: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) | '!' )
            {
            // StandardLuceneGrammar.g:260:9: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) | '!' )
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
                    // StandardLuceneGrammar.g:260:10: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
                    // StandardLuceneGrammar.g:260:48: '!'
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
            // StandardLuceneGrammar.g:261:7: ( ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' ) )
            // StandardLuceneGrammar.g:261:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            {
            // StandardLuceneGrammar.g:261:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
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
                    // StandardLuceneGrammar.g:261:10: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
                    // StandardLuceneGrammar.g:261:60: 'n'
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
            // StandardLuceneGrammar.g:264:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // StandardLuceneGrammar.g:264:9: ( ' ' | '\\t' | '\\r' | '\\n' )
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
            // StandardLuceneGrammar.g:273:13: ( '0' .. '9' )
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
            // StandardLuceneGrammar.g:275:23: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' ) )
            // StandardLuceneGrammar.g:275:25: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\\\' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '*' | '|' | '&' | '?' | '\\\\\\\"' | '/' )
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
            // StandardLuceneGrammar.g:284:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // StandardLuceneGrammar.g:285:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // StandardLuceneGrammar.g:285:2: ( INT )+
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


            // StandardLuceneGrammar.g:285:7: ( '.' ( INT )+ )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='.') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // StandardLuceneGrammar.g:285:8: '.' ( INT )+
                    {
                    match('.'); 

                    // StandardLuceneGrammar.g:285:12: ( INT )+
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
            // StandardLuceneGrammar.g:290:2: ( ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )* )
            // StandardLuceneGrammar.g:291:2: ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )*
            {
            // StandardLuceneGrammar.g:291:2: ( NORMAL_CHAR | ESC_CHAR )
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
                    // StandardLuceneGrammar.g:291:4: NORMAL_CHAR
                    {
                    mNORMAL_CHAR(); 


                    }
                    break;
                case 2 :
                    // StandardLuceneGrammar.g:291:18: ESC_CHAR
                    {
                    mESC_CHAR(); 


                    }
                    break;

            }


            // StandardLuceneGrammar.g:291:28: ( NORMAL_CHAR | ESC_CHAR )*
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
            	    // StandardLuceneGrammar.g:291:30: NORMAL_CHAR
            	    {
            	    mNORMAL_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // StandardLuceneGrammar.g:291:44: ESC_CHAR
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
            // StandardLuceneGrammar.g:295:15: ( ( NORMAL_CHAR | ( STAR | QMARK ) )+ )
            // StandardLuceneGrammar.g:296:2: ( NORMAL_CHAR | ( STAR | QMARK ) )+
            {
            // StandardLuceneGrammar.g:296:2: ( NORMAL_CHAR | ( STAR | QMARK ) )+
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
            	    // StandardLuceneGrammar.g:296:3: NORMAL_CHAR
            	    {
            	    mNORMAL_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // StandardLuceneGrammar.g:296:17: ( STAR | QMARK )
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
            // StandardLuceneGrammar.g:302:2: ( DQUOTE (~ ( '\\\"' | '?' | '*' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:303:2: DQUOTE (~ ( '\\\"' | '?' | '*' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:303:9: (~ ( '\\\"' | '?' | '*' ) )+
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
            // StandardLuceneGrammar.g:306:17: ( DQUOTE (~ ( '\\\"' ) )+ DQUOTE )
            // StandardLuceneGrammar.g:307:2: DQUOTE (~ ( '\\\"' ) )+ DQUOTE
            {
            mDQUOTE(); 


            // StandardLuceneGrammar.g:307:9: (~ ( '\\\"' ) )+
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
        // StandardLuceneGrammar.g:1:8: ( T__55 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | ESC_CHAR | TO | AND | OR | NOT | NEAR | WS | NUMBER | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING )
        int alt15=28;
        alt15 = dfa15.predict(input);
        switch (alt15) {
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
                // StandardLuceneGrammar.g:1:112: ESC_CHAR
                {
                mESC_CHAR(); 


                }
                break;
            case 18 :
                // StandardLuceneGrammar.g:1:121: TO
                {
                mTO(); 


                }
                break;
            case 19 :
                // StandardLuceneGrammar.g:1:124: AND
                {
                mAND(); 


                }
                break;
            case 20 :
                // StandardLuceneGrammar.g:1:128: OR
                {
                mOR(); 


                }
                break;
            case 21 :
                // StandardLuceneGrammar.g:1:131: NOT
                {
                mNOT(); 


                }
                break;
            case 22 :
                // StandardLuceneGrammar.g:1:135: NEAR
                {
                mNEAR(); 


                }
                break;
            case 23 :
                // StandardLuceneGrammar.g:1:140: WS
                {
                mWS(); 


                }
                break;
            case 24 :
                // StandardLuceneGrammar.g:1:143: NUMBER
                {
                mNUMBER(); 


                }
                break;
            case 25 :
                // StandardLuceneGrammar.g:1:150: TERM_NORMAL
                {
                mTERM_NORMAL(); 


                }
                break;
            case 26 :
                // StandardLuceneGrammar.g:1:162: TERM_TRUNCATED
                {
                mTERM_TRUNCATED(); 


                }
                break;
            case 27 :
                // StandardLuceneGrammar.g:1:177: PHRASE
                {
                mPHRASE(); 


                }
                break;
            case 28 :
                // StandardLuceneGrammar.g:1:184: PHRASE_ANYTHING
                {
                mPHRASE_ANYTHING(); 


                }
                break;

        }

    }


    protected DFA15 dfa15 = new DFA15(this);
    static final String DFA15_eotS =
        "\11\uffff\1\35\1\37\4\uffff\1\40\2\uffff\2\45\1\uffff\1\45\1\uffff"+
        "\1\53\1\uffff\1\45\1\uffff\1\55\1\45\6\uffff\1\60\1\61\1\uffff\2"+
        "\45\1\26\2\45\1\uffff\1\45\1\uffff\1\55\3\uffff\1\24\1\30\1\45\1"+
        "\55\1\uffff\1\53";
    static final String DFA15_eofS =
        "\70\uffff";
    static final String DFA15_minS =
        "\1\0\10\uffff\2\0\4\uffff\1\0\1\uffff\3\0\1\uffff\1\0\1\uffff\1"+
        "\0\1\uffff\1\0\1\uffff\2\0\4\uffff\1\0\1\uffff\2\0\1\uffff\5\0\1"+
        "\uffff\1\0\1\uffff\1\0\3\uffff\4\0\1\uffff\1\0";
    static final String DFA15_maxS =
        "\1\uffff\10\uffff\2\uffff\4\uffff\1\uffff\1\uffff\3\uffff\1\uffff"+
        "\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff\2\uffff\4\uffff"+
        "\1\uffff\1\uffff\2\uffff\1\uffff\5\uffff\1\uffff\1\uffff\1\uffff"+
        "\1\uffff\3\uffff\4\uffff\1\uffff\1\uffff";
    static final String DFA15_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff\1\13\1\14\1\15"+
        "\1\16\1\uffff\1\20\3\uffff\1\23\1\uffff\1\24\1\uffff\1\25\1\uffff"+
        "\1\27\2\uffff\1\11\1\32\1\12\1\17\1\uffff\1\34\2\uffff\1\31\5\uffff"+
        "\1\26\1\uffff\1\30\1\uffff\1\33\1\21\1\22\4\uffff\1\33\1\uffff";
    static final String DFA15_specialS =
        "\1\17\10\uffff\1\7\1\13\4\uffff\1\30\1\uffff\1\2\1\11\1\16\1\uffff"+
        "\1\21\1\uffff\1\10\1\uffff\1\6\1\uffff\1\20\1\1\4\uffff\1\15\1\uffff"+
        "\1\25\1\26\1\uffff\1\3\1\4\1\12\1\22\1\23\1\uffff\1\31\1\uffff\1"+
        "\14\3\uffff\1\0\1\32\1\24\1\27\1\uffff\1\5}>";
    static final String[] DFA15_transitionS = {
            "\11\34\2\32\2\34\1\32\22\34\1\32\1\30\1\17\3\34\1\24\1\20\1"+
            "\2\1\3\1\11\1\7\1\34\1\10\1\34\1\1\12\33\1\6\4\34\1\12\1\34"+
            "\1\23\14\34\1\31\1\25\4\34\1\22\6\34\1\4\1\21\1\5\1\15\2\34"+
            "\1\23\14\34\1\27\1\25\13\34\1\13\1\26\1\14\1\16\uff81\34",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\3\36\4\uffff\1\36"+
            "\1\uffff\1\36\1\uffff\1\36\1\uffff\12\36\1\uffff\40\36\4\uffff"+
            "\34\36\4\uffff\uff81\36",
            "\11\36\2\uffff\2\36\1\uffff\22\36\3\uffff\3\36\4\uffff\1\36"+
            "\1\uffff\1\36\1\uffff\1\36\1\uffff\12\36\1\uffff\40\36\4\uffff"+
            "\34\36\4\uffff\uff81\36",
            "",
            "",
            "",
            "",
            "\42\41\1\uffff\7\41\1\42\24\41\1\42\uffc0\41",
            "",
            "\0\43",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\17"+
            "\46\1\44\13\46\4\uffff\34\46\4\uffff\uff81\46",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\16"+
            "\46\1\47\14\46\4\uffff\17\46\1\47\14\46\4\uffff\uff81\46",
            "",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\22"+
            "\46\1\50\10\46\4\uffff\23\46\1\50\10\46\4\uffff\uff81\46",
            "",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\5"+
            "\46\1\52\11\46\1\51\13\46\1\uffff\1\45\2\uffff\6\46\1\52\11"+
            "\46\1\51\13\46\4\uffff\uff81\46",
            "",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\5"+
            "\46\1\52\11\46\1\51\13\46\4\uffff\6\46\1\52\11\46\1\51\13\46"+
            "\4\uffff\uff81\46",
            "",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\54\1\uffff\12\56\1\uffff\4\46\1\36\33"+
            "\46\1\uffff\1\45\2\uffff\34\46\4\uffff\uff81\46",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\33"+
            "\46\4\uffff\34\46\4\uffff\uff81\46",
            "",
            "",
            "",
            "",
            "\42\41\1\57\7\41\1\42\24\41\1\42\uffc0\41",
            "",
            "\11\45\2\uffff\2\45\1\uffff\22\45\3\uffff\3\45\6\uffff\1\45"+
            "\1\uffff\1\45\1\uffff\12\45\1\uffff\4\45\1\uffff\33\45\1\uffff"+
            "\1\45\2\uffff\34\45\4\uffff\uff81\45",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\33"+
            "\46\1\uffff\1\45\2\uffff\34\46\4\uffff\uff81\46",
            "",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\33"+
            "\46\4\uffff\34\46\4\uffff\uff81\46",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\4"+
            "\46\1\62\26\46\4\uffff\5\46\1\62\26\46\4\uffff\uff81\46",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\33"+
            "\46\1\uffff\1\45\2\uffff\34\46\4\uffff\uff81\46",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\24"+
            "\46\1\63\6\46\4\uffff\25\46\1\63\6\46\4\uffff\uff81\46",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\1"+
            "\46\1\64\31\46\4\uffff\2\46\1\64\31\46\4\uffff\uff81\46",
            "",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\65\1\uffff\4\46\1\36\33"+
            "\46\4\uffff\34\46\4\uffff\uff81\46",
            "",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\54\1\uffff\12\56\1\uffff\4\46\1\36\33"+
            "\46\1\uffff\1\45\2\uffff\34\46\4\uffff\uff81\46",
            "",
            "",
            "",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\33"+
            "\46\1\uffff\1\45\2\uffff\34\46\4\uffff\uff81\46",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\33"+
            "\46\1\uffff\1\45\2\uffff\34\46\4\uffff\uff81\46",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\22"+
            "\46\1\67\10\46\4\uffff\23\46\1\67\10\46\4\uffff\uff81\46",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\65\1\uffff\4\46\1\36\33"+
            "\46\1\uffff\1\45\2\uffff\34\46\4\uffff\uff81\46",
            "",
            "\11\46\2\uffff\2\46\1\uffff\22\46\3\uffff\3\46\4\uffff\1\36"+
            "\1\uffff\1\46\1\uffff\1\46\1\uffff\12\46\1\uffff\4\46\1\36\33"+
            "\46\1\uffff\1\45\2\uffff\34\46\4\uffff\uff81\46"
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
            return "1:1: Tokens : ( T__55 | LPAREN | RPAREN | LBRACK | RBRACK | COLON | PLUS | MINUS | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | ESC_CHAR | TO | AND | OR | NOT | NEAR | WS | NUMBER | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA15_50 = input.LA(1);

                        s = -1;
                        if ( ((LA15_50 >= '\u0000' && LA15_50 <= '\b')||(LA15_50 >= '\u000B' && LA15_50 <= '\f')||(LA15_50 >= '\u000E' && LA15_50 <= '\u001F')||(LA15_50 >= '#' && LA15_50 <= '%')||LA15_50==','||LA15_50=='.'||(LA15_50 >= '0' && LA15_50 <= '9')||(LA15_50 >= ';' && LA15_50 <= '>')||(LA15_50 >= '@' && LA15_50 <= 'Z')||(LA15_50 >= '_' && LA15_50 <= 'z')||(LA15_50 >= '\u007F' && LA15_50 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_50=='\\') ) {s = 37;}

                        else if ( (LA15_50=='*'||LA15_50=='?') ) {s = 30;}

                        else s = 20;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA15_28 = input.LA(1);

                        s = -1;
                        if ( ((LA15_28 >= '\u0000' && LA15_28 <= '\b')||(LA15_28 >= '\u000B' && LA15_28 <= '\f')||(LA15_28 >= '\u000E' && LA15_28 <= '\u001F')||(LA15_28 >= '#' && LA15_28 <= '%')||LA15_28==','||LA15_28=='.'||(LA15_28 >= '0' && LA15_28 <= '9')||(LA15_28 >= ';' && LA15_28 <= '>')||(LA15_28 >= '@' && LA15_28 <= 'Z')||(LA15_28 >= '_' && LA15_28 <= 'z')||(LA15_28 >= '\u007F' && LA15_28 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_28=='*'||LA15_28=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA15_17 = input.LA(1);

                        s = -1;
                        if ( ((LA15_17 >= '\u0000' && LA15_17 <= '\uFFFF')) ) {s = 35;}

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA15_38 = input.LA(1);

                        s = -1;
                        if ( ((LA15_38 >= '\u0000' && LA15_38 <= '\b')||(LA15_38 >= '\u000B' && LA15_38 <= '\f')||(LA15_38 >= '\u000E' && LA15_38 <= '\u001F')||(LA15_38 >= '#' && LA15_38 <= '%')||LA15_38==','||LA15_38=='.'||(LA15_38 >= '0' && LA15_38 <= '9')||(LA15_38 >= ';' && LA15_38 <= '>')||(LA15_38 >= '@' && LA15_38 <= 'Z')||(LA15_38 >= '_' && LA15_38 <= 'z')||(LA15_38 >= '\u007F' && LA15_38 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_38=='*'||LA15_38=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA15_39 = input.LA(1);

                        s = -1;
                        if ( (LA15_39=='D'||LA15_39=='d') ) {s = 50;}

                        else if ( ((LA15_39 >= '\u0000' && LA15_39 <= '\b')||(LA15_39 >= '\u000B' && LA15_39 <= '\f')||(LA15_39 >= '\u000E' && LA15_39 <= '\u001F')||(LA15_39 >= '#' && LA15_39 <= '%')||LA15_39==','||LA15_39=='.'||(LA15_39 >= '0' && LA15_39 <= '9')||(LA15_39 >= ';' && LA15_39 <= '>')||(LA15_39 >= '@' && LA15_39 <= 'C')||(LA15_39 >= 'E' && LA15_39 <= 'Z')||(LA15_39 >= '_' && LA15_39 <= 'c')||(LA15_39 >= 'e' && LA15_39 <= 'z')||(LA15_39 >= '\u007F' && LA15_39 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_39=='*'||LA15_39=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA15_55 = input.LA(1);

                        s = -1;
                        if ( ((LA15_55 >= '\u0000' && LA15_55 <= '\b')||(LA15_55 >= '\u000B' && LA15_55 <= '\f')||(LA15_55 >= '\u000E' && LA15_55 <= '\u001F')||(LA15_55 >= '#' && LA15_55 <= '%')||LA15_55==','||LA15_55=='.'||(LA15_55 >= '0' && LA15_55 <= '9')||(LA15_55 >= ';' && LA15_55 <= '>')||(LA15_55 >= '@' && LA15_55 <= 'Z')||(LA15_55 >= '_' && LA15_55 <= 'z')||(LA15_55 >= '\u007F' && LA15_55 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_55=='\\') ) {s = 37;}

                        else if ( (LA15_55=='*'||LA15_55=='?') ) {s = 30;}

                        else s = 43;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA15_25 = input.LA(1);

                        s = -1;
                        if ( (LA15_25=='O'||LA15_25=='o') ) {s = 41;}

                        else if ( (LA15_25=='E'||LA15_25=='e') ) {s = 42;}

                        else if ( ((LA15_25 >= '\u0000' && LA15_25 <= '\b')||(LA15_25 >= '\u000B' && LA15_25 <= '\f')||(LA15_25 >= '\u000E' && LA15_25 <= '\u001F')||(LA15_25 >= '#' && LA15_25 <= '%')||LA15_25==','||LA15_25=='.'||(LA15_25 >= '0' && LA15_25 <= '9')||(LA15_25 >= ';' && LA15_25 <= '>')||(LA15_25 >= '@' && LA15_25 <= 'D')||(LA15_25 >= 'F' && LA15_25 <= 'N')||(LA15_25 >= 'P' && LA15_25 <= 'Z')||(LA15_25 >= '_' && LA15_25 <= 'd')||(LA15_25 >= 'f' && LA15_25 <= 'n')||(LA15_25 >= 'p' && LA15_25 <= 'z')||(LA15_25 >= '\u007F' && LA15_25 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_25=='*'||LA15_25=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA15_9 = input.LA(1);

                        s = -1;
                        if ( ((LA15_9 >= '\u0000' && LA15_9 <= '\b')||(LA15_9 >= '\u000B' && LA15_9 <= '\f')||(LA15_9 >= '\u000E' && LA15_9 <= '\u001F')||(LA15_9 >= '#' && LA15_9 <= '%')||LA15_9=='*'||LA15_9==','||LA15_9=='.'||(LA15_9 >= '0' && LA15_9 <= '9')||(LA15_9 >= ';' && LA15_9 <= 'Z')||(LA15_9 >= '_' && LA15_9 <= 'z')||(LA15_9 >= '\u007F' && LA15_9 <= '\uFFFF')) ) {s = 30;}

                        else s = 29;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA15_23 = input.LA(1);

                        s = -1;
                        if ( (LA15_23=='O'||LA15_23=='o') ) {s = 41;}

                        else if ( (LA15_23=='E'||LA15_23=='e') ) {s = 42;}

                        else if ( ((LA15_23 >= '\u0000' && LA15_23 <= '\b')||(LA15_23 >= '\u000B' && LA15_23 <= '\f')||(LA15_23 >= '\u000E' && LA15_23 <= '\u001F')||(LA15_23 >= '#' && LA15_23 <= '%')||LA15_23==','||LA15_23=='.'||(LA15_23 >= '0' && LA15_23 <= '9')||(LA15_23 >= ';' && LA15_23 <= '>')||(LA15_23 >= '@' && LA15_23 <= 'D')||(LA15_23 >= 'F' && LA15_23 <= 'N')||(LA15_23 >= 'P' && LA15_23 <= 'Z')||(LA15_23 >= '_' && LA15_23 <= 'd')||(LA15_23 >= 'f' && LA15_23 <= 'n')||(LA15_23 >= 'p' && LA15_23 <= 'z')||(LA15_23 >= '\u007F' && LA15_23 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_23=='\\') ) {s = 37;}

                        else if ( (LA15_23=='*'||LA15_23=='?') ) {s = 30;}

                        else s = 43;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA15_18 = input.LA(1);

                        s = -1;
                        if ( (LA15_18=='O') ) {s = 36;}

                        else if ( ((LA15_18 >= '\u0000' && LA15_18 <= '\b')||(LA15_18 >= '\u000B' && LA15_18 <= '\f')||(LA15_18 >= '\u000E' && LA15_18 <= '\u001F')||(LA15_18 >= '#' && LA15_18 <= '%')||LA15_18==','||LA15_18=='.'||(LA15_18 >= '0' && LA15_18 <= '9')||(LA15_18 >= ';' && LA15_18 <= '>')||(LA15_18 >= '@' && LA15_18 <= 'N')||(LA15_18 >= 'P' && LA15_18 <= 'Z')||(LA15_18 >= '_' && LA15_18 <= 'z')||(LA15_18 >= '\u007F' && LA15_18 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_18=='*'||LA15_18=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA15_40 = input.LA(1);

                        s = -1;
                        if ( ((LA15_40 >= '\u0000' && LA15_40 <= '\b')||(LA15_40 >= '\u000B' && LA15_40 <= '\f')||(LA15_40 >= '\u000E' && LA15_40 <= '\u001F')||(LA15_40 >= '#' && LA15_40 <= '%')||LA15_40==','||LA15_40=='.'||(LA15_40 >= '0' && LA15_40 <= '9')||(LA15_40 >= ';' && LA15_40 <= '>')||(LA15_40 >= '@' && LA15_40 <= 'Z')||(LA15_40 >= '_' && LA15_40 <= 'z')||(LA15_40 >= '\u007F' && LA15_40 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_40=='\\') ) {s = 37;}

                        else if ( (LA15_40=='*'||LA15_40=='?') ) {s = 30;}

                        else s = 22;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA15_10 = input.LA(1);

                        s = -1;
                        if ( ((LA15_10 >= '\u0000' && LA15_10 <= '\b')||(LA15_10 >= '\u000B' && LA15_10 <= '\f')||(LA15_10 >= '\u000E' && LA15_10 <= '\u001F')||(LA15_10 >= '#' && LA15_10 <= '%')||LA15_10=='*'||LA15_10==','||LA15_10=='.'||(LA15_10 >= '0' && LA15_10 <= '9')||(LA15_10 >= ';' && LA15_10 <= 'Z')||(LA15_10 >= '_' && LA15_10 <= 'z')||(LA15_10 >= '\u007F' && LA15_10 <= '\uFFFF')) ) {s = 30;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA15_46 = input.LA(1);

                        s = -1;
                        if ( (LA15_46=='.') ) {s = 44;}

                        else if ( ((LA15_46 >= '0' && LA15_46 <= '9')) ) {s = 46;}

                        else if ( ((LA15_46 >= '\u0000' && LA15_46 <= '\b')||(LA15_46 >= '\u000B' && LA15_46 <= '\f')||(LA15_46 >= '\u000E' && LA15_46 <= '\u001F')||(LA15_46 >= '#' && LA15_46 <= '%')||LA15_46==','||(LA15_46 >= ';' && LA15_46 <= '>')||(LA15_46 >= '@' && LA15_46 <= 'Z')||(LA15_46 >= '_' && LA15_46 <= 'z')||(LA15_46 >= '\u007F' && LA15_46 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_46=='\\') ) {s = 37;}

                        else if ( (LA15_46=='*'||LA15_46=='?') ) {s = 30;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA15_33 = input.LA(1);

                        s = -1;
                        if ( (LA15_33=='\"') ) {s = 47;}

                        else if ( ((LA15_33 >= '\u0000' && LA15_33 <= '!')||(LA15_33 >= '#' && LA15_33 <= ')')||(LA15_33 >= '+' && LA15_33 <= '>')||(LA15_33 >= '@' && LA15_33 <= '\uFFFF')) ) {s = 33;}

                        else if ( (LA15_33=='*'||LA15_33=='?') ) {s = 34;}

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA15_19 = input.LA(1);

                        s = -1;
                        if ( (LA15_19=='N'||LA15_19=='n') ) {s = 39;}

                        else if ( ((LA15_19 >= '\u0000' && LA15_19 <= '\b')||(LA15_19 >= '\u000B' && LA15_19 <= '\f')||(LA15_19 >= '\u000E' && LA15_19 <= '\u001F')||(LA15_19 >= '#' && LA15_19 <= '%')||LA15_19==','||LA15_19=='.'||(LA15_19 >= '0' && LA15_19 <= '9')||(LA15_19 >= ';' && LA15_19 <= '>')||(LA15_19 >= '@' && LA15_19 <= 'M')||(LA15_19 >= 'O' && LA15_19 <= 'Z')||(LA15_19 >= '_' && LA15_19 <= 'm')||(LA15_19 >= 'o' && LA15_19 <= 'z')||(LA15_19 >= '\u007F' && LA15_19 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_19=='*'||LA15_19=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA15_0 = input.LA(1);

                        s = -1;
                        if ( (LA15_0=='/') ) {s = 1;}

                        else if ( (LA15_0=='(') ) {s = 2;}

                        else if ( (LA15_0==')') ) {s = 3;}

                        else if ( (LA15_0=='[') ) {s = 4;}

                        else if ( (LA15_0==']') ) {s = 5;}

                        else if ( (LA15_0==':') ) {s = 6;}

                        else if ( (LA15_0=='+') ) {s = 7;}

                        else if ( (LA15_0=='-') ) {s = 8;}

                        else if ( (LA15_0=='*') ) {s = 9;}

                        else if ( (LA15_0=='?') ) {s = 10;}

                        else if ( (LA15_0=='{') ) {s = 11;}

                        else if ( (LA15_0=='}') ) {s = 12;}

                        else if ( (LA15_0=='^') ) {s = 13;}

                        else if ( (LA15_0=='~') ) {s = 14;}

                        else if ( (LA15_0=='\"') ) {s = 15;}

                        else if ( (LA15_0=='\'') ) {s = 16;}

                        else if ( (LA15_0=='\\') ) {s = 17;}

                        else if ( (LA15_0=='T') ) {s = 18;}

                        else if ( (LA15_0=='A'||LA15_0=='a') ) {s = 19;}

                        else if ( (LA15_0=='&') ) {s = 20;}

                        else if ( (LA15_0=='O'||LA15_0=='o') ) {s = 21;}

                        else if ( (LA15_0=='|') ) {s = 22;}

                        else if ( (LA15_0=='n') ) {s = 23;}

                        else if ( (LA15_0=='!') ) {s = 24;}

                        else if ( (LA15_0=='N') ) {s = 25;}

                        else if ( ((LA15_0 >= '\t' && LA15_0 <= '\n')||LA15_0=='\r'||LA15_0==' ') ) {s = 26;}

                        else if ( ((LA15_0 >= '0' && LA15_0 <= '9')) ) {s = 27;}

                        else if ( ((LA15_0 >= '\u0000' && LA15_0 <= '\b')||(LA15_0 >= '\u000B' && LA15_0 <= '\f')||(LA15_0 >= '\u000E' && LA15_0 <= '\u001F')||(LA15_0 >= '#' && LA15_0 <= '%')||LA15_0==','||LA15_0=='.'||(LA15_0 >= ';' && LA15_0 <= '>')||LA15_0=='@'||(LA15_0 >= 'B' && LA15_0 <= 'M')||(LA15_0 >= 'P' && LA15_0 <= 'S')||(LA15_0 >= 'U' && LA15_0 <= 'Z')||(LA15_0 >= '_' && LA15_0 <= '`')||(LA15_0 >= 'b' && LA15_0 <= 'm')||(LA15_0 >= 'p' && LA15_0 <= 'z')||(LA15_0 >= '\u007F' && LA15_0 <= '\uFFFF')) ) {s = 28;}

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA15_27 = input.LA(1);

                        s = -1;
                        if ( (LA15_27=='.') ) {s = 44;}

                        else if ( ((LA15_27 >= '0' && LA15_27 <= '9')) ) {s = 46;}

                        else if ( ((LA15_27 >= '\u0000' && LA15_27 <= '\b')||(LA15_27 >= '\u000B' && LA15_27 <= '\f')||(LA15_27 >= '\u000E' && LA15_27 <= '\u001F')||(LA15_27 >= '#' && LA15_27 <= '%')||LA15_27==','||(LA15_27 >= ';' && LA15_27 <= '>')||(LA15_27 >= '@' && LA15_27 <= 'Z')||(LA15_27 >= '_' && LA15_27 <= 'z')||(LA15_27 >= '\u007F' && LA15_27 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_27=='\\') ) {s = 37;}

                        else if ( (LA15_27=='*'||LA15_27=='?') ) {s = 30;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA15_21 = input.LA(1);

                        s = -1;
                        if ( (LA15_21=='R'||LA15_21=='r') ) {s = 40;}

                        else if ( ((LA15_21 >= '\u0000' && LA15_21 <= '\b')||(LA15_21 >= '\u000B' && LA15_21 <= '\f')||(LA15_21 >= '\u000E' && LA15_21 <= '\u001F')||(LA15_21 >= '#' && LA15_21 <= '%')||LA15_21==','||LA15_21=='.'||(LA15_21 >= '0' && LA15_21 <= '9')||(LA15_21 >= ';' && LA15_21 <= '>')||(LA15_21 >= '@' && LA15_21 <= 'Q')||(LA15_21 >= 'S' && LA15_21 <= 'Z')||(LA15_21 >= '_' && LA15_21 <= 'q')||(LA15_21 >= 's' && LA15_21 <= 'z')||(LA15_21 >= '\u007F' && LA15_21 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_21=='*'||LA15_21=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA15_41 = input.LA(1);

                        s = -1;
                        if ( (LA15_41=='T'||LA15_41=='t') ) {s = 51;}

                        else if ( ((LA15_41 >= '\u0000' && LA15_41 <= '\b')||(LA15_41 >= '\u000B' && LA15_41 <= '\f')||(LA15_41 >= '\u000E' && LA15_41 <= '\u001F')||(LA15_41 >= '#' && LA15_41 <= '%')||LA15_41==','||LA15_41=='.'||(LA15_41 >= '0' && LA15_41 <= '9')||(LA15_41 >= ';' && LA15_41 <= '>')||(LA15_41 >= '@' && LA15_41 <= 'S')||(LA15_41 >= 'U' && LA15_41 <= 'Z')||(LA15_41 >= '_' && LA15_41 <= 's')||(LA15_41 >= 'u' && LA15_41 <= 'z')||(LA15_41 >= '\u007F' && LA15_41 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_41=='*'||LA15_41=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA15_42 = input.LA(1);

                        s = -1;
                        if ( (LA15_42=='A'||LA15_42=='a') ) {s = 52;}

                        else if ( ((LA15_42 >= '\u0000' && LA15_42 <= '\b')||(LA15_42 >= '\u000B' && LA15_42 <= '\f')||(LA15_42 >= '\u000E' && LA15_42 <= '\u001F')||(LA15_42 >= '#' && LA15_42 <= '%')||LA15_42==','||LA15_42=='.'||(LA15_42 >= '0' && LA15_42 <= '9')||(LA15_42 >= ';' && LA15_42 <= '>')||LA15_42=='@'||(LA15_42 >= 'B' && LA15_42 <= 'Z')||(LA15_42 >= '_' && LA15_42 <= '`')||(LA15_42 >= 'b' && LA15_42 <= 'z')||(LA15_42 >= '\u007F' && LA15_42 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_42=='*'||LA15_42=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA15_52 = input.LA(1);

                        s = -1;
                        if ( (LA15_52=='R'||LA15_52=='r') ) {s = 55;}

                        else if ( ((LA15_52 >= '\u0000' && LA15_52 <= '\b')||(LA15_52 >= '\u000B' && LA15_52 <= '\f')||(LA15_52 >= '\u000E' && LA15_52 <= '\u001F')||(LA15_52 >= '#' && LA15_52 <= '%')||LA15_52==','||LA15_52=='.'||(LA15_52 >= '0' && LA15_52 <= '9')||(LA15_52 >= ';' && LA15_52 <= '>')||(LA15_52 >= '@' && LA15_52 <= 'Q')||(LA15_52 >= 'S' && LA15_52 <= 'Z')||(LA15_52 >= '_' && LA15_52 <= 'q')||(LA15_52 >= 's' && LA15_52 <= 'z')||(LA15_52 >= '\u007F' && LA15_52 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_52=='*'||LA15_52=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA15_35 = input.LA(1);

                        s = -1;
                        if ( ((LA15_35 >= '\u0000' && LA15_35 <= '\b')||(LA15_35 >= '\u000B' && LA15_35 <= '\f')||(LA15_35 >= '\u000E' && LA15_35 <= '\u001F')||(LA15_35 >= '#' && LA15_35 <= '%')||LA15_35==','||LA15_35=='.'||(LA15_35 >= '0' && LA15_35 <= '9')||(LA15_35 >= ';' && LA15_35 <= '>')||(LA15_35 >= '@' && LA15_35 <= 'Z')||LA15_35=='\\'||(LA15_35 >= '_' && LA15_35 <= 'z')||(LA15_35 >= '\u007F' && LA15_35 <= '\uFFFF')) ) {s = 37;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA15_36 = input.LA(1);

                        s = -1;
                        if ( ((LA15_36 >= '\u0000' && LA15_36 <= '\b')||(LA15_36 >= '\u000B' && LA15_36 <= '\f')||(LA15_36 >= '\u000E' && LA15_36 <= '\u001F')||(LA15_36 >= '#' && LA15_36 <= '%')||LA15_36==','||LA15_36=='.'||(LA15_36 >= '0' && LA15_36 <= '9')||(LA15_36 >= ';' && LA15_36 <= '>')||(LA15_36 >= '@' && LA15_36 <= 'Z')||(LA15_36 >= '_' && LA15_36 <= 'z')||(LA15_36 >= '\u007F' && LA15_36 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_36=='\\') ) {s = 37;}

                        else if ( (LA15_36=='*'||LA15_36=='?') ) {s = 30;}

                        else s = 49;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA15_53 = input.LA(1);

                        s = -1;
                        if ( ((LA15_53 >= '0' && LA15_53 <= '9')) ) {s = 53;}

                        else if ( ((LA15_53 >= '\u0000' && LA15_53 <= '\b')||(LA15_53 >= '\u000B' && LA15_53 <= '\f')||(LA15_53 >= '\u000E' && LA15_53 <= '\u001F')||(LA15_53 >= '#' && LA15_53 <= '%')||LA15_53==','||LA15_53=='.'||(LA15_53 >= ';' && LA15_53 <= '>')||(LA15_53 >= '@' && LA15_53 <= 'Z')||(LA15_53 >= '_' && LA15_53 <= 'z')||(LA15_53 >= '\u007F' && LA15_53 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_53=='\\') ) {s = 37;}

                        else if ( (LA15_53=='*'||LA15_53=='?') ) {s = 30;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA15_15 = input.LA(1);

                        s = -1;
                        if ( ((LA15_15 >= '\u0000' && LA15_15 <= '!')||(LA15_15 >= '#' && LA15_15 <= ')')||(LA15_15 >= '+' && LA15_15 <= '>')||(LA15_15 >= '@' && LA15_15 <= '\uFFFF')) ) {s = 33;}

                        else if ( (LA15_15=='*'||LA15_15=='?') ) {s = 34;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA15_44 = input.LA(1);

                        s = -1;
                        if ( ((LA15_44 >= '0' && LA15_44 <= '9')) ) {s = 53;}

                        else if ( ((LA15_44 >= '\u0000' && LA15_44 <= '\b')||(LA15_44 >= '\u000B' && LA15_44 <= '\f')||(LA15_44 >= '\u000E' && LA15_44 <= '\u001F')||(LA15_44 >= '#' && LA15_44 <= '%')||LA15_44==','||LA15_44=='.'||(LA15_44 >= ';' && LA15_44 <= '>')||(LA15_44 >= '@' && LA15_44 <= 'Z')||(LA15_44 >= '_' && LA15_44 <= 'z')||(LA15_44 >= '\u007F' && LA15_44 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_44=='*'||LA15_44=='?') ) {s = 30;}

                        else s = 37;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA15_51 = input.LA(1);

                        s = -1;
                        if ( ((LA15_51 >= '\u0000' && LA15_51 <= '\b')||(LA15_51 >= '\u000B' && LA15_51 <= '\f')||(LA15_51 >= '\u000E' && LA15_51 <= '\u001F')||(LA15_51 >= '#' && LA15_51 <= '%')||LA15_51==','||LA15_51=='.'||(LA15_51 >= '0' && LA15_51 <= '9')||(LA15_51 >= ';' && LA15_51 <= '>')||(LA15_51 >= '@' && LA15_51 <= 'Z')||(LA15_51 >= '_' && LA15_51 <= 'z')||(LA15_51 >= '\u007F' && LA15_51 <= '\uFFFF')) ) {s = 38;}

                        else if ( (LA15_51=='\\') ) {s = 37;}

                        else if ( (LA15_51=='*'||LA15_51=='?') ) {s = 30;}

                        else s = 24;

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