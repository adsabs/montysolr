/**
 * Created by rchyla on 3/19/14.
 */

define(['underscore', 'jquery', 'backbone', 'marionette',
  'js/components/api_query',
  'js/components/pubsub_events',
  'hbs!./templates/widget-view',
  'hbs!./templates/item-view'

],

  function(_, $, Backbone, Marionette,
           ApiQuery, PubSubEvents,
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
        'click .remove' : 'removeItem',
        'blur input[name=key]': 'onChange',
        'blur input[name=value]': 'onChange'
      },
      onChange: function(ev) {
        var container = $(ev.target);
        var attr = _.clone(this.model.attributes);
        var newVal = $(ev.target).val();

        if (container.attr('name') == 'key' && attr.key != newVal) {
          this.trigger('key-changed', this.model, newVal);
        }
        else if (container.attr('name') == 'value' && attr.value != newVal) {
          this.trigger('value-changed', this.model, newVal);
        }
      },
      removeItem : function(ev){
        ev.preventDefault();
        this.trigger('remove-clicked', this.model);
      }
    });

    var WidgetView = Marionette.CompositeView.extend({
      template : WidgetTemplate,
      itemView : ItemView,
      itemViewContainer: "#api-query-values",
      events: {
        'click button#api-query-load': 'loadApiQuery',
        'click button#api-query-add': 'addNewItem',
        'click button#api-query-run': 'runApiQuery',
        'submit form': 'loadApiQuery'
      },
      addNewItem: function(ev) {
        if (ev)
          ev.preventDefault();
        this.trigger('add-new-item', this);
      },
      loadApiQuery: function(ev) {
        if (ev)
          ev.preventDefault();
        var data = this.$el.find('input#api-query-input').val();
        if (data && _.isString(data) && data.trim().length > 0) {
          this.trigger('load-api-query', data);
        }
      },
      runApiQuery: function(ev) {
        if (ev)
          ev.preventDefault();
        var q = new ApiQuery();
        _.map(this.collection.models, function(a) {
          var attr = a.attributes;
          if (attr.key) {
            q.set(attr.key, attr.value.split('|'));
          }
        });
        this.$el.find('#api-query-result').text(q.url());
        this.trigger('run-api-query', q);
      },
      updateInputBox: function(data) {
        this.$el.find('input#api-query-input').val(data);
      },
      removeKey: function(data) {
        this.trigger('remove-key', data);
      }

    });

    var WidgetController = Marionette.Controller.extend({

      initialize : function(apiQuery){
        if (!apiQuery || !(apiQuery instanceof ApiQuery)) {
          apiQuery = new ApiQuery(); // empty
        }
        this.collection = new KeyValueCollection(this.getData(apiQuery));
        this.view = new WidgetView({collection: this.collection, model: new KeyValue({initialValue: apiQuery.url() || 'q=planet'})});
        this.listenTo(this.view, 'all', this.onAll);
        return this;
      },


      render : function(){
        this.view.render()
        return this.view;
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
      onAll: function() {
        //console.log('onAll', arguments[0]);
        var event = arguments[0];

        if (event == 'itemview:remove-clicked') {
          arguments[2].destroy();
        }
        else if (event == 'itemview:key-changed') {
          arguments[2].set('key', arguments[3]);;
        }
        else if (event == 'itemview:value-changed') {
          arguments[2].set('value', arguments[3]);;
        }
        else if (event == 'add-new-item') {
          arguments[1].collection.add({key:'', value:''});
        }
        else if (event == 'load-api-query') {
          this.onLoad(arguments[1]);
        }
        else if (event == 'run-api-query') {
          this.view.updateInputBox(arguments[1].url()); // update the input box
          this.onRun(arguments[1]);
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
       * Catches and displays ApiQuery that has travelled through the
       * PubSub queue
       */
      onAllPubSub: function() {
        var event = arguments[0];
        if (event == PubSubEvents.NEW_QUERY || event == PubSubEvents.INVITING_REQUEST) {
          console.log('[debug:ApiQueryWidget]', arguments[0]);
          //this.onLoad(arguments[1]);
          this.view.updateInputBox(arguments[1].url()); // update the input
        }
      },

      /**
       * Called by the UI View when 'Run' is clicked; you can override
       * the method to provide your own impl
       *
       * @param ApiQuery
       */
      onRun: function(apiQuery) {
        if (this.pubsub) {
          this.pubsub.publish(this.pubsub.NEW_QUERY, apiQuery);
        }
      }

    });

    return WidgetController;
  });


