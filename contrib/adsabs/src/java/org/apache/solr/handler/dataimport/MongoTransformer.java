package org.apache.solr.handler.dataimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.solr.handler.dataimport.FacetHierarchy.SourceField;


/*
 * This class can be used to modify loaded mongo
 * records. For example, to extracted nested fields.
 * Remember, that it will see field names already
 * transformed.
 */

public class MongoTransformer extends Transformer {

	
  private Map<SourceField,String> mapToList;
	
	@SuppressWarnings("serial")
  public MongoTransformer() {
		super();
		/**
		 * a workaround for modifying data
		 * loaded from Mongo - it is hardcoded (but
		 * I can do it better once we stabilize with 
		 * regard to MongoDB import pipeline)
		 *
		 */
		mapToList = new HashMap<SourceField, String>() {{
			put(new SourceField("tmp_grant", new String[]{"agency","grant"}), "grant");
		}};
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
	public Object transformRow(Map<String, Object> row, Context context) {
		
	  for (Entry<SourceField, String> en: mapToList.entrySet()) {
	  	SourceField sourceField = en.getKey();
	  	String targetField = en.getValue();
	  	if (row.containsKey(sourceField.name) && row.get(sourceField.name) instanceof List) {
	  		List<Map> sourceValue = (List<Map>) row.get(sourceField.name);
	  		List target;
	  		if (row.containsKey(targetField)) {
	  			target = (List) row.get(targetField);
	  		}
	  		else {
	  			target = new ArrayList<String>();
	  			row.put(targetField, target);
	  		}
	  		if (target instanceof List) {
  				for (Map sv: sourceValue) {
  					for (String k: sourceField.keys) {
		  				if (sv.containsKey(k)) {
		  					target.add((String) sv.get(k));
		  				}
	  				}
	  			}
	  			
	  		}
	  	}
	  }
	  
		return row;
	}

}
