'''
Created on Feb 4, 2011

@author: rca
'''

class MontySolrTarget(object):
    def __init__(self, message_id, callable):
        self._message_id = message_id
        self._target = callable
    def getTarget(self):
        return self._target
    def getMessageId(self):
        return self._message_id