
import sys, os
import cPickle
from invenio import bibrank_citation_indexer as bci
from invenio import intbitset
import time

'''Utility to import pickled cached citation dictionary into a database.
WARNING! This will replace your database entries!!! '''

if len(sys.argv) > 1:
    basedir = sys.argv[1]
else:
    basedir = '/opt/rchyla/citdicts'
    
if not os.path.exists(basedir) and not os.path.isdir(basedir):
    raise Exception('%s is not a folder' % basedir)

cit_names = ['citationdict',
             'reversedict', 'selfcitdict', 'selfcitedbydict']

def insert_into_cit_db(dic, name):
    """an aux thing to avoid repeating code"""
    ndate = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    s = bci.serialize_via_marshal(dic)
    #check that this column really exists
    testres = bci.run_sql("select object_name from rnkCITATIONDATA where object_name = %s",
                   (name,))
    if testres:
        bci.run_sql("UPDATE rnkCITATIONDATA SET object_value = %s where object_name = %s",
                (s, name))
    else:
        #there was no entry for name, let's force..
        bci.run_sql("INSERT INTO rnkCITATIONDATA(object_name,object_value) values (%s,%s)",
                 (name,s))
    bci.run_sql("UPDATE rnkCITATIONDATA SET last_updated = %s where object_name = %s",
           (ndate,name))


for dname in cit_names:
    # load the dictionary
    f = os.path.join(basedir, dname)
    if os.path.exists(f):
        print 'loading %s...' % dname
        fi = open(f, 'rb')
        cd = cPickle.load(fi)
        if isinstance(cd, basestring):
            cd = intbitset.intbitset().fastload(cd)
        fi.close()
        print 'loaded %s made of %s entries' % (dname, len(cd))
        print 'saving into db...'
        insert_into_cit_db(cd, dname)
        print 'saved!'
    
