package org.adsabs.solr.analysis;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.analysis.WriteableExplicitSynonymMap;
import org.apache.solr.analysis.WriteableSynonymMap;
import org.apache.solr.analysis.author.AuthorQueryVariations;
import org.apache.solr.analysis.author.AuthorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessCuratedAuthorSynonyms {
	
    public static final Logger log = LoggerFactory.getLogger(ProcessCuratedAuthorSynonyms.class);

	public static ArrayList<List<String>> parseGroups(BufferedReader in) {
		
		ArrayList<List<String>> groups = new ArrayList<List<String>>();
		ArrayList<String> l = new ArrayList<String>();
		String currentLine;
		
		try {
			while ((currentLine = in.readLine()) != null) {
				currentLine = currentLine.trim();
				if (currentLine.length() == 0) {
					groups.add(l);
					l = new ArrayList<String>();
				} else {
					l.add(currentLine);
				}
			}
			if (l.size() > 0) {
				groups.add(l);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return groups;
	}
	
	public static List<String> normalize(final List<String> l) {
		List<String> normalized = new ArrayList<String>() {{
			for (String s : l) {
				log.debug("normalizing " + s);
				String n = AuthorUtils.normalizeAuthor(s);
				log.debug("normalized: " + n);
				add(n);
			}
		}};
		return normalized;
	}
	
	public static HashMap<String,HashSet<String>> transformGroup(List<String> group) {
		
		log.debug("group: " + group.toString());
		
		// normalize incoming author names
		final List<String> normalized = normalize(group);
		log.debug("Normalized group: " + normalized.toString());
		
		// expanded list with all auto-generated synonym possibilities
		final List<String> withAutoSynonyms = new ArrayList<String>() {{
			for (String s : normalized) {
				addAll(AuthorUtils.getAsciiTransliteratedVariants(s));
				add(s);
			}
		}};
		log.debug("withAutoSynonyms: " + withAutoSynonyms.toString());
		
		// build a map of name -> variations to be used later
		final HashMap<String,HashSet<String>> variationsMap = new HashMap<String,HashSet<String>>();
		for (String s : withAutoSynonyms) {
			HashMap<String,String> parsedAuthor = null;
			try {
				parsedAuthor = AuthorUtils.parseAuthor(s);
			} catch (Exception e) {
				log.error("Error parsing " + s);
				log.error(e.getStackTrace().toString());
				continue;
			}
				
			variationsMap.put(s, AuthorQueryVariations.generateSynonymVariations(parsedAuthor));
		}
		
		log.debug("variations: " + variationsMap.toString());
		
		// build hashmap where keys are each author name from withAutoSynonms
		// and values are the set of variations generated from each of the remaining names
		HashMap<String,HashSet<String>> transformed = new HashMap<String,HashSet<String>>();
		for (final String synonymKey : withAutoSynonyms) {
			log.debug("working on " + synonymKey);
			// create list that includes all of withAutoSynonyms except the current synonymKey
			final List<String> theRest = new ArrayList<String>() {{
				for (String s : withAutoSynonyms) {
					if (!s.equals(synonymKey)) {
						add(s);
					}
				}
			}};
			Collections.sort(theRest);
			log.debug("the rest: " + theRest);
			
			HashSet<String> synonymValues = new HashSet<String>() {{
				for (String s : theRest) {
					add(s);
					if (variationsMap.containsKey(s)) {
						addAll(variationsMap.get(s));
					}
				}
			}};
			
			log.debug("synonymValues: " + synonymValues);
			
			transformed.put(synonymKey, synonymValues);
		}
		
		return transformed;
	}

	public static void processSynonyms(BufferedReader in, String outFile) {
		WriteableSynonymMap synMap = new WriteableExplicitSynonymMap();
		synMap.setOutput(outFile);
		ArrayList<List<String>> groups = parseGroups(in);
		for (List<String> group : groups) {
			HashMap<String,HashSet<String>> transformed = transformGroup(group);
			
			for (String key : transformed.keySet()) {
				synMap.put(key, transformed.get(key));
			}
		}
		try {
			synMap.persist(false);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PrintStream out = null;
		BufferedReader in = null;
		
		if (args.length < 2) {
			System.out.println("Usage: ProcessCuratedAuthorSynonyms <infile> <outfile>");
			System.exit(1);
		}
		
		File inFile = new File(args[0]);
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("invalid input file: " + args[0]);
		}
		
		processSynonyms(in, args[1]);
	}

}
