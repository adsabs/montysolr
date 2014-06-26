define([
  'js/widgets/facet/collection',
  'js/widgets/facet/widget',
  'js/widgets/facet/container_view',
  'hbs!js/widgets/facet/templates/logic-container',
  'js/widgets/base/tree_view',
  'js/widgets/base/base_widget',
  'js/components/paginator',
  'js/widgets/facet/zoomable_graph_view'
], function (
  FacetCollection,
  FacetWidget,
  FacetContainerView,
  LogicSelectionContainerTemplate,
  TreeView,
  BaseWidget,
  Paginator,
  ZoomableGraphView
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
        displayNum: 5,
        maxDisplayNum: 100,
        openByDefault: false,
        showOptions: true,
        fl: 'id'
      };

      containerOptions = _.extend(containerOptions,
        _.pick(options, ['displayNum', 'maxDisplayNum', 'openByDefault', 'showOptions', 'logicOptions']));

      if (containerOptions.logicOptions) {
        containerOptions.template = LogicSelectionContainerTemplate;
      }

      var controllerOptions = {
        defaultQueryArguments: {
          "facet": "true",
          "facet.field": options.facetField,
          "facet.mincount": "1"
        },
        view: new FacetContainerView(containerOptions),
        paginator: new Paginator({start: 0, rows: 20, startName: "facet.offset", rowsName: "facet.limit"})
      };

      var controllerOptions = _.extend(controllerOptions, _.pick(options,
        ['responseProcessors', 'defaultQueryArguments', 'extractionProcessors']));


      var widget = new FacetWidget(controllerOptions);
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
        displayNum: 5,
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

      var controllerOptions = {
        defaultQueryArguments: {
          "facet": "true",
          "facet.field": options.facetField,
          "facet.mincount": "1",
          "facet.limit": 20,
          "facet.prefix": "0/",
          fl: 'id'
        },
        view: new FacetContainerView(containerOptions),
        paginator: new Paginator({start: 0, rows: 20, startName: "facet.offset", rowsName: "facet.limit"})
      };

      var controllerOptions = _.extend(controllerOptions, _.pick(options,
        ['responseProcessors', 'defaultQueryArguments', 'extractionProcessors']));

      var widget = new FacetWidget(controllerOptions);
      return widget;
    },



    /**
     * Factory method to create single level paging facets; required
     * arguments:
     *    facetField
     *    facetTitle
     *    xAxisTitle
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
    makeGraphFacet: function (options) {
      //required config
      if (!(options.facetField && options.facetTitle && options.xAxisTitle)) {
        throw new Error("Required configuration variables were not passed");
      }

      var GraphContainerView = FacetContainerView.extend({
        itemViewOptions: function (model, index) {
          return _.extend({xAxisTitle: options.facetTitle, title: options.facetTitle}, FacetContainerView.prototype.itemViewOptions.apply(this, arguments));
        }
      });

      var containerOptions = {
        model: new FacetContainerView.ContainerModelClass({title: options.facetTitle}),
        collection: new FacetCollection(),
        displayNum: 5,
        maxDisplayNum: 100,
        openByDefault: false,
        showOptions: true,
        itemView: ZoomableGraphView
      };

      containerOptions = _.extend(containerOptions,
        _.pick(options, ['displayNum', 'maxDisplayNum', 'openByDefault', 'showOptions']));


      var controllerOptions = {
        defaultQueryArguments: {
          "facet": "true",
          "facet.field": options.facetField,
          "facet.mincount": "1",
          "facet.limit": 100,
          fl: 'id'
        },
        view: new GraphContainerView(containerOptions)
      };

      var controllerOptions = _.extend(controllerOptions, _.pick(options,
        ['defaultQueryArguments']));

      var GraphWidget = FacetWidget.extend({
        facetField: options.facetField,
        //XXX:rca hack - facet.prefix should be cleaned up by QM
        customizeQuery: function(apiQuery) {
          var q = apiQuery.clone();
          q.unlock();
          q.unset('facet.prefix');
          if (this.defaultQueryArguments) {
            q = this.composeQuery(this.defaultQueryArguments, q);
          }
          return q;
        },
        processResponse: function (apiResponse) {
          //console.log(JSON.stringify(apiResponse.getApiQuery().toJSON()));
          var view = this.view;
          var coll = this.collection;
          var facets = apiResponse.get("facet_counts.facet_fields." + this.facetField);
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

          if (facetsCol.length == 0) {
            coll.reset();
            return;
          }

          facetsCol = _.sortBy(facetsCol, function (d) {
            return d.x
          });

          coll.reset({
            "graphInfo": facetsCol,
            "value": [facetsCol[0].x, facetsCol[facetsCol.length - 1].x]
          });

        }
      });



      return new GraphWidget(controllerOptions);

    }
  };
  return FacetFactory
});
