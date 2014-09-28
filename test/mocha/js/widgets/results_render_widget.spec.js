define(['marionette',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/widgets/results/widget',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/list_of_things/widget',
    'js/components/api_response',
    'js/components/api_query'
  ],
  function (Marionette,
    Backbone,
    MinimalPubsub,
    ResultsWidget,
    ApiQuery,
    Test1,
    Test2,
    ListOfThingsWidget,
    ApiResponse,
    ApiQuery) {

    describe("Render Results (UI Widget)", function () {

      var minsub, widget, $w;
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

        widget = new ResultsWidget();

        widget.activate(minsub.beehive.getHardenedInstance());
        $w = $(widget.render().el);

        //prevent infinite requests for data
        widget.collection.requestData = function(){};

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


      it("returns ResultsWidget object", function(done) {
        expect(new ResultsWidget()).to.be.instanceof(ResultsWidget);
        expect(new ResultsWidget()).to.be.instanceof(ListOfThingsWidget);
        done();
      });

      it("should listen to INVITING_REQUEST and automatically request and render data", function(){

        //default model, not sure why its there?
        expect(widget.collection.length).to.eql(1);

        expect(widget.getCurrentQuery().toJSON()).to.eql({});

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        expect(widget.collection.length).to.eql(10);
        expect(widget.getCurrentQuery().get("q")[0]).to.eql("star");


      })


      it("should join highlights with their records on a model by model basis", function (done) {

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        expect(widget.collection.findWhere({"recid": 4189917}).get("details").highlights[0]).to.eql("External triggers of <em>star</em> formation.");
        done();
      });



      it("should render the results button only if highlights exist given the paginated docs", function(){

        var responseWithHighlights = new ApiResponse({
          "responseHeader":{
            "status":0,
            "QTime":11,
            "params":{
              "fl":"id",
              "indent":"true",
              "q":"author:accomazzi,a",
              "hl.simple.pre":"<em>",
              "hl.simple.post":"</em>",
              "wt":"json",
              "hl":"true"}},
          "response":{"numFound":175,"start":0,"docs":[
            {
              "id":"10406064"},
            {
              "id":"3513629"},
            {
              "id":"5422941"},]
          },
          "highlighting":{
            "10406064":{"title": "fooblydoo"},
            "3513629":{"abstract": ""}
          }})

        responseWithHighlights.setApiQuery(new ApiQuery())

        widget.processResponse(responseWithHighlights);


        //transfer models

        widget.paginationModel.set({page: 1, perPage : 2});

        //expect results button;

        expect(widget.view.render().$el.find(".show-details").length).to.eql(1);


        //pagination showing only 1 record without highlights should hide the button


        widget.paginationModel.set({page: 2, perPage : 1});


        expect(widget.view.render().$el.find(".show-details").length).to.eql(0);


        var responseWithoutHighlights = new ApiResponse({
          "responseHeader":{
            "status":0,
            "QTime":11,
            "params":{
              "fl":"id",
              "indent":"true",
              "q":"author:accomazzi,a",
              "hl.simple.pre":"<em>",
              "hl.simple.post":"</em>",
              "wt":"json",
              "hl":"true"}},
          "response":{"numFound":175,"start":0,"docs":[
            {
              "id":"10406064"},
            {
              "id":"3513629"},
            {
              "id":"5422941"},]
          },
          "highlighting":{
            "10406064":{"title": ""},
            "3513629":{"abstract": ""}
          }})

        responseWithoutHighlights.setApiQuery(new ApiQuery());

        widget.collection.reset({});
        widget.visibleCollection.reset({});

        widget.processResponse(responseWithoutHighlights);

        widget.paginationModel.set({page: 1, perPage : 2});


        //results button should not exist

        expect(widget.view.render().$el.find(".show-details").length).to.eql(0);

      })



      it("should show details (if available) when a user clicks on 'show details'", function () {


        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        widget.visibleCollection.reset(widget.collection.models);


        expect($w.find('.more-info:last').hasClass("hide")).to.equal(true);

        $w.find("button.show-details").click();
        expect($w.find('.more-info:last').hasClass("hide")).to.be.equal(false);
        $w.find("button.show-details").click();
        expect($w.find('.more-info:last').hasClass("hide")).to.be.equal(true);
      });



    })

  });