package org.apache.lucene.newseman;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;

import java.io.IOException;

public class SemanticTagger {

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
