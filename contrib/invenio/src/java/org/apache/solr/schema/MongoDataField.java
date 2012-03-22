package org.apache.solr.schema;

import java.util.ArrayList;
import java.util.Map;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;

import org.ads.mongodb.MongoConnection;
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
	
	private String collectionName = null;
	private String databaseName = null;
	private String mongoFieldName = null;
	
	@Override
	protected void init(IndexSchema schema, Map<String,String> args) {
		this.collectionName = args.get("collectionName");
		this.databaseName = args.get("databaseName");
		this.mongoFieldName = args.get("mongoFieldName");
	    args.remove("collectionName");
	    args.remove("databaseName");
	    args.remove("mongoFieldName");
	    super.init(schema, args);
	}
	
	public Fieldable[] createFields(SchemaField field, String externalVal, float boost) {

		Mongo m = MongoConnection.getInstance();
		DB db = m.getDB(this.databaseName);
		DBCollection collection = db.getCollection(this.collectionName);
		if (this.mongoFieldName != null) {
			this.mongoFieldName = field.name;
		}
		
		BasicDBObject query = new BasicDBObject();
		query.put("bibcode", externalVal);
		DBObject doc = collection.findOne(query);
		
		ArrayList<String> readers = (ArrayList<String>) doc.get(this.mongoFieldName);
		Field[] fields = new Field[readers.size()];
		
		for (int i = 0; i < readers.size(); i++) {
			fields[i] = this.createField(field, readers.get(i), boost);
		}
		
		return fields;
	}
}
