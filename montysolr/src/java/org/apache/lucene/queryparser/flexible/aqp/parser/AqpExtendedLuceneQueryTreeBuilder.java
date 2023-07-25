package org.apache.lucene.queryparser.flexible.aqp.parser;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpNearQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;

public class AqpExtendedLuceneQueryTreeBuilder extends
    AqpStandardQueryTreeBuilder {
	
	public void init() {
		super.init();
		setBuilder(AqpNearQueryNode.class,	new AqpNearQueryNodeBuilder());
	}

}
