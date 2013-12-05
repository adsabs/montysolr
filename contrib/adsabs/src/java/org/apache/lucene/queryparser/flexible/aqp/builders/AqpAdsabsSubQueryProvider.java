package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.io.IOException;
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
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.NestedParseException;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParser;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParserFull;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.MoreLikeThisQueryFixed;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.SecondOrderCollector;
import org.apache.lucene.search.SecondOrderCollector.FinalValueType;
import org.apache.lucene.search.CacheWrapper;
import org.apache.lucene.search.SecondOrderCollectorAdsClassicScoringFormula;
import org.apache.lucene.search.SecondOrderCollectorCacheWrapper;
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
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.BoostQParserPlugin;
import org.apache.solr.search.CitationLRUCache;
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
import org.apache.solr.search.SpatialBoxQParserPlugin;
import org.apache.solr.search.SpatialFilterQParserPlugin;
import org.apache.solr.servlet.SolrRequestParsers;


/**
 * I know this is confusing. This is called in the building phase,
 * by that time all the parsing was already done. All the parsers
 * here return a QUERY
 * 
 * @see AqpFunctionQueryBuilderProvider
 */
public class AqpAdsabsSubQueryProvider implements
AqpFunctionQueryBuilderProvider {

	
	public static Map<String, AqpSubqueryParser> parsers = new HashMap<String, AqpSubqueryParser>();

	//TODO: make configurable
	static String[] citationSearchIdField = new String[]{"bibcode", "alternate_bibcode"};
	static String citationSearchRefField = "reference";

	static {
		
		/* @api.doc
		 * 
		 * def lucene(query):
		 * 		"""
		 *    Default Lucene query parser
		 * 		"""
		 */
		parsers.put(LuceneQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				QParser q = fp.subQuery(fp.getString(), LuceneQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		/**
		 * comment XXX
		 */
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
		parsers.put(DisMaxQParserPlugin.NAME, new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				QParser q = fp.subQuery(fp.getString(), DisMaxQParserPlugin.NAME);
				return simplify(q.getQuery());
			}
		});
		parsers.put(ExtendedDismaxQParserPlugin.NAME, new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				QParser q = fp.subQuery(fp.getString(), ExtendedDismaxQParserPlugin.NAME);
				return simplify(q.getQuery());
			}
		});
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

		/* @api.doc
		 * 
		 * def trending(query):
		 * 		"""
		 *    Finds the 200 most interesting papers first, then uses
		 *    this initial set to collect *all* readers of these papers
		 *    and then finds other docs these readers read.
		 *    
		 *    Technical note: we are using modified MoreLikeThis
		 *    functionality, with the following parameters:
		 *    
		 *     - setMinTermFrequency(0)
		 *		 - setMinDocFreq(2)
		 *		 - setMaxQueryTerms(200)
		 *     - setBoost(2.0f)
		 *     - setPercentTermsToMatch(0.0f)
		 *     
		 *    @since 40.2.0.0 
		 * 		
		 * 		"""
		 *    return "trending(%s)" % query
		 */
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
				discoverMostReadQ.getcollector().setFinalValueType(FinalValueType.ABS_COUNT);

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

				// configurable params
				mlt.setMinTermFrequency(0);
				mlt.setMinDocFreq(2);
				mlt.setMaxQueryTerms(200);
				mlt.setBoost(2.0f);
				mlt.setPercentTermsToMatch(0.0f);

				//try {
					//  Query q = mlt.rewrite(req.getSearcher().getIndexReader());
				//  System.out.println(q);
				//} catch (IOException e) {
				//}

				return mlt;
			}
		});

		
		/* @api.doc
		 * 
		 * def pos(query, start, end=None):
		 * 		"""
		 *    Positional search; returns only documents that
		 *    are in the given position (range).
		 *    
		 *    Example: 
		 *    
		 *    	```pos(author:accomazzi, 1)``` finds the papers
		 *    			where 'accomazzi' is the first author
		 *      
		 *      ```pos(author:accomazzi, 1, 1)``` finds the papers
		 *          where 'accomazzi' is the only author
		 *          
		 *      ```pos(author:accomazzi, 1, 5)``` finds the papers
		 *          where 'accomazzi' is listed as 1st-5th author
		 *      
		 *    Technical note:
		 *    
		 *    This query will work only for indexes that contain
		 *    positional information, such as: title, author. It
		 *    will not work for other indexes, such as bibcode,
		 *    keyword. Though we'll still allow you to query 
		 *    them (even if it is useless).
		 *    
		 *    
		 *    Syntax note:
		 *    
		 *    The old ADS Classic syntax was: ```^accomazzi$```
		 *    where ```^``` means *first* and ```$``` means *last*.
		 *    ADS Classic cannot search for position ranges, but
		 *    the new system cannot search for the last (yet). It
		 *    is low priority now. 
		 *     
		 *    @since 40.2.0.0 
		 * 		
		 * 		"""
		 *    return "pos(%s, %s, %s)" % (query, start, end or start)
		 */
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

		/* @api.doc
		 * 
		 * def classic_relevance(query, ratio=0.5):
		 * 		"""
		 *    Toy-implementation of the ADS Classic relevance score
		 *    algorithm. You can wrap any query and obtain the 
		 *    hits sorted in the ADS Classic ways (sort of)
		 *    
		 *    Technical note:
		 *    
		 *    This is inefficient and not to be used in production. 
		 *    We apply the **boost factor** that was computed beforehand
		 *    by ADS Classic to each document that matches. (We are not
		 *    scoring docs that are not selected by Lucene). 
		 *    The boost factor is inside ```cite_read_boost``` field - 
		 *    we'll use cache to retrieve these values fast, 
		 *    but it is still inefficient 
		 *    
		 *
		 *    ADS Classic score is implemented as:
		 * 
		 *    ```new_score = (0.5 * norm(lucene_score)) + (0.5 * cite_read_boost)```
		 * 
		 *    where:
		 *    
		 *       norm(LS) = normalized score (in this case it will be a Lucene
		 *                  score, normalized to be in the range 1-0, where 
		 *                  1 = the first, best hit; LS/MaximumLuceneScore
		 *                  
		 *       cite_read_boost = the document boosts are combination of 
		 *                  normalized reads and cites: 
		 *                  ```cite_read_boost = log(1 + cites + norm_reads)```
		 *                  
		 *                  where:
		 *                  
		 *                  	```norm_reads``` are normalized values for 
		 *                    reads over the past two years
		 *                    
		 *                    
		 *     
		 *    @experimental
		 *    @synonym cr()
		 *    @since 40.2.2.0
		 *    @since 40.3.0.1 - added parameter to configure ratio
		 * 		
		 * 		"""
		 *    return "classic_relevance(%s, %0.2f)" % (query,ratio)
		 */
		parsers.put("classic_relevance", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {

				Query innerQuery = fp.parseNestedQuery();
				float ratio = 0.5f;
				if (fp.hasMoreArguments()) {
					ratio = fp.parseFloat();
				}
				
				if (ratio < 0 || ratio > 1.0f) {
					throw new ParseException("The ratio must be in the range 0.0-1.0");
				}
				
				final SolrQueryRequest req = fp.getReq();
				@SuppressWarnings("rawtypes")
				final CitationLRUCache cache = (CitationLRUCache) req.getSearcher().getCache("citations-cache");
				
				
				CacheWrapper citationsWrapper = new SecondOrderCollectorCacheWrapper() {
					@Override
					public AtomicReader getAtomicReader() {
						return req.getSearcher().getAtomicReader();
					}
					@Override
				  public int[] getLuceneDocIds(int sourceDocid) {
					  return cache.getCitations(sourceDocid);
				  }
					
					@SuppressWarnings("unchecked")
          @Override
					public int getLuceneDocId(int sourceDocid, Object sourceValue) {
					  return (Integer) cache.get(sourceValue);
				  }
					@Override
          public int internalHashCode() {
	          return cache.hashCode();
          }
					@Override
          public String internalToString() {
	          return cache.name();
          }
				};
				
				
				
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorAdsClassicScoringFormula(citationsWrapper, "cite_read_boost", ratio));
			}
		});
		parsers.put("cr", parsers.get("classic_relevance"));


		/* @api.doc
		 * 
		 * def topn(max, query, spec=None):
		 * 		"""
		 *    Limit results to the best top N (by their ranking or sort order)
		 *    
		 *    @param max
		 *    	- integer, how many results should be considered
		 *    @param query
		 *    	- query object
		 *    @param spec
		 *    	- str, can be either 'relevance' or
		 *        sort specification in the SOLR format
		 *    
		 *    Example: 
		 *    
		 *    	```topn(200, title:hubble)``` returns only the
		 *         first 200 papers based on the relevancy score
		 *         
		 *      ```topn(200, citations(title:hubble), citation_count desc)``` 
		 *         returns only the
		 *         first 200 papers, but because the results are 
		 *         sorted by number of citations, you will get the first
		 *         200 most cited papers
		 *         
		 *      
		 *    Technical note:
		 *    
		 *    We do not impose limit of hits that you can return with 
		 *    this operator. But you must be aware that the query is
		 *    going to be slower than normal queries.
		 *    
		 *    @since 40.2.2.0
		 *    """
		 *    return "topn(%s, %s, '%s')" % (int(max), query, spec or 'score')
		 *    
		 */
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

				sortOrRank = sortOrRank.toLowerCase();

				if (sortOrRank.contains("\"") || sortOrRank.contains("\'")) {
					sortOrRank = sortOrRank.substring(1, sortOrRank.length()-1);
				}


				if (sortOrRank.equals("score")) {
					return new SecondOrderQuery(innerQuery, null, 
							new SecondOrderCollectorTopN(topN));
				}
				else {
					Sort sortSpec = QueryParsing.parseSort(sortOrRank, fp.getReq());

					SolrIndexSearcher searcher = fp.getReq().getSearcher();

					TopFieldCollector collector;
					try {
						collector = TopFieldCollector.create(searcher.weightSort(sortSpec), topN, false, true, true, true);
					} catch (IOException e) {
						throw new ParseException("I am sorry, you can't use " + sortOrRank + " for topn() sorting. Reason: " + e.getMessage());
					}

					return new SecondOrderQuery(innerQuery, null, 
							new SecondOrderCollectorTopN(sortOrRank, topN, collector));
				}
			}
		});

		/* @api.doc
		 * 
		 * def citations(query):
		 * 		"""
		 *    Finds set of papers that have **P** in their reference list
		 *    
		 *    'P' is the set of papers that will be selected by the query
		 *    
		 *    Example: 
		 *    
		 *    	```citations(title:hubble)``` returns papers (potentionally
		 *         hundreds of thousands!) that are citing papers P
		 *         
		 *         
		 *      ```citations(citations(author:huchra))``` returns papers 
		 *         (potentionally millions!) that are citing papers that
		 *         are citing papers written by 'huchra'
		 *         
		 *      
		 *    Technical note:
		 *    
		 *    We have optimized this query so that it works well with 
		 *    millions of hits. But don't expect miracles. 0.5M hits
		 *    takes few hundred milliseconds; 2M hits will take seconds
		 *    (but less than 10s, since that is the speed the old desktop
		 *    did it)
		 *    
		 *    
		 *    @since 40.1.0.0
		 *    """
		 *    return "citations(%s)" % (query,)
		 *    
		 */
		parsers.put("citations", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				
				final SolrQueryRequest req = fp.getReq();
				@SuppressWarnings("rawtypes")
				final CitationLRUCache cache = (CitationLRUCache) req.getSearcher().getCache("citations-cache");
				
				
				CacheWrapper citationsWrapper = new SecondOrderCollectorCacheWrapper() {
					@Override
					public AtomicReader getAtomicReader() {
						return req.getSearcher().getAtomicReader();
					}
					@Override
				  public int[] getLuceneDocIds(int sourceDocid) {
					  return cache.getCitations(sourceDocid);
				  }
					
					@SuppressWarnings("unchecked")
          @Override
					public int getLuceneDocId(int sourceDocid, Object sourceValue) {
					  return (Integer) cache.get(sourceValue);
				  }
					@Override
          public int internalHashCode() {
	          return cache.hashCode();
          }
					@Override
          public String internalToString() {
	          return cache.name();
          }
				};
				
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorCitedBy(citationsWrapper), false);
			}
		});


		/* @api.doc
		 * 
		 * def references(query):
		 * 		"""
		 *    Finds set of papers that **are** in the references list of **P** 
		 *    
		 *    'P' is the set of papers that will be selected by the query
		 *    
		 *    Example: 
		 *    
		 *    	```references(title:hubble)``` returns papers (potentionally
		 *         few hundred) that are **cited by** papers that have 'hubble'
		 *         in their title
		 *         
		 *         
		 *      ```references(author:huchra)``` returns papers 
		 *         that your favorite author cites
		 *         
		 *      
		 *    Technical note:
		 *    
		 *    The same caveats as citations()
		 *    
		 *    
		 *    @since 40.1.0.0
		 *    """
		 *    return "references(%s)" % (query,)
		 *    
		 */
		parsers.put("references", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				
				final SolrQueryRequest req = fp.getReq();
				@SuppressWarnings("rawtypes")
        final CitationLRUCache cache = (CitationLRUCache) req.getSearcher().getCache("citations-cache");
				
				
				CacheWrapper referencesWrapper = new SecondOrderCollectorCacheWrapper() {
					@Override
					public AtomicReader getAtomicReader() {
						return req.getSearcher().getAtomicReader();
					}
					@Override
				  public int[] getLuceneDocIds(int sourceDocid) {
					  return cache.getReferences(sourceDocid);
				  }
					
					@SuppressWarnings("unchecked")
          @Override
					public int getLuceneDocId(int sourceDocid, Object sourceValue) {
					  return (Integer) cache.get(sourceValue);
				  }
					@Override
          public int internalHashCode() {
	          return cache.hashCode();
          }
					@Override
          public String internalToString() {
	          return cache.name();
          }
				};
				
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorCitesRAM(referencesWrapper), false);
			}
		});

		/* @api.doc
		 * 
		 * def joincitations(query):
		 * 		"""
		 *    Equivalent of citations() but implemented using lucene block-join 
		 *    
		 *    
		 *    @experimental
		 *    @access devel
		 *    @since 40.1.0.0
		 *    """
		 *    return "joincitations(%s)" % (query,) 
		 */
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
		});

		/* @api.doc
		 * 
		 * def joinreferences(query):
		 * 		"""
		 *    Equivalent of references() but implemented using lucene block-join 
		 *    
		 *    
		 *    @experimental
		 *    @access devel
		 *    @since 40.1.0.0
		 *    """
		 *    return "joinreferences(%s)" % (query,)
		 *     
		 */
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
		});


		/* @api.doc
		 * 
		 * def useful(query):
		 * 		"""
		 *    What experts are citing; this mimics the ADS Classic implementation
		 *    ```references(topn(200, classic_relevance(Q)))```
		 *    
		 *    In other words, this will first find papers using the query, 
		 *    it will re-score them using the ADS classic ranking formula,
		 *    then selects 200 top papers. And then get **references from** these
		 *    200 papers.
		 *    
		 *    @experimental
		 *    @since 40.2.0.0
		 *    """
		 *    return "useful(%s)" % (query,)
		 *     
		 */
		parsers.put("useful", new AqpSubqueryParserFull() { // this function values can be analyzed
			public Query parse(FunctionQParser fp) throws ParseException {          
				Query innerQuery = fp.parseNestedQuery();
				
				final SolrQueryRequest req = fp.getReq();
				@SuppressWarnings("rawtypes")
				final CitationLRUCache cache = (CitationLRUCache) req.getSearcher().getCache("citations-cache");
				
				
				CacheWrapper citationsWrapper = new SecondOrderCollectorCacheWrapper() {
					@Override
					public AtomicReader getAtomicReader() {
						return req.getSearcher().getAtomicReader();
					}
					@Override
				  public int[] getLuceneDocIds(int sourceDocid) {
					  return cache.getCitations(sourceDocid);
				  }
					
					@SuppressWarnings("unchecked")
          @Override
					public int getLuceneDocId(int sourceDocid, Object sourceValue) {
					  return (Integer) cache.get(sourceValue);
				  }
					@Override
          public int internalHashCode() {
	          return cache.hashCode();
          }
					@Override
          public String internalToString() {
	          return cache.name();
          }
				};
				
				return  new SecondOrderQuery( // references
						new SecondOrderQuery( // topn
								new SecondOrderQuery(innerQuery, // classic_relevance
										new SecondOrderCollectorAdsClassicScoringFormula(citationsWrapper, "cite_read_boost")), 
										new SecondOrderCollectorTopN(200)),
										new SecondOrderCollectorCitesRAM(citationsWrapper));

			};
		});

		/* @api.doc
		 * 
		 * def useful2(query):
		 * 		"""
		 *    What experts are citing; original implementation of useful() 
		 *    -- using special collector
		 *    
		 *    Technical details:
		 *    
		 *    This function will add the cite_read_boost factor (from the
		 *    1st order set) to the score (of the 2nd order result set).
		 *    If no boost factor is available, doc will be penalized by 
		 *    having its score lowered by 20%
		 *    
		 *    @access devel
		 *    @experimental
		 *    @since 40.1.2.0
		 *    """
		 *    return "useful2(%s)" % (query,)
		 *     
		 */
		parsers.put("useful2", new AqpSubqueryParserFull() { // this function values can be analyzed
			public Query parse(FunctionQParser fp) throws ParseException {          
				Query innerQuery = fp.parseNestedQuery();
				
				final SolrQueryRequest req = fp.getReq();
				@SuppressWarnings("rawtypes")
        final CitationLRUCache cache = (CitationLRUCache) req.getSearcher().getCache("citations-cache");
				
				
				CacheWrapper citationsWrapper = new SecondOrderCollectorCacheWrapper() {
					@Override
					public AtomicReader getAtomicReader() {
						return req.getSearcher().getAtomicReader();
					}
					@Override
				  public int[] getLuceneDocIds(int sourceDocid) {
					  return cache.getCitations(sourceDocid);
				  }
					
					@SuppressWarnings("unchecked")
          @Override
					public int getLuceneDocId(int sourceDocid, Object sourceValue) {
					  return (Integer) cache.get(sourceValue);
				  }
					@Override
          public int internalHashCode() {
	          return cache.hashCode();
          }
					@Override
          public String internalToString() {
	          return cache.name();
          }
				};
				//TODO: make configurable the name of the field
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorOperatorExpertsCiting(citationsWrapper, "cite_read_boost"));
			}
		});


		/* @api.doc
		 * 
		 * def reviews(query):
		 * 		"""
		 *    What is cited by experts; this mimics the ADS Classic implementation
		 *    is: ```citations(topn(200, classic_relevance(Q)))```
		 *    
		 *    In other words, this will first find papers using the query, 
		 *    it will re-score them using the ADS classic ranking formula,
		 *    then selects 200 top papers. And then get **citations for** these
		 *    200 papers.
		 *    
		 *    @experimental
		 *    @since 40.2.0.0
		 *    """
		 *    return "reviews(%s)" % (query,)
		 *     
		 */
		parsers.put("reviews", new AqpSubqueryParserFull() { // this function values can be analyzed
			public Query parse(FunctionQParser fp) throws ParseException {          
				Query innerQuery = fp.parseNestedQuery();
				
				final SolrQueryRequest req = fp.getReq();
				@SuppressWarnings("rawtypes")
				final CitationLRUCache cache = (CitationLRUCache) req.getSearcher().getCache("citations-cache");
				
				
				CacheWrapper citationsWrapper = new SecondOrderCollectorCacheWrapper() {
					@Override
					public AtomicReader getAtomicReader() {
						return req.getSearcher().getAtomicReader();
					}
					@Override
				  public int[] getLuceneDocIds(int sourceDocid) {
					  return cache.getCitations(sourceDocid);
				  }
					
					@SuppressWarnings("unchecked")
          @Override
					public int getLuceneDocId(int sourceDocid, Object sourceValue) {
					  return (Integer) cache.get(sourceValue);
				  }
					@Override
          public int internalHashCode() {
	          return cache.hashCode();
          }
					@Override
          public String internalToString() {
	          return cache.name();
          }
				};
				
				SecondOrderQuery outerQuery = new SecondOrderQuery( // citations
						new SecondOrderQuery( // topn
								new SecondOrderQuery(innerQuery, // classic_relevance
										new SecondOrderCollectorAdsClassicScoringFormula(citationsWrapper, "cite_read_boost")), 
										new SecondOrderCollectorTopN(200)),
										new SecondOrderCollectorCitedBy(citationsWrapper));
				outerQuery.getcollector().setFinalValueType(FinalValueType.ABS_COUNT);
				return outerQuery;
			};
		});
		
		
		/* @api.doc
		 * 
		 * def instructive(query):
		 * 		"""
		 *    The synonym of @see reviews
		 *    """
		 *    return reviews(query)
		 */
		parsers.put("instructive", parsers.get("reviews"));

		// original impl of reviews() = find papers that cite the most cited papers
		parsers.put("reviews2", new AqpSubqueryParserFull() { // this function values can be analyzed
			public Query parse(FunctionQParser fp) throws ParseException {          
				Query innerQuery = fp.parseNestedQuery();
				
				final SolrQueryRequest req = fp.getReq();
				@SuppressWarnings("rawtypes")
				final CitationLRUCache cache = (CitationLRUCache) req.getSearcher().getCache("citations-cache");
				
				
				CacheWrapper citationsWrapper = new SecondOrderCollectorCacheWrapper() {
					@Override
					public AtomicReader getAtomicReader() {
						return req.getSearcher().getAtomicReader();
					}
					@Override
				  public int[] getLuceneDocIds(int sourceDocid) {
					  return cache.getCitations(sourceDocid);
				  }
					
					@SuppressWarnings("unchecked")
          @Override
					public int getLuceneDocId(int sourceDocid, Object sourceValue) {
					  return (Integer) cache.get(sourceValue);
				  }
					@Override
          public int internalHashCode() {
	          return cache.hashCode();
          }
					@Override
          public String internalToString() {
	          return cache.name();
          }
				};
				
				String boostField = "cite_read_boost";
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorCitingTheMostCited(citationsWrapper, boostField));
			}
		});
		parsers.put("citis", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				
				final SolrQueryRequest req = fp.getReq();
				@SuppressWarnings("rawtypes")
        final CitationLRUCache cache = (CitationLRUCache) req.getSearcher().getCache("citations-cache");
				
				
				CacheWrapper citationsWrapper = new SecondOrderCollectorCacheWrapper() {
					@Override
					public AtomicReader getAtomicReader() {
						return req.getSearcher().getAtomicReader();
					}
					@Override
				  public int[] getLuceneDocIds(int sourceDocid) {
					  return cache.getCitations(sourceDocid);
				  }
					
					@SuppressWarnings("unchecked")
          @Override
					public int getLuceneDocId(int sourceDocid, Object sourceValue) {
					  return (Integer) cache.get(sourceValue);
				  }
					@Override
          public int internalHashCode() {
	          return cache.hashCode();
          }
					@Override
          public String internalToString() {
	          return cache.name();
          }
				};
				
				return new SecondOrderQuery(innerQuery, null, 
						new SecondOrderCollectorCites(citationsWrapper, new String[] {citationSearchRefField}), false);

			}
		});
		parsers.put("aqp", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {          
				QParser q = fp.subQuery(fp.getString(), "aqp");
				return q.getQuery();
			}
		});

		parsers.put("adismax", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {          
				QParser q = fp.subQuery(fp.getString(), "adismax");
				return simplify(q.getQuery());
			}
		});

		parsers.put("edismax_nonanalyzed", new AqpSubqueryParserFull() { // used for nodes that were already analyzed
			public Query parse(FunctionQParser fp) throws ParseException {
				final String original = fp.getString();
				QParser ep = fp.subQuery("xxx", "adismax");
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
		});
		parsers.put("edismax_combined_aqp", new AqpSubqueryParserFull() { // will decide whether new aqp() parse is needed
			public Query parse(FunctionQParser fp) throws ParseException {
				final String original = fp.getString();
				QParser eqp = fp.subQuery(original, "adismax");
				Query q = eqp.getQuery();
				return simplify(q);
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
				//if (f.equals("author")) {
				//  return "author";
				//}
				return null;
				//return f; // always re-analyze
			}
			private Query reAnalyze(String field, String value, float boost) throws ParseException {
				QParser fParser = getParser();
				System.out.println(field+ ":"+fParser.getString() + "|value=" + value);
				QParser aqp = fParser.subQuery(field+ ":"+fParser.getString(), "aqp");
				Query q = aqp.getQuery();
				q.setBoost(boost);
				return q;
			}
		});
		parsers.put("edismax_always_aqp", new AqpSubqueryParserFull() { // will use edismax to create top query, but the rest is done by aqp
			public Query parse(FunctionQParser fp) throws ParseException {
				final String original = fp.getString();
				QParser eqp = fp.subQuery("xxx", "adismax");
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
		});

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
	
	/**
	 * comment ZZZZZ
	 */
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

	/*
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
	*/

}
