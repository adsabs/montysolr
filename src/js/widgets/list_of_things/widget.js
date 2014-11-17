/**
 * This widget can paginate through a list of results. It can easily be inherited
 * by results widget ,table of contents widget, etc. It either listens to INVITING_REQUEST
 * in the case of the results widget, or the loadBibcode method (currently all other widgets).
 *
 * This widget consists of the following components:
 *
 * 1. a pagination view (you can choose from expanding view or paginated view [default])
 * 2. an associated pagination model (all pagination info is kept here and only here)
 * 3. a list view that listens to the visible records and renders them
 * 4. an item view for each record rendered repeatedly by the list view
 * 5. a controller that handles requesting and recieving data from pubsub and initializing everything
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
    './model',
    './paginated_view'
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
    PaginationMixin,
    PaginatedCollection,
    PaginatedView
    ) {



    var ListOfThingsWidget = BaseWidget.extend({
      initialize: function (options) {
        options = options || {};


        _.defaults(options, _.pick(this, ['view', 'collection', 'pagination', 'model']));

        var defaultPagination = {
          perPage: 20,
          numFound: undefined,
          currentQuery: undefined,
          start: 0
        };
        options.pagination = _.defaults(options.pagination || {}, defaultPagination);

        if (!options.collection) {
          options.collection = new PaginatedCollection();
        }
        if (!options.view) {
          if (options.model) {
            options.view = new PaginatedView({collection: options.collection, model: options.model});
          }
          else {
            options.view = new PaginatedView({collection: options.collection});
          }
        }
        options.model = options.view.model;
        options.model.set(options.pagination, {silent: true});

        _.extend(this, _.pick(options, ['model', 'collection', 'view']));

        // XXX:rca - start using modelEvents, instead of all....
        this.listenTo(this.collection, "all", this.onAllInternalEvents);
        this.on("all", this.onAllInternalEvents);

        BaseWidget.prototype.initialize.call(this, options);

      },


      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');

        _.bindAll(this, 'onStartSearch', 'onDisplayDocuments', 'processResponse');
        this.pubsub.subscribe(this.pubsub.START_SEARCH, this.onStartSearch);
        this.pubsub.subscribe(this.pubsub.DISPLAY_DOCUMENTS, this.onDisplayDocuments);
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      onStartSearch: function(apiQuery) {
        this.view.close();
        this.view = this.view.constructor({collection: this.options.collection, model: this.options.model});
        this.options.view = this.view;
      },

      onDisplayDocuments: function(apiQuery) {
        this.view.model.set("currentQuery", apiQuery);
        BaseWidget.prototype.dispatchRequest.call(this, apiQuery);
      },

      processResponse: function (apiResponse) {
        var q = apiResponse.getApiQuery();
        this.setCurrentQuery(q);

        var pagination = this.getPaginationInfo(apiResponse);

        var docs = apiResponse.get("response.docs");
        docs = _.map(docs, function(d) {
          d.identifier = d.bibcode;
          return d;
        });

        docs = this.processDocs(apiResponse, docs, pagination);

        if (docs.length) {
          //update model with pagination info
          //has to happen right before collection changes
          this.model.set(pagination, {silent : true});

          //just using add because the collection was emptied
          //when a new request was made
          this.collection.add(docs);

          this.collection.showMore(pagination.perPage);
        }
        else {
          //nothing was found, show empty view
          this.collection.reset();
        }

        // XXX:rca - hack, to be solved later
        this.trigger('page-manager-event', 'widget-ready',
          {numFound: apiResponse.get("response.numFound"), widget: this});
      },

      getPaginationInfo: function(apiResponse) {
        var q = apiResponse.getApiQuery();
        var toSet = {
          "numFound":  apiResponse.get("response.numFound"),
          "currentQuery":q
        };

        //checking to see if we need to reset start or rows values
        var rows =  q.get("rows") || this.model.get('perPage');
        var start = q.get("start") || this.model.get('start');

        if (rows){
          rows = _.isArray(rows) ? rows[0] : rows;
          toSet.perPage = rows;
        }

        if (_.isNumber(start)){
          start = _.isArray(start) ? start[0] : start;
          toSet.page = PaginationMixin.getPageVal(start, rows);
        }
        toSet.start = start;
        return toSet;
      },

      processDocs: function(apiResponse, docs, paginationInfo) {
        return PaginationMixin.addPaginationToDocs(docs, apiResponse.get("response.start"));
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

    _.extend(ListOfThingsWidget.prototype, PaginationMixin);

    return ListOfThingsWidget;
  });
