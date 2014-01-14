package org.apache.solr.handler.dataimport;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.solr.common.SolrException;
import org.apache.solr.core.CloseHook;
import org.apache.solr.core.SolrCore;
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
	public final static String MAIN_ID_ATTR = "mongoGetIdsFrom";
	
	
	
	protected Map<String, Map<String, Object>> mongoCache = new HashMap<String, Map<String, Object>>();
	private MongoConnection mc;
	
	/*
	protected String mongoDocIdField;
	protected BasicDBObject mongoFields;
	protected Map<String,String> fieldColumnMap;
	protected MongoClient mongo;
	protected DB db;
	protected DBCollection mainColl;
	*/

	
	
	
	@Override
	public void init(Context context, Properties initProps) {
		super.init(context, initProps);
		mc = initMongo(context, initProps);
		
	}
	
	protected MongoConnection initMongo(Context context, Properties initProps) {
		
		String key = this.getClass().getSimpleName() + "#MongoConnection";
		Object conn = context.getSessionAttribute(key, Context.SCOPE_SOLR_CORE);
		
		
		if (conn instanceof MongoConnection) {
			MongoConnection mc = (MongoConnection) conn;
			if (mc.db != null)
				return mc;
		}
		
		final MongoConnection mc = new MongoConnection();
		context.setSessionAttribute(key, mc, Context.SCOPE_SOLR_CORE);
		
		Map<String, Object> params = context.getRequestParameters();
		
		String mongoHost = params.containsKey(MONGO_HOST) ? (String) params.get(MONGO_HOST) : initProps.getProperty(MONGO_HOST);
		int mongoPort = params.containsKey(MONGO_PORT) ? Integer.parseInt((String)params.get(MONGO_PORT)) : Integer.parseInt(initProps.getProperty(MONGO_PORT));
		String mongoDBName = params.containsKey(MONGO_DB_NAME) ? (String) params.get(MONGO_DB_NAME) : initProps.getProperty(MONGO_DB_NAME);
		String mongoCollectionName = params.containsKey(MONGO_COLLECTION_NAME) ? (String) params.get(MONGO_COLLECTION_NAME) : initProps.getProperty(MONGO_COLLECTION_NAME);
		String mongoUser = params.containsKey(MONGO_DB_USER) ? (String) params.get(MONGO_DB_USER) : initProps.getProperty(MONGO_DB_USER);
		String mongoPass = params.containsKey(MONGO_DB_PASS) ? (String) params.get(MONGO_DB_PASS) : initProps.getProperty(MONGO_DB_PASS);
		String mainIdField = params.containsKey(MAIN_ID_ATTR) ? (String) params.get(MAIN_ID_ATTR) : initProps.getProperty(MAIN_ID_ATTR);
		
		String mongoDocIdField;
		BasicDBObject mongoFields;
		Map<String,String> mongoFieldToColumn;
		MongoClient mongo = null;
		DB db;
		DBCollection mainColl;
		
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
			if (mongo != null) {
				mongo.close();
			}
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, e);
		}
		
		mongoDocIdField = initProps.getProperty(MONGO_DOC_ID);
		mongoFieldToColumn = new HashMap<String,String>();
		mongoFields = new BasicDBObject();
		mongoFields.put("_id", 1);
		mongoFields.put(mongoDocIdField, 1);
		
		List<Map<String, String>> fields = context.getAllEntityFields();
		
		for (Map<String, String> field : fields) {
			if ("true".equals(field.get(MONGO_FIELD_ATTR))) {
				String mongoFieldName = field.get(MONGO_FIELD_NAME_ATTR);
				String columnName = field.get("column");
				
				if (mongoFieldName == null) {
					mongoFieldName = columnName;
				}
				mongoFields.put(mongoFieldName, 1);
				mongoFieldToColumn.put(mongoFieldName, columnName);
			}
		}
		
		
		mc.mongo = mongo;
		mc.db = db;
		mc.mainColl = mainColl;
		mc.mongoDocIdField = mongoDocIdField;
		mc.mongoFieldToColumn = mongoFieldToColumn;
		mc.mongoFields = mongoFields;
		mc.mainIdField = mainIdField;
		
		context.getSolrCore().addCloseHook(new CloseHook() {
      @Override
      public void preClose(SolrCore core) {
        mc.close();
      }
      @Override
      public void postClose(SolrCore core) {
      }
    });
		
		return mc;
	}
	

	@Override
	public Reader getData(String query) {
		mongoCache.clear();
		
		Reader data = super.getData(query);
		if (data != null) {
			
		  // do not bother if there are no mongo fields requested
			if (mc.mongoFieldToColumn.size()==0) return data;
			
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
		
		mongoQuery.put(mc.mongoDocIdField, new BasicDBObject("$in", bibcodes));
		//mongoQuery.put(mongoDocIdField, new BasicDBObject("$in", new String[]{"2009arXiv0909.1287I","1987PhRvD..36..277B"}));
		
		DBCursor cursor = mc.mainColl.find(mongoQuery, mc.mongoFields);
		HashMap<String, Map<Object, String>> collectionsToFetch = new HashMap<String, Map<Object, String>>();
		HashMap<String, Map<String, String>> collectionsToFetchFields = new HashMap<String, Map<String, String>>();
		
		try {
			Iterator<DBObject> it = cursor.iterator();
			Object v;
			
			Map<String, String> mongoToColumn = mc.mongoFieldToColumn;
			String mongoDocIdField = mc.mongoDocIdField;
			String indexFieldName;
			
			while (it.hasNext()) {
				DBObject doc = it.next();
				String docId = (String) doc.get(mongoDocIdField);
				assert docId != null;
				
				Map<String, Object> row = new HashMap<String, Object>();
				
				for (String mongoField : doc.keySet()) {
					indexFieldName = mongoToColumn.get(mongoField);
					
					if (indexFieldName == null)
						continue;
					
					v = doc.get(mongoField);
					
					if (v == null)
						continue;
					
					if (v instanceof DBRef) { // 
						DBRef o = (DBRef) v;
						
						// we'll collect the references to other collections and retrieve them in another big sweep
						// later - cause getting them one by one (as mongodb does) is stupid&slow...
						
						// we have to find out what fields are references; the ids of the references and then save
						// the link between our original docs' <-> refd id. I know it is more work, but it is 
						// defensive coding; I don't want to rely on the assumption that the references are always
						// linked through the ID of the primary document. ...
						
						//System.out.println(o.fetch());
						String colName = o.getRef(); //("$ref").toString(); // in which collection the new value is
						Object refObjId = o.getId(); //get("$id").toString(); // what ID in the new collection the object has
						
						if (!collectionsToFetch.containsKey(colName)) {
							collectionsToFetch.put(colName, new HashMap<Object, String>());
							collectionsToFetchFields.put(colName, new HashMap<String, String>());
						}
						
						collectionsToFetch.get(colName).put(refObjId, docId);
						collectionsToFetchFields.get(colName).put(mongoField, indexFieldName);
							
					}
					else {
						row.put(indexFieldName, v);
					}
				}
				mongoCache.put(docId, row);
			}
		}
		finally {
			cursor.close();
		}
		
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
				cursor = mc.db.getCollection(collectionName).find(otherCollQuery, otherFields);
				
				try {
					// and insert them back into the solr doc (using the solr fieldname)
					Iterator<DBObject> it = cursor.iterator();
					while (it.hasNext()) {
						DBObject newData = it.next();
						Map<String, Object> docToUpdate = mongoCache.get(refId2DocId.get(newData.get("_id")));
						for (String columnx : mongoField2SolrDocField.keySet()) {
							docToUpdate.put(mongoField2SolrDocField.get(columnx), newData.get(columnx));
						}
					}
				}
				finally {
					cursor.close();
				}
			}
		}
  }

	@Override
	public void close() {
		super.close();
		mc = null;
	}
	
	
	public Map<String, String> getFieldColumnMap() {
		return mc.mongoFieldToColumn;
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
		
		String docId = (String) row.get(mc.mainIdField);
		if (mongoCache.containsKey(docId)) {
			row.putAll(mongoCache.get(docId));
		}
	  
  }
	
	public static class MongoConnection {
		public String mainIdField;
		public MongoClient mongo;
		public BasicDBObject mongoFields;
		public Map<String, String> mongoFieldToColumn;
		public String mongoDocIdField;
		public DBCollection mainColl;
		public DB db;
		
		public void close() {
			mongo.close();
			mongo = null;
			db = null;
			mongoFields = null;
			mongoFieldToColumn = null;
			mongoDocIdField = null;
			mainColl = null;
			mainIdField = null;
			return;
		}

	}
	
}
