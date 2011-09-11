grammar StandardLuceneGrammar;

options {
  language = Java;
  output = AST;
}

tokens {
  DEFOP;
  ATOM;
  MODIFIER;
  VALUE;
  CLAUSE;
  RELATION;
  RANGE;
  FIELD;
  FUZZY;
  ADDED;
  BOOST;
  QNORMAL;
  QPHRASE;
  QTRUNCQUOTED;
  QTRUNCATED;
  QRANGEIN;
  QRANGEEX;
}

mainQ : 
	clauseDefault+ -> ^(DEFOP clauseDefault+)
	;
   
  
clauseDefault
  : (first=clauseStrongest -> $first) (NOT others=clauseStrongest -> ^(NOT clauseStrongest+ ))*
  ;

clauseStrongest
  : (first=clauseStrong  -> $first) (AND others=clauseStrong -> ^(AND clauseStrong+ ))*
  ;
  
clauseStrong
  : (first=clauseWeak -> $first) (OR others=clauseWeak -> ^(OR clauseWeak+ ))*
  ;
  
clauseWeak
  : (first=primaryClause -> $first) (NEAR others=primaryClause -> ^(NEAR primaryClause+ ))* 
  ;
  
primaryClause
	: 
	atom 
	| modifier? LPAREN clauseDefault+ RPAREN (CARAT NUMBER)? -> ^(CLAUSE ^(MODIFIER modifier?) ^(BOOST NUMBER?) ^(DEFOP clauseDefault+) )
	;
    

atom   
	: 
	modifier? field multi_value -> ^(ATOM ^(MODIFIER modifier?) ^(FIELD field) ^(VALUE multi_value))
	| modifier? field? value -> ^(ATOM ^(MODIFIER modifier?) ^(FIELD field?) ^(VALUE value*))
	;
   
	   
field	
	:	
	TERM_NORMAL COLON -> TERM_NORMAL
	;

value  
	: 
  	range 
  	| term
  	;


range	:	
	(
	range_term_in -> ^(QRANGEIN range_term_in)
	| range_term_ex -> ^(QRANGEEX range_term_ex) 
	)
	term_modifier? -> $range term_modifier?
	;	

range_term_in
	:	
       LBRACK
       WS*
       (normal -> normal | quoted -> quoted)
       ((WS* TO WS*)
       (t=normal | q=quoted)
       -> $range_term_in $t? $q?
       )?
       WS*
       RBRACK
	;


range_term_ex
	:	
       LCURLY
       WS*
       (normal -> normal | quoted -> quoted)
       (
       (WS* TO WS*)
       (t=normal | q=quoted)
       -> $range_term_ex $t? $q?
       )?
       WS*
       RCURLY
	;	


multi_value
	: 
	LPAREN mterm+ RPAREN -> mterm+
	;
	
	
mterm	
	:	
	(modifier? term -> ^(ATOM ^(MODIFIER modifier?) ^(VALUE term)))
	;
		
term
	:	
	( normal -> ^(QNORMAL normal)
	| truncated -> ^(QTRUNCATED truncated)
	| quoted -> ^(QPHRASE quoted)
	| quoted_truncated -> ^(QTRUNCQUOTED quoted_truncated)
	)
	term_modifier? -> term_modifier? $term
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
Lucene allows default fuzzy value (if not specified). This, however,
generates warnings
	...
	TILDE f=NUMBER* -> ^(MODIFIER ^(BOOST ) ^(FUZZY $f?))
	

Decision can match input such as "NUMBER" using multiple alternatives: 1, 2
As a result, alternative(s) 2 were disabled for that input

	
TODO: add  semantic predicates; switch on memoization

(CARAT b=NUMBER -> ^(MODIFIER ^(BOOST $b) ^(FUZZY ))) (TILDE f=NUMBER -> ^(MODIFIER ^(BOOST $b) ^(FUZZY $f)))?
	|	TILDE f=NUMBER? -> ^(MODIFIER ^(BOOST ) ^(FUZZY $f?))
	
*/
term_modifier	:	
	((CARAT b=NUMBER -> ^(MODIFIER ^(BOOST $b) ^(FUZZY ))) (TILDE f=NUMBER -> ^(MODIFIER ^(BOOST $b) ^(FUZZY $f)))?)
	| (TILDE -> ^(MODIFIER ^(BOOST) ^(FUZZY )) ) (f=NUMBER? -> ^(MODIFIER ^(BOOST ) ^(FUZZY $f?)))
	
	
	/*
	(CARAT b=NUMBER -> ^(MODIFIER ^(BOOST $b) ^(FUZZY ))) 
		(
		 TILDE f=NUMBER -> ^(MODIFIER ^(BOOST $b) ^(FUZZY $f))
		 | TILDE -> ^(MODIFIER ^(BOOST $b) ^(FUZZY '0.5'))
		)?
	|(TILDE NUMBER)=> TILDE f=NUMBER -> ^(MODIFIER ^(BOOST ) ^(FUZZY $f?))
	|(TILDE)=> TILDE -> ^(MODIFIER ^(BOOST ) ^(FUZZY '0.5'))
	*/
	/*
	(boost fuzzy)=> CARAT NUMBER TILDE NUMBER
	|(CARAT NUMBER TILDE) => CARAT NUMBER TILDE
	| (TILDE NUMBER) => TILDE NUMBER
	| (TILDE)=> TILDE
	|CARAT NUMBER (TILDE NUMBER)?
	*/
	;

boost	:	
	CARAT NUMBER
	;

fuzzy	:	
	TILDE NUMBER
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
NEAR  : (('n' | 'N') ('e' | 'E') ('a' | 'A') ('r' | 'R') | 'n');


WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;


fragment INT: '0' .. '9';

fragment NORMAL_CHAR  : ~(' ' | '\t' | '\n' | '\r'
      | '\\' | '\'' | '\"' 
      | '(' | ')' | '[' | ']' | '{' | '}'
      | '+' | '-' | '!' | ':' | '~' | '^' 
      | '*' | '|' | '&' | '?' | '\\\"'  //this line is not present in lucene StandardParser.jj
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
	(NORMAL_CHAR | STAR | QMARK)+
	;


PHRASE	
	:	
	DQUOTE ~('\"'|'?'|'*')+ DQUOTE
	;

PHRASE_ANYTHING	:	
	DQUOTE ~('\"')+ DQUOTE
	;
