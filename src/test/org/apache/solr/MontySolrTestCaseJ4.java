package org.apache.solr;

import java.io.File;

import montysolr.util.MontySolrSetup;

import org.junit.AfterClass;
import org.junit.BeforeClass;


public class MontySolrTestCaseJ4 extends SolrTestCaseJ4 {
  
  @BeforeClass
  public static void beforeClassMontySolrTestCase() throws Exception {
    MontySolrSetup.init("montysolr.java_bridge.SimpleBridge",
        MontySolrSetup.getMontySolrHome() + "/src/python");
  }

  @AfterClass
  public static void afterClassMontySolrTestCase() throws Exception {
    
  }
  
  public static String TEST_HOME() {
    return MontySolrSetup.getSolrHome();
  }
  
  public static String MONTYSOLR_HOME = MontySolrSetup.getMontySolrHome();
  private static final String SOLR_HOME = MontySolrSetup.getSolrHome();
  private static final String SOURCE_HOME = MONTYSOLR_HOME
      + "/src/test-files";
  public static String TEST_HOME = SOURCE_HOME + "/solr";
  public static String WEBAPP_HOME = new File(SOLR_HOME, "src/webapp/web")
      .getAbsolutePath();
  public static String EXAMPLE_HOME = new File(SOLR_HOME, "example/solr")
      .getAbsolutePath();
  public static String EXAMPLE_MULTICORE_HOME = new File(SOLR_HOME,
      "example/multicore").getAbsolutePath();
  public static String EXAMPLE_SCHEMA = EXAMPLE_HOME + "/conf/schema.xml";
  public static String EXAMPLE_CONFIG = EXAMPLE_HOME + "/conf/solrconfig.xml";
  
  
  
}
