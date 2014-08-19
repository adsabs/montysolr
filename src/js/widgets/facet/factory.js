define([
  'js/widgets/facet/collection',
  'js/widgets/facet/widget',
  'js/widgets/facet/container_view',
  'hbs!js/widgets/facet/templates/logic-container',
  'js/widgets/facet/tree_view',
  'js/widgets/base/base_widget',
  'js/components/paginator',
  'js/widgets/facet/graph-facet/year_graph',
  'js/widgets/facet/graph-facet/h_index_graph'


], function (
  FacetCollection,
  FacetWidget,
  FacetContainerView,
  LogicSelectionContainerTemplate,
  TreeView,
  BaseWidget,
  Paginator,
  YearGraphView,
  CitationGraphView,
  ReadsGraphView
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
        hierMaxLevels: 2,
        view: new FacetContainerView(containerOptions),
        paginator: new Paginator({start: 0, rows: 20, startName: "facet.offset", rowsName: "facet.limit"})
      };

      var controllerOptions = _.extend(controllerOptions, _.pick(options,
        ['responseProcessors', 'defaultQueryArguments', 'extractionProcessors', 'hierMaxLevels']));

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

      var viewOptions = options.graphViewOptions;

      var containerOptions = {
        model: new FacetContainerView.ContainerModelClass({noOptions : true}),
        collection: new FacetCollection(),
        openByDefault: true,
        itemView: options.graphView,
        itemViewOptions : viewOptions
      };

      var controllerOptions = {
        view: new FacetContainerView(containerOptions),
        facetField : options.facetField
      };

      var GraphWidget = FacetWidget.extend({
        processResponse : options.processResponse,
        defaultQueryArguments : options.defaultQueryArguments,
        //XXX:rca hack - facet.prefix should be cleaned up by QM
        customizeQuery: function(apiQuery) {
          var q = apiQuery.clone();
          q.unlock();
          q.unset('facet.prefix');
          if (this.defaultQueryArguments) {
            q = this.composeQuery(this.defaultQueryArguments, q);
          }
          return q;
        }
      });
      return new GraphWidget(controllerOptions);

    }
  };
  return FacetFactory
});
