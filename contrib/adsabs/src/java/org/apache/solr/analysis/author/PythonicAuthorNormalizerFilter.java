package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonCall;
import monty.solr.jni.PythonMessage;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;

/*
 * This filter will call Python library: http://code.google.com/p/python-nameparser/
 * to parse the input string, eg.
 * 
 * Doe, Lt. Gen. John A. Kenneth IV 
 *
 * is parsed as:
 * 
 * <HumanName : [
 *  Title: 'Lt. Gen.' 
 *  First: 'John' 
 *  Middle: 'A. Kenneth' 
 *  Last: 'Doe' 
 *  Suffix: 'IV'
 * ]>
 *
 * Input can contain several author names, but these need to be separated
 * by semicolon
 */
public final class PythonicAuthorNormalizerFilter extends TokenFilter implements PythonCall {

  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
  private String pythonFunctionName = "parse_human_name";
  private List<String> buffer = new ArrayList<String>();
  
  /**
   * @param input
   */
  public PythonicAuthorNormalizerFilter(TokenStream input) {
    super(input);
  }

  @Override
  public boolean incrementToken() throws IOException {
  	if (buffer.size() > 0) {
  		termAtt.setEmpty().append(buffer.remove(0));
      typeAtt.setType(AuthorUtils.AUTHOR_INPUT);
      return true;
  	}
  	
    if (!input.incrementToken()) return false;
    
    String original = termAtt.toString();
    
    for (String individual: original.split(";")) {
    	PythonMessage message = MontySolrVM.INSTANCE
	      .createMessage(pythonFunctionName)
	      .setSender("PythonicAuthorNormalizerFilter")
	      .setParam("input", individual);
  
    	MontySolrVM.INSTANCE.sendMessage(message);

    	Object result = message.getResults();
    	if (result != null) {
    		@SuppressWarnings("unchecked")
        HashMap<String, String> parsedName = (HashMap<String,String>) result;
    		if (parsedName.containsKey("Last")) {
    			buffer.add(parsedName.get("Last") + "," 
    					+ (parsedName.containsKey("First") ? " " + parsedName.get("First") : "")
    					+ (parsedName.containsKey("Middle") ? " " + parsedName.get("Middle") : "")
    					);
    		}
    		else {
    			if (parsedName.containsKey("First") && parsedName.containsKey("Middle")) {
    				// should never happen
    				throw new SolrException(ErrorCode.BAD_REQUEST, "We cannot reliably parse author name: " + individual);
    			}
    			else if (parsedName.containsKey("First")) {
    				buffer.add(parsedName.get("First") + ","); // we treat it as surname
    			}
    		}
    	}
    	else {
    		buffer.add(original);
    	}
    }
    
    if (buffer.size() > 0) {
	    termAtt.setEmpty().append(buffer.remove(0));
	    typeAtt.setType(AuthorUtils.AUTHOR_INPUT);
    }
    
    return true;
  }

	@Override
  public void setPythonFunctionName(String name) {
		pythonFunctionName = name;
  }

	@Override
  public String getPythonFunctionName() {
	  return pythonFunctionName;
  }
}