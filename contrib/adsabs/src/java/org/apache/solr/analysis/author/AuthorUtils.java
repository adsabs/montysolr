/**
 * 
 */
package org.apache.solr.analysis.author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;

import java.text.*;

/**
 * @author jluker
 *
 */
public class AuthorUtils {
	
	public static final String AUTHOR_QUERY_VARIANT = "AUTHOR_QUERY_VARIANT";
	public static final String AUTHOR_INPUT = "AUTHOR_INPUT";
	public static final String AUTHOR_TRANSLITERATED = "AUTHOR_TRANSLITERATED";
	public static final String AUTHOR_CURATED_SYN = "AUTHOR_CURATED_SYN";
	
	
	public static String normalizeAuthor(String a) {
		a = a.replaceAll("(?<=\\b\\p{L})\\.(?=\\s*\\b)", " ");
		a = a.replaceAll("[^\\w\\s\'\\p{L}\\p{Digit},_-]", "");
		a = a.trim().replaceAll("\\s+", " ");
		//a = a.toUpperCase();
		if (!(a.contains(",") || a.contains(" ")))
		  a = a + ",";
		return a;
	}
	
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
		
		// include original
		// synonyms.add(a);
		
		// downgrade to ascii
		String downgraded = foldToAscii(a);
		synonyms.add(downgraded);
		
		// transliterate accents
		String transAcc = transliterateAccents(a);
		synonyms.add(transAcc);
		
		// handle russian name stuff
		HashSet<String> transRus = transliterateRussianNames(new String[] {a, downgraded, transAcc});
		synonyms.addAll(transRus);
		
		// remove the original input from the set
		synonyms.remove(a);
		return new ArrayList<String>(synonyms);
	}
	
	static String foldToAscii(String a) {
		char[] in = a.toCharArray();
		char[] out = new char[in.length * 4];
		int outPos = ASCIIFoldingFilter.foldToASCII(in, 0, out, 0, in.length);
		return String.copyValueOf(out).trim();
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
	
	
	// XXX: this doesn't look right to me, the fifth step gets (possibly)
	// 5 times more items than the first step
	public static HashSet<String> transliterateRussianNames(String[] in) {
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
     * 'E == E == IE == YE
     * note that we do not index 'E since the search
     * engine simply strips all apostrophes
     */
	static Pattern p0 = Pattern.compile("(?<=\\w{2})'(?=[Ee])");
	static HashSet<String> translitRussianApostrophes(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		
		while (itr.hasNext()) {
			Matcher m = p0.matcher(itr.next());
			if (m.find()) {
				syn.add(m.replaceAll("I"));
				syn.add(m.replaceAll("Y"));
				syn.add(m.replaceAll(""));
			}
		}
		//log.debug("apostrophes: " + syn);
		return syn;
	}
		
    /* russian last names I:
     * [^IJY]EV$ == IEV$ == YEV$ == JEV$ 
     * [^IJY]EVA$ == IEVA$ == YEVA$ == JEVA$ 
     */
	static Pattern p1 = Pattern.compile("(?<![IJY])EV(?=A?,)");
	static HashSet<String> translitRussianLastNames1(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		while (itr.hasNext()) {
			Matcher m = p1.matcher(itr.next());
			if (m.find()) {
				syn.add(m.replaceAll("IEV"));
				syn.add(m.replaceAll("YEV"));
				syn.add(m.replaceAll("JEV"));
			}
		}
		//log.debug("last names I: " + syn);
		return syn;
     }
		
    /* russian last names II:
     * ([NRBO])IA$ == $1IIA$ == $1IYA$
     */
	static Pattern p2 = Pattern.compile("(?<=[NRBO])I(?=A,)");
	static HashSet<String> translitRussianLastNames2(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		while (itr.hasNext()) {
			Matcher m = p2.matcher(itr.next());
			if (m.find()) {
				syn.add(m.replaceAll("II"));
				syn.add(m.replaceAll("IY"));
			}
		}
		//log.debug("last names II: " + syn);
		return syn;
	}

    /* russian last names III:
     * ([DHKLMNPSZ])IAN$ == $1YAN$ == $1JAN$ 
     */
	static Pattern p3 = Pattern.compile("(?<=[DHKLMNPSZ])[IJY](?=AN,)");
	static HashSet<String> translitRussianLastNames3(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		
		while (itr.hasNext()) {
			Matcher m = p3.matcher(itr.next());
			if (m.find()) {
				syn.add(m.replaceAll("I"));
				syn.add(m.replaceAll("J"));
				syn.add(m.replaceAll("Y"));
			}
		}
		//log.debug("last names III: " + syn);
		return syn;
	}
		
    /* russian last names IV:
     * AIA$ == AYA$ == AJA$ 
     */
	static Pattern p4 = Pattern.compile("(?<=[KNV]A)[IJY](?=A,)");
	static HashSet<String> translitRussianLastNames4(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		
		while (itr.hasNext()) {
			Matcher m = p4.matcher(itr.next());
			if (m.find()) {
				syn.add(m.replaceAll("I"));
				syn.add(m.replaceAll("J"));
				syn.add(m.replaceAll("Y"));
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
	static Pattern p5 = Pattern.compile("(?<=[KV])I(?=,)");
	static HashSet<String> translitRussianLastNames5(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		
		while (itr.hasNext()) {
			Matcher m = p5.matcher(itr.next());
			if (m.find()) {
				syn.add(m.replaceAll("I"));
				syn.add(m.replaceAll("Y"));
				syn.add(m.replaceAll("YI"));
				syn.add(m.replaceAll("IY"));
				syn.add(m.replaceAll("IJ"));
				syn.add(m.replaceAll("II"));
			}
		}
		//log.debug("last names V: " + syn);
		return syn;
	}
		
	/* russian first names
	 * ^IU == ^YU
	 * ^IA == ^YA
	 */
	static Pattern p6 = Pattern.compile("(?<=, )[YI](?=[AU])");
	static HashSet<String> translitRussianFirstNames(Iterator<String> itr) {
		HashSet<String> syn = new HashSet<String>();
		
		while (itr.hasNext()) {
			Matcher m = p6.matcher(itr.next());
			if (m.find()) {
				syn.add(m.replaceAll("I"));
				syn.add(m.replaceAll("Y"));
			}
		}
		//log.debug("first names: " + syn);
		return syn;
	}
		
}
