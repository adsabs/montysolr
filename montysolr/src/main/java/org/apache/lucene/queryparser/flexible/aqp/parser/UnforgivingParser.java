package org.apache.lucene.queryparser.flexible.aqp.parser;

import org.antlr.runtime.*;

public class UnforgivingParser extends Parser {

    public UnforgivingParser(TokenStream input) {
        super(input);
        // TODO Auto-generated constructor stub
    }

    public UnforgivingParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)
            throws RecognitionException {
        //do not try to recover
        MismatchedTokenException e = new MismatchedTokenException(ttype, input);
        throw e;
    }


}
