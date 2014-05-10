package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.sandbox.queries.regex.RegexQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ComplexExplanation;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.SpanWeight;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.util.Bits;

public class SpanConverter {
	
	boolean wrapNonConvertible = false;

	public SpanQuery getSpanQuery(SpanConverterContainer container)
	throws QueryNodeException {
		Query q = container.query;
		if (q instanceof SpanQuery) {
			return (SpanQuery) q;
		} else if (q instanceof TermQuery) {
			return new SpanTermQuery(((TermQuery) q).getTerm());
		} else if (q instanceof WildcardQuery) {
			return new SpanMultiTermQueryWrapper<WildcardQuery>((WildcardQuery) q);
		} else if (q instanceof PrefixQuery) {
			return new SpanMultiTermQueryWrapper<PrefixQuery>((PrefixQuery) q);
		} else if (q instanceof PhraseQuery) {
			return convertPhraseToSpan(container);
		} else if (q instanceof BooleanQuery) {
			return convertBooleanToSpan(container);
		} else {
			
				SpanQuery wrapped = wrapNonConvertible(container);
				if (wrapped != null)
					return wrapped;
				
				throw new QueryNodeException(new MessageImpl(
					QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
					"(yet) Unsupported clause inside span query: "
					+ q.getClass().getName()));
		}
	}

	public SpanQuery wrapNonConvertible(SpanConverterContainer container) {
		if (wrapNonConvertible) {
			return doWrapping(container);
		}
	  return null;
  }

	private SpanQuery doWrapping(SpanConverterContainer container) {
	  return new EmptySpanQuery(container.query);
  }

	public void setWrapNonConvertible(boolean v) {
		wrapNonConvertible = v;
	}
	
	private SpanQuery convertPhraseToSpan(SpanConverterContainer container) {
		PhraseQuery q = (PhraseQuery) container.query;

		SpanQuery clauses[] = new SpanQuery[q.getTerms().length];
		int i = 0;
		for (Term term: q.getTerms()) {
			clauses[i++] = new SpanTermQuery(term); 
		}
		return new SpanNearQuery(clauses, q.getSlop() > 0 ? q.getSlop() : 1, true);
	}

	/*
	 * Silly convertor for now it can handle only boolean queries of the same type
	 * (ie not mixed cases). To do that, I have to build a graph (tree) and maybe
	 * of only pairs (?)
	 */
	protected SpanQuery convertBooleanToSpan(SpanConverterContainer container) 
	throws QueryNodeException {
		BooleanQuery q = (BooleanQuery) container.query;

		BooleanClause[] clauses = q.getClauses();
		SpanQuery[] spanClauses = new SpanQuery[clauses.length];
		Occur o = null;
		int i = 0;
		for (BooleanClause c : clauses) {
			if (o != null && !o.equals(c.getOccur())) {
				throw new QueryNodeException(new MessageImpl(
						QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
						"(yet) Unsupported clause inside span query: "
						+ q.getClass().getName()));
			}
			o = c.getOccur();

			Query sq = c.getQuery();
			SpanQuery result = getSpanQuery(new SpanConverterContainer(sq, 1, true));
			result.setBoost(sq.getBoost());
			spanClauses[i] = result;
			i++;
		}

		if (o.equals(Occur.MUST)) {
			return new SpanNearQuery(spanClauses, container.slop,
					container.inOrder);
		} else if (o.equals(Occur.SHOULD)) {
			return new SpanOrQuery(spanClauses);
		} else if (o.equals(Occur.MUST_NOT)) {
			SpanQuery[] exclude = new SpanQuery[spanClauses.length - 1];
			for (int j = 1; j < spanClauses.length; j++) {
				exclude[j - 1] = spanClauses[j];
			}
			return new SpanNotQuery(spanClauses[0], new SpanOrQuery(exclude));
		}

		throw new QueryNodeException(new MessageImpl(
				QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
				"Congratulations! You have hit (yet) unsupported case: "
				+ q.getClass().getName()));
	}

	class Leaf {
		public List<BooleanClause> members = new ArrayList<BooleanClause>();
		public BooleanClause left;
		public Leaf right;

		public Leaf(BooleanClause left, Leaf right) {
			this.left = left;
			this.right = right;
		}
	}

	/*
	 * Creates a tree of the clauses, according to operator precedence:
	 * 
	 * Thus: D +C -A -B becomes:
	 * 
	 * - / \ A - / \ B + / \ C D
	 */
	private Leaf constructTree(BooleanClause[] clauses) {
		List<BooleanClause> toProcess = Arrays.asList(clauses);
		Leaf leaf = new Leaf(null, null);
		leaf.members = toProcess;

		// from highest priority
		// findNots(leaf);
		// findAnds(leaf);
		// findOrs(leaf);
		return leaf;
	}

	private void findNots(Leaf leaf) {

		for (BooleanClause m : leaf.members) {
			if (m.getOccur().equals(Occur.MUST_NOT)) {
				leaf.members.remove(m);
				leaf.left = m;
			}
		}

	}

	
	public static class EmptySpanQuery extends SpanQuery {
		
		private Query wrappedQ;
		private Spans emptySpan;

		public EmptySpanQuery(Query wrappedQ) {
			this.wrappedQ = wrappedQ;
			
			emptySpan = new Spans() {

				@Override
        public boolean next() throws IOException {
	        return false;
        }

				@Override
        public boolean skipTo(int target) throws IOException {
	        return false;
        }

				@Override
        public int doc() {
	        return 0;
        }

				@Override
        public int start() {
	        return 0;
        }

				@Override
        public int end() {
	        return 0;
        }

				@Override
        public Collection<byte[]> getPayload() throws IOException {
	        return null;
        }

				@Override
        public boolean isPayloadAvailable() throws IOException {
	        return false;
        }

        @Override
        public long cost() {
          return 0;
        }
	    	
	    };
		}

		@Override
    public Spans getSpans(AtomicReaderContext context, Bits acceptDocs,
        Map<Term, TermContext> termContexts) throws IOException {
	    return emptySpan;
    }

		@Override
    public String getField() {
			if (wrappedQ instanceof RegexpQuery) {
				return ((RegexpQuery) wrappedQ).getField();
			}
			else if (wrappedQ instanceof RegexQuery) {
				return ((RegexQuery) wrappedQ).getField();
			}
			
			return null;
    }

		@Override
    public String toString(String field) {
	    return "EmptySpanQuery(" + wrappedQ.toString()+")";
    }
		
		@Override
	  public Weight createWeight(IndexSearcher searcher) throws IOException {
	    return new EmptySpanWeight(this);
	  }
		
		@Override
	  public void extractTerms(Set<Term> terms) {
			// pass
		}
	}
	
	public static class EmptySpanWeight extends Weight {
		
		private Query query;

		public EmptySpanWeight (Query q) {
			query = q;
		}
		
		@Override
    public Explanation explain(AtomicReaderContext context, int doc)
        throws IOException {
			return new ComplexExplanation(false, 0.0f, "Ignored: " + query.toString());
    }

		@Override
    public Query getQuery() {
	    return query;
    }

		@Override
    public float getValueForNormalization() throws IOException {
	    return 1.0f;
    }

		@Override
    public void normalize(float norm, float topLevelBoost) {
			//pass
    }

    @Override
    public Scorer scorer(AtomicReaderContext context, Bits acceptDocs)
        throws IOException {
      return null;
    }
		
	}
}
