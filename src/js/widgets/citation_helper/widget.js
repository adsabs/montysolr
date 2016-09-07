define([
  'marionette',
  'js/widgets/base/base_widget',
  'js/components/api_response',
  'js/components/json_response',
  'js/components/api_request',
  'js/components/api_query',
  'js/mixins/dependon',
  'hbs!./templates/citation_helper_template',
  'bootstrap',
  'js/components/api_feedback',
  'js/components/api_targets',
  'analytics'
], function (
  Marionette,
  BaseWidget,
  ApiResponse,
  JsonResponse,
  ApiRequest,
  ApiQuery,
  Dependon,
  CitationHelperTemplate,
  bs,
  ApiFeedback,
  ApiTargets,
  analytics
  ) {

  var Model = Backbone.Model.extend({
    defaults : {
      items: [],
      widgetName: "CitationHelper",
      libraryID: null,
      permission: null,
      libname: null
    }
  });

  var View = Marionette.ItemView.extend({

    template: CitationHelperTemplate,

    events : {
      "click .close-widget": "signalCloseWidget",
      "click .submit-add-to-library" : "libraryAdd"
    },

    modelEvents: {
      "change" : 'render'
    },

    libraryAdd : function(){

      //show loading view
      this.$(".submit-add-to-library").html('<i class="fa fa-spinner fa-pulse"></i>');

      var data = {};

      //we have the selected library in the model but only if there was a select event, so query DOM
      data.libraryID = this.model.get('libraryID');
      data.libname = this.model.get('libname');
      var bibcodes = this.$('input:checked').map(function() {return this.value;}).get();
      data.recordsToAdd = bibcodes;
      this.trigger("library-add", data);
    },

    signalCloseWidget: function () {
      this.trigger('close-widget');
    }

  });

  var CitationHelperWidget = BaseWidget.extend({

    viewEvents: {
      'close-widget': 'closeWidget',
      "library-add" : "libraryAddSubmit"
    },

    initialize: function (options) {

      options = options || {};

      this.model = new Model();
      this.view = new View({model : this.model});
      Marionette.bindEntityEvents(this, this.view, Marionette.getOption(this, "viewEvents"));

    },

    activate : function(beehive){
      this.setBeeHive(beehive);
      _.bindAll(this, "setCurrentQuery", "processResponse");
      var pubsub = beehive.getService('PubSub');
      pubsub.subscribe(pubsub.INVITING_REQUEST, this.setCurrentQuery);
      pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);

      //on initialization, store the current query
      if (this.getBeeHive().getObject("AppStorage")){
        this.setCurrentQuery(this.getBeeHive().getObject("AppStorage").getCurrentQuery());
      };

    },

    libraryAddSubmit : function(data){
      var options = {}, that = this;

      options.library = data.libraryID;
      options.bibcodes = data.recordsToAdd;
      name = data.libname;

      //this returns a promise
      this.getBeeHive().getObject("LibraryController").addBibcodesToLib(options)
          .done(function(response, status){
            var numAlreadyInLib = response.numBibcodesRequested - parseInt(response.number_added);
            that.model.set("feedback", {
              success : true,
              name : name,
              id : data.libraryID,
              numRecords: response.number_added,
              numAlreadyInLib : numAlreadyInLib
            });

          })
          .fail(function(response){
            that.model.set("feedback", {
              success : false,
              name : name,
              id : data.libraryID,
              error: JSON.parse(arguments[0].responseText).error
            });
          });

      this.clearFeedbackWithDelay();
    },

    clearFeedbackWithDelay : function(){

      var that = this,
          //five seconds
          timeout = 5000;

      setTimeout(function(){
        that.model.unset("feedback");
      }, timeout);

    },

    // This is the function that retrieves results from the Citation Helper micro service
    // The service expects a POST request with a set of bibcodes in the POST body:
    //     {"bibcodes": [ ... list of bibcodes]}
    // and if actual results are found, the service will answer with a list of dictionaries
    // with each dictionary have 4 attributes: bibcode, title, author and score
    getSuggestions : function(bibcodes){

      var d = $.Deferred(),
        pubsub = this.getPubSub(),
        options = {
          type : "POST",
          contentType : "application/json"
        };

      var request =  new ApiRequest({
        target: ApiTargets.SERVICE_CITATION_HELPER,
        query: new ApiQuery({"bibcodes" : bibcodes}),
        options : options
      });
      // so promise can be resolved

      pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, function(response){
        d.resolve(response.toJSON());
      });

      pubsub.publish(pubsub.EXECUTE_REQUEST, request);
      return d.promise();
    },

    // Depending on the type of response we get, we either contact the Citation Helper end point
    // to get results, or we use the results retrieved from this end point to populate the
    // template and display the results
    processResponse: function (response) {
      //We received bibcodes from the search end point
      if (response instanceof ApiResponse){
        var bibcodes = _.map(response.get("response.docs"), function(d){return d.bibcode})
        // send the bibcodes to the Citation Helper end point
        this.getSuggestions(bibcodes);
      }
      // We got an answer from the Citation Helper end point:
	  // the Citation Helper sends back a list of dictionaries
      // with keys 'bibcode', 'title', 'author', 'score'
      else if (response instanceof JsonResponse ) {
        this.processSuggestions(response)
      }
    },

    processSuggestions: function (response) {
      //how is the json response formed? need to figure out why attributes is there
      response = response.attributes ? response.attributes : response;
      if ((response.Error && response.Error === "Unable to get results!") || (response.status == 500)) {
        this.model.set('msg', 'Citation Helper did not find any suggestions.')
      } else {
        this.model.set('items', response);
      };
    },

    renderWidgetForCurrentQuery : function(){
      this.reset();
      this.dispatchRequest(this.getCurrentQuery());
    },

    reset: function () {
      this.model.clear({silent : true}).set('items',[], {silent : true});
    },

    closeWidget: function () {
      this.getPubSub().publish(this.getPubSub().NAVIGATE, "results-page");
    },

    renderWidgetForListOfBibcodes : function(bibcodes, data){
      this.reset();

      var request =  new ApiRequest({
        target: ApiTargets.SERVICE_CITATION_HELPER,
        query: new ApiQuery({"bibcodes" : bibcodes}),
        options : {
          type : "POST",
          contentType : "application/json"
        }
      });

      this.model.set({'libraryID': data.libid,
        'permission': data.permission,
        'libname': data.libname});

      this.getPubSub().publish(this.getPubSub().EXECUTE_REQUEST, request);
    }

  });

  _.extend(CitationHelperWidget.prototype, Dependon.BeeHive);

  return CitationHelperWidget;
});
