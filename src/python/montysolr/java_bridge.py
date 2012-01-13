
from montysolr import config
from montysolr.initvm import montysolr_java as sj

import time

'''
This class is always instantiated by the Java VM when a message 
needs to be passed from Java to Python.

At a startup, MontySolr inspects the system property called 
"montysolr.bridge" and loads the Python module.

Example:
  java -Dmontysolr.bridge=montysolr.java_bridge.SimpleBridge -jar...
  
Will load module montysolr.java_bridge and instantiate class
SimpleBridge upon MontySolr start.
  
Created on Jan 13, 2011

@author: rca
'''


class SimpleBridge(sj.MontySolrBridge):

    def __init__(self, handler=None):
        if not handler:
            handler_module = __import__(config.MONTYSOLR_HANDLER, globals(), locals(), fromlist=['Handler'])
            handler = handler_module.Handler
        super(SimpleBridge, self).__init__()
        self._handler = handler
        self._handler_module = handler.__module__

    def receive_message(self, message):
        if config.MONTYSOLR_KILLLOAD:
            message.threadInfo('Reloading ourselves mylord!', self._handler_module)
            self._handler_module = reload(self._handler_module)
            self._handler = self._handler_module.Handler
        if config.MONTYSOLR_BUGDEBUG:
            start = time.time()
            self._handler.handle_message(message)
            message.threadInfo('Total time: %s' % (time.time() - start,))
        else:
            self._handler.handle_message(message)
    
    
    def eval_command(self, message):
        exec(message, globals(), locals())


    def set_handler(self, handler):
        self._handler = handler
