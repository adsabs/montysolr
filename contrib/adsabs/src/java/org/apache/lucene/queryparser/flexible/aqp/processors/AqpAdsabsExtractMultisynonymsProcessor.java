package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.queryparser.flexible.aqp.ADSEscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.util.SolrPluginUtils;

/**
 * This analyzer is a WORKAROUND for the unfieded searches - edismax
 * does not know how to handle multi-token synonyms. And this problem
 * will be 'fully' solved only after I rewrite the whole edismax
 * into aqp (and I don't want to mess with that right now)
 * 
 * But it should be done, because we are running analysis just 
 * to discover a multitoken synonym (ie. synonym spanning over 
 * several tokens). We will remember it and make the parser to
 * build a new AST
 * 
 * <pre>	
 *           AqpFunctionQueryNode("some hubble space telescope")
 * </pre>
 *           
 *  becomes:
 * 
 * <pre>
 * 					                    OR
 *                               |
 *             ------------------------------------------                  
 *            /                                          \
 *       FieldQN        AqpFunctionQueryNode("some hubble space telescope")
 *          |
 *   -------------
 *  /             \     
 * title   'hubble space telescope'  
 * </pre>
 * 
 * This workaround is necessary only for PHRASE queries (because in ADS
 * we index even multi-token as single words, so everything work as expected
 * even with edismax)
 * 
 * To activate it, you MUST set:
 * 	  aqp.unfielded.phrase.edismax.synonym.workaround = true
 *    qf = list of edismax fields
 */

public class AqpAdsabsExtractMultisynonymsProcessor extends QueryNodeProcessorImpl {

	private CharTermAttribute termAtt;
	private float boostCorrection = 0.9f;
	
	public AqpAdsabsExtractMultisynonymsProcessor() {
		// empty
	}

	@Override
	public QueryNode process(QueryNode queryTree) throws QueryNodeException {
		QueryConfigHandler config = this.getQueryConfigHandler();

		if (config.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
				&& config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
				.getRequest() != null) {
			
			Map<String, String> args = getQueryConfigHandler().get(
	    		AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER);
			
			if (!args.containsKey("aqp.unfielded.phrase.edismax.synonym.workaround") || args.get("aqp.unfielded.phrase.edismax.synonym.workaround") == "false") {
				return queryTree;
			}
			
			if (args.containsKey("aqp.unfielded.phrase.edismax.synonym.workaround.boost.correction")) {
				boostCorrection = Float.parseFloat(args.get("aqp.unfielded.phrase.edismax.synonym.workaround.boost.correction"));
			}
			
			return super.process(queryTree);
		}

		return queryTree;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
	throws QueryNodeException {
		
	  if (node instanceof AqpFunctionQueryNode && ((AqpFunctionQueryNode) node).getName().contains("edismax")) {
	  	
	  	
	  	OriginalInput oi = ((AqpFunctionQueryNode) node).getOriginalInput();
	  	
	  	if (oi == null)
	  		return node;
	  	
	  	String subQuery = oi.value;
	  	
	  	subQuery = ADSEscapeQuerySyntaxImpl.discardEscapeChar(subQuery).toString();
	  	
	  	if (!subQuery.contains("\"")) {
	  		return node; // not necessary for normal queries
	  	}
	  	
	  	QueryConfigHandler config = this.getQueryConfigHandler();
	  	SolrQueryRequest req = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
			.getRequest();
	  	SolrParams params = req.getParams();
	  	
	  	String qf = params.get(DisMaxParams.QF);
	  	if (qf == null) {
	  		return node;
	  	}
	  	
	  	List<QueryNode> lst = new ArrayList<QueryNode>();
	  	
	  	Map<String,Float> parsedQf = SolrPluginUtils.parseFieldBoosts(qf);
	  	for (Entry<String, Float> entry: parsedQf.entrySet()) {
	  		String field = entry.getKey();
	  		if (field.charAt(0) == '_') // special fields
	  			continue;
	  		Float boost = entry.getValue();
	  		ArrayList<String> result = extractMultiSpanTokens(field, subQuery);
	  		if (result != null && result.size() > 0) {
	  			for (String val: result) {
	  				lst.add(new BoostQueryNode(
	  						new AqpNonAnalyzedQueryNode(new FieldQueryNode(field, val, -1, -1)), 
	  						boost!=null ? new BigDecimal(boost*boostCorrection).setScale(3, RoundingMode.HALF_EVEN).floatValue() : 1.0f));
	  			}
	  		}
	  	}
	  	
	  	if (lst.size() >0 ) {
	  		lst.add(node);
	  		return new AqpOrQueryNode(lst);
	  	}
			
	  }
	  
		return node;
	}


	

	
	private ArrayList<String> extractMultiSpanTokens(CharSequence field, String value) throws QueryNodeException {
		QueryConfigHandler config = this.getQueryConfigHandler();

		Locale locale = getQueryConfigHandler().get(ConfigurationKeys.LOCALE);
		if (locale == null) {
			locale = Locale.getDefault();
		}
		Analyzer analyzer = config.get(StandardQueryConfigHandler.ConfigurationKeys.ANALYZER);

		ArrayList<String> out = new ArrayList<String>();
		TokenStream source = null;
		try {
			source = analyzer.tokenStream(field.toString(),
					new StringReader(value));
			source.reset();
		} catch (IOException e1) {
		  if (source != null) {
        try {
          source.close();
        } catch (IOException e) {
          // NOOP
        }
		  }
			return null;
		}

		
		int start = -1;
		int end = -1;
		int lastTokenStart = -1;
		int lastTokenEnd = -1;
		boolean isMultiSpan = false;
		
		OffsetAttribute offsetAtt = null;
		try {
			while (source.incrementToken()) {
				if (source.hasAttribute(OffsetAttribute.class)) {
	        offsetAtt = source.getAttribute(OffsetAttribute.class);
	      }
				else {
					break;
				}
				
				start = offsetAtt.startOffset();
				end = offsetAtt.endOffset();
				termAtt = source.getAttribute(CharTermAttribute.class);
				
				if (start == lastTokenStart && end > lastTokenEnd && isMultiSpan) {
					out.add(termAtt.toString());
					continue;
				}
				
				if (start <= lastTokenEnd && end > lastTokenEnd) {
					isMultiSpan = true;
					out.add(termAtt.toString());
					continue;
				}
				else {
					isMultiSpan = false;
				}
				
				lastTokenStart = start;
				lastTokenEnd = end;
			}
		} catch (IOException e) {
		  // noop
		} finally {
		  try {
        source.close();
      } catch (IOException e2) {
        // NOOP
      }
		}

		return out;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
	throws QueryNodeException {
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
	throws QueryNodeException {
		return children;
	}

}