/**
 * WARNING: this is a DEVELOPMENT server; DO NOT USE it in production!
 *
 * If you need to make SOLR available, you can use:
 *  http://github.com/adsabs/adsws
 *  http://github.com/adsabs/solr-service
 *
 * @type {exports}
 */

var express = require('express');
var http = require('http');
var url = require('url');
var needle = require('needle');
var querystring = require('querystring');

var search_re = /\/v?1\/search$/;
var qtree_re = /\/v?1\/qtree$/;
var bootstrap_re = /\/v?1\/bumblebee\/bootstrap$/;

var app = express();

var API_ENDPOINT = process.env.API_ENDPOINT || 'http://localhost:5000/api/1';
var SOLR_ENDPOINT = process.env.SOLR_ENDPOINT || API_ENDPOINT || "http://adswhy.cfa.harvard.edu:9000/solr/select";

//sandbox tokens (not for production; these are sandbox values for testing)
var ORCID_OAUTH_CLIENT_ID = process.env.ORCID_OAUTH_CLIENT_ID || 'APP-P5ANJTQRRTMA6GXZ';
var ORCID_OAUTH_CLIENT_SECRET = process.env.ORCID_OAUTH_CLIENT_SECRET || '989e54c8-7093-4128-935f-30c19ed9158c';
var ORCID_API_ENDPOINT = process.env.ORCID_API_ENDPOINT || 'https://sandbox.orcid.org';


// this examples does not have any routes, however
// you may `app.use(app.router)` before or after these
// static() middleware. If placed before them your routes
// will be matched BEFORE file serving takes place. If placed
// then file serving is performed BEFORE any routes are hit:
app.use(app.router);


app.use(express.json());       // to support JSON-encoded bodies
app.use(express.urlencoded()); // to support URL-encoded bodies

// log requests
app.use(express.logger('dev'));


//$ curl -i -L -H 'Accept: application/json' --data 'client_id=APP-P5ANJTQRRTMA6G
//XZ&client_secret=989e54c8-7093-4128-935f-30c19ed9158c&grant_type=authorization_
//code&code=H1trXI' 'https://api.sandbox.orcid.org/oauth/token'

app.use('/exchangeOAuthCode', function(req, res, next) {
  console.log(req.query);
  var code = req.query.code;

  var data = {
    code: code,
    grant_type: 'authorization_code',
    client_id: ORCID_OAUTH_CLIENT_ID,
    client_secret: ORCID_OAUTH_CLIENT_SECRET
  };
  console.log(data);

  var options = {
    headers: {
      'content-type': 'application/x-www-form-urlencoded',
      'Accept': 'application/json'
    }
  };

  console.log('foo', options);
  console.log('endpoint', ORCID_API_ENDPOINT + '/oauth/token');
  console.log('data', data)

  needle.post(ORCID_API_ENDPOINT + '/oauth/token', data, options, function(err, resp, body) {
    if (err) {
      console.log('error', err)
      res.send(err.status || 500, err);
    }
    else {
      console.log('done', body)
      res.send(resp.statusCode, body);
      //res.writeHead(resp.statusCode, {'Content-Type': resp.headers['content-type']});
      //res.write(body);
      //res.end();
    }
  });
});

// a simple 'proxy' that takes the query and fetches response
// from the remote url; care is taken to pass only parameters
// not to change the url path
app.use('/api', function (req, res, next) {
  var r = url.parse(req.url);
  var endpoint = SOLR_ENDPOINT;
  var end = url.parse(SOLR_ENDPOINT);

  console.log('/api', req.body, r);

  if (r.pathname)
    r.pathname = r.pathname.replace(/\/\/+/, '/');

  if (r.pathname.match(search_re)) {
    // optionally swith endpoints
  }
  else if(r.pathname.match(qtree_re)) {
    end.pathname = '/solr/qtree';
  }
  else if (r.pathname.match(bootstrap_re)) {
    end = url.parse(API_ENDPOINT);
    end.pathname = '/api/1/bumblebee/bootstrap';
  }
  else {
    res.send(503, {error: 'Unknown service: ' + req.url + ' Are you using the correct endpoint (/api/1/search etc...)?'});
    return;
  }

  var payload;
  if (r.query) {
    payload = querystring.parse(r.query);
  }
  else {
    payload = req.body;
  }

  // prevent getting fulltext data
  payload.wt = 'json';
  if (payload.fl) {
    var fields = payload.fl.split(',');
    if (fields.indexOf('body') > -1) {
      fields[fields.indexOf('body')] = 'body_';
      payload.fl = fields.join(',');
    }
  }
  else { // in this case solr would return everything
    payload.fl = 'id';
  }


  var options = {
    hostname: end.hostname,
    port: end.port,
    path: end.pathname,
    method: 'POST',
    data: querystring.stringify(payload)
  };

  if (!r.query) {
    req.headers['Content-Type'] = 'application/x-www-form-urlencoded';
    //req.headers['Content-Type'] = 'application/json';
    //req.headers['Content-Length'] = options.data.length;
  }

  console.log('final query', end.hostname + ':' + end.port + end.pathname, options, req.headers);

  var n = needle.post(end.hostname + ':' + end.port + end.pathname,
    options.data,
    {parse: false, headers: req.headers, timeout:0},
    function (err, apiResponse, body) {
      if (err) {
        console.log("Got error: " + err.message);
        res.send(err.status || 500, {error: err.message});
      }
      else {
        res.writeHead(apiResponse.statusCode, {'Content-Type': apiResponse.headers['content-type']});
        res.write(body);
        res.end();
      }
    });


});





// serve the static files & folders
home = process.env.HOMEDIR || 'src';

app.use("/", express.static(__dirname + '/' + home));
app.use('/', express.directory(__dirname + '/' + home));

// map test dependencies so that we can run tests too
app.use('/test', express.static(__dirname + '/test'));
app.use('/test', express.directory(__dirname + '/test'));
app.use('/bower_components', express.static(__dirname + '/bower_components'));
app.use('/bower_components', express.directory(__dirname + '/bower_components'));
app.use('/node_modules', express.static(__dirname + '/node_modules'));
app.use('/node_modules', express.directory(__dirname + '/node_modules'));

port = process.env.PORT || 3000;


app.get( /\/index\.html\/.*$/, function(req, res) {

  // Prepare the context
  res.sendfile( home + '/index.html');

});


app.listen(port);


console.log('listening on port ' + port);
console.log('HOMEDIR', home, process.env.HOMEDIR);
console.log('API_ENDPOINT', API_ENDPOINT, process.env.API_ENDPOINT);
console.log('SOLR_ENDPOINT', SOLR_ENDPOINT, process.env.SOLR_ENDPOINT);
//console.log(process.env);
