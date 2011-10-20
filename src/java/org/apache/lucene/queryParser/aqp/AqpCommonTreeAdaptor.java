package org.apache.lucene.queryParser.aqp;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;

import org.apache.lucene.queryParser.aqp.AqpCommonTree;

public class AqpCommonTreeAdaptor extends CommonTreeAdaptor {
	
	public Object create(Token payload) {
		return new AqpCommonTree(payload);
	}
	
	public Token getToken(Object t) {
		if ( t instanceof AqpCommonTree ) {
			return ((AqpCommonTree)t).getToken();
		}
		return null; // no idea what to do
	}
}
