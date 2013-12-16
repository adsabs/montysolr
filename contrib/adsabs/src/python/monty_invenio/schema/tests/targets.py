

from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets


"""The following module holds targets that do not
depend on invenio, their value is mostly for
testing"""

import os



def get_field_value(message):
    """This simply assumes the field we got passed in is a 
    link to the filesystem"""
    
    value = message.getParam('externalVal')
    if not value:
        return
    value = str(value)
    if os.path.exists(value):
        fo = open(value, 'r')
        val = fo.read()
        fo.close()
        message.setResults(val)




def montysolr_targets():
    return make_targets(
                        "PythonTextField:get_field_value", get_field_value,
                        )
