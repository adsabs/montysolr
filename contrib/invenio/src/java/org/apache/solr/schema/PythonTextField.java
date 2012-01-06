package org.apache.solr.schema;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.solr.common.ResourceLoader;
import org.apache.solr.util.plugin.ResourceLoaderAware;

import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

/**
 * <code>TextField</code> is the basic type for configurable text analysis.
 * Analyzers for field types using this implementation should be defined in the
 * schema.
 * 
 * {@link PythonTextField} is a pythonic extension, the code responsibe for
 * retrieving the text is in the module monty_invenio.scheme.targets
 */
public class PythonTextField extends TextField {
	private String pythonFunctionName = "get_field_value";
	
	@Override
	protected void init(IndexSchema schema, Map<String,String> args) {
		String pythonImpl = args.get("pythonFunctionName");
	    if (pythonImpl != null) {
	      pythonFunctionName = pythonImpl;
	    }
	    args.remove("pythonFunctionName");
	}
	
	
	public Field createField(SchemaField field, String externalVal, float boost) {

		PythonMessage message = MontySolrVM.INSTANCE
				.createMessage(pythonFunctionName)
				.setSender("PythonTextField")
				.setParam("field", field)
				.setParam("externalVal", externalVal)
				.setParam("boost", boost);

		MontySolrVM.INSTANCE.sendMessage(message);
		
		Object res = message.getResults();
		
		if (res != null) {
			String val = (String) res;
			if (val != null)
				return super.createField(field, val, boost);
		}

		return null;
	}
}
