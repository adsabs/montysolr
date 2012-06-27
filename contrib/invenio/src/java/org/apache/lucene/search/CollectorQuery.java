package org.apache.lucene.search;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.ReaderUtil;
import org.apache.lucene.util.ToStringUtils;


/**
 * 
 * This is a first attempt on the 2nd order operators, while it works, 
 * it will work only for searching inside the boundaries of index
 * segments.
 * 
 */
public class CollectorQuery extends Query {

	private static final long serialVersionUID = -5670377581753190942L;
	Query query;
	Filter filter = null;
	Map<Integer, Integer> docStarts;
	private CollectorCreator collectorCreator;

	/**
	 * Constructs a new query which applies a filter to the results of the
	 * original query. Filter.getDocIdSet() will be called every time this query
	 * is used in a search.
	 * 
	 * @param query
	 *            Query to be filtered, cannot be <code>null</code>.
	 * @param filter
	 *            Filter to apply to query results, cannot be <code>null</code>.
	 */
	/*
	public CollectorQuery(Query query, Filter filter, Collector collector) {
		this.query = query;
		this.filter = filter;
		this.collector = collector;
		
		if (collector == null) {
			throw new IllegalStateException("Collector must not be null");
		}
	}
	
	public CollectorQuery(Query query, IndexReader reader, Collector collector) {
		this.query = query;
		this.filter = null;
		this.collector = collector;
		
		if (collector == null) {
			throw new IllegalStateException("Collector must not be null");
		}
		initDocStarts(reader);
		
	}
	*/
	
	public CollectorQuery(Query query, IndexReader reader, CollectorCreator creator ) {
		this.query = query;
		this.collectorCreator = creator;
		docStarts = getDocStarts(reader);
	}
	
	private Map<Integer, Integer> getDocStarts(IndexReader reader) {
		List<IndexReader> subReadersList = new ArrayList<IndexReader>();
	    ReaderUtil.gatherSubReaders(subReadersList, reader);
	    IndexReader[] subReaders = subReadersList.toArray(new IndexReader[subReadersList.size()]);
	    Map<Integer, Integer> docStarts = new HashMap<Integer, Integer>(subReaders.length);
	    int maxDoc = 0;
	    for (int i = 0; i < subReaders.length; i++) {
	      docStarts.put(subReaders[i].hashCode(), maxDoc);
	      maxDoc += subReaders[i].maxDoc();
	    }
	    return docStarts;
	}
	
	/**
	 * Returns a Weight that applies the filter to the enclosed query's Weight.
	 * This is accomplished by overriding the Scorer returned by the Weight.
	 */
	public Weight createWeight(final Searcher searcher) throws IOException {
	    
		/*
		try {
			Collector collector = collectorCreator.create();
			searcher.search(query, collector);
			Set<Integer> hits = ((SetCollector) collector).getHits();
			System.out.println(hits.size());
			Map<Integer, Integer> ds = getDocStarts(((IndexSearcher) searcher).getIndexReader());
			System.out.println(ds);
		} catch (Exception e) {
			//pass
		}
		*/
	    
		Weight weight = query.createWeight(searcher);
		Similarity similarity = query.getSimilarity(searcher);
		
		
		return new CollectorWeight(weight, similarity, collectorCreator, docStarts);
	}
	

	/** Rewrites the wrapped query. */
	public Query rewrite(IndexReader reader) throws IOException {
		Query rewritten = query.rewrite(reader);
		if (rewritten != query) {
			CollectorQuery clone = (CollectorQuery) this.clone();
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
	
	public CollectorCreator getCollectorCreator() {
		return collectorCreator;
	}

	// inherit javadoc
	public void extractTerms(Set<Term> terms) {
		getQuery().extractTerms(terms);
	}

	/** Prints a user-readable version of this query. */
	public String toString(String s) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CollectorQuery(");
		buffer.append(query.toString(s));
		buffer.append(", filter=" + (filter!=null ? filter.toString() : "null"));
		buffer.append(", collector=" + (collectorCreator!=null ? collectorCreator.toString() : "null"));
		buffer.append(")");
		buffer.append(ToStringUtils.boost(getBoost()));
		return buffer.toString();
	}

	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof CollectorQuery) {
			CollectorQuery fq = (CollectorQuery) o;
			return (query.equals(fq.query) 
					&& (filter != null ? filter.equals(fq.filter) : true)
					&& collectorCreator.equals(fq.collectorCreator)
					&& getBoost() == fq.getBoost());
		}
		return false;
	}

	/** Returns a hash code value for this object. */
	public int hashCode() {
		if (filter != null) {
			return query.hashCode() ^ filter.hashCode() ^ collectorCreator.hashCode()
				+ Float.floatToRawIntBits(getBoost());
		}
		
		return query.hashCode() ^ collectorCreator.hashCode()
				+ Float.floatToRawIntBits(getBoost());
	}
	
	
	public static class CollectorCreator {
		private Constructor<Collector> constructor;
		private Object[] params;

		public CollectorCreator(Constructor<Collector> constructor, Object...params) {
			this.constructor = constructor;
			this.params = params;
		}
		
		public Collector create() throws IllegalArgumentException, InstantiationException, 
			IllegalAccessException, InvocationTargetException {
			return constructor.newInstance(params);
		}
		
		/** Returns a hash code value for this object. */
		public int hashCode() {
			int h = constructor.hashCode();
			for (Object o: params) {
				h = h ^ o.hashCode();
			}
			return h;
		}
		
		/** Returns true iff <code>o</code> is equal to this. */
		public boolean equals(Object o) {
			if (o instanceof CollectorCreator) {
				CollectorCreator fq = (CollectorCreator) o;
				return hashCode() == fq.hashCode();
			}
			return false;
		}
		
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append(constructor.getDeclaringClass().getSimpleName());
			buffer.append("(");
			int i = 0;
			for (Object o: params) {
				if (i>0) {
					buffer.append(", ");
				}
				if (o instanceof String) {
					buffer.append((String) o);
				}
				else {
					buffer.append(o.getClass().getSimpleName());
				}
				
				i++;
			}
			buffer.append(")");
			return buffer.toString();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static CollectorCreator createCollector(Class clazz, Object...params) throws SecurityException, NoSuchMethodException {
		
		@SuppressWarnings("rawtypes")
		Class[] parameterTypes = new Class[params.length];
		int i = 0;
		for (Object o: params) {
			Class<? extends Object> cls = o.getClass();
			if (o instanceof String || o instanceof int[][] || o instanceof int[]) {
				parameterTypes[i] = cls;
			}
			else if (cls.getInterfaces().length > 0) {
				parameterTypes[i] = cls.getInterfaces()[0];
			}
			else {
				parameterTypes[i] = cls;
			}
			i++;
		}
		
		Constructor<Collector> constructor = clazz.getConstructor(parameterTypes);
		return new CollectorQuery.CollectorCreator(constructor, params);
		
	}
}
