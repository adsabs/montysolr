
define([
    'backbone',
    'js/components/generic_module',
    'js/mixins/hardened',
    'js/components/api_targets',
    'js/components/api_request',
    'js/components/api_feedback'

  ],
  function(
    Backbone,
    GenericModule,
    Hardened,
    ApiTargets,
    ApiRequest,
    ApiFeedback
    ) {

    var LibraryModel = Backbone.Model.extend({

      defaults : function(){
        return {
          name: undefined,
          description: undefined,
          id : undefined,
          permissions : undefined,
          num_papers : 0,
          date_created : undefined,
          date_last_modified : undefined
        }
      }

    });

    var LibraryCollection = Backbone.Collection.extend({

      model : LibraryModel

    });


    var LibraryController = GenericModule.extend({

      initialize : function(){
        this.collection = new LibraryCollection();
        //will only be a reset event
        this.listenTo(this.collection, "reset", this.onCollectionChange);
      },

      activate: function (beehive) {
        this.beehive = beehive;
        this.pubsub = beehive.Services.get('PubSub');
        this.key = this.pubsub.getPubSubKey();

        this.pubsub.subscribe(this.key, this.pubsub.START_SEARCH, _.bind(this.updateCurrentQuery, this));
        //get info about libraries after app starts
        this.pubsub.subscribe(this.key, this.pubsub.APP_STARTED, _.bind(this.fetchAllLibraryData, this));

      },

      /*
       * private methods
       */

      //announce the updated summary data to interested widgets
      onCollectionChange : function(){
        this.pubsub.publish(this.key, this.pubsub.LIBRARY_CHANGE, this.collection.toJSON());
      },

      updateCurrentQuery : function(apiQuery){
        this._currentQuery = apiQuery;
      },

      fetchAllLibraryData : function(){

        var that = this, endpoint = ApiTargets["LIBRARIES"];

        this.composeRequest(endpoint, "GET")
          .done(function(data){
            that.collection.reset(data.libraries);
          })
          .fail(function(){

          });
      },

      composeRequest : function (target, method, options) {
        var request,
            options = options || {},
            data = options.data || undefined,
            that = this;

        //using "endpoint" to mean the actual url string
        //get data from the relevant model based on the endpoint

        var deferred = $.Deferred();

        function done(){
          deferred.resolve.apply(undefined, arguments);
          if (_.contains(["PUT", "POST", "DELETE"], method)){
            that.fetchAllLibraryData();
          }
        }
        function fail(){
          deferred.resolve.apply(undefined, arguments);
        }

        request = new ApiRequest({
            target :target,
            options : {
              context : this,
              type: method,
              data: JSON.stringify(data),
              contentType : "application/json",
              done: done,
              fail : fail
            }
          });

          this.beehive.getService("Api").request(request);

        return deferred;

        },

      _executeApiRequest: function(apiQuery){

        var req = new ApiRequest({
          target: ApiTargets.SEARCH,
          query: apiQuery
        });
        var defer = $.Deferred();
        this.pubsub.subscribeOnce(this.key, this.pubsub.DELIVERING_RESPONSE, _.bind(function(data) {
          defer.resolve(data);
        }), this);

        this.pubsub.publish(this.key, this.pubsub.EXECUTE_REQUEST, req);

        return defer;
      },

      _getBibcodes : function(options) {

        var deferred = $.Deferred();

        if (options.bibcodes == "all"){

          var limit = options.limit || 2000;

          //fetch bibcodes from solr
          var q = this._currentQuery.clone();
          q.set("rows", limit);
          q.set("fl", "bibcode");

          this._executeApiRequest(q).done(function(apiResponse){
            //get bibs here
            var bibs = _.map(apiResponse.get("response.docs"), function(d){
              return d.bibcode;
            });
            deferred.resolve(bibs);
          });

        }
        else if (options.bibcodes == "selected"){

          var bibs = this.beehive.getObject("AppStorage").getSelectedPapers();
          deferred.resolve(bibs);
        }
        else {
          throw new Error("should we add all bibcodes or only selected ones?");
        }

        return deferred;

      },


      /*
      * functions below are all accessible through hardened interface
      * */

      getLibraryMetadata : function(id){

        var deferred = $.Deferred();

        //either collection doesn't exist, or we don't have the id yet
        if (!this.collection.toJSON().length || (id && !this.collection.get(id))){
          this.fetchAllLibraryData();
          this.listenToOnce(this.collection, "reset", function(){
            if (id){
              if (this.collection.get(id)){
                deferred.resolve(this.collection.get(id).toJSON());
              }
              else {
                deferred.reject("library id not found");
              }
            }
            else {
              deferred.resolve(this.collection.toJSON());
            }

          })

        }
        else {
          if (id){
            if (this.collection.get(id)) {
              deferred.resolve(this.collection.get(id).toJSON());
            }
              else {
                deferred.reject("library id not found");
              }
          }
          else {
           deferred.resolve(this.collection.toJSON());
          }

        }

        return deferred;

      },

      /*
      * get all records from an individual library
      *
      */

      getLibraryRecords : function(id){

        var endpoint = ApiTargets["LIBRARIES"] + "/" + id;
        return this.composeRequest(endpoint, "GET");

      },

      /*
       * requires id, name, description
       */

      createLibrary : function(options){

        var  endpoint = ApiTargets["LIBRARIES"];
        return this.composeRequest(endpoint, "POST", options)

      },


      deleteLibrary : function(id){

        var that = this,
            endpoint = ApiTargets["DOCUMENTS"] + "/" + id,
            name;

        this.getLibraryMetadata(id).done(function(data){
          name = data.name;
        });

       var promise  = this.composeRequest(endpoint, "DELETE")
          .done(function(){
            that.pubsub.publish(that.key, that.pubsub.NAVIGATE, "AllLibrariesWidget");
            var message = "Library <b>" + name + "</b> was successfully deleted";
            that.pubsub.publish(that.key, that.pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "success"}));
          })
          .fail(function(){
            var message = "Library <b>" + name + "</b> could not be deleted";
            that.pubsub.publish(that.key, that.pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
          });

        return promise;

      },

      // updateData = {bibcode:[1,2,3], action: "add/remove" }
      updateLibraryContents : function(id, updateData){

        var endpoint = ApiTargets["DOCUMENTS"] + "/" + id;
        return this.composeRequest(endpoint, "POST", updateData);

      },

      /*
      * email, permission, value
      * */

      updateLibraryPermissions : function(id, data){

        var endpoint = ApiTargets["PERMISSIONS"] + "/" + id;
        return this.composeRequest(endpoint, "POST", {data : data});

      },

      updateLibraryMetadata : function(id, data){

        var endpoint = ApiTargets["DOCUMENTS"] + "/" + id;
        return this.composeRequest(endpoint, "PUT", {data : data});

      },

      /*fetches bibcodes, then submits them to library endpoint*/
     addBibcodesToLib : function(options){

       var that = this, promise = this._getBibcodes(options).then(function(bibcodes) {
        //should return success or fail message
        return that.updateLibraryContents(options.library, {data : { bibcode : bibcodes, action : "add"} });
      });
       return promise

     },

     createLibAndAddBibcodes : function(options){

       var that = this, promise =  this._getBibcodes(options).then(function(bibcodes){
         if (!bibcodes){
           throw new Error("Solr returned no bibcodes, can't put them in the new library");
         }
         options.bibcode = bibcodes;
         var createLibraryPromise = that.createLibrary({data : options});
         return createLibraryPromise;
       });

       return promise;

      },

      hardenedInterface: {
        getLibraryMetadata : "getLibraryMetadata (all or for individual library)",
        getLibraryRecords : "getLibraryRecords",
        createLibrary : "createLibrary",

        //these two functions fetch bibcodes based on the arguments given
        createLibAndAddBibcodes : "createLibAndAddBibcodes",
        addBibcodesToLib : "addBibcodesToLib",

        deleteLibrary : "deleteLibrary",
        updateLibraryContents : "updateLibraryContents",
        updateLibraryPermissions : "updateLibraryPermissions",
        updateLibraryMetadata : "updateLibraryMetadata"
      }

    });

    _.extend(LibraryController.prototype, Hardened);

    return LibraryController;

  });