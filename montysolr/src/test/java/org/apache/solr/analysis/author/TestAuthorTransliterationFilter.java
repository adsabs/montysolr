package org.apache.solr.analysis.author;

import org.apache.lucene.tests.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class TestAuthorTransliterationFilter extends BaseTokenStreamTestCase {

    final class TestFilter extends TokenFilter {
        private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

        public TestFilter(TokenStream input) {
            super(input);
        }

        public boolean incrementToken() throws IOException {
            if (!input.incrementToken())
                return false;
            typeAtt.setType(AuthorUtils.AUTHOR_INPUT);
            return true;
        }
    }

    public void testAuthorSynonyms() throws Exception {

        checkIt("Müller, Bill", "Müller, Bill", "Mueller, Bill", "Muller, Bill");
        checkIt("Peißker, L", "Peißker, L", "Peissker, L");

    }

    public void testAccents() throws Exception {
        checkIt("Jeřábková, Tereza", "Jeřábková, Tereza", "Jerabkova, Tereza");
        checkIt("Dupré", "Dupré", "Dupre,");
        checkIt("Duprè", "Duprè", "Dupre,"); // Dupre\\xcc\\x80
        checkIt("\u0141", "Ł", "L,");
        checkIt("Mendigutıa", "Mendigutıa", "Mendigutia,");
        checkIt("krivodubski, v", "krivodubski, v", "krivodubskyi, v", "krivodubskiy, v", "krivodubskij, v", "krivodubskii, v");

    }

    public void testTransliterationIsLocaleIndependent() {
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(Locale.ROOT);
            Set<String> expected = new HashSet<>(AuthorUtils.getAsciiTransliteratedVariants("KIEV, I"));

            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            Set<String> actual = new HashSet<>(AuthorUtils.getAsciiTransliteratedVariants("KIEV, I"));

            assertEquals(expected, actual);
        } finally {
            Locale.setDefault(previous);
        }
    }

    private void checkIt(String input, String... expected) throws Exception {
        Reader reader = new StringReader(input);
        Tokenizer tokenizer = new KeywordTokenizer();
        tokenizer.setReader(reader);
        AuthorTransliterationFactory factory = new AuthorTransliterationFactory(new HashMap<String, String>());
        TokenStream stream = factory.create(new TestFilter(tokenizer));
        assertTokenStreamContents(stream, expected);
    }
}
