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

import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.aqp.processors.AqpQNORMALProcessor;
import org.apache.lucene.util.AttributeImpl;

/**
 * This attribute is used by {@link AqpQNORMALProcessor} processor and
 * must be defined in the {@link QueryConfigHandler}. This attribute tells the
 * processor what is the default field when no field is defined in a
 * phrase. <br/>
 * 
 */
public class DefaultFieldAttributeImpl extends AttributeImpl 
				implements DefaultFieldAttribute {


	private static final long serialVersionUID = -4268918577762615525L;
	
	protected String defaultField = "defaultField";

	@Override
	public void setDefaultField(String defaultField) {
		this.defaultField = defaultField;
	}

	@Override
	public String getDefaultField() {
		return this.defaultField;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void copyTo(AttributeImpl target) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	  public String toString() {
	    return "<defaultField defaultField=" + this.defaultField
	        + "/>";
	  }


}

