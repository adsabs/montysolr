/**
 * 
 */
package invenio.montysolr.jni;


/**
 * All the bridge implementation between Java<->Python are required to have
 * these basic methods
 * 
 * @author rca
 *
 */
public interface PythonBridge {
	
	/**
	 * Returns the name of this Bridge implementation
	 * @return
	 */
	public String getName();
	
	/**
	 * Sets the name of the bridge after the Bridge was instantiated by the
	 * PythonVMBridge
	 * @void
	 */
	public void setName(String name);
	
	
	/**
	 * Generic method for sending a PythonMessage into the remote JNI side
	 * @param message
	 */
	public void sendMessage(PythonMessage message);
	
}
