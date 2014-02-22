var express = require('express');
var http = require('http');
var url = require('url');
var app = express();

var API_ENDPOINT = process.env.API_ENDPOINT || "http://adswhy.cfa.harvard.edu:9000/solr/select";


// this examples does not have any routes, however
// you may `app.use(app.router)` before or after these
// static() middleware. If placed before them your routes
// will be matched BEFORE file serving takes place. If placed
// then file serving is performed BEFORE any routes are hit:
app.use(app.router);


// log requests
app.use(express.logger('dev'));


// a simple 'proxy' that takes the query and fetches response
// from the remote url; care is taken to pass only parameters
// not to change the url path
app.use('/api/search', function(req, res, next){
    var r = url.parse(req.url);
    
    //console.log(r);
    
    if (r.pathname !== '/') {
      res.send(503, {error: 'Unknown service: ' + req.url});
    }
    
    var search = http.get(API_ENDPOINT + '?' + r.query, function(apiResponse) {
        res.writeHead(apiResponse.statusCode, {'Content-Type': apiResponse.headers['content-type']});
        apiResponse.on("data", function(chunk) {
            res.write(chunk);
        });
        apiResponse.on("end", function(chunk) {
            res.end();
        });
    })
    .on('error', function(err) {
        res.send(err.status || 500, {error: err.message});
        console.log("Got error: " + err.message);
    });
});


// serve the static files & folders
home = process.env.HOMEDIR || './';
app.use(express.static(__dirname + '/' + home));
app.use(express.directory(__dirname + '/' + home));



port = process.env.PORT || 3000;
app.listen(port);

console.log('listening on port ' + port);
//console.log(process.env);

