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
	private SemanticTagger tagger;
	
	public void setUp() throws Exception {
		super.setUp();
		
		addToSysPath(getMontySolrHome() + "/contrib/newseman/src/python");
		addTargetsToHandler("monty_newseman.targets", "monty_newseman.tests.targets");
		
		this.url = "sqlite:///:memory:"; 
		
		tagger = new SemanticTagger(this.url);
		
		// fill the db with test data
		PythonMessage message = MontySolrVM.INSTANCE.createMessage(
				"fill_newseman_dictionary")
				.setParam("url", tagger.getName());
		MontySolrVM.INSTANCE.sendMessage(message);
	}

	/**
	   * Must return a fully qualified name of the python module to load, eg:
	   * "montysolr.tests.basic"
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
		ArrayList<List<String>> out = new ArrayList<List<String>>();
		
		List<String> header = Arrays.asList(results[0]);
		
		int idx_token = header.indexOf("token");
		int idx_sem = header.indexOf("sem");
		int idx_grp_sem = header.indexOf("multi-sem");
		int idx_grp = header.indexOf("multi-synonyms");
		int idx_syn = header.indexOf("synonyms");
		
		ArrayList<String> tok = new ArrayList<String>();
		ArrayList<String> sem = new ArrayList<String>();
		ArrayList<String> grp = new ArrayList<String>();
		ArrayList<String> grs = new ArrayList<String>();
		ArrayList<String> syn = new ArrayList<String>();
		
		for (String[] row: results) {
			if (idx_token > 0 && row.length > idx_token)
				tok.add(row[idx_token]);
			if (idx_sem > 0 && row.length > idx_sem)
				sem.add(row[idx_sem]);
			if (idx_grp > 0 && row.length > idx_grp)
				grp.add(row[idx_grp]);
			if (idx_grp_sem > 0 && row.length > idx_grp_sem)
				grs.add(row[idx_grp_sem]);
			if (idx_syn > 0 && row.length > idx_syn)
				syn.add(row[idx_syn]);
		}
		
		out.add(tok);
		out.add(sem);
		out.add(grp);
		out.add(grs);
		out.add(syn);
		
		return out;
		
	}

	public void testTranslation_add_purge()  {
		
		// configure for fuzzy parsing
		tagger.configureTagger("czech", 2, "add", "purge");
		
		String text = "velká světová revoluce byla velká říjnová revoluce bez velké extra říjnové revoluce";
	    
		String[] words = text.split(" ");
		
		String[][] tokens = createTokens(words);
		
		String[][] results = tagger.translateTokens(tokens);

		List<List<String>> data = getResults(results);
		
		List<String> tok = data.get(0);
		List<String> sms = data.get(1);
		List<String> grp = data.get(2);
		List<String> grs = data.get(3);
		List<String> syn = data.get(4);

		assertTrue(Collections.frequency(sms, "XXX") == 1);
		assertTrue(Collections.frequency(grs, "XXX") == 1);
		assertTrue(Collections.frequency(sms, "r2") == 1);
		assertTrue(Collections.frequency(grp, "velk říjn revol") == 1);
		assertTrue(Collections.frequency(syn, "velká říjnová revoluce") == 1);
	}

	public void testTranslation_rewrite_purge()  {
		
		// configure for fuzzy parsing
		tagger.configureTagger("czech", 2, "rewrite", "purge");
		
		String text = "velká světová revoluce byla velká říjnová revoluce bez velké extra říjnové revoluce";
	    
		String[] words = text.split(" ");
		
		String[][] tokens = createTokens(words);
		
		String[][] results = tagger.translateTokens(tokens);

		List<List<String>> data = getResults(results);
		
		List<String> tok = data.get(0);
		List<String> sms = data.get(1);
		List<String> grp = data.get(2);
		List<String> grs = data.get(3);
		List<String> syn = data.get(4);

		assertTrue(Collections.frequency(sms, "XXX") == 1);
		assertTrue(Collections.frequency(grs, "XXX") == 1);
		assertTrue(Collections.frequency(sms, "r2") == 1);
		assertTrue(Collections.frequency(grp, "velk říjn revol") == 0);
		assertTrue(Collections.frequency(syn, "velká říjnová revoluce") == 0);
		assertTrue(Collections.frequency(tok, "velká říjnová revoluce") == 1);
	}
	
}
