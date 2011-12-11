package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.core.parser.SyntaxParser;

public interface AqpSyntaxParser extends SyntaxParser {
	/**
	   * @param grammarName
	   *          - the name of the query
	   * @throws Exception
	   * 		  - there are different implementations (should we want different interfaces?)
	   * 		    some may be loading grammars on the fly, others will load grammars directly
	   *  
	   * @return AqpSyntaxParser
	   */
	  public AqpSyntaxParser initializeGrammar(String grammarName)
	      throws Exception;
}
