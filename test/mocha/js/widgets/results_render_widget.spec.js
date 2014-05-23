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
      beforeEach(function() {

        minsub = new (MinimalPubsub.extend({
          request: function(apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return Test2;
            } else {
              return Test1;
            }
          }
          }))({verbose: false});
      });

      afterEach(function() {
        minsub.close();
        var ta = $('#test-area');
        if (ta) {
          ta.empty();
        }
      });


      it("returns ResultsWidget object", function() {
        expect(new ResultsWidget()).to.be.instanceof(ResultsWidget);
        expect(new ResultsWidget()).to.be.instanceof(PaginatedBaseWidget);
      });

      it("should consist of a Marionette Controller with a Marionette Composite View as its main view", function () {

        expect(new ResultsWidget()).to.be.instanceof(Marionette.Controller);
        expect(new ResultsWidget().view).to.be.instanceof(Marionette.CompositeView);

      });

      it("hides Load more if there is no data", function() {
        var widget = new ResultsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render());

        expect($w.find('.load-more').css('display')).to.be.equal('none');

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({q: "star"}));

        expect($w.find('.load-more').css('display')).to.be.equal('block');

      });

      it("should listen to INVITING_REQUEST event", function (done) {

        var widget = new ResultsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render());

        //get widget to request info
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        //find bibcode rendered
        expect($w.find(".bib").eq(0).text()).to.equal("2013arXiv1305.3460H");


        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        //find new first bib to confirm re-render
        expect($w.find(".bib").eq(0).text()).to.equal("2006IEDL...27..896K");
        done();
      });

      it("should know to load more results", function (done) {


        var widget = new ResultsWidget({
          displayNum: 6,
          rows: 10,
          maxDisplayNum: 19
        });
        widget.activate(minsub.beehive.getHardenedInstance());

        var $w = $(widget.render());
        $('#test-area').append($w);

        //get widget to request info
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        //we'll have 10 items (4 hidden)
        expect($('.results-item').length).to.be.equal(10);
        expect($('.results-item').not('.hide').length).to.be.equal(6);
        expect($('.results-item').filter('.hide').length).to.be.equal(4);
        expect($(".bib").eq(0).text()).to.equal("2013arXiv1305.3460H");

        // click on load more
        $(".load-more button").click();

        setTimeout(
          function() {
            //we'll have 20 items (12 hidden)
            expect($('.results-item').length).to.be.equal(20);
            expect($('.results-item').not('.hide').length).to.be.equal(12);
            expect($('.results-item').filter('.hide').length).to.be.equal(8);
            expect($(".bib").eq(13).text()).to.equal("1978GeoRL...5..294C");

            // click once more (no fetching should happen)
            $(".load-more button").click();

            //we'll have 20 items (2 hidden)
            expect($('.results-item').length).to.be.equal(20);
            expect($('.results-item').not('.hide').length).to.be.equal(18);
            expect($('.results-item').filter('.hide').length).to.be.equal(2);
            expect($(".bib").eq(17).text()).to.equal("1982LPSC...13..260D");

            // click once more (no fetching should happen and i expect to see 19 (maxAllowed) )
            $(".load-more button").click();
            setTimeout(function() {
              expect($('.results-item').length).to.be.equal(20);
              expect($('.results-item').not('.hide').length).to.be.equal(19);
              expect($('.results-item').filter('.hide').length).to.be.equal(1);
              done()}, 50);
          },
        50);

      });


      it("should join highlights with their records on a model by model basis", function (done) {

        var widget = new ResultsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render());

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        expect(widget.collection.get("4189917").get("highlights")[0]).to.eql("External triggers of <em>star</em> formation.");

        expect($w.find('.more-info:last > ul > li:first').html()).to.eql("Diffuse high-energy radiation from regions of massive <em>star</em> formation.");
        done();
      });




      it("should show highlights (if there are any) when a user clicks on 'show details'", function () {

        var widget = new ResultsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        widget.render();

        //$('#test-area').append(widget.render().el);

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        var v = widget.getView();

        expect(v.$('.more-info:last').hasClass("hide")).to.equal(true);
        v.$("#show-results-snippets").click();
        expect(v.$('.more-info:last').hasClass("hide")).to.be.equal(false);
        v.$("#show-results-snippets").click();
        expect(v.$('.more-info:last').hasClass("hide")).to.be.equal(true);
      })

    })

  });
