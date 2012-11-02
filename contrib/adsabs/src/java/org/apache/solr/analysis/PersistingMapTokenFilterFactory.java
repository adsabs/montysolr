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


public abstract class PersistingMapTokenFilterFactory extends TokenFilterFactory 
implements ResourceLoaderAware {

  public static final Logger log = LoggerFactory.getLogger(PersistingMapTokenFilterFactory.class);
  private WriteableSynonymMap synMap = null;
  
  public void init(Map<String, String> args) {
    super.init(args);
    synMap = createSynonymMap();
  }
  
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

  public WriteableSynonymMap createSynonymMap() {
    String syntax = args.get("syntax");
    if (syntax == null) {
      syntax = "explicit";
    }

    if (syntax.equals("equivalent")) {
      return new WriteableEquivalentSynonymMap(null);
    } else if (syntax.equals("explicit")) {
      return new WriteableExplicitSynonymMap(null);
    } else {
      throw new RuntimeException("Unknown syntax type specified for WriteableSynonymMap");
    }
  }
  

  
  private void loadFile(ResourceLoader loader, String filepath) {
    try {
      log.info("Loading: " + filepath);
      getSynonymMap().populateMap(((SolrResourceLoader) loader).getLines(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  

  public WriteableSynonymMap getSynonymMap() {
    if (synMap == null) {
      synMap = createSynonymMap();
    }
    return synMap;
  }

  public void setSynonymMap(WriteableSynonymMap synMap) {
    this.synMap = synMap;
  }


}
