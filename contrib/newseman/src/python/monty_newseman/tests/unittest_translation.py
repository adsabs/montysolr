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
            
    
    def _fill_dictionary(self):
        # cheating, get the python object
        seman = targets.Cacher.get_last()
        d = seman.surface().getSurfaceDictionary()
        fill_dictionary(d)
    
    def _create_array(self, words):
        
        header = ['token', 'id']
        t_array = J.JArray_object(len(words)+1)
        
        header = J.JArray_string(['token', 'id'])
        t_array[0] = header 
        
        w = 0
        while w < len(words):
            t = J.JArray_string(len(header))
            t[0] = words[w]
            t[1] = str(w)
            t_array[w+1] = t
            w += 1
        return t_array
    
    def _get_results(self, results):
        
        results = J.JArray_object.cast_(results)
        
        header = list(J.JArray_string.cast_(results[0]))
        idx_sem = header.index("sem")
        idx_grp = header.index("multi-token")
        idx_grp_sem = header.index("multi-sem")
        
        results = [J.JArray_string.cast_(x) for x in results]
        
        sms = [str(len(x) > idx_sem and x[idx_sem] or '') for x in results]
        grp = [str(len(x) > idx_grp and x[idx_grp] or '') for x in results]
        grs = [str(len(x) > idx_grp_sem and x[idx_grp_sem] or '') for x in results]
        
        return (sms, grp, grs)
            
    def test_translate_basic(self):
        
        
        message = self.bridge.createMessage("initialize_seman") \
            .setParam('url', self.seman_url) \
            .setParam('language', 'czech')
        self.bridge.sendMessage(message)
        
        
        self._fill_dictionary()
            
        words = "velká světová revoluce byla velká říjnová revoluce protože s velkou říjnovou revolucí \
        a bez velké říjnové revoluce a ještě velká říjnová revoluce socialistická komunistická \
        s velkou říjnovou revolucí".split()
        
        
        message = self.bridge.createMessage("translate_tokens")
        message.setParam("tokens", self._create_array(words))
        self.bridge.sendMessage(message)
        
        
        # this would be done on java side
        results = message.getResults()
        assert results != None
        (sms, grp, grs) = self._get_results(results)
        assert sms.count('XXX') == 5
        assert grs.count('XXX') == 0
        assert sms.count('r2') == 1 #revolution
    
    def test_translate_add_purge(self):
        
        message = self.bridge.createMessage("initialize_seman") \
            .setParam('url', self.seman_url) \
            .setParam('language', 'czech')
            
            
        self.bridge.sendMessage(message)
        
        self._fill_dictionary()
        
        message = self.bridge.createMessage("configure_seman") \
            .setParam('url', self.seman_url) \
            .setParam('language', 'czech') \
            .setParam('max_distance', 1) \
            .setParam('grp_action', 'add') \
            .setParam('grp_cleaning', 'purge')
            
        self.bridge.sendMessage(message)
        
        words = "velká světová revoluce byla velká říjnová revoluce protože s velkou říjnovou revolucí \
        a bez velké říjnové revoluce a ještě velká říjnová revoluce socialistická komunistická \
        s velkou a říjnovou revolucí".split()
        
        
        message = self.bridge.createMessage("translate_tokens")
        message.setParam("tokens", self._create_array(words))
        self.bridge.sendMessage(message)
        
        # this would be done on java side
        results = message.getResults()
        assert results != None
        (sms, grp, grs) = self._get_results(results)
        assert sms.count('XXX') == 4
        assert grs.count('XXX') == 1
        assert grp.count('velk říjn revol') == 1
        assert sms.count('r2') == 1 #revolution
        
        
        # add extra word which makes the distance too big
        words = "velká světová revoluce byla velká říjnová revoluce protože s velkou říjnovou revolucí \
        a bez velké říjnové revoluce a ještě velká říjnová revoluce socialistická komunistická \
        s velkou a extra říjnovou revolucí".split()
        
        
        message = self.bridge.createMessage("translate_tokens")
        message.setParam("tokens", self._create_array(words))
        self.bridge.sendMessage(message)
        
        
        results = message.getResults()
        (sms, grp, grs) = self._get_results(results)
        assert sms.count('XXX') == 4
        assert grs.count('XXX') == 0
        assert grp.count('velk říjn revol') == 0
        assert sms.count('r2') == 2 #revolution

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