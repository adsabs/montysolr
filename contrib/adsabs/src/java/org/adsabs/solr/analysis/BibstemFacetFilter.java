package org.adsabs.solr.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BibstemFacetFilter extends TokenFilter {
	
    public static final Logger log = LoggerFactory.getLogger(BibstemFacetFilter.class);
    
	protected BibstemFacetFilter(TokenStream input) {
		super(input);
	}

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	
	public static boolean isBibcode(String bibcode) {
		return (bibcode.matches("^\\d{4}.+") && bibcode.length() == 19);
	}
	
	public static boolean hasVolume(String bibcode) {
		return bibcode.substring(9, 13).matches("^[\\.\\d]+$");
	}
	
	public static String extractBibstem(String bibcode) {
		if (hasVolume(bibcode)) {
			return bibcode.substring(4, 9).replaceAll("\\.", "");
		} else {
			return bibcode.substring(4, 13);
		}
	}
	
	@Override
	public boolean incrementToken() throws IOException {
        if (!input.incrementToken()) {                           
            return false;
        }
		String origTerm = termAtt.toString();
		if (!isBibcode(origTerm)) {
			log.error("Input was not a bibcode!: " + origTerm);
			return false;
		}
		
		String bibstem = extractBibstem(origTerm);
		termAtt.setEmpty().append(bibstem);
		return true;
	}

}
