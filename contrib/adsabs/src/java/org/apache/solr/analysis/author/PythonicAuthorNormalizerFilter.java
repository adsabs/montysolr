package org.apache.solr.analysis.author;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonCall;
import monty.solr.jni.PythonMessage;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.jython.JythonObjectFactory;
import org.jython.monty.interfaces.JythonNameParser;

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
public final class PythonicAuthorNormalizerFilter extends TokenFilter {

  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
  private List<String> buffer = new ArrayList<String>();
  private JythonNameParser jythonParser;
  
  /**
   * @param input
   */
  public PythonicAuthorNormalizerFilter(TokenStream input) {
    super(input);
    JythonObjectFactory factory = new JythonObjectFactory(JythonNameParser.class, "jython_name_parser", "HumanParser");
    this.jythonParser = (JythonNameParser) factory.createObject();
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
    	
    	Map<String,String> parsedName = jythonParser.parse_human_name(individual);

    	if (parsedName != null) {
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
    			else {
    				//buffer.add(""); // else it was parsed as title and will be ignored
    			}
    			
    		}
    	}
    	else {
    		buffer.add(original);
    	}
    }
    
    if (buffer.size() ==0) {
    	return false;
    }
    
    termAtt.setEmpty().append(buffer.remove(0));
    typeAtt.setType(AuthorUtils.AUTHOR_INPUT);
    
    return true;
  }
}