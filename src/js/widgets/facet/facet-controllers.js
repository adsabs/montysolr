define(['backbone', 'marionette', 'js/components/api_query', 'js/components/api_request', './views/facet-item-views',
  'js/widgets/multi_callback/paginated_multi_callback_widget', 'js/components/paginator'],
  function (Backbone, Marionette, ApiQuery, ApiRequest, FacetItemViews, PaginatedMultiCallbackWidget, Paginator) {//needed for the hierarchical processResponse

  //XXX:rca - this should really be separated into two files
  // - basic_facet_widget
  // - hierarchical_widget

  var BaseFacetWidget = PaginatedMultiCallbackWidget.extend({

    initialize: function (options) {

      options = options || {};

      //you can override numbering for facets if you need to by passing in options

      this.start = 0, this.rows = 15;

      //telling widget how to access pagination variables
      this.startName = "facet.offset";
      this.rowsName = "facet.limit";

      try {
        this.view = options.view;
        this.collection = options.view.collection;
        this.facetName = options.facetName;
      }

      catch (e) {
        // XXX:rca - will fail only if options.view.collection is missing - yo ushould test explicitly for undefined objects
        throw new Error("Facet widget controller is missing one or more key configuration variables")
      }

      this.solrPath = options.solrPath || "facet_counts.facet_fields";

      // XXX:rca - why would I be passing the function in options? we should use .extend() for that
      if (options.processResponse) {
        this.processResponse = options.processResponse
      }

      //for minor pre-processing that takes place in the default processResponse
      if (options.extractFacets) {
        this.extractFacets = options.extractFacets
      }

      //deliver info to pubsub after one of two main submit events (depending on facet type)
      this.listenTo(this.view, "changeApplySubmit", this.deliverApplySubmitQuery);

      this.listenTo(this.view, "SelectLogicSubmit", this.deliverSelectLogicSubmitQuery);

      this.listenTo(this.view, "moreDataRequested", this.dispatchFollowUpRequest)

      PaginatedMultiCallbackWidget.prototype.initialize.call(this, options)
    },

    /*
    If you look into the super class's dispatchRequest() you will
    see it is doing the same thing as this method. This begs the question:

    why 'dispatchBasicRequest() exists? its code should be inside 'dispatchRequest'
      instead

     */
    dispatchRequest: function (apiQuery) {

      //resetting pagination because this function is called
      //in response to a new query
      if (this.paginator) {
        this.paginator.reset();

      }
      this.setCurrentQuery(apiQuery);

      this.dispatchBasicRequest()

      this.collection.needsReset = true;

    },

    // XXX:rca - pls try to remove (refactor)
    dispatchFollowUpRequest: function () {
      console.log("dispatching follow up request")

      this.dispatchBasicRequest();
    },

    /*basic facet requests.
     decides which parameters to stick in the query, then calls
     this.composeRequest to request the data and return an id that
     will store the callback in this.queriesInProgress
     */
    dispatchBasicRequest   : function () {

      var id, req, customQuery;

      // XXX:rca - my previous boss called these things 'Bulgarian constants'
      // i don't know why, but it meant some 'hardcoded variables' that pretend
      // to be constants ;-) Maybe you should put them into Paginator and
      // make clear it is a constant
      if (this.solrPath === "facet_counts.facet_fields") {
        var d = {
          "facet"         : "true",
          "facet.field"   : this.facetName,
          "facet.mincount": "1"
        };
        if (this.paginator) {
          _.extend(d, this.paginator.run())

        }
        customQuery = this.composeQuery(d)

      }
      else {
        //assuming that info is just in metadata
        customQuery = this.composeQuery();
      }
      id = customQuery.url();


      //it's responding to INVITING_REQUEST or requesting moreinfo, so just do default information request
      this.registerCallback(id, this.processResponse, {
        view: this.view
      });

      req = this.composeRequest(customQuery);
      if (req) {
        console.log("issuing Request", customQuery.url())
        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
      }
    },

    // XXX:rca - I'm trying to understand what it does: 'deliverApply' ? :-)
    // shall we try some less cryptic method names? eg. onApplySelection

    deliverApplySubmitQuery: function () {

      var changed, currentFQ, finalFQ, newQuery, facetQuery;

      currentFQ = this.getCurrentQuery().get("fq");

      //get the item that has changed
      changed = this.view.collection.filter(function (x) {
        return x.get("newValue") !== undefined
      });

      changed = changed[0].get("newValue");

      //no logic, so it's a single facet
      facetQuery = this.facetName + ":[" + changed.join(" TO ") + "]"

      currentFQ ? finalFQ = currentFQ + " AND " + facetQuery : finalFQ = facetQuery;

      newQuery = this.composeQuery({
        fq: finalFQ
      });

      this.pubsub.publish(this.pubsub.NEW_QUERY, newQuery);

    },

    // XXX:rca - dtto
    deliverSelectLogicSubmitQuery: function () {

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

        facetQuery = "-" + this.facetName + ":(" + "\"" + selected.join(" AND ") + "\")"
      }
      else if (logic === "limit to") {
        facetQuery = this.facetName + ":(\"" + selected + "\")"
      }
      else {
        //it's "and" or "or"
        facetQuery = this.facetName + ":(\"" + selected.join("\" " + logic.toUpperCase() + " \"") + "\")"
      }

      currentFQ ? finalFQ = currentFQ + " AND " + facetQuery : finalFQ = facetQuery;

      newQuery = this.composeQuery({
        fq: finalFQ
      });

      this.pubsub.publish(this.pubsub.NEW_QUERY, newQuery);

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
    processResponse: function (apiResponse, data) {

      // TODO: pass collection data (instead of the view)
      view = data.view

      //starting assumption is that the collection could fetch more facets if it wanted to

      var coll = view.collection;
      coll.moreFacets = true;

      var facets = apiResponse.get(this.solrPath + "." + this.facetName);
      var facetsCol = [];
      var preprocess = coll.preprocess

      if (this.extractFacets) {
        facets = this.extractFacets(facets)
      }

      _.each(facets, function (f, i) {
        if (i % 2 === 0) {

          /*if it's hierarchical, drop solr data points that aren't at this level
           (I don't know why they are included)
           also drop facets that occur 0 times (this is mainly for hierarchical facets)*/
          if ((typeof coll.level === "number" && coll.level !== parseInt(f.match(/^\s*(\d)\//)[1]))) {
            return
          }

          if (facets[i + 1] === 0) {
            //if we are getting zero facets, there are no more
            coll.moreFacets = false;
            return
          }

          //first, applying text preprocessing to facet
          if (preprocess && Array.isArray(preprocess)) {
            _.each(preprocess, function (p) {
              t = this.collection[p](f)
            }, this);
          }
          else if (preprocess && typeof preprocess === "string") {
            t = this.collection[preprocess](f)
          }
          else {
            t = f
          }

          var d = {
            title: " " + t + " (" + facets[i + 1] + ")",
            //totally un-touched original value
            //prefacing slashes removed for when val is used in faceting
            value: f
          };

          //in case of hierarchical vals, also provide val without slashes
          if (_.contains(preprocess, "removeSlash")) {
            d.valWithoutSlash = this.collection["removeSlash"](f)
          }

          facetsCol.push(d)

        }
      }, this);

      //if we've reached the end of the hierarchy, change the itemview for the parent view
      if (data.lastLevel === true) {

        view.itemView = FacetItemViews.CheckboxOneLevelView;
      }

      if (coll.needsReset) {
        console.log("resetting the collection")
        coll.reset(facetsCol);
        coll.needsReset = false;

      }
      else {
        coll.add(facetsCol);
        //not sure if necessary
        coll.trigger("re-rendering")
      }

    }


  });

  // has extra methods for requesting child data of facets
  var HierarchicalFacetWidget = BaseFacetWidget.extend({

    initialize: function (options) {

      _.bindAll(this, "updateChildCollection");
      BaseFacetWidget.prototype.initialize.call(this, options);

      //change facet name to reflect hierarchical nature
      this.nonHierFacetName = this.facetName;
      this.facetName = this.facetName + (options.facetSuffix || "_facet_hier");

      //this will always match hierarchical requests for data, no matter the level
      this.listenTo(this.view, "hierarchicalDataRequest", this.requestChildData);

      //telling the first-level collection which level it is
      this.view.collection.level = 0;

      this.levelDepth = options.levelDepth;

    },

    updateChildCollection: function (apiResponse, view, extra) {
      // XXX:rca - the same issue as the processResponse above
      this.processResponse(apiResponse, view, extra)

    },
    //only for hierarchical facets
    requestChildData     : function () {
      var view, valWithoutSlash, level;
      view = arguments[arguments.length - 1];

      valWithoutSlash = view.collection.removeSlash(view.model.get("value"));
      level = view.model.collection.level;

      nextLevel = parseInt(level) + 1;

      view.collection.preprocess = this.collection.preprocess;
      view.collection.level = nextLevel

      //add this collection to this.childCollections
      if (!this.childCollections) {
        this.childCollections = {};
      }

      if (!this.childCollections[view.cid]) {
        this.childCollections[view.cid] = {collection: view.collection,
          paginator : new Paginator({rows: 5,
            startName : "facet.offset",
            rowsName : "facet.limit"})
        }
      }

      view.listenTo(view.collection, "reset", this._renderChildren)

      params = {};

      params["q"] = this.getCurrentQuery().get("q") + " " + this.nonHierFacetName + ":\"" + valWithoutSlash + "\"";
      params["facet.field"] = this.facetName;
      params["facet.prefix"] = nextLevel + "/" + valWithoutSlash;
      params["facet"] = "true";
      params["fl"] = "title"
      params = _.extend(params, this.childCollections[view.cid].paginator.run());
      if (nextLevel == this.levelDepth) {
        this.dispatchSpecialRequest(params, this.updateChildCollection, {
          view     : view,
          lastLevel: true
        })

      }
      else {
        this.dispatchSpecialRequest(params, this.updateChildCollection, {
          view: view
        })
      }
    }
  });

  var FacetControllers = {
    BaseFacetWidget        : BaseFacetWidget,
    HierarchicalFacetWidget: HierarchicalFacetWidget
  };

  return FacetControllers

});
