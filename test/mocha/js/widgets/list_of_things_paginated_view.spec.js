define(['marionette',
    'backbone',
    'underscore',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/list_of_things/paginated_view',
    'js/widgets/list_of_things/model',
    'js/widgets/list_of_things/widget',
    'js/widgets/base/base_widget',
    'js/bugutils/minimal_pubsub'
  ],
  function (Marionette,
            Backbone,
            _,
            ApiQuery,
            test1,
            test2,
            PaginatedView,
            PaginatedCollection,
            ListOfThings,
            BaseWidget,
            MinPubSub
    ) {

    describe("ListOfThings (PaginatedView)", function () {

      it("returns PaginatedView object", function(done) {
        expect(new PaginatedView()).to.be.instanceof(Marionette.CompositeView);
        var m = new PaginatedCollection();
        expect(m).to.be.instanceof(Backbone.Collection);
        expect(new ListOfThings()).to.be.instanceof(BaseWidget);
        done();
      });


      it("the collection supports pagination operations'", function (done) {
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
      });

      it("the item view reacts to actions inside the model", function (done) {

        var coll = new PaginatedCollection();
        var view = new PaginatedView({collection: coll});
        var docs = test1.response.docs;

        _.each(docs, function(d) {
          view.collection.add(_.clone(d));
        });

        var $w = $(view.render().el);
        $('#test').append($w);

        // there should be no items (yet)
        expect($w.find("label").length).to.equal(0);

        view.collection.showMore(5);
        expect($w.find("label").length).to.equal(5);

        view.collection.showRange(3,5);
        expect($w.find("label").length).to.equal(3);

        view.collection.showRange(0,20);

        // the signals should still work (even if elements were removed)
        $($w.find('label input')[0]).click();
        expect(view.children.first().model.attributes.chosen).to.be.true;


        // one problem with this mechanism is that elements remain attached
        // to the page (and so the most appropriate mechanism is to close
        // the whole widget

        view.close();
        view = new PaginatedView({collection: coll, model: view.model});

        $w = $(view.render().el);
        $('#test').append($w);
        expect($w.find("label").length).to.equal(10);

        view.model.set("showDetailsButton", true);
        expect(view.model.get('showDetailsButton')).to.be.true;
        $w.find('button.show-details').click();
        expect(view.model.get('showDetailsButton')).to.be.false;

        view.close();

        done();
      });


      it("the controller reacts to user actions", function(done) {

        var counter = 0;
        var minsub = new (MinPubSub.extend({
          request: function(apiRequest) {
            counter++;
            if (counter % 2 == 0)
              return test2;
            return test1;
          }
        }))({verbose: false});


        var widget = new ListOfThings();
        widget.activate(minsub.beehive.getHardenedInstance());

        var $w = widget.render().$el;
        $('#test').append($w);

        minsub.publish(minsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'foo:bar'}));
        expect($w.find("label").length).to.equal(10);

        // click on next page
        $w.find('button.show-details').click();

        // this should trigger new request
        expect($w.find("label").length).to.equal(10);



        done();
      });

    })

  });