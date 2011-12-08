package org.apache.lucene.queryParser.aqp;

import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.QueryParserHelper;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.standard.StandardQueryParser;
import org.apache.lucene.queryParser.standard.config.DefaultOperatorAttribute.Operator;
import org.apache.lucene.queryParser.standard.processors.StandardQueryNodeProcessorPipeline;

public class TestAqpQPHelperInvenio extends TestAqpQPHelper {
	
	@Override
	public AqpQueryParser getParser(Analyzer a) throws Exception {
	    if (a == null)
	      a = new SimpleAnalyzer(TEST_VERSION_CURRENT);
	    AqpQueryParser qp = getParser();
	    qp.setAnalyzer(a);

	    qp.setDefaultOperator(Operator.OR);
	    qp.setDebug(this.debugParser);
	    return qp;

	  }
	  
	  public QueryParserHelper getParser(boolean standard) throws Exception {
		  
		  class DebuggingQueryNodeProcessorPipeline extends StandardQueryNodeProcessorPipeline {
			  DebuggingQueryNodeProcessorPipeline(QueryConfigHandler queryConfig) {
				  super(queryConfig);
			  }
			  public QueryNode process(QueryNode queryTree) throws QueryNodeException {
					String oldVal = null;
					String newVal = null;
					
					oldVal = queryTree.toString();
					int i = 1;
					System.out.println("     0. starting");
					System.out.println("--------------------------------------------");
					System.out.println(oldVal);
					
					Iterator<QueryNodeProcessor> it = this.iterator();

					QueryNodeProcessor processor;
					while (it.hasNext()) {
						processor = it.next();
						
						System.out.println("     " + i + ". step "	+ processor.getClass().toString());
						queryTree = processor.process(queryTree);
						newVal = queryTree.toString();
						System.out.println("     Tree changed: " + (newVal.equals(oldVal) ? "NO" : "YES"));
						System.out.println("--------------------------------------------");
						System.out.println(newVal);
						oldVal = newVal;
						i += 1;
					}
					
					System.out.println("");
					System.out.println("final result:");
					System.out.println("--------------------------------------------");
					System.out.println(queryTree.toString());
					return queryTree;

				}
		  }
		  if (standard) {
			  StandardQueryParser sp = new StandardQueryParser();
			  sp.setQueryNodeProcessor(new DebuggingQueryNodeProcessorPipeline(sp.getQueryConfigHandler()));
			  return sp;
		  }
		  else {
		      return new AqpQueryParserInvenio();
		  }
	  }
	  
	  public AqpQueryParser getParser() throws Exception {
		  AqpQueryParser qp = new AqpQueryParserInvenio();
		  return qp;
	  }
	  
	  public void testAmbiguity() throws Exception {
		    super.assertQueryEquals("e(+)e(-)", null, "e(+)e(-)");
		}
}
