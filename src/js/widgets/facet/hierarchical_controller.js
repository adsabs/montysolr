define(['backbone', 'marionette', 'js/components/api_query',
    'js/components/api_request',  './base_controller', 'js/components/paginator'],
  function (Backbone, Marionette, ApiQuery, ApiRequest, BaseFacetController,  Paginator ){

// has extra methods for requesting child data of facets
var HierarchicalFacetController = BaseFacetController.extend({

  initialize: function (options) {

    _.bindAll(this, "updateChildCollection");

    BaseFacetController.prototype.initialize.call(this, options);

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
        paginator : new Paginator({rows: 15,
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
      this.dispatchSupplementalRequest(params, this.updateChildCollection, {
        view     : view,
        lastLevel: true
      })

    }
    else {
      this.dispatchSupplementalRequest(params, this.updateChildCollection, {
        view: view
      })
    }
  }
})

return HierarchicalFacetController

})
