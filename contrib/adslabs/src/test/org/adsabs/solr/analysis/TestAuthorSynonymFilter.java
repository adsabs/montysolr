package org.adsabs.solr.analysis;

import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.solr.analysis.BaseTokenTestCase;
import org.apache.solr.analysis.PatternTokenizer;

public class TestAuthorSynonymFilter extends BaseTokenTestCase {
	public void testAuthorSynonyms1() throws Exception {
		Reader reader = new StringReader("MILLER, BILL");
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		AuthorSynonymFilterFactory factory = new AuthorSynonymFilterFactory();
		AuthorSynonymMap map = new AuthorSynonymMap();
		List<String> rules = new ArrayList<String>();
		rules.add("MILLER, WILLIAM => MILLER, B; MILLER, BILL; MILLER,; MILLER, BILL\\b.*");
		rules.add("MILLER, BILL => MILLER, WILLIAM; MILLER, WILLIAM\\b.*; MILLER,; MILLER, W");
		factory.parseRules(rules, map);
		factory.setSynonymMap(map);
		TokenStream stream = factory.create(tokenizer);
		String[] expected = { "MILLER, BILL", "MILLER, W", "MILLER,", "MILLER, WILLIAM\\b.*", "MILLER, WILLIAM" };
		assertTokenStreamContents(stream, expected);
	}
	public void testAuthorSynonyms2() throws Exception {
		Reader reader = new StringReader("GRANT, CAROLYN;GRANT,;GRANT, C\b.*;GRANT, CAROLYN\b.*;GRANT, C");
		PatternTokenizer tokenizer = new PatternTokenizer(reader, Pattern.compile(";"), 0);
		AuthorSynonymFilterFactory factory = new AuthorSynonymFilterFactory();
		AuthorSynonymMap map = new AuthorSynonymMap();
		List<String> rules = new ArrayList<String>();
		rules.add("GRANT, CAROLYN S => STERN, CAROLYN P;STERN, C P.*;STERN, CAROLYN P.*;STERN GRANT, CAROLYN\b.*;STERN GRANT, C;STERN GRANT, CAROLYN;STERN, CAROLYN;STERN,;STERN GRANT,;STERN, C");
		rules.add("STERN GRANT, CAROLYN => STERN, CAROLYN P;GRANT, C;GRANT, CAROLYN S;STERN, C P.*;GRANT, CAROLYN;STERN, CAROLYN P.*;GRANT, C S.*;GRANT, CAROLYN S.*;STERN, CAROLYN;STERN,;GRANT,; STERN, C");
		rules.add("STERN, CAROLYN P => GRANT, C;GRANT, CAROLYN S;GRANT, CAROLYN;GRANT, C S.*;STERN GRANT, CAROLYN\b.*;STERN GRANT, C;STERN GRANT, CAROLYN;GRANT, CAROLYN S.*;GRANT,;STERN GRANT,");
		factory.parseRules(rules, map);
		factory.setSynonymMap(map);
		TokenStream stream = factory.create(tokenizer);
		String[] expected = {};
		assertTokenStreamContents(stream, expected);
	}
}
