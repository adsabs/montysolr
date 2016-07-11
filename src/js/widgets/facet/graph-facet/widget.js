define([
      'js/components/api_query',
      'js/components/api_request',
      'js/widgets/base/base_widget',
      'js/components/api_query_updater'
    ],
    function (
        ApiQuery,
        ApiRequest,
        BaseWidget,
        ApiQueryUpdater
    ) {

      var BaseFacetWidget = BaseWidget.extend({

        initialize: function (options) {
          options = options || {};
          this.processResponse = options.processResponse;
          this.model = new Backbone.Model();
          this.view = new options.graphView( _.extend(
              options.graphViewOptions,
              { model : this.model }
          ));

          this.listenTo(this.view, "all", this.onAllInternalEvents);
          this.facetField = options.facetField ;
          this.queryUpdater = new ApiQueryUpdater(this.facetField);
          BaseWidget.prototype.initialize.apply(this, arguments);
          this.listenTo(this.view, "facet-applied", this.handleConditionApplied);
        },

        activate: function (beehive) {
          this.setBeeHive(beehive);
          _.bindAll(this, 'dispatchRequest', 'processResponse');
          //custom dispatchRequest function goes here
          this.getPubSub().subscribe(this.getPubSub().INVITING_REQUEST, this.dispatchRequest);
          this.getPubSub().subscribe(this.getPubSub().DELIVERING_RESPONSE, this.processResponse);
        },

        dispatchRequest: function (apiQuery) {
          //reset the graph
          this.model.unset('graphData');
          var q = this.customizeQuery(apiQuery);
          var req = this.composeRequest(q);
          this.getPubSub().publish(this.getPubSub().DELIVERING_REQUEST, req);
        },

        handleConditionApplied: function(val) {
          var q = this.getCurrentQuery();
          val = this.facetField + ":" + val;
          q = q.clone();
          var fieldName = 'q';
          this.queryUpdater.updateQuery(q, fieldName, 'limit', val);
          this.dispatchNewQuery(q);
        }

      });

      return BaseFacetWidget

    });
