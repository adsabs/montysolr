

package org.apache.solr.schema;

import org.apache.lucene.search.SortField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.solr.request.XMLWriter;
import org.apache.solr.request.TextResponseWriter;


import invenio.montysolr.jni.PythonBridge;
import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/** <code>TextField</code> is the basic type for configurable text analysis.
 * Analyzers for field types using this implementation should be defined in the schema.
 * @version $Id: TextField.java 764291 2009-04-12 11:03:09Z shalin $
 */
public class PythonTextField extends CompressableField {
  protected void init(IndexSchema schema, Map<String,String> args) {
    properties |= TOKENIZED;
    if (schema.getVersion()> 1.1f) properties &= ~OMIT_TF_POSITIONS;

    super.init(schema, args);
  }

  public SortField getSortField(SchemaField field, boolean reverse) {
    return getStringSort(field, reverse);
  }

  public void write(XMLWriter xmlWriter, String name, Fieldable f) throws IOException {
    xmlWriter.writeStr(name, f.stringValue());
  }

  public void write(TextResponseWriter writer, String name, Fieldable f) throws IOException {
    writer.writeStr(name, f.stringValue(), true);
  }

  public Field createField(SchemaField field, String externalVal, float boost) {

	  //String val = bridge.workoutFieldValue(this.getClass().getName(), field, externalVal, boost);
	  PythonMessage message = MontySolrVM.INSTANCE.createMessage("workout_field_value")
	  				.setSender("PythonTextField")
	  				.setParam("field", field)
	  				.setParam("externalVal", externalVal)
	  				.setParam("boost", boost);

	    try {
			MontySolrVM.INSTANCE.sendMessage(message);
			if (message.containsKey("result")) {
				  String val = (String) message.getResults();
				  if (val != null)
					  return super.createField(field, val, boost);
			  }
		} catch (InterruptedException e) {
			// pass, we will not access the message object it may be
			// in inconsistent state
		}


	  return null;
  }
}
