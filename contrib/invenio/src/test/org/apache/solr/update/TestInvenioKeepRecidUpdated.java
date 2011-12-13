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

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.util.MontySolrAbstractTestCase;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.DocSlice;

/**
 * Most of the tests for StandardRequestHandler are in ConvertedLegacyTest
 * 
 */
public class TestInvenioKeepRecidUpdated extends MontySolrAbstractTestCase {
	
	//TODO: dynamically retrieve these values
	private String importurl = "http://localhost:8983/solr/waiting-dataimport";
	private String updateurl = "http://localhost:8983/solr/waiting-dataimport";
	private String deleteurl = "http://localhost:8983/solr/waiting-dataimport";
	private String inveniourl = "http://insdev01.cern.ch/search";
	
	
	@Override
	public String getSchemaFile() {
		return getMontySolrHome()
		+ "/contrib/invenio/src/test-files/solr/conf/schema-invenio-keeprecid-updater.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return getMontySolrHome()
				+ "/contrib/invenio/src/test-files/solr/conf/solrconfig-invenio-keeprecid-updater.xml";
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		addToSysPath(getMontySolrHome() + "/contrib/invenio/src/python");
		
		addTargetsToHandler("monty_invenio.targets");
		addTargetsToHandler("monty_invenio.tests.unittest_updating");
		
		PythonMessage message = MontySolrVM.INSTANCE.createMessage("reset_records");
		MontySolrVM.INSTANCE.sendMessage(message);
		
	}

	
	@Override
	public String getModuleName() {
		return "montysolr.java_bridge.SimpleBridge";
	}

	
	public void testIndexing() throws Exception {
		SolrCore core = h.getCore();

		PythonMessage message = MontySolrVM.INSTANCE.createMessage("create_record");
		MontySolrVM.INSTANCE.sendMessage(message);
		Integer firstAdded = (Integer) message.getResults();
		
		message = MontySolrVM.INSTANCE.createMessage("create_record");
		MontySolrVM.INSTANCE.sendMessage(message);
		Integer secondAdded = (Integer) message.getResults();
		
		message = MontySolrVM.INSTANCE.createMessage("change_records")
						.setParam("recids", new int[]{1,2,3,4,5,6,7,8,9});
		MontySolrVM.INSTANCE.sendMessage(message);
		
		message = MontySolrVM.INSTANCE.createMessage("delete")
			.setParam("recid", firstAdded);
		MontySolrVM.INSTANCE.sendMessage(message);

		InvenioKeepRecidUpdated handler = new InvenioKeepRecidUpdated();
		
		
		assertQ("Make sure they got in",
				req("qt", "invenio_update", "last_recid", "-1",
						"inveniourl", inveniourl,
						"importurl", importurl,
						"updateurl", updateurl,
						"deleteurl", deleteurl), 
				"//*[@importStatus='busy']");
		


	}
}
