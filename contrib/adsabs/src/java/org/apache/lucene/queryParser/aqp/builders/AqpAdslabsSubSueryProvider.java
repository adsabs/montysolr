package org.apache.lucene.queryParser.aqp.builders;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.aqp.AqpSubqueryParser;
import org.apache.lucene.queryParser.aqp.AqpSubqueryParserFull;
import org.apache.lucene.queryParser.aqp.NestedParseException;
import org.apache.lucene.queryParser.aqp.config.AqpRequestParams;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.search.CitedByCollector;
import org.apache.lucene.search.CitesCollector;
import org.apache.lucene.search.CitesCollectorString;
import org.apache.lucene.search.CollectorQuery;
import org.apache.lucene.search.DictionaryRecIdCache;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SecondOrderCollectorCites;
import org.apache.lucene.search.SecondOrderQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.common.params.SolrParams;
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
import org.apache.solr.search.RawQParserPlugin;
import org.apache.solr.search.SolrIndexReader;
import org.apache.solr.search.SpatialBoxQParserPlugin;
import org.apache.solr.search.SpatialFilterQParserPlugin;
import org.apache.solr.search.ValueSourceParser;
import org.apache.solr.search.function.PositionSearchFunction;
import org.apache.solr.search.function.ValueSource;

public class AqpAdslabsSubSueryProvider implements
		AqpFunctionQueryBuilderProvider {
	
	public static Map<String, AqpSubqueryParser> parsers = new HashMap<String, AqpSubqueryParser>();
	
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
		parsers.put(ExtendedDismaxQParserPlugin.NAME, new AqpSubqueryParser() {
		      public Query parse(FunctionQParser fp) throws ParseException {    		  
	    		  QParser q = fp.subQuery(fp.getString(), ExtendedDismaxQParserPlugin.NAME);
	    		  return q.getQuery();
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
		    		  throw new ParseException("Raw query parser requires you to specify local params, eg: raw({!f=field}"+fp.getString()+")");
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
		parsers.put("refersto", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				
				// TODO: make configurable 
				String refField = "reference";
				String idField = "bibcode";
				
				int[][] invCache;
				try {
					invCache = DictionaryRecIdCache.INSTANCE.
					getUnInvertedDocidsStrField(req.getSearcher().getIndexReader(), idField, refField);
				} catch (IOException e) {
					throw new ParseException(e.getLocalizedMessage());
				}
				//return new CollectorQuery(innerQuery, new CitedByCollector(invCache, refField));
				try {
					return new CollectorQuery(innerQuery, (IndexReader) req.getSearcher().getReader(),
							CollectorQuery.createCollector(CitedByCollector.class, invCache, refField));
				} catch (Exception e) {
					req.getCore().log.error(e.toString());
					throw new ParseException("Ouuups, our developers are lame mulas - server error!");
				}
		      }
		    });
		parsers.put("cites", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				
				
				// TODO: make configurable
				String refField = "reference";
				String idField = "bibcode";
				
				Map<String, Integer> cache;
				
				try {
					cache = DictionaryRecIdCache.INSTANCE.
						getTranslationCacheString(req.getSearcher().getIndexReader(), idField);
					
				} catch (IOException e) {
					throw new ParseException(e.getLocalizedMessage());
				}
				//return new CollectorQuery(innerQuery, new CitesCollectorString(cache, refField));
				try {
					//return new CollectorQuery(innerQuery, (IndexReader) req.getSearcher().getReader(),
					//		CollectorQuery.createCollector(CitesCollectorString.class, cache, refField));
					return new SecondOrderQuery(innerQuery, null, new SecondOrderCollectorCites(cache, refField), false);
				} catch (Exception e) {
					req.getCore().log.error(e.toString());
					throw new ParseException("Ouuups, our developers are lame mulas - server error!");
				}
		      }
		    });
	};

	public QueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
		throws QueryNodeException {
		
		
		AqpSubqueryParser provider = parsers.get(funcName);
		if (provider == null)
			return null;
			
		//AqpFunctionQueryTreeBuilder.flattenChildren(node);
		
		AqpRequestParams reqAttr = config.getAttribute(AqpRequestParams.class);
		
		SolrQueryRequest req = reqAttr.getRequest();
		if (req == null)
			return null;
		
		String qStr = reqAttr.getQueryString();
		
		Integer[] start_span = new Integer[]{0,0};
		Integer[] end_span = new Integer[]{qStr.length(),0};
		
		
		swimDeep(node.getChildren().get(0), start_span);
		swimDeep(node.getChildren().get(node.getChildren().size()-1), end_span);
		
		String subQuery = qStr.substring(start_span[1]+1, end_span[1]+1);
		
		
		AqpFunctionQParser parser = new AqpFunctionQParser(subQuery, reqAttr.getLocalParams(), 
				reqAttr.getParams(), req);
		
		if (provider instanceof AqpSubqueryParserFull) {
			AqpFunctionQueryTreeBuilder.removeFuncName(node);
		}
		else {
			AqpFunctionQueryTreeBuilder.simplifyValueNode(node);
		}
		
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
