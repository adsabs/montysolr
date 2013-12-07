/**
 * 
 */
package org.apache.solr.handler.dataimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.solr.analysis.author.AuthorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Code to receive Author name as an input and generate
 * the hierarchical facet as output, to insert into
 * a doc field
 *
 * See: http://wiki.apache.org/solr/HierarchicalFaceting#A.27facet.prefix.27__Based_Drill_Down
 */
public class FacetHierarchy {
	
	String columnName; // under what name to insert results into a doc
	List<String> sourceFields; // what fields to read data from
	Map<String, String[]> sourceKeys; // what keys to read (in case the field is nested)
	String splitBy = ",";
	String splitKeys = ":";
	
	public FacetHierarchy(String columnName, String[] source) {
		this.columnName = columnName;
		this.sourceFields = new ArrayList<String>();
		
		for (String s : source) {
			String[] parts = s.split(splitKeys);
			if (parts.length > 1) {
				String[] keys = new String[parts.length-1];
				for (int i=1; i<parts.length;i++) {
					keys[i-1] = parts[i];
				}
				sourceKeys.put(parts[0], keys);
			}
			sourceFields.add(parts[0]);
		}
	}
	
	public void addFacets(Map<String, Object> row) {
		
		ArrayList<String> target = new ArrayList<String>();
		
		for (String field : sourceFields) {
			if (row.containsKey(field)) {
				Object obj = row.get(field);
				
				if (obj instanceof List) {
					generateFacets((List<String>) obj, target);
				}
				else if (obj instanceof String) {
					generateFacets((String) obj, target);
				}
				else if (obj instanceof String[]) {
					generateFacets((String[]) obj, target);
				}
				else if (obj instanceof Map && sourceKeys.containsKey(field)) {
					generateFacets((Map) obj, target, sourceKeys.get(field));
				}
			}
		}
		
		if (target.size() > 0) {
			if (row.containsKey(columnName)) {
				List<String> output = (List<String>) row.get(columnName);
				output.addAll(target);
			}
			else {
				row.put(columnName, target);
			}
		}
		
	}
	
	
	@SuppressWarnings("rawtypes")
  private void generateFacets(Map obj, ArrayList<String> target, String[] keysToRead) {
		String[] vals = new String[keysToRead.length];
		for (int i=0; i<keysToRead.length;i++) {
			if (!obj.containsKey(keysToRead[i])) {
				return; // refuse to proceed
			}
			vals[i] = keysToRead[i];
		}
	  generateFacets(vals, target);
  }

	/*
	 * Input: "Smith, Jones" or ["Smith, Jones"]
	 * Written: ["0/Smith, J", "1/Smith, J/Smith, James"]
	 */
	
	protected void generateFacets(List<String> input, List<String> target) {
		for (String s: input) {
			generateFacets(s, target);
		}
	}
	
	protected void generateFacets(String[] input, List<String> target) {
		for (String s: input) {
			generateFacets(s, target);
		}
	}
	
	protected void generateFacets(String input, List<String> target) {
		
		if (input == null || input.equals("")) {
			return;
		}
		
		String[] parts = generateParts(input);
		
		for (int depth = 0; depth < parts.length; depth++) {
			
			StringBuilder newFacet = new StringBuilder(); 
			
			for (int i = 0; i <= depth; i++) {
				if (i == 0) {
					newFacet.append(String.format("%d", depth));
				}
				newFacet.append("/");
				newFacet.append(parts[i]);
			}
			
			target.add(newFacet.toString());
		}
	}
	
	/*
	 * Receives already normalized input (author name) and 
	 * returns back parts to build facets from
	 */
	protected String[] generateParts(String input) {
		
		input = AuthorUtils.normalizeAuthor(input);
		
		input = doFurtherFacetNormalizations(input);
		
		
		// this is currently optimized for speed, we know
		// we want only two levels, but subclasses can 
		// make it better
		int comma = input.indexOf(splitBy);
		if (comma == -1 || comma+1 >= input.length()) {
			return new String[]{input};
		}
		else {
			String surname = input.substring(0, comma);
			String other = input.substring(comma+1).trim();
			String[] ret = new String[2];
			ret[0] = surname + ", " + other.substring(0, 1);
			ret[1] = input;
			return ret;
		}
	}

	private String doFurtherFacetNormalizations(String input) {
		
		// get ascii version
		input = AuthorUtils.foldToAscii(input);

		/*
		// turn alone-standing letters into Uppercase
		char[] chars = input.toCharArray();
		for (int i=0;i<chars.length;i++) {
			if (Character.isSpaceChar(chars[i])) {
				// look to the left
				if (i-1 >= 0) {
					if (i-2 < 0 || Character.isSpaceChar(chars[i-2])) {
						chars[i-1] = Character.toUpperCase(chars[i-1]);
					}
				}
				// look to the right
				if (i+1 == chars.length || (i+2 <= chars.length && Character.isSpaceChar(chars[i+2]))) {
					chars[i+1] = Character.toUpperCase(chars[i+1]);
				}
				i += 2;
			}
		}
		*/
		// turn first letters uppercase
		char[] chars = input.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		for (int i=1;i<chars.length;i++) {
			if (!Character.isAlphabetic(chars[i])) {
				if (i+1 < chars.length && Character.isLowerCase(chars[i+1])) {
					chars[i+1] = Character.toUpperCase(chars[i+1]);
					i += 1;
				}
			}
		}
		
		return String.copyValueOf(chars);
		
  }
	
}
