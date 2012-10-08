package org.adsabs.solr;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.solr.analysis.author.AuthorQueryVariations;

import junit.framework.TestCase;

public class TestAuthorVariations extends TestCase {
	
	public void xtestgetNameVariations() {
		HashSet<String> name = AuthorQueryVariations.getQueryVariationsInclRegex("Hector, Gomez Q");
		for (String n: name) {
			System.out.println(n);
		}
		
		System.out.println("-------------");
		name = AuthorQueryVariations.getQueryVariations("Hector, Gomez Q");
		for (String n: name) {
			System.out.println(n);
		}
		
		System.out.println("-------------");
		
		name = AuthorQueryVariations.getQueryVariationsInclRegex("H quintero gomez");
		for (String n: name) {
			System.out.println(n);
		}
		
		System.out.println("-------------");
		name = AuthorQueryVariations.getQueryVariations("H quintero gomez");
		for (String n: name) {
			System.out.println(n);
		}
	}
	
	public void testgenerateNameVariations1() {
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
		HashSet<String> actual = AuthorQueryVariations.generateNameVariations(input);
		assertEquals(expected, actual);
	}
	public void testgenerateNameVariations2() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "HECTOR");
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, HECTOR\\b.*");
		expected.add("GOMEZ, H\\b.*");
		expected.add("GOMEZ, H");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorQueryVariations.generateNameVariations(input);
		assertEquals(expected, actual);
	}
	public void testgenerateNameVariations3() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H.*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorQueryVariations.generateNameVariations(input);
		assertEquals(expected, actual);
	}
	public void testgenerateNameVariations4() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		input.put("middle", "Q");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H\\w* Q.*");
		expected.add("GOMEZ, H\\w*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorQueryVariations.generateNameVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testgenerateNameVariations5() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		input.put("middle", "QUINTERO");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H\\w* QUINTERO\\b.*");
		expected.add("GOMEZ, H\\w* Q\\b.*");
		expected.add("GOMEZ, H\\w*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorQueryVariations.generateNameVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testgenerateNameVariations6() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ,.*");
		HashSet<String> actual = AuthorQueryVariations.generateNameVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testgenerateNameVariations7() {
		HashMap<String,String> input = new HashMap<String,String>() {{
			put("last", "MILLAR");
			put("first", "CAROL");
			put("middle", "EVELYN");
		}}; 
		HashSet<String> expected = new HashSet<String>() {{
			add("MILLAR, CAROL EVELYN\\b.*");
		    add("MILLAR, CAROL E\\b.*");
		    add("MILLAR, C E\\b.*");
		    add("MILLAR, CAROL");
		    add("MILLAR, C");
		    add("MILLAR,");
		}};
		HashSet<String> actual = AuthorQueryVariations.generateNameVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testGenerateSynonymVariations1() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "HECTOR");
		input.put("last", "GOMEZ");
		input.put("middle", "Q");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, HECTOR");
		expected.add("GOMEZ, H");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorQueryVariations.generateSynonymVariations(input);
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
		HashSet<String> actual = AuthorQueryVariations.generateSynonymVariations(input);
		assertEquals(expected, actual);
	}
	public void testGenerateSynonymVariations3() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("first", "H");
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ, H.*");
		expected.add("GOMEZ,");
		HashSet<String> actual = AuthorQueryVariations.generateSynonymVariations(input);
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
		HashSet<String> actual = AuthorQueryVariations.generateSynonymVariations(input);
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
		HashSet<String> actual = AuthorQueryVariations.generateSynonymVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testGenerateSynonymVariations6() {
		HashMap<String,String> input = new HashMap<String,String>();
		input.put("last", "GOMEZ");
		HashSet<String> expected = new HashSet<String>();
		expected.add("GOMEZ,.*");
		HashSet<String> actual = AuthorQueryVariations.generateSynonymVariations(input);
		assertEquals(expected, actual);
	}
	
	public void testgenerateSynonymVariations7() {
		HashMap<String,String> input = new HashMap<String,String>() {{
			put("last", "MILLAR");
			put("first", "CAROL");
			put("middle", "EVELYN");
		}}; 
		HashSet<String> expected = new HashSet<String>() {{
			add("MILLAR, CAROL EVELYN\\b.*");
		    add("MILLAR, C E\\b.*");
		    add("MILLAR, CAROL");
		    add("MILLAR, C");
		    add("MILLAR,");
		}};
		HashSet<String> actual = AuthorQueryVariations.generateSynonymVariations(input);
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
