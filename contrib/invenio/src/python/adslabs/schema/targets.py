

from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets

from invenio import bibdocfile

import adslabs.multiprocess_api_calls as multi_api_calls
import adslabs.api_calls as api_calls

import os
import time

SchemaField = j.SchemaField #@UndefinedVariable

def get_bibrec_bibdoc(message):
    """Simply retrieves the text of all the documents attached
    to the given recid"""
    value = message.getParam('externalVal')
    if not value:
        return
    value = str(value)
    
    out = []
    bibrec = bibdocfile.BibRecDocs(value)
    bibdocs = bibrec.list_bibdocs()
    for doc in bibdocs:
        out.append(doc.get_text())
    return ' '.join(out)
    

def get_field_value(message):
    
    value = message.getParam('externalVal')
    if not value:
        return
    bibcode = str(value)
    message.threadInfo('got bibcode ' + bibcode)
    
    field = SchemaField.cast_(message.getParam('field'))
    field_name = field.getName()
    message.threadInfo('getting field value for field: %s' % field_name)
    
    ret = None
    if bibcode:
        start = time.time()
        (wid, results) = api_calls.dispatch('load_fulltext', bibcode, field_name)
        t = time.time() - start
        message.threadInfo("Fulltext load took: %s s. len=%s and was executed by: %s" % (t, len(results), wid))
        message.setResults(results)
    else:
        message.threadInfo("hey, i didn't get a bibcode!")
        message.setResults("")
    return
    
#def get_field_value(message):
#    """This simply assumes the field we got passed in is a 
#    link to the filesystem"""
#    
#    value = message.getParam('externalVal')
#    if not value:
#        return
#    value = str(value)
#    if os.path.exists(value):
#        fo = open(value, 'r')
#        val = fo.read()
#        fo.close()
#        message.setResults(val)


def arxiv_field_value(message):
    """
    This is a hack - but it tries hard to find the fulltext
    amongs the AFS folders, It receives the path encoded
    in a string
        eg. 
        arxiv_id:XXXX.XXXX|src_dir:dir1,dir2
    """
    
    sender = str(message.getSender())
    if sender in 'PythonTextField':
        value = message.getParam('externalVal')
        if not value:
            return
        value = str(value)
        #print 'searching for', value
        vals = {}
        #ret = value.lower() + ' Hey! '
        ret = None
        if value:
            parts = value.split('|')
            for p in parts:
                k, v = p.split(':', 1)
                if v[0] == '[' and v[-1] == ']':
                    v = v[1:-1]
                vals[k] = v
            if 'arxiv_id' in vals and 'src_dir' in vals:
                #print vals
                dirs = vals['src_dir'].split(',')
                ax = vals['arxiv_id'].split(',')[0].strip()
                if ax.find('/') > -1:
                    arx_parts = ax.split('/') #math-gt/060889
                    fname = ''.join(arx_parts)
                    topdir = arx_parts[1][:4]
                elif ax.find('.') > -1:
                    arx_parts = ax.replace('arXiv:', '').split('.', 1) #arXiv:0712.0712
                    topdir = arx_parts[0]
                    fname = '.'.join(arx_parts)
                else:
                    return ret

                if len(arx_parts) == 2:


                    for d in dirs:
                        #print (d, topdir, fname + '.txt')
                        newname = os.path.join(d, topdir, fname + '.txt')
                        if os.path.exists(newname):
                            fo = open('/tmp/solr-index.txt', 'a')
                            fo.write(newname + '\n')
                            fo.close()
                            ret = open(newname, 'r').read()
                            if ret:
                                message.setResults(ret.decode('utf8'))
                        else:
                            fo = open('/tmp/solr-not-found.txt', 'a')
                            fo.write('%s\t%s\n' % (newname, value))
                            fo.close()
                        break


def montysolr_targets():
    return make_targets("PythonTextField:get_field_value", get_field_value,
                        "PythonTextField:arxiv_field_value", arxiv_field_value,
                        "PythonTextField:get_bibrec_bibdoc", get_bibrec_bibdoc,
                        )
