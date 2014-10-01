define(['marionette',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/widgets/list_of_things/widget',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2',
    'js/components/api_response',
    'js/components/api_request',
    'js/widgets/base/base_widget'
  ],
  function (Marionette,
            Backbone,
            MinimalPubsub,
            ListOfThingsWidget,
            ApiQuery,
            Test1,
            Test2,
            ApiResponse,
            ApiRequest,
            BaseWidget) {

    describe("ListOfThings (UI Widget)", function () {

      var minsub, w;
      beforeEach(function(done) {

        minsub = new (MinimalPubsub.extend({
          request: function(apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return Test2;
            } else {

              Test1.response.start  = 10
              return Test1;
            }
          }
          }))({verbose: false});

        w = new ListOfThingsWidget();
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

      it("has a pagination view and model that handle displaying and transmitting pagination state and changes", function(){

        var v = w.paginationView;

        w.collection.stopListening();

        var m = w.paginationModel;

        m.set("currentQuery", new ApiQuery());

        m.set({numFound: 100, page : 1, perPage : 10})

        expect(v.$(".pagination li").length).to.eql(5);

        expect(v.$(".pagination li").filter(function(n){return $(n).text().trim() === "«"}).length).to.eql(0)

        expect(v.$(".pagination li:first").text().trim()).to.eql("1")

        expect(v.$(".pagination li:last").text().trim()).to.eql("5")

        m.set({numFound: 100, page : 4, perPage : 10})

        expect(v.$(".pagination li:first").text().trim()).to.eql("«");

        expect(v.$(".pagination li:last").text().trim()).to.eql("6")

        expect(v.$(".pagination li").length).to.eql(6);


        m.set({numFound: 30, page : 1, perPage : 10})

        expect(v.$(".pagination li").length).to.eql(3);

        expect(v.$(".pagination li:first").text().trim()).to.eql("1");

        expect(v.$(".pagination li:last").text().trim()).to.eql("3")


      })

      it("has a master collection that listens to the pagination model and transfers the proper models to the visible collection", function(){

        w.stopListening();

        //testing updateStartAndEndIndex method
        expect(w.collection.updateStartAndEndIndex).to.be.instanceof(Function);

        w.paginationModel.set({page: 1 , numFound: 100 , perPage:10 });

        w.collection.updateStartAndEndIndex();

        expect(w.collection.currentStartIndex).to.eql(0);

        expect(w.collection.currentEndIndex).to.eql(9);

        w.paginationModel.set({page: 1 , numFound: 100 , perPage:20 });

        w.collection.updateStartAndEndIndex();

        expect(w.collection.currentStartIndex).to.eql(0);

        expect(w.collection.currentEndIndex).to.eql(19);

        w.paginationModel.set({page: 1 , numFound: 15 , perPage:20 });

        w.collection.updateStartAndEndIndex();

        expect(w.collection.currentStartIndex).to.eql(0);

        expect(w.collection.currentEndIndex).to.eql(14);

        w.paginationModel.set({page: 2 , numFound: 50 , perPage:20 });

        expect(w.collection.currentStartIndex).to.eql(20);

        expect(w.collection.currentEndIndex).to.eql(39);

        //testing transferModels method (comes after updateStartAndEndIndex)
        expect(w.collection.transferModels).to.be.instanceof(Function);

        w.collection.off("reset");
        w.collection.off("add");
        w.collection.reset();
        w.visibleCollection.reset();

        w.collection.currentStartIndex = 2;
        w.collection.currentEndIndex = 4;

        w.collection.reset([{resultsIndex:0 },{resultsIndex:1 },{resultsIndex:2 },{resultsIndex:3 },{resultsIndex:4 },{resultsIndex:5 }]);

        var requestDataSpy = sinon.stub(w.collection, "requestData");

        w.collection.transferModels();

        expect(_.pluck(w.visibleCollection.toJSON(), "resultsIndex")).to.eql([2,3,4]);

        expect(requestDataSpy.callCount).to.eql(0);

        w.collection.reset([{resultsIndex:0 },{resultsIndex:1}]);

        w.collection.transferModels();

        //this time it had to request more data
        expect(requestDataSpy.callCount).to.eql(1);

      })

      it("has a composite view that displays records for each model in the collection")

      it("has a controller that can accept a command to load data, fetches data, and augments the collection", function(){

        //this will be overridden for the main "results" widget
        expect(w.loadBibcodeData).to.be.instanceof(Function);

        w.pubsub = {publish: function(){}}

        w.solrOperator = "foo"

        var pubSubSpy = sinon.spy(w.pubsub, "publish")

        //returns a deferred object
        expect(w.loadBibcodeData("testBibcode").promise).to.be.instanceOf(Function);

        //makes a request
        expect(pubSubSpy.callCount).to.eql(1);
        expect(pubSubSpy.firstCall.args[1]).to.be.instanceOf(ApiRequest);
        expect(pubSubSpy.firstCall.args[1].get("query").get("q")[0]).to.eql("foo(bibcode:testBibcode)")


      })



      it("returns ListOfThingsWidget object", function(done) {
        expect(new ListOfThingsWidget()).to.be.instanceof(ListOfThingsWidget);
        expect(new ListOfThingsWidget()).to.be.instanceof(BaseWidget);
        done();
      });

      it("should consist of a Marionette Controller with a Marionette Composite View as its main view", function (done) {

        expect(new ListOfThingsWidget()).to.be.instanceof(Marionette.Controller);
        expect(new ListOfThingsWidget().view).to.be.instanceof(Marionette.CompositeView);

        done();
      });


//      it("should request links_data and parse it into the model")
//      it("should render links for the relevant categories")



    })

  });
