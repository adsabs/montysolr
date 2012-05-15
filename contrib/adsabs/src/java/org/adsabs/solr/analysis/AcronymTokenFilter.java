/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.adsabs.solr.analysis;

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.CharacterUtils;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jluker
 * 
 * Checks if the token is an acronym (is present in the HashMap) and 
 * adds the full name after the term (depends on the value of emitBoth)
 */
public class AcronymTokenFilter extends TokenFilter {
    public static final Logger log = LoggerFactory.getLogger(AcronymTokenFilter.class);
    public static final String TOKEN_TYPE_ACRONYM = "ACRONYM";
    public static final int ACRONYM_MIN_LENGTH = 2;

    // stores our disocvered acronyms
    private StringBuffer acronym;

    // controls index-time vs. query-time behavior
    private boolean emitBoth;
    private boolean lowercaseAcronyms;

    private AttributeSource.State current = null;
    private final CharacterUtils charUtils;
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

    public AcronymTokenFilter(TokenStream in, boolean emitBoth, boolean lowercaseAcronyms) {
        super(in);
        this.charUtils = CharacterUtils.getInstance(Version.LUCENE_30);
        this.acronym = new StringBuffer();
        this.emitBoth = emitBoth;
        this.lowercaseAcronyms = lowercaseAcronyms;
    }

    @Override
    public boolean incrementToken() throws IOException {
        // check if we got an acronym
        if (this.acronym.length() > 0) {
            createToken(this.acronym.toString(), this.current);
            acronym.setLength(0);
            return true;
        }

        if (!input.incrementToken()) {                           
            return false;
        }

        String origTerm = termAtt.toString();
        boolean termIsAcronym = termIsAcronym(origTerm);
        typeAtt.setType(TypeAttribute.DEFAULT_TYPE);

        final char[] buffer = termAtt.buffer();
        if (lowercaseAcronyms || !termIsAcronym) {
            final int length = termAtt.length();
            for (int i = 0; i < length;) {
                i += Character.toChars(
                    Character.toLowerCase(
                        charUtils.codePointAt(buffer, i)), buffer, i);
            }
        } else if (termIsAcronym) {
            typeAtt.setType(TOKEN_TYPE_ACRONYM);
        }

        if (termIsAcronym && emitBoth) {                              
            current = captureState();                            
            this.acronym.append(origTerm);
        }

        return true;
    }

    protected boolean createToken(String acronym, AttributeSource.State current) {
        restoreState(current);
        termAtt.setEmpty().append(acronym);
        typeAtt.setType(TOKEN_TYPE_ACRONYM);
        posIncrAtt.setPositionIncrement(0);
        return true;
  }
    private boolean termIsAcronym(String term) {
        if (term.length() < ACRONYM_MIN_LENGTH || !term.equals(term.toUpperCase())) {
            return false;
        }
        return true;
    }

}
