
// XXX:rca - let's call this whole file 'facet_factory' and eventually document the options ;-)

define(['./views/facet-container-views',
      './views/facet-item-views',
      './facet-collection', './facet-controllers'
    ], function(facetContainerViews, facetItemViews, IndividualFacetCollection, FacetControllers){

      var BaseFacetWidget = FacetControllers.BaseFacetWidget,
          HierarchicalFacetWidget = FacetControllers.HierarchicalFacetWidget;

    var FacetFactory = {
      makeBasicCheckboxFacet: function (options) {
        //required config
        if (!(options.facetName && options.userFacingName)) {
          throw new Error("Required configuration variables were not passed")
        }

        /*optional config variables:
         options.preprocess (titleCase, allCaps, removeSlash)
         options.solrPath
         options.defaultNumFacets (limit facets shown to certain number other than 5)
         options.openByDefault (true/false; default false)
         options.singleLogic =[]
         options.multiLogic = []}
         options.extractFacets (a function that hands in facets and passes off
         processed facets in processResponse)
         */

        var containerModel = new SelectLogicModel({
          title      : options.userFacingName,
          value      : options.facetName,
          singleLogic: options.singleLogic,
          multiLogic : options.multiLogic
        });

        var coll = new IndividualFacetCollection([], {
          preprocess: options.preprocess
        });

        var containerview = new facetContainerViews.SelectLogicContainer({
          collection      : coll,
          model           : containerModel,
          itemView        : facetItemViews.CheckboxOneLevelView,
          defaultNumFacets: options.defaultNumFacets,
          openByDefault   : options.openByDefault
        });

        var widget = new BaseFacetWidget({
          view         : containerview,
          facetName    : options.facetName,
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
         options.facetPrefix : (if you need to override "_facet_hier")
         options.preprocess (titleCase, allCaps, removeSlash)
         options.solrPath
         options.defaultNumFacets (limit facets shown to certain number other than 5)
         options.openByDefault (true/false; default false)
         options.singleLogic =[]
         options.multiLogic = []}
         options.extractFacets (a function that hands in facets and passes off
         processed facets in processResponse)
         */

        var containerModel = new SelectLogicModel({
          title      : options.userFacingName,
          singleLogic: options.singleLogic,
          multiLogic : options.multiLogic
        });

        var coll = new IndividualFacetCollection([], {
          preprocess: options.preprocess
        });

        var containerview = new facetContainerViews.SelectLogicContainer({
          collection   : coll,
          model        : containerModel,
          itemView     : facetItemViews.CheckboxHierarchicalView,
          openByDefault: options.openByDefault,
          defaultNumFacets: options.defaultNumFacets

        });

        var widget = new HierarchicalFacetWidget({
          //levelDepth is zero-indexed
          levelDepth   : options.levelDepth || 1,
          view         : containerview,
          facetName    : options.facetName,
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

        var containerModel = new BaseContainerModel({
          title: options.userFacingName
        });
        var coll = new IndividualFacetCollection();

        var containerview = new facetContainerViews.ChangeApplyContainer({
          collection     : coll,
          model          : containerModel,
          itemView       : facetItemViews.ZoomableGraphView,
          openByDefault  : options.openByDefault,
          //so that the html template for the graph can use these values
          itemViewOptions: {
            xAxisTitle: options.xAxisTitle,
            title     : options.userFacingName
          }
        });

        var widget = new BaseFacetWidget({
          unpaginated    : true,
          view           : containerview,
          facetName      : options.facetName,
          processResponse: function (apiResponse, data) {
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
              "value" : [facetsCol[0].x, facetsCol[facetsCol.length-1].x]
            });

          }
        })
        return widget
      }
    };
    return FacetFactory
  })
