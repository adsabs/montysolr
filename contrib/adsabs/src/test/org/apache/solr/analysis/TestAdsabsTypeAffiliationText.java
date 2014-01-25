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
import org.adsabs.solr.AdsConfig.F;
import org.junit.BeforeClass;

/**
 * Test for the affiliation_text type
 * 
 */
public class TestAdsabsTypeAffiliationText extends MontySolrQueryTestCase {

	@BeforeClass
	public static void beforeTestAdsabsTypeNormalizedStringAscii() throws Exception {
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() 
				+ "/contrib/adsabs/src/python");
		MontySolrSetup.addTargetsToHandler("adsabs.targets");
	}
	
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

    assertU(addDocs("aff", "W.K. Kellogg Radiation Laboratory, California Institute of Technology, Pasadena, CA 91125, USA"));
    assertU(addDocs("aff", "W.K. Kellogg <xfoo> Radiation Laboratory, California Institute of Technology, Pasadena, CA(91125), USA"));

    assertU(commit());
    
    //dumpDoc(null, F.ID, "aff");
    
    assertQ(req("q", "*:*"), "//*[@numFound='2']");
    assertQ(req("q", "aff:xfoo"), "//*[@numFound='0']");

    assertQueryEquals(req("q", "aff:\"Pasadena, CA 91125\"", "qt", "aqp"), 
    		"aff:\"pasadena acr::ca 91125\"", 
    		PhraseQuery.class
    		);
    assertQueryEquals(req("q", "aff:\"Pasadena, CA(91125)\"", "qt", "aqp"), 
    		"aff:\"pasadena acr::ca 91125\"", 
    		PhraseQuery.class
    		);
    
    assertQ(req("q", "aff:\"Pasadena, CA 91125\""), 
    		"//*[@numFound='2']", 
    		"//doc/str[@name='id'][.='0']",
    		"//doc/str[@name='id'][.='1']"
    		);
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAffiliationText.class);
  }
}
