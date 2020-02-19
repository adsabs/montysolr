package org.apache.solr.analysis.author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.*;
import static net.gcardone.junidecode.Junidecode.unidecode;

/**
 * OK; i'll admit it - this is extremely ugly piece of code which i inherited;
 * I tried to rewrite it once, but it was so convoluted that I resigned. 
 * 
 *
 */
public class AuthorUtils {
	
	public static final String AUTHOR_QUERY_VARIANT = "AUTHOR_QUERY_VARIANT";
	public static final String AUTHOR_INPUT = "AUTHOR_INPUT";
	public static final String AUTHOR_TRANSLITERATED = "AUTHOR_TRANSLITERATED";
	public static final String AUTHOR_CURATED_SYN = "AUTHOR_CURATED_SYN";
	
	// to remove commas from behind initials B. => B
	static Pattern n0 = Pattern.compile("(?<=\\b\\p{L})\\.(?=\\s*\\b)");
	// these are the characters we allow for author names
	// we keep any unicode character with its punctuation, digits,
	// and some special characters
	// original, which may miss diacritics: "(?<=\\b\\p{L})\\.(?=\\s*\\b)" \P{M}\p{M}*+
	// [^,\\-\\w\\s\\{N}\\p{L}\\p{M}*+]
	
	
	static Pattern n1 = Pattern.compile("[^,\\-\\s\\p{N}\\p{L}\\p{M}]");
	static Pattern n1b = Pattern.compile("[^,\\-\\s\\'\\p{N}\\p{L}\\p{M}]");
	
	// to normalize spaces
	static Pattern n2 = Pattern.compile("\\s+");
	// to normalize non escaped commas
	static Pattern n3 = Pattern.compile("(?<!\\\\),\\s*");
	// deal with word delimiters
	static Pattern n4 = Pattern.compile("(?<=\\p{L})\\'\\s*");
	
	public static String normalizeAuthor(String a) {
		return normalizeAuthor(a, false);
	}
	
	public static String normalizeAuthor(String a, boolean keepApostrophe) {
		boolean hasWildcards = a.indexOf('*') > -1 || a.indexOf('?') > -1; // \*\? should never be encountered here 
	  if (!keepApostrophe)
	    a = n4.matcher(a).replaceAll("-");
    a = n0.matcher(a).replaceAll(" ");
    if (keepApostrophe)
      a = n1b.matcher(a).replaceAll("");
    else
      a = n1.matcher(a).replaceAll("");
    a = n3.matcher(a).replaceAll(", ");
    a = n2.matcher(a.trim()).replaceAll(" ");
    
    
    if (!hasWildcards && !(a.contains(","))) // || a.contains(" ")
      a = a + ",";
    // do this at the end, we want to see the space instead of '-'
    a = a.replace('-', ' ');
    // normalize spaces once again
    a = n2.matcher(a.trim()).replaceAll(" ");
    return a;
  }
	
	/**
	 * this whole thing become obsolete when we included the python
	 * name parser library (that does MUCH better job in parsing names)
	 * 
	 * TODO: kill AuthorUtils.parseAuthor 
	 */
	public static HashMap<String,String> parseAuthor(String a) {
		return parseAuthor(a, true);
	}
	
	public static HashMap<String,String> parseAuthor(String a, boolean normalize) {
		HashMap<String,String> parsed = new HashMap<String,String>();
		if (a == null || a.length() == 0) {
			return parsed;
		}
		if (normalize) {
			a = AuthorUtils.normalizeAuthor(a);
		}
		NameParser np = new NameParser();
		String[] p;
		try {
			p = np.parseName(a);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String[] keys = {"title", "first", "middle", "last", "suffix"};
		for (int i = 0; i < keys.length; i++) {
			if (p[i] != null) {
				parsed.put(keys[i], p[i]);
			}
		}
		return parsed;
	}
	
	public static ArrayList<String> getAsciiTransliteratedVariants(String a) {
		HashSet<String> synonyms = new HashSet<String>();
		//a = a.toUpperCase();
		
		// include original
		synonyms.add(a);
		
		// downgrade to ascii
		String downgraded = foldToAscii(a);
		synonyms.add(downgraded);
		
	  // transliterate accents
    String transAcc = transliterateAccents(a);
    synonyms.add(transAcc);
    
		// handle russian name stuff
		HashSet<String> transRus = transliterateRussianNames(synonyms);
		synonyms.addAll(transRus);
		
		// apostrophes are now preserved in the index
		// so we need to generate translits for those
		if (a.contains("'"))
		  synonyms.add(a.replace("'", ""));
		
		// remove the original input from the set
		synonyms.remove(a);
		
		return new ArrayList<String>(synonyms);
	}
	
	public static String foldToAscii(String a) {
	  String b = unidecode(a.trim());
	  if (b.contains(" ,"))
	    b = b.replace(" ,", ",");
	  return b;
	}

	
  static String transliterateAccents(String a) {
    String decomposed = Normalizer.normalize(a, Normalizer.Form.NFD);
    char[] in = decomposed.toCharArray();
    char[] out = new char[in.length * 4];
    int outPos = 0;
    for (int i = 0; i < in.length; i++) {
      final char c = in[i];
      // prev will be the 1st part of the decomp char
      char prev = (i > 0) ? in[i - 1] : '\0';
      char replacement;
      if (c < '\u0080') {
        out[outPos++] = c;
        continue;
      }
      switch (c) {
        case '\u0141':
          replacement = 'L';
          break;
        case '\u0308':
          replacement = 'E';
          break;
        case '\u030a':
          replacement = 'A';
          break;
        case '\u0301': 
          replacement = 'E';
          break;
        case '\u030c':
          replacement = 'H';
          break;
        default:
          prev = '\0';
          replacement = c;
      }
      if (prev != '\0' && !Character.isUpperCase(prev)) {
        replacement = Character.toLowerCase(replacement);
      }
      out[outPos++] = replacement;
    }
    return String.copyValueOf(out).trim();
  }
  
	/*
	 * Splits name into parts (separated by comma and then by space)
	 * The comma is retained; spaces between parts of names are removed
	 * e.g 'john      , james' becomes: ['john,', 'james']
	 */
	public static String[] splitName(String name) {
		if (name.indexOf(',') > -1) {
			//System.out.println(name);
			int comma = name.indexOf(',');
			String[] nameParts = name.substring(comma+1).trim().split(" ");
			if (nameParts[0].equals(""))
				return new String[]{name.substring(0, comma).trim() + ","};
			
			String[] out = new String[nameParts.length+1];
			out[0] = name.substring(0, comma).trim() + ",";
			int i = 1;
			for (String s: nameParts) {
				out[i] = s;
				i += 1;
			}
			//System.out.println(Arrays.toString(out));
			return out;
		}
		else {
			return name.split(" ");
		}
	}
	
	

  // XXX: this doesn't look right to me, the fifth step gets (possibly)
	// 5 times more items than the first step
	public static HashSet<String> transliterateRussianNames(Set<String> in) {
		HashSet<String> synonyms = new HashSet<String>();
		for (String s : in) {
			HashSet<String> syn = new HashSet<String>();
			syn.add(s);
			syn.addAll(translitRussianApostrophes(syn.iterator()));
			syn.addAll(translitRussianLastNames1(syn.iterator()));
			syn.addAll(translitRussianLastNames2(syn.iterator()));
			syn.addAll(translitRussianLastNames3(syn.iterator()));
			syn.addAll(translitRussianLastNames4(syn.iterator()));
			syn.addAll(translitRussianLastNames5(syn.iterator()));
			syn.addAll(translitRussianFirstNames(syn.iterator()));
			synonyms.addAll(syn);
		}
		return synonyms;
	}
	
    /*
     * take care of russian apostrophes:
     * 'E => E == IE == YE
     * note that we do not index 'E since the search
     * engine simply strips all apostrophes
     */
	static Pattern p0 = Pattern.compile("(?<=\\w{2})'(?=[Ee])");
	static HashSet<String> translitRussianApostrophes(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		
		String x;
		while (itr.hasNext()) {
		  x = itr.next();
			Matcher m = p0.matcher(x);
			if (m.find()) {
			  if (x.charAt(m.end()) == 'E') {
          syn.add(m.replaceAll("I"));
          syn.add(m.replaceAll("Y"));
          syn.add(m.replaceAll(""));			    
			  }
			  else {
			    syn.add(m.replaceAll("i"));
			    syn.add(m.replaceAll("y"));
			    syn.add(m.replaceAll(""));
			  }
			}
		}
		//log.debug("apostrophes: " + syn);
		return syn;
	}
		
    /* russian last names I:
     * [^IJY]EV$ => IEV$ == YEV$ == JEV$ 
     * [^IJY]EVA$ => IEVA$ == YEVA$ == JEVA$ 
     */
	static Pattern p1 = Pattern.compile("(?<![IJYijy])[Ee][Vv](?=[aA]?,)");
	static HashSet<String> translitRussianLastNames1(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		String x;
		while (itr.hasNext()) {
		  x = itr.next();
			Matcher m = p1.matcher(x);
			if (m.find()) {
			  if (x.charAt(m.start()) == 'E') {
			    syn.add(m.replaceAll("IEV"));
	        syn.add(m.replaceAll("YEV"));
	        syn.add(m.replaceAll("JEV"));
			  }
			  else {
			    syn.add(m.replaceAll("iev"));
	        syn.add(m.replaceAll("yev"));
	        syn.add(m.replaceAll("jev"));
			  }
				
			}
		}
		//log.debug("last names I: " + syn);
		return syn;
   }
		
    /* russian last names II:
     * ([NRBO])IA$ == $1IIA$ == $1IYA$
     */
	static Pattern p2 = Pattern.compile("(?<=[NRBOnrbo])[Ii](?=[Aa],)");
	static HashSet<String> translitRussianLastNames2(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		String x;
		while (itr.hasNext()) {
		  x = itr.next();
			Matcher m = p2.matcher(x);
			if (m.find()) {
			  if (x.charAt(m.start()) == 'I') {
			    syn.add(m.replaceAll("II"));
			    syn.add(m.replaceAll("IY"));			    
			  }
			  else {
			    syn.add(m.replaceAll("ii"));
          syn.add(m.replaceAll("iy"));
			  }
			}
		}
		//log.debug("last names II: " + syn);
		return syn;
	}

    /* russian last names III:
     * ([DHKLMNPSZ])IAN$ == $1YAN$ == $1JAN$ 
     */
	static Pattern p3 = Pattern.compile("(?<=[DHKLMNPSZdhklmnpsz])[IJYijy](?=[Aa][Nn],)");
	static HashSet<String> translitRussianLastNames3(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		String x;
		while (itr.hasNext()) {
		  x = itr.next();
			Matcher m = p3.matcher(x);
			if (m.find()) {
			  if (x.charAt(m.start()) == 'I' || x.charAt(m.start()) == 'J' || x.charAt(m.start()) == 'Y') {
			    syn.add(m.replaceAll("I"));
			    syn.add(m.replaceAll("J"));
			    syn.add(m.replaceAll("Y"));			    
			  }
			  else {
			    syn.add(m.replaceAll("i"));
          syn.add(m.replaceAll("j"));
          syn.add(m.replaceAll("y"));
			  }
			}
		}
		//log.debug("last names III: " + syn);
		return syn;
	}
		
    /* russian last names IV:
     * AIA$ == AYA$ == AJA$ 
     */
	static Pattern p4 = Pattern.compile("(?<=[KNVknv][Aa])[IJYijy](?=[Aa],)");
	static HashSet<String> translitRussianLastNames4(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		String x;
		while (itr.hasNext()) {
		  x = itr.next();
			Matcher m = p4.matcher(x);
			if (m.find()) {
			  if (x.charAt(m.start()) == 'I' || x.charAt(m.start()) == 'J' || x.charAt(m.start()) == 'Y') {
			    syn.add(m.replaceAll("I"));
			    syn.add(m.replaceAll("J"));
			    syn.add(m.replaceAll("Y"));			    
			  }
			  else {
			    syn.add(m.replaceAll("i"));
          syn.add(m.replaceAll("j"));
          syn.add(m.replaceAll("y"));
			  }
			}
		}
		//log.debug("last names IV: " + syn);
		return syn;
	}
		
    /* russian last names V:
     * KI$ == KII$ == KIJ$ == KIY$ = KYI$
     * VI$ == VII$ == VIJ$ == VIY$ = VYI$
     * first transform [KVH]I into [KVH]II
     */
	static Pattern p5 = Pattern.compile("(?<=[KVkv])[Ii](?=,)");
	static HashSet<String> translitRussianLastNames5(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		String x;
		while (itr.hasNext()) {
		  x = itr.next();
			Matcher m = p5.matcher(x);
			if (m.find()) {
			  if (x.charAt(m.start()) == 'I') {
			    syn.add(m.replaceAll("I"));
			    syn.add(m.replaceAll("Y"));
			    syn.add(m.replaceAll("YI"));
			    syn.add(m.replaceAll("IY"));
			    syn.add(m.replaceAll("IJ"));
			    syn.add(m.replaceAll("II"));			    
			  }
			  else {
			    syn.add(m.replaceAll("i"));
          syn.add(m.replaceAll("y"));
          syn.add(m.replaceAll("yi"));
          syn.add(m.replaceAll("iy"));
          syn.add(m.replaceAll("ij"));
          syn.add(m.replaceAll("ii"));
			  }
			}
		}
		//log.debug("last names V: " + syn);
		return syn;
	}
		
	/* russian first names
	 * ^IU == ^YU
	 * ^IA == ^YA
	 */
	static Pattern p6 = Pattern.compile("(?<=, )[YIyi](?=[AUau])");
	static HashSet<String> translitRussianFirstNames(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		String x;
		while (itr.hasNext()) {
		  x = itr.next();
			Matcher m = p6.matcher(x);
			if (m.find()) {
			  if (x.charAt(m.start()) == 'I' || x.charAt(m.start()) == 'Y') {
			    syn.add(m.replaceAll("I"));
          syn.add(m.replaceAll("Y"));
			  }
			  else {
			    syn.add(m.replaceAll("i"));
			    syn.add(m.replaceAll("y"));			    
			  }
			}
		}
		//log.debug("first names: " + syn);
		return syn;
	}
			
}
