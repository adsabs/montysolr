package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryParser.aqp.AqpAdsabsQueryParser;

/**
 * This class is used explicitly (and only) for exceptions
 * that happen inside a QueryParser (AQP). We want to have a
 * mechanism to raise and exception that is not interfering
 * with existing interfaces. But which is caught be the 
 * {@link AqpAdsabsQueryParser}
 * 
 * @author rchyla
 *
 */

public class NestedParseException extends RuntimeException {
	
	private static final long serialVersionUID = -3943145526662562552L;
	
	public NestedParseException() {
        super();
    }
	
	public NestedParseException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public NestedParseException(String string) {
		super(string);
	}
	
	public NestedParseException(Throwable cause) {
        super(cause);
    }

	

}
