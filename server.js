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

//BC:rca - these secret tokens, can they be committed into a repository?
var ORCID_OAUTH_CLIENT_ID = process.env.ORCID_OAUTH_CLIENT_ID || 'APP-P5ANJTQRRTMA6GXZ';
var ORCID_OAUTH_CLIENT_SECRET = process.env.ORCID_OAUTH_CLIENT_SECRET || '989e54c8-7093-4128-935f-30c19ed9158c';
var ORCID_API_ENDPOINT = process.env.ORCID_API_ENDPOINT || 'https://api.sandbox.orcid.org';
var ORCID_REDIRECT_URI = 'http://localhost:3000/oauthRedirect.html';


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

app.use('/oauth/redirect', function(req, res, next){
  var code = req.query.code;

  // pair thru ids in headers ????
});

//https://sandbox.orcid.org/oauth/authorize
// ?client_id=APP-P5ANJTQRRTMA6GXZ
// &response_type=code
// &scope=/orcid-profile/read-limited
// &redirect_uri=https://developers.google.com/oauthplayground

app.use('/oauth/getAuthCode', function(req, res, next){
  var scope = req.query.scope;
  var data = {
    client_id: ORCID_OAUTH_CLIENT_ID,
    response_type: 'code',
    scope: scope,
    redirect_uri: ORCID_REDIRECT_URI
  };

  var options = {
    headers: {
      'content-type': 'application/x-www-form-urlencoded'
    }
  };

  needle.post(ORCID_API_ENDPOINT + '/oauth/authorize', data, options, function(err, resp, body) {
    if (err) {
      res.send(err.status || 500, err);
    }
    else {
      res.send(resp.statusCode, body);
    }
  });

});

app.use('/oauth/exchangeAuthCode', function(req, res, next) {
  var code = req.query.code;
  var redirect_uri = req.query.redirectUri;
  var scope = req.query.scope;

  var data = {
    code: code,
    redirect_uri: redirect_uri,
    scope: scope,
    grant_type: 'authorization_code',
    client_id: ORCID_OAUTH_CLIENT_ID,
    client_secret: ORCID_OAUTH_CLIENT_SECRET
  };

  var options = {
    headers: {
      'content-type': 'application/x-www-form-urlencoded'
    }
  };

  needle.post(ORCID_API_ENDPOINT + '/oauth/token', data, options, function(err, resp, body) {
    if (err) {
      res.send(err.status || 500, err);
    }
    else {
      res.send(resp.statusCode, body);
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
home = process.env.HOMEDIR || '/';
//app.use(express.static(__dirname + '/' + home));
//app.use(express.directory(__dirname + '/' + home ));

app.use("/", express.static('src'))

// map test dependencies so that we can run tests too
app.use('/src', express.static(__dirname + '/' + home));
app.use('/src', express.directory(__dirname + '/' + home));

app.use('/test', express.static(__dirname + '/test'));
app.use('/test', express.directory(__dirname + '/test'));

app.use('/bower_components', express.static(__dirname + '/bower_components'));
app.use('/bower_components', express.directory(__dirname + '/bower_components'));
app.use('/node_modules', express.static(__dirname + '/node_modules'));
app.use('/node_modules', express.directory(__dirname + '/node_modules'));

port = process.env.PORT || 3000;


app.get( /\/index\.html\/.*$/, function(req, res) {

  // Prepare the context
  res.sendfile( home + '/index.html' )

});


app.listen(port);


console.log('listening on port ' + port);
console.log('API_ENDPOINT', API_ENDPOINT, process.env.API_ENDPOINT);
console.log('SOLR_ENDPOINT', SOLR_ENDPOINT, process.env.SOLR_ENDPOINT);
//console.log(process.env);



