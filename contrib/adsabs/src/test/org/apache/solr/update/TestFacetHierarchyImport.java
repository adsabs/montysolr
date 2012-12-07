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


import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.DataImporter;
import org.apache.solr.handler.dataimport.config.DIHConfiguration;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TextField;
import org.apache.solr.servlet.DirectSolrConnection;
import org.junit.BeforeClass;
import org.adsabs.solr.AdsConfig.F;

import java.lang.reflect.Field; 
import java.util.Map;

/**
 * Tests that the dataimport handler does really wait and does not
 * return immediately. Also, one of the fields is fetched by Python.
 * 
 */
public class TestFacetHierarchyImport extends MontySolrQueryTestCase {
	
	
	@BeforeClass
	public static void beforeTestAdsDataImport() throws Exception {
		// to use filesystem instead of ram
		//System.setProperty("solr.directoryFactory","solr.SimpleFSDirectoryFactory");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() 
				+ "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.tests.targets");
		
	}
	
	@Override
	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
	    	});
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
	}


	public void mockHandler(SolrRequestHandler handler) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
		
		DIHConfiguration config = null;
		
		Field fields[] = handler.getClass().getDeclaredFields();
		for (Field f: fields) {
			if (f.toString().contains("importer")) {
				f.setAccessible(true);
				DataImporter importer = (DataImporter) f.get(handler);
				
				Field fields2[] = DataImporter.class.getDeclaredFields();
				for (Field f2: fields2) {
					if (f2.toString().endsWith("config")) {
						f2.setAccessible(true);
						config = (DIHConfiguration) f2.get(importer);
					}
				
				}
			}
		}
		
		config.getDataSources().get(null).put("type", "InvenioDataSource");
		
		
		// HACK to prevent DIH from running mongoDB types
		Field schema = config.getClass().getDeclaredField("lowerNameVsSchemaField");
		schema.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		Map<String, SchemaField> lowerNameVsSchemaField = (Map<String, SchemaField>) schema.get(config);
		
		
		SchemaField full = lowerNameVsSchemaField.get("full");
//		System.out.println(full);
		
		FieldType type = null;
		Field fields3[] = full.getClass().getDeclaredFields();
		for (Field f: fields3) {
			if (f.toString().endsWith("type")) {
				f.setAccessible(true);
				type = (FieldType) f.get(full);
			}
		}
		System.out.println(type.toString());
		
		//schema = h.getCore().getSchema();
		TextField tf = new TextField();
		//tf.init(h.getCore().getSchema(), null);
		
		TextField x = (TextField) type;
		
		lowerNameVsSchemaField.remove("full");
		lowerNameVsSchemaField.remove("ack");
		lowerNameVsSchemaField.remove("reader");
		
	}
	
	public void testImport() throws Exception {
		
		
		String testDir = MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/";
		SolrRequestHandler handler = h.getCore().getRequestHandler("/invenio/import");
		SolrCore core = h.getCore();
		
		String url = "file://" + MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/ads-demo-records.xml";
		
		SolrQueryRequest req = req("command", "full-import",
				"dirs", testDir,
				"commit", "true",
				"url", url
				);
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		core.execute(handler, req, rsp);
		commit("waitFlush", "true", "waitSearcher", "true");
		
		req = req("command", "full-import",
	        "dirs", testDir,
	        "commit", "true",
	        "url", "file:///non-existing-file?p=recid:500->505 OR recid:508&foo=bar"
	        );
		
		rsp = new SolrQueryResponse();
	    core.execute(handler, req, rsp);
    
		assertQ(req("q", "first_author_facet_hier:\"0/Cutri, R\""), "//*[@numFound='1']");
		assertQ(req("q", "first_author_facet_hier:\"1/Cutri, R/Cutri, R. M.\""), "//*[@numFound='1']");
    
		assertQ(req("q", "author_facet_hier:\"0/Stumpe, M\""), "//*[@numFound='2']");
		assertQ(req("q", "author_facet_hier:\"1/Stumpe, M/Stumpe, M. C.\""), "//*[@numFound='2']");
		
		assertQ(req("q", "author_facet_hier:\"1//et al.\""), "//*[@numFound='0']");
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestFacetHierarchyImport.class);
    }
}
