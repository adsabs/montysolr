/**
 * 
 */
package org.apache.solr.handler.dataimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author jluker
 *
 */
public class FacetHierarchy {
	
	Logger log = LoggerFactory.getLogger(FacetHierarchy.class);
	
	String columnName;
	List<List<String>> sourceFields;
	
	public FacetHierarchy(String columnName, String[] source) {
		this.columnName = columnName;
		this.sourceFields = new ArrayList<List<String>>();
		
		for (String s : source) {
			List<String> fieldNames = new ArrayList<String>();
			if (s.contains("+")) {
				fieldNames.addAll(Arrays.asList(s.split("\\+")));
			} else {
				fieldNames.add(s);
			}
			sourceFields.add(fieldNames);
		}
	}
	
	public void addFacets(Map<String, Object> row) {
		
		List<String> newFacets = new ArrayList<String>();
		List<String> sourceValues = new ArrayList<String>();
		
		for (List<String> fields : sourceFields) {
			for (String sourceField : fields) {
				String sourceValue = (String) row.get(sourceField);
				if (sourceValue == null) {
					throw new RuntimeException("source was null");
				} else if (sourceValue == "") {
					throw new RuntimeException("source value array contains empty strings");
				}
				sourceValues.add(sourceValue);
			}
		}
		
		generateFacets(sourceValues, newFacets);
		row.put(columnName, newFacets);
	}
	
	/*
	 * Creates new hierarchical-style facet values based on sourceValues.
	 * sourceValue input is a list of lists; one list of values for each level of the hierarchy.
	 * Example using a two-level hierarchy of author_norm -> author:
	 * 
	 * Input:
	 * sourceValues: [["Smith, J", "Jones, B"],["Smith, James", "Jones, Brenda"]]
	 * 
	 * Output:
	 * newFacets:  ["0/Smith, J", "0/Jones, B", "1/Smith, J/Smith, James", "1/Jones, B/Jones, Brenda"]
	 * 
	 * See: http://wiki.apache.org/solr/HierarchicalFaceting#A.27facet.prefix.27__Based_Drill_Down
	 */
	private void generateFacets(List<String> sourceValues, List<String> newFacets) {
		
		for (int depth = 0; depth < sourceFields.size(); depth++) {
			
			String newFacet = null;
			
			for (int i = 0; i <= depth; i++) {
				if (newFacet == null) {
					newFacet = String.format("%d", depth);
				}
				newFacet += String.format("/%s", (String) sourceValues.get(i));
			}
			newFacets.add(newFacet);
		}
	}
}
