package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class AqpFieldQueryNodeBuilder implements StandardQueryBuilder {

  public AqpFieldQueryNodeBuilder() {
    // empty constructor
  }

  public Query build(QueryNode queryNode) throws QueryNodeException {
    FieldQueryNode fieldNode = (FieldQueryNode) queryNode;
    
    if (fieldNode.getFieldAsString().equals("*")
        && fieldNode.getTextAsString().equals("*")) {
      return new MatchAllDocsQuery();
    }
    
    // this is rather a hack; SOLR has no problem with lower ranges
    // being expressed as '*' but for upper ranges; it is a problem
    // builders don't have access to configuration so another problem
    // here is that we are hardcoding the 'year' field
    if (fieldNode.getTextAsString().equals("*") && fieldNode.getFieldAsString().equals("year")) {
      if (queryNode.getParent() != null && queryNode.getParent() instanceof TermRangeQueryNode &&
          ((TermRangeQueryNode)queryNode.getParent()).getUpperBound().getTextAsString().equals("*") ) {
        fieldNode.setText("2222");
      }
    }

    return new TermQuery(new Term(fieldNode.getFieldAsString(),
        fieldNode.getTextAsString()));

  }

}
