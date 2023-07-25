package org.apache.solr.analysis.author;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public final class AuthorDetectAndIgnoreFilter extends TokenFilter {

	private Integer maxlen;
	private final CharTermAttribute termAtt;
	
	protected AuthorDetectAndIgnoreFilter(TokenStream input, Integer maxlen) {
	  super(input);
	  this.maxlen = maxlen;
	  termAtt = addAttribute(CharTermAttribute.class);
  }

	@Override
  public boolean incrementToken() throws IOException {
		if (!input.incrementToken()) {
      return false;
    }
	  
		if (maxlen != null && maxlen != -1) {
			String[] parts = termAtt.toString().split(" ");
			if (parts.length > maxlen) {
				return false;
			}
		}
	  return true;
  }

}
