

import os
import thread
import sys

from nameparser import HumanName



def dispatch(func_name, *args, **kwargs):
    """Dispatches the call to the *local* worker
    It returns a tuple (ThreadID, result)
    """
    tid = thread.get_ident()
    out = globals()[func_name](*args, **kwargs)
    return [tid, out]



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
