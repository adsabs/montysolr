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
  MULTITERM;
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
  QDATE;
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
  : (first=clauseStrongest -> $first) (or others=clauseStrongest -> ^(OPERATOR["OR"] clauseStrongest+ ))*
  ;

clauseStrongest
  : (first=clauseStrong  -> $first) (and others=clauseStrong -> ^(OPERATOR["AND"] clauseStrong+ ))*
  ;
  
clauseStrong
  : (first=clauseWeak -> $first) (not others=clauseWeak -> ^(OPERATOR["NOT"] clauseWeak+ ))*
  ;
  
clauseWeak
  : (first=primaryClause -> $first) (near others=primaryClause -> ^(near primaryClause+) )* 
  ;
  
primaryClause
	: 
	(modifier LPAREN clauseDefault+ RPAREN )=> modifier? LPAREN clauseDefault+ RPAREN term_modifier? 
		-> ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(CLAUSE ^(OPERATOR["DEFOP"] clauseDefault+)))) // Default operator
	| (LPAREN clauseDefault+ RPAREN term_modifier)=> modifier? LPAREN clauseDefault+ RPAREN term_modifier? 
		-> ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(CLAUSE ^(OPERATOR["DEFOP"] clauseDefault+)))) // Default operator
	| (LPAREN )=> LPAREN clauseDefault+ RPAREN
		-> clauseDefault+
	| mterm
	| atom
	;
    

atom   
	: 
	modifier? field multi_value term_modifier? -> ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(FIELD field ^(VALUE multi_value))))
	| 
	modifier? field? value term_modifier? -> ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(FIELD field? ^(VALUE value))))
	| (STAR COLON)? STAR -> ^(QANYTHING STAR["*"])
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
	| QMARK -> ^(QNORMAL QMARK)
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
	LPAREN mclause RPAREN -> mclause
	;



mclause	
	:
	clauseDefault+ -> ^(OPERATOR["DEFOP"] clauseDefault+)
	
	// 1st working	
	//(mterm+ -> mterm+)
	//(op=operator rhs=fclause -> ^(OPERATOR ^(OPERATOR["DEFOP"] $mclause) $rhs))?
	;

// this is just to fool ANTLR as we cannot reference "$mclause"
fclause	:	
	mclause
	;
		
mterm	
	:	
	modifier? value -> ^(MODIFIER modifier? ^(VALUE value))
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
	TILDE CARAT? -> ^(FUZZY TILDE) ^(BOOST CARAT?)
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
	(NEAR -> ^(OPERATOR["NEAR 5"]) )
	('/' b=NUMBER -> ^(OPERATOR["NEAR " + $b.getText()]) )?
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


fragment ESC_CHAR:  '\\' .; 

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


fragment INT: '0' .. '9';

fragment NORMAL_CHAR  : 
     ~(' ' | '\t' | '\n' | '\r' | '\u3000'
      | '\\' | '\'' | '\"' 
      | '(' | ')' | '[' | ']' | '{' | '}'
      | '+' | '-' | '!' | ':' | '~' | '^' 
      | '*' | '|' | '&' | '?' | '\\\"' | '/'  //this line is not present in lucene StandardParser.jj
      );  	


NUMBER  
	: 
	INT+ ('.' INT+)?
	;

DATE_TOKEN
	:	INT INT? ('/'|'-'|'.') INT INT? ('/'|'-'|'.') INT INT (INT INT)?
	;

TERM_NORMAL
	: 
	( NORMAL_CHAR | ESC_CHAR) ( NORMAL_CHAR | ESC_CHAR)*
	;


TERM_TRUNCATED: 
	(STAR|QMARK) ((NORMAL_CHAR|ESC_CHAR)+ (QMARK|STAR))* (NORMAL_CHAR|ESC_CHAR)*
	| ((NORMAL_CHAR|ESC_CHAR)+ (QMARK|STAR))+ (NORMAL_CHAR|ESC_CHAR)*
	;


PHRASE	
	:	
	DQUOTE ~('\"'|'\\"'|'?'|'*')+ DQUOTE
	;

PHRASE_ANYTHING	:	
	DQUOTE ~('\"'|'\\"')+ DQUOTE
	;
