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


import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.queryParser.aqp.config.AqpFeedbackEventHandler.ACTION;


public class AqpFeedbackImpl extends AttributeImpl 
				implements AqpFeedback {
	
	private static final long serialVersionUID = 5178148416076100953L;
	

	private List<AqpFeedbackEvent> events =  new ArrayList<AqpFeedbackEvent>();
	private List<AqpFeedbackEventHandler> handlers = new ArrayList<AqpFeedbackEventHandler>();
	
	@Override
	public void clear() {
		events.clear();
	}

	@Override
	public void copyTo(AttributeImpl target) {
		throw new UnsupportedOperationException();
	}

	public void sendEvent(AqpFeedback.TYPE type, QueryNode node, String msg,
			Object... args) {
		
		AqpFeedbackEvent event = new AqpFeedbackEvent(type, node, msg, args);
		for (AqpFeedbackEventHandler handler : handlers) {
			ACTION r = handler.handle(event);
			if (r == ACTION.BREAK) {
				break;
			}
			else if (r == ACTION.STOP) {
				return;
			}
			
		}
		events.add(event);
		
	}

	public void registerEventHandler(AqpFeedbackEventHandler handler) {
		handlers.add(handler);
	}


	

}


