package org.adsabs.solr;

//import invenio.montysolr.jni.MontySolrVM;
//import invenio.montysolr.jni.PythonMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorVariations {

    public static final Logger log = LoggerFactory.getLogger(AuthorVariations.class);

	public static HashSet<String> getQueryVariations(String authorString) {
		
		HashMap<String,String> parsedAuthor = null;
		try {
			parsedAuthor = AuthorUtils.parseAuthor(authorString);
		} catch (NameParsingException npe) {
			log.error(npe.getMessage());
		}
		
		HashSet<String> variations = new HashSet<String>();
		if (parsedAuthor == null) {
			variations.add(authorString);
			return variations;
		}
		generateQueryVariations(parsedAuthor, variations);
		return variations;
	}
	
	public static HashSet<String> generateQueryVariations(HashMap<String,String> parsedAuthor) {
		HashSet<String> variations = new HashSet<String>();
		return generateQueryVariations(parsedAuthor, variations);
	}
	
	public static HashSet<String> generateQueryVariations(
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
						variations.add(last + ", " + first + " " + middle.substring(0,1) + ".*");
					}
				} else {
					if (middle.length() > 1) {
						variations.add(last + ", " + first + "\\w* " + middle.substring(0,1) + ".*");
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
	
	public static HashSet<String> generateSynonymVariations(HashMap<String,String> parsedAuthor) {
		HashSet<String> variations = new HashSet<String>();
		return generateSynonymVariations(parsedAuthor, variations);
	}
	
	public static HashSet<String> generateSynonymVariations(
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
						variations.add(last + ", " + first.substring(0,1) + " " + middle + ".*");
						variations.add(last + ", " + first + " " + middle + ".*");
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
