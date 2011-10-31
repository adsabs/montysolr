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
		String oldVal = null;
		String newVal = null;
		
		oldVal = queryTree.toString();
		int i = 1;
		System.out.println("     0. starting");
		System.out.println("--------------------------------------------");
		System.out.println(oldVal);
		
		Iterator<QueryNodeProcessor> it = this.iterator();

		QueryNodeProcessor processor;
		while (it.hasNext()) {
			processor = it.next();
			
			System.out.println("     " + i + ". step "	+ processor.getClass().toString());
			queryTree = processor.process(queryTree);
			newVal = queryTree.toString();
			System.out.println("     Tree changed: " + (newVal.equals(oldVal) ? "NO" : "YES"));
			System.out.println("--------------------------------------------");
			System.out.println(newVal);
			oldVal = newVal;
			i += 1;
		}
		
		System.out.println("");
		System.out.println("final result:");
		System.out.println("--------------------------------------------");
		System.out.println(queryTree.toString());
		return queryTree;

	}

}
