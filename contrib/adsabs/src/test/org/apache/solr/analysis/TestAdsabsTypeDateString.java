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
import org.junit.BeforeClass;

/**
 * Test for the date_string type
 * 
 */
public class TestAdsabsTypeDateString extends MontySolrQueryTestCase {

	@BeforeClass
	public static void beforeClass() throws Exception {

		makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
			MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
			MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
		});

		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome()
			    + "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";

		configString = MontySolrSetup.getMontySolrHome()
			    + "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";

		initCore(configString, schemaString, MontySolrSetup.getSolrHome()
				+ "/example/solr");
	}
	


  public void test() throws Exception {

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
    
    assertU(addDocs(F.TYPE_DATE_FIELDS, "1976-01-01T00:30:00Z"));
		assertU(addDocs(F.TYPE_DATE_FIELDS, "1976-01-02T00:30:00Z"));
		assertU(addDocs(F.TYPE_DATE_FIELDS, "1976-02-01T00:30:00Z"));
		assertU(addDocs(F.TYPE_DATE_FIELDS, "1976-01-02T00:30:00Z"));
		assertU(addDocs(F.TYPE_DATE_FIELDS, "1976-12-30T00:30:00Z")); // year 76 had only 30 days in Dec
		assertU(addDocs(F.TYPE_DATE_FIELDS, "1977-01-01T00:30:00Z"));

    assertU(commit());

    assertQ(req("q", "*:*"), "//*[@numFound='16']");
    
    
    // test the query parser does the right thing
    //setDebug(true);
    
    // 2012-01-01T00:00:00 - 2012-02-01T00:00:00 (excl)
    assertQueryEquals(req("q", "pubdate:2012-01", "defType", "aqp"), 
    		"date:[1325376000000 TO 1328054400000}", 
    		NumericRangeQuery.class);
    assertQueryEquals(req("q", "pubdate:2012-00", "defType", "aqp"), 
    		"date:[1325376000000 TO 1328054400000}", 
    		NumericRangeQuery.class);
    
    // 2012-01-01T00:00:00 - 2013-01-01T00:00:00 (excl)
    assertQueryEquals(req("q", "pubdate:2012", "defType", "aqp"), 
    		"date:[1325376000000 TO 1356998400000}", 
    		NumericRangeQuery.class);
    // 2012-01-01T00:00:00 - 2012-01-01T23:59:59
    assertQueryEquals(req("q", "pubdate:[2012]", "defType", "aqp"), 
    		"date:[1325376000000 TO 1325462399000]", 
    		NumericRangeQuery.class);
    // 1012-01-01T00:00:01 - 2012-12-31T23:59:59
    assertQueryEquals(req("q", "pubdate:[* TO 2012]", "defType", "aqp"), 
    		"date:[-30231100799000 TO 1356998399000]", 
    		NumericRangeQuery.class);
    
    // 2012-01-01T00:00:00 - 3011-12-31T23:59:59
    assertQueryEquals(req("q", "pubdate:[2012 TO *]", "defType", "aqp"), 
    		"date:[1325376000000 TO 32882284799000]", 
    		NumericRangeQuery.class);
    
    // 2012-01-01T00:00:00 - 2013-12-31T23:59:59
    assertQueryEquals(req("q", "pubdate:[2012 TO 2013]", "defType", "aqp"), 
    		"date:[1325376000000 TO 1388534399000]", 
    		NumericRangeQuery.class);
    
    // 2012-01-01T00:00:00 - 2013-01-31T23:59:59
    assertQueryEquals(req("q", "pubdate:[2012-01 TO 2013-01]", "defType", "aqp"), 
    		"date:[1325376000000 TO 1359676799000]", 
    		NumericRangeQuery.class);    
    
    // 2012-01-01T00:30:00 - 2013-01-01T23:59:59
    assertQueryEquals(req("q", "pubdate:[2012-01-01 TO 2013-01-1]", "defType", "aqp"), 
    		"date:[1325377800000 TO 1357084799000]", 
    		NumericRangeQuery.class);    
    
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
    assertQueryEquals(req("q", "pubdate:2012-10-01", "defType", "aqp"), 
    		"date:[1349051400000 TO 1349136000000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012-10-01"), 
        "//*[@numFound='2']", 
        "//doc/str[@name='id'][.='1']",
        "//doc/str[@name='id'][.='2']"
        );
    
    assertQueryEquals(req("q", "pubdate:2012-11", "defType", "aqp"), "date:[1351728000000 TO 1354320000000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012-11"), 
        "//*[@numFound='1']", 
        "//doc/str[@name='id'][.='3']");
    
    // notice, the pubdate search fails, but when we use date it works
    assertQueryEquals(req("q", "pubdate:2012-12-02", "defType", "aqp"), 
    		"date:[1354408200000 TO 1354492800000}", NumericRangeQuery.class);
    assertQ(req("q", "pubdate:2012-12-02"), "//*[@numFound='0']");
    
    assertQ(req("q", "date:2012-12-01T00\\:00\\:00Z"), 
    		"//*[@numFound='1']",
    		"//doc/str[@name='id'][.='4']"
    		);
    
    
    assertQueryEquals(req("q", "pubdate:[2012-10-00 TO 2012-12-02]", "defType", "aqp"), 
    		"date:[1349049600000 TO 1354492799000]", NumericRangeQuery.class);
    

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
    
    
    assertQ(req("q", "pubdate:1976-00"),  // 00 gets automatically translated into 1976-01-01 (includes 1976-01-00)
				"//*[@numFound='3']"
		);
		assertQ(req("q", "pubdate:1976-00-00"), // gets automatically translated into 01-01
				"//*[@numFound='1']",
				"//doc/str[@name='id'][.='10']"
		);
		assertQ(req("q", "pubdate:1976-00-32"), // nonsense, but should be parsed properly into 01-01 
				"//*[@numFound='0']"
				); 
		assertQ(req("q", "pubdate:1976-01-00"), // i refuse to parse '00' as 'search for whole month'
				"//*[@numFound='1']"
				);
		assertQ(req("q", "pubdate:1976-01-01"), 
			"//*[@numFound='1']",
			"//doc/str[@name='id'][.='10']"
			);
		assertQ(req("q", "pubdate:1976-01-04"), 
			"//*[@numFound='0']"
			);
		
    
  }
  
}
