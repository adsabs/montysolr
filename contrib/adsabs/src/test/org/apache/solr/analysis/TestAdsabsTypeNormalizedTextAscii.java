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
import org.apache.solr.util.AbstractSolrTestCase.Doc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.adsabs.solr.AdsConfig.F;

/**
 * Test for the normalized_text_ascii type
 * 
 */
public class TestAdsabsTypeNormalizedTextAscii extends MontySolrQueryTestCase {

  private int idValue = 0;

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

    assertU(adoc(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "Bílá kobyla skočila přes čtyřista"));
    assertU(adoc(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "třicet-tři stříbrných střech"));
    assertU(adoc(F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS, "A ještě TřistaTřicetTři stříbrných stovek"));

    assertU(commit());

    assertQ(req("q", "*:*"), "//*[@numFound='3']");

    // the ascii folding filter emits both unicode and the ascii version
    for (String f: F.TYPE_NORMALIZED_TEXT_ASCII_FIELDS) {
      
      // ascii normalization
      assertQueryEquals(req("q", f + ":Bílá", "qt", "aqp"), f+":bila", TermQuery.class);
      assertQueryEquals(req("q", f + ":Bila", "qt", "aqp"), f+":bila", TermQuery.class);
      assertQueryEquals(req("q", f + ":bila", "qt", "aqp"), f+":bila", TermQuery.class);
      assertQ(req("q", f + ":Bílá"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='0']");
      assertQ(req("q", f + ":Bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='0']");
      assertQ(req("q", f + ":bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='0']");
      
      // whitespace analyzer & phrases
      assertQueryEquals(req("q", f + ":třicet-tři", "qt", "aqp"), f+":tricet-tri", TermQuery.class);
      assertQ(req("q", f + ":třicet-tři"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
      
      assertQueryEquals(req("q", f + ":třicet tři", "qt", "aqp"), String.format("+%s:tricet +(%s:tři %s:tri)", f, F.DEF_FIELD, F.DEF_FIELD), BooleanQuery.class);
      assertQ(req("q", f + ":třicet tři"), "//*[@numFound='0']");
      
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
      
    }
  }
  
  public String adoc(String[] fields, String...values) {
    ArrayList<String> vals = new ArrayList<String>(Arrays.asList(values));
    String[] fieldsVals = new String[fields.length*(values.length*2)];
    int i = 0;
    for (String f: fields) {
      for (String v: values) {
        fieldsVals[i++] = f;
        fieldsVals[i++] = v;
      }
    }
    return adoc(fieldsVals);
  }
  
  @Override
  public String adoc(String... fieldsAndValues) {
    ArrayList<String> fVals = new ArrayList<String>(Arrays.asList(fieldsAndValues));
    if (fVals.indexOf(F.ID) == -1 || !(fVals.indexOf(F.ID)%2==1)) {
      fVals.add(F.ID);
      fVals.add(Integer.toString(incrementId()));
    }
    if (fVals.indexOf(F.BIBCODE) == -1 || !(fVals.indexOf(F.BIBCODE)%2==1)) {
      fVals.add(F.BIBCODE);
      String bibc = ("AAAAA........" + Integer.toString(idValue));
      fVals.add(bibc.substring(bibc.length()-13, bibc.length()));
    }
    String[] newVals = new String[fVals.size()];
    for (int i=0;i<fVals.size();i++) {
      newVals[i] = fVals.get(i);
    }
    return super.adoc(newVals);
  }
  
  public int incrementId() {
    return idValue++;
  }

  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeNormalizedTextAscii.class);
  }
}
