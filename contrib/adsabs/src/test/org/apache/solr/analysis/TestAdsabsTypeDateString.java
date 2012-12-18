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

import org.apache.lucene.search.NumericRangeQuery;
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

    // The real job of the indexing is done by the DIH transformer, so this test
    // is sort of useless without it (but look at TestADSDataImport, there we test
    // it)
    
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2012-10-01T00:00:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2012-11-01T00:00:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2012-12-01T00:00:00Z"));

    assertU(commit());

    assertQ(req("q", "*:*"), "//*[@numFound='3']");
    assertQueryEquals(req("q", "pubdate:2012", "qt", "aqp"), "date:[1325376000000 TO 1356998400000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012"), "//*[@numFound='3']", 
        "//doc/str[@name='id'][.='0']",
        "//doc/str[@name='id'][.='1']",
        "//doc/str[@name='id'][.='2']");
    
    assertQueryEquals(req("q", "pubdate:2012-11", "qt", "aqp"), "date:[1351728000000 TO 1354320000000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012-11"), 
        "//*[@numFound='1']", 
        "//doc/str[@name='id'][.='1']");
    
    assertQueryEquals(req("q", "pubdate:2012-12-02", "qt", "aqp"), "date:[1354408200000 TO 1354494600000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012-12-02"), "//*[@numFound='0']");
    
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeDateString.class);
  }
}
