package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.solr.request.SolrQueryRequest;

/**
 * This analyzer is applied only to certain nodes in order to clean
 * them up. It is using solr analyzer chains for a fields, by convention
 * these fields are of the following types
 * 
 * _wildcard - to be used on the wildcard searches
 * _regex
 * _fuzzy
 * 
 */

public class AqpAdsabsCarefulAnalyzerProcessor extends QueryNodeProcessorImpl {

	private CharTermAttribute termAtt;
	
	public AqpAdsabsCarefulAnalyzerProcessor() {
		// empty
	}

	@Override
	public QueryNode process(QueryNode queryTree) throws QueryNodeException {
		QueryConfigHandler config = this.getQueryConfigHandler();

		if (config.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
				&& config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
				.getRequest() != null) {
			return super.process(queryTree);
		}

		return queryTree;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
	throws QueryNodeException {
		
	  String field = null;
	  String value =null;
	  String[] tokens;
	  if (node instanceof WildcardQueryNode) {
	    field = ((WildcardQueryNode) node).getFieldAsString();
	    value = ((WildcardQueryNode) node).getTextAsString();
	    
	    int asteriskPosition = -1;
	    int qmarkPosition = -1;
	    int origLen = value.length();
	    
	    if (value.indexOf('*') > -1) {
	      asteriskPosition = value.indexOf('*');
	    }
	    if (value.indexOf('?') > -1) {
	      qmarkPosition = value.indexOf('?');
	    }
	    
	    if (asteriskPosition > 0 && asteriskPosition+1 < value.length()
	        || qmarkPosition > 0 && qmarkPosition+1 < value.length()
	        || asteriskPosition > -1 && qmarkPosition > -1)
	      return node;
	        
	    for (String suffix: new String[]{"_wildcard", ""}) {
  	    if (hasAnalyzer(field + suffix)) {
          tokens =  analyze(field + suffix, value);
          
          if (tokens.length != 1)
            return node; // break, let the analyzer decide the fate
          
          String newToken = tokens[0];
          if (newToken.length() < origLen) {
            if (qmarkPosition > -1) {
              if (qmarkPosition == 0) {
                newToken = '?' + tokens[0];
              }
              else { 
                newToken = tokens[0] + '?';
              }
            }
            else {
              if (asteriskPosition == 0) {
                newToken = '*' + tokens[0];
              }
              else {
                newToken = tokens[0] + '*';
              }
            }
          }
          
          if (!newToken.equals(value)) {
            return new WildcardQueryNode(field, 
                newToken, ((WildcardQueryNode)node).getBegin(),
              ((WildcardQueryNode)node).getEnd());
          }
        }
	    }
	  }
	  else if(node instanceof FuzzyQueryNode) {
	    field = ((FuzzyQueryNode) node).getFieldAsString();
	    value = ((FuzzyQueryNode) node).getTextAsString();
	    for (String suffix: new String[]{"_fuzzy", ""}) {
  	    if (hasAnalyzer(field+suffix)) {
          tokens =  analyze(field + suffix, value);
          
          if (tokens.length > 1)
            return node; // break, let the analyzer decide the fate
          
          if (!tokens[0].equals(value)) {
            return new FuzzyQueryNode(field, 
                tokens[0], 
              ((FuzzyQueryNode)node).getSimilarity(),
              ((FuzzyQueryNode)node).getBegin(),
              ((FuzzyQueryNode)node).getEnd());
          }
        }
	    }
	  }
	  else if(node instanceof AqpAdsabsRegexQueryNode) {
	    field = ((FieldQueryNode) node).getFieldAsString();
	    value = ((FieldQueryNode) node).getText().toString();
	    for (String suffix: new String[]{"_regex",}) {
  	    if (hasAnalyzer(field + suffix)) {
  	      tokens =  analyze(field + suffix, value);
  	      
  	      if (tokens.length > 1)
  	        return node; // break, let the analyzer decide the fate
  	      
  	      if (!tokens[0].equals(value)) {
  	        return new AqpAdsabsRegexQueryNode(field, 
  	            tokens[0], ((FieldQueryNode)node).getBegin(),
  	            ((FieldQueryNode)node).getEnd());
  	      }
  	    }
	    }
	  }
	  
		return node;
	}


	

  private boolean hasAnalyzer(String fieldName) {
	  SolrQueryRequest req = this.getQueryConfigHandler()
	  .get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
    .getRequest();
	  if (req.getSchema().hasExplicitField(fieldName)) {
	    return true;
	  }
	  return false;
	}
	
	private String[] analyze(CharSequence field, String value) throws QueryNodeException {
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
			if (source != null)
	      try {
	        source.close();
        } catch (IOException e) {
	        // ignore
        }
			return new String[0];
		}

		
		try {
			while (source.incrementToken()) {
			  termAtt = source.getAttribute(CharTermAttribute.class);
				out.add(termAtt.toString());
			}
			source.close();
		} catch (IOException e) {
			// pass
		}
		
		return out.toArray(new String[out.size()]);
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