/**
 * 
 */
package org.apache.solr.analysis.author;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.PersistingMapTokenFilterFactory;
import org.apache.solr.analysis.WriteableExplicitSynonymMap;
import org.apache.solr.analysis.WriteableSynonymMap;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.StrUtils;

public class AuthorCollectorFactory extends PersistingMapTokenFilterFactory {

  private List<String> tokenTypes;
  private boolean emitTokens;

  public void init(Map<String, String> args) {
    super.init(args);
    if (args.containsKey("tokenTypes")) {
      tokenTypes = StrUtils.splitSmart(args.get("tokenTypes"), ",", false);
    }
    else {
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "The tokenType parameter missing");
    }
    emitTokens = false;
    if (args.containsKey("emitTokens")) {
      if (((String) args.get("emitTokens")).equals("true")) {
        emitTokens = true;
      }
    }
  }

  /* (non-Javadoc)
   * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
   */
  public AuthorCollectorFilter create(TokenStream input) {
    
    AuthorCollectorFilter collector = new AuthorCollectorFilter(input, getSynonymMap());
    collector.setTokenTypes(tokenTypes);
    collector.setEmitTokens(emitTokens);
    return collector;
  }
  
  @Override
  public WriteableSynonymMap createSynonymMap() {
    return new WriteableExplicitSynonymMap() { // no configuration allowed!
      @Override
      public void add(String origName, Set<String> values) {
        // key = the original author input (possibly with utf8 characters)
        // values = set of transliterated values
        Set<String> masterSet = null;
        for (String key: values) {
          if (containsKey(key)) {
            masterSet = get(key);
            break;
          }
        }
        
        if (masterSet==null) { 
          masterSet = new LinkedHashSet<String>();
        }
        masterSet.add(origName);
        
        for (String key: values) {
          put(key, masterSet);
        }
      }
      
      @Override
      public String formatEntry(String key, Set<String>values) {
        StringBuffer out = new StringBuffer();
        //out.append(super.formatEntry(key, values));
        String[] nameParts = key.split(" ");
        if (nameParts.length > 1) {
          nameParts[0] = nameParts[0].replace(",", "\\,");
          String[][] otherNames = new String[values.size()][];
          int n = 0;
          for (String name: values) {
            otherNames[n++] = name.split(" ");
            otherNames[n-1][0] = otherNames[n-1][0].replace(",", "\\,"); 
          }
          do {
            for (n=0;n<nameParts.length;n++) {
              out.append(join(nameParts, n));
              out.append("=>");
              boolean notFirst = false;
              for (String[] other: otherNames) {
                if (notFirst) out.append(",");
                out.append(join(other, n));
                notFirst = true;
              }
              out.append("\n");
            }
          } while (shortened(nameParts, otherNames));
        }
        return out.toString();
      }
      
      private String join(String[] name, int v) {
        StringBuffer out = new StringBuffer();
        boolean notFirst = false;
        for (int i=0;i<=v;i++) {
          if (notFirst) out.append("\\ ");
          out.append(name[i]);
          notFirst=true;
        }
        return out.toString();
      }
      
      private boolean shortened(String[]nameParts, String[][] otherNames) {
        boolean toBeModified = false;
        for (int i=1;i<nameParts.length;i++) {
          if (nameParts[i].length() > 1) {
            toBeModified = true;
            break;
          }
        }
        if (!toBeModified) return false;
        for (int i=1;i<nameParts.length;i++) {
          if (nameParts[i].length() > 1) {
            nameParts[i] = nameParts[i].substring(0, 1);
            for (String[] other: otherNames) {
              other[i] = other[i].substring(0, 1);
            }
          }
        }
        return true;
      }
    };
  }

}
