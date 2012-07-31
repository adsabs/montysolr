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

package org.apache.solr.core;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.AdsConfigHandler;


public class TestExtendedConfig extends MontySolrAbstractTestCase {

	public String getSchemaFile() {
		
	    makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
	    	});
		
		return "schema.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" + 
		"extended-solrconfig.xml";
	}


  public void testExtendedConfig() {
	  AdsConfigHandler handler = (AdsConfigHandler) h.getCore().getRequestHandler("ads-config");
	  SolrParams defaults = handler.getParams("defaults");
	  assertTrue(defaults.get("spellcheck.onlyMorePopular").equals("false"));
	  SolrParams test = handler.getParams("testVal");
	  assertTrue(test.get("testVal").equals("testValue"));
	  SolrParams arr = handler.getParams("query");
	  assertTrue(arr.get("0").equals("all"));
	  assertTrue(arr.get("1").equals("false"));
  }
}