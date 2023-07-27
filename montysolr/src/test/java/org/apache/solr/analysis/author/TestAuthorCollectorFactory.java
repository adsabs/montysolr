package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TestAuthorCollectorFactory extends BaseTokenStreamTestCase {

    public void testCollector() throws IOException, InterruptedException {


        Map<String, String> args = new HashMap<String, String>();
        args.put("tokenTypes", String.format("%s,%s", AuthorUtils.AUTHOR_INPUT,
                AuthorUtils.AUTHOR_TRANSLITERATED));
        args.put("emitTokens", "true");

        AuthorCollectorFactory factory = new AuthorCollectorFactory(args);
        factory.setExplicitLuceneMatchVersion(true);

        AuthorNormalizeFilterFactory normFactory = new AuthorNormalizeFilterFactory(new HashMap<String, String>());
        AuthorTransliterationFactory transliteratorFactory = new AuthorTransliterationFactory(new HashMap<String, String>());

        //create the synonym writer for the test MÜLLER, BILL
        TokenStream stream = new PatternTokenizer(Pattern.compile(";"), -1);
        ((Tokenizer) stream).setReader(new StringReader("MÜLLER, BILL;MÜller, Bill"));
        TokenStream ts = factory.create(transliteratorFactory.create(normFactory.create(stream)));
        assertTrue(ts instanceof AuthorCollectorFilter);
        ts.reset();

        TypeAttribute typeAtt = ts.getAttribute(TypeAttribute.class);
        CharTermAttribute termAtt = ts.getAttribute(CharTermAttribute.class);

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MÜLLER, BILL");
        assertEquals(AuthorUtils.AUTHOR_INPUT, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MUELLER, BILL");
        assertEquals(AuthorUtils.AUTHOR_TRANSLITERATED, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MULLER, BILL");
        assertEquals(AuthorUtils.AUTHOR_TRANSLITERATED, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MÜller, Bill");
        assertEquals(AuthorUtils.AUTHOR_INPUT, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MUEller, Bill");
        assertEquals(AuthorUtils.AUTHOR_TRANSLITERATED, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MUller, Bill");
        assertEquals(AuthorUtils.AUTHOR_TRANSLITERATED, typeAtt.type());

        assertFalse(ts.incrementToken());
        ts.close();

        args.put("emitTokens", "true");
        args.put("tokenTypes", AuthorUtils.AUTHOR_INPUT);
        factory = new AuthorCollectorFactory(args);
        stream = new PatternTokenizer(Pattern.compile(";"), -1);
        ((Tokenizer) stream).setReader(new StringReader("MÜLLER, BILL;MÜller, Bill"));
        ts = factory.create(transliteratorFactory.create(normFactory.create(stream)));
        ts.reset();

        typeAtt = ts.getAttribute(TypeAttribute.class);
        termAtt = ts.getAttribute(CharTermAttribute.class);

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MÜLLER, BILL");
        assertEquals(AuthorUtils.AUTHOR_INPUT, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MÜller, Bill");
        assertEquals(AuthorUtils.AUTHOR_INPUT, typeAtt.type());

        assertFalse(ts.incrementToken());
        ts.close();


        args.put("emitTokens", "false");
        args.put("tokenTypes", AuthorUtils.AUTHOR_TRANSLITERATED);
        factory = new AuthorCollectorFactory(args);

        stream = new PatternTokenizer(Pattern.compile(";"), -1);
        ((Tokenizer) stream).setReader(new StringReader("MÜLLER, BILL;MÜller, Bill"));
        ts = factory.create(transliteratorFactory.create(normFactory.create(stream)));
        ts.reset();

        typeAtt = ts.getAttribute(TypeAttribute.class);
        termAtt = ts.getAttribute(CharTermAttribute.class);

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MÜLLER, BILL");
        assertEquals(AuthorUtils.AUTHOR_INPUT, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MÜller, Bill");
        assertEquals(AuthorUtils.AUTHOR_INPUT, typeAtt.type());

        assertFalse(ts.incrementToken());
        ts.close();


        args.put("emitTokens", "false");
        args.put("tokenTypes", "foo");
        factory = new AuthorCollectorFactory(args);
        stream = new PatternTokenizer(Pattern.compile(";"), -1);
        ((Tokenizer) stream).setReader(new StringReader("MÜLLER, BILL;MÜller, Bill"));
        ts = factory.create(transliteratorFactory.create(normFactory.create(stream)));
        ts.reset();

        typeAtt = ts.getAttribute(TypeAttribute.class);
        termAtt = ts.getAttribute(CharTermAttribute.class);

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MÜLLER, BILL");
        assertEquals(AuthorUtils.AUTHOR_INPUT, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MUELLER, BILL");
        assertEquals(AuthorUtils.AUTHOR_TRANSLITERATED, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MULLER, BILL");
        assertEquals(AuthorUtils.AUTHOR_TRANSLITERATED, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MÜller, Bill");
        assertEquals(AuthorUtils.AUTHOR_INPUT, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MUEller, Bill");
        assertEquals(AuthorUtils.AUTHOR_TRANSLITERATED, typeAtt.type());

        ts.incrementToken();
        assertEquals(termAtt.toString(), "MUller, Bill");
        assertEquals(AuthorUtils.AUTHOR_TRANSLITERATED, typeAtt.type());

        assertFalse(ts.incrementToken());
    }


}
