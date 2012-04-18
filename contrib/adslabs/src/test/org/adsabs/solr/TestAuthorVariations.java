package org.adsabs.solr;

import java.util.HashMap;
import java.util.HashSet;

import junit.framework.TestCase;

public class TestAuthorVariations extends TestCase {
	
	public void testGenerateQueryVariations1() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "HECTOR");
		input.put("last", "GOMEZ");
		input.put("middle", "Q");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, HECTOR Q.*");
		expected.add("GOMEZ, HECTOR");
		expected.add("GOMEZ, H Q.*");
		expected.add("GOMEZ, H");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateQueryVariations(input);
		assertEquals(expected, actual);
	}
	public void testGenerateQueryVariations2() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "HECTOR");
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, HECTOR\\b.*");
		expected.add("GOMEZ, H\\b.*");
		expected.add("GOMEZ, H");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateQueryVariations(input);
		assertEquals(expected, actual);
	}
	public void testGenerateQueryVariations3() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H.*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateQueryVariations(input);
		assertEquals(expected, actual);
	}
	public void testGenerateQueryVariations4() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		input.put("middle", "Q");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H\\w* Q.*");
		expected.add("GOMEZ, H\\w*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateQueryVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testGenerateQueryVariations5() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		input.put("middle", "QUINTERO");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H\\w* QUINTERO\\b.*");
		expected.add("GOMEZ, H\\w* Q.*");
		expected.add("GOMEZ, H\\w*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateQueryVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testGenerateQueryVariations6() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ,.*");
		HashSet<String> actual = AuthorVariations.generateQueryVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testGenerateSynonymVariations1() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "HECTOR");
		input.put("last", "GOMEZ");
		input.put("middle", "Q");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, HECTOR Q.*");
		expected.add("GOMEZ, HECTOR");
		expected.add("GOMEZ, H Q.*");
		expected.add("GOMEZ, H");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateSynonymVariations(input);
		assertEquals(expected, actual);
	}
	public void testGenerateSynonymVariations2() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "HECTOR");
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, HECTOR\\b.*");
		expected.add("GOMEZ, H");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateSynonymVariations(input);
		assertEquals(expected, actual);
	}
	public void testGenerateSynonymVariations3() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H.*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateSynonymVariations(input);
		assertEquals(expected, actual);
	}
	public void testGenerateSynonymVariations4() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		input.put("middle", "Q");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H\\w* Q.*");
		expected.add("GOMEZ, H\\w*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateSynonymVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testGenerateSynonymVariations5() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		input.put("middle", "QUINTERO");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H\\w* QUINTERO\\b.*");
		expected.add("GOMEZ, H\\w*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorVariations.generateSynonymVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testGenerateSynonymVariations6() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ,.*");
		HashSet<String> actual = AuthorVariations.generateSynonymVariations(input);
		assertEquals(expected, actual);
	}
//	public void testParseAuthor() {
//		HashMap<String,String> expected = new HashMap<String,String>();
//		expected.put("last", "Miller");
//		expected.put("first", "Janice");
//		expected.put("middle", "G");
//		HashMap<String,String> actual = AuthorVariations.parseAuthor("Miller, Janice G");
//		assertEquals(expected, actual);
//	}
}
