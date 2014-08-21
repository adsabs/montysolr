import os
import requests
from urllib import urlencode
from flask import Flask, request
app = Flask(__name__,static_folder='src')


def solr(request):
  headers = request.headers
  payload = dict(request.form)
  payload['wt'] = 'json'
  headers = dict(headers.items())
  headers['Content-Type'] = 'application/x-www-form-urlencoded'

  # TODO: Unify configuration scheme for setting API and/or search backend
  r = requests.post('http://localhost:8983/solr/select', data=urlencode(payload, doseq=True), headers=headers)
  return r.text, r.status_code

@app.route('/',defaults={'path': ''})
@app.route('/<path:path>',methods=['GET','POST'])
def catch_all(path):
    DOC_ROOT="/look-ma-not-root"
    if path.replace(DOC_ROOT[1:],'').startswith('api'):
      return solr(request)

    P = os.path.join(DOC_ROOT,path.replace(DOC_ROOT[1:],'#'))

    tmpl = '''
       <html><script>window.location="%s"</script></html>
    ''' % P
    print tmpl
    return tmpl

if __name__ == '__main__':
  app.run(host="0.0.0.0",port=4999,debug=True)
