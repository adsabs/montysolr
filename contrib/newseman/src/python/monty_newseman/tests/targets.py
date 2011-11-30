# -*- coding: utf8 -*-

from montysolr.initvm import JAVA as j
from montysolr.utils import make_targets

from monty_newseman import targets
from monty_newseman.tests import unittest_translation

"""
All these are just utility methods that call the real semantic
tagger functions. It is here to simplify the life of unittester
"""


def fill_newseman_dictionary(message):
    """Loads the semantic tagger dictionary"""
    url = message.getParam("url")
    if url:
        seman = targets.Cacher.get_seman(str(url))
    else:
        seman = targets.Cacher.get_last()
    db = seman.surface().getSurfaceDictionary()
    unittest_translation.fill_dictionary(db)
    



def montysolr_targets():
    return make_targets(fill_newseman_dictionary=fill_newseman_dictionary,
                        )
