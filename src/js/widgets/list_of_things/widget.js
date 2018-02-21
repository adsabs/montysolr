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
    'js/components/api_feedback',
    'js/widgets/base/base_widget',
    'hbs!js/widgets/list_of_things/templates/item-template',
    'hbs!js/widgets/list_of_things/templates/results-container-template',
    'js/mixins/add_stable_index_to_collection',
    './model',
    './paginated_view'
  ],

  function (Marionette,
    Backbone,
    ApiRequest,
    ApiQuery,
    ApiFeedback,
    BaseWidget,
    ItemTemplate,
    ResultsContainerTemplate,
    PaginationMixin,
    PaginatedCollection,
    PaginatedView
    ) {


    var ListOfThingsWidget = BaseWidget.extend({
      initialize: function (options) {
        options = options || {};

        _.defaults(options, _.pick(this, ['view', 'collection', 'pagination', 'model', 'description', 'childView']));

        //widget.reset will restore these default pagination settings
        //for now, it doesn't make sense to pass them as options
        //since localStorage perPage will override it anyway

        //this functions as model.defaults while allowing the inheriting
        //widgets to provide their own models with their own defaults
        this.pagination = {
          pagination: true,
          //default per page : 25
          perPage: 25,
          numFound: undefined,
          currentQuery: undefined,
          start: 0,
          pageData: undefined
        };

        options.collection = options.collection || new PaginatedCollection();

        if (!options.view) {

          //operator instructs view to show a link that has citations:(bibcode) or something similar
            options.view = new PaginatedView({
              collection: options.collection,
              model: options.model,
              childView : options.childView
            });
        }

        options.view.model.set(this.pagination, {silent: true});
        options.view.model.set({
              //for the template button that opens search in search results page
                sortOrder : options.sortOrder,
                removeSelf : options.removeSelf,
                queryOperator: options.queryOperator,
                description: options.description
              }, {silent : true});

        _.extend(this, {model : options.view.model, view : options.view });

        // this is the hidden collection (just to hold data)
        this.hiddenCollection = new PaginatedCollection();

        // XXX:rca - start using modelEvents, instead of all....
        this.listenTo(this.hiddenCollection, "all", this.onAllInternalEvents);
        this.listenTo(this.view, "all", this.onAllInternalEvents);
        this.on("all", this.onAllInternalEvents);

        BaseWidget.prototype.initialize.call(this, options);

      },

      //this must be extended by inheriting widgets to listen to display events
      activate: function (beehive) {

        this.setBeeHive(beehive);
        _.bindAll(this, ["updatePaginationPreferences"]);

        this.getPubSub().subscribe(this.getPubSub().USER_ANNOUNCEMENT, this.updatePaginationPreferences);

        if (this.getBeeHive().getObject("User") && this.getBeeHive().getObject("User").getLocalStorage ){
          var perPage = this.getBeeHive().getObject("User").getLocalStorage().perPage;
          if (perPage){
            //set the pagination perPage value to whatever is in local storage,
            //otherwise it will be the default val from the initialize function
            this.pagination.perPage = perPage;
            this.model.set(this.pagination);
          }
        }
      },

      updatePaginationPreferences : function(event, data){
        if (event == "user_info_change" && data.perPage && data.perPage !== this.pagination.perPage ){
          //update per-page value
          this.updatePagination({perPage : data.perPage});
        }
      },

      /**
       * Get the current query from either our own apiResponse or from
       * the application local storage
       *
       * @param {ApiResponse} apiResponse - the response from the api
       * @returns {string} - the query string
       * @private
       */
      _getCurrentQueryString: function (apiResponse) {
        var q = '';
        var res = (apiResponse)
          ? apiResponse
          : this.getBeeHive().getObject('AppStorage').getCurrentQuery();

        // check for simbids
        if (!_.isUndefined(res)) {
          q = res.getApiQuery().get('q');

          // if there is a simbid, look to see if there is a translated string
          if (_.isEmpty(q) || q[0].indexOf('simbid') > -1) {
            q = [res.get('responseHeader.params.__original_query')];
          }
        }

        return q;
      },

      processResponse: function (apiResponse) {

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
          this.view.model.set('query', false);
        } else {
          this.view.model.set('query', this._getCurrentQueryString(apiResponse));
        }

        // XXX:rca - hack, to be solved later
        this.trigger('page-manager-event', 'widget-ready',
          {numFound: apiResponse.has("response.numFound")
            ? apiResponse.get("response.numFound")
            : this.hiddenCollection.length});

        //finally, loading view (from pagination template) can be removed or added

        if (//for pages other than the last page
            (this.model.get("perPage") === this.collection.length) ||
           //when this is satisfied, all pages are found
            (this.hiddenCollection.length === apiResponse.get("response.numFound"))
        ) {
          this.model.set("loading", false);
        }
        else {
          this.model.set("loading", true);
        }
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

        // this information is important for calculation of pages
        var numFound = apiResponse.get("response.numFound") || 0;
        var perPage =  this.model.get('perPage') || (q.has("rows") ? q.get('rows')[0] : 10);
        var start = this.model.get("start") || 0;

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

        var pageData = this._getPaginationData( page, perPage, numFound);

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

    /*
    * data for the page numbers template at the bottom
    * */
      _getPaginationData: function(page,  perPage, numFound) {

        //page is zero indexed
        return {
          //copying this here for convenience
          perPage : perPage,
          totalPages: Math.ceil(numFound/perPage),
          currentPage : page + 1,
          previousPossible : page > 0,
          nextPossible : (page + 1) * perPage < numFound
        };
      },

      processDocs: function(apiResponse, docs, paginationInfo) {
        if (!apiResponse.has('response')) return [];
        var params = apiResponse.get("response");
        var start = params.start || (paginationInfo.start || 0);
        docs = PaginationMixin.addPaginationToDocs(docs, start);
        return docs;
      },

      defaultQueryArguments: {
        fl: 'id',
        start : 0
      },

      /*
      * right now only perPage value can be updated by list of things
      * */

      updateLocalStorage : function(options){
        //if someone has selected perPage, save it in to localStorage
        if (options.hasOwnProperty("perPage") && _.contains([25, 50, 100], options.perPage)){
          this.getBeeHive().getObject("User").setLocalStorage({ perPage : options.perPage });
          console.log("set user's page preferences in localStorage: " + options.perPage);
        }
        //updatePagination will be called after localStorage triggers an event
      },

      updatePagination: function(options) {

        var numFound = options.numFound || this.model.get('numFound');
        var currentQuery = options.currentQuery || this.model.get('currentQuery') || new ApiQuery();
        var perPage = options.perPage || this.model.get('perPage');

        //page is zero indexed! so 0 == page 1, etc
        var page;
        //if someone is changing the # of records per page,
        // take them back to first page to prevent confusion
        if (options.perPage && !options.hasOwnProperty("page")) options.page = 0;
        if (options.page !== undefined){

          //validate page
          //which should be a zero-indexed integer
          if (options.hasOwnProperty("page") && (!_.isNumber(options.page) || isNaN(options.page) ) ){
            //raise an error
            var pubsub = this.getPubSub();
            pubsub.publish(pubsub.ALERT, new ApiFeedback({
              code: ApiFeedback.CODES.ALERT,
              msg: "Please enter a page number between " + 1 + " and " + Math.ceil(numFound/perPage),
              type: "danger",
              modal : true
            }));
            return
          }
          //if number is too high or too low, set it to max allowed
          else if (options.page < 0){ page = 0 }
          else if (options.page > Math.ceil(numFound/perPage) -1 ) { page = Math.ceil(numFound/perPage) -1 }
          else { page = parseInt(options.page) }

        }
        else {
          page = null
        }

        var start = this.getPageStart(page, perPage, numFound);

        // click to go to another 'page' will skip this
        if (page === null && this.collection.length) {
          var resIdx = this.collection.models[0].get('resultsIndex');
          page = PaginationMixin.getPageVal(resIdx, perPage);
        }
        if (page === null) {
          page = PaginationMixin.getPageVal(this.model.get('start'), perPage);
        }

        var pageData = this._getPaginationData( page,  perPage, numFound);
        var showRange = [page*perPage, (page*perPage)+perPage-1];

        //this needs to be updated as a default
        this.pagination.perPage = perPage;

        this.model.set({
          start: start,
          perPage: perPage,
          page: page,
          numFound: numFound,
          pageData: pageData,
          currentQuery: currentQuery,
          showRange: showRange
        });

        //force a re-render of parent container
        // since the page value might have been greater or less than allowed
        //without the model changing (since values are adjusted)

        this.view.render();

        this.hiddenCollection.showRange(showRange[0], showRange[1]);
        this.collection.reset(this.hiddenCollection.getVisibleModels());

        //finally, scroll back to top
        document.body.scrollTop = document.documentElement.scrollTop = 0;

      },

      onAllInternalEvents: function(ev, arg1, arg2) {

        //for testing, allow widget to not have been activated
        try {
          var pubsub = this.getPubSub();
        } catch(e){
        }

        if (ev === "pagination:changePerPage"){
          this.updateLocalStorage({perPage: arg1});
        }
        else if (ev === "pagination:select") {
          return this.updatePagination({page: arg1});
        }
        else if (ev === 'show:missing') {
          _.each(arg1, function(gap) {
            var numFound = this.model.get('numFound');
            var start = gap.start;
            var perPage = this.model.get('perPage');

            if (start >= numFound)  return; // ignore this

            var q = this.model.get('currentQuery').clone();
            q.set('__fetch_missing', 'true');
            q.set('start', start);
            //larger row numbers were causing timeouts
            q.set('rows', 25);
            var req = this.composeRequest(q);

            //allows widgets to override if necessary
            this.executeRequest(req);

          }, this);

        }
        else if (ev == "childview:toggleSelect") {
          pubsub.publish(pubsub.PAPER_SELECTION, arg2.data.identifier);
        }
      },

      executeRequest : function(req){
        this.getPubSub().publish(this.getPubSub().EXECUTE_REQUEST, req);
      },

      reset: function() {
        this.collection.reset();
        this.hiddenCollection.reset();
        //reset the model, favoring values in this.pagination
        this.model.set(_.defaults({
          currentQuery: this.getCurrentQuery(),
          query: false
        }, this.pagination, this.model.defaults()));
      }

    });

    _.extend(ListOfThingsWidget.prototype, PaginationMixin);

    return ListOfThingsWidget;
  });
