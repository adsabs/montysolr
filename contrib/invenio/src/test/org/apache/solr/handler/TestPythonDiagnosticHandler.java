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

import org.junit.BeforeClass;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonMessage;
import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

/**
 * Testing the (python) diagnostic handler.
 * 
 */
public class TestPythonDiagnosticHandler extends MontySolrAbstractTestCase {

	@BeforeClass
	public static void beforeClass() throws Exception {
		MontySolrSetup.init("montysolr.tests.basic.bridge.Bridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
	}
	
  @Override
  public String getSchemaFile() {
    return MontySolrSetup.getMontySolrHome()
        + "/contrib/invenio/src/test-files/solr/collection1/conf/schema-minimal.xml";
  }

  @Override
  public String getSolrConfigFile() {
    return MontySolrSetup.getMontySolrHome()
        + "/contrib/invenio/src/test-files/solr/collection1/conf/solrconfig-diagnostic-test.xml";
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    lrf = h.getRequestFactory("standard", 0, 20);
  }

  @SuppressWarnings({ "unchecked" })
  public void test() throws Exception {
    
  	PythonMessage message = null;
		
		message = MontySolrVM.INSTANCE.createMessage(
				"diagnostic_test").setParam("query", "none");

		MontySolrVM.INSTANCE.sendMessage(message);

		Object result = message.getResults();
		assertNotNull(result);
		String res = (String) result;
		assertTrue("Diagnostic test returned unexpected results, or diagnostic_test was overwritten!", 
				res.contains("PYTHONPATH") && res.contains("sys.path")
				&& res.contains("PYTHONPATH"));
		
		assertQ("nope",
				req("qt", "/diagnostic_test", "q", "nope"),
				"contains(//str[@name='diagnostic_message']/text(), 'sys.path')",
				"contains(//str[@name='diagnostic_message']/text(), 'current targets:')");
		
		//send several messages (the same)
		MontySolrVM.INSTANCE.sendMessage(message);
		MontySolrVM.INSTANCE.sendMessage(message);
		MontySolrVM.INSTANCE.sendMessage(message);
		

  }

}
