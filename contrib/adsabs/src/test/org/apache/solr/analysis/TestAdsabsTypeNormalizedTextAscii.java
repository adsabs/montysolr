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
 * Test for the normalized_text_ascii type
 * 
 */
public class TestAdsabsTypeNormalizedTextAscii extends MontySolrQueryTestCase {

	
	
  @BeforeClass
	public static void beforeClass() throws Exception {
		
		makeResourcesVisible(Thread.currentThread().getContextClassLoader(),
		        new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1/conf",
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

    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "Bílá kobyla skočila přes čtyřista"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "třicet-tři stříbrných střech"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "A ještě TřistaTřicetTři stříbrných stovek"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "Cutri, R"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "Cutri,R"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "Cutri,.R"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "one-jets")); //6.
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "jets-two"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "three-jets-four"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "five jets"));
    
    assertU(commit("waitSearcher", "true"));

    assertQ(req("q", "*:*"), "//*[@numFound='10']");
    
    //dumpDoc(null, F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS);
    
    for (String f: F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS) {
      
      // ascii normalization
      assertQueryEquals(req("q", f + ":Bílá", "qt", "aqp"), f+":bila", TermQuery.class);
      assertQueryEquals(req("q", f + ":Bila", "qt", "aqp"), f+":bila", TermQuery.class);
      assertQueryEquals(req("q", f + ":bila", "qt", "aqp"), f+":bila", TermQuery.class);
      assertQ(req("q", f + ":Bílá"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='0']");
      assertQ(req("q", f + ":Bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='0']");
      assertQ(req("q", f + ":bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='0']");
      
      // whitespace analyzer & phrases
      assertQueryEquals(req("q", f + ":třicet-tři", "qt", "aqp"), f+":tricettri", TermQuery.class);
      assertQ(req("q", f + ":třicet-tři"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
      
      assertQueryEquals(req("q", f + ":(třicet tři)", "qt", "aqp"), String.format("+%s:tricet +%s:tri", f, f), BooleanQuery.class);
      assertQ(req("q", f + ":(třicet tři)"), "//*[@numFound='0']");
      
      assertQueryEquals(req("q", f + ":\"třicet tři\"", "qt", "aqp"), String.format("%s:\"tricet tri\"", f), PhraseQuery.class);
      assertQ(req("q", f + ":\"třicet tři\""), "//*[@numFound='0']");
      
      assertQueryEquals(req("q", f + ":\"třicet tři\"", "qt", "aqp"), String.format("%s:\"tricet tri\"", f), PhraseQuery.class);
      assertQ(req("q", f + ":\"třicet tři\""), "//*[@numFound='0']");
      
      assertQueryEquals(req("q", f + ":\"stříbrných střech\"", "qt", "aqp"), String.format("%s:\"stribrnych strech\"", f), PhraseQuery.class);
      assertQ(req("q", f + ":\"stříbrných střech\""), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
      
      // no WDF
      assertQ(req("q", f + ":TřistaTřicetTři"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='2']");
      assertQ(req("q", f + ":třistatřicettři"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='2']");
      assertQ(req("q", f + ":TristaTricetTri"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='2']");
      
      assertQ(req("q", f + ":\"cutri,\""), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='3']");
      
      // TODO: I could easily activate this behaviour if we allow ANY field inside AqpDEFOPMarkPlainNodes
      //assertQ(req("q", f + ":cutri,r"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='4']");
      //assertQ(req("q", f + ":cutri,.r"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='5']");
      
      assertQ(req("q", f + ":\"five jets\""), "//*[@numFound='1']");
      assertQ(req("q", f + ":\"fivejets\""), "//*[@numFound='0']");
      assertQ(req("q", f + ":\"onejets\""), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='6']");
      assertQ(req("q", f + ":\"one-jets\""), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='6']");
      assertQ(req("q", f + ":\"*jets\""), "//*[@numFound='2']", "//doc[1]/str[@name='id'][.='6']"); // curiously also finds 'jets' (id 5.)
      assertQ(req("q", f + ":\"jets\""), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='9']");
      assertQ(req("q", f + ":\"jets*\""), "//*[@numFound='2']", "//doc[1]/str[@name='id'][.='7']"); // also 'jets' (id 5.)
      assertQ(req("q", f + ":\"*jets*\""), "//*[@numFound='4']");
      
      // find only 'jets' (where the word stood alone)
      assertQ(req("q", f + ":\"jets\""), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='9']");
    }
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeNormalizedTextAscii.class);
  }
}
