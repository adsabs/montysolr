define([
  'marionette',
  'js/components/api_query',
  'js/components/api_targets',
  'js/components/api_request',
  'js/widgets/base/base_widget',
  'hbs!./templates/recommender_template',
  'bootstrap'
], function(
  Marionette,
  ApiQuery,
  ApiTargets,
  ApiRequest,
  BaseWidget,
  RecommenderTemplate
  ){

  var RecommenderView = Marionette.ItemView.extend({

    initialize : function(){
      this.listenTo(this.collection, "reset", this.render);
    },

    template : RecommenderTemplate,


    events : {
      "click .button-toggle" : "toggleList"
    },

    toggleList : function(){

      this.$(".additional-papers").toggleClass("hidden");
      if ( this.$(".additional-papers").hasClass("hidden")){
        this.$(".button-toggle").text("more");
      }
      else {
        this.$(".button-toggle").text("less");
      }
    },

    onRender : function(){
      this.$(".icon-help").popover({trigger: "hover"});
    },
    className : "recommender-widget s-recommender-widget"
  });


  var RecommenderWidget = BaseWidget.extend({

    initialize : function(){
      this.collection = new Backbone.Collection();
      this.view = new RecommenderView({collection : this.collection});
    },

    activate: function (beehive) {

      /*
      * hiding this widget for now
      * */
//
//      this.pubsub = beehive.Services.get('PubSub');
//      _.bindAll(this, ['processResponse', 'onDisplayDocuments']);
//      this.pubsub.subscribe(this.pubsub.DISPLAY_DOCUMENTS, this.onDisplayDocuments);
//      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
    },

    onDisplayDocuments: function(apiQuery) {
      var bibcode = apiQuery.get('q');
      if (bibcode.length > 0 && bibcode[0].indexOf('bibcode:') > -1) {
        bibcode = bibcode[0].replace('bibcode:', '');
        this.loadBibcodeData(bibcode);
      }
    },

    loadBibcodeData : function(bibcode){

      if (bibcode === this._bibcode){
        this.trigger('page-manager-event', 'widget-ready', {'isActive': true});
      }
      else {
        //clear the current collection
        this.collection.reset();
        this._bibcode = bibcode;
        var target = ApiTargets.RECOMMENDER + "/" + bibcode;
        var request =  new ApiRequest({
          target:target
        });
        this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, request);
      }
    },

    processResponse : function(data){
      data = data.toJSON();
      if (data.recommendations){
        this.collection.reset(data.recommendations);
        //right now this is being ignored by the toc widget
        this.trigger('page-manager-event', 'widget-ready', {'isActive': true});
      }
    }
  });

  return RecommenderWidget;
});