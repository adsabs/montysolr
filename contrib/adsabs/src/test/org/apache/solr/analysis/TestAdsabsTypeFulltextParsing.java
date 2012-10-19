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


import monty.solr.util.DocReconstructor;
import monty.solr.util.DocReconstructor.GrowableStringArray;
import monty.solr.util.DocReconstructor.Reconstructed;
import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.adsabs.solr.AdsConfig.F;

/**
 * Tests that the fulltext is parsed properly, the ads_text type
 * is not as simple as it seems
 * 
 */
public class TestAdsabsTypeFulltextParsing extends MontySolrQueryTestCase {

	@Override
	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
	    	});
		
		/*
		 * For purposes of the test, we make a copy of the schema.xml,
		 * and create our own synonym files
		 */
		
		String configFile = MontySolrSetup.getMontySolrHome()
			+ "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
		
		File newConfig;
		try {
			
			newConfig = duplicateFile(new File(configFile));
			
			File synonymsFile = createTempFile(new String[]{
					"hubble\0space\0telescope, HST",
			});
			replaceInFile(newConfig, "synonyms=\"ads_text.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		
		return newConfig.getAbsolutePath();
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
	}

	
	public void test() throws Exception {
		
	  /*
	   * The difficult part with this token type is the presence of synonyms (besides other things)
	   * So, for example in the sentence:
	   * 
	   *   Mirrors of the hubble space telescope
	   *   
	   * We want to do different things during indexing and querying
	   * 
	   *  indexing: mirrors,hubble|hubble space telescope|hst,space,telescope
	   *  querying: +mirrors +(hubble space telescope | hst)
	   *  
	   * 
	   * During the indexing we want to output BOTH the original tokens, as well as their
	   * synonyms. But in the search phase, we only want the synonyms. HOWEVER, we need
	   * the original tokens for the proximity queries, if we indexed 'hubble space telescope'
	   * as one token, we cannot search for 'hubble NEAR telescope'
	   * 
	   * The default solr synonym filter is configured for indexing, but it has the ability
	   * to do what we want. Unfortunately, the public API does not allow us to configure
	   * its behaviour.
	   * 
	   */
		assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.ADS_TEXT_TYPE, "Bílá kobyla skočila přes čtyřista"));
		assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.ADS_TEXT_TYPE, "třicet-tři stříbrných střech"));
		assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.ADS_TEXT_TYPE, "A ještě TřistaTřicetTři stříbrných křepeliček"));
		assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxxx", F.ADS_TEXT_TYPE, "Mirrors of the hubble space telescope first"));
		assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxxx", F.ADS_TEXT_TYPE, "Mirrors of the HST second"));
		assertU(adoc(F.ID, "6", F.BIBCODE, "xxxxxxxxxxxxx", F.ADS_TEXT_TYPE, "Mirrors of the Hst third"));
		assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxxx", F.ADS_TEXT_TYPE, "Mirrors of the HubbleSpaceTelescope fourth"));
		
		assertU(commit());
		
		dumpDoc(null, F.ID, F.ADS_TEXT_TYPE);
		
		
		// the ascii folding filter emits both unicode and the ascii version
		assertQ(req("q", F.ADS_TEXT_TYPE + ":Bílá"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
		assertQ(req("q", F.ADS_TEXT_TYPE + ":Bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
		assertQ(req("q", F.ADS_TEXT_TYPE + ":bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
		
		
		//setDebug(true);
		
		/*
		 * Test multi-token translation, the chain is set to recognize
		 * synonyms. So even if the query string is split into 3 tokens,
		 * we are able to join them and find their synonym (HST)
		 */
		assertQueryEquals(req("q", "hubble space telescope", "qt", "aqp"), 
				"all:Hst all:hubble space telescope all:HST", BooleanQuery.class);
		
		assertQueryEquals(req("q", "hubble space -telescope", "qt", "aqp"), 
				"all:hubble all:space -all:telescope", BooleanQuery.class);
		
		assertQueryEquals(req("q", "hubble space title:telescope", "qt", "aqp"), 
				"all:hubble all:space title:telescope", BooleanQuery.class);
		
		/*
		 * Synonym expansion 1token->many
		 */
		
		assertQueryEquals(req("q", "HST", "qt", "aqp"), 
				"all:Hst all:hubble space telescope all:HST", BooleanQuery.class);
		
		/*
		 * But right now, synonym expansion is case sensitive, so these will
		 * fail (even if 'Hst' is in the synonym list). The Acronym filter
		 * causes this behaviour. We should change that (everything must be 
		 * indexed/searched lowercase)
		 */
		assertQueryEquals(req("q", "Hst", "qt", "aqp"), 
				"all:hst", TermQuery.class);
		
		assertQueryEquals(req("q", "hst", "qt", "aqp"), 
				"all:hst", TermQuery.class);
		
		assertQueryEquals(req("q", "HST OR Hst", "qt", "aqp"), 
				"(all:Hst all:hubble space telescope all:HST) all:hst", BooleanQuery.class);
		
		
		
		
		// TODO
		//assertQueryEquals(req("q", "hubble space telescope +star", "qt", "aqp"), 
		//		"(all:Hst all:hubble space telescope all:HST) +star", BooleanQuery.class);
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeFulltextParsing.class);
    }
}
