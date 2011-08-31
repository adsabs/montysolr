grammar StandardLuceneGrammar;

options {
  language = Java;
  output = AST;
}


mainQ : 
  clause;

clause  : 
  clauseWeakest+;
  
clauseWeakest
  : clauseWeaker (NOT clauseWeaker)*;

clauseWeaker
  : clauseWeak (AND clauseWeak)*;
  
clauseWeak
  : clauseStrong (OR clauseStrong)*;
  
clauseStrong
  : primaryClause (NEAR primaryClause)*;
  
primaryClause
  : 
  query
  | LPAREN clause RPAREN (CARAT boost=NUMBER)?
  ;
    

query   : 
  field? (term)
  //| LPAREN query RPAREN
  ;
   
field	:	
	term COLON
	;

term  : 
  rangein 
  | TERM_NORMAL
  | TERM_QUOTED
  | TERM_QUOTED_TRUNCATED
  | TERM_TRUNCATED
  ;

rangein	:	
	TERM_RANGEIN
	;
	
operator: (AND | OR | NOT | NEAR);

modifier: (PLUS|MINUS);

/* We want to be case insensitive */

AND   : (('a' | 'A') ('n' | 'N') ('d' | 'D') | '&&') ;
OR  : (('o' | 'O') ('r' | 'R') | '||');
NOT   : (('n' | 'N') ('o' | 'O') ('t' | 'T') | '!');
NEAR  : ('n' | 'N') ('e' | 'E') ('a' | 'A') ('r' | 'R') ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

fragment INT: '0' .. '9';

NUMBER  : INT+ ('.' INT+)?;


fragment NORMAL_CHAR  : ~(' ' | '\t' | '\n' | '\r'
      | '\\' | '\'' | '\"' 
      | '(' | ')' | '[' | ']' | '{' | '}'
      | '+' | '-' | '!' | ':' | '~' | '^' 
      | '*' | '|' | '&' | '?'  //this line is not present in lucene StandardParser.jj
      );  
  
fragment ESC_CHAR:  '\\' .; 


TERM_QUOTED
  : '\"' (~('\"' | '?' | '*' ))* '\"'
  ; 

TERM_NORMAL
  : NORMAL_CHAR ( NORMAL_CHAR | ESC_CHAR )*
  ;

TERM_QUOTED_TRUNCATED: '\"' (~('\"' | '?' | '*') | STAR | QMARK )+ '\"';

TERM_TRUNCATED: (NORMAL_CHAR | STAR | QMARK)+;

TERM_RANGEIN
	:	
	RANGEIN_START {System.out.println("start");}
	(RANGEIN_GOOP | TERM_QUOTED)
	(' '+ RANGEIN_TO ' '+)?
	(RANGEIN_GOOP | TERM_QUOTED)?
	RANGEIN_END
	;
// ------------ problematic --------------

	    

// ------------ problematic --------------


//TERMCHAR  :
//  (~('\u0000'..'\u001f' | ' ' | '\\' | '\"' | '(' | ')' | '[' | ']' | ':' | ',' | ';' | '+' | '-' | '*' | '|' | '&' | '{' | '}' | '~' | '^') )*
//  ;

//TERMCHAR: ~('"'| '\\' | ' ' | '(' | ')');


// every character that follows a backslash is considered as an escaped character

//fragment ESCAPED_CHAR: '\\' .;

//fragment TERM_START_CHAR: ( ~( ' ' | '\t' | '\n' | '\r' | '\u3000' | '+' | '-' | '!' | '(' | ')' | ':' | '^' | '[' | ']' | '\"' | '{' | '}' | '~' | '\\') | ESCAPED_CHAR );
                       
//fragment TERM_CHAR: ( TERM_START_CHAR | ESCAPED_CHAR | '-' | '+' );

//fragment QUOTED_CHAR: ( ~('\"' | '\\') | ESCAPED_CHAR );




LPAREN  : '(';

RPAREN  : ')';

fragment LBRACK  : '[';

fragment RBRACK  : ']';

COLON   : ':' ;

PLUS  : '+' ;

MINUS : '-' ;

fragment STAR  : '*' ;

fragment QMARK  : '?' ;

VBAR  : '|' ;

AMPER : '&' ;

fragment LCURLY  : '{' ;

fragment RCURLY  : '}' ;

CARAT : '^' ;

TILDE : '~' ;



RANGEIN_START
	: 
	'['
	;
	
fragment RANGEIN_TO
	:	
	'TO'
	;

RANGEIN_END
	:	
	']'
	;

RANGEIN_QUOTED
	: '\"' (~('\"') | '\\\"')+ '\"'
	;	

RANGEIN_GOOP
	: (~(' ' | ']'))+
	; 
	

