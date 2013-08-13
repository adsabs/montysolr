package monty.solr;

import monty.solr.jni.MontySolrVM;

import java.io.File;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.util.thread.QueuedThreadPool;



public class JettyRunner {
  int port = 8983;
  String context = "/solr";
  String webroot = null;
  Server server;
  boolean daemonMode = false;
  boolean isRunning = false;

  public JettyRunner() {
  }

  public JettyRunner(String[] args) throws Exception {
    this.configure(args);
  }

  public void configure(String[] params) throws Exception {
    for (int i = 0; i < params.length; i++) {
      String t = params[i];
      if (t.contains("--port")) {
        port = new Integer(params[i + 1]);
        i++;
      } else if (t.contains("--context")) {
        context = params[i + 1];
        i++;
      } else if (t.contains("--webroot")) {
        webroot = params[i + 1];
        i++;
      } else if (t.contains("--daemon")) {
        daemonMode = true;
      } else if (t.length() == 0) {
        // ignore empty options
      } else {
        throw new Exception("Unknown option '" + t + "'");
      }

    }

    if (webroot == null) {
      throw new Exception("--webroot missing");
    }
    File wr = new File(webroot);
    if (!wr.exists()) {
      throw new Exception("--webroot does not exists");
    }

    if ( System.getProperty("solr.solr.home") == null || !(new File(System.getProperty("solr.solr.home"))).exists()) {
      throw new Exception("solr.solr.home is not set or does not exist!");
    }



  }

  public void start() throws Exception {
    // This must happen in the main thread
    MontySolrVM.INSTANCE.start("montysolr_java");

    if (!isRunning) {

      server = new Server(port);

      WebAppContext ctx = new WebAppContext(server, webroot, context);


      // this sets the normal java class-loading policy, when system
      // classes (and classes loaded first) have higher priority
      // this is imporant for our singleton to work, otherwise there
      // are different classloaders and the singletons are not singletons
      // across webapps
      ctx.setParentLoaderPriority(true);

      // also this works and has the same effect (I don't know what are
      // implications of one or the other method
      //ctx.setClassLoader(this.getClass().getClassLoader());

      SocketConnector connector = new SocketConnector();
      connector.setMaxIdleTime(1000 * 60 * 60);
      connector.setSoLingerTime(-1);
      connector.setPort(port);
      server.setConnectors(new Connector[] { connector });

      server.setStopAtShutdown(true);

      // this is a temporary solution until i plug-in back the standard
      // jetty loading/startup mechanism
      QueuedThreadPool tp = new QueuedThreadPool();
      tp.setMinThreads(10);
      int mt = System.getProperty("montysolr.max_threads") != null ? Integer.valueOf(System.getProperty("montysolr.max_threads")) : 200;
      tp.setMaxThreads(mt);
      server.setThreadPool(tp);


      server.start();
      port = connector.getLocalPort();
      isRunning = true;
    }
  }

  public void stop() throws Exception {
    if (isRunning) {
      server.stop();
      isRunning = false;
    }
  }

  public static JettyRunner init() {
    JettyRunner jr = new JettyRunner();
    ClassLoader currentContextLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(jr.getClass().getClassLoader()); // load jetty
    //Thread.currentThread().setContextClassLoader(currentContextLoader );
    return jr;
  }
  private void join() throws Exception {
    if (!isRunning) {
      this.start();
    }
    server.join();
  }

  private void testJSP() throws Exception
    {
      // Currently not an extensive test, but it does fire up the JSP pages and make
      // sure they compile ok

      String queryPath = "http://localhost:"+port+context+"/";
      String adminPath = "http://localhost:"+port+context+"/admin/";

      String html = IOUtils.toString( new URL(queryPath).openStream() );
      assert html.contains("<body"); // real error will be an exception

      html = IOUtils.toString( new URL(adminPath).openStream() );
      assert html.contains("Solr Admin"); // real error will be an exception

    }

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    System.out.println("bootstrap.Main loader = " + JettyRunner.class.getClassLoader().toString());
    JettyRunner jr = null;
    try {
      jr = init(); 
      jr.configure(args);

      if (jr.daemonMode) {
        jr.join();
      }
      else {
        jr.start();
        jr.testJSP();
        jr.stop();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      jr.stop();
    }

  }
}
