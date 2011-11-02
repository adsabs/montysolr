package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.ParametricQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.ParametricQueryNode.CompareOperator;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;

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

	AqpQRANGEEXProcessor() {
		lowerComparator = CompareOperator.GT;
		upperComparator = CompareOperator.LT;
	}

}
