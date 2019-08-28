package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queryparser.flexible.aqp.NestedParseException;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParser;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParserFull;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.search.BitSetQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.LuceneCacheWrapper;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.ConstantScoreQuery;
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
import org.apache.lucene.search.SimpleCollector;
import org.apache.lucene.search.SolrCacheWrapper;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.spans.SpanPositionRangeQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.batch.BatchHandlerRequestData;
import org.apache.solr.handler.batch.BatchProvider;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.BoostQParserPlugin;
import org.apache.solr.search.CitationCache;
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
import org.apache.solr.search.SortSpecParsing;
import org.apache.solr.search.SpatialBoxQParserPlugin;
import org.apache.solr.search.SpatialFilterQParserPlugin;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.servlet.SolrRequestParsers;
import org.apache.solr.uninverting.UninvertingReader;


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
	
	private static LuceneCacheWrapper<NumericDocValues> getLuceneCache(FunctionQParser fp, String fieldname) throws SyntaxError {
	  LuceneCacheWrapper<NumericDocValues> cacheWrapper;
    SchemaField field = fp.getReq().getSchema().getField(fieldname);
    try {
      cacheWrapper = LuceneCacheWrapper.getFloatCache(
          "cite_read_boost", UninvertingReader.Type.SORTED_SET_FLOAT, 
          fp.getReq().getSearcher().getSlowAtomicReader());
    } catch (IOException e) {
      throw new SyntaxError("Naughty, naughty server error", e);
    }
    return cacheWrapper;
	}
	
	static {
		
		/* @api.doc
		 * 
		 * def lucene(query):
		 * 		"""
		 *    Default Lucene query parser
		 * 		"""
		 */
		parsers.put(LuceneQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), LuceneQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		/**
		 * comment XXX
		 */
		parsers.put(OldLuceneQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), OldLuceneQParserPlugin.NAME);
				return q.getQuery();

			}
		});
		parsers.put(FunctionQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), FunctionQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		parsers.put(PrefixQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), PrefixQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		parsers.put(BoostQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), BoostQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		parsers.put(DisMaxQParserPlugin.NAME, new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), DisMaxQParserPlugin.NAME);
				return simplify(q.getQuery());
			}
		});
		parsers.put(ExtendedDismaxQParserPlugin.NAME, new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), ExtendedDismaxQParserPlugin.NAME);
				return simplify(q.getQuery());
			}
		});
		parsers.put(FieldQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), FieldQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		parsers.put(RawQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {
				String qstr = fp.getString();
				if (!qstr.substring(0,2).equals("{!")) {
					throw new SyntaxError(
							"Raw query parser requires you to specify local params, eg: raw({!f=field}"+fp.getString()+")");
				}
				QParser q = fp.subQuery(qstr, RawQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		parsers.put(NestedQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), NestedQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		parsers.put(FunctionRangeQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), FunctionRangeQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		parsers.put(SpatialFilterQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser q = fp.subQuery(fp.getString(), SpatialFilterQParserPlugin.NAME);
				return q.getQuery();
			}
		});
		parsers.put(SpatialBoxQParserPlugin.NAME, new AqpSubqueryParser() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
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
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				QParser aqp = fp.subQuery(fp.getString(), "aqp");
				Query innerQuery = aqp.parse();

				SolrQueryRequest req = fp.getReq();
				SolrIndexSearcher searcher = req.getSearcher();

				// find the 200 most interesting papers and collect their readers
				SecondOrderQuery discoverMostReadQ = new SecondOrderQuery(innerQuery, 
						new SecondOrderCollectorTopN(200));
				discoverMostReadQ.getcollector().setFinalValueType(FinalValueType.ABS_COUNT);

				final StringBuilder readers = new StringBuilder();
				final HashSet<String> fieldsToLoad = new HashSet<String>();
				final String fieldName = "reader";
				fieldsToLoad.add(fieldName);

				try {
					searcher.search(discoverMostReadQ, new SimpleCollector() {
						private Document d;
						private LeafReader reader;
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
						public void doSetNextReader(LeafReaderContext context)
						throws IOException {
							this.reader = context.reader();
						}
            @Override
            public boolean needsScores() {
              return true;
            }
					});
				} catch (IOException e) {
					throw new SyntaxError(e.getMessage(), e);
				}

				MoreLikeThisQuery mlt = new MoreLikeThisQuery(readers.toString(), new String[] {fieldName}, 
						new WhitespaceAnalyzer(), fieldName);

				// configurable params
				mlt.setMinTermFrequency(0);
				mlt.setMinDocFreq(2);
				mlt.setMaxQueryTerms(200);
				mlt.setPercentTermsToMatch(0.0f);

				//try {
					//  Query q = mlt.rewrite(req.getSearcher().getIndexReader());
				//  System.out.println(q);
				//} catch (IOException e) {
				//}

				return new BoostQuery(mlt, 2.0f);
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
			public Query parse(FunctionQParser fp) throws SyntaxError {
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
				
				// a field can have a different positionIncrementGap
				int positionIncrementGap = 1;
				if (fp.getReq() != null) {
					IndexSchema schema = fp.getReq().getSchema();
					SchemaField field = schema.getFieldOrNull(query.toString().split(":")[0]);
					if (field != null) {
						FieldType fType = field.getType();
						//if (!fType.isMultiValued()) {
						//	throw new SyntaxError("The positional search doesn't make sense for: " + query);
						//}
						positionIncrementGap = fType.getIndexAnalyzer().getPositionIncrementGap(field.getName());
						if (positionIncrementGap == 0)
							positionIncrementGap = 1;
					}
				}
				
				boolean wrapConstant = false;
				float boostFactor = 1.0f;
				if (query instanceof BoostQuery) {
				  boostFactor = ((BoostQuery) query).getBoost();
				  query = ((BoostQuery) query).getQuery();
				}
				if (query instanceof ConstantScoreQuery) {
				  query = ((ConstantScoreQuery) query).getQuery();
				  wrapConstant = true;
				}
				

				SpanQuery spanQuery;
				try {
					spanQuery = converter.getSpanQuery(new SpanConverterContainer(query, 1, true));
				} catch (QueryNodeException e) {
					SyntaxError ex = new SyntaxError(e.getMessage(), e);
					ex.setStackTrace(e.getStackTrace());
					throw ex;
				}

				query = new SpanPositionRangeQuery(spanQuery, (start-1)*positionIncrementGap , end*positionIncrementGap); //lucene counts from zeroes
				if (wrapConstant)
				  query = new ConstantScoreQuery(query);
				if (boostFactor != 1.0f)
				  query = new BoostQuery(query, boostFactor);
				return query;
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
			public Query parse(FunctionQParser fp) throws SyntaxError {

				Query innerQuery = fp.parseNestedQuery();
				float ratio = 0.5f;
				if (fp.hasMoreArguments()) {
					ratio = fp.parseFloat();
				}
				
				if (ratio < 0 || ratio > 1.0f) {
					throw new SyntaxError("The ratio must be in the range 0.0-1.0");
				}
				
				@SuppressWarnings("unchecked")
				SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
						(CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));
				
				LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "cite_read_boost");
				
				return new SecondOrderQuery(innerQuery, 
						new SecondOrderCollectorAdsClassicScoringFormula(citationsWrapper, boostWrapper, ratio));
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
			public Query parse(FunctionQParser fp) throws SyntaxError {
				int topN = -1;
				try {
					topN = fp.parseInt();
				}
				catch (NumberFormatException e) {
					throw new SyntaxError("The function signature is topn(int, query, [sort order]). Error: " + e.getMessage());
				}

				if (topN < 1) {  //|| topN > 50000 - previously, i was limiting the fields
					throw new SyntaxError("Hmmm, the first argument of your operator must be a positive number.");
				}

				QParser eqp = fp.subQuery(fp.parseId(), "aqp");
				Query innerQuery = eqp.getQuery();

				if (innerQuery == null) {
					throw new SyntaxError("This query is empty: " + eqp.getString());
				}

				String sortOrRank = "score desc"; 
				if (fp.hasMoreArguments()) {
					sortOrRank = fp.parseId();
				}

				sortOrRank = sortOrRank.toLowerCase();

				if (sortOrRank.contains("\"") || sortOrRank.contains("\'")) {
					sortOrRank = sortOrRank.substring(1, sortOrRank.length()-1);
				}

				SortSpec sortSpec = SortSpecParsing.parseSortSpec(sortOrRank, fp.getReq());
				
				if (sortSpec.getSort() == null) {
					return new SecondOrderQuery(innerQuery, 
							new SecondOrderCollectorTopN(topN));
				}
				else {

					SolrIndexSearcher searcher = fp.getReq().getSearcher();

					TopFieldCollector collector;
					try {
						collector = TopFieldCollector.create(searcher.weightSort(sortSpec.getSort()), topN, false, true, true);
					} catch (IOException e) {
						throw new SyntaxError("I am sorry, you can't use " + sortOrRank + " for topn() sorting. Reason: " + e.getMessage());
					}

					return new SecondOrderQuery(innerQuery, 
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
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				Query innerQuery = fp.parseNestedQuery();
				
				@SuppressWarnings("unchecked")
				SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
						(CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));
								
				return new SecondOrderQuery(innerQuery, 
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
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				Query innerQuery = fp.parseNestedQuery();
				
				@SuppressWarnings("unchecked")
				SolrCacheWrapper<CitationCache<Object, Integer>> referencesWrapper = new SolrCacheWrapper.ReferencesCache(
						(CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));
				
				
					return new SecondOrderQuery(innerQuery, 
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
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				try {
					// XXX: not sure if i can use several fields: citationSearchIdField
					return JoinUtil.createJoinQuery("bibcode", false, "reference", innerQuery, 
							req.getSearcher(), ScoreMode.Avg);
				} catch (IOException e) {
					throw new SyntaxError(e.getMessage());
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
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				try {
					return JoinUtil.createJoinQuery("bibcode", false, "citation", innerQuery, 
							req.getSearcher(), ScoreMode.Avg);
				} catch (IOException e) {
					throw new SyntaxError(e.getMessage());
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
		 *    In other words, this will first find papers using the inner query, 
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
			public Query parse(FunctionQParser fp) throws SyntaxError {          
				Query innerQuery = fp.parseNestedQuery();
				
				@SuppressWarnings("unchecked")
        SolrCacheWrapper<CitationCache<Object, Integer>> referencesWrapper = new SolrCacheWrapper.ReferencesCache(
						(CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));
				
				LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "cite_read_boost");
				
				SecondOrderQuery outerQuery = 
				 new SecondOrderQuery( // references
						new SecondOrderQuery( // topn
								//new SecondOrderQuery(innerQuery, // classic_relevance
								//		new SecondOrderCollectorAdsClassicScoringFormula(referencesWrapper, boostWrapper)),
								    innerQuery,
										new SecondOrderCollectorTopN(200)),
										new SecondOrderCollectorCitesRAM(referencesWrapper));
				
				outerQuery.getcollector().setFinalValueType(FinalValueType.ABS_COUNT_NORM);
				return outerQuery;
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
			public Query parse(FunctionQParser fp) throws SyntaxError {          
				Query innerQuery = fp.parseNestedQuery();
				
				@SuppressWarnings("unchecked")
				SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
						(CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));
				
				//TODO: make configurable the name of the field				
				LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "cite_read_boost");

				return new SecondOrderQuery(innerQuery, 
						new SecondOrderCollectorOperatorExpertsCiting(citationsWrapper, boostWrapper));
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
			public Query parse(FunctionQParser fp) throws SyntaxError {          
				Query innerQuery = fp.parseNestedQuery();
				
				@SuppressWarnings("unchecked")
				SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
						(CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));
				
				LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "cite_read_boost");
				
				SecondOrderQuery outerQuery = 
				 new SecondOrderQuery( // citations
						new SecondOrderQuery( // topn
						    innerQuery,
								//new SecondOrderQuery(innerQuery, // classic_relevance
								//		new SecondOrderCollectorAdsClassicScoringFormula(citationsWrapper, boostWrapper)), 
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
			public Query parse(FunctionQParser fp) throws SyntaxError {          
				Query innerQuery = fp.parseNestedQuery();
				
				@SuppressWarnings("unchecked")
				SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
						(CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));
				
				LuceneCacheWrapper<NumericDocValues> boostWrapper = getLuceneCache(fp, "cite_read_boost");
				
				return new SecondOrderQuery(innerQuery, 
						new SecondOrderCollectorCitingTheMostCited(citationsWrapper, boostWrapper));
			}
		});
		parsers.put("citis", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				Query innerQuery = fp.parseNestedQuery();
				
				@SuppressWarnings("unchecked")
				SolrCacheWrapper<CitationCache<Object, Integer>> citationsWrapper = new SolrCacheWrapper.CitationsCache(
						(CitationCache<Object, Integer>) fp.getReq().getSearcher().getCache("citations-cache"));
				
				return new SecondOrderQuery(innerQuery, 
						new SecondOrderCollectorCites(citationsWrapper, new String[] {citationSearchRefField}), false);

			}
		});
		parsers.put("aqp", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws SyntaxError {          
				QParser q = fp.subQuery(fp.getString(), "aqp");
				return q.getQuery();
			}
		});

		parsers.put("adismax", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws SyntaxError {          
				QParser q = fp.subQuery(fp.getString(), "adismax");
				return simplify(q.getQuery());
			}
		});

		parsers.put("edismax_nonanalyzed", new AqpSubqueryParserFull() { // used for nodes that were already analyzed
			public Query parse(FunctionQParser fp) throws SyntaxError {
				final String original = fp.getString();
				QParser ep = fp.subQuery("xxx", "adismax");
				Query q = ep.getQuery();
				QParser fakeParser = new QParser(original, null, null, null) {
					@Override
					public Query parse() throws SyntaxError {
						String[] parts = getString().split(":");
						return new TermQuery(new Term(parts[0], original));
					}
				};
				return simplify(reParse(q, fakeParser, TermQuery.class));
			}
		});
		parsers.put("edismax_combined_aqp", new AqpSubqueryParserFull() { // will decide whether new aqp() parse is needed
			public Query parse(FunctionQParser fp) throws SyntaxError {
				final String original = fp.getString();
				QParser eqp = fp.subQuery(original, "adismax");
				Query q = eqp.getQuery();
				return simplify(q);
			}
			protected Query swimDeep(DisjunctionMaxQuery query) throws SyntaxError {
				List<Query> parts = query.getDisjuncts();
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
						parts.set(i, reAnalyze(field, getParser().getString(), 
						    oldQ.getClass().isInstance(BoostQuery.class) ? ((BoostQuery)oldQ).getBoost() : null));
					}
					else {
						parts.set(i, swimDeep(oldQ));
					}
				}
				return query;
			}

			private String toBeAnalyzedAgain(TermQuery q) {
				//String f = q.getTerm().field();
				//if (f.equals("author")) {
				//  return "author";
				//}
				return null;
				//return f; // always re-analyze
			}
			private Query reAnalyze(String field, String value, Float boost) throws SyntaxError {
				QParser fParser = getParser();
				System.out.println(field+ ":"+fParser.getString() + "|value=" + value);
				QParser aqp = fParser.subQuery(field+ ":"+fParser.getString(), "aqp");
				Query q = aqp.getQuery();
				if (boost != null && boost != 1.0f) {
				  q = new BoostQuery(q, boost);
				}
				return q;
			}
		});
		parsers.put("edismax_always_aqp", new AqpSubqueryParserFull() { // will use edismax to create top query, but the rest is done by aqp
			public Query parse(FunctionQParser fp) throws SyntaxError {
				final String original = fp.getString();
				QParser eqp = fp.subQuery("xxx", "adismax");
				fp.setString(original);
				Query q = eqp.getQuery();
				return simplify(reParse(q, fp, (Class<?>)null));
			}
			protected Query swimDeep(DisjunctionMaxQuery query) throws SyntaxError {
				List<Query> parts = query.getDisjuncts();
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
						parts.set(i, reAnalyze(field, getParser().getString(), 
						    oldQ.getClass().isInstance(BoostQuery.class) ? ((BoostQuery)oldQ).getBoost() : null));
					}
					else {
						parts.set(i, swimDeep(oldQ));
					}
				}
				return query;
			}

			private Query reAnalyze(String field, String value, Float boost) throws SyntaxError {
				QParser fParser = getParser();
				QParser aqp = fParser.subQuery(field+ ":"+fParser.getString(), "aqp");
				Query q = aqp.getQuery();
				if (boost != null && boost != 1.0f) {
          q = new BoostQuery(q, boost);
        }
				return q;
			}
		});

		parsers.put("tweak", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws SyntaxError {

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
							throw new SyntaxError("Wrong parameter: " + e.getMessage(), e);
						}
					}
				}
				return q;
			}
		});
		
	  // helper method; SOLR is not warming up caches when index is opened first time
		// so we have to do it ourselves
		parsers.put("warm_cache", new AqpSubqueryParserFull() {
			@SuppressWarnings("unchecked")
				public Query parse(FunctionQParser fp) throws SyntaxError {

				final SolrQueryRequest req = fp.getReq();
				@SuppressWarnings("rawtypes")
				final CitationCache cache = (CitationCache) req.getSearcher().getCache("citations-cache");
				if (!cache.isWarmingOrWarmed()) {
					cache.warm(req.getSearcher(), cache);
				}
				return new MatchNoDocsQuery();
			}
		});
		
		/* @api.doc
		 * 
		 * def constant(query):
		 * 		"""
		 *    Applies constant score (that can be set by boost factor)
		 *    
		 *    Example: 
		 *    
		 *    	```constant(title:hubble^2)```
		 *    
		 *    
		 *    @since 63.1.0.24
		 *    """
		 *    return "constant(%s)" % (query,)
		 *    
		 */
		parsers.put("constant", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws SyntaxError {    		  
				Query innerQuery = fp.parseNestedQuery();
				
				return new ConstantScoreQuery(innerQuery);
			}
		});
		
	parsers.put("docs", new AqpSubqueryParserFull() {
      public Query parse(FunctionQParser fp) throws SyntaxError {

        SolrQueryRequest req = fp.getReq();
        String input = fp.getString();
        String filterName = input.substring(1, input.length()-1);
        
        
        // pick the content stream (actually a filter)
        List<ContentStream> streams = new ArrayList<ContentStream>(1);
        if (req.getContentStreams() != null) {
          ContentStream lcs = null;
          for (ContentStream cs: req.getContentStreams()) {
            if (filterName.equals(cs.getName())) {
              streams.add(cs);
            }
          }
        }
        
        
        // create a new local request with just this one content stream
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("defType", "bitset");
        
        SolrQueryRequestBase locReq = (SolrQueryRequestBase) new LocalSolrQueryRequest(req.getCore(), params);
        try {
          locReq.setContentStreams(streams);
          
          // stream can also be in local params...
          String filter = req.getParams().get(filterName);
          String qString;
          if (filter != null) {
            qString = filter.trim();
          }
          else {
            qString = "*:*";
          }
          params.set("q", qString);
          
          Query q;
          
          if (streams.size() == 0 && qString.equals("*:*"))
              throw new SolrException(ErrorCode.BAD_REQUEST,"Invalid query, missing stream for bigquery("+input+")");
          
          try {
            q = QParser.getParser(qString, "bitset", locReq).getQuery();
          } catch( SyntaxError e ){
            throw new SolrException(ErrorCode.BAD_REQUEST,"Invalid query bigquery("+input+")",e);
          }
          
          return q;
        }
        finally {
          locReq.close();
        }
      }
    });
		
  /* @api.doc
   * 
   * def similar(queryOrText, fields, maxQueryTerms, docToSearch, minTermFreq, minDocFreq, percentToMatch):
   *    """
   *    Finds similar documents:
   *    
   *      @param queryOrText: string, this can be a query or input
   *      @param fields: list of fields separated by spaces, or special token 'input'
   *        which means "use the query as is, as input"
   *      @param maxQueryTerms: modifies similarity search, only this many terms will
   *        be considered during the search (those terms are NOT the first X collected,
   *        but they will be the first X terms weighted by TFIDF)
   *      @param docToSearch: how many documents to collect in the first phase, is ignored
   *        when fields='input'
   *      @param minTermFreq: term is selected only if its frequency is this or higher
   *      @param minDocFreq: selected term must be present in at least that many documents
   *      @param percentToMatch: ratio of terms that have to be present in the selected
   *        documents, default is 0.0f. For example, if 100 terms was used to discover 
   *        similar docs, and if the ratio was 0.3f - then 30 terms must be present in the
   *        docs that are returned.
   *      
   *    
   *    Example: 
   *    
   *      ```similar(title:hubble^2, abstract, 100)```
   *      
   *      Will find all documents that have 'hubble' in title, will collect first and use their terms
   *      to discover similar documents in abstract. It will only use 100 search tokens.
   *    
   *    
   *    @since 63.1.0.24
   *    @since 63.1.0.57  - exposed parameters to modify similar() behaviour
   *    """
   *    return "similar(%s)" % (query,)
   *    
   */
	parsers.put("similar", new AqpSubqueryParserFull() {
      public Query parse(FunctionQParser fp) throws SyntaxError {
        String input = fp.parseId();
        
        String toLoad = "abstract";
        if (fp.hasMoreArguments())
          toLoad = fp.parseId();
        
        int maxQueryTerms = 500;
        if (fp.hasMoreArguments())
          maxQueryTerms = Math.min(fp.parseInt(), maxQueryTerms);
        
        int docToSearch = 200;
        if (fp.hasMoreArguments())
          docToSearch = Math.min(fp.parseInt(), docToSearch);
        
        int minTermFrequency = 2;
        if (fp.hasMoreArguments())
          minTermFrequency = Math.max(fp.parseInt(), 1);
        
        int minDocFrequency = 2;
        if (fp.hasMoreArguments())
          minDocFrequency = Math.max(fp.parseInt(), 1);
        
        float percentToMatch = 0.0f;
        if (fp.hasMoreArguments())
          percentToMatch = fp.parseFloat();
        
        final StringBuilder text = new StringBuilder();
        SolrQueryRequest req = fp.getReq();
        FixedBitSet toIgnore = null;
        String[] fieldsToLoad = toLoad.split(" ");
        
        if (toLoad.indexOf("input") > -1) {
          text.append(input);
          if (toLoad.length() > 5) {
            fieldsToLoad = toLoad.substring(toLoad.indexOf("input")+6).split(" ");
          }
          else {
            fieldsToLoad = new String[] {"abstract"};
          }
        }
        else {
          
          fieldsToLoad = toLoad.split(" ");
          QParser aqp = fp.subQuery(input, "aqp");
          Query innerQuery = aqp.parse();
          
          HashSet<String> docFields = new HashSet<String>();
          for (String f: fieldsToLoad) {
            docFields.add(f);
          }
          
          SolrIndexSearcher searcher = req.getSearcher();
          

          toIgnore = new FixedBitSet(searcher.maxDoc());
          
          TopDocs topDocs;
          
          try {
            topDocs = searcher.search(innerQuery, docToSearch);
            if (topDocs.totalHits == 0)
              return new MatchNoDocsQuery();
            
            for (ScoreDoc d: topDocs.scoreDocs) {
              toIgnore.set(d.doc);
              Document vals = searcher.doc(d.doc, docFields);
              for (String f: docFields) {
                for (String x: vals.getValues(f)) {
                  text.append(x);
                  text.append(" ");
                }
              }
            }
            
            // set some better defaults when it is a rare doc
            if (topDocs.totalHits < 2) {
              minTermFrequency = 0;
              minDocFrequency = 1;
            }
           
          } catch (IOException e) {
            throw new SyntaxError(e.getMessage(), e);
          }
          
        }
        
        
        
        Analyzer analyzer = req.getSchema().getIndexAnalyzer();
        MoreLikeThisQuery mlt = new MoreLikeThisQuery(text.toString(), fieldsToLoad, 
            analyzer, fieldsToLoad[0]);
        
        mlt.setMinTermFrequency(minTermFrequency);
        mlt.setMinDocFreq(minDocFrequency);
        
        mlt.setPercentTermsToMatch(percentToMatch);
        mlt.setMaxQueryTerms(maxQueryTerms);
        
        if (toIgnore != null) {
          BooleanQuery.Builder query = new BooleanQuery.Builder();
          query.add(mlt, BooleanClause.Occur.MUST);
          query.add(new BitSetQuery(toIgnore), BooleanClause.Occur.MUST_NOT);
          return query.build();
        }
        else {
          return mlt;
        }
        
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
