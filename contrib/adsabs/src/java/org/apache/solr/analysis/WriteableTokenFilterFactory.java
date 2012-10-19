package org.apache.solr.analysis;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.solr.common.util.StrUtils;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenFilterFactory;


public abstract class WriteableTokenFilterFactory extends TokenFilterFactory 
    implements ResourceLoaderAware {
	
	public static final String SYNTAX_TYPE = "syntax";
	public static final String SYNTAX_TYPE_DEFAULT = WriteableSynonymMap.SYNTAX_TYPE_EXPLICIT;
	
    public static final Logger log = LoggerFactory.getLogger(WriteableTokenFilterFactory.class);

	private WriteableSynonymMap synMap = null;
	
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
		    args.put("outFile", outFile);
		    synMap.setOutput(args.get("outFile"));
	    }
	    else {
	    	log.warn("Missing required argument 'outFile' at: " + this.getClass().getName() 
	    			+ "The synonyms will not be saved to disk");
	    }
		
	    String synonyms = args.get("synonyms");
	    if (synonyms != null) {
	    	File outFilePath = new File(synonyms);
		    if (outFilePath.isAbsolute() == false) {
		    	List<String> files = StrUtils.splitFileNames(synonyms);
		        for (String file : files) {
		        	loadFile(loader, file);
		        }
		    } else {
	        	loadFile(loader, synonyms);
		    }
	    }
	}
	
	private void loadFile(ResourceLoader loader, String filepath) {
    	try {
    		log.info("Loading: " + filepath);
			getSynonymMap().parseRules(((SolrResourceLoader) loader).getLines(filepath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void init(Map<String, String> args) {
		super.init(args);
		initSynMap();
	}
	
	private void initSynMap() {
		String syntax = args.get(SYNTAX_TYPE);
		
		if (syntax == null) {
			syntax = SYNTAX_TYPE_DEFAULT;
		}
		
		if (syntax.equals(WriteableSynonymMap.SYNTAX_TYPE_EQUIVALENT)) {
			this.synMap = new WriteableEquivalentSynonymMap(null);
		} else if (syntax.equals(WriteableSynonymMap.SYNTAX_TYPE_EXPLICIT)) {
			this.synMap = new WriteableExplicitSynonymMap(null);
		} else {
			throw new RuntimeException("Unknown syntax type specified for WriteableSynonymMap");
		}
	}
	
	
	public WriteableSynonymMap getSynonymMap() {
		if (synMap == null) {
			initSynMap();
		}
		return synMap;
	}
	
	public void setSynonymMap(WriteableSynonymMap synMap) {
		this.synMap = synMap;
	}
	
	
}
