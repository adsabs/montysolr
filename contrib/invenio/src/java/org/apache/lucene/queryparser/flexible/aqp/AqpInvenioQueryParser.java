package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParser;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpInvenioQueryConfigHandler;


/**
 * The complete configuration for the Invenio query 
 * parser, this is the main object which parses the 
 * query, from the string, until the query object.
 * 
 * @author rca
 *
 */

public class AqpInvenioQueryParser extends AqpQueryParser {

	public static AqpInvenioQueryParser init () 
		throws QueryNodeParseException {
		return new AqpInvenioQueryParser(new AqpInvenioQueryConfigHandler(), 
				new AqpInvenioSyntaxParser(), //.initializeGrammar("Invenio"),
				new AqpInvenioNodeProcessorPipeline(null),
				new AqpInvenioQueryTreeBuilder());
	}

	
	public static AqpInvenioQueryParser init(String grammarName) throws Exception {
		throw new IllegalArgumentException(
				"Invenio query parser does not support loadable grammars");
	}
	
	public AqpInvenioQueryParser(QueryConfigHandler config,
			AqpSyntaxParser parser,
			QueryNodeProcessorPipeline processor,
			QueryTreeBuilder builder) {
		super(config, parser, processor, builder);
	}

	
}
