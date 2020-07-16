package org.apache.solr.handler.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LegacyNumericTokenStream.LegacyNumericTermAttribute;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SimpleCollector;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TextField;
import org.apache.solr.search.SolrIndexSearcher;
import org.noggit.JSONUtil;

/**
 * Provider that dumps selected fields to disk - it can analyze fields 
 * and show their values as indexed (but only for text/string fields).
 * 
 * This method is INEFFICIENT and is to be avoided for normal operations!
 * 
 * How it works:
 * 
 * 		1. query for all documents that satisfy conditions
 * 		2. in loop (for every doc):
 * 				- read selected fields for the current document (stored values)
 * 				- analyze the values by the appropriate analyzer (for that field)
 * 			  - collect all tokens, put them inside array
 *        - put the array into a fake solr doc
 *        - dump the doc to disk in JSON
 *        
 * Tokens that are indexed at the same position are joined by '|'
 * (eg. "field" : ["foo|fool", "bar", ....])
 * 
 * These are the parameters:
 * 
 * 		fields: list of fields to dump (comma separated)
 *    analyze: true/false - whether to dump values as indexed [true]
 */

public class BatchProviderDumpAnalyzedFields extends BatchProvider {
	
	public void run(SolrQueryRequest req, BatchHandlerRequestQueue queue) throws Exception {
		
		SolrParams params = req.getParams();
	  String jobid = params.get("jobid");
	  String workDir = params.get("#workdir");
	  
		SolrCore core = req.getCore();
		IndexSchema schema = core.getLatestSchema();
		final HashMap<String, FieldType> fieldsToLoad = new HashMap<String, FieldType>();
		final Analyzer analyzer = core.getLatestSchema().getIndexAnalyzer();


		String[] fields = params.getParams("fields");
		
		if (fields == null) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "'fields' parameter missssssing");
		}
		
		for (String f: fields) {
			for (String ff: f.split("( |,)")) {
				SchemaField field = schema.getFieldOrNull(ff);
				if (field==null || !field.stored()) {
					throw new SolrException(ErrorCode.BAD_REQUEST, "We cannot dump fields that do not exist or are not stored: " + ff);
				}
				fieldsToLoad.put(ff, field.getType());
			}
		}

		final boolean analyze = params.getBool("analyze", true);
		
		Query query = this.getQuery(req);

		SolrIndexSearcher se = req.getSearcher();

		HashMap<String, String> descr = new HashMap<String, String>();
		descr.put("query", query.toString());
		descr.put("indexDir", se.getPath());
		descr.put("maxDoc", Integer.toString(se.maxDoc()));
		descr.put("date", new Date().toString()); 

		File jobFile = new File(workDir + "/" + params.get("jobid"));
		final BufferedWriter out = new BufferedWriter(new FileWriter(jobFile), 1024*256);
		out.write("{\n");
		out.write("\"description\": " + JSONUtil.toJSON(descr).replace("\n", " ") + ",\n");
		out.write("\"data\" : [\n");
		
		final BatchHandlerRequestQueue batchQueue = queue;
		
		se.search(query, new SimpleCollector() {
			private LeafReader reader;
			private int processed = 0;
			private CharTermAttribute termAtt;
			private LegacyNumericTermAttribute numAtt;
			private PositionIncrementAttribute posIncrAtt;
			private Map<String, List<String>>document = new HashMap<String, List<String>>();

			protected void doSetNextReader(LeafReaderContext context) throws IOException {
			  reader = context.reader();
			}
			@Override
			public void collect(int i) throws IOException {
				Document d;
				d = reader.document(i, fieldsToLoad.keySet());
				processed++;
				document.clear();

				if (processed % 10000 == 0) {
					if(batchQueue.isStopped()) { // inside, because queue is synchronized
						throw new IOException("Collector interrupted - stopping");
					}
				}


				for (Entry<String,FieldType> en: fieldsToLoad.entrySet()) {
					List<String> tokens = new ArrayList<String>(500);
					String fName = en.getKey();
					FieldType fType = en.getValue();
					
					// test this field is analyzable as text field
					boolean isText = fType.getClass().isAssignableFrom(StringField.class) 
						|| fType.getClass().isAssignableFrom(TextField.class)
						; 
					
					//System.out.println(fName + " " + fType.getClass() + " isText " + isText);
					
					document.put(fName, tokens);
					posIncrAtt = null;
					String[] vals = d.getValues(fName);
					
					if (!analyze) {
						for (String s: vals) {
							tokens.add(s);
						}
					}
					else {
					
						if (!isText) {
							for (String s: vals) {
								tokens.add(s);
							}
						}
						else {
							for (String s: vals) {
								
								TokenStream buffer = analyzer.tokenStream(fName, new StringReader(fType.indexedToReadable(s)));
								
								if (!buffer.hasAttribute(CharTermAttribute.class)) {
									continue; // empty stream
								}
		
								termAtt = buffer.getAttribute(CharTermAttribute.class);
		
								if (buffer.hasAttribute(PositionIncrementAttribute.class)) {
									posIncrAtt = buffer.getAttribute(PositionIncrementAttribute.class);
								}
								buffer.reset();
		
								if (posIncrAtt != null) {
									while (buffer.incrementToken()) {
										if (posIncrAtt.getPositionIncrement() == 0) {
											tokens.set(tokens.size()-1, tokens.get(tokens.size()-1) + "|" + fType.indexedToReadable(termAtt.toString()));
										}
										else {
											tokens.add(fType.indexedToReadable(termAtt.toString()));
										}
									}
								}
								else {
									while (buffer.incrementToken()) {
										tokens.add(fType.indexedToReadable(termAtt.toString()));
									}
								}
								
								buffer.close();
							}
						}
					}
				}

				if (processed > 1) {
					out.write(",\n");
				}
				// bummer, it doesn't have api for newlines - according to quick googling
				// control chars should be escaped in JSON, so this should be safe
				out.write(JSONUtil.toJSON(document, 0).replace("\n", ""));
			}
			
      @Override
      public boolean needsScores() {
        // TODO Auto-generated method stub
        return false;
      }
		});
		//System.out.println("written + " + jobFile);
		out.write("]\n");
		out.write("}");
		out.close();
	}
	
	@Override
  public String getDescription() {
	  return "Dumps selected fields (for selected docs) to disk in JSON format";
  }
}
