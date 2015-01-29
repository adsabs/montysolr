Explanation of the module structure:
====================================


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
         
