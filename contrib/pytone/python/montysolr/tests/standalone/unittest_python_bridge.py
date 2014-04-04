'''
Created on Feb 4, 2011

@author: rca
'''
import unittest
from montysolr import handler
from montysolr.tests.montysolr_testcase import LuceneTestCase
from montysolr.utils import MontySolrTarget
from montysolr import initvm
j = initvm.JAVA


class TestingMethods():
    def montysolr_targets(self):
        
        def test_a(message):
            data = j.JArray_int.cast_(message.getParam("data"))
            data = data * 2
            message.setParam("result", j.JArray_int(data))
            
        def test_b(message):
            data = j.JArray_int.cast_(message.getParam("data"))
            data = str(data)
            message.setParam("result", data)
        
        return [
           MontySolrTarget('test_a', test_a),
           MontySolrTarget('test_b', test_b),
                ]

class TestHandler(handler.Handler):
    def init(self):
        self.discover_targets([TestingMethods()])

class Test(LuceneTestCase):

    def setUp(self):
        LuceneTestCase.setUp(self)
        self.setHandler(TestHandler())

    
    def test_basic(self):
        """Testing basic functions (outside solr)"""
        message = self.bridge.createMessage("test_a") \
            .setParam('data', j.JArray_int([0,1,2]))
        
        self.bridge.sendMessage(message)
        res = list(j.JArray_int.cast_(message.getParam("result")))
        assert res == [0, 1, 2, 0, 1, 2]
        
        #lets reuse the message object
        message.setReceiver("test_b")
        self.bridge.sendMessage(message)
        res = str(message.getParam("result"))
        assert res.find("[0, 1, 2]") > -1
        
        message = self.bridge.createMessage("test_b") \
            .setParam('data', j.JArray_int([0,1,2]))
        self.bridge.sendMessage(message)
        res = str(message.getParam("result"))
        assert res.find("[0, 1, 2]") > -1
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()