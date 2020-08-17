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

import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.PhraseQuery;
import org.junit.BeforeClass;

/**
 * Test for the affiliation_text type
 * 
 */
public class TestAdsabsTypeAffiliationText extends MontySolrQueryTestCase {

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

    assertU(addDocs("aff_raw", "W.K. Kellogg Radiation Laboratory, California Institute of Technology, Pasadena, CA 91125, USA"));
    assertU(addDocs("aff_raw", "W.K. Kellogg <xfoo> Radiation Laboratory, California Institute of Technology, Pasadena, CA(91125), USA"));
    assertU(addDocs("aff_raw", "IMCCE/Observatoire de Paris"));
    assertU(addDocs("aff_raw", "INAF - Osservatorio Astronomico di Brera, via E. Bianchi 46, I-23807 Merate, Italy;",
    		            "aff_raw", "INAF - IASF Milano, via E. Bassini 15, I-20133 Milano, Italy"));
    assertU(addDocs("aff_raw", "Instituto de Astrofísica de Andalucía (IAA-CSIC) foo:doo"));
    assertU(addDocs("aff_raw", "foo1", "aff_raw", "foo2", "aff_raw", "-", "aff_raw", "foo4"));

    assertU(commit());
    
    //dumpDoc(null, "aff_raw");
    //System.err.println(h.query(req("q", "aff_raw:foo1")));
    
    assertQ(req("q", "*:*"), "//*[@numFound>='2']");
    assertQ(req("q", "aff_raw:xfoo"), "//*[@numFound='0']");

    assertQueryEquals(req("q", "aff_raw:\"Pasadena, CA 91125\"", "qt", "aqp"), 
    		"aff_raw:\"pasadena acr::ca 91125\"",
    		PhraseQuery.class
    		);
    assertQueryEquals(req("q", "aff_raw:\"Pasadena, CA(91125)\"", "qt", "aqp"), 
    		"aff_raw:\"pasadena acr::ca 91125\"",
    		PhraseQuery.class
    		);
    
    assertQ(req("q", "aff_raw:\"Pasadena, CA 91125\""), 
    		"//*[@numFound='2']", 
    		"//doc/str[@name='id'][.='0']",
    		"//doc/str[@name='id'][.='1']"
    		);
    
    assertQ(req("q", "aff_raw:IMCCE"), 
    		"//*[@numFound='1']", 
    		"//doc/str[@name='id'][.='2']"
    		);
    
    assertQ(req("q", "aff_raw:imcce"), 
    		"//*[@numFound='1']", 
    		"//doc/str[@name='id'][.='2']"
    		);
    
    assertQ(req("q", "aff_raw:IASF"), 
    		"//*[@numFound='1']", 
    		"//doc/str[@name='id'][.='3']"
    		);
    
    assertQ(req("q", "aff_raw:iasf"), 
    		"//*[@numFound='1']", 
    		"//doc/str[@name='id'][.='3']"
    		);
    
    assertQ(req("q", "aff_raw:IAA-CSIC"), 
    		"//*[@numFound='1']", 
    		"//doc/str[@name='id'][.='4']"
    		);
    
    assertQ(req("q", "aff_raw:IAACSIC"), 
    		"//*[@numFound='1']", 
    		"//doc/str[@name='id'][.='4']"
    		);
    assertQ(req("q", "aff_raw:iaa-csic"), 
    		"//*[@numFound='1']", 
    		"//doc/str[@name='id'][.='4']"
    		);
    
    assertQ(req("q", "aff_raw:\"INAF - IASF\""), 
    		"//*[@numFound='1']", 
    		"//doc/str[@name='id'][.='3']"
    		);
    assertQ(req("q", "aff_raw:\"inaf - iasf\""), 
    		"//*[@numFound='1']", 
    		"//doc/str[@name='id'][.='3']"
    		);

    
    assert h.query(req("q", "aff_raw:foo1", "fl", "aff_raw"))
 		.contains("<arr name=\"aff_raw\">" +
				"<str>foo1</str>" +
				"<str>foo2</str>" +
                "<str>-</str>" +
                "<str>foo4</str>"
        );
    
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAffiliationText.class);
  }
}
