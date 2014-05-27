define([
  'js/widgets/facet/collection',
  'js/widgets/facet/widget',
  'js/widgets/facet/container_view',
  'hbs!js/widgets/facet/templates/logic-container',
  'js/widgets/base/tree_view',
  'js/widgets/base/base_widget'
], function (
  FacetCollection,
  FacetWidget,
  FacetContainerView,
  LogicSelectionContainerTemplate,
  TreeView,
  BaseWidget
  ) {


  var FacetFactory = {

    /**
     * Factory method to create single level paging facets; required
     * arguments:
     *    facetField
     *    facetTitle
     *
     * Optional arguments:
     *
     *    displayNum = how many items to display
     *    maxDisplayNum = maximum to display
     *    openByDefault = default is false
     *    showOptions = [false]; show the containers options
     *    logicOptions = object {'single': [....], 'multiple': [...]}
     *          when present, the facet allows for selection of logical
     *          operators - these will be applied to the selected items
     *          'single' is for situations when only one item is selected
     *          'multiple' applies to more than one item
     *
     * @param options
     * @returns {BaseFacetWidget}
     */
    makeBasicCheckboxFacet: function (options) {
      //required config
      if (!(options.facetField && options.facetTitle)) {
        throw new Error("Required configuration variables were not passed")
      }

      var containerOptions = {
        model: new FacetContainerView.ContainerModelClass({title: options.facetTitle}),
        collection: new FacetCollection(),
        displayNum: 15,
        maxDisplayNum: 100,
        openByDefault: false,
        showOptions: true
      };

      containerOptions = _.extend(containerOptions,
        _.pick(options, ['displayNum', 'maxDisplayNum', 'openByDefault', 'showOptions', 'logicOptions']));

      if (containerOptions.logicOptions) {
        containerOptions.template = LogicSelectionContainerTemplate;
      }

      var widget = new FacetWidget({
        defaultQueryArguments: {
          "facet": "true",
          "facet.field": options.facetField,
          "facet.mincount": "1"
        },
        view: new FacetContainerView(containerOptions)
      });
      return widget;
    },

    /**
     * Factory method for creating multi-level paging facets. For the
     * list of options see the single-level docstring
     *
     * @param options
     * @returns {BaseFacetWidget}
     */
    makeHierarchicalCheckboxFacet: function (options) {
      //required config
      if (!(options.facetField && options.facetTitle)) {
        throw new Error("Required configuration variables were not passed")
      }

      var containerOptions = {
        model: new FacetContainerView.ContainerModelClass({title: options.facetTitle}),
        collection: new FacetCollection(),
        displayNum: 15,
        maxDisplayNum: 100,
        openByDefault: false,
        showOptions: true,
        itemView: TreeView,
        collection: new TreeView.CollectionClass()
      };

      containerOptions = _.extend(containerOptions,
        _.pick(options, ['displayNum', 'maxDisplayNum', 'openByDefault', 'showOptions', 'logicOptions']));

      if (containerOptions.logicOptions) {
        containerOptions.template = LogicSelectionContainerTemplate;
      }

      var widget = new FacetWidget({
        defaultQueryArguments: {
          "facet": "true",
          "facet.field": options.facetField,
          "facet.mincount": "1"
        },
        view: new FacetContainerView(containerOptions)
      });
      return widget;
    },

    makeGraphFacet: function(options) {
      return new BaseWidget();
    }


    /* TBD

     optional config variables:
     options.solrPath
     options.openByDefault (true/false; default false)
     options.level: how deep does the hierarchy go

    makeGraphFacet: function (options) {
      //required config
      if (!(options.facetField && options.userFacingName && options.xAxisTitle)) {
        throw new Error("Required configuration variables were not passed")
      }
      ;


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
        facetName: options.facetField,

      })

      widget.processResponse = function (apiResponse, data) {
        view = data.view;
        coll = view.collection;
        var facets = apiResponse.get(this.solrPath + "." + this.facetField);
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
    */
  };
  return FacetFactory
})
