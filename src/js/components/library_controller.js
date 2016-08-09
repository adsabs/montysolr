
define([
    'backbone',
    'js/components/generic_module',
    'js/mixins/hardened',
    'js/components/api_targets',
    'js/components/api_request',
    'js/components/api_feedback',
    'js/components/api_query',
    'js/mixins/dependon'

  ],
  function(
    Backbone,
    GenericModule,
    Hardened,
    ApiTargets,
    ApiRequest,
    ApiFeedback,
    ApiQuery,
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

        pubsub.subscribe(pubsub.INVITING_REQUEST, _.bind(this.updateCurrentQuery, this));
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
              //also clear out the bibcode cache if necessary
              delete this._libraryBibcodeCache[arg1.id];
            }
            else {
              pubsub.publish(pubsub.LIBRARY_CHANGE, this.collection.toJSON(),  {ev: ev});
            }
          });

        }, this);

      },

      handleUserAnnouncement : function(event){
        if (event == "user_signed_in"){
          this._fetchAllMetadata();
        }
        else if (event == "user_signed_out" ) {
          this.collection.reset({});
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
          var limit = options.limit || 2000,
              start = 0,
              rows = 100,
              bibcodes = [];

          var q = this._currentQuery.clone();
          q.unlock();
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
        //for abstract widget
        else if (_.isArray(options.bibcodes)){
          deferred.resolve(options.bibcodes);
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
          that._metadataLoaded = true;
          that.collection.reset(data.libraries);
        });

      },

      /*
       * public methods
       *
       */
      /*
      * this is how widgets/controllers can learn about
      * all libraries
      * if you provide an id, you just get info
      * about the lib with that id
      * */

      getLibraryMetadata : function(id){
        // check to see if the id is even in the collection,
        // if not return fetchLibraryMetadata;
        if (id && !this.collection.get(id)) {
          return this.fetchLibraryMetadata(id);
        }
        var deferred = $.Deferred();
        var that = this;
        //if this is the initial check, just wait until we can load the metadata
        if  (!this._metadataLoaded){
          this._fetchAllMetadata().done(function(data){
            //make sure the collection is refilled before this promise is resolved
            setTimeout(function(){
              var data = id ? that.collection.get(id).toJSON() : that.collection.toJSON();
              deferred.resolve(data);
            }, 1);
          })
        }
        else {
          var data = id ? that.collection.get(id).toJSON() : that.collection.toJSON();
          deferred.resolve(data);
        }
        return deferred.promise();
      },

      /*
      * fetch the data especially -- useful for public libraries but also
      * in case the new data hasn't been added to the collection yet
      * */

      fetchLibraryMetadata : function(id){
        var that = this;
        if (!id) throw new Error("need to provide a library id");
        var deferred = $.Deferred();

        this.composeRequest(ApiTargets["LIBRARIES"] + "/" + id)
            .done(function(data){
              deferred.resolve(data.metadata);
              //set into collection
              that.collection.add(data.metadata, {merge : true});
            })
            .fail(function(){
              // just navigate to a 404 page
              that.getPubSub().publish(that.getPubSub().NAVIGATE, "404");
            });

      return deferred.promise();
      },


      /*
      *
      * here, store lists of bibcodes requested from the 'getLibraryBibcodes'
      * method. they are stored here so that if a user quickly toggles back
      * and forth between the export/metrics/vis widget, we won't have to re-fetch
      * the bibcodes
      * */

      _libraryBibcodeCache : {},

      /*
      * get list of 2000 bibcodes,
      * this is used for fetching data for export, metrics, etc
      *
      * */

      getLibraryBibcodes : function(id){

        var deferred = $.Deferred(),
            that = this,
            //hard limit, no more than 10,000 records can be exported to search results page
            maxReturned = 10000;

        //we already have it in the cache, so just resolve + return promise
        if (this._libraryBibcodeCache[id]) {
          deferred.resolve(this._libraryBibcodeCache[id]);
          return deferred.promise();
        }

        var limit = maxReturned,
            //start gets incremented
            start = 0,
            rows = 100,
            bibcodes = [],
            endpoint = ApiTargets["LIBRARIES"] + "/" + id,
            that = this;

        //this function gets called repeatedly
        function done(data) {
          limit = data.solr.response.numFound > maxReturned ? maxReturned : data.solr.response.numFound;
          var bibs = _.pluck(data.solr.response.docs, 'bibcode');
          [].push.apply(bibcodes, bibs);
          start += rows;
          if (start < limit){  makeRequest(); }
          else {
            that._libraryBibcodeCache[id] = bibcodes;
            deferred.resolve(bibcodes);
          }
        }

        function makeRequest(){

          var q = new ApiQuery();
          q.set("rows", 100);
          q.set("fl", "bibcode");

          q.set("start", start);

          var request =  new ApiRequest({
            target: endpoint,
            query: q,
            options : {
              context : this,
              contentType : "application/x-www-form-urlencoded",
              done : done,
              fail : this.handleError
            }
          });
          that.getBeeHive().getService("Api").request(request);
        }

        makeRequest.call(this);
        return deferred.promise();

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
          .done(function(){
           that.fetchLibraryMetadata(id);
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

      importLibraries : function(service){

        var endpoint, d = $.Deferred(), that = this;
        if (service === "classic"){
          endpoint = ApiTargets.LIBRARY_IMPORT_CLASSIC_TO_BBB;
        }
        else if (service === "twopointoh"){
          endpoint = ApiTargets.LIBRARY_IMPORT_ADS2_TO_BBB;
        }
        else {
          console.error("didn't recognize library endpoint! should be one of 'classic' or 'twopointoh' ");
          return
        }

        this.getBeeHive().getService("Api").request(new ApiRequest({
          target: endpoint,
          options: {
            done: function (data) {
             d.resolve.apply(undefined,[].slice.apply(arguments));
              //re-fetch metadata, since new libs were imported
              that._fetchAllMetadata();
            },
            fail: function (data) {
              d.reject.apply(undefined,[].slice.apply(arguments));
            }
          }
        }));

        return d.promise();

      },


      hardenedInterface: {
        getLibraryMetadata : "returns json list of libraries, optional lib id as param",
        createLibrary : "createLibrary",

        //these two functions fetch bibcodes based on the arguments given
        createLibAndAddBibcodes : "createLibAndAddBibcodes",
        addBibcodesToLib : "addBibcodesToLib",

        deleteLibrary : "deleteLibrary",
        updateLibraryContents : "updateLibraryContents",
//      updateLibraryPermissions : "updateLibraryPermissions",
        updateLibraryMetadata : "updateLibraryMetadata",

        importLibraries : "importLibraries",

        //currently called by library individual widget to get
        //lists of bibs to pass to export, metrics, vis widgets etc
        getLibraryBibcodes : "getLibraryBibcodes"
      }

    });

    _.extend(LibraryController.prototype, Hardened, Dependon.BeeHive);

    return LibraryController;

  });
