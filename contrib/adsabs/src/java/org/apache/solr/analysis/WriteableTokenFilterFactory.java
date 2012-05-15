package org.apache.solr.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.adsabs.solr.analysis.WriteableSynonymMap;
import org.apache.solr.common.ResourceLoader;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.StrUtils;
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
		    args.put("outFile", outFile);
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
		    	List<String> newFiles = new ArrayList<String>();
		        for (String file : files) {
		        	outFilePath = new File(file);
		        	if (!outFilePath.canRead()) {
		        		newFiles.add(new File(((SolrResourceLoader) loader).getConfigDir() + file).getAbsolutePath());
		        	}
		        }
		    	args.put("synonyms", StrUtils.join(newFiles, ','));
		    }
	    }
	}
	
	public void init(Map<String, String> args) {
		if (args.containsKey("outFile")) {
			synMap = new WriteableSynonymMap(args.get("outFile"));
			//args.remove("outFile");
		}
		else {
			synMap = new WriteableSynonymMap(null);
		}
		
		if (args.containsKey("synonyms")) {
			String synonyms = args.get("synonyms");
		    try {
		      File synonymFile = new File(synonyms);
		      if (synonymFile.exists()) {
		    	  getSynonymMap().parseRules(getLines(synonyms));
		      } else  {
		        List<String> files = StrUtils.splitFileNames(synonyms);
		        for (String file : files) {
		          getSynonymMap().parseRules(getLines(file.trim()));
		        }
		      }
		    } catch (IOException e) {
		      throw new RuntimeException(e);
		    }
		}
		
		super.init(args);
	}
	
	
	public WriteableSynonymMap getSynonymMap() {
		return synMap;
	}
	
	public void setSynonymMap(WriteableSynonymMap synMap) {
		this.synMap = synMap;
	}
	
	private List<String> getLines(String inputFile) throws IOException {
		ArrayList<String> lines;
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(inputFile)),
					Charset.forName("UTF-8")));

			lines = new ArrayList<String>();
			for (String word = null; (word = input.readLine()) != null;) {
				// skip initial bom marker
				if (lines.isEmpty() && word.length() > 0
						&& word.charAt(0) == '\uFEFF')
					word = word.substring(1);
				// skip comments
				if (word.startsWith("#"))
					continue;
				word = word.trim();
				// skip blank lines
				if (word.length() == 0)
					continue;
				lines.add(word);
			}
		} catch (CharacterCodingException ex) {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
					"Error loading resource (wrong encoding?): " + inputFile,
					ex);
		} finally {
			if (input != null)
				input.close();
		}
		return lines;

	}
}
