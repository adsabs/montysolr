package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;

/**
 * Converts QPHRASE node into @{link {@link QuotedFieldQueryNode}. The field
 * value is the @{link DefaultFieldAttribute} specified in the configuration.
 *
 * <p>
 * <p>
 * If the user specified a field, it will be set by the @{link
 * AqpFIELDProcessor} Therefore the {@link AqpQPHRASEProcessor} should run
 * before it.
 *
 * @see QueryConfigHandler
 */
public class AqpQPHRASEProcessor extends AqpQProcessor {

    public boolean nodeIsWanted(AqpANTLRNode node) {
        return node.getTokenLabel().equals("QPHRASE");
    }

    public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
        String field = getDefaultFieldName();

        AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);

        return new QuotedFieldQueryNode(field,
                EscapeQuerySyntaxImpl.discardEscapeChar(subChild.getTokenInput()
                        .substring(1, subChild.getTokenInput().length() - 1)),
                subChild.getTokenStart() + 1, subChild.getTokenEnd() - 1);

    }

}
