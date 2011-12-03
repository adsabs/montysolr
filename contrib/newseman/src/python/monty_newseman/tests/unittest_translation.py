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
        h = ["token", "id", "sem", "extra-sem", "synonyms", "extra-synonyms", "pos", "extra-canonical"]
        out = {}
        
        for x in h:
            out.setdefault(x, [])
        for x in header:
            out.setdefault(x, [])
            
        for i in range(1, len((results))):
            row = list(J.JArray_string.cast_(results[i]))
            for ii in range(len(header)):
                key = header[ii]
                if len(row) > ii:
                    out[key].append(row[ii])
                else:
                    out[key].append(None)
        return out
        
            
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
        
        data = self._get_results(results)
        sem = data['sem']
        mulsem = data['extra-sem']
        mulsyn = data['extra-synonyms']
        
        assert sem.count('XXX') == 5
        assert mulsem.count('XXX') == 0
        assert sem.count('r2') == 1 #revolution
    
    
    
    def test_translate_add_purge(self):
        
        message = self.bridge.createMessage("initialize_seman") \
            .setParam('url', self.seman_url) \
            .setParam('language', 'czech')
            
            
        self.bridge.sendMessage(message)
        
        self._fill_dictionary()
        
        message = self.bridge.createMessage("configure_seman") \
            .setParam('url', self.seman_url) \
            .setParam('language', 'czech') \
            .setParam('max_distance', 2) \
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
        
        data = self._get_results(results)
        sem = data['sem']
        mulsem = data['extra-sem']
        mulcan = data['extra-canonical']
        mulsyn = data['extra-synonyms']
        
        
        assert sem.count('XXX') == 4
        assert mulsem.count('XXX') == 1
        assert mulcan.count('velk říjn revol') == 1
        assert sem.count('r2') == 1 #revolution
        
        
        # add extra word which makes the distance too big
        words = "velká světová revoluce byla velká říjnová revoluce protože s velkou říjnovou revolucí \
        a bez velké říjnové revoluce a ještě velká říjnová revoluce socialistická komunistická \
        s velkou a extra říjnovou revolucí".split()
        
        
        message = self.bridge.createMessage("translate_tokens")
        message.setParam("tokens", self._create_array(words))
        self.bridge.sendMessage(message)
        
        
        results = message.getResults()
        data = self._get_results(results)
        sem = data['sem']
        mulsem = data['extra-sem']
        mulsyn = data['extra-synonyms']
        
        assert sem.count('XXX') == 4
        assert mulsyn.count('XXX') == 0
        assert mulsyn.count('velk říjn revol') == 0
        assert sem.count('r2') == 2 #revolution
        
        
        
        
        words = 'velká světová revoluce byla velká říjnová revoluce bez velké extra říjnové revoluce'.split()
        
        message = self.bridge.createMessage("translate_tokens")
        message.setParam("tokens", self._create_array(words))
        self.bridge.sendMessage(message)
        
        
        results = message.getResults()
        data = self._get_results(results)
        sem = data['sem']
        token = data['token']
        ids = data['id']
        mulsem = data['extra-sem']
        mulsyn = data['extra-synonyms']
        
        assert len(ids) == len(set(ids))
        assert token.count('extra') == 1
        assert sem.count('XXX') == 1
        assert mulsem.count('XXX') == 1
        assert sem.count('r2') == 1
        assert ' '.join(token) == ' '.join(words)
        assert '.'.join(token) != '.'.join(words)



def fill_dictionary(d):
    #radixes
    d.addEntry(key= u"velk", language='czech', type='sem', group= u'radix', value= u'r0')
    d.addEntry(key= u"svět", language='czech', type='sem', group= u'radix', value= u'r1')
    d.addEntry(key= u"revol", language='czech', type='sem', group= u'radix', value= u'r2')
    d.addEntry(key= u"říjn", language='czech', type='sem', group= u'radix', value= u'r3')
    d.addEntry(key= u"byl", language='czech', type='sem', group= u'radix', value= u'r4')
    d.addEntry(key= u"social", language='czech', type='sem', group= u'radix', value= u'r5')
    d.addEntry(key= u"komun", language='czech', type='sem', group= u'radix', value= u'r6 r7')
    
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
    d.addEntry(key= u"bez", language='czech', type='sem', group= u'immutable', value= u'i0')
    d.addEntry(key= u"protože", language='czech', type='sem', group= u'immutable', value= u'i0')
    d.addEntry(key= u"a", language='czech', type='sem', group= u'immutable', value= u'i0')
    d.addEntry(key= u"ještě", language='czech', type='sem', group= u'immutable', value= u'i0')
    
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