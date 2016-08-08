/**
 * Created by rchyla on 5/14/14.
 */

define([
    'js/widgets/filter_visualizer/widget',
    'js/components/api_query',
    'backbone',
    'jquery',
    "js/bugutils/minimal_pubsub"
  ],
  function (
    FilterVisualizerWidget,
    ApiQuery,
    Backbone,
    $,
    MinSub
  ) {
    describe("FilterVisualizer Widget (filter_visualizer_widget.spec.js)", function () {

      var minsub;

      beforeEach(function (done) {
        minsub = new (MinSub.extend({
          request: function(apiRequest) {
            console.log('called')
            return {some: 'foo'}
          }
        }))({verbose: false});
        done();
      });

      afterEach(function(){
        $("#test").empty()
      })

      it("returns FilterVisualizer object", function () {
        expect(new FilterVisualizerWidget()).to.be.instanceof(FilterVisualizerWidget);
      });

      it("when created, it displays nothing", function () {
        var widget = new FilterVisualizerWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render());

        expect($w.find('#filter-visualizer').text().trim()).to.be.equal("");
      });

      it("displays filters visually (simplified by default)", function (done) {

        var widget = new FilterVisualizerWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render());

        $('#test').append($w);

        widget.processFeedback(minsub.createFeedback({
          code: minsub.T.FEEDBACK.CODES.SEARCH_CYCLE_STARTED,
          numFound: 100,
          query: minsub.createQuery({
            "q": [
              "star"
            ],
            "sort": [
              "date desc"
            ],
            "fq_author": [
              "((author_facet_hier:\"0/Wang, J\") NOT author_facet_hier:\"0/Chen, H\" NOT author_facet_hier:\"0/Zhang, Z\")"
            ],
            "fq": [
              "{!type=aqp cache=false cost=150 v=$fq_author}",
              "{!type=aqp v=$fq_database}"
            ],
            "__author_facet_hier_fq_author": [
              "NOT",
              "(author_facet_hier:\"0/Wang, J\")",
              "author_facet_hier:\"0/Chen, H\"",
              "author_facet_hier:\"0/Zhang, Z\""
            ],
            "fq_database": [
              "(database:physics AND database:astronomy)"
            ],
            "__database_fq_database": [
              "AND",
              "database:physics",
              "database:astronomy"
            ]
          })
        }));


        expect($w.find('#filter-visualizer .filter-topic-group').length).to.equal(5);
        expect($w.find('#filter-visualizer').text().indexOf('Wang, J')).to.be.gt(-1);

        minsub.subscribeOnce(minsub.START_SEARCH, function() {
            done()}
        ); // if this fires, query was issued

        // click on the first
        $w.find('button.filter-operand-remove:first').click();

      });

      it("displays filters visually", function (done) {

        var widget = new FilterVisualizerWidget({withoutOperators: false});
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render());

        $('#test').append($w);

        widget.processFeedback(minsub.createFeedback({
          code: minsub.T.FEEDBACK.CODES.SEARCH_CYCLE_STARTED,
          numFound: 100,
          query: minsub.createQuery({
            "q": [
              "star"
            ],
            "sort": [
              "date desc"
            ],
            "fq_author": [
              "((author_facet_hier:\"0/Wang, J\") OR author_facet_hier:\"0/Chen, H\" OR author_facet_hier:\"0/Zhang, Z\")"
            ],
            "fq": [
              "{!type=aqp cache=false cost=150 v=$fq_author}",
              "{!type=aqp v=$fq_database}"
            ],
            "__author_facet_hier_fq_author": [
              "OR",
              "(author_facet_hier:\"0/Wang, J\")",
              "author_facet_hier:\"0/Chen, H\"",
              "author_facet_hier:\"0/Zhang, Z\""
            ],
            "fq_database": [
              "(database:physics AND database:astronomy)"
            ],
            "__database_fq_database": [
              "AND",
              "database:physics",
              "database:astronomy"
            ]
          })
        }));


        expect($w.find('#filter-visualizer .filter-topic-group').length).to.equal(2);
        expect($w.find('#filter-visualizer').text().indexOf('Wang, J')).to.be.gt(-1);

        minsub.subscribeOnce(minsub.START_SEARCH, function() {
          done()}
        ); // if this fires, query was issued

        // click on the first
        $w.find('button.filter-operand-remove:first').click();

      });

      it("knows how to deal with queries", function() {
        var widget = new FilterVisualizerWidget({withoutOperators: false});

        var q, filters, gui_data, mq;

        q = minsub.createQuery({
          "q": ["star"],
          "sort": ["date desc"]
        });
        filters = widget.extractFilters(q);
        expect(filters).to.eql([]);

        /**
         *  this query is created when you search for 'star' and then filter by facet
         */
        q = minsub.createQuery({
          "q":["star"],
          "sort":["date desc"],
          "fq_author":["(author_facet_hier:\"0/Wang, J\")"],
          "__author_facet_hier_fq_author":["AND","author_facet_hier:\"0/Wang, J\""],
          "fq":["{!type=aqp cache=false cost=150 v=$fq_author}"]}
        );
        filters = widget.extractFilters(q);
        expect(filters[0]).to.eql({
          category: 'Author',
          filter_name: 'fq_author',
          filter_query: "(author_facet_hier:\"0/Wang, J\")",
          filter_key: '__author_facet_hier_fq_author',
          filter_value: ["AND","author_facet_hier:\"0/Wang, J\""]
        });
        expect(filters.length).to.eql(1);
        gui_data = widget.prepareGUIData(filters);

        expect(gui_data).to.eql([
          {
            elements: [{type: 'category', display: 'Author', value: 'fq_author|category|Author'},
            {type: 'operand', display: 'Wang, J', value: 'fq_author|operand|author_facet_hier:"0/Wang, J"'},
            {type: 'control', display: '', value: 'fq_author|control|x'}
            ]
          }
        ]);
        widget._saveInfo(q, filters);
        mq = widget.createModifiedQuery('fq_author|control|x'); // at this point we can execute new query
        expect(mq.toJSON()).to.eql({
          "q":["star"],
          "sort":["date desc"],
        });
        mq = widget.createModifiedQuery('fq_author|operand|author_facet_hier:"0/Wang, J"');
        expect(mq.toJSON()).to.eql({
          "q":["star"],
          "sort":["date desc"],
        });


        /**
         * add other two authors (OR) to the previous filter
         */
        q = minsub.createQuery({
          "q":["star"],
          "sort":["date desc"],
          "fq_author":["((author_facet_hier:\"0/Wang, J\") OR author_facet_hier:\"0/Chen, H\" OR author_facet_hier:\"0/Zhang, Z\")"],
          "fq":["{!type=aqp cache=false cost=150 v=$fq_author}"],
          "__author_facet_hier_fq_author":["OR","(author_facet_hier:\"0/Wang, J\")","author_facet_hier:\"0/Chen, H\"","author_facet_hier:\"0/Zhang, Z\""]
        });
        filters = widget.extractFilters(q);
        expect(filters).to.eql([{
          category: 'Author',
          filter_name: 'fq_author',
          filter_query: "((author_facet_hier:\"0/Wang, J\") OR author_facet_hier:\"0/Chen, H\" OR author_facet_hier:\"0/Zhang, Z\")",
          filter_key: '__author_facet_hier_fq_author',
          filter_value: ["OR", "(author_facet_hier:\"0/Wang, J\")", "author_facet_hier:\"0/Chen, H\"", "author_facet_hier:\"0/Zhang, Z\""]
        }]);
        gui_data = widget.prepareGUIData(filters);
        expect(gui_data).to.eql([
          {elements:[
            {
              "type": "category",
              "display": "Author",
              "value": "fq_author|category|Author"
            },
            {
              "type": "operand",
              "display": "Wang, J",
              "value": "fq_author|operand|(author_facet_hier:\"0/Wang, J\")"
            },
            {
              "type": "operator",
              "display": "OR",
              "value": "fq_author|operator|OR"
            },
            {
              "type": "operand",
              "display": "Chen, H",
              "value": "fq_author|operand|author_facet_hier:\"0/Chen, H\""
            },
            {
              "type": "operator",
              "display": "OR",
              "value": "fq_author|operator|OR"
            },
            {
              "type": "operand",
              "display": "Zhang, Z",
              "value": "fq_author|operand|author_facet_hier:\"0/Zhang, Z\""
            },
            {
              "type": "control",
              "display": "",
              "value": "fq_author|control|x"
            }
          ]}
        ]);
        widget._saveInfo(q, filters);
        mq = widget.createModifiedQuery('fq_author|operand|(author_facet_hier:\"0/Wang, J\")');
        expect(mq.toJSON()).to.eql({
          "q":["star"],
          "sort":["date desc"],
          "fq_author":["(author_facet_hier:\"0/Chen, H\" OR author_facet_hier:\"0/Zhang, Z\")"],
          "fq":["{!type=aqp cache=false cost=150 v=$fq_author}"],
          "__author_facet_hier_fq_author":["OR","author_facet_hier:\"0/Chen, H\"","author_facet_hier:\"0/Zhang, Z\""]
        });


        /**
         * Now mix in the database facet
         */
        q = minsub.createQuery({
          "q": [
            "star"
          ],
          "sort": [
            "date desc"
          ],
          "fq_author": [
            "((author_facet_hier:\"0/Wang, J\") OR author_facet_hier:\"0/Chen, H\" OR author_facet_hier:\"0/Zhang, Z\")"
          ],
          "fq": [
            "{!type=aqp cache=false cost=150 v=$fq_author}",
            "{!type=aqp v=$fq_database}"
          ],
          "__author_facet_hier_fq_author": [
            "OR",
            "(author_facet_hier:\"0/Wang, J\")",
            "author_facet_hier:\"0/Chen, H\"",
            "author_facet_hier:\"0/Zhang, Z\""
          ],
          "fq_database": [
            "(database:physics AND database:astronomy)"
          ],
          "__database_fq_database": [
            "AND",
            "database:physics",
            "database:astronomy"
          ]
        });
        filters = widget.extractFilters(q);
        expect(filters).to.eql([
          {
            category: 'Author',
            filter_name: 'fq_author',
            filter_query: "((author_facet_hier:\"0/Wang, J\") OR author_facet_hier:\"0/Chen, H\" OR author_facet_hier:\"0/Zhang, Z\")",
            filter_key: '__author_facet_hier_fq_author',
            filter_value: ["OR", "(author_facet_hier:\"0/Wang, J\")", "author_facet_hier:\"0/Chen, H\"", "author_facet_hier:\"0/Zhang, Z\""]
          },
          {
            category: 'Collection',
            filter_name: 'fq_database',
            filter_query: "(database:physics AND database:astronomy)",
            filter_key: '__database_fq_database',
            filter_value: [
              "AND",
              "database:physics",
              "database:astronomy"
            ]
          }
        ]);
        gui_data = widget.prepareGUIData(filters);
        expect(gui_data).to.eql([
          {
            "elements": [
              {
                "type": "category",
                "display": "Author",
                "value": "fq_author|category|Author"
              },
              {
                "type": "operand",
                "display": "Wang, J",
                "value": "fq_author|operand|(author_facet_hier:\"0/Wang, J\")"
              },
              {
                "type": "operator",
                "display": "OR",
                "value": "fq_author|operator|OR"
              },
              {
                "type": "operand",
                "display": "Chen, H",
                "value": "fq_author|operand|author_facet_hier:\"0/Chen, H\""
              },
              {
                "type": "operator",
                "display": "OR",
                "value": "fq_author|operator|OR"
              },
              {
                "type": "operand",
                "display": "Zhang, Z",
                "value": "fq_author|operand|author_facet_hier:\"0/Zhang, Z\""
              },
              {
                "type": "control",
                "display": "",
                "value": "fq_author|control|x"
              }
            ]
          },
          {
            "elements": [
              {
                "type": "category",
                "display": "Collection",
                "value": "fq_database|category|Collection"
              },
              {
                "type": "operand",
                "display": "physics",
                "value": "fq_database|operand|database:physics"
              },
              {
                "type": "operator",
                "display": "AND",
                "value": "fq_database|operator|AND"
              },
              {
                "type": "operand",
                "display": "astronomy",
                "value": "fq_database|operand|database:astronomy"
              },
              {
                "type": "control",
                "display": "",
                "value": "fq_database|control|x"
              }
            ]
          }
        ]);

        widget._saveInfo(q, filters);
        mq = widget.createModifiedQuery('fq_database|operand|database:astronomy');
        expect(mq.toJSON()).to.eql({
          "q": [
            "star"
          ],
          "sort": [
            "date desc"
          ],
          "fq_author": [
            "((author_facet_hier:\"0/Wang, J\") OR author_facet_hier:\"0/Chen, H\" OR author_facet_hier:\"0/Zhang, Z\")"
          ],
          "fq": [
            "{!type=aqp cache=false cost=150 v=$fq_author}",
            "{!type=aqp v=$fq_database}"
          ],
          "__author_facet_hier_fq_author": [
            "OR",
            "(author_facet_hier:\"0/Wang, J\")",
            "author_facet_hier:\"0/Chen, H\"",
            "author_facet_hier:\"0/Zhang, Z\""
          ],
          "fq_database": [
            "database:physics"
          ],
          "__database_fq_database": [
            "AND",
            "database:physics"
          ]
        });

      });

      it("has can build simpler gui version", function() {
        var widget = new FilterVisualizerWidget({withoutOperators: true});
        var gui_data = widget.prepareGUIData([
          {
            category: 'Author',
            filter_name: 'fq_author',
            filter_query: "((author_facet_hier:\"0/Wang, J\") NOT author_facet_hier:\"0/Chen, H\" NOT author_facet_hier:\"0/Zhang, Z\")",
            filter_key: '__author_facet_hier_fq_author',
            filter_value: ["NOT", "(author_facet_hier:\"0/Wang, J\")", "author_facet_hier:\"0/Chen, H\"", "author_facet_hier:\"0/Zhang, Z\""]
          },
          {
            category: 'Collection',
            filter_name: 'fq_database',
            filter_query: "(database:physics AND database:astronomy)",
            filter_key: '__database_fq_database',
            filter_value: [
              "AND",
              "database:physics",
              "database:astronomy"
            ]
          }
        ]);

        expect(gui_data).to.eql([
          {
            "elements": [
              {
                "type": "category",
                "display": "Author",
                "value": "fq_author|category|Author"
              },
              {
                "type": "operand",
                "display": "Wang, J",
                "value": "fq_author|operand|(author_facet_hier:\"0/Wang, J\")"
              }
            ]
          },
          {
            "elements": [
              {
                "type": "category",
                "display": "Author",
                "value": "fq_author|category|Author"
              },
              {
                "type": "operand",
                "display": "-Chen, H",
                "value": "fq_author|operand|author_facet_hier:\"0/Chen, H\""
              }
            ]
          },
          {
            "elements": [
              {
                "type": "category",
                "display": "Author",
                "value": "fq_author|category|Author"
              },
              {
                "type": "operand",
                "display": "-Zhang, Z",
                "value": "fq_author|operand|author_facet_hier:\"0/Zhang, Z\""
              }
            ]
          },
          {
            "elements": [
              {
                "type": "category",
                "display": "Collection",
                "value": "fq_database|category|Collection"
              },
              {
                "type": "operand",
                "display": "+physics",
                "value": "fq_database|operand|database:physics"
              }
            ]
          },
          {
            "elements": [
              {
                "type": "category",
                "display": "Collection",
                "value": "fq_database|category|Collection"
              },
              {
                "type": "operand",
                "display": "+astronomy",
                "value": "fq_database|operand|database:astronomy"
              }
            ]
          }
        ]);
      });

      it("can beautify displayed query", function() {
        var widget = new FilterVisualizerWidget({withoutOperators: true});
        expect(widget.beautifyOperand('fq_facets180', 'property:foo')).to.eql('foo');
        expect(widget.beautifyOperand('fq_keyword_facet', 'keyword_facet:foo\\ bar')).to.eql('foo bar');
        expect(widget.beautifyOperand('fq_bibstem_facet', 'bibstem_facet:foo')).to.eql('foo');
        expect(widget.beautifyOperand('fq_bibgroup_facet', 'bibgroup_facet:foo')).to.eql('foo');
        expect(widget.beautifyOperand('fq_data_facet', 'data_facet:foo')).to.eql('foo');
        expect(widget.beautifyOperand('fq_vizier_facet', 'vizier_facet:foo')).to.eql('foo');
        expect(widget.beautifyOperand('fq_grant', 'grant:foo')).to.eql('foo');
      });

      it("handles bigquery as a filter special case", function(){

        var widget = new FilterVisualizerWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render());

        $('#test').append($w);

          widget.processFeedback(minsub.createFeedback({
                  code: minsub.T.FEEDBACK.CODES.SEARCH_CYCLE_STARTED,
                  numFound: 100,
                  query: minsub.createQuery({
                    "q": [
                      "*:*"
                    ],
                    "__qid": [
                      "bd07de928f0066253af7528336acaf4a"
                    ],
                    "__bigquerySource": [
                      "Library: Papers by Alberto Accomazzi"
                    ]
                  })
                })
              );

        expect($("span.filter-topic-group").length).to.eql(1);
        expect($("span.filter-topic-group h5").text()).to.eql('Library: Papers by Alberto Accomazzi');

        var publishSpy = sinon.spy();

        widget.getBeeHive = function(){
          return {
            getService : function(){
              return {
                publish : publishSpy
              }
            }
          }
        }

        $("button.filter-operand-remove").click();

        expect(publishSpy.args[0][1].toJSON()).to.eql({
          "q": [
            "*"
          ],
          "__clearBiqQuery": [
            "true"
          ]
        });

      });

    });
  });
