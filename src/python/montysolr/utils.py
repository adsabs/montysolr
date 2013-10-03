'''
Created on Feb 4, 2011

@author: rca
'''

import multiprocessing
import os

class MontySolrTarget(object):
    """Simple class that represents the target that is registered
    by handlers
    
    It has a str id, which is made of <sender>:<recipient>
        eg. SolrRequestHandler:answer_query
        
        sender = the Java side class (usually)
        recipient = the Python side function name (always)
        
    Where sender may be empty, which means it can be any sender
        eg *:answer_query
        
    """
    def __init__(self, recipient, callable, sender='*'):
        self._recipient = recipient
        self._sender = sender
        self._target = callable
    def getTarget(self):
        return self._target
    def getMessageId(self):
        return '%s:%s' % (self._sender, self._recipient)
    

def make_targets(*args, **kwargs):
    """Creates a list of targets, the keys are the 
    recipient names and the value the callable that
    implements them"""
    targets = []
    assert len(args) % 2 == 0
    i = 0
    while i < len(args):
        name = args[i]
        sender = None
        if ':' in name:
            sender, name = name.split(':')
        if sender:
            targets.append(MontySolrTarget(name, args[i+1], sender=sender))
        else:
            targets.append(MontySolrTarget(name, args[i+1]))
        i += 2
        
    for k,v in kwargs.items():
        targets.append(MontySolrTarget(k, v))
    return targets


def multiprocessing_aware(original_function):
    """Decorator to make the api dispatch function 
    multiprocessing aware
    """
    from montysolr import config
    num_cpus = 0
    global api_calls
    # start multiprocessing with that many processes in the pool
    if str(config.MONTYSOLR_MAX_WORKERS) == '-1':
        try:
            num_proc = multiprocessing.cpu_count()
        except:
            num_proc = 1
    elif int(config.MONTYSOLR_MAX_WORKERS) > 1:
        num_cpus = int(config.MONTYSOLR_MAX_WORKERS)
    
    if num_cpus > 1:
        POOL = multiprocessing.Pool(processes=num_cpus)
        def new_function(func_name, *args, **kwargs):
            """Dispatches the call to a remote worker
            @return: (worker_id, result)
            """
            g = original_function.__globals__
            func_name_pre = '%s_local_pre' % func_name
            func_name_post = '%s_local_post' % func_name
            print 'MULTIPROCESSING'
            if func_name_pre in g:
                args = list(args)
                g[func_name_pre](args, kwargs)
                
            kwargs['remote_call'] = True
            handle = POOL.apply_async(original_function, args=(func_name, args, kwargs))
            (worker_pid, result) = handle.get()
            del kwargs['remote_call']
        
            if func_name_post in g:
                result = g[func_name_post](result)
        
            return(worker_pid, result)
        return new_function
    else:
        return original_function