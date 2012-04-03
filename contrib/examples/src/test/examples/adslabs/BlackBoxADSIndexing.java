package examples.adslabs;

import org.junit.BeforeClass;

import examples.BlackBoxAbstractTestCase;


public class BlackBoxADSIndexing extends BlackBoxAbstractTestCase{
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		setEName("adslabs");
		exampleInit();
	}
	
	public void testPython() {
		System.out.println("hi!");
	}
	
	

	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxADSIndexing.class);
    }
}
