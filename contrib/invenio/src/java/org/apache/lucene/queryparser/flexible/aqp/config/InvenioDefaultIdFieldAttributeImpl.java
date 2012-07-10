package org.apache.lucene.queryparser.flexible.aqp.config;

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

import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQNORMALProcessor;
import org.apache.lucene.util.AttributeImpl;

/**
 * This attribute is used by {@link AqpQNORMALProcessor} processor and must be
 * defined in the {@link QueryConfigHandler}. This attribute tells the processor
 * what is the default field when no field is defined in a phrase. <br/>
 * 
 */
public class InvenioDefaultIdFieldAttributeImpl extends AttributeImpl implements
		InvenioDefaultIdFieldAttribute {

	private static final long serialVersionUID = 3664676068061966884L;
	protected String defaultField = "defaultField";

	public void setDefaultIdField(String defaultField) {
		this.defaultField = defaultField;
	}

	public String getDefaultIdField() {
		return this.defaultField;
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public void copyTo(AttributeImpl target) {
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return "<defaultIdField value=" + this.defaultField + "/>";
	}

}
