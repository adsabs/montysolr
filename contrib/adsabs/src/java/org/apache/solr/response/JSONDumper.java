package org.apache.solr.response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.util.Bits;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.handler.batch.BatchProviderDumpIndex;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.ReturnFields;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.SolrReturnFields;

/**
 * This class is here only to provide a convenient way to dump
 * documents into a file. It is used from the BatchProviders
 * 
 * As it is package protected, we cannot instantiate it from
 * inside .batch package
 * 
 *  @see BatchProviderDumpIndex
 *
 */

public class JSONDumper extends JSONWriter {

	public JSONDumper(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp) {
	  super(writer, req, rsp);
	  this.doIndent = false;
  }
	
	private Bits liveDocs = null;
	
	public static JSONDumper create(SolrQueryRequest req, File jobFile, Bits bitSet) throws IOException {
		SolrQueryResponse rsp = new SolrQueryResponse();
		FileWriter writer = new FileWriter(jobFile);
		
		ReturnFields returnFields = new SolrReturnFields( req );
    rsp.setReturnFields( returnFields );
    
		JSONDumper d = new JSONDumper(writer, req, rsp);
		d.setBitset(bitSet);
		return d;
  }
	
	public void setBitset(Bits bitSet) {
		this.liveDocs = bitSet;
  }
	
	
	public void writeResponse() throws IOException {
    Boolean omitHeader = req.getParams().getBool(CommonParams.OMIT_HEADER);
    if(omitHeader != null && omitHeader) rsp.getValues().remove("responseHeader");
    
    SolrIndexSearcher searcher = req.getSearcher();
    
    if (liveDocs == null) {
    	liveDocs = searcher.getSlowAtomicReader().getLiveDocs();
    }
    
    
    int maxDoc = searcher.maxDoc();
    
    try {
	    
	    //responseWriter.write(sw,req,rsp);
	    
	    ReturnFields fields = rsp.getReturnFields(); // return everything
	    Set<String> fnames = fields.getLuceneFieldNames();
	    int docCounter = 0;
	    for (int i=0; i<maxDoc; i++) {
	    	if (liveDocs!=null && !liveDocs.get(i)) {
	    		continue;
	    	}
	      Document doc = searcher.doc(i);
	      SolrDocument sdoc = toSolrDocument( doc , schema);
	      writeSolrDocument( null, sdoc, fields, docCounter++ );
	      getWriter().write("\n");
	    }
    }
    finally {
    	close();
    	writer.write('\n');  // ending with a newline looks much better from the command line
      writer.close();
    }
  }

  public static final SolrDocument toSolrDocument( Document doc, final IndexSchema schema ) {
    SolrDocument out = new SolrDocument();
    for( IndexableField f : doc.getFields()) {
      // Make sure multivalued fields are represented as lists
      Object existing = out.get(f.name());
      if (existing == null) {
        SchemaField sf = schema.getFieldOrNull(f.name());
        if (sf != null && sf.multiValued()) {
          List<Object> vals = new ArrayList<>();
          vals.add( f );
          out.setField( f.name(), vals );
        }
        else{
          out.setField( f.name(), f );
        }
      }
      else {
        out.addField( f.name(), f );
      }
    }
    return out;
  }
	
}
