define(['underscore',
    'js/components/query_builder/plugin',
    'js/bugutils/minimal_pubsub'],
  function (_,
            QueryBuilderPlugin,
            MinimalPubsub) {


    describe("UI QueryBuilder - using live server (query_builder/plugin.testcases.spec.js)", function () {

      // this.pending = !window.bbbTest.serverReady;

      var minsub, queryBuilder;
      beforeEach(function(done) {
        minsub = new (MinimalPubsub.extend({
          request: function(apiRequest) {
            var query = apiRequest.get('query');
            var response = {};
            $.ajax({
              type: "GET",
              url: "/api/1/qtree",
              data: query.url(),
              dataType: "json",
              async: false,
              success: function (data) {
                if (data.responseHeader)
                  response = data;
              }
            });
            return response;
          }
        }))({verbose: false});

        queryBuilder = new QueryBuilderPlugin({el: '#test',
          qtreeGetter: QueryBuilderPlugin.buildQTreeGetter(minsub.beehive.getHardenedInstance())});

        done();
      });

      afterEach(function(done) {
        minsub.destroy();
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });

      var testQ = function(inputString, expectedString, expectedUIRules) {
        var promise = queryBuilder.updateQueryBuilder(inputString);
        promise.done(function(apiResponse) {
          console.log(inputString, "\n   ", JSON.stringify(queryBuilder.getRules()));
          if (expectedString)
            expect(queryBuilder.getQuery()).to.be.equal(expectedString);
          if (expectedUIRules)
            expect(queryBuilder.getRules()).to.be.eql(expectedUIRules);
        });

      };

      it("foo", function (done) {
        testQ('foo', 'foo');
        done();
      });

      it("\"phrase foo\"", function (done) {
        testQ('"phrase foo"', '"phrase foo"');
        done();
      });

      it("foo*", function (done) {
        testQ('foo*', 'foo*');
        done();
      });

      it("f?oo", function (done) {
        testQ('f?oo', 'f?oo');
        done();
      });

      it("foo*bar", function (done) {
        testQ('foo*bar', 'foo*bar');
        done();
      });

      it("author:foo", function (done) {
        testQ('author:foo', 'author:foo');
        done();
      });

      it("author:foo*", function (done) {
        testQ('author:foo*', 'author:foo*');
        done();
      });

      it("title:foo", function (done) {
        testQ('title:foo', 'title:foo');
        done();
      });

      it("title:(foo bar)", function (done) {
        testQ('title:(foo bar)', 'title:(foo bar)');
        done();
      });

      it("foo AND bar", function (done) {
        testQ('foo AND bar', 'foo AND bar');
        done();
      });

      it("(x AND y)", function (done) {
        testQ('(x AND y)', 'x AND y');
        testQ('(x and y)', 'x AND y');
        testQ('((x AND y))', '(x AND y)');
        done();
      });

      it("(x OR y)", function (done) {
        testQ('(x OR y)', 'x OR y');
        testQ('(x or y)', 'x OR y');
        testQ('((x OR y))', '(x OR y)');
        done();
      });

      it("(x AND (y AND z)", function() {
        testQ('(x AND (y AND z)', '(x AND (y AND z)');
      });

      it.skip("supports fields that we have", function (done) {
        this.timeout(5000);
        testQ('_version_:1', '_version_:1');
        testQ('abstract:foo', 'abstract:foo');
        testQ('ack:foo', 'ack:foo');
        testQ('aff:foo', 'aff:foo');
        testQ('alternate_bibcode:foo', 'alternate_bibcode:foo');
        testQ('alternate_title:foo', 'alternate_title:foo');
        testQ('arxiv_class:foo', 'arxiv_class:foo');
        testQ('author:foo', 'author:foo');
        testQ('author_facet:foo', 'author_facet:foo');
        testQ('bibgroup:foo', 'bibgroup:foo');
        testQ('body:foo', 'body:foo');
        testQ('citation:foo', 'citation:foo');
        testQ('citation_count:0', 'citation_count:0');
        testQ('comment:foo', 'comment:foo');
        testQ('copyright:foo', 'copyright:foo');
        testQ('data:foo', 'data:foo');
        testQ('database:foo', 'database:foo');
        testQ('date:2012', 'date:2012');
        testQ('doctype:foo', 'doctype:foo');
        testQ('doi:foo', 'doi:foo');
        testQ('eid:foo', 'eid:foo');
        testQ('email:foo', 'email:foo');
        testQ('facility:foo', 'facility:foo');
        testQ('full:foo', 'full:foo');
        testQ('grant:foo', 'grant:foo');
        testQ('id:0', 'id:0');
        testQ('identifier:foo', 'identifier:foo');
        testQ('indexstamp:0001', 'indexstamp:0001');
        testQ('issn:foo', 'issn:foo');
        testQ('isbn:foo', 'isbn:foo');
        testQ('issue:foo', 'issue:foo');
        testQ('keyword:foo', 'keyword:foo');
        testQ('lang:foo', 'lang:foo');
        testQ('orcid:foo', 'orcid:foo');
        testQ('page:foo', 'page:foo');
        testQ('property:foo', 'property:foo');
        testQ('pub:foo', 'pub:foo');
        testQ('pubdate:2012', 'pubdate:2012');
        testQ('read_count:0', 'read_count:0');
        //testQ('reader:foo', 'reader:foo');
        //testQ('recid:foo', 'recid:foo');
        testQ('reference:foo', 'reference:foo');
        testQ('simbid:0', 'simbid:0');
        testQ('thesis:foo', 'thesis:foo');
        testQ('title:foo', 'title:foo');
        testQ('vizier:foo', 'vizier:foo');
        testQ('volume:foo', 'volume:foo');
        testQ('year:2012', 'year:2012');
        done();
      });

      it("understands funcitons", function(done) {
        testQ('pos(james AND jim, 1,    2)', 'pos(james AND jim, 1, 2)');
        testQ('pos(james AND jim, 1)', 'pos(james AND jim, 1)');
        done();
      });

    });
  });