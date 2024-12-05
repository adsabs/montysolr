/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.solr.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenFilterFactory;

import java.util.Map;

public class DateNormalizerTokenFilterFactory extends TokenFilterFactory {

    private final String inputFormat;
    private final String offset;

    public DateNormalizerTokenFilterFactory(Map<String, String> args) {
        super(args);
        this.inputFormat = args.containsKey("format") ? args.get("format") : "yyyy-MM-dd";
        this.offset = "+30MINUTES";
        args.remove("format");
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameter(s): " + args);
        }
    }

    public TokenStream create(TokenStream input) {
        return new DateNormalizerTokenFilter(input, this.inputFormat, this.offset);
    }

}
