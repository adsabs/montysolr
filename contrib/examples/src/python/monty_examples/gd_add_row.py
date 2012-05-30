

from montysolr import config, optionparse
import sys 

import gdata.spreadsheet.service

log = config.get_logger('montysolr.gd_add_row')



def run(user=None, password=None, spreadsheet=None, keys=[], data=[], verbose=None, sep=None):
    '''
    Utility to add values into a Google SpreadSheet

    usage: %prog -u montysolr -p <password> -s SpreadSheetName -k date,docs -d 12/13/14,5000
    -p, --password = PASS: password to access the Google Data
    -u, --user = USER: user name
    -s, --spreadsheet = FILE: spread sheet name (you must have created the spreadsheet manually!)
    -k, --keys = KEYS: comma separated names from the header in the table (you must have created it manually!)
    -d, --data = DATA: comma separated list of values to insert, each row is then split using row-separator
    -e, --sep = SEP: character to use as a value separator for the data fields, default [|]
    -v, --verbose = VERBOSE: numeric value of the logging module [30]
    '''

    log.setLevel(int(verbose))
    
    vals = []
    for v in data:
        vv = v.split(sep)
        if len(vv) != len(keys):
            log.error("The data is not of the same size as header!")
            log.error("header=%s, data=%s" % (keys, v))
            raise(Exception("Wrong input"))
        vals.append(vv)
    
    client = get_client(user, password)
    if client is None:
        log.error("Error logging into the GoogleData")
        raise()
    
    datasheet = get_spreadsheet(client, spreadsheet)
    if datasheet is None:
        log.error("Error obtaining a spreadsheet with name: " + spreadsheet + ". You should create it manually first!")
        raise()
    
    update(client, datasheet, keys, vals)
    
    log.info("Success!")


def get_client(user, passwd):
    gd_client = gdata.spreadsheet.service.SpreadsheetsService()
    gd_client.email = user
    gd_client.password = passwd
    gd_client.ProgrammaticLogin()
    return gd_client


def get_spreadsheet(client, spreadsheet_name):
  
    docs= client.GetSpreadsheetsFeed()
    spreads = []
    for i in docs.entry: 
        spreads.append(i.title.text)
        
    spread_number = None
    for i,j in enumerate(spreads):
        if j == spreadsheet_name: 
            return docs.entry[i]
    if spread_number == None:
      return None

def update(client, spreadsheet, keys, values):
    key = spreadsheet.id.text.rsplit('/', 1)[1]
    feed = client.GetWorksheetsFeed(key)
    wksht_id = feed.entry[0].id.text.rsplit('/', 1)[1]
    feed = client.GetListFeed(key,wksht_id)
    
    for vals in values:
        kwargs = {}
        for i in range(len(keys)):
            kwargs[keys[i].lower()] = vals[i]
        entry = client.InsertRow(kwargs,key,wksht_id)
    return 1
      
    
def main():
    if len(sys.argv) == 1 or (len(sys.argv) > 1 and sys.argv[1] == 'demo'):
        args = '''
          --user montysolr
          --password <pass>
          --spreadsheet ADSIndexingTest
          --keys IndexingDate,TotalDocs,TotalSecs
          --data 16/5/2012x|500000|30,17/5/2012|500000|15,18/5/2012|500000|20
          '''
        sys.argv[1:] = args.split()

    options,args=optionparse.parse(run.__doc__)

    run(**options.__dict__)


if __name__ == '__main__':
    main()