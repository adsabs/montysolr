package invenio.montysolr;

import java.io.IOException;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonBridge;
import invenio.montysolr.jni.PythonMessage;

import org.apache.jcc.PythonException;
import invenio.montysolr.util.MontySolrTestCaseJ4;
import invenio.montysolr.util.MontySolrAbstractTestCase;

public class TestMontySolrBasicOperations extends MontySolrAbstractTestCase {
	
	

	/**
	   * Must return a fully qualified name of the python module to load, eg:
	   * 
	   * "montysolr.tests.basic"
	   * 
	   */
	@Override
	public String getModuleName() {
		return "montysolr.tests.basic.bridge.Bridge";
	}

	@Override
	public String getSchemaFile() {
		return "schema-minimal.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return "solrconfig-diagnostic-test.xml";
	}

	public void testBasicOperations() throws IOException, InterruptedException {
		
		
		
		PythonMessage message = MontySolrVM.INSTANCE.createMessage(
				"diagnostic_test").setParam("query", "none");

		try {
			MontySolrVM.INSTANCE.sendMessage(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException("Error calling MontySolr!");
		}

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
		try {
			MontySolrVM.INSTANCE.sendMessage(message);
			MontySolrVM.INSTANCE.sendMessage(message);
			MontySolrVM.INSTANCE.sendMessage(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException("Error multiple calling MontySolr!");
		}
		
		message = MontySolrVM.INSTANCE.createMessage(
			"unknown_call").setParam("query", "none");
		
		PythonBridge ba = MontySolrVM.INSTANCE.getBridge();
		try {
			MontySolrVM.INSTANCE.sendMessage(message);
			assertSame(ba, MontySolrVM.INSTANCE.getBridge());
			MontySolrVM.INSTANCE.sendMessage(message);
			assertSame(ba, MontySolrVM.INSTANCE.getBridge());
			MontySolrVM.INSTANCE.sendMessage(message);
			assertSame(ba, MontySolrVM.INSTANCE.getBridge());
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException("Error multiple calling MontySolr!");
		}
		
		try {
			MontySolrVM.INSTANCE.evalCommand("import sys;sys.path.insert(0,\'XYZW\')" );
			message = MontySolrVM.INSTANCE.createMessage(
				"diagnostic_test").setParam("query", "none");
			MontySolrVM.INSTANCE.sendMessage(message);
			res = (String) message.getResults();
			assertTrue("Diagnostic test returned unexpected results!", 
					res.contains("PYTHONPATH") && res.contains("XYZW"));
		} catch (InterruptedException e1) {
			throw new IOException("Error evaluating Python command!");
		}
		
		
		// now sets the sys.path and change the montysolr.bridge which
		// should get a new bridge instance
		PythonBridge b = MontySolrVM.INSTANCE.getBridge();
		
		try {
			MontySolrVM.INSTANCE.evalCommand("import sys;sys.path.insert(0,\'" + 
					MontySolrTestCaseJ4.MONTYSOLR_HOME + "/src/python/montysolr/tests\')" );
		} catch (InterruptedException e1) {
			throw new IOException("Error evaluating Python command!");
		}
		
		System.setProperty("montysolr.bridge", "basic.bridge.Bridge");
		message = MontySolrVM.INSTANCE.createMessage(
			"diagnostic_test").setParam("query", "none");
		
		try {
			MontySolrVM.INSTANCE.sendMessage(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException("Error multiple calling MontySolr!");
		}
		
		PythonBridge b2 = MontySolrVM.INSTANCE.getBridge();
		
		assertNotSame(b, b2);
		
		
		try {
			MontySolrVM.INSTANCE.evalCommand("print sys.path + 1 "); //wrong operation
			fail("Wrong python exec did not raise error");
		} catch (PythonException e) {
			// this is OK
		}

		try {
			MontySolrVM.INSTANCE.evalCommand("print sys.path + 1 "); //wrong syntax
			fail("Wrong python exec did not raise error");
		} catch (PythonException e) {
			// this is OK
		}
		
		
	}

}
