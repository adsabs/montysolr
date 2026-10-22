package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.solr.request.SolrQueryRequest;

import java.util.List;

/**
 * Expands active synonym wrappers through normal analysis. For inactive wrappers, exact-tagged
 * fields use an explicitly configured {@code _nosyn} field when available; other fields bypass
 * analysis.
 *
 * @author rchyla
 */
public class AqpAdsabsSynonymNodeProcessor extends QueryNodeProcessorImpl implements
        QueryNodeProcessor {

    @Override
    protected QueryNode preProcessNode(QueryNode node)
            throws QueryNodeException {
        return node;
    }

    @Override
    protected QueryNode postProcessNode(QueryNode node)
            throws QueryNodeException {
        if (node instanceof AqpAdsabsSynonymQueryNode) {
            AqpAdsabsSynonymQueryNode synNode = (AqpAdsabsSynonymQueryNode) node;
            if (synNode.isActivated()) {
                return expandSynonyms(synNode);

            } else {

                return applyNonAnalyzableToAllChildren(synNode.getChild());
            }
        }
        return node;
    }

    protected QueryNode applyNonAnalyzableToAllChildren(QueryNode node) {

        if (node instanceof AqpNonAnalyzedQueryNode) {
            return node;
        } else if (node instanceof FieldQueryNode) {

            if (node.getTag("aqp.exact") != null) {
                FieldQueryNode fieldNode = (FieldQueryNode) node;
                String fieldName = fieldNode.getFieldAsString();
                String noSynonymsField = fieldName + "_nosyn";

                if (hasExplicitField(noSynonymsField)) {
                    fieldNode.setField(noSynonymsField);
                    return fieldNode;
                }
            }

            // catch all, avoid analysis
            return new AqpNonAnalyzedQueryNode((FieldQueryNode) node);
        }

        List<QueryNode> children = node.getChildren();

        if (children != null) {

            for (int i = 0; i < children.size(); i++) {
                children.set(i, applyNonAnalyzableToAllChildren(children.get(i)));
            }
            node.set(children);
        }
        return node;

    }

    protected QueryNode expandSynonyms(AqpAdsabsSynonymQueryNode synNode) {
        // I believe it is the job of the analyzers to expand the node, but it may depend...
        return synNode.getChild();
    }

    private boolean hasExplicitField(String fieldName) {
        SolrQueryRequest req = this.getQueryConfigHandler()
                .get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
                .getRequest();
        return req != null && req.getSchema().hasExplicitField(fieldName);
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }

}
