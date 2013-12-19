package org.apache.solr.handler.batch;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.util.Bits;
import org.apache.solr.analysis.author.AuthorNormalizeFilterFactory;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SolrIndexSearcher;

/**
 * Provider which collects all author names,
 * and saves their ASCII => UTF8 mapping to disc. 
 *
 */
public class BatchProviderDumpAuthorNames extends BatchProvider {
	
	private Bits docsToCollect = null;
	public String sourceField = "author";
	public String targetField = "author_collector";

	public void setDocsToCollect(Bits docsToCollect) {
		this.docsToCollect = docsToCollect;
	}
	
	
	
	
	public void run(SolrQueryRequest req, BatchHandlerRequestQueue queue) throws Exception {
		
		SolrCore core = req.getCore();
    IndexSchema schema = req.getSchema();
    
    BatchHandlerRequestData data = queue.pop();
    
    SchemaField field = core.getSchema().getFieldOrNull(sourceField);
    
    if (field==null || !field.stored()) {
      data.setMsg("We cannot dump fields that are not stored: " + sourceField);
      queue.registerFailedBatch(this, data);
      return;
    }
    
    final Analyzer analyzer = core.getSchema().getQueryAnalyzer();
    
    SchemaField tField = core.getSchema().getFieldOrNull(targetField);
    
    if (tField == null) {
      data.setMsg("We cannot find analyzer for: " + targetField);
      queue.registerFailedBatch(this, data);
      return;
    }
    
    AuthorNormalizeFilterFactory factory = new AuthorNormalizeFilterFactory();
    final String targetAnalyzer = targetField;
    
    DirectoryReader ir = req.getSearcher().getIndexReader();
    SolrIndexSearcher se = req.getSearcher();
    
    final HashSet<String> fieldsToLoad = new HashSet<String>();
    fieldsToLoad.add(sourceField);
    
    se.search(new MatchAllDocsQuery(), new Collector() {
      private AtomicReader reader;
      private int i = 0;
      
      @Override
      public boolean acceptsDocsOutOfOrder() {
        return true;
      }

      @Override
      public void collect(int i) {
        Document d;
        try {
          d = reader.document(i, fieldsToLoad);
          for (String f: fieldsToLoad) {
            String[] vals = d.getValues(f);
            for (String s: vals) {
              TokenStream ts = analyzer.tokenStream(targetAnalyzer, new StringReader(s));
              ts.reset();
              while (ts.incrementToken()) {
                //pass
              }
            }
          }
        } catch (IOException e) {
          // pass
        }
      }
      @Override
      public void setNextReader(AtomicReaderContext context) {
        this.reader = context.reader();
      }
      @Override
      public void setScorer(org.apache.lucene.search.Scorer scorer) {
        // Do Nothing
      }
    });
    
    // persist the data
    TokenStream ts = analyzer.tokenStream(targetField, new StringReader("xxx"));
    ts.reset();
    ts.reset();
    ts.reset();
    
  }
	
	@Override
  public String getDescription() {
	  return "Collect author names and saves them to disk as synonym file";
  }


}
