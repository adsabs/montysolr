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

package org.apache.solr.handler.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.adsabs.solr.AdsConfig.F;
import org.apache.lucene.util.LuceneTestCase.SuppressCodecs;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.batch.BatchHandler;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;

@SuppressCodecs({"Lucene3x", "SimpleText"})
public class TestBatchRequestHandler extends MontySolrQueryTestCase {
	
  private File generatedTransliterations;
	private BatchHandler handler;

	@BeforeClass
	public static void beforeClass() throws Exception {
		
		makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
			    MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
		      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
		    });
				
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome()
  	      + "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
		
		configString = MontySolrSetup.getMontySolrHome()
          + "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
		
		initCore(configString, schemaString, MontySolrSetup.getSolrHome()
			    + "/example/solr");
	}
	


  public void test() throws Exception {
    
    // now index some data
    assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk,", F.TYPE_ADS_TEXT, "what ever comes under"));
    assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M.", F.TYPE_ADS_TEXT, "my head is not what"));
    assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Marel", F.TYPE_ADS_TEXT, "goes in yours"));
    assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja", F.TYPE_ADS_TEXT, "the old warrior angels of angels"));
    assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja Karel", F.TYPE_ADS_TEXT, "great war, with horses"));
    assertU(commit());
    
    assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxx7", F.AUTHOR, "Adamčuk, Molja K", F.TYPE_ADS_TEXT, "and heavy horses"));
    assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M K", F.TYPE_ADS_TEXT, "cutting down machine and by the machine"));
    assertU(adoc(F.ID, "9", F.BIBCODE, "xxxxxxxxxxxx9", F.AUTHOR, "Adamčuk, Karel Molja", F.TYPE_ADS_TEXT, "guns, from different angels"));
    assertU(adoc(F.ID, "10", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel M", F.TYPE_ADS_TEXT, "but still, British insisted"));
    assertU(adoc(F.ID, "11", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, K Molja", F.TYPE_ADS_TEXT, "as well as Germans did"));
    assertU(adoc(F.ID, "12", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "ǎguşan, Adrian, , Dr", F.TYPE_ADS_TEXT, "words are too weak..."));
    assertU(commit());
    
    
    //if (true) {
	    handler = new BatchHandler();
	    NamedList<Object> defaults = new NamedList<Object>();
	    defaults.add("allowed", ".*");
	    defaults.add("asynchronous", true);
	    defaults.add("workdir", "batch-handler");
	    
	    NamedList<Object> providers = new NamedList<Object>();
	    /*
	    providers.add("dump-index", "org.apache.solr.handler.batch.BatchProviderDumpIndexFields");
	    providers.add("dump-index-use-bibcodes", "org.apache.solr.handler.batch.BatchProviderDumpBibcodes");
	    providers.add("dump-freqs", "org.apache.solr.handler.batch.BatchProviderDumpTermFreqs");
	    providers.add("dump-docs", "org.apache.solr.handler.batch.BatchProviderDumpIndex");
	    providers.add("dump-citation-index", "org.apache.solr.handler.batch.BatchProviderDumpCitationCache");
	    providers.add("find-freq-phrases", "org.apache.solr.handler.batch.BatchProviderFindWordGroups");
	    */
	    providers.add("_test-params", "org.apache.solr.handler.batch.TestBatchRequestHandler$TestProvider");
	    providers.add("_fail", "org.apache.solr.handler.batch.TestBatchRequestHandler$TestProviderFail");
	    
	    NamedList<Object> nl = new NamedList<Object>();
	    nl.add("defaults", defaults);
	    nl.add("providers", providers);
	    handler.init(nl); // default
    //}
    //else {
    //	handler = (BatchHandler) h.getCore().getRequestHandler("/batch");
    //}
    //handler = (BatchHandler) h.getCore().getRequestHandler("/batch");
    
    //while (true) {
	    
	  // ========================================
	    
	    
	  String jobid;
	  
    jobid = run(req("command", "_test-params", 
    		"q", "{!aqp} lang:(german OR english) AND *:*",
        "fields", "bibcode,title,author"),
        "foo\nzooo\nščř");
    
    
    BatchHandlerRequestQueue thisQueue = queue.remove(0);
    SolrParams thisParams = params.remove(0);
    
    assertEquals("{!aqp} lang:(german OR english) AND *:*", thisParams.get("q"));
    assertEquals(true, thisParams.getBool("asynchronous"));
    assertEquals("batch-handler", thisParams.get("workdir"));
    assertEquals("bibcode,title,author", thisParams.get("fields"));
    assertEquals(jobid, thisParams.get("jobid"));
    assertEquals("foo\nzooo\nščř", thisParams.get("#data"));
    
    assert thisQueue.isJobidFailed(jobid) == false;
    assert thisQueue.isJobidFinished(jobid) == true;
    assert thisQueue.isJobidRegistered(jobid) == true;
    assert thisQueue.isJobidRunning(jobid) == false;
    
    String data = getResponse(req("command", "get-results", "jobid", jobid));
    assert data.contains("test-handler jobid:" + jobid);
    
	  // ========================================
    	
    // make it fail
    
    jobid = run(req("command", "_fail", 
    		"q", "{!aqp} lang:(german OR english) AND *:*",
        "fields", "bibcode,title,author"));
    data = getResponse(req("command", "status", 
    		"jobid", jobid, "wt", "json", "indent", "true"));
    
    assert thisQueue.isJobidFailed(jobid) == true;
    assert thisQueue.isJobidFinished(jobid) == false;
    assert thisQueue.isJobidRegistered(jobid) == true;
    assert thisQueue.isJobidRunning(jobid) == false;
    
    assert data.contains("Woooot!");
    assert data.contains("org.apache.solr.handler.batch.TestBatchRequestHandler$TestProviderFail.run");
    
    
  }
  
  
  private String getResponse(SolrQueryRequest req) throws IOException, InterruptedException {
  	SolrQueryResponse rsp = new SolrQueryResponse();
  	try {
	    h.getCore().execute(handler, req, rsp);
	    while (handler.isBusy()) {
	      Thread.sleep(100);
	    }
	    
	    StringWriter sw = new StringWriter(32000);
	    QueryResponseWriter responseWriter = h.getCore().getQueryResponseWriter(req);
	    responseWriter.write(sw,req,rsp);
	    return sw.toString();
  	}
  	finally {
  		req.close();
  	}
  }

  private String run(SolrQueryRequest req) throws InterruptedException {
  	return run(req, null);
  }
  
	private String run(SolrQueryRequest req, String data) throws InterruptedException {
    SolrQueryResponse rsp = new SolrQueryResponse();
    try {
	    SolrCore core = h.getCore();
	    core.execute(handler, req, rsp);
	    req.close();
	    
	    String jobid = (String) rsp.getValues().get("jobid");
	    assert jobid != null;
	    
	    if (data != null) {
		    // now send data to be used for dumping
		    req = req("command", "receive-data", "jobid", jobid);
		    rsp = new SolrQueryResponse();
		    List<ContentStream> cs = new ArrayList<ContentStream>(1);
		    ContentStreamBase f = new ContentStreamBase.StringStream(data);
		    cs.add(f);
		    ((LocalSolrQueryRequest)req).setContentStreams(cs);
		    core.execute(handler, req, rsp);
		    while (handler.isBusy()) {
		    	Thread.sleep(100);
		    }
		    req.close();
	    }
	    
	    
	    req = req("command", "start");
	    rsp = new SolrQueryResponse();
	    core.execute(handler, req, rsp);
	    while (handler.isBusy()) {
	      Thread.sleep(100);
	    }
	    req.close();
	    return jobid;
    }
    finally {
    	req.close();
    }
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
  
  
  private static List<BatchHandlerRequestQueue> queue = new ArrayList<BatchHandlerRequestQueue>();
  private static List<SolrParams> params = new ArrayList<SolrParams>();
  
  public static class TestProvider extends BatchProvider {

		@Override
    public void run(SolrQueryRequest locReq, BatchHandlerRequestQueue q)
        throws Exception {
			ModifiableSolrParams pars = new ModifiableSolrParams(locReq.getParams());
			
			String jobid = pars.get("jobid");
		  String workDir = pars.get("#workdir");
		  
		  File input = new File(workDir + "/" + jobid + ".input");
		  if (input.canRead()) {
		  	pars.set("#file", input.toString());
		  	pars.set("#data", new Scanner( input, "UTF-8" ).useDelimiter("\\A").next());
		  	
		  	File jobFile = new File(workDir + "/" + jobid);
				BufferedWriter out = new BufferedWriter(new FileWriter(jobFile), 1024*256);
				out.write("test-handler jobid:" + jobid);
				out.close();
		  }
		  
	    params.add(pars);
	    queue.add(q);
    }

		@Override
    public String getDescription() {
	    return "Test provider";
    }
  	
  }
  
  public static class TestProviderFail extends BatchProvider {

		@Override
    public void run(SolrQueryRequest locReq, BatchHandlerRequestQueue q)
        throws Exception {
			throw new SolrException(ErrorCode.BAD_REQUEST, "Woooot!");
    }

		@Override
    public String getDescription() {
	    return "Failing provider";
    }
  	
  }

  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestBatchRequestHandler.class);
  }
}
