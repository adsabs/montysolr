package org.apache.solr.handler.dataimport;

import java.util.Map;

import com.mongodb.DBObject;

public class MongoTransformer extends Transformer {

//	private Mongo mongo = null;
	
	@Override
	public Object transformRow(Map<String, Object> row, Context context) {
		
		AdsDataSource ds = (AdsDataSource) context.getDataSource();
		Map<String,String> fieldColumnMap = ds.getFieldColumnMap();		// mapping mongo field names -> solr schema names
		
		// do not bother if there are no mongo fields requested
		if (fieldColumnMap.size()==0) return row;
		
		String docId = (String) row.get(ds.getMongoDocIdField());
		DBObject doc = ds.getMongoDoc(docId);
		
		if (doc != null) {
			for (String column : fieldColumnMap.keySet()) {
				String mongoField = fieldColumnMap.get(column);
				row.put(column, doc.get(mongoField));
			}
		}
		
		return row;
	}

}
