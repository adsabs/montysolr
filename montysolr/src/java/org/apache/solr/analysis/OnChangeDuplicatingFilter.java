package org.apache.solr.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.DuplicateTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public final class OnChangeDuplicatingFilter extends TokenFilter {
	
	public static final String DUPLICATE = "TOKEN_DUPLICATE";
	
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private final DuplicateTermAttribute dupAttr = addAttribute(DuplicateTermAttribute.class);
    
	private State currentState;

	
	public OnChangeDuplicatingFilter(TokenStream input) {
		super(input);
		currentState = null;
	}
	
    
	@Override
	public boolean incrementToken() throws IOException {
		if (currentState != null) {
			restoreState(currentState);
			typeAtt.setType(DUPLICATE);
			posIncrAtt.setPositionIncrement(0);
			dupAttr.clear();
			currentState = null;
			return true;
		}
		if (input.incrementToken()) {
			String orig = dupAttr.getValue();
			String modified = termAtt.toString();
			if (orig != null && !orig.equals(modified)) {
				currentState = captureState();
				termAtt.setEmpty().append(orig);
			}
			dupAttr.clear();
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		currentState = null;
	}
	
}
