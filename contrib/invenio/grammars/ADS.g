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
  : (first=clauseSemicolon  -> $first) (and others=clauseSemicolon -> ^(OPERATOR["AND"] clauseSemicolon+ ))*
  ;

clauseSemicolon
  : (first=clauseComma  -> $first) (semicolon others=clauseComma -> ^(OPERATOR["SEMICOLON"] clauseComma+ ))*
  ;

clauseComma
  : (first=clauseNot  -> $first) (comma others=clauseNot -> ^(OPERATOR["COMMA"] clauseNot+ ))*
  ;
    
clauseNot
  : (first=clauseNear -> $first) (not others=clauseNear -> ^(OPERATOR["NOT"] clauseNear+ ))*
  ;
  
clauseNear
  : (first=clauseBasic -> $first) (near others=clauseBasic -> ^(near clauseBasic+) )* 
  ;
  
clauseBasic
	: 
	 (lmodifier? func_name) => lmodifier? func_name clauseOr+  RPAREN
	 -> ^(CLAUSE ^(MODIFIER lmodifier? ^(QFUNC func_name ^(OPERATOR["DEFOP"] clauseOr+))))
	| (lmodifier LPAREN clauseOr+ RPAREN )=> lmodifier? LPAREN clauseOr+ RPAREN rmodifier? 
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
	
	;
   

field	
	:	
	TERM_NORMAL COLON -> TERM_NORMAL
	;

value  
	: 
	range_term_in -> ^(QRANGEIN range_term_in)
//	| range_term_ex -> ^(QRANGEEX range_term_ex) 
	| identifier -> ^(QIDENTIFIER identifier)
	| coordinate -> ^(QCOORDINATE coordinate)
	| normal -> ^(QNORMAL normal)	
	| truncated -> ^(QTRUNCATED truncated)	
	| quoted -> ^(QPHRASE quoted)
	| quoted_truncated -> ^(QPHRASETRUNC quoted_truncated)
	| DATE_RANGE -> ^(QDATE DATE_RANGE)
	| AUTHOR_SEARCH -> ^(QPOSITION AUTHOR_SEARCH)
	| QMARK -> ^(QTRUNCATED QMARK)
	//| COMMA -> ^(QCOMMA COMMA)
  	;

	

range_term_in
        options {greedy=true;}
	:	
       LBRACK
       (a=range_value -> range_value ^(QANYTHING QANYTHING["*"]))
       (TO?  b=range_value -> $a $b? )?
       RBRACK
	;

/*
deactivated for the time being

range_term_ex
	:	
       LCURLY
       ( a=range_value -> range_value ^(QANYTHING QANYTHING["*"]))
       ( 'TO' ? b=range_value -> $a $b? )?
       RCURLY
	;	
*/

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

/* this works, could be used, it is stricter

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


	
operator: (
	AND -> OPERATOR["AND"]
	| OR -> OPERATOR["OR"]
	| NOT -> OPERATOR["NOT"]
	| NEAR -> OPERATOR["NEAR"]
	);	
*/	

normal	
	:
	TERM_NORMAL
	| NUMBER
	| TO
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


lmodifier: 
	PLUS -> PLUS["+"]
	| MINUS -> MINUS["-"]
	| '=' -> SYNOP["="]
	| '#' -> SYNOP["#"]
	;



rmodifier	:	
	TILDE CARAT? -> ^(BOOST CARAT?) ^(FUZZY TILDE) 
	| CARAT TILDE? -> ^(BOOST CARAT) ^(FUZZY TILDE?)
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

comma	:	
	COMMA+
	;	

semicolon
	:
	SEMICOLON+
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
	//20 54 05.689 +37 01 17.38
	//NUMBER NUMBER NUMBER (PLUS|MINUS) NUMBER NUMBER NUMBER
	//| //10:12:45.3-45:17:50
	HOUR
	| //15h17m-11d10m
	H_NUMBER M_NUMBER (PLUS|MINUS) D_NUMBER M_NUMBER
	| // 15h17+89d15
	H_NUMBER NUMBER (PLUS|MINUS) D_NUMBER NUMBER
	| // 275d11m15.6954s+17d59m59.876s 
	D_NUMBER M_NUMBER S_NUMBER (PLUS|MINUS) D_NUMBER M_NUMBER S_NUMBER
	| // 12.34567h-17.87654d
	H_NUMBER (PLUS|MINUS) D_NUMBER
	| // 350.123456d-17.33333d <=> 350.123456 -17.33333
	'<=>'
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

MINUS : ('-'|'–');

STAR  : '*' ;

QMARK  : '?'+ ;


//LCURLY  : '{' ;

//RCURLY  : '}' ;

CARAT : '^' NUMBER?;

TILDE : '~' NUMBER?;

DQUOTE	:	'\"';

SQUOTE	:	'\'';

COMMA	:	',';

SEMICOLON:	';';


fragment AS_CHAR
	:
	~('0' .. '9' | ' ' | COMMA | PLUS | MINUS | '$')
	;
	
	
fragment ESC_CHAR:  '\\' .; 

TO	:	'TO';

/* We want to be case insensitive */
AND   : (('a' | 'A') ('n' | 'N') ('d' | 'D')) ;
OR  : (('o' | 'O') ('r' | 'R'));
NOT   : ('n' | 'N') ('o' | 'O') ('t' | 'T');
NEAR  : ('n' | 'N') ('e' | 'E') ('a' | 'A') ('r' | 'R') ;



	
AUTHOR_SEARCH
	:
	'^' AS_CHAR+ (',' (' ' | AS_CHAR)+)* '$'?
	;


/*
COORDINATE
	:
	// AS a LEXICAL token, these patterns work, but they generate too a big
	// lexer code, either they must be built into a separate grammar, or
	// be done differently, with regex for example
	
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
	;
	*/
	
	
	

	
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
	      | '–' | ';'
	      )
	 | ESC_CHAR );  	


fragment TERM_CHAR
	:	
	(TERM_START_CHAR  | '+' | '-' | '–' | '=' | '#')
	;


	
DATE_TOKEN
	:	
	INT INT? ('/'|MINUS|'.') INT INT? ('/'|MINUS|'.') INT INT (INT INT)?
	;

NUMBER  
	: 
	INT+ ('.' INT+)?
	;

fragment M_NUMBER:	
	NUMBER 'm'
	;	
fragment H_NUMBER:	
	NUMBER 'h'
	;	
fragment D_NUMBER:	
	NUMBER 'd'
	;	
fragment S_NUMBER:	
	NUMBER 's'
	;			
HOUR
	:	
	INT INT COLON INT INT COLON NUMBER (PLUS|MINUS) INT INT COLON INT INT COLON NUMBER
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

	