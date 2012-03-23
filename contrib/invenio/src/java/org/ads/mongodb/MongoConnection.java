package org.ads.mongodb;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.MongoException;

public class MongoConnection {
	
	public static final Logger log = LoggerFactory.getLogger(MongoConnection.class);
	
    private static Mongo m = null;
    private static DB db = null;

    public synchronized static Mongo init(String host, int port) 
    		throws UnknownHostException, MongoException {
        log.info("initializing mongo connection using host " + host + " and port " + port);
    	m = new Mongo(host, port);
    	return getInstance();
    }
    public synchronized static Mongo getInstance() {
        if (m == null) {
        	try {
        		log.warn("returning mongo connection with default host settings");
	            init("localhost", 27017);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
        return m;
    }
    
    
}
