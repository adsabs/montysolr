/**
 * This widget can paginate through a list of results. It can easily be inherited
 * by results widget ,table of contents widget, etc. It either listens to INVITING_REQUEST
 * in the case of the results widget, or the loadBibcode method (currently all other widgets).
 *
 * This widget consists of the following components:
 *
 * 1. a pagination view
 * 2. an associated pagination model (all pagination info is kept here and only here)
 * 3. a 'master' collection that holds all currently accessible records
 * 4. a 'visible' collection that shows all records that should currently be visible on screen
 * 5. a list view that listens to the visible collection and renders it
 * 6. an item view for each record rendered repeatedly by the list view
 * 7. a controller that handles requesting and recieving data from pubsub and initializing everything
 *
 */

define([
    'marionette',
    'backbone',
    'js/components/api_request',
    'js/components/api_query',
    'js/widgets/base/base_widget',
    'hbs!./templates/item-template',
    'hbs!./templates/results-container-template',
    'js/mixins/link_generator_mixin',
    'hbs!./templates/pagination-template',
    'js/mixins/add_stable_index_to_collection',
    'hbs!./templates/empty-view-template',
    'hbs!./templates/initial-view-template'
  ],

  function (Marionette,
    Backbone,
    ApiRequest,
    ApiQuery,
    BaseWidget,
    ItemTemplate,
    ResultsContainerTemplate,
    LinkGenerator,
    PaginationTemplate,
    WidgetPaginationMixin,
    EmptyViewTemplate,
    InitialViewTemplate
    ) {



    var ListOfThingsWidget = BaseWidget.extend({
      initialize: function (options) {
        //so each widget gets its own copy instead of sharing one on the prototype chain
        this.defaultQueryArguments = _.result(this, "defaultQueryArguments");

        //adding data for the letter links
        this.defaultQueryArguments.fl = this.defaultQueryArguments.fl + "," + this.resultsPageFields;

        //letting widget know that the loading transition should happen
        this.showLoad =  true;

        this.visibleCollection = new VisibleCollection();

        var paginationOptions = {};

        if (options.perPage){
          paginationOptions.perPage = options.perPage;
          this.defaultQueryArguments.rows = options.perPage
        }
        else {
          paginationOptions.perPage =  this.defaultQueryArguments.rows;
        }

        paginationOptions.numFound = undefined;
        paginationOptions.currentQuery = undefined;
        paginationOptions.page = 1;

        //have to use a cloned copy or else it will work on the first go but then
        //be modified once the model itself is modified!!!
        this.paginationModel = new PaginationModel({}, {defaults : function(){return _.clone(paginationOptions)}});

        this.paginationView = new PaginationView({
          model : this.paginationModel,
          //from the pagination mixin
          getStartVal : this.getStartVal

        });


        //showdetails defaults to false, so details button will be hidden
        this.view = new ListView({
          collection: this.visibleCollection,
          paginationView : this.paginationView,
          showDetailsButton : this.showDetailsButton,
          mainResults : this.mainResults

        });

        this.collection = new MasterCollection({}, {visibleCollection : this.visibleCollection,
          paginationModel: this.paginationModel});

        this.listenTo(this.collection, "all", this.onAllInternalEvents);
        this.on("all", this.onAllInternalEvents);

        BaseWidget.prototype.initialize.call(this, options);

      },


      activate: function (beehive) {
        _.bindAll(this,  "processResponse");
        this.pubsub = beehive.Services.get('PubSub');

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      resetWidget : function(){

        //this will trigger paginationModel to reset itself
        var defaults =  _.result(this.paginationModel, 'defaults')
        //reset pagination model, but prevent the view from immediately re-rendering
        //by passing silent
        this.paginationModel.set(defaults, {silent : true});

        //this will hopefully be overridden by the pagination information
        //gathered in processResponse, but just to be safe I'm setting it here too.

        //also resetting the main collection and the visible collection

        this.collection.reset(null, {silent : true});

        //prevent rendering of empty view
        this.visibleCollection.reset(null, {silent: true});

      },

      resetBibcode : function(bibcode){
        this._bibcode = bibcode;
      },

      /*
       -a way to get data on command on a per-bibcode-basis
       -used by the page managers
       -checks first to see if bibcode is the same as the current bibcode, in which case nothing happens
       -it returns a promise which is resolved when the data
       is available
       -this function has to be overridden for some less straightforward
       solr queries, such as "similar/more like this"
       */
      loadBibcodeData: function (bibcode) {

        if (bibcode === this._bibcode){
          this.deferredObject =  $.Deferred();
          this.deferredObject.resolve(this.paginationModel.get("numFound"));
          return this.deferredObject.promise();
        }

        //numFound needs to equal undefined as a signal to other functions that the request cycle has restarted
        //need to know whether to add or reset the collection (the absence of numFound means it is a new
        //query that needs to be reset)
        this.resetWidget();
        this.resetBibcode(bibcode);

        if ((!this.solrOperator && !this.solrField) || (this.solrOperator && this.solrField)){
          throw new Error("Can't call loadBibcodeData without either a solrOperator or a solrField, and can't have both!")
        }

        var searchTerm = this.solrOperator? this.solrOperator + "(bibcode:" + bibcode +")" : this.solrField + ":" + bibcode

        this.deferredObject =  $.Deferred();

        var q = this.composeQuery(this.defaultQueryArguments, new ApiQuery());

        q.set("q", searchTerm);

        if (this.sortOrder){
          q.set("sort", this.sortOrder)
        }

        var req = this.composeRequest(q);
        if (req) {
          this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
        }
        return this.deferredObject.promise();
      },

      //will be requested in composeRequest
      defaultQueryArguments: function(){
        return {
          fl: 'title,abstract,bibcode,author,keyword,citation_count,pub,aff,volume,year',
          rows : 25,
          start : 0
        }
      },

      processResponse: function (apiResponse) {

        this.setCurrentQuery(apiResponse.getApiQuery());

        var toSet = {"numFound":  apiResponse.get("response.numFound"),
          "currentQuery":this.getCurrentQuery()};

        //checking to see if we need to reset start or rows values
        var r =  this.getCurrentQuery().get("rows");
        var s = this.getCurrentQuery().get("start");

        if (r){
          r = $.isArray(r) ? r[0] : r;
          toSet.perPage = r;
        }

        if (s) {
          var perPage =  toSet.perPage || this.paginationModel.get("perPage");
          s = $.isArray(s) ? s[0] : s;

          //getPageVal comes from the pagination mixin
          toSet.page= this.getPageVal(s, perPage);
        }

        var docs = apiResponse.get("response.docs")
        docs = _.map(docs, function(d) {
          d.identifier = d.bibcode;
          return d
        });


        docs = this.parseLinksData(docs);
        docs = this.addPaginationToDocs(docs, apiResponse.get("response.start"));

        if (docs.length) {
          //reset the pagination model with toSet values
          //has to happen right before collection changes
          this.paginationModel.set(toSet, {silent : true});

          //just using add because the collection was emptied
          //when a new request was made
          this.collection.add(docs);

          /*
           * we need a special event that fires only once in event
           * of a reset OR an add
           * */
          this.collection.trigger("collection:augmented");
        }
        else {
          //nothing was found, show empty view
          this.collection.reset();
        }

        //resolving the promises generated by "loadBibcodeData"
        if (this.deferredObject){
          this.deferredObject.resolve(this.paginationModel.get("numFound"))
        }

        // XXX:rca - hack, to be solved later
        this.trigger('page-manager-event', 'widget-ready',
          {numFound: apiResponse.get("response.numFound"), widget: this});
      },

      setPaginationRequestPending : function(){
        this._paginationRequestPending = true;
      },

      resetPaginationRequest : function(){
        this._paginationRequestPending = false;
      },

      isPaginationPending : function(){
        return this._paginationRequestPending;
      },

      onAllInternalEvents: function(ev, arg1, arg2) {
        if (ev === "pagination:change"){
          this.resetPaginationRequest()
        }
        else if (ev === "dataRequest") {
          if (this.isPaginationPending()){
            return
          }
          var start = arg1;
          var rows = arg2;
          var q = this.getCurrentQuery().clone();
          q.unlock();
          q = this.composeQuery(this.defaultQueryArguments, q);

          q.set("start", start);
          q.set("rows", rows)

          var req = this.composeRequest(q);
          if (req) {
            this.setPaginationRequestPending();
            this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
          }
          if (this.mainResults){
            //letting other interested widgets know that more info was fetched
            //i.e. there was a pagination event
            this.pubsub.publish(this.pubsub.CUSTOM_EVENT, {event: "pagination", data: {start: start, rows: rows}});
          }
        }
      }
    });

    // add mixins
    _.extend(ListOfThingsWidget.prototype, LinkGenerator);
    _.extend(ListOfThingsWidget.prototype, WidgetPaginationMixin)

    return ListOfThingsWidget;
  });
