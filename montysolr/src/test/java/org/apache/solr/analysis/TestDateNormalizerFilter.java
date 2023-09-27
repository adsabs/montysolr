package org.apache.solr.analysis;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;

import java.io.StringReader;
import java.util.HashMap;

public class TestDateNormalizerFilter extends BaseTokenStreamTestCase {

    public void test() throws Exception {
        HashMap<String, String> config = new HashMap<String, String>();
        config.put("format", "yyyy-MM-dd|yy-MM-dd|yy-MM|yyyy");
        DateNormalizerTokenFilterFactory factory = new DateNormalizerTokenFilterFactory(config);

        TokenStream stream;
        stream = factory.create(whitespaceMockTokenizer(new StringReader("2014 2014-00 2014-12 2014-12-01 2014-12-00")));
        assertTokenStreamContents(stream,
                new String[]{
                        "2014-01-01T00:00:00Z",
                        "2014-01-01T00:00:00Z",
                        "2014-12-01T00:00:00Z",
                        "2014-12-01T00:30:00Z",
                        "2014-12-01T00:00:00Z"}

        );
    }
}
