'''
Created on Feb 4, 2011

@author: rca
'''

from montysolr import handler, config
import sys

class Handler(handler.Handler):
    '''Simple handler that just calls the methods sequentially
    and discovers the targets from the config file definitions
    '''

    def init(self):
        self.discover_targets(config.MONTYSOLR_TARGETS)



Handler = Handler()
