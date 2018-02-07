
'use strict';
define([
  'underscore',
  'require',
  'analytics_config',
  'jquery'
], function (_, require, config, $) {

  /*
   * Set of targets
   * each has a set of hooks which coorespond to the event label passed
   * types represents the possible event targets which can be used
   * url is a template which will be passed the incoming data
   */
  var TARGETS = {
    'resolver': {
      hooks: ['toc-link-followed'],
      types: [
        'abstract', 'citations', 'references',
        'metrics', 'coreads', 'graphics'
      ],
      url: _.template('resolver/<%= bibcode %>/<%= target %>')
    }
  };

  /**
   * fire off the xhr request to the url
   *
   * @param {string} url
   * @param {object} data
   */
  var sendEvent = function (url) {
    $.ajax({ url: url, type: 'GET' });
  };

  /**
   * Go through the targets and fire the event if the label passed
   * matches one of the hooks specified.  Also the data.target must match one
   * of the types listed on the target config
   *
   * @param {string} label - the event label
   * @param {object} data - the event data
   */
  var adsLogger = function (label, data) {

    // if label or data is not present, do nothing
    if (_.isString(label) || _.isPlainObject(data)) {
      _.forEach(TARGETS, function (val, key) {

        // send event if we find a hook and the target is in the list of types
        if (_.contains(val.hooks, label) && _.contains(val.types, data.target)) {
          sendEvent(val.url(data));
        }
      });
    }
  };

  if (window.GoogleAnalyticsObject)
    return function () { window[window.GoogleAnalyticsObject].apply(this, arguments); };

  if (!config) {
    console.error('Analytics will not work because require.config["js/components/analytics"]["require"] does not tells where to find config');
    return function() {}; // empty function, ignoring
  }

  var Analytics,
    gaName = "ga"; // Global name of analytics object. Defaults to `ga`.

  // Setup temporary Google Analytics objects.
  window.GoogleAnalyticsObject = gaName;
  window[gaName] = function () { (
    window[gaName].q = window[gaName].q || []).push(arguments);
    // safety measure
    if (window[gaName].q.length > 100)
      window[gaName].q = window[gaName].q.slice(0, 50)
  };
  window[gaName].l = 1 * new Date();

  // Create a function that wraps `window[gaName]`.
  // This allows dependant modules to use `window[gaName]` without knowingly
  // programming against a global object.
  Analytics = function () {
    adsLogger.apply(null, _.rest(arguments, 3));
    window[gaName].apply(this, arguments);
  };

  // Immediately add a pageview event to the queue.
  window[gaName]("create", config.googleTrackingCode, config.googleTrackingOptions);

  // Asynchronously load Google Analytics, letting it take over our `window[gaName]`
  // object after it loads. This allows us to add events to `window[gaName]` even
  // before the library has fully loaded.
  var defer = $.Deferred();
  require(['google-analytics'], function(ga) {
    defer.resolve();
  }, function(err) {
    console.warn('google-analytics could not be loaded; we will work just fine without it', err);
    defer.reject();
  });
  Analytics.promise = defer.promise();

  return Analytics;
});
