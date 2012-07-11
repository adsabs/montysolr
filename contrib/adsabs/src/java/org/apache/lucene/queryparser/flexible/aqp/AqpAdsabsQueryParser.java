package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParserLoadableImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.parser.ADSSyntaxParser;




public class AqpAdsabsQueryParser extends AqpQueryParser {


	public static AqpQueryParser init() throws Exception {
		return new AqpAdsabsQueryParser(new AqpAdsabsQueryConfigHandler(), 
				  new ADSSyntaxParser(),
				  new AqpAdsabsNodeProcessorPipeline(null),
				  new AqpAdsabsQueryTreeBuilder());
	}

	public static AqpQueryParser init(String grammarName) throws Exception {
		return new AqpAdsabsQueryParser(new AqpAdsabsQueryConfigHandler(), 
				new AqpSyntaxParserLoadableImpl().initializeGrammar(grammarName),
				  new AqpAdsabsNodeProcessorPipeline(null),
				  new AqpAdsabsQueryTreeBuilder());
	}
	

	public AqpAdsabsQueryParser(
			QueryConfigHandler config,
			AqpSyntaxParser parser,
			QueryNodeProcessorPipeline processor,
			QueryTreeBuilder builder) {
		super(config, parser, processor, builder);
	}
	
}
