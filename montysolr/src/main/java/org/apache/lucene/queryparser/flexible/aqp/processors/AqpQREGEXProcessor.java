package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;

/**
 * Converts QREGEX node into @{link {@link RegexpQueryNode}. The field value is
 * the @{link DefaultFieldAttribute} specified in the configuration.
 *
 * <p>
 * <p>
 * If the user specified a field, it will be set by the @{link
 * AqpFIELDProcessor} Therefore the {@link AqpQNORMALProcessor} should run
 * before it.
 *
 * @see QueryConfigHandler
 */
public class AqpQREGEXProcessor extends AqpQProcessor {

    public boolean nodeIsWanted(AqpANTLRNode node) {
        return node.getTokenLabel().equals("QREGEX");
    }

    public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
        String field = getDefaultFieldName();

        AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
        String input = subChild.getTokenInput();
        return new AqpAdsabsRegexQueryNode(field, input.substring(1,
                input.length() - 1), subChild.getTokenStart(), subChild.getTokenEnd());
    }

}
