package org.apache.solr.analysis;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.solr.schema.DateField;
import org.apache.solr.util.DateMathParser;

public final class DateNormalizerTokenFilter extends TokenFilter {

  private SimpleDateFormat format;
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private DateMathParser dmp;
  
  protected DateNormalizerTokenFilter(TokenStream input, String incomingFormat) {
    super(input);
    format = new SimpleDateFormat(incomingFormat, Locale.ROOT);
    dmp = new DateMathParser(DateField.UTC, Locale.ROOT);
  }

  @Override
  public boolean incrementToken() throws IOException {
    if (!input.incrementToken()) {                           
      return false;
    }
    CharSequence v = normalize(termAtt.toString());
    termAtt.setEmpty().append(v);
    return true;
  }

  private CharSequence normalize(String string) {
    try {
      Date date = format.parse(string.replace("00", "01"));
      dmp.setNow(date);
      date = dmp.parseMath("+30MINUTES");
      return DateField.formatExternal(date);
    } catch (ParseException e) {
      return "0000-00-00T00:00:00Z"; // error parsing input data
    }
  }

}
