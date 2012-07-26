package monty.solr.jni;

import org.apache.jcc.PythonException;
import org.apache.jcc.PythonVM;



import java.util.concurrent.Semaphore;

/**
 * {@link MontySolrVM} is the main access point for classes that want to
 * exchange data between Java and Python. It is implemented as singleton.
 * 
 * <br/>
 * 
 * You normally do this:
 * 
 * <pre>
 *  PythonMessage message = MontySolrVM.INSTANCE
 *   .createMessage("some_function_x")
 *   .setSender(this.getClass().toString())
 *   .setParam("some-values", value_object);
 *   
 *  MontySolrVM.INSTANCE.sendMessage(message);
 *  </pre>
 *
 *	What happens is:
 *  <ol>
 *  <li> {@link MontySolrVM} creates a message ({@link PythonMessage})</li>
 *  <li> The message is filled with values: recipient name (the function),
 *       the sender name (can be anything), data that the remote Python function
 *       expects. It is up to you to provide correct data. MontySolr has no
 *       easy way to check it for you </li>
 *  <li> A short bridge {@link PythonVMBridge} is established between 
 *       Java VM and PythonVM. The message instance is sent to the Python side</li>
 *  <li> A {@code handler} on the remote side receives the message, inspects
 *       its header and passes it on to the function that implements the
 *       response handler. 
 *       <br/>
 *       Return values must be inserted inside the {@link PythonMessage}
 *       instance. And since it is a Java object, it can access only Java objects.
 *       This is important to know if you are working with it from the Python side.
 *       <br/> 
 *       If no appropriate target was found for the message, the Python handler
 *       usually writes an error message {@link PythonMessage#threadInfo(String)}
 *       and exits.</li>
 *  <li> Exit will close the {@link PythonVMBridge} and garbage collect the data - even
 *       if you still keep reference to the {@link PythonMessage} object on the remote
 *       Python side. <b>But don't do that! They go away after the bridge is closed and
 *       this will only make your Python (and the rest of JavaVM) crash.</b> </li>
 *  <li> After the {@link PythonVMBridge} was closed, {@link MontySolrVM#INSTANCE#sendMessage(PythonMessage)}
 *       returns as well and you can access the results. (you should always use
 *       {@link PythonMessage#getResults()} </li>
 *  </ol>
 *  
 *  <p>
 *  IMPORTANT:
 *  
 *  <p>
 *  
 *  <b>Exception handling</b>
 *  
 *  <p>
 *  
 *  The Python can raise Exceptions - unless they are of the type SystemException (and not 
 *  recoverable) they will be wrapped into {@link PythonException}. However, this is an
 *  unchecked exception! So you must decide yourself whether you catch it
 *  
 *  <b>{@code montysolr.max_workers}</b>
 *  
 *  <p>
 *  Python does not support non-blocking parallel threads due to GIL 
 *  (Global Interpreter Lock). This means that always <b>only one thread</b> can run
 *  at a given time.
 *  
 *  <p>
 *  
 *  Standard solution in Python is to use multiprocessing module (default in
 *  Python >2.5, available separately for 2.4). Multiprocessing means that 
 *  we create <b>several</b> Python interpreter instances and distribute the 
 *  jobs to them. They can run in parallel and will not compete for resources.
 *  And in a limited ways, they can share/exchange data.
 *  
 *  <p>
 *  In order to take advantage of the multiprocessing, you must set 
 *  {@code montysolr.max_workers} system property. 
 *  {@link MontySolrVM} will check this property at startup
 *  {@code montysolr.max_workers} -- if this value parses to Integer, we
 *  will allow that many workers. Otherwise, we assume that there is only
 *  worker and all calls to {@link MontySolrVM#INSTANCE#sendMessage(PythonMessage)}
 *  will be blocking and sequantial (your Java will be waiting until Python
 *  finishes before it will allow another message).
 *  
 *  <p>
 *  
 *  <b>{@code python.bridge}</b>
 * 
 *  <p>
 *  
 *  This system property tells {@link MontySolrVM} what is the name of the 
 *  main Python module. This module is loaded at a startup and serves
 *  as a gate keeper. It receives the {@link PythonMessage} (already wrapped
 *  by a JCC to resemble a Python instance) and passes this message to the
 *  handler (usually).
 *  
 *  <p>
 *  
 *  You can write your own python bridge class. You must reload whole JavaVM
 *  in order to activate it.
 *
 */
public enum MontySolrVM {
	INSTANCE;
	
//	public static final Logger log = LoggerFactory.getLogger(MontySolrVM.class);
	private PythonVM vm = null;
	private int interrupted = 0;
	private int workers = (System.getProperty("montysolr.max_workers") != null 
			? new Integer(System.getProperty("montysolr.max_workers")): 1);
	
	private Semaphore semaphore = new Semaphore(workers, true);
	
	/**
	 * This method must be called from the main thread before the Solr init.
	 * @param programName 
	 * 			name of the program (it is just a symbolic name, at this
	 * 			stage nothing is yet run/initialized and even if the config
	 * 			is wrong, no error will happen). But if the program is a 
	 * 			real file, its parent folder will be added (by Python itself)
	 *          to the PYTHONPATH
	 */
	public PythonVM start(String programName) {
		if (vm == null) {
			vm = PythonVM.start(programName);
		}
		return vm;
	}
	
	/**
	 * This method must be called from the main thread before the Solr init.
	 * It will use a property <code>montysolr.modulepath</code> to initialize
	 * the Python interpreter. If the property is empty, it will use "python"
	 * @return
	 */
	public PythonVM start() {
		if (vm == null) {
			vm = PythonVM.start(System.getProperty("montysolr.modulepath", "python"));
		}
		return vm;
	}
	
	/**
	 * Creates a new instance of the bridge over the Python waters.
	 * This instance can be used to send the PythonMessage, but until
	 * sendMessage is called, it does nothing
	 * @return {@link PythonBridge}
	 */
	public PythonBridge getBridge() {
		return PythonVMBridge.start(System.getProperty("montysolr.bridge"));
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
     * 
     * The underlying Python process is throwing unchecked {@link PythonException}
     */
    
    public void sendMessage(PythonMessage message) {
    	PythonBridge b = getBridge();
    	try {
			semaphore.acquire();
			b.sendMessage(message);
    	}
    	catch (InterruptedException e) {
    		interrupted++;
    		if (interrupted > workers) {
    			
    			System.err.println(e.getMessage());
    			System.err.println("Warning, the thread calling the Python process was interrupted." +
  					  "If you use multiprocessing on the Python side, this may result in " +
  					  "memory leaks (the process is not instantly terminated by Java;" +
  					  " is garbage collected by Python after it finished though)");
    			System.err.println("Total number of interrupts so far: " + interrupted);
    		}
    	} finally {
    		semaphore.release();
    	}
    	
    }
    
    /**
     * Passed the python command directly to the handler
     * @param pythonString
     */
    public void evalCommand(String pythonString) {
    	PythonBridge b = getBridge();
    	try {
			semaphore.acquire();
			b.evalCommand(pythonString);
			// This works for the new JCC with support for eval, but it is not
			// in the trunk yet
			//int val = vm.eval(pythonString);
			//System.out.println("exec(" + pythonString + ") = " + val);
    	}
		catch (InterruptedException e) {
	    		interrupted++;
	    		if (interrupted > workers) {
	    			System.err.println(e.getMessage());
	    			System.err.println("Warning, the thread calling the Python process was interrupted." +
	    					  "If you use multiprocessing on the Python side, this may result in " +
	    					  "memory leaks (the process is not instantly terminated by Java;" +
	    					  " is garbage collected by Python after it finished though)");
	    			System.err.println("Total number of interrupts so far: " + interrupted);
	    		}
    	} finally {
    		semaphore.release();
    	}
    }

}

/**
 * {@link PythonVMBridge} is virtually the bridge that is always created
 * between the MontySolr Java Virtual Machine {@link MontySolrVM} and the 
 * remote Python side.
 * 
 * This class MUST NOT be a singleton. It serves the purpose of communicating w/ 
 * Python VM. You get the bridge and use methods of the bridge.
 * 
 * @author rca
 *
 */

class PythonVMBridge {
    static protected PythonBridge bridge = null;
    private static String bridgeId = null;
    static boolean warned = false;
    
    protected PythonVMBridge() {
    	// empty constructor
    }
    
    static public PythonBridge start(String bridgeName) throws PythonException {
    	if (bridgeName==null) {
    		if (!warned) {
    			System.err.println("please set montysolr.bridge system property (using default: montysolr.java_bridge.SimpleBridge)");
    			warned = true;
    		}
    		bridgeName = "montysolr.java_bridge.SimpleBridge";
    	}
    	
    	if (bridge!=null && bridgeName.equals(bridgeId))
    		return bridge;
    	
    	String[] parts = bridgeName.split("\\.");
    	bridgeId = bridgeName;
    	
    	if (parts.length > 1) {
    		StringBuffer module = new StringBuffer();
    		module.append(parts[0]);
    		for (int i=1;i<parts.length-1;i++) {
    			module.append(".");
    			module.append(parts[i]);
    		}
    		bridge = start(module.toString(), parts[parts.length-1]);
    	}
    	else {
    		bridge = start(bridgeName);
    	}
    	return bridge;
    }

    static public PythonBridge start(String moduleName, String className) throws PythonException {
    	PythonVM vm = PythonVM.get();
    	bridge = (PythonBridge)vm.instantiate(moduleName, className);
    	bridge.setName(moduleName+'.'+className);
        return bridge;
    }
    
    static public PythonBridge getBridge() throws PythonException {
    	//return start("SimpleBridge");
    	return bridge;
    }
    

}


