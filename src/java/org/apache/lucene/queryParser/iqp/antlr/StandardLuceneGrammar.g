grammar StandardLuceneGrammar;

options {
  language = Java;
  output = AST;
}

tokens {
  DEFOP;
  ATOM;
  MODIFIER;
  BOOST;
  VALUE;
  CLAUSE;
  RELATION;
  RANGE;
}

mainQ : 
  clauseDefault* -> ^(DEFOP clauseDefault+)
  ;

  
clauseDefault
  : (first=clauseStrongest -> $first) (NOT others=clauseStrongest -> ^(NOT clauseStrongest+))*
  ;

clauseStrongest
  : (first=clauseStrong  -> $first) (AND others=clauseStrong -> ^(AND clauseStrong+))*
  ;
  
clauseStrong
  : (first=clauseWeak -> $first) (OR others=clauseWeak -> ^(OR clauseWeak+))*
  ;
  
clauseWeak
  : (first=primaryClause -> $first) (NEAR others=primaryClause -> ^(NEAR primaryClause+))* 
  ;
  
primaryClause
  : 
  
  atom -> ^(ATOM atom)
  | (modifier? LPAREN first=clauseDefault -> $first) (clauseDefault* RPAREN boost? -> ^(CLAUSE ^(MODIFIER modifier?) ^(BOOST boost?) ^(DEFOP clauseDefault+)))
  //^(CLAUSE ^(MODIFIER modifier?) ^(BOOST boost?) clauseDefault+)

  //atom -> ^(ATOM ^(MODIFIER ) ^(BOOST ) ^(VALUE atom))
  //| (LPAREN clauseDefault+ RPAREN BOOST? -> clauseDefault+ )//^(CLAUSE ^(MODIFIER modifier?) ^(BOOST BOOST) clauseDefault+))
  ;
    

atom   : 
  modifier?
  (
  field multi_term
  | field? (value)
  )
  //| LPAREN query RPAREN
  ;
   
	   
field	:	
	TERM_NORMAL COLON -> TERM_NORMAL
	;

value  : 
  range 
  | term
  
//  | normal
//  | quoted
//  | quoted_truncated
//  | truncated
  ;

multi_term
	: LPAREN mterm+ RPAREN
	;
	
	
mterm	:	(modifier? term)
	;
		
term
	:	(normal | quoted | quoted_truncated | truncated)
		(
		(BOOST FUZZY_SLOP?)
		| FUZZY_SLOP
		)?
	;

range	:	
	(RANGE_TERM_IN|RANGE_TERM_EX) boost?
	;	
	
truncated
	:	TERM_TRUNCATED
	; 

quoted	:	TERM_QUOTED
	;

quoted_truncated
	:	TERM_QUOTED_TRUNCATED;

normal	:	TERM_NORMAL
		| NUMBER
	;	
	
operator: (AND | OR | NOT | NEAR);

modifier: (PLUS|MINUS);

ESC_CHAR:  '\\' .; 

boost	:	BOOST;

BOOST	:	
	(CARAT boost=NUMBER)
	;
	
FUZZY_SLOP
	:	TILDE NUMBER?;

TO	:	'TO';

/* We want to be case insensitive */
AND   : (('a' | 'A') ('n' | 'N') ('d' | 'D') | (AMPER AMPER?)) ;
OR  : (('o' | 'O') ('r' | 'R') | (VBAR VBAR?));
NOT   : (('n' | 'N') ('o' | 'O') ('t' | 'T') | '!');
NEAR  : (('n' | 'N') ('e' | 'E') ('a' | 'A') ('r' | 'R') | 'n');

fragment NORMAL_CHAR  : ~(' ' | '\t' | '\n' | '\r'
      | '\\' | '\'' | '\"' 
      | '(' | ')' | '[' | ']' | '{' | '}'
      | '+' | '-' | '!' | ':' | '~' | '^' 
      | '*' | '|' | '&' | '?'  //this line is not present in lucene StandardParser.jj
      );  
  



TERM_QUOTED
  : '\"' (~('\"' | '?' | '*' | '\\\"' ))* '\"'
  ; 

fragment INT: '0' .. '9';



NUMBER  : INT+ ('.' INT+)?;


TERM_NORMAL
  : ( NORMAL_CHAR | ESC_CHAR ) ( NORMAL_CHAR | ESC_CHAR )*
  ;

TERM_QUOTED_TRUNCATED: '\"' (~('\"' | '?' | '*') | STAR | QMARK )+ '\"';

TERM_TRUNCATED: (NORMAL_CHAR | STAR | QMARK)+;

RANGE_TERM_IN
	:	
       LBRACK
       WS*
       (TERM_NORMAL | TERM_QUOTED)
       ((WS* TO WS*)
       (TERM_NORMAL | TERM_QUOTED))?
       WS*
       RBRACK
	;


RANGE_TERM_EX
	:	
       LCURLY
       WS*
       (TERM_NORMAL | TERM_QUOTED)
       ((WS* TO WS*)
       (TERM_NORMAL | TERM_QUOTED))?
       WS*
       RCURLY
	;	
	

range_data
	:	(TERM_NORMAL | TERM_QUOTED) {System.out.println("gotcha");}
	;	





LPAREN  : '(';

RPAREN  : ')';

fragment LBRACK  : '[';

fragment RBRACK  : ']';

COLON   : ':' ;  //this must NOT be fragment

PLUS  : '+' ;

MINUS : '-';

fragment STAR  : '*' ;

fragment QMARK  : '?' ;

fragment VBAR  : '|' ;

fragment AMPER : '&' ;

fragment LCURLY  : '{' ;

fragment RCURLY  : '}' ;

fragment CARAT : '^' ;

fragment TILDE : '~' ;

fragment DQUOTE	
	:	'\"';

fragment SQUOTE
	:	'\'';



WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

	


