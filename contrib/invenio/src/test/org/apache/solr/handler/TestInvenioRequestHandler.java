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


import java.util.HashMap;
import java.util.Map;

import montysolr.util.MontySolrAbstractTestCase;
import montysolr.util.MontySolrSetup;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.ResultContext;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.DocSlice;

/**
 * Testing the (legacy) Invenio handler. Now, the AQP is the 
 * preferred way to handle queries.
 * 
 */
public class TestInvenioRequestHandler extends MontySolrAbstractTestCase {

	@Override
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/invenio/src/test-files/solr/collection1/conf/schema-minimal.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/invenio/src/test-files/solr/collection1/conf/solrconfig-invenio-handler.xml";
	}


	@SuppressWarnings({ "unchecked" })
	public void _testSorting() throws Exception {
		SolrCore core = h.getCore();

		assertU(adoc("id", "10", "title", "test", "val_s1", "aaa"));
		assertU(adoc("id", "11", "title", "test", "val_s1", "bbb"));
		assertU(adoc("id", "12", "title", "test", "val_s1", "ccc"));
		assertU(commit());

		assertQ("Make sure they got in",
				req("q", "title:test", "qt", "invenio"), "//*[@numFound='3']");

		assertQ("Make sure they got in",
				req("q", "title:test", "qt", "invenio.lazy"),
				"//*[@numFound='3']");

		Map<String, String> args = new HashMap<String, String>();
		Map<Object, Object> context = null;
		SolrQueryResponse rsp = null;

		args.put(CommonParams.Q, "title:test");
		args.put("indent", "true");
		SolrQueryRequest req = new LocalSolrQueryRequest(core,
				new MapSolrParams(args));

		rsp = h.queryAndResponse("invenio", req);
		assertTrue(((ResultContext) rsp.getValues().get("response")).docs.matches() == 3);
		context = req.getContext();
		assertTrue(context.containsKey("inv.params"));
		assertTrue(context.get("inv.params").toString().equals("{}"));

		rsp = h.queryAndResponse("invenio.lazy", req);
		assertTrue(((ResultContext) rsp.getValues().get("response")).docs.matches() == 3);
		context = req.getContext();
		assertTrue(context.containsKey("inv.params"));
		assertTrue(!context.get("inv.params").toString().equals("{}"));
		assertTrue(((HashMap<String, String>) context.get("inv.params")).get(
				"test3").equals("val3"));

		args.put("inv.params", "test1=val1&test2=val2");
		req = new LocalSolrQueryRequest(core, new MapSolrParams(args));
		rsp = h.queryAndResponse("invenio", req);
		assertTrue(((ResultContext) rsp.getValues().get("response")).docs.matches() == 3);
		context = req.getContext();
		assertTrue(((HashMap<String, String>) context.get("inv.params")).get(
				"test1").equals("val1"));
		assertTrue(((HashMap<String, String>) context.get("inv.params")).get(
				"test2").equals("val2"));

		args.put("inv.params", "test1=val1&test2=val2");
		req = new LocalSolrQueryRequest(core, new MapSolrParams(args));
		rsp = h.queryAndResponse("invenio.lazy", req);
		assertTrue(((ResultContext) rsp.getValues().get("response")).docs.matches() == 3);
		context = req.getContext();
		assertTrue(((HashMap<String, String>) context.get("inv.params")).get(
				"test1").equals("val1"));
		assertTrue(((HashMap<String, String>) context.get("inv.params")).get(
				"test2").equals("val2"));
		assertTrue(!((HashMap<String, String>) context.get("inv.params"))
				.containsKey("test3"));
		
		assertU(commit());
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestInvenioRequestHandler.class);
    }
}
