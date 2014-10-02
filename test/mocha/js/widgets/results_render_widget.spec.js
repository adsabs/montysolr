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
  function (Marionette, Backbone, MinimalPubsub, ResultsWidget, ApiQuery, Test1, Test2, ListOfThingsWidget, ApiResponse, ApiQuery) {

    describe("Render Results (UI Widget)", function () {

      var minsub, widget, $w;
      beforeEach(function (done) {

        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return Test2;
            } else {
              Test1.response.start = 10
              return Test1;
            }
          }
        }))({verbose: false});

        widget = new ResultsWidget();

        widget.activate(minsub.beehive.getHardenedInstance());
        $w = $(widget.render().el);

        //prevent infinite requests for data
        widget.collection.requestData = function () {
        };

        done();
      });

      afterEach(function (done) {
        minsub.close();
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });


      it("returns ResultsWidget object", function (done) {
        expect(new ResultsWidget()).to.be.instanceof(ResultsWidget);
        expect(new ResultsWidget()).to.be.instanceof(ListOfThingsWidget);
        done();
      });

      it("should listen to INVITING_REQUEST and automatically request and render data", function () {

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

      it.skip("should show three authors with semicolons in the correct places and, if there are more, show the number of the rest", function () {
        //$('#test').append($w);
        var $parentRow = $($w.find("input[value='2002CeMDA..82..113F']").parents().eq(4))
        //
        expect($parentRow.find("ul.just-authors li:first").text()).to.equal("Fellhauer, M.;");
        expect($parentRow.find("ul.just-authors li:eq(2)").text()).to.equal("Kroupa, P.");
        expect($parentRow.find("ul.just-authors").siblings().eq(0).text()).to.equal("and 1 more");
        //
        //
      })


      it.skip("should show details (if available) when a user clicks on 'show details'", function (done) {
        //
        var widget = new ListOfThingsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        widget.render();
        //
        //$('#test').append(widget.render().el);
        //
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));
        //
        var $w = $(widget.render().el);
        //
        expect($w.find('.more-info:last').hasClass("hide")).to.equal(true);
        //
        $w.find("button.show-details").click();
        expect($w.find('.more-info:last').hasClass("hide")).to.be.equal(false);
        $w.find("button.show-details").click();
        expect($w.find('.more-info:last').hasClass("hide")).to.be.equal(true);
        done();
      });

      it.skip("should hide detail controls if no record has details", function (done) {
        //
        var changeIt = true;
        var widget = new (ListOfThingsWidget.extend({
          parseResponse: function (apiResponse) {
            var resp = ListOfThingsWidget.prototype.parseResponse.apply(this, arguments);
            //
            _.each(resp, function (model) {
              if (changeIt) {
                delete model['details'];
              }
              else {
                model.details = 'one';
              }
            });
            //
            return resp;
          }
        }))();
        //
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render().el);
        //
        //$('#test').append(widget.render().el);
        //
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));
        //
        expect($w.find('.results-controls').hasClass("hide")).to.equal(true);
        //
        changeIt = false;
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));
        //
        expect($w.find('.results-controls').hasClass("hide")).to.equal(false);
        done();
      });

      it.skip("should listen to INVITING_REQUEST event", function (done) {
        //
        var widget = new (ListOfThingsWidget.extend({
          parseResponse: function (apiResponse) {
            var resp = ListOfThingsWidget.prototype.parseResponse.apply(this, arguments);
            _.each(resp, function (model) {
              model['identifier'] = model.bibcode;
            });
            return resp;
          }
        }))();
        //
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = widget.render().$el;
        //
        //get widget to request info
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));
        //
        //find bibcode rendered
        expect($w.find(".identifier").eq(0).text()).to.equal("2013arXiv1305.3460H");
        //
        //
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));
        //
        //find new first bib to confirm re-render
        expect($w.find(".identifier").eq(0).text()).to.equal("2006IEDL...27..896K");
        done();
      });

      it("should render the results button only if highlights exist given the paginated docs", function () {

        var responseWithHighlights = new ApiResponse({
          "responseHeader": {
            "status": 0,
            "QTime": 11,
            "params": {
              "fl": "id",
              "indent": "true",
              "q": "author:accomazzi,a",
              "hl.simple.pre": "<em>",
              "hl.simple.post": "</em>",
              "wt": "json",
              "hl": "true"}},
          "response": {"numFound": 175, "start": 0, "docs": [
            {
              "id": "10406064"},
            {
              "id": "3513629"},
            {
              "id": "5422941"},
          ]
          },
          "highlighting": {
            "10406064": {"title": "fooblydoo"},
            "3513629": {"abstract": ""}
          }})

        responseWithHighlights.setApiQuery(new ApiQuery())

        widget.processResponse(responseWithHighlights);


        //transfer models

        widget.paginationModel.set({page: 1, perPage: 2});

        //expect results button;

        expect(widget.view.render().$el.find(".show-details").length).to.eql(1);


        //pagination showing only 1 record without highlights should hide the button


        widget.paginationModel.set({page: 2, perPage: 1});


        expect(widget.view.render().$el.find(".show-details").length).to.eql(0);


        var responseWithoutHighlights = new ApiResponse({
          "responseHeader": {
            "status": 0,
            "QTime": 11,
            "params": {
              "fl": "id",
              "indent": "true",
              "q": "author:accomazzi,a",
              "hl.simple.pre": "<em>",
              "hl.simple.post": "</em>",
              "wt": "json",
              "hl": "true"}},
          "response": {"numFound": 175, "start": 0, "docs": [
            {
              "id": "10406064"},
            {
              "id": "3513629"},
            {
              "id": "5422941"},
          ]
          },
          "highlighting": {
            "10406064": {"title": ""},
            "3513629": {"abstract": ""}
          }})

        responseWithoutHighlights.setApiQuery(new ApiQuery());

        widget.collection.reset({});
        widget.visibleCollection.reset({});

        widget.processResponse(responseWithoutHighlights);

        widget.paginationModel.set({page: 1, perPage: 2});


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