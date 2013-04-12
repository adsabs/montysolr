package org.apache.lucene.queryparser.flexible.aqp.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.search.QParser;


public class AqpSubqueryParserFull extends AqpSubqueryParser {
  
  private QParser parser = null;
  private Class<?>[] qtypes = null;
  private ReentrantLock parsingLock = new ReentrantLock();
  
  public QParser getParser() {
    return parser;
  }
  public Class[] getQtypes() {
    return qtypes;
  }
  
  public Query simplify(Query query) {
    if (query instanceof BooleanQuery) {
      List<BooleanClause>clauses = ((BooleanQuery) query).clauses();
      if (clauses.size()==1 && ((BooleanQuery) query).getBoost() == 1.0) {
        Query q = clauses.get(0).getQuery();
        if (q.toString().toString().equals("")) return null;
        if (q instanceof DisjunctionMaxQuery && ((DisjunctionMaxQuery) q).getDisjuncts().size()==1) {
          return ((DisjunctionMaxQuery) q).getDisjuncts().get(0);
        }
        return q;
      }
    }
    return query;
  }
  
  public Query reParse(Query query, QParser qp, Class<?>...types) throws ParseException {
    parsingLock.lock();
    try {
      parser = qp;
      qtypes = types;
      swimDeep(query);
      return query;
    }
    finally {
      parser = null;
      qtypes = null;
      parsingLock.unlock();
    }
  }
  
  protected boolean isWanted(Query query) {
    for (Class<?>type : qtypes) {
      if (type.isInstance(query)) {
        return true;
      }
    }
    return false;
  }

  protected Query swimDeep(TermQuery query) throws ParseException {
    if (parser != null && qtypes != null && isWanted(query)) {
      parser.setString(query.toString());
      Query newQ = parser.parse();
      newQ.setBoost(query.getBoost());
      return newQ;
    }
    return query;
  }
  
  protected Query swimDeep(DisjunctionMaxQuery query) throws ParseException {
    ArrayList<Query> parts = query.getDisjuncts();
    for (int i=0;i<parts.size();i++) {
      Query oldQ = parts.get(i);
      parts.set(i, swimDeep(oldQ));
    }
    return query;
    
  }
  
  protected Query swimDeep(BooleanQuery query) throws ParseException {
    List<BooleanClause>clauses = query.clauses();
    for (int i=0;i<clauses.size();i++) {
      BooleanClause c = clauses.get(i);
      Query qq = swimDeep(c.getQuery());
      c.setQuery(qq);
    }
    return query;
  }

  protected Query swimDeep(Query query) throws ParseException {
    if (query instanceof BooleanQuery) {
      return swimDeep((BooleanQuery) query);
    }
    else if (query instanceof DisjunctionMaxQuery) {
      return swimDeep((DisjunctionMaxQuery) query);
    }
    else if (query instanceof TermQuery) {
      return swimDeep((TermQuery) query);
    }
    return query;
  }
  
}
