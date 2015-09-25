
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

      createGraphViews: function (response) {

        var hist = response.histograms;

        //citations graph
        var citationsModel = new this.GraphModel();
        this.childViews.citationsGraphView = new this.GraphView({model: citationsModel });
        this.childViews.citationsGraphView.model.set("graphData", this.dataExtractor.plot_citshist({norm: false, citshist_data: hist["citations"]}));
        this.childViews.citationsGraphView.model.set("normalizedGraphData", this.dataExtractor.plot_citshist({norm: true, citshist_data: hist["citations"]}));

        //reads graph
        var readsModel = new this.GraphModel();
        this.childViews.readsGraphView = new this.GraphView({model: readsModel });
        this.childViews.readsGraphView.model.set("graphData", this.dataExtractor.plot_readshist({norm: false, readshist_data: hist["reads"]}));
        this.childViews.readsGraphView.model.set("normalizedGraphData", this.dataExtractor.plot_readshist({norm: true, readshist_data: hist["reads"]}));
      },

      createTableData : function(response){
        var data = {};
        var generalData = {refereed: response["basic stats refereed"], total: response["basic stats"]};
        var citationData = {refereed: response["citation stats refereed"], total: response["citation stats"]};

        data.readsModelData = {
          totalNumberOfReads: [generalData.total["total number of reads"], generalData.refereed["total number of reads"]],
          averageNumberOfReads: [generalData.total["average number of reads"], generalData.refereed["average number of reads"]],
          medianNumberOfReads: [generalData.total["median number of reads"], generalData.refereed["median number of reads"]],
          totalNumberOfDownloads: [generalData.total["total number of downloads"], generalData.refereed["total number of downloads"] ],
          averageNumberOfDownloads: [generalData.total["average number of downloads"], generalData.refereed["average number of downloads"]],
          medianNumberOfDownloads: [generalData.total["median number of downloads"], generalData.total["median number of downloads"]]
        };

        data.citationsModelData = {
          numberOfCitingPapers: [citationData.total["number of citing papers"], citationData.refereed["number of citing papers"]],
          totalCitations: [citationData.total["total number of citations"], citationData.refereed["total number of citations"]],
          numberOfSelfCitations: [citationData.total["number of self-citations"], citationData.refereed["number of self-citations"]],
          averageCitations: [citationData.total["average number of citations"], citationData.refereed["average number of citations"]],
          medianCitations: [citationData.total["median number of citations"],citationData.refereed["median number of citations"]],
          normalizedCitations: [citationData.total["normalized number of citations"], citationData.refereed["normalized number of citations"]],
          refereedCitations: [citationData.total["total number of refereed citations"], citationData.refereed["total number of refereed citations"]],
          averageRefereedCitations: [citationData.total["average number of refereed citations"],citationData.refereed["average number of refereed citations"]],
          medianRefereedCitations: [citationData.total["median number of refereed citations"], citationData.refereed["median number of refereed citations"]],
          normalizedRefereedCitations: [citationData.total["normalized number of refereed citations"], citationData.refereed["normalized number of refereed citations"]]
        };

        function limitPlaces(n){
          var stringNum = n.toString();
          if (stringNum.indexOf(".") > -1 && stringNum.split(".")[1]){
            return n.toFixed(1)
          }
          return n
        }

        //keep to 2 decimal places
        _.each(data, function(table,k){
          _.each(table, function(arr, name){
            table[name] = [limitPlaces(arr[0]), limitPlaces(arr[1])];
          });
        });

        return data;
      },

      processMetrics : function (response){
        //how is the json response formed? need to figure out why attributes is there
        response = response.attributes ? response.attributes : response;
        // for now, metrics api returns errors as 200 messages, so we have to detect it
        if ((response.msg && response.msg.indexOf('Unable to get results') > -1) || (response.status == 500)) {
          this.closeWidget();
          this.getPubSub().publish(this.getPubSub().ALERT, new ApiFeedback({
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
