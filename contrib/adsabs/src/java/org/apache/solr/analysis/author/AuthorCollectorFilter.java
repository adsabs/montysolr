/**
 * 
 */
package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.solr.analysis.WriteableSynonymMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class harvests (eats) different spellings and variations of the
 * author names. The variations will not get indexed.
 * 
 * The tokenizer chain is doing more work than if the generation and 
 * harvesting was done in one tokenizer, however I gave preference to 
 * clarity against speed. If we find the changed code is too slow, we
 * should revert it.
 *
 */
public final class AuthorCollectorFilter extends TokenFilter {

  public static final Logger log = LoggerFactory.getLogger(AuthorCollectorFilter.class);
  private WriteableSynonymMap synMap = null;

  private final CharTermAttribute termAtt;
  private final TypeAttribute typeAtt;
  private boolean emitTokens = false;
  private Set<String> tokenBuffer;
  private Set<String> tokenTypes;
  private String authorInput;

  public AuthorCollectorFilter(TokenStream input, WriteableSynonymMap synMap) {
    super(input);
    termAtt = addAttribute(CharTermAttribute.class);
    typeAtt = addAttribute(TypeAttribute.class);
    tokenBuffer = new LinkedHashSet<String>();
    tokenTypes = new HashSet<String>();
    this.synMap = synMap;
  }


  /* (non-Javadoc)
   * @see org.apache.lucene.analysis.TokenStream#incrementToken()
   */
  @Override
  public boolean incrementToken() throws IOException {

    if (!input.incrementToken()) {
      return false;
    }

    if (authorInput!=null && tokenTypes.contains(typeAtt.type())) {
      tokenBuffer.add(termAtt.toString());

      if (emitTokens) {
        return true;
      }
      // we'll eat the tokens
      while (input.incrementToken()) {
        if (tokenTypes.contains(typeAtt.type())) {
          tokenBuffer.add(termAtt.toString());
        }
        else {
          if (typeAtt.type().equals(AuthorUtils.AUTHOR_INPUT)) {
            addTokensToSynMap();
            authorInput = termAtt.toString();
          }
          return true;
        }
      }
      return false;
    }

    if (typeAtt.type().equals(AuthorUtils.AUTHOR_INPUT)) {
      authorInput = termAtt.toString();
    }
    return true;
  }


  private void addTokensToSynMap() {
    if (tokenBuffer.size()>0 && synMap != null) {
      synMap.add(authorInput, tokenBuffer);
      tokenBuffer.clear();
      authorInput=null;
    }
  }


  @Override
  public void reset() throws IOException {
    super.reset();
  }
  
  @Override
  public void end() throws IOException {
    super.end();
    addTokensToSynMap();
  }
  
  @Override
  public void close() throws IOException {
    synMap.persist();
    super.close();
  }

  public void setEmitTokens(boolean b) {
    emitTokens = b;
  }


  public void setTokenTypes(List<String> tokenTypes) {
    for (String tt: tokenTypes) {
      this.tokenTypes.add(tt);
    }
  }

}
