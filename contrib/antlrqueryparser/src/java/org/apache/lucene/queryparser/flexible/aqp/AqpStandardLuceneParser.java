package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;

public class AqpStandardLuceneParser {

  /**
   * Constructs a {@link StandardQueryParser} object. The default grammar used
   * is "LuceneGrammar" {@see AqpQueryParser#AqpQueryParser(String)}
   * 
   * @throws Exception
   */

  public static AqpQueryParser init(String grammarName)
      throws QueryNodeParseException {
    return new AqpQueryParser(new AqpStandardQueryConfigHandler(),
        new AqpSyntaxParserLoadableImpl().initializeGrammar(grammarName),
        new AqpStandardQueryNodeProcessorPipeline(null),
        new AqpStandardQueryTreeBuilder());
  }

  public static AqpQueryParser init() throws QueryNodeParseException {
    return AqpStandardLuceneParser.init("StandardLuceneGrammar");
  }

  /**
   * Constructs a {@link StandardQueryParser} object and sets an
   * {@link Analyzer} to it. The same as:
   * 
   * <ul>
   * StandardQueryParser qp = new StandardQueryParser();
   * qp.getQueryConfigHandler().setAnalyzer(analyzer);
   * </ul>
   * 
   * @param analyzer
   *          the analyzer to be used by this query parser helper
   * @throws Exception
   */
  public AqpQueryParser init(Analyzer analyzer) throws Exception {
    AqpQueryParser p = AqpStandardLuceneParser.init("StandardLuceneGrammar");
    p.setAnalyzer(analyzer);
    return p;
  }
}
