package invenio.montysolr.jni;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.core.SolrResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This is abstract class that holds testing methods and basic methods
 * of each bridge
 * @author rca
 *
 */

public abstract class BasicBridge {
	
	public static final Logger log = LoggerFactory.getLogger(BasicBridge.class);

	protected String bridgeName = null;
	
	public String getName() {
		return this.bridgeName;
	}
	public void setName(String name) {
		this.bridgeName = name;
	}
	
	// ------------- java testing methods -----------------
	

	public void testPrint() {
		System.out.println("java is printing, instance: " + this.toString()
				+ " from thread id: " + Thread.currentThread().getId());
	}

	public String testReturnString() {
		return "java is printing, instance: " + this.toString()
				+ " from thread id: " + Thread.currentThread().getId();
	}
	
	public List<String> testReturnListOfStrings() {
		ArrayList<String> l = new ArrayList<String>();
		l.add(getName());
		l.add(this.toString());
		l.add(Long.toString(Thread.currentThread().getId()));
		return l;
	}
	
	public List<Integer> testReturnListOfIntegers() {
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(0);
		l.add(1);
		return l;
	}
	
	

}
