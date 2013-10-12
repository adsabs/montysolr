

import os
import thread
import sys
import time

from montysolr.utils import get_numcpus, multiprocess_aware
from nameparser import HumanName, constants
import multiprocessing


POOL = None
def start_multiprocessing(num_proc=None, default=4):
    global POOL
    if not num_proc:
        try:
            num_proc = multiprocessing.cpu_count()
        except:
            num_proc = default
        POOL = multiprocessing.Pool(processes=num_proc)
    else:
        POOL = multiprocessing.Pool(processes=num_proc)
    return num_proc


#@multiprocess_aware - as I discovered, it is much faster
# without multiprocessing
def dispatch(func_name, *args, **kwargs):
    """Dispatches the call to the *local* worker
    It returns a tuple (ThreadID, result)
    """
    
    if '__remote_call' in kwargs and kwargs['__remote_call'] == True:
        del kwargs['__remote_call']
        
        if POOL is None:
            start_multiprocessing(get_numcpus())
            
        g = globals()
        func_name_pre = '%s_remote_pre' % func_name
        func_name_post = '%s_remote_post' % func_name
        
        if func_name_pre in g:
            args = list(args)
            g[func_name_pre](args, kwargs)
    
        handle = POOL.apply_async(_dispatch_remote, args=(func_name, args, kwargs))
        (worker_pid, result) = handle.get()
        
        
        if func_name_post in g:
            result = g[func_name_post](result)
    
        return (os.getpid(), result)
    else:
        tid = thread.get_ident()
        out = globals()[func_name](*args, **kwargs)
        return (tid, out)





def _dispatch_remote(func_name, args, kwargs):
    """This receives the data on the remote side and calls
    the actual function that does the job and returns results.
    """

    g = globals()
    func_name_pre = '%s_remote_pre' % func_name
    func_name_post = '%s_remote_post' % func_name

    if func_name_pre in g:
        args = list(args)
        g[func_name_pre](args, kwargs)

    result = g[func_name](*args, **kwargs)
    
    if func_name_post in g:
        result = g[func_name_post](result)

    return [os.getpid(), result]




def fix_name(input):
    """
    Very small changes to the name before we send it to the parser
    i am trying to not change things; but it seemed excessive to 
    put these tidbits into another java filter before the
    Pythonic filter, so i put them here
    
     #362 - smartly handle o' sullivan (there is a space after ')
     
    """
    if "' " in input:
        while "' " in input:
            start = input.find("' ")
            end = start + 2
            while end+1 < len(input) and input[end+1] == ' ':
                end =+ 1 
            input = input.replace(input[start:end], "'")
    return input
    
def parse_human_name(input):
    """Parses human names using python nameparse library
    """
    
    #name = HumanName(fix_name(input))
    prefixes = constants.PREFIXES
    prefixes.add("'t")
    name = HumanName(fix_name(input), prefixes_c=prefixes)


    out = {}
    for k in ('first', 'middle', 'last', 'suffix', 'title'):
        if getattr(name, k):
            out[k[0].upper() + k[1:]] = getattr(name, k)
    return out 
      

if __name__ == '__main__':
    pass
