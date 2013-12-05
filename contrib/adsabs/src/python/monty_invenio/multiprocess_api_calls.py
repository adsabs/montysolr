'''
Created on Feb 13, 2011

@author: rca
'''

from monty_invenio import api_calls
from invenio.intbitset import intbitset
import os
import multiprocessing


POOL = None

# ======================================================
#  Multiprocess versions  of the api_call methods
# 
#  It works by wrapping the standard call into a
#  a series of normalizers (basically, what you want
#  to do in these serializers is to de/serialize data
#
#  Sequence for func_X is:
#  
#  0. func_X_local_pre()
#    1. func_X_remote_pre()
#      2. func_X()
#    3. func_X_remote_post()
#  4. func_X_local_post()
#
#  Steps 1-3 are executed on the remote machine.
#  
# ======================================================

def citation_summary_local_pre(args, kwargs):
    args[0] = args[0].fastdump()

def citation_summary_remote_pre(args, kwargs):
    args[0] = intbitset().fastload(args[0])

def search_remote_post_X(result):
    if result:
        res = result[1]
        if len(res) > 0:
            result[1] = intbitset(res).fastdump()
    return result

def search_local_post_X(result):
    if result:
        res = result[1]
        if isinstance(res, basestring):
            result[1] = intbitset().fastload(res).tolist()
    return result

def sort_and_format_local_pre(args, kwargs):
    args[0] = intbitset(args[0]).fastdump()

def sort_and_format_remote_pre(args, kwargs):
    args[0] = intbitset().fastload(args[0])



# ======================================================
#    Start of the multi-processing
# ======================================================

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

# ======================================================
#    Some code to execute on lazy-initialization
# ======================================================

from invenio import bibrank_citation_searcher as bcs, \
    search_engine_summarizer as ses

# initialize citation dictionaries in parent (so that forks have them shared)
bcs.get_citation_dict("citationdict")
bcs.get_citation_dict("reversedict")


# ======================================================
#    Dispatching code
# ======================================================

def dispatch(func_name, *args, **kwargs):
    """Dispatches the call to a remote worker
    @return: (worker_id, result)
    """
    g = globals()
    func_name_pre = '%s_local_pre' % func_name
    func_name_post = '%s_local_post' % func_name

    if func_name_pre in g:
        args = list(args)
        g[func_name_pre](args, kwargs)

    handle = POOL.apply_async(_dispatch_remote, args=(func_name, args, kwargs))
    (worker_pid, result) = handle.get()

    if func_name_post in g:
        result = g[func_name_post](result)

    return(worker_pid, result)



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

    (thread_id, result) = api_calls.dispatch(func_name, *args, **kwargs)

    if func_name_post in g:
        result = g[func_name_post](result)

    return (os.getpid(), result)


def _dispatch(func_name, *args, **kwargs):
    return api_calls.dispatch(func_name, *args, **kwargs)








