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
    'js/components/api_query',
    'hbs!./templates/group_template',
    'hbs!./templates/rule_template'
  ],

  function(
    _,
    Bootstrap,
    $,
    jQueryQueryBuilderPlugin,
    GenericModule,
    RulesTranslator,
    ApiQuery,
    GroupTemplate,
    RuleTemplate
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
            "operator_is_not": "is not",
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

            delete_rule: 'remove',
            delete_group: 'group',
            add_rule: 'term',
            add_group: 'group'


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

        var singleTokenOperators = ['is', 'is_wildcard', 'is_exactly', 'is_not', 'is_not_wildcard'];
        var multiTokenOperators = ['contains', 'is_phrase', 'contains_not', 'is_not_phrase', 'is_wildcard'];
        var functionOperators = ['is_function', 'is_not_function'];

        this.singleTokenOperators = singleTokenOperators;
        this.multiTokenOperators = multiTokenOperators;
        this.functionOperators = functionOperators;


        this.$el.queryBuilder({
          sortable: false,

          filters: [
            {id: '__all__', label: 'Index', type: 'string',
              operators: multiTokenOperators, createOperatorIfNecessary: true,
              placeholder: 'e.g. capillary surfaces'
            },
            {id: 'author', label: 'Author', type: 'string', placeholder: 'Planck, Max',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: '^author', label: 'First Author', type: 'string', placeholder: 'Einstein, A',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'title', label: 'Title', type: 'string',
              operators: multiTokenOperators, createOperatorIfNecessary: true,
              placeholder: 'e.g. energy survey'
            },
            {id: 'abstract', label: 'Abstract', type: 'string',
              operators: multiTokenOperators, createOperatorIfNecessary: true,
              placeholder: 'e.g. foo bar'
            },
            {id: 'keyword', label: 'Keyword', type: 'string',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'full', label: 'Fulltext', type: 'string',
              operators: multiTokenOperators, createOperatorIfNecessary: true},
            {id: 'pos()', label: 'position()', type: 'string',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'any valid query'},
                  {id: 'start', label: 'start', type: 'number', placeholder: 'start position, e.g. 1'},
                  {id: 'end', label: 'end', type: 'number', placeholder: 'end:optional, e.g. 100'}
                ]);
              },
              onAfterSetValue: function($rule, value, filter, operator) {
                var vals = value.split('|');
                $rule.find('input[name=query]').val(vals[0]);
                $rule.find('input[name=start]').val(vals[1]);
                if (vals[2])
                  $rule.find('input[name=end]').val(vals[2]);
              }
            },
            {id: 'citations()', label: 'citations()', type: 'string', placeholder: 'query',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'any valid query'}
                ]);
              },
              onAfterSetValue: function($rule, value, filter, operator) {
                var vals = value.split('|');
                $rule.find('input[name=query]').val(vals[0]);
              }
            },
            {id: 'references()', label: 'references()', type: 'string', placeholder: 'instructive(title:&quot;monte carlo&quot;)',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'any valid query'}
                ]);
              },
              onAfterSetValue: function($rule, value, filter, operator) {
                var vals = value.split('|');
                $rule.find('input[name=query]').val(vals[0]);
              }
            },
            {id: 'trending()', label: 'trending()', type: 'string', placeholder: '(any valid query)',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'any valid query'}
                ]);
              },
              onAfterSetValue: function($rule, value, filter, operator) {
                var vals = value.split('|');
                $rule.find('input[name=query]').val(vals[0]);
              }
            },
            {id: 'reviews()', label: 'reviews()', type: 'string', placeholder: '(any valid query)',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden target'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'any valid query'}
                ]);
              },
              onAfterSetValue: function($rule, value, filter, operator) {
                var vals = value.split('|');
                $rule.find('input[name=query]').val(vals[0]);
              }
            },
            {id: 'topn()', label: 'topn()', type: 'string',
              operators: functionOperators,
              input: function($rule, filter) {
                return this.createFunctionInputs([
                  {id: $rule.attr('id'), label: 'function', type: 'text', placeholder: 'hidden'},
                  {id: 'query', label: 'query', type: 'text', placeholder: 'any valid query'},
                  {id: 'number', label: 'number', type: 'number', placeholder: 'how many results, e.g 50'},
                  {
                    id: 'sorting',
                    label: 'sorting',
                    type: 'string',
                    input: 'select',
                    values: {
                      'citation_count desc': 'Pick first n most cited',
                      'citation_count asc': 'Pick first n least cited',
                      'date desc': 'Pick first n newest',
                      'date asc': 'Pick first n oldest',
                      'relevance': 'Pick first n most relevant'
                    }
                  }
                ]);
              },
              onAfterSetValue: function($rule, value, filter, operator) {
                var vals = value.split('|');
                $rule.find('input[name=number]').val(vals[0]);
                $rule.find('input[name=query]').val(vals[1]);
                if (vals[2])
                  $rule.find('input[name=sorting]').val(vals[3]);
              }
            },

            {id: '_version_', label: 'Version', type: 'string', placeholder: 'xxx',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'ack', label: 'Acknowledgements', type: 'string', placeholder: 'ADS',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'aff', label: 'Affiliation', type: 'string', placeholder: 'Harvard',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'alternate_bibcode', label: 'Alternate Bibcode', type: 'string', placeholder: 'e.g. 2015ASPC..492..208G',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'alternate_title', label: 'Alternate Title', type: 'string', placeholder: 'e.g. freisetzen (de)',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'arxiv_class', label: 'Arxiv category', type: 'string', placeholder: 'e.g. phys',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'author_facet', label: 'Author Facet', type: 'string', placeholder: 'e.g. 1/Einstein, A./Einstein, Albert',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'bibgroup', label: 'Bibliographic Group', type: 'string', placeholder: 'e.g. CFA',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'body', label: 'Body', type: 'string', placeholder: 'will search in full text (minus title, abstract...)',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'citation', label: 'Citation', type: 'string', placeholder: 'e.g. 2015SPIE*',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'citation_count', label: 'Citation count', type: 'string', placeholder: 'e.g. [0 TO 100]',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'comment', label: 'Comment', type: 'string', placeholder: '# reserved for librarians',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'copyright', label: 'Copyright', type: 'string', placeholder: 'e.g. ',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'data', label: 'Data', type: 'string', placeholder: '# reserved for librarians',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'database', label: 'Database', type: 'string', placeholder: 'e.g. physics',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'date', label: 'Date', type: 'string', placeholder: '# e.g. 1976-01-02T00:30:00Z',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'doctype', label: 'Document Type', type: 'string', placeholder: 'e.g. book',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'doi', label: 'DOI', type: 'string', placeholder: 'e.g. 010.1038/srep04183',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'eid', label: 'Electronic ID', type: 'string', placeholder: '# internal use',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'email', label: 'Email', type: 'string', placeholder: 'e.g. adsabs@harvard.edu',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'facility', label: 'Facility', type: 'string', placeholder: 'e.g. CERN',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'grant', label: 'Grant ID', type: 'string', placeholder: 'e.g. NNX12AG54G',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'id', label: 'ID', type: 'string', placeholder: 'e.g. 15',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'identifier', label: 'Identifier', type: 'string', placeholder: 'any bibcode, eid, doi or other ids',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'indexstamp', label: 'Indexstamp', type: 'string', placeholder: '# internal use',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'isbn', label: 'ISBN', type: 'string', placeholder: '',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'issn', label: 'ISSN', type: 'string', placeholder: '',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'issue', label: 'Issue', type: 'string', placeholder: '1',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'lang', label: 'Language', type: 'string', placeholder: '# internal use',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'orcid', label: 'ORCID', type: 'string', placeholder: '0000-0001-8178-9506',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'page', label: 'Page', type: 'string', placeholder: '2',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'property', label: 'Property', type: 'string', placeholder: '# internal use',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'pub', label: 'Publication', type: 'string', placeholder: 'apj',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'pubdate', label: 'Publ. Date', type: 'string', placeholder: '2014-08',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'read_count', label: 'Times read', type: 'string', placeholder: '[10 TO 20]',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'reference', label: 'Reference', type: 'string', placeholder: 'e.g. 2015SPIE*',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'simbid', label: 'SIMBAD ID', type: 'string', placeholder: '# internal use',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'thesis', label: 'Thesis', type: 'string', placeholder: '',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'vizier', label: 'Vizier', type: 'string', placeholder: '',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'volume', label: 'Volume', type: 'string', placeholder: 'e.g. v1',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'year', label: 'Year', type: 'string', placeholder: 'e.g. 2015',
              operators: singleTokenOperators, createOperatorIfNecessary: true},
            {id: 'black_hole', label: 'Literal Input', type: 'string',
              operators: functionOperators, createOperatorIfNecessary: true,
              placeholder: 'e.g. ^"$%^&*"'
            }

          ],
          extend: {
            createFunctionInputs: function(profiles) {
              var $target, values, $current, $container;
              $container = $('<span/>');
              var $target = $(this.getRuleInput(profiles[0].id, profiles[0]));
              $target.addClass('hide');

              for (var i=1; i<profiles.length; i++) {
                var html = this.getRuleInput(profiles[i].id, profiles[i]);
                $current = $(html.replace('_value"', '"'));
                $current.attr('index', i-1);
                $current.change(function() {
                  // update the hidden value
                  var val = $target.val();
                  if (!_.isString(val))
                    return;
                  var vals = val.split("|");
                  var idx = parseInt($(this).attr('index')) || 0;
                  vals[idx] = $(this).val();
                  $target.val(vals.join('|'));
                });
                $container.append($current);
              }
              $container.append($target);
              return $container;
            },

            getGroupTemplate: function(group_id) {

              var conditions = [];
              var l = this.settings.conditions.length, cond;
              for (var i=0; i < l; i++) {
                cond = this.settings.conditions[i];
                conditions.push('<label class="btn btn-xs btn-primary-faded logic-button ' + (this.settings.default_condition == cond ? 'active' : '') + '"><input type="radio" name="'+ group_id +'_cond" value="' + cond + '"' + (this.settings.default_condition == cond ? 'checked' : '') + '>'+ (this.lang[cond.toLowerCase() + '_condition'] || cond) +'</label>');
              }
              conditions = conditions.join('\n');

              var h = '\
<dl id="'+ group_id +'" class="rules-group-container" '+ (this.settings.sortable ? 'draggable="true"' : '') +'> \
  <dt class="rules-group-header"> \
    <div class="btn-group"> \
      ' + conditions + '\
      &nbsp;&nbsp;\
    </div> \
    <div class="btn-group"> \
      <button type="button" class="btn btn-xs btn-success" data-add="rule"><i class="glyphicon glyphicon-plus"></i> '+ this.lang.add_rule +'</button> \
      <button type="button" class="btn btn-xs btn-success" data-add="group"><i class="glyphicon glyphicon-plus-sign"></i> '+ this.lang.add_group +'</button> \
      <button type="button" class="btn btn-xs btn-danger" data-delete="group"><i class="glyphicon glyphicon-remove"></i> '+ this.lang.delete_group +'</button> \
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
              return RuleTemplate({
                rule_id : rule_id,
                sortable: this.settings.sortable,
                deleteRule : this.lang.delete_rule
              });
            }
          }
        });


        // prevent the form from being closed
        this.$el.on('click.queryBuilder', '[data-delete=rule]', function(ev) {
          ev.stopPropagation();
        });
        this.$el.on('click.queryBuilder', '[data-delete=group]', function(ev) {
          ev.stopPropagation();
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
        if (_.isEmpty(rules))
          return '';
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
      isDirty: function(queryToCompare) {

        if (!this.$el.data('queryBuilder'))
          return false;

        try {
          var formQ = this.getQuery();

          if (queryToCompare && formQ && formQ != queryToCompare)
            return true;

          if (formQ == '')
            return false;

          if (this._rules && this.getQuery(this._rules) != formQ) {
            return true;
          }
          else {
            return false;
          }
        }
        catch(err) {
          console.trace(err);
        }
        return true; // safer default
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
      },

      destroy: function() {

        this.$el.off('change.queryBuilder');
        this.$el.off('click.queryBuilder');
        this.$el.off('keypress.input');
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
          this.setBeeHive(beehive);
          var pubsub = this.getPubSub();
          pubsub.subscribe(pubsub.DELIVERING_RESPONSE, _.bind(this.getResponse, this));
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
          this.getPubSub().publish(this.getPubSub().GET_QTREE, new ApiQuery({'q': query}));
          return this.promise;
        },

        /**
         * This function receives ApiResponse from the PubSub - usually as a
         * response to the request to parse a query.
         *
         * @param apiResponse
         */
        getResponse: function(apiResponse) {
          if (!apiResponse.has('qtree'))
            return;

          var qtree = JSON.parse(apiResponse.get('qtree'));
          if (this.promise && qtree) {
            this.promise.resolve(qtree);
          }
        }
      }, Dependon.BeeHive);

      return new getter(beehive);
    };

    return QueryBuilder;
  });
