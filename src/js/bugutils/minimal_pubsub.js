define(['underscore', 'backbone',
  'js/components/query_mediator',
  'js/services/pubsub',
  'js/components/beehive',
  'js/components/pubsub_events'
], function(
  _,
  BackBone,
  QueryMediator,
  PubSub,
  BeeHive,
  PubSubEvents
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
      this.beehive.addService('PubSub', this.pubsub);
      var self = this;
      this.beehive.addService('Api', {
        request: function(req, context) {
          self.requestCounter += 1;
          var response = self.request.apply(self, arguments);
          if (self.verbose) {
            console.log('[MinSub]', 'request', self.requestCounter, response);
          }
          context.done.call(context.context, response);
        }});
      this.beehive.addObject('QueryMediator', new QueryMediator());
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

    request: function(apiRequest, params) {
      if (this.verbose) {
        console.log('[Api]', 'request', apiRequest, params);
      }
      return {};
    }

  });

  MinimalPubsub.extend = Backbone.Model.extend;
  return MinimalPubsub;
});