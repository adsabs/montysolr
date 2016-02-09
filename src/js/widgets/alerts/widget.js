/**
 * This widget is capable of displaying messages to the user, however it does not
 * listen on the PubSub. It is invoked/controlled by alerts_mediator. That is the
 * controller which decides what to do.
 */

define([
      'marionette',
      'js/widgets/base/base_widget',
      'js/components/api_query',
      'js/components/api_feedback',
    //list of possible alerts
      'js/components/alerts',
      './modal_view',
      './page_top_alert',
      'jquery',
      'jquery-ui',
      'bootstrap'

    ],
    function (
              Marionette,
              BaseWidget,
              ApiQuery,
              Alerts,
              ApiFeedback,
              ModalView,
              BannerView,
              $,
              $ui,
              bootstrap
    ) {


      var AlertModel = Backbone.Model.extend({
        defaults: {
          type: 'info',
          msg: undefined,
          title: undefined,
          events: undefined,
          modal: false
        }
      });

      /*
      * function called in onRender method of all alert views
      * this allows for custom events to be added to the alert views
      * when the events happen, a promise is resolved with the name of the event
      * NOTE: this assumes there is only 1 event per alert view
      * */

        function delegateAdditionalEvents() {
          var self = this;
          var events = this.model.get('events');
          var delegateEventSplitter = /^(\S+)\s*(.*)$/;

          //first, remove previous events
          if (this.$el) {
            this.$el.off('.customEvents' + this.mid);
          }

          // attach functions to events; copied from backbone
          // when 'event' is fired, it will call/resolve the
          // promise object with the name of the event
          if (events) {

            _.each(events, function (evtValue, evt) {

              var match = evt.match(delegateEventSplitter);
              var eventName = match[1], selector = match[2];
              var key = evt;

              //create an event listener that resolves the promise
              //with the supplied data when there is the proper event
              var method = function (ev) {
                if (ev) {
                  ev.preventDefault();
                  ev.stopPropagation();
                }
                var promise = this.model.get('promise');
                var evts = this.model.get('events');
                if (evts[key]){
                  promise.resolve(evts[key]);
                }
                else {
                  promise.resolve(key);
                }
                // unless it is modal, close it automatically
                if (!this.model.get('modal')) {
                  this.model.set('msg', null);
                }
              };

              method = _.bind(method, self);
              eventName += '.customEvents' + this.mid;
              if (selector === '') {
                self.$el.on(eventName, method);
              } else {
                self.$el.on(eventName, selector, method);
              }
            });
          }
      };

      var modalOnRender = ModalView.prototype.onRender;

      ModalView.prototype.onRender = function(){
        delegateAdditionalEvents.apply(this, arguments);
        if (modalOnRender) modalOnRender.apply(this, arguments);
      };

      var bannerOnRender = BannerView.prototype.onRender;

      BannerView.prototype.onRender = function(){
        delegateAdditionalEvents.apply(this, arguments);
        if (bannerOnRender) bannerOnRender.apply(this, arguments);
      };


      var AlertsWidget = BaseWidget.extend({

        initialize: function (options) {
          this.model = new AlertModel();
          this.view = new BannerView({model: this.model});
          this.modalView = new ModalView({model: this.model});
          BaseWidget.prototype.initialize.apply(this, arguments);

        },

        activate: function (beehive) {
          this.setBeeHive(beehive);
          //listen to navigate event and close widget
          var pubsub = this.getPubSub();
          pubsub.subscribe(pubsub.NAVIGATE, this.modalView.closeModal);
        },

        alert: function (feedback) {
          var promise = $.Deferred();
          this.model.set({
            msg: feedback.msg,
            events: feedback.events,
            title: feedback.title,
            type: feedback.type || "info",
            modal: feedback.modal,
            promise: promise
          });
          return promise.promise();
        }

      });

      return AlertsWidget;
    });