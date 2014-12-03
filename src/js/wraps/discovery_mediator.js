
define([
    'underscore',
    'jquery',
    'js/components/feedback_mediator',
    'js/components/api_feedback'
  ],

  function (
    _,
    $,
    FeedbackMediator,
    ApiFeedback
    ) {

    var searchStarted = function() {};

    var makeSpace = function() {

      if (this.app) {
        var mpm = this.app.getObject('MasterPageManager');
        if (mpm) {
          var child = mpm.getCurrentActiveChild();
          if (child.view && child.view.showCols) {
            child.view.showCols({left: true, right: false});
            // open the view again
            this.pubsub.once(this.pubsub.NAVIGATE + ' ' + this.pubsub.START_SEARCH, function() {child.view.showCols({right:true})});
          }
        }
      }

      //var $btn = $('button.btn-expand.right-expand');
      //if ($btn.hasClass('pull-right')) {
      //  $btn.click();
      //}
    };


    return function() {
      var mediator = new FeedbackMediator();
      mediator.addFeedbackHandler(ApiFeedback.CODES.MAKE_SPACE, makeSpace);
      // TODO - register state (to undo the action)

      return mediator;
    }

  });
