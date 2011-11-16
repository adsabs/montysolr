package org.apache.solr.search.function;
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

import org.apache.solr.util.AbstractSolrTestCase;


/**
 *
 *
 **/
public class FieldPositionTest extends AbstractSolrTestCase {
  @Override
  public String getSchemaFile() {
    return "schema-fieldpos.xml";
  }
  
  @Override
  public String getSolrConfigFile() {
	  return "solrconfig-authorposquery.xml";
  }


  public void test() throws Exception {
    assertU(adoc("id", "1", "test_pos", "ellis luker brooks simko mele pele", "test_ws", "roman one two three"));
    assertU(adoc("id", "2", "test_pos", "this is not author list", "test_ws", "roman one two three"));
    assertU(adoc("id", "3", "test_pos", "ellis luker brooks simko mele pele winehouse"));
    assertU(adoc("id", "4", "test_pos", "hey bumping of author name positions is necessary!"));
    assertU(commit());
    
    
    assertQ(req("q", "test_ws:roman"),
    		"//*[@numFound='2']");
    
    assertQ(req("q", "test_pos:roman[0,1]"),
			"//*[@numFound='2']");

  }



}
