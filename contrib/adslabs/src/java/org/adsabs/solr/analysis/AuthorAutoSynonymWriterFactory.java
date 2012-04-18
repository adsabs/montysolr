/**
 * 
 */
package org.adsabs.solr.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;
import org.apache.solr.common.ResourceLoader;
import org.apache.solr.common.SolrException;
import org.apache.solr.util.plugin.ResourceLoaderAware;
import org.apache.solr.core.SolrResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jluker
 *
 */
public class AuthorAutoSynonymWriterFactory extends BaseTokenFilterFactory implements ResourceLoaderAware {
	
    public static final Logger log = LoggerFactory.getLogger(AuthorAutoSynonymWriterFactory.class);
    
    @Override
	public void inform(ResourceLoader loader) {
    	
		this.outFile = args.get("outFile");
	    if (this.outFile == null)
	        throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Missing required argument 'outFile'.");
	    
	    File outFilePath = new File(this.outFile);
	    if (outFilePath.isAbsolute() == false) {
	    	outFilePath = new File(((SolrResourceLoader) loader).getConfigDir() + this.outFile);
	    }
	
		log.info("creating new BufferedWriter for " + this.outFile);
		
		Charset UTF_8 = Charset.forName("UTF-8");
		try {
			Writer w = new OutputStreamWriter(new FileOutputStream(outFilePath, true), UTF_8);
			this.writer = new BufferedWriter(w);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void init(Map<String, String> args) {
		super.init(args);
	}
	
	private BufferedWriter writer;
	private String outFile;
	
	/* (non-Javadoc)
	 * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	@Override
	public TokenStream create(TokenStream input) {
		return new AuthorAutoSynonymWriter(input, this.writer);
	}
	
	public void setWriter(BufferedWriter writer) {
		this.writer = writer;
	}
}
