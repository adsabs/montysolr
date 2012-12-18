package org.apache.solr.handler.dataimport;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.solr.analysis.DateNormalizerTokenFilter;
import org.apache.solr.analysis.DateNormalizerTokenFilterFactory;

/*
 * Transforms the dates from the pubdate field, so that
 * they can be indexed as date type. We call the ADS
 * date token filter
 */
public class AdsDateTransformer extends Transformer {

  private Tokenizer tokenizer;
  private TokenStream tokenStream;
  
  public AdsDateTransformer() {
    super();
    tokenizer = new KeywordTokenizer(new StringReader(""));
    tokenStream = new DateNormalizerTokenFilter(tokenizer, "yyyy-MM-dd|yyyy-MM|yyyy", "+30MINUTES");
    
  }
  @Override
  public Object transformRow(Map<String, Object> row, Context context) {
    String value = (String) row.get("pubdate");
    
    if (value != null) {
      try {
        tokenizer.setReader(new StringReader(value));
        tokenStream.reset();
        tokenStream.incrementToken();
        row.put("date", tokenStream.getAttribute(CharTermAttribute.class).toString());
      } catch (IOException e) {
        throw new RuntimeException("Vvvery baaad, we have gotten no date, we have an exception instead!");
      }
    }
    
    return row;
  }

}
