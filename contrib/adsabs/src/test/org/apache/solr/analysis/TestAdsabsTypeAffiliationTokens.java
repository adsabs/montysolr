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
			    		+ "A01400;SI/CfA;Center for Astrophysics | Harvard and Smithsonian;Harvard Smithsonian Center for Astrophysics;RID61814;03c3r2d17;Q1133697;grid.455754.2\n"
			    		+ "AX;SI\n"
			    		+ "AB=>CfA\n"
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
    assertU(addDocs(
        "institution", "SI/CfA; Harvard U/CfA", 
        "institution", "Harvard U/Phys; Brown U/Ast",
        "aff", "SI/CfA")
        );
    assertU(addDocs(
        "institution", "Harvard U/Law; -",
        "institution", "SI/CfA; Harvard U/CfA", 
        "aff", "SI/CfA")
        );
    assertU(commit());
    
    assertQueryEquals(req("q", "aff_id:https\\://ror.org/ror.1"), 
        "Synonym(aff_id:bar aff_id:foo aff_id:ror.1)",
        SynonymQuery.class
        );
    assertQueryEquals(req("q", "aff_id:\"https://ror.org/ror.1\""), 
        "Synonym(aff_id:bar aff_id:foo aff_id:ror.1)",
        SynonymQuery.class
        );
    
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
    assertQ(req("q", "pos(institution:\"-\", 1)"), "//*[@numFound='2']");
    assertQ(req("q", "pos(institution:\"INFN/Catania\", 1)"), "//*[@numFound='0']");
    assertQ(req("q", "pos(institution:\"INFN\", 1)"), "//*[@numFound='0']");
    assertQ(req("q", "pos(institution:\"Catania\", 1)"), "//*[@numFound='0']");
    
    assertQ(req("q", "pos(institution:\"INFN/Catania\", 2)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"INFN\", 2)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"Catania\", 2)"), "//*[@numFound='1']");
    
    // search parts of the affiliation
    assertQ(req("q", "institution:\"SI\""), "//*[@numFound='2']");
    assertQ(req("q", "institution:\"CfA\""), "//*[@numFound='2']");
    assertQ(req("q", "institution:\"Harvard U\""), "//*[@numFound='2']");
    assertQ(req("q", "institution:\"Law\""), "//*[@numFound='1']");
    assertQ(req("q", "institution:\"Phys\""), "//*[@numFound='1']");
    
    // search parts (but honour position)
    assertQ(req("q", "pos(institution:\"SI\", 1)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"CfA\", 1)"), "//*[@numFound='1']");
    assertQ(req("q", "pos(institution:\"Harvard U\", 2)"), "//*[@numFound='2']");
    assertQ(req("q", "pos(institution:\"CfA\", 2)"), "//*[@numFound='1']");
    
    // search parent/child
    assertQ(req("q", "institution:\"SI/CfA\""), "//*[@numFound='2']");
    
    // do the same but as phrase; it should fail because the parser WILL NOT
    // treat empty space as a delimiter; it considers it part of the token
    // like 'Harvard U'
    assertQ(req("q", "institution:\"SI CfA\""), "//*[@numFound='0']");
    
    // proximity operator however should yield the record
    assertQ(req("q", "institution:\"SI\" NEAR1 institution:\"CfA\""), "//*[@numFound='2']");
    
    // but not mix up insitutions that were separated by ';' (those affiliations
    // belong to different authors/persons)
    assertQ(req("q", "institution:\"Law\" NEAR5 institution:\"CfA\""), "//*[@numFound='0']");
    
    // one person however can have multiple affiliations; and they can be searched via proximity
    assertQ(req("q", "institution:\"Phys\" NEAR5 institution:\"Ast\""), "//*[@numFound='1']");
    
    // SI/CfA is also known through identifiers/canonical names - NOTE: the input synonyms
    // MUST CONTAIN correct entry, i.e. "SI/CfA" - for some reason we used to have "SI CfA"
    assertQ(req("q", "institution:\"A01400\""), "//*[@numFound='2']");
    assertQ(req("q", "institution:\"RID61814\""), "//*[@numFound='2']");
    assertQ(req("q", "institution:\"Harvard Smithsonian Center for Astrophysics\""), "//*[@numFound='2']");
    assertQ(req("q", "institution:\"03c3r2d17\""), "//*[@numFound='2']");
    assertQ(req("q", "institution:\"Q1133697\""), "//*[@numFound='2']");
    assertQ(req("q", "institution:\"grid.455754.2\""), "//*[@numFound='2']");

    
    // what is the meaning of the pipe? (|) -- it forces our parser to treat the query
    // as a regex; to not do that we have to set aqp.regex.disallowed.fields
    assertQ(req("q", "institution:\"Center for Astrophysics | Harvard and Smithsonian\"",
        "aqp.regex.disallowed.fields", "institution"), "//*[@numFound='2']");
    
    // and we also want to find the records via parent/child relationship BUT using
    // synonyms; so assume that parent (SI) is also known under synonym 'AX' and 
    // CfA is known under synonym 'AB'; the search "AX/AB" should then find the same
    // thing as "SI/CfA" -- HOWEVER, note, this feature requires synonym mapping
    // either of the explicit form:
    // AX => SI
    // or more forgiving (and more wasteful):
    // AX;SI
    // IMHO this feature is confusing; will bloat the synonym file and users
    // are going to be confused by it. I'd just say: use canonical forms of 
    // the synonym. And, and... be aware that if the synonym file contains a cycle
    // i.e. 'parent/child' entry sharing synonyms with 'child' entry; then the
    // two will be merged and considered as one:
    // 
    // SI/CfA;A01400
    // CfA;A014000
    // 
    // becomes:
    // SI/CfA;A01400;CfA
    assertQ(req("q", "institution:\"AX/AB\""), "//*[@numFound='2']");
    
  }
  


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAffiliationTokens.class);
  }
}
