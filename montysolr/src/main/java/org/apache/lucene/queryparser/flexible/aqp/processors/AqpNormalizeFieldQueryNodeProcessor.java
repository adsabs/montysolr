package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.RangeQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.solr.request.SolrQueryRequest;

import java.util.List;


public class AqpNormalizeFieldQueryNodeProcessor extends
        QueryNodeProcessorImpl {


    @Override
    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
        if (node instanceof AqpNonAnalyzedQueryNode) {
            return node;
        }
        if (node instanceof WildcardQueryNode
                || node instanceof FuzzyQueryNode
                || (node instanceof FieldQueryNode && node.getParent() instanceof RangeQueryNode)
                || node instanceof RegexpQueryNode) {


            FieldQueryNode txtNode = (FieldQueryNode) node;
            CharSequence text = txtNode.getText();

            Analyzer analyzer = getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);

            if (analyzer != null) {
                String field = txtNode.getFieldAsString();
                if (hasAnalyzer(field + "_query_normalizer")) {
                    text = analyzer.normalize(field + "_query_normalizer", text.toString()).utf8ToString();
                } else if (hasAnalyzer("query_normalizer")) {
                    text = analyzer.normalize("query_normalizer", text.toString()).utf8ToString();
                } else {
                    text = analyzer.normalize(field, text.toString()).utf8ToString();
                }
                txtNode.setText(text);
            }
        }

        return node;
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }

    private boolean hasAnalyzer(String fieldName) {
        SolrQueryRequest req = this.getQueryConfigHandler()
                .get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
                .getRequest();
        return req == null || req.getSchema().hasExplicitField(fieldName);
    }
}
