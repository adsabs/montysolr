package org.apache.solr.analysis.author;

import java.util.Map;

import org.jython.JythonObjectFactory;
import org.jython.monty.interfaces.JythonNameParser;


public class NameParser {
	
    private JythonNameParser jythonParser;

	public NameParser() {
    	JythonObjectFactory factory = new JythonObjectFactory(JythonNameParser.class, "jython_name_parser", "HumanParser");
        this.jythonParser = (JythonNameParser) factory.createObject(); 
    }
	
	/**
	 * This method will parse a name into first middle and last names.
	 * @return Map containing title, first, middle and last names, suffix
	 */
	public Map<String, String> parseName(String name) {	
		return jythonParser.parse_human_name(name);
	}
}
