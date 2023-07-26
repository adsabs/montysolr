/*
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
package org.apache.solr.core;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Locale;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.DocValuesFormat;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.codecs.lucene50.Lucene50StoredFieldsFormat.Mode;
import org.apache.lucene.codecs.lucene62.Lucene62Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Debugging version of the solr codec; we can't use -Dtests.codec=SimpleText
 * 
 * @lucene.experimental
 */
public class SimpleTextCodecFactory extends CodecFactory implements SolrCoreAware {
  
  /**
   * Key to use in init arguments to set the compression mode in the codec.
   */
  public static final String COMPRESSION_MODE = "compressionMode";
  
  public static final Mode SOLR_DEFAULT_COMPRESSION_MODE = Mode.BEST_SPEED;
  
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  
  private Codec codec;
  private volatile SolrCore core;
  
  
  // TODO: we need to change how solr does this?
  // rather than a string like "Direct" you need to be able to pass parameters
  // and everything to a field in the schema, e.g. we should provide factories for 
  // the Lucene's core formats (Memory, Direct, ...) and such.
  //
  // So I think a FieldType should return PostingsFormat, not a String.
  // how it constructs this from the XML... i don't care.

  @Override
  public void inform(SolrCore core) {
    this.core = core;
  }

  @Override
  public void init(NamedList args) {
    super.init(args);
    assert codec == null;
    String compressionModeStr = (String)args.get(COMPRESSION_MODE);
    Mode compressionMode;
    if (compressionModeStr != null) {
      try {
        compressionMode = Mode.valueOf(compressionModeStr.toUpperCase(Locale.ROOT));
      } catch (IllegalArgumentException e) {
        throw new SolrException(ErrorCode.SERVER_ERROR, 
            "Invalid compressionMode: '" + compressionModeStr + 
            "'. Value must be one of " + Arrays.toString(Mode.values()));
      }
      log.debug("Using compressionMode: " + compressionMode);
    } else {
      compressionMode = SOLR_DEFAULT_COMPRESSION_MODE;
      log.debug("Using default compressionMode: " + compressionMode);
    }
    codec = new SimpleTextCodec();
  }

  @Override
  public Codec getCodec() {
    assert core != null : "inform must be called first";
    return codec;
  }
}
