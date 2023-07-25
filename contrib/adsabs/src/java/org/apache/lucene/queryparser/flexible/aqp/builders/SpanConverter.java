package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.SynonymQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.spans.SpanBoostQuery;
import org.apache.lucene.search.spans.SpanCollector;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanScorer;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.SpanWeight;
import org.apache.lucene.search.spans.Spans;

public class SpanConverter {
	
	boolean wrapNonConvertible = false;

	public SpanQuery getSpanQuery(SpanConverterContainer container)
	throws QueryNodeException {
		Query q = container.query;
		float boost = container.boost;
		
		if (q instanceof SpanQuery) {
			return wrapBoost((SpanQuery) q, boost);
		} else if (q instanceof TermQuery) {
			return wrapBoost(new SpanTermQuery(((TermQuery) q).getTerm()), boost);
		} else if (q instanceof ConstantScoreQuery) {
			return getSpanQuery(new SpanConverterContainer(((ConstantScoreQuery) q).getQuery(), 1, true, 0.0f));
		} else if (q instanceof WildcardQuery) {
			return wrapBoost(new SpanMultiTermQueryWrapper<WildcardQuery>((WildcardQuery) q), boost);
		} else if (q instanceof PrefixQuery) {
			return wrapBoost(new SpanMultiTermQueryWrapper<PrefixQuery>((PrefixQuery) q), boost);
		} else if (q instanceof PhraseQuery) {
			return wrapBoost(convertPhraseToSpan(container), boost);
		} else if (q instanceof BooleanQuery) {
			return wrapBoost(convertBooleanToSpan(container), boost);
		} else if (q instanceof RegexpQuery) {
		    return wrapBoost(new SpanMultiTermQueryWrapper<RegexpQuery>((RegexpQuery) q), boost);
		} else if (q instanceof DisjunctionMaxQuery) {
			return wrapBoost(convertDisjunctionQuery(container), boost);
		} else if (q instanceof BoostQuery) {
		    return wrapBoost(getSpanQuery(new SpanConverterContainer(((BoostQuery) q).getQuery(), 1, true)),
		      ((BoostQuery) q).getBoost());
		} else if (q instanceof MatchNoDocsQuery) {
	        return new EmptySpanQuery(container.query);
		} else if (q instanceof SynonymQuery) {
            return wrapBoost(convertSynonymToSpan(container), boost);
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
	
	private SpanQuery convertDisjunctionQuery(SpanConverterContainer container) throws QueryNodeException {
    DisjunctionMaxQuery q = (DisjunctionMaxQuery) container.query;
    List<Query> clauses = q.getDisjuncts();
    if (clauses.size() == 0) {
      container.query = new MatchNoDocsQuery();
      return getSpanQuery(container);
    }
    else if (clauses.size() == 1) {
      container.query = clauses.get(0);
      return getSpanQuery(container);
    }
    else {
      // we assume it is OR query case
      List<Query> disjuncts = q.getDisjuncts();
      BooleanQuery.Builder bQuery = new BooleanQuery.Builder();
      for (Query qa: disjuncts) {
        bQuery.add(qa, Occur.SHOULD);
      }
      container.query = bQuery.build();
      return getSpanQuery(container);
    }
  }

  private SpanQuery wrapBoost(SpanQuery q, float boost) {
	  if (boost != 1.0f)
	    return new SpanBoostQuery(q, boost);
	  return q;
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

		List<BooleanClause> clauses = q.clauses();
		SpanQuery[] spanClauses = new SpanQuery[clauses.size()];
		String field = null;
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
			spanClauses[i] = result;
			
			i++;
		}
		
		try {
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
		}
		catch (IllegalArgumentException exc) {
			throw new QueryNodeException(new MessageImpl(
					QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
					"Proximity searches must be executed against the same field; please specify the field explicitly"));
		}
		

		throw new QueryNodeException(new MessageImpl(
				QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
				"Congratulations! You have hit (yet) unsupported case: "
				+ q.getClass().getName()));
	}

	/**
	 * Convert Synonym query, essentially will treat it the same way as 
	 * boolean OR query
	 * 
	 */
	protected SpanQuery convertSynonymToSpan(SpanConverterContainer container) 
   throws QueryNodeException {
     SynonymQuery q = (SynonymQuery) container.query;
     SpanQuery[] spanClauses = new SpanQuery[q.getTerms().size()];
     
     int i = 0;
     for (Term t : q.getTerms()) {
       TermQuery sq = new TermQuery(t);
       SpanQuery result = getSpanQuery(new SpanConverterContainer(sq, 1, false));
       spanClauses[i] = result;
       i++;
     }
     
     return new SpanOrQuery(spanClauses);
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
        public int nextStartPosition() throws IOException {
          // TODO Auto-generated method stub
          return 0;
        }

        @Override
        public int startPosition() {
          // TODO Auto-generated method stub
          return 0;
        }

        @Override
        public int endPosition() {
          // TODO Auto-generated method stub
          return 0;
        }

        @Override
        public int width() {
          // TODO Auto-generated method stub
          return 0;
        }

        @Override
        public void collect(SpanCollector collector) throws IOException {
          // TODO Auto-generated method stub
          
        }

        @Override
        public float positionsCost() {
          // TODO Auto-generated method stub
          return 0;
        }

        @Override
        public int docID() {
          // TODO Auto-generated method stub
          return 0;
        }

        @Override
        public int nextDoc() throws IOException {
          // TODO Auto-generated method stub
          return 0;
        }

        @Override
        public int advance(int target) throws IOException {
          // TODO Auto-generated method stub
          return 0;
        }

        @Override
        public long cost() {
          // TODO Auto-generated method stub
          return 0;
        }
	    };
		}


		@Override
    public String getField() {
			if (wrappedQ instanceof RegexpQuery) {
				return ((RegexpQuery) wrappedQ).getField();
			}
			return null;
    }

		@Override
    public String toString(String field) {
	    return "EmptySpanQuery(" + wrappedQ.toString()+")";
    }
		
		@Override
	  public SpanWeight createWeight(IndexSearcher searcher, boolean needsScores, float boost) throws IOException {
	    return new EmptySpanWeight(this, searcher, null, boost);
	  }
		

    @Override
    public boolean equals(Object obj) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public int hashCode() {
      // TODO Auto-generated method stub
      return 0;
    }
	}
	
	public static class EmptySpanWeight extends SpanWeight {
		

    public EmptySpanWeight(SpanQuery query, IndexSearcher searcher, Map<Term, TermContext> termContexts, float boost)
        throws IOException {
      super(query, searcher, termContexts, boost);
    }


    @Override
    public void extractTerms(Set<Term> terms) {
      
    }

    @Override
    public Explanation explain(LeafReaderContext context, int doc) throws IOException {
      return Explanation.noMatch("Ignored: " + parentQuery.toString(), new ArrayList<Explanation>());
    }

    @Override
    public SpanScorer scorer(LeafReaderContext context) throws IOException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void extractTermContexts(Map<Term, TermContext> contexts) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public Spans getSpans(LeafReaderContext ctx, Postings requiredPostings) throws IOException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public boolean isCacheable(LeafReaderContext ctx) {
      return false;
    }
		
	}
}
