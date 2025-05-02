package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;

import java.util.Optional;

/**
 * Detects first-author queries (author:^name) and rewrites them
 * into first_author:name queries to avoid generating pos(...) functions.
 */
public class AqpFirstAuthorMappingProcessor extends AqpQProcessor {

    @Override
    public boolean nodeIsWanted(AqpANTLRNode node) {
        // Only process nodes labeled as QPOSITION (tagged for positional queries)
        return node.getTokenLabel().equals("FIELD") &&
                node.getChildren().stream().anyMatch((child) -> child instanceof AqpANTLRNode
                        && ((AqpANTLRNode) child).getTokenLabel().equals("QPOSITION"));
    }

    public boolean hasQPositionChild(QueryNode node) {
        if (!(node instanceof AqpANTLRNode))
            return false;

        if (((AqpANTLRNode) node).getChild("QPOSITION") != null)
            return true;

        return node.getChildren().stream().anyMatch(this::hasQPositionChild);
    }

    @Override
    public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
        if (node.getChildren().size() > 2) {
            // This processor does not work for complex nested queries
            return node;
        }

        Optional<QueryNode> termNormalNode = node.getChildren().stream()
                .filter((child) -> child instanceof AqpANTLRNode
                    && ((AqpANTLRNode) child).getTokenName().equals("TERM_NORMAL"))
                .findFirst();
        if (termNormalNode.isEmpty()) {
            return node;
        }

        String fieldName = ((AqpANTLRNode)termNormalNode.get()).getTokenInput();
        if (!fieldName.equals("author")) {
            return node;
        }

        Optional<QueryNode> positionNode = node.getChildren().stream()
                .filter((child) -> child instanceof AqpANTLRNode
                    && ((AqpANTLRNode) child).getTokenName().equals("QPOSITION"))
                .findFirst();
        if (positionNode.isEmpty()) {
            return node;
        }

        AqpANTLRNode qPositionNode = (AqpANTLRNode) positionNode.get();
        AqpANTLRNode subChild = (AqpANTLRNode) qPositionNode.getChildren().get(0);
        String input = subChild.getTokenInput().trim();

        // Only handle queries starting with ^
        if (!input.startsWith("^")) {
            return node;  // Let it proceed to normal pos(...) handling
        }

        // Strip the ^ and validate input
        String authorName = input.substring(1).trim();
        if (authorName.isEmpty()) {
            throw new QueryNodeException(
                    new MessageImpl("First author name is empty after '^' stripping."));
        }

        // Create FieldQueryNode targeting first_author
        FieldQueryNode firstAuthorNode = new FieldQueryNode(
                "first_author",
                authorName,
                subChild.getInputTokenStart(),
                subChild.getInputTokenEnd()
        );

        return firstAuthorNode;
    }

    // Utility method to determine the field associated with the node
    private String getFieldName(AqpANTLRNode node, String defaultField) {
        if (node.getParent() == null || node.getParent().getChildren().size() != 2) {
            return defaultField;
        }

        QueryNode possibleField = node.getParent().getChildren().get(0);
        if (possibleField instanceof AqpANTLRNode) {
            String testValue = ((AqpANTLRNode) possibleField).getTokenInput();
            if (testValue != null) {
                return testValue;
            }
        }
        return defaultField;
    }
}
