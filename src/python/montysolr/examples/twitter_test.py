'''
Created on Feb 4, 2011

@author: rca
'''

from montysolr.initvm import montysolr_java as sj
from montysolr.utils import MontySolrTarget

import twitter

def twitter_api(message):
    req = sj.SolrQueryRequest.cast_(message.getParam('request'))
    rsp = sj.SolrQueryResponse.cast_(message.getParam('response'))

    params = req.getParams()
    core = sj.SolrCore.cast_(req.getCore())
    schema = sj.IndexSchema.cast_(req.getSchema())
    updateHandler = sj.UpdateHandler.cast_(core.getUpdateHandler())

    addCmd = sj.AddUpdateCommand()
    addCmd.allowDups = False
    addCmd.overwriteCommitted = False
    addCmd.overwritePending = False


    action = params.get("action")

    if action == 'search':
        term = params.get("term")

        if not term:
            rsp.add("python-message", 'Missing search term!')
            return

        api = twitter.Api()
        docs = api.GetSearch(term)
        i = 0
        for d in docs:
            d = d.AsDict()
            doc = sj.SolrInputDocument();
            doc.addField(schema.getUniqueKeyField().getName(), d['id'])
            doc.addField("title", d['text'])
            doc.addField("description", d['source'])
            doc.addField("author", d['user']['screen_name'])
            i += 1
            addCmd.doc = sj.DocumentBuilder.toDocument(doc, schema)
            updateHandler.addDoc(addCmd)

        updateCmd = sj.CommitUpdateCommand(True) # coz for demo we want to see it
        updateHandler.commit(updateCmd)
        message.setResults(i)
        rsp.add('python-message', 'Found and indexed %s docs for term %s from Twitter' % (len(docs), term))

    else:
        rsp.add("python-message", 'Unknown action: %s' % action)



def montysolr_targets():
    targets = [
           MontySolrTarget('TwitterAPIHandler:twitter_api', twitter_api),
           ]
    return targets
