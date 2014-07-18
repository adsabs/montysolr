define(['jquery',
  'underscore',
  'js/components/query_builder/plugin',
  'js/components/generic_module',
  'js/bugutils/minimal_pubsub'
], function ($, _, QueryBuilderPlugin, GenericModule, MinimalPubsub) {

  describe("QueryBuilder Query to UI (translation rules)", function () {


      var minsub, expectedQTree;
      beforeEach(function (done) {

        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            var query = apiRequest.get('query');
            return {'responseHeader': {'status': 0, 'QTime': 0, params: query.toJSON()},
              'qtree': JSON.stringify(expectedQTree)};
          }
        }))({verbose: false});
        done();
      });

      afterEach(function (done) {
        minsub.close();
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });


      it("foo", function (done) {
        var p = new QueryBuilderPlugin(
          {qtreeGetter: QueryBuilderPlugin.buildQTreeGetter(minsub.beehive.getHardenedInstance())});


        expectedQTree = {"name": "OPERATOR", "label": "DEFOP", "children": [
          {"name": "MODIFIER", "label": "MODIFIER", "children": [
            {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
              {"name": "FIELD", "label": "FIELD", "children": [
                {"name": "QNORMAL", "label": "QNORMAL", "children": [
                  {"name": "TERM_NORMAL", "input": "foo", "start": 0, "end": 2}
                ] }
              ] }
            ] }
          ] }
        ] };

        var promise = p.updateQueryBuilder("foo");

        promise.done(function (qtree) {
          var rules = p.getRulesFromQTree(qtree);

          var expected =
          {"rules": [
            {"id": "__all__", "field": "__all__", "type": "string", "operator": "is", "value": "foo"}
          ]};

          expect(rules).to.be.eql(expected);
          expect(p.getQuery(rules)).to.be.eql("foo");

          done();
        });

      });

      it("title:foo AND (bar OR bla)", function (done) {
        var p = new QueryBuilderPlugin(
          {qtreeGetter: QueryBuilderPlugin.buildQTreeGetter(minsub.beehive.getHardenedInstance())});


        expectedQTree = {"name": "OPERATOR", "label": "DEFOP", "children": [
          {"name": "OPERATOR", "label": "AND", "children": [
            {"name": "MODIFIER", "label": "MODIFIER", "children": [
              {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                {"name": "FIELD", "label": "FIELD", "children": [
                  {"name": "TERM_NORMAL", "input": "title", "start": 0, "end": 4},
                  {"name": "QNORMAL", "label": "QNORMAL", "children": [
                    {"name": "TERM_NORMAL", "input": "foo", "start": 6, "end": 8}
                  ] }
                ] }
              ] }
            ] },
            {"name": "CLAUSE", "label": "CLAUSE", "children": [
              {"name": "OPERATOR", "label": "DEFOP", "children": [
                {"name": "OPERATOR", "label": "OR", "children": [
                  {"name": "MODIFIER", "label": "MODIFIER", "children": [
                    {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                      {"name": "FIELD", "label": "FIELD", "children": [
                        {"name": "QNORMAL", "label": "QNORMAL", "children": [
                          {"name": "TERM_NORMAL", "input": "bar", "start": 15, "end": 17}
                        ] }
                      ] }
                    ] }
                  ] },
                  {"name": "MODIFIER", "label": "MODIFIER", "children": [
                    {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                      {"name": "FIELD", "label": "FIELD", "children": [
                        {"name": "QNORMAL", "label": "QNORMAL", "children": [
                          {"name": "TERM_NORMAL", "input": "bla", "start": 22, "end": 24}
                        ] }
                      ] }
                    ] }
                  ] }
                ] }
              ] }
            ] }
          ] }
        ] };

        var promise = p.updateQueryBuilder("title:foo AND (bar OR bla)");

        promise.done(function (qtree) {
          var rules = p.getRulesFromQTree(qtree);

          var expected =
          {"condition": "AND", "rules": [
            {"id": "title", "field": "title", "type": "string", "operator": "is", "value": "foo"},
            {"condition": "OR", "rules": [
              {"id": "__all__", "field": "__all__", "type": "string", "operator": "is", "value": "bar"},
              {"id": "__all__", "field": "__all__", "type": "string", "operator": "is", "value": "bla"}
            ]}
          ]};

          expect(rules).to.be.eql(expected);
          expect(p.getQuery(rules)).to.be.eql("title:foo AND (bar OR bla)");

          done();
        });

      });


      it("title:(foo bar)", function (done) {
        var p = new QueryBuilderPlugin(
          {qtreeGetter: QueryBuilderPlugin.buildQTreeGetter(minsub.beehive.getHardenedInstance())});


        expectedQTree = {"name": "OPERATOR", "label": "DEFOP", "children": [
          {"name": "MODIFIER", "label": "MODIFIER", "children": [
            {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
              {"name": "FIELD", "label": "FIELD", "children": [
                {"name": "TERM_NORMAL", "input": "title", "start": 0, "end": 4},
                {"name": "CLAUSE", "label": "CLAUSE", "children": [
                  {"name": "OPERATOR", "label": "DEFOP", "children": [
                    {"name": "MODIFIER", "label": "MODIFIER", "children": [
                      {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                        {"name": "FIELD", "label": "FIELD", "children": [
                          {"name": "QNORMAL", "label": "QNORMAL", "children": [
                            {"name": "TERM_NORMAL", "input": "foo", "start": 7, "end": 9}
                          ] }
                        ] }
                      ] }
                    ] },
                    {"name": "MODIFIER", "label": "MODIFIER", "children": [
                      {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                        {"name": "FIELD", "label": "FIELD", "children": [
                          {"name": "QNORMAL", "label": "QNORMAL", "children": [
                            {"name": "TERM_NORMAL", "input": "bar", "start": 11, "end": 13}
                          ] }
                        ] }
                      ] }
                    ] }
                  ] }
                ] }
              ] }
            ] }
          ] }
        ] };

        var promise = p.updateQueryBuilder("title:(foo bar)");

        promise.done(function (qtree) {
          var rules = p.getRulesFromQTree(qtree);

          var expected =
          {"rules": [
            {"id": "title", "field": "title", "type": "string", "operator": "contains", "value": "foo bar"}
          ]};

          expect(rules).to.be.eql(expected);
          expect(p.getQuery(rules)).to.be.eql("title:(foo bar)");

          done();
        });

      });

      it("topn(title:\"star\",1,2)", function (done) {
        var p = new QueryBuilderPlugin(
          {qtreeGetter: QueryBuilderPlugin.buildQTreeGetter(minsub.beehive.getHardenedInstance())});


        expectedQTree = {"name": "OPERATOR", "label": "DEFOP", "children": [
          {"name": "CLAUSE", "label": "CLAUSE", "children": [
            {"name": "MODIFIER", "label": "MODIFIER", "children": [
              {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                {"name": "QFUNC", "label": "QFUNC", "children": [
                  {"name": "FUNC_NAME", "input": "topn(", "start": 0, "end": 4},
                  {"name": "OPERATOR", "label": "DEFOP", "children": [
                    {"name": "MODIFIER", "label": "MODIFIER", "children": [
                      {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                        {"name": "FIELD", "label": "FIELD", "children": [
                          {"name": "TERM_NORMAL", "input": "title", "start": 5, "end": 9},
                          {"name": "QPHRASE", "label": "QPHRASE", "children": [
                            {"name": "PHRASE", "input": "\"star\"", "start": 11, "end": 16}
                          ] }
                        ] }
                      ] }
                    ] },
                    {"name": "MODIFIER", "label": "MODIFIER", "children": [
                      {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                        {"name": "FIELD", "label": "FIELD", "children": [
                          {"name": "QDELIMITER", "label": "QDELIMITER", "children": [
                            {"name": "COMMA", "input": ",", "start": 17, "end": 17}
                          ] }
                        ] }
                      ] }
                    ] },
                    {"name": "MODIFIER", "label": "MODIFIER", "children": [
                      {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                        {"name": "FIELD", "label": "FIELD", "children": [
                          {"name": "QNORMAL", "label": "QNORMAL", "children": [
                            {"name": "NUMBER", "input": "1", "start": 18, "end": 18}
                          ] }
                        ] }
                      ] }
                    ] },
                    {"name": "MODIFIER", "label": "MODIFIER", "children": [
                      {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                        {"name": "FIELD", "label": "FIELD", "children": [
                          {"name": "QDELIMITER", "label": "QDELIMITER", "children": [
                            {"name": "COMMA", "input": ",", "start": 19, "end": 19}
                          ] }
                        ] }
                      ] }
                    ] },
                    {"name": "MODIFIER", "label": "MODIFIER", "children": [
                      {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                        {"name": "FIELD", "label": "FIELD", "children": [
                          {"name": "QNORMAL", "label": "QNORMAL", "children": [
                            {"name": "NUMBER", "input": "2", "start": 20, "end": 20}
                          ] }
                        ] }
                      ] }
                    ] }
                  ] },
                  {"name": "RPAREN", "input": ")", "start": 21, "end": 21}
                ] }
              ] }
            ] }
          ] }
        ] };

        var promise = p.updateQueryBuilder("topn(title:\"star\",1,2)");

        promise.done(function (qtree) {
          var rules = p.getRulesFromQTree(qtree);

          var expected =
          {"rules": [
            {"id": "topn()", "field": "topn()", "type": "string", "operator": "is_function", "value": "title:\"star\"|1|2"}
          ]};

          expect(rules).to.be.eql(expected);
          expect(p.getQuery(rules)).to.be.eql("topn(title:\"star\", 1, 2)");

          done();
        });

      });

      it("citations(references(instructive(foo)))", function (done) {
        var p = new QueryBuilderPlugin(
          {qtreeGetter: QueryBuilderPlugin.buildQTreeGetter(minsub.beehive.getHardenedInstance())});


        expectedQTree = {"name": "OPERATOR", "label": "DEFOP", "children": [
          {"name": "CLAUSE", "label": "CLAUSE", "children": [
            {"name": "MODIFIER", "label": "MODIFIER", "children": [
              {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                {"name": "QFUNC", "label": "QFUNC", "children": [
                  {"name": "FUNC_NAME", "input": "citations(", "start": 0, "end": 9},
                  {"name": "OPERATOR", "label": "DEFOP", "children": [
                    {"name": "CLAUSE", "label": "CLAUSE", "children": [
                      {"name": "MODIFIER", "label": "MODIFIER", "children": [
                        {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                          {"name": "QFUNC", "label": "QFUNC", "children": [
                            {"name": "FUNC_NAME", "input": "references(", "start": 10, "end": 20},
                            {"name": "OPERATOR", "label": "DEFOP", "children": [
                              {"name": "CLAUSE", "label": "CLAUSE", "children": [
                                {"name": "MODIFIER", "label": "MODIFIER", "children": [
                                  {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                                    {"name": "QFUNC", "label": "QFUNC", "children": [
                                      {"name": "FUNC_NAME", "input": "instructive(", "start": 21, "end": 32},
                                      {"name": "OPERATOR", "label": "DEFOP", "children": [
                                        {"name": "MODIFIER", "label": "MODIFIER", "children": [
                                          {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                                            {"name": "FIELD", "label": "FIELD", "children": [
                                              {"name": "QNORMAL", "label": "QNORMAL", "children": [
                                                {"name": "TERM_NORMAL", "input": "foo", "start": 33, "end": 35}
                                              ] }
                                            ] }
                                          ] }
                                        ] }
                                      ] },
                                      {"name": "RPAREN", "input": ")", "start": 36, "end": 36}
                                    ] }
                                  ] }
                                ] }
                              ] }
                            ] },
                            {"name": "RPAREN", "input": ")", "start": 37, "end": 37}
                          ] }
                        ] }
                      ] }
                    ] }
                  ] },
                  {"name": "RPAREN", "input": ")", "start": 38, "end": 38}
                ] }
              ] }
            ] }
          ] }
        ] };

        var promise = p.updateQueryBuilder("citations(references(instructive(foo)))");

        promise.done(function (qtree) {
          var rules = p.getRulesFromQTree(qtree);

          var expected =
          {"rules": [
            {"id": "citations()", "field": "citations()", "type": "string", "operator": "is_function", "value": "references(instructive(foo))"}
          ]};

          expect(rules).to.be.eql(expected);
          expect(p.getQuery(rules)).to.be.eql("citations(references(instructive(foo)))");

          done();
        });

      });

      it("catataons(foo OR bar)", function (done) {
        var p = new QueryBuilderPlugin(
          {qtreeGetter: QueryBuilderPlugin.buildQTreeGetter(minsub.beehive.getHardenedInstance())});


        expectedQTree = {"name": "OPERATOR", "label": "DEFOP", "children": [
          {"name": "CLAUSE", "label": "CLAUSE", "children": [
            {"name": "MODIFIER", "label": "MODIFIER", "children": [
              {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                {"name": "QFUNC", "label": "QFUNC", "children": [
                  {"name": "FUNC_NAME", "input": "catataons(", "start": 0, "end": 9},
                  {"name": "OPERATOR", "label": "DEFOP", "children": [
                    {"name": "OPERATOR", "label": "OR", "children": [
                      {"name": "MODIFIER", "label": "MODIFIER", "children": [
                        {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                          {"name": "FIELD", "label": "FIELD", "children": [
                            {"name": "QNORMAL", "label": "QNORMAL", "children": [
                              {"name": "TERM_NORMAL", "input": "foo", "start": 10, "end": 12}
                            ] }
                          ] }
                        ] }
                      ] },
                      {"name": "MODIFIER", "label": "MODIFIER", "children": [
                        {"name": "TMODIFIER", "label": "TMODIFIER", "children": [
                          {"name": "FIELD", "label": "FIELD", "children": [
                            {"name": "QNORMAL", "label": "QNORMAL", "children": [
                              {"name": "TERM_NORMAL", "input": "bar", "start": 17, "end": 19}
                            ] }
                          ] }
                        ] }
                      ] }
                    ] }
                  ] },
                  {"name": "RPAREN", "input": ")", "start": 20, "end": 20}
                ] }
              ] }
            ] }
          ] }
        ] };

        var promise = p.updateQueryBuilder("catataons(foo OR bar)");

        promise.done(function (qtree) {
          var rules = p.getRulesFromQTree(qtree);

          var expected =
          {"rules": [
            {"id": "black_hole", "field": "black_hole", "type": "string", "operator": "is_literal", "value": "catataons(foo OR bar)"}
          ]};

          expect(rules).to.be.eql(expected);
          expect(p.getQuery(rules)).to.be.eql("catataons(foo OR bar)");

          done();
        });

      });

    }
  );
});