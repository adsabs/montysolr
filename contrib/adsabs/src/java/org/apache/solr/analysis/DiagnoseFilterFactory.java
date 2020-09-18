package org.apache.solr.analysis;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class DiagnoseFilterFactory extends TokenFilterFactory {

	private String msg;

	public DiagnoseFilterFactory(Map<String,String> args) {
		super(args);
		msg = null;
		if (args.containsKey("msg")) {
			msg = args.remove("msg");
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
	public DiagnoseFilter create(TokenStream input) {
		return new DiagnoseFilter(input, msg);
	}

}

final class DiagnoseFilter extends TokenFilter {

	private int numTokens = 0;

	private final PositionIncrementAttribute posIncrAtt;
	private final TypeAttribute typeAtt;
	private final CharTermAttribute termAtt;
	private final OffsetAttribute offsetAtt;
	private final String msg;
  private final PositionLengthAttribute posLen;

	public DiagnoseFilter(TokenStream input, String msg) {
		super(input);
		this.msg = msg;
		posIncrAtt = addAttribute(PositionIncrementAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
		termAtt = addAttribute(CharTermAttribute.class);
		offsetAtt = addAttribute(OffsetAttribute.class);
		posLen = addAttribute(PositionLengthAttribute.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public boolean incrementToken() throws IOException {

		if (!input.incrementToken()) return false;

		System.out.println("stage:" + (msg != null ? msg : "null") + 
				" term=" + termAtt.toString() + " posInc="
				+ posIncrAtt.getPositionIncrement()
				+ " posLen=" + posLen.getPositionLength()
				+ " type=" + typeAtt.type()
				+ " offsetStart=" + offsetAtt.startOffset() + " offsetEnd="
				+ offsetAtt.endOffset());

		return true;
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		numTokens = 0;
	}
}