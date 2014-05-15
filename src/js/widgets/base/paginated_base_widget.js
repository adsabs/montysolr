/**
 * Created by alex on 5/8/14.
 */

define(['backbone', 'marionette', 'js/widgets/base/base_widget',
  'js/components/api_query', 'js/components/api_request', 'js/components/paginator'],
  function (Backbone, Marionette, BaseWidget, ApiQuery, ApiRequest, Paginator) {

  var paginatedBaseWidget = BaseWidget.extend({

    initialize    : function (options) {

      // XXX:rca - if 'unpaginated' then the widget will break, because it expects
      // this.paginator to be there
      if (!options.unpaginated) {
        this.paginator = new Paginator({"start": options.start, "rows": options.rows,
          "startName": options.startName, "rowsName": options.rowsName})
      }

      BaseWidget.prototype.initialize.call(this, options)
    },

    //overrides base composeRequest to add rows and start info to the apiquery
    composeRequest: function (params) {

      /*
      XXX:rca - refactored slightly, this shows one problem though; this function
         should only need to override the function that changes query, but because
         they are married under composeRequest, it is not easy. This tells us that
         there should be two methods:

          1. the one that changes the query: ie. changeQuery()
          2. the one that builds request: ie. composeRequest()
       */
      var req = BaseWidget.prototype.composeRequest.apply(this, arguments);
      if (!req)
        return;

      var q = req.get('query');
      var paginationVars = this.paginator.run();

      _.each(paginationVars, function(v, k){
        q.set(k, v)
      });

      return new ApiRequest({
        target: 'search',
        query : q
      });
    },

    dispatchRequest: function (apiQuery) {
      this.paginator.reset(); // XXX:rca - why is resetting for every request?
      BaseWidget.prototype.dispatchRequest.apply(this, arguments);

    },

    /*This is a function your widget can use to request more info. So you might do something like
     * MyController.listenTo(MyController.View, "moreInfoRequested", "dispatchFollowUpRequest"
     *
     * XXX:rca - in essence, this function just duplicates dispatchRequest, so it is unnecessary;
     *   especially if setCurrentQuery() were not called inside dispatchRequest()
     *         - pls remove it
     * */
    dispatchFollowUpRequest: function () {

      var fieldsAndPag = _.extend(this.defaultFields, this.paginator.run())

      var req = this.composeRequest(fieldsAndPag);
      if (req) {

        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
        console.log("follow up request", req, fieldsAndPag)
      }
    }

  })

  return paginatedBaseWidget

})

