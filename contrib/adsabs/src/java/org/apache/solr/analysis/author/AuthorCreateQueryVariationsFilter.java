package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
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

  public AuthorCreateQueryVariationsFilter(TokenStream input, String tokenType, 
      boolean surname, boolean variate, boolean addWildcards, boolean shortenMultiname) {
    
    super(input);
    this.termAtt = addAttribute(CharTermAttribute.class);
    this.posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    this.variationStack = new Stack<String>();
    this.typeAtt = addAttribute(TypeAttribute.class);
    this.createVariations = variate;
    this.plainSurname = surname;
    this.tokenType = tokenType;
    this.addWildcards = addWildcards;
    this.shortenMultiname = shortenMultiname;
  }

  private Stack<String> variationStack;
  private AttributeSource.State current;

  private final CharTermAttribute termAtt;
  private final PositionIncrementAttribute posIncrAtt;
  private final TypeAttribute typeAtt;
  private int maxNumberOfNames = 6; // safety precaution, we are pretty efficient but one should be careful...
  private boolean plainSurname;
  private boolean createVariations;
  private boolean addWildcards;
  private boolean shortenMultiname;

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

    if ((tokenType==null || typeAtt.type().equals(tokenType)) && this.genVariations()) {
      this.current = this.captureState();
    }

    return true;
  }

  private boolean genVariations() {
    String authorName = termAtt.toString();

    if (!authorName.contains(",")) return false;
    
    if (authorName.endsWith(",") && addWildcards) {
      variationStack.push(authorName + "*");
      return true;
    }
    
    String[] parts = authorName.split(" ", maxNumberOfNames );
    
    boolean lastPartWasAcronym = parts[parts.length-1].length() == 1;
    
    if (createVariations) {
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
