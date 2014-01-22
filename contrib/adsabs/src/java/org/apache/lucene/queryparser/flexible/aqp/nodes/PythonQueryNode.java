package org.apache.lucene.queryparser.flexible.aqp.nodes;


import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeError;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.search.CacheWrapper;
import org.apache.lucene.search.SolrCacheWrapper;

public class PythonQueryNode extends QueryNodeImpl implements QueryNode {

	private static final long serialVersionUID = 3935454544149998076L;
	private boolean useIntBits;
	private SolrCacheWrapper cacheWrapper;
	private String pythonFunctionName;
	
	public PythonQueryNode(QueryNode query, SolrCacheWrapper cache) {
		if (query == null) {
			throw new QueryNodeError(new MessageImpl(
					QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED, "query",
					"null"));
		}
		
		assert cache != null;
		allocate();
		setLeaf(false);
		add(query);
		cacheWrapper = cache;
	}

	
	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		if (getChild() == null)
			return "";

		String leftParenthensis = "";
		String rightParenthensis = "";

		if (getChild() != null && getChild() instanceof PythonQueryNode) {
			leftParenthensis = "(";
			rightParenthensis = ")";
		}

		return leftParenthensis + "#" + getChild().toQueryString(escapeSyntaxParser)
				+ rightParenthensis ;

	}

	
	public String toString() {
		return "<python>\n"  + getChild().toString() + "\n</python>";
	}

	public QueryNode getChild() {
		return getChildren().get(0);
	}

	
	/*
	 * Indicates whether intbitsets should be used as a transport mechanism
	 * between java na python
	 */
	public boolean useIntBitSet() {
	  return false;
  }
	
	public void setIntBitSet(boolean v) {
		useIntBits = v;
	}

	
	/*
	 * The PythonQuery will need how to translate remote system IDs into
	 * lucene ids. This wrapper should provide that functionality
	 * This should not be null
	 */
	public SolrCacheWrapper getCacheWrapper() {
	  return cacheWrapper;
  }
	
	public void setCacheWrapper(SolrCacheWrapper cache) {
		cacheWrapper = cache;
	}


	/*
	 * The remote endpoint has its own name - this value should not
	 * be exposed for the end-user to change. This function can return
	 * null, in which case the PythonQuery will use the default target
	 */
	public String getPythonFunctionName() {
	  return pythonFunctionName;
  }
	
	public void setPythonFunctionName(String v) {
		pythonFunctionName = v;
	}
			

}
