package monty.solr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonMessage;
import monty.solr.util.ProcessUtils;

import org.eclipse.jetty.start.Main;
//import org.eclipse.jetty.webapp.WebAppContext;

/*
 * A simple wrapper for the default Solr start.jar
 * 
 * We load the JCC and then pass commands to Jetty
 */
public class Start {
	
	public static void main(String[] params) throws Exception {
		
	  //WebAppContext.setSystemClasses(new String[]{"monty."}); 
	  
		List<String> jettyParams = new ArrayList<String>();
		
		boolean jccStarted = false;
		
		for (int i = 0; i < params.length; i++) {
			String t = params[i];
			if (t.contains("--discover-jcc")) {
			  
				i++;
				if (i>=params.length) {
					throw new IllegalStateException("Usage: java -jar monty_start.jar --discover-jcc <python-executable>");
				}
				String python = params[i];
				if ((new File(python)).canExecute() == false) {
					throw new IllegalStateException("The python interpreter is not valid: " + python);
				}
				ProcessUtils.checkJCCPath(python);
				
			  // This must happen in the main thread    
		    MontySolrVM.INSTANCE.start("montysolr_java");
		    jccStarted = true;
				
				PythonMessage message = MontySolrVM.INSTANCE
  	      .createMessage("diagnostic_test")
  	      .setParam("query", "python-init");
				MontySolrVM.INSTANCE.sendMessage(message);
				
				Object result = message.getResults();
		    if (result != null) {
		      String res = (String) result;
		      System.err.println("Diagnostic message: \n" + res);
		    }
		    
			}
			else {
				jettyParams.add(t);
			}
		}
		
		
		params = new String[jettyParams.size()];
		jettyParams.toArray(params);

		// This must happen in the main thread
		if (!jccStarted)
		  MontySolrVM.INSTANCE.start("montysolr_java");
		

		Main.main(params);
	}
	
}
