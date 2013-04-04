package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TextableQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.solr.analysis.author.AuthorUtils;

/**
 * Looks at the QueryNode(s) and if they are author searches,
 * it adds to them some ADS specific post-analysis logic
 * 
 * @see AqpFieldMapper
 * @see QueryConfigHandler
 * @author rchyla
 * 
 */
public class AqpAdsabsExpandAuthorSearchProcessor extends QueryNodeProcessorImpl {

  private Map<String, int[]> fields;

  public AqpAdsabsExpandAuthorSearchProcessor() {
    // empty constructor
  }

  @Override
  public QueryNode process(QueryNode queryTree) throws QueryNodeException {
    if (getQueryConfigHandler().has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS)) {
      fields = getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS);
      return super.process(queryTree);
    }
    return queryTree;
  }

  @Override
  protected QueryNode preProcessNode(QueryNode node)
    throws QueryNodeException {
    return node;
  }

  @Override
  protected QueryNode postProcessNode(QueryNode node)
    throws QueryNodeException {
    
    if (node.getTag(AqpAdsabsAnalyzerProcessor.ORIGINAL_VALUE) != null) {
      String origValue = (String) node.getTag(AqpAdsabsAnalyzerProcessor.ORIGINAL_VALUE);
      String normalized = AuthorUtils.normalizeAuthor(origValue);
      NameInfo nameInfo = new NameInfo(normalized);
      int[] level = new int[]{0}; //ugly, ugly
      return expandNodes(node, nameInfo, level);
    }
    return node;
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
  throws QueryNodeException {
    return children;
  }
  
  private QueryNode expandNodes(QueryNode node, NameInfo origNameInfo, int[] level) throws QueryNodeException {
    
    ArrayList<QueryNode> pl = new ArrayList<QueryNode>();
    
    if (!node.isLeaf()) {
      List<QueryNode> children = node.getChildren();
      for (int i=0;i<children.size();i++) {
        doExpansion(origNameInfo, children.get(i), pl, level);
        children.addAll(i+1, pl);
        i += pl.size();
        pl.clear();
      }
      //children.addAll(pl);
    }
    else {
      // now expand the parent
      doExpansion(origNameInfo, node, pl, level);
    }
    

    if (pl.size()>0) {
      pl.add(0, node);
      return new GroupQueryNode(new BooleanQueryNode(pl));
    }
    
    return node;
  }
  
  private void doExpansion(NameInfo origNameInfo, QueryNode node, List<QueryNode> parentChildren, int[] level) 
  throws QueryNodeException {
    
    if (node instanceof TextableQueryNode ) {
      
      level[0] = level[0]+1; // marker to tell us not to expand synonyms any more
      
      if (node instanceof FuzzyQueryNode || node instanceof RegexpQueryNode 
          || node instanceof WildcardQueryNode) {
        return;
      }
      
      
      FieldQueryNode fqn = ((FieldQueryNode) node);
      if (fields.containsKey(fqn.getFieldAsString())) {
        
        // 'name upgrade'
        if (level[0] == 1 && !isLongForm(origNameInfo.origName)) {
          try {
            String[] synonyms = getSynonyms(origNameInfo.origName);
            if (synonyms != null) {
              for (String syn: synonyms) {
                parentChildren.add(new FieldQueryNode(fqn.getField(), syn, fqn.getBegin(), fqn.getEnd()));
              }
            }
          } catch (IOException e) {
            throw new QueryNodeException(new MessageImpl("Wonky, wonky, bong, bong - synonym expansion failed..." + e.getMessage()));
          }
        }
        
        String v = fqn.getTextAsString();
        String[] nameParts = fqn.getTextAsString().split(" ");
        
        /*
        if (node instanceof WildcardQueryNode) { // only "kurtz, m*" cases are tolerated
          if (nameParts[nameParts.length-1].length() > 1) return;
          nameParts[nameParts.length-1] = nameParts[nameParts.length-1].replace("*", "").trim();
        }
        */
        
        if (nameParts.length == 1) { // the new name is just surname
          
          if (nameParts.length < origNameInfo.noOfParts ) return; // do nothing
          
          if (origNameInfo.containsOnlySurname) { // orig was lone surname
            parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + "*", fqn.getBegin(), fqn.getEnd()));
          }
          else {
            // do nothing
          }
        }
        else { // new name has several parts
          if (nameParts.length < origNameInfo.noOfParts ) return; // do nothing
          
          if (origNameInfo.containsOnlySurname) { // orig was lone surname
            // we could extract the surname and search for "surname, *" but i have decided against it
            // the surname probably comes from the synonym expansion and if it was there, it can contain initials
            parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + " *", fqn.getBegin(), fqn.getEnd()));
          }
          else {
            if (origNameInfo.lastPartWasAcronym) { // orig name had only initial at the end
              if (nameParts[nameParts.length-1].length() == 1) { // allow broader search only if the expanded form also has initial
                parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + "*", fqn.getBegin(), fqn.getEnd()));
              }
              else {
                parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + " *", fqn.getBegin(), fqn.getEnd()));
              }
            }
            else {
              parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + " *", fqn.getBegin(), fqn.getEnd()));
            }
          }
          
          // special regular expression cases, only happens if the new name and the original 
          // have initial somewhere in the middle (and both at the same position)
          if (regexIsPossible(nameParts, origNameInfo.parts)) {
            StringBuffer nn = new StringBuffer();
            nn.append(nameParts[0]);
            for (int i=1;i<nameParts.length-1;i++) {
              if (nameParts[i].length()==1 && origNameInfo.parts[i].length()==1) {
                nn.append(" " + nameParts[i] + "[^\\s]+");
              }
              else {
                nn.append(" " + nameParts[i]);
              }
            }
            nn.append(" " + nameParts[nameParts.length-1]);
            parentChildren.add(new AqpAdsabsRegexQueryNode(fqn.getField(), nn.toString(), fqn.getBegin(), fqn.getEnd()));
            parentChildren.add(new AqpAdsabsRegexQueryNode(fqn.getField(), nn.toString() + " .*", fqn.getBegin(), fqn.getEnd()));
          }
          
        }
        
        return;
      }
    }
    
    if (!node.isLeaf()) expandNodes(node, origNameInfo, level);
  }
  
  private boolean regexIsPossible(String[] orig, String[] newName) {
    for (int i=1;i<orig.length-1 && i<newName.length;i++) {
      if (orig[i].length()==1 && newName[i].length()==1 && i+1<newName.length) {
        return true;
      }
    }
    return false;
  }
  
  private String[] getSynonyms(String origInput) throws IOException {
    Analyzer analyzer = getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);
    TokenStream source;
    try {
      source = analyzer.tokenStream("author_short_name_rage", new StringReader(origInput));
      source.reset();
    } catch (IOException e1) {
      throw new RuntimeException(e1);
    }
    
    CharTermAttribute termAtt = source.getAttribute(CharTermAttribute.class);
    
    List<String> synonyms = new ArrayList<String>();
    while (source.incrementToken()) {
      synonyms.add(termAtt.toString());
    }
    
    if (synonyms.size()<2) { // the first one is the original
      return null;
    }
    synonyms.remove(0);
    return synonyms.toArray(new String[synonyms.size()]);
  }
  
  private boolean isLongForm(String name) {
    String[] parts = name.split(" ");
    int longParts = 0;
    for (int i=1;i<parts.length;i++) {
      if (parts[i].length() > 1)
        longParts++;
    }
    return parts.length==longParts;
  }
  
  class NameInfo {
    public String origName;
    public boolean lastPartWasAcronym;
    public int noOfParts;
    public String[] parts;
    public boolean containsOnlySurname = false;
    
    public NameInfo(String name) {
      // lone surnames get always expanded
      if (name.endsWith(",") || !name.contains(",")) containsOnlySurname = true;
      
      // whether to add a space, ie. Kurtz, Michael J -> Kurtz, Michael J*
      // but Kurtz, Michael Julian -> Kurtz, Michael Julian *
      parts = name.split(" ");
      lastPartWasAcronym = parts[parts.length-1].length() == 1;
      
      noOfParts = parts.length;
      origName = name;
    }
  }

}
