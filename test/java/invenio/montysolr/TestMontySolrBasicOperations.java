package invenio.montysolr;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;

import org.apache.solr.util.MontySolrAbstractTestCase;

public class TestMontySolrBasicOperations extends MontySolrAbstractTestCase {


	@Override
	public String getSchemaFile() {
		return "schema-minimal.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return "solrconfig-diagnostic-test.xml";
	}

	public void testBasicOperations() throws IOException {

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
		assertTrue("Diagnostic test returned unexpected results!", 
				res.contains("PYTHONPATH") && res.contains(":diagnostic_test -->"));
		
		assertQ("nope",
				req("qt", "/diagnostic_test", "q", "nope"),
				"//lst/int"); //TODO: get xpath correctly

	}

}
