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

package org.apache.solr.analysis;


import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.search.TermQuery;
import org.adsabs.solr.AdsConfig.F;

/**
 * Test for the date_string type
 * 
 */
public class TestAdsabsTypeDateString extends MontySolrQueryTestCase {


  @Override
  public String getSchemaFile() {
    makeResourcesVisible(this.solrConfig.getResourceLoader(),
        new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
    });
    return MontySolrSetup.getMontySolrHome()
    + "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";

  }

  public String getSolrConfigFile() {
    return MontySolrSetup.getMontySolrHome()
    + "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
  }


  public void test() throws Exception {

    assertU(addDocs(F.TYPE_DATE_STRING_FIELDS, "1999-00-00"));
    assertU(addDocs(F.TYPE_DATE_STRING_FIELDS, "2000-01-25"));
    assertU(addDocs(F.TYPE_DATE_STRING_FIELDS, "2000-01-26"));

    assertU(commit());

    assertQ(req("q", "*:*"), "//*[@numFound='3']");

    for (String f: F.TYPE_DATE_STRING_FIELDS) {
      
      // ascii normalization
      //assertQueryEquals(req("q", f + ":1999-00-00", "qt", "aqp"), "pubdate_string:1999-01-01T03:30:00Z", TermQuery.class);
      //assertQ(req("q", f + ":1999-00-00"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='0']");
      
    }
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeDateString.class);
  }
}
