'''
Created on Feb 4, 2011

@author: rca
'''
import unittest
from montysolr import handler
from montysolr.python_bridge import JVMBridge
from montysolr.utils import MontySolrTarget
import sys
import os

sj = JVMBridge.getObjMontySolr()

class TestingMethods():
    def montysolr_targets(self):
        
        def test_a(message):
            data = sj.JArray_int.cast_(message.getParam("data"))
            data = data * 2
            message.setParam("result", sj.JArray_int(data))
            
        def test_b(message):
            data = sj.JArray_int.cast_(message.getParam("data"))
            data = str(data)
            message.setParam("result", data)
        
        return [
           MontySolrTarget(':test_a', test_a),
           MontySolrTarget(':test_b', test_b),
                ]

class TestHandler(handler.Handler):
    def init(self):
        self.discover_targets([TestingMethods()])

class Test(unittest.TestCase):

    def setUp(self):
        self._handler = JVMBridge._handler
        JVMBridge.setHandler(TestHandler())

    def tearDown(self):
        JVMBridge.setHandler(self._handler)
    
    def test_basic(self):
        """Testing basic functions (outside solr)"""
        sj = JVMBridge.getObjMontySolr()
        message = JVMBridge.createMessage("test_a") \
            .setParam('data', sj.JArray_int([0,1,2]))
        
        JVMBridge.sendMessage(message)
        res = list(sj.JArray_int.cast_(message.getParam("result")))
        assert res == [0, 1, 2, 0, 1, 2]
        
        #lets reuse the message object
        message.setReceiver("test_b")
        JVMBridge.sendMessage(message)
        res = str(message.getParam("result"))
        assert res.find("[0, 1, 2]") > -1
        
        message = JVMBridge.createMessage("test_b") \
            .setParam('data', sj.JArray_int([0,1,2]))
        JVMBridge.sendMessage(message)
        res = str(message.getParam("result"))
        assert res.find("[0, 1, 2]") > -1
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()