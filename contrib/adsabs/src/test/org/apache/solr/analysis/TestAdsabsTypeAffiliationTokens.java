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

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TermQuery;
import org.junit.BeforeClass;


/**
 * Test for the affiliation_text type
 * 
 */
public class TestAdsabsTypeAffiliationTokens extends MontySolrQueryTestCase {

  @BeforeClass
  public static void beforeClass() throws Exception {
  	
  	makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
  		    MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1/conf",
		      MontySolrSetup.getSolrHome() + "/example/solr/collection1"
		    });
  	
    System.setProperty("solr.allow.unsafe.resourceloading", "true");
    
    
    schemaString = MontySolrSetup.getMontySolrHome()
        + "/contrib/examples/adsabs/server/solr/collection1/conf/schema.xml";
      
    configString = MontySolrSetup.getMontySolrHome()
        + "/contrib/examples/adsabs/server/solr/collection1/conf/solrconfig.xml";
    
    initCore(configString, schemaString, MontySolrSetup.getSolrHome()
			    + "/example/solr");
  }
  

  public void test() throws Exception {

    assertU(addDocs("aff_abbrev", "foo bar", "aff_abbrev", "bar baz/hey"));
    assertU(addDocs("aff_abbrev", "Kavli Institute/Dept of Physics"));
    assertU(commit());
    
    // make sure docs are there
    assertQ(req("q", "*:*"), "//*[@numFound>='2']");
    
    // query parsing tests
    assertQueryEquals(req("q", "aff_abbrev:\"Foo Bar\""), 
        "aff_abbrev:foo bar",
        TermQuery.class
        );
    // it is not visible here, but tokens are: foo bar, baz
    assertQueryEquals(req("q", "aff_abbrev:\"Foo Bar/Baz\""), 
        "aff_abbrev:\"foo bar baz\"",
        PhraseQuery.class
        );
    
    // test matches
    assertQ(req("q", "aff_abbrev:\"foo bar\""), 
        "//*[@numFound='1']",
        "//doc/str[@name='id'][.='0']"
        );
    assertQ(req("q", "aff_abbrev:\"bar baz\""), 
        "//*[@numFound='1']",
        "//doc/str[@name='id'][.='0']"
        );
    assertQ(req("q", "aff_abbrev:HEY"), 
        "//*[@numFound='1']",
        "//doc/str[@name='id'][.='0']"
        );
    assertQ(req("q", "aff_abbrev:\"bar BAZ/heY\""), 
        "//*[@numFound='1']",
        "//doc/str[@name='id'][.='0']"
        );
    
    // only match full tokens
    assertQ(req("q", "aff_abbrev:foo"), "//*[@numFound='0']");
    assertQ(req("q", "aff_abbrev:\"baz/hey\""), "//*[@numFound='0']");
    

    // check the affiliation is there stored as one string
    assert h.query(req("q", "aff_abbrev:\"Kavli Institute/Dept of Physics\""))
 		.contains("<str>Kavli Institute/Dept of Physics</str>"
        );
    
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAffiliationTokens.class);
  }
}
