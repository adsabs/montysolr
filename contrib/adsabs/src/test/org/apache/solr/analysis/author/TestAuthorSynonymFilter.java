package org.apache.solr.analysis.author;

import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.solr.analysis.WriteableEquivalentSynonymMap;
import org.apache.solr.analysis.WriteableExplicitSynonymMap;
import org.apache.solr.analysis.WriteableSynonymMap;
import org.apache.solr.analysis.author.AuthorSynonymFilterFactory;

public class TestAuthorSynonymFilter extends BaseTokenStreamTestCase {
	public void testAuthorSynonyms1() throws Exception {
		
		
		AuthorSynonymFilterFactory factory = new AuthorSynonymFilterFactory();
		WriteableSynonymMap map = new WriteableExplicitSynonymMap();
		map.setOutput("foo");
		
		List<String> rules = new ArrayList<String>();
		rules.add("MILLER, WILLIAM=>MILLER, B;MILLER, BILL;MILLER,;MILLER, BILL\\b.*");
		rules.add("MILLER, BILL=>MILLER, WILLIAM;MILLER, WILLIAM\\b.*;MILLER,;MILLER, W");
		
		for (int i=0; i<rules.size();i++) {
			rules.set(i, rules.get(i).replace(",", "\\,").replace(" ", "\\ ").replace(";", ","));
		}
		map.populateMap(rules);
		factory.setSynonymMap(map);
		
		Set orderedMap = new LinkedHashSet();
		for (String s: new String[]{"MILLER, B", "MILLER, BILL", "MILLER,", "MILLER, BILL\\b.*"}) {
			orderedMap.add(s);
		}
		assertEquals(map.get("MILLER, WILLIAM"), orderedMap);
		map.put("MILLER, WILLIAM", orderedMap);
		
		// 2nd row
		orderedMap = new LinkedHashSet();
		for (String s: new String[]{"MILLER, WILLIAM", "MILLER, WILLIAM\\b.*", "MILLER,", "MILLER, W"}) {
			orderedMap.add(s);
		}
		assertEquals(map.get("MILLER, BILL"), orderedMap);
		map.put("MILLER, BILL", orderedMap);
		
		Reader reader = new StringReader("MILLER, BILL");
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		TokenStream stream = factory.create(tokenizer);
		String[] expected = { "MILLER, BILL", "MILLER, WILLIAM", "MILLER, WILLIAM\\b.*", "MILLER,", "MILLER, W" };
		assertTokenStreamContents(stream, expected);
	}
	public void testAuthorSynonyms2() throws Exception {
		
		AuthorSynonymFilterFactory factory = new AuthorSynonymFilterFactory();
		WriteableSynonymMap map = new WriteableExplicitSynonymMap();
		map.setOutput("foo");
		
		List<String> rules = new ArrayList<String>();
		
		//XXX: there is a problem with the matcher, it doesn't find
		// the author name on the right side of =>
		// it doesn't expand the pattern to match all possible names
		// example: "GRANT, CAROLYN;GRANT,;GRANT, C\b.*;GRANT, CAROLYN\b.*;GRANT, C"
		
		String[] lines = {
		 "GRANT, CAROLYN S=>STERN, CAROLYN P;STERN, C P.*;STERN, CAROLYN P.*;STERN, C",
		 "STERN GRANT, CAROLYN=>STERN, CAROLYN P;GRANT, C",
		 "STERN, CAROLYN P=>GRANT, C;GRANT, CAROLYN S;GRANT, CAROLYN;GRANT, C S.*"
		};
		for (String l: lines ) {
			rules.add(l);
		}
		
		for (int i=0; i<rules.size();i++) {
			rules.set(i, rules.get(i).replace(",", "\\,").replace(" ", "\\ ").replace(";", ","));
		}
		map.populateMap(rules);
		
		// ???: I'm thinking this must necessary to control the order of the returned synonyms
		for (String r: lines) {
			LinkedHashSet orderedMap = new LinkedHashSet();
			String[] dd = r.split("=>");
			for (String x: dd[1].split(";")) {
				orderedMap.add(x);
			}
			map.put(dd[0], orderedMap);
		}
			
		Reader reader = new StringReader("GRANT, CAROLYN S;GRANT,;GRANT, C\\b.*;GRANT, CAROLYN\\b.*;GRANT, C");
		PatternTokenizer tokenizer = new PatternTokenizer(reader, Pattern.compile(";"), -1);
		
		factory.setSynonymMap(map);
		TokenStream stream = factory.create(tokenizer);
		String[] expected = {
				"GRANT, CAROLYN S",
				"STERN, CAROLYN P", "STERN, C P.*", "STERN, CAROLYN P.*", "STERN, C",
				"GRANT,",
				"GRANT, C\\b.*",
				"GRANT, CAROLYN\\b.*",
				"STERN, C", "STERN, CAROLYN P", "STERN, C P.*", "GRANT, CAROLYN S", "STERN, CAROLYN P.*", 
				"GRANT, C",
		};
		assertTokenStreamContents(stream, expected);
	}
	
	public void testAuthorEquivSynonymMap() throws Exception {
		
		AuthorSynonymFilterFactory factory = new AuthorSynonymFilterFactory();
		WriteableSynonymMap map = new WriteableEquivalentSynonymMap();
		map.setOutput("foo");
		
		List<String> rules = new ArrayList<String>(){{
			add("ADAMČUK\\,\\ K,ADAMCUK\\,\\ K,ADAMCHUK\\,\\ K,");
			add("ADAMČUK\\,\\ KAREL,ADAMCUK\\,\\ KAREL,ADAMCHUK\\,\\ KAREL,");
			add("ADAMČUK\\,\\ KOLJA,ADAMCUK\\,\\ KOLJA,ADAMCHUK\\,\\ KOLJA,");
		}};
		
//		for (int i=0; i<rules.size();i++) {
//			rules.set(i, rules.get(i).replace(",", "\\,").replace(" ", "\\ ").replace(";", ","));
//		}
		map.populateMap(rules);
		
		Reader reader = new StringReader("ADAMČUK, KAREL\\b.*;ADAMČUK, K\\b.*;ADAMČUK, KAREL;ADAMČUK, K;ADAMČUK,");
		PatternTokenizer tokenizer = new PatternTokenizer(reader, Pattern.compile(";"), -1);
		
		factory.setSynonymMap(map);
		TokenStream stream = factory.create(tokenizer);
		String[] expected = {
			"ADAMČUK, KAREL\\b.*", "ADAMCHUK, KAREL", "ADAMČUK, KAREL", "ADAMCUK, KAREL",
			"ADAMČUK, K\\b.*", "ADAMCUK, K", "ADAMCHUK, K", "ADAMČUK, K",
			"ADAMČUK, KAREL", "ADAMČUK, KAREL", "ADAMCUK, KAREL", "ADAMCHUK, KAREL",
			"ADAMČUK, K", "ADAMČUK, K", "ADAMCUK, K", "ADAMCHUK, K",
			"ADAMČUK,",
		};
		assertTokenStreamContents(stream, expected);
	}
}
