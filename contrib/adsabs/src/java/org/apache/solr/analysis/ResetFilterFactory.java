/**
 * 
 */
package org.apache.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class ResetFilterFactory extends TokenFilterFactory {

    private String incomingType;
    private Integer posIncrement;
    private String outgoingType;
    private int[] range;
    private String prefix;

    public void init(Map<String, String> args) {
	    super.init(args);
	    
	    incomingType = null;
	    if (args.containsKey("incomingType")) {
	      incomingType = args.get("incomingType");
	      if (incomingType.equals("null")) incomingType=null;
	    }
	    
	    posIncrement = null;
      if (args.containsKey("posIncrement")) {
        posIncrement = Integer.parseInt((String)args.get("posIncrement"));
      }
      
      outgoingType = null;
      if (args.containsKey("outgoingType")) {
        outgoingType = (String) args.get("outgoingType");
      }
      
      range = new int[]{0, Integer.MAX_VALUE};
      if (args.containsKey("range")) {
        String r = (String) args.get("range");
        int i = 0;
        for (String x: r.split(",")) {
          range[i++] = Integer.parseInt(x);
        }
      }
      prefix = null;
      if (args.containsKey("addPrefix")) {
        prefix = (String) args.get("addPrefix");
      }
	}
	  
	/* (non-Javadoc)
	 * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	public ResetFilter create(TokenStream input) {
		return new ResetFilter(input, incomingType, posIncrement, outgoingType, range, prefix);
	}

}
