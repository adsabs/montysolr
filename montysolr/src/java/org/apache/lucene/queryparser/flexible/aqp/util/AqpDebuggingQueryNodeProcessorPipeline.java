package org.apache.lucene.queryparser.flexible.aqp.util;

import java.util.Iterator;
import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;

/**
 *  This class is used for debugging purposes (eg. from unittests
 *  or when the query parser was invoked with debuqQuery=true)
 *  
 *  The debugging output shows the stage of the AST tree after
 *  each processing stage completed. Including the changes in 
 *  the internal 'map'.
 * 
 */
public class AqpDebuggingQueryNodeProcessorPipeline extends
    QueryNodeProcessorPipeline {

  EscapeQuerySyntax escaper = new EscapeQuerySyntaxImpl();
	private Class<? extends QueryNodeProcessorPipeline> originalProcessorClass;

  public AqpDebuggingQueryNodeProcessorPipeline(QueryConfigHandler queryConfig, 
  		Class<? extends QueryNodeProcessorPipeline> originalClass) {
    super(queryConfig);
    originalProcessorClass = originalClass;
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
    System.out.println( this.getClass().toGenericString());
    System.out.println("     0. starting");
    System.out.println("--------------------------------------------");
    System.out.println(oldVal);

    Iterator<QueryNodeProcessor> it = this.iterator();

    QueryNodeProcessor processor;
    while (it.hasNext()) {
      processor = it.next();

      System.out.println("     " + i + ". step "
          + processor.getClass().toString());
      queryTree = processor.process(queryTree);
      newVal = queryTree.toString();
      newMap = harvestTagMap(queryTree);
      System.out.println("     Tree changed: "
          + (newVal.equals(oldVal) ? "NO" : "YES"));

      if (!newMap.equals(oldMap)) {
        System.out.println("     Tags changed: YES");
        System.out.println("     -----------------");
        System.out.println(newMap);
        System.out.println("     -----------------");
      }
      System.out.println(newVal.equals(oldVal) ? (newMap.equals(oldMap) ? "" : newVal) : newVal);

      System.out.println("--------------------------------------------");


      oldVal = newVal;
      oldMap = newMap;
      i += 1;
    }

    System.out.println("");
    System.out.println("final result:");
    System.out.println("--------------------------------------------");
    System.out.println(queryTree.toString());
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
        output.append("");
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
