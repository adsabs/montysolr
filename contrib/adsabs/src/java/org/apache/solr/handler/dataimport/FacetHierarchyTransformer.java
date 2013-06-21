package org.apache.solr.handler.dataimport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacetHierarchyTransformer extends Transformer {

	private final static String FACET_HIERARCHY_FIELD_ATTR = "facetHierarchyField";
	private final static String FACET_HIERARCHY_FIELD_NAMES_ATTR = "fields";
	private final static String FACET_HIERARCHY_FIELD_MULTIVALUE_ATTR = "multiValuedSource";
	private final static String FIELD_COLUMN_ATTR = "column";
	
	Logger log = LoggerFactory.getLogger(FacetHierarchyTransformer.class);
	private List<FacetHierarchy> facetHierarchies;

	@Override
	public Object transformRow(Map<String, Object> row, Context context) {
		
		for (FacetHierarchy fh : getFacetHierarchiers(context)) {
			try {
				fh.addFacets(row);
			} catch (RuntimeException e) {
				IndexSchema schema = context.getSolrCore().getSchema();
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
		if (facetHierarchies != null)
			return facetHierarchies;
		facetHierarchies = createFacetHierarchies(context);
		return facetHierarchies;
	}
	
	private List<FacetHierarchy> createFacetHierarchies(Context context) {
		
		List<Map<String, String>> allFields = context.getAllEntityFields();
		List<FacetHierarchy> result = new ArrayList<FacetHierarchy>();
		
		for (Map<String, String> fieldDef : allFields) {
			if ("true".equals(fieldDef.get(FACET_HIERARCHY_FIELD_ATTR))) {
				String columnName = fieldDef.get(FIELD_COLUMN_ATTR);
				boolean multiValueSource = "true".equals(fieldDef.get(FACET_HIERARCHY_FIELD_MULTIVALUE_ATTR));
				
				String[] sourceFields = fieldDef.get(FACET_HIERARCHY_FIELD_NAMES_ATTR).split(",");
				if (sourceFields.length < 2) {
					throw new RuntimeException("Facet hierarchy requires > 1 field");
				}
				
				FacetHierarchy fh = null;
				if (multiValueSource) {
					fh = new FacetHierarchyMV(columnName, sourceFields);
				} else {
					fh = new FacetHierarchy(columnName, sourceFields);
				}
				result.add(fh);
			}
		}
		return result;
	}
}
