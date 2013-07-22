package perf;

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
import org.apache.lucene.misc.GetTermInfo;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import perf.CreateQueries.TermFreq;

public class CreatePerformanceQueriesHandler extends RequestHandlerBase {
  
	private CreateQueries extractor = new CreateQueries();
  private int numQueries = 500;
  
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
			try {
				topTerms = CreateQueries.getTopTermsByDocFreq(reader, field, topN, false);
			}
			catch (RuntimeException e) {
				if (e.getMessage().contains("index is too small")) {
					continue;
				}
				throw e;
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
	    
			
			Map<String, String> fieldData = new HashMap<String, String>();
			rsp.add(field, fieldData);
			
			List<String> prefixes = Arrays.asList("HighFreq", "MedFreq", "LowFreq");
			List<TermFreq[]> data = Arrays.asList(highFreqTerms, mediumFreqTerms, lowFreqTerms);
			
			for (int i=0; i<prefixes.size(); i++) {
				if (data.get(i).length > 0) {
					fieldData.put("termQueries" + prefixes.get(i), formatEntries(data.get(i), new Formatter()));
					
					fieldData.put("wildcardQueriesBeginStar" + prefixes.get(i), formatEntries(data.get(i), new Formatter() {
						public String format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							return "*" + term.substring(term.length()/2) + "\\t>=" + tf.df + "\n";
						}
					}));
					
					fieldData.put("wildcardQueriesEndStar" + prefixes.get(i), formatEntries(data.get(i), new Formatter() {
						public String format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							return term.substring(0, term.length()/2) + "*" + "\\t>=" + tf.df + "\n";
						}
					}));
					
					fieldData.put("wildcardQueriesMidStar" + prefixes.get(i), formatEntries(data.get(i), new Formatter() {
						public String format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							String p1 = term.substring(0, term.length()/2) + "*";
							return p1 + term.substring(term.length() - Math.max(1, term.length() - p1.length() - 3));
						}
					}));
					
					fieldData.put("fuzzyQueries1" + prefixes.get(i), formatEntries(data.get(i), new Formatter() {
						public String format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							return "\"" + term + "\"~1" + "\\t>=" + tf.df + "\n";
						}
					}));
					
					fieldData.put("fuzzyQueries1" + prefixes.get(i), formatEntries(data.get(i), new Formatter() {
						public String format(TermFreq tf) {
							String term = tf.term.utf8ToString();
							return "\"" + term + "\"~2" + "\\t>=" + tf.df + "\n";
						}
					}));
					
				}
			}
			
			final Random random = new Random(1492);
			String[] couples = new String[]{"HighFreq+HighFreq", "HighFreq+MedFreq", "HighFreq+LowFreq"};
			
			for (int i=0; i<couples.length; i++) {
				String[] parts = couples[i].split("\\+");
				if (data.get(prefixes.indexOf(parts[0])).length > 0 && data.get(prefixes.indexOf(parts[1])).length > 0) {
					
					fieldData.put("boolean" + parts[0] + "Or" + parts[1], 
							formatEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
								public String format(TermFreq tf1, TermFreq tf2) {
									return tf1.term.utf8ToString() + " OR " + tf2.term.utf8ToString() + "\\t>=" + tf1.df + "\n";
								}
							}));
					fieldData.put("boolean" + parts[0] + "And" + parts[1], 
							formatEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
								public String format(TermFreq tf1, TermFreq tf2) {
									return tf1.term.utf8ToString() + " AND " + tf2.term.utf8ToString() + "\\t>=" + tf1.df + "\n";
								}
							}));
					fieldData.put("boolean" + parts[0] + "Near5" + parts[1], 
							formatEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
								public String format(TermFreq tf1, TermFreq tf2) {
									return tf1.term.utf8ToString() + " NEAR5 " + tf2.term.utf8ToString() + "\\t>=" + tf1.df + "\n";
								}
							}));
					fieldData.put("boolean" + parts[0] + "Near2" + parts[1], 
							formatEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
								public String format(TermFreq tf1, TermFreq tf2) {
									return tf1.term.utf8ToString() + " NEAR2 " + tf2.term.utf8ToString() + "\\t>=" + tf1.df + "\n";
								}
							}));
					fieldData.put("boolean" + parts[0] + "Not" + parts[1], 
							formatEntries(random, data.get(prefixes.indexOf(parts[0])), data.get(prefixes.indexOf(parts[1])),
							new Formatter() {
								public String format(TermFreq tf1, TermFreq tf2) {
									return tf1.term.utf8ToString() + " NOT " + tf2.term.utf8ToString() + "\\t>=" + tf1.df + "\n";
								}
							}));
				}
			}
			
			
		}
		
	}
	
	
	public static class Formatter {
		
		public String format(TermFreq tf) {
			return tf.term.utf8ToString() + "\\t=" + tf.df + "\n";
		}
		public String format(TermFreq tf1, TermFreq tf2) {
			return tf1.term.utf8ToString() + " " + tf2.term.utf8ToString() + "\\t>=" + tf1.df + "\n";
		}
	}
	
	
	protected String formatEntries(TermFreq[] terms, Formatter formatter) {
		final Set<String> seen = new HashSet<String>();
		StringBuilder out = new StringBuilder();
		for (TermFreq tf: terms) {
			
			if (tf.term.length >= 3) {
				String d = formatter.format(tf);
				if (d != null && !seen.contains(d)) {
					seen.add(d);
					out.append(d);
				}
			}
			
		}
		return out.toString();
	}
	
	protected String formatEntries(Random random, TermFreq[] terms, TermFreq[] terms2, Formatter formatter) {
		StringBuilder out = new StringBuilder();
		final Set<String> seen = new HashSet<String>();
		
		int sanity = Math.max(terms.length, 10000);
		int count = 0;
    while(count < numQueries && sanity > 0) {
      final int idx1 = random.nextInt(terms.length);
      final int idx2 = random.nextInt(terms2.length);
      final TermFreq high = terms[idx1];
      final TermFreq medium = terms2[idx2];
      final String query = formatter.format(high, medium);
      if (query != null && !seen.contains(query)) {
        seen.add(query);
        count++;
        out.append(query);
      }
      sanity--;
    }
		return out.toString();
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
