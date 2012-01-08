package org.apache.solr.search;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.Weight;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.util.OpenBitSet;
import org.apache.lucene.util.ToStringUtils;
import org.apache.solr.search.CitationQuery;

/**
 * Parse Invenio's variant on the refersto citation <br>
 * Other parameters:
 * <ul>
 * <li>q.op - the default operator "OR" or "AND"</li>
 * <li>df - the default field name</li>
 * </ul>
 * <br>
 * Example:
 * <code>{!relation q.op=AND df=author sort='price asc'}coauthor:ellis +bar -baz</code>
 */
public class CitationRefersToQParserPlugin extends QParserPlugin {
	public static String NAME = "refersto";

	@Override
	public void init(NamedList args) {
	}

	@Override
	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		return new InvenioRefersToQParser(qstr, localParams, params, req);
	}

}

class InvenioRefersToQParser extends QParser {
	String sortStr;
	SolrQueryParser lparser;

	public InvenioRefersToQParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);
	}

	public Query parse() throws ParseException {
		String qstr = getString();

		String defaultField = getParam(CommonParams.DF);
		if (defaultField == null) {
			defaultField = getReq().getSchema().getDefaultSearchFieldName();
		}
		lparser = new SolrQueryParser(this, defaultField);

		// these could either be checked & set here, or in the SolrQueryParser
		// constructor
		String opParam = getParam(QueryParsing.OP);
		if (opParam != null) {
			lparser.setDefaultOperator("AND".equals(opParam) ? QueryParser.Operator.AND
					: QueryParser.Operator.OR);
		} else {
			// try to get default operator from schema
			QueryParser.Operator operator = getReq().getSchema()
					.getSolrQueryParser(null).getDefaultOperator();
			lparser.setDefaultOperator(null == operator ? QueryParser.Operator.OR
					: operator);
		}

		Query mainq = lparser.parse(qstr);
		//Filter qfilter = new CitationRefersToFilter();

		//return new CitationRefersToQuery(mainq, qfilter);
		return new CitationQuery(mainq, req, localParams);
	}

	public String[] getDefaultHighlightFields() {
		return new String[] { lparser.getField() };
	}

}

class CitationRefersToQuery
extends Query {

	  Query query;
	  Filter filter;

	  /**
	   * Constructs a new query which applies a filter to the results of the original query.
	   * Filter.getDocIdSet() will be called every time this query is used in a search.
	   * @param query  Query to be filtered, cannot be <code>null</code>.
	   * @param filter Filter to apply to query results, cannot be <code>null</code>.
	   */
	  public CitationRefersToQuery (Query query, Filter filter) {
	    this.query = query;
	    this.filter = filter;
	  }

	  /**
	   * Returns a Weight that applies the filter to the enclosed query's Weight.
	   * This is accomplished by overriding the Scorer returned by the Weight.
	   */
	  public Weight createWeight(final Searcher searcher) throws IOException {
	    final Weight weight = query.createWeight (searcher);
	    final Similarity similarity = query.getSimilarity(searcher);
	    return new Weight() {
	      private float value;

	      // pass these methods through to enclosed query's weight
	      public float getValue() { return value; }
	      public float sumOfSquaredWeights() throws IOException {
	        return weight.sumOfSquaredWeights() * getBoost() * getBoost();
	      }
	      public void normalize (float v) {
	        weight.normalize(v);
	        value = weight.getValue() * getBoost();
	      }
	      public Explanation explain (IndexReader ir, int i) throws IOException {
	        Explanation inner = weight.explain (ir, i);
	        if (getBoost()!=1) {
	          Explanation preBoost = inner;
	          inner = new Explanation(inner.getValue()*getBoost(),"product of:");
	          inner.addDetail(new Explanation(getBoost(),"boost"));
	          inner.addDetail(preBoost);
	        }
	        Filter f = CitationRefersToQuery.this.filter;
	        DocIdSet docIdSet = f.getDocIdSet(ir);
	        DocIdSetIterator docIdSetIterator = docIdSet == null ? DocIdSet.EMPTY_DOCIDSET.iterator() : docIdSet.iterator();
	        if (docIdSetIterator == null) {
	          docIdSetIterator = DocIdSet.EMPTY_DOCIDSET.iterator();
	        }
	        if (docIdSetIterator.advance(i) == i) {
	          return inner;
	        } else {
	          Explanation result = new Explanation
	            (0.0f, "failure to match filter: " + f.toString());
	          result.addDetail(inner);
	          return result;
	        }
	      }

	      // return this query
	      public Query getQuery() { return CitationRefersToQuery.this; }

	      // return a filtering scorer
	      public Scorer scorer(IndexReader indexReader, boolean scoreDocsInOrder, boolean topScorer)
	          throws IOException {
	        final Scorer scorer = weight.scorer(indexReader, true, false);
	        if (scorer == null) {
	          return null;
	        }
	        DocIdSet docIdSet = filter.getDocIdSet(indexReader);
	        if (docIdSet == null) {
	          return null;
	        }
	        final DocIdSetIterator docIdSetIterator = docIdSet.iterator();
	        if (docIdSetIterator == null) {
	          return null;
	        }

	        return new Scorer(similarity) {

	          private int doc = -1;

	          private int advanceToCommon(int scorerDoc, int disiDoc) throws IOException {
	            while (scorerDoc != disiDoc) {
	              if (scorerDoc < disiDoc) {
	                scorerDoc = scorer.advance(disiDoc);
	              } else {
	                disiDoc = docIdSetIterator.advance(scorerDoc);
	              }
	            }
	            return scorerDoc;
	          }

	          public void score(Collector collector) throws IOException {
	        	    collector.setScorer(this);
	        	    int doc;
	        	    while ((doc = nextDoc()) != NO_MORE_DOCS) {
	        	      collector.collect(doc);
	        	    }
	        	  }

	          /** @deprecated use {@link #nextDoc()} instead. */
	          @Deprecated
	          public boolean next() throws IOException {
	            return nextDoc() != NO_MORE_DOCS;
	          }

	          public int nextDoc() throws IOException {
	            int scorerDoc, disiDoc;
	            return doc = (disiDoc = docIdSetIterator.nextDoc()) != NO_MORE_DOCS
	                && (scorerDoc = scorer.nextDoc()) != NO_MORE_DOCS
	                && advanceToCommon(scorerDoc, disiDoc) != NO_MORE_DOCS ? scorer.docID() : NO_MORE_DOCS;
	          }

	          public int docID() { return doc; }

	          /** @deprecated use {@link #advance(int)} instead. */
	          @Deprecated
	          public boolean skipTo(int i) throws IOException {
	            return advance(i) != NO_MORE_DOCS;
	          }

	          public int advance(int target) throws IOException {
	            int disiDoc, scorerDoc;
	            return doc = (disiDoc = docIdSetIterator.advance(target)) != NO_MORE_DOCS
	                && (scorerDoc = scorer.advance(disiDoc)) != NO_MORE_DOCS
	                && advanceToCommon(scorerDoc, disiDoc) != NO_MORE_DOCS ? scorer.docID() : NO_MORE_DOCS;
	          }

	          public float score() throws IOException { return getBoost() * scorer.score(); }

	          // add an explanation about whether the document was filtered
	          public Explanation explain (int i) throws IOException {
	        	  Explanation exp = new Explanation();
	        	  // FIX: use the IndexReader.explain()
	        		  //Weight.explain(indexReader, i);
	            //Explanation exp = scorer.explain(i);

	            if (docIdSetIterator.advance(i) == i) {
	              exp.setDescription ("allowed by filter: "+exp.getDescription());
	              exp.setValue(getBoost() * exp.getValue());
	            } else {
	              exp.setDescription ("removed by filter: "+exp.getDescription());
	              exp.setValue(0.0f);
	            }
	            return exp;
	          }
	        };
	      }
	    };
	  }

	  /** Rewrites the wrapped query. */
	  public Query rewrite(IndexReader reader) throws IOException {
	    Query rewritten = query.rewrite(reader);
	    if (rewritten != query) {
	      CitationRefersToQuery clone = (CitationRefersToQuery)this.clone();
	      clone.query = rewritten;
	      return clone;
	    } else {
	      return this;
	    }
	  }

	  public Query getQuery() {
	    return query;
	  }

	  public Filter getFilter() {
	    return filter;
	  }

	  // inherit javadoc
	  public void extractTerms(Set<Term> terms) {
	      getQuery().extractTerms(terms);
	  }

	  /** Prints a user-readable version of this query. */
	  public String toString (String s) {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("filtered(");
	    buffer.append(query.toString(s));
	    buffer.append(")->");
	    buffer.append(filter);
	    buffer.append(ToStringUtils.boost(getBoost()));
	    return buffer.toString();
	  }

	  /** Returns true iff <code>o</code> is equal to this. */
	  public boolean equals(Object o) {
	    if (o instanceof CitationRefersToQuery) {
	    	CitationRefersToQuery fq = (CitationRefersToQuery) o;
	      return (query.equals(fq.query) && filter.equals(fq.filter) && getBoost()==fq.getBoost());
	    }
	    return false;
	  }

	  /** Returns a hash code value for this object. */
	  public int hashCode() {
	    return query.hashCode() ^ filter.hashCode() + Float.floatToRawIntBits(getBoost());
	  }
	}

class CitationRefersToFilter extends Filter {

	/**
	 * This method returns a set of documents that are referring (citing)
	 * the set of documents we retrieved in the underlying query
	 */
	  @Override
	  public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
	    final OpenBitSet bitSet = new OpenBitSet(reader.maxDoc());
	    for (int i=0; i < reader.maxDoc(); i++) {
	    	bitSet.set(i);
	    }
	    return bitSet;
	  }


}
