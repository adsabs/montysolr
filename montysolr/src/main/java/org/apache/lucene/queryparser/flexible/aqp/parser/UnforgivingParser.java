package org.apache.lucene.queryparser.flexible.aqp.parser;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;

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
	throws RecognitionException
	{
		//do not try to recover
		MismatchedTokenException e = new MismatchedTokenException(ttype, input);
		throw e;
	}
	

}
