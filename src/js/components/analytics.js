define([
  'require',
  'analytics_config',
  'jquery'
], function (
  require,
  config,
  $) {

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
  Analytics = function () { window[gaName].apply(this, arguments); };

  // Immediately add a pageview event to the queue.
  window[gaName]("create", config.googleTrackingCode, config.googleTrackingOptions);
  window[gaName]("send", "pageview");

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