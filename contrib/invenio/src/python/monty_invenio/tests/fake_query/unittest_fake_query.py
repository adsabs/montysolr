
from montysolr.initvm import JAVA as j
from montysolr.tests.montysolr_testcase import LuceneTestCase

import targets
import random

'''
Here we test (on the Python side) the fake_query.targets --
on the Java side, something similar is run in one of the 
InvenioQuery tests
'''

class Test(LuceneTestCase):


    def setUp(self):
        LuceneTestCase.setUp(self)
        self.setTargets(targets)
        
    def test_indexing(self):
        message = self.bridge.createMessage("index_docs").setParam('no_docs', 10)
        self.bridge.sendMessage(message)
        
        text = message.getParam('text')
        recid = message.getParam('recid')
        docs = message.getParam('docs')
        
        assert text != None
        assert recid != None
        assert docs != None
        
        text = list(j.JArray_string.cast_(text))
        docs = list(j.JArray_string.cast_(docs))
        recid = list(j.JArray_int.cast_(recid))
        
        assert len(text) > 10
        assert len(recid) == 10
        assert len(set(recid)) == 10
        assert 0 in recid and 9 in recid
        
        
    def test_search(self):
        message = self.bridge.createMessage("index_docs").setParam('no_docs', 10)
        self.bridge.sendMessage(message)
        docs = self.get_docs(message)
        
        text = list(set(docs['text']))
        
        for x in range(5):
            r_word = text[random.randint(0, len(text)-1)]
            
            message = self.bridge.createMessage("fake_search").setParam('query', r_word)
            self.bridge.sendMessage(message)
            
            res = message.getResults()
            assert res != None
            
            hits = list(j.JArray_int.cast_(res))
            
            hits = self.get_hits(r_word)
            
            if r_word in docs['index']:
                assert len(hits) == len(docs['index'][r_word])
            else:
                assert len(hits) == 0
            
            
        message = self.bridge.createMessage("fake_search").setParam('query', r_word+'bla')
        self.bridge.sendMessage(message)
        hits = list(j.JArray_int.cast_(message.getResults()))
        assert len(hits) == 0
        
        
    def get_hits(self, q):
        message = self.bridge.createMessage("fake_search").setParam('query', q)
        self.bridge.sendMessage(message)
        hits = list(j.JArray_int.cast_(message.getResults()))
        return hits
    
    def get_docs(self, message):
        text = list(j.JArray_string.cast_(message.getParam('text')))
        recid = list(j.JArray_int.cast_(message.getParam('recid')))
        docs = list(j.JArray_string.cast_(message.getParam('docs')))
        out = {'text': text, 'recid': recid, 'index': {}}
        index = out['index']
        for d in docs:
            parts = d.split(' ')
            for p in parts[1:]:
                index.setdefault(p, [])
                index[p].append(parts[0])
        return out