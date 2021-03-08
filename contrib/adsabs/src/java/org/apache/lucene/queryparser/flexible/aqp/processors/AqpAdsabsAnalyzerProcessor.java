package org.apache.lucene.queryparser.flexible.aqp.processors;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAnalyzerQueryNodeProcessor;

/**
 * This processor prevents analysis to happen for nodes that are 
 * inside certain node types. This is needed especially for the 
 * functional queries where we do not know how the values are to 
 * be processed and the function itself should decide.
 * 
 * @author rchyla
 *
 */
public class AqpAdsabsAnalyzerProcessor extends AqpAnalyzerQueryNodeProcessor {

	public final static String ORIGINAL_VALUE = "ORIGINAL_ANALYZED_VALUE";
  public final static String ANALYZED = "already_analyzed";
  
  private boolean enteredCleanZone = false;
  private int counter = 0;
  

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    if (enteredCleanZone == true) {
      counter++;
    }
    else if (node instanceof AqpNonAnalyzedQueryNode || node.getTag(ANALYZED) != null) {
      enteredCleanZone = true;
      counter++;
    }
    else if (node instanceof AqpFunctionQueryNode && ((AqpFunctionQueryNode) node).canBeAnalyzed() == false) {
      enteredCleanZone = true;
      counter++;
    }

    return super.preProcessNode(node);
  }

  @Override
  protected QueryNode postProcessNode(QueryNode node)
  throws QueryNodeException {
    if (enteredCleanZone == true) {
      counter--;
      if (counter == 0) {
        enteredCleanZone = false;
      }
      return node;
    }


    String fv = null;
    int maxValueLength = Integer.min(
        Integer.valueOf(getConfigVal("aqp.maxPhraseLength", "150")),
        Integer.valueOf(getConfigVal("aqp.maxAbsolutePhraseLength", "500")));          ;
    
    if (node instanceof FieldQueryNode) {
      fv = ((FieldQueryNode) node).getTextAsString();
      if (fv.length() >= maxValueLength) {
        String fName = getConfigVal("aqp.longPhraseFunction", "similar");
        
        QueryConfigHandler config = getQueryConfigHandler();
        
        if (!config.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)) {
          throw new QueryNodeException(new MessageImpl(
              "Invalid configuration",
              "Missing FunctionQueryBuilder provider"));
        }
        
        AqpFunctionQueryBuilder builder = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)
                        .getBuilder(fName, (QueryNode) node, config);
        
        if (builder == null) {
          throw new QueryNodeException(new MessageImpl(QueryParserMessages.INVALID_SYNTAX,
              "Unknown function \"" + fName + "\"" ));
        }
        
        List<OriginalInput> values = new ArrayList<OriginalInput>();
        values.add(new OriginalInput(fv, ((FieldQueryNode) node).getBegin(), ((FieldQueryNode) node).getEnd()));
        // input abstract, 30, 0, 1, 1, 0.5)
        values.add(new OriginalInput("input " + ((FieldQueryNode) node).getFieldAsString(), -1, -1));
        values.add(new OriginalInput("30", -1, -1));
        values.add(new OriginalInput("0", -1, -1));
        values.add(new OriginalInput("1", -1, -1));
        values.add(new OriginalInput("1", -1, -1));
        values.add(new OriginalInput("0.5", -1, -1));
        
        return new AqpFunctionQueryNode(fName, builder, values);
      }
    }
    QueryNode rn = super.postProcessNode(node);
    if (rn != node || node instanceof FieldQueryNode 
        //&& !((FieldQueryNode)node).getTextAsString().equals(fv)
        ) {
      rn.setTag(ORIGINAL_VALUE, fv);
      rn.setTag(ANALYZED, true);
      //return new AqpAnalyzedQueryNode(rn);
    }
    
    return rn;

  }
  protected static Map<String, String> empty = new HashMap<String, String>();
  public Map<String, String> getConfigMap() {
    Map<String, String> args = getQueryConfigHandler().get(
        AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER);
    if (args == null)
      return empty;
    return args;
  }
  
  public String getConfigVal(String key) {
    Map<String, String> args = getConfigMap();
    if (args.containsKey(key)) {
      return args.get(key);
    }
    return null;
  }
  
  public String getConfigVal(String key, String defaultVal) {
    Map<String, String> args = getConfigMap();
    if (args.containsKey(key)) {
      return args.get(key);
    }
    return defaultVal;
  }
  
  
}
