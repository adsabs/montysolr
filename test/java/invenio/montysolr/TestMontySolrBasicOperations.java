package invenio.montysolr;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonBridge;
import invenio.montysolr.jni.PythonMessage;

import org.apache.jcc.PythonException;
import org.apache.solr.MontySolrTestCaseJ4;
import org.apache.solr.util.MontySolrAbstractTestCase;

public class TestMontySolrBasicOperations extends MontySolrAbstractTestCase {
	
	
	
	@Override
	public String getBridgeName() {
		return "montysolr.tests.bridge.Bridge";
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
			MontySolrVM.INSTANCE.evalCommand("import sys;print sys.path;print sys.argv");
			MontySolrVM.INSTANCE.sendMessage(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException("Error calling MontySolr!");
		}

		Object result = message.getResults();
		assertNotNull(result);
		String res = (String) result;
		assertTrue("Diagnostic test returned unexpected results!", 
				res.contains("PYTHONPATH") && res.contains(":diagnostic_test -->"));
		
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
		
		System.setProperty("montysolr.bridge", "bridge.Bridge");
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
			MontySolrVM.INSTANCE.evalCommand("print (sys.path"); //wrong syntax
		} catch (PythonException e) {
			// this is OK
		}
		
	}

}
