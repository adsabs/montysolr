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
	String[] fields;
	boolean multiValueSource = false;
	
	public FacetHierarchy(String columnName, String[] fields, boolean multiValueSource) {
		this.columnName = columnName;
		this.fields = fields;
		this.multiValueSource = multiValueSource;
	}
	
	public void addFacets(Map<String, Object> row) {
		
		List<String> newFacets = new ArrayList<String>();
		List<Object> sourceValues = new ArrayList<Object>();
		for (String f : fields) {
			Object source = row.get(f);
			if (source == null) {
				throw new RuntimeException("source was null");
			}
			sourceValues.add(row.get(f));
		}
		
		if (multiValueSource) {
			int expectedSourceLength = -1;
			for (int i = 0; i < sourceValues.size(); i++) {
				List<String> sv = (List<String>) sourceValues.get(i);
				int len = sv.size();
				if (expectedSourceLength == -1) {
					expectedSourceLength = len;
				} else if (len != expectedSourceLength) {
					throw new RuntimeException("source value arrays have inconsistent length");
				}
				if (sv.contains("")) {
					throw new RuntimeException("source value array contains empty strings");
				}
			}
		} else {
			if (sourceValues.contains("")) {
				throw new RuntimeException("source value array contains empty strings");
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
	private void generateFacets(List<Object> sourceValues, List<String> newFacets) {
		
		for (int depth = 0; depth < fields.length; depth++) {
			
			String[] newFacetArray;
			if (multiValueSource) {
				int len = ((List<String>) sourceValues.get(0)).size();
				newFacetArray = new String[len];
			} else {
				newFacetArray = new String[1];
			}
			
			for (int i = 0; i <= depth; i++) {
				if (multiValueSource) {
					List<String> sv = (List<String>) sourceValues.get(i);
					for (int j = 0; j < sv.size(); j++) {
						if (newFacetArray[j] == null) {
							newFacetArray[j] = String.format("%d", depth);
						}
						newFacetArray[j] += String.format("/%s", sv.get(j));
					}
					
				} else {
					if (newFacetArray[0] == null) {
						newFacetArray[0] = String.format("%d", depth);
					}
					newFacetArray[0] += String.format("/%s", (String) sourceValues.get(i));
				}
			}
			newFacets.addAll(Arrays.asList(newFacetArray));
		}
	}
}
