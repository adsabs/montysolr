bumblebee
=========

[![Build Status](https://travis-ci.org/adsabs/bumblebee.svg?branch=master)](https://travis-ci.org/adsabs/bumblebee)

Bumblebee is a platform for building UI interface(s) - it was designed for Astrophysics Data System (https://ui.adsabs.harvard.edu) and its API.

Bumblebee uses Backbone, jQuery, and Bootstrap and you will find here many custom components/widgets. **Our main 'discovery' application is essentially a very advanced front-end for any SOLR. However, be warned that your SOLR instance should to be protected against abuse*. If you don't know how, use these projects:

  - http://github.com/adsabs/adsws (our own API middle-layer)
  - http://github.com/adsabs/solr-service (micro-service which exposes SOLR)

Installation
============

To get Bumblebee up and running on your machine, follow *one* of these instructions:


dev setup - vagrant (virtualbox)
================================

This is the easiest option. It will create a virtual machine and run the application. No hassles.
Port 8000 is forwarded directly to the host (8000:8000). This directory is synced to /vagrant/ on the guest.

1. `vagrant up`
1. `vagrant ssh`
1. `cd /vagrant && grunt server`


dev setup - linux
=================

```bash
  $ sudo apt-get install node npm phantomjs
  $ sudo npm install -g grunt-cli

  # if you don't have 'node' but 'nodejs' (on DEBIAN), you also need:
  $ sudo apt-get install nodejs-legacy
```

Now (inside the project), run:

```bash
  # install the dependencies from package.json
  $ npm install
  
  # setup the project (libraries)
  $ grunt setup 
```

dev setup - mac OS X
====================

If you have mac port:

```bash
  $ sudo port install nodejs npm phantomjs
  $ sudo npm install -g grunt-cli
  $ npm install
  $ grunt setup
```

dev setup - windows
===================

Yes, you can develop even on Windows! ;-)

1. install http://msysgit.github.io/
2. install node.js http://nodejs.org/download/
3. install http://phantomjs.org/download.html
4. open Git Bash from Windows Start 

```bash
   $ cd bumblebee 
   $ npm install -g grunt-cli 
   $ npm install 
   $ grunt setup 
```

Configuration
=============

You need to review/update configuration. The web application loads its config from local modules and from remote urls (if configured to do so). The most important config files are:

  * ./src/js/discovery.config.js

    This is the require.js configuration, but we also keep the definition
    of all modules/widgets/plugins that the application should load (or make
    available) there. It is in the section: 'js/apps/discovery/main'

  * src/js/discovery.vars.js

    This is for frequently changing variables (e.g. what is the main url under which
    bumblebee is running, or the url to the API services). By default,
    this file will be created during 'grunt setup' and if it already exists, it
    will NOT be overwritten. Look at 'discovery.vars.js.defaults' for explanation.

  * dynamic discovery

    The application can also load config from remote urls (during startup) if
    configured to do so in src/js/discoverry.vars.js >> 'bootstrapUrls'


Deployment
==========

Bumblebee contains set of components alongside with (mini) applications. To deploy stuff, you need to make a release and expose it through a webserver. During deployment, bumblebee will minify CSS, JS and also add MD5 hashes to the libraries.

```bash
 cd bumblebee
 grunt release
 grunt test:release # optional step - run tests against the assembled release
 cp -r dist /my/web/root # copy the release into a webserver
``` 




Developing for bumblebee
========================

We develop very robust code, with excellent test coverage, however without promises of backward compatibility. Things change dramatically and we are moving fast. Do not let 'getting it right' kill your creativity, IT IS OK TO MAKE MISTAKES!

Set your editor to use spaces instead of tabs (width: 2)

When you want to interact with the application:

 ```bash
  # start the webserver (it will automatically reload on code changes)
  $ grunt server
  $ open http://localhost:8000
  ```


Take advantage of the headless testing framework! You can be editing
code and have it automatically re-tested.
 
```bash
  # run tests
  $ grunt test:web
```

By default, the PhantomJS will execute all tests; you can run specific suite as:

```bash
  $ grunt test:web --testname='mocha/tests.html?bbbSuite=core-suite'
```


Tests can also be opened in a browser, you can click on a certain class of tests
(to ignore others), and you can combine test suites:

```bash
   # open the test in a browser
   $ chrome http://localhost:8000/test/mocha/tests.html?bbbSuite=ui-suite|core-suite
```

To get coverage reports:

```bash
   # open the test in a browser
   $ chrome http://localhost:8000/test/mocha/coverage.html?bbbSuite=core-suite
```


To make a pull request, clone our repo, create a new branch, add your changes,
push the new branch to github and open a new pull request.
_Before making the push, make sure unittests and coverage tests pass!_


```bash
   $ grunt test coverage
.
.
PASS [100% > 60% ] : wraps/references.js (3 / 3)
PASS [ 73% > 60% ] : wraps/table_of_contents.js (11 / 15)

Global Coverage Results: (63% minimum)
PASS [ 63% = 63% ] : global (4982 / 7818)

Unit Test Results: 271 specs passed! (6.04s)
>> No issues found.

Done, without errors.
```

To run test suites against multiple browsers, use Sauce Labs. First edit local-config.js,
set the sauce_ variables (you should get them from Sauce Labs account)

```bash
$ grunt sauce # optionally, past name of the test, ie. --testname='mocha/tests.html?bbbSuite=core-suite'
```


If you need to change the way how grunt works, you can edit `local-config.js`

  * port_development: when running tests, webserver will start on this port [default: 8000]
  * port_development: port for webserver when running code from dist (ie. testing deployment) [default: 5000]
  * solr_endpoint: url of Solr service, see below for help on setting up a tunnel [default: http://localhost:9000/solr/select]
  * api_endpoint: url of API service, see below for help on setting up a tunnel [default: http://localhost:8000/api/1]


To help you get started, explore these:

  * ./src/example.html (./src/js/apps/example)

	very stupid application showing use of requirejs to load modules

  * ./src/todo.html (./src/js/apps/todo)

    a complete port of the TODO application from todomvc.com (using requirejs)

  * for interactive development of widgets

	 http://localhost:8000/test/test-widgets.html?top-region-left=js/widgets/api_query/widget&top-region-right=js/widgets/api_request/widget&middle-region-left=js/widgets/api_response/widget

	 notice that each components is identified by its path (without .js suffix) eg. `top-region=js/widgets/api_query/widget`

  * finally, the complete test suite of the discovery application

     http://localhost:8000/test/tests.html

Documentation
=============
       
Look inside the docs folder, mainly:
       
  - [How to write a widget](./docs/how-to-write-widget.md)
  - [Architecture Overview](./docs/architecture.md) 
  - [Explanation of the Search Cycle](./docs/search-cycle.md)

  

Miscellanea
===========

- You can run server.js from command line.  If you want to connect to our SOLR server, the easiest thing to do is to tunnel to adswhy:9000 like so:

  ```bash
  $ ssh -N -f -L 9000:adswhy.cfa.harvard.edu:9000 pogo3.cfa.harvard.edu
  $ env SOLR_ENDPOINT=http://localhost:9000/solr/select HOMEDIR=src node server.js
```
  
- If you want to debug/edit in place node.js code, use supervisor (you need to install it first)

  ```bash
  $ env SOLR_ENDPOINT=http://localhost:9000/solr/select HOMEDIR=src supervisor server.js
```

- You can see the search being routed to the SOLR api by accessing: http://localhost:port/api/1/search, for instance:
  http://localhost:port/api/1/search?q=kurtz 
       


Bumblebee is developed by ADS, some devs use WebStorm - the open-source license was kindly provided by JetBrains.


[![WebStorm](http://www.jetbrains.com/webstorm/documentation/docs/logo_webstorm.png)](http://www.jetbrains.com/webstorm)
[![Analytics](https://ga-beacon.appspot.com/UA-37369750-9/adsabs/bumblebee?flat)](https://github.com/igrigorik/ga-beacon)
