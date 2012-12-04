package org.apache.lucene.queryparser.flexible.aqp;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.search.QParser;


public class AqpSubqueryParserFull extends AqpSubqueryParser {
  
  
  private QParser parser;
  private Class<?>[] qtypes;

  public Query reParse(Query query, QParser qp, Class<?>...types) throws ParseException {
    parser = qp;
    qtypes = types;
    swimDeep(query);
    return query;
  }
  
  private boolean isWanted(Query query) {
    for (Class<?>type : qtypes) {
      if (type.isInstance(query)) {
        return true;
      }
    }
    return false;
  }

  private Query swimDeep(TermQuery query) throws ParseException {
    if (isWanted(query)) {
      parser.setString(query.toString());
      return parser.parse();
    }
    return query;
  }
  
  private Query swimDeep(DisjunctionMaxQuery query) throws ParseException {
    ArrayList<Query> parts = query.getDisjuncts();
    for (int i=0;i<parts.size();i++) {
      parts.set(i, swimDeep(parts.get(i)));
    }
    return query;
    
  }
  
  private Query swimDeep(BooleanQuery query) throws ParseException {
    List<BooleanClause>clauses = query.clauses();
    for (int i=0;i<clauses.size();i++) {
      BooleanClause c = clauses.get(i);
      Query qq = swimDeep(c.getQuery());
      c.setQuery(qq);
    }
    return query;
  }

  private Query swimDeep(Query query) throws ParseException {
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
