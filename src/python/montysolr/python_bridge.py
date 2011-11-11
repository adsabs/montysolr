
'''
Created on Feb 7, 2011

@author: rca

This class is useful for testing and when we want to call Java
*from inside* Python. Ie when Python is the main process and 
javaVM resides inside Python interpreter (usually it is reversed).

It is a singleton that contains a reference 
to the handlers - we don't need to instantiate it everytime
as is the case when calling from Java. Therefore we can 
put the VM and the bridge parts together.

It intentionally has the java-style method names
'''

from montysolr import initvm
import sys

class JVMBridge(object):
    
    def __new__(cls, *args):
        if hasattr(initvm.montysolr_java, '_JVMBridge_SINGLETON'):
            return getattr(initvm.montysolr_java, '_JVMBridge_SINGLETON')
        else:
            instance = super(JVMBridge, cls).__new__(cls)
            setattr(initvm.montysolr_java, '_JVMBridge_SINGLETON', instance)
            instance._store = {}
            return instance
        
    def __del__(self):
        #if 'solr.container' in self._store:
        #    self._store['solr.container'].shutdown()
        sys.stderr.write('!!!!!!! - Bridge deleted')
    
    def __init__(self, handler=None):
        
        if not handler: #FIXME: we must make the handler configurable outside the code
            import montysolr.sequential_handler as handler
            handler = handler.Handler
        self._handler = handler
        self._lucene = initvm.lucene
        self._solr = initvm.solr_java
        self._sj = initvm.montysolr_java
        
        
    def sendMessage(self, message):
        self._sj.getVMEnv().attachCurrentThread()
        self._handler.handle_message(message)
        
    def setHandler(self, handler):
        self._handler = handler
    
    def createMessage(self, receiver):
        self._sj.getVMEnv().attachCurrentThread()
        return self._sj.PythonMessage(receiver)
    
    def getObjMontySolr(self):
        return self._sj
    
    def getObjLucene(self):
        return self._lucene
    
    def getObjSolr(self):
        return self._solr
    
    def setObj(self, name, value):
        self._store[name] = value
    
    def getObj(self, name):
        return self._store[name]
    
    def hasObj(self, name):
        return name in self._store
    
JVMBridge = JVMBridge()
    