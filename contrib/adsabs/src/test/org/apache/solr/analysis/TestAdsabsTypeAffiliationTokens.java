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

import java.io.File;
import java.io.IOException;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.SynonymQuery;
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
    
    
    schemaString = getSchemaFile();
      
    configString = MontySolrSetup.getMontySolrHome()
        + "/contrib/examples/adsabs/server/solr/collection1/conf/solrconfig.xml";
    
    initCore(configString, schemaString, MontySolrSetup.getSolrHome()
			    + "/example/solr");
  }

	public static String getSchemaFile() {

		/*
		 * For purposes of the test, we make a copy of the schema.xml, and create
		 * our own synonym files
		 */

		String configFile = MontySolrSetup.getMontySolrHome()
		    + "/contrib/examples/adsabs/server/solr/collection1/conf/schema.xml";

		File newConfig;
		try {

			newConfig = duplicateFile(new File(configFile));

			
			File simpleTokenSynonymsFile = createTempFile(
			    new String[] { "id1,id2\n"
			        + "ror.1;foo;bar\n"
			    		+ "A00001;Aalborg U;Aalborg University;RID1004;04m5j1k67;000000010742471X;Q601956;grid.5117.2;\n\n"
			    		+ "A00002;Aarhus U;Aarhus University;RID1006;01aj84f44;0000000119562722;Q924265;grid.7048.b;\n"
			    		});

			replaceInFile(newConfig, "synonyms=\"aff_id.synonyms\"",
			    "synonyms=\"" + simpleTokenSynonymsFile.getAbsolutePath() + "\"");

		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}

		return newConfig.getAbsolutePath();
	}

  public void test() throws Exception {

    assertU(addDocs("institution", "foo bar", "institution", "bar baz/hey"));
    assertU(addDocs("institution", "Kavli Institute/Dept of Physics"));
    assertU(addDocs("institution", "U Catania/Dep Phy Ast; -",
        "institution", "U Catania/Dep Phy Ast; -; -; INFN/Catania",
        "institution", "U Catania/Dep Phy Ast; -"
        ));
    assertU(commit());
    
    
    // test synonyms
    assertQueryEquals(req("q", "aff_id:\"ror.1\""), 
        "Synonym(aff_id:bar aff_id:foo aff_id:ror.1)",
        SynonymQuery.class
        );
    assertQueryEquals(req("q", "aff_id:\"ROR.1\""), 
        "Synonym(aff_id:bar aff_id:foo aff_id:ror.1)",
        SynonymQuery.class
        );
    assertQueryEquals(req("q", "aff_id:\"A00001\""), 
        "Synonym(aff_id:000000010742471x aff_id:04m5j1k67 aff_id:a00001 aff_id:aalborg u aff_id:aalborg university aff_id:grid.5117.2 aff_id:q601956 aff_id:rid1004)",
        SynonymQuery.class
        );
    assertQueryEquals(req("q", "aff_id:\"a00001\""), 
        "Synonym(aff_id:000000010742471x aff_id:04m5j1k67 aff_id:a00001 aff_id:aalborg u aff_id:aalborg university aff_id:grid.5117.2 aff_id:q601956 aff_id:rid1004)",
        SynonymQuery.class
        );
    assertQueryEquals(req("q", "aff_id:\"04m5j1k67\""), 
        "Synonym(aff_id:000000010742471x aff_id:04m5j1k67 aff_id:a00001 aff_id:aalborg u aff_id:aalborg university aff_id:grid.5117.2 aff_id:q601956 aff_id:rid1004)",
        SynonymQuery.class
        );
    assertQueryEquals(req("q", "aff_id:\"Aalborg U\""), 
        "Synonym(aff_id:000000010742471x aff_id:04m5j1k67 aff_id:a00001 aff_id:aalborg u aff_id:aalborg university aff_id:grid.5117.2 aff_id:q601956 aff_id:rid1004)",
        SynonymQuery.class
        );
    
    // make sure docs are there
    assertQ(req("q", "*:*"), "//*[@numFound>='2']");
    
    // query parsing tests
    assertQueryEquals(req("q", "institution:\"Foo Bar\""), 
        "institution:foo bar",
        TermQuery.class
        );
    // it is not visible here, but tokens are: foo bar, baz
    assertQueryEquals(req("q", "institution:\"Foo Bar/Baz\""), 
        "institution:\"foo bar baz\"",
        PhraseQuery.class
        );
    
    // test matches
    assertQ(req("q", "institution:\"foo bar\""), 
        "//*[@numFound='1']",
        "//doc/str[@name='id'][.='0']"
        );
    assertQ(req("q", "institution:\"bar baz\""), 
        "//*[@numFound='1']",
        "//doc/str[@name='id'][.='0']"
        );
    assertQ(req("q", "institution:HEY"), 
        "//*[@numFound='1']",
        "//doc/str[@name='id'][.='0']"
        );
    assertQ(req("q", "institution:\"bar BAZ/heY\""), 
        "//*[@numFound='1']",
        "//doc/str[@name='id'][.='0']"
        );
    
    // only match full tokens
    assertQ(req("q", "institution:foo"), "//*[@numFound='0']");
    assertQ(req("q", "institution:\"baz/hey\""), "//*[@numFound='0']");
    

    // check the affiliation is there stored as one string
    assert h.query(req("q", "institution:\"Kavli Institute/Dept of Physics\"", "fl", "institution"))
 		.contains("<str>Kavli Institute/Dept of Physics</str>"
        );
    
    //"U Catania/Dep Phy Ast; -; -; INFN/Catania"
    assertQ(req("q", "institution:\"U Catania\""), "//*[@numFound='1']");
    assertQ(req("q", "institution:\"Dep Phy Ast\""), "//*[@numFound='1']");
    assertQ(req("q", "institution:\"U Catania/Dep Phy Ast\""), "//*[@numFound='1']");
    
    // must not happen if positionIncrementGap > 0
    assertQ(req("q", "institution:\"Catania/U Catania\""), "//*[@numFound='0']");
    
    // this is ok, it's expected
    assertQ(req("q", "institution:\"-;INFN\""), "//*[@numFound='1']");
    assertQ(req("q", "institution:\"-;-\""), "//*[@numFound='1']");
    
    assertQ(req("q", "pos(institution:\"U Catania/Dep Phy Ast\", 1)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"U Catania\", 1)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"Dep Phy Ast\", 1)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"-\", 1)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"INFN/Catania\", 1)"), "//*[@numFound='0']");
    assertQ(req("q", "pos(institution:\"INFN\", 1)"), "//*[@numFound='0']");
    assertQ(req("q", "pos(institution:\"Catania\", 1)"), "//*[@numFound='0']");
    
    assertQ(req("q", "pos(institution:\"INFN/Catania\", 2)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"INFN\", 2)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"Catania\", 2)"), "//*[@numFound='1']");
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAffiliationTokens.class);
  }
}
