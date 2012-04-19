package org.apache.lucene.queryParser.aqp.config;


/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.util.Attribute;


/**
 * This attribute is used to collect feedback messages and suggestions
 * from the query parser
 * 
 * WARNING: experimental, may change soon!
 */
public interface AqpFeedback extends Attribute {
	
	public enum TYPE { DEBUG, INFO, WARN, ERROR, SYNTAX_SUGGESTION, DEPRECATED };
	
	/*
	 * I am NOT trying to re-implement a wheel, I am just 
	 * confused what is the proper way to wrap SLF4J used
	 * by SOLR (but not by Lucene) and not introduce it as
	 * a dependency to Lucene
	 */
	public AqpFeedbackEvent createEvent(TYPE level, 
			Class<? extends QueryNodeProcessor> qnClass, 
			QueryNode node, 
			String msg, Object...args);
	
	public void sendEvent(AqpFeedbackEvent event);
	

	
	public void registerEventHandler(AqpFeedbackEventHandler handler);
}
