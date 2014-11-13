define(['marionette',
    'backbone',
    'underscore',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/list_of_things/paginated_view',
    'js/widgets/list_of_things/model'
  ],
  function (Marionette,
            Backbone,
            _,
            ApiQuery,
            test1,
            test2,
            PaginatedView,
            PaginatedCollection
    ) {

    describe("ListOfThings (PaginatedView)", function () {

      it("returns PaginatedView object", function(done) {
        //expect(new PaginatedView()).to.be.instanceof(Marionette.CompositeView);
        var m = new PaginatedCollection();
        expect(m).to.be.instanceof(Backbone.Collection);
        done();
      });


      /*
      it("shows controls when model is updated", function(done) {
        var view = new PaginatedView();
        var $w = $(view.render().el);
        expect($w.find('.results-controls').hasClass('hide')).to.be.false;
        view.model.set("showControls", true);
        expect($w.find('.results-controls').hasClass('hide')).to.be.true;
        done();
      });
      */

      it.skip("the model supports the pagination operations", function (done) {

        var view = new PaginatedView();
        var $w = $(view.render().el);

        //find bibcode rendered
        expect($w.find(".identifier").eq(0).text().trim()).to.equal("2013arXiv1305.3460H");


        //find new first bib to confirm re-render
        expect($w.find(".identifier").eq(0).text()).to.equal("2006IEDL...27..896K");
        done();
      });


      it("the model supports paginated operations'", function (done) {
        var coll = new PaginatedCollection();
        var docs = test1.response.docs;

        _.each(docs, function(d) {
          coll.add(_.clone(d));
        });

        var ix = _.map(coll.models, function(m) {return m.attributes.resultsIndex});
        expect(ix).to.be.eql(_.range(0,10));

        expect(coll.getNumVisible()).to.be.equal(0);
        expect(coll.showMore(5)).to.be.equal(5);
        expect(coll.getNumVisible()).to.be.equal(5);

        expect(coll.showMore(10)).to.be.equal(5);
        expect(coll.getNumVisible()).to.be.equal(10);

        expect(coll.showRange(3,6)).to.be.equal(4);
        expect(coll.getNumVisible()).to.be.equal(4);

        expect(coll.showRange(30,40)).to.be.equal(0);
        expect(coll.getNumVisible()).to.be.equal(0);

        // jump to the page 20-30
        var ri = 20;
        _.each(docs, function(d) {
          coll.add(_.extend({resultsIndex: ri++}, d));
        });

        var spy = sinon.spy();
        coll.on('show:missing', spy);

        expect(coll.models.length).to.be.eql(20);
        expect(coll.showRange(0,4)).to.be.equal(5);
        expect(coll.showMore(10)).to.be.equal(5); // only 5 are available, then a gap
        expect(spy.lastCall.args[0]).to.be.eql([{start: 10, end: 14}]);

        coll.add({resultsIndex: 50, title: 'foo'});

        expect(coll.showRange(0,4)).to.be.equal(5);
        expect(coll.showMore(10)).to.be.equal(5); // only 5 are available, then a gap
        expect(spy.lastCall.args[0]).to.be.eql([{start: 10, end: 14}]);

        // however when we ask to display more, the system reports more gaps
        coll.showMore(100);
        expect(spy.lastCall.args[0]).to.be.eql([{"start":10,"end":19},{"start":30,"end":49}]);

        done();
      })

    })

  });