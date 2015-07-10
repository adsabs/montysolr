define([
  'underscore',
  'jquery',
  'backbone',
  'marionette',
  'js/components/api_query',
  'js/widgets/base/base_widget',
  'hbs!./templates/resources_template',
  'js/mixins/link_generator_mixin',
  'bootstrap'
], function(
  _,
  $,
  Backbone,
  Marionette,
  ApiQuery,
  BaseWidget,
  ResourcesTemplate,
  LinkGenerator,
  Bootstrap
  ){


  var ResourcesModel = Backbone.Model.extend({

    defaults : function(){

      return {
        fullTextSources : undefined,
        dataProducts : undefined
      }
    }
  });


  var ResourcesView = Marionette.ItemView.extend({
    template : ResourcesTemplate,
    
    modelEvents: {
      "change": "render"
    },

    className : "resources-widget",

    onRender : function(){
      this.$('[data-toggle="tooltip"]').tooltip();
    }
  });


  var ResourcesWidget = BaseWidget.extend({
    initialize : function(options){
      options = options || {};
      this.model = new ResourcesModel();
      this.view = new ResourcesView({model : this.model});
      this._bibcode = options.bibcode || undefined;
    },

    activate: function (beehive) {
      this.beehive = beehive;
      this.pubsub = beehive.Services.get('PubSub');
      _.bindAll(this, ['processResponse', 'onDisplayDocuments']);
      this.pubsub.subscribe(this.pubsub.DISPLAY_DOCUMENTS, this.onDisplayDocuments);
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
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
        self.trigger('page-manager-event', 'widget-ready', {'isActive': true});
      }
      else {
        this._bibcode = bibcode;
        var searchTerm = "bibcode:"+this._bibcode;
        //abstractPageFields comes from the LinkGenerator Mixin
        this.dispatchRequest(new ApiQuery({'q': searchTerm, fl : "links_data,[citations],property,bibcode"}));
      }

    },


    processResponse : function(apiResponse){

      var data = apiResponse.get("response.docs[0]");
      //get link server info if it exists
      data.link_server = this.beehive.getObject("User").getUserData("USER_DATA").link_server;
      //link mixin
      data = this.parseResourcesData(data);

      this.model.set(data);
      this.trigger('page-manager-event', 'widget-ready', {'isActive': true});

    }

  });

  _.extend(ResourcesWidget.prototype, LinkGenerator);
  return ResourcesWidget
});