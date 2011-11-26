package newseman;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;

import java.io.IOException;

public class SemanticTagger {
	
	private String url = null;
	 
	SemanticTagger(String url) {
		this.url = url;
		
	}
	
	public void createTagger(String url) throws InterruptedException {
		
		PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("initialize_seman")
			.setSender(this.getClass().toString())
			.setParam("url", url);
		MontySolrVM.INSTANCE.sendMessage(message);
	}
	

	public String[][] translateTokenCollection(String[][] tokens) throws IOException {

		PythonMessage message = MontySolrVM.INSTANCE
				.createMessage("translate_token_collection")
				.setSender(this.getClass().toString())
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
