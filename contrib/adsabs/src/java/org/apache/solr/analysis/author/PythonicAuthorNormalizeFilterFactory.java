package org.apache.solr.analysis.author;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class PythonicAuthorNormalizeFilterFactory extends TokenFilterFactory {
	
	
	protected PythonicAuthorNormalizeFilterFactory(Map<String,String> args) {
    super(args);
    if (!args.isEmpty()) {
      throw new IllegalArgumentException("Unknown parameter(s): " + args);
    }
  }

  public PythonicAuthorNormalizerFilter create(TokenStream input) {
		return new PythonicAuthorNormalizerFilter(input);
	}
}
