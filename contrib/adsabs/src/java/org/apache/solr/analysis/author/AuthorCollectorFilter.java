/**
 * 
 */
package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
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

  private final CharTermAttribute termAtt;
  private final TypeAttribute typeAtt;
  private boolean emitTokens = false;
  private Set<String> tokenTypes;

  public AuthorCollectorFilter(TokenStream input) {
    super(input);
    termAtt = addAttribute(CharTermAttribute.class);
    typeAtt = addAttribute(TypeAttribute.class);
    tokenTypes = new HashSet<String>();
  }


  /* (non-Javadoc)
   * @see org.apache.lucene.analysis.TokenStream#incrementToken()
   */
  @Override
  public boolean incrementToken() throws IOException {

    if (!input.incrementToken()) {
      return false;
    }

    //System.out.println("token:" + termAtt.toString());
    
    if (emitTokens) {
    	if (tokenTypes.contains(typeAtt.type())) {
	        return true;
    	}
    	else {
	      // we'll eat the tokens
	      while (input.incrementToken()) {
	        if (tokenTypes.contains(typeAtt.type())) {
	          return true;
	        }
	      }
	      return false;
	    }
    }
    else {
    	if (tokenTypes.contains(typeAtt.type())) {
	      // we'll eat the tokens
	      while (input.incrementToken()) {
	        if (tokenTypes.contains(typeAtt.type())) {
	          // pass
	        }
	        else {
	        	return true;
	        }
	      }
	      return false;
	    }
    	else {
    		return true;
    	}
    }

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
