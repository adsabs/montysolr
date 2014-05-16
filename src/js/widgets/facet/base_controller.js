define(['backbone', 'marionette', 'js/components/api_query', 'js/components/api_request', './item_views',
    'js/widgets/base/paginated_multi_callback_widget', 'js/components/paginator'],
  function (Backbone, Marionette, ApiQuery, ApiRequest, FacetItemViews, PaginatedMultiCallbackWidget, Paginator) {

    var BaseFacetWidget = PaginatedMultiCallbackWidget.extend({

      initialize: function (options) {

        options = options || {};

        if (typeof options.view === "undefined" || typeof options.view.collection === "undefined" || typeof options.facetName === "undefined") {
          throw new Error("You are missing key configuration variables.")
        }
        else {
          this.view = options.view;
          this.collection = options.view.collection;
          this.facetName = options.facetName;
        }

        //telling widget how to use pagination variables
        this.start = 0, this.rows = 15;
        this.startName = "facet.offset";
        this.rowsName = "facet.limit";


        this.solrPath = options.solrPath || "facet_counts.facet_fields";
        if (this.solrPath === "facet_counts.facet_fields") {
          this.requestParams = function () {
            return  {"facet": "true",
              "facet.field": this.facetName,
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



      /**
       * By default this method does nothing
       *
       * @param apiResponse
       */
      extractFacetsData: function(apiResponse) {
        return null;
      },

      dispatchRequest: function (apiQuery) {

        PaginatedMultiCallbackWidget.prototype.dispatchRequest.call(this, apiQuery);
        this.collection.needsReset = true;

      },

      onMoreDataRequested: function () {
        var oldVal = this.collection.needsReset;
        this.dispatchRequest(this.getCurrentQuery());
        this.collection.needsReset = oldVal;
      },


      dispatchBasicRequest: function () {

        var id, req, customQuery;

        // XXX:rca - my previous boss called these things 'Bulgarian constants'
        // i don't know why, but it meant some 'hardcoded variables' that pretend
        // to be constants ;-) Maybe you should put them into Paginator and
        // make clear it is a constant

        if (this.paginator) {
          customQuery = this.composeQuery(_.extend(this.requestParams(), this.paginator.run()))
        }
        else {

          customQuery = this.composeQuery(this.requestParams())
        }

        id = customQuery.url();

        //it's responding to INVITING_REQUEST or requesting moreinfo, so just do default information request
        this.registerCallback(id, this.processResponse, {
          view: this.view
        });

        req = this.composeRequest(customQuery);
        if (req) {
          console.log("issuing Request", customQuery.url(), this.facetName)
          this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
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
        facetQuery = this.facetName + ":[" + changed.join(" TO ") + "]"

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
              value: f,
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
          view.triggerMethod("composite:collection:reset:add")

        }
        else {
          coll.add(facetsCol);
          view.triggerMethod("composite:collection:reset:add")
        }
      }





    });

    return BaseFacetWidget

  });