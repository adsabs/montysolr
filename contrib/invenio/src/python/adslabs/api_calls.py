

import os
import thread
import sys


import ptree


def dispatch(func_name, *args, **kwargs):
    """Dispatches the call to the *local* worker
    It returns a tuple (ThreadID, result)
    """
    tid = thread.get_ident()
    out = globals()[func_name](*args, **kwargs)
    return [tid, out]



def load_fulltext(bibcode, field_name):
    ptree_path = ptree.id2ptree(bibcode)
    # TODO: make this path a config setting again
    full_path = '/proj/ads/fulltext/extracted%s%s.txt' % (ptree_path, field_name)
    if os.path.exists(full_path):
        fo = open(full_path, 'r')
        text = fo.read()
        fo.close()
        return text.decode('utf-8')
    else:
        return u""
      

if __name__ == '__main__':
    pass
