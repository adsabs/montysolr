/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.AcronymTokenFilter;

/**
 *
 * @author jluker
 */
public class AcronymTokenFilterFactory extends TokenFilterFactory {

    private boolean emitBoth;
    private String prefix = null;
    private String tokenType = null;

    public AcronymTokenFilterFactory(Map<String,String> args) {
        super(args);
        this.emitBoth = this.getBoolean(args, "emitBoth", true);
        if (args.containsKey("prefix")) {
        	prefix = (String) args.get("prefix");
        	args.remove("prefix");
        }
        if (args.containsKey("setType")) {
        	tokenType = (String) args.get("setType");
        	args.remove("setType");
        }
        if (!args.isEmpty()) {
          throw new IllegalArgumentException("Unknown parameter(s): " + args);
        }
    }

    public TokenStream create(TokenStream input) {
        return new AcronymTokenFilter(input, this.emitBoth, this.prefix, this.tokenType);
    }

}
