
from montysolr.tests.montysolr_testcase import LuceneTestCase, MontySolrTestCase
from invenio import dbquery

'''
Abstract classes that are used for running regression tests,
it will check the presence of the demo site (in a very
rudimentary way) and then throw Exception if we have some
suspicion to the contrary

Regression tests should subclass from classes here
'''

class InvenioDemoTestCaseLucene(LuceneTestCase):
    pass

class InvenioDemoTestCaseSolr(MontySolrTestCase):
    pass
        

def check_demo_site_exists():
    n = dbquery.run_sql("SELECT name FROM collection WHERE id=1")[0][0]
    if n != 'Atlantis Institute of Fictive Science':
        raise Exception("Your Invenio installation does not seem to have demo records loaded\n" +
                        "Please install demo first:\n" + 
                        "/your/invenio/bin/inveniocfg --drop-demo-site --create-demo-site --load-demo-records --yes-i-know ")
        


check_demo_site_exists()