package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.aqp.AqpAdslabsNodeProcessorPipeline;
import org.apache.lucene.queryParser.aqp.AqpAdslabsQueryConfigHandler;
import org.apache.lucene.queryParser.aqp.AqpQueryParser;
import org.apache.lucene.queryParser.aqp.AqpAdslabsQueryTreeBuilder;
import org.apache.lucene.queryParser.aqp.parser.ADSSyntaxParser;
import org.apache.lucene.queryParser.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorPipeline;




public class AqpAdsabsQueryParser extends AqpQueryParser {



	public AqpAdsabsQueryParser() throws Exception {
		new AqpAdsabsQueryParser("ADS");
	}

	
	public AqpAdsabsQueryParser(String grammarName) throws Exception {
		super(new AqpAdslabsQueryConfigHandler(), 
				  new ADSSyntaxParser(),
				  new AqpAdslabsNodeProcessorPipeline(null),
				  new AqpAdslabsQueryTreeBuilder());
	}

	public AqpAdsabsQueryParser(
			QueryConfigHandler config,
			AqpSyntaxParser parser,
			QueryNodeProcessorPipeline processor,
			QueryTreeBuilder builder) {
		super(config, parser, processor, builder);
	}
	
}
