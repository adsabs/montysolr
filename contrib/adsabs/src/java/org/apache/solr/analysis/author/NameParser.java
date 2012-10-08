package org.apache.solr.analysis.author;

//Thanks to Robert Cooper for this!
//package com.totsp.bookworm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
*
* @author kebernet
*/
public class NameParser {
	
    
	private static final Set<String> TITLES = new HashSet<String>();
	private static final Set<String> SUFFIXES = new HashSet<String>();
	private static final Set<String> COMPOUND_NAMES = new HashSet<String>();
	public static final int TITLE = 0;
	public static final int FIRST_NAME = 1;
	public static final int MIDDLE_NAME = 2;
	public static final int LAST_NAME = 3;
	public static final int SUFFIX = 4;

	static {
		for (String title : new String[] { "dr.", "dr", "doctor", "mr.", "mr", "mister", "ms.", "ms", "miss", "mrs.",
             "mrs", "mistress", "hn.", "hn", "honorable", "the", "honorable", "his", "her", "honor", "fr", "fr.",
             "frau", "hr", "herr", "rv.", "rv", "rev.", "rev", "reverend", "reverend", "madam", "lord", "lady",
             "sir", "senior", "bishop", "rabbi", "holiness", "rebbe", "deacon", "eminence", "majesty", "consul",
             "vice", "president", "ambassador", "secretary", "undersecretary", "deputy", "inspector", "ins.",
             "detective", "det", "det.", "constable", "private", "pvt.", "pvt", "petty", "p.o.", "po", "first",
             "class", "p.f.c.", "pfc", "lcp.", "lcp", "corporal", "cpl.", "cpl", "colonel", "col", "col.",
             "capitain", "cpt.", "cpt", "ensign", "ens.", "ens", "lieutenant", "lt.", "lt", "ltc.", "ltc",
             "commander", "cmd.", "cmd", "cmdr", "rear", "radm", "r.adm.", "admiral", "adm.", "adm", "commodore",
             "cmd.", "cmd", "general", "gen", "gen.", "ltgen", "lt.gen.", "maj.gen.", "majgen.", "major", "maj.",
             "mjr", "maj", "seargent", "sgt.", "sgt", "chief", "cf.", "cf", "petty", "officer", "c.p.o.", "cpo",
             "master", "cmcpo", "fltmc", "formc", "mcpo", "mcpocg", "command", "fleet", "force" }) {
	       NameParser.TITLES.add(title);
	    }

	    for (String suffix : new String[] { "jr.", "jr", "junior", "ii", "iii", "iv", "senior", "sr.", "sr", //family
             "phd", "ph.d", "ph.d.", "m.d.", "md", "d.d.s.", "dds", // doctors
             "k.c.v.o", "kcvo", "o.o.c", "ooc", "o.o.a", "ooa", "g.b.e", "gbe", // knighthoods
             "k.b.e.", "kbe", "c.b.e.", "cbe", "o.b.e.", "obe", "m.b.e", "mbe", //   cont
             "esq.", "esq", "esquire", "j.d.", "jd", // lawyers
             "m.f.a.", "mfa", //misc
             "r.n.", "rn", "l.p.n.", "lpn", "l.n.p.", "lnp", //nurses
             "c.p.a.", "cpa", //money men
             "d.d.", "dd", "d.div.", "ddiv", //preachers
             "ret", "ret." }) {
	       NameParser.SUFFIXES.add(suffix);
	    }

	    for (String comp : new String[] { "de", "la", "st", "st.", "ste", "ste.", "saint", "van", "der", "al", "bin",
             "le", "mac", "di", "del", "vel", "von", "e'", "san", "af", "el", "\'t" }) {
	       NameParser.COMPOUND_NAMES.add(comp);
	    }
	}

	/**
	 * This method will parse a name into first middle and last names.
	 * <p>
	 *  Notes: "Al" is treated as a name. "al" as a name fragment. That is the
	 *  only exception for capitalization.
	 * </p>
	 * @param name name to parse
	 * @return String[5] containing title, first, middle and last names, suffix
	 */
	public String[] parseName(String name) {
	    // NOTE Add lookahead for Suffixes to support 
	    // "Winthrop Wolfcasts, the 31st Duke of Winchester"
	    String[] result = new String[5];
	
	    if (name == null) {
	       return result;
	    }
	
	    StringBuffer title = new StringBuffer();
	    StringBuffer first = new StringBuffer();
	    StringBuffer middle = new StringBuffer();
	    StringBuffer last = new StringBuffer();
	    StringBuffer suffix = new StringBuffer();
	    boolean isLastCommaFirst = false;
	
	    if (name.indexOf(",") != -1) {
	       String[] lastRest = name.split(",");
	
	       if (lastRest.length > 2) {
	          isLastCommaFirst = true;
	       } else if (lastRest.length > 1) {
	          String[] suffixes = lastRest[1].toLowerCase().trim().split(" ");
	
	          for (String check : suffixes) {
	             if (!NameParser.SUFFIXES.contains(check)) {
	                isLastCommaFirst = true;
	
	                break;
	             }
	          }
	       } else if (lastRest.length == 1) {
	    	   name = name.replaceFirst(",$", "");
	       }
	    }
	
	    if (isLastCommaFirst) // the user split the last name
	    {
	       ArrayList<String> lastRest = new ArrayList<String>(Arrays.asList(name.split(",")));
	
//	       if (lastRest.size() > 2) {
//	          for (int i = 2; i < lastRest.length; i++) //append the remaining elements to the end of the second element
//	          {
//	             lastRest[1] += (" " + lastRest[i]);
//	          }
//	       }
	
	       result[NameParser.LAST_NAME] = lastRest.remove(0).trim();
	
	       if ((lastRest.size() == 1) && (lastRest.get(0).trim().indexOf(" ") == -1)) // easy case
	       {
	          result[NameParser.FIRST_NAME] = lastRest.remove(0).trim();
	
	          return result;
	       } else {
	    	   // join the rest together and split again on whitespace
	          ArrayList<String> rest = new ArrayList<String>(Arrays.asList(StringUtils.join(lastRest, " ").trim().split("\\s+")));
	
	          //parse titles
	          for (int i = 0; i < rest.size(); i++) {
	        	  if (NameParser.TITLES.contains(rest.get(i).toLowerCase().trim())) {
	        		  title.append(rest.remove(i));
	        	  }
	          }
	
	          if (title.length() > 0) {
	             result[NameParser.TITLE] = title.toString();
	          }
	
	          //parse suffixes
	          for (int i = 0; i < rest.size(); i++) {
	        	  if (NameParser.SUFFIXES.contains(rest.get(i).toLowerCase().trim())) {
	        		  suffix.insert(0, rest.remove(i));
	        	  }
	          }
	
	          if (suffix.length() > 0) {
	             result[NameParser.SUFFIX] = suffix.toString();
	          }
	
	          int[] nextNameOrder = new int[] { NameParser.FIRST_NAME, NameParser.MIDDLE_NAME };
	          int nextNameIndex = 0;
	
	          for (int i = 0; i < rest.size(); i++) {
	             StringBuffer nextName = new StringBuffer();
	
	             while (!rest.get(i).trim().equals("Al") && NameParser.COMPOUND_NAMES.contains(rest.get(i).toLowerCase().trim())) {
	                nextName.append(rest.get(i).trim());
	
	                if (i != (rest.size() - 1)) {
	                   nextName.append(' ');
	                }
	
	                i++;
	
	                if (i == (rest.size() - 1)) {
	                   break;
	                }
	             }
	
	             nextName.append(rest.get(i));
	             if (nextNameIndex < nextNameOrder.length) {
		             result[nextNameOrder[nextNameIndex]] = nextName.toString();
	             } else {
	            	 result[nextNameOrder[nextNameOrder.length - 1]] += " " + nextName.toString();
	             }
	             nextNameIndex++;
	             
	
//	             if (nextNameIndex == nextNameOrder.length) {
//	                for (int j = i + 1; j < tail; j++) {
//	                   if (j != (i + 1)) {
//	                      nextName.append(' ');
//	                   }
//	
//	                   nextName.append(rest[j]);
//	                }
//	
//	                result[nextNameOrder[nextNameIndex - 1]] = nextName.toString();
//	
//	                break;
//	             }
	          }
	       }
	    } // end last, first case.
	    else {
	       String[] names = name.split(" ");
	       int head = 0;
	       int tail = names.length - 1;
	
	       //parse titles
	       for (int i = head; (i < tail) && NameParser.TITLES.contains(names[i].toLowerCase().trim()); i++) {
	          if (i != 0) {
	             title.append(' ');
	          }
	
	          title.append(names[i]);
	          head++;
	       }
	
	       if (title.length() > 0) {
	          result[NameParser.TITLE] = title.toString();
	       }
	
	       //parse suffixes
	       for (int i = tail; (i >= head) && NameParser.SUFFIXES.contains(names[i].toLowerCase().trim()); i--) {
	          if (i != tail) {
	             suffix.insert(0, ' ');
	          }
	
	          suffix.insert(0, names[i]);
	          tail--;
	       }
	
	       if (suffix.length() > 0) {
	          result[NameParser.SUFFIX] = suffix.toString();
	          names[tail] = names[tail].replaceAll(",", "");
	       }
	
	       if (head == tail) { //Only one name left
	
	          if (names[head].trim().length() > 0) {
	             result[NameParser.LAST_NAME] = names[head];
	          }
	       } else {
	          //parse last name
	          last.append(names[tail]);
	          tail--;
	
	          for (int i = tail; (i >= head) && !names[i].trim().equals("Al")
	                   && NameParser.COMPOUND_NAMES.contains(names[i].toLowerCase().trim()); i--) {
	             last.insert(0, ' ');
	
	             last.insert(0, names[i]);
	             tail--;
	          }
	
	          boolean firstPass = true;
	
	          //parse first name
	          for (int i = head; i <= tail; i++) {
	             if (!firstPass) {
	                first.append(' ');
	             }
	
	             first.append(names[i].trim());
	             head++;
	             firstPass = false;
	
	             if (names[i].trim().equals("Al") || !NameParser.COMPOUND_NAMES.contains(names[i].trim().toLowerCase())) {
	                break;
	             }
	          }
	
	          //build middle name
	          for (int i = head; i <= tail; i++) {
	             if (i != head) {
	                middle.append(' ');
	             }
	
	             middle.append(names[i].trim());
	          }
	       }
	
	       if (first.length() > 0) {
	          result[NameParser.FIRST_NAME] = first.toString().trim();
	       }
	
	       if (last.length() > 0) {
	          result[NameParser.LAST_NAME] = last.toString().trim();
	       }
	
	       if (middle.length() > 0) {
	          result[NameParser.MIDDLE_NAME] = middle.toString().trim();
	       }
	    }
	
	    return result;
	}
}
