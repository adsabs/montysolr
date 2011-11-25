# -*- coding: utf8 -*-

import unittest
from montysolr import handler
from montysolr.initvm import JAVA as J
from montysolr.python_bridge import JVMBridge
from montysolr.utils import make_targets

from monty_newseman import targets
import sys
import os



class Test(unittest.TestCase):
    
    def setUp(self):
        self._old_handler = JVMBridge.getHandler()
        self.bridge = JVMBridge
        self.set_targets(targets)
        targets.Cacher.cache = {}
        self.basedir = os.path.dirname(os.path.abspath(os.path.abspath(__file__) + '../../../..'))
        #self.seman_url = 'sqlite:///%s/resources/test_database.sqlite' % self.basedir
        self.seman_url = 'sqlite:///:memory:'

    def tearDown(self):
        JVMBridge.setHandler(self._old_handler)
    
    
    def set_targets(self, targets):
        if not isinstance(targets, list):
            targets = [targets]
            
        class TestHandler(handler.Handler):
            def init(self):
                self.discover_targets(targets)
        self.bridge.setHandler(TestHandler())
        
        
    def test_create_seman(self):
        """Testing seman initialization"""
        
        message = self.bridge.createMessage("initialize_seman") \
            .setParam('url', self.seman_url) \
            .setParam('language', 'czech')
            
            
            
    def test_translate_token_collection(self):
        
        
        message = self.bridge.createMessage("initialize_seman") \
            .setParam('url', self.seman_url) \
            .setParam('language', 'czech')
        self.bridge.sendMessage(message)
        
        
        # cheating, get the python object
        seman = targets.Cacher.get_last()
        d = seman.surface().getSurfaceDictionary()
        fill_dictionary(d)
            
        
        words = "velká světová revoluce byla Velká říjnová revoluce protože s Velkou říjnovou revolucí \
        a bez velké říjnové revoluce a ještě velká říjnová revoluce socialistická komunistická \
        s velkou a říjnovou revolucí".split()
        
        t_array = J.JArray_object(len(words))
        i = 0
        while i < len(words):
            t = J.JArray_string(4)
            t[0] = "token"
            t[1] = words[i]
            t[2] = 'id'
            t[3] = str(i)
            t_array[i] = t
            i += 1
        
        message = self.bridge.createMessage("translate_token_collection")
        message.setParam("tokens", t_array)
        
        self.bridge.sendMessage(message)
        
        # this would be done on java side
        results = message.getResults()
        
        assert results != None
        
        group_found = False
        results = J.JArray_object.cast_(results)
        for token in results:
            token = J.JArray_string.cast_(token)
            print token
            if len(token) > 4:
                assert token[4] == 'sem'
                if token[5] == 'X X X':
                    group_found = True
        
        assert group_found == True
        


def fill_dictionary(d):
    #radixes
    d.addEntry(key= u"velk", language='czech', type='sem', group= u'radix', value= u'r0')
    d.addEntry(key= u"svět", language='czech', type='sem', group= u'radix', value= u'r1')
    d.addEntry(key= u"revol", language='czech', type='sem', group= u'radix', value= u'r2')
    d.addEntry(key= u"říjn", language='czech', type='sem', group= u'radix', value= u'r3')
    d.addEntry(key= u"byl", language='czech', type='sem', group= u'radix', value= u'r4')
    d.addEntry(key= u"social", language='czech', type='sem', group= u'radix', value= u'r5')
    d.addEntry(key= u"komun", language='czech', type='sem', group= u'radix', value= u'r6')
    
    #suffixes
    d.addEntry(key= u"á", language='czech', type='sem', group= u'suffix', value= u's1')
    d.addEntry(key= u"ová", language='czech', type='sem', group= u'suffix', value= u's2')
    d.addEntry(key= u"ové", language='czech', type='sem', group= u'suffix', value= u's2x')
    d.addEntry(key= u"uce", language='czech', type='sem', group= u'suffix', value= u's3')
    d.addEntry(key= u"a", language='czech', type='sem', group= u'suffix', value= u's4')
    d.addEntry(key= u"e", language='czech', type='sem', group= u'suffix', value= u's3')
    d.addEntry(key= u"é", language='czech', type='sem', group= u'suffix', value= u's3')
    d.addEntry(key= u"ou", language='czech', type='sem', group= u'suffix', value= u's4')
    d.addEntry(key= u"ovou", language='czech', type='sem', group= u'suffix', value= u's5')
    d.addEntry(key= u"ucí", language='czech', type='sem', group= u'suffix', value= u's6')
    d.addEntry(key= u"istická", language='czech', type='sem', group= u'suffix', value= u's7')
    
    #immutables
    d.addEntry(key= u"bez", language='czech', type='sem', group= u'immutable', value= u'r0')
    d.addEntry(key= u"protože", language='czech', type='sem', group= u'immutable', value= u'r0')
    d.addEntry(key= u"a", language='czech', type='sem', group= u'immutable', value= u'r0')
    d.addEntry(key= u"ještě", language='czech', type='sem', group= u'immutable', value= u'r0')
    
    #skupina slov
    d.addEntry(key= u"velk říjn revol", language='czech', type='sem', group= u'radix', value= u'XXX')
    d.addEntry(key= u"velk říjn revol social marx", language='czech', type='sem', group= u'radix', value= u'MARX') #this should be missed, only similar
    d.addEntry(key= u"ústav inform stud a knihovnic", language='czech', type='sem', group= u'radix', value= u'UISK')
    
    #multiple definitions
    d.addEntry(key= u"jeden", language='czech', type='entity', group= u'radix', value= u'm0 m9')
    d.addEntry(key= u"člověk", language='czech', type='entity', group= u'radix', value= u'm1')
    d.addEntry(key= u"jeden", language='czech', type='entity', group= u'radix', value= u's1 s2')
    d.addEntry(key= u"člověk", language='czech', type='entity', group= u'radix', value= u's3')
    
    d.saveChanges()
        
        
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()