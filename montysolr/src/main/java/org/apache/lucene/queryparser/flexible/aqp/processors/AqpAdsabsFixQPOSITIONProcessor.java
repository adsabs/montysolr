package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

public class AqpAdsabsFixQPOSITIONProcessor extends AqpQProcessor {

    @Override
    public boolean nodeIsWanted(AqpANTLRNode node) {
        return node.getTokenLabel().equals("QNORMAL") || node.getTokenLabel().startsWith("QPHRASE");
    }

    public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {

        AqpANTLRNode inputNode = ((AqpANTLRNode) node.getChildren().get(0));
        String input = inputNode.getTokenInput();

        // TODO: emit warnings

        if (node.getTokenLabel().equals("QNORMAL")) {
            if (hasTrailingPositionMarker(input)) {
                node.setTokenName("QPOSITION");
                node.setTokenLabel("QPOSITION");
            }
        } else {
            String unquotedInput = input.substring(1, input.length() - 1);
            if (unquotedInput.startsWith("^") || hasTrailingPositionMarker(unquotedInput)) {
                node.setTokenName("QPOSITION");
                node.setTokenLabel("QPOSITION");
                inputNode.setTokenInput(unquotedInput);
            }
        }

        return node;
    }

    private boolean hasTrailingPositionMarker(String input) {
        // A lone dollar is not the obsolete trailing-dollar author-position marker.
        return input.endsWith("$") && input.length() > 1;
    }

}
