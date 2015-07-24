
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

      activate : function(beehive){
        this.setBeeHive(beehive);
      _.bindAll(this, "setCurrentQuery", "processResponse");
      var pubsub = beehive.getService("PubSub");
      //this will have to be changed later
      pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);
    },

    ingestBroadcastedPayload: function(data) {
        var bibcode = data.bibcode;
        var self = this;
        this.containerModel.set("title", data.title);
        this.getMetrics([bibcode]).done(function(data) {
          //check to see that there is at least 1 read/citation
          if (self.hasReads(data) || self.hasCitations(data)){
            self.trigger('page-manager-event', 'widget-ready', {isActive: true, widget : self});
          }
          });
      },

      hasCitations : function(data){
        return data["citation stats"]["total number of citations"] > 0;
      },

      hasReads : function(data){
         return  data["basic stats"]["total number of reads"] > 0;
      },

      insertViews: function (data) {
        //render the container view
        this.view.render({title : this.containerModel.get("title")});
        this.view.$("#indices").hide();
        this.view.$("#papers").hide();
        this.view.$(".metrics-metadata").hide();

        //attach table views
        if (this.hasReads(data)){
          this.view.readsTable.show(this.childViews.readsTableView);
          this.view.readsGraph.show(this.childViews.readsGraphView);
        }
        else {
          this.view.$(this.view.readsTable.el).html("No reads found for this article.");
        }
        if (this.hasCitations(data)){
          this.view.citationsTable.show(this.childViews.citationsTableView);
          this.view.citationsGraph.show(this.childViews.citationsGraphView);
        }
        else {
          this.view.$(this.view.citationsTable.el).html("No citations found for this article.");
        }
        //some table rows need to be hidden
        this.view.$(".hidden-abstract-page").hide();
      },

      processMetrics : function (response){
        //how is the json response formed? need to figure out why attributes is there
        response = response.attributes ? response.attributes : response;
        // for now, metrics api returns errors as 200 messages, so we have to detect it
        if ((response.msg && response.msg.indexOf('Unable to get results') > -1) || (response.status == 500)) {
          this.closeWidget();
          this.pubsub.publish(this.pubsub.ALERT, new ApiFeedback({
            code: ApiFeedback.CODES.ALERT,
            msg: 'Unfortunately, the metrics service returned error (it affects only some queries). Please try with different search parameters.',
            modal: true
          }));
          return;
        }
        this.containerModel.set("data", response);
      },

      onShow : function(){
        var data = this.containerModel.get("data");
        this.createTableViews(data);
        this.createGraphViews(data);
        this.insertViews(data);
      }


    });

    return Widget;

  });
