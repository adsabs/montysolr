package org.apache.solr.analysis.author;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.PersistingMapTokenFilterFactory;
import org.apache.solr.common.util.StrUtils;
import org.apache.lucene.analysis.synonym.NewSolrSynonymParser;
import org.apache.lucene.analysis.synonym.NewSynonymFilterFactory;
import org.apache.lucene.analysis.synonym.NewSynonymFilterFactory.SynonymParser;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.util.CharsRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This is a trickster class - it modifies the synonym input on the fly, so that
 * we don't need to bother with producing the multiplicated data from the 
 * author synonyms. But obviously, this could introduce some bugs...
 */
public class AuthorShortNameUpgradeFilterFactory extends PersistingMapTokenFilterFactory implements ResourceLoaderAware {

  public static final Logger log = LoggerFactory.getLogger(AuthorShortNameUpgradeFilterFactory.class);

  
  /*
   * If we were insane (and we apparently are!) we would want to generate all combinations
   * of the name, ie.
   * 
   *  Surname, One Two Three; Foo, Bar Baz
   *  
   * Results in:
   *  
   *  Surname, One T T => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, O Two T => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, O T Three => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, One T T => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, One Two T => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, O Two Three => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, One T Three => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  
   *  PLUS!
   *  
   *  Surname, One T => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, O Two => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, One => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, O => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname,  => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  
   *  and this happens for every name in the list!!!
   *  
   *  OK, I honestly think this is too much combinations and most of them are going to be
   *  useless. Especially, because these combinations ARE ALREADY generated during the
   *  search for synonyms (but NOT used in the query if there is no EXACT match!) 
   *  
   *  I secretly hope, that if a user typed "Foo, Boo Boz" she REALLY doesn't want
   *  to get "Foo, Baz Bar" -- and this is exactly what they would get IFF this logic is
   *  implemented - ie. if the synonym file contained another line with:
   *  
   *  "Foo, Boo Boz; Surname, One Boo"
   *  
   *  The combination "Foo, B B" will cause all the patters of these two lines to be merged
   *  with "Foo, Bar Baz" and the FALSE FALSE HITS are returned. And I don't like this idea and I 
   *  think it is wrong. 
   *  
   *  So, I am going to implement a middle path that creates ONLY the 
   *  initial and surname variations. And if we discover, that in fact this behaviour is not
   *  desirable (then I can change it)
   *  
   *  So, to summarize, I want to generate only these patters for now:
   *  
   *  Surname, => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, O => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, O T => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  Surname, O T T => Surname, One Two Three; Surname, O T T; Foo, Bar Baz; Foo, B B
   *  
   */
  public static class MakeAllShortNames extends NewSynonymFilterFactory.SynonymBuilderFactory {

    protected SynonymParser getParser(Analyzer analyzer) {
      return new NewSolrSynonymParser(true, true, analyzer) {

        public void add(Reader in) throws IOException, ParseException {
          LineNumberReader br = new LineNumberReader(in);
          StringBuffer newBr = new StringBuffer();
          String line = null;
          
          String[] parts;
          HashSet<String> seen = new HashSet<String>();
          
          try {
            while ((line = br.readLine()) != null) {
            	//System.out.println(line);
              // modify the original on-the-fly
              if (line.length() == 0 || line.charAt(0) == '#') {
                continue; // ignore empty lines and comments
              }
              seen.clear();
              
              String[] sides = line.split("=>");
              if (sides.length > 1) { // explicit mapping
                String[] names = getNames(sides[1]);
                parts = sides[0].split(" ");
                if (isLongForm(parts) && containsLongForm(names) > 0) {
                  for (String shortForm: getAllShortForms(parts)) {
                    if (seen.contains(shortForm)) continue;
                    seen.add(shortForm);
                    newBr.append(escape(shortForm) + "=>" +
                        sides[0] + "," +
                        buildLine(names));
                    newBr.append("\n");
                  }
                }
              }
              else {
                String[] names = getNames(sides[0]);
                if (containsLongForm(names) > 1) {
                  String newLine = buildLine(names);
                  for (int i=0;i<names.length;i++) {
                    parts = names[i].split(" ");
                    if (isLongForm(parts)) {
                      for (String shortForm: getAllShortForms(parts)) {
                        if (seen.contains(shortForm)) continue;
                        seen.add(shortForm);
                        newBr.append(escape(shortForm) + "=>" +
                            newLine);
                        newBr.append("\n");
                      }
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
          super.add(new InputStreamReader(new ByteArrayInputStream(newBr.toString().getBytes(Charset.forName("UTF-8"))),
              Charset.forName("UTF-8").newDecoder()));

        }
        
        private String[] getAllShortForms(String[] parts) {
          String[] names = new String[parts.length];
          for (int i=0;i<parts.length;i++) {
            StringBuilder out = new StringBuilder();
            out.append(parts[0]);
            for (int j=1;j<=i;j++) {
              out.append(" ");
              out.append(parts[i].substring(0, 1));
            }
            names[i] = out.toString();
          }
          return names;
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
          return parts.length > 1;
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
  
  /*
   * The following class will change (on-the-fly) the synonym file, for every
   * rule which contains a fullname, ie. 
   * 
   *    Surname, Name; Foo, Bar
   *    
   * it will produce new mappings of the form
   * 
   *    Surname, N => Surname, Name; Foo, B; Foo, Bar
   *    Foo, B => Foo, Bar; Surname, Name; Surname, N
   *    
   * 
   *    
   * This class was the first attempt I wrote on the synonym upgrade, however
   * Alberto wants that all combinations of names are upgraded. Ie. 
   *    
   *    Surname, => Surname, Name; Foo, B; Foo, Bar
   *    Surname, N => Surname, Name; Foo, B; Foo, Bar
   *    Foo, => Foo, Bar; Surname, Name; Surname, N
   *    Foo, B => Foo, Bar; Surname, Name; Surname, N
   * 
   */
  public static class MakeShortNames extends NewSynonymFilterFactory.SynonymBuilderFactory {

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