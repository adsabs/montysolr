/**
 * 
 */
package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.ArrayList;
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
 * @author jluker
 * 
 * This class creates different spellings and variations of the
 * author names as they are indexed (it was initially called
 * AuthorAutoSynonymFilter)
 *
 */
public final class AuthorTransliterationFilter extends TokenFilter {

  public static final Logger log = LoggerFactory.getLogger(AuthorTransliterationFilter.class);

  public AuthorTransliterationFilter(TokenStream input) {
    super(input);
    this.termAtt = addAttribute(CharTermAttribute.class);
    this.posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    this.transliterationStack = new Stack<String>();
    this.typeAtt = addAttribute(TypeAttribute.class);
  }

  private Stack<String> transliterationStack;
  private AttributeSource.State current;

  private final CharTermAttribute termAtt;
  private final PositionIncrementAttribute posIncrAtt;
  private final TypeAttribute typeAtt;

  /* (non-Javadoc)
   * @see org.apache.lucene.analysis.TokenStream#incrementToken()
   */
  @Override
  public boolean incrementToken() throws IOException {

    if (this.transliterationStack.size() > 0) {
      String syn = this.transliterationStack.pop();
      this.restoreState(this.current);
      this.termAtt.setEmpty();
      this.termAtt.append(syn);
      this.posIncrAtt.setPositionIncrement(0);
      this.typeAtt.setType(AuthorUtils.AUTHOR_TRANSLITERATED);
      return true;
    }

    if (!input.incrementToken()) return false;

    if (typeAtt.type().equals(AuthorUtils.AUTHOR_INPUT) && this.genVariants()) {
      this.current = this.captureState();
    }

    return true;
  }

  private boolean genVariants() {
    //log.debug("generating name variants for: " + authorName);
    ArrayList<String> synonyms = AuthorUtils.getAsciiTransliteratedVariants(termAtt.toString());
    if (synonyms.size() > 0) {
      //log.debug("variants: " + synonyms);
      transliterationStack.addAll(synonyms);
      return true;
    }

    return false;
  }

  @Override
  public void reset() throws IOException {
    super.reset();
    transliterationStack.clear();
    current = null;
  }
}
