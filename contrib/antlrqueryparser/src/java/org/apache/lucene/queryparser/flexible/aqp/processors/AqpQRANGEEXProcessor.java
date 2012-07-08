package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.ParametricQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ParametricQueryNode.CompareOperator;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * Converts QRANGEEX node into @{link {@link ParametricQueryNode}. 
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
 * @see AqpQRANGEINProcessor
 * @see AqpQueryNodeProcessorPipeline
 *
 */
public class AqpQRANGEEXProcessor extends AqpQRANGEINProcessor {

	public AqpQRANGEEXProcessor() {
		lowerComparator = CompareOperator.GT;
		upperComparator = CompareOperator.LT;
	}
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QRANGEEX")) {
			return true;
		}
		return false;
	}

}
