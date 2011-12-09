grammar FixInvenio;

options {
  language = Java;
  output = AST;
}

/*
This is a small grammar that helps to deal with some pathological
cases, such as e(+)e(-), it will escape special characters and 
have the results available in the public property 'corrected'

It also returns the token that can be used to change the query.
The 'suspicious' token is what should be inspected.

Basically, what we change are only groups of characters that happen
to be inside brackets without spaces (well, don't blame for this,
it is not my invention and don't consider it as a best solution).

*/

tokens {
  AMBIGUITY;
  QREGEX;
  QPHRASE;
  TOKEN;
  SPACE;
}

@header{
   package org.apache.lucene.queryParser.aqp.parser;
   import java.util.regex.Pattern;
   import java.util.regex.Matcher;
}
@lexer::header {
   package org.apache.lucene.queryParser.aqp.parser;
}

@members {
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
    		return text;
	}
}


mainQ : 
	group
	//{
	//System.out.print("corrected=>\n" + corrected + "\n<=\n");
	//}
	;

group	:	
	LPAREN group RPAREN
	| (space? token (space? token space?)* (LPAREN group RPAREN)?)+
	;	

token	:
	( suspicious {corrected.append(correctSuspicious($token.text));} -> ^(AMBIGUITY suspicious {new CommonTree(new CommonToken(AMBIGUITY, correctSuspicious($token.text)))})
	| safe {corrected.append($token.text);} -> ^(TOKEN safe)
	| phrase {corrected.append($token.text);} -> ^(QPHRASE phrase)
	| regex {corrected.append($token.text);} -> ^(QREGEX regex)
	)
	;
	
suspicious
	:	
	SUSPICIOUS_TOKEN
	;

safe:	
	SAFE_TOKEN
	;
phrase	:	
	PHRASE
	;
regex	:
	REGEX
	;
space	:	
	WS {corrected.append($space.text);} -> ^(SPACE WS)
	;
/* ================================================================
 * =                     LEXER                                    =
 * ================================================================
 */

fragment SLASH   : '/';

LPAREN  : '(';

RPAREN  : ')';


fragment DQUOTE	
	:	'\"';

fragment SQUOTE
	:	'\'';


WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        | '\u3000'
        )+
    ;

fragment INT: '0' .. '9';


fragment ESC_CHAR:  '\\' .; 



PHRASE	:	
	DQUOTE (ESC_CHAR|~('\"'|'\\'))+ DQUOTE
	| SQUOTE (ESC_CHAR|~('\''|'\\'|'\\\''))+ SQUOTE
	;

REGEX	
	:	
	SLASH (ESC_CHAR|~('\\'|'\\/'))+ SLASH
	;

SAFE_TOKEN
	:(~(' ' | '\t' | '\n' | '\r' | '\u3000'
	   | '\'' | '\"'| '\\' | '/' | ')' | '(')
	 | ESC_CHAR )+; 

SUSPICIOUS_TOKEN
	:
	(SAFE_TOKEN LPAREN SUB_SUS? RPAREN)+ SUB_SUS?
	;


	
fragment SUB_SUS
	:
	LPAREN SUB_SUS RPAREN
	| SAFE_TOKEN
	;		