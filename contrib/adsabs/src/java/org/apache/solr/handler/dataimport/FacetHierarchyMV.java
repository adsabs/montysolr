/**
 * 
 */
package org.apache.solr.handler.dataimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;


/**
 *	Variation on the FacetHierarchy but instead of generating
 *  variations of the field, this one will combine them
 * 
 * Input:
 * sourceValues: [["foo", "bar"],["woo", "too"]]
 * 
 * Output:
 * newFacets:  ["0/foo", "0/bar", "1/foo/woo", "1/bar/too"] *  
 */
public class FacetHierarchyMV extends FacetHierarchy {
	
	public FacetHierarchyMV(String columnName, String[] source) {
		super(columnName, source);
	}

	@Override
	public void addFacets(Map<String, Object> row) {
		
		int smallestCommonDenominator = -1;
		
		ArrayList<List<String>> toInclude = new ArrayList<List<String>>();
		for (SourceField field : sourceFields) {
			if (row.containsKey(field.name)) {
				Object obj = row.get(field.name);
				
				if (obj == null) {
					return; // give up
				}
				
				List<String> data;
				if (obj instanceof List) {
					if (((List) obj).get(0) instanceof Map) {
						Map<String, Object> sourceData = (Map<String, Object>) ((List) obj).get(0);
						data = new ArrayList<String>();
						for (String k: field.keys) {
							if (!sourceData.containsKey(k)) {
								return; //give up
							}
							data.add((String) sourceData.get(k));
						}
					}
					else {
						data = (List<String>) obj;
					}
				}
				else {
					throw new SolrException(ErrorCode.BAD_REQUEST, "The received data is junk (cause i can't make sense of it): "  + obj);
				}
					
				
				if (smallestCommonDenominator == -1 || data.size() < smallestCommonDenominator) {
					smallestCommonDenominator = data.size();
				}
				toInclude.add(data); // non-multivalued fields will cause Exception, that's fine
			}
		}
		
		if (toInclude.size() == 0) {
			return;
		}
		
		// transform data from rows into cols
		String[][] dataCols = new String[smallestCommonDenominator][toInclude.size()];
		for (int i=0;i<smallestCommonDenominator;i++) {
			for (int j=0;j<toInclude.size();j++) {
				dataCols[i][j] = toInclude.get(j).get(i);
			}
		}
		
		ArrayList<String> newFacets = new ArrayList<String>();
		
		for (String[] parts: dataCols) {
		
			for (int depth = 0; depth < parts.length; depth++) {
				
				StringBuilder newFacet = new StringBuilder(); 
				
				for (int i = 0; i <= depth; i++) {
					if (i == 0) {
						newFacet.append(String.format("%d", depth));
					}
					newFacet.append("/");
					newFacet.append(parts[i]);
				}
				
				newFacets.add(newFacet.toString());
			}
		}
		
		if (newFacets.size() > 0) {
			if (row.containsKey(columnName)) {
				List<String> output = (List<String>) row.get(columnName);
				output.addAll(newFacets);
			}
			else {
				row.put(columnName, newFacets);
			}
		}
	}
	
}
