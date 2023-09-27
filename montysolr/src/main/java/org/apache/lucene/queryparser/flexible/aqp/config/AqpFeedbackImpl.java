package org.apache.lucene.queryparser.flexible.aqp.config;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedbackEventHandler.ACTION;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeReflector;

import java.util.ArrayList;
import java.util.List;

public class AqpFeedbackImpl extends AttributeImpl implements AqpFeedback {

    private static final long serialVersionUID = 5178148416076100953L;

    private final List<AqpFeedbackEvent> events = new ArrayList<AqpFeedbackEvent>();
    private final List<AqpFeedbackEventHandler> handlers = new ArrayList<AqpFeedbackEventHandler>();

    @Override
    public void clear() {
        events.clear();
    }

    @Override
    public void copyTo(AttributeImpl target) {
        throw new UnsupportedOperationException();
    }

    public void registerEventHandler(AqpFeedbackEventHandler handler) {
        handlers.add(handler);
    }

    public AqpFeedbackEvent createEvent(TYPE level,
                                        Class<? extends QueryNodeProcessor> qnClass, QueryNode node, String msg,
                                        Object... args) {
        return new AqpFeedbackEventImpl(level, qnClass, node, msg, args);
    }

    public void sendEvent(AqpFeedbackEvent event) {
        for (AqpFeedbackEventHandler handler : handlers) {
            ACTION r = handler.handle(event);
            if (r == ACTION.STOP) {
                return;
            } else if (r == ACTION.SAVE_EVENT) {
                if (!events.contains(event)) {
                    events.add(event);
                }
            }
        }
    }

    @Override
    public void reflectWith(AttributeReflector reflector) {
        for (AqpFeedbackEvent ev : events) {
            reflector.reflect(AqpFeedback.class, "feedback", ev.getType());
        }
    }

}
