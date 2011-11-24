'''
Created on Feb 4, 2011

@author: rca
'''

class MontySolrTarget(object):
    """Simple class that represents the target that is registered
    by handlers
    
    It has a str id, which is made of <sender>:<recipiend>
        eg. SolrRequestHandler:answer_query
        
    Where recipient may be empty, which means it can be any sender
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
    

def make_targets(**kwargs):
    """Creates a list of targets, the keys are the 
    recipient names and the value the callable that
    implements them"""
    targets = []
    for k,v in kwargs.items():
        targets.append(MontySolrTarget(k, v))
    return targets