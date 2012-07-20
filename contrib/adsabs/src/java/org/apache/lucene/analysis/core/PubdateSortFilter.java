package org.apache.lucene.analysis.core;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PubdateSortFilter extends TokenFilter {

    public static final Logger log = LoggerFactory.getLogger(PubdateSortFilter.class);
    
	public PubdateSortFilter(TokenStream input) {
		super(input);
	}
	
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	@Override
	public boolean incrementToken() throws IOException {
        if (!input.incrementToken()) {                           
            return false;
        }
		String origTerm = termAtt.toString();
		String pubdateSort = origTerm.replaceAll("-", "");
		termAtt.setEmpty().append(pubdateSort);
		return true;
	}

}
