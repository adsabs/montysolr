
from montysolr.config import MSBUGDEBUG, MSKILLLOAD, MSIAMHANDLER
from montysolr.initvm import montysolr_java as sj

import time

'''
This class is always instantiated by the Java VM when a message 
needs to be passed from Java to Python.

At a startup, MontySolr inspects the system property called 
"python.bridge" and loads the Python module.

Example:
  java -Dpython.bridge=montysolr.java_bridge.SimpleBridge -jar...
  
Will load module montysolr.java_bridge and instantiate class
SimpleBridge upon MontySolr start.
  
Created on Jan 13, 2011

@author: rca
'''


class SimpleBridge(sj.MontySolrBridge):

    def __init__(self, handler=None):
        if not handler:
            handler_module = __import__(MSIAMHANDLER, globals(), locals(), fromlist=['Handler'])
            handler = handler_module.Handler
        super(SimpleBridge, self).__init__()
        self._handler = handler
        self._handler_module = handler.__module__

    def receive_message(self, message):
        if MSKILLLOAD:
            req = message.getSolrQueryRequest()
            if req:
                params = req.getParams()
                if params.get("reload"):
                    message.threadInfo('Reloading ourselves mylord!', self._handler_module)
                    self._handler_module = reload(self._handler_module)
                    self._handler = self._handler_module.Handler
        if MSBUGDEBUG:
            start = time.time()
            self._handler.handle_message(message)
            message.threadInfo('Total time: %s' (time.time() - start))
        else:
            self._handler.handle_message(message)



    def set_handler(self, handler):
        self._handler = handler
