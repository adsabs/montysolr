package invenio.montysolr;

import invenio.montysolr.jni.PythonBridge;
import invenio.montysolr.jni.MontySolrVM;

import java.io.PrintStream;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import junit.framework.TestCase;



/**
 * Base class for all Lucene unit tests.
 * <p>
 * Currently the
 * only added functionality over JUnit's TestCase is
 * asserting that no unhandled exceptions occurred in
 * threads launched by ConcurrentMergeScheduler and asserting sane
 * FieldCache usage athe moment of tearDown.
 * </p>
 * <p>
 * If you
 * override either <code>setUp()</code> or
 * <code>tearDown()</code> in your unit test, make sure you
 * call <code>super.setUp()</code> and
 * <code>super.tearDown()</code>
 * </p>
 * @see #assertSaneFieldCaches
 */
public abstract class MontySolrTestCase extends TestCase {

  protected MontySolrVM VM;

  public MontySolrTestCase() {
    super();
  }

  public MontySolrTestCase(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
	  MontySolrVM.INSTANCE.start("montysolr_java");
	  this.VM = MontySolrVM.INSTANCE;
	  super.setUp();

  }

  protected PythonBridge getBridge() {
	  return MontySolrVM.INSTANCE.getBridge();
  }

  protected String getTestLabel() {
    return getClass().getName() + "." + getName();
  }

  protected void tearDown() throws Exception {
      super.tearDown();
  }


  /**
   * Convinience method for logging an iterator.
   * @param label String logged before/after the items in the iterator
   * @param iter Each next() is toString()ed and logged on it's own line. If iter is null this is logged differnetly then an empty iterator.
   * @param stream Stream to log messages to.
   */
  public static void dumpIterator(String label, Iterator iter,
                                  PrintStream stream) {
    stream.println("*** BEGIN "+label+" ***");
    if (null == iter) {
      stream.println(" ... NULL ...");
    } else {
      while (iter.hasNext()) {
        stream.println(iter.next().toString());
      }
    }
    stream.println("*** END "+label+" ***");
  }

  /**
   * Convinience method for logging an array.  Wraps the array in an iterator and delegates
   * @see dumpIterator(String,Iterator,PrintStream)
   */
  public static void dumpArray(String label, Object[] objs,
                               PrintStream stream) {
    Iterator iter = (null == objs) ? null : Arrays.asList(objs).iterator();
    dumpIterator(label, iter, stream);
  }

}

