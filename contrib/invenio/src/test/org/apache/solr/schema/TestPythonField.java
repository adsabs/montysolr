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

import invenio.montysolr.util.MontySolrAbstractTestCase;


/**
 * Most of the tests for StandardRequestHandler are in ConvertedLegacyTest
 * 
 */
public class TestPythonField extends MontySolrAbstractTestCase {

  @Override public String getSchemaFile() { 
	  return getMontySolrHome() + 
	  "/contrib/invenio/src/test-files/solr/conf/schema-python-field.xml";
	  }
  @Override public String getSolrConfigFile() { 
	  
	  return "solrconfig.xml";
	  
  }
  @Override public void setUp() throws Exception {
    super.setUp();
    addToSysPath(getMontySolrHome() + "/contrib/invenio/src/python");
    addTargetsToHandler("monty_invenio.schema.targets");
  }
  
  @Override
  public String getModuleName() {
	  return "montysolr.java_bridge.SimpleBridge";
  }
  
public void testSorting() throws Exception {
    
	assertU(adoc("id", "9",  "title", "test", "text", getMontySolrHome() + "/contrib/invenio/src/test-files/data/text1.txt"));
    assertU(adoc("id", "10", "title", "test", "text", getMontySolrHome() + "/contrib/invenio/src/test-files/data/text1.txt"));
    assertU(adoc("id", "11", "title", "test", "text", getMontySolrHome() + "/contrib/invenio/src/test-files/data/text2.txt"));
    assertU(adoc("id", "12", "title", "nonexisting", "text", getMontySolrHome() + "/contrib/invenio/src/test-files/text-nonexisting.txt"));
    assertU(commit());
    
    
    
    assertQ("Make sure they got in", req("q", "title:test")
            ,"//*[@numFound='3']"
            );
    
    assertQ("Make sure they got in", req("q", "title:nonexisting")
            ,"//*[@numFound='1']"
            );
    
    
    assertQ("Make sure they got in", req("q", "text:PythonTextField")
            ,"//*[@numFound='2']"
            );
    
    assertQ("Make sure they got in", req("q", "text:blind")
            ,"//*[@numFound='1']"
            );
    
    assertQ("Make sure they got in", req("q", "text:HI")
            ,"//*[@numFound='3']"
            );
    
    
  }
}
