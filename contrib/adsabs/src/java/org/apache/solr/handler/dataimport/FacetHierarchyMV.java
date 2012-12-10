/**
 * 
 */
package org.apache.solr.handler.dataimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author jluker
 *
 */
public class FacetHierarchyMV extends FacetHierarchy {
	
	public FacetHierarchyMV(String columnName, String[] source) {
		super(columnName, source);
	}

	@Override
	public void addFacets(Map<String, Object> row) {
		
		List<String> newFacets = new ArrayList<String>();
		List<List<String>> sourceValues = new ArrayList<List<String>>();
		
		int expectedSourceLength = -1;
		for (List<String> fields : sourceFields) {
			List<String> sv = new ArrayList<String>();
			for (String sourceField : fields) {
				List<String> rowValue = (List<String>) row.get(sourceField);
				if (rowValue == null) {
					throw new RuntimeException("source was null");
				}
				sv.addAll(rowValue);
			}	
			int len = sv.size();
			if (expectedSourceLength == -1) {
				expectedSourceLength = len;
			} else if (len != expectedSourceLength) {
				throw new RuntimeException("source value arrays have inconsistent length");
			}
			if (sv.contains("")) {
				throw new RuntimeException("source value array contains empty strings");
			}
			sourceValues.add(sv);
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
	private void generateFacets(List<List<String>> sourceValues, List<String> newFacets) {
		
		for (int depth = 0; depth < sourceFields.size(); depth++) {
			
			String[] newFacetArray;
			int len = sourceValues.get(0).size();
			newFacetArray = new String[len];
			
			for (int i = 0; i <= depth; i++) {
				List<String> sv = sourceValues.get(i);
				for (int j = 0; j < sv.size(); j++) {
					if (newFacetArray[j] == null) {
						newFacetArray[j] = String.format("%d", depth);
					}
					newFacetArray[j] += String.format("/%s", sv.get(j));
				}
			}
			newFacets.addAll(Arrays.asList(newFacetArray));
		}
	}
}
