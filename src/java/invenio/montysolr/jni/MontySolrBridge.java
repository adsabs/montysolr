package invenio.montysolr.jni;



import org.apache.jcc.PythonVM;



/**
 * This class is used for calling Invenio from inside JavaVM
 * 
 * @author rca
 * 
 */

public class MontySolrBridge extends BasicBridge implements PythonBridge {
	
	private long pythonObject;
	protected String bridgeName;
	

	public void pythonExtension(long pythonObject) {
		this.pythonObject = pythonObject;
	}

	public long pythonExtension() {
		return this.pythonObject;
	}

	public void finalize() throws Throwable {
		pythonDecRef();
	}

	public native void pythonDecRef();

	
	
	/**
	 * The main method that passes the PythonMessage instance
	 * to the remote site over the JNI/JCC bridge
	 * @param message
	 */
	@Override
	public void sendMessage(PythonMessage message) {
		PythonVM vm = PythonVM.get();
		vm.acquireThreadState();
		receive_message(message);
		vm.releaseThreadState();
	}
	public native void receive_message(PythonMessage message);
	
	
	/**
	 * Just some testing methods, should be removed after
	 * the code stabilizes
	 */
	public native void test_print();
	public native String test_return_string();

}



