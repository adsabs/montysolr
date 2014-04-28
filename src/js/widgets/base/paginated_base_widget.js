/**
 * Created by alex on 5/8/14.
 */

define(['backbone', 'marionette', 'js/widgets/base/base_widget',
  'js/components/api_query', 'js/components/api_request', 'js/components/paginator'],
  function (Backbone, Marionette, BaseWidget, ApiQuery, ApiRequest, Paginator) {

  var paginatedBaseWidget = BaseWidget.extend({

    initialize    : function (options) {
      if (!options.unpaginated) {
        this.paginator = new Paginator({"start": options.start, "rows": options.rows,
          "startName": options.startName, "rowsName": options.rowsName})
      }

      BaseWidget.prototype.initialize.call(this, options)
    },

    //overrides base composeRequest to add rows and start info to the apiquery
    composeRequest: function (params) {
      if (params) {
        var q = this.composeQuery(params);
      }
      else if (this.defaultFields) {
        var q = this.composeQuery(this.defaultFields);
      }
      else {
        var q = this.composeQuery();
      }

      var paginationVars = this.paginator.run();

      _.each(paginationVars, function(v, k){
        q.set(k, v)
      })

      return new ApiRequest({
        target: 'search',
        query : q
      });
    },

    dispatchRequest        : function (apiQuery) {
      this.paginator.reset();

      this.setCurrentQuery(apiQuery);

      var req = this.composeRequest();
      if (req) {
        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
      }
    },

    /*This is a function your widget can use to request more info. So you might do something like
     * MyController.listenTo(MyController.View, "moreInfoRequested", "dispatchFollowUpRequest"*/
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

