package org.apache.lucene.queryParser.aqp;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

public class AqpCommonTree extends CommonTree {
	
	public AqpCommonTree() { }
	
	public AqpCommonTree(CommonTree node) {
		super(node);
	}
	
	public AqpCommonTree(AqpCommonTree node) {
		super(node);
	}
	
	public AqpCommonTree(Token t) {
		this.token = t;
	}
	
	/** Return the whole tree converted to QueryNode tree */
    public QueryNode toQueryNodeTree() {
		if ( children==null || children.size()==0 ) {
			return this.toQueryNode();
		}
		
		QueryNode buf = toQueryNode();
		for (int i = 0; children!=null && i < children.size(); i++) {
			AqpCommonTree t = (AqpCommonTree)children.get(i);
			buf.add(t.toQueryNodeTree());
		}
		return buf;
	}
    
    public QueryNode toQueryNode() {
    	return (QueryNode) new AqpANTLRNode(this);
    }
    
    public String getTokenType() {
    	if ( isNil() ) {
			return "nil";
		}
		if ( getType()==Token.INVALID_TOKEN_TYPE ) {
			return "<errornode>";
		}
		if ( token==null ) {
			return null;
		}
		return token.getText();
    }
    
    public String getTokenInput() {
    	CharStream is = token.getInputStream();
    	if ( is==null ) {
			return null;
		}
		int n = is.size();
		if ( getStartIndex()<n && getStopIndex()<n) {
			return is.substring(getStartIndex(),getStopIndex());
		}
		else {
			return "<EOF>";
		}
    }
    
    public int getStartIndex() {
    	return ((CommonToken)token).getStartIndex();
    }
    
    public int getStopIndex() {
    	return ((CommonToken)token).getStopIndex();
    }
    
}
