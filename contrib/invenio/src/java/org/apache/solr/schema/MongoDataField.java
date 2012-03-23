package org.apache.solr.schema;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	public static final Logger log = LoggerFactory.getLogger(TextField.class);

	private String collectionName = null;
	private String databaseName = null;
	private String mongoFieldName = null;
	
	@Override
	protected void init(IndexSchema schema, Map<String,String> args) {
		this.collectionName = args.get("collectionName");
		this.databaseName = args.get("databaseName");
		this.mongoFieldName = args.get("mongoFieldName");
        log.info("initialized using db: " + this.databaseName + ", collection: " + this.collectionName);
	    args.remove("collectionName");
	    args.remove("databaseName");
	    args.remove("mongoFieldName");
	    super.init(schema, args);
	}
	
	@Override
	public boolean isPolyField() {
	    return true;   // really only true if the field is indexed
	}
	  
	public Fieldable[] createFields(SchemaField field, String externalVal, float boost) {

		Mongo m = MongoConnection.getInstance();
		DB db = m.getDB(this.databaseName);
		DBCollection collection = db.getCollection(this.collectionName);
		if (this.mongoFieldName == null) {
			this.mongoFieldName = field.name;
		}
		
		log.info("using field name: " + this.mongoFieldName);
		
		BasicDBObject query = new BasicDBObject();
		query.put("bibcode", externalVal);
		log.info("query: " + query);
		DBObject doc = collection.findOne(query);
		log.info("doc: " + doc);
		
		if (doc == null) {
			return new Field[0];
		}
		else {
			
			ArrayList<String> readers = (ArrayList<String>) doc.get(this.mongoFieldName);
			log.info("readers: " + readers);
			Field[] fields = new Field[readers.size()];
		
			for (int i = 0; i < readers.size(); i++) {
				log.info("creating field for " + readers.get(i));
				fields[i] = this.createField(field, readers.get(i), boost);
			}
		
			return fields;
		}
	}
}
