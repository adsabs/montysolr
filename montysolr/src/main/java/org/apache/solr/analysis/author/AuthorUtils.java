package org.apache.solr.analysis.author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.anyascii.AnyAscii;

public class AuthorUtils {

	static final Trie trie = buildTrie();
	static final NameParser nameParser = new NameParser(); 

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

	/**
	 * Method used by the tokenizer chain to normalize author names.
	 * 
	 * @param a - author name
	 * @param keepApostrophe - bool, preserve apostrophe
	 * @return Normalized string
	 */
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
		a = a.replaceAll(" ,", ",");
		return a;
	}


	/**
	 * Utility method to split string (author name) into constituting parts
	 * 
	 * @param a - author name
	 * @return map with 'last', 'first', 'middle' keys
	 */
	public static Map<String,String> parseAuthor(String a) {
		return parseAuthor(a, true);
	}

	public static Map<String,String> parseAuthor(String a, boolean normalize) {
		if (normalize) {
			return nameParser.parseName(AuthorUtils.normalizeAuthor(a));
		}
		else {			
			return nameParser.parseName(a);
		}
	}

	/**
	 * Utility method employed by AuthorTransliterationTokenizer and also by other components
	 * inside the parser chain to discover other potential reading of the author's name.
	 * 
	 * @param a - author name as string
	 * @return - list of author name variants in ascii form
	 */
	public static ArrayList<String> getAsciiTransliteratedVariants(String a) {
		HashSet<String> synonyms = new HashSet<String>();
		
		a = normalizeAuthor(a, true);
		//if (a.endsWith(","))
		//	a = a.substring(0, a.length()-1);

		// include original
		synonyms.add(a);

		// downgrade to ascii
		synonyms.add(normalizeAuthor(foldToAscii(a), true));
		
		// work around unidecode not always doing what we want
		String b = replaceUmlaut(a);
		if (!b.equals(a)) {
			synonyms.add(normalizeAuthor(foldToAscii(b), true));
		}

		// handle russian name stuff
		HashSet<String> transRus = transliterateRussianNames(synonyms );
		synonyms.addAll(transRus);
		synonyms.addAll(translitRussianApostrophes(synonyms.iterator()));

		// apostrophes are now preserved in the index
		// so we need to generate translits for those
		if (a.contains("'"))
			synonyms.add(a.replace("'", ""));

		// remove the original input from the set
		synonyms.remove(a);

		return new ArrayList<String>(synonyms);
	}

	protected static String foldToAscii(String a) {
		return AnyAscii.transliterate(a);
	}

	private static String replaceUmlaut(String input) {
		StringBuilder out = new StringBuilder();
		for (char c: input.toCharArray()) {
			switch(c) {
			case '\u00fc':
				out.append("ue");
				break;
			case '\u00f6':
				out.append("oe");
				break;
			case '\u00e4':
				out.append("ae");
				break;
			case '\u00df':
				out.append("ss");
				break;
			case '\u00dc':
				out.append("UE");
				break;
			case '\u00d6':
				out.append("OE");
				break;
			case '\u00c4':
				out.append("AE");
				break;
			default:
				out.append(c);
			}
		}
		return out.toString();
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
	
	
	/**
	 * Build efficient data structure for searching suffixes
	 * 
	 */
	private static Trie buildTrie() {
		ArrayList<Resolution> patterns = new ArrayList<Resolution>();
		
		/* russian last names I:
		 * [^IJY]EV$ => IEV$ == YEV$ == JEV$ 
		 * [^IJY]EVA$ => IEVA$ == YEVA$ == JEVA$ 
		 */
		patterns.add(new Resolution(new String[]{"ev,", "iev,", "yev,", "jev,"}));
		patterns.add(new Resolution(new String[]{"eva,", "ieva,", "yeva,", "jeva,"}));
		
		
		/* russian last names II:
		 * ([NRBO])IA$ == $1IIA$ == $1IYA$
		 */
		patterns.add(new Resolution(new String[]{"ia,", "iia,", "iya,"}, "nrbo"));
		
		/* russian last names III:
		 * ([DHKLMNPSZ])IAN$ == $1YAN$ == $1JAN$ 
		 */
		patterns.add(new Resolution(new String[]{"ian,", "yan,", "jan,"}, "dhklmnpsz"));
		
		/* russian last names IV:
		 * AIA$ == AYA$ == AJA$ 
		 */
		
		patterns.add(new Resolution(new String[]{"aia,", "aya,", "aja,"}));
		
		/* russian last names V:
		 * KI$ == KII$ == KIJ$ == KIY$ = KYI$
		 * VI$ == VII$ == VIJ$ == VIY$ = VYI$
		 * first transform [KVH]I into [KVH]II
		 */
		patterns.add(new Resolution(new String[]{"ki,", "kii,", "kij,", "kiy,", "kyi,"}, "dhklmnpsz"));
		
		
		/* russian first names
		 * ^IU == ^YU
		 * ^IA == ^YA
		 * 
		 * The only detail is that the pattern must be reversed (because we normally search in 
		 * a reversed version of a name; and comma is missing)
		 */
		patterns.add(new Resolution(new String[]{"ui", "uy"}));
		patterns.add(new Resolution(new String[]{"ai", "ay"}));
		
		String reverse;
		Trie trie = new Trie();
		
		for (Resolution resolution: patterns) {
			for (String s: resolution.suffixes) {
				reverse = StringUtils.reverse(s);
				trie.insert(reverse,  resolution);
			}			
		}
		return trie;
		
	}

	
	static Set<String> transliterateRussianName(String name) {
		// always search lowercase
		name = name.toLowerCase();
		HashSet<String> out= new HashSet<String>();
		out.add(name);
		
		String[] parts = splitName(name);
		String surname = parts[0];
		StringBuilder first = new StringBuilder();
		int i = 1;
		while (i < parts.length) {
			if (i > 1)
				first.append(" ");
			first.append(parts[i]);
			i += 1;
		}
		
		String rn = StringUtils.reverse(surname);
		Result result = trie.search(rn);
		
		// first modify surnames (suffixes are unique)
		if (result != null) {
			Resolution v = result.result;
			for (String x: v.transform(surname, result.suffix)) {
				if (first.length() > 0) {
					out.add(x + " " + first);					
				}
				else {
					out.add(x);
				}
			}
		}

		// then modify first names (possibly multi-plying output)
		String rfn = StringUtils.reverse(first.toString());
		result = trie.search(first.toString());
		if (result != null) {
			// collect the set of surname synonyms
			HashSet<String> surnames = new HashSet<String>();
			for (String o: out) {
				parts = splitName(o);
				surnames.add(parts[0]);
			}
			// combine each first name synonym with each surname synonym
			for (String x: result.result.transform(rfn, result.suffix)) {
				x = StringUtils.reverse(x);
				for (String rsurname: surnames) {
					out.add(rsurname + " " + x);
				}
			}
		}
		out.remove(name); // remove the original
		return out;

	}

	/*
	 * transliterate all names using Trie search for suffixes
	 */
	private static HashSet<String> transliterateRussianNames(Set<String> in) {
		HashSet<String> synonyms = new HashSet<String>();
		for (String s : in) {
			for (String r: transliterateRussianName(s)) {
				synonyms.add(r);
			}
		}
		return synonyms;
	}

	/*
	 * take care of russian apostrophes:
	 * 'E => E == IE == YE
	 * note that we do not index 'E since the search
	 * engine simply strips all apostrophes
	 */
	private static Pattern p0 = Pattern.compile("(?<=\\w{2})\'(?=[Ee])");
	private static Set<String> translitRussianApostrophes(Iterator<String> it) {
		Set<String> out = new HashSet<String>();
		String name;
		while (it.hasNext()) {
			name = it.next();
			if (name.indexOf("'e") >= 1) {
				//name = name.replaceAll("'e", "__");
				out.add(name.replaceAll("'e", "ie"));
				out.add(name.replaceAll("'e", "ye"));
				out.add(name.replaceAll("'e", "e"));
			}
			
		}
		
		return out;
	}

	
	private static class TrieNode {
	    private char c;
	    private HashMap<Character, TrieNode> children = new HashMap<>();
	    private Resolution leaf = null;

	    public TrieNode() {}

	    public TrieNode(char c){
	        this.c = c;
	    }

	    public HashMap<Character, TrieNode> getChildren() {
	        return children;
	    }

	    public void setChildren(HashMap<Character, TrieNode> children) {
	        this.children = children;
	    }

	    public boolean isLeaf() {
	        return leaf != null;
	    }

	    public void setLeaf(Resolution res) {
	        this.leaf = res;
	    }
	    
	    public Resolution getValue() {
	    	return this.leaf;
	    }
	}
	
	private static class Trie {

	    private TrieNode root;

	    public Trie() {
	        root = new TrieNode();
	    }

	    public void insert(String word, Resolution res) {
	        HashMap<Character, TrieNode> children = root.getChildren();
	        for(int i = 0; i < word.length(); i++) {
	            char c = word.charAt(i);
	            TrieNode node;
	            if(children.containsKey(c)) {
	                node = children.get(c);
	            } else { 
	                node = new TrieNode(c);
	                children.put(c, node);
	            }
	            children = node.getChildren();

	            if(i == word.length() - 1) {
	                node.setLeaf(res);
	            }
	        }
	    }

	    public Result search(String word) {
	        HashMap<Character, TrieNode> children = root.getChildren();
	        Resolution lastFound = null;
	        int lastI = 0;
	        
	        TrieNode node = null;
	        for(int i = 0; i < word.length(); i++) {
	            char c = word.charAt(i);
	            if(children.containsKey(c)) {
	                node = children.get(c);
	                children = node.getChildren();
	                if (node.isLeaf()) {
	                	lastFound = node.getValue();
	                	lastI = i;
	                }
	            } else { 
	                node = null;
	                break;
	            }
	        }
	        if (lastFound == null)
	        	return null;
	        return new Result(word.substring(0, lastI+1), lastFound);
	    }

	}
	
	private static class Result { 
	private String suffix;
	private Resolution result;
	
		Result(String suffix, Resolution res) {
			this.suffix = suffix;
			this.result = res;
		}
	}
	
	private static class Resolution {
		private String[] suffixes;
		private String mustMatch = "";
		private String mustNotMatch = "";
		Resolution(String[] suffixes) {
			this.suffixes = suffixes;
			
		}
		Resolution(String[] suffixes, String mustMatch) {
			this.suffixes = suffixes;
			this.mustMatch = mustMatch;
			
		}
		Resolution(String[] suffixes, String mustMatch, String mustNotMatch) {
			this.suffixes = suffixes;
			this.mustMatch = mustMatch;
			this.mustNotMatch = mustNotMatch;
		}
		
		List<String> transform(String surname, String key) {
			ArrayList<String> out = new ArrayList<String>();
			//out.add(surname);
			
			String prefix = surname.substring(0, surname.length() - key.length());
			String suffix = surname.substring(surname.length()-key.length());
			int prevChar = surname.length() - key.length() - 1;
			
			for (String s: suffixes) {
				if (suffix.equals(s))
					continue;
				if (this.mustMatch.length() > 0 && (prevChar <= 0 || (mustMatch.indexOf(surname.charAt(prevChar)) == -1))) {
					continue;					
				}
				if (this.mustNotMatch.length() > 0 && (prevChar <= 0 || (mustNotMatch.indexOf(surname.charAt(prevChar)) > -1))) {
					continue;					
				}
				out.add(prefix + s);
			}
			
			return out;
		}
	}

}
