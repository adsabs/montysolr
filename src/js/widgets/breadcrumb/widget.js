/**
 * Created by rchyla on 5/14/14.
 *
 * This is a simple widget which just displays the history of the search (as it was progressing)
 * ie.
 *
 *   (45.000) >> (345) >> (34) >> 0(x)
 *
 * And allows only to remove items from right (not in the middle) - or if you click on the item,
 * it will re-execute the search and remove all the items to the right
 *
 * TODO: it needs to react on browser-back/forward
 */

define([
    'underscore',
    'jquery',
    'backbone',
    'marionette',
    'js/components/api_query',
    'js/components/pubsub_events',
    'hbs!./templates/widget-view',
    'hbs!./templates/item-view'
  ],

  function(
    _,
    $,
    Backbone,
    Marionette,
    ApiQuery,
    PubSubEvents,
    WidgetTemplate,
    ItemTemplate
    ){

    // Model
    var KeyValue = Backbone.Model.extend({ });

    // Collection of data
    var KeyValueCollection = Backbone.Collection.extend({
      model : KeyValue
    });

    var ItemView = Marionette.ItemView.extend({
      tagName: 'span',
      template : ItemTemplate,
      events : {
        'click .remove' : 'onRemove',
        'click' : 'onClick'
      },
      onClick: function(ev) {
        ev.preventDefault();
        this.trigger('item-click', this.model);
      },
      onRemove : function(ev){
        ev.preventDefault();
        this.trigger('item-remove', this.model);
      }
    });

    var WidgetView = Marionette.CompositeView.extend({
      template : WidgetTemplate,
      childView : ItemView,
      childViewContainer: "#simple-breadcrumb",
      events: {
      }

    });

    var WidgetController = Marionette.Controller.extend({

      initialize : function(apiQuery){
        if (!apiQuery || !(apiQuery instanceof ApiQuery)) {
          apiQuery = new ApiQuery(); // empty
        }
        this.collection = new KeyValueCollection(this.getData(apiQuery));
        this.view = new WidgetView({collection: this.collection});
        this.listenTo(this.view, 'all', this.onAllInternalEvents);
        this.listening = false;
        return this;
      },

      activate: function(beehive) {
        _.bindAll(this, "onNewQuery", "onRequest", "onResponse", "onAllPubSub");
        var pubsub = beehive.Services.get('PubSub');
        pubsub.subscribe(pubsub.START_SEARCH, this.onNewQuery);
        pubsub.subscribe(pubsub.DELIVERING_REQUEST, this.onRequest);
        //pubsub.subscribe('all', this.onAllPubSub);
        this.pubsub = pubsub;
        this.stack = [];
        this.N = 0;
        this.maxSize = 100; // make configurable?
      },

      render : function(){
        this.view.render();
        return this.view.el
      },

      onNewQuery: function(apiQuery, key) {
        console.log('START_SEARCH', apiQuery.url(), key);
        this.listening = true;
      },

      onRequest: function(apiRequest, key) {
        if (!this.listening)
          return;

        var q = apiRequest.get('query');
        if (q) {
          console.log('NEW REQUEST', q);
          // remember who initiated the new-query
          pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE+key.getId(), this.onResponse);
          this.listening = false;
        }
      },

      onResponse: function(apiResponse) {
        console.log('NEW RESPONSE', apiResponse);
      },


      /**
       * Catches and displays ApiQuery that has travelled through the
       * PubSub queue
       */
      onAllPubSub: function() {
        var event = arguments[0];
        if (event == this.eventKey) {
          console.log('[debug:ApiQueryWidget]', arguments[0]);
          //this.onLoad(arguments[1]);
          this.view.updateInputBox(arguments[1].url()); // update the input
        }
      },


      onLoad : function(apiQuery) {
        if (this.collection) {
          this.collection.reset(this.getData(apiQuery))
        }
        else {
          this.initialize(apiQuery);
        }
      },


      /**
       * This is the central function - listening to all events
       * in this widget's views; and manipulating the models
       * that back views
       */
      onAllInternalEvents: function() {
        //console.log('onAll', arguments[0]);
        var event = arguments[0];

        if (event == 'childview:item-click') {
          // TODO: destroy all models after this one and issue new query with this request
          arguments[2].destroy();
        }
        else if (event == 'childview:item-remove') {
          arguments[2].destroy();
        }
      },

      /**
       * Function to massage input values and return what we want to
       * pass to the model
       *
       * @param apiQuery
       * @returns {*|Array}
       */
      getData: function(apiQuery) {
        if (!apiQuery) {
          throw Error("Wrong input!");
        }
        if (_.isString(apiQuery)) {
          apiQuery = new ApiQuery().load(apiQuery);
        }

        var pairs = _.map(apiQuery.pairs(), function(pair){
          return {key: pair[0], value: pair[1].join('|')};
        });
        return pairs;
      }


    });

    return WidgetController;
  });


