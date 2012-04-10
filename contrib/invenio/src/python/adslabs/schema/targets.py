

from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets

from invenio import bibdocfile

import adslabs.api_calls as api_calls
import os
import time

SchemaField = j.SchemaField #@UndefinedVariable

    

def get_field_value_using_bibcode(message):
    
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
    


def montysolr_targets():
    return make_targets("PythonTextField:get_field_value_using_bibcode", get_field_value_using_bibcode,
                        )
