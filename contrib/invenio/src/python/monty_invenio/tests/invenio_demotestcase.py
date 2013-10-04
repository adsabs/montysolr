
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
    c = dbquery.run_sql("SELECT name FROM collection WHERE name='Atlantis Times'")
    u = dbquery.run_sql("SELECT id FROM user WHERE email='romeo.montague@cds.cern.ch'")
    r = dbquery.run_sql("SELECT COUNT(id) FROM bibrec")
    if not len(c) or (not len(u) and int(u[0][0]) != 5) or (not len(r) or r[0][0] < 90): 
        raise Exception("Your Invenio installation does not seem to be the demo site\n" +
                        "Please check you have the demo (or install it):\n" + 
                        "/your/invenio/bin/inveniocfg --drop-demo-site --create-demo-site --load-demo-records --yes-i-know ")
        


check_demo_site_exists()