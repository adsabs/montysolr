package org.apache.solr.analysis;

import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.core.SolrResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public abstract class PersistingMapTokenFilterFactory extends TokenFilterFactory
        implements ResourceLoaderAware {

    public static final Logger log = LoggerFactory.getLogger(PersistingMapTokenFilterFactory.class);
    protected WriteableSynonymMap synMap = null;
    protected String outFile;
    protected String synonyms;
    protected String syntax;

    public PersistingMapTokenFilterFactory(Map<String, String> args) {
        super(args);
        synMap = createSynonymMap();

        if (args.containsKey("outFile")) {
            this.outFile = args.remove("outFile");
        }
        if (args.containsKey("synonyms")) {
            this.synonyms = args.remove("synonyms");
        }
        if (args.containsKey("syntax")) {
            this.syntax = args.remove("syntax");
        }
    }

    public void inform(ResourceLoader loader) {

        if (outFile != null) {
            File outFilePath = new File(outFile);
            if (!outFilePath.exists()) {
                try {
                    outFilePath.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                log.warn("We have created " + outFilePath);
            }
            outFile = outFilePath.getAbsolutePath();
            synMap.setOutput(outFile);
        } else {
            log.warn("Missing required argument 'outFile' at: " + this.getClass().getName()
                    + "The synonyms will not be saved to disk");
        }

        if (synonyms != null) {
            File outFilePath = new File(synonyms);
            if (!outFilePath.isAbsolute()) {
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
        if (syntax == null) {
            syntax = "explicit";
        }

        if (syntax.equals("equivalent")) {
            return new WriteableEquivalentSynonymMap();
        } else if (syntax.equals("explicit")) {
            return new WriteableExplicitSynonymMap();
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
