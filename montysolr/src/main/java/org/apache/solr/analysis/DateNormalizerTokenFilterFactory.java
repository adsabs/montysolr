/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;

public class DateNormalizerTokenFilterFactory extends TokenFilterFactory {

    private String inputFormat;
    private String offset;

    public DateNormalizerTokenFilterFactory(Map<String,String> args) {
        super(args);
        this.inputFormat = args.containsKey("format") ? (String) args.get("format") : "yyyy-MM-dd";
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
