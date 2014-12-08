define([
  'marionette',
  'js/components/api_query',
  'js/components/api_request',
  'js/widgets/base/base_widget',
  'hbs!./templates/recommender_template',
  'bootstrap'
], function(
  Marionette,
  ApiQuery,
  ApiRequest,
  BaseWidget,
  RecommenderTemplate
  ){



  var RecommenderView = Marionette.ItemView.extend({

    initialize : function(){

      this.listenTo(this.collection, "reset", this.render);

    },

    template : RecommenderTemplate,

    onRender : function(){

      this.$(".icon-help").popover({trigger : "hover", placement : "left"});

    }

  });


  var RecommenderWidget = BaseWidget.extend({


    initialize : function(){

      this.collection = new Backbone.Collection();

      this.view = new RecommenderView({collection : this.collection});

      this.showLoad = true;

    },

    activate: function (beehive) {
      this.pubsub = beehive.Services.get('PubSub');
      _.bindAll(this, ['onNewQuery', 'processResponse', 'onDisplayDocuments']);
      this.pubsub.subscribe(this.pubsub.START_SEARCH, this.onNewQuery);
      this.pubsub.subscribe(this.pubsub.DISPLAY_DOCUMENTS, this.onDisplayDocuments);
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
    },

    onNewQuery: function() {
      this.collection.reset();
    },

    onDisplayDocuments: function(apiQuery) {
      var bibcode = apiQuery.get('q');
      var self = this;
      if (bibcode.length > 0 && bibcode[0].indexOf('bibcode:') > -1) {
        bibcode = bibcode[0].replace('bibcode:', '');
        this.loadBibcodeData(bibcode).done(function(data) {
          self.trigger('page-manager-event', 'widget-ready', {'isActive': true, numFound: data});
        });
      }
    },

    loadBibcodeData : function(bibcode){

      if (bibcode === this._bibcode){
        this.deferredObject =  $.Deferred();
        this.deferredObject.resolve(this.model);
        return this.deferredObject.promise();
      }
      else {
        this._bibcode = bibcode;
        this.deferredObject =  $.Deferred();

        var target = "services/recommender/" + bibcode;

        var request =  new ApiRequest({
          target:target,
          query : new ApiQuery()
        });

        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, request);
        return this.deferredObject.promise();
      }

    },

    processResponse : function(data){

      this.collection.reset(data["recommendations"]);

    }


  });


  return RecommenderWidget;


})