'''
Created on Feb 4, 2011

@author: rca
'''

from montysolr import handler


class Handler(handler.Handler):
    '''Simple handler that just calls the methods
    '''

    def init(self):
        self.discover_targets(['montysolr.inveniopie.targets', 'montysolr.examples.twitter_test'])



Handler = Handler()
