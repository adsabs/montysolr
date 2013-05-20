package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * Converts QRANGEIN node into @{link {@link ParametricQueryNode}. The field
 * value is the @{link DefaultFieldAttribute} specified in the configuration.
 * 
 * Because QRANGE nodes have this shape:
 * 
 * <pre>
 *                      QRANGE
 *                      /    \
 *                 QNORMAL  QPHRASE
 *                   /          \
 *                 some       "phrase"
 * </pre>
 * 
 * It is important to queue {@AqpQRANGEEProcessor} and
 * {@AqpQRANGEINProcessor} <b>before</b> processors that
 * transform QNORMAL, QPHRASE and other Q nodes <br/>
 * 
 * If the user specified a field, it will be set by the @{link
 * AqpFIELDProcessor} Therefore this processor should queue before @{link
 * AqpFIELDProcessor}.
 * 
 * 
 * @see QueryConfigHandler
 * @see DefaultFieldAttribute
 * @see AqpQRANGEEXProcessor
 * @see AqpQueryNodeProcessorPipeline
 * 
 */
public class AqpQRANGEINProcessor extends AqpQProcessor {

  protected boolean lowerInclusive = true;
  protected boolean upperInclusive = true;

  public boolean nodeIsWanted(AqpANTLRNode node) {
    if (node.getTokenLabel().equals("QRANGEIN")) {
      return true;
    }
    return false;
  }

  public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
    String field = getDefaultFieldName();

    AqpANTLRNode lowerNode = (AqpANTLRNode) node.getChildren().get(0);
    AqpANTLRNode upperNode = (AqpANTLRNode) node.getChildren().get(1);

    NodeData lower = getTokenInput(lowerNode);
    NodeData upper = getTokenInput(upperNode);

    FieldQueryNode lowerBound = new FieldQueryNode(field,
        EscapeQuerySyntaxImpl.discardEscapeChar(lower.value), lower.start,
        lower.end);
    FieldQueryNode upperBound = new FieldQueryNode(field,
        EscapeQuerySyntaxImpl.discardEscapeChar(upper.value), upper.start,
        upper.end);

    return new TermRangeQueryNode(lowerBound, upperBound, lowerInclusive,
        upperInclusive);

  }

  public NodeData getTokenInput(AqpANTLRNode node) {
    String label = node.getTokenLabel();
    AqpANTLRNode subNode = (AqpANTLRNode) node.getChildren().get(0);

    if (label.equals("QANYTHING")) {
      return new NodeData("*", subNode.getTokenStart(), subNode.getTokenEnd());
    } else if (label.contains("PHRASE")) {
      return new NodeData(subNode.getTokenInput().substring(1,
          subNode.getTokenInput().length() - 1), subNode.getTokenStart() + 1,
          subNode.getTokenEnd() - 1);
    } else {
      return new NodeData(subNode.getTokenInput(), subNode.getTokenStart(),
          subNode.getTokenEnd());
    }
  }

  class NodeData {
    public String value;
    public int start;
    public int end;

    NodeData(String value, int start, int end) {
      this.value = value;
      this.start = start;
      this.end = end;
    }
  }

}
