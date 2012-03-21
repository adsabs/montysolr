grammar ADS;

options {
  language = Java;
  output = AST;
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
  QFIRST;
  QFUNC;
}

@header{
   package org.apache.lucene.queryParser.aqp.parser;
}
@lexer::header {
   package org.apache.lucene.queryParser.aqp.parser;
}

mainQ : 
	clauseOr+ -> ^(OPERATOR["DEFOP"] clauseOr+) // Default operator
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
	| (LPAREN )=> LPAREN clauseOr+ RPAREN
		-> clauseOr+
	| atom
	;
    

atom   
	: 
	modifier? field multi_value term_modifier?
	 -> ^(CLAUSE ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(FIELD field multi_value))))
	| 
	modifier? field? value term_modifier? 
	-> ^(MODIFIER modifier? ^(TMODIFIER term_modifier? ^(FIELD field? value)))
	| modifier? (STAR COLON)? STAR 
	-> ^(MODIFIER modifier? ^(QANYTHING STAR["*"]))
	| modifier? func_name clauseBasic (',' clauseBasic)* RPAREN
	-> ^(MODIFIER modifier? ^(QFUNC func_name clauseBasic+ RPAREN ))
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
	| DATE_RANGE -> ^(QDATE DATE_RANGE)
	| AUTHOR_SEARCH -> ^(QFIRST AUTHOR_SEARCH)
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

func_name
	:	
	FUNC_NAME
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
	IDENTIFIER
	|
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


fragment ESC_CHAR:  '\\' .; 

TO	:	'TO';

/* We want to be case insensitive */
AND   : (('a' | 'A') ('n' | 'N') ('d' | 'D') | (AMPER AMPER?)|',') ;
OR  : (('o' | 'O') ('r' | 'R') | (VBAR VBAR?));
NOT   : ('n' | 'N') ('o' | 'O') ('t' | 'T');
NEAR  : (('n' | 'N') ('e' | 'E') ('a' | 'A') ('r' | 'R') | 'n') ;



// just used for debugging
id	:	
	IDENTIFIER
	;

as	:	
	AUTHOR_SEARCH
	;
// debugging end

	
AUTHOR_SEARCH
	:
	'^' AS_CHAR+ (',' (' ' | AS_CHAR)+)?
	;
fragment AS_CHAR
	:
	~('0' .. '9' | ' ' | ',' | '+' | '-')
	;
	
DATE_RANGE
	:	
	'-'? INT INT INT INT
	| INT INT INT INT '-' (INT INT INT INT)?
	;
	
IDENTIFIER
	:	('arXiv'|'arxiv') ':' TERM_CHAR+
	| INT+ '.' INT+ '/' INT+ ('.' INT+)?
	;


FUNC_NAME
	:	
	TERM_NORMAL '('
	;	


WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        | '\u3000'
        ) 
        {$channel=HIDDEN;}
    ;	

fragment INT: '0' .. '9';


fragment TERM_START_CHAR
	:
	(~(' ' | '\t' | '\n' | '\r' | '\u3000'
	      | '\'' | '\"' 
	      | '(' | ')' | '[' | ']' | '{' | '}'
	      | '+' | '-' | '!' | ':' | '~' | '^' 
	      | '?' | '*' | '\\'|','
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
