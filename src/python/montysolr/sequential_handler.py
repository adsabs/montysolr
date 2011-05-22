'''
Created on Feb 4, 2011

@author: rca
'''

from montysolr.config import MSTARGETUS
from montysolr import handler


class Handler(handler.Handler):
    '''Simple handler that just calls the methods
    '''

    def init(self):
        self.discover_targets(MSTARGETUS)



Handler = Handler()
