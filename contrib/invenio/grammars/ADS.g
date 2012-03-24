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
  QPOSITION;
  QFUNC;
  QCOMMA;
  QIDENTIFIER;
  QCOORDINATE;
  SYNOP;
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
	(lmodifier LPAREN clauseOr+ RPAREN )=> lmodifier? LPAREN clauseOr+ RPAREN rmodifier? 
	 -> ^(CLAUSE ^(MODIFIER lmodifier? ^(TMODIFIER rmodifier? ^(OPERATOR["DEFOP"] clauseOr+)))) // Default operator
	| (LPAREN clauseOr+ RPAREN rmodifier)=> lmodifier? LPAREN clauseOr+ RPAREN rmodifier? 
	 -> ^(CLAUSE ^(MODIFIER lmodifier? ^(TMODIFIER rmodifier? ^(OPERATOR["DEFOP"] clauseOr+)))) // Default operator
	| (LPAREN )=> LPAREN clauseOr+ RPAREN
		-> clauseOr+
	| atom
	;
    

atom   
	: 
	lmodifier? field multi_value rmodifier?
	 -> ^(CLAUSE ^(MODIFIER lmodifier? ^(TMODIFIER rmodifier? ^(FIELD field multi_value))))
	| 
	lmodifier? field? value rmodifier? 
	-> ^(MODIFIER lmodifier? ^(TMODIFIER rmodifier? ^(FIELD field? value)))
	| lmodifier? (STAR COLON)? STAR 
	-> ^(MODIFIER lmodifier? ^(QANYTHING STAR["*"]))
	| lmodifier? func_name clauseBasic (',' clauseBasic)* RPAREN
	-> ^(MODIFIER lmodifier? ^(QFUNC func_name clauseBasic+ RPAREN))
	;
   

field	
	:	
	TERM_NORMAL COLON -> TERM_NORMAL
	;

value  
	: 
	range_term_in -> ^(QRANGEIN range_term_in)
	| range_term_ex -> ^(QRANGEEX range_term_ex) 
	| identifier -> ^(QIDENTIFIER identifier)
	| coordinate -> ^(QCOORDINATE coordinate)
	| normal -> ^(QNORMAL normal)	
	| truncated -> ^(QTRUNCATED truncated)	
	| quoted -> ^(QPHRASE quoted)
	| quoted_truncated -> ^(QPHRASETRUNC quoted_truncated)
	| DATE_RANGE -> ^(QDATE DATE_RANGE)
	| AUTHOR_SEARCH -> ^(QPOSITION AUTHOR_SEARCH)
	| QMARK -> ^(QTRUNCATED QMARK)
	| COMMA -> ^(QCOMMA COMMA)
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
	lmodifier? value -> ^(MODIFIER lmodifier? value)
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

lmodifier: 
	PLUS -> PLUS["+"]
	| MINUS -> MINUS["-"]
	| '=' -> SYNOP["="]
	| '#' -> SYNOP["#"]
	;


/*
This terribly convoluted grammar is here because of weird AST rewrite rules
and because we need to allow for default value when TILDE is not followed by
anything

This grammar has problem with following
	:	term^4~ 9999
	where 999 is another term, not a fuzzy value
*/
rmodifier	:	
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

identifier	
	:	
	IDENTIFIER
	;
	
coordinate
	:
	COORDINATE
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

DQUOTE	:	'\"';

SQUOTE	:	'\'';

COMMA	:	',';


fragment AS_CHAR
	:
	~('0' .. '9' | ' ' | ',' | '+' | '-' | '$')
	;
	
	
fragment ESC_CHAR:  '\\' .; 

TO	:	'TO';

/* We want to be case insensitive */
AND   : (('a' | 'A') ('n' | 'N') ('d' | 'D') | (AMPER AMPER?)|',') ;
OR  : (('o' | 'O') ('r' | 'R') | (VBAR VBAR?));
NOT   : ('n' | 'N') ('o' | 'O') ('t' | 'T');
NEAR  : (('n' | 'N') ('e' | 'E') ('a' | 'A') ('r' | 'R') | 'n') ;



	
AUTHOR_SEARCH
	:
	'^' AS_CHAR+ (',' (' ' | AS_CHAR)+)* '$'?
	;

	
DATE_RANGE
	:	
	'-' INT INT INT INT
	| INT INT INT INT '-' (INT INT INT INT)?
	;
	
IDENTIFIER
	:	('arXiv'|'arxiv') ':' TERM_CHAR+
	|'doi:' TERM_CHAR+
	//| INT+ '.' INT+ '/' INT+ ('.' INT+)?
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
	      | '?' | '*' | '\\'|',' | '=' | '#'
	      )
	 | ESC_CHAR );  	


fragment TERM_CHAR
	:	
	(TERM_START_CHAR | '-' | '+' | '=' | '#')
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


COORDINATE
	:
	/*	
	//20 54 05.689 +37 01 17.38
	INT INT INT INT '.' INT INT INT ('+'|'-') INT INT INT INT INT INT '.' INT INT 
	| //10:12:45.3-45:17:50
	INT INT ':' INT INT ':' INT INT '.' INT ('+'|'-') INT INT ':' INT INT ':' INT INT ':' INT INT	
	| //15h17m-11d10m
	INT INT 'h' INT INT 'm' ('+'|'-') INT INT 'd' INT INT 'm'	
	| // 15h17+89d15
	INT INT 'h' INT INT ('+'|'-') INT INT 'd' INT INT	

	| // 275d11m15.6954s+17d59m59.876s 
	INT+ 'd' INT INT 'm' INT INT '.' INT+ 's' ('+'|'-') INT+ 'd' INT INT 'm' INT INT '.' INT+ 's'	
	| // 12.34567h-17.87654d
	INT INT '.' INT INT INT INT INT 'h' ('+'|'-') INT INT '.' INT INT INT INT INT 'd'	
	| // 350.123456d-17.33333d <=> 350.123456 -17.33333
	INT+ '.' INT+ 'd'? ('+'|'-') INT+ '.' INT+ 'd'? '<' '=' '>' INT+ '.' INT+ 'd'? ('+'|'-') INT+ '.' INT+ 'd'?
	*/
	;	