/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.adsabs.solr.analysis;

import java.util.Map;

import org.apache.solr.analysis.BaseTokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;

/**
 *
 * @author jluker
 */
public class AcronymTokenFilterFactory extends BaseTokenFilterFactory {

    private boolean emitBoth;
    private boolean lowercaseAcronyms;

    @Override
    public void init(Map<String,String> args) {
        super.init(args);
        assureMatchVersion();
        this.emitBoth = this.getBoolean("emitBoth", true);
        this.lowercaseAcronyms = this.getBoolean("lowercaseAcronyms", true);
    }

    public TokenStream create(TokenStream input) {
        return new AcronymTokenFilter(input, this.emitBoth, this.lowercaseAcronyms);
    }

}
