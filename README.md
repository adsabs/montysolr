bumblebee
=========

[![Build Status](https://travis-ci.org/adsabs/bumblebee.svg?branch=master)](https://travis-ci.org/adsabs/bumblebee)

This is a new UI for adslabs

A word or two as introduction into this bright new world of perplexity. Mistakes 
are wonderful gifts to humanity, IT IS OK TO MAKE MISTAKES. Please let no 
'getting it right' kill your creativity!


Things may change dramatically, be prepared.


dev setup - vagrant (virtualbox)
=================

Port 8000 is forwarded directly to the host (8000:8000). This directory is synced to /vagrant/ on the guest.

1. `vagrant up`
1. `vagrant ssh`
1. `cd /vagrant && grunt server`


dev setup - linux
=================

If you don't have node.js, do this (or equivalent of your distribution):

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

And you are ready to go!
========================

Set your editor to use spaces instead of tabs (width: 2)

```bash
  # run some tests
  $ grunt test:web

  # open a browser and look on what is inside
  $ open http://localhost:8000
```

If you need to change the way how build works, edit `local-config.js`

  * port_development: when running tests, webserver will start on this port [default: 8000]
  * port_development: port for webserver when running code from dist (ie. testing deployment) [default: 5000]
  * solr_endpoint: url of Solr service, see below for help on setting up a tunnel [default: http://localhost:9000/solr/select]
  * api_endpoint: url of API service, see below for help on setting up a tunnel [default: http://localhost:8000/api/1]


To help you get started, explore these examples: 

  * ./src/example.html (./src/js/apps/example)
    
	very stupid application showing use of requirejs to load modules 
	
  * ./src/todo.html (./src/js/apps/todo)
  
    a complete port of the TODO application from todomvc.com (using requirejs)
	
  * for interactive development of widgets
  
	 http://localhost:8000/test/test-widgets.html?top-region-left=js/widgets/api_query/widget&top-region-right=js/widgets/api_request/widget&middle-region-left=js/widgets/api_response/widget
	 
	 notice that each components is identified by its path (without .js suffix) eg. `top-region=js/widgets/api_query/widget`
  
  * finally, the complete test suite of the discovery application 
  
     http://localhost:8000/test/tests.html




Typical dev-cycle
=================

 1. write tests
 2. write code and make sure tests are passing
 3. ```grunt deploy``` -- this will prepare the target (note: deploy is not yet ready)


Take advantage of the headless testing framework! You can edit code and
have it automatically re-tested.
 
```bash
  # run tests (it will reload automatically and report in terminal)
  $ grunt test:web
  
  # or somewhat faster (but not good for cross site ajax calls)
  $ grunt test:local
```

By default, the PhantomJS will execute tests from: test/mocha/discovery.spec.html; you can
run other tests like so:

```bash
  $ grunt test:web --testname=mocha/foo  # assuming test/mocha/foo.spec.html exists!
```


Tests can also be opened in a browser, you can zone-in on certain class of tests when editing,
but you have to reload in your browser

```bash
   # open the test in a browser
   $ chrome ./test/mocha/todo.spec.html
```

When you want to interact with the server (and see the application through web):

 ```bash
  # start the webserver (it will automatically reload on code changes)
  $ grunt server
  ```

  
Explanation of the module structure:
====================================

I have discovered that none of the boilerplate generators are perfect (surprise ;)), hence inspiration was taken from these:

  - https://github.com/backbone-boilerplate/backbone-boilerplate
  - https://github.com/jkat98/benm
  - https://github.com/artsy/ezel


This is the current file/folder structure, with short explanation:  

```bash
/bumblebee.
   |-package.json
   |-bower.json
   |-Gruntfile.json
   |-server.js
   |   
   |-/bower_components
   |-/dist
   |-/node_modules
   |-/src/js     <-- our code lives here
   |---apps
   |-----discovery
   |-------img
   |-------styles
   |-------templates
   |---collections
   |---components
   |---libs
   |---modules
   |---views   
   |-/src/libs   <-- external dependencies
   |-/test
   |---mocha
   |-----apps
   |-------discovery
``` 

We are using bower, because npm (browserify) is not yet ready to package libraries for client development. It works great, but mind the following:

  - bower.json: specifies libraries that are needed for client-side ie. 
         inside browser (e.g. backbone)
  - package.json: lists libraries that are necessary for server-side, ie. development
         (e.g. grunt, webserver)
         

Contents
========

##### server.js
Development node.js server - it serves the statics files as well as provides proxy for /api
  requests.

##### Gruntfile.json
various instructions and commands, run: grunt --help
  
##### /bower_components & /node_modules
these will be created once you setup your environment
  
##### /dist
here we export the deployment-ready version of the code (compacted, minified, ready to be included into flask)
    
##### /src/js

  - this is the main folder where our code lives

  - ./apps: because we want to develop several separate applications (e.g. discovery,
    private libraries) I decided to keep them all here.
    
    - each application resides in its own folder, eg. ./apps/discovery and
      will have the standardized structure:
        - config.js = configuration for require.js loader
        - main.js = app creator (think: this code bootstraps app.js)
        - app.js = application itself
        - routes.js = mapping of paths

        
    - (it seems) that each application may have to carry its own static
      folders, eg. /styles /templates [but maybe we should create
      some mirroring tree just for the static files? Depends on what you
      consider to be a code: templates and css can be viewed as such]
     
  - ```bash
    ./modules
    ./models
    ./collections
    ./views
    ```
    
    - these folders should hold the *general, reusable* code that we develop,
      and that we want to import in our apps

##### /src/libs

  - created automatically, when you run grunt setup - contains
    all external dependencies (ie. backbone, requirejs)
    
    
##### /src/img, /src/css

  - other files (assets) needed for the application (mind that each application
    can also carry within itself some templates and even css; but we should 
    limit that to minimum and stick most of the assets here)

    
##### /test
 
  - ./mocha
  
    - because we may want to use several javascript testing frameworks, so far we
      keep the standard structure: [framework]/<tree>
      
    - the folder will mirror the code tree, eg.:
    ```bash
      ./mocha
         |
         /apps
         |  |
         |  /discovery
         |     |
         |     router.specs.js
         |     main.specs.js
         |  ...   
         /modules
             |
             facets.specs.js
     ```
    

             
You can refer to the following page for more links and discussion: 
http://labs.adsabs.harvard.edu/trac/ads-invenio/wiki/BackboneResources



miscellanea
===========

- you can run server.js from command line.  If you want to connect to our SOLR server, the easiest thing to do is to tunnel to adswhy:9000 like so:

  ```bash
  $ ssh -N -f -L 9000:adswhy.cfa.harvard.edu:9000 pogo3.cfa.harvard.edu
  $ env SOLR_ENDPOINT=http://localhost:9000/solr/select HOMEDIR=src node server.js
```
  
- if you want to debug/edit in place node.js code, use supervisor (you need to install it first)

  ```bash
  $ env SOLR_ENDPOINT=http://localhost:9000/solr/select HOMEDIR=src supervisor server.js
```

- you can see the search being routed to the SOLR api by accessing: http://localhost:port/api/1/search, for instance:
  http://localhost:port/api/1/search?q=kurtz 
