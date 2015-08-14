
define([
    'underscore',
    'jquery',
    'js/components/alerts_mediator',
    'js/components/api_feedback',
    'js/widgets/widget_states',
    'js/components/alerts',
    'js/components/api_response',
    'analytics'
  ],

  function (
    _,
    $,
    AlertsMediator,
    ApiFeedback,
    WidgetStates,
    Alerts,
    ApiResponse,
    analytics
    ) {


    var Mediator = AlertsMediator.extend({

      onAlert: function(apiFeedback, psk) {

        var widgetInfo = this.getApp().getPluginOrWidgetName(psk.getId());
        if (apiFeedback.code == ApiFeedback.CODES.ALERT) {

          // XXX: temporary solution, the alerts are not working properly when
          // the page is fully extended, so we contract it first
          if (widgetInfo && widgetInfo.toLowerCase().indexOf('metrics') > -1) {
            //this.publishFeedback({code: ApiFeedback.CODES.UNMAKE_SPACE});
          }
        }
        console.log(apiFeedback);

        AlertsMediator.prototype.onAlert.call(this, arguments);
      },

      publishFeedback: function(data) {
        var pubsub = this.getPubSub();
        pubsub.publish(pubsub.FEEDBACK, new ApiFeedback(data));
      }


    });
    return Mediator;

  });
