package org.apache.solr.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenFilterFactory;

import java.util.Map;

public class ResetFilterFactory extends TokenFilterFactory {

    private String incomingType;
    private Integer posIncrement;
    private String outgoingType;
    private final int[] range;
    private String prefix;

    public ResetFilterFactory(Map<String, String> args) {
        super(args);

        incomingType = null;
        if (args.containsKey("incomingType")) {
            incomingType = args.remove("incomingType");
            if (incomingType.equals("null")) incomingType = null;
        }

        posIncrement = null;
        if (args.containsKey("posIncrement")) {
            posIncrement = Integer.parseInt(args.remove("posIncrement"));
        }

        outgoingType = null;
        if (args.containsKey("outgoingType")) {
            outgoingType = args.remove("outgoingType");
        }

        range = new int[]{0, Integer.MAX_VALUE};
        if (args.containsKey("range")) {
            String r = args.remove("range");
            int i = 0;
            for (String x : r.split(",")) {
                range[i++] = Integer.parseInt(x);
            }
        }
        prefix = null;
        if (args.containsKey("addPrefix")) {
            prefix = args.remove("addPrefix");
        }

        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameter(s): " + args);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.solr.analysis.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
     */
    public ResetFilter create(TokenStream input) {
        return new ResetFilter(input, incomingType, posIncrement, outgoingType, range, prefix);
    }

}
