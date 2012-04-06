package org.apache.lucene.queryParser.aqp;

/**
 * Tests that invenio grammar is able to handle the 'strange'
 * cases.
 */

public class TestAqpInvenioSimple extends AqpTestAbstractCase {
	
	public void setUp() throws Exception {
		super.setUp();
		//setDebug(true);
	}
	
	public AqpQueryParser getParser() throws Exception {
		AqpQueryParser qp = new AqpInvenioQueryParser();
		return qp;
	}

	public void testAmbiguity() throws Exception {
		assertQueryEquals("e(+)e(-)", null, "e(+)e(-)");
		assertQueryEquals("x:e(+)e(-)", null, "x:e(+)e(-)");
		assertQueryEquals("# e(+)e(-)", null, "<(ints,recid)e(+)e(-)>");
	}
	
	public void testSwitchingQuery() throws Exception {
		assertQueryEquals("f:this #f:query", null, "f:this <(ints,recid)f:query>");
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAqpInvenioSimple.class);
    }
}
