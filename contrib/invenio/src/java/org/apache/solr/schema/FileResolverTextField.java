

package org.apache.solr.schema;

import org.apache.lucene.document.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/** <code>TextField</code> is the basic type for configurable text analysis.
 * Analyzers for field types using this implementation should be defined in the schema.
 * 
 * This implementation just compares java against puthon.
 */
public class FileResolverTextField extends TextField {


  public Field createField(SchemaField field, String externalVal, float boost) {
	  //System.out.println(externalVal);
	  //String val = externalVal.toLowerCase() + " Hey!"; //null;
	  String val = null;
	  String[] vals = externalVal.split("\\|");
	  Map<String, String> values = new HashMap<String, String>();
	  for (String v: vals) {
		  if (v.indexOf(':') > 0) {
			  String[] parts = v.split(":", 0);
			  String p = parts[1];
			  if (p.startsWith("[") && p.endsWith("]")) {
				  p = p.substring(1, p.length()-1);
			  }
			  values.put(parts[0], p);
		  }
	  }
	  if (values.containsKey("src_dir") && values.containsKey("arxiv_id")) {
		  String[] dirs = values.get("src_dir").split(",");

		  String arx = values.get("arxiv_id");
		  String fname = null;
		  String topdir = null;

		  if (arx.indexOf('/') > -1) {
			  String[] arx_parts = arx.split("/", 0); //hep-th/0002162
			  topdir = arx_parts[1].substring(0, 4);
			  fname = arx_parts[0] + arx_parts[1];
		  }
		  else if(arx.indexOf(':') > -1) {
			  String[] arx_parts = arx.replace("arXiv:", "").split("\\.", 0); //arXiv:0712.0712
			  topdir = arx_parts[0];
			  fname = arx_parts[0] + '.' + arx_parts[1];
		  }

		  if (fname != null) {
			  File f = null;
			  for (String d: dirs) {
				  String s = d + "/" + topdir + "/" + fname;
				  f = new File(s + ".txt");
				  if (f.exists()) {
					  StringBuilder text = new StringBuilder();
					    String fEncoding = "UTF-8";
					    String NL = System.getProperty("line.separator");
						Scanner scanner;
						try {
							scanner = new Scanner(new FileInputStream(f), fEncoding );
							try {
						      while (scanner.hasNextLine()){
						        text.append(scanner.nextLine() + NL);
						      }
						    }
						    finally{
						      scanner.close();
						      val = text.toString();
						    }
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

				  break;
				  }
			  }
		  }
	  }// value has src_dir and arxiv_id

	  return super.createField(field, val, boost);
  }
}
