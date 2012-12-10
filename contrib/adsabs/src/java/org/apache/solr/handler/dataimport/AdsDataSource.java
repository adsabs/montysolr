package org.apache.solr.handler.dataimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoURI;


/**
 * {@link AdsDataSource} makes sure that the MongoDB is initialized
 * before being used by the data-importer.
 * 
 * This is necessary for the fields that are of type {@link MongoDataField}
 * and are used in the data-config.xml
 * 
 * TODO: add unittest/blackbox test to make sure this works
 *
 */

public class AdsDataSource extends InvenioDataSource {

	Logger log = LoggerFactory.getLogger(AdsDataSource.class);

	private final static String MONGO_DOC_ID = "mongoDocIdField";
	private final static String MONGO_HOST = "mongoHost";
	private final static String MONGO_PORT = "mongoPort";
	private final static String JDBC_CONFIG = "jdbcConfig";
	private final static String MONGO_DB_NAME = "mongoDBName";
	private final static String MONGO_COLLECTION_NAME = "mongoCollectionName";
	private final static String FIELD_COLUMN_ATTR = "column";
	private final static String MONGO_FIELD_NAME_ATTR = "mongoFieldName";
	private final static String MONGO_FIELD_ATTR = "mongoField";
	
	private final static String FACET_HIERARCHY_FIELD_ATTR = "facetHierarchyField";
	private final static String FACET_HIERARCHY_FIELD_NAMES_ATTR = "fields";
	private final static String FACET_HIERARCHY_FIELD_MULTIVALUE_ATTR = "multiValuedSource";
	
	private String mongoDBName;
	private String mongoCollectionName;
	private String mongoDocIdField;
	private String mongoHost;
	private String mongoPort;
	
	private List<FacetHierarchy> facetHierarchies;
	
	private JdbcDataSource jdbc;
	private BasicDBObject mongoFields;
	private Map<String,String> fieldColumnMap;
	
	@Override
	public void init(Context context, Properties initProps) {
		super.init(context, initProps);
		initMongoFields(context, initProps);
		initFacetHierarchies(context, initProps);
	}
	
	private void initMongoFields(Context context, Properties initProps) {
		mongoDocIdField = initProps.getProperty(MONGO_DOC_ID);
		mongoHost = initProps.getProperty(MONGO_HOST);
		mongoPort = initProps.getProperty(MONGO_PORT);
		mongoDBName = initProps.getProperty(MONGO_DB_NAME);
		mongoCollectionName = initProps.getProperty(MONGO_COLLECTION_NAME);
		
		List<Map<String, String>> fields = context.getAllEntityFields();
		
		fieldColumnMap = new HashMap<String,String>();
		mongoFields = new BasicDBObject();
		mongoFields.put("_id", 0);
		for (Map<String, String> field : fields) {
			if ("true".equals(field.get(MONGO_FIELD_ATTR))) {
				String mongoFieldName = field.get(MONGO_FIELD_NAME_ATTR);
				String columnName = field.get(FIELD_COLUMN_ATTR);
				
				if (mongoFieldName == null) {
					mongoFieldName = columnName;
				}
				mongoFields.put(mongoFieldName, 1);
				fieldColumnMap.put(columnName, mongoFieldName);
			}
		}
	}
	
	private void initFacetHierarchies(Context context, Properties initProps) {
		
		List<Map<String, String>> allFields = context.getAllEntityFields();
		facetHierarchies = new ArrayList<FacetHierarchy>();
		
		for (Map<String, String> fieldDef : allFields) {
			if ("true".equals(fieldDef.get(FACET_HIERARCHY_FIELD_ATTR))) {
				String columnName = fieldDef.get(FIELD_COLUMN_ATTR);
				String hierarchyFields = fieldDef.get(FACET_HIERARCHY_FIELD_NAMES_ATTR);
				boolean multiValueSource = "true".equals(fieldDef.get(FACET_HIERARCHY_FIELD_MULTIVALUE_ATTR));
				
				String[] sourceFields = fieldDef.get(FACET_HIERARCHY_FIELD_NAMES_ATTR).split(",");
				if (sourceFields.length < 2) {
					throw new RuntimeException("Facet hierarchy requires > 1 field");
				}
				
				FacetHierarchy fh = null;
				if (multiValueSource) {
					fh = new FacetHierarchyMV(columnName, sourceFields);
				} else {
					fh = new FacetHierarchy(columnName, sourceFields);
				}
				facetHierarchies.add(fh);
			}
		}
	}
	
  public void destroy() {
    System.out.println("destroy" + Thread.currentThread());
  }
	
	@Override
  public void close() {
    super.close();
  }
	
	public List<FacetHierarchy> getFacetHierarchies() {
		return facetHierarchies;
	}
	
	public Map<String, String> getFieldColumnMap() {
		return fieldColumnMap;
	}

	public BasicDBObject getMongoFields() {
		return mongoFields;
	}
	
	public MongoURI getMongoURI() {
		return new MongoURI("mongodb://" + mongoHost + ":" + mongoPort);
	}
	
	public String getMongoDBName() {
		return mongoDBName;
	}
	
	public String getMongoCollectionName() {
		return mongoCollectionName;
	}
	
	public String getMongoDocIdField() {
		return mongoDocIdField;
	}
	
}
