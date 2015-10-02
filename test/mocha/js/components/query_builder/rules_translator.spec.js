
define([
  'jquery',
  'underscore',
  'js/components/query_builder/rules_translator',
  'js/components/generic_module'
  ], function(
  $,
  _,
  RulesTranslator,
  GenericModule) {

  describe("QueryBuilder - Rule Translator (rules_translator.spec.js)", function () {

      it("from UI to the query string", function() {
        var t = new RulesTranslator();

        expect(t.buildQuery({
          "condition": "OR",
          "rules": [
            {
              "condition": "AND",
              "rules": [
                {
                  "id": "author",
                  "field": "author",
                  "type": "string",
                  "input": "text",
                  "operator": "contains",
                  "value": "\"accomazzi,a\" OR \"kurtz,m\""
                }
              ]
            },
            {
              "id": "author",
              "field": "author",
              "type": "string",
              "input": "text",
              "operator": "is",
              "value": "foo"
            }
          ]
        })).to.eql('(author:("accomazzi,a" OR "kurtz,m")) OR author:foo');

        expect(t.buildQuery({
          "rules": [
            {
              "id": "pos()",
              "field": "pos()",
              "type": "string",
              "operator": "is_function",
              "value": "complex AND query|1"
            }
          ]
        })).to.eql('pos(complex AND query, 1)');

        expect(t.buildQuery({
          "rules": [
            {
              "id": "pos()",
              "field": "pos()",
              "type": "string",
              "operator": "is_function",
              "value": "complex AND query|1|10"
            }
          ]
        })).to.eql('pos(complex AND query, 1, 10)');

        expect(t.buildQuery({
          "rules": [
            {
              "id": "trending()",
              "field": "trending()",
              "type": "string",
              "operator": "is_function",
              "value": "complex AND query"
            }
          ]
        })).to.eql('trending(complex AND query)');

        expect(t.buildQuery({
          "rules": [
            {
              "id": "reviews()",
              "field": "reviews()",
              "type": "string",
              "operator": "is_function",
              "value": "complex AND query"
            }
          ]
        })).to.eql('reviews(complex AND query)');

        expect(t.buildQuery({
          "rules": [
            {
              "id": "citations()",
              "field": "citations()",
              "type": "string",
              "operator": "is_function",
              "value": "complex AND query"
            }
          ]
        })).to.eql('citations(complex AND query)');

        expect(t.buildQuery({
          "rules": [
            {
              "id": "references()",
              "field": "references()",
              "type": "string",
              "operator": "is_function",
              "value": "complex AND query"
            }
          ]
        })).to.eql('references(complex AND query)');

        expect(t.buildQuery({
          "rules": [
            {
              "id": "topn()",
              "field": "topn()",
              "type": "string",
              "operator": "is_function",
              "value": "10|complex AND query"
            }
          ]
        })).to.eql('topn(10, complex AND query)');

        expect(t.buildQuery({
          "rules": [
            {
              "id": "topn()",
              "field": "topn()",
              "type": "string",
              "operator": "is_function",
              "value": "10|complex AND query|citation_count desc"
            }
          ]
        })).to.eql('topn(10, complex AND query, citation_count desc)');

      });

      it.skip("qtree -> rules", function() {

        var t = new RulesTranslator();
        var qtree = {"name":"OPERATOR", "label":"DEFOP", "children": [
          {"name":"MODIFIER", "label":"MODIFIER", "children": [
            {"name":"TMODIFIER", "label":"TMODIFIER", "children": [
              {"name":"FIELD", "label":"FIELD", "children": [
                {"name":"TERM_NORMAL", "input":"title", "start":0, "end":4},
                {"name":"QNORMAL", "label":"QNORMAL", "children": [
                  {"name":"TERM_NORMAL", "input":"joe", "start":6, "end":8}]
                }]
              }]
            }]
          },
          {"name":"MODIFIER", "label":"MODIFIER", "children": [
            {"name":"TMODIFIER", "label":"TMODIFIER", "children": [
              {"name":"FIELD", "label":"FIELD", "children": [
                {"name":"QNORMAL", "label":"QNORMAL", "children": [
                  {"name":"TERM_NORMAL", "input":"doe", "start":10, "end":12}]
                }]
              }]
            }]
          }]
        };

        var rules = t.convertQTreeToRules(qtree);
        //console.log(JSON.stringify(rules))
        expect(rules).to.eql({"condition":"DEFOP","rules":[{"id":"title","field":"title","type":"string","operator":"contains","value":"joe"},{"id":"__all__","field":"__all__","type":"string","operator":"contains","value":"doe"}]});
      });

      it.skip("(x OR y)", function() {
        var t = new RulesTranslator();
        var qtree = {"name":"OPERATOR", "label":"DEFOP", "children": [
          {"name":"OPERATOR", "label":"DEFOP", "children": [
            {"name":"OPERATOR", "label":"OR", "children": [
              {"name":"MODIFIER", "label":"MODIFIER", "children": [
                {"name":"TMODIFIER", "label":"TMODIFIER", "children": [
                  {"name":"FIELD", "label":"FIELD", "children": [
                    {"name":"QNORMAL", "label":"QNORMAL", "children": [
                      {"name":"TERM_NORMAL", "input":"x", "start":1, "end":1}]
                    }]
                  }]
                }]
              },
              {"name":"MODIFIER", "label":"MODIFIER", "children": [
                {"name":"TMODIFIER", "label":"TMODIFIER", "children": [
                  {"name":"FIELD", "label":"FIELD", "children": [
                    {"name":"QNORMAL", "label":"QNORMAL", "children": [
                      {"name":"TERM_NORMAL", "input":"y", "start":6, "end":6}]
                    }]
                  }]
                }]
              }]
            }]
          }]
        };

        var rules = t.convertQTreeToRules(qtree);
        console.log(JSON.stringify(rules))
        expect(rules).to.eql({"condition": "DEFOP", "rules": [
          {"condition": "DEFOP", "rules": [
            {"condition": "OR", "rules": [
              {"id": "__all__", "field": "__all__", "type": "string", "operator": "contains", "value": "x"},
              {"id": "__all__", "field": "__all__", "type": "string", "operator": "contains", "value": "y"}
            ]}
          ]}
        ]});

      });


    }
  );
});