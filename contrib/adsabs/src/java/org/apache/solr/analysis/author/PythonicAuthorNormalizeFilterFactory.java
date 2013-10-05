package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class PythonicAuthorNormalizeFilterFactory extends TokenFilterFactory {
	
	public PythonicAuthorNormalizeFilterFactory() {
		
		// if I do this (calling pythong at the very beginning)
		// the JVM crashes (core dumps) when we use multiprocessing
		
		//		Reader reader = new StringReader("Malcolm X");
		//		Tokenizer tokenizer = new KeywordTokenizer(reader);
		//		PythonicAuthorNormalizerFilter filter = new PythonicAuthorNormalizerFilter(tokenizer);
		//		try {
		//	    filter.reset();
		//	    while (filter.incrementToken()) {
		//				//pass
		//			}
		//    } catch (IOException e) {
		//	    throw new SolrException(ErrorCode.SERVER_ERROR, "Server is not ready to execute Pythonic name parser", e);
		//    }
		
	}
	
	public PythonicAuthorNormalizerFilter create(TokenStream input) {
		return new PythonicAuthorNormalizerFilter(input);
	}
}
