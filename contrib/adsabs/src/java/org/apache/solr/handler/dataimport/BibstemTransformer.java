package org.apache.solr.handler.dataimport;

import java.util.Map;
import java.util.regex.Pattern;

public class BibstemTransformer extends Transformer {

  private static Pattern lastFour = Pattern.compile("^[\\.\\d]+$");

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
  public Object transformRow(Map<String, Object> row, Context context) {
    String bibcode = (String) row.get("bibcode");
    
    if (bibcode == null || bibcode.length() != 19) return row;
    
    String[] bibstems = new String[2];
    bibstems[0] = bibcode.substring(4,9).replace(".", "");
    bibstems[1] = trimDots(bibcode.substring(4,13));
    row.put("bibstem_facet", extractBibstem(bibcode));
    row.put("bibstem", bibstems);
    return row;
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
