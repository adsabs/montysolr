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
  QPHRASETRUNC;
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
	| modifier? field? value -> ^(ATOM ^(MODIFIER modifier?) ^(FIELD field?) ^(VALUE value))
	;
   
	   
field	
	:	
	TERM_NORMAL COLON -> TERM_NORMAL
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
	)
	term_modifier? -> term_modifier? $value
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
	(modifier? value -> ^(ATOM ^(MODIFIER modifier?) ^(VALUE value)))
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
	  (CARAT b=NUMBER -> ^(MODIFIER ^(BOOST $b) ^(FUZZY ))
	 ) 
	( //syntactic predicate
	 (TILDE NUMBER )=>TILDE f=NUMBER -> ^(MODIFIER ^(BOOST $b) ^(FUZZY $f)) //
	 | TILDE -> ^(MODIFIER ^(BOOST $b) ^(FUZZY NUMBER["0.5"]))
	 )* // set the default value
	
	)
	// second alternative [only ~ | ~NUMBER]
	| 
	  (TILDE -> ^(MODIFIER ^(BOOST) ^(FUZZY NUMBER["0.5"])) ) // set the default value
	  ((~(WS))=>f=NUMBER -> ^(MODIFIER ^(BOOST ) ^(FUZZY $f?)) )* //replace the default but '~' must not be followed by WS
	
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
        ) 
        {$channel=HIDDEN;}
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
