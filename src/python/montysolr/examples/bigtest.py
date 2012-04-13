'''
Created on Feb 4, 2011

@author: rca
'''

from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets


import random
import time


JArray_string = j.JArray_string #@UndefinedVariable
JArray_int = j.JArray_int #@UndefinedVariable
JArray_byte = j.JArray_byte #@UndefinedVariable
SolrQueryResponse = j.SolrQueryResponse #@UndefinedVariable
SolrQueryRequest = j.SolrQueryRequest #@UndefinedVariable
Integer = j.Integer #@UndefinedVariable
HashMap = j.HashMap #@UndefinedVariable
List = j.List #@UndefinedVariable
String = j.String #@UndefinedVariable
ArrayList = j.ArrayList #@UndefinedVariable


def bigtest(message):
    action = str(message.getParam('action'))
    size = message.getParam_int("size", 5000)
    filled = message.getParam_int("filled", 1000)
    
    if 'recids' in action:
        if action == 'recids_int':
            result = range(0, size)
            result = JArray_int(result)
        elif action == 'recids_str':
            result = [ '%s' % x for x in xrange(size)]
            result = JArray_string(result)
        elif action == 'recids_hm_strstr':
            result = HashMap().of_(String, String)
            for x in xrange(size):
                result.put(str(x), str(x))
        elif action == 'recids_hm_strint':
            result = HashMap().of_(String, Integer)
            for x in xrange(size):
                result.put(str(x), x)
        elif action == 'recids_hm_intint':
            result = HashMap().of_(Integer, Integer)
            for x in xrange(size):
                result.put(x, x)
        elif action == 'recids_bitset':
            from invenio import intbitset
            result = intbitset.intbitset(rhs=size)
            step = int(size / filled)
            for x in xrange(0, size, step):
                result.add(x)
            result = JArray_byte(result.fastdump())
        else:
            result = None

        message.setResults(result)

def bigtest_www(message):
    req = SolrQueryRequest.cast_(message.getParam('request'))
    rsp = SolrQueryResponse.cast_(message.getParam('response'))

    params = req.getParams()
    action = params.get("action")

    start = time.time()

    if 'recids' in action:
        size = params.getInt("size", 5000)
        filled = int(params.getInt('filled', 1000).intValue())
        message.setParam('size', size)
        message.setParam('filled', filled)
        message.setParam('action', action)
        bigtest_www(message)
    else:
        help = '''
        action:args:description
        recids_int:@size(int):returns array of integers of the given size
        recids_str:@size(int):returns array of strings of the given size
        recids_hm_strstr:@size(int):returns hashmap of string:string of the given size
        recids_hm_strint:@size(int):returns hashmap of string:int of the given size
        recids_hm_intint:@size(int):returns hashmap of int:int of the given size
        recids_bitset:@size(int) - size of the bitset; @filled(int) - number of elements that are set:returns bit array (uses invenio bitset for the transfer)
        '''
        for line in help.split('\n'):
            rsp.add('python-message', line.strip())


    rsp.add('python-message', 'Python call finished in: %s ms.' % (time.time() - start))


def montysolr_targets():
    targets = make_targets(bigtest=bigtest, bigtest_www=bigtest_www);
    return targets
