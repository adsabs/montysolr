package monty.solr.jni;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PythonMessage represents the core communication mechanism between
 * Java side and (remote) Python side. PythonMessage extends 
 * {@link HashMap} and on the remote Python side, all public methods
 * of the message are visible.
 * 
 * <p>
 * 
 * You can put any type of object inside the message, but there are
 * also some public methods that help you to retrieve (properly casted
 * Solr object) on the Python side.
 * 
 * <p>
 * 
 * Even if you can put any key/value object inside the PythonMessage,
 * you should not be doing it. Always use the getters/setters when
 * possible.
 *
 * <p>
 * 
 * And be aware, that from inside Python, you can insert into the 
 * message instance <b>only Java instances</b>. Sometimes, JCC will
 * automatically convert the Python object into the Java instance,
 * however if something doesn't work, try to directly create a Java
 * object and place it into the PythonMessage.
 * 
 * <p>
 * <b>
 * WARNING: bad things happen if you are trying to retrieve 
 * and cast wrong objects. You must know which key has the 
 * correct type of the value! We cannot by typesafe, this class
 * is used for JNI communication.
 * </b>
 *
 */

public class PythonMessage extends HashMap<String, Object>{

	private static final long serialVersionUID = -3744935985066647405L;
	public static final Logger log = LoggerFactory.getLogger(PythonMessage.class);
	
	public static final String RECEIVER = "receiver";
	public static final String SENDER = "sender";
	public static final String RESULTS = "#result";

	public PythonMessage(String receiver) {
		this.put(RECEIVER, receiver);
	}
	
	/**
	 * @return The name of the class that is sending the message (or null if not set)
	 */
	public String getSender() {
		return (String) this.get(SENDER);
	}
	
	/**
	 * Set the name of the sender (usually the fully qualified name #class.getName())
	 * @return PythonMessage instance
	 */
	public PythonMessage setSender(String sender) {
		if (sender == null) {
			throw new IllegalStateException("Sender must not be null");
		}
		this.put(SENDER, sender);
		return this;
	}
	
	/**
	 * @return Returns the name of the receiver
	 */
	public String getReceiver() {
		return (String) this.get(RECEIVER);
	}

	/**
	 * Set the name of the receiver (usually the name of the python functions)
	 * This is used by the Python side to find out which API to call.
	 * @return RECEIVER name
	 */
	public PythonMessage setReceiver(String receiver) {
		this.put(RECEIVER, receiver);
		return this;
	}

	
	/**
	 * Sets the paramater of a message
	 * @param name
	 * 	 - key of the value
	 * @param value
	 * @return {@link PythonMessage}
	 */
	public PythonMessage setParam(String name, Object value) {
		this.put(name, value);
		return this;
	}

	
	/**
	 * Gets the paramater from the message, behaves exactly as
	 * {@link HashMap#get(Object)}
	 * @param name
	 * 	 - key of the value
	 */
	public Object getParam(String name) {
		return this.get(name);
	}
	
	
	/**
	 * Gets the paramater from the message, behaves exactly as
	 * (Ingeger) {@link HashMap#get(Object)}
	 * @param name
	 * 	 - key of the value
	 * @param defVal
	 * 	 - default value which is returned if the key was not 
	 *     found
	 * 
	 * <b>
	 * WARNING: bad things happen if you are trying to retrieve 
	 * and cast wrong objects. You must know which key has the 
	 * correct type of the value! We cannot by typesafe, this is
	 * meant for JNI communication.
	 * </b>
	 */
	public int getParam_int(String name, int defVal) {
		if (this.containsKey(name)) {
			return (Integer) this.get(name);
		}
		return defVal;
	}
	
	/**
	 * Gets the paramater from the message, behaves exactly as
	 * (String) {@link HashMap#get(Object)}
	 * @param name
	 * 	 - key of the value
	 * @param defVal
	 * 	 - default value which is returned if the key was not 
	 *     found
	 *     
	 * <b>
	 * WARNING: bad things happen if you are trying to retrieve 
	 * and cast wrong objects. You must know which key has the 
	 * correct type of the value! We cannot by typesafe, this is
	 * meant for JNI communication.
	 * </b>
	 */
	public String getParam_str(String name, String defVal) {
		if (this.containsKey(name)) {
			return (String) this.get(name);
		}
		return defVal;
	}

	
	/**
	 * Gets the paramater from the message, behaves exactly as
	 * (int[]) {@link HashMap#get(Object)}
	 * @param name
	 * 	 - key of the value
	 * <b>
	 * WARNING: bad things happen if you are trying to retrieve 
	 * and cast wrong objects. You must know which key has the 
	 * correct type of the value! We cannot by typesafe, this is
	 * meant for JNI communication.
	 * </b>
	 * 
	 * @deprecated use getParamArray_int(String name, int defVal)
	 */
	public int[] getParamArray_int(String name) {
		return (int[]) this.get(name);
	}
	
	
	
	/**
	 * Gets the paramater from the message, behaves exactly as
	 * (int[]) {@link HashMap#get(Object)}
	 * @param name
	 * 	 - key of the value
	 * @param defVal
	 * 	 - default values to return when null
	 * <b>
	 * WARNING: bad things happen if you are trying to retrieve 
	 * and cast wrong objects. You must know which key has the 
	 * correct type of the value! We cannot by typesafe, this is
	 * meant for JNI communication.
	 * </b>
	 */
	public int[] getParamArray_int(String name, int[] defVal) {
		if (this.containsKey(name)) {
			return (int[]) this.get(name);
		}
		return defVal;
	}
	
	/**
	 * Gets the paramater from the message, behaves exactly as
	 * (int[]) {@link HashMap#get(Object)}
	 * @param name
	 * 	 - key of the value
	 * <b>
	 * WARNING: bad things happen if you are trying to retrieve 
	 * and cast wrong objects. You must know which key has the 
	 * correct type of the value! We cannot by typesafe, this is
	 * meant for JNI communication.
	 * </b>
	 * 
	 * @deprecated use getParamArray_int(String name, String defVal)
	 */
	public String[] getParamArray_str(String name) {
		return (String[]) this.get(name);
	}
	
	
	/**
	 * Gets the paramater from the message, behaves exactly as
	 * (String[]) {@link HashMap#get(Object)}
	 * @param name
	 * 	 - key of the value
	 * @param defVal
	 * 	 - default values to return when null
	 * <b>
	 * WARNING: bad things happen if you are trying to retrieve 
	 * and cast wrong objects. You must know which key has the 
	 * correct type of the value! We cannot by typesafe, this is
	 * meant for JNI communication.
	 * </b>
	 */
	public String[] getParamArray_str(String name, String[] defVal) {
		if (this.containsKey(name)) {
			return (String[]) this.get(name);
		}
		return defVal;
	}
	
	/**
	 * Returns the results of the Python computation
	 *  {@link HashMap#get(RESULTS)}
	 * @return
	 */
	public Object getResults() {
		return this.get(RESULTS);
	}
	
	
	
	/**
	 * Sets the results of the Python computation
	 *  {@link HashMap#put(RESULTS, result)}
	 * @return
	 */
	public void setResults(Object result) {
		this.put(RESULTS, result);
	}


	public String toString() {
		Set<Entry<String, Object>> s = this.entrySet();
		StringBuilder out = new StringBuilder();
		for (Entry<String, Object> e: s) {
			out.append(e.getKey());
			out.append("=");
			Object v = e.getValue();
			if (v instanceof AbstractCollection) {
				if (((AbstractCollection) v).size() > 10) {
					out.append("@" + v.getClass());
				}
				else {
					out.append(v);
				}
			}
			else {
				out.append(v);
			}
			out.append(",");
		}
		return out.toString();
	}

	
	/**
	 * Method used for debugging, it prints (into log) 
	 *   message + 
	 *   information about the current thread, its name, id and actual time
	 * @param s
	 * 	- the message you want to print
	 */
	public void threadInfo(String s) {
		log.info("[Python] " + s + this.getInfo());
	}

	private String getInfo() {
		return "  [Thread=" + Thread.currentThread().getName() + " id=" + Thread.currentThread().getId() +
				" time=" + System.currentTimeMillis() + "]";
	}
	
	
	public void setSolrQueryRequest(SolrQueryRequest req) {
		throw new IllegalStateException("This method has been removed, please use the common getParam()/setParam()");
	}
	public SolrQueryRequest getSolrQueryRequest(SolrQueryRequest req) {
		throw new IllegalStateException("This method has been removed, please use the common getParam()/setParam()");
	}
	public void setSolrQueryResponse(SolrQueryResponse rsp) {
		throw new IllegalStateException("This method has been removed, please use the common getParam()/setParam()");
	}
	public SolrQueryResponse setSolrQueryResponse(SolrQueryRequest rsp) {
		throw new IllegalStateException("This method has been removed, please use the common getParam()/setParam()");
	}
}
