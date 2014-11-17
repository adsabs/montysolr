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
        this.hiddenCollection.reset(null, {silent: true});
        //this.resetView();
      },

      onDisplayDocuments: function(apiQuery) {
        this.view.model.set("currentQuery", apiQuery);
        BaseWidget.prototype.dispatchRequest.call(this, apiQuery);
      },

      processResponse: function (apiResponse) {
        //this.resetView();

        var q = apiResponse.getApiQuery();

        var docs = apiResponse.get("response.docs");
        docs = _.map(docs, function(d) {
          d.identifier = d.bibcode;
          return d;
        });

        var pagination = this.getPaginationInfo(apiResponse, docs);
        docs = this.processDocs(apiResponse, docs, pagination);

        if (docs.length) {
          this.hiddenCollection.add(docs, {merge: true});
          this.model.set(pagination);

          //if (pagination.showMore) {
          //  this.hiddenCollection.showMore(pagination.showMore);
          //}
          this.hiddenCollection.showRange(pagination.start, pagination.start + pagination.perPage);
          this.view.collection.reset(this.hiddenCollection.getVisibleModels());

        }

        // XXX:rca - hack, to be solved later
        this.trigger('page-manager-event', 'widget-ready',
          {numFound: apiResponse.get("response.numFound"), widget: this});
      },

      getPaginationInfo: function(apiResponse, docs) {
        var q = apiResponse.getApiQuery();
        var toSet = {
          "numFound":  apiResponse.get("response.numFound"),
          "currentQuery":q
        };

        //checking to see if we need to reset start or rows values
        var perPage =  q.get("rows") || this.model.get('perPage');
        var start = q.get("start") || this.model.get('start');

        if (perPage){
          perPage = _.isArray(perPage) ? perPage[0] : perPage;
          toSet.perPage = perPage;
        }

        if (_.isNumber(start) || _.isArray(start)){
          start = _.isArray(start) ? start[0] : start;
          toSet.page = PaginationMixin.getPageVal(start, perPage);
        }
        else {
          toSet.page = 0;
        }
        toSet.start = start;

        var numVisible = this.hiddenCollection.getNumVisible();
        var showMore = 0;
        if (numVisible == 0) {
          showMore = perPage;
        } else if(numVisible % perPage !== 0) {
          showMore = numVisible % perPage;
        }
        toSet.showMore = showMore;

        // create pagination navigation links
        var pageNums = PaginationMixin.generatePageNums(toSet.page, 3);
        pageNums = PaginationMixin.ensurePagePossible(pageNums, toSet.perPage, toSet.numFound);

        var pageData = {};
        //only render pagination controls if there are more pages
        if (pageNums.length > 1) {
          //now, finally, generate links for each page number
          var pageData = _.map(pageNums, function (n) {
            var baseQ = q.clone();
            var s = PaginationMixin.getStartVal(n.p, perPage);
            baseQ.set("start", s);
            baseQ.set("rows", perPage);
            n.start = s;
            n.perPage = perPage;
            n.link = baseQ.url();
            return n;
          }, this);
        }
        toSet.pageData = pageData;

        //should we show a "back to first page" button?
        toSet.showFirst = (_.pluck(pageNums, "p").indexOf(1) !== -1) ? false : true;

        return toSet;
      },

      processDocs: function(apiResponse, docs, paginationInfo) {
        return PaginationMixin.addPaginationToDocs(docs, apiResponse.get("responseHeader.params.start"));
      },


      defaultQueryArguments: {
        fl: 'id',
        rows : 10,
        start : 0
      },



      onAllInternalEvents: function(ev, arg1, arg2) {
        if (ev === "pagination:change"){
          console.log('need to recompute', arg1);
        }
        else if (ev === "pagination:select") {
          console.log('need to request data', arg1);
          var pageData = _.findWhere(this.model.attributes.pageData, {p: arg1});
          if (pageData) {
            var start = pageData.start;
            var perPage = pageData.perPage;
            this.hiddenCollection.showRange(start, start+perPage);
          }
        }
        else if (ev === 'show:missing') {
          console.log('we have to retrieve new data');
          // TODO: show spinning wheel?? (we could do it from the template)
          _.each(arg1, function(gap) {
            var q = this.getCurrentQuery().clone();
            q.set('start', gap.start);
            q.set('rows', this.model.get('perPage'));
            BaseWidget.prototype.dispatchRequest.call(this, q);
          }, this);

        }
      },

      resetView: function() {
        //var $el = this.view.$el;
        //this.view.$itemViewContainer.empty();
        //this.view.closeChildren();

        //this.view.close();
        //this.view = new this.view.constructor({hiddenCollection: this.options.collection, model: this.options.model});
        //this.view.$el = $el;
        //this.view.render();
        //this.options.view = this.view;
      }
    });

    _.extend(ListOfThingsWidget.prototype, PaginationMixin);

    return ListOfThingsWidget;
  });
