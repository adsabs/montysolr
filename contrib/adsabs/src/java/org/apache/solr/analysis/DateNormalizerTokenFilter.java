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

  private SimpleDateFormat[] format;
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private DateMathParser dmp;
  private String offset;
  
  public DateNormalizerTokenFilter(TokenStream input, String incomingFormat, String offset) {
    super(input);
    this.offset = offset;
    String[] parts = incomingFormat.split("\\|");
    format = new SimpleDateFormat[parts.length];
    for (int i=0;i<parts.length;i++) {
      format[i] = new SimpleDateFormat(parts[i], Locale.ROOT);
      format[i].setTimeZone(DateField.UTC);
    }
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
    boolean normalDate = true;
    if (string.contains("00")) {
      string = string.replace("-00", "");
      normalDate = false;
    }
    if (string.length()<10) {
      normalDate = false;
    }
    
    for (SimpleDateFormat f: this.format) {
      try {
          Date date = f.parse(string);
          dmp.setNow(date);
          if (normalDate) { // move the docs with the date specified 30 min into the future (so that unspecified
                            // dates will all cluster in the first x minutes of the day)
            date = dmp.parseMath(this.offset);
          }
          //else {
            //date = dmp.parseMath("+5MINUTES"); // 00-00 dates are 1 minute after midnight
          //}
          return DateField.formatExternal(date);
      } catch (ParseException e) {
        //pass
      }
    }
    return "0000-00-00T00:00:00Z"; // error parsing input data
  }

}
