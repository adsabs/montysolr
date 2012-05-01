package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.aqp.AqpAdslabsNodeProcessorPipeline;
import org.apache.lucene.queryParser.aqp.AqpAdslabsQueryConfigHandler;
import org.apache.lucene.queryParser.aqp.AqpQueryParser;
import org.apache.lucene.queryParser.aqp.AqpAdslabsQueryTreeBuilder;
import org.apache.lucene.queryParser.aqp.parser.ADSSyntaxParser;




public class AqpAdslabsQueryParser extends AqpQueryParser {

	public AqpAdslabsQueryParser() throws Exception {
		new AqpAdslabsQueryParser("ADS");
	}

	
	public AqpAdslabsQueryParser(String grammarName) throws Exception {
		super(new AqpAdslabsQueryConfigHandler(), 
				  new ADSSyntaxParser(),
				  new AqpAdslabsNodeProcessorPipeline(null),
				  new AqpAdslabsQueryTreeBuilder());
	}

	
	
}
