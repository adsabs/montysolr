'''
Created on Feb 4, 2011

@author: rca
'''
import unittest
from montysolr import initvm, java_bridge
from montysolr import handler
import sys
import os

sj = java_bridge.sj

class TestHandler(handler.Handler):
    def init(self):
        self.discover_targets([os.path.join(os.path.basedir(__file__), 'testing_targets.py')])

class Test(unittest.TestCase):


    def setUp(self):
        self.bridge = java_bridge.SimpleBridge() 

    def tearDown(self):
        pass
    
    def test_basic(self):
        b = self.bridge
        
        
        assert b.testReturnString().find('java is printing') > -1 
        assert b.getName() is None  # the bridge has name only when started from java
        
        message = sj.PythonMessage('receive_field_value').setParam('value', sj.JArray_string(['x','z']))
        b.receive_message(message)
        ret = message.getParam('result')
        if ret:
            r = list(ret)
            assert r == ['x', 'z']


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()