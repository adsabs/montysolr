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


import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;

/**
 * Tests that the dataimport handler does really wait and does not
 * return immediately. Also, one of the fields is fetched by Python.
 * 
 */
@Ignore
public class BenchmarkAdsDataImport extends MontySolrQueryTestCase {
	

	@BeforeClass
	public static void beforeTestAdsDataImport() throws Exception {
		// to use filesystem instead of ram
		//System.setProperty("solr.directoryFactory","solr.SimpleFSDirectoryFactory");
		
	}
	
	@Override
	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
	    	});
		
		String configFile = MontySolrSetup.getMontySolrHome()
      + "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
		
		return configFile;
	}

	@Override
	public String getSolrConfigFile() {
		
    String configFile = MontySolrSetup.getMontySolrHome()
    + "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
    String dataConfig = MontySolrSetup.getMontySolrHome()
    + "/contrib/examples/adsabs/solr/collection1/conf/data-config.xml";
  
    File newConfig;
    File newDataConfig;
    try {
      
      newConfig = duplicateFile(new File(configFile));
      newDataConfig = duplicateFile(new File(dataConfig));
  
      replaceInFile(newDataConfig, "mongoHost=\"adszee\"", "mongoHost=\"localhost\"");
      replaceInFile(newConfig, "data-config.xml", newDataConfig.getAbsolutePath());
  
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException(e.getMessage());
    }
  
    return newConfig.getAbsolutePath();
	}


	
	public void testImport() throws Exception {
		
		
		String testDir = MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/";
		
		SolrRequestHandler handler = h.getCore().getRequestHandler("/invenio/import");
		
		//mockHandler(handler);
		
		SolrCore core = h.getCore();
		
		String url = "file://" + MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/ads-demo-records.xml";
		
		SolrQueryRequest req = req("command", "full-import",
				"dirs", testDir,
				"commit", "true",
				"url", url
				);
		
		int i = 0;
		while (i < 10000) {
		SolrQueryResponse rsp = new SolrQueryResponse();
		  core.execute(handler, req, rsp);
		  i++;
		  commit("waitFlush", "true", "waitSearcher", "true");
		}
		
		
		//dumpDoc(null, "bibcode", "title", "abstract", "all", "ack", "full");
		
    
    assertQ(req("q", "bibcode:2012ApJ...760..135R"), "//doc/arr[@name='links_data']/str[contains(text(),'MAST')]");
    assertQ(req("q", "bibcode:2012ApJ...760..135R"), "//doc/arr[@name='ids_data']/str[contains(text(),'\"alternate_bibcode\":\"2012arXiv1210.5163R\"')]");
    
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BenchmarkAdsDataImport.class);
    }
}
