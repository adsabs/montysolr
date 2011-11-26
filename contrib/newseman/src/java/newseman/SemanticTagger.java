package newseman;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;

import java.io.IOException;

public class SemanticTagger {
	
	private String url = null;
	private String name = null;
	
	 
	SemanticTagger(String url) {
		this.url = url;
		this.name = getClass().toString() + Thread.currentThread().getId();
		
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * Initializes a new pythonic tagger
	 * @param url - the database used by SEMAN (in the sqlachemy format, eg: sqlite:///tmp/data.sqlite)
	 * @param name - the identifier of the new database, it will be used to access the instance
	 * 		of SEMAN (remember, you can create many instances with the same url; url is ambiguous, but
	 * 		the name should be always unique)
	 * @throws InterruptedException
	 */
	public void createTagger(String url, String name) throws InterruptedException {
		
		PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("initialize_seman")
			.setSender(this.getClass().toString())
			.setParam("url", this.url)
			.setParam("name", this.name);
		MontySolrVM.INSTANCE.sendMessage(message);
	}
	
	
	/**
	 * Receives the two-dimensional array in the form of:
	 * <pre>
	 * 
	 * [
	 *  ["token", "id", ....] # first row is a header
	 *  ["word", "5646465",...] # data
	 *  ["word2", "5646567",...]
	 *  ....
	 *  
	 * ]
	 * </pre>
	 * 
	 * Passes the data for translation to Python SEMAN and
	 * retrieves results. The results will be enriched, based
	 * on the current configuration (tokens may be marged, new
	 * added, some deleted etc)
	 * 
	 * @param tokens
	 * @return
	 * @throws IOException
	 */
	public String[][] translateTokens(String[][] tokens) throws IOException {

		PythonMessage message = MontySolrVM.INSTANCE
				.createMessage("translate_tokens")
				.setSender(this.getClass().toString())
				.setParam("url", this.name)
				.setParam("tokens", tokens);
		
		try {
			MontySolrVM.INSTANCE.sendMessage(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException("Error calling MontySolr!");
		}

		Object results = message.getResults();
		if (results != null) {
			String[][] translatedTokens = (String[][]) results;
			return translatedTokens;
		}
		else {
			throw new IOException("Wrong return type null");
		}
	}

}
