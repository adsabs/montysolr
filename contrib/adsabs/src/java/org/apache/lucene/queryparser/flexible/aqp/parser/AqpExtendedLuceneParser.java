package org.apache.lucene.queryparser.flexible.aqp.parser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParserLoadableImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;

public class AqpExtendedLuceneParser {

  /**
   * Constructs a {@link StandardQueryParser} object. The default grammar used
   * is "LuceneGrammar" {@link AqpQueryParser}
   * 
   * @throws QueryNodeParseException
   *    Syntactic/semantic error occured
   */

  public static AqpQueryParser init(String grammarName)
      throws QueryNodeParseException {
    return new AqpQueryParser(new AqpStandardQueryConfigHandler(),
        new AqpSyntaxParserLoadableImpl().initializeGrammar(grammarName),
        new AqpStandardQueryNodeProcessorPipeline(null),
        new AqpExtendedLuceneQueryTreeBuilder());
  }

  /**
   * Instantiates {@link StandardLuceneGrammarSyntaxParser}, this method
   * is using a dedicated parser class, instead of loading the parser
   * by its grammar name
   * 
   * @throws QueryNodeParseException
   *  Syntactic/semantic error occured
   */
  public static AqpQueryParser init() throws QueryNodeParseException {
  	return new AqpQueryParser(new AqpStandardQueryConfigHandler(),
  			new ExtendedLuceneGrammarSyntaxParser(),
        new AqpStandardQueryNodeProcessorPipeline(null),
        new AqpExtendedLuceneQueryTreeBuilder());
  	
  }

  /**
   * Constructs a {@link StandardQueryParser} object and sets an
   * {@link Analyzer} to it. The same as:
   * 
   * <pre>
   * StandardQueryParser qp = new StandardQueryParser();
   * qp.getQueryConfigHandler().setAnalyzer(analyzer);
   * </pre>
   * 
   * @param analyzer
   *          the analyzer to be used by this query parser helper
   */
  public AqpQueryParser init(Analyzer analyzer) throws Exception {
    AqpQueryParser p = AqpStandardLuceneParser.init("ExtendedLuceneGrammar");
    p.setAnalyzer(analyzer);
    return p;
  }
}
