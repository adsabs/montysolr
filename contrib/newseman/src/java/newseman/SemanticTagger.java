package newseman;


import java.io.IOException;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonMessage;


/**
 * Wrapper for Pythonic application SEMAN. It enhances tokens with additional
 * features such as: semantic codes, part-of-speech tags, synonyms, multi-token
 * synonyms. It can also return the quasi-root of the word, modify the collection
 * of tokens (add new, remove unwanted tokens etc).
 * 
 * The translation is called using {@link MontySolrVM}, therefore Python must
 * be correctly initialized. Also you must make sure, that the python module
 * <code> monty_newseman.targets </code> is on <code>sys.path</code>
 * 
 * @see SemanticTaggerTokenFilter
 *  
 * @author rchyla
 *
 */
public class SemanticTagger {
	
	private String url = null;
	private String name = null;
	private String valueSeparator = "|";
	
	 
	SemanticTagger(String url) {
		this.url = url;
		this.name = getClass().getCanonicalName() + Thread.currentThread().getId();
		createTagger(this.url, this.name);
	}
	
	
	public String getSeparator() {
		return valueSeparator;
	}
	
	public void setSeparator(String valueSeparator) {
		this.valueSeparator = valueSeparator;
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
	 */
	public void createTagger(String url, String name) {
		
		PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("initialize_seman")
			.setSender(this.getClass().getCanonicalName())
			.setParam("url", url)
			.setParam("name", name);
		MontySolrVM.INSTANCE.sendMessage(message);
	}
	
	
	public void configureTagger(String language, Integer maxDistance,
			String groupAction, String groupCleaning ) {

		PythonMessage message = MontySolrVM.INSTANCE.createMessage("configure_seman")
	        .setParam("url", this.getName());
		
		if (language != null)
	        message.setParam("language", language);
	    if (maxDistance != null)
	        message.setParam("max_distance", maxDistance);
	    if (groupAction != null)
	        message.setParam("grp_action", groupAction);
	    if (groupCleaning != null)
	        message.setParam("grp_cleaning", groupCleaning);
	        
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
	public String[][] translateTokens(String[][] tokens) throws NullPointerException {

		PythonMessage message = MontySolrVM.INSTANCE
				.createMessage("translate_tokens")
				.setSender(this.getClass().getCanonicalName())
				.setParam("url", this.name)
				.setParam("joinChar", this.valueSeparator)
				.setParam("tokens", tokens);
		
		MontySolrVM.INSTANCE.sendMessage(message);

		Object results = message.getResults();
		if (results != null) {
			Object[] res = (Object[]) results;
			String[][] translatedTokens = new String[res.length][];
			for (int i=0;i<res.length;i++) {
				translatedTokens[i] = (String[]) res[i];
			}
			return translatedTokens;
		}
		else {
			throw new NullPointerException("Wrong return type null");
		}
	}

}
