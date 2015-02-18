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
        expect(coll.models.length).to.be.eql(24); // the gap 29-39 was auto-filled

        // jump to the page 20-30
        var ri = 20;
        _.each(docs, function(d) {
          //console.log('adding' + (ri), d);
          coll.add(_.extend({resultsIndex: ri}, d), {merge: true});
          ri += 1;
        });
        expect(_.map(coll.models, function(x) {return x.attributes.resultsIndex + ':' + (x.attributes.emptyPlaceholder ? 0 : 1)})).to.eql(
          [ "0:1", "1:1", "2:1", "3:1", "4:1", "5:1", "6:1", "7:1", "8:1", "9:1", "10:0", "11:0", "12:0",
            "20:1", "21:1", "22:1", "23:1", "24:1", "25:1", "26:1", "27:1", "28:1", "29:1",
            "30:0", "31:0", "32:0", "33:0", "34:0", "35:0", "36:0", "37:0", "38:0", "39:0"]
        );
        expect(coll.models.length).to.be.eql(33);

        var spy = sinon.spy();
        coll.on('show:missing', spy);

        expect(coll.showRange(0,4)).to.be.equal(5);
        expect(coll.showMore(10)).to.be.equal(5); // only 5 are available, then a gap
        expect(spy.lastCall.args[0]).to.be.eql([{start: 10, end: 14}]);

        coll.add({resultsIndex: 50, title: 'foo'});

        expect(coll.showRange(0,4)).to.be.equal(5);
        expect(coll.showMore(10)).to.be.equal(5); // only 5 are available, then a gap
        expect(spy.lastCall.args[0]).to.be.eql([{start: 10, end: 14}]);

        // however when we ask to display more, the system reports more gaps
        coll.showRange(0, 100);
        expect(spy.lastCall.args[0]).to.be.eql([{"start":10,"end":19},{"start":30,"end":49},
          {"start":51,"end":99}]);

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

        expect($w.find('.details:first').hasClass("hide")).to.be.true;
        expect($w.find('.details:last').hasClass("hide")).to.be.true;


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
        //$('#test').append($w);

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

      it("has a mechanism to prevent infinite requests", function(done){

        var minsub = new (MinPubSub.extend({
          request: function(apiRequest) {
            var t = test1();
            t.response.start  = 0;
            return t;
          }
        }))({verbose: false});
        var widget = new ListOfThings();

        widget.activate(minsub.beehive.getHardenedInstance());
        var publishStub = sinon.stub(widget.pubsub, "publish");

        /* now I will set pagination, return the wrong records, and check to make
         sure data was only requested once despite the fact that the request wasn't
         properly fulfilled
         */


        minsub.publish(minsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'foo:bar'}));
        expect(publishStub.callCount).to.eql(1);

        //XXX:TODO finish

        done();
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
        expect($w.find('.details').hasClass('hide')).to.be.false;
        $w.find('.details-control').click();
        expect(view.toggleDetails.callCount).to.be.eql(2);
        expect($w.find('.details').hasClass('hide')).to.be.true;

        view.showLinks.reset();
        view.hideLinks.reset();

        $w.find('.letter-icon').trigger('mouseenter');
        expect(view.showLinks.called).to.be.true;
        $w.find('.letter-icon').trigger('mouseleave');
        expect(view.hideLinks.called).to.be.true;

        triggerSpy.reset();
        $w.find('.letter-icon:last').trigger('mouseenter');
        $w.find('.orcid-action.orcid-update').click();
        expect(triggerSpy.called).to.eql(true);
        expect(triggerSpy.lastCall.args[0]).to.eql('OrcidAction');
        expect(triggerSpy.lastCall.args[1].model.attributes.identifier).to.eql('foo');
        expect(triggerSpy.lastCall.args[1].target).to.be.defined;
        expect(triggerSpy.lastCall.args[1].view).to.be.defined;
        expect(triggerSpy.lastCall.args[1].action).to.eql('orcid-update');
      });
    })

  });