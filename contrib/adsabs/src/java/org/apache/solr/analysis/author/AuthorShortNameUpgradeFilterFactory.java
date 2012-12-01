/**
 * 
 */
package org.apache.solr.analysis.author;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.PersistingMapTokenFilterFactory;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.StrUtils;
import org.apache.lucene.analysis.synonym.NewSolrSynonymParser;
import org.apache.lucene.analysis.synonym.NewSynonymFilterFactory;
import org.apache.lucene.analysis.synonym.NewSynonymFilterFactory.SynonymParser;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.util.CharsRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class AuthorShortNameUpgradeFilterFactory extends PersistingMapTokenFilterFactory implements ResourceLoaderAware {

  public static final Logger log = LoggerFactory.getLogger(AuthorShortNameUpgradeFilterFactory.class);

  public static class SynonymBuilderFactory extends NewSynonymFilterFactory.SynonymBuilderFactory {

    protected SynonymParser getParser(Analyzer analyzer) {
      return new NewSolrSynonymParser(true, true, analyzer) {

        public void add(Reader in) throws IOException, ParseException {
          LineNumberReader br = new LineNumberReader(in);
          StringBuffer newBr = new StringBuffer();
          String line = null;
          
          String[] parts;
          
          try {
            while ((line = br.readLine()) != null) {
              // modify the original on-the-fly
              if (line.length() == 0 || line.charAt(0) == '#') {
                continue; // ignore empty lines and comments
              }
              String[] sides = line.split("=>");
              if (sides.length > 1) { // explicit mapping
                String[] names = getNames(sides[1]);
                parts = sides[0].split(" ");
                if (isLongForm(parts) && containsLongForm(names) > 0) {
                  newBr.append(escape(makeShortForm(parts)) + "=>" +
                      sides[0] + "," +
                      buildLine(names));
                  newBr.append("\n");
                }
              }
              else {
                String[] names = getNames(sides[0]);
                if (containsLongForm(names) > 1) {
                  String newLine = buildLine(names);
                  for (int i=0;i<names.length;i++) {
                    parts = names[i].split(" ");
                    if (isLongForm(parts)) {
                      newBr.append(escape(makeShortForm(parts)) + "=>" +
                          newLine);
                      newBr.append("\n");
                    }
                  }
                }
              }
            }
          } catch (IllegalArgumentException e) {
            ParseException ex = new ParseException("Invalid synonym rule at line " + br.getLineNumber(), 0);
            ex.initCause(e);
            throw ex;
          } finally {
            br.close();
          }
          
          // pass the modified synonym to the builder to create a synonym map
          super.add(new InputStreamReader(new ByteArrayInputStream(newBr.toString().getBytes()),
              Charset.forName("UTF-8").newDecoder()));

        }
        @Override
        public void add(CharsRef input, CharsRef output, boolean includeOrig) {
          super.add(input, output, true);
        }
        
        private String[] getNames(String vals) {
          List<String> nn = StrUtils.splitSmart(vals, ',');
          String names[] = new String[nn.size()];
          int j = 0;
          for (String n: nn) {
            names[j] = unescape(n);
            j++;
          }
          return names;
        }
        private String buildLine(String[] names) {
          HashSet<String> set = new HashSet<String>();
          StringBuilder out = new StringBuilder();
          boolean notFirst = false;
          
          for (String name: names) {
            
            String[] p = name.split(" ");
            if (isLongForm(p)) {
              set.add(makeShortForm(p));
            }
            set.add(name);
          }
          for (String name: set) {
            if (notFirst) out.append(",");
            out.append(escape(name));
            notFirst = true;
          }
          return out.toString();
        }
        
        
        private String unescape(String s) {
          return s.replace("\\ ", " ").replace("\\,", ",");
        }
        
        
        private String escape(String s) {
          return s.replace(" ", "\\ ").replace(",", "\\,");
        }
        
        
        private String makeShortForm(String[] parts) {
          StringBuilder out = new StringBuilder();
          out.append(parts[0]);
          for (int i=1;i<parts.length;i++) {
            out.append(" ");
            out.append(parts[i].substring(0, 1));
          }
          return out.toString();
        }
        
        private boolean isLongForm(String[] parts) {
          boolean res = false;
          for (int i=1;i<parts.length;i++) {
            if (parts[i].length() > 1)
              return true;
          }
          return res;
        }
        private int containsLongForm(String[] names) {
          int i = 0;
          for (String name: names) {
            if (isLongForm(name.split(" "))) {
              i++;
            }
          }
          return i;
        }
      };
    }
  }
  
  
  @Override
  public void inform(ResourceLoader loader) {
    super.inform(loader);
  }

  @Override
  public TokenStream create(TokenStream input) {
    // this filter factory does nothing on its own, it is used by the
    // NewSynonymFilteFactory
    return input;
  }

}