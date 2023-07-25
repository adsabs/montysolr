package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;
import java.util.Set;

import org.antlr.runtime.CharStream;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.parser.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;

/**
 * A generic class that is used by other query processors, eg.
 * {@link AqpQNORMALProcessor}
 * 
 * 
 * 
 */
public class AqpQProcessor extends QueryNodeProcessorImpl implements
    QueryNodeProcessor {

  public String defaultField = null;
  
  
  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    if (node instanceof AqpANTLRNode) {
      AqpANTLRNode n = (AqpANTLRNode) node;
      if (nodeIsWanted(n)) {
        return createQNode(n);
      }
    }
    return node;
  }

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
    return node;
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
    return children;
  }

  public boolean nodeIsWanted(AqpANTLRNode node) {
    throw new UnsupportedOperationException();
  }

  public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
    throw new UnsupportedOperationException();
  }

  public String getDefaultFieldName() throws QueryNodeException {
    QueryConfigHandler queryConfig = getQueryConfigHandler();

    String defaultField = null;
    if (queryConfig != null) {

      if (queryConfig
          .has(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD)) {
        defaultField = queryConfig
            .get(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD);
      }
    }
    return defaultField;
  }

  public AqpFeedback getFeedbackAttr() throws QueryNodeException {
    QueryConfigHandler config = getQueryConfigHandler();
    if (config.has(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK)) {
      return config
          .get(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK);
    } else {
      throw new QueryNodeException(new MessageImpl(
          QueryParserMessages.NODE_ACTION_NOT_SUPPORTED,
          "Configuration error, missing AqpFeedback.class in the config!"));
    }
  }

  public static QueryNode getTerminalNode(QueryNode node) {
    while (!node.isLeaf()) {
      return getTerminalNode(node.getChildren().get(
          node.getChildren().size() - 1));
    }
    return node;
  }

  public static OriginalInput getOriginalInput(AqpANTLRNode node, Set<String> labelsToIncreaseCount)
      throws ParseException {

    CharStream inputStream = getInputStream(node);
    if (inputStream == null) {
      throw new ParseException(new MessageImpl(
          "The supplied tree doesn't have input stream"));
    }
    int[] startIndex = new int[] { inputStream.size() };
    getTheLowestIndex(startIndex, node);
    if (startIndex[0] == -1) {
      throw new ParseException(new MessageImpl(
          "We cannot find where the input starts"));
    }
    int[] lastIndex = new int[] { startIndex[0] };
    getTheHighestIndex(lastIndex, node, labelsToIncreaseCount);
    if (lastIndex[0] < startIndex[0]) {
      throw new ParseException(new MessageImpl(
          "We cannot find where the input ends"));
    }
    
    return new OriginalInput(
        inputStream.substring(startIndex[0], lastIndex[0]), startIndex[0],
        lastIndex[0]);
  }

  

	public static CharStream getInputStream(QueryNode node) {
    if (node.isLeaf()) {
      if (node instanceof AqpANTLRNode) {
        if (((AqpANTLRNode) node).getTree().getToken().getInputStream() != null) {
          return ((AqpANTLRNode) node).getTree().getToken().getInputStream();
        }
      }
    } else {
      for (QueryNode child : node.getChildren()) {
        CharStream r = getInputStream(child);
        if (r != null) {
          return r;
        }
      }
    }
    return null;
  }

  private static void getTheHighestIndex(int[] i, QueryNode node, Set<String> labelsToIncreaseCount) {
    if (!node.isLeaf()) {
      for (QueryNode n : node.getChildren()) {
        getTheHighestIndex(i, n, labelsToIncreaseCount);
      }
    }
    if (node instanceof AqpANTLRNode) {
      int si = ((AqpANTLRNode) node).getInputTokenEnd();
      /*
       * In the grammar, we are generating AST tree, but leaving out
       * brackets, because they were not deemed interesting to see - 
       * but this means we don't know their position, so if the node
       * contained the QRANGE or something similar, we'll see the 
       * input shortened by n x 1
       */
      if (labelsToIncreaseCount != null) {
      	String label = ((AqpANTLRNode) node).getTokenLabel();
      	if (labelsToIncreaseCount.contains(label))
      		i[0] =  i[0]+1;
      }
      
      if (i[0] < si) {
        i[0] = si;
      }
    }
  }

  private static void getTheLowestIndex(int[] i, QueryNode node) {
  	if (node instanceof AqpANTLRNode) {
      int si = ((AqpANTLRNode) node).getInputTokenStart();
      if (si > -1 && si < i[0]) {
        i[0] = si;
      }
    }
    if (!node.isLeaf()) {
      for (QueryNode n : node.getChildren()) {
        getTheLowestIndex(i, n);
      }
    }
  }

  public static class OriginalInput {
    public String value;
    public int start;
    public int end;

    public OriginalInput(String value, int startIndex, int endIndex) {
      this.value = value;
      this.start = startIndex;
      this.end = endIndex;
    }

    public String toString() {
      return String.format("%s <%d:%d>", this.value, this.start, this.end);
    }
  }
}
