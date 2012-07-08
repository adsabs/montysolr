package invenio.montysolr;

import java.util.ArrayList;
import java.util.List;

import invenio.montysolr.jni.MontySolrVM;
import org.eclipse.jetty.start.Main;

import invenio.montysolr.util.MontySolrSetup;

/*
 * A simple wrapper for the default Solr start.jar
 * 
 * We load the JCC and then pass command
 */
public class Start {
	
	public static void main(String[] params) throws Exception {
		
		List<String> jettyParams = new ArrayList<String>();
		boolean testPython = false;
		for (int i = 0; i < params.length; i++) {
			String t = params[i];
			if (t.contains("--discover-jcc")) {
				MontySolrSetup.checkJCCPath();
			}
			else if (t.contains("--test-python")) {
				testPython = true;
			}
			else {
				jettyParams.add(t);
			}
		}
		
		
		params = new String[jettyParams.size()];
		jettyParams.toArray(params);

		// This must happen in the main thread		
		MontySolrVM.INSTANCE.start("montysolr_java");
		
		if (testPython==true) {
			MontySolrSetup.evalCommand("import montysolr,sys; sys.stderr.write('MontySolr=%s\\n' % montysolr.__file__)");
		}

		Main.main(params);
	}

}
