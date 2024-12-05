package monty.solr.util;


import org.apache.lucene.tests.util.LuceneTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;


public abstract class MontySolrAbstractLuceneTestCase extends LuceneTestCase {

    @BeforeClass
    public static void beforeClassMontySolrTestCase() throws Exception {
        envInit();
    }

    @AfterClass
    public static void afterClassMontySolrTestCase() throws Exception {
        System.clearProperty("montysolr.home");
        System.clearProperty("solr.test.sys.prop1");
        System.clearProperty("solr.test.sys.prop2");
    }

    public void tearDown() throws Exception {
        System.clearProperty("python.cachedir.skip");
        System.clearProperty("python.console.encoding");
        super.tearDown();
    }

    /**
     * Must be called first, so that we make sure
     * properties are set (?)
     */
    public static void envInit() throws Exception {
        // do nonno
    }


}
