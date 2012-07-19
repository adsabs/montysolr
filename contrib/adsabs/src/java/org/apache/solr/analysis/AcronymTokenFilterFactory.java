/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.core.AcronymTokenFilter;

/**
 *
 * @author jluker
 */
public class AcronymTokenFilterFactory extends TokenFilterFactory {

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
