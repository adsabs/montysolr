package org.apache.solr.handler.dataimport;

import java.net.UnknownHostException;
import java.util.Map;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class MongoTransformer extends Transformer {

	@Override
	public Object transformRow(Map<String, Object> row, Context context) {
		
		AdsDataSource ds = (AdsDataSource) context.getDataSource();
		BasicDBObject mongoFields = ds.getMongoFields();				// the fields we want to selectively fetch
		Map<String,String> fieldColumnMap = ds.getFieldColumnMap();		// mapping mongo field names -> solr schema names
		
		DBObject doc = null;
		try {
			/*
			 * connecting to mongo every row like this is dumb, but it's the only way
			 * to keep the threaded test-framework from complaining. There may be a fix 
			 * coming down the mongo dev pipe eventually: https://jira.mongodb.org/browse/JAVA-595
			 * (assuming that's actually related to the issue I'm seeing)
			 */
			Mongo mongo = (new Mongo.Holder()).connect(ds.getMongoURI());
			DB db = mongo.getDB(ds.getMongoDBName());
			DBCollection collection = db.getCollection(ds.getMongoCollectionName());
			
			BasicDBObject query = new BasicDBObject();
			String docIdField = ds.getMongoDocIdField();
			query.put(docIdField, row.get(docIdField));
			
			doc = collection.findOne(query, mongoFields);
			mongo.close();
			
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if (doc != null) {
			for (String column : fieldColumnMap.keySet()) {
				String mongoField = fieldColumnMap.get(column);
				row.put(column, doc.get(mongoField));
			}
		}
		
		return row;
	}

}
