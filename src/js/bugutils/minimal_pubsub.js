/**
 * A minimal request-response chain; this can be used in simple applications or
 * for testing purposes (ie. unittests) like this:
 *
 *
 * var minsub = new (MinimalPubsub.extend({
 *         request: function(apiRequest) {
 *           return {some: 'foo'}
 *         }
 *         }))({verbose: false});
 *
 *
 * // and the rest is standard mantra...
 *
 * var widget = new MyNewWidget();
 * widget.activate(minsub.beehive.getHardenedInstance());
 *
 * minsub.publish(minsub.START_SEARCH, new ApiQuery({q: 'star'}));
 *
 *
 * You just need to implement a request method, which returns a JSON
 * object (MinSub will wrap that object into ApiResponse) and
 * deliver to the requesting application
 *
 * Options:
 *
 *    - verbose: true/false
 *        will log into the console what is going on
 *
 */

define(['underscore', 'backbone',
  'js/components/query_mediator',
  'js/services/pubsub',
  'js/components/beehive',
  'js/components/pubsub_events',
  'js/components/api_query',
  'js/components/api_request',
  'js/components/api_feedback',
  'js/components/api_response'
], function(
  _,
  BackBone,
  QueryMediator,
  PubSub,
  BeeHive,
  PubSubEvents,
  ApiQuery,
  ApiRequest,
  ApiFeedback,
  ApiResponse
  ) {

  var MinimalPubsub = function() {
    this.beehive = null;
    this.pubsub = null;
    this.initialize.apply(this, arguments);
  };

  _.extend(MinimalPubsub.prototype, Backbone.Events, PubSubEvents,  {
    initialize: function(options) {
      if (options.verbose) {
        console.log('[MinSub]', 'starting');
      }
      this.requestCounter = 0;
      this.beehive = new BeeHive();
      this.pubsub = new PubSub();
      this.pubsub.debug = true;
      this.beehive.addService('PubSub', this.pubsub);
      var self = this;
      this.beehive.addService('Api', options.Api || {
        request: function(req, context) {
          self.requestCounter += 1;
          if (!context) {
            context = req.get('options');
          }
          var response = self.request.apply(self, arguments);
          if (self.verbose) {
            console.log('[MinSub]', 'request', self.requestCounter, response);
          }
          var defer = $.Deferred();
          defer.done(function() {
            context.done.call(context.context, response);
          });
          defer.resolve(response);
          return defer.promise();
        }});
      this.beehive.addObject('QueryMediator', new QueryMediator({
        recoveryDelayInMs: 0,
        shortDelayInMs: 0,
        longDelayInMs: 0,
        monitoringDelayInMs: 5
      }));
      this.beehive.activate();
      this.key = this.pubsub.getPubSubKey();
      this.listen();
      _.extend(this, options);
    },

    listen: function() {
      this.pubsub.subscribe(this.pubsub.getPubSubKey(), 'all', _.bind(this.logAll, this));
    },

    close: function(){
      if (this.verbose) {
        console.log('[MinSub]', 'closing');
      }
      this.beehive.close();
    },

    logAll: function(ev) {
      if (this.verbose) {
        var args = Array.prototype.slice.call(arguments, 1);
        console.log('[PubSub]', ev, args);
      }
    },

    publish: function() {
      var args = _.toArray(arguments);
      args.unshift(this.key);
      this.pubsub.publish.apply(this.pubsub, args);
    },

    subscribe: function(signal, callback) {
      var args = _.toArray(arguments);
      args.unshift(this.key);
      this.pubsub.subscribe.apply(this.pubsub, args);
    },
    subscribeOnce: function(signal, callback) {
      var args = _.toArray(arguments);
      args.unshift(this.key);
      this.pubsub.subscribeOnce.apply(this.pubsub, args);
    },
    on: function() {
      this.pubsub.on.apply(this.pubsub, arguments);
    },
    off: function() {
      this.pubsub.on.apply(this.pubsub, arguments);
    },

    request: function(apiRequest, params) {
      if (this.verbose) {
        console.log('[Api]', 'request', apiRequest, params);
      }
      return {};
    },

    createQuery: function(data) {
      return new ApiQuery(data);
    },

    createRequest: function(data) {
      return new ApiRequest(data);
    },

    createFeedback: function(data) {
      return new ApiFeedback(data);
    },

    T: {
      RESPONSE: ApiResponse,
      REQUEST: ApiRequest,
      QUERY: ApiQuery,
      FEEDBACK: ApiFeedback
    }

  });

  MinimalPubsub.extend = Backbone.Model.extend;
  return MinimalPubsub;
});