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
    public static final String TOKEN_TYPE_ACRONYM = "ACRONYM";
    public static final int ACRONYM_MIN_LENGTH = 2;
    public static final String ACRONYM_PREFIX = "acr::";
    
    // controls index-time vs. query-time behavior
    private boolean emitBoth;

    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private State currentState = null;

    public AcronymTokenFilter(TokenStream in, boolean emitBoth, boolean lowercaseAcronyms) {
        super(in);
        this.emitBoth = emitBoth;
    }

    @Override
    public boolean incrementToken() throws IOException {
        // check if we got an acronym
        if (this.currentState != null) {
          restoreState(currentState);
          typeAtt.setType(TOKEN_TYPE_ACRONYM);
          posIncrAtt.setPositionIncrement(0);
          currentState=null;
          return true;
        }

        if (!input.incrementToken()) {                           
            return false;
        }

        String origTerm = termAtt.toString();
        if (termIsAcronym(origTerm)) {
          termAtt.setEmpty().append(ACRONYM_PREFIX+origTerm);
          
          if (!emitBoth) {
            typeAtt.setType(TOKEN_TYPE_ACRONYM);
            return true;
          }
          currentState = captureState();
          termAtt.setEmpty().append(origTerm);
           
        }

        return true;
    }

    private boolean termIsAcronym(String term) {
        if (term.length() < ACRONYM_MIN_LENGTH || !term.equals(term.toUpperCase())) {
            return false;
        }
        return true;
    }
    
    public void reset() throws IOException {
      super.reset();
      currentState=null;
    }

}
