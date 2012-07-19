package org.apache.lucene.analysis.synonym;

import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.solr.analysis.AuthorSynonymFilterFactory;
import org.apache.solr.analysis.WriteableSynonymMap;

public class TestAuthorSynonymFilter extends BaseTokenStreamTestCase {
	public void testAuthorSynonyms1() throws Exception {
		Reader reader = new StringReader("MILLER, BILL");
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		AuthorSynonymFilterFactory factory = new AuthorSynonymFilterFactory();
		WriteableSynonymMap map = new WriteableSynonymMap(null);
		List<String> rules = new ArrayList<String>();
		rules.add("MILLER, WILLIAM=>MILLER, B;MILLER, BILL;MILLER,;MILLER, BILL\\b.*");
		rules.add("MILLER, BILL=>MILLER, WILLIAM;MILLER, WILLIAM\\b.*;MILLER,;MILLER, W");
		
		for (int i=0; i<rules.size();i++) {
			rules.set(i, rules.get(i).replace(",", "\\,").replace(" ", "\\ ").replace(";", ","));
		}
		map.parseRules(rules);
		factory.setSynonymMap(map);
		TokenStream stream = factory.create(tokenizer);
		String[] expected = { "MILLER, BILL", "MILLER, W", "MILLER,", "MILLER, WILLIAM\\b.*", "MILLER, WILLIAM" };
		assertTokenStreamContents(stream, expected);
	}
	public void testAuthorSynonyms2() throws Exception {
		Reader reader = new StringReader("GRANT, CAROLYN S;GRANT,;GRANT, C\b.*;GRANT, CAROLYN\b.*;GRANT, C");
		PatternTokenizer tokenizer = new PatternTokenizer(reader, Pattern.compile(";"), -1);
		AuthorSynonymFilterFactory factory = new AuthorSynonymFilterFactory();
		WriteableSynonymMap map = new WriteableSynonymMap(null);
		List<String> rules = new ArrayList<String>();
		
		//XXX: there is a problem with the matcher, it doesn't find
		// the author name on the right side of =>
		// it doesn't expand the pattern to match all possible names
		// example: "GRANT, CAROLYN;GRANT,;GRANT, C\b.*;GRANT, CAROLYN\b.*;GRANT, C"
		
		rules.add("GRANT, CAROLYN S=>STERN, CAROLYN P;STERN, C P.*;STERN, CAROLYN P.*;STERN GRANT, CAROLYN\b.*;STERN GRANT, C;STERN GRANT, CAROLYN;STERN, CAROLYN;STERN,;STERN GRANT,;STERN, C");
		rules.add("STERN GRANT, CAROLYN=>STERN, CAROLYN P;GRANT, C;GRANT, CAROLYN S;STERN, C P.*;GRANT, CAROLYN;STERN, CAROLYN P.*;GRANT, C S.*;GRANT, CAROLYN S.*;STERN, CAROLYN;STERN,;GRANT,; STERN, C");
		rules.add("STERN, CAROLYN P=>GRANT, C;GRANT, CAROLYN S;GRANT, CAROLYN;GRANT, C S.*;STERN GRANT, CAROLYN\b.*;STERN GRANT, C;STERN GRANT, CAROLYN;GRANT, CAROLYN S.*;GRANT,;STERN GRANT,");
		
		for (int i=0; i<rules.size();i++) {
			rules.set(i, rules.get(i).replace(",", "\\,").replace(" ", "\\ ").replace(";", ","));
		}
		map.parseRules(rules);
		
		factory.setSynonymMap(map);
		TokenStream stream = factory.create(tokenizer);
		String[] expected = {
				"GRANT, CAROLYN S", 
				"STERN, C", 
				"STERN GRANT,", 
				"STERN,", 
				"STERN, CAROLYN", 
				"STERN GRANT, CAROLYN", 
				"STERN GRANT, C", 
				"STERN GRANT, CAROLYN\b.*",
				"STERN, CAROLYN P.*", 
				"STERN, C P.*", 
				"STERN, CAROLYN P",
				"GRANT,",
				"GRANT, C\b.*", 
				"GRANT, CAROLYN\b.*", 
				"GRANT, C" 
		};
		assertTokenStreamContents(stream, expected);
		
		
		// according to jay this is not supposed to work this way
		/*
		reader = new StringReader("STERN, C");
		tokenizer = new PatternTokenizer(reader, Pattern.compile(";"), -1);
		stream = factory.create(tokenizer);
		String[] expected2 = {
				"STERN, C", 
				"STERN, C", 
				"STERN GRANT,", 
				"STERN,", 
				"STERN, CAROLYN", 
				"STERN GRANT, CAROLYN", 
				"STERN GRANT, C", 
				"STERN GRANT, CAROLYN\b.*",
				"STERN, CAROLYN P.*", 
				"STERN, C P.*", 
				"STERN, CAROLYN P",
				"GRANT,",
				"GRANT, C\b.*", 
				"GRANT, CAROLYN\b.*", 
				"GRANT, C" 
		};
		assertTokenStreamContents(stream, expected2);
		*/
	}
}
