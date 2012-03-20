package invenio.montysolr;

import java.io.IOException;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonBridge;
import invenio.montysolr.jni.PythonMessage;

import org.apache.jcc.PythonException;
import org.junit.BeforeClass;

import invenio.montysolr.util.MontySolrSetup;
import invenio.montysolr.util.MontySolrTestCaseJ4;
import invenio.montysolr.util.MontySolrAbstractTestCase;

public class TestMontySolrBasicOperations extends MontySolrAbstractTestCase {
	
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		MontySolrSetup.init("montysolr.tests.basic.bridge.Bridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
	}
	

	@Override
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome() + "/src/test-files/solr/conf/" +
		"schema-minimal.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + "/src/test-files/solr/conf/" +
		"solrconfig-diagnostic-test.xml";
	}

	public void testBasicOperations() throws IOException {
		
		
		boolean caught = false;
		
		PythonMessage message = MontySolrVM.INSTANCE.createMessage(
				"diagnostic_test").setParam("query", "none");

		MontySolrVM.INSTANCE.sendMessage(message);

		Object result = message.getResults();
		assertNotNull(result);
		String res = (String) result;
		assertTrue("Diagnostic test returned unexpected results, or diagnostic_test was overwritten!", 
				res.contains("PYTHONPATH") && res.contains("sys.path")
				&& res.contains("PYTHONPATH"));
		
		assertQ("nope",
				req("qt", "/diagnostic_test", "q", "nope"),
				"//lst/int"); //TODO: get xpath correctly
		
		//send several messages (the same)
		MontySolrVM.INSTANCE.sendMessage(message);
		MontySolrVM.INSTANCE.sendMessage(message);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		// can't use solr log facility
		System.err.println("Please ignore ERROR messages, if any...");
		caught = false;
		try {
			message = MontySolrVM.INSTANCE.createMessage(
				"unknown_call").setParam("query", "none");
			
			MontySolrVM.INSTANCE.sendMessage(message);
		}
		catch (RuntimeException e) {
			// this is OK
			caught = true;
		}
		assertTrue(caught);
		System.err.println("...after now ERROR messages are again relevant");
		
		PythonBridge ba = MontySolrVM.INSTANCE.getBridge();
		assertSame(ba, MontySolrVM.INSTANCE.getBridge());
		
		caught = false;
		try {
			MontySolrVM.INSTANCE.sendMessage(message);
		} 
		catch (PythonException e) {
			caught = true;
		}
		assertTrue(caught);
		
		PythonBridge bb = MontySolrVM.INSTANCE.getBridge();
		assertSame(bb, MontySolrVM.INSTANCE.getBridge());
		
		
		// evaluate python string
		MontySolrVM.INSTANCE.evalCommand("import sys;sys.path.insert(0,\'XYZW\')" );
		message = MontySolrVM.INSTANCE.createMessage(
			"diagnostic_test").setParam("query", "none");
		MontySolrVM.INSTANCE.sendMessage(message);
		res = (String) message.getResults();
		assertTrue("Diagnostic test returned unexpected results!", 
				res.contains("PYTHONPATH") && res.contains("XYZW"));
		
		
		// now sets the sys.path and change the montysolr.bridge which
		// should get a new bridge instance
		PythonBridge b = MontySolrVM.INSTANCE.getBridge();
		
		MontySolrVM.INSTANCE.evalCommand("import sys;sys.path.insert(0,\'" + 
				MontySolrTestCaseJ4.MONTYSOLR_HOME + "/src/python/montysolr/tests\')" );
		
		System.setProperty("montysolr.bridge", "basic.bridge.Bridge");
		message = MontySolrVM.INSTANCE.createMessage(
			"diagnostic_test").setParam("query", "none");
		
		// just for fun do it again
		MontySolrVM.INSTANCE.sendMessage(message);
		
		PythonBridge b2 = MontySolrVM.INSTANCE.getBridge();
		
		assertNotSame(b, b2);
		
		
		caught = false;
		try {
			MontySolrVM.INSTANCE.evalCommand("print sys.path + 1 "); //wrong operation
			fail("Wrong python exec did not raise error");
		} catch (PythonException e) {
			caught = true;	// this is OK
		}
		assertTrue(caught);

		caught = false;
		try {
			MontySolrVM.INSTANCE.evalCommand("print sys.path + 1 "); //wrong syntax
			fail("Wrong python exec did not raise error");
		} catch (PythonException e) {
			caught = true;	// this is OK
		}
		
		assertTrue(caught);
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestMontySolrBasicOperations.class);
    }

}


