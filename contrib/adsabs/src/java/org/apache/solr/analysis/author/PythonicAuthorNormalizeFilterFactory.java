package org.apache.solr.analysis.author;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;

public class PythonicAuthorNormalizeFilterFactory extends TokenFilterFactory {
	
	public PythonicAuthorNormalizeFilterFactory() {
		// test python is ready (don't want to wait until its too late)
		Reader reader = new StringReader("Malcolm X");
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		PythonicAuthorNormalizerFilter filter = new PythonicAuthorNormalizerFilter(tokenizer);
		try {
	    filter.reset();
	    while (filter.incrementToken()) {
				//pass
			}
    } catch (IOException e) {
	    throw new SolrException(ErrorCode.SERVER_ERROR, "Server is not ready to execute Pythonic name parser", e);
    }
		
	}
	
	public PythonicAuthorNormalizerFilter create(TokenStream input) {
		return new PythonicAuthorNormalizerFilter(input);
	}
}
