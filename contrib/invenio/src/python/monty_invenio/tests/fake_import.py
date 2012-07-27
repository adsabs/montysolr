
import sys
import time
from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets


'''
This mocks up the import 
'''

from java.util import HashMap, ArrayList
from java.lang import String 

JArray_int = j.JArray_int #@UndefinedVariable

_CACHE = {'recids':{}} 

def get_msg_dict(message):
    out = {'ADDED':[], 'UPDATED':[], 'DELETED':[]}
    for name in ['ADDED', 'UPDATED', 'DELETED']:
        if message.getParam(name):
            out[name] = [int(x) for x in str(message.getParam(name)).split(',')]
    return out
        

def set_changed_recids_mock(message):
    data = get_msg_dict(message)
    _CACHE['recids'] = data
    for name in ('mod_date', 'last_recid'):
        if message.getParam(name):
            _CACHE[name] =  str(message.getParam(name))
        elif name in _CACHE:
            del _CACHE[name]
    sys.stderr.write(str(_CACHE) + "\n")
        

def get_changed_recids_mock(message):
    out = HashMap() #.of_(String, JArray_int)
    for k,v in _CACHE['recids'].items():
        out.put(k, JArray_int(v))
    message.setResults(out)
    for name in ('mod_date', 'last_recid'):
        val = _CACHE[name]
        try:
            val = int(val)
        except:
            pass
        if name in _CACHE:
            message.setParam(name, val)
    
    time.sleep(0.5)


def montysolr_targets():
    targets = make_targets('MockInvenioKeepRecidUpdated:get_changed_recids_mock', get_changed_recids_mock, 
                           '*:set_changed_recids_mock', set_changed_recids_mock,
           )
    return targets
    
