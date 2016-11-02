package org.apache.solr.analysis;

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * TODO: set payloads?
 */
public final class ResetFilter extends TokenFilter {

  private final String incomingType;
  private final Integer posIncrement;
  private final String outgoingType;
  private int numTokens = 0;
  
  private final PositionIncrementAttribute posIncrAtt;
  private final TypeAttribute typeAtt;
  private final CharTermAttribute termAtt;
  private int lowEnd;
  private int highEnd;
	private String prefix;

  
  public ResetFilter(TokenStream input, String incomingType,
      Integer posIncrement, String outgoingType, int[] range,
      String prefix) {
    super(input);
    posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    typeAtt = addAttribute(TypeAttribute.class);
    termAtt = addAttribute(CharTermAttribute.class);
    
    this.incomingType = incomingType;
    this.posIncrement = posIncrement;
    this.outgoingType = outgoingType;
    this.lowEnd = range[0];
    this.highEnd = range[1];
    this.prefix = prefix;
  }

  
  

  /* (non-Javadoc)
   * @see org.apache.lucene.analysis.TokenStream#incrementToken()
   */
  @Override
  public boolean incrementToken() throws IOException {

    if (!input.incrementToken()) return false;
    
    
    if (numTokens >= lowEnd && numTokens <= highEnd) {
    
      //System.out.println(termAtt.toString() + " pos=" + posIncrAtt.getPositionIncrement() + " type=" + typeAtt.type());
      if (incomingType == null) { // null means process all tokens
        resetAttributes();
      }
      else if (typeAtt.type().equals(incomingType)) {
        resetAttributes();
      }
      //System.out.println("   " + termAtt.toString() + " pos=" + posIncrAtt.getPositionIncrement() + " type=" + typeAtt.type());

    }
    
    numTokens++;
    
    return true;
  }

  private void resetAttributes() {
    if (posIncrement != null) {
      posIncrAtt.setPositionIncrement(posIncrement);
    }
    if (outgoingType != null) {
      typeAtt.setType(outgoingType);
    }
    if (prefix != null) {
    	String newValue = prefix + termAtt.toString();
    	termAtt.setEmpty().append(newValue);
    }
  }

  @Override
  public void reset() throws IOException {
    super.reset();
    numTokens = 0;
  }
}
