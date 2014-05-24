

define(['backbone', 'marionette', 'js/components/api_query', 'js/components/api_request', './item_views',
    'js/widgets/base/paginated_multi_callback_widget', 'js/components/paginator'],
  function (Backbone, Marionette, ApiQuery, ApiRequest, FacetItemViews, PaginatedMultiCallbackWidget, Paginator) {

    var BaseFacetWidget = PaginatedMultiCallbackWidget.extend({

      initialize: function (options) {

        options = options || {};
        this._checkStandardWidgetOptions(options);

        this.view = options.view;
        this.collection = options.view.collection;
        this.facetField = options.facetField;

        //telling widget how to use pagination variables
        this.start = 0, this.rows = 15;
        this.startName = "facet.offset";
        this.rowsName = "facet.limit";


        this.solrPath = options.solrPath || "facet_counts.facet_fields";
        if (this.solrPath === "facet_counts.facet_fields") {
          this.requestParams = function () {
            return  {"facet": "true",
              "facet.field": this.facetField,
              "facet.mincount": "1"
            }
          }
        }

        //deliver info to pubsub after one of two main submit events (depending on facet type)
        this.listenTo(this.view, "changeApplySubmit", this.onFacetApplySubmit);
        this.listenTo(this.view, "containerLogicSelected", this.onContainerLogicSelected);
        this.listenTo(this.view, "moreDataRequested", this.onMoreDataRequested);

        PaginatedMultiCallbackWidget.prototype.initialize.call(this, options)
      },


      dispatchRequest: function (apiQuery) {
        var q = this.customizeQuery(apiQuery);
        if (q) {
          var qid = q.url();
          this.registerCallback(qid, this.processFacetResponse, {collection: this.collection});
          var req = this.composeRequest(q);
          if (req) {
            this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
            this.collection.needsReset = true; //XXX:rca ???
          }
        }
      },

      /*takes raw solr info, returns an array of dicts suitable for
       adding to a collection. Useful for main queries and hierarchical queries.
       This is just the basic version for checkbox views, otherwise override*/

      /*
       XXX:rca - so this is VERY confusing - if you follow the chain of inheritance, you will
       see that the BasicWidget registers 'processResponse' method as a method
       that communicates with PubSub; but here you are using the same method to pass additional
       data (it will never be called with this data by PubSub)

       I propose that you always keep the signature (and if we need to change the method signature
       we change it everywhere (this is just to keep our sanity - api is a 'contract' and javascript
       is loose; so we must be careful)

       ...and rename this method to something else (it should be used only inside the widget;
       it is not facing PubSub)

       */
      processFacetResponse: function (apiResponse, data) {
        //starting assumption is that the collection could fetch more facets if it wanted to

        var query = apiResponse.getApiQuery();
        var fField = query.get('facet.field');

        if (!fField) {
          throw Error('The query contains no facet.field parameter!');
        }

        var coll = data.collection;
        var facetPath = "facet_counts.facet_fields." + fField;

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

          // XXX:rca i don't believe this is correct (it must depend on the query)

          /*if it's hierarchical, drop solr data points that aren't at this level
           (I don't know why they are included)
           also drop facets that occur 0 times (this is mainly for hierarchical facets)*/
          if (typeof coll.level === "number" && fValue.indexOf('/') > -1 && coll.level !== parseInt(fValue.split('/')[0])) {
            return;
          }

          var modifiedValue = preprocessorChain.call(this, fValue);
          var d = {
            title: " " + modifiedValue + " (" + fNum + ")",
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

        if (coll.needsReset) {
          console.log("resetting the collection")
          coll.reset(facetsCol);
          coll.needsReset = false;
          //view.triggerMethod("composite:collection:reset:add")

        }
        else {
          coll.add(facetsCol);
          console.log(coll.length);
          //view.triggerMethod("composite:collection:reset:add")
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


      onMoreDataRequested: function () {
        throw new Error('wtf!');
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

    return BaseFacetWidget

  });