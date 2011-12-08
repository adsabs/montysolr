grammar FixInvenio;

options {
  language = Java;
  output = AST;
}

/*
This is a small grammar that helps to deal with some pathological
cases, such as e(+)e(-)
*/

tokens {
  AMBIGUITY;
  QREGEX;
  QPHRASE;
}

@header{
   package org.apache.lucene.queryParser.aqp.parser;
}
@lexer::header {
   package org.apache.lucene.queryParser.aqp.parser;
}

@members {
	public StringBuffer corrected = new StringBuffer();
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
			text= text.replaceAll("(?!\\\\)([()])", "\\\\$1");
		}
		return text;
	}
}


mainQ : 
	space*  token+
	(space token+)* space*
	{
	System.out.print(corrected);
	}
	;
	

token	:
	( suspicious {corrected.append(correctSuspicious($token.text));} -> ^(AMBIGUITY[$suspicious.text])
	| safe {corrected.append($token.text);} -> safe 
	| phrase {corrected.append($token.text);} -> phrase
	| regex {corrected.append($token.text);} -> regex
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
	WS {corrected.append($space.text);}
	;
/* ================================================================
 * =                     LEXER                                    =
 * ================================================================
 */

fragment SLASH   : '/';

fragment LPAREN  : '(';

fragment RPAREN  : ')';


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
	:(SAFE_TOKEN | ')' | '(' | '/')+; 

		