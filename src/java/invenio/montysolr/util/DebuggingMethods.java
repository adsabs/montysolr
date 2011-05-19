package invenio.montysolr.util;

import org.apache.jcc.PythonVM;

public class DebuggingMethods {
	
	public static void discoverImportableModules() {
		String[] modules = {"montysolr_java", "montysolr_java.solrpye.invenio", "montysolr_java.solrpye", "solrpye.invenio"};
        String[] classes = {"TestX", "InvenioSolrBridge", "Emql"};
        Object m = null;
        PythonVM vm = PythonVM.get();
        for (int i=0;i<modules.length;i++) {
        	for (int ii=0;ii<classes.length;ii++) {
	        	try {
		        	m = vm.instantiate(modules[i], classes[ii]);
		        	System.out.println("Success importing module " + modules[i] + " class " + classes[ii]);
		        }
		        catch (Exception e) {
					System.err.println("Error importing module " + modules[i] + " class " + classes[ii]);
				}
        	}
        }
	}

}
