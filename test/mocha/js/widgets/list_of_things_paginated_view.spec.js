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
    'js/bugutils/minimal_pubsub'
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
            MinPubSub
    ) {

    describe("ListOfThings", function () {

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
            var q = apiRequest.get('query');
            var ret = test1;
            if (counter % 2 == 0)
              ret = test2;

            ret = _.clone(ret);
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

        var data = JSON.parse(JSON.stringify(test1));
        data.response.numFound = 100;

        _.each(_.range(5), function(n) {
          data.response.start = n*10;
          var res = new ApiResponse(data);
          res.setApiQuery(new ApiQuery({q: 'foo:bar'}));
          widget.processResponse(res);
        });

        var $w = widget.render().$el;
        //$('#test').append($w);

        widget.updatePagination({numFound: 100, page : 1, perPage : 5});
        console.log(widget.model.attributes);
        expect($w.find(".pagination li").length).to.eql(5);
        expect($w.find(".pagination li").filter(function(n){return $(n).text().trim() === "«"}).length).to.eql(0);
        expect($w.find(".pagination li:first").text().trim()).to.eql("1");
        expect($w.find(".pagination li:last").text().trim()).to.eql("5");

        widget.updatePagination({numFound: 100, page : 4, perPage : 5});
        expect($w.find(".pagination li:first").text().trim()).to.eql("«");
        expect($w.find(".pagination li:last").text().trim()).to.eql("7");
        expect($w.find(".pagination li").length).to.eql(6);

        widget.updatePagination(({numFound: 15, page : 1, perPage : 5}));
        expect($w.find(".pagination li").length).to.eql(3);
        expect($w.find(".pagination li:first").text().trim()).to.eql("1");
        expect($w.find(".pagination li:last").text().trim()).to.eql("3");
      });

      it("has a mechanism to prevent infinite requests", function(done){

        var minsub = new (MinPubSub.extend({
          request: function(apiRequest) {
            var t = JSON.parse(JSON.stringify(test1));
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
    })

  });