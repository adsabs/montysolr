package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.AqpSubqueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSubqueryParserFull;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SecondOrderCollectorCitedBy;
import org.apache.lucene.search.SecondOrderCollectorCites;
import org.apache.lucene.search.SecondOrderCollectorCitesRAM;
import org.apache.lucene.search.SecondOrderCollectorCitingTheMostCited;
import org.apache.lucene.search.SecondOrderCollectorOperatorExpertsCiting;
import org.apache.lucene.search.SecondOrderQuery;
import org.apache.lucene.search.TermQuery;
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
import org.apache.solr.search.SpatialBoxQParserPlugin;
import org.apache.solr.search.SpatialFilterQParserPlugin;


/*
 * I know this is confusing. This is called in the building phase,
 * by that time all the parsing was already done.
 */

public class AqpAdsabsSubQueryProvider implements
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
		
		
	  // citations(P) - set of papers that have P in their reference list
		parsers.put("citations", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				// TODO: make configurable 
				String refField = "reference";
				String idField = "bibcode";
				return new SecondOrderQuery(innerQuery, null, new SecondOrderCollectorCitedBy(idField, refField), false);
	      }
	    }.configure(true)); // true=canBeAnalyzed
		parsers.put("incoming_links", parsers.get("citations"));
		parsers.put("citedby", parsers.get("citations")); // XXX - to remove after AAS!!!
		
	  
		// references(P) - set of papers that are in the reference list of P
		parsers.put("references", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				// TODO: make configurable
				String refField = "reference";
				String idField = "bibcode";
				return new SecondOrderQuery(innerQuery, null, new SecondOrderCollectorCitesRAM(idField, refField), false);
			}
		  }.configure(true)); // true=canBeAnalyzed
		parsers.put("outgoing_links", parsers.get("references"));
		parsers.put("cites", parsers.get("references")); // XXX - to remove after AAS!!!
		parsers.put("refersto", parsers.get("references")); // XXX - to remove after AAS!!!

		
		// useful() = what experts are citing
    parsers.put("useful", new AqpSubqueryParserFull() { // this function values can be analyzed
      public Query parse(FunctionQParser fp) throws ParseException {          
        Query innerQuery = fp.parseNestedQuery();
        SolrQueryRequest req = fp.getReq();
        // TODO: make configurable
        String refField = "reference";
        String idField = "bibcode";
        String boostField = "cite_read_boost";
        return new SecondOrderQuery(innerQuery, null, new SecondOrderCollectorOperatorExpertsCiting(idField, refField, boostField));
      }
      }.configure(true)); // true=canBeAnalyzed
    // reviews() = find papers that cite the most cited papers
    parsers.put("reviews", new AqpSubqueryParserFull() { // this function values can be analyzed
      public Query parse(FunctionQParser fp) throws ParseException {          
        Query innerQuery = fp.parseNestedQuery();
        SolrQueryRequest req = fp.getReq();
        // TODO: make configurable
        String refField = "reference";
        String idField = "bibcode";
        String boostField = "cite_read_boost";
        return new SecondOrderQuery(innerQuery, null, new SecondOrderCollectorCitingTheMostCited(idField, refField, boostField));
      }
      }.configure(true)); // true=canBeAnalyzed
		parsers.put("citis", new AqpSubqueryParserFull() {
			public Query parse(FunctionQParser fp) throws ParseException {    		  
				Query innerQuery = fp.parseNestedQuery();
				SolrQueryRequest req = fp.getReq();
				
				// TODO: make configurable
				String refField = "reference";
				String idField = "bibcode";
				
				return new SecondOrderQuery(innerQuery, null, new SecondOrderCollectorCites(idField, refField), false);
				
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
      }
      private Query reAnalyze(String field, String value, float boost) throws ParseException {
        QParser fParser = getParser();
        QParser aqp = fParser.subQuery(field+ ":"+fParser.getString(), "aqp");
        Query q = aqp.getQuery();
        q.setBoost(boost);
        return q;
      }
    }.configure(false)); // not analyzed
	};

	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
		throws QueryNodeException {
		
		
		AqpSubqueryParser provider = parsers.get(funcName);
		if (provider == null)
			return null;
			
		//AqpFunctionQueryTreeBuilder.flattenChildren(node);
		
		AqpRequestParams reqAttr = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
		
		SolrQueryRequest req = reqAttr.getRequest();
		if (req == null)
			return null;
		
		// need better way to get the input string
		//if (node instanceof AqpANTLRNode)
		
		String subQuery = "";
		
		if (node.isLeaf()) {
		  subQuery = (String) node.getTag("subQuery");
		  assert subQuery != null;
		}
		else {
  		String qStr = reqAttr.getQueryString();
  		
  		Integer[] start_span = new Integer[]{0,0};
  		Integer[] end_span = new Integer[]{qStr.length(),qStr.length()};
  		
  		
  		swimDeep(node.getChildren().get(0), start_span);
  		swimDeep(node.getChildren().get(node.getChildren().size()-1), end_span);
  		
  		subQuery = qStr.substring(start_span[1]+1, end_span[1]-1);
		}
		
		AqpFunctionQParser parser = new AqpFunctionQParser(subQuery, reqAttr.getLocalParams(), 
				reqAttr.getParams(), req);
		
		if (!node.isLeaf()) {
  		if (provider instanceof AqpSubqueryParserFull) {
  			AqpFunctionQueryTreeBuilder.removeFuncName(node);
  		}
  		else {
  			AqpFunctionQueryTreeBuilder.simplifyValueNode(node);
  		}
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
