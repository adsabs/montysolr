package org.apache.solr.analysis.author;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public final class AuthorNormalizeFilter extends TokenFilter {

  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
  private boolean keepApostrophes = false;

  public AuthorNormalizeFilter(TokenStream input) {
    super(input);
  }
  
  public AuthorNormalizeFilter(TokenStream input, boolean keepApostrophes) {
    super(input);
    this.keepApostrophes  = keepApostrophes;
  }

  /* (non-Javadoc)
   * @see org.apache.lucene.analysis.TokenStream#incrementToken()
   */
  @Override
  public boolean incrementToken() throws IOException {
    if (!input.incrementToken()) return false;
    
//    if (setIntoPayload ) {
//      payloadAtt.setPayload(new BytesRef(termAtt.toString()));
//    }
    String normalized;
    
    normalized = AuthorUtils.normalizeAuthor(termAtt.toString(), keepApostrophes);
    //System.out.println("normalized="+normalized);
    termAtt.setEmpty().append(normalized);
    typeAtt.setType(AuthorUtils.AUTHOR_INPUT);
    
    return true;
  }
}
