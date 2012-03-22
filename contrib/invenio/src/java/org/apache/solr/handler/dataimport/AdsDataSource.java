package org.apache.solr.handler.dataimport;

import java.net.UnknownHostException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import org.ads.mongodb.MongoConnection;

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
		
		try {
			mongoInstance = MongoConnection.init(mongoHost, mongoPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
