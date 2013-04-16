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

    // also look at TestADSDataImport, there we test it too
    
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2012-10-01T00:00:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2012-10-01T00:30:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2012-10-01T00:31:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2012-11-01T00:00:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2012-12-01T00:00:00Z"));
    
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2013-10-01T00:00:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2013-10-01T00:30:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2013-10-01T00:31:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2013-11-01T00:00:00Z"));
    assertU(addDocs(F.TYPE_DATE_FIELDS, "2013-12-01T00:00:00Z"));

    assertU(commit());

    assertQ(req("q", "*:*"), "//*[@numFound='10']");
    
    assertQueryEquals(req("q", "pubdate:2012", "qt", "aqp"), "date:[1325376000000 TO 1356998400000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012"), "//*[@numFound='5']", 
        "//doc/str[@name='id'][.='0']",
        "//doc/str[@name='id'][.='1']",
        "//doc/str[@name='id'][.='2']",
        "//doc/str[@name='id'][.='3']",
        "//doc/str[@name='id'][.='4']"
        );
    
    // notice, if doc contains values "2012-10-00" "2012-10-01" without
    // specifying the seconds, it will be indexed into 00:00 of a day
    // but when you search for "2012-10-01" you will search everything
    // *after* the first 30mins of a day! If you want to get also the
    // 'zero' hour docs, you must search for '2012-01' or specify the 
    // hour precisely
    assertQueryEquals(req("q", "pubdate:2012-10-01", "qt", "aqp"), "date:[1349051400000 TO 1349137800000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012-10-01"), 
        "//*[@numFound='2']", 
        "//doc/str[@name='id'][.='1']",
        "//doc/str[@name='id'][.='2']"
        );
    
    assertQueryEquals(req("q", "pubdate:2012-11", "qt", "aqp"), "date:[1351728000000 TO 1354320000000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012-11"), 
        "//*[@numFound='1']", 
        "//doc/str[@name='id'][.='3']");
    
    // notice, the pubdate search fails, but when we use date it works
    assertQueryEquals(req("q", "pubdate:2012-12-02", "qt", "aqp"), 
    		"date:[1354408200000 TO 1354494600000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012-12-02"), "//*[@numFound='0']");
    
    assertQ(req("q", "date:2012-12-01T00\\:00\\:00Z"), 
    		"//*[@numFound='1']",
    		"//doc/str[@name='id'][.='4']"
    		);
    
    
    // notice: the range is not inclusive at the end [....} like above
    assertQueryEquals(req("q", "pubdate:[2012-10-00 TO 2012-12-02]", "qt", "aqp"), 
    		"date:[1349049600000 TO 1354408200000]", NumericRangeQuery.class);
    

    // search for any article from the 10th month
    assertQ(req("q", "pubdate:[2012-10-00 TO 2012-12-02]"), "//*[@numFound='5']", 
    		"//doc/str[@name='id'][.='0']",
        "//doc/str[@name='id'][.='1']",
        "//doc/str[@name='id'][.='2']",
        "//doc/str[@name='id'][.='3']",
        "//doc/str[@name='id'][.='4']"
        );
    
    // here we skip the the articles that were indexed with 2012-10-00 pubdate
    assertQ(req("q", "pubdate:[2012-10-01 TO 2012-12-02]"), "//*[@numFound='4']", 
        "//doc/str[@name='id'][.='1']",
        "//doc/str[@name='id'][.='2']",
        "//doc/str[@name='id'][.='3']",
        "//doc/str[@name='id'][.='4']"
        );
    
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeDateString.class);
  }
}
