package examples.adslabs;


public class BlackBoxADSIndexing extends BlackBoxADSAbstract{
	
	public void testPython() {
		System.out.println("hi!");
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxADSIndexing.class);
    }
}
