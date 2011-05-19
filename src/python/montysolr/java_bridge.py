
from montysolr.initvm import montysolr_java as sj



'''
Created on Jan 13, 2011

@author: rca
'''

DEBUG = False

class SimpleBridge(sj.MontySolrBridge):
    
    def __init__(self, handler=None):
        if not handler:
            import montysolr.sequential_handler as handler_module
            handler = handler_module.Handler
        super(SimpleBridge, self).__init__()
        self._handler = handler
        self._handler_module = handler.__module__
        
    def receive_message(self, message):
        if DEBUG:
            # HACK: to remove this whole block
            req = message.getSolrQueryRequest()
            if req:
                params = req.getParams()
                if params.get("reload"):
                    message.threadInfo('Reloading python!', self._handler_module)
                    self._handler_module = reload(self._handler_module)
                    self._handler = self._handler_module.Handler
        self._handler.handle_message(message)
        
    def set_handler(self, handler):
        self._handler = handler
