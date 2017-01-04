/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.lucene.analysis.core;

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 *
 * @author jluker
 * 
 * Checks if the token is an acronym (is present in the HashMap) and 
 * adds the full name after the term (depends on the value of emitBoth)
 */
public final class AcronymTokenFilter extends TokenFilter {
  public static final int ACRONYM_MIN_LENGTH = 2;
  public static final float ACRONYM_UPPER_MIN_RATIO = 0.8f;

  // controls index-time vs. query-time behavior
  private boolean emitBoth;

  private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
  private State currentState = null;
	private String prefix;
	private String tokenType;

  public AcronymTokenFilter(TokenStream in, boolean emitBoth, String prefix, String tokenType) {
    super(in);
    this.emitBoth = emitBoth;
    this.prefix = prefix;
    this.tokenType = tokenType;
  }

  @Override
  public boolean incrementToken() throws IOException {
    // check if we got an acronym
    if (this.currentState != null) {
      restoreState(currentState);
      if (this.tokenType != null) {
      	typeAtt.setType(this.tokenType);
      }
      posIncrAtt.setPositionIncrement(0);
      currentState=null;
      return true;
    }

    if (!input.incrementToken()) {                           
      return false;
    }

    String origTerm = termAtt.toString();
    if (termIsAcronym(origTerm)) {
    	if (prefix != null) {
    		termAtt.setEmpty().append(prefix+origTerm);
    	}

      if (!emitBoth) {
      	if (this.tokenType != null) {
      		typeAtt.setType(this.tokenType);
      	}
        return true;
      }
      currentState = captureState();
      termAtt.setEmpty().append(origTerm);

    }

    return true;
  }

  /**
   * Checks that the string is considered a valid acronoym. Usually all letters
   * must be UPPERCASE (there is a minimum ration)
   * 
   * @param term
   * @return true when the term has only UPPERCASE and digits
   */
  public static boolean termIsAcronym(String term) {
    if (term.length() < ACRONYM_MIN_LENGTH ) {
      return false;
    }
    int u = 0;
    int d = 0;
    int l = term.length();

    for (char c: term.toCharArray()) {
      if (Character.isUpperCase(c)) {
        u++;
      }
      else if (Character.isDigit(c)) {
        d++;
      }
    }
    if (d==l) {
      return false;
    }
    
    if (u+d < ACRONYM_MIN_LENGTH)
      return false;
    
    if ( ((float) u+d / term.length()) < ACRONYM_UPPER_MIN_RATIO)
      return false;
    
    return true;
  }

  public void reset() throws IOException {
    super.reset();
    currentState=null;
  }

}
