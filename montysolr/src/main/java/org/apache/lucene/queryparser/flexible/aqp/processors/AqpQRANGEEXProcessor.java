package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;

/**
 * Converts QRANGEEX node into {@link TermRangeQueryNode}. The field
 * value is the @{link DefaultFieldAttribute} specified in the configuration.
 * <p>
 * Because QRANGE nodes have this shape:
 *
 * <pre>
 *                      QRANGE
 *                      /    \
 *                 QNORMAL  QPHRASE
 *                   /          \
 *                 some       "phrase"
 * </pre>
 * <p>
 * It is important to queue {@AqpQRANGEEProcessor} and
 * {@AqpQRANGEINProcessor} <b>before</b> processors that
 * transform QNORMAL, QPHRASE and other Q nodes
 * <p>
 * If the user specified a field, it will be set by the @{link
 * AqpFIELDProcessor} Therefore this processor should queue before @{link
 * AqpFIELDProcessor}.
 *
 * @see QueryConfigHandler
 * @see AqpQRANGEINProcessor
 */
public class AqpQRANGEEXProcessor extends AqpQRANGEINProcessor {

    public AqpQRANGEEXProcessor() {
        lowerInclusive = false;
        upperInclusive = false;
    }

    public boolean nodeIsWanted(AqpANTLRNode node) {
        return node.getTokenLabel().equals("QRANGEEX");
    }

}
