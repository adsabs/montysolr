package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.queryparser.flexible.aqp.AqpInvenioQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpTestAbstractCase;
import org.apache.lucene.util.Version;

/**
 * Tests that invenio grammar is able to handle the 'strange'
 * cases.
 */

public class TestAqpInvenioSimple extends AqpTestAbstractCase {
	
	public void setUp() throws Exception {
		super.setUp();
	}
	
	public AqpQueryParser getParser() throws Exception {
		AqpQueryParser qp = AqpInvenioQueryParser.init();
		qp.setDebug(this.debugParser);
		return qp;
	}

	public void testAmbiguity() throws Exception {
	  WhitespaceAnalyzer wsa = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
		assertQueryEquals("e(+)e(-)", wsa, "e(+)e(-)");
		assertQueryEquals("x:e(+)e(-)", wsa, "x:e(+)e(-)");
		assertQueryEquals("# e(+)e(-)", wsa, "<(ints,recid)e(+)e(-)>");
	}
	
	public void testSwitchingQuery() throws Exception {
		assertQueryEquals("f:this #f:query", null, "f:this <(ints,recid)f:query>");
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpInvenioSimple.class);
    }
}
