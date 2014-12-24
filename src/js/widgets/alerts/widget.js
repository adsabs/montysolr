

define([
    'js/widgets/base/base_widget',
    'js/components/api_query',
    'hbs!./templates/alerts_template',
    'marionette',
    'js/components/api_feedback',
    'jquery',
    'jquery-ui'

  ],
  function(
    BaseWidget,
    ApiQuery,
    WidgetTemplate,
    Marionette,
    ApiFeedback,
    $,
    $ui
    ){


    var AlertsModel = Backbone.Model.extend({
      defaults : {
        type: 'info',
        msg: undefined,
        events: undefined
      }
    });


    var delegateEventSplitter = /^(\S+)\s*(.*)$/;
    
    var AlertsView = Marionette.ItemView.extend({

      tagName : "span",
      className : "s-alerts",

      initialize: function () {
      },

      template: WidgetTemplate,

      events: {
        'click #alertBox button.close': 'close'
      },

      modelEvents: {
        "change": 'render'
      },

      close: function() {
        this.model.set('msg', null);
      },

      render: function() {
        // this seems to be necessary (at least when the actions happen too fast ... like in unittests)
        if (this.$el && this.$el.find('#alertModal').length) {
          this.$el.find('#alertModal').modal('hideModal');
        }
        this.stopListening(this.$el);
        Marionette.ItemView.prototype.render.apply(this, arguments);
      },

      onRender: function() {
        var self = this;
        var events = this.model.get('events');


        if (events) {
          var bindings = {};
          _.each(events, function(data, evt) {

            var match = key.match(delegateEventSplitter);
            var eventName = match[1], selector = match[2];
            method = _.bind(method, this);
            eventName += '.delegateEvents' + this.cid;
            if (selector === '') {
              this.$el.on(eventName, method);
            } else {
              this.$el.on(eventName, selector, method);
            }

            var prescription = {key: evt, def: data};
            self.on(evt, function(ev) {
              console.log('got it', this);
            }, {view: self, key: evt, data: data});
          });
        }

      }

    });


    var AlertsWidget = BaseWidget.extend({

      initialize : function(options){
        this.model = new AlertsModel();
        this.view = new AlertsView({model : this.model});
        BaseWidget.prototype.initialize.apply(this, arguments);
      },

      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');
        this.pubsub.subscribe(this.pubsub.ALERT, _.bind(this.alert, this));
      },

      alert: function(feedback) {
        switch (feedback.code) {
          case ApiFeedback.CODES.ALERT:
            this.model.set('events', feedback.events);
            this.model.set('msg', feedback.msg);
            break;
        }
      }

    });


    return AlertsWidget;
  });