import os
import cPickle
from invenio import bibrank_citation_searcher as bcs
from invenio import intbitset

'''Utility to dump cached citation dictionary into a filesystem'''

basedir = '/opt/rchyla/citdicts'

cit_names = ['citationdict',
             'reversedict', 'selfcitdict', 'selfcitedbydict']
             
for dname in cit_names:
    print 'loading: %s' % dname
    cd = bcs.get_citation_dict(dname)  # load the dictionary
    f = os.path.join(basedir, dname)  # dump it out
    fo = open(f, 'wb')
    print 'dumping of %s entries started' % len(cd)
    if isinstance(cd, intbitset):
        cPickle.dump(cd.fastdump(), fo)
    else:
        cPickle.dump(cd, fo)
    fo.close()
    print 'dumped %s into %s' % (dname, f)
    
    

