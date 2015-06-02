/**
 * Created by rchyla on 3/19/14.
 */

define(['underscore', 'jquery', 'backbone', 'marionette',
  'js/components/api_request',
  'js/components/api_query',
  'js/components/pubsub_events',
  'hbs!./templates/widget-view',
  'hbs!./templates/item-view'

],

  function(_, $, Backbone, Marionette,
           ApiRequest, ApiQuery, PubSubEvents,
           WidgetTemplate, ItemTemplate){

    // Model
    var KeyValue = Backbone.Model.extend({ });

    // Collection of data
    var KeyValueCollection = Backbone.Collection.extend({
      model : KeyValue
    });

    var ItemView = Marionette.ItemView.extend({
      tagName: 'tr',
      template : ItemTemplate,
      events : {
        'blur .value>input': 'onChange'
      },
      onChange: function(ev) {
        var container = $(ev.target);
        var attr = _.clone(this.model.attributes);
        var newVal = $(ev.target).val();

        if (container.attr('name') == 'value' && attr.value != newVal) {
          this.trigger('value-changed', this.model, newVal);
        }
      }
    });

    var WidgetView = Marionette.CompositeView.extend({
      template : WidgetTemplate,
      childView : ItemView,
      childViewContainer: "#api-request-values",
      events: {
        'click button#api-request-load': 'loadApiRequest',
        'click button#api-request-run': 'runApiRequest',
        'submit form': 'loadApiRequest'
      },
      loadApiRequest: function(ev) {
        if (ev)
          ev.preventDefault();
        var data = $('input#api-request-input').val();
        if (data && _.isString(data) && data.trim().length > 0) {
          this.trigger('load-api-request', data);
        }
      },
      runApiRequest: function(ev) {
        if (ev)
          ev.preventDefault();
        this.trigger('run-api-request');
      },
      updateInputBox: function(data) {
        this.$el.find('input#api-request-input').val(data);
      },
      updateResultsText: function(data) {
        this.$el.find('#api-request-result').text(data);
      }

    });

    var WidgetController = Marionette.Controller.extend({

      initialize : function(apiRequest){
        if (!apiRequest || !(apiRequest instanceof ApiRequest)) {
          apiRequest = new ApiRequest(); // empty
        }
        this.collection = new KeyValueCollection(this.getData(apiRequest));
        this.view = new WidgetView({collection: this.collection, model: new KeyValue({initialValue: apiRequest.url()})});
        this.listenTo(this.view, 'all', this.onAll);
        return this;
      },


      render : function(){
        this.view.render()
        return this.view.el
      },


      onLoad : function(apiRequest) {
        if (this.collection) {
          this.collection.reset(this.getData(apiRequest))
        }
        else {
          this.initialize(apiRequest);
        }
      },


      /**
       * This is the central function - listening to all events
       * in this widget's views; and manipulating the models
       * that back views
       */
      onAll: function() {
        //console.log('onAll', arguments[0]);
        var event = arguments[0];

        if (event == 'childview:value-changed') {
          arguments[2].set('value', arguments[3]);;
        }
        else if (event == 'load-api-request') {
          this.onLoad(arguments[1]);
        }
        else if (event == 'run-api-request') {
          // we need to turn serilialize request into real request
          var req = new ApiRequest();
          _.map(this.collection.models, function(a) {
            var attr = a.attributes;
            if (attr.key && attr.value) {
              if (attr.key == 'query') {
                req.set('query', new ApiQuery().load(attr.value));
              }
              else if(attr.key == 'sender') {
                req.set(attr.key, attr.value.split('|'));
              }
              else {
                req.set(attr.key, attr.value);
              }
            }
          });
          this.view.updateInputBox(req.url()); // update the input box
          this.view.updateResultsText(req.url()); // update the input box
          this.onRun(req);
        }
      },

      /**
       * Function to massage input values and return what we want to
       * pass to the model
       *
       * @param {ApiRequest|String}
       * @returns {*|Array}
       */
      getData: function(apiRequest) {
        if (!apiRequest) {
          throw Error("Wrong input!");
        }
        if (_.isString(apiRequest)) {
          apiRequest = new ApiRequest().load(apiRequest);
        }

        var models = [];
        var vals = {target: 'search', query: '', sender: ''};
        _.each(_.pairs(vals), function(element, index, list) {
          var k = element[0];
          var o = {key: k, value: v};
          if (apiRequest.has(k) && apiRequest.get(k)) {
            var v = apiRequest.get(k);
            if (_.isArray(v)) {
              o.value = v.join('|');
            }
            else {
              o.value = v.url ? v.url() : v;
            }
          }
          models.push(o);
        });

        return models;
      },

      /**
       * The methods below are only working if you activate the widget and
       * pass it BeeHive
       *
       * @param beehive
       */
      activate: function(beehive) {
        var pubsub = beehive.Services.get('PubSub');
        pubsub.subscribe('all', _.bind(this.onAllPubSub, this));
        this.pubsub = pubsub;
      },

      /**
       * Catches and displays ApiRequest that has travelled through the
       * PubSub queue
       */
      onAllPubSub: function() {
        var event = arguments[0];
        if (event == PubSubEvents.DELIVERING_REQUEST) {
          console.log('[debug:ApiRequestWidget]', arguments[0]);
          this.onLoad(arguments[1]);
          this.view.updateResultsText(arguments[1].url());
        }
        else if (event == PubSubEvents.INVITING_REQUEST) {
          console.log('[debug:ApiRequestWidget]', arguments[0]);
          var r = new ApiRequest({query: arguments[1], target: 'search'});
          this.onLoad(r);
          this.view.updateResultsText(r.url());
        }
      },

      /**
       * Called by the UI View when 'Run' is clicked; you can override
       * the method to provide your own impl
       *
       * @param model
       */
      onRun: function(apiRequest) {
        if (this.pubsub) {
          this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, apiRequest);
        }
      }

    });

    return WidgetController;
  });


