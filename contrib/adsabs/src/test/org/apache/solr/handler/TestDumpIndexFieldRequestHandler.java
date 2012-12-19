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

package org.apache.solr.handler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.adsabs.solr.AdsConfig.F;
import org.apache.lucene.search.BooleanQuery;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class TestDumpIndexFieldRequestHandler extends MontySolrQueryTestCase {


  private File generatedTransliterations;



  @Override
  public String getSchemaFile() {
    makeResourcesVisible(this.solrConfig.getResourceLoader(),
        new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
    });

    /*
     * For purposes of the test, we make a copy of the schema.xml,
     */

    String configFile = MontySolrSetup.getMontySolrHome()
    + "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";

    File newSchema;
    try {

      generatedTransliterations = createTempFile(formatSynonyms(new String[]{
          "ADAMCHuk, m => ADAMČuk, m",
          "ADAMCuk, m => ADAMČuk, m",
      }
      ));
      newSchema = duplicateModify(new File(configFile), 
          "outFile=\"author_generated.translit\"", "outFile=\"" + generatedTransliterations.getAbsolutePath() + "\"",
          "synonyms=\"author_generated.translit\"", "synonyms=\"" + generatedTransliterations.getAbsolutePath() + "\"");
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException(e.getMessage());
    }

    return newSchema.getAbsolutePath();
  }

  public String getSolrConfigFile() {
    return MontySolrSetup.getMontySolrHome()
    + "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
  }


  public void test() throws Exception {
    
    // first, let's see if the tokenizer correctly picked up the rules from the existing transliteration file
    assertQueryEquals(req("qt", "aqp", "q", "author:\"adamchuk, m\""), 
        "author:adamčuk, m author:adamčuk, m* author:adamčuk, " +
        "author:adamchuk, m author:adamchuk, m* author:adamchuk, " +
        "author:adamcuk, m author:adamcuk, m* author:adamcuk,", BooleanQuery.class);

    // now index some data
    assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk,"));
    assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M."));
    assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Marel"));
    assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja"));
    assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja Karel"));
    assertU(commit());
    
    // check the file still contains the original data
    checkFile(formatSynonyms(new String[]{
        "ADAMCHuk, m => ADAMČuk, m",
        "ADAMCuk, m => ADAMČuk, m",
    }));
    
    SolrCore core = h.getCore();
    DumpIndexField handler = (DumpIndexField) h.getCore().getRequestHandler("/dump-index");
    SolrQueryRequest req = req("command", "dump",
        "sourceField", "author", "targetField", "author_collector");
    
    SolrQueryResponse rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);
    
    
    req = req("command", "start");
    rsp = new SolrQueryResponse();
    core.execute(handler, req, rsp);

    while (handler.isBusy()) {
      assertQ(req("qt", "/dump-index", "command", "info"), 
          "//str[@name='status'][.='busy']"
          );
      Thread.sleep(300);
    }
    
    checkFile(formatSynonyms(new String[]{
        "Adamcuk, Molja Karel=>Adamčuk, Molja Karel",
        "Adamcuk, Molja K=>Adamčuk, Molja K",
        //"Adamcuk, M Karel=>Adamčuk, M Karel",
        "Adamcuk, M K=>Adamčuk, M K",
        "Adamcuk, M=>Adamčuk, M",
        "Adamchuk, Molja Karel=>Adamčuk, Molja Karel",
        "Adamchuk, Molja K=>Adamčuk, Molja K",
        //"Adamczuk, M Karel=>Adamčuk, M Karel",
        "Adamchuk, M K =>Adamčuk, M K",
        "Adamchuk, M =>Adamčuk, M",
    }));
    
  }
  
  private void checkFile(String... expected) throws IOException {
    List<String> lines = h.getCore().getResourceLoader().getLines(generatedTransliterations.getAbsolutePath());
    for (String t: expected) {
      if (t.substring(0,1).equals("!")) {
        assertFalse("Present: " + t, lines.contains(t.substring(1)));
      }
      else {
        assertTrue("Missing: " + t, lines.contains(t));
      }
    }
  }
  
  

  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestDumpIndexFieldRequestHandler.class);
  }
}
