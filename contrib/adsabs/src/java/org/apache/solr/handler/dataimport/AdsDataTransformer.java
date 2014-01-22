package org.apache.solr.handler.dataimport;

import java.util.Map;
import java.util.TreeMap;



/**
 * This class is a workaround for loading data from several
 * resources at once. The data itself is loaded by the 
 * parent DataSource, however since there is no way to 
 * register transformers directly from there (without hacks)
 * this class here is responsible for calling the DataSource 
 *
 */
public class AdsDataTransformer extends Transformer {

	
	@Override
	public Object transformRow(Map<String, Object> row, Context context) {
		
	  if (context.getDataSource() instanceof AdsDataSource) {
	    ((AdsDataSource) context.getDataSource()).transformRow(row);
	  }
	  
	  /*
	   * and because the *stupid* DIH doesn't reflect order in which 
	   * fields are defined in the data config.xml (ie. is not using linked hash map) 
	   * the fields are just out in random order; so we must sort them
	   */
	  return new TreeMap<String,Object>(row);
		//return row;
	}

}

