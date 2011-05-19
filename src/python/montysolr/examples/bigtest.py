'''
Created on Feb 4, 2011

@author: rca
'''

from montysolr.initvm import montysolr_java as sj
from montysolr.utils import MontySolrTarget


import random
import time

def bigtest(message):
    req = message.getSolrQueryRequest()
    rsp = message.getSolrQueryResponse()

    params = req.getParams()
    action = params.get("action")

    start = time.time()

    if 'recids' in action:

        size = params.getInt("size", 5000)

        if action == 'recids_int':
            result = range(0, size)
            result = sj.JArray_int(result)
        elif action == 'recids_str':
            result = [ '%s' % x for x in xrange(size)]
            result = sj.JArray_string(result)
        elif action == 'recids_hm_strstr':
            result = sj.HashMap().of_(sj.String, sj.String)
            for x in xrange(size):
                result.put(str(x), str(x))
        elif action == 'recids_hm_strint':
            result = sj.HashMap().of_(sj.String, sj.Integer)
            for x in xrange(size):
                result.put(str(x), x)
        elif action == 'recids_hm_intint':
            result = sj.HashMap().of_(sj.Integer, sj.Integer)
            for x in xrange(size):
                result.put(x, x)
        elif action == 'recids_bitset':
            from invenio import intbitset
            filled = int(params.getInt('filled').intValue())
            result = intbitset.intbitset(rhs=size)
            step = int(size / filled)
            for x in xrange(0, size, step):
                result.add(x)
            result = sj.JArray_byte(result.fastdump())
        else:
            result = None

        message.setResults(result)

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
    targets = [
           MontySolrTarget(':bigtest', bigtest),
           ]
    return targets
