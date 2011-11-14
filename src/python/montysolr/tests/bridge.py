
from montysolr.initvm import montysolr_java as sj
from montysolr.tests import handler

import time

'''
This class is used for unittests
'''


class Bridge(sj.MontySolrBridge):

    def __init__(self):
        super(Bridge, self).__init__()
        self._handler = handler.Handler()

    def receive_message(self, message):
        self._handler.handle_message(message)
        
    def eval_command(self, message):
        exec(message, globals(), locals())



if __name__ == '__main__':
    b = Bridge()
    b.eval_command("import sys;sys.argv.insert(0, 'x');print sys.argv")
    b.eval_command("import sys;sys.argv.insert(0, 'x');print sys.argv")