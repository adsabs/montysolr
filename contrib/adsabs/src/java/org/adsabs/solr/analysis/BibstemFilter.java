package org.adsabs.solr.analysis;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BibstemFilter extends TokenFilter {
	
    public static final Logger log = LoggerFactory.getLogger(BibstemFilter.class);
    
    private static Pattern fourDigit = Pattern.compile("^\\d{4}.+"); 
    private static Pattern lastFour = Pattern.compile("^[\\.\\d]+$");
    	
	protected BibstemFilter(TokenStream input) {
		super(input);
	}

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	
	public static boolean isBibcode(String bibcode) {
		return (fourDigit.matcher(bibcode).matches() && bibcode.length() == 19);
	}
	
	public static boolean hasVolume(String bibcode) {
		return lastFour.matcher(bibcode.substring(9, 13)).matches();
	}
	
	public static String extractBibstem(String bibcode) {
		if (hasVolume(bibcode)) {
			return bibcode.substring(4, 9).replace(".", "");
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
			throw new IOException("Input was not a bibcode!: " + origTerm);
		}
		
		String bibstem = extractBibstem(origTerm);
		termAtt.setEmpty().append(bibstem);
		return true;
	}

}
