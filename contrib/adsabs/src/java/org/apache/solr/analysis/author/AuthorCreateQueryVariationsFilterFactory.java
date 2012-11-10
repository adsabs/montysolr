package org.apache.solr.analysis.author;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class AuthorCreateQueryVariationsFilterFactory extends TokenFilterFactory {

  private boolean plainSurname;
  private boolean acronymVariations;

  @Override
  public void init(Map<String,String> args) {
    super.init(args);
    acronymVariations = false;
    plainSurname = false;

    if (args.containsKey("acronymVariations")) {
      acronymVariations = args.get("acronymVariations").equals("true");
    }

    if (args.containsKey("plainSurname")) {
      plainSurname = args.get("plainSurname").equals("true");
    }
  }

  public TokenStream create(TokenStream input) {
    return new AuthorCreateQueryVariationsFilter(input, plainSurname, acronymVariations);
  }

}
