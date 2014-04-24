/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.update;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.InvenioDB.BatchOfInvenioIds;
import org.junit.BeforeClass;

/**
 * This extensive suite verifies that we are able to pick changes
 * that are happening inside Invenio. We are not running any actual
 * indexing, but we do test whether the handlers *would* be called.
 * We do not change anything in the Invenio DB, nor Solr index.
 * 
 * This test requires python access to Invenio demo-site. And we 
 * use the solr/example - to serve as solr installation.
 * 
 */
public class BlackBoxKeepRecidUpdated extends MontySolrAbstractTestCase {

  private String importurl = "http://localhost:8983/solr/import-dataimport";
  private String updateurl = "http://localhost:8983/solr/update-dataimport&dirs=x";
  private String deleteurl = "http://localhost:8983/solr/delete-dataimport";
  private String inveniourl = "http://inspirebeta.net/search";

  @BeforeClass
  public static void beforeBlackBoxKeepRecidUpdated() throws Exception {
    System.setProperty("storeAll", "true");
  }

  @Override
  public String getSchemaFile() {
    return MontySolrSetup.getMontySolrHome()
    + "/contrib/adsabs/src/test-files/solr/collection1/conf/schema-invenio-keeprecid-updater.xml";
  }

  @Override
  public String getSolrConfigFile() {
    return MontySolrSetup.getMontySolrHome()
    + "/contrib/adsabs/src/test-files/solr/collection1/conf/solrconfig-invenio-keeprecid-updater.xml";
  }

  public String getSolrHome() {
    System.clearProperty("solr.solr.home"); //always force field re-computation
    return MontySolrSetup.getSolrHome() + "/example/solr"; 

  }

  @Override
  public void setUp() throws Exception {
    System.setProperty("solr.directoryFactory", "StandardDirectoryFactory");
    super.setUp();
  }




  public void testIndexing() throws Exception {
    SolrCore core = h.getCore();
    MyInvenioKeepRecidUpdated handler = new MyInvenioKeepRecidUpdated();
    handler.setName("h-00");

    SolrQueryResponse rsp = new SolrQueryResponse();

    // fresh state, everything reset - we are not committing anything
    // and no upload is done (but we'll check the correct urls were
    // generated)


    // extected: everything is retrieved as new

    core.execute(handler, req("last_recid", "-1", 
        "inveniourl", inveniourl,
        "importurl", importurl,
        "updateurl", updateurl,
        "deleteurl", deleteurl), rsp);

    // must wait for the landler to finish his threads
    while (handler.isBusy()) {
      Thread.sleep(10);
    }

    List<Integer> added = handler.retrievedRecIds.added;
    List<Integer> updated = handler.retrievedRecIds.updated;
    List<Integer> deleted = handler.retrievedRecIds.deleted;

    assertTrue(handler.lastRecId == -1); // cause we havent indexed them
    assertTrue(added.size() >= 104);
    assertTrue(updated.size() == 0);
    assertTrue(deleted.size() == 0); // this can work only for fresh demo

    
    handler = new MyInvenioKeepRecidUpdated();
    core.execute(handler, req("last_recid", "-1", 
        "inveniourl", inveniourl,
        "importurl", importurl,
        "updateurl", updateurl,
        "deleteurl", deleteurl,
        "batchsize", "10"), rsp);

    // must wait for the landler to finish his threads
    while (handler.isBusy()) {
      Thread.sleep(10);
    }
    
    
    assertTrue(handler.retrievedRecIds.added.size() == 10);
    assertTrue(handler.retrievedRecIds.updated.size() == 0);
    assertTrue(handler.retrievedRecIds.deleted.size() == 0); 
    
  }


  public class MyInvenioKeepRecidUpdated extends InvenioKeepRecidUpdated {

    public BatchOfInvenioIds retrievedRecIds;
    public Integer lastRecId;
    public Integer lastUpdatedRecId;
    private int stage = 0;
    private String[] stages = new String[]{importurl, updateurl, deleteurl};
    public List<Integer> added = null;
    public List<Integer> updated = null;
    public List<Integer> deleted = null;
    public String tName = null;

    public void setName(String name) {
      tName = name;
    }
    public void skipStage() {
      stage++;
    }

    @SuppressWarnings("unchecked")
    protected  BatchOfInvenioIds retrieveRecids(Properties prop, 
        SolrQueryRequest req, SolrQueryResponse rsp) throws SQLException {

      if (prop.containsKey("last_recid")) {
        lastRecId = Integer.valueOf(prop.getProperty("last_recid"));
      }

      BatchOfInvenioIds ret = super.retrieveRecids(prop, req, rsp);
      if (ret != null) {
        retrievedRecIds = ret;
        lastUpdatedRecId = ret.lastRecid;
      }
      return ret;
    }

    @Override
    protected void runUpload(List<String> urlsToFetch) throws MalformedURLException, IOException, InterruptedException {
      while (urlsToFetch.size() > 0) {
        String url = urlsToFetch.remove(0);
        assertTrue("Name: " + tName + "\nStage: " + stage + "\nUrl: " + url + "\nDoest not contain: " + inveniourl,
            url.contains(java.net.URLEncoder.encode(inveniourl, "UTF-8")));
        assertTrue("Name: " + tName + "\nStage: " + stage + "\nUrl: " + url + "\nDoest not contain: " + stages[stage],
            url.contains(stages[stage]));
        urlsToFetch.clear(); // we want just once
      }
      stage++;
    }

    @Override
    protected void runProcessingAdded(List<Integer> recids, SolrQueryRequest req) throws IOException {
      added = recids;
      super.runProcessingAdded(recids, req);
    }
    @Override
    protected void runProcessingDeleted(List<Integer> recids, SolrQueryRequest req) throws IOException {
      deleted = recids;
      super.runProcessingDeleted(recids, req);
    }
    @Override
    protected void runProcessingUpdated(List<Integer> recids, SolrQueryRequest req) throws IOException {
      updated = recids;
      super.runProcessingUpdated(recids, req);
    }

  }


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(BlackBoxKeepRecidUpdated.class);
  }
}
