/**
 * 
 */
package org.apache.solr.analysis.author;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

/**
 * @author jluker
 * 
 */
public class AuthorTransliterationFactory extends TokenFilterFactory {
  
  private String inputType;
  
  public AuthorTransliterationFactory(Map<String,String> args) {
    super(args);
    if (args.containsKey("inputType")) {
      // tokenTypes = StrUtils.splitSmart(args.get("tokenTypes"), ",", false);
      inputType = args.remove("inputType");
      assert inputType instanceof String && inputType.length() > 0;
      if (inputType.equals("null")) inputType = null;
    } else {
      inputType = AuthorUtils.AUTHOR_INPUT;
    }
    if (!args.isEmpty()) {
      throw new IllegalArgumentException("Unknown parameter(s): " + args);
    }
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis
   * .TokenStream)
   */
  public AuthorTransliterationFilter create(TokenStream input) {
    return new AuthorTransliterationFilter(input, inputType);
  }
  
}
