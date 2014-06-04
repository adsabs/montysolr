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

      var minsub;
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
        done();
      });

      afterEach(function(done) {
        minsub.close();
        var ta = $('#test-area');
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

        var widget = new ResultsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render().el);

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        expect(widget.collection.get("4189917").get("details").highlights[0]).to.eql("External triggers of <em>star</em> formation.");

        expect($w.find('.more-info:last > ul > li:first').html()).to.eql("Diffuse high-energy radiation from regions of massive <em>star</em> formation.");
        done();
      });


    })

  });
