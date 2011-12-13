
from montysolr.tests.montysolr_testcase import LuceneTestCase, MontySolrTestCase
from invenio import dbquery

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