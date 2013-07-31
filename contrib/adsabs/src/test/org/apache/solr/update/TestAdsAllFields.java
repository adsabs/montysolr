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

package org.apache.solr.update;


import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.AdsDataSource;
import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * This test verifies all indexes are in place and the search against
 * them works. This is the main test for the whole ADS search.
 * 
 * The test is using demo-records from contrib/adsabs/src/test-files/ads-demo-records.xml
 * 
 * We mock MongoDB data and we do NOT use Invenio
 * 
 */

//@SuppressCodecs({"Lucene3x"})
public class TestAdsAllFields extends MontySolrQueryTestCase {
  
  @BeforeClass
  public static void beforeTestAdsDataImport() throws Exception {
    // to use filesystem instead of ram
    System.setProperty("solr.directoryFactory","solr.SimpleFSDirectoryFactory");
    //System.setProperty("tests.codec","SimpleText");
    MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() 
        + "/contrib/invenio/src/python");
    MontySolrSetup.addTargetsToHandler("monty_invenio.schema.tests.targets");

  }

  @Override
  public String getSchemaFile() {
    makeResourcesVisible(this.solrConfig.getResourceLoader(),
        new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
    });
    String configFile = MontySolrSetup.getMontySolrHome()
    + "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
    return configFile;
  }

  @Override
  public String getSolrConfigFile() {

    String configFile = MontySolrSetup.getMontySolrHome()
    		+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
    String dataConfig = MontySolrSetup.getMontySolrHome()
    		+ "/contrib/examples/adsabs/solr/collection1/conf/data-config.xml";

    File newConfig = new File(configFile);

    
    //Mock the MongoDB data
    File newDataConfig;
    try {
      newConfig = duplicateFile(new File(configFile));
      newDataConfig = duplicateModify(new File(dataConfig), 
      		"mongoHost=\"adszee\"", String.format("mongoHost=\"%s\"", System.getProperty("tests.mongodb.host")),
      		"AdsDataSource", this.getClass().getCanonicalName() + "\\$MongoMockDataSource");
      replaceInFile(newConfig, "data-config.xml", newDataConfig.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException(e.getMessage());
    }

    return newConfig.getAbsolutePath();
  }




  public void testImport() throws Exception {

    String testDir = MontySolrSetup.getMontySolrHome()
    + "/contrib/adsabs/src/test-files/";

    SolrRequestHandler handler = h.getCore().getRequestHandler(
    "/invenio/import");

    SolrCore core = h.getCore();

    String url = "file://" + MontySolrSetup.getMontySolrHome()
    + "/contrib/adsabs/src/test-files/ads-demo-records.xml";

    SolrQueryRequest req = req("command", "full-import", "dirs", testDir,
        "commit", "true", "url", url);
    SolrQueryResponse rsp = new SolrQueryResponse();

    // if you get AssertionError here, most likely the test cannot access
    // the mongodb instance (on localhost)
    core.execute(handler, req, rsp);

    commit("waitFlush", "true", "waitSearcher", "true");

    // dumpDoc(null);

    req = req("command", "full-import",
        "dirs", testDir,
        "commit", "true",
        "url", "file:///non-existing-file?p=recid:500->505 OR recid:508&foo=bar"
    );
    rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);

    assertQ(req("qt", "/invenio-doctor", "command", "detailed-info"), 
        "//str[@name='queueSize'][.='3']",
        "//str[@name='failedRecs'][.='0']",
        "//str[@name='failedBatches'][.='0']",
        "//str[@name='failedTotal'][.='0']",
        "//str[@name='registeredRequests'][.='3']",
        "//str[@name='restartedRequests'][.='0']",
        "//str[@name='docsToCheck'][.='7']",
        "//str[@name='status'][.='idle']"
    );



    

    /*
     * id - str type, the unique id key, we do no processing
     */
    
    assertQ(req("q", "id:2"), "//*[@numFound='1']");
    assertQ(req("q", "id:9218605"), "//*[@numFound='1']");
    assertQ(req("q", "id:9218920"), "//*[@numFound='1']");
    assertQ(req("q", "id:9106442"), "//*[@numFound='0']");
    assertQ(req("q", "id:002"), "//*[@numFound='0']");


    /*
     * recid - recid is a int field
     */

    assertQ(req("q", "recid:2"), "//*[@numFound='1']");
    assertQ(req("q", "recid:9218605"), "//*[@numFound='1']");
    assertQ(req("q", "recid:9218920"), "//*[@numFound='1']");
    assertQ(req("q", "recid:9106442"), "//*[@numFound='0']");
    assertQ(req("q", "recid:002"), "//*[@numFound='1']");
    assertQ(req("q", "recid:0009218605"), "//*[@numFound='1']");
    assertQ(req("q", "recid:9218920"), "//*[@numFound='1']");
    assertQ(req("q", "recid:9106442"), "//*[@numFound='0']");		


    //dumpDoc(null, "bibdoc", "author", "author_norm", "first_author", "first_author_norm", "author_facet", "author_surname", "first_author_surname", "author_facet_hier", "first_author_facet_hier");

    /*
     * author 
     * 
     * here we really test only the import mechanism, the order of authors
     * and duplication. The parsing logic has its own unittest
     */
    assertQ(req("q", "author:\"Mosser, B\""), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='9218605']");
    assertQ(req("q", "author:\"Mosser, B\" AND author:\"Goupil, M\""), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='9218605']");
    //System.out.println(h.query(req("q", "author:\"Mosser, B\"")));
    assert h.query(req("q", "author:\"Mosser, B\""))
    .contains("<arr name=\"author_norm\">" +
        "<str>Mosser, B</str>" + 
        "<str>Goupil, M</str>" + 
        "<str>Belkacem, K</str>" + 
        "<str>Stello, D</str>" + 
        "<str>Marques, J</str>" + 
        "<str>Elsworth, Y</str>" + 
        "<str>Barban, C</str>" + 
        "<str>Beck, P</str>" + 
        "<str>Bedding, T</str>" + 
        "<str>De Ridder, J</str>" + 
        "<str>Garcia, R</str>" + 
        "<str>Hekker, S</str>" + 
        "<str>Kallinger, T</str>" + 
        "<str>Samadi, R</str>" + 
        "<str>Stumpe, M</str>" + 
        "<str>Barclay, T</str>" + 
    "<str>Burke, C</str></arr>");
    assert h.query(req("q", "author:\"Mosser, B\""))
    .contains("<arr name=\"author\">" + 
        "<str>Mosser, B.</str>" + 
        "<str>Goupil, M. J.</str>" + 
        "<str>Belkacem, K.</str>" + 
        "<str>Stello, D.</str>" + 
        "<str>Marques, J. P.</str>" + 
        "<str>Elsworth, Y.</str>" + 
        "<str>Barban, C.</str>" + 
        "<str>Beck, P. G.</str>" + 
        "<str>Bedding, T. R.</str>" + 
        "<str>De Ridder, J.</str><" + 
        "str>Garcia, R. A.</str>" + 
        "<str>Hekker, S.</str>" + 
        "<str>Kallinger, T.</str>" + 
        "<str>Samadi, R.</str>" + 
        "<str>Stumpe, M. C.</str>" + 
        "<str>Barclay, T.</str>" + 
        "<str>Burke, C. J.</str>" + 
    "</arr>");

   

    /*
     * For the reference resolver, the field which contains only the last
     * name of the first author
     * 
     *  first_author
     *  first_author_norm
     *  first_author_surname
     *  author_norm
     */

    assertQ(req("q", "first_author:\"Cutri, R M\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "first_author:\"Cutri, R\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "first_author:\"Cutri,R\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "first_author:Cutri, R"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );

    // the record contains "Cutri, R"
    assertQ(req("q", "first_author_norm:\"Cutri, R. M.\""), 
        "//*[@numFound='0']"
    );
    assertQ(req("q", "first_author_norm:\"Cutri, R.\""), 
        "//*[@numFound='0']"
    );
    assertQ(req("q", "first_author_norm:\"Cutri, R\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "first_author_norm:\"cutri, r\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    // success because the field is normalized
    assertQ(req("q", "first_author_norm:\"Cutri,R\""), 
        "//*[@numFound='1']"
    );
    assertQ(req("q", "first_author_norm:\"Cutri,.R\""), 
        "//*[@numFound='0']"
    );


    assertQ(req("q", "first_author_surname:cutri"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "first_author_surname:Cutri"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "first_author_surname:\"Cutri,\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "first_author_surname:\"Cutri,R\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "first_author_surname:CUTRI"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );

    assertQ(req("q", "author_surname:\"Nonexisting, F\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "author_surname:\"Nonexisting,F\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "author_surname:\"Nonexisting,\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );

    assertQ(req("q", "author_norm:\"Nonexisting, F\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );

    /*
     * author facets
     */

    assertQ(req("q", "author_facet:\"Tenenbaum, P\""), "//*[@numFound='1']");
    assertQ(req("q", "author_facet:\"Mosser, B\""), "//*[@numFound='1']");

    assertQ(req("q", "first_author_facet_hier:\"0/Cutri, R\""), "//*[@numFound='1']");
		assertQ(req("q", "first_author_facet_hier:\"1/Cutri, R/Cutri, R. M.\""), "//*[@numFound='1']");
		assertQ(req("q", "author_facet_hier:\"0/Stumpe, M\""), "//*[@numFound='2']");
		assertQ(req("q", "author_facet_hier:\"1/Stumpe, M/Stumpe, M. C.\""), "//*[@numFound='2']");
		assertQ(req("q", "author_facet_hier:\"1//et al.\""), "//*[@numFound='0']");
		
    
    /*
     * page marc:773
     */
    //dumpDoc(null, "recid", "page");
    assertQ(req("q", "page:2056"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']");
    assertQ(req("q", "page:2056-2059 AND recid:9218920"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']");
    assertQ(req("q", "page:a056"), "//*[@numFound='1']");
    assertQ(req("q", "page:a056-"), "//*[@numFound='1']");
    assertQ(req("q", "page:a056-2059"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='2']");
    assertQ(req("q", "page:90024"), "//*[@numFound='1']");

    /*
     * volume
     */
    assertQ(req("q", "volume:l219"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218511']");
    assertQ(req("q", "volume:L219"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218511']");

    /*
     * issue
     */
    assertQ(req("q", "issue:4"), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='9218511']");
    assertQ(req("q", "issue:2x"), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='9143768']");


    /*
     * aff
     */
    //dumpDoc(null, "recid", "aff");
    assertQ(req("q", "aff:NASA"),
        "//doc/int[@name='recid'][.='9218511']",
    "//*[@numFound='1']"); // regardless of case
    assertQ(req("q", "aff:SPACE"), "//*[@numFound='0']"); // be case sensitive with uppercased query terms
    assertQ(req("q", "aff:KAVLI"), "//*[@numFound='0']"); // same here
    assertQ(req("q", "aff:kavli"), // otherwise case-insensitive
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='9218511']"); 
    assertQ(req("q", "aff:Kavli"), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='9218511']");
    assertQ(req("q", "aff:46556"), 
    "//*[@numFound='1']");
    assertQ(req("q", "aff:\"Notre Dame\""), 
    "//*[@numFound='1']");

    /*
     * email
     */
    assertQ(req("q", "email:rcutri@example.edu"), "//*[@numFound='1']");
    assertQ(req("q", "email:rCutri@example.edu"), "//*[@numFound='1']");
    assertQ(req("q", "email:rcutri@example*"), "//*[@numFound='1']");


    /*
     * database & bibgroup
     */
    assertQ(req("q", "database:astronomy"), "//*[@numFound='9']");
    assertQ(req("q", "database:Astronomy"), "//*[@numFound='9']");
    assertQ(req("q", "database:ASTRONOMY"), "//*[@numFound='9']");
    assertQ(req("q", "database:ASTRONOM*"), "//*[@numFound='9']");
    assertQ(req("q", "database:ASTRONOM?"), "//*[@numFound='9']");
    assertQ(req("q", "database:astronom*"), "//*[@numFound='9']");
    assertQ(req("q", "database:astronom?"), "//*[@numFound='9']");

    assertQ(req("q", "bibgroup:cfa"), "//*[@numFound='3']");
    assertQ(req("q", "bibgroup:CFA"), "//*[@numFound='3']");
    assertQ(req("q", "bibgroup_facet:CfA"), "//*[@numFound='3']");
    assertQ(req("q", "bibgroup_facet:cfa"), "//*[@numFound='0']");

    assertQ(req("q", "bibgroup:cf*"), "//*[@numFound='3']");
    assertQ(req("q", "bibgroup:CF*"), "//*[@numFound='3']");
    assertQ(req("q", "bibgroup:?FA"), "//*[@numFound='3']");

    assertQ(req("q", "property:catalog AND property:nonarticle"), "//*[@numFound='4']");
    assertQ(req("q", "property:Catalog AND property:Nonarticle"), "//*[@numFound='4']");
    assertQ(req("q", "property:CATALOG AND property:nonarticle"), "//*[@numFound='4']");
    assertQ(req("q", "property:catalog AND property:NONARTICLE"), "//*[@numFound='4']");


    /*
     * Bibcodes
     */

    assertQ(req("q", "bibcode:2012yCat..35409143M"), "//*[@numFound='1']");
    assertQ(req("q", "bibcode:2012ycat..35409143m"), "//*[@numFound='1']");
    assertQ(req("q", "bibcode:2012YCAT..35409143M"), "//*[@numFound='1']");
    assertQ(req("q", "bibcode:2012YCAT..*"), "//*[@numFound='5']");
    assertQ(req("q", "bibcode:201?YCAT..35409143M"), "//*[@numFound='1']");
    assertQ(req("q", "bibcode:*YCAT..35409143M"), "//*[@numFound='1']");


    /*
     * Bibstem is derived from bibcode, it is either the bibcode[4:9] OR
     * bibcode[4:13] when the volume information is NOT present
     * 
     * So this bibcode: 2012yCat..35a09143M
     * has bibstem:     yCat..35a
     * 
     * But this bicode: 2012yCat..35009143M
     * has bibstem:     yCat
     * 
     * Bibstem is not case sensitive (at least for now, so the above values
     * are lowercased)
     * 
     */

    //dumpDoc(null, F.ID, "bibcode", "bibstem");

    //System.out.println(direct.request("/select?q=*:*&fl=bibcode,bibstem,recid,title", null).replace("</", "\n</"));
    assertQ(req("q", "bibstem:YCAT"), "//*[@numFound='5']");
    assertQ(req("q", "bibstem:yCat"), "//*[@numFound='5']");
    assertQ(req("q", "bibstem:ycat"), "//*[@numFound='5']");

    assertQ(req("q", "bibstem:yCat..35a"), "//*[@numFound='1']");
    assertQ(req("q", "bibstem:yCat..35*"), "//*[@numFound='3']");
    assertQ(req("q", "bibstem:yCat..35?"), "//*[@numFound='3']");

    assertQ(req("q", "bibstem:apj.."), 
        "//*[@numFound='3']",
        "//doc/str[@name='bibcode'][.='1981ApJ...243..677D']",
        "//doc/str[@name='bibcode'][.='2012ApJ...760..135R']",
        "//doc/str[@name='bibcode'][.='1991ApJ...371..665R']");

    //XXX: this has changed, the last dot gets removed when we try to guess regex query
    // need a better solution for this ambiguity yCat..* becomes 'yCat.*'
    assertQ(req("q", "bibstem:yCat..*"), "//*[@numFound='5']");
    assertQ(req("q", "bibstem:yCat.*"), "//*[@numFound='5']");
    assertQ(req("q", "bibstem:yCat*"), "//*[@numFound='5']");
    assertQ(req("q", "bibstem:stat.conf"), "//*[@numFound='1']");
    assertQ(req("q", "bibstem:STAT.CONF"), "//*[@numFound='1']");





    /*
     * doi:
     * 
     * According to the standard, doi can contain almost any utf-8
     * char
     */

    //dumpDoc(null, F.ID, "bibcode", "doi");
    assertQ(req("q", "doi:abcds/esdfs.123045"), "//*[@numFound='1']");
    assertQ(req("q", "doi:doi\\:abcds/esdfs.123045"), "//*[@numFound='1']");
    assertQ(req("q", "doi:\"doi:ŽŠČŘĎŤŇ:123456789\""), "//*[@numFound='1']");
    assertQ(req("q", "doi:\"doi:ŽŠČŘĎŤŇ:123456789\""), "//*[@numFound='1']");
    assertQ(req("q", "doi:\"doi:ŽŠČŘĎŤŇ.123456789\""), "//*[@numFound='1']");
    assertQ(req("q", "doi:\"doi:žščřďťň.123456789\""), "//*[@numFound='1']");
    assertQ(req("q", "doi:\"doi:žščŘĎŤŇ?123456789\""), "//*[@numFound='2']");
    assertQ(req("q", "doi:\"doi:žščŘĎŤŇ\\?123456789\""), "//*[@numFound='2']");

    //dumpDoc(null, "recid", "title", "keyword");

    /*
     * keywords
     */
    assertQ(req("q", "keyword:\"classical statistical mechanics\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='5979890']"
    );
    assertQ(req("q", "keyword:\"WORLD WIDE WEB\""), // should be case-insensitive 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='5979890']"); 
    assertQ(req("q", "keyword:\"fluid dynamics\""),
        "//doc/int[@name='recid'][.='3813361']", // should get both 695$a ...
    "//*[@numFound='1']"); 
    assertQ(req("q", "keyword:\"angular momentum\""), // ... and 695$b
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='3813361']");  
    assertQ(req("q", "keyword:WISE"), // ... and 653$a 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    ); 
    assertQ(req("q", "keyword:UNWISE"), // ... and 653$b 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "keyword:wise"), // ... and 653$a 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    ); 
    assertQ(req("q", "keyword:unwise"), // ... and 653$b 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218920']"
    );
    assertQ(req("q", "keyword:\"planets and satellites\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218511']"
    ); 

    assertQ(req("q", "keyword_norm:\"angular momentum\""),
        "//doc/int[@name='recid'][.='3813361']",
    "//*[@numFound='1']"); // case-insensitive
    assertQ(req("q", "keyword_norm:Magnitudes"), "//*[@numFound='0']"); // should not get 695$a 
    assertQ(req("q", "keyword_norm:WISE"), "//*[@numFound='0']"); // ... or 653$a
    assertQ(req("q", "keyword_norm:unwise"), "//*[@numFound='1']"); // ... but 653$b
    assertQ(req("q", "keyword_norm:\"methods numerical\""), "//*[@numFound='1']"); // should get 695$b 

    assertQ(req("q", "keyword_facet:\"world wide web\""), "//*[@numFound='0']"); // case-sensitive
    assertQ(req("q", "keyword_facet:planets"), "//*[@numFound='0']"); // not tokenized
    assertQ(req("q", "keyword_facet:\"planets and satellites\""), "//*[@numFound='1']"); // should get 653$b
    assertQ(req("q", "keyword_facet:\"methods numerical\""), "//*[@numFound='1']"); // should get 695$b



    /*
     * identifier
     * 
     * should be translated into the correct field (currently, the grammar 
     * understands only arxiv: and doi: (and doi gets handled separately)
     * 
     * Also, the subfields a|z|y are to be indexed inside 'identifier' index
     */
    //dumpDoc(null, "recid", "identifier");
    assertQ(req("q", "arxiv:1234.5678"), "//*[@numFound='1']");
    assertQ(req("q", "arxiv:\"arXiv:1234.5678\""), "//*[@numFound='1']");
    assertQ(req("q", "arXiv:1234.5678"), "//*[@numFound='1']");
    assertQ(req("q", "identifier:1234.5678"), "//*[@numFound='1']");
    assertQ(req("q", "arXiv:hep-ph/1234"), "//*[@numFound='1']");
    assertQ(req("q", "arxiv:\"ARXIV:hep-ph/1234\""), "//*[@numFound='1']");
    assertQ(req("q", "arxiv:hep-ph/1234"), "//*[@numFound='1']");
    assertQ(req("q", "identifier:hep-ph/1234"), "//*[@numFound='1']");

    assertQ(req("q", "identifier:2001test.mat..6096A"), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='5979890']");
    assertQ(req("q", "identifier:2001cond.mat..6096A"),  
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='5979890']");
    assertQ(req("q", "identifier:cond-mat/0106096"), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='5979890']");
    assertQ(req("q", "identifier:2002RvMP...74...47A"),  
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='5979890']");
    assertQ(req("q", "identifier:1992ARA&A..30..543M"),
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='3813361']");

    
    /*
     * grants
     * 
     */
    //dumpDoc(null, "recid", "bibcode", "grant", "grant_ids", "grant_facet_hier", "reference");
		assertQ(req("q", "grant:\"NSF-AST 0618398\""),
  		"//*[@numFound='1']",
  		"//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']");
		assertQ(req("q", "grant_facet_hier:\"0/NSF-AST\""),
  		"//*[@numFound='1']",
  		"//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']");
		assertQ(req("q", "grant_facet_hier:1/NSF-AST/0618398"),
  		"//*[@numFound='1']",
  		"//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']");
		assertQ(req("q", "grant_facet_hier:0/NSF-AST"),
	  		"//*[@numFound='1']",
	  		"//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']");
		

    /*
     * title
     * 
     * just basics here, the parsing tests are inside TestAdstypeFulltextParsing
     * 
     */
    assertQ(req("q", "title:\"title is not available\""), "//*[@numFound='1']"); // everything besides title is stopword
    assertQ(req("q", "title:no-sky"), "//*[@numFound='1']");
    assertQ(req("q", "title:nosky"), "//*[@numFound='1']");
    assertQ(req("q", "title:KEPLER"), "//*[@numFound='0']"); // should search only for acronym acr::kepler
    assertQ(req("q", "title:kepler"), "//*[@numFound='2']"); // normal search
    assertQ(req("q", "title:\"q\\'i\""), 
        "//*[@numFound='1']",
    		"//doc/int[@name='recid'][.='4']");

    
    /*
     * alternate_title
     * 
     * should be copid into main title field
     */

    assertQ(req("q", "alternate_title:\"Probing red giants\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218605']"
    ); 
    assertQ(req("q", "title:\"Probing red giants\""), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218605']"
    );
    assertQ(req("q", "alternate_title:\"Oscillations of red giants\""), 
    		"//*[@numFound='0']"
    		);
    assertQ(req("q", "title:oscillations alternate_title:probing"), 
    		"//*[@numFound='1']",
    		"//doc/int[@name='recid'][.='9218605']");
    
    
    /*
     * abstract
     * 
     */
    assertQ(req("q", "abstract:abstract"), "//*[@numFound='1']"); // everything besides title is stopword
    assertQ(req("q", "abstract:No-SKy"), "//*[@numFound='1']");
    assertQ(req("q", "abstract:nosky"), "//*[@numFound='1']");
    assertQ(req("q", "abstract:sph"), "//*[@numFound='1']");
    assertQ(req("q", "abstract:SPH"), "//*[@numFound='1']");
    assertQ(req("q", "abstract:PARTICLE"), "//*[@numFound='0']"); // acronyms = acr::particle

    // tokens with special characters inside must be searched as a phrase, otherwise it
    // becomes: abstract:q'i abstract:q abstract:i abstract:qi
    // but even as a phrase, it will search for: "q (i qi)"
    assertQ(req("q", "abstract:\"q\\'i\"", "fl", "recid,abstract,title"), "//*[@numFound='1']");
    assertQ(req("q", "abstract:\"q'i\"", "fl", "recid,abstract,title"), "//*[@numFound='1']");
    assertQ(req("q", "abstract:\"q\\\\'i\"", "fl", "recid,abstract,title"), "//*[@numFound='1']");
    assertQ(req("q", "abstract:ABSTRACT", "fl", "recid,abstract,title"), "//*[@numFound='0']"); // is considered acronym


    /*
     * reference
     */
    assertQ(req("q", "reference:2010mnras.407.2611p"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9143768']"
    );
    assertQ(req("q", "reference:2009ApJ...694..556B"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9143768']"
    );
    assertQ(req("q", "reference:2004ApJS..154..519S"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9143768']"
    );


    /*
     * unfielded search
     * 
     * test we get records without specifying the field (depends on the current
     * solrconfig.xml setup)
     * 
     * author^2 title^1.4 abstract^1.3 keyword^1.4 keyword_norm^1.4 all full^0.1
     */

    // author
    assertQ(req("q", "Barabási"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='5979890']"
    ); 
    // title
    assertQ(req("q", "bibcode"),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='2']"
    );
    // abstract
    assertQ(req("q", "DPi1b", "fl", "title,recid"), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='9218605']");
    // keyword
    assertQ(req("q", "KERNEL FUNCTIONS"),
        "//doc/int[@name='recid'][.='3813361']",
    "//*[@numFound='1']");
    // keyword norm
    assertQ(req("q", "UNWISE"),
        "//doc/int[@name='recid'][.='9218920']",
    "//*[@numFound='1']");
    // affiliations are not copied to all
    assertQ(req("q", "kavli"), 
        //"//doc/int[@name='recid'][.='9218511']"
        "//*[@numFound='0']"
    ); 
    // alternate title copied to all
    assertQ(req("q", "Probing red giants"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='9218605']"
    ); 
    // full
    //dumpDoc(null, "recid", "full");
    assertQ(req("q", "hashimoto"), 
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='3673891']"
    );


    /*
     * citations()/references() queries (use special dummy records, field 999i)
     */
    assertQ(req("q", "recid:12"), "//*[@numFound='1']");
    assertQ(req("q", "recid:16"), "//*[@numFound='1']");
    assertQ(req("q", "id:12"), "//*[@numFound='1']");
    assertQ(req("q", "id:16"), "//*[@numFound='1']");
    assertQ(req("q", "citations(id:12)"), "//*[@numFound='3']");
    assertQ(req("q", "citations(title:test)"), "//*[@numFound='4']");


    assertQ(req("q", "references(id:12)"), "//*[@numFound='0']");
    assertQ(req("q", "references(id:13)"), "//*[@numFound='2']");
    assertQ(req("q", "references(id:12 OR id:13)"), "//*[@numFound='2']");
    assertQ(req("q", "references(title:test)"), "//*[@numFound='5']");
    assertQ(req("q", "references(recid:12)"), "//*[@numFound='0']");
    assertQ(req("q", "references(recid:13)"), "//*[@numFound='2']");
    assertQ(req("q", "references(recid:12 OR recid:13)"), "//*[@numFound='2']");
    assertQ(req("q", "references(title:test)"), "//*[@numFound='5']");


    /*
     * read_count (float type)
     */
    //dumpDoc(null, "recid", "bibcode", "read_count", "cite_read_boost");
    assertQ(req("q", "read_count:[0.0 TO 19.0]", "fl", "recid,bibcode,title,read_count"), 
        "//doc/str[@name='bibcode'][.='1991ApJ...371..665R']",
        "//doc/str[@name='bibcode'][.='1976AJ.....81...67S']",
        "//doc/str[@name='bibcode'][.='2009arXiv0909.1287I']",
        "//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']",
        "//*[@numFound='4']");
    assertQ(req("q", "read_count:19.0"), 
        "//doc/str[@name='bibcode'][.='1991ApJ...371..665R']",
        "//*[@numFound='1']");
    assertQ(req("q", "read_count:15.0"), 
        "//doc/str[@name='bibcode'][.='1976AJ.....81...67S']",
        "//*[@numFound='1']");
    assertQ(req("q", "read_count:1.0"), 
        "//doc/str[@name='bibcode'][.='2009arXiv0909.1287I']",
        "//*[@numFound='1']");
    assertQ(req("q", "read_count:0.0"), 
        "//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']",
        "//*[@numFound='1']");
    

    /*
     * cite_read_boost
     */
		//dumpDoc(null, "recid", "read_count", "cite_read_boost");
    assertQ(req("q", "cite_read_boost:[0.0 TO 1.0]"), 
        "//doc/str[@name='bibcode'][.='1991ApJ...371..665R']",
        "//doc/str[@name='bibcode'][.='1976AJ.....81...67S']",
        "//doc/str[@name='bibcode'][.='2009arXiv0909.1287I']",
        "//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']",
        "//*[@numFound='4']");
    assertQ(req("q", "cite_read_boost:0.4649"), 
        "//doc/str[@name='bibcode'][.='1991ApJ...371..665R']",
        "//*[@numFound='1']");
    assertQ(req("q", "cite_read_boost:0.373"), 
        "//doc/str[@name='bibcode'][.='1976AJ.....81...67S']",
        "//*[@numFound='1']");
    assertQ(req("q", "cite_read_boost:0.2416"), 
        "//doc/str[@name='bibcode'][.='2009arXiv0909.1287I']",
        "//*[@numFound='1']");
    assertQ(req("q", "cite_read_boost:0.4104"), 
        "//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']",
        "//*[@numFound='1']");
    
    assertQ(req("q", "cite_read_boost:[0.1 TO 0.373]"), 
        "//doc/str[@name='bibcode'][.='1976AJ.....81...67S']",
        "//doc/str[@name='bibcode'][.='2009arXiv0909.1287I']",
        "//*[@numFound='2']");
    assertQ(req("q", "cite_read_boost:[0.4103 TO 0.410399999999]"), 
        "//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']",
        "//*[@numFound='1']");
    assertQ(req("q", "cite_read_boost:[0.41039999 TO 0.4648999999]"), 
        "//doc/str[@name='bibcode'][.='1987PhRvD..36..277B']",
        "//doc/str[@name='bibcode'][.='1991ApJ...371..665R']",
        "//*[@numFound='2']");
    
    /*
     * pubdate - 17/12/2012 changed to be the date type
     * 
     * we have records with these dates:
     *    20: 1976-00-00
     *    21: 1976-01-00
     *    22: 1976-01-02
     *    23: 1976-02-01
     *    24: 1976-01-02
     *    25: 1976-31-12 // will get indexed as 1976-02-01T00:30:00Z (probably because 00-00 fals into 1/1 + 30MIN)
     *    26: 1977-00-00
     *    27: 1977-01-01
     */
    //setDebug(true);
    //dumpDoc(null, "bibcode", "pubdate", "date");

    assertQ(req("q", "title:dateparsingtest"), "//*[@numFound='8']");
    assertQ(req("q", "pubdate:1976", "fl", "title,pubdate,date,recid"), 
        "//*[@numFound='6']",
        "//doc/int[@name='recid'][.='20']",
        "//doc/int[@name='recid'][.='21']",
        "//doc/int[@name='recid'][.='22']",
        "//doc/int[@name='recid'][.='23']",
        "//doc/int[@name='recid'][.='24']",
        "//doc/int[@name='recid'][.='25']"
    );
    assertQ(req("q", "pubdate:1976-00"),  // 00 gets automatically translated into 1976-01-01 (includes 1976-01-00)
        "//*[@numFound='4']",
        "//doc/int[@name='recid'][.='20']",
        "//doc/int[@name='recid'][.='21']",
        "//doc/int[@name='recid'][.='22']",
        "//doc/int[@name='recid'][.='24']"
    );
    assertQ(req("q", "pubdate:1976-00-00"), // gets automatically translated into 01-01
        "//*[@numFound='3']",
        "//doc/int[@name='recid'][.='20']",
        "//doc/int[@name='recid'][.='21']",
        "//doc/int[@name='recid'][.='24']"
    );

    assertQ(req("q", "pubdate:1976-00-32"), "//*[@numFound='0']"); // nonsense, but should be parsed properly into 01-01
    assertQ(req("q", "pubdate:1976-01-00"), 
        "//*[@numFound='3']",
        "//doc/int[@name='recid'][.='20']",
        "//doc/int[@name='recid'][.='21']",
    "//doc/int[@name='recid'][.='24']");

    assertQ(req("q", "pubdate:1976-01-01"), 
    "//*[@numFound='0']");
    assertQ(req("q", "pubdate:1976-01-02"), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='22']");
    assertQ(req("q", "pubdate:1976-02-00"), 
        "//*[@numFound='2']",
        "//doc/int[@name='recid'][.='23']",
    "//doc/int[@name='recid'][.='25']");
    assertQ(req("q", "pubdate:1976-02-01"), 
        "//*[@numFound='2']",
        "//doc/int[@name='recid'][.='23']",
    "//doc/int[@name='recid'][.='25']");
    assertQ(req("q", "pubdate:1977-00-00"), 
        "//*[@numFound='2']",
        "//doc/int[@name='recid'][.='26']",
    "//doc/int[@name='recid'][.='27']");
    assertQ(req("q", "pubdate:1977-01-01"), 
        "//*[@numFound='1']",
    "//doc/int[@name='recid'][.='27']");

    // test the right date is picked from the record
    assertQ(req("q", "bibcode:2012AJ....144..19XX"), 
        "//*[@numFound='1']"
        // when run with -DstoreAll=true
        //"//doc/str[@name='pubdate'][.='2012-12-00']"
        //"//doc/str[@name='date'][.='2012-12-01T00:00:00Z']"
    );
    assertQ(req("q", "pubdate:2012-12-00"), 
        "//*[@numFound='1']",
        "//doc/str[@name='bibcode'][.='2012AJ....144..19XX']"
    );



    /*
     * links_data (856 data is generated and stored as JSON for display purposes)
     * ids_data (035 data is generated and stored as JSON for display purposes)
     */
    assertQ(req("q", "bibcode:2012ApJ...760..135R"), "//doc/arr[@name='links_data']/str[contains(text(),'MAST')]");
    assertQ(req("q", "bibcode:2012ApJ...760..135R"), "//doc/arr[@name='ids_data']/str[contains(text(),'\"alternate_bibcode\":\"2012arXiv1210.5163R\"')]");



    /*
     * 2nd order queries
     */
    
     // what other papers we cite
    assertQ(req("q", "references(*:*)"), 
    		"//*[@numFound='5']");
    assertQ(req("q", "references(id:10)"), 
				"//*[@numFound='2']",
				"//doc/int[@name='recid'][.='11']",
				"//doc/int[@name='recid'][.='12']");
    
    // who cites us
    assertQ(req("q", "citations(*:*)"), 
    		"//*[@numFound='4']");
    assertQ(req("q", "citations(id:10)"), 
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='11']");
    
    // similar to references(X)
    assertQ(req("q", "useful(*:*)"), 
    		"//*[@numFound='5']");
    assertQ(req("q", "useful(id:10)"), 
				"//*[@numFound='2']",
				"//doc/int[@name='recid'][.='11']",
				"//doc/int[@name='recid'][.='12']");
    
    
    // this is similar to citations(x)
    assertQ(req("q", "reviews(*:*)"), 
    		"//*[@numFound='4']");
    assertQ(req("q", "reviews(id:10)"), 
				"//*[@numFound='1']",
				"//doc/int[@name='recid'][.='11']");
    
    // cut only the first n results
    assertQ(req("q", "topn(2, reviews(*:*))"), 
		"//*[@numFound='2']");

  }

  
  public static class MongoMockDataSource extends AdsDataSource {
  	
  	@Override
    protected void initMongo(Context context, Properties initProps) {
  		return;
  	}
  	
  	
  	@Override
  	public void close() {
  		return; // do nothing
  	}
  	
  	@Override
  	protected void populateMongoCache(List<String> bibcodes) {
  		
  		assert bibcodes.contains("1987PhRvD..36..277B");
  		assert bibcodes.contains("1991ApJ...371..665R");
  		assert bibcodes.contains("1976AJ.....81...67S");
  		assert bibcodes.contains("2009arXiv0909.1287I");
  		
  		HashMap<String, Object> row = new HashMap<String, Object>();
  		row.put("grant", Arrays.asList("NSF-AST 0618398"));
  		row.put("grant_agencies", Arrays.asList("NSF-AST"));
  		row.put("grant_ids", Arrays.asList("0618398"));
  		row.put("read_count", Arrays.asList(0.0f));
  		row.put("cite_read_boost", Arrays.asList(0.4104f));
  		mongoCache.put("1987PhRvD..36..277B", row);
  		
  		row = new HashMap<String, Object>();
  		row.put("full", Arrays.asList("Some fulltext Hashimoto"));
  		row.put("read_count", Arrays.asList(19.0f));
  		row.put("cite_read_boost", Arrays.asList(0.4649f));
  		mongoCache.put("1991ApJ...371..665R", row);
  		
  		
  		row = new HashMap<String, Object>();
  		row.put("read_count", Arrays.asList(15.0f));
  		row.put("cite_read_boost", Arrays.asList(0.373f));
  		mongoCache.put("1976AJ.....81...67S", row);
  		
  		row = new HashMap<String, Object>();
  		row.put("read_count", Arrays.asList(1.0f));
  		row.put("cite_read_boost", Arrays.asList(0.2416f));
  		mongoCache.put("2009arXiv0909.1287I", row);
  		
  		
  		
  	}
  }
  
  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsAllFields.class);
  }
}
