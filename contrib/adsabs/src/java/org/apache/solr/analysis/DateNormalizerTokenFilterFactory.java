/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.AcronymTokenFilter;

public class DateNormalizerTokenFilterFactory extends TokenFilterFactory {

    private String inputFormat;

    @Override
    public void init(Map<String,String> args) {
        super.init(args);
        assureMatchVersion();
        this.inputFormat = args.containsKey("format") ? (String) args.get("format") : "yyyy-MM-dd";
    }

    public TokenStream create(TokenStream input) {
        return new DateNormalizerTokenFilter(input, this.inputFormat);
    }

}
