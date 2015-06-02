
define([
    'underscore',
    'jquery',
    'js/widgets/metrics/widget',
    'js/widgets/base/base_widget',
    'js/components/api_query',
    'js/components/json_response'
  ],

  function (
    _,
    $,
    MetricsWidget,
    BaseWidget,
    ApiQuery,
    JsonResponse
    ) {

    var Widget = BaseWidget.extend({
      initialize : function(options) {
        // other widgets can send us data through page manager
        this.on('page-manager-message', function(event, data){
          if (event === "broadcast-payload"){
            this.ingestBroadcastedPayload(data);
          }
        });
        BaseWidget.prototype.initialize.call(this, options);
        this.innerWidget = new MetricsWidget();
        this.view = this.innerWidget.view;
      },

      activate: function(beehive) {
        _.bindAll(this, "setCurrentQuery", "processResponse");
        this.pubsub = beehive.Services.get('PubSub');
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
        this.innerWidget.pubsub = this.pubsub;
      },

      ingestBroadcastedPayload: function(data) {
        if (data.bibcode) {
          var q = new ApiQuery({'q': 'bibcode:' + data.bibcode});
          this.innerWidget.setCurrentQuery(q);
          this.innerWidget.onShow();
        }
      },

      render: function() {
        var ret = this.innerWidget.render();
        this.el = ret.el;
        return ret;
      },

      processResponse: function(apiResponse) {

        if (apiResponse instanceof JsonResponse) {
          // decide if it is worth showing it
          var stats = _.values(apiResponse.attributes["all stats"]);
          var reads = _.values(apiResponse.attributes["all reads"]);
          if (!(stats && reads)) return;
          var f = function(x) {return x > 2};
          if (!(_.filter(_.values(stats), f).length || _.filter(_.values(reads), f).length)) return;
          this._response = apiResponse;
          this.trigger('page-manager-event', 'widget-ready',
            {numFound: 1});
        }
        else {
          this.innerWidget.processResponse(apiResponse);
        }
      },

      onShow: function() {
        this.innerWidget.resetWidget();
        this.innerWidget.processResponse(this._response);
        this.innerWidget.childViews.papersGraphView.destroy();
      }

    });

    return Widget;

  });
