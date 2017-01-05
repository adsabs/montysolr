package org.apache.solr.analysis.author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.solr.analysis.author.AuthorUtils;

import junit.framework.TestCase;

// TODO: fix the errors, these methods should not be part of public API 
// they are confusing when public 

public class TestAuthorUtils extends TestCase {
	
	public void testNormalizeAuthor() {
		
		assertEquals("Kurtz, Michael", AuthorUtils.normalizeAuthor("Kurtz,   Michael"));
		assertEquals("Huchra, J", AuthorUtils.normalizeAuthor("Huchra,    J."));
		assertEquals("Gomez, Hector Q", AuthorUtils.normalizeAuthor(" Gomez,   Hector Q  "));
		assertEquals("Gómez, Hector Q", AuthorUtils.normalizeAuthor("Gómez, Hector Q"));
		assertEquals("Foo Eye, Bar", AuthorUtils.normalizeAuthor("Foo'Eye, Bar"));
		assertEquals("Radio, F M", AuthorUtils.normalizeAuthor("Radio, F.M."));
		assertEquals("NA49 COLLABORATION,", AuthorUtils.normalizeAuthor("NA49 COLLABORATION"));
		assertEquals("29, 000 STARDUST HOME DUSTERS", AuthorUtils.normalizeAuthor("29, 000 STARDUST HOME DUSTERS"));
		// unicode character vs unicode+accent
		// U+0061 (a) + U+0300
		// U+00E0 (à)
		assertEquals("G\u0061\u0300mez, Hector Q", AuthorUtils.normalizeAuthor("G\u0061\u0300mez, Hector Q."));
		assertEquals("G\u00E0mez, Hector Q", AuthorUtils.normalizeAuthor("G\u00E0mez, Hector Q."));
		
		assertEquals("hey, joe", AuthorUtils.normalizeAuthor("hey,_joe"));
		assertEquals("hey, joe", AuthorUtils.normalizeAuthor("hey, joe$#@^&*!!!"));
		
		assertEquals("o sullivan, mike", AuthorUtils.normalizeAuthor("o'sullivan, mike"));
		assertEquals("o sullivan, mike", AuthorUtils.normalizeAuthor("o' sullivan, mike"));
		assertEquals("mc donald, co", AuthorUtils.normalizeAuthor("mc'donald, co(.)"));
		assertEquals("GómezFoo, He ctor 29Q", AuthorUtils.normalizeAuthor("%$Gómez_Foo, He-ctor;  29Q."));
		assertEquals("Gómez, Hector Q", AuthorUtils.normalizeAuthor("  Gómez,\n Hector    Q "));
		
		assertEquals("Moon, D S", AuthorUtils.normalizeAuthor("Moon, D. -S."));
		assertEquals("Moon, D S", AuthorUtils.normalizeAuthor("Moon, D.-S."));
		assertEquals("Moon, D S", AuthorUtils.normalizeAuthor("Moon, D.--S."));
		assertEquals("Moon, D S", AuthorUtils.normalizeAuthor("Moon, D. -S.-"));
		assertEquals("Moon, Dae Sik", AuthorUtils.normalizeAuthor("Moon, Dae-Sik"));
		assertEquals("Moon, Dae Sik", AuthorUtils.normalizeAuthor("Moon, Dae -Sik"));
		assertEquals("Moon, Dae Sik", AuthorUtils.normalizeAuthor("Moon, Dae - Sik "));
	}

	public void testParseAuthor() throws Exception {
		HashMap<String,String> expected = new HashMap<String,String>();
		expected.put("last", "Hoover");
		expected.put("first", "Herbert");
		expected.put("middle", "C");
		assertEquals(expected, AuthorUtils.parseAuthor("Hoover, Herbert C."));
	}

	public void testASCIIFolding() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("MULLER, BILL");
		expected.add("MUELLER, BILL");
		ArrayList<String> actual = AuthorUtils.getAsciiTransliteratedVariants("MÜLLER, BILL");
		assertEquals(expected, new HashSet<String>(actual));
		expected.clear();
		expected.add("GOMEZ, HECTOR Q");
		expected.add("GOEMEZ, HECTOR Q");
		actual = AuthorUtils.getAsciiTransliteratedVariants("GÓMEZ, HECTOR Q");
		assertEquals(expected, new HashSet<String>(actual));
	}
	
	public void testTransliterate() {
		HashMap<String,String> testMap = new HashMap<String,String>();
		testMap.put("Ü", "UE");
		testMap.put("ä", "ae");
		testMap.put("č", "ch");
		for (String k : testMap.keySet()) {
			String expected = testMap.get(k);
			String actual = AuthorUtils.transliterateAccents(k);
			assertEquals(expected, actual);
		}
	}
	
	// for reference implementation
	// see: 
	public void testTransRussianApostrophes() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("FOOIEYE, BAR");
		expected.add("FOOYEYE, BAR");
		expected.add("FOOEYE, BAR");
		HashSet<String> input = new HashSet<String>();
		input.add("FOO'EYE, BAR");
		HashSet<String> actual = AuthorUtils.translitRussianApostrophes(input.iterator());
		assertEquals(expected, actual);
	}
		
	public void testTransRussianLastNames1() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("FOOYEV, BAR");
		expected.add("FOOJEV, BAR");
		expected.add("FOOIEV, BAR");
		HashSet<String> input = new HashSet<String>();
		input.add("FOOEV, BAR");
		HashSet<String> actual = AuthorUtils.translitRussianLastNames1(input.iterator());
		assertEquals(expected, actual);
	}	
	
	public void testTransRussianLastNames2() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("FOONIIA, BAR");
		expected.add("FOONIYA, BAR");
		HashSet<String> input = new HashSet<String>();
		input.add("FOONIA, BAR");
		HashSet<String> actual = AuthorUtils.translitRussianLastNames2(input.iterator());
		assertEquals(expected, actual);
	}
	
	public void testTransRussianLastNames3() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("FOODYAN, BAR");
		expected.add("FOODIAN, BAR");
		expected.add("FOODJAN, BAR");
		HashSet<String> input = new HashSet<String>();
		input.add("FOODJAN, BAR");
		HashSet<String> actual = AuthorUtils.translitRussianLastNames3(input.iterator());
		assertEquals(expected, actual);
	}
		
	public void testTransRussianLastNames4() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("FOOKAYA, BAR");
		expected.add("FOOKAJA, BAR");
		expected.add("FOOKAIA, BAR");
		HashSet<String> input = new HashSet<String>();
		input.add("FOOKAYA, BAR");
		HashSet<String> actual = AuthorUtils.translitRussianLastNames4(input.iterator());
		assertEquals(expected, actual);
	}
		
	public void testTransRussianLastNames5() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("FOOKYI, BAR");
		expected.add("FOOKII, BAR");
		expected.add("FOOKY, BAR");
		expected.add("FOOKI, BAR");
		expected.add("FOOKIY, BAR");
		expected.add("FOOKIJ, BAR");
		expected.add("FOOVYI, BAR");
		expected.add("FOOVII, BAR");
		expected.add("FOOVY, BAR");
		expected.add("FOOVI, BAR");
		expected.add("FOOVIY, BAR");
		expected.add("FOOVIJ, BAR");
		HashSet<String> input = new HashSet<String>();
		input.add("FOOKI, BAR");
		input.add("FOOVI, BAR");
		HashSet<String> actual = AuthorUtils.translitRussianLastNames5(input.iterator());
		assertEquals(expected, actual);
	}
		
	public void testTransRussianFirstNames() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("FOOBAR, YURI");
		expected.add("FOOBAR, IURI");
		expected.add("FOOBAR, YAGNI");
		expected.add("FOOBAR, IAGNI");
		HashSet<String> input = new HashSet<String>();
		input.add("FOOBAR, YURI");
		input.add("FOOBAR, IAGNI");
		HashSet<String> actual = AuthorUtils.translitRussianFirstNames(input.iterator());
		assertEquals(expected, actual);
	}
	
	public void testTransRussianNames() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("FOOVIY, IURI");
		expected.add("FOOVIY, YURI");
		expected.add("FOOVI, YURI");
		expected.add("FOOVYI, YURI");
		expected.add("FOOVYI, IURI");
		expected.add("FOOVIJ, IURI");
		expected.add("FOOVY, IURI");
		expected.add("FOOVIJ, YURI");
		expected.add("FOOVY, YURI");
		expected.add("FOOVI, IURI");
		expected.add("FOOVII, IURI");
		expected.add("FOOVII, YURI");
		HashSet<String> actual = AuthorUtils.transliterateRussianNames(new String[] {"FOOVI, YURI"});
		assertEquals(expected, actual);
	}
	
	public void testGenSynonyms() {
		HashSet<String> expected = new HashSet<String>();
		expected.add("FOO'EYE, BAR");
		expected.add("FOO'EYE, BAER");
		expected.add("FOOIEYE, BÄR");
		expected.add("FOOYEYE, BÄR");
		expected.add("FOOEYE, BÄR");
		expected.add("FOOIEYE, BAR");
		expected.add("FOOYEYE, BAR");
		expected.add("FOOEYE, BAR");
		expected.add("FOOIEYE, BAER");
		expected.add("FOOYEYE, BAER");
		expected.add("FOOEYE, BAER");
		HashSet<String> actual = new HashSet<String>(AuthorUtils.getAsciiTransliteratedVariants("FOO'EYE, BÄR"));
		assertEquals(expected, actual);
	}
}
