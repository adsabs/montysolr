Deployment strategy
===================

There is multitude of options, this document just summarizes my current thoughs
(and the strategy I took) with regard to deployment

Requirements:
  - minimize loading time
    - the load time has to main reasons: size of the downloaded file and number
      of requests. Browsers limit number of simultaneous threads per domain (2-4,
      or sometimes 6)
    - we could dump everything into one big file, but that is wrong because some
      files are changing more often that others. Besides, mobile clients have
      stricter limits on file sizes (ie. old mobile phones will not save big files,
      so effectively negating the cache)
    - it makes sense to minify file contents, but it doesn't make sense to uglify
      them (that can introduce hideous bugs; it adds only 4% savings)
    - gzip and compression is good, we should let webserver handle that
    - CDN might be good, but probably only for frequent libraries (this needs to
      be tested; some libraries cannot be found on big CDN's - like backone - and
      according to statistics, only 2-4% percent of users visit websites that
      loaded the same js libs; thus the browser doesn't need to request it again
      todo: link to w3trends)
    
  - frequent releases
    - we are often rolling out new code, this should be very automatic and (painless)
      experience to both user and developer
    - basically, we must force browser to reload resources:
      - headers are too brittle (must be configured outside of bbb; we could let
        webserver do it, but again it is more involved)
      - using 'urlArgs' require.js could prepend a string to any download, i.e.
        it would force browsers to request the latest version. But I don't like this,
        because it requires manual intervention (change of the urlArgs config), but
        most importantly, certain (mis-configured) caches will not cache files
        with url parameters (and will force reload)
      - 'cache busting': this technique adds checksum (MD5 of file contents) to the
        file name; so everytime the file contents changes, so does the file name 
        effectively forcing browser to reload

  - robustness
    - we need to exchange complexity for speed, however it should not introduce
      bugs; the deployment should be understandable so that it can be maintained
      and fixed (if necessary)
      

Strategy:
=========
      
So, based on lots of reading and fair amount of thinking, I came with this strategy 
for deployment of new BBB releases. These steps must happen in order:

  0. clean up (grunt clean:dist)
    
    - remove ```./dist``` folder 
    
  1. rebuild (```grunt setup less```)
    
    - setup downloads all version of js libraries and places them inside ```src/libs``` 
      (this is just to ensure that release runs with the versions committed to the
      repo; developers sometimes run a machine with old versions)
    - concatenates all CSS into one file ```src/styles/css/style.css```
    
  2. copy (```grunt copy:release```)
    
    - copy *everything* from inside ```./src``` into ```./dist```
    - everything means all libraries, js code, templates, css... the whole BBB
      application is contained in this one folder
      
  3. minify (```grunt requirejs:minify_release```)
  
    - this step works with contents of ```dist``` only
    - takes all of our javascript files (and the big css) and minifies them 
      (it ignores js/libs)
    - the original files are replaced with the minified files
    - (should js maps be generated???)
    - open question: if I generate a module with all of its dependencies wrapped,
      can we still use 'cache dumping'?
    
  4. hash (```grunt hash_require:js hash_require:css```)
  
    - add a md5 checksum to all of our js code and to the main CSS, e.g.
      ```js/components/api_query.js``` will become ```js/components/api_query.c544d137.js```
    - this process will generate two files; these files contain the orinal
      path and the new hashed pathname. These maps can be used to generate new
      require.js config. And also html files need to be updated
      - jsmap.json
      - cssmap.json
    - the original (minified) files are kept in place, the hashed versions are
      saved next to them (same folder)
    - hash is appended to the file, even if S3 may work better/faster if we
      were to prepend the prefix (but we are not using S3 at the moment, so it 
      seems just easier on developers)
      
    - ??? shall we do the same for html templates
    
  5. assemble (```grunt assemble```)
  
    - the html file (index.html) must be modified each time
      - adding path to the ```discovery.config.<hash>.js```
      - adding correct path to the ```styles/css/style.<hash>.js```
      
    - discovery.config.<hash>.js must be generated everytime anew
    
      - this config must contain paths to all files that were affected by our 
        changes. (ie. config.path section must list 'symbolic-name' -> 'hashed-path')
        
        - our symbolic names are always written as 'js/component/foo' or 
          'js/widgets/foo'; the js libraries that we are using are written 
          using their name, e.g. 'backbone' or 'jquery'
            
      - but somehowe we'll have to force existing clients to check for
        new versions. Even if we set no-cache pragma on the discovery config,
        it will not affect running clients. This will have to be added to the
        application logic
        
       - as a consequence: we need to keep previous versions around, because
         old clients will be using old paths. This means that new versions
         must be copied into the same folder (they will not interfere, because
         of hashes)
    
  6. verify
    
    - you can run all tests against the application and they should pass
    - to do that, we have to start development server in release mode ```grunt server:relese```
      this will be serving files from ```./dist``` under /
    - a special file called test.html will be inserted into ./dist, you 
      can access http://localhost:8000/test.html
    
  7. deploy
  
    - this happens outside of grunt; it is a matter of copying the ```./dist```
      folder into the destination where webserver serves it
    - there is just one caveat (as per the last point): we need to have access
      to the previous realeases, therefore the old files should be kept there (or
      we have to force clients to reload everything on every new release)
      
      

Interesting links: 
 - https://developer.yahoo.com/performance/rules.html#num_http
 - http://modernweb.com/2014/03/10/is-jquery-too-big-for-mobile/
 