define([], function() {
  "use strict";
  var config = {

    /**
     * You can change the application
     */

    /**
     * Where to insert the application
     */
    targetElement: 'div#embed',

    /**
     * Widget to load and insert into the application
     * (this will be loaded by requirejs, so it can be
     *  - symbolic name: defined in config's paths
     *  - relative path: 'js/widgets/hello_world'
     *  - absolute url '//somewhere.org/my/widget'
     */
    mainWidget: 'js/widgets/hello_world/widget',

    /**
     * The url to the API services, if you develop locally,
     * (and have created tunnel to the API) you can set this
     * to //localhost:5000/v1 - but since our API allows for
     * limited number of cross-site requests (from origin
     * of 'http://localhost:8000' you can also use the production
     * API of //api.adsabs.harvard.edu/v1/
     */
    apiRoot: '//api.adsabs.harvard.edu/v1/',

    /**
     * to let bumblebee discover oauth access_token at boot time
     * and load dynamic configuration (which will be merged with
     * the default config)
     * this can be absolute url; or url relative to the api path
     */
    bootstrapUrls: ['http://localhost:8000/embed.dyn.js'],

    /**
     *  pushState: when true, urls are without hashtag '#'
     *  root is the url, under which your application is
     *  deployed, eg. /foo/bar if the main page lives at
     *  http://somewhere.org/foo/bar/index.html
     */
    routerConf: {
      pushState: false,
      root: '/',
    },

    /**
     * When set to true, window.app will contain reference to
     * to the application object
     */
    debugExportBBB: true,

    /**
     * To get debugging output in console
     */
    debug: false,

  }
  return config
});
