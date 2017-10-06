
define([
      'js/widgets/metrics/widget',
      'js/components/api_feedback'
    ],

    function (
        MetricsWidget,
        ApiFeedback
    ) {

      var Widget = MetricsWidget.extend({

        initialize : function(options){

          this.on('page-manager-message', function(event, data){
            if (event === "broadcast-payload"){
              this.ingestBroadcastedPayload(data);
            }
          });
          MetricsWidget.prototype.initialize.apply(this, arguments);
        },

        ingestBroadcastedPayload: function(data) {
          var bibcode = data.bibcode;
          var self = this;
          this.containerModel.set("title", data.title);
          this.getMetrics([bibcode]).done(function() {

            // Everything worked, show the widget
            self.trigger('page-manager-event', 'widget-ready', {isActive: true, widget : self});
            if (self._waiting) {
              self.onShow();
              self._waiting = false;
            }
          }).fail(function () {

            // if the metrics fail, kill it
            self.trigger('page-manager-event', 'widget-ready', {
              isActive: false,
              widget : self
            });
            self.destroy();
          });
        },


        onShow : function(){
          var response = this.containerModel.get("data");
          if (!response) {
            this._waiting = true;
            return
          }
          this.createTableViews(response, 1);
          this.createGraphViewsForOnePaper(response);
          this.insertViewsForOnePaper(response);
        }


      });

      return Widget;

    });
