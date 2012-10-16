/**
 * 
 */
package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.solr.analysis.WriteableSynonymMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rchyla
 * 
 * This class harvests (eats) different spellings and variations of the
 * author names. The variations will not get indexed.
 * 
 * The tokenizer chain is doing more work than if the generation and 
 * harvesting was done in one tokenizer, however I gave preference to 
 * clarity against speed. If we find the changed code is too slow, we
 * should revert it.
 *
 */
public final class AuthorTransliterationsCollectorFilter extends TokenFilter {

    public static final Logger log = LoggerFactory.getLogger(AuthorTransliterationsCollectorFilter.class);
	private WriteableSynonymMap synMap;
	
	private final CharTermAttribute termAtt;
    private final TypeAttribute typeAtt;
    private AttributeSource.State current;
    
	public AuthorTransliterationsCollectorFilter(TokenStream input, WriteableSynonymMap synMap) {
		super(input);
		termAtt = addAttribute(CharTermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
		current = null;
		this.synMap = synMap;
	}
	
	
	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public boolean incrementToken() throws IOException {
		
		if (current != null) {
			restoreState(current);
			current = null;
			return true;
		}
		
	    if (!input.incrementToken()) return false;
	    
	    if (typeAtt.type().equals(AuthorUtils.TOKEN_TYPE_AUTHOR)) {
	    	current = captureState();
	    	String authorName = termAtt.toString();
	    	Set<String> nameVariants;
	    	if (synMap.containsKey(authorName)) {
	    		nameVariants = synMap.get(authorName);
	    	}
	    	else {
	    		nameVariants = new HashSet<String>();
	    		synMap.put(authorName, nameVariants);
	    	}
	    	while (input.incrementToken()) {
	    		if (typeAtt.type().equals(AuthorUtils.TOKEN_TYPE_AUTHOR_GENERATED_TRANSLITERATION)) {
	    			nameVariants.add(termAtt.toString());
	    		}
	    		else {
	    			State newState = captureState();
	    			restoreState(current);
	    			current = newState;
	    			return true;
	    		}
	    	}
	    	restoreState(current);
	    	current = null;
	    	return true;
	    }
	    
	    
    	return true;
	}
	
	
	public void reset() throws IOException {
		super.reset();
		current = null;
	}

}
