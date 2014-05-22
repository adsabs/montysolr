define([
  './model',
  './container_view',
  './logic_container_view',
  './change_apply_container_view',
  './hierarchical_item_view',
  './zoomable_graph_view',
  './item_views',
  './collection', 
  './base_controller',
  './hierarchical_controller'
], function (
  FacetModel,
  BaseContainerView, 
  SelectLogicContainerView,
  ChangeApplyContainerView,
  HierarchicalItemView,
  ZoomableGraphView,
  AdditionalViews,
  FacetCollection, 
  BaseController,
  HierarchicalController) {


  var FacetFactory = {
    makeBasicCheckboxFacet: function (options) {
      //required config
      if (!(options.facetName && options.userFacingName)) {
        throw new Error("Required configuration variables were not passed")
      }

      /*optional config variables:
       options.facetSuffix : (if you need to override "_facet_hier")
       options.preprocess (titleCase, allCaps, removeSlash)
       options.solrPath
       options.defaultNumFacets (limit facets shown to certain number other than 5)
       options.openByDefault (true/false; default false)
       options.singleLogic =[]
       options.multiLogic = []}
       options.extractFacets (a function that hands in facets and passes off
       processed facets in processResponse)
       */

      var containerModel = new SelectLogicContainerView.ContainerModelClass({
        title: options.userFacingName,
        value: options.facetName,
        singleLogic: options.singleLogic,
        multiLogic: options.multiLogic
      });

      var coll = new FacetCollection([], {
        preprocess: options.preprocess
      });

      var containerview = new SelectLogicContainerView({
        collection: coll,
        model: containerModel,
        itemView: AdditionalViews.CheckboxOneLevelView,
        defaultNumFacets: options.defaultNumFacets,
        openByDefault: options.openByDefault
      });

      var widget = new BaseController({
        view: containerview,
        facetName: options.facetName,
        extractFacets: options.extractFacets

      })

      return widget
    },

    makeHierarchicalCheckboxFacet: function (options) {
      //required config
      if (!(options.facetName && options.userFacingName)) {
        throw new Error("Required configuration variables were not passed")
      }
      ;

      /*optional config variables:
       options.facetSuffix : (if you need to override "_facet_hier")
       options.preprocess (titleCase, allCaps, removeSlash)
       options.solrPath
       options.defaultNumFacets (limit facets shown to certain number other than 5)
       options.openByDefault (true/false; default false)
       options.singleLogic =[]
       options.multiLogic = []}
       options.extractFacets (a function that hands in facets and passes off
       processed facets in processResponse)
       */

      var containerModel = new SelectLogicContainerView.ContainerModelClass({
        title: options.userFacingName,
        singleLogic: options.singleLogic,
        multiLogic: options.multiLogic
      });

      var coll = new FacetCollection([], {
        preprocess: options.preprocess
      });

      var containerview = new SelectLogicContainerView({
        collection: coll,
        model: containerModel,
        itemView: AdditionalViews.CheckboxHierarchicalView,
        openByDefault: options.openByDefault,
        defaultNumFacets: options.defaultNumFacets

      });

      var widget = new HierarchicalController({
        //levelDepth is zero-indexed
        levelDepth: options.levelDepth || 1,
        view: containerview,
        facetName: options.facetName,
        extractFacets: options.extractFacets
      })

      return widget
    },

    makeGraphFacet: function (options) {
      //required config
      if (!(options.facetName && options.userFacingName && options.xAxisTitle)) {
        throw new Error("Required configuration variables were not passed")
      }
      ;

      /*optional config variables:
       options.solrPath
       options.openByDefault (true/false; default false)
       options.level: how deep does the hierarchy go
       */

      var containerModel = new BaseContainerView.ContainerModelClass({
        title: options.userFacingName
      });
      var coll = new FacetCollection();

      var containerview = new ChangeApplyContainerView({
        collection: coll,
        model: containerModel,
        itemView: ZoomableGraphView,
        openByDefault: options.openByDefault,
        //so that the html template for the graph can use these values
        itemViewOptions: {
          xAxisTitle: options.xAxisTitle,
          title: options.userFacingName
        }
      });

      var widget = new BaseController({
        unpaginated: true,
        view: containerview,
        facetName: options.facetName,

      })

      widget.processResponse = function (apiResponse, data) {
        view = data.view;
        coll = view.collection;
        var facets = apiResponse.get(this.solrPath + "." + this.facetName);
        var facetsCol = [];
        _.each(facets, function (f, i) {
          if (i % 2 === 0) {
            facetsCol.push({
              //"title"
              x: parseInt(f),
              //"value"
              y: parseInt(facets[i + 1])
            })
          }
        }, this);

        facetsCol = _.sortBy(facetsCol, function (d) {
          return d.x
        });

        coll.reset({
          "graphInfo": facetsCol,
          "value": [facetsCol[0].x, facetsCol[facetsCol.length - 1].x]
        });

      };

      return widget
    }
  };
  return FacetFactory
})
