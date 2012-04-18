/**
 * 
 */
package org.adsabs.solr.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.adsabs.solr.AuthorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jluker
 *
 */
public class AuthorAutoSynonymWriter extends TokenFilter {

    public static final Logger log = LoggerFactory.getLogger(AuthorAutoSynonymWriter.class);
    
	private BufferedWriter writer;
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	/**
	 * @param input
	 */
	public AuthorAutoSynonymWriter(TokenStream input, BufferedWriter writer) {
		super(input);
		this.writer = writer;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public boolean incrementToken() throws IOException {
	    if (!input.incrementToken()) return false;
	    String authorName = termAtt.toString();
	    log.debug("generating synonyms for " + authorName);
	    ArrayList<String> synonyms = AuthorUtils.genSynonyms(authorName);
	    log.debug("synonyms: " + synonyms);
	    synonyms.add(authorName);
	    if (synonyms.size() > 1) {
		    this.writeSynonyms(synonyms);
	    }
        return true;
	}
	
	public void writeSynonyms(ArrayList<String> synonyms) {
		writeSynonyms(synonyms, this.writer);
	}
	
	public void writeSynonyms(ArrayList<String> synonyms, BufferedWriter writer) {
		ArrayList<String> escaped = new ArrayList<String>();
		for (String s : synonyms) {
			log.debug("escaping " + s);
			escaped.add(s.replace(",", "\\,").replace(" ", "\\ "));
		}
		String synonymSet = StringUtils.join(escaped, ",");
		log.debug("synonym set: " + synonymSet);
		try {
			synchronized(writer) {
				writer.write(synonymSet);
				writer.newLine();
				writer.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
