'''
Created on Feb 4, 2011

@author: rca
'''

from montysolr.config import MSTARGETUS
from montysolr import handler


class Handler(handler.Handler):
    '''Simple handler that just calls the methods sequentially
    and discovers the targets from the config file definitions
    '''

    def init(self):
        self.discover_targets(MSTARGETUS)



Handler = Handler()
