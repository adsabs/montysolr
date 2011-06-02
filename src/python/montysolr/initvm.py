'''
Created on Jan 13, 2011

@author: rca
'''
import os
import sys
import lucene

try:
    import solr_java
    import montysolr_java
except:
    _d = os.path.abspath(os.path.dirname(__file__) + '/../../build/dist')
    if _d not in sys.path and os.path.exists(_d):
        sys.stderr.write('Warning: we add the default folder to sys.path:\n')
        sys.stderr.write(_d + '\n')
        sys.path.append(_d)
    import solr_java
    import montysolr_java


#if os.getenv('MONTYSOLR_DEBUG'):
#    from invenio import remote_debugger
#    remote_debugger.start('3') #or override '3|ip:192.168.31.1|port:9999'


_jvmargs = ''
if os.getenv('MONTYSOLR_JVMARGS_PYTHON'):
    _jvmargs = os.getenv('MONTYSOLR_JVMARGS_PYTHON')

# the distribution may contain a file that lists the jars that weere used
# for comilation, get the and add them to the classpath
_cp = os.path.join(os.path.dirname(montysolr_java.__file__), 'classpath')
_classpath=''
if os.path.exists(_cp):
    _classpath = open(_cp, 'r').read()

# order of calls important - first we initialize MontySolr
# then the rest

if _jvmargs:
    montysolr_java.initVM(lucene.CLASSPATH+os.pathsep+montysolr_java.CLASSPATH+os.pathsep+_classpath, vmargs=_jvmargs)
else:
    montysolr_java.initVM(lucene.CLASSPATH+os.pathsep+montysolr_java.CLASSPATH+os.pathsep+_classpath)
lucene.initVM()
solr_java.initVM()
