package org.apache.solr.handler.dataimport;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.solr.common.SolrException;
import org.bson.BSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.DBCollection;

/**
 * Loads data from the MongoDB, this class extracts 
 * bibcodes (970__a) from the data that was retrieved
 * by the parent DataSource. We do one query per every
 * batch (NOT per every doc)
 * 
 * {@link AdsDataSource} makes sure that the MongoDB is initialized
 * before being used by the data-importer.
 * 
 */

public class AdsDataSource extends InvenioDataSource {

	Logger log = LoggerFactory.getLogger(AdsDataSource.class);

	public final static String MONGO_DOC_ID = "mongoDocIdField";
	public final static String MONGO_HOST = "mongoHost";
	public final static String MONGO_PORT = "mongoPort";
	public final static String MONGO_DB_NAME = "mongoDBName";
	public final static String MONGO_DB_USER = "mongoUser";
	public final static String MONGO_DB_PASS = "mongoPass";
	public final static String MONGO_COLLECTION_NAME = "mongoCollectionName";
	public final static String MONGO_FIELD_NAME_ATTR = "mongoFieldName";
	public final static String MONGO_FIELD_ATTR = "mongoField";
	
	
	protected String mongoDocIdField;
	protected Map<String, Map<String, Object>> mongoCache = new HashMap<String, Map<String, Object>>();
	protected BasicDBObject mongoFields;
	protected Map<String,String> fieldColumnMap;
	protected MongoClient mongo;
	protected DB db;
	protected DBCollection mainColl;
	
	
	@Override
	public void init(Context context, Properties initProps) {
		super.init(context, initProps);
		initMongo(context, initProps);
		
		List<Map<String, String>> fields = context.getAllEntityFields();
		
		mongoDocIdField = initProps.getProperty(MONGO_DOC_ID);
		fieldColumnMap = new HashMap<String,String>();
		mongoFields = new BasicDBObject();
		mongoFields.put("_id", 1);
		mongoFields.put(mongoDocIdField, 1);
		
		for (Map<String, String> field : fields) {
			if ("true".equals(field.get(MONGO_FIELD_ATTR))) {
				String mongoFieldName = field.get(MONGO_FIELD_NAME_ATTR);
				String columnName = field.get("column");
				
				if (mongoFieldName == null) {
					mongoFieldName = columnName;
				}
				mongoFields.put(mongoFieldName, 1);
				fieldColumnMap.put(columnName, mongoFieldName);
			}
		}
	}
	
	protected void initMongo(Context context, Properties initProps) {
		
		Map<String, Object> params = context.getRequestParameters();
		
		String mongoHost = params.containsKey(MONGO_HOST) ? (String) params.get(MONGO_HOST) : initProps.getProperty(MONGO_HOST);
		int mongoPort = params.containsKey(MONGO_PORT) ? Integer.parseInt((String)params.get(MONGO_PORT)) : Integer.parseInt(initProps.getProperty(MONGO_PORT));
		String mongoDBName = params.containsKey(MONGO_DB_NAME) ? (String) params.get(MONGO_DB_NAME) : initProps.getProperty(MONGO_DB_NAME);
		String mongoCollectionName = params.containsKey(MONGO_COLLECTION_NAME) ? (String) params.get(MONGO_COLLECTION_NAME) : initProps.getProperty(MONGO_COLLECTION_NAME);
		String mongoUser = params.containsKey(MONGO_DB_USER) ? (String) params.get(MONGO_DB_USER) : initProps.getProperty(MONGO_DB_USER);
		String mongoPass = params.containsKey(MONGO_DB_PASS) ? (String) params.get(MONGO_DB_PASS) : initProps.getProperty(MONGO_DB_PASS);
		
		try {
			if (mongoUser != null && mongoUser.length() > 0 && mongoPass != null && mongoPass.length() > 0) {
				ArrayList<MongoCredential> creds = new ArrayList<MongoCredential>();
				creds.add(MongoCredential.createMongoCRCredential(mongoUser, mongoDBName, mongoPass.toCharArray()));
				mongo = new MongoClient(new ServerAddress(mongoHost, mongoPort), creds);
			}
			else {
				mongo = new MongoClient(new ServerAddress(mongoHost, mongoPort));
			}
			db = mongo.getDB(mongoDBName);
			mainColl = db.getCollection(mongoCollectionName);
			mainColl.findOne(); // check we indeed have a connection
		} catch (Exception e) {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, 
			    e);
		}
	}
	

	@Override
	public Reader getData(String query) {
		mongoCache.clear();
		
		Reader data = super.getData(query);
		if (data != null) {
			
		  // do not bother if there are no mongo fields requested
			if (fieldColumnMap.size()==0) return data;
			
			// otherwise parse the data, find bibcodes, query mongo
			// and return string wrapped in stringreader, it is more
			// expensive, but likely MUCH faster than loading every
			// document individually
			
			StringBuilder buffer = new StringBuilder();
			
			// harvest all data for given bibcodes from mongo in one go
			Scanner scanner = new java.util.Scanner(data);
			scanner.useDelimiter("<record>");
			List<String> bibcodes = new ArrayList<String>();
			
			while (scanner.hasNext()) {
				String record = scanner.next();
				
				int pos = record.indexOf("tag=\"970\"");
				
				if (pos == -1) {
					buffer.append(record);
					continue;
				}
				else {
					buffer.append("<record>");
					buffer.append(record);
				}
				
				
				String bibcode = record.substring(record.indexOf("code=\"a\">", pos)+9, record.indexOf("</", pos+13));
				bibcode = bibcode.trim();
        bibcode = StringEscapeUtils.unescapeHtml(bibcode);
				bibcodes.add(bibcode);
			}

			if (bibcodes.size() > 0)
				populateMongoCache(bibcodes);
      
			try {
	      data.close();
      } catch (IOException e) {
	      log.error(e.getMessage());
      }
			return new StringReader(buffer.toString());
			
		}
		return data;
	}
	
	protected void populateMongoCache(List<String> bibcodes) {
		BasicDBObject mongoQuery = new BasicDBObject();
		
		mongoQuery.put(mongoDocIdField, new BasicDBObject("$in", bibcodes));
		//mongoQuery.put(mongoDocIdField, new BasicDBObject("$in", new String[]{"2009arXiv0909.1287I","1987PhRvD..36..277B"}));
		
		DBCursor cursor = mainColl.find(mongoQuery, mongoFields);
		Iterator<DBObject> it = cursor.iterator();
		Object v;
		String mongoField;
		HashMap<String, Map<Object, String>> collectionsToFetch = new HashMap<String, Map<Object, String>>();
		HashMap<String, Map<String, String>> collectionsToFetchFields = new HashMap<String, Map<String, String>>();
		
		while (it.hasNext()) {
			DBObject doc = it.next();
			String docId = (String) doc.get(mongoDocIdField);
			assert docId != null;
			
			Map<String, Object> row = new HashMap<String, Object>();
			
			for (String column : fieldColumnMap.keySet()) {
				mongoField = fieldColumnMap.get(column);
				
				if (doc.containsField(mongoField)) {
					v = doc.get(mongoField);
					if (v instanceof DBRef) { // 
						DBRef o = (DBRef) v;
						
						// we'll collect the references to other collections and retrieve them in another big sweep
						// later - cause getting them one by one is stupid/slow...
						
						// we have to find out what fields are references; the ids of the references and then save
						// the link between our original docs' <-> refd id. I know it is more work, but it is 
						// defensive coding; I don't want to rely on the assumption that the references are always
						// linked through the ID of the primary document. ...
						
						System.out.println(o.fetch());
						String colName = o.getRef(); //("$ref").toString(); // in which collection the new value is
						Object refObjId = o.getId(); //get("$id").toString(); // what ID in the new collection the object has
						
						if (!collectionsToFetch.containsKey(colName)) {
							collectionsToFetch.put(colName, new HashMap<Object, String>());
							collectionsToFetchFields.put(colName, new HashMap<String, String>());
						}
						
						collectionsToFetch.get(colName).put(refObjId, docId);
						collectionsToFetchFields.get(colName).put(mongoField, column);
							
					}
					else {
						row.put(column, v);
					}
				}
			}
			mongoCache.put(docId, row);
		}
		cursor.close();
		
		// now fetch data from referenced collection(s)
		if (collectionsToFetch.size() > 0) {
			for (Entry<String, Map<Object,String>> entry: collectionsToFetch.entrySet()) {
				
				String collectionName = entry.getKey(); // the other collection name
				Map<Object, String> refId2DocId = entry.getValue(); // how to connect values back to the original doc
				Map<String, String> mongoField2SolrDocField = collectionsToFetchFields.get(collectionName);
				
				// prepare the query
				BasicDBObject otherCollQuery = new BasicDBObject();
				String[] ids = entry.getValue().values().toArray(new String[entry.getValue().values().size()]);
				otherCollQuery.put("_id", new BasicDBObject("$in", ids));
				BasicDBObject otherFields = new BasicDBObject();
				for (String field: mongoField2SolrDocField.keySet()) {
					otherFields.put(field, 1);
				}
				
				
				// get the values
				cursor = db.getCollection(collectionName).find(otherCollQuery, otherFields);
				
				// and insert them back into the solr doc (using the solr fieldname)
				it = cursor.iterator();
				while (it.hasNext()) {
					DBObject newData = it.next();
					Map<String, Object> docToUpdate = mongoCache.get(refId2DocId.get(newData.get("_id")));
					for (String column : mongoField2SolrDocField.keySet()) {
						docToUpdate.put(mongoField2SolrDocField.get(column), newData.get(column));
					}
				}
				
				cursor.close();
			}
		}
  }

	@Override
	public void close() {
		super.close();
		mongo.close();
	}
	
	
	public Map<String, String> getFieldColumnMap() {
		return fieldColumnMap;
	}

	/*
	public DBObject getMongoDoc(String docId) {
		BasicDBObject query = new BasicDBObject();
		query.put(mongoDocIdField, docId);
		return mainColl.findOne(query, mongoFields);
	}
	*/
	
	
	/**
	 * this must be called from outside, by some
	 * of the transformers
	 * 
	 * @param row
	 */
	public void transformRow(Map<String, Object> row) {
		
		String docId = (String) row.get(mongoDocIdField);
		if (mongoCache.containsKey(docId)) {
			row.putAll(mongoCache.get(docId));
		}
	  
  }
	
}
