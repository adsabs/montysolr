package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.aqp.AqpAdslabsNodeProcessorPipeline;
import org.apache.lucene.queryParser.aqp.AqpAdslabsQueryConfigHandler;
import org.apache.lucene.queryParser.aqp.AqpAdslabsQueryTreeBuilder;
import org.apache.lucene.queryParser.aqp.parser.ADSSyntaxParser;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParserLoadableImpl;




public class AqpAdsabsQueryParser extends AqpQueryParser {


	public static AqpQueryParser init() throws Exception {
		return new AqpAdsabsQueryParser(new AqpAdslabsQueryConfigHandler(), 
				  new ADSSyntaxParser(),
				  new AqpAdslabsNodeProcessorPipeline(null),
				  new AqpAdslabsQueryTreeBuilder());
	}

	public static AqpQueryParser init(String grammarName) throws Exception {
		return new AqpAdsabsQueryParser(new AqpAdslabsQueryConfigHandler(), 
				new AqpSyntaxParserLoadableImpl().initializeGrammar(grammarName),
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
