grammar ADS;

options {
  language = Java;
  output = AST;
  superClass = UnforgivingParser;
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
  QDELIMITER;
  QIDENTIFIER;
  QCOORDINATE;
  QREGEX;
  XMETA;
  EQUAL;
  HASH;
}

@header{
   package org.apache.lucene.queryparser.flexible.aqp.parser;
}
@lexer::header {
   package org.apache.lucene.queryparser.flexible.aqp.parser;
}
@lexer::members {
  private final java.util.ArrayDeque<Token> pendingTokens =
      new java.util.ArrayDeque<Token>();

  /**
   * AUTHOR_SEARCH is permissive for comma-separated names. If it consumed an
   * explicit field suffix, split that suffix into normal lexer tokens without
   * rewinding the CharStream, so ANTLR's line and column state remains intact.
   */
  @Override
  public Token nextToken() {
    if (!pendingTokens.isEmpty()) {
      return pendingTokens.removeFirst();
    }

    Token token = super.nextToken();
    if (token.getType() != AUTHOR_SEARCH) {
      return token;
    }

    String text = token.getText();
    int boundary = fieldBoundary(text);
    if (boundary < 0) {
      return token;
    }

    int fieldEnd = boundary;
    while (fieldEnd < text.length() && text.charAt(fieldEnd) != ':') {
      fieldEnd++;
    }
    if (fieldEnd >= text.length()) {
      return token;
    }

    org.antlr.runtime.CommonToken authorToken =
        (org.antlr.runtime.CommonToken) token;
    int start = authorToken.getStartIndex();
    int line = authorToken.getLine();
    int column = authorToken.getCharPositionInLine();
    authorToken.setText(text.substring(0, boundary));
    authorToken.setStopIndex(start + boundary - 1);

    org.antlr.runtime.CommonToken fieldToken =
        new org.antlr.runtime.CommonToken(authorToken.getInputStream(), TERM_NORMAL,
            Token.DEFAULT_CHANNEL, start + boundary, start + fieldEnd - 1);
    fieldToken.setStartIndex(start + boundary);
    fieldToken.setStopIndex(start + fieldEnd - 1);
    fieldToken.setLine(line);
    fieldToken.setCharPositionInLine(column + boundary);

    org.antlr.runtime.CommonToken colonToken =
        new org.antlr.runtime.CommonToken(authorToken.getInputStream(), COLON,
            Token.DEFAULT_CHANNEL, start + fieldEnd, start + fieldEnd);
    colonToken.setStartIndex(start + fieldEnd);
    colonToken.setStopIndex(start + fieldEnd);
    colonToken.setLine(line);
    colonToken.setCharPositionInLine(column + fieldEnd);

    pendingTokens.add(fieldToken);
    pendingTokens.add(colonToken);
    queueFieldValue(authorToken, text, fieldEnd + 1);

    return authorToken;
  }

  private void queueFieldValue(
      org.antlr.runtime.CommonToken authorToken, String text, int valueStart) {
    if (valueStart >= text.length()) {
      return;
    }

    org.antlr.runtime.ANTLRStringStream valueStream =
        new org.antlr.runtime.ANTLRStringStream(text.substring(valueStart));
    ADSLexer valueLexer = new ADSLexer(valueStream);
    int inputStart = authorToken.getStartIndex() + valueStart;
    org.antlr.runtime.CommonToken valueToken;
    while ((valueToken =
        (org.antlr.runtime.CommonToken) valueLexer.nextToken()).getType()
        != Token.EOF) {
      org.antlr.runtime.CommonToken queuedToken =
          new org.antlr.runtime.CommonToken(authorToken.getInputStream(),
              valueToken.getType(), valueToken.getChannel(),
              inputStart + valueToken.getStartIndex(),
              inputStart + valueToken.getStopIndex());
      queuedToken.setLine(authorToken.getLine() + valueToken.getLine() - 1);
      int tokenColumn = valueToken.getCharPositionInLine();
      if (valueToken.getLine() == 1) {
        tokenColumn += authorToken.getCharPositionInLine() + valueStart;
      }
      queuedToken.setCharPositionInLine(tokenColumn);
      pendingTokens.add(queuedToken);
    }
  }

  @Override
  public void reset() {
    super.reset();
    if (pendingTokens != null) {
      pendingTokens.clear();
    }
  }

  private int fieldBoundary(String text) {
    boolean afterComma = false;
    for (int i = 1; i < text.length(); i++) {
      if (text.charAt(i) == ',') {
        afterComma = true;
        continue;
      }
      if (!afterComma || text.charAt(i) != ' ') {
        continue;
      }
      int field = i;
      while (field < text.length() && text.charAt(field) == ' ') {
        field++;
      }
      if (field >= text.length() || !Character.isLetter(text.charAt(field))) {
        continue;
      }
      int colon = field + 1;
      while (colon < text.length()
          && (Character.isLetterOrDigit(text.charAt(colon))
              || text.charAt(colon) == '_' || text.charAt(colon) == '-')) {
        colon++;
      }
      if (colon < text.length() && text.charAt(colon) == ':') {
        return field;
      }
    }
    return -1;
  }

  private boolean isCaretPhraseDollar() {
    int offset = -1;
    if (input.LA(offset) != '"') {
      return false;
    }
    offset--;
    while (input.LA(offset) != CharStream.EOF) {
      if (input.LA(offset) == '"' && input.LA(offset - 1) != '\\') {
        return input.LA(offset - 1) == '^';
      }
      offset--;
    }
    return false;
  }

  private static final String ELEMENT_SYMBOLS =
      "|H|He|Li|Be|B|C|N|O|F|Ne|Na|Mg|Al|Si|P|S|Cl|Ar|K|Ca|Sc|Ti|V|Cr|Mn|Fe|Co|Ni|Cu|Zn|"
      + "Ga|Ge|As|Se|Br|Kr|Rb|Sr|Y|Zr|Nb|Mo|Tc|Ru|Rh|Pd|Ag|Cd|In|Sn|Sb|Te|I|Xe|Cs|Ba|"
      + "La|Ce|Pr|Nd|Pm|Sm|Eu|Gd|Tb|Dy|Ho|Er|Tm|Yb|Lu|Hf|Ta|W|Re|Os|Ir|Pt|Au|Hg|Tl|"
      + "Pb|Bi|Po|At|Rn|Fr|Ra|Ac|Th|Pa|U|Np|Pu|Am|Cm|Bk|Cf|Es|Fm|Md|No|Lr|Rf|Db|Sg|"
      + "Bh|Hs|Mt|Ds|Rg|Cn|Nh|Fl|Mc|Lv|Ts|Og|";

  private boolean isForbiddenLine() {
    StringBuilder value = new StringBuilder();
    int i = 1;
    int c = input.LA(i);
    if (c == '[') {
      value.append((char) c);
      c = input.LA(++i);
      if (!Character.isUpperCase((char) c)) return false;
      value.append((char) c);
      c = input.LA(++i);
      if (Character.isLowerCase((char) c)) {
        value.append((char) c);
        c = input.LA(++i);
      }
    } else {
      if (!Character.isUpperCase((char) c)) return false;
      value.append((char) c);
      c = input.LA(++i);
      if (Character.isLowerCase((char) c)) {
        value.append((char) c);
        c = input.LA(++i);
      }
    }
    while (c == ' ' || c == '\t') {
      value.append((char) c);
      c = input.LA(++i);
    }
    if (c == '[') {
      value.append((char) c);
      c = input.LA(++i);
      while (c == ' ' || c == '\t') {
        value.append((char) c);
        c = input.LA(++i);
      }
    }
    int romanStart = value.length();
    while ("IVXLCDMivxlcdm".indexOf(c) >= 0) {
      value.append((char) c);
      c = input.LA(++i);
    }
    if (value.length() == romanStart) return false;
    while (c == ' ' || c == '\t') {
      value.append((char) c);
      c = input.LA(++i);
    }
    if (c != ']') return false;
    value.append(']');
    return isForbiddenLine(value.toString());
  }
  private boolean isForbiddenLine(String value) {
    int close = value.lastIndexOf(']');
    if (close <= 0) return false;
    int open = value.indexOf('[');
    int elementStart = open == 0 ? 1 : 0;
    while (elementStart < close && isLineSpace(value.charAt(elementStart))) elementStart++;
    int elementEnd = elementStart + 1;
    if (elementEnd < close && Character.isLowerCase(value.charAt(elementEnd))) elementEnd++;
    if (!isElementSymbol(value, elementStart, elementEnd)) return false;
    int romanStart = elementEnd;
    if (open >= elementEnd) {
      while (romanStart < open && isLineSpace(value.charAt(romanStart))) romanStart++;
      if (romanStart != open) return false;
      romanStart = open + 1;
    }
    while (romanStart < close && isLineSpace(value.charAt(romanStart))) romanStart++;
    int romanEnd = close;
    while (romanEnd > romanStart && isLineSpace(value.charAt(romanEnd - 1))) romanEnd--;
    return isCanonicalRoman(value, romanStart, romanEnd);
  }

  private boolean isElementSymbol(String value, int start, int end) {
    return ELEMENT_SYMBOLS.contains("|" + value.substring(start, end) + "|");
  }

  private boolean isCanonicalRoman(String value, int start, int end) {
    if (start >= end) return false;
    boolean lower = Character.isLowerCase(value.charAt(start));
    for (int i = start; i < end; i++) {
      char c = value.charAt(i);
      if (!"IVXLCDMivxlcdm".contains(String.valueOf(c))
          || Character.isLowerCase(c) != lower) return false;
    }
    int i = consumeRun(value, start, end, 'M', 3);
    if (i < end && upper(value.charAt(i)) == 'C'
        && i + 1 < end && (upper(value.charAt(i + 1)) == 'M' || upper(value.charAt(i + 1)) == 'D')) {
      i += 2;
    } else if (i < end && upper(value.charAt(i)) == 'D') {
      i = consumeRun(value, i + 1, end, 'C', 3);
    } else {
      i = consumeRun(value, i, end, 'C', 3);
    }
    if (i < end && upper(value.charAt(i)) == 'X'
        && i + 1 < end && (upper(value.charAt(i + 1)) == 'C' || upper(value.charAt(i + 1)) == 'L')) {
      i += 2;
    } else if (i < end && upper(value.charAt(i)) == 'L') {
      i = consumeRun(value, i + 1, end, 'X', 3);
    } else {
      i = consumeRun(value, i, end, 'X', 3);
    }
    if (i < end && upper(value.charAt(i)) == 'I'
        && i + 1 < end && (upper(value.charAt(i + 1)) == 'X' || upper(value.charAt(i + 1)) == 'V')) {
      i += 2;
    } else if (i < end && upper(value.charAt(i)) == 'V') {
      i = consumeRun(value, i + 1, end, 'I', 3);
    } else {
      i = consumeRun(value, i, end, 'I', 3);
    }
    return i == end;
  }

  private int consumeRun(String value, int start, int end, char symbol, int max) {
    int i = start;
    while (i < end && upper(value.charAt(i)) == symbol && i - start < max) i++;
    return i;
  }

  private char upper(char value) {
    return Character.toUpperCase(value);
  }

  private boolean isLineSpace(char value) {
    return value == ' ' || value == '\t';
  }
}

mainQ : 
  clauseOr+ EOF -> ^(OPERATOR["DEFOP"] clauseOr+) // Default operator
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
   (lmodifier? func_name) => lmodifier? func_name clauseOr+  RPAREN rmodifier?
   -> ^(CLAUSE ^(MODIFIER lmodifier? ^(TMODIFIER rmodifier? ^(QFUNC func_name ^(OPERATOR["DEFOP"] clauseOr+) RPAREN))))
  | (lmodifier LPAREN clauseOr+ RPAREN )=> lmodifier? LPAREN clauseOr+ RPAREN rmodifier? 
   -> ^(CLAUSE ^(MODIFIER lmodifier? ^(TMODIFIER rmodifier? ^(OPERATOR["DEFOP"] clauseOr+)))) // Default operator
  | (LPAREN clauseOr+ RPAREN rmodifier)=> lmodifier? LPAREN clauseOr+ RPAREN rmodifier? 
   -> ^(CLAUSE ^(MODIFIER lmodifier? ^(TMODIFIER rmodifier? ^(OPERATOR["DEFOP"] clauseOr+)))) // Default operator
  | (LPAREN )=> LPAREN clauseOr+ RPAREN
    -> ^(CLAUSE ^(OPERATOR["DEFOP"] clauseOr+))
  | atom
  ;
    

atom   
  : 
  lmodifier? field multi_value rmodifier?
   -> ^(MODIFIER lmodifier? ^(TMODIFIER rmodifier? ^(FIELD field multi_value)))
  | lmodifier? field? value rmodifier? 
  -> ^(MODIFIER lmodifier? ^(TMODIFIER rmodifier? ^(FIELD field? value)))
  //| lmodifier? (STAR COLON)? STAR 
  //-> ^(MODIFIER lmodifier? ^(QANYTHING STAR["*"]))
  
  ;
   

field 
  : 
  TERM_NORMAL COLON -> TERM_NORMAL
  //| STAR COLON -> STAR["*"]
  ;


range_term_in
        options {greedy=true;}
  : 
       LBRACK
       (a=range_value -> $a ^(QANYTHING QANYTHING["*"]))
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
value  
  :
  REGEX -> ^(QREGEX REGEX)
  | f=FORBIDDEN_LINE -> ^(QNORMAL $f)
  |range_term_in -> ^(QRANGEIN range_term_in)
//  | range_term_ex -> ^(QRANGEEX range_term_ex) 
  | identifier -> ^(QIDENTIFIER identifier)
  | coordinate -> ^(QCOORDINATE coordinate)
  | normal -> ^(QNORMAL normal) 
  | truncated -> ^(QTRUNCATED truncated)  
  | CARAT_PHRASE quoted -> ^(QPHRASE quoted)
  | quoted -> ^(QPHRASE quoted)
  | quoted_truncated -> ^(QPHRASETRUNC quoted_truncated)
  | DATE_RANGE -> ^(QDATE DATE_RANGE)
  | AUTHOR_SEARCH -> ^(QPOSITION AUTHOR_SEARCH)
  | QMARK -> ^(QTRUNCATED QMARK)
  | match_all -> ^(QANYTHING match_all)
  | STAR -> ^(QTRUNCATED STAR)
  | LOCAL_PARAMS -> ^(XMETA LOCAL_PARAMS) 
  | COMMA -> ^(QDELIMITER COMMA)
  | SEMICOLON -> ^(QDELIMITER SEMICOLON)
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
  LPAREN multiClause RPAREN -> ^(CLAUSE multiClause)
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

match_all
  : 
  STAR COLON STAR
  ;
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

quoted  : 
  PHRASE
  ;


lmodifier: 
  PLUS -> PLUS["+"]
  | MINUS -> MINUS["-"]
  | '=' -> EQUAL["="]
  | '#' -> HASH["#"]
  ;



rmodifier : 
  TILDE CARAT? -> ^(BOOST CARAT?) ^(FUZZY TILDE) 
  | CARAT TILDE? -> ^(BOOST CARAT) ^(FUZZY TILDE?)
  ;


boost :
  (CARAT -> ^(BOOST NUMBER["DEF"])) // set the default value
  (NUMBER -> ^(BOOST NUMBER))? //replace the default with user input
  ;

fuzzy :
  (TILDE -> ^(FUZZY NUMBER["DEF"])) // set the default value
  (NUMBER -> ^(FUZZY NUMBER))? //replace the default with user input
  ;

not : 
  (AND NOT)=> AND NOT
  | NOT
  ;
  
and   : 
  AND
  ;
  
or  : 
  OR
  ;   

near  : 
  (NEAR -> ^(OPERATOR[$NEAR]) )
  ;

comma : 
  COMMA+
  ; 

semicolon
  :
  SEMICOLON+
  ;

date  : 
  //a=NUMBER '/' b=NUMBER '/' c=NUMBER -> ^(QDATE $a $b $c)
  DATE_TOKEN
  ;

identifier  
  : 
  //IDENTIFIER  
  ('doi:' -> QNORMAL["doi"]
  |'arxiv:' -> QNORMAL["arxiv"]
  |'arXiv:'  -> QNORMAL["arxiv"]
  |'scix:' -> QNORMAL["scix"])
  (TERM_NORMAL -> $identifier TERM_NORMAL
  | PHRASE_ANYTHING  -> $identifier ^(QPHRASETRUNC PHRASE_ANYTHING)
  | PHRASE -> $identifier ^(QPHRASE PHRASE)
  | NUMBER  -> $identifier NUMBER
  | STAR -> $identifier ^(QANYTHING STAR)
  )
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

MINUS : '-'; // decided not to catch long dashes: '–' | '\u2014' | '\u2012' | '\u2013'

STAR  : '*' ;

QMARK  : '?'+ ;


//LCURLY  : '{' ;

//RCURLY  : '}' ;

CARAT_PHRASE : {input.LA(2) == '"'}? '^';
CARAT : '^' NUMBER?;


TILDE : '~' NUMBER?;

DQUOTE  : '\"';

//SQUOTE  : '\'';

COMMA : ',';

SEMICOLON:  ';';
DOLLAR_AFTER_QUOTE
  : {isCaretPhraseDollar()}? '$'
  ;


fragment AS_CHAR
  :
  ~('0' .. '9' | ' ' | '"' | COMMA | PLUS | MINUS | '$' | LPAREN | RPAREN)
  ;
  
  
fragment ESC_CHAR:  '\\' .; 

TO  : 'TO';

/* We want to be case insensitive */
AND   : (('a' | 'A') ('n' | 'N') ('d' | 'D')) ;
OR  : (('o' | 'O') ('r' | 'R'));
NOT   : ('n' | 'N') ('o' | 'O') ('t' | 'T');
NEAR  : ('n' | 'N') ('e' | 'E') ('a' | 'A') ('r' | 'R') ('0'..'9')*;



  
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
  
//IDENTIFIER
//  : ('arXiv'|'arxiv') ':' TERM_CHAR+
//  |'doi:' TERM_CHAR+
//  //| INT+ '.' INT+ '/' INT+ ('.' INT+)?
//  ;

  

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
        | '\"' 
        | '(' | ')' | '[' | ']' | '{' | '}'
        | '+' | '-' | '!' | ':' | '~' | '^' 
        | '?' | '*' | '\\'|',' | '=' | '#'
        | ';'|'/'
        )
   | ESC_CHAR );    


fragment TERM_CHAR
  : 
  (TERM_START_CHAR  | '+' | '-' | '=' | '#' | '/')
  ;


  
DATE_TOKEN
  : 
  INT INT? ('/'|MINUS|'.') INT INT? ('/'|MINUS|'.') INT INT (INT INT)?
  ;

NUMBER  
  : 
  ('+'|'-')? INT+ ('.' INT+)?
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

fragment ROMAN_NUMERAL
  :
  ('I' | 'V' | 'X' | 'L' | 'C' | 'D' | 'M'
   | 'i' | 'v' | 'x' | 'l' | 'c' | 'd' | 'm')+
  ;

fragment ELEMENT_SYMBOL
  :
  ('A'..'Z') ('a'..'z')?
  ;

fragment BRACKET_ELEMENT_SYMBOL
  :
  ('A'..'Z') ('a'..'z')?
  ;

FORBIDDEN_LINE
  :
  {isForbiddenLine()}?=>
  ( ELEMENT_SYMBOL (' ' | '\t')* LBRACK (' ' | '\t')* ROMAN_NUMERAL (' ' | '\t')* RBRACK
  | LBRACK BRACKET_ELEMENT_SYMBOL (' ' | '\t')* ROMAN_NUMERAL (' ' | '\t')* RBRACK
  | ELEMENT_SYMBOL (' ' | '\t')* ROMAN_NUMERAL (' ' | '\t')* RBRACK
  )
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

PHRASE_ANYTHING : 
  DQUOTE (ESC_CHAR|~('\"'|'\\'))+ DQUOTE
  ;

LOCAL_PARAMS  : 
  '{!' (ESC_CHAR|~('}'|'\\'))+ '}'
  ;
REGEX : 
  '/' (ESC_CHAR|~('/'|'\\'))+ '/'
  ; 