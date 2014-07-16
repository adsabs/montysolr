define(['marionette',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/widgets/list_of_things/widget',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/base/paginated_base_widget'
  ],
  function (Marionette,
            Backbone,
            MinimalPubsub,
            ListOfThingsWidget,
            ApiQuery,
            Test1,
            Test2,
            PaginatedBaseWidget) {

    describe("ListOfThings (UI Widget)", function () {

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
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });


      it("returns ListOfThingsWidget object", function(done) {
        expect(new ListOfThingsWidget()).to.be.instanceof(ListOfThingsWidget);
        expect(new ListOfThingsWidget()).to.be.instanceof(PaginatedBaseWidget);
        done();
      });

      it("should consist of a Marionette Controller with a Marionette Composite View as its main view", function (done) {

        expect(new ListOfThingsWidget()).to.be.instanceof(Marionette.Controller);
        expect(new ListOfThingsWidget().view).to.be.instanceof(Marionette.CompositeView);

        done();
      });

      it("hides Load more & Details if there is no data", function(done) {
        var widget = new (ListOfThingsWidget.extend({
          parseResponse: function(apiResponse) {
            var resp = ListOfThingsWidget.prototype.parseResponse.apply(this, arguments);
            _.each(resp, function(model) {
              model['details'] = 'hey';
            });
            return resp;
          }
        }))();
        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render().el);

        expect($w.find('.load-more').hasClass('hide')).to.be.true;
        expect($w.find('.results-controls').hasClass('hide')).to.be.true;

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({q: "star"}));

        expect($w.find('.load-more').hasClass('hide')).to.be.false;
        expect($w.find('.results-controls').hasClass('hide')).to.be.false;

        done();

      });

      it("should listen to INVITING_REQUEST event", function (done) {

        var widget = new (ListOfThingsWidget.extend({
          parseResponse: function(apiResponse) {
            var resp = ListOfThingsWidget.prototype.parseResponse.apply(this, arguments);
            _.each(resp, function(model) {
              model['identifier'] = model.bibcode;
            });
            return resp;
          }
        }))();

        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = widget.render().$el;

        //get widget to request info
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        //find bibcode rendered
        expect($w.find(".identifier").eq(0).text()).to.equal("2013arXiv1305.3460H");


        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        //find new first bib to confirm re-render
        expect($w.find(".identifier").eq(0).text()).to.equal("2006IEDL...27..896K");
        done();
      });

      it("should know to load more results", function (done) {

        var ItemModel = ListOfThingsWidget.prototype.ItemModelClass.extend({
          parse: function(doc) {
            doc['identifier'] = doc.bibcode;
            return doc;
          }
        });

        var CollectionClass = ListOfThingsWidget.prototype.CollectionClass.extend({
          model: ItemModel
        });

        var W = ListOfThingsWidget.extend({
          ItemModelClass: ItemModel,
          CollectionClass: CollectionClass
        });

        var widget = new W({
          displayNum: 6,
          rows: 10,
          maxDisplayNum: 19
        });

        widget.activate(minsub.beehive.getHardenedInstance());

        var $w = $(widget.render().el);
        $('#test').append($w);

        //get widget to request info
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        //we'll have 10 items (4 hidden)
        expect($('.results-item').length).to.be.equal(10);
        expect($('.results-item').not('.hide').length).to.be.equal(6);
        expect($('.results-item').filter('.hide').length).to.be.equal(4);
        expect($(".identifier").eq(0).text()).to.equal("2013arXiv1305.3460H");

        // click on load more
        $(".load-more button").click();

        setTimeout(
          function() {
            //we'll have 20 items (12 hidden)
            expect($('.results-item').length).to.be.equal(20);
            expect($('.results-item').not('.hide').length).to.be.equal(12);
            expect($('.results-item').filter('.hide').length).to.be.equal(8);
            expect($(".identifier").eq(13).text()).to.equal("1978GeoRL...5..294C");

            // click once more (no fetching should happen)
            $(".load-more button").click();

            //we'll have 20 items (2 hidden)
            expect($('.results-item').length).to.be.equal(20);
            expect($('.results-item').not('.hide').length).to.be.equal(18);
            expect($('.results-item').filter('.hide').length).to.be.equal(2);
            expect($(".identifier").eq(17).text()).to.equal("1982LPSC...13..260D");

            // click once more (no fetching should happen and i expect to see 19 (maxAllowed) )
            $(".load-more button").click();
            setTimeout(function() {
              expect($('.results-item').length).to.be.equal(20);
              expect($('.results-item').not('.hide').length).to.be.equal(19);
              expect($('.results-item').filter('.hide').length).to.be.equal(1);
              done()}, 100);
          },
        100);

      });


      it("should show details (if available) when a user clicks on 'show details'", function (done) {

        var widget = new ListOfThingsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        widget.render();

        //$('#test').append(widget.render().el);

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        var $w = $(widget.render().el);

        expect($w.find('.more-info:last').hasClass("hide")).to.equal(true);

        $w.find("button.show-details").click();
        expect($w.find('.more-info:last').hasClass("hide")).to.be.equal(false);
        $w.find("button.show-details").click();
        expect($w.find('.more-info:last').hasClass("hide")).to.be.equal(true);
        done();
      });

      it("should hide detail controls if no record has details", function (done) {

        var changeIt = true;
        var widget = new (ListOfThingsWidget.extend({
          parseResponse: function(apiResponse) {
            var resp = ListOfThingsWidget.prototype.parseResponse.apply(this, arguments);

            _.each(resp, function(model) {
              if (changeIt) {
                delete model['details'];
              }
              else {
                model.details = 'one';
              }
            });

            return resp;
          }
        }))();

        widget.activate(minsub.beehive.getHardenedInstance());
        var $w = $(widget.render().el);

        //$('#test').append(widget.render().el);

        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        expect($w.find('.results-controls').hasClass("hide")).to.equal(true);

        changeIt = false;
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        expect($w.find('.results-controls').hasClass("hide")).to.equal(false);
        done();
      })

    })

  });
