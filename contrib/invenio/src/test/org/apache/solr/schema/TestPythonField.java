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

package org.apache.solr.schema;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.junit.BeforeClass;


/**
 * Tests if the document is made of values fetched by Python
 * 
 */
public class TestPythonField extends MontySolrAbstractTestCase {

	@BeforeClass
	public static void beforeTestPythonField() throws Exception {
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome()
				+ "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.tests.targets");
	}

	@Override
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/invenio/src/test-files/solr/collection1/conf/"
				+ "schema-python-field.xml";
	}

	@Override
	public String getSolrConfigFile() {

		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/invenio/src/test-files/solr/collection1/conf/"
				+ "solrconfig-basic.xml";

	}

	public void testSorting() throws Exception {

		String tp = MontySolrSetup.getMontySolrHome();

		assertU(adoc("id", "9", "title", "test", "text", tp
				+ "/contrib/invenio/src/test-files/data/text1.txt"));
		assertU(adoc("id", "10", "title", "test", "text", tp
				+ "/contrib/invenio/src/test-files/data/text1.txt"));
		assertU(adoc("id", "11", "title", "test", "text", tp
				+ "/contrib/invenio/src/test-files/data/text2.txt"));
		assertU(adoc("id", "12", "title", "nonexisting", "text", tp
				+ "/contrib/invenio/src/test-files/text-nonexisting.txt"));
		assertU(commit());

		assertQ("Make sure they got in", req("q", "title:test"),
				"//*[@numFound='3']");

		assertQ("Make sure they got in", req("q", "title:nonexisting"),
				"//*[@numFound='1']");

		assertQ("Make sure they got in", req("q", "text:PythonTextField"),
				"//*[@numFound='2']");

		assertQ("Make sure they got in", req("q", "text:blind"),
				"//*[@numFound='1']");

		assertQ("Make sure they got in", req("q", "text:HI"),
				"//*[@numFound='3']");

	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestPythonField.class);
    }
}
