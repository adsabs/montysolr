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
    PaginationTemplate,
    PaginationMixin,
    PaginatedCollection,
    PaginatedView
    ) {



    var ListOfThingsWidget = BaseWidget.extend({
      initialize: function (options) {
        options = options || {};


        _.defaults(options, _.pick(this, ['view', 'collection', 'pagination', 'model', 'description']));

        var defaultPagination = {
          pagination: true,
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


        if (options.description){
          //allow the widget to describe itself at the top of its view
          options.model.set("description", options.description);
        }

        _.extend(this, _.pick(options, ['model', 'view']));

        // this is the hidden collection (just to hold data)
        this.hiddenCollection = new PaginatedCollection();

        // XXX:rca - start using modelEvents, instead of all....
        this.listenTo(this.hiddenCollection, "all", this.onAllInternalEvents);
        this.listenTo(this.view, "all", this.onAllInternalEvents);
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
        this.reset();
      },

      onDisplayDocuments: function(apiQuery) {
        BaseWidget.prototype.dispatchRequest.call(this, apiQuery);
      },

      processResponse: function (apiResponse) {
        var q = apiResponse.getApiQuery();

        var docs = this.extractDocs(apiResponse);

        var pagination = this.getPaginationInfo(apiResponse, docs);

        docs = this.processDocs(apiResponse, docs, pagination);

        if (docs && docs.length) {
          this.hiddenCollection.add(docs, {merge: true});

          if (pagination.showRange) {
            // we must update the model before updating collection because the showRange
            // can automatically start fetching documents
            this.model.set(pagination);
            this.hiddenCollection.showRange(pagination.showRange[0], pagination.showRange[1]);
          }
          this.view.collection.reset(this.hiddenCollection.getVisibleModels());
        }

        // XXX:rca - hack, to be solved later
        this.trigger('page-manager-event', 'widget-ready',
          {numFound: apiResponse.has("response.numFound")
            ? apiResponse.get("response.numFound")
            : this.hiddenCollection.length,
            widget: this});
      },

      extractDocs: function(apiResponse) {
        var docs = apiResponse.get("response.docs");
        docs = _.map(docs, function(d) {
          d.identifier = d.bibcode;
          return d;
        });
        return docs;
      },

      getPaginationInfo: function(apiResponse, docs) {
        var q = apiResponse.getApiQuery();


        // this information is important for calcullation of pages
        var numFound = apiResponse.get("response.numFound");
        var perPage =  this.model.get('perPage') || (q.has("rows") ? q.get('rows')[0] : 10);
        var start = q.has("start") ? q.get('start')[0] : 0;

        // compute the page number of this request
        var page = PaginationMixin.getPageVal(start, perPage);

        // compute which documents should be made visible
        var showRange = [page*perPage, ((page+1)*perPage)-1];


        // means that we were fetching the missing documents (to fill gaps in the collection)
        var fillingGaps = q.has('__fetch_missing');
        if (fillingGaps) {
          return {
            start: start,
            showRange: showRange
          }
        }

        // compute paginations (to be inserted into navigation)
        var numAround = this.model.get('numAround') || 2;
        var pageData = this._getPageDataDatastruct(q, page, numAround, perPage, numFound);


        //should we show a "back to first page" button?
        var showFirst = (_.pluck(pageData, "p").indexOf(1) !== -1) ? false : true;

        return {
          numFound: numFound,
          perPage: perPage,
          start: start,
          page: page,
          showRange: showRange,
          pageData: pageData,
          currentQuery: q
        }
      },

      _getPageDataDatastruct: function(q, page, numAround, perPage, numFound) {
        var pageData = {};
        var pageNums = PaginationMixin.generatePageNums(page, numAround, perPage, numFound);
        if (pageNums.length > 1) { //only render pagination controls if there are more pages
          //now, finally, generate links for each page number
          var pageData = _.map(pageNums, function (n) {
            n.start = PaginationMixin.getPageStart(n.p, perPage);
            n.end = PaginationMixin.getPageEnd(n.p, perPage);
            n.perPage = perPage;

            var baseQ = q.clone();
            baseQ.set("start", n.start+1); // solr is 1-based
            baseQ.set("rows", perPage);
            n.link = baseQ.url();
            n.p = n.p + 1; // make page nums 1-based
            return n;
          }, this);
        }
        return pageData;
      },


      processDocs: function(apiResponse, docs, paginationInfo) {
        if (!apiResponse.has('response')) return [];
        var params = apiResponse.get("response");
        var start = params.start || (paginationInfo.start || 0);
        docs = PaginationMixin.addPaginationToDocs(docs, start);
        return docs
      },


      defaultQueryArguments: {
        fl: 'id',
        rows : 10,
        start : 0
      },


      updatePagination: function(options) {
        var perPage = options.perPage || this.model.get('perPage');
        var page = _.isNumber(options.page) ? options.page : null;
        var numFound = options.numFound || this.model.get('numFound');
        var numAround = options.numAround || this.model.get('numAround') || 2;
        var currentQuery = options.currentQuery || this.model.get('currentQuery');

        // click to go to another 'page' will skip this
        if (page === null && this.collection.length) {
          var resIdx = this.collection.models[0].get('resultsIndex');
          page = PaginationMixin.getPageVal(resIdx, perPage);
        }
        if (page === null) {
          page = PaginationMixin.getPageVal(this.model.get('start'), perPage);
        }

        var pageData = this._getPageDataDatastruct(currentQuery, page, numAround, perPage, numFound);
        var showRange = [page*perPage, (page*perPage)+perPage-1];
        var showFirst = (_.pluck(pageData, "p").indexOf(1) !== -1) ? false : true;

        this.model.set({
          perPage: perPage,
          page: page,
          numFound: numFound,
          numAround: numAround,
          pageData: pageData,
          currentQuery: currentQuery,
          showRange: showRange,
          showFirst: showFirst
        });


        this.hiddenCollection.showRange(showRange[0], showRange[1]);
        this.collection.reset(this.hiddenCollection.getVisibleModels());
      },

      onAllInternalEvents: function(ev, arg1, arg2) {
        if (ev === "pagination:change"){
          this.updatePagination({perPage: arg1});
        }
        else if (ev === "pagination:select") {
          return this.updatePagination({page: arg1-1});
        }
        else if (ev === 'show:missing') {

          // TODO: show spinning wheel?? (we could do it from the template)
          _.each(arg1, function(gap) {
            var numFound = this.model.get('numFound');
            var start = gap.start;
            var perPage = this.model.get('perPage');

            if (start >= numFound)
              return; // ignore this

            var q = this.model.get('currentQuery').clone();
            q.set('__fetch_missing', 'true');
            q.set('start', start);
            q.set('rows', perPage);
            var req = this.composeRequest(q);

            //console.log('we have to retrieve new data', arg1);

            if (req) {
              this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, req);
            }
          }, this);

        }
      },

      reset: function() {
        this.collection.reset();
        this.hiddenCollection.reset();
        this.model.set({
          showDetails : false,
          pageData: {}
        })
      }

    });

    _.extend(ListOfThingsWidget.prototype, PaginationMixin);

    return ListOfThingsWidget;
  });
