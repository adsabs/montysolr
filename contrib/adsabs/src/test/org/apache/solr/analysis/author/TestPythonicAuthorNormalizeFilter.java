package org.apache.solr.analysis.author;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import monty.solr.util.MontySolrAbstractLuceneTestCase;

import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.TokenStream;

public class TestPythonicAuthorNormalizeFilter extends MontySolrAbstractLuceneTestCase {
	
	
	public void test() throws Exception {
		
		compare("Ringo Starr", "Ringo Starr,", "Starr, Ringo");
		compare("W K H Panofsky", "W K H Panofsky,", "Panofsky, W K H");
		compare("Ibanez y Gracia, Maria Luisa, II, (ed.)", "Ibanez y Gracia, Maria Luisa, II, (ed.)", "Ibanez y Gracia, Maria Luisa");
		compare("Ibanez y Gracia, Maria Luisa, II., ed.", "Ibanez y Gracia, Maria Luisa, II., ed.", "Ibanez y Gracia, Maria Luisa");
		compare("Epstein, Brian, The Fifth Beatle", "Epstein, Brian, The Fifth Beatle", "Epstein, Brian");
		compare("Michael Edward Peskin", "Michael Edward Peskin,", "Peskin, Michael Edward");
		compare("M.E. Peskin", "M.E. Peskin,", "Peskin, M. E.");
		compare("M.E.Peskin", "M.E.Peskin,", "Peskin., M. E."); // to me this seems a bug of the python parser (?)
		compare("Ronaldo", "Ronaldo,");
		compare("Cantina Octavia Jones-Smith", "Cantina Octavia Jones-Smith,", "Jones-Smith, Cantina Octavia");
		compare("Jean-Luc Picard", "Jean-Luc Picard,", "Picard, Jean-Luc");
		compare("Jean Luc Picard", "Jean Luc Picard,", "Picard, Jean Luc");
		compare("Jean Luc de Picard", "Jean Luc de Picard,", "de Picard, Jean Luc");
		compare("Juan Q. Xavier Velasquez y Garcia, Jr.", "Juan Q. Xavier Velasquez y Garcia, Jr.", "Velasquez y Garcia, Juan Q. Xavier");
		compare("Jean-Luc Picard;Jean Luc de Picardie", "Jean-Luc Picard,", "Picard, Jean-Luc", "Jean Luc de Picardie,", "de Picardie, Jean Luc");
		compare("Pinilla-Alonso", "Pinilla-Alonso,");
		compare("Pinilla Alonso,", "Pinilla Alonso,");
		compare("Pinilla-Alonso, Brava", "Pinilla-Alonso, Brava");
		
		compare("purpose of this review is to bridge the gap between", "purpose of this review is to bridge the gap between,", "between, purpose of this review is to bridge the gap");
		compare("o' sullivan", "o'sullivan,");
		compare("o'sullivan", "o'sullivan,");
		compare("o' john o'sullivan", "o' john o'sullivan,", "o'sullivan, o'john");
		compare("o' john, o'sullivan", "o'john, o'sullivan");
		compare("Joachim von Lubow", "Joachim von Lubow,", "von Lubow, Joachim");
		compare("Gerard 't Hooft", "Gerard 't Hooft,", "'t Hooft, Gerard");
		compare("Pieter J. in 't Veld", "Pieter J. in 't Veld,", "'t Veld, Pieter J. in");
		compare("first", "first,");
		compare("first;james", "first,", "james,");
		compare("james; first; foo", "james,", "first,", "foo,");
		compare("V Maestro", "V Maestro,", "Maestro, V");
		compare("Maestro, V", "Maestro, V");
		compare("Maestro, J", "Maestro, J");
		compare("J Maestro", "J Maestro,", "Maestro, J");
		compare("Alves de Oliveira", "Alves de Oliveira,", "de Oliveira, Alves");
		
		// #16 github: honorifics, when the only thing we have, should be treated as surnames
		compare("goodman", "goodman,");
		compare("alissa goodman", "alissa goodman,", "goodman, alissa");
	}
	
	public void compare(String input, String... expected) throws Exception {
		Reader reader = new StringReader(input);
		Tokenizer tokenizer = new KeywordTokenizer();
		tokenizer.setReader(reader);
		PythonicAuthorNormalizeFilterFactory factory = new PythonicAuthorNormalizeFilterFactory(new HashMap<String,String>());
		TokenStream stream = factory.create(tokenizer);
		stream.reset();
		CharTermAttribute termAtt = stream.getAttribute(CharTermAttribute.class);
		
		ArrayList<String> data = new ArrayList<String>();
		while (stream.incrementToken()) {
			data.add(termAtt.toString());
		}
		String[] actuals = new String[data.size()];
		int i = 0;
		for (String s: data) {
			actuals[i] = s;
			i++;
		}
		assertArrayEquals("Tokenization differs for: " + input, 
				expected, actuals);
	}
	
	

}
