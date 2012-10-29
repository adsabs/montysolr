

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.lucene.queryparser.flexible.aqp.TestAqpAdsabs;
import org.apache.lucene.queryparser.flexible.aqp.TestAqpSLGMultiField;
import org.apache.lucene.queryparser.flexible.aqp.TestAqpSLGSimple;
import org.apache.lucene.queryparser.flexible.aqp.TestAqpSLGStandardTest;
import org.apache.lucene.queryparser.flexible.aqp.TestAqpSLGMultiAnalyzer;
import org.apache.lucene.queryparser.flexible.aqp.TestAqpInvenioSimple;
import org.apache.solr.search.TestInvenioQueryParser;
import org.apache.solr.search.TestAqpAdsabsSolrSearch;
import org.apache.solr.analysis.author.TestAdsAuthorParsing;

/*
 * I am using this to run several unittest at once from Eclipse,
 * please ignore...
 */
public class EclipseCheckParsers {

	public static Test suite() {
		TestSuite suite = new TestSuite(EclipseCheckParsers.class.getName());
		//$JUnit-BEGIN$
		suite.addTest(TestAqpSLGSimple.suite());
		suite.addTest(TestAqpSLGMultiField.suite());
		suite.addTest(TestAqpSLGStandardTest.suite());
		suite.addTest(TestAqpSLGMultiAnalyzer.suite());
		
		suite.addTest(TestAqpInvenioSimple.suite());
		suite.addTest(TestInvenioQueryParser.suite());
		
		
		suite.addTest(TestAqpAdsabs.suite());
		suite.addTest(TestAqpAdsabsSolrSearch.suite());
		suite.addTest(TestAdsAuthorParsing.suite());
		//$JUnit-END$
		return suite;
	}

}
