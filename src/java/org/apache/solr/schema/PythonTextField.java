

package org.apache.solr.schema;

import org.apache.lucene.document.Field;


import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;


/** <code>TextField</code> is the basic type for configurable text analysis.
 * Analyzers for field types using this implementation should be defined in the schema.
 */
public class PythonTextField extends TextField {
  public Field createField(SchemaField field, String externalVal, float boost) {

	  //String val = bridge.workoutFieldValue(this.getClass().getName(), field, externalVal, boost);
	  PythonMessage message = MontySolrVM.INSTANCE.createMessage("workout_field_value")
	  				.setSender("PythonTextField")
	  				.setParam("field", field)
	  				.setParam("externalVal", externalVal)
	  				.setParam("boost", boost);

	    try {
			MontySolrVM.INSTANCE.sendMessage(message);
			Object res = message.getResults();
			if (res != null) {
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
