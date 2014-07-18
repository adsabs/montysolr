define(['underscore',
    'js/components/query_builder/plugin',
    'js/bugutils/minimal_pubsub'],
  function (_,
            QueryBuilderPlugin,
            MinimalPubsub) {


    describe("UI QueryBuilder - using live server", function () {

      var liveServerReady;
      var isApiAvailable = function () {
        liveServerReady = false;
        $.ajax({
          type: "GET",
          url: "/api/1/qtree",
          data: "q=*:*",
          dataType: "json",
          async: false,
          success: function (data) {
            if (data.responseHeader)
              liveServerReady = true;
          }
        });
        return liveServerReady;
      };

      this.pending = !isApiAvailable();

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
        minsub.close();
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

      it("all supported indexes", function (done) {
        testQ('title:foo', 'title:foo');
        testQ('author:foo', 'author:foo');
        testQ('keyword:foo', 'keyword:foo');
        testQ('full:foo', 'full:foo');
        // TBD: add more

        done();
      });


    });
  });