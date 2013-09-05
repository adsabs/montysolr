package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.CommonQueryParserConfiguration;
import org.apache.lucene.queryparser.flexible.aqp.NestedParseException;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParser;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParserFull;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.MoreLikeThisQueryFixed;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.SecondOrderCollector;
import org.apache.lucene.search.SecondOrderCollector.FinalValueType;
import org.apache.lucene.search.SecondOrderCollectorAdsClassicScoringFormula;
import org.apache.lucene.search.SecondOrderCollectorCitedBy;
import org.apache.lucene.search.SecondOrderCollectorCites;
import org.apache.lucene.search.SecondOrderCollectorCitesRAM;
import org.apache.lucene.search.SecondOrderCollectorCitingTheMostCited;
import org.apache.lucene.search.SecondOrderCollectorOperatorExpertsCiting;
import org.apache.lucene.search.SecondOrderCollectorTopN;
import org.apache.lucene.search.SecondOrderQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.spans.SpanPositionRangeQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.util.Version;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.MoreLikeThisParams;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.MoreLikeThisHandler;
import org.apache.solr.handler.MoreLikeThisHandler.MoreLikeThisHelper;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.BoostQParserPlugin;
import org.apache.solr.search.DisMaxQParserPlugin;
import org.apache.solr.search.ExtendedDismaxQParserPlugin;
import org.apache.solr.search.FieldQParserPlugin;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.FunctionQParserPlugin;
import org.apache.solr.search.FunctionRangeQParserPlugin;
import org.apache.solr.search.LuceneQParserPlugin;
import org.apache.solr.search.NestedQParserPlugin;
import org.apache.solr.search.OldLuceneQParserPlugin;
import org.apache.solr.search.PrefixQParserPlugin;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.RawQParserPlugin;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.SortSpec;
import org.apache.solr.search.SpatialBoxQParserPlugin;
import org.apache.solr.search.SpatialFilterQParserPlugin;
import org.apache.solr.search.ValueSourceParser;
import org.apache.solr.search.function.PositionSearchFunction;
import org.apache.solr.servlet.SolrRequestParsers;
import org.apache.solr.util.SolrPluginUtils;


/*
 * I know this is confusing. This is called in the building phase,
 * by that time all the parsing was already done. All the parsers
 * here return a QUERY
 */

public class AqpAdsabsSubQueryProvider implements
		AqpFunctionQueryBuilderProvider {
	
	public static Map<String, AqpSubqueryParser> parsers = new HashMap<String, AqpSubqueryParser>();
	
  //TODO: make configurable
	static String[] citationSearchIdField = new String[]{"bibcode", "alternate_bibcode"};
	static String citationSearchRefField = "reference";
	
	static {
		parsers.put(LuceneQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), LuceneQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(OldLuceneQParserPlugin.NAME, new AqpSubqueryParser() {
	      public Query parse(FunctionQParser fp) throws ParseException {    		  
    		  QParser q = fp.subQuery(fp.getString(), OldLuceneQParserPlugin.NAME);
    		  return q.getQuery();
    		  
	      }
	    });
		parsers.put(FunctionQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), FunctionQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(PrefixQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), PrefixQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(BoostQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), BoostQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(DisMaxQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), DisMaxQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(ExtendedDismaxQParserPlugin.NAME, new AqpSubqueryParserFull() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), ExtendedDismaxQParserPlugin.NAME);
	    		  return simplify(q.getQuery());
		      }
		    }.configure(false)); // not analyzed
		parsers.put(FieldQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), FieldQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(RawQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {
		    	  String qstr = fp.getString();
		    	  if (!qstr.substring(0,2).equals("{!")) {
		    		  throw new ParseException(
		    				  "Raw query parser requires you to specify local params, eg: raw({!f=field}"+fp.getString()+")");
		    	  }
	    		  QParser q = fp.subQuery(qstr, RawQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(NestedQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), NestedQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(FunctionRangeQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), FunctionRangeQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(SpatialFilterQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), SpatialFilterQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		parsers.put(SpatialBoxQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), SpatialBoxQParserPlugin.NAME);
	    		  return q.getQuery();
		      }
		    });
		
	  // coreads(Q) - what people read: MoreLikeThese(topn(200,classic_relevance(Q)))
		parsers.put("trending", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				QParser aqp = fp.subQuery(fp.getString(), "aqp");
				Query innerQuery = aqp.parse();
				
				SolrQueryRequest req = fp.getReq();
				SolrIndexSearcher searcher = req.getSearcher();
				
				// find the 200 most interesting papers and collect their readers
				SecondOrderQuery discoverMostReadQ = new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorTopN(200));
				
				final StringBuilder readers = new StringBuilder();
				final HashSet<String> fieldsToLoad = new HashSet<String>();
				final String fieldName = "reader";
				fieldsToLoad.add(fieldName);
				
				try {
	        searcher.search(discoverMostReadQ, new Collector() {
						private Document d;
						private AtomicReader reader;
						private boolean firstPassed = false;
	        	@Override
	          public void setScorer(Scorer scorer) throws IOException {
	            //pass
	          }
	        	@Override
	          public void collect(int doc) throws IOException {
	        		d = reader.document(doc, fieldsToLoad);
	            for (String val: d.getValues(fieldName)) {
	            	if (firstPassed)
	            		readers.append(" ");
	            	readers.append(val);
	            	firstPassed = true;
	            }
	          }

	        	@Override
	          public void setNextReader(AtomicReaderContext context)
	              throws IOException {
	            this.reader = context.reader();
	          }

	        	@Override
	          public boolean acceptsDocsOutOfOrder() {
	            return false;
	          }
	        });
        } catch (IOException e) {
	        throw new ParseException(e.getMessage());
        }
				
        MoreLikeThisQuery mlt = new MoreLikeThisQueryFixed(readers.toString(), new String[] {fieldName}, 
        		new WhitespaceAnalyzer(Version.LUCENE_40), fieldName);
        SolrParams params = req.getParams();
        
        // configurable params
        mlt.setMinTermFrequency(1);
        mlt.setMinDocFreq(1);
        mlt.setMaxQueryTerms(512);
        mlt.setBoost(2.0f);
        mlt.setPercentTermsToMatch(0.0f);
        return mlt;
			}
		  }.configure(true)); // true=canBeAnalyzed
		
		parsers.put("pos", new AqpSubqueryParserFull() {
      @Override
      public Query parse(FunctionQParser fp) throws ParseException {
      	Query query = fp.parseNestedQuery();
      	int start = fp.parseInt();
      	int end = start;
      	
      	if (fp.hasMoreArguments()) {
  			  end = fp.parseInt();
  		  }
      	
      	if (fp.hasMoreArguments()) {
  			  throw new NestedParseException("Wrong number of arguments");
  		  }
      	
      	assert start > 0;
      	assert start <= end;
      	
      	SpanConverter converter = new SpanConverter();
      	converter.setWrapNonConvertible(true);
      	
      	SpanQuery spanQuery;
        try {
	        spanQuery = converter.getSpanQuery(new SpanConverterContainer(query, 1, true));
        } catch (QueryNodeException e) {
	        ParseException ex = new ParseException(e.getMessage());
	        ex.setStackTrace(e.getStackTrace());
	        throw ex;
        }
      	
      	return new SpanPositionRangeQuery(spanQuery, start-1, end); //lucene counts from zeroes
      }
    });
		
		// ADS Classic toy-implementation of the relevance score
		parsers.put("classic_relevance", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {
				
				Query innerQuery = fp.parseNestedQuery();
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorAdsClassicScoringFormula("cite_read_boost"));
	      }
	    }.configure(true)); // true=canBeAnalyzed
		parsers.put("cr", parsers.get("classic_relevance"));
		
		
  	// topn(int, Q, [relevance|sort-spec]) - limit results to the best top N (by their ranking or sort order)
		parsers.put("topn", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {
				int topN = -1;
				try {
					topN = fp.parseInt();
				}
				catch (NumberFormatException e) {
					throw new ParseException("The function signature is topn(int, query, [sort order]). Error: " + e.getMessage());
				}
				
				if (topN < 1) {  //|| topN > 50000 - previously, i was limiting the fields
					throw new ParseException("Hmmm, the first argument of your operator must be a positive number.");
				}
				
				QParser eqp = fp.subQuery(fp.parseId(), "aqp");
        Query innerQuery = eqp.getQuery();
        
        if (innerQuery == null) {
        	throw new ParseException("This query is empty: " + eqp.getString());
        }
        
        String sortOrRank = "score"; 
        if (fp.hasMoreArguments()) {
        	sortOrRank = fp.parseId();
        }
				
				if (sortOrRank.contains("\"") || sortOrRank.contains("\'")) {
					sortOrRank = sortOrRank.substring(1, sortOrRank.length()-1);
				}
				
				sortOrRank = sortOrRank.toLowerCase();
				if (sortOrRank.equals("score")) {
					return new SecondOrderQuery(innerQuery, null, 
							new SecondOrderCollectorTopN(topN));
				}
				else {
					Sort sortSpec = QueryParsing.parseSort(sortOrRank, fp.getReq());
					
					SolrIndexSearcher searcher = fp.getReq().getSearcher();
					
					TopFieldCollector collector;
				  try {
		        collector = TopFieldCollector.create(searcher.weightSort(sortSpec), topN, false, false, false, true);
	        } catch (IOException e) {
		        throw new ParseException("I am sorry, you can't use " + sortOrRank + " for topn() sorting. Reason: " + e.getMessage());
	        }
	        
	        return new SecondOrderQuery(innerQuery, null, 
							new SecondOrderCollectorTopN(sortOrRank, topN, collector));
				}
			}
		}.configure(false)); // true=canBeAnalyzed
		
	  // citations(P) - set of papers that have P in their reference list
		parsers.put("citations", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorCitedBy(citationSearchIdField, citationSearchRefField), false);
	      }
	    }.configure(true)); // true=canBeAnalyzed
		
	  
		// references(P) - set of papers that are in the reference list of P
		parsers.put("references", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorCitesRAM(citationSearchIdField, citationSearchRefField), false);
			}
		  }.configure(true)); // true=canBeAnalyzed
		
		// test for comparison with the citation query
		parsers.put("joincitations", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				try {
					// XXX: not sure if i can use several fields: citationSearchIdField
	        return JoinUtil.createJoinQuery("bibcode", false, "reference", innerQuery, 
	        		req.getSearcher(), ScoreMode.Avg);
        } catch (IOException e) {
        	throw new ParseException(e.getMessage());
        }
			}
		  }.configure(true)); // true=canBeAnalyzed
		
		parsers.put("joinreferences", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				try {
	        return JoinUtil.createJoinQuery("reference", true, "bibcode", innerQuery, 
	        		req.getSearcher(), ScoreMode.None); // will not work properly iff mode=Avg|Max
        } catch (IOException e) {
        	throw new ParseException(e.getMessage());
        }
			}
		  }.configure(true)); // true=canBeAnalyzed
		
		
		// useful() = what experts are citing; ADS Classic implementation
		// is: references(topn(200, classic_relevance(Q)))
		parsers.put("useful", new AqpSubqueryParserFull() { // this function values can be analyzed
      public Query parse(FunctionQParser fp) throws ParseException {          
        Query innerQuery = fp.parseNestedQuery();
        
        return  new SecondOrderQuery( // references
			        		new SecondOrderQuery( // topn
			        				new SecondOrderQuery(innerQuery, // classic_relevance
			        						new SecondOrderCollectorAdsClassicScoringFormula("cite_read_boost")), 
	    						new SecondOrderCollectorTopN(200)),
			    			new SecondOrderCollectorCitesRAM(citationSearchIdField, citationSearchRefField));
        
	      };
    }.configure(true)); // true=canBeAnalyzed
		
		// original implementation of useful() -- using special collector
    parsers.put("useful2", new AqpSubqueryParserFull() { // this function values can be analyzed
      public Query parse(FunctionQParser fp) throws ParseException {          
        Query innerQuery = fp.parseNestedQuery();
        SolrQueryRequest req = fp.getReq();
        String boostField = "cite_read_boost";
        return new SecondOrderQuery(innerQuery, null, 
        		new SecondOrderCollectorOperatorExpertsCiting(citationSearchIdField, citationSearchRefField, boostField));
      }
      }.configure(true)); // true=canBeAnalyzed
    
    
    // reviews() = what is cited by experts; ADS Classic implementation
		// is: citations(topn(200, classic_relevance(Q)))
		parsers.put("reviews", new AqpSubqueryParserFull() { // this function values can be analyzed
      public Query parse(FunctionQParser fp) throws ParseException {          
        Query innerQuery = fp.parseNestedQuery();
        
        return  new SecondOrderQuery( // citations
			        		new SecondOrderQuery( // topn
			        				new SecondOrderQuery(innerQuery, // classic_relevance
			        						new SecondOrderCollectorAdsClassicScoringFormula("cite_read_boost")), 
	    						new SecondOrderCollectorTopN(200)),
	    						new SecondOrderCollectorCitedBy(citationSearchIdField, citationSearchRefField));
        
	      };
    }.configure(true)); // true=canBeAnalyzed
		parsers.put("instructive", parsers.get("reviews"));
		
    // original impl of reviews() = find papers that cite the most cited papers
    parsers.put("reviews2", new AqpSubqueryParserFull() { // this function values can be analyzed
      public Query parse(FunctionQParser fp) throws ParseException {          
        Query innerQuery = fp.parseNestedQuery();
        SolrQueryRequest req = fp.getReq();
        String boostField = "cite_read_boost";
        return new SecondOrderQuery(innerQuery, null, 
        		new SecondOrderCollectorCitingTheMostCited(citationSearchIdField, citationSearchRefField, boostField));
      }
      }.configure(true)); // true=canBeAnalyzed
		parsers.put("citis", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorCites(citationSearchIdField, citationSearchRefField), false);
				
			}
		  }.configure(true)); // true=canBeAnalyzed
		parsers.put("aqp", new AqpSubqueryParserFull() {
      public Query parse(FunctionQParser fp) throws ParseException {          
        QParser q = fp.subQuery(fp.getString(), "aqp");
        return q.getQuery();
      }
    }.configure(false)); // not analyzed
		
		parsers.put("edismax_nonanalyzed", new AqpSubqueryParserFull() { // used for nodes that were already analyzed
      public Query parse(FunctionQParser fp) throws ParseException {
        final String original = fp.getString();
        QParser ep = fp.subQuery("xxx", ExtendedDismaxQParserPlugin.NAME);
        Query q = ep.getQuery();
        QParser fakeParser = new QParser(original, null, null, null) {
          @Override
          public Query parse() throws ParseException {
            String[] parts = getString().split(":");
            return new TermQuery(new Term(parts[0], original));
          }
        };
        return simplify(reParse(q, fakeParser, TermQuery.class));
      }
    }.configure(false)); // not analyzed
		parsers.put("edismax_combined_aqp", new AqpSubqueryParserFull() { // will decide whether new aqp() parse is needed
      public Query parse(FunctionQParser fp) throws ParseException {
        final String original = fp.getString();
        QParser eqp = fp.subQuery(original, ExtendedDismaxQParserPlugin.NAME);
        Query q = eqp.getQuery();
        return simplify(reParse(q, fp, null));
      }
      protected Query swimDeep(DisjunctionMaxQuery query) throws ParseException {
        ArrayList<Query> parts = query.getDisjuncts();
        for (int i=0;i<parts.size();i++) {
          Query oldQ = parts.get(i);
          String field = null;
          if (oldQ instanceof TermQuery) {
            field = toBeAnalyzedAgain(((TermQuery) oldQ));
          }
          else if(oldQ instanceof BooleanQuery) {
            List<BooleanClause>clauses = ((BooleanQuery) oldQ).clauses();
            if (clauses.size()>0) {
              Query firstQuery = clauses.get(0).getQuery();
              if (firstQuery instanceof TermQuery) {
                field = toBeAnalyzedAgain(((TermQuery) firstQuery));
              }
            }
          }
          if (field!=null) {
            parts.set(i, reAnalyze(field, getParser().getString(), oldQ.getBoost()));
          }
          else {
            parts.set(i, swimDeep(oldQ));
          }
        }
        return query;
      }
      
      private String toBeAnalyzedAgain(TermQuery q) {
        String f = q.getTerm().field();
        if (f.equals("author")) {
          return "author";
        }
        return null;
        //return f; // always re-analyze
      }
      private Query reAnalyze(String field, String value, float boost) throws ParseException {
        QParser fParser = getParser();
        QParser aqp = fParser.subQuery(field+ ":"+fParser.getString(), "aqp");
        Query q = aqp.getQuery();
        q.setBoost(boost);
        return q;
      }
    }.configure(false)); // not analyzed
		parsers.put("edismax_always_aqp", new AqpSubqueryParserFull() { // will use edismax to create top query, but the rest is done by aqp
      public Query parse(FunctionQParser fp) throws ParseException {
        final String original = fp.getString();
        QParser eqp = fp.subQuery("xxx", ExtendedDismaxQParserPlugin.NAME);
        fp.setString(original);
        Query q = eqp.getQuery();
        return simplify(reParse(q, fp, null));
      }
      protected Query swimDeep(DisjunctionMaxQuery query) throws ParseException {
        ArrayList<Query> parts = query.getDisjuncts();
        for (int i=0;i<parts.size();i++) {
          Query oldQ = parts.get(i);
          String field = null;
          if (oldQ instanceof TermQuery) {
            field = ((TermQuery)oldQ).getTerm().field();
          }
          else if(oldQ instanceof BooleanQuery) {
            List<BooleanClause>clauses = ((BooleanQuery) oldQ).clauses();
            if (clauses.size()>0) {
              Query firstQuery = clauses.get(0).getQuery();
              if (firstQuery instanceof TermQuery) {
                field = ((TermQuery) firstQuery).getTerm().field();
              }
            }
          }
          if (field!=null) {
            parts.set(i, reAnalyze(field, getParser().getString(), oldQ.getBoost()));
          }
          else {
            parts.set(i, swimDeep(oldQ));
          }
        }
        return query;
      }
      
      private Query reAnalyze(String field, String value, float boost) throws ParseException {
        QParser fParser = getParser();
        QParser aqp = fParser.subQuery(field+ ":"+fParser.getString(), "aqp");
        Query q = aqp.getQuery();
        q.setBoost(boost);
        return q;
      }
    }.configure(false)); // not analyzed
		
		parsers.put("tweak", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {
				
				String configuration = fp.parseId();
				Query q = fp.parseNestedQuery();
				
				MultiMapSolrParams params = SolrRequestParsers.parseQueryString(configuration);
				
				if (params.get("collector_final_value", null) != null) {
					String cfv = params.get("collector_final_value", "avg");
					if (q instanceof SecondOrderQuery) {
						SecondOrderCollector collector = ((SecondOrderQuery) q).getcollector();
						try {
							collector.setFinalValueType(SecondOrderCollector.FinalValueType.valueOf(cfv));
						}
						catch (IllegalArgumentException e) {
							throw new ParseException("Wrong parameter: " + e.getMessage());
						}
					}
				}
        return q;
      }
		});
	};

	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
		throws QueryNodeException {
		
		
		AqpSubqueryParser provider = parsers.get(funcName);
		if (provider == null)
			return null;
			
		AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
		
		SolrQueryRequest req = reqAttr.getRequest();
		if (req == null)
			return null;
		
		
		SolrParams localParams = reqAttr.getLocalParams();
		if (localParams == null) {
			localParams = new ModifiableSolrParams();
		}
		else {
			localParams = new ModifiableSolrParams(localParams);
		}
		
		if (localParams.get(QueryParsing.DEFTYPE, null) == null) {
			((ModifiableSolrParams) localParams).set(QueryParsing.DEFTYPE, "aqp");
		}
		AqpFunctionQParser parser = new AqpFunctionQParser("", localParams, 
				reqAttr.getParams(), req);
		return new AqpSubQueryTreeBuilder(provider, parser);
				
	}

	private void getSpan(QueryNode node, Integer[] span) {
		List<QueryNode> children = node.getChildren();
		swimDeep(children.get(0), span);
		swimDeep(children.get(children.size()-1), span);
	}

	private void swimDeep(QueryNode node, Integer[] span) {
		
		if (node instanceof AqpANTLRNode) {
			int i = ((AqpANTLRNode) node).getTokenStart();
			int j = ((AqpANTLRNode) node).getTokenEnd();
			
			if(j>i) {
				if (i != -1 && i < span[0]) {
					span[0] = i;
				}
				if (j != -1 && j > span[1]) {
					span[1] = j;
				}
			}
		}
		if (!node.isLeaf()) {
			for (QueryNode child: node.getChildren()) {
				swimDeep(child, span);
			}
		}
		
	}

}
