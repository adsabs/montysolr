

define(['backbone',
        'marionette',
    'js/components/api_query',
    'js/components/api_request',
    'js/widgets/base/paginated_multi_callback_widget',
    'js/components/paginator',
    'js/mixins/widget_pagination',
    'js/components/api_query_updater'],
  function (Backbone,
            Marionette,
            ApiQuery,
            ApiRequest,
            PaginatedMultiCallbackWidget,
            Paginator,
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

        this.responseProcessors = options['responseProcessors'] || this.responseProcessors || [];

      },


      _dispatchRequest: function (apiQuery, data) {
        var q = this.customizeQuery(apiQuery);
        data = data || {};
        if (q) {
          var qid = q.url();
          this.registerCallback(qid, this.processFacetResponse, {collection: data.collection || this.collection, view: data.view || this.view});
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

        var query = apiResponse.getApiQuery();
        var paginator = this.findPaginator(query).paginator;

        if (this.setCurrentQuery) {
          this.setCurrentQuery(query);
        }

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
            modified: modifiedValue,
            children: []
          };

          facetsCol.push(d)

        };

        // set into the nested hierarchical view the query that was used to get the data
        // will be needed for paging
        if (view.setCurrentQuery) {
          view.setCurrentQuery(query);
        }

        coll.add(facetsCol);
        
        // check whether we were fetching more data or we were getting fresh data
        if (paginator.getCycle() <= 1) {

          paginator.setMaxNum(apiResponse.get('response.numFound')); // this is not useful, cuz facets have a different counter
        }

        if (facetsCol.length > 0) { // we got a full batch (so we'll assume there is more)
          view.enableShowMore();
        }
        else {
          view.disableShowMore();
        }
      },

      getPreprocessorChain: function() {
        if (this._preprocessor) {
          return this._preprocessor;
        }
        var func;
        if (_.isArray(this.responseProcessors)) {
          func = _.compose.apply(_, this.responseProcessors);
        }
        else if (typeof this.responseProcessors === 'function') {
          func = this.responseProcessors;
        }
        else {
          func = function(v) {return v};
        }
        this._preprocessor = func;
        return func;
      },


      //deliver info to pubsub after one of two main submit events (depending on facet type)
      onAllInternalEvents: function(ev, arg1, arg2) {
        //console.log(ev);
        if (ev.indexOf("fetchMore") > -1) {
          var numOfLoadedButHidden = arguments[arguments.length-2];
          var data = arguments[arguments.length-1];
          var paginator, view, collection, q;
          if (data && data.view) {
            q = data.query || this.getCurrentQuery();
            view = data.view;
            collection = data.collection;
            paginator = this.findPaginator(q).paginator;
          }
          else {
            numOfLoadedButHidden = data;
            q = this.getCurrentQuery();
            view = this.view;
            collection = this.collection;
            paginator = this.findPaginator(q).paginator;
          }
          var p = this.handlePagination(this.view.displayNum, this.view.maxDisplayNum, numOfLoadedButHidden, paginator, view, collection);
          if (p && p.before) {
            p.before();
          }
          if (p && p.runQuery) {
            this._dispatchRequest(q, {collection: collection, view: view});
          }
        }
        else if (ev.substring(ev.length-20) == 'itemview:itemClicked') {
          var view = arguments[arguments.length-1];
          this.handleConditionApplied(view.model);
        }
        else if (ev.substring(ev.length-20) == 'itemview:treeClicked') { // hierarchical view
          var view = arguments[arguments.length-1];
          this.handleConditionApplied(view.model);
        }
        else if (ev.indexOf('treeNodeDisplayed') > -1) {
          var view = arguments[arguments.length-1];
          this.handleTreeExpansion(view); // see if we need to fetch deeper data
        }
        else if (ev == 'containerLogicSelected') {
          this.handleLogicalSelection(arg1);
        }
      },


      handleTreeExpansion: function(view) {

        //XXX:rca - this is a hack
        view.displayNum = this.displayNum;

        var model = view.model;
        var children = view.collection;

        if (!children || children._finished) {
          return; // do nothing
        }

        // we need to fetch data one level deeper
        var q = this.getCurrentQuery();
        q = q.clone();

        q = this.composeQuery(_.clone(this.defaultQueryArguments), q);

        var val = model.get('value');

        if (!val) {
          return; // nothing to do
        }

        var elems = val.split('/');
        var nextLevel = parseInt(elems[0]) + 1;

        q.set('facet.prefix', nextLevel + "/" + elems.slice(1).join('/'));

        var self = this;
        var paginator = this.findPaginator(q).paginator;

        q = paginator.run(q);
        this.registerCallback(q.url(), function(apiResponse) {
          self.processFacetResponse(apiResponse, {view: view, collection: children});
        });

        var req = this.composeRequest(q);
        if (req) {
          this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
          setTimeout(function() {
            if (children.length == 0) {
              view.triggerMethod('NoMoreDataOnThisLevel');
            }
          }, 5000);
        }


        children._finished = true;
      },

      handleConditionApplied: function(model) {

        var q = this.getCurrentQuery();

        if (this.view.logicOptions) { // we'll just save the values and wait for the operator

          var selectedItems = this.queryUpdater.getTmpEntry(q, 'SelectedItems', {});
          if (model.get('selected')) {
            selectedItems[model.cid] = model.get('value');
          }
          else {
            delete selectedItems[model.cid];
          }
        }
        else {

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
        }
      },

      handleLogicalSelection: function(operator) {
        var q = this.getCurrentQuery();
        var paginator = this.findPaginator(q).paginator;
        var conditions = this.queryUpdater.getTmpEntry(q, 'SelectedItems');
        var self = this;

        if (conditions && _.keys(conditions).length > 0) {
          conditions = _.values(conditions);
          _.each(conditions, function(c, i, l) {
            l[i] = self.queryUpdater.escapeInclWhitespace(c);
          });

          q = q.clone();

          // XXX:rca - extend the updater to support 'exclude' and 'OR' operations
          if (operator == 'and' || operator == 'limit to') {
            this.queryUpdater.updateQuery('q', q, conditions, 'OR', 'add');
          }

          this.dispatchNewQuery(paginator.cleanQuery(q));
        }
      },

      // XXX:rca - these were the old Alex's methods; i'll remove them once it is
      // clear all functionality has been incorporated...
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