/**
 * Created by rchyla on 3/19/14.
 *
 * Simple widget for debugging API response; it allows to copy&paste
 * JSON input directly into the text area and have it loaded.
 *
 * It also listens to DELIVERING_RESPONSE event
 */

define([
    'underscore',
    'jquery',
    'backbone',
    'marionette',
    'js/components/api_response',
    'hbs!./templates/widget-view',
    'js/components/pubsub_events',
    'js/mixins/dependon'
  ],

  function(
    _,
    $,
    Backbone,
    Marionette,
    ApiResponse,
    WidgetTemplate,
    PubSubEvents,
    Dependon
    ){

    var Model = Backbone.Model.extend({ });

    var WidgetView = Marionette.ItemView.extend({
      template : WidgetTemplate,
      events: {
        'click button#api-response-load': '_load',
        'click button#api-response-run': '_run',
        'submit form': '_load',
        'blur textarea#api-response-input': '_onChange'
      },

      // this is alternative way to trigger re-rendering
      // upon model change
      //modelEvents: {
      //  'change': 'fieldsChanged'
      //},
      //fieldsChanged: function() {
      //  this.render();
      //},

      _onChange: function() {
        this._changed = this.model.input != $('textarea#api-response-input').val();
      },
      _load: function(ev) {
        ev.preventDefault();
        var data = this.$el.find('textarea#api-response-input').val();
        this.options.controller.triggerMethod('load', data);
      },
      _run: function(ev) {
        ev.preventDefault();
        if (this._changed) {
          this._load(ev);
        }
        this.options.controller.triggerMethod('run', this.model.attributes);
      },
      _error: function(msg) {
        this.$el.find('#api-response-error').empty();
        this.$el.find('#api-response-error').append(msg);
      }
    });

    var WidgetController = Marionette.Controller.extend({


      _getModel: function(data) {
        var model = null;
        if (_.isString(data)) {
          var r = new ApiResponse(JSON.parse(data));
          model = new Model({key: r.url(), input: data, result: 'new ApiResponse(' + JSON.stringify(r.toJSON()) + ')', R: r});
        }
        else if (data && _.isObject(data) && 'url' in data) {
          model = new Model({key: data.url(), input: JSON.stringify(data.toJSON()), result: 'new ApiResponse(' + JSON.stringify(data.toJSON()) + ')', R: data});
        }
        else {
          model = new Model({key: '', input: '', result: '{}', R: {}});
        }

        return model;
      },

      initialize : function(data){
        this.model = this._getModel(data);
        this.view = new WidgetView({model: this.model, controller: this});
        return this;
      },

      render : function(){
        this.view.render();
        return this.view.el
      },

      onLoad : function(data) {
        try {
          var m = this._getModel(data);
          this.model.set(m.attributes);
          this.view.render();
          this.view._error('');
        }
        catch(e) {
          if (this.view) {
            console.error(e.message, data);
            this.view._error(e.message);
          }
          throw e;
        }
      },


      /**
       * The methods below are only working if you activate the widget and
       * pass it BeeHive
       *
       * @param beehive
       */
      activate: function(beehive) {
        this.setBeeHive(beehive);
        var pubsub = this.getPubSub();
        pubsub.subscribe('all', _.bind(this.onAllPubSub, this));
      },

      /**
       * Catches and displays ApiResponse that has travelled through the
       * PubSub queue
       */
      onAllPubSub: function() {
        var event = arguments[0];
        if (event.indexOf(PubSubEvents.DELIVERING_RESPONSE) > -1) {
          console.log('[debug:ApiResponseWidget]', arguments[0]);
          this.onLoad(arguments[1]);
        }
      },

      /**
       * Called by the UI View when 'Run' is clicked; you can override
       * the method to provide your own impl
       *
       * @param model
       */
      onRun: function(model) {
        if (this.hasPubSub()) {
          this.getPubSub().publish(this.getPubSub().DELIVERING_RESPONSE, model.R);
        }
      }

    });

    _.extend(WidgetController.prototype, Dependon.BeeHive);

    return WidgetController;
  });


