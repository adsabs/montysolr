define(['marionette',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/widgets/results/widget',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/base/paginated_base_widget'
  ],
  function (Marionette,
            Backbone,
            MinimalPubsub,
            ResultsWidget,
            ApiQuery,
            Test1,
            Test2,
            PaginatedBaseWidget) {

    describe("Render Results (UI Widget)", function () {

      var minsub, widget, $w;
      beforeEach(function(done) {

        minsub = new (MinimalPubsub.extend({
          request: function(apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return Test2;
            } else {
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
        expect(new ResultsWidget()).to.be.instanceof(ResultsWidget);
        expect(new ResultsWidget()).to.be.instanceof(PaginatedBaseWidget);
        done();
      });


      it("should join highlights with their records on a model by model basis", function (done) {

        expect(widget.collection.get("4189917").get("details").highlights[0]).to.eql("External triggers of <em>star</em> formation.");

        expect($w.find('.more-info:last > ul > li:first').html()).to.eql("Diffuse high-energy radiation from regions of massive <em>star</em> formation.");
        done();
      });

      it("should show three authors with semicolons in the correct places and, if there are more, show the number of the rest", function(){
        //$('#test').append($w);
        var $parentRow = $($w.find("input[value='2002CeMDA..82..113F']").parents().eq(4))

        expect($parentRow.find("ul.just-authors li:first").text()).to.equal("Fellhauer, M.;");
        expect($parentRow.find("ul.just-authors li:eq(2)").text()).to.equal("Kroupa, P.");
        expect($parentRow.find("ul.just-authors").siblings().eq(0).text()).to.equal("and 1 more");


      })


    })

  });
