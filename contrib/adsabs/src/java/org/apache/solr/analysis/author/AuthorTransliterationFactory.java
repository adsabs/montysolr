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

    public void init(Map<String, String> args) {
	    super.init(args);
	    if (args.containsKey("inputType")) {
	      //tokenTypes = StrUtils.splitSmart(args.get("tokenTypes"), ",", false);
	      inputType = args.get("inputType");
	      assert inputType instanceof String && inputType.length() > 0;
	      if (inputType.equals("null")) inputType=null;
	    }
	    else {
	      inputType = AuthorUtils.AUTHOR_INPUT;
	    }
	}
	  
	/* (non-Javadoc)
	 * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	public AuthorTransliterationFilter create(TokenStream input) {
		return new AuthorTransliterationFilter(input, inputType);
	}

}
