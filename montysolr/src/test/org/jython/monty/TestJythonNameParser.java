package org.jython.monty;

import java.util.Map;

import org.apache.lucene.util.LuceneTestCase;
import org.jython.JythonObjectFactory;
import org.jython.monty.interfaces.JythonNameParser;
import org.python.core.PySystemState;

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

public class TestJythonNameParser extends LuceneTestCase {
  
  
  public void tearDown() throws Exception {
    System.clearProperty("python.cachedir.skip");
    System.clearProperty("python.console.encoding");
    super.tearDown();
  }
  
  public void test() {
    //PythonInterpreter interp = new PythonInterpreter();
    //interp.exec("import sys; print sys.path");
    //interp.exec("sys.path.append('/var/lib/montysolr/perpetuum/python/lib/python2.7/site-packages')");
    
    PySystemState state = new PySystemState();
    JythonObjectFactory factory = new JythonObjectFactory(state, JythonNameParser.class, "jython_name_parser", "HumanParser");
    
    JythonNameParser instance = (JythonNameParser) factory.createObject();
    
    Map<String,String> res = instance.parse_human_name("Jimmi Hendrix");
    assertEquals(res.get("First"), "Jimmi");
    assertEquals(res.get("Last"), "Hendrix");
    
    for (int i=0; i<100; i++) {
      res = instance.parse_human_name("Jimmi Hendrix");
      if (i % 100 == 0) {
        //System.out.println(i);
      }
    }

    res = instance.parse_human_name("Stephen M'   Donald");
    assertEquals(res.get("First"), "Stephen");
    assertEquals(res.get("Last"), "M'Donald");
    
  }
  
}
