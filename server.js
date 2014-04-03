var express = require('express');
var http = require('http');
var url = require('url');
var search_re = /\/1\/search$/;
var app = express();

var API_ENDPOINT = process.env.API_ENDPOINT || "http://adswhy.cfa.harvard.edu:9000/solr/select";


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


// a simple 'proxy' that takes the query and fetches response
// from the remote url; care is taken to pass only parameters
// not to change the url path
app.use('/api', function (req, res, next) {
  var r = url.parse(req.url);
  var endpoint = API_ENDPOINT;
  var end = url.parse(API_ENDPOINT);

  console.log(req.body, r);

  if (r.pathname.match(search_re)) {
    // optionally swith endpoints
  }
  else {
    res.send(503, {error: 'Unknown service: ' + req.url + ' Are you using the correct endpoint (/api/1/search etc...)?'});
    return;
  }

  var options = {
    hostname: end.hostname,
    port: end.port,
    path: end.pathname,
    method: 'POST',
    data: r.query || req.body
  };

  console.log(options);

  //TODO: I'm doing something wrong - it is not making post request properly
  //var search = http.request(options, function (apiResponse) {
  var search = http.get(endpoint + '?' + r.query + '&wt=json', function (apiResponse) {
    res.writeHead(apiResponse.statusCode, {'Content-Type': apiResponse.headers['content-type']});
    apiResponse.on("data", function (chunk) {
      res.write(chunk);
    });
    apiResponse.on("end", function (chunk) {
      res.end();
    });
  }).on('error', function (err) {
      res.send(err.status || 500, {error: err.message});
      console.log("Got error: " + err.message);
    });
});


// serve the static files & folders
home = process.env.HOMEDIR || './';
app.use(express.static(__dirname + '/' + home));
app.use(express.directory(__dirname + '/' + home));

// map test dependencies so that we can run tests too
app.use('/src', express.static(__dirname + '/' + home));
app.use('/src', express.directory(__dirname + '/' + home));

app.use('/test', express.static(__dirname + '/test'));
app.use('/test', express.directory(__dirname + '/test'));

app.use('/bower_components', express.static(__dirname + '/bower_components'));
app.use('/bower_components', express.directory(__dirname + '/bower_components'));

port = process.env.PORT || 3000;
app.listen(port);

console.log('listening on port ' + port);
//console.log(process.env);

