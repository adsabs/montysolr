/**
 *
 * This is a widget which analyzes the current query and displays the
 * filter components in a visual form
 *
 *
 * And allows to remove items from the query individually
 *
 * TODO: it needs to react on browser-back/forward
 */

define([
    'underscore',
    'jquery',
    'backbone',
    'marionette',
    'js/components/api_query',
    'js/components/pubsub_events',
    'hbs!./templates/widget-view',
    'hbs!./templates/item-view',
    'js/components/api_feedback',
    'js/mixins/dependon'
  ],

  function(
    _,
    $,
    Backbone,
    Marionette,
    ApiQuery,
    PubSubEvents,
    WidgetTemplate,
    ItemTemplate,
    ApiFeedback,
    Dependon
    ){

    // Model
    var KeyValue = Backbone.Model.extend({ });

    // Collection of data
    var KeyValueCollection = Backbone.Collection.extend({
      model : KeyValue
    });

    var ItemView = Marionette.ItemView.extend({
      tagName : 'span',
      className : "filter-topic-group",
      template : ItemTemplate,
      events : {
        'click button' : 'onClick'
      },
      onClick: function(ev) {
        ev.preventDefault();
        var $t = $(ev.target);
        this.trigger('filter-event', $t.attr('value')
          ? $t.attr('value')
          : $t.parent().attr('value'));
      }
    });

    var WidgetView = Marionette.CompositeView.extend({
      template : WidgetTemplate,
      childView : ItemView,
      childViewContainer: "#filter-visualizer",
      events: {
      }

    });

    var WidgetController = Marionette.Controller.extend({

      activate: function(beehive) {
        _.bindAll(this, "processFeedback", "onFilterEvent");
        this.setBeeHive(beehive);
        pubsub = beehive.getService('PubSub');
        pubsub.subscribe(pubsub.FEEDBACK, this.processFeedback);
      },

      initialize : function(options){
        options = _.defaults(options, {
          withoutOperators: true,
          maxNumberOfTokens: 5,
          maxQueryLen: 100
        }); // simplified version by default
        this.collection = new KeyValueCollection({});
        this.view = new WidgetView({collection: this.collection});
        this.listenTo(this.view, 'childview:filter-event', this.onFilterEvent);
        this.knownFilters = {
          'fq_author' : 'Author',
          'fq_database': 'Collection',
          'fq_property' : "Property",
          'fq_facets180': 'Status',
          'fq_keyword_facet': 'Keywords',
          'fq_bibstem_facet': 'Publication',
          'fq_bibgroup_facet': 'BibGroup',
          'fq_data_facet': 'Data',
          'fq_vizier_facet': 'Vizier',
          'fq_simbad_object_facet_hier': 'Object',
          'fq_grant': 'Grant',
          'fq_visualization_author': 'Author Network',
          'fq_visualization_paper': 'Paper Network',
          'fq_wordcloud': 'Concept Cloud',
          'fq_bubble_chart': 'Results Graph',

        };
        this.currentQuery = null;
        this.withoutOperators = options.withoutOperators;
        this.maxNumberOfTokens = options.maxNumberOfTokens;
        this.maxQueryLen = options.maxQueryLen;
      },


      render : function(){
        this.view.render();
        return this.view.el
      },


      processFeedback: function(feedback) {
        switch (feedback.code) {
          case ApiFeedback.CODES.SEARCH_CYCLE_STARTED:
            this.processQuery({numFound: feedback.numFound, q: feedback.query.clone()});
            break;
        }
      },


      /**
       * Object oriented interface; the main method for
       * displaying the filters
       */
      processQuery: function(data) {
        var q = data.q; numFound = data.numFound;

        var filters = this.extractFilters(q);
        this._saveInfo(q, filters);
        var guiModels = this.prepareGUIData(filters);

        //this.view.model.set({
        //  numFound: numFound,
        //  filters: filters,
        //  query: q
        //});

        this.collection.reset(guiModels);
      },

      _saveInfo: function(q, filters) {
        this.currentQuery = q;
        this.currentFilters = {};
        _.each(filters, function(v) {
          this.currentFilters[v.filter_name] = v;
        }, this);
      },

      /**
       * Extracts everything we know from the query filters.
       * This logic *has to be extended* when we add new facets
       * and filters; it is not generic and it likely fails in
       * some usecases...ehi,..
       *
       * @param apiQuery
       *
       * @return array of objects, each containing information
       *    about the filter, i.e.
       *    [{
       *       category: 'AUTHOR',
       *       filter_name: 'fq_author',
       *       filter_query: "(author_facet_hier:\"0/Wang, J\")",
       *       filter_key: '__author_facet_hier_fq_author',
       *       filter_value: ["AND","author_facet_hier:\"0/Wang, J\""]
       *     }]
       */
      extractFilters: function(apiQuery) {
        var data = apiQuery.toJSON();
        var filters = [];
        if (!data.fq) {
          return filters; // nothing to do
        }

        var fq = data.fq;

        // O(n^2) - but ok, it's short list
        var keys = _.keys(data);

        _.each(keys, function(k) {
          if (k.indexOf('fq_') == 0) {

            // is this filter in the list of accepted values?
            if (!this.knownFilters[k]) {
              return;
            }
            var oneFilter = {
              category: this.knownFilters[k],
              filter_name: k,
              filter_query: data[k][0],
            };

            if (data[k].length > 1) {
              console.warn('Breach of an api contract, filter contains too many values', k, data[k]);
              return;
            }

            // now we have to identify the remaining parts
            var filteredKeys = _.filter(keys, function(x) {if (x.indexOf(k) > -1 && x !== k) return true;})

            if (filteredKeys.length == 1) {
              oneFilter.filter_key = filteredKeys[0];
              oneFilter.filter_value = data[filteredKeys[0]];
            }
            else {
              console.warn("no fq metadata available, so presumably query is being loaded from url (if not, there is a problem!) ")
            }

            filters.push(oneFilter);
          }
        }, this);

        //clean up for hierarchical facets (remove parents)
        filters.forEach(function(filt){
          filt.filter_value = filt.filter_value.map(function(facet){
            return facet.replace(/1\/[^/]+\//, '');
          });
        });

        // finally sort them by preference/ remove unknown
        filters = _.sortBy(filters, function(x) {
          var ix = _.indexOf(self.knownFilters, x.category);
          if (ix > -1)
            return ix;
          return 1000; // everything else will be together (at the end)
        });
        return filters;
      },

      /**
       * Extracts the data from the filters suitable to pass to the GUI
       *
       * @param filters - array of filter objects, ie.
       *   [
       *    {
       *     "category": "Author",
       *     "filter_name": "fq_author",
       *     "filter_query": "(author_facet_hier:\"0/Wang, J\")",
       *     "filter_key": "__author_facet_hier_fq_author",
       *     "filter_value": [
       *      "AND",
       *      "author_facet_hier:\"0/Wang, J\""
       *     ]
       *    }
       *    ]
       *
       * @return - array or arrays with gui data elements, ie.
       *
       *  [
       *    {elements: [
       *      {type: 'category', display: 'Author', value: 'fq_author|category|Author'},
       *      {type: 'operand', display: 'Wang, J', value: 'fq_author|operand|author_facet_hier:"0/Wang, J"'},
       *      {type: 'control', display: 'x', value: 'fq_author|control|x'}
       *    ]}
       *  ]
       *
       **/
      prepareGUIData: function(filters) {

        if (this.withoutOperators)
          return this._prepareGUIDataWithoutOperators(filters);

        var guiData = [];
        _.each(filters, function(filter) {
          var oneFilter = [];
          oneFilter.push({
            type: 'category',
            display: filter.category,
            value: filter.filter_name + '|category|' + filter.category
          });

          // if there are too many elements, just show one value 'x custom values'
          if (filter.filter_value.length-1 > this.maxNumberOfTokens || filter.filter_value.join(' ').length > this.maxQueryLen) {
            oneFilter.push({
              type: 'control',
              display: (filter.filter_value.length - 1) + ' custom values',
              value: filter.filter_name + '|control|x'
            });
            guiData.push({elements: oneFilter});
            return;
          }

          var i = 0, operator = filter.filter_value[0];
          _.each(filter.filter_value.slice(1), function(operand) {
            if (++i > 1) {
              oneFilter.push({
                type: 'operator',
                display: operator,
                value: filter.filter_name + '|operator|' + operator
              });
            }

            oneFilter.push({
              type: 'operand',
              display: this.beautifyOperand(filter.filter_name, operand),
              value: filter.filter_name + '|operand|' + operand
            });

          }, this);
          oneFilter.push({
            type: 'control',
            display: '',
            value: filter.filter_name + '|control|x'
          });
          guiData.push({elements: oneFilter}); // deep nested because of !hbs
        }, this);
        return guiData;
      },

      _prepareGUIDataWithoutOperators: function(filters) {

        var guiData = [];
        _.each(filters, function(filter) {
          var oneFilter = [];
          oneFilter.push({
            type: 'category',
            display: filter.category,
            value: filter.filter_name + '|category|' + filter.category
          });

          // if there are too many elements (or we're missing data bc we loaded from url), just show one value 'x custom values'
          if ( !filter.filter_value ||
                filter.filter_value.length-1 > this.maxNumberOfTokens ||
                filter.filter_value.join(' ').length > this.maxQueryLen
            ) {
            oneFilter.push({
              type: 'operand',
              display: 'custom filter',
              value: filter.filter_name + '|control|x'
            });
            guiData.push({elements: oneFilter});
            return;
          }

          var i = 0, operator = filter.filter_value[0], indicator = '';
          if (operator == 'NOT') {
            indicator = '-';
          }
          else if (operator == 'AND') {
            indicator = '+';
          }
          _.each(filter.filter_value.slice(1), function(operand) {
            var element = _.clone(oneFilter);
            var ind = i++ == 0 && operator == 'NOT' ? '' : indicator; // if NOT, the first element is without prefix
            element.push({
              type: 'operand',
              display: ind + this.beautifyOperand(filter.filter_name, operand),
              value: filter.filter_name + '|operand|' + operand
            });

            guiData.push({elements: element}); // deep nested because of !hbs
          }, this);
        }, this);
        return guiData;
      },

      /**
       * Returns a simplified version of a query - suitable for display to users
       * In general, this will remove the fields and just keep the value (without
       * quotes and backslashes)
       *
       * @param filter_name - string identifying the filter
       *
       * @param queryString - raw query (string)
       *
       * @return string
       */
      beautifyOperand: function(filter_name, queryString) {
        var s = queryString;
        // first remove brackets, if any
        while (s.indexOf('(') == 0 && s.indexOf(')') == s.length-1) {
          s = s.substr(1, s.length-2);
        }

        // replace fields
        switch (filter_name) {
          case 'fq_author':
            s = s.replace(/author_facet_hier:/g, '');
            s = s.replace(/\d+\//g, ''); // remove hier markers
            break;
          case 'fq_database':
            s = s.replace(/database:/g, '');
            break;
          case 'fq_facets180':
            s = s.replace(/property:/g, '');
            break;
          case 'fq_keyword_facet':
            s = s.replace(/keyword_facet:/g, '');
            s = s.replace(/\\/g, '');
            break;
          case 'fq_bibstem_facet':
            s = s.replace(/bibstem_facet:/g, '');
            break;
          case 'fq_bibgroup_facet':
            s = s.replace(/bibgroup_facet:/g, '');
            break;
          case 'fq_data_facet':
            s = s.replace(/data_facet:/g, '');
            break;
          case 'fq_vizier_facet':
            s = s.replace(/vizier_facet:/g, '');
            break;
          case 'fq_grant':
            s = s.replace(/grant:/g, '');
            break;
          case 'fq_simbad_object_facet_hier':
            s = s.replace(/simbad_object_facet_hier:0\\\//g, '');
            break;
        }

        // replace quotes, if any
        s = s.replace(/\\?"/g, '');
        // *:* to all
        s = s.replace(/\*:\*/, 'any');

        return s;
      },

      /**
       * Builds a new ApiQuery based on the command we receive from the GUI
       */
      createModifiedQuery: function(value) {
        var parts = value.split('|'), c = {};
        c.name = parts[0];
        c.part = parts[1];
        c.value = parts[2];

        var q = this.currentQuery.toJSON();

        if (!q.fq && !q.fq[c.name]) {
          console.error('The current query has no filter: ' + c.name);
          return;
        }

        switch (c.part) {
          case 'operand': // remove one item from the filter
            if (q[this.currentFilters[c.name]['filter_key']].length > 2) { // if we remove one, there must be >=1 left
              var aux = q[this.currentFilters[c.name]['filter_key']];
              if (_.indexOf(aux, c.value) == -1) {
                console.error('Cannot find/remove the value: ' + c.value, aux);
                throw Error("Error in modifying the query");
              }
              aux = _.reject(aux, function(x) { return x == c.value});

              // now build a new query
              var newQuery;
              if (aux.length > 2) {
                var op = ' ' + aux[0] + ' ';
                newQuery = '(' + aux.slice(1).join(op) + ')';
              }
              else {
                newQuery = aux[1];
              }
              q[c.name] = newQuery;
              q[this.currentFilters[c.name]['filter_key']] = aux;
              break;
            }
            // else we delete the whole filter
          case 'control': // remove the whole filter
            delete q[c.name]; // delete the filter
            delete q[this.currentFilters[c.name]['filter_key']]; // auxiliary structure

            q.fq = _.filter(q.fq, function(val) { // and remove its mentions from 'fq'
              if (val.indexOf('v=$' + c.name) == -1) //TODO: deal with partial matches
                return true;
            });

            break;

        }

        if (q.fq && q.fq.length == 0)
          delete q.fq;

        return new ApiQuery(q);
      },


      onLoad : function(apiQuery) {
        if (this.collection) {
          this.collection.reset(this.getData(apiQuery))
        }
        else {
          this.initialize(apiQuery);
        }
      },


      /**
       * Listening to all events
       * in this widget's views; and manipulating the models
       * that back views
       */
      onFilterEvent: function(node, value) {
        var newQuery = this.createModifiedQuery(value);
        var ps = this.getBeeHive().getService('PubSub');
        if (ps)
          ps.publish(ps.START_SEARCH, newQuery);
      }

    });
    _.extend(WidgetController.prototype, Dependon.BeeHive);

    return WidgetController;
  });


