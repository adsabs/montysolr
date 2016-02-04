/**
 * A widget for the internal ADS Deployment
 */

define([
    'underscore',
    'jquery',
    'backbone',
    'marionette',

    'js/components/api_query',
    'js/components/api_request',
    'js/components/api_response',

    'js/widgets/base/base_widget',
    'hbs!./templates/layout',
    'hbs!./templates/item-view'
  ],
  function(
    _,
    $,
    Backbone,
    Marionette,
    ApiQuery,
    ApiRequest,
    ApiResponse,
    BaseWidget,
    WidgetTemplate,
    ItemTemplate
    ){


    var Model = Backbone.Model.extend({
      defaults : {
        msg: undefined
      }
    });


    /**
     * View is 'bound' to the model, so everytime the model changes, the View will
     * update itself (ie. redraw)
     *
     * The idea is simple: your controller will update the model, and the view
     * will then show the model. Your controller will not be updating the view
     * directly. But it can ask the view to provide some information.
     *
     */
    var View = Marionette.ItemView.extend({

      tagName : "div", // this view will create <div class="s-hello">....</div> html node
      className : "s-service",
      template: WidgetTemplate,
      events: {
        'click button': 'onButtonClick'
      },
      modelEvents: {
        "change" : 'render'
      },

      onButtonClick: function(ev) {
        if (ev) {
          ev.stopPropagation();
        }
        // send the data back to the controller, which will decide what to do with it
        this.trigger('service-action', this.$el.find('input').val());
      }

    });


    var Controller = BaseWidget.extend({

      viewEvents: {
         'service-action': 'onServiceAction'
      },

      initialize : function(options){
        this.model = new Model();
        this.view = new View({model : this.model});
        BaseWidget.prototype.initialize.apply(this, arguments);
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        var pubsub = beehive.getService('PubSub');
        pubsub.subscribe(pubsub.INVITING_REQUEST, _.bind(this.onRequest, this));
        pubsub.subscribe(pubsub.DELIVERING_RESPONSE, _.bind(this.onResponse, this));
      },

      onRequest: function(apiQuery) {
        if (!(apiQuery instanceof ApiQuery) || !apiQuery.has('q'))
          throw new Error('You are kidding me!');

        q.set('foo', this.model.get('name') || 'world');

        this.dispatchRequest(q); // calling out parent's method
      },

      // triggered externally, by a query-mediator, when it receives data for our query
      onResponse: function(apiResponse) {
        if (apiResponse.has('response.numFound')) {
          this.model.set('msg', 'The query found: ' + apiResponse.get('response.numFound') + ' results.');
        }
      },

      onServiceAction: function(name) {
        if (name.toLowerCase() == 'bumblebee') {
          this.model.set('name', 'wonderful ' + name);
        }
        else {
          this.model.set('name', name);
        }
      }
    });


    return Controller;
  });