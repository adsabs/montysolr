package org.apache.lucene.newseman;

import java.util.List;

import org.apache.lucene.util.Attribute;

public interface SemanticTagAttribute extends Attribute {
	
	/*
	 * Inserts the semantic tags into the token
	 */
	public void setSemanticTags(List<String> tags);
	
	/*
	 * Retrieves the list of semantic tags
	 */
	public List<String> getSemanticTags();
	
	/*
	 * Returns true if the current token has at least
	 * one semantic tag
	 */
	public boolean hasSemanticTags();
}
