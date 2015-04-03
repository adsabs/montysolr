define(['marionette',
    'backbone',
    'underscore',
    'js/components/api_query',
    'js/components/api_response',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/list_of_things/paginated_view',
    'js/widgets/list_of_things/model',
    'js/widgets/list_of_things/widget',
    'js/widgets/base/base_widget',
    'js/bugutils/minimal_pubsub',
    'js/widgets/list_of_things/item_view'
  ],
  function (Marionette,
            Backbone,
            _,
            ApiQuery,
            ApiResponse,
            test1,
            test2,
            PaginatedView,
            PaginatedCollection,
            ListOfThings,
            BaseWidget,
            MinPubSub,
            ItemView
    ) {

    describe("ListOfThings (list_of_things_widget.spec.js)", function () {

      afterEach(function (done) {
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });

      it("returns PaginatedView object", function(done) {
        expect(new PaginatedView()).to.be.instanceof(Marionette.CompositeView);
        var m = new PaginatedCollection();
        expect(m).to.be.instanceof(Backbone.Collection);
        expect(new ListOfThings()).to.be.instanceof(BaseWidget);
        done();
      });


      it("the collection supports pagination operations'", function (done) {
        var coll = new PaginatedCollection();
        var docs = test1().response.docs;
        var spy = sinon.spy();
        coll.on('show:missing', spy);

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
        expect(coll.models.length).to.be.eql(10);
        expect(spy.lastCall.args[0]).to.be.eql([{start: 10, end: 14}]);

        spy.reset();
        expect(coll.showRange(3,6)).to.be.equal(4);
        expect(coll.getNumVisible()).to.be.equal(4);
        expect(coll.models.length).to.be.eql(10); // no change there
        expect(spy.called).to.eql(false);

        expect(coll.showRange(30,39)).to.be.equal(0);
        expect(coll.getNumVisible()).to.be.equal(0);
        expect(coll.models.length).to.be.eql(10); // the gap 30-39 was not auto-filled
        expect(spy.lastCall.args[0]).to.be.eql([{start: 30, end: 39}]);

        coll.add({resultsIndex: 20, title: 'foo'});
        coll.add({resultsIndex: 50, title: 'foo'});

        // however when we ask to display more, the system reports more gaps
        coll.showRange(0, 99);
        expect(spy.lastCall.args[0]).to.be.eql([
          {"start":10,"end":19},
          {"start":21,"end":49},
          {"start":51,"end":99}]);

        // has a mechanism to prevent recursive requests
        spy.reset();
        coll.showRange(90, 99);
        expect(spy.lastCall.args[0]).to.be.eql([{start: 90, end: 99}]);
        spy.reset();
        coll.showRange(90, 99);
        expect(spy.called).to.eql(false);
        coll.showRange(91, 99);
        expect(spy.lastCall.args[0]).to.be.eql([{start: 91, end: 99}]);

        done();
      });

      it("the item view reacts to actions inside the model", function (done) {

        var coll = new PaginatedCollection();
        var view = new PaginatedView({collection: coll});
        var docs = test1().response.docs;

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

        view.toggleDetails();

        expect($w.find('.details:first').hasClass("hide")).to.be.false;
        expect($w.find('.details:last').hasClass("hide")).to.be.false;


        view.toggleDetails();

        expect($w.find('.details:first').hasClass("sr-only")).to.be.true;
        expect($w.find('.details:last').hasClass("sr-only")).to.be.true;


        view.close();

        done();
      });

      it("the controller reacts to user actions", function(done) {

        var counter = 0;
        var minsub = new (MinPubSub.extend({
          request: function(apiRequest) {
            counter++;
            var q = apiRequest.get('query');
            var ret = test1();
            if (counter % 2 == 0)
              ret = test2();

            _.each(q.keys(), function(k) {
              ret.responseHeader.params[k] = q.get(k)[0];
            });
            //_.extend(ret.responseHeader.params, q.toJSON());
            return ret;
          }
        }))({verbose: false});


        var widget = new ListOfThings();
        widget.activate(minsub.beehive.getHardenedInstance());

        var $w = widget.render().$el;
        $('#test').append($w);

        // give command to display first 20 docs; since responses are coming in
        // batches of 10; the collection will automatically ask twice
        minsub.publish(minsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'foo:bar'}));

        expect($w.find("label").length).to.equal(20);

        // click on next page // this should trigger new request
        $w.find('a[data-paginate=2]').click();
        expect($w.find("label").length).to.equal(20);

        done();
      });

      it("has a pagination view and model that handle displaying and transmitting pagination state and changes", function(){

        var widget = new ListOfThings({pagination: {perPage: 5}});
        //widget.activate(minsub.beehive.getHardenedInstance());

        var data = test1();
        data.response.numFound = 100;

        _.each(_.range(5), function(n) {
          data.response.start = n*10;
          var res = new ApiResponse(data);
          res.setApiQuery(new ApiQuery({q: 'foo:bar'}));
          widget.processResponse(res);
        });

        var $w = widget.render().$el;
        $('#test').append($w);

        widget.updatePagination({numFound: 100, page : 1, perPage : 5});
        //console.log(widget.model.attributes);
        expect($w.find(".pagination li").length).to.eql(5);
        expect($w.find(".pagination li").filter(function(n){return $(n).text().trim() === "«"}).length).to.eql(0);
        expect($w.find(".pagination li:first").text().trim()).to.eql("1");
        expect($w.find(".pagination li:last").text().trim()).to.eql("5");
        expect(widget.collection.models[0].get('resultsIndex')).to.eql(5);
        expect(widget.collection.models[4].get('resultsIndex')).to.eql(9);

        widget.updatePagination({numFound: 100, page : 4, perPage : 5});
        expect($w.find(".pagination li:first").text().trim()).to.eql("«");
        expect($w.find(".pagination li:last").text().trim()).to.eql("7");
        expect($w.find(".pagination li").length).to.eql(6);
        expect(widget.collection.models[0].get('resultsIndex')).to.eql(20);
        expect(widget.collection.models[4].get('resultsIndex')).to.eql(24);

        widget.updatePagination(({numFound: 15, page : 0, perPage : 5}));
        expect($w.find(".pagination li").length).to.eql(3);
        expect($w.find(".pagination li:first").text().trim()).to.eql("1");
        expect($w.find(".pagination li:last").text().trim()).to.eql("3");
        expect(widget.collection.models[0].get('resultsIndex')).to.eql(0);
        expect(widget.collection.models[4].get('resultsIndex')).to.eql(4);
      });


      it("the ItemView has user interacting parts", function() {
        var model = new Backbone.Model({visible: true, identifier: 'foo',
          orderNum: 1, title: 'test',
          details: {
            highlights: ['one high', 'two high'],
            shortAbstract: 'silly short'
          },
          links: {
            text: {title: 'foo', link: 'link'},
            list: [
              {link: 'link1', title: 'title1'},
              {link: 'link2', title: 'title2'}
            ]
          },
          orcid: {
            actions: [
              {action: 'orcid-update', title: 'update'},
              {action: 'orcid-delete', title: 'delete'}
            ]
          }
        });
        var M = ItemView.extend({});
        sinon.spy(M.prototype, 'toggleSelect');
        sinon.spy(M.prototype, 'toggleDetails');
        sinon.spy(M.prototype, 'showLinks');
        sinon.spy(M.prototype, 'hideLinks');
        var triggerSpy = sinon.spy();


        var view = new M({model: model});
        view.on('all', triggerSpy);

        var $w = view.render().$el;
        $('#test').append($w);


        $w.find('input[name=identifier]').trigger('change');
        expect(view.toggleSelect.callCount).to.be.eql(1);
        $w.find('.details-control').click();
        expect(view.toggleDetails.callCount).to.be.eql(1);
        expect($w.find('.details').hasClass('sr-only')).to.be.false;
        $w.find('.details-control').click();
        expect(view.toggleDetails.callCount).to.be.eql(2);
        expect($w.find('.details').hasClass('sr-only')).to.be.true;

        view.showLinks.reset();
        view.hideLinks.reset();

        $w.find('.letter-icon').trigger('mouseenter');
        expect(view.showLinks.called).to.be.true;
        $w.find('.letter-icon').trigger('mouseleave');
        expect(view.hideLinks.called).to.be.true;

        //XXX:alex - this needs to be replaced with the latest version of orcid interaction
        /*
        triggerSpy.reset();
        $w.find('.letter-icon:last').trigger('mouseenter');
        $w.find('.orcid-action.orcid-update').click();
        expect(triggerSpy.called).to.eql(true);
        expect(triggerSpy.lastCall.args[0]).to.eql('OrcidAction');
        expect(triggerSpy.lastCall.args[1].model.attributes.identifier).to.eql('foo');
        expect(triggerSpy.lastCall.args[1].target).to.be.defined;
        expect(triggerSpy.lastCall.args[1].view).to.be.defined;
        expect(triggerSpy.lastCall.args[1].action).to.eql('orcid-update');
        */
      });
    })

  });