/**
 * Created by alex on 5/8/14.
 */
define(['backbone', 'marionette', 'js/components/api_query', 'js/components/api_request',
  'js/widgets/multi_callback/multi_callback_widget', 'js/components/paginator'],
  function (Backbone, Marionette, ApiQuery, ApiRequest, MultiCallbackWidget, Paginator) {

  var PaginatedMultiCallbackWidget = MultiCallbackWidget.extend({

    initialize: function (options) {
      options = options || {};

      if (! options.unpaginated){


        this.paginator = new Paginator({"start" : Marionette.getOption(this, "start"),
          "rows" :  Marionette.getOption(this, "rows"),
          "startName": Marionette.getOption(this, "startName"),
          "rowsName": Marionette.getOption(this, "rowsName")})
      };

      console.log("new paginator", this.paginator, this)

      MultiCallbackWidget.prototype.initialize.call(this, options)
    },

    /*This function is called every time a request to pubsub is made. It keeps track
     * of the current start value and returns the rows: and start: value that you want to
     * put into your apiquery*/


    dispatchRequest : function (apiQuery) {

      var id, req, customQuery;
      this.setCurrentQuery(apiQuery);

      this.pagination.start = this.pagination.initialStart;

      var d = this.defaultFields();

      _.extend(d, this.paginator.run());
      customQuery = this.composeQuery(d)

      id = customQuery.url();

      //it's responding to INVITING_REQUEST, so just do default information request
      this.registerCallback(id, this.processResponse, {
        view: this.view
      })

      req = this.composeRequest(customQuery);
      if (req) {
        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
      }
    },

    /*This is a function your widget can use to request more info. So you might do something like
     * MyController.listenTo(MyController.View, "moreInfoRequested", "dispatchFollowUpRequest")*/
    dispatchFollowUpRequest: function () {

      var d = this.defaultFields;

      _.extend(d, this.paginator.run());

      customQuery = this.composeQuery(d)

      id = customQuery.url();

      //it's responding to INVITING_REQUEST, so just do default information request
      this.registerCallback(id, this.processResponse, {
        view: this.view
      })

      var req = this.composeRequest(d);
      if (req) {

        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
      }
    }

  })

  return PaginatedMultiCallbackWidget
});