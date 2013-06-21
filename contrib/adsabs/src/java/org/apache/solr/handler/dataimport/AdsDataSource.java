package org.apache.solr.handler.dataimport;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.solr.common.SolrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
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

	private final static String MONGO_DOC_ID = "mongoDocIdField";
	private final static String MONGO_HOST = "mongoHost";
	private final static String MONGO_PORT = "mongoPort";
	private final static String MONGO_DB_NAME = "mongoDBName";
	private final static String MONGO_COLLECTION_NAME = "mongoCollectionName";
	private final static String MONGO_FIELD_NAME_ATTR = "mongoFieldName";
	private final static String MONGO_FIELD_ATTR = "mongoField";
	
	
	
	private String mongoDBName;
	private String mongoCollectionName;
	private String mongoDocIdField;
	private String mongoHost;
	private int mongoPort;
	
	private Map<String, Map<String, Object>> mongoCache = new HashMap<String, Map<String, Object>>();
	private BasicDBObject mongoFields;
	private Map<String,String> fieldColumnMap;
	private MongoClient mongo;
	private DBCollection docs;
	
	@Override
	public void init(Context context, Properties initProps) {
		super.init(context, initProps);
		initMongo(context, initProps);
	}
	
	private void initMongo(Context context, Properties initProps) {
		mongoDocIdField = initProps.getProperty(MONGO_DOC_ID);
		mongoHost = initProps.getProperty(MONGO_HOST);
		mongoPort = Integer.parseInt(initProps.getProperty(MONGO_PORT));
		mongoDBName = initProps.getProperty(MONGO_DB_NAME);
		mongoCollectionName = initProps.getProperty(MONGO_COLLECTION_NAME);
		
		try {
			mongo = new MongoClient(new ServerAddress(mongoHost, mongoPort));
			docs = mongo.getDB(mongoDBName).getCollection(mongoCollectionName);
		} catch (UnknownHostException e) {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, 
			    e.toString());
		}
		
		List<Map<String, String>> fields = context.getAllEntityFields();
		
		fieldColumnMap = new HashMap<String,String>();
		mongoFields = new BasicDBObject();
		mongoFields.put("_id", 0);
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
			
			scanner.reset();
			//try {
	      //data.reset(); // reposition the reader
	      scanner.reset();
      //} catch (IOException e) {
      //	e.printStackTrace();
      //}
      
			BasicDBObject mongoQuery = new BasicDBObject();
			mongoQuery.put(mongoDocIdField, new BasicDBObject("$in", bibcodes)); //new String[]{"2009arXiv0909.1287I","1987PhRvD..36..277B"})); //new String[]{"2009arXiv0909.1287I","1987PhRvD..36..277B"}
			
			DBCursor cursor = docs.find(mongoQuery, mongoFields);
			Iterator<DBObject> it = cursor.iterator();
			while (it.hasNext()) {
				DBObject doc = it.next();
				String docId = (String) doc.get(mongoDocIdField);
				Map<String, Object> row = new HashMap<String, Object>();
				for (String column : fieldColumnMap.keySet()) {
					String mongoField = fieldColumnMap.get(column);
					row.put(column, doc.get(mongoField));
				}
				mongoCache.put(docId, row);
			}
			
			return new StringReader(buffer.toString());
			
		}
		return data;
	}
	
	@Override
	public void close() {
		super.close();
		mongo.close();
	}
	
	
	public Map<String, String> getFieldColumnMap() {
		return fieldColumnMap;
	}

	public DBObject getMongoDoc(String docId) {
		BasicDBObject query = new BasicDBObject();
		query.put(mongoDocIdField, docId);
		return docs.findOne(query, mongoFields);
	}
	

	public void transformRow(Map<String, Object> row) {
		
		String docId = (String) row.get(mongoDocIdField);
		if (mongoCache.containsKey(docId)) {
			row.putAll(mongoCache.get(docId));
		}
	  
  }
	
}
