package org.apache.lucene.analysis.core;

import java.io.IOException;
import java.util.Stack;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The filter receives a bibcode, eg. 2009ARA&A..47..481A
 * and returns the bibstem_j (position 4:9) and 
 * bibstem_jv (position 4:13)
 * 
 * But if the filter is in the facet mode, it returns only
 * one of these. When the volume is present and it is a number,
 * it returns only the shorter bibstem.
 *  
 *
 */
public final class BibstemFilter extends TokenFilter {
	
    public static final Logger log = LoggerFactory.getLogger(BibstemFilter.class);
    
    private static Pattern fourDigit = Pattern.compile("^\\d{4}.+"); 
    private static Pattern lastFour = Pattern.compile("^[\\.\\d]+$");
    private boolean facetMode = false;
    private Stack<String> stack;
    private AttributeSource.State current;
    
	public BibstemFilter(TokenStream input, boolean facetMode) {
		super(input);
		this.facetMode = facetMode;
		this.stack = new Stack<String>();
	}

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final PositionIncrementAttribute posAtt = addAttribute(PositionIncrementAttribute.class);
	
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
		
		if (this.stack.size() > 0) {
			String syn = this.stack.pop();
			this.restoreState(this.current);
			termAtt.setEmpty();
			termAtt.append(syn);
			posAtt.setPositionIncrement(0);
			return true;
		}
		
        if (!input.incrementToken()) {                           
            return false;
        }
        
		String origTerm = termAtt.toString();
		if (!isBibcode(origTerm)) {
			throw new IOException("Input was not a bibcode!: " + origTerm);
		}
		
		termAtt.setEmpty();
		
		if (facetMode) {
			termAtt.append(extractBibstem(origTerm));
		}
		else { // emit both
			this.current = this.captureState();
			termAtt.append(origTerm.substring(4,9).replace(".", ""));
			this.stack.add(trimDots(origTerm.substring(4,13)));
		}
		return true;
	}
	
	private String trimDots(String value) {
		int s = 0;
		int e = value.length()-1;
		while(e>s) {
			if (value.charAt(e) == '.') {
				e--;
			}
			else {
				break;
			}
		}
		while(s<e) {
			if (value.charAt(s) == '.') {
				s++;
			}
			else {
				break;
			}
		}
		return value.substring(s, e+1);
	}

}
