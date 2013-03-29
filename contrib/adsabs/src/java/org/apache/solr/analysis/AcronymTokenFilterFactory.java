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

    @Override
    public void init(Map<String,String> args) {
        super.init(args);
        assureMatchVersion();
        this.emitBoth = this.getBoolean("emitBoth", true);
        if (args.containsKey("prefix")) {
        	prefix = (String) args.get("prefix");
        }
        if (args.containsKey("setType")) {
        	tokenType = (String) args.get("setType");
        }
    }

    public TokenStream create(TokenStream input) {
        return new AcronymTokenFilter(input, this.emitBoth, this.prefix, this.tokenType);
    }

}
