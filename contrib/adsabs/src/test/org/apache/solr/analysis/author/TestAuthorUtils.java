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
		assertEquals("áéíóůÁÉÍÓŮ,", AuthorUtils.normalizeAuthor("áéíóůÁÉÍÓŮ"));
		// this character gets removed ⻉
		assertEquals("\u8349,", AuthorUtils.normalizeAuthor("\u8349")); // 草
		
	}

	public void testParseAuthor() throws Exception {
		HashMap<String,String> expected = new HashMap<String,String>();
		expected.put("last", "Hoover");
		expected.put("first", "Herbert");
		expected.put("middle", "C");
		assertEquals(expected, AuthorUtils.parseAuthor("Hoover, Herbert C."));
	}

	public void testASCIIFolding() {
		
		assertEquals("zazolc gesla jazn", AuthorUtils.foldToAscii("zażółć gęślą jaźń"));
		assertEquals("aeiouyAEIOUY", AuthorUtils.foldToAscii("áéíóůýÁÉÍÓŮÝ"));
		assertEquals("peissker", AuthorUtils.foldToAscii("peißker"));
	}
	

  public void testTransliterate() {
    check(AuthorUtils.getAsciiTransliteratedVariants("MÜLLER, BILL"), "MULLER, BILL", "MUELLER, BILL");
    check(AuthorUtils.getAsciiTransliteratedVariants("GÓMEZ, HECTOR Q"), "GOMEZ, HECTOR Q", "GOEMEZ, HECTOR Q");
    check(AuthorUtils.getAsciiTransliteratedVariants("guer\u00E7o, r"), "guerco, r", "guerc\u0327o, r"); // guerço, r - in unicode guer\u00E7o
    
		assertEquals("FUE", AuthorUtils.transliterateAccents("FÜ"));
		assertEquals("fae", AuthorUtils.transliterateAccents("fä"));
		assertEquals("ch", AuthorUtils.transliterateAccents("č"));
	}
	
	// for reference implementation
	// see: 
	public void testTransRussianApostrophes() {
	  check(AuthorUtils.translitRussianApostrophes((
        new HashSet<String>(){{add("FOO'EYE, BAR");}}).iterator()), 
        "FOOEYE, BAR", "FOOYEYE, BAR", "FOOIEYE, BAR");
	  check(AuthorUtils.translitRussianApostrophes((
        new HashSet<String>(){{add("Foo'eye, BAR");}}).iterator()), 
        "Fooeye, BAR", "Fooyeye, BAR", "Fooieye, BAR");
	}
		
	public void testTransRussianLastNames1() {
		
		check(AuthorUtils.translitRussianLastNames1((
		    new HashSet<String>(){{add("FOOEV, BAR");}}).iterator()), 
		    "FOOYEV, BAR", "FOOJEV, BAR", "FOOIEV, BAR");
		
		check(AuthorUtils.translitRussianLastNames1((
        new HashSet<String>(){{add("Fooev, BAR");}}).iterator()), 
        "Fooyev, BAR", "Foojev, BAR", "Fooiev, BAR");
	}
	
	
	public void testTransRussianLastNames2() {
		check(AuthorUtils.translitRussianLastNames2((
        new HashSet<String>(){{add("FOONIA, BAR");}}).iterator()), 
		    "FOONIIA, BAR", "FOONIYA, BAR");
		check(AuthorUtils.translitRussianLastNames2((
        new HashSet<String>(){{add("Foonia, BAR");}}).iterator()), 
        "Fooniia, BAR", "Fooniya, BAR");
	}
	
	public void testTransRussianLastNames3() {
	  check(AuthorUtils.translitRussianLastNames3((
        new HashSet<String>(){{add("FOODJAN, BAR");}}).iterator()), 
        "FOODYAN, BAR", "FOODIAN, BAR", "FOODJAN, BAR");
	  check(AuthorUtils.translitRussianLastNames3((
        new HashSet<String>(){{add("Foodjan, BAR");}}).iterator()), 
        "Foodyan, BAR", "Foodian, BAR", "Foodjan, BAR");
	}
		
	public void testTransRussianLastNames4() {
	  check(AuthorUtils.translitRussianLastNames4((
        new HashSet<String>(){{add("FOOKAYA, BAR");}}).iterator()), 
        "FOOKAYA, BAR", "FOOKAJA, BAR", "FOOKAIA, BAR");
	  check(AuthorUtils.translitRussianLastNames4((
        new HashSet<String>(){{add("Fookaya, BAR");}}).iterator()), 
        "Fookaya, BAR", "Fookaja, BAR", "Fookaia, BAR");
	}
		
	public void testTransRussianLastNames5() {
		
		check(AuthorUtils.translitRussianLastNames5((
        new HashSet<String>(){{add("FOOKI, BAR");}}).iterator()), 
        "FOOKYI, BAR", "FOOKII, BAR", "FOOKY, BAR", "FOOKI, BAR", "FOOKIY, BAR", "FOOKIJ, BAR");

    check(AuthorUtils.translitRussianLastNames5((
        new HashSet<String>(){{add("Fooki, BAR");}}).iterator()), 
        "Fookyi, BAR", "Fookii, BAR", "Fooky, BAR", "Fooki, BAR", "Fookiy, BAR", "Fookij, BAR");

		check(AuthorUtils.translitRussianLastNames5((
        new HashSet<String>(){{add("FOOVI, BAR");}}).iterator()), 
        "FOOVYI, BAR", "FOOVII, BAR", "FOOVY, BAR", "FOOVI, BAR", "FOOVIY, BAR", "FOOVIJ, BAR");

    check(AuthorUtils.translitRussianLastNames5((
        new HashSet<String>(){{add("Foovi, BAR");}}).iterator()), 
        "Foovyi, BAR", "Foovii, BAR", "Foovy, BAR", "Foovi, BAR", "Fooviy, BAR", "Foovij, BAR");

	}
		
	public void testTransRussianFirstNames() {
	  
	  check(AuthorUtils.translitRussianFirstNames((
        new HashSet<String>(){{add("FOO, YURI");}}).iterator()), 
        "FOO, YURI", "FOO, IURI");
	  check(AuthorUtils.translitRussianFirstNames((
        new HashSet<String>(){{add("FOO, Yuri");}}).iterator()), 
        "FOO, Yuri", "FOO, Iuri");
	  
	  check(AuthorUtils.translitRussianFirstNames((
        new HashSet<String>(){{add("FOO, IAGNI");}}).iterator()), 
        "FOO, YAGNI", "FOO, IAGNI");
	  
	  
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
		HashSet<String> t = new HashSet<String>();
		t.add("FOOVI, YURI");
		HashSet<String> actual = AuthorUtils.transliterateRussianNames(t);
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
	
	private void check(HashSet<String> result, String...expected) {
    String[] actual = new String[result.size()];
    int i = 0;
    for (String n: result) {
      actual[i++] = n;
    }
    Arrays.sort(actual);
    Arrays.sort(expected);
    assertEquals(Arrays.toString(expected), Arrays.toString(actual));
  }
  
  private void check(ArrayList<String> result, String...expected) {
//    System.out.println(StringUtils.escape(result.toString()));
//    System.out.println(StringUtils.escape(Arrays.toString(expected)));
    
    assertEquals(Arrays.toString(expected), result.toString());
    
  }

}
