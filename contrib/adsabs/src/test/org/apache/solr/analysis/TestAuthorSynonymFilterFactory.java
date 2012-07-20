package org.apache.solr.analysis;

import static org.junit.Assert.*;

import java.util.*;

import org.apache.solr.analysis.WriteableSynonymMap;
import org.junit.Test;

public class TestAuthorSynonymFilterFactory {

	@Test
	public void testParseRules() {
		List<String> rules = new ArrayList<String>();
		rules.add("MILLER, WILLIAM=>MILLER, B;MILLER, BILL;MILLER,;MILLER, BILL\\b.*");
		rules.add("MILLER, BILL=>MILLER, WILLIAM;MILLER, WILLIAM\\b.*;MILLER,;MILLER, W");
		
		for (int i=0; i<rules.size();i++) {
			rules.set(i, rules.get(i).replace(",", "\\,").replace(" ", "\\ ").replace(";", ","));
		}
		
	    WriteableSynonymMap synMap = new WriteableSynonymMap(null);
	    synMap.parseRules(rules);
	    List<String> expected = new ArrayList<String>();
	    expected.add("MILLER, B");
	    expected.add("MILLER, BILL");
	    expected.add("MILLER,");
	    expected.add("MILLER, BILL\\b.*");
	    assertEquals(expected, synMap.get("MILLER, WILLIAM"));
	    expected = new ArrayList<String>();
	    expected.add("MILLER, WILLIAM");
	    expected.add("MILLER, WILLIAM\\b.*");
	    expected.add("MILLER,");
	    expected.add("MILLER, W");
	    assertEquals(expected, synMap.get("MILLER, BILL"));
	}

}
