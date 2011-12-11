package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.aqp.builders.AqpStandardQueryTreeBuilder;
import org.apache.lucene.queryParser.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryParser.aqp.parser.InvenioSyntaxParser;
import org.apache.lucene.queryParser.aqp.processors.AqpQueryNodeProcessorPipeline;

public class AqpQueryParserInvenio extends AqpQueryParser {

	public AqpQueryParserInvenio() throws Exception {
		super(new AqpStandardQueryConfigHandler(), 
				new InvenioSyntaxParser(),
				new AqpQueryNodeProcessorPipeline(null),
				new AqpStandardQueryTreeBuilder());
	}
	
	public AqpQueryParserInvenio(String grammarName) throws Exception {
		throw new IllegalArgumentException("Invenio query parser does not use reflection");
	}
	
}
