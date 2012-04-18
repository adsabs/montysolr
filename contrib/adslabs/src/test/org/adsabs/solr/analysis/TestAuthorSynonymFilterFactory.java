package org.adsabs.solr.analysis;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class TestAuthorSynonymFilterFactory {

	@Test
	public void testParseRules() {
		List<String> rules = new ArrayList<String>();
		rules.add("MILLER, WILLIAM => MILLER, B; MILLER, BILL; MILLER,; MILLER, BILL\\b.*");
		rules.add("MILLER, BILL => MILLER, WILLIAM; MILLER, WILLIAM\\b.*; MILLER,; MILLER, W");
	    AuthorSynonymMap synMap = new AuthorSynonymMap();
	    AuthorSynonymFilterFactory asff = new AuthorSynonymFilterFactory();
	    asff.parseRules(rules, synMap);
	    List<String> expected = new ArrayList<String>();
	    expected.add("MILLER, B");
	    expected.add("MILLER, BILL");
	    expected.add("MILLER,");
	    expected.add("MILLER, BILL\\b.*");
	    assertEquals(synMap.get("MILLER, WILLIAM"), expected);
	    expected = new ArrayList<String>();
	    expected.add("MILLER, WILLIAM");
	    expected.add("MILLER, WILLIAM\\b.*");
	    expected.add("MILLER,");
	    expected.add("MILLER, W");
	    assertEquals(synMap.get("MILLER, BILL"), expected);
	}

}
