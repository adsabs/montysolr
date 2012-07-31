package examples.invenio;

import java.io.IOException;
import java.net.MalformedURLException;
import junit.framework.Assert;

import monty.solr.util.MontySolrSetup;
import monty.solr.util.MontySolrTestCaseJ4;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.BeforeClass;

import examples.MontySolrJettyBase;

public class TestInvenioExample extends MontySolrJettyBase {

  @BeforeClass
  public static void beforeClass() throws Exception {
    MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome()
        + "/contrib/invenio/src/python");
    MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
    MontySolrSetup.addTargetsToHandler("monty_invenio.schema.targets");
  }

  public static String getSolrConfigFile() {
    return MontySolrTestCaseJ4.getSolrConfigFile();
  }

  public String getSolrHome() {
    String base = MontySolrSetup.getMontySolrHome();
    String exampleHome = base + "/build/contrib/examples/invenio";
    return exampleHome + "/solr";
  }

  public void setUp() throws Exception {

    server = createNewSolrServer();
  }

  public void tearDown() {
    server.shutdown();
  }

  public SolrQuery que(String... params) {
    SolrQuery q = new SolrQuery();
    for (int i = 0; i < params.length; i = i + 2) {
      q.set(params[i], params[i + 1]);
    }
    return q;
  }

  public void testAll() throws MalformedURLException, IOException,
      SolrServerException, InterruptedException {
    // Currently not an extensive test, but it does make sure the basic
    // functionality works

    server.deleteByQuery("*:*");
    server.commit(true, true);

    QueryResponse qr;

    qr = server.query(que("qt", "/invenio", "q", "*:*"));
    Assert.assertEquals(0, qr.getResults().getNumFound());

    qr = server.query(que("qt", "/invenio/update", "last_recid", "-1",
        "inveniourl", "http://localhost/search"
    // "inveniourl", java.net.URLEncoder.encode( "file://"
    // + MontySolrSetup.getMontySolrHome() +
    // "/contrib/invenio/src/test-files/data/demo-site.xml",
    // "UTF-8")
        ));
    Assert.assertEquals(0, qr.getStatus());
    Assert.assertEquals("busy", qr.getResponse().get("importStatus"));

    qr = server.query(que("qt", "/invenio/update"));
    while (qr.getResponse().get("importStatus").equals("busy")) {
      qr = server.query(que("qt", "/invenio/update"));
      Thread.sleep(50);
    }

    qr = server.query(que("qt", "/invenio", "q", "*:*"));
    Assert.assertEquals(0, qr.getResults().getNumFound());

    // no effect, becasuse no new recids found
    // qr = server.query(que("qt", "/invenio/update", "commit", "true"));
    Thread.sleep(5000);
    server.commit(true, true);

    qr = server.query(que("qt", "/invenio", "q", "*:*"));
    Assert.assertEquals(96, qr.getResults().getNumFound());

    qr = server.query(que("qt", "/invenio", "q", "#boson"));
    Assert.assertEquals(5, qr.getResults().getNumFound());
    Assert.assertEquals("<(ints,recid)boson>", qr.getResponse()
        .get("inv_query"));

    qr = server.query(que("qt", "/invenio", "q", "#boson", "inv.params",
        "of=hcs")); // of=hcs --> citation summary

    String cs1 = qr.getResponse().get("inv_response").toString();
    Assert.assertTrue(cs1.contains("id=\"citesummary\""));

    qr = server.query(que("qt", "/invenio", "q", "boson", "inv.params",
        "of=hcs")); // of=hcs --> citation summary
    String cs2 = qr.getResponse().get("inv_response").toString();

    Assert.assertTrue(cs2.contains("id=\"citesummary\""));

    Assert.assertTrue(cs1.equals(cs2));
  }
}
