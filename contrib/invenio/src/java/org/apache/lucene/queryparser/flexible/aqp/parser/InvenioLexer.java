// $ANTLR 3.4 /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g 2012-07-30 21:37:27

   package org.apache.lucene.queryparser.flexible.aqp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class InvenioLexer extends Lexer {
    public static final int EOF=-1;
    public static final int AMBIGUITY=4;
    public static final int AMPER=5;
    public static final int AND=6;
    public static final int ATOM=7;
    public static final int BAR=8;
    public static final int BOOST=9;
    public static final int CARAT=10;
    public static final int CLAUSE=11;
    public static final int COLON=12;
    public static final int DATE_TOKEN=13;
    public static final int DQUOTE=14;
    public static final int ESC_CHAR=15;
    public static final int FIELD=16;
    public static final int FUZZY=17;
    public static final int IDENTIFIER=18;
    public static final int INT=19;
    public static final int LBRACK=20;
    public static final int LCURLY=21;
    public static final int LPAREN=22;
    public static final int MINUS=23;
    public static final int MODIFIER=24;
    public static final int NEAR=25;
    public static final int NOT=26;
    public static final int NUMBER=27;
    public static final int OPERATOR=28;
    public static final int OR=29;
    public static final int PHRASE=30;
    public static final int PHRASE_ANYTHING=31;
    public static final int PLUS=32;
    public static final int QANYTHING=33;
    public static final int QDATE=34;
    public static final int QFUNC=35;
    public static final int QMARK=36;
    public static final int QNORMAL=37;
    public static final int QPHRASE=38;
    public static final int QPHRASETRUNC=39;
    public static final int QRANGEEX=40;
    public static final int QRANGEIN=41;
    public static final int QREGEX=42;
    public static final int QTRUNCATED=43;
    public static final int RBRACK=44;
    public static final int RCURLY=45;
    public static final int REGEX=46;
    public static final int RPAREN=47;
    public static final int SECOND_ORDER_OP=48;
    public static final int SLASH=49;
    public static final int SQUOTE=50;
    public static final int STAR=51;
    public static final int TERM_CHAR=52;
    public static final int TERM_NORMAL=53;
    public static final int TERM_START_CHAR=54;
    public static final int TERM_TRUNCATED=55;
    public static final int TILDE=56;
    public static final int TMODIFIER=57;
    public static final int TO=58;
    public static final int VBAR=59;
    public static final int WS=60;

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
    public String getGrammarFileName() { return "/dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g"; }

    // $ANTLR start "SECOND_ORDER_OP"
    public final void mSECOND_ORDER_OP() throws RecognitionException {
        try {
            int _type = SECOND_ORDER_OP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:331:2: ( ( 'refersto' | 'citedby' | 'cited' | 'cocitedwith' ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:332:2: ( 'refersto' | 'citedby' | 'cited' | 'cocitedwith' )
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:332:2: ( 'refersto' | 'citedby' | 'cited' | 'cocitedwith' )
            int alt1=4;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='r') ) {
                alt1=1;
            }
            else if ( (LA1_0=='c') ) {
                int LA1_2 = input.LA(2);

                if ( (LA1_2=='i') ) {
                    int LA1_3 = input.LA(3);

                    if ( (LA1_3=='t') ) {
                        int LA1_5 = input.LA(4);

                        if ( (LA1_5=='e') ) {
                            int LA1_6 = input.LA(5);

                            if ( (LA1_6=='d') ) {
                                int LA1_7 = input.LA(6);

                                if ( (LA1_7=='b') ) {
                                    alt1=2;
                                }
                                else {
                                    alt1=3;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 1, 6, input);

                                throw nvae;

                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 1, 5, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 3, input);

                        throw nvae;

                    }
                }
                else if ( (LA1_2=='o') ) {
                    alt1=4;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 2, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }
            switch (alt1) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:332:3: 'refersto'
                    {
                    match("refersto"); 



                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:333:4: 'citedby'
                    {
                    match("citedby"); 



                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:334:4: 'cited'
                    {
                    match("cited"); 



                    }
                    break;
                case 4 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:335:4: 'cocitedwith'
                    {
                    match("cocitedwith"); 



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
    // $ANTLR end "SECOND_ORDER_OP"

    // $ANTLR start "IDENTIFIER"
    public final void mIDENTIFIER() throws RecognitionException {
        try {
            int _type = IDENTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:340:2: ( ( 'arXiv' | 'arxiv' ) ':' ( TERM_CHAR )+ | ( INT )+ '.' ( INT )+ '/' ( INT )+ ( '.' ( INT )+ )? )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='a') ) {
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:340:4: ( 'arXiv' | 'arxiv' ) ':' ( TERM_CHAR )+
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:340:4: ( 'arXiv' | 'arxiv' )
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0=='a') ) {
                        int LA2_1 = input.LA(2);

                        if ( (LA2_1=='r') ) {
                            int LA2_2 = input.LA(3);

                            if ( (LA2_2=='X') ) {
                                alt2=1;
                            }
                            else if ( (LA2_2=='x') ) {
                                alt2=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 2, 2, input);

                                throw nvae;

                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 2, 1, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 0, input);

                        throw nvae;

                    }
                    switch (alt2) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:340:5: 'arXiv'
                            {
                            match("arXiv"); 



                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:340:13: 'arxiv'
                            {
                            match("arxiv"); 



                            }
                            break;

                    }


                    match(':'); 

                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:340:26: ( TERM_CHAR )+
                    int cnt3=0;
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( ((LA3_0 >= '\u0000' && LA3_0 <= '\b')||(LA3_0 >= '\u000B' && LA3_0 <= '\f')||(LA3_0 >= '\u000E' && LA3_0 <= '\u001F')||(LA3_0 >= '#' && LA3_0 <= '\'')||(LA3_0 >= '+' && LA3_0 <= '9')||(LA3_0 >= ';' && LA3_0 <= '>')||(LA3_0 >= '@' && LA3_0 <= 'Z')||LA3_0=='\\'||(LA3_0 >= '_' && LA3_0 <= 'z')||(LA3_0 >= '\u007F' && LA3_0 <= '\u2FFF')||(LA3_0 >= '\u3001' && LA3_0 <= '\u300B')||(LA3_0 >= '\u300D' && LA3_0 <= '\uFFFF')) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:340:26: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


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
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:341:4: ( INT )+ '.' ( INT )+ '/' ( INT )+ ( '.' ( INT )+ )?
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:341:4: ( INT )+
                    int cnt4=0;
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
                    	    if ( cnt4 >= 1 ) break loop4;
                                EarlyExitException eee =
                                    new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);


                    match('.'); 

                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:341:13: ( INT )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0 >= '0' && LA5_0 <= '9')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
                    	    if ( cnt5 >= 1 ) break loop5;
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);


                    match('/'); 

                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:341:22: ( INT )+
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
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:341:27: ( '.' ( INT )+ )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='.') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:341:28: '.' ( INT )+
                            {
                            match('.'); 

                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:341:32: ( INT )+
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
                            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "IDENTIFIER"

    // $ANTLR start "SLASH"
    public final void mSLASH() throws RecognitionException {
        try {
            int _type = SLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:344:9: ( '/' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:344:11: '/'
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
    // $ANTLR end "SLASH"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:346:9: ( '(' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:346:11: '('
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:348:9: ( ')' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:348:11: ')'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:350:9: ( '[' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:350:11: '['
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:352:9: ( ']' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:352:11: ']'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:354:9: ( ':' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:354:11: ':'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:356:16: ( '+' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:356:18: '+'
            {
            match('+'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:358:16: ( '-' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:358:18: '-'
            {
            match('-'); 

            }


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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:360:7: ( '*' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:360:9: '*'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:362:8: ( ( '?' )+ )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:362:10: ( '?' )+
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:362:10: ( '?' )+
            int cnt10=0;
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='?') ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:362:10: '?'
            	    {
            	    match('?'); 

            	    }
            	    break;

            	default :
            	    if ( cnt10 >= 1 ) break loop10;
                        EarlyExitException eee =
                            new EarlyExitException(10, input);
                        throw eee;
                }
                cnt10++;
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:364:16: ( '|' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:364:18: '|'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:366:16: ( '&' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:366:18: '&'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:368:9: ( '{' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:368:11: '{'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:370:9: ( '}' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:370:11: '}'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:372:7: ( '^' ( ( INT )+ ( '.' ( INT )+ )? )? )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:372:9: '^' ( ( INT )+ ( '.' ( INT )+ )? )?
            {
            match('^'); 

            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:372:13: ( ( INT )+ ( '.' ( INT )+ )? )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( ((LA14_0 >= '0' && LA14_0 <= '9')) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:372:14: ( INT )+ ( '.' ( INT )+ )?
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:372:14: ( INT )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0 >= '0' && LA11_0 <= '9')) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:372:19: ( '.' ( INT )+ )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0=='.') ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:372:20: '.' ( INT )+
                            {
                            match('.'); 

                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:372:24: ( INT )+
                            int cnt12=0;
                            loop12:
                            do {
                                int alt12=2;
                                int LA12_0 = input.LA(1);

                                if ( ((LA12_0 >= '0' && LA12_0 <= '9')) ) {
                                    alt12=1;
                                }


                                switch (alt12) {
                            	case 1 :
                            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:374:7: ( '~' ( ( INT )+ ( '.' ( INT )+ )? )? )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:374:9: '~' ( ( INT )+ ( '.' ( INT )+ )? )?
            {
            match('~'); 

            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:374:13: ( ( INT )+ ( '.' ( INT )+ )? )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( ((LA18_0 >= '0' && LA18_0 <= '9')) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:374:14: ( INT )+ ( '.' ( INT )+ )?
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:374:14: ( INT )+
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
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:374:19: ( '.' ( INT )+ )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0=='.') ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:374:20: '.' ( INT )+
                            {
                            match('.'); 

                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:374:24: ( INT )+
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
                            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:377:2: ( '\\\"' | '\\u300C' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
            {
            if ( input.LA(1)=='\"'||input.LA(1)=='\u300C' ) {
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
    // $ANTLR end "DQUOTE"

    // $ANTLR start "SQUOTE"
    public final void mSQUOTE() throws RecognitionException {
        try {
            int _type = SQUOTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:380:2: ( '\\'' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:380:4: '\\''
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

    // $ANTLR start "BAR"
    public final void mBAR() throws RecognitionException {
        try {
            int _type = BAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:382:5: ( '#' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:382:7: '#'
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
    // $ANTLR end "BAR"

    // $ANTLR start "TO"
    public final void mTO() throws RecognitionException {
        try {
            int _type = TO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:386:4: ( 'TO' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:386:6: 'TO'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:389:7: ( ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) ) | ( AMPER ( AMPER )? ) | PLUS )
            int alt20=3;
            switch ( input.LA(1) ) {
            case 'A':
            case 'a':
                {
                alt20=1;
                }
                break;
            case '&':
                {
                alt20=2;
                }
                break;
            case '+':
                {
                alt20=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;

            }

            switch (alt20) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:389:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:389:9: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:389:10: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:389:49: ( AMPER ( AMPER )? )
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:389:49: ( AMPER ( AMPER )? )
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:389:50: AMPER ( AMPER )?
                    {
                    mAMPER(); 


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:389:56: ( AMPER )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0=='&') ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:389:66: PLUS
                    {
                    mPLUS(); 


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
    // $ANTLR end "AND"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:390:5: ( ( ( 'o' | 'O' ) ( 'r' | 'R' ) ) | ( VBAR ( VBAR )? ) )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0=='O'||LA22_0=='o') ) {
                alt22=1;
            }
            else if ( (LA22_0=='|') ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;

            }
            switch (alt22) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:390:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:390:7: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:390:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:390:35: ( VBAR ( VBAR )? )
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:390:35: ( VBAR ( VBAR )? )
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:390:36: VBAR ( VBAR )?
                    {
                    mVBAR(); 


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:390:41: ( VBAR )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0=='|') ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:391:7: ( ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) ) | MINUS )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0=='N'||LA23_0=='n') ) {
                alt23=1;
            }
            else if ( (LA23_0=='-') ) {
                alt23=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;

            }
            switch (alt23) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:391:9: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:391:9: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:391:10: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:391:49: MINUS
                    {
                    mMINUS(); 


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
    // $ANTLR end "NOT"

    // $ANTLR start "NEAR"
    public final void mNEAR() throws RecognitionException {
        try {
            int _type = NEAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:392:7: ( ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:392:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:392:9: ( ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' ) | 'n' )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0=='n') ) {
                int LA24_1 = input.LA(2);

                if ( (LA24_1=='E'||LA24_1=='e') ) {
                    alt24=1;
                }
                else {
                    alt24=2;
                }
            }
            else if ( (LA24_0=='N') ) {
                alt24=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;

            }
            switch (alt24) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:392:10: ( 'n' | 'N' ) ( 'e' | 'E' ) ( 'a' | 'A' ) ( 'r' | 'R' )
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:392:60: 'n'
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:395:5: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:395:9: ( ' ' | '\\t' | '\\r' | '\\n' | '\\u3000' )
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:416:13: ( '0' .. '9' )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:419:18: ( '\\\\' . )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:419:21: '\\\\' .
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:423:2: ( (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | '#' | '|' | '/' | '\\u300C' ) | ESC_CHAR ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:424:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | '#' | '|' | '/' | '\\u300C' ) | ESC_CHAR )
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:424:2: (~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | '#' | '|' | '/' | '\\u300C' ) | ESC_CHAR )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( ((LA25_0 >= '\u0000' && LA25_0 <= '\b')||(LA25_0 >= '\u000B' && LA25_0 <= '\f')||(LA25_0 >= '\u000E' && LA25_0 <= '\u001F')||(LA25_0 >= '$' && LA25_0 <= '&')||LA25_0==','||LA25_0=='.'||(LA25_0 >= '0' && LA25_0 <= '9')||(LA25_0 >= ';' && LA25_0 <= '>')||(LA25_0 >= '@' && LA25_0 <= 'Z')||(LA25_0 >= '_' && LA25_0 <= 'z')||(LA25_0 >= '\u007F' && LA25_0 <= '\u2FFF')||(LA25_0 >= '\u3001' && LA25_0 <= '\u300B')||(LA25_0 >= '\u300D' && LA25_0 <= '\uFFFF')) ) {
                alt25=1;
            }
            else if ( (LA25_0=='\\') ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;

            }
            switch (alt25) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:424:3: ~ ( ' ' | '\\t' | '\\n' | '\\r' | '\\u3000' | '\\'' | '\\\"' | '(' | ')' | '[' | ']' | '{' | '}' | '+' | '-' | '!' | ':' | '~' | '^' | '?' | '*' | '\\\\' | '#' | '|' | '/' | '\\u300C' )
                    {
                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\u001F')||(input.LA(1) >= '$' && input.LA(1) <= '&')||input.LA(1)==','||input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= ';' && input.LA(1) <= '>')||(input.LA(1) >= '@' && input.LA(1) <= 'Z')||(input.LA(1) >= '_' && input.LA(1) <= 'z')||(input.LA(1) >= '\u007F' && input.LA(1) <= '\u2FFF')||(input.LA(1) >= '\u3001' && input.LA(1) <= '\u300B')||(input.LA(1) >= '\u300D' && input.LA(1) <= '\uFFFF') ) {
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
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:431:5: ESC_CHAR
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:435:2: ( ( TERM_START_CHAR | '-' | '+' | '#' | '/' | '\\'' ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:436:2: ( TERM_START_CHAR | '-' | '+' | '#' | '/' | '\\'' )
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:436:2: ( TERM_START_CHAR | '-' | '+' | '#' | '/' | '\\'' )
            int alt26=6;
            int LA26_0 = input.LA(1);

            if ( ((LA26_0 >= '\u0000' && LA26_0 <= '\b')||(LA26_0 >= '\u000B' && LA26_0 <= '\f')||(LA26_0 >= '\u000E' && LA26_0 <= '\u001F')||(LA26_0 >= '$' && LA26_0 <= '&')||LA26_0==','||LA26_0=='.'||(LA26_0 >= '0' && LA26_0 <= '9')||(LA26_0 >= ';' && LA26_0 <= '>')||(LA26_0 >= '@' && LA26_0 <= 'Z')||LA26_0=='\\'||(LA26_0 >= '_' && LA26_0 <= 'z')||(LA26_0 >= '\u007F' && LA26_0 <= '\u2FFF')||(LA26_0 >= '\u3001' && LA26_0 <= '\u300B')||(LA26_0 >= '\u300D' && LA26_0 <= '\uFFFF')) ) {
                alt26=1;
            }
            else if ( (LA26_0=='-') ) {
                alt26=2;
            }
            else if ( (LA26_0=='+') ) {
                alt26=3;
            }
            else if ( (LA26_0=='#') ) {
                alt26=4;
            }
            else if ( (LA26_0=='/') ) {
                alt26=5;
            }
            else if ( (LA26_0=='\'') ) {
                alt26=6;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;

            }
            switch (alt26) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:436:3: TERM_START_CHAR
                    {
                    mTERM_START_CHAR(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:436:21: '-'
                    {
                    match('-'); 

                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:436:27: '+'
                    {
                    match('+'); 

                    }
                    break;
                case 4 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:436:33: '#'
                    {
                    match('#'); 

                    }
                    break;
                case 5 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:436:39: '/'
                    {
                    match('/'); 

                    }
                    break;
                case 6 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:436:45: '\\''
                    {
                    match('\''); 

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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:441:2: ( ( INT )+ ( '.' ( INT )+ )? )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:442:2: ( INT )+ ( '.' ( INT )+ )?
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:442:2: ( INT )+
            int cnt27=0;
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( ((LA27_0 >= '0' && LA27_0 <= '9')) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
            	    if ( cnt27 >= 1 ) break loop27;
                        EarlyExitException eee =
                            new EarlyExitException(27, input);
                        throw eee;
                }
                cnt27++;
            } while (true);


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:442:7: ( '.' ( INT )+ )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0=='.') ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:442:8: '.' ( INT )+
                    {
                    match('.'); 

                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:442:12: ( INT )+
                    int cnt28=0;
                    loop28:
                    do {
                        int alt28=2;
                        int LA28_0 = input.LA(1);

                        if ( ((LA28_0 >= '0' && LA28_0 <= '9')) ) {
                            alt28=1;
                        }


                        switch (alt28) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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
                    	    if ( cnt28 >= 1 ) break loop28;
                                EarlyExitException eee =
                                    new EarlyExitException(28, input);
                                throw eee;
                        }
                        cnt28++;
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:446:2: ( INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )? )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:447:2: INT ( INT )? ( '/' | '-' | '.' ) INT ( INT )? ( '/' | '-' | '.' ) INT INT ( INT INT )?
            {
            mINT(); 


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:447:6: ( INT )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( ((LA30_0 >= '0' && LA30_0 <= '9')) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:447:29: ( INT )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( ((LA31_0 >= '0' && LA31_0 <= '9')) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:
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


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:447:56: ( INT INT )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( ((LA32_0 >= '0' && LA32_0 <= '9')) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:447:57: INT INT
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:451:2: ( TERM_START_CHAR ( TERM_CHAR )* )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:452:2: TERM_START_CHAR ( TERM_CHAR )*
            {
            mTERM_START_CHAR(); 


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:452:18: ( TERM_CHAR )*
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( ((LA33_0 >= '\u0000' && LA33_0 <= '\b')||(LA33_0 >= '\u000B' && LA33_0 <= '\f')||(LA33_0 >= '\u000E' && LA33_0 <= '\u001F')||(LA33_0 >= '#' && LA33_0 <= '\'')||(LA33_0 >= '+' && LA33_0 <= '9')||(LA33_0 >= ';' && LA33_0 <= '>')||(LA33_0 >= '@' && LA33_0 <= 'Z')||LA33_0=='\\'||(LA33_0 >= '_' && LA33_0 <= 'z')||(LA33_0 >= '\u007F' && LA33_0 <= '\u2FFF')||(LA33_0 >= '\u3001' && LA33_0 <= '\u300B')||(LA33_0 >= '\u300D' && LA33_0 <= '\uFFFF')) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:452:20: TERM_CHAR
            	    {
            	    mTERM_CHAR(); 


            	    }
            	    break;

            	default :
            	    break loop33;
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:456:15: ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ )
            int alt45=3;
            alt45 = dfa45.predict(input);
            switch (alt45) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:2: ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )*
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:2: ( STAR | QMARK )
                    int alt34=2;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0=='*') ) {
                        alt34=1;
                    }
                    else if ( (LA34_0=='?') ) {
                        alt34=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 34, 0, input);

                        throw nvae;

                    }
                    switch (alt34) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:3: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:8: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+
                    int cnt37=0;
                    loop37:
                    do {
                        int alt37=2;
                        alt37 = dfa37.predict(input);
                        switch (alt37) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:16: ( TERM_CHAR )+ ( QMARK | STAR )
                    	    {
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:16: ( TERM_CHAR )+
                    	    int cnt35=0;
                    	    loop35:
                    	    do {
                    	        int alt35=2;
                    	        int LA35_0 = input.LA(1);

                    	        if ( ((LA35_0 >= '\u0000' && LA35_0 <= '\b')||(LA35_0 >= '\u000B' && LA35_0 <= '\f')||(LA35_0 >= '\u000E' && LA35_0 <= '\u001F')||(LA35_0 >= '#' && LA35_0 <= '\'')||(LA35_0 >= '+' && LA35_0 <= '9')||(LA35_0 >= ';' && LA35_0 <= '>')||(LA35_0 >= '@' && LA35_0 <= 'Z')||LA35_0=='\\'||(LA35_0 >= '_' && LA35_0 <= 'z')||(LA35_0 >= '\u007F' && LA35_0 <= '\u2FFF')||(LA35_0 >= '\u3001' && LA35_0 <= '\u300B')||(LA35_0 >= '\u300D' && LA35_0 <= '\uFFFF')) ) {
                    	            alt35=1;
                    	        }


                    	        switch (alt35) {
                    	    	case 1 :
                    	    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:16: TERM_CHAR
                    	    	    {
                    	    	    mTERM_CHAR(); 


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


                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:27: ( QMARK | STAR )
                    	    int alt36=2;
                    	    int LA36_0 = input.LA(1);

                    	    if ( (LA36_0=='?') ) {
                    	        alt36=1;
                    	    }
                    	    else if ( (LA36_0=='*') ) {
                    	        alt36=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 36, 0, input);

                    	        throw nvae;

                    	    }
                    	    switch (alt36) {
                    	        case 1 :
                    	            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:28: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:34: STAR
                    	            {
                    	            mSTAR(); 


                    	            }
                    	            break;

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


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:42: ( TERM_CHAR )*
                    loop38:
                    do {
                        int alt38=2;
                        int LA38_0 = input.LA(1);

                        if ( ((LA38_0 >= '\u0000' && LA38_0 <= '\b')||(LA38_0 >= '\u000B' && LA38_0 <= '\f')||(LA38_0 >= '\u000E' && LA38_0 <= '\u001F')||(LA38_0 >= '#' && LA38_0 <= '\'')||(LA38_0 >= '+' && LA38_0 <= '9')||(LA38_0 >= ';' && LA38_0 <= '>')||(LA38_0 >= '@' && LA38_0 <= 'Z')||LA38_0=='\\'||(LA38_0 >= '_' && LA38_0 <= 'z')||(LA38_0 >= '\u007F' && LA38_0 <= '\u2FFF')||(LA38_0 >= '\u3001' && LA38_0 <= '\u300B')||(LA38_0 >= '\u300D' && LA38_0 <= '\uFFFF')) ) {
                            alt38=1;
                        }


                        switch (alt38) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:457:43: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop38;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:4: TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )*
                    {
                    mTERM_START_CHAR(); 


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+
                    int cnt41=0;
                    loop41:
                    do {
                        int alt41=2;
                        alt41 = dfa41.predict(input);
                        switch (alt41) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:21: ( TERM_CHAR )* ( QMARK | STAR )
                    	    {
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:21: ( TERM_CHAR )*
                    	    loop39:
                    	    do {
                    	        int alt39=2;
                    	        int LA39_0 = input.LA(1);

                    	        if ( ((LA39_0 >= '\u0000' && LA39_0 <= '\b')||(LA39_0 >= '\u000B' && LA39_0 <= '\f')||(LA39_0 >= '\u000E' && LA39_0 <= '\u001F')||(LA39_0 >= '#' && LA39_0 <= '\'')||(LA39_0 >= '+' && LA39_0 <= '9')||(LA39_0 >= ';' && LA39_0 <= '>')||(LA39_0 >= '@' && LA39_0 <= 'Z')||LA39_0=='\\'||(LA39_0 >= '_' && LA39_0 <= 'z')||(LA39_0 >= '\u007F' && LA39_0 <= '\u2FFF')||(LA39_0 >= '\u3001' && LA39_0 <= '\u300B')||(LA39_0 >= '\u300D' && LA39_0 <= '\uFFFF')) ) {
                    	            alt39=1;
                    	        }


                    	        switch (alt39) {
                    	    	case 1 :
                    	    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:21: TERM_CHAR
                    	    	    {
                    	    	    mTERM_CHAR(); 


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    break loop39;
                    	        }
                    	    } while (true);


                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:32: ( QMARK | STAR )
                    	    int alt40=2;
                    	    int LA40_0 = input.LA(1);

                    	    if ( (LA40_0=='?') ) {
                    	        alt40=1;
                    	    }
                    	    else if ( (LA40_0=='*') ) {
                    	        alt40=2;
                    	    }
                    	    else {
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 40, 0, input);

                    	        throw nvae;

                    	    }
                    	    switch (alt40) {
                    	        case 1 :
                    	            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:33: QMARK
                    	            {
                    	            mQMARK(); 


                    	            }
                    	            break;
                    	        case 2 :
                    	            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:39: STAR
                    	            {
                    	            mSTAR(); 


                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt41 >= 1 ) break loop41;
                                EarlyExitException eee =
                                    new EarlyExitException(41, input);
                                throw eee;
                        }
                        cnt41++;
                    } while (true);


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:47: ( TERM_CHAR )*
                    loop42:
                    do {
                        int alt42=2;
                        int LA42_0 = input.LA(1);

                        if ( ((LA42_0 >= '\u0000' && LA42_0 <= '\b')||(LA42_0 >= '\u000B' && LA42_0 <= '\f')||(LA42_0 >= '\u000E' && LA42_0 <= '\u001F')||(LA42_0 >= '#' && LA42_0 <= '\'')||(LA42_0 >= '+' && LA42_0 <= '9')||(LA42_0 >= ';' && LA42_0 <= '>')||(LA42_0 >= '@' && LA42_0 <= 'Z')||LA42_0=='\\'||(LA42_0 >= '_' && LA42_0 <= 'z')||(LA42_0 >= '\u007F' && LA42_0 <= '\u2FFF')||(LA42_0 >= '\u3001' && LA42_0 <= '\u300B')||(LA42_0 >= '\u300D' && LA42_0 <= '\uFFFF')) ) {
                            alt42=1;
                        }


                        switch (alt42) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:458:48: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop42;
                        }
                    } while (true);


                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:459:4: ( STAR | QMARK ) ( TERM_CHAR )+
                    {
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:459:4: ( STAR | QMARK )
                    int alt43=2;
                    int LA43_0 = input.LA(1);

                    if ( (LA43_0=='*') ) {
                        alt43=1;
                    }
                    else if ( (LA43_0=='?') ) {
                        alt43=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 43, 0, input);

                        throw nvae;

                    }
                    switch (alt43) {
                        case 1 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:459:5: STAR
                            {
                            mSTAR(); 


                            }
                            break;
                        case 2 :
                            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:459:10: QMARK
                            {
                            mQMARK(); 


                            }
                            break;

                    }


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:459:17: ( TERM_CHAR )+
                    int cnt44=0;
                    loop44:
                    do {
                        int alt44=2;
                        int LA44_0 = input.LA(1);

                        if ( ((LA44_0 >= '\u0000' && LA44_0 <= '\b')||(LA44_0 >= '\u000B' && LA44_0 <= '\f')||(LA44_0 >= '\u000E' && LA44_0 <= '\u001F')||(LA44_0 >= '#' && LA44_0 <= '\'')||(LA44_0 >= '+' && LA44_0 <= '9')||(LA44_0 >= ';' && LA44_0 <= '>')||(LA44_0 >= '@' && LA44_0 <= 'Z')||LA44_0=='\\'||(LA44_0 >= '_' && LA44_0 <= 'z')||(LA44_0 >= '\u007F' && LA44_0 <= '\u2FFF')||(LA44_0 >= '\u3001' && LA44_0 <= '\u300B')||(LA44_0 >= '\u300D' && LA44_0 <= '\uFFFF')) ) {
                            alt44=1;
                        }


                        switch (alt44) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:459:17: TERM_CHAR
                    	    {
                    	    mTERM_CHAR(); 


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt44 >= 1 ) break loop44;
                                EarlyExitException eee =
                                    new EarlyExitException(44, input);
                                throw eee;
                        }
                        cnt44++;
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
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:464:2: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' | '\\\\\\'' | '\\u300C' ) )+ DQUOTE | SQUOTE ( ESC_CHAR |~ ( '\\'' | '\\\\' | '?' | '*' | '\\\\\\'' ) )+ SQUOTE )
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0=='\"'||LA48_0=='\u300C') ) {
                alt48=1;
            }
            else if ( (LA48_0=='\'') ) {
                alt48=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 48, 0, input);

                throw nvae;

            }
            switch (alt48) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:465:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' | '\\\\\\'' | '\\u300C' ) )+ DQUOTE
                    {
                    mDQUOTE(); 


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:465:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '?' | '*' | '\\\\\\'' | '\\u300C' ) )+
                    int cnt46=0;
                    loop46:
                    do {
                        int alt46=3;
                        int LA46_0 = input.LA(1);

                        if ( (LA46_0=='\\') ) {
                            alt46=1;
                        }
                        else if ( ((LA46_0 >= '\u0000' && LA46_0 <= '!')||(LA46_0 >= '#' && LA46_0 <= ')')||(LA46_0 >= '+' && LA46_0 <= '>')||(LA46_0 >= '@' && LA46_0 <= '[')||(LA46_0 >= ']' && LA46_0 <= '\u300B')||(LA46_0 >= '\u300D' && LA46_0 <= '\uFFFF')) ) {
                            alt46=2;
                        }


                        switch (alt46) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:465:10: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:465:19: ~ ( '\\\"' | '\\\\' | '?' | '*' | '\\\\\\'' | '\\u300C' )
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= ')')||(input.LA(1) >= '+' && input.LA(1) <= '>')||(input.LA(1) >= '@' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\u300B')||(input.LA(1) >= '\u300D' && input.LA(1) <= '\uFFFF') ) {
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
                    	    if ( cnt46 >= 1 ) break loop46;
                                EarlyExitException eee =
                                    new EarlyExitException(46, input);
                                throw eee;
                        }
                        cnt46++;
                    } while (true);


                    mDQUOTE(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:466:4: SQUOTE ( ESC_CHAR |~ ( '\\'' | '\\\\' | '?' | '*' | '\\\\\\'' ) )+ SQUOTE
                    {
                    mSQUOTE(); 


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:466:11: ( ESC_CHAR |~ ( '\\'' | '\\\\' | '?' | '*' | '\\\\\\'' ) )+
                    int cnt47=0;
                    loop47:
                    do {
                        int alt47=3;
                        int LA47_0 = input.LA(1);

                        if ( (LA47_0=='\\') ) {
                            alt47=1;
                        }
                        else if ( ((LA47_0 >= '\u0000' && LA47_0 <= '&')||(LA47_0 >= '(' && LA47_0 <= ')')||(LA47_0 >= '+' && LA47_0 <= '>')||(LA47_0 >= '@' && LA47_0 <= '[')||(LA47_0 >= ']' && LA47_0 <= '\uFFFF')) ) {
                            alt47=2;
                        }


                        switch (alt47) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:466:12: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:466:21: ~ ( '\\'' | '\\\\' | '?' | '*' | '\\\\\\'' )
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= ')')||(input.LA(1) >= '+' && input.LA(1) <= '>')||(input.LA(1) >= '@' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
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
                    	    if ( cnt47 >= 1 ) break loop47;
                                EarlyExitException eee =
                                    new EarlyExitException(47, input);
                                throw eee;
                        }
                        cnt47++;
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

    // $ANTLR start "PHRASE_ANYTHING"
    public final void mPHRASE_ANYTHING() throws RecognitionException {
        try {
            int _type = PHRASE_ANYTHING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:469:17: ( DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '\\u300C' ) )+ DQUOTE | SQUOTE ( ESC_CHAR |~ ( '\\'' | '\\\\' | '\\\\\\'' ) )+ SQUOTE )
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0=='\"'||LA51_0=='\u300C') ) {
                alt51=1;
            }
            else if ( (LA51_0=='\'') ) {
                alt51=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                throw nvae;

            }
            switch (alt51) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:470:2: DQUOTE ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '\\u300C' ) )+ DQUOTE
                    {
                    mDQUOTE(); 


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:470:9: ( ESC_CHAR |~ ( '\\\"' | '\\\\' | '\\u300C' ) )+
                    int cnt49=0;
                    loop49:
                    do {
                        int alt49=3;
                        int LA49_0 = input.LA(1);

                        if ( (LA49_0=='\\') ) {
                            alt49=1;
                        }
                        else if ( ((LA49_0 >= '\u0000' && LA49_0 <= '!')||(LA49_0 >= '#' && LA49_0 <= '[')||(LA49_0 >= ']' && LA49_0 <= '\u300B')||(LA49_0 >= '\u300D' && LA49_0 <= '\uFFFF')) ) {
                            alt49=2;
                        }


                        switch (alt49) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:470:10: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:470:19: ~ ( '\\\"' | '\\\\' | '\\u300C' )
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\u300B')||(input.LA(1) >= '\u300D' && input.LA(1) <= '\uFFFF') ) {
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
                    	    if ( cnt49 >= 1 ) break loop49;
                                EarlyExitException eee =
                                    new EarlyExitException(49, input);
                                throw eee;
                        }
                        cnt49++;
                    } while (true);


                    mDQUOTE(); 


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:471:4: SQUOTE ( ESC_CHAR |~ ( '\\'' | '\\\\' | '\\\\\\'' ) )+ SQUOTE
                    {
                    mSQUOTE(); 


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:471:11: ( ESC_CHAR |~ ( '\\'' | '\\\\' | '\\\\\\'' ) )+
                    int cnt50=0;
                    loop50:
                    do {
                        int alt50=3;
                        int LA50_0 = input.LA(1);

                        if ( (LA50_0=='\\') ) {
                            alt50=1;
                        }
                        else if ( ((LA50_0 >= '\u0000' && LA50_0 <= '&')||(LA50_0 >= '(' && LA50_0 <= '[')||(LA50_0 >= ']' && LA50_0 <= '\uFFFF')) ) {
                            alt50=2;
                        }


                        switch (alt50) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:471:12: ESC_CHAR
                    	    {
                    	    mESC_CHAR(); 


                    	    }
                    	    break;
                    	case 2 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:471:21: ~ ( '\\'' | '\\\\' | '\\\\\\'' )
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
                    	    if ( cnt50 >= 1 ) break loop50;
                                EarlyExitException eee =
                                    new EarlyExitException(50, input);
                                throw eee;
                        }
                        cnt50++;
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
    // $ANTLR end "PHRASE_ANYTHING"

    // $ANTLR start "REGEX"
    public final void mREGEX() throws RecognitionException {
        try {
            int _type = REGEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:475:2: ( SLASH ( ESC_CHAR |~ ( '\\\\' | '\\\\/' ) )+ SLASH )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:476:2: SLASH ( ESC_CHAR |~ ( '\\\\' | '\\\\/' ) )+ SLASH
            {
            mSLASH(); 


            // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:476:8: ( ESC_CHAR |~ ( '\\\\' | '\\\\/' ) )+
            int cnt52=0;
            loop52:
            do {
                int alt52=3;
                int LA52_0 = input.LA(1);

                if ( (LA52_0=='/') ) {
                    int LA52_1 = input.LA(2);

                    if ( ((LA52_1 >= '\u0000' && LA52_1 <= '\uFFFF')) ) {
                        alt52=2;
                    }


                }
                else if ( (LA52_0=='\\') ) {
                    alt52=1;
                }
                else if ( ((LA52_0 >= '\u0000' && LA52_0 <= '.')||(LA52_0 >= '0' && LA52_0 <= '[')||(LA52_0 >= ']' && LA52_0 <= '\uFFFF')) ) {
                    alt52=2;
                }


                switch (alt52) {
            	case 1 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:476:9: ESC_CHAR
            	    {
            	    mESC_CHAR(); 


            	    }
            	    break;
            	case 2 :
            	    // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:476:18: ~ ( '\\\\' | '\\\\/' )
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
            	    if ( cnt52 >= 1 ) break loop52;
                        EarlyExitException eee =
                            new EarlyExitException(52, input);
                        throw eee;
                }
                cnt52++;
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

    public void mTokens() throws RecognitionException {
        // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:8: ( SECOND_ORDER_OP | IDENTIFIER | SLASH | LPAREN | RPAREN | LBRACK | RBRACK | COLON | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | BAR | TO | AND | OR | NOT | NEAR | WS | NUMBER | DATE_TOKEN | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING | REGEX )
        int alt53=30;
        alt53 = dfa53.predict(input);
        switch (alt53) {
            case 1 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:10: SECOND_ORDER_OP
                {
                mSECOND_ORDER_OP(); 


                }
                break;
            case 2 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:26: IDENTIFIER
                {
                mIDENTIFIER(); 


                }
                break;
            case 3 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:37: SLASH
                {
                mSLASH(); 


                }
                break;
            case 4 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:43: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 5 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:50: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 6 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:57: LBRACK
                {
                mLBRACK(); 


                }
                break;
            case 7 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:64: RBRACK
                {
                mRBRACK(); 


                }
                break;
            case 8 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:71: COLON
                {
                mCOLON(); 


                }
                break;
            case 9 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:77: STAR
                {
                mSTAR(); 


                }
                break;
            case 10 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:82: QMARK
                {
                mQMARK(); 


                }
                break;
            case 11 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:88: LCURLY
                {
                mLCURLY(); 


                }
                break;
            case 12 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:95: RCURLY
                {
                mRCURLY(); 


                }
                break;
            case 13 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:102: CARAT
                {
                mCARAT(); 


                }
                break;
            case 14 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:108: TILDE
                {
                mTILDE(); 


                }
                break;
            case 15 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:114: DQUOTE
                {
                mDQUOTE(); 


                }
                break;
            case 16 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:121: SQUOTE
                {
                mSQUOTE(); 


                }
                break;
            case 17 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:128: BAR
                {
                mBAR(); 


                }
                break;
            case 18 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:132: TO
                {
                mTO(); 


                }
                break;
            case 19 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:135: AND
                {
                mAND(); 


                }
                break;
            case 20 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:139: OR
                {
                mOR(); 


                }
                break;
            case 21 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:142: NOT
                {
                mNOT(); 


                }
                break;
            case 22 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:146: NEAR
                {
                mNEAR(); 


                }
                break;
            case 23 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:151: WS
                {
                mWS(); 


                }
                break;
            case 24 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:154: NUMBER
                {
                mNUMBER(); 


                }
                break;
            case 25 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:161: DATE_TOKEN
                {
                mDATE_TOKEN(); 


                }
                break;
            case 26 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:172: TERM_NORMAL
                {
                mTERM_NORMAL(); 


                }
                break;
            case 27 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:184: TERM_TRUNCATED
                {
                mTERM_TRUNCATED(); 


                }
                break;
            case 28 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:199: PHRASE
                {
                mPHRASE(); 


                }
                break;
            case 29 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:206: PHRASE_ANYTHING
                {
                mPHRASE_ANYTHING(); 


                }
                break;
            case 30 :
                // /dvt/workspace/montysolr/contrib/invenio/grammars/Invenio.g:1:222: REGEX
                {
                mREGEX(); 


                }
                break;

        }

    }


    protected DFA45 dfa45 = new DFA45(this);
    protected DFA37 dfa37 = new DFA37(this);
    protected DFA41 dfa41 = new DFA41(this);
    protected DFA53 dfa53 = new DFA53(this);
    static final String DFA45_eotS =
        "\4\uffff\1\13\1\uffff\5\13\2\uffff\1\13";
    static final String DFA45_eofS =
        "\16\uffff";
    static final String DFA45_minS =
        "\3\0\1\uffff\7\0\2\uffff\1\0";
    static final String DFA45_maxS =
        "\3\uffff\1\uffff\7\uffff\2\uffff\1\uffff";
    static final String DFA45_acceptS =
        "\3\uffff\1\2\7\uffff\1\3\1\1\1\uffff";
    static final String DFA45_specialS =
        "\1\4\1\6\1\7\1\uffff\1\0\1\10\1\5\1\12\1\11\1\1\1\2\2\uffff\1\3}>";
    static final String[] DFA45_transitionS = {
            "\11\3\2\uffff\2\3\1\uffff\22\3\4\uffff\3\3\3\uffff\1\1\1\uffff"+
            "\1\3\1\uffff\1\3\1\uffff\12\3\1\uffff\4\3\1\2\33\3\1\uffff\1"+
            "\3\2\uffff\34\3\4\uffff\u2f81\3\1\uffff\13\3\1\uffff\ucff3\3",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\10\3\4\1\12\3\uffff"+
            "\1\7\1\4\1\6\1\4\1\11\12\4\1\uffff\4\4\1\uffff\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\4\uffff\u2f81\4\1\uffff\13\4\1\uffff\ucff3"+
            "\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\10\3\4\1\12\3\uffff"+
            "\1\7\1\4\1\6\1\4\1\11\12\4\1\uffff\4\4\1\2\33\4\1\uffff\1\5"+
            "\2\uffff\34\4\4\uffff\u2f81\4\1\uffff\13\4\1\uffff\ucff3\4",
            "",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\10\3\4\1\12\2\uffff"+
            "\1\14\1\7\1\4\1\6\1\4\1\11\12\4\1\uffff\4\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\4\uffff\u2f81\4\1\uffff\13\4\1\uffff\ucff3"+
            "\4",
            "\0\15",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\10\3\4\1\12\2\uffff"+
            "\1\14\1\7\1\4\1\6\1\4\1\11\12\4\1\uffff\4\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\4\uffff\u2f81\4\1\uffff\13\4\1\uffff\ucff3"+
            "\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\10\3\4\1\12\2\uffff"+
            "\1\14\1\7\1\4\1\6\1\4\1\11\12\4\1\uffff\4\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\4\uffff\u2f81\4\1\uffff\13\4\1\uffff\ucff3"+
            "\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\10\3\4\1\12\2\uffff"+
            "\1\14\1\7\1\4\1\6\1\4\1\11\12\4\1\uffff\4\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\4\uffff\u2f81\4\1\uffff\13\4\1\uffff\ucff3"+
            "\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\10\3\4\1\12\2\uffff"+
            "\1\14\1\7\1\4\1\6\1\4\1\11\12\4\1\uffff\4\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\4\uffff\u2f81\4\1\uffff\13\4\1\uffff\ucff3"+
            "\4",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\10\3\4\1\12\2\uffff"+
            "\1\14\1\7\1\4\1\6\1\4\1\11\12\4\1\uffff\4\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\4\uffff\u2f81\4\1\uffff\13\4\1\uffff\ucff3"+
            "\4",
            "",
            "",
            "\11\4\2\uffff\2\4\1\uffff\22\4\3\uffff\1\10\3\4\1\12\2\uffff"+
            "\1\14\1\7\1\4\1\6\1\4\1\11\12\4\1\uffff\4\4\1\14\33\4\1\uffff"+
            "\1\5\2\uffff\34\4\4\uffff\u2f81\4\1\uffff\13\4\1\uffff\ucff3"+
            "\4"
    };

    static final short[] DFA45_eot = DFA.unpackEncodedString(DFA45_eotS);
    static final short[] DFA45_eof = DFA.unpackEncodedString(DFA45_eofS);
    static final char[] DFA45_min = DFA.unpackEncodedStringToUnsignedChars(DFA45_minS);
    static final char[] DFA45_max = DFA.unpackEncodedStringToUnsignedChars(DFA45_maxS);
    static final short[] DFA45_accept = DFA.unpackEncodedString(DFA45_acceptS);
    static final short[] DFA45_special = DFA.unpackEncodedString(DFA45_specialS);
    static final short[][] DFA45_transition;

    static {
        int numStates = DFA45_transitionS.length;
        DFA45_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA45_transition[i] = DFA.unpackEncodedString(DFA45_transitionS[i]);
        }
    }

    class DFA45 extends DFA {

        public DFA45(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 45;
            this.eot = DFA45_eot;
            this.eof = DFA45_eof;
            this.min = DFA45_min;
            this.max = DFA45_max;
            this.accept = DFA45_accept;
            this.special = DFA45_special;
            this.transition = DFA45_transition;
        }
        public String getDescription() {
            return "456:1: TERM_TRUNCATED : ( ( STAR | QMARK ) ( ( TERM_CHAR )+ ( QMARK | STAR ) )+ ( TERM_CHAR )* | TERM_START_CHAR ( ( TERM_CHAR )* ( QMARK | STAR ) )+ ( TERM_CHAR )* | ( STAR | QMARK ) ( TERM_CHAR )+ );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA45_4 = input.LA(1);

                        s = -1;
                        if ( (LA45_4=='*'||LA45_4=='?') ) {s = 12;}

                        else if ( ((LA45_4 >= '\u0000' && LA45_4 <= '\b')||(LA45_4 >= '\u000B' && LA45_4 <= '\f')||(LA45_4 >= '\u000E' && LA45_4 <= '\u001F')||(LA45_4 >= '$' && LA45_4 <= '&')||LA45_4==','||LA45_4=='.'||(LA45_4 >= '0' && LA45_4 <= '9')||(LA45_4 >= ';' && LA45_4 <= '>')||(LA45_4 >= '@' && LA45_4 <= 'Z')||(LA45_4 >= '_' && LA45_4 <= 'z')||(LA45_4 >= '\u007F' && LA45_4 <= '\u2FFF')||(LA45_4 >= '\u3001' && LA45_4 <= '\u300B')||(LA45_4 >= '\u300D' && LA45_4 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA45_4=='\\') ) {s = 5;}

                        else if ( (LA45_4=='-') ) {s = 6;}

                        else if ( (LA45_4=='+') ) {s = 7;}

                        else if ( (LA45_4=='#') ) {s = 8;}

                        else if ( (LA45_4=='/') ) {s = 9;}

                        else if ( (LA45_4=='\'') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA45_9 = input.LA(1);

                        s = -1;
                        if ( (LA45_9=='*'||LA45_9=='?') ) {s = 12;}

                        else if ( ((LA45_9 >= '\u0000' && LA45_9 <= '\b')||(LA45_9 >= '\u000B' && LA45_9 <= '\f')||(LA45_9 >= '\u000E' && LA45_9 <= '\u001F')||(LA45_9 >= '$' && LA45_9 <= '&')||LA45_9==','||LA45_9=='.'||(LA45_9 >= '0' && LA45_9 <= '9')||(LA45_9 >= ';' && LA45_9 <= '>')||(LA45_9 >= '@' && LA45_9 <= 'Z')||(LA45_9 >= '_' && LA45_9 <= 'z')||(LA45_9 >= '\u007F' && LA45_9 <= '\u2FFF')||(LA45_9 >= '\u3001' && LA45_9 <= '\u300B')||(LA45_9 >= '\u300D' && LA45_9 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA45_9=='\\') ) {s = 5;}

                        else if ( (LA45_9=='-') ) {s = 6;}

                        else if ( (LA45_9=='+') ) {s = 7;}

                        else if ( (LA45_9=='#') ) {s = 8;}

                        else if ( (LA45_9=='/') ) {s = 9;}

                        else if ( (LA45_9=='\'') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA45_10 = input.LA(1);

                        s = -1;
                        if ( (LA45_10=='*'||LA45_10=='?') ) {s = 12;}

                        else if ( ((LA45_10 >= '\u0000' && LA45_10 <= '\b')||(LA45_10 >= '\u000B' && LA45_10 <= '\f')||(LA45_10 >= '\u000E' && LA45_10 <= '\u001F')||(LA45_10 >= '$' && LA45_10 <= '&')||LA45_10==','||LA45_10=='.'||(LA45_10 >= '0' && LA45_10 <= '9')||(LA45_10 >= ';' && LA45_10 <= '>')||(LA45_10 >= '@' && LA45_10 <= 'Z')||(LA45_10 >= '_' && LA45_10 <= 'z')||(LA45_10 >= '\u007F' && LA45_10 <= '\u2FFF')||(LA45_10 >= '\u3001' && LA45_10 <= '\u300B')||(LA45_10 >= '\u300D' && LA45_10 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA45_10=='\\') ) {s = 5;}

                        else if ( (LA45_10=='-') ) {s = 6;}

                        else if ( (LA45_10=='+') ) {s = 7;}

                        else if ( (LA45_10=='#') ) {s = 8;}

                        else if ( (LA45_10=='/') ) {s = 9;}

                        else if ( (LA45_10=='\'') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA45_13 = input.LA(1);

                        s = -1;
                        if ( (LA45_13=='*'||LA45_13=='?') ) {s = 12;}

                        else if ( ((LA45_13 >= '\u0000' && LA45_13 <= '\b')||(LA45_13 >= '\u000B' && LA45_13 <= '\f')||(LA45_13 >= '\u000E' && LA45_13 <= '\u001F')||(LA45_13 >= '$' && LA45_13 <= '&')||LA45_13==','||LA45_13=='.'||(LA45_13 >= '0' && LA45_13 <= '9')||(LA45_13 >= ';' && LA45_13 <= '>')||(LA45_13 >= '@' && LA45_13 <= 'Z')||(LA45_13 >= '_' && LA45_13 <= 'z')||(LA45_13 >= '\u007F' && LA45_13 <= '\u2FFF')||(LA45_13 >= '\u3001' && LA45_13 <= '\u300B')||(LA45_13 >= '\u300D' && LA45_13 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA45_13=='\\') ) {s = 5;}

                        else if ( (LA45_13=='-') ) {s = 6;}

                        else if ( (LA45_13=='+') ) {s = 7;}

                        else if ( (LA45_13=='#') ) {s = 8;}

                        else if ( (LA45_13=='/') ) {s = 9;}

                        else if ( (LA45_13=='\'') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA45_0 = input.LA(1);

                        s = -1;
                        if ( (LA45_0=='*') ) {s = 1;}

                        else if ( (LA45_0=='?') ) {s = 2;}

                        else if ( ((LA45_0 >= '\u0000' && LA45_0 <= '\b')||(LA45_0 >= '\u000B' && LA45_0 <= '\f')||(LA45_0 >= '\u000E' && LA45_0 <= '\u001F')||(LA45_0 >= '$' && LA45_0 <= '&')||LA45_0==','||LA45_0=='.'||(LA45_0 >= '0' && LA45_0 <= '9')||(LA45_0 >= ';' && LA45_0 <= '>')||(LA45_0 >= '@' && LA45_0 <= 'Z')||LA45_0=='\\'||(LA45_0 >= '_' && LA45_0 <= 'z')||(LA45_0 >= '\u007F' && LA45_0 <= '\u2FFF')||(LA45_0 >= '\u3001' && LA45_0 <= '\u300B')||(LA45_0 >= '\u300D' && LA45_0 <= '\uFFFF')) ) {s = 3;}

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA45_6 = input.LA(1);

                        s = -1;
                        if ( (LA45_6=='*'||LA45_6=='?') ) {s = 12;}

                        else if ( ((LA45_6 >= '\u0000' && LA45_6 <= '\b')||(LA45_6 >= '\u000B' && LA45_6 <= '\f')||(LA45_6 >= '\u000E' && LA45_6 <= '\u001F')||(LA45_6 >= '$' && LA45_6 <= '&')||LA45_6==','||LA45_6=='.'||(LA45_6 >= '0' && LA45_6 <= '9')||(LA45_6 >= ';' && LA45_6 <= '>')||(LA45_6 >= '@' && LA45_6 <= 'Z')||(LA45_6 >= '_' && LA45_6 <= 'z')||(LA45_6 >= '\u007F' && LA45_6 <= '\u2FFF')||(LA45_6 >= '\u3001' && LA45_6 <= '\u300B')||(LA45_6 >= '\u300D' && LA45_6 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA45_6=='\\') ) {s = 5;}

                        else if ( (LA45_6=='-') ) {s = 6;}

                        else if ( (LA45_6=='+') ) {s = 7;}

                        else if ( (LA45_6=='#') ) {s = 8;}

                        else if ( (LA45_6=='/') ) {s = 9;}

                        else if ( (LA45_6=='\'') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA45_1 = input.LA(1);

                        s = -1;
                        if ( ((LA45_1 >= '\u0000' && LA45_1 <= '\b')||(LA45_1 >= '\u000B' && LA45_1 <= '\f')||(LA45_1 >= '\u000E' && LA45_1 <= '\u001F')||(LA45_1 >= '$' && LA45_1 <= '&')||LA45_1==','||LA45_1=='.'||(LA45_1 >= '0' && LA45_1 <= '9')||(LA45_1 >= ';' && LA45_1 <= '>')||(LA45_1 >= '@' && LA45_1 <= 'Z')||(LA45_1 >= '_' && LA45_1 <= 'z')||(LA45_1 >= '\u007F' && LA45_1 <= '\u2FFF')||(LA45_1 >= '\u3001' && LA45_1 <= '\u300B')||(LA45_1 >= '\u300D' && LA45_1 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA45_1=='\\') ) {s = 5;}

                        else if ( (LA45_1=='-') ) {s = 6;}

                        else if ( (LA45_1=='+') ) {s = 7;}

                        else if ( (LA45_1=='#') ) {s = 8;}

                        else if ( (LA45_1=='/') ) {s = 9;}

                        else if ( (LA45_1=='\'') ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA45_2 = input.LA(1);

                        s = -1;
                        if ( ((LA45_2 >= '\u0000' && LA45_2 <= '\b')||(LA45_2 >= '\u000B' && LA45_2 <= '\f')||(LA45_2 >= '\u000E' && LA45_2 <= '\u001F')||(LA45_2 >= '$' && LA45_2 <= '&')||LA45_2==','||LA45_2=='.'||(LA45_2 >= '0' && LA45_2 <= '9')||(LA45_2 >= ';' && LA45_2 <= '>')||(LA45_2 >= '@' && LA45_2 <= 'Z')||(LA45_2 >= '_' && LA45_2 <= 'z')||(LA45_2 >= '\u007F' && LA45_2 <= '\u2FFF')||(LA45_2 >= '\u3001' && LA45_2 <= '\u300B')||(LA45_2 >= '\u300D' && LA45_2 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA45_2=='\\') ) {s = 5;}

                        else if ( (LA45_2=='-') ) {s = 6;}

                        else if ( (LA45_2=='+') ) {s = 7;}

                        else if ( (LA45_2=='#') ) {s = 8;}

                        else if ( (LA45_2=='/') ) {s = 9;}

                        else if ( (LA45_2=='\'') ) {s = 10;}

                        else if ( (LA45_2=='?') ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA45_5 = input.LA(1);

                        s = -1;
                        if ( ((LA45_5 >= '\u0000' && LA45_5 <= '\uFFFF')) ) {s = 13;}

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA45_8 = input.LA(1);

                        s = -1;
                        if ( (LA45_8=='*'||LA45_8=='?') ) {s = 12;}

                        else if ( ((LA45_8 >= '\u0000' && LA45_8 <= '\b')||(LA45_8 >= '\u000B' && LA45_8 <= '\f')||(LA45_8 >= '\u000E' && LA45_8 <= '\u001F')||(LA45_8 >= '$' && LA45_8 <= '&')||LA45_8==','||LA45_8=='.'||(LA45_8 >= '0' && LA45_8 <= '9')||(LA45_8 >= ';' && LA45_8 <= '>')||(LA45_8 >= '@' && LA45_8 <= 'Z')||(LA45_8 >= '_' && LA45_8 <= 'z')||(LA45_8 >= '\u007F' && LA45_8 <= '\u2FFF')||(LA45_8 >= '\u3001' && LA45_8 <= '\u300B')||(LA45_8 >= '\u300D' && LA45_8 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA45_8=='\\') ) {s = 5;}

                        else if ( (LA45_8=='-') ) {s = 6;}

                        else if ( (LA45_8=='+') ) {s = 7;}

                        else if ( (LA45_8=='#') ) {s = 8;}

                        else if ( (LA45_8=='/') ) {s = 9;}

                        else if ( (LA45_8=='\'') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA45_7 = input.LA(1);

                        s = -1;
                        if ( (LA45_7=='*'||LA45_7=='?') ) {s = 12;}

                        else if ( ((LA45_7 >= '\u0000' && LA45_7 <= '\b')||(LA45_7 >= '\u000B' && LA45_7 <= '\f')||(LA45_7 >= '\u000E' && LA45_7 <= '\u001F')||(LA45_7 >= '$' && LA45_7 <= '&')||LA45_7==','||LA45_7=='.'||(LA45_7 >= '0' && LA45_7 <= '9')||(LA45_7 >= ';' && LA45_7 <= '>')||(LA45_7 >= '@' && LA45_7 <= 'Z')||(LA45_7 >= '_' && LA45_7 <= 'z')||(LA45_7 >= '\u007F' && LA45_7 <= '\u2FFF')||(LA45_7 >= '\u3001' && LA45_7 <= '\u300B')||(LA45_7 >= '\u300D' && LA45_7 <= '\uFFFF')) ) {s = 4;}

                        else if ( (LA45_7=='\\') ) {s = 5;}

                        else if ( (LA45_7=='-') ) {s = 6;}

                        else if ( (LA45_7=='+') ) {s = 7;}

                        else if ( (LA45_7=='#') ) {s = 8;}

                        else if ( (LA45_7=='/') ) {s = 9;}

                        else if ( (LA45_7=='\'') ) {s = 10;}

                        else s = 11;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 45, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA37_eotS =
        "\2\10\1\uffff\5\10\2\uffff\1\10";
    static final String DFA37_eofS =
        "\13\uffff";
    static final String DFA37_minS =
        "\10\0\2\uffff\1\0";
    static final String DFA37_maxS =
        "\10\uffff\2\uffff\1\uffff";
    static final String DFA37_acceptS =
        "\10\uffff\1\2\1\1\1\uffff";
    static final String DFA37_specialS =
        "\1\3\1\1\1\5\1\7\1\4\1\2\1\0\1\10\2\uffff\1\6}>";
    static final String[] DFA37_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\3\uffff\1"+
            "\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\uffff\33\1\1\uffff\1\2"+
            "\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\0\12",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1"
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
            return "()+ loopback of 457:15: ( ( TERM_CHAR )+ ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA37_6 = input.LA(1);

                        s = -1;
                        if ( ((LA37_6 >= '\u0000' && LA37_6 <= '\b')||(LA37_6 >= '\u000B' && LA37_6 <= '\f')||(LA37_6 >= '\u000E' && LA37_6 <= '\u001F')||(LA37_6 >= '$' && LA37_6 <= '&')||LA37_6==','||LA37_6=='.'||(LA37_6 >= '0' && LA37_6 <= '9')||(LA37_6 >= ';' && LA37_6 <= '>')||(LA37_6 >= '@' && LA37_6 <= 'Z')||(LA37_6 >= '_' && LA37_6 <= 'z')||(LA37_6 >= '\u007F' && LA37_6 <= '\u2FFF')||(LA37_6 >= '\u3001' && LA37_6 <= '\u300B')||(LA37_6 >= '\u300D' && LA37_6 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA37_6=='\\') ) {s = 2;}

                        else if ( (LA37_6=='-') ) {s = 3;}

                        else if ( (LA37_6=='+') ) {s = 4;}

                        else if ( (LA37_6=='#') ) {s = 5;}

                        else if ( (LA37_6=='/') ) {s = 6;}

                        else if ( (LA37_6=='\'') ) {s = 7;}

                        else if ( (LA37_6=='*'||LA37_6=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA37_1 = input.LA(1);

                        s = -1;
                        if ( ((LA37_1 >= '\u0000' && LA37_1 <= '\b')||(LA37_1 >= '\u000B' && LA37_1 <= '\f')||(LA37_1 >= '\u000E' && LA37_1 <= '\u001F')||(LA37_1 >= '$' && LA37_1 <= '&')||LA37_1==','||LA37_1=='.'||(LA37_1 >= '0' && LA37_1 <= '9')||(LA37_1 >= ';' && LA37_1 <= '>')||(LA37_1 >= '@' && LA37_1 <= 'Z')||(LA37_1 >= '_' && LA37_1 <= 'z')||(LA37_1 >= '\u007F' && LA37_1 <= '\u2FFF')||(LA37_1 >= '\u3001' && LA37_1 <= '\u300B')||(LA37_1 >= '\u300D' && LA37_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA37_1=='\\') ) {s = 2;}

                        else if ( (LA37_1=='-') ) {s = 3;}

                        else if ( (LA37_1=='+') ) {s = 4;}

                        else if ( (LA37_1=='#') ) {s = 5;}

                        else if ( (LA37_1=='/') ) {s = 6;}

                        else if ( (LA37_1=='\'') ) {s = 7;}

                        else if ( (LA37_1=='*'||LA37_1=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA37_5 = input.LA(1);

                        s = -1;
                        if ( ((LA37_5 >= '\u0000' && LA37_5 <= '\b')||(LA37_5 >= '\u000B' && LA37_5 <= '\f')||(LA37_5 >= '\u000E' && LA37_5 <= '\u001F')||(LA37_5 >= '$' && LA37_5 <= '&')||LA37_5==','||LA37_5=='.'||(LA37_5 >= '0' && LA37_5 <= '9')||(LA37_5 >= ';' && LA37_5 <= '>')||(LA37_5 >= '@' && LA37_5 <= 'Z')||(LA37_5 >= '_' && LA37_5 <= 'z')||(LA37_5 >= '\u007F' && LA37_5 <= '\u2FFF')||(LA37_5 >= '\u3001' && LA37_5 <= '\u300B')||(LA37_5 >= '\u300D' && LA37_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA37_5=='\\') ) {s = 2;}

                        else if ( (LA37_5=='-') ) {s = 3;}

                        else if ( (LA37_5=='+') ) {s = 4;}

                        else if ( (LA37_5=='#') ) {s = 5;}

                        else if ( (LA37_5=='/') ) {s = 6;}

                        else if ( (LA37_5=='\'') ) {s = 7;}

                        else if ( (LA37_5=='*'||LA37_5=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA37_0 = input.LA(1);

                        s = -1;
                        if ( ((LA37_0 >= '\u0000' && LA37_0 <= '\b')||(LA37_0 >= '\u000B' && LA37_0 <= '\f')||(LA37_0 >= '\u000E' && LA37_0 <= '\u001F')||(LA37_0 >= '$' && LA37_0 <= '&')||LA37_0==','||LA37_0=='.'||(LA37_0 >= '0' && LA37_0 <= '9')||(LA37_0 >= ';' && LA37_0 <= '>')||(LA37_0 >= '@' && LA37_0 <= 'Z')||(LA37_0 >= '_' && LA37_0 <= 'z')||(LA37_0 >= '\u007F' && LA37_0 <= '\u2FFF')||(LA37_0 >= '\u3001' && LA37_0 <= '\u300B')||(LA37_0 >= '\u300D' && LA37_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA37_0=='\\') ) {s = 2;}

                        else if ( (LA37_0=='-') ) {s = 3;}

                        else if ( (LA37_0=='+') ) {s = 4;}

                        else if ( (LA37_0=='#') ) {s = 5;}

                        else if ( (LA37_0=='/') ) {s = 6;}

                        else if ( (LA37_0=='\'') ) {s = 7;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA37_4 = input.LA(1);

                        s = -1;
                        if ( ((LA37_4 >= '\u0000' && LA37_4 <= '\b')||(LA37_4 >= '\u000B' && LA37_4 <= '\f')||(LA37_4 >= '\u000E' && LA37_4 <= '\u001F')||(LA37_4 >= '$' && LA37_4 <= '&')||LA37_4==','||LA37_4=='.'||(LA37_4 >= '0' && LA37_4 <= '9')||(LA37_4 >= ';' && LA37_4 <= '>')||(LA37_4 >= '@' && LA37_4 <= 'Z')||(LA37_4 >= '_' && LA37_4 <= 'z')||(LA37_4 >= '\u007F' && LA37_4 <= '\u2FFF')||(LA37_4 >= '\u3001' && LA37_4 <= '\u300B')||(LA37_4 >= '\u300D' && LA37_4 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA37_4=='\\') ) {s = 2;}

                        else if ( (LA37_4=='-') ) {s = 3;}

                        else if ( (LA37_4=='+') ) {s = 4;}

                        else if ( (LA37_4=='#') ) {s = 5;}

                        else if ( (LA37_4=='/') ) {s = 6;}

                        else if ( (LA37_4=='\'') ) {s = 7;}

                        else if ( (LA37_4=='*'||LA37_4=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA37_2 = input.LA(1);

                        s = -1;
                        if ( ((LA37_2 >= '\u0000' && LA37_2 <= '\uFFFF')) ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA37_10 = input.LA(1);

                        s = -1;
                        if ( ((LA37_10 >= '\u0000' && LA37_10 <= '\b')||(LA37_10 >= '\u000B' && LA37_10 <= '\f')||(LA37_10 >= '\u000E' && LA37_10 <= '\u001F')||(LA37_10 >= '$' && LA37_10 <= '&')||LA37_10==','||LA37_10=='.'||(LA37_10 >= '0' && LA37_10 <= '9')||(LA37_10 >= ';' && LA37_10 <= '>')||(LA37_10 >= '@' && LA37_10 <= 'Z')||(LA37_10 >= '_' && LA37_10 <= 'z')||(LA37_10 >= '\u007F' && LA37_10 <= '\u2FFF')||(LA37_10 >= '\u3001' && LA37_10 <= '\u300B')||(LA37_10 >= '\u300D' && LA37_10 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA37_10=='\\') ) {s = 2;}

                        else if ( (LA37_10=='-') ) {s = 3;}

                        else if ( (LA37_10=='+') ) {s = 4;}

                        else if ( (LA37_10=='#') ) {s = 5;}

                        else if ( (LA37_10=='/') ) {s = 6;}

                        else if ( (LA37_10=='\'') ) {s = 7;}

                        else if ( (LA37_10=='*'||LA37_10=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA37_3 = input.LA(1);

                        s = -1;
                        if ( ((LA37_3 >= '\u0000' && LA37_3 <= '\b')||(LA37_3 >= '\u000B' && LA37_3 <= '\f')||(LA37_3 >= '\u000E' && LA37_3 <= '\u001F')||(LA37_3 >= '$' && LA37_3 <= '&')||LA37_3==','||LA37_3=='.'||(LA37_3 >= '0' && LA37_3 <= '9')||(LA37_3 >= ';' && LA37_3 <= '>')||(LA37_3 >= '@' && LA37_3 <= 'Z')||(LA37_3 >= '_' && LA37_3 <= 'z')||(LA37_3 >= '\u007F' && LA37_3 <= '\u2FFF')||(LA37_3 >= '\u3001' && LA37_3 <= '\u300B')||(LA37_3 >= '\u300D' && LA37_3 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA37_3=='\\') ) {s = 2;}

                        else if ( (LA37_3=='-') ) {s = 3;}

                        else if ( (LA37_3=='+') ) {s = 4;}

                        else if ( (LA37_3=='#') ) {s = 5;}

                        else if ( (LA37_3=='/') ) {s = 6;}

                        else if ( (LA37_3=='\'') ) {s = 7;}

                        else if ( (LA37_3=='*'||LA37_3=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA37_7 = input.LA(1);

                        s = -1;
                        if ( ((LA37_7 >= '\u0000' && LA37_7 <= '\b')||(LA37_7 >= '\u000B' && LA37_7 <= '\f')||(LA37_7 >= '\u000E' && LA37_7 <= '\u001F')||(LA37_7 >= '$' && LA37_7 <= '&')||LA37_7==','||LA37_7=='.'||(LA37_7 >= '0' && LA37_7 <= '9')||(LA37_7 >= ';' && LA37_7 <= '>')||(LA37_7 >= '@' && LA37_7 <= 'Z')||(LA37_7 >= '_' && LA37_7 <= 'z')||(LA37_7 >= '\u007F' && LA37_7 <= '\u2FFF')||(LA37_7 >= '\u3001' && LA37_7 <= '\u300B')||(LA37_7 >= '\u300D' && LA37_7 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA37_7=='\\') ) {s = 2;}

                        else if ( (LA37_7=='-') ) {s = 3;}

                        else if ( (LA37_7=='+') ) {s = 4;}

                        else if ( (LA37_7=='#') ) {s = 5;}

                        else if ( (LA37_7=='/') ) {s = 6;}

                        else if ( (LA37_7=='\'') ) {s = 7;}

                        else if ( (LA37_7=='*'||LA37_7=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 37, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA41_eotS =
        "\2\10\1\uffff\5\10\2\uffff\1\10";
    static final String DFA41_eofS =
        "\13\uffff";
    static final String DFA41_minS =
        "\10\0\2\uffff\1\0";
    static final String DFA41_maxS =
        "\10\uffff\2\uffff\1\uffff";
    static final String DFA41_acceptS =
        "\10\uffff\1\2\1\1\1\uffff";
    static final String DFA41_specialS =
        "\1\4\1\0\1\6\1\3\1\5\1\10\1\1\1\2\2\uffff\1\7}>";
    static final String[] DFA41_transitionS = {
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff\1"+
            "\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff\1"+
            "\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\0\12",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1",
            "",
            "",
            "\11\1\2\uffff\2\1\1\uffff\22\1\3\uffff\1\5\3\1\1\7\2\uffff"+
            "\1\11\1\4\1\1\1\3\1\1\1\6\12\1\1\uffff\4\1\1\11\33\1\1\uffff"+
            "\1\2\2\uffff\34\1\4\uffff\u2f81\1\1\uffff\13\1\1\uffff\ucff3"+
            "\1"
    };

    static final short[] DFA41_eot = DFA.unpackEncodedString(DFA41_eotS);
    static final short[] DFA41_eof = DFA.unpackEncodedString(DFA41_eofS);
    static final char[] DFA41_min = DFA.unpackEncodedStringToUnsignedChars(DFA41_minS);
    static final char[] DFA41_max = DFA.unpackEncodedStringToUnsignedChars(DFA41_maxS);
    static final short[] DFA41_accept = DFA.unpackEncodedString(DFA41_acceptS);
    static final short[] DFA41_special = DFA.unpackEncodedString(DFA41_specialS);
    static final short[][] DFA41_transition;

    static {
        int numStates = DFA41_transitionS.length;
        DFA41_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA41_transition[i] = DFA.unpackEncodedString(DFA41_transitionS[i]);
        }
    }

    class DFA41 extends DFA {

        public DFA41(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 41;
            this.eot = DFA41_eot;
            this.eof = DFA41_eof;
            this.min = DFA41_min;
            this.max = DFA41_max;
            this.accept = DFA41_accept;
            this.special = DFA41_special;
            this.transition = DFA41_transition;
        }
        public String getDescription() {
            return "()+ loopback of 458:20: ( ( TERM_CHAR )* ( QMARK | STAR ) )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA41_1 = input.LA(1);

                        s = -1;
                        if ( ((LA41_1 >= '\u0000' && LA41_1 <= '\b')||(LA41_1 >= '\u000B' && LA41_1 <= '\f')||(LA41_1 >= '\u000E' && LA41_1 <= '\u001F')||(LA41_1 >= '$' && LA41_1 <= '&')||LA41_1==','||LA41_1=='.'||(LA41_1 >= '0' && LA41_1 <= '9')||(LA41_1 >= ';' && LA41_1 <= '>')||(LA41_1 >= '@' && LA41_1 <= 'Z')||(LA41_1 >= '_' && LA41_1 <= 'z')||(LA41_1 >= '\u007F' && LA41_1 <= '\u2FFF')||(LA41_1 >= '\u3001' && LA41_1 <= '\u300B')||(LA41_1 >= '\u300D' && LA41_1 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA41_1=='\\') ) {s = 2;}

                        else if ( (LA41_1=='-') ) {s = 3;}

                        else if ( (LA41_1=='+') ) {s = 4;}

                        else if ( (LA41_1=='#') ) {s = 5;}

                        else if ( (LA41_1=='/') ) {s = 6;}

                        else if ( (LA41_1=='\'') ) {s = 7;}

                        else if ( (LA41_1=='*'||LA41_1=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA41_6 = input.LA(1);

                        s = -1;
                        if ( ((LA41_6 >= '\u0000' && LA41_6 <= '\b')||(LA41_6 >= '\u000B' && LA41_6 <= '\f')||(LA41_6 >= '\u000E' && LA41_6 <= '\u001F')||(LA41_6 >= '$' && LA41_6 <= '&')||LA41_6==','||LA41_6=='.'||(LA41_6 >= '0' && LA41_6 <= '9')||(LA41_6 >= ';' && LA41_6 <= '>')||(LA41_6 >= '@' && LA41_6 <= 'Z')||(LA41_6 >= '_' && LA41_6 <= 'z')||(LA41_6 >= '\u007F' && LA41_6 <= '\u2FFF')||(LA41_6 >= '\u3001' && LA41_6 <= '\u300B')||(LA41_6 >= '\u300D' && LA41_6 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA41_6=='\\') ) {s = 2;}

                        else if ( (LA41_6=='-') ) {s = 3;}

                        else if ( (LA41_6=='+') ) {s = 4;}

                        else if ( (LA41_6=='#') ) {s = 5;}

                        else if ( (LA41_6=='/') ) {s = 6;}

                        else if ( (LA41_6=='\'') ) {s = 7;}

                        else if ( (LA41_6=='*'||LA41_6=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA41_7 = input.LA(1);

                        s = -1;
                        if ( ((LA41_7 >= '\u0000' && LA41_7 <= '\b')||(LA41_7 >= '\u000B' && LA41_7 <= '\f')||(LA41_7 >= '\u000E' && LA41_7 <= '\u001F')||(LA41_7 >= '$' && LA41_7 <= '&')||LA41_7==','||LA41_7=='.'||(LA41_7 >= '0' && LA41_7 <= '9')||(LA41_7 >= ';' && LA41_7 <= '>')||(LA41_7 >= '@' && LA41_7 <= 'Z')||(LA41_7 >= '_' && LA41_7 <= 'z')||(LA41_7 >= '\u007F' && LA41_7 <= '\u2FFF')||(LA41_7 >= '\u3001' && LA41_7 <= '\u300B')||(LA41_7 >= '\u300D' && LA41_7 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA41_7=='\\') ) {s = 2;}

                        else if ( (LA41_7=='-') ) {s = 3;}

                        else if ( (LA41_7=='+') ) {s = 4;}

                        else if ( (LA41_7=='#') ) {s = 5;}

                        else if ( (LA41_7=='/') ) {s = 6;}

                        else if ( (LA41_7=='\'') ) {s = 7;}

                        else if ( (LA41_7=='*'||LA41_7=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA41_3 = input.LA(1);

                        s = -1;
                        if ( ((LA41_3 >= '\u0000' && LA41_3 <= '\b')||(LA41_3 >= '\u000B' && LA41_3 <= '\f')||(LA41_3 >= '\u000E' && LA41_3 <= '\u001F')||(LA41_3 >= '$' && LA41_3 <= '&')||LA41_3==','||LA41_3=='.'||(LA41_3 >= '0' && LA41_3 <= '9')||(LA41_3 >= ';' && LA41_3 <= '>')||(LA41_3 >= '@' && LA41_3 <= 'Z')||(LA41_3 >= '_' && LA41_3 <= 'z')||(LA41_3 >= '\u007F' && LA41_3 <= '\u2FFF')||(LA41_3 >= '\u3001' && LA41_3 <= '\u300B')||(LA41_3 >= '\u300D' && LA41_3 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA41_3=='\\') ) {s = 2;}

                        else if ( (LA41_3=='-') ) {s = 3;}

                        else if ( (LA41_3=='+') ) {s = 4;}

                        else if ( (LA41_3=='#') ) {s = 5;}

                        else if ( (LA41_3=='/') ) {s = 6;}

                        else if ( (LA41_3=='\'') ) {s = 7;}

                        else if ( (LA41_3=='*'||LA41_3=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA41_0 = input.LA(1);

                        s = -1;
                        if ( ((LA41_0 >= '\u0000' && LA41_0 <= '\b')||(LA41_0 >= '\u000B' && LA41_0 <= '\f')||(LA41_0 >= '\u000E' && LA41_0 <= '\u001F')||(LA41_0 >= '$' && LA41_0 <= '&')||LA41_0==','||LA41_0=='.'||(LA41_0 >= '0' && LA41_0 <= '9')||(LA41_0 >= ';' && LA41_0 <= '>')||(LA41_0 >= '@' && LA41_0 <= 'Z')||(LA41_0 >= '_' && LA41_0 <= 'z')||(LA41_0 >= '\u007F' && LA41_0 <= '\u2FFF')||(LA41_0 >= '\u3001' && LA41_0 <= '\u300B')||(LA41_0 >= '\u300D' && LA41_0 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA41_0=='\\') ) {s = 2;}

                        else if ( (LA41_0=='-') ) {s = 3;}

                        else if ( (LA41_0=='+') ) {s = 4;}

                        else if ( (LA41_0=='#') ) {s = 5;}

                        else if ( (LA41_0=='/') ) {s = 6;}

                        else if ( (LA41_0=='\'') ) {s = 7;}

                        else if ( (LA41_0=='*'||LA41_0=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA41_4 = input.LA(1);

                        s = -1;
                        if ( ((LA41_4 >= '\u0000' && LA41_4 <= '\b')||(LA41_4 >= '\u000B' && LA41_4 <= '\f')||(LA41_4 >= '\u000E' && LA41_4 <= '\u001F')||(LA41_4 >= '$' && LA41_4 <= '&')||LA41_4==','||LA41_4=='.'||(LA41_4 >= '0' && LA41_4 <= '9')||(LA41_4 >= ';' && LA41_4 <= '>')||(LA41_4 >= '@' && LA41_4 <= 'Z')||(LA41_4 >= '_' && LA41_4 <= 'z')||(LA41_4 >= '\u007F' && LA41_4 <= '\u2FFF')||(LA41_4 >= '\u3001' && LA41_4 <= '\u300B')||(LA41_4 >= '\u300D' && LA41_4 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA41_4=='\\') ) {s = 2;}

                        else if ( (LA41_4=='-') ) {s = 3;}

                        else if ( (LA41_4=='+') ) {s = 4;}

                        else if ( (LA41_4=='#') ) {s = 5;}

                        else if ( (LA41_4=='/') ) {s = 6;}

                        else if ( (LA41_4=='\'') ) {s = 7;}

                        else if ( (LA41_4=='*'||LA41_4=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA41_2 = input.LA(1);

                        s = -1;
                        if ( ((LA41_2 >= '\u0000' && LA41_2 <= '\uFFFF')) ) {s = 10;}

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA41_10 = input.LA(1);

                        s = -1;
                        if ( ((LA41_10 >= '\u0000' && LA41_10 <= '\b')||(LA41_10 >= '\u000B' && LA41_10 <= '\f')||(LA41_10 >= '\u000E' && LA41_10 <= '\u001F')||(LA41_10 >= '$' && LA41_10 <= '&')||LA41_10==','||LA41_10=='.'||(LA41_10 >= '0' && LA41_10 <= '9')||(LA41_10 >= ';' && LA41_10 <= '>')||(LA41_10 >= '@' && LA41_10 <= 'Z')||(LA41_10 >= '_' && LA41_10 <= 'z')||(LA41_10 >= '\u007F' && LA41_10 <= '\u2FFF')||(LA41_10 >= '\u3001' && LA41_10 <= '\u300B')||(LA41_10 >= '\u300D' && LA41_10 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA41_10=='\\') ) {s = 2;}

                        else if ( (LA41_10=='-') ) {s = 3;}

                        else if ( (LA41_10=='+') ) {s = 4;}

                        else if ( (LA41_10=='#') ) {s = 5;}

                        else if ( (LA41_10=='/') ) {s = 6;}

                        else if ( (LA41_10=='\'') ) {s = 7;}

                        else if ( (LA41_10=='*'||LA41_10=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA41_5 = input.LA(1);

                        s = -1;
                        if ( ((LA41_5 >= '\u0000' && LA41_5 <= '\b')||(LA41_5 >= '\u000B' && LA41_5 <= '\f')||(LA41_5 >= '\u000E' && LA41_5 <= '\u001F')||(LA41_5 >= '$' && LA41_5 <= '&')||LA41_5==','||LA41_5=='.'||(LA41_5 >= '0' && LA41_5 <= '9')||(LA41_5 >= ';' && LA41_5 <= '>')||(LA41_5 >= '@' && LA41_5 <= 'Z')||(LA41_5 >= '_' && LA41_5 <= 'z')||(LA41_5 >= '\u007F' && LA41_5 <= '\u2FFF')||(LA41_5 >= '\u3001' && LA41_5 <= '\u300B')||(LA41_5 >= '\u300D' && LA41_5 <= '\uFFFF')) ) {s = 1;}

                        else if ( (LA41_5=='\\') ) {s = 2;}

                        else if ( (LA41_5=='-') ) {s = 3;}

                        else if ( (LA41_5=='+') ) {s = 4;}

                        else if ( (LA41_5=='#') ) {s = 5;}

                        else if ( (LA41_5=='/') ) {s = 6;}

                        else if ( (LA41_5=='\'') ) {s = 7;}

                        else if ( (LA41_5=='*'||LA41_5=='?') ) {s = 9;}

                        else s = 8;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 41, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA53_eotS =
        "\1\uffff\3\41\1\60\1\63\5\uffff\1\65\1\66\4\uffff\1\67\1\73\1\uffff"+
        "\2\41\1\27\1\uffff\1\41\1\uffff\1\103\1\uffff\1\41\1\uffff\1\41"+
        "\1\uffff\1\41\1\uffff\1\41\1\uffff\5\41\1\uffff\5\41\1\60\1\uffff"+
        "\2\41\13\uffff\1\123\1\27\1\31\2\41\1\uffff\7\41\1\27\2\60\1\41"+
        "\5\uffff\1\33\7\41\1\60\5\41\1\uffff\1\103\1\41\1\156\3\41\1\160"+
        "\1\60\3\41\1\uffff\1\41\1\uffff\1\41\1\160\1\41\1\173\1\41\1\156"+
        "\1\41\3\160\1\uffff\1\41\1\156\1\41\1\160\1\173\2\41\1\156";
    static final String DFA53_eofS =
        "\u0084\uffff";
    static final String DFA53_minS =
        "\6\0\5\uffff\2\0\4\uffff\2\0\1\uffff\3\0\1\uffff\1\0\1\uffff\1\0"+
        "\1\uffff\1\0\1\uffff\3\0\1\uffff\7\0\1\uffff\6\0\1\uffff\2\0\5\uffff"+
        "\2\0\2\uffff\7\0\1\uffff\14\0\1\uffff\1\0\2\uffff\16\0\1\uffff\13"+
        "\0\1\uffff\1\0\1\uffff\12\0\1\uffff\10\0";
    static final String DFA53_maxS =
        "\6\uffff\5\uffff\2\uffff\4\uffff\2\uffff\1\uffff\3\uffff\1\uffff"+
        "\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff\1\uffff\3\uffff\1\uffff"+
        "\7\uffff\1\uffff\6\uffff\1\uffff\2\uffff\5\uffff\2\uffff\2\uffff"+
        "\7\uffff\1\uffff\14\uffff\1\uffff\1\uffff\2\uffff\16\uffff\1\uffff"+
        "\13\uffff\1\uffff\1\uffff\1\uffff\12\uffff\1\uffff\10\uffff";
    static final String DFA53_acceptS =
        "\6\uffff\1\4\1\5\1\6\1\7\1\10\2\uffff\1\13\1\14\1\15\1\16\2\uffff"+
        "\1\21\3\uffff\1\23\1\uffff\1\24\1\uffff\1\25\1\uffff\1\27\3\uffff"+
        "\1\32\7\uffff\1\33\6\uffff\1\30\2\uffff\1\3\1\36\1\11\1\12\1\17"+
        "\2\uffff\1\35\1\20\7\uffff\1\26\14\uffff\1\34\1\uffff\1\34\1\22"+
        "\16\uffff\1\34\13\uffff\1\1\1\uffff\1\2\12\uffff\1\31\10\uffff";
    static final String DFA53_specialS =
        "\1\106\1\56\1\103\1\112\1\10\1\105\5\uffff\1\60\1\73\4\uffff\1\100"+
        "\1\13\1\uffff\1\104\1\107\1\16\1\uffff\1\116\1\uffff\1\45\1\uffff"+
        "\1\36\1\uffff\1\114\1\122\1\17\1\uffff\1\70\1\121\1\51\1\46\1\64"+
        "\1\63\1\66\1\uffff\1\44\1\26\1\74\1\142\1\53\1\102\1\uffff\1\130"+
        "\1\136\5\uffff\1\117\1\25\2\uffff\1\120\1\37\1\33\1\57\1\62\1\125"+
        "\1\133\1\uffff\1\65\1\20\1\67\1\43\1\30\1\3\1\12\1\41\1\110\1\75"+
        "\1\101\1\143\1\uffff\1\137\2\uffff\1\71\1\131\1\21\1\55\1\27\1\1"+
        "\1\14\1\123\1\42\1\50\1\135\1\141\1\124\1\0\1\uffff\1\126\1\22\1"+
        "\115\1\32\1\76\1\77\1\52\1\6\1\134\1\23\1\15\1\uffff\1\31\1\uffff"+
        "\1\61\1\132\1\111\1\7\1\24\1\2\1\35\1\54\1\47\1\140\1\uffff\1\127"+
        "\1\40\1\34\1\72\1\113\1\4\1\5\1\11}>";
    static final String[] DFA53_transitionS = {
            "\11\36\2\35\2\36\1\35\22\36\1\35\1\uffff\1\21\1\23\2\36\1\26"+
            "\1\22\1\6\1\7\1\13\1\27\1\36\1\33\1\36\1\5\12\4\1\12\4\36\1"+
            "\14\1\36\1\25\14\36\1\34\1\30\4\36\1\24\6\36\1\10\1\37\1\11"+
            "\1\17\2\36\1\3\1\36\1\2\12\36\1\32\1\30\2\36\1\1\10\36\1\15"+
            "\1\31\1\16\1\20\u2f81\36\1\35\13\36\1\21\ucff3\36",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\6\42\1\40\25\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\12\42\1\52\5\42\1\53\13\42\4\uffff"+
            "\u2f81\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\16\42\1\55\14\42\1\uffff\1\43\2\uffff\17\42\1\55\3\42\1\54"+
            "\10\42\4\uffff\u2f81\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\61\1\56\1\62\12\57\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\0\64",
            "",
            "",
            "",
            "",
            "",
            "\11\51\2\uffff\2\51\1\uffff\22\51\3\uffff\5\51\3\uffff\17\51"+
            "\1\uffff\4\51\1\uffff\33\51\1\uffff\1\51\2\uffff\34\51\4\uffff"+
            "\u2f81\51\1\uffff\13\51\1\uffff\ucff3\51",
            "\11\51\2\uffff\2\51\1\uffff\22\51\3\uffff\5\51\3\uffff\17\51"+
            "\1\uffff\4\51\1\14\33\51\1\uffff\1\51\2\uffff\34\51\4\uffff"+
            "\u2f81\51\1\uffff\13\51\1\uffff\ucff3\51",
            "",
            "",
            "",
            "",
            "\42\71\1\uffff\7\71\1\72\24\71\1\72\34\71\1\70\u2faf\71\1\uffff"+
            "\ucff3\71",
            "\47\75\1\uffff\2\75\1\72\24\75\1\72\34\75\1\74\uffa3\75",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\17\42\1\76\13\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\16\42\1\55\14\42\1\uffff\1\43\2\uffff\17\42\1\55\14\42\4\uffff"+
            "\u2f81\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\2\42\1\77\1"+
            "\50\2\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42"+
            "\1\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\22\42\1\100\10\42\1\uffff\1\43\2\uffff\23\42\1\100\10\42\4"+
            "\uffff\u2f81\42\1\uffff\13\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\5\42\1\102\11\42\1\101\13\42\1\uffff\1\43\2\uffff\6\42\1\102"+
            "\11\42\1\101\13\42\4\uffff\u2f81\42\1\uffff\13\42\1\uffff\ucff3"+
            "\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\5\42\1\102\11\42\1\101\13\42\1\uffff\1\43\2\uffff\6\42\1\102"+
            "\11\42\1\101\13\42\4\uffff\u2f81\42\1\uffff\13\42\1\uffff\ucff3"+
            "\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\0\104",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\7\42\1\105\24\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\0\106",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\25\42\1\107\6\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\4\42\1\110\27\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\30\42\1\111\2\42\1\uffff\1\43\2\uffff\31\42\1\112\2\42\4\uffff"+
            "\u2f81\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\4\42\1\113\26\42\1\uffff\1\43\2\uffff\5\42\1\113\26\42\4\uffff"+
            "\u2f81\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\114\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\61\1\56\1\62\12\115\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\116\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\116\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "",
            "",
            "",
            "",
            "",
            "\0\117",
            "\42\71\1\120\7\71\1\72\24\71\1\72\34\71\1\70\u2faf\71\1\120"+
            "\ucff3\71",
            "",
            "",
            "\0\121",
            "\47\75\1\122\2\75\1\72\24\75\1\72\34\75\1\74\uffa3\75",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\24\42\1\124\6\42\1\uffff\1\43\2\uffff\25\42\1\124\6\42\4\uffff"+
            "\u2f81\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\1\42\1\125\31\42\1\uffff\1\43\2\uffff\2\42\1\125\31\42\4\uffff"+
            "\u2f81\42\1\uffff\13\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\6\42\1\126\25\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\6\42\1\127\25\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\12\42\1\130\21\42\4\uffff\u2f81"+
            "\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\12\42\1\131\21\42\4\uffff\u2f81"+
            "\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\12\42\1\132\21\42\4\uffff\u2f81"+
            "\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\136\1\135\1\133\12\134\1\uffff\4\42"+
            "\1\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\137\1\47\12\115\1\uffff\4\42\1"+
            "\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\136\1\135\1\141\12\140\1\uffff\4\42"+
            "\1\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\42\71\1\120\7\71\1\72\24\71\1\72\34\71\1\70\u2faf\71\1\120"+
            "\ucff3\71",
            "",
            "\47\75\1\122\2\75\1\72\24\75\1\72\34\75\1\74\uffa3\75",
            "",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\22\42\1\143\10\42\1\uffff\1\43\2\uffff\23\42\1\143\10\42\4"+
            "\uffff\u2f81\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\23\42\1\144\10\42\4\uffff\u2f81"+
            "\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\5\42\1\145\26\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\25\42\1\146\6\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\27\42\1\147\4\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\27\42\1\150\4\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\151\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\136\1\135\1\133\12\152\1\uffff\4\42"+
            "\1\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\153\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\153\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\152\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\136\1\135\1\141\12\42\1\uffff\4\42\1"+
            "\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\153\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\24\42\1\154\7\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\3\42\1\155\30\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\6\42\1\157\25\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\160\4\42\1\51\33"+
            "\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13\42"+
            "\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\160\4\42\1\51\33"+
            "\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13\42"+
            "\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\161\1\47\12\162\1\uffff\4\42\1"+
            "\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\163\12\152\1\uffff\4\42\1"+
            "\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\164\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\25\42\1\165\6\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\32\42\1\166\1\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\5\42\1\167\26\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\170\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\161\1\47\12\171\1\uffff\4\42\1"+
            "\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\172\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\174\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\20\42\1\175\13\42\4\uffff\u2f81"+
            "\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\30\42\1\176\3\42\4\uffff\u2f81\42"+
            "\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\170\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\161\1\47\12\177\1\uffff\4\42\1"+
            "\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\161\1\47\12\172\1\uffff\4\42\1"+
            "\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\u0080\1\uffff\4\42\1"+
            "\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\12\42\1\u0081\21\42\4\uffff\u2f81"+
            "\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\161\1\47\12\172\1\uffff\4\42\1"+
            "\51\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff"+
            "\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\25\42\1\u0082\6\42\4\uffff\u2f81"+
            "\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\11\42\1\u0083\22\42\4\uffff\u2f81"+
            "\42\1\uffff\13\42\1\uffff\ucff3\42",
            "\11\42\2\uffff\2\42\1\uffff\22\42\3\uffff\1\46\3\42\1\50\2"+
            "\uffff\1\51\1\45\1\42\1\44\1\42\1\47\12\42\1\uffff\4\42\1\51"+
            "\33\42\1\uffff\1\43\2\uffff\34\42\4\uffff\u2f81\42\1\uffff\13"+
            "\42\1\uffff\ucff3\42"
    };

    static final short[] DFA53_eot = DFA.unpackEncodedString(DFA53_eotS);
    static final short[] DFA53_eof = DFA.unpackEncodedString(DFA53_eofS);
    static final char[] DFA53_min = DFA.unpackEncodedStringToUnsignedChars(DFA53_minS);
    static final char[] DFA53_max = DFA.unpackEncodedStringToUnsignedChars(DFA53_maxS);
    static final short[] DFA53_accept = DFA.unpackEncodedString(DFA53_acceptS);
    static final short[] DFA53_special = DFA.unpackEncodedString(DFA53_specialS);
    static final short[][] DFA53_transition;

    static {
        int numStates = DFA53_transitionS.length;
        DFA53_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA53_transition[i] = DFA.unpackEncodedString(DFA53_transitionS[i]);
        }
    }

    class DFA53 extends DFA {

        public DFA53(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 53;
            this.eot = DFA53_eot;
            this.eof = DFA53_eof;
            this.min = DFA53_min;
            this.max = DFA53_max;
            this.accept = DFA53_accept;
            this.special = DFA53_special;
            this.transition = DFA53_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( SECOND_ORDER_OP | IDENTIFIER | SLASH | LPAREN | RPAREN | LBRACK | RBRACK | COLON | STAR | QMARK | LCURLY | RCURLY | CARAT | TILDE | DQUOTE | SQUOTE | BAR | TO | AND | OR | NOT | NEAR | WS | NUMBER | DATE_TOKEN | TERM_NORMAL | TERM_TRUNCATED | PHRASE | PHRASE_ANYTHING | REGEX );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA53_97 = input.LA(1);

                        s = -1;
                        if ( ((LA53_97 >= '0' && LA53_97 <= '9')) ) {s = 107;}

                        else if ( ((LA53_97 >= '\u0000' && LA53_97 <= '\b')||(LA53_97 >= '\u000B' && LA53_97 <= '\f')||(LA53_97 >= '\u000E' && LA53_97 <= '\u001F')||(LA53_97 >= '$' && LA53_97 <= '&')||LA53_97==','||LA53_97=='.'||(LA53_97 >= ';' && LA53_97 <= '>')||(LA53_97 >= '@' && LA53_97 <= 'Z')||(LA53_97 >= '_' && LA53_97 <= 'z')||(LA53_97 >= '\u007F' && LA53_97 <= '\u2FFF')||(LA53_97 >= '\u3001' && LA53_97 <= '\u300B')||(LA53_97 >= '\u300D' && LA53_97 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_97=='\\') ) {s = 35;}

                        else if ( (LA53_97=='-') ) {s = 36;}

                        else if ( (LA53_97=='+') ) {s = 37;}

                        else if ( (LA53_97=='#') ) {s = 38;}

                        else if ( (LA53_97=='/') ) {s = 39;}

                        else if ( (LA53_97=='\'') ) {s = 40;}

                        else if ( (LA53_97=='*'||LA53_97=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA53_89 = input.LA(1);

                        s = -1;
                        if ( (LA53_89=='v') ) {s = 103;}

                        else if ( ((LA53_89 >= '\u0000' && LA53_89 <= '\b')||(LA53_89 >= '\u000B' && LA53_89 <= '\f')||(LA53_89 >= '\u000E' && LA53_89 <= '\u001F')||(LA53_89 >= '$' && LA53_89 <= '&')||LA53_89==','||LA53_89=='.'||(LA53_89 >= '0' && LA53_89 <= '9')||(LA53_89 >= ';' && LA53_89 <= '>')||(LA53_89 >= '@' && LA53_89 <= 'Z')||(LA53_89 >= '_' && LA53_89 <= 'u')||(LA53_89 >= 'w' && LA53_89 <= 'z')||(LA53_89 >= '\u007F' && LA53_89 <= '\u2FFF')||(LA53_89 >= '\u3001' && LA53_89 <= '\u300B')||(LA53_89 >= '\u300D' && LA53_89 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_89=='\\') ) {s = 35;}

                        else if ( (LA53_89=='-') ) {s = 36;}

                        else if ( (LA53_89=='+') ) {s = 37;}

                        else if ( (LA53_89=='#') ) {s = 38;}

                        else if ( (LA53_89=='/') ) {s = 39;}

                        else if ( (LA53_89=='\'') ) {s = 40;}

                        else if ( (LA53_89=='*'||LA53_89=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA53_118 = input.LA(1);

                        s = -1;
                        if ( ((LA53_118 >= '\u0000' && LA53_118 <= '\b')||(LA53_118 >= '\u000B' && LA53_118 <= '\f')||(LA53_118 >= '\u000E' && LA53_118 <= '\u001F')||(LA53_118 >= '$' && LA53_118 <= '&')||LA53_118==','||LA53_118=='.'||(LA53_118 >= '0' && LA53_118 <= '9')||(LA53_118 >= ';' && LA53_118 <= '>')||(LA53_118 >= '@' && LA53_118 <= 'Z')||(LA53_118 >= '_' && LA53_118 <= 'z')||(LA53_118 >= '\u007F' && LA53_118 <= '\u2FFF')||(LA53_118 >= '\u3001' && LA53_118 <= '\u300B')||(LA53_118 >= '\u300D' && LA53_118 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_118=='\\') ) {s = 35;}

                        else if ( (LA53_118=='-') ) {s = 36;}

                        else if ( (LA53_118=='+') ) {s = 37;}

                        else if ( (LA53_118=='#') ) {s = 38;}

                        else if ( (LA53_118=='/') ) {s = 39;}

                        else if ( (LA53_118=='\'') ) {s = 40;}

                        else if ( (LA53_118=='*'||LA53_118=='?') ) {s = 41;}

                        else s = 110;

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA53_73 = input.LA(1);

                        s = -1;
                        if ( (LA53_73=='i') ) {s = 89;}

                        else if ( ((LA53_73 >= '\u0000' && LA53_73 <= '\b')||(LA53_73 >= '\u000B' && LA53_73 <= '\f')||(LA53_73 >= '\u000E' && LA53_73 <= '\u001F')||(LA53_73 >= '$' && LA53_73 <= '&')||LA53_73==','||LA53_73=='.'||(LA53_73 >= '0' && LA53_73 <= '9')||(LA53_73 >= ';' && LA53_73 <= '>')||(LA53_73 >= '@' && LA53_73 <= 'Z')||(LA53_73 >= '_' && LA53_73 <= 'h')||(LA53_73 >= 'j' && LA53_73 <= 'z')||(LA53_73 >= '\u007F' && LA53_73 <= '\u2FFF')||(LA53_73 >= '\u3001' && LA53_73 <= '\u300B')||(LA53_73 >= '\u300D' && LA53_73 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_73=='\\') ) {s = 35;}

                        else if ( (LA53_73=='-') ) {s = 36;}

                        else if ( (LA53_73=='+') ) {s = 37;}

                        else if ( (LA53_73=='#') ) {s = 38;}

                        else if ( (LA53_73=='/') ) {s = 39;}

                        else if ( (LA53_73=='\'') ) {s = 40;}

                        else if ( (LA53_73=='*'||LA53_73=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA53_129 = input.LA(1);

                        s = -1;
                        if ( (LA53_129=='t') ) {s = 130;}

                        else if ( ((LA53_129 >= '\u0000' && LA53_129 <= '\b')||(LA53_129 >= '\u000B' && LA53_129 <= '\f')||(LA53_129 >= '\u000E' && LA53_129 <= '\u001F')||(LA53_129 >= '$' && LA53_129 <= '&')||LA53_129==','||LA53_129=='.'||(LA53_129 >= '0' && LA53_129 <= '9')||(LA53_129 >= ';' && LA53_129 <= '>')||(LA53_129 >= '@' && LA53_129 <= 'Z')||(LA53_129 >= '_' && LA53_129 <= 's')||(LA53_129 >= 'u' && LA53_129 <= 'z')||(LA53_129 >= '\u007F' && LA53_129 <= '\u2FFF')||(LA53_129 >= '\u3001' && LA53_129 <= '\u300B')||(LA53_129 >= '\u300D' && LA53_129 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_129=='\\') ) {s = 35;}

                        else if ( (LA53_129=='-') ) {s = 36;}

                        else if ( (LA53_129=='+') ) {s = 37;}

                        else if ( (LA53_129=='#') ) {s = 38;}

                        else if ( (LA53_129=='/') ) {s = 39;}

                        else if ( (LA53_129=='\'') ) {s = 40;}

                        else if ( (LA53_129=='*'||LA53_129=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA53_130 = input.LA(1);

                        s = -1;
                        if ( (LA53_130=='h') ) {s = 131;}

                        else if ( ((LA53_130 >= '\u0000' && LA53_130 <= '\b')||(LA53_130 >= '\u000B' && LA53_130 <= '\f')||(LA53_130 >= '\u000E' && LA53_130 <= '\u001F')||(LA53_130 >= '$' && LA53_130 <= '&')||LA53_130==','||LA53_130=='.'||(LA53_130 >= '0' && LA53_130 <= '9')||(LA53_130 >= ';' && LA53_130 <= '>')||(LA53_130 >= '@' && LA53_130 <= 'Z')||(LA53_130 >= '_' && LA53_130 <= 'g')||(LA53_130 >= 'i' && LA53_130 <= 'z')||(LA53_130 >= '\u007F' && LA53_130 <= '\u2FFF')||(LA53_130 >= '\u3001' && LA53_130 <= '\u300B')||(LA53_130 >= '\u300D' && LA53_130 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_130=='\\') ) {s = 35;}

                        else if ( (LA53_130=='-') ) {s = 36;}

                        else if ( (LA53_130=='+') ) {s = 37;}

                        else if ( (LA53_130=='#') ) {s = 38;}

                        else if ( (LA53_130=='/') ) {s = 39;}

                        else if ( (LA53_130=='\'') ) {s = 40;}

                        else if ( (LA53_130=='*'||LA53_130=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA53_106 = input.LA(1);

                        s = -1;
                        if ( (LA53_106=='/') ) {s = 115;}

                        else if ( ((LA53_106 >= '0' && LA53_106 <= '9')) ) {s = 106;}

                        else if ( ((LA53_106 >= '\u0000' && LA53_106 <= '\b')||(LA53_106 >= '\u000B' && LA53_106 <= '\f')||(LA53_106 >= '\u000E' && LA53_106 <= '\u001F')||(LA53_106 >= '$' && LA53_106 <= '&')||LA53_106==','||LA53_106=='.'||(LA53_106 >= ';' && LA53_106 <= '>')||(LA53_106 >= '@' && LA53_106 <= 'Z')||(LA53_106 >= '_' && LA53_106 <= 'z')||(LA53_106 >= '\u007F' && LA53_106 <= '\u2FFF')||(LA53_106 >= '\u3001' && LA53_106 <= '\u300B')||(LA53_106 >= '\u300D' && LA53_106 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_106=='\\') ) {s = 35;}

                        else if ( (LA53_106=='-') ) {s = 36;}

                        else if ( (LA53_106=='+') ) {s = 37;}

                        else if ( (LA53_106=='#') ) {s = 38;}

                        else if ( (LA53_106=='\'') ) {s = 40;}

                        else if ( (LA53_106=='*'||LA53_106=='?') ) {s = 41;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA53_116 = input.LA(1);

                        s = -1;
                        if ( ((LA53_116 >= '0' && LA53_116 <= '9')) ) {s = 124;}

                        else if ( ((LA53_116 >= '\u0000' && LA53_116 <= '\b')||(LA53_116 >= '\u000B' && LA53_116 <= '\f')||(LA53_116 >= '\u000E' && LA53_116 <= '\u001F')||(LA53_116 >= '$' && LA53_116 <= '&')||LA53_116==','||LA53_116=='.'||(LA53_116 >= ';' && LA53_116 <= '>')||(LA53_116 >= '@' && LA53_116 <= 'Z')||(LA53_116 >= '_' && LA53_116 <= 'z')||(LA53_116 >= '\u007F' && LA53_116 <= '\u2FFF')||(LA53_116 >= '\u3001' && LA53_116 <= '\u300B')||(LA53_116 >= '\u300D' && LA53_116 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_116=='\\') ) {s = 35;}

                        else if ( (LA53_116=='-') ) {s = 36;}

                        else if ( (LA53_116=='+') ) {s = 37;}

                        else if ( (LA53_116=='#') ) {s = 38;}

                        else if ( (LA53_116=='/') ) {s = 39;}

                        else if ( (LA53_116=='\'') ) {s = 40;}

                        else if ( (LA53_116=='*'||LA53_116=='?') ) {s = 41;}

                        else s = 123;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA53_4 = input.LA(1);

                        s = -1;
                        if ( (LA53_4=='.') ) {s = 46;}

                        else if ( ((LA53_4 >= '0' && LA53_4 <= '9')) ) {s = 47;}

                        else if ( (LA53_4=='-') ) {s = 49;}

                        else if ( ((LA53_4 >= '\u0000' && LA53_4 <= '\b')||(LA53_4 >= '\u000B' && LA53_4 <= '\f')||(LA53_4 >= '\u000E' && LA53_4 <= '\u001F')||(LA53_4 >= '$' && LA53_4 <= '&')||LA53_4==','||(LA53_4 >= ';' && LA53_4 <= '>')||(LA53_4 >= '@' && LA53_4 <= 'Z')||(LA53_4 >= '_' && LA53_4 <= 'z')||(LA53_4 >= '\u007F' && LA53_4 <= '\u2FFF')||(LA53_4 >= '\u3001' && LA53_4 <= '\u300B')||(LA53_4 >= '\u300D' && LA53_4 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_4=='\\') ) {s = 35;}

                        else if ( (LA53_4=='/') ) {s = 50;}

                        else if ( (LA53_4=='+') ) {s = 37;}

                        else if ( (LA53_4=='#') ) {s = 38;}

                        else if ( (LA53_4=='\'') ) {s = 40;}

                        else if ( (LA53_4=='*'||LA53_4=='?') ) {s = 41;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA53_131 = input.LA(1);

                        s = -1;
                        if ( ((LA53_131 >= '\u0000' && LA53_131 <= '\b')||(LA53_131 >= '\u000B' && LA53_131 <= '\f')||(LA53_131 >= '\u000E' && LA53_131 <= '\u001F')||(LA53_131 >= '$' && LA53_131 <= '&')||LA53_131==','||LA53_131=='.'||(LA53_131 >= '0' && LA53_131 <= '9')||(LA53_131 >= ';' && LA53_131 <= '>')||(LA53_131 >= '@' && LA53_131 <= 'Z')||(LA53_131 >= '_' && LA53_131 <= 'z')||(LA53_131 >= '\u007F' && LA53_131 <= '\u2FFF')||(LA53_131 >= '\u3001' && LA53_131 <= '\u300B')||(LA53_131 >= '\u300D' && LA53_131 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_131=='\\') ) {s = 35;}

                        else if ( (LA53_131=='-') ) {s = 36;}

                        else if ( (LA53_131=='+') ) {s = 37;}

                        else if ( (LA53_131=='#') ) {s = 38;}

                        else if ( (LA53_131=='/') ) {s = 39;}

                        else if ( (LA53_131=='\'') ) {s = 40;}

                        else if ( (LA53_131=='*'||LA53_131=='?') ) {s = 41;}

                        else s = 110;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA53_74 = input.LA(1);

                        s = -1;
                        if ( (LA53_74=='i') ) {s = 90;}

                        else if ( ((LA53_74 >= '\u0000' && LA53_74 <= '\b')||(LA53_74 >= '\u000B' && LA53_74 <= '\f')||(LA53_74 >= '\u000E' && LA53_74 <= '\u001F')||(LA53_74 >= '$' && LA53_74 <= '&')||LA53_74==','||LA53_74=='.'||(LA53_74 >= '0' && LA53_74 <= '9')||(LA53_74 >= ';' && LA53_74 <= '>')||(LA53_74 >= '@' && LA53_74 <= 'Z')||(LA53_74 >= '_' && LA53_74 <= 'h')||(LA53_74 >= 'j' && LA53_74 <= 'z')||(LA53_74 >= '\u007F' && LA53_74 <= '\u2FFF')||(LA53_74 >= '\u3001' && LA53_74 <= '\u300B')||(LA53_74 >= '\u300D' && LA53_74 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_74=='\\') ) {s = 35;}

                        else if ( (LA53_74=='-') ) {s = 36;}

                        else if ( (LA53_74=='+') ) {s = 37;}

                        else if ( (LA53_74=='#') ) {s = 38;}

                        else if ( (LA53_74=='/') ) {s = 39;}

                        else if ( (LA53_74=='\'') ) {s = 40;}

                        else if ( (LA53_74=='*'||LA53_74=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA53_18 = input.LA(1);

                        s = -1;
                        if ( (LA53_18=='\\') ) {s = 60;}

                        else if ( ((LA53_18 >= '\u0000' && LA53_18 <= '&')||(LA53_18 >= '(' && LA53_18 <= ')')||(LA53_18 >= '+' && LA53_18 <= '>')||(LA53_18 >= '@' && LA53_18 <= '[')||(LA53_18 >= ']' && LA53_18 <= '\uFFFF')) ) {s = 61;}

                        else if ( (LA53_18=='*'||LA53_18=='?') ) {s = 58;}

                        else s = 59;

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA53_90 = input.LA(1);

                        s = -1;
                        if ( (LA53_90=='v') ) {s = 104;}

                        else if ( ((LA53_90 >= '\u0000' && LA53_90 <= '\b')||(LA53_90 >= '\u000B' && LA53_90 <= '\f')||(LA53_90 >= '\u000E' && LA53_90 <= '\u001F')||(LA53_90 >= '$' && LA53_90 <= '&')||LA53_90==','||LA53_90=='.'||(LA53_90 >= '0' && LA53_90 <= '9')||(LA53_90 >= ';' && LA53_90 <= '>')||(LA53_90 >= '@' && LA53_90 <= 'Z')||(LA53_90 >= '_' && LA53_90 <= 'u')||(LA53_90 >= 'w' && LA53_90 <= 'z')||(LA53_90 >= '\u007F' && LA53_90 <= '\u2FFF')||(LA53_90 >= '\u3001' && LA53_90 <= '\u300B')||(LA53_90 >= '\u300D' && LA53_90 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_90=='\\') ) {s = 35;}

                        else if ( (LA53_90=='-') ) {s = 36;}

                        else if ( (LA53_90=='+') ) {s = 37;}

                        else if ( (LA53_90=='#') ) {s = 38;}

                        else if ( (LA53_90=='/') ) {s = 39;}

                        else if ( (LA53_90=='\'') ) {s = 40;}

                        else if ( (LA53_90=='*'||LA53_90=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA53_109 = input.LA(1);

                        s = -1;
                        if ( (LA53_109=='y') ) {s = 118;}

                        else if ( ((LA53_109 >= '\u0000' && LA53_109 <= '\b')||(LA53_109 >= '\u000B' && LA53_109 <= '\f')||(LA53_109 >= '\u000E' && LA53_109 <= '\u001F')||(LA53_109 >= '$' && LA53_109 <= '&')||LA53_109==','||LA53_109=='.'||(LA53_109 >= '0' && LA53_109 <= '9')||(LA53_109 >= ';' && LA53_109 <= '>')||(LA53_109 >= '@' && LA53_109 <= 'Z')||(LA53_109 >= '_' && LA53_109 <= 'x')||LA53_109=='z'||(LA53_109 >= '\u007F' && LA53_109 <= '\u2FFF')||(LA53_109 >= '\u3001' && LA53_109 <= '\u300B')||(LA53_109 >= '\u300D' && LA53_109 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_109=='\\') ) {s = 35;}

                        else if ( (LA53_109=='-') ) {s = 36;}

                        else if ( (LA53_109=='+') ) {s = 37;}

                        else if ( (LA53_109=='#') ) {s = 38;}

                        else if ( (LA53_109=='/') ) {s = 39;}

                        else if ( (LA53_109=='\'') ) {s = 40;}

                        else if ( (LA53_109=='*'||LA53_109=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA53_22 = input.LA(1);

                        s = -1;
                        if ( (LA53_22=='&') ) {s = 63;}

                        else if ( ((LA53_22 >= '\u0000' && LA53_22 <= '\b')||(LA53_22 >= '\u000B' && LA53_22 <= '\f')||(LA53_22 >= '\u000E' && LA53_22 <= '\u001F')||(LA53_22 >= '$' && LA53_22 <= '%')||LA53_22==','||LA53_22=='.'||(LA53_22 >= '0' && LA53_22 <= '9')||(LA53_22 >= ';' && LA53_22 <= '>')||(LA53_22 >= '@' && LA53_22 <= 'Z')||(LA53_22 >= '_' && LA53_22 <= 'z')||(LA53_22 >= '\u007F' && LA53_22 <= '\u2FFF')||(LA53_22 >= '\u3001' && LA53_22 <= '\u300B')||(LA53_22 >= '\u300D' && LA53_22 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_22=='\\') ) {s = 35;}

                        else if ( (LA53_22=='-') ) {s = 36;}

                        else if ( (LA53_22=='+') ) {s = 37;}

                        else if ( (LA53_22=='#') ) {s = 38;}

                        else if ( (LA53_22=='/') ) {s = 39;}

                        else if ( (LA53_22=='\'') ) {s = 40;}

                        else if ( (LA53_22=='*'||LA53_22=='?') ) {s = 41;}

                        else s = 23;

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA53_32 = input.LA(1);

                        s = -1;
                        if ( (LA53_32=='f') ) {s = 69;}

                        else if ( ((LA53_32 >= '\u0000' && LA53_32 <= '\b')||(LA53_32 >= '\u000B' && LA53_32 <= '\f')||(LA53_32 >= '\u000E' && LA53_32 <= '\u001F')||(LA53_32 >= '$' && LA53_32 <= '&')||LA53_32==','||LA53_32=='.'||(LA53_32 >= '0' && LA53_32 <= '9')||(LA53_32 >= ';' && LA53_32 <= '>')||(LA53_32 >= '@' && LA53_32 <= 'Z')||(LA53_32 >= '_' && LA53_32 <= 'e')||(LA53_32 >= 'g' && LA53_32 <= 'z')||(LA53_32 >= '\u007F' && LA53_32 <= '\u2FFF')||(LA53_32 >= '\u3001' && LA53_32 <= '\u300B')||(LA53_32 >= '\u300D' && LA53_32 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_32=='\\') ) {s = 35;}

                        else if ( (LA53_32=='-') ) {s = 36;}

                        else if ( (LA53_32=='+') ) {s = 37;}

                        else if ( (LA53_32=='#') ) {s = 38;}

                        else if ( (LA53_32=='/') ) {s = 39;}

                        else if ( (LA53_32=='\'') ) {s = 40;}

                        else if ( (LA53_32=='*'||LA53_32=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA53_69 = input.LA(1);

                        s = -1;
                        if ( (LA53_69=='e') ) {s = 86;}

                        else if ( ((LA53_69 >= '\u0000' && LA53_69 <= '\b')||(LA53_69 >= '\u000B' && LA53_69 <= '\f')||(LA53_69 >= '\u000E' && LA53_69 <= '\u001F')||(LA53_69 >= '$' && LA53_69 <= '&')||LA53_69==','||LA53_69=='.'||(LA53_69 >= '0' && LA53_69 <= '9')||(LA53_69 >= ';' && LA53_69 <= '>')||(LA53_69 >= '@' && LA53_69 <= 'Z')||(LA53_69 >= '_' && LA53_69 <= 'd')||(LA53_69 >= 'f' && LA53_69 <= 'z')||(LA53_69 >= '\u007F' && LA53_69 <= '\u2FFF')||(LA53_69 >= '\u3001' && LA53_69 <= '\u300B')||(LA53_69 >= '\u300D' && LA53_69 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_69=='\\') ) {s = 35;}

                        else if ( (LA53_69=='-') ) {s = 36;}

                        else if ( (LA53_69=='+') ) {s = 37;}

                        else if ( (LA53_69=='#') ) {s = 38;}

                        else if ( (LA53_69=='/') ) {s = 39;}

                        else if ( (LA53_69=='\'') ) {s = 40;}

                        else if ( (LA53_69=='*'||LA53_69=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA53_86 = input.LA(1);

                        s = -1;
                        if ( (LA53_86=='r') ) {s = 100;}

                        else if ( ((LA53_86 >= '\u0000' && LA53_86 <= '\b')||(LA53_86 >= '\u000B' && LA53_86 <= '\f')||(LA53_86 >= '\u000E' && LA53_86 <= '\u001F')||(LA53_86 >= '$' && LA53_86 <= '&')||LA53_86==','||LA53_86=='.'||(LA53_86 >= '0' && LA53_86 <= '9')||(LA53_86 >= ';' && LA53_86 <= '>')||(LA53_86 >= '@' && LA53_86 <= 'Z')||(LA53_86 >= '_' && LA53_86 <= 'q')||(LA53_86 >= 's' && LA53_86 <= 'z')||(LA53_86 >= '\u007F' && LA53_86 <= '\u2FFF')||(LA53_86 >= '\u3001' && LA53_86 <= '\u300B')||(LA53_86 >= '\u300D' && LA53_86 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_86=='\\') ) {s = 35;}

                        else if ( (LA53_86=='-') ) {s = 36;}

                        else if ( (LA53_86=='+') ) {s = 37;}

                        else if ( (LA53_86=='#') ) {s = 38;}

                        else if ( (LA53_86=='/') ) {s = 39;}

                        else if ( (LA53_86=='\'') ) {s = 40;}

                        else if ( (LA53_86=='*'||LA53_86=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA53_100 = input.LA(1);

                        s = -1;
                        if ( (LA53_100=='s') ) {s = 108;}

                        else if ( ((LA53_100 >= '\u0000' && LA53_100 <= '\b')||(LA53_100 >= '\u000B' && LA53_100 <= '\f')||(LA53_100 >= '\u000E' && LA53_100 <= '\u001F')||(LA53_100 >= '$' && LA53_100 <= '&')||LA53_100==','||LA53_100=='.'||(LA53_100 >= '0' && LA53_100 <= '9')||(LA53_100 >= ';' && LA53_100 <= '>')||(LA53_100 >= '@' && LA53_100 <= 'Z')||(LA53_100 >= '_' && LA53_100 <= 'r')||(LA53_100 >= 't' && LA53_100 <= 'z')||(LA53_100 >= '\u007F' && LA53_100 <= '\u2FFF')||(LA53_100 >= '\u3001' && LA53_100 <= '\u300B')||(LA53_100 >= '\u300D' && LA53_100 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_100=='\\') ) {s = 35;}

                        else if ( (LA53_100=='-') ) {s = 36;}

                        else if ( (LA53_100=='+') ) {s = 37;}

                        else if ( (LA53_100=='#') ) {s = 38;}

                        else if ( (LA53_100=='/') ) {s = 39;}

                        else if ( (LA53_100=='\'') ) {s = 40;}

                        else if ( (LA53_100=='*'||LA53_100=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA53_108 = input.LA(1);

                        s = -1;
                        if ( (LA53_108=='t') ) {s = 117;}

                        else if ( ((LA53_108 >= '\u0000' && LA53_108 <= '\b')||(LA53_108 >= '\u000B' && LA53_108 <= '\f')||(LA53_108 >= '\u000E' && LA53_108 <= '\u001F')||(LA53_108 >= '$' && LA53_108 <= '&')||LA53_108==','||LA53_108=='.'||(LA53_108 >= '0' && LA53_108 <= '9')||(LA53_108 >= ';' && LA53_108 <= '>')||(LA53_108 >= '@' && LA53_108 <= 'Z')||(LA53_108 >= '_' && LA53_108 <= 's')||(LA53_108 >= 'u' && LA53_108 <= 'z')||(LA53_108 >= '\u007F' && LA53_108 <= '\u2FFF')||(LA53_108 >= '\u3001' && LA53_108 <= '\u300B')||(LA53_108 >= '\u300D' && LA53_108 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_108=='\\') ) {s = 35;}

                        else if ( (LA53_108=='-') ) {s = 36;}

                        else if ( (LA53_108=='+') ) {s = 37;}

                        else if ( (LA53_108=='#') ) {s = 38;}

                        else if ( (LA53_108=='/') ) {s = 39;}

                        else if ( (LA53_108=='\'') ) {s = 40;}

                        else if ( (LA53_108=='*'||LA53_108=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA53_117 = input.LA(1);

                        s = -1;
                        if ( (LA53_117=='o') ) {s = 125;}

                        else if ( ((LA53_117 >= '\u0000' && LA53_117 <= '\b')||(LA53_117 >= '\u000B' && LA53_117 <= '\f')||(LA53_117 >= '\u000E' && LA53_117 <= '\u001F')||(LA53_117 >= '$' && LA53_117 <= '&')||LA53_117==','||LA53_117=='.'||(LA53_117 >= '0' && LA53_117 <= '9')||(LA53_117 >= ';' && LA53_117 <= '>')||(LA53_117 >= '@' && LA53_117 <= 'Z')||(LA53_117 >= '_' && LA53_117 <= 'n')||(LA53_117 >= 'p' && LA53_117 <= 'z')||(LA53_117 >= '\u007F' && LA53_117 <= '\u2FFF')||(LA53_117 >= '\u3001' && LA53_117 <= '\u300B')||(LA53_117 >= '\u300D' && LA53_117 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_117=='\\') ) {s = 35;}

                        else if ( (LA53_117=='-') ) {s = 36;}

                        else if ( (LA53_117=='+') ) {s = 37;}

                        else if ( (LA53_117=='#') ) {s = 38;}

                        else if ( (LA53_117=='/') ) {s = 39;}

                        else if ( (LA53_117=='\'') ) {s = 40;}

                        else if ( (LA53_117=='*'||LA53_117=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA53_57 = input.LA(1);

                        s = -1;
                        if ( (LA53_57=='\"'||LA53_57=='\u300C') ) {s = 80;}

                        else if ( (LA53_57=='\\') ) {s = 56;}

                        else if ( ((LA53_57 >= '\u0000' && LA53_57 <= '!')||(LA53_57 >= '#' && LA53_57 <= ')')||(LA53_57 >= '+' && LA53_57 <= '>')||(LA53_57 >= '@' && LA53_57 <= '[')||(LA53_57 >= ']' && LA53_57 <= '\u300B')||(LA53_57 >= '\u300D' && LA53_57 <= '\uFFFF')) ) {s = 57;}

                        else if ( (LA53_57=='*'||LA53_57=='?') ) {s = 58;}

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA53_43 = input.LA(1);

                        s = -1;
                        if ( (LA53_43=='c') ) {s = 72;}

                        else if ( ((LA53_43 >= '\u0000' && LA53_43 <= '\b')||(LA53_43 >= '\u000B' && LA53_43 <= '\f')||(LA53_43 >= '\u000E' && LA53_43 <= '\u001F')||(LA53_43 >= '$' && LA53_43 <= '&')||LA53_43==','||LA53_43=='.'||(LA53_43 >= '0' && LA53_43 <= '9')||(LA53_43 >= ';' && LA53_43 <= '>')||(LA53_43 >= '@' && LA53_43 <= 'Z')||(LA53_43 >= '_' && LA53_43 <= 'b')||(LA53_43 >= 'd' && LA53_43 <= 'z')||(LA53_43 >= '\u007F' && LA53_43 <= '\u2FFF')||(LA53_43 >= '\u3001' && LA53_43 <= '\u300B')||(LA53_43 >= '\u300D' && LA53_43 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_43=='\\') ) {s = 35;}

                        else if ( (LA53_43=='-') ) {s = 36;}

                        else if ( (LA53_43=='+') ) {s = 37;}

                        else if ( (LA53_43=='#') ) {s = 38;}

                        else if ( (LA53_43=='/') ) {s = 39;}

                        else if ( (LA53_43=='\'') ) {s = 40;}

                        else if ( (LA53_43=='*'||LA53_43=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA53_88 = input.LA(1);

                        s = -1;
                        if ( (LA53_88=='t') ) {s = 102;}

                        else if ( ((LA53_88 >= '\u0000' && LA53_88 <= '\b')||(LA53_88 >= '\u000B' && LA53_88 <= '\f')||(LA53_88 >= '\u000E' && LA53_88 <= '\u001F')||(LA53_88 >= '$' && LA53_88 <= '&')||LA53_88==','||LA53_88=='.'||(LA53_88 >= '0' && LA53_88 <= '9')||(LA53_88 >= ';' && LA53_88 <= '>')||(LA53_88 >= '@' && LA53_88 <= 'Z')||(LA53_88 >= '_' && LA53_88 <= 's')||(LA53_88 >= 'u' && LA53_88 <= 'z')||(LA53_88 >= '\u007F' && LA53_88 <= '\u2FFF')||(LA53_88 >= '\u3001' && LA53_88 <= '\u300B')||(LA53_88 >= '\u300D' && LA53_88 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_88=='\\') ) {s = 35;}

                        else if ( (LA53_88=='-') ) {s = 36;}

                        else if ( (LA53_88=='+') ) {s = 37;}

                        else if ( (LA53_88=='#') ) {s = 38;}

                        else if ( (LA53_88=='/') ) {s = 39;}

                        else if ( (LA53_88=='\'') ) {s = 40;}

                        else if ( (LA53_88=='*'||LA53_88=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA53_72 = input.LA(1);

                        s = -1;
                        if ( (LA53_72=='i') ) {s = 88;}

                        else if ( ((LA53_72 >= '\u0000' && LA53_72 <= '\b')||(LA53_72 >= '\u000B' && LA53_72 <= '\f')||(LA53_72 >= '\u000E' && LA53_72 <= '\u001F')||(LA53_72 >= '$' && LA53_72 <= '&')||LA53_72==','||LA53_72=='.'||(LA53_72 >= '0' && LA53_72 <= '9')||(LA53_72 >= ';' && LA53_72 <= '>')||(LA53_72 >= '@' && LA53_72 <= 'Z')||(LA53_72 >= '_' && LA53_72 <= 'h')||(LA53_72 >= 'j' && LA53_72 <= 'z')||(LA53_72 >= '\u007F' && LA53_72 <= '\u2FFF')||(LA53_72 >= '\u3001' && LA53_72 <= '\u300B')||(LA53_72 >= '\u300D' && LA53_72 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_72=='\\') ) {s = 35;}

                        else if ( (LA53_72=='-') ) {s = 36;}

                        else if ( (LA53_72=='+') ) {s = 37;}

                        else if ( (LA53_72=='#') ) {s = 38;}

                        else if ( (LA53_72=='/') ) {s = 39;}

                        else if ( (LA53_72=='\'') ) {s = 40;}

                        else if ( (LA53_72=='*'||LA53_72=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA53_111 = input.LA(1);

                        s = -1;
                        if ( (LA53_111=='d') ) {s = 119;}

                        else if ( ((LA53_111 >= '\u0000' && LA53_111 <= '\b')||(LA53_111 >= '\u000B' && LA53_111 <= '\f')||(LA53_111 >= '\u000E' && LA53_111 <= '\u001F')||(LA53_111 >= '$' && LA53_111 <= '&')||LA53_111==','||LA53_111=='.'||(LA53_111 >= '0' && LA53_111 <= '9')||(LA53_111 >= ';' && LA53_111 <= '>')||(LA53_111 >= '@' && LA53_111 <= 'Z')||(LA53_111 >= '_' && LA53_111 <= 'c')||(LA53_111 >= 'e' && LA53_111 <= 'z')||(LA53_111 >= '\u007F' && LA53_111 <= '\u2FFF')||(LA53_111 >= '\u3001' && LA53_111 <= '\u300B')||(LA53_111 >= '\u300D' && LA53_111 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_111=='\\') ) {s = 35;}

                        else if ( (LA53_111=='-') ) {s = 36;}

                        else if ( (LA53_111=='+') ) {s = 37;}

                        else if ( (LA53_111=='#') ) {s = 38;}

                        else if ( (LA53_111=='/') ) {s = 39;}

                        else if ( (LA53_111=='\'') ) {s = 40;}

                        else if ( (LA53_111=='*'||LA53_111=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 26 : 
                        int LA53_102 = input.LA(1);

                        s = -1;
                        if ( (LA53_102=='e') ) {s = 111;}

                        else if ( ((LA53_102 >= '\u0000' && LA53_102 <= '\b')||(LA53_102 >= '\u000B' && LA53_102 <= '\f')||(LA53_102 >= '\u000E' && LA53_102 <= '\u001F')||(LA53_102 >= '$' && LA53_102 <= '&')||LA53_102==','||LA53_102=='.'||(LA53_102 >= '0' && LA53_102 <= '9')||(LA53_102 >= ';' && LA53_102 <= '>')||(LA53_102 >= '@' && LA53_102 <= 'Z')||(LA53_102 >= '_' && LA53_102 <= 'd')||(LA53_102 >= 'f' && LA53_102 <= 'z')||(LA53_102 >= '\u007F' && LA53_102 <= '\u2FFF')||(LA53_102 >= '\u3001' && LA53_102 <= '\u300B')||(LA53_102 >= '\u300D' && LA53_102 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_102=='\\') ) {s = 35;}

                        else if ( (LA53_102=='-') ) {s = 36;}

                        else if ( (LA53_102=='+') ) {s = 37;}

                        else if ( (LA53_102=='#') ) {s = 38;}

                        else if ( (LA53_102=='/') ) {s = 39;}

                        else if ( (LA53_102=='\'') ) {s = 40;}

                        else if ( (LA53_102=='*'||LA53_102=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 27 : 
                        int LA53_62 = input.LA(1);

                        s = -1;
                        if ( ((LA53_62 >= '\u0000' && LA53_62 <= '\b')||(LA53_62 >= '\u000B' && LA53_62 <= '\f')||(LA53_62 >= '\u000E' && LA53_62 <= '\u001F')||(LA53_62 >= '$' && LA53_62 <= '&')||LA53_62==','||LA53_62=='.'||(LA53_62 >= '0' && LA53_62 <= '9')||(LA53_62 >= ';' && LA53_62 <= '>')||(LA53_62 >= '@' && LA53_62 <= 'Z')||(LA53_62 >= '_' && LA53_62 <= 'z')||(LA53_62 >= '\u007F' && LA53_62 <= '\u2FFF')||(LA53_62 >= '\u3001' && LA53_62 <= '\u300B')||(LA53_62 >= '\u300D' && LA53_62 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_62=='\\') ) {s = 35;}

                        else if ( (LA53_62=='-') ) {s = 36;}

                        else if ( (LA53_62=='+') ) {s = 37;}

                        else if ( (LA53_62=='#') ) {s = 38;}

                        else if ( (LA53_62=='/') ) {s = 39;}

                        else if ( (LA53_62=='\'') ) {s = 40;}

                        else if ( (LA53_62=='*'||LA53_62=='?') ) {s = 41;}

                        else s = 83;

                        if ( s>=0 ) return s;
                        break;

                    case 28 : 
                        int LA53_126 = input.LA(1);

                        s = -1;
                        if ( (LA53_126=='i') ) {s = 129;}

                        else if ( ((LA53_126 >= '\u0000' && LA53_126 <= '\b')||(LA53_126 >= '\u000B' && LA53_126 <= '\f')||(LA53_126 >= '\u000E' && LA53_126 <= '\u001F')||(LA53_126 >= '$' && LA53_126 <= '&')||LA53_126==','||LA53_126=='.'||(LA53_126 >= '0' && LA53_126 <= '9')||(LA53_126 >= ';' && LA53_126 <= '>')||(LA53_126 >= '@' && LA53_126 <= 'Z')||(LA53_126 >= '_' && LA53_126 <= 'h')||(LA53_126 >= 'j' && LA53_126 <= 'z')||(LA53_126 >= '\u007F' && LA53_126 <= '\u2FFF')||(LA53_126 >= '\u3001' && LA53_126 <= '\u300B')||(LA53_126 >= '\u300D' && LA53_126 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_126=='\\') ) {s = 35;}

                        else if ( (LA53_126=='-') ) {s = 36;}

                        else if ( (LA53_126=='+') ) {s = 37;}

                        else if ( (LA53_126=='#') ) {s = 38;}

                        else if ( (LA53_126=='/') ) {s = 39;}

                        else if ( (LA53_126=='\'') ) {s = 40;}

                        else if ( (LA53_126=='*'||LA53_126=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 29 : 
                        int LA53_119 = input.LA(1);

                        s = -1;
                        if ( (LA53_119=='w') ) {s = 126;}

                        else if ( ((LA53_119 >= '\u0000' && LA53_119 <= '\b')||(LA53_119 >= '\u000B' && LA53_119 <= '\f')||(LA53_119 >= '\u000E' && LA53_119 <= '\u001F')||(LA53_119 >= '$' && LA53_119 <= '&')||LA53_119==','||LA53_119=='.'||(LA53_119 >= '0' && LA53_119 <= '9')||(LA53_119 >= ';' && LA53_119 <= '>')||(LA53_119 >= '@' && LA53_119 <= 'Z')||(LA53_119 >= '_' && LA53_119 <= 'v')||(LA53_119 >= 'x' && LA53_119 <= 'z')||(LA53_119 >= '\u007F' && LA53_119 <= '\u2FFF')||(LA53_119 >= '\u3001' && LA53_119 <= '\u300B')||(LA53_119 >= '\u300D' && LA53_119 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_119=='\\') ) {s = 35;}

                        else if ( (LA53_119=='-') ) {s = 36;}

                        else if ( (LA53_119=='+') ) {s = 37;}

                        else if ( (LA53_119=='#') ) {s = 38;}

                        else if ( (LA53_119=='/') ) {s = 39;}

                        else if ( (LA53_119=='\'') ) {s = 40;}

                        else if ( (LA53_119=='*'||LA53_119=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 30 : 
                        int LA53_28 = input.LA(1);

                        s = -1;
                        if ( (LA53_28=='O'||LA53_28=='o') ) {s = 65;}

                        else if ( (LA53_28=='E'||LA53_28=='e') ) {s = 66;}

                        else if ( ((LA53_28 >= '\u0000' && LA53_28 <= '\b')||(LA53_28 >= '\u000B' && LA53_28 <= '\f')||(LA53_28 >= '\u000E' && LA53_28 <= '\u001F')||(LA53_28 >= '$' && LA53_28 <= '&')||LA53_28==','||LA53_28=='.'||(LA53_28 >= '0' && LA53_28 <= '9')||(LA53_28 >= ';' && LA53_28 <= '>')||(LA53_28 >= '@' && LA53_28 <= 'D')||(LA53_28 >= 'F' && LA53_28 <= 'N')||(LA53_28 >= 'P' && LA53_28 <= 'Z')||(LA53_28 >= '_' && LA53_28 <= 'd')||(LA53_28 >= 'f' && LA53_28 <= 'n')||(LA53_28 >= 'p' && LA53_28 <= 'z')||(LA53_28 >= '\u007F' && LA53_28 <= '\u2FFF')||(LA53_28 >= '\u3001' && LA53_28 <= '\u300B')||(LA53_28 >= '\u300D' && LA53_28 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_28=='\\') ) {s = 35;}

                        else if ( (LA53_28=='-') ) {s = 36;}

                        else if ( (LA53_28=='+') ) {s = 37;}

                        else if ( (LA53_28=='#') ) {s = 38;}

                        else if ( (LA53_28=='/') ) {s = 39;}

                        else if ( (LA53_28=='\'') ) {s = 40;}

                        else if ( (LA53_28=='*'||LA53_28=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 31 : 
                        int LA53_61 = input.LA(1);

                        s = -1;
                        if ( (LA53_61=='\'') ) {s = 82;}

                        else if ( (LA53_61=='\\') ) {s = 60;}

                        else if ( ((LA53_61 >= '\u0000' && LA53_61 <= '&')||(LA53_61 >= '(' && LA53_61 <= ')')||(LA53_61 >= '+' && LA53_61 <= '>')||(LA53_61 >= '@' && LA53_61 <= '[')||(LA53_61 >= ']' && LA53_61 <= '\uFFFF')) ) {s = 61;}

                        else if ( (LA53_61=='*'||LA53_61=='?') ) {s = 58;}

                        if ( s>=0 ) return s;
                        break;

                    case 32 : 
                        int LA53_125 = input.LA(1);

                        s = -1;
                        if ( ((LA53_125 >= '\u0000' && LA53_125 <= '\b')||(LA53_125 >= '\u000B' && LA53_125 <= '\f')||(LA53_125 >= '\u000E' && LA53_125 <= '\u001F')||(LA53_125 >= '$' && LA53_125 <= '&')||LA53_125==','||LA53_125=='.'||(LA53_125 >= '0' && LA53_125 <= '9')||(LA53_125 >= ';' && LA53_125 <= '>')||(LA53_125 >= '@' && LA53_125 <= 'Z')||(LA53_125 >= '_' && LA53_125 <= 'z')||(LA53_125 >= '\u007F' && LA53_125 <= '\u2FFF')||(LA53_125 >= '\u3001' && LA53_125 <= '\u300B')||(LA53_125 >= '\u300D' && LA53_125 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_125=='\\') ) {s = 35;}

                        else if ( (LA53_125=='-') ) {s = 36;}

                        else if ( (LA53_125=='+') ) {s = 37;}

                        else if ( (LA53_125=='#') ) {s = 38;}

                        else if ( (LA53_125=='/') ) {s = 39;}

                        else if ( (LA53_125=='\'') ) {s = 40;}

                        else if ( (LA53_125=='*'||LA53_125=='?') ) {s = 41;}

                        else s = 110;

                        if ( s>=0 ) return s;
                        break;

                    case 33 : 
                        int LA53_75 = input.LA(1);

                        s = -1;
                        if ( ((LA53_75 >= '\u0000' && LA53_75 <= '\b')||(LA53_75 >= '\u000B' && LA53_75 <= '\f')||(LA53_75 >= '\u000E' && LA53_75 <= '\u001F')||(LA53_75 >= '$' && LA53_75 <= '&')||LA53_75==','||LA53_75=='.'||(LA53_75 >= '0' && LA53_75 <= '9')||(LA53_75 >= ';' && LA53_75 <= '>')||(LA53_75 >= '@' && LA53_75 <= 'Z')||(LA53_75 >= '_' && LA53_75 <= 'z')||(LA53_75 >= '\u007F' && LA53_75 <= '\u2FFF')||(LA53_75 >= '\u3001' && LA53_75 <= '\u300B')||(LA53_75 >= '\u300D' && LA53_75 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_75=='\\') ) {s = 35;}

                        else if ( (LA53_75=='-') ) {s = 36;}

                        else if ( (LA53_75=='+') ) {s = 37;}

                        else if ( (LA53_75=='#') ) {s = 38;}

                        else if ( (LA53_75=='/') ) {s = 39;}

                        else if ( (LA53_75=='\'') ) {s = 40;}

                        else if ( (LA53_75=='*'||LA53_75=='?') ) {s = 41;}

                        else s = 23;

                        if ( s>=0 ) return s;
                        break;

                    case 34 : 
                        int LA53_92 = input.LA(1);

                        s = -1;
                        if ( (LA53_92=='/') ) {s = 91;}

                        else if ( ((LA53_92 >= '0' && LA53_92 <= '9')) ) {s = 106;}

                        else if ( (LA53_92=='.') ) {s = 93;}

                        else if ( (LA53_92=='-') ) {s = 94;}

                        else if ( ((LA53_92 >= '\u0000' && LA53_92 <= '\b')||(LA53_92 >= '\u000B' && LA53_92 <= '\f')||(LA53_92 >= '\u000E' && LA53_92 <= '\u001F')||(LA53_92 >= '$' && LA53_92 <= '&')||LA53_92==','||(LA53_92 >= ';' && LA53_92 <= '>')||(LA53_92 >= '@' && LA53_92 <= 'Z')||(LA53_92 >= '_' && LA53_92 <= 'z')||(LA53_92 >= '\u007F' && LA53_92 <= '\u2FFF')||(LA53_92 >= '\u3001' && LA53_92 <= '\u300B')||(LA53_92 >= '\u300D' && LA53_92 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_92=='\\') ) {s = 35;}

                        else if ( (LA53_92=='+') ) {s = 37;}

                        else if ( (LA53_92=='#') ) {s = 38;}

                        else if ( (LA53_92=='\'') ) {s = 40;}

                        else if ( (LA53_92=='*'||LA53_92=='?') ) {s = 41;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 35 : 
                        int LA53_71 = input.LA(1);

                        s = -1;
                        if ( (LA53_71=='e') ) {s = 87;}

                        else if ( ((LA53_71 >= '\u0000' && LA53_71 <= '\b')||(LA53_71 >= '\u000B' && LA53_71 <= '\f')||(LA53_71 >= '\u000E' && LA53_71 <= '\u001F')||(LA53_71 >= '$' && LA53_71 <= '&')||LA53_71==','||LA53_71=='.'||(LA53_71 >= '0' && LA53_71 <= '9')||(LA53_71 >= ';' && LA53_71 <= '>')||(LA53_71 >= '@' && LA53_71 <= 'Z')||(LA53_71 >= '_' && LA53_71 <= 'd')||(LA53_71 >= 'f' && LA53_71 <= 'z')||(LA53_71 >= '\u007F' && LA53_71 <= '\u2FFF')||(LA53_71 >= '\u3001' && LA53_71 <= '\u300B')||(LA53_71 >= '\u300D' && LA53_71 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_71=='\\') ) {s = 35;}

                        else if ( (LA53_71=='-') ) {s = 36;}

                        else if ( (LA53_71=='+') ) {s = 37;}

                        else if ( (LA53_71=='#') ) {s = 38;}

                        else if ( (LA53_71=='/') ) {s = 39;}

                        else if ( (LA53_71=='\'') ) {s = 40;}

                        else if ( (LA53_71=='*'||LA53_71=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 36 : 
                        int LA53_42 = input.LA(1);

                        s = -1;
                        if ( (LA53_42=='t') ) {s = 71;}

                        else if ( ((LA53_42 >= '\u0000' && LA53_42 <= '\b')||(LA53_42 >= '\u000B' && LA53_42 <= '\f')||(LA53_42 >= '\u000E' && LA53_42 <= '\u001F')||(LA53_42 >= '$' && LA53_42 <= '&')||LA53_42==','||LA53_42=='.'||(LA53_42 >= '0' && LA53_42 <= '9')||(LA53_42 >= ';' && LA53_42 <= '>')||(LA53_42 >= '@' && LA53_42 <= 'Z')||(LA53_42 >= '_' && LA53_42 <= 's')||(LA53_42 >= 'u' && LA53_42 <= 'z')||(LA53_42 >= '\u007F' && LA53_42 <= '\u2FFF')||(LA53_42 >= '\u3001' && LA53_42 <= '\u300B')||(LA53_42 >= '\u300D' && LA53_42 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_42=='\\') ) {s = 35;}

                        else if ( (LA53_42=='-') ) {s = 36;}

                        else if ( (LA53_42=='+') ) {s = 37;}

                        else if ( (LA53_42=='#') ) {s = 38;}

                        else if ( (LA53_42=='/') ) {s = 39;}

                        else if ( (LA53_42=='\'') ) {s = 40;}

                        else if ( (LA53_42=='*'||LA53_42=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 37 : 
                        int LA53_26 = input.LA(1);

                        s = -1;
                        if ( (LA53_26=='O'||LA53_26=='o') ) {s = 65;}

                        else if ( (LA53_26=='E'||LA53_26=='e') ) {s = 66;}

                        else if ( ((LA53_26 >= '\u0000' && LA53_26 <= '\b')||(LA53_26 >= '\u000B' && LA53_26 <= '\f')||(LA53_26 >= '\u000E' && LA53_26 <= '\u001F')||(LA53_26 >= '$' && LA53_26 <= '&')||LA53_26==','||LA53_26=='.'||(LA53_26 >= '0' && LA53_26 <= '9')||(LA53_26 >= ';' && LA53_26 <= '>')||(LA53_26 >= '@' && LA53_26 <= 'D')||(LA53_26 >= 'F' && LA53_26 <= 'N')||(LA53_26 >= 'P' && LA53_26 <= 'Z')||(LA53_26 >= '_' && LA53_26 <= 'd')||(LA53_26 >= 'f' && LA53_26 <= 'n')||(LA53_26 >= 'p' && LA53_26 <= 'z')||(LA53_26 >= '\u007F' && LA53_26 <= '\u2FFF')||(LA53_26 >= '\u3001' && LA53_26 <= '\u300B')||(LA53_26 >= '\u300D' && LA53_26 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_26=='\\') ) {s = 35;}

                        else if ( (LA53_26=='-') ) {s = 36;}

                        else if ( (LA53_26=='+') ) {s = 37;}

                        else if ( (LA53_26=='#') ) {s = 38;}

                        else if ( (LA53_26=='/') ) {s = 39;}

                        else if ( (LA53_26=='\'') ) {s = 40;}

                        else if ( (LA53_26=='*'||LA53_26=='?') ) {s = 41;}

                        else s = 67;

                        if ( s>=0 ) return s;
                        break;

                    case 38 : 
                        int LA53_37 = input.LA(1);

                        s = -1;
                        if ( ((LA53_37 >= '\u0000' && LA53_37 <= '\b')||(LA53_37 >= '\u000B' && LA53_37 <= '\f')||(LA53_37 >= '\u000E' && LA53_37 <= '\u001F')||(LA53_37 >= '$' && LA53_37 <= '&')||LA53_37==','||LA53_37=='.'||(LA53_37 >= '0' && LA53_37 <= '9')||(LA53_37 >= ';' && LA53_37 <= '>')||(LA53_37 >= '@' && LA53_37 <= 'Z')||(LA53_37 >= '_' && LA53_37 <= 'z')||(LA53_37 >= '\u007F' && LA53_37 <= '\u2FFF')||(LA53_37 >= '\u3001' && LA53_37 <= '\u300B')||(LA53_37 >= '\u300D' && LA53_37 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_37=='\\') ) {s = 35;}

                        else if ( (LA53_37=='-') ) {s = 36;}

                        else if ( (LA53_37=='+') ) {s = 37;}

                        else if ( (LA53_37=='#') ) {s = 38;}

                        else if ( (LA53_37=='/') ) {s = 39;}

                        else if ( (LA53_37=='\'') ) {s = 40;}

                        else if ( (LA53_37=='*'||LA53_37=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 39 : 
                        int LA53_121 = input.LA(1);

                        s = -1;
                        if ( (LA53_121=='.') ) {s = 113;}

                        else if ( ((LA53_121 >= '0' && LA53_121 <= '9')) ) {s = 127;}

                        else if ( ((LA53_121 >= '\u0000' && LA53_121 <= '\b')||(LA53_121 >= '\u000B' && LA53_121 <= '\f')||(LA53_121 >= '\u000E' && LA53_121 <= '\u001F')||(LA53_121 >= '$' && LA53_121 <= '&')||LA53_121==','||(LA53_121 >= ';' && LA53_121 <= '>')||(LA53_121 >= '@' && LA53_121 <= 'Z')||(LA53_121 >= '_' && LA53_121 <= 'z')||(LA53_121 >= '\u007F' && LA53_121 <= '\u2FFF')||(LA53_121 >= '\u3001' && LA53_121 <= '\u300B')||(LA53_121 >= '\u300D' && LA53_121 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_121=='\\') ) {s = 35;}

                        else if ( (LA53_121=='-') ) {s = 36;}

                        else if ( (LA53_121=='+') ) {s = 37;}

                        else if ( (LA53_121=='#') ) {s = 38;}

                        else if ( (LA53_121=='/') ) {s = 39;}

                        else if ( (LA53_121=='\'') ) {s = 40;}

                        else if ( (LA53_121=='*'||LA53_121=='?') ) {s = 41;}

                        else s = 112;

                        if ( s>=0 ) return s;
                        break;

                    case 40 : 
                        int LA53_93 = input.LA(1);

                        s = -1;
                        if ( ((LA53_93 >= '0' && LA53_93 <= '9')) ) {s = 107;}

                        else if ( ((LA53_93 >= '\u0000' && LA53_93 <= '\b')||(LA53_93 >= '\u000B' && LA53_93 <= '\f')||(LA53_93 >= '\u000E' && LA53_93 <= '\u001F')||(LA53_93 >= '$' && LA53_93 <= '&')||LA53_93==','||LA53_93=='.'||(LA53_93 >= ';' && LA53_93 <= '>')||(LA53_93 >= '@' && LA53_93 <= 'Z')||(LA53_93 >= '_' && LA53_93 <= 'z')||(LA53_93 >= '\u007F' && LA53_93 <= '\u2FFF')||(LA53_93 >= '\u3001' && LA53_93 <= '\u300B')||(LA53_93 >= '\u300D' && LA53_93 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_93=='\\') ) {s = 35;}

                        else if ( (LA53_93=='-') ) {s = 36;}

                        else if ( (LA53_93=='+') ) {s = 37;}

                        else if ( (LA53_93=='#') ) {s = 38;}

                        else if ( (LA53_93=='/') ) {s = 39;}

                        else if ( (LA53_93=='\'') ) {s = 40;}

                        else if ( (LA53_93=='*'||LA53_93=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 41 : 
                        int LA53_36 = input.LA(1);

                        s = -1;
                        if ( ((LA53_36 >= '\u0000' && LA53_36 <= '\b')||(LA53_36 >= '\u000B' && LA53_36 <= '\f')||(LA53_36 >= '\u000E' && LA53_36 <= '\u001F')||(LA53_36 >= '$' && LA53_36 <= '&')||LA53_36==','||LA53_36=='.'||(LA53_36 >= '0' && LA53_36 <= '9')||(LA53_36 >= ';' && LA53_36 <= '>')||(LA53_36 >= '@' && LA53_36 <= 'Z')||(LA53_36 >= '_' && LA53_36 <= 'z')||(LA53_36 >= '\u007F' && LA53_36 <= '\u2FFF')||(LA53_36 >= '\u3001' && LA53_36 <= '\u300B')||(LA53_36 >= '\u300D' && LA53_36 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_36=='\\') ) {s = 35;}

                        else if ( (LA53_36=='-') ) {s = 36;}

                        else if ( (LA53_36=='+') ) {s = 37;}

                        else if ( (LA53_36=='#') ) {s = 38;}

                        else if ( (LA53_36=='/') ) {s = 39;}

                        else if ( (LA53_36=='\'') ) {s = 40;}

                        else if ( (LA53_36=='*'||LA53_36=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 42 : 
                        int LA53_105 = input.LA(1);

                        s = -1;
                        if ( (LA53_105=='.') ) {s = 113;}

                        else if ( ((LA53_105 >= '0' && LA53_105 <= '9')) ) {s = 114;}

                        else if ( ((LA53_105 >= '\u0000' && LA53_105 <= '\b')||(LA53_105 >= '\u000B' && LA53_105 <= '\f')||(LA53_105 >= '\u000E' && LA53_105 <= '\u001F')||(LA53_105 >= '$' && LA53_105 <= '&')||LA53_105==','||(LA53_105 >= ';' && LA53_105 <= '>')||(LA53_105 >= '@' && LA53_105 <= 'Z')||(LA53_105 >= '_' && LA53_105 <= 'z')||(LA53_105 >= '\u007F' && LA53_105 <= '\u2FFF')||(LA53_105 >= '\u3001' && LA53_105 <= '\u300B')||(LA53_105 >= '\u300D' && LA53_105 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_105=='\\') ) {s = 35;}

                        else if ( (LA53_105=='-') ) {s = 36;}

                        else if ( (LA53_105=='+') ) {s = 37;}

                        else if ( (LA53_105=='#') ) {s = 38;}

                        else if ( (LA53_105=='/') ) {s = 39;}

                        else if ( (LA53_105=='\'') ) {s = 40;}

                        else if ( (LA53_105=='*'||LA53_105=='?') ) {s = 41;}

                        else s = 112;

                        if ( s>=0 ) return s;
                        break;

                    case 43 : 
                        int LA53_46 = input.LA(1);

                        s = -1;
                        if ( ((LA53_46 >= '0' && LA53_46 <= '9')) ) {s = 76;}

                        else if ( ((LA53_46 >= '\u0000' && LA53_46 <= '\b')||(LA53_46 >= '\u000B' && LA53_46 <= '\f')||(LA53_46 >= '\u000E' && LA53_46 <= '\u001F')||(LA53_46 >= '$' && LA53_46 <= '&')||LA53_46==','||LA53_46=='.'||(LA53_46 >= ';' && LA53_46 <= '>')||(LA53_46 >= '@' && LA53_46 <= 'Z')||(LA53_46 >= '_' && LA53_46 <= 'z')||(LA53_46 >= '\u007F' && LA53_46 <= '\u2FFF')||(LA53_46 >= '\u3001' && LA53_46 <= '\u300B')||(LA53_46 >= '\u300D' && LA53_46 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_46=='\\') ) {s = 35;}

                        else if ( (LA53_46=='-') ) {s = 36;}

                        else if ( (LA53_46=='+') ) {s = 37;}

                        else if ( (LA53_46=='#') ) {s = 38;}

                        else if ( (LA53_46=='/') ) {s = 39;}

                        else if ( (LA53_46=='\'') ) {s = 40;}

                        else if ( (LA53_46=='*'||LA53_46=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 44 : 
                        int LA53_120 = input.LA(1);

                        s = -1;
                        if ( ((LA53_120 >= '0' && LA53_120 <= '9')) ) {s = 120;}

                        else if ( ((LA53_120 >= '\u0000' && LA53_120 <= '\b')||(LA53_120 >= '\u000B' && LA53_120 <= '\f')||(LA53_120 >= '\u000E' && LA53_120 <= '\u001F')||(LA53_120 >= '$' && LA53_120 <= '&')||LA53_120==','||LA53_120=='.'||(LA53_120 >= ';' && LA53_120 <= '>')||(LA53_120 >= '@' && LA53_120 <= 'Z')||(LA53_120 >= '_' && LA53_120 <= 'z')||(LA53_120 >= '\u007F' && LA53_120 <= '\u2FFF')||(LA53_120 >= '\u3001' && LA53_120 <= '\u300B')||(LA53_120 >= '\u300D' && LA53_120 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_120=='\\') ) {s = 35;}

                        else if ( (LA53_120=='-') ) {s = 36;}

                        else if ( (LA53_120=='+') ) {s = 37;}

                        else if ( (LA53_120=='#') ) {s = 38;}

                        else if ( (LA53_120=='/') ) {s = 39;}

                        else if ( (LA53_120=='\'') ) {s = 40;}

                        else if ( (LA53_120=='*'||LA53_120=='?') ) {s = 41;}

                        else s = 112;

                        if ( s>=0 ) return s;
                        break;

                    case 45 : 
                        int LA53_87 = input.LA(1);

                        s = -1;
                        if ( (LA53_87=='d') ) {s = 101;}

                        else if ( ((LA53_87 >= '\u0000' && LA53_87 <= '\b')||(LA53_87 >= '\u000B' && LA53_87 <= '\f')||(LA53_87 >= '\u000E' && LA53_87 <= '\u001F')||(LA53_87 >= '$' && LA53_87 <= '&')||LA53_87==','||LA53_87=='.'||(LA53_87 >= '0' && LA53_87 <= '9')||(LA53_87 >= ';' && LA53_87 <= '>')||(LA53_87 >= '@' && LA53_87 <= 'Z')||(LA53_87 >= '_' && LA53_87 <= 'c')||(LA53_87 >= 'e' && LA53_87 <= 'z')||(LA53_87 >= '\u007F' && LA53_87 <= '\u2FFF')||(LA53_87 >= '\u3001' && LA53_87 <= '\u300B')||(LA53_87 >= '\u300D' && LA53_87 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_87=='\\') ) {s = 35;}

                        else if ( (LA53_87=='-') ) {s = 36;}

                        else if ( (LA53_87=='+') ) {s = 37;}

                        else if ( (LA53_87=='#') ) {s = 38;}

                        else if ( (LA53_87=='/') ) {s = 39;}

                        else if ( (LA53_87=='\'') ) {s = 40;}

                        else if ( (LA53_87=='*'||LA53_87=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 46 : 
                        int LA53_1 = input.LA(1);

                        s = -1;
                        if ( (LA53_1=='e') ) {s = 32;}

                        else if ( ((LA53_1 >= '\u0000' && LA53_1 <= '\b')||(LA53_1 >= '\u000B' && LA53_1 <= '\f')||(LA53_1 >= '\u000E' && LA53_1 <= '\u001F')||(LA53_1 >= '$' && LA53_1 <= '&')||LA53_1==','||LA53_1=='.'||(LA53_1 >= '0' && LA53_1 <= '9')||(LA53_1 >= ';' && LA53_1 <= '>')||(LA53_1 >= '@' && LA53_1 <= 'Z')||(LA53_1 >= '_' && LA53_1 <= 'd')||(LA53_1 >= 'f' && LA53_1 <= 'z')||(LA53_1 >= '\u007F' && LA53_1 <= '\u2FFF')||(LA53_1 >= '\u3001' && LA53_1 <= '\u300B')||(LA53_1 >= '\u300D' && LA53_1 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_1=='\\') ) {s = 35;}

                        else if ( (LA53_1=='-') ) {s = 36;}

                        else if ( (LA53_1=='+') ) {s = 37;}

                        else if ( (LA53_1=='#') ) {s = 38;}

                        else if ( (LA53_1=='/') ) {s = 39;}

                        else if ( (LA53_1=='\'') ) {s = 40;}

                        else if ( (LA53_1=='*'||LA53_1=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 47 : 
                        int LA53_63 = input.LA(1);

                        s = -1;
                        if ( ((LA53_63 >= '\u0000' && LA53_63 <= '\b')||(LA53_63 >= '\u000B' && LA53_63 <= '\f')||(LA53_63 >= '\u000E' && LA53_63 <= '\u001F')||(LA53_63 >= '$' && LA53_63 <= '&')||LA53_63==','||LA53_63=='.'||(LA53_63 >= '0' && LA53_63 <= '9')||(LA53_63 >= ';' && LA53_63 <= '>')||(LA53_63 >= '@' && LA53_63 <= 'Z')||(LA53_63 >= '_' && LA53_63 <= 'z')||(LA53_63 >= '\u007F' && LA53_63 <= '\u2FFF')||(LA53_63 >= '\u3001' && LA53_63 <= '\u300B')||(LA53_63 >= '\u300D' && LA53_63 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_63=='\\') ) {s = 35;}

                        else if ( (LA53_63=='-') ) {s = 36;}

                        else if ( (LA53_63=='+') ) {s = 37;}

                        else if ( (LA53_63=='#') ) {s = 38;}

                        else if ( (LA53_63=='/') ) {s = 39;}

                        else if ( (LA53_63=='\'') ) {s = 40;}

                        else if ( (LA53_63=='*'||LA53_63=='?') ) {s = 41;}

                        else s = 23;

                        if ( s>=0 ) return s;
                        break;

                    case 48 : 
                        int LA53_11 = input.LA(1);

                        s = -1;
                        if ( ((LA53_11 >= '\u0000' && LA53_11 <= '\b')||(LA53_11 >= '\u000B' && LA53_11 <= '\f')||(LA53_11 >= '\u000E' && LA53_11 <= '\u001F')||(LA53_11 >= '#' && LA53_11 <= '\'')||(LA53_11 >= '+' && LA53_11 <= '9')||(LA53_11 >= ';' && LA53_11 <= '>')||(LA53_11 >= '@' && LA53_11 <= 'Z')||LA53_11=='\\'||(LA53_11 >= '_' && LA53_11 <= 'z')||(LA53_11 >= '\u007F' && LA53_11 <= '\u2FFF')||(LA53_11 >= '\u3001' && LA53_11 <= '\u300B')||(LA53_11 >= '\u300D' && LA53_11 <= '\uFFFF')) ) {s = 41;}

                        else s = 53;

                        if ( s>=0 ) return s;
                        break;

                    case 49 : 
                        int LA53_113 = input.LA(1);

                        s = -1;
                        if ( ((LA53_113 >= '0' && LA53_113 <= '9')) ) {s = 120;}

                        else if ( ((LA53_113 >= '\u0000' && LA53_113 <= '\b')||(LA53_113 >= '\u000B' && LA53_113 <= '\f')||(LA53_113 >= '\u000E' && LA53_113 <= '\u001F')||(LA53_113 >= '$' && LA53_113 <= '&')||LA53_113==','||LA53_113=='.'||(LA53_113 >= ';' && LA53_113 <= '>')||(LA53_113 >= '@' && LA53_113 <= 'Z')||(LA53_113 >= '_' && LA53_113 <= 'z')||(LA53_113 >= '\u007F' && LA53_113 <= '\u2FFF')||(LA53_113 >= '\u3001' && LA53_113 <= '\u300B')||(LA53_113 >= '\u300D' && LA53_113 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_113=='\\') ) {s = 35;}

                        else if ( (LA53_113=='-') ) {s = 36;}

                        else if ( (LA53_113=='+') ) {s = 37;}

                        else if ( (LA53_113=='#') ) {s = 38;}

                        else if ( (LA53_113=='/') ) {s = 39;}

                        else if ( (LA53_113=='\'') ) {s = 40;}

                        else if ( (LA53_113=='*'||LA53_113=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 50 : 
                        int LA53_64 = input.LA(1);

                        s = -1;
                        if ( ((LA53_64 >= '\u0000' && LA53_64 <= '\b')||(LA53_64 >= '\u000B' && LA53_64 <= '\f')||(LA53_64 >= '\u000E' && LA53_64 <= '\u001F')||(LA53_64 >= '$' && LA53_64 <= '&')||LA53_64==','||LA53_64=='.'||(LA53_64 >= '0' && LA53_64 <= '9')||(LA53_64 >= ';' && LA53_64 <= '>')||(LA53_64 >= '@' && LA53_64 <= 'Z')||(LA53_64 >= '_' && LA53_64 <= 'z')||(LA53_64 >= '\u007F' && LA53_64 <= '\u2FFF')||(LA53_64 >= '\u3001' && LA53_64 <= '\u300B')||(LA53_64 >= '\u300D' && LA53_64 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_64=='\\') ) {s = 35;}

                        else if ( (LA53_64=='-') ) {s = 36;}

                        else if ( (LA53_64=='+') ) {s = 37;}

                        else if ( (LA53_64=='#') ) {s = 38;}

                        else if ( (LA53_64=='/') ) {s = 39;}

                        else if ( (LA53_64=='\'') ) {s = 40;}

                        else if ( (LA53_64=='*'||LA53_64=='?') ) {s = 41;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 51 : 
                        int LA53_39 = input.LA(1);

                        s = -1;
                        if ( ((LA53_39 >= '\u0000' && LA53_39 <= '\b')||(LA53_39 >= '\u000B' && LA53_39 <= '\f')||(LA53_39 >= '\u000E' && LA53_39 <= '\u001F')||(LA53_39 >= '$' && LA53_39 <= '&')||LA53_39==','||LA53_39=='.'||(LA53_39 >= '0' && LA53_39 <= '9')||(LA53_39 >= ';' && LA53_39 <= '>')||(LA53_39 >= '@' && LA53_39 <= 'Z')||(LA53_39 >= '_' && LA53_39 <= 'z')||(LA53_39 >= '\u007F' && LA53_39 <= '\u2FFF')||(LA53_39 >= '\u3001' && LA53_39 <= '\u300B')||(LA53_39 >= '\u300D' && LA53_39 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_39=='\\') ) {s = 35;}

                        else if ( (LA53_39=='-') ) {s = 36;}

                        else if ( (LA53_39=='+') ) {s = 37;}

                        else if ( (LA53_39=='#') ) {s = 38;}

                        else if ( (LA53_39=='/') ) {s = 39;}

                        else if ( (LA53_39=='\'') ) {s = 40;}

                        else if ( (LA53_39=='*'||LA53_39=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 52 : 
                        int LA53_38 = input.LA(1);

                        s = -1;
                        if ( ((LA53_38 >= '\u0000' && LA53_38 <= '\b')||(LA53_38 >= '\u000B' && LA53_38 <= '\f')||(LA53_38 >= '\u000E' && LA53_38 <= '\u001F')||(LA53_38 >= '$' && LA53_38 <= '&')||LA53_38==','||LA53_38=='.'||(LA53_38 >= '0' && LA53_38 <= '9')||(LA53_38 >= ';' && LA53_38 <= '>')||(LA53_38 >= '@' && LA53_38 <= 'Z')||(LA53_38 >= '_' && LA53_38 <= 'z')||(LA53_38 >= '\u007F' && LA53_38 <= '\u2FFF')||(LA53_38 >= '\u3001' && LA53_38 <= '\u300B')||(LA53_38 >= '\u300D' && LA53_38 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_38=='\\') ) {s = 35;}

                        else if ( (LA53_38=='-') ) {s = 36;}

                        else if ( (LA53_38=='+') ) {s = 37;}

                        else if ( (LA53_38=='#') ) {s = 38;}

                        else if ( (LA53_38=='/') ) {s = 39;}

                        else if ( (LA53_38=='\'') ) {s = 40;}

                        else if ( (LA53_38=='*'||LA53_38=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 53 : 
                        int LA53_68 = input.LA(1);

                        s = -1;
                        if ( ((LA53_68 >= '\u0000' && LA53_68 <= '\b')||(LA53_68 >= '\u000B' && LA53_68 <= '\f')||(LA53_68 >= '\u000E' && LA53_68 <= '\u001F')||(LA53_68 >= '$' && LA53_68 <= '&')||LA53_68==','||LA53_68=='.'||(LA53_68 >= '0' && LA53_68 <= '9')||(LA53_68 >= ';' && LA53_68 <= '>')||(LA53_68 >= '@' && LA53_68 <= 'Z')||(LA53_68 >= '_' && LA53_68 <= 'z')||(LA53_68 >= '\u007F' && LA53_68 <= '\u2FFF')||(LA53_68 >= '\u3001' && LA53_68 <= '\u300B')||(LA53_68 >= '\u300D' && LA53_68 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_68=='\\') ) {s = 35;}

                        else if ( (LA53_68=='-') ) {s = 36;}

                        else if ( (LA53_68=='+') ) {s = 37;}

                        else if ( (LA53_68=='#') ) {s = 38;}

                        else if ( (LA53_68=='/') ) {s = 39;}

                        else if ( (LA53_68=='\'') ) {s = 40;}

                        else if ( (LA53_68=='*'||LA53_68=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 54 : 
                        int LA53_40 = input.LA(1);

                        s = -1;
                        if ( ((LA53_40 >= '\u0000' && LA53_40 <= '\b')||(LA53_40 >= '\u000B' && LA53_40 <= '\f')||(LA53_40 >= '\u000E' && LA53_40 <= '\u001F')||(LA53_40 >= '$' && LA53_40 <= '&')||LA53_40==','||LA53_40=='.'||(LA53_40 >= '0' && LA53_40 <= '9')||(LA53_40 >= ';' && LA53_40 <= '>')||(LA53_40 >= '@' && LA53_40 <= 'Z')||(LA53_40 >= '_' && LA53_40 <= 'z')||(LA53_40 >= '\u007F' && LA53_40 <= '\u2FFF')||(LA53_40 >= '\u3001' && LA53_40 <= '\u300B')||(LA53_40 >= '\u300D' && LA53_40 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_40=='\\') ) {s = 35;}

                        else if ( (LA53_40=='-') ) {s = 36;}

                        else if ( (LA53_40=='+') ) {s = 37;}

                        else if ( (LA53_40=='#') ) {s = 38;}

                        else if ( (LA53_40=='/') ) {s = 39;}

                        else if ( (LA53_40=='\'') ) {s = 40;}

                        else if ( (LA53_40=='*'||LA53_40=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 55 : 
                        int LA53_70 = input.LA(1);

                        s = -1;
                        if ( ((LA53_70 >= '\u0000' && LA53_70 <= '\b')||(LA53_70 >= '\u000B' && LA53_70 <= '\f')||(LA53_70 >= '\u000E' && LA53_70 <= '\u001F')||(LA53_70 >= '$' && LA53_70 <= '&')||LA53_70==','||LA53_70=='.'||(LA53_70 >= '0' && LA53_70 <= '9')||(LA53_70 >= ';' && LA53_70 <= '>')||(LA53_70 >= '@' && LA53_70 <= 'Z')||(LA53_70 >= '_' && LA53_70 <= 'z')||(LA53_70 >= '\u007F' && LA53_70 <= '\u2FFF')||(LA53_70 >= '\u3001' && LA53_70 <= '\u300B')||(LA53_70 >= '\u300D' && LA53_70 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_70=='\\') ) {s = 35;}

                        else if ( (LA53_70=='-') ) {s = 36;}

                        else if ( (LA53_70=='+') ) {s = 37;}

                        else if ( (LA53_70=='#') ) {s = 38;}

                        else if ( (LA53_70=='/') ) {s = 39;}

                        else if ( (LA53_70=='\'') ) {s = 40;}

                        else if ( (LA53_70=='*'||LA53_70=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 56 : 
                        int LA53_34 = input.LA(1);

                        s = -1;
                        if ( ((LA53_34 >= '\u0000' && LA53_34 <= '\b')||(LA53_34 >= '\u000B' && LA53_34 <= '\f')||(LA53_34 >= '\u000E' && LA53_34 <= '\u001F')||(LA53_34 >= '$' && LA53_34 <= '&')||LA53_34==','||LA53_34=='.'||(LA53_34 >= '0' && LA53_34 <= '9')||(LA53_34 >= ';' && LA53_34 <= '>')||(LA53_34 >= '@' && LA53_34 <= 'Z')||(LA53_34 >= '_' && LA53_34 <= 'z')||(LA53_34 >= '\u007F' && LA53_34 <= '\u2FFF')||(LA53_34 >= '\u3001' && LA53_34 <= '\u300B')||(LA53_34 >= '\u300D' && LA53_34 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_34=='\\') ) {s = 35;}

                        else if ( (LA53_34=='-') ) {s = 36;}

                        else if ( (LA53_34=='+') ) {s = 37;}

                        else if ( (LA53_34=='#') ) {s = 38;}

                        else if ( (LA53_34=='/') ) {s = 39;}

                        else if ( (LA53_34=='\'') ) {s = 40;}

                        else if ( (LA53_34=='*'||LA53_34=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 57 : 
                        int LA53_84 = input.LA(1);

                        s = -1;
                        if ( ((LA53_84 >= '\u0000' && LA53_84 <= '\b')||(LA53_84 >= '\u000B' && LA53_84 <= '\f')||(LA53_84 >= '\u000E' && LA53_84 <= '\u001F')||(LA53_84 >= '$' && LA53_84 <= '&')||LA53_84==','||LA53_84=='.'||(LA53_84 >= '0' && LA53_84 <= '9')||(LA53_84 >= ';' && LA53_84 <= '>')||(LA53_84 >= '@' && LA53_84 <= 'Z')||(LA53_84 >= '_' && LA53_84 <= 'z')||(LA53_84 >= '\u007F' && LA53_84 <= '\u2FFF')||(LA53_84 >= '\u3001' && LA53_84 <= '\u300B')||(LA53_84 >= '\u300D' && LA53_84 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_84=='\\') ) {s = 35;}

                        else if ( (LA53_84=='-') ) {s = 36;}

                        else if ( (LA53_84=='+') ) {s = 37;}

                        else if ( (LA53_84=='#') ) {s = 38;}

                        else if ( (LA53_84=='/') ) {s = 39;}

                        else if ( (LA53_84=='\'') ) {s = 40;}

                        else if ( (LA53_84=='*'||LA53_84=='?') ) {s = 41;}

                        else s = 27;

                        if ( s>=0 ) return s;
                        break;

                    case 58 : 
                        int LA53_127 = input.LA(1);

                        s = -1;
                        if ( (LA53_127=='.') ) {s = 113;}

                        else if ( ((LA53_127 >= '0' && LA53_127 <= '9')) ) {s = 122;}

                        else if ( ((LA53_127 >= '\u0000' && LA53_127 <= '\b')||(LA53_127 >= '\u000B' && LA53_127 <= '\f')||(LA53_127 >= '\u000E' && LA53_127 <= '\u001F')||(LA53_127 >= '$' && LA53_127 <= '&')||LA53_127==','||(LA53_127 >= ';' && LA53_127 <= '>')||(LA53_127 >= '@' && LA53_127 <= 'Z')||(LA53_127 >= '_' && LA53_127 <= 'z')||(LA53_127 >= '\u007F' && LA53_127 <= '\u2FFF')||(LA53_127 >= '\u3001' && LA53_127 <= '\u300B')||(LA53_127 >= '\u300D' && LA53_127 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_127=='\\') ) {s = 35;}

                        else if ( (LA53_127=='-') ) {s = 36;}

                        else if ( (LA53_127=='+') ) {s = 37;}

                        else if ( (LA53_127=='#') ) {s = 38;}

                        else if ( (LA53_127=='/') ) {s = 39;}

                        else if ( (LA53_127=='\'') ) {s = 40;}

                        else if ( (LA53_127=='*'||LA53_127=='?') ) {s = 41;}

                        else s = 112;

                        if ( s>=0 ) return s;
                        break;

                    case 59 : 
                        int LA53_12 = input.LA(1);

                        s = -1;
                        if ( (LA53_12=='?') ) {s = 12;}

                        else if ( ((LA53_12 >= '\u0000' && LA53_12 <= '\b')||(LA53_12 >= '\u000B' && LA53_12 <= '\f')||(LA53_12 >= '\u000E' && LA53_12 <= '\u001F')||(LA53_12 >= '#' && LA53_12 <= '\'')||(LA53_12 >= '+' && LA53_12 <= '9')||(LA53_12 >= ';' && LA53_12 <= '>')||(LA53_12 >= '@' && LA53_12 <= 'Z')||LA53_12=='\\'||(LA53_12 >= '_' && LA53_12 <= 'z')||(LA53_12 >= '\u007F' && LA53_12 <= '\u2FFF')||(LA53_12 >= '\u3001' && LA53_12 <= '\u300B')||(LA53_12 >= '\u300D' && LA53_12 <= '\uFFFF')) ) {s = 41;}

                        else s = 54;

                        if ( s>=0 ) return s;
                        break;

                    case 60 : 
                        int LA53_44 = input.LA(1);

                        s = -1;
                        if ( (LA53_44=='X') ) {s = 73;}

                        else if ( (LA53_44=='x') ) {s = 74;}

                        else if ( ((LA53_44 >= '\u0000' && LA53_44 <= '\b')||(LA53_44 >= '\u000B' && LA53_44 <= '\f')||(LA53_44 >= '\u000E' && LA53_44 <= '\u001F')||(LA53_44 >= '$' && LA53_44 <= '&')||LA53_44==','||LA53_44=='.'||(LA53_44 >= '0' && LA53_44 <= '9')||(LA53_44 >= ';' && LA53_44 <= '>')||(LA53_44 >= '@' && LA53_44 <= 'W')||(LA53_44 >= 'Y' && LA53_44 <= 'Z')||(LA53_44 >= '_' && LA53_44 <= 'w')||(LA53_44 >= 'y' && LA53_44 <= 'z')||(LA53_44 >= '\u007F' && LA53_44 <= '\u2FFF')||(LA53_44 >= '\u3001' && LA53_44 <= '\u300B')||(LA53_44 >= '\u300D' && LA53_44 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_44=='\\') ) {s = 35;}

                        else if ( (LA53_44=='-') ) {s = 36;}

                        else if ( (LA53_44=='+') ) {s = 37;}

                        else if ( (LA53_44=='#') ) {s = 38;}

                        else if ( (LA53_44=='/') ) {s = 39;}

                        else if ( (LA53_44=='\'') ) {s = 40;}

                        else if ( (LA53_44=='*'||LA53_44=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 61 : 
                        int LA53_77 = input.LA(1);

                        s = -1;
                        if ( (LA53_77=='.') ) {s = 95;}

                        else if ( ((LA53_77 >= '0' && LA53_77 <= '9')) ) {s = 77;}

                        else if ( ((LA53_77 >= '\u0000' && LA53_77 <= '\b')||(LA53_77 >= '\u000B' && LA53_77 <= '\f')||(LA53_77 >= '\u000E' && LA53_77 <= '\u001F')||(LA53_77 >= '$' && LA53_77 <= '&')||LA53_77==','||(LA53_77 >= ';' && LA53_77 <= '>')||(LA53_77 >= '@' && LA53_77 <= 'Z')||(LA53_77 >= '_' && LA53_77 <= 'z')||(LA53_77 >= '\u007F' && LA53_77 <= '\u2FFF')||(LA53_77 >= '\u3001' && LA53_77 <= '\u300B')||(LA53_77 >= '\u300D' && LA53_77 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_77=='\\') ) {s = 35;}

                        else if ( (LA53_77=='-') ) {s = 36;}

                        else if ( (LA53_77=='+') ) {s = 37;}

                        else if ( (LA53_77=='#') ) {s = 38;}

                        else if ( (LA53_77=='/') ) {s = 39;}

                        else if ( (LA53_77=='\'') ) {s = 40;}

                        else if ( (LA53_77=='*'||LA53_77=='?') ) {s = 41;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 62 : 
                        int LA53_103 = input.LA(1);

                        s = -1;
                        if ( (LA53_103==':') ) {s = 112;}

                        else if ( ((LA53_103 >= '\u0000' && LA53_103 <= '\b')||(LA53_103 >= '\u000B' && LA53_103 <= '\f')||(LA53_103 >= '\u000E' && LA53_103 <= '\u001F')||(LA53_103 >= '$' && LA53_103 <= '&')||LA53_103==','||LA53_103=='.'||(LA53_103 >= '0' && LA53_103 <= '9')||(LA53_103 >= ';' && LA53_103 <= '>')||(LA53_103 >= '@' && LA53_103 <= 'Z')||(LA53_103 >= '_' && LA53_103 <= 'z')||(LA53_103 >= '\u007F' && LA53_103 <= '\u2FFF')||(LA53_103 >= '\u3001' && LA53_103 <= '\u300B')||(LA53_103 >= '\u300D' && LA53_103 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_103=='\\') ) {s = 35;}

                        else if ( (LA53_103=='-') ) {s = 36;}

                        else if ( (LA53_103=='+') ) {s = 37;}

                        else if ( (LA53_103=='#') ) {s = 38;}

                        else if ( (LA53_103=='/') ) {s = 39;}

                        else if ( (LA53_103=='\'') ) {s = 40;}

                        else if ( (LA53_103=='*'||LA53_103=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 63 : 
                        int LA53_104 = input.LA(1);

                        s = -1;
                        if ( (LA53_104==':') ) {s = 112;}

                        else if ( ((LA53_104 >= '\u0000' && LA53_104 <= '\b')||(LA53_104 >= '\u000B' && LA53_104 <= '\f')||(LA53_104 >= '\u000E' && LA53_104 <= '\u001F')||(LA53_104 >= '$' && LA53_104 <= '&')||LA53_104==','||LA53_104=='.'||(LA53_104 >= '0' && LA53_104 <= '9')||(LA53_104 >= ';' && LA53_104 <= '>')||(LA53_104 >= '@' && LA53_104 <= 'Z')||(LA53_104 >= '_' && LA53_104 <= 'z')||(LA53_104 >= '\u007F' && LA53_104 <= '\u2FFF')||(LA53_104 >= '\u3001' && LA53_104 <= '\u300B')||(LA53_104 >= '\u300D' && LA53_104 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_104=='\\') ) {s = 35;}

                        else if ( (LA53_104=='-') ) {s = 36;}

                        else if ( (LA53_104=='+') ) {s = 37;}

                        else if ( (LA53_104=='#') ) {s = 38;}

                        else if ( (LA53_104=='/') ) {s = 39;}

                        else if ( (LA53_104=='\'') ) {s = 40;}

                        else if ( (LA53_104=='*'||LA53_104=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 64 : 
                        int LA53_17 = input.LA(1);

                        s = -1;
                        if ( (LA53_17=='\\') ) {s = 56;}

                        else if ( ((LA53_17 >= '\u0000' && LA53_17 <= '!')||(LA53_17 >= '#' && LA53_17 <= ')')||(LA53_17 >= '+' && LA53_17 <= '>')||(LA53_17 >= '@' && LA53_17 <= '[')||(LA53_17 >= ']' && LA53_17 <= '\u300B')||(LA53_17 >= '\u300D' && LA53_17 <= '\uFFFF')) ) {s = 57;}

                        else if ( (LA53_17=='*'||LA53_17=='?') ) {s = 58;}

                        else s = 55;

                        if ( s>=0 ) return s;
                        break;

                    case 65 : 
                        int LA53_78 = input.LA(1);

                        s = -1;
                        if ( ((LA53_78 >= '0' && LA53_78 <= '9')) ) {s = 96;}

                        else if ( (LA53_78=='.') ) {s = 93;}

                        else if ( (LA53_78=='-') ) {s = 94;}

                        else if ( ((LA53_78 >= '\u0000' && LA53_78 <= '\b')||(LA53_78 >= '\u000B' && LA53_78 <= '\f')||(LA53_78 >= '\u000E' && LA53_78 <= '\u001F')||(LA53_78 >= '$' && LA53_78 <= '&')||LA53_78==','||(LA53_78 >= ';' && LA53_78 <= '>')||(LA53_78 >= '@' && LA53_78 <= 'Z')||(LA53_78 >= '_' && LA53_78 <= 'z')||(LA53_78 >= '\u007F' && LA53_78 <= '\u2FFF')||(LA53_78 >= '\u3001' && LA53_78 <= '\u300B')||(LA53_78 >= '\u300D' && LA53_78 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_78=='\\') ) {s = 35;}

                        else if ( (LA53_78=='/') ) {s = 97;}

                        else if ( (LA53_78=='+') ) {s = 37;}

                        else if ( (LA53_78=='#') ) {s = 38;}

                        else if ( (LA53_78=='\'') ) {s = 40;}

                        else if ( (LA53_78=='*'||LA53_78=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 66 : 
                        int LA53_47 = input.LA(1);

                        s = -1;
                        if ( (LA53_47=='.') ) {s = 46;}

                        else if ( ((LA53_47 >= '0' && LA53_47 <= '9')) ) {s = 77;}

                        else if ( (LA53_47=='-') ) {s = 49;}

                        else if ( ((LA53_47 >= '\u0000' && LA53_47 <= '\b')||(LA53_47 >= '\u000B' && LA53_47 <= '\f')||(LA53_47 >= '\u000E' && LA53_47 <= '\u001F')||(LA53_47 >= '$' && LA53_47 <= '&')||LA53_47==','||(LA53_47 >= ';' && LA53_47 <= '>')||(LA53_47 >= '@' && LA53_47 <= 'Z')||(LA53_47 >= '_' && LA53_47 <= 'z')||(LA53_47 >= '\u007F' && LA53_47 <= '\u2FFF')||(LA53_47 >= '\u3001' && LA53_47 <= '\u300B')||(LA53_47 >= '\u300D' && LA53_47 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_47=='\\') ) {s = 35;}

                        else if ( (LA53_47=='/') ) {s = 50;}

                        else if ( (LA53_47=='+') ) {s = 37;}

                        else if ( (LA53_47=='#') ) {s = 38;}

                        else if ( (LA53_47=='\'') ) {s = 40;}

                        else if ( (LA53_47=='*'||LA53_47=='?') ) {s = 41;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 67 : 
                        int LA53_2 = input.LA(1);

                        s = -1;
                        if ( (LA53_2=='i') ) {s = 42;}

                        else if ( (LA53_2=='o') ) {s = 43;}

                        else if ( ((LA53_2 >= '\u0000' && LA53_2 <= '\b')||(LA53_2 >= '\u000B' && LA53_2 <= '\f')||(LA53_2 >= '\u000E' && LA53_2 <= '\u001F')||(LA53_2 >= '$' && LA53_2 <= '&')||LA53_2==','||LA53_2=='.'||(LA53_2 >= '0' && LA53_2 <= '9')||(LA53_2 >= ';' && LA53_2 <= '>')||(LA53_2 >= '@' && LA53_2 <= 'Z')||(LA53_2 >= '_' && LA53_2 <= 'h')||(LA53_2 >= 'j' && LA53_2 <= 'n')||(LA53_2 >= 'p' && LA53_2 <= 'z')||(LA53_2 >= '\u007F' && LA53_2 <= '\u2FFF')||(LA53_2 >= '\u3001' && LA53_2 <= '\u300B')||(LA53_2 >= '\u300D' && LA53_2 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_2=='\\') ) {s = 35;}

                        else if ( (LA53_2=='-') ) {s = 36;}

                        else if ( (LA53_2=='+') ) {s = 37;}

                        else if ( (LA53_2=='#') ) {s = 38;}

                        else if ( (LA53_2=='/') ) {s = 39;}

                        else if ( (LA53_2=='\'') ) {s = 40;}

                        else if ( (LA53_2=='*'||LA53_2=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 68 : 
                        int LA53_20 = input.LA(1);

                        s = -1;
                        if ( (LA53_20=='O') ) {s = 62;}

                        else if ( ((LA53_20 >= '\u0000' && LA53_20 <= '\b')||(LA53_20 >= '\u000B' && LA53_20 <= '\f')||(LA53_20 >= '\u000E' && LA53_20 <= '\u001F')||(LA53_20 >= '$' && LA53_20 <= '&')||LA53_20==','||LA53_20=='.'||(LA53_20 >= '0' && LA53_20 <= '9')||(LA53_20 >= ';' && LA53_20 <= '>')||(LA53_20 >= '@' && LA53_20 <= 'N')||(LA53_20 >= 'P' && LA53_20 <= 'Z')||(LA53_20 >= '_' && LA53_20 <= 'z')||(LA53_20 >= '\u007F' && LA53_20 <= '\u2FFF')||(LA53_20 >= '\u3001' && LA53_20 <= '\u300B')||(LA53_20 >= '\u300D' && LA53_20 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_20=='\\') ) {s = 35;}

                        else if ( (LA53_20=='-') ) {s = 36;}

                        else if ( (LA53_20=='+') ) {s = 37;}

                        else if ( (LA53_20=='#') ) {s = 38;}

                        else if ( (LA53_20=='/') ) {s = 39;}

                        else if ( (LA53_20=='\'') ) {s = 40;}

                        else if ( (LA53_20=='*'||LA53_20=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 69 : 
                        int LA53_5 = input.LA(1);

                        s = -1;
                        if ( ((LA53_5 >= '\u0000' && LA53_5 <= '\uFFFF')) ) {s = 52;}

                        else s = 51;

                        if ( s>=0 ) return s;
                        break;

                    case 70 : 
                        int LA53_0 = input.LA(1);

                        s = -1;
                        if ( (LA53_0=='r') ) {s = 1;}

                        else if ( (LA53_0=='c') ) {s = 2;}

                        else if ( (LA53_0=='a') ) {s = 3;}

                        else if ( ((LA53_0 >= '0' && LA53_0 <= '9')) ) {s = 4;}

                        else if ( (LA53_0=='/') ) {s = 5;}

                        else if ( (LA53_0=='(') ) {s = 6;}

                        else if ( (LA53_0==')') ) {s = 7;}

                        else if ( (LA53_0=='[') ) {s = 8;}

                        else if ( (LA53_0==']') ) {s = 9;}

                        else if ( (LA53_0==':') ) {s = 10;}

                        else if ( (LA53_0=='*') ) {s = 11;}

                        else if ( (LA53_0=='?') ) {s = 12;}

                        else if ( (LA53_0=='{') ) {s = 13;}

                        else if ( (LA53_0=='}') ) {s = 14;}

                        else if ( (LA53_0=='^') ) {s = 15;}

                        else if ( (LA53_0=='~') ) {s = 16;}

                        else if ( (LA53_0=='\"'||LA53_0=='\u300C') ) {s = 17;}

                        else if ( (LA53_0=='\'') ) {s = 18;}

                        else if ( (LA53_0=='#') ) {s = 19;}

                        else if ( (LA53_0=='T') ) {s = 20;}

                        else if ( (LA53_0=='A') ) {s = 21;}

                        else if ( (LA53_0=='&') ) {s = 22;}

                        else if ( (LA53_0=='+') ) {s = 23;}

                        else if ( (LA53_0=='O'||LA53_0=='o') ) {s = 24;}

                        else if ( (LA53_0=='|') ) {s = 25;}

                        else if ( (LA53_0=='n') ) {s = 26;}

                        else if ( (LA53_0=='-') ) {s = 27;}

                        else if ( (LA53_0=='N') ) {s = 28;}

                        else if ( ((LA53_0 >= '\t' && LA53_0 <= '\n')||LA53_0=='\r'||LA53_0==' '||LA53_0=='\u3000') ) {s = 29;}

                        else if ( ((LA53_0 >= '\u0000' && LA53_0 <= '\b')||(LA53_0 >= '\u000B' && LA53_0 <= '\f')||(LA53_0 >= '\u000E' && LA53_0 <= '\u001F')||(LA53_0 >= '$' && LA53_0 <= '%')||LA53_0==','||LA53_0=='.'||(LA53_0 >= ';' && LA53_0 <= '>')||LA53_0=='@'||(LA53_0 >= 'B' && LA53_0 <= 'M')||(LA53_0 >= 'P' && LA53_0 <= 'S')||(LA53_0 >= 'U' && LA53_0 <= 'Z')||(LA53_0 >= '_' && LA53_0 <= '`')||LA53_0=='b'||(LA53_0 >= 'd' && LA53_0 <= 'm')||(LA53_0 >= 'p' && LA53_0 <= 'q')||(LA53_0 >= 's' && LA53_0 <= 'z')||(LA53_0 >= '\u007F' && LA53_0 <= '\u2FFF')||(LA53_0 >= '\u3001' && LA53_0 <= '\u300B')||(LA53_0 >= '\u300D' && LA53_0 <= '\uFFFF')) ) {s = 30;}

                        else if ( (LA53_0=='\\') ) {s = 31;}

                        if ( s>=0 ) return s;
                        break;

                    case 71 : 
                        int LA53_21 = input.LA(1);

                        s = -1;
                        if ( (LA53_21=='N'||LA53_21=='n') ) {s = 45;}

                        else if ( ((LA53_21 >= '\u0000' && LA53_21 <= '\b')||(LA53_21 >= '\u000B' && LA53_21 <= '\f')||(LA53_21 >= '\u000E' && LA53_21 <= '\u001F')||(LA53_21 >= '$' && LA53_21 <= '&')||LA53_21==','||LA53_21=='.'||(LA53_21 >= '0' && LA53_21 <= '9')||(LA53_21 >= ';' && LA53_21 <= '>')||(LA53_21 >= '@' && LA53_21 <= 'M')||(LA53_21 >= 'O' && LA53_21 <= 'Z')||(LA53_21 >= '_' && LA53_21 <= 'm')||(LA53_21 >= 'o' && LA53_21 <= 'z')||(LA53_21 >= '\u007F' && LA53_21 <= '\u2FFF')||(LA53_21 >= '\u3001' && LA53_21 <= '\u300B')||(LA53_21 >= '\u300D' && LA53_21 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_21=='\\') ) {s = 35;}

                        else if ( (LA53_21=='-') ) {s = 36;}

                        else if ( (LA53_21=='+') ) {s = 37;}

                        else if ( (LA53_21=='#') ) {s = 38;}

                        else if ( (LA53_21=='/') ) {s = 39;}

                        else if ( (LA53_21=='\'') ) {s = 40;}

                        else if ( (LA53_21=='*'||LA53_21=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 72 : 
                        int LA53_76 = input.LA(1);

                        s = -1;
                        if ( (LA53_76=='/') ) {s = 91;}

                        else if ( ((LA53_76 >= '0' && LA53_76 <= '9')) ) {s = 92;}

                        else if ( (LA53_76=='.') ) {s = 93;}

                        else if ( (LA53_76=='-') ) {s = 94;}

                        else if ( ((LA53_76 >= '\u0000' && LA53_76 <= '\b')||(LA53_76 >= '\u000B' && LA53_76 <= '\f')||(LA53_76 >= '\u000E' && LA53_76 <= '\u001F')||(LA53_76 >= '$' && LA53_76 <= '&')||LA53_76==','||(LA53_76 >= ';' && LA53_76 <= '>')||(LA53_76 >= '@' && LA53_76 <= 'Z')||(LA53_76 >= '_' && LA53_76 <= 'z')||(LA53_76 >= '\u007F' && LA53_76 <= '\u2FFF')||(LA53_76 >= '\u3001' && LA53_76 <= '\u300B')||(LA53_76 >= '\u300D' && LA53_76 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_76=='\\') ) {s = 35;}

                        else if ( (LA53_76=='+') ) {s = 37;}

                        else if ( (LA53_76=='#') ) {s = 38;}

                        else if ( (LA53_76=='\'') ) {s = 40;}

                        else if ( (LA53_76=='*'||LA53_76=='?') ) {s = 41;}

                        else s = 48;

                        if ( s>=0 ) return s;
                        break;

                    case 73 : 
                        int LA53_115 = input.LA(1);

                        s = -1;
                        if ( ((LA53_115 >= '0' && LA53_115 <= '9')) ) {s = 122;}

                        else if ( ((LA53_115 >= '\u0000' && LA53_115 <= '\b')||(LA53_115 >= '\u000B' && LA53_115 <= '\f')||(LA53_115 >= '\u000E' && LA53_115 <= '\u001F')||(LA53_115 >= '$' && LA53_115 <= '&')||LA53_115==','||LA53_115=='.'||(LA53_115 >= ';' && LA53_115 <= '>')||(LA53_115 >= '@' && LA53_115 <= 'Z')||(LA53_115 >= '_' && LA53_115 <= 'z')||(LA53_115 >= '\u007F' && LA53_115 <= '\u2FFF')||(LA53_115 >= '\u3001' && LA53_115 <= '\u300B')||(LA53_115 >= '\u300D' && LA53_115 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_115=='\\') ) {s = 35;}

                        else if ( (LA53_115=='-') ) {s = 36;}

                        else if ( (LA53_115=='+') ) {s = 37;}

                        else if ( (LA53_115=='#') ) {s = 38;}

                        else if ( (LA53_115=='/') ) {s = 39;}

                        else if ( (LA53_115=='\'') ) {s = 40;}

                        else if ( (LA53_115=='*'||LA53_115=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 74 : 
                        int LA53_3 = input.LA(1);

                        s = -1;
                        if ( (LA53_3=='r') ) {s = 44;}

                        else if ( (LA53_3=='N'||LA53_3=='n') ) {s = 45;}

                        else if ( ((LA53_3 >= '\u0000' && LA53_3 <= '\b')||(LA53_3 >= '\u000B' && LA53_3 <= '\f')||(LA53_3 >= '\u000E' && LA53_3 <= '\u001F')||(LA53_3 >= '$' && LA53_3 <= '&')||LA53_3==','||LA53_3=='.'||(LA53_3 >= '0' && LA53_3 <= '9')||(LA53_3 >= ';' && LA53_3 <= '>')||(LA53_3 >= '@' && LA53_3 <= 'M')||(LA53_3 >= 'O' && LA53_3 <= 'Z')||(LA53_3 >= '_' && LA53_3 <= 'm')||(LA53_3 >= 'o' && LA53_3 <= 'q')||(LA53_3 >= 's' && LA53_3 <= 'z')||(LA53_3 >= '\u007F' && LA53_3 <= '\u2FFF')||(LA53_3 >= '\u3001' && LA53_3 <= '\u300B')||(LA53_3 >= '\u300D' && LA53_3 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_3=='\\') ) {s = 35;}

                        else if ( (LA53_3=='-') ) {s = 36;}

                        else if ( (LA53_3=='+') ) {s = 37;}

                        else if ( (LA53_3=='#') ) {s = 38;}

                        else if ( (LA53_3=='/') ) {s = 39;}

                        else if ( (LA53_3=='\'') ) {s = 40;}

                        else if ( (LA53_3=='*'||LA53_3=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 75 : 
                        int LA53_128 = input.LA(1);

                        s = -1;
                        if ( ((LA53_128 >= '\u0000' && LA53_128 <= '\b')||(LA53_128 >= '\u000B' && LA53_128 <= '\f')||(LA53_128 >= '\u000E' && LA53_128 <= '\u001F')||(LA53_128 >= '$' && LA53_128 <= '&')||LA53_128==','||LA53_128=='.'||(LA53_128 >= '0' && LA53_128 <= '9')||(LA53_128 >= ';' && LA53_128 <= '>')||(LA53_128 >= '@' && LA53_128 <= 'Z')||(LA53_128 >= '_' && LA53_128 <= 'z')||(LA53_128 >= '\u007F' && LA53_128 <= '\u2FFF')||(LA53_128 >= '\u3001' && LA53_128 <= '\u300B')||(LA53_128 >= '\u300D' && LA53_128 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_128=='\\') ) {s = 35;}

                        else if ( (LA53_128=='-') ) {s = 36;}

                        else if ( (LA53_128=='+') ) {s = 37;}

                        else if ( (LA53_128=='#') ) {s = 38;}

                        else if ( (LA53_128=='/') ) {s = 39;}

                        else if ( (LA53_128=='\'') ) {s = 40;}

                        else if ( (LA53_128=='*'||LA53_128=='?') ) {s = 41;}

                        else s = 123;

                        if ( s>=0 ) return s;
                        break;

                    case 76 : 
                        int LA53_30 = input.LA(1);

                        s = -1;
                        if ( ((LA53_30 >= '\u0000' && LA53_30 <= '\b')||(LA53_30 >= '\u000B' && LA53_30 <= '\f')||(LA53_30 >= '\u000E' && LA53_30 <= '\u001F')||(LA53_30 >= '$' && LA53_30 <= '&')||LA53_30==','||LA53_30=='.'||(LA53_30 >= '0' && LA53_30 <= '9')||(LA53_30 >= ';' && LA53_30 <= '>')||(LA53_30 >= '@' && LA53_30 <= 'Z')||(LA53_30 >= '_' && LA53_30 <= 'z')||(LA53_30 >= '\u007F' && LA53_30 <= '\u2FFF')||(LA53_30 >= '\u3001' && LA53_30 <= '\u300B')||(LA53_30 >= '\u300D' && LA53_30 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_30=='\\') ) {s = 35;}

                        else if ( (LA53_30=='-') ) {s = 36;}

                        else if ( (LA53_30=='+') ) {s = 37;}

                        else if ( (LA53_30=='#') ) {s = 38;}

                        else if ( (LA53_30=='/') ) {s = 39;}

                        else if ( (LA53_30=='\'') ) {s = 40;}

                        else if ( (LA53_30=='*'||LA53_30=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 77 : 
                        int LA53_101 = input.LA(1);

                        s = -1;
                        if ( (LA53_101=='b') ) {s = 109;}

                        else if ( ((LA53_101 >= '\u0000' && LA53_101 <= '\b')||(LA53_101 >= '\u000B' && LA53_101 <= '\f')||(LA53_101 >= '\u000E' && LA53_101 <= '\u001F')||(LA53_101 >= '$' && LA53_101 <= '&')||LA53_101==','||LA53_101=='.'||(LA53_101 >= '0' && LA53_101 <= '9')||(LA53_101 >= ';' && LA53_101 <= '>')||(LA53_101 >= '@' && LA53_101 <= 'Z')||(LA53_101 >= '_' && LA53_101 <= 'a')||(LA53_101 >= 'c' && LA53_101 <= 'z')||(LA53_101 >= '\u007F' && LA53_101 <= '\u2FFF')||(LA53_101 >= '\u3001' && LA53_101 <= '\u300B')||(LA53_101 >= '\u300D' && LA53_101 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_101=='\\') ) {s = 35;}

                        else if ( (LA53_101=='-') ) {s = 36;}

                        else if ( (LA53_101=='+') ) {s = 37;}

                        else if ( (LA53_101=='#') ) {s = 38;}

                        else if ( (LA53_101=='/') ) {s = 39;}

                        else if ( (LA53_101=='\'') ) {s = 40;}

                        else if ( (LA53_101=='*'||LA53_101=='?') ) {s = 41;}

                        else s = 110;

                        if ( s>=0 ) return s;
                        break;

                    case 78 : 
                        int LA53_24 = input.LA(1);

                        s = -1;
                        if ( (LA53_24=='R'||LA53_24=='r') ) {s = 64;}

                        else if ( ((LA53_24 >= '\u0000' && LA53_24 <= '\b')||(LA53_24 >= '\u000B' && LA53_24 <= '\f')||(LA53_24 >= '\u000E' && LA53_24 <= '\u001F')||(LA53_24 >= '$' && LA53_24 <= '&')||LA53_24==','||LA53_24=='.'||(LA53_24 >= '0' && LA53_24 <= '9')||(LA53_24 >= ';' && LA53_24 <= '>')||(LA53_24 >= '@' && LA53_24 <= 'Q')||(LA53_24 >= 'S' && LA53_24 <= 'Z')||(LA53_24 >= '_' && LA53_24 <= 'q')||(LA53_24 >= 's' && LA53_24 <= 'z')||(LA53_24 >= '\u007F' && LA53_24 <= '\u2FFF')||(LA53_24 >= '\u3001' && LA53_24 <= '\u300B')||(LA53_24 >= '\u300D' && LA53_24 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_24=='\\') ) {s = 35;}

                        else if ( (LA53_24=='-') ) {s = 36;}

                        else if ( (LA53_24=='+') ) {s = 37;}

                        else if ( (LA53_24=='#') ) {s = 38;}

                        else if ( (LA53_24=='/') ) {s = 39;}

                        else if ( (LA53_24=='\'') ) {s = 40;}

                        else if ( (LA53_24=='*'||LA53_24=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 79 : 
                        int LA53_56 = input.LA(1);

                        s = -1;
                        if ( ((LA53_56 >= '\u0000' && LA53_56 <= '\uFFFF')) ) {s = 79;}

                        if ( s>=0 ) return s;
                        break;

                    case 80 : 
                        int LA53_60 = input.LA(1);

                        s = -1;
                        if ( ((LA53_60 >= '\u0000' && LA53_60 <= '\uFFFF')) ) {s = 81;}

                        if ( s>=0 ) return s;
                        break;

                    case 81 : 
                        int LA53_35 = input.LA(1);

                        s = -1;
                        if ( ((LA53_35 >= '\u0000' && LA53_35 <= '\uFFFF')) ) {s = 70;}

                        if ( s>=0 ) return s;
                        break;

                    case 82 : 
                        int LA53_31 = input.LA(1);

                        s = -1;
                        if ( ((LA53_31 >= '\u0000' && LA53_31 <= '\uFFFF')) ) {s = 68;}

                        if ( s>=0 ) return s;
                        break;

                    case 83 : 
                        int LA53_91 = input.LA(1);

                        s = -1;
                        if ( ((LA53_91 >= '0' && LA53_91 <= '9')) ) {s = 105;}

                        else if ( ((LA53_91 >= '\u0000' && LA53_91 <= '\b')||(LA53_91 >= '\u000B' && LA53_91 <= '\f')||(LA53_91 >= '\u000E' && LA53_91 <= '\u001F')||(LA53_91 >= '$' && LA53_91 <= '&')||LA53_91==','||LA53_91=='.'||(LA53_91 >= ';' && LA53_91 <= '>')||(LA53_91 >= '@' && LA53_91 <= 'Z')||(LA53_91 >= '_' && LA53_91 <= 'z')||(LA53_91 >= '\u007F' && LA53_91 <= '\u2FFF')||(LA53_91 >= '\u3001' && LA53_91 <= '\u300B')||(LA53_91 >= '\u300D' && LA53_91 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_91=='\\') ) {s = 35;}

                        else if ( (LA53_91=='-') ) {s = 36;}

                        else if ( (LA53_91=='+') ) {s = 37;}

                        else if ( (LA53_91=='#') ) {s = 38;}

                        else if ( (LA53_91=='/') ) {s = 39;}

                        else if ( (LA53_91=='\'') ) {s = 40;}

                        else if ( (LA53_91=='*'||LA53_91=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 84 : 
                        int LA53_96 = input.LA(1);

                        s = -1;
                        if ( (LA53_96=='.') ) {s = 93;}

                        else if ( (LA53_96=='-') ) {s = 94;}

                        else if ( ((LA53_96 >= '\u0000' && LA53_96 <= '\b')||(LA53_96 >= '\u000B' && LA53_96 <= '\f')||(LA53_96 >= '\u000E' && LA53_96 <= '\u001F')||(LA53_96 >= '$' && LA53_96 <= '&')||LA53_96==','||(LA53_96 >= '0' && LA53_96 <= '9')||(LA53_96 >= ';' && LA53_96 <= '>')||(LA53_96 >= '@' && LA53_96 <= 'Z')||(LA53_96 >= '_' && LA53_96 <= 'z')||(LA53_96 >= '\u007F' && LA53_96 <= '\u2FFF')||(LA53_96 >= '\u3001' && LA53_96 <= '\u300B')||(LA53_96 >= '\u300D' && LA53_96 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_96=='\\') ) {s = 35;}

                        else if ( (LA53_96=='/') ) {s = 97;}

                        else if ( (LA53_96=='+') ) {s = 37;}

                        else if ( (LA53_96=='#') ) {s = 38;}

                        else if ( (LA53_96=='\'') ) {s = 40;}

                        else if ( (LA53_96=='*'||LA53_96=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 85 : 
                        int LA53_65 = input.LA(1);

                        s = -1;
                        if ( (LA53_65=='T'||LA53_65=='t') ) {s = 84;}

                        else if ( ((LA53_65 >= '\u0000' && LA53_65 <= '\b')||(LA53_65 >= '\u000B' && LA53_65 <= '\f')||(LA53_65 >= '\u000E' && LA53_65 <= '\u001F')||(LA53_65 >= '$' && LA53_65 <= '&')||LA53_65==','||LA53_65=='.'||(LA53_65 >= '0' && LA53_65 <= '9')||(LA53_65 >= ';' && LA53_65 <= '>')||(LA53_65 >= '@' && LA53_65 <= 'S')||(LA53_65 >= 'U' && LA53_65 <= 'Z')||(LA53_65 >= '_' && LA53_65 <= 's')||(LA53_65 >= 'u' && LA53_65 <= 'z')||(LA53_65 >= '\u007F' && LA53_65 <= '\u2FFF')||(LA53_65 >= '\u3001' && LA53_65 <= '\u300B')||(LA53_65 >= '\u300D' && LA53_65 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_65=='\\') ) {s = 35;}

                        else if ( (LA53_65=='-') ) {s = 36;}

                        else if ( (LA53_65=='+') ) {s = 37;}

                        else if ( (LA53_65=='#') ) {s = 38;}

                        else if ( (LA53_65=='/') ) {s = 39;}

                        else if ( (LA53_65=='\'') ) {s = 40;}

                        else if ( (LA53_65=='*'||LA53_65=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 86 : 
                        int LA53_99 = input.LA(1);

                        s = -1;
                        if ( ((LA53_99 >= '\u0000' && LA53_99 <= '\b')||(LA53_99 >= '\u000B' && LA53_99 <= '\f')||(LA53_99 >= '\u000E' && LA53_99 <= '\u001F')||(LA53_99 >= '$' && LA53_99 <= '&')||LA53_99==','||LA53_99=='.'||(LA53_99 >= '0' && LA53_99 <= '9')||(LA53_99 >= ';' && LA53_99 <= '>')||(LA53_99 >= '@' && LA53_99 <= 'Z')||(LA53_99 >= '_' && LA53_99 <= 'z')||(LA53_99 >= '\u007F' && LA53_99 <= '\u2FFF')||(LA53_99 >= '\u3001' && LA53_99 <= '\u300B')||(LA53_99 >= '\u300D' && LA53_99 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_99=='\\') ) {s = 35;}

                        else if ( (LA53_99=='-') ) {s = 36;}

                        else if ( (LA53_99=='+') ) {s = 37;}

                        else if ( (LA53_99=='#') ) {s = 38;}

                        else if ( (LA53_99=='/') ) {s = 39;}

                        else if ( (LA53_99=='\'') ) {s = 40;}

                        else if ( (LA53_99=='*'||LA53_99=='?') ) {s = 41;}

                        else s = 67;

                        if ( s>=0 ) return s;
                        break;

                    case 87 : 
                        int LA53_124 = input.LA(1);

                        s = -1;
                        if ( ((LA53_124 >= '0' && LA53_124 <= '9')) ) {s = 128;}

                        else if ( ((LA53_124 >= '\u0000' && LA53_124 <= '\b')||(LA53_124 >= '\u000B' && LA53_124 <= '\f')||(LA53_124 >= '\u000E' && LA53_124 <= '\u001F')||(LA53_124 >= '$' && LA53_124 <= '&')||LA53_124==','||LA53_124=='.'||(LA53_124 >= ';' && LA53_124 <= '>')||(LA53_124 >= '@' && LA53_124 <= 'Z')||(LA53_124 >= '_' && LA53_124 <= 'z')||(LA53_124 >= '\u007F' && LA53_124 <= '\u2FFF')||(LA53_124 >= '\u3001' && LA53_124 <= '\u300B')||(LA53_124 >= '\u300D' && LA53_124 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_124=='\\') ) {s = 35;}

                        else if ( (LA53_124=='-') ) {s = 36;}

                        else if ( (LA53_124=='+') ) {s = 37;}

                        else if ( (LA53_124=='#') ) {s = 38;}

                        else if ( (LA53_124=='/') ) {s = 39;}

                        else if ( (LA53_124=='\'') ) {s = 40;}

                        else if ( (LA53_124=='*'||LA53_124=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 88 : 
                        int LA53_49 = input.LA(1);

                        s = -1;
                        if ( ((LA53_49 >= '0' && LA53_49 <= '9')) ) {s = 78;}

                        else if ( ((LA53_49 >= '\u0000' && LA53_49 <= '\b')||(LA53_49 >= '\u000B' && LA53_49 <= '\f')||(LA53_49 >= '\u000E' && LA53_49 <= '\u001F')||(LA53_49 >= '$' && LA53_49 <= '&')||LA53_49==','||LA53_49=='.'||(LA53_49 >= ';' && LA53_49 <= '>')||(LA53_49 >= '@' && LA53_49 <= 'Z')||(LA53_49 >= '_' && LA53_49 <= 'z')||(LA53_49 >= '\u007F' && LA53_49 <= '\u2FFF')||(LA53_49 >= '\u3001' && LA53_49 <= '\u300B')||(LA53_49 >= '\u300D' && LA53_49 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_49=='\\') ) {s = 35;}

                        else if ( (LA53_49=='-') ) {s = 36;}

                        else if ( (LA53_49=='+') ) {s = 37;}

                        else if ( (LA53_49=='#') ) {s = 38;}

                        else if ( (LA53_49=='/') ) {s = 39;}

                        else if ( (LA53_49=='\'') ) {s = 40;}

                        else if ( (LA53_49=='*'||LA53_49=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 89 : 
                        int LA53_85 = input.LA(1);

                        s = -1;
                        if ( (LA53_85=='R'||LA53_85=='r') ) {s = 99;}

                        else if ( ((LA53_85 >= '\u0000' && LA53_85 <= '\b')||(LA53_85 >= '\u000B' && LA53_85 <= '\f')||(LA53_85 >= '\u000E' && LA53_85 <= '\u001F')||(LA53_85 >= '$' && LA53_85 <= '&')||LA53_85==','||LA53_85=='.'||(LA53_85 >= '0' && LA53_85 <= '9')||(LA53_85 >= ';' && LA53_85 <= '>')||(LA53_85 >= '@' && LA53_85 <= 'Q')||(LA53_85 >= 'S' && LA53_85 <= 'Z')||(LA53_85 >= '_' && LA53_85 <= 'q')||(LA53_85 >= 's' && LA53_85 <= 'z')||(LA53_85 >= '\u007F' && LA53_85 <= '\u2FFF')||(LA53_85 >= '\u3001' && LA53_85 <= '\u300B')||(LA53_85 >= '\u300D' && LA53_85 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_85=='\\') ) {s = 35;}

                        else if ( (LA53_85=='-') ) {s = 36;}

                        else if ( (LA53_85=='+') ) {s = 37;}

                        else if ( (LA53_85=='#') ) {s = 38;}

                        else if ( (LA53_85=='/') ) {s = 39;}

                        else if ( (LA53_85=='\'') ) {s = 40;}

                        else if ( (LA53_85=='*'||LA53_85=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 90 : 
                        int LA53_114 = input.LA(1);

                        s = -1;
                        if ( (LA53_114=='.') ) {s = 113;}

                        else if ( ((LA53_114 >= '0' && LA53_114 <= '9')) ) {s = 121;}

                        else if ( ((LA53_114 >= '\u0000' && LA53_114 <= '\b')||(LA53_114 >= '\u000B' && LA53_114 <= '\f')||(LA53_114 >= '\u000E' && LA53_114 <= '\u001F')||(LA53_114 >= '$' && LA53_114 <= '&')||LA53_114==','||(LA53_114 >= ';' && LA53_114 <= '>')||(LA53_114 >= '@' && LA53_114 <= 'Z')||(LA53_114 >= '_' && LA53_114 <= 'z')||(LA53_114 >= '\u007F' && LA53_114 <= '\u2FFF')||(LA53_114 >= '\u3001' && LA53_114 <= '\u300B')||(LA53_114 >= '\u300D' && LA53_114 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_114=='\\') ) {s = 35;}

                        else if ( (LA53_114=='-') ) {s = 36;}

                        else if ( (LA53_114=='+') ) {s = 37;}

                        else if ( (LA53_114=='#') ) {s = 38;}

                        else if ( (LA53_114=='/') ) {s = 39;}

                        else if ( (LA53_114=='\'') ) {s = 40;}

                        else if ( (LA53_114=='*'||LA53_114=='?') ) {s = 41;}

                        else s = 112;

                        if ( s>=0 ) return s;
                        break;

                    case 91 : 
                        int LA53_66 = input.LA(1);

                        s = -1;
                        if ( (LA53_66=='A'||LA53_66=='a') ) {s = 85;}

                        else if ( ((LA53_66 >= '\u0000' && LA53_66 <= '\b')||(LA53_66 >= '\u000B' && LA53_66 <= '\f')||(LA53_66 >= '\u000E' && LA53_66 <= '\u001F')||(LA53_66 >= '$' && LA53_66 <= '&')||LA53_66==','||LA53_66=='.'||(LA53_66 >= '0' && LA53_66 <= '9')||(LA53_66 >= ';' && LA53_66 <= '>')||LA53_66=='@'||(LA53_66 >= 'B' && LA53_66 <= 'Z')||(LA53_66 >= '_' && LA53_66 <= '`')||(LA53_66 >= 'b' && LA53_66 <= 'z')||(LA53_66 >= '\u007F' && LA53_66 <= '\u2FFF')||(LA53_66 >= '\u3001' && LA53_66 <= '\u300B')||(LA53_66 >= '\u300D' && LA53_66 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_66=='\\') ) {s = 35;}

                        else if ( (LA53_66=='-') ) {s = 36;}

                        else if ( (LA53_66=='+') ) {s = 37;}

                        else if ( (LA53_66=='#') ) {s = 38;}

                        else if ( (LA53_66=='/') ) {s = 39;}

                        else if ( (LA53_66=='\'') ) {s = 40;}

                        else if ( (LA53_66=='*'||LA53_66=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 92 : 
                        int LA53_107 = input.LA(1);

                        s = -1;
                        if ( ((LA53_107 >= '0' && LA53_107 <= '9')) ) {s = 116;}

                        else if ( ((LA53_107 >= '\u0000' && LA53_107 <= '\b')||(LA53_107 >= '\u000B' && LA53_107 <= '\f')||(LA53_107 >= '\u000E' && LA53_107 <= '\u001F')||(LA53_107 >= '$' && LA53_107 <= '&')||LA53_107==','||LA53_107=='.'||(LA53_107 >= ';' && LA53_107 <= '>')||(LA53_107 >= '@' && LA53_107 <= 'Z')||(LA53_107 >= '_' && LA53_107 <= 'z')||(LA53_107 >= '\u007F' && LA53_107 <= '\u2FFF')||(LA53_107 >= '\u3001' && LA53_107 <= '\u300B')||(LA53_107 >= '\u300D' && LA53_107 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_107=='\\') ) {s = 35;}

                        else if ( (LA53_107=='-') ) {s = 36;}

                        else if ( (LA53_107=='+') ) {s = 37;}

                        else if ( (LA53_107=='#') ) {s = 38;}

                        else if ( (LA53_107=='/') ) {s = 39;}

                        else if ( (LA53_107=='\'') ) {s = 40;}

                        else if ( (LA53_107=='*'||LA53_107=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 93 : 
                        int LA53_94 = input.LA(1);

                        s = -1;
                        if ( ((LA53_94 >= '0' && LA53_94 <= '9')) ) {s = 107;}

                        else if ( ((LA53_94 >= '\u0000' && LA53_94 <= '\b')||(LA53_94 >= '\u000B' && LA53_94 <= '\f')||(LA53_94 >= '\u000E' && LA53_94 <= '\u001F')||(LA53_94 >= '$' && LA53_94 <= '&')||LA53_94==','||LA53_94=='.'||(LA53_94 >= ';' && LA53_94 <= '>')||(LA53_94 >= '@' && LA53_94 <= 'Z')||(LA53_94 >= '_' && LA53_94 <= 'z')||(LA53_94 >= '\u007F' && LA53_94 <= '\u2FFF')||(LA53_94 >= '\u3001' && LA53_94 <= '\u300B')||(LA53_94 >= '\u300D' && LA53_94 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_94=='\\') ) {s = 35;}

                        else if ( (LA53_94=='-') ) {s = 36;}

                        else if ( (LA53_94=='+') ) {s = 37;}

                        else if ( (LA53_94=='#') ) {s = 38;}

                        else if ( (LA53_94=='/') ) {s = 39;}

                        else if ( (LA53_94=='\'') ) {s = 40;}

                        else if ( (LA53_94=='*'||LA53_94=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 94 : 
                        int LA53_50 = input.LA(1);

                        s = -1;
                        if ( ((LA53_50 >= '0' && LA53_50 <= '9')) ) {s = 78;}

                        else if ( ((LA53_50 >= '\u0000' && LA53_50 <= '\b')||(LA53_50 >= '\u000B' && LA53_50 <= '\f')||(LA53_50 >= '\u000E' && LA53_50 <= '\u001F')||(LA53_50 >= '$' && LA53_50 <= '&')||LA53_50==','||LA53_50=='.'||(LA53_50 >= ';' && LA53_50 <= '>')||(LA53_50 >= '@' && LA53_50 <= 'Z')||(LA53_50 >= '_' && LA53_50 <= 'z')||(LA53_50 >= '\u007F' && LA53_50 <= '\u2FFF')||(LA53_50 >= '\u3001' && LA53_50 <= '\u300B')||(LA53_50 >= '\u300D' && LA53_50 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_50=='\\') ) {s = 35;}

                        else if ( (LA53_50=='-') ) {s = 36;}

                        else if ( (LA53_50=='+') ) {s = 37;}

                        else if ( (LA53_50=='#') ) {s = 38;}

                        else if ( (LA53_50=='/') ) {s = 39;}

                        else if ( (LA53_50=='\'') ) {s = 40;}

                        else if ( (LA53_50=='*'||LA53_50=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 95 : 
                        int LA53_81 = input.LA(1);

                        s = -1;
                        if ( (LA53_81=='\'') ) {s = 82;}

                        else if ( (LA53_81=='\\') ) {s = 60;}

                        else if ( ((LA53_81 >= '\u0000' && LA53_81 <= '&')||(LA53_81 >= '(' && LA53_81 <= ')')||(LA53_81 >= '+' && LA53_81 <= '>')||(LA53_81 >= '@' && LA53_81 <= '[')||(LA53_81 >= ']' && LA53_81 <= '\uFFFF')) ) {s = 61;}

                        else if ( (LA53_81=='*'||LA53_81=='?') ) {s = 58;}

                        if ( s>=0 ) return s;
                        break;

                    case 96 : 
                        int LA53_122 = input.LA(1);

                        s = -1;
                        if ( (LA53_122=='.') ) {s = 113;}

                        else if ( ((LA53_122 >= '0' && LA53_122 <= '9')) ) {s = 122;}

                        else if ( ((LA53_122 >= '\u0000' && LA53_122 <= '\b')||(LA53_122 >= '\u000B' && LA53_122 <= '\f')||(LA53_122 >= '\u000E' && LA53_122 <= '\u001F')||(LA53_122 >= '$' && LA53_122 <= '&')||LA53_122==','||(LA53_122 >= ';' && LA53_122 <= '>')||(LA53_122 >= '@' && LA53_122 <= 'Z')||(LA53_122 >= '_' && LA53_122 <= 'z')||(LA53_122 >= '\u007F' && LA53_122 <= '\u2FFF')||(LA53_122 >= '\u3001' && LA53_122 <= '\u300B')||(LA53_122 >= '\u300D' && LA53_122 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_122=='\\') ) {s = 35;}

                        else if ( (LA53_122=='-') ) {s = 36;}

                        else if ( (LA53_122=='+') ) {s = 37;}

                        else if ( (LA53_122=='#') ) {s = 38;}

                        else if ( (LA53_122=='/') ) {s = 39;}

                        else if ( (LA53_122=='\'') ) {s = 40;}

                        else if ( (LA53_122=='*'||LA53_122=='?') ) {s = 41;}

                        else s = 112;

                        if ( s>=0 ) return s;
                        break;

                    case 97 : 
                        int LA53_95 = input.LA(1);

                        s = -1;
                        if ( ((LA53_95 >= '0' && LA53_95 <= '9')) ) {s = 106;}

                        else if ( ((LA53_95 >= '\u0000' && LA53_95 <= '\b')||(LA53_95 >= '\u000B' && LA53_95 <= '\f')||(LA53_95 >= '\u000E' && LA53_95 <= '\u001F')||(LA53_95 >= '$' && LA53_95 <= '&')||LA53_95==','||LA53_95=='.'||(LA53_95 >= ';' && LA53_95 <= '>')||(LA53_95 >= '@' && LA53_95 <= 'Z')||(LA53_95 >= '_' && LA53_95 <= 'z')||(LA53_95 >= '\u007F' && LA53_95 <= '\u2FFF')||(LA53_95 >= '\u3001' && LA53_95 <= '\u300B')||(LA53_95 >= '\u300D' && LA53_95 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_95=='\\') ) {s = 35;}

                        else if ( (LA53_95=='-') ) {s = 36;}

                        else if ( (LA53_95=='+') ) {s = 37;}

                        else if ( (LA53_95=='#') ) {s = 38;}

                        else if ( (LA53_95=='/') ) {s = 39;}

                        else if ( (LA53_95=='\'') ) {s = 40;}

                        else if ( (LA53_95=='*'||LA53_95=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 98 : 
                        int LA53_45 = input.LA(1);

                        s = -1;
                        if ( (LA53_45=='D'||LA53_45=='d') ) {s = 75;}

                        else if ( ((LA53_45 >= '\u0000' && LA53_45 <= '\b')||(LA53_45 >= '\u000B' && LA53_45 <= '\f')||(LA53_45 >= '\u000E' && LA53_45 <= '\u001F')||(LA53_45 >= '$' && LA53_45 <= '&')||LA53_45==','||LA53_45=='.'||(LA53_45 >= '0' && LA53_45 <= '9')||(LA53_45 >= ';' && LA53_45 <= '>')||(LA53_45 >= '@' && LA53_45 <= 'C')||(LA53_45 >= 'E' && LA53_45 <= 'Z')||(LA53_45 >= '_' && LA53_45 <= 'c')||(LA53_45 >= 'e' && LA53_45 <= 'z')||(LA53_45 >= '\u007F' && LA53_45 <= '\u2FFF')||(LA53_45 >= '\u3001' && LA53_45 <= '\u300B')||(LA53_45 >= '\u300D' && LA53_45 <= '\uFFFF')) ) {s = 34;}

                        else if ( (LA53_45=='\\') ) {s = 35;}

                        else if ( (LA53_45=='-') ) {s = 36;}

                        else if ( (LA53_45=='+') ) {s = 37;}

                        else if ( (LA53_45=='#') ) {s = 38;}

                        else if ( (LA53_45=='/') ) {s = 39;}

                        else if ( (LA53_45=='\'') ) {s = 40;}

                        else if ( (LA53_45=='*'||LA53_45=='?') ) {s = 41;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;

                    case 99 : 
                        int LA53_79 = input.LA(1);

                        s = -1;
                        if ( (LA53_79=='\"'||LA53_79=='\u300C') ) {s = 80;}

                        else if ( (LA53_79=='\\') ) {s = 56;}

                        else if ( ((LA53_79 >= '\u0000' && LA53_79 <= '!')||(LA53_79 >= '#' && LA53_79 <= ')')||(LA53_79 >= '+' && LA53_79 <= '>')||(LA53_79 >= '@' && LA53_79 <= '[')||(LA53_79 >= ']' && LA53_79 <= '\u300B')||(LA53_79 >= '\u300D' && LA53_79 <= '\uFFFF')) ) {s = 57;}

                        else if ( (LA53_79=='*'||LA53_79=='?') ) {s = 58;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 53, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}