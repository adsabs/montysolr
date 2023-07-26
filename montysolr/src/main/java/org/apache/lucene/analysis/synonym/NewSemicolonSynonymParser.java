package org.apache.lucene.analysis.synonym;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.CharsRefBuilder;

public class NewSemicolonSynonymParser extends NewSynonymFilterFactory.SynonymParser {
  private final boolean expand;
  
  public NewSemicolonSynonymParser(boolean dedup, boolean expand, Analyzer analyzer) {
    super(dedup, analyzer);
    this.expand = expand;
  }
  
  public void add(Reader in) throws IOException, ParseException {
    LineNumberReader br = new LineNumberReader(in);
    try {
      addInternal(br);
    } catch (IllegalArgumentException e) {
      ParseException ex = new ParseException("Invalid synonym rule at line " + br.getLineNumber() + " " + br.readLine(), 0);
      ex.initCause(e);
      throw ex;
    } finally {
      br.close();
    }
  }
  
  private void addInternal(BufferedReader in) throws IOException {
    String line = null;
    while ((line = in.readLine()) != null) {
      if (line.length() == 0 || line.charAt(0) == '#') {
        continue; // ignore empty lines and comments
      }
      
      CharsRef inputs[];
      CharsRef outputs[];
      
      // TODO: we could process this more efficiently.
      String sides[] = split(line, "=>");
      if (sides.length > 1) { // explicit mapping
        if (sides.length != 2) {
          throw new IllegalArgumentException("more than one explicit mapping specified on the same line");
        }
        String inputStrings[] = split(sides[0], ";");
        inputs = new CharsRef[inputStrings.length];
        for (int i = 0; i < inputs.length; i++) {
          inputs[i] = analyze(inputStrings[i].trim(), new CharsRefBuilder());
        }
        
        String outputStrings[] = split(sides[1], ";");
        outputs = new CharsRef[outputStrings.length];
        for (int i = 0; i < outputs.length; i++) {
          outputs[i] = analyze(outputStrings[i].trim(), new CharsRefBuilder());
        }
      } else {
        String inputStrings[] = split(line, ";");
        inputs = new CharsRef[inputStrings.length];
        for (int i = 0; i < inputs.length; i++) {
          inputs[i] = analyze(inputStrings[i].trim(), new CharsRefBuilder());
        }
        if (expand) {
          outputs = inputs;
        } else {
          outputs = new CharsRef[1];
          outputs[0] = inputs[0];
        }
      }
      
      // currently we include the term itself in the map,
      // and use includeOrig = false always.
      // this is how the existing filter does it, but its actually a bug,
      // especially if combined with ignoreCase = true
      for (int i = 0; i < inputs.length; i++) {
        for (int j = 0; j < outputs.length; j++) {
          add(inputs[i], outputs[j], false);
        }
      }
    }
  }
  
  private static String[] split(String s, String separator) {
    ArrayList<String> list = new ArrayList<String>(2);
    StringBuilder sb = new StringBuilder();
    int pos=0, end=s.length();
    while (pos < end) {
      if (s.startsWith(separator,pos)) {
        if (sb.length() > 0) {
          list.add(sb.toString());
          sb=new StringBuilder();
        }
        pos+=separator.length();
        continue;
      }

      char ch = s.charAt(pos++);
      if (ch=='\\') {
        sb.append(ch);
        if (pos>=end) break;  // ERROR, or let it go?
        ch = s.charAt(pos++);
      }

      sb.append(ch);
    }

    if (sb.length() > 0) {
      list.add(sb.toString());
    }

    return list.toArray(new String[list.size()]);
  }
  
  private String unescape(String s) {
    if (s.indexOf("\\") >= 0) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < s.length(); i++) {
        char ch = s.charAt(i);
        if (ch == '\\' && i < s.length() - 1) {
          sb.append(s.charAt(++i));
        } else {
          sb.append(ch);
        }
      }
      return sb.toString();
    }
    return s;
  }
}
