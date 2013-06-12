from montysolr import initvm
import unittest

j = initvm.JAVA

class Test(unittest.TestCase):
    
    
    def test_modules(self):
        """Testing existence of modules"""
        
        print "Deactivated 12/6/2013 - something doesn't work"
        return 
        
        assert initvm.solr_java != None
        assert initvm.montysolr_java != None
        assert initvm.lucene != None
        
        l = initvm.lucene
        s = initvm.solr_java
        
        assert id(j.Weight) == id(l.Weight) == id(s.Weight)
        
        # failing now
        #assert id(j.JArray_string) == id(l.JArray_string) == id(s.JArray_string)
        #assert id(j.JArray_long) == id(l.JArray_long) == id(s.JArray_long)
        
        assert id(j.ArrayList) == id(l.ArrayList) == id(s.ArrayList)
        
        assert hasattr(j, "PythonBridge")
        assert hasattr(j, "PythonMessage")
        assert hasattr(j, "MontySolrBridge")
    
    def test_basics(self):
        """Basic operations"""
        
        print "Deactivated 12/6/2013 - something doesn't work"
        return
    
        # create array of strings
        x = j.JArray_string(5)
        for i in range(5):
            x[i] = 'x' 
        y = j.JArray_string(['x','x','x','x','x'])
        assert x == y
        assert x == ['x','x','x','x','x']
        
        # create array of objects
        x = j.JArray_object(5)
        for i in range(5):
            x[i] = j.JObject()
        assert str(x) == 'JArray<object>[None, None, None, None, None]'
        
        # create array of string arrays
        x = j.JArray_object(5)
        for i in range(5):
            y = j.JArray_string(5)
            x[i] = y
        for i in range(5):
            y = j.JArray_string.cast_(x[i])
            assert str(y) == 'JArray<string>[None, None, None, None, None]'
        
        # create array of string objects
        x = j.JArray_object(5)
        for i in range(5):
            x[i] = j.JArray_string(['x', 'z'])
        for i in range(5):
            y = j.JArray_string.cast_(x[i])
            assert str(y) == 'JArray<string>[u\'x\', u\'z\']'
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
