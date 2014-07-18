/**
 * A plugin to build interactive UI for the search. This plugin can be inserted into
 * any element on the page; and provides several methods for setting/getting state
 * of the form.
 *
 * Plugin itself does not communicate with API - but if you provide 'qtreeGetter'
 * that has methods 'getQTree', then it will parse query input automatically.
 *
 */

define([
  'underscore',
  'bootstrap',
  'jquery',
  'jquery-querybuilder',
  'js/components/generic_module',
  'js/components/query_builder/rules_translator',
  'js/components/api_query'
  ],

  function(
  _,
  Bootstrap,
  $,
  jQueryQueryBuilderPlugin,
  GenericModule,
  RulesTranslator,
  ApiQuery
  ) {

    var QueryBuilder = GenericModule.extend({


      initialize: function(options) {

        this._rules = null;
        this.rulesTranslator = new RulesTranslator();

        this.rulesTranslator.setValidFunctions(
          {
            'topn()': true,
            'citations()': true,
            'references()': true,
            'instructive()': true,
            'trending()': true,
            'pos()': true
          }
        );

        this.qtreeGetter = options.qtreeGetter || null;
        if (this.qtreeGetter && !this.qtreeGetter.getQTree) {
          throw new Error("qtreeGetter must provide method 'getQTree'");
        }

        if (options.el) {
          this.$el = $(options.el);
        }
        else {
          this.$el = $('<div/>');
        }

        $.fn.queryBuilder.defaults.set({
          conditions: ['AND', 'OR'],
          lang: {
            "defop_condition": "Space",

            "operator_is": "is",
            "operator_is_not": "is",
            "operator_is_exactly": "is exactly",

            "operator_is_function": "matches",
            "operator_is_not_function": "doesn't match",

            "operator_contains": "has words",
            "operator_contains_not": "excludes words",

            "operator_is_wildcard": "starts with",
            "operator_is_not_wildcard": "doesn't start with",


            "operator_is_phrase": "has phrase",
            "operator_is_wildphrase": "has wildcard",
            "operator_is_not_phrase": "excludes phrase",
            "operator_is_not_empty": "is not empty",

            "operator_pos()": "Limit by Position",
            "operator_citations()": "Get Citations",
            "operator_references()": "Get References",
            "operator_trending()": "Find Trending Papers",
            "operator_instructive()": "Find Instructive Papers",

            delete_rule: ' ',
            delete_group: '',
            add_rule: ' ',
            add_group: ' '

          },
          operators: [
            {type: 'is',               accept_values: true,  apply_to: ['string', 'number', 'datetime']},
            {type: 'is_not',           accept_values: true,  apply_to: ['string', 'number', 'datetime']},
            {type: 'is_exactly',       accept_values: true,  apply_to: ['string', 'number', 'datetime']},

            {type: 'contains',         accept_values: true,  apply_to: ['string']},
            {type: 'contains_not',     accept_values: true,  apply_to: ['string']},

            {type: 'is_literal',       accept_values: true,  apply_to: ['string']},
            {type: 'is_function',      accept_values: true,  apply_to: ['string']},
            {type: 'is_not_function',  accept_values: true,  apply_to: ['string']},

            {type: 'is_phrase',        accept_values: true,  apply_to: ['string']},
            {type: 'is_wildphrase',    accept_values: true,  apply_to: ['string']},
            {type: 'is_not_phrase',    accept_values: true,  apply_to: ['string']},

            {type: 'is_wildcard',      accept_values: true,  apply_to: ['string']},
            {type: 'is_not_wildcard',  accept_values: true,  apply_to: ['string']},
            {type: 'starts_with',      accept_values: true,  apply_to: ['string']},
            {type: 'starts_not_with',  accept_values: true,  apply_to: ['string']},

            {type: 'is_empty',         accept_values: false, apply_to: ['string']},
            {type: 'is_not_empty',     accept_values: false, apply_to: ['string']},

            {type: 'less',             accept_values: true,  apply_to: ['number', 'datetime']},
            {type: 'less_or_equal',    accept_values: true,  apply_to: ['number', 'datetime']},
            {type: 'greater',          accept_values: true,  apply_to: ['number', 'datetime']},
            {type: 'greater_or_equal', accept_values: true,  apply_to: ['number', 'datetime']},


            {type: 'contains',         accept_values: true,  apply_to: ['string']},
            {type: 'contains_not',     accept_values: true,  apply_to: ['string']},
            {type: 'contains_phrase',  accept_values: true,  apply_to: ['string']},
            {type: 'contains_not_phrase', accept_values: true,  apply_to: ['string']},
            {type: 'is_not_empty',     accept_values: false, apply_to: ['string']},

            {type: 'pos()',            accept_values: true, apply_to: ['string']},
            {type: 'citations()',      accept_values: true, apply_to: ['string']},
            {type: 'references()',     accept_values: true, apply_to: ['string']},
            {type: 'trending()',       accept_values: true, apply_to: ['string']},
            {type: 'instructive()',    accept_values: true, apply_to: ['string']}

          ]
        });

        var singleTokenOperators = ['is', 'is_wildcard', 'is_exactly', 'is_not', 'is_not_wildcard', 'is_not_empty'];
        var multiTokenOperators = ['contains', 'is_phrase', 'contains_not', 'is_not_phrase', 'is_not_empty', 'is_wildcard'];
        var functionOperators = ['is_function', 'is_not_function'];

        this.singleTokenOperators = singleTokenOperators;
        this.multiTokenOperators = multiTokenOperators;
        this.functionOperators = functionOperators;


        this.$el.queryBuilder({
          sortable: false,

          filters: [
            {id: 'author', label: 'Author', type: 'string', placeholder: 'Planck, Max',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: '^author', label: 'First Author', type: 'string', placeholder: 'Einstein, A',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'title', label: 'Title', type: 'string',
              operators: multiTokenOperators, createOperatorIfNecessary: true},
            {id: '__all__', label: 'Any Field', type: 'string',
              operators: multiTokenOperators, createOperatorIfNecessary: true},
            {id: 'abstract', label: 'Abstract', type: 'string',
              operators: multiTokenOperators, createOperatorIfNecessary: true},
            {id: 'keyword', label: 'Keyword', type: 'string',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'full', label: 'Fulltext', type: 'string',
              operators: multiTokenOperators, createOperatorIfNecessary: true},
            {id: 'pos()', label: 'Search by Position()', type: 'string',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'author:&quot;higgs, p&quot;'},
                  {id: 'start', label: 'start', type: 'number', placeholder: '1 (start)'},
                  {id: 'end', label: 'end', type: 'number', placeholder: '2 (end:optional)'}
                ]);
              }
            },
            {id: 'citations()', label: 'citations()', type: 'string', placeholder: 'author:&quot;Einstein, A&quot;',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'author:einstein year:1905'}
                ]);
              }
            },
            {id: 'references()', label: 'references()', type: 'string', placeholder: 'instructive(title:&quot;monte carlo&quot;)',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'title:&quot;brownian motion&quot;'}
                ]);
              }
            },
            {id: 'trending()', label: 'trending()', type: 'string', placeholder: '(any valid query)',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'higgs boson'}
                ]);
              }
            },
            {id: 'instructive()', label: 'instructive()', type: 'string', placeholder: '(any valid query)',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'monte carlo'}
                ]);
              }
            },
            {id: 'topn()', label: 'topn()', type: 'string',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden'},
                  {id: 'number', label: 'number', type: 'number', placeholder: '100'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'citations(author:&quot;von neumann, john&quot;)'},
                  {
                    id: 'sorting',
                    label: 'sorting',
                    type: 'string',
                    input: 'select',
                    values: {
                      'citation_count desc': 'Pick first n most cited',
                      'citation_count asc': 'Pick first n least cited',
                      'pubdate desc': 'Pick first n newest',
                      'pubdate asc': 'Pick first n oldest',
                      'relevance': 'Pick first n most relevant'
                    }
                  }
                ]);
              }
            },
            {id: 'black_hole', label: 'literal', type: 'string',
              operators: functionOperators, createOperatorIfNecessary: true
            }
          ],
          extend: {
            createFunctionInputs: function(profiles) {
              var $target, values, $current, $container;
              $container = $('<span/>');
              var $target = $(this.getRuleInput(profiles[0].id, profiles[0]));
              $target.addClass('hide');
              values = [];

              for (var i=1; i<profiles.length; i++) {
                $current = $(this.getRuleInput(profiles[i].id, profiles[i]));
                $current.attr('index', i-1);
                $current.change(function() {
                  values[parseInt($(this).attr('index')) || '0'] = $(this).val();
                  $target.val(values.join('|'));
                });
                $container.append($current);
                values.push('');
              }
              $container.append($target);
              return $container;
            },

            getGroupTemplate: function(group_id) {

              var conditions = [];
              var l = this.settings.conditions.length, cond;
              for (var i=0; i < l; i++) {
                cond = this.settings.conditions[i];
                conditions.push('<label class="btn btn-xs btn-primary ' + (this.settings.default_condition == cond ? 'active' : '') + '"><input type="radio" name="'+ group_id +'_cond" value="' + cond + '"' + (this.settings.default_condition == cond ? 'checked' : '') + '>'+ (this.lang[cond.toLowerCase() + '_condition'] || cond) +'</label>');
              }
              conditions = conditions.join('\n');

              var h = '\
<dl id="'+ group_id +'" class="rules-group-container" '+ (this.settings.sortable ? 'draggable="true"' : '') +'> \
  <dt class="rules-group-header"> \
    <div class="btn-group pull-right"> \
      <button type="button" class="btn btn-xs btn-success" data-add="rule"><i class="glyphicon glyphicon-plus"></i> '+ this.lang.add_rule +'</button> \
      <button type="button" class="btn btn-xs btn-success" data-add="group"><i class="glyphicon glyphicon-plus-sign"></i> '+ this.lang.add_group +'</button> \
      <button type="button" class="btn btn-xs btn-danger" data-delete="group"><i class="glyphicon glyphicon-remove"></i> '+ this.lang.delete_group +'</button> \
    </div> \
    <div class="btn-group"> \
      ' + conditions + '\
    </div> \
    '+ (this.settings.sortable ? '<div class="drag-handle"><i class="glyphicon glyphicon-sort"></i></div>' : '') +' \
  </dt> \
  <dd class=rules-group-body> \
    <ul class=rules-list></ul> \
  </dd> \
</dl>';

              return h;
            },

            getRuleTemplate: function(rule_id) {
              var h = '\
<li id="'+ rule_id +'" class="rule-container" '+ (this.settings.sortable ? 'draggable="true"' : '') +'> \
  <div class="rule-header"> \
    <div class="btn-group pull-left"> \
      <button type="button" class="btn btn-xs btn-danger" data-delete="rule"><i class="glyphicon glyphicon-remove"></i> '+ this.lang.delete_rule +'</button> \
    </div> \
  </div> \
  '+ (this.settings.sortable ? '<div class="drag-handle"><i class="glyphicon glyphicon-sort"></i></div>' : '') +' \
  <div class="rule-filter-container"></div> \
  <div class="rule-operator-container"></div> \
  <div class="rule-value-container"></div> \
</li>';

              return h;
            }
          }
        });
      },


      /**
       * Utility method to load CSS into the page in which the plugin is used.
       */
      loadCss: function() {
        var url = require.toUrl('jquery-querybuilder') + '.css';

        if ($(document.getElementsByTagName("head")[0]).find('link[href=\''+url+'\']').length == 0) {
          var link = document.createElement("link");
          link.type = "text/css";
          link.rel = "stylesheet";
          link.href = url;
          document.getElementsByTagName("head")[0].appendChild(link);
        }
      },

      /**
       * Set the current state of the UI query builder
       *
       * @param rules
       */
      setRules: function(rules) {
        this._rules = rules;
        this.$el.queryBuilder('setRules', rules);
      },

      /**
       * Returns the rules as set inside the UI query builder
       *
       * @returns {*}
       */
      getRules: function() {
        return this.$el.queryBuilder('getRules');
      },

      /**
       * Resets the query builde UI
       */
      reset: function() {
        this._rules = null;
        this.$el.queryBuilder('reset');
      },

      /**
       * Returns a query string as built from the UI rules. You
       * can supply the rules; or they will be taken from the
       * getRules()
       *
       * @returns {*}
       */
      getQuery: function(rules) {
        if (!rules)
          rules = this.getRules();

        var query = this.rulesTranslator.buildQuery(rules);

        return query || '';

      },



      /**
       * Converts qtree (as returned by SOLR query parser) into UI rules
       * (that can be used by the UI builder)
       *
       * @param qtree
       */
      getRulesFromQTree: function(qtree) {
        var rules = this.rulesTranslator.convertQTreeToRules(qtree);

        // we need to check/modify the rules to fit the constraints
        // that we are using
        return this.checkRulesConstraints(rules);
      },




      /**
       * The main logic to execute when we want to update the UI Query Builder
       * using *query string*
       *
       * It returns the promise object from the QTreeGetter
       *
       * @see: this.buildQTreeGetter
       */
      updateQueryBuilder: function(query) {

        if (!this.qtreeGetter) {
          throw new Error('You must provide qtreeGetter object for this to work');
        }

        var self = this;
        //first parse the query string into qtree
        return this.qtreeGetter.getQTree(query).done(function(qtree) {
          // insert the original query string
          qtree.originalQuery = query;

          //translate qtree into 'rules'
          var rules = self.getRulesFromQTree(qtree);
          // and update the UI builder with them
          self.setRules(rules);
        });
      },

      /**
       * Check/modify the UI rules as extracted from the QTree
       * we'll add the specific logic, eg. that certain fields
       * can use only certain operators (even if the grammar
       * allows them to use all possible combinations)
       *
       * @param UIRules
       */
      checkRulesConstraints: function(UIRules) {
        this._checkRulesConstraints(UIRules);
        return UIRules;
      },

      _checkRulesConstraints: function(uiRules) {
        /*if (uiRules.field) {
          var m;
          if (m = this.operatorMap[uiRules.field]) {
            if (m[uiRules.operator]) {
              uiRules.operator = m[uiRules.operator];
            }
            else {
              throw new Error("Operator mapping is missing a value for:" + JSON.stringify(uiRules) + ' we have: ' + JSON.stringify(m));
            }
          }
        }*/

        if (uiRules.rules) {
          _.each(uiRules.rules, function(r) {
            this._checkRulesConstraints(r);
          }, this);
        }
      },


      /**
       * Returns true if the UI was changed (ie. user did something that changed the
       * original parse tree)
       *
       */
      isDirty: function() {
        try {
          if (this._rules && this.getQuery(this._rules) != this.getQuery()) {
            return true;
          }
        }
        catch(err) {
          console.trace(err);
          return true;
        }
        return false;
      },


      setQTreeGetter: function(getter) {
        this.qtreeGetter = getter;
      },

      /**
       * Attach a monitor to the UI - the callback will be called
       * once there is a change to the UI (ie. user initiated change)
       *
       * @param callback
       * @param delay
       */
      attachHeartBeat: function(callback, delay) {
        if (!delay) {
          delay = 100;
        }

        var throttled = _.debounce(callback, delay);

        this.$el.on('change.queryBuilder', function(ev) {
          throttled();
        });
        this.$el.on('click.queryBuilder', function(ev) {
          throttled();
        });
        this.$el.on('keypress.input', function(ev) {
          throttled();
        });
      }

    });

    /**
     * Convenience method to build a QTree getter object; it requires access
     * to the BeehIVE (PubSub)
     *
     * @param beehive
     */
    QueryBuilder.buildQTreeGetter = function(beehive) {

      var getter = function(beehive) {
        this.activate(beehive);
      };

      _.extend(getter.prototype, {

        activate: function(beehive) {
          this.pubsub = beehive.Services.get('PubSub');
          this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, _.bind(this.getResponse, this));
        },

        /**
         * Given an ApiQuery - asks SOLR to give us QTREE; this function
         * will return a promise. When the promise is resolved, the function
         * will receive ApiResponse
         *
         * @param query
         * @returns {Deferred}
         */
        getQTree: function(query) {
          this.promise = $.Deferred();
          this.pubsub.publish(this.pubsub.GET_QTREE, new ApiQuery({'q': query}));
          return this.promise;
        },

        /**
         * This function receives ApiResponse from the PubSub - usually as a
         * response to the request to parse a query.
         *
         * @param apiResponse
         */
        getResponse: function(apiResponse) {
          var qtree = JSON.parse(apiResponse.get('qtree'));
          if (this.promise && qtree) {
            this.promise.resolve(qtree);
          }
        }
      });

      return new getter(beehive);
    };

    return QueryBuilder;
  });