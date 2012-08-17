package org.apache.solr.schema;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexableField;

import org.adsabs.mongodb.MongoConnection;
import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * <code>TextField</code> is the basic type for configurable text analysis.
 * Analyzers for field types using this implementation should be defined in the
 * schema.
 * 
 * {@link MongoDataField} is an extension for retrieving values from a MonogDB instance.
 */
public class MongoDataField extends TextField {
	
	public static final Logger log = LoggerFactory.getLogger(TextField.class);

	private static final String DEFAULT_QUERY_KEY = "bibcode";
	
	private String collectionName = null;
	private String databaseName = null;
	private String mongoFieldName = null;
	private boolean polyField = false;
	
	@Override
	protected void init(IndexSchema schema, Map<String,String> args) {
		this.collectionName = args.get("collectionName");
	    args.remove("collectionName");
		this.databaseName = args.get("databaseName");
	    args.remove("databaseName");
		this.mongoFieldName = args.get("mongoFieldName");
	    args.remove("mongoFieldName");
		this.polyField = Boolean.parseBoolean(args.get("isPolyField"));
        log.info("polyField: " + this.polyField);
	    args.remove("isPolyField");
        log.info("initialized using db: " + this.databaseName + ", collection: " + this.collectionName);
	    super.init(schema, args);
	}
	
	@Override
	public boolean isPolyField() {
		return this.polyField;
	}
	  
	@Override
	public IndexableField createField(SchemaField field, Object externalVal, float boost) {

		String fieldName = this.mongoFieldName != null
			? this.mongoFieldName
			: field.name;
		
		DBObject doc = this.executeMongoQuery((String)externalVal, fieldName);
		
		if (doc != null) {
			String val = (String) doc.get(fieldName);
			
			if (val != null)
				return super.createField(field, val, boost);
		}

		return null;
	}
	
	@Override
	public IndexableField[] createFields(SchemaField field, Object externalVal, float boost) {

		String fieldName = this.mongoFieldName != null
			? this.mongoFieldName
			: field.name;
			
		DBObject doc = this.executeMongoQuery((String)externalVal, fieldName);
		
		if (doc != null) {
			ArrayList<String> data = (ArrayList<String>) doc.get(fieldName);
			
			if (data != null) {
			  IndexableField[] fields = new Field[data.size()];
				
				for (int i = 0; i < data.size(); i++) {
					fields[i] = super.createField(field, data.get(i), boost);
				}
		
				return fields;
			}
		}
		
		return new IndexableField[0];
	}
		
	public DBObject executeMongoQuery(String externalVal, String fieldName) {
		
		Mongo m = MongoConnection.getInstance();
		DB db = m.getDB(this.databaseName);
		DBCollection collection = db.getCollection(this.collectionName);
		
		BasicDBObject query = new BasicDBObject();
		query.put(DEFAULT_QUERY_KEY, externalVal);
		//log.debug("query: " + query);
		
		BasicDBObject wanted = new BasicDBObject();
		wanted.put(fieldName, 1);
		
		DBObject doc = collection.findOne(query, wanted);
		
		return doc;
	}
		
}
