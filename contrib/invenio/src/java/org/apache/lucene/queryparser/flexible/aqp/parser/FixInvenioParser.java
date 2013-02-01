// $ANTLR 3.4 /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g 2013-01-31 18:09:50

   package org.apache.lucene.queryparser.flexible.aqp.parser;
   import java.util.regex.Pattern;
   import java.util.regex.Matcher;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class FixInvenioParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AMBIGUITY", "DQUOTE", "ESC_CHAR", "INT", "LPAREN", "PHRASE", "QPHRASE", "QREGEX", "REGEX", "RPAREN", "SAFE_TOKEN", "SLASH", "SPACE", "SQUOTE", "SUB_SUS", "SUSPICIOUS_TOKEN", "TOKEN", "WS"
    };

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
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public FixInvenioParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public FixInvenioParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return FixInvenioParser.tokenNames; }
    public String getGrammarFileName() { return "/dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g"; }


    	public StringBuffer corrected = new StringBuffer();
    	Pattern escapePattern = Pattern.compile("(?<!\\\\\\\\)([()+-:\\[\\]\\}\\{])");
    	public String correctSuspicious(String text) {
    		char[] charr = text.toCharArray();
    		int lBrack = 0;
    		int rBrack = 0;
    		
    		for (int i=0;i<charr.length;i++) {
    			char p = i > 1 ? charr[i-1] : ' ';
    		        char c = charr[i];
    			if (c == ')' && p != '\\') {
    				rBrack++;
    				continue;
    			}
    			if (c == '(' && p != '\\') {
    				lBrack++;
    				continue;
    			}
    		}
    		if (lBrack == rBrack) {
        			Matcher z = escapePattern.matcher(text);
            		text = z.replaceAll("\\\\$1");
        			
        		}
        		return "\u300c" + text + "\u300c";
    	}


    public static class mainQ_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "mainQ"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:69:1: mainQ : group ;
    public final FixInvenioParser.mainQ_return mainQ() throws RecognitionException {
        FixInvenioParser.mainQ_return retval = new FixInvenioParser.mainQ_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        FixInvenioParser.group_return group1 =null;



        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:69:7: ( group )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:70:2: group
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_group_in_mainQ88);
            group1=group();

            state._fsp--;

            adaptor.addChild(root_0, group1.getTree());

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "mainQ"


    public static class group_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "group"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:76:1: group : ( LPAREN group RPAREN | ( ( space )? token ( ( space )? token ( space )? )* ( LPAREN group RPAREN )? )+ );
    public final FixInvenioParser.group_return group() throws RecognitionException {
        FixInvenioParser.group_return retval = new FixInvenioParser.group_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token LPAREN2=null;
        Token RPAREN4=null;
        Token LPAREN10=null;
        Token RPAREN12=null;
        FixInvenioParser.group_return group3 =null;

        FixInvenioParser.space_return space5 =null;

        FixInvenioParser.token_return token6 =null;

        FixInvenioParser.space_return space7 =null;

        FixInvenioParser.token_return token8 =null;

        FixInvenioParser.space_return space9 =null;

        FixInvenioParser.group_return group11 =null;


        Object LPAREN2_tree=null;
        Object RPAREN4_tree=null;
        Object LPAREN10_tree=null;
        Object RPAREN12_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:76:8: ( LPAREN group RPAREN | ( ( space )? token ( ( space )? token ( space )? )* ( LPAREN group RPAREN )? )+ )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==LPAREN) ) {
                alt7=1;
            }
            else if ( (LA7_0==PHRASE||LA7_0==REGEX||LA7_0==SAFE_TOKEN||LA7_0==SUSPICIOUS_TOKEN||LA7_0==WS) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:77:8: LPAREN group RPAREN
                    {
                    root_0 = (Object)adaptor.nil();


                    LPAREN2=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_group120); 
                    LPAREN2_tree = 
                    (Object)adaptor.create(LPAREN2)
                    ;
                    adaptor.addChild(root_0, LPAREN2_tree);


                    pushFollow(FOLLOW_group_in_group122);
                    group3=group();

                    state._fsp--;

                    adaptor.addChild(root_0, group3.getTree());

                    RPAREN4=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_group124); 
                    RPAREN4_tree = 
                    (Object)adaptor.create(RPAREN4)
                    ;
                    adaptor.addChild(root_0, RPAREN4_tree);


                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:10: ( ( space )? token ( ( space )? token ( space )? )* ( LPAREN group RPAREN )? )+
                    {
                    root_0 = (Object)adaptor.nil();


                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:10: ( ( space )? token ( ( space )? token ( space )? )* ( LPAREN group RPAREN )? )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==PHRASE||LA6_0==REGEX||LA6_0==SAFE_TOKEN||LA6_0==SUSPICIOUS_TOKEN||LA6_0==WS) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:11: ( space )? token ( ( space )? token ( space )? )* ( LPAREN group RPAREN )?
                    	    {
                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:11: ( space )?
                    	    int alt1=2;
                    	    int LA1_0 = input.LA(1);

                    	    if ( (LA1_0==WS) ) {
                    	        alt1=1;
                    	    }
                    	    switch (alt1) {
                    	        case 1 :
                    	            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:11: space
                    	            {
                    	            pushFollow(FOLLOW_space_in_group136);
                    	            space5=space();

                    	            state._fsp--;

                    	            adaptor.addChild(root_0, space5.getTree());

                    	            }
                    	            break;

                    	    }


                    	    pushFollow(FOLLOW_token_in_group139);
                    	    token6=token();

                    	    state._fsp--;

                    	    adaptor.addChild(root_0, token6.getTree());

                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:24: ( ( space )? token ( space )? )*
                    	    loop4:
                    	    do {
                    	        int alt4=2;
                    	        switch ( input.LA(1) ) {
                    	        case WS:
                    	            {
                    	            switch ( input.LA(2) ) {
                    	            case SUSPICIOUS_TOKEN:
                    	                {
                    	                alt4=1;
                    	                }
                    	                break;
                    	            case SAFE_TOKEN:
                    	                {
                    	                alt4=1;
                    	                }
                    	                break;
                    	            case PHRASE:
                    	                {
                    	                alt4=1;
                    	                }
                    	                break;
                    	            case REGEX:
                    	                {
                    	                alt4=1;
                    	                }
                    	                break;

                    	            }

                    	            }
                    	            break;
                    	        case SUSPICIOUS_TOKEN:
                    	            {
                    	            alt4=1;
                    	            }
                    	            break;
                    	        case SAFE_TOKEN:
                    	            {
                    	            alt4=1;
                    	            }
                    	            break;
                    	        case PHRASE:
                    	            {
                    	            alt4=1;
                    	            }
                    	            break;
                    	        case REGEX:
                    	            {
                    	            alt4=1;
                    	            }
                    	            break;

                    	        }

                    	        switch (alt4) {
                    	    	case 1 :
                    	    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:25: ( space )? token ( space )?
                    	    	    {
                    	    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:25: ( space )?
                    	    	    int alt2=2;
                    	    	    int LA2_0 = input.LA(1);

                    	    	    if ( (LA2_0==WS) ) {
                    	    	        alt2=1;
                    	    	    }
                    	    	    switch (alt2) {
                    	    	        case 1 :
                    	    	            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:25: space
                    	    	            {
                    	    	            pushFollow(FOLLOW_space_in_group142);
                    	    	            space7=space();

                    	    	            state._fsp--;

                    	    	            adaptor.addChild(root_0, space7.getTree());

                    	    	            }
                    	    	            break;

                    	    	    }


                    	    	    pushFollow(FOLLOW_token_in_group145);
                    	    	    token8=token();

                    	    	    state._fsp--;

                    	    	    adaptor.addChild(root_0, token8.getTree());

                    	    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:38: ( space )?
                    	    	    int alt3=2;
                    	    	    int LA3_0 = input.LA(1);

                    	    	    if ( (LA3_0==WS) ) {
                    	    	        alt3=1;
                    	    	    }
                    	    	    switch (alt3) {
                    	    	        case 1 :
                    	    	            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:38: space
                    	    	            {
                    	    	            pushFollow(FOLLOW_space_in_group147);
                    	    	            space9=space();

                    	    	            state._fsp--;

                    	    	            adaptor.addChild(root_0, space9.getTree());

                    	    	            }
                    	    	            break;

                    	    	    }


                    	    	    }
                    	    	    break;

                    	    	default :
                    	    	    break loop4;
                    	        }
                    	    } while (true);


                    	    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:47: ( LPAREN group RPAREN )?
                    	    int alt5=2;
                    	    int LA5_0 = input.LA(1);

                    	    if ( (LA5_0==LPAREN) ) {
                    	        alt5=1;
                    	    }
                    	    switch (alt5) {
                    	        case 1 :
                    	            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:78:48: LPAREN group RPAREN
                    	            {
                    	            LPAREN10=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_group153); 
                    	            LPAREN10_tree = 
                    	            (Object)adaptor.create(LPAREN10)
                    	            ;
                    	            adaptor.addChild(root_0, LPAREN10_tree);


                    	            pushFollow(FOLLOW_group_in_group155);
                    	            group11=group();

                    	            state._fsp--;

                    	            adaptor.addChild(root_0, group11.getTree());

                    	            RPAREN12=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_group157); 
                    	            RPAREN12_tree = 
                    	            (Object)adaptor.create(RPAREN12)
                    	            ;
                    	            adaptor.addChild(root_0, RPAREN12_tree);


                    	            }
                    	            break;

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


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "group"


    public static class token_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "token"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:82:1: token : ( suspicious -> ^( AMBIGUITY suspicious ) | safe -> ^( TOKEN safe ) | phrase -> ^( QPHRASE phrase ) | regex -> ^( QREGEX regex ) ) ;
    public final FixInvenioParser.token_return token() throws RecognitionException {
        FixInvenioParser.token_return retval = new FixInvenioParser.token_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        FixInvenioParser.suspicious_return suspicious13 =null;

        FixInvenioParser.safe_return safe14 =null;

        FixInvenioParser.phrase_return phrase15 =null;

        FixInvenioParser.regex_return regex16 =null;


        RewriteRuleSubtreeStream stream_suspicious=new RewriteRuleSubtreeStream(adaptor,"rule suspicious");
        RewriteRuleSubtreeStream stream_safe=new RewriteRuleSubtreeStream(adaptor,"rule safe");
        RewriteRuleSubtreeStream stream_regex=new RewriteRuleSubtreeStream(adaptor,"rule regex");
        RewriteRuleSubtreeStream stream_phrase=new RewriteRuleSubtreeStream(adaptor,"rule phrase");
        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:82:7: ( ( suspicious -> ^( AMBIGUITY suspicious ) | safe -> ^( TOKEN safe ) | phrase -> ^( QPHRASE phrase ) | regex -> ^( QREGEX regex ) ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:83:2: ( suspicious -> ^( AMBIGUITY suspicious ) | safe -> ^( TOKEN safe ) | phrase -> ^( QPHRASE phrase ) | regex -> ^( QREGEX regex ) )
            {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:83:2: ( suspicious -> ^( AMBIGUITY suspicious ) | safe -> ^( TOKEN safe ) | phrase -> ^( QPHRASE phrase ) | regex -> ^( QREGEX regex ) )
            int alt8=4;
            switch ( input.LA(1) ) {
            case SUSPICIOUS_TOKEN:
                {
                alt8=1;
                }
                break;
            case SAFE_TOKEN:
                {
                alt8=2;
                }
                break;
            case PHRASE:
                {
                alt8=3;
                }
                break;
            case REGEX:
                {
                alt8=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }

            switch (alt8) {
                case 1 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:83:4: suspicious
                    {
                    pushFollow(FOLLOW_suspicious_in_token192);
                    suspicious13=suspicious();

                    state._fsp--;

                    stream_suspicious.add(suspicious13.getTree());

                    corrected.append(correctSuspicious(input.toString(retval.start,input.LT(-1))));

                    // AST REWRITE
                    // elements: suspicious
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 83:67: -> ^( AMBIGUITY suspicious )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:83:70: ^( AMBIGUITY suspicious )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(AMBIGUITY, "AMBIGUITY")
                        , root_1);

                        adaptor.addChild(root_1, stream_suspicious.nextTree());

                        adaptor.addChild(root_1, new CommonTree(new CommonToken(AMBIGUITY, correctSuspicious(input.toString(retval.start,input.LT(-1))))));

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:84:4: safe
                    {
                    pushFollow(FOLLOW_safe_in_token209);
                    safe14=safe();

                    state._fsp--;

                    stream_safe.add(safe14.getTree());

                    corrected.append(input.toString(retval.start,input.LT(-1)));

                    // AST REWRITE
                    // elements: safe
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 84:42: -> ^( TOKEN safe )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:84:45: ^( TOKEN safe )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(TOKEN, "TOKEN")
                        , root_1);

                        adaptor.addChild(root_1, stream_safe.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 3 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:85:4: phrase
                    {
                    pushFollow(FOLLOW_phrase_in_token224);
                    phrase15=phrase();

                    state._fsp--;

                    stream_phrase.add(phrase15.getTree());

                    corrected.append(input.toString(retval.start,input.LT(-1)));

                    // AST REWRITE
                    // elements: phrase
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 85:44: -> ^( QPHRASE phrase )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:85:47: ^( QPHRASE phrase )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QPHRASE, "QPHRASE")
                        , root_1);

                        adaptor.addChild(root_1, stream_phrase.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 4 :
                    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:86:4: regex
                    {
                    pushFollow(FOLLOW_regex_in_token239);
                    regex16=regex();

                    state._fsp--;

                    stream_regex.add(regex16.getTree());

                    corrected.append(input.toString(retval.start,input.LT(-1)));

                    // AST REWRITE
                    // elements: regex
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 86:43: -> ^( QREGEX regex )
                    {
                        // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:86:46: ^( QREGEX regex )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(QREGEX, "QREGEX")
                        , root_1);

                        adaptor.addChild(root_1, stream_regex.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "token"


    public static class suspicious_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "suspicious"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:90:1: suspicious : SUSPICIOUS_TOKEN ;
    public final FixInvenioParser.suspicious_return suspicious() throws RecognitionException {
        FixInvenioParser.suspicious_return retval = new FixInvenioParser.suspicious_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token SUSPICIOUS_TOKEN17=null;

        Object SUSPICIOUS_TOKEN17_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:91:2: ( SUSPICIOUS_TOKEN )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:92:2: SUSPICIOUS_TOKEN
            {
            root_0 = (Object)adaptor.nil();


            SUSPICIOUS_TOKEN17=(Token)match(input,SUSPICIOUS_TOKEN,FOLLOW_SUSPICIOUS_TOKEN_in_suspicious266); 
            SUSPICIOUS_TOKEN17_tree = 
            (Object)adaptor.create(SUSPICIOUS_TOKEN17)
            ;
            adaptor.addChild(root_0, SUSPICIOUS_TOKEN17_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "suspicious"


    public static class safe_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "safe"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:95:1: safe : SAFE_TOKEN ;
    public final FixInvenioParser.safe_return safe() throws RecognitionException {
        FixInvenioParser.safe_return retval = new FixInvenioParser.safe_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token SAFE_TOKEN18=null;

        Object SAFE_TOKEN18_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:95:5: ( SAFE_TOKEN )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:96:2: SAFE_TOKEN
            {
            root_0 = (Object)adaptor.nil();


            SAFE_TOKEN18=(Token)match(input,SAFE_TOKEN,FOLLOW_SAFE_TOKEN_in_safe277); 
            SAFE_TOKEN18_tree = 
            (Object)adaptor.create(SAFE_TOKEN18)
            ;
            adaptor.addChild(root_0, SAFE_TOKEN18_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "safe"


    public static class phrase_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "phrase"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:98:1: phrase : PHRASE ;
    public final FixInvenioParser.phrase_return phrase() throws RecognitionException {
        FixInvenioParser.phrase_return retval = new FixInvenioParser.phrase_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token PHRASE19=null;

        Object PHRASE19_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:98:8: ( PHRASE )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:99:2: PHRASE
            {
            root_0 = (Object)adaptor.nil();


            PHRASE19=(Token)match(input,PHRASE,FOLLOW_PHRASE_in_phrase288); 
            PHRASE19_tree = 
            (Object)adaptor.create(PHRASE19)
            ;
            adaptor.addChild(root_0, PHRASE19_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "phrase"


    public static class regex_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "regex"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:101:1: regex : REGEX ;
    public final FixInvenioParser.regex_return regex() throws RecognitionException {
        FixInvenioParser.regex_return retval = new FixInvenioParser.regex_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token REGEX20=null;

        Object REGEX20_tree=null;

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:101:7: ( REGEX )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:102:2: REGEX
            {
            root_0 = (Object)adaptor.nil();


            REGEX20=(Token)match(input,REGEX,FOLLOW_REGEX_in_regex298); 
            REGEX20_tree = 
            (Object)adaptor.create(REGEX20)
            ;
            adaptor.addChild(root_0, REGEX20_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "regex"


    public static class space_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "space"
    // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:104:1: space : WS -> ^( SPACE WS ) ;
    public final FixInvenioParser.space_return space() throws RecognitionException {
        FixInvenioParser.space_return retval = new FixInvenioParser.space_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token WS21=null;

        Object WS21_tree=null;
        RewriteRuleTokenStream stream_WS=new RewriteRuleTokenStream(adaptor,"token WS");

        try {
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:104:7: ( WS -> ^( SPACE WS ) )
            // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:105:2: WS
            {
            WS21=(Token)match(input,WS,FOLLOW_WS_in_space309);  
            stream_WS.add(WS21);


            corrected.append(input.toString(retval.start,input.LT(-1)));

            // AST REWRITE
            // elements: WS
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 105:38: -> ^( SPACE WS )
            {
                // /dvt/workspace/montysolr/contrib/invenio/grammars/FixInvenio.g:105:41: ^( SPACE WS )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(SPACE, "SPACE")
                , root_1);

                adaptor.addChild(root_1, 
                stream_WS.nextNode()
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "space"

    // Delegated rules


 

    public static final BitSet FOLLOW_group_in_mainQ88 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_group120 = new BitSet(new long[]{0x0000000000285300L});
    public static final BitSet FOLLOW_group_in_group122 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_group124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_space_in_group136 = new BitSet(new long[]{0x0000000000085200L});
    public static final BitSet FOLLOW_token_in_group139 = new BitSet(new long[]{0x0000000000285302L});
    public static final BitSet FOLLOW_space_in_group142 = new BitSet(new long[]{0x0000000000085200L});
    public static final BitSet FOLLOW_token_in_group145 = new BitSet(new long[]{0x0000000000285302L});
    public static final BitSet FOLLOW_space_in_group147 = new BitSet(new long[]{0x0000000000285302L});
    public static final BitSet FOLLOW_LPAREN_in_group153 = new BitSet(new long[]{0x0000000000285300L});
    public static final BitSet FOLLOW_group_in_group155 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_group157 = new BitSet(new long[]{0x0000000000285202L});
    public static final BitSet FOLLOW_suspicious_in_token192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_safe_in_token209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_phrase_in_token224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_regex_in_token239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUSPICIOUS_TOKEN_in_suspicious266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SAFE_TOKEN_in_safe277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PHRASE_in_phrase288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REGEX_in_regex298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_space309 = new BitSet(new long[]{0x0000000000000002L});

}