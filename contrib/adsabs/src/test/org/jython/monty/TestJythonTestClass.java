package org.jython.monty;

import org.apache.lucene.util.LuceneTestCase;
import org.jython.JythonObjectFactory;
import org.jython.monty.interfaces.JythonSimpleClass;
import org.python.util.PythonInterpreter;

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

public class TestJythonTestClass extends LuceneTestCase {
  
  
  public void tearDown() throws Exception {
    System.clearProperty("python.cachedir.skip");
    System.clearProperty("python.console.encoding");
    super.tearDown();
  }
  
  public void test() {
//    PythonInterpreter interp = new PythonInterpreter();
//    interp.exec("import sys; print sys.path");
//    interp.exec("sys.path.append('/dvt/workspace/montysolr/contrib/adsabs/src/jython')");
//    interp.exec("import sys; print sys.path");
    
    JythonObjectFactory factory = new JythonObjectFactory(JythonSimpleClass.class, "simple_class", "SimpleClass");
    
    JythonSimpleClass instance = (JythonSimpleClass) factory.createObject();
    
    instance.set_name("foo-bar");
    assertEquals("foo-bar", instance.get_name());
    
    //interp.exec("print dir(weakref");
  }
  
}
