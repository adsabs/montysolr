package org.apache.solr.handler.dataimport;

import java.util.List;
import java.util.Map;

import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacetHierarchyTransformer extends Transformer {

	Logger log = LoggerFactory.getLogger(FacetHierarchyTransformer.class);

	@Override
	public Object transformRow(Map<String, Object> row, Context context) {
		
		AdsDataSource ds = (AdsDataSource) context.getDataSource();
		List<FacetHierarchy> facetHierarchies = ds.getFacetHierarchies();
		
		for (FacetHierarchy fh : facetHierarchies) {
			try {
				fh.addFacets(row);
			} catch (RuntimeException e) {
				IndexSchema schema = context.getSolrCore().getSchema();
				String idFieldName = schema.getUniqueKeyField().getName();
				log.warn(
					String.format(
						"There was a problem generating hierarchical facets for record %s: %s",
						row.get(idFieldName),
						e.getMessage())
					);
			}
		}
		
		return row;
	}
}
