package org.apache.lucene.queryParser.aqp.processors;

import java.util.Iterator;

import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;

public class AqpDebuggingQueryNodeProcessorPipeline extends
		AqpQueryNodeProcessorPipeline {

	public AqpDebuggingQueryNodeProcessorPipeline(QueryConfigHandler queryConfig) {
		super(queryConfig);
		
	}

	public QueryNode process(QueryNode queryTree) throws QueryNodeException {
		int i = 1;
		System.out.println("     0. starting");
		System.out.println("--------------------------------------------");
		System.out.println(queryTree.toString());
		
		Iterator<QueryNodeProcessor> it = this.iterator();

		QueryNodeProcessor processor;
		while (it.hasNext()) {
			processor = it.next();
			
			System.out.println("     " + i + ". step "	+ processor.getClass().toString());
			System.out.println("--------------------------------------------");
			queryTree = processor.process(queryTree);
			System.out.println(queryTree.toString());
			i += 1;
		}
		System.out.println("final result:");
		System.out.println(queryTree.toString());
		return queryTree;

	}

}
