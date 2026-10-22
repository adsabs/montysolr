package monty.solr.util;


import org.apache.lucene.tests.util.LuceneTestCase;
import org.junit.AfterClass;


public abstract class MontySolrAbstractLuceneTestCase extends LuceneTestCase {

    @AfterClass
    public static void afterClassMontySolrTestCase() throws Exception {
        System.clearProperty("montysolr.home");
        System.clearProperty("solr.test.sys.prop1");
        System.clearProperty("solr.test.sys.prop2");
    }

}
