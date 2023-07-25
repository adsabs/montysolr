package org.apache.solr.handler.batch;

import java.io.File;

import org.adsabs.solr.AdsConfig.F;
import org.apache.solr.analysis.author.AuthorUtils;
import org.apache.solr.request.SolrQueryRequest;

public class TestBatchProviderDumpAuthorNames extends BatchProviderTest {


	public void test() throws Exception {

    // index some data
    assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk,"));
    assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M."));
    assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk,       Marel"));
    assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja"));
    assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja     Karel"));
    assertU(commit());
    
    assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja K"));
    assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M K"));
    assertU(adoc(F.ID, "9", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel     Molja"));
    assertU(adoc(F.ID, "10", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel M"));
    assertU(adoc(F.ID, "11", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, K Molja"));
    assertU(adoc(F.ID, "12", F.BIBCODE, "xxxxxxxxxxx12", F.AUTHOR, "ǎguşan, Adrian, , Dr"));
    assertU(adoc(F.ID, "13", F.BIBCODE, "xxxxxxxxxxx13", F.AUTHOR, "")); // no author
    assertU(adoc(F.ID, "14", F.BIBCODE, "xxxxxxxxxxx14", F.AUTHOR, "á"));
    assertU(adoc(F.ID, "15", F.BIBCODE, "xxxxxxxxxxx15", F.AUTHOR, "sárname \\, name ,,,,")); // try to confuse it
    assertU(adoc(F.ID, "16", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk"));
    assertU(adoc(F.ID, "17", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk  "));
    assertU(commit());
    
    assertU(adoc(F.ID, "18", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "González-Alfonso, E"));
    assertU(adoc(F.ID, "19", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Sil'chenko, E"));
    assertU(adoc(F.ID, "20", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "SAV'E, E"));
    assertU(adoc(F.ID, "21", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Wyrzykowski, Ł"));
    assertU(adoc(F.ID, "22", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Peißker, L"));
    assertU(commit());
		
		
		BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
		String tmpDir = new File("./temp").getAbsolutePath();
		BatchProviderI provider = new BatchProviderDumpAuthorNames();
		
		SolrQueryRequest req = req("jobid", "00000", "#workdir", tmpDir, "sourceField", "author", "analyzerField", "author_collector");
		provider.run(req, queue);
		req.close();


    
		checkFile(tmpDir + "/00000", 
				new String[]{
        "adamcuk, molja karel=>adamčuk, molja karel",
        "adamcuk, molja k=>adamčuk, molja k",
        //"Adamcuk, M Karel=>Adamčuk, M Karel",
        "adamcuk, m k=>adamčuk, m k",
        "adamcuk, m=>adamčuk, m",
        //"Adamczuk, M Karel=>Adamčuk, M Karel",
        "adamcuk, karel molja=>adamčuk, karel molja",
        "adamcuk, karel m=>adamčuk, karel m",
        "!ahguşan, adrian, , dr=>ǎguşan, adrian, , dr",
        "!agusan, adrian, ,=>ǎguşan, adrian, ,",
        "!ahguşan, a ,=>ǎguşan, a ,",
        "!agusan, a , d=>ǎguşan, a , d",
        "agusan, adrian dr=>ǎguşan, adrian dr",
        "agusan, a=>ǎguşan, a",
        "!a=>á,",
        "gonzalez alfonso,=>gonzález alfonso,",
        "gonzalez alfonso, e=>gonzález alfonso, e",
        "savye, e=>sav e, e",
        "silchenko,=>sil chenko,", // this is cleaned up "sil'chenko"
        "!sil'chenko,=>sil chenko,",
        "wyrzykowski, l=>wyrzykowski, ł",
        "peissker, l=>peißker, l",
				}
		);
	}
	
	public static String[] formatSynonyms(String[] strings) {
    String[] newLines = new String[strings.length];
    int nl = 0;
    for (String line : strings) {
      StringBuilder out = new StringBuilder();
      String[] kv = line.split("=>");
      for (int i=0;i<kv.length;i++) {
        if (i>0) out.append("=>");
        String[] names = kv[i].split(";");
        for (int j=0;j<names.length;j++) {
          if (j>0) out.append(",");
          out.append(names[j].trim().replace(" ", "\\ ").replace(",", "\\,"));
        }
      }
      newLines[nl++] = out.toString();
    }
    return newLines;
  }
	
}
