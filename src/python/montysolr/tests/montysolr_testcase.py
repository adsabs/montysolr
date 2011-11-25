'''
Created on Feb 4, 2011

@author: rca
'''

import os
import sys
import tempfile

# "-Djava.util.logging.config.file=/x/dev/workspace/sandbox/montysolr/example/etc/test.logging.properties"
os.environ['MONTYSOLR_JVMARGS_PYTHON'] = ""

import unittest
from montysolr import initvm, java_bridge, handler

sj = initvm.montysolr_java


class MontySolrTestCase(unittest.TestCase):

    def __init__(self, *args, **kwargs):
        unittest.TestCase.__init__(self, *args, **kwargs)
        self.base_dir = os.path.abspath(os.path.dirname(__file__) + '../../../../..')
        self.solr_home = os.path.join(self.base_dir, 'src/test-files/solr')
        self.data_dir = None #os.path.join(self.solr_home, 'data')
        self.handler = None
        self.bridge = None

    def setUp(self):
        self.old_home = self.old_data_dir = None
        self.old_home = sj.System.getProperty('solr.solr.home', '')
        self.old_data_dir = sj.System.getProperty('solr.data.dir', '')
        
        self.data_dir = self.makeDataDir()
        
        sj.System.setProperty('solr.solr.home', self.getSolrHome())
        sj.System.setProperty('solr.data.dir', self.data_dir)

        if self.getHandler():
            self.bridge = java_bridge.SimpleBridge(self.getHandler())

        #self.initializer = sj.CoreContainer.Initializer()
        #self.core_container = self.initializer.initialize()
        #self.server = sj.EmbeddedSolrServer(self.core_container, "")

        self.core = sj.SolrCore.getSolrCore()

    def tearDown(self):
        #self.core_container.shutdown()
        if self.data_dir:
            os.removedirs(self.data_dir)
            self.data_dir = None
            
        if self.old_home != None:
            sj.System.setProperty('solr.solr.home', self.old_home)
        if self.old_data_dir != None:
            sj.System.setProperty('solr.data.dir', self.old_data_dir)


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
        self.handler = handler

    def getHandler(self):
        return self.handler

    def loadHandler(self, module_path):
        if not isinstance(module_path, list):
            module_path = [module_path]
        class TestHandler(handler.Handler):
            def init(self):
                self.discover_targets(module_path)
        return TestHandler()


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
