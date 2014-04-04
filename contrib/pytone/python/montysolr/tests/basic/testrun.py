

from montysolr.tests.montysolr_testcase import MontySolrTestCase, initvm
from montysolr.tests.basic import bridge, handler

from monty.solr.jni import PythonMessage

JAVA = initvm.JAVA

#PythonMessage = sj.PythonMessage #@UndefinedVariable
JArray_string = JAVA.JArray_string #@UndefinedVariable

class Test(MontySolrTestCase):


    def setUp(self):
        MontySolrTestCase.setUp(self)
        self.bridge = bridge.Bridge()
    
    def test_text(self):
        message = PythonMessage('receive_text').setParam('arg', 'test')
        self.bridge.receive_message(message)
        res = message.getResults()
        
        self.assertFalse('testx' == res, "Java returned Python obj?")
        self.assertTrue('testx' == str(res), "Java returned Python obj?")
        self.assertFalse('testx' == res, "Java returned Python obj?")
        #self.assertFalse(String('testx').equals(String.cast_(res)), "Java returned Python obj?")
    
    def test_text_array(self):
        message = PythonMessage('receive_text_array').setParam('arg', JArray_string(['x','y']))
        self.bridge.receive_message(message)
        res = JArray_string.cast_(message.getResults())
        expected = JArray_string(['x','y','z'])
        
        self.assertTrue(expected == res, "Java returned Python obj?")
        #self.assertTrue(expected.equals(res), "Java obj.equals() doesn't work for array")
        as_list = list(res)
        self.assertTrue(as_list == ['x', 'y', 'z'])
        
    def test_text_array_big(self):
        data = map(str, range(500000))
        message = PythonMessage('receive_text_array').setParam('arg', JArray_string(data))
        self.bridge.receive_message(message)
        res = JArray_string.cast_(message.getResults())
        expected = JArray_string(data + ['z'])
        
        self.assertTrue(expected == res, "Big array is missing")
        
