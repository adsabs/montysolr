package org.apache.solr.analysis.author;

import java.io.File;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.solr.analysis.WriteableExplicitSynonymMap;
import org.apache.solr.analysis.WriteableSynonymMap;

public class TestAuthorSynonymFilterFactory extends LuceneTestCase {

	private File tmpFile;

	@Override
	public void setUp() throws Exception {
		tmpFile = File.createTempFile("montySolr-unittest", null);
		super.setUp();
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		FileUtils.deleteQuietly(tmpFile);
	}

	public void testParseRules() {
		List<String> rules = new ArrayList<String>();
		rules.add("MILLER, WILLIAM=>MILLER, B;MILLER, BILL;MILLER,;MILLER, BILL\\b.*");
		rules.add("MILLER, BILL=>MILLER, WILLIAM;MILLER, WILLIAM\\b.*;MILLER,;MILLER, W");

		for (int i=0; i<rules.size();i++) {
			rules.set(i, rules.get(i).replace(",", "\\,").replace(" ", "\\ ").replace(";", ","));
		}

		WriteableSynonymMap synMap = new WriteableExplicitSynonymMap();
		synMap.setOutput(tmpFile.getAbsolutePath());

		synMap.populateMap(rules);
		Set<String> expected = new HashSet<String>();
		expected.add("MILLER, B");
		expected.add("MILLER, BILL");
		expected.add("MILLER,");
		expected.add("MILLER, BILL\\b.*");
		assertEquals(expected, synMap.get("MILLER, WILLIAM"));
		expected = new HashSet<String>();
		expected.add("MILLER, WILLIAM");
		expected.add("MILLER, WILLIAM\\b.*");
		expected.add("MILLER,");
		expected.add("MILLER, W");
		assertEquals(expected, synMap.get("MILLER, BILL"));
	}

}
