package org.apache.lucene.queryparser.flexible.aqp;

import java.util.Map;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

public class AqpCommonTree extends CommonTree {
	
	protected Map<Integer, String> typeToNameMap;
	
	public AqpCommonTree() {
		super();
	}
	
	public AqpCommonTree(CommonTree node) {
		super(node);
	}
	
	public AqpCommonTree(AqpCommonTree node) {
		super(node);
	}
	
	public AqpCommonTree(Token t) {
		this.token = t;
	}
	
	public Tree dupNode() {
		AqpCommonTree r = new AqpCommonTree(this);
		r.setTypeToNameMap(this.typeToNameMap);
		return r;
	}
	
	/** Return the whole tree converted to QueryNode tree */
    public QueryNode toQueryNodeTree()
    	throws RecognitionException {
		if ( children==null || children.size()==0 ) {
			return this.toQueryNode();
		}
		
		QueryNode buf = toQueryNode();
		for (int i = 0; children!=null && i < children.size(); i++) {
			Object child = children.get(i);
			if (child instanceof CommonErrorNode) {
				throw ((CommonErrorNode) child).trappedException;
			}
			AqpCommonTree t = (AqpCommonTree)child;
			buf.add(t.toQueryNodeTree());
		}
		return buf;
	}
    
    public QueryNode toQueryNode() {
    	return (QueryNode) new AqpANTLRNode(this);
    }
    
    public String getTokenLabel() {
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
    
    public int getTokenType() {
    	return token.getType();
    }
    
    public String getTypeLabel() {
    	int t = getTokenType();
    	return typeToNameMap.get(t);
    }
    
    public int getStartIndex() {
    	return ((CommonToken)token).getStartIndex();
    }
    
    public int getStopIndex() {
    	return ((CommonToken)token).getStopIndex();
    }
    
    public void setTypeToNameMap(Map<Integer, String> typeMap) {
    	typeToNameMap = typeMap; 
    }
    
}
