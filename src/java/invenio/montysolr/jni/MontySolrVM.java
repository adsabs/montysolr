package invenio.montysolr.jni;

import org.apache.jcc.PythonException;
import org.apache.jcc.PythonVM;



import java.util.concurrent.Semaphore;

public enum MontySolrVM {
	INSTANCE;
	
	private PythonVM vm = null;
	
	private Semaphore semaphore = 
		new Semaphore((System.getProperty("montysolr.max_workers") != null ? new Integer(System.getProperty("montysolr.max_workers")) : 1), true);
	
	public PythonVM start(String programName) {
		if (vm == null)
			vm = PythonVM.start(programName);
		return vm;
	}
	
	/**
	 * Creates a new instance of the bridge over the Python waters.
	 * This instance can be used to send the PythonMessage, but until
	 * sendMessage is called, it does nothing
	 * @return {@link PythonBridge}
	 */
	public PythonBridge getBridge() {
		return PythonVMBridge.start();
	}
	
	/**
	 * Creates a PythonMessage that wraps all the parameters that will be delivered
	 * to the remote side. It will also contain any return value
	 * @param receiver
	 * @return void
	 */
    public PythonMessage createMessage(String receiver) {
    	return new PythonMessage(receiver);
    }
    
    /**
     * Passes the message over to the remote site, this method is just a factory
     * the passing is done by the Bridge itself
     * @throws InterruptedException 
     */
    
    public void sendMessage(PythonMessage message) throws InterruptedException {
    	PythonBridge b = getBridge();
    	try {
			semaphore.acquire();
			b.sendMessage(message);
    	} finally {
    		semaphore.release();
    	}
    	
    }
    
	

}

/**
 * This class MUST NOT be a singleton. It serves the purpose of communicating w 
 * Python VM. You get the bridge and use methods of the bridge.
 * 
 * @author rca
 *
 */

class PythonVMBridge {
    static protected PythonBridge bridge;
    
    protected PythonVMBridge() {
    	
    }
    
    static public PythonBridge start() throws PythonException {
    	if (System.getProperty("python.bridge") == null) {
    		return start("SimpleBridge");
    	}
    	else {
    		return start(System.getProperty("python.bridge"));
    	}
    }
    
    static public PythonBridge start(String bridgeName) throws PythonException {
    	if (bridgeName == "montysolr.java_bridge.SimpleBridge" ||
    			bridgeName == "SimpleBridge") {
    		return start("montysolr.java_bridge", "SimpleBridge");
    	}
		return bridge;
    }

    static public PythonBridge start(String moduleName, String className) throws PythonException 
    {
        if (bridge == null)
        {
        	PythonVM vm = PythonVM.get();
        	bridge = (PythonBridge)vm.instantiate(moduleName, className);
        	bridge.setName(moduleName+'.'+className);
        }

        return bridge;
    }
    
    static public PythonBridge get() throws PythonException {
    	return start("SimpleBridge");
    	//return bridge;
    }
    

}


