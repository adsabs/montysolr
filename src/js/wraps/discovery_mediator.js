
define([
    'underscore',
    'jquery',
    'js/components/feedback_mediator',
    'js/components/api_feedback',
    'js/widgets/widget_states'
  ],

  function (
    _,
    $,
    FeedbackMediator,
    ApiFeedback,
    WidgetStates
    ) {


    var handlers = {};

    handlers[ApiFeedback.CODES.MAKE_SPACE] = function(feedback) {
      var mpm = this.getApp().getObject('MasterPageManager');
      if (mpm) {
        var child = mpm.getCurrentActiveChild();
        if (child.view && child.view.showCols) {
          child.view.showCols({left: true, right: false});
          // open the view again
          this.pubsub.once(this.pubsub.NAVIGATE, function() {child.view.showCols({right:true})});
        }
      }
    };

    handlers[ApiFeedback.CODES.SEARCH_CYCLE_STARTED] = function(feedback) {
      // retrieve ids of all components that wait for a query
      var ids = _.keys(feedback.cycle.waiting);

      // turn ids into a list of widgets
      var widgets = this.getWidgets(ids);

      // activate loading state
      if (widgets) {
        this.changeWidgetsState(widgets, {state: WidgetStates.WAITING});
      }

    };


    handlers[ApiFeedback.CODES.SEARCH_CYCLE_FINISHED] = function(feedback) {
      // retrieve ids of all components that wait for a query
      var ids = _.keys(feedback.cycle.inprogress);

      // turn ids into a list of widgets
      var widgets = this.getWidgets(ids);

      // change widget state
      if (widgets) {
        this.changeWidgetsState(widgets, {state: WidgetStates.IDLE});
      }

    };

    return function() {
      var mediator = new FeedbackMediator();
      _.each(_.pairs(handlers), function(pair) {
        mediator.addFeedbackHandler(pair[0], pair[1]);
      });
      return mediator;
    }

  });
