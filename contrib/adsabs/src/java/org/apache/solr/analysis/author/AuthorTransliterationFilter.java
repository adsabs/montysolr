package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates different spellings and variations of the
 * author names as they are indexed (it was initially called
 * AuthorAutoSynonymFilter)
 */

public final class AuthorTransliterationFilter extends TokenFilter {

  public static final Logger log = LoggerFactory.getLogger(AuthorTransliterationFilter.class);
  private String tokenType;

  public AuthorTransliterationFilter(TokenStream input, String tokenType) {
    super(input);
    this.termAtt = addAttribute(CharTermAttribute.class);
    this.posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    this.transliterationStack = null;
    this.typeAtt = addAttribute(TypeAttribute.class);
    this.tokenType = tokenType;
    this.N = 0;
  }

  private int N;
  private List<String> transliterationStack;
  private AttributeSource.State current;

  private final CharTermAttribute termAtt;
  private final PositionIncrementAttribute posIncrAtt;
  private final TypeAttribute typeAtt;
  

  /* (non-Javadoc)
   * @see org.apache.lucene.analysis.TokenStream#incrementToken()
   */
  @Override
  public boolean incrementToken() throws IOException {

    if (this.N > 0) {
      String syn = this.transliterationStack.get(--N);
      this.restoreState(this.current);
      this.termAtt.setEmpty();
      this.termAtt.append(syn);
      this.posIncrAtt.setPositionIncrement(0);
      this.typeAtt.setType(AuthorUtils.AUTHOR_TRANSLITERATED);
      return true;
    }

    if (!input.incrementToken()) return false;

    if (tokenType == null && this.genVariants()) { // null means process all tokens
      this.current = captureState();
    }
    else if (typeAtt.type().equals(tokenType) && this.genVariants()) {
      this.current = this.captureState();
    }

    return true;
  }

  private boolean genVariants() {
    //log.debug("generating name variants for: " + authorName);
    ArrayList<String> synonyms = AuthorUtils.getAsciiTransliteratedVariants(termAtt.toString());
    if (synonyms != null && synonyms.size() > 0) {
      //log.debug("variants: " + synonyms);
      transliterationStack = synonyms;
      N = synonyms.size();
      return true;
    }

    return false;
  }

  @Override
  public void reset() throws IOException {
    super.reset();
    transliterationStack = null;
    N = 0;
    current = null;
  }
}
