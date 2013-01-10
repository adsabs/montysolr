package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;

public class AqpAdsabsRegexNodeProcessor extends QueryNodeProcessorImpl implements
	QueryNodeProcessor  {

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof FieldQueryNode && !(node instanceof AqpNonAnalyzedQueryNode) &&
		    !(node.getParent() instanceof TermRangeQueryNode)) {
			FieldQueryNode n = (FieldQueryNode) node;
			String input = n.getTextAsString();
			
			if (input == null) {
				return node;
			}
			
			// try to detect if we have special regex characters inside the string
			if (isRegex(input)) {
				if (!isRegex(input.replace(".?", "").replace(".*", ""))) { // detect simple wildcard cases
					input = input.replace(".*", "*").replace(".?", "?");
					if (input.endsWith("*") && !input.contains("?")) {
						return new PrefixWildcardQueryNode(n.getFieldAsString(), input, n.getBegin(), n.getEnd());
					}
					return new WildcardQueryNode(n.getFieldAsString(), input, n.getBegin(), n.getEnd());
				}
				
				try {
					Pattern.compile(input);
					return new AqpAdsabsRegexQueryNode(n.getFieldAsString(), input, n.getBegin(), n.getEnd());
				}
				catch (PatternSyntaxException e) {
					return node;
				}
				
			}
			
			
		}
		return node;
	}
	
	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}

	/*
	 * Very simple way to look for regular expression class characters inside the
	 * string, we ignore java 'characters' (ie. not translate the string into a
	 * string literal and look for the value). So if somebody searches for \n
	 * we don't consider that to be a regular expression (if the parser is configured
	 * correctly, such characters are expanded before they get here - so there should
	 * be no \n). But it depends... anywayw, searching for \n,\r,\f etc is very strange
	 * with ADS search engine
	 */
	String  regexMarker =
		"(?<!\\\\)" + // negative lookbehind
		"(" +
//			"(\\[|\\^|\\&|\\||\\{)" + // characters whose presence means regex
			"(\\[|\\^|\\||\\{)" + // characters whose presence means regex
			"|" + "(\\\\b|\\\\B|\\\\A|\\\\G|\\\\Z|\\\\z)" +
			"|" + "(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)" + // predefined char classes
			"|" + "\\\\p\\{" + // character classes
			"|" + "\\.(\\?|\\*|\\{)" +
		")";
	
	Pattern regexTest = Pattern.compile(regexMarker);
	
	private boolean isRegex(String input) {
		Matcher matcher = regexTest.matcher(input);
		if (matcher.find()) {
			return true;
		}
		return false;
		
	}
}
