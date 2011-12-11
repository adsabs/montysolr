package org.apache.lucene.queryParser.aqp;

public class TestAqpInvenioSimple extends TestAqpAbstractCase {
	
	public void setUp() throws Exception {
		super.setUp();
		setDebug(false);
	}
	
	@Override
	public AqpQueryParser getParser() throws Exception {
		AqpQueryParser qp = new AqpInvenioQueryParser();
		return qp;
	}

	public void testAmbiguity() throws Exception {
		super.assertQueryEquals("e(+)e(-)", null, "e(+)e(-)");
		super.assertQueryEquals("x:e(+)e(-)", null, "x:e(+)e(-)");
		super.assertQueryEquals("# e(+)e(-)", null, "<field:e(+)e(-)>");
	}
	
	public void testSwitchingQuery() throws Exception {
		super.assertQueryEquals("f:this #f:query", null, "f:this <f:query>");
	}
}
