package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.search.Query;

public class SpanConverterContainer {
	public Query query;
	public int slop;
	public boolean inOrder;
  public float boost;

	public SpanConverterContainer(Query q, int slop, boolean inOrder) {
		this.query = q;
		this.slop = slop;
		this.inOrder = inOrder;
		this.boost = 1.0f;
	}
	
	public SpanConverterContainer(Query q, int slop, boolean inOrder, float boost) {
    this.query = q;
    this.slop = slop;
    this.inOrder = inOrder;
    this.boost = boost;
  }
}
