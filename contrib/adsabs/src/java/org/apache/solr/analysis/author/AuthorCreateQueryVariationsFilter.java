package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;

/*
 * Inspect the query form and decide whether we should generate the ADS
 * fail-safe query additions. If the author search is:
 * 
 * "Kurtz, Michael"
 * 
 * then the old records can be simply under
 * 
 * "Kurtz, M"
 * "Kurtz,"
 * 
 * This filter expects NORMALIZED form of the author name. 
 * 
 */
public final class AuthorCreateQueryVariationsFilter extends TokenFilter {

  private final String tokenType;
  
  private final PayloadAttribute payloadAtt = addAttribute(PayloadAttribute.class);
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
  private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

  private boolean lookAtPayloadForOrigAuthor;
  private int maxNumberOfNames = 6; // safety precaution, we are pretty efficient but one should be careful...
  private boolean plainSurname;
  private int createVariations;
  private boolean addWildcards;
  private boolean shortenMultiname;
  
  public AuthorCreateQueryVariationsFilter(TokenStream input, String tokenType, 
      boolean surname, int variate, boolean addWildcards, 
      boolean shortenMultiname, boolean lookAtPayloadForOrigAuthor) {
    
    super(input);
    this.variationStack = new Stack<String>();
    this.createVariations = variate;
    this.plainSurname = surname;
    this.tokenType = tokenType;
    this.addWildcards = addWildcards;
    this.shortenMultiname = shortenMultiname;
    this.lookAtPayloadForOrigAuthor = lookAtPayloadForOrigAuthor;
  }

  private Stack<String> variationStack;
  private AttributeSource.State current;
  private String origAuthorName = null;
  

  @Override
  public boolean incrementToken() throws IOException {
    
    if (this.variationStack.size() > 0) {
      String syn = this.variationStack.remove(0);
      this.restoreState(this.current);
      this.termAtt.setEmpty();
      this.termAtt.append(syn);
      this.posIncrAtt.setPositionIncrement(0);
      this.typeAtt.setType(AuthorUtils.AUTHOR_QUERY_VARIANT);
      return true;
    }
    
    if (!input.incrementToken()) return false;
    
    // sort of hack, we want to know what the original input was
    // but it can't be done otherwise because the SynonynFilter
    // is resetting all attributes!!! This will work only for the
    // first author in the token list, but since we use the filter
    // only for the query, it should be fine
    if (origAuthorName==null) origAuthorName=termAtt.toString();

    if ((tokenType==null || typeAtt.type().equals(tokenType)) && this.genVariations()) {
      this.current = this.captureState();
    }

    return true;
  }

  private boolean genVariations() {
    String authorName = termAtt.toString();
    
    if (lookAtPayloadForOrigAuthor && payloadAtt.getPayload() != null) {
      origAuthorName = payloadAtt.getPayload().utf8ToString();
    }

    if (!authorName.contains(",")) return false;
    
    if (authorName.endsWith(",") && addWildcards) {
      variationStack.push(authorName + " *");
      return true;
    }
    
    String[] parts = authorName.split(" ", maxNumberOfNames );
    String[] origParts = origAuthorName.split(" ", maxNumberOfNames );
    
    // this is an important indicator that influences how the wildcard variant
    // is generated (if there is only acronym in the input, we do prefix search,
    // if there is more than 2 characters, we append a space - ie. "kurtz, mi *"
    // as opposed to "kurtz, m*"
    boolean lastPartWasAcronym = origParts[origParts.length-1].length() == 1;
    
    if (createVariations > 0 && parts.length >= createVariations) {
      List<Integer> ids = new ArrayList<Integer>();
      for (int i=1;i<parts.length;i++) {
        if (parts[i].length()>1) ids.add(i);
      }
      
      StringBuilder output = new StringBuilder();
      
      if (ids.size()>0) { // number of to-be-shortened name parts
        int[] idx = new int[ids.size()];
        for (int i=0;i<idx.length;i++) {
          idx[i] = ids.get(i);
        }
        List<int[]> combinations = comb(idx);
        for (int[] comb: combinations) {
          String[] newParts = Arrays.copyOf(parts, parts.length);
          for (int x: comb) {
            newParts[x] = parts[x].substring(0,1);
          }
          output = new StringBuilder();
          boolean notFirst = false;
          for (int i=0;i<newParts.length;i++) {
            if (notFirst) output.append(" ");
            output.append(newParts[i]);
            notFirst=true;
          }
          if (lastPartWasAcronym) {
            if (addWildcards) {
              variationStack.push(output.toString() + "*"); // prefix search
            }
            else {
              variationStack.push(output.toString());
            }
          }
          else {
            if (addWildcards) {
              variationStack.push(output.toString());
              variationStack.push(output.toString() + " *");
            }
            else {
              variationStack.push(output.toString());
            }
          }
        }
      }
      else {
        if (addWildcards) {
          variationStack.push(authorName + (lastPartWasAcronym ? "*" : " *"));
        }
      }
    }
    
    if (shortenMultiname && parts.length > 2) {
      variationStack.push(parts[0] + " " + parts[1]);
      if (parts[1].length()>1) {
        variationStack.push(parts[0] + " " + parts[1].substring(0,1));
      }
    }
    
    if (plainSurname) variationStack.push(parts[0]);
    
    return variationStack.size() > 0;
    
  }


  @Override
  public void reset() throws IOException {
    super.reset();
    variationStack.clear();
    current = null;
    origAuthorName=null;
  }

  private List<int[]> comb(int... items) {
    ArrayList<int[]> comb = new ArrayList<int[]>();
    Arrays.sort(items);
    for (int k = 1; k <= items.length; k++) {
      kcomb(items, 0, k, new int[k], comb);
    }
    return comb;
  }
  public void kcomb(int[] items, int n, int k, int[] arr, List<int[]> comb) {
    if (k == 0) {
      comb.add(Arrays.copyOf(arr, arr.length));
    } else {
      for (int i = n; i <= items.length - k; i++) {
        arr[arr.length - k] = items[i];
        kcomb(items, i + 1, k - 1, arr, comb);
      }
    }
  }
}
