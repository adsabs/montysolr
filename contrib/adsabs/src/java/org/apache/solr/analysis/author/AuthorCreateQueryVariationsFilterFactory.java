package org.apache.solr.analysis.author;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class AuthorCreateQueryVariationsFilterFactory extends TokenFilterFactory {

  private boolean plainSurname;
  private boolean acronymVariations;
  private String tokenType;
  private boolean addWildcards;
  private boolean shortenMultiname;

  @Override
  public void init(Map<String,String> args) {
    super.init(args);
    acronymVariations = false;
    plainSurname = false;
    addWildcards = false;
    shortenMultiname = false;
    tokenType = null;
    
    if (args.containsKey("acronymVariations")) {
      acronymVariations = args.get("acronymVariations").equals("true");
    }

    if (args.containsKey("plainSurname")) {
      plainSurname = args.get("plainSurname").equals("true");
    }
    
    if (args.containsKey("addWildcards")) {
      addWildcards = args.get("addWildcards").equals("true");
    }
    
    if (args.containsKey("addShortenedMultiName")) {
      shortenMultiname = args.get("addShortenedMultiName").equals("true");
    }
    
    if (args.containsKey("tokenType")) {
      tokenType = args.get("tokenType");
      if (tokenType.equals("null")) tokenType = null;
    }
  }

  public TokenStream create(TokenStream input) {
    return new AuthorCreateQueryVariationsFilter(input, tokenType, 
        plainSurname, acronymVariations, addWildcards, shortenMultiname);
  }

}
