package org.apache.lucene.queryParser.aqp;

public class TestAqpInvenioSimple extends TestAqpAbstractCase {
	
	public void setUp() throws Exception {
		super.setUp();
		setDebug(true);
	}
	
	@Override
	public AqpQueryParser getParser() throws Exception {
		AqpQueryParser qp = new AqpQueryParserInvenio();
		return qp;
	}

	public void testAmbiguity() throws Exception {
		super.assertQueryEquals("e(+)e(-)", null, "e(+)e(-)");
	}
	
	public void testSwitchingQuery() throws Exception {
		super.assertQueryEquals("f:this #f:query", null, "f:this <f:query>");
	}
}
