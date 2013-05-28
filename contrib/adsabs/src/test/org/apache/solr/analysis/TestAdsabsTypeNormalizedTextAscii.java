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

/**
 * Test for the normalized_text_ascii type
 * 
 */
public class TestAdsabsTypeNormalizedTextAscii extends MontySolrQueryTestCase {


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

    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "Bílá kobyla skočila přes čtyřista"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "třicet-tři stříbrných střech"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "A ještě TřistaTřicetTři stříbrných stovek"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "Cutri, R"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "Cutri,R"));
    assertU(addDocs(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "Cutri,.R"));

    assertU(commit());

    assertQ(req("q", "*:*"), "//*[@numFound='6']");
    
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
      assertQ(req("q", f + ":cutri,r"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='4']");
      assertQ(req("q", f + ":cutri,.r"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='5']");
    }
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeNormalizedTextAscii.class);
  }
}
