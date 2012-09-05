package org.apache.solr.schema;

import java.util.Map;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonMessage;

import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.StorableField;

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
	protected void init(IndexSchema schema, Map<String, String> args) {
		String pythonImpl = args.get("pythonFunctionName");
		if (pythonImpl != null) {
			pythonFunctionName = pythonImpl;
		}
		args.remove("pythonFunctionName");
		super.init(schema, args);
	}

	public StorableField createField(SchemaField field, Object value,
			float boost) {
		value = getFieldValue(field.name, toInternal(value.toString()));
		if (value == null)
			return null;
		return super.createField(field, value, boost);
	}

	private String getFieldValue(String field, String externalVal) {

		PythonMessage message = MontySolrVM.INSTANCE
				.createMessage(pythonFunctionName).setSender("PythonTextField")
				.setParam("field", field).setParam("externalVal", externalVal);

		MontySolrVM.INSTANCE.sendMessage(message);

		Object res = message.getResults();

		if (res != null) {
			return (String) res;
		}

		return null;
	}
}
