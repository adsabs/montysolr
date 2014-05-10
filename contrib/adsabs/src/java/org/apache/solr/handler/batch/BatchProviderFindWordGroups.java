package org.apache.solr.handler.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	  IndexSchema schema = locReq.getCore().getLatestSchema();
	  
	  File jobFile = new File(workDir + "/" + params.get("jobid"));
		final BufferedWriter out = new BufferedWriter(new FileWriter(jobFile), 1024*256);
		
		final int maxlen = params.getInt("maxlen", 2);
		final int stopAterReaching = Math.min(params.getInt("stopAfterReaching", 10000), 1000000);
		float upperLimit = Float.parseFloat(params.get("upperLimit", "1.0"));
		float lowerLimit = Float.parseFloat(params.get("lowerLimit", "0.9"));
		final int maxClauses = Math.min(params.getInt("maxClauses", 5), 500);
		
		assert upperLimit <= 1.0f && upperLimit > 0.0f;
		assert lowerLimit < upperLimit && lowerLimit >= 0.0f;
		
		
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
	  
	  final Map<String, Integer> collectedItems = new HashMap<String, Integer>();
	  
	  while (true) {
	  	
	  	int origSize = terms.size();
	  	if (terms.size() < 1 || collectedItems.size() > stopAterReaching) {
	  		break;
	  	}
	  	
	  	
	  	Query query = buildQuery(terms, fieldsToLoad, maxClauses);
	  	assert terms.size() < origSize;
	  	
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
					
					if (processed % 10000 == 0) {
						if(batchQueue.isStopped()) { // inside, because queue is synchronized
							throw new IOException("Collector interrupted - stopping");
						}
					}
					
					if (collectedItems.size() > stopAterReaching) {
						return;
					}
					
					Document d;
					d = reader.document(i, fieldsToLoad);
					processed++;
					String tokenStr;
					int keepAdding = -1;
					
					
					
					for (String f: fieldsToLoad) {
						String[] vals = d.getValues(f);
						posIncrAtt = null;
						for (String s: vals) {
							//System.out.println("analyzing: " + s);
							LinkedList<String> tokenQueue = new LinkedList<String>();
							TokenStream buffer = analyzer.tokenStream(f, new StringReader(s));

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
									
									tokenStr = termAtt.toString();
									
									if (tokenStr.trim().equals(""))
										continue;
									
									//System.out.println(tokenStr);
									
									if (posIncrAtt.getPositionIncrement() == 0) {
										if (termMap.contains(tokenStr)) {
											tokenQueue.removeLast();
											tokenQueue.addLast(tokenStr);
											addEverythingLeftToRight(tokenQueue, collectedItems);
											keepAdding = maxlen;
										}
										continue;
									}
									
									if (tokenQueue.size() >= maxlen) {
										tokenQueue.removeFirst();
									}
									
									tokenQueue.addLast(tokenStr);
									
									if (termMap.contains(tokenStr)) {
										addEverythingLeftToRight(tokenQueue, collectedItems);
										keepAdding = maxlen;
									}
									else if (keepAdding-- > 0) {
										addEverythingLeftToRight(tokenQueue, collectedItems);
									}
								}
							}
							else {
								while (buffer.incrementToken()) {
									
									tokenStr = termAtt.toString();
									
									if (tokenStr.trim().equals(""))
										continue;
									
									if (tokenQueue.size() >= maxlen) {
										tokenQueue.removeFirst();
										tokenQueue.addLast(tokenStr);
									}
									
									if (termMap.contains(tokenStr)) {
										addEverythingLeftToRight(tokenQueue, collectedItems);
										keepAdding = maxlen;
									}
									else if (keepAdding-- > 0) {
										addEverythingLeftToRight(tokenQueue, collectedItems);
									}
									
								}
							}
						}
					}
				}
				
				private void addEverythingLeftToRight(LinkedList<String> tokenQueue,
            Map<String, Integer> collectedItems) {
					if (tokenQueue.size() == 1 || tokenQueue.size() < maxlen)
						return;
					
					String key = tokenQueue.get(0);
	        for (int i=1;i<tokenQueue.size();i++) {
	        	key = key + "|" + tokenQueue.get(i);
	        }
	        //System.out.println("adding: " + key);
	        if (collectedItems.containsKey(key)) {
        		collectedItems.put(key, collectedItems.get(key)+1);
        	}
        	else {
        		collectedItems.put(key, 1);
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
			
			if (collectedItems.size() > stopAterReaching) {
				break;
			}
		}
	  
	  // sort results by frequency, highest first
	  List<Entry<String, Integer>> colVal = new ArrayList<Entry<String, Integer>>(collectedItems.size());
	  for (Entry<String, Integer> e: collectedItems.entrySet()) {
	  	colVal.add(e);
	  }
	  
	  Collections.sort(colVal, new Comparator<Entry<String, Integer>>() {
			@Override
      public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
	      int f = o2.getValue().compareTo(o1.getValue());
	      if (f == 0)
	      	return o1.getKey().compareTo(o2.getKey());
	      return f;
      };
		});
	  
	  int upperL = upperLimit == 1.0f ? 0 : colVal.size() - Math.round(colVal.size() * upperLimit);
	  int lowerL = lowerLimit == 0.0f ? colVal.size() : colVal.size() - Math.round(colVal.size() * lowerLimit);
	  
	  for (int i=upperL; i < colVal.size() && i < lowerL; i++) {
	  	Entry<String, Integer> entry = colVal.get(i);
	  	out.write(entry.getKey());
	  	out.write("\t");
	  	out.write(Integer.toString(entry.getValue()));
	  	out.write("\n");
	  }
	  out.close();
	  
  }
	
	private Query buildQuery(List<String> terms, HashSet<String> fieldsToLoad, int maxClauses) {
	  ArrayList<String> toRemove = new ArrayList<String>();
	  BooleanQuery bq = new BooleanQuery();
	  
	  String ff = "";
	  if (fieldsToLoad.size() > 0) {
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
	  	toRemove.add(terms.get(i));
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
