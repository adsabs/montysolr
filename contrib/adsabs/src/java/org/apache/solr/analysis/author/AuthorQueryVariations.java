package org.apache.solr.analysis.author;

//import montysolr.jni.MontySolrVM;
//import montysolr.jni.PythonMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a variations of the author names, it receives eg:
 * John, K
 * @author jluker
 *
 */
public class AuthorQueryVariations {

    public static final Logger log = LoggerFactory.getLogger(AuthorQueryVariations.class);

    /**
     * This method takes input string, e.g. "Hector, Gomez Q" and generates variations
     * of the author name.
     * 
     *  HECTOR, GOMEZ
	 *	HECTOR, G
	 *  HECTOR,
	 *  HECTOR, GOMEZ Q.*
	 *  HECTOR, G Q.*
     * 
     * It is essentially the same output as you get from getSynonymVariations except
     * a few special cases. These special cases are variations needed for querying the 
     * index of author names, but not needed or wanted for the process of transforming
     * the curated synonyms
     * 
     * Example "h quintero gomez" will output:
     * 
     * GOMEZ, H\w* QUINTERO\b.*
	 * GOMEZ,
  	 * GOMEZ, H\w*
	 * GOMEZ, H\w* Q\b.*   <-- only this one is extra added
     *  
     * @param authorString
     * @return
     */
	public static HashSet<String> getQueryVariationsInclRegex(String authorString) {
		
		HashMap<String,String> parsedAuthor = null;
		parsedAuthor = AuthorUtils.parseAuthor(authorString);
		
		HashSet<String> variations = new HashSet<String>();
		if (parsedAuthor == null) {
			variations.add(authorString);
			return variations;
		}
		generateNameVariations(parsedAuthor, variations);
		return variations;
	}
	
	public static HashSet<String> generateNameVariations(HashMap<String,String> parsedAuthor) {
		HashSet<String> variations = new HashSet<String>();
		return generateNameVariations(parsedAuthor, variations);
	}
	
	protected static HashSet<String> generateNameVariations(
			HashMap<String,String> parsedAuthor,
			HashSet<String> variations) {
		
		// get the base variations
		generateSynonymVariations(parsedAuthor, variations);
		
		// add the variations that are needed only for the query phase
		String last = parsedAuthor.get("last");
		String first = parsedAuthor.get("first");
		String middle = parsedAuthor.get("middle");
		
		if (first != null) {
			if (middle != null) {
				if (first.length() > 1) {
					if (middle.length() > 1) {
						variations.add(last + ", " + first + " " + middle.substring(0,1) + "\\b.*");
					} else {
						variations.add(last + ", " + first + " " + middle + ".*");
						variations.add(last + ", " + first.substring(0,1) + " " + middle + ".*");
					}
				} else {
					if (middle.length() > 1) {
						variations.add(last + ", " + first + "\\w* " + middle.substring(0,1) + "\\b.*");
					}
				}
			} else {
				if (first.length() > 1) {
					variations.add(last + ", " + first.substring(0,1) + "\\b.*");
				}
			}
		}
		
		return variations;
	}
	
	
	
	
	/**
     * This method takes input string, e.g. "Hector, Gomez Q" and generates variations
     * of the author name PLUS enhances the variations with regular expression patterns.
     * 
     * The process that transforms the curated synonyms uses *only* the variations
     * generated here. This limited set is also included in the variations used at query
     * time but DON'T ADD THINGS HERE that are only necessary for the query phase--use
     * getNameVariations for that
     * 
     *  HECTOR, GOMEZ
	 *	HECTOR, G
	 *  HECTOR,
	 *  HECTOR, GOMEZ Q.*
	 *  HECTOR, G Q.*
     * 
     *  
     * @param authorString
     * @return
     */
	public static HashSet<String> getQueryVariations(String authorString) {
		
		HashMap<String,String> parsedAuthor = null;
		parsedAuthor = AuthorUtils.parseAuthor(authorString);
		
		HashSet<String> variations = new HashSet<String>();
		if (parsedAuthor == null) {
			variations.add(authorString);
			return variations;
		}
		return generateSynonymVariations(parsedAuthor, variations);
	}

	public static HashSet<String> generateSynonymVariations(HashMap<String,String> parsedAuthor) {
		HashSet<String> variations = new HashSet<String>();
		return generateSynonymVariations(parsedAuthor, variations);
	}
	
	protected static HashSet<String> generateSynonymVariations(
			HashMap<String,String> parsedAuthor,
			HashSet<String> variations) {
		
		String last = parsedAuthor.get("last");
		String first = parsedAuthor.get("first");
		String middle = parsedAuthor.get("middle");
		
		if (parsedAuthor.size() == 1 && last != null) {
			variations.add(String.format("%s,.*", last)); // all we got was last name
		} else {
			variations.add(String.format("%s,", last));
		}
		
		if (first != null) {
			if (middle != null) {
				if (first.length() > 1) {
					variations.add(last + ", " + first);
					variations.add(last + ", " + first.substring(0,1));
					if (middle.length() > 1) {
						variations.add(last + ", " + first + " " + middle + "\\b.*");
						variations.add(last + ", " + first.substring(0,1) + " " + middle.substring(0,1) + "\\b.*");
					} else if (middle.length() == 1) {
//						variations.add(last + ", " + first.substring(0,1) + " " + middle + ".*");
//						variations.add(last + ", " + first + " " + middle + ".*");
					}
				} else {
					variations.add(last + ", " + first + "\\w*");
					if (middle.length() > 1) {
						variations.add(last + ", " + first + "\\w* " + middle + "\\b.*");
					} else if (middle.length() == 1) {
						variations.add(last + ", " + first + "\\w* " + middle + ".*");
					}
				}
			} else {
				if (first.length() > 1) {
					variations.add(last + ", " + first + "\\b.*");
					variations.add(last + ", " + first.substring(0,1));
				} else if (first.length() == 1) {
					variations.add(last + ", " + first + ".*");
				}
			}
		}
		
		return variations;
	}
}
