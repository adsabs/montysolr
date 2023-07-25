package org.apache.solr.analysis.author;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

	public void testRussian() {

		// must work in any direction
		check("krivodubski, v", "krivodubskii, v", "krivodubskij, v", "krivodubskiy, v", "krivodubskyi, v");
		check("krivodubskij, v", "krivodubskii, v", "krivodubski, v", "krivodubskiy, v", "krivodubskyi, v");
		check("krivodubskiy, v", "krivodubski, v", "krivodubskii, v", "krivodubskij, v", "krivodubskyi, v");
		check("krivodubskyi, v", "krivodubskii, v", "krivodubskij, v", "krivodubskiy, v", "krivodubski, v");
		
		// suffix -ki wont be matched because it is not preceded by selected consonant
		check("peki, v");
		
		// this one is
		check("pelki, v", "pelkii, v", "pelkij, v", "pelkiy, v", "pelkyi, v");
		
		// similar (but different suffixes)
		check("anajev, z", "anayev, z", "anaiev, z", "anaev, z");
		check("anaev, z", "anayev, z", "anaiev, z", "anajev, z");
		
		check("tarzjan,", "tarzian,", "tarzyan,");
		check("tarzyan,", "tarzian,", "tarzjan,");
		
		check("tarjan,");
		check("taryan,");
		
		check("fookaya,", "fookaja,", "fookaia,");
		
		// woman's surname
		check("sarnieva,", "sarneva,", "sarnjeva,", "sarnyeva,");
		check("sarneva,", "sarnjeva,", "sarnyeva,", "sarnieva,");
		
		//old pattern: looks wrong, because k is preceded by o
		//check("FOOKI, BAR", "FOOKYI, BAR", "FOOKII, BAR", "FOOKY, BAR", "FOOKIY, BAR", "FOOKIJ, BAR");
		//what we do now:
		check("fooki,");
		
		
		// first names
		check("gagarin, yuri", "gagarin, iuri");
		check("gagarin, iuri", "gagarin, yuri");
		
		// german modifications
		check("foo, BÄR", "foo, BAER", "foo, BAR");
		
		// long 'E -> E, IE, YE (seem wrong still)
		check("f'edorov", "fedorov,", "fiedorov,", "fyedorov,");
	}


	private void check(String a, String... expected) {
		ArrayList<String> actual = AuthorUtils.getAsciiTransliteratedVariants(a);

		Arrays.sort(expected);

		String[] ac = new String[actual.size()];
		actual.toArray(ac);
		Arrays.sort(ac);

//		 System.out.println(a);
//		 System.out.println(Arrays.asList(expected));
//		 System.out.println(Arrays.asList(ac));

		assertEquals(Arrays.asList(expected), Arrays.asList(ac));

	}

}
