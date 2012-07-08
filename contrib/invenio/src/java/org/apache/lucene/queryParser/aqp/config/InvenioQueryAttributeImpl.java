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

import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQNORMALProcessor;
import org.apache.lucene.util.AttributeImpl;
import org.apache.solr.common.SolrException;

/**
 * This attribute is used by {@link AqpQNORMALProcessor} processor and
 * must be defined in the {@link QueryConfigHandler}. This attribute tells the
 * processor what is the default field when no field is defined in a
 * phrase. <br/>
 * 
 */
public class InvenioQueryAttributeImpl extends AttributeImpl
				implements InvenioQueryAttribute {
	
	
	
	private ArrayList<String> overridenFields = new ArrayList<String>();
	private QMode mode = QMode.MAXSOLR;
	private Channel channel = Channel.DEFAULT;
	
	
	public void setChannel(String channel) {
		if (channel == null) {
			return;
		}
		if (channel.contains("intbit")) {
			this.channel = Channel.INTBITSET;
		}
		else {
			this.channel = Channel.DEFAULT;
		}
	}
	
	

	
	public void setOverridenFields(String[] overridenFields) {
		if (overridenFields == null) {
			return;
		}
		
		ArrayList<String> xfields = new ArrayList<String>();
		for (String f: overridenFields) {
			if (f.indexOf(",") > -1) {
				for (String x: f.split(",")) {
					xfields.add(x);
				}
			}
			else {
				xfields.add(f);
			}
		}
		setOverridenFields(xfields);
	}

	
	public void setOverridenFields(ArrayList<String> xfields) {
		overridenFields = xfields;
	}

	
	public void setMode(String mode, Boolean schemaHasExplicitAsteriskField) {
		if (mode != null ) {
			if (mode.contains("maxinv") && !schemaHasExplicitAsteriskField) {
				throw new SolrException(
						SolrException.ErrorCode.SERVER_ERROR,
						"Query parser is configured to pass as many fields to Invenio as possible," +
						" for this to work, schema must contain a dynamic field declared as '*'" +
						"<dynamicField name=\"*\" type=\"text\" multiValued=\"true\" />");
			}
			if (mode.contains("maxinv")) {
				this.mode = QMode.MAXINV;
			}
			else if (mode.contains("maxsolr")) {
				this.mode = QMode.MAXSOLR;
			}
			else {
				throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Unknown iq.mode: " + mode);
			}
		}

	}
	
	
	  public void clear() {
	    throw new UnsupportedOperationException();
	  }

	  
	  public void copyTo(AttributeImpl target) {
	    throw new UnsupportedOperationException();
	  }



	
	public Channel getChannel() {
		return channel;
	}



	
	public List<String> getOverridenFields() {
		return overridenFields;
	}



	
	public QMode getMode() {
		return mode;
	}
	

}

