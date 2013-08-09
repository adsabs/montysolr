package org.apache.solr.handler.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SolrIndexSearcher;


public class BatchProviderFindWordGroups extends BatchProvider {
	
	int maxClauses = 500;
	
	@Override
  public void run(SolrQueryRequest locReq, BatchHandlerRequestQueue queue) throws Exception {
	  SolrParams params = locReq.getParams();
	  String jobid = params.get("jobid");
	  String workDir = params.get("#workdir");
	  File input = new File(workDir + "/" + jobid + ".input");
	  if (!input.canRead()) {
	  	throw new SolrException(ErrorCode.BAD_REQUEST, "No input data available, bark bark - " + input);
	  }
	  
	  List<String> terms = readInputFile(input);
	  final HashSet<String> termMap = new HashSet<String>();
	  termMap.addAll(terms);
	  
	  
	  SolrIndexSearcher searcher = locReq.getSearcher();
	  IndexSchema schema = locReq.getCore().getSchema();
	  
	  File jobFile = new File(workDir + "/" + params.get("jobid"));
		final BufferedWriter out = new BufferedWriter(new FileWriter(jobFile), 1024*256);
		
		final int maxDistance = params.getInt("maxDistance", 2);
		final Analyzer analyzer = schema.getAnalyzer();
		final HashSet<String> fieldsToLoad = new HashSet<String>();
		String[] fields = params.getParams("fields");
		for (String f: fields) {
			for (String ff: f.split("( |,)")) {
				SchemaField field = schema.getFieldOrNull(ff);
				if (field==null || !field.stored()) {
					throw new SolrException(ErrorCode.BAD_REQUEST, "We cannot dump fields that do not exist or are not stored: " + ff);
				}
				fieldsToLoad.add(ff);
			}
		}
	  
	  boolean isFinished = false;
	  final Map<String, Integer> collectedItems = new HashMap<String, Integer>();
	  
	  while (isFinished == false) {
	  	Query query = buildQuery(terms, fieldsToLoad);
	  	
			final BatchHandlerRequestQueue batchQueue = queue;
			
			searcher.search(query, null, new Collector() {
				private AtomicReader reader;
				private int processed = 0;
				private CharTermAttribute termAtt;
				private PositionIncrementAttribute posIncrAtt;

				@Override
				public boolean acceptsDocsOutOfOrder() {
					return true;
				}

				@Override
				public void collect(int i) throws IOException {
					Document d;
					d = reader.document(i, fieldsToLoad);
					processed++;
					String tokenStr;
					int keepAdding = 0;
					
					if (processed % 10000 == 0) {
						if(batchQueue.isStopped()) { // inside, because queue is synchronized
							throw new IOException("Collector interrupted - stopping");
						}
					}

					
					for (String f: fieldsToLoad) {
						String[] vals = d.getValues(f);
						posIncrAtt = null;
						for (String s: vals) {
							LinkedList<String> tokenQueue = new LinkedList<String>();
							TokenStream buffer = analyzer.tokenStream(f, new StringReader(s));

							if (!buffer.hasAttribute(CharTermAttribute.class)) {
								continue; // empty stream
							}

							termAtt = buffer.getAttribute(CharTermAttribute.class);
							
							if (buffer.hasAttribute(PositionIncrementAttribute.class)) {
								posIncrAtt = buffer.getAttribute(PositionIncrementAttribute.class);
							}

							if (posIncrAtt != null) {
								while (buffer.incrementToken()) {
									
									tokenStr = termAtt.toString();
									
									if (posIncrAtt.getPositionIncrement() != 0) {
										if (tokenQueue.size() >= maxDistance-1) {
											tokenQueue.removeLast();
											tokenQueue.addFirst(tokenStr);
										}
										if (termMap.contains(tokenStr)) {
											addEverythingRightToLeft(tokenQueue, collectedItems);
											keepAdding = tokenQueue.size();
										}
									}
									else if (termMap.contains(tokenStr)) {
										addEverythingRightToLeft(tokenQueue, collectedItems);
									}
								}
							}
							else {
								while (buffer.incrementToken()) {
									
									tokenStr = termAtt.toString();
									
									if (tokenQueue.size() >= maxDistance-1) {
										tokenQueue.removeLast();
										tokenQueue.addFirst(tokenStr);
									}
									
									if (termMap.contains(tokenStr)) {
										addEverythingRightToLeft(tokenQueue, collectedItems);
										keepAdding = tokenQueue.size();
									}
									else if (keepAdding-- > 0) {
										addEverythingRightToLeft(tokenQueue, collectedItems);
									}
									
								}
							}
						}
					}
				}
				
				private void addEverythingRightToLeft(LinkedList<String> tokenQueue,
            Map<String, Integer> collectedItems) {
					String key = tokenQueue.get(tokenQueue.size()-1);
	        for (int i=tokenQueue.size();i>0;i--) {
	        	key = tokenQueue.get(i) + " " + key;
	        	if (collectedItems.containsKey(key)) {
	        		collectedItems.put(key, collectedItems.get(key)+1);
	        	}
	        	else {
	        		collectedItems.put(key, 1);
	        	}
	        }
        }

				private void addEverythingLeftToRight(LinkedList<String> tokenQueue,
            Map<String, Integer> collectedItems) {
					String key = tokenQueue.get(0);
	        for (int i=1;i<tokenQueue.size();i++) {
	        	key = tokenQueue.get(i) + " " + key;
	        	if (collectedItems.containsKey(key)) {
	        		collectedItems.put(key, collectedItems.get(key)+1);
	        	}
	        	else {
	        		collectedItems.put(key, 1);
	        	}
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
		}
  }
	
	private Query buildQuery(List<String> terms, HashSet<String> fieldsToLoad) {
	  ArrayList<String> toRemove = new ArrayList<String>();
	  BooleanQuery bq = new BooleanQuery();
	  
	  String ff = "";
	  if (fieldsToLoad.size() > 1) {
	  	for (String x: fieldsToLoad) {
	  		ff = x;
	  	}
	  }
	  
	  for (int i=0;i<terms.size() && i<maxClauses;i++) {
	  	if (fieldsToLoad.size() > 1) {
	  		BooleanQuery bbq;
				bbq = new BooleanQuery();
				for (String f: fieldsToLoad) {
					new BooleanClause(new TermQuery(new Term(f, terms.get(i))), Occur.SHOULD);
				}
	  		bq.add(bbq, Occur.SHOULD);
	  	}
	  	else {
	  		bq.add(new BooleanClause(new TermQuery(new Term(ff, terms.get(i))), Occur.SHOULD));
	  	}
	  }
	  for (String t: toRemove) {
	  	terms.remove(t);
	  }
	  return bq;
  }

	private List<String> readInputFile(File input) throws IOException {
		ArrayList<String> out = new ArrayList<String>();
	  BufferedReader br = new BufferedReader(new FileReader(input));
	  String line;
	  while ((line = br.readLine()) != null) {
	     line = line.toLowerCase().trim();
	     out.add(line);
	  }
	  br.close();
	  return out;
  }

	
	@Override
  public String getDescription() {
	  return "Takes list of terms and find their complements";
  }
}
