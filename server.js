/**
 * WARNING: this is a DEVELOPMENT server; DO NOT USE it in production!
 *
 * If you need to make SOLR available, you can use:
 *  http://github.com/adsabs/adsws
 *  http://github.com/adsabs/solr-service
 *
 * @type {exports}
 */

const express = require('express');
const path = require('path');
const proxy = require('http-proxy-middleware');
const app = express();
const targets = {
  dev: 'https://devapi.adsabs.harvard.edu',
  prod: 'https://api.adsabs.harvard.edu',
  qa: 'https://api.adsabs.harvard.edu'
};

// default configuration
let config = {

  // root path for web (usually src/ or dist/)
  root: '/src',

  // api route, this will be proxied
  apiPath: '/v1',

  // proxy settings
  proxy: {
    target: targets[process.env.TARGET || 'dev'],
    changeOrigin: true
  }
};

if (process.env.NODE_ENV === 'release') {
  config = Object.assign(config, { root: '/dist' });
}

// serve the static assets
app.use('/', express.static(path.join(__dirname, config.root)));
app.use('/test', express.static(path.join(__dirname, '/test')));
app.use('/node_modules', express.static(path.join(__dirname, '/node_modules')));
app.use('/bower_components', express.static(path.join(__dirname, '/bower_components')));

// proxy api calls to the api endpoint
app.use(config.apiPath, proxy(config.proxy));

// start the server
app.listen(8000, () => {
  console.log('config: ', JSON.stringify(config));
  console.log('Listening on port 8000');
});




