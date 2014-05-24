

define(['backbone', 'marionette', 'js/components/api_query', 'js/components/api_request', './item_views',
    'js/widgets/base/paginated_multi_callback_widget', 'js/components/paginator',
     'js/mixins/widget_pagination',
     'js/components/api_query_updater'],
  function (Backbone, Marionette, ApiQuery, ApiRequest, FacetItemViews, PaginatedMultiCallbackWidget, Paginator,
    WidgetPagination,
    ApiQueryUpdater) {

    var BaseFacetWidget = PaginatedMultiCallbackWidget.extend({

      initialize: function (options) {

        options = options || {};
        this._checkStandardWidgetOptions(options);

        this.view = options.view;
        this.collection = options.view.collection;


        PaginatedMultiCallbackWidget.prototype.initialize.call(this, options)

        this.listenTo(this.view, "all", this.onAllInternalEvents);
        if (! this.defaultQueryArguments['facet.field']) {
          throw new Error("Required parameter defaultQueryArguments[facet.field] is missing");
        }
        this.facetField = this.defaultQueryArguments['facet.field'];
        this.queryUpdater = new ApiQueryUpdater(this.facetField);

      },


      _dispatchRequest: function (apiQuery) {
        var q = this.customizeQuery(apiQuery);
        if (q) {
          var qid = q.url();
          this.registerCallback(qid, this.processFacetResponse, {collection: this.collection, view: this.view});
          var req = this.composeRequest(q);
          if (req) {
            this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
          }
        }
      },

      /**
       * Default (generic) method for handling single-level facets; it knows how to page
       * through results
       *
       * @param apiResponse
       * @param data
       */
      processFacetResponse: function (apiResponse, data) {
        //starting assumption is that the collection could fetch more facets if it wanted to

        var query = apiResponse.getApiQuery();
        var paginator = this.findPaginator(query).paginator;

        var fField = query.get('facet.field');

        if (!fField) {
          throw Error('The query contains no facet.field parameter!');
        }

        var view = data.view;
        var coll = data.collection;
        var facetPath = "facet_counts.facet_fields." + fField;

        // no data for us
        if (!apiResponse.has(facetPath)) {
          coll.reset();
          return;
        }

        var facets = apiResponse.get(facetPath);
        if (this.extractFacets) {
          facets = this.extractFacets(facets);
        }

        var facetsCol = [];
        var l = facets.length;
        var fValue, fNum;
        var preprocessorChain = this.getPreprocessorChain();

        for (var i=0; i<l; i=i+2) {

          fValue = facets[i];
          fNum = facets[i+1];

          var modifiedValue = preprocessorChain.call(this, fValue);
          var d = {
            title: modifiedValue,
            value: fValue,
            count: fNum,
            modified: modifiedValue
          };

          facetsCol.push(d)

        };

        //if we've reached the end of the hierarchy, change the itemview for the parent view
        if (data.lastLevel === true) {
          view.itemView = FacetItemViews.CheckboxOneLevelView;
        }

        // check whether we were fetching more data or we were getting fresh data
        if (paginator.getCycle() <= 1) {
          coll.reset(facetsCol);
          paginator.setMaxNum(apiResponse.get('response.numFound')); // XXX:rca - facets will have a different counter
          if (paginator.maxNum > view.displayNum) {
            view.enableShowMore();
          }
          else {
            view.disableShowMore();
          }
        } else {
          //it's in response to "load more"
          coll.add(facetsCol);
        }
      },

      getPreprocessorChain: function() {
        if (this._preprocessor) {
          return this._preprocessor;
        }
        var func;
        if (_.isArray(this.preprocess)) {
          func = _.compose(this.preprocess);
        }
        else if (typeof this.preprocess === 'function') {
          func = this.preprocess;
        }
        else {
          func = function(v) {return v};
        }
        this._preprocessor = func;
        return func;
      },


      //deliver info to pubsub after one of two main submit events (depending on facet type)
      onAllInternalEvents: function(ev, arg1, arg2) {
        console.log(ev);
        if (ev === 'changeApplySubmit') {
          throw new Error('OK');
        }
        else if (ev === 'containerLogicSelected') {

        }
        else if (ev === 'moreDataRequested') {

        }
        else if (ev == "fetchMore") {
          var pag = this.findPaginator(this.getCurrentQuery());
          var p = this.handlePagination(this.view.displayNum, this.view.maxDisplayNum, arg1, pag.paginator, this.view, this.collection);
          if (p && p.before) {
            p.before();
            this._dispatchRequest(this.getCurrentQuery());
          }
        }
        else if (ev == 'itemview:itemClicked') {
          var model = arg1.model; // <- the view in question
          this.handleConditionApplied(model);
        }
      },


      handleConditionApplied: function(model) {
        var q = this.getCurrentQuery();
        var value = model.get('value');

        if (value) {
          var paginator = this.findPaginator(q).paginator;

          q = q.clone();
          value = this.queryUpdater.escapeInclWhitespace(value);
          if (model.get('selected')) {
            this.queryUpdater.updateQuery('q', q, value, 'AND', 'add');
          }
          else {
            this.queryUpdater.updateQuery('q', q, value, 'AND', 'remove');
          }

          this.dispatchNewQuery(paginator.cleanQuery(q));
        }

      },

      // XXX:rca - this should really have been inside the view
      // the view should extract the data and pass them to the
      // controller
      onFacetApplySubmit: function () {

        var changed, currentFQ, finalFQ, newQuery, facetQuery;

        currentFQ = this.getCurrentQuery().get("fq");

        //get the item that has changed
        changed = this.view.collection.filter(function (x) {
          return x.get("newValue") !== undefined
        });

        changed = changed[0].get("newValue");

        //no logic, so it's a single facet
        facetQuery = this.facetField + ":[" + changed.join(" TO ") + "]"

        currentFQ ? finalFQ = currentFQ + " AND " + facetQuery : finalFQ = facetQuery;

        newQuery = this.composeQuery({
          fq: finalFQ
        });

        this.pubsub.publish(this.pubsub.NEW_QUERY, newQuery);

      },

      // XXX:rca - dtto
      onContainerLogicSelected: function () {

        var facetQuery, selected, logic;

        var currentFQ, finalFQ, newQuery;

        currentFQ = this.getCurrentQuery().get("fq");

        //selected from basic collection
        var getSelected = function (collection) {
          return collection.where({
            selected: true
          });
        };

        selected = getSelected(this.collection);

        //selected from possible child collections
        _.each(this.childCollections, function (v, k) {
          selected.push(getSelected(v.collection))
        });

        selected = _.map(selected, function (s) {
          return s.get("value")
        });

        logic = this.view.model.get("selected")

        if (logic === "exclude") {

          facetQuery = "-" + this.facetField + ":(" + "\"" + selected.join(" AND ") + "\")"
        }
        else if (logic === "limit to") {
          facetQuery = this.facetField + ":(\"" + selected + "\")"
        }
        else {
          //it's "and" or "or"
          facetQuery = this.facetField + ":(\"" + selected.join("\" " + logic.toUpperCase() + " \"") + "\")"
        }

        currentFQ ? finalFQ = currentFQ + " AND " + facetQuery : finalFQ = facetQuery;

        newQuery = this.composeQuery({
          fq: finalFQ
        });

        this.pubsub.publish(this.pubsub.NEW_QUERY, newQuery);

      }

    });

    // add mixins
    _.extend(BaseFacetWidget.prototype, WidgetPagination);

    return BaseFacetWidget

  });