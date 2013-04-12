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

package org.apache.solr.handler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.adsabs.solr.AdsConfig.F;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.util.LuceneTestCase.SuppressCodecs;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;

@SuppressCodecs({"Lucene3x", "SimpleText"})
public class TestBatchRequestHandler extends MontySolrQueryTestCase {


  private File generatedTransliterations;


  public String getSchemaFile() {
  	makeResourcesVisible(this.solrConfig.getResourceLoader(),
        new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
    });
  	return MontySolrSetup.getMontySolrHome()
  	    + "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
  }
  
  public String getSolrConfigFile() {
    return MontySolrSetup.getMontySolrHome()
    + "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
  }


  public void test() throws Exception {
    
    // now index some data
    assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk,", F.TYPE_ADS_TEXT, "what ever comes under"));
    assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M.", F.TYPE_ADS_TEXT, "my head is not what"));
    assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Marel", F.TYPE_ADS_TEXT, "goes in yours"));
    assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja", F.TYPE_ADS_TEXT, "the old warrior angels of angels"));
    assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja Karel", F.TYPE_ADS_TEXT, "great war, with horses"));
    assertU(commit());
    
    assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja K", F.TYPE_ADS_TEXT, "and heavy horses"));
    assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M K", F.TYPE_ADS_TEXT, "cutting down machine and by the machine"));
    assertU(adoc(F.ID, "9", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel Molja", F.TYPE_ADS_TEXT, "guns, from different angels"));
    assertU(adoc(F.ID, "10", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel M", F.TYPE_ADS_TEXT, "but still, British insisted"));
    assertU(adoc(F.ID, "11", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, K Molja", F.TYPE_ADS_TEXT, "as well as Germans did"));
    assertU(adoc(F.ID, "12", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "ǎguşan, Adrian, , Dr", F.TYPE_ADS_TEXT, "words are too weak..."));
    assertU(commit());
    
    BatchHandler handler = new BatchHandler();
    NamedList<String> nl = new NamedList<String>();
    handler.init(nl); // default
    
    
    //while (true) {
    	
    SolrQueryRequest req = req("command", "dump-index", "q", "*:*",
        "fields", "bibcode,title,author");
    SolrQueryResponse rsp = new SolrQueryResponse();
    
    SolrCore core = h.getCore();
    core.execute(handler, req, rsp);
    req.close();
    
    String jobid = (String) rsp.getValues().get("jobid");
    assert jobid != null;
    
    
    req = req("command", "start");
    rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);
    while (handler.isBusy()) {
      Thread.sleep(300);
    }
    req.close();
    
    req = req("command", "get-results", "jobid", jobid);
    rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);
    while (handler.isBusy()) {
      Thread.sleep(300);
    }
    
    StringWriter sw = new StringWriter(32000);
    QueryResponseWriter responseWriter = core.getQueryResponseWriter(req);
    responseWriter.write(sw,req,rsp);
    req.close();
    
    assert sw.toString().contains("\"bibcode\":[\"xxxxxxxxxxxxx\"],");
    assert sw.toString().contains("\"title\":[\"head\"]");
    //System.out.println(sw.toString());
    
    
    
    // ======================
    
    req = req("command", "dump-freqs", "q", "*:*",
        "fields", "bibcode,title,author");
    rsp = new SolrQueryResponse();
    
    core.execute(handler, req, rsp);
    req.close();
    
    jobid = (String) rsp.getValues().get("jobid");
    assert jobid != null;
    
    
    req = req("command", "start");
    rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);
    while (handler.isBusy()) {
      Thread.sleep(300);
    }
    req.close();
    
    req = req("command", "get-results", "jobid", jobid);
    rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);
    while (handler.isBusy()) {
      Thread.sleep(300);
    }
    
    sw = new StringWriter(32000);
    responseWriter = core.getQueryResponseWriter(req);
    responseWriter.write(sw,req,rsp);
    req.close();
    
    //System.out.println(sw.toString());
    assert sw.toString().contains("angels\t3\t2");
    
    
    //}
    
  }
  
  private void checkFile(String... expected) throws IOException {
    List<String> lines = h.getCore().getResourceLoader().getLines(generatedTransliterations.getAbsolutePath());
    for (String t: expected) {
      if (t.substring(0,1).equals("!")) {
        assertFalse("Present: " + t, lines.contains(t.substring(1)));
      }
      else {
        assertTrue("Missing: " + t, lines.contains(t));
      }
    }
  }
  
  

  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestBatchRequestHandler.class);
  }
}
