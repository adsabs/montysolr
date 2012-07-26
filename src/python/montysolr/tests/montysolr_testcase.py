'''
Created on Feb 4, 2011

@author: rca
'''

import os
import sys
import tempfile
import sys

from montysolr.python_bridge import JVMBridge

# "-Djava.util.logging.config.file=/x/dev/workspace/sandbox/montysolr/example/etc/test.logging.properties"
os.environ['MONTYSOLR_JVMARGS_PYTHON'] = ""

import unittest
from montysolr import initvm, java_bridge, handler

from java.lang import System
from org.apache.solr.core import SolrCore, CoreContainer

class MontySolrTestCase(unittest.TestCase):
    """use this to test Solr related code"""

    def __init__(self, *args, **kwargs):
        unittest.TestCase.__init__(self, *args, **kwargs)
        self.base_dir = os.path.abspath(os.path.dirname(__file__) + '../../../../..')
        self.solr_home = os.path.join(self.base_dir, 'src/test-files/solr')
        self.data_dir = None #os.path.join(self.solr_home, 'data')
        self.handler = None
        self.bridge = None

    def setUp(self):
        self.old_home = self.old_data_dir = None
        self.old_home = System.getProperty('solr.solr.home', '')
        self.old_data_dir = System.getProperty('solr.data.dir', '')
        
        self.data_dir = self.makeDataDir()
        
        System.setProperty('solr.solr.home', self.getSolrHome())
        System.setProperty('solr.data.dir', self.data_dir)
        
        self.bridge = JVMBridge
        
        #if self.getHandler():
        #    self.bridge = java_bridge.SimpleBridge(self.getHandler())

        self.initializer = CoreContainer.Initializer()
        self.core_container = self.initializer.initialize()
        #self.server = sj.EmbeddedSolrServer(self.core_container, "")

        #self.core = SolrCore.getSolrCore()
        self.core = self.core_container.getSolrCore("collection1")

    def tearDown(self):
        #self.core_container.shutdown()
        if self.data_dir:
            os.removedirs(self.data_dir)
            self.data_dir = None
            
        if self.old_home != None:
            System.setProperty('solr.solr.home', self.old_home)
        if self.old_data_dir != None:
            System.setProperty('solr.data.dir', self.old_data_dir)


    def getBaseDir(self):
        return self.base_dir

    def setBaseDir(self, basedir):
        self.base_dir = basedir

    def setSolrHome(self, solrhome):
        self.solr_home = solrhome

    def getSolrHome(self):
        return self.solr_home
    
    def makeDataDir(self):
        return tempfile.mkdtemp(prefix=self.__class__.__module__ + '.' + self.__class__.__name__) 
        
    def getDataDir(self):
        return self.data_dir

    def setDataDir(self, datadir):
        self.data_dir = datadir

    def setHandler(self, handler):
        self.bridge.setHandler(handler)
        
    def getBridge(self):
        return self.bridge
    
    def setBridge(self, bridge):
        self.bridge = bridge

    def getHandler(self):
        return self.bridge.getHandler()

    def loadHandler(self, module_path):
        if not isinstance(module_path, list):
            module_path = [module_path]
        class TestHandler(handler.Handler):
            def init(self):
                self.discover_targets(module_path)
        h = TestHandler()
        h.init()
        return h
    
    def addSysPath(self, paths):
        if not isinstance(paths, list):
            paths = [paths]
        for p in paths:
            if p not in sys.path:
                sys.path.insert(0, p)
                
    def setTargets(self, targets):
        self.bridge.setHandler(self.loadHandler(targets))
        
    def addTargets(self, targets):
        if not isinstance(targets, list):
            targets = [targets]
        self.bridge.getHandler().discover_targets(targets)
    
    def hasTarget(self, name):
        return self.bridge.getHandler().has_target(name)
        

class LuceneTestCase(MontySolrTestCase):
    """Use this to test a code that is needing only lucene
    - usually what I write in python in order to test the
    python part (what is called from solr)
    """
    def setUp(self):
        self.bridge = JVMBridge()
    def tearDown(self):
        pass

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
