package org.apache.solr.handler.dataimport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Is called by the DIH to transform document, during the first
 * call we'll  initialize facet configuration 
 *
 */
public class FacetHierarchyTransformer extends Transformer {

	private final static String FACET_HIERARCHY_ATTR = "facetHierarchyField";
	private final static String FACET_HIERARCHY_INPUT_ATTR = "input";
	private final static String FIELD_COLUMN_ATTR = "column";
	
	Logger log = LoggerFactory.getLogger(FacetHierarchyTransformer.class);
	private List<FacetHierarchy> facetHierarchies;

	@Override
	public Object transformRow(Map<String, Object> row, Context context) {
		
		for (FacetHierarchy fh : getFacetHierarchiers(context)) {
			try {
				fh.addFacets(row);
			} catch (RuntimeException e) {
				IndexSchema schema = context.getSolrCore().getLatestSchema();
				String idFieldName = schema.getUniqueKeyField().getName();
				log.debug(
						"There was a problem generating hierarchical facets for record {}: {}",
						row.get(idFieldName),
						e.getMessage()
					);
			}
		}
		
		return row;
	}
	
	private List<FacetHierarchy> getFacetHierarchiers(Context context) {
		if (facetHierarchies == null) {
			initializeFacetHierarchies(context);
		}
		return facetHierarchies;
	}
	
	private void initializeFacetHierarchies(Context context) {
		
		List<Map<String, String>> allFields = context.getAllEntityFields();
		facetHierarchies = new ArrayList<FacetHierarchy>();
		
		for (Map<String, String> fieldDef : allFields) {
			if ("true".equals(fieldDef.get(FACET_HIERARCHY_ATTR))) {
				
				String columnName = fieldDef.get(FIELD_COLUMN_ATTR);
				assert columnName != null;
				
				
				
				String input = fieldDef.get(FACET_HIERARCHY_INPUT_ATTR);
				assert input != null;
				
				FacetHierarchy fh = null;
				if (input.contains("+")) {
					fh = new FacetHierarchyMV(columnName, fieldDef.get(FACET_HIERARCHY_INPUT_ATTR).split("\\+"));
				} else {
					fh = new FacetHierarchy(columnName, fieldDef.get(FACET_HIERARCHY_INPUT_ATTR).split("\\,"));
				}
				facetHierarchies.add(fh);
			}
		}
	}
}
