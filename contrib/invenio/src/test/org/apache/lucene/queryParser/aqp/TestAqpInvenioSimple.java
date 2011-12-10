package org.apache.lucene.queryParser.aqp;

public class TestAqpInvenioSimple extends TestAqpAbstractCase {

	@Override
	public AqpQueryParser getParser() throws Exception {
		AqpQueryParser qp = new AqpQueryParserInvenio();
		return qp;
	}

	public void testAmbiguity() throws Exception {
		setDebug(true);
		super.assertQueryEquals("e(+)e(-)", null, "e(+)e(-)");
	}
}
