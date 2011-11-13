package org.apache.solr.search.function;



import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.ValueSourceParser;
import org.apache.solr.search.function.distance.StringDistanceFunction;

public class AuthorPositionSourceParser extends ValueSourceParser {

	@Override
	public ValueSource parse(FunctionQParser fp) throws ParseException {
		String author = fp.parseArg();
        int start = fp.parseInt();
        int end = fp.parseInt();

		return new AuthorPositionFunction(author, start, end);
	}

	@Override
    public void init(NamedList args) {
	/* initialize the value to consider as null */
    }

}

class AuthorPositionFunction extends ValueSource {
	protected String author;
	protected int start = 0;
	protected int end = 9999;
	protected HashMap<Integer, Float> subQueryHits;
	protected SpanTermQuery subQuery;

	AuthorPositionFunction(String author, int start, int end) {
		this.author = author;
		this.start = start;
		this.end = end;
	    subQuery = new SpanTermQuery(new Term("author_ws", this.author)); //HACK
	}

	protected String name() {
	    return "author";
  }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
	    if (!(o instanceof AuthorPositionFunction)) return false;

	    AuthorPositionFunction that = (AuthorPositionFunction) o;
	    if (this.hashCode() != that.hashCode()) return false;

	    return false;
	}

	@Override
	public int hashCode() {
		int result = author.hashCode();
	    result = 31 * result + start;
	    result = 31 * result + end;
	    return result;
	}

	@Override
	public String description() {
		StringBuilder sb = new StringBuilder();
		sb.append("author").append('(');
		sb.append(author).append(',').append(start).append(",").append(end);
		sb.append(')');
		return sb.toString();
	}

	@Override
	  public void createWeight(Map context, Searcher searcher) throws IOException {
		// collect the docs that match
		subQueryHits = new HashMap<Integer, Float>(); //XXX: probably inefficient, should do better
		IndexReader reader = ((IndexSearcher)searcher).getIndexReader();
		Spans spans = subQuery.getSpans(reader);
		while (spans.next() == true) {
			int s = spans.start();
			int e = spans.end();
			if (s >= this.start && e<= this.end) { // all inclusive, shall it be overlap?
				subQueryHits.put(spans.doc(), (float) s);
				//spans.skipTo(spans.doc());
			}
		}



		/**


		searcher.search(subQuery,  new Collector() {
	         private int base = 0;
	         private Scorer scorer;
	         @Override
	         public void setScorer(Scorer scorer) throws IOException {
	          this.scorer = scorer;
	         }
	         @Override
	         public final void collect(int doc) throws IOException {
	        	 subQueryHits.put(doc + base, scorer.score());
	         }
	         @Override
	         public void setNextReader(IndexReader reader, int docBase) {
	           base = docBase;
	         }
	         @Override
	         public boolean acceptsDocsOutOfOrder() {
	           return true;
	         }
	       });
	     **/

	  }

	@Override
	  public DocValues getValues(Map context, IndexReader reader) throws IOException {
	    return new DocValues() {

	      @Override
	      public float floatVal(int doc) {
	    	  //XXX: is docBase already included in the doc? or shall we do st with context???
	    	  if (subQueryHits.containsKey(doc)) {
	    		  return 1.0f; // this could do nice weighting/scoring as well
	    	  }
	    	  else {
	    		  return -1.0f;
	    		  //return -Float.MAX_VALUE;
	    	  }
	      }

	      @Override
	      public int intVal(int doc) {
	        return (int) doubleVal(doc);
	      }

	      @Override
	      public long longVal(int doc) {
	        return (long) doubleVal(doc);
	      }

	      @Override
	      public double doubleVal(int doc) {
	        return (double) floatVal(doc);
	      }

	      @Override
	      public String toString(int doc) {
	        StringBuilder sb = new StringBuilder();
	        sb.append("author").append('(');
	        sb.append(subQuery.toString());
	        sb.append(" * ").append(floatVal(doc));
	        sb.append(')');
	        return sb.toString();
	      }
	    };
	  }

}
