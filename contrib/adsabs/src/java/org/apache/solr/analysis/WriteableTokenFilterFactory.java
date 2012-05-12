package org.apache.solr.analysis;

import java.io.File;
import java.io.IOException;
import org.adsabs.solr.analysis.WriteableSynonymMap;
import org.apache.solr.common.ResourceLoader;
import org.apache.solr.core.SolrResourceLoader;


public abstract class WriteableTokenFilterFactory extends BaseTokenStreamFactory {
	
	private WriteableSynonymMap synMap;
	
	public void inform(ResourceLoader loader) {
    	
		String outFile = args.get("outFile");
		
	    if (outFile != null) {
	    	args.remove("outFile");
		    File outFilePath = new File(outFile);
		    if (outFilePath.isAbsolute() == false) {
		    	outFilePath = new File(((SolrResourceLoader) loader).getConfigDir() + outFile);
		    }
		
		    if (!outFilePath.exists()) {
				try {
					outFilePath.createNewFile();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				log.warn("We have created " + outFilePath);
			}
		    outFile = outFilePath.getAbsolutePath();
	    }
	    else {
	    	log.warn("Missing required argument 'outFile' at: " + this.getClass().getName() 
	    			+ "The synonyms will not be saved to disk");
	    }
		
		synMap = new WriteableSynonymMap(outFile);
	}
	
	public WriteableSynonymMap getSynonymMap() {
		return synMap;
	}
	
	public void setSynonymMap(WriteableSynonymMap synMap) {
		this.synMap = synMap;
	}
}
