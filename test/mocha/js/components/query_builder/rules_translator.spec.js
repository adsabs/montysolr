
define(['jquery',
  'underscore',
  'js/components/query_builder/rules_translator',
  'js/components/generic_module'
], function(
  $,
  _,
  RulesTranslator,
  GenericModule) {

  describe("QueryBuilder - Rule Translator (component)", function () {


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