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

package examples.adsabs;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import monty.solr.util.MontySolrSetup;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.FailSafeInvenioNoRollbackWriter;
import org.apache.solr.handler.dataimport.WaitingDataImportHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.InvenioImportBackup;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.junit.BeforeClass;

import examples.BlackAbstractTestCase;

/**
 * Tests that invenio-doctor is able to re-start the failed batches
 * NOTE: this is using the Invenio demo site, but because ADSABS
 * is configured to recognize records that have bibcode inthe field
 * 970__a then we get only 22 recs from the Invenio demo.
 * 
 */
public class BlackBoxFailingRecords extends BlackAbstractTestCase {

  @BeforeClass
  public static void beforeBlackBoxFailingRecords() throws Exception {
    setEName("adsabs");
    exampleInit();
    MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
    MontySolrSetup.addTargetsToHandler("monty_invenio.schema.targets");
  }

  @Override
  public String getSolrConfigFile() {
    String configFile = MontySolrSetup.getMontySolrHome()
      + "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
    String dataConfig = MontySolrSetup.getMontySolrHome()
      + "/contrib/examples/adsabs/solr/collection1/conf/data-config.xml";
    File dc = duplicateModify(new File(dataConfig), "mongoField=\"true\"", "mongoField=\"false\"");
    
    File newConfig = duplicateModify(new File(configFile), "data-config.xml", dc.getAbsolutePath());
    return newConfig.getAbsolutePath();
    
  }


  public void testImport() throws Exception {

    
    WaitingDataImportHandler handler = (WaitingDataImportHandler) h.getCore().getRequestHandler("/invenio/import");
    SolrCore core = h.getCore();


    SolrQueryRequest req = req("command", "full-import",
        "commit", "true",
        "url", "python://search?p=" + URLEncoder.encode("recid:1->104", "UTF-8")
    );
    SolrQueryResponse rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);


    assertU(commit());
    assertQ(req("q", "*:*", "fl", "recid,title"), "//*[@numFound='22']");
    assertQ(req("q", "id:84"), "//*[@numFound='1']");
    assertQ(req("q", "id:78"), "//*[@numFound='1']");
    assertQ(req("q", "abstract:\"Hubbard-Stratonovich\""), "//*[@numFound='1']");


    // clean the slate
    assertU(delQ("*:*"));
    assertU(commit());
    assertQ(req("q", "*:*"), "//*[@numFound='0']");
    
    failThis.put("78", true);
    failThis.put("84", true);

    req = req("command", "full-import",
        "commit", "true",
        "writerImpl", TestFailingWriter.class.getName(),
        "url", "python://search?p=" + URLEncoder.encode("recid:1->60 OR recid:61->104", "UTF-8")
    );
    rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);


    assertQ(req("qt", "/invenio-doctor", "command", "info"), 
        "//str[@name='queueSize'][.='3']",
        "//str[@name='failedRecs'][.='0']",
        "//str[@name='failedBatches'][.='0']",
        "//str[@name='failedTotal'][.='0']",
        "//str[@name='registeredRequests'][.='3']",
        "//str[@name='restartedRequests'][.='0']",
        "//str[@name='docsToCheck'][.='103']",
        "//str[@name='status'][.='idle']"
    );
    assertQ(req("qt", "/invenio-doctor", "command", "detailed-info"), 
        "//str[@name='queueSize'][.='3']",
        "//str[@name='failedRecs'][.='0']",
        "//str[@name='failedBatches'][.='0']",
        "//str[@name='failedTotal'][.='0']",
        "//str[@name='registeredRequests'][.='3']",
        "//str[@name='restartedRequests'][.='0']",
        "//str[@name='docsToCheck'][.='103']",
        "//str[@name='status'][.='idle']",
        "*[count(//arr[@name='toBeDone']/str)=3]",
        "*[count(//arr[@name='failedBatches']/str)=0]"
    );


    InvenioImportBackup doctor = (InvenioImportBackup) h.getCore().getRequestHandler("/invenio-doctor");
    req = req("command", "start");
    rsp = new SolrQueryResponse();
    core.execute(doctor, req, rsp);

    while (doctor.isBusy()) {
      assertQ(req("qt", "/invenio-doctor", "command", "info"), 
          "//str[@name='status'][.='busy']"
          );
      Thread.sleep(300);
    }

    assertQ(req("qt", "/invenio-doctor", "command", "info"), 
        "//str[@name='status'][.='idle']"
    );

    assertQ(req("q", "*:*"), "//*[@numFound='20']");
    assertQ(req("q", "id:84"), "//*[@numFound='0']");
    assertQ(req("q", "id:83"), "//*[@numFound='1']");
    assertQ(req("q", "id:85"), "//*[@numFound='1']");
    
    assertQ(req("q", "id:78"), "//*[@numFound='0']");
    assertQ(req("q", "id:77"), "//*[@numFound='1']");
    assertQ(req("q", "id:79"), "//*[@numFound='1']");
    

    String response = h.query("/invenio-doctor", req("qt", "/invenio-doctor", "command", "detailed-info"));

    //System.out.println(response);

    assertQ(req("qt", "/invenio-doctor", "command", "info"), 
        "//str[@name='queueSize'][.='0']",
        "//str[@name='failedRecs'][.='2']",
        "//str[@name='failedBatches'][.='0']",
        "//str[@name='failedTotal'][.='2']",
        "//str[@name='registeredRequests'][.='21']",
        "//str[@name='restartedRequests'][.='21']",
        "//str[@name='docsToCheck'][.='0']",
        "//str[@name='status'][.='idle']"
    );

    
    assertQ(req("qt", "/invenio-doctor", "command", "reset"));
    assertQ(req("qt", "/invenio-doctor", "command", "info"), 
        "//str[@name='queueSize'][.='0']",
        "//str[@name='failedRecs'][.='0']");
        
    // now we expect the least possible number of restarts
    req = req("command", "full-import",
        "commit", "true",
        "writerImpl", TestFailingWriter.class.getName(),
        "url", "python://search?p=" + URLEncoder.encode("recid:82->86", "UTF-8")
    );
    rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);
    
    assertQ(req("qt", "/invenio-doctor", "command", "info"), 
        "//str[@name='queueSize'][.='3']",
        "//str[@name='failedRecs'][.='0']",
        "//str[@name='failedBatches'][.='0']",
        "//str[@name='failedTotal'][.='0']",
        "//str[@name='registeredRequests'][.='3']",
        "//str[@name='restartedRequests'][.='0']",
        "//str[@name='docsToCheck'][.='3']",
        "//str[@name='status'][.='idle']"
    );
    
    req = req("command", "start");
    rsp = new SolrQueryResponse();
    core.execute(doctor, req, rsp);

    while (doctor.isBusy()) {
      Thread.sleep(300);
    }
    
    assertQ(req("qt", "/invenio-doctor", "command", "info"), 
        "//str[@name='queueSize'][.='0']",
        "//str[@name='failedRecs'][.='1']",
        "//str[@name='failedBatches'][.='0']",
        "//str[@name='failedTotal'][.='1']",
        "//str[@name='registeredRequests'][.='3']",
        "//str[@name='restartedRequests'][.='3']",
        "//str[@name='docsToCheck'][.='0']",
        "//str[@name='status'][.='idle']"
    );

    
    // now test the new component which looks inside the invenio db
    // discovers the missing recs and calls indexing on them
    
    assertU(commit());
    assertQ(req("q", "recid:77 OR recid:80"), "//*[@numFound='2']");
    assertU(delQ("recid:77 OR recid:80"));
    
    // this is necessary, otherwise the results may be wrong because
    // lucene cache is used to compare records (and the cache may
    // contain deleted records even after the commit)
    assertU(commit("expungeDeletes", "true"));
    assertQ(req("q", "recid:77 OR recid:80"), "//*[@numFound='0']");
    
    assertQ(req("qt", "/invenio-doctor", "command", "info"), 
        "//str[@name='queueSize'][.='0']",
        "//str[@name='failedRecs'][.='1']",
        "//str[@name='failedBatches'][.='0']",
        "//str[@name='failedTotal'][.='1']",
        "//str[@name='registeredRequests'][.='3']",
        "//str[@name='restartedRequests'][.='3']",
        "//str[@name='docsToCheck'][.='0']",
        "//str[@name='status'][.='idle']"
    );
    
    req = req("command", "discover", "params", "batchSize=50");
    rsp = new SolrQueryResponse();
    core.execute(doctor, req, rsp);

    while (doctor.isBusy()) {
      Thread.sleep(300);
    }

    req = req("command", "start");
    rsp = new SolrQueryResponse();
    core.execute(doctor, req, rsp);
    
    while (doctor.isBusy()) {
      Thread.sleep(300);
    }
    
    
    assertU(commit());
    
    assertQ(req("q", "recid:77 OR recid:80"), "//*[@numFound='2']");
    
    assertQ(req("qt", "/invenio-doctor", "command", "info"), 
        "//str[@name='queueSize'][.='0']",
        "//str[@name='failedRecs'][.='1']",
        "//str[@name='failedBatches'][.='0']",
        "//str[@name='failedTotal'][.='1']",
        "//str[@name='registeredRequests'][.='6']",
        "//str[@name='restartedRequests'][.='6']",
        "//str[@name='docsToCheck'][.='0']",
        "//str[@name='status'][.='idle']"
    );
    
    assertQ(req("qt", "/invenio-doctor", "command", "show-missing"), 
        "//str[@name='queueSize'][.='0']",
        "//str[@name='failedRecs'][.='1']",
        "//str[@name='failedBatches'][.='0']",
        "//str[@name='failedTotal'][.='1']",
        "//str[@name='registeredRequests'][.='6']",
        "//str[@name='restartedRequests'][.='6']",
        "//str[@name='docsToCheck'][.='0']",
        "//str[@name='status'][.='idle']",
        "//arr[@name='missingRecs']/int[.='77']",
        "//arr[@name='missingRecs']/int[.='80']"
    );
    
    assertQ(req("qt", "/invenio-doctor", "command", "reset"), null);
    assertQ(req("qt", "/invenio-doctor", "command", "discover",
        "params", "batchSize=1&fetch_size=2&max_records=1"), null);
    req = req("command", "start");
    rsp = new SolrQueryResponse();
    core.execute(doctor, req, rsp);
    
    while (doctor.isBusy()) {
      Thread.sleep(300);
    }
    
    assertU(commit());
    assertQ(req("q", "recid:77 OR recid:80"), "//*[@numFound='2']");
    
    /*
    assertQ(req("qt", "/invenio-doctor", "command", "detailed-info"), 
        "//str[@name='registeredRequests'][.='9']",
        "//str[@name='restartedRequests'][.='9']",
        "//str[@name='status'][.='idle']"
    );
    */
    
  }

  public static final Map<String, Boolean> failThis = new HashMap<String, Boolean>();
  public static final Map<String, Integer> alreadyFailed = new HashMap<String, Integer>();
  
  public static class TestFailingWriter extends FailSafeInvenioNoRollbackWriter {
    
    List<Integer> allowedIds = null;
    
    public TestFailingWriter(UpdateRequestProcessor processor,
        SolrQueryRequest req) {
      super(processor, req);
    }
    
    @Override
    public void rollback() {
      super.rollback();
    }
    
    @Override
    public boolean upload(SolrInputDocument d) {
      SolrInputField f = d.getField("id");
      String val = (String) f.getFirstValue();
      
      if (failThis.containsKey(val)) {
        if (!alreadyFailed.containsKey(val)) {
          alreadyFailed.put(val, 0);
        }
        alreadyFailed.put(val, alreadyFailed.get(val)+1);
        throw new IllegalStateException("Causing rollback to be called! Id: " + val);
      }
      return super.upload(d);
    }
    
  }
  
  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(BlackBoxFailingRecords.class);
  }
}
