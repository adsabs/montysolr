package newseman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.util.MontySolrAbstractTestCase;

public class TestSemanticTagger extends MontySolrAbstractTestCase {
	
	private String url;
	
	public void setUp() throws Exception {
		super.setUp();
		this.url = "sqlite:///:memory:";  
	}

	/**
	   * Must return a fully qualified name of the python module to load, eg:
	   * 
	   * "montysolr.tests.basic"
	   * 
	   */
	@Override
	public String getModuleName() {
		return "montysolr.java_bridge.SimpleBridge";
	}

	@Override
	public String getSchemaFile() {
		return "schema-minimal.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return "solrconfig-diagnostic-test.xml";
	}
	
	private String[][] createTokens(String[] words) {
		
		String[][] tokens = new String[(words.length + 1)][];
		tokens[0] = new String[]{"token", "id"};
		int i = 1;
		for (String word: words) {
			tokens[i] = new String[]{word, Integer.toString(i)};
			i++;
		}
		return tokens;
	}
	
	private List<List<String>> getResults(String[][] results) {
		ArrayList<List<String>> out = new ArrayList<List<String>>(3);
		
		List<String> header = Arrays.asList(results[0]);
		int idx_sem = header.indexOf("sem");
		int idx_grp_sem = header.indexOf("multi-sem");
		int idx_grp = header.indexOf("multi-token");
		
		ArrayList<String> sem = new ArrayList<String>();
		ArrayList<String> grp = new ArrayList<String>();
		ArrayList<String> grs = new ArrayList<String>();
		
		for (String[] row: results) {
			sem.add(row[idx_sem]);
			grp.add(row[idx_grp]);
			grs.add(row[idx_grp_sem]);
		}
		
		out.add(0, sem);
		out.add(1, grp);
		out.add(2, grs);
		
		return out;
		
	}

	public void testFuzzyTranslation() throws IOException, InterruptedException {
		
		
		MontySolrVM.INSTANCE.evalCommand("import sys;sys.path.append(\'" 
				+ this.getMontySolrHome() + "/contrib/newseman/src/python\')");
		
		
		SemanticTagger tagger = new SemanticTagger(this.url);
		
		// fill the db with test data
		PythonMessage message = MontySolrVM.INSTANCE.createMessage(
				"fill_newseman_dictionary")
				.setParam("url", tagger.getName());
		
		// configure for fuzzy parsing
		message = MontySolrVM.INSTANCE.createMessage("configure_seman")
	        .setParam("url", tagger.getName())
	        .setParam("language", "czech")
	        .setParam("max_distance", 1)
	        .setParam("grp_action", "add")
	        .setParam("grp_cleaning", "purge");
		
		String text = "velká světová revoluce byla velká říjnová revoluce protože s velkou říjnovou revolucí " +
	        "a bez velké říjnové revoluce a ještě velká říjnová revoluce socialistická komunistická " +
	        "s velkou a říjnovou revolucí";
	    
		String[] words = text.split(" ");
		
		String[][] tokens = createTokens(words);
		
		String[][] results = tagger.translateTokens(tokens);

		List<List<String>> data = getResults(results);
		
		List<String> sms = data.get(0);
		List<String> grp = data.get(1);
		List<String> grs = data.get(2);

		assertTrue(Collections.frequency(sms, "XXX") == 4);
		assertTrue(Collections.frequency(grs, "XXX") == 1);
		assertTrue(Collections.frequency(sms, "r2") == 1);
		assertTrue(Collections.frequency(grp, "velk říjn revol") == 1);
	}

}
