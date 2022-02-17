package org.apache.solr.analysis.author;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.analysis.author.AuthorUtils;

import junit.framework.TestCase;


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
		assertEquals("áéíóůÁÉÍÓŮ,", AuthorUtils.normalizeAuthor("áéíóůÁÉÍÓŮ"));
		// this character gets removed ⻉
		assertEquals("\u8349,", AuthorUtils.normalizeAuthor("\u8349")); // 草

	}

	public void testParseAuthor() throws Exception {
		HashMap<String, String> expected = new HashMap<String, String>();
		expected.put("Last", "Hoover");
		expected.put("First", "Herbert");
		expected.put("Middle", "C");
		assertEquals(expected, AuthorUtils.parseAuthor("Hoover, Herbert C."));
	}

	public void testASCIIFolding() {

		assertEquals("zazolc gesla jazn", AuthorUtils.foldToAscii("zażółć gęślą jaźń"));
		assertEquals("aeiouyAEIOUY", AuthorUtils.foldToAscii("áéíóůýÁÉÍÓŮÝ"));
		assertEquals("peissker", AuthorUtils.foldToAscii("peißker"));
	}

	public void testTransliterate() {
		check("MÜLLER, BILL", "MULLER, BILL", "MUELLER, BILL");
		check("GÓMEZ, HECTOR Q", "GOMEZ, HECTOR Q");
		check("guer\u00E7o, r", "guerco, r"); // guerço,	
	}

	
	public void testTransliterations() {
		check("FOO'EYE, BAR", "FOOEYE, BAR", "FOOYEYE, BAR", "FOOIEYE, BAR");
		check("FOOEV, BAR", "FOOYEV, BAR", "FOOJEV, BAR", "FOOIEV, BAR");
		check("Fooev, BAR", "Fooyev, BAR", "Foojev, BAR", "Fooiev, BAR");
		check("FOODJAN, BAR", "FOODYAN, BAR", "FOODIAN, BAR");
		check("Fookaya, BAR", "Fookaja, BAR", "Fookaia, BAR");
		check("FOOKI, BAR", "FOOKYI, BAR", "FOOKII, BAR", "FOOKY, BAR", "FOOKIY, BAR", "FOOKIJ, BAR");
		check("FOOVI, BAR", "FOOVYI, BAR", "FOOVII, BAR", "FOOVY, BAR", "FOOVIY, BAR", "FOOVIJ, BAR");
		check("FOO, YURI", "FOO, IURI");
		check("FOO, IAGNI", "FOO, YAGNI");
	}

	public void testTransRussianNames() {
		check("FOOVI, YURI", 
				"FOOVI, IURI",
				"FOOVII, IURI",
				"FOOVII, YURI",
				"FOOVIJ, IURI",
				"FOOVIJ, YURI",
				"FOOVIY, IURI",
				"FOOVIY, YURI",
				"FOOVY, IURI",
				"FOOVY, YURI",
				"FOOVYI, IURI",
				"FOOVYI, YURI");
	}

	public void testGenSynonyms() {
		check("FOO'EYE, BÄR", 
				"FOO'EYE, BAER",
				"FOO'EYE, BAR",
				"FOOEYE, BAER",
				"FOOEYE, BAR",
				"FOOEYE, BÄR",
				"FOOIEYE, BAER",
				"FOOIEYE, BAR",
				"FOOIEYE, BÄR",
				"FOOYEYE, BAER",
				"FOOYEYE, BAR",
				"FOOYEYE, BÄR");
	}


	private void check(String a, String... expected) {
		ArrayList<String> actual = AuthorUtils.getAsciiTransliteratedVariants(a);
		
		Arrays.sort(expected);
		
		String[] ac = new String[actual.size()];
		actual.toArray(ac);
		Arrays.sort(ac);
		
		//System.out.println(a);
		//System.out.println(Arrays.asList(expected));
		//System.out.println(Arrays.asList(ac));
		
		assertEquals(Arrays.asList(expected), Arrays.asList(ac));
		
	}



}
