package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpNearQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardLuceneParser;

/**
 * This test case is extending {@link TestAqpSLGSimple}
 * we just use a different grammar
 * 
 * Just an example to show how easy it is to add new 
 * functionality to the parser. 
 * 
 *   1. change grammar
 *   2. add new builders/processors
 *   
 * Note: If the ExtendedLuceneGrammar was a separate parser,
 * it is much better to create its own configuration. See
 * {@link AqpStandardLuceneParser#init(String)} for details 
 * 
 */
public class TestAqpExtendedLGSimple extends TestAqpSLGSimple {


  @Override
  public void setUp() throws Exception {
    super.setUp();
    setGrammarName("ExtendedLuceneGrammar");
  }

  public void testExtensions() throws Exception {

    AqpQueryParser qp = getParser();
    ((AqpQueryTreeBuilder) qp.getQueryBuilder()).setBuilder(AqpNearQueryNode.class,	new AqpNearQueryNodeBuilder());
    
    assertQueryMatch(qp, "this NEAR that", "field",
        "spanNear([field:this, field:that], 5, false)");

    assertQueryMatch(qp, "this NEAR3 that", "field",
    		"spanNear([field:this, field:that], 3, false)");
    
    assertQueryMatch(qp, "this NEAR3 (that OR foo*)", "field",
				"spanNear([field:this, spanOr([field:that, SpanMultiTermQueryWrapper(field:foo*)])], 3, false)");

    
  }

}
