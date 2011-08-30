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
  term
  //| LPAREN query RPAREN
  ;
  


term  : 
  NORMAL_TERM
  | QUOTED_TERM
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
      | '*' | '|' | '&'  //this line is not present in lucene StandardParser.jj
      );  
  
fragment ESC_CHAR:  '\\' .; 


QUOTED_TERM
  : '\"' (~('\"'))* '\"'
  ; 

NORMAL_TERM
  : NORMAL_CHAR ( NORMAL_CHAR | ESC_CHAR )*
  ;
    

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

LBRACK  : '[';

RBRACK  : ']';

COLON   : ':' ;

PLUS  : '+' ;

MINUS : '-' ;

STAR  : '*' ;

VBAR  : '|' ;

AMPER : '&' ;

LCURLY  : '{' ;

RCURLY  : '}' ;

CARAT : '^' ;

TILDE : '~' ;



