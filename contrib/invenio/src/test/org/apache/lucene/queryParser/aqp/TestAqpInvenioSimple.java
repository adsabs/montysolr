package org.apache.lucene.queryParser.aqp;

public class TestAqpInvenioSimple extends TestAqpAbstractCase {
	
	public void setUp() throws Exception {
		super.setUp();
		setDebug(false);
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
}
