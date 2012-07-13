package montysolr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import montysolr.jni.MontySolrVM;
import montysolr.util.ProcessUtils;

import org.eclipse.jetty.start.Main;


/*
 * A simple wrapper for the default Solr start.jar
 * 
 * We load the JCC and then pass commands to Jetty
 */
public class Start {
	
	public static void main(String[] params) throws Exception {
		
		List<String> jettyParams = new ArrayList<String>();

		for (int i = 0; i < params.length; i++) {
			String t = params[i];
			if (t.contains("--monty-discover-jcc")) {
				i++;
				if (i>params.length) {
					throw new IllegalStateException("Usage: java -jar monty_start.jar --monty-discover-jcc <python-executable>");
				}
				String python = params[i];
				if ((new File(python)).canExecute() == false) {
					throw new IllegalStateException("The python interpreter is not valid: " + python);
				}
				ProcessUtils.checkJCCPath(python);
			}
			else {
				jettyParams.add(t);
			}
		}
		
		
		params = new String[jettyParams.size()];
		jettyParams.toArray(params);

		// This must happen in the main thread		
		MontySolrVM.INSTANCE.start("montysolr_java");
		

		Main.main(params);
	}
	
}
