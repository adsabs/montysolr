package invenio.montysolr.jni;



import org.apache.jcc.PythonVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This class is used for calling Invenio from inside JavaVM.
 * It is the crucial piece of native link between Python and
 * Java. But the implementation details are taken care of by
 * @{link PythonVM}
 * 
 * @author rca
 * 
 */

public class MontySolrBridge implements PythonBridge {
	
	public static final Logger log = LoggerFactory.getLogger(MontySolrBridge.class);
	
	private long pythonObject;
	
	protected String bridgeName = null;
	
	public String getName() {
		return this.bridgeName;
	}
	public void setName(String name) {
		this.bridgeName = name;
	}

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
	public void sendMessage(PythonMessage message) {
		PythonVM vm = PythonVM.get();
		vm.acquireThreadState();
		receive_message(message);
		vm.releaseThreadState();
	}
	public native void receive_message(PythonMessage message);
	
	
	/**
	 * The main method for chaning Python environment
	 * @param pythonString
	 */
	public void evalCommand(String pythonString) {
		PythonVM vm = PythonVM.get();
		vm.acquireThreadState();
		eval_command(pythonString);
		vm.releaseThreadState();
	}
	public native void eval_command(String pythonString);
	

}



