package invenio.montysolr;

import java.io.File;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.webapp.WebAppClassLoader;


public class JettyRunner {
	int port = 8983;
	String context = "/test";
	String webroot = "/x/dev/workspace/test-solr/webapp";
	Server server;
	boolean isRunning = false;

	public JettyRunner() {
		System.out.println("JettyRunner loaded");
	}

	public JettyRunner(String[] args) throws Exception {
		System.out.println("JettyRunner loaded");
		this.configure(args);
	}

	public void configure(String[] params) throws Exception {
		for (int i = 0; i < params.length; i++) {
			String t = params[i];
			if (t.contains("port")) {
				port = new Integer(params[i + 1]);
			} else if (t.contains("context")) {
				context = params[i + 1];
			} else if (t.contains("solr.home")) {
				System.setProperty("solr.solr.home", params[i + 1]);
			} else if (t.contains("webroot")) {
				webroot = params[i + 1];
			} else {
				throw new Exception("Unknown option " + t);
			}
			i++;
		}

		File h = new File(System.getProperty("solr.solr.home"));
		if ( !h.exists()) {
			throw new Exception("solr.solr.home not set or not exists");
		}

	}

	public void start() throws Exception {
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

	    // analysis
	    html = IOUtils.toString( new URL(adminPath+"analysis.jsp").openStream() );
	    assert html.contains("Field Analysis"); // real error will be an exception

	    // schema browser
	    html = IOUtils.toString( new URL(adminPath+"schema.jsp").openStream() );
	    assert html.contains("Schema"); // real error will be an exception

	    // schema browser
	    html = IOUtils.toString( new URL(adminPath+"threaddump.jsp").openStream() );
	    assert html.contains("org.apache.solr"); // real error will be an exception


	  }

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("bootstrap.Main loader = " + JettyRunner.class.getClassLoader().toString());
		JettyRunner jr = null;
		try {
			jr = new JettyRunner();
			ClassLoader currentContextLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(jr.getClass().getClassLoader()); // load jetty

			jr.configure(args);
			jr.start();
			Thread.currentThread().setContextClassLoader(currentContextLoader );
			jr.testJSP();
			jr.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jr.stop();
		}

	}

}
