grammar StandardLuceneGrammar;

//
//  This is a re-implementation of the standard lucene syntax with ANTLR3
//   http://lucene.apache.org/core/4_3_0/queryparser/index.html
//   
//   The query syntax is complete and supports the same features as the 
//   original parser written in JCC. The advantage of this grammar is that it
//   is 'pluggable' into Lucene's modern flexible parser, so that you can 
//   add custom logic on top of the 'rigid' query syntax. Besides...the grammar
//   is not that 'rigid' - you can modify the grammar and easily recompile.
//   
//   # run this commad inside antlrqueryparser
//   
//      $ ant generate-antlr -Dgrammar=MyNewGrammar
//   
//   # or if you want to test things, do:
//   
//      $ ant try-view -Dgrammar=MyNewGrammar -Dquery="foo AND bar"
//   
//   
//   Implementation note: I have tried hard to avoid putting language specific details
//   into the grammar, unfortunately this was not always possible. But it is kept
//   at minimum. You can generate parser code in your language of choice
//   if you change the following:
//   
//   options  : 
//     language=<your-target-language-supported-by-antlr3>
//     superClass= the default is to subclass 'UnforgivingParser', this java class
//                         lives in the package oal.queryparser.flixible.aqp.parser
//                         and its purpose is to bark everytime when an exception
//                         happens (otherwise, ANTLR tries to recover from some situations
//                         -- you may want to remove this definition, or add your own
//                         error recovery logic there)
//
//   @header:  this adds the java declaration to the generated parser file,
//                    feel free to remove (if you want to test the grammar using
//                    ANTLRWorks, you want to remove it)
//   @lexer::header: dtto but for lexer
//   @lexer::members: again, necessary for being strict and prevent error 
//                                recovery, but this applies only to lexer errors.
//
//  One last note - if you want to implement your own error recovery, have a look
//  at the generated java class 
//       
//      oal.queryparser.flixible.aqp.parser.<GrammarName>SyntaxParser.java
//
//  There we are raising parse exception as well
//


options {
  language = Java;
  output = AST;
  superClass = UnforgivingParser;
}

tokens {
  OPERATOR;
  ATOM;
  MODIFIER;
  TMODIFIER;
  CLAUSE;
  FIELD;
  FUZZY;
  BOOST;
  QNORMAL;
  QPHRASE;
  QPHRASETRUNC;
  QTRUNCATED;
  QRANGEIN;
  QRANGEEX;
  QANYTHING;
  QDATE;
}


// java-specific and error recovery-unfriendly details.... 

@header{
  package org.apache.lucene.queryparser.flexible.aqp.parser;
}
@lexer::header {
  package org.apache.lucene.queryparser.flexible.aqp.parser;
}
// this is for exceptions on lexer level - ANTLRv3 does not seem to have a better way
@lexer::members {
	public void recover(RecognitionException re) {
		// throw unchecked exception
	  throw new RuntimeException(re);
	}
}

// ...below this point, language agnostic EBNF grammar lives.





mainQ : 
	clauseOr+ EOF -> ^(OPERATOR["DEFOP"] clauseOr+)
	;
   
  
clauseOr
  : (first=clauseAnd -> $first) (or others=clauseAnd -> ^(OPERATOR["OR"] clauseAnd+ ))*
  ;

clauseAnd
  : (first=clauseNot  -> $first) (and others=clauseNot -> ^(OPERATOR["AND"] clauseNot+ ))*
  ;
  
clauseNot
  : (first=clauseNear -> $first) (not others=clauseNear -> ^(OPERATOR["NOT"] clauseNear+ ))*
  ;
  
clauseNear
  : (first=clauseBasic -> $first) (near others=clauseBasic -> ^(near clauseBasic+) )* 
  ;
  
clauseBasic
	:
	(modifier LPAREN clauseOr+ RPAREN )=> modifier? LPAREN clauseOr+ RPAREN term_modifier? 
	 -> ^(CLAUSE ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(OPERATOR["DEFOP"] clauseOr+)))) // Default operator
	| (LPAREN clauseOr+ RPAREN term_modifier)=> modifier? LPAREN clauseOr+ RPAREN term_modifier? 
	 -> ^(CLAUSE ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(OPERATOR["DEFOP"] clauseOr+)))) // Default operator
	| (LPAREN  )=> LPAREN clauseOr+ RPAREN
	 -> clauseOr+
	| atom
	;
    

atom   
	: 
	modifier? field multi_value term_modifier?
	 -> ^(CLAUSE ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(FIELD field multi_value))))
	| modifier? field? value term_modifier? 
	 -> ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(FIELD field? value)))
	| modifier? (STAR COLON)? STAR 
	 -> ^(MODIFIER modifier? ^(QANYTHING STAR["*"]))
	;
   

field	
	:	
	TERM_NORMAL COLON -> TERM_NORMAL
	;

value  
	: 
	range_term_in -> ^(QRANGEIN range_term_in)
	| range_term_ex -> ^(QRANGEEX range_term_ex) 
	| normal -> ^(QNORMAL normal)	
	| truncated -> ^(QTRUNCATED truncated)	
	| quoted -> ^(QPHRASE quoted)
	| quoted_truncated -> ^(QPHRASETRUNC quoted_truncated)
	| QMARK -> ^(QTRUNCATED QMARK)
  	;

	

range_term_in
	:	
       LBRACK
       (a=range_value -> range_value ^(QANYTHING QANYTHING["*"]))
       ( TO? b=range_value -> $a $b? )?
       RBRACK
	;


range_term_ex
	:	
       LCURLY
       ( a=range_value -> range_value ^(QANYTHING QANYTHING["*"]))
       ( TO? b=range_value -> $a $b? )?
       RCURLY
	;	

range_value
	:	
	truncated -> ^(QTRUNCATED truncated)
	| quoted -> ^(QPHRASE quoted)
	| quoted_truncated -> ^(QPHRASETRUNC quoted_truncated)
	| date -> ^(QNORMAL date)
	| normal -> ^(QNORMAL normal)	
	| STAR -> ^(QANYTHING STAR)
	;

multi_value
	: 
	LPAREN multiClause RPAREN -> multiClause
	;



multiClause	
	:
	
	//m:(a b NEAR c OR d OR e)
	
	// without duplicating the rules (but it allows recursion)
	clauseOr+ -> ^(OPERATOR["DEFOP"] clauseOr+)
	
	// allows only limited set of operations
	//multiDefault
	
	// this is also working, but i want operator precedence
	//multiClause:
	//(mterm+ -> mterm+)
	//(op=operator rhs=fclause -> ^(OPERATOR ^(OPERATOR["DEFOP"] $mclause) $rhs))?
	//;
	//flause:mclause;
	;

multiDefault
	:	
	multiOr+ -> ^(OPERATOR["DEFOP"] multiOr+)
	;

multiOr	
	:	
	(first=multiAnd  -> $first) (or others=multiAnd-> ^(OPERATOR["OR"] multiAnd+ ))*
	;	
		
multiAnd
	:	
	(first=multiNot  -> $first) (and others=multiNot -> ^(OPERATOR["AND"] multiNot+ ))*
	;	

multiNot	
	:	
	(first=multiNear  -> $first) (not others=multiNear-> ^(OPERATOR["NOT"] multiNear+ ))*
	;	

multiNear	
	:	
	(first=multiBasic  -> $first) (near others=multiBasic-> ^(near multiBasic+ ))*
	;	


multiBasic
	:	
	mterm
	;
		
mterm	
	:	
	modifier? value -> ^(MODIFIER modifier? value)
	;
	

normal	
	:	
	TERM_NORMAL
	| NUMBER
	;	

	

			
truncated
	:	
	TERM_TRUNCATED
	; 


quoted_truncated
	:	
	PHRASE_ANYTHING
	;

quoted	:	
	PHRASE
	;



	
operator: (
	AND -> OPERATOR["AND"]
	| OR -> OPERATOR["OR"]
	| NOT -> OPERATOR["NOT"]
	| NEAR -> OPERATOR["NEAR"]
	);

modifier: 
	PLUS -> PLUS["+"]
	| MINUS -> MINUS["-"];


/*
This terribly convoluted grammar is here because of weird AST rewrite rules
and because we need to allow for default value when TILDE is not followed by
anything

This grammar has problem with following
	:	term^4~ 9999
	where 999 is another term, not a fuzzy value
*/
term_modifier	:	
	TILDE CARAT? -> ^(BOOST CARAT?) ^(FUZZY TILDE) 
	| CARAT TILDE? -> ^(BOOST CARAT) ^(FUZZY TILDE?)
/*
	// first alternative
	(
	  (CARAT b=NUMBER -> ^(BOOST $b) ^(FUZZY )
	 ) 
	( //syntactic predicate
	 (TILDE NUMBER )=>TILDE f=NUMBER -> ^(BOOST $b) ^(FUZZY $f)
	 | TILDE -> ^(BOOST $b) ^(FUZZY NUMBER["0.5"])
	 )* // set the default value
	
	)
	// second alternative [only ~ | ~NUMBER]
	| 
	  (TILDE -> ^(BOOST) ^(FUZZY NUMBER["0.5"])) // set the default value
	  ((~(WS|TILDE|CARAT))=>f=NUMBER -> ^(BOOST) ^(FUZZY $f?) )* //replace the default but '~' must not be followed by WS
*/	
	;


boost	:
	(CARAT -> ^(BOOST NUMBER["DEF"])) // set the default value
	(NUMBER -> ^(BOOST NUMBER))? //replace the default with user input
	;

fuzzy	:
	(TILDE -> ^(FUZZY NUMBER["DEF"])) // set the default value
	(NUMBER -> ^(FUZZY NUMBER))? //replace the default with user input
	;

not	:	
	(AND NOT)=> AND NOT
	| NOT
	;
	
and 	:	
	AND
	;
	
or 	:	
	OR
	;		

near	:	
	(NEAR -> ^(OPERATOR["NEAR"]) )
	('/' b=NUMBER -> ^(OPERATOR["NEAR:" + $b.getText()]) )?
	;

date	:	
	//a=NUMBER '/' b=NUMBER '/' c=NUMBER -> ^(QDATE $a $b $c)
	DATE_TOKEN
	;

/* ================================================================
 * =                     LEXER                                    =
 * ================================================================
 */



LPAREN  : '(';

RPAREN  : ')';

LBRACK  : '[';

RBRACK  : ']';

COLON   : ':' ;  //this must NOT be fragment

PLUS  : '+' ;

MINUS : ('-'|'\!');

STAR  : '*' ;

QMARK  : '?'+ ;

fragment VBAR  : '|' ;

fragment AMPER : '&' ;

LCURLY  : '{' ;

RCURLY  : '}' ;

CARAT : '^' (INT+ ('.' INT+)?)?;

TILDE : '~' (INT+ ('.' INT+)?)?;

DQUOTE	
	:	'\"';

SQUOTE
	:	'\'';




TO	:	'TO';

/* We want to be case insensitive */
AND   : (('a' | 'A') ('n' | 'N') ('d' | 'D') | (AMPER AMPER?)) ;
OR  : (('o' | 'O') ('r' | 'R') | (VBAR VBAR?));
NOT   : ('n' | 'N') ('o' | 'O') ('t' | 'T');
NEAR  : (('n' | 'N') ('e' | 'E') ('a' | 'A') ('r' | 'R') | 'n') ;


WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        | '\u3000'
        ) 
        {$channel=HIDDEN;}
    ;

      
/*	
fragment TERM_CHAR  : 
     ~(' ' | '\t' | '\n' | '\r' | '\u3000'
      | '\\' | '\'' | '\"' 
      | '(' | ')' | '[' | ']' | '{' | '}'
      | '+' | '-' | '!' | ':' | '~' | '^' 
      | '*' | '|' | '&' | '?' | '\\\"' | '/'  //this line is not present in lucene StandardParser.jj
      );  	
*/


fragment INT: '0' .. '9';


fragment ESC_CHAR:  '\\' .; 


fragment TERM_START_CHAR
	:
	(~(' ' | '\t' | '\n' | '\r' | '\u3000'
	      | '\'' | '\"' 
	      | '(' | ')' | '[' | ']' | '{' | '}'
	      | '+' | '-' | '!' | ':' | '~' | '^' 
	      | '?' | '*' | '\\'
	      )
	 | ESC_CHAR );  	


fragment TERM_CHAR
	:	
	(TERM_START_CHAR | '-' | '+')
	;


NUMBER  
	: 
	INT+ ('.' INT+)?
	;

DATE_TOKEN
	:	
	INT INT? ('/'|'-'|'.') INT INT? ('/'|'-'|'.') INT INT (INT INT)?
	;

TERM_NORMAL
	: 
	TERM_START_CHAR ( TERM_CHAR )*
	;


TERM_TRUNCATED: 
	(STAR|QMARK) (TERM_CHAR+ (QMARK|STAR))+ (TERM_CHAR)*
	| TERM_START_CHAR (TERM_CHAR* (QMARK|STAR))+ (TERM_CHAR)*
	| (STAR|QMARK) TERM_CHAR+
	;


PHRASE	
	:	
	DQUOTE (ESC_CHAR|~('\"'|'\\'|'?'|'*'))+ DQUOTE
	;

PHRASE_ANYTHING	:	
	DQUOTE (ESC_CHAR|~('\"'|'\\'))+ DQUOTE
	;

