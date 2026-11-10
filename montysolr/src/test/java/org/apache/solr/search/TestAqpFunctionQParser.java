package org.apache.solr.search;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.apache.solr.common.params.ModifiableSolrParams;

import java.util.Collections;

public class TestAqpFunctionQParser extends LuceneTestCase {
    private int parseInteger(String value) throws Exception {
        AqpFunctionQParser parser = new AqpFunctionQParser("", null, new ModifiableSolrParams(), null);
        parser.setQueryNode(new AqpFunctionQueryNode("pos", null,
                Collections.singletonList(new OriginalInput(value, 0, value.length()))));
        return parser.parseInt();
    }

    public void testIntegerBoundsAndSyntaxErrors() throws Exception {
        assertEquals(Integer.MAX_VALUE, parseInteger("2147483647"));
        assertEquals(Integer.MIN_VALUE, parseInteger("-2147483648"));
        for (String value : new String[]{"", "\"\"", "author:\"Example, A\"",
                "2147483648", "-2147483649", "'2'"}) {
            SyntaxError error = expectThrows(SyntaxError.class, () -> parseInteger(value));
            assertTrue(error.getCause() instanceof NumberFormatException);
        }
    }
}
