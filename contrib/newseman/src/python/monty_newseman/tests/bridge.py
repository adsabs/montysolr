
from montysolr.initvm import JAVA as j
import handler


'''
This class is used for unittests
'''


class Bridge(j.MontySolrBridge): #@UndefinedVariable

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