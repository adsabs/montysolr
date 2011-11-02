package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.processors.AqpQRANGEINProcessor.NodeData;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.ParametricQueryNode;
import org.apache.lucene.queryParser.core.nodes.ParametricRangeQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.ParametricQueryNode.CompareOperator;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;

/**
 * Converts QRANGEIN node into @{link {@link ParametricQueryNode}. 
 * The field value is the @{link DefaultFieldAttribute} 
 * specified in the configuration.
 * 
 * Because QRANGE nodes have this shape:
 * <pre>
 *                      QRANGE
 *                      /    \
 *                 QNORMAL  QPHRASE
 *                   /          \
 *                 some       "phrase"
 * </pre>
 * 
 * It is important to queue {@AqpQRANGEEProcessor} and {@AqpQRANGEINProcessor}
 * <b>before</b> processors that transform QNORMAL, QPHRASE and other Q nodes
 * <br/>
 * 
 * If the user specified a field, it will be set by the @{link AqpFIELDProcessor}
 * Therefore this processor should queue before @{link AqpFIELDProcessor}.
 * 
 * 
 * @see QueryConfigHandler
 * @see DefaultFieldAttribute
 * @see AqpQRANGEEXProcessor
 * @see AqpQueryNodeProcessorPipeline
 *
 */
public class AqpQRANGEINProcessor extends AqpQProcessor {
	
	protected CompareOperator lowerComparator = CompareOperator.GE;
	protected CompareOperator upperComparator = CompareOperator.LE;
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QRANGEIN")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		String field = getDefaultFieldName();
		
		
		AqpANTLRNode lowerNode = (AqpANTLRNode) node.getChildren().get(0);
		AqpANTLRNode upperNode = (AqpANTLRNode) node.getChildren().get(1);
		
		NodeData lower = getTokenInput(lowerNode);
		NodeData upper = getTokenInput(upperNode);
		
		ParametricQueryNode lowerBound = new ParametricQueryNode(field,
				lowerComparator, EscapeQuerySyntaxImpl.discardEscapeChar(lower.value),
				lower.start, lower.end);
		ParametricQueryNode upperBound = new ParametricQueryNode(field,
				upperComparator, EscapeQuerySyntaxImpl.discardEscapeChar(upper.value),
				upper.start, upper.end);
		
		return new ParametricRangeQueryNode(lowerBound, upperBound);
		
	}
	
	public NodeData getTokenInput(AqpANTLRNode node) {
		String label = node.getTokenLabel();
		AqpANTLRNode subNode = (AqpANTLRNode) node.getChildren().get(0);
		
		if (label.equals("QANYTHING")) {
			return new NodeData("*", subNode.getTokenStart(), subNode.getTokenEnd());
		}
		else if (label.contains("PHRASE")) {
			return new NodeData(subNode.getTokenInput().substring(1, -1), 
					subNode.getTokenStart()+1, subNode.getTokenEnd()-1);
		}
		else {
			return new NodeData(subNode.getTokenInput(), 
					subNode.getTokenStart(), subNode.getTokenEnd());
		}
	}
	
	class NodeData {
		public String value;
		public int start;
		public int end;
		NodeData(String value, int start, int end) {
			this.value = value;
			this.start = start;
			this.end = end;
		}
	}

}
