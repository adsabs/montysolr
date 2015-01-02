package org.apache.solr.handler.batch;

import org.adsabs.solr.AdsConfig.F;
import org.apache.solr.request.SolrQueryRequest;

public class TestBatchProviderDumpAuthorNames extends BatchProviderTest {


	public void test() throws Exception {

    // index some data
    assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk,"));
    assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M."));
    assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Marel"));
    assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja"));
    assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja Karel"));
    assertU(commit());
    
    assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja K"));
    assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M K"));
    assertU(adoc(F.ID, "9", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel Molja"));
    assertU(adoc(F.ID, "10", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel M"));
    assertU(adoc(F.ID, "11", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, K Molja"));
    assertU(adoc(F.ID, "12", F.BIBCODE, "xxxxxxxxxxx12", F.AUTHOR, "ǎguşan, Adrian, , Dr"));
    assertU(adoc(F.ID, "13", F.BIBCODE, "xxxxxxxxxxx13", F.AUTHOR, "")); // no author
    assertU(adoc(F.ID, "14", F.BIBCODE, "xxxxxxxxxxx14", F.AUTHOR, "á"));
    assertU(adoc(F.ID, "15", F.BIBCODE, "xxxxxxxxxxx15", F.AUTHOR, "sárname \\, name ,,,,")); // try to confuse it
    assertU(adoc(F.ID, "16", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk"));
    assertU(adoc(F.ID, "17", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk  "));
    assertU(commit());
		
		
		BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
		String tmpDir = System.getProperty("java.io.tmpdir");
		BatchProviderI provider = new BatchProviderDumpAuthorNames();
		
		SolrQueryRequest req = req("jobid", "00000", "#workdir", tmpDir, "sourceField", "author", "analyzerField", "author_collector");
		provider.run(req, queue);
		req.close();


    
		checkFile(tmpDir + "/00000", 
				new String[]{
        "Adamcuk, Molja Karel=>Adamčuk, Molja Karel",
        "Adamcuk, Molja K=>Adamčuk, Molja K",
        //"Adamcuk, M Karel=>Adamčuk, M Karel",
        "Adamcuk, M K=>Adamčuk, M K",
        "Adamcuk, M=>Adamčuk, M",
        "Adamchuk, Molja Karel=>Adamčuk, Molja Karel",
        "Adamchuk, Molja K=>Adamčuk, Molja K",
        //"Adamczuk, M Karel=>Adamčuk, M Karel",
        "Adamchuk, M K=>Adamčuk, M K",
        "Adamchuk, M=>Adamčuk, M",
        "Adamcuk, Karel Molja=>Adamčuk, Karel Molja",
        "Adamcuk, Karel M=>Adamčuk, Karel M",
        "!ahguşan, Adrian, , Dr=>ǎguşan, Adrian, , Dr",
        "!agusan, Adrian, ,=>ǎguşan, Adrian, ,",
        "!ahguşan, A ,=>ǎguşan, A ,",
        "!agusan, A , D=>ǎguşan, A , D",
        "agusan, Adrian Dr=>ǎguşan, Adrian Dr",
        "agusan, A=>ǎguşan, A",
        "ahguşan, Adrian D=>ǎguşan, Adrian D",
        "ahguşan, Adrian Dr=>ǎguşan, Adrian Dr",
        "ahguşan, A D=>ǎguşan, A D",
        "!a=>á,"
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
