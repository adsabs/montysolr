grammar Invenio;

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
  QFUNC;
  QPHRASE;
  QPHRASETRUNC;
  QTRUNCATED;
  QRANGEIN;
  QRANGEEX;
  QREGEX;  
  QANYTHING;
  QDATE;
  AMBIGUITY;
}

@header{
   package org.apache.lucene.queryParser.aqp.parser;
}
@lexer::header {
   package org.apache.lucene.queryParser.aqp.parser;
}

mainQ : 
	operator clauseTop -> ^(AMBIGUITY["leftmost-operation"] operator clauseTop)
	| clauseTop
	;

clauseTop
  :
  //(clauseOr -> ^(OPERATOR["DEFOP"] $clauseTop clauseOr))+
  //(clauseOr^)+
  clauseOr -> clauseOr
  ;

clauseOr
  : 
  (clauseBasic -> clauseBasic)
  (operator a=clauseBasic -> ^(operator $clauseOr $a)
  | a=clauseBasic -> ^(OPERATOR["DEFOP"] $clauseOr $a))*
  ;

clauseBare
  :
  (clauseBasic -> clauseBasic)
  (a=clauseBasic -> ^(OPERATOR["DEFOP"] $clauseBare $a))*
  ;
  
     
/*  
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
  : (first=clauseBasic -> $first) (near others=clauseBasic -> ^(near clauseBasic+ ))* 
  ;
*/
  
clauseBasic
	:
	(modifier LPAREN clauseOr RPAREN )=> modifier? LPAREN clauseOr RPAREN term_modifier? 
	 -> ^(CLAUSE ^(MODIFIER modifier? ^(TMODIFIER term_modifier? clauseOr)))
	| (LPAREN clauseOr RPAREN term_modifier)=> modifier? LPAREN clauseOr RPAREN term_modifier? 
	 -> ^(CLAUSE ^(MODIFIER modifier? ^(TMODIFIER term_modifier? clauseOr)))
	| (LPAREN )=> LPAREN clauseOr RPAREN
	 -> clauseOr
	| second_order_op clauseBasic -> ^(second_order_op clauseBasic)
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
	| REGEX -> ^(QREGEX REGEX)
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
	//clauseOr+ -> ^(OPERATOR["DEFOP"] clauseOr+)
	clauseTop
	
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
	| IDENTIFIER
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
	//PLUS -> PLUS["+"]
	//| MINUS -> MINUS["-"]
	BAR
	;


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

second_order_op
	:	
	o=SECOND_ORDER_OP COLON -> ^(QFUNC[$o.getText()])
	;

/* ================================================================
 * =                     LEXER                                    =
 * ================================================================
 */
SECOND_ORDER_OP
	:	
	('refersto'
	| 'citedby'
	| 'cited')
	;


IDENTIFIER
	:	('arXiv'|'arxiv') ':' TERM_CHAR+
	| INT+ '.' INT+ '/' INT+ ('.' INT+)?
	;

SLASH   : '/';

LPAREN  : '(';

RPAREN  : ')';

LBRACK  : '[';

RBRACK  : ']';

COLON   : ':' ;  //this must NOT be fragment

fragment PLUS  : '+' ;

fragment MINUS : '-';

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

BAR	:	'#'
	;


TO	:	'TO';

/* We want to be case insensitive */
AND   : (('a' | 'A') ('n' | 'N') ('d' | 'D')) | (AMPER AMPER?) | PLUS;
OR  : (('o' | 'O') ('r' | 'R')) | (VBAR VBAR?);
NOT   : (('n' | 'N') ('o' | 'O') ('t' | 'T')) | MINUS;
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
	      | '?' | '*' | '\\' | '#' | '|' | '/'
	      )
	 | ESC_CHAR );  	


fragment TERM_CHAR
	:	
	(TERM_START_CHAR | '-' | '+' | '#' | '/' | '\'')
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
	DQUOTE (ESC_CHAR|~('\"'|'\\'|'?'|'*'|'\\\''))+ DQUOTE
	| SQUOTE (ESC_CHAR|~('\''|'\\'|'?'|'*'|'\\\''))+ SQUOTE
	;

PHRASE_ANYTHING	:	
	DQUOTE (ESC_CHAR|~('\"'|'\\'))+ DQUOTE
	| SQUOTE (ESC_CHAR|~('\''|'\\'|'\\\''))+ SQUOTE
	;

REGEX	
	:	
	SLASH (ESC_CHAR|~('\\'|'\\/'))+ SLASH
	;

		