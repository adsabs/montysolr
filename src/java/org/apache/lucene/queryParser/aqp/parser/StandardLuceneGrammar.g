grammar StandardLuceneGrammar;

options {
  language = Java;
  output = AST;
}

tokens {
  OPERATOR;
  ATOM;
  NUCLEUS;
  MULTIATOM;
  MULTIVALUE;
  MODIFIER;
  TMODIFIER;
  VALUE;
  CLAUSE;
  FIELD;
  FUZZY;
  ADDED;
  BOOST;
  QNORMAL;
  QPHRASE;
  QPHRASETRUNC;
  QTRUNCATED;
  QRANGEIN;
  QRANGEEX;
  QANYTHING;
}

@header{
   package org.apache.lucene.queryParser.aqp.parser;
}
@lexer::header {
   package org.apache.lucene.queryParser.aqp.parser;
}

mainQ : 
	clauseDefault+ -> ^(OPERATOR["DEFOP"] clauseDefault+) // Default operator
	;
   
  
clauseDefault
  : (first=clauseStrongest -> $first) (OR others=clauseStrongest -> ^(OPERATOR["OR"] clauseStrongest+ ))*
  ;

clauseStrongest
  : (first=clauseStrong  -> $first) (AND others=clauseStrong -> ^(OPERATOR["AND"] clauseStrong+ ))*
  ;
  
clauseStrong
  : (first=clauseWeak -> $first) (NOT others=clauseWeak -> ^(OPERATOR["NOT"] clauseWeak+ ))*
  ;
  
clauseWeak
  : (first=primaryClause -> $first) (near others=primaryClause -> ^(near primaryClause+) )* 
  ;
  
primaryClause
	: 
	
	(modifier LPAREN clauseDefault+ RPAREN )=> modifier? LPAREN clauseDefault+ RPAREN (CARAT NUMBER)? -> ^(CLAUSE ^(MODIFIER modifier?) ^(BOOST NUMBER?) ^(OPERATOR["DEFOP"] clauseDefault+) ) // Default operator
	| (LPAREN clauseDefault+ RPAREN CARAT NUMBER)=> modifier? LPAREN clauseDefault+ RPAREN (CARAT NUMBER)? -> ^(CLAUSE ^(MODIFIER modifier?) ^(BOOST NUMBER?) ^(OPERATOR["DEFOP"] clauseDefault+) ) // Default operator
	| (LPAREN)=> LPAREN clauseDefault+ RPAREN -> clauseDefault+
	| atom 
	;
    

atom   
	: 
	modifier? field multi_value term_modifier? -> ^(MULTIATOM ^(MODIFIER modifier?) ^(FIELD field) ^(MULTIVALUE multi_value) term_modifier?)
	| modifier? field? value -> ^(ATOM ^(MODIFIER modifier?) ^(NUCLEUS ^(FIELD field?) ^(VALUE value)))
	;
   
	   
field	
	:	
	TERM_NORMAL COLON -> TERM_NORMAL
	| STAR COLON -> STAR
	;

value  
	: 
	( 
	range_term_in -> ^(QRANGEIN range_term_in)
	| range_term_ex -> ^(QRANGEEX range_term_ex) 
	| normal -> ^(QNORMAL normal)
	| truncated -> ^(QTRUNCATED truncated)
	| quoted -> ^(QPHRASE quoted)
	| quoted_truncated -> ^(QPHRASETRUNC quoted_truncated)
	| STAR -> ^(QANYTHING STAR)
	)
	term_modifier? -> term_modifier? $value
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
	normal -> ^(QNORMAL normal)
	| truncated -> ^(QTRUNCATED truncated)
	| quoted -> ^(QPHRASE quoted)
	| quoted_truncated -> ^(QPHRASETRUNC quoted_truncated)
	| STAR -> ^(QANYTHING STAR)
	;

multi_value
	: 
	LPAREN mterm+ RPAREN -> mterm+
	;
	
	
mterm	
	:	
	(modifier? value -> ^(ATOM ^(MODIFIER modifier?) ^(NUCLEUS FIELD ^(VALUE value))))
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



	
operator: (AND | OR | NOT | NEAR);

modifier: (PLUS|MINUS);


/*
This terribly convoluted grammar is here because of weird AST rewrite rules
and because we need to allow for default value when TILDE is not followed by
anything

This grammar has problem with following
	:	term^4~ 9999
	where 999 is another term, not a fuzzy value
*/
term_modifier	:	
	// first alternative
	(
	  (CARAT b=NUMBER -> ^(TMODIFIER ^(BOOST $b) ^(FUZZY ))
	 ) 
	( //syntactic predicate
	 (TILDE NUMBER )=>TILDE f=NUMBER -> ^(TMODIFIER ^(BOOST $b) ^(FUZZY $f)) //
	 | TILDE -> ^(TMODIFIER ^(BOOST $b) ^(FUZZY NUMBER["0.5"]))
	 )* // set the default value
	
	)
	// second alternative [only ~ | ~NUMBER]
	| 
	  (TILDE -> ^(TMODIFIER ^(BOOST) ^(FUZZY NUMBER["0.5"])) ) // set the default value
	  ((~(WS|TILDE|CARAT))=>f=NUMBER -> ^(TMODIFIER ^(BOOST ) ^(FUZZY $f?)) )* //replace the default but '~' must not be followed by WS
	
	;

boost	:	
	CARAT NUMBER
	;

fuzzy	:	
	TILDE NUMBER
	;


near	:	
	(NEAR -> ^(OPERATOR["NEAR 5"]) )
	('/' b=NUMBER -> ^(OPERATOR["NEAR " + $b.getText()]) )?
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

MINUS : '-';

STAR  : '*' ;

QMARK  : '?' ;

fragment VBAR  : '|' ;

fragment AMPER : '&' ;

LCURLY  : '{' ;

RCURLY  : '}' ;

CARAT : '^';

TILDE : '~' ;

DQUOTE	
	:	'\"';

SQUOTE
	:	'\'';


ESC_CHAR:  '\\' .; 

TO	:	'TO';

/* We want to be case insensitive */
AND   : (('a' | 'A') ('n' | 'N') ('d' | 'D') | (AMPER AMPER?)) ;
OR  : (('o' | 'O') ('r' | 'R') | (VBAR VBAR?));
NOT   : (('n' | 'N') ('o' | 'O') ('t' | 'T') | '!');
NEAR  : (('n' | 'N') ('e' | 'E') ('a' | 'A') ('r' | 'R') | 'n') ;


WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) 
        {$channel=HIDDEN;}
    ;


fragment INT: '0' .. '9';

fragment NORMAL_CHAR  : ~(' ' | '\t' | '\n' | '\r'
      | '\\' | '\'' | '\"' 
      | '(' | ')' | '[' | ']' | '{' | '}'
      | '+' | '-' | '!' | ':' | '~' | '^' 
      | '*' | '|' | '&' | '?' | '\\\"' | '/'  //this line is not present in lucene StandardParser.jj
      );  	


NUMBER  
	: 
	INT+ ('.' INT+)?
	;


TERM_NORMAL
	: 
	( NORMAL_CHAR | ESC_CHAR) ( NORMAL_CHAR | ESC_CHAR)*
	;


TERM_TRUNCATED: 
	(NORMAL_CHAR | (STAR | QMARK))+
	//(STAR | QMARK)? NORMAL_CHAR+ ((STAR | QMARK) (NORMAL_CHAR+ (STAR | QMARK))?)?
	;


PHRASE	
	:	
	DQUOTE ~('\"'|'?'|'*')+ DQUOTE
	;

PHRASE_ANYTHING	:	
	DQUOTE ~('\"')+ DQUOTE
	;
