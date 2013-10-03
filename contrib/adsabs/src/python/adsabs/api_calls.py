

import os
import thread
import sys

from montysolr.utils import multiprocessing_aware
from nameparser import HumanName


@multiprocessing_aware
def dispatch(func_name, *args, **kwargs):
    """Dispatches the call to the *local* worker
    It returns a tuple (ThreadID, result)
    """
    
    if 'remote_call' in kwargs and kwargs['remote_call'] == True:
        g = globals()
        func_name_pre = '%s_remote_pre' % func_name
        func_name_post = '%s_remote_post' % func_name
    
        if func_name_pre in g:
            args = list(args)
            g[func_name_pre](args, kwargs)
    
        result = globals()[func_name](*args, **kwargs)
    
        if func_name_post in g:
            result = g[func_name_post](result)
    
        return (os.getpid(), result)
    else:
        tid = thread.get_ident()
        out = globals()[func_name](*args, **kwargs)
        return (tid, out)



def parse_human_name(input):
    """Parses human names using python nameparse library
    """
    name = HumanName(input)
    out = {}
    for k in ('first', 'middle', 'last', 'suffix', 'title'):
        if getattr(name, k):
            out[k[0].upper() + k[1:]] = getattr(name, k)
    return out 
      

if __name__ == '__main__':
    pass
