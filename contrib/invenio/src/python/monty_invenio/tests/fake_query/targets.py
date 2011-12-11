# -*- coding: utf8 -*-

from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets

import random

_docs = {}

def index_docs(message):
    _docs.clear()
    
    no_docs = message.getParam('no_docs')
    if not no_docs:
        no_docs = 100
    else:
        no_docs = int(str(no_docs))
    
    words = map(lambda x: 'x%s' % x, range(no_docs * 3))
    random.shuffle(words)
    words = words[int(len(words)/2):]
    recids = range(no_docs)
    random.shuffle(recids)
    
    
    text = {}
    recid = {}
    _docs['text'] = text
    _docs['recid'] = recid
    
    i = 0
    docstr = []
    for x in range(no_docs):
        recid = recids[x]
        doc = {'recid': recid, 'text': words[i:i+3]}
        _docs[i] = doc
        for w in words[i:i+3]:
            text.setdefault(w, [])
            text[w].append(recid)
        docstr.append('%s %s' % (recid, ' '.join(words[i:i+3])))
        i += 6
        if i > len(words):
            i = 0
        
    message.setParam('text', j.JArray_string(words))
    message.setParam('recid', j.JArray_int(recids))
    message.setParam('docs', j.JArray_string(docstr))


def fake_search(message):
    """receives the query and returns ids"""
    q = str(message.getParam("query"))
    if ':' in q:
        field, val = q.split(':')
    else:
        field, val = 'text', q
        
    index = _docs[field]
    
    ret = []
    if val in index:
        ret.extend(index[val])
    
    message.setResults(j.JArray_int(ret))
    
    

def fake_search_intbitset(message):    
    """receives the query and returns ids encoded
    as intbitsets"""
    
    from invenio import intbitset
    
    q = str(message.getParam("query"))
    if ':' in q:
        field, val = q.split(':')
    else:
        field, val = 'text', q
        
    index = _docs[field]
    
    ret = intbitset.intbitset()
    if val in index:
        for i in index[val]:
            ret.add(i)
    
    message.setResults(j.JArray_byte(ret.fastdump()))


def montysolr_targets():
    return make_targets("InvenioQuery:fake_search", fake_search,
                        "InvenioQuery:fake_search_intbitset", fake_search_intbitset,
                        fake_search=fake_search,
                        index_docs=index_docs,
                        
                        )
