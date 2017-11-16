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
    'js/widgets/list_of_things/item_view',
    'js/widgets/list_of_things/details_widget'
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
            ItemView,
            DetailsWidget
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

        view.destroy();
        view = new PaginatedView({collection: coll, model: view.model});

        $w = $(view.render().el);
        $('#test').append($w);

        expect($w.find("label").length).to.equal(10);

        expect($w.find(".abstract-row:first").text().trim()).to.eql("");

        view.model.set("showAbstract", "closed");

        //should set to "open"
        view.toggleAbstract();

        view.toggleChildrenAbstracts();

        expect(normalizeSpace($w.find(".abstract-row:first").text().trim())).to.eql('Abstract In this paper we give a bijective proof for a relation between uni- bi- and tricellular maps of certain topological genus. While this relation can formally be obtained using Matrix-theory as a result of the Schwinger-Dyson equation, we here present a bijection for the corresponding coefficient equation. Our construction is facilitated by repeated application of a certain cutting, the contraction of edges, incident to two vertices and the deletion of certain edges.');

        expect(coll.pluck("showAbstract")[0]).to.eql(true);

        expect(coll.pluck("showHighlights")[0]).to.eql(undefined);

        coll.models[0].set("highlights", ["testhighlight"]);

        view.model.set("showHighlights", "closed");

        //should set to "open"
        view.toggleHighlights();

        expect($w.find(".highlight-row:first li").text()).to.eql("testhighlight")

        view.destroy();

        done();
      });


      it("the controller reacts to user actions", function(done) {

        var counter = 0;
        var minsub = new (MinPubSub.extend({
          request: function(apiRequest) {
            counter++;
            var q = apiRequest.get('query');
            var ret = test1();
            _.each(q.keys(), function(k) {
              ret.responseHeader.params[k] = q.get(k)[0];
            });
            //but widget is currently checking in the response.start not the responseheader
            ret.response.start = q.get("start")[0];
            return ret;
          }
        }))({verbose: false});

        var fakeUserObject = {getHardenedInstance : function(){return this},
          isOrcidModeOn : function(){return false},
          getUserData : function(){ return {link_server :  "foo"}},
          getLocalStorage : function(){return { perPage : 50 }}

        };
        minsub.beehive.addObject("User", fakeUserObject);

        var widget = new DetailsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());


        var $w = widget.render().$el;
        $('#test').append($w);

        // give command to display first 20 docs; since responses are coming in
        // batches of 10; the collection will automatically ask twice
        minsub.publish(minsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'bibcode:bar'}));
        expect($w.find("label").length).to.equal(50);
        expect($(".s-checkbox-col").text().replace(/\s+/g, " ")).to.eql(" 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 ")

        // click on next page // this should trigger new request
        $w.find('.page-control.next-page').click();
        expect($(".s-checkbox-col").text().replace(/\s+/g, " ")).to.eql(" 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80 81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99 100 ");

        done();
      });

      it("has a pagination view and model that handle displaying and transmitting pagination state and changes to localStorage", function(done){

        var minsub = new (MinPubSub.extend({
          request: function(apiRequest) {
            var q = apiRequest.get('query');
            var ret = test1();
            _.each(q.keys(), function(k) {
              ret.responseHeader.params[k] = q.get(k)[0];
            });
            //but widget is currently checking in the response.start not the responseheader
            ret.response.start = q.get("start")[0];
            return ret;
          }
        }))({verbose: false});

        var setStorageSpy = sinon.spy();

        var fakeUserObject = {getHardenedInstance : function(){return this},
          isOrcidModeOn : function(){return false},
          getUserData : function(){ return {link_server :  "foo"}},
          getLocalStorage : function(){return { perPage : 25}},
          setLocalStorage : setStorageSpy
        };
        minsub.beehive.addObject("User", fakeUserObject);

        var widget = new DetailsWidget();
        //to test to make sure getLocalStorage was called
        widget.model.set("perPage", 0);
        widget.activate(minsub.beehive.getHardenedInstance());


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

        //should initialize with the values from local storage
        expect(widget.pagination.perPage).to.eql(25);

        //first, set the page but don't have local storage send an event --> page won't change
        widget.trigger("pagination:changePerPage", 50);
        //an update in perPage should trigger a call to localstorage
        expect(setStorageSpy.args[0][0]).to.eql({perPage: 50});

        //but model shouldn't change, unless there's a user event, which didn't happen here
        expect(widget.model.get("perPage")).to.eql(25);
        expect($(".per-page--active").text().trim()).to.eql("25");
        expect($("input.page-control").val()).to.eql("1");


        //now, user object will publish the change
        fakeUserObject.setLocalStorage = function(arg) {
          minsub.publish(minsub.USER_ANNOUNCEMENT, "user_info_change", arg)
        }

        widget.trigger("pagination:changePerPage", 50);
        expect(widget.model.get("perPage")).to.eql(50);
        
        expect(JSON.stringify(widget.model.toJSON())).to.eql('{"mainResults":false,"showAbstract":"closed","showHighlights":false,"pagination":true,"start":0,"perPage":50,"numFound":100,"currentQuery":{"q":["foo:bar"]},"pageData":{"perPage":50,"totalPages":2,"currentPage":1,"previousPossible":false,"nextPossible":true},"page":0,"showRange":[0,49],"query":false,"loading":false}');

        expect($(".per-page--active").text().trim()).to.eql("50");
        expect($("input.page-control").val()).to.eql("1");

        expect($(".page-control.previous-page").parent().hasClass("disabled")).to.be.true;
        expect($(".page-control.next-page").parent().hasClass("disabled")).to.be.false;

        expect(widget.collection.models[0].get('resultsIndex')).to.eql(0);
        expect(widget.collection.models[4].get('resultsIndex')).to.eql(4);


        done();
      });


      it("correctly displays pagination options depending on the model's settings", function(done){

        var minsub = new (MinPubSub.extend({
          request: function(apiRequest) {
            var q = apiRequest.get('query');
            var ret = test1();
            _.each(q.keys(), function(k) {
              ret.responseHeader.params[k] = q.get(k)[0];
            });
            //but widget is currently checking in the response.start not the responseheader
            ret.response.start = q.get("start")[0];
            return ret;
          }
        }))({verbose: false});


        var fakeUserObject = {
          getHardenedInstance : function(){return this},
          isOrcidModeOn : function(){return false},
          getLocalStorage : function(){return { perPage : 25 }},
        };
        minsub.beehive.addObject("User", fakeUserObject);

        var widget = new DetailsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());

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


        //just calling the update function directly

        widget.updatePagination({ page : 2 , perPage : 25});

        expect($(".per-page--active").text().trim()).to.eql("25");
        expect($("input.page-control").val()).to.eql("3");

        expect($(".page-control.previous-page").parent().hasClass("disabled")).to.be.false;
        expect($(".page-control.next-page").parent().hasClass("disabled")).to.be.false;

        expect(widget.collection.models[0].get('resultsIndex')).to.eql(50);
        expect(widget.collection.models[4].get('resultsIndex')).to.eql(54);

        /*
         * last page
         * */

        widget.updatePagination(({ page : 3, perPage : 25}));

        expect($(".page-control.previous-page").parent().hasClass("disabled")).to.be.false;
        expect($(".page-control.next-page").parent().hasClass("disabled")).to.be.true;

        expect($(".per-page--active").text().trim()).to.eql("25");
        expect($("input.page-control").val()).to.eql("4");


        expect(widget.collection.models[0].get('resultsIndex')).to.eql(75);
        expect(widget.collection.models[4].get('resultsIndex')).to.eql(79);

        done()

      });

      it("displays a loading view on pages that have not finished loading all the papers", function(){

        var requests;

        var minsub = new (MinPubSub.extend({
          request: function(apiRequest) {
            if (requests === 0) return;
            requests--;
            var ret = test1();
            var q = apiRequest.get('query');
            _.each(q.keys(), function(k) {
              ret.responseHeader.params[k] = q.get(k)[0];
            });
            //but widget is currently checking in the response.start not the responseheader
            ret.response.start = q.get("start")[0];
            return ret;
          }
        }))({verbose: false});


        var fakeUserObject = {
          getHardenedInstance : function(){return this},
          isOrcidModeOn : function(){return false},
          getLocalStorage : function(){return { perPage : 25 }},
        };
        minsub.beehive.addObject("User", fakeUserObject);

        var widget = new DetailsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());

        var data = test1();
        data.response.numFound = 32;

        var $w = widget.render().$el;
        $('#test').append($w);

        data.response.start = 0;
        var res = new ApiResponse(data);
        res.setApiQuery(new ApiQuery({q: 'foo:bar'}));

        requests = 1;

        widget.processResponse(res);

        expect($(".page-loading").text().trim()).to.eql("Loading more papers...");

        widget.reset();

        requests = 3;

        _.each(_.range(3), function(n) {
          data.response.start = n*10;
          var res = new ApiResponse(data);
          res.setApiQuery(new ApiQuery({q: 'foo:bar'}));
          widget.processResponse(res);
        });

        expect($(".page-loading").text().trim()).to.eql("");

        requests = 0;
        widget.updatePagination({ page : 2 , perPage : 25});

        //add penultimate record

        data.response.start = 30;
        data.response.docs = test1().response.docs.slice(0,1);
        var res = new ApiResponse(data);
        res.setApiQuery(new ApiQuery({q: 'foo:bar'}));

        widget.processResponse(res);

        expect($(".page-loading").text().trim()).to.eql("Loading more papers...");

        //add final record, remove loading view

        data.response.start = 31;
        data.response.docs = test1().response.docs.slice(1,2);
        var res = new ApiResponse(data);
        res.setApiQuery(new ApiQuery({q: 'foo:bar'}));

        widget.processResponse(res);

        expect($(".page-loading").text().trim()).to.eql("");

      });


      it("the item view allows the user to view the lsit in a search results page if 'operator' option is true and 'queryOperator' option is set", function() {

        var coll = new PaginatedCollection();
        var view = new PaginatedView({collection: coll});
        var docs = test1().response.docs;

        _.each(docs, function (d) {
          view.collection.add(_.clone(d));
        });

        view.model.set({"bibcode" :  "foo", queryOperator: "citations"})

        var $w = $(view.render().el);
        $('#test').append($w);

        expect($("#test .s-operator").html().trim()).to.eql('<a href="#search/q=citations(bibcode%3Afoo)" class="btn btn-sm btn-inverse btn-primary-faded"><i class="fa fa-search"></i> view this list in a search results page</a>');

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
        sinon.spy(M.prototype, 'showLinks');
        sinon.spy(M.prototype, 'hideLinks');
        var triggerSpy = sinon.spy();


        var view = new M({model: model});
        view.on('all', triggerSpy);

        var $w = view.render().$el;
        $('#test').append($w);

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
