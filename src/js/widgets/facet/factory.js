define([
  'js/widgets/facet/collection',
  'js/widgets/facet/widget',
  'js/widgets/facet/container_view',
  'hbs!js/widgets/facet/templates/logic-container',
  'js/widgets/facet/tree_view',
  'js/widgets/base/base_widget',
  'js/components/paginator',
  'js/widgets/facet/graph-facet/year-graph',
  'js/widgets/facet/graph-facet/citation-graph',
  'js/widgets/facet/graph-facet/reads-graph'


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
    makeGraphFacet: function (graphName) {

      var graphView, processResponse, facetField, defaultQueryArguments;

      if (graphName === "year"){
        graphView = YearGraphView;
        facetField = "year";
        defaultQueryArguments = {
          "facet.pivot" : "property,year",
          "facet" : "true",
          "facet.minCount" : "1"
        };
        processResponse = function (apiResponse) {

          this.setCurrentQuery(apiResponse.getApiQuery());

          var data = apiResponse.get("facet_counts.facet_pivot.property,year");

          if (apiResponse.get("response.numFound") < 2){
            this.collection.reset({graphData : []});
            return
          }


          var refData = _.findWhere(data, {value : "refereed"});

          if (refData){
            refData = refData.pivot;
          }

          var nonRefData = _.findWhere(data, {value : "notrefereed"});

          if (nonRefData){
            nonRefData = nonRefData.pivot;
          }

          var maxVal, minVal;

          _.each(refData, function(d){
            var val = parseInt(d.value);
            if (maxVal === undefined){
              maxVal = val;
            }
            else if (val > maxVal){
              maxVal = val
            }
            if(minVal === undefined){
              minVal = val;
            }
            else if (parseInt(d.value) < minVal){
              minVal = parseInt(d.value);
            }
          });

          _.each(nonRefData, function(d){
            var val = parseInt(d.value);
            if (maxVal === undefined){
              maxVal = val;
            }
            else if (val > maxVal){
              maxVal = val
            }
            if(minVal === undefined){
              minVal = val;
            }
            else if (parseInt(d.value) < minVal){
              minVal = parseInt(d.value);
            }
          });

          var yearRange = _.range(minVal, maxVal+1);

          var finalData = [];

          _.each(yearRange, function(year){
            var stringYear = year + "";
            refCount = _.filter(refData, function(d){return d.value === stringYear})[0];
            refCount = refCount? refCount.count : 0;
            nonRefCount = _.filter(nonRefData, function(d){return d.value === stringYear})[0];
            nonRefCount = nonRefCount? nonRefCount.count : 0;

            finalData.push({x: year, y: refCount + nonRefCount, refCount: refCount})

          })

          if (finalData.length < 2){
            this.collection.reset({graphData : []});
            return
          }

          this.collection.reset([{graphData : finalData}]);
        }
      }
      else if (graphName === "citation"){
        graphView = CitationGraphView;
        facetField = "citation_count";
        defaultQueryArguments = {
          "facet.pivot" : "property,citation_count",
          "facet" : "true",
          "facet.limit": "-1"
        };
        processResponse = function (apiResponse) {
          this.setCurrentQuery(apiResponse.getApiQuery());

          var data = apiResponse.get("facet_counts.facet_pivot.property,citation_count");

          if (apiResponse.get("response.numFound") < 2){
            this.collection.reset({graphData : []});
            return
          }

          var refData = _.findWhere(data, {value : "refereed"});

          if (refData){
            refData = refData.pivot;
          }

          var nonRefData = _.findWhere(data, {value : "notrefereed"});

          if (nonRefData){
            nonRefData = nonRefData.pivot;
          }

          var finalData = [];

          _.each(refData, function(d) {
            var val = d.value, count = d.count;
            _.each(_.range(count), function () {
              finalData.push({refereed: true, x: undefined, y: val})
            })
          })

          _.each(nonRefData, function(d) {
            var val = d.value, count = d.count;
            _.each(_.range(count), function () {
              finalData.push({refereed: false, x: undefined, y: val})
            })
          })

          if (finalData.length < 2){
            this.collection.reset({graphData : []});
            return
          }

          finalData = finalData.sort(function (a, b) {
            return b.y- a.y;
          });

          //a cut off of 2000
          finalData = _.first(finalData, 2000);

          finalData = _.map(finalData, function(d, i){
            d.x = i +1
            return d
          });

          this.collection.reset([{graphData : finalData}]);

        }
      }
      else if (graphName === "reads"){
        graphView = ReadsGraphView;
        facetField = "read_count";
        defaultQueryArguments = {
          "facet.pivot" : "property,read_count",
          "facet" : "true",
          "facet.limit": "-1"
        };
        processResponse = function (apiResponse) {
          this.setCurrentQuery(apiResponse.getApiQuery());

          var data = apiResponse.get("facet_counts.facet_pivot.property,read_count");

          if (apiResponse.get("response.numFound") < 2){
            this.collection.reset({graphData : []});
            return
          }

          var refData = _.findWhere(data, {value : "refereed"});

          if (refData){
            refData = refData.pivot;
          }

          var nonRefData = _.findWhere(data, {value : "notrefereed"});

          if (nonRefData){
            nonRefData = nonRefData.pivot;
          }

          var finalData = [];

          _.each(refData, function(d) {
            var val = d.value, count = d.count;
            _.each(_.range(count), function () {
              finalData.push({refereed: true, x: undefined, y: val})
            })
          })

          _.each(nonRefData, function(d) {
            var val = d.value, count = d.count;
            _.each(_.range(count), function () {
              finalData.push({refereed: false, x: undefined, y: val})
            })
          })

          if (finalData.length < 2){
            this.collection.reset({graphData : []});
            return
          }

          finalData = finalData.sort(function (a, b) {
            return b.y- a.y;
          });

          //a cut off of 2000
          finalData = _.first(finalData, 2000);

          finalData = _.map(finalData, function(d, i){
            d.x = i +1
            return d
          });

          this.collection.reset([{graphData : finalData}]);

        }
      }
      else {
        throw new Error("We can't make that particular graph, check the name you passed to the factory")
      }

      var containerOptions = {
        model: new FacetContainerView.ContainerModelClass({noOptions : true}),
        collection: new FacetCollection(),
        openByDefault: true,
        itemView: graphView
      };

      var controllerOptions = {
        view: new FacetContainerView(containerOptions),
        facetField : facetField
      };

      var GraphWidget = FacetWidget.extend({
        processResponse : processResponse,
        defaultQueryArguments : defaultQueryArguments,
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
