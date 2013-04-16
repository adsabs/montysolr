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
 * Test for the normalized_string_ascii type
 * 
 */
public class TestAdsabsTypeNormalizedStringAscii extends MontySolrQueryTestCase {


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

    assertU(addDocs(F.TYPE_NORMALIZED_STRING_ASCII_FIELDS, "Bílá kobyla skočila přes čtyřista"));
    assertU(addDocs(F.TYPE_NORMALIZED_STRING_ASCII_FIELDS, "třicet-tři stříbrných střech"));
    assertU(addDocs(F.TYPE_NORMALIZED_STRING_ASCII_FIELDS, "A ještě TřistaTřicetTři stříbrných stovek"));
    assertU(addDocs(F.TYPE_NORMALIZED_STRING_ASCII_FIELDS, "one two three"));
    assertU(addDocs(F.TYPE_NORMALIZED_STRING_ASCII_FIELDS, "este-c'est que"));
    assertU(addDocs(F.TYPE_NORMALIZED_STRING_ASCII_FIELDS, "568"));

    assertU(commit());
    
    //dumpDoc(null, F.ID, F.TYPE_NORMALIZED_STRING_ASCII_FIELDS[0]);
    
    assertQ(req("q", "*:*"), "//*[@numFound='6']");

    assertQueryEquals(req("q", "bibcode:Bílá", "qt", "aqp"), "bibcode:bila", TermQuery.class);
    assertQueryEquals(req("q", "bibcode:Bila-bila", "qt", "aqp"), "bibcode:bilabila", TermQuery.class);
    assertQ(req("q", "bibcode:Bílá*"), 
    		"//*[@numFound='1']", 
    		"//doc[1]/str[@name='id'][.='0']");
    assertQ(req("q", "bibcode:Bílá-kobyla*"), "//*[@numFound='1']", 
    		"//doc[1]/str[@name='id'][.='0']");
    assertQ(req("q", "bibcode:kobyla"), "//*[@numFound='0']");
    
    assertQ(req("q", "bibcode:\"one two three\""), 
    		"//*[@numFound='1']", 
    		"//doc[1]/str[@name='id'][.='3']");
    assertQ(req("q", "bibcode:\"este-c'est que\""), 
    		"//*[@numFound='1']", 
    		"//doc[1]/str[@name='id'][.='4']");
    assertQ(req("q", "bibcode:568"), 
    		"//*[@numFound='1']", 
    		"//doc[1]/str[@name='id'][.='5']");
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeNormalizedStringAscii.class);
  }
}
