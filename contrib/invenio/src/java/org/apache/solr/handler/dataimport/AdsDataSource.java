package org.apache.solr.handler.dataimport;

import java.net.UnknownHostException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import org.ads.mongodb.MongoConnection;
import org.apache.solr.schema.MongoDataField;


/**
 * {@link AdsDataSource} makes sure that the MongoDB is initialized
 * before being used by the data-importer.
 * 
 * This is necessary for the fields that are of type {@link MongoDataField}
 * and are used in the data-config.xml
 * 
 *
 */

public class AdsDataSource extends URLDataSource {

	Logger log = LoggerFactory.getLogger(MongoConnection.class);

	private final static String MONGO_HOST = "mongoHost";
	private final static String MONGO_PORT = "mongoPort";
	private final static String JDBC_CONFIG = "jdbcConfig";
	
	JdbcDataSource jdbc = null;
	Mongo mongoInstance = null;
	
	public void init(Context context, Properties initProps) {
		super.init(context, initProps);
		
		String mongoHost = initProps.getProperty(MONGO_HOST);
		int mongoPort = Integer.parseInt(initProps.getProperty(MONGO_PORT));
		
		log.info("creating mongo connection instance");
		try {
			mongoInstance = MongoConnection.init(mongoHost, mongoPort);
			log.info("mongo instance: " + mongoInstance);
		} catch (UnknownHostException e) {
			log.error(e.getStackTrace().toString());
		} catch (MongoException e) {
			log.error(e.getStackTrace().toString());
		}
	}
}
