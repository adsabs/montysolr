package perf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.SyntaxError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import perf.CreateQueries.TermFreq;

public class CreatePerformanceQueriesHandler extends RequestHandlerBase {
  
	private CreateQueries extractor = new CreateQueries();
  private int numQueries = 500;
	private boolean runTotalHitsResolution;
	private SolrQueryRequest req = null;
  
	public static final Logger log = LoggerFactory
			.getLogger(CreatePerformanceQueriesHandler.class);


	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {
		SolrParams params = req.getParams();
		String q = params.get(CommonParams.Q);
		
		DirectoryReader reader = req.getSearcher().getIndexReader();
		
		
		List<String> fieldsToTarget = new ArrayList<String>();
		String fields = params.get("fields", null);
		int topN = params.getInt("topN", 5000);
		numQueries = params.getInt("numQueries", 500);
		runTotalHitsResolution = params.getBool("runTotalHitsResolution", false);
		this.req = req;
		
		if (fields == null) {
			for (Entry<String, SchemaField> sField: req.getSchema().getFields().entrySet()) {
				SchemaField fInfo = sField.getValue();
				String fName = sField.getKey();
				if (fInfo.indexed() && fInfo.stored()) {
					fieldsToTarget.add(fName);
				}
			}
		}
		else {
			for (String f: fields.split("\\s*,\\s*")) {
				fieldsToTarget.add(f);
			}
		}
		
		for (String field: fieldsToTarget) {
			log.info("Finding top df terms for field: " + field);
			TermFreq[] topTerms;
			
			Map<String, String> fieldData = new HashMap<String, String>();
			rsp.add(field, fieldData);
			
			try {
				topTerms = CreateQueries.getTopTermsByDocFreq(reader, field, topN, false);
			}
			catch (RuntimeException e) {
				fieldData.put("error", e.getMessage());
				continue;
			}
			
			long maxDF = topTerms[0].df;
			
			int upto = 1;
	    while(topTerms.length > upto && topTerms[upto].df >= maxDF/10) {
	      upto++;
	    }
	    
	    TermFreq[] highFreqTerms = new TermFreq[upto];
	    System.arraycopy(topTerms, 0, highFreqTerms, 0, highFreqTerms.length);

	    while(topTerms.length > upto && topTerms[upto].df >= maxDF/100) {
	      upto++;
	    }
	    final TermFreq[] mediumFreqTerms = new TermFreq[upto - highFreqTerms.length];
	    System.arraycopy(topTerms, highFreqTerms.length, mediumFreqTerms, 0, mediumFreqTerms.length);

	    int downTo = topTerms.length-1;
	    while(topTerms[downTo].df < maxDF/1000) {
	      downTo--;
	    }
	    downTo++;
	    final TermFreq[] lowFreqTerms = new TermFreq[topTerms.length - downTo];
	    System.arraycopy(topTerms, downTo, lowFreqTerms, 0, lowFreqTerms.length);
	    
			
			
			List<String> prefixes = Arrays.asList("HighFreq", "MedFreq", "LowFreq");
			List<TermFreq[]> data = Arrays.asList(highFreqTerms, mediumFreqTerms, lowFreqTerms);
			
			Set<FormattedQuery>entries;
			
			for (int i=0; i<prefixes.size(); i++) {
				if (data.get(i).length > 0) {
					
					// simple cases, no need for resolution
					entries = createEntries(data.get(i), new Formatter());
					fieldData.put("termQueries" + prefixes.get(i), mashup(entries));
					
					// prefix queries
					entries = createEntries(data.get(i), new Formatter() {
						public FormattedQuery format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							return new FormattedQuery("*" + term.substring(term.length()/2), tf.df);
						}
					});
					resolveIfNecessary(field, entries);
					fieldData.put("wildcardQueriesBeginStar" + prefixes.get(i), mashup(entries));
					
					
				  // wildcard queri*
					entries = createEntries(data.get(i), new Formatter() {
						public FormattedQuery format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							return new FormattedQuery(term.substring(0, term.length()/2) + "*", tf.df);
						}
					});
					resolveIfNecessary(field, entries);
					fieldData.put("wildcardQueriesEndStar" + prefixes.get(i), mashup(entries));
					
					
					// wild*rd queries
					entries = createEntries(data.get(i), new Formatter() {
						public FormattedQuery format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							String p1 = term.substring(0, term.length()/2) + "*";
							return new FormattedQuery(p1 + term.substring(term.length() - Math.max(1, term.length() - p1.length() - 3)),
									tf.df);
						}
					});
					resolveIfNecessary(field, entries);
					fieldData.put("wildcardQueriesMidStar" + prefixes.get(i), mashup(entries));
					
					// fuzzy queries
					entries = createEntries(data.get(i), new Formatter() {
						public FormattedQuery format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							return new FormattedQuery("\"" + term + "\"~1", tf.df);
						}
					});
					resolveIfNecessary(field, entries);
					fieldData.put("fuzzyQueries1" + prefixes.get(i), mashup(entries));
					
					// fuzzy queries~2
					entries = createEntries(data.get(i), new Formatter() {
						public FormattedQuery format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							return new FormattedQuery("\"" + term + "\"~2", tf.df);
						}
					});
					resolveIfNecessary(field, entries);
					fieldData.put("fuzzyQueries2" + prefixes.get(i), mashup(entries));
					
				}
			}
			
			final Random random = new Random(1492);
			String[] couples = new String[]{"HighFreq+HighFreq", "HighFreq+MedFreq", "HighFreq+LowFreq"};
			
			for (int i=0; i<couples.length; i++) {
				String[] parts = couples[i].split("\\+");
				if (data.get(prefixes.indexOf(parts[0])).length > 0 && data.get(prefixes.indexOf(parts[1])).length > 0) {
					
					// Boolean OR
					entries = createEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
								public FormattedQuery format(TermFreq tf1, TermFreq tf2) {
									return new FormattedQuery(tf1.term.utf8ToString() + " OR " + tf2.term.utf8ToString(),
											tf1.df, tf2.df);
								}
							});
					resolveIfNecessary(field, entries);
					fieldData.put("boolean" + parts[0] + "Or" + parts[1],	mashup(entries));
					
					// boolean AND
					entries = createEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
							public FormattedQuery format(TermFreq tf1, TermFreq tf2) {
								return new FormattedQuery(tf1.term.utf8ToString() + " AND " + tf2.term.utf8ToString(),
										tf1.df, tf2.df);
							}
						});
					resolveIfNecessary(field, entries);
					fieldData.put("boolean" + parts[0] + "And" + parts[1], mashup(entries));
					
					// proximity NEAR5
					entries = createEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
							public FormattedQuery format(TermFreq tf1, TermFreq tf2) {
								return new FormattedQuery(tf1.term.utf8ToString() + " NEAR5 " + tf2.term.utf8ToString(),
										tf1.df, tf2.df);
							}
						});
					resolveIfNecessary(field, entries);
					fieldData.put("boolean" + parts[0] + "Near5" + parts[1], mashup(entries));
					
					
				  // proximity NEAR2
					entries = createEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
							public FormattedQuery format(TermFreq tf1, TermFreq tf2) {
								return new FormattedQuery(tf1.term.utf8ToString() + " NEAR2 " + tf2.term.utf8ToString(),
										tf1.df, tf2.df);
							}
						});
					resolveIfNecessary(field, entries);
					fieldData.put("boolean" + parts[0] + "Near2" + parts[1], mashup(entries));
					
					// boolean NOT
					entries = createEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
							public FormattedQuery format(TermFreq tf1, TermFreq tf2) {
								return new FormattedQuery(tf1.term.utf8ToString() + " NOT " + tf2.term.utf8ToString(),
										tf1.df, tf2.df);
							}
						});
					resolveIfNecessary(field, entries);
					fieldData.put("boolean" + parts[0] + "Not" + parts[1], mashup(entries));
				}
			}
			
			
		}
		
		this.req = null;
	}
	
	
	public static class FormattedQuery {
		private String query;
		private long[] frequencies;
		private int totalHits = -1;
		
		public FormattedQuery(String query, long...frequencies) {
			this.query = query;
			this.frequencies = frequencies;
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(query);
			sb.append("\t");
			sb.append("#freq=");
			boolean first = true;
			for (long f: frequencies) {
				sb.append(first ? "" : ",");
				sb.append(f);
				first = false;
			}
			if (totalHits > -1) {
				sb.append("\t#numFound=");
				sb.append(totalHits);
			}
			sb.append("\n");
			return sb.toString();
		}
		@Override
		public int hashCode() {
			return this.query.hashCode();
		}
		
		public void resolveFound(SolrIndexSearcher searcher, QParser parser) throws SyntaxError, IOException {
			parser.setString(query);
			TopDocs hits = searcher.search(parser.parse(), 1);
			totalHits = hits.totalHits;
		}
	}
	
	public static class Formatter {
		public FormattedQuery format(TermFreq tf) {
			return new FormattedQuery(tf.term.utf8ToString(), tf.df);
		}
		public FormattedQuery format(TermFreq tf1, TermFreq tf2) {
			return new FormattedQuery(tf1.term.utf8ToString() + " " + tf2.term.utf8ToString(), tf1.df);
		}
	}
	
	
	protected Set<FormattedQuery> createEntries(TermFreq[] terms, Formatter formatter) throws ParseException, IOException {
		final Set<FormattedQuery> seen = new HashSet<FormattedQuery>();
		int count = 0;
		for (TermFreq tf: terms) {
			if (count > numQueries)
				break;
			if (tf.term.length >= 3) {
				FormattedQuery d = formatter.format(tf);
				if (d != null && !seen.contains(d)) {
					seen.add(d);
					count++;
				}
			}
		}
		return seen;
	}
	
	
	protected Set<FormattedQuery> createEntries(Random random, 
			TermFreq[] terms, TermFreq[] terms2, Formatter formatter) throws ParseException, IOException {
		
		final Set<FormattedQuery> seen = new HashSet<FormattedQuery>();
		
		int sanity = Math.max(terms.length, 10000);
		int count = 0;
    while(count < numQueries && sanity > 0) {
      final int idx1 = random.nextInt(terms.length);
      final int idx2 = random.nextInt(terms2.length);
      final TermFreq high = terms[idx1];
      final TermFreq medium = terms2[idx2];
      final FormattedQuery query = formatter.format(high, medium);
      if (query != null && !seen.contains(query)) {
        seen.add(query);
        count++;
      }
      sanity--;
    }
    return seen;
	}
	
	private String mashup(Set<FormattedQuery> seen) {
		StringBuilder out = new StringBuilder();
		for (FormattedQuery q: seen) {
			out.append(q.toString());
		}
		return out.toString();
	}

	private void resolveIfNecessary(String fieldName, Set<FormattedQuery> seen) throws SyntaxError, IOException {
		
		HashSet<FormattedQuery> toRemove = new HashSet<FormattedQuery>();
		SolrIndexSearcher searcher = req.getSearcher();
		SolrParams oldParams = req.getParams();
		ModifiableSolrParams newParams = new ModifiableSolrParams(oldParams);
		newParams.set(CommonParams.DF, fieldName);
		req.setParams(newParams);
		
		String defType = req.getParams().get(QueryParsing.DEFTYPE, QParserPlugin.DEFAULT_QTYPE);
		QParser parser = QParser.getParser("xx", defType, req);
		
		try {
			if (runTotalHitsResolution) {
			
	    	int i=0;
	    	log.info("Resolving performance queries (total hits) to go: " + seen.size());
	    	for (FormattedQuery q: seen) {
	    		try {
	    			q.resolveFound(searcher, parser);
	    		}
	    		catch (SyntaxError e1) {
	  				log.info("Removing invalid query: " + q.query);
	  				log.info(e1.getMessage());
	  				toRemove.add(q);
	  			}
	    		i++;
	    		if (i % 100 == 0) {
	    			log.info("Resolving performance queries (total hits) to go: " + (seen.size() - i));
	    		}
	    	}
	    }
			else {
				for (FormattedQuery q: seen) {
					parser.setString(q.query);
					try {
						parser.parse();
					}
					catch (Exception e) {
						log.info("Removing invalid query: " + q.query);
						log.info(e.getMessage());
						toRemove.add(q);
					}
				}
			}
		}
		finally {
			req.setParams(oldParams);
		}
		seen.removeAll(toRemove);
		
	}
	protected Map<String, String> getTermQueries(TermFreq[] topTerms, int topN, int upTo) {
		
    
    // First pass: get high/medium/low freq terms:

    long maxDF = topTerms[0].df;
    StringBuilder high = new StringBuilder();
    StringBuilder med = new StringBuilder();
    StringBuilder low = new StringBuilder();
    

    int counter = 0;
    StringBuilder writer = high;
    for(int idx=0;idx<topTerms.length;idx++) {
      final TermFreq tf = topTerms[idx];
      if (tf.df >= maxDF/10) {
        writer = high;
      } else if (tf.df >= maxDF/100) {
        if (writer != med) {
          counter = 0;
        }
        writer = med;
      } else {
        if (writer != low) {
          counter = 0;
        }
        writer = low;
      }

      if (counter++ < upTo) {
        writer.append(tf.term.utf8ToString() + "\\t=" + tf.df + "\n");
      }
    }
    Map<String, String>out = new HashMap<String, String>();
    out.put("HighFreq", high.toString());
    out.put("MedFreq", med.toString());
    out.put("LowFreq", low.toString());
    
    return out;
	}

  @Override
  public String getDescription() {
    return "Creates a list of queries for performance testing";
  }


  @Override
  public String getSource() {
    return "$URL: http://github.com/romanchyla/montysolr/contrib/examples/src/java/perf/CreatePerformanceQueriesHandler.java $";
  }
  

}
