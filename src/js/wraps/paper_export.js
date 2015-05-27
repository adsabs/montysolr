
define([

    'js/widgets/export/widget',

    'js/components/api_query',
    'js/components/json_response'
  ],

  function (
    ExportWidget,
    ApiQuery,
    JsonResponse
    ) {

    var Widget = ExportWidget.extend({

      initialize : function(options) {
       // other widgets can send us data through page manager
        this.on('page-manager-message', function(event, data){
          if (event === "broadcast-payload"){
            this.ingestBroadcastedPayload(data);
          }
        });
        ExportWidget.prototype.initialize.call(this, options);
      },

      activate: function(beehive) {
        _.bindAll(this, "setCurrentQuery", "processResponse");
        this.pubsub = beehive.Services.get('PubSub');
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      ingestBroadcastedPayload: function(data) {
        if (data.bibcode) {
         this.bibcode = data.bibcode;
        }
      },

      setSubView : function(format){
        this.exportRecords(format, [this.bibcode]);
      },

      processResponse : function(){
      }

    });

    return Widget;

  });
