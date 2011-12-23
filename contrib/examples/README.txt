
This folder holds the configuration files of the various Solr examples/demos.


Upon installation, the examples contain only the files that were changed or/and 
are not present in the original Solr distribution.

If you want to assemble the examples, do:

  $ cd montysolr
  $ ant examples
  

== RUNNING THE DEMO ==

1. edit the build.properties file and set the webdist variable
    eg. webdist=./examples/twitter
2. run the demo (from the montysolr root)
    $ ant run-montysolr-daemon