
define([
    'backbone',
    'js/components/generic_module',
    'js/mixins/hardened',
    'js/components/api_targets',
    'js/components/api_request',
    'js/components/api_feedback',
    'js/mixins/dependon'

  ],
  function(
    Backbone,
    GenericModule,
    Hardened,
    ApiTargets,
    ApiRequest,
    ApiFeedback,
    Dependon
    ) {



    var LibraryModel = Backbone.Model.extend({

      defaults : function(){
        //this is the data we expect to get from the server
        return {
          num_documents : 0,
          date_last_modified : undefined,
          permission: undefined,
          description: "",
          public: false,
          num_users: 1,
          owner : undefined,
          date_created: undefined,
          id: undefined,
          title: ""

        }
      }

    });

    var LibraryCollection = Backbone.Collection.extend({

      model : LibraryModel

    });


    var LibraryController = GenericModule.extend({

      initialize : function(){
        //store all metadata entries here
        this.collection = new LibraryCollection();
      },

      activate: function (beehive) {
        this.setBeeHive(beehive.getHardenedInstance())
        pubsub = this.getBeeHive().getService('PubSub');

        pubsub.subscribe(pubsub.START_SEARCH, _.bind(this.updateCurrentQuery, this));
        pubsub.subscribe(pubsub.USER_ANNOUNCEMENT, _.bind(this.handleUserAnnouncement, this));

        /*
         * the three events that come from changing a collection:
         * -change if a model's contents were changed
         * -add if models were added
         * -reset if the entire collection was reset
         * -remove when a model is removed (library deleted)
         * */
        _.each(["change", "add", "reset", "remove"], function(ev){

          this.listenTo(this.collection, ev, function(arg1, arg2){
            if (ev == "change" && arg1 instanceof Backbone.Model ){
              //a single model changed, widgets might want to know which one
              pubsub.publish(pubsub.LIBRARY_CHANGE, this.collection.toJSON(), {ev: ev, id: arg1.id});
            }
            else {
              pubsub.publish(pubsub.LIBRARY_CHANGE, this.collection.toJSON(),  {ev: ev});
            }
          });

        }, this);

      },

      handleUserAnnouncement : function(event){
        if (event == "user_signed_in" || event == "user_signed_out" ){
          this._fetchAllMetadata();
        }
      },

      /*
       * private methods
       */

      updateCurrentQuery : function(apiQuery){
        this._currentQuery = apiQuery;
      },

      composeRequest : function (target, method, options) {
        var request,
          options = options || {},
          data = options.data || undefined;

        //using "endpoint" to mean the actual url string
        //get data from the relevant model based on the endpoint

        var deferred = $.Deferred();

        function done(){
          var args = [_.extend(arguments[0], options.extraArguments), [].slice(arguments, 1)];
          deferred.resolve.apply(undefined, args);
        }

        function fail(){
          deferred.reject.apply(undefined, arguments);
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

        this.getBeeHive().getService("Api").request(request);

        return deferred;

      },

      _executeApiRequest: function(apiQuery){

        var req = new ApiRequest({
          target: ApiTargets.SEARCH,
          query: apiQuery
        });
        var defer = $.Deferred();
        pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, _.bind(function(data) {
          defer.resolve(data);
        }), this);

        pubsub.publish(pubsub.EXECUTE_REQUEST, req);

        return defer;
      },

      _getBibcodes : function(options) {

        var deferred = $.Deferred(), that = this;

        if (options.bibcodes == "all"){

          var limit = options.limit || 1000,
              start = 0,
              rows = 100,
              bibcodes = [];

          var q = this._currentQuery.clone();
              q.set("rows", 100);
              q.set("fl", "bibcode");

          function makeRequest(){

            q.set("start", start);

            this._executeApiRequest(q).done(function(apiResponse){

              var bibs = _.map(apiResponse.get("response.docs"), function(d){
                return d.bibcode;
              });

              [].push.apply(bibcodes, bibs);
              start += rows;
              if (start < limit){
                makeRequest.call(that);
              }
              else {
                deferred.resolve(bibcodes);
              }
            });

          }

          makeRequest.call(this);

        }
        else if (options.bibcodes == "selected"){

          var bibs = this.getBeeHive().getObject("AppStorage").getSelectedPapers();
          deferred.resolve(bibs);
        }
        else {
          throw new Error("should we add all bibcodes or only selected ones?");
        }

        return deferred.promise();

      },

      _fetchAllMetadata : function(){

        var that = this;

        var endpoint = ApiTargets["LIBRARIES"];
        return this.composeRequest(endpoint, "GET").done(function(data){
          that._dataLoaded = true;
          that.collection.reset(data.libraries);
        });

      },

      _dataLoaded : false,


      /*
       * public methods
       *
       */

      isDataLoaded : function(){
        return this._dataLoaded;
      },


      getAllMetadata : function(){
        return this.collection.toJSON();
      },

      /*
       * get all records + metadata from an individual library
       *
       */

      getLibraryData: function(id){

        var endpoint = ApiTargets["LIBRARIES"] + "/" + id;
        return this.composeRequest(endpoint, "GET");

      },

      /*
       * requires id, name, description
       */

      createLibrary : function(data){

        var that = this;

        var endpoint = ApiTargets["LIBRARIES"];
        return this.composeRequest(endpoint, "POST", {data : data})
          .done(function(data){
            //refresh collection
            that._fetchAllMetadata();
          });
      },

      deleteLibrary : function(id, name){

        var that = this,
          endpoint = ApiTargets["DOCUMENTS"] + "/" + id,
          name;

        var promise  = this.composeRequest(endpoint, "DELETE")
          .done(function(){
            //delete library from internal representation
            that.collection.remove(id);
            //take care of ui
            that.getBeeHive().getService("PubSub").publish(that.getBeeHive().getService("PubSub").NAVIGATE, "AllLibrariesWidget", "libraries");
            var message = "Library <b>" + name + "</b> was successfully deleted";
            that.getBeeHive().getService("PubSub").publish(that.getBeeHive().getService("PubSub").ALERT, new ApiFeedback({code: 0, msg: message, type : "success"}));
          })
          .fail(function(jqXHR){
            var error = JSON.parse(jqXHR.responseText).error
            var message = "Library <b>" + name + "</b> could not be deleted : (" + error + ")";
            that.getBeeHive().getService("PubSub").publish(that.getBeeHive().getService("PubSub").ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
          });

        return promise;

      },

      /*
       * @param id
       * @param updateData e.g. {bibcode:[1,2,3], action: "add/remove" }
       *
       */
      updateLibraryContents : function(id, updateData){

        var that = this,
          data = {data : updateData, extraArguments : {numBibcodesRequested : updateData.bibcode.length}};

        var endpoint = ApiTargets["DOCUMENTS"] + "/" + id;
        return this.composeRequest(endpoint, "POST", data)
          .done(function(info){
            var currentNum = that.collection.get(id).get("num_documents");
            var newNum = info.number_added ? currentNum + info.number_added : currentNum - info.number_removed;

            that.collection.get(id).set({
              "num_documents":newNum,
              "date_last_modified" : new Date().toString()
            });

          })
          .fail(function(jqXHR){
            var error = JSON.parse(jqXHR.responseText).error
            var message = "Library <b>" +  that.collection.get(id).title + "</b> could not be updated: (" + error +")";
            that.getBeeHive().getService("PubSub").publish(that.getBeeHive().getService("PubSub").ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
          });

      },

//      /*
//      * email, permission, value
//      * */
//
//      updateLibraryPermissions : function(id, data){
//
//        var that = this;
//
//        var endpoint = ApiTargets["PERMISSIONS"] + "/" + id;
//        return this.composeRequest(endpoint, "POST", {data : data})
//          .done(function(data){
//            this.collection.get(id).set(data);
//          })
//          .fail(function(jqXHR){
//            var error = JSON.parse(jqXHR.responseText).error;
//            var message = "Library <b>" +  that.collection.get(id).title + "</b> could not be updated: ("  + error + ")";
//            that.getBeeHive().getService("PubSub").publish(that.key, that.getBeeHive().getService("PubSub").ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
//          });
//
//      },

      updateLibraryMetadata : function(id, data){

        var that = this;

        var endpoint = ApiTargets["DOCUMENTS"] + "/" + id;
        return this.composeRequest(endpoint, "PUT", {data : data})
          .done(function(data){
            that.collection.get(id).set(data);
          })
          .fail(function(jqXHR){
            var error = JSON.parse(jqXHR.responseText).error;
            var message = "Library <b>" +  that.collection.get(id).get("name") + "</b> could not be updated: (" + error + ")";
            that.getBeeHive().getService("PubSub").publish(that.getBeeHive().getService("PubSub").ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
          });

      },

      /*fetches bibcodes, then submits them to library endpoint
       *
       * @param data e.g. {"library": [library_id], "bibcodes": ["all"/ "selected"]}
       *
       */
      addBibcodesToLib : function(data){

        var that = this, promise = this._getBibcodes(data).then(function(bibcodes) {
          //should return success or fail message
          return that.updateLibraryContents(data.library, { bibcode : bibcodes, action : "add" })
            .fail(function(){
              var message = "Library <b>" +  that.collection.get(data.library).title + "</b> could not be updated";
              that.getBeeHive().getService("PubSub").publish(that.getBeeHive().getService("PubSub").ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
            });

        });

        return promise

      },

      /* fetch the bibcodes, then POST to the create endpoint with the bibcodes
       *
       * @param data: e.g. {bibcodes: ["all"/"selected"], name: "ddddd"}
       *
       */

      createLibAndAddBibcodes : function(data){

        var that = this, promise =  this._getBibcodes(data).then(function(bibcodes){
          if (!bibcodes){
            throw new Error("Solr returned no bibcodes, can't put them in the new library");
          }
          data.bibcode = bibcodes;
          var createLibraryPromise = that.createLibrary(data)
            .fail(function(){
              var message = "Library <b>" +  name+ "</b> could not be created";
              that.getBeeHive().getService("PubSub").publish(that.getBeeHive().getService("PubSub").ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
            });

          return createLibraryPromise

        });

        return promise;

      },

      hardenedInterface: {
        getAllMetadata : "returns json list of libraries",
        getLibraryData : "get library records + metadata",
        createLibrary : "createLibrary",

        //these two functions fetch bibcodes based on the arguments given
        createLibAndAddBibcodes : "createLibAndAddBibcodes",
        addBibcodesToLib : "addBibcodesToLib",

        deleteLibrary : "deleteLibrary",
        updateLibraryContents : "updateLibraryContents",
//      updateLibraryPermissions : "updateLibraryPermissions",
        updateLibraryMetadata : "updateLibraryMetadata",

        isDataLoaded : "tells if initial load event has happened"
      }

    });

    _.extend(LibraryController.prototype, Hardened, Dependon.BeeHive);

    return LibraryController;

  });