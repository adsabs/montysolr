define(['marionette',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/widgets/results/widget',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/list_of_things/widget'
  ],
  function (Marionette,
    Backbone,
    MinimalPubsub,
    ResultsWidget,
    ApiQuery,
    Test1,
    Test2,
    ListOfThingsWidget) {

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

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

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
        console.log(new ResultsWidget())
        expect(new ResultsWidget()).to.be.instanceof(ResultsWidget);
        expect(new ResultsWidget()).to.be.instanceof(ListOfThingsWidget);
        done();
      });


      it("should join highlights with their records on a model by model basis", function (done) {

        expect(widget.collection.findWhere({"recid": 4189917}).get("details").highlights[0]).to.eql("External triggers of <em>star</em> formation.");
        done();
      });
//
//      it("should show three authors with semicolons in the correct places and, if there are more, show the number of the rest", function(){
//        //$('#test').append($w);
//        debugger;
//        var $parentRow = $($w.find("input[value='2002CeMDA..82..113F']").parents().eq(4))
//
//        expect($parentRow.find("ul.just-authors li:first").text()).to.equal("Fellhauer, M.;");
//        expect($parentRow.find("ul.just-authors li:eq(2)").text()).to.equal("Kroupa, P.");
//        expect($parentRow.find("ul.just-authors").siblings().eq(0).text()).to.equal("and 1 more");
//
//
//      })


    })

  });