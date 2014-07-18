/**
 * Created by rchyla on 6/23/14.
 *
 * This component provides translation between SOLR Qtree and the
 * Js QueryBuilder UI rules.
 *
 */

define(['underscore',
    'js/components/generic_module',
    'js/components/api_query_updater'],
  function (_,
            GenericModule,
            ApiQueryUpdater) {


    var TreeNode = function (operator, value) {
      this.operator = operator;
      this.value = value;
      this.children = [];
    };
    TreeNode.prototype.addChild = function (childNode) {
      this.children.push(childNode);
    };
    TreeNode.prototype.addChildren = function (childNodes) {
      this.children = _.union(this.children, childNodes);
    };
    TreeNode.prototype.toString = function (level) {
      if (_.isUndefined(level))
        level = 0; // root

      if (this.value) { // leaf node
        return this.value;
      }

      var queries = [];
      _.each(this.children, function (child, index, list) {
        queries.push(child.toString(level+1));
      });

      var q = queries.join(' ' + this.operator + ' ');
      if (level > 0) {
        q = "(" + q + ")";
      }

      return q;
    };


    var RuleNode = function() {
      this.validOperators = {'AND': 'AND', 'OR': 'OR', 'DEFOP': 'DEFOP'};
    };
    _.extend(RuleNode.prototype, {
      setCondition: function(val) {
        this.rules = this.rules || [];
        if (!val) {
          this.condition = this.validOperators.DEFOP || 'AND';
          return;
        }
        if (!this.validOperators.hasOwnProperty(val))
          throw new Error("Unknown operator: " + val);
        this.condition = this.validOperators[val];
      },
      _serializeValue: function() {
        if (!this.value)
          return;
        var v = this.value;
        if (this.fuzzy)
          v += '~' + this.fuzzy;
        if (this.boost)
          v += '^' + this.boost;
        return v;
      },
      _modifyOperator: function(ret) {
        if (this.modifier) {
          if (this.modifier == '-') {
            ret.operator = ret.operator + '_not';
          }
          else if (this.modifier == '=') {
            ret.operator = ret.operator + '_exactly';
          }
        }
        return ret;
      },
      toJSON: function() {
        var ret = _.clone(_.pick(this, ['condition', 'id', 'field', 'type', 'input', 'operator']));
        var v = this._serializeValue();
        if (v)
          ret.value = v;
        this._modifyOperator(ret);
        if (this.rules) {
          ret.rules = [];
          _.each(this.rules, function(rule) {
            ret.rules.push(rule.toJSON());
          });
        }
        return ret;
      },
      setValue: function(val, type) {
        this.value = val;
        this.type = type || 'string';
      },
      setOffset: function(x) {
        this.offset = x;
      },
      setType: function(t) {
        this.type = t;
      },
      setEnd: function(x) {
        this.end = x;
      },
      getField: function() {
        return this.field;
      },
      setField: function(f) {
        this.field = f;
        this.id = f;
      },
      setModifier: function(m) {
        this.modifier = m;
      },
      getModifier: function() {
        return this.modifier;
      },
      setFuzzy: function(f) {
        this.fuzzy = f.replace('~', '');
      },
      setBoost: function(v) {
        this.boost = v.replace('^', '');
      },
      setOperator: function(o) {
        this.operator = o;
      },
      addChild: function(ruleNode) {
        this.rules = this.rules || [];
        this.rules.push(ruleNode);
      }
    });


    var RulesTranslator = GenericModule.extend({
      initialize: function(options) {
        this.apiQueryUpdater = new ApiQueryUpdater('rulesTranslator');
        this.validFunctions = {
          'topn()': true,
          'citations()': true,
          'references()': true,
          'instructive()': true,
          'trending()': true
        };
      },

      /**
       * Converts QTree into the UI Rules. SOLR query parser returns
       * the QTree representation of the query string; which we turn
       * into rules that UI builder can use
       *
       * Original query:
       *    author:Roman AND (title:galaxy OR abstract:42)
       *
       * Typical QTree input:
       *
       * {"name":"OPERATOR", "label":"DEFOP", "children": [
       *   {"name":"MODIFIER", "label":"MODIFIER", "children": [
       *     {"name":"TMODIFIER", "label":"TMODIFIER", "children": [
       *       {"name":"FIELD", "label":"FIELD", "children": [
       *         {"name":"TERM_NORMAL", "input":"title", "start":0, "end":4},
       *         {"name":"QNORMAL", "label":"QNORMAL", "children": [
       *           {"name":"TERM_NORMAL", "input":"joe", "start":6, "end":8}]
       *         }]
       *       }]
       *     }]
       *   },
       *   {"name":"MODIFIER", "label":"MODIFIER", "children": [
       *     {"name":"TMODIFIER", "label":"TMODIFIER", "children": [
       *       {"name":"FIELD", "label":"FIELD", "children": [
       *         {"name":"QNORMAL", "label":"QNORMAL", "children": [
       *           {"name":"TERM_NORMAL", "input":"doe", "start":10, "end":12}]
       *         }]
       *       }]
       *     }]
       *   }]
       * };
       *
       * Output: see docstring for `buildQuery()`
       *
       * @param qtree
       */
      convertQTreeToRules: function(qtree) {

        if (!qtree)
          throw new Error("Empty qtree");

        this._parentize(qtree);

        var root = this._extractRules(qtree);

        if (!root.rules) {
          var r = new RuleNode();
          r.addChild(root);
          root = r;
        }

        //console.log('convertQTreeToRules', JSON.stringify(root.toJSON()));
        return root.toJSON();
      },


      /**
       * Adds reference to the parent to each of the node
       *
       * @param qtree
       * @param parent
       * @private
       */
      _parentize: function(qtree, parent) {
        var self = this;
        qtree.parent = parent;
        _.each(qtree.children, function(node) {
          self._parentize(node, qtree);
        })
      },

      _extractRules: function(qtree) {

        var root = new RuleNode();
        this._extractRule(qtree, root);
        return root;
      },

      _extractRule: function(qtree, ruleNode) { // ruleNode can be null
        var ruleNode, inputNode;
        var self = this;
        //console.log('extracting', qtree.name, ruleNode);

        switch (qtree.name) {
          case 'OPERATOR':

            if (qtree.children.length == 1) {
              this._extractRule(qtree.children[0], ruleNode);
              break;
            }

            ruleNode.setCondition(qtree.label);
            _.each(qtree.children, function(child) {
              var newGroup = new RuleNode();
              this._extractRule(child, newGroup);
              ruleNode.addChild(newGroup);
            }, this);

            break;
          case 'CLAUSE':

            var childr = qtree;
            while (childr.children && childr.children.length == 1 && childr.children[0].name == 'OPERATOR') {
              childr = childr.children[0];
            }

            if (childr !== qtree) {
              ruleNode.setCondition(childr.label);
              _.each(childr.children, function(child) {
                var newGroup = new RuleNode();
                this._extractRule(child, newGroup);
                ruleNode.addChild(newGroup);
              }, this);
              break;
            }
            else {
              _.each(childr.children, function(child) {
                this._extractRule(child, ruleNode);
              }, this);
            }

            break;

          case 'FIELD':
            if (qtree.children.length == 2) {

              if (qtree.children[1].name == 'CLAUSE') { // field:(foo bar)
                var field = qtree.children[0].input;
                var values = [];

                _.each(qtree.children[1].children, function(qt) {
                  var rule = new RuleNode();
                  this._extractRule(qt, rule);
                  values.push(this.buildQuery(rule));
                }, this);

                var result = new RuleNode();
                result.setField(field);
                result.setValue(values.join(' '));
                result.setOperator('contains');
                ruleNode.addChild(result);
              }
              else {
                ruleNode.setField(qtree.children[0].input);
                this._extractRule(qtree.children[qtree.children.length-1], ruleNode);
              }
            }
            else { // unfielded search
              ruleNode.setField('__all__');
              this._extractRule(qtree.children[qtree.children.length-1], ruleNode);
            }
            break;
          case 'MODIFIER':
            if (qtree.children.length == 2)
              ruleNode.setModifier(qtree.children[0].name);
            this._extractRule(qtree.children[qtree.children.length-1], ruleNode);
            break;
          case 'TMODIFIER':
            _.each(qtree.children, function(c) {
              self._extractRule(c, ruleNode);
            });
            break;
          case 'BOOST':
            if (qtree.children.length > 0)
              ruleNode.setBoost(qtree.children[0].label);
            break;
          case 'FUZZY':
            if (qtree.children.length > 0)
              ruleNode.setFuzzy(qtree.children[0].label);
            break;
          case 'QNORMAL':
            ruleNode.setValue(qtree.children[0].input);
            ruleNode.setOffset(qtree.children[0].start);
            ruleNode.setEnd(qtree.children[0].end);
            ruleNode.setOperator('is');
            break;
          case 'QPHRASE':
            var input =  qtree.children[0].input;
            ruleNode.setValue(input.substring(1, input.length-1));
            ruleNode.setOffset(qtree.children[0].start+1);
            ruleNode.setEnd(qtree.children[0].end-1);
            ruleNode.setOperator('is_phrase');
            break;
          case 'QPHRASETRUNC':
            var input =  qtree.children[0].input.trim();
            if (input.substring(input.length-1) == '*') {
              input = input.substring(0, input.length-1);
              ruleNode.setOperator('starts_with');
            }
            else {
              ruleNode.setOperator('is_wildcard');
            }
            ruleNode.setValue(input);
            ruleNode.setOffset(qtree.children[0].start+1);
            ruleNode.setEnd(qtree.children[0].end-1);
            break;
          case 'QTRUNCATED':
            var input =  qtree.children[0].input.trim();
            if (input.substring(input.length-1) == '*') {
              input = input.substring(0, input.length-1);
              ruleNode.setOperator('starts_with');
            }
            else {
              ruleNode.setOperator('is_wildcard');
            }
            ruleNode.setValue(input);
            ruleNode.setOffset(qtree.children[0].start);
            ruleNode.setEnd(qtree.children[0].end);
            break;
          case 'QRANGEEX':
          case 'QRANGEIN':
          case 'QANYTHING':
            ruleNode.setOperator('is_not_empty');
            break;
          case 'QDATE':
            break;
          case 'QPOSITION':
            if (qtree.children[0].name == 'AUTHOR_SEARCH') {
              var input =  qtree.children[0].input.trim();
              ruleNode.setValue(input.replace('^', ''));
              ruleNode.setField('^author');
              ruleNode.setOperator('is');
            }
            else {
              _.each(qtree.children, function(child) {
                this._extractRule(child, ruleNode);
              }, this);
            }
            break;
          case 'QFUNC':
            var funcName = qtree.children[0].input.replace('(', '()');
            var vals = this.extractFunctionValues(funcName, qtree.children[1]);
            if (vals) {
              ruleNode.setValue(vals.join('|'));
              ruleNode.setField(funcName);
              if (ruleNode.getModifier() == 'MINUS') {
                ruleNode.setOperator('is_not_function');
              }
              else {
                ruleNode.setOperator('is_function');
              }
            }
            else {

              // in the more complex case we will simply extract the string by position

              var originalQuery = this._getOriginalQuery(qtree);
              if (!originalQuery) {
                throw new Error('Eeeek, we can\'t extract function values - bummmmmmer! Sorry boss');
              }

              var offset = qtree.children[0].end + 1;
              var end = qtree.children[qtree.children.length - 1].end;

              if (!(offset && end)) {
                throw new Error('Eeeek, this is a weird query tree, i don\'t know how to parse it');
              }

              ruleNode.setValue(qtree.children[0].input + originalQuery.substring(offset, end) + ')');
              ruleNode.setField('black_hole');
              ruleNode.setOperator('is_literal');
            }
            break;
          case 'QDELIMITER':
            // ignore
            break;
          case 'QIDENTIFIER':
          case 'QCOORDINATE':
          case 'QREGEX':
            throw new Error('Not yet ready for: ' + JSON.stringify(qtree));
            break;
          default:
            console.log('skipping', qtree);
            break;

        }


        //console.log('_extractRule', qtree.name, JSON.stringify(ruleNode));
        return ruleNode;
      },


      /**
       * This function can construrct a query (as a string) from the
       * UIQueryBuilder rules. Typically, this is the how the input
       * looks like:
       *
       * {
       *   "condition": "AND",
       *   "rules": [
       *     {
       *       "id": "author",
       *       "field": "author",
       *       "type": "string",
       *       "input": "text",
       *       "operator": "is",
       *       "value": "Roman"
       *     },
       *     {
       *       "condition": "OR",
       *       "rules": [
       *         {
       *           "id": "title",
       *           "field": "title",
       *           "type": "string",
       *           "input": "text",
       *           "operator": "contains",
       *           "value": "galaxy"
       *         },
       *         {
       *           "id": "abstract",
       *           "field": "abstract",
       *           "type": "string",
       *           "input": "text",
       *           "operator": "contains",
       *           "value": "42"
       *         }
       *       ]
       *     }
       *   ]
       * }
       *
       * And the output will be:
       *
       *   author:Roman AND (title:galaxy OR abstract:42)
       *
       * @param rules
       * @returns String
       */
      buildQuery: function (rules) {

        if (rules.rules) {
          var root = new TreeNode(rules.condition);
          var tree = this._buildQueryTree(root, rules.rules);
          if (tree) {

            // final modifications (removing some of the unnecessary details)
            return tree.toString().split(' DEFOP ').join(' ').split('__all__:').join('');

          }
        }
        else {
          var root = new TreeNode('DEFOP');
          var tree = this._buildQueryTree(root, [rules]);
          if (tree) {
            // final modifications (removing some of the unnecessary details)
            return tree.toString().split(' DEFOP ').join(' ').split('__all__:').join('');
          }
        }

        return null;
      },

      _buildQueryTree: function (treeNode, rules) {
        var self = this;
        if (rules && rules.length > 0) {
          _.each(rules, function(rule){
            if (rule.condition) {
              var node = new TreeNode(rule.condition);
              treeNode.addChild(node);
              self._buildQueryTree(node, rule.rules);
            }
            else if (rule.rules) {
              self._buildQueryTree(treeNode, rule.rules);
            }
            else {
              var node = self._buildOneRule(rule);
              if (node) {
                treeNode.addChild(node);
              }
            }
          });
        }
        return treeNode;
      },

      _buildOneRule: function(rule) {
        var val, q, field;
        if (rule.type == 'string') {
          var input = rule.value.trim();

          field = rule.field || '__all__';

          switch(rule.operator) {

            case 'is_phrase':
            case 'is_not_phrase':
            case 'contains_phrase':
            case 'contains_not_phrase':

              val = this.apiQueryUpdater.quote(input);
              q = field + ':' + val;
              if (rule.operator.indexOf('_not') > -1)
                q = '-' + q;
              break;

            case 'is':
            case 'is_not':
              val = this.apiQueryUpdater.quoteIfNecessary(input);
              q = field + ':' + val;
              if (rule.operator.indexOf('_not') > -1)
                q = '-' + q;
              break;
            case 'contains':
            case 'contains_not':
              if (input.indexOf(' ') > -1) {
                val = this.apiQueryUpdater.quoteIfNecessary(input, '(', ')');
              }
              else {
                val = this.apiQueryUpdater.quoteIfNecessary(input, '"', '"');
              }

              q = field + ':' + val;
              if (rule.operator.indexOf('_not') > -1)
                q = '-' + q;
              break;

            case 'is_exactly':
            case 'is_not_exactly':
              q = '=' + field + ':' + this.apiQueryUpdater.quoteIfNecessary(input);
              if (rule.operator.indexOf('_not_') > -1)
                q = 'NOT ' + q;
              break;

            case 'is_wildcard':
            case 'is_not_wildcard':
            case 'starts_with':
            case 'starts_not_with':
              if (input.indexOf('*') > -1 || input.indexOf('?') > -1) { // user input contains '*' - they should know what they do
                input = this.apiQueryUpdater.quoteIfNecessary(input);
              }
              else {
                var newInput = this.apiQueryUpdater.quoteIfNecessary(input);
                if (newInput.length != input.length) {
                  input = newInput.substring(0, newInput.length-1) + "*\"";
                }
                else {
                  input += '*';
                }
              }

              q = field + ':' + input;
              if (rule.operator.indexOf('_not_') > -1)
                q = '-' + q;
              break;


            case 'regex':
              if (field == '__all__') {
                q = 'regex(all,'  + input + ')';
              }
              else {
                q = 'regex(' + field + ', ' + input + ')';
              }
              break;

            case 'is_not_empty':
              if (field == '__all__') {
                q = '*:*';
              }
              else {
                q = field + ':?';
              }
              break;

            case 'is_function':
            case 'is_not_function':
              switch(field) {
                case 'topn()':
                  q = 'topn(' + input.split('|').join(', ') + ')';
                  break;
                default:
                  q = field.replace('()', '(') + input.split('|').join(', ') + ')';
                  //throw 'unknown function' + field;
              }

              if (rule.operator.indexOf('_not_') > -1)
                q = '-' + q;

              break;

            case 'is_literal':
              q = input;
              break;

            default:
              throw new Error('Unknown operator: ' + rule.operator);
          }

          return new TreeNode('', q);
        }
        else {
          throw new Error("Not knowing what to do with: " + JSON.stringify(rule));
        }
      },

      extractFunctionValues: function(funcname, qtree) {

        if (!this.validFunctions[funcname])
          return null;

        var vals = [];
        _.each(qtree.children, function(child) {
          var childNode = new RuleNode();
          this._extractRule(child, childNode);

          // detect more complicated case of the nested queries and bail
          // out; we dont want to handle nested structures, if we give up
          // the parent will simply extract the input inbetween brackets
          //if (childNode.rules && childNode.rules.length() > 1) {
          //  return null;
          //}

          if (!(childNode.value || childNode.rules)) { // commas?
            return;
          }

          //console.log(JSON.stringify(childNode));
          vals.push(this.buildQuery(childNode));
        }, this);
        return vals;
      },

      _getOriginalQuery: function(qtree) {
        var t = qtree;
        while (t.parent) {
          t = t.parent;
        }
        if (t.originalQuery) {
          return t.originalQuery;
        }
        return '';
      },

      addValidFunctions: function(fs) {
        this.validFunctions = _.extend(this.validFunctions, fs);
      },
      setValidFunctions: function(vals) {
        this.validFunctions = vals;
      },
      getValidFunctions: function() {
        return this.validFunctions;
      }


    });

    return RulesTranslator;
  });
