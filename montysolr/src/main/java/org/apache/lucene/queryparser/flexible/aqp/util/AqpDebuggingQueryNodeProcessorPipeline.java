package org.apache.lucene.queryparser.flexible.aqp.util;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;

import java.util.Iterator;
import java.util.List;

/**
 * This class is used for debugging purposes (eg. from unittests
 * or when the query parser was invoked with debuqQuery=true)
 * <p>
 * The debugging output shows the stage of the AST tree after
 * each processing stage completed. Including the changes in
 * the internal 'map'.
 */
public class AqpDebuggingQueryNodeProcessorPipeline extends
        QueryNodeProcessorPipeline {

    EscapeQuerySyntax escaper = new EscapeQuerySyntaxImpl();
    private final Class<? extends QueryNodeProcessorPipeline> originalProcessorClass;
    private final StringBuilder debugOutput;

    public AqpDebuggingQueryNodeProcessorPipeline(QueryConfigHandler queryConfig,
                                                  Class<? extends QueryNodeProcessorPipeline> originalClass) {
        this(queryConfig, originalClass, new StringBuilder());
    }

    public AqpDebuggingQueryNodeProcessorPipeline(QueryConfigHandler queryConfig,
                                                  Class<? extends QueryNodeProcessorPipeline> originalClass,
                                                  StringBuilder debugOutput) {
        super(queryConfig);
        originalProcessorClass = originalClass;
        this.debugOutput = debugOutput;
    }

    private void debug(String message) {
        System.out.println(message);
        debugOutput.append(message).append('\n');
    }

    public QueryNode process(QueryNode queryTree) throws QueryNodeException {
        String oldVal = null;
        String newVal = null;
        String oldMap = null;
        String newMap = null;

        oldVal = queryTree.toString();
        oldMap = harvestTagMap(queryTree);
        newMap = oldMap;

        int i = 1;
        debug(this.getClass().toGenericString());
        debug("     0. starting");
        debug("--------------------------------------------");
        debug(oldVal);

        Iterator<QueryNodeProcessor> it = this.iterator();

        QueryNodeProcessor processor;
        while (it.hasNext()) {
            processor = it.next();

            debug("     " + i + ". step "
                    + processor.getClass().toString());
            queryTree = processor.process(queryTree);
            newVal = queryTree.toString();
            newMap = harvestTagMap(queryTree);
            debug("     Tree changed: "
                    + (newVal.equals(oldVal) ? "NO" : "YES"));

            if (!newMap.equals(oldMap)) {
                debug("     Tags changed: YES");
                debug("     -----------------");
                debug(newMap);
                debug("     -----------------");
            }
            debug(newVal.equals(oldVal) ? (newMap.equals(oldMap) ? "" : newVal) : newVal);

            debug("--------------------------------------------");


            oldVal = newVal;
            oldMap = newMap;
            i += 1;
        }

        debug("");
        debug("final result:");
        debug("--------------------------------------------");
        debug(queryTree.toString());
        return queryTree;

    }

    private String harvestTagMap(QueryNode queryTree) {
        StringBuffer output = new StringBuffer();
        harvestTagMapDesc(queryTree, output, 0);
        return output.toString().trim();
    }

    private void harvestTagMapDesc(QueryNode queryTree, StringBuffer output,
                                   int level) {
        if (queryTree.getTagMap().size() > 0) {
            for (int i = 0; i < level; i++) {
            }
            output.append(queryTree.toQueryString(escaper));
            output.append(" : ");
            // output.append(queryTree.getClass().getSimpleName());
            // output.append(" : ");
            output.append(queryTree.getTagMap());
            output.append("\n");
        }
        List<QueryNode> children = queryTree.getChildren();
        if (children != null) {
            for (QueryNode child : queryTree.getChildren()) {
                harvestTagMapDesc(child, output, level + 1);
            }
        }
    }

    public Class<? extends QueryNodeProcessorPipeline> getOriginalProcessorClass() {
        return originalProcessorClass;
    }
}
