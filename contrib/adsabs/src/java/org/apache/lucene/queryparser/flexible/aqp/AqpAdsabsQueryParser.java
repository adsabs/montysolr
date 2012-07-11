package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdslabsNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdslabsQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParserLoadableImpl;
import org.apache.lucene.queryparser.flexible.aqp.parser.ADSSyntaxParser;




public class AqpAdsabsQueryParser extends AqpQueryParser {


	public static AqpQueryParser init() throws Exception {
		return new AqpAdsabsQueryParser(new AqpAdsabsQueryConfigHandler(), 
				  new ADSSyntaxParser(),
				  new AqpAdslabsNodeProcessorPipeline(null),
				  new AqpAdslabsQueryTreeBuilder());
	}

	public static AqpQueryParser init(String grammarName) throws Exception {
		return new AqpAdsabsQueryParser(new AqpAdsabsQueryConfigHandler(), 
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
