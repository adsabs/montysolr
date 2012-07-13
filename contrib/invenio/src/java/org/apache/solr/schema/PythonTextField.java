package org.apache.solr.schema;

import java.util.Map;

import montysolr.jni.MontySolrVM;
import montysolr.jni.PythonMessage;

import org.apache.lucene.index.IndexableField;

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
	    super.init(schema, args);
	}
	
	public IndexableField createField(SchemaField field, Object value, float boost) {
	  return super.createField(field, getFieldValue(field.name, toInternal(value.toString())), boost);
	}
	
	protected IndexableField createField(String name, String val, org.apache.lucene.document.FieldType type, float boost){
    return super.createField(name, getFieldValue(name, val), type, boost);
  }
	
	private String getFieldValue(String field, String externalVal) {

		PythonMessage message = MontySolrVM.INSTANCE
				.createMessage(pythonFunctionName)
				.setSender("PythonTextField")
				.setParam("field", field)
				.setParam("externalVal", externalVal);

		MontySolrVM.INSTANCE.sendMessage(message);
		
		Object res = message.getResults();
		
		if (res != null) {
			return (String) res;
		}

		return null;
	}
}
